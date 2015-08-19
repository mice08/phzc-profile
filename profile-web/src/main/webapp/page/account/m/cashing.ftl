<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p","backUrl":"${config.getDomainProfile()}/m/orders.do","parentName":"提现"} in page>
<@page.pc>
<script src="${config.getDomainMstatic()}/skin/js/custom.js"></script>

    <section class="user_cash no_banner">
    
    <form>
    <div class="warning_form">
        为了您的资金安全，不建议更换银行卡。
    </div>
    <div class="infor_bank">
        <img src="${config.getDomainMstatic()}/skin/images/banks/${bankId}.png">
        <p>
    <front id="bankName"> </front> <br>
            尾号：${cardNoEnd! '****'} 储蓄卡
        </p>
    </div>
    
    <div class="warning_form2">
		可转出余额<font color="red">${acctInfo.acctBal! '0.00'} </font>元
    </div>
    <ul>
        <li>
            <label>
                金额（￥）
            </label>
            <input type="number" id="amount" name="amount" placeholder="请输入金额" class="cash_money" maxlength="7">
        </li>
    <div class="warning_form2">
       单笔限额<font color="red">100万</font>元
    </div>
        
        <li class="normal">
            <a class="submit" href="#">
                提现
            </a>
        </li>
    </ul>
</form>
<script>
var amountNotDoubleMess = "提现金额必须为整数或小数，</br>小数点后不超过2位！";
var amoutMoreThanLimitCash = "提现金额超过限制！";
var noSufficientFunds = "当前余额不足！";

    function validateNumber(value) {
		if(value != null)
			value = $.trim(value);
		var reg =/^[+]?\d+\.?\d{0,2}$/;
		if(reg.test(value) && parseFloat(value) > 0) {
			return true;
		}
		return false;
	}

    $(function(){
        var money_input=$(".cash_money");
        var ext_password=$(".ext_password");
        var ext_password_h5=ext_password.find("h5");
        $("a.submit").click(function(e){
        	var amount = $.trim($("#amount").val());
        	var acctBal = $.trim($("#acctBal").val());
        	if(!validateNumber(amount)) {
				showWarningMessage(amountNotDoubleMess);
				return;
			} else if(parseFloat(amount) > parseFloat(acctBal)) {
				showWarningMessage(noSufficientFunds);
				return;
			}else if(isMoreThanLimitCash(amount)) {
				showWarningMessage(amoutMoreThanLimitCash);
				return;
			}
        	
            e.preventDefault();
            var value=parseFloat(money_input.val());
            var reg = /(-?\d+)(\d{3})/;//千分位
            var result=value* (1).toFixed(2) +"";
            while (reg.test(result)) {
                result = result.replace(reg, "$1,$2");
            }

            if(result=="NaN"){
                result=0;
            }

            ext_password_h5.text(result+"元");
            openExt(ext_password);
            $(".ext_password_input").focus();
            //$("#cashBtn").addClass("disable");
            $("#transPwd").val('');
        });
    })
</script>    </section>
</div>
<div class="ext ext_password">
    <div class="wrapper">
    <h2>
        请输入交易密码
    </h2>
    <h5>
        4,000.00元
    </h5>
    <input type="password" id="transPwd" name="transPwd" class="ext_password_input">
    <div class="link_part">
        <a href="#">
            取消
        </a>
        <a id="cashBtn" href="javascript:submit_cash();"  class="">
            确定
        </a>
    </div>
</div>


