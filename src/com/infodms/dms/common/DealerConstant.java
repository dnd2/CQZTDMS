package com.infodms.dms.common;

public interface DealerConstant
{	
	//销售类型
	public static String SALES_TYPE = "4266";
	public static final int SALES_TYPE_NORMAL = 42661001;
	public static final int SALES_TYPE_FLEET = 42661002;
	
	//问卷类型
	public static String QUESTIONNAIRE_TYPE = "4308";
	
	//答案类型
	public static String QUESTION_TYPE = "4307";
	
	//战败结果
	public static String LOSE_RESULT = "4262";
	public static String LOSE_RESULT_01 = "42621001";//失联
	public static String LOSE_RESULT_02 = "42621002";//放弃
	public static String LOSE_RESULT_03 = "42621003";//购买竞品
	
	//客户状态
	public static String CUSTOMER_STATUS_A = "4243";
	public static String CUSTOMER_STATUS_A_A = "42431001";//潜在客户
	public static String CUSTOMER_STATUS_A_B = "42431002";//基盘客户
	public static String CUSTOMER_STATUS_A_C = "42431003";//战败客户
	
	//接触类型
	public static String DATABASE_CUSTOMER_STATUS = "4225";
	
	//接触方式
	public static String CONNECT_MODE = "4227";
	
	//与车辆使用者关系(公司)
	public static String LINKMAN_CUSTOMER_RELATION1 = "4219";
	//与车辆使用者关系(个人)
	public static String LINKMAN_CUSTOMER_RELATION2 = "4220";
	
	//预计来店动作
	public static String PLAN_ACTIVITY = "4216";
	//客户第一来源
	public static String CUSTOMER_SOURCE = "4201";
	public static String CUSTOMER_SOURCE_10 = "42011010";
	
	//信息渠道
	public static String INFORMATION_CHANNELS = "4203";
	
	//行业分类
    public static String INDUSTRY_CATEGORY = "4213";
    
    //联系人职务
    public static String LINKMAN_POST = "4214";
    
    //婚否
    public static String LINKMAN_MARRAY_STATUS = "4205";
    
    //车辆用途
    public static String VEHILCE_USED = "4210";
    
    //购车形态
    public static String BUY_PROPERTY = "4211";
    
    //购买方式
    public static String BUY_MODE = "4244";
    
    //是否置换车
    public static String BUY_EXCHANGE = "4245";
    
    //数据库潜客状态
    public static String CUSTOMER_STATUS = "4222";
    
    //接触类型
    public static String IS_FIRST = "4224";
    
    //接触结果
    public static String CONTACT_RESULT = "4226";
    
	public static String codeJsUrl = ""; // 字典表数据js的url

	public static String regionJsUrl = ""; // 省份城市表数据js的url

	public static String LOGON_USER = "LOGON_USER"; // session设置登录用户

	public static Integer PAGE_SIZE = 10; // pageSize

	public static int errorNum = 50;
	
	//系统公用功能
	public static final String COMMON_URI = "/common";
	
	public static final String Login_Function_Id = "10000000";
	
	//数据校验长度常量
	public static final int Length_Check_Char_1 = 1;		//长度不超过1，即数据库长度为1
	
	public static final int Length_Check_Char_6 = 6;		//长度不超过6，即数据库长度为20或者邮编的长度校验
	
	public static final int Length_Check_Char_8 = 8;		//整数长度不超过8，对应数据库长度为（10,2），小数长度最多为2
	
	public static final int Length_Check_Char_10 = 10;		//长度不超过10，数据库长度为30
	
	public static final int Length_Check_Char_12 = 12;		//长度不超过12,认证号校验
	
	public static final int Length_Check_Char_15 = 15;		//长度不超过15，数据库长度为50
	
	public static final int Length_Check_Char_17 = 17;		//长度不超过17，VIN校验
	
	public static final int Length_Check_Char_20 = 20;		//长度不超过20，数据库长度为60
	
	public static final int Length_Check_Char_30 = 30;		//长度不超过30，数据库长度为100
	
	public static final int Length_Check_Char_50 = 50;		//长度不超过50，数据库长度为150
	
	public static final int Length_Check_Char_60 = 60;		//长度不超过60，数据库长度为200
	
	public static final int Length_Check_Char_100 = 100;	//长度不超过100，数据库长度为300
	
	/**
	 * 数据权限类别(经销商) - 本人
	 */
	public static final Long DLR_BR = new Long(1000000001);
	
	/**
	 * 数据权限类别(经销商) - 本组织及以下
	 */
	public static final Long DRL_BZZJYX = new Long(1000000002);
	
	/**
	 * 数据权限类别(经销商) - 整个经销商
	 */
	public static final Long DRL_ZGJXS = new Long(1000000003);
	
	/**
	 * 数据权限类别(车厂) - 本组织及以下
	 */
	public static final Long SGM_BZZJYX = new Long(1000000004);
	
	/**
	 * 数据权限类别(车厂) - 整个SGM经销商
	 */
	public static final Long SGM_ZGJXS = new Long(1000000005);
	
	/**
	 * 数据权限类别(车厂) - 整个SGM经销商(不含大卖场)
	 */
	public static final Long SGM_ZGJXSNODMC = new Long(1000000006);

	// 状态
	public static Integer STATUS = 1001; // 状态类型

	public static Integer STATUS_ENABLE = 10011001; // 有效

	public static Integer STATUS_DISABLE = 10011002; // 无效

	public static Integer SYS_USER = 1002; // 用户类型

	public static Integer SYS_USER_SGM = 10021001; // 用户类型主机厂端

	public static Integer SYS_USER_DEALER = 10021002; // 用户类型经销商端

	// 性别
	public static Integer GENDER_TYPE = 1003;

	public static final Integer MAN = 1003001;
	
	public static final Integer WOMEN = 1003002;
	
	public static final Integer NONO = 1003003;

	// 意向级别
	public static Integer INTENTION_LEVEL = 2009;

	public static Integer INTENTION_LEVEL_H = 20091001; // H

	public static Integer INTENTION_LEVEL_A = 20091002; // A

	public static Integer INTENTION_LEVEL_B = 20091003; // B

	public static Integer INTENTION_LEVEL_C = 20091004; // C

	public static Integer INTENTION_LEVEL_F = 20091005; // F

	public static Integer INTENTION_LEVEL_O = 20091006; // O

	// 来访意向
	public static String INTENTION_TYPE = "2010";

	public static String INTENTION_TYPE_BUY = "20101001"; // 收购-卖车

	public static String INTENTION_TYPE_SALE = "20101002"; // 销售-买车

	public static String INTENTION_TYPE_OLD_NEW = "20101003"; // 以旧换新

	public static String INTENTION_TYPE_OLD_OLD = "20101004"; // 依旧换旧

	// 来访方式
	public static String VISIT_WAY = "2011";

	public static String VISIT_WAY_INITIATIVE = "20111001"; // 主动来店

	public static String VISIT_WAY_DATE = "20111002"; // 邀约来店

	public static String VISIT_WAY_INTRO_NEWCAR = "20111003"; // 新车部引荐

	public static String VISIT_WAY_PHONE = "20111004"; // 来电

	// 处理结果
	public static String ACCEPT_RES = "2012";

	public static String ACCEPT_RES_UNACTION = "20121001"; // 未处理

	public static String ACCEPT_RES_ACCEPT = "20121002"; // 已处理

	public static String ACCEPT_RES_ABATE = "20121003"; // 已失效

	// 关键职位业务类型	
	public static String POSSITION_TYPE_EVALUATE = "20601001"; //评估师
	
	public static Integer POSSITION_TYPE_SALER = 20601002; //销售顾问
	
	public static Integer POSSITION_TYPE_WAREHOUSE_ADMIN = 20601003; //仓库管理员
	
	public static Integer POSSITION_TYPE_DIGHT = 20601004; //整备员


	// 公共-是否
	public static String IF_TYPE = "1004";

	public static Integer IF_TYPE_YES = 10041001; // 是

	public static Integer IF_TYPE_NO = 10041002; // 否

	// 整备状态
	public static Integer REFITTING_TYPE = 2030;

	public static Integer REFITTING_TYPE_BEGIN = 20301001; // 未整备

