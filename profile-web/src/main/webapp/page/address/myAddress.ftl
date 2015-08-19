<#import "../share/pc/layout.ftl" as page> 
<#assign actives="active3" in page>
<@page.pc>
        <div class="user_page">
			<div class="main clearfix">
				<#include "../leftNav.ftl">
		        <@leftNav activeMenu="item6">
		        </@leftNav>
				<div class="right">
					<div class="user_page_inner  user_page6-1">
						<h1>收货地址</h1>

						<a href="" class="newaddr"> + 添加新地址</a>

						<div class="address">
							<h3 class='harve'>我的收货地址</h3>
							<table>
								<thead>
								<tr>
									<td>收货人</td>
									<td>所在地区</td>
									<td>详细地址</td>
									<td>邮编</td>
									<td>电话/手机</td>
									<td class='work4'>操作</td>
								</tr>
								</thead>
								<tbody>
								<#if list?exists>
									<#list list as addr>  
										<tr>
											<td>${addr.name}</td>
											<td>${addr.province!''}${addr.city!''}${addr.area!''}</td>
											<td>${addr.address}</td>
											<td>${addr.postcode}</td>
											<td>
												<#if addr.telephone??>
													${addr.telephone}
												</#if>
												<#if addr.phone??>
													${addr.phone}
												</#if>
											</td>
											<td class='del_td4'>
												<a href="javascript:void(0);" onclick="javascript:queryAddressById(${addr.addressId});" >修改</a>
												<a href="deleteAddress.do?addressId=${addr.addressId}" onclick="return confirm('确定删除吗？');">删除</a>
												<#if addr.isDefault == "Y">
													<span  class='del_a'>默认地址</span>
												<#else>
													<a href="setDefaultAddress.do?addressId=${addr.addressId}">设为默认</a>
												</#if>
											</td>
										</tr>
									</#list>
								</#if>
								</tbody>
							</table>
							<#if list?? && (list?size > 0)>
	    						<div  class="none" style="display:none"></div>
	    					<#else>
								<div  class="none"></div>
    						</#if>
						</div>
						<script>
							(function($){
								$(".user_page6-1 tbody tr").hover(function(){
									$(this).find("a.set").show()
								},function(){
									$(this).find("a.set").hide()
								})
							})(jQuery);
						</script>
						<script>
							$(" .newaddr").click(function(e){
								e.preventDefault();
								openExt(".ext_address")
							})
						</script>            
					</div>
				</div>
			</div>
		</div>
		<div class="bgcover"></div>
<div class="ext ext_address">
    <h1>  添加新地址</h1>
<form id="addressForm" name="addressForm" action="addAddress.do" enctype="multipart/form-data"  method="post">
    <div class="input_row">
        <label>
            <i>*</i> 收货人姓名
        </label>
        <div class="input_part">
            <input type="text" id="name" name="name"  onblur="checkName()" >
            <span>  长度不超过25个字 </span>
        </div>
    </div>
    <div class="input_row">
        <label>手机号码  </label>
        <div class="input_part">
            <input type="text" id="telephone" name="telephone" onblur="checkPhone()" maxlength="11">
            <span>   输入手机号码 </span>
        </div>
    </div>
    <div class="input_row">
        <label>     电话号码  </label>
        <div class="input_part">
        	<input type="text" id="phone" name="phone"  onblur="checkPhone()" maxlength="16">
            <span>  输入电话号码 </span>
        </div>
    </div>
    <div class="input_row">
        <label>
            <i>*</i>所在地区
        </label>
        <div class="select_part select_part_short zindex1" id="area_level1">   
            <a class="select_part_main" href="#" data-value="">省</a>
            <ul>           </ul>
        </div>
        <span class="span_sp"></span>
        <div class="select_part select_part_short zindex1" id="area_level2">
            <a class="select_part_main" href="#" data-value="">市</a>
            <ul>           </ul>
        </div>
        <span class="span_sp"></span>
        <div class="select_part select_part_short zindex1" id="area_level3" name="area_level3">
            <a class="select_part_main" href="#" data-value="">区</a>
            <ul>           </ul>
        </div>
        <!--      <span class="span_sp">县/区</span>-->
    </div>
    <div class="input_row">
        <label>
            <i>*</i>详细地址
        </label>
        <div class="text_part">
            <textarea id="textarea" name="textarea" onblur="checkAddress();"></textarea>
            <span> 建议您如实填写详细收货地址，例如门牌号、楼层和房间号等信息            </span>
        </div>
    </div>
    <div class="input_row">
        <label>   邮政编码  </label>
        <div class="input_part">
            <input type="text" id="postCode" name="postCode" maxlength="6" >
            <span>  输入邮政编码 </span>
        </div>
    </div>
    <div class="input_row">
        <label>

        </label>
        <input type="checkbox" id="isDefault" name="isDefault"  value="Y"  checked="checked">
        <span> 设为默认收货地址       </span>
    </div>
    <div class="input_row">
        <label>

        </label>
        <a class="submit" href="javascript:submitForm();" id="insertAddress">  提交       </a>
    </div>
    <input type="hidden" id="addressId" name="addressId" value="" />
    <input type="hidden" id="address" name="address" value="" />
    <input type="hidden" id="province" name="province" value="" />
    <input type="hidden" id="city" name="city" value="" />
    <input type="hidden" id="area" name="area" value="" />
