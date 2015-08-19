/*弹窗公用 js*/
var settime=null ;//settimeout 临时
function openExt(obj,settimeout){
    obj.show();
    obj.animate({opacity:1},300);
    if(settimeout){
        settime=setTimeout(function(){
            closeExt($(".ext_warning"));
        },2000)
    }
}
function closeExt(obj){
    obj.animate({
        opacity:0
    },300,'ease-in-out',function(){
        obj.hide()
    });
}

$(".ext_warning").click(
    function(e){
        clearTimeout(settime);
        closeExt($(this));
    }
);
/*end:弹窗公用js*/
/*后台架构js*/
var warning="";
var warning_obj=$(".ext_warning");
var warning_obj_inner=$(".ext_warning .wrapper");
/*验证信息*/
function checkUserName_phone(val){
    if($.trim(val)==""){
        warning="手机号不能为空白";
        return false;
    }else if(!(/^1\d{10}$/.test($.trim(val)))){
        warning="手机号格式不正确";
        return false;
    }else{
        return true
    }
};
function checkPassWord_register(val){
    if($.trim(val)=="") {
        warning="密码不能为空白";
        return false;
    }
    else if(!/^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9~!@#$%^&*()_+=`;':",.<>/?]{6,20}$/.test($.trim(val))){
        warning="密码必须为6-20位数字和字母组合";
        return false;
    }
    else{
        return true;
    }
};
function checkPassWord(val){
    if($.trim(val)=="") {
        warning="密码不能为空白";
        return false;
    }
    else{
        return true;
    }
};
function checkIdcode(val){
    if($.trim(val)!="") {
        return true;
    }
    else{
        warning="验证码不能为空白";
        return false;
    }
};
function checkRePassWord(val1,val2){
    if(val1==val2) {
        return true;
    }
    else{
        warning="两次密码必须相同";
        return false;
    }
};
function checkEmail(val){
    if($.trim(val)=="") {
        warning="邮箱不能为空白";
        return false;
    }
    else if(!/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test($.trim(val))){
        warning="您输入的邮箱有误";
        return false;
    }
    else{
        return true;
    }
};
function ajax_Form(data,url){
    var result="";
    $.ajax({
        type: 'POST',
        url: url ,
        data: data ,
        success: function(data){
            result=data;
        } ,
        error:function(error){
        },
        dataType: "json"
    });
    return result;
}
/*end:后台架构js*/



/*checkbox     register2.html*/
(function(){
    $(".checkbox").click(function(e){
        e.preventDefault();
        var obj=$(this)
        if(obj.hasClass("checked")){
            obj.removeClass("checked");
        }else{
            obj.addClass("checked");
        }
    })
})(Zepto);
(function(){
    $(".register_detail form i a").click(function(e){
        e.preventDefault();
        openExt($(".ext_register"))
    })
})(Zepto);
/*ext close*/
(function(){
    var ext=$(".ext");
    $(".ext a.close").click(function(e){
        e.preventDefault();
        closeExt(ext)
    });

})(Zepto);


/*form radiobox*/
(function(){
    var document_obj=$(document);
    document_obj.on("touchstart","li.radio",function(){
        var obj=$(this);
        var data_name=obj.attr("data-name");
        $("[data-name="+data_name+"]").removeClass("radio_checked");
        obj.addClass("radio_checked")
    })
    document_obj.on("click","li.radio",function(){
        var obj=$(this);
        var data_name=obj.attr("data-name");
        $("[data-name="+data_name+"]").removeClass("radio_checked");
        obj.addClass("radio_checked")
    })
})(Zepto);
/*form checkbox*/
(function(){
    var document_obj=$(document);
    document_obj.on("touchstart","li.checkbox",function(e){
        e.stopPropagation();
        var obj=$(this);
        if(obj.hasClass("checkbox_checked")){
            obj.removeClass("checkbox_checked")
        }else{
            obj.addClass("checkbox_checked")
        }
    })
})(Zepto);



