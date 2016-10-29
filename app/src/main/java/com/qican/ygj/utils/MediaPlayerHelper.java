package com.qican.ygj.utils;


import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MediaPlayerHelper implements MediaPlayer.OnPreparedListener, 
                                      MediaPlayer.OnErrorListener,
                                      MediaPlayer.OnCompletionListener {
	
	private final static String TAG = "MediaPlayerHelper";
	
	public interface OnProgressListener
	{
		void OnProgress(int v);
	};
	
	public interface OnCompletionListener
	{
		void onCompletion(MediaPlayerHelper player);
	}
	
	class UIHandler extends Handler
	{
		public UIHandler(Looper looper)
		{
			super(looper);
		}
		
		// 消息处理函数
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				start();
				break;
			}
		}
	};
	
	private OnProgressListener mPListener = null;
	private Timer mTimer = null;
	private TimerTask mProgressTask = null;
	private OnCompletionListener mCListener = null;
	
	private boolean mIsResume = false;
	private MediaPlayer mMediaPlayer = null;
	private Handler mHandler = null;
	private boolean mNeedRelease = false;
	
	private MediaPlayerHelper(MediaPlayer mediaPlayer, Context context)
	{
		mMediaPlayer = mediaPlayer;
		mHandler = new UIHandler(context.getMainLooper());
		mMediaPlayer.setOnCompletionListener(this);
	}
	
	public static MediaPlayerHelper create(Context context, int resId)
	{
		MediaPlayer player = MediaPlayer.create(context, resId);
		if (player == null) return null;		
		
		try {
			player.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MediaPlayerHelper ex = new MediaPlayerHelper(player, context);
		//ex.setOnCompletionListener(ex);
		
		return ex;
	}
	
	public static MediaPlayerHelper create(Context context, Uri uri)
	{
		MediaPlayer player = MediaPlayer.create(context, uri);
		if (player == null) 
		{
			player = new MediaPlayer();
			try {
				player.setDataSource(context, uri);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(player == null)
				return null;
			
			try {
				player.prepare();
			} catch (Exception e) {
				Logging.e(TAG, e.getMessage());
			}
		}
		
		MediaPlayerHelper ex = new MediaPlayerHelper(player, context);
		//ex.setOnCompletionListener(ex);
		
		return ex;
	}
	
	public static MediaPlayerHelper create(Context context, String path, int offset, int length)
	{
		FileDescriptor fd = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			fd = fis.getFD();
			if (length == 0) length = fis.available();
		} 
		catch (Exception e) {
			FileUtils.closeCloseable(fis);
			return null;
		}
		
		MediaPlayer player = null;
		try {
			player = new MediaPlayer();
			player.setDataSource(fd, offset, length);
		} 
		catch (Exception e) {
			Logging.e(TAG, e.getMessage());
			return null;
		}
		finally {
			FileUtils.closeCloseable(fis);
		}
		
		try {
			player.prepare();
		} catch (Exception e) {
			Logging.e(TAG, e.getMessage());
		}
		
		MediaPlayerHelper ex = new MediaPlayerHelper(player, context);
		return ex;
	}
	
	public static MediaPlayerHelper create(Context context, FileDescriptor fd, int offset, int length)
	{
		MediaPlayer player = null;
		try {
			player = new MediaPlayer();
			player.setDataSource(fd, offset, length);
		} 
		catch (Exception e) {
			Logging.e(TAG, String.format("MediaPlayerHelper create Error!\r\ne.getStackTrace():%s\r\ne.getMessage():%s", e.getStackTrace(), e.getMessage()));
			return null;
		}
		
		try {
			player.prepare();
		} catch (Exception e) {
			Logging.e(TAG, e.getMessage());
		}
		
		MediaPlayerHelper ex = new MediaPlayerHelper(player, context);
		return ex;
	}
	
	public void setOnCompletionListener(OnCompletionListener l)
	{
		if (null != mMediaPlayer)
			mMediaPlayer.setOnCompletionListener(this);		
		mCListener = l;
	}
	
	public void play()
	{
		mHandler.sendEmptyMessage(1);
	}
	
	public synchronized void start()
	{
		if (null != mMediaPlayer)
		{
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnErrorListener(this);
			
			try
			{
				if (!mMediaPlayer.isPlaying()) 
					mMediaPlayer.start();
				mIsResume = true;
			}
			catch (Exception e)
			{
				Logging.d("MediaPlayerEx:start", e.toString());
			}
		}
	}
	
	public boolean isPlaying()
	{
		if (null != mMediaPlayer)
		{
			try
			{
				return mMediaPlayer.isPlaying();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	
	public void seekTo(int msec)
	{
		if(mMediaPlayer != null)
		{
			try
			{
				mMediaPlayer.seekTo(msec);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onPrepared(MediaPlayer mp) 
	{
		synchronized (this) 
		{
			mNeedRelease = true;
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {		
		if (null != mCListener) mCListener.onCompletion(this);
		
		return false;
	}
	
	// 停止播放
	public synchronized void stop()
	{
		try
		{
			if (null != mMediaPlayer)
			{
				mIsResume = false;
				if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
			}
		}
		catch (Exception e)
		{
			Logging.d("MediaPlayerEx:stop", e.toString());
		}
		
		canelTimer();
	}

	public synchronized void pause()
	{
		try
		{
			if (null != mMediaPlayer)
			{
				mIsResume = false;
				mMediaPlayer.pause();
			}
		}
		catch (Exception e)
		{
			Logging.d("MediaPlayerEx:pause", e.toString());
		}

	}
	
	public void ContinueToPlay()
	{
		this.start();
	}
	
	// 播放完成事件
	@Override
	public synchronized void onCompletion(MediaPlayer mp) 
	{
		if (null != mCListener) mCListener.onCompletion(this);
		
		release();
	}
	
	public void setOnSeekCompleteListener(OnSeekCompleteListener listener)
	{
		if (null != mMediaPlayer)
			mMediaPlayer.setOnSeekCompleteListener(listener);
	}
	
	public synchronized void release()
	{
		if (null != mMediaPlayer)
		{
			stop();
			//if (mNeedRelease) 
			if (null != mMediaPlayer) mMediaPlayer.release();
			mMediaPlayer = null;
		}
		
		canelTimer();
	}
	
	public int getDuration()
	{
		try
		{
			return mMediaPlayer.getDuration();
		}
		catch (Exception e)
		{
			Logging.d("MediaPlayerEx:getCurrentPosition", e.toString());
		}
		
		return 0;
	}
	
	public int getCurrentPosition()
	{
		try
		{
			return mMediaPlayer.getCurrentPosition();
		}
		catch (Exception e)
		{
			Logging.d("MediaPlayerEx:getCurrentPosition", e.toString());
		}
		
		return 0;	
	}
	
	public void setProgressListener(OnProgressListener l)
	{
		mPListener = l;
		canelTimer();
		createTimer();
	}
	
	public boolean isResume()
	{
		return mIsResume;
	}
	
	public void reSet()
	{
		mMediaPlayer.reset();
	}
	
	private void createTimer()
	{
		mTimer = new Timer();
		mProgressTask = new TimerTask()
		{
			@Override
			public void run() {
				if (mIsResume && mPListener != null && mMediaPlayer != null)
				{
					mPListener.OnProgress(mMediaPlayer.getCurrentPosition());	
				}
			}
		};
		
		mTimer.schedule(mProgressTask, 0, 100);
	}
	
	private void canelTimer()
	{
		if (null != mProgressTask)
		{
			mProgressTask.cancel();
			mProgressTask = null;
		}
		
		if (null != mTimer)
		{
			mTimer.cancel();
			mTimer.purge();
			mTimer = null;
		}	
	}
}
