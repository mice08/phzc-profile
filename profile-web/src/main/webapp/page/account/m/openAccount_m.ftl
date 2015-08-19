<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p","backUrl":"${config.getDomainProfile()}/m/identity/identityCenter.do","parentName":"实名认证"} in page>
<@page.pc>
		<section class="temp_page user_real no_banner">
			<form class="form-horizontal" id="bankCardForm" name="bankCardForm" action="/m/openAccount/openAccount.do" method="post">
				<input type="hidden" id="source" name="source" value="${source!''}">
				<ul class="clearfix">
					<li><label> 真实姓名 </label> <input  type="text" id="realName" name="realName" placeholder="请输入真实姓名"></li>
					<li><label> 身份证号 </label> <input type="number" id="certId" name="certId" placeholder="请输入身份证号"></li>
				</ul>
				
				<a href="javascript:submitForm();" class="submit "> 提交 </a>
			</form>
		</section>
	</div>
	<div class="ext ext_warning">
		<div class="wrapper">姓名或者身份证号有误</div>
	</div>
	<div class="logo"></div>
	<script>
        function submitForm(){
        	var flag=true;
			if(!checkCertId()){
				warning_obj_inner.text(warning);
				openExt(warning_obj, true);
				flag=false;
			}
			if(!checkRealName()){
				warning_obj_inner.text(warning);
				openExt(warning_obj, true);
				flag=false;
			}
			if(flag){
				document.bankCardForm.submit();
			}
 		}
        
        function checkCertId(){
        	var val=$("#certId").val();
            if($.trim(val)=="") {
                warning="身份证不能为空白";
                return false;
            }
            
            else if(!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test($.trim(val))){
                warning="请输入正确的身份证号码";
                return false;
            }
            else{
                return true;
            }
        }
        function checkRealName(){
        	var val=$("#realName").val();
        	if($.trim(val)=="") {
                warning="姓名不能为空";
                return false;
            }
        	else if(!/^([\u4e00-\u9fa5]+)$/.test($.trim(val))){
                 warning="请输入真实的姓名";
                 return false;
             }
             else{
                 return true;
             }
        }
    </script>
</@page.pc>