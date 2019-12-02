
package streamerlookup;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

/**
 *
 * @author cesar
 */
public class UsernameStreamerMap {
	private String path = "<path for serialized map of originNames - twitchUsername>";
    HashMap<String, String> hmap;
    
    public UsernameStreamerMap(){
        this.hmap = new HashMap<>();
        fillMap();
    }
    
    String getStreamer(String username){
        return hmap.get(username);
    }
    
    boolean contains(String key){
        return hmap.containsKey(key);
    }
    
    private void fillMap(){
        try
        {
           FileInputStream fis = new FileInputStream(path);
           ObjectInputStream ois = new ObjectInputStream(fis);
           hmap = (HashMap) ois.readObject();
           ois.close();
           fis.close();
        }catch(IOException ioe)
        {
           ioe.printStackTrace();
           return;
        }catch(ClassNotFoundException c)
        {
           System.out.println("Class not found");
           c.printStackTrace();
           return;
        }
    }
}
