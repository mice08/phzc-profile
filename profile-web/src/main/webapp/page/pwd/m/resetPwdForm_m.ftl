<#import "../../share/m/layout.ftl" as page> 
<#assign params={"hStyle":"header_p","parentName":"重置交易密码"} in page>
<@page.pc>
	<section class="payment_set_password no_banner">
		<form class="form_order" id="form_modifyPwd" action="resetTransPwd.do">
			<ul class="clearfix">
				<li><label> 交易密码 </label> <input type="password" class="username" id="newPwd" name="newPwd" placeholder="输入交易密码" maxlength="20" autocomplete="off"></li>
				<li><label> 再次确认 </label> <input type="password" class="username" id="newPwd2" name="newPwd2" placeholder="再次输入交易密码" maxlength="20" autocomplete="off"></li>
				<li class="normal"><a class="submit"> 提交 </a></li>
			</ul>
		</form>
	</section>
</div>
<div class="ext ext_warning">
	<div class="wrapper">用户名、手机号或密码有误</div>
</div>
<div class="logo"></div>
<script>
	/*form_modifyPwd 异步请求重置交易密码 */
	$(function() {
		var form_login = $("#form_modifyPwd");
		form_login.find("a.submit").click(
				function(e) {
					var mainobj = $(this).parents("form");
					e.preventDefault();
					e.stopPropagation();
					var action = mainobj.attr("action");

					var newPwd = mainobj.find("[name='newPwd']").val();
					var newPwd2 = mainobj.find("[name='newPwd2']").val();

					if (!checkPassWord_register(newPwd)) {
						warning_obj_inner.text(warning);
						openExt(warning_obj, true);
						mainobj.find("[name='newPwd']").val(""); //清空
						mainobj.find("[name='newPwd2']").val(""); //清空
						return false;
					}
					if (!checkPassWord_register(newPwd2)) {
						warning_obj_inner.text(warning);
						openExt(warning_obj, true);
						mainobj.find("[name='newPwd']").val(""); //清空
						mainobj.find("[name='newPwd2']").val(""); //清空
						return false;
					}
					if (!checkRePassWord(newPwd, newPwd2)) {
						warning_obj_inner.text(warning);
						openExt(warning_obj, true);
						mainobj.find("[name='newPwd']").val(""); //清空
						mainobj.find("[name='newPwd2']").val(""); //清空
						return false;
					}
					var data = {
						"newPwd" : newPwd,
					};
					$.ajax({
						type : 'POST',
						url : action,
						data : data,
						success : function(data) {
							if (data.success === true) {
								//跳转成功页面
								window.location.href = "resetSuccess.do"
							} else {
								warning_obj_inner.text("重置失败:"
										+ data.modForm.errorString);
								openExt(warning_obj, true);
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
