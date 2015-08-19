<#import "../share/pc/layout.ftl" as page>
<@page.pc>
<html>
<head>
</head>
<body>
<div class="user_page">
    <!--[if IE 6]>
    <script src="skin/js/png_fix.js"></script>
    <script>
        DD_belatedPNG.fix('.img_part .cover');
    </script>
    <![endif]-->
    <div class="bgcover"></div>
    <div class="main clearfix">
        <#include "../leftNav.ftl">
        <@leftNav activeMenu="item7">
        </@leftNav>
        <div class="right">
            <div class="user_page_inner  user_page7-2">

                <ul class="user_top cha_pass">
                    <li class="first">
                        <a href="/discussion/query.do">
                            我的吐槽
                        </a>
                    </li>
                    <li>
                        <a href="#"  class="active">
                            我要吐槽
                        </a>
                    </li>
                </ul>
                <div class="mytweet">

                    <form action="/discussion/submit.do" id="disForm" name= "disForm" method ="post">
                        <div class="input_row">
                            <div class="text_part">
                    <textarea name="content" id="content">
                    </textarea>
                        <span>
                        请在这里留下您的意见和建议，我们非常期待(1-500字以内)。
                        </span>
                            </div>
                        </div>
                        <div class="input_row">
                            <input type="checkbox" id="connectflag" name="connectflag" value="1">
                            <input type="hidden" id="errmsg" name="errmsg" value="${errmsg}">
                            <span>请允许我们通过您的会员信息与您沟通，谢谢支持。 </span>
                        </div>
                        <div class="input_row ">

                            <a class="submit submit7" href="#"  onclick="validateForm()">
                                提交
                            </a>
                        </div>
                    </form>
                </div>            </div>
        </div>
    </div>
</div>
</div>
</div>

<div class="ext ext_state1" style="display: hide;">
    <h1>提示</h1>
    <div class="ext_hint ext_hint_success">
        <div class="content" style="margin-left:0px;text-align: center;">
            <p></p><h2><i id="alertMessageInfo">吐槽内容不能为空！</i></h2><p></p>
            <a href="javascript:clickConfirm('.ext_state1');" style="margin-top:40px;margin-left: auto;margin-right: auto;" class="confirm2">确认</a>
        </div>
    </div>
    <a href="javascript:clickConfirm('.ext_state1');" class="close"> 关闭 </a>
    <script>
        function clickConfirm(divName){
            closeExt(divName);
        }
    </script>
</div>

<div class="ext ext_state2" style="display: hide;">
    <h1>提示</h1>
    <div class="ext_hint ext_hint_success">
        <div class="content" style="margin-left:0px;text-align: center;">
            <p></p><h2><i id="alertMessageInfo">吐槽字数最多为500！</i></h2><p></p>
            <a href="javascript:clickConfirm('.ext_state2');" style="margin-top:40px;margin-left: auto;margin-right: auto;" class="confirm2">确认</a>
        </div>
    </div>
    <a href="javascript:clickConfirm('.ext_state2');" class="close"> 关闭 </a>
    <script>
        function clickConfirm(divName){
            closeExt(divName);
        }
    </script>
</div>

<div class="ext ext_state3" style="display: hide;">
    <h1>提示</h1>
    <div class="ext_hint ext_hint_success">
        <div class="content" style="margin-left:0px;text-align: center;">
            <p></p><h2><i id="alertMessageInfo">您输入的内容含有非法字符,请重新输入！</i></h2><p></p>
            <a href="javascript:clickConfirm('.ext_state3');" style="margin-top:40px;margin-left: auto;margin-right: auto;" class="confirm2">确认</a>
        </div>
    </div>
    <a href="javascript:clickConfirm('.ext_state3');" class="close"> 关闭 </a>
    <script>
        function clickConfirm(divName){
            closeExt(divName);
        }
    </script>
</div>
</body>
<script>

    var errmsg = $("#errmsg").val();
    if(errmsg!=""&&errmsg!=null){
        alert(errmsg);
    }

    function validateForm(){
        //remove_infornmation_alert("connectflag");
        //var flag = $("input[type=checkbox]").is(':checked');
        //if(!flag){
        //	infornmation_alert("error","connectflag","请选择允许");
        //	return ;
        //}
        var content = document.getElementById("content").value;
        var patrn=/[`~!@#$%^&*()_+<>:"{}.\/'[\]]/im;

        if(patrn.test(content)){
            openExt(".ext_state3");
            return false;
        }

        content=content.replace(/\s+/g,"");
        if(content==""||content==null){
            openExt(".ext_state1");
            return false;
        }else{
            content = document.getElementById("content").value
            if(parseInt(content.length)>500){
                openExt(".ext_state2");
                return false;
            }
        }
        document.getElementById("disForm").submit();
    }
</script>
</html>
</@page.pc>