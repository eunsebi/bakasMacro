package com.ribomation.droidAtScreen.matching;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;

public class FeatureMatching {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.load("C:/eunsebi/opencv/build/java/x86/opencv_java2413.dll");
		String filename1 = "C:\\eunsebi\\Image\\penguin1.png";
		String filename2 = "C:\\eunsebi\\Image\\penguin2.png";

		int ret;
		ret = compareFeature(filename1, filename2);

		if (ret > 0) {
			System.out.println("Two images are same.");
		} else {
			System.out.println("Two images are different");
		}

	}

	private static int compareFeature(String filename1, String filename2) {
		// TODO Auto-generated method stub
		int retVal = 0;
		long startTime = System.currentTimeMillis();

		Mat img1 = Highgui.imread(filename1, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		Mat img2 = Highgui.imread(filename2, Highgui.CV_LOAD_IMAGE_GRAYSCALE);

		MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
		MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
		Mat descriptors1 = new Mat();
		Mat descriptors2 = new Mat();

		FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
		DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);

		detector.detect(img1, keypoints1);
		detector.detect(img2, keypoints2);

		extractor.compute(img1, keypoints1, descriptors1);
		extractor.compute(img2, keypoints2, descriptors2);

		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorExtractor.BRIEF);

		MatOfDMatch matches = new MatOfDMatch();

		if (descriptors2.cols() == descriptors1.cols()) {
			matcher.match(descriptors1, descriptors2, matches);

			DMatch[] match = matches.toArray();
			double max_dist = 0;
			double min_dist = 100;

			for (int i = 0; i < descriptors1.rows(); i++) {
				double dist = match[i].distance;
				if (dist < min_dist)
					min_dist = dist;
				if (dist > max_dist)
					max_dist = dist;
			}

			System.out.println("max_dist=" + max_dist + ", min_dist=" + min_dist);

			for (int i = 0; i < descriptors1.rows(); i++) {
				if (match[i].distance <= 10) {
					retVal++;
				}
			}
			System.out.println("matching count=" + retVal);
		}

		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("estimatedTime=" + estimatedTime + "ms");

		return retVal;
	}

}
