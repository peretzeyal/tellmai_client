package com.TMAI.android.client.connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;


public class HttpRequest {
	private static final String TAG = "HttpRequest";

	private static final String boundary = "-----------------------******";
	private static final String newLine = "\r\n";
	private static final int maxBufferSize = 4096;
	


	    public static void uploadUserPhoto() {
	        HttpParams params = new BasicHttpParams();
	        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	        DefaultHttpClient mHttpClient = new DefaultHttpClient(params);
	        try {

	            HttpPost httppost = new HttpPost("http://tmai.cloudshuffle.com/");

	            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);  
	            multipartEntity.addPart("email", new StringBody(""));
	            multipartEntity.addPart("name", new StringBody(""));
	            multipartEntity.addPart("project_id", new StringBody("15"));
	            multipartEntity.addPart("severity", new StringBody("5"));
	            multipartEntity.addPart("latitude", new StringBody("5"));
	            multipartEntity.addPart("longitude", new StringBody("5"));
	            multipartEntity.addPart("can_reply", new StringBody("2"));
	            multipartEntity.addPart("upload", new StringBody(""));
	            multipartEntity.addPart("tags", new StringBody("5"));
	            multipartEntity.addPart("upload_url", new StringBody("http://s3.amazonaws.com/tmai/recordings/torrent_2.txt"));

	            httppost.setEntity(multipartEntity);

	            mHttpClient.execute(httppost, new PhotoUploadResponseHandler());

	        } catch (Exception e) {
	            Log.e("error", e.getLocalizedMessage(), e);
	        }
	    }
	    private static class PhotoUploadResponseHandler implements ResponseHandler {

	        @Override
	        public Object handleResponse(HttpResponse response)
	                throws ClientProtocolException, IOException {

	            HttpEntity r_entity = response.getEntity();
	            String responseString = EntityUtils.toString(r_entity);
	            Log.d("UPLOAD", responseString);

	            return null;
	        }

	    }
	
