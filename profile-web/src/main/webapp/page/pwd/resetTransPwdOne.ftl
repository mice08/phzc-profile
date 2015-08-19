<#import "../share/pc/layout.ftl" as page> <@page.pc>
<div class="user_page">
	<div class="main clearfix">
		<div class="step_part">
			<ul class="step_top">
				<li class="item1 active"><span class="active"> <i> 1 </i> <b> 验证短信 </b>
				</span></li>
				<li class="item2 "><span class=""> <i> 2 </i> <b> 设置新交易密码 </b>
				</span></li>
				<li class="item3 "><span class=""> <i> 3 </i> <b> 完成 </b>
				</span></li>
			</ul>
			<form>
				<div class="input_row">
					<label> 手机号码： </label>

					<div class="input_part">
						<input type="text" value="${phoneNo!}" readonly />
					</div>
				</div>
				<div class="input_row">
					<label> 短信验证码： </label>

					<div class="input_part yzm_part">
						<input type="text" disabled="disabled" id="telCode" name="telCode" maxlength="6" onblur="checkTelCode()" />
					</div>
					<div id="sendMessage">
						<a class="yzm" href="javascript:void(0);" > 发送验证码 </a>
					</div>
				</div>

				<div class="input_row">
					<label> </label> <a href="javascript:submitForm();" id="updateUsrMp1" class="submit sumbit1">下一步</a>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			enable_yzm();
				click_yzm(function(){
					rewiSendMessage();
				},function(){
					
				})
		})
	
		function checkTelCode() {
			remove_infornmation_alert("telCode");
		}
		/**
		 * 检查校验码
		 */
		function submitForm() {
			var telCode = $("#telCode").val();//短信验证码
			if (!telCode) {
				remove_infornmation_alert("telCode");
				infornmation_alert("error", "telCode", "请填写短信验证码");
				return;
			}
			//            $("#telCodeMsg").hide();
			//显示遮罩
			/*$('#rechargeDl').showLoading();*/
			//异步提交验证请求
			$.ajax({
				url : "verifyMessageCode.do",
				type : "POST",
				dataType : "json",
				data : {
					telCode : telCode,
					changeStep : 'PwdOne'
				},
				success : function(data) {
					if (data.success) {
						window.location.href = "resetTransPwdTwo.do";
					} else {
						alert(data.message);
						return;
					}
				},
				error : function(data) {
					alert('服务器异常！');
				}
			});
		}
		/**
		 * 发送短信验证码
		 */
		function rewiSendMessage() {
			$("#telCode").removeAttr("disabled");
			//            sendCount();
			$.ajax({
				url : "sendForResetTransPwd.do",
				type : "POST",
				dataType : 'json',
				data : 'changeStep=PwdOne'
			}).done(function(data) {
				if (!data.success) {
					//alert(data.message);
					return;
				}
			});
		}
	</script>
	</@page.pc>