<script>
	/**
	$("#cashBtn").mouseout(function(){
		if($("#transPwd").val() != "") {
  			$("#cashBtn").removeClass("disable");
		}
	});
	*/

    (function($){
        $(".link_part a").eq(0).click(function(){
            closeExt($(".ext"))
        })
    })(Zepto);
    
    $(function(){
    	$("#bankName").html(getBankDesc());
    });
    
    function isMoreThanLimitCash(value) {
		var bankId = $("#bankId").val();
		if(!value)
			return true;
		var amount = parseFloat(value);
		return amount > 1000000;
	}
    
    function getBankDesc() {
		var bankId = $("#bankId").val();
		if(bankId == "ICBC")//工行
			return "中国工商银行";
		else if(bankId == "BOC")//中国银行
			return "中国银行";
		else if(bankId == "CCB")//建设
			return "中国建设银行";
		else if(bankId == "PSBC")//邮政
			return "邮政储蓄银行";
		else if(bankId == "CMB")//招商
			return "招商银行";
		else if(bankId == "CIB")//兴业
			return "兴业银行";
		else if(bankId == "CEB")//光大
			return "光大银行";
		else if(bankId == "GDB")//广发
			return "广发银行";
		else if(bankId == "CMBC")//民生
			return "民生银行";
		else if(bankId == "PINGAN")//平安
			return "平安银行";
		else if(bankId == "HXB")//华夏
			return "华夏银行";
		else if(bankId == "SPDB")//浦东发展
			return "浦东发展银行";
		else
		    return "**银行";
		//中信银行，广州农商，广州银行，目前没有bankId  TODO
	}
	
	function showWarningMessage(message) {
		$(".ext_warning p").html(message);
		openExt($(".ext_warning"));
	}
	
	/**
	 * 提交提现请求
	*/
	function submit_cash(){	
		var amount = $.trim($("#amount").val());//提现金额		
		//var smsCode = $.trim($("#smsCode").val());//短信验证码
		var transPwd = $("#transPwd").val();//交易密码
		 if(!validateNumber(amount)) {
			showWarningMessage(amountNotDoubleMess);
			return;
		} else if(isMoreThanLimitCash(amount)) {
			showWarningMessage(amoutMoreThanLimitCash);
			return;
		}else if(!transPwd){
			showWarningMessage("交易密码不能为空！");
			return;
		}

		$("#cashBtn").removeAttr("href");
		closeExt($(".ext_password"));
		addFullLoading();

		$.ajax({
			url:"cashing.do",
			type:"POST",
			dataType:'json',	
			
			data:{
				//"mobileNo": mobileNo, 
				"amount":amount,
				//"smsCode":smsCode,
				"transPwd":transPwd
			},
			success : function(data) {
				showCashResult(data);
				$("#cashBtn").attr("href","javascript:submit_cash();");
				removeLoading();
			},
			error : function(error) {
				showWarningMessage("系统异常!"+"</br>请联系管理员!");
				$("#cashBtn").attr("href","javascript:submit_cash();");
				removeLoading();
			}
	   });
	}
	var sucFlag = false;
	function showCashResult(data) {
		if(data != null) {
			if(data['result'] == true || data['result'] == 'true') {
				if(data['responseDesc'] == 'processing' || data['responseDesc'] == 'success') {
					sucFlag = true;
					showCashResultMessage($('#cashResultMess'),"提现完成，请稍后查询！");
				}
			} else {
				sucFlag = false;
				if(data['responseDesc'] == "tranPwdNotCorrect") {
					openExt($("#transPwdMess"));
				} else {
					showCashResultMessage($("#cashResultMess"),data.msg);
				}
			}
		}
	}
	
	function showCashResultMessage(obj,message) {
		$("#infoBox a:last-of-type").css("float", "none");
		$("#cashResultMess h2").html(message);
		openExt(obj);
	}
	function closeDialog(obj) {
		obj.hide();
		if(sucFlag) {
			window.location.href = "/m/orders.do";
		}
	}
</script>       
 </div>
<div class="logo"> 
</div>

<div id="warnMess" class="ext ext_warning">
 <div class="wrapper">
       <p></p>
</div>
</div>

<div id="transPwdMess" class="ext ext_information">
   <div class="wrapper">
    <h2>
        交易密码不正确
    </h2>
    <div class="link_part">
        <a href="${config.getDomainProfile()}/m/modifyPwd/resetVerify.do">
            忘记密码
        </a>
        <a href="#" class="">
            重新输入
        </a>
    </div>
</div>
<script>
    (function($){
        $("#transPwdMess a").eq(1).click(function(){
            closeExt($("#transPwdMess"));
            openExt($(".ext_password"));
            $("#transPwd").val('');
        })
    })(Zepto);
</script>        
</div>


<div id="cashResultMess" class="ext ext_information">
   <div class="wrapper">
    <h2>
    </h2>
    <div id="infoBox" class="link_part" align="center">
        <a href="javascript:closeDialog($('#cashResultMess'));" align="center">
           确定
        </a>

    </div>
	</div>
</div>

<input type="hidden" id="bankId" name="bankId" value=${bankId} />
<input type="hidden" id="acctBal" name="acctBal" value=${acctInfo.acctBal! '0.00'} />
</@page.pc>