</form>
<a href="#" id="closerClear" class="close"> 关闭</a>
<script src="${config.getDomainPcstatic()}/skin/js/jquery.jscrollpane.min.js"></script>
<script src="${config.getDomainPcstatic()}/skin/js/area_json.js"></script>
<script>
    var api1=null,api2=null,api3=null;
    var ul=$(".ext .select_part ul");
    var ul_level1=$("#area_level1 ul");
    var ul_level2=$("#area_level2 ul");
    var ul_level3=$("#area_level3 ul");
    var html="";
    for ( var item in area_json ){
        html+="<li><a href='#' data-value='"+item+"'>"+item+"</a></li>"
    };
    ul_level1.html(html);
    api1= ul_level1.jScrollPane({
        showArrows:false,
        maintainPosition: true
    }).data('jsp');
    api2= ul_level2.jScrollPane({
        showArrows:false,
        maintainPosition: true
    }).data('jsp');
    api3=ul_level3.jScrollPane({
        showArrows:false,
        maintainPosition: true
    }).data('jsp');

    $(".ext_address").hover(function(){
        ul.css({"display":"block","visibility":"hidden"});
        api1.reinitialise();
        ul.css({"display":"none","visibility":"visible"})
    });

    $("#area_level1 ul a").click(function(){
        var html="";
        var value=$(this).attr("data-value");
        for ( var item in area_json[value]){
            html+="<li><a href='#' data-value='"+item+"'>"+item+"</a></li>"
        }
        $("#area_level2 ul .jspPane").html(html);
        $("#area_level2 a.select_part_main").text("市").attr("data-value","").removeClass("active");
        
        $("#area_level3 ul .jspPane").html("");
        $("#area_level3 a.select_part_main").text("区").attr("data-value","").removeClass("active");
		
        ul_level2.css({"display":"block","visibility":"hidden"});
        api2.reinitialise();
        api3.reinitialise();
        ul.css({"display":"none","visibility":"visible"})
    });

    $("form").on("click","#area_level2 ul a",function(){
        var html="";
        var value=$(this).attr("data-value");
        var value2=$("#area_level1 a.select_part_main").attr("data-value");
        $.each(area_json[value2][value],function(k,v){
            html+="<li><a href='#' data-value='"+v+"'>"+v+"</a></li>"
        });

        ul_level3.css({"display":"block","visibility":"hidden"});
        $("#area_level3 ul .jspPane").html(html);
        $("#area_level3 a.select_part_main").text("区").attr("data-value","").removeClass("active");
        ul.css({"display":"block","visibility":"hidden"});
        api3.reinitialise();
        ul.css({"display":"none","visibility":"visible"})
    });
	/**  表单提交开始    **/
	function valueTelePhone(obj){
		var phoneReg =/^1[3458][0-9]\d{8}$/; 
		return phoneReg.test(obj);
	}
	function valuePhone(obj){
		var phoneReg1 = /^\d{3,4}-?\d{7,9}$/;
		//var phoneReg1 =/^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/;
		return phoneReg1.test(obj);
	}
	
	
	function valueCode(obj){
		var codeReg=/^[1-9][0-9]{5}$/;
		return codeReg.test(obj);
	}	
	
	/*校验用户名*/
	function checkName(){ 
		remove_infornmation_alert("name");
		var flag ;
		var name = $("#name").val();
		if(!name){
			infornmation_alert("error","name","请输入姓名");
			flag = false;
		}else if(name.length < 2){
			infornmation_alert("error","name","长度在2~25个字");
			flag = false;
		}else{
			infornmation_alert("correct","name","");
			flag = true;
		}
		
		return flag;
	}
	
	 // 空字符串判断
	function isEmpty(v, allowBlank) {
	   return v === null || v === undefined || (!allowBlank ? v === "" : false);
	}
	/*校验电话*/
	function checkPhone(){
		remove_infornmation_alert("telephone");
		remove_infornmation_alert("phone");
		
		var telephone=$("#telephone").val();
		var phone=$("#phone").val();
		var flag ;
		if (isEmpty(telephone) && isEmpty(phone)) {
	        //自定义错误提示
	        infornmation_alert("error","telephone","请输入至少一种联系方式");
	        return false;
      	}
		// 手机填了、固定电话没填
		if (!isEmpty(telephone) && isEmpty(phone)) {
			var chPhone= checkUserName_phone(telephone);
	        if( chPhone ){
				infornmation_alert("correct","telephone","");
				flag = true;
			}else{
				infornmation_alert("error","telephone","手机号码不正确,请重新输入");
				flag = false;
			}
      	}
      	 // 手机没填、固定电话填了
	     if (isEmpty(telephone) && !isEmpty(phone)) {
	     	var chPhone = valuePhone(phone);
	        if (chPhone) {
	          //自定义错误提示
		        infornmation_alert("correct","phone","");
				flag = true;
	        } else {
	         	infornmation_alert("error","phone","电话号码不正确,请重新输入");
				flag = false;
	        }
	     }
		
		if (!isEmpty(telephone) && !isEmpty(phone)) {
	       var chPhone1= checkUserName_phone(telephone);
	       var chPhone2 = valuePhone(phone);
	       if(chPhone1){
	       	infornmation_alert("correct","telephone","");
	       }
	       if(chPhone2){
	       	infornmation_alert("correct","phone","");
	       }
	       if(chPhone1 || chPhone2){
	       		flag = true;
	       }else{
	       		flag = false;
	       }
      	}	
		return  flag ;
		
	}
	
	/*校验详细地址 */
	function checkAddress(){
		remove_infornmation_alert("textarea");
		var flag ;
		var address=$("#textarea").val();
		if(!address){
			infornmation_alert("error","textarea","请填写详细地址");
			flag = false;
		}else{
			$("#address").val(address);
			infornmation_alert("correct","textarea","");
			flag = true;
		}
		return flag;
	}
	
	/*** 区域地址 **/
	function checkArea(){
		remove_infornmation_alert("area_level3");
		var flag ;
		
		var area1 = $("#area_level1>a").attr("data-value");
		var area2 = $("#area_level2>a").attr("data-value");
		var area3 = $("#area_level3>a").attr("data-value");
		
		if(!area1 || !area2 || !area3){
			infornmation_alert("error","area_level3","");
			flag = false;
		}else{
			$("#province").val(area1);
			$("#city").val(area2);
			$("#area").val(area3);
			infornmation_alert("correct","area_level3","");
			flag = true;
		}
		return flag;
	}
	
	function checkAll(){
		var chName = checkName() ;
		var chPhone = checkPhone();
		var chArea  = checkArea();
		var chAddress = checkAddress();
		
		if(chName && chArea && chAddress && chPhone){
			return true ;
		}else{
			return false ;
		}
		
	}
	
	/*提交*/
	function submitForm(){
		var all = checkAll();
		if(all){
			document.addressForm.submit();
		}else{
			return ;
		}		
			
	}
	
	/*** 修改收货地址 **/
	function queryAddressById(addressId){
		var data = {
			"addressId" : addressId
		};
		$.ajax({
			type : 'POST',
			url : 'queryAddressById.do' ,
			data : data,
			success : function(resp) {
				if (resp.success === true) {
					var addressId = resp.address.addressId;
					var name = resp.address.name;
					var telephone = resp.address.telephone ;
					var phone = resp.address.phone ;
					var address = resp.address.address ;
					var postCode = resp.address.postcode ;
					var province = resp.address.province;
					var city = resp.address.city;
					var area = resp.address.area;
					
					$("#addressId").val(addressId);
					
					if(null != name ){
						$(".input_row .input_part span").hide();
						$("#name").val(name);
					}
					
					if(null != telephone){
						$(".input_row .input_part span").hide();
						$("#telephone").val(telephone);
					}
					
					if(null != phone){
						$(".input_row .input_part span").hide();
						$("#phone").val(phone);
					}
					
					if(null != province){
						$("#area_level1>a").attr("data-value", province).html(province);
					}
					if(null != city){
						$("#area_level2>a").attr("data-value", city).html(city);
					}
					if(null != area){
						$("#area_level3>a").attr("data-value", area).html(area);
					}
					
					if(null != address){
						$(".text_part span").hide();
						$("#textarea").val(address);
						$("#address").val(address);
					}
					
					$("#postCode").val(postCode);
					
					openExt(".ext_address")
				} else {
					alert(出错啦,请重试);
				}
			}
		});
	}
	
	/** 关闭弹窗时清除数据 **/
	$("#closerClear").click(function(){
		$("#addressId").val("");
		$("#address").val("");
		$("#province").val("");
		$("#city").val("");
		$("#area").val("");
		
		$("#name").val("");
		$("#telephone").val("");
		$("#phone").val("");
		$("#area_level1>a").attr("data-value", "").html("省");
		$("#area_level2>a").attr("data-value", "").html("市");
		$("#area_level3>a").attr("data-value", "").html("区");
		$("#textarea").val("");
		$("#postCode").val("");
		
		$(".input_row .input_part span").show();
		$(".text_part span").show();
		
		
		
 	})
</script>
</@page.pc>