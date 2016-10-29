package com.qican.ygj.utils;

import java.io.FileDescriptor;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.Handler;
import android.os.Message;

public class MutiTimeMediaPlayer implements MediaPlayerHelper.OnCompletionListener
{
	private Object mLockObj = null;
	
	private int mPlayTimes 	= 0;
	private int mInterval 	= 0;
	private boolean mIsPause = false;
	private int mCurrentTimes = 0;
	
	private MediaPlayerHelper mPlayer 	= null;
	private PlayerHanlder mHandler		=null;
	
	private Context mContext 	= null;
	private FileDescriptor mfd  = null;
	
	private int mOffset = 0;
	private int mLength = 0;
	
	private final int PLAY_NEXT = 1;
	private OnPlayerStatusListener mStatusListener = null;
	
	public interface OnPlayerStatusListener
	{
		void OnPlay(int currentTime);
		void OnStop();
		void OnCompletion(int currentTime);
	}
	
	private MutiTimeMediaPlayer(Context context, FileDescriptor fd, int offset, int length, int playTimes, int interval)
	{
		this.mContext 	= context;
		this.mfd 		= fd;
		this.mOffset 	= offset;
		this.mLength 	= length;
		
		this.mPlayTimes = playTimes;
		this.mInterval 	= interval;
		
		mHandler = new PlayerHanlder();
		mLockObj = new Object();
	}
	
	private void setMediaPlayer(MediaPlayerHelper player)
	{
		this.mPlayer = player;
		player.setOnCompletionListener(this);
	}
	
	public void setOnPlayerStatusListener(OnPlayerStatusListener listener)
	{
		mStatusListener = listener;
	}
	
	public static MutiTimeMediaPlayer createPlayer(Context context, FileDescriptor fd, int offset, int length, int playTimes, int interval)
	{
		MediaPlayerHelper playerHelper = MediaPlayerHelper.create(context, fd, offset, length);
		if (null == playerHelper) return null;
		
		MutiTimeMediaPlayer player = new MutiTimeMediaPlayer(context, fd, offset, length, playTimes, interval);
		player.setMediaPlayer(playerHelper);
		
		return player;
	}
	
	public static MutiTimeMediaPlayer createPlayer(Context context, int resId, int playTimes, int interval) {
		AssetFileDescriptor afd = context.getResources().openRawResourceFd(resId);
		return createPlayer(context, afd.getFileDescriptor(), 
				                     (int)afd.getStartOffset(), 
				                     (int)afd.getLength(), 
				                     playTimes, interval);
	}
	
	public void start()
	{
		synchronized (mLockObj) 
		{
			if (null != mStatusListener) mStatusListener.OnPlay(mCurrentTimes);
			
			if (null != mPlayer) 
			{
				++mCurrentTimes;
				mPlayer.start();
				
			}
		}
	}
	
	public void stop()
	{
		synchronized (mLockObj) 
		{
			mHandler.removeMessages(PLAY_NEXT);
			
			if (null != mPlayer && null != mStatusListener) mStatusListener.OnStop();
			release();
		}
	}
	
	private void release()
	{
		if (null == mPlayer) return;
		mPlayer.release();
		mPlayer = null;
	}
	
	public void setPause(boolean value) {
		mIsPause = value;
	}
	public boolean getPause() {
		return mIsPause;
	}
	public void setRestore() {
		if (mPlayer == null) return;
		
		if (null != mPlayer /*播放被停止，无需播放下一次*/ &&
				mPlayTimes != mCurrentTimes)//没有达到播放次数
				
		{
			Message msg = mHandler.obtainMessage(PLAY_NEXT);
			msg.obj = this;
			mHandler.sendMessageDelayed(msg, mInterval);
		}
		else
		{
			release();
		}
	}
	
	@Override
	public void onCompletion(MediaPlayerHelper player) 
	{
		synchronized (mLockObj) 
		{
			if (null != mStatusListener) mStatusListener.OnCompletion(mCurrentTimes);
			
			if(mIsPause) /*当前处于暂停状态*/ {
				return;
			}
			else {
				setRestore();
			}
		}
	}
	
	private class PlayerHanlder extends Handler
	{
		@Override
		public void handleMessage(Message msg) 
		{
			synchronized (mLockObj) 
			{
				switch (msg.what)
				{
				case PLAY_NEXT:
						if (null == mPlayer) return;//播放被停止，无需播放下一次
						
						MediaPlayerHelper playerHelper = MediaPlayerHelper.create(mContext, mfd, mOffset, mLength);
						if (null == playerHelper) return;
						
						MutiTimeMediaPlayer player = (MutiTimeMediaPlayer)(msg.obj);
						
						player.setMediaPlayer(playerHelper);
						player.setOnPlayerStatusListener(mStatusListener);
						player.start();
					break;
	
				default:
					break;
				}
			}
		}
	}

}