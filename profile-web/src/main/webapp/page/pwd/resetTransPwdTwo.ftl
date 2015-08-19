<#import "../share/pc/layout.ftl" as page> <@page.pc>
<div class="user_page">
	<div class="main clearfix">
		<div class="step_part">
			<ul class="step_top">
				<li class="item1 "><span class=""> <i> 1 </i> <b> 验证短信 </b>
				</span></li>
				<li class="item2 active"><span class="active"> <i> 2 </i> <b> 设置新交易密码 </b>
				</span></li>
				<li class="item3 "><span class=""> <i> 3 </i> <b> 完成 </b>
				</span></li>
			</ul>
			<form>
				<div class="input_row">
					<label> 新交易密码： </label>

					<div class="input_part">
						<input id="firstTransPwd" name="firstTransPwd" maxlength="20" type="password" autocomplete="off" />
					</div>
				</div>
				<div class="input_row">
					<label> 确认新交易密码： </label>

					<div class="input_part">
						<input id="firstTransPwd2" name="firstTransPwd2" maxlength="20" type="password" autocomplete="off" />
					</div>

				</div>

				<div class="input_row">
					<label> </label> <a href="javascript:submitForm();" id="resetTransPwd" class="submit sumbit1">下一步</a>
				</div>
			</form>
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
	<script type="text/javascript">
		/**
		 * 提交表单
		 */
		function submitForm() {

			var firstTransPwd = $("#firstTransPwd").val();
			var firstTransPwd2 = $("#firstTransPwd2").val();

			if (!checkPassWord_register(firstTransPwd)) {
				remove_infornmation_alert("firstTransPwd");
				infornmation_alert("error", "firstTransPwd", "新" + warning);
				return;
			}
			if (!checkPassWord_register(firstTransPwd2)) {
				remove_infornmation_alert("firstTransPwd2");
				infornmation_alert("error", "firstTransPwd2", "确认" + warning);
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
					infornmation_alert("error", "firstTransPwd", "新密码和确认密码必须相同");
					return;
				}
			}
			var action = "resetTransPwd.do";
			var data = {
				"firstTransPwd" : firstTransPwd,
				changeStep : 'PwdTwo'
			};

			$.ajax({
				type : 'POST',
				url : action,
				data : data,
				success : function(data) {
					if (data.success === true) {
						window.location.href = "resetTransPwdThree.do";
					} else {
						var target = $(".ext_state2");
						target.find("h2").text("交易密码重置失败");
						target.find("p")
								.html(
										"<span>" + data.modForm.errorString
												+ "</span>");
						openExt(".ext_state2");
					}

				},
				error : function(error) {
				},
				dataType : "json"
			});

		}

		$(function() {
			//重置交易密码blur
			$("[name=firstTransPwd]").blur(
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
								infornmation_alert("error", "firstTransPwd",
										"新密码和确认密码必须相同");
							}
						} else {
							remove_infornmation_alert("firstTransPwd");
							remove_infornmation_alert("firstTransPwd2");
						}

					});

			$("[name=firstTransPwd2]").blur(
					function() {
						var firstTransPwd = $("#firstTransPwd").val();
						var firstTransPwd2 = $("#firstTransPwd2").val();
						if (!checkPassWord_register(firstTransPwd2)) {
							remove_infornmation_alert("firstTransPwd2");
							infornmation_alert("error", "firstTransPwd2", "确认"
									+ warning);
						} else if (!checkRePassWord(firstTransPwd,
								firstTransPwd2)) {
							if (firstTransPwd == "") {
								remove_infornmation_alert("firstTransPwd");
								remove_infornmation_alert("firstTransPwd2");
							} else {
								remove_infornmation_alert("firstTransPwd");
								remove_infornmation_alert("firstTransPwd2");
								infornmation_alert("error", "firstTransPwd",
										"新密码和确认密码必须相同");
							}
						} else {
							remove_infornmation_alert("firstTransPwd");
							remove_infornmation_alert("firstTransPwd2");
						}

					});

		});
	</script>
	</@page.pc>