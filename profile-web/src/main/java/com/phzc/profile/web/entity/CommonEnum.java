package com.phzc.profile.web.entity;

public interface CommonEnum {
	
	public static final String QUERYBYORDERID = "queryByOrderId";
	public static final String QUERYBYCUSTID = "queryByCustId";
	public static final String ORDER_REFUND = "orderRefund";
	public static final String ORDER_CANCEL = "orderCancel";
	public static final String CREATEORDER = "createOrder";
	public static final String CREATEPREORDER = "createPreOrder";
	public static final String ORDERPAY = "orderPay";
	public static final String SENDMESSAGE2EL = "sendMessage2EL";
	public static final String MERID ="site";
	public static final String VERSION = "10";
	public static final String MERPRIV = "";
	public static final String REQEXT = "";
	public static final String OPERID = "000001";
	public static final String MERKEY = "TS2015";
	public static final String ACCOUNTBASEDA = "BASEDA";//基本借记户	
	public static final String ACCOUNTSPEDA = "SPEDA";//专用账户	
	public static final String ACCOUNTPRODDA = "PRODDA";//产品账户    
	public static final String ACCOUNTPA = "PA"; //积分账户    
	public static final String ACCOUNTINT = "INT"; //内部账户
	public static final String ACCOUNT_JJ = "D";// – 借记账户
	public static final String ACCOUNT_DJ = "C";// – 贷记账户
	public static final String PAYCHNL_YUEZHIFU = "10";//10-余额支付
	public static final String PAYCHNL_PINGANFU = "11";//11-平安付
	public static final String PAYCHNL_TONGLIAN = "12";//12-通联支付
	public static final String PAYCHNL_YILIAN = "13";//13-易联支付
	public static final String PAYCHNL_YIBAO = "14";//14-易宝支付
	public static final String ONLINEPAY="01";//在线支付
	public static final String SYSID = "60012";//-网站PC端
	public static final String BEDPID= "phzc";//业务部门代号，默认phzc
	public static final String VALIDSTATE_NEVER = "U";//未验证
	public static final String VALIDSTATE_FAIL = "F";//验证失败
	public static final String VALIDSTATE_SUCC = "S";//验证成功
	
	public static final String UNPAY = "01";//未支付
	public static final String WAIT_BALANCE_PAY = "03";//等待支付尾款
	
	public static enum PaymentAmtType{
		PAYMENTAMTTYPE_APPOINTAMT("01","预约金"),
		PAYMENTAMTTYPE_TAILAMT("02","尾款"),
		PAYMENTAMTTYPE_TOTALAMT("03","订单全额");
		
		private final String code;
		private final String desc;
		
		public String getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}
		private PaymentAmtType(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		public static String getDescByCode(String selectCode) {
			for(int i = 0; i < PaymentAmtType.values().length; i++){
				if(PaymentAmtType.values()[i].code.equals(selectCode)){
					return PaymentAmtType.values()[i].getDesc();
				}
			}
			return "";
		}
	}
	
	public static enum BankImage{
		GUANGDABANK("CGB","bank_01.png"),
		HUAXIABANK("HXB","bank_02.png"),
		JIANSHEBANK("CCB","bank_03.png"),
		PINGANBANK("PINGAN","bank_04.png"),
		PUFABANK("SPDB","bank_05.png"),
		XINGYEBANK("CIB","bank_06.png"),
		YOUZHENGBANK("PSBC","bank_07.png"),
		ZHAOSHANGBANK("CMB","bank_08.png"),
		ZHONGGUOBANK("BOC","bank_09.png"),
		ZHONGXINBANK("ECITIC","bank_10.png");
		
		private final String code;
		private final String desc;
		
		public String getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}
		private BankImage(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		public static String getDescByCode(String selectCode) {
			for(int i = 0; i < BankImage.values().length; i++){
				if(BankImage.values()[i].code.equals(selectCode)){
					return BankImage.values()[i].getDesc();
				}
			}
			return "";
		}
	}
	
	public static enum BailFlag{
		BAILFLAG_YES("1","支持 "),
		BAILFLAG_NO("2","不支持");

		private final String code;
		private final String desc;
		
		public String getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}
		private BailFlag(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		
		public static String getDescByCode(String selectCode) {
			for(int i = 0; i < BailFlag.values().length; i++){
				if(BailFlag.values()[i].code .equals(selectCode)){
					return BailFlag.values()[i].getDesc();
				}
			}
			return "";
		}
	}
	
