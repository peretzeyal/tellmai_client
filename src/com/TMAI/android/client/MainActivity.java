package com.TMAI.android.client;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TMAI.android.client.audio.AudioRecorder;
import com.TMAI.android.client.connection.HttpRequest;
import com.TMAI.android.client.data.MemoInfo;
import com.TMAI.android.client.dialog.DialogUtils;
import com.TMAI.android.client.gui.GuiUtils;
import com.TMAI.android.client.gui.GuiUtils.RecordingButtonsState;
import com.TMAI.android.client.prefs.Prefs;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;

public class MainActivity extends BaseMainActivity{
	private static final String TAG = "MainActivity";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_screen);

		//TODO set name from project id
		audioRecorder = new AudioRecorder(MainActivity.this ,audioFolder+ADUIO_FILE_NAME);
		GuiUtils.handler = new Handler();
		//get full audio file path
		//currentAudioFileName = audioRecorder.getAudioFilePath();

		initGui();
		guiUpdate();
		
		//HttpRequest.postData();
		
/*	String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
		String fileName = "sunset.jpg";*/
		/*			UploadFileConnection uploadFileConnection = new UploadFileConnection();
		uploadFileConnection.uploadObject(this, audioFolder, fileName);*/
	    //AmazonS3Client s3Client = new AmazonS3Client( new BasicAWSCredentials( Constants.ACCESS_KEY_ID, Constants.SECRET_KEY ) );
/*		 AWSCredentials myCredentials = new BasicAWSCredentials( "AKIAIZJ3NZHNB4LTL6DA","I3okz8F9rZ7wedthNQQzToPzWQAu7dr12nhMXbP7" );
		 TransferManager tx = new TransferManager(myCredentials);*/

         	//s3Client.createBucket( "tmai_test");
        	
        	//new java.io.File( filePath) );  // Content type is determined by file extension.
        		
			