/*	request = HttpRequest()
    post_data = '''{"email": "test2@test.com", "severity": %s, "id": "5","uploaded_type": %s,
            "recording_id": "2", "recording_url": "http://aaa.com/aa","location": "",
            "project_id": "", "project_name": ""}''' % (SEVERITY.TYPE1, CALL_TYPE.TYPE1)
    request._raw_post_data = post_data
    resp = self.client.post('/api/v1/review/', data=post_data, content_type='application/json')

    self.assertEqual(resp.status_code, 201)
    self.assertEqual(resp['location'], 'http://testserver/api/v1/review/5/')

    # make sure posted object exists
    resp = self.client.get('/api/v1/review/5/', data={'format': 'json'})
    self.assertEqual(resp.status_code, 200)
    obj = json.loads(resp.content)
    self.assertEqual(obj['email'], 'test2@test.com')
    self.assertEqual(obj['uploaded_type'], CALL_TYPE.TYPE1)
    self.assertEqual(obj['recording_id'], '2')*/
    
    private static final String infoHeader = "";
	
    public static void postData() {
        // Create a new HttpClient and Post Header

        try {
        	
//    		Map<String,String> params = new HashMap<String,String>();
//    		params.put("AWSAccessKeyId", "AKIAIZJ3NZHNB4LTL6DA");
//    		params.put("Content-Type", "image/jpeg");
//    		//params.put("policy", "some-policy-defined-by-yourself");
//    		params.put("acl", "private");
//    		params.put("signature", "I3okz8F9rZ7wedthNQQzToPzWQAu7dr12nhMXbP7");
//    		params.put("success_action_status", "201");
//    		params.put("AWSAccessKeyId", "our-aws-access-key");
//    		params.put("Content-Type", "image/jpeg");
//    		params.put("policy", "some-policy-defined-by-yourself");
//    		params.put("Filename", "photo.jpg");
//    		params.put("key", "images/photo.jpg");
//    		params.put("acl", "private");
//    		params.put("signature", "some-signature-defined-by-yourself");
//    		params.put("success_action_status", "201");
//        	        
        	
        	
        	
        	
/*        	HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://tmai.cloudshuffle.com/");

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("email", "root@example.com"));
            nameValuePairs.add(new BasicNameValuePair("project_id", "15"));
            nameValuePairs.add(new BasicNameValuePair("acl", "private"));
            nameValuePairs.add(new BasicNameValuePair("csrfmiddlewaretoken", "30504b6efdc91fec7ffcdcef10cd6b43"));

            

            nameValuePairs.add(new BasicNameValuePair("success_action_status", "201"));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            Header[] headers= response.getAllHeaders();
            */
            
        	HttpClient client = new DefaultHttpClient();
        	HttpPost post = new HttpPost("http://tmai.cloudshuffle.com/recordings/");
/*        	post.setHeader("Accept", "application/json");
        	post.setHeader("User-Agent", "Apache-HttpClient/4.1 (java 1.5)");
        	post.setHeader("Host", "myhost.com");*/
/*        	post.setHeader("Authorization","csrfmiddlewaretoken:30504b6efdc91fec7ffcdcef10cd6b43");
        	List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        	nvps.add(new BasicNameValuePair("data[body]", "test"));
        	AbstractHttpEntity ent=new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
        	ent.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
        	ent.setContentEncoding("UTF-8");
        	post.setEntity(ent);
        	post.setURI(new URI("http://tmai.cloudshuffle.com"));*/
        	
//        	post.setHeader("Host","tmai.cloudshuffle.com");
//        	post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:5.0) Gecko/20100101 Firefox/5.0");
//        	//post.setHeader("Accept", "application/json");
//        	post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//        	post.setHeader("Accept-Language","en-us,en;q=0.5");
//        	post.setHeader("Accept-Encoding","gzip, deflate");
//        	post.setHeader("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");
//        	post.setHeader("Connection","keep-alive");
//        	post.setHeader("Referer","http://tmai.cloudshuffle.com/");

        	HttpParams postParams = new BasicHttpParams();
        	
        	postParams.setParameter("email", "");
        	postParams.setParameter("name", "");
        	postParams.setParameter("project_id", "15");
        	postParams.setParameter("severity", "5");
        	postParams.setParameter("latitude", "5");
        	postParams.setParameter("longitude", "5");
        	postParams.setParameter("can_reply", "3");
        	postParams.setParameter("upload", "");
        	postParams.setParameter("tags", "5");
        	postParams.setParameter("upload_url", "http://s3.amazonaws.com/tmai/recordings/torrent_2.txt");
        	post.setParams(postParams);
        	
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
            nameValuePairs.add(new BasicNameValuePair("email", ""));
            nameValuePairs.add(new BasicNameValuePair("name", ""));
            nameValuePairs.add(new BasicNameValuePair("project_id", "15"));
            nameValuePairs.add(new BasicNameValuePair("severity", "5"));
            nameValuePairs.add(new BasicNameValuePair("latitude", "5"));
            nameValuePairs.add(new BasicNameValuePair("longitude", "5"));
            nameValuePairs.add(new BasicNameValuePair("can_reply", "3"));
            nameValuePairs.add(new BasicNameValuePair("upload", ""));
            nameValuePairs.add(new BasicNameValuePair("tags", "5"));
            nameValuePairs.add(new BasicNameValuePair("upload_url", "http://s3.amazonaws.com/tmai/recordings/torrent_2.txt"));

            

  
            //post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        	
        	
        	HttpResponse response = client.execute(post);
        	HttpEntity  entity   = response.getEntity();

            String responseString = EntityUtils.toString(entity);
            Log.d("UPLOAD", responseString);
        	String statusLine = response.getStatusLine().toString();

        } catch (ClientProtocolException e) {
			Log.d(TAG, "problem stoping the record "+e);
        } catch (IOException e) {
			Log.d(TAG, "problem stoping the record "+e);
        }
    } 
    
 
    
	private static final String header1 = 
		"POST / HTTP/1.1\n" +
		"Host: %s\n" +
		"User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.10) Gecko/20071115 Firefox/2.0.0.10\n" +
		"Accept: text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5\n" +
		"Accept-Language: en-us,en;q=0.5\n" +
		"Accept-Encoding: gzip,deflate\n" +
		"Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7\n" +
		"Keep-Alive: 300\n" +
		"Connection: keep-alive\n" +
		"Content-Type: multipart/form-data; boundary=---------------------------1355210151131 Content-Length: 1122 -----------------------------1355210151131 Content-Disposition: form-data; name=\"email\" -----------------------------1355210151131 Content-Disposition: form-data; name=\"name\" 15 -----------------------------1355210151131 Content-Disposition: form-data; name=\"project_id\" -----------------------------1355210151131 Content-Disposition: form-data; name=\"severity\" 1 -----------------------------1355210151131 Content-Disposition: form-data; name=\"latitude\" 1 -----------------------------1355210151131 Content-Disposition: form-data; name=\"longitude\" 1 -----------------------------1355210151131 Content-Disposition: form-data; name=\"can_reply\" 2 -----------------------------1355210151131 Content-Disposition: form-data; name=\"upload\"; filename=\"\" Content-Type: application/octet-stream -----------------------------1355210151131 Content-Disposition: form-data; name=\"tags\" 1 -----------------------------1355210151131 Content-Disposition: form-data; name=\"upload_url\" http://s3.amazonaws.com/tmai/recordings/torrent_2.txt -----------------------------1355210151131--" +
		"Content-Length: %s\n\n";
	
	private static final String header = 
		"POST / HTTP/1.1\n" +
		"Host: %s\n" +
		"User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.10) Gecko/20071115 Firefox/2.0.0.10\n" +
		"Accept: text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5\n" +
		"Accept-Language: en-us,en;q=0.5\n" +
		"Accept-Encoding: gzip,deflate\n" +
		"Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7\n" +
		"Keep-Alive: 300\n" +
		"Connection: keep-alive\n" +
		"Content-Type: multipart/form-data; boundary=" + boundary + "\n" +
		"Content-Length: %s\n\n";

	
	public void postSocket(String sUrl, Map params 
			 ) throws Exception {
		OutputStream writer = null;
		BufferedReader reader = null;
		Socket socket = null;
		try {
			int bytesAvailable;
			int bufferSize;
			int bytesRead;
			//int totalProgress = endProgress - startProgress;

			//task.myPublishProgress(new Long(startProgress));

			String openingPart = writeContent(params);
			String closingPart = newLine + "--" + boundary + "--" + newLine;
			long totalLength = openingPart.length() + closingPart.length() ;//+ streamLength;

			// strip off the leading http:// otherwise the Socket will not work
			String socketUrl = sUrl;
			if (socketUrl.startsWith("http://")) {
				socketUrl = socketUrl.substring("http://".length());
			}

			socket = new Socket(socketUrl, 80);
			socket.setKeepAlive(true);
			writer = socket.getOutputStream();
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			writer.write(String.format(header, socketUrl, Long.toString(totalLength)).getBytes());
			writer.write(openingPart.getBytes());

			//bytesAvailable = stream.available();
			//bufferSize = Math.min(bytesAvailable, maxBufferSize);
			//byte[] buffer = new byte[bufferSize];
			//bytesRead = stream.read(buffer, 0, bufferSize);
			//int readSoFar = bytesRead;
			//task.myPublishProgress(new Long(startProgress + Math.round(totalProgress * readSoFar / streamLength)));
/*			while (bytesRead > 0) {
				writer.write(buffer, 0, bufferSize);
				//bytesAvailable = stream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				//bytesRead = stream.read(buffer, 0, bufferSize);
				readSoFar += bytesRead;
				//task.myPublishProgress(new Long(startProgress + Math.round(totalProgress * readSoFar / streamLength)));
			}*/
			//stream.close();
			writer.write(closingPart.getBytes());
			Log.d(TAG, closingPart);
			writer.flush();

			// read the response
			String s = reader.readLine();
			// do something with response s
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (writer != null) { try { writer.close(); writer = null;} catch (Exception ignore) {}}
			if (reader != null) { try { reader.close(); reader = null;} catch (Exception ignore) {}}
			if (socket != null) { try {socket.close(); socket = null;} catch (Exception ignore) {}}
		}
		//task.myPublishProgress(new Long(endProgress));
	}

	/*	public static void postSocket(String sUrl, Map params, InputStream stream, long streamLength, 
			PutOrderFilesTask task, int startProgress, int endProgress, String fileName, String contentType) {*/
