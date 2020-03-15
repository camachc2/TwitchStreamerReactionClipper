
package streamerlookup;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Mat;



public class Driver {
    private final int LAPLACE_VARIENCE_CUTOFF = 1000;
    private ImageProcessing imageProcessor = new ImageProcessing();
    private FileManager fileManager = new FileManager();
    private TwitchAPI twitchAPI = new TwitchAPI();
    private UsernameStreamerMap twitchNamesMap = new UsernameStreamerMap();
    private File imageFile = null;
    private String imageText;
    
    //token comes from calling twitchAPI.openUserTokenAuthWebsite()
    private final String USER_TOKEN = "<your token here>"; 
    
    
    public void run()  {    
        if(imageContainsName()){
            investigateName();
        }
                
    }
    
    boolean imageContainsName(){
        imageFile = getFileImageFromClipboard();
        Mat croppedImageMatrix = transformImage();
        return isHighVariance(croppedImageMatrix);
    }
    
    Mat transformImage(){
        Mat laplaceMatrix = imageProcessor.makeLaplacianFrom(imageFile);
        return cropImage(laplaceMatrix);
    }
    
    /** 
     * isHighVariance is true iff the image has large statistical variance in the laplace.
     * An image of text would have a variance that would almost always surpass LAPLCE_VARIENCE_CUTOFF.
     */
    boolean isHighVariance(Mat imageMatrix){
        return imageProcessor.getVarience(imageMatrix) >= LAPLACE_VARIENCE_CUTOFF;
    }
    
    File getFileImageFromClipboard(){
        String path = "StreamerLookup\\Images\\temp\\full.jpg";
        try {
            return Clipboard.tryToGetFileImageFromClipboard(path);
        } catch (UnsupportedFlavorException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, "Cliboard does not contain data to create an image.", ex);
        } catch (IOException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, "Clipboard could not locate the path.", ex);
        }
        
        return new File(path);
    }
    
    Mat cropImage(Mat laplace){
        int totalWidth = laplace.cols(); int totalHeight = laplace.rows();
        FractionalCrop fractionalCrop = new FractionalCrop();
        int x = fractionalCrop.getPixelX(totalWidth);
        int y = fractionalCrop.getPixelY(totalHeight);
        int width = fractionalCrop.getPixelWidth(totalWidth);
        int height = fractionalCrop.getPixelHeight(totalHeight);
        return imageProcessor.cropImage(x, y, width, height, laplace);
    }
    
    void investigateName()  {
        ArrayList<String> originNames = printScreenImageToUsernames();
        
        for(String originName: originNames){            
            if(usernameInDatabase(originName)){
                attemptToClipStreamer(originName);
            }
        }
        
    }
    
    ArrayList<String> printScreenImageToUsernames(){
        ArrayList<String> result = new ArrayList();
        try {
            result.addAll(tryToRetrieveUsernames());
        } catch (UnsupportedFlavorException | IOException | NullPointerException | IllegalArgumentException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    ArrayList<String> tryToRetrieveUsernames() throws UnsupportedFlavorException, IOException{
        NameExtractor extractor = new NameExtractor();
        imageText = extractor.imageToText(imageFile);
        return extractor.scanTextForUsernames(imageText);
    }
    
    boolean usernameInDatabase(String originName){
        return twitchNamesMap.contains(originName.toLowerCase());
    }
    
    void attemptToClipStreamer(String originName) {
        String streamerID = getTwitchUserID(originName);
        clipStreamer(originName, streamerID);
    }
    
    
    String getTwitchUserID(String ingameUsername){
        String result = "";
        try{
            result = tryToGetTwitchUserID(ingameUsername);
        }
        catch(IOException ex){
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    String tryToGetTwitchUserID(String ingameUsername) throws IOException{
                String twitchUsername = twitchNamesMap.getStreamer(ingameUsername.toLowerCase());
                return twitchAPI.getStreamerID(twitchUsername);
    }
    
    void clipStreamer(String originName,String streamerID){
        try{
            tryToClipStreamer(originName, streamerID);
        }
        catch(Exception ex){
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, "Was not able to clip streamer.", ex);
        }
    }
    
    void tryToClipStreamer(String originName, String streamerID) throws IOException{
        String clipURL = twitchAPI.clipStreamerNow(streamerID, USER_TOKEN);
        fileManager.createDirectory(originName, imageFile, clipURL);
    }
    
    
    void makePossibleTTVProfile(){
        if(imageText.toUpperCase().contains("TWITCH") || imageText.toUpperCase().contains("TTV")){
            fileManager.saveImage(imageFile);
        }
    }
    
}
