package com.infodms.dms.constants;

public interface PTConstants {
	//配件采购订单编号前缀
	String PT_NO = "ptor";
	//配件索赔单编号前缀
	String CLAIM_NO = "PTCR";
	String SIGN_NO = "PTSG";
	String poinitUrl = "/jsp/partsmanage/purchase/qyeryPartOrderList.jsp";//采购订单查询
	String orderInfoUrl = "/jsp/partsmanage/purchase/partOrderInfo.jsp";//采购订单明细
	String compileUrl = "/jsp/partsmanage/purchase/orderCompile.jsp";//采购订单编辑
	String dcPartUrl = "/jsp/partsmanage/purchase/queryDcPart.jsp";//查询供货方配件信息
	String addPartUrl = "/jsp/partsmanage/purchase/addPartInfo.jsp";//新增采购配件信息
	String stockInitUrl = "/jsp/partsmanage/infosearch/qyeryDealerDlrstockInfo.jsp";//经销商库存
	String stockInfoUrl = "/jsp/partsmanage/infosearch/qyeryDlrstockmoveDetail.jsp";//库存明细
	String dsinitUrl = "/jsp/partsmanage/infosearch/qyeryDSRelactionInfo.jsp";//配送中心与经销商关系
	String relactionUrl = "/jsp/partsmanage/infosearch/qyeryRelactionForUpdate.jsp";//配送中心与经销商维护
	String dealerUrl = "/jsp/partsmanage/infosearch/queryDealerInfoList.jsp";//查询经销商页面
	String piinitUrl = "/jsp/partsmanage/infosearch/qyeryPartInfoSearch.jsp";//配件基本信息
	String infoUrl = "/jsp/partsmanage/infosearch/queryPartDetail.jsp";//配件详细信息
	String infoUrlMod = "/jsp/partsmanage/infosearch/queryPartDetailMod.jsp";//配件详细信息-修改页面
	String opinitUrl = "/jsp/partsmanage/infosearch/qyeryOrderParamList.jsp";//配件订单参数查询
	String addUrl = "/jsp/partsmanage/infosearch/addOrderParam.jsp";//添加参数
	String updateUrl = "/jsp/partsmanage/infosearch/updateOrderParam.jsp";//修改参数
	String ssinitUrl = "/jsp/partsmanage/infosearch/qyerySupplierSearch.jsp";//供应商查询
	String INIT_URL = "/jsp/partsmanage/partclaim/partClaimApplyQuery.jsp";//配件索赔申请-查询页面
	String PART_CLAIM = "/jsp/partsmanage/partclaim/partClaimApply.jsp";//配件索赔申请-详细页面
	String purchaseUrl = "/jsp/partsmanage/purchase/purchaseOrderManage.jsp";//配件采购订单管理
	String updateOrderUrl = "/jsp/partsmanage/purchase/updateOrderPart.jsp";//配件采购订单修改
	String orderForwordUrl = "/jsp/partsmanage/purchase/orderForword.jsp";//配件采购订单上报
	String carefullyUrl = "/jsp/partsmanage/purchase/partCarefully.jsp";//配件采购订单预审
	String forwordOderPartUrl = "/jsp/partsmanage/purchase/forwordOrderPart.jsp";//配件采购订单预审
	String orderSheetUrl = "/jsp/partsmanage/purchase/purchaseOrderSign.jsp";//配件采购订单签收列表页
	String orderSignUrl = "/jsp/partsmanage/purchase/orderSing.jsp";//配件采购订单签详细页
	String PART_CLAIM_CHECK_QUERY = "/jsp/partsmanage/partclaim/partClaimCheckQuery.jsp";//配件索赔审核-查询页面
	String PART_CLAIM_CHECK = "/jsp/partsmanage/partclaim/partClaimCheck.jsp";//配件索赔审核-审核页面
	String partUtl = "/jsp/partsmanage/purchase/partAll.jsp";//新增配件页面
	String PART_CLAIM_QUERY = "/jsp/partsmanage/partclaim/partClaimQuery.jsp";//配件索赔查询
	String PART_CLAIM_DETAIL = "/jsp/partsmanage/partclaim/partClaimDetail.jsp";//配件索赔查询-详细信息
	String PART_ORDER_QUERY = "/jsp/partsmanage/purchase/queryPartOrder.jsp";//配件索赔查询-详细信息
	String signInfoUrl = "/jsp/partsmanage/purchase/signInfo.jsp";//新增配件页面
	String PART_CLAIM_DEALER_QUERY = "/jsp/partsmanage/partclaim/partClaimDealerQuery.jsp";//配件索赔查询
	String PART_INFO_MOD = "/jsp/partsmanage/infosearch/qyeryPartInfoMod.jsp";//配件信息修改
	String part_type_Url = "/jsp/partsmanage/infosearch/qyeryPartType.jsp";//配件大类信息
	String part_type_Add = "/jsp/partsmanage/infosearch/addPartType.jsp";//新增配件大类信息
	String query_part = "/jsp/partsmanage/infosearch/queryPart.jsp";//  查询配件信息弹出框
	String UPDATE_URL = "/jsp/partsmanage/infosearch/updatePartType.jsp" ; // 修改配件大类
	
	//计划变更维护
	String PART_EXCEPTION_QUERY_URL = "/jsp/parts/baseManager/partExceptionQueryManger/partExceptionQuery.jsp";//计划变更维护首页
	String PART_EXCEPTION_QUERY_ADD = "/jsp/parts/baseManager/partExceptionQueryManger/partExceptionAdd.jsp";//计划变更维护增加页面
	String PART_EXCEPTION_QUERY_Mod = "/jsp/parts/baseManager/partExceptionQueryManger/partExceptionMod.jsp";//计划变更维护修改页面
	String PART_EXCEPTION_QUERY_SELECT = "/jsp/parts/baseManager/partExceptionQueryManger/partSelectSingle.jsp";//配件编码选择页面1
	String PART_EXCEPTION_QUERY_SELECT2 = "/jsp/parts/baseManager/partExceptionQueryManger/partSelectSingle2.jsp";//配件编码选择页面2
	