	public static Integer REFITTING_TYPE_MIDDLE = 20301002; // 整备中

	public static Integer REFITTING_TYPE_END = 20301003; // 整备完成

	// 特殊车辆类型
	public static Integer SPECIALCAR_TYPE = 2020;

	// 关怀结果
	public static Integer CARE_RESULT = 2046; // 已执行

	public static Integer CARE_RESULT_Y = 20461001; // 已执行

	public static Integer CARE_RESULT_N = 20461002; // 未执行

	// 关怀类型
	public static String CARE_TYPE = "2008";

	public static String CARE_TYPE_THANK = "20081001"; // 感谢函邮寄

	public static String CARE_TYPE_TRANSFER = "20081002"; // 过户完成通知

	public static String CARE_TYPE_INSURANCE = "20081003"; // 保险提醒

	public static String CARE_TYPE_MAINTENANCE = "20081004"; // 保养提醒

	public static String CARE_TYPE_CUSTOM = "20081005"; // 其它
	
	public static String CARE_TYPE_YEARCHECK = "20081006"; // 年检提醒

	// 客户类型
	public static String CUST_TYPE = "2045";

	public static String CUST_TYPE_INDIVIDUAL = "20451001"; // 个人

	public static String CUST_TYPE_CORP = "20451002"; // 公司

	// 车辆认证状态
	public static String CERTIFICATION_STATUS = "2019";

	public static String CERTIFICATION_STATUS_NOT = "20191001"; // 未认证

	public static String CERTIFICATION_STATUS_PRO = "20191002"; // 认证中

	public static String CERTIFICATION_STATUS_YES = "20191003"; // 认证完成

	public static String CERTIFICATION_STATUS_INV = "20191004"; // 认证失效

	// 认证申请单状态
	public static String CERTORDER_STATUS = "2047";

	public static String CERTORDER_STATUS_SUBMIT = "20471001"; // 已提交

	public static String CERTORDER_STATUS_PASS = "20471002"; // 已审批

	public static String CERTORDER_STATUS_BACK = "20471003"; // 已驳回

	public static String CERTORDER_STATUS_REJECT = "20471004"; // 已拒绝

	// 图片类型
	public static String PICTURE_TYPE = "2031";

	// 车辆信息
	// 左前45度
	public static String VEHICLE_FRONT_BIG = "20311001"; // 左前45度-大

	public static String VEHICLE_FRONT_MID = "20311012"; // 左前45度-中

	public static String VEHICLE_FRONT_SMA = "20311023"; // 左前45度-小

	// 右后45度角
	public static String VEHICLE_BACK_BIG = "20311002"; // 右后45度-大

	public static String VEHICLE_BACK_MID = "20311013"; // 右后45度-中

	public static String VEHICLE_BACK_SMA = "20311024"; // 右后45度-小

	// 发动机舱
	public static String VEHICLE_ENGIN_BIG = "20311003"; // 发动机舱-大

	public static String VEHICLE_ENGIN_MID = "20311014"; // 发动机舱-中

	public static String VEHICLE_ENGIN_SMA = "20311025"; // 发动机舱-小

	// 内饰前仪表台
	public static String VEHICLE_METER_BIG = "20311004"; // 内饰前仪表台-大

	public static String VEHICLE_METER_MID = "20311015"; // 内饰前仪表台-中

	public static String VEHICLE_METER_SMA = "20311026"; // 内饰前仪表台-小

	// 内饰后座椅
	public static String VEHICLE_TRIM_BIG = "20311005"; // 内饰后座椅-大

	public static String VEHICLE_TRIM_MID = "20311016"; // 内饰后座椅-中

	public static String VEHICLE_TRIM_SMA = "20311027"; // 内饰后座椅-小

	// 后备箱
	public static String VEHICLE_CARRIAGE_BIG = "20311006"; // 后备箱-大

	public static String VEHICLE_CARRIAGE_MID = "20311017"; // 后备箱-中

	public static String VEHICLE_CARRIAGE_SMA = "20311028"; // 后备箱-小

	// 认证申请单
	public static String VEHICLE_INSPECT1_BIG = "20311007"; // 106项检测1-大

	public static String VEHICLE_INSPECT1_MID = "20311018"; // 106项检测1-中

	public static String VEHICLE_INSPECT1_SMA = "20311029"; // 106项检测1-小

	public static String VEHICLE_INSPECT2_BIG = "20311008"; // 106项检测2-大

	public static String VEHICLE_INSPECT2_MID = "20311019"; // 106项检测2-中

	public static String VEHICLE_INSPECT2_SMA = "20311030"; // 106项检测2-小

	public static String VEHICLE_MILEAGE_BIG = "20311009"; // 里程数-大

	public static String VEHICLE_MILEAGE_MID = "20311020"; // 里程数-中

	public static String VEHICLE_MILEAGE_SMA = "20311031"; // 里程数-小

	public static String VEHICLE_LICENCE_BIG = "20311010"; // 车辆行驶证-大

	public static String VEHICLE_LICENCE_MID = "20311021"; // 车辆行驶证-中

	public static String VEHICLE_LICENCE_SMA = "20311032"; // 车辆行驶证-小
	
	// 销售上报
	public static String  VEHICLE_INVOICE_BIG = "20311011"; //过户发票大图

	public static String  VEHICLE_INVOICE_MID = "20311022"; //过户发票中图

	public static String  VEHICLE_INVOICE_SMA = "20311033"; //过户发票小图


	// 车辆状态
	public static String VHCL_STATUS = "2015";

	public static String VHCL_STATUS_NOT = "20151001"; // 待收购

	public static String VHCL_STATUS_PRO = "20151002"; // 已收购

	public static String VHCL_STATUS_YES = "20151003"; // 已销售

	// 性别
	public static String SEX = "1003";

	// 车辆价格类型
	public static String VHCL_PRICE_TYPE = "2023";

	public static String VHCL_PRICE_TYPE_PUR = "20231001"; // 收购价

	public static String VHCL_PRICE_TYPE_PUBLIC = "20231002"; // 标牌价

	public static String VHCL_PRICE_TYPE_BASE = "20231003"; // 底价

	public static String VHCL_PRICE_TYPE_HOLD = "20231004"; // 保留价

	// 意向跟进结果(留用)
	// public static String FOLLOW_STAT_NO = "20491001"; //未跟进
	// public static String FOLLOW_STAT_YES = "20491002"; //已跟进

	// 系统业务类型
	public static String BUSINESS_PURCHASE = "20501001"; // 收购

	public static String BUSINESS_SELL = "20501002"; // 销售

	// 来电来访处理类型
	public static String ACCEPT_TYPE_INTENT = "20511001"; // 来访转意向

	public static String ACCEPT_TYPE_FOLLOW = "20511002"; // 来访转跟进

	// 过户状态
	public static String TRANSFER_STATUS = "2027";

	public static String TRANSFER_STATUS_WAIT = "20271001"; // 等待过户

	public static String TRANSFER_STATUS_PRO = "20271002"; // 过户中

	public static String TRANSFER_STATUS_ACHIEVE = "20271003"; // 过户完成

	public static String TRANSFER_STATUS_TEMP = "20271004"; // 等待对方过户

	// 过户类型
	public static String TYPE_STATUS = "2028";

	public static String TYPE_STATUS_SG = "20281001"; // 收购过户

	public static String TYPE_STATUS_DB_IN = "20281002"; // 调入过户

	public static String TYPE_STATUS_SS = "20281003"; // 销售过户
	
	public static String TYPE_STATUS_DB_OUT = "20281004"; // 调出过户

	// 车辆预留状态
	public static String RESRV_STATUS = "2052";

	public static String RESRV_STATUS_NO = "20521001"; // 未预留

	public static String RESRV_STATUS_YES = "20521002"; // 已预留

	// 车辆评估单状态
	public static String EVALUATE_STAT = "2054";
	
	public static String EVALUATE_STAT_UNPUTIN = "20541001"; // 未提交

	public static String EVALUATE_STAT_PUTIN = "20541002"; // 已提交

	public static String EVALUATE_STAT_DISVALUE_UNPUTIN = "20541003"; // 已失效(未提交)

	public static String EVALUATE_STAT_DISVALUE_PUTIN = "20541004"; // 已失效(已提交)

