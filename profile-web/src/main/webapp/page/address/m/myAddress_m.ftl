<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p","backUrl":"${config.getDomainProfile()}/m/userInfo.do","parentName":"收货地址"} in page>
<@page.pc>

<#--<div class="pagewrapper">-->
    <section class="user_address no_banner">
    <#--<div class="header_page custom_header_page">-->
    <#--<span>-->
        <#--收货地址    </span>-->
        <#--<a class="back" href="${config.getDomainProfile()}/m/userInfo.do" onclick="${config.getDomainProfile()}/m/userInfo.do">-->
            <#--back-->
        <#--</a>-->
    <#--</div>-->
        <form id="addressForm" name="addressForm" action="addAddress.do" enctype="multipart/form-data" method="post">
            <ul>
                <li>
                    <label> 收货人 </label>
                    <input type="text" id="name" name="name" placeholder="填写用户名" class="recharge_money" value="${address.name!}">
                </li>
                <li>
                    <label> 手机号 </label>
                    <input type="number" id="telephone" name="telephone" placeholder="填写手机号码" class="recharge_money" value="${address.telephone!}">
                </li>
                <li class="select_part sp3">
                    <label> 省 </label>
                    <select id="area_level1"></select>
                </li>
                <li class="select_part">
                    <label> 市 </label>
                    <select id="area_level2"></select>
                </li>
                <li class="select_part">
                    <label> 区 </label>
                    <select id="area_level3"></select>
                </li>
                <li>
                    <label> 详细地址 </label>
                    <textarea placeholder="填写详细地址" id="address" name="address">${address.address!}</textarea>
                </li>
                <li>
                    <label> 邮政编码 </label>
                    <input type="number" id="postCode" name="postCode" placeholder="填写邮政编码" class="recharge_money" value="${address.postcode!}">
                </li>
                <li class="normal">
                    <a class="submit" href="javascript:submitForm();"> 提交 </a>
                </li>
            </ul>
            <input type="hidden" id="province" name="province" value="${address.province!''}"/>
            <input type="hidden" id="city" name="city" value="${address.city!''}"/>
            <input type="hidden" id="area" name="area" value="${address.area!''}"/>
            <input type="hidden" id="addressId" name="addressId" value="${address.addressId!''}"/>
            <input type="hidden" id="custId" name="custId" value="${address.custId!''}"/>
            <input type="hidden" id="phone" name="phone" value="${address.phone!''}"/>
            <input type="hidden" id="isDefault" name="isDefault" value="${address.isDefault!''}"/>
            <input type="hidden" id="fromUrl" name="fromUrl" value="${fromUrl!''}"/>
        </form>
        <script src="${config.getDomainMstatic()}/skin/js/area_json.js?${config.getDomainMstaticVersion()}"></script>
        <script>
            var ul_level1 = $("#area_level1");
            var ul_level2 = $("#area_level2");
            var ul_level3 = $("#area_level3");
            var html = "";
            for (var item in area_json) {
                html += "<option value='" + item + "'>" + item + "</option>"
            }
            ul_level1.html(html);
            if ($("#province").val() != "") {
                ul_level1.val($("#province").val());
            }

            ul_level1.change(function () {
                var html = "";
                var value = $(this).attr("value");
                for (var item in area_json[value]) {
                    html += "<option value='" + item + "'>" + item
                            + "</option>"
                }
                ul_level2.html(html);
                if ($("#city").val() != "") {
                    ul_level2.val($("#city").val());
                }
                ul_level3.html("");
                ul_level2.change();
            });

            ul_level2.change(function () {
                var html = "";
                var value = $(this).attr("value");
                var value2 = ul_level1.val();
                $.each(area_json[value2][value], function (k, v) {
                    html += "<option value='" + v + "'>" + v + "</option>"
                });
                ul_level3.html(html);
                if ($("#area").val() != "") {
                    ul_level3.val($("#area").val());
                }
            });

            ul_level1.change()

            /*提交*/
            function submitForm() {
                var all = checkAll();
                if (all) {
                    document.addressForm.submit();
                } else {
                    return;
                }
            }

            function checkAll() {
                var chName = checkName();
                var chPhone = checkPhone();
                var chArea = checkArea();
                var chAddress = checkAddress();

                if (chName && chArea && chAddress && chPhone) {
                    return true;
                } else {
                    return false;
                }
            }

            /*校验用户名*/
            function checkName() {
                var flag;
                var name = $("#name").val();
                if (!name) {
                    warning_obj_inner.text("请输入姓名");
                    openExt(warning_obj, true);
                    flag = false;
                } else if (name.length < 2) {
                    warning_obj_inner.text("姓名长度在2~25个字");
                    openExt(warning_obj, true);
                    flag = false;
                } else {
                    flag = true;
                }
                return flag;
            }

            /*校验电话*/
            function checkPhone() {
                var telephone = $("#telephone").val();
                var flag;
                if (!telephone) {
                    warning_obj_inner.text("请输入手机");
                    openExt(warning_obj, true);
                    flag = false;
                } else if (!checkUserName_phone(telephone)) {//用户名
                    warning_obj_inner.text(warning);
                    openExt(warning_obj, true);
                    flag = false;
                } else {
                    flag = true;
                }
                return flag;
            }

            /*** 区域地址 **/
            function checkArea() {
                var flag;

                var area1 = $("#area_level1").val();
                var area2 = $("#area_level2").val();
                var area3 = $("#area_level3").val();

                if(!area1){
                    warning_obj_inner.text("请选择省");
                    openExt(warning_obj, true);
                    return false;
                }
            	if(!area2){
           		    warning_obj_inner.text("请选择市");
                    openExt(warning_obj, true);
                    return false;
            	}
            	if(!area3){
           		 	warning_obj_inner.text("请选择区");
                    openExt(warning_obj, true);
                    return false;
           		}
                $("#province").val(area1);
                $("#city").val(area2);
                $("#area").val(area3);
				
                return true;
            }

            /*校验详细地址 */
            function checkAddress() {
                var flag;
                var address = $("#address").val();
                if (!address) {
                    warning_obj_inner.text("请填写详细地址");
                    openExt(warning_obj, true);
                    flag = false;
                } else {
                    flag = true;
                }
                return flag;
            }

            // 空字符串判断
            function isEmpty(v, allowBlank) {
                return v === null || v === undefined
                        || (!allowBlank ? v === "" : false);
            }
        </script>
    </section>
</div>

<div class="ext ext_warning">
    <div class="wrapper">收货地址有误</div>
</div>
<div class="logo"></div>

</@page.pc>
