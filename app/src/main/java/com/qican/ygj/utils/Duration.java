package com.qican.ygj.utils;

public class Duration {
	
	private long mLastModifyTime = 0;
	private long mTimeOut=0;
	
	public Duration(){
		mTimeOut=System.currentTimeMillis();
		
	}
   
   public boolean elapsed(long msec){
	   
	   long current = System.currentTimeMillis();
	   if (current < mLastModifyTime) {
		   update();
		   return false;
	   }
	   
	   long tmp = current - msec;
	    if( tmp > mLastModifyTime )
	        return true;
	    
	    return false;
   }
   
   public boolean timeOut(long setTimeOut){
	   long current = System.currentTimeMillis();	   
	   if( current-mTimeOut > setTimeOut){
		   updateTimeOut();
		   return true;
	   }
	   updateTimeOut();
	   return false;
   }
   
   public void update() {
	   mLastModifyTime = System.currentTimeMillis();
   }
   
   public void updateTimeOut(){
	   mTimeOut=System.currentTimeMillis(); 
   }
}
