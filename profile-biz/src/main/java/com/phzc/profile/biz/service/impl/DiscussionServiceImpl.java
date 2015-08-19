package com.phzc.profile.biz.service.impl;

import com.phzc.profile.biz.dao.DiscussionMapper;
import com.phzc.profile.biz.model.Discussion;
import com.phzc.profile.biz.service.DiscussionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.UUID;

@Service("discussionService")
public class DiscussionServiceImpl implements DiscussionService {
	
	private static final Logger logger = LoggerFactory.getLogger(DiscussionServiceImpl.class);
	
	@Autowired
	private DiscussionMapper discussionMapper;


	@Override
	public List<Discussion> query(Map<String, Object> param) {
		List<Discussion> list = discussionMapper.query(param);
		return list;
	}

	@Override
	public int insert(Discussion discussion) {
		String uuid = String.valueOf(UUID.randomUUID());
		discussion.setDisSeq(uuid);
		return discussionMapper.insert(discussion);
	}
}