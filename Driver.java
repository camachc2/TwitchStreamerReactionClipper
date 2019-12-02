
package streamerlookup;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Driver {
    private FileManager fileManager = new FileManager();
    private TwitchAPI twitchAPI = new TwitchAPI();
    private UsernameStreamerMap twitchNamesMap = new UsernameStreamerMap();
    File imageFile = null;
    String response;
    
    //token comes from calling twitchAPI.openUserTokenAuthWebsite()
    private final String USER_TOKEN = "<your own User token>";  //you need to log in
    
    public void run() throws IOException {
        ArrayList<String> originNames = printScreenImageToUsernames();
        
        for(String originName: originNames){
            System.out.println(originName);
            if(twitchNamesMap.contains(originName.toLowerCase())){
                String twitchUsername = twitchNamesMap.getStreamer(originName.toLowerCase());
                System.out.println("twitch username: "+twitchUsername);
                String streamerID = twitchAPI.getStreamerID(twitchUsername);
                String clipURL = twitchAPI.clipStreamerNow(streamerID, USER_TOKEN);
                fileManager.createProfile(twitchUsername, imageFile, clipURL);
            }
        }
        
        if(response.toUpperCase().contains("TWITCH") || response.toUpperCase().contains("TTV")){
            fileManager.saveImage(imageFile);
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
        imageFile = fileManager.tryToGetImageFromClipboard();
        response = extractor.imageToText(imageFile);
        return extractor.scanTextForUsernames(response);
    }
}
