
package streamerlookup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;

/**
 *
 * @author cesar
 */
public class NameExtractor {
    private final String OCR_KEY = "<your ocr key>";
    private final String URL = "https://api.ocr.space/parse/image"; 
    private String charset = "UTF-8";
    MultipartUtility multipart = new MultipartUtility(URL, charset, OCR_KEY);
    
    NameExtractor() throws IOException{
        addHTTPRequestFields();
    }
    
       private void addHTTPRequestFields(){
        addHeaders();
        addPOSTParameters();
    }
    private void addHeaders(){
        multipart.addHeaderField("User-Agent", "Mozilla/5.0");
        multipart.addHeaderField("Accept-Language", "en-US,en;q=0.5");
    }
    
    private void addPOSTParameters(){
        multipart.addFormField("apikey", OCR_KEY);
        multipart.addFormField("isOverlayRequired", "false");
        multipart.addFormField("language", "eng");
        multipart.addFormField("filetype", "JPG");
        multipart.addFormField("scale", "true");
        multipart.addFormField("OCREngine", "2");
    }
    
    
    public ArrayList<String> scanTextForUsernames(String response){        
        ArrayList<String> result = new ArrayList();        
        String[] lines = response.split("\n");
        
        for (String line : lines) {
            String[] words = line.split(" ");
            if(line.startsWith("ELIMINATED ")){             
                result.add(extractUsernameFromLine(words, 1, words.length));
            }   //eliminations generate the text "ELIMINATION <name>"
            else if(line.startsWith("ASSIST, ELIMINATION")){
                result.add(extractUsernameFromLine(words, 2, words.length));
            }   //assists generate the text "ASSIST, ELIMINATION <name>" 
            else if(line.startsWith("Hold E Access ")){     
                result.add(extractUsernameFromLine(words, 3, words.length-1));
            }   //access crate generates the text "Hold E Access <name> Items" 
            else if(line.startsWith("KNOCKED DOWN ")){      
                result.add(extractUsernameFromLine(words, 2, words.length));
            }   //KNOCKED DOWN generates the text "KNOCKED DOWN <name>" 
        }
        return result;            
    }
    
    //imageToText API removes underscores, this function adds them back
    String extractUsernameFromLine(String[] words, int start, int end){
        String result= "";
        if(words.length > start){
            result += words[start];
        }
        for(int i = start+1; i < end; i++){
            result  += "_"+words[i]; 
        }
        return result;
    }
    
    public String imageToText(File imageFile) throws IOException{
        if(imageFile == null){
            return "";
        }
        multipart.addFilePart("file", imageFile);
        String response = multipart.finish().toString();
        return jsonToText(response);
    }
    
    private String jsonToText(String json){
        JSONArray wholeArr = new JSONArray(json);
        JSONArray parseResultArr = new JSONArray(wholeArr.getJSONObject(0).get("ParsedResults").toString());
        return parseResultArr.getJSONObject(0).getString("ParsedText");
    }
    
    
}
