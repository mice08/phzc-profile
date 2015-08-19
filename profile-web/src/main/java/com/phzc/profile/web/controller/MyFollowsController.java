package com.phzc.profile.web.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.phzc.console.base.pojo.User;
import com.phzc.pms.facade.model.base.UserAttentionDto;
import com.phzc.pms.facade.model.base.UserProductQueryResponse;
import com.phzc.pms.facade.model.base.UserRelationUpdateResponse;
import com.phzc.pms.facade.service.base.UserRelationFacade;
import com.phzc.pms.facade.service.business.UserProductFacade;
import com.phzc.profile.web.form.PageSet;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/order/*")
public class MyFollowsController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MyFollowsController.class);

    private static int rowSize = 10;

    @RequestMapping(value = "follows.do", method = RequestMethod.GET)
    public String follows(Model model, String curPage, HttpServletRequest request) {
        model.addAttribute("config", applicationPropertyConfig);
        model.addAttribute("title", "我的关注");
        try {
            User user = userSessionFacade.getUser(request);
            if (null == user) {
                return applicationPropertyConfig.getDomainPassport() + "/siteLogin/loginForm.do?toUrl=" + applicationPropertyConfig.getDomainProfile() + "/order/follows.do";
            }
            String custId = user.getCustId();
            UserProductFacade userProductFacade = getUserProductWebServiceClient();
            int startPage;
            if (StringUtils.isBlank(curPage) || "1".equals(curPage)) {
                startPage = 0;
            } else {
                startPage = (Integer.valueOf(curPage) - 1)*rowSize;
            }
            UserProductQueryResponse userProductQueryResponse = userProductFacade.queryUserAttentionProduct(custId, startPage, rowSize);
            if (CollectionUtils.isEmpty(userProductQueryResponse.getUserAttentionList())) {
                return "order/myFollows.ftl";
            }
            List<UserAttentionDto> userAttentionDtoList = userProductQueryResponse.getUserAttentionList();
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
        return "order/myFollows.ftl";
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