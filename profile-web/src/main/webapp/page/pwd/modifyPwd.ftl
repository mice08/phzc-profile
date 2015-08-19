<#import "../share/pc/layout.ftl" as page> 
<#assign titleName="修改密码" in page>
<#assign actives="active3" in page>
<@page.pc>
<div class="user_page">
	<div class="main clearfix">
		<#include "../leftNav.ftl"> <@leftNav activeMenu="item8"> </@leftNav>
		<div class="right">
			<div class="user_page_inner  user_page8-1">
				<input id="isTransPwdSet" value="${isTransPwdSet}" type="hidden"> <input id="chkStat" value="${chkStat}" type="hidden">

				<ul class="user_top cha_pass">
					<li class="first"><a href="javascript:void(0);" id="tab_login" class="active" onclick="switchTab(1);"> 登录密码 </a></li>

					<li><a id="tab_trans" href="javascript:void(0);" style="display: none" onclick="switchTab(2);"> 交易密码 </a></li>

				</ul>
				<form class="cha_pass_f" id="form_modifyLoginPwd" action="modifyLoginPwd.do">
					<div class="input_row ">
						<label> <i>*</i>原密码
						</label>
						<div class="input_part">
							<input id="orgLoginPwd" name="orgLoginPwd" class="f_pass" type="password" maxlength="20" autocomplete="off">

						</div>
						<a href="${config.getDomainPassport()}/pwd/resetVerifyForm.do" class="normal"> 忘记密码？ </a>
					</div>

					<div class="input_row">
						<label> <i>*</i>新密码
						</label>
						<div class="input_part ">
							<input id="newLoginPwd" name="newLoginPwd" class="f_pass" type="password" maxlength="20" autocomplete="off"> <span></span>
						</div>
						<!-- 	<div class="password_check">
							<p>密码长度为6~20长度，必须包含数字和字母</p>
							<ul>
								<li class="password_item1 active"></li>
								<li></li>
								<li class="password_item3"></li>
							</ul>
						</div> -->
					</div>
					<div class="input_row">
						<label> <i>*</i>确认新密码
						</label>
						<div class="input_part ">
							<input id="newLoginPwd2" name="newLoginPwd2" class="f_pass" type="password" maxlength="20" autocomplete="off"> <span></span>
						</div>


					</div>

					<div class="input_row">
						<label> </label> <a class="submit" href="#"> 保存 </a>
					</div>

				</form>

				<form class="cha_pass_f" id="form_modifyTransPwd" action="modifyTransPwd.do" style="display: none">
					<div class="input_row">
						<label> <i>*</i>原密码
						</label>
						<div class="input_part">
							<input id="orgTransPwd" name="orgTransPwd" class="f_pass" type="password" maxlength="20" autocomplete="off">
						</div>
						<a href="${config.getDomainProfile()}/modifyPwd/resetTransPwdOne.do" class="normal"> 忘记密码？ </a>
					</div>

					<div class="input_row">
						<label> <i>*</i>新密码
						</label>
						<div class="input_part ">
							<input id="newTransPwd" name="newTransPwd" class="f_pass" type="password" maxlength="20" autocomplete="off"> <span></span>
						</div>

					</div>

					<div class="input_row">
						<label> <i>*</i>确认新密码
						</label>
						<div class="input_part ">
							<input id="newTransPwd2" name="newTransPwd2" class="f_pass" type="password" maxlength="20" autocomplete="off"> <span></span>
						</div>

					</div>

					<div class="input_row">
						<label> </label> <a class="submit" href="#"> 保存 </a>
					</div>

				</form>

				<form class="cha_pass_f" id="form_setTransPwd" action="setTransPwd.do" style="display: none">

					<div class="input_row">
						<label> <i>*</i>新密码
						</label>
						<div class="input_part ">
							<input id="firstTransPwd" name="firstTransPwd" class="f_pass" type="password" maxlength="20" autocomplete="off"> <span></span>
						</div>

					</div>

					<div class="input_row">
						<label> <i>*</i>确认新密码
						</label>
						<div class="input_part ">
							<input id="firstTransPwd2" name="firstTransPwd2" class="f_pass" type="password" maxlength="20" autocomplete="off"> <span></span>
						</div>

					</div>

					<div class="input_row">
						<label> </label> <a class="submit" href="#"> 保存 </a>
					</div>

				</form>
			</div>
		</div>
	</div>
