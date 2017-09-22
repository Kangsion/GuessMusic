package com.imooc.guessmusic.ui;

import java.util.ArrayList;

import com.imooc.guessmusic.R;
import com.imooc.guessmusic.model.WordButton;
import com.imooc.guessmusic.model.WordButtonListener;
import com.imooc.guessmusic.util.Util;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class MyGridView extends GridView {
    private ArrayList<WordButton> mArrayList=new ArrayList<WordButton>();
    
    public static MyGridAdptar mAdptar;
    
    private Context mContext;
    
    public static int COUNTS_WORDS=16;
    
    private Animation mScaleAnimation;
    
    private WordButtonListener mListener;
    
	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext=context;
		 //initAllWord();
		mAdptar=new MyGridAdptar();
		setAdapter(mAdptar);
	}
	
	class MyGridAdptar extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mArrayList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mArrayList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final WordButton holder;
			if(convertView==null)
			{
				convertView=Util.getView(mContext, R.layout.self_ui_gridview_item);
                //加载动画
				mScaleAnimation=AnimationUtils.loadAnimation(mContext, R.anim.scale);
				//设置动画延迟
				mScaleAnimation.setStartOffset(position*100);
				
				holder=mArrayList.get(position);
				holder.mIndex=position;
				if (holder.mViewButton == null) {    
				holder.mViewButton=(Button)convertView.findViewById(R.id.item_btn);
				holder.mViewButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mListener.OnClick(holder);
					}
				});
				}
				convertView.setTag(holder);
				
			}else
			{
				holder=(WordButton) convertView.getTag();
			}
			holder.mViewButton.setText(holder.mWordString);
			return convertView;
		}
		
	}
	/* private void initAllWord()
	 {
		
		 
		 for(int i=0;i<MyGridView.COUNTS_WORDS;i++)
		 {
			 WordButton button=new WordButton();
			 button.mWordString=i+"";
			 mArrayList.add(button);
			 
		 }
		 
	
		 
	 }*/
	public void setWoredButtonListener(WordButtonListener listener)
	{
		mListener=listener;
	}
	
	public void updateData(ArrayList<WordButton> mAllWords) {
		// TODO Auto-generated method stub
		mArrayList=mAllWords;
		setAdapter(mAdptar);
	}

}
