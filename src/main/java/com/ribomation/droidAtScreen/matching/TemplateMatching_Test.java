package com.ribomation.droidAtScreen.matching;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

class MatchingDemo {

	public void run(String inFile, String templateFile, String outFile, int match_method) {
		System.out.println("\nRunning Template Matching");

		Mat img = Highgui.imread(inFile);
		Mat templ = Highgui.imread(templateFile);

		// / Create the result matrix
		int result_cols = img.cols() - templ.cols() + 1;
		int result_rows = img.rows() - templ.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

		// / Do the Matching and Normalize
		Imgproc.matchTemplate(img, templ, result, match_method);
		Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());

		// / Localizing the best match with minMaxLoc
		MinMaxLocResult mmr = Core.minMaxLoc(result);

		Point matchLoc;
		if (match_method == Imgproc.TM_SQDIFF || match_method == Imgproc.TM_SQDIFF_NORMED) {
			matchLoc = mmr.minLoc;
		} else {
			matchLoc = mmr.maxLoc;
		}

		// / Show me what you got
		Core.rectangle(img, matchLoc, new Point(matchLoc.x + templ.cols(), matchLoc.y + templ.rows()), new Scalar(0, 255, 0));

		// Save the visualized detection.
		System.out.println("Writing " + outFile);
		System.out.println("MinLoc X : " + matchLoc.x);
		System.out.println("MinLoc Y : " + matchLoc.y);
		System.out.println("MaxLoc X : " + mmr.maxLoc.x);
		System.out.println("MaxLoc Y : " + mmr.maxLoc.y);
		Highgui.imwrite(outFile, img);

	}
}

public class TemplateMatching_Test {
	public static void main(String[] args) {
		//System.loadLibrary("opencv_java2413");
		System.load("C:/eunsebi/opencv/build/java/x86/opencv_java2413.dll");
		new MatchingDemo().run("C:/eunsebi/image/misskorea.jpg", "C:/eunsebi/image/template.jpg", "C:/eunsebi/image/templatematch.jpg", Imgproc.TM_CCOEFF);
	}
}
