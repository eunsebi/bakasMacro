package com.ribomation.droidAtScreen.cmd;

import java.awt.Point;
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
import com.ribomation.droidAtScreen.dev.AndroidDevice;
import com.ribomation.droidAtScreen.dev.ScreenImage;
import com.ribomation.droidAtScreen.gui.DeviceFrame;
import com.ribomation.droidAtScreen.model.Configuration;
import com.ribomation.droidAtScreen.util.OpenCvUtils;

@SuppressWarnings("serial")
public class TemplateMatchingCommand extends CommandWithTarget<DeviceFrame>{

	private int delay = 1000;
	private boolean problem;
	private Logger log;
	private Thread runner;
	private DeviceFrame device;
	
	
	/*static {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }*/
	
	public TemplateMatchingCommand(DeviceFrame deviceFrame) {
		super(deviceFrame);
		this.log = LoggerFactory.getLogger(AndroidDevice.class.getName() + ":" + deviceFrame.getName());
		//log.debug(String.format("DeviceFrame(device=%s)", device));
		updateButton(deviceFrame);
	}
	
	@Override
	protected void doExecute(Application app, DeviceFrame deviceFrame) {
		deviceFrame.setTemplateMaching(!deviceFrame.isTemplateMatching());
		System.out.println("isTemplate : " + deviceFrame.isTemplateMatching());
		System.out.println("1");
		updateButton(deviceFrame);
		device = deviceFrame;
		Runnable r = new Macro();
		runner = new Thread(r);
		runner.start();
		//System.out.println("\nRunning Template Matching");
	}

	protected void updateButton(DeviceFrame deviceFrame) {
		setIcon(deviceFrame.isTemplateMatching() ? "Stop" : "Start");
		System.out.println("2");
		setTooltip(String.format("Bakas Macro is [%s]. (Useful for ZTE Blade devices)", deviceFrame.isUpsideDown() ? "Start" : "Stop"));
		
	}
	
	public class CompareInfo {
		public boolean match;
		public double x;
		public double y;

		public CompareInfo (boolean match, double x, double y) {
			this.match = match;
			this.x = x;
			this.y = y;
		}
	}
	
	public static class Adv
    {
        public static int[] world = new int[10];
        public static int[] stage = new int[10];

        public static int[] cmbAdvWorld1 = new int[10];
        public static int[] cmbAdvStage1 = new int[10];

        public static int[] cmbAdvWorld2 = new int[10];
        public static int[] cmbAdvStage2 = new int[10];

        public static int[] cmbAdvWorld3 = new int[10];
        public static int[] cmbAdvStage3 = new int[10];

        public static int[] world_select = new int[10];
        public static int[] stage_select = new int[10];

        public static int progressSelected = -1;

        public static String[] worldName = { "1라우스트룸", "2성곽", "3황금궁전", "4안식의광장", "5도라도대장간",
            "6바덴항구", "7철의관문", "8사령의제단", "9고대의지하통로","10아벨의은신처",
            "11라우스트룸", "12성곽", "13황금궁전", "14안식의광장", "15도라도대장간",
            "16바덴항구", "17철의관문", "18사령의제단", "19고대의지하통로","20아벨의은신처"
        };
        //public static string[] worldName = { "1신비의숲", "2침묵의광산", "3화염의사막", "4암흑의무덤", "5용의유적지", "6눈보라의대지",
        //    "7복수자의지옥",  "8벚꽃의항구", "8수행의길", "8하늘의문", "8안개의섬", "9끝없는성벽", "9풍요의시장", "9붉은협곡",
        //    "10신선의봉우리", "10절대자의궁궐", "11어둠의안식처1", "11어둠의안식처2", "12그림자의눈" };

