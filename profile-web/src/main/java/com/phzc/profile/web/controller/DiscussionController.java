package com.phzc.profile.web.controller;

import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.profile.biz.model.Discussion;
import com.phzc.profile.biz.service.DiscussionService;
import com.phzc.profile.web.form.DiscussionForm;
import com.phzc.ubs.common.facade.model.UserInfoDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/discussion/*")
public class DiscussionController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(DiscussionController.class);
	
	@Autowired
	private DiscussionService discussionService;

	/**
	 * 我要吐槽多条件查询
	 * @param param
	 * @return
	 */
	@RequestMapping(value="query.do", method = RequestMethod.GET)
	public String query(Model model,HttpServletRequest request){
		logger.info("[DiscussionController-->query]");
		Map<String, Object> param=new HashMap<String, Object>();
		User user = userSessionFacade.getUser(request);
		param.put("custId", user.getCustId());
		UserInfoDto userInfo = null;
		try {
//			userInfo = queryUserInfoByCustIdFromUbs("20150629205256","000001");
			userInfo = queryUserInfoByCustIdFromUbs(user.getCustId(),user.getOperId());
		} catch (ServiceException e) {
			logger.error("吐槽查询UBS用户信息异常!",e);
			e.printStackTrace();
		}
		model.addAttribute("config", applicationPropertyConfig);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("disList", this.getDisList(param));
		return "information/viewDiscussionInfo.ftl";
	}
	
	@RequestMapping(value="submitInit.do", method = RequestMethod.GET)
    public String submitInit(Model model, HttpServletRequest request, HttpServletResponse response) {
		logger.info("[DiscussionController-->submitInit]");
        model.addAttribute("config", applicationPropertyConfig);
        return "information/submitDiscussion.ftl";
    }
	
	
	/**
	 * 我要吐槽提交
	 * @param param
	 * @return
	 */
	@RequestMapping(value="submit.do", method = RequestMethod.POST)
	public String submit(Model model,DiscussionForm discussionForm,HttpServletRequest request){
		logger.info("[DiscussionController-->submit]");
		User user = userSessionFacade.getUser(request);
		Discussion dis=new Discussion();
		dis.setDisContent(discussionForm.getContent());
		//0:不回复，1：回复
		if(discussionForm.getConnectflag()==null||"".equals(discussionForm.getConnectflag())){
			dis.setDisConnectflag(0);
		}else{
			dis.setDisConnectflag(discussionForm.getConnectflag());
		}
		dis.setDisDealstate(0);
		dis.setDisMobilephone(user.getMobileNo());
		dis.setCustId(user.getCustId());
		dis.setDisCreatetime(new Timestamp(System.currentTimeMillis()));
		try {
			discussionService.insert(dis);
		} catch (Exception e) {
			logger.error("我的吐槽插入失败!",e);
		}
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("custId", user.getCustId());
		model.addAttribute("config", applicationPropertyConfig);
		model.addAttribute("disList", this.getDisList(param));
		return "information/viewDiscussionInfo.ftl";
	}
	
	public List<Discussion> getDisList(Map<String, Object> param){
		logger.info("[DiscussionController-->getDisList]");
		List<Discussion> list = new ArrayList<Discussion>();
		try {
			list = discussionService.query(param);
		} catch (Exception e) {
			logger.error("我的吐槽查询失败!",e);
		}
		return list;
	}

}