	//配件采购属性维护
	String PART_PLANNER_QUERY_URL = "/jsp/parts/baseManager/partPlannerQueryManager/partPlannerQuery.jsp";//配件采购属性维护首页
	String VENDER_SELECT_SETING_URL = "/jsp/parts/baseManager/partPlannerQueryManager/venderSelectSeting.jsp";//默认供应商最小包装量页面
	
	//配件计划员与仓库维护
	String PART_PLANNER_WAREHOUSE_URL = "/jsp/parts/baseManager/partPlannerWarehouseMangager/partPlannerWarehouseManage.jsp";//计划员与仓库维护
	String PART_PLANNER_WAREHOUSE_MOD = "/jsp/parts/baseManager/partPlannerWarehouseMangager/partPlannerWarehouseMod.jsp";//计划员与仓库修改
	String PART_PLANNER_WAREHOUSE_ADD = "/jsp/parts/baseManager/partPlannerWarehouseMangager/partPlannerWarehouseAdd.jsp";//计划员与仓库新增
	String WAREHOUSE_INFO = "/jsp/parts/baseManager/partPlannerWarehouseMangager/queryWarehouse.jsp";//计划员与仓库修改
	String WAREHOUSE_Add_INFO = "/jsp/parts/baseManager/partPlannerWarehouseMangager/queryWarehouseForAdd.jsp";//计划员与仓库修改
	String PLANNER_INFO = "/jsp/parts/baseManager/partPlannerWarehouseMangager/planerSelectSingle.jsp";//选择计划员
	
	//配件人员类型设置
	String PART_USER_POST_PAGE = "/jsp/parts/baseManager/partUserPostManager/partUserPostManage.jsp";//配件人员类型设置首页
	String PART_USER_POST_MOD = "/jsp/parts/baseManager/partUserPostManager/partUserPostMod.jsp";//配件人员类型修改页面
	String PART_USER_ORDER_MOD = "/jsp/parts/baseManager/partUserPostManager/partUserOrderMod.jsp";//配件人员类型修改页面
	String MOD_USE_INFO = "/jsp/parts/baseManager/partUserPostManager/queryUsersForMod.jsp";//人员选择（修改）
	String MOD_USE_ORDER_INFO = "/jsp/parts/baseManager/partUserPostManager/queryUsersOrderForMod.jsp";//订单类型选择（修改）
	String PART_USER_POST_ADD = "/jsp/parts/baseManager/partUserPostManager/partUserPostAdd.jsp";//配件人员类型新增页面
	String ADD_USE_INFO = "/jsp/parts/baseManager/partUserPostManager/queryUsersForAdd.jsp";//人员选择（新增）
	
	
	//供应商信息维护
	String addPartVenderInitUrl = "/jsp/parts/baseManager/partsBaseManager/partVender/partVenderAdd.jsp";//供应商添加页面 
	String partVenderInitUrl = "/jsp/parts/baseManager/partsBaseManager/partVender/partVenderQuery.jsp";//供应商分页查询页面
	String PARTVENDER_INFO_MOD="/jsp/parts/baseManager/partsBaseManager/partVender/partVenderMod.jsp";//供应商修改页面
	String PARTVENDER_INFO_VIEW="/jsp/parts/baseManager/partsBaseManager/partVender/partVenderView.jsp";//供应商查看页面
	String PART_VENDER_RELATION ="/jsp/parts/baseManager/partsBaseManager/partVender/partVenderRelationQuery.jsp";//供应商配件比例查询
	String PART_VENDER_RELATION_MAG ="/jsp/parts/baseManager/partsBaseManager/partVender/partVenderRelationMag.jsp";//供应商配件比例查询
	
	//制造商信息维护
	String partMakerInitUrl = "/jsp/parts/baseManager/partsBaseManager/partMaker/partMakerQuery.jsp";//制造商查询页面
	String addPartMakerInitUrl = "/jsp/parts/baseManager/partsBaseManager/partMaker/partMakerAdd.jsp";//制造商添加页面
	String PARMAKER_INFO_MOD = "/jsp/parts/baseManager/partsBaseManager/partMaker/partMakerMod.jsp";//制造商修改页面
	String PARMAKER_INFO_VIEW="/jsp/parts/baseManager/partsBaseManager/partMaker/partMakerView.jsp";//制造商查看页面
	String PARMAKER_SETTING="/jsp/parts/baseManager/partsBaseManager/partMaker/partMakerRelation.jsp";//制造商明细页面
	
	//配件采购价格维护
	String PART_BUYPRICE_QUERY_URL = "/jsp/parts/baseManager/partsBaseManager/partBuyPrice/partBuyPriceQuery.jsp";//配件采购价格查询页面
	String PART_BUYPRICE_ADD_URL = "/jsp/parts/baseManager/partsBaseManager/partBuyPrice/partBuyPriceAdd.jsp";//配件采购价格新增页面
	String PART_VENDER_MAKER = "/jsp/parts/baseManager/partsBaseManager/partBuyPrice/partVenderMaker.jsp";//配件供应商制造商关系维护
	String SEL_MAKER_FOR_MOD = "/jsp/parts/baseManager/partsBaseManager/partBuyPrice/queryMakerForMod.jsp";//配件供应商制造商关系维护->选择制造商
	String SEL_VENDER_FOR_MOD = "/jsp/parts/baseManager/partsBaseManager/partBuyPrice/queryVenderForMod.jsp";//配件供应商制造商关系维护->选择制造商
	
	//配件主信息维护
	String PartBaseQueryUrl = "/jsp/parts/baseManager/PartBaseQuery.jsp"; //配件主信息维护查询
	String PartBaseQueryDetailUrl = "/jsp/parts/baseManager/PartBaseView.jsp"; //配件主信息维护详细
	String PartBaseQueryEditUrl = "/jsp/parts/baseManager/PartBaseMod.jsp"; //配件主信息维护修改
	String PartBaseQueryAddUrl = "/jsp/parts/baseManager/partBaseAdd.jsp"; //配件主信息维护新增
	
	//配件销售价格维护
	String PartSalePriceUrl = "/jsp/parts/baseManager/partSalePrice/partSalePrice.jsp"; //配件主信息维护查询
	String AddPartSalePriceUrl = "/jsp/parts/baseManager/partSalePrice/partSalePriceAdd.jsp"; //设置价格列表
	String partSalePriceHisUrl = "/jsp/parts/baseManager/partSalePrice/partSalePriceHis.jsp"; //配件主信息维护修改历史查询
	
