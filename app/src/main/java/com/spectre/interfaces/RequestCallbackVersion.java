package com.spectre.interfaces;

import org.json.JSONObject;

public interface RequestCallbackVersion {
	public void onSuccess(JSONObject js, String msg);
	public void onFailed(JSONObject js, String msg);
	public void onAuthFailed(JSONObject js, String msg);
	public void onNull(JSONObject js, String msg);
	public void onException(JSONObject js, String msg);
	public void onInactive(JSONObject js, String inactive, String status);
	public void onVersionChange(JSONObject js, String status);
}
