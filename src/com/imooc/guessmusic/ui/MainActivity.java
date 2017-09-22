package com.imooc.guessmusic.ui;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.imooc.guessmusic.R;
import com.imooc.guessmusic.data.Const;
import com.imooc.guessmusic.model.IAlertDialogButtonListener;
import com.imooc.guessmusic.model.Song;
import com.imooc.guessmusic.model.WordButton;
import com.imooc.guessmusic.model.WordButtonListener;
import com.imooc.guessmusic.ui.MyGridView.MyGridAdptar;
import com.imooc.guessmusic.util.Hanzi;
import com.imooc.guessmusic.util.MyPlayer;
import com.imooc.guessmusic.util.Util;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Loader.ForceLoadContentObserver;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;


public class MainActivity extends Activity implements WordButtonListener{
     //��Ƭ��ض���
	private Animation mPanAnim;
	private LinearInterpolator mPanLin;
	
	private Animation mBarInAnim;
	private LinearInterpolator mBarInLin;
	
	private Animation mBarOutAnim;
	private LinearInterpolator mBarOutLin;
	
	private ImageButton mBtnPan;
	private ImageView mViewPan;
	private ImageView mViewPanBar;
    
	private boolean isRunning=false;
	
	//��������
	private ArrayList<WordButton> mAllWords;
	
	//��ѡ������
	private ArrayList<WordButton> mWordSelected;
	
	private LinearLayout mWordSelectedContainer;
	
	private MyGridView myGridView;
	
	//��ǰ������
	private Song mCurrentSong;
	
	//�ؿ�����
	private int mCurrentStageIndex=-1;
	
	//����ȷ
	private final static int STATUS_ANSERR_RIGHT=1;
	
	//�𰸴���
	private final static int STATUS_ANSERR_WRONG=2;
	
	//�𰸲�����
	private final static int STATUS_ANSERR_LACK=3;
	
	//��ǰ��ҵ�����
	private int mCurrentCoins=Const.TOTAL_COINS;
	private TextView myCoins;
	
	//���ؽ���
	private LinearLayout mPassView;
	
	private TextView mCurrentSongNamePassView;
	
	private TextView mCurrentStagePassView;
	
	private ImageButton BtnNext;
	
	private ImageButton BtnWeixin;
	
	private TextView mCurrentStageView;
	
	private final static int ID_DIALOG_DELETE_WORD=1;
	
	private final static int ID_DIALOG_TIP_ANSWER=2;
	
	private final static int ID_DIALOG_LACK_COINS=3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//��ʼ������
		initAnim();
		initView();
		
		//���ֿ�
		initCurrentStageData();
		//ɾ������
		handleDeleteWord();
		
		//��ʾ��
		handleTipAnswer();
		
		//���Ŷ���������
		handleButton();
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		mBtnPan=(ImageButton) findViewById(R.id.btn_play_start);
		mViewPan=(ImageView) findViewById(R.id.imageView1);
		mViewPanBar=(ImageView) findViewById(R.id.imageView2);
		myGridView=(MyGridView) findViewById(R.id.gridview);
		
		mCurrentStageView=(TextView)findViewById(R.id.text_current_stage);
		