	// 单据状态(收购协议状态)
	public static String PROTOCOL_STAT = "2029";

	public static String PROTOCOL_STAT_UNPUTIN = "20291001";// 未提交

	public static String PROTOCOL_STAT_PUTIN = "20291002";// 已提交

	public static String PROTOCOL_STAT_REJECT = "20291003";// 已驳回

	public static String PROTOCOL_STAT_PASS = "20291004";// 已通过

	public static String PROTOCOL_STAT_DROP = "20291005";// 已撤销

	// 联系方式类别
	public static String LINK_WAY_TYPE = "2055";
	//手机
	public static int PERSONAL_CELL_TYPE = 20551001;
	//工作电话
	public static int WORK_TEL_TYPE = 20551002;
	//家庭电话
	public static int FAMILY_TEL_TYPE = 20551003;
	//电子邮件
	public static int EMAIL_TYPE = 20551004;

	// 行业
	public static String INDUSTRY_WAY = "2042";

	// 证件类型
	public static String CERTIFICATE_TTYPE = "2056";

	// 婚姻状况
	public static String MARRY_TTYPE = "1006";

	// 学历
	public static String DIPLOMA_TYPE = "1007";

	// 家庭月收入
	public static String FAMILY_INCOME_TYPE = "2062";

	// 爱好
	public static String FAVOR_TYPE = "2058";
	
	// 了解渠道
	public static String KNOW_CHANNEL_TYPE = "2078";
	// 了解渠道（其它）
	public static String KNOW_CHANNEL_OTHER_TYPE = "20781010";

	// 联系人类别
	public static String LINK_MAN_TYPE = "2057";
	//决策人
	public static String DECIDOR_TYPE = "20571002";

	// 手续类型
	public static String TRANS_PROC_TYPE = "2026";

	public static String TRANS_PROC_TYPE_1 = "20261001"; // 行驶证

	public static String TRANS_PROC_TYPE_2 = "20261002"; // 年检

	public static String TRANS_PROC_TYPE_3 = "20261003"; // 道路通行费

	public static String TRANS_PROC_TYPE_4 = "20261004"; // 违章记录

	public static String TRANS_PROC_TYPE_5 = "20261005"; // 环保标志

	public static String TRANS_PROC_TYPE_6 = "20261006"; // 登记证

	public static String TRANS_PROC_TYPE_7 = "20261007"; // 保险费

	public static String TRANS_PROC_TYPE_8 = "20261008"; // 车船税

	public static String TRANS_PROC_TYPE_9 = "20261009"; // 购置税
	
	public static String TRANS_PROC_TYPE_10 = "20261010"; // 未清偿车贷

	// 过户项目
	public static String TRANS_PRO = "2059";

	public static String TRANS_PRO_FEE = "20591001";

	public static String TRANS_PRO_BACK = "20591002";

	public static String TRANS_PRO_OTHER = "20591003";

	// 费用类型
	public static String TRANS_FEE = "1005";

	public static String TRANS_FEE_INCOME = "10051001";

	public static String TRANS_FEE_PAYOUT = "10051002";

	// 意向跟进计划完成状态
	public static String FOLLOW_STAT_UNCOMPLETED = "20611001"; // 未完成

	public static String FOLLOW_STAT_COMPLETE = "20611002"; // 已完成

	// 认证号
	// 认证号第一部分
	public static String AUTH_NUMBER_AUTH_CAR = "U"; // 诚新认证车唯一定义

	public static String AUTH_NUMBER_EXCHANGE_CAR = "N";// 诚新旧车置换新车业务唯一定义（如再启动置换申批业务的识别号，预留）

	// 认证号第二部分,品牌
	public static String AUTH_NUMBER_BRAND_BUICK = "B"; // 别克

	public static String AUTH_NUMBER_BRAND_CHEVROLET = "F";// 雪佛兰

	public static String AUTH_NUMBER_BRAND_CADILLAC = "C"; // 凯迪拉克

	// 认证号第三部分,车来源性质
	public static String AUTH_NUMBER_SOURCE_PUR = "C"; // 经销商直接收购的车辆认证车辆

	public static String AUTH_NUMBER_SOURCE_SGM = "A"; // 经销商通过SGM拍卖的SGM公司车辆

	public static String AUTH_NUMBER_SOURCE_STO = "I"; // 经销商拍卖的SGM高库龄车辆

	public static String AUTH_NUMBER_SOURCE_VIP = "V"; // 上海通用汽车VIP客户

	public static String AUTH_NUMBER_SOURCE_TEST = "D"; // 原为经销商试乘试驾车辆
	
	//操作名称
	public static String ACTION_NAME_EVA = "车辆评估"; // 评估
	
	public static String ACTION_NAME_SALE_ORDER = "车辆销售"; // 销售
	
	public static String ACTION_NAME_RPCH_AGRM = "车辆收购"; // 收购
	
	public static String ACTION_NAME_CTF_APY = "认证申请"; // 认证申请
	
	//提醒类型
	public static String RETRNN_TYPE = "2043";
	
	public static String RETURN_VISIT = "20431001"; // 回访
	
	public static String EXAMINE_AND_ENDORSE = "20431002"; // 审批
	
	public static String WAIT_TO_HANDLE = "20431003"; // 待办
	
	//操作类型
	public static String RPCH_AGRM_DISPOSITION = "20441001"; // 收购意向
	
	public static String PURCHASE_AGREEMENT = "20441002"; // 收购协议
	
	public static String SALE_ORDER_DISPOSITION = "20441003"; // 销售意向
	
	public static String CLIENT_LOVING = "20441004"; // 客户关怀
	
	public static String VEHICLE_CANNIBALIZE = "20441005"; // 车辆调拨调入
	
	public static String SALE_ORDER = "20441006"; // 销售订单
	
	public static String EQUIPPEN_NEWSREEL = "20441007"; // 整备记录
	
	public static String VEHICLE_COME_ON = "20441008"; // 车辆出库
	
	public static String VEHICLE_SELL_FOLD = "20441009"; // 车辆寄售调入
	
	public static String VEHICLE_SELL_EXIT = "20441010"; // 车辆寄售退回
	
	public static String VEHICLE_INCOME = "20441011"; //车辆入库

	public static String SELL_REPORT = "20441012"; //销售上报
	
	public static String VEHICLE_TRANSFER = "20441013"; //车辆过户
	
	public static String OBLIGATE_CANCEL = "20441014"; //预留取消
	
	public static String OPERATE_AUTH_SUB = "20441015"; //认证申请
	
	public static String OPERATE_AUTH_APRV = "20441016"; //认证审批
	
	public static String OPERATE_SALES_APRV = "20441017"; //销售审批
	
	public static String OPERATE_VISIT_DEAL = "20441018"; //来访处理
	
	// 意向跟进内容
	public static String FOLLOW_RES = "2063"; // 意向跟进内容

	public static String FOLLOW_RES_GENGRAL = "20631001"; // 维护信息

	public static String FOLLOW_RES_DEFER = "20631002"; // 延期访问

	public static String FOLLOW_RES_EVALUATE = "20631003"; // 车辆评估

	// 调拨状态(调拨,寄售)
	public static String ALLOCATE_TYPE = "2069"; // 调出

	public static String ALLOCATE_TYPE_OUT = "20691001"; // 调出

	public static String ALLOCATE_TYPE_IN = "20691002"; // 调入
	
	// 调拨状态
	public static String ALLOCATE_STAT = "2035";

	public static String ALLOCATE_STAT_REQUEST = "20351001"; // 已请求

	public static String ALLOCATE_STAT_ACCEPT = "20351002"; // 已通过

	public static String ALLOCATE_STAT_REJECT = "20351003"; // 已驳回
	
	// 寄售状态
	public static String CNSG_STAT = "2064";

	public static String CNSG_STAT_REQUEST = "20641001"; // 已请求

	public static String CNSG_STAT_ACCEPT = "20641002"; // 已通过

	public static String CNSG_STAT_REJECT = "20641003"; // 已驳回
	
	public static String CNSG_STAT_QUIT = "20641004"; // 已退回

	// 价格设定情况
	public static String PRICE_SET = "2065";

	public static String PRICE_SET_YES = "20651002"; // 已设定