	//配件服务商开票信息维护
	String PART_BILL_QUERY_URL = "/jsp/parts/baseManager/partsBaseManager/partBill/partBillQuery.jsp";//配件服务商开票信息查询页面
	String PART_BILL_ADD_URL = "/jsp/parts/baseManager/partsBaseManager/partBill/partBillAdd.jsp";//配件服务商开票信息新增页面
	String PARTBILL_INFO_MOD = "/jsp/parts/baseManager/partsBaseManager/partBill/partBillMod.jsp";//制造商修改页面
	
	//服务商发运j接收地址维护
	String PART_ADDR_QUERY_URL = "/jsp/parts/baseManager/partsBaseManager/partAddr/partAddrQuery.jsp";//服务商发运接收地址查询页面
	String PART_ADDR_ADD_URL = "/jsp/parts/baseManager/partsBaseManager/partAddr/partAddrAdd.jsp";//服务商发运接收地址新增页面
	String PART_ADDR_INFO_MOD = "/jsp/parts/baseManager/partsBaseManager/partAddr/partAddrMod.jsp";//制造商修改页面
	
	//计划维护
	String purchasePlanSettingUrl = "/jsp/parts/purchaseManager/purchasePlanSetting/purchasePlanSetting.jsp"; //计划维护
	String purchasePlanBatchAddUrl = "/jsp/parts/purchaseManager/purchasePlanSetting/batchAddPlanPg.jsp"; //计划批量新增
    String purchasePlanSettingUrl2 = "/jsp/parts/purchaseManager/purchasePlanSetting/purchasePlanSettingJJ.jsp"; //计划维护
    String foreCastPartQueryUrl = "/jsp/parts/purchaseManager/purchasePlanSetting/foreCastPartQuery.jsp"; //预计到货查询
	String purchasePlanSettingAddUrl = "/jsp/parts/purchaseManager/purchasePlanSetting/purchasePlanAdd.jsp";//新增维护
    String purchasePlanSettingAddUrl2 = "/jsp/parts/purchaseManager/purchasePlanSetting/purchasePlanAddJJ.jsp";//新增维护
	String purchasePlanSettingViewUrl = "/jsp/parts/purchaseManager/purchasePlanSetting/purchasePlanView.jsp";//查看维护
	String purchasePlanSettingModUrl = "/jsp/parts/purchaseManager/purchasePlanSetting/purchasePlanMod.jsp";//修改维护
	String partPlanQueryUrl = "/jsp/parts/purchaseManager/partPlanQuery/partPlanQuery.jsp";//计划查询
	String partPlanQueryViewUrl = "/jsp/parts/purchaseManager/partPlanQuery/partPlanQueryView.jsp";//计划查询查看
	String partPlanCheckUrl = "/jsp/parts/purchaseManager/partPlanCheck/partPlanCheck.jsp";//计划审核
	String partPlanCheckViewUrl = "/jsp/parts/purchaseManager/partPlanCheck/partPlanCheckView.jsp";//计划审核详细
	String partPlanConfirmUrl = "/jsp/parts/purchaseManager/partPlanConfirm/partPlanConfirm.jsp";//计划审核确认
	String partPlanConfirmViewUrl = "/jsp/parts/purchaseManager/partPlanConfirm/partPlanConfirmView.jsp";//计划审核确认
	String PART_PURCHASEORDERM_QUERY_URL = "/jsp/parts/purchaseManager/purchaseOrderQuery4Print.jsp";//采购订单查询
	String PURCHASEORDER_CHK_URL = "/jsp/parts/purchaseManager/purchaseOrderChk.jsp";//生成验收指令
	String PURCHASEORDER_CHK_URL1 = "/jsp/parts/purchaseManager/purchaseOrderChk1.jsp";//生成验收指令
	String PURCHASEORDER_MOD_URL = "/jsp/parts/purchaseManager/purchaseOrderMod.jsp";//采购订单修改
	String PURCHASEORDER_VIEW_URL = "/jsp/parts/purchaseManager/purchaseOrderView.jsp";//采购订单查看
	String PURCHASEORDER_PRINT_URL = "/jsp/parts/purchaseManager/purchaseOrderGPrint.jsp";//订单验收指令打印
	String PRODUCT_RECV_ORDER_URL = "/jsp/parts/purchaseManager/productRecvOrder.jsp";//采购订单转领用订单
	
	//配件拆合比例设置
	String PART_SPLIT_QUERY_URL = "/jsp/parts/baseManager/partsBaseManager/partSplit/partSplitQuery.jsp";//配件拆合比例查询页面
	String PART_SPLIT_ADD_URL = "/jsp/parts/baseManager/partsBaseManager/partSplit/partSplitAdd.jsp";//配件拆合比例新增页面
	String PART_SPLIT_INFO_VIEW="/jsp/parts/baseManager/partsBaseManager/partSplit/partSplitView.jsp";//配件拆合比例查看页面
	String PART_SPLIT_INFO_MOD = "/jsp/parts/baseManager/partsBaseManager/partSplit/partSplitMod.jsp";//配件拆合比例修改页面
	
