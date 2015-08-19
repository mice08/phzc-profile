package com.phzc.profile.web.dto;

import java.util.List;

import com.phzc.profile.biz.model.Discussion;

public class GetDiscussionResponseDTO {
	private String respCode;
	private String respDesc;
	private Integer totalCount;
	private List<Discussion> discussionList;
	
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespDesc() {
		return respDesc;
	}
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public List<Discussion> getDiscussionList() {
		return discussionList;
	}
	public void setDiscussionList(List<Discussion> discussionList) {
		this.discussionList = discussionList;
	}


	
	
}
