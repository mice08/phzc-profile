<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--[if lt IE 8]> <html class="lt-ie8"><![endif]-->
<!--[if gt IE 8]><!-->
<html><!--<![endif]-->
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
    <p class="left">
        您好 欢迎来到普惠众筹网！目前网站内测阶段仅对内部员工开放，敬请提出宝贵意见,谢谢！
    </p>
    <p class="right">
        <span class="login">
            <a href="#">
                [登录]
            </a>
            |
            <a href="#">
                [免费注册]
            </a>
        </span>
        <span class="infor">
            <a href="#">
                退出
            </a>
            |
            <a href="#">
                用户中心
            </a>
        </span>
        <span class="email">
            服务邮箱：pub_zc@pingan.com.cn
        </span>
    </p>
</div>    </div>
    <div class="header">
        <div class="main">
    <img src="<c:url value='/images/logo.png'/>" class="logo">
    <ul>
        <li>
            <a href="#" class="">
                首页
            </a>
        </li>
        <li>
            <a href="#" class="">
                众筹项目
            </a>
        </li>
        <li>
            <a href="#" class="">
                发起众筹
            </a>
        </li>
        <li>
            <a href="#" class="">
                帮助中心
            </a>
        </li>
        <li>
            <a href="#" class="">
                筹吧
            </a>
            <em>
                new
            </em>
        </li>
    </ul>
</div>    </div>
    <div class="bodyer">
        <div class="user_page">
    <div class="main clearfix">
        <div class="left">
            <div class="user_nav">
    <div class="user_infor">
        <div class="img_part">
            <img src="<c:url value='/images/head.jpg' />">
            <div class="cover">

            </div>
        </div>
        <h3>
            Seagoo 22
        </h3>
    </div>
    <ul>
        <li class="item1 active">
            <a href="user_page1.html" class=" active">
                我的众筹
            </a>
            <span></span>
        </li>
        <li class="item2 ">
            <a href="user_page2.html" class=" ">
                我的认证
            </a>
            <span></span>
        </li>
        <li class="item3 ">
            <a href="user_page3-1.html" class=" ">
                我的银行卡
            </a>
            <span></span>
        </li>
        <li class="item4 ">
            <a href="user_page4.html" class=" ">
                充值提现
            </a>
            <span></span>
        </li>
        <li class="item5 ">
            <a href="user_page5.html" class=" ">
                我的资料
            </a>
            <span></span>
        </li>
        <li class="item6 ">
            <a href="user_page6.html" class=" ">
                收货地址
            </a>
            <span></span>
        </li>
        <li class="item7 ">
            <a href="user_page7.html" class=" ">
                我的吐槽
            </a>
            <span></span>
        </li>
        <li class="item8 ">
            <a href="user_page8.html" class=" ">
                修改密码
            </a>
            <span></span>
        </li>
    </ul>
