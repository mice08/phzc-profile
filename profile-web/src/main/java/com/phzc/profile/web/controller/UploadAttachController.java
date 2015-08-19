package com.phzc.profile.web.controller;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.console.base.session.UserSessionFacade;
import com.phzc.profile.biz.model.IdentityAttachInfoRecord;
import com.phzc.profile.biz.service.IdentityAttachService;
import com.phzc.profile.web.entity.FileStatEnum;
import com.phzc.profile.web.utils.DateUtils;
/**
 * 文件上传服务
 * @author ZHAIRONGYE
 *
 */
@Controller
@RequestMapping("/identity/*")
public class UploadAttachController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(UploadAttachController.class);
	//获得session
	@Autowired
	private UserSessionFacade userSessionFacade;
	//文件上传服务
	@Autowired
	private IdentityAttachService identityAttachService;
	private static  Map<String,String> staticMap=new HashMap<String,String>();
	static {
		staticMap.put("01", "存款证明");
		staticMap.put("02", "基金或股票市值证明");
		staticMap.put("03", "理财产品市值证明");
		staticMap.put("04", "第三方存管资产证明");
		staticMap.put("05", "其他金融资产证明");
		
		staticMap.put("10", "家庭关系证明");
		staticMap.put("11", "名片");
		staticMap.put("12", "新三版帐户证明");
		staticMap.put("13", "房产证明");
		staticMap.put("14", "金融机构在职证明");
		staticMap.put("15", "信用卡账单");
		
		staticMap.put("99", "身份证(正)");
		staticMap.put("98", "身份证(反)");
		
		staticMap.put("20", "工资性收入证明");
		staticMap.put("21", "经营性收入证明");
		staticMap.put("22", "租赁性收入证明");
		staticMap.put("23", "其他收入证明");
		
		
	}
	/**
	 * Method execute
	 * 
	 * 先将附件写入到服务器硬盘，图片信息放入到session,待提交认证后一并入库
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward   
	 */
	@RequestMapping(value = "uploadAttach.do", method = RequestMethod.POST)
	public String upload(HttpServletRequest request, Model model,@RequestParam("attachType") String attachType,@RequestParam("attachFile") MultipartFile file){
		
		model.addAttribute("config", applicationPropertyConfig);
		
		String message = "你已成功上传文件";
		try {
			
			User userSession=userSessionFacade.getUser(request);
			PrintWriter out = null;
			if (!file.isEmpty()) {

				if(StringUtils.isEmpty(file.getContentType())){
					message = "附件类型非法！";
					out.write("<script>parent.callBack('"+message+"');</script>");
					return null;
				}
				String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				//扩展名不对
				if(!checkFileExtension(fileExtension)){
					message = "不支持的文件格式!";
					out.write("<script>parent.callBack('"+message+"');</script>");
					return null;
				}
				//数据库中存的名字
				String fileName=System.currentTimeMillis()+".jpg";
				
				String filename = UUID.randomUUID().toString()+UUID.randomUUID().toString()+file.getOriginalFilename(); // 得到上传时的文件名
				//数据库中存的路径
				String filePath="profile/"+DateUtils.getCurrentDate()+"/";
				FileUtils.writeByteArrayToFile(new File(applicationPropertyConfig.getUploadPath()+filePath, filename),file.getBytes());
				
				IdentityAttachInfoRecord record = new IdentityAttachInfoRecord();
				record.setFileName( fileName);
				record.setFilePath("/"+filePath+filename);
				record.setFileStat(FileStatEnum.N.getCode());
				record.setFileType(attachType);
				Timestamp time = new Timestamp(System.currentTimeMillis());
				record.setUpdDatetime(time);
				record.setUserId(userSession.getCustId());
				identityAttachService.insertIndentityAttachInfo(record);
				
				logger.info("upload over. " + filename);
				return "identity/uploadResult.ftl";
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "对不起,文件上传失败了!";
			model.addAttribute("msg", message);
			return "identity/uploadResult.ftl";
		}
		return null;
	}	
	
	@RequestMapping(value = "updateAttach.do", method = RequestMethod.GET)
	public String updateAttachState(HttpServletRequest request, Model model,int attachId){
		
		model.addAttribute("config", applicationPropertyConfig);
		
		IdentityAttachInfoRecord identityAttachInfoRecord=new IdentityAttachInfoRecord();
		identityAttachInfoRecord.setAttachId(attachId);
		identityAttachInfoRecord.setFileStat(FileStatEnum.C.getCode());
		// User
		User userSession=userSessionFacade.getUser(request);
		try {
			
			identityAttachService.updateIndentityAttachInfo(identityAttachInfoRecord);
			
			//获得图片信息
			
			identityAttachInfoRecord.setUserId(userSession.getCustId());
			identityAttachInfoRecord.setFileStat(FileStatEnum.N.getCode());
			List<IdentityAttachInfoRecord> identityRecord=identityAttachService.getIdentityFilepath(identityAttachInfoRecord);
			if(null!=identityRecord){
				for(int index=0;index<identityRecord.size();index++){
					identityRecord.get(index).setFileDesc(staticMap.get(identityRecord.get(index).getFileType()));
				}
			}
			model.addAttribute("config", applicationPropertyConfig);
			model.addAttribute("identityRecord", identityRecord);
			return "identity/recordList.ftl";
		} catch (ServiceException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	@RequestMapping(value = "queryAttach.do", method = RequestMethod.GET)
	public String queryAttach(HttpServletRequest request, Model model){
		
		model.addAttribute("config", applicationPropertyConfig);
		
		User userSession=userSessionFacade.getUser(request);
		//获得图片信息
		IdentityAttachInfoRecord  identityAttachInfoRecord=new IdentityAttachInfoRecord();
		identityAttachInfoRecord.setUserId(userSession.getCustId());
		identityAttachInfoRecord.setFileStat(FileStatEnum.N.getCode());
		List<IdentityAttachInfoRecord> identityRecord=identityAttachService.getIdentityFilepath(identityAttachInfoRecord);
		if(null!=identityRecord){
			for(int index=0;index<identityRecord.size();index++){
				identityRecord.get(index).setFileDesc(staticMap.get(identityRecord.get(index).getFileType()));
			}
		}
		model.addAttribute("config", applicationPropertyConfig);
		model.addAttribute("identityRecord", identityRecord);
		return "identity/recordList.ftl";
	}
	
	
	/**
	 * 格式的校验
	 * @param fileExtension
	 * @return
	 */
	private boolean checkFileExtension(String fileExtension){
		
		if(StringUtils.equals(fileExtension, ".PNG") ){
			return true;
		}
		
		if(StringUtils.equals(fileExtension, ".JPG") ){
			return true;
		}
		
		if(StringUtils.equals(fileExtension, ".BMP") ){
			return true;
		}
		
		if(StringUtils.equals(fileExtension, ".JPEG") ){
			return true;
		}
		
		if(StringUtils.equals(fileExtension, ".png") ){
			return true;
		}
		
		if(StringUtils.equals(fileExtension, ".jpg") ){
			return true;
		}
		
		if(StringUtils.equals(fileExtension, ".bmp") ){
			return true;
		}
		
		if(StringUtils.equals(fileExtension, ".jpeg") ){
			return true;
		}
		
		return false;
	}
}
