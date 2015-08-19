<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--[if lt IE 8]> <html class="lt-ie8"><![endif]-->
<!--[if gt IE 8]><!-->
<html>
<!--<![endif]-->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title></title>
<link rel="stylesheet" href="<c:url value='/styles/global.css'/>" />
<link rel="stylesheet" href="<c:url value='/styles/common.css'/>" />
<link rel="stylesheet" href="<c:url value='/styles/home.css'/>" />
<link rel="stylesheet" href="<c:url value='/styles/style1.css'/>" />
<link rel="stylesheet" href="<c:url value='/styles/style2.css'/>" />
<script src="<c:url value='/js/jquery-1.11.2.js'/>"></script>
<script src="<c:url value='/js/jquery.caroufredsel.min.js'/>"></script>
</head>
<body>
	<div class="pagewrapper">
		<div class="top_bar">
			<div class="main">
				<a href="#" class="home">主页</a>
				<p class="left">您好 欢迎来到普惠众筹网！目前网站内测阶段仅对内部员工开放，敬请提出宝贵意见,谢谢！</p>
				<p class="right">
					<span class="login"> <a href="#"> [登录] </a> | <a href="#">
							[免费注册] </a>
					</span> <span class="infor"> <a href="#"> 退出 </a> | <a href="#">
							用户中心 </a>
					</span> <span class="email"> 服务邮箱：pub_zc@pingan.com.cn </span>
				</p>
			</div>
		</div>
		<div class="header">
			<div class="main">
				<img src="<c:url value='/images/logo.png' />" class="logo">
				<ul>
					<li><a href="#" class=""> 首页 </a></li>
					<li><a href="#" class=""> 众筹项目 </a></li>
					<li><a href="#" class=""> 发起众筹 </a></li>
					<li><a href="#" class=""> 帮助中心 </a></li>
					<li><a href="#" class=""> 筹吧 </a> <em> new </em></li>
				</ul>
			</div>
		</div>
		<div class="bodyer">
			<div class="user_page">
				<div class="main clearfix">
					<div class="left">
						<div class="user_nav">
							<div class="user_infor">
								<div class="img_part">
									<img src="<c:url value='/images/head.jpg' />">
									<div class="cover"></div>
								</div>
								<h3>Seagoo 22</h3>
							</div>
							<ul>
								<li class="item1 "><a href="user_page1-1.html" class=" ">
										我的众筹 </a> <span></span></li>
								<li class="item2 "><a href="user_page2-1.html" class=" ">
										我的认证 </a> <span></span></li>
								<li class="item3 active"><a href="user_page3-1.html"
									class=" active"> 我的银行卡 </a> <span></span></li>
								<li class="item4 "><a href="user_page4.html" class=" ">
										充值提现 </a> <span></span></li>
								<li class="item5 "><a href="user_page5-1.html" class=" ">
										我的资料 </a> <span></span></li>
								<li class="item6 "><a href="user_page6-1.html" class=" ">
										收货地址 </a> <span></span></li>
								<li class="item7 "><a href="user_page7.html" class=" ">
										我的吐槽 </a> <span></span></li>
								<li class="item8 "><a href="user_page8-1.html" class=" ">
										修改密码 </a> <span></span></li>
							</ul>
						</div>
					</div>
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
											(将向你的手机<c:out value="${confusedMobile}"></c:out>发送短信验证码) </span>
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
												console.log("error data: ", error);
											},
											dataType : "json"
										});							        
							        
							        })
							    })(jQuery);
							
							</script>
						</div>
					</div>
				</div>
			</div>

			<!--[if IE 6]>
<script src="skin/js/png_fix.js"></script>
<script>
    DD_belatedPNG.fix('.img_part .cover');