		//���ý�ҵ�����
		myCoins=(TextView) findViewById(R.id.text_bar_coin);
		myCoins.setText(mCurrentCoins+"");
		myGridView.setWoredButtonListener(this);
		mWordSelectedContainer=(LinearLayout) findViewById(R.id.word_select_container);
		mBtnPan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handleButton();
				
			}

			
		});
	}
	//������Ű�ť
	private void handleButton() {
		// TODO Auto-generated method stub
		if (mViewPanBar != null) {
		if(!isRunning)
		{
		  isRunning=true;
		  mViewPanBar.startAnimation(mBarInAnim);
		  mBtnPan.setVisibility(View.INVISIBLE);
		  //��������
		  MyPlayer.PlaySong(MainActivity.this, mCurrentSong.getmSongFileName());
		}
		}
	}
	//��ʼ������Ч��	
	private void initAnim() {
		// TODO Auto-generated method stub
		mPanAnim=AnimationUtils.loadAnimation(this,R.anim.roate);
		mPanLin=new LinearInterpolator();
		mPanAnim.setInterpolator(mPanLin);
		mPanAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mViewPanBar.startAnimation(mBarOutAnim);
			}
		});
		
		
		mBarInAnim=AnimationUtils.loadAnimation(this,R.anim.roate_45);
		mBarInLin=new LinearInterpolator();
		mBarInAnim.setFillAfter(true);
		mBarInAnim.setInterpolator(mBarInLin);
        mBarInAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
			
				mViewPan.startAnimation(mPanAnim);
				
			}
		});

		mBarOutAnim=AnimationUtils.loadAnimation(this,R.anim.roate_d_45);
		mBarOutLin=new LinearInterpolator();
		mBarOutAnim.setFillAfter(true);
		mBarOutAnim.setInterpolator(mBarOutLin);
		mBarOutAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				isRunning = false;
            	mBtnPan.setVisibility(View.VISIBLE);
			}
		});
	}
	//��ȡ��������Ϣ
	private Song loadStageSongInfo(int stageIndex)
	{
		Song song=new Song();
		String [] stage=Const.SONG_INFO[stageIndex];
		song.setmSongName(stage[Const.INDEX_SONG_NAME]);
		song.setmSongFileName(stage[Const.INDEX_FILE_NAME]);
		return song;
	}
    //��ʼ�����ֿ�
	 private void initCurrentStageData()
	 {
		 //��ȡ��ǰ�صĸ�����Ϣ
		 mCurrentSong=loadStageSongInfo(++mCurrentStageIndex);
				 
		 mWordSelected=initWordSelect();
		 LayoutParams params=new LayoutParams(80,80);
		 
		 //��մ�
	     mWordSelectedContainer.removeAllViews();
		 //�����µĴ�
		 for(int i=0;i<mWordSelected.size();i++)
		 {
			 mWordSelectedContainer.addView(mWordSelected.get(i).mViewButton, params);
		 }
		 
		 //��ǰ�ص�����
		 if(mCurrentStageView!=null)
		 mCurrentStageView.setText((mCurrentStageIndex+1)+"");
		 
		 mAllWords=initAllWord();
		 myGridView.updateData(mAllWords);
	 }
	 //�������д�ѡ����
	 private String [] generateWords()
	 {
		 String [] words=new String[MyGridView.COUNTS_WORDS];
		 Random random=new Random();
		 //�������
		 for(int i=0;i<mCurrentSong.getmNameLength();i++)
		 {
			 words[i]=mCurrentSong.getNameChar()[i]+"";
			 
		 }
		 //��ȡ������ִ�������
		 for(int i=mCurrentSong.getmNameLength();i<myGridView.COUNTS_WORDS;i++)
		 {
			 words[i]=Hanzi.RandomChar()+"";
		 }
		 //��������˳��
		 for(int i=myGridView.COUNTS_WORDS-1;i>=0;i--)
		 {
			 
			 int index=random.nextInt(i+1);
			 String buf=words[index];
			 words[index]=words[i];
			 words[i]=buf;
		 }
		return words;
		 
	 }
	 //��ʼ���������ֿ�
	 private ArrayList<WordButton> initAllWord()
	 {
		 ArrayList<WordButton> data=new ArrayList<WordButton>();
		 //��ȡ���д�ѡ����
		 String [] words=generateWords();
		 for(int i=0;i<MyGridView.COUNTS_WORDS;i++)
		 {
			 WordButton button=new WordButton();
			 button.mWordString=words[i];
			 data.add(button);
			 
		 }
		 
		return data;
		 
	 }
	 //��ʼ����ѡ���ֿ�
	 private ArrayList<WordButton> initWordSelect()
	 {
		 ArrayList<WordButton> data=new ArrayList<WordButton>();
		 for(int i=0;i<mCurrentSong.getmNameLength();i++)
		 {
		    View view=Util.getView(MainActivity.this, R.layout.self_ui_gridview_item);
			final WordButton holder=new WordButton();
			holder.mViewButton=(Button) view.findViewById(R.id.item_btn);
			holder.mViewButton.setTextColor(Color.WHITE);
			holder.mViewButton.setText("");
			holder.mViewButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					clearTheAnswer(holder);
				}
			});
			holder.mIsVisible=false;
			holder.mViewButton.setBackgroundResource(R.drawable.game_wordblank);
			data.add(holder);
		 }
		return data;
	 }
	@Override
	public void OnClick(WordButton button) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "sadasd", Toast.LENGTH_SHORT).show();
		if(button!=null)
		{
		setSelectWord(button);
		//��ȡ����״̬
	    int answer=checkTheAnswer();
	    switch(answer)
	    {
	    case STATUS_ANSERR_RIGHT:
	    	handlePassEvent();
	    	break;
	    case STATUS_ANSERR_WRONG:
	    	sparkword();
	    	break;
	    case STATUS_ANSERR_LACK:
	    	for(int i=0;i<mWordSelected.size();i++)
			{
				mWordSelected.get(i).mViewButton.setTextColor(Color.WHITE);
			}
	    	break;
	    }
		}
	}
	//��ѡ��Button���뵽��ѡ��
	private void setSelectWord(WordButton wordButton) {
		// TODO Auto-generated method stub
		for(int i=0;i<mWordSelected.size();i++)
		{
			if(mWordSelected.get(i).mWordString.length()==0)
			{
				
				mWordSelected.get(i).mWordString=(wordButton.mWordString);
				mWordSelected.get(i).mIsVisible=true;
				mWordSelected.get(i).mViewButton.setText(wordButton.mWordString);
				mWordSelected.get(i).mIndex=wordButton.mIndex;
				//���ÿɼ���
				setButtonVisible(wordButton,View.INVISIBLE);
				break;
			}
		}
	}
	//����Button�Ŀɼ���
	private void setButtonVisible(WordButton wordButton, int invisible) {
		// TODO Auto-generated method stub
		 wordButton.mViewButton.setVisibility(invisible);
		 wordButton.mIsVisible=invisible==View.VISIBLE?true:false;
	}
	//�����ѡ���Button
    private void clearTheAnswer(WordButton wordButton)
    {
    	wordButton.mViewButton.setText("");
    	wordButton.mWordString="";
    	wordButton.mIsVisible=false;
    	setButtonVisible(mAllWords.get(wordButton.mIndex), View.VISIBLE);
    }
    //�����Ƿ���ȷ
    private int checkTheAnswer()
    {
    	//�ȼ���������
    	for(int i=0;i<mWordSelected.size();i++)
    	{
    		if(mWordSelected.get(i).mWordString.length()==0)
    		{
    			return STATUS_ANSERR_LACK;
    		}
    	}
    	//�������������ȷ��
    	StringBuffer sb=new StringBuffer();
    	for(int i=0;i<mWordSelected.size();i++)
    	{
    		sb.append(mWordSelected.get(i).mWordString);
    	}
    	return (sb.toString().equals(mCurrentSong.getmSongName()))?
    			STATUS_ANSERR_RIGHT:STATUS_ANSERR_WRONG;
    }
    //������˸
    private void sparkword()
    {
    	TimerTask task=new TimerTask() {
    		boolean change=false;
        	int mSpardTimes=0;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(++mSpardTimes>6)
							return;
						
						for(int i=0;i<mWordSelected.size();i++)
						{
							mWordSelected.get(i).mViewButton.setTextColor(change==true?Color.RED:Color.WHITE);
						}
						change=!change;
					}
					
				});
				
			}
		};
    	Timer timer=new Timer();
    	timer.schedule(task, 1,150);
        
    }
    
    
    //����ɾ����ѡ�����¼�
    private void handleDeleteWord()
    {
    	ImageButton button=(ImageButton) findViewById(R.id.delete_btn);
    	button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//deleteOneWord();
				showConfirmDialog(ID_DIALOG_DELETE_WORD);
			}
		});
    }
    
    //����������
    private boolean handleCoins(int data)
    {
    	if(mCurrentCoins+data>=0)
    	{
    		mCurrentCoins+=data;
    		myCoins.setText(mCurrentCoins+"");
    		return true;
    	}
    	else{ return false;}
    }
    //�������ļ���ȡһЩ��������Ҫ�Ľ��
    private int getDeleteWordCoins()
    {
    	return this.getResources().getInteger(R.integer.pay_delete_word);
    }
    private int getTipWordCoins()
    {
    	return this.getResources().getInteger(R.integer.pay_tip_answer);
    }
    
    //ɾ������
    private void deleteOneWord()
    {
    	//���ٽ��
    	if(!handleCoins(-getDeleteWordCoins()))
    	{
    		//��Ҳ�������ʾ�Ի���
    		showConfirmDialog(STATUS_ANSERR_LACK);
    		return;
    	}
    	//�����������Ӧ��WordButton���ɼ�
    	setButtonVisible(findNotAnswerWord(), View.INVISIBLE);
    }
    //�ҵ�һ�����Ǵ𰸵��ļ������ҵ�ǰ�ǿɼ���
    private WordButton findNotAnswerWord()
    {
    	Random random=new Random();
    	WordButton buf=null;
    	
    	while(true)
    	{
    		int index=random.nextInt(MyGridView.COUNTS_WORDS);
    		
    		buf=mAllWords.get(index);
    		if(buf.mIsVisible&&!isTheAnswerWord(buf))
    		{
    			return buf;
    		};
    	}
    }
    //������ʾ���¼�
    private void handleTipAnswer()
    {
    	
    	ImageButton button=(ImageButton) findViewById(R.id.tip_btn);
    	button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			   showConfirmDialog(ID_DIALOG_TIP_ANSWER);	
				//tipAnswer();
			}
		});
    }
    
    //�ҵ�һ����ʾ��
    private WordButton findAnswerWord(int index)
    {
    	WordButton buf=null;
    	for(int i=0;i<MyGridView.COUNTS_WORDS;i++)
    	{
    		buf=mAllWords.get(i);
    		if(buf.mWordString.equals(""+mCurrentSong.getNameChar()[index]))
    		{
    			return buf;
    		}
    	}
		return null;
    	
    }
    //�Զ�ѡ��һ����
    private void tipAnswer()
    {
    	//���ٽ��
    	if(!handleCoins(-getTipWordCoins()))
    	{
    		//��Ҳ�������ʾ�Ի���
    		showConfirmDialog(STATUS_ANSERR_LACK);
    		return;
    	}
    	boolean tipWord=false;
    	for(int i=0;i<mWordSelected.size();i++)
    	{
    		if(mWordSelected.get(i).mWordString.length()==0)
    		{
    			OnClick(findAnswerWord(i));
    			
    			tipWord=true;
    			
    			if(!tipWord)
    	    	{
    	    		sparkword();
    	    	}
    			
    			return;
    		}
    	}
    	
    }
	private boolean isTheAnswerWord(WordButton wordButton) {
		// TODO Auto-generated method stub
		
		for (int i = 0; i < mCurrentSong.getmNameLength(); i++) {
			if ((mCurrentSong.getNameChar()[i] + "").equals(wordButton.mWordString
					)) {
				return true;
			}
		}
		return false;
	}
	
	//��������¼�
    private void handlePassEvent()
    {
    	//���ؽ���
    	mPassView=(LinearLayout) findViewById(R.id.pass_view);
    	mPassView.setVisibility(View.VISIBLE);
    	
    	//ֹͣ����
    	mViewPan.clearAnimation();
    	
    	//ֹͣ����
    	MyPlayer.StopSong(MainActivity.this);
    	
    	//��ǰ�ؿ�����
    	if(mCurrentStagePassView==null)
    	{
    		mCurrentStagePassView=(TextView) findViewById(R.id.text_current_stage_pass);
    		mCurrentStagePassView.setText((mCurrentStageIndex+1)+"");
    	}
    	//��ǰ��������
    	if(mCurrentSongNamePassView==null)
    	{
    		mCurrentSongNamePassView=(TextView) findViewById(R.id.text_current_song_name_pass);
    		mCurrentSongNamePassView.setText(mCurrentSong.getmSongName());
    	}
    	
    	//������һ��
    	BtnNext=(ImageButton) findViewById(R.id.btn_next);
    	BtnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(judeAppPassed())
				{
					//����ͨ�ؽ���
				    Util.startActivity(MainActivity.this, AllPassView.class);
				}else{
					//��ʼ��һ��
					mPassView.setVisibility(View.INVISIBLE);
					MyPlayer.PlayTone(MainActivity.this, MyPlayer.INDEX_TONE_COIN);
					initCurrentStageData();
					//���Ŷ���������
					handleButton();
				}
			}

			
		});
    }
    private boolean judeAppPassed() {
		// TODO Auto-generated method stub
		return (mCurrentStageIndex==Const.SONG_INFO.length-1);
	}

    
    
    
    /**
     *    ��ʾ�Ի���-----------------------------------------------------------------------
     */
    
    //�����¼���Ӧ
    private IAlertDialogButtonListener mBtnOkDeleteWordListener=new IAlertDialogButtonListener() {
		
		@Override
		public void onClick() {
			// TODO Auto-generated method stub
			deleteOneWord();
		}
	};
    private IAlertDialogButtonListener mBtnOkTipAnswerListener=new IAlertDialogButtonListener() {
		
		@Override
		public void onClick() {
			// TODO Auto-generated method stub
			tipAnswer();
		}
	};
	private IAlertDialogButtonListener mListener=new IAlertDialogButtonListener() {
		
		@Override
		public void onClick() {
			// TODO Auto-generated method stub
			
		}
	};
	private void showConfirmDialog(int id)
	{
		switch (id) {
		case ID_DIALOG_DELETE_WORD:
			Util.showDialog(MainActivity.this, "ȷ������"+getDeleteWordCoins()+
					"�����ȥ��һ�������", mBtnOkDeleteWordListener);
			break;
		case ID_DIALOG_LACK_COINS:
			Util.showDialog(MainActivity.this, "��Ҳ��㣬���ֵ", mListener);
			break;
		case ID_DIALOG_TIP_ANSWER:
			Util.showDialog(MainActivity.this, "ȷ������"+getTipWordCoins()+
					"����ҵõ�һ����ʾ��", mBtnOkTipAnswerListener);
	        break;
		default:
			break;
		}
	}
	
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MyPlayer.StopSong(MainActivity.this);
	}
}
