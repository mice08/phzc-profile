package com.phzc.profile.web.form;

import com.phzc.profile.web.dto.OrderPaybackInfo;

import java.io.Serializable;
import java.util.List;

public class OrderInfoVo implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 6315661207722699567L;

    private String imageUrl;

    private String updateTime;//订单更新时间

    private String productName;//产品名称

    private int productRate;

    private String orderId;//订单号
	
	private String custId;//用户Id
	
	private String productId;//产品Id
	
	private String howBuyId;//方案Id
	
	private String orderType;//订单类型
	
	private String orderStatus;//订单状态
	
	private String orderAmt;//订单金额
	
	private String orderNum;//订单份额
	
	private String orderFee;//运费
	
	private String totalOnlineAmt;//线上实付总金额
	
	private String totalOfflineAmt;//线下实付总金额
	
	private String paymentAmt;//总计实付金额
	
	private String totalRefundAmt;//总计退款金额
	
	private String bailAmount;//保证金
	
	private String bailTime;//保证金支付时间
	
	private String isOverCollect;//是否超募
	
	private String isOfflinePay;//是否补单
	
	private String isMobileCom;//是否电话沟通
	
	private String IsConsoleEntry;//是否控台录入
	
	private String isPaymentRegister;//是否资产登记
	
	private String isRefundSettle;//是否退款清算

	private String insertTime;//创建时间
	
	private String sysId;//渠道来源
	
	private String refundFlg = "0";  //退款标识   0-不退款，1-退款
	
    private List<OrderPaybackInfo> payBackInfoList;//订单详情信息列表
    
    private String isAppoint="0";//预约金按钮标志 0不显示,1显示
    
    private String isAllPay="0";//全款支付按钮标志
    
    private String isBalance="0";//尾款按钮标志

	private String availableRefundAmount;//允许退款金额
	
	public String getIsAllPay() {
		return isAllPay;
	}
	public void setIsAllPay(String isAllPay) {
		this.isAllPay = isAllPay;
	}
	public String getIsBalance() {
		return isBalance;
	}
	public void setIsBalance(String isBalance) {
		this.isBalance = isBalance;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getHowBuyId() {
		return howBuyId;
	}
	public void setHowBuyId(String howBuyId) {
		this.howBuyId = howBuyId;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderAmt() {
		return orderAmt;
	}
	public void setOrderAmt(String orderAmt) {
		this.orderAmt = orderAmt;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getOrderFee() {
		return orderFee;
	}
	public void setOrderFee(String orderFee) {
		this.orderFee = orderFee;
	}
	public String getTotalOnlineAmt() {
		return totalOnlineAmt;
	}
	public void setTotalOnlineAmt(String totalOnlineAmt) {
		this.totalOnlineAmt = totalOnlineAmt;
	}
	public String getTotalOfflineAmt() {
		return totalOfflineAmt;
	}
	public void setTotalOfflineAmt(String totalOfflineAmt) {
		this.totalOfflineAmt = totalOfflineAmt;
	}
	public String getPaymentAmt() {
		return paymentAmt;
	}
	public void setPaymentAmt(String paymentAmt) {
		this.paymentAmt = paymentAmt;
	}
	public String getTotalRefundAmt() {
		return totalRefundAmt;
	}
	public void setTotalRefundAmt(String totalRefundAmt) {
		this.totalRefundAmt = totalRefundAmt;
	}
	public String getBailAmount() {
		return bailAmount;
	}
	public void setBailAmount(String bailAmount) {
		this.bailAmount = bailAmount;
	}
	public String getBailTime() {
		return bailTime;
	}
	public void setBailTime(String bailTime) {
		this.bailTime = bailTime;
	}
	public String getIsOverCollect() {
		return isOverCollect;
	}
	public void setIsOverCollect(String isOverCollect) {
		this.isOverCollect = isOverCollect;
	}
	public String getIsOfflinePay() {
		return isOfflinePay;
	}
	public void setIsOfflinePay(String isOfflinePay) {
		this.isOfflinePay = isOfflinePay;
	}
	public String getIsMobileCom() {
		return isMobileCom;
	}
	public void setIsMobileCom(String isMobileCom) {
		this.isMobileCom = isMobileCom;
	}
	public String getIsConsoleEntry() {
		return IsConsoleEntry;
	}
	public void setIsConsoleEntry(String isConsoleEntry) {
		IsConsoleEntry = isConsoleEntry;
	}
	public String getIsPaymentRegister() {
		return isPaymentRegister;
	}
	public void setIsPaymentRegister(String isPaymentRegister) {
		this.isPaymentRegister = isPaymentRegister;
	}
	public String getIsRefundSettle() {
		return isRefundSettle;
	}
	public void setIsRefundSettle(String isRefundSettle) {
		this.isRefundSettle = isRefundSettle;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}
	public String getSysId() {
		return sysId;
	}
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	public List<OrderPaybackInfo> getPayBackInfoList() {
		return payBackInfoList;
	}
	public void setPayBackInfoList(List<OrderPaybackInfo> payBackInfoList) {
		this.payBackInfoList = payBackInfoList;
	}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getProductRate() {
        return productRate;
    }

    public void setProductRate(int productRate) {
        this.productRate = productRate;
    }

    public String getOverCollect() {
        return isOverCollect;
    }

    public void setOverCollect(String overCollect) {
        isOverCollect = overCollect;
    }

    public String getOfflinePay() {
        return isOfflinePay;
    }

    public void setOfflinePay(String offlinePay) {
        isOfflinePay = offlinePay;
    }

    public String getMobileCom() {
        return isMobileCom;
    }

    public void setMobileCom(String mobileCom) {
        isMobileCom = mobileCom;
    }

    public String getPaymentRegister() {
        return isPaymentRegister;
    }

    public void setPaymentRegister(String paymentRegister) {
        isPaymentRegister = paymentRegister;
    }

    public String getRefundSettle() {
        return isRefundSettle;
    }

    public void setRefundSettle(String refundSettle) {
        isRefundSettle = refundSettle;
    }
    
    public String getRefundFlg() {
        return refundFlg;
    }
    public void setRefundFlg(String refundFlg) {
        this.refundFlg = refundFlg;
    }
	public String getIsAppoint() {
		return isAppoint;
	}
	public void setIsAppoint(String isAppoint) {
		this.isAppoint = isAppoint;
	}

	public String getAvailableRefundAmount() {
		return availableRefundAmount;
	}

	public void setAvailableRefundAmount(String availableRefundAmount) {
		this.availableRefundAmount = availableRefundAmount;
	}
}