	//配件销售人员区域范围设置
	String PART_SALES_QUERY_URL = "/jsp/parts/baseManager/partsBaseManager/partSalesScope/partSalesQuery.jsp";//配件销售人员查询页面
	String PART_SALESSCOPE_QUERY_URL = "/jsp/parts/baseManager/partsBaseManager/partSalesScope/partSalesScopeQuery.jsp";//配件销售人员对应区域范围查询页面
	String PART_SALESSCOPE_QUERY_URL1 = "/jsp/parts/baseManager/partsBaseManager/partSalesScope/partSalesScopeQuery1.jsp";//配件销售人员对应区域范围查询页面
	//采购订单管理
	String PART_PURCHASEORDERCHK_QUERY_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderChk/purchaseOrderChkQuery.jsp";//验收页面
	String PART_PURORDERCHKDTL_QUERY_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderChk/purOrderChkDtlQuery.jsp";//进货明细页面
	String PART_PURORDERDTLDELIVERY_QUERY_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderChk/purOrderDtlDeliveryQuery.jsp";//采购订单明细及交货率统计页面
	String PART_PURCHASEORDERCHK_PRINT_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderChk/purchaseOrderChkPrint.jsp";//验收单打印页面
	String PART_PURCHASEORDERIN_QUERY_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/purchaseOrderInQuery.jsp";//入库页面
	String PART_PURCHASEORDERBALANCE_QUERY_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purchaseOrderBalanceMng.jsp";//结算页面
	String PART_PURORDERBALANCEDTL_QUERY_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purOrderBalanceDtl.jsp";//结算信息查询页面
	String PART_PURORDERBALANCELY_QUERY_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purOrderBalanceLy.jsp";//结算信息查询页面(单个领用单明细)
    String PART_PURCHASEORDERBALANCE_QUERY_URL2 = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purchaseOrderBalanceQuery.jsp";//结算页面
    String PART_PURCHASEORDERBALANCE_UPDATE_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purOrderBalUpdate.jsp";//结算修改页面
    String PART_PURCHASEORDERBALANCE_ADD_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purchaseOrderBalanceAdd.jsp";//结算增加页面
    String PURCHASEORDERBAL_PRINT_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purchaseOrderBalPrint.jsp";//结算修改打印页面
	String PART_PURCHASEORDERBALANCEDETAIL_QUERY_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purchaseOrderBalanceDetailQuery.jsp";//结算明细页面
	String PART_PURCHASEORDER_QUERY_URL = "/jsp/parts/purchaseOrderManager/purchaseOrder/purchaseOrderQuery.jsp";//采购订单查询页面
	String PART_PURCHASEORDER_INDIFF_QUERY_URL = "/jsp/parts/purchaseOrderManager/purchaseOrder/purchaseOrderInDiffQuery.jsp";//采购订单查询页面
	String PART_PURCHASEORDER_IN_QUERY_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/purOrderInReport.jsp";//入库统计查询页面
	String PART_CHECKREPORT_QUERY_URL = "/jsp/report/partStockReport/partCheckReport.jsp";//库存盘点报表(本部)
	String PART_CHECKREPORTGYZX_QUERY_URL = "/jsp/report/partStockReport/partCheckGyzxReport.jsp";//库存盘点报表(供应中心)
	String PART_PURORDERCHK_PRINT_QUERY_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderChk/purchaseOrderChkPrintQuery.jsp";//验收单打印查询页面
	String PART_PURORDERDTL_PRINT_QUERY_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderChk/purchaseOrderDtlPrintQuery.jsp";//验收单明细查询页面
	String PART_PURORDERCHK_PRINT_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderChk/purOrderChkPrint.jsp";//入库单打印查询页面
	String PART_PURORDERDTL_PRINT_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderChk/purOrderDtlPrint.jsp";//入库单明细打印页面
    String PART_PURORDERCHK_PRINT_URL2 = "/jsp/parts/purchaseOrderManager/purchaseOrderChk/purOrderChkPrint2.jsp";//验收单明细打印查询页面
    String PART_PURCHASEORDERINSTOCK_PRINT_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderChk/purchaseOrderInStockMng.jsp";//入库确认页面
    String PART_PURCHASEORDERINSTOCK_VIEW_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderChk/purchaseOrderInStockView.jsp";//入库确认明细查看页面
    String PART_PURCHASEORDERVIEW_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderChk/purOrderView.jsp";//验收单查看页面
    String PART_PURCHASEORDERBALANCE_PRINT_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purOrderBalancePrintQuery.jsp";//结算打印查询页面
    String PART_PURORDERBALANCE_PRINT_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purOrderBalancePrint.jsp";//结算打印查询页面
    String PART_PURCHASEORDERBALANCE_PRINTDTL_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purOrderBalancePrintDtlQuery.jsp";//结算打印明细查询页面
    String PART_PURCHASEORDERBALANCE_CONFIRM_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purOrderBalanceConfirmQuery.jsp";//结算确认页面
    String PART_PURCHASEORDERBALANCE_CONFIRMDTL_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purOrderBalanceConfirmDtlQuery.jsp";//结算确认明细查询页面
    String PART_PURCHASEORDERBALANCE_ALL_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purOrderBalAllQuery.jsp";//结算汇总查询页面
    String PART_PURCHASEORDERBALANCE_DTL_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purOrderBalDtlQuery.jsp";//结算明细查询页面
	
	
	//销售退货申请审核
	String PART_RETURN_APPLY_QUERY_URL = "/jsp/parts/storageManager/partReturnManager/partDlrReturnChk/partDlrReturnApplyQuery.jsp";// 配件销售退货申请查询页面
	String PART_RETURN_APPLY_CHECK_URL = "/jsp/parts/storageManager/partReturnManager/partDlrReturnChk/partDlrReturnApplyCheck.jsp";// 配件销售退货审核页面
	String PART_RETURN_BACK_CHECK_URL = "/jsp/parts/storageManager/partReturnManager/partDlrReturnChk/partDlrReturnBackCheck.jsp";// 配件销售退货回运页面


	//销售退货出库
	String PART_RETURN_OUT_QUERY_URL = "/jsp/parts/storageManager/partReturnManager/partDlrReturnOut/partDlrReturnApplyQuery.jsp";// 配件销售退货申请查询页面
	String PART_RETURN_OUT_URL = "/jsp/parts/storageManager/partReturnManager/partDlrReturnOut/partDlrReturnOut.jsp";// 配件销售退货出库页面
    String PART_RETURN_BACK_CHECK_URL2 = "/jsp/parts/storageManager/partReturnManager/partDlrReturnOut/partDlrReturnBackCheck.jsp";// 配件销售退货回运页面
	//销售退货入库
	String PART_RETURN_IN_QUERY_URL = "/jsp/parts/storageManager/partReturnManager/partDlrReturnIn/partDlrReturnApplyQuery.jsp";// 配件销售退货申请查询页面
	String PART_RETURN_IN_URL = "/jsp/parts/storageManager/partReturnManager/partDlrReturnIn/partDlrReturnIn.jsp";// 配件销售退货入库页面
	