/*		 Upload myUpload = tx.upload("tmai_test.s3.amazonaws.com", uploadFile.getName(), uploadFile);


		 while (myUpload.isDone() == false) {
		     System.out.println("Transfer: " + myUpload.getDescription());
		     System.out.println("  - State: " + myUpload.getState());
		     System.out.println("  - Progress: " + myUpload.getProgress().getBytesTransfered());
		     // Do work while we wait for our upload to complete...
		     try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }*/

	}

	@Override
	protected void onResume() {
		super.onResume();
		guiUpdate();
	}

	/**
	 * initialize GUI objects
	 */
	private void initGui() {
		initProjectGUI();
		initOptionGUIButtons();
		initRecordGui();

	}

	private void resetProjectObjects(){
		projectIDTV.setText("");
		projectNameTV.setText("");
		projectID="";
		projectName="";
		guiUpdate();
	}
	
	/**
	 * initialize project selection GUI and function
	 */
	private void initProjectGUI(){
		
		projectIDTV = (TextView) findViewById(R.id.project_id_tv);
		projectIDTV.setText("");
		projectIDTV.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable projectText) {
				Log.d(TAG, "project name change");
				projectID = projectText.toString();
				guiUpdate();
			}
		});
		
		projectNameTV = (TextView) findViewById(R.id.project_name_tv);
		projectNameTV.setText("");
		projectNameTV.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable projectText) {
				Log.d(TAG, "project name change");
				projectName = projectText.toString();
				guiUpdate();
			}
		});

		Button selectProjectButton = (Button) findViewById(R.id.select_project_button);
		View projectNameLayout = (View)findViewById(R.id.linearLayout_project_name);

		projectNameLayout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				resetProjectObjects();
				Intent intent = new Intent(MainActivity.this,LocationActivity.class);
				startActivityForResult(intent, LocationActivity.LOCATION_ACTIVITY);
			}
		});
		selectProjectButton.setEnabled(Prefs.isLocationApproved());

		Button enterProjectButton = (Button) findViewById(R.id.enter_project_id_button);
		View projectIDLayout = (View)findViewById(R.id.linearLayout_project_id);
		projectIDLayout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String title = getString(R.string.input_titel_text);
				String msg = getString(R.string.input_message_text);
				resetProjectObjects();
				DialogUtils.createInputDialog(MainActivity.this, title, msg, projectIDTV);
			}
		});

		allowReply = (CheckBox) findViewById(R.id.allow_reply_cb);

		allowReply.setChecked(!Prefs.getContactEmail().equals(""));
		sendButton = (Button) findViewById(R.id.send_button);
		sendButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//TODO upload info and file
				MemoInfo memoInfo = createMemoInfo();
				new uploadFileToServerTask().execute(memoInfo);
			}
		});


	}
	
	/**
	 * initialize the kind and type option buttons
	 */
	private void initOptionGUIButtons(){
		kind1Button = (Button) findViewById(R.id.kind_1_button);
		kind2Button = (Button) findViewById(R.id.kind_2_button);
		kind3Button = (Button) findViewById(R.id.kind_3_button);


		severityRatingBar = (RatingBar) findViewById(R.id.severity_ratingBar);
		
/*		type1Button = (Button) findViewById(R.id.type_1_button);
		type2Button = (Button) findViewById(R.id.type_2_button);
		type3Button = (Button) findViewById(R.id.type_3_button);*/

		OnClickListener kindClickListener = new OnClickListener() {
			@Override
			public void onClick(View button) {
				button.setSelected(!button.isSelected());
			}
		};

/*		OnClickListener typeClickListener = new OnClickListener() {
			@Override
			public void onClick(View button) {
				//only select on at time
				boolean selected = button.isSelected();
				type1Button.setSelected(false);
				type2Button.setSelected(false);
				type3Button.setSelected(false);
				button.setSelected(!selected);
			}
		};*/

		kind1Button.setOnClickListener(kindClickListener);
		kind2Button.setOnClickListener(kindClickListener);
		kind3Button.setOnClickListener(kindClickListener);

/*		type1Button.setOnClickListener(typeClickListener);
		type2Button.setOnClickListener(typeClickListener);
		type3Button.setOnClickListener(typeClickListener);*/
	}
	
	/**
	 * initialize the audio recording and audio playing buttons and actions 
	 */
	private void initRecordGui() {
		startButton = (ImageButton) findViewById(R.id.play_button);
		stopButton = (ImageButton) findViewById(R.id.stop_button);
		recordButton = (ImageButton) findViewById(R.id.record_button);

		GuiUtils.changeButtonState(MainActivity.this, RecordingButtonsState.DISABLED);

		startButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				//context.getDatabasePath(name).getPath();
				audioPlayer.playAudio(audioFolder+ADUIO_FILE_NAME, MainActivity.this);
				GuiUtils.changeButtonState(MainActivity.this, RecordingButtonsState.PLAYING);
			}
		});

		stopButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				audioPlayer.stopAudio();
				if (audioRecorder!=null){
					try {
						audioRecorder.stop();
					} catch (Exception e) {
						Log.d(TAG, "problem stoping the record "+e);
					}
				}
				//GuiUtils.changeButtonState(MainActivity.this, RecordingButtonsState.STOPPED);
				guiUpdate();
			}
		});

		recordButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				//set the button selected if the audioRecorder is not initialized
				view.setSelected(audioRecorder==null);
				if (audioRecorder!=null){
					try {
						audioRecorder.start();
						GuiUtils.changeButtonState(MainActivity.this, RecordingButtonsState.RECORDING);
					} catch (Exception e) {
						Log.d(TAG, "problem recording "+e);
						DialogUtils.createToast(MainActivity.this,getString(R.string.main_error_start_audio));
						guiUpdate();
					}
				}
			}
		});
	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == LocationActivity.LOCATION_ACTIVITY){
			if (data != null){
				String locationName = data.getStringExtra(LocationActivity.LOCATION_NAME);
				if (locationName!=null){
					projectNameTV.setText(locationName);
					guiUpdate();
				}
			}
		}
	}
}
