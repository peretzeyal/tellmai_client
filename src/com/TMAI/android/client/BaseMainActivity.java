package com.TMAI.android.client;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TMAI.android.client.audio.AudioPlayer;
import com.TMAI.android.client.audio.AudioRecorder;
import com.TMAI.android.client.connection.InfoConnection;
import com.TMAI.android.client.connection.UploadFileConnection;
import com.TMAI.android.client.data.EntityValidationResult;
import com.TMAI.android.client.data.MemoInfo;
import com.TMAI.android.client.dialog.DialogUtils;
import com.TMAI.android.client.gui.GuiUtils;
import com.TMAI.android.client.gui.NotificationUtils;
import com.TMAI.android.client.gui.GuiUtils.RecordingButtonsState;
import com.TMAI.android.client.location.DeviceLocation;
import com.TMAI.android.client.prefs.Prefs;
import com.TMAI.android.client.utils.GeneralUtils;

public class BaseMainActivity extends BaseAppActivity{

	private static final String TAG = "BaseMainActivity";

	//original file name
	protected static final String ADUIO_FILE_SUFFIX = ".3gp";  
	protected static final String ADUIO_FILE_NAME = "record.3gp";
	//protected static final String ADUIO_FOLDER = "";
	protected static String audioFolder;
	protected static String oldAudioFolder;

	//audio file objects
	protected AudioRecorder audioRecorder;
	protected AudioPlayer audioPlayer = new AudioPlayer();

	protected String projectID = "";
	protected String projectName = "";

	//gui objects
	public TextView projectNameTV;
	public TextView projectIDTV;

	protected CheckBox allowReply;
	//gui record buttons
	public ImageButton startButton;
	public ImageButton stopButton;
	public ImageButton recordButton;
	public ProgressBar duraionProgressBar;