	public static String PRICE_SET_NO = "20651001"; // 未设定

	// 出入库状态
	public static String INOUT_STO_STAT = "2066"; // 出入库状态

	public static String INOUT_STO_STAT_NOIN = "20661001"; // 待入库

	public static String INOUT_STO_STAT_IN = "20661002"; // 入库

	public static String INOUT_STO_STAT_NOOUT = "20661003"; // 待出库

	public static String INOUT_STO_STAT_OUT = "20661004"; // 出库

	// 入库类型
	public static String INV_IN_TYPE = "2067";

	public static String INV_IN_TYPE_BUYIN = "20671001"; // 收购入库

	public static String INV_IN_TYPE_ALOIN = "20671002"; // 调拨入库

	public static String INV_IN_TYPE_DIGIN = "20671003"; // 整备入库

	public static String INV_IN_TYPE_EXPIN = "20671004"; // 试乘试驾入库

	public static String INV_IN_TYPE_MARIN = "20671005"; // 市场活动入库

	public static String INV_IN_TYPE_LODHIN = "20671006";// 寄售入库

	public static String INV_IN_TYPE_MOVIN = "20671008"; // 移库入库

	public static String INV_IN_TYPE_OTHIN = "20671007"; // 其他入库
	
	public static String INV_IN_TYPE_LORTOUT = "20671009"; // 寄售退回入库

	// 出库类型
	public static String INV_OUT_TYPE = "2068";

	public static String INV_OUT_TYPE_BUYOUT = "20681001"; // 销售出库

	public static String INV_OUT_TYPE_ALOOUT = "20681002"; // 调拨出库

	public static String INV_OUT_TYPE_DIGOUT = "20681003"; // 整备出库

	public static String INV_OUT_TYPE_EXPOUT = "20681004"; // 试乘试驾出库

	public static String INV_OUT_TYPE_MAROUT = "20681005"; // 市场活动出库

	public static String INV_OUT_TYPE_LODOUT = "20681006"; // 寄售出库

	public static String INV_OUT_TYPE_MOVOUT = "20681008"; // 移库出库

	public static String INV_OUT_TYPE_OTHOUT = "20681007"; // 其他出库
	
	public static String INV_OUT_TYPE_LORTOUT = "20681009"; // 寄售退回出库

	// 订单类型
	public static String ORDER_TYPE = "2039";

	public static String ORDER_TYPE_GENERIC = "20391001";// 一般销售订单

	public static String ORDER_TYPE_LICENSE = "20391002";// 牌照销售

	public static String ORDER_TYPE_BATCH = "20391003";// 批发销售

	// 车辆库存状态
	public static String VHCL_STO = "2070";

	public static String VHCL_STO_IN = "20701001"; // 在库

	public static String VHCL_STO_WAY = "20701002"; // 在途

	public static String VHCL_STO_OUT = "20701003"; // 销售出库

	// 车辆发布状态
	public static String ISSUANCE_SATE = "2016";

	public static String ISSUANCE_SATE_YES = "20161002"; // 已发布

	public static String ISSUANCE_SATE_NO = "20161001";// 未发布

	// 车辆上架状态
	public static String ONSHELL_STATE = "2018";

	public static String ONSHELL_STATE_YES = "20181002"; // 已上架

	public static String ONSHELL_STATE_NO = "20181001"; // 未上架

	// 车辆共享状态
	public static String SHARE_STATE = "2017";

	public static String SHARE_STATE_YES = "20171002"; // 已共享

	public static String SHARE_STATE_NO = "20171001"; // 未共享

	// 车辆hold
	public static String HOLD_STATUS = "2053"; // 正常
	public static String HOLD_STATUS_NORMAL = "20531001"; // 正常
	public static String HOLD_STATUS_AUTH = "20531002"; // 认证中
	public static String HOLD_STATUS_PURCHASE = "20531003"; // 收购中
	public static String HOLD_STATUS_SELL = "20531004"; // 销售中
	public static String HOLD_STATUS_ALLOC = "20531005"; // 调拨中
	public static String HOLD_STATUS_CNSG = "20531006"; // 寄售中
	public static String HOLD_STATUS_CNSG_RETURN = "20531007"; // 寄售退回中

	// 操作类型（收购）
	public static String OPERATION_PURCHASE = "1"; // 收购类型

	// 操作类型（销售）
	public static String OPERATION_SELL = "2"; // 销售类型

	// 公司性质
	public static String COMPNAY_TYPE = "2071";

	// FLASH操作数据库的用户ID
	public static String FLASH_UID = "1234567890";

	// 阀值类型
	public static String STOCK_AGE_FUNDS_VALUE = "2038";

	public static String STOCK_AGE_VALUE = "20381001"; // 库龄阀值

	public static String FUNDS_VALUE = "20381002"; // 资金阀值

	public static String AGE_MAX_VALUE = "0"; // 库龄阀值设置天数最大值

	public static String FUND_MAX_VALUE = "0"; // 资金阀值设置资金最大值

	// 基本颜色
	public static String COLOR = "2001";

	public static String COLOR_RED = "20011001"; // 红

	public static String COLOR_ORANGE = "20011002"; // 橙

	public static String COLOR_YELLOW = "20011003"; // 黄

	public static String COLOR_GREEN = "20011004"; // 绿

	public static String COLOR_CYAN = "20011005"; // 青

	public static String COLOR_BLUE = "20011006"; // 蓝

	public static String COLOR_PURPLE = "20011007"; // 紫

	public static String COLOR_WHITE = "20011008"; // 白

	// 事故状态
	public static String ACDT_STAT = "2034";

	static String ACDT_STAT_NULL = "20341001";// 无

	static String ACDT_STAT_SMALL = "20341002";// 小

	static String ACDT_STAT_BIG = "20341003";// 大

	// 使用类型
	public static String USAGE_QUAL = "2025";

	static String USAGE_QUAL_YUN = "20251001";// 运营

	static String USAGE_QUAL_JIA = "20251002";// 家用

	static String USAGE_QUAL_CHU = "20251003";// 出租

	// 认证号维护状态
	public static String CERTNUM_STAT = "2072";

	public static String CERTNUM_STAT_YES = "20721001"; // 已维护

	public static String CERTNUM_STAT_NO = "20721002"; // 未维护

	// 附件
	public static String VEHICLE_ACC = "2036";

	public static String VEHICLE_ACC_OTHER = "20361001"; // 其他

	// 销售上报状态
	// 销售上报
	public static String VEHICLE_SALE_REPORT = "2021";

	// 未提交
	public static String VEHICLE_SALE_REPORT_UNSUBMIT = "20211001";

	// 已提交
	public static String VEHICLE_SALE_REPORT_SUBMIT = "20211002";

	// 驳回
	public static String VEHICLE_SALE_REPORT_BACK = "20211003";

	// 通过
	public static String VEHICLE_SALE_REPORT_PASS = "20211004";
	// 战败
	public static String DEFEATED_TYPE = "2041";
	//价格
	public static String DEFEATED_TYPE_PRICE = "20411001";
	//竟品
	public static String DEFEATED_TYPE_ARTICLE = "20411002";
	//服务
	public static String DEFEATED_TYPE_SERVCE = "20411003";
	
	//牌照状态
	public static String LIC_STAT_PRUC = "20731001"; //已收购
	
	public static String LIC_STAT_SALE = "20731002"; //已销售
	
	public static String LIC_STAT_ABATE = "20731003"; //已失效
	
	//整备类型
	public static String EVA_YES_TYPE = "20481001"; //含质保
	
	public static String EVA_NO_TYPE = "20481002"; //不含质保
	
	//公司类型
	public static String COMPANY_TYPE = "2024"; // 

	/**
	 * 公司类型 - 车厂
	 */
	public static final String COMPANY_TYPE_SGM = "20241001";

	/**
	 * 公司类型 - 代理商
	 */
	public static final String COMPANY_TYPE_DEALER = "20241002";
	
	/**
	 * 公司类型 - 分销商
	 */
	public static final String COMPANY_TYPE_DEALER_DMC = "20241003";
	
	//渠道类型
	public static Integer DEALER_TYPE = 3059; // 


	/**
	 * 渠道类型 - 代理商
	 */
	public static final Integer DEALER_TYPE_AGENT = 30591001;
	
