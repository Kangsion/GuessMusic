package com.imooc.guessmusic.util;

import java.security.InvalidAlgorithmParameterException;

import com.imooc.guessmusic.R;
import com.imooc.guessmusic.model.IAlertDialogButtonListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class Util {
	
	private static AlertDialog mAlertDialog; 
	
    public static View getView(Context context,int layoutId)
    {
    	LayoutInflater inflater=(LayoutInflater) context.getSystemService
    			(Context.LAYOUT_INFLATER_SERVICE); 
    	View layout=inflater.inflate(layoutId, null);
    	return layout;
    }
    
    
    public static void startActivity(Context context,Class<?> cl)
    {
    	Intent intent=new Intent();
    	intent.setClass(context, cl);
    	
    	context.startActivity(intent);
    	((Activity)context).finish();
    	
    }
    public static void showDialog(final Context context,String messages
    		,final IAlertDialogButtonListener listener)
    {
    	View dialogView=null;
    	
    	AlertDialog.Builder builder=new AlertDialog.Builder(context);
    	dialogView=getView(context, R.layout.dialog_view);
    	
    	ImageButton btnOkView=(ImageButton) dialogView.findViewById(R.id.btn_ok);
    	ImageButton btnCancelView=(ImageButton) dialogView.findViewById(R.id.btn_cancel);
    	TextView textMessageView=(TextView) dialogView.findViewById(R.id.text_dialog_message);
    	textMessageView.setText(messages);
    	
    	btnOkView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mAlertDialog!=null)
					mAlertDialog.cancel();
				
				if(listener!=null)
				{
					 MyPlayer.PlayTone(context, MyPlayer.INDEX_TONE_ENTER);
					listener.onClick();
				   
				}
			}
		});
    	btnCancelView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mAlertDialog!=null)
				{
					mAlertDialog.cancel();
					MyPlayer.PlayTone(context, MyPlayer.INDEX_TONE_CANCEL);
				}
				      
			}
		});
    	
    	builder.setView(dialogView);
    	mAlertDialog=builder.create();
    	mAlertDialog.show();
    }
    
}
