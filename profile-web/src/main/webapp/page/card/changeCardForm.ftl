<#import "../share/pc/layout.ftl" as page> 
<@page.pc>

<div class="user_page">
	<div class="main clearfix">
	
<#include "../leftNav.ftl">
	<@leftNav activeMenu="item3">
</@leftNav>

<div class="right">
	<div class="user_page_inner  user_page3">
		<h1>更换银行卡</h1>
		<div class="user_form">
			<form class="form_type1  user_page3_2_form" id = "form_changeCard" action="changeCard.do">
				<input id="cardSeqId_hidden" type="hidden" value="${cardSeqId}" />
				<input id="mobile_hidden" type="hidden" value="${mobile}" />
				<div class="input_row">
					<label> <i>*</i>短信验证码
					</label>
					<div class="input_part yzm_part">
						<input type="text" name="smsVerifyCode">
					</div>
					<a href="javascript:void(0);" class="yzm"> 发送验证码 </a> <span class="sp">
						(将向你的手机${confusedMobile}发送短信验证码) </span>
				</div>
				<div class="input_row">
					<label> <i>*</i>交易密码
					</label>
					<div class="input_part">
						<input type="password" name="transPwd">
					</div>
				</div>
				<div class="input_row">
					<label> </label> <a class="submit" href="javascript:void(0);"> 提交 </a>
				</div>
			</form>
		</div>
		<script>
		    (function($){
		        var yzm_link=$("a.yzm");
		        var setInterval_obj=null;
		        function reverseTime(num){
		            num-=1;
		            yzm_link.text(num+"s,重新发送验证码");
		            setInterval_obj=setInterval(function(){
		                num-=1;
		                yzm_link.text(num+"s,重新发送验证码");
		            },1000);
		            setInterval(function(){
		                clearInterval(setInterval_obj);
		                yzm_link.text("重新发送验证码");
		                yzm_link.removeClass("disable")
		            },1000*num)
		        }

		        yzm_link.click(function(e){
		            if($(this).hasClass("disable")) return;
		            e.preventDefault();
		            yzm_link.addClass("disable");
		            var num = 60;
		            reverseTime(num);
		        
					var mobile_val = $("#mobile_hidden").val();
					var data = {
						"mobile" : mobile_val
					};

					$.ajax({
						type : 'POST',
						url : "/sms/sendForUnbind.do",
						data : data,
						success : function(data) {
						},
						error : function(error) {
						},
						dataType : "json"
					});							        
		        
		        })
		    })(jQuery);
		
		</script>
	</div>
</div>
</div>

<script>

/*form_unbind*/
$(function() {
	var form_bind = $("#form_changeCard");
	form_bind.find("a.submit").click(function(e) {
		var mainobj = $(this).parents("form");
		e.preventDefault();
		e.stopPropagation();
		var action = mainobj.attr("action");

		var cardSeqId = $("#cardSeqId_hidden").val();
		var transPwd = mainobj.find("[name='transPwd']").val();
		var smsVerifyCode = mainobj.find("[name='smsVerifyCode']").val();

		var data = {
			"cardSeqId" : cardSeqId,
			"transPwd" : transPwd,
			"smsVerifyCode" : smsVerifyCode
		};

		$.ajax({
			type : 'POST',
			url : action,
			data : data,
			success : function(data) {
				if (data.success === true) {
					window.location.href = "/card/myCards.do";
				} else {
					alert(data.cardForm.errorString);
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