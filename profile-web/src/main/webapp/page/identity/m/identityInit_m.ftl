<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p","backUrl":"${config.getDomainProfile()}/m/identity/identityCenter.do","parentName":"特定合格投资人认证"} in page>
<@page.pc>
		<section class="user_identification no_banner">
			<form class="form-horizontal" id="identityForm" name="identityForm" action="/m/identity/identity.do" method="post">
				<input type="hidden" id="assetSal" name="assetSal" value="" > 
				<input type="hidden" id="assetTot" name="assetTot" value="" > 
				<input type="hidden" id="isAgree" name="isAgree" value="3" > 
				<input type="hidden" id="source" name="source" value="" > 
				<ul>
					<li><label> 姓名 </label> <span> ${userIdentity.usrName} </span></li>
					<li><label> 身份证号 </label> <span> ${userIdentity.certId} </span></li>
					<li class="normal">
						<div class="li_infor">特定合格投资人，以下条件选择之一即可</div>
					</li>
					<li id="assets_box_01" class="checkbox"><label> 金融资产不低于300万人民币 </label> <b></b></li>
					<li id="assets_box_02" class="checkbox"><label> 最近三年个人年均收入不低于50万 </label> <b></b>
					</li>
					<li class="normal"><a href="javascript:submitForm();" class="submit"> 保存 </a></li>
				</ul>
			</form>
		</section>
	</div>
	<div class="ext ext_warning">
		<div class="wrapper">请至少勾选一项</div>
	</div>
	<div class="logo"></div>
	<script>
		//提交检验
		function submitForm(){
			var checkeFlg=false;
			if($("#assets_box_01").hasClass("checkbox_checked")){
				$('#assetSal').val(1);
				checkeFlg=true;
			}
			if($("#assets_box_02").hasClass("checkbox_checked")){
				$('#assetTot').val(2);
				checkeFlg=true;
			}
			if(checkeFlg){
				$("#isAgree").val(3);
				document.identityForm.submit();
			}else{
				openExt($(".ext_warning"));
			}
			
		}
	</script>

 </@page.pc>