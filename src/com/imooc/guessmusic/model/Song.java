package com.imooc.guessmusic.model;

public class Song {
   //歌曲名字
	private String mSongName;
	
	//歌曲的文件名
	private String mSongFileName;
	
	//歌曲名字长度
	private int mNameLength;

	public String getmSongName() {
		return mSongName;
	}

	public void setmSongName(String SongName) {
		this.mSongName = SongName;
		
		this.mNameLength=SongName.length();
	}

	public String getmSongFileName() {
		return mSongFileName;
	}

	public void setmSongFileName(String mSongFileName) {
		this.mSongFileName = mSongFileName;
	}

	public int getmNameLength() {
		return mNameLength;
	}

	public void setmNameLength(int mNameLength) {
		this.mNameLength = mNameLength;
	}
	//获取文件名字符数组
	public char [] getNameChar(){
		return mSongName.toCharArray();
	}
	
}
