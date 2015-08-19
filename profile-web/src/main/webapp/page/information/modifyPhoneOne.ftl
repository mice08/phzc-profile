<#import "../share/pc/layout.ftl" as page>
<@page.pc>
<div class="user_page">
    <div class="main clearfix">
        <div class="step_part">
            <ul class="step_top">
                <li class="item1 active">
        <span class="active">
            <i>
                1
            </i>
            <b>
                验证身份
            </b>
        </span>
                </li>
                <li class="item2 ">
        <span class="">
            <i>
                2
            </i>
            <b>
                设置新手机号码
            </b>
        </span>
                </li>
                <li class="item3 ">
        <span class="">
            <i>
                3
            </i>
            <b>
                完成
            </b>
        </span>
                </li>
            </ul>
            <form>
                <div class="input_row">
                    <label>
                        已验证手机号码：
                    </label>

                    <div class="input_part">
                        <input type="text" value="${phoneNo!}" readonly/>
                    </div>
                </div>
                <div class="input_row">
                    <label>
                        短信验证码：
                    </label>

                    <div class="input_part yzm_part">
                        <input type="text" disabled="disabled" id="telCode" name="telCode" maxlength="6"
                               onblur="checkTelCode()"/>
                    </div>
                    <div id="sendMessage">
                        <a class="yzm" href="javascript:void(0);" onclick="rewiSendMessage()">
                            发送验证码
                        </a>
                    </div>
                </div>

                <div class="input_row">
                    <label>
                    </label>
                    <a href="javascript:submitForm();" id="updateUsrMp1" class="submit sumbit1">下一步</a>
                </div>
            </form>
        </div>
    </div>
    <script type="text/javascript">

        (function($){
            var yzm_link=$("a.yzm");
            var setInterval_obj=null;
            function reverseTime(num){
                num-=1;
                yzm_link.text(num+"s");
                setInterval_obj=setInterval(function(){
                    num-=1;
                    yzm_link.text(num+"s");
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
            })
        })(jQuery);

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
                infornmation_alert("error","telCode","请填写短信验证码");
                return;
            }
//            $("#telCodeMsg").hide();
            //显示遮罩
            /*$('#rechargeDl').showLoading();*/
            //异步提交验证请求
            $.ajax({
                url: "${config.getDomainProfile()}/information/verifyMessageCode.do",
                type: "POST",
                dataType: "json",
                data:{
                    telCode:telCode,
                    changeStep:'PhoneOne'
                },
                success: function (data) {
                    if (data.success) {
                        window.location.href = "${config.getDomainProfile()}/information/changePhoneTwo.do";
                    } else {
                        alert(data.message);
                        return;
                    }
                },
                error: function (data) {
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
                url: "${config.getDomainProfile()}/information/sendForModifyPhone.do",
                type: "POST",
                dataType: 'json',
                data: 'changeStep=PhoneOne'
            }).done(function (data) {
                if (!data.success) {
                    alert(data.message);
                    return;
                }
            });
        }
        /**
         * 短信发送倒计时
         */
        /*function sendCount() {
            if (countNum > 0) {
                setTimeout(sendCount, 1000);
                countNum--;
                $("#sendMessage").html("&nbsp;&nbsp;" + countNum + "秒后可重新发送");
            } else {
                countNum = 60;
                $("#sendMessage").html("<a href=\"javascript:void(0);\" class=\"yzm\" onclick=\"rewiSendMessage();\">获取短信验证码</a>");

            }
        }*/
    </script>
</@page.pc>