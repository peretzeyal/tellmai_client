package com.TMAI.android.client;

import java.io.File;

import android.app.Activity;
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
import com.TMAI.android.client.data.MemoInfo;
import com.TMAI.android.client.dialog.DialogUtils;
import com.TMAI.android.client.gui.GuiUtils;
import com.TMAI.android.client.gui.GuiUtils.RecordingButtonsState;
import com.TMAI.android.client.location.DeviceLocation;
import com.TMAI.android.client.prefs.Prefs;
import com.TMAI.android.client.utils.GeneralUtils;

public class BaseMainActivity extends Activity{

	private static final String TAG = "BaseMainActivity";

	protected static final String ADUIO_FILE_NAME = "record.3gp";
	//protected static final String ADUIO_FOLDER = "";
	protected static String audioFolder;

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
	RatingBar severityRatingBar;
	//gui send button
	public Button sendButton;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		audioFolder = GeneralUtils.getAudioFolder();
		if (audioFolder==null){
			//close app the device has no external storage to use
			closeApp();
		}
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
	 * execute the file and info upload to the server on AsyncTask
	 */
	protected class uploadFileToServerTask extends AsyncTask<MemoInfo, Void, Boolean>{

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result){
				//the file and the info updated successfully
				GeneralUtils.deleteAudioFile(audioFolder+ADUIO_FILE_NAME);
				DialogUtils.createToast(BaseMainActivity.this, getString(R.string.toast_file_was_uploaded_successfully));
			}
			else{
				//problem updating the file and info
				DialogUtils.createToast(BaseMainActivity.this, getString(R.string.toast_file_was_not_uploaded_successfully));
			}
			guiUpdate();
		}

		protected Boolean doInBackground(MemoInfo... paramsMemoInfo) {
			MemoInfo memoInfo = paramsMemoInfo[0];
			UploadFileConnection uploadFileConnection = new UploadFileConnection();
			String fileURL = uploadFileConnection.uploadAudioFile(BaseMainActivity.this, audioFolder, ADUIO_FILE_NAME);
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
		String kindText = GuiUtils.getSelectedButtonText(kind1Button, ",")+
		GuiUtils.getSelectedButtonText(kind2Button, ",")+
		GuiUtils.getSelectedButtonText(kind3Button, ",");
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
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
