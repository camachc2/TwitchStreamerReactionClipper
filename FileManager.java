
package streamerlookup;

import java.awt.Image;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/**
 *
 * @author cesar
 */
public class FileManager {
    private final String IMAGE_PATH = "<your image path folder>";
    private final int LAST_N_SIZE= 10;
    
    //When theres nothing on the cliboard it will throw a java.lang.IllegalArgumentException: image == null!
    public File tryToGetImageFromClipboard() throws UnsupportedFlavorException, IOException{
        Image image = Clipboard.getImage();
        String path = IMAGE_PATH+"\\LastNScreenshots\\"+getDate()+".jpg";
        File imageFile = new File(path);
        ImageIO.write((BufferedImage) image, "jpg", imageFile);
        removeOldestNFilesInDir(IMAGE_PATH+"\\LastNScreenshots\\");
        return imageFile;
    }
    
    
    public void createProfile(String username, File imageFile, String data) throws IOException{
        String folderPath = IMAGE_PATH+getDate()+"_"+username+"\\";
        makeUsernameDirectory(folderPath);
        imageFile.renameTo(new File(folderPath+username+".jpg"));
        File file = new File(folderPath+username+".txt");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(data);
        fileWriter.flush();
        fileWriter.close();
    }
    
    private void makeUsernameDirectory(String newFolderPath){
        File file = new File(newFolderPath);
        file.mkdirs();
    }
    
    private void removeOldestNFilesInDir(String path){
        File dir = new File(path);
        File[] files = dir.listFiles();
        
        sortByLastModified(files);
        
        //keeps the latest N files
        for(int i = LAST_N_SIZE; i < files.length; i++){
            files[i].delete();
        }
    }
    
    private void sortByLastModified(File[] files){
        Arrays.sort(files, (File f1, File f2) -> {
            return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
        });
    }
    
    private String getDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd HH_mm_ss");  
        LocalDateTime now = LocalDateTime.now(); 
        return dtf.format(now);
    }
    
    public void saveImage(File file){
       file.renameTo(new File(IMAGE_PATH+"\\Possible Streamers\\"+getDate()+".jpg"));
    }
}
