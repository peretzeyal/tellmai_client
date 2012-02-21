package com.TMAI.android.client.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.TMAI.android.client.data.EntityValidationResult;
import com.TMAI.android.client.data.MemoInfo;
import com.TMAI.android.client.data.EntityValidationResult.EntityValidationResultType;


public class InfoConnection {
	private static final String TAG = "InfoConnection";

	private final static String SERVER_FEEDBACK_POST_URL = "http://tmai.cloudshuffle.com/api/feedback/";
	//"http://tmai.cloudshuffle.com/recordings/"; 
	private final static String SERVER_ENTITY_CHECK_GET_URL = "http://tmai.cloudshuffle.com/api/entity/";

	
	private final static String JSON_HEADER = "application/json";
	private final static String ENTITY_NAME_HEADER  = "name";
	private final static String OS_IDENTIFIER  = "Android";

	
	public static String getProjectNameByID(String projectID){
		String projectNeme = null;

		return projectNeme;

	}

	/**
	 * connect to the server and validate that the EntityID exists
	 * @param EntityID - the EntityID we want to check
	 * @return EntityValidationResult object
	 */
	public static EntityValidationResult entityValidation(String EntityID){
		EntityValidationResult entityValidationResult = new EntityValidationResult(
				EntityValidationResultType.CONNECTION_PROBLEM, EntityID, "");
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(SERVER_ENTITY_CHECK_GET_URL+EntityID);

			//execute get
			HttpResponse response = client.execute(get);
			String entityName = null;
			if (checkResponseSuccess(response)){
				entityName = getEntityName(response);
			}
			entityValidationResult.setEntityName(entityName);
			if (entityName!=null){
				entityValidationResult.setEntityValidationResultType(EntityValidationResultType.ENTITY_EXISTS);
			}
			else{
				entityValidationResult.setEntityValidationResultType(EntityValidationResultType.ENTITY_DOES_NOT_EXISTS);
			}
		} catch (ClientProtocolException e) {
			Log.d(TAG, "problem. stoping the post "+e);
		} catch (IOException e) {
			Log.d(TAG, "problem. stoping the post "+e);
			entityValidationResult.setEntityValidationResultType(EntityValidationResultType.CONNECTION_PROBLEM);
		}
		return entityValidationResult;
	}


	private static String getEntityName(HttpResponse response){
		HttpEntity entity = response.getEntity();
		String entityName = null;
		if (entity.getContentType().getValue().equals(JSON_HEADER) ){
			try {
				String responseString = EntityUtils.toString(entity);
				JSONObject json=new JSONObject(responseString);
				entityName = (String) json.get(ENTITY_NAME_HEADER);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return entityName;

	}

	/**
	 * @param memoInfo
	 * @return post the MemoInfo data on the server using HttpClient
	 */
	public static boolean uploadMemoInfo(MemoInfo memoInfo){
		// Create a new HttpClient and Post Header

		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(SERVER_FEEDBACK_POST_URL);

			//fill in the params
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
			nameValuePairs.add(new BasicNameValuePair("uploader_email", memoInfo.getEmail()));
			//we only send either ProjectID or ProjectName not both (ProjectID is preferred)
			if (memoInfo.getProjectID()!=null && !memoInfo.getProjectID().equals("")){
				nameValuePairs.add(new BasicNameValuePair("entity_id", memoInfo.getProjectID()));
			}
			else{
				nameValuePairs.add(new BasicNameValuePair("cid_name", memoInfo.getProjectName()));
			}
			nameValuePairs.add(new BasicNameValuePair("severity", String.valueOf(memoInfo.getSeverity())));
			nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(memoInfo.getLatitude())));
			nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(memoInfo.getLongitude())));
			//nameValuePairs.add(new BasicNameValuePair("can_reply", memoInfo.getCanReplyString()));
			if (memoInfo.getCanReply()){
				nameValuePairs.add(new BasicNameValuePair("contact_me", "on"));
			}
			nameValuePairs.add(new BasicNameValuePair("upload", ""));
			nameValuePairs.add(new BasicNameValuePair("tags", memoInfo.getKind()));
			nameValuePairs.add(new BasicNameValuePair("url", memoInfo.getFileUrl()));
			nameValuePairs.add(new BasicNameValuePair("mobile_version", OS_IDENTIFIER));
/*			nameValuePairs.add(new BasicNameValuePair("duration", "10"));
			nameValuePairs.add(new BasicNameValuePair("bitrate", "5"));*/
			

			
			Log.d(TAG, "memo url: "+memoInfo.getFileUrl());

			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			//execute post
			HttpResponse response = client.execute(post);
			//printErrorResponse(response);
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
		//printErrorResponse(response);
		if (statusLine.getStatusCode() == 200){
			//the upload was successful
			return true;
		}
		//the Response failed
		return false;
	}

	private static void printErrorResponse(HttpResponse response){
		try {
			HttpEntity  entity   = response.getEntity();
			String responseString = EntityUtils.toString(entity);
			Log.d(TAG, responseString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
