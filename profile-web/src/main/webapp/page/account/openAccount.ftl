 <#import "../share/pc/layout.ftl" as page> 
<#assign actives="active3" in page>
<@page.pc>
        <div class="user_page">
			<div class="main clearfix">
				<#include "../leftNav.ftl">
		        <@leftNav activeMenu="item2">
		        </@leftNav>
				<div class="right">
					<div class="user_page_inner  user_page2-4">
		                <ul class="user_top cha_pass">
							<li class="first">
								<a href="#" class="active">	实名认证</a>
							</li>
							<li>
								<a href="${config.getDomainProfile()}/identity/queryIdentity.do" >合格投资人认证	</a>
							</li>
						</ul>
							<form class="form-horizontal" id="bankCardForm" name="bankCardForm" action="/openAccount/openAccount.do" method="post">
								<input type="hidden" id="certStatus" name="certStatus" value="${certRslt}">
								<input type="hidden" id="cerName" name="cerName">
								<input type="hidden" id="cerId" name="cerId">
								<input type="hidden" name="bankNm">
									
										<#if certRslt == 0>
											<div class="input_row">
												<label>	<i>*</i>姓名	</label>
												<div class="input_part">
													<input type="text" id="realName" name="realName" maxlength="20" onblur="checkCardNm();" value="<#if cardForm?exists>${cardForm.realName!''}</#if>">
												</div>
												<em class="warning">请输入证件上的姓名</em>
											</div>
											
											<div class="input_row">
												<label>	<i>*</i>证件类型	</label>
												<div class="select_part  zindex2">
													<a class="select_part_main" href="#" data-value="0">大陆身份证</a>
												</div>
											</div>
											
											<div class="input_row">
												<label>	<i>*</i>证件号码</label>
												<div class="input_part">
													<input type="text"  id="certId" name="certId" maxlength="18" onblur="checkCertId(); " value="<#if cardForm?exists>${cardForm.certId!''}</#if>">
												</div>
											</div>
											
											<div class="input_row">
									            <label>	<i>*</i>设置交易密码</label>
									            <div class="input_part">
									                <input type="password" autocomplete="off" id="j_password" name="transPwd" maxlength="20" onblur="checkPwd();"  placeholder="交易密码不能和登录密码相同">
									            </div>
									            <div class="password_check">
									                <p>	  密码长度为6~20长度，必须包含数字和字母	</p>
									                <ul>
									                    <li class="password_item1 active"></li>
									                    <li class="password_item2"></li>
									                    <li class="password_item3"></li>
									                </ul>
									            </div>
									        </div>
									        <div class="input_row">
									            <label>	<i>*</i>确认交易密码</label>
									            <div class="input_part">
									                <input type="password" autocomplete="off" id="confirm_transPwd" name="confirmTransPwd" maxlength="20" onblur="checkConfirmPwd();" placeholder="交易密码不能和登录密码相同">
									            </div>
									        </div>
											
											
											<div class="input_row">
												<label>
						
												</label>
												<a class="submit" href="javascript:submitForm();" id="bindCardNow">	实名认证</a>
											</div>
										<#elseif certRslt==1>
											<div class="input_row">
												<label>	<i>*</i>姓名	</label>
												<span>${certNm } </span>
											</div>
											
											<div class="input_row">
												<label>	<i>*</i>证件类型	</label>
												<div class="select_part  zindex2">
													<a class="select_part_main" href="#" data-value="0">大陆身份证</a>
												</div>
											</div>
											
											<div class="input_row">
												<label>	<i>*</i>证件号码</label>
												<span>${certId } </span>
											</div>
											
											<div class="input_row">
									            <label>	<i>*</i>设置交易密码</label>
									            <div class="input_part">
									                <input type="password" autocomplete="off" id="j_password" name="transPwd" maxlength="20" onblur="checkPwd();"  placeholder="交易密码不能和登录密码相同">
									            </div>
									            <div class="password_check">
									                <p>	  密码长度为6~20长度，必须包含数字和字母	</p>
									                <ul>
									                    <li class="password_item1 active"></li>
									                    <li class="password_item2"></li>
									                    <li class="password_item3"></li>
									                </ul>
									            </div>
									        </div>
									        <div class="input_row">
									            <label>	<i>*</i>确认交易密码</label>
									            <div class="input_part">
									                <input type="password" autocomplete="off" id="confirm_transPwd" name="confirmTransPwd" maxlength="20" onblur="checkConfirmPwd();" placeholder="交易密码不能和登录密码相同">
									            </div>
									        </div>
											
											
											<div class="input_row">
												<label>
						
												</label>
												<a class="submit" href="javascript:submitForm();" id="bindCardNow">	实名认证</a>
											</div>
										<#elseif certRslt==4>
											<div style='padding-left:410px; padding-top:100px; padding-bottom:200px;font-size:24px; color:#f05a23'>${cardForm.errorString!''}</div>
											<div style='padding-left:410px; padding-top:100px; padding-bottom:200px;font-size:24px; color:#f05a23'><a href="${config.getDomainProfile()}/openAccount/openAccount.do" >重新申请实名认证	</a></div>
										<#elseif certRslt==5>
											<div style='padding-left:410px; padding-top:100px; padding-bottom:200px;font-size:24px; color:#f05a23'>${respDesc!''}</div>
											<div style='padding-left:410px; padding-top:100px; padding-bottom:200px;font-size:24px; color:#f05a23'><a href="${config.getDomainProfile()}/openAccount/openAccount.do" >重新申请实名认证	</a></div>
										<#elseif certRslt==6>
											<div style='padding-left:410px; padding-top:100px; padding-bottom:200px;font-size:24px; color:#f05a23'>${respDesc!''}</div>
											<div style='padding-left:410px; padding-top:100px; padding-bottom:200px;font-size:24px; color:#f05a23'><a href="${config.getDomainProfile()}/openAccount/openAccount.do" >重新申请实名认证	</a></div>
										<#elseif certRslt==7>
											<div style='padding-left:410px; padding-top:100px; padding-bottom:200px;font-size:24px; color:#f05a23'>${respDesc!''}</div>
											<div style='padding-left:410px; padding-top:100px; padding-bottom:200px;font-size:24px; color:#f05a23'><a href="${config.getDomainProfile()}/openAccount/openAccount.do" >重新申请实名认证	</a></div>
										</#if>
								</dl>
							</form>
						<script>
					        $(function(){
					            var password=$("[name=transPwd]");
					            var password_check=$(".password_check");
					            var resultBar=$(".password_check ul li");
					            var resultBar1=resultBar.eq(0);
					            var resultBar2=resultBar.eq(1);
					            var resultBar3=resultBar.eq(2);
					            function check_keydown(){
					                var result=password_detect(password.val());
					                if(result==3){
					                    password_check.show();
					                    resultBar.addClass("active");
					                }
					                if(result==2){
					                    password_check.show();
					                    resultBar.removeClass("active");
					                    resultBar1.addClass("active");
					                    resultBar2.addClass("active");
					                }
					                if(result==1){
					                    password_check.show();
					                    resultBar.removeClass("active");
					                    resultBar1.addClass("active");
					                }
					                if(result==0){
					                    password_check.hide();
					                    resultBar.removeClass("active");
					                }
					            }
					            password.keyup(function(){
					                check_keydown()
					            });
					            password.blur(
					                    function(){
					                        check_keydown()
					                    }
					            )
					        })
					    </script>
					</div>
				</div>
			</div>
		</div>
