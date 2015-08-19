<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p", "parentName":"添加银行卡"} in page>
<@page.pc>

		<section class="payment_addcard2 no_banner">
			<form id="bindStepTwoForm" class="form_order" action="bindStepTwo.do">
				<div class="warning_form">信息将加密处理，仅用于银行卡验证</div>
				<ul class="clearfix">
					<li>
						<label>验证码</label>
						<input type="text" class="yzm" placeholder="请输入短信验证码" name="smsVerifyCode">
						<a href="javascript:void(0);" class="yzm_link">
							重新发送
						</a>
					</li>
					<li class="normal">
						<a href="javascript:void(0);" class="checkbox checked">点击</a> 
						<i>我已阅读并同意
							<a href="javascript:void(0);" class="open">《用户协议》</a>
						</i> 
					</li>
					<li class="normal">
						<a class="submit" onclick="next();">下一步</a>
					</li>
				</ul>
			</form>
		</section>
	</div>
	<div class="ext ext_warning">
		<div class="wrapper">用户名、手机号或密码有误</div>
	</div>
	<div class="logo"></div>

<script type="text/javascript">

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
        var form_register = $("#form_register");
        
        if ($(this).hasClass("disable"))
            return;
        e.preventDefault();
        yzm_link.addClass("disable");
        var num = 60;
        reverseTime(num);

        sendSmsVerifyCode();
    });

function sendSmsVerifyCode() {
	$.ajax({
        type: 'GET',
        url: "bindStepOne.do",
        data: {
            certStatus: '${cardForm.certStatus}',
            cardId: '${cardForm.cardId}',
            bankName: '${cardForm.bankName}',
            mobile: '${cardForm.mobile}'
        },
        dateType: 'json',
        success: function(data) {
            console.log("ret: ", data);
            if (data.success === true) {
                location.reload();
            } else {
                warning_obj_inner.text("重新提交失败: " + data.msg);
                openExt(warning_obj, true);
                return false;
            }
        },
        error: function(error) {
        }
    });
}

function next() {
	var smsVerifyCode = $("[name=smsVerifyCode]").val();
    if ($.trim(smsVerifyCode) === "") {
        warning_obj_inner.text("请输入短信验证码");
        openExt(warning_obj, true);
        return false;
    }

    addFullLoading();

    var action = $("#bindStepTwoForm").attr("action");
	$.ajax({
        type: 'POST',
        url: action,
        data: {
        	smsVerifyCode: smsVerifyCode
        },
        dateType: 'json',
        success: function(data) {
            console.log("ret: ", data);
            removeLoading();
            if (data.success === true) {
                window.location.href = "bindSuccess.do?bankId=${cardForm.bankId}&bankName=" + encodeURI("${cardForm.bankName!''}") + "&cardId=${cardForm.cardId}"
                    + "&source=" + encodeURI("${source!''}");
            } else {
                warning_obj_inner.text("提交失败: " + data.msg);
                openExt(warning_obj, true);
                return false;
            }
        } ,
        error: function(error) {
            removeLoading();
        }
    });
}

</script>

</@page.pc>