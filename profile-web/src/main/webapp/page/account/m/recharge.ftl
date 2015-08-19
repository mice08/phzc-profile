<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p","backUrl":"${config.getDomainProfile()}/m/orders.do","parentName":"充值"} in page>
<@page.pc>
<script src="${config.getDomainMstatic()}/skin/js/custom.js"></script>

    <section class="user_recharge no_banner">
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
        该卡本次最多可充值<span id="rechargeLimit"></span>元
    </div>
    <ul>
        <li>
            <label>
                金额（￥）
            </label>
            <input type="number" id="amount" name="amount" placeholder="请输入金额" class="recharge_money" maxlength="7">
        </li>
     <li>
            <label>
                验证码
            </label>
            <input type="text" id="smsCode" class="yzm"  placeholder="输入手机验证码" >
            <a href="#" id="sendMessage" class="yzm_link">
                点击发送
            </a>
        </li>
        <li class="normal">
            <a id="rechargeStep1" class="submit" href="#">
                充值
            </a>
        </li>
    </ul>
</form>
<script>
var amountNotEmptyMess = "充值金额不能为空！"; 
var amountNotDoubleMess = "充值金额必须为整数或小数，</br>小数点后不超过2位！";
var amoutMoreThanLimitRecharge = "充值金额超过限制！";
var smsCodeCanNotEmpty = "短信验证码不能为空！";

    $(function(){
       
        $("a.submit").click(function(e){
        	var amount = $.trim($("#amount").val());
        	var smsCode = $.trim($("#smsCode").val());
        	 if(!validateNumber(amount)) {
				showWarningMessage(amountNotDoubleMess);
				return;
			} else if(isMoreThanLimitRecharge(amount)) {
				showWarningMessage(amoutMoreThanLimitRecharge);
				return;
			} else if(!smsCode){
				showWarningMessage(smsCodeCanNotEmpty);
				return;
			}
        	
        	$("#rechargeStep1").removeAttr("href");
        	validateSmsCode();
        	
           
        });
    })
    
	function validateSmsCode() {
		addFullLoading();
		var smsCode = $.trim($("#smsCode").val());
		   $.ajax({
			url:"validateSmsCode.do",
			type:"POST",
			dataType:'json',	
			
			data:{"smsCode":smsCode},
			success : function(data) {
				showValidateSmsResult(data);
				$("#rechargeStep1").attr("href","javascript:submit_recharge();");
				removeLoading();
			},
			error : function(error) {
				showWarningMessage("系统异常!"+"</br>请联系管理员!");
				$("#rechargeStep1").attr("href","javascript:submit_recharge();");
				removeLoading();
			}
	   });
	}
    
  function showValidateSmsResult(data) {
		if(data != null) {
			if(data['result'] == true || data['result'] == 'true') {
				 var money_input=$(".recharge_money");
        		var ext_password=$(".ext_password");
        		var ext_password_h5=ext_password.find("h5");
        		
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
	            
	            //$("#rechargeBtn").addClass("disable");
            	$("#transPwd").val('');
			} else {
				showRechargeResultMessage($("#rechargeMess"),data.msg);
			}
		}
	}

	/*验证码*/
	yzm_link = $(".yzm_link");
	var setInterval_obj = null;
	var setInterval_obj2 = null;
	function reverseTime(num) {
		num -= 1;
		yzm_link.text(num+"秒后重发");
		setInterval_obj = setInterval(function() {
			num -= 1;
			yzm_link.text(num+"秒后重发");
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
	

/**
 * 发送短信验证码
*/
yzm_link.click(function(e) {
	var amount = $.trim($("#amount").val());
	if(!validateNumber(amount)) {
		showWarningMessage(amountNotDoubleMess);
		return;
	} else if(isMoreThanLimitRecharge(amount)) {
		showWarningMessage(amoutMoreThanLimitRecharge);
		return;
	}
	
	if ($(this).hasClass("disable"))
			return;
	e.preventDefault();
	$("#sendMessage").addClass("disable");
	reverseTime(60);
	
	//var mobileNo = $("#mobileNo").val();//手机号
	$("#smsCode").removeAttr("disabled");
	$.ajax({
		url:"sendSmsForRecharge.do",
		type:"GET",
		dataType:'json',
		data:{"transAmt":amount, "refresh" : getRandomNum(999)},
		success : function(data) {
		},
		error : function(error) {
		}
   });
});

	function getRandomNum(n) {
		var random = Math.floor(Math.random()*n+1);
		var b = new Date();
		return b.getTime()+""+random;
	}
</script>  
</section>
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
        <a id="rechargeBtn" href="javascript:submit_recharge();" class="">
            确定
        </a>
    </div>
</div>


<script>
    (function($){
        $(".link_part a").eq(0).click(function(){
            closeExt($(".ext"))
        })
    })(Zepto);
    
    $(function(){
    	$("#bankName").html(getBankDesc());
    	$("#rechargeLimit").html(getRechargeLimit());
    });
    
    function validateNumber(value) {
		if(value != null)
			value = $.trim(value);
		var reg =/^[+]?\d+\.?\d{0,2}$/;
		if(reg.test(value) && parseFloat(value) > 0) {
			return true;
		}
		return false;
	}
    
    function getRechargeLimit() {
		var bankId = $("#bankId").val();
		if(bankId == "ICBC")//工行
			return 50000;
		else if(bankId == "BOC")//中国银行
			return 10000;
		else if(bankId == "CCB")//建设
			return 500000;
		else if(bankId == "PSBC")//邮政
			return 10000;
		else if(bankId == "CMB")//招商
			return 50000;
		else if(bankId == "CIB")//兴业
			return 20000;
		else if(bankId == "CEB")//光大
			return 500000;
		else if(bankId == "GDB")//广发
			return 500000;
		else if(bankId == "CMBC")//民生
			return 500000;
		else if(bankId == "PINGAN")//平安
			return 500000;
		else if(bankId == "HXB")//华夏
			return 1000000;
		else if(bankId == "SPDB")//浦东发展
			return 49999;
		else
		    return 500000;//默认50W
		//中信银行，广州农商，广州银行，目前没有bankId  TODO
	}
    
    function isMoreThanLimitRecharge(value) {
		var bankId = $("#bankId").val();
		if(!value)
			return true;
		var amount = parseFloat(value);
		if(bankId == "ICBC")//工行
			return amount > 50000;
		else if(bankId == "BOC")//中国银行
			return amount > 10000;
		else if(bankId == "CCB")//建设
			return amount > 500000;
		else if(bankId == "PSBC")//邮政
			return amount > 10000;
		else if(bankId == "CMB")//招商
			return amount > 50000;
		else if(bankId == "CIB")//兴业
			return amount > 20000;
		else if(bankId == "CEB")//光大
			return amount > 500000;
		else if(bankId == "GDB")//广发
			return amount > 500000;
		else if(bankId == "CMBC")//民生
			return amount > 500000;
		else if(bankId == "PINGAN")//平安
			return amount > 500000;
		else if(bankId == "HXB")//华夏
			return amount > 1000000;
		else if(bankId == "SPDB")//浦东发展
			return amount > 49999;
		else
		    return amount > 500000;//默认50W
		//中信银行，广州农商，广州银行，目前没有bankId  TODO
		return false;
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
	 * 提交充值请求
	*/
	function submit_recharge(){	
		var amount = $.trim($("#amount").val());//充值金额		
		var smsCode = $.trim($("#smsCode").val());//短信验证码
		var transPwd = $("#transPwd").val();//交易密码
		 if(!validateNumber(amount)) {
			showWarningMessage(amountNotDoubleMess);
			return;
		} else if(isMoreThanLimitRecharge(amount)) {
			showWarningMessage(amoutMoreThanLimitRecharge);
			return;
		}else if(!transPwd){
			showWarningMessage("交易密码不能为空！");
			return;
		}
		
		closeExt($(".ext_password"));
		$("#rechargeBtn").removeAttr("href");
		addFullLoading();
		
		$.ajax({
			url:"recharge.do",
			type:"POST",
			dataType:'json',	
			
			data:{
				//"mobileNo": mobileNo, 
				"transAmt":amount,
				"smsCode":smsCode,
				"transPwd":transPwd
			},
			success : function(data) {
				showRechargeResult(data);
				$("#rechargeBtn").attr("href","javascript:submit_recharge();");
				
			},
			error : function(error) {
				showWarningMessage("系统异常!"+"</br>请联系管理员!");
				$("#rechargeBtn").attr("href","javascript:submit_recharge();");
				removeLoading();
			}
	   });
	}
	var sucFlag = false;
	function showRechargeResult(data) {
		if(data != null) {
			if(data['result'] == true || data['result'] == 'true') {
				if(data['responseDesc'] == 'processing' || data['responseDesc'] == 'success') {
					sucFlag = true;
					showRechargeResultMessage($('#rechargeMess'),"充值完成，请稍后查询！");
					removeLoading();
				}
			} else {
				sucFlag = false;
				if(data['responseDesc'] == "wrongOfTransPwd") {
					openExt($("#transPwdMess"));
					removeLoading();
				} else {
					showRechargeResultMessage($("#rechargeMess"),data.msg);
					removeLoading();
				}
			}
		}
	}
	
	function showRechargeResultMessage(obj) {
		openExt(obj);
	}
	
	function showRechargeResultMessage(obj,message) {
		//$("#recBox a:last-of-type").css("float", "none");
		$("#rechargeMess h2").html(message);
		openExt(obj);
	}
	function closeDialog(obj) {
		obj.hide();
		if(sucFlag) {
			window.location.href = "/m/orders.do"; //TODO
		}
	}
	/**
	$("#rechargeBtn").mouseout(function(){
		if($("#transPwd").val() != "") {
  			$("#rechargeBtn").removeClass("disable");
		}
	});*/
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

<div id="rechargeMess" class="ext ext_information">
   <div class="wrapper">
    <h2>
    </h2>
    <div id="recBox" class="link_part" style="margin-right:8.2rem" >
    
        <a href="javascript:closeDialog($('#rechargeMess'));" align="center">
           确定
        </a>

    </div>
	</div>
</div>

<input type="hidden" id="bankId" name="bankId" value=${bankId} />
</@page.pc>