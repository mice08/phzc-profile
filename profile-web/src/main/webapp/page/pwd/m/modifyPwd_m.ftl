<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p","parentName":"修改密码"} in page>
 <@page.pc>
	<section class="user_set_password no_banner">
		<input id="modifyType" value="${modifyType}" type="hidden">
		<form id="form_modifyPwd" action="changePwd.do">
			<ul class="clearfix">
				<li><label> 旧密码 </label> <input id="orgPwd" name="orgPwd" type="password" placeholder="输入旧密码" maxlength="20" autocomplete="off"></li>
				<li class="sp3"><label> 新密码 </label> <input id="newPwd" name="newPwd" type="password" placeholder="输入新密码" maxlength="20"
					autocomplete="off"></li>
				<li><label> 再次确认 </label> <input id="newPwd2" name="newPwd2" type="password" placeholder="再次输入新密码" maxlength="20"
					autocomplete="off"></li>
				<li class="normal"><a class="submit"> 提交 </a></li>
			</ul>
			<div class="linkpart">
			<#if modifyType == "login">
			<a href="${config.getDomainPassport()}/m/pwd/resetVerifyForm.do" class="right"> 忘记密码？ </a>
			<#else>
			<a href="${config.getDomainProfile()}/m/modifyPwd/resetVerify.do" class="right"> 忘记密码？ </a>
			</#if>
			</div>
		</form>
	</section>
</div>
<div class="ext ext_warning">
	<div class="wrapper">原密码，新密码或确认密码有误</div>
</div>
<div class="logo"></div>
<script type="text/javascript">
	/*form_modifyPwd 异步请求修改登录交易密码 */
	$(function() {
		var form_login = $("#form_modifyPwd");
		form_login
				.find("a.submit")
				.click(
						function(e) {
							var mainobj = $(this).parents("form");
							e.preventDefault();
							e.stopPropagation();
							var action = mainobj.attr("action");

							var modifyType = $("#modifyType").val();
							var orgPwd = mainobj.find("[name='orgPwd']").val();
							var newPwd = mainobj.find("[name='newPwd']").val();
							var newPwd2 = mainobj.find("[name='newPwd2']")
									.val();

							if (!checkPassWord_register(orgPwd)) {
								warning_obj_inner.text(warning);
								openExt(warning_obj, true);
								mainobj.find("[name='orgPwd']").val(""); //清空
								mainobj.find("[name='newPwd']").val(""); //清空
								mainobj.find("[name='newPwd2']").val(""); //清空
								return false;
							}
							if (!checkPassWord_register(newPwd)) {
								warning_obj_inner.text(warning);
								openExt(warning_obj, true);
								mainobj.find("[name='orgPwd']").val(""); //清空
								mainobj.find("[name='newPwd']").val(""); //清空
								mainobj.find("[name='newPwd2']").val(""); //清空
								return false;
							}
							if (!checkPassWord_register(newPwd2)) {
								warning_obj_inner.text(warning);
								openExt(warning_obj, true);
								mainobj.find("[name='orgPwd']").val(""); //清空
								mainobj.find("[name='newPwd']").val(""); //清空
								mainobj.find("[name='newPwd2']").val(""); //清空
								return false;
							}
							if (orgPwd == newPwd) {
								warning_obj_inner.text("原密码和新密码不能相同");
								openExt(warning_obj, true);
								mainobj.find("[name='orgPwd']").val(""); //清空
								mainobj.find("[name='newPwd']").val(""); //清空
								mainobj.find("[name='newPwd2']").val(""); //清空
								return false;
							}
							if (!checkRePassWord(newPwd, newPwd2)) {
								warning_obj_inner.text(warning);
								openExt(warning_obj, true);
								mainobj.find("[name='orgPwd']").val(""); //清空
								mainobj.find("[name='newPwd']").val(""); //清空
								mainobj.find("[name='newPwd2']").val(""); //清空
								return false;
							}
							var data = {
								"orgPwd" : orgPwd,
								"newPwd" : newPwd,
								"modifyType" : modifyType
							};
							$
									.ajax({
										type : 'POST',
										url : action,
										data : data,
										success : function(data) {
											if (data.success === true) {
												//带修改类型跳转成功页面
												window.location.href = "modifySuccess.do?modifyType="
														+ data.modifyType;
											} else {
												warning_obj_inner.text("修改失败:"
														+ data.modForm.errorString);
												openExt(warning_obj, true);
												mainobj.find("[name='orgPwd']").val(""); //清空
												mainobj.find("[name='newPwd']").val(""); //清空
												mainobj.find("[name='newPwd2']").val(""); //清空
												return false;
											}
										},
										error : function(error) {
										},
										dataType : "json"
									});

						});

	});
</script>
</@page.pc>