	/**
	 * 渠道类型 - 分销商
	 */
	public static final Integer DEALER_TYPE_DISTRIBUTION = 30591002;
	
	//车辆来源
	public static String VEHICLE_SOURCE = "2014";
	
	//六方位图片状态
	public static String CAR_PICTURE_TYPE = "2074"; 
	//六方位图片状态-无
	public static String NO_CAR_PICTURE_TYPE = "20741001"; 
	//六方位图片状态-部分
	public static String SOME_CAR_PICTURE_TYPE = "20741002";  
	//六方位图片状态-完整
	public static String ALL_CAR_PICTURE_TYPE = "20741003";  

	public static String LIC_OPERATE_TYPE_FL = "20751001"; // 车牌分离
	public static String LIC_OPERATE_TYPE_ZH = "20751002"; //车牌整合
	
	public static String REPORT  ="2076"; //报告类型
	public static String REPORT_MSRP = "20761001"; //MSRP // 普通车况价格
	public static String REPORT_REMNANT_COST = "20761002"; //车型残值 // 新车指导价
	public static String REPORT_REMNANT_COST_RATE = "20761003"; //车型残值率
	
	public static String VHCL_COLOR_SERIES  ="2077"; //车辆色系
	
	public static String VHCL_OWNER_PKG  ="2092"; //原厂配置
	
	public static String VHCL_ADDITION_PKG  ="2032"; //增加配置
	
	//车辆评估类别：外部，内部，车架，附件，性能
	public static String VHCL_EVA_TYPE  ="2033"; 			//车辆评估类别
	public static String VHCL_EVA_OUTER  ="20331001"; 		//外部
	public static String VHCL_EVA_INNER  ="20331002"; 		//内部
	public static String VHCL_EVA_FRAME  ="20331003"; 		//车架
	public static String VHCL_EVA_ADDITON  ="20331004"; 	//附件
	public static String VHCL_EVA_FUNC  ="20331005"; 		//性能
	
	//评估性能类别
	public static String VHCL_EVA_FTYPE  ="2005";			//评估性能类别
	public static String VHCL_EVA_FTYPE_BEGIN  ="2079";		//起动怠速
	public static String VHCL_EVA_FTYPE_START  ="2080";		//起步制动
	public static String VHCL_EVA_FTYPE_LOWRUN  ="2081";	//低速转向
	public static String VHCL_EVA_FTYPE_UNBALAN  ="2082";	//颠簸行驶
	public static String VHCL_EVA_FTYPE_ADDRUN  ="2083";	//加速行驶
	public static String VHCL_EVA_FTYPE_BALAN  ="2084";		//稳速行驶
	public static String VHCL_EVA_FTYPE_DELRUN  ="2085";	//减速滑行
	public static String VHCL_EVA_FTYPE_ROAD  ="2086";		//行车制动
	public static String VHCL_EVA_FTYPE_UPGET  ="2087";		//倒档行驶
	public static String VHCL_EVA_FTYPE_STOP  ="2088";		//停车熄火
	
	//车辆评估重大瑕疵
	public static String VHCL_EVA_SHORT  ="2004";				//瑕疵类别
	
	//车辆评估附件类别
	public static String VHCL_EVA_ADDA  ="2089";				//评估附件A
	public static String VHCL_EVA_ADDB  ="2090";				//评估附件B
	public static String VHCL_EVA_ADDC  ="2091";				//评估附件C

	//车辆评估配置
	public static final String VHCL_EVA_PKG_AIR  ="20921001";	//空调
	public static final String VHCL_EVA_PKG_CELL ="20921002";   //气囊
	public static final String VHCL_EVA_PKG_OTHER="20921017";	//其它
	
	//车辆评估增加配置
	public static final String VHCL_EVA_ADDPKG_RUN  ="20321001";	//非原装轮毂
	public static final String VHCL_EVA_ADDPKG_OTHER="20321011";   	//其它
	
	//空调类型
	public static final String VHCL_CONF_AIR_TYPE  ="2003";			//空调
	//气囊数
	public static final String VHCL_CONF_CELL_NUM  ="2007";			//气囊数
	//内饰颜色
	public static final String VHCL_EVA_PKG_INNER_COLOR  ="2002";			//内饰颜色
	
//汇款用途
	public static String REMIT_PURPOSE = "3002";
//汇款用途类别
	public static String REMIT_PURPOSE_NORMAL = "30021001";	//正常订单
	public static String REMIT_PURPOSE_CREDIT = "30021002";	//信贷还款
	public static String REMIT_PURPOSE_SVO = "30021003";	//SVO订金
	
	//汇款人类型
	public static String REMIT_TYPE = "3004";
	
	public static String REMIT_TYPE_OWEN = "30041001"; // 本单位
	
	public static String REMIT_TYPE_OTHER = "30041002"; // 他人代付
	
	//汇款状态
	public static String REMIT_STATUS = "3005";
	
	public static String REMIT_STATUS_SUBMIT = "30051001"; // 已提报
	
	public static String REMIT_STATUS_FAXCONFRIM = "30051002"; // 传真已确认
	
	public static String REMIT_STATUS_CONFRIM = "30051003"; // 到帐已确认
	
	//汇款银行
	public static String REMIT_BANK = "3001";
	
	public static String REMIT_BANK_ICBC = "30011001"; // 工行
	
	public static String REMIT_BANK_CCB = "30011002"; // 建行
	
	public static String REMIT_BANK_ABC = "30011003"; // 农行
	
	public static String REMIT_BANK_BC = "30011004"; // 中行
	
	public static String REMIT_BANK_BCM = "30011005"; // 交行
	
	public static String REMIT_BANK_OTHER = "30011006"; // 其他
	
//汇款方式
	public static String REMIT_MODE = "3003";

	public static String REMIT_MODE_VIRTUAL = "30031001";	//虚拟款项
	public static String REMIT_MODE_CHECK = "30031002";	//银行汇票
	public static String REMIT_MODE_ACCEPT = "30031003";	//银行承兑
	
	public static final Integer ORDER_TYPE_NORMAL = 30551001; //常规订单
	public static final Integer ORDER_TYPE_SVO    = 30551002; //SVO订单
	
	//固定职位
	public static String BEGIN_POSITION = "3085"; // 

	/**
	 *固定职位 - 董事长
	 */
	public static final String BEGIN_POSITION_BOSS = "30851001";

	/**
	 *固定职位 - 总经理
	 */
	public static final String BEGIN_POSITION_MANAGER = "30851002";

	/**
	 *固定职位 - 市场部经理
	 */
	public static final String BEGIN_POSITION_MARKET = "30851003";

	/**
	 *固定职位 - 财务主管
	 */
	public static final String BEGIN_POSITION_MONEY = "30851004";

	/**
	 *固定职位 - 销售经理
	 */
	public static final String BEGIN_POSITION_SALES = "30851005";

	/**
	 *固定职位 - 前台
	 */
	public static final String BEGIN_POSITION_FRONT = "30851006";

	/**
	 *固定职位 - 销售顾问
	 */
	public static final String BEGIN_POSITION_SPECIALIST = "30851007";

	/**
	 *固定职位 - 系统管理员
	 */
	public static final String BEGIN_POSITION_ADMIN = "30851008";
	
//收款人
	public static String REMIT_INNAME = "3085";

	public static String REMIT_INNAME_WSL = "30851001";	//江铃五十铃汽车有限公司
	public static String REMIT_INNAME_JLGF = "30085002";	//江铃汽车股份有限公司
	public static String REMIT_INNAME_JLXS = "30851003";	//江铃汽车股份有限公司销售服务分公司	
	
//SVO需求评审状态
	public static String SVO_AUDIT = "3057";
	
	public static String SVO_AUDIT_PRE = "30571001";//待评审
	public static String SVO_AUDIT_UPDATE = "30571002";//驳回修改
	public static String SVO_AUDIT_ING = "30571003";//评审中
	public static String SVO_AUDIT_BACK = "30571004";//评审返回
	public static String SVO_AUDIT_PASS = "30571005";//评审通过
	public static String SVO_AUDIT_ACCEPT = "30571006";//接受评审
	public static String SVO_AUDIT_REJECT = "30571007";//拒绝
	public static String SVO_AUDIT_REMODEL = "30571008";//改装评审中
	public static String SVO_AUDIT_PRODUCT = "30571009";//生产确认中
	public static String SVO_AUDIT_SUCCESS = "30571010";//评审完成
	
