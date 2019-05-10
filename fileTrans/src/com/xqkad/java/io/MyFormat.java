package com.xqkad.java.io;

import com.xqkad.filetrans.R;

public class MyFormat {
	public static final String[] 
		Images 		= {".png",".jpg",".jpeg",".gif",".bmp"},
		Audios 		= {".wav",".mp3",".ogg",".midi"},
		Videos 		= {".mp4",".flv",".3gp",".rmvb",".avi"},
		Packages 	= {".apk"},
		Texts 		= {".txt"},
		WebTexts 	= {".htm",".html",".js",".jsp",".xml"},
		Words 		= {".doc",".docx"},
		Excels 		= {".xls",".xlsx"},
		Pdfs 		= {".pdf"},
		Rars 		= {".rar",".zip",".gz"},
		PPTs 		= {".ppt",".pptx"};
	
	public static final int
		DIR 	= 0,
		IMAGE 	= 1,
		AUDIO	= 2,
		VIDEO 	= 3,
		APK 	= 4,
		TXT 	= 5,
		HTML 	= 6,
		WORD 	= 7,
		EXCEL 	= 8,
		PDF 	= 9,
		RAR 	= 10,
		PPT 	= 11;
	
	
	public static boolean checkFormat(String filename,
			String[] formats) {
		for (String fmt : formats) {
			if (filename.endsWith(fmt))
				return true;
		}
		return false;
	}
	
	public static int getFormat(String name){
		int format = -1;
		
		if(checkFormat(name, Images)){
			format = IMAGE;
		}else if(MyFormat.checkFormat(name, MyFormat.Videos)){
			format = VIDEO;
        }else if(MyFormat.checkFormat(name, MyFormat.Audios)){
        	format = AUDIO;
        }else if(MyFormat.checkFormat(name, MyFormat.WebTexts)){
        	format = HTML;
        }else if(MyFormat.checkFormat(name, MyFormat.Texts)){
        	format = TXT;
        }else if(MyFormat.checkFormat(name, MyFormat.Packages)){
        	format = APK;
        }else if(MyFormat.checkFormat(name, MyFormat.Words)){
        	format = WORD;
        }else if(MyFormat.checkFormat(name, MyFormat.Excels)){
        	format = EXCEL;
        }else if(MyFormat.checkFormat(name, MyFormat.Pdfs)){
        	format = PDF;
        }else if(MyFormat.checkFormat(name, MyFormat.PPTs)){
        	format = PPT;
        }else if(MyFormat.checkFormat(name, MyFormat.Rars)){
        	format = RAR;
        }
		
		return format;
	}
}