        public static int[][] stageX = {
            { 95, 235, 485, 705, 910, 1120, 0, 0, 0, 0 },
            //2
            { 640, 919, 1181, 460, 705, 965, 0, 0, 0, 0 },
            //3
            { 640, 934, 1191, 490, 750, 1210, 0, 0, 0, 0 },
            //4
            { 640, 902, 1112, 270, 500, 745, 790, 1060, 0, 0 },
            //5
            { 640, 886, 1112, 185, 390, 590, 800, 1035, 0, 0 },
            //6
            { 640, 813, 1044, 330, 580, 775, 1020, 520, 0, 0 },
            //7
            { 640, 881, 1082, 485, 690, 555, 805, 315, 0, 0 },
            //8
            { 640, 911, 1064, 450, 690, 940, 1190, 520, 780, 1060 },
            //9
            { 640, 903, 1166, 490, 765, 785, 1045, 415, 620, 815 },
            //10
            { 640, 855, 1034, 350, 585, 840, 970, 430, 420, 480 },
            //11
            { 640, 887, 1109, 340, 550, 790, 0, 0, 0, 0 },
            //12
            { 640, 917, 1181, 540, 785, 1040, 0, 0, 0, 0 },
            //13
            { 640, 934, 1191, 390, 650, 920, 0, 0, 0, 0 },
            //14
            { 640, 902, 1111, 305, 535, 780, 825, 325, 0, 0 },
            //15
            { 640, 885, 1110, 250, 460, 655, 865, 130, 0, 0 },
            //16
            { 640, 815, 1045, 385, 635, 830, 1075, 520, 0, 0 },
            //17
            { 640, 882, 1081, 375, 585, 445, 700, 180, 0, 0 },
            //18
            { 640, 911, 1162, 465, 710, 960, 1210, 595, 855, 1130 },
            //19
            { 640, 904, 1165, 370, 640, 660, 920, 425, 630, 820 },
            //20
            { 640, 854, 1034, 375, 610, 865, 995, 595, 855, 1130 } };
        public static int[][] stageY = {
            { 355, 490, 460, 490, 460, 365, 0, 0, 0, 0 },
            //2
            { 288, 257, 315, 430, 405, 370, 0, 0, 0, 0 },
            //3
            { 416, 369, 318, 410, 355, 405, 0, 0, 0, 0 },
            //4
            { 386, 362, 472, 305, 270, 245, 430, 405, 0, 0 },
            //5
            { 357, 291, 231, 425, 510, 465, 415, 380, 0, 0 },
            //6
            { 444, 323, 285, 275, 335, 460, 470, 435, 0, 0 },
            //7
            { 498, 417, 285, 220, 300, 455, 460, 390, 0, 0 },
            //8
            { 424, 383, 324, 340, 400, 430, 405, 400, 390, 375 },
            //9
            { 342, 391, 392, 240, 245, 425, 425, 490, 245, 405 },
            //10
            { 354, 465, 327, 385, 290, 365, 215, 265, 455, 430 },
            //11
            { 489, 461, 488, 460, 365, 320, 0, 0, 0, 0 },
            //12
            { 288, 258, 315, 430, 400, 365, 0, 0, 0, 0 },
            //13
            { 415, 368, 316, 415, 350, 400, 0, 0, 0, 0 },
            //14
            { 385, 363, 475, 305, 270, 240, 425, 400, 0, 0 },
            //15
            { 360, 290, 235, 425, 510, 465, 415, 375, 0, 0 },
            //16
            { 440, 320, 285, 275, 330, 465, 470, 435, 0, 0 },
            //17
            { 500, 414, 285, 220, 300, 450, 460, 385, 0, 0 },
            //18
            { 422, 378, 322, 340, 395, 430, 405, 395, 385, 370 },
            //19
            { 340, 390, 390, 240, 245, 420, 420, 345, 245, 400 },
            //20
            { 353, 463, 328, 380, 290, 360, 210, 395, 385, 370 } };

    }
	
	class Macro implements Runnable {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			log.debug("Running Bakas Macro");
			
			int repeat=0;
			
