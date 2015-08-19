package com.phzc.profile.web.controller;

import com.phzc.profile.biz.constants.CommonConstants;
import com.phzc.profile.biz.model.Discussion;
import com.phzc.profile.biz.service.DiscussionService;
import com.phzc.profile.web.dto.GetDiscussionResponseDTO;
import com.phzc.profile.web.form.DiscussionForm;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/api/*")
public class DiscussionMobileController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(DiscussionMobileController.class);
	
	@Autowired
	private DiscussionService discussionService;

	/**
	 * 我要吐槽提交
	 * @param param
	 * @return
	 */
	@RequestMapping(value="getDiscussionList.do", method = RequestMethod.GET)
	@ResponseBody	
	public GetDiscussionResponseDTO getDiscussionList(HttpServletRequest request){
		logger.info("[DiscussionMobileController-->getDiscussionList]");
		Map<String, Object> param=new HashMap<String, Object>();
		GetDiscussionResponseDTO getDiscussionResponseDTO = new GetDiscussionResponseDTO();
		String custId=request.getParameter("custId");
		String pageNum=request.getParameter("pageNum");
		String pageSize=request.getParameter("pageSize");
		String mobilePhone=request.getParameter("mobilePhone");
		String Token=request.getParameter("Token");
		logger.info("custId:"+custId);
		logger.info("pageNum:"+pageNum);
		logger.info("pageSize:"+pageSize);
		logger.info("mobilePhone:"+mobilePhone);
		logger.info("token:"+Token);
		int startRowNum = 0;
		try{
			if(StringUtils.isNotBlank(pageNum)&&StringUtils.isNotBlank(pageSize)){
				startRowNum=Integer.parseInt(pageNum)*Integer.parseInt(pageSize);
			}
			if(StringUtils.isBlank(custId)||StringUtils.isBlank(pageNum)||
					StringUtils.isBlank(pageSize)||StringUtils.isBlank(mobilePhone)||					
					StringUtils.isBlank(Token)){
				logger.error("我要吐槽多条件查询参数不正确：custId,mobilePhone,pageNum,pageSize,Token 5个参数必须填写");
				getDiscussionResponseDTO.setRespCode(CommonConstants.RESULT_FAIL_CODE);
				getDiscussionResponseDTO.setRespDesc("吐槽多条件查询参数不正确：custId,mobilePhone,pageNum,pageSize,Token 5个参数必须填写");
				return getDiscussionResponseDTO;
			}
			param.put("custId", custId);
			param.put("disMobilephone", mobilePhone);
			int totalCount = this.getDisList(param).size();
			param.put("startRowNum", startRowNum);
			param.put("pageNum", Integer.parseInt(pageNum));
			List<Discussion> discussionList = this.getDisList(param);
			getDiscussionResponseDTO.setRespCode(CommonConstants.RESULT_OK_CODE);
			getDiscussionResponseDTO.setRespDesc(CommonConstants.RESULT_OK_VALUE);
			getDiscussionResponseDTO.setTotalCount(totalCount);
			getDiscussionResponseDTO.setDiscussionList(discussionList);
		}catch(Exception e){
			logger.error("我要吐槽多条件查询异常",e);
			getDiscussionResponseDTO.setRespCode(CommonConstants.RESULT_FAIL_CODE);
			getDiscussionResponseDTO.setRespDesc(CommonConstants.RESULT_FAIL_VALUE);
		}
		return getDiscussionResponseDTO;
	}
	
	
	/**
	 * 我要吐槽提交
	 * @param param
	 * @return
	 */
	@RequestMapping(value="submitDiscussion.do", method = RequestMethod.POST)
	@ResponseBody	
	public GetDiscussionResponseDTO submit(DiscussionForm discussionForm){
		logger.info("[DiscussionMobileController-->submitDiscussion]");
		GetDiscussionResponseDTO getDiscussionResponseDTO = new GetDiscussionResponseDTO();
		try {
			logger.info("custId:"+discussionForm.getCustId());
			logger.info("Content:"+discussionForm.getContent());
			logger.info("connectflag:"+discussionForm.getConnectflag());
			logger.info("mobilePhone:"+discussionForm.getMobilePhone());
			logger.info("Token:"+discussionForm.getToken());
			if(StringUtils.isBlank(discussionForm.getCustId())||StringUtils.isBlank(discussionForm.getContent())||
					discussionForm.getConnectflag()==null||StringUtils.isBlank(discussionForm.getMobilePhone())||					
					StringUtils.isBlank(discussionForm.getToken())){
				logger.error("吐槽插入参数不正确：custId,content,connectflag,mobilePhone,token 5个参数必须填写");
				getDiscussionResponseDTO.setRespCode(CommonConstants.RESULT_FAIL_CODE);
				getDiscussionResponseDTO.setRespDesc("吐槽插入参数不正确：custId,content,connectflag,mobilePhone,token 5个参数必须填写");
				return getDiscussionResponseDTO;
			}

			if(discussionForm.getContent().length()>500){
				getDiscussionResponseDTO.setRespCode(CommonConstants.RESULT_FAIL_CODE);
				getDiscussionResponseDTO.setRespDesc("吐槽内容不能超过500字！");
				return getDiscussionResponseDTO;
			}
			
			Discussion dis=new Discussion();
			dis.setDisContent(discussionForm.getContent());
			dis.setDisMail(discussionForm.getMail());
			dis.setDisMobilephone(discussionForm.getMobilePhone());
			dis.setCustId(discussionForm.getCustId());
			//0不联系，1联系，选中为1
			dis.setDisConnectflag(discussionForm.getConnectflag());
			dis.setDisDealstate(0);
			dis.setDisCreatetime(new Timestamp(System.currentTimeMillis()));
			discussionService.insert(dis);
			getDiscussionResponseDTO.setRespCode(CommonConstants.RESULT_OK_CODE);
			getDiscussionResponseDTO.setRespDesc(CommonConstants.RESULT_OK_VALUE);
		} catch (Exception e) {
			logger.error("我的吐槽插入失败!",e);
			getDiscussionResponseDTO.setRespCode(CommonConstants.RESULT_FAIL_CODE);
			getDiscussionResponseDTO.setRespDesc(CommonConstants.RESULT_FAIL_VALUE);
		}
		return getDiscussionResponseDTO;
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