	//采购退货申请
	String PART_OEMRETURN_APPLY_QUERY_URL = "/jsp/parts/storageManager/partReturnManager/partOemReturnApply/partOemReturnApplyInfoQuery.jsp";//采购退货申请查询页面
	String PART_OEMRETURN_IN_QUERY_URL = "/jsp/parts/storageManager/partReturnManager/partOemReturnApply/partOemReturnInQuery.jsp";// 入库信息查询页面
	String PART_OEMRETURN_APPLY_URL = "/jsp/parts/storageManager/partReturnManager/partOemReturnApply/partOemReturnApply.jsp";// 配件采购退货申请页面
	String PART_OEMRETURN_APPLY_MOD_URL = "/jsp/parts/storageManager/partReturnManager/partOemReturnApply/partOemReturnApplyMod.jsp";// 配件采购退货申请页面
	String PART_OEMRETURN_APPLYNO_URL = "/jsp/parts/storageManager/partReturnManager/partOemReturnApply/partOemReturnApplyNoInCode.jsp";// 配件采购退货申请页面(无入库单)
	String PART_OEMRETURN_APPLY_VIEW_URL = "/jsp/parts/storageManager/partReturnManager/partOemReturnApply/partOemReturnApplyDetail.jsp";// 配件采购退货申请详细信息页面
	String PART_OEMRETURN_APPLY_PRINT_URL = "/jsp/parts/storageManager/partReturnManager/partOemReturnApply/partOemReturnApplyPrint.jsp";// 配件采购退货申请打印页面
	
	//采购退货审核
	String PART_OEMRETURN_CHECK_QUERY_URL = "/jsp/parts/storageManager/partReturnManager/partOemReturnChk/partOemReturnApplyQuery.jsp";// 采购退货审核查询页面
	String PART_OEMRETURN_CHECK_URL = "/jsp/parts/storageManager/partReturnManager/partOemReturnChk/partOemReturnApplyCheck.jsp";// 采购退货审核页面
	
	//采购退货出库
	String PART_OEMRETURN_OUT_QUERY_URL = "/jsp/parts/storageManager/partReturnManager/partOemReturnOut/partOemReturnApplyQuery.jsp";// 采购退货申请查询页面
	String PART_OEMRETURN_OUT_URL = "/jsp/parts/storageManager/partReturnManager/partOemReturnOut/partOemReturnOut.jsp";// 采购退货出库页面
	
	//退货查询
	String PART_DLRRETURN_QUERY_URL = "/jsp/parts/storageManager/partReturnManager/partReturnQuery/partDlrReturnQuery.jsp";// 销售退货查询页面
	String PART_RETURN_QUERY_URL = "/jsp/parts/storageManager/partReturnManager/partReturnQuery/partReturnQuery.jsp";// 采购退货查询页面
	String PART_OEMRETURN_PRINT_URL = "/jsp/parts/storageManager/partReturnManager/partReturnQuery/partSellReturnPrint.jsp";//销售退货单查询打印页面
	String PART_PROCUREMENT_PRINT_URL= "/jsp/parts/storageManager/partReturnManager/partReturnQuery/partProcurementReturnPrint.jsp";//采购退货单打印页面
	
	//配件销售管理
	String PART_BO_QUERY ="/jsp/parts/salesManager/carFactorySalesManager/boOrderMananger/partBoQuery.jsp"; //BO单查询
	String PART_DLR_ORDER ="/jsp/parts/salesManager/carFactorySalesManager/partDlrOrder.jsp"; //配件订单提报主页面

	String PART_REPLACED_DLR_ORDER ="/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrOrder.jsp"; //配件切换订单提报主页面
	String PART_DLR_ORDER_ADD ="/jsp/parts/salesManager/carFactorySalesManager/partDlrOrderAdd.jsp"; //配件订单提报新增页面
	String PART_REPLACED_DLR_ORDER_ADD ="/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrOrderAdd.jsp"; //配件订单提报新增页面
	String PART_DLR_ORDER_DETAIL ="/jsp/parts/salesManager/carFactorySalesManager/partDlrOrderDetail.jsp"; //配件订单提报查看页面
	String PART_DLR_ORDER_MOD ="/jsp/parts/salesManager/carFactorySalesManager/partDlrOrderMod.jsp"; //配件订单提报修改页面
	String PART_REPLACED_DLR_ORDER_MOD ="/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrOrderMod.jsp"; //配件订单提报修改页面
	String PART_DLR_ORDER_CHECK_QUERY ="/jsp/parts/salesManager/carFactorySalesManager/partDlrOrderQuery.jsp"; //配件订单提报审核查询页面
	String PART_REPLACED_DLR_ORDER_CHECK_QUERY ="/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrOrderQuery.jsp"; //配件订单提报审核查询页面
	String PART_DLR_GXORDER_CHECK_QUERY ="/jsp/parts/salesManager/carFactorySalesManager/partDlrOrderGxQuery.jsp"; //广宣订单审核查询页面
	String PART_DLR_ORDER_CHECK_DETAIL_EX ="/jsp/parts/salesManager/carFactorySalesManager/partDlrOrderDetailEX.jsp"; //配件异常入库
	String PART_DLR_ORDER_CHECK_DETAIL ="/jsp/parts/salesManager/carFactorySalesManager/partDlrOrderCheckDetail.jsp"; //配件订单提报审核查看页面
	String PART_DLR_ORDER_CHECK_CHECK ="/jsp/parts/salesManager/carFactorySalesManager/partDlrOrderCheck.jsp"; //配件订单提报审核页面
	String PART_REPLACED_DLR_ORDER_CHECK_CHECK ="/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrOrderCheck.jsp"; //切换订单提报审核页面
	String PART_REPLACED_DLR_BACKHAUL ="/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrBackhaul.jsp"; //替换件回运页面
	String PART_REPLACED_DLR_BACK ="/jsp/parts/salesManager/carFactorySalesManager/partReplacedDlrBack.jsp"; //替换件回运页面
	String PART_DLR_ORDER_CHECK_GX ="/jsp/parts/salesManager/carFactorySalesManager/partDlrGxOrderCheck.jsp"; //广宣订单审核页面
	String PART_DLR_ORDER_SO_MAIN ="/jsp/parts/salesManager/carFactorySalesManager/partSoMain.jsp"; //销售单主页面
	String PART_DLR_ORDER_SO_ADD ="/jsp/parts/salesManager/carFactorySalesManager/partSoMainAdd.jsp"; //销售单增加页面
	String PART_DLR_ORDER_SO_Detail ="/jsp/parts/salesManager/carFactorySalesManager/partSoMainDetail.jsp"; //销售单详细页面
	String PART_DLR_ORDER_SO_MODIFY ="/jsp/parts/salesManager/carFactorySalesManager/partSoMainModify.jsp"; //销售单修改页面
	String PART_DLR_ORDER_FIN_CHECK_MAIN ="/jsp/parts/salesManager/carFactorySalesManager/partSalesOrderFinCheckMain.jsp"; //销售单财务审核
	String PART_DLR_ORDER_FIN_CHECK_DETAIL ="/jsp/parts/salesManager/carFactorySalesManager/partSalesOrderFinCheckDetail.jsp"; //销售单财务审核查看页面
	String PART_PKG_MAIN ="/jsp/parts/salesManager/carFactorySalesManager/partPkgMain.jsp"; //装箱单页面
	String PART_PKG_DETAIL ="/jsp/parts/salesManager/carFactorySalesManager/partPkgDetail.jsp"; //装箱查看页面
	String PART_PKG_ORDER ="/jsp/parts/salesManager/carFactorySalesManager/partPkgOrder.jsp"; //装箱页面
	String PART_PKG_PRINT ="/jsp/parts/salesManager/carFactorySalesManager/partPkgPrint.jsp"; //装箱单拣货打印
	String PART_PKG_DTL_PRINT ="/jsp/parts/salesManager/carFactorySalesManager/partPkgDtlPrint.jsp"; //装箱清单打印
	String PART_PKG_DTL_PRINT_BATCH ="/jsp/parts/salesManager/carFactorySalesManager/partPkgDtlPrintBatch.jsp"; //装箱清单打印
	String PART_CAR_CFM_PRINT ="/jsp/parts/salesManager/carFactorySalesManager/partConfirmPrint.jsp"; // 打印随车装箱单
	
