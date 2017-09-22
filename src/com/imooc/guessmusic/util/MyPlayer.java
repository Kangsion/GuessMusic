package com.imooc.guessmusic.util;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

public class MyPlayer {
	public static int INDEX_TONE_CANCEL=0;
	
	public static int INDEX_TONE_COIN=1;
	
	public static int INDEX_TONE_ENTER=2;
	
	//“Ù¿÷
   private static MediaPlayer mediaPlayer;
   
   private static String [] SONG_NAMES={"cancel.mp3","coin.mp3","enter.mp3"};
   
   //“Ù–ß
   private static MediaPlayer [] mToneMediaPlayer=new MediaPlayer[SONG_NAMES.length];
   
   public static void PlayTone(Context context,int Index)
   {
	   if(mToneMediaPlayer[Index]==null)
	   {
		   mToneMediaPlayer[Index]=new MediaPlayer();
		   
		   AssetManager assetManager=context.getAssets();
		   try {
			AssetFileDescriptor FileDescriptor=assetManager.openFd(SONG_NAMES[Index]);
			mToneMediaPlayer[Index].setDataSource(FileDescriptor.getFileDescriptor(), FileDescriptor.getStartOffset()
					, FileDescriptor.getLength());
			
			mToneMediaPlayer[Index].prepare();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	   mToneMediaPlayer[Index].start();
	   
	   
   }
   
   
   public static void PlaySong(Context context,String filename)
   {
	   if(mediaPlayer==null)
	   {
		   mediaPlayer=new MediaPlayer();
	   }
	   mediaPlayer.reset();
	   
	   AssetManager assetManager=context.getAssets();
	   try {
		AssetFileDescriptor FileDescriptor=assetManager.openFd(filename);
		mediaPlayer.setDataSource(FileDescriptor.getFileDescriptor(), FileDescriptor.getStartOffset()
				, FileDescriptor.getLength());
		
		mediaPlayer.prepare();
		mediaPlayer.start();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
   public static void StopSong(Context context)
   {
	   if(mediaPlayer!=null)
	   {
		   mediaPlayer.stop();
	   }
   }
}
