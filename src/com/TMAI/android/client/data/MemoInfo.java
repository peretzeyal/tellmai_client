package com.TMAI.android.client.data;

public class MemoInfo {

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

	public MemoInfo(String email, String projectName, String projectID,
			String kind, int severity, double latitude, double longitude,
			boolean canReply, String fileUrl) {
		super();
		this.email = email;
		this.projectName = projectName;
		this.projectID = projectID;
		this.kind = kind;
		this.severity = severity;
		this.latitude = latitude;
		this.longitude = longitude;
		this.canReply = canReply;
		this.fileUrl = fileUrl;
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
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
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