	String PART_TRANS_PLAN_MAIN ="/jsp/parts/salesManager/carFactorySalesManager/transplan/partTransplan.jsp"; //配件发运计划页面
	String PART_TRANS_PLAN_PRINT ="/jsp/parts/salesManager/carFactorySalesManager/transplan/partTransplanPrint.jsp"; //配件发运计划页面
	String PART_TRANSPLAN_ORDER ="/jsp/parts/salesManager/carFactorySalesManager/transplan/partTransplanCreate.jsp"; //配件发运计划生成页面
	String PART_OUTSTOCK_ORDER_MAIN ="/jsp/parts/salesManager/carFactorySalesManager/partOutstockMain.jsp"; //出库单页面
	String PART_OUTSTOCK_ORDER ="/jsp/parts/salesManager/carFactorySalesManager/partOutstock.jsp"; //出库页面
	
	String PART_OUTSTOCK_QUERY_MAIN ="/jsp/parts/salesManager/carFactorySalesManager/partOutstockQueryMain.jsp"; //出库单管理页面
	String PART_OUTSTOCK_QUERY_DETAIL ="/jsp/parts/salesManager/carFactorySalesManager/partOutstockQueryDetail.jsp"; //出库单管理查看页面
	
	String PART_TRANS_MAIN ="/jsp/parts/salesManager/carFactorySalesManager/partTransMain.jsp"; //发运单
	String PART_TRANS_ORDER ="/jsp/parts/salesManager/carFactorySalesManager/partTransOrder.jsp"; //生成发运单
	String PART_TRANS_ORDER_DTL ="/jsp/parts/salesManager/carFactorySalesManager/partTransOrderDtl.jsp"; //生成发运单
	
	String PART_DLR_INSTOCK_MAIN ="/jsp/parts/salesManager/carFactorySalesManager/partDlrInstockMain.jsp"; //发运单
	String PART_DLR_INSTOCK_DETAIL ="/jsp/parts/salesManager/carFactorySalesManager/partDlrInstockDetail.jsp"; //发运单详细
	String PART_DLR_INSTOCK ="/jsp/parts/salesManager/carFactorySalesManager/partDlrInstock.jsp"; //发运单详细
	

	//配件拆合件申请
	String PART_SPLIT_APPLY_QUERY_URL = "/jsp/parts/storageManager/partSplitManager/partSplitApply/partSplitApplyQuery.jsp";//配件拆合申请查询页面
	String PART_SPLIT_APPLY_ADD_URL = "/jsp/parts/storageManager/partSplitManager/partSplitApply/partSplitApplyAdd.jsp";//配件拆合申请页面
	String PART_RETURN_APPLY_DETAIL_URL = "/jsp/parts/storageManager/partSplitManager/partSplitApply/partSplitApplyDetail.jsp";//配件拆合申请明细页面
	//配件拆合件审核
	String PART_SPLIT_CHK_QUERY_URL = "/jsp/parts/storageManager/partSplitManager/partSplitChk/partSplitChkQuery.jsp";//配件拆合审核查询页面
	String PART_SPLIT_CHK_URL = "/jsp/parts/storageManager/partSplitManager/partSplitChk/partSplitChk.jsp";//配件拆合审核页面
	//配件拆合件出入库
	String PART_SPLIT_INOROUT_URL = "/jsp/parts/storageManager/partSplitManager/partSplitInOrOut/partSplitInOrOut.jsp";//配件拆合出入库页面
	String PART_SPLIT_INOROUT_DETAIL_URL = "/jsp/parts/storageManager/partSplitManager/partSplitInOrOut/partSplitInOrOutDetail.jsp";//配件拆合出入库明细页面
	String PART_SPLIT_INOROUT_DETAILPRINT_URL = "/jsp/parts/storageManager/partSplitManager/partSplitInOrOut/partSplitInOrOutDetailPrint.jsp";//配件拆合出入库明细打印页面
	//配件调拨
	String PART_TRANSFER_QUERY_URL = "/jsp/parts/partAllotManager/partTransferManager/partTransferQuery.jsp";//配件调拨单查询页面
	String PART_TRANSFER_ADD_URL = "/jsp/parts/partAllotManager/partTransferManager/partTransferAdd.jsp";//配件调拨单新增页面
	String PART_TRANSFER_ERROR_URL = "/jsp/parts/partAllotManager/partTransferManager/partTransferError.jsp";//配件调拨错误页面
	String PART_TRANSFER_DETAIL_URL = "/jsp/parts/partAllotManager/partTransferManager/partTransferDetail.jsp";//配件调拨明细页面
	String PART_TRANSFER_UPDATE_URL = "/jsp/parts/partAllotManager/partTransferManager/partTransferMod.jsp";//配件调拨明细页面
	String PART_TRANSFERCHK_QUERY_URL = "/jsp/parts/partAllotManager/partTransferManager/partTransferChkQuery.jsp";//配件调拨单审核查询页面
	String PART_TRANSFER_CHK_URL = "/jsp/parts/partAllotManager/partTransferManager/partTransferChk.jsp";//配件调拨审核页面
		