	public static enum BailCalType{
		BAILCALTYPE_RATE("1","按比例 "),
		BAILCALTYPE_AMOUNT("2","定额");

		private final String code;
		private final String desc;
		
		public String getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}
		private BailCalType(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		
		public static String getDescByCode(String selectCode) {
			for(int i = 0; i < BailCalType.values().length; i++){
				if(BailCalType.values()[i].code .equals(selectCode)){
					return BailCalType.values()[i].getDesc();
				}
			}
			return "";
		}
	}
	
	public static enum ProductType{
		ProductType_GQ("10","股权众筹"),
		ProductType_GY("20","公益众筹"),
		ProductType_XF("30","消费众筹"),
		ProductType_ZQ("40","债权众筹"),
		ProductType_SW("50","实物众筹"),
		ProductType_WH("60","文化众筹");

		private final String code;
		private final String desc;
		
		public String getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}
		private ProductType(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		
		public static String getDescByCode(String selectCode) {
			for(int i = 0; i < ProductType.values().length; i++){
				if(ProductType.values()[i].code .equals(selectCode)){
					return ProductType.values()[i].getDesc();
				}
			}
			return "";
		}
	}
	
	public static enum FinanceType{
		APPOINT_FINANCE("01","预约金"),
		TAIL_FINANCE("02","尾款金额"),
		ORDER_FINANCE("03","订单全额");
		
		private final String code;
		private final String desc;
		
		public String getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}
		private FinanceType(String code, String desc) {
			this.code = code;
			this.desc =desc;
		}
		
		public static String getDescByCode(String selectCode) {
			for(int i = 0; i < FinanceType.values().length; i++){
				if(FinanceType.values()[i].code .equals(selectCode)){
					return FinanceType.values()[i].getDesc();
				}
			}
			return "";
		}
	}
	
	public static enum OrderType{
		NOMARORDER("01","普通订单"),
		REWARDORDER("02","打赏订单");
		
		private final String code;
		private final String desc;
		
		public String getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}
		private OrderType(String code, String desc) {
			this.code = code;
			this.desc =desc;
		}
		
		public static String getDescByCode(String selectCode) {
			for(int i = 0; i < OrderType.values().length; i++){
				if(OrderType.values()[i].code .equals(selectCode)){
					return OrderType.values()[i].getDesc();
				}
			}
			return "";
		}
	}
	
	
	/**
	 * 产品状态枚举值
	 * @author liuhaichen659
	 *
	 */
	public static enum ProductStatus{
		PRODUCTSTATUS_DEFAULT(1,"初始"),
		PRODUCTSTATUS_WAITSUBMIT(2,"待提交"),
		PRODUCTSTATUS_WAITREVIEW(3,"待审核"),
		PRODUCTSTATUS_REVIEWING(4,"审核中"),
		PRODUCTSTATUS_REVIEWEDSUCC(5,"审核通过"),
		PRODUCTSTATUS_REVIEWEDFAIL(6,"审核未通过"),
		PRODUCTSTATUS_BEFOREAPPOINT(7,"预热中"),
		PRODUCTSTATUS_APPOINTING(8,"预约中"),
		PRODUCTSTATUS_EXECTING(9,"众筹中"),
		PRODUCTSTATUS_EXECTEFAIL(10,"众筹失败"),
		PRODUCTSTATUS_EXECTESUCC(11,"众筹成功"),
		PRODUCTSTATUS_PRODUCTSUCC(12,"项目成功"),
		PRODUCTSTATUS_PRODUCTFAIL(13,"项目失败"),
		PRODUCTSTATUS_ONLINEWAITREVIEW(14,"上线后待审核"),
		PRODUCTSTATUS_SUCCOVER(15,"成功结束"),
		PRODUCTSTATUS_FAILOVER(16,"失败结束"),
		PRODUCTSTATUS_OFFLINE(17,"下架");
		
		private final Integer code;
		private final String desc;
		
		public Integer getCode() {
			return code;
		}
		
		public String getDesc() {
			return desc;
		}

		public static String getDescByCode(Integer selectCode) {
			for(int i = 0; i < ProductStatus.values().length; i++){
				if(ProductStatus.values()[i].code == selectCode){
					return ProductStatus.values()[i].getDesc();
				}
			}
			return "";
		}
		
		private ProductStatus(Integer code, String desc) {
			this.code = code;
			this.desc = desc;
		}
	}
	
}
