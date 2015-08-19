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
                身份认证
            </b>
        </span>
                </li>
                <li class="item2 active">
        <span class="active">
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
                        请输入新邮箱：
                    </label>

                    <div class="input_part">
                        <input type="text" id="usrEmail" name="usrEmail" onblur="checkUsrEm()"/></p>
                    </div>
                    <div class="" id="usrEmMsg"></div>
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
        if (!usrEmail) {
            $("#usrEmMsg").show();
            $("#usrEmMsg").html("&nbsp;&nbsp;邮箱号不能为空！");
        } else if (!IsEmail(usrEmail)) {
            $("#usrEmMsg").show();
            $("#usrEmMsg").html(" &nbsp;&nbsp;邮箱号格式不正确！");
        } else {
            $("#usrEmMsg").hide();
        }
    }


    /*
     * 提交
     */
    function submitForm() {
        var usrEmail = $("#usrEmail").val();
        if (!usrEmail) {
            $("#usrEmMsg").show();
            $("#usrEmMsg").html(" &nbsp;&nbsp;邮箱号不能为空！");
            return;
        } else if (!IsEmail(usrEmail)) {
            $("#usrEmMsg").show();
            $("#usrEmMsg").html(" &nbsp;&nbsp;邮箱号格式不正确！");
            return;
        }
        $("#usrMpMsg").hide();
        //异步提交验证请求
        $.ajax({
            url: "${config.getDomainProfile()}/information/changeNewEmail.do",
            type: "POST",
            dataType: "json",
            data: {
                newEmail: usrEmail
            },
            success: function (data) {
                if (data.success) {
                    window.location.href = "${config.getDomainProfile()}/information/changeEmailThree.do";
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

</script>
</@page.pc>