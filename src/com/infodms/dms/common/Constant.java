package com.infodms.dms.common;

import com.sun.org.apache.bcel.internal.verifier.exc.StaticCodeConstraintException;

public interface Constant {
    public static String codeJsUrl = ""; // 字典表数据js的url

    public static String regionJsUrl = ""; // 省份城市表数据js的url

    public static String LOGON_USER = "LOGON_USER"; // session设置登录用户

    public static String PART_ADD_PER = "PART_ADD_PER";//配件加价率哪个

    public static Integer PAGE_SIZE = 10; // pageSize

    public static Integer BILL_PAGE_SIZE = 10; // 运单生成专用

    public static Integer PAGE_SIZE_MIDDLE = 50; // pageSize

    public static Integer PAGE_SIZE_MAX = 999999; // pageSize 为了某些无需分页的查询

    public static Integer PAGE_SIZE_CITY = 20;//城市里程数分页参数

    public static int errorNum = 50;

    public static Integer SALE_RETURN_PARA_SH_PRICE = 40011001;

    public static Integer PRODUCT_COMBO_PARA = 40002001;

    public static Integer NORMAL_ORDER_ORG_CHK = 40170000;

    public static Integer NORMAL_ORDER_REQ_BEF_WEEK = 20511001;

    public static String WAYBILL_PRINT_REMARK = "合格证、发票、随车工具、保修及用户手册齐全，车辆完整无质损";

    public static final String areaId = "2010010100000001";  //景德镇产地ID
    public static final String areaIdJZD = "2010010100000001";  //景德镇产地ID

    public static final String logiRoleId="4000006607";//物流商角色ID
    

    public static final Double Doubel_base = 10000000000D;//数值大小基准1
    
    //运输方式
    public static final int TRANSPORT_MODE = 1500; 
    public static final String TRANSPORT_MODE_KJ = "15001001";  //快件
    public static final String TRANSPORT_MODE_MJ = "15001002";  //慢件
    public static final String TRANSPORT_MODE_ZT = "15001003";  //中铁
    public static final String TRANSPORT_MODE_JZX = "15001004";  //集装箱
    //计价方式
    public static final int VALUATION_MODE = 1501; 
    public static final String VALUATION_MODE_ZL = "15011001";  //重量
    public static final String VALUATION_MODE_PI = "15011002";  //批次
    //配件-仓库类型
    public static final int PARTS_WAREHOUSE_TYPE = 1502; 
    public static final String PARTS_WAREHOUSE_TYPE_ZXC = "15021001";  //中心仓
    public static final String PARTS_WAREHOUSE_TYPE_DLC = "15021002";  //代理仓
    public static final String PARTS_WAREHOUSE_TYPE_JXS = "15021003";  //经销商仓库
    //配件-订单tt_part_po_dtl转tt_part_oem_po状态
    public static final int PARTS_ORDER_STATUS = 1503; 
    public static final String PARTS_ORDER_STATUS_NO = "15031001";  //未确认
    public static final String PARTS_ORDER_STATUS_YES = "15031002";  //已确认
    public static final String PARTS_ORDER_STATUS_CLOSE = "15031003";//已关闭
    //配件-验收状态
    public static final int PARTS_ORDER_OEM_STATUS = 1504; 
    public static final String PARTS_ORDER_OEM_STATUS_NO = "15041001";  //部分验收
    public static final String PARTS_ORDER_OEM_STATUS_YES = "15041002";  //完全验收
    public static final String PARTS_ORDER_OEM_STATUS_WEI = "15041003";  //未验收
    public static final String PARTS_ORDER_OEM_STATUS_CLOSE = "15041004";  //已关闭
    
    //配件-标签状态
    public static final int PARTS_LABEL_STATUS = 1505; 
    public static final String PARTS_LABEL_STATUS_KC = "15051001";  //备件库存
    public static final String PARTS_LABEL_STATUS_ZY = "15051002";  //备件占用
    public static final String PARTS_LABEL_STATUS_XS = "15051003";  //备件销售
    public static final String PARTS_LABEL_STATUS_LH = "15051004";  //备件拣货
    public static final String PARTS_LABEL_STATUS_YZ = "15051005";  //备件验证
    public static final String PARTS_LABEL_STATUS_SH = "15051006";  //备件损坏
    public static final String PARTS_LABEL_STATUS_CK = "15051007";  //备件出库
    
    //配件-采购订单类型
    public static final int PARTS_PURCH_ORDER_TYPE = 1506; 
    public static final String PARTS_PURCH_ORDER_TYPE_ZC = "15061001";  //正常采购
    public static final String PARTS_PURCH_ORDER_TYPE_WT = "15061002";  //经销商委托(委托订单)
    public static final String PARTS_PURCH_ORDER_TYPE_XZC = "15061003";  //销转采(针对销售采购订单)
    
    
    /**
     * mofied by andy.ten@tom.com 组织树根节点代码，可能每个车厂不同，需更改
     */
    public static String ORG_ROOT_CODE = "4000";

    public static final String OEM_ACTIVITIES = "2010010100070674";
    /**
     * mofied by andy.ten@tom.com 职位业务类型
     */
    public static final int POSE_BUS_TYPE = 1078; // 职位业务类型
    public static final int POSE_BUS_TYPE_SYS = 10781001; //系统管理(ALL)
    public static final int POSE_BUS_TYPE_VS = 10781002; //销售管理职位
    public static final int POSE_BUS_TYPE_WR = 10781003; //车厂售后
    public static final int POSE_BUS_TYPE_DVS = 10781004; //经销商销售
    public static final int POSE_BUS_TYPE_DWR = 10781005; //经销商售后
    public static final int POSE_BUS_TYPE_JSZX = 10781006; //结算中心职位类型
    public static final int POSE_BUS_TYPE_WL = 10781007; //储运物流
    public static final int POSE_BUS_TYPE_CUS = 10781008; //客户关系
    public static final int POSE_BUS_TYPE_CP = 10781009; //车厂配件
    public static final int POSE_BUS_TYPE_DP = 10781010; //经销商配件
    public static final int POSE_BUS_TYPE_WRD = 10781011; //车厂售后东安

    // 经销商类型
    public static final int DEALER_TYPE = 1077; //经销商类型
    public static final int DEALER_TYPE_DVS = 10771001; //经销商整车销售
    public static final int DEALER_TYPE_DWR = 10771002; //经销商售后
    public static final int DEALER_TYPE_JSZX = 10771003;//结算中心
    public static final int DEALER_TYPE_QYZDL = 10771004;//区域总代理
    public static final int DEALER_TYPE_DP = 10771005;//经销商配件
    //系统公用功能
    public static final String COMMON_URI = "/common";

    public static final String Login_Function_Id = "10000000";

    //数据校验长度常量
    public static final int Length_Check_Char_1 = 1; //长度不超过1，即数据库长度为1

    public static final int Length_Check_Char_6 = 6; //长度不超过6，即数据库长度为20或者邮编的长度校验

    public static final int Length_Check_Char_8 = 8; //整数长度不超过8，对应数据库长度为（10,2），小数长度最多为2

    public static final int Length_Check_Char_10 = 10; //长度不超过10，数据库长度为30

    public static final int Length_Check_Char_12 = 12; //长度不超过12,认证号校验

    public static final int Length_Check_Char_15 = 15; //长度不超过15，数据库长度为50

    public static final int Length_Check_Char_17 = 17; //长度不超过17，VIN校验

    public static final int Length_Check_Char_20 = 20; //长度不超过20，数据库长度为60

    public static final int Length_Check_Char_30 = 30; //长度不超过30，数据库长度为100

    public static final int Length_Check_Char_50 = 50; //长度不超过50，数据库长度为150

    public static final int Length_Check_Char_60 = 60; //长度不超过60，数据库长度为200

    public static final int Length_Check_Char_100 = 100; //长度不超过100，数据库长度为300

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

    // 状态
    public static Integer STATUS = 1001; // 状态类型

    public static Integer STATUS_ENABLE = 10011001; // 有效

    public static Integer STATUS_DISABLE = 10011002; // 无效

    public static Integer SYS_USER = 1002; // 用户类型

    public static Integer SYS_USER_SGM = 10021001; // 用户类型主机厂端

    public static Integer SYS_USER_DEALER = 10021002; // 用户类型经销商端

    /* -------------------------------------------------
	 * 审核类型（待审核、审核通过、审核驳回）
	 * 如果需要加其他审核部门或人员建议另外在业务表
	 * 加个审核部门或人员字段，这样可以形成XXX审核通过...
	 * ------------------------------------------------*/
    public static Integer SYS_CHECK_STATUS = 1026;
    public static Integer SYS_CHECK_STATUS_01 = 10261001;
    public static Integer SYS_CHECK_STATUS_02 = 10261002;
    public static Integer SYS_CHECK_STATUS_03 = 10261003;

    //公司类型
    //modified by andy.ten@tom.com
    public static String COMPANY_TYPE = "1053"; //

    /**
     * 公司类型 - 车厂
     */
    public static final String COMPANY_TYPE_SGM = "10531001";


    /**
     * 公司类型 - 经销商
     */
    public static final String COMPANY_TYPE_DEALER = "10531002";

    /**
     * 公司属性
     */
    public static final String COMPANY_PROPERTY = "1191";
    public static final String COMPANY_PROPERTY_01 = "11911001";
    public static final String COMPANY_PROPERTY_02 = "11911002";
    public static final String COMPANY_PROPERTY_03 = "11911003";
	public static final Integer SERVICEACTIVITY_TYPE_06 = 10561006;//模板活动



    //公司

    // end

    // 公共-是否
    public static String IF_TYPE = "1004";

    public static Integer IF_TYPE_YES = 10041001; // 是

    public static Integer IF_TYPE_NO = 10041002; // 否

    // 性别
    public static Integer GENDER_TYPE = 1003;

    public static final Integer MAN = 10031001;

    public static final Integer WOMEN = 10031002;

    public static final Integer NONO = 10031003;

    // 组织类型
    // modified by andy.ten@tom.com
    public static Integer ORG_TYPE = 1019;

    public static Integer ORG_TYPE_OEM = 10191001; // 主机厂

    public static Integer ORG_TYPE_DEALER = 10191002; // 经销商

    //组织层级
    public static Integer DUTY_TYPE = 1043;

    public static Integer DUTY_TYPE_COMPANY = 10431001; // 公司

    public static Integer DUTY_TYPE_DEPT = 10431002; // 部门

    public static Integer DUTY_TYPE_LARGEREGION = 10431003; // 大区

    public static Integer DUTY_TYPE_SMALLREGION = 10431004; // 小区

    public static Integer DUTY_TYPE_DEALER = 10431005; // 经销商
    // end

    //分销商级别
    public static Integer DISTRIBUTION_TYPE = 3097;

    public static Integer DISTRIBUTION_TYPE_A = 30971001; //A
    public static Integer DISTRIBUTION_TYPE_B = 30971002; //B
    public static Integer DISTRIBUTION_TYPE_C = 30971003; //C
    public static Integer DISTRIBUTION_TYPE_D = 30971004; //D

    // 处理结果
    public static String ACCEPT_RES = "2012";

    public static String ACCEPT_RES_UNACTION = "20121001"; // 未处理

    public static String ACCEPT_RES_ACCEPT = "20121002"; // 已处理

    public static String ACCEPT_RES_ABATE = "20121003"; // 已失效

    // 预警等级（一级、二级、三级）
    public static String VR_LEVEL = "9401";

    public static String VR_LEVEL_1 = "94011001"; // 一级

    public static String VR_LEVEL_2 = "94011002"; // 二级

    public static String VR_LEVEL_3 = "94011003"; // 三级

    // 预警类型（整车/配件）
    public static String VR_TYPE = "9402";

    public static String VR_TYPE_1 = "94021001"; // 整车

    public static String VR_TYPE_2 = "94021002"; // 配件

    public static String VR_TYPE_3 = "94021003"; // 配件精品

    // 配件三包类型
    public static String PART_WR_TYPE = "9403";

    public static String PART_WR_TYPE_1 = "94031001"; // 常用配件

    public static String PART_WR_TYPE_2 = "94031002"; // 易损易耗件

    public static String PART_WR_TYPE_3 = "94031003"; // 配件类型

    // 是否关注天数内
    public static Integer ALL_DAYS = 9404;
    public static Integer ALL_DAYS_1 = 94041001; // 在关注期内
    public static Integer ALL_DAYS_2 = 94041002; // 在关注期外

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

    //市场问题工单
    public static String MARKET_ORDER_INFO_TYPE = "1005";//信息类别

    //申请工单类型 合格证/VIP
    public static final String ORDER_SV_TYPE = "1010";
    public static final String ORDER_SV_TYPE_01 = "10101001"; //合格证
    public static final String ORDER_SV_TYPE_02 = "10101002"; //铭牌

    //合格证/VIP申请表操作类型
    public static final String ORDER_SV_ACTION = "1011";
    public static final String ORDER_SV_ACTION_01 = "10111001"; //补办
    public static final String ORDER_SV_ACTION_02 = "10111002"; //更换
    public static final String ORDER_SV_ACTION_03 = "10111003"; //修复
    public static final String ORDER_SV_ACTION_04 = "10111004"; //更改颜色

    // 服务资料邮寄方式
    public static final String ORDER_SI_MAIL_TYPE = "1009";
    public static final String ORDER_SI_MAIL_TYPE_01 = "10091001"; //普通邮寄
    public static final String ORDER_SI_MAIL_TYPE_02 = "10091002"; //快递
    // 品质情报上报目录分组
    public static final String LISTGROUP = "2014";
    public static final String LISTGROUP_01 = "20141001"; //发动机
    public static final String LISTGROUP_02 = "20141002"; //变速器
    public static final String LISTGROUP_03 = "20141003"; //车身钣金
    public static final String LISTGROUP_04 = "20141004"; //车身及装饰
    public static final String LISTGROUP_05 = "20141005"; //底盘系统
    public static final String LISTGROUP_06 = "20141006"; //电气系统
    public static final String LISTGROUP_07 = "20141007"; //空调系统
    public static final String LISTGROUP_08 = "20141008"; //燃油供给
    public static final String LISTGROUP_09 = "20141009"; //工具及辅料

    //退换类型
    public static final String EX_TYPE = "1012";
    public static final String EX_TYPE_01 = "10121001"; //市场问题调换
    public static final String EX_TYPE_02 = "10121002"; //用户要求退换
    public static final String EX_TYPE_03 = "10121003"; //长安主要退换

    //奖惩类型
    public static final String PUNISHMENT = "1006";
    public static final String PUNISHMENT_01 = "10061001"; //奖励
    public static final String PUNISHMENT_02 = "10061002"; //惩罚

    //奖励方式
    public static final String REWARD = "1007";
    public static final String REWARD_01 = "10071001"; //通报表扬
    public static final String REWARD_02 = "10071002"; //现金奖励
    public static final String REWARD_03 = "10071003"; //其他

    //处罚方式
    public static final String PUNISH = "1008";
    public static final String PUNISH_01 = "10081001"; //通报批评
    public static final String PUNISH_02 = "10081002"; //黄牌警告
    public static final String PUNISH_03 = "10081003"; //罚款
    public static final String PUNISH_04 = "10081004"; //解除协议
    public static final String PUNISH_05 = "10081005"; //其他

    //删除标示
    public static final String IS_DEL_01 = "1"; //逻辑删除
    public static final String IS_DEL_00 = "0"; //逻辑未删除
    //信息反馈管理工单状态

    //市场问题工单、退换车申请书
    public static final Integer MARKET_BACK_STATUS_TYPE = 1014;//工单状态类别
    public static final Integer MARKET_BACK_STATUS_UNREPORT = 10141001;
    public static final Integer MARKET_BACK_STATUS_REPORTED = 10141002;
    public static final Integer MARKET_BACK_STATUS_AREA_PASS = 10141003;
    public static final Integer MARKET_BACK_STATUS_AREA_REJECT = 10141004;
    public static final Integer MARKET_BACK_STATUS_TECH_PASS = 10141005;
    public static final Integer MARKET_BACK_STATUS_TECH_REJECT = 10141006;
    public static final Integer MARKET_BACK_STATUS_AREA_DIRECTOR_PASS = 10141007;
    public static final Integer MARKET_BACK_STATUS_AREA_DIRECTOR_REJECT = 10141008;
    public static final Integer MARKET_BACK_STATUS_BALANCE = 10141009;//已结算
    //奖惩审批表
    public static final Integer AWARD_PUNISH_STATUS_TYPE = 1015;//工单状态类别
    public static final Integer AWARD_PUNISH_STATUS_UNREPORT = 10151001;//待上报
    public static final Integer AWARD_PUNISH_STATUS_REPORTED = 10151002;//已上报
    public static final Integer AWARD_PUNISH_STATUS_SERVICE_PASS = 10151003;//售后服务部审核通过
    public static final Integer AWARD_PUNISH_STATUS_SERVICE_REJECT = 10151004;//售后服务部审核驳回
    public static final Integer AWARD_PUNISH_STATUS_CAR_PASS = 10151005;//轿车公司审核通过
    public static final Integer AWARD_PUNISH_STATUS_CAR_REJECT = 10151006;//轿车公司审核驳回

    //服务大区编号
    public static final Integer SERVICE_AREA = 10101003;//服务大区编号10101003

    //服务车申请表、服务活动申请表--轿车
    public static final Integer SERVICE_APPLY_ACTIVE_STATUS_TYPE = 1016;//工单状态类别
    public static final Integer SERVICE_APPLY_ACTIVE_STATUS_UNREPORT = 10161001;
    public static final Integer SERVICE_APPLY_ACTIVE_STATUS_REPORTED = 10161002;
    public static final Integer SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS = 10161003;
    public static final Integer SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT = 10161004;
    public static final Integer SERVICE_APPLY_ACTIVE_SERVICE_STATUS_PASS = 10161005;
    public static final Integer SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT = 10161006;
    public static final Integer SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_PASS = 10161007;
    public static final Integer SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT = 10161008;
    public static final Integer SERVICE_APPLY_ACTIVE_CAR_MATERIAL_UPLOAD = 10161009; // 资料已上传
    public static final Integer SERVICE_APPLY_ACTIVE_CAR_MATERIAL_UPLOAD_GET = 10161010; // 资料区域已签收
    public static final Integer SERVICE_APPLY_ACTIVE_CAR_AREA_REFUS = 10161011;//区域拒签
    public static final Integer SERVICE_APPLY_ACTIVE_CAR_SERVICE_GET = 10161012;//服务部已签收
    public static final Integer SERVICE_APPLY_ACTIVE_CAR_SERVICE_REFUS = 10161013;//服务部拒签
    public static final Integer SERVICE_APPLY_ACTIVE_CAR_AUDIT_GET = 10161014;//事业部已签收
    public static final Integer SERVICE_APPLY_ACTIVE_CAR_AUDIT_REFUS = 10161015;//事业部拒签

    //服务车申请表、服务活动申请表--微车
    public static final Integer SERVICE_APPLY_ACTIVE_STATUS_TYPE_MINI = 1139;//工单状态类别
    public static final Integer SERVICE_APPLY_ACTIVE_STATUS_UNREPORT_MINI = 11391001;
    public static final Integer SERVICE_APPLY_ACTIVE_STATUS_REPORTED_MINI = 11391002;
    public static final Integer SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS_MINI = 11391003;
    public static final Integer SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT_MINI = 11391004;
    public static final Integer SERVICE_APPLY_ACTIVE_SERVICE_STATUS_PASS_MINI = 11391005;
    public static final Integer SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT_MINI = 11391006;
    public static final Integer SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_PASS_MINI = 11391007;
    public static final Integer SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT_MINI = 11391008;
    public static final Integer SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_UPLOAD = 11391009;//已上传
    public static final Integer SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_UPLOAD_GET = 11391010;//已上传
    //服务资料审批表、合格证/VIP
    public static final Integer SERVICEINFO_VIP_STATUS_TYPE = 1017;//工单状态类别
    public static final Integer SERVICEINFO_VIP_STATUS_UNREPORT = 10171001;
    public static final Integer SERVICEINFO_VIP_STATUS_REPORTED = 10171002;
    public static final Integer SERVICEINFO_VIP_AREA_STATUS_PASS = 10171003;
    public static final Integer SERVICEINFO_VIP_AREA_STATUS_REJECT = 10171004;
    public static final Integer SERVICEINFO_VIP_SERVICE_STATUS_PASS = 10171005;
    public static final Integer SERVICEINFO_VIP_SERVICE_STATUS_REJECT = 10171006;
    public static final Integer SERVICEINFO_VIP_AUDIT_STATUS_PASS = 10171007;
    public static final Integer SERVICEINFO_VIP_AUDIT_STATUS_REJECT = 10171008;

    //物料组级别
    public static final String GROUP_LEVEL_02 = "2";
    public static final Integer IS_DEL = 1;//删除标志

    public static final Integer MAT_TYPE = 1023;
    public static final Integer MAT_TYPE_01 = 10231001;//常规车
    public static final Integer MAT_TYPE_02 = 10231002;//定做车

    // 销售部物料分配标示
    public static final Long REGION = -2222222222L;
    public static final String REGION_NAME = "销售部";
    //车辆生命周期
    public static final Integer VEHICLE_LIFE = 1032;
    public static final Integer VEHICLE_LIFE_01 = 10321001;//线上
    public static final Integer VEHICLE_LIFE_02 = 10321002;//车厂库存
    public static final Integer VEHICLE_LIFE_03 = 10321003;//经销商库存
    public static final Integer VEHICLE_LIFE_04 = 10321004;//实销
    public static final Integer VEHICLE_LIFE_05 = 10321005;//经销商在途
    public static final Integer VEHICLE_LIFE_06 = 10321006;//无效状态
    public static final Integer VEHICLE_LIFE_07 = 10321007;//调拨在途
    public static final Integer VEHICLE_LIFE_08 = 10321008;//车厂出库
    public static final Integer VEHICLE_LIFE_09 = 10321009;//已生成运单【介于车厂出库与经销商在途之间】
    public static final Integer VEHICLE_LIFE_10 = 10321010;// 订单锁定
	public static final Integer VEHICLE_LIFE_11 = 10321011;// 电商库
	public static final Integer VEHICLE_LIFE_12 = 10321012;//车厂调拨在途

    //车辆锁定状态
    public static final Integer LOCK_STATUS = 1024;
//	 注释的代表影响原来的车辆锁定状态   需要使用下面铃木的状态
//    public static final Integer LOCK_STATUS_01 = 10241001;//正常状态
//    public static final Integer LOCK_STATUS_02 = 10241002;//发运锁定
//    public static final Integer LOCK_STATUS_03 = 10241003;//预留锁定
//    public static final Integer LOCK_STATUS_04 = 10241004;//质损锁定
//    public static final Integer LOCK_STATUS_05 = 10241005;//实销退车锁定
//    public static final Integer LOCK_STATUS_PFSD = 10241006;//批发锁定
//    public static final Integer LOCK_STATUS_07 = 10241007;//借出锁定
//    public static final Integer LOCK_STATUS_08 = 10241008;//配车锁定
//    public static final Integer LOCK_STATUS_09 = 10241009;//库存调拨锁定
//    public static final Integer LOCK_STATUS_10 = 10241010;//特殊车辆上报锁定
//    public static final Integer LOCK_STATUS_11 = 10241011;//实销更改锁定
//    public static final Integer LOCK_STATUS_12 = 10241012;//实车退车锁定
//    public static final Integer LOCK_STATUS_ZHSD = 10241007;//二手车置换锁定
	public static final Integer LOCK_STATUS_01 = 10241001;// 正常状态
	public static final Integer LOCK_STATUS_02 = 10241002;// 发运锁定
	public static final Integer LOCK_STATUS_03 = 10241003;// 预留锁定
	public static final Integer LOCK_STATUS_04 = 10241004;// 质损锁定
	public static final Integer LOCK_STATUS_05 = 10241005;// 退车锁定
	public static final Integer LOCK_STATUS_PFSD = 10241006;// 批发锁定
	public static final Integer LOCK_STATUS_ZHSD = 10241007;// 二手车置换锁定
	public static final Integer LOCK_STATUS_08 = 10241008;// 调拨锁定
	public static final Integer LOCK_STATUS_09 = 10241009;// 实效信息修改锁定
	public static final Integer LOCK_STATUS_10 = 10241010;// 节能上报锁定
	public static final Integer LOCK_STATUS_11 = 10241011;// 采购退货锁定
	public static final Integer LOCK_STATUS_12 = 10241012;// 特殊车锁定
	public static final Integer LOCK_STATUS_13 = 10241013;// 借出锁定

    public static Integer PERPAR_TYPE = 9409;

    public static Integer PERPAR_TYPE_01 = 94091001; //A
    public static Integer PERPAR_TYPE_02 = 94091002; //B
    public static Integer PERPAR_TYPE_03 = 94091003; //C
    public static Integer PERPAR_TYPE_04 = 94091004; //D
    public static Integer PERPAR_TYPE_05 = 94091005; //D

//    public static String ORDER_STATUS_10 = null;

    public static String SPECIAL_NEED_STATUS_13 = null;


    //create by yangpo ----------------------start--------------------------------------
    //计划类型
    public static final Integer PLAN_TYPE = 1041;
    public static final Integer PLAN_TYPE_BUY = 10411001;//采购计划
    public static final Integer PLAN_TYPE_SALE = 10411002;//销售计划

    //计划确认状态
    public static final Integer PLAN_MANAGE = 1027;
    public static final Integer PLAN_MANAGE_UNCONFIRM = 10271001;//待确认
    public static final Integer PLAN_MANAGE_CONFIRM = 10271002;//已确认

    //预测状态
    public static final Integer FORECAST_STATUS = 1030;
    public static final Integer FORECAST_STATUS_UNCONFIRM = 10301001;//未上报
    public static final Integer FORECAST_STATUS_CONFIRM = 10301002;//已提报

    public static final Integer FORECAST_STATUS_FORMAL = 60591001;//正式预测
	public static final Integer FORECAST_STATUS_ROLL = 60591002;//滑动预测
	
    //create by yangpo----------------------end-----------------------------------------------

    //是否可提报补充订单
    public static final Integer NASTY_ORDER_REPORT_TYPE = 1033;
    public static final Integer NASTY_ORDER_REPORT_TYPE_01 = 10331001;//可提报
    public static final Integer NASTY_ORDER_REPORT_TYPE_02 = 10331002;//不可提报

    //是否可预测订单yudanwen
    public static final Integer FORECAST_FLAG_REPORT_TYPE = 9688;
    public static final Integer FORECAST_FLAG_REPORT_TYPE_01 = 96881001;//可预测
    public static final Integer FORECAST_FLAG_REPORT_TYPE_02 = 96881002;//不预测

    //是否可通过产
    public static final Integer FORECAST_FLAG_REPORT_PRO = 9689;
    public static final Integer FORECAST_FLAG_REPORT_PRO_01 = 96891001;//可生产 
    public static final Integer FORECAST_FLAG_REPORT_PRO_02 = 96891002;//不可生产


    //订单类型
    public static final Integer ORDER_TYPE = 1020;
    public static final Integer ORDER_TYPE_01 = 10201001;	// 常规订单
    public static final Integer ORDER_TYPE_02 = 10201002;	// 一般订做车  补充订单
	public static final Integer ORDER_TYPE_04 = 10201004;	// 常规预测订单
	public static final Integer ORDER_TYPE_05 = 10201005;	// 补充预测订单
    
   
    /**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 描述:银翔原型报错修复
     * 订做车订单
     * Ded：10201003
     * Date:2017-06-29
     */
    public static final Integer ORDER_TYPE_03 = 10201003;	// 订做车订单 特殊订单
    
    public static final Integer ORDER_TYPE_06 = 10201006;// 电商订单

    // 提车单资金扣减状态
    public static final Integer ORDER_ACCOUNT_STATUS = 1600;
    public static final Integer ORDER_ACCOUNT_STATUS_01 = 16001001; // 已取消
    public static final Integer ORDER_ACCOUNT_STATUS_02 = 16001002; // 已冻结
    public static final Integer ORDER_ACCOUNT_STATUS_03 = 16001003; // 已扣减

    // 单据审核状态
    public static final Integer NORMAL_STATUS = 1601;
    public static final Integer NORMAL_STATUS_01 = 16011001;	// 已保存
    public static final Integer NORMAL_STATUS_02 = 16011002;	// 已提交
    public static final Integer NORMAL_STATUS_03 = 16011003;	// 已确认
    public static final Integer NORMAL_STATUS_04 = 16011004;	// 已驳回
    public static final Integer NORMAL_STATUS_05 = 16011005;	// 已作废
    public static final Integer NORMAL_STATUS_06 = 16011006;	// 已调账

    // 提车单单据类型
    public static final Integer ORDER_INVOICE_TYPE = 1602;
    public static final Integer ORDER_INVOICE_TYPE_01 = 16021001;	// 整车订单
    public static final Integer ORDER_INVOICE_TYPE_02 = 16021002;	// 中转出库单

    //组板保险险种
    public static final Integer BOARD_POLICY_TYPE = 1603;
    public static final Integer BOARD_POLICY_TYPE_01 = 16031001;
    public static final Integer BOARD_POLICY_TYPE_02 = 16031002;

    //发运状态
    public static final Integer ORDER_STATUS = 1021;
    public static final Integer ORDER_STATUS_01 = 10211001;// 未分派
    public static final Integer ORDER_STATUS_02 = 10211002;// 已分派提交
    public static final Integer ORDER_STATUS_03 = 10211003;// 已分派审核
    public static final Integer ORDER_STATUS_04 = 10211004;// 部分组板
    public static final Integer ORDER_STATUS_05 = 10211005;// 已组板提交
    public static final Integer ORDER_STATUS_06 = 10211006;// 已组板确认
    public static final Integer ORDER_STATUS_07 = 10211007;// 已发运
    public static final Integer ORDER_STATUS_08 = 10211008;// 已交接
    public static final Integer ORDER_STATUS_09 = 10211009;// 已交车
    public static final Integer ORDER_STATUS_10 = 10211010;// 待财务审核 废弃
    public static final Integer ORDER_STATUS_11 = 10211011;// 已分派 废弃
    public static final Integer ORDER_STATUS_12 = 10211012;// 代交车审核 废弃
    public static final Integer ORDER_STATUS_13 = 10211013;// 作废 废弃
	public static final Integer ORDER_STATUS_14 = 10211014;// 分派驳回
    //public static final Integer ORDER_STATUS_12 = 10211012;//未分派

	// 订单状态
	public static final Integer ORDER_COM_STATUS = 1991;
	public static final Integer ORDER_COM_STATUS_01 = 19911001;// 未提报
	public static final Integer ORDER_COM_STATUS_02 = 19911002;// 预提报
	public static final Integer ORDER_COM_STATUS_03 = 19911003;// 已提报
	public static final Integer ORDER_COM_STATUS_04 = 19911004;// 驳回
	public static final Integer ORDER_COM_STATUS_05 = 19911005;// 审核通过
	public static final Integer ORDER_COM_STATUS_06 = 19911006;// 已取消
	public static final Integer ORDER_COM_STATUS_07 = 19911007;// 财务待确认
	public static final Integer ORDER_COM_STATUS_08 = 19911008;// 待事业部审核-微车
	public static final Integer ORDER_COM_STATUS_09 = 19911009;// 待车厂审核-微车
	public static final Integer ORDER_COM_STATUS_10 = 19911010;// 待财务审核
	
    public static final Integer ORDER_VEHICLE_TYPE = 1221;
    public static final Integer ORDER_VEHICLE_TYPE_01 = 12211001;	// 正常提车
    public static final Integer ORDER_VEHICLE_TYPE_02 = 12211002;	// 试乘试驾
    public static final Integer ORDER_VEHICLE_TYPE_03 = 12211003;	// 售后服务车
    public static final Integer ORDER_VEHICLE_TYPE_04 = 12211004;	// 服务代步车

    //资金账户类型
    public static final Integer ACCOUNT_TYPE = 1025;
    public static final Integer ACCOUNT_TYPE_01 = 10251001;//现款
    public static final Integer ACCOUNT_TYPE_02 = 10251002;//承兑汇票
    public static final Integer ACCOUNT_TYPE_03 = 10251003;//三方信贷_九江银行
    
    /**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 描述:银翔原型报错修复
     * 实际意义未详
     * Ded：10251004
     * Date:2017-06-29
     */
    public static final Integer ACCOUNT_TYPE_04 = 10251004;
    
    public static final Integer ACCOUNT_TYPE_14 = 10251014; // 三方（幻速H2E专款）
    public static final Integer ACCOUNT_TYPE_05 = 10259999;//三方信贷...

    // 账户类型类 CHANA
    public static final Integer ACCOUNT_CLASS = 1400;
    public static final Integer ACCOUNT_CLASS_CSGC = 14001001; // 兵财存货融资
    public static final Integer ACCOUNT_CLASS_DRAFT = 14001002; // 承兑汇票
    public static final Integer ACCOUNT_CLASS_FINANCE = 14001003; // 三方协议车
    public static final Integer ACCOUNT_CLASS_CASH = 14001004; // 现款
    public static final Integer ACCOUNT_CLASS_CREDIT = 14001005; // 信贷销售
    public static final Integer ACCOUNT_CLASS_DISCOUNT = 14001006; // 折扣

    //资金异动类型
    public static final Integer ACCOUNT_CHANGE_TYPE = 1034;
    public static final Integer ACCOUNT_CHANGE_TYPE_01 = 10341001;//打款
    public static final Integer ACCOUNT_CHANGE_TYPE_02 = 10341002;//打款冲销
    public static final Integer ACCOUNT_CHANGE_TYPE_03 = 10341003;//开蓝票
    public static final Integer ACCOUNT_CHANGE_TYPE_04 = 10341004;//开红票
    public static final Integer ACCOUNT_CHANGE_TYPE_05 = 10341005;//开蓝票冲销
    public static final Integer ACCOUNT_CHANGE_TYPE_06 = 10341006;//开红票冲销

    //资金异动类型(新)
    public static final Integer ACCOUNT_CHANGE_TYPE_NEW = 1354;
    public static final Integer ACCOUNT_CHANGE_TYPE_NEW_01 = 13541001;//打款
    public static final Integer ACCOUNT_CHANGE_TYPE_NEW_02 = 13541002;//开票

    //订单发运状态
    public static final Integer DELIVERY_STATUS = 1028;
    public static final Integer DELIVERY_STATUS_01 = 10281001;//待财务确认
    public static final Integer DELIVERY_STATUS_02 = 10281002;//财务已确认
    public static final Integer DELIVERY_STATUS_03 = 10281003;//财务已驳回
    public static final Integer DELIVERY_STATUS_04 = 10281004;//已开票
    public static final Integer DELIVERY_STATUS_05 = 10281005;//已出库
    public static final Integer DELIVERY_STATUS_06 = 10281006;//ERP待处理
    public static final Integer DELIVERY_STATUS_07 = 10281007;//ERP处理成功
    public static final Integer DELIVERY_STATUS_08 = 10281008;//ERP处理失败
    public static final Integer DELIVERY_STATUS_09 = 10281009;//已取消
    public static final Integer DELIVERY_STATUS_10 = 10281010;//部分出库
    public static final Integer DELIVERY_STATUS_11 = 10281011;//部分验收
    public static final Integer DELIVERY_STATUS_12 = 10281012;//完全验收
    public static final Integer DELIVERY_STATUS_13 = 10281013;//取消待财务确认

    //订单发运申请执行状态
    public static final Integer REQ_EXEC_STATUS = 1069;
    public static final Integer REQ_EXEC_STATUS_01 = 10691001;//待发运
    public static final Integer REQ_EXEC_STATUS_02 = 10691002;//已发运

    //发运方式
    public static final Integer TRANSPORT_TYPE = 1029;
    public static final Integer TRANSPORT_TYPE_01 = 10291001;//自提
    public static final Integer TRANSPORT_TYPE_02 = 10291002;//平板
    public static final Integer TRANSPORT_TYPE_03 = 10291003;//船运
    public static final Integer TRANSPORT_TYPE_04 = 10291004;//火车
    public static final Integer TRANSPORT_TYPE_05 = 10291005;//集装箱
    public static final Integer TRANSPORT_TYPE_06 = 10291006;//驾送
    //职位类型
    public static final Integer POSE_TYPE_MANAGER = 10000000;//经理
    public static final Integer POSE_TYPE_NORMAL = 10000001;//普通

    //服务活动--活动类型
    public static final Integer ACTIVITY_TYPE = 1013;
    public static final Integer ACTIVITY_TYPE_MAINTAIN = 10131001;
    public static final Integer ACTIVITY_TYPE_AIR = 10131002;
    public static final Integer ACTIVITY_TYPE_OTHER = 10131003;

    //服务活动--评估类型
    public static final Integer EVALUATE_TYPE = 9410;
    public static final Integer EVALUATE_TYPE_01 = 94101001;
    public static final Integer EVALUATE_TYPE_02 = 94101002;
    public static final Integer EVALUATE_TYPE_03 = 94101003;

    //索赔基本参数设定
    public final static String CLAIM_BASIC_PARAMETER = "1042";
    public final static Integer CLAIM_BASIC_PARAMETER_01 = 10421001;//一般工时单价
    public final static Integer CLAIM_BASIC_PARAMETER_02 = 10421002;//其他费用限定
    public final static Integer CLAIM_BASIC_PARAMETER_03 = 10421003;//VAT税率
    public final static Integer CLAIM_BASIC_PARAMETER_04 = 10421004;//索赔有效天数
    public final static Integer CLAIM_BASIC_PARAMETER_05 = 10421005;//索赔工时税率
    public final static Integer CLAIM_BASIC_PARAMETER_06 = 10421006;//配件税率
    public final static Integer CLAIM_BASIC_PARAMETER_07 = 10421007;//其他费用税率
    public final static Integer CLAIM_BASIC_PARAMETER_08 = 10421008;//配件加价率（东安）
    public final static Integer CLAIM_BASIC_PARAMETER_09 = 10421009;//配件加价率(昌河)
    public final static Integer CLAIM_BASIC_PARAMETER_10 = 10421010;//车型大类

    //用户初始密码
    public final static String USER_PASSWORD = "123456";

    //下发代码类别：
    //wjb add by 2010-05-31
    public final static Integer BUSINESS_CHNG_CODE = 1044;
    public final static Integer BUSINESS_CHNG_CODE_01 = 10441001; //质损程度
    public final static Integer BUSINESS_CHNG_CODE_02 = 10441002;//质损区域
    public final static Integer BUSINESS_CHNG_CODE_03 = 10441003; //质损类型
    public final static Integer BUSINESS_CHNG_CODE_04 = 10441004; //故障代码
    //	下发代码对应的下发状态：
    //wjb add by 2010-06-01
    public final static Integer DOWNLOAD_CODE_STATUS = 1051;
    public final static Integer DOWNLOAD_CODE_STATUS_01 = 10511001;//待下发
    public final static Integer DOWNLOAD_CODE_STATUS_02 = 10511002;//已处理
    public final static Integer DOWNLOAD_CODE_STATUS_03 = 10511003;//已下发

    /*//调拨申请状态
    public final static Integer DISPATCH_STATUS = 1055;
    public final static Integer DISPATCH_STATUS_01 = 10551001;//待大区审核(调出)
    public final static Integer DISPATCH_STATUS_02 = 10551002;//待大区审核(调入)
    public final static Integer DISPATCH_STATUS_03 = 10551003;//待审核(车厂)
    public final static Integer DISPATCH_STATUS_04 = 10551004;//审核通过
    public final static Integer DISPATCH_STATUS_05 = 10551005;//驳回
    public final static Integer DISPATCH_STATUS_06 = 10551006;//待区域审核(调出)
    public final static Integer DISPATCH_STATUS_07 = 10551007;//待区域审核(调入)*/
	// 批发申请状态
	public final static Integer DISPATCH_STATUS = 1055;
	public final static Integer DISPATCH_STATUS_01 = 10551001;// 待审核
	public final static Integer DISPATCH_STATUS_02 = 10551002;// 区域审核通过
	public final static Integer DISPATCH_STATUS_03 = 10551003;// 审核通过
	public final static Integer DISPATCH_STATUS_04 = 10551004;// 驳回
	public final static Integer DISPATCH_STATUS_05 = 10551005;// 批发入库
	public final static Integer DISPATCH_STATUS_06 = 10551006;// 初审通过

    //服务活动管理--活动类型
    public static final Integer SERVICEACTIVITY_TYPE = 1056;
    public static final Integer SERVICEACTIVITY_TYPE_01 = 10561001;//技术升级
    public static final Integer SERVICEACTIVITY_TYPE_02 = 10561002;//客户关怀
    public static final Integer SERVICEACTIVITY_TYPE_03 = 10561003;//俱乐部活动
    public static final Integer SERVICEACTIVITY_TYPE_04 = 10561004;//流动服务
    public static final Integer SERVICEACTIVITY_TYPE_05 = 10561005;//替换件

    //服务活动管理--活动类别
    public static final Integer SERVICEACTIVITY_KIND = 1057;
    public static final Integer SERVICEACTIVITY_KIND_01 = 10571001;//免费检测
    public static final Integer SERVICEACTIVITY_KIND_02 = 10571002;//免费保养
    public static final Integer SERVICEACTIVITY_KIND_03 = 10571003;//产品整改
    public static final Integer SERVICEACTIVITY_KIND_04 = 10571004;//赠送礼物
    public static final Integer SERVICEACTIVITY_KIND_05 = 10571005;//客户关怀
    public static final Integer SERVICEACTIVITY_KIND_06 = 10571006;//促销活动
    public static final Integer SERVICEACTIVITY_KIND_07 = 10571007;//其他
    //服务活动管理--处理方式
    public static final Integer SERVICEACTIVITYDEAL_WITH = 1067;
    public static final Integer SERVICEACTIVITYDEAL_WITH_01 = 10671001;//维修
    public static final Integer SERVICEACTIVITYDEAL_WITH_02 = 10671002;//零件更换
    public static final Integer SERVICEACTIVITYDEAL_WITH_03 = 10671003;//整车收回
    public static final Integer SERVICEACTIVITYDEAL_WITH_04 = 10671004;//保养

    //服务活动管理--服务活动活动状态
    public static final Integer SERVICEACTIVITY_STATUS = 1068;
    public static final Integer SERVICEACTIVITY_STATUS_01 = 10681001;//尚未发布
    public static final Integer SERVICEACTIVITY_STATUS_02 = 10681002;//已经发布
    public static final Integer SERVICEACTIVITY_STATUS_03 = 10681003;//重新发布
    public static final Integer SERVICEACTIVITY_STATUS_04 = 10681004;//已经结束

    //服务活动管理--车辆性质
    public static final Integer SERVICEACTIVITY_CHARACTOR = 1072;
    //轿车
    //public static final Integer SERVICEACTIVITY_CHARACTOR_01=10721001;//出租车
    //public static final Integer SERVICEACTIVITY_CHARACTOR_02=10721002;//私家车
    //public static final Integer SERVICEACTIVITY_CHARACTOR_03=10721003;//公务车
    //public static final Integer SERVICEACTIVITY_CHARACTOR_03=10721004;//试乘试驾车
    //public static final Integer SERVICEACTIVITY_CHARACTOR_03=10721005;//售后服务车
    //微车
    public static final Integer SERVICEACTIVITY_CHARACTOR_01 = 10721001;//政府采购
    public static final Integer SERVICEACTIVITY_CHARACTOR_02 = 10721002;//太阳能行业
    public static final Integer SERVICEACTIVITY_CHARACTOR_03 = 10721003;//家电行业
    public static final Integer SERVICEACTIVITY_CHARACTOR_04 = 10721004;//摩托（电动）车行业
    public static final Integer SERVICEACTIVITY_CHARACTOR_05 = 10721005;//客运线路车
    public static final Integer SERVICEACTIVITY_CHARACTOR_06 = 10721006;//货运线路车
    public static final Integer SERVICEACTIVITY_CHARACTOR_07 = 10721007;//食品饮料行业
    public static final Integer SERVICEACTIVITY_CHARACTOR_08 = 10721008;//日化用品行业
    public static final Integer SERVICEACTIVITY_CHARACTOR_09 = 10721009;//IT通讯行业
    public static final Integer SERVICEACTIVITY_CHARACTOR_10 = 10721010;//金融（银行、保险）行业
    public static final Integer SERVICEACTIVITY_CHARACTOR_11 = 10721011;//CNG
    public static final Integer SERVICEACTIVITY_CHARACTOR_12 = 10721012;//仓栅等改装车
    public static final Integer SERVICEACTIVITY_CHARACTOR_13 = 10721013;//专用车（公检法司）行业
    public static final Integer SERVICEACTIVITY_CHARACTOR_14 = 10721014;//专用车（卫生救护）行业
    public static final Integer SERVICEACTIVITY_CHARACTOR_15 = 10721015;//专用车（邮政）行业
    public static final Integer SERVICEACTIVITY_CHARACTOR_16 = 10721016;//零卖车
    public static final Integer SERVICEACTIVITY_CHARACTOR_17 = 10721017;//其他行业

    //服务活动管理--处理方式
    public static final Integer SERVICEACTIVITY_CAR_type = 3536;
    public static final Integer SERVICEACTIVITY_CAR_type_01 = 3536001;//维修
    public static final Integer SERVICEACTIVITY_CAR_type_02 = 3536002;//零件更换
    public static final Integer SERVICEACTIVITY_CAR_type_03 = 3536003;//整车收回
    public static final Integer SERVICEACTIVITY_CAR_type_04 = 3536004;//保养

    //服务活动管理--活动项目
    public static final Integer SERVICEACTIVITY_CAR_cms = 3537;
    public static final Integer SERVICEACTIVITY_CAR_cms_01 = 3537001;//免费检测
    public static final Integer SERVICEACTIVITY_CAR_cms_02 = 3537002;//免费保养
    public static final Integer SERVICEACTIVITY_CAR_cms_03 = 3537003;//技术升级
    public static final Integer SERVICEACTIVITY_CAR_cms_04 = 3537004;//免费赠送
    public static final Integer SERVICEACTIVITY_CAR_cms_05 = 3537005;//配件赠送
    public static final Integer SERVICEACTIVITY_CAR_cms_06 = 3537006;//工时费打折
    public static final Integer SERVICEACTIVITY_CAR_cms_07 = 3537007;//材料费打折
    //服务活动管理--生产基地
    public static final Integer SERVICEACTIVITY_CAR_YIELDLY = 1131;
    public static final Integer SERVICEACTIVITY_CAR_YIELDLY_01 = 11311001;//景德镇
    public static final Integer SERVICEACTIVITY_CAR_YIELDLY_02 = 11311002;//九江
    public static final Integer SERVICEACTIVITY_CAR_YIELDLY_03 = 11311003;//合肥

    //服务活动管理--服务活动车辆范围
    public static final Integer SERVICEACTIVITY_VEHICLE_AREA = 1132;
    public static final Integer SERVICEACTIVITY_VEHICLE_AREA_01 = 11321001;//售前车
    public static final Integer SERVICEACTIVITY_VEHICLE_AREA_02 = 11321002;//售后车

    //服务活动管理--车龄日期类型定义
    //PGM add by 2010-06-03
    public static final Integer SERVICEACTIVITY_DATE_TYPE = 1080;
    public static final Integer SERVICEACTIVITY_DATE_TYPE_01 = 10801001;//销售日期
    public static final Integer SERVICEACTIVITY_DATE_TYPE_02 = 10801002;//生产日期

    //服务活动管理--车辆信息确认[维修状态]
    //PGM add by 2010-06-10
    public static final Integer SERVICEACTIVITY_REPAIR_STATUS = 1048;
    public static final Integer SERVICEACTIVITY_REPAIR_STATUS_01 = 10481001;//已修
    public static final Integer SERVICEACTIVITY_REPAIR_STATUS_02 = 10481002;//未修

    //服务活动管理--车辆信息确认[销售状态]
    //PGM add by 2010-06-10
    public static final Integer SERVICEACTIVITY_SALE_STATUS = 1049;
    public static final Integer SERVICEACTIVITY_SALE_STATUS_01 = 10491001;//已售
    public static final Integer SERVICEACTIVITY_SALE_STATUS_02 = 10491002;//未售

    //服务活动管理--服务活动车辆活动状态
    //PGM add by 2010-06-10
    public static final Integer SERVICEACTIVITY_CAR_STATUS = 1105;
    public static final Integer SERVICEACTIVITY_CAR_STATUS_03 = 11051001;//尚未下发
    public static final Integer SERVICEACTIVITY_CAR_STATUS_01 = 11051102;//已经下发
    public static final Integer SERVICEACTIVITY_CAR_STATUS_02 = 11051203;//已经修理完成

    //服务活动车辆修完之后下发状态
    //liuqiang add by 2010-07-30
    public static final Integer IS_SEND_OUT_00 = 0;//未下发
    public static final Integer IS_SEND_OUT_01 = 1;//已下发

    //车型组类型
    //wjb add by 2010-06-01
    public final static Integer WR_MODEL_GROUP_TYPE = 1045;
    public final static Integer WR_MODEL_GROUP_TYPE_01 = 10451001; //售后车型组
    public final static Integer WR_MODEL_GROUP_TYPE_02 = 10451002; //销售车型组

    //索赔工时树 层次码
    //	wjb add by 2010-06-01
    public final static String CLAIM_LABHOUR_TREE_CODE_01 = "1"; //大类
    public final static String CLAIM_LABHOUR_TREE_CODE_02 = "2"; //小类
    public final static String CLAIM_LABHOUR_TREE_CODE_03 = "3"; //工时
    public final static String CLAIM_LABHOUR_TREE_CODE_04 = "4"; //附加工时

    //客户关系管理 add by zouchao 2010-06-02
    public final static Integer COMP_LEVEL_TYPE = 1058; // 投诉等级
    public final static Integer COMP_LEVEL_TYPE_01 = 10581001; // 标准投诉
    public final static Integer COMP_LEVEL_TYPE_02 = 10581002; // 补充投诉
    public final static Integer COMP_LEVEL_TYPE_03 = 10581003; // 重大投诉

    public final static Integer COMP_SOURCE_TYPE = 1059; // 投诉来源
    public final static Integer COMP_SOURCE_TYPE_01 = 10591001; // 呼叫中心
    public final static Integer COMP_SOURCE_TYPE_02 = 10591002; // 网络
    public final static Integer COMP_SOURCE_TYPE_03 = 10591003; // 连锁平台
    public final static Integer COMP_SOURCE_TYPE_04 = 10591004; // 重大媒体

    public final static Integer COMP_STATUS_TYPE = 1060; // 投诉状态
    public final static Integer COMP_STATUS_TYPE_01 = 10601001; // 未分配
    public final static Integer COMP_STATUS_TYPE_02 = 10601002; // 处理中
    public final static Integer COMP_STATUS_TYPE_03 = 10601003; // 已关闭
    public final static Integer COMP_STATUS_TYPE_04 = 10601004; // 处理完成

    public final static Integer AUDIT_RESULT_TYPE = 1061; // 处理结果
    public final static Integer AUDIT_RESULT_TYPE_01 = 10611001; // 用户未到服务站
    public final static Integer AUDIT_RESULT_TYPE_02 = 10611002; // 备件未到
    public final static Integer AUDIT_RESULT_TYPE_03 = 10611003; // 备件缺货
    public final static Integer AUDIT_RESULT_TYPE_04 = 10611004; // 建议关闭
    public final static Integer AUDIT_RESULT_TYPE_05 = 10611005; // 正在处理
    public final static Integer AUDIT_RESULT_TYPE_06 = 10611006; // 是

    public final static Integer AUDIT_ACTION_TYPE = 1062; // 发生动作
    public final static Integer AUDIT_ACTION_TYPE_01 = 10621001; // 电话
    public final static Integer AUDIT_ACTION_TYPE_02 = 10621002; // 拜访
    public final static Integer AUDIT_ACTION_TYPE_03 = 10621003; // 邀请活动
    public final static Integer AUDIT_ACTION_TYPE_04 = 10621004; // 邀约到店

    public final static Integer AGE_TYPE = 1063; // 年龄
    public final static Integer AGE_TYPE_01 = 10631001; // 18-25
    public final static Integer AGE_TYPE_02 = 10631002; // 25-30
    public final static Integer AGE_TYPE_03 = 10631003; // 30-35
    public final static Integer AGE_TYPE_04 = 10631004; // 35-40
    public final static Integer AGE_TYPE_05 = 10631005; // 40以上

    public final static Integer AUDIT_STATUS_TYPE = 1064; // 处理状态
    public final static Integer AUDIT_STATUS_TYPE_01 = 10641001; // 未分配
    public final static Integer AUDIT_STATUS_TYPE_02 = 10641002; // 区域处理中
    public final static Integer AUDIT_STATUS_TYPE_03 = 10641003; // 服务站处理中
    public final static Integer AUDIT_STATUS_TYPE_04 = 10641004; // 区域/服务站关闭
    public final static Integer AUDIT_STATUS_TYPE_05 = 10641005; // 总部关闭

    public final static Integer CLA_TYPE = 1066;
    public final static Integer CLA_TYPE_01 = 10661001; //一般维修
    public final static Integer CLA_TYPE_02 = 10661002; //免费保养
    public final static Integer CLA_TYPE_03 = 10661003; //追加索赔
    public final static Integer CLA_TYPE_04 = 10661004; //重复修理索赔 改为急件索赔
    public final static Integer CLA_TYPE_05 = 10661005; //零件索赔更换
    public final static Integer CLA_TYPE_06 = 10661006; //服务活动
    public final static Integer CLA_TYPE_07 = 10661007; //售前维修
    public final static Integer CLA_TYPE_08 = 10661008; //保外索赔
    public final static Integer CLA_TYPE_09 = 10661009; //外出维修
    public final static Integer CLA_TYPE_10 = 10661010; //特殊服务
    public final static Integer CLA_TYPE_11 = 10661011; //PDI
    public final static Integer CLA_TYPE_12 = 10661012; //急件工单
    public final static Integer CLA_TYPE_13 = 10661013; //配件索赔

    public final static Integer COMP_TYPE_TYPE = 1065; // 投诉内容
    public final static Integer COMP_TYPE_TYPE_01 = 10651001; // 轿车质量问题
    public final static Integer COMP_TYPE_TYPE_02 = 10651002; // 轿车事故投诉和媒体来电投诉
    public final static Integer COMP_TYPE_TYPE_03 = 10651003; // 轿车服务问题
    public final static Integer COMP_TYPE_TYPE_04 = 10651004; // 轿车配件问题
    public final static Integer COMP_TYPE_TYPE_05 = 10651005; // 销售咨询
    public final static Integer COMP_TYPE_TYPE_06 = 10651006; // 销售投诉
    public final static Integer COMP_TYPE_TYPE_07 = 10651007; // 轿车外出救急
    public final static Integer COMP_TYPE_TYPE_08 = 10651008; // 轿车服务商经销商来电
    public final static Integer COMP_TYPE_TYPE_09 = 10651009; // 轿车活动咨询
    public final static Integer COMP_TYPE_TYPE_10 = 10651010; // 轿车其他问题
    //add by liuqiang
    public final static Integer FREIGHT_TYPE = 1046; //货运方式
    public final static Integer FREIGHT_TYPE_01 = 10461001; //自提
    public final static Integer FREIGHT_TYPE_02 = 10461002; //空运
    public final static Integer FREIGHT_TYPE_03 = 10461003; //铁路快件
    public final static Integer FREIGHT_TYPE_04 = 10461004; //集装箱
    public final static Integer FREIGHT_TYPE_05 = 10461005; //富宝
    public final static Integer FREIGHT_TYPE_06 = 10461006; //慢件
    public final static Integer FREIGHT_TYPE_07 = 10461007; //特快专递
    public final static Integer FREIGHT_TYPE_08 = 10461008; //汽车
    public final static Integer FREIGHT_TYPE_09 = 10461009; //水运
    public final static Integer FREIGHT_TYPE_10 = 10461010; //中铁快运

    //库存状态更改(用于“详细车籍TT_VS_VHCL_CHNG”)
    //add by liubo 2010-06-03
    public final static Integer STORAGE_CHANGE_TYPE = 1070;
    public final static Integer STORAGE_CHANGE_TYPE_01 = 10701001;//验收入库
    public final static Integer STORAGE_CHANGE_TYPE_02 = 10701002;//调拨出库
    public final static Integer STORAGE_CHANGE_TYPE_03 = 10701003;//调拨入库
    public final static Integer STORAGE_CHANGE_TYPE_04 = 10701004;//销售出库
    public final static Integer STORAGE_CHANGE_TYPE_05 = 10701005;//车辆位置变更
    public final static Integer STORAGE_CHANGE_TYPE_06 = 10701006;//调拨审批通过
    public final static Integer STORAGE_CHANGE_TYPE_07 = 10701007;//调拨审批驳回
    public final static Integer STORAGE_CHANGE_TYPE_08 = 10701008;//调拨申请
    public final static Integer STORAGE_CHANGE_TYPE_09 = 10701009;// 退货审核通过
    public final static Integer STORAGE_CHANGE_TYPE_10 = 10701010;// 调拨出库
    public final static Integer STORAGE_CHANGE_TYPE_11 = 10701011;// 调拨入库
	public final static Integer STORAGE_CHANGE_TYPE_12 = 10701012;// 调拨通知
	public final static Integer STORAGE_CHANGE_TYPE_13 = 10701006;// 批发初审通过

    //销售状态更改
    public final static Integer SALES_STATUS_CHANGE_TYPE = 1071;
    public final static Integer SALES_STATUS_CHANGE_TYPE_01 = 10711001;//发运
    public final static Integer SALES_STATUS_CHANGE_TYPE_02 = 10711002;//开票
    public final static Integer SALES_STATUS_CHANGE_TYPE_03 = 10711003;//验收
    public final static Integer SALES_STATUS_CHANGE_TYPE_04 = 10711004;//实效上报
    public final static Integer SALES_STATUS_CHANGE_TYPE_05 = 10711005;//实销信息更改申请
    public final static Integer SALES_STATUS_CHANGE_TYPE_06 = 10711006;//实销信息更改审批通过
    public final static Integer SALES_STATUS_CHANGE_TYPE_07 = 10711007;//实销信息更改审批驳回

    public final static Integer BACK_TRANSPORT_TYPE = 1073; //回运类型
    public final static Integer BACK_TRANSPORT_TYPE_01 = 10731001; //紧急回运
    public final static Integer BACK_TRANSPORT_TYPE_02 = 10731002; //常规回运

    public final static Integer CLAIM_APPLY_ORD_TYPE = 1079; //索赔申请单状态
    public final static Integer CLAIM_APPLY_ORD_TYPE_01 = 10791001; //未上报
    public final static Integer CLAIM_APPLY_ORD_TYPE_02 = 10791002; //已上报
    public final static Integer CLAIM_APPLY_ORD_TYPE_03 = 10791003; //审核中
    public final static Integer CLAIM_APPLY_ORD_TYPE_04 = 10791004; //审核通过
    public final static Integer CLAIM_APPLY_ORD_TYPE_05 = 10791005; //审核拒绝
    public final static Integer CLAIM_APPLY_ORD_TYPE_06 = 10791006; //审核退回
    public final static Integer CLAIM_APPLY_ORD_TYPE_07 = 10791007; //结算支付
    public final static Integer CLAIM_APPLY_ORD_TYPE_08 = 10791008; //结算审核中
    public final static Integer CLAIM_APPLY_ORD_TYPE_09 = 10791009; //结算折扣中
    public final static Integer CLAIM_APPLY_ORD_TYPE_10 = 10791010; //结算折扣
    public final static Integer CLAIM_APPLY_ORD_TYPE_11 = 10791011; //结算支付(室主任)
    public final static Integer CLAIM_APPLY_ORD_TYPE_12 = 10791012; //折扣驳回(科员)
    public final static Integer CLAIM_APPLY_ORD_TYPE_13 = 10791013; // 索赔单最终状态(结算)
    public final static Integer CLAIM_APPLY_ORD_TYPE_14 = 10791014; //结算折扣(室主任)
    public final static Integer CLAIM_APPLY_ORD_TYPE_15 = 10791015;
    public final static Integer CLAIM_APPLY_ORD_TYPE_16 = 10791016; //索赔单最终状态(折扣)
    
    public final static Integer APP_CLAIM_TYPE = 1999; //索赔申请单状态
    public final static Integer APP_CLAIM_TYPE_01 = 19991001; //未上报
    public final static Integer APP_CLAIM_TYPE_02 = 19991002; //审核中
    public final static Integer APP_CLAIM_TYPE_03 = 19991003; //审核退回
    public final static Integer APP_CLAIM_TYPE_04 = 19991004; //一级审核通过
    public final static Integer APP_CLAIM_TYPE_05 = 19991005; //二级级审核通过
    public final static Integer APP_CLAIM_TYPE_06 = 19991006; //审核拒绝
    public final static Integer APP_CLAIM_TYPE_07 = 19991007; //结算支付
    public final static Integer APP_CLAIM_TYPE_08 = 19991008; //结算审核中


    //信函总类
    public final static Integer CLAIM_LETTER_TYPE = 9406; //信函总类
    public final static Integer CLAIM_LETTER_TYPE_01 = 94061001;
    public final static Integer CLAIM_LETTER_TYPE_02 = 94061002;
    public final static Integer CLAIM_LETTER_TYPE_03 = 94061003;
    public final static Integer CLAIM_LETTER_TYPE_04 = 94061004;

    //信函好坏
    public final static Integer CLAIM_LETTERSF_TYPE = 9407; //信函总类
    public final static Integer CLAIM_LETTERSF_TYPE_01 = 94071001;
    public final static Integer CLAIM_LETTERSF_TYPE_02 = 94071002;

    //配件类型：
    //add by wjb at 2010-06-05
    public final static Integer PART_TYPE = 1052; //配件类型
    public final static Integer PART_TYPE_01 = 10521001; //基础
    public final static Integer PART_TYPE_02 = 10521002; //常用
    public final static Integer PART_TYPE_03 = 10521003; //易损

    //配件出入库类型 add by lishuai at 2010-06-06
    public final static Integer DO_STATUS = 1076;
    public final static Integer DO_STATUS_01 = 10761001; //出库
    public final static Integer DO_STATUS_02 = 10761002; //入库

    public final static Integer GENEREAL_ORDER_WEEK_PARA = 20011001; //常规订单提报周度参数
    public final static Integer URGENT_ORDER_WEEK_PARA = 20011002; //补充订单提报周度参数
    public final static Integer GENEREAL_ORDER_BEFORE_WEEK_PARA = 20021001; //常规订单提报提前周度参数
    public final static Integer URGENT_ORDER_WEEK_BEFORE_PARA = 20021002; //补充订单提报提前周度参数
    public final static Integer URGENT_ORDER_ACCOUNT_CHECK_PARA = 20031001; //资金检查开关
    public final static Integer ORDER_DEFERRED_PARA = 20041001; //订单延期周度参数

    public final static Integer QUTOA_AHEAD_ISSUE_MONTH_PARA = 20081001; //配额提前下发月份
    public final static Integer QUTOA_AIM_WEIGHT_PARA = 20101001; //目标权重
    public final static Integer QUTOA_HOPE_WEIGHT_PARA = 20101002; //预测权重
    public final static Integer QUTOA_FACT_WEIGHT_PARA = 20101003; //实销权重
    public final static Integer QUTOA_MIN_WEIGHT_PARA = 20161001; //配额最小提报量百分比
    public final static Integer QUTOA_MAX_WEIGHT_PARA = 20161002; //配额最小提报量百分比
    public final static Integer MONTH_GENEREAL_ORDER_MONTH_PARA = 20191001; //月度常规订单提报月度参数
    public final static Integer MONTH_GENEREAL_ORDER_BEFORE_MONTH_PARA = 20201001; //月度常规订单提报提前月度参数
    public final static Integer RESOURCE_RESERVE_CHECK_GENERAL_ORDER_PARA = 20221001; //资源审核验证未审常规订单开关参数
    public final static Integer ORDER_REPORT_MAX_DISCOUNT_RATE_PARA = 20231001; //订单启票最大折扣点参数
    public final static Integer SPECIAL_NEED_FLEET_CHECK_PARA = 20241001; //订做车是否需要大客户审核参数
    public final static Integer URLLIMIT = 20241002; //是否需要对售后或销售的URL访问进行限制
    public final static Integer MONTH_MAKE_PARA = 20211003;

    //回运清单状态 add by zhaolunda 2010-06-07
    public final static Integer BACK_LIST_STATUS = 1081;
    public final static Integer BACK_LIST_STATUS_01 = 10811001; //未上报
    public final static Integer BACK_LIST_STATUS_02 = 10811002; //已上报
    public final static Integer BACK_LIST_STATUS_03 = 10811003; //已签收未审核
    public final static Integer BACK_LIST_STATUS_04 = 10811004; //部分审核
    public final static Integer BACK_LIST_STATUS_05 = 10811005; //已审核
//    public final static Integer BACK_LIST_STATUS_06 = 10811006; //冻结
    public static final Integer BACK_LIST_STATUS_07 = 10811007;//运费已审核

    //客户类型(用于"实销信息管理") add by liubo 2010-06-07
    public final static Integer CUSTOMER_TYPE = 1083;
    public final static Integer CUSTOMER_TYPE_01 = 10831001; //个人客户
    public final static Integer CUSTOMER_TYPE_02 = 10831002; //公司客户

    //保养类型：
    //add by wjb at 2010-06-07
    public final static Integer FEE_STATUS = 1084;
    public final static Integer FEE_STATUS_01 = 10841001; //首次保养
    public final static Integer FEE_STATUS_02 = 10841002; //二次保养
    public final static Integer FEE_STATUS_03 = 10841003; //三次保养

    //特殊质保车标识  add by XZM at 2010-06-07
    public final static Integer IS_SPECIALCASE_FALSE = new Integer(0); //否
    public final static Integer IS_SPECIALCASE_TURE = new Integer(1); //是

    //索赔预售权项目类型 add by XZM at 2010-06-07
    public final static Integer PRE_AUTH_ITEM = 1087; //索赔预售权项目类型 类别代码
    public final static Integer PRE_AUTH_ITEM_01 = 10871001;//维修项目(工时)
    public final static Integer PRE_AUTH_ITEM_02 = 10871002;//维修材料（配件）
    public final static Integer PRE_AUTH_ITEM_03 = 10871003;//其他费用（其它项目）

    //预授权审核状态 add by XZM at 2010-06-08
    public final static Integer PRE_AUTH_STATUS = 1086; //预授权审核状态  类别代码
    public final static Integer PRE_AUTH_STATUS_01 = 10861001;//未上报
    public final static Integer PRE_AUTH_STATUS_02 = 10861002;//已上报
    public final static Integer PRE_AUTH_STATUS_03 = 10861003;//已审核

    //经销商级别
    public final static Integer DEALER_LEVEL = 1085;
    public final static Integer DEALER_LEVEL_01 = 10851001; //一级经销商
    public final static Integer DEALER_LEVEL_02 = 10851002; //二级经销商

    public final static Integer PAY_MENT = 9411;
    public final static Integer PAY_MENT_01 = 94111001; //正常付款
    public final static Integer PAY_MENT_02 = 94111002; //全部转配件
    //税率
    public final static Integer TAX_RATE = 9412;
    public final static Integer TAX_RATE_01 = 94121001; //正常付款
    public final static Integer TAX_RATE_02 = 94121002; //全部转配件
    public final static Integer TAX_RATE_03 = 94121003; //全部转配件

    //项目类别
    public final static Integer TAX_RATE_GIFT = 9413;
    public final static Integer TAX_RATE_GIFT_01 = 94131001; //礼品
    public final static Integer TAX_RATE_GIFT_02 = 94131002; //金额
    public final static Integer TAX_RATE_GIFT_03 = 94131003; //宣传费用

    //结算费用类型
    public final static Integer TAX_RATE_FEE = 9414;
    public final static Integer TAX_RATE_FEE_01 = 94141001; //劳务费
    public final static Integer TAX_RATE_FEE_02 = 94141002; //材料费

    //索赔申请单 是否为主损配件  或  工时标识  add by XZM at 2010-06-08
    public final static Integer IS_MAIN_TROUBLE = 1; //是
    public final static Integer IS_NOT_MAIN_TROUBLE = 0;//否

    //配件提报周期类型 add by lishuai 2010-6-8
    public final static String CYCLE_TYPE = "1088";
    public final static String CYCLE_TYPE_QUARTER = "10881003";//季度
    public final static String CYCLE_TYPE_MONTH = "10881002";//月度
    public final static String CYCLE_TYPE_WEEK = "10881001";//周度

    // add by zouchao 2010-06-08
    public final static String SERVICE_COMP_TYPE = "1089"; // 服务投诉
    public final static String SERVICE_COMP_TYPE_01 = "10891001"; // 服务质量
    public final static String SERVICE_COMP_TYPE_02 = "10891002"; // 服务态度

    //付款方式(用于实销信息上报) add by liubo 2010-06-08
    public final static Integer PAYMENT = 1036;
    public final static Integer PAYMENT_01 = 10361001; //分期付款
    public final static Integer PAYMENT_02 = 10361002; //一次性付款
    public final static Integer PAYMENT_03 = 10361003; //按揭
 // 购置方式(用于实销信息上报) add by liubo 2010-06-08
 	public final static Integer PAYMENT_04 = 10361004; // 二手车置换
 	public final static Integer PAYMENT_05 = 10361005; // 二手车置换转按揭
 	
    //公司规模(实销信息上报)add by liubo 2010-06-08
    public final static Integer COMPANY_SCOPE = 1090;
    public final static Integer COMPANY_SCOPE_01 = 10901001; //1至49人
    public final static Integer COMPANY_SCOPE_02 = 10901002; //50至99人
    public final static Integer COMPANY_SCOPE_03 = 10901003; //100-499人
    public final static Integer COMPANY_SCOPE_04 = 10901004; //500-999人
    public final static Integer COMPANY_SCOPE_05 = 10901005; //1000-4999人
    public final static Integer COMPANY_SCOPE_06 = 10901006; //5000人以上

    //公司性质(实销信息上报)add by liubo 2010-06-09
    public final static Integer COMPANY_KIND = 1092;
    public final static Integer COMPANY_KIND_01 = 10921001;//国有企业
    public final static Integer COMPANY_KIND_02 = 10921002;//集体企业
    public final static Integer COMPANY_KIND_03 = 10921003;//有限责任公司
    public final static Integer COMPANY_KIND_04 = 10921004;//股份有限公司
    public final static Integer COMPANY_KIND_05 = 10921005;//私营企业
    public final static Integer COMPANY_KIND_06 = 10921006;//中外合资企业
    public final static Integer COMPANY_KIND_07 = 10921007;//外商投资企业

    // add by zouchao 2010-06-08
    public final static String FLEET_TYPE = "1091"; // 集团客户类型

    public final static String FLEET_TYPE_01 = "10911001"; // 专用车
    public final static String FLEET_TYPE_02 = "10911002"; // 政府采购
    public final static String FLEET_TYPE_03 = "10911003"; // 教练车
    public final static String FLEET_TYPE_04 = "10911004"; // 城市货的
    public final static String FLEET_TYPE_05 = "10911005"; // 物流企业
    public final static String FLEET_TYPE_06 = "10911006"; // 大型集团
    public final static String FLEET_TYPE_07 = "10911007"; // 其他

    public final static String FLEET_TYPE_08 = "10911008"; // 政府采购、省市警务
    public final static String FLEET_TYPE_09 = "10911009"; // 企事业、员工
    public final static String FLEET_TYPE_10 = "10911010"; // 出租、租赁
    public final static String FLEET_TYPE_11 = "10911011"; // 团购
    public final static String FLEET_TYPE_12 = "10911012"; // 兵装人
    public final static String FLEET_TYPE_13 = "10911013"; // 残疾人

    public final static String FLEET_TYPE_14 = "10911014"; // 政府采购
    public final static String FLEET_TYPE_15 = "10911015"; // 企事业
    public final static String FLEET_TYPE_16 = "10911016"; // 出租
    public final static String FLEET_TYPE_17 = "10911017"; // 租赁类
    public final static String FLEET_TYPE_18 = "10911018"; // 员工类
    public final static String FLEET_TYPE_19 = "10911019"; // 团购
    public final static String FLEET_TYPE_20 = "10911020"; // 专用车

    //旧件抵扣 add by zhaolunda 2010-06-09
    public final static Integer OLDPART_DEDUCT_TYPE = 1050;
    public final static Integer OLDPART_DEDUCT_TYPE_01 = 10501001; //配件代码不符
//    public final static Integer OLDPART_DEDUCT_TYPE_02 = 10501002; //配件数量不符
    public final static Integer OLDPART_DEDUCT_TYPE_03 = 10501003; //未检测出故障
//    public final static Integer OLDPART_DEDUCT_TYPE_04 = 10501004; //包装不当损坏
//    public final static Integer OLDPART_DEDUCT_TYPE_05 = 10501005; //标识被损坏
    public final static Integer OLDPART_DEDUCT_TYPE_06 = 10501006; //无件
    public final static Integer OLDPART_DEDUCT_TYPE_07 = 10501007; //非原厂配件
    public final static Integer OLDPART_DEDUCT_TYPE_08 = 10501008; //供应商代码错误
//    public final static Integer OLDPART_DEDUCT_TYPE_09 = 10501009; //超三包件
    public final static Integer OLDPART_DEDUCT_TYPE_10 = 10501010; //非质量问题
    public final static Integer OLDPART_DEDUCT_TYPE_11 = 10501011; //非主故障部位
//    public final static Integer OLDPART_DEDUCT_TYPE_12 = 10501012; //关联件不全
    public final static Integer OLDPART_DEDUCT_TYPE_13 = 10501013; //破损
//    public final static Integer OLDPART_DEDUCT_TYPE_14 = 10501014; //应更换总成件
//    public final static Integer OLDPART_DEDUCT_TYPE_15 = 10501015; //应更换散件
//    public final static Integer OLDPART_DEDUCT_TYPE_16 = 10501016; //厂家置换件
    public final static Integer OLDPART_DEDUCT_TYPE_17 = 10501017; //无旧件标签
//    public final static Integer OLDPART_DEDUCT_TYPE_18 = 10501018; //无审批手续
//    public final static Integer OLDPART_DEDUCT_TYPE_19 = 10501019; //索赔单作废
    public final static Integer OLDPART_DEDUCT_TYPE_20 = 10501020; //无故障现象或原因分析
    public final static Integer OLDPART_DEDUCT_TYPE_21 = 10501021; //故障与描述不符
    public final static Integer OLDPART_DEDUCT_TYPE_22 = 10501022; //连带扣件
    public final static Integer OLDPART_DEDUCT_TYPE_23 = 10501023; //其他

    //证件类别 add by liubo 2010-06-09
    public final static Integer CARD_TYPE = 1093;
    public final static Integer CARD_TYPE_01 = 10931001;//身份证
    public final static Integer CARD_TYPE_02 = 10931002;//军官证
    public final static Integer CARD_TYPE_03 = 10931003;//警官证
    public final static Integer CARD_TYPE_04 = 10931004;//组织机构代码证
    public final static Integer CARD_TYPE_05 = 10931005;//营业执照
    public final static Integer CARD_TYPE_06 = 10931006;//其他
    public final static Integer CARD_TYPE_PASSPORT = 10931004; //护照

    //客户来源 add by liubo 2010-06-09
    public final static Integer CUSTOMER_FROM = 1094;
    public final static Integer CUSTOMER_FROM_01 = 10941001;//客户直销
    public final static Integer CUSTOMER_FROM_02 = 10941002;//朋友介绍
    public final static Integer CUSTOMER_FROM_03 = 10941003;//广告
    public final static Integer CUSTOMER_FROM_04 = 10941004;//电视广告
    public final static Integer CUSTOMER_FROM_05 = 10941005;//网络广告
    public final static Integer CUSTOMER_FROM_06 = 10941006;//报纸广告
    public final static Integer CUSTOMER_FROM_07 = 10941007;//展览展示
    public final static Integer CUSTOMER_FROM_08 = 10941008;//广播广告
    public final static Integer CUSTOMER_FROM_09 = 10941009;//户外广告
    public final static Integer CUSTOMER_FROM_10 = 10941010;//DM宣传
    public final static Integer CUSTOMER_FROM_11 = 10941011;//短信
    public final static Integer CUSTOMER_FROM_12 = 10941012;//其它
    public final static Integer CUSTOMER_FROM_13 = 10941013;//重复购买

    //家庭月收入add by liubo 2010-06-09
    public final static Integer EARNING_MONTH = 1095;
    public final static Integer EARNING_MONTH_01 = 10951001;//3000元以下
    public final static Integer EARNING_MONTH_02 = 10951002;//3000－5999元
    public final static Integer EARNING_MONTH_03 = 10951003;//6000－9999元
    public final static Integer EARNING_MONTH_04 = 10951004;//10000－14999元
    public final static Integer EARNING_MONTH_05 = 10951005;//15000－19999元
    public final static Integer EARNING_MONTH_06 = 10951006;//20000－24999元
    public final static Integer EARNING_MONTH_07 = 10951007;//25000－29999元
    public final static Integer EARNING_MONTH_08 = 10951008;//30000－50000元
    public final static Integer EARNING_MONTH_09 = 10951009;//50000元以上

    //教育程度add by liubo 2010-06-09
    public final static Integer EDUCATION_TYPE = 1096;
    public final static Integer EDUCATION_TYPE_01 = 10961001;//小学
    public final static Integer EDUCATION_TYPE_02 = 10961002;//初中
    public final static Integer EDUCATION_TYPE_03 = 10961003;//高中/中专/技校
    public final static Integer EDUCATION_TYPE_04 = 10961004;//大专
    public final static Integer EDUCATION_TYPE_05 = 10961005;//本科
    public final static Integer EDUCATION_TYPE_06 = 10961006;//硕士或以上

    //所在行业add by liubo 2010-06-09
    public final static Integer TRADE_TYPE = 1097;
    public final static Integer TRADE_TYPE_01 = 10971001;//制造业
    public final static Integer TRADE_TYPE_02 = 10971002;//房地产业
    public final static Integer TRADE_TYPE_03 = 10971003;//电力、煤气及水的生产和供应业
    public final static Integer TRADE_TYPE_04 = 10971004;//科研及综合技术服务业
    public final static Integer TRADE_TYPE_05 = 10971005;//金融保险业
    public final static Integer TRADE_TYPE_06 = 10971006;//教育、文化和广播电影电视业
    public final static Integer TRADE_TYPE_07 = 10971007;//交通运输仓储业
    public final static Integer TRADE_TYPE_08 = 10971008;//国家机关、政党机关和社会团体
    public final static Integer TRADE_TYPE_09 = 10971009;//计算机业( IT业)
    public final static Integer TRADE_TYPE_10 = 10971010;//社会服务业
    public final static Integer TRADE_TYPE_11 = 10971011;//批发零售贸易业
    public final static Integer TRADE_TYPE_12 = 10971012;//卫生体育和社会福利业
    public final static Integer TRADE_TYPE_13 = 10971013;//餐饮业
    public final static Integer TRADE_TYPE_14 = 10971014;//其它

    //婚姻状况add by liubo 2010-06-09
    public final static Integer MARRIAGE_TYPE = 1098;
    public final static Integer MARRIAGE_TYPE_01 = 10981001;//未婚
    public final static Integer MARRIAGE_TYPE_02 = 10981002;//已婚

    //职业
    public final static Integer PROFESSION_TYPE = 1099;
    public final static Integer PROFESSION_TYPE_01 = 10991001;//企业主或企业股东
    public final static Integer PROFESSION_TYPE_02 = 10991002;//高层管理人员（核心管理人员）
    public final static Integer PROFESSION_TYPE_03 = 10991003;//中层管理人员（部门管理人员）
    public final static Integer PROFESSION_TYPE_04 = 10991004;//一般职员/职工
    public final static Integer PROFESSION_TYPE_05 = 10991005;//其它

    // add by zouchao 2010-06-09
    public final static Integer FUND_SIZE_TYPE = 1100; // 资金规模
    public final static Integer FUND_SIZE_TYPE_01 = 11001001; // 30万以下
    public final static Integer FUND_SIZE_TYPE_02 = 11001002; // 30~50万
    public final static Integer FUND_SIZE_TYPE_03 = 11001003; // 50~100万
    public final static Integer FUND_SIZE_TYPE_04 = 11001004; // 100万以上

    public final static Integer DATA_TYPE = 1101; // 大用户数据类型
    public final static Integer DATA_TYPE_01 = 11011001; // 新记录
    public final static Integer DATA_TYPE_02 = 11011002; // 旧记录

    public final static Integer FLEET_INFO_TYPE = 1102; // 大用户报备状态
    public final static Integer FLEET_INFO_TYPE_01 = 11021001; // 未提交
    public final static Integer FLEET_INFO_TYPE_02 = 11021002; // 待确认
    public final static Integer FLEET_INFO_TYPE_03 = 11021003; // 已确认
    public final static Integer FLEET_INFO_TYPE_04 = 11021004; //驳回
    public final static Integer FLEET_INFO_TYPE_06 = 11021006; // 待车厂确认
	public final static Integer FLEET_INFO_TYPE_05 = 11021005; // 待车厂确认
	public final static Integer FLEET_INFO_TYPE_07 = 11021007; // 终止

    public final static Integer PURPOSE_TYPE = 1103; // 购车用途
    public final static Integer PURPOSE_TYPE_01 = 11031001; // 用途1
    public final static Integer PURPOSE_TYPE_02 = 11031002; // 用途2

    //索赔申请单-授权规则维护-授权项    add by XZM at 2010-06-10
    public final static Integer CLAIM_AUTH_TYPE = 1104; //授权项类别
    public final static Integer CLAIM_AUTH_TYPE_01 = 11041001; //索赔配件金额
    public final static Integer CLAIM_AUTH_TYPE_02 = 11041002; //维修工时总金额
    public final static Integer CLAIM_AUTH_TYPE_03 = 11041003; //主要件次数
    public final static Integer CLAIM_AUTH_TYPE_04 = 11041004; //非主要件次数
    public final static Integer CLAIM_AUTH_TYPE_05 = 11041005; //修理累计时间
    public final static Integer CLAIM_AUTH_TYPE_06 = 11041006; //维修总费用(工时费用+材料费用+外派费用)

    /**
     * public final static Integer CLAIM_AUTH_TYPE_01 = 11041001; //维修操作代码
     * public final static Integer CLAIM_AUTH_TYPE_02 = 11041002; //零件金额
     * public final static Integer CLAIM_AUTH_TYPE_03 = 11041003; //维修总金额
     * public final static Integer CLAIM_AUTH_TYPE_04 = 11041004; //其它项目费用
     * public final static Integer CLAIM_AUTH_TYPE_05 = 11041005; //修理完工天数
     * public final static Integer CLAIM_AUTH_TYPE_06 = 11041006; //索赔类型
     * public final static Integer CLAIM_AUTH_TYPE_07 = 11041007; //索赔申请次数
     * public final static Integer CLAIM_AUTH_TYPE_08 = 11041008; //修理完上报期限（天）
     * public final static Integer CLAIM_AUTH_TYPE_09 = 11041009; //是否特殊质保车辆
     * public final static Integer CLAIM_AUTH_TYPE_10 = 11041010; //经销商代码
     * public final static Integer CLAIM_AUTH_TYPE_11 = 11041011; //车型代码
     * public final static Integer CLAIM_AUTH_TYPE_12 = 11041012; //产地
     * public final static Integer CLAIM_AUTH_TYPE_13 = 11041013; //经销商星星级别
     * public final static Integer CLAIM_AUTH_TYPE_14 = 11041014; //车型大类
     * public final static Integer CLAIM_AUTH_TYPE_15 = 11041015; //配件代码
     */
    public final static Integer QUOTA_TYPE = 1106; // 配额类型
    public final static Integer QUOTA_TYPE_01 = 11061001; // 区域配额
    public final static Integer QUOTA_TYPE_02 = 11061002; // 经销商配额
    public final static Integer QUOTA_TYPE_03 = 11061003; // 车厂配额

    public final static Integer CHECK_STATUS = 1107; // 审核状态
    public final static Integer CHECK_STATUS_01 = 11071001; // 预审核通过
    public final static Integer CHECK_STATUS_02 = 11071002; // 预审核驳回
    public final static Integer CHECK_STATUS_03 = 11071003; // 资源审核通过
    public final static Integer CHECK_STATUS_04 = 11071004; // 资源审核驳回
    public final static Integer CHECK_STATUS_05 = 11071005; // 代交车审核通过
    public final static Integer CHECK_STATUS_QRWC = 11071006; // 代交车确认完成

    //索赔申请单-审核类型 add by XZM at 2010-06-11
    public final static char IS_AUTO_AUDITING = 0; //自动审核
    public final static char IS_MANUAL_AUDITING = 1; //人工审核

    //配件采购订单状态
    public final static String PART_ORDER_STATUS = "1074";
    public final static String PART_ORDER_STATUS_01 = "10741001"; //已保存
    public final static String PART_ORDER_STATUS_02 = "10741002"; //已完成
    public final static String PART_ORDER_STATUS_03 = "10741003"; //已上报
    public final static String PART_ORDER_STATUS_04 = "10741004"; //已批准
    public final static String PART_ORDER_STATUS_05 = "10741005"; //已驳回
    public final static String PART_ORDER_STATUS_06 = "10741006"; //已签收

    //索赔自动审核规则类型 add by XZM at 2010-06-11
    public final static Integer CLAIM_RULE_TYPE = 1108;
    public final static Integer CLAIM_RULE_TYPE_01 = 11081001;//拒绝规则
    public final static Integer CLAIM_RULE_TYPE_02 = 11081002;//退回规则
    public final static Integer CLAIM_RULE_TYPE_03 = 11081003;//人工规则
    public final static Integer CLAIM_RULE_TYPE_04 = 11081004;//自动通过规则

    //资源变更类型
    public final static Integer RESOURCE_CHNG_TYPE = 1109;
    public final static Integer RESOURCE_CHNG_TYPE_01 = 11091001;//计划变更
    public final static Integer RESOURCE_CHNG_TYPE_02 = 11091002;//订单变更

    //实销个人客户类型 add by liubo 2010-6-12
    public final static Integer IS_OLD_CTM = 1110;
    public final static Integer IS_OLD_CTM_01 = 11101001;//新客户
    public final static Integer IS_OLD_CTM_02 = 11101002;//老客户

    //订做车需求状态 add zhumingwei 2014-1-13
    public final static Integer SPECIAL_NEED_STATUS = 1113;
    public final static Integer SPECIAL_NEED_STATUS_01 = 11131001;//已保存
    public final static Integer SPECIAL_NEED_STATUS_02 = 11131002;//已提交
    public final static Integer SPECIAL_NEED_STATUS_03 = 11131003;//区域审核通过
    public final static Integer SPECIAL_NEED_STATUS_04 = 11131004;//销售部审核通过
    public final static Integer SPECIAL_NEED_STATUS_05 = 11131005;//区域审核驳回
    public final static Integer SPECIAL_NEED_STATUS_06 = 11131006;//销售部审核驳回
    public final static Integer SPECIAL_NEED_STATUS_07 = 11131007;//经销商定做车确认
    
    /**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 描述:银翔原型报错修复
     * 实际意义未详
     * Ded：11131009，11131014
     * Date:2017-06-29
     */
    public final static Integer SPECIAL_NEED_STATUS_09 = 11131009;
    public final static Integer SPECIAL_NEED_STATUS_14 = 11131014;
    /**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 描述:银翔原型报错修复
     * 实际意义未详
     * Ded：11131008
     * Date:2017-06-29
     */
    public final static Integer SPECIAL_NEED_STATUS_08 = 11131008;
    public final static Integer SPECIAL_NEED_STATUS_10 = 11131010;// 待上级审核

    //订做车需求审核类型 add zhumingwei 2014-1-17
    public final static Integer CHECK_TYPE_STATUS = 1242;
    public final static Integer CHECK_TYPE_STATUS_01 = 12421001;//区域审核
    public final static Integer CHECK_TYPE_STATUS_02 = 12421002;//销售部审核

    //订做车需求审核状态
    public final static Integer SPECIAL_NEED_CHECK_STATUS = 1114;
    public final static Integer SPECIAL_NEED_CHECK_STATUS_01 = 11141001;//产品审核通过
    public final static Integer SPECIAL_NEED_CHECK_STATUS_02 = 11141002;//产品审核驳回
    public final static Integer SPECIAL_NEED_CHECK_STATUS_03 = 11141003;//价格核定完成
    public final static Integer SPECIAL_NEED_CHECK_STATUS_04 = 11141004;//价格核定驳回
    public final static Integer SPECIAL_NEED_CHECK_STATUS_05 = 11141005;//大客户审核完成
    public final static Integer SPECIAL_NEED_CHECK_STATUS_06 = 11141006;//大客户审核驳回
    public final static Integer SPECIAL_NEED_CHECK_STATUS_07 = 11141007;//上级审核通过
    public final static Integer SPECIAL_NEED_CHECK_STATUS_08 = 11141008;//上级审核驳回

    //订单更改类型
    public final static Integer ORDER_CHNG_TYPE = 1115;
    public final static Integer ORDER_CHNG_TYPE_01 = 11151001;//订单取消

    //授权项算术比较符 add by wjb at 2010-06-17
    public final static Integer COMP_TYPE = 1111;
    public final static Integer COMP_TYPE_01 = 11111001;//=
    public final static Integer COMP_TYPE_02 = 11111002;//<
    public final static Integer COMP_TYPE_03 = 11111003;//<=
    public final static Integer COMP_TYPE_04 = 11111004;//>=
    public final static Integer COMP_TYPE_05 = 11111005;//>
    public final static Integer COMP_TYPE_06 = 11111006;//<>
    //授权项逻辑比较符 add by wjb at 2010-06-17
    public final static Integer LOGIC_TYPE = 1112;
    public final static Integer LOGIC_TYPE_01 = 11121001;//Begin
    public final static Integer LOGIC_TYPE_02 = 11121002;//Equal
    public final static Integer LOGIC_TYPE_03 = 11121003;//notBegin
    public final static Integer LOGIC_TYPE_04 = 11121004;//notEqual
    //索赔预授权审批意见  add by PGM at 2010-06-22
    public final static Integer PRECLAIM_AUDIT = 1116;
    public final static Integer PRECLAIM_AUDIT_01 = 11161001;//同意
    public final static Integer PRECLAIM_AUDIT_02 = 11161002;//拒绝
    public final static Integer PRECLAIM_AUDIT_03 = 11161003;//已接收

    //集团客户支持状态
    public final static Integer FLEET_SUPPORT_STATUS = 1118;
    public final static Integer FLEET_SUPPORT_STATUS_01 = 11181001;//未申请
    public final static Integer FLEET_SUPPORT_STATUS_02 = 11181002;//已申请
    public final static Integer FLEET_SUPPORT_STATUS_03 = 11181003;//财务待审核
    public final static Integer FLEET_SUPPORT_STATUS_04 = 11181004;//审核通过
    public final static Integer FLEET_SUPPORT_STATUS_05 = 11181005;//驳回
    public final static Integer FLEET_SUPPORT_STATUS_06 = 11181006;//合同已维护
    public final static Integer FLEET_SUPPORT_STATUS_07 = 11181007;//实销已审核
    public final static Integer FLEET_SUPPORT_STATUS_08 = 11181008;//大区审核通过

    //集团客户支持审核状态
    public final static Integer FLEET_SUPPORT_CHECK_STATUS = 1119;
    public final static Integer FLEET_SUPPORT_CHECK_STATUS_01 = 11191001;//审核通过
    public final static Integer FLEET_SUPPORT_CHECK_STATUS_02 = 11191002;//审核驳回
    public final static Integer FLEET_SUPPORT_CHECK_STATUS_03 = 11191003;//大区审核通过

    //服务活动评估状态  add by PGM at 2010-06-23
    public final static Integer SUMMARY_STATUS = 1120;
    public final static Integer SUMMARY_STATUS_01 = 11201001;//已评估
    public final static Integer SUMMARY_STATUS_02 = 11201002;//未评估

    //经销商配额计算类型
    public final static Integer QUOTA_CALCULATE_TYPE = 1117;
    public final static Integer QUOTA_CALCULATE_TYPE_01 = 11171001;//加权平均法
    public final static Integer QUOTA_CALCULATE_TYPE_02 = 11171002;//库存深度法

    //货运单签收状态  add by lishuai at 2010-06-25
    public final static String IS_SIGNED_00 = "0";//未签收
    public final static String IS_SIGNED_01 = "1";//已签收

    //配件索赔审批状态  add by liuqiang at 2010-06-25
    public final static Integer PART_CLAIM_STATUS = 1121;
    public final static Integer PART_CLAIM_STATUS_01 = 11211001;//全部
    public final static Integer PART_CLAIM_STATUS_02 = 11211002;//未审核
    public final static Integer PART_CLAIM_STATUS_03 = 11211003;//已通过
    public final static Integer PART_CLAIM_STATUS_04 = 11211004;//未驳回

    //索赔抵扣状态标识 add by XZM at 2010-06-25
    public final static Integer DEDUCT_STATUS = 1; //抵扣

    //集团客户实销上报审核状态 add by liubo at 2010-06-28
    public final static Integer Fleet_SALES_CHECK_STATUS = 1122;
    public final static Integer Fleet_SALES_CHECK_STATUS_01 = 11221001;//未审核
    public final static Integer Fleet_SALES_CHECK_STATUS_02 = 11221002;//审核通过
    public final static Integer Fleet_SALES_CHECK_STATUS_03 = 11221003;//驳回

    //实销审核状态
    public final static Integer SALES_CHECK_STATUS = 1123;
    public final static Integer SALES_CHECK_STATUS_01 = 11231001;//审核通过
    public final static Integer SALES_CHECK_STATUS_02 = 11231002;//审核驳回

    //配额下发状态 add by YUYONG at 2010-06-28
    public static final Integer QUOTA_STATUS = 1031;
    public static final Integer QUOTA_STATUS_01 = 10311001;//未下发
    public static final Integer QUOTA_STATUS_02 = 10311002;//已下发

    //实销信息更改申请审核状态 add by liubo at 2010-06-29
    public static final Integer SALES_INFO_CHANGE_STATUS = 1124;
//    public static final Integer SALES_INFO_CHANGE_STATUS_01 = 11241001; //销售部审核通过
//    public static final Integer SALES_INFO_CHANGE_STATUS_02 = 11241002; //呼叫中心审核通过
//    public static final Integer SALES_INFO_CHANGE_STATUS_03 = 11241003; //驳回
//    public static final Integer SALES_INFO_CHANGE_STATUS_04 = 11241004; //未审核
//    public static final Integer SALES_INFO_CHANGE_STATUS_05 = 11241005; //区域经理审核通过
//    public static final Integer SALES_INFO_CHANGE_STATUS_06 = 11241006; //大区经理审核通过
//    public static final Integer SALES_INFO_CHANGE_STATUS_07 = 11241007; //售后审核通过
    public static final Integer SALES_INFO_CHANGE_STATUS_01 = 11241001; //未审核
    public static final Integer SALES_INFO_CHANGE_STATUS_02 = 11241002; //区域审核通过
    public static final Integer SALES_INFO_CHANGE_STATUS_03 = 11241003; //车厂审核通过
    public static final Integer SALES_INFO_CHANGE_STATUS_04 = 11241004; //驳回

    
    //市场活动类别
    public static final Integer CAMPAIGN_TYPE = 1125;
    public static final Integer CAMPAIGN_TYPE_01 = 11251001; //车厂下发
    public static final Integer CAMPAIGN_TYPE_02 = 11251002; //经销商自发
    public static final Integer CAMPAIGN_TYPE_03 = 11251003; //区域自发

    //活动方案状态
    public static final Integer CAMPAIGN_CHECK_STATUS = 1126;
    public static final Integer CAMPAIGN_CHECK_STATUS_01 = 11261001; //车厂方案已保存
    public static final Integer CAMPAIGN_CHECK_STATUS_02 = 11261002; //车厂已下发
    public static final Integer CAMPAIGN_CHECK_STATUS_03 = 11261003; //方案待大区审核
    public static final Integer CAMPAIGN_CHECK_STATUS_04 = 11261004; //方案待市场部审核
    public static final Integer CAMPAIGN_CHECK_STATUS_05 = 11261005; //方案审核驳回
    public static final Integer CAMPAIGN_CHECK_STATUS_06 = 11261006; //方案审核完成
    public static final Integer CAMPAIGN_CHECK_STATUS_07 = 11261007; //总结待大区审核
    public static final Integer CAMPAIGN_CHECK_STATUS_08 = 11261008; //总结待市场部审核
    public static final Integer CAMPAIGN_CHECK_STATUS_09 = 11261009; //总结审核驳回
    public static final Integer CAMPAIGN_CHECK_STATUS_10 = 11261010; //已结案
    public static final Integer CAMPAIGN_CHECK_STATUS_11 = 11261011; //已取消

    public static final Integer CAMPAIGN_CHECK_STATUS_12 = 11261012; //车厂方案已下发
    public static final Integer CAMPAIGN_CHECK_STATUS_13 = 11261013; //方案待区域经理审核
    public static final Integer CAMPAIGN_CHECK_STATUS_14 = 11261014; //方案待大区经理审核
    public static final Integer CAMPAIGN_CHECK_STATUS_15 = 11261015; //方案待业务总监审核
    public static final Integer CAMPAIGN_CHECK_STATUS_16 = 11261016; //方案待活动管理部审核
    public static final Integer CAMPAIGN_CHECK_STATUS_17 = 11261017; //方案待市场管理中心总监审核
    public static final Integer CAMPAIGN_CHECK_STATUS_18 = 11261018; //方案待销售中心总经理审核

    public static final Integer CAMPAIGN_CHECK_STATUS_19 = 11261019; //方案待事业部领导审核
    public static final Integer CAMPAIGN_CHECK_STATUS_20 = 11261020; //方案待大区大客户经理审核
    public static final Integer CAMPAIGN_CHECK_STATUS_21 = 11261021; //方案待大客户经理审核
    public static final Integer CAMPAIGN_CHECK_STATUS_22 = 11261022; //方案待大区分管副总审核审核
    public static final Integer CAMPAIGN_CHECK_STATUS_23 = 11261023; //方案待市场分管副总审核审核
    public static final Integer CAMPAIGN_CHECK_STATUS_24 = 11261024; // 上级(小区)已保存(未下发)

    //方案审核状态
    public static final Integer MARKET_CHECK_STATUS = 1127;
    public static final Integer MARKET_CHECK_STATUS_01 = 11271001; //审核通过
    public static final Integer MARKET_CHECK_STATUS_02 = 11271002; //审核驳回
    public static final Integer MARKET_CHECK_STATUS_03 = 11271003; //签字附件上传完成

    //市场活动附件类型
    public static final Integer CAMPAIGN_ATTACH_TYPE = 1128;
    public static final Integer CAMPAIGN_ATTACH_TYPE_01 = 11281001; //活动方案附件
    public static final Integer CAMPAIGN_ATTACH_TYPE_02 = 11281002; //执行方案附件
    public static final Integer CAMPAIGN_ATTACH_TYPE_03 = 11281003; //方案审核附件
    public static final Integer CAMPAIGN_ATTACH_TYPE_04 = 11281004; //活动总结附件

    //费用类型
    public static final Integer COST_TYPE = 1129;
    public static final Integer COST_TYPE_01 = 11291001; //服务中心市场推广费用
    public static final Integer COST_TYPE_02 = 11291002; //运营大区市场推广费用
    public static final Integer COST_TYPE_03 = 11291003; //轿车事业部费用
    public static final Integer COST_TYPE_04 = 11291004; //长安汽车广告费
    public static final Integer COST_TYPE_05 = 11291005; //大客户市场推广折让
    public static final Integer COST_TYPE_06 = 11291006; //轿车事业部推广基金
    public static final Integer COST_TYPE_07 = 11291007; //服务中心单店市场推广基金
    public static final Integer COST_TYPE_08 = 11291008;	//区域市场推广基金 ADD BY TANV 2014-02-14
    public static final Integer COST_TYPE_09 = 11291009;	//市场推广费 ADD BY TANV 2014-02-14
    public static final Integer COST_TYPE_10 = 11291010;	//市场推广费(华东、西北大区) ADD BY TANV 2014-02-14
    public static final Integer WORK_FLOW = 1530;
    public static final Integer WORK_FLOW_01 = 15301001;	//默认流程
    public static final Integer WORK_FLOW_02 = 15301002;	//区域市场推广基金
    public static final Integer WORK_FLOW_03 = 15301003;	//市场推广费(华东、西北大区)

    //费用来源
    public static final Integer COST_SOURCE = 1130;
    public static final Integer COST_SOURCE_01 = 11301001; //经销商费用
    public static final Integer COST_SOURCE_02 = 11301002; //区域费用
    public static final Integer COST_SOURCE_03 = 11301003; //总部费用

    //活动类型
    public static final Integer COSTACTIVITY_TYPE = 1160;
    public static final Integer COSTACTIVITY_TYPE_XZ = 11601001; //巡展
    public static final Integer COSTACTIVITY_TYPE_SYQJTZS = 11601002; //商业区静态展示
    public static final Integer COSTACTIVITY_TYPE_PJH = 11601003; //品鉴会
    public static final Integer COSTACTIVITY_TYPE_WZ = 11601004; //外展
    public static final Integer COSTACTIVITY_TYPE_QYCZ = 11601005; //区域车展
    public static final Integer COSTACTIVITY_TYPE_SCJS = 11601006; //试乘试驾
    public static final Integer COSTACTIVITY_TYPE_KHGHHD = 11601007; //客户关怀活动
    public static final Integer COSTACTIVITY_TYPE_OTHER = 11601008; //其他

    //仓库类别   add by liubo
    public static final Integer WAREHOUSE_TYPE = 1035;
    public static final Integer WAREHOUSE_TYPE_01 = 10351001;//仓库类型1
    public static final Integer WAREHOUSE_TYPE_02 = 10351002;//仓库类型2
    public static final Integer WAREHOUSE_TYPE_03 = 10351003;//仓库类型3
    public static final Integer WAREHOUSE_TYPE_04 = 10351004;//特殊商品库

    // add by zouchao 2010-07-09
    public static final Integer PARA_TYPE = 1133; //可变代码维护类型
    public static final Integer PARA_TYPE_01 = 11331001;//经销商类型
    public static final Integer PARA_TYPE_02 = 11331002;//经销商评级

    //巡航服务线路状态  add by zhaolunda 2010-07-13
    public final static Integer CURI_SERVICE_STATUS = 1134;
    public final static Integer CURI_SERVICE_STATUS_01 = 11341001; //未上报
    public final static Integer CURI_SERVICE_STATUS_02 = 11341002; //已上报
    public final static Integer CURI_SERVICE_STATUS_03 = 11341003; //审核退回
    public final static Integer CURI_SERVICE_STATUS_04 = 11341004; //已审核
    public final static Integer CURI_SERVICE_STATUS_05 = 11341005; //中止
    public final static Integer CURI_SERVICE_STATUS_06 = 11341006; //已删除
    public final static Integer CURI_SERVICE_STATUS_07 = 11341007; //已批复
    public final static Integer CURI_SERVICE_STATUS_08 = 11341008; //批复退回

    //特殊外出费用状态  add by zhaolunda 2010-07-13
    public final static Integer SPE_OUTFEE_STATUS = 1135;
    public final static Integer SPE_OUTFEE_STATUS_01 = 11351001; //未上报
    public final static Integer SPE_OUTFEE_STATUS_02 = 11351002; //已上报
    public final static Integer SPE_OUTFEE_STATUS_03 = 11351003; //审核退回
    public final static Integer SPE_OUTFEE_STATUS_04 = 11351004; //已审核
    public final static Integer SPE_OUTFEE_STATUS_05 = 11351005; //已结算
    public final static Integer SPE_OUTFEE_STATUS_06 = 11351006; //结算驳回

    //索赔工时调整设定调整类
    public final static Integer ADJUST_MODE_TYPE = 1351;
    public final static Integer ADJUST_MODE_TYPE_01 = 13511001; //服务站级别
    public final static Integer ADJUST_MODE_TYPE_02 = 13511002; //服务站类型

    //三包规则类型  add by PGM 2010-07-14
    public final static Integer RULE_TYPE = 1137;
    public final static Integer RULE_TYPE_01 = 11371001; //系统规则
    public final static Integer RULE_TYPE_02 = 11371002; //业务规则

    //预授权申请类型  add by wangjinbao 2010-07-14
    public final static Integer APPLY_TYPE = 1136;
    public final static Integer APPLY_TYPE_01 = 11361001; //维修
    public final static Integer APPLY_TYPE_02 = 11361002; //保养
    public final static Integer APPLY_TYPE_03 = 11361003; //外出服务
    public final static Integer APPLY_TYPE_04 = 11361004; //活动

    //add by yuyong 2010-07-14
    public final static Integer MATERIAL_PRICE_MAX = 5000000; //物料能提报订单的价格最大值

    //特殊费用渠道  add by zhaolunda 2010-07-14
    public final static Integer SPE_OUTFEE_CHANNEL = 1138;
    public final static Integer SPE_OUTFEE_CHANNEL_01 = 11381001; //巡航服务

    //地区类型
    public final static Integer REGION_TYPE = 1054;
    public final static Integer REGION_TYPE_01 = 10541001; //国家
    public final static Integer REGION_TYPE_02 = 10541002; //省市
    public final static Integer REGION_TYPE_03 = 10541003; //地区
    public final static Integer REGION_TYPE_04 = 10541004; //区县

    //配件是否需要回运标识   add by XZM 2010-07-21
    public final static Integer IS_NEED_RETURN = 1; //需要回运
    public final static Integer IS_UNNEED_RETURN = 0; //不需要回运

    //产地标识  add by XZM 2010-07-28
    public final static Integer YIELDLY_TYPE = 1131; //产地对应类别

    // add by zouchao 2010-08-02
    public final static Integer QUALITY_COMP_TYPE = 1140; //质量投诉类型
    public final static Integer QUALITY_COMP_TYPE_01 = 11401001; //质量投诉1
    public final static Integer QUALITY_COMP_TYPE_02 = 11401002; //质量投诉2

    public final static Integer PART_COMP_TYPE = 1141; //配件投诉类型
    public final static Integer PART_COMP_TYPE_01 = 11411001; //配件投诉1
    public final static Integer PART_COMP_TYPE_02 = 11411002; //配件投诉2

    //目标类型 add by liubo 2010-08-16
	/* public final static Integer TARGET_TYPE = 1142; public final static
	 * Integer TARGET_TYPE_01 = 11421001; //批售计划 public final static Integer
	 * TARGET_TYPE_02 = 11421002; //零售计划 */

    //经销商仓库类型   add by yuyong
    public static final Integer DEALER_WAREHOUSE_TYPE = 1142;
    public static final Integer DEALER_WAREHOUSE_TYPE_01 = 11421001;//自有库
    public static final Integer DEALER_WAREHOUSE_TYPE_02 = 11421002;//代销库

    //特殊工时 add by wangchao 2010-8-19
    public final static Integer SPEC_LABOUR_CODE = 9999;// 特殊工时以9999开头

    //Add by liuqiang 接口用户的标志
    Integer INTER_USER_TYPE = 10001001;

    public final static String areaManager = "northManger";

    //结算单审核状态 add by lishuai103@yahoo.cn
    public final static String ACC_STATUS = "1143";
    public final static String ACC_STATUS_01 = "11431001";//结算中
    public final static String ACC_STATUS_02 = "11431002";//结算完成
    public final static String ACC_STATUS_03 = "11431003";//财务复核中
    public final static String ACC_STATUS_04 = "11431004";//财务审核完成
    public final static String ACC_STATUS_05 = "11431005";//经销商已确认
    public final static String ACC_STATUS_06 = "11431006";//结算单待收单
    public final static String ACC_STATUS_07 = "11431007";//结算单未上报
    public final static String ACC_STATUS_08 = "11431008";//结算单已上报
    public final static String ACC_STATUS_09 = "11431009";//结算单已完成

    //维修类型 add by wangchao 2010-08-28
    public final static Integer REPAIR_TYPE = 1144;
    public final static String REPAIR_TYPE_01 = "11441001"; //一般维修
    public final static String REPAIR_TYPE_02 = "11441002"; //外出维修
    public final static String REPAIR_TYPE_03 = "11441003"; //售前维修
    public final static String REPAIR_TYPE_04 = "11441004"; //保养
    public final static String REPAIR_TYPE_05 = "11441005"; //服务活动
    public final static String REPAIR_TYPE_06 = "11441006"; //特殊服务
    public final static String REPAIR_TYPE_07 = "11441007"; //急件工单
    public final static String REPAIR_TYPE_08 = "11441008"; //PDI
    public final static String REPAIR_TYPE_09 = "11441009"; //配件索赔

    //授权和结算室状态区分 add by XZM 2010-08-28
    public final static Integer AUDIT_TYPE_01 = 0; //技术授权
    public final static Integer AUDIT_TYPE_02 = 1; //结算授权
    //经销商评级 add by yangzhen 2010-08-28
    public final static Integer DEALER_CLASS_TYPE = 1145;
    public final static Integer DEALER_CLASS_TYPE_01 = 11451001; //服务中心
    public final static Integer DEALER_CLASS_TYPE_02 = 11451002; //授权专营店
    public final static Integer DEALER_CLASS_TYPE_03 = 11451003; //特约维修站
    public final static Integer DEALER_CLASS_TYPE_04 = 11451004; //汽车超市
    public final static Integer DEALER_CLASS_TYPE_05 = 11451005; //二级销售网点
    public final static Integer DEALER_CLASS_TYPE_06 = 11451006; //结算中心
    public final static Integer DEALER_CLASS_TYPE_07 = 11451007; //专用车公司
    public final static Integer DEALER_CLASS_TYPE_08 = 11451008; //长安国际公司
    public final static Integer DEALER_CLASS_TYPE_09 = 11451009; //二级经销商
    public final static Integer DEALER_CLASS_TYPE_10 = 11451010; //三级经销商
    public final static Integer DEALER_CLASS_TYPE_11 = 11451011; //一级经销商
    public final static Integer DEALER_CLASS_TYPE_12 = 11451012; //旗舰店
    public final static Integer DEALER_CLASS_TYPE_13 = 11451013; //退网
    /*---------------------质量信息卡跟踪 start------------------------*/
    //投诉类型
    public final static Integer COMPLAIN_SORT = 1146;
    public final static Integer COMPLAIN_SORT_01 = 11461001; //服务(F)
    public final static Integer COMPLAIN_SORT_02 = 11461002; //产品质量(Z)
    public final static Integer COMPLAIN_SORT_03 = 11461003; //备件(P)

    //信息类别
    public final static Integer INFOMATION_SORT = 1147;
    public final static Integer INFOMATION_SORT_01 = 11471001; //一般
    public final static Integer INFOMATION_SORT_02 = 11471002; //重大
    public final static Integer INFOMATION_SORT_03 = 11471003; //告急

    //问题部位
    public final static Integer QUESTION_PART = 1148;
    public final static Integer QUESTION_PART_01 = 11481001; //底盘部分
    public final static Integer QUESTION_PART_02 = 11481002; //电气部分
    public final static Integer QUESTION_PART_03 = 11481003; //车身部分
    public final static Integer QUESTION_PART_04 = 11481004; //发动机部分
    public final static Integer QUESTION_PART_05 = 11481005; //空调部分

    //故障性质
    public final static Integer FAULT_NATURE = 1149;
    public final static Integer FAULT_NATURE_01 = 11491001; //零部件质量
    public final static Integer FAULT_NATURE_02 = 11491002; //外观质量
    public final static Integer FAULT_NATURE_03 = 11491003; //装配调整质量
    public final static Integer FAULT_NATURE_04 = 11491004; //其他问题

    //变速器类别
    public final static Integer GEARBOX_NATURE = 1150;
    public final static Integer GEARBOX_NATURE_01 = 11501001; //自动挡(AT)
    public final static Integer GEARBOX_NATURE_02 = 11501002; //手动挡(MT)
	/*---------------------质量信息卡跟踪 end------------------------*/

    //经销商罚款支付状态
    public final static String PAY_STATUS = "1151";
    public final static String PAY_STATUS_01 = "11511001"; //未支付
    public final static String PAY_STATUS_02 = "11511002"; //已支付

    //行政抵扣状态
    public final static String ADMIN_STATUS = "1152";
    public final static String ADMIN_STATUS_01 = "11521001";//未扣款
    public final static String ADMIN_STATUS_02 = "11521002";//已扣款

    //劳务清单状态
    public final static Integer LABOR_LIST_STATUS = 1155;
    public final static Integer LABOR_LIST_STATUS_SAVE = 11551001;//保存
    public final static Integer LABOR_LIST_STATUS_REPORT = 11551002;//上报
    public final static Integer LABOR_LIST_STATUS_SIGN = 11551003;//签收
    public final static Integer LABOR_LIST_STATUS_STOP = 11551004;
    public final static Integer LABOR_LIST_STATUS_STOP1 = 11551005;
    //集团客户报备审核状态
    public final static Integer Fleet_Audit = 1154;
    public final static Integer FLEET_AUDIT_01 = 11541001;//审核通过
    public final static Integer FLEET_AUDIT_02 = 11541002;//审核驳回
    //客户星级
    public final static Integer GUEST_STARS = 1153;
    public final static Integer GUEST_STARS_01 = 11531001; //A
    public final static Integer GUEST_STARS_02 = 11531002; //B
    public final static Integer GUEST_STARS_03 = 11531003; //C
    public final static Integer GUEST_STARS_04 = 11531004; //D

    //发运申请状态 add by yuyong
    public final static Integer ORDER_REQ_STATUS = 1157;
    public final static Integer ORDER_REQ_STATUS_01 = 11571001; //已提报
    public final static Integer ORDER_REQ_STATUS_02 = 11571002; //经销商待确认
    public final static Integer ORDER_REQ_STATUS_03 = 11571003; //初审完成
    public final static Integer ORDER_REQ_STATUS_04 = 11571004; //审核完成
    public final static Integer ORDER_REQ_STATUS_05 = 11571005; //驳回初审
    public final static Integer ORDER_REQ_STATUS_06 = 11571006; //发运完成
    public final static Integer ORDER_REQ_STATUS_07 = 11571007; //已取消
    public final static Integer ORDER_REQ_STATUS_08 = 11571008; //代交车审核
    public final static Integer ORDER_REQ_STATUS_09 = 11571009; //财务已驳回
    public final static Integer ORDER_REQ_STATUS_YSH = 11571010; //预审核
    public final static Integer ORDER_REQ_STATUS_DJCQR = 11571011; //代交车确认
    public final static Integer ORDER_REQ_STATUS_DCWSH = 11571012; //代交车确认
    public final static Integer ORDER_REQ_STATUS_BH = 11571013; //代交车确认
    public final static Integer ORDER_REQ_STATUS_FINAL = 11571014; // 终审完成
    public final static Integer ORDER_REQ_STATUS_DEL = 11571015; // 已删除
    public final static Integer ORDER_REQ_STATUS_CYQR = 11571016; // 储运已确认
    public final static Integer ORDER_REQ_STATUS_CYBH = 11571017; // 储运驳回

    //资源保留状态 add by yuyong
    public final static Integer RESOURCE_RESERVE_STATUS = 1158;
    public final static Integer RESOURCE_RESERVE_STATUS_01 = 11581001; //保留
    public final static Integer RESOURCE_RESERVE_STATUS_02 = 11581002; //临时保存
    /**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 描述:银翔原型报错修复
     * 订单资源保留，具体意义未知
     * Ded：11581003
     * Date:2017-06-29
     */
    public final static Integer RESOURCE_RESERVE_STATUS_03 = 11581003 ;

    //工单对应索赔单是否上报 add by XZM 2010.09.03
    public final static String IS_REPORT = "1"; //已上报
    public final static String IS_NOT_REPORT = "0"; //未上报

    //工单状态：预授权
    public final static Integer RO_FORE = 1156;
    public final static Integer RO_FORE_01 = 11561001; //预授权审核中
    public final static Integer RO_FORE_02 = 11561002; //预授权审核通过
    public final static Integer RO_FORE_03 = 11561003; //预授权审核退回
    public final static Integer RO_FORE_04 = 11561004; //预授权审核待上报
    public final static Integer RO_FORE_05 = 11561005; //预授权审核废弃
    public final static Integer RO_FORE_06 = 11561006; //预授权审核拒绝

    //工单状态：结算
    public final static Integer RO_STATUS = 1159;
    public final static Integer RO_STATUS_01 = 11591001; //未结算
    public final static Integer RO_STATUS_02 = 11591002; //已结算
    public final static Integer RO_STATUS_03 = 11591003; //废弃
    public final static Integer RO_STATUS_04 = 11591004; //取消结算待审核
    //计划管理导入模板参数
    public final static Integer PLAN_TEMPLATE = 2014;
    public final static Integer PLAN_TEMPLATE_01 = 20141004; //月度生成计划导入

    //退换车状态
    public final static String RETURN_STATUS = "1161";
    public final static String RETURN_STATUS_01 = "11611001";//待审核
    public final static String RETURN_STATUS_02 = "11611002";//销售审核通过
    public final static String RETURN_STATUS_03 = "11611003";//大区审核驳回
    public final static String RETURN_STATUS_04 = "11611004";//大区审核通过
    public final static String RETURN_STATUS_05 = "11611005"; //售后审核通过
    public final static String RETURN_STATUS_06 = "11611006"; //销售审核驳回
    public final static String RETURN_STATUS_07 = "11611007"; //总经理审核通过
    public final static String RETURN_STATUS_08 = "11611008"; //售后审核驳回
    public final static String RETURN_STATUS_09 = "11611009"; //总经理审核驳回
    
    //退车状态  一套新的code值
    public final static String RETURN_CAR_STATUS="1402"; // 经销商退车（新装态）
    public final static String RETURN_CAR_STATUS_01="14021001"; // 经销商提报退车
    public final static String RETURN_CAR_STATUS_02="14021002"; // 区域已审核
    public final static String RETURN_CAR_STATUS_03="14021003"; // 销售部已审核
    public final static String RETURN_CAR_STATUS_04="14021004"; // 储运部已审核
    public final static String RETURN_CAR_STATUS_05="14021005"; // 审核结束
    public final static String RETURN_CAR_STATUS_07="14021007"; // 审核驳回
    
    //轿车服务问题
    public final static String SEDAN_SERVES = "1162";
    public final static String SEDAN_SERVES_01 = "11621001"; //服务态度
    public final static String SEDAN_SERVES_02 = "11621002"; //服务质量
    public final static String SEDAN_SERVES_03 = "11621003"; //服务政策
    public final static String SEDAN_SERVES_04 = "11621004"; //服务费用
    public final static String SEDAN_SERVES_05 = "11621005"; //无效投诉
    public final static String SEDAN_SERVES_06 = "11621006"; //服务其他

    //轿车配件问题
    public final static String SEDAN_ACCESSORIES = "1163";
    public final static String SEDAN_ACCESSORIES_01 = "11631001"; //服务商引起不能供保
    public final static String SEDAN_ACCESSORIES_02 = "11631002"; //配件中心一起的配件不能供保
    public final static String SEDAN_ACCESSORIES_03 = "11631003"; //总部引起的配件不能供保
    public final static String SEDAN_ACCESSORIES_04 = "11631004"; //配件供保
    public final static String SEDAN_ACCESSORIES_05 = "11631005"; //配件质量
    public final static String SEDAN_ACCESSORIES_06 = "11631006"; //配件三包
    public final static String SEDAN_ACCESSORIES_07 = "11631007"; //无效投诉
    public final static String SEDAN_ACCESSORIES_08 = "11631008"; //配件其他

    //销售咨询
    public final static String SALE_CONSULTS = "1164";
    public final static String SALE_CONSULTS_01 = "11641001"; //奔奔咨询
    public final static String SALE_CONSULTS_02 = "11641002"; //杰勋咨询
    public final static String SALE_CONSULTS_03 = "11641003"; //志翔咨询
    public final static String SALE_CONSULTS_04 = "11641004"; //悦翔咨询
    public final static String SALE_CONSULTS_05 = "11641005"; //经销商咨询
    public final static String SALE_CONSULTS_06 = "11641006"; //其他

    //销售投诉
    public final static String SALE_COMPLAINS = "1165";
    public final static String SALE_COMPLAINS_01 = "11651001"; //价格投诉
    public final static String SALE_COMPLAINS_02 = "11651002"; //服务态度
    public final static String SALE_COMPLAINS_03 = "11651003"; //政策执行投诉
    public final static String SALE_COMPLAINS_04 = "11651004"; //更改配置
    public final static String SALE_COMPLAINS_05 = "11651005"; //无效投诉
    public final static String SALE_COMPLAINS_06 = "11651006"; //上牌(上户)投诉
    public final static String SALE_COMPLAINS_07 = "11651007"; //其他

    //轿车外出急救
    public final static String SEDAN_HELPS = "1166";
    public final static String SEDAN_HELPS_01 = "11661001"; //轿车急救(自用户)
    public final static String SEDAN_HELPS_02 = "11661002"; //轿车急救(自服务商)

    //轿车服务商经销商来电
    public final static String SEDAN_SERVES_DEALER = "1167";
    public final static String SEDAN_SERVES_DEALER_01 = "11671001"; //咨询
    public final static String SEDAN_SERVES_DEALER_02 = "11671002"; //投诉

    //轿车活动咨询
    public final static String SEDAN_ACTIVITY = "1168";
    public final static String SEDAN_ACTIVITY_01 = "11681001"; //服务活动咨询
    public final static String SEDAN_ACTIVITY_02 = "11681002"; //销售活动咨询

    //轿车其他问题
    public final static String SEDAN_OTHER_PROBLEMS = "1169";
    public final static String SEDAN_OTHER_PROBLEMS_01 = "11691001"; //保养问题
    public final static String SEDAN_OTHER_PROBLEMS_02 = "11691002"; //信息查询
    public final static String SEDAN_OTHER_PROBLEMS_03 = "11691003"; //意见或建议
    public final static String SEDAN_OTHER_PROBLEMS_04 = "11691004"; //技术咨询
    public final static String SEDAN_OTHER_PROBLEMS_05 = "11691005"; //其他

    //轿车质量问题
    public final static String SEDAN_QUALITY = "1170";
    public final static String SEDAN_QUALITY_01 = "11701001"; //发动机
    public final static String SEDAN_QUALITY_02 = "11701002"; //空调
    public final static String SEDAN_QUALITY_03 = "11701003"; //底盘
    public final static String SEDAN_QUALITY_04 = "11701004"; //车身
    public final static String SEDAN_QUALITY_05 = "11701005"; //变速器
    public final static String SEDAN_QUALITY_06 = "11701006"; //电器
    public final static String SEDAN_QUALITY_07 = "11701007"; //油耗
    public final static String SEDAN_QUALITY_08 = "11701008"; //制动系统
    public final static String SEDAN_QUALITY_09 = "11701009"; //三包外问题
    public final static String SEDAN_QUALITY_10 = "11701010"; //质量问题

    //轿车事故投诉和媒体来电投诉
    public final static String SEDAN_MEDIUM_COMPLAINS = "1171";
    public final static String SEDAN_MEDIUM_COMPLAINS_01 = "11711001"; //有效投诉
    public final static String SEDAN_MEDIUM_COMPLAINS_02 = "11711002"; //无效投诉
    public final static String SEDAN_MEDIUM_COMPLAINS_03 = "11711003"; //三包外问题

    //打印状态
    public final static String PRINT_FLAG_00 = "0";//未打印
    public final static String PRINT_FLAG_01 = "1";//已打印
    public final static String PRINT_FLAG_00_VALUE = "未打印";
    public final static String PRINT_FLAG_01_VALUE = "已打印";

    //移库单状态
    public final static String STO_STATUS = "1172";
    public final static String STO_STATUS_01 = "11721001";//处理中
    public final static String STO_STATUS_02 = "11721002";//处理完成
    public final static String STO_STATUS_03 = "11721003";//处理失败
    public final static String STO_STATUS_04 = "11721004";//已取消

    //三包策略状态  add by XZM 2010-09-10
    public final static Integer GAME_TYPE = 1173;
    public final static Integer GAME_TYPE_01 = 11731001;//基础策略
    public final static Integer GAME_TYPE_02 = 11731002;//当前策略

    //账户冻结状态  add by yuyong 2010-09-11
    public final static Integer ACCOUNT_FREEZE_STATUS = 1176;
    public final static Integer ACCOUNT_FREEZE_STATUS_01 = 11761001;//冻结中
    public final static Integer ACCOUNT_FREEZE_STATUS_02 = 11761002;//已释放

    //旧件回运方式  add by yuchanghong 2010-09-13
    public final static Integer OLD_RETURN_STATUS = 1177;
    public final static Integer OLD_RETURN_STATUS_01 = 11771001;//公路物流
    public final static Integer OLD_RETURN_STATUS_02 = 11771002;//铁路包裹
    public final static Integer OLD_RETURN_STATUS_03 = 11771003;//自送
    public final static Integer OLD_RETURN_STATUS_04 = 11771004;//快运

    public final static Integer RESERVE_TYPE = 1179;
    public final static Integer RESERVE_TYPE_01 = 11791001;//订单保留
    public final static Integer RESERVE_TYPE_02 = 11791002;//移库保留

    /*------------------add by wangchao 2010-09-13-----------------------*/
    public final static Integer PAY_TYPE = 1180; //付费方式
    public final static Integer PAY_TYPE_01 = 11801001;//自费
    public final static Integer PAY_TYPE_02 = 11801002;//OEM收费

    public final static String VEHICLE_OWN_01 = "自有车";
    public final static String VEHICLE_OWN_02 = "代管车";
    public final static String VEHICLE_OWN_03 = "代销车";
    //首页新闻状态 add by xiongchuan 2010-09-11
    public final static Integer NEWS_STATUS = 1174;
    public final static Integer NEWS_STATUS_1 = 11741001;//正常
    public final static Integer NEWS_STATUS_2 = 11741002;//作废
    public final static Integer NEWS_STATUS_3 = 11741003;//关闭

    //首页新闻类别
    public final static Integer NEWS_TYPE = 1175;
    public final static Integer NEWS_type_1 = 11751001;//备件管理
    public final static Integer NEWS_type_2 = 11751002;//服务政策
    public final static Integer NEWS_type_3 = 11751003;//系统应用
    public final static Integer NEWS_type_4 = 11751004;//技术通告
    public final static Integer NEWS_type_5 = 11751005;//汽车维修
    public final static Integer NEWS_type_6 = 11751006;//维修方法
    public final static Integer NEWS_type_7 = 11751007;//销售公告

    //查看权限
    public final static Integer VIEW_NEWS_TYPE = 1178;
    public final static Integer VIEW_NEWS_type_1 = 11781001;//经销商端
    public final static Integer VIEW_NEWS_type_2 = 11781002;//OEM端
    public final static Integer VIEW_NEWS_type_3 = 11781003;//公用

    //消息类型
    public final static String MSG_TYPE = "1199";
    public final static String MSG_TYPE_1 = "10771001,10771003,10771004";//销售
    public final static String MSG_TYPE_2 = "10771002";//售后
    public final static String MSG_TYPE_3 = "11991003";//公用

    //索赔单是否过三包期
    public final static Integer IS_NO_OVER_GUARANTEES = 1;//未过三包期

    //预付款金额百分比
    public final static Integer ADVANCE_ID = 20171001;

    //退车审核状态
    public final static Integer RETURN_CHECK_STATUS = 1182;
    public final static Integer RETURN_CHECK_STATUS_01 = 11821001;//待总部批复
    public final static Integer RETURN_CHECK_STATUS_02 = 11821002;//审核通过
    public final static Integer RETURN_CHECK_STATUS_03 = 11821003;//驳回
    public final static Integer RETURN_CHECK_STATUS_SYB = 11821004;//待事业部批复
    public final static Integer RETURN_CHECK_STATUS_05 = 11821005;//待区域审核
    public final static Integer RETURN_CHECK_STATUS_06 = 11821006;//待售后审核
    //费用类型
    public final static String FEE_TYPE = "1183";
    public final static String FEE_TYPE_01 = "11831001";//市场工单费用
    public final static String FEE_TYPE_02 = "11831002";//特殊工单费用
    public final static String FEE_TYPE_03 = "11831003";//特殊工单费用
    public final static String FEE_TYPE_04 = "11831004";//特殊工单费用
    public final static String FEE_TYPE_05 = "11831005";//特殊工单费用
    public final static String FEE_TYPE_06 = "11831006";//特殊工单费用

    //费用审核状态
    public final static String SPEFEE_STATUS = "1184";
    public final static String SPEFEE_STATUS_01 = "11841001";//未提报
    public final static String SPEFEE_STATUS_02 = "11841002";//已提报
    public final static String SPEFEE_STATUS_03 = "11841003";//服务经理退回
    public final static String SPEFEE_STATUS_04 = "11841004";//服务经理通过
    public final static String SPEFEE_STATUS_05 = "11841005";//车厂审核退回
    public final static String SPEFEE_STATUS_06 = "11841006";//车厂审核通过
    public final static String SPEFEE_STATUS_07 = "11841007";//部门总监退回
    public final static String SPEFEE_STATUS_08 = "11841008";//部门总监通过
    public final static String SPEFEE_STATUS_09 = "11841009";//销售公司总经理退回
    public final static String SPEFEE_STATUS_10 = "11841010";//销售公司总经理通过
    public final static String SPEFEE_STATUS_11 = "11841011";//集团总经理退回
    public final static String SPEFEE_STATUS_12 = "11841012";//集团总经理通过
    public final static String SPEFEE_STATUS_14 = "11841014";//最终审核

    //费用渠道
    public final static String FEE_CHANNEL = "1185";
    public final static String FEE_CHANNEL_01 = "11851001";
    public final static String FEE_CHANNEL_02 = "11851002";
    public final static String FEE_CHANNEL_03 = "11851003";
    public final static String FEE_CHANNEL_04 = "11851004";
    public final static String FEE_CHANNEL_01_VALUE = "售前外出维修";
    //市场工单中的费用明细
    public final static Integer FEE_TYPE1 = 1364;
    public final static Integer FEE_TYPE1_01 = 13641001; //市场工单
    public final static Integer FEE_TYPE1_02 = 13641002; //活动工单
    public final static Integer FEE_TYPE1_03 = 13641003; //市场工单--轿车

    //无零件
    public final static String NO_PARTNAME = "无零件"; //配件投诉类型
    public final static String NO_PARTCODE = "000000"; //配件投诉1
    //预授权标示 0 1

    public final static Integer APPROVAL_YN_NO = 0; //否
    public final static Integer APPROVAL_YN_YES = 1; //是

    /** 潜在客户上报 **/
    //证件类型代码
    Integer DICT_CERTIFICATE_TYPE = 1239; //证件类别
    Integer DICT_CERTIFICATE_TYPE_IDENTITY_CARD = 12391001; //居民身份证
    Integer DICT_CERTIFICATE_TYPE_PASSPORT = 12391002; //护照
    Integer DICT_CERTIFICATE_TYPE_OFFICER = 12391003; //军官
    Integer DICT_CERTIFICATE_TYPE_SOLDIER = 12391004; //士兵
    Integer DICT_CERTIFICATE_TYPE_POLICE_OFFICER = 12391005; //警官
    Integer DICT_CERTIFICATE_TYPE_OTHER = 12391006; //其他

    //购车目的
    Integer DICT_BUY_PURPOSE_TYPE = 1346; //购车目的类型
    Integer DICT_BUY_PURPOSE_INSTEAD_OF_WALK = 13461001; //代步工具
    Integer DICT_BUY_PURPOSE_COMMERCE_USE = 13461002; //商务用车
    Integer DICT_BUY_PURPOSE_AMUSEMENT = 13461003; //休闲娱乐
    Integer DICT_BUY_PURPOSE_OTHER = 13461004; //其他

    //职业类别
    Integer DICT_VOCATION_TYPE = 1240; //职业类别
    Integer DICT_VOCATION_LB = 12401001;//私营公司老板/自由职业者/个体户
    Integer DICT_VOCATION_GLRY = 12401002;//中层管理人员 (如：部门经理等)
    Integer DICT_VOCATION_XSRY = 12401003;//销售人员/销售代表
    Integer DICT_VOCATION_SYGS = 12401004;//私营公司职员/主管
    Integer DICT_VOCATION_JTZF = 12401005;//家庭主妇/夫
    Integer DICT_VOCATION_TX = 12401006;//退休
    Integer DICT_VOCATION_QT = 12401007;//其他
    Integer DICT_VOCATION_DY = 12401008;//没有工作
    Integer DICT_VOCATION_XS = 12401009;//学生
    Integer DICT_VOCATION_TLLD = 12401010;//体力劳动者(技术工或熟练工)
    Integer DICT_VOCATION_GWY = 12401011;//公务员，教师，警察，护士等
    Integer DICT_VOCATION_XZZY = 12401012;//行政职员
    Integer DICT_VOCATION_ZYRS = 12401013;//专业人士 (如：律师，医生等)
    Integer DICT_VOCATION_GCRY = 12401014;//高层管理人员 (如：首席执行总裁，总监，总经理等)

    //意向级别
    Integer DICT_INTENT_LEVEL = 1310; //意向级别
    Integer DICT_INTENT_LEVEL_H = 13101001; //H级
    Integer DICT_INTENT_LEVEL_A = 13101002; //A级
    Integer DICT_INTENT_LEVEL_B = 13101003; //B级
    Integer DICT_INTENT_LEVEL_C = 13101004; //C级
    Integer DICT_INTENT_LEVEL_N = 13101005; //N级
    Integer DICT_INTENT_LEVEL_FO = 13101006; //F0级
    Integer DICT_INTENT_LEVEL_F = 13101007; //F级
    Integer DICT_INTENT_LEVEL_O = 13101008; //O级
    Integer DICT_INTENT_LEVEL_D = 13101009; //D级

    //客户来源
    Integer DICT_CUS_SOURCE = 1311; //客户来源
    Integer DICT_CUS_SOURCE_EXHI_HALL = 13111001; //展厅活动
    Integer DICT_CUS_SOURCE_AD_ACTIVITY = 13111002; //报纸杂志
    Integer DICT_CUS_SOURCE_MARKET_ACTIVITY = 13111003; //市场展示及促销活动
    Integer DICT_CUS_SOURCE_TENURE_CUSTOMER = 13111004; //重复购买
    Integer DICT_CUS_SOURCE_FRIEND = 13111005; //朋友介绍
    Integer DICT_CUS_SOURCE_ADDI_PURCHASE = 13111006; //老客户介绍
    Integer DICT_CUS_SOURCE_OTHER = 13111007; //其他
    Integer DICT_CUS_SOURCE_VISITOR = 13111008; //陌生拜访
    Integer DICT_CUS_SOURCE_RADIO = 13111009; //电台广告
    Integer DICT_CUS_SOURCE_TV = 13111010; //电视广告
    Integer DICT_CUS_SOURCE_STREET = 13111011; //路牌广告
    Integer DICT_CUS_SOURCE_NET = 13111012; //网络广告
    Integer DICT_CUS_SOURCE_ROAD = 13111013; //路过

    //媒体类型
    Integer DICT_MEDIA_TYPE = 1298; //广告投放媒体类型
    Integer DICT_MEDIA_TYPE_NEWSPAPER = 12981001; //报纸
    Integer DICT_MEDIA_TYPE_TELEVISION = 12981002; //电视
    Integer DICT_MEDIA_TYPE_BROADCASTING = 12981003; //电台
    Integer DICT_MEDIA_TYPE_NET = 12981004; //网络
    Integer DICT_MEDIA_TYPE_MAGAZINE = 12981005; //杂志
    Integer DICT_MEDIA_TYPE_GUIDEPOST = 12981006; //路牌
    Integer DICT_MEDIA_TYPE_OUTDOORS = 12981007; //户外
    Integer DICT_MEDIA_TYPE_BODYWORK = 12981008; //车身
    Integer DICT_MEDIA_TYPE_OTHER = 12981009; //其它
    /** 潜在客户上报TC_CODE结束 **/

    public final static String ROOM_CHARGE = "QT004"; //住宿费
    public final static String EATUP_FEE = "QT003"; //餐补费
    public final static String TRANSPORTATION = "QT002"; //交通费
    public final static String FAX_FEE = "QT001"; //电话传真费
    public final static String SUBSIDIES_FEE = "QT005"; //补助工时费

    /** 保养费用比重参数ID(TM_BUSINESS_PARA) */
    public final static Integer FREE_PARA_TYPE = 20181001;

    //仓库级别
    public static final Integer WAREHOUSE_LEVEL = 1187;
    public static final Integer WAREHOUSE_LEVEL_01 = 11871001;//总部库
    public static final Integer WAREHOUSE_LEVEL_02 = 11871002;//中转库
    //是否里程限制
    public final static Integer ACTIVI_MILAGE_YES = 1; //是
    public final static Integer ACTIVI_MILAGE_NO = 0;//否

    //汇总报表操作状态
    public final static Integer TAXABLE_SERVICE_SUM = 1188;
    public final static Integer TAXABLE_SERVICE_SUM_WAIT = 11881000;//待收票
    public final static Integer TAXABLE_SERVICE_SUM_GET = 11881001;//已收票
    public final static Integer TAXABLE_SERVICE_SUM_FANCE = 11881002;//已财务挂账
    public final static Integer TAXABLE_SERVICE_SUM_FANCE3 = 11881003;//已退票
    public final static Integer TAXABLE_SERVICE_SUM_FANCE4 = 11881004;//已退帐

    //通用三包规则
    public final static Integer COMMON_RULE = 20130801; //通用三包规则

    //扣款类型
    public final static Integer Freeze_TYPE = 1347;
    public final static Integer Freeze_TYPE_01 = 13471001;//定做车预扣款

    //add by lishuai 集团客户审核状态
    public final static int FLEET_JUDE_STATUS = 1190;
    public final static int FLEET_JUDE_STATUS_01 = 11901001;//服务经理审核
    public final static int FLEET_JUDE_STATUS_02 = 11901002;//部门总监审核
    public final static int FLEET_JUDE_STATUS_03 = 11901003;//销售公司总经理审核
    public final static int FLEET_JUDE_STATUS_04 = 11901004;//集团总经理审核
    public final static int FLEET_JUDE_STATUS_05 = 11901005;//车厂审核
    public final static int FLEET_JUDE_STATUS_06 = 11901006;//最终审核

    //add by lishuai 审核权限
    public final static String FLEET_CON_STATUS = "1352";
    public final static String FLEET_CON_STATUS_00 = "13521000";//审核驳回
    public final static String FLEET_CON_STATUS_01 = "13521001";//业务中心审核
    public final static String FLEET_CON_STATUS_02 = "13521002";//销售中心审核
    public final static String FLEET_CON_STATUS_03 = "13521003";//总经理审批
    public final static String FLEET_CON_STATUS_04 = "13521004";//总经理审批

    //订单审核类型
    public final static Integer ORDER_CHECK_TYPE = 1348;
    public final static Integer ORDER_CHECK_TYPE_01 = 13481001;//事业部审核
    public final static Integer ORDER_CHECK_TYPE_02 = 13481002;//销售部审核
    public final static Integer ORDER_CHECK_TYPE_DLR = 13481003;//经销商预审核

    //回运清单类型(加入二级经销商后)
    public final static Integer RETURNORDER_TYPE = 1312;
    public final static Integer RETURNORDER_TYPE_01 = 13121001;//未上报
    public final static Integer RETURNORDER_TYPE_02 = 13121002;//已上报
    public final static Integer RETURNORDER_TYPE_03 = 13121003;//已完成
    public final static Integer RETURNORDER_TYPE_04 = 13121004;//已签收
    public final static Integer RETURNORDER_TYPE_05 = 13121005;//待签收

    //经销商地址更改申请状态
    public final static Integer DEALER_ADDRESS_CHANGE_STATUS = 1353;
    public final static Integer DEALER_ADDRESS_CHANGE_STATUS_01 = 13531001;//未提报
    public final static Integer DEALER_ADDRESS_CHANGE_STATUS_02 = 13531002;//待审核
    public final static Integer DEALER_ADDRESS_CHANGE_STATUS_FIRST_CHK = 13531005;//初审完成
    public final static Integer DEALER_ADDRESS_CHANGE_STATUS_03 = 13531003;//无效
    public final static Integer DEALER_ADDRESS_CHANGE_STATUS_04 = 13531004;//有效
    public final static Integer DEALER_ADDRESS_CHANGE_STATUS_Logistics = 13531006;//待物流部审核

    //结算汇总单状态
    public final static Integer BALANCE_GATHER_TYPE = 1315;
    public final static Integer BALANCE_GATHER_TYPE_01 = 13151001;//未上报
    public final static Integer BALANCE_GATHER_TYPE_02 = 13151002;//已上报
    public final static Integer BALANCE_GATHER_TYPE_03 = 13151003;//已签收
    public final static Integer BALANCE_GATHER_TYPE_04 = 13151004;//已审核

    //发运订单变更类型
    public final static Integer REQ_LOG_TYPE = 1355;
    public final static Integer REQ_LOG_TYPE_01 = 13551001; //订单提报
    public final static Integer REQ_LOG_TYPE_02 = 13551002; //订单审核
    public final static Integer REQ_LOG_TYPE_03 = 13551003; //经销商确认
    public final static Integer REQ_LOG_TYPE_04 = 13551004; //发运指令下达
    public final static Integer REQ_LOG_TYPE_05 = 13551005; //财务审核
    public final static Integer REQ_LOG_TYPE_06 = 13551006; //ERP订单生成
    public final static Integer REQ_LOG_TYPE_07 = 13551007; //开票
    public final static Integer REQ_LOG_TYPE_08 = 13551008; //发运
    public final static Integer REQ_LOG_TYPE_09 = 13551009; //订单取消
    public final static Integer REQ_LOG_TYPE_YTB = 13551010; //订单预提报
    public final static Integer REQ_LOG_TYPE_BH = 13551011; //订单预提报
    public final static Integer REQ_LOG_TYPE_TZ = 13551012; //订单预提报

    //业务范围类型
    public final static Integer AREA_YTPE = 1356;
    public final static Integer AREA_YTPE_01 = 13561001; //专用车
    public final static Integer AREA_YTPE_02 = 13561002; //非专用车

    Integer VEHICLE_CHANGE_STATUS = 1313;
    Integer VEHICLE_CHANGE_STATUS_01 = 13131001;//已保存
    Integer VEHICLE_CHANGE_STATUS_02 = 13131002;//审核中
    Integer VEHICLE_CHANGE_STATUS_03 = 13131003;//审核通过
    Integer VEHICLE_CHANGE_STATUS_04 = 13131004;//审核退回

    //车辆变更申请类型
    Integer VEHICLE_CHANGE_TYPE = 1314;
    Integer VEHICLE_CHANGE_TYPE_01 = 13141001;//行驶里程变更
    Integer VEHICLE_CHANGE_TYPE_02 = 13141002;//保养次数变更
    Integer VEHICLE_CHANGE_TYPE_03 = 13141003;//购车时间变更
    Integer VEHICLE_CHANGE_TYPE_04 = 13141004;//三包策略变更
    Integer VEHICLE_CHANGE_TYPE_05 = 13141005;//车主信息变更

    //数据提报错误类型
    Integer VEHICLE_CHANGE_ERROR = 1316;
    Integer VEHICLE_CHANGE_ERROR_01 = 13161001;//操作错误

    //工单问题状态
    Integer RO_PRO_STATUS = 1359;
    Integer RO_PRO_STATUS_01 = 13591001;//正常工单
    Integer RO_PRO_STATUS_02 = 13591002;//问题工单

    //经销商作业等级
    public final static Integer DEALER_LABOUR_CODE = 1357;
    public final static Integer DEALER_LABOUR_CODE_01 = 13571001;
    public final static Integer DEALER_LABOUR_CODE_02 = 13571002;
    public final static Integer DEALER_LABOUR_CODE_03 = 13571003;
    public final static Integer DEALER_LABOUR_CODE_04 = 13571004;

    /************* Iveron by 2010-11-05 ******************/
    //打印名称
    public final static Integer print = 1358;
    public final static Integer print_code = 13581001;//长安微车旧件标签打印
    public final static Integer print_old = 13581002;//微车，轿车旧件打印区分
    public final static Integer print_old3 = 13581003;//微车，轿车旧件打印区分
    //申请删除状态
    Integer RO_APP_STATUS = 1362;
    Integer RO_APP_STATUS_01 = 13621001;//未申请
    Integer RO_APP_STATUS_02 = 13621002;//已通过
    Integer RO_APP_STATUS_03 = 13621003;//已申请

    Integer TYPE_CODE = 2026;//用来取出旧件原始起止时间(轿车和微车)

    public static final String DAY = "8001";
    public static final String DAY_12 = "80021001";//12月结算日期(1-10)
    public static final String OLD_DAY_12 = "80021002";//12月旧件日期
    public static final String DAY_12_31 = "80021003";//12月结算日期(11-31)
    /************* Iveron by 2010-11-05 ******************/

    /********* add by liuxh 20101115 增加经销商结算级别和开票级别 ***********/
    public final static String BALANCE_LEVEL = "1360";
    public final static String BALANCE_LEVEL_SELF = "13601001"; //独立结算
    public final static String BALANCE_LEVEL_HIGH = "13601002"; //上级结算

    public final static String INVOICE_LEVEL = "1361";
    public final static String INVOICE_LEVEL_SELF = "13611001"; //独立开票
    public final static String INVOICE_LEVEL_HIGH = "13611002"; //上级开票
    /********* add by liuxh 20101115 增加经销商结算级别和开票级别 ***********/

    public final static String DLR_SERVICE_STATUS = "1369";
    public final static String DLR_SERVICE_STATUS_01 = "13691001"; //意向
    public final static String DLR_SERVICE_STATUS_02 = "13691002"; //正常
    public final static String DLR_SERVICE_STATUS_03 = "13691003"; //拟撤
    public final static String DLR_SERVICE_STATUS_04 = "13691004"; //已撤	
    //巡航服务路线状态
    public final static Integer CRUISE_STATUS = 1363;
    public final static Integer CRUISE_STATUS_01 = 13631001;//未上报
    public final static Integer CRUISE_STATUS_02 = 13631002;//已上报
    public final static Integer CRUISE_STATUS_03 = 13631003;//审核退回
    public final static Integer CRUISE_STATUS_04 = 13631004;//已审核
    public final static Integer CRUISE_STATUS_05 = 13631005;//中止
    public final static Integer CRUISE_STATUS_06 = 13631006;//已批复
    public final static Integer CRUISE_STATUS_07 = 13631007;//批复退回

    /********* add by devin 20121107 增加物流评价 **start ***********/
	/* 物流评价类型 */
    public final static String LOGISTICS_COMMENT_TYPE = "3001";
    public final static String LOGISTICS_COMMENT_TYPE_DEALER = "30011001"; //经销商
    public final static String LOGISTICS_COMMENT_TYPE_CARRIER = "30011002"; //承运商

    /* 经销商物流评价项目 */
    public final static String LOGISTICS_D_COMMENT = "3002";
    public final static String LOGISTICS_D_COMMENT_ATTITUDE = "30021001"; //服务态度
    public final static String LOGISTICS_D_COMMENT_ONTIME = "30021002"; //按期交货
    public final static String LOGISTICS_D_COMMENT_DAMAGE = "30021003"; //车辆完好
    public final static String LOGISTICS_D_COMMENT_AMOUNT = "30021004"; //数量满足
    public final static String LOGISTICS_D_COMMENT_ATONE = "30021005"; //货损赔偿

    /* 承运商物流评价项目 */
    public final static String LOGISTICS_CY_COMMENT = "3003";
    public final static String LOGISTICS_CY_COMMENT_ATTITUDE = "30031001"; //人员态度
    public final static String LOGISTICS_CY_COMMENT_ONTIME = "30031002"; //及时验收

    /* 物流评价分数 */
    public final static String LOGISTICS_POINT = "3004";
    public final static String LOGISTICS_POINT_01 = "30041001"; // 0 分
    public final static String LOGISTICS_POINT_02 = "30041002"; // 1 分
    public final static String LOGISTICS_POINT_03 = "30041003"; // 2 分
    public final static String LOGISTICS_POINT_04 = "30041004"; // 3 分
    public final static String LOGISTICS_POINT_05 = "30041005"; // 4 分
    public final static String LOGISTICS_POINT_06 = "30041006"; // 5 分

    /* 物流总体评价 */
    public final static String LOGISTICS_TOTAL = "3005";
    public final static String LOGISTICS_TOTAL_01 = "30051001"; // 0 分(好评)
    public final static String LOGISTICS_TOTAL_02 = "30051002"; // 1 分(中评)
    public final static String LOGISTICS_TOTAL_03 = "30051003"; // 2 分(差评)

    /********* add by devin 20121107 增加物流评价 **end *********/

    public static final String CAR_STATUS = "8001";
    public static final String CAR_STATUS_SALES = "80011001";//未售
    public static final String CAR_STATUS_SALES_NO = "80011002";//已售

    //了解途径
    public static final String KNOW_ADDRESS = "8003";
    public static final String KNOW_ADDRESS_01 = "80031001";//朋友介绍
    public static final String KNOW_ADDRESS_02 = "80031002";//报纸广告
    public static final String KNOW_ADDRESS_03 = "80031003";//网络广告
    public static final String KNOW_ADDRESS_04 = "80031004";//电视广告
    public static final String KNOW_ADDRESS_05 = "80031005";//广播广告
    public static final String KNOW_ADDRESS_06 = "80031006";//户外广告
    public static final String KNOW_ADDRESS_07 = "80031007";//展览展示
    public static final String KNOW_ADDRESS_08 = "80031008";//DM宣传
    public static final String KNOW_ADDRESS_09 = "80031009";//短信
    public static final String KNOW_ADDRESS_10 = "80031010";//重复购买
    //购买原因
    public static final String SALES_RESON = "8004";
    public static final String SALES_RESON_01 = "80041001";//价格
    public static final String SALES_RESON_02 = "80041002";//操作性
    public static final String SALES_RESON_03 = "80041003";//品牌/口碑
    public static final String SALES_RESON_04 = "80041004";//外观
    public static final String SALES_RESON_05 = "80041005";//配置
    public static final String SALES_RESON_06 = "80041006";//售后服务
    public static final String SALES_RESON_07 = "80041007";//性能
    public static final String SALES_RESON_08 = "80041008";//安全性
    public static final String SALES_RESON_09 = "80041009";//舒适性
    public static final String SALES_RESON_10 = "80041010";//动力性
    public static final String SALES_RESON_11 = "80041011";//服务
    public static final String SALES_RESON_12 = "80041012";//质量
    public static final String SALES_RESON_13 = "80041013";//内饰
    //购买用途
    public static final String SALES_ADDRESS = "8005";
    public static final String SALES_ADDRESS_01 = "80051001";//私家车
    public static final String SALES_ADDRESS_02 = "80051002";//公务车
    public static final String SALES_ADDRESS_03 = "80051003";//出租车

    // 地址时限
    public static final Integer ADDRESS_TIME_LIMIT = 1368;
    public static final Integer ADDRESS_TIME_LIMIT_PERP = 13681001; // 永久
    public static final Integer ADDRESS_TIME_LIMIT_TEMP = 13681002; // 暂时

    //add by wanghx 增加基地实体
    // 微车
    public final static Integer ENTITY_WC_CQ = 82; // 微车重庆基地
    public final static Integer ENTITY_WC_HE = 142; // 微车河北基地
    public final static Integer ENTITY_WC_NJ = 197; // 微车南京基地
    // 轿车
    public final static Integer ENTITY_JC = 726; // 轿车实体

    // 车厂公司
    public final static String OEM_COM_SVC = "4010010100070674"; // 微车车厂公司
    public final static String OEM_COM_JC = "2010010100070674"; // 轿车车厂公司

    public final static Integer IS_ALLOW_RESERVE_MORE = 20501001;// 订单资源审核保留资源控制

    // 实销退车类型
    public static final Integer SALE_RETURN = 1365;
    public static final Integer SALE_RETURN_ZLTC = 13651001; // 质量退车
    public static final Integer SALE_RETURN_GXFWTC = 13651002; // 更新服务记录退车
    public static final Integer SALE_PRICE = 3017;

    // 媒体车型品牌
    public static final Integer MEDIA_MODEL = 1366;
    public static final Integer MEDIA_MODEL_BBL = 13661001; // 幻速S2
    public static final Integer MEDIA_MODEL_BBM = 13661002; // 幻速S3
    /*public static final Integer MEDIA_MODEL_YX = 13661003; // 悦翔
	public static final Integer MEDIA_MODEL_CX30 = 13661004; // CX30
	public static final Integer MEDIA_MODEL_CX20 = 13661005; // CX20
	public static final Integer MEDIA_MODEL_AWQX = 13661006; // A网全系
	public static final Integer MEDIA_MODEL_BWQX = 13661007; // B网全系
	public static final Integer MEDIA_MODEL_YD = 13661008; // 逸动
	public static final Integer MEDIA_MODEL_YXV3 = 13661009; // 悦翔v3
	public static final Integer MEDIA_MODEL_YXV5 = 13661010; // 悦翔v3
	public static final Integer MEDIA_MODEL_CS35 = 13661011; // CS35
*/
    // 媒体类型
    public static final Integer MEDIA_TYPE = 1367;
    public static final Integer MEDIA_TYPE_QT = 13671001; // 墙体
    public static final Integer MEDIA_TYPE_DT = 13671002; // 电台
    public static final Integer MEDIA_TYPE_DS = 13671003; // 电视
    public static final Integer MEDIA_TYPE_BZ = 13671004; // 报纸
    public static final Integer MEDIA_TYPE_WL = 13671005; // 网络
    public static final Integer MEDIA_TYPE_DX = 13671006; // 短信
    public static final Integer MEDIA_TYPE_OTHER = 13671007; // 其他

    //服务车申请状态(vc)
    public static final Integer SERVICE_CAR_APPLY = 1370;
    public static final Integer SERVICE_CAR_APPLY_01 = 13701001; //未提报
    public static final Integer SERVICE_CAR_APPLY_02 = 13701002; //已提报
    public static final Integer SERVICE_CAR_APPLY_03 = 13701003; //事业部通过
    public static final Integer SERVICE_CAR_APPLY_04 = 13701004; //事业部退回
    public static final Integer SERVICE_CAR_APPLY_05 = 13701005; //事业部拒绝
    public static final Integer SERVICE_CAR_APPLY_06 = 13701006; //价格初审通过
    public static final Integer SERVICE_CAR_APPLY_07 = 13701007; //价格初审拒绝
    public static final Integer SERVICE_CAR_APPLY_08 = 13701008; //总部通过
    public static final Integer SERVICE_CAR_APPLY_09 = 13701009; //总部拒绝
    public static final Integer SERVICE_CAR_APPLY_10 = 13701010; //同意兑现

    //服务车类型(vc)
    public static final Integer SERVICE_CAR = 1371;
    public static final Integer SERVICE_CAR_01 = 13711001;
    public static final Integer SERVICE_CAR_02 = 13711002;
    public static final Integer SERVICE_CAR_03 = 13711003;
    public static final Integer SERVICE_CAR_04 = 13711004;
    public static final Integer SERVICE_CAR_05 = 13711005;
    public static final Integer SERVICE_CAR_06 = 13711006;
    public static final Integer SERVICE_CAR_07 = 13711007;

    //服务车资料上传状态(vc)
    public static final Integer SERVICE_CAR_FILES = 1372;
    public static final Integer SERVICE_CAR_FILES_01 = 13721001; //未提报
    public static final Integer SERVICE_CAR_FILES_02 = 13721002; //已提报
    public static final Integer SERVICE_CAR_FILES_03 = 13721003; //总部通过
    public static final Integer SERVICE_CAR_FILES_04 = 13721004; //总部退回
    public static final Integer SERVICE_CAR_FILES_05 = 13721005; //总部拒绝
    public static final Integer SERVICE_CAR_FILES_06 = 13721006; //销售部通过
    public static final Integer SERVICE_CAR_FILES_07 = 13721007; //销售部退回
    public static final Integer SERVICE_CAR_FILES_08 = 13721008; //销售部拒绝
    public static final Integer SERVICE_CAR_FILES_09 = 13721009; //总部资料已签收
    public static final Integer SERVICE_CAR_FILES_10 = 13721010; //总部资料已退回
    public static final Integer SERVICE_CAR_FILES_11 = 13721011; //总部资料已拒绝
    public static final Integer SERVICE_CAR_FILES_12 = 13721012; //销售部资料已签收
    public static final Integer SERVICE_CAR_FILES_13 = 13721013; //销售部资料退回
    public static final Integer SERVICE_CAR_FILES_14 = 13721014; //销售部资料已拒绝

    //车辆PIN申请状态
    public static final Integer VEHICLE_PIN = 1380;
    public static final Integer VEHICLE_PIN_01 = 13801001; //已提报
    public static final Integer VEHICLE_PIN_02 = 13801002; //已回复

    //服务车审核回退功能
    public static final Integer GO_LAST = 1373;
    public static final Integer GO_LAST_01 = 13731001; //价格初审回退
    public static final Integer GO_LAST_02 = 13731002; //总部回退

    /******* add by liuxh 20101229 增加轿车和微车区分代码 ******/
    public static final Integer CHANA_SYS = 40000000;
    public static final Integer CHG_SINGLE_PRICE = 40140000;
    public static final Integer ENERGY_APPLY_END_DATE = 40150000;
    public static final Integer ENERGY_VHL_SALES_DATE = 40160000;

    public static final String COMPANY_CODE_JC = "JC"; //轿车
    public static final String COMPANY_CODE_CVS = "CVS"; //微车

    public static final Double ACTIVITIES_TAX = 1D; // 市场活动税率
    public static final Double FLEET_ACTIVITIES_TAX = 1.17; //市场活动大客户税率

    /************** Iverson Add By 2011-01-06申明旧件结算时间类型 **************************/
    public static final int CHANGE_TYPE = 8007; // 申明类型
    public static final int CHANGE_TYPE_OLD = 80071001; //申明旧件
    public static final int CHANGE_TYPE_BALABCE = 80071002; //申明结算
    public static final int chana = 8008; // 申明类型
    public static final int chana_jc = 80081001; //轿车
    public static final int chana_wc = 80081002; //微车

    public static final Integer PARAM_TYPE = 1388;//输入框类型
    public static final Integer PARAM_TYPE_01 = 13881001;//文本框
    public static final Integer PARAM_TYPE_02 = 13881002;//日期控件
    public static final Integer PARAM_TYPE_03 = 13881003;//下拉列表
    public static final Integer PARAM_TYPE_04 = 13881004;//多值输入框

    /************ zhumingwei 2011-02-22 *****************************/
    public final static Integer MAIN_RESOURCES = 8009;//维修资源
    public final static Integer MAIN_RESOURCES_01 = 80091001; //无
    public final static Integer MAIN_RESOURCES_02 = 80091002; //一类
    public final static Integer MAIN_RESOURCES_03 = 80091003; //二类
    public final static Integer MAIN_RESOURCES_04 = 80091004; //三类

    public final static Integer ADMIN_LEVEL = 8010;//行政级别
    public final static Integer ADMIN_LEVEL_01 = 80101001; //地级市
    public final static Integer ADMIN_LEVEL_02 = 80101002; //省会
    public final static Integer ADMIN_LEVEL_03 = 80101003; //省会级
    public final static Integer ADMIN_LEVEL_04 = 80101004; //县级
    public final static Integer ADMIN_LEVEL_05 = 80101005; //县级市
    public final static Integer ADMIN_LEVEL_06 = 80101006; //乡镇

    public final static Integer IMAGE_LEVEL = 8011;//形象等级

    public final static Integer IMAGE_LEVEL_01 = 80111001; //A类
    public final static Integer IMAGE_LEVEL_02 = 80111002; //B类
    public final static Integer IMAGE_LEVEL_03 = 80111003; //C类
    public final static Integer IMAGE_LEVEL_04 = 80111004; //D类
    public final static Integer IMAGE_LEVEL_05 = 80111005; //E类
    public final static Integer IMAGE_LEVEL_06 = 80111006; //F类
	
	/*public final static Integer IMAGE_LEVEL_01 = 80111001; //形象等级
	public final static Integer IMAGE_LEVEL_02 = 80111002; //销售服务非标店
	public final static Integer IMAGE_LEVEL_03 = 80111003; //汽车超市标准B型
	public final static Integer IMAGE_LEVEL_04 = 80111004; //服务非标店
	public final static Integer IMAGE_LEVEL_05 = 80111005; //Ed类专卖店
	public final static Integer IMAGE_LEVEL_06 = 80111006; //Ec类专卖店
	public final static Integer IMAGE_LEVEL_07 = 80111007; //Eb类专卖店
	public final static Integer IMAGE_LEVEL_08 = 80111008; //Dd类专卖店
	public final static Integer IMAGE_LEVEL_09 = 80111009; //Dc类专卖店
	public final static Integer IMAGE_LEVEL_10 = 80111010; //Db类专卖店
	public final static Integer IMAGE_LEVEL_11 = 80111011; //Da类专卖店
	public final static Integer IMAGE_LEVEL_12 = 80111012; //c类技术服务店
	public final static Integer IMAGE_LEVEL_13 = 80111013; //Cd类专卖店
	public final static Integer IMAGE_LEVEL_14 = 80111014; //Cc类专卖店
	public final static Integer IMAGE_LEVEL_15 = 80111015; //Cb类专卖店
	public final static Integer IMAGE_LEVEL_16 = 80111016; //Ca类4S店
	public final static Integer IMAGE_LEVEL_17 = 80111017; //b类技术服务中心
	public final static Integer IMAGE_LEVEL_18 = 80111018; //Bc类专卖店
	public final static Integer IMAGE_LEVEL_19 = 80111019; //Bb类专卖店
	public final static Integer IMAGE_LEVEL_20 = 80111020; //Bb类4S店
	public final static Integer IMAGE_LEVEL_21 = 80111021; //Ba类4S店
	public final static Integer IMAGE_LEVEL_22 = 80111022; //a类技术服务中心
	public final static Integer IMAGE_LEVEL_23 = 80111023; //Ab类4S店
	public final static Integer IMAGE_LEVEL_24 = 80111024; //Aa类4S店
	public final static Integer IMAGE_LEVEL_25 = 80111025;
	public final static Integer IMAGE_LEVEL_26 = 80111026;
	public final static Integer IMAGE_LEVEL_27 = 80111027;
	public final static Integer IMAGE_LEVEL_28 = 80111028;
	public final static Integer IMAGE_LEVEL_29 = 80111029;
	public final static Integer IMAGE_LEVEL_30 = 80111030;//A类
	public final static Integer IMAGE_LEVEL_31 = 80111031;//B类
	public final static Integer IMAGE_LEVEL_32 = 80111032;//C类
	public final static Integer IMAGE_LEVEL_33 = 80111033;//D类
	public final static Integer IMAGE_LEVEL_34 = 80111034;//E类
	public final static Integer IMAGE_LEVEL_35 = 80111035;//F类
	public final static Integer IMAGE_LEVEL_36 = 80111036;
	public final static Integer IMAGE_LEVEL_37 = 80111037;
	public final static Integer IMAGE_LEVEL_38 = 80111038;
	public final static Integer IMAGE_LEVEL_39 = 80111039;
	public final static Integer IMAGE_LEVEL_40 = 80111040;
	public final static Integer IMAGE_LEVEL_41 = 80111041;
	public final static Integer IMAGE_LEVEL_42 = 80111042;
	public final static Integer IMAGE_LEVEL_43 = 80111043;
	public final static Integer IMAGE_LEVEL_44 = 80111044;
	public final static Integer IMAGE_LEVEL_45 = 80111045;
	public final static Integer IMAGE_LEVEL_46 = 80111046;
	public final static Integer IMAGE_LEVEL_47 = 80111047;
	public final static Integer IMAGE_LEVEL_48 = 80111048;
	public final static Integer IMAGE_LEVEL_49 = 80111049;
	public final static Integer IMAGE_LEVEL_50 = 80111050;*/

    public final static Integer DEALER_CHANGE = 8012;//经销商变更信息
    public final static Integer DEALER_CHANGE_01 = 80121001; //待上报
    public final static Integer DEALER_CHANGE_02 = 80121002; //已上报
    public final static Integer DEALER_CHANGE_03 = 80121003; //审核通过
    public final static Integer DEALER_CHANGE_04 = 80121004; //审核拒绝
    /************ zhumingwei 2011-02-22 *****************************/

    /**************************** 车辆惠民节能状态 ****************************************/
    public static final Integer VEHICLE_ENERGY_CON = 8013;
    public static final Integer VEHICLE_ENERGY_CON_APPLY = 80131001; //已提报
    public static final Integer VEHICLE_ENERGY_CON_SALEPASS = 80131002; //销售部审核通过
    public static final Integer VEHICLE_ENERGY_CON_PASS = 80131003; //审核完成
    public static final Integer VEHICLE_ENERGY_CON_CANCLE = 80131004; //已取消

    /************************************** 质损类型 **********************************************/
    public static final Integer CLAIM_ZHISHUN = 8014;
    public static final Integer CLAIM_ZHISHUN_1 = 80141001; //运输质损
    public static final Integer CLAIM_ZHISHUN_2 = 80141002; //售前质损

    /************************************** 维修部件 **********************************************/
    public static final Integer REPAIR_PART = 8016;
    public static final Integer REPAIR_PART_DQYS = 80161001; //电气及约束
    public static final Integer RRPAIR_PART_DPXT = 80161002; //发动机及电喷系统
    public static final Integer REPARI_PART_DP = 80161003; //底盘
    public static final Integer REPARI_PART_CS = 80161004; //车身

    public static final String PLACE_OF_CQ = "80171001";//重庆
    public static final String PLACE_OF_HB = "80171002";//河北
    public static final String PLACE_OF_NJ = "80171003";//南京
    public static final String PLACE_OF_CH = "80171004";//昌河

    //zhumingwei 2011-04-15
    public static final Integer OLD_PRICE = 8018;//旧件运费状态
    public static final Integer OLD_PRICE_1 = 80181001; //未开票
    public static final Integer OLD_PRICE_2 = 80181002; //开票

    /************************************** 市场工单活动项目 ***********************************************/
    public static final Integer ACTIVITY_PROJECT = 8021; //市场工单活动项目
    public static final Integer ACTIVITY_PROJECT_TRADING_CLAIM = 80211001; //商誉索赔
    public static final Integer ACTIVITY_PROJECT_TRANSPORT_DUTY = 80211002; //运输责任
    /************************************** 市场工单活动项目 ***********************************************/

    /************************************** 经销商站长及三包员职位 *********************************************/
    public static final Integer STATION_HEAD_AND_AEGIS = 8022; //服务站人员及联系方式
    public static final Integer STATION_HEAD_AND_AEGIS_01 = 80221001; //服务经理
    public static final Integer STATION_HEAD_AND_AEGIS_02 = 80221002; //索赔主管
    public static final Integer STATION_HEAD_AND_AEGIS_03 = 80221003; //技术主管
    public static final Integer STATION_HEAD_AND_AEGIS_04 = 80221004; //服务主管
    public static final Integer STATION_HEAD_AND_AEGIS_05 = 80221005; //配件主管
    public static final Integer DisplancementCarrequ_replace = 8019;//二手车类型
    public static final Integer DisplancementCarrequ_replace_1 = 80191001; //以旧换新
    public static final Integer DisplancementCarrequ_replace_2 = 80191002; //报废换新
    public static final Integer DisplancementCarrequ_replace_3 = 80191003; //2012大回访置换
    public static final Integer DisplancementCarrequ_replace_4 = 80191004; //2012大回访置换销办字328
    public static final Integer DisplancementCarrequ_replace_5 = 80191005;

    public static final Integer DisplancementCarrequ_cek = 8020;//二手车置换状态
    public static final Integer DisplancementCarrequ_cek_1 = 80201001; //已提报
    public static final Integer DisplancementCarrequ_cek_2 = 80201002; //分销中心初审完成
    public static final Integer DisplancementCarrequ_cek_3 = 80201003; //总部审核完成
    public static final Integer DisplancementCarrequ_cek_4 = 80201004; //总部已确认
    public static final Integer DisplancementCarrequ_cek_5 = 80201005; //经销商已确认
    public static final Integer DisplancementCarrequ_cek_SYBQS = 80201006; //总部已签收
    public static final Integer DisplancementCarrequ_cek_RETRUN = 80201007; //驳回

    public static final Integer DisplancementCarrequ_zige = 8023;//置换资格
    public static final Integer DisplancementCarrequ_zige_1 = 80231001;//4S+1S
    public static final Integer DisplancementCarrequ_zige_2 = 80231002;//二手车市场
    public static final Integer DisplancementCarrequ_zige_3 = 80231003;//其他
    public static final Integer DisplancementCarrequ_zige_4 = 80231004;//4S+1S及其他
    public static final Integer DisplancementCarrequ_zige_5 = 80231005;//二手车市场及其他

    // 地址日志审核状态
    public static final Integer ADDRESS_CHECK_PASS = 80301001;
    public static final Integer ADDRESS_CHECK_BACK = 80301002;

    //客户登记跟踪信息渠道
    public static final Integer INFO_DITCH = 8052;
    public static final Integer INFO_DITCH_PYTJ = 80521001; // 朋友推荐
    public static final Integer INFO_DITCH_ZXH = 80521002; // 展销会
    public static final Integer INFO_DITCH_DZXY = 80521003; // 店招吸引
    public static final Integer INFO_DITCH_BZ = 80521004; // 报纸
    public static final Integer INFO_DITCH_DS = 80521005; // 电视
    public static final Integer INFO_DITCH_DX = 80521006; // 短信
    public static final Integer INFO_DITCH_GB = 80521007; // 广播
    public static final Integer INFO_DITCH_WL = 80521008; // 网络
    public static final Integer INFO_DITCH_HWGG = 80521009; // 户外广告
    public static final Integer INFO_DITCH_ZZ = 80521010; // 杂志

    //客户登记跟踪购买侧重点
    public static final Integer BUY_POINT = 8053;
    public static final Integer BUY_POINT_WG = 80531001; // 外观
    public static final Integer BUY_POINT_DLXN = 80531002; // 动力性能
    public static final Integer BUY_POINT_SYCB = 80531003; // 使用成本
    public static final Integer BUY_POINT_SHFB = 80531004; // 售后方便
    public static final Integer BUY_POINT_SSX = 80531005; // 舒适性
    public static final Integer BUY_POINT_AQX = 80531006; // 安全性
    public static final Integer BUY_POINT_CKX = 80531007; // 操控性
    public static final Integer BUY_POINT_JG = 80531008; // 价格
    public static final Integer BUY_POINT_JSNY = 80531009; // 结实耐用
    public static final Integer BUY_POINT_KJ = 80531010; // 空间

    //客户登记跟踪放弃购买原因
    public static final Integer DESERT_REASON = 8054;
    public static final Integer DESERT_REASON_CWG = 80541001; // 车的外观
    public static final Integer DESERT_REASON_YH = 80541002; // 油耗
    public static final Integer DESERT_REASON_CPZL = 80541003; // 产品质量
    public static final Integer DESERT_REASON_JG = 80541004; // 价格
    public static final Integer DESERT_REASON_KJ = 80541005; // 空间
    public static final Integer DESERT_REASON_SHZC = 80541006; // 售后服务政策
    public static final Integer DESERT_REASON_QTPP = 80541007; // 考虑其他品牌

    //客户登记跟踪客户性质
    public static final Integer CUS_NATURE = 8055;
    public static final Integer CUS_NATURE_XG = 80551001; // 新购
    public static final Integer CUS_NATURE_HG = 80551002; // 换购
    public static final Integer CUS_NATURE_ZG = 80551003; // 增购BARGAIN_CUS

    //客户登记跟踪成交客户
    public static final Integer BARGAIN_CUS = 8056;
    public static final Integer BARGAIN_CUS_SQ = 80561001; // 首洽成交
    public static final Integer BARGAIN_CUS_GZ = 80561002; // 跟踪成交
    public static final Integer BARGAIN_CUS_SJ = 80561003; // 试驾成交

    //销售顾问学历
    public static final Integer SALES_CONSULTANT_RECORDS = 8057;
    public static final Integer SALES_CONSULTANT_RECORDS_XX = 80571001; // 小学
    public static final Integer SALES_CONSULTANT_RECORDS_CZ = 80571002; // 初中
    public static final Integer SALES_CONSULTANT_RECORDS_GZ = 80571003; // 高中
    public static final Integer SALES_CONSULTANT_RECORDS_ZZ = 80571004; // 中专
    public static final Integer SALES_CONSULTANT_RECORDS_DZ = 80571005; // 大专
    public static final Integer SALES_CONSULTANT_RECORDS_BK = 80571006; // 本科
    public static final Integer SALES_CONSULTANT_RECORDS_SS = 80571007; // 硕士
    public static final Integer SALES_CONSULTANT_RECORDS_BS = 80571008; // 博士

    //销售顾问状态
    public static final Integer SALES_CONSULTANT_STATUS = 8058;
    public static final Integer SALES_CONSULTANT_STATUS_RAISE = 80581001; // 已提报
    public static final Integer SALES_CONSULTANT_STATUS_PASS = 80581002; // 审核通过
    public static final Integer SALES_CONSULTANT_STATUS_RETURN = 80581003; // 审核驳回
    public static final Integer SALES_CONSULTANT_STATUS_CANCEL = 80581004; // 取消

    /** 维修客户回访常量 **/
    public static final Integer NO_VISIT_REASON = 8050; //回访不成功原因
    public static final Integer NO_VISIT_REASON_1 = 8050001; //客户信息错误
    public static final Integer NO_VISIT_REASON_2 = 8050002; // 成功
    public static final Integer NO_VISIT_REASON_3 = 8050003;// 无法接通
    public static final Integer NO_VISIT_REASON_4 = 8050004;// 拒绝回访

    public static final Integer NO_SATISFIED = 8040; //不满意项
    public static final Integer NO_SATISFIED_1 = 8040001; //服务接待
    public static final Integer NO_SATISFIED_2 = 8040002;//服务环境
    public static final Integer NO_SATISFIED_3 = 8040003;//维修质量
    public static final Integer NO_SATISFIED_4 = 8040004;//等候时间
    public static final Integer NO_SATISFIED_5 = 8040005;//维修时间
    public static final Integer NO_SATISFIED_6 = 8040006;//备件保供
    public static final Integer NO_SATISFIED_7 = 8040007;//维修收费
    public static final Integer NO_SATISFIED_8 = 8040008;//产品质量

    public static final Integer REPAIR_ITEM = 8060; //维修项目
    public static final Integer REPAIR_ITEM_1 = 8060001; //保养
    public static final Integer REPAIR_ITEM_2 = 8060002; //维修

    /******* zhumingwei add 2011-06-28DFS处理状态 ***********/
    public static final Integer RETURN_FLAG = 8061; //DFS处理状态
    public static final Integer RETURN_FLAG_1 = 80611001; //DFS待回复
    public static final Integer RETURN_FLAG_2 = 80611002; //dfs回复取消
    public static final Integer RETURN_FLAG_3 = 80611003; //dfs回复成功
    /******* zhumingwei add 2011-06-28DFS处理状态 ***********/

    /******* zhumingwei add 2011-06-30DFS处理状态 ***********/
    //车辆变更申请类型
    public static final Integer LABOR_CHANGE_TYPE = 8062;
    public static final Integer LABOR_CHANGE_TYPE_01 = 80621001;//单一经销商变更
    public static final Integer LABOR_CHANGE_TYPE_02 = 80621002;//经销商身份变更
    public static final Integer LABOR_CHANGE_TYPE_03 = 80621003;//基地和车型组变更
    /******* zhumingwei add 2011-06-30DFS处理状态 ***********/

    //预测小状态
    public static final Integer FORECAST_SUBSTATUS = 2038;
    public static final Integer FORECAST_SUBSTATUS_1 = 20381001;//大区已上报
    public static final Integer FORECAST_SUBSTATUS_2 = 20381002;//已过预测上报时间，无法提报
    public static final Integer FORECAST_SUBSTATUS_3 = 20381003;//大区已驳回，需重新上报
    public static final Integer FORECAST_SUBSTATUS_4 = 20381004; //总部已驳回，需重新上报
    public static final Integer FORECAST_SUBSTATUS_5 = 20381005; //贵单位需求预测已提报，等待大区审核

    /******* zhumingwei add 2011-07-06加价方式 ***********/
    //车辆变更申请类型
    public static final Integer MONEY_TYPE = 8063;
    public static final Integer MONEY_TYPE_01 = 80631001;//金额加价
    public static final Integer MONEY_TYPE_02 = 80631002;//百分比加价
    /******* zhumingwei add 2011-07-06加价方式 ***********/

    //分组类型
    public static final Integer DIVIDE_GROUP_TYPE = 8080;
    public static final Integer DIVIDE_GROUP_TYPE_DLR_COMPANY = 80801001; // 经销商公司分组

    public static final Integer DIVIDE_CLASS = 8081;
    public static final Integer DIVIDE_CLASS_SALE = 80811001;
    public static final Integer DIVIDE_CLASS_SERVICE = 80811002;

    /*********** 帮助答疑 ******************/
    //问题分类，以下分别是销售问题、售后问题
    public static final Integer QUETION_TYPE = 9090;
    public static final Integer QUETION_TYPE_1 = 90901001;//销售问题
    public static final Integer QUETION_TYPE_2 = 90901002;//售后问题
    //问题状态，以下分别是已保存、已提交、已回答、已取消
    public static final Integer QUETION_STATUS = 9091;
    public static final Integer QUETION_STATUS_1 = 90911001;//已保存
    public static final Integer QUETION_STATUS_2 = 90911002;//已提交
    public static final Integer QUETION_STATUS_3 = 90911003;//已回答
    public static final Integer QUETION_STATUS_4 = 90911004;//已取消
    //操作类型，以下分别是取消、回答变更
    public static final Integer OPERATE_TYPE = 9092;
    public static final Integer OPERATE_TYPE_1 = 90921001;//取消
    public static final Integer OPERATE_TYPE_2 = 90921002;//回答变更

    public static final Integer FLAG_FLEET = 8864;
    public static final Integer FLAG_FLEET_01 = 88641001;//是
    public static final Integer FLAG_FLEET_02 = 88641002;//否


    /************** xiongchuan add 2011-8-19 正负激励 **************************/
    public static final Integer FINE_TYPE = 8064;
    public static final Integer FINE_TYPE_01 = 80641001;//扣款
    public static final Integer FINE_TYPE_02 = 80641002;//奖励

    /*************** xiongchuan addDate 2011-10-10 开票单位变更状态 *************/
    public static final Integer CHANGE_STATUS = 9093;
    public static final Integer CHANGE_STATUS_1 = 90931001;//未提报
    public static final Integer CHANGE_STATUS_2 = 90931002;//已提报
    public static final Integer CHANGE_STATUS_3 = 90931003;//审核通过
    public static final Integer CHANGE_STATUS_4 = 90931004;//审核退回

    /*************** zhumingwei addDate 2011-10-28抽查结算单主表状态 *************/
    public static final Integer CHECK_APP_STATUS = 9098;
    public static final Integer CHECK_APP_STATUS_1 = 90981001;//未签收
    public static final Integer CHECK_APP_STATUS_2 = 90981002;//已签收

    /*************** zhumingwei addDate 2011-10-28抽查结算单明细表状态 *************/
    public static final Integer CHECK_APP_DETAIL_STATUS = 9990;
    public static final Integer CHECK_APP_DETAIL_STATUS_1 = 99901001;//未审核
    public static final Integer CHECK_APP_DETAIL_STATUS_2 = 99901002;//已审核

    /*************** zhumingwei addDate 2011-11-18抽查初始时间 *************/
    public static final String FRIST_TIME = "2011-03-31";

    /*************** xiongchuan addDate 2011-11-24 **************************/
    public static final Integer RESPONSIBILITY_TYPE = 9104;
    public static final Integer RESPONSIBILITY_TYPE_1 = 91041001;//零件自身问题
    public static final Integer RESPONSIBILITY_TYPE_2 = 91041002;//其他零件损坏造成

    /************ yangyuhang addDate 2012-02-06 按揭类型 *****************************/
    public static final Integer MORTGAGE_TYPE = 1389;
    public static final Integer MORTGAGE_TYPE_1 = 13891001;//兵财车贷
    public static final Integer MORTGAGE_TYPE_2 = 13891002;//上汽通用金融车贷通
    public static final Integer MORTGAGE_TYPE_3 = 13891003;//其他
    public static final Integer MORTGAGE_TYPE_4 = 13891004;//银行
    public static final Integer MORTGAGE_TYPE_5 = 13891005;//担保公司
    public static final Integer MORTGAGE_TYPE_6 = 13891006;//重庆汽车金融公司

    /************ yangyuhang addDate 2012-02-20 贷款方式 *****************************/
    public static final Integer Loans = 1390;
    public static final Integer Loans_1 = 13901001;//等额本息
    public static final Integer Loans_2 = 13901002;//等额本金
    public static final Integer Loans_3 = 13901003;//分段式还款
    public static final Integer Loans_4 = 13901004;//其他

    /************ yangyuhang addDate 2012-04-12 入库、出库、报废出库编码 *****************************/
    public static final Integer Exit_scrap = 8888;
    public static final Integer Exit_scrap_1 = 88881001;//入库
    public static final Integer Exit_scrap_2 = 88881002;//出库
    public static final Integer Exit_scrap_3 = 88881003;//报废出库

    /************ yangyuhang addDate 2012-04-12 下端录入方式 *****************************/
    public static final Integer Entry_mode = 8889;
    public static final Integer Entry_mode_1 = 88891001;//手动
    public static final Integer Entry_mode_2 = 88891002;//自动

    public static final Integer upfile_status = 9105;
    public static final Integer upfile_status1 = 91051001;//抽查单据索赔单未上传
    public static final Integer upfile_status2 = 91051002;//抽查单据索赔单已上传
    /************ chenchao addDate 2012-07-05 用户首次登陆是否修改密码 *****************************/
    public static final Integer CHECK_FIRST_LOGIN = 50031001;
    /************ chenchao addDate 2012-07-05 是否启用密码过期验证 *****************************/
    public static final Integer CHECK_PASSWORD_DATE = 50041001;
    /*************** zhumingwei addDate 2012-5-23抽查批次是否上报 *************/
    public static final Integer SB_STATUS = 9991;
    public static final Integer SB_STATUS_1 = 99911001;//未上报
    public static final Integer SB_STATUS_2 = 99911002;//已上报
    public static final Integer SB_STATUS_3 = 99911003;//已完成

    /*************** yangyuhang addDate 2012-8-30出库类型 *************/
    public static final Integer Stock_type = 9992;
    public static final Integer Stock_type_1 = 99921001;//不索赔出库
    public static final Integer Stock_type_2 = 99921002;//二次抵扣出库
    public static final Integer Stock_type_3 = 99921003;//扫描出库
    public static final Integer Stock_type_4 = 99921004;//补录出库

    /*************** yangyuhang addDate 2012-9-4录入类型 *************/
    public static final Integer Entry_type = 9993;
    public static final Integer Entry_type_1 = 99931001;//手动录入
    public static final Integer Entry_type_2 = 99931002;//扫描录入

    /* 系统业务参数维护 */
    //经营情况调查表参数控制
    public static final Integer BUSINESSSURVEY_TYPE = 6001;
    //经营情况调查询日期控制,起始和结束时间
    public static final Integer BUSINESSSURVEY_START_DATE = 6001001;
    public static final Integer BUSINESSSURVEY_END_DATE = 6001002;
    //经营情况调查表提交次数
    public static final Integer BUSINESSSURVEY_SUBMIT_TIMES = 6001003;

    //常规订单超时统计默认单价
    public static final Integer ORDER_TIMEOUT_STATISTICS_PRICE = 6001004;

    //知识库类别@guozhengguang
    public static final Integer KNOW_MANAGE = 9501;
    public static final Integer KNOW_MANAGE_1 = 95011001;//公共知识库
    public static final Integer KNOW_MANAGE_2 = 95011002;//私立知识库
    //知识库状态@guozhengguang
    public static final Integer STATE_MANAGE = 9502;
    public static final Integer STATE_MANAGE_1 = 95021001;//未审核
    public static final Integer STATE_MANAGE_2 = 95021002;//已审核
    //回访管理>> 我的客户回访：当前状态@guozg
    public static final Integer RV_STATUS = 9508;
    public static final Integer RV_STATUS_1 = 95081001;//未回访
    public static final Integer RV_STATUS_2 = 95081002;//继续回访
    public static final Integer RV_STATUS_3 = 95081003;//正在流转--已经去掉
    public static final Integer RV_STATUS_4 = 95081004;//已回访
    public static final Integer RV_STATUS_5 = 95081005;//等待设置问卷
    public static final Integer RV_STATUS_6 = 95081006;//等待指定回访人

    //回访管理>> 我的客户回访：回访结果@guozg
    public static final Integer RD_IS_ACCEPT = 9512;
    public static final Integer RD_IS_ACCEPT_1 = 95121001;//成功回访
    public static final Integer RD_IS_ACCEPT_2 = 95121002;//无人接听
    public static final Integer RD_IS_ACCEPT_3 = 95121003;//拒访
    public static final Integer RD_IS_ACCEPT_4 = 95121004;//空号
    public static final Integer RD_IS_ACCEPT_5 = 95121005;//电话有误
    public static final Integer RD_IS_ACCEPT_6 = 95121006;//无法联系
    //回访管理>> 我的客户回访：处理方式@guozg
    public static final Integer RD_MODE = 9514;
    public static final Integer RD_MODE_1 = 95141001;//继续回访
    public static final Integer RD_MODE_2 = 95141002;//结束回访
    //回访管理>> 我的客户回访：问题类型@guozg
    public static final Integer QD_QUE_TYPE = 9515;
    public static final Integer QD_QUE_TYPE_1 = 95151001;//单选
    public static final Integer QD_QUE_TYPE_2 = 95151002;//多选
    public static final Integer QD_QUE_TYPE_3 = 95151003;//问答
    public static final Integer QD_QUE_TYPE_4 = 95151004;//问答（统计）

    //回访管理>> 我的客户回访：文本框类型@guozg
    public static final Integer qd_txt_type = 9516;
    public static final Integer qd_txt_type_1 = 95161001;//单行
    public static final Integer qd_txt_type_2 = 95161002;//多行
    //回访管理>> 我的客户回访：是否座席管理员@guozg
    public static final Integer se_is_manamger = 9522;
    public static final Integer se_is_manamger_1 = 95221001;//是
    public static final Integer se_is_manamger_2 = 95221002;//否
    //回访管理>> 我的客户回访：问卷管理 问卷类型@guozg
    public static final Integer QR_TYPE = 9525;
    public static final Integer QR_TYPE_1 = 95251001;//有序
    public static final Integer QR_TYPE_2 = 95251002;//无序
    //回访管理>> 我的客户回访：问卷管理问卷状态@guozg
    public static final Integer QR_STATUS = 9526;
    public static final Integer QR_STATUS_1 = 95261001;//有效
    public static final Integer QR_STATUS_2 = 95261002;//无效
    //入库状态
    public static final Integer IN_STATUS = 9700;
    public static final Integer IN_STATUS_01 = 9700001;//可自动入库
    public static final Integer IN_STATUS_02 = 9700002;//不可自动入库
    //出库状态
    public static final Integer OUT_STATUS = 9701;
    public static final Integer OUT_STATUS_01 = 9701001;//可出库
    public static final Integer OUT_STATUS_02 = 9701002;//不可出库
    //库区类型
    public static final Integer RES_TYPE = 9702;
    public static final Integer RES_TYPE_01 = 9702001;//正常区
    public static final Integer RES_TYPE_02 = 9702002;//借出区
    public static final Integer RES_TYPE_03 = 9702003;//质损区
    public static final Integer RES_TYPE_04 = 9702004;//预留区
    public static final Integer RES_TYPE_05 = 9702005;//试制试验区

    //备件品牌
    public static final Integer YIELDLY = 9703;
    public static final Integer YIELDLY_01 = 9703001;//SUV-幻速
    public static final Integer YIELDLY_02 = 9703002;//威望
    public static final Integer YIELDLY_03 = 9703003;//
    //操作类型
    public static final Integer OPERATOR_TYPE = 9704;
    public static final Integer OPERATOR_TYPE_01 = 9704001;//新增道数
    public static final Integer OPERATOR_TYPE_02 = 9704002;//减少道数
    //自动入库状态
    public static final Integer AUTO_IN_STATUS = 9705;
    public static final Integer AUTO_IN_STATUS_01 = 9705001;//正常
    public static final Integer AUTO_IN_STATUS_02 = 9705002;//锁定
    //自动出库状态
    public static final Integer AUTO_OUT_STATUS = 9706;
    public static final Integer AUTO_OUT_STATUS_01 = 9706001;//正常
    public static final Integer AUTO_OUT_STATUS_02 = 9706002;//锁定

    //统计类型
    public static final Integer STA_TYPE = 9719;
    public static final Integer STA_TYPE_01 = 97191001;//配车及时率
    public static final Integer STA_TYPE_02 = 97191002;//发运及时率
    public static final Integer STA_TYPE_03 = 97191003;//综合统计

    //统计类型(入库跟出库)
    public static final Integer INOUT_TYPE = 9720;
    public static final Integer INOUT_TYPE_01 = 97201001;//入库
    public static final Integer INOUT_TYPE_02 = 97201002;//出库

    //座席级别 wangming
    public static final Integer SEATS_LEVEL = 9503;
    public static final Integer SENIOR_CONSULTANT = 95031001;//高级咨询师
    public static final Integer CONSULTANT = 95031002;//普通咨询师
    public static final Integer SUPERVISORY_PERSONNEL = 95031003;//监督人员
    public static final Integer NOT_SEAT = 95031004;//非座席
    public static final Integer CONSULTANT_EXPERT = 95031005;//咨询专家

    //类型管理 所属类型 wangming
    public static final String TYPE_TOP = "1";//类型  
    public static final String TYPE_COMPLAIN = "9505";//投诉
    public static final String TYPE_COMPLAIN_NAME = "投诉";
    public static final String TYPE_CONSULT = "9506";//咨询
    public static final String TYPE_CONSULT_NAME = "咨询";
    public static final String TYPE_RETURN_VISIT = "9507";//回访
    public static final String TYPE_RETURN_VISIT1 = "95071001";//1333抽查回访
    public static final String TYPE_RETURN_VISIT2 = "95071002";//客户关怀回访
    public static final String TYPE_RETURN_VISIT3 = "95071003";//市场调查回访
    public static final String TYPE_RETURN_VISIT4 = "95071004";//服务站满意度回访
    public static final String TYPE_RETURN_VISIT_NAME = "回访";

    public static final String TYPE_tow_activity = "1988";//服务活动类型二级
    public static final String TYPE_tow_activity1 = "19881001";//全国性客户关怀
    public static final String TYPE_tow_activity2 = "19881002";//区域性客户关怀
    public static final String TYPE_tow_activity3 = "19881003";//经销商客户关怀
    public static final String TYPE_tow_activity4 = "19881004";//集团客户关怀

    /*************** bianlz addDate 2013-4-2配件主数据维护 *************/
    //修改为同以前一致
    public static final Integer PART_BASE_FLAG = 1004;//配件中所有是否
    public static final Integer PART_BASE_FLAG_YES = 10041001;//配件中所有是
    public static final Integer PART_BASE_FLAG_NO = 10041002;//配件中所有否

    public static final Integer ACTIVITY_PKG  = 9416;//活动套餐
    public static final Integer ACTIVITY_PKG_A = 94161001;//A
    public static final Integer ACTIVITY_PKG_B = 94161002;//B
    public static final Integer ACTIVITY_PKG_C = 94161003;//C
    public static final Integer ACTIVITY_PKG_D = 94161004;//D
    public static final Integer ACTIVITY_PKG_E = 94161005;//E
    public static final Integer ACTIVITY_PKG_F = 94161006;//F
    public static final Integer ACTIVITY_PKG_G = 94161007;//G

    public static final Integer BANCENT_INVOICE  = 9415;//开票单号使用情况
    public static final Integer BANCENT_INVOICE_01 = 94151001;//使用
    public static final Integer BANCENT_INVOICE_02 = 94151002;//未使用
 
    public static final Integer PART_BASE_PART_TYPES = 9202;//配件主数据维护中配件属性
    public static final Integer PART_BASE_PART_TYPES_SELF_MADE = 92021001;//配件主数据维护中配件属性-自制件
    public static final Integer PART_BASE_PART_TYPES_PURCHASE = 92021002;//配件主数据维护中配件属性-国产件
    public static final Integer PART_BASE_PART_TYPES_ENTRANCE = 92021003;//配件主数据维护中配件属性-进口件

    public static final Integer PART_BASE_MATERIAL = 9203;//配件主数据维护中配件材料
    public static final Integer PART_BASE_MATERIAL_GLASS = 92031001;//配件主数据维护中配件材料-玻璃件
    public static final Integer PART_BASE_MATERIAL_plastic = 92031002;//配件主数据维护中配件材料-塑料件
    public static final Integer PART_BASE_MATERIAL_METAL = 92031003;//配件主数据维护中配件材料-金属件
    public static final Integer PART_BASE_MATERIAL_PLATE = 92031004;//配件主数据维护中配件材料-钣金件
    public static final Integer PART_BASE_MATERIAL_RUBBER = 92031005;//配件主数据维护中配件材料-橡胶件
    public static final Integer PART_BASE_MATERIAL_OIL = 92031006;//配件主数据维护中配件材料-油品
    public static final Integer PART_BASE_MATERIAL_OTHER = 92031007;//配件主数据维护中配件材料-其他件

    public static final Integer PART_BASE_MOBILITY = 9274;//配件主数据维护中配件流动性
    public static final Integer PART_BASE_MOBILITY_01 = 92741001;//配件主数据维护中配件材料流动性-1
    public static final Integer PART_BASE_MOBILITY_02 = 92741002;//配件主数据维护中配件材料流动性-2
    public static final Integer PART_BASE_MOBILITY_03 = 92741003;//配件主数据维护中配件材料流动性-3
    public static final Integer PART_BASE_MOBILITY_04 = 92741004;//配件主数据维护中配件材料流动性-4
    public static final Integer PART_BASE_MOBILITY_05 = 92741005;//配件主数据维护中配件材料流动性-5
    public static final Integer PART_BASE_MOBILITY_06 = 92741006;//配件主数据维护中配件材料流动性-0

    public static final Integer PART_BASE_UOM = 9205;//配件主数据维护中配件单位
    public static final Integer PART_BASE_UOM_INDIVIDUAL = 92051001;//配件主数据维护中配件材料-个
    public static final Integer PART_BASE_UOM_PACKAGE = 92051002;//配件主数据维护中配件材料-包

    public static final Integer PART_BASE_BUY_STATE = 9206;//配件主数据维护中配件采购状态
    public static final Integer PART_BASE_BUY_STATE_K = 92061001;//配件主数据维护中配件采购状态-K
    public static final Integer PART_BASE_BUY_STATE_M = 92061002;//配件主数据维护中配件采购状态-M
    public static final Integer PART_BASE_BUY_STATE_S = 92061003;//配件主数据维护中配件采购状态-S

    public static final Integer PART_BASE_POSITION = 9207;//配件主数据维护中配件结构状态
    public static final Integer PART_BASE_POSITION_ENGINE1 = 92071001;//发动机
    public static final Integer PART_BASE_POSITION_ENGINE2 = 92071002;//油液
    public static final Integer PART_BASE_POSITION_ENGINE3 = 92071003;//底盘
    public static final Integer PART_BASE_POSITION_ENGINE4 = 92071004;//车身
    public static final Integer PART_BASE_POSITION_ENGINE5 = 92071005;//附件
    public static final Integer PART_BASE_POSITION_ENGINE6 = 92071006;//电器

    //供应商国内/外
    public static final Integer IS_ABROAD_V = 9220;
    public static final Integer PARTVENDER_NO = 92201002;//国内
    public static final Integer PARTVENDER_YES = 92201001;//国外
    //供应商类型
    public static final Integer VENDER_TYPE = 9221;
    public static final Integer PARTVENDER_INNER = 92211002;//内部
    public static final Integer PARTVENDER_OUTER = 92211001;//外部

    //制造商国内/外
    public static final Integer IS_ABROAD_M = 9222;
    public static final Integer PARTMAKER_NO = 92221002;//国内
    public static final Integer PARTMAKER_YES = 92221001;//国外

    //制造商类型
    public static final Integer PARTMAKER_TYPE = 9223;
    public static final Integer PARTMAKER_INNER = 92231002;//内部
    public static final Integer PARTMAKER_OUTER = 92231001;//外部

    //配件采购价格是否暂估
    public static final Integer IS_GUARD = 9224;
    public static final Integer IS_GUARD_NO = 92241001;//否
    public static final Integer IS_GUARD_YES = 92241002;//是

    //配件类型维护
    public static final Integer FIXCODE_TYPE = 9225;
    public static final Integer FIXCODE_TYPE_01 = 92251001;//人员类型
    public static final Integer FIXCODE_TYPE_02 = 92251002;//计量类型
    public static final Integer FIXCODE_TYPE_03 = 92251003;//包装类型
    public static final Integer FIXCODE_TYPE_04 = 92251004;//发运类型
    public static final Integer FIXCODE_TYPE_05 = 92251005;//服务商类型
    public static final Integer FIXCODE_TYPE_06 = 92251006;//运单类型
    public static final Integer FIXCODE_TYPE_07 = 92251007;//装箱人
    public static final Integer FIXCODE_TYPE_08 = 92251008;//承运物流
    public static final Integer FIXCODE_TYPE_09 = 92251009;//预留1 -- 品牌
    public static final Integer FIXCODE_TYPE_10 = 92251010;//预留2 -- 适用车型
    public static final Integer FIXCODE_TYPE_11 = 92251011;//预留3 -- 适用车系
    public static final Integer FIXCODE_TYPE_12 = 92251012;//预留4

    //配件资金类型
    public static final Integer FIXCODE_CURRENCY = 9226;
    public static final Integer FIXCODE_CURRENCY_01 = 92261001;//现款
    public static final Integer FIXCODE_CURRENCY_02 = 92261002;//其他

    //配件退货单据类型
    public static final Integer RETURN_TYPE = 9228;
    public static final Integer RETURN_TYPE_01 = 92281001;//已保存
    public static final Integer RETURN_TYPE_02 = 92281002;//已提交
    public static final Integer RETURN_TYPE_03 = 92281003;//审核已通过
    public static final Integer RETURN_TYPE_04 = 92281004;//审核已驳回
    public static final Integer RETURN_TYPE_05 = 92281005;//已完成
    public static final Integer RETURN_TYPE_06 = 92281006;//已作废

    //服务商资金导入 类型
    public static final Integer FIXCODE_IMPORT_TYPE = 60011001;
    public static final String FIXCODE_BALANCE_IMPORT = "0"; //余额导入
    public static final String FIXCODE_CHANGE_BALANCE_IMPORT = "1"; //发生额导入

    //账户往来类型
    public static final String FIXCODE_ACCOUNT_TYPE_01 = "0"; //打款
    public static final String FIXCODE_ACCOUNT_TYPE_02 = "1"; //扣款

    //服务商资金占用明细 -> CHANGE_TYPE
    public static final Integer DEALER_AMOUNT_CHANGE_TYPE_PREEMPTION = 1; //预占
    public static final Integer DEALER_AMOUNT_CHANGE_TYPE_RELEASED = 2; //释放

    //来电类型 wangming
    public static final String CALL_TYPE = "9509";
    public static final String UNCALLING_TYPE = "95091001";//未接通来电
    public static final String CALLING_TYPE = "95091002";//已接通来电

    //呼入类型 wangming
    public static final String CALL_IN_OUT_TYPE = "9510";
    public static final String CALL_IN_TYPE = "95101001";//呼入
    public static final String CALL_OUT_TYPE = "95101002";//呼出

    //评分 wangming
    public static final String GRADE_TYPE = "9511";
    public static final String PLEASED = "95111001";//满意
    public static final String GENERAL = "95111002";//一般
    public static final String YAWP = "95111003";//不满意

    //坐席状态 wangming
    public static final Integer SEAT_TYPE = 9513;
    public static final Integer SEAT_UNLOGIN_TYPE = 95131001;//坐席未登录
    public static final Integer SEAT_FREE_TYPE = 95131002;//坐席空闲
    public static final Integer SEAT_INCOMING_TYPE = 95131003;//坐席有来电
    public static final Integer SEAT_CALLING_TYPE = 95131004;//坐席处于受话状态
    public static final Integer SEAT_BUSY_TYPE = 95131005;//坐席被置忙

    //分机号
    public static final Integer SEAT_EXT = 9992;
    /*************** bianlz addDate 2013-4-7配件销售价格列表设置 *************/
    public static final Integer PART_SALE_PRICE_START_FLAG = 9208;//配件销售价格列表设置是否启用
    public static final Integer PART_SALE_PRICE_START = 92081001;//配件销售价格列表设置-启用
    public static final Integer PART_SALE_PRICE_STOP = 92081002;//配件销售价格列表设置-停用

    public static final Integer PART_SALE_PRICE_SCOPE_TYPE = 9209;//配件销售价格列表设置价格范围
    public static final Integer PART_SALE_PRICE_SCOPE_TYPE_01 = 92091001;//配件销售价格列表设置-全国统一价
    public static final Integer PART_SALE_PRICE_SCOPE_TYPE_02 = 92091002;//配件销售价格列表设置-服务商类型
    public static final Integer PART_SALE_PRICE_SCOPE_TYPE_03 = 92091003;//配件销售价格列表设置-指定经销商

    public static final Integer PART_SALE_PRICE_DEALER_TYPE = 9210;//配件销售价格列表设置服务商类型
    public static final Integer PART_SALE_PRICE_DEALER_TYPE_01 = 92101001;//配件销售价格列表设置-供应中心
    public static final Integer PART_SALE_PRICE_DEALER_TYPE_02 = 92101002;//配件销售价格列表设置-一级服务商
    public static final Integer PART_SALE_PRICE_DEALER_TYPE_03 = 92101003;//配件销售价格列表设置-二级服务商
    public static final Integer PART_SALE_PRICE_DEALER_TYPE_04 = 92101003;//配件销售价格列表设置-三级服务商
    public static final Integer PART_SALE_PRICE_DEALER_TYPE_05 = 92101003;//配件销售价格列表设置-四级服务商
    public static final Integer PART_SALE_PRICE_DEALER_TYPE_06 = 92101003;//配件销售价格列表设置-其他

    //人员状态
    public static final Integer PERSON_STATUS = 9707;
    public static final Integer PERSON_STATUS_01 = 9707001;//在职
    public static final Integer PERSON_STATUS_02 = 9707002;//离职

    //发运计划自动审核参数
    public static final Integer AUTO_AUDITOR = 9708001;//自动审核
    public static final Integer LAB_AUDITOR = 9708002;//人工审核

    //发运分派管理 自动分派备注信息
    public static final String SEND_REMARK = "自动分派发运单";//自动分派

    //组板状态
    public static final Integer BO_STATUS = 9709;
    public static final Integer BO_STATUS01 = 9709001;//未组板
    public static final Integer BO_STATUS02 = 9709002;//组板中
    public static final Integer BO_STATUS03 = 9709003;//完全组板
    //处理状态
    public static final Integer HANDLE_STATUS = 9710;
    public static final Integer HANDLE_STATUS10 = 9710010;//分派中
    public static final Integer HANDLE_STATUS01 = 9710001;//未配车
    public static final Integer HANDLE_STATUS02 = 9710002;//部分配车
    public static final Integer HANDLE_STATUS03 = 9710003;//完全配车（未验出库状态）
    public static final Integer HANDLE_STATUS04 = 9710004;//部分出库
    public static final Integer HANDLE_STATUS05 = 9710005;//完全出库
    public static final Integer HANDLE_STATUS06 = 9710006;//已生成运单（未验收状态）
    public static final Integer HANDLE_STATUS07 = 9710007;//部分验收
    public static final Integer HANDLE_STATUS08 = 9710008;//完全验收
    public static final Integer HANDLE_STATUS09 = 9710009;//部分生成运单
    public static final Integer HANDLE_STATUS11 = 9710011;//计划已发送
    //运单状态
    public static final String SEND_STATUS = "9711";
    public static final String SEND_STATUS_01 = "97111001";  //待回厂
    public static final String SEND_STATUS_02 = "97111002";  //已回厂
    public static final String SEND_STATUS_03 = "97111003";  //已结算
    public static final String SEND_STATUS_04 = "97111004";  //待回厂之前【待发运】

    //退库类型
    public static final Integer REC_TYPE = 9717;
    public static final Integer REC_TYPE01 = 9717001;//换下车
    public static final Integer REC_TYPE02 = 9717002;//实销退车

    /*************** bianlz addDate 2013-4-9采购计划 *************/
    public static final Integer PART_PURCHASE_PLAN_TYPE = 9211;//采购计划类型
    public static final Integer PART_PURCHASE_PLAN_TYPE_01 = 92111001;//采购计划类型-月度计划
    public static final Integer PART_PURCHASE_PLAN_TYPE_02 = 92111002;//采购计划类型-紧急计划
    public static final Integer PART_PURCHASE_PLAN_TYPE_03 = 92111111;//采购计划类型-领用计划 (值不插入到tc_code 表)
    public static final Integer PART_PURCHASE_PLAN_TYPE_04 = 92111003;//采购计划类型-周计划
    

    public static final Integer PART_PURCHASE_PLAN_CREATE_TYPE = 9212;//采购计划生成类型
    public static final Integer PART_PURCHASE_PLAN_CREATE_TYPE_01 = 92121001;//采购计划生成类型-数据导入
    public static final Integer PART_PURCHASE_PLAN_CREATE_TYPE_02 = 92121002;//采购计划生成类型-人工录入
    public static final Integer PART_PURCHASE_PLAN_CREATE_TYPE_03 = 92121003;//采购计划生成类型-自动生成

    public static final Integer PART_PURCHASE_PLAN_CHECK_STATUS = 9213;//采购计划上报，审核，通过， 驳回

    public static final Integer PART_PURCHASE_PLAN_CHECK_STATUS_01 = 92131001;//采购计划已保存
    public static final Integer PART_PURCHASE_PLAN_CHECK_STATUS_02 = 92131002;//采购计划已提交
    public static final Integer PART_PURCHASE_PLAN_CHECK_STATUS_03 = 92131003;//采购计划审批通过
    public static final Integer PART_PURCHASE_PLAN_CHECK_STATUS_04 = 92131004;//采购计划审批驳回
    public static final Integer PART_PURCHASE_PLAN_CHECK_STATUS_05 = 92131005;//采购计划确认通过
    public static final Integer PART_PURCHASE_PLAN_CHECK_STATUS_06 = 92131006;//采购计划确认驳回
    public static final Integer PART_PURCHASE_PLAN_CHECK_STATUS_07 = 92131007;//采购计划已作废

    /*************************** 财务转账 ************************/
    public static final Integer TRANSFER_STATUS = 1880; //财务转账状态
    public static final Integer TRANSFER_STATUS_01 = 18801001; //财务保存
    public static final Integer TRANSFER_STATUS_02 = 18801002; //财务提交
    public static final Integer TRANSFER_STATUS_03 = 18801003; //财务审核通过
    public static final Integer TRANSFER_STATUS_04 = 18801004; //财务审核保存
    public static final Integer TRANSFER_STATUS_05 = 18801005; //财务作废

    //库位表车辆ID默认值
    public static final Integer SIT_VELID = -1;//默认值
    // 投诉处理状态
    public static final Integer COMPLAINT_PROCESS = 9517; // 投诉处理状态
    public static final Integer COMPLAINT_PROCESS_TURN = 95171001; //转其他部门处理
    // 咨询处理状态
    public static final Integer CONSULT_PROCESS = 9528;
    public static final Integer CONSULT_PROCESS_SPOT = 95281001; //现场处理
    public static final Integer CONSULT_PROCESS_WAIT = 95281002; //待处理
    public static final Integer CONSULT_PROCESS_FINISH = 95281003; //已处理

    //工单配件责任性质
    public static final Integer RESPONS_NATURE_STATUS = 9400;//责任性质
    public static final Integer RESPONS_NATURE_STATUS_01 = 94001001;//零件自身问题
    public static final Integer RESPONS_NATURE_STATUS_02 = 94001002;//其他零件损坏

    //配件单据编码和表对应关系
    public static final Integer PART_CODE_RELATION = 9229;
    public static final Integer PART_CODE_RELATION_01 = 92291001;//总部计划单
    public static final Integer PART_CODE_RELATION_02 = 92291002;//总部采购单
    public static final Integer PART_CODE_RELATION_03 = 92291003;//总部验收单
    public static final Integer PART_CODE_RELATION_04 = 92291004;//总部入库单
    public static final Integer PART_CODE_RELATION_05 = 92291005;//总部结算单
    public static final Integer PART_CODE_RELATION_06 = 92291006;//DLR采购订单
    public static final Integer PART_CODE_RELATION_07 = 92291007;//总部销售单
    public static final Integer PART_CODE_RELATION_08 = 92291008;//总部装箱单
    public static final Integer PART_CODE_RELATION_09 = 92291009;//总部出库单
    public static final Integer PART_CODE_RELATION_10 = 92291010;//总部发运单
    public static final Integer PART_CODE_RELATION_11 = 92291011;//供应中心销售单
    public static final Integer PART_CODE_RELATION_12 = 92291012;//供应中心出库单
    public static final Integer PART_CODE_RELATION_13 = 92291013;//供应中心发运单
    public static final Integer PART_CODE_RELATION_14 = 92291014;//零售OR领用单
    public static final Integer PART_CODE_RELATION_15 = 92291015;//调拨单
    public static final Integer PART_CODE_RELATION_16 = 92291016;//销售退货申请单
    public static final Integer PART_CODE_RELATION_17 = 92291017;//销售退货单
    public static final Integer PART_CODE_RELATION_18 = 92291018;//采购退货单
    public static final Integer PART_CODE_RELATION_19 = 92291019;//库存状态调整单（来货错误、质量问题、借条通用）
    public static final Integer PART_CODE_RELATION_20 = 92291020;//现场BO
    public static final Integer PART_CODE_RELATION_21 = 92291021;//一般BO
    public static final Integer PART_CODE_RELATION_22 = 92291022;//库存盘点单
    public static final Integer PART_CODE_RELATION_23 = 92291023;//
    public static final Integer PART_CODE_RELATION_24 = 92291024;//库存盘点结果单
    public static final Integer PART_CODE_RELATION_25 = 92291025;//库存状态调整单
    public static final Integer PART_CODE_RELATION_26 = 92291026;//入库单
    public static final Integer PART_CODE_RELATION_27 = 92291027;//配件拆合件申请(合件)
    public static final Integer PART_CODE_RELATION_28 = 92291028;//库存状态调整单
    public static final Integer PART_CODE_RELATION_29 = 92291029;//销售退货出库单
    public static final Integer PART_CODE_RELATION_30 = 92291030;//销售退货入库单
    public static final Integer PART_CODE_RELATION_31 = 92291031;//杂项入库单
    public static final Integer PART_CODE_RELATION_32 = 92291032;//订单验收指令
    public static final Integer PART_CODE_RELATION_33 = 92291033;//直发
    public static final Integer PART_CODE_RELATION_34 = 92291034;//计划
    public static final Integer PART_CODE_RELATION_35 = 92291035;//促销
    public static final Integer PART_CODE_RELATION_36 = 92291036;//市场升级
    public static final Integer PART_CODE_RELATION_37 = 92291037;//内部领用订单
    public static final Integer PART_CODE_RELATION_38 = 92291038;//领用单
    public static final Integer PART_CODE_RELATION_39 = 92291039;//总部计划领件单
    public static final Integer PART_CODE_RELATION_40 = 92291040;//广宣品发运计划单
    public static final Integer PART_CODE_RELATION_41 = 92291041;//切换订单
    public static final Integer PART_CODE_RELATION_42 = 92291042;//配件赠品
    public static final Integer PART_CODE_RELATION_43 = 92291043;//接收入库--ERP(入库)
    public static final Integer PART_CODE_RELATION_44 = 92291044;//接收入库--ERP(出库)
    public static final Integer PART_CODE_RELATION_45 = 92291045;//杂项出库单
    public static final Integer PART_CODE_RELATION_46 = 92291046;//配件货位移位出库
    public static final Integer PART_CODE_RELATION_47 = 92291047;//配件货位移位入库
    public static final Integer PART_CODE_RELATION_48 = 92291048;//配件发运计划单
    public static final Integer PART_CODE_RELATION_49 = 92291049;//三包急件订单
    public static final Integer PART_CODE_RELATION_50 = 92291050;//替换件验收数量不足自动生成常规订单
    public static final Integer PART_CODE_RELATION_51 = 92291051;//切换订单审核后2个月无旧件返回自动生成常规订单
    public static final Integer PART_CODE_RELATION_52 = 92291052;//切换订单审核自动生成销售单
    public static final Integer PART_CODE_RELATION_53 = 92291053;//配件价格变更申请
    public static final Integer PART_CODE_RELATION_54 = 92291054;//配件移位&配件状态转换
    public static final Integer PART_CODE_RELATION_55 = 92291055;//接收入库
    public static final Integer PART_CODE_RELATION_56 = 92291056;//切换订单验收入库扣减服务商库存
    public static final Integer PART_CODE_RELATION_57 = 92291057;//移库
    public static final Integer PART_CODE_RELATION_58 = 92291058;//移库审核
    public static final Integer PART_CODE_RELATION_59 = 92291059;//售后质保手册
    public static final Integer PART_CODE_RELATION_70 = 92291070;//销售采购订单
    public static final Integer PART_CODE_RELATION_62 = 92291062;//库存调整

    //
    public static final Integer PART_CODE_RELATION_66 = 92291066;//在库成本调整申请单
    
    Integer PART_CODE_RELATION_60 = 92291060;//滚动计划单
    Integer PART_CODE_RELATION_61 = 92291061;//滚动计划补充单
    
    public static final Integer PART_CODE_RELATION_83 = 92291083;//用品入库用方便锁定备件
    public static final Integer PART_CODE_RELATION_91 = 92291091;//退换货解封单

    //延期状态
    public static final Integer DELAY_STATUS = 9518;
    public static final Integer NOT_APPLY = 95181001;//未申请
    public static final Integer ALREADY_APPLY = 95181002;//已申请
    public static final Integer PASS_APPLY = 95181003;//已通过
    public static final Integer REJECT_APPLY = 95181004;//已驳回
    public static final Integer PASS_APPLY_TO_ADMIN = 95181005;//已通过交给管理员审核
    public static final Integer PASS_Manager_01 = 95181006;//室主任审核通过
    public static final Integer PASS_Manager_02 = 95181007;//处长审核通过
    public static final Integer PASS_Manager_03 = 95181008;//副总审核通过

    //审核级别
    public static final Integer Level_Manager = 9552;//
    public static final Integer Level_Manager_01 = 95521001;//室主任审核
    public static final Integer Level_Manager_02 = 95521002;//处长审核
    public static final Integer Level_Manager_03 = 95521003;//副总审核

    //延期类型
    public static final Integer DEFER_TYPE = 9408;
    public static final Integer DEFER_TYPE_01 = 94081001;//延期时间
    public static final Integer DEFER_TYPE_02 = 94081002;//强制关闭
    //冲减方式
    public static final Integer WRITE_DOWNS_WAY_STATUS = 9750;//冲减方式
    public static final Integer WRITE_DOWNS_WAY_STATUS_01 = 97501001;//前冲
    public static final Integer WRITE_DOWNS_WAY_STATUS_02 = 97501002;//后冲

    //返利状态
    public static final Integer REBATE_STATUS = 9930;//返利状态
    public static final Integer REBATE_STATUS_01 = 99301001;//已保存
    public static final Integer REBATE_STATUS_02 = 99301002;//已提交
    public static final Integer REBATE_STATUS_03 = 99301003;//审核通过
    public static final Integer REBATE_STATUS_04 = 99301004;//审核驳回
    public static final Integer REBATE_STATUS_05 = 99301005;//作废

    //返利科目
    public static final Integer REBATE_TYPES = 9940;//返利科目
    public static final Integer REBATE_TYPES_01 = 99401001;//月度
    public static final Integer REBATE_TYPES_02 = 99401002;//季度
    public static final Integer REBATE_TYPES_03 = 99401003;//年度
    public static final Integer REBATE_TYPES_04 = 99401004;//贴息

    //里程范围
    public static final Integer MILEAGE_RANGE = 9519;//里程范围
    public static final Integer MILEAGE_RANGE_1 = 95191001;//5000KM以内
    public static final Integer MILEAGE_RANGE_2 = 95191002;//5000KM-10000KM
    public static final Integer MILEAGE_RANGE_3 = 95191003;//10000KM-15000KM
    public static final Integer MILEAGE_RANGE_4 = 95191004;//15000KM-20000KM
    public static final Integer MILEAGE_RANGE_5 = 95191005;//20000KM-25000KM
    public static final Integer MILEAGE_RANGE_6 = 95191006;//25000KM-30000KM
    public static final Integer MILEAGE_RANGE_7 = 95191007;//30000KM以上

    //故障部件
    public static final Integer FAULT_PART = 9520;//故障部件
    public static final Integer FAULT_PART_01 = 95201001;//车身
    public static final Integer FAULT_PART_02 = 95201002;//底盘
    public static final Integer FAULT_PART_03 = 95201003;//电器

    public static final String DB_SAME = "SAME";
    public static final String DB_NO_SAME = "NO_SAME";

    //采购订单管理
    public static final Integer PART_PURCHASE_ORDERCHK_STATUS = 9227;
    public static final Integer PART_PURCHASE_ORDERCHK_STATUS_01 = 92271001;//待确认
    public static final Integer PART_PURCHASE_ORDERCHK_STATUS_02 = 92271002;//已关闭
    public static final Integer PART_PURCHASE_ORDERCHK_STATUS_03 = 92271003;//已完成
    public static final Integer PART_PURCHASE_ORDERIN_STATUS = 9230;
    public static final Integer PART_PURCHASE_ORDERCIN_STATUS_01 = 92301001;//待入库
    public static final Integer PART_PURCHASE_ORDERCIN_STATUS_02 = 92301002;//入库完成
    public static final Integer PART_PURCHASE_ORDERCIN_STATUS_03 = 92301003;//部分入库
    public static final Integer PART_PURCHASE_ORDERCIN_TYPE = 9231;//入库单类型
    public static final Integer PART_PURCHASE_ORDERCIN_TYPE_01 = 92311001;//一般入库
    public static final Integer PART_PURCHASE_ORDERCIN_TYPE_02 = 92311002;//直发虚拟入库
    public static final Integer PART_PURCHASE_ORDERBALANCE_STATUS = 9232;
    public static final Integer PART_PURCHASE_ORDERBALANCE_STATUS_01 = 92321001;//待结算
    public static final Integer PART_PURCHASE_ORDERBALANCE_STATUS_02 = 92321002;//结算中
    public static final Integer PART_PURCHASE_ORDERBALANCE_STATUS_03 = 92321003;//财务已确认
    public static final Integer PART_PURCHASE_ORDERBALANCE_STATUS_04 = 92321004;//已提交
    public static final Integer PART_PURCHASE_ORDERBALANCE_STATUS_05 = 92321005;//已作废
    public static final Integer PART_PURCHASE_ORDERBALANCE_STATUS_06 = 92321006;//已驳回

    public static final Double PART_TAX_RATE = 0.17;//税率

    // 配件库存状态变更 -> 业务类型Nd
    public static final Integer PART_STOCK_STATUS_BUSINESS_TYPE = 9233;
    public static final Integer PART_STOCK_STATUS_BUSINESS_TYPE_01 = 92331001; //正常
    public static final Integer PART_STOCK_STATUS_BUSINESS_TYPE_02 = 92331002; //盘盈
    public static final Integer PART_STOCK_STATUS_BUSINESS_TYPE_03 = 92331003; //盘亏
    public static final Integer PART_STOCK_STATUS_BUSINESS_TYPE_04 = 92331004; //来货错误
    public static final Integer PART_STOCK_STATUS_BUSINESS_TYPE_05 = 92331005; //质量问题
    public static final Integer PART_STOCK_STATUS_BUSINESS_TYPE_06 = 92331006; //借条
    public static final Integer PART_STOCK_STATUS_BUSINESS_TYPE_07 = 92331007; //现场BO

    // 配件库存状态变更 -> 调整类型
    public static final Integer PART_STOCK_STATUS_CHANGE_TYPE = 9234;
    public static final Integer PART_STOCK_STATUS_CHANGE_TYPE_01 = 92341001; //封存
    public static final Integer PART_STOCK_STATUS_CHANGE_TYPE_02 = 92341002; //解封

    //配件库存状态变更 上传文件最大行数
    public static final Integer PART_STOCK_STATUS_CHANGE_MAX_LINENUM = 10000;

    //配件零售领用管理 出库类型
    public static final Integer PART_SALE_STOCK_REMOVAL_TYPE = 9242;
    public static final Integer PART_SALE_STOCK_REMOVAL_TYPE_01 = 92421001;//零售
    public static final Integer PART_SALE_STOCK_REMOVAL_TYPE_02 = 92421002;//领用

    //配件零售领用管理 订单类型
    public static final Integer PART_RESALE_RECEIVE_ORDER_TYPE = 9243;
    public static final Integer PART_RESALE_RECEIVE_ORDER_TYPE_01 = 92431001;//已保存
    public static final Integer PART_RESALE_RECEIVE_ORDER_TYPE_02 = 92431002;//已提交
    public static final Integer PART_RESALE_RECEIVE_ORDER_TYPE_03 = 92431003;//已完成
    public static final Integer PART_RESALE_RECEIVE_ORDER_TYPE_04 = 92431004;//已通过
    public static final Integer PART_RESALE_RECEIVE_ORDER_TYPE_05 = 92431005;//已驳回
    public static final Integer PART_RESALE_RECEIVE_ORDER_TYPE_06 = 92431006;//已作废

    //库存盘点单据状态
    public static final Integer PART_INVE_ORDER_STATE = 9251;
    public static final Integer PART_INVE_ORDER_STATE_01 = 92511001;//已保存
    public static final Integer PART_INVE_ORDER_STATE_02 = 92511002;//审核中
    public static final Integer PART_INVE_ORDER_STATE_03 = 92511003;//处理中
    public static final Integer PART_INVE_ORDER_STATE_04 = 92511004;//已驳回
    public static final Integer PART_INVE_ORDER_STATE_05 = 92511005;//已关闭
    public static final Integer PART_INVE_ORDER_STATE_06 = 92511006;//已完成
    public static final Integer PART_INVE_ORDER_STATE_07 = 92511007;//已作废

    //库存盘点审核结果 （暂时不启用）
    public static final Integer PART_INVE_CHECK_RESULT = 9252;
    public static final Integer PART_INVE_CHECK_RESULT_01 = 92521001;//通过
    public static final Integer PART_INVE_CHECK_RESULT_02 = 92521002;//驳回

    //库存盘点处理方式
    public static final Integer PART_INVE_RESOLVE_TYPE = 9253;
    public static final Integer PART_INVE_RESOLVE_TYPE_01 = 92531001;//封存
    public static final Integer PART_INVE_RESOLVE_TYPE_02 = 92531002;//盈亏出入库

    //配件出入库记录FIXCODE
//    public static final String PART_RECORD_BATCH = "1306";
    //供应商
    public static final String PART_RECORD_VENDER_ID = "21799";

    //配件库存盘点类型
    public static final Integer PART_STOCK_INVE_TYPE = 9246;
    public static final Integer PART_STOCK_INVE_TYPE_01 = 92461001;//全部
    public static final Integer PART_STOCK_INVE_TYPE_02 = 92461002;//动态

    //配件杂项出库类型
    public static final Integer MISC_EX_TYPE = 9288;
    public static final Integer MISC_EX_TYPE_01 = 92881001;//研发部门领用
    public static final Integer MISC_EX_TYPE_02 = 92881002;//管理部门领用
    public static final Integer MISC_EX_TYPE_03 = 92881003;//生产部门领用
    public static final Integer MISC_EX_TYPE_04 = 92881004;//杂发
    public static final Integer MISC_EX_TYPE_05 = 92881005;//其它


    //配件杂项入库类型
    public static final Integer MISC_EI_TYPE = 9289;
    public static final Integer MISC_EI_TYPE_01 = 92891001;//出库类型01
    public static final Integer MISC_EI_TYPE_02 = 92891002;//出库类型02
    public static final Integer MISC_EI_TYPE_03 = 92891003;//出库类型03
    public static final Integer MISC_EI_TYPE_04 = 92891004;//出库类型04
    public static final Integer MISC_EI_TYPE_05 = 92891005;//出库类型05

    //配件库存盘点状态
    public static final Integer PART_STOCK_INVE_STATE = 9247;
    public static final Integer PART_STOCK_INVE_STATE_01 = 92471001;//已保存
    public static final Integer PART_STOCK_INVE_STATE_02 = 92471002;//盘点中
    public static final Integer PART_STOCK_INVE_STATE_03 = 92471003;//已盘点

    //导出金税文本状态
    public static final Integer PART_INVO_OUT_STATE = 9260;
    public static final Integer PART_INVO_OUT_STATE_01 = 92601001;//未导出
    public static final Integer PART_INVO_OUT_STATE_02 = 92601002;//已导出

    //配件是否锁定状态
    public static final Integer PART_STATE_UN_LOCKED = 0;//未锁定
    public static final Integer PART_STATE_LOCKED = 1;//已锁定

    //盘点单是否可用状态
    public static final Integer PART_STATUS_EN_ABLE = 1;//盘点单可用
    public static final Integer PART_STATUS_UN_ABLE = 2;//盘点单不可用

    //抱怨级别
    public static final Integer COMPLAINT_LEVEL = 9523;
    public static final Integer COMPLAINT_GENERAL = 95231001; //一般
    public static final Integer COMPLAINT_GREAT = 95231002; //重大
    public static final Integer COMPLAINT_COMPLEX = 95231003; //复杂

    //抱怨类型
    public static final Integer COMPLAINT_TYPE = 2003;
    public static final Integer COMPLAINT_TYPE_01 = 20031001; //服务活动/产品相关
    public static final Integer COMPLAINT_TYPE_02 = 20031002; //供应商服务相关
    public static final Integer COMPLAINT_TYPE_03 = 20031003; //特约服务商服务相关
    public static final Integer COMPLAINT_TYPE_04 = 20031004; //维修相关
    public static final Integer COMPLAINT_TYPE_05 = 20031005; //配件相关
    public static final Integer COMPLAINT_TYPE_06 = 20031006; //其他

    //规定处理期限
    public static final Integer RULE_LIMIT = 9524;
    public static final Integer RULE_ONE_DAY = 95241001; //1天
    public static final Integer ONE_DAY = 1;
    public static final Integer RULE_THREE_DAY = 95241002; //3天
    public static final Integer THREE_DAY = 3;
    public static final Integer RULE_SEVEN_DAY = 95241003; //7天
    public static final Integer SEVEN_DAY = 7;

    //承诺状态
    public static final Integer PROMISE_STATUS = 9950;//承诺状态
    public static final Integer PROMISE_STATUS_01 = 99501001;//保存
    public static final Integer PROMISE_STATUS_02 = 99501002;//待大区经理审核
    public static final Integer PROMISE_STATUS_03 = 99501003;//待大区首席审核
    public static final Integer PROMISE_STATUS_04 = 99501004;//待计划审核
    public static final Integer PROMISE_STATUS_05 = 99501005;//待销售公司审核
    public static final Integer PROMISE_STATUS_06 = 99501006;//待财务审核
    public static final Integer PROMISE_STATUS_07 = 99501007;//财务审核通过
    public static final Integer PROMISE_STATUS_08 = 99501008;//审核驳回
    public static final Integer PROMISE_STATUS_09 = 99501009;//删除

    // 承诺还款状态
    public static final Integer PROMISE_PAY_STATUS = 9951;
    public static final Integer PROMISE_PAY_STATUS_00 = 99511000;	// 已删除
    public static final Integer PROMISE_PAY_STATUS_01 = 99511001;	// 未提报
    public static final Integer PROMISE_PAY_STATUS_02 = 99511002;	// 未还款
    public static final Integer PROMISE_PAY_STATUS_03 = 99511003;	// 已还款

    //编号类型
    public static final String NOCRT_ORDER_NO = "20051001";//订单号
    public static final String NOCRT_BO_ORDER_NO = "20051000";//订单号
    public static final String NOCRT_BOARD_NO = "20051002";//组板号
    public static final String NOCRT_PRO_NO = "20051003";//承诺单号
    public static final String NOCRT_OUTSTORE_NO = "20051004";//出库单号
    public static final String NOCRT_CUSORDER_NO = "20051005";//定做车订单号 
    public static final String NOCRT_CUS_PRODUCT_ORDER_NO = "20051006";//定做车汇总订单号 
    public static final String NOCRT_PLAN_NO = "20051007";//生产计划号
    public static final String NOCRT_TOTAL_PRO_NO = "20051008";//生产汇总订单号
    public static final String NOCRT_SEND_ORDER_NO = "20051009";//发运单号
    public static final String NOCRT_DKH_NO = "20051010";//集团车特殊配置
    public static final String NOCRT_BOARD_MY_NO = "200510011";//组板号(自提单)
    public static final String NOCRT_TOTAL_PRO_NO2 = "200510012";//集团客户订单号单独生成
    public static final String NOCRT_ASS_NO = "200510013";//分派号
    public static final String FIN_NO = "200510014";//经销商现金打款流水号
    public static final String NOCRT_SWZC_NO = "20050015";
    public static final String NOCRT_DB_ORDER_NO = "20050016";//调拨单号
    public static final String NOCRT_JS_APPLY_NO = "20050017";//结算申请单号
    
//    public static final String YC_NOCRT_ORDER_NO = "200510014";//预测转需求销售订单号
//	public static final String XQ_NOCRT_ORDER_NO = "200510015";//需求计划单号
	public static final String REBATE_NO = "200510016";//返利单号
	public static final String RETURN_VEHICLE_NO = "200510017";//退车申请单号
	public static final String SPECIAL_ORDER_NO = "200510018";//特殊订单号
	public static final String SPECIAL_PACKAGE_NO = "200510019";//特殊订单物料配置码
	public static final String FLEET_INTENT_NO = "200510020";//大客户支持申请单号
	public static final String VS_ORDER_NO = "200510021";//销售清单号
	public static final String PASS_NO = "200510022";//商品车交接及点检单
	public static final String VEHICLE_QUALITY_NO = "200510023";//收车质量信息反馈单号

    //添加默认值
    public static final Integer DEFAULT_VALUE = -1;

    public static final Integer PROMISE_DEP = 9960;//承诺审核部门层级
    public static final Integer PROMISE_DEP_01 = 99601001;//经销商
    public static final Integer PROMISE_DEP_02 = 99601002;//大区经理
    public static final Integer PROMISE_DEP_03 = 99601003;//大区首席
    public static final Integer PROMISE_DEP_04 = 99601004;//计划处
    public static final Integer PROMISE_DEP_05 = 99601005;//销售公司
    public static final Integer PROMISE_DEP_06 = 99601006;//财务处

    //支付状态
    public static final Integer PAYMENT_STATUS = 9715;//承诺审核部门层级
    public static final Integer PAYMENT_STATUS_01 = 97151001;//未付款
    public static final Integer PAYMENT_STATUS_02 = 97151002;//付款中
    public static final Integer PAYMENT_STATUS_03 = 97151003;//已付款

    //咨询状态
    public static final String CONSULT_STATUS = "9527";
    //public static final String CONSULT_STATUS_SPOT = "95271001";//现场处理
    public static final String CONSULT_STATUS_WAIT = "95271001";//待处理
    public static final String CONSULT_STATUS_FINISH = "95271002";//已处理

    //投诉状态
    public static final String COMPLAINT_STATUS = "9529";
    public static final String COMPLAINT_STATUS_WAIT = "95291001";//待处理
    public static final String COMPLAINT_STATUS_DOING = "95291002";//大区处理中
    public static final String COMPLAINT_STATUS_DOING_ALREADY = "95291006"; //大区已处理
    public static final String COMPLAINT_STATUS_DOING_REJECT = "95291008"; //驳回大区
    public static final String COMPLAINT_STATUS_WAIT_FEEDBACK = "95291003";//待反馈
    public static final String COMPLAINT_STATUS_WAIT_CLOSE = "95291004";//坐席回访已处理
    public static final String COMPLAINT_STATUS_ALREADY_CLOSE = "95291005";//已关闭
    public static final String COMPLAINT_STATUS_CLOSE = "95291007";//强制关闭
    public static final String COMPLAINT_DEALER_STATUS_DOING = "95291009";//经销商处理中
    public static final String COMPLAINT_DEALER_DEPT_DOING = "95291010";//部门处理中

    public static final Integer CHANEL_TYPE = 9000;//渠道类型
    public static final Integer CHANEL_TYPE_1 = 90001001;//来电
    public static final Integer CHANEL_TYPE_2 = 90001002;//网络
    public static final Integer CHANEL_TYPE_3 = 90001003;//数字营销转

    //销售退货申请状态
    public static final Integer PART_DLR_RETURN_STATUS = 9236;
    public static final Integer PART_DLR_RETURN_STATUS_01 = 92361001;//正常(未提交)
    public static final Integer PART_DLR_RETURN_STATUS_02 = 92361002;//审核中
    public static final Integer PART_DLR_RETURN_STATUS_03 = 92361003;//驳回
    public static final Integer PART_DLR_RETURN_STATUS_04 = 92361004;//出库中
    public static final Integer PART_DLR_RETURN_STATUS_05 = 92361005;//入库中
    public static final Integer PART_DLR_RETURN_STATUS_06 = 92361006;//退货完成
    public static final Integer PART_DLR_RETURN_STATUS_07 = 92361007;//已作废
    public static final Integer PART_DLR_RETURN_STATUS_08 = 92361008;//已关闭

    public static final Integer PART_DLR_RETURN_STATUS_09 = 92361009;//待验货
    public static final Integer PART_DLR_RETURN_STATUS_10 = 92361010;//向配送中心出库
    public static final Integer PART_DLR_RETURN_STATUS_11 = 92361011;//配送中心入库
    public static final Integer PART_DLR_RETURN_STATUS_12 = 92361012;//向总部出库
    public static final Integer PART_DLR_RETURN_STATUS_13 = 92361013;//总部入库
    public static final Integer PART_DLR_RETURN_STATUS_14 = 92361014;//总部退供应商
    public static final Integer PART_DLR_RETURN_STATUS_15 = 92361015;//待财务审核
    public static final Integer PART_DLR_RETURN_STATUS_16 = 92361016;//待ERP冲销
    public static final Integer PART_DLR_RETURN_STATUS_17 = 92361017;//待发票作废
    public static final Integer PART_DLR_RETURN_STATUS_18 = 92361018;//红字通知单
    public static final Integer PART_DLR_RETURN_STATUS_19 = 92361019;//税务退货证明
    public static final Integer PART_DLR_RETURN_STATUS_20 = 92361020;//待开红字发票
    public static final Integer PART_DLR_RETURN_STATUS_21 = 92361021;//已开红字发票
    public static final Integer PART_DLR_RETURN_STATUS_22 = 92361022;//总部处理中
    public static final Integer PART_DLR_RETURN_STATUS_99 = 92361099;//退货完成
    

    //退货类型
    public static final Integer PART_RETURN_TYPE = 9239;
    public static final Integer PART_RETURN_TYPE_01 = 92391001;//销售退货
    public static final Integer PART_RETURN_TYPE_02 = 92391002;//采购退货

    //采购退货申请状态
    public static final Integer PART_OEM_RETURN_STATUS = 9244;
    public static final Integer PART_OEM_RETURN_STATUS_00 = 92441000;//未提交
    public static final Integer PART_OEM_RETURN_STATUS_01 = 92441001;//审核中
    public static final Integer PART_OEM_RETURN_STATUS_02 = 92441002;//驳回
    public static final Integer PART_OEM_RETURN_STATUS_03 = 92441003;//出库中
    public static final Integer PART_OEM_RETURN_STATUS_04 = 92441004;//退货完成
    public static final Integer PART_OEM_RETURN_STATUS_05 = 92441005;//已作废

    /*************** bianlz addDate 2013-4-18配件销售管理(车厂) *************/
    public static final Integer CAR_FACTORY_SALES_MANAGER_BO_STATE = 9214;//BO单状态
    public static final Integer CAR_FACTORY_SALES_MANAGER_BO_STATE_01 = 92141001;//BO单状态-已保存
    public static final Integer CAR_FACTORY_SALES_MANAGER_BO_STATE_02 = 92141002;//BO单状态-部分处理
    public static final Integer CAR_FACTORY_SALES_MANAGER_BO_STATE_03 = 92141003;//BO单状态-已处理

    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_TYPE = 9215;//订单类型
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01 = 92151001;//订单类型-紧急订单
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02 = 92151002;//订单类型-常规订单
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03 = 92151003;//订单类型-特殊订单
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 = 92151004;//订单类型-直发订单、委托订单
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_05 = 92151005;//订单类型-调拨订单
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_06 = 92151006;//订单类型-BO订单
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 = 92151007;//订单类型-广宣订单
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08 = 92151008;//订单类型-市场促销订单
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_09 = 92151009;//订单类型-内部领用订单
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10 = 92151010;//订单类型-切换订单
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_11 = 92151011;//订单类型-三包急件订单
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12 = 92151012;//订单类型-销售采购订单

    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE = 9216;//订单状态
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 = 92161001;//订单状态——保存
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 = 92161002;//订单状态——提交
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03 = 92161003;//订单状态——审核
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04 = 92161004;//订单状态——作废
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 = 92161005;//订单状态——驳回
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06 = 92161006;//订单状态——车厂已审核
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07 = 92161007;//订单状态——已经关闭

    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE_08 = 92161008;//订单状态——未提报
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE_09 = 92161009;//订单状态——审核中
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE_10 = 92161010;//订单状态——审核通过
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE_11 = 92161011;//订单状态——审核驳回
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE_12 = 92161012;//订单状态——已删除
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE_13 = 92161013;//订单状态——已回运
    public static final Integer CAR_FACTORY_SALES_MANAGER_ORDER_STATE_14 = 92161014;//订单状态——已验收

    public static final Integer PART_TRANSFER_ORDER_STATE = 9261;//配件调拨单状态
    public static final Integer PART_TRANSFER_ORDER_STATE_01 = 92611001;//已提交

    public static final Integer PART_GX_ORDER_STATE = 9279;//广宣品发运计划单状态
    public static final Integer PART_GX_ORDER_STATE_01 = 92791001;//已保存
    public static final Integer PART_GX_ORDER_STATE_02 = 92791002;//已提交
    public static final Integer PART_GX_ORDER_STATE_03 = 92791003;//已确认
    public static final Integer PART_GX_ORDER_STATE_04 = 92791004;//已发运

    public static final Integer PART_GX_ORDER_OUT_TYPE = 9278;//出库类型
    public static final Integer PART_GX_ORDER_OUT_TYPE_01 = 92781001;//随车出库
    public static final Integer PART_GX_ORDER_OUT_TYPE_02 = 92781002;//直发出库

    public static final Integer CAR_FACTORY_SALES_MANAGER_TRANS_TYPE = 9217;//发运方式
    public static final Integer CAR_FACTORY_SALES_MANAGER_TRANS_TYPE_01 = 92171001;//发运方式-铁路快件
    public static final Integer CAR_FACTORY_SALES_MANAGER_TRANS_TYPE_02 = 92171002;//发运方式-自取
    public static final Integer CAR_FACTORY_SALES_MANAGER_TRANS_TYPE_03 = 92171003;//发运方式-汽车

    public static final Integer CAR_FACTORY_SALES_MANAGER_STOCK = 9218;//库存
    public static final Integer CAR_FACTORY_SALES_MANAGER_STOCK_01 = 92181001;//库存-有库存
    public static final Integer CAR_FACTORY_SALES_MANAGER_STOCK_02 = 92181002;//库存-没有库存

    public static final Integer CAR_FACTORY_SALES_PAY_TYPE = 9219;//付款方式
    public static final Integer CAR_FACTORY_SALES_PAY_TYPE_01 = 92191001;//现金
    public static final Integer CAR_FACTORY_SALES_PAY_TYPE_02 = 92191002;//支票
    public static final Integer CAR_FACTORY_SALES_PAY_TYPE_03 = 92191003;//其他

    public static final Integer PART_OPERATION_TYPE = 9318;//为TT_PART_OPERATION_HISTORY的TYPE字段服务，代表业务类型，不需要插入TC_CODE
    public static final Integer PART_OPERATION_TYPE_01 = 93181001;//配件销售管理(车厂)订单
    public static final Integer PART_OPERATION_TYPE_02 = 93181002;//销售单

    public static final Integer CAR_FACTORY_ORDER_CHECK_STATE = 9237;//配件销售订单审核
    public static final Integer CAR_FACTORY_ORDER_CHECK_STATE_01 = 92371001;//已保存
    public static final Integer CAR_FACTORY_ORDER_CHECK_STATE_02 = 92371002;//已提交
    public static final Integer CAR_FACTORY_ORDER_CHECK_STATE_03 = 92371003;//资源审核通过
    public static final Integer CAR_FACTORY_ORDER_CHECK_STATE_04 = 92371004;//资源审核驳回
    public static final Integer CAR_FACTORY_ORDER_CHECK_STATE_05 = 92371005;//财务审核通过
    public static final Integer CAR_FACTORY_ORDER_CHECK_STATE_06 = 92371006;//财务审核驳回
    public static final Integer CAR_FACTORY_ORDER_CHECK_STATE_07 = 92371007;//已作废

    public static final Integer CAR_FACTORY_ORDER_CHECK_PAY_TRANS = 9238;//运费支付方式
    public static final Integer CAR_FACTORY_ORDER_CHECK_PAY_TRANS_01 = 92381001;//免
    public static final Integer CAR_FACTORY_ORDER_CHECK_PAY_TRANS_02 = 92381002;//不免
    public static final Integer CAR_FACTORY_ORDER_CHECK_PAY_TRANS_03 = 92381003;//支票
    public static final Integer CAR_FACTORY_ORDER_CHECK_PAY_TRANS_04 = 92381004;//挂账

    public static final Integer CAR_FACTORY_SALE_ORDER_STATE = 9240;//销售单状态
    public static final Integer CAR_FACTORY_SALE_ORDER_STATE_01 = 92401001;//已保存
    public static final Integer CAR_FACTORY_SALE_ORDER_STATE_02 = 92401002;//已提交
    public static final Integer CAR_FACTORY_SALE_ORDER_STATE_03 = 92401003;//已作废
    public static final Integer CAR_FACTORY_SALE_ORDER_STATE_04 = 92401004;//财务通过
    public static final Integer CAR_FACTORY_SALE_ORDER_STATE_05 = 92401005;//财务驳回
    public static final Integer CAR_FACTORY_SALE_ORDER_STATE_06 = 92401006;//装箱中
    public static final Integer CAR_FACTORY_SALE_ORDER_STATE_07 = 92401007;//已装箱
    public static final Integer CAR_FACTORY_SALE_ORDER_STATE_08 = 92401008;//已出库
    public static final Integer CAR_FACTORY_SALE_ORDER_STATE_09 = 92401009;//已强制关闭(整单完全现场BO)

    public static final Integer CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE = 9241;//资金占用明细
    public static final Integer CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_01 = 92411001;//占用
    public static final Integer CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_02 = 92411002;//释放
    public static final Integer CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_03 = 92411003;//开票

    public static final Integer CAR_FACTORY_PKG_STATE = 9245;//装箱状态
    public static final Integer CAR_FACTORY_PKG_STATE_01 = 92451001;//装箱中
    public static final Integer CAR_FACTORY_PKG_STATE_02 = 92451002;//已装箱
    public static final Integer CAR_FACTORY_PKG_STATE_03 = 92451003;//已出库
    public static final Integer CAR_FACTORY_PKG_STATE_05 = 92451005;//已完成(整车配件随车确认)
    public static final Integer CAR_FACTORY_PKG_STATE_04 = 92451004;//已确认(售后配件随车确认)

    public static final Integer CAR_FACTORY_PKG_TYPE = 9249;//装箱方式
    public static final Integer CAR_FACTORY_PKG_TYPE_01 = 92491001;//纸箱          
    public static final Integer CAR_FACTORY_PKG_TYPE_02 = 92491002;//木箱          
    public static final Integer CAR_FACTORY_PKG_TYPE_03 = 92491003;//轮胎          
    public static final Integer CAR_FACTORY_PKG_TYPE_04 = 92491004;//钢板弹簧      
    public static final Integer CAR_FACTORY_PKG_TYPE_05 = 92491005;//编织袋        
    public static final Integer CAR_FACTORY_PKG_TYPE_06 = 92491006;//消声器        
    public static final Integer CAR_FACTORY_PKG_TYPE_07 = 92491007;//驾驶室带大梁  

    public static final Integer CAR_FACTORY_OUTSTOCK_STATE = 9254;//出库状态
    public static final Integer CAR_FACTORY_OUTSTOCK_STATE_01 = 92541001;//已装箱
    public static final Integer CAR_FACTORY_OUTSTOCK_STATE_02 = 92541002;//已出库
    public static final Integer CAR_FACTORY_OUTSTOCK_STATE_03 = 92541003;//已发运
    public static final Integer CAR_FACTORY_OUTSTOCK_STATE_04 = 92541004;//已开票
    public static final Integer CAR_FACTORY_OUTSTOCK_STATE_05 = 92541005;//已入库
    public static final Integer CAR_FACTORY_OUTSTOCK_STATE_06 = 92541006;//已作废

    public static final Integer CAR_FACTORY_SO_FORM = 9255;//SOFORM
    public static final Integer CAR_FACTORY_SO_FORM_01 = 92551001;//新增
    public static final Integer CAR_FACTORY_SO_FORM_02 = 92551002;//订单生成
    public static final Integer CAR_FACTORY_SO_FORM_03 = 92551003;//拆分
    public static final Integer CAR_FACTORY_SO_FORM_04 = 92551004;//BO单
    public static final Integer CAR_FACTORY_SO_FORM_05 = 92551005;//直发订单虚拟出库

    public static final Integer CAR_FACTORY_TRANS_STATE = 9256;//发运单状态
    public static final Integer CAR_FACTORY_TRANS_STATE_01 = 92561001;//已发运
    public static final Integer CAR_FACTORY_TRANS_STATE_02 = 92561002;//已接收

    public static final Integer CAR_FACTORY_INSTOCK_APPROVAL_STATE = 9257;//验收状态
    public static final Integer CAR_FACTORY_INSTOCK_APPROVAL_STATE_01 = 92571001;//正常
    public static final Integer CAR_FACTORY_INSTOCK_APPROVAL_STATE_02 = 92571002;//缺件
    public static final Integer CAR_FACTORY_INSTOCK_APPROVAL_STATE_03 = 92571003;//破损

    public static final Integer CAR_FACTORY_INSTOCK_STATE = 9258;//接收入库状态
    public static final Integer CAR_FACTORY_INSTOCK_STATE_01 = 92581001;//已入库

    public static final String CONFIRE_TYPE = "9530";//确认意见
    public static final String CONFIRE_WAIT_TYPE = "95301001";//未处理完毕,继续处理
    public static final String CONFIRE_TURN_TYPE = "95301002";//转部门处理
    public static final String CONFIRE_RETURN_TYPE = "95301003";//已转入客户回访
    public static final String CONFIRE_ACCEPTE_TYPE = "95301004";//接到已批准的抱怨关闭申请单
    public static final String CONFIRE_CONTINUE_RETURN_TYPE = "95301005";//继续回访
    public static final String CONFIRE_CLOSE_TYPE = "95301006";//已处理满意关闭
    
    
    public static final String BQHS_TYPE = "9476";//北汽开票状态
    public static final String BQHS_TYPE_01 = "94761001";//已生成
    public static final String BQHS_TYPE_02 = "94761002";//已上报
    public static final String BQHS_TYPE_03 = "94761003";//已审核
   

    public static final Integer IS_CLAIM = 9532;//是否索赔
    public static final Integer IS_CLAIM_01 = 95321001;//索赔
    public static final Integer IS_CLAIM_02 = 95321002;//不索赔

    public static final Integer IS_RETURN = 9536;//是否回运
    public static final Integer IS_RETURN_01 = 95361001;//需回运
    public static final Integer IS_RETURN_02 = 95361002;//不回运
    //索赔出库类型
    public static final Integer OUT_CLAIM_TYPE = 9533;//索赔出库类型
    public static final Integer OUT_CLAIM_TYPE_01 = 95331001;//供应商开票
    public static final Integer OUT_CLAIM_TYPE_02 = 95331002;//报废
    public static final Integer OUT_CLAIM_TYPE_03 = 95331003;//误判

    //中转库出库单状态
    public static final Integer OUT_ORDER_CHECK_STATUS = 1374;//中转库出库单状态
    public static final Integer OUT_ORDER_CHECK_STATUS_01 = 13741001;//未提报
    public static final Integer OUT_ORDER_CHECK_STATUS_02 = 13741002;//待计划处审核
    public static final Integer OUT_ORDER_CHECK_STATUS_03 = 13741003;//待室主任审核
    public static final Integer OUT_ORDER_CHECK_STATUS_04 = 13741004;//待财务审核
    public static final Integer OUT_ORDER_CHECK_STATUS_05 = 13741005;//待开票
    public static final Integer OUT_ORDER_CHECK_STATUS_06 = 13741006;//开票完成
    public static final Integer OUT_ORDER_CHECK_STATUS_07 = 13741007;//驳回
    public static final Integer OUT_ORDER_CHECK_STATUS_08 = 13741008;//已删除

    //返利使用比例
    public static final Integer ORDER_USE_REBATE_PERCENTAGE = 30;//返利使用比例

    //中转出库单审核审核状态
    public static final Integer OUT_ORDER_AUDIT_STATUS = 1375;//中转库出库单状态
    public static final Integer OUT_ORDER_AUDIT_STATUS_01 = 13751001;//中转库审核通过
    public static final Integer OUT_ORDER_AUDIT_STATUS_02 = 13751002;//驳回

    //中转出库单审核类型
    public static final Integer OUT_ORDER_AUDIT_TYPE = 1376;//中转库出库单审核类型
    public static final Integer OUT_ORDER_AUDIT_TYPE_01 = 13761001;//计划处审核
    public static final Integer OUT_ORDER_AUDIT_TYPE_02 = 13761002;//二次审核
    public static final Integer OUT_ORDER_AUDIT_TYPE_03 = 13761003;//财务审核
    public static final Integer OUT_ORDER_AUDIT_TYPE_04 = 13761004;//大区审核
    public static final Integer OUT_ORDER_AUDIT_TYPE_05 = 13761005;//财务开票确认
    public static final Integer OUT_ORDER_AUDIT_TYPE_06 = 13761006;//计划处组板确认
    public static final Integer OUT_ORDER_AUDIT_TYPE_07 = 13761007;//计划处组板审核

    //订做车生产订单状态
    public static final Integer ORDER_REPORT_STATUS = 1200;//订做车生产订单状态
    public static final Integer ORDER_REPORT_STATUS_01 = 12001001;//未提报
    public static final Integer ORDER_REPORT_STATUS_02 = 12001002;//已提报
    public static final Integer ORDER_REPORT_STATUS_03 = 12001003;//已转生产订单
    public static final Integer ORDER_REPORT_STATUS_04 = 12001004;//全部提车
    public static final Integer ORDER_REPORT_STATUS_05 = 12001005;//部分提车
    public static final Integer ORDER_REPORT_STATUS_06 = 12001006;//大区审核通过
    public static final Integer ORDER_REPORT_STATUS_07 = 12001007;//审核驳回

    //订做车生产订单类型
    public static final Integer ORDER_REPORT_TYPE = 1201;//订做车生产订单类型
    public static final Integer ORDER_REPORT_TYPE_01 = 12011001;//一般订做车
    public static final Integer ORDER_REPORT_TYPE_02 = 12011002;//集团客户订做车

    //生产订单状态
    public static final Integer PRODUCT_ORDER_CHECK_STATUS = 1210;
    public static final Integer PRODUCT_ORDER_CHECK_STATUS_01 = 12101001;//未生成
    public static final Integer PRODUCT_ORDER_CHECK_STATUS_02 = 12101002;//已生成
    public static final Integer PRODUCT_ORDER_CHECK_STATUS_03 = 12101003;//未提报
    public static final Integer PRODUCT_ORDER_CHECK_STATUS_04 = 12101004;//生产计划审核驳回
    public static final Integer PRODUCT_ORDER_CHECK_STATUS_05 = 12101005;//生产计划生成驳回

    

    //生产计划状态
    public static final Integer PRODUCT_PLAN_CHECK_STATUS = 1212;
    public static final Integer PRODUCT_PLAN_CHECK_STATUS_01 = 12121001;//待审核
    public static final Integer PRODUCT_PLAN_CHECK_STATUS_02 = 12121002;//审核通过
    public static final Integer PRODUCT_PLAN_CHECK_STATUS_03 = 12121003;//审核驳回

    //单据编码
    public static final Integer ORDER_CODE_TYPE = 1220;
    public static final Integer ORDER_CODE_TYPE_01 = 12201001;//生产计划号
    public static final Integer ORDER_CODE_TYPE_02 = 12201002;//汇总订单号

    //配件拆合件类型
    public static final Integer PART_SPCPD_TYPE = 9248;
    public static final Integer PART_SPCPD_TYPE_01 = 92481001;//拆件
    public static final Integer PART_SPCPD_TYPE_02 = 92481002;//合件

    //配件拆合申请状态
    public static final Integer PART_SPCPD_STATUS = 9250;
    public static final Integer PART_SPCPD_STATUS_01 = 92501001;//审核中
    public static final Integer PART_SPCPD_STATUS_02 = 92501002;//驳回
    public static final Integer PART_SPCPD_STATUS_03 = 92501003;//出入库中
    public static final Integer PART_SPCPD_STATUS_04 = 92501004;//完成

    //车辆退回状态
    public static final Integer RETREAT_STATUS = 9716;
    public static final Integer RETREAT_STATUS01 = 9716001;//正常
    public static final Integer RETREAT_STATUS02 = 9716002;//退回

    //配件折扣类型
    public static final Integer PART_DISCOUNT_TYPE = 9259;
    public static final Integer PART_DISCOUNT_TYPE_01 = 92591001;//服务商类型
    public static final Integer PART_DISCOUNT_TYPE_02 = 92591002;//服务商
    public static final Integer PART_DISCOUNT_TYPE_03 = 92591003;//配件种类
    public static final Integer PART_DISCOUNT_TYPE_04 = 92591004;//订单金额

    //回访考核项目
    public static final Integer REVIEW_ASSESS_TYPE = 9535;
    public static final Integer REVIEW_ASSESS_TYPE_01 = 95351001;//日均电话频次
    public static final Integer REVIEW_ASSESS_TYPE_02 = 95351002;//单次时长
    public static final Integer REVIEW_ASSESS_TYPE_03 = 95351003;//用户满意度

    //入库异常状态
    public static final Integer INSTOCK_EXCEPTION_REPLY = 9262;//
    public static final Integer INSTOCK_EXCEPTION_REPLY_01 = 92621001;//未回复
    public static final Integer INSTOCK_EXCEPTION_REPLY_02 = 92621002;//已回复
    public static final Integer INSTOCK_EXCEPTION_REPLY_03 = 92621003;//已关闭

    //配件自制/外购
    public static final Integer PART_PRODUCE_STATE = 9263;
    public static final Integer PART_PRODUCE_STATE_01 = 92631001;//自制
    public static final Integer PART_PRODUCE_STATE_02 = 92631002;//外购

    //配件配件品牌
    public static final Integer PART_PRODUCE_FAC = 9264;
    public static final Integer PART_PRODUCE_FAC_01 = 92641001;//景德镇工厂
    public static final Integer PART_PRODUCE_FAC_02 = 92641002;//合肥工厂
    public static final Integer PART_PRODUCE_FAC_03 = 92641003;//九江工厂
    public static final Integer PART_PRODUCE_FAC_04 = 92641004;//独自采购

    //配件包装发运方式
    public static final Integer PART_PACK_STATE = 9265;
    public static final Integer PART_PACK_STATE_01 = 92651001;//不限
    public static final Integer PART_PACK_STATE_02 = 92651002;//不可空运

    //配件销售虚拟出入库改变开关
    public static final Integer PART_SALE_STOCK_CONTROL = 20271001;//
    //配件销售虚拟金额校验开关
    public static final Integer PART_SALE_ACCOUNT_CONTROL = 20271002;//

    //旧件回运单，签收包装情况
    public static final Integer OLD_PART_PAKGE = 9538;//
    public static final Integer OLD_PART_PAKGE_01 = 95381001;//完好
    public static final Integer OLD_PART_PAKGE_02 = 95381002;//包装有破损
    public static final Integer OLD_PART_PAKGE_03 = 95381003;//包装散
    public static final Integer OLD_PART_PAKGE_04 = 95381004;//货运站丢件
    public static final Integer OLD_PART_PAKGE_05 = 95381005;//无包装
    public static final Integer OLD_PART_PAKGE_06 = 95381006;//无
    public static final Integer OLD_PART_PAKGE_07 = 95381007;//包装箱超重
    //旧件回运单，签收故障卡情况

    public static final Integer OLD_PART_MARK = 9539;//
    public static final Integer OLD_PART_MARK_01 = 95391001;//符合要求
    public static final Integer OLD_PART_MARK_02 = 95391002;//不符合要求
    public static final Integer OLD_PART_MARK_03 = 95391003;//填写不全
    public static final Integer OLD_PART_MARK_04 = 95391004;//无故障卡
    public static final Integer OLD_PART_MARK_05 = 95391005;//粘挂不规范
    //旧件回运单，签收清单情况

    public static final Integer OLD_PART_DETAIL = 9540;//
    public static final Integer OLD_PART_DETAIL_01 = 95401001;//好
    public static final Integer OLD_PART_DETAIL_02 = 95401002;//清单脏污
    public static final Integer OLD_PART_DETAIL_03 = 95401003;//清单破损
    public static final Integer OLD_PART_DETAIL_04 = 95401004;//分月份手工清单
    public static final Integer OLD_PART_DETAIL_05 = 95401005;//未分月份手工清单
    public static final Integer OLD_PART_DETAIL_06 = 95401006;//无清单
    public static final Integer OLD_PART_DETAIL_07 = 95401007;//纸张大小不符合要求
    public static final Integer OLD_PART_DETAIL_08 = 95401008;//不合格

    public static final Integer PART_IS_CHANGHE = 9541;//是否重庆件
    public static final Integer PART_IS_CHANGHE_01 = 95411001;//重庆
    public static final Integer PART_IS_CHANGHE_02 = 95411002;//东安

    public static final Integer SHIFT_TIMES = 9550;//班次类型
    public static final Integer SHIFT_TIMES_NORMAL_DAYS = 95501001;//回访班
    public static final Integer SHIFT_TIMES_LONG_DAYS = 95501002;//长白班
    public static final Integer SHIFT_TIMES_NIGHT_DAYS = 95501003;//夜班
    public static final Integer SHIFT_TIMES_A_DAYS = 95501004;//回访A班

    public static final Integer SHIFT_KIND = 9551;//坐席业务
    public static final Integer SHIFT_KIND_01 = 95511001;//其它回访
    public static final Integer SHIFT_KIND_02 = 95511002;//投诉回访
    public static final Integer SHIFT_KIND_03 = 95511003;//接电话		

    //配件销售余额校验开关
    public static final Integer PART_SALE_BALANCE_VALIDATE_CONTROL = 20281001;

    public static final Integer ADMIN_VERIFY = 9531; //管理员审核
    public static final Integer ADMIN_VERIFY_SUCCESS = 95311001; //管理员审核
    public static final Integer ADMIN_VERIFY_FAIL = 95311002; //管理员审核

    //采购单验收指令生成状态
    public static final Integer PURCHASE_ORDER_STATE = 9269;
    public static final Integer PURCHASE_ORDER_STATE_01 = 92691001;//未完成
    public static final Integer PURCHASE_ORDER_STATE_02 = 92691002;//已完成
    public static final Integer PURCHASE_ORDER_STATE_03 = 92691003;//已关闭
    public static final Integer PURCHASE_ORDER_STATE_04 = 92691004;//已超期

    //配件：服务商开票类型
    public static final Integer DLR_INVOICE_TYPE = 9270;
    public static final Integer DLR_INVOICE_TYPE_01 = 92701001;//增值税专用发票
    public static final Integer DLR_INVOICE_TYPE_02 = 92701002;//增值税普通发票

    //采购退货类型
    public static final Integer PART_OEM_RETURN_TYPE = 9271;
    public static final Integer PART_OEM_RETURN_TYPE_01 = 92711001;//有单退货
    public static final Integer PART_OEM_RETURN_TYPE_02 = 92711002;//无单退货

    //配件：财务开票方式
    public static final Integer INVOICE_WAY = 9272;
    public static final Integer INVOICE_WAY_01 = 92721001;//正常开票
    public static final Integer INVOICE_WAY_02 = 92721002;//手工开票
    //开工单时，配件的使用类型
    public static final Integer PART_USE_TYPE = 8032;
    public static final Integer PART_USE_TYPE_01 = 80321001;//维修
    public static final Integer PART_USE_TYPE_02 = 80321002;//更换

    //导出SHEET类型
    public static final Integer EXPORT_TYPE = 9718;
    public static final Integer EXPORT_ONLY_SHEET = 97181001;//单一SHEET
    public static final Integer EXPORT_MORE_SHEET = 97181002;//多个SHEET

    //三包凭证补办状态
    public static final Integer PACKGE_CHANGE_STATUS = 9544;
    public static final Integer PACKGE_CHANGE_STATUS_01 = 95441001;// 未上报
    public static final Integer PACKGE_CHANGE_STATUS_02 = 95441002;// 已上报
    public static final Integer PACKGE_CHANGE_STATUS_03 = 95441003;// 大区审核退回
    public static final Integer PACKGE_CHANGE_STATUS_04 = 95441004;// 大区审核通过
    public static final Integer PACKGE_CHANGE_STATUS_05 = 95441005;// 技术服务处审核退回
    public static final Integer PACKGE_CHANGE_STATUS_06 = 95441006;// 技术服务处审核通过

    //条码申请状态
    public static final Integer BARCODE_APPLY_STATUS = 9545;
    public static final Integer BARCODE_APPLY_STATUS_01 = 95451001;// 未上报
    public static final Integer BARCODE_APPLY_STATUS_02 = 95451002;// 已上报
    public static final Integer BARCODE_APPLY_STATUS_03 = 95451003;// 审核退回
    public static final Integer BARCODE_APPLY_STATUS_04 = 95451004;// 审核通过

    public static final String REGEX = ".";// split通过什么截取
    //工时单价政策状态
    public static final Integer LABOUR_CHANG_STATUS = 9546;
    public static final Integer LABOUR_CHANG_STATUS_01 = 95461001;// 未执行
    public static final Integer LABOUR_CHANG_STATUS_02 = 95461002;// 执行中
    public static final Integer LABOUR_CHANG_STATUS_03 = 95461003;// 以终止

    public final static Integer Quality_Verify = 9553;//质量申报审核
    public final static Integer Quality_Verify_01 = 95531001; //未申报
    public final static Integer Quality_Verify_02 = 95531002; //已申报
    public final static Integer Quality_Verify_03 = 95531003; //一级审核通过
    public final static Integer Quality_Verify_04 = 95531004; //驳回
    public final static Integer Quality_Verify_05 = 95531005; //审核通过

    public static final Integer OUT_PART_TYPE = 9547;
    public static final Integer OUT_PART_TYPE_01 = 95471001;// 主因件出库
    public static final Integer OUT_PART_TYPE_02 = 95471002;// 常规件出库
    public static final Integer OUT_PART_TYPE_03 = 95471003;// 特殊通知单
    public static final Integer OUT_PART_TYPE_04 = 95471004;// 无旧件单

    public static final Integer PART_MOBILITY = 9274;//配件主数据维护中配件流动性
    public static final Integer PART_MOBILITY_A = 92741001;//1
    public static final Integer PART_MOBILITY_B = 92741002;//2
    public static final Integer PART_MOBILITY_C = 92741003;//3
    public static final Integer PART_MOBILITY_D = 92741004;//4
    public static final Integer PART_MOBILITY_E = 92741005;//5
    public static final Integer PART_MOBILITY_F = 92741006;//6

    public static final Integer ORDER_ORIGIN_TYPE = 9275;//配件订单来源类型
    public static final Integer ORDER_ORIGIN_TYPE_01 = 92751001;//计划
    public static final Integer ORDER_ORIGIN_TYPE_02 = 92751002;//领用
    public static final Integer ORDER_ORIGIN_TYPE_03 = 92751003;//直发

    public static final Integer PART_GIFT_WAY = 9276;//赠送配件方式
    public static final Integer PART_GIFT_WAY_01 = 92761001;//整单方式
    public static final Integer PART_GIFT_WAY_02 = 92761002;//品种方式

    public static final Integer PART_BALANCE_TYPE = 9277;//配件结算类型
    public static final Integer PART_BALANCE_TYPE_01 = 92771001;//料据拨付单
    public static final Integer PART_BALANCE_TYPE_02 = 92771002;//领用单

    public static final Integer PART_FREIGHTAGE_OPTION = 9278;//配件运费加价条件
    public static final Integer PART_FREIGHTAGE_OPTION_01 = 92781001;//大于
    public static final Integer PART_FREIGHTAGE_OPTION_02 = 92781002;//小于
    public static final Integer PART_FREIGHTAGE_OPTION_03 = 92781003;//等于

    public static final Integer TRANS_ORG = 92351001;//

    public static Integer PAGE_SIZE_PART_MINI = 100; // pageSize
    public static Integer PAGE_SIZE_PART_NORMAL = 300;
    public static Integer PAGE_SIZE_PART_MIDDLE = 500; // pageSize

    public static String VEHICLE_IS_FAMILY = "SBCL001";//家用车策略代码,修改时 必须和数据库维护的家用车代码一致，否则无法判断
    public static String VEHICLE_IS_FAMILY_02 = "20130801";//家用车规则代码,修改时 必须和数据库维护的家用车代码一致，否则无法判断

    public static Integer Part_SALES_PRICE_FOR_SA = 45;//销售价格执行日期for售后 45天

    public static final Integer PACK_TYPE = 1241;//包装类别
    public static final Integer PACK_TYPE_01 = 12411001;//个件包装
    public static final Integer PACK_TYPE_02 = 12411002;//发运包装   

    public static final Integer CER_TYPE=1480;//三方信贷 类型
    public static final Integer CER_TYPE_01 = 14801001;//新签
    public static final Integer CER_TYPE_02 = 14801002;//续签

    //排产订单状态
    public static final Integer PRO_ORDER_STATUS = 1481;
    public static final Integer PRO_ORDER_STATUS_01 = 14811001;//已保存
    public static final Integer PRO_ORDER_STATUS_02 = 14811002;//已提交

    public static final Integer HGZ_STATUS=9722;//合格证状态
    public static final Integer HGZ_STATUS_01 = 97221001;//未扣留
    public static final Integer HGZ_STATUS_02 = 97221002;//扣留   

    public static final Integer QUELITY_GRATE=9555;//工单质量等级
    public static final Integer QUELITY_GRATE_01 = 95551001;//一级
    public static final Integer QUELITY_GRATE_02 = 95551002;//二级 
    public static final Integer QUELITY_GRATE_03 = 95551003;//三级
    public static final Integer QUELITY_GRATE_04 = 95551004;//四级 

    public static final String  BQ_ZZ_GUISS_01="01";//北汽销售
    public static final String  BQ_ZZ_GUISS_02="02";//北汽幻速

    //收货地址类型
    public static final Integer SH_ADDRESS_TYPE = 2048;
    public static final Integer SH_ADDRESS_TYPE_01 = 20481001; //广宣收货地址
    public static final Integer SH_ADDRESS_TYPE_02 = 20481002; //发票收件地址
    public static final Integer SH_ADDRESS_TYPE_03 = 20481003; //信函收件地址
    public static final Integer SH_ADDRESS_TYPE_04 = 20481004; //整车收货地址

    //售后收货地址类型
    public static final Integer SHOU_ADDRESS_TYPE = 2049;
    public static final Integer SHOU_ADDRESS_TYPE_01 = 20491001; //配件接收地址
    public static final Integer SHOU_ADDRESS_TYPE_02 = 20491002; //发票邮寄地址
    public static final Integer SHOU_ADDRESS_TYPE_03 = 20491003; //信件接收地址


    //运输单审核状态
    public static final Integer SP_JJ_TRANSPORT_STATUS = 9554;
    public static final Integer SP_JJ_TRANSPORT_STATUS_01 = 95541001; //已保存
    public static final Integer SP_JJ_TRANSPORT_STATUS_02 = 95541002; //已提交
    public static final Integer SP_JJ_TRANSPORT_STATUS_03 = 95541003; //已通过
    public static final Integer SP_JJ_TRANSPORT_STATUS_04 = 95541004; //已驳回
    public static final Integer SP_JJ_TRANSPORT_STATUS_05 = 95541005; //已删除

    //运输单到货方式
    public static final Integer SP_JJ_TRANSPORT_SENDWAY = 9556;
    public static final Integer SP_JJ_TRANSPORT_SENDWAY_01 = 95561001; //自提
    public static final Integer SP_JJ_TRANSPORT_SENDWAY_02 = 95561002; //送货上门
    //旧件回运申请单审核状态
    public static final Integer SP_JJ_RETURN_APPLY_STATUS = 9557;
    public static final Integer SP_JJ_RETURN_APPLY_STATUS_01 = 95571001; //已保存
    public static final Integer SP_JJ_RETURN_APPLY_STATUS_02 = 95571002; //已提交
    public static final Integer SP_JJ_RETURN_APPLY_STATUS_03 = 95571003; //已审核
    public static final Integer SP_JJ_RETURN_APPLY_STATUS_04 = 95571004; //已补录

    //旧件回运申请是否同意
    public static final Integer SP_JJ_RETURN_APPLY_ISAGREE = 9558;
    public static final Integer SP_JJ_RETURN_APPLY_ISAGREE_01 = 95581001; //同意
    public static final Integer SP_JJ_RETURN_APPLY_ISAGREE_02 = 95581002; //不同意

    //配件活动管理--活动类型
    public static final Integer PART_ACTIVITY_TYPE = 9559;
    public static final Integer PART_ACTIVITY_TYPE_REPLACED_01 = 95591001; //替换件活动

    //配件订单有效期类型
    public static final Integer PART_PERIOD_TYPE = 9560;
    public static final Integer PART_PERIOD_TYPE_REPLACED_01 = 95601001; //替换件回运有效期
    public static final Integer PART_PERIOD_TYPE_ORDER_02 = 95601002; //订单有效期

    public static final Integer PART_ORDER_STATE = 9561;//配件业务操作状态
    public static final Integer PART_ORDER_STATE_01 = 95611001;//拣货中
    public static final Integer PART_ORDER_STATE_02 = 95611002;//发运中

    //配件活动管理--活动配件类型
    public static final Integer ACTIVITY_PART_TYPE = 9562;
    public static final Integer ACTIVITY_PART_TYPE_01 = 95621001; //装车配件
    public static final Integer ACTIVITY_PART_TYPE_02 = 95621002; //库存配件

    //自动生成问卷常量信息
    public static final Integer QUESTION = 7001;//问卷
    public static final Integer QUESTION_QUESTIONNAIRE = 70011001;//问卷信息(回访登记自动生成问卷)
    public static final Integer QUESTION_DETAIL = 70011002;//问卷问题信息(回访登记自动生成问题)
    public static final Integer QUESTION_ANSWER = 70011003;//问卷选择信息(回答1|回答2)

    /***************来电客户类型属性 wizard_lee 2014-4-18***********************/
    public static final Integer BIZ_CUSTOMER_TYPE = 7002;//来电咨询客户类型
    public static final Integer BIZ_CUSTOMER_BUY_TYPE = 70021001;//已购车类型
    public static final Integer BIZ_CUSTOMER_PERSONAL_TYPE = 70021002;//潜在客户类型
    public static final Integer BIZ_CUSTOMER_COMPANY_TYPE = 70021003;//潜在商家类型
    public static final Integer BIZ_CUSTOMER_OTHER_TYPE = 70021004;//其他类型

    /*************了解渠道 wizard_lee 2014-04-18 *****************************/
    public static final Integer BIZ_KNOWN_CHANEL = 7003;
    public static final Integer BIZ_KNOWN_CHANEL1 = 70031001;	//央视
    public static final Integer BIZ_KNOWN_CHANEL2 = 70031002;	//广播
    public static final Integer BIZ_KNOWN_CHANEL3 = 70031003;	//北汽员工
    public static final Integer BIZ_KNOWN_CHANEL4 = 70031004;	//朋友介绍
    public static final Integer BIZ_KNOWN_CHANEL5 = 70031005;	//车展
    public static final Integer BIZ_KNOWN_CHANEL6 = 70031006;	//数字营销
    public static final Integer BIZ_KNOWN_CHANEL7 = 70031007;	//实体店
    public static final Integer BIZ_KNOWN_CHANEL8 = 70031008;	//网站/汽车之家
    public static final Integer BIZ_KNOWN_CHANEL9 = 70031009;	//凤凰网
    public static final Integer BIZ_KNOWN_CHANEL10 = 70031010;	//官网
    public static final Integer BIZ_KNOWN_CHANEL11 = 70031011;	//易车网
    public static final Integer BIZ_KNOWN_CHANEL12 = 70031012;	//汽车网
    public static final Integer BIZ_KNOWN_CHANEL13 = 70031013;	//搜狐
    public static final Integer BIZ_KNOWN_CHANEL14 = 70031014;	//新浪
    public static final Integer BIZ_KNOWN_CHANEL15 = 70031015;	//腾讯
    public static final Integer BIZ_KNOWN_CHANEL16 = 70031016;	//发布会
    public static final Integer BIZ_KNOWN_CHANEL17 = 70031017;	//微信
    public static final Integer BIZ_KNOWN_CHANEL18 = 70031018;	//太平洋
    public static final Integer BIZ_KNOWN_CHANEL19 = 70031019;	//网上车市
    public static final Integer BIZ_KNOWN_CHANEL20 = 70031020;	//网易
    public static final Integer BIZ_KNOWN_CHANEL21 = 70031021;	//爱卡
    public static final Integer BIZ_KNOWN_CHANEL22 = 70031022;	//车友网
    public static final Integer BIZ_KNOWN_CHANEL23 = 70031023;	//不清楚哪个网站

    /*************商家类型 wizard_lee 2014-04-21 ***************************/
    public static final Integer BUSSNESS_TYPE = 7004;
    public static final Integer BUSSNESS_TYPE_DEALER = 70041001;	//经销商
    public static final Integer BUSSNESS_TYPE_SERVICE = 70041002;	//服务商
    public static final Integer BUSSNESS_TYPE_PART = 70041003;		//配件商

    public static final Integer PART_PRICE_CHG_STATE = 9280;//配件价格变更状态
    public static final Integer PART_PRICE_CHG_STATE_01 = 92801001;//已保存
    public static final Integer PART_PRICE_CHG_STATE_02 = 92801002;//已提交
    public static final Integer PART_PRICE_CHG_STATE_03 = 92801003;//已审核
    public static final Integer PART_PRICE_CHG_STATE_04 = 92801004;//已驳回
    public static final Integer PART_PRICE_CHG_STATE_05 = 92801005;//已作废
    public static final String COMPLAY_INFO = "北汽幻速";

    /*************配件交货跟踪排序   2014-05-15 ***************************/
    public static final String SORT_BY_ORDER_TYPE = "ORDER_TYPE";//订单类型
    public static final String SORT_BY_PICK_ORDER_ID = "PICK_ORDER_ID";//拣配单号
    public static final String SORT_BY_DEALER_CODE = "DEALER_CODE";//服务商代码
    public static final String SORT_BY_CREATE_DATE = "CREATE_DATE";//销售单生成日期
    public static final String SORT_BY_ROW_NUM = "ROW_NUM";//行数
    public static final String SORT_BY_ROW_SQTY = "ROW_SQTY";//数量
    public static final String SORT_BY_PICK_PRINT = "PICK_PRINT";//拣配打印
    public static final String SORT_BY_PKG_STATUS = "PKG_STATUS";//包装状态
    public static final String SORT_BY_PKG_PRINT = "PKG_PRINT";//包装打印
    public static final String SORT_BY_TRANS_STATUS = "TRANS_STATUS";//发运状态
    public static final String SORT_BY_TRANS_PRINT = "TRANS_PRINT";//发运打印
    public static final String SORT_BY_OUT_DATE = "OUT_DATE";//出库日期
    public static final String SORT_BY_IS_OVER = "IS_OVER";//是否已完成
    /*************配件交货跟踪排序   2014-05-15 ***************************/

    //呼叫中心审核客户实销信息
    public static final Integer CALLCENTER_CHECK_STATUS = 1801;
    public static final Integer CALLCENTER_CHECK_STATUS_01 = 18011001;//待审核
    public static final Integer CALLCENTER_CHECK_STATUS_02 = 18011002;//已审核
    public static final Integer CALLCENTER_CHECK_STATUS_03 = 18011003;//已驳回

    //销售部审核客户实销信息
    public static final Integer SALESX_CHECK_STATUS = 1901;
    public static final Integer SALESX_CHECK_STATUS_01 = 19011001;//待审核
    public static final Integer SALESX_CHECK_STATUS_02 = 19011002;//已审核
    public static final Integer SALESX_CHECK_STATUS_03 = 19011003;//已驳回

    //返利类型定义
    public static final Integer FIN_RETURN_TYPE=1902;
    public static final Integer	FIN_RETURN_TYPE_MONTH = 19021001;//月度返利
    public static final Integer	FIN_RETURN_TYPE_CASH = 19021002; //现金打款
    public static final Integer	FIN_RETURN_TYPE_JITUAN = 19021003;//集团客户
    public static final Integer	FIN_RETURN_TYPE_QUARTER = 19021004;//季度补齐
    public static final Integer	FIN_RETURN_TYPE_SCSJ = 19021005;//试乘试驾售后服务车

    /************* 17.7.6 未在tc_code中查询到编码 ***************************/
    /*************特殊车辆报备状态 ZZQ 2014-05-29 ***************************/
//    public static final Integer SPECIAL_VEHICLE_REPORT_STATE = 9281;//
//    public static final Integer SPECIAL_VEHICLE_REPORT_STATE_01 = 92811001;//已保存
//    public static final Integer SPECIAL_VEHICLE_REPORT_STATE_02 = 92811002;//已提交
//    public static final Integer SPECIAL_VEHICLE_REPORT_STATE_03 = 92811003;//已审核
//    public static final Integer SPECIAL_VEHICLE_REPORT_STATE_04 = 92811004;//已驳回
//    public static final Integer SPECIAL_VEHICLE_REPORT_STATE_05 = 92811005;//已撤销

    //特殊车辆上报审核人员类型--试乘试驾(实际角色以TT_SPECIAL_VEHICLE_RELATION为准)
    public static final Integer SCSJ_STAFF_TYPE = 9282;
    //区域运营部
    public static final Integer SCSJ_STAFF_TYPE_01 = 92821001;
    //销售管理中心
    public static final Integer SCSJ_STAFF_TYPE_02 = 92821002;
    //总经理
    public static final Integer SCSJ_STAFF_TYPE_03 = 92821003;

    //特殊车辆上报审核人员类型--巡展(实际角色以TT_SPECIAL_VEHICLE_RELATION为准)
    public static final Integer XZ_STAFF_TYPE = 9284;
    //区域运营部
    public static final Integer XZ_STAFF_TYPE_01 = 92841001;
    //销售管理中心
    public static final Integer XZ_STAFF_TYPE_02 = 92841002;
    //总经理
    public static final Integer XZ_STAFF_TYPE_03 = 92841003;

    //特殊车辆上报审核人员类型--售后(实际角色以TT_SPECIAL_VEHICLE_RELATION为准)
    public static final Integer AFTER_SALE_STAFF_TYPE = 9283;
    //大区经理
    public static final Integer AFTER_SALE_STAFF_TYPE_01 = 92831001;
    //业务总监
    public static final Integer AFTER_SALE_STAFF_TYPE_02 = 92831002;
    //销售总监
    public static final Integer AFTER_SALE_STAFF_TYPE_03 = 92831003;
    //服务总监
    public static final Integer AFTER_SALE_STAFF_TYPE_04 = 92831004;
    //总经理
    public static final Integer AFTER_SALE_STAFF_TYPE_05 = 92831005;
    


//    public static final String  COMPLAY_INFO = "北汽幻速";
    /*************特殊车辆报备状态 ZZQ 2014-05-29 ***************************/

    //配件资金账户用途
    public static final Integer PART_ACCOUNT_PURPOSE_TYPE = 9563;
    public static final Integer PART_ACCOUNT_PURPOSE_TYPE_01 = 95631001; //普通配件款
    public static final Integer PART_ACCOUNT_PURPOSE_TYPE_02 = 95631002; //精品配件款

    //实销审核日志类型
    public static final Integer ACTUAL_DEALER_SUBMIT_LOG=1802;
    public static final Integer ACTUAL_DEALER_SUBMIT_LOG_01=18021001;//正常实销上报含首次驳回修改
    public static final Integer ACTUAL_DEALER_SUBMIT_LOG_02=18021002;//实销变更
    public static final Integer ACTUAL_DEALER_SUBMIT_LOG_03=18021003;//回访修改状态
    public static final Integer ACTUAL_DEALER_SUBMIT_LOG_04=18021004;//实销退车

    //实销审核类型
    public static final Integer ACTUAL_DEALER_SUBMIT=1803;
    public static final Integer ACTUAL_DEALER_SUBMIT_01=18031001; //正常上报含实销修改
    public static final Integer ACTUAL_DEALER_SUBMIT_02=18031002; //实销变更
    public static final Integer ACTUAL_DEALER_SUBMIT_03=18031003; //回访

    //紧急调件
    public static final Integer OLD_PART_BORROW=1804;
    public static final Integer OLD_PART_BORROW_01=18041001; //已保存
    public static final Integer OLD_PART_BORROW_02=18041002; //已下发
    public static final Integer OLD_PART_BORROW_03=18041003; //无效

    //实销退车车辆处理方式
    public static final Integer REPAIR_AS_QCAR=1805;
    public static final Integer REPAIR_AS_NEWCAR = 18051001; //故障车转商品车
    public static final Integer REPAIR_AS_SNDCAR = 18051002; //故障车转二手车
    public static final Integer REPAIR_AS_NOQCAR = 18051003; //故障车转脱保车

    //形象店建店支持审核角色
    public static final Integer VI_CONSTRUCT_AUDIT = 9285;
    //提报角色
    public static final Integer VI_CONSTRUCT_AUDIT_01 = 92851001;
    //渠道部
    public static final Integer VI_CONSTRUCT_AUDIT_02 = 92851002;
    //销售部
    public static final Integer VI_CONSTRUCT_AUDIT_03 = 92851003;
    //总经理
    public static final Integer VI_CONSTRUCT_AUDIT_04 = 92851004;
    public static final Integer VI_CONSTRUCT_AUDIT_05 = 92851005;

    //形象店建店支持审核状态
    public static final Integer VI_CONSTRUCT_STATUS = 9286;
    //保存
    public static final Integer VI_CONSTRUCT_STATUS_00 = 92861000;
    //销售部审核中
    public static final Integer VI_CONSTRUCT_STATUS_01 = 92861001;
    //审核通过
    public static final Integer VI_CONSTRUCT_STATUS_02 = 92861002;
    //驳回
    public static final Integer VI_CONSTRUCT_STATUS_03 = 92861003;
    //终止
    public static final Integer VI_CONSTRUCT_STATUS_04 = 92861004;
    
    //二级经销商审核角色
    public static final Integer DEALER_SECEND_AUDIT = 9287;
    //提报角色
    public static final Integer DEALER_SECEND_AUDIT_01 = 92871001;
    //区域经理
    public static final Integer DEALER_SECEND_AUDIT_02 = 92871002;
    //销售管理中心
    public static final Integer DEALER_SECEND_AUDIT_03 = 92871003;
    //总经理
    public static final Integer DEALER_SECEND_AUDIT_04 = 92871004;

    public static final Integer DEALER_SECEND_AUDIT_05 = 92871005;

    //二级经销商审核状态
    public static final Integer DEALER_SECEND_STATUS = 9290;
    //保存
    public static final Integer DEALER_SECEND_STATUS_01 = 92901001;
    //大区经理审核中
    public static final Integer DEALER_SECEND_STATUS_02 = 92901002;
    //销售管理中心审核中
    public static final Integer DEALER_SECEND_STATUS_03 = 92901003;
    //总经理审核中
    public static final Integer DEALER_SECEND_STATUS_04 = 92901004;
    //驳回
    public static final Integer DEALER_SECEND_STATUS_05 = 92901005;
    //总经理审核通过
    public static final Integer DEALER_SECEND_STATUS_06 = 92901006;
    //审核通过
    public static final Integer DEALER_SECEND_STATUS_07 = 92901007;

    //团购车用途
    public static final Integer group_buy_type = 9291;
    //企、事业单位工作用车
    public static final Integer group_buy_type_01 = 92911001;
    //租赁车
    public static final Integer group_buy_type_02 = 92911002;
    //承运车
    public static final Integer group_buy_type_03 = 92911003;
    //驾校教练车
    public static final Integer group_buy_type_04 = 92911004;
    //媒体用车
    public static final Integer group_buy_type_05 = 92911005;
    //公、检、法、司、部队、国家安全部门的公务用车
    public static final Integer group_buy_type_06 = 92911006;
    //其它
    public static final Integer group_buy_type_07 = 92911007;

    //团购车审核角色
    public static final Integer GROUP_BUY_AUDIT = 9292;
    //提报角色
    public static final Integer GROUP_BUY_AUDIT_01 = 92921001;
    //业务中心
    public static final Integer GROUP_BUY_AUDIT_02 = 92921002;
    //销售中心
    public static final Integer GROUP_BUY_AUDIT_03 = 92921003;
    //总经理
    public static final Integer GROUP_BUY_AUDIT_04 = 92921004;

    public static final Integer GROUP_BUY_AUDIT_05 = 92921005;

    //团购车审核状态
    public static final Integer GROUP_BUY_STATUS = 9293;
    //业务中心审核中
    public static final Integer GROUP_BUY_STATUS_02 = 92931002;
    //销售中心审核中
    public static final Integer GROUP_BUY_STATUS_03 = 92931003;
    //总经理审核中
    public static final Integer GROUP_BUY_STATUS_04 = 92931004;
    //驳回
    public static final Integer GROUP_BUY_STATUS_05 = 92931005;
    //业务中心审核通过
    public static final Integer GROUP_BUY_STATUS_06 = 92931006;
    //销售中心审核通过
    public static final Integer GROUP_BUY_STATUS_07 = 92931007;
    //总经理审核通过
    public static final Integer GROUP_BUY_STATUS_08 = 92931008;

    //索赔单审核配件状态
    public static final Integer PART_AUDIT_STATUS=9568;
    public static final Integer PART_AUDIT_STATUS_01 = 95681001; //未审核
    public static final Integer PART_AUDIT_STATUS_02 = 95681002; //挂起
    public static final Integer PART_AUDIT_STATUS_03 = 95681003; //审核通过
    public static final Integer PART_AUDIT_STATUS_04 = 95681004; //拒赔

    //商务政策类型
    public static final Integer POLICY_TYPE=9995;
    public static final Integer POLICY_TYPE_01 = 99951001; //常规商务政策
    public static final Integer POLICY_TYPE_02 = 99951002; //特殊商务政策

    //二级经销商状态
    public final static String DLR_SERVICE_STATUS_SECEND = "9996";
    public final static String DLR_SERVICE_STATUS_SECEND_01 = "99961001"; //正常
    public final static String DLR_SERVICE_STATUS_SECEND_02 = "99961002"; //注销
    public final static String DLR_SERVICE_STATUS_SECEND_03 = "99961003"; //待退
    public final static String DLR_SERVICE_STATUS_SECEND_04 = "99961004"; //试运营	
    public final static String DLR_SERVICE_STATUS_SECEND_05 = "99961005"; //升级	
    public final static String DLR_SERVICE_STATUS_SECEND_06 = "99961006"; //降级	

    //返利财务审核状态
    public static final Integer FIN_AUDIT_STATUS=2000;
    public static final Integer FIN_AUDIT_STATUS_01 = 20001001; //代提报状态
    public static final Integer FIN_AUDIT_STATUS_02 = 20001002; //待审核状态
    public static final Integer FIN_AUDIT_STATUS_03 = 20001003; //审核通过状态
    public static final Integer FIN_AUDIT_STATUS_04 = 20001004; //审核驳回状态

    //预录入财务审核状态
    public static final Integer FIN_INPUT_AUDIT_STATUS=2001;
    public static final Integer FIN_INPUT_AUDIT_STATUS_01 = 20011001; //保存
    public static final Integer FIN_INPUT_AUDIT_STATUS_02 = 20011002; //审核中
    public static final Integer FIN_INPUT_AUDIT_STATUS_03 = 20011003; //审核通过
    public static final Integer FIN_INPUT_AUDIT_STATUS_04 = 20011004; //驳回

    //验收形象店形式
    public static final Integer VI_CONSTRUCT_TYPE=2002;
    public static final Integer VI_CONSTRUCT_TYPE_01 = 20021001; //新建店
    public static final Integer VI_CONSTRUCT_TYPE_02 = 20021002; //改建店
    public static final Integer VI_CONSTRUCT_TYPE_03 = 20021003; //销服分体店

    //参数类型
    public static final Integer PARAMETER_TYPE=2004;
    public static final Integer PARAMETER_TYPE_01=20042001; //PDI参数

    //服务站信息变更审核状态
    public static final Integer SERVICE_CHANGE_STATUS=2005;
    public static final Integer SERVICE_CHANGE_STATUS_01 = 20051001; //服务站已提报
    public static final Integer SERVICE_CHANGE_STATUS_02 = 20051002; //1级审核通过
    public static final Integer SERVICE_CHANGE_STATUS_03 = 20051003; //2级审核通过
    public static final Integer SERVICE_CHANGE_STATUS_04 = 20051004; //驳回
    public static final Integer SERVICE_CHANGE_STATUS_05 = 20051005; //备用
    public static final Integer SERVICE_CHANGE_STATUS_06 = 20051006; //备用

    //服务站信息变更审核人员
    public static final Integer SERVICE_CHANGE_STAFF=2006;
    public static final Integer SERVICE_CHANGE_STAFF_01 = 20061001; //区域经理
    public static final Integer SERVICE_CHANGE_STAFF_02 = 20061002; //车厂

    //配件移库状态 add 20141218
    public static final Integer PART_MV_STATUS = 9295;
    public static final Integer PART_MV_STATUS_01 = 92951001; //已保存
    public static final Integer PART_MV_STATUS_02 = 92951002; //已提交
    public static final Integer PART_MV_STATUS_03 = 92951003; //已审核
    public static final Integer PART_MV_STATUS_04 = 92951004; //已出库
    public static final Integer PART_MV_STATUS_05 = 92951005; //已入库
    public static final Integer PART_MV_STATUS_06 = 92951006; //已作废
    
    
    public static final Integer PROBLEM_TYPE = 9997;
    public static final Integer PROBLEM_TYPE_01 = 99971001; //已处理
    public static final Integer PROBLEM_TYPE_02 = 99971002; //未处理
    
    public static final Integer CHOOSE_TYPE = 9343;
    public static final Integer CHOOSE_TYPE_01 = 93431001; //已处理
    public static final Integer CHOOSE_TYPE_02 = 93431002; //未处理
    
    public static final Integer STATUS_TYPE = 9344;
    public static final Integer STATUS_TYPE_03 = 93441003; //已回运
    public static final Integer STATUS_TYPE_04 = 93441004; //已审件
    public static final Integer STATUS_TYPE_05 = 93441005; //已结算
    
    //旧件回运延期状态
    public static final Integer STATUS_OLD_PART = 9345;
    public static final Integer STATUS_OLD_PART_01 = 93451001;
    public static final Integer STATUS_OLD_PART_02 = 93451002; 
    public static final Integer STATUS_OLD_PART_03 = 93451003; 
    public static final Integer STATUS_OLD_PART_04 = 93451004;
    
    //预授权状态
    public static final Integer STATUS_YSQ = 9346;
    public static final Integer STATUS_YSQ_01 = 93461001;//服务站待上报
    public static final Integer STATUS_YSQ_02 = 93461002;//服务站已上报
    public static final Integer STATUS_YSQ_03 = 93461003;//技术部审核通过
    public static final Integer STATUS_YSQ_04 = 93461004;//技术部审核退回
    public static final Integer STATUS_YSQ_05 = 93461005;//技术主管审核通过
    public static final Integer STATUS_YSQ_06 = 93461006;//技术主管审核退回
    public static final Integer STATUS_YSQ_07 = 93461007;//发动机审核通过
    public static final Integer STATUS_YSQ_08 = 93461008;//技术部撤销审核
    public static final Integer STATUS_YSQ_09 = 93461009;//技术部主管撤销审核
    
    public static final Integer SERVICE_DEALER_KP_TYPE=2007;
    public static final Integer SERVICE_DEALER_KP_TYPE01=20071001;
    public static final Integer SERVICE_DEALER_KP_TYPE02=20071002;
    public static final Integer SERVICE_DEALER_KP_TYPE03=20071003;
    
    public static final Integer MARKETING_FEE_AUDIT_STATUS=2008;
    public static final Integer MARKETING_FEE_AUDIT_STATUS01=20081001;  //保存
    public static final Integer MARKETING_FEE_AUDIT_STATUS02=20081002;	//审核驳回与保存状态一致
    public static final Integer MARKETING_FEE_AUDIT_STATUS03=20081003;  //已上报
    public static final Integer MARKETING_FEE_AUDIT_STATUS04=20081004;  //（区域）审核通过
    public static final Integer MARKETING_FEE_AUDIT_STATUS05=20081005;  //（大区）审核通过
    public static final Integer MARKETING_FEE_AUDIT_STATUS06=20081006;  //（市场部）审核通过
    
    //public static final Integer MARKETING_CLASS = 2009;			//活动大类
    public static final Integer Marketing_CLASS_01 = 2009;	//巡展活动
    public static final Integer Marketing_CLASS_02 = 2010;  //店头活动
    public static final Integer Marketing_CLASS_03 = 2011;  //服务活动
    public static final Integer Marketing_CLASS_04 = 2012;  //地方车展
    public static final Integer Marketing_CLASS_05 = 2013;  //媒体及非媒体投放
    public static final Integer Marketing_CLASS_06 = 2015;  //数字营销
    
    public static final Integer Marketing_CLASS_2009_01 = 20091001;	//巡展
    public static final Integer Marketing_CLASS_2009_02 = 20091002;	//定展
    
    public static final Integer Marketing_CLASS_2010_01 = 20101001;	//上市发布
    public static final Integer Marketing_CLASS_2010_02 = 20101002;	//车友会
    public static final Integer Marketing_CLASS_2010_03 = 20101003;	//店头促销
    
    public static final Integer Marketing_CLASS_2011_01 = 20111001;	//服务活动01
    public static final Integer Marketing_CLASS_2011_02 = 20111002;	//服务活动02
    public static final Integer Marketing_CLASS_2011_03 = 20111003;	//服务活动03
    
    public static final Integer Marketing_CLASS_2012_01 = 20121001;	//室内车展
    public static final Integer Marketing_CLASS_2012_02 = 20121002;	//室外车展
    
    public static final Integer Marketing_CLASS_2013_01 = 20131001;	//电视/移动电视
    public static final Integer Marketing_CLASS_2013_02 = 20131002;	//电台
    public static final Integer Marketing_CLASS_2013_03 = 20131003;	//报纸
    public static final Integer Marketing_CLASS_2013_04 = 20131004;	//户外广告
    public static final Integer Marketing_CLASS_2013_05 = 20131005;	//户外视频广告
    public static final Integer Marketing_CLASS_2013_06 = 20131006;	//物料制作
    
    public static final Integer Marketing_CLASS_2015_01 = 20151001;	//垂直网站采集
    public static final Integer Marketing_CLASS_2015_02 = 20151002;	//网络广告
    public static final Integer Marketing_CLASS_2015_03 = 20151003;	//短信群发
    public static final Integer Marketing_CLASS_2015_04 = 20151004;	//微信客户端
    public static final Integer Marketing_CLASS_2015_05 = 20151005;	//其他

     //服务经理人员角色ID
    public static final  String FWJL_ROLE_ID = "4000005644";
    public static final  String QYJL_ROLE_ID = "4000005842";//区域经理
    
    //对账函状态设置
    public static final Integer DLR_DUIZHANG_STATUS = 3001;
    public static final Integer DLR_DUIZHANG_STATUS_01 = 30011001; //初始状态--未下发
    public static final Integer DLR_DUIZHANG_STATUS_02 = 30011002; //已下发状态
    public static final Integer DLR_DUIZHANG_STATUS_03 = 30011003;		//经销商已反馈
    
    public static final Integer CRM_INCOMING_CALL_STATUS_2023 = 2023;	        //投诉咨询管理节点状态
    public static final Integer CRM_INCOMING_CALL_STATUS_2023_00 = 20231000;	//未跟进
    public static final Integer CRM_INCOMING_CALL_STATUS_2023_01 = 20231001;	//跟进
    public static final Integer CRM_INCOMING_CALL_STATUS_2023_02 = 20231002;	//阶段性闭环申请
    public static final Integer CRM_INCOMING_CALL_STATUS_2023_03 = 20231003;	//通过
    public static final Integer CRM_INCOMING_CALL_STATUS_2023_04 = 20231004;	//驳回
    public static final Integer CRM_INCOMING_CALL_STATUS_2023_05 = 20231005;	//完全闭环申请
    public static final Integer CRM_INCOMING_CALL_STATUS_2023_06 = 20231006;	//已关闭
    
    //合格证赎证状态
    public static final Integer HEGEZHENG_SHUZHENG_STATUS_2024 = 2024;	        //合格证赎证状态
    public static final Integer HEGEZHENG_SHUZHENG_STATUS_2024_01 = 20241001;   //合格证赎证已提交
    public static final Integer HEGEZHENG_SHUZHENG_STATUS_2024_02 = 20241002;   //合格证赎证驳回
    public static final Integer HEGEZHENG_SHUZHENG_STATUS_2024_03 = 20241003;   //合格证赎证已放证

    
    //特殊费用状态
    public static final Integer SPE_STATUS = 2033;	       
    public static final Integer SPE_STATUS_01 = 20331001;  //保存 
    public static final Integer SPE_STATUS_02 = 20331002;  //上报
    public static final Integer SPE_STATUS_03 = 20331003;  //服务经理通过
    public static final Integer SPE_STATUS_04 = 20331004;  //服务经理退回
    public static final Integer SPE_STATUS_05 = 20331005;  //区域经理通过
    public static final Integer SPE_STATUS_06 = 20331006;  //区域经理退回
    public static final Integer SPE_STATUS_07 = 20331007;  //区域总监通过
    public static final Integer SPE_STATUS_08 = 20331008;  //区域总监退回
    public static final Integer SPE_STATUS_09 = 20331009;  //技术支持部通过
    public static final Integer SPE_STATUS_10 = 20331010;  //技术支持部退回
    public static final Integer SPE_STATUS_11 = 20331011;  //索赔结算通过
    public static final Integer SPE_STATUS_12 = 20331012;  //索赔结算退回
    public static final Integer SPE_STATUS_13 = 20331013;  //审核退回
    public static final Integer SPE_STATUS_14 = 20331014;  //审核通过
    public static final Integer SPE_STATUS_15 = 20331015;  //审核拒绝

    
    //车辆库存
    public static final Integer WAREHOUSE_TYPEID = 88;  //整车库存
    public static final Integer WAREHOUSE_TYPEID_01 = 8807;  //整车内销库
    public static final Integer WAREHOUSE_TYPEID_02 = 8809;  //幻速商品车库
    public static final Integer WAREHOUSE_TYPEID_03 = 8814;  //幻速商品车二库
    public static final Integer WAREHOUSE_TYPEID_04 = 8833;  //商品车异地退回库


    // 二级网络性质
    public static final Integer SECOND_LEVEL_NETWORK_NATURE = 2034;
    public static final Integer SECOND_LEVEL_NETWORK_NATURE_01 = 20341001; // 直营
    public static final Integer SECOND_LEVEL_NETWORK_NATURE_02 = 20341002; // 联营
    public static final Integer SECOND_LEVEL_NETWORK_NATURE_03 = 20341003; // 其它
    
    // 服务网点性质
    public static final Integer SERVICE_NETWORK_NATURE = 2035;
    public static final Integer SERVICE_NETWORK_NATURE_01 = 20351001; // 自有
    public static final Integer SERVICE_NETWORK_NATURE_02 = 20351002; // 合作
    
    // 维修资质
    public static final Integer REPAIR_APTITUDE = 2036;
    public static final Integer REPAIR_APTITUDE_01 = 20361001; // 一类
    public static final Integer REPAIR_APTITUDE_02 = 20361002; // 二类
    public static final Integer REPAIR_APTITUDE_03 = 20361003; // 三类
    
    // 星级申报
    public static final Integer LEVEL_REPORT = 2037;
    public static final Integer LEVEL_REPORT_03 = 20371003; // 三星级
    public static final Integer LEVEL_REPORT_04 = 20371004; // 四星级
    public static final Integer LEVEL_REPORT_05 = 20371005; // 五星级
    public static final Integer LEVEL_REPORT_06 = 20371006; // 六星级
    public static final Integer LEVEL_REPORT_07 = 20371007; // 七星级

    // 二级网络性质
    public static final Integer NEW_RO_TYPE = 9333;
    public static final Integer NEW_RO_TYPE_01 = 93331001; 
    public static final Integer NEW_RO_TYPE_02 = 93331002; 
    public static final Integer NEW_RO_TYPE_03 = 93331003; 
    
    //预授权件类型
    public static final Integer YSQ_PART_TYPE = 7231;
    public static final Integer YSQ_PART_TYPE_01 = 72311001;//易损件
    public static final Integer YSQ_PART_TYPE_02 = 72311002; //留存件
    
    // 赊销授信审核
    public static final Integer CREDIT_AUDIT = 1288;
    public static final Integer CREDIT_AUDIT_01 = 12881001; // 已保存
    public static final Integer CREDIT_AUDIT_02 = 12881002; // 待财务审核
    public static final Integer CREDIT_AUDIT_03 = 12881003; // 审核通过
    public static final Integer CREDIT_AUDIT_04 = 12881004; // 已驳回
    
    /**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 描述:银翔原型报错修复
     * 实际意义未详
     * Ded：000000000
     * Date:2017-06-29
     */
    public static final Integer STATION_HEAD = 00000000;
    public static final Integer AEGIS = 00000000;
    public static final String areaIdJJ = "00000000";
    public static final String areaIdHF = "00000000" ;
    
    /*******************张磊17.7.6新增配件基础数据*****************************/

//    /**
//     * 配件主数据维护-配件种类-9500
//     */
//    public static final Integer ZT_PB_PART_TYPE = 9500;
//    /**
//     * 配件主数据-配件种类-自制件-95001001
//     */
//    public static final Integer ZT_PB_PART_TYPE_SELF_MADE = 95001001; 
//    /**
//     * 配件主数据-配件种类-配套件-95001002
//     */
//    public static final Integer ZT_PB_PART_TYPE_SUPPORTING = 95001002; 
    
    /**
     * 配件主数据维护-装配-9570
     */
    public static final Integer ZT_PB_PART_FIT = 9570;
    /**
     * 配件主数据维护-装配-总装-95701001
     */
    public static final Integer ZT_PB_PART_FIT_ASSEMBLY = 95701001;
    /**
     * 配件主数据维护-装配-二次装配-95701003
     */
    public static final Integer ZT_PB_PART_FIT_SECOND = 95701003;
    
    /**
     * 配件主数据维护-类别-9571
     */
    public static final Integer ZT_PB_PART_CATEGORY = 9571;
    /**
     * 配件主数据维护-类别-配件-95711001
     */
    public static final Integer ZT_PB_PART_CATEGORY_FITTING = 95711001;
    /**
     * 配件主数据维护-类别-辅料-95711002
     */
    public static final Integer ZT_PB_PART_CATEGORY_ACCESSORIES = 95711002;
    /**
     * 配件主数据维护-类别-精装-95711003
     */
    public static final Integer ZT_PB_PART_CATEGORY_HARDCOVER = 95711003;
    
    /**
     * 采购方式
     */
    Integer PURCHASE_WAY = 9281;
    Integer PURCHASE_WAY_01 =92811001;//总装车 厂采购
    Integer PURCHASE_WAY_02 =92811002;//专用车采购
    Integer PURCHASE_WAY_03 =92811003;//供应商采购
    Integer PURCHASE_WAY_04 =92811004;//中储采购
    Integer PURCHASE_WAY_05 =92811005;//01自制件采购(已作废)
    Integer PURCHASE_WAY_06 =92811006;//自制件采购
    Integer PURCHASE_WAY_07 =92811007;//自制件采购
    Integer PURCHASE_WAY_08 =92811008;//自制件采购
    Integer PURCHASE_WAY_09 =92811009;//自制件采购
    
//    public static final Integer PURCHASE_WAY_01 = 92811001;// 客服采购 
//    public static final Integer PURCHASE_WAY_02 = 92811002;// 三工厂采购 
//    public static final Integer PURCHASE_WAY_03 = 92811003;// 四工厂采购 
//    public static final Integer PURCHASE_WAY_04 = 92811004;// 五工厂采购 
//    public static final Integer PURCHASE_WAY_05 = 92811005;// 北京采购 
//    public static final Integer PURCHASE_WAY_06 = 92811006;// 合肥采购 
//    public static final Integer PURCHASE_WAY_07 = 92811007;// 铃木采购 
//    public static final Integer PURCHASE_WAY_08 = 92811008;// 河北采购 
//    public static final Integer PURCHASE_WAY_09 = 92811009;// 福特采购 
//    public static final Integer PURCHASE_WAY_10 = 92811010;// 东安动力 
//    public static final Integer PURCHASE_WAY_11 = 92811011;// 东安三菱 
//    public static final Integer PURCHASE_WAY_12 = 92811012;// 南京采购 
//    public static final Integer PURCHASE_WAY_13 = 92811013;// 新能源采购 
//    public static final Integer PURCHASE_WAY_14 = 92811014;// 江铃采购    
    
    //public static final Integer ZT_PB_PART_PROCUREMENT_SITE = 9281;
    //public static final Integer ZT_PB_PART_PROCUREMENT_SITE_01 = 92811001;
    //public static final Integer ZT_PB_PART_PROCUREMENT_SITE_02 = 92811002;
    //public static final Integer ZT_PB_PART_PROCUREMENT_SITE_03 = 92811003;

    // 上级采购单位
//    public static final Integer PURCHASE_TYPE = 9714;
//    public static final Integer  PURCHASE_TYPE_01 = 97141001; // 北京工厂
//    public static final Integer  PURCHASE_TYPE_02 = 97141002; // 福特工厂
//    public static final Integer  PURCHASE_TYPE_03 = 97141003; // 股份公司
//    public static final Integer  PURCHASE_TYPE_04 = 97141004; // 合肥工厂
//    public static final Integer  PURCHASE_TYPE_05 = 97141005; // 河北工厂
//    public static final Integer  PURCHASE_TYPE_06 = 97141006; // 客服公司
//    public static final Integer  PURCHASE_TYPE_07 = 97141007; // 铃木工厂
//    public static final Integer  PURCHASE_TYPE_08 = 97141008; // 南京工厂
//    public static final Integer  PURCHASE_TYPE_09 = 97141009; // 东安动力
//    public static final Integer  PURCHASE_TYPE_10 = 97141010; // 东安三菱
//    public static final Integer  PURCHASE_TYPE_11 = 97141011; // 新能源公司
//    public static final Integer  PURCHASE_TYPE_12 = 97141012; // 江铃工厂
    
    /**
     * 配件替换件类型-9572
     */
    public static final Integer ZT_PB_PART_REPLACE_TYPE = 9572;
    /**
     * 配件替换件类型-- 部分替换 --95721001
     */
    public static final Integer ZT_PB_PART_REPLACE_TYPE_01 = 95721001;
    /**
     * 配件替换件类型-- 完全替换 --95721002
     */
    public static final Integer ZT_PB_PART_REPLACE_TYPE_02 = 95721002;
    /**
     * 配件替换件类型-- 旧-新，新×旧 --9572100
     */
    //public static final Integer ZT_PB_PART_REPLACE_TYPE_03 = 95721003;
    /**
     * 配件替换件类型-- 旧×新，新-旧 --9572100
     */
    //public static final Integer ZT_PB_PART_REPLACE_TYPE_04 = 95721004;
    
    /**
     * 合同类型 - 9573
     */
    public static final Integer CONTRACT_TYPE = 9573;
    /**
     * 合同类型 - 基础合同 - 95731001
     */
    public static final Integer CONTRACT_TYPE_01 = 95731001;
    /**
     * 合同类型 - 订单合同 - 95731002
     */
    public static final Integer CONTRACT_TYPE_02 = 95731002;
    
    /**
     * 配件调整申请状态  - 9574
     */
    public static final Integer PART_ABJUSTMENT_APPLY = 9574;
    /**
     * 配件调整申请状态 - 未申请 - 95731001
     */
    public static final Integer PART_ABJUSTMENT_APPLY_01 = 95741001;
    /**
     * 合同类型 - 已申请 - 95731002
     */
    public static final Integer PART_ABJUSTMENT_APPLY_02 = 95741002;
    
    /**
     * 配件调整审核状态  - 9575
     */
    public static final Integer PART_ABJUSTMENT_CHECK = 9575;
    /**
     * 配件调整审核状态 - 未审核 - 95751001
     */
    public static final Integer PART_ABJUSTMENT_CHECK_01 = 95751001;
    /**
     * 配件调整审核状态 - 已审核 - 95751002
     */
    public static final Integer PART_ABJUSTMENT_CHECK_02 = 95751002;
    
    /**
     * 配件调整审核处理状态   - 9576
     */
    public static final Integer PART_ABJUSTMENT_CHECK_DEAL_STATUS = 9576;
    /**
     * 配件调整审核处理状态  - 通过 - 95761001
     */
    public static final Integer PART_ABJUSTMENT_CHECK_DEAL_STATUS_01 = 95761001;
    /**
     * 配件调整审核处理状态  - 驳回 - 95761002
     */
    public static final Integer PART_ABJUSTMENT_CHECK_DEAL_STATUS_02 = 95761002;
    
    /**
     * 配件库存调整状态   - 9577
     */
    public static final Integer PART_ABJUSTMENT_STATE = 9577;
    /**
     * 配件调整审核处理状态  - 未提交申请 - 95771001
     */
    public static final Integer PART_ABJUSTMENT_STATE_01 = 95771001;
    /**
     * 配件调整审核处理状态  - 审核中 - 95771002
     */
    public static final Integer PART_ABJUSTMENT_STATE_02 = 95771002;
    /**
     * 配件调整审核处理状态  - 审核完成 - 95771003
     */
    public static final Integer PART_ABJUSTMENT_STATE_03 = 95771003;
    
    /**
     * 配件调整类型   - 9578
     */
    public static final Integer PART_ABJUSTMENT_TYPE = 9578;
    /**
     * 配件调整类型  - 添加 - 95781001
     */
    public static final Integer PART_ABJUSTMENT_TYPE_01 = 95781001;
    /**
     * 配件调整类型  - 减少 - 95781002
     */
    public static final Integer PART_ABJUSTMENT_TYPE_02 = 95781002;
    /**
     * 配件退货审核等级   - 9579
     */
    public static final Integer PART_RETURN_CHK_LEVEL = 9578;
    /**
     * 配件退货审核等级  - 一级审核 - 95791001
     */
    public static final Integer PART_RETURN_CHK_LEVEL_01 = 95791001;
    /**
     * 配件退货审核等级  - 二级审核 - 95781002
     */
    public static final Integer PART_RETURN_CHK_LEVEL_02 = 95791002;
    /**
     * 配件退货审核等级  - 三级审核 - 95781003
     */
    public static final Integer PART_RETURN_CHK_LEVEL_03 = 95791003;

    /**
     * 退货解封处理状态
     */
    public static final Integer RC_JF_STATE = 9723;
    /**
     * 已作废
     */
    public static final Integer RC_JF_STATE_00 = 97231000;
    /**
     * 已保存
     */
    public static final Integer RC_JF_STATE_01 = 97231001;
    /**
     * 已提交
     */
    public static final Integer RC_JF_STATE_02 = 97231002;
    /**
     * 已审核
     */
    public static final Integer RC_JF_STATE_03 = 97231003;
    /**
     * 已驳回
     */
    public static final Integer RC_JF_STATE_04 = 97231004;
    /**
     * 已解封
     */
    public static final Integer RC_JF_STATE_05 = 97231005;
    
    /**
     * 解封类型
     */
    public static final Integer RC_JF_TYPE = 9724;
    /**
     * 退货解封
     */
    public static final Integer RC_JF_TYPE_01 = 97241001;
    /**
     * 换货解封
     */
    public static final Integer RC_JF_TYPE_02 = 97241002;
    
//    /**
//     * 配件计划周期 - 9573
//     */
//    public static final Integer PART_PLAN_CYCLE = 9573;
//    /**
//     * 配件计划周期 -  - 95731001
//     */
//    public static final Integer PART_PLAN_CYCLE_01 = 95731001;
//    /**
//     * 配件计划周期 -  - 95731002
//     */
//    public static final Integer PART_PLAN_CYCLE_02 = 95731002;
//    /**
//     * 配件计划周期 -  - 95731003
//     */
//    public static final Integer PART_PLAN_CYCLE_03 = 95731003;
//    /**
//     * 配件计划周期 -  - 95731004
//     */
//    public static final Integer PART_PLAN_CYCLE_04 = 95731004;
//    /**
//     * 配件计划周期 -  - 95731005
//     */
//    public static final Integer PART_PLAN_CYCLE_05 = 95731005;
//    /**
//     * 配件计划周期 -  - 95731006
//     */
//    public static final Integer PART_PLAN_CYCLE_06 = 95731006;
//    /**
//     * 配件计划周期 -  - 95731007
//     */
//    public static final Integer PART_PLAN_CYCLE_07 = 95731007;
    
    
    
    
    public static final Integer SYSTEM_MODULE = 1001 ;
    public static final Integer SYSTEM_BUSINESS = 1002 ;
  //日报表状态
  	public static final Integer DAILY_STATUS=1320;
  	public static final Integer DAILY_STATUS_UNCONFIRM=13201001;//未提交
  	public static final Integer DAILY_STATUS_CONFIRM=13201002;//已提交
  	public static final Integer DAILY_STATUS_DELETE=13201003;//删除
    
  	/** *************yinshunhui 商务支持审核状态************ */
	public static final Long SUPPORT_INFO_STATUS = Long.parseLong("1996");// 商务支持审核
	public static final Long SUPPORT_INFO_STATUS_01 = Long.parseLong("19961001");// 待小区审核
	public static final Long SUPPORT_INFO_STATUS_02 = Long.parseLong("19961002");// 待大区审核
	public static final Long SUPPORT_INFO_STATUS_03 = Long.parseLong("19961003");// 待车厂审核
	public static final Long SUPPORT_INFO_STATUS_04 = Long.parseLong("19961004");// 审核通过
	public static final Long SUPPORT_INFO_STATUS_05 = Long.parseLong("19961005");// 驳回
	public static final Long SUPPORT_INFO_STATUS_06 = Long.parseLong("19961006");// 终止
	
	
	public static final Integer PLAN_CHECK_STATUS = 1040;// 市场活动审批状态
	public static final Integer PLAN_CHECK_STATUS_01 = 10401001;// 车厂已下发
	public static final Integer PLAN_CHECK_STATUS_02 = 10401002;// 事业部提报
	public static final Integer PLAN_CHECK_STATUS_03 = 10401003;// 区域审核通过
	public static final Integer PLAN_CHECK_STATUS_04 = 10401004;// 市场部审核通过
	public static final Integer PLAN_CHECK_STATUS_05 = 10401005;// 方案审核完成
	public static final Integer PLAN_CHECK_STATUS_06 = 10401006;// 方案已驳回
	public static final Integer PLAN_CHECK_STATUS_07 = 10401007;// 方案已取消
	
	//战败审核类型
	public static final Integer RESPOND_STATUS_TYPE= 9001;
	public static final Integer RESPOND_STATUS_TYPE_01 = 90011001;//已提报
	public static final Integer RESPOND_STATUS_TYPE_02 = 90011002;//初审通过
	public static final Integer RESPOND_STATUS_TYPE_03 = 90011003;//终审通过
	public static final Integer RESPOND_STATUS_TYPE_04 = 90011004;//已驳回
	
	//经销商活动总结费用申请状态
	public static final Integer SUMMERY_STATUS = 1329;
	public static final Integer SUMMERY_STATUS_01 = 13291001; // 活动总结已保存
	public static final Integer SUMMERY_STATUS_02 = 13291002; // 待大区审核
	public static final Integer SUMMERY_STATUS_03 = 13291003; // 待车厂审核
	public static final Integer SUMMERY_STATUS_04 = 13291004; // 大区审核通过
	public static final Integer SUMMERY_STATUS_05 = 13291005; // 大区审核驳回
	public static final Integer SUMMERY_STATUS_06 = 13291006; // 车厂审核通过
	public static final Integer SUMMERY_STATUS_07 = 13291007; // 车厂审核驳回
	
	public static Integer IS_RECEIVE_0 = 30021000; // 否
	public static Integer IS_RECEIVE_1 = 30021001; // 是
	
	public static Integer ALLOCATE_STATUS_01 = 30031001; // 调拨通知
	public static Integer ALLOCATE_STATUS_02 = 30031002; // 调拨出库
	public static Integer ALLOCATE_STATUS_03 = 30031003; // 调拨入库
	
	//经理日报表车系数据维护
	public static final String carType[]={"奥拓","羚羊","雨燕","天语两厢","天语三厢","锋驭","启悦"};
	
	//职位级别
	public static final Integer DEALER_USER_LEVEL = 6028;
	public static final Integer DEALER_USER_LEVEL_01 = 60281001;//总经理
	public static final Integer DEALER_USER_LEVEL_02 = 60281002;//销售经理
	public static final Integer DEALER_USER_LEVEL_03 = 60281003;//销售主管
	public static final Integer DEALER_USER_LEVEL_04 = 60281004;//销售顾问
	public static final Integer DEALER_USER_LEVEL_05 = 60281005;//dcrc
	//线索来源
	public static final Integer LEADS_SOURCE = 6015;
	public static final Integer LEADS_SOURCE_01 = 60151001;//来电
	public static final Integer LEADS_SOURCE_02 = 60151002;//来电
	public static final Integer LEADS_SOURCE_03 = 60151003;//官网
	public static final Integer LEADS_SOURCE_04 = 60151004;//两车平台
	public static final Integer LEADS_SOURCE_05 = 60151005;//400
	//线索类型
	public static final Integer LEADS_TYPE= 6014;
	public static final Integer LEADS_TYPE_01 = 60141001;//车厂导入
	public static final Integer LEADS_TYPE_02 = 60141002;//DCRC录入
	public static final Integer LEADS_TYPE_03 = 60141003;//顾问录入
	public static final Integer LEADS_TYPE_04 = 60141004;//经销商导入
	//意向等级
	public static final Integer INTENT_TYPE= 6010;
	public static final Integer INTENT_TYPE_H = 60101001;//H
	public static final Integer INTENT_TYPE_A = 60101002;//A
	public static final Integer INTENT_TYPE_B = 60101003;//B
	public static final Integer INTENT_TYPE_C = 60101004;//C
	public static final Integer INTENT_TYPE_O = 60101005;//O
	public static final Integer INTENT_TYPE_E = 60101006;//E
	public static final Integer INTENT_TYPE_L = 60101007;//L
	//审核结果
	public static final Integer AUDIT_RESULT = 6030;
	public static final Integer AUDIT_RESULT_01 = 60301001;//同意
	public static final Integer AUDIT_RESULT_02 = 60301002;//不同意
	//来电契机
	public static final Integer COME_REASON= 6003;
	public static final Integer COME_REASON_01 = 60031001;//网络
	public static final Integer COME_REASON_02 = 60031002;//电视
	public static final Integer COME_REASON_03 = 60031003;//报刊	
	public static final Integer COME_REASON_04 = 60031004;//杂志
	
	//线索来源(DCRC客流录入页面用)
	public static final Integer LEADS_SOURCE2 = 6031;
	public static final Integer LEADS_SOURCE2_01 = 60311001;//来电
	public static final Integer LEADS_SOURCE2_02 = 60311002;//来店

	//线索状态
	public static final Integer LEADS_STATUS= 6016;
	public static final Integer LEADS_STATUS_01 = 60161001;//有效
	public static final Integer LEADS_STATUS_02 = 60161002;//战败
	public static final Integer LEADS_STATUS_03 = 60161003;//失效
	public static final Integer LEADS_STATUS_04 = 60161004;//无效
	public static final Integer LEADS_STATUS_05 = 60161005;//重复线索

	//客流状态
	public static final Integer CUSTOMER_STATUS= 6016;
	public static final Integer CUSTOMER_STATUS_01 = 60161001;//有效
	public static final Integer CUSTOMER_STATUS_02 = 60161002;//无效

	//分派状态
	public static final Integer ALLOT_STATUS= 6029;
	public static final Integer ALLOT_STATUS_01 = 60291001;//未分派
	public static final Integer ALLOT_STATUS_02 = 60291002;//已分派
	
	//购车方式
	public static final Long BUY_TYPE= new Long(6005);
	public static final Long BUY_TYPE_01= new Long(60051001);//车贷
	public static final Long BUY_TYPE_02= new Long(60051002);//置换
	public static final Long BUY_TYPE_03= new Long(60051003);//现金
	public static final Long BUY_TYPE_04= new Long(60051004);//车贷+置换
	//兴趣爱好
	public static final Long INTEREST_TYPE= new Long(6001);
	public static final Long INTEREST_TYPE_01= new Long(60011002);//体育
	public static final Long INTEREST_TYPE_02= new Long(60011003);//音乐
	public static final Long INTEREST_TYPE_03= new Long(60011002);//休闲
	public static final Long INTEREST_TYPE_04= new Long(60011003);//棋牌
	//政治面貌
	public static final Long POLITICAL_TYPE= new Long(6011);
	public static final Long POLITICAL_TYPE_01= new Long(60111002);//党员
	public static final Long POLITICAL_TYPE_02= new Long(60111003);//团员
	public static final Long POLITICAL_TYPE_03= new Long(60111002);//无
	//关注因素
	public static final Long CONCERN_FAX= new Long(6007);
	public static final Long CONCERN_FAX_01= new Long(60071001);//品牌
	public static final Long CONCERN_FAX_02= new Long(60071002);//质量
	public static final Long CONCERN_FAX_03= new Long(60071003);//价格
	public static final Long CONCERN_FAX_04= new Long(60071004);//外观
	public static final Long CONCERN_FAX_05= new Long(60071005);//内饰
	public static final Long CONCERN_FAX_06= new Long(60071006);//舒适性
	public static final Long CONCERN_FAX_07= new Long(60071007);//动力
	//意向颜色
	public static final Long INTENT_COLOR= new Long(6006);
	public static final Long INTENT_COLOR_01= new Long(60061001);//红
	public static final Long INTENT_COLOR_02= new Long(60061002);//黄
	public static final Long INTENT_COLOR_03= new Long(60061003);//蓝
	public static final Long INTENT_COLOR_04= new Long(60061004);//黑
	public static final Long INTENT_COLOR_05= new Long(60061005);//银灰
	public static final Long INTENT_COLOR_06= new Long(60061006);//咖啡
	//战败类型
	public static final Long DEFEAT_TYPE= new Long(6033);
	public static final Long DEFEAT_TYPE_01= new Long(60331001);//产品
	public static final Long DEFEAT_TYPE_02= new Long(60331002);//流程
	//客户类型
	public static final Long CTM_TYPE= new Long(6034);
	public static final Long CTM_TYPE_01= new Long(60341001);//保有
	public static final Long CTM_TYPE_02= new Long(60341002);//有望
	public static final Long CTM_TYPE_03= new Long(60341003);//重购客户
	public static final Long CTM_TYPE_04= new Long(60341004);//战败
	public static final Long CTM_TYPE_05= new Long(60341005);//失效
	//客户性质
	public static final Long CTM_PROP= new Long(6035);
	public static final Long CTM_PROP_01= new Long(60351001);//个人
	public static final Long CTM_PROP_02= new Long(60351002);//单位
	public static final Long CTM_PROP_03= new Long(60351002);//团购
	//索赔结算状态
	public static final Integer CLAIM_STAUTS= new Integer(6077);
	public static final Integer CLAIM_STAUTS_01= new Integer(60771001);//已保存
	public static final Integer CLAIM_STAUTS_02= new Integer(60771002);//已上报
	public static final Integer CLAIM_STAUTS_03= new Integer(60771003);//已验收
	public static final Integer CLAIM_STAUTS_04= new Integer(60771004);//已退回
	//顾问是否确认
	public static final Integer ADVISER_CONFIRM= 6032;
	public static final Integer ADVISER_CONFIRM_01 = 60321001;//待确认
	public static final Integer ADVISER_CONFIRM_02 = 60321002;//已确认
	
	//集客方式
	public static final Integer COLLECT_FASHION= 6002;
	public static final Integer COLLECT_FASHION_01 = 60021001;//首次自然来店客户
	public static final Integer COLLECT_FASHION_02 = 60021002;//邀约再次来店客户
	public static final Integer COLLECT_FASHION_03 = 60021003;//通过线索邀约第一次来店客户
	public static final Integer COLLECT_FASHION_04 = 60021004;//转介绍第一次来店客户
	
	//邀约类型
	public static final Integer INVITE_WAY= 6021;
	public static final Integer INVITE_WAY_01 = 60211001;//线索邀约
	public static final Integer INVITE_WAY_02 = 60211002;//再次邀约
	
	//邀约方式
	public static final Integer INVITE_TYPE= 6047;
	public static final Integer INVITE_TYPE_01 = 60471001;//电话
	public static final Integer INVITE_TYPE_02 = 60471002;//短信
	public static final Integer INVITE_TYPE_03 = 60471003;//微信
	public static final Integer INVITE_TYPE_04 = 60471004;//微博
	public static final Integer INVITE_TYPE_05 = 60471005;//QQ
	public static final Integer INVITE_TYPE_06 = 60471006;//上门拜访
	public static final Integer INVITE_TYPE_07 = 60471007;//DMS邮寄
	
	//客户状态
	public static final Integer CTM_STATUS= 6036;
	public static final Integer CTM_STATUS_01 = 60361001;//有效
	public static final Integer CTM_STATUS_02 = 60361002;//战败
	public static final Integer CTM_STATUS_03 = 60361003;//失效
	public static final Integer CTM_STATUS_04 = 60361004;//无效
	
	//任务状态
	public static final Integer TASK_STATUS= 6017;
	public static final Integer TASK_STATUS_01 = 60171001;//进行中
	public static final Integer TASK_STATUS_02 = 60171002;//已完成
	public static final Integer TASK_STATUS_03 = 60171003;//无效
	public static final Integer TASK_STATUS_04 = 60171004;//锁定
	
	
	//交车状态
	public static final Integer delivery_status= 6057;
	public static final Integer delivery_status_01 = 60571001;//已交车
	public static final Integer delivery_status_02 = 60571002;//已上报
	public static final Integer delivery_status_03 = 60571003;//待退车
	public static final Integer delivery_status_04 = 60571004;//已退车
	
	//与车主的关系
	public static final Integer linkMan_status= 6058;
	public static final Integer linkMan_status_01 = 60581001;//老客户推荐
	public static final Integer linkMan_status_02 = 60581002;//情报转介绍
	
	//购买类型
	public static final Integer BUY_TYPE2= 6009;
	public static final Integer BUY_TYPE2_01 = 60091001;//新增
	public static final Integer BUY_TYPE2_02 = 60091002;//本品牌换购
	public static final Integer BUY_TYPE2_03 = 60091003;//增购
	public static final Integer BUY_TYPE2_04 = 60091004;//他品牌换购
	public static final Integer BUY_TYPE2_05 = 60091005;//正在摇号
	public static final Integer BUY_TYPE2_06 = 60091006;//中签
	public static final Integer BUY_TYPE2_07 = 60091007;//报废
	public static final Integer BUY_TYPE2_08 = 60091008;//其他

	//销售流程进度
	public static final Integer SALSE_PROGRESS= 6009;
	public static final Integer SALSE_PROGRESS_01 = 60091001;//线索开拓
	public static final Integer SALSE_PROGRESS_02 = 60091002;//客户接待
	public static final Integer SALSE_PROGRESS_03 = 60091003;//需求分析
	public static final Integer SALSE_PROGRESS_04 = 60091004;//产品说明
	public static final Integer SALSE_PROGRESS_05 = 60091005;//试乘试驾
	public static final Integer SALSE_PROGRESS_06 = 60091006;//报价成交
	public static final Integer SALSE_PROGRESS_07 = 60091007;//完美交车
	public static final Integer SALSE_PROGRESS_08 = 60091008;//跟踪回访
	
	//主管审核
	public static final Integer DIRECTOR_AUDIT= 6019;
	public static final Integer DIRECTOR_AUDIT_01 = 60191001;//未审核
	public static final Integer DIRECTOR_AUDIT_02 = 60191002;//审核通过
	public static final Integer DIRECTOR_AUDIT_03 = 60191003;//审核驳回
	public static final Integer DIRECTOR_AUDIT_04 = 60191004;//无需审核
	
	//订单状态
	public static final Integer TPC_ORDER_STATUS= 6023;
	public static final Integer TPC_ORDER_STATUS_01 = 60231001;//有效
	public static final Integer TPC_ORDER_STATUS_02 = 60231002;//部分交车
	public static final Integer TPC_ORDER_STATUS_03 = 60231003;//修改锁定
	public static final Integer TPC_ORDER_STATUS_04 = 60231004;//退单锁定
	public static final Integer TPC_ORDER_STATUS_05 = 60231005;//完成交车
	public static final Integer TPC_ORDER_STATUS_06 = 60231006;//无效
	public static final Integer TPC_ORDER_STATUS_07 = 60231007;//退单
	public static final Integer TPC_ORDER_STATUS_08 = 60231008;//新增待审核
	public static final Integer TPC_ORDER_STATUS_09 = 60231009;//修改锁定待车厂审核
	public static final Integer TPC_ORDER_STATUS_10 = 60231010;//退单锁定待车厂审核
	public static final Integer TPC_ORDER_STATUS_11 = 60231011;//新增审核驳回
	//成交类型
	public static final Integer DEAL_TYPE= 6038;
	public static final Integer DEAL_TYPE_01 = 60381001;//A
	public static final Integer DEAL_TYPE_02 = 60381002;//B
	//实效类型
	public static final Integer FAILURE_TYPE= 6039;
	public static final Integer FAILURE_TYPE_01 = 60391001;//战败
	public static final Integer FAILURE_TYPE_02 = 60391002;//实效
	//审核是战败实效单据的状态
	public static final Integer FAILURE_AUDIT= 6040;
	public static final Integer FAILURE_AUDIT_01 = 60401001;//未审核
	public static final Integer FAILURE_AUDIT_02 = 60401002;//经理通过
	public static final Integer FAILURE_AUDIT_03 = 60401003;//经理驳回并重新分配
	public static final Integer FAILURE_AUDIT_04 = 60401004;//dcrc通过
	public static final Integer FAILURE_AUDIT_05 = 60401005;//dcrc驳回
	//战败形式
	public static final Integer DEFEAT_WAY= 6041;
	public static final Integer DEFEAT_WAY_01 = 60411001;//本品
	public static final Integer DEFEAT_WAY_02 = 60411002;//它品
	
	//(订单退订)修改类型
	public static final Integer UPDATE_TYPE= 6042;
	public static final Integer UPDATE_TYPE_01 = 60421001;//修改
	public static final Integer UPDATE_TYPE_02 = 60421002;//退单
	public static final Integer UPDATE_TYPE_03 = 60421003;//数量审核
	//回访类型
	public static final Integer REVISIT_TYPE= 6043;
	public static final Integer REVISIT_TYPE_01 = 60431001;//1日回访
	public static final Integer REVISIT_TYPE_02 = 60431002;//7日回访
	public static final Integer REVISIT_TYPE_03 = 60431003;//月度回访
	public static final Integer REVISIT_TYPE_04 = 60431004;//季度回访
	
	//跟进方式
	public static final Integer FOLLOW_TYPE= 6046;
	public static final Integer FOLLOW_TYPE_01 = 60461001;//电话
	public static final Integer FOLLOW_TYPE_02 = 60461002;//短信
	public static final Integer FOLLOW_TYPE_03 = 60461003;//微信
	public static final Integer FOLLOW_TYPE_04 = 60461004;//微博
	public static final Integer FOLLOW_TYPE_05 = 60461005;//QQ
	public static final Integer FOLLOW_TYPE_06 = 60461006;//上门拜访
	public static final Integer FOLLOW_TYPE_07 = 60461007;//DMS邮寄
	
	//提醒类型
	public static final Integer REMIND_TYPE= 6026;
	public static final Integer REMIND_TYPE_01 = 60261001;//线索分派
	public static final Integer REMIND_TYPE_02 = 60261002;//客户信息补录
	public static final Integer REMIND_TYPE_03 = 60261003;//(车厂)线索确认
	public static final Integer REMIND_TYPE_04 = 60261004;//(DCRC)线索确认
	public static final Integer REMIND_TYPE_05 = 60261005;//新增跟进任务
	public static final Integer REMIND_TYPE_06 = 60261006;//新增计划邀约任务
	public static final Integer REMIND_TYPE_07 = 60261007;//新增邀约到店任务
	public static final Integer REMIND_TYPE_08 = 60261008;//新增订单任务
	public static final Integer REMIND_TYPE_09 = 60261009;//新增交车任务
	public static final Integer REMIND_TYPE_10 = 60261010;//新增战败审核
	public static final Integer REMIND_TYPE_11 = 60261011;//新增失效审核
	public static final Integer REMIND_TYPE_12 = 60261012;//新增回访任务
	public static final Integer REMIND_TYPE_13 = 60261013;//修改订单审核
	public static final Integer REMIND_TYPE_14 = 60261014;//退订单审核
	public static final Integer REMIND_TYPE_15 = 60261015;//新增邀约计划审核
	public static final Integer REMIND_TYPE_16 = 60261016;//新增修改邀约计划
	public static final Integer REMIND_TYPE_17 = 60261017;//新增退订单任务
	public static final Integer REMIND_TYPE_18 = 60261018;//新增退订单重启任务
	public static final Integer REMIND_TYPE_19 = 60261019;//线索再分派
	public static final Integer REMIND_TYPE_20 = 60261020;//重复线索
	public static final Integer REMIND_TYPE_21 = 60261021;//新增订单数量审核
	
	//接触方式
	public static final Integer POINT_WAY= 6027;
	public static final Integer POINT_WAY_01 = 60271001;//线索录入
	public static final Integer POINT_WAY_02 = 60271002;//DCRC分派
	public static final Integer POINT_WAY_03 = 60271003;//建立客户档案
	public static final Integer POINT_WAY_04 = 60271004;//跟进任务
	public static final Integer POINT_WAY_05 = 60271005;//计划邀约任务
	public static final Integer POINT_WAY_06 = 60271006;//邀约到店任务
	public static final Integer POINT_WAY_07 = 60271007;//订单任务
	public static final Integer POINT_WAY_08 = 60271008;//交车任务
	public static final Integer POINT_WAY_09 = 60271009;//回访任务
	public static final Integer POINT_WAY_10 = 60271010;//经理审核
	public static final Integer POINT_WAY_11 = 60271011;//经理驳回
	public static final Integer POINT_WAY_12 = 60271012;//修改订单
	public static final Integer POINT_WAY_13 = 60271013;//退订单
	public static final Integer POINT_WAY_14 = 60271014;//dcrc审核
	public static final Integer POINT_WAY_15 = 60271015;//dcrc驳回
	public static final Integer POINT_WAY_16 = 60271016;//顾问录入
	public static final Integer POINT_WAY_17 = 60271017;//退车
	
	//重启类型
	public static final Integer RESTART_TYPE= 6052;
	public static final Integer RESTART_TYPE_01 = 60521001;//非重启
	public static final Integer RESTART_TYPE_02 = 60521002;//战败重启
	public static final Integer RESTART_TYPE_03 = 60521003;//实效重启
	
	//战败审核类型
	public static final Integer DEFEAT_AUDIT_TYPE= 6056;
	public static final Integer DEFEAT_AUDIT_TYPE_01 = 60561001;//经理审核
	public static final Integer DEFEAT_AUDIT_TYPE_02 = 60561002;//DCRC抽查
	
	/** *************yinshunhui 积分类型************ */
//	public static final Long PERFORMANCE_INTEG = Long.parseLong("99981002");// 业绩积分
//	public static final Long AUTHENTICATION_INTEG = Long.parseLong("99981001");// 认证积分
	
	/** *************yinshunhui 积分变动类型************ */
	public static final Long INTEG_CHANGE_SALE = Long.parseLong("29921001");// 实销加分
	public static final Long INTEG_CHANGE_RETURN = Long.parseLong("29921002");// 零售货
	public static final Long INTEG_CHANGE_CLEAR = Long.parseLong("29921003");// 积分清零
	public static final Long INTEG_CHANGE_AUTHEN = Long.parseLong("29921004");// 认证积分
	public static final Long INTEG_CHANGE_year = Long.parseLong("29921005");// 年限积分
	public static final Long INTEG_CHANGE_INIT = Long.parseLong("29921006");// 初始化数据
	public static final Long INTEG_CHANGE_MGR = Long.parseLong("29921007");// 经理月度积分
	public static final Long INTEG_CHANGE_MARK = Long.parseLong("29921008");// 月初积分标识
	public static final Long INTEG_CHANGE_PERFORMANCE = Long.parseLong("29921009");// 业绩积分
	public static final Long INTEG_CHANGE_TOTAL = Long.parseLong("29921010");// 综合评价积分
	public static final Long INTEG_CHANGE_AGAINST = Long.parseLong("29921011");// 积分兑现
	public static final Long INTEG_CHANGE_SWITCH = Long.parseLong("29921012");// 机构转积分
	
	/** *************yinshunhui 审核状态************ */
	public static final Long TO_SMALL_AUDIT = Long.parseLong("19941001");// 待小区审核
	public static final Long SMALL_AUDIT_THROUGH = Long.parseLong("19941002");// 小区审核通过
	public static final Long TO_OTD_AUDIT = Long.parseLong("19941003");// 待车厂审核
	public static final Long OTD_AUDIT_THROUGH = Long.parseLong("19941004");// 车厂审核通过
	public static final Long REGECT_STATUS = Long.parseLong("19941005");// 驳回
	public static final Long TO_SSEVICE_AUDIT = Long.parseLong("19941006");// 待售后审核
	public static final Long TO_SSEVICE_THROUGH = Long.parseLong("19941007");// 售后审核通过 

	
	/** *************yinshunhui 是否兑现************ */
	public static final Long IS_AGAINST_YES = Long.parseLong("29901001");// 兑现
	public static final Long IS_AGAINST_NO = Long.parseLong("29901002");// 未兑现
	
	// 微博状态
	public static final Long WEB_TYPE_01 = new Long(12031001); // 未提交
	public static final Long WEB_TYPE_02 = new Long(12031002); // 已提交
	public static final Long WEB_TYPE_03 = new Long(12031003); // 车厂审核通过
	public static final Long WEB_TYPE_04 = new Long(12031004); // 驳回
	public static final Long WEB_TYPE_05 = new Long(12031005); // 取消
	
	/** *************yinshunhui 人员状态************ */
	public static final Long USER_STATUS_UNSUBMIT = Long.parseLong("99991001");// 未提报
	public static final Long USER_STATUS_SUBMIT = Long.parseLong("99991002");// 已提报
	public static final Long USER_STATUS_AUDIT = Long.parseLong("99991003");// 审核通过
	public static final Long USER_STATUS_REJECT = Long.parseLong("99991004");// 驳回
	public static final Long USER_STATUS_CANCEL = Long.parseLong("99991005");// 取消
	
	/** *************yinshunhui 职位状态************ */
	public static final Long POSITION_STATUS_ON = Long.parseLong("99941001");// 在职
	public static final Long POSITION_STATUS_LEAVE = Long.parseLong("99941002");// 离职
	public static final Long POSITION_STATUS_SWITCH = Long.parseLong("99941003");// 体系装换
	
	/** *************yinshunhui 连续三个月积分为零************ */
	public static final Long THREE_MONTH_ZERO_YES = Long.parseLong("19901001");// 是
	public static final Long THREE_MONTH_ZERO_NO = Long.parseLong("19901002");// 否
	
	/** *************yinshunhui 人员变动类型************ */
	public static final Long PERSON_CHANGE_TYPE_ENTRY = Long.parseLong("29931001");// 入职
	public static final Long PERSON_CHANGE_TYPE_LEAVE = Long.parseLong("29931002");// 离职
	public static final Long PERSON_CHANGE_TYPE_SWITCH = Long.parseLong("29931003");// 体系转换
	public static final Long PERSON_CHANGE_TYPE_AHTHEN = Long.parseLong("29931004");// 认证
	public static final Long PERSON_CHANGE_TYPE_ALTER = Long.parseLong("29931005");// 修改
	public static final Long PERSON_CHANGE_TYPE_AUDIT = Long.parseLong("29931006");// 修改
	
	/** *************yinshunhui 认证的等级************ */
	public static final Long AUTHENTICATION_LEVEL_THREE = Long.parseLong("29911001");// 三星级销售顾问
	public static final Long AUTHENTICATION_LEVEL_FOUR = Long.parseLong("29911002");// 四星级销售顾问
	public static final Long AUTHENTICATION_LEVEL_FIVE = Long.parseLong("29911003");// 五星级销售顾问
	
	/**
	 * 质损标记
	 */
	public static Integer IS_DAMAGE_0 = 30011000; // 否

	public static Integer IS_DAMAGE_1 = 30011001; // 是
	
	 //运输方式
    public static final Integer TT_TRANS_WAY = 9559;
    public static final Integer TT_TRANS_WAY_01 = 95591001; //平板
    public static final Integer TT_TRANS_WAY_02 = 95591002; //火车
    public static final Integer TT_TRANS_WAY_03 = 95591003; //船运
    public static final Integer TT_TRANS_WAY_04 = 95591004; //集装箱
    public static final Integer TT_TRANS_WAY_05 = 95591005; //驾送
    public static final Integer TT_TRANS_WAY_06 = 95591006; //自提

    
  //退回生产线类型/*2015.8.31 艾春 添加*/
  	public static final Integer RETREAT_TYPE = 9718;
  	public static final Integer RETREAT_TYPE_REAL = 97181001;//退回生产线
  	public static final Integer RETREAT_TYPE_TRIAL = 97181002;//试制移库
  	
	 /**接车入库接口车辆状态*/
	public static Integer VEHICLE_IN_STATUS = 1527 ;
	 /**接车入库接口车辆已经入库*/
	public static Integer VEHICLE_IN_STATUS_01 = 15271001;
	 /**接车入库接口车辆尚未入库*/
	public static Integer VEHICLE_IN_STATUS_02 = 15271002;
    
    // 单据类型 
    public static final Integer VOUCHER_TYPE = 9623;
    public static final Integer VOUCHER_TYPE_01 = 96231001; //投诉
    public static final Integer VOUCHER_TYPE_02 = 96231002; //咨询
    public static final String VOUCHER_COMPLAIN_NAME = "投诉";
    public static final String VOUCHER_CONSULT_NAME = "咨询";
    
    // 单据状态
    public static final Integer VOUCHER_STATUS = 9624;
    public static final Integer VOUCHER_STATUS_01 = 96241001; //待处理
    public static final Integer VOUCHER_STATUS_02 = 96241002; //坐席处理中
    public static final Integer VOUCHER_STATUS_03 = 96241003; //客户专员处理中
    public static final Integer VOUCHER_STATUS_04 = 96241004; //大区处理中
    public static final Integer VOUCHER_STATUS_05 = 96241005; //服务站处理中
    public static final Integer VOUCHER_STATUS_06 = 96241006; //已处理
    public static final Integer VOUCHER_STATUS_07 = 96241007; //已上报
    public static final Integer VOUCHER_STATUS_08 = 96241008; //已关闭
 // pin申请单据状态
    public static final Integer PIN_APPLY_STATUS = 9625;
    public static final Integer PIN_APPLY_STATUS_01 = 96251001; //未提报
    public static final Integer PIN_APPLY_STATUS_02 = 96251002; //已提报
    public static final Integer PIN_APPLY_STATUS_03 = 96251003; //已回复
    // 处理状态 dispose
    public static final Integer DISPOSE_STATUS = 9626;
    public static final Integer DISPOSE_STATUS_01 = 96261001; //已受理
    public static final Integer DISPOSE_STATUS_02 = 96261002; //已处理
    public static final Integer DISPOSE_STATUS_03 = 96261003; //已分派
    public static final Integer DISPOSE_STATUS_04 = 96261004; //已上报
    public static final Integer DISPOSE_STATUS_05 = 96261005; //已关闭
    public static final Integer DISPOSE_STATUS_06 = 96261006; //处理中
    //外出救援单据状态
    public static final Integer OUT_MAINTAIN = 9630;
    public static final Integer OUT_MAINTAIN_01 = 96301001; //未上报
    public static final Integer OUT_MAINTAIN_02 = 96301002; //审核中
    public static final Integer OUT_MAINTAIN_03 = 96301003; //审核退回
    public static final Integer OUT_MAINTAIN_04 = 96301004; //审核通过
    public static final Integer OUT_MAINTAIN_05 = 96301005; //审核拒绝
    // 投诉内容
    public static final Integer COMPLAIN_CONTENT  = 9605;
    public static final Integer COMPLAIN_CONTENT_01 = 96051001; //产品问题
    public static final Integer COMPLAIN_CONTENT_02 = 96051002; //服务问题
    public static final Integer COMPLAIN_CONTENT_03 = 96051003; //配件问题
    public static final Integer COMPLAIN_CONTENT_04 = 96051004; //销售问题
    public static final Integer COMPLAIN_CONTENT_05 = 96051005; //维修问题
    public static final Integer COMPLAIN_CONTENT_06 = 96051006; //紧急救援
    public static final Integer COMPLAIN_CONTENT_07 = 96051007; //其他问题
    
    // 咨询内容
    public static final Integer CONSULT_CONTENT  = 9606;
    public static final Integer CONSULT_CONTENT_01 = 96061001; //产品信息
    public static final Integer CONSULT_CONTENT_02 = 96061002; //销售网络
    public static final Integer CONSULT_CONTENT_03 = 96061003; //服务网络
    public static final Integer CONSULT_CONTENT_04 = 96061004; //保修政策
    public static final Integer CONSULT_CONTENT_05 = 96061005; //配件信息
    public static final Integer CONSULT_CONTENT_06 = 96061006; //招商信息
    public static final Integer CONSULT_CONTENT_07 = 96061007; //服务活动
    public static final Integer CONSULT_CONTENT_08 = 96061007; //促销信息
    public static final Integer CONSULT_CONTENT_09 = 96061007; //技术支持
    public static final Integer CONSULT_CONTENT_10 = 96061007; //公司内部信息
    public static final Integer CONSULT_CONTENT_11 = 96061007; //其他信息
    
    
    //车辆用途Vehicle use
    public static final String VEHICLE_USE = "8006";
    public static final String VEHICLE_USE_01 = "80061001";//上下班代步
    public static final String VEHICLE_USE_02 = "80061002";//接送家人孩子
    public static final String VEHICLE_USE_03 = "80061003";//家庭旅游(近郊、长途)
    public static final String VEHICLE_USE_04 = "80061004";//与家人外出购物或休闲活动
    public static final String VEHICLE_USE_05 = "80061005";//以货物配送为主
    public static final String VEHICLE_USE_06 = "80061006";//个人业务货物运输（小微企业）
    public static final String VEHICLE_USE_07 = "80061007";//跑业务谈生意代步
    public static final String VEHICLE_USE_08 = "80061008";//接送客户
    public static final String VEHICLE_USE_09 = "80061009";//接送员工
    public static final String VEHICLE_USE_10 = "80061010";//客运出租业务
    public static final String VEHICLE_USE_11 = "80061011";//货运出租业务
    public static final String VEHICLE_USE_12 = "80061012";//其他（请注明
    
    //仓库类型
    public static final String SALES_WAREHOUSE_TYPE = "1401";//销售仓库类型
    public static final String SALES_WAREHOUSE_TYPE_IN = "14011001";//销售内部仓库
    public static final String SALES_WAREHOUSE_TYPE_OUT = "14011002";//销售外部仓库   
    //服务活动单据状态
    public static final Integer SERVICEACTIVITYAPPLY_STATUS = 9627;
    public static final Integer SERVICEACTIVITYAPPLY_STATUS_01 = 96271001;//已保存
    public static final Integer SERVICEACTIVITYAPPLY_STATUS_02 = 96271002;//审核中
    public static final Integer SERVICEACTIVITYAPPLY_STATUS_03 = 96271003;//审核退回
    public static final Integer SERVICEACTIVITYAPPLY_STATUS_04 = 96271004;//审核拒绝
    public static final Integer SERVICEACTIVITYAPPLY_STATUS_05 = 96271005;//审核通过
    
    //服务活动管理--服务活动活动状态(新)
    public static final Integer SERVICEACTIVITY_STATUS_NEW = 9629;
    public static final Integer SERVICEACTIVITY_STATUS_NEW_01 = 96291001;//尚未发布
    public static final Integer SERVICEACTIVITY_STATUS_NEW_02 = 96291002;//已经发布
    public static final Integer SERVICEACTIVITY_STATUS_NEW_03 = 96291003;//已经结束
    
    //服务活动管理--活动类型(新)
    public static final Integer SERVICEACTIVITY_TYPE_NEW = 9628;
    public static final Integer SERVICEACTIVITY_TYPE_NEW_01 = 96281001;//技术升级
    public static final Integer SERVICEACTIVITY_TYPE_NEW_02 = 96281002;//送保养
    public static final Integer SERVICEACTIVITY_TYPE_NEW_03 = 96281003;//送检测
    //发运单据类型
    public static final Integer DELIVERY_ORD_TYPE = 1213;
    public static final Integer DELIVERY_ORD_TYPE_ORDER = 12131001; //订单
    public static final Integer DELIVERY_ORD_TYPE_ALLOCAT = 12131002; //调拨单
    
	public static final Integer LICENSE_PLATE_FIRST = 5001; //车牌号首字

	
	//补充计划类型
    Integer ADD_PLAN_TYPE = 9301;
    Integer ADD_PLAN_TYPE_JJ =93010001;//补充计划(紧急)   紧急计划 
    Integer ADD_PLAN_TYPE_TS =93010002;//补充计划(特殊)   特殊计划
    Integer ADD_PLAN_TYPE_DZ =93010003;//补充计划(定制)   定制计划
    Integer ADD_PLAN_TYPE_PT =93010004;//补充计划(月度)   月度计划
    Integer ADD_PLAN_TYPE_GD =93010005;//滚动计划
    Integer ADD_PLAN_TYPE_BC =93010006;//滚动补充计划     
    Integer ADD_PLAN_TYPE_BC1 =93010007;//补充计划(补充)   补充计划
    
  //滚动计划确认状态
    Integer SCROLL_PLAN_CONFIRM_STATUS=9298;
    Integer SCROLL_PLAN_CONFIRM_STATUS_01=92981001;//待确认
    Integer SCROLL_PLAN_CONFIRM_STATUS_02=92981002;//已确认
    Integer SCROLL_PLAN_CONFIRM_STATUS_03=92981003;//采销确认
    Integer SCROLL_PLAN_CONFIRM_STATUS_04=92981004;//最终确认
    Integer SCROLL_PLAN_CONFIRM_STATUS_05=92981005;//销售导入错误
    Integer SCROLL_PLAN_CONFIRM_STATUS_06=92981006;//销售导入总成件
    Integer SCROLL_PLAN_CONFIRM_STATUS_07=92981007;//销售确认总成件
    
    
  //生产订单类型
    Integer PRODUCT_ORDER_TYPE_STATUS = 1211;
    Integer PRODUCT_ORDER_TYPE_STATUS_01 = 12111001;//周度生产订单
    Integer PRODUCT_ORDER_TYPE_STATUS_02 = 12111002;//月度生产订单
    Integer PRODUCT_ORDER_TYPE_STATUS_03 = 12111003;//临时生产订单
    //调拨单状态
    public static final Integer DB_ORDER_STATUS = 1214;
    public static final Integer DB_ORDER_STATUS_01 = 12141001; //已保存
    public static final Integer DB_ORDER_STATUS_02 = 12141002; //已提报
    
 // 维修时间预警规则等级
 	public static final Integer SWANINGTIME_LEVEL = 6105;
 	public static final Integer SWANINGTIME_LEVEL_01 = 61051001;// 1级 红色
 	public static final Integer SWANINGTIME_LEVEL_02 = 61051002;// 2级 橙色
 	public static final Integer SWANINGTIME_LEVEL_03 = 61051003;// 3级 黄色
 	
 	public static final Integer SWANINGTIME_TYPE = 6103;
	public static final Integer SWANINGTIME_TYPE_01 = 61031001;// 单次维修天数预警
	public static final Integer SWANINGTIME_TYPE_02 = 61031002;// 维修总天数预警
	
	// 三包预警规则设置 预警类型
		public static final Integer WANINGTIME_TYPE = 6999;
		public static final Integer WANINGTIME_TYPE_01 = 69991001;// 总成
		public static final Integer WANINGTIME_TYPE_02 = 69991002;// 严重安全故障模式
		public static final Integer WANINGTIME_TYPE_03 = 69991003;// 同一质量问题
    
		public static final Integer Warehousing_library_type = 1215;// 出入库类型
		public static final Integer Warehousing_library_type_1 = 12151001; // 采购入库
		public static final Integer Warehousing_library_type_2 = 12151002; // 调拨入库
		public static final Integer Warehousing_library_type_3 = 12151003; // 借进登记
		public static final Integer Warehousing_library_type_4 = 12151004; // 借出归还
		public static final Integer Warehousing_library_type_5 = 12151005; // 配件报溢
		public static final Integer Warehousing_library_type_6 = 12151006; // 维修发料
		public static final Integer Warehousing_library_type_7 = 12151007; // 配件销售
		public static final Integer Warehousing_library_type_8 = 12151008; // 车间借料
		public static final Integer Warehousing_library_type_9 = 12151009; // 内部领用
		public static final Integer Warehousing_library_type_10 = 12151010; // 调拨出库
		public static final Integer Warehousing_library_type_11 = 12151011; // 借出登记
		public static final Integer Warehousing_library_type_12 = 12151012; // 借进归还
		public static final Integer Warehousing_library_type_13 = 12151013; // 配件移库出库
		public static final Integer Warehousing_library_type_14 = 12151014; // 配件报损
		public static final Integer Warehousing_library_type_15 = 12151015; // 采购退货
		public static final Integer Warehousing_library_type_16 = 12151016; // 其他入库
		public static final Integer Warehousing_library_type_17 = 12151017; // 其他出库
		public static final Integer Warehousing_library_type_18 = 12151018; // 订货退回
		public static final Integer Warehousing_library_type_19 = 12151019; // 成本价调整入库
		public static final Integer Warehousing_library_type_20 = 12151020; // 成本价调整出库
		public static final Integer Warehousing_library_type_21 = 12151021; // 发料价格调整入库
		public static final Integer Warehousing_library_type_22 = 12151022; // 发料价格调整出库
		public static final Integer Warehousing_library_type_23 = 12151023; // 配件移库
		public static final Integer Warehousing_library_type_24 = 12151024; // 工单预留
		public static final Integer Warehousing_library_type_25 = 12151025; // 装饰装潢
		
		// zhumingwei 2013-05-30 是否出库
		public static final Integer is_Warehousing = 1278;// 是否出库
		public static final Integer is_Warehousing_1 = 12781001; // 出库
		public static final Integer is_Warehousing_2 = 12781002; // 入库
		
		// OEM库房流水数据来源
		public static final Integer OEM_WAREHOUSE_SOURCE_TYPE = 6110; // OEM库房流水数据来源
		public static final Integer OEM_WAREHOUSE_SOURCE_TYPE_DMS = 61101001; // DMS系统
		public static final Integer OEM_WAREHOUSE_SOURCE_TYPE_SHOP = 61101002; // 店面系统
		public static final Integer OEM_WAREHOUSE_SOURCE_TYPE_DT = 61101003; // 顶腾系统
		
		// luole 2013-07-12 配件库房验证是与否
		public static final Integer is_warehouse = 1279;//
		public static final Integer is_warehouse_1 = 12791001; // 是
		public static final Integer is_warehouse_2 = 12791002; // 否
    
 // 上级采购单位
    Integer PURCHASE_TYPE = 9714;
    Integer PURCHASE_TYPE_01 = 97141001;//涂装车间
    Integer PURCHASE_TYPE_02 = 97141002;//专用车厂
    Integer PURCHASE_TYPE_03 = 97141003;//供应商
    Integer PURCHASE_TYPE_04 = 97141004;//总装车间

    	
    	//抵扣单状态	
    	public static final Integer DEDUCTION_STATUS=9631;
    	public static final Integer DEDUCTION_STATUS_1 = 96311001; // 未结算
		public static final Integer DEDUCTION_STATUS_2 = 96311002; // 已结算
		
		//质量信息审核状态
    	public static final Integer INFORMATION_STATUS=9632;
    	public static final Integer INFORMATION_STATUS_1 = 96321001; // 一级审核通过
		public static final Integer INFORMATION_STATUS_2 = 96321002; // 一级审核驳回
    	public static final Integer INFORMATION_STATUS_3 = 96321003; // 二级审核通过
    	public static final Integer INFORMATION_STATUS_4 = 96321004; // 二级审核驳回



    /**
     * 司机审核状态
     */
	public static final Integer DRIVER_TYPE = 2050;//
	public static final Integer DRIVER_TYPE_01 = 20501001; // 保存
	public static final Integer DRIVER_TYPE_02 = 20501002; // 提交
	public static final Integer DRIVER_TYPE_03 = 20501003; // 审核通过
	public static final Integer DRIVER_TYPE_04 = 20501004; // 审核拒绝
    
	/**
     * 交接单状态
     */
	public static final Integer WAYBILL_STATUS = 9998;
	public static final Integer WAYBILL_STATUS_01 = 99981001; //已导入
	public static final Integer WAYBILL_STATUS_02 = 99981002; //部分交车
	public static final Integer WAYBILL_STATUS_03 = 99981003; //已交车
    /**
     * 交接单明细状态
     */
	public static final Integer WAYBILL_DTL_STATUS = 2052;
	public static final Integer WAYBILL_DTL_STATUS_01 = 20521001;//在途
	public static final Integer WAYBILL_DTL_STATUS_02 = 20521002;//已交车
	
	
	 /**
     * 操作部门
     */
    public static final Integer DODEPT = 9713;
    public static final Integer DODEPT_01 = 97131001;//财务
    public static final Integer DODEPT_02 = 97131002;//采购
    
    //售后服务工单-工单状态
    public static final Integer SERVICE_ORDER_STATUS = 1250;
    public static final Integer SERVICE_ORDER_STATUS_01 = 12501001;//未上报
    public static final Integer SERVICE_ORDER_STATUS_02 = 12501002;//未结算
    public static final Integer SERVICE_ORDER_STATUS_03 = 12501003;//预授权审核中
    public static final Integer SERVICE_ORDER_STATUS_04 = 12501004;//预授权审核通过
    public static final Integer SERVICE_ORDER_STATUS_05 = 12501005;//预授权审核驳回
    public static final Integer SERVICE_ORDER_STATUS_06 = 12501006;//预授权审核拒绝
    public static final Integer SERVICE_ORDER_STATUS_08 = 12501008;//已结算
    public static final Integer SERVICE_ORDER_STATUS_09 = 12501009;//废弃
    
    //售后服务工单-预授权内容类型
    public static final Integer AUTH_CONTENT_TYPE = 1251;
    public static final Integer AUTH_CONTENT_TYPE_01 = 12511001;//维修类型预授权
    public static final Integer AUTH_CONTENT_TYPE_02 = 12511002;//工时监控预授权
    public static final Integer AUTH_CONTENT_TYPE_03 = 12511003;//配件监控预授权
    public static final Integer AUTH_CONTENT_TYPE_04 = 12511004;//金额监控预授权
    public static final Integer AUTH_CONTENT_TYPE_05 = 12511005;//预警监控预授权
    
    //售后服务工单-预授权审核状态
    public static final Integer AUTH_AUDIT_STATUS = 1252;
    public static final Integer AUTH_AUDIT_STATUS_01 = 12521001;//待预授权审核
    public static final Integer AUTH_AUDIT_STATUS_02 = 12521002;//预授权审核通过
    public static final Integer AUTH_AUDIT_STATUS_03 = 12521003;//预授权审核驳回
    public static final Integer AUTH_AUDIT_STATUS_04 = 12521004;//预授权审核拒绝
    
    //结算单状态
    public static final Integer BAL_ORDER_STATUS = 9610;
    public static final Integer BAL_ORDER_STATUS_00 = 96101000;//未挂账
    public static final Integer BAL_ORDER_STATUS_01 = 96101001;//已挂账
    public static final Integer BAL_ORDER_STATUS_02 = 96101002;//已申请
    public static final Integer BAL_ORDER_STATUS_03 = 96101003;//已审核
    public static final Integer BAL_ORDER_STATUS_04 = 96101004;//已补录
    public static final Integer BAL_ORDER_STATUS_05 = 96101005;//已付款
    public static final Integer BAL_ORDER_STATUS_06 = 96101006;//审核驳回
    public static final Integer BAL_ORDER_STATUS_07 = 96101007;//已保存
    
    //同意首保费用
    public static final Integer AGREE_MAINTAIN_COST = 8033;
    public static final Integer AGREE_MAINTAIN_COST_01 = 80331001;//同意
    public static final Integer AGREE_MAINTAIN_COST_02 = 80331002;//不同意
    
    //抵扣项目类型
    public static final Integer DEDUCTION_OBJECT_TYPE = 1253;
    public static final Integer DEDUCTION_OBJECT_TYPE_01 = 12531001;//维修配件抵扣
    public static final Integer DEDUCTION_OBJECT_TYPE_02 = 12531002;//维修工时抵扣
    public static final Integer DEDUCTION_OBJECT_TYPE_03 = 12531003;//其他费用抵扣
    
    //抵扣类型
    public static final Integer DEDUCTION_TYPE = 1254;
    public static final Integer DEDUCTION_TYPE_01 = 12541001;//一次抵扣
    public static final Integer DEDUCTION_TYPE_02 = 12541002;//二次抵扣
    
}


