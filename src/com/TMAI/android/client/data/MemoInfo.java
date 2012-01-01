package com.TMAI.android.client.data;

import java.io.Serializable;
import java.text.DecimalFormat;

public class MemoInfo implements Serializable{

	public static final String FILE_SUFFIX = ".mi";  
	
	private String email;
	private String projectName;
	private String projectID;
	private String kind;
	private int severity;
	private double latitude;
	private double longitude;
	private boolean canReply;
	private String fileUrl;

	public MemoInfo() {
		super();
		this.email = "";
		this.projectName = "";
		this.projectID = "";
		this.kind = "";
		this.severity = 0;
		this.latitude = 0;
		this.longitude = 0;
		this.canReply = false;
		this.fileUrl = "";
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectID() {
		return projectID;
	}
	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public int getSeverity() {
		return severity;
	}
	public void setSeverity(int severity) {
		this.severity = severity;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		DecimalFormat decimalFormat = new DecimalFormat("##.######");
		this.latitude = Double.valueOf(decimalFormat.format(latitude));
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		DecimalFormat decimalFormat = new DecimalFormat("##.######");
		this.longitude = Double.valueOf(decimalFormat.format(longitude));
	}
	public boolean getCanReply() {
		return canReply;
	}
	public String getCanReplyString() {
		//yes = 2 , no = 3
		if(canReply){
			return "2";
		}
		return "3";
	}
	public void setCanReply(boolean canReply) {
		this.canReply = canReply;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
}
