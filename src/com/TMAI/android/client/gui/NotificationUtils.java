package com.TMAI.android.client.gui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.TMAI.android.client.R;


public class NotificationUtils {

	private static final int NOTIFICATION_ID = 278;


	public static void addUploadSuccessfullyNotification(Activity activity ) {

		NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notifyIntent;
		notifyIntent = new Intent();

		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(activity, 0, notifyIntent, 0);
		int iconId = R.drawable.icon;
		String title = activity.getString(R.string.notification_upload_successfully_titel_text);
		String msg = activity.getString(R.string.notification_upload_successfully_msg_text);

		Notification notification = new Notification(iconId, null, 0);
		notification.setLatestEventInfo(activity, title, msg, intent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(NOTIFICATION_ID, notification);
	}
	
	public void removeaddUploadSuccessfullyNotification(Activity activity ) {
		NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
	}
}