	//车辆类型
	public static String A_CAR_TYPE = "30871001";
	public static String B_CAR_TYPE = "30871002";
	
	//订单状态
	public static final String ORDER_STATUS_DEALER_DMC_NO_REPORT = "30491001";		//分销商未提报
	public static final String ORDER_STATUS_DEALER_NO_REPORT = "30491002";		//代理商未提报
	public static final String ORDER_STATUS_NO_CONFIRM = "30491003";		//未确认
	public static final String ORDER_STATUS_CONFIRM = "30491004";		//已确认
	public static final String ORDER_STATUS_CANCEL = "30491005";		//已取消
	
	//客户第一来源
	public static final String COUSTOMER_FROM = "4201";	
	public static final String COUSTOMER_FROM_01 = "42011001";		//中介
	public static final String COUSTOMER_FROM_02 = "42011002";		//陌生拜访
	public static final String COUSTOMER_FROM_03 = "42011003";		//展厅来店
	public static final String COUSTOMER_FROM_04 = "42011004";		//展厅来电
	public static final String COUSTOMER_FROM_05 = "42011005";		//亲友介绍
	public static final String COUSTOMER_FROM_06 = "42011006";		//市场活动
	public static final String COUSTOMER_FROM_07 = "42011007";		//服务人员介绍
	public static final String COUSTOMER_FROM_08 = "42011008";		//老客户转介绍
	public static final String COUSTOMER_FROM_09 = "42011009";		//老客户续购
	public static final String COUSTOMER_FROM_10 = "420110010";		//数据库潜客
	
	//信息渠道
	public static final String INFOMATION_FROM = "4203";		
	public static final String INFOMATION_FROM_01 = "42031001";		//转介绍
	public static final String INFOMATION_FROM_02 = "42031002";		//户外
	public static final String INFOMATION_FROM_03 = "42031003";		//电视
	public static final String INFOMATION_FROM_04 = "42031004";		//电台
	public static final String INFOMATION_FROM_05 = "42031005";		//报纸
	public static final String INFOMATION_FROM_06 = "42031006";		//网站
	public static final String INFOMATION_FROM_07 = "42031007";		//114

	//客户类型
	public static final String CUSTOMER_TYPE = "4204";
	public static final String CUSTOMER_TYPE_01 = "42041001";		//个人客户
	public static final String CUSTOMER_TYPE_02 = "42041002";		//单位客户
	
	//宜联时间
	public static final String LINKMAN_CONNECT_TIME		= "4205";		
	public static final String LINKMAN_CONNECT_TIME_01	= "42051001";	//上午
	public static final String LINKMAN_CONNECT_TIME_02	= "42051002";	//中午
	public static final String LINKMAN_CONNECT_TIME_03	= "42051003";	//下午
	public static final String LINKMAN_CONNECT_TIME_04	= "42051004";	//晚上
	
	//年收入
	public static final String LINKMAN_EARNING_YEAR		= "4206	";		
	public static final String LINKMAN_EARNING_YEAR_01	= "42061001";	//1万元以下
	public static final String LINKMAN_EARNING_YEAR_02	= "42061002";	//1－3万元
	public static final String LINKMAN_EARNING_YEAR_03	= "42061003";	//3－5万元
	public static final String LINKMAN_EARNING_YEAR_04	= "42061004";	//5－10万元
	public static final String LINKMAN_EARNING_YEAR_05	= "42061005";	//10－15万元
	public static final String LINKMAN_EARNING_YEAR_06	= "42061006";	//15－20万元
	public static final String LINKMAN_EARNING_YEAR_07	= "42061007";	//20－30万元
	public static final String LINKMAN_EARNING_YEAR_08	= "42061008";	//30－50万元
	public static final String LINKMAN_EARNING_YEAR_09	= "42061009";	//50-100万元
	public static final String LINKMAN_EARNING_YEAR_10	= "42061010";	//100-150万元
	public static final String LINKMAN_EARNING_YEAR_11	= "42061011";	//150-200万元
	public static final String LINKMAN_EARNING_YEAR_12	= "42061012";	//300-500万元
	public static final String LINKMAN_EARNING_YEAR_13	= "42061013";	//500万元以上
	
	//有效证件
	public static final String CERTIFICATE_TYPE = "4207"; 
	public static final String CERTIFICATE_TYPE_01 = "42071001";		//身份证
	public static final String CERTIFICATE_TYPE_02 = "42071002";		//驾驶证
	public static final String CERTIFICATE_TYPE_03 = "42071003";		//军官证
	
	//爱好
	public static final String LINKMAN_HOBBY = "4208";
	public static final String LINKMAN_HOBBY_01 = "42081001";		//休闲活动(如打牌、钓鱼、打麻将)
	public static final String LINKMAN_HOBBY_02 = "42081002";		//社交活动(如聚会、酒会)
	public static final String LINKMAN_HOBBY_03 = "42081003";		//高档休闲活动(如高尔夫、会所)
	public static final String LINKMAN_HOBBY_04 = "42081004";		//摄影
	public static final String LINKMAN_HOBBY_05 = "42081005";		//逛街/购物
	public static final String LINKMAN_HOBBY_06 = "42081006";		//上网/网络游戏/电子游戏
	public static final String LINKMAN_HOBBY_07 = "42081007";		//投资理财(如炒股、投资基金)
	public static final String LINKMAN_HOBBY_08 = "42081008";		//养宠物/园艺种植
	public static final String LINKMAN_HOBBY_09 = "42081009";		//阅读(如看书、看报)
	public static final String LINKMAN_HOBBY_10 = "42081010";		//公园
	public static final String LINKMAN_HOBBY_11 = "42081011";		//SPA、美容
	public static final String LINKMAN_HOBBY_12 = "42081012";		//艺术培训（绘画、乐器、舞蹈等）
	public static final String LINKMAN_HOBBY_13 = "42081013";		//收藏（红木、古玩、字画等）
	public static final String LINKMAN_HOBBY_14 = "42081014";		//和家人共度（例如：陪孩子在家玩耍等）
	public static final String LINKMAN_HOBBY_15 = "42081015";		//志愿者
	public static final String LINKMAN_HOBBY_16 = "42081016";		//看电视
	public static final String LINKMAN_HOBBY_17 = "42081017";		//其它
	
	//喜欢沟通方式
	public static final String LINKMAN_CONNECT_TYPE		= "4209";		
	public static final String LINKMAN_CONNECT_TYPE_01	= "42091001";	//固定电话
	public static final String LINKMAN_CONNECT_TYPE_02	= "42091002";	//邮件
	public static final String LINKMAN_CONNECT_TYPE_03	= "42091003";	//电子邮件
	public static final String LINKMAN_CONNECT_TYPE_04	= "42091004";	//短信
	
