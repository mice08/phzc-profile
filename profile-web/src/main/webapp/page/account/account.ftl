<#import "../share/pc/layout.ftl" as page>
<@page.pc>

	<div class="user_page">
	<div class="main clearfix">

		<#include "../leftNav.ftl"> <@leftNav activeMenu="item4"> </@leftNav>

		<!-- add right content -->
		<div class="right">
			<div class="user_page_inner  user_page4-1">

				<div class="user_page4_hd clearfix">
					<div class='user_page4_hd_p'>
						<p class='user_page4_hd_balance'>账户余额</p>
						<p class='user_page4_hd_num1'>

							${acctInfo.acctBal! '0.00'} <span class='user_page4_hd_chief'>元</span>
						</p>
					</div>

					<div class='user_page4_hd_p'>
						<p class='user_page4_cash'>冻结余额</p>
						<p class='user_page4_hd_num3'>
							${acctInfo.frzBal! '0.00'} <span class='user_page4_hd_chief'>元</span>
						</p>
					</div>

					<div class='user_page4_hd_p'>
						<p class='user_page4_cash'>可用余额</p>
						<p class='user_page4_hd_num3'>
							${acctInfo.avlBal! '0.00'} <span class='user_page4_hd_chief'>元</span>
						</p>
					</div>

					<div class='user_page4_hd_pr'>
						<a id="rechargeAct" href="initRecharge.do">充值</a> 
						<a id="cashAct" class='user_page4_hd_funds' href="initCash.do">提现</a>
					</div>

				</div>

				<div class="user_page4_ce">

					<div class="user_page4_ce_a">
						<a id="rechargeRec"
							href="javascript:switchRechargeAndCashing('R');" class="active">
							充值记录 </a> <a id="cashingRec" class='user_page4_ce_ad'
							href="javascript:switchRechargeAndCashing('C');"> 提现记录 </a>
					</div>
					<div class="user_page4_ce_time">
						<span>时间</span>
						<div class="user_page4_ce_inp">
							<input name="startDate" id="startDate" class="timepick"
								type="text" value=''> <a href=""
								class='user_page4_ce_jorte'></a>
						</div>
					</div>
					<span>一</span>
					<div class="user_page4_ce_time">
						<div class="user_page4_ce_inp">
							<input name="endDate" id="endDate" class="timepick" type="text"
								value=''> <a href="" class='user_page4_ce_jorte'></a>
						</div>

					</div>
					<span>交易状态</span>
					<div class="select_part">
						<a id="transStat" class="select_part_main" href="#"
							data-value=""> 全部 </a>
						<ul>
							<li><a href="#" data-value=""> 全部 </a></li>
							<li><a href="#" data-value="S"> 成功 </a></li>
							<li><a href="#" data-value="P"> 处理中 </a></li>
							<li><a href="#" data-value="F"> 失败 </a></li>
						</ul>
					</div>
					<a id="searchRec" href="javascript:getRecords();"
						class='user_page4_ce_sear'>查询</a>
				</div>

				<table class="user_page4_ft">
					<thead>
						<tr>
							<th class='user_page4_ft_tim'>时间</th>
							<th>金额</th>
							<th>交易类型</th>
							<th>交易状态</th>
						</tr>
					</thead>
					<div>
						<tbody id="rechargeOrCashRecord">

						</tbody>
					</div>
				</table>


				<div class="pager">
					<div class="pager_inner"></div>
				</div>


			</div>
		</div>
	</div>
