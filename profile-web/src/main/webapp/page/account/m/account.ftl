        <div class="top">
        	<a href="/m/userInfo.do"><h3 id="userName">用户中心</h3></a>
		    <a href="/m/userInfo.do"><img src="${config.getDomainMstatic()}/skin/images/head.jpg"
		     class="head"> 
		     </a>
		     
		    <a href="#">
		    <h1 id="acctBal">
		    	0.00
		    </h1>
		    <h2>
		        资金账户（元）
		    </h2>
		    </a>
		    <div class="link_part">
		        <a href="/m/account/initRecharge.do">
		            充值
		        </a>
		  	<a href="/m/account/initCash.do">
		            提现
		        </a>
		    </div>
		</div>
		
		
<script>

	   function getcookie(name) {
				var cookie_start = document.cookie.indexOf(name);
				var cookie_end = document.cookie.indexOf(";", cookie_start);
				return cookie_start == -1 ? '' : unescape(document.cookie.substring(cookie_start + name.length + 1, (cookie_end > cookie_start ? cookie_end : document.cookie.length)));		
	   } 
			         
	   var COOKIE_NAME = 'userName';  
	   if(getcookie(COOKIE_NAME)){  
			//$("#userName").html( getcookie(COOKIE_NAME) );
	   }
	



	function navigateAccountLog(){
		var url = "/m/account/accountLog.do?startDate=";
		url += getBeforeDate(15) + "&endDate=" 
		      + getBeforeDate(0) + "&currentPage=1&pageSize=20";
		window.location.href = url;
	}

    function getBeforeDate(n){
        var n = n;
        var d = new Date();
        var year = d.getFullYear();
        var mon=d.getMonth()+1;
        var day=d.getDate();
        if(day <= n){
                if(mon>1) {
                   mon=mon-1;
                }
               else {
                 year = year-1;
                 mon = 12;
                 }
               }
              d.setDate(d.getDate()-n);
              year = d.getFullYear();
              mon=d.getMonth()+1;
              day=d.getDate();
         s = year+(mon<10?('0'+mon):mon)+(day<10?('0'+day):day);
         return s;
    }

	$(document).ready(function(){
	  	$.ajax({
			url:"/m/account/myAccount.do",
			type:"GET",
			dataType:'json',	
			data:{"refresh" : getRandomNum(999)},
			success : function(data) {
				if(data != null && data['result'].length >0 ) {
					$("#acctBal").html(data['result']); 
				}else {
					$("#acctBal").html("0.00");
				}
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