	//行业分类
	public static final String CLASS_TYPE = "4213";
	public static final String CLASS_TYPE_01 = "42131001";		//制药/医药
	public static final String CLASS_TYPE_02 = "42131002";		//医疗卫生
	public static final String CLASS_TYPE_03 = "42131003";		//电力
	public static final String CLASS_TYPE_04 = "42131004";		//通讯/电信
	public static final String CLASS_TYPE_05 = "42131005";		//工程建设
	public static final String CLASS_TYPE_06 = "42131006";		//公路交通
	public static final String CLASS_TYPE_07 = "42131007";		//公用事业单位
	public static final String CLASS_TYPE_08 = "42131008";		//航空
	public static final String CLASS_TYPE_09 = "42131009";		//公检法司
	public static final String CLASS_TYPE_10 = "42131010";		//金融保安
	public static final String CLASS_TYPE_11 = "42131011";		//客运旅游
	public static final String CLASS_TYPE_12 = "42131012";		//矿山冶炼
	public static final String CLASS_TYPE_13 = "42131013";		//商业贸易
	public static final String CLASS_TYPE_14 = "42131014";		//农林牧渔水
	public static final String CLASS_TYPE_15 = "42131015";		//汽车租赁
	public static final String CLASS_TYPE_16 = "42131016";		//石油石化
	public static final String CLASS_TYPE_17 = "42131017";		//食品饮料
	public static final String CLASS_TYPE_18 = "42131018";		//铁路
	public static final String CLASS_TYPE_19 = "42131019";		//文教科技
	public static final String CLASS_TYPE_20 = "42131020";		//物流运输
	public static final String CLASS_TYPE_21 = "42131021";		//烟草
	public static final String CLASS_TYPE_22 = "42131022";		//邮政
	public static final String CLASS_TYPE_23 = "42131023";		//政府机关
	public static final String CLASS_TYPE_24 = "42131024";		//制造企业
	public static final String CLASS_TYPE_25 = "42131025";		//纺织服装
	public static final String CLASS_TYPE_26 = "42131026";		//改装厂
	public static final String CLASS_TYPE_27 = "42131027";		//酒店餐饮
	public static final String CLASS_TYPE_28 = "42131028";		//家电百货
	public static final String CLASS_TYPE_29 = "42131029";		//市政城管
	public static final String CLASS_TYPE_30 = "42131030";		//化工/能源
	public static final String CLASS_TYPE_31 = "42131031";		//房地产
	public static final String CLASS_TYPE_32 = "42131032";		//建筑/安装

	//单位属性
	public static final String CUSTOMER_KIND = "4215";
	public static final String CUSTOMER_KIND_01 = "42151001";		//国营
	public static final String CUSTOMER_KIND_02 = "42151002";		//民营
	public static final String CUSTOMER_KIND_03 = "42151003";		//三资
	public static final String CUSTOMER_KIND_04 = "42151004";		//集体
	public static final String CUSTOMER_KIND_05 = "42151005";		//个体
	public static final String CUSTOMER_KIND_06 = "42151006";		//其他
	
	//订单状态
	public static final String ORDER_STATUS = "4238";
	public static final String ORDER_STATUS_01 = "42381001";		//待提交
	public static final String ORDER_STATUS_02 = "42381002";		//待价格审批
	public static final String ORDER_STATUS_03 = "42381003";		//待交定金
	public static final String ORDER_STATUS_04 = "42381004";		//订单处理中
	public static final String ORDER_STATUS_05 = "42381005";		//订单完成
	public static final String ORDER_STATUS_06 = "42381006";		//取消
	
	//订单审核状态
	public static final String AUDIT_STATUS = "4239";
	public static final String AUDIT_STATUS_01 = "42391001";		//审核通过
	public static final String AUDIT_STATUS_02 = "42391002";		//审核驳回
	
	//潜客级别(客户级别)
	public static final String CUSTOMER_LEVEL = "4242";
	public static final String CUSTOMER_LEVEL_01 = "42421001";		//A级 7天内
	public static final String CUSTOMER_LEVEL_02 = "42421002";		//B级 1月内
	public static final String CUSTOMER_LEVEL_03 = "42421003";		//C级 3月内
	public static final String CUSTOMER_LEVEL_04 = "42421004";		//D级 3月以上
	public static final String CUSTOMER_LEVEL_05 = "42421005";		//O级
	public static final String CUSTOMER_LEVEL_06 = "42421006";		//F级
	public static final String CUSTOMER_LEVEL_07 = "42421007";		//J级


	//交车方式
	public static final String DELIVERY_TYPE	= "4249";
	public static final String DELIVERY_TYPE_01	= "42491001";	//店里交车
	public static final String DELIVERY_TYPE_02	= "42491002";	//送车上门
	
	//付款方式
	public static final String PAY_TYPE	= "4250";
	public static final String PAY_TYPE_01	= "42501001";	//一次付清
	public static final String PAY_TYPE_02	= "42501002";	//按揭贷款
	public static final String PAY_TYPE_03	= "42501003";	//分期贷款
	
	//开票方式
	public static final String INVOICE_TYPE	= "4251";
	public static final String INVOICE_TYPE_01	= "42511001";	//付清后开票
	public static final String INVOICE_TYPE_02	= "42511002";	//预先开票
	
	//婚否
	public static final String MARRY_STATUS	= "4267";
	public static final String MARRY_STATUS_01	= "42671001";	//未婚
	public static final String MARRY_STATUS_02	= "42671002";	//已婚
	
	// 组织类型
	public static Integer ORG_TYPE = 5007;
	public static Integer ORG_TYPE_GROUP = 50071001; // 集团/公司
	public static Integer ORG_TYPE_AGENT = 50071002; // 代理商	
	public static Integer ORG_TYPE_BRANCH = 50071003; // 分店	
	public static Integer ORG_TYPE_DISTRIBUTION = 50071004; // 分销商
	public static Integer ORG_TYPE_TEAM = 50071005; // 团队
	
	  //批售报备状态
	public static int WHOLE_SALE_STATUS= 3040;		//批售报备状态
	public static int WHOLE_SALE_STATUS_NOT = 30401001;		//已报备
	public static int WHOLE_SALE_STATUS_YES = 30401002;		//已确认
	//批售报备信息变更审核状态
	public static int WHOLE_SALE_EXAMINE_STATUS= 3041;		//批售报备变更状态
	public static int WHOLE_SALE_EXAMINE_STATUS_ING = 30411001;		//审批中
	public static int WHOLE_SALE_EXAMINE_STATUS_PASS = 30411002;		//审批通过
	public static int WHOLE_SALE_EXAMINE_STATUS_REJECT = 30411003;		//审批驳回
	
	//状态
	public static String STATUS_TYPE = "4305";
	public static String STATUS_TYPE_RESTART = "43051001";		//启用
	public static String STATUS_TYPE_STOP = "43051002";			//停用
	
	//验收状态
	public static int INSPECTION_STATUS = 5001;
	public static int INSPECTION_STATUS_PRE = 50011001;				//未验收
	public static int INSPECTION_STATUS_END = 50011002;				//已验收
	public static int INSPECTION_STATUS_UNAVAILABLE = 50011003;		//无效验收
	
	//质损情况
	public static int INSPECTION_DAMAGE = 5002;
	public static int INSPECTION_DAMAGE_NONE = 50021001;			//无质损
	public static int INSPECTION_DAMAGE_HAS = 50021002;				//有质损
	
	//称谓方式
	public static String APPELLATION_FULL_NAME = "50131001";	//全名
	public static String APPELLATION_SURNAMES = "50131002";		//姓氏
	public static String APPELLATION_NO_NAME = "50131003";		//无
	
	//省市县默认值
	public static String REGIONCITYCOUNTY_TYPE = "9001";				//类型
	public static String REGIONCITYCOUNTY_REGION = "90011001";				//省/直辖市
	public static String REGIONCITYCOUNTY_CITY = "90011002";				//地级市
	public static String REGIONCITYCOUNTY_COUNTY = "90011003";				//区县/县级市
	
	//价格状态
	public static String PRICE_TYPE = "4900";				//价格状态类型
	public static String PRICE_TYPE_YES = "49001001";			//已维护
	public static String PRICE_TYPE_NO = "49001002";			//未维护
	
	//市场活动方案类别
	public static final String ACTIVITIES_TYPE = "3020";				//活动类型
	public static final String ACTIVITIES_TYPE_LOCAL = "30201004";	//本地活动
	
	  //附加费用
	public static String ADDITION_TYPE = "4253";
	public static String ADDITION_TYPE_CHANGE = "42531001";//改装费用
	public static String ADDITION_TYPE_INSURANCE = "42531002";//保险费用
	public static String ADDITION_TYPE_LICENSE = "42531003";//上牌费用
	public static String ADDITION_TYPE_COMMISSION = "42531004";//代办费用
	public static String ADDITION_TYPE_OTHER = "42531005";//其它费用
	
	//车辆节点
	public static String VEHICLE_NODE = "3060";
	public static String VEHICLE_NODE_01="30601001";    //生产线	
	public static String VEHICLE_NODE_02="30601002";	//车厂库
	public static String VEHICLE_NODE_03="30601003";	//发运在途
	public static String VEHICLE_NODE_04="30601004";	//经销商在库
	public static String VEHICLE_NODE_05="30601005";	//已实销
	