</div>

	<script>
    /*date-pick*/
    (function($){
        $(function(){
            $('input.timepick').manhuaDate({
                Event : "click",//可选
                Left : 0,//弹出时间停靠的左边位置
                Top : -16,//弹出时间停靠的顶部边位置
                fuhao : "-",//日期连接符默认为-
                isTime : false,//是否开启时间值默认为false
                beginY : 1949,//年份的开始默认为1949
                endY :2100//年份的结束默认为2049
            });
        });
        $(".user_page4_ce_jorte").click(function(e){
            e.preventDefault();
            $(this).parents(".user_page4_ce_inp").find("input").trigger("click")
        })
    })(jQuery);
    
    $(function(){
    	$("#startDate").val($("#_startDate").val());
		$("#endDate").val($("#_endDate").val());
		$("#transStatSelect").val($("#_transStat").val());
		  //日期控件赋值
        if($("#startDate").val() == ""){
            $("#startDate").val(getBeforeDate(7));
        }
        if($("#endDate").val() == ""){
        	$("#endDate").val(getBeforeDate(0));
        }
    });
    
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
         s = year+"-"+(mon<10?('0'+mon):mon)+"-"+(day<10?('0'+day):day);
         return s;
    }
    
    
    function switchRechargeAndCashing(action) {
    	//清空原有记录
    	if(action != $("#currentAction").val) {
    		$("#rechargeOrCashRecord").children().remove();
    		$(".pager .pager_inner").wly_pager_bulder("destory");
    		$("#currentPage").val(1);
		}
    	if(action == "R") {//充值
    		activeRechargeButton();
    	} else {//提现
    		activeCashButton();
    	}
    }
    
    function activeRechargeButton() {
    	$("#currentAction").val("R");
    	$("#rechargeRec").removeClass("user_page4_ce_ad");
    	$("#rechargeRec").addClass("active");
    	$("#cashingRec").removeClass("active");
    	$("#cashingRec").addClass("user_page4_ce_ad");
    	
    	$("#rechargeAct").removeClass("user_page4_hd_funds");
    	$("#cashAct").addClass("user_page4_hd_funds");
    }
    
    function activeCashButton() {
    	$("#currentAction").val("C");
    	$("#rechargeRec").removeClass("active");
    	$("#rechargeRec").addClass("user_page4_ce_ad");
    	$("#cashingRec").removeClass("user_page4_ce_ad");
    	$("#cashingRec").addClass("active");
    	
    	$("#rechargeAct").addClass("user_page4_hd_funds");
    	$("#cashAct").removeClass("user_page4_hd_funds");
    }
    
    var pageItems = 5;//分页时展现几个页码
	var pageSize = 20;//默认显示20条
    
	function getRecords() {
		var rechargeRec = $("#rechargeRec").attr("class");
		var transStat = $("#transStat").attr("data-value");
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		
		$("#searchRec").removeAttr("href");
		if(rechargeRec == "active") {//充值
			var data = {
					"merId" : "site",
					"cmdId" : "queryRechargeLog",
					"transStat" : transStat,
					"startDate" : startDate,
					"endDate" : endDate,
					"version" : "10",
					"currentPage" : $("#currentPage").val(),
					"pageSize" : pageSize,
					"refresh" : getRandomNum(999)
				};

				$.ajax({
					type : 'GET',
					url : "rechargeRecord.do",
					data : data,
					dataType : "json",
					success : function(data) {
						createRechargeOrCashRecordHtml(data,"recharge");
						$("#searchRec").attr("href","javascript:getRecords();");
					},
					error : function(error) {
						$("#searchRec").attr("href","javascript:getRecords();");
					},
					dataType : "json"
				});
		} else {
			
			//提现
			var data = {
					"transStat" : transStat,
					"startDate" : startDate,
					"endDate" : endDate,
					"currentPage" : "1", //TODO
					"pageSize" : "100",
					"refresh" : getRandomNum(999)
					
				};

				$.ajax({
					type : 'GET',
					url : "cashRecord.do",
					data : data,
					dataType : "json",
					success : function(data) {
						createRechargeOrCashRecordHtml(data,"cash");
						$("#searchRec").attr("href","javascript:getRecords();");
					},
					error : function(error) {
						$("#searchRec").attr("href","javascript:getRecords();");
					},
					dataType : "json"
				});
			
			
		}
	}
	
	function getRandomNum(n) {
		var random = Math.floor(Math.random()*n+1);
		var b = new Date();
		return b.getTime()+""+random;
	}
	
	function getTradeStatus(transStat) {
		if(transStat == "P") {
			return "处理中";
		} else if(transStat == "S") {
			return "成功";
		} else if(transStat == "F") {
			return "失败";
		} else if(transStat == "I") {
			return "审核中";
		} else if(transStat == "H") {
			return "审核中";
		} else if(transStat == "H") {
			return "审核失败";
		} else {
			return "";
		}
		
	}
	
	$(function(){
		var rechargeInfoStr = $("#rechargeRecord").val();
		var cashRecordStr = $("#cashRecord").val();
		if(rechargeInfoStr && rechargeInfoStr.length > 0) {
			var recJson = $.parseJSON(rechargeInfoStr);
			createRechargeOrCashRecordHtml(recJson, "recharge");
			activeRechargeButton();
			$("#rechargeRecord").val('');
		}else if(cashRecordStr && cashRecordStr.length > 0) {
			var cashJson = $.parseJSON(cashRecordStr);
			createRechargeOrCashRecordHtml(cashJson, "cash");
			activeCashButton();
			$("#cashRecord").val('');
		}
		
	});
	
	function formatDate(date) {
		if(!date || date.length != 14) {
			return "时间格式不正确";
		}
		var year = date.substring(0,4);
		var month = date.substring(4,6);
		var day = date.substring(6,8);
		var hour = date.substring(8,10);
		var minute = date.substring(10,12);
		var second = date.substring(12,14);
		var d = year+"-"+month+"-"+day +" "+hour+":"+ minute +":"+ second;
		return d;
	}
	
	function createRechargeOrCashRecordHtml(data, action) {
		$("#rechargeOrCashRecord").children().remove();
		var actionDesc;
		var resultMessage;
		
		var totalRecord;
		var totalPage;
		var currentPage; 
				
		if(action == "recharge") {
			actionDesc = "充值";
			resultMessage = "暂无充值记录！";
			if(data != null) {
				totalRecord = data['totalRecord'];
				totalPage = data['totalPage'];
				currentPage = data['currentPage'];
				data = data['rechargeInfoList'];
			}
			
		} else {
			actionDesc = "提现";
			resultMessage = "暂无提现记录！";
		}
		
		if(data != null && data.length > 0 ) {
			
			for(var i=0; i<data.length; i++) {
				var rechargeOrCashInfo = data[i];
				var tradeStatus = getTradeStatus(rechargeOrCashInfo["transStat"]);
				var trClass = "";
				if(parseInt(i % 2) == 0) {
					trClass = "user_page4_tbtr";
				} 
				
				var transTime = (action == "recharge") ? formatDate(rechargeOrCashInfo["acctTime"]) : rechargeOrCashInfo["sysTime"];
				$("#rechargeOrCashRecord").append(
						 "<tr class='"+trClass+"'>"
						 +"<td class='user_page4_tbsp'>"
						 +"<span class='user_page4_tbsp1'>"
						 + transTime //交易时间
						 +"</span>"
						 +"</td>"
						 +"<td>"
						 + rechargeOrCashInfo["transAmt"] //金额
						 +"</td>"
						 +"<td>"+actionDesc+"</td>" //交易类型
						 +"<td class='user_page4_succeed'>"
						 +tradeStatus  //交易状态
						 +"</td>"
						 +"<tr>");
			}
			
			if(action == "recharge") {
				paging(totalPage,currentPage);//显示分页页脚
			}
			
		} else {
			resultMessage = "<tr><td colspan=\"4\"> <h2 align=\"center\" style=\"font-size: 15px;color: red;\">"
							+resultMessage+"</h2></td></tr>";
			$("#rechargeOrCashRecord").append(resultMessage);
		}
	}
	
	function paging(totalPage,currentPage) {
		$(".pager .pager_inner").wly_pager_bulder({pager_length:parseInt(totalPage),pager_per_length:parseInt(pageItems),pager_percent:parseInt(currentPage)});
	}
	//分页点击事件
	$(document).on("click",".pager .pager_inner a",function(e){  
		var curPage = $(this).attr("data-pager");
		$("#currentPage").val(curPage);
		getRecords();
    });
	
	function recharge() {
		window.location.href = "initRecharge.do";
	}
	function cash() {
		window.location.href = "initCash.do";
	}
</script> 
<input type="hidden" name="currentAction" id="currentAction" value="R" />
<input type="hidden" name="_startDate" id="_startDate" value="${startDate! ''}" />
<input type="hidden" name="_endDate" id="_endDate" value="${endDate! ''}" />
<input type="hidden" name="_transStat" id="_transStat" value="${transStat! ''}" />
<input type="hidden" name="currentPage" id="currentPage" value="1" />
<input type="hidden" name="rechargeRecord" id="rechargeRecord" value="${rechargeRecord! ''}" />
<input type="hidden" name="cashRecord" id="cashRecord" value="${cashRecord! ''}" />
</div>
</@page.pc>