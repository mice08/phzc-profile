package com.phzc.profile.biz.dao;

import com.phzc.profile.biz.model.Discussion;

import java.util.Map;
import java.util.List;

public interface DiscussionMapper {

	List<Discussion> query(Map<String, Object> param);

	int insert(Discussion discussion);
	
}