</script>
<![endif]-->
		</div>
		<div class="footer">
			<div class="main">
				<div class="top">
					<p class="bottom_menu">
						<a href="#"> 关于我们 </a> | <a href="#"> 人才招聘 </a> | <a href="#">
							联系我们 </a> <br> <a href="#"> 用户协议 </a> | <a href="#"> 友情链接 </a> |
						<a href="#"> 网站地图 </a>
					</p>
					<ul class="bottom_infor">
						<li class="item1">服务邮箱<br> pub_zc@pingan.com.cn
						</li>
						<li class="item2">服务邮箱<br> pub_zc@pingan.com.cn
						</li>
					</ul>
				</div>
				<div class="bottom">
					<div class="bottom_links">
						<a href="#" class="open"> 平安集团旗下网站 </a>
						<ul>
							<li><a href="http://www.pingan.com" target="_blank">中国平安官网</a></li>
							<li><a href="http://one.pingan.com" target="_blank">平安一账通</a></li>
							<li><a href="http://www.lufax.com" target="_blank">陆金所</a></li>
							<li><a href="http://www.xsme.cn" target="_blank">平安交易所</a></li>
						</ul>
					</div>
				</div>
				<p>版权所有 ©2015 深圳前海普惠众筹交易股份有限公司 粤ICP备15042952号</p>
			</div>
		</div>
	</div>
	<div class="fix_function">
		<a href="#"> 意见<br> 反馈
		</a> <a class="last" href="#"> 返回 </a>
	</div>
	<div class="bgcover"></div>
	<script src="<c:url value='/js/custom.js' />"></script>
</body>

<script>