</div>
<div class="bgcover"></div>
<div class="ext ext_state1">
	<h1>成功</h1>
	<div class="ext_hint ext_hint_success">
		<div class="img"></div>
		<div class="content">
			<h2>恭喜您，登录密码修改成功</h2>
			<p>
				<!-- 提现资金预计将在<span>2015-04-12</span>前到帐 -->
			</p>
		</div>
	</div>
	<a href="#" class="close" onclick="clearInfo();"> 关闭 </a>
</div>
<div class="ext ext_state2">
	<h1>失败</h1>
	<div class="ext_hint ext_hint_failed">
		<div class="img"></div>
		<div class="content">
			<h2>修改失败</h2>
			<p>
				<!-- 可能是网络异常。请稍后再试。 -->
			</p>
		</div>
	</div>
	<a href="#" class="close" onclick="clearInfo();"> 关闭 </a>
</div>

<div class="ext ext_state3">
	<h1>成功</h1>
	<div class="ext_hint ext_hint_success">
		<div class="img"></div>
		<div class="content">
			<h2>恭喜您，交易密码设置成功</h2>
			<p>
				<!-- 提现资金预计将在<span>2015-04-12</span>前到帐 -->
			</p>
		</div>
	</div>
	<a href="#" class="close" onclick="reloadPage();"> 关闭 </a>
