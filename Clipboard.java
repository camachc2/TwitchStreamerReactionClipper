
package streamerlookup;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

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
}