function md5(string){
    function md5_RotateLeft(lValue, iShiftBits) {
        return (lValue<<iShiftBits) | (lValue>>>(32-iShiftBits));
    }
    function md5_AddUnsigned(lX,lY){
        var lX4,lY4,lX8,lY8,lResult;
        lX8 = (lX & 0x80000000);
        lY8 = (lY & 0x80000000);
        lX4 = (lX & 0x40000000);
        lY4 = (lY & 0x40000000);
        lResult = (lX & 0x3FFFFFFF)+(lY & 0x3FFFFFFF);
        if (lX4 & lY4) {
            return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
        }
        if (lX4 | lY4) {
            if (lResult & 0x40000000) {
                return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
            } else {
                return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
            }
        } else {
            return (lResult ^ lX8 ^ lY8);
        }
    }
    function md5_F(x,y,z){
        return (x & y) | ((~x) & z);
    }
    function md5_G(x,y,z){
        return (x & z) | (y & (~z));
    }
    function md5_H(x,y,z){
        return (x ^ y ^ z);
    }
    function md5_I(x,y,z){
        return (y ^ (x | (~z)));
    }
    function md5_FF(a,b,c,d,x,s,ac){
        a = md5_AddUnsigned(a, md5_AddUnsigned(md5_AddUnsigned(md5_F(b, c, d), x), ac));
        return md5_AddUnsigned(md5_RotateLeft(a, s), b);
    };
    function md5_GG(a,b,c,d,x,s,ac){
        a = md5_AddUnsigned(a, md5_AddUnsigned(md5_AddUnsigned(md5_G(b, c, d), x), ac));
        return md5_AddUnsigned(md5_RotateLeft(a, s), b);
    };
    function md5_HH(a,b,c,d,x,s,ac){
        a = md5_AddUnsigned(a, md5_AddUnsigned(md5_AddUnsigned(md5_H(b, c, d), x), ac));
        return md5_AddUnsigned(md5_RotateLeft(a, s), b);
    };
    function md5_II(a,b,c,d,x,s,ac){
        a = md5_AddUnsigned(a, md5_AddUnsigned(md5_AddUnsigned(md5_I(b, c, d), x), ac));
        return md5_AddUnsigned(md5_RotateLeft(a, s), b);
    };
    function md5_ConvertToWordArray(string) {
        var lWordCount;
        var lMessageLength = string.length;
        var lNumberOfWords_temp1=lMessageLength + 8;
        var lNumberOfWords_temp2=(lNumberOfWords_temp1-(lNumberOfWords_temp1 % 64))/64;
        var lNumberOfWords = (lNumberOfWords_temp2+1)*16;
        var lWordArray=Array(lNumberOfWords-1);
        var lBytePosition = 0;
        var lByteCount = 0;
        while ( lByteCount < lMessageLength ) {
            lWordCount = (lByteCount-(lByteCount % 4))/4;
            lBytePosition = (lByteCount % 4)*8;
            lWordArray[lWordCount] = (lWordArray[lWordCount] | (string.charCodeAt(lByteCount)<<lBytePosition));
            lByteCount++;
        }
        lWordCount = (lByteCount-(lByteCount % 4))/4;
        lBytePosition = (lByteCount % 4)*8;
        lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80<<lBytePosition);
        lWordArray[lNumberOfWords-2] = lMessageLength<<3;
        lWordArray[lNumberOfWords-1] = lMessageLength>>>29;
        return lWordArray;
    };
    function md5_WordToHex(lValue){
        var WordToHexValue="",WordToHexValue_temp="",lByte,lCount;
        for(lCount = 0;lCount<=3;lCount++){
            lByte = (lValue>>>(lCount*8)) & 255;
            WordToHexValue_temp = "0" + lByte.toString(16);
            WordToHexValue = WordToHexValue + WordToHexValue_temp.substr(WordToHexValue_temp.length-2,2);
        }
        return WordToHexValue;
    };
    function md5_Utf8Encode(string){
        string = string.replace(/\r\n/g,"\n");
        var utftext = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                utftext += String.fromCharCode(c);
            }else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            } else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }
        }
        return utftext;
    };
    var x=Array();
    var k,AA,BB,CC,DD,a,b,c,d;
    var S11=7, S12=12, S13=17, S14=22;
    var S21=5, S22=9 , S23=14, S24=20;
    var S31=4, S32=11, S33=16, S34=23;
    var S41=6, S42=10, S43=15, S44=21;
    string = md5_Utf8Encode(string);
    x = md5_ConvertToWordArray(string);
    a = 0x67452301; b = 0xEFCDAB89; c = 0x98BADCFE; d = 0x10325476;
    for (k=0;k<x.length;k+=16) {
        AA=a; BB=b; CC=c; DD=d;
        a=md5_FF(a,b,c,d,x[k+0], S11,0xD76AA478);
        d=md5_FF(d,a,b,c,x[k+1], S12,0xE8C7B756);
        c=md5_FF(c,d,a,b,x[k+2], S13,0x242070DB);
        b=md5_FF(b,c,d,a,x[k+3], S14,0xC1BDCEEE);
        a=md5_FF(a,b,c,d,x[k+4], S11,0xF57C0FAF);
        d=md5_FF(d,a,b,c,x[k+5], S12,0x4787C62A);
        c=md5_FF(c,d,a,b,x[k+6], S13,0xA8304613);
        b=md5_FF(b,c,d,a,x[k+7], S14,0xFD469501);
        a=md5_FF(a,b,c,d,x[k+8], S11,0x698098D8);
        d=md5_FF(d,a,b,c,x[k+9], S12,0x8B44F7AF);
        c=md5_FF(c,d,a,b,x[k+10],S13,0xFFFF5BB1);
        b=md5_FF(b,c,d,a,x[k+11],S14,0x895CD7BE);
        a=md5_FF(a,b,c,d,x[k+12],S11,0x6B901122);
        d=md5_FF(d,a,b,c,x[k+13],S12,0xFD987193);
        c=md5_FF(c,d,a,b,x[k+14],S13,0xA679438E);
        b=md5_FF(b,c,d,a,x[k+15],S14,0x49B40821);
        a=md5_GG(a,b,c,d,x[k+1], S21,0xF61E2562);
        d=md5_GG(d,a,b,c,x[k+6], S22,0xC040B340);
        c=md5_GG(c,d,a,b,x[k+11],S23,0x265E5A51);
        b=md5_GG(b,c,d,a,x[k+0], S24,0xE9B6C7AA);
        a=md5_GG(a,b,c,d,x[k+5], S21,0xD62F105D);
        d=md5_GG(d,a,b,c,x[k+10],S22,0x2441453);
        c=md5_GG(c,d,a,b,x[k+15],S23,0xD8A1E681);
        b=md5_GG(b,c,d,a,x[k+4], S24,0xE7D3FBC8);
        a=md5_GG(a,b,c,d,x[k+9], S21,0x21E1CDE6);
        d=md5_GG(d,a,b,c,x[k+14],S22,0xC33707D6);
        c=md5_GG(c,d,a,b,x[k+3], S23,0xF4D50D87);
        b=md5_GG(b,c,d,a,x[k+8], S24,0x455A14ED);
        a=md5_GG(a,b,c,d,x[k+13],S21,0xA9E3E905);
        d=md5_GG(d,a,b,c,x[k+2], S22,0xFCEFA3F8);
        c=md5_GG(c,d,a,b,x[k+7], S23,0x676F02D9);
        b=md5_GG(b,c,d,a,x[k+12],S24,0x8D2A4C8A);
        a=md5_HH(a,b,c,d,x[k+5], S31,0xFFFA3942);
        d=md5_HH(d,a,b,c,x[k+8], S32,0x8771F681);
        c=md5_HH(c,d,a,b,x[k+11],S33,0x6D9D6122);
        b=md5_HH(b,c,d,a,x[k+14],S34,0xFDE5380C);
        a=md5_HH(a,b,c,d,x[k+1], S31,0xA4BEEA44);
        d=md5_HH(d,a,b,c,x[k+4], S32,0x4BDECFA9);
        c=md5_HH(c,d,a,b,x[k+7], S33,0xF6BB4B60);
        b=md5_HH(b,c,d,a,x[k+10],S34,0xBEBFBC70);
        a=md5_HH(a,b,c,d,x[k+13],S31,0x289B7EC6);
        d=md5_HH(d,a,b,c,x[k+0], S32,0xEAA127FA);
        c=md5_HH(c,d,a,b,x[k+3], S33,0xD4EF3085);
        b=md5_HH(b,c,d,a,x[k+6], S34,0x4881D05);
        a=md5_HH(a,b,c,d,x[k+9], S31,0xD9D4D039);
        d=md5_HH(d,a,b,c,x[k+12],S32,0xE6DB99E5);
        c=md5_HH(c,d,a,b,x[k+15],S33,0x1FA27CF8);
        b=md5_HH(b,c,d,a,x[k+2], S34,0xC4AC5665);
        a=md5_II(a,b,c,d,x[k+0], S41,0xF4292244);
        d=md5_II(d,a,b,c,x[k+7], S42,0x432AFF97);
        c=md5_II(c,d,a,b,x[k+14],S43,0xAB9423A7);
        b=md5_II(b,c,d,a,x[k+5], S44,0xFC93A039);
        a=md5_II(a,b,c,d,x[k+12],S41,0x655B59C3);
        d=md5_II(d,a,b,c,x[k+3], S42,0x8F0CCC92);
        c=md5_II(c,d,a,b,x[k+10],S43,0xFFEFF47D);
        b=md5_II(b,c,d,a,x[k+1], S44,0x85845DD1);
        a=md5_II(a,b,c,d,x[k+8], S41,0x6FA87E4F);
        d=md5_II(d,a,b,c,x[k+15],S42,0xFE2CE6E0);
        c=md5_II(c,d,a,b,x[k+6], S43,0xA3014314);
        b=md5_II(b,c,d,a,x[k+13],S44,0x4E0811A1);
        a=md5_II(a,b,c,d,x[k+4], S41,0xF7537E82);
        d=md5_II(d,a,b,c,x[k+11],S42,0xBD3AF235);
        c=md5_II(c,d,a,b,x[k+2], S43,0x2AD7D2BB);
        b=md5_II(b,c,d,a,x[k+9], S44,0xEB86D391);
        a=md5_AddUnsigned(a,AA);
        b=md5_AddUnsigned(b,BB);
        c=md5_AddUnsigned(c,CC);
        d=md5_AddUnsigned(d,DD);
    }
    return (md5_WordToHex(a)+md5_WordToHex(b)+md5_WordToHex(c)+md5_WordToHex(d)).toLowerCase();
};

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
			"transPwd" : md5(transPwd),
			"smsVerifyCode" : smsVerifyCode
		};

		$.ajax({
			type : 'POST',
			url : action,
			data : data,
			success : function(data) {
				console.log("ret: ", data);
				if (data.success === true) {
					window.location.href = "/card/myCards.do";
				}
			},
			error : function(error) {
				console.log("error data: ", error);
			},
			dataType : "json"
		});

	});

});

</script>

</html>