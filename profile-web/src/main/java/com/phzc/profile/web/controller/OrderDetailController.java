package com.phzc.profile.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phzc.console.base.pojo.User;
import com.phzc.console.base.remote.WebServiceClientFactory;
import com.phzc.console.base.session.UserSessionFacade;
import com.phzc.console.util.Md5Utils;
import com.phzc.pms.facade.model.base.ProductInfoForMyOrderResponse;
import com.phzc.pms.facade.model.base.ProductInfoForOrderResponse;
import com.phzc.pms.facade.model.base.ProductOrderDto;
import com.phzc.pms.facade.service.dubbo.ProductDetailDubbo;
import com.phzc.pms.facade.service.dubbo.ProductProjectDetailDubbo;
import com.phzc.pms.facade.service.dubbo.UserProductDubbo;
import com.phzc.profile.web.dto.*;
import com.phzc.profile.web.entity.CommonEnum;
import com.phzc.profile.web.form.OrderInfoVo;
import com.phzc.profile.web.form.PageSet;
import com.phzc.ubs.common.facade.model.*;
import com.phzc.ubs.common.facade.service.AddressInfoFacade;
import com.phzc.ubs.common.facade.service.UserInfoFacade;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Controller
public class OrderDetailController extends BaseController {
    Logger logger = (Logger) Logger.getLogger(OrderDetailController.class);

    @Autowired
    private WebServiceClientFactory serviceClientFactory;

    @Autowired
    private UserSessionFacade userSessionFacade;

    @Autowired
    private ProductDetailDubbo productDetailDubbo;

    @Autowired
    private ProductProjectDetailDubbo productProjectDetailDubbo;

    @Autowired
    private UserProductDubbo userProductDubbo;

    static private final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    static private final String REFOUND_FLAG_NO = "0";
    static private final String REFOUND_FLAG_YES = "1";
    static private final String rowSize = "10";//每页显示条目数

