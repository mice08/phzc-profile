<#import "../share/pc/layout.ftl" as page>
<@page.pc>
<div class="user_page">
    <div class="main clearfix">
        <div class="step_part">
            <ul class="step_top">
                <li class="item1 ">
        <span class="">
            <i>
                1
            </i>
            <b>
                验证身份
            </b>
        </span>
                </li>
                <li class="item2 active">
        <span class="active">
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
                        新手机号码：
                    </label>

                    <div class="input_part">
                        <input type="text" id="mobileNo" name="mobileNo" maxlength="11" onblur="checkUsrMp()"/>
                    </div>
                </div>
                <div class="input_row">
                    <label>
                        短信验证码：
                    </label>

                    <div class="input_part yzm_part">
                        <input type="text" disabled="disabled" id="telCode" name="telCode"
                               maxlength="6"
                               onblur="checkTelCode()"/>
                    </div>
                    <a href="javascript:void(0);" class="yzm" ">
                        发送验证码
                    </a>

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

        function IsMobile(mobileNo) {

            if (!(/^1[3458][0-9]\d{8}$/.test(mobileNo))) {
                return false;
            }
            else {
                return true;
            }
        }

        function checkUsrMp() {
            remove_infornmation_alert("mobileNo");
            var mobileNo = $("#mobileNo").val();//手机号
            if (!mobileNo) {
                infornmation_alert("error","mobileNo","手机号不能为空");
                return false;
            } else if (!IsMobile(mobileNo)) {
                infornmation_alert("error","mobileNo","手机号格式不正确");
                return false;
            } else {
                return true;
            }
        }


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
                if(!checkUsrMp()) return;
                if($(this).hasClass("disable")) return;


                //校验手机号，并且发送验证码
                var mobileNo = $("#mobileNo").val();//手机号
                remove_infornmation_alert("mobileNo");
                var url = "${config.getDomainProfile()}/information/verifyNewPhoneNo.do"
                $.ajax({
                    url: url,
                    type: "POST",
                    dataType: 'json',
                    data: 'mobileNo=' + mobileNo,
                    success: function (data) {
                        if (!data.success) {
                            infornmation_alert("error","mobileNo",data.message);
                            return false;
                        } else {

                            e.preventDefault();
                            yzm_link.addClass("disable");
                            var num = 60;
                            reverseTime(num);

                            //异步提交验证请求
                            $("#telCode").removeAttr("disabled");
                            $.ajax({
                                url: "${config.getDomainProfile()}/information/sendForModifyPhone.do",
                                type: "POST",
                                dataType: 'json',
                                data: {
                                    phoneNo: mobileNo,
                                    changeStep:'PhoneTwo'
                                }
                            }).done(function (resp) {

                            });
                        }
                    }
                });

            })
        })(jQuery);

        function checkTelCode() {
            remove_infornmation_alert("telCode");
        }

        /**
         * 检查手机格式
         */
        function submitForm() {
            var mobileNo = $("#mobileNo").val();//手机号
            var telCode = $.trim($("#telCode").val());//短信验证码
            remove_infornmation_alert("telCode");
            remove_infornmation_alert("mobileNo");
            checkUsrMp();
            if (!telCode) {
                infornmation_alert("error","telCode","请填写短信验证码");
                return;
            }
            var url = "${config.getDomainProfile()}/information/verifyNewPhoneNo.do"
            $.ajax({
                url: url,
                type: "POST",
                dataType: 'json',
                data: 'mobileNo=' + mobileNo,
                success: function (data) {
                    if (!data.success) {
                        infornmation_alert("error","mobileNo",data.message);
                        return false;
                    } else {
                        //异步提交验证请求
                        $.ajax({
                            url: "${config.getDomainProfile()}/information/verifyMessageCode.do",
                            type: "POST",
                            dataType: "json",
                            data: {
                                telCode: telCode,
                                changeStep:'PhoneTwo'
                            },
                            success: function (data) {
                                if (data.success) {
                                    $.ajax({
                                        url: "${config.getDomainProfile()}/information/modifyNewPhoneNo.do",
                                        type: "POST",
                                        dataType: "json",
                                        data: {
                                            mobileNo: mobileNo
                                        },
                                        success: function (data) {
                                            if (data.success) {
                                                window.location.href = "${config.getDomainProfile()}/information/changePhoneThree.do";
                                            } else {
                                                alert(data.message);
                                                return;
                                            }
                                        },
                                        error: function (data) {
                                            return;
                                        }
                                    });
                                } else {
                                    alert(data.message);
                                    return;
                                }

                            },
                            error: function (data) {
                                return;
                            }
                        });
                    }
                }
            });
        }

        /**
         * 发送短信验证码
         */

        function rewiSendMessage() {
            var mobileNo = $("#mobileNo").val();//手机号
            if (!checkUsrMp()) {
                return;
            }
            remove_infornmation_alert("mobileNo");
            var url = "${config.getDomainProfile()}/information/verifyNewPhoneNo.do"
            $.ajax({
                url: url,
                type: "POST",
                dataType: 'json',
                data: 'mobileNo=' + mobileNo,
                success: function (data) {
                    if (!data.success) {
                        infornmation_alert("error","mobileNo",data.message);
                        return false;
                    } else {
                        //异步提交验证请求
                        $("#telCode").removeAttr("disabled");
                        $.ajax({
                            url: "${config.getDomainProfile()}/information/sendForModifyPhone.do",
                            type: "POST",
                            dataType: 'json',
                            data: {
                                phoneNo: mobileNo,
                                changeStep:'PhoneTwo'
                            }
                        }).done(function (resp) {

                        });
                    }
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
                $("#sendMessage").html(countNum + "秒后可重新发送");
            } else {
                countNum = 60;
                $("#sendMessage").html("<a href=\"javascript:void(0);\" class=\"sms_but_send\" onclick=\"rewiSendMessage('two','zc');\">获取短信验证码</a>");

            }
        }*/

    </script>
</@page.pc>