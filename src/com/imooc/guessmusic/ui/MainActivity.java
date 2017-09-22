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
     //唱片相关动画
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
	
	//文字容器
	private ArrayList<WordButton> mAllWords;
	
	//已选择容器
	private ArrayList<WordButton> mWordSelected;
	
	private LinearLayout mWordSelectedContainer;
	
	private MyGridView myGridView;
	
	//当前歌曲类
	private Song mCurrentSong;
	
	//关卡索引
	private int mCurrentStageIndex=-1;
	
	//答案正确
	private final static int STATUS_ANSERR_RIGHT=1;
	
	//答案错误
	private final static int STATUS_ANSERR_WRONG=2;
	
	//答案不完整
	private final static int STATUS_ANSERR_LACK=3;
	
	//当前金币的数量
	private int mCurrentCoins=Const.TOTAL_COINS;
	private TextView myCoins;
	
	//过关界面
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
		//初始化动画
		initAnim();
		initView();
		
		//文字框
		initCurrentStageData();
		//删除文字
		handleDeleteWord();
		
		//提示答案
		handleTipAnswer();
		
		//播放动画和音乐
		handleButton();
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		mBtnPan=(ImageButton) findViewById(R.id.btn_play_start);
		mViewPan=(ImageView) findViewById(R.id.imageView1);
		mViewPanBar=(ImageView) findViewById(R.id.imageView2);
		myGridView=(MyGridView) findViewById(R.id.gridview);
		
		mCurrentStageView=(TextView)findViewById(R.id.text_current_stage);
		
		//设置金币的数量
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
	//点击播放按钮
	private void handleButton() {
		// TODO Auto-generated method stub
		if (mViewPanBar != null) {
		if(!isRunning)
		{
		  isRunning=true;
		  mViewPanBar.startAnimation(mBarInAnim);
		  mBtnPan.setVisibility(View.INVISIBLE);
		  //播放音乐
		  MyPlayer.PlaySong(MainActivity.this, mCurrentSong.getmSongFileName());
		}
		}
	}
	//初始化动画效果	
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
	//获取歌曲的信息
	private Song loadStageSongInfo(int stageIndex)
	{
		Song song=new Song();
		String [] stage=Const.SONG_INFO[stageIndex];
		song.setmSongName(stage[Const.INDEX_SONG_NAME]);
		song.setmSongFileName(stage[Const.INDEX_FILE_NAME]);
		return song;
	}
    //初始化文字框
	 private void initCurrentStageData()
	 {
		 //读取当前关的歌曲信息
		 mCurrentSong=loadStageSongInfo(++mCurrentStageIndex);
				 
		 mWordSelected=initWordSelect();
		 LayoutParams params=new LayoutParams(80,80);
		 
		 //清空答案
	     mWordSelectedContainer.removeAllViews();
		 //增加新的答案
		 for(int i=0;i<mWordSelected.size();i++)
		 {
			 mWordSelectedContainer.addView(mWordSelected.get(i).mViewButton, params);
		 }
		 
		 //当前关的索引
		 if(mCurrentStageView!=null)
		 mCurrentStageView.setText((mCurrentStageIndex+1)+"");
		 
		 mAllWords=initAllWord();
		 myGridView.updateData(mAllWords);
	 }
	 //生成所有待选文字
	 private String [] generateWords()
	 {
		 String [] words=new String[MyGridView.COUNTS_WORDS];
		 Random random=new Random();
		 //存入歌名
		 for(int i=0;i<mCurrentSong.getmNameLength();i++)
		 {
			 words[i]=mCurrentSong.getNameChar()[i]+"";
			 
		 }
		 //获取随机文字存入数组
		 for(int i=mCurrentSong.getmNameLength();i<myGridView.COUNTS_WORDS;i++)
		 {
			 words[i]=Hanzi.RandomChar()+"";
		 }
		 //打乱文字顺序
		 for(int i=myGridView.COUNTS_WORDS-1;i>=0;i--)
		 {
			 
			 int index=random.nextInt(i+1);
			 String buf=words[index];
			 words[index]=words[i];
			 words[i]=buf;
		 }
		return words;
		 
	 }
	 //初始化所有文字框
	 private ArrayList<WordButton> initAllWord()
	 {
		 ArrayList<WordButton> data=new ArrayList<WordButton>();
		 //获取所有待选文字
		 String [] words=generateWords();
		 for(int i=0;i<MyGridView.COUNTS_WORDS;i++)
		 {
			 WordButton button=new WordButton();
			 button.mWordString=words[i];
			 data.add(button);
			 
		 }
		 
		return data;
		 
	 }
	 //初始化待选文字框
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
		//获取答题状态
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
	//待选框Button加入到已选框
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
				//设置可见性
				setButtonVisible(wordButton,View.INVISIBLE);
				break;
			}
		}
	}
	//设置Button的可见性
	private void setButtonVisible(WordButton wordButton, int invisible) {
		// TODO Auto-generated method stub
		 wordButton.mViewButton.setVisibility(invisible);
		 wordButton.mIsVisible=invisible==View.VISIBLE?true:false;
	}
	//清除已选框的Button
    private void clearTheAnswer(WordButton wordButton)
    {
    	wordButton.mViewButton.setText("");
    	wordButton.mWordString="";
    	wordButton.mIsVisible=false;
    	setButtonVisible(mAllWords.get(wordButton.mIndex), View.VISIBLE);
    }
    //检查答案是否正确
    private int checkTheAnswer()
    {
    	//先检查答案完整性
    	for(int i=0;i<mWordSelected.size();i++)
    	{
    		if(mWordSelected.get(i).mWordString.length()==0)
    		{
    			return STATUS_ANSERR_LACK;
    		}
    	}
    	//答案完整，检查正确性
    	StringBuffer sb=new StringBuffer();
    	for(int i=0;i<mWordSelected.size();i++)
    	{
    		sb.append(mWordSelected.get(i).mWordString);
    	}
    	return (sb.toString().equals(mCurrentSong.getmSongName()))?
    			STATUS_ANSERR_RIGHT:STATUS_ANSERR_WRONG;
    }
    //文字闪烁
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
    
    
    //处理删除待选文字事件
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
    
    //处理金币数量
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
    //从配置文件获取一些操作所需要的金币
    private int getDeleteWordCoins()
    {
    	return this.getResources().getInteger(R.integer.pay_delete_word);
    }
    private int getTipWordCoins()
    {
    	return this.getResources().getInteger(R.integer.pay_tip_answer);
    }
    
    //删除文字
    private void deleteOneWord()
    {
    	//减少金币
    	if(!handleCoins(-getDeleteWordCoins()))
    	{
    		//金币不够，显示对话框
    		showConfirmDialog(STATUS_ANSERR_LACK);
    		return;
    	}
    	//将这个索引对应的WordButton不可见
    	setButtonVisible(findNotAnswerWord(), View.INVISIBLE);
    }
    //找到一个不是答案的文件，并且当前是可见的
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
    //处理提示答案事件
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
    
    //找到一个提示答案
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
    //自动选择一个答案
    private void tipAnswer()
    {
    	//减少金币
    	if(!handleCoins(-getTipWordCoins()))
    	{
    		//金币不够，显示对话框
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
	
	//处理过关事件
    private void handlePassEvent()
    {
    	//过关界面
    	mPassView=(LinearLayout) findViewById(R.id.pass_view);
    	mPassView.setVisibility(View.VISIBLE);
    	
    	//停止动画
    	mViewPan.clearAnimation();
    	
    	//停止音乐
    	MyPlayer.StopSong(MainActivity.this);
    	
    	//当前关卡索引
    	if(mCurrentStagePassView==null)
    	{
    		mCurrentStagePassView=(TextView) findViewById(R.id.text_current_stage_pass);
    		mCurrentStagePassView.setText((mCurrentStageIndex+1)+"");
    	}
    	//当前歌曲名称
    	if(mCurrentSongNamePassView==null)
    	{
    		mCurrentSongNamePassView=(TextView) findViewById(R.id.text_current_song_name_pass);
    		mCurrentSongNamePassView.setText(mCurrentSong.getmSongName());
    	}
    	
    	//进入下一关
    	BtnNext=(ImageButton) findViewById(R.id.btn_next);
    	BtnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(judeAppPassed())
				{
					//进入通关界面
				    Util.startActivity(MainActivity.this, AllPassView.class);
				}else{
					//开始下一关
					mPassView.setVisibility(View.INVISIBLE);
					MyPlayer.PlayTone(MainActivity.this, MyPlayer.INDEX_TONE_COIN);
					initCurrentStageData();
					//播放动画和音乐
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
     *    提示对话框-----------------------------------------------------------------------
     */
    
    //处理事件响应
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
			Util.showDialog(MainActivity.this, "确定花掉"+getDeleteWordCoins()+
					"个金币去掉一个错误答案", mBtnOkDeleteWordListener);
			break;
		case ID_DIALOG_LACK_COINS:
			Util.showDialog(MainActivity.this, "金币不足，请充值", mListener);
			break;
		case ID_DIALOG_TIP_ANSWER:
			Util.showDialog(MainActivity.this, "确定花掉"+getTipWordCoins()+
					"个金币得到一个提示答案", mBtnOkTipAnswerListener);
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
