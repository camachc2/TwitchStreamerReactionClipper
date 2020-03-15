
package streamerlookup;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Clipboard {
    public static Image getImage() throws UnsupportedFlavorException, IOException{
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)){
          return (Image) transferable.getTransferData(DataFlavor.imageFlavor);
        }
        else{
          return null;
        }
    }
    
    public static File tryToGetFileImageFromClipboard(String path) throws UnsupportedFlavorException, IOException{
        Image image = Clipboard.getImage();
        File imageFile = new File(path);
        ImageIO.write((BufferedImage) image, "jpg", imageFile);
        return imageFile;
    }
    
    
    public static void saveClipboard(String path) throws UnsupportedFlavorException, IOException{
        Image image = Clipboard.getImage();
        File imageFile = new File(path);
        ImageIO.write((BufferedImage) image, "jpg", imageFile);
    }
    
}
