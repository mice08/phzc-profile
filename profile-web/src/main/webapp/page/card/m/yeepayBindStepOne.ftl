<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p", "parentName":"添加银行卡"} in page>
<@page.pc>

        <section class="payment_addcard1 no_banner">
            <form id="bindStepOneForm" class="form_order" action="bindStepOne.do">
                <div class="warning_form">为了您的资金安全，银行卡持卡人需与实名认证姓名一致。</div>
                <ul class="clearfix">
                    <li>
                        <label>持卡人</label>
                        <span>${realName}</span>
                    </li>
                    <li>
                        <label>银行卡号</label>
                        <input type="text" class="username" placeholder="输入银行卡号" name="cardId">
                    </li>
                    <li>
                        <label>卡类型</label>
                        <div class="bank_infor">
                        </div>
                    </li>
                    <li class="sp3">
                        <label>手机号</label>
                        <input type="number" class="mobile" placeholder="请输入银行预留手机号" name="mobile">
                    </li>
                    <li class="normal">
                        <a class="submit" onclick="next();">下一步</a>
                    </li>
                </ul>
            </form>
        </section>
    </div>

<script type="text/javascript">

(function($) {

    var quotaMap = {
        "CMBC": {"quotaOnce": 5, "quotaOneDay": 50, "quotaOneYear": 100},
        "GDB": {"quotaOnce": 5, "quotaOneDay": 50, "quotaOneYear": 100},
        "PINGAN": {"quotaOnce": 50, "quotaOneDay": 50, "quotaOneYear": 100},
        "CMB": {"quotaOnce": 5, "quotaOneDay": 20, "quotaOneYear": 100},
        "PSBC": {"quotaOnce": 5, "quotaOneDay": 50, "quotaOneYear": 100},
        "ICBC": {"quotaOnce": 5, "quotaOneDay": 5, "quotaOneYear": 100},
        "SPDB": {"quotaOnce": 5, "quotaOneDay": 50, "quotaOneYear": 100},
        "CIB": {"quotaOnce": 5, "quotaOneDay": 50, "quotaOneYear": 100},
        "BOC": {"quotaOnce": 5, "quotaOneDay": 50, "quotaOneYear": 100},
        "CEB": {"quotaOnce": 5, "quotaOneDay": 5, "quotaOneYear": 100},
        "BCM": {"quotaOnce": 50, "quotaOneDay": 50, "quotaOneYear": 100},
        "CCB": {"quotaOnce": 50, "quotaOneDay": 50, "quotaOneYear": 100},
        "HXB": {"quotaOnce": 5, "quotaOneDay": 50, "quotaOneYear": 100},
        "ABC": {"quotaOnce": 5, "quotaOneDay": 50, "quotaOneYear": 100},
        "CITIC": {"quotaOnce": 5, "quotaOneDay": 50, "quotaOneYear": 100}
    }

    $("[name=cardId]").blur(function() {
        var cardId = this.value;
        if($.trim(cardId) === "") {
            warning_obj_inner.text("请输入银行卡号");
            openExt(warning_obj, true);
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
                    var bankId = data.bankInfo.bankId;
                    var quotaOnce = quotaMap[bankId].quotaOnce;
                    var quotaOneDay = quotaMap[bankId].quotaOneDay;
                    var quotaOneYear = quotaMap[bankId].quotaOneYear;
                    console.log(bankId, data.bankInfo.bankNm, quotaOnce, quotaOneDay, quotaOneYear);

                    var html = "<img src='${config.getDomainMstatic()}/skin/images/banks/" + bankId + ".png'>"
                    +"<p>"
                    +   "<span bankId=" + bankId + ">" + data.bankInfo.bankNm + " </span><br>"
                    +   "限额    " + quotaOnce + "万／笔   " + quotaOneDay + "万／日"
                    +"</p>";
                    $('.bank_infor').html(html);
                } else {
                    $('.bank_infor').html("暂不支持该银行，请更换其他银行卡");
                }
            },
            error : function(error) {
            },
            dataType : "json"
        });
    });

    $("[name=mobile]").blur(function() {
        var mobile = this.value;
        if (!checkUserName_phone(mobile)) {
            warning_obj_inner.text(warning);
            openExt(warning_obj, true);
            return false;
        }
    });

})(Zepto);
    
function next() {
    var bankName = $(".bank_infor span").text();
    bankName = $.trim(bankName);
    var bankId = $(".bank_infor span").attr("bankId");
    // if (bankName === "") {
    //     warning_obj_inner.text("请选择银行");
    //     openExt(warning_obj, true);
    //     return false;
    // }

    var cardId = $("[name=cardId]").val();
    if ($.trim(cardId) === "") {
        warning_obj_inner.text("请输入银行卡号");
        openExt(warning_obj, true);
        return false;
    }

    var mobile = $("[name=mobile]").val()
    if (!checkUserName_phone(mobile)) {
        warning_obj_inner.text(warning);
        openExt(warning_obj, true);
        return false;
    }

    var formData = {
        certStatus: 2,
        cardId: cardId,
        bankName: bankName,
        mobile: mobile
    }

    addFullLoading();

    var action = $("#bindStepOneForm").attr("action");
    $.ajax({
        type: 'GET',
        url: action,
        data: formData,
        dataType: 'json',  //使用jsonp实现跨域跳转
        // jsonp: 'jsoncallback',
        success: function(data) {
            console.log("ret: ", data);
            removeLoading();
            // if (data.code !== undefined && data.login !== undefined && data.code === 403) {
            //     window.location.href = data.login;
            //     return;
            // }
            if (data.success === true) {
                window.location.href = "bindStepTwoForm.do?"//certStatus=2&cardId=" + cardId + "&bankId=" + bankId + "&bankName=" + encodeURI(bankName) + "&mobile=" + mobile
                    + "source=" + encodeURI("${source!''}");
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

    <div class="ext ext_warning">
        <div class="wrapper">用户名、手机号或密码有误</div>
    </div>

    <div class="logo"></div>

</@page.pc>