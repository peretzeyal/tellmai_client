package com.TMAI.android.client.connection;

import java.io.File;

import android.content.Context;

import com.TMAI.android.client.utils.BuildInfo;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class UploadFileConnection {

	private static final String TAG = "UploadFileConnection";
	
	private static String s3URL = "https://s3.amazonaws.com/";
	private static String projectBucket = "tmai_test/recordings";
	
	String ACCESS_KEY_ID = "AKIAIZJ3NZHNB4LTL6DA";
	String SECRET_KEY = "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";
	
	public String uploadAudioFile(Context context, String filePath, String fileName){
		try {
			File uploadFile = new File(filePath+ fileName);
			long fileSize = uploadFile.length();
			
			byte[] b1 = BuildInfo.getSU();
			String b1String = new String(b1);
			byte[] b2 = BuildInfo.getSP();
			String b2String = new String(b2);
			AmazonS3Client s3Client = new AmazonS3Client( new BasicAWSCredentials( b1String, b2String ) );
			PutObjectRequest por = new PutObjectRequest( projectBucket, uploadFile.getName(), uploadFile);
			s3Client.putObject( por );
			String fileURL = s3URL+projectBucket+"/"+fileName;
			return fileURL;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

/*	public String uploadObject(Context context, String filePath, String fileName){
		Map<String,String> params = new HashMap<String,String>();
		Uri uri = Uri.parse("file://"+filePath+fileName);
		params.put("AWSAccessKeyId", "AKIAIZJ3NZHNB4LTL6DA");
		params.put("Content-Type", "image/jpeg");
		//params.put("policy", "some-policy-defined-by-yourself");
		params.put("Filename", fileName);
		params.put("key", "Recordings/"+fileName);
		params.put("acl", "private");
		params.put("signature", "I3okz8F9rZ7wedthNQQzToPzWQAu7dr12nhMXbP7");
		params.put("success_action_status", "201");
		params.put("AWSAccessKeyId", "our-aws-access-key");
		params.put("Content-Type", "image/jpeg");
		params.put("policy", "some-policy-defined-by-yourself");
		params.put("Filename", "photo.jpg");
		params.put("key", "images/photo.jpg");
		params.put("acl", "private");
		params.put("signature", "some-signature-defined-by-yourself");
		params.put("success_action_status", "201");

		File uploadFile = new File(filePath+fileName);
		long fileSize = uploadFile.length();
		String fileURL = null;
		try {
				      HttpRequest.postSocket("your-bucket-name.s3.amazonaws.com", params,
		         context.getContentResolver().openInputStream(uri)
		         fileSize, this, 10, 70, "photo.jpg", "image/jpeg");
			HttpRequest httpRequest =  new HttpRequest();
			//			httpRequest.postSocket("your-bucket-name.s3.amazonaws.com", params,

			httpRequest.postSocket("tmai_test.s3.amazonaws.com", params,
					context.getContentResolver().openInputStream(uri),
					fileSize, fileName, "image/jpeg");
			
			//TODO create file url
			
		} catch (Exception e) {
			return null;
		}
		return fileURL;
	}*/
}
