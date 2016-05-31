package com.ribomation.droidAtScreen.matching;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.cmd.Command;
import com.ribomation.droidAtScreen.dev.ScreenImage;
import com.ribomation.droidAtScreen.gui.DeviceFrame;
import com.ribomation.droidAtScreen.model.Configuration;
import com.ribomation.droidAtScreen.util.OpenCvUtils;

public class TemplateMatching{
	

	private final static Logger logger = LoggerFactory.getLogger(TemplateMatching.class);
	
	protected void matching(Application app, DeviceFrame device, String searchName) {
		System.out.println("\nRunning Template Matching");
		ScreenImage sourceImg = device.getLastScreenshot().copy();
		int match_method = Imgproc.TM_CCOEFF;
		
		String temp = Command.setKonLoad(searchName);
		
		if (device.getName() != "127.0.0.1"){
			sourceImg = sourceImg.rotate();
		}
		
		BufferedImage _image = sourceImg.toBufferedImage();

        // Convert the camera image and template image to the same type. This
        // is required by the cvMatchTemplate call.
        
		//BufferedImage template = ImageUtils.convertBufferedImage(template, BufferedImage.TYPE_BYTE_GRAY);
        //BufferedImage image = ImageUtils.convertBufferedImage(_image, BufferedImage.TYPE_BYTE_GRAY);

        //Mat templateMat = OpenCvUtils.toMat(template);
		Mat imageMat = OpenCvUtils.toMat(_image);
		//Mat imageMat = Highgui.imread("C:/eunsebi/image/test.jpg");
		
        Mat templateMat = Highgui.imread(temp.substring(1, temp.length()));
        //Mat resultMat = new Mat();
        
        // / Create the result matrix
        int result_cols = imageMat.cols() - templateMat.cols() + 1;
		int result_rows = imageMat.rows() - templateMat.rows() + 1;
		Mat resultMat = new Mat(result_rows, result_cols, CvType.CV_32FC1);
        
        Imgproc.matchTemplate(imageMat, templateMat, resultMat, Imgproc.TM_CCOEFF_NORMED);

        Mat debugMat = null;
        if (logger.isDebugEnabled()) {
            debugMat = imageMat.clone();
        }

        MinMaxLocResult mmr = Core.minMaxLoc(resultMat);
        double maxVal = mmr.maxVal;

        // TODO: Externalize?
        double threshold = 0.7f;
        double corr = 0.85f;

        double rangeMin = Math.max(threshold, corr * maxVal);
        double rangeMax = maxVal;
        
        org.opencv.core.Point matchLoc;
		if (match_method == Imgproc.TM_SQDIFF || match_method == Imgproc.TM_SQDIFF_NORMED) {
			matchLoc = mmr.minLoc;
		} else {
			matchLoc = mmr.maxLoc;
		}
		
        if (logger.isDebugEnabled()) {
        	if (rangeMin > 0.8) {
        		Core.rectangle(debugMat, matchLoc,
        				new org.opencv.core.Point(matchLoc.x + templateMat.cols(), matchLoc.y + templateMat.rows()),
        				new Scalar(255,0,0));
        		System.out.println("찾았다");
        	} else {
        		Core.rectangle(debugMat, matchLoc,
        				new org.opencv.core.Point(matchLoc.x + templateMat.cols(), matchLoc.y + templateMat.rows()),
        				new Scalar(0,0,255));
        	}
            //Core.rectangle(debugMat, matchLoc, new org.opencv.core.Point(matchLoc.x + templateMat.cols(), matchLoc.y + templateMat.rows()), new Scalar(0, 255, 0));
            
            Core.putText(debugMat, "" + rangeMin,
            		new org.opencv.core.Point(matchLoc.x + templateMat.cols(), matchLoc.y + templateMat.rows()),
                    Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(255,255,255));
        }
        
        System.out.println("rangeMin : " + rangeMin);
        System.out.println("rangeMax : " + rangeMax);

        long t = System.currentTimeMillis();
        saveDebugImage(t + "_0_template", templateMat);
        /*saveDebugImage(t + "_1_camera", imageMat);
        saveDebugImage(t + "_2_result", resultMat);*/
        saveDebugImage(t + "_3_debug", debugMat);

        //return matches;
	}
	
	protected void saveDebugImage(String name, Mat mat) {
        if (logger.isDebugEnabled()) {
            try {
                BufferedImage debugImage = OpenCvUtils.toBufferedImage(mat);
                File file = Configuration.get().createResourceFile(TemplateMatching.class,
                        name + "_", ".png");
                ImageIO.write(debugImage, "PNG", file);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

}
