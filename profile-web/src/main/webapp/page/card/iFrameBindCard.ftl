<#import "../share/pc/iFrameLayout.ftl" as page> 
<@page.pc>

<input id="msg_hidden" type="hidden" value="${msg!''}" />

<div class="ext ext_iframe">
	<div id="bindForm" class="noframe ext_card_add">
		<p class="user_warning">请保持身份信息与该银行卡开户信息一致，否则将认证失败。只支持储蓄卡绑定。</p>
		<form id="form_bindCard" action="iFrameBind.do">
			<div class="input_row">
				<label> 持卡人 </label> 
				<span name="realName"> ${userInfo.usrName} </span>
			</div>
			<div class="input_row">
				<label> 银行卡号 </label>
				<div class="input_part">
					<input type="text" name="cardId">
				</div>
			</div>
			<div class="input_row">
				<label> 银行 </label> 
				<span name="bankName">  </span>
			</div>
			<div class="input_row">
				<label> 手机号码 </label>
				<div class="input_part">
					<input type="text" name="mobile"> 
					<span> 银行预留手机号码 </span>
				</div>
			</div>
			<div class="input_row">
				<label> 短信验证码 </label>
				<div class="input_part yzm_part">
					<input type="text" name="smsVerifyCode"> 
					<span></span>
				</div>
				<a href="javascript:void(0);" class="yzm"> 发送验证码 </a>
			</div>
			<div class="input_row">
				<label> </label> <input type="checkbox" name="file3" value="1">
				<span> 我已阅读并同意<a href="javascript:void(0);" onclick="openProtocol(); return true;">《快捷支付协议》</a>
				</span>
			</div>
			<div class="input_row">
				<label> </label> <a class="submit" href="javascript:void(0);"> 同意协议并提交 </a>
			</div>
		</form>
		<div class="user_note">
			<span> 说明： </span><br> 1、仅支持绑定储蓄卡<br>
			2、支持的银行包括：平安银行、工商银行、建设银行、中国银行、招商银行、中信银行、民生银行、浦东发展银行、广大银行、兴业银行、邮政储蓄银行、广发银行、华夏银行、深发展银行、广州银行、广州农商银行
		</div>
	</div>
	<div id="submitResult" class="noframe ext_state1" style="display:none;">
		<div class="ext_hint ext_hint_success">
			<div class="img"></div>
			<div class="content">
				<h2>提交成功，等待银行电话确认</h2>
				<p>
					10分钟内将收到广州银联02096585来电，接到电话前请勿关闭当前窗口。<br />
					来电后，请按照语音提示输入绑定银行卡的交易密码，确认您是卡主。<br />
					关闭当前窗口后如发现尚未出现绑定的银行卡，你可以尝试刷新支付页面。<br />
				</p>
				<a href="javascript:void(0);" onclick="confirmSumbitSuccess();" class="confirm2">
					已成功绑卡
        		</a>
        		<p style="color: #ff0000; margin-top: 6px;">
        			请在接到电话，确认绑卡成功后再点击绑定成功。
        		</p>
			</div>
		</div>
	</div>
</div>

<script>

function openProtocol() {
	parent.window.open("${config.getDomainWww()}/static/protocol3.html");
}

function alertMsg() {
	var msg = $("#msg_hidden").val();
	if (msg !== null && msg !== "" && typeof(msg) !== "undefined") {
		alert(msg);
	}
}

window.onload = alertMsg;

$(function(){
	click_yzm(function() {
		var form_bindCard = $("#form_bindCard");
		var mobile_val = form_bindCard.find("[name='mobile']").val();
		
		var data = {
			"mobile" : mobile_val
		};

		$.ajax({
			type : 'POST',
			url : "/sms/sendForBind.do",
			data : data,
			success : function(data) {
			},
			error : function(error) {
			},
			dataType : "json"
		});
	}, function() {});

});

function confirmSumbitSuccess() {
	parent.${successFunction}();
}

/*form_bind*/
$(function() {
	
	$("[name=cardId]").blur(function(e) {
		remove_infornmation_alert("cardId");
		var cardId = this.value;
		if($.trim(cardId) === "") {
			infornmation_alert("error", "cardId", "请输入银行卡号");
			return false;
		}
		
		$.ajax({
			type : 'GET',
			url : 'queryBankInfo.do',
			data : {
				'cardId' : cardId
			},
			success : function(data) {
				if (data.success === true) {
					$("[name=bankName]").text(data.bankInfo.bankNm);
				}
			},
			error : function(error) {
			},
			dataType : "json"
		});
		
	});
	
	$("[name=mobile]").blur(function(e) {
		remove_infornmation_alert("mobile");
		var mobile = this.value;
		if(!checkUserName_phone(mobile)) {
			infornmation_alert("error", "mobile", warning);
			lock_yzm();
			return false;
		} else {
			enable_yzm();
		}
	});
	
	var form_bind = $("#form_bindCard");
	form_bind.find("a.submit").click(function(e) {
		remove_infornmation_alert("cardId");
		remove_infornmation_alert("bankName");
		remove_infornmation_alert("mobile");
		remove_infornmation_alert("smsVerifyCode");
		
		var mainobj = $(this).parents("form");
		e.preventDefault();
		e.stopPropagation();
		var action = mainobj.attr("action");
		
		var cardId = mainobj.find("[name='cardId']").val();
		var bankName = mainobj.find("[name='bankName']").text();
		var mobile = mainobj.find("[name='mobile']").val();
		var smsVerifyCode = mainobj.find("[name='smsVerifyCode']").val();
		
		if ($.trim(cardId) === "") {
			infornmation_alert("error", "cardId", "请输入银行卡号");
			return false;
		}
		
		if(!checkUserName_phone(mobile)) {
			infornmation_alert("error", "mobile", warning);
			lock_yzm();
			return false;
		} else {
			enable_yzm();
		}
		
		if($.trim(smsVerifyCode) === "") {
			infornmation_alert("error", "smsVerifyCode", "请输入短信验证码");
			return false;
		}
		
		if (mainobj.find("[name='file3']").attr("checked") === undefined) {
			alert("请先同意快捷支付协议");
			return false;
		}
		
		var data = {
			"cardId" : cardId,
			"bankName" : bankName,
			"mobile" : mobile,
			"smsVerifyCode" : smsVerifyCode
		};

		$.ajax({
			type : 'POST',
			url : action,
			data : data,
			success : function(data) {
				if (data.success === true) {
					//call parent's success functionality
					
					$("#bindForm")[0].style.display="none";
					$("#submitResult")[0].style.display="";
					
				} else {
					//call parent's failure functionality
					parent.${failureFunction}(data.msg);
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