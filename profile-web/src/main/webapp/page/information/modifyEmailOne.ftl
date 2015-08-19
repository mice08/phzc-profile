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
                身份认证
            </b>
        </span>
                </li>
                <li class="item2 ">
        <span class="">
            <i>
                2
            </i>
            <b>
                设置新邮箱地址
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
                        请输入当前邮箱：
                    </label>

                    <div class="input_part">
                        <input type="text" id="usrEmail" name="usrEmail" onblur="checkUsrEm()"/></p>
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
</div>
<script type="text/javascript">


    function IsEmail(usrEmail) {

        if (!(/^([a-za-z0-9_.-])+@(([a-za-z0-9-])+.)+([a-za-z0-9]{2,4})+$/.test(usrEmail))) {
            return false;
        }
        else {
            return true;
        }
    }


    function checkUsrEm() {
        var usrEmail = $("#usrEmail").val();
        remove_infornmation_alert("usrEmail");
        if (!usrEmail) {
            infornmation_alert("error","usrEmail","邮箱号不能为空");
        } else if (!IsEmail(usrEmail)) {
            infornmation_alert("error","usrEmail","邮箱号格式不正确");
        }
    }


    /*
     * 提交
     */
    function submitForm() {
        var usrEmail = $("#usrEmail").val();
        remove_infornmation_alert("usrEmail");
        if (!usrEmail) {
            infornmation_alert("error","usrEmail","邮箱号不能为空");
            return;
        } else if (!IsEmail(usrEmail)) {
            infornmation_alert("error","usrEmail","邮箱号格式不正确");
            return;
        }
        $("#usrMpMsg").hide();
        //异步提交验证请求
        $.ajax({
            url: "${config.getDomainProfile()}/information/verifyCurrentEmail.do",
            type: "POST",
            dataType: "json",
            data: {
                currentEmail: usrEmail
            },
            success: function (data) {
                if (data.success == true) {
                    window.location.href = "${config.getDomainProfile()}/information/changeEmailTwo.do";
                } else {
                    if (data.message == 'wrong') {
                        alert("邮箱验证失败！");
                    } else {
                        alert(data.message);
                    }
                    return;
                }
            },
            error: function (data) {
                return;
            }
        });
    }

</script>
</@page.pc>