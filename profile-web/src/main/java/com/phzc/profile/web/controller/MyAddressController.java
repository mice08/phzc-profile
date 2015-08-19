package com.phzc.profile.web.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.console.util.ConsolePropertiesManager;
import com.phzc.profile.biz.service.UserAuthorizeService;
import com.phzc.profile.web.form.AddressForm;
import com.phzc.ubs.common.facade.model.AddressInfoDto;
import com.phzc.ubs.common.facade.model.AddressInfoRequest;
import com.phzc.ubs.common.facade.model.AddressInfoResult;
import com.phzc.ubs.common.facade.model.DeleteRecAddressInfoRequest;
import com.phzc.ubs.common.facade.model.DeleteRecAddressInfoResult;
import com.phzc.ubs.common.facade.model.InsertAddressInfoRequest;
import com.phzc.ubs.common.facade.model.InsertAddressInfoResult;
import com.phzc.ubs.common.facade.model.ModifyRecAddressInfoRequest;
import com.phzc.ubs.common.facade.model.ModifyRecAddressInfoResult;
import com.phzc.ubs.common.facade.service.AddressInfoFacade;

@Controller
@RequestMapping("/address/*")
public class MyAddressController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MyAddressController.class);
	
	@Autowired
	private UserAuthorizeService userAuthorizeService;

	// UBS安全码
	private String ubsConsumerId = ConsolePropertiesManager.getUbsConsumerCode();
	 
	@RequestMapping(value = "queryAddress.do", method = RequestMethod.GET)
	public String queryAddress(Model model , HttpServletRequest request){
		model.addAttribute("config", applicationPropertyConfig);
		
		User user = userSessionFacade.getUser(request);
		String custId = user.getCustId();
		
		
		
		 try {
			AddressInfoFacade addressInfoFacade = (AddressInfoFacade)serviceClientFactory.getWebServiceClient(AddressInfoFacade.class,"addressInfoFacade", "UBS");
			AddressInfoRequest queryReq = new AddressInfoRequest();//查询地址请求
            queryReq.setConsumerId(ubsConsumerId);
            queryReq.setCustId(custId);
            AddressInfoResult  queryResult= addressInfoFacade.queryRecAddress(queryReq);//调用查询地址接口
            if(null != queryResult){//判断返回结果集是否成功
                List<AddressInfoDto> list=queryResult.getAddressInfoList();//结果集
                if (null != list && !list.isEmpty()) {
                	 Collections.sort(list, new Comparator<AddressInfoDto>() {
 	                    public int compare(AddressInfoDto arg0, AddressInfoDto arg1) {
 	                        return arg0.getCreateTime().compareTo(arg1.getCreateTime());
 	                    }
 	                 });
 	                /*for (AddressInfoDto addressInfoDto : list) {
 						Date createTime = addressInfoDto.getCreateTime();
 						String address = addressInfoDto.getAddress();
 						Date updateTime = addressInfoDto.getUpdateTime();
 						logger.info("createtime:"+createTime.toString());
 						logger.info("updateTime:" + updateTime.toString());
 						logger.info("地址是:"+address);
 					}*/
                }
                
                
                String respCode = queryResult.getRespCode();//响应结果
                String respDesc = queryResult.getRespDesc();//响应结果描述
                
                if(!"000".equals(respCode)){//如果查询结果没有成功
                	logger.info("查询结果没有成功:"+respCode + ":" + respDesc);
                	model.addAttribute("fail", respDesc);
                }
                model.addAttribute("list", list);
            }
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		
		return "address/myAddress.ftl";
	}
	
	@RequestMapping(value = "addAddress.do", method = RequestMethod.POST)
	public String addAddress(Model model, AddressForm addressForm , HttpServletRequest request){
		model.addAttribute("config", applicationPropertyConfig);
		
		String msg="添加失败!";
		
		if(StringUtils.isBlank(addressForm.getName())){
			model.addAttribute("fail", "请输入收货人姓名");
        }else if(addressForm.getName().length()>25){
        	model.addAttribute("fail", "收货人姓名长度超过25个字符");
        }
        if (StringUtils.isBlank(addressForm.getAddress())){
        	model.addAttribute("fail", "收货地址不能为空");
        }
        if (StringUtils.isBlank(addressForm.getPhone()) && StringUtils.isBlank(addressForm.getTelephone())) {
        	model.addAttribute("fail", "手机、座机号码至少有一项必填");
        } else if (StringUtils.trimToEmpty(addressForm.getPhone()).length() > 20
                   || StringUtils.trimToEmpty(addressForm.getTelephone()).length() > 20) {
        	model.addAttribute("fail", "手机、座机号码至少有一项长度超过20个字符");
        }
        
        Integer addressId = addressForm.getAddressId();
        
        /*** AddressId()为null 新增 **/
        if(null == addressForm.getAddressId()){
        	try {
    			AddressInfoFacade addressInfoFacade = (AddressInfoFacade)serviceClientFactory.getWebServiceClient(AddressInfoFacade.class,
    			       "addressInfoFacade ", "UBS");
    			
    			InsertAddressInfoRequest insertReq = new InsertAddressInfoRequest();
    			User user = userSessionFacade.getUser(request);
    			String custId = user.getCustId();
    		       insertReq.setConsumerId(ubsConsumerId);
    		       insertReq.setCustId(custId);
    		       insertReq.setName(addressForm.getName());//收件人姓名          
    		       insertReq.setIsDefault(addressForm.getIsDefault());//默认收货地址
    		       insertReq.setProvince(addressForm.getProvince());
    		       insertReq.setCity(addressForm.getCity());
    		       insertReq.setArea(addressForm.getArea());//所在区域
    		       insertReq.setAddress(addressForm.getAddress());//详细地址
    		       
    		       insertReq.setPhone(addressForm.getPhone());//固定电话
    		       insertReq.setTelephone(addressForm.getTelephone());//手机号码
    		       insertReq.setPostCode(addressForm.getPostCode());//邮政编码
    		       InsertAddressInfoResult insertAddress=addressInfoFacade.addRecAddress(insertReq);
    		       if(null!=insertAddress){
    		    	   String respCode = insertAddress.getRespCode();
    		           String respDesc = insertAddress.getRespDesc();
    		           if(!"000".equals(respCode)){
    		               msg="添加收货地址失败!";
    		               logger.info(msg + respCode + ":" + respDesc);
    		               addressForm.setErrorCode(Integer.parseInt(respCode));
    		               addressForm.setErrorString(respDesc);
    		               model.addAttribute("addressForm", addressForm);
    		               return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
    		           }else{
    		        	   msg="添加收货地址成功!";
    		        	   logger.info(msg);
    		        	   return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
    		           }
    		       } 
    		} catch (ServiceException e) {
    			logger.error(e.getMessage(), e);
    			e.printStackTrace();
    		}
        }
        
        /** update***/
        String isDefault="N"; 
        if("Y".equals(addressForm.getIsDefault()) ){
            isDefault="Y";
        }
        
        try{
        	AddressInfoFacade ubsAddressInfoService = (AddressInfoFacade)serviceClientFactory.getWebServiceClient(AddressInfoFacade.class,
                    "addressInfoFacade ", "UBS");
        	String custId = userSessionFacade.getUser(request).getCustId();
            ModifyRecAddressInfoRequest updateReq = new ModifyRecAddressInfoRequest();//修改地址请求
        	updateReq.setConsumerId(ubsConsumerId);
            updateReq.setCustId(custId);
            updateReq.setAddressId(addressId.toString());
            updateReq.setName(addressForm.getName());
            updateReq.setTelephone(addressForm.getTelephone());
            updateReq.setProvince(addressForm.getProvince());
            updateReq.setCity(addressForm.getCity());
            updateReq.setArea(addressForm.getArea());//所在区域
            updateReq.setPhone(addressForm.getPhone());
            updateReq.setPostCode(addressForm.getPostCode());
            updateReq.setAddress(addressForm.getAddress());
            updateReq.setIsDefault(isDefault);
            
            ModifyRecAddressInfoResult  updateResult= ubsAddressInfoService.modifyRecAddress(updateReq);//调用查询地址接口
            if(null != updateResult){//判断返回结果是否成功
                
                String respCode = updateResult.getRespCode();//响应结果
                String respDesc = updateResult.getRespDesc();
                if(!"000".equals(respCode)){//如果查询结果没有成功
                	logger.info("更新没有成功"+respCode +":" + respDesc );
                    return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
                }else{
                	logger.info("更新成功++");
                	return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
                }
            }
        }catch (ServiceException e) {
        	 logger.error(e.getMessage(), e);
        	 e.printStackTrace();
        	 return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
        }
        
		return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
	}
	
	@RequestMapping(value = "deleteAddress.do")
	public String deleteAddress(Model model , HttpServletRequest request ){
		model.addAttribute("config", applicationPropertyConfig);
		
		String addressId = (String) request.getParameter("addressId");
		
	        try {
	            
	        	String custId = userSessionFacade.getUser(request).getCustId();
	            
	            if(addressId==null){
	            	
	            	return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
	            }
	            AddressInfoFacade addressInfoFacade = (AddressInfoFacade)serviceClientFactory.getWebServiceClient(AddressInfoFacade.class,
	                "addressInfoFacade ", "UBS");
	            DeleteRecAddressInfoRequest deleteReq=new DeleteRecAddressInfoRequest();
	           
	            /*if(UserSessionFactory.getUserSession(req)!=null){      //判断当前登录名是否为空 不为空则获取custId 
	                custId = UserSessionFactory.getUserSession(req).getCustId();
	            }else{
	                deleteAddressForm.setErrorCode(ErrorCode.VALIDATOR_PARAM_ERROR);//为空则显示登录超时
	                deleteAddressForm.setErrorString("登录超时，请重新登录！");               
	                return mapping.getInputForward();
	            }*/
	            deleteReq.setConsumerId(ubsConsumerId);
	            deleteReq.setCustId(custId);
	            deleteReq.setAddressId(addressId);
	          DeleteRecAddressInfoResult deleteAddress=addressInfoFacade.deleteRecAddress(deleteReq);
	          if(null!=deleteAddress){
	             logger.info("删除成功!");
	          }else if("N".equals(deleteAddress)){
	              logger.info("删除失败");
	          }  
	      } catch (ServiceException e) {
	    	  logger.error(e.getMessage(), e);
	          e.printStackTrace();
	          return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
	      } catch (Exception e) {
	    	  logger.error(e.getMessage(), e);
	          e.printStackTrace();
	      }
		return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
	}
	
	
	@RequestMapping(value = "queryAddressById.do")
	public @ResponseBody JSONObject queryAddressById(Model model , @RequestParam String addressId, HttpServletRequest request  ){
		model.addAttribute("config", applicationPropertyConfig);
		
		String custId = userSessionFacade.getUser(request).getCustId();
		System.out.println("custid"+ custId);
		System.out.println("addressid "+ addressId);
		JSONObject jsonObj = new JSONObject();
		try {
            
	          //判断传入参数custId是否为空
				if(null==addressId){
					jsonObj.put("success", false);
                	return jsonObj;
	            }
	            AddressInfoFacade addressInfoFacade = (AddressInfoFacade)serviceClientFactory.getWebServiceClient(AddressInfoFacade.class,"addressInfoFacade", "UBS");
	            AddressInfoRequest queryReq = new AddressInfoRequest();//查询地址请求
	            queryReq.setConsumerId(ubsConsumerId);
	            queryReq.setCustId(custId);
	            queryReq.setAddressId(addressId);
	            AddressInfoResult  queryResult= addressInfoFacade.queryRecAddress(queryReq);//调用查询地址接口
	            if(null != queryResult){//判断返回结果集是否成功
	                List<AddressInfoDto> list=queryResult.getAddressInfoList();//结果集
	                String respCode = queryResult.getRespCode();//响应结果
	                String respDesc = queryResult.getRespDesc();
	                if(!"000".equals(respCode)){//如果查询结果没有成功
	                	logger.info("查询结果没有成功" + respCode + ":" + respDesc);
	                	jsonObj.put("success", false);
	                	return jsonObj;
	                }
	                AddressInfoDto address=list.get(0);
	                logger.info("省是::"+address.getProvince()+"++");
	                jsonObj.put("success", true);
	                jsonObj.put("address", address);
	                return jsonObj;
	            }
	        } catch (ServiceException e) {
	        	logger.error(e.getMessage(), e);
	            e.printStackTrace();
	            jsonObj.put("success", false);
            	return jsonObj;
	        } catch (Exception e) {
	        	logger.error(e.getMessage(), e);
	            e.printStackTrace();
	        }
		jsonObj.put("success", false);
    	return jsonObj;
	}
	
	@RequestMapping(value = "setDefaultAddress.do")
	public String setDefaultAddress(Model model , HttpServletRequest request ){
		model.addAttribute("config", applicationPropertyConfig);

		String addressId = (String) request.getParameter("addressId");
		
	        try {
	        	String custId = userSessionFacade.getUser(request).getCustId();
	            
	            if(addressId==null){
	            	return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
	            }
	            AddressInfoFacade addressInfoFacade = (AddressInfoFacade)serviceClientFactory.getWebServiceClient(AddressInfoFacade.class,"addressInfoFacade", "UBS");
	            AddressInfoRequest queryReq = new AddressInfoRequest();//查询地址请求
	            queryReq.setCustId(custId);
	            queryReq.setAddressId(addressId);
	            queryReq.setConsumerId(ubsConsumerId);
	            AddressInfoResult  queryResult= addressInfoFacade.queryRecAddress(queryReq);//调用查询地址接口
	            if(null != queryResult){//判断返回结果集是否成功
	                List<AddressInfoDto> list=queryResult.getAddressInfoList();//结果集
	                
	                String respCode = queryResult.getRespCode();//响应结果
	                String respDesc = queryResult.getRespDesc();
	                
	                if(!"000".equals(respCode)){//如果查询结果没有成功
	                   /* addressForm.setErrorCode(Integer.parseInt(respCode));
	                    addressForm.setErrorString(respDesc);
	                    req.setAttribute("addressForm", addressForm);*/
	                	logger.info("查询结果没有成功" + respCode + ":" + respDesc);
	                	return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
	                }
	                AddressInfoDto address=list.get(0);
	                ModifyRecAddressInfoRequest updateReq = new ModifyRecAddressInfoRequest();//修改地址请求
	                updateReq.setCustId(custId);
	                updateReq.setAddressId(addressId.toString());
	                updateReq.setConsumerId(ubsConsumerId);
	                updateReq.setIsDefault("Y"); 
	                
	                //姓名
	                if(null==address.getName() || StringUtils.isBlank(address.getName())){
	                    updateReq.setName("");
	                }else{
	                    updateReq.setName(address.getName());
	                }
	                
	                //手机
	                if(null==address.getTelephone() || StringUtils.isBlank(address.getTelephone())){
	                    updateReq.setTelephone("");
	                }else{
	                    updateReq.setTelephone(address.getTelephone());
	                }
	                
	                //电话
	                if(null==address.getPhone() || StringUtils.isBlank(address.getPhone())){
	                    updateReq.setPhone("");
	                }else{
	                    updateReq.setPhone(address.getPhone());
	                }
	                
	                //邮编
	                if(null==address.getPostcode() || StringUtils.isBlank(address.getPostcode())){
	                    updateReq.setPostCode("");
	                }else{
	                    updateReq.setPostCode(address.getPostcode());
	                }
	                
	                //地域
	                if(null==address.getProvince() || StringUtils.isBlank(address.getProvince())){
	                    updateReq.setProvince("");
	                }else{
	                    updateReq.setProvince(address.getProvince());
	                }
	                if(null==address.getCity() || StringUtils.isBlank(address.getCity())){
	                    updateReq.setCity("");
	                }else{
	                    updateReq.setCity(address.getCity());
	                }
	                if(null==address.getArea() || StringUtils.isBlank(address.getArea())){
	                    updateReq.setArea("");
	                }else{
	                    updateReq.setArea(address.getArea());
	                }
	                
	                //详细地址
	                if(null==address.getAddress() || StringUtils.isBlank(address.getAddress())){
	                    updateReq.setAddress("");
	                }else{
	                    updateReq.setAddress(address.getAddress());
	                }
	                
	                ModifyRecAddressInfoResult  updateResult= addressInfoFacade.modifyRecAddress(updateReq);//调用查询地址接口
	                if(null != updateResult){//判断返回结果是否成功
	                    
	                    respCode = updateResult.getRespCode();//响应结果
	                    respDesc = updateResult.getRespDesc();
	                    if(!"000".equals(respCode)){//如果查询结果没有成功
	                        /*addressForm.setErrorCode(Integer.parseInt(respCode));     //errCode是否该用webservice返回的code？
	                        addressForm.setErrorString(respDesc);
	                        req.setAttribute("addressForm", addressForm);*/
	                    	logger.info("设置默认失败" + respCode + ":" + respDesc);
	                    	return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
	                    }
	                    logger.info("设置默认成功");
	                    return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
	                }
	            }
	      } catch (ServiceException e) {
	    	  logger.error(e.getMessage(), e);
	          e.printStackTrace();
	          return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
	      } catch (Exception e) {
	    	  logger.error(e.getMessage(), e);
	          e.printStackTrace();
	      }
		return "redirect:"+applicationPropertyConfig.getDomainProfile()+"/address/queryAddress.do";
	}
}