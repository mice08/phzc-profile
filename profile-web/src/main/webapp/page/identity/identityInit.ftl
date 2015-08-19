 <#import "../share/pc/layout.ftl" as page> 
<#assign actives="active3" in page>
 <@page.pc> 
 <#setting number_format="0">
<div class="user_page">
	<div class="main clearfix">
		<#include "../leftNav.ftl"> <@leftNav activeMenu="item2"> </@leftNav>

		<div class="right">
			<div class="user_page_inner  user_page2-1">
				<ul class="user_top cha_pass">
					<li class="first"><a
						href="${config.getDomainProfile()}/openAccount/openAccount.do">
							实名认证</a></li>
					<li><a href="#" class="active">合格投资人认证 </a></li>
				</ul>
				<div class="user_form">
					
					<form id="identityForm" name="identityForm" action="identity.do" enctype="multipart/form-data" method="post">
						<input id="idType" name="idType" type="hidden" value="2" /> 
						<input type="hidden" id="idSid" name="idSid" value="${idSid!''}" />
						<div class="ajax_part">
							<div class="input_row">
								<label> <i>*</i>姓名
								</label>
								<div class="input_part">
									<#if userIdentity.usrName?length gt 1 > 
										<span>${userIdentity.usrName}</span>
									<#else> 
										<input type="text" id="usrName" name="usrName" value=""> 
									</#if>
								</div>
							</div>
							<div class="input_row">
								<label> <i>*</i>证件类型
								</label>
								<div class="select_part zindex1">
									<a class="select_part_main" id="credType" name="credType" href="#" data-value="99"> 身份证</a>
								</div>
							</div>
							<div class="input_row">
								<label> <i>*</i>证件号码
								</label>
								<div class="input_part">
									<#if userIdentity.certId?length gt 1> 
										<span>${userIdentity.certId}</span> 
									<#else> 
										<input type="text" id="certId" name="certId" value="">
									</#if>
								</div>
							</div>
							<div class="input_row">
								<input type="hidden" id="telephone" name="telephone" value="${userIdentity.usrMp?default("")}" /> 
								<#if (userIdentity.usrMp)?exists > 
									<label> 手机号码 </label> <span>${userIdentity.usrMp?default("")}</span> 
								<#else> 
									<label> 手机号码 </label><span> </span> 
								</#if>
							</div>
							<div class="input_row">
								<label> 所在企业 </label>
								<div class="input_part">
									<input type="text" id="orgName" name="orgName">
								</div>
							</div>
							<div class="input_row input_row_sp">
								<label> <i>*</i>特定合格投资人</label><span>（以下条件至少勾选一项)</span>
							</div>
							<div class="input_row input_row_sp">
								<label> </label> 
								<input type="checkbox" id="assetSal" name="assetSal" value="1" checked="checked"> 
								<span>个人金融资产不低于300万人民币 </span>
							</div>
							<div class="input_row input_row_sp">
								<label> </label> <input type="checkbox"  id="assetTot" name="assetTot"value="2"> 
									<span> 最近三年个人年均收入不低于50万人民币 </span>
							</div>
							
							<div class="input_row input_row_sp">
								<label><i>*</i>服务条款</label> <input type="checkbox" name="isAgree" id="isAgree" value="3"> 
									<span> 我已阅读<a href="${config.getDomainWww()}/static/protocol4.html" target="_blank">《平安众加平台非公开股权融资投资规则》</a>
								</span>
							</div>
							<div class="input_row">
								<label> </label> <input type="checkbox" name="agree" id="agree"	value="4"> <span>
									本人承诺以上信息真实有效，资金来源合法，并自愿承担因此而产生的全部后果 </span>
							</div>
							<div class="input_row">
								<label> </label> <a id="showSubmit" class="submit" href="javascript:void(0);" onclick="doSubmit();"> 提交认证 </a>
							</div>
							<div class="input_row">
								<label> </label> 
								<span>
									以上信息仅供本平台进行后续合格投资者认证使用。
								</span>
							</div>
					</form>
				</div>
				<script type="text/javascript">
					
	//个人认证的表单提交
	function doSubmit() {
		
		var existCheck=true;
		if($("#usrName").length>0){
			existCheck=checkUserName();
		}
		if($("#certId").length>0){
			existCheck=checkCertId();
		}
		checkAgree();
		checkIsAgree();
		investorChecked();
		if(investorChecked()&&checkAgree()&&checkIsAgree()&&existCheck){
			$("#showSubmit").addClass("disable");
			$("#identityForm").submit();
		}
	}
	
	//合格投资人校验
	function investorChecked() {
		remove_infornmation_alert("assetSal");
		remove_infornmation_alert("assetTot");
		if ($("input[name='assetSal']").attr('checked')=="checked") {
			infornmation_alert("correct","assetSal","");
			return true;
		} 
		if($("input[name='assetTot']").attr('checked')=="checked"){
			infornmation_alert("correct","assetTot","");
			return true;
		}
		if(!$("input[name='assetSal']").is(':checked')&&!$("input[name='assetTot']").is(':checked')){
			infornmation_alert("error","assetSal","请选择一种合格投资人类型");
			return false;
		}
		
	}
	//同意服务条款下方的协议
	function checkAgree() {
		remove_infornmation_alert("agree");
		if ($("input[name='agree']").attr('checked')=="checked") {
			infornmation_alert("correct","agree","");
			return true;
		} else {
			infornmation_alert("error","agree","请阅读并同意条款");
			return false;
		}
	}
	//同意服务条款
	function checkIsAgree() {
		remove_infornmation_alert("isAgree");
		if ($("input[name='isAgree']").attr('checked')=="checked") {
			infornmation_alert("correct","isAgree","");
			return true;

		} else {
			infornmation_alert("error","isAgree","请阅读并同意条款");
			return false;
		}
	}
	//校验格式
	function checkunv(value){
			var re = /^[a-zA-Z\u4e00-\u9fa5]+$/ ; 
			var val = value ; 
			if (re.test(val)) { 
				return true ;
			}else{ 
				 return false ;  
			}
		}
	//个人真实姓名校验
	function checkUserName() {
		//姓名校验
		if($('#usrName').length>0){
			var usrName = $('#usrName').val();
			remove_infornmation_alert("usrName");
			if (usrName == null || $.trim(usrName) == '') {
				infornmation_alert("error","usrName","请您输入真实姓名");
				return false;
			}
			if (checkunv(usrName)) {
				infornmation_alert("correct","error","usrName","");
				return true;
			} else {
				infornmation_alert("error","usrName","真实姓名必须是汉字");
				return false;
			}
		}else
			return false;
		
	}
	
	/*检查身份证号*/
function checkCertId(){ 
	remove_infornmation_alert("certId");
	var certId=$("#certId").val();
	var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};  

	if(area[parseInt(certId.substr(0,2))]==null){ 
		infornmation_alert("error","certId","身份证号地区非法!");
		return false;
	}

	var regex1 = /^[1-9][0-7]\d{4}((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229))\d{3}(\d|X|x)?$/;
	
	switch(certId.length){ 
		case 15: 
			if ( (parseInt(certId.substr(6,2))+1900) % 4 == 0 || ((parseInt(idcard.substr(6,2))+1900) % 100 == 0 && (parseInt(idcard.substr(6,2))+1900) % 4 == 0 )){ 
				ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;//测试出生日期的合法性 
			} else { 
				ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;//测试出生日期的合法性 
			} 
			if(ereg.test(certId)){
				return true;
			}else{
				infornmation_alert("error","certId","身份证号格式错误");
				return false;
			}
			break;
		case 18: 
			if(regex1.test(certId)){
				infornmation_alert("correct","certId","OK");
				return true;
			}else{
				infornmation_alert("error","certId","身份证号格式错误");
				return false;
			}
			break;
		default:
			infornmation_alert("error","certId","身份证号格式错误");
			return false;
			break;
	}
}
	
</script>
			</div>
		</div>
	</div>
</div>
<div class="bgcover"></div>
<div class="ext ext_state3">
	<h1>提示</h1>
	<div class="ext_hint ext_hint_failed">
		<div class="img"></div>
		<div class="content">
			<h2>上传文件缺失</h2>
			<p>上传文件缺失</p>
		</div>
	</div>
	<a href="#" class="close"> 关闭 </a>
</div>
</@page.pc>
