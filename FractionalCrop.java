/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package streamerlookup;

/**
 *
 * @author cesar
 */
public class FractionalCrop {
    private double frac_x = 0.421875;
    private double frac_y = 0.703704;
    private double frac_width = 0.104167;
    private double frac_height = 0.0370370;
    
    FractionalCrop(){}
    
    FractionalCrop(double frac_x, double frac_y, double frac_width, double frac_height){
        this.frac_x = frac_x;
        this.frac_y = frac_y;
        this.frac_width = frac_width;
        this.frac_height = frac_height;
    }
    
    int getPixelX(int windowWidth){
        return (int) Math.ceil(windowWidth*frac_x);
    }
    
    int getPixelY(int windowHeight){
        return (int) Math.ceil(windowHeight*frac_y);
    }
    
    int getPixelWidth(int windowWidth){
        return (int) Math.ceil(windowWidth*frac_width);
    }
    
    int getPixelHeight(int windowHeight){
        return (int) Math.ceil(windowHeight*frac_height);
    }
    
}