</div>
<script>
	/* 验证密码开始 */
	function verifyPassword(obj) {
		var pwdReg = /^(?![^a-zA-Z]+$)(?!\D+$).{6,20}$/; // /^(?=.*?[a-zA-Z])(?=.*?[0-6])[!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~A-Za-z0-9]{8,16}$/;
		return pwdReg.test(obj);
	}

	//刷新页面
	function reloadPage() {
		location.reload();
	}

	window.onload = function() {
		var chkStat = $("#chkStat").val();
		var form_login = $("#form_modifyLoginPwd");
		var form_trans = $("#form_modifyTransPwd");
		var form_set_trans = $("#form_setTransPwd");
		var tab_login = $("#tab_login");
		var tab_trans = $("#tab_trans");
		
		if (chkStat == 0) {
			tab_login.addClass("active");
			tab_trans.css("display", "none");
			form_login.css("display", "block");
			form_trans.css("display", "none");
			form_set_trans.css("display", "none");
		}else{
			tab_login.addClass("active");
			tab_trans.css("display", "block");
			tab_trans.removeClass("active");
			form_login.css("display", "block");
			form_trans.css("display", "none");
			form_set_trans.css("display", "none");
		}
	};

	//清空所有输入框和提示
	function clearInfo() {
		$("#orgLoginPwd").val("");
		remove_infornmation_alert("orgLoginPwd");
		$("#newLoginPwd").val("");
		remove_infornmation_alert("newLoginPwd");
		$("#newLoginPwd2").val("");
		remove_infornmation_alert("newLoginPwd2");
		$("#orgTransPwd").val("");
		remove_infornmation_alert("orgTransPwd");
		$("#newTransPwd").val("");
		remove_infornmation_alert("newTransPwd");
		$("#newTransPwd2").val("");
		remove_infornmation_alert("newTransPwd2");
		$("#firstTransPwd").val("");
		remove_infornmation_alert("firstTransPwd");
		$("#firstTransPwd2").val("");
		remove_infornmation_alert("firstTransPwd2");
	}

	//切换tab
	function switchTab(str) {
		var isTransPwdSet = $("#isTransPwdSet").val();
		var form_login = $("#form_modifyLoginPwd");
		var form_trans = $("#form_modifyTransPwd");
		var form_set_trans = $("#form_setTransPwd");
		var tab_login = $("#tab_login");
		var tab_trans = $("#tab_trans");

		//切换tab，清空所有输入框和提示
		clearInfo();

		if (isTransPwdSet == 0) {
			if (str == 1) {
				tab_login.addClass("active");
				tab_trans.removeClass("active");
				form_login.css("display", "block");
				form_set_trans.css("display", "none");
			} else {
				tab_login.removeClass("active");
				tab_trans.addClass("active");
				form_login.css("display", "none");
				form_set_trans.css("display", "block");
			}
		} else {
			if (str == 1) {
				tab_login.addClass("active");
				tab_trans.removeClass("active");
				form_login.css("display", "block");
				form_trans.css("display", "none");
			} else {
				tab_login.removeClass("active");
				tab_trans.addClass("active");
				form_login.css("display", "none");
				form_trans.css("display", "block");
			}
		}
	}

	/*form_modifyLoginPwd 异步请求修改登录密码 */
	$(function() {
		var form_login = $("#form_modifyLoginPwd");
		form_login.find("a.submit").click(
				function(e) {
					var mainobj = $(this).parents("form");
					e.preventDefault();
					e.stopPropagation();
					var action = mainobj.attr("action");

					var orgLoginPwd = mainobj.find("[name='orgLoginPwd']")
							.val();
					var newLoginPwd = mainobj.find("[name='newLoginPwd']")
							.val();
					var newLoginPwd2 = mainobj.find("[name='newLoginPwd2']")
							.val();

					if (!checkPassWord_register(orgLoginPwd)) {
						remove_infornmation_alert("orgLoginPwd");
						infornmation_alert("error", "orgLoginPwd", "原"
								+ warning);
						return;
					}
					if (!checkPassWord_register(newLoginPwd)) {
						remove_infornmation_alert("newLoginPwd");
						infornmation_alert("error", "newLoginPwd", "新"
								+ warning);
						return;
					}
					if (!checkPassWord_register(newLoginPwd2)) {
						remove_infornmation_alert("newLoginPwd2");
						infornmation_alert("error", "newLoginPwd2", "确认"
								+ warning);
						return;
					}
					if (orgLoginPwd == newLoginPwd && orgLoginPwd != "") {
						remove_infornmation_alert("newLoginPwd");
						infornmation_alert("error", "newLoginPwd",
								"原密码和新登录密码不能相同");
						return;
					}
					if (!checkRePassWord(newLoginPwd, newLoginPwd2)) {
						if (newLoginPwd2 == "") {
							remove_infornmation_alert("newLoginPwd");
							remove_infornmation_alert("newLoginPwd2");
							return;
						} else {
							remove_infornmation_alert("newLoginPwd");
							remove_infornmation_alert("newLoginPwd2");
							infornmation_alert("error", "newLoginPwd",
									"新密码和确认密码必须相同");
							return;
						}
					}

					var data = {
						"orgLoginPwd" : orgLoginPwd,
						"newLoginPwd" : newLoginPwd
					};

					$.ajax({
						type : 'POST',
						url : action,
						data : data,
						success : function(data) {
							if (data.success === true) {
								var target = $(".ext_state1");
								/* target.find("h1").text("成功");
								target.find("h2").html("<span>恭喜您，登录密码修改成功</span>");
								target.find("p").html(""); */
								openExt(".ext_state1");
								clearInfo();
							} else {
								var target = $(".ext_state2");
								target.find("h2").text("登录密码修改失败");
								target.find("p").html(
										"<span>" + data.modForm.errorString
												+ "</span>");
								openExt(".ext_state2");
							}
						},
						error : function(error) {
						},
						dataType : "json"
					});

				});

	});

	/*form_modifyTransPwd 异步请求修改交易密码 */
	$(function() {
		var form_trans = $("#form_modifyTransPwd");
		form_trans.find("a.submit").click(
				function(e) {
					var mainobj = $(this).parents("form");
					e.preventDefault();
					e.stopPropagation();
					var action = mainobj.attr("action");

					var orgTransPwd = mainobj.find("[name='orgTransPwd']")
							.val();
					var newTransPwd = mainobj.find("[name='newTransPwd']")
							.val();
					var newTransPwd2 = mainobj.find("[name='newTransPwd2']")
							.val();

					var valaction = "validTransPwd.do";
					var valdata = {
						"orgTransPwd" : orgTransPwd
					};

					var validTransOrgin = 0;

					$.ajax({
						type : 'POST',
						url : valaction,
						data : valdata,
						async : false, //等ajax做完再继续判断
						success : function(data) {
							if (data.success === true) {
							} else {
								remove_infornmation_alert("orgTransPwd");
								infornmation_alert("error", "orgTransPwd",
										"原密码不正确");
								validTransOrgin = 1;
							}

						},
						error : function(error) {
						},
						dataType : "json"
					});
					if (validTransOrgin == 1) {
						return;
					}

					if (!checkPassWord_register(orgTransPwd)) {
						remove_infornmation_alert("orgTransPwd");
						infornmation_alert("error", "orgTransPwd", "原"
								+ warning);
						return;
					}
					if (!checkPassWord_register(newTransPwd)) {
						remove_infornmation_alert("newTransPwd");
						infornmation_alert("error", "newTransPwd", "新"
								+ warning);
						return;
					}
					if (!checkPassWord_register(newTransPwd2)) {
						remove_infornmation_alert("newTransPwd2");
						infornmation_alert("error", "newTransPwd2", "确认"
								+ warning);
						return;
					}
					if (orgTransPwd == newTransPwd && orgTransPwd != "") {
						remove_infornmation_alert("newTransPwd");
						infornmation_alert("error", "newTransPwd",
								"原密码和新交易密码不能相同");
						return;
					}
					if (!checkRePassWord(newTransPwd, newTransPwd2)) {
						if (newTransPwd2 == "") {
							remove_infornmation_alert("newTransPwd");
							remove_infornmation_alert("newTransPwd2");
							return;
						} else {
							remove_infornmation_alert("newTransPwd");
							remove_infornmation_alert("newTransPwd2");
							infornmation_alert("error", "newTransPwd",
									"新密码和确认密码必须相同");
							return;
						}
					}

					var data = {
						"orgTransPwd" : orgTransPwd,
						"newTransPwd" : newTransPwd
					};

					$.ajax({
						type : 'POST',
						url : action,
						data : data,
						success : function(data) {
							if (data.success === true) {
								var target = $(".ext_state1");
								target.find("h2").html(
										"<span>恭喜您，交易密码修改成功</span>");
								openExt(".ext_state1");
								clearInfo();
							} else {
								var target = $(".ext_state2");
								target.find("h2").text("交易密码修改失败");
								target.find("p").html(
										"<span>" + data.modForm.errorString
												+ "</span>");
								openExt(".ext_state2");
							}
						},
						error : function(error) {
						},
						dataType : "json"
					});

				});

	});

	/*form_setTransPwd 设置交易密码 */
	$(function() {
		var form_trans = $("#form_setTransPwd");
		form_trans.find("a.submit").click(
				function(e) {
					var mainobj = $(this).parents("form");
					e.preventDefault();
					e.stopPropagation();
					var action = mainobj.attr("action");

					var firstTransPwd = mainobj.find("[name='firstTransPwd']")
							.val();
					var firstTransPwd2 = mainobj
							.find("[name='firstTransPwd2']").val();

					if (!checkPassWord_register(firstTransPwd)) {
						remove_infornmation_alert("firstTransPwd");
						infornmation_alert("error", "firstTransPwd", "新"
								+ warning);
						return;
					}
					if (!checkPassWord_register(firstTransPwd2)) {
						remove_infornmation_alert("firstTransPwd2");
						infornmation_alert("error", "firstTransPwd2", "确认"
								+ warning);
						return;
					}
					if (!checkRePassWord(firstTransPwd, firstTransPwd2)) {
						if (firstTransPwd2 == "") {
							remove_infornmation_alert("firstTransPwd");
							remove_infornmation_alert("firstTransPwd2");
							return;
						} else {
							remove_infornmation_alert("firstTransPwd");
							remove_infornmation_alert("firstTransPwd2");
							infornmation_alert("error", "firstTransPwd",
									"新密码和确认密码必须相同");
							return;
						}
					}

					var data = {
						"firstTransPwd" : firstTransPwd
					};

					$.ajax({
						type : 'POST',
						url : action,
						data : data,
						success : function(data) {
							if (data.success === true) {
								openExt(".ext_state3");
							} else {
								var target = $(".ext_state3");
								target.find("h2").text("交易密码设置失败");
								target.find("p").html(
										"<span>" + data.modForm.errorString
												+ "</span>");
								openExt(".ext_state3");
							}

						},
						error : function(error) {
						},
						dataType : "json"
					});

				});

	});

	$(function() {

		//登录密码修改blur
		$("[name=orgLoginPwd]").blur(function() {
			var orgLoginPwd = $("#orgLoginPwd").val();
			var newLoginPwd = $("#newLoginPwd").val();

			if (!checkPassWord_register(orgLoginPwd)) {
				remove_infornmation_alert("orgLoginPwd");
				infornmation_alert("error", "orgLoginPwd", "原" + warning);
			} else if (orgLoginPwd == newLoginPwd && orgLoginPwd != "") {
				remove_infornmation_alert("newLoginPwd");
				infornmation_alert("error", "newLoginPwd", "原密码和新登录密码不能相同");
			} else {
				remove_infornmation_alert("orgLoginPwd");
			}

		});

		$("[name=newLoginPwd]").blur(function() {
			var orgLoginPwd = $("#orgLoginPwd").val();
			var newLoginPwd = $("#newLoginPwd").val();
			var newLoginPwd2 = $("#newLoginPwd2").val();
			if (!checkPassWord_register(newLoginPwd)) {
				remove_infornmation_alert("newLoginPwd");
				infornmation_alert("error", "newLoginPwd", "新" + warning);
			} else if (orgLoginPwd == newLoginPwd && orgLoginPwd != "") {
				remove_infornmation_alert("newLoginPwd");
				infornmation_alert("error", "newLoginPwd", "原密码和新登录密码不能相同");
			} else if (!checkRePassWord(newLoginPwd, newLoginPwd2)) {
				if (newLoginPwd2 == "") {
					remove_infornmation_alert("newLoginPwd");
					remove_infornmation_alert("newLoginPwd2");
				} else {
					remove_infornmation_alert("newLoginPwd");
					remove_infornmation_alert("newLoginPwd2");
					infornmation_alert("error", "newLoginPwd", "新密码和确认密码必须相同");
				}
			} else {
				remove_infornmation_alert("newLoginPwd");
				remove_infornmation_alert("newLoginPwd2");
			}

		});

		$("[name=newLoginPwd2]").blur(function() {
			var orgLoginPwd = $("#orgLoginPwd").val();
			var newLoginPwd = $("#newLoginPwd").val();
			var newLoginPwd2 = $("#newLoginPwd2").val();
			if (!checkPassWord_register(newLoginPwd2)) {
				remove_infornmation_alert("newLoginPwd2");
				infornmation_alert("error", "newLoginPwd2", "确认" + warning);
			} else if (orgLoginPwd == newLoginPwd && orgLoginPwd != "") {
				remove_infornmation_alert("newLoginPwd");
				infornmation_alert("error", "newLoginPwd", "原密码和新登录密码不能相同");
			} else if (!checkRePassWord(newLoginPwd, newLoginPwd2)) {
				if (newLoginPwd == "") {
					remove_infornmation_alert("newLoginPwd");
					remove_infornmation_alert("newLoginPwd2");
				} else {
					remove_infornmation_alert("newLoginPwd");
					remove_infornmation_alert("newLoginPwd2");
					infornmation_alert("error", "newLoginPwd", "新密码和确认密码必须相同");
				}
			} else {
				remove_infornmation_alert("newLoginPwd");
				remove_infornmation_alert("newLoginPwd2");
			}

		});

		//交易密码修改blur
		$("[name=orgTransPwd]").blur(function() {
			var orgTransPwd = $("#orgTransPwd").val();
			var newTransPwd = $("#newTransPwd").val();

			var action = "validTransPwd.do";
			var data = {
				"orgTransPwd" : orgTransPwd
			};

			$.ajax({
				type : 'POST',
				url : action,
				data : data,
				success : function(data) {
					if (data.success === true) {
						remove_infornmation_alert("orgTransPwd");
					} else {
						remove_infornmation_alert("orgTransPwd");
						infornmation_alert("error", "orgTransPwd", "原密码不正确");
					}

				},
				error : function(error) {
				},
				dataType : "json"
			});

			if (!checkPassWord_register(orgTransPwd)) {
				remove_infornmation_alert("orgTransPwd");
				infornmation_alert("error", "orgTransPwd", "原" + warning);
			} else if (orgTransPwd == newTransPwd && orgTransPwd != "") {
				remove_infornmation_alert("newTransPwd");
				infornmation_alert("error", "newTransPwd", "原密码和新交易密码不能相同");
			} else {
				remove_infornmation_alert("orgTransPwd");
			}

		});

		$("[name=newTransPwd]").blur(function() {
			var orgTransPwd = $("#orgTransPwd").val();
			var newTransPwd = $("#newTransPwd").val();
			var newTransPwd2 = $("#newTransPwd2").val();

			if (!checkPassWord_register(newTransPwd)) {
				remove_infornmation_alert("newTransPwd");
				infornmation_alert("error", "newTransPwd", "新" + warning);
			} else if (orgTransPwd == newTransPwd && orgTransPwd != "") {
				remove_infornmation_alert("newTransPwd");
				infornmation_alert("error", "newTransPwd", "原密码和新交易密码不能相同");
			} else if (!checkRePassWord(newTransPwd, newTransPwd2)) {
				if (newTransPwd2 == "") {
					remove_infornmation_alert("newTransPwd");
					remove_infornmation_alert("newTransPwd2");
				} else {
					remove_infornmation_alert("newTransPwd");
					remove_infornmation_alert("newTransPwd2");
					infornmation_alert("error", "newTransPwd", "新密码和确认密码必须相同");
				}
			} else {
				remove_infornmation_alert("newTransPwd");
				remove_infornmation_alert("newTransPwd2");
			}

		});

		$("[name=newTransPwd2]").blur(function() {
			var orgTransPwd = $("#orgTransPwd").val();
			var newTransPwd = $("#newTransPwd").val();
			var newTransPwd2 = $("#newTransPwd2").val();
			if (!checkPassWord_register(newTransPwd2)) {
				remove_infornmation_alert("newTransPwd2");
				infornmation_alert("error", "newTransPwd2", "确认" + warning);
			} else if (orgTransPwd == newTransPwd && orgTransPwd != "") {
				remove_infornmation_alert("newTransPwd");
				infornmation_alert("error", "newTransPwd", "原密码和新交易密码不能相同");
			} else if (!checkRePassWord(newTransPwd, newTransPwd2)) {
				if (newTransPwd == "") {
					remove_infornmation_alert("newTransPwd");
					remove_infornmation_alert("newTransPwd2");
				} else {
					remove_infornmation_alert("newTransPwd");
					remove_infornmation_alert("newTransPwd2");
					infornmation_alert("error", "newTransPwd", "新密码和确认密码必须相同");
				}
			} else {
				remove_infornmation_alert("newTransPwd");
				remove_infornmation_alert("newTransPwd2");
			}

		});

		//设置交易密码blur
		$("[name=firstTransPwd]")
				.blur(
						function() {
							var firstTransPwd = $("#firstTransPwd").val();
							var firstTransPwd2 = $("#firstTransPwd2").val();
							if (!checkPassWord_register(firstTransPwd)) {
								remove_infornmation_alert("firstTransPwd");
								infornmation_alert("error", "firstTransPwd",
										warning);
							} else if (!checkRePassWord(firstTransPwd,
									firstTransPwd2)) {
								if (firstTransPwd2 == "") {
									remove_infornmation_alert("firstTransPwd");
									remove_infornmation_alert("firstTransPwd2");
								} else {
									remove_infornmation_alert("firstTransPwd");
									remove_infornmation_alert("firstTransPwd2");
									infornmation_alert("error",
											"firstTransPwd", "新密码和确认密码必须相同");
								}
							} else {
								remove_infornmation_alert("firstTransPwd");
								remove_infornmation_alert("firstTransPwd2");
							}

						});

		$("[name=firstTransPwd2]")
				.blur(
						function() {
							var firstTransPwd = $("#firstTransPwd").val();
							var firstTransPwd2 = $("#firstTransPwd2").val();
							if (!checkPassWord_register(firstTransPwd2)) {
								remove_infornmation_alert("firstTransPwd2");
								infornmation_alert("error", "firstTransPwd2",
										"确认" + warning);
							} else if (!checkRePassWord(firstTransPwd,
									firstTransPwd2)) {
								if (firstTransPwd == "") {
									remove_infornmation_alert("firstTransPwd");
									remove_infornmation_alert("firstTransPwd2");
								} else {
									remove_infornmation_alert("firstTransPwd");
									remove_infornmation_alert("firstTransPwd2");
									infornmation_alert("error",
											"firstTransPwd", "新密码和确认密码必须相同");
								}
							} else {
								remove_infornmation_alert("firstTransPwd");
								remove_infornmation_alert("firstTransPwd2");
							}

						});

	});
</script>

</@page.pc>