</div>        </div>
        <div class="right">
            <div class="user_page_inner  user_page1">
                
    <div class="user_page1_nav">
        <ul class='user_page1_nav_ul'>
            <li class='active'><a href="">我支持的</a></li>
            <li><a href="">我预约的</a></li>
            <li class='user_page1_nav_li'><a href="">我关注的</a><i>4</i></li>
        </ul>
    </div>
    <table class="right_item">
        <thead>
        <tr>
            <td class="right_item_thd">项目信息</td>
            <td>份额/份数</td>
            <td>支付金额</td>
            <td>订单状态</td>
            <td class="right_item_thead">状态</td>
        </tr>
        </thead>
        <tbody>

        <tr>
            <td class="right_tab_tbody">
                <img src="<c:url value='/images/thead_pt_1.jpg' />" alt="">
                <div class="right_tab_td">
                    <p class='right_tab_p'>
                        <span class='right_tab_time'>2015-6-15</span><span>14:58:22</span>
                    </p>

                    <p>我送盲童一本书</p>
                    <div class="right_tab_per">
                        <p class="right_tab_bar">
                            <i class='right_tab_bar_co right_tab_bar1'></i>
                        </p>52%
                    </div>

                    <p class='right_tab_order'>订单编号：812020150615467891</p>

                </div>
            </td>
            <td>1</td>
            <td class='right_money'>200.00</td>
            <td>未支付</td>
            <td>
                <a href="" class="pay_btn">立即支付</a>
                <a href="" class='right_look'>查看详情</a>
            </td>
        </tr>
        <tr class="right_tbody">
            <td class="right_tab_tbody">
                <img src="<c:url value='/images/thead_pt_1.jpg' />" alt="">
                <div class="right_tab_td">
                    <p class='right_tab_p'>
                        <span class='right_tab_time'>2015-6-29</span><span>14:58:22</span>
                    </p>
                    <p>豆意咖啡-深圳本土创新咖啡典范</p>
                    <div class="right_tab_per">
                        <p class="right_tab_bar">
                            <i class='right_tab_bar_co right_tab_bar2'></i>
                        </p>81%
                    </div>

                    <p class='right_tab_order'>订单编号：812020150615467891</p>

                </div>
            </td>
            <td>2</td>
            <td class='right_money'>20000.00</td>
            <td class='right_def'>已过期</td>
            <td>

                <a href="" class='right_look'>查看详情</a>
            </td>
        </tr>
        <tr>
            <td class="right_tab_tbody">
                <img src="<c:url value='/images/thead_pt_1.jpg' />" alt="">
                <div class="right_tab_td">
                    <p class='right_tab_p'>
                        <span class='right_tab_time'>2015-7-5</span><span>14:58:22</span>
                    </p>
                    <p>集善残疾儿童助养项目</p>
                    <div class="right_tab_per">
                        <p class="right_tab_bar">
                            <i class='right_tab_bar_co right_tab_bar3'></i>
                        </p>38%
                    </div>

                    <p class='right_tab_order'>订单编号：812020150615467891</p>

                </div>
            </td>
            <td>1</td>
            <td class='right_money'>3240.00</td>
            <td>支付成功</td>
            <td>

                <a href="" class='right_look'>查看详情</a>
                <a href="" class='right_look'>查看证书</a>
            </td>
        </tr>
        <tr class="right_tbody">
            <td class="right_tab_tbody">
                <img src="<c:url value='/images/thead_pt_1.jpg' />" alt="">
                <div class="right_tab_td">
                    <p class='right_tab_p'>
                        <span class='right_tab_time'>2015-7-10</span><span>14:58:22</span>
                    </p>
                    <p>物流众筹1号</p>
                    <div class="right_tab_per">
                        <p class="right_tab_bar">
                            <i class='right_tab_bar_co right_tab_bar4'></i>
                        </p>135%
                    </div>

                    <p class='right_tab_order'>订单编号：812020150615467891</p>

                </div>
            </td>
            <td>1</td>
            <td class='right_money'>10000.00</td>
            <td class='right_def'>交易关闭</td>
            <td>

                <a href="" class='right_look'>查看详情</a>
            </td>
        </tr>
        </tbody>
    </table>
    <div class="pager">
    <div class="pager_inner">
        <a href="#" class="first">
            |<
        </a>
        <a href="#" class="prev">
            <
        </a>
        <a href="#" class="active">
            1
        </a>
        <a href="#">
            2
        </a>
        <a href="#">
            3
        </a>
        <a href="#">
            4
        </a>
        <a href="#">
            5
        </a>
        <a href="#">
            6
        </a>
        <a href="#">
            7
        </a>
        <a href="#">
            8
        </a>
        <a href="#" class="more">
            ...
        </a>
        <a href="#" class="next">
            >
        </a>
        <a href="#" class="last">
            >|
        </a>
    </div>
</div>            </div>
        </div>
    </div>
</div>

<!--[if IE 6]>
<script src="skin/js/png_fix.js"></script>
<script>
    DD_belatedPNG.fix('.user_nav .user_infor .cover');
</script>
<![endif]-->    </div>
    <div class="footer">
        <div class="main">
    <div class="top">
        <p class="bottom_menu">
            <a href="#">
                关于我们
            </a>
            |
            <a href="#">
                人才招聘
            </a>
            |
            <a href="#">
                联系我们
            </a>
            <br>
            <a href="#">
                用户协议
            </a>
            |
            <a href="#">
                友情链接
            </a>
            |
            <a href="#">
                网站地图
            </a>
        </p>
        <ul class="bottom_infor">
            <li class="item1">
                服务邮箱<br>
                pub_zc@pingan.com.cn
            </li>
            <li class="item2">
                服务邮箱<br>
                pub_zc@pingan.com.cn
            </li>
        </ul>
    </div>
    <div class="bottom">
        <div class="bottom_links">
            <a href="#" class="open">
                平安集团旗下网站
            </a>
            <ul>
                <li><a href="http://www.pingan.com" target="_blank">中国平安官网</a></li>
                <li><a href="http://one.pingan.com" target="_blank">平安一账通</a></li>
                <li><a href="http://www.lufax.com" target="_blank">陆金所</a></li>
                <li><a href="http://www.xsme.cn" target="_blank">平安交易所</a></li>
            </ul>
        </div>
    </div>
    <p>
        版权所有 ©2015 深圳前海普惠众筹交易股份有限公司 粤ICP备15042952号
    </p>
</div>    </div>
</div>
<div class="fix_function">
    <a href="#">
        意见<br>
        反馈
    </a>
    <a class="last" href="#">
        返回
    </a>
</div>
<script src="<c:url value='/js/custom.js' />"></script>
</body>
</html>