	//gui kind buttons
	protected Button kind1Button;
	protected Button kind2Button;
	protected Button kind3Button;	
	/*	//gui type buttons
	protected Button type1Button;
	protected Button type2Button;
	protected Button type3Button;	*/
	//gui rating bar
	protected RatingBar severityRatingBar;
	//gui send button
	public Button sendButton;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		audioFolder = GeneralUtils.getAudioFolder();
		oldAudioFolder = GeneralUtils.getOldAudioFolder();
		if (audioFolder==null||oldAudioFolder==null){
			//close app the device has no external storage to use
			closeApp();
		}
	}

	protected void resetProjectObjects(){
		projectIDTV.setText("");
		projectNameTV.setText("");
		projectID="";
		projectName="";
		guiUpdate();
	}
	
	protected void resetOptionObjects(){
		kind1Button.setSelected(false);
		kind2Button.setSelected(false);
		kind3Button.setSelected(false);
		severityRatingBar.setRating(1);
	}

	
	/**
	 * update the current gui according to project/recording state
	 */
	protected void guiUpdate(){
		//check project name
		boolean projectNameEnaled = !projectName.equals("")||!projectID.equals("");
		if (!projectNameEnaled){
			GuiUtils.changeButtonState(BaseMainActivity.this, RecordingButtonsState.DISABLED);
			return;
		}
		//check if recording file exists
		File file = new File(audioFolder+ADUIO_FILE_NAME);
		boolean audioFileEnabled = file.exists();
		if (projectNameEnaled && audioFileEnabled){
			GuiUtils.changeButtonState(BaseMainActivity.this, RecordingButtonsState.STOPPED);
		}
		else if(projectNameEnaled){
			GuiUtils.changeButtonState(BaseMainActivity.this, RecordingButtonsState.NO_RECORDING);
		}
	}


	/**
	 * saving the current file and MemoInfo to the old folder
	 */
	protected void savingAudioAndMemoInfoFiles(){
		MemoInfo memoInfo = createMemoInfo();
		String newFileName = GuiUtils.getDateAndTime(BaseMainActivity.this)+"_"+
			GeneralUtils.getProjectInfo(memoInfo);//projectID+projectName;
		GeneralUtils.moveFile(audioFolder, ADUIO_FILE_NAME, oldAudioFolder, newFileName+ADUIO_FILE_SUFFIX);
		GeneralUtils.saveMemoInfo(memoInfo, oldAudioFolder, newFileName+MemoInfo.FILE_SUFFIX);
		resetProjectObjects();
		sendUnUploadedFiles(true);
	}

	/**
	 * checks for unuploaded audio and MemInfo files and start uploading
	 * @param excuteUplad = true allows upload ,false only to check for upload files
	 */
	protected boolean sendUnUploadedFiles(boolean excuteUplad){
		boolean uploadFilesExists = false;
		File folder = new File(oldAudioFolder);
		if (folder.exists()){
			for (File file : folder.listFiles()) {
				if (file.getName().toLowerCase().endsWith(MemoInfo.FILE_SUFFIX.toLowerCase())){
					//this is a MemoInfo file
					String memoinfoFile = file.getAbsolutePath();
					String audioFile = oldAudioFolder+GeneralUtils.removeExtention(file.getName()) + ADUIO_FILE_SUFFIX;
					if (!(new File(audioFile)).exists()){
						//the audio file dosen't exists delete the MemoInfo file
						GeneralUtils.deleteFile(memoinfoFile);
						continue;
					}
					uploadFilesExists = true;
					//MemoInfo memoInfo = GeneralUtils.readMemoInfo(memoinfoFile);
					//both aduio and MemoInfo file exists upload them
					if(excuteUplad){
						new uploadFileToServerTask().execute(GeneralUtils.removeExtention(file.getName()));
						return uploadFilesExists;
					}
				}
			}
		}
		return uploadFilesExists;
	}


	
	/**
	 * execute the file and info upload to the server on AsyncTask
	 */
	protected class uploadFileToServerTask extends AsyncTask<String, Void, Boolean>{

		String memoinfoFileName = "";
		String audioFileName = "";
		String memoInfoMsg = "";

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result==null){
				//entity dosn't exists
				//show a notification to the user
				NotificationUtils.addEntityValidationNotification(BaseMainActivity.this,memoInfoMsg);
			}
			else if (result){
				//the file and the info updated successfully
				GeneralUtils.deleteFile(oldAudioFolder+memoinfoFileName);
				GeneralUtils.deleteFile(oldAudioFolder+audioFileName);
				//DialogUtils.createToast(BaseMainActivity.this, getString(R.string.toast_file_was_uploaded_successfully));
				NotificationUtils.addUploadSuccessfullyNotification(BaseMainActivity.this,memoInfoMsg);
			}
			else {
				//problem updating the file and info
				//DialogUtils.createToast(BaseMainActivity.this, getString(R.string.toast_file_was_not_uploaded_successfully));
			}
			guiUpdate();
			//if app in the background
			if (!appInForeground){
				//wait 10 seconds if the app is still in the background close it
				Timer timeOut = new Timer();
				timeOut.schedule(new TimerTask() {
					@Override
					public void run() {
						if (!appInForeground){
							closeApp();
						}
					}
				}, 10*1000);
			}

		}

		protected Boolean doInBackground(String... str) {
			//file name without extensions
			memoinfoFileName = str[0] + MemoInfo.FILE_SUFFIX;
			audioFileName = str[0] + ADUIO_FILE_SUFFIX;
			MemoInfo memoInfo = GeneralUtils.readMemoInfo(oldAudioFolder+memoinfoFileName);
			memoInfoMsg = GeneralUtils.getProjectInfo(memoInfo);
			
			if (memoInfo.getProjectID()!=null && !memoInfo.getProjectID().equals("")){
				//if we are using entity ID and not entity name check it exists
				EntityValidationResult entityValidationResult = InfoConnection.entityValidation(memoInfo.getProjectID());
				switch (entityValidationResult.getEntityValidationResultType()) {
				case ENTITY_EXISTS:
					//entity id exists
					break;
				case ENTITY_DOES_NOT_EXISTS:
					//entity dosn't exists
					return null;
				case CONNECTION_PROBLEM:
					//problem connecting to the server
					return null;
				}
			}
			
			UploadFileConnection uploadFileConnection = new UploadFileConnection();
			String fileURL = uploadFileConnection.uploadAudioFile(BaseMainActivity.this, oldAudioFolder, audioFileName);
			//uploadFileConnection.uploadObject(BaseMainActivity.this, audioFolder, ADUIO_FILE_NAME);
			if (fileURL != null){
				//file was uploaded to the server, update info server
				memoInfo.setFileUrl(fileURL);
				boolean infoUpdated = InfoConnection.uploadMemoInfo(memoInfo);
				if (infoUpdated){
					return true;
				}
			}
			return false;
		}

	}

	protected MemoInfo createMemoInfo(){
		MemoInfo memoInfo = new MemoInfo();
		memoInfo.setProjectID(projectID);
		memoInfo.setProjectName(projectName);
		String email = Prefs.getContactEmail();
		if (email==null||email.equals("")){
			email = GeneralUtils.getNativeContactEmail(BaseMainActivity.this);
		}
		memoInfo.setEmail(email);

		//returns the value of the kind buttons
		String kindText = GuiUtils.getSelectedButtonText(kind1Button, "-")+
		GuiUtils.getSelectedButtonText(kind2Button, "-")+
		GuiUtils.getSelectedButtonText(kind3Button, "-");
		if (kindText.endsWith("-")){
			kindText=kindText.substring(0,kindText.length()-1);
		}
		memoInfo.setKind(kindText);

		memoInfo.setSeverity((int) severityRatingBar.getRating());

		//get current location
		Location location = DeviceLocation.getLocation(BaseMainActivity.this);
		if (location!=null){
			memoInfo.setLatitude(location.getLatitude());
			memoInfo.setLongitude(location.getLongitude());
		}

		memoInfo.setCanReply(allowReply.isChecked());

		//we can't set the file url until the file is uploaded to the server
		memoInfo.setFileUrl("");
		return memoInfo;
	}

	protected void closeApp(){
		Log.d(TAG, "Closing "+getString(R.string.app_name));
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
