package com.TMAI.android.client.gui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.TMAI.android.client.R;
import com.TMAI.android.client.data.MemoInfo;


public class NotificationUtils {

	private static final int UPLOAD_SUCCESSFULLY_NOTIFICATION_ID = 278;
	private static final int ENTITY_VALIDATION_NOTIFICATION_ID = 279;



	public static void addUploadSuccessfullyNotification(Activity activity, String memoInfoMsg) {

		NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notifyIntent;
		notifyIntent = new Intent();

		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(activity, 0, notifyIntent, 0);
		int iconId = R.drawable.icon;
		String title = activity.getString(R.string.notification_upload_successfully_titel_text);
		String msg = activity.getString(R.string.notification_upload_successfully_msg_text);
		msg = msg.replace("***", memoInfoMsg);
		Notification notification = new Notification(iconId, null, 0);
		notification.setLatestEventInfo(activity, title, msg, intent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(UPLOAD_SUCCESSFULLY_NOTIFICATION_ID, notification);
	}
	
	
	public static void removeUploadSuccessfullyNotification(Activity activity ) {
		NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(UPLOAD_SUCCESSFULLY_NOTIFICATION_ID);
	}
	
	
	public static void addEntityValidationNotification(Activity activity, String memoInfoMsg) {

		NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notifyIntent;
		notifyIntent = new Intent();

		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(activity, 0, notifyIntent, 0);
		int iconId = R.drawable.icon;
		String title = activity.getString(R.string.notification_entity_validation_titel_text);
		String msg = activity.getString(R.string.notification_entity_validation_msg_text);
		msg = msg.replace("***", memoInfoMsg);
		Notification notification = new Notification(iconId, null, 0);
		notification.setLatestEventInfo(activity, title, msg, intent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(ENTITY_VALIDATION_NOTIFICATION_ID, notification);
	}
	
	
	public static void removeEntityValidationNotification(Activity activity ) {
		NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(ENTITY_VALIDATION_NOTIFICATION_ID);
	}
}