	//配件BO
	String PART_BO_QUERY_URL = "/jsp/parts/salesManager/carFactorySalesManager/partBoQuery.jsp";//BO查询页面
	String PART_BO_QUERYALL_URL = "/jsp/parts/salesManager/carFactorySalesManager/partBoQueryAll.jsp";//BO汇总查询页面
	String PART_BO_RISK_QUERY_URL = "/jsp/parts/salesManager/carFactorySalesManager/partBoRiskQuery.jsp";//BO风险查询页面
	String PART_BO_RATEGYZX_QUERY_URL = "/jsp/parts/salesManager/carFactorySalesManager/partBoRateGyzxQuery.jsp";//BO率查询页面(供应中心)
	String PART_SALES_REPORT_QUERY_URL = "/jsp/parts/salesManager/carFactorySalesManager/partSalesReportQuery.jsp";//配件销售报表查询页面
	String PART_SALES_REPORTDIRECT_QUERY_URL = "/jsp/parts/salesManager/carFactorySalesManager/partSalesDirectReportQuery.jsp";//直发销售报表查询页面
	String PART_SALES_REPORTGYZX_QUERY_URL = "/jsp/parts/salesManager/carFactorySalesManager/partSalesReportGyzxQuery.jsp";//配件销售报表(供应中心)查询页面
	String PART_SALES_DTL_REPORTLOC_QUERY_URL = "/jsp/parts/salesManager/carFactorySalesManager/partSalesDtlReportLocQuery.jsp";//配件销售明细(本部)查询页面
	String PART_SALES_DTL_REPORTGYZX_QUERY_URL = "/jsp/parts/salesManager/carFactorySalesManager/partSalesDtlReportGyzxQuery.jsp";//配件销售明细(供应中心)查询页面
	String PART_BO_RISK_DTL_URL = "/jsp/parts/salesManager/carFactorySalesManager/partBoRiskDtlQuery.jsp";//BO风险订单明细页面
	String PART_BO_CYCLE_QUERY_URL = "/jsp/parts/salesManager/carFactorySalesManager/partBoCycleQuery.jsp";//BO周报表查询页面
	String PART_BO_QUERYDTL_URL = "/jsp/parts/salesManager/carFactorySalesManager/partBoQueryDtl.jsp";//BO所有明细查询页面
	String PART_BO_DETAIL_URL = "/jsp/parts/salesManager/carFactorySalesManager/partBoDetail.jsp";//BO明细查询页面
	String PART_BOHANDLE_QUERY_URL = "/jsp/parts/salesManager/carFactorySalesManager/partBoHandleQuery.jsp";//BO处理查询页面
	String PART_BO_DETAIL_CLOSE_URL = "/jsp/parts/salesManager/carFactorySalesManager/partBoDetailClose.jsp";//BO明细关闭页面
	String PART_BO_TOSALORDER_URL = "/jsp/parts/salesManager/carFactorySalesManager/partBoToSalOrder.jsp";//BO转销售单页面
	String PART_BO_TOSALORDER_CHK_URL = "/jsp/parts/salesManager/carFactorySalesManager/partBoToSalOrderChk.jsp";//BO转销售单审核页面
	String PART_BO_TOSALORDER_CHKALL_URL = "/jsp/parts/salesManager/carFactorySalesManager/partBoToSalOrderChkAll.jsp";//BO转销售单审核页面
	String PART_OFF_REPORT_QUERY_URL = "/jsp/report/partSalesReport/partOffReportQuery.jsp";//配件配件流失率分析报表查询页面
	
	//配件折扣率维护
	String PART_DISCOUNT_QUERY_URL = "/jsp/parts/baseManager/partsBaseManager/partDiscount/partDiscountQuery.jsp";//配件折扣率查询页面
	String PART_DISCOUNT_ADD_URL = "/jsp/parts/baseManager/partsBaseManager/partDiscount/partDiscountAdd.jsp";//配件折扣率新增页面
	String PART_DISCOUNT_MOD1_URL = "/jsp/parts/baseManager/partsBaseManager/partDiscount/partDiscountMod1.jsp";//配件折扣率(服务商类型)修改页面
	String PART_DISCOUNT_MOD2_URL = "/jsp/parts/baseManager/partsBaseManager/partDiscount/partDiscountMod2.jsp";//配件折扣率(服务商)修改页面
	String PART_DISCOUNT_MOD3_URL = "/jsp/parts/baseManager/partsBaseManager/partDiscount/partDiscountMod3.jsp";//配件折扣率(配件种类)修改页面
	String PART_DISCOUNT_MOD4_URL = "/jsp/parts/baseManager/partsBaseManager/partDiscount/partDiscountMod4.jsp";//配件折扣率(订单金额)修改页面
	
	String PART_OUTSTOCK_ORDER_DETAIL ="/jsp/parts/salesManager/carFactorySalesManager/partOutstockDetail.jsp"; //出库详细页面
	
	//zhumingwei add 2013-10-23 配件进销存数据统计 
	String partsInvoicingData_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/partsInvoicingData.jsp";
	//zhumingwei add 2013-10-23 添加销售快报
	String PART_DLR_ORDER_SO_Detail1 ="/jsp/parts/salesManager/carFactorySalesManager/partSoMainDetail1.jsp";  
	String PART_DLR_ORDER_OUT_Detail1 ="/jsp/parts/salesManager/carFactorySalesManager/partOutMainDetail1.jsp";

