package com.phzc.profile.biz.model;

import java.sql.Timestamp;

public class IdentityAttachInfoRecord {
	private Integer attachId;
	private Integer identityId;
	private String fileType;
	private String fileName;
	private String userId;
	private String filePath;
	private String fileStat;
	private Timestamp updDatetime;
	private String attachFile;
	private String fileDesc;
	
	public String getFileDesc() {
		return fileDesc;
	}
	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}
	public String getAttachFile() {
		return attachFile;
	}
	public void setAttachFile(String attachFile) {
		this.attachFile = attachFile;
	}
	public Integer getAttachId() {
		return attachId;
	}
	public String getFileType() {
		return fileType;
	}
	public String getUserId() {
		return userId;
	}
	public String getFileName() {
		return fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public String getFileStat() {
		return fileStat;
	}
	public Timestamp getUpdDatetime() {
		return updDatetime;
	}
	public void setAttachId(Integer attachId) {
		this.attachId = attachId;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public void setFileStat(String fileStat) {
		this.fileStat = fileStat;
	}
	public void setUpdDatetime(Timestamp updDatetime) {
		this.updDatetime = updDatetime;
	}
	public Integer getIdentityId() {
		return identityId;
	}
	public void setIdentityId(Integer identityId) {
		this.identityId = identityId;
	}
	
}
