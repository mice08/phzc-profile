package com.phzc.profile.web.controller.m;

import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.console.util.ConsolePropertiesManager;
import com.phzc.profile.biz.service.UserAuthorizeService;
import com.phzc.profile.web.controller.BaseController;
import com.phzc.profile.web.form.AddressForm;
import com.phzc.ubs.common.facade.model.*;
import com.phzc.ubs.common.facade.service.AddressInfoFacade;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/m/address/*")
public class MMyAddressController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MMyAddressController.class);

	@Autowired
	private UserAuthorizeService userAuthorizeService;

	// UBS安全码
	private String ubsConsumerId = ConsolePropertiesManager.getUbsConsumerCode();

	@RequestMapping(value = "queryAddress.do", method = RequestMethod.GET)
	public String queryAddress(Model model, HttpServletRequest request) {
		model.addAttribute("config", applicationPropertyConfig);

		User user = userSessionFacade.getUser(request);
		// User user = new User();
		// user.setCustId("20150629205256");
		// user.setOperId("000001");
		String custId = user.getCustId();

		try {
			AddressInfoFacade addressInfoFacade = (AddressInfoFacade) serviceClientFactory.getWebServiceClient(AddressInfoFacade.class, "addressInfoFacade", "UBS");
			AddressInfoRequest queryReq = new AddressInfoRequest();// 查询地址请求
			queryReq.setConsumerId(ubsConsumerId);
			queryReq.setCustId(custId);
			AddressInfoResult queryResult = addressInfoFacade.queryRecAddress(queryReq);// 调用查询地址接口
			if (null != queryResult) {// 判断返回结果集是否成功
				List<AddressInfoDto> list = queryResult.getAddressInfoList();// 结果集
				if (null != list && !list.isEmpty()) {
					// 先判断有没有默认地址
					int isdefaultCount = 0;
					for (AddressInfoDto addressInfoDto : list) {
						if (StringUtils.equals("Y", addressInfoDto.getIsDefault())) {
							model.addAttribute("address", addressInfoDto);
							isdefaultCount++;
						}
					}
					// 如果没有取更新最晚的那条地址
					if (isdefaultCount == 0) {
						Collections.sort(list, new Comparator<AddressInfoDto>() {
							public int compare(AddressInfoDto arg0, AddressInfoDto arg1) {
								return arg1.getUpdateTime().compareTo(arg0.getUpdateTime());
							}
						});
						model.addAttribute("address", list.get(0));
					}
				}

				String respCode = queryResult.getRespCode();// 响应结果
				String respDesc = queryResult.getRespDesc();// 响应结果描述

				if (!"000".equals(respCode)) {// 如果查询结果没有成功
					logger.info("查询结果没有成功:" + respCode + ":" + respDesc);
					model.addAttribute("fail", respDesc);
				}
				//获取来源地址
				String toUrl = request.getParameter("toUrl");
				if (!StringUtils.isEmpty(toUrl)) {
					model.addAttribute("fromUrl", toUrl);
				}
				
				// model.addAttribute("list", list);
			}
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return "address/m/myAddress_m.ftl";
	}

	@RequestMapping(value = "addAddress.do", method = RequestMethod.POST)
	public String addAddress(Model model, AddressForm addressForm, HttpServletRequest request) {
		model.addAttribute("config", applicationPropertyConfig);

		String msg = "添加失败!";

		if (StringUtils.isBlank(addressForm.getName())) {
			model.addAttribute("fail", "请输入收货人姓名");
		} else if (addressForm.getName().length() > 25) {
			model.addAttribute("fail", "收货人姓名长度超过25个字符");
		}
		if (StringUtils.isBlank(addressForm.getAddress())) {
			model.addAttribute("fail", "收货地址不能为空");
		}
		if (StringUtils.isBlank(addressForm.getPhone()) && StringUtils.isBlank(addressForm.getTelephone())) {
			model.addAttribute("fail", "手机、座机号码至少有一项必填");
		} else if (StringUtils.trimToEmpty(addressForm.getPhone()).length() > 20 || StringUtils.trimToEmpty(addressForm.getTelephone()).length() > 20) {
			model.addAttribute("fail", "手机、座机号码至少有一项长度超过20个字符");
		}

		Integer addressId = addressForm.getAddressId();

		// User user = new User();
		// user.setCustId("20150629205256");
		// user.setOperId("000001");

		/*** AddressId()为null 新增 **/
		if (null == addressForm.getAddressId()) {
			try {
				AddressInfoFacade addressInfoFacade = (AddressInfoFacade) serviceClientFactory.getWebServiceClient(AddressInfoFacade.class, "addressInfoFacade ", "UBS");

				InsertAddressInfoRequest insertReq = new InsertAddressInfoRequest();
				User user = userSessionFacade.getUser(request);
				String custId = user.getCustId();
				insertReq.setConsumerId(ubsConsumerId);
				insertReq.setCustId(custId);
				insertReq.setName(addressForm.getName());// 收件人姓名
				// TODO 从weixin添加的默认设置为default
				insertReq.setIsDefault("Y");// 默认收货地址
				insertReq.setProvince(addressForm.getProvince());
				insertReq.setCity(addressForm.getCity());
				insertReq.setArea(addressForm.getArea());// 所在区域
				insertReq.setAddress(addressForm.getAddress());// 详细地址

				insertReq.setPhone(addressForm.getPhone());// 固定电话
				insertReq.setTelephone(addressForm.getTelephone());// 手机号码
				insertReq.setPostCode(addressForm.getPostCode());// 邮政编码
				InsertAddressInfoResult insertAddress = addressInfoFacade.addRecAddress(insertReq);
				if (null != insertAddress) {
					String respCode = insertAddress.getRespCode();
					String respDesc = insertAddress.getRespDesc();
					if (!"000".equals(respCode)) {
						msg = "添加收货地址失败!";
						logger.info(msg + respCode + ":" + respDesc);
						addressForm.setErrorCode(Integer.parseInt(respCode));
						addressForm.setErrorString(respDesc);
						model.addAttribute("addressForm", addressForm);
						return "redirect:" + applicationPropertyConfig.getDomainProfile() + "/m/address/queryAddress.do";
					} else {
						msg = "添加收货地址成功!";
						logger.info(msg);
						
						if (!StringUtils.isEmpty(addressForm.getFromUrl())) {
							//不为空跳到原来的页面
							return "redirect:" + addressForm.getFromUrl();
						}else{
							//为空跳回用户中心
							return "redirect:" + applicationPropertyConfig.getDomainProfile() + "/m/userInfo.do";
						}

						
					}
				}
			} catch (ServiceException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}

		/** update ***/
		String isDefault = "N";
		if ("Y".equals(addressForm.getIsDefault())) {
			isDefault = "Y";
		}

		try {
			AddressInfoFacade ubsAddressInfoService = (AddressInfoFacade) serviceClientFactory.getWebServiceClient(AddressInfoFacade.class, "addressInfoFacade ", "UBS");
			String custId = userSessionFacade.getUser(request).getCustId();
			// String custId = user.getCustId();
			ModifyRecAddressInfoRequest updateReq = new ModifyRecAddressInfoRequest();// 修改地址请求
			updateReq.setConsumerId(ubsConsumerId);
			updateReq.setCustId(custId);
			updateReq.setAddressId(addressId.toString());
			updateReq.setName(addressForm.getName());
			updateReq.setTelephone(addressForm.getTelephone());
			updateReq.setProvince(addressForm.getProvince());
			updateReq.setCity(addressForm.getCity());
			updateReq.setArea(addressForm.getArea());// 所在区域
			updateReq.setPhone(addressForm.getPhone());
			updateReq.setPostCode(addressForm.getPostCode());
			updateReq.setAddress(addressForm.getAddress());
			// TODO 从weixin添加的默认设置为default
			updateReq.setIsDefault("Y");// 默认收货地址

			ModifyRecAddressInfoResult updateResult = ubsAddressInfoService.modifyRecAddress(updateReq);// 调用查询地址接口
			if (null != updateResult) {// 判断返回结果是否成功

				String respCode = updateResult.getRespCode();// 响应结果
				String respDesc = updateResult.getRespDesc();
				if (!"000".equals(respCode)) {// 如果查询结果没有成功
					logger.info("更新没有成功" + respCode + ":" + respDesc);
					return "redirect:" + applicationPropertyConfig.getDomainProfile() + "/m/address/queryAddress.do";
				} else {
					logger.info("更新成功++");
					
					if (!StringUtils.isEmpty(addressForm.getFromUrl())) {
						//不为空跳到原来的页面
						return "redirect:" + addressForm.getFromUrl();
					}else{
						//为空跳回用户中心
						return "redirect:" + applicationPropertyConfig.getDomainProfile() + "/m/userInfo.do";
					}
				}
			}
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return "redirect:" + applicationPropertyConfig.getDomainProfile() + "/m/address/queryAddress.do";
		}

		return "redirect:" + applicationPropertyConfig.getDomainProfile() + "/m/address/queryAddress.do";
	}

	@RequestMapping(value = "queryAddressById.do", method = RequestMethod.GET)
	public String queryAddressById(Model model, @RequestParam String addressId, HttpServletRequest request) {
		model.addAttribute("config", applicationPropertyConfig);

		User user = userSessionFacade.getUser(request);
		// User user = new User();
		// user.setCustId("20150629205256");
		// user.setOperId("000001");
		String custId = user.getCustId();

		try {
			AddressInfoFacade addressInfoFacade = (AddressInfoFacade) serviceClientFactory.getWebServiceClient(AddressInfoFacade.class, "addressInfoFacade", "UBS");
			AddressInfoRequest queryReq = new AddressInfoRequest();// 查询地址请求
			queryReq.setConsumerId(ubsConsumerId);
			queryReq.setCustId(custId);
			queryReq.setAddressId(addressId);
			AddressInfoResult queryResult = addressInfoFacade.queryRecAddress(queryReq);// 调用查询地址接口
			if (null != queryResult) {// 判断返回结果集是否成功
				List<AddressInfoDto> list = queryResult.getAddressInfoList();// 结果集
				if (null != list && !list.isEmpty()) {
					Collections.sort(list, new Comparator<AddressInfoDto>() {
						public int compare(AddressInfoDto arg0, AddressInfoDto arg1) {
							return arg1.getUpdateTime().compareTo(arg0.getUpdateTime());
						}
					});
					model.addAttribute("address", list.get(0));
					for (AddressInfoDto addressInfoDto : list) {
						if (StringUtils.equals("Y", addressInfoDto.getIsDefault())) {
							model.addAttribute("address", addressInfoDto);
						}
					}
				}

				String respCode = queryResult.getRespCode();// 响应结果
				String respDesc = queryResult.getRespDesc();// 响应结果描述

				if (!"000".equals(respCode)) {// 如果查询结果没有成功
					logger.info("查询结果没有成功:" + respCode + ":" + respDesc);
					model.addAttribute("fail", respDesc);
				}
				// model.addAttribute("list", list);
			}
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return "address/m/myAddress_m.ftl";
	}

}