//	public void postSocket(String sUrl, Map params, InputStream stream, long streamLength, 
//			 String fileName, String contentType) throws Exception {
//		OutputStream writer = null;
//		BufferedReader reader = null;
//		Socket socket = null;
//		try {
//			int bytesAvailable;
//			int bufferSize;
//			int bytesRead;
//			//int totalProgress = endProgress - startProgress;
//
//			//task.myPublishProgress(new Long(startProgress));
//
//			String openingPart = writeContent(params, fileName, contentType);
//			String closingPart = newLine + "--" + boundary + "--" + newLine;
//			long totalLength = openingPart.length() + closingPart.length() + streamLength;
//
//			// strip off the leading http:// otherwise the Socket will not work
//			String socketUrl = sUrl;
//			if (socketUrl.startsWith("http://")) {
//				socketUrl = socketUrl.substring("http://".length());
//			}
//
//			socket = new Socket(socketUrl, 80);
//			socket.setKeepAlive(true);
//			writer = socket.getOutputStream();
//			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//			writer.write(String.format(header, socketUrl, Long.toString(totalLength)).getBytes());
//			writer.write(openingPart.getBytes());
//
//			bytesAvailable = stream.available();
//			bufferSize = Math.min(bytesAvailable, maxBufferSize);
//			byte[] buffer = new byte[bufferSize];
//			bytesRead = stream.read(buffer, 0, bufferSize);
//			int readSoFar = bytesRead;
//			//task.myPublishProgress(new Long(startProgress + Math.round(totalProgress * readSoFar / streamLength)));
//			while (bytesRead > 0) {
//				writer.write(buffer, 0, bufferSize);
//				bytesAvailable = stream.available();
//				bufferSize = Math.min(bytesAvailable, maxBufferSize);
//				bytesRead = stream.read(buffer, 0, bufferSize);
//				readSoFar += bytesRead;
//				//task.myPublishProgress(new Long(startProgress + Math.round(totalProgress * readSoFar / streamLength)));
//			}
//			stream.close();
//			writer.write(closingPart.getBytes());
//			Log.d(TAG, closingPart);
//			writer.flush();
//
//			// read the response
//			String s = reader.readLine();
//			// do something with response s
//		} catch (Exception e) {
//			throw new Exception(e);
//		} finally {
//			if (writer != null) { try { writer.close(); writer = null;} catch (Exception ignore) {}}
//			if (reader != null) { try { reader.close(); reader = null;} catch (Exception ignore) {}}
//			if (socket != null) { try {socket.close(); socket = null;} catch (Exception ignore) {}}
//		}
//		//task.myPublishProgress(new Long(endProgress));
//	}


	private String writeContent(Map params) {

		StringBuffer buf = new StringBuffer();

		Set<String> keys = params.keySet();
		for (String key : keys) {
			String val = (String) params.get(key);
			buf.append("--")
			.append(boundary)
			.append(newLine);
			buf.append("Content-Disposition: form-data; name=\"")
			.append(key)
			.append("\"")
			.append(newLine)
			.append(newLine)
			.append(val)
			.append(newLine);
		}

/*		buf.append("--")
		.append(boundary)
		.append(newLine);
		buf.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
		.append(fileName)
		.append("\"")
		.append(newLine);
		buf.append("Content-Type: ")
		.append(contentType)
		.append(newLine)
		.append(newLine);
*/
		return buf.toString();
	}
	
	/**
	 * Populate the multipart request parameters into one large stringbuffer which will later allow us to 
	 * calculate the content-length header which is mandatotry when putting objects in an S3
	 * bucket
	 * 
	 * @param params
	 * @param fileName the name of the file to be uploaded
	 * @param contentType the content type of the file to be uploaded
	 * @return
	 *//*
	private String writeContent(Map params, String fileName, String contentType) {

		StringBuffer buf = new StringBuffer();

		Set<String> keys = params.keySet();
		for (String key : keys) {
			String val = (String) params.get(key);
			buf.append("--")
			.append(boundary)
			.append(newLine);
			buf.append("Content-Disposition: form-data; name=\"")
			.append(key)
			.append("\"")
			.append(newLine)
			.append(newLine)
			.append(val)
			.append(newLine);
		}

		buf.append("--")
		.append(boundary)
		.append(newLine);
		buf.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
		.append(fileName)
		.append("\"")
		.append(newLine);
		buf.append("Content-Type: ")
		.append(contentType)
		.append(newLine)
		.append(newLine);

		return buf.toString();
	}*/
}
