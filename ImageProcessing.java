/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package streamerlookup;

import java.io.File;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author cesar
 */
public class ImageProcessing {
    ImageProcessing(){
        System.load("opencv\\build\\java\\x64\\opencv_java420.dll");
    }
    
    
    public Mat createMatFrom(String path){
        return Imgcodecs.imread(path);
    }
    
    public Mat createMatFrom(File image){
        return Imgcodecs.imread(image.getAbsolutePath());
    }
    
    public void saveMat(Mat m, String path){
        Imgcodecs.imwrite(path, m); 
    }
    
    public Mat makeGrayscaleFrom(String path){
        Mat grey = new Mat();
        Imgproc.cvtColor(createMatFrom(path), grey, Imgproc.COLOR_RGB2GRAY);
        return grey;
    }
    
    public Mat makeGrayscaleFrom(File f){
        Mat grey = new Mat();
        Imgproc.cvtColor(createMatFrom(f), grey, Imgproc.COLOR_RGB2GRAY);
        return grey;
    }
    
    public Mat makeGrayscaleFrom(Mat rbgImage){
        Mat grey = new Mat();
        Imgproc.cvtColor(rbgImage, grey, Imgproc.COLOR_RGB2GRAY);
        return grey;
    }
    
    private static Mat getFilter2DKernel(){
        return new Mat(9,9, CvType.CV_32F) {
                    {
                       put(0,0,0);
                       put(0,1,-1);
                       put(0,2,0);

                       put(1,0-1);
                       put(1,1,4);
                       put(1,2,-1);

                       put(2,0,0);
                       put(2,1,-1);
                       put(2,2,0);
                    }};
    }
    
    public Mat makeFilter2Dfrom(String path){
        Mat f2d = new Mat();
        Mat kernel = getFilter2DKernel();
        Mat grey = makeGrayscaleFrom(path);
        Imgproc.filter2D(grey, f2d, -1, kernel);
        return f2d;
    }
    public Mat makeFilter2Dfrom(File f){
        Mat f2d = new Mat();
        Mat kernel = getFilter2DKernel();
        Mat grey = makeGrayscaleFrom(f);
        Imgproc.filter2D(grey, f2d, -1, kernel);
        return f2d;
    }
    public Mat makeFilter2Dfrom(Mat rgbImage){
        Mat f2d = new Mat();
        Mat kernel = getFilter2DKernel();
        Mat grey = makeGrayscaleFrom(rgbImage);
        Imgproc.filter2D(grey, f2d, -1, kernel);
        return f2d;
    }
    
    
    public Mat makeLaplacianFrom(String path){
        Mat laplace = new Mat();
        Mat grey = makeGrayscaleFrom(path);
        Imgproc.Laplacian(grey, laplace, 3);
        return laplace;
    }
     
    public Mat makeLaplacianFrom(File f){
        Mat laplace = new Mat();
        Mat grey = makeGrayscaleFrom(f);
        Imgproc.Laplacian(grey, laplace, 3);
        return laplace;
    }
    public Mat makeLaplacianFrom(Mat rgbImage){
        Mat laplace = new Mat();
        Mat grey = makeGrayscaleFrom(rgbImage);
        Imgproc.Laplacian(grey, laplace, 3);
        return laplace;
    }
    public Double getVarience(Mat m){
        MatOfDouble median = new MatOfDouble();
        MatOfDouble std = new MatOfDouble();        
        Core.meanStdDev(m, median , std);
        return Math.pow(std.get(0,0)[0], 2);
    }
    
    public Mat cropImage(int x, int y, int width, int length, Mat m){
        Rect roi = new Rect(x, y , width, length);
        return new Mat(m, roi);
        
    }
}