    //订单serviceUrl
    @RequestMapping(value = "/orders.do", method = RequestMethod.GET)
    public String mySupport(Model model, HttpServletRequest request, HttpServletResponse response, String curPage) {
        logger.info("mySupport Enter");
        System.out.println("1=wo lai le");
        model.addAttribute("config", applicationPropertyConfig);
        try {
            User user = userSessionFacade.getUser(request);
//        	User user = new User();
//    		user.setCustId("20150629205256");
//            user.setCustId("20150624100336");
//    		user.setOperId("000001");

            /*调用订单接口CustId查询普通订单接口(tradeService.queryByCustId)*/
            List<OrderInfo> orderInfoList = getOrderInfoList(user.getOperId(), user.getCustId(), model, curPage);
            System.out.println("4调用订单接口,获取订单列表：" + orderInfoList.size());
            List<OrderInfoVo> orderInfoVoList = new ArrayList<OrderInfoVo>();
            if (CollectionUtils.isEmpty(orderInfoList)) {
                logger.error("orderInfoList is null");
                model.addAttribute("orderInfoList", orderInfoVoList);
                return "order/myOrder.ftl";
            }

//            logger.error("orderInfoList.size():" + orderInfoList.size());

            List<String> userSupportProductIdList = new ArrayList<String>();
            StringBuffer buffer = new StringBuffer();
            for (OrderInfo orderInfo : orderInfoList) {
                buffer.append(orderInfo.getProductId());
                buffer.append("-");
                buffer.append(orderInfo.getHowBuyId());
                userSupportProductIdList.add(buffer.toString());
                buffer.setLength(0);
            }

            for (OrderInfo order : orderInfoList) {
                OrderInfoVo vo = new OrderInfoVo();
                BeanUtils.copyProperties(vo, order);
                //退款按钮显示判断
                showRefundFlg(vo, order);
                //设置可以退款金额
                vo.setAvailableRefundAmount(calculateRefundAmount(order).toPlainString());
                orderInfoVoList.add(vo);
            }

            System.out.println("4-5:userSupportProductIdList" + userSupportProductIdList.size());

            //dubbo调用获取产品信息
            ProductInfoForMyOrderResponse productInfoForMyOrderRsp = userProductDubbo.queryProductForMyOrder(userSupportProductIdList);
            System.out.println("5.dubbo of profuct ：userProductDubbo=" + (userProductDubbo == null));
            System.out.println("5.1.dubbo of profuct ：productInfoForMyOrderRsp=" + (productInfoForMyOrderRsp == null));
            System.out.println("5.2.dubbo of profuct ：productInfoForMyOrderRsp=" + productInfoForMyOrderRsp.getReponseDesc() + "=" + (productInfoForMyOrderRsp.getReponseDesc()));
            if (null == productInfoForMyOrderRsp) {
                logger.error("productInfoForMyOrderRsp is null");
                model.addAttribute("orderInfoList", orderInfoVoList);
                return "order/myOrder.ftl";
            }

            //200	处理成功
            if (productInfoForMyOrderRsp.getResponseCode().endsWith("200")) {
//                Map<Integer, ProductOrderDto> productOrderDtoMap = productInfoForMyOrderRsp.getResultMap();
//                System.out.println("7.productOrderDtoMap"+ (productOrderDtoMap==null));
                Map<String, ProductOrderDto> productOrderDtoMap = new HashMap<String, ProductOrderDto>();
                if (CollectionUtils.isNotEmpty(productInfoForMyOrderRsp.getResultList())) {
                    for (ProductOrderDto dto : productInfoForMyOrderRsp.getResultList()) {
                        if (!productOrderDtoMap.containsKey(dto.getProductId())) {
                            productOrderDtoMap.put(dto.getProductId() + "-" + dto.getProjectId(), dto);
                        }
                    }
                }
                if (productOrderDtoMap != null) {
                    for (OrderInfoVo vo : orderInfoVoList) {
                        ProductOrderDto dto = productOrderDtoMap.get(vo.getProductId() + "-" + vo.getHowBuyId());
                        if (null == dto) {
                            continue;
                        }
                        vo.setProductName(dto.getProductName());
                        vo.setImageUrl(dto.getImageUrl());
                        System.out.println("7.imageurl" + dto.getImageUrl());
                        vo.setProductRate(dto.getProductRate());
                        //支付按钮判断
                        showPaymentButton(dto, vo);
                    }
                }
            } else {
                logger.error(productInfoForMyOrderRsp.getReponseDesc());
            }
            model.addAttribute("orderInfoList", orderInfoVoList);
//            logger.error("productInfoForMyOrderRsp.getResultList().size():" + productInfoForMyOrderRsp.getResultList().size());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return "order/myOrder.ftl";
    }

    private void showPaymentButton(ProductOrderDto dto, OrderInfoVo vo) {
        if (null == dto) {
            return;
        }
        Timestamp startTime = dto.getSalesBeginTime();
        String orderStatus = vo.getOrderStatus();
        if (CommonEnum.UNPAY.equals(orderStatus)) {
            //未支付流程
            if ("Y".equals(dto.getCanBuyFlag())) {
                //产品可购买
                vo.setIsAllPay("1");
                if (dto.getIsAppoint().equals("1") && System.currentTimeMillis() - (86450000 * 7) <= startTime.getTime()) {
                    vo.setIsAppoint("1");
                }
            }
        } else if (CommonEnum.WAIT_BALANCE_PAY.equals(orderStatus)) {
            //等待尾款支付流程
            if (System.currentTimeMillis() - (86450000 * 10) <= startTime.getTime()) {
                vo.setIsBalance("1");
            }
        }
    }

    /**
     * 退款按钮显示判断设定
     *
     * @param vo
     * @param order
     * @return
     */
    private void showRefundFlg(OrderInfoVo vo, OrderInfo order) {

        int prodStatus = -1;
        Date saleEndTm = null;
        String prodTyp = "";
        //产品信息
        ProductInfoForOrderResponse productResponse =
                productDetailDubbo.queryProductDetailsForOrder(Integer.parseInt(order.getProductId()));
        if (null != productResponse) {//判断返回结果集是否成功
            String respCode = productResponse.getResponseCode();//响应结果
            if ("200".equals(respCode)) {//如果查询成功
                prodStatus = productResponse.getProductDto().getProductStatus();  //产品状态
                saleEndTm = productResponse.getProductDto().getSalesEndTime();   //募集结束时间
                prodTyp = productResponse.getProductDto().getProductType();     //产品类型
            } else {
                logger.info("queryProductDetailsForOrder() 取得失败！ order.getProductId():" + order.getProductId());
            }
        } else {
            logger.info("queryProductDetailsForOrder() 取得失败！ order.getProductId():" + order.getProductId());
            return;
        }

        if (prodStatus == 9) { // 9-众筹中
            String orderStatus = order.getOrderStatus(); //订单状态
            String orderType = order.getOrderType(); //订单类型
            if ("03".equals(orderStatus)) { //订单状态：03-等待支付尾款
                vo.setRefundFlg(REFOUND_FLAG_YES);
                return;
            } else if ("04".equals(orderStatus)) { //订单状态：04-支付成功
                if ("01".equals(orderType) || "N".equals(order.getIsOverCollect()) ) {//订单类型：01-普通订单, N-非超募订单
                    if ("10".equals(prodTyp)) { // 股权类
                        Date now = new Date();
                        long between = betweenHours(order.getUpdateTime(), now);
                        //当前时间-支付完成时间<72小时
                        if (between < 72) {
                            long dayHours = betweenHours(order.getUpdateTime(), saleEndTm);
                            //募集结束时间 - 支付完成日 > 5天
                            if (dayHours > (5 * 24)) {
                                vo.setRefundFlg(REFOUND_FLAG_YES);
                                return;
                            }
                        }
                    }
                }
            }
        }
        vo.setRefundFlg(REFOUND_FLAG_NO);
    }

    private static long betweenHours(String endDt, Date beginDt) {
        SimpleDateFormat dfs = new SimpleDateFormat(FORMAT_YYYYMMDDHHMMSS);
        long between = 0;
        try {
            java.util.Date end = dfs.parse(endDt);
            between = (beginDt.getTime() - end.getTime());// 得到两者的毫秒数
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long hours = (between / (60 * 60 * 1000));  //小时数 取整
        return hours;
    }

    private static int betweenDays(String endDt, Date beginDt) {
        SimpleDateFormat dfs = new SimpleDateFormat(FORMAT_YYYYMMDDHHMMSS);
        long between = 0;
        try {
            java.util.Date end = dfs.parse(endDt);
            between = (beginDt.getTime() - end.getTime());// 得到两者的毫秒数

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long days = between / (24 * 60 * 60 * 1000);  // 天数 取整
        return Integer.parseInt(String.valueOf(days));
    }

    /**
     * 计算退款金额
     *
     * @param order
     * @return
     */
    private BigDecimal calculateRefundAmount(OrderInfo order) {
        if ("03".equals(order.getOrderStatus())) {
            logger.info("订单状态：" + order.getOrderStatus() + "退款金额：" + order.getBailAmount());
            return null == order.getBailAmount() ? BigDecimal.ZERO : new BigDecimal(order.getBailAmount());
        } else {
            logger.info("订单状态：" + order.getOrderStatus() + "退款金额：" + order.getOrderAmt());
            return null == order.getOrderAmt() ? BigDecimal.ZERO : new BigDecimal(order.getOrderAmt());
        }
    }

    @RequestMapping(value = "/order/detail", method = RequestMethod.POST)
    public String orderDetail(HttpServletRequest request, Model model, OrderInfoVo orderInfoVo) {
        model.addAttribute("config", applicationPropertyConfig);
        String addressId = null;
        try {
            //获得订单信息
            addressId = getOrderInfo(orderInfoVo.getOrderId(), orderInfoVo.getCustId(), model);

        } catch (Exception e) {
            logger.error("order query exception");
            e.printStackTrace();
        }
        try {
            //ubs
            //获得用户信息,收货信息
            getUserInfo(orderInfoVo.getCustId(), addressId, model);
        } catch (Exception e) {
            logger.error("ubs query exception");
            e.printStackTrace();
        }
        try {
            //pms
            //获得产品信息,回报信息
            getProductInfo(orderInfoVo.getProductId(), orderInfoVo.getHowBuyId(), model);
        } catch (Exception e) {
            logger.error("product query exception");
            e.printStackTrace();
        }
        //trade
        model.addAttribute("orderInfoVo", orderInfoVo);
        return "order/orderDetail.ftl";
    }

    private String getOrderInfo(String orderId, String custId, Model model) throws Exception {
        OrderInfo orderInfo = getOrderInfoFromTs(orderId, custId);
        List<OrderPaybackInfo> payBackList = orderInfo.getPayBackInfoList();

        BigDecimal amt = new BigDecimal(orderInfo.getOrderAmt());
        BigDecimal fee = new BigDecimal(orderInfo.getOrderFee());
        String paymentAmt = orderInfo.getPaymentAmt();
        if (StringUtils.isBlank(paymentAmt)) {
            paymentAmt = "0";
        }
        BigDecimal payment = new BigDecimal(paymentAmt);
        //订单金额
        model.addAttribute("totalAmt", String.format("%.2f ", amt.add(fee)));
        //应付金额
        model.addAttribute("totalFee", String.format("%.2f ", amt.add(fee).subtract(payment)));

        //订单创建时间格式化 20150729233053
        String createTime = orderInfo.getInsertTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date a = sdf.parse(createTime);
        String formatDate = format.format(a);
        orderInfo.setInsertTime(formatDate);

        //电子合同信息
        StringBuffer stringBuffer = new StringBuffer();
        ;
        if (CollectionUtils.isNotEmpty(orderInfo.getContractInfoList())) {
            stringBuffer.append("查看");
            for (TsContractInfo contractInfo : orderInfo.getContractInfoList()) {
                stringBuffer.append("<a href='" + contractInfo.getContractPath() + "'>《" + contractInfo.getContractName() + "》</a>");
            }
        }
        model.addAttribute("contractInfo", stringBuffer.toString());

        model.addAttribute("orderInfo", orderInfo);
        if (CollectionUtils.isNotEmpty(payBackList)) {
            return payBackList.get(0).getAddressId();
        }
        return null;
    }

    private OrderInfo getOrderInfoFromTs(String orderId, String custId) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Map<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put("merId", CommonEnum.MERID);
        valueMap.put("cmdId", CommonEnum.QUERYBYORDERID);
        valueMap.put("version", CommonEnum.VERSION);
        valueMap.put("merPriv", CommonEnum.MERPRIV);
        valueMap.put("orderId", orderId);
        valueMap.put("custId", custId);
        valueMap.put("operId", "");
        valueMap.put("reqExt", CommonEnum.REQEXT);
        valueMap.put("merKey", CommonEnum.MERKEY);
        StringBuffer strBuf = new StringBuffer();
        Iterator<Entry<String, String>> it = valueMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = (Entry<String, String>) it.next();
            strBuf.append(entry.getValue());
        }
        valueMap.put("chkValue", Md5Utils.md5(strBuf.toString(), "UTF-8"));
        logger.info("get order Info request data: " + JSON.toJSONString(valueMap));
        String resultStr = httpClientPost(valueMap);
        QueryByCustIdResult queryByCustIdResult = JSON.parseObject(resultStr, QueryByCustIdResult.class);
        logger.info("get order Info result data: " + JSON.toJSONString(queryByCustIdResult));
        return queryByCustIdResult.getOrderInfo();
    }

    private List<OrderInfo> getOrderInfoList(String operId, String custId, Model model, String curPage) throws Exception {
        PageSet page = new PageSet();
        System.out.println("2= get order list");
        Map<String, String> valueMap = new LinkedHashMap<String, String>();

        valueMap.put("merId", CommonEnum.MERID);
        valueMap.put("cmdId", CommonEnum.QUERYBYCUSTID);
        valueMap.put("version", CommonEnum.VERSION);
        valueMap.put("merPriv", CommonEnum.MERPRIV);
        valueMap.put("custId", custId);
        valueMap.put("operId", operId);
        if (StringUtils.isEmpty(curPage) || StringUtils.equals("1", curPage)) {
            valueMap.put("currentPage", "1");
        } else {
            valueMap.put("currentPage", curPage);
        }
        valueMap.put("pageSize", rowSize);
        valueMap.put("reqExt", CommonEnum.REQEXT);
        valueMap.put("merKey", CommonEnum.MERKEY);

        StringBuffer strBuf = new StringBuffer();
        Iterator<Entry<String, String>> it = valueMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = (Entry<String, String>) it.next();
            strBuf.append(entry.getValue());
        }
        valueMap.put("chkValue", Md5Utils.md5(strBuf.toString(), "UTF-8"));

        String resultStr = httpClientPost(valueMap);

        QueryByCustIdResultList queryByCustIdResultList = JSON.parseObject(resultStr, QueryByCustIdResultList.class);
        System.out.println("3 call order center get order result:" + resultStr);
        List<OrderInfo> orderInfoList = queryByCustIdResultList.getOrderInfoList();
        if (CollectionUtils.isNotEmpty(orderInfoList)) {
            page.setRowCount(Integer.valueOf(queryByCustIdResultList.getTotalRecord()));
            if (StringUtils.isNotEmpty(curPage)) {
                page.setCurPage(Integer.valueOf(curPage));
            }
            page.countMaxPage();
        }
        model.addAttribute("page", page);
//		model.addAttribute("orderInfoList", orderInfoList);
        return orderInfoList;
    }

