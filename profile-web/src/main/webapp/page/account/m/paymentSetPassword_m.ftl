<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p","backUrl":"${config.getDomainProfile()}/m/identity/identityCenter.do","parentName":"交易密码"} in page>
<@page.pc>
		<section class="payment_set_password no_banner">
			<form class="form_order" id="bankCardForm" name="bankCardForm" action="/m/openAccount/setTransPassWord.do" method="post">
				<ul class="clearfix">
					<li><label> 交易密码 </label> <input id="transPwd" name="transPwd" type="password" class="username" placeholder="输入交易密码"></li>
					<li><label> 再次确认 </label> <input id="confirm_transPwd" name="confirmTransPwd" type="password" class="yzm" placeholder="再次确认密码"> 
					<li class="normal"><a class="submit" href="javascript:submitForm();"> 提交 </a></li>
				</ul>
			</form>
		</section>
	</div>
	<div class="ext ext_warning">
		<div class="wrapper">确认密码输入不一致</div>
	</div>
	<div class="logo"></div>
	<script>
		function submitForm() {
			var flag = true;
			var transPwd = $("#transPwd").val();
			var confirm_transPwd = $("#confirm_transPwd").val();
    	   
			if(!checkPassWord_register(transPwd)) {
			   	warning_obj_inner.text(warning);
				openExt(warning_obj, true);
				flag = false;
			}

			if(!checkRePassWord(transPwd, confirm_transPwd)) {
				warning_obj_inner.text(warning);
				openExt(warning_obj, true);
				flag = false;
			}

			if(flag) {
				var formData = {
			        transPwd: transPwd,
			        confirmTransPwd: confirm_transPwd
			    }

				var action = $("#bankCardForm").attr("action");
			    $.ajax({
			        type: 'POST',
			        url: action,
			        data: formData,
			        dataType: 'json',
			        success: function(data) {
			            console.log("ret: ", data);
			            if (data.success === true) {
			                window.location.href = "/m/openAccount/setTransPassWordSuccess.do?source=" + encodeURI("${source!''}");
			            } else {
			                warning_obj_inner.text("设置交易密码失败: " + data.msg);
			                openExt(warning_obj, true);
			                return false;
			            }
			        } ,
			        error: function(error) {
			        }
			    });
			}
 		}
    </script>
</@page.pc>