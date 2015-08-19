package com.phzc.profile.web.controller.m;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.pms.facade.model.base.UserAttentionDto;
import com.phzc.pms.facade.model.base.UserProductQueryResponse;
import com.phzc.pms.facade.model.base.UserRelationUpdateResponse;
import com.phzc.pms.facade.service.base.UserRelationFacade;
import com.phzc.pms.facade.service.business.UserProductFacade;
import com.phzc.profile.web.controller.BaseController;
import com.phzc.profile.web.form.PageSet;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/m/order/*")
public class MMyFollowsController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MMyFollowsController.class);

    private static int rowSize = 10;

    @RequestMapping(value = "follows.do", method = RequestMethod.GET)
    public String follows(Model model, String curPage, HttpServletRequest request) {
        model.addAttribute("config", applicationPropertyConfig);
        model.addAttribute("title", "我的关注");
        try {
            User user = userSessionFacade.getUser(request);
            if (null == user) {
                return applicationPropertyConfig.getDomainPassport() + "/m/login/loginForm.do";
            }
            String custId = user == null ? "0" : user.getCustId();
//                        String custId="1507301819349707";
            UserProductFacade userProductFacade = getUserProductWebServiceClient();
            int startPage;
            if (StringUtils.isBlank(curPage) || "1".equals(curPage)) {
                startPage = 0;
            } else {
                startPage = (Integer.valueOf(curPage) - 1) * rowSize;
            }
            UserProductQueryResponse userProductQueryResponse = userProductFacade.queryUserAttentionProduct(custId, startPage, rowSize);
            List<UserAttentionDto> userAttentionDtoList = CollectionUtils.isEmpty(userProductQueryResponse.getUserAttentionList()) ? null : userProductQueryResponse.getUserAttentionList();
            model.addAttribute("userAttentionList", userAttentionDtoList);

            PageSet page = new PageSet();
            page.setRowCount(userProductQueryResponse.getTotalCount());
            if (StringUtils.isNotEmpty(curPage)) {
                page.setCurPage(Integer.valueOf(curPage));
            }
            page.countMaxPage();
            model.addAttribute("pageSet", page);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return "order/m/myFollows_m.ftl";
    }

    @RequestMapping(value = "followsPaging.do", method = RequestMethod.GET)
    @ResponseBody
    public void doPaging(String curPage, HttpServletRequest request, HttpServletResponse httpresponse) {
        PrintWriter pw = null;
        try {
            User user = userSessionFacade.getUser(request);
            String custId = user == null ? "0" : user.getCustId();
//            String custId="1507301819349707";
            List<UserAttentionDto> userAttentionDtoList =new ArrayList<>();
            if (Integer.parseInt(curPage)>1){
                userAttentionDtoList = getUserAttentionDtos(curPage, custId);
            }
            pw = httpresponse.getWriter();
            String html = getDisplayHtml(userAttentionDtoList);
            pw.write(html);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
            pw.write(e.getLocalizedMessage());
        }
    }

    /**
     * 获取金额格式化后形式
     *
     * @param amount
     * @return
     */
    public String getFormatFunds(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("###,###.00");
        String raisedFunds = df.format(Double.valueOf(amount.toString()));
        if (raisedFunds.equals("0.0000") || raisedFunds.equals("0") || raisedFunds.equals(".00")) {
            raisedFunds = "0.00";
        }
        return raisedFunds;
    }

    private String getDisplayHtml(List<UserAttentionDto> userAttentionDtoList) {
        StringBuilder html = new StringBuilder();
        if (CollectionUtils.isNotEmpty(userAttentionDtoList)) {
            for (UserAttentionDto dto : userAttentionDtoList) {
                String rate = dto.getProductRate().substring(0, dto.getProductRate().length() - 1);
                html.append("<li data-value=\"" + rate + "\">");
                html.append("<a href=\"" + applicationPropertyConfig.getDomainItem() + "/m/item/detail.do?productId=" + dto.getProductId() + "\">");
                html.append("<img src=\"" + dto.getImageUrl() + "\">");
                html.append("<div class=\"infor\">");
                html.append("<h1>" + dto.getProductName() + "</h1>");
                html.append("<div class=\"progress\"><div class=\"progress_inner\" style=\"width:"+dto.getProductRate()+";\"></div></div>");
                html.append("<div class=\"num\">");
                html.append("<p><strong><span>" + dto.getProductRate() + "</span></strong><br>已达到</p>");
                html.append("<p><strong><span>￥" + getFormatFunds(dto.getProductShare()) + "</span></strong><br>已筹集</p>");
                html.append("<p><strong><span>" + (dto.getSellCount()==null?"0":dto.getSellCount()) + "</span></strong><br>支持人数</p>");
                html.append("<p><strong><span>" + dto.getRemainDays() + "天</span></strong><br>剩余天数</p>");
                html.append("</div></div>");
                switch (dto.getProductStatus()) {
                    case 9:
                        html.append("<div class=\"states states1\"></div>");
                        break;
                    case 7:
                        html.append("<div class=\"states states2\"></div>");
                        break;
                    case 11:
                        html.append("<div class=\"states states3\"></div>");
                        break;
                    case 10:
                        html.append("<div class=\"states states4\"></div>");
                        break;
                    case 8:
                        html.append("<div class=\"states states5\"></div>");
                        break;
                    case 12:
                        html.append("<div class=\"states states6\"></div>");
                        break;
                    case 13:
                        html.append("<div class=\"states states7\"></div>");
                        break;
                    case 15:
                        html.append("<div class=\"states states8\"></div>");
                        break;
                    case 16:
                        html.append("<div class=\"states states9\"></div>");
                        break;
                }
                html.append("</a></li>");
            }
        }
        return html.toString();
    }

    private List<UserAttentionDto> getUserAttentionDtos(String curPage, String custId) throws ServiceException {
        UserProductFacade userProductFacade = getUserProductWebServiceClient();
        int startPage;
        if (StringUtils.isBlank(curPage) || "1".equals(curPage)) {
            startPage = 0;
        } else {
            startPage = (Integer.valueOf(curPage) - 1) * rowSize;
        }
        UserProductQueryResponse userProductQueryResponse = userProductFacade.queryUserAttentionProduct(custId, startPage, rowSize);
        return CollectionUtils.isEmpty(userProductQueryResponse.getUserAttentionList()) ? null : userProductQueryResponse.getUserAttentionList();
    }

    @RequestMapping(value = "cancelFollow.do", method = RequestMethod.POST)
    public void cancelFollow(Model model, HttpServletRequest request, HttpServletResponse response) {
        String productId = request.getParameter("productId");
        logger.info("取消关注product id: " + productId);
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            User user = userSessionFacade.getUser(request);
            String custId = user == null ? "" : user.getCustId() + "";//从session中获取user
//            String custId="20150624100336";
            UserRelationFacade userRelationFacade = getUserRelationWebServiceClient();
            UserRelationUpdateResponse userRelationUpdateResponse = userRelationFacade.cancelUserFollow(productId, custId);
            if (userRelationUpdateResponse.getResponseCode() == "200") {
                pw.write("取消关注成功");
            }
        } catch (Exception e) {
            pw.write(e.getLocalizedMessage());
            logger.error(e.getMessage());
        }
        pw.flush();
        pw.close();
    }

}