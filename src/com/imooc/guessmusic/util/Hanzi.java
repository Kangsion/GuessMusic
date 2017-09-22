package com.imooc.guessmusic.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class Hanzi {
	//随机生成一个汉字
   public static char RandomChar()
   {
	  String str="";
	  int hightPos;
	  int lowPos;
	  
	  Random random=new Random();
	  hightPos=(176+Math.abs(random.nextInt(39)));
	  lowPos=(161+Math.abs(random.nextInt(93)));
	  
	  byte[] b=new byte[2];
	  b[0]=(Integer.valueOf(hightPos)).byteValue();
	  b[1]=(Integer.valueOf(lowPos)).byteValue();
	  
	  try {
		str=new String(b,"GBK");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   return str.charAt(0); 
	  
   }
}