			MAIN:
				try {
					if (!work(3, "Screen_main", -1, -1)) {
						
						while (work(3, "comm/Button_back", 0, 0));
					}
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			ADV:
			try {
				// 메뉴 출력
				targetClick(350, 20);
				
				// 모험지역 찾기
				work(0, "Screen_adv", 0, 0);
				
				if (work(2, "adv/Screen_adv_map", -1, -1)) {
					// 18지역 선택
					targetClick(706, 132);
						Thread.sleep(1000);
					// 18-3지역 선택
					targetClick(1164, 328);
				}
				
				// 준비하기
				work(0, "comm/Button_ready", 0, 0);
				
				// 모험 준비화면
				if (work(2, "adv/Screen_adv_ready", -1, -1)) {
					// 버프 확인
					
					// 보상확인
					//while (work(2, "comm/Button_reward", 0, 0));
					
					// 모험 시작 클릭
					if (work(2, "adv/Screen_adv_ready", -1, -1))
						targetClick(920, 660);
				}
				
					
				// 반복 루프
				for (int i = 0; i < 5; i++) {
					//소지품 가득
					System.out.println("반복 횟수 : " +i);
					// 전투중
					if (work(2, "comm/battle", -1, -1)) {
						log.debug("전투중");
						for (;;) {
							
							//System.out.println("전투중 무한 루프");
							// 성공화면
							if (work(3, "adv/Screen_adv_sucess", -1, -1, false)) {
								log.debug("모험 성공");
									Thread.sleep(delay);
								
								// 다시하기 클릭
								work(2, "comm/Button_restart", 0, 0);
								
								// 모험 준비화면(루프 완료시)
								//work(3, "adv/Screen_adv_ready", -1, -1);
								break;
							}
								
							if (work(3, "adv/Screen_adv_fail", -1, -1, false)) {
								log.debug("모험 실패");
								
								// 다시하기 클릭
								work(2, "comm/Button_restart", 0, 0);
								
								// 모험 준비화면(루프 완료시)
								//work(3, "adv/Screen_adv_ready", -1, -1);
								break;
							}
							
						} // 전투중 무한 루프
					}
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Adv end
				
			RAID:
				try {
					if (!work(3, "Screen_main", -1, -1)){
						targetClick(350, 20);
						
						while (work(3, "comm/Button_back", 0, 0))
					        Thread.sleep(delay);
					}
				
					// 메뉴판
					targetClick(350, 20);
					
					if (!work(3, "Screen_partyRaid", 0, 0))
		            {
		                targetScroll(1190, 325, 730, 325);
		                targetScroll(1190, 325, 730, 325);
		                Thread.sleep(delay);
		
		                //Work(0, "파티레이드", 0, 0);
		                while (!work(3, "Screen_partyRaid", 0, 0))
		                {
		                    targetScroll(1190, 325, 730, 325);
		                    Thread.sleep(delay);
		                }
		
		            }
					
					if (work(2, "raid/Screen_raid_waiting", -1, -1))
		            {
		                //var d = Fun_Raid.cmbRadWorld.ToString();
		                /*if (d.Equals("테레프라유적지"))
		                	targetClick(235, 585);
		                    //Work(2, "테레프라유적지", 0, 0);
		
		                if (d.Equals("켈레시스사원"))
		                	targetClick(1010, 305);
		                    //Work(2, "켈레시스사원", 0, 0);
		
		                if (d.Equals("키메라둥지"))*/
		                	targetClick(1000, 570);
		                    //Work(2, "키메라둥지", 0, 0);
		
		                /*if (d.Equals("진행안함"))
		                {
		                    work(3, "뒤로가기", 0, 0);
		                    Thread.sleep(delay);
		                    goto dual;
		                }*/
		            }
					
					//for (int i = 0; i < Fun_Raid.numRadCount; i++)
					for (int i = 0; i < 5; i++)
		            {
		                // 레이드 시작
		                repeat = i;
		                // 준비화면
		                if (work(2, "raid/Screen_raid_arrangements", -1, -1))
		                {
		                    Thread.sleep(delay);
		                    if (work(3, "Screen_notice", -1, -1))
		                    {
		                    	work(0, "comm/Button_confirm", 0, 0);
		                    	Thread.sleep(delay);
		
		                        targetClick(1040, 545);
		                        Thread.sleep(delay);
		
		                        work(0, "comm/Button_confirm", 0, 0);
		                        Thread.sleep(delay);
		
		                        //시작하기 클릭
		                        //Work(0, "레이드_준비화면", 1000, 650);
		                    }
		
		                    Thread.sleep(delay);
		                    /*if (work(3, "레이드_보상받기", 1045, 540))
		                    {
		                    	Thread.sleep(delay);
		                        work(3, "레이드_보상확인", 0, 0);
		                    }*/
		
		                    // 준비화면 이면 시작하기 클릭
		                    work(3, "raid/Screen_raid_arrangements", 1000, 650);
		
		                    Thread.sleep(delay);
		
		                    // 소지품 가득 일단 계속 진행
		                    /*if (work(3, "레이드_소지품가득", -1, -1))
		                    {
		                        if (chkInvenEmpty.Checked)
		                        {
		                        	targetClick(745, 510);
		                            bagEmpty();
		
		                            work(0, "레이드_준비화면", 1000, 650);
		                            Thread.sleep(delay);
		                        }
		                        else
		                        {
		                        	targetClick(550, 510);
		                        }
		                    }*/
		
		                    if (work(2, "raid/Screen_quickStart", -1, -1))
		                    {
		
		                        //if (Fun_Raid.cmbRaidcontinue.Equals("바로시작"))
		                        	work(3, "raid/Button_quickStart", 0, 0, false);
		                        /*else
		                        	work(3, "내영웅", 0, 0, false);*/
		
		                        // 스태미나 부족  
		                        /*if (work(3, "레이드_스태미나부족", -1, -1))
		                        {
		                            if (Fun_Raid.cmbRadStamina.Equals("구매안함"))
		                            {
		                            	targetClick(555, 510);
		                                break;
		                            }
		                            else
		                            {
		                                LogAdd("스태미나 구입");
		                                targetClick(720, 510);
		                            }
		
		                        }*/
		                    }
		                    Thread.sleep(delay * 2);
		
		                    //Thread.Sleep(Sys.delay);
		
		                    //while (Work(2, "모험_로딩중", -1, -1))
		                    //    LogAdd("로딩중!!!!!");
		
		                    //LogAdd("  진입중");
		
		                    //Stats.raid++;
		                    //statsNotify();
		
		                    if (work(2, "comm/battle", -1, -1) || work(2, "raid/Screen_raid_sucess", -1, -1, false))
		                    {
		                        log.debug("     << 레이드중 >>");
		                        //Thread.Sleep(Sys.delay);
		
		                        Thread.sleep(delay * 2);
		                        targetClick(1110, 390);
		
		                        for (;;)
		                        {
		                            if (work(3, "raid/Screen_raid_sucess", -1, -1, false))
		                            {
		                                //Stats.adv_susess++;
		                                //statsNotify();
		                            	log.debug("  레이드 성공!!!");
		                                work(3, "raid/Button_raid_exit", 0, 0);
		                                Thread.sleep(delay * 8);
		                                break;
		                            }
		
		                            if (work(3, "raid/Screen_raid_arrangements", -1, -1, false))
		                            {
		                                //Stats.adv_susess++;
		                                //statsNotify();
		                                log.debug("  레이드 실패!!!");
		                                //Work(1, "레이드_나가기", 0, 0);
		                                Thread.sleep(delay * 8);
		                                break;
		                            }
		
		                        } // 무한루프 endFor
		                    } // 전투중 endif
		                } // 대기화면 endif
		
		            } //반복횟수 endFor
		
		            /*if (Fun_PushBullet.chkPushChoice3)
		            {
		                if (repeat == 0)
		                    Fun_PushBullet.bulletBody = "파티 레이드 모드 " + modeChar + " 스태미나가 부족하여 완료하지 못했습니다.";
		                else
		                    Fun_PushBullet.bulletBody = "파티 레이드 모드 " + modeChar + "캐릭터 " + repeat + "회 완료";
		                pushBulletStream();
		            }*/
		            repeat = 0;
		
		            log.debug("레이드 모드 완료");
		
		            if (!problem) Thread.sleep(delay);
		
		            if (work(2, "raid/Screen_raid_arrangements", -1, -1))
		            {
		                while (work(3, "comm/Button_back", 0, 0))
		                    Thread.sleep(delay * 3);
		            }
				
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        //goto adv:
		}
		
	}
	
	public void targetClick(double x, double y) {
		Point p = new Point();
		p = new Point(
				(int) x,
				(int) y
		);
		
		device.tap(p);
	}
	
	public void targetClick(int x, int y) {
		Point p = new Point();
		p = new Point(
				(int) x,
				(int) y
		);
		
		device.tap(p);
	}
	
	private void targetScroll(int x1, int y1, int x2, int y2) {
		// TODO Auto-generated method stub
		Point p1 = new Point();
		Point p2 = new Point();
		
		p1 = new Point(
				(int) x1,
				(int) y1
		);
		
		p2 = new Point(
				(int) x2,
				(int) y2
		);
		device.scroll(p1, p2);
		
	}
	
	public boolean work(int mode, String name, int clickX, int clickY) throws InterruptedException {
		return work(mode, name, clickX, clickY, true, 0.1);
	}
	
	public boolean work(int mode, String name, int clickX, int clickY, boolean capture) throws InterruptedException {
		return work(mode, name, clickX, clickY, capture, 0.1);
	}
	
	public boolean work(int mode, String name, int clickX, int clickY, boolean capture, double tolerance) throws InterruptedException {
		// mode = 0 => 1, mode = 1 => 1, mode = 2 => 3, mode = 3 => 3
		//log.debug("Work: "+ name );
		boolean matched = false;
		
		if (problem)
			mode = mode | 1;
		
		for (int i = 0; i < 5*60*1000 / (delay == 0 ? 1 : delay); i++) {
			if (((mode & 1) == 0 && ! problem) || matched) {
					Thread.sleep(delay);
			}

			CompareInfo info = imageCompare(name);
			log.debug(i + "  " + (info.match ? "발견됨  " : "탐색중  ") + name);
			
			if (info.match) {
				problem=false;
				
				if (clickX >= 0 && (!matched || (i % 20 == 0))){
					/*System.out.println("좌표");
					Point p = new Point(
							(int) info.x ,
							(int) info.y
							);
					System.out.println("x : " + p.x);
					System.out.println("y : " + p.y);*/
					//device.tap(p);
					//targetClick(info.x, info.y);
					targetClick((clickX == 0) ? info.x : clickX, (clickY == 0) ? info.y : clickY);
					matched = true;
					
				}
                if((mode & 2) == 2){
                	//System.out.println("발견  Mode 2,3번");
                	return true;
                }
                //System.out.println("발견 Mode 0번");
			} else {
				if (matched)
                {
                    // 못찾을때 0번
                    //System.out.println("몾찾았다  Mode 0번");
                    return true;
                }
                if ((mode & 1) == 1 || (mode & 4) == 4)
                {
                    // 1번, 3번, 4번
                    //System.out.println("몾찾았다  Mode 1,2,3번");
                    return false;
                }
                //System.out.println("미발견  0,1,2번");
			}
			
			/*System.out.println("info : " + info.x);
			System.out.println("info : " + info.y);*/
			
			
		} // for End
		
		return false;
	}
	
	@SuppressWarnings("unused")
	public CompareInfo imageCompare(String searchName) {
		
		ScreenImage sourceImg = device.getLastScreenshot().copy();
		int match_method = Imgproc.TM_CCOEFF;
		
		String ext = "png";
		
		String name = (TemplateMatchingCommand.class.getResource("/").getPath()+"KonImg/" + searchName + "." + ext.toLowerCase()).substring(1);
		
		//String name = setKonLoad(searchName);
		//System.out.println("name : " + name);
		
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
		
		Mat templateMat = Highgui.imread(name);
		//Mat resultMat = new Mat();
		
		// / Create the result matrix
		int result_cols = imageMat.cols() - templateMat.cols() + 1;
		int result_rows = imageMat.rows() - templateMat.rows() + 1;
		Mat resultMat = new Mat(result_rows, result_cols, CvType.CV_32FC1);
		
		Imgproc.matchTemplate(imageMat, templateMat, resultMat, Imgproc.TM_CCOEFF_NORMED);

		Mat debugMat = null;
		if (log.isDebugEnabled()) {
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
		
		/*System.out.println("rangeMin : " + rangeMin);
		System.out.println("rangeMax : " + rangeMax);
		System.out.println("min x : " + matchLoc.x);
		System.out.println("min y : " + matchLoc.y);*/

		//log.debug("매칭율 : " + rangeMin);
		
		long t = System.currentTimeMillis();
		
		if (log.isDebugEnabled()) {
			if (rangeMin > 0.8) {
				Core.rectangle(debugMat, matchLoc,
						new org.opencv.core.Point(matchLoc.x + templateMat.cols(), matchLoc.y + templateMat.rows()),
						new Scalar(255,0,0));
				Core.putText(debugMat, "" + rangeMin,
			    		new org.opencv.core.Point(matchLoc.x + templateMat.cols(), matchLoc.y + templateMat.rows()),
			            Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(255,255,255));
				
				//System.out.println("찾았다");
				//log.debug(searchName + " 찾았다");
				
				// 이미지 저장
				//saveDebugImage(searchName + "_3_찾았다", debugMat);
				
				return new CompareInfo(true, matchLoc.x, matchLoc.y);
				
			} else {
				Core.rectangle(debugMat, matchLoc,
						new org.opencv.core.Point(matchLoc.x + templateMat.cols(), matchLoc.y + templateMat.rows()),
						new Scalar(0,0,255));
				Core.putText(debugMat, "" + rangeMin,
			    		new org.opencv.core.Point(matchLoc.x + templateMat.cols(), matchLoc.y + templateMat.rows()),
			            Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(255,255,255));

				//log.debug(searchName + " 못 찾았다");
				
				//saveDebugImage(t + "_0_template", templateMat);
				//saveDebugImage(t + "_1_camera", imageMat);
				//saveDebugImage(t + "_2_result", resultMat);
				//saveDebugImage("_3_debug", debugMat);
				return new CompareInfo(false, 0, 0);

			}
		    //Core.rectangle(debugMat, matchLoc, new org.opencv.core.Point(matchLoc.x + templateMat.cols(), matchLoc.y + templateMat.rows()), new Scalar(0, 255, 0));
		}
		
		//saveDebugImage(t + "_0_template", templateMat);
		//saveDebugImage(t + "_1_camera", imageMat);
		//saveDebugImage(t + "_2_result", resultMat);
		//saveDebugImage(t + "_3_debug", debugMat);

		return new CompareInfo(false, 0, 0);

	}

	protected void saveDebugImage(String name, Mat mat) {
        if (log.isDebugEnabled()) {
            try {
                BufferedImage debugImage = OpenCvUtils.toBufferedImage(mat);
                File file = Configuration.get().createResourceFile(TemplateMatchingCommand.class,
                        name + "_", ".png");
                ImageIO.write(debugImage, "PNG", file);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
}
