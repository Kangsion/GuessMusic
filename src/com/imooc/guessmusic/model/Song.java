package com.imooc.guessmusic.model;

public class Song {
   //��������
	private String mSongName;
	
	//�������ļ���
	private String mSongFileName;
	
	//�������ֳ���
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
	//��ȡ�ļ����ַ�����
	public char [] getNameChar(){
		return mSongName.toCharArray();
	}
	
}