	//zhumingwei add 2013-10-28 待入库明细 
	String partsDInData_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/partsDInData.jsp";
	String partsYxzyData_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/partsYxzyData.jsp";
	
    //合同管理
    String PART_CONTRACT_QUERY="/jsp/parts/baseManager/PartContractManager/partContractList.jsp";//合同管理查询页面
    String PART_CONTRACT_QUERY_SEARCH="/jsp/parts/baseManager/PartContractManager/partContractQuery.jsp";//合同管理查询页面
    String PART_CONTRACT_ADD="/jsp/parts/baseManager/PartContractManager/addContract.jsp";//合同管理新增页面
    String PART_CONTRACT_CHOOSE="/jsp/parts/baseManager/PartContractManager/queryPartsForAdd.jsp";//合同管理新增选择备件页面
    String CONTRACT_PART_INIT="/jsp/parts/baseManager/PartContractManager/partShow.jsp";
    
    // 配件库存调整管理
    String PART_STOCK_NUM_APPLY="/jsp/parts/storageManager/stockNumManager/apply/partStockNumApplyQuery.jsp";// 配件库存调整申请页面
    String PART_STOCK_NUM_APPLY_ADD="/jsp/parts/storageManager/stockNumManager/apply/partStockNumApplyAdd.jsp";// 配件库存调整新增申请页面
    String PART_STOCK_NUM_APPLY_MOD="/jsp/parts/storageManager/stockNumManager/apply/partStockNumApplyMod.jsp";// 配件库存调整新增申请页面
    String PART_STOCK_NUM_APPLY_VIEW="/jsp/parts/storageManager/stockNumManager/apply/partStockNumApplyView.jsp";// 配件库存调整新增申请页面
    String PART_STOCK_NUM_CHK_QUERY="/jsp/parts/storageManager/stockNumManager/check/partStockNumCheckQuery.jsp";// 配件库存调整审核列表页面
    String PART_STOCK_NUM_CHK="/jsp/parts/storageManager/stockNumManager/check/partStockNumCheck.jsp";// 配件库存调整审核页面
    String PART_STOCK_NUM_CHK_VIEW="/jsp/parts/storageManager/stockNumManager/check/partStockNumCheckView.jsp";// 配件库存调整审核查看详情页面

	String ScrollSalesUrl="/jsp/parts/purchaseManager/scrollManager/scrollSales.jsp";//滚动计划编制
	
	//计划维护
		String ScrollSelectUtl="/jsp/parts/purchaseManager/scrollManager/scrollPlanSelect.jsp";//滚动计划查询
		
		String ScrollSalesUrlDtl="/jsp/parts/purchaseManager/scrollManager/scrollSalesDtl.jsp";//滚动计划编制
		String SALES_SUB_VIEW_URL="/jsp/parts/purchaseManager/scrollManager/salesSubView.jsp";//滚动计划导入查看
		String ScrollPlanUrl="/jsp/parts/purchaseManager/scrollManager/scrollPlan.jsp";//滚动计划确认(计划)
		String ScrollPurchaseUrl="/jsp/parts/purchaseManager/scrollManager/scrollPurchase.jsp";//滚动计划确认(采购)
		String ScrollGYSUrl="/jsp/parts/purchaseManager/scrollManager/scrollGYS.jsp";//滚动计划确认(供应商)
		String PlanSplitUrl="/jsp/parts/purchaseManager/scrollManager/planSplit.jsp";//计划拆分
		String SPConcertUrl="/jsp/parts/purchaseManager/scrollManager/spConcert.jsp";//采销协处理
		
		String PART_PURCHASEORDERM_DELETE_URL = "/jsp/parts/purchaseManager/purchaseOrderDelete.jsp";//采购交接单批量作废
		String PART_PURCHASEORDERM_QUERY_PRINT_URL = "/jsp/parts/purchaseManager/purchaseOrderQueryInPrint.jsp";//采购订单查询
		String PART_PURCHASEORDERM_QUERY_PRINT_ERP_URL = "/jsp/parts/purchaseManager/purchaseOrderQueryInERPPrint.jsp";//采购订单查询
		
		String PURCHASEORDER_IN_PRINT_URL = "/jsp/parts/purchaseManager/purchaseOrderInPrint.jsp";//两联单打印
		String PURCHASEORDER_IN_PRINT_ERP_URL = "/jsp/parts/purchaseManager/purchaseOrderInERPPrint.jsp";//三联单打印
		
		String AddPurchasePlanUrl="/jsp/parts/purchaseManager/addManager/addPurchasePlan.jsp";//补充计划编制
		String ADD_PURCHASE_NEED_QUERY="/jsp/parts/purchaseManager/addManager/addPurchaseNeed.jsp";//补充需求编制
		String PART_FORECAST_PO_QUERY="/jsp/parts/purchaseManager/addManager/partForecastPoQuery.jsp";//需求与备件交接单差异明细表
		String PART_FORECAST_PO_SEL="/jsp/parts/purchaseManager/addManager/partForecastPoSelQuery.jsp";//需求计划交付完成明细表
		String AddPlanUrl="/jsp/parts/purchaseManager/addManager/addPlanDel.jsp";//补充计划新增
		String FORECAST_VIEW="/jsp/parts/purchaseManager/addManager/UploadForecastView.jsp";//补充需求导入展示页面
		String INPUT_ERROR="/jsp/parts/purchaseManager/addManager/inputError.jsp";//补充需求导入错误页面
		String FORECAST_UPDATE="/jsp/parts/purchaseManager/addManager/updateForecastOem.jsp";//补充需求修改页面
		String FORECAST_UPDATE_LOG="/jsp/parts/purchaseManager/addManager/updateForecastOemLogView.jsp";//补充需求修改页面
		String QueryPlanDelRUL="/jsp/parts/purchaseManager/addManager/queryPlanDel.jsp";//补充计划查看
		
		
	// 配件财务管理
	String PART_PART_DEALER_ACC_CHK_QUERY  = "/jsp/parts/financeManager/dealerAccChkMag/dealerAccChkQuery.jsp"; // 配件打款登记审核
}
