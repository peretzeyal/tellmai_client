package com.TMAI.android.client.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.TMAI.android.client.data.MemoInfo;


public class InfoConnection {
	private static final String TAG = "InfoConnection";

	private final static String SERVER_URL = "http://tmai.cloudshuffle.com/recordings/"; 


	public static String getProjectNameByID(String projectID){
		String projectNeme = null;

		return projectNeme;

	}

	/**
	 * @param memoInfo
	 * @return post the MemoInfo data on the server using HttpClient
	 */
	public static boolean uploadMemoInfo(MemoInfo memoInfo){
		// Create a new HttpClient and Post Header

		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(SERVER_URL);

			//fill in the params
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
			nameValuePairs.add(new BasicNameValuePair("email", memoInfo.getEmail()));
			nameValuePairs.add(new BasicNameValuePair("name", memoInfo.getProjectName()));
			nameValuePairs.add(new BasicNameValuePair("project_id", memoInfo.getProjectID()));
			nameValuePairs.add(new BasicNameValuePair("severity", String.valueOf(memoInfo.getSeverity())));
			nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(memoInfo.getLatitude())));
			nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(memoInfo.getLongitude())));
			nameValuePairs.add(new BasicNameValuePair("can_reply", memoInfo.getCanReplyString()));
			nameValuePairs.add(new BasicNameValuePair("upload", ""));
			nameValuePairs.add(new BasicNameValuePair("tags", memoInfo.getKind()));
			nameValuePairs.add(new BasicNameValuePair("upload_url", memoInfo.getFileUrl()));
			Log.d(TAG, "memo url: "+memoInfo.getFileUrl());

			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			//execute post
			HttpResponse response = client.execute(post);

			return checkResponseSuccess(response);
		} catch (ClientProtocolException e) {
			Log.d(TAG, "problem. stoping the post "+e);
		} catch (IOException e) {
			Log.d(TAG, "problem. stoping the post "+e);
		}
		return false;
	}

	private static boolean checkResponseSuccess(HttpResponse response){
		StatusLine statusLine =	response.getStatusLine();
		printErrorResponse(response);
		if (statusLine.getStatusCode() == 200){
			//the upload was successful
			Log.d(TAG, "memo upload was successful");
			return true;
		}
		//the upload failed
		Log.d(TAG, "memo upload failed");
		return false;
	}

	private static void printErrorResponse(HttpResponse response){
		try {
			HttpEntity  entity   = response.getEntity();
			String responseString = EntityUtils.toString(entity);
			Log.d(TAG, responseString);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