    /**
     * 通过httpclient方式调用订单中心
     *
     * @param params
     * @return
     */
    @SuppressWarnings({"resource", "deprecation"})
    private String httpClientPost(Map<String, String> params) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(applicationPropertyConfig.getTs());
            List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
            for (String key : params.keySet()) {
                String v = params.get(key);
                list.add(new BasicNameValuePair(key, v));
            }
            UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(list);
            postEntity.setContentEncoding("UTF-8");
            httpPost.setEntity(postEntity);
            HttpResponse clientResponse = httpClient.execute(httpPost);
            HttpEntity entity = clientResponse.getEntity();
            return IOUtils.toString(entity.getContent(), "UTF-8");
        } catch (Exception e) {
            logger.error("query trade info failed");
            e.printStackTrace();
        }
        return null;
    }


    private void getUserInfo(String custId, String addressId, Model model) throws Exception {
//		custId="20150624100336";
        //获得用户信息
        UserInfoFacade userInfoFacade = (UserInfoFacade) serviceClientFactory.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");
        IdInfoRequest queryReq = new IdInfoRequest();//查询地址请求
        queryReq.setCustId(custId);
        queryReq.setConsumerId(ubsConsumerId);
        IdInfoResult queryResult = userInfoFacade.queryUserByCustId(queryReq);//调用查询地址接口
        if (null != queryResult) {//判断返回结果集是否成功
            List<UserInfoDto> list = queryResult.getList();//结果集
            String respCode = queryResult.getRespCode();//响应结果
            if ("000".equals(respCode)) {//如果查询成功
                if (CollectionUtils.isNotEmpty(list)) {
                    model.addAttribute("userInfoDto", list.get(0));
                }
            }
        }
        if (addressId != null) {
            //获得收货信息
            AddressInfoFacade addressInfoFacade = (AddressInfoFacade) serviceClientFactory.getWebServiceClient(AddressInfoFacade.class, "addressInfoFacade", "UBS");
            AddressInfoRequest addReq = new AddressInfoRequest();//查询地址请求
            addReq.setCustId(custId);
            addReq.setConsumerId(ubsConsumerId);
            AddressInfoResult addressResult = addressInfoFacade.queryRecAddress(addReq);//调用查询地址接口
            if (null != addressResult) {//判断返回结果集是否成功
                List<AddressInfoDto> list = addressResult.getAddressInfoList();//结果集
                String respCode = addressResult.getRespCode();//响应结果
                if ("000".equals(respCode)) {//如果查询成功
                    if (CollectionUtils.isNotEmpty(list)) {
                        AddressInfoDto address = null;
                        for (int i = 0; i < list.size(); i++) {
                            if (addressId.equals(list.get(i).getAddressId().toString())) {
                                address = list.get(i);
                            }
                        }
                        if (null != address) {
                            String telephone = address.getTelephone();//手机
                            String phone = address.getPhone();//固话
                            if (StringUtils.isBlank(telephone)) {
                                address.setTelephone(phone);
                            }
                        }
                        model.addAttribute("addressInfoDto", address);
                    }
                }
            }
        }
    }

    private void getProductInfo(String productId, String projectId, Model model) throws Exception {
        System.out.println("start query productId");
        //产品信息
        ProductInfoForMyOrderResponse productInfoForMyOrderRsp = userProductDubbo.queryProductDetailForMyOrder(Integer.parseInt(productId), Integer.parseInt(projectId));
        if (null != productInfoForMyOrderRsp) {//判断返回结果集是否成功
            String respCode = productInfoForMyOrderRsp.getResponseCode();//响应结果
            if ("200".equals(respCode)) {//如果查询成功
                model.addAttribute("projectDto", productInfoForMyOrderRsp.getResultMap().get(Integer.parseInt(productId)));
            }
        }
    }


    /**
     * 订单退款
     * refundAmt: refundAmt,
     * refundNum: refundNum,
     * orderId: orderId
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/refundOrder.do", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject refundOrder(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            String refundAmt = request.getParameter("refundAmt");
            String refundNum = request.getParameter("refundNum");
            String orderId = request.getParameter("orderId");
            User user = userSessionFacade.getUser(request);
            String custId = user.getCustId();
            String operId = user.getOperId();
            OrderRefundResult orderRefundResult = refundOrder(custId, operId, orderId, refundAmt, refundNum);
            if ("000".equals(orderRefundResult.getRespCode())) {
                jsonObject.put("success", true);
                jsonObject.put("message", "success");
                return jsonObject;
            } else {
                jsonObject.put("success", false);
                jsonObject.put("message", orderRefundResult.getRespDesc());
                return jsonObject;
            }
        } catch (Exception e) {
            jsonObject.put("success", false);
            jsonObject.put("message", "服务器异常，请稍后再试！");
        }
        return jsonObject;
    }

    private OrderRefundResult refundOrder(String custId, String operId, String orderId, String refundAmt, String refundNum) {

        Map<String, String> valueMap = new LinkedHashMap<String, String>();

        valueMap.put("merId", CommonEnum.MERID);
        valueMap.put("cmdId", CommonEnum.ORDER_REFUND);
        valueMap.put("version", CommonEnum.VERSION);
        valueMap.put("merPriv", CommonEnum.MERPRIV);
        valueMap.put("custId", custId);
        valueMap.put("operId", operId);
        valueMap.put("orderId", orderId);
        valueMap.put("refundAmt", refundAmt);
        valueMap.put("refundNum", refundNum);
        valueMap.put("reason", "不买了");
        valueMap.put("remark", "");
        valueMap.put("reqExt", CommonEnum.REQEXT);
        valueMap.put("merKey", CommonEnum.MERKEY);

        String chkValue = Md5Utils.md5(CommonEnum.MERID + CommonEnum.ORDER_REFUND + CommonEnum.VERSION + CommonEnum.MERPRIV
                + orderId + custId + operId + refundAmt + refundNum + CommonEnum.REQEXT + CommonEnum.MERKEY);
        valueMap.put("chkValue", chkValue);
        logger.info("refund request data: " + JSON.toJSONString(valueMap));
        String resultStr = httpClientPost(valueMap);
        OrderRefundResult orderRefundResult = JSON.parseObject(resultStr, OrderRefundResult.class);
        logger.info("refund result data: " + JSON.toJSONString(orderRefundResult));
        return orderRefundResult;
    }

    /**
     * 取消订单
     * orderId: orderId
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/cancelOrder.do", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject cancelOrder(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            String orderId = request.getParameter("orderId");
            User user = userSessionFacade.getUser(request);
            String custId = user.getCustId();
            String operId = user.getOperId();
//            String custId = "20150624100336";
//            String operId = "111";
            OrderCancelResult orderCancelResult = cancelOrder(custId, operId, orderId);
            if ("000".equals(orderCancelResult.getRespCode())) {
                jsonObject.put("success", true);
                jsonObject.put("message", "success");
                return jsonObject;
            } else {
                jsonObject.put("success", false);
                jsonObject.put("message", orderCancelResult.getRespDesc());
                return jsonObject;
            }
        } catch (Exception e) {
            jsonObject.put("success", false);
            jsonObject.put("message", "服务器异常，请稍后再试！");
        }
        return jsonObject;
    }

    private OrderCancelResult cancelOrder(String custId, String operId, String orderId) {

        Map<String, String> valueMap = new LinkedHashMap<String, String>();

        valueMap.put("merId", CommonEnum.MERID);
        valueMap.put("cmdId", CommonEnum.ORDER_CANCEL);
        valueMap.put("version", CommonEnum.VERSION);
        valueMap.put("merPriv", CommonEnum.MERPRIV);
        valueMap.put("custId", custId);
        valueMap.put("operId", operId);
        valueMap.put("orderId", orderId);
        valueMap.put("reqExt", CommonEnum.REQEXT);
//        valueMap.put("merKey", CommonEnum.MERKEY);

        String chkValue = Md5Utils.md5(CommonEnum.MERID + CommonEnum.ORDER_CANCEL + CommonEnum.VERSION + CommonEnum.MERPRIV
                + orderId + custId + operId + CommonEnum.REQEXT + CommonEnum.MERKEY);
        valueMap.put("chkValue", chkValue);
        logger.info("cancelOrder request data: " + JSON.toJSONString(valueMap));
        String resultStr = httpClientPost(valueMap);
        OrderCancelResult orderCancelResult = JSON.parseObject(resultStr, OrderCancelResult.class);
        logger.info("cancelOrder result data: " + JSON.toJSONString(orderCancelResult));
        return orderCancelResult;
    }

    /**
     * 判断订单有没有超时关闭
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/judgeOrder.do", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject judgeOrder(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String orderId = request.getParameter("orderId");
        User user = null;
        try {
            user = userSessionFacade.getUser(request);
            String custId = user.getCustId();
            OrderInfo orderInfo = getOrderInfoFromTs(orderId, custId);
            if (null != orderInfo) {
                jsonObject.put("orderStatus", orderInfo.getOrderStatus());
                jsonObject.put("success", true);
                return jsonObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Get session user error: " + e.getLocalizedMessage());
        }
        jsonObject.put("message", "服务器异常，请联系管理员！");
        jsonObject.put("success", false);
        return jsonObject;
    }
}
