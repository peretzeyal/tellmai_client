package com.TMAI.android.client.connection;





import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Process;
import android.util.Log;

public class FileUploader implements Runnable {
	private static final String TAG = "FileUploader";

	URL connectURL;

	String responseString;
	String fileName;
	byte[] dataToServer;
	FileInputStream fileInputStream = null;

	String lineEnd = "\r\n";
	String twoHyphens = "--";

	//String boundary = "---------------------------239413274531762";
	String boundary = "---------------------------1355210151131";

	
	static String urlParameters =
        "email=" + "" +
        "name=" + "" +
        "project_id=" + "15" +
        "latitude=" + "5" +
        "longitude=" + "5" +
        "can_reply=" + "2" +
        "tags=" + "5" +
        "upload_url=" + "http://s3.amazonaws.com/tmai/recordings/torrent_2.txt";
        
        

	//static String urlParameters ="";

	public static String excutePost(String targetURL)
	{
	  URL url;
	  HttpURLConnection connection = null;  
	  try {
	    //Create connection
	    url = new URL(targetURL);
	    connection = (HttpURLConnection)url.openConnection();
	    connection.setRequestMethod("POST");
	    connection.setRequestProperty("Content-Type", 
	         "application/x-www-form-urlencoded");
				
	    connection.setRequestProperty("Content-Length", "" + 
	             Integer.toString(urlParameters.getBytes().length));
	    connection.setRequestProperty("Content-Language", "en-US");  
				
	    //connection.setRequestProperty("Content-Disposition", "form-data; name=\"severity\" 1 " );
	    connection.setRequestProperty("severity", "1" );

	    connection.setUseCaches (false);
	    connection.setDoInput(true);
	    connection.setDoOutput(true);

	    //Send request
	   // DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
	    DataOutputStream wr = new DataOutputStream (System.out);
	    wr.writeBytes (urlParameters);
	    wr.flush ();
	    wr.close ();

	    //Get Response	
	    InputStream is = connection.getInputStream();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    String line;
	    StringBuffer response = new StringBuffer(); 
	    while((line = rd.readLine()) != null) {
	      response.append(line);
	      response.append('\r');
	    }
	    rd.close();
	    Log.d("response",response.toString());
	    return response.toString();

	  } catch (Exception e) {

	    e.printStackTrace();
	    return null;

	  } finally {

	    if(connection != null) {
	      connection.disconnect(); 
	    }
	  }
	}
	

	public FileUploader(String urlString, String fileName ){
		try{
			connectURL = new URL(urlString);

		}catch(Exception ex){
			Log.e(TAG, "MALFORMATED URL");
		}
		this.fileName = fileName;


	}





	public void run() {
		Log.d(TAG, "Upload Thread Started.");
		// Lower thread priority a little. We're not the UI.
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

		try {
			fileInputStream = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		postFile(fileName);

		System.out.println("Upload Thread ended.");
	}



	private void writeHeader(DataOutputStream dos, String name, String value) throws IOException {
		dos.writeBytes(twoHyphens + boundary + lineEnd);
		dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"");
		dos.writeBytes(lineEnd);
		dos.writeBytes(lineEnd);
		dos.writeBytes(value);
		dos.writeBytes(lineEnd);
	}


	void postFile(String mediaFileName){

		try
		{
			//------------------ CLIENT REQUEST


			// Open a HTTP connection to the URL

			HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(false);

			// Use a post method.
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
/*			conn.setRequestProperty("Content-Length", "1122" + boundary);
			conn.setRequestProperty("Content-Disposition", "form-data; name=\"severity\" 1 " + boundary);*/
			DataOutputStream dos = new DataOutputStream( conn.getOutputStream()  );
			

	/*		writeHeader(dos, "media-type-id", "6");
			writeHeader(dos, "media-file-id", "2510");
			writeHeader(dos, "camera-id", "5");
			writeHeader(dos, "user-name", "android-tablet-api-user");
			writeHeader(dos, "password", "g7h0Kms5@q#54");
			writeHeader(dos, "user-id", "98");*/
			writeHeader(dos, "email", "");
			writeHeader(dos, "name", "");
			writeHeader(dos, "project_id", "15");
			//writeHeader(dos, "severity", "5");
			writeHeader(dos, "latitude", "5");
			writeHeader(dos, "longitude", "5");
			writeHeader(dos, "can_reply", "2");
			writeHeader(dos, "tags", "5");
			writeHeader(dos, "upload_url", "http://s3.amazonaws.com/tmai/recordings/torrent_2.txt");

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			//dos.writeBytes("Content-Disposition: form-data; name=\"upload\"; filename=\"" + mediaFileName + "\"");
			dos.writeBytes("Content-Disposition: form-data; name=\"upload\"; filename=\"\"");
			dos.writeBytes(lineEnd);
			//dos.writeBytes("Content-Type: video/mp4");
			dos.writeBytes(lineEnd);
			dos.writeBytes(lineEnd);


/*			// create a buffer of maximum size
			int bytesAvailable = fileInputStream.available();
			int maxBufferSize = 8192;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];

			// read file and write it into form...

			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0)
			{
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes(lineEnd);

			// more headers


			// close streams
			fileInputStream.close();*/
			//dos.flush();
			dos.close();
			InputStream is = conn.getInputStream();
			// retrieve the response from server
			int ch;

			StringBuffer b =new StringBuffer();
			while( ( ch = is.read() ) != -1 ){
				b.append( (char)ch );
			}
			String s=b.toString();
			Log.d(TAG, "Response: " + s);
			
			

		}
		catch (MalformedURLException ex)
		{
			Log.e(TAG, ex.getMessage());
		}

		catch (IOException ioe)
		{
			Log.e(TAG, ioe.getMessage());
		}
	}
}