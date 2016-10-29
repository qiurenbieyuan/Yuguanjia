package com.qican.ygj.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class BroadCommandJsonUtils {
	
	public BroadCommandJsonUtils(String flag) {
		mFlag = flag;
	}

	private String mTag = "";
	private String mCommand = "";
	private String mType = null;
	private Map<String, String> mData = new HashMap<String, String>();
	private String mFlag = "";

	public boolean parseJson(String json) {
		if (json == null || json.equals(""))
			return false;
		JSONObject object;
		try {
			object = new JSONObject(json);
			mTag = object.optString("App");
			mCommand = object.optString("CmdName");
			JSONObject obj = new JSONObject((object.optString("Data")).toString());
		
			if(mFlag.equals("smartbook"))
				parseDataSmartBook(obj);
			if(mFlag.equals("EbagHome"))
				parseDataEBagHome(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return true;
	}
	
	private void parseDataSmartBook(JSONObject obj) {
		if (obj.equals("") || obj == null)
			return;
		mData.put("index", String.valueOf(obj.optInt("index")));
		if (mCommand.equals("play")) {
			mData.put("id", obj.optString("id"));
			mData.put("type", obj.optString("type"));
		} 
	}
	
	private void parseDataEBagHome(JSONObject obj) {
		if (obj.equals("") || obj == null)
			return;
		if(mCommand.equals("redflower")){
			mType = obj.optString("type");
			mData.put("Id", obj.optString("id"));
			mData.put("UserName", obj.optString("username"));
			mData.put("ClassName", obj.optString("coursename"));
			mData.put("Date", obj.optString("date"));
			mData.put("IsRead", obj.optString("isread"));
			mData.put("KeepJson", obj.optString("json"));
		}
	}
	public Map<String, String> getData() {
		return mData;
	}

	public String getCommand() {
		return mCommand;
	}

	public String getTag() {
		return mTag;
	}
	
	public String getType(){
		return mType;
	}
}
