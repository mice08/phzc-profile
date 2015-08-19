<#import "../../share/m/layout.ftl" as page> 
<#assign params={"hStyle":"header_p","parentName":"验证手机号"} in page>
<@page.pc>
	<section class="register_detail no_banner">
		<form id="form_password_forget" action="verifySmsCode.do">
			<ul class="clearfix">
				<li><label> 手机号 </label> <input type="text" class="username" name="mobile" value="${phoneNo!}" readonly></li>
				<li><label> 验证码 </label> <input type="text" class="yzm" placeholder="填写短信验证码" name="smsVerifyCode"> <a
					href="javascript:void(0);" class="yzm_link">点击发送 </a></li>
				<li class="normal"><a class="submit"> 下一步 </a></li>
			</ul>
		</form>
		<script>
			$(function() {
				/*form_password_forget*/
				var form_password_forget = $("#form_password_forget");
				form_password_forget.find("a.submit").click(function(e) {
					var mainobj = $(this).parents("form");
					e.preventDefault();
					e.stopPropagation();
					var action = mainobj.attr("action");
					var username_val = mainobj.find("[name='mobile']").val();
					var Idcode = mainobj.find(".yzm").val();
					/*  if(!checkUserName_phone(username_val)){//用户名
					     warning_obj_inner.text(warning);
					     openExt(warning_obj,true);
					     return false;
					 }else */if (!checkIdcode(Idcode)) {//验证码
						warning_obj_inner.text(warning);
						openExt(warning_obj, true);
						mainobj.find(".yzm").val(""); //清空
						return false;
					} else {
						var data = {
							"mobile" : username_val,
							"smsVerifyCode" : Idcode
						};

						$.ajax({
							type : 'POST',
							url : action,
							data : data,
							success : function(data) {
								console.log(data);
								if (data.success === true) {
									window.location.href = "resetPwdForm.do";
								} else {
									warning_obj_inner.text(data.msg);
									openExt(warning_obj, true);
									resetTime();
									return false;
								}
							},
							error : function(error) {
								console.log("error data: ", error);
							},
							dataType : "json"
						});
					}
				})
			})
		</script>
	</section>
</div>
<div class="ext ext_warning">
	<div class="wrapper">用户名、手机号或密码有误</div>
</div>
<div class="logo"></div>
<script>
	/*验证码*/
	yzm_link = $(".yzm_link");
	var setInterval_obj = null;
	var setInterval_obj2 = null;
	function reverseTime(num) {
		num -= 1;
		yzm_link.text(num);
		setInterval_obj = setInterval(function() {
			num -= 1;
			yzm_link.text(num);
		}, 1000);
		setInterval_obj2 = setInterval(function() {
			clearInterval(setInterval_obj);
			yzm_link.text("重新发送");
			yzm_link.removeClass("disable")
		}, 1000 * num)
	};
	function resetTime() {
		clearInterval(setInterval_obj2);
		clearInterval(setInterval_obj);
		yzm_link.text("重新发送");
		yzm_link.removeClass("disable")
	};
	yzm_link.click(function(e) {
		var form_password_forget = $("#form_password_forget");
		var mobile_val = form_password_forget.find("[name='mobile']").val();
		/* if (!checkUserName_phone(mobile_val)) {
			warning_obj_inner.text(warning);
			openExt(warning_obj, true);
			return false;
		} */
		if ($(this).hasClass("disable"))
			return;
		e.preventDefault();
		yzm_link.addClass("disable");
		var num = 60;
		reverseTime(num);

		var data = {
			"phoneNo" : mobile_val
		};

		$.ajax({
			type : 'POST',
			url : "sendForReset.do",
			data : data,
			success : function(data) {
				if (data.success === false) {
					console.log(data);
				}
			},
			error : function(error) {
				console.log("error data: ", error);
			},
			dataType : "json"
		});

	});
</script>

</@page.pc>