<script src="${config.getDomainPcstatic()}/skin/js/jquery.jscrollpane.min.js"></script>
<script type="text/javascript">

function init(){
	$("#cardId").focus();
}

/*检查姓名*/
function checkCardNm(){
	$(".warning").hide();
	remove_infornmation_alert("realName");
	var certNm = $("#realName").val();
	var flag ;
	if(certNm == null || certNm == ""){
		infornmation_alert("error","realName","请正确填写!");
		flag = false;
	}else{
		infornmation_alert("correct","realName","");
		flag = true;
	}
	
	return flag;
}
/*检查身份证号*/
function checkCertId(){ 
	remove_infornmation_alert("certId");
	var idcard=$("#certId").val();
	var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};  

	if(area[parseInt(idcard.substr(0,2))]==null){ 
		infornmation_alert("error","certId","身份证号地区非法!");
		return false;
	}

	var regex1 = /^[1-9][0-7]\d{4}((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229))\d{3}(\d|X|x)?$/;
	
	switch(idcard.length){ 
		case 15: 
			if ( (parseInt(idcard.substr(6,2))+1900) % 4 == 0 || ((parseInt(idcard.substr(6,2))+1900) % 100 == 0 && (parseInt(idcard.substr(6,2))+1900) % 4 == 0 )){ 
				ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;//测试出生日期的合法性 
			} else { 
				ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;//测试出生日期的合法性 
			} 
			if(ereg.test(idcard)){
				return true;
			}else{
				infornmation_alert("error","certId","身份证号格式错误");
				return false;
			}
			break;
		case 18: 
			if(regex1.test(idcard)){
				infornmation_alert("correct","certId","");
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

	function checkPwd(){
		remove_infornmation_alert("transPwd");
		var pwd = $("#j_password").val();
		var flag = checkPassWord_register(pwd);
		if(flag){
			infornmation_alert("correct","transPwd","");
		}else{
			infornmation_alert("error","transPwd","请正确填写!");
		}
		return flag;
	}
	
	function checkConfirmPwd(){
		remove_infornmation_alert("confirmTransPwd");
		var pwd = $("#j_password").val();
		var confirmPwd = $("#confirm_transPwd").val();
		
		var flag = false ;
		var flag1 = checkPassWord_register(confirmPwd);
		
		if(flag1){
			flag = checkRePassWord(pwd,confirmPwd);
			if(flag){
				infornmation_alert("correct","confirmTransPwd","");
			}else{
				infornmation_alert("error","confirmTransPwd","密码要一致");
			}
		}else{
			infornmation_alert("error","confirmTransPwd","请确认密码!");
		}
		return flag;
	}
	


</script>

<script>

/*验证码结束*/
function checkAll(){
	
	var cSt =${certRslt};
	var cTp, cCtp;
    //var ar=agree();
    switch(cSt){
	    case 0:
	    	var cNm=checkCardNm();
	    	var cId=checkCertId();
	    	cTp=checkPwd();
	        cCtp=checkConfirmPwd(); 
	    	if( cNm && cId && cTp && cCtp ){
	        	return true;
	        }else{
	        	return false;
	        }
	    	break;
	    case 1:
	    	cTp=checkPwd();
	        cCtp=checkConfirmPwd();
	    	if(cTp && cCtp)
	    	{	
	        	return true;
	        }else{
	        	return false;
	        }
	    	break;
	    default: 
			return false;
			break;
    }
    
    return true;
}

function submitForm(){
		var cerNm='${certNm}';
		var cerId='${certId}';
		$('#cerName').val(cerNm);
		$('#cerId').val(cerId);
		var all = checkAll();
		if(all){
			document.bankCardForm.submit();
		}
 }

</script>
</@page.pc>