	//配车状态
	public static Integer MATCH_VEHICLE_TYPE = 3062;
	public static Integer MATCH_VEHICLE_TYPE_NORMAL = 30621001;  //正常
	public static Integer MATCH_VEHICLE_TYPE_CHANGE = 30621002;   //已换车
	public static Integer MATCH_VEHICLE_TYPE_CANCEL = 30621003;//已退车
	
	//车辆状态
	public static String VEHICLE_STATUS = "5016";
	public static String VEHICLE_STATUS_01="50161001";//正常库存
	public static String VEHICLE_STATUS_02="50161002";//调拨在途
	public static String VEHICLE_STATUS_03="50161003";//订单配车
	public static String VEHICLE_STATUS_04="50161004";//交车出库
	public static String VEHICLE_STATUS_05="50161005";//销售上报
	public static String VEHICLE_STATUS_06="50161006";//锁定
	public static String VEHICLE_STATUS_07="50161007";//外借
	
	//通报类别状态
	public static Integer BULLETIN_TYPE = 4282;
	public static Integer BULLETIN_TYPE_NOT = 42821001;//可用
	public static Integer BULLETIN_TYPE_YSE = 42821002;//注销
	
	//通报状态
	public static Integer BULLETIN_STATUS = 4283;
	public static Integer BULLETIN_STATUS_NOT = 42831001;//无效
	public static Integer BULLETIN_STATUS_YSE = 42831002;//有效
	
	//基盘客户类型
	public static Integer BASE_CUSTOMER = 9004;
	public static Integer BASE_CUSTOMER_FINE = 90041001;//优质客户
	public static Integer BASE_CUSTOMER_POTENTIAL = 90041002;//潜力客户
	public static Integer BASE_CUSTOMER_COMMON = 90041003;//一般客户
	//结算状态
	public static String CHECK_STATUS = "4274";
	public static String CHECK_STATUS_01 = "42741001";//未结清
	public static String CHECK_STATUS_02 = "42741002";//已结清
	
	//支付方式
	public static String DEFRAY_TYPE = "5421";
	public static String DEFRAY_TYPE_01 = "54211001";//现金
	public static String DEFRAY_TYPE_02 = "54211002";//信用卡
	
	//配车状态
	public static String VEHICLE_MATCH_STATUS ="5012";
	public static String VEHICLE_MATCH_STATUS_01 ="50121001";//已配车
	public static String VEHICLE_MATCH_STATUS_02 ="50121002";//可交车
	public static String VEHICLE_MATCH_STATUS_03 ="50121003";//交车出库
	public static String VEHICLE_MATCH_STATUS_04 ="50121004";//配车取消
	public static String VEHICLE_MATCH_STATUS_05 ="50121005";//已退车
	public static String VEHICLE_MATCH_STATUS_06 ="50121006";//已销售
	
	//交车状态
	public static String VEHICLE_COMMIT_STATUS = "4275";
	public static String VEHICLE_COMMIT_STATUS_01 = "42751001";//未交完
	public static String VEHICLE_COMMIT_STATUS_02 = "42751002";//已交完
	
	//收款类型
	public static String CHARGE_TYPE ="4273";
	public static String CHARGE_TYPE_01 ="42731001";//订金
	public static String CHARGE_TYPE_02 ="42731002";//车款
	public static String CHARGE_TYPE_03 ="42731003";//退车款
	
	//缺货清单状态
	public static String NO_VEHICLE_STATUS="4252";
	public static String NO_VEHICLE_STATUS_01="42521001";//未确认
	public static String NO_VEHICLE_STATUS_02="42521002";//已确认
	
	//职位类别
	public static String POSITION_TYPE="5420";
	public static String POSITION_TYPE_01="54201001";//经理
	public static String POSITION_TYPE_02="54201002";//销售经理
	public static String POSITION_TYPE_03="54201003";//销售顾问
	public static String POSITION_TYPE_04="54201004";//DCRC主管
	public static String POSITION_TYPE_05="54201005";//DCRC
	
	//发运状态
	public static String ORDER_SHIP_STATUS="3051";
	public static String ORDER_SHIP_STATUS_01="30511001";
	public static String ORDER_SHIP_STATUS_02="30511002";	
	
	// 处理状态
	public static String FAIL_DEAL_STATUS = "4237";
	public static String FAIL_DEAL_STATUS_01 = "42371001";// 未处理
	public static String FAIL_DEAL_STATUS_02 = "42371002";// 同意战败
	public static String FAIL_DEAL_STATUS_03 = "42371003";// 战败驳回
	public static String FAIL_DEAL_STATUS_04 = "42371004";// 战败挽救
	public static String FAIL_DEAL_STATUS_05 = "42371005";// 待DCRC核实

	// 跟进回访计划状态
	public static final Integer ACTIONED_STATUS = 4247;
	public static final Integer ACTIONED_STATUS_01 = 42471001;// 未执行
	public static final Integer ACTIONED_STATUS_02 = 42471002;// 已执行

	// 意向状态
	public static final Integer INTENT_STATUS = 4276;
	public static final Integer INTENT_STATUS_01 = 42761001;// 跟进中
	public static final Integer INTENT_STATUS_02 = 42761002;// 战败
	public static final Integer INTENT_STATUS_03 = 42761003;// 战胜
	public static final Integer INTENT_STATUS_04 = 42761004;// 被合并
	public static final Integer INTENT_STATUS_05 = 42761005;// 待审批

	// 预计来店动作
	public static final Integer PLAN_ACTIVITY_01 = 42161001;// 主动拜访
	public static final Integer PLAN_ACTIVITY_02 = 42161002;// 电话联络
	public static final Integer PLAN_ACTIVITY_03 = 42161003;// 参加活动
	public static final Integer PLAN_ACTIVITY_04 = 42161004;// 再回展厅
	public static final Integer PLAN_ACTIVITY_05 = 42161005;// 试乘试驾
	public static final Integer PLAN_ACTIVITY_06 = 42161006;// 定金订单
	
	// 失联原因
	public static final Integer LOST_CONTACT_REASON = 4259;
	
	// 放弃原因
	public static final Integer GIVE_UP_REASON = 4260;
	
	// 放弃原因
	public static final Integer BUY_COMPETITION_REASON = 4261;
	
	// 战败车辆类型
	public static final Integer LOSE_MODEL = 4218;
	public static final Integer LOSE_MODEL_01 = 42181001;
	public static final Integer LOSE_MODEL_02 = 42181002;
	public static final Integer LOSE_MODEL_03 = 42181003;
	public static final Integer LOSE_MODEL_04 = 42181004;
	public static final Integer LOSE_MODEL_05 = 42181005;
	//代理商订单类型
	public static final Integer AGENT_ORDER_TYPE=9012;
	public static final Integer AEENT_ORDER_TYPE_01=90121001;//常规订单
	public static final Integer AEENT_ORDER_TYPE_02=90121002;//SVO订单
	
	//代理商调拨状态
	public static final Integer EXCHANGE_STATUS_01=50041001;//申请
	public static final Integer EXCHANGE_STATUS_02=50041002;//确认
	public static final Integer EXCHANGE_STATUS_03=50041003;//拒绝
	
	public static final Integer SET_TYPE = 5014; //设定类型
	
	public static final Integer SET_TYPE_BUSINESS = 50141001;//作业看板
	public static final Integer SET_TYPE_POSITION = 50141002;//职位首页
	
	public static final Integer POSITION_MENU = 5015; //职位首页
	
	public static final Integer POSITION_MENU_PERSONER = 50151001;//个人
	public static final Integer POSITION_MENU_TEAM = 50151002;//团队
	public static final Integer POSITION_MENU_BULLETIN = 50151003;//通知通告
	
	//业务看板
	public static final Integer BUSINESS_TYPE =9007;
	
	public static final Integer BUSINESS_TYPE_CUSTOMER =90071001;//新增潜客
	public static final Integer BUSINESS_TYPE_ORDER = 90071002;//新增订单
	public static final Integer BUSINESS_TYPE_CAR = 90071003;//完成交车
	public static final Integer BUSINESS_TYPE_FAIL = 90071004;//战败客户
	public static final Integer BUSINESS_TYPE_STOCK = 90071005;//新车入库
	public static final Integer BUSINESS_TYPE_AGE = 90071006;//库龄100天
	
	
	
	
}
