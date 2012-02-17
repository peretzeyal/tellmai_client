package com.TMAI.android.client;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.TMAI.android.client.audio.AudioRecorder;
import com.TMAI.android.client.connection.FileUploader;
import com.TMAI.android.client.connection.HttpRequest;
import com.TMAI.android.client.connection.InfoConnection;
import com.TMAI.android.client.data.MemoInfo;
import com.TMAI.android.client.dialog.DialogUtils;
import com.TMAI.android.client.gui.GuiUtils;
import com.TMAI.android.client.gui.ProgressThread;
import com.TMAI.android.client.gui.GuiUtils.RecordingButtonsState;
import com.TMAI.android.client.prefs.Prefs;

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
        Prefs.init(this);

		initGui();
		guiUpdate();
		
		//if the last time the connection was down
		sendUnUploadedFiles(true);
		MemoInfo memoInfo = new MemoInfo();
		memoInfo.setProjectID("1");
		memoInfo.setProjectName("na");
		memoInfo.setCanReply(true);
		memoInfo.setKind("service");
		memoInfo.setSeverity(3);
		loadGuiFromMemo(memoInfo);
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
	
	private void loadGuiFromMemo(MemoInfo memoInfo){
		projectNameTV.setText(memoInfo.getProjectName());
		projectIDTV.setText(memoInfo.getProjectID());

		allowReply.setChecked(memoInfo.getCanReply());
		
		if (memoInfo.getKind().toLowerCase().contains(kind1Button.getText().toString().toLowerCase())){
			kind1Button.setSelected(true);
		}
		if (memoInfo.getKind().toLowerCase().contains(kind2Button.getText().toString().toLowerCase())){
			kind2Button.setSelected(true);
		}
		if (memoInfo.getKind().toLowerCase().contains(kind3Button.getText().toString().toLowerCase())){
			kind3Button.setSelected(true);
		}

		severityRatingBar.setRating(memoInfo.getSeverity());
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
				projectName = projectText.toString();
				guiUpdate();
			}
		});

		//Button selectProjectButton = (Button) findViewById(R.id.select_project_button);
		View projectNameLayout = (View)findViewById(R.id.linearLayout_project_name);

		projectNameLayout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				resetProjectObjects();
				Intent intent = new Intent(MainActivity.this,LocationActivity.class);
				startActivityForResult(intent, LocationActivity.LOCATION_ACTIVITY);
			}
		});
		//selectProjectButton.setEnabled(Prefs.isLocationApproved());

		//Button enterProjectButton = (Button) findViewById(R.id.enter_project_id_button);
		View projectIDLayout = (View)findViewById(R.id.linearLayout_project_id);
		projectIDLayout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String title = getString(R.string.input_titel_text);
				String msg = getString(R.string.input_message_text);
				resetProjectObjects();
				DialogUtils.createInputDialog(MainActivity.this, title, msg, "", projectIDTV, projectNameTV);
			}
		});

		final Button changeEmail = (Button) findViewById(R.id.change_email_button);
		changeEmail.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				DialogUtils.createChangeEmailDialog(MainActivity.this);
			}
		});
		
		allowReply = (CheckBox) findViewById(R.id.allow_reply_cb);
		allowReply.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					changeEmail.setVisibility(View.VISIBLE);
				}
				else{
					changeEmail.setVisibility(View.INVISIBLE);
				}
			}
		});
		String currentEmail = Prefs.getContactEmail();
		allowReply.setChecked(currentEmail!=null && !currentEmail.equals(""));

		
		sendButton = (Button) findViewById(R.id.send_button);
		sendButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//save the current audio and MemoInfo files
				DialogUtils.createAnotherMemoDialog(MainActivity.this);
				savingAudioAndMemoInfoFiles();
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
		severityRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				if(rating==0){
					//don't allow 0 rating
					ratingBar.setRating(1);
				}
			}
		});
		
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
		duraionProgressBar = (ProgressBar) findViewById(R.id.duration_progressBar);

		GuiUtils.changeButtonState(MainActivity.this, RecordingButtonsState.DISABLED);

		startButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				//context.getDatabasePath(name).getPath();
				int audioTime = audioPlayer.playAudio(audioFolder+ADUIO_FILE_NAME, MainActivity.this);
				GuiUtils.changeButtonState(MainActivity.this, RecordingButtonsState.PLAYING);
				GuiUtils.startDuraionProgressBarTimer(MainActivity.this, audioTime);
			}
		});

		stopButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				GuiUtils.stopDuraionProgressBarTimer();
				Log.d(TAG, "stop aduio "+audioPlayer);

				audioPlayer.stopAudio();
				Log.d(TAG, "stop audioRecorder "+audioRecorder);

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
						//duraionProgressBar.setMax(AudioRecorder.connectionTimeOutPeriod);
						GuiUtils.startDuraionProgressBarTimer(MainActivity.this, AudioRecorder.connectionTimeOutPeriod/1000);
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
	
	
	
	 @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
			 closeApp();
		 }
		return super.onKeyDown(keyCode, event);
	}


}
