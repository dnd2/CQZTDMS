package com.infodms.dms.actions.parts.planManager;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.parts.image.PartImgManager;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.parts.baseManager.partPlannerQueryManager.partPlannerQueryDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.planManager.PartPlanManagerDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrInstockDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartBuyPricePO;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartOemPoPO;
import com.infodms.dms.po.TtPartPlanDetailPO;
import com.infodms.dms.po.TtPartPlanMainPO;
import com.infodms.dms.po.TtPartPlanScrollDelPO;
import com.infodms.dms.po.TtPartPlanScrollPO;
import com.infodms.dms.po.TtPartPoDtlPO;
import com.infodms.dms.po.TtPartPoInPO;
import com.infodms.dms.po.TtPartPoMainPO;
import com.infodms.dms.po.TtPartRecordPO;
import com.infodms.dms.po.TtPartSplitDefinePO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.po.VwPartStockPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 配件计划管理
 * @author fanzhineng
 * @Date 2017年4月11日09:51:26
 */
/**
 * @author fanzhineng
 *
 */
public class PartPlanManager extends BaseImport implements PTConstants{
	public static final Logger logger = Logger.getLogger(PartPlanManager.class);
	private static final PartPlanManagerDao dao = PartPlanManagerDao.getInstance();
	/*=================================URL START=======================================*/
	/**
	 * 跳转到滚动计划编制页面
	 */
	private static final String TO_ROLLING_PLAN_INIT = "/jsp/parts/planManager/rollingPlanManager/toRollingPlanInit.jsp";
	
	private static final String TO_ROLLING_PLAN_INIT_Z = "/jsp/parts/planManager/rollingPlanManager/toRollingPlanInitZ.jsp";
	
	private static final String toPlanSettingAddUrl = "/jsp/parts/planManager/rollingPlanManager/toPlanAdd.jsp";//新增维护
	
	private static final String toPlanSettingAddUrl2 = "/jsp/parts/planManager/rollingPlanManager/toPlanAddJJ.jsp";//新增维护
	/**
	 * 配件计划确认（非计划室）
	 */
	private static final String TO_PART_PLAN_VALIDATION = "/jsp/parts/planManager/rollingPlanManager/toPartPlanValidation.jsp";
	private static final String TO_PART_PLAN_VALIDATION_z = "/jsp/parts/planManager/rollingPlanManager/toPartPlanValidationZ.jsp";
	/**
	 * 配件计划确认（计划室）
	 */
	private static final String TO_PART_PLAN_VALIDATION_JHS = "/jsp/parts/planManager/rollingPlanManager/toPartPlanValidationJhs.jsp";
	/**
	 * 跳转到滚动计划编制修改页面
	 */
	private static final String TO_ROLLING_PLAN_EDIT = "/jsp/parts/planManager/rollingPlanManager/toRollingPlanEdit.jsp";
	
	private static final String TO_ROLLING_PLAN_SH = "/jsp/parts/planManager/rollingPlanManager/toRollingPlanSh.jsp";
	private static final String TO_ROLLING_PLAN_SH_Z = "/jsp/parts/planManager/rollingPlanManager/toRollingPlanShZ.jsp";
	/**
	 * 跳转到滚动计划编制查看页面
	 */
	private static final String TO_ROLLING_PLAN_SELECT = "/jsp/parts/planManager/rollingPlanManager/toRollingPlanSelect.jsp";
	/**
	 * 跳转到采购订单查询页面
	 */
	private static final String TO_PURCHASE_ORDER_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurchaseOrderInit.jsp";
	/**
	 * 跳转到采购订单入库初始页面
	 */
	private static final String TO_PURCHASE_ORDER_INWH_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurchaseOrderInwhInit.jsp";

	/**
	 * 跳转到RF采购订单扫描入库页面
	 */
	private static final String TO_RF_PURCHASE_SCAN_IN_STOCK = "/jsp/parts/planManager/rfManager/toRFPurchaseScanInStock.jsp";
	/**
	 * 跳转到采购订单明细查看页面
	 */
	private static final String TO_PURCHAR_ORDER_MX = "/jsp/parts/planManager/purchaseOrderManager/toPurcharOrderMx.jsp";
	/**
	 * 跳转到采购订单ASN单打印页面
	 */
	private static final String TO_PURCHAR_ORDER_ASN_PRINT = "/jsp/parts/planManager/purchaseOrderManager/toPurcharOrderAsnPrint.jsp";
	/**
	 * 跳转到采购订单二维码打印页面
	 */
	private static final String TO_PURCHAR_ORDER_QR_PRINT = "/jsp/parts/planManager/purchaseOrderManager/toPurcharOrderQrPrint.jsp";
	/**
	 * 跳转到中储采购订单查询页面
	 */
	private static final String TO_PURCHASE_ORDER_CMST_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurchaseOrderCMSTInit.jsp";
	/**
	 * 跳转到中储采购订单打印页面
	 */
	private static final String TO_PURCHASE_ORDER_CMST_PRINT_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurchaseOrderCMSTPrintInit.jsp";
	
	
	private static final String TO_PURCHASE_ORDER_CMST_PRINT = "/jsp/parts/planManager/purchaseOrderManager/toPurchaseOrderCMSTPrint.jsp";
	
	/**
	 * 跳转到采购订单涂装确认页面
	 */
	private static final String TO_PURCHASE_ORDER_PAINTING_CONFIRM = "/jsp/parts/planManager/rollingPlanManager/toPurchaseOrderPaintingConfirm.jsp";
	/**
	 * 跳转到采购订单焊装确认页面
	 */
	private static final String TO_PURCHASE_ORDER_WELDING_CONFIRM = "/jsp/parts/planManager/rollingPlanManager/toPurchaseOrderWeldingConfirm.jsp";
	/**
	 * 跳转到补充计划编制页面
	 */
	private static final String TO_SUPPLEMENT_PLAN_INIT = "/jsp/parts/planManager/rollingPlanManager/toSupplementPlanInit.jsp";
	/**
	 * 跳转到采购到货确认页面
	 */
	private static final String TO_PUR_RCV_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurRcvInit.jsp";
	/**
	 * 跳转到采购到货查询页面
	 */
	private static final String TO_PUR_RCV_SELECT_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurRcvSelectInit.jsp";
	/**
	 * 跳转到ASN单打印页面
	 */
	private static final String TO_PUR_CONFIRM_ASN_PRINT = "/jsp/parts/planManager/purchaseOrderManager/toPurConfirmAsnPrint.jsp";
	/**
	 * 跳转到采购到货入库初始页面
	 */
	private static final String TO_PUR_RCV_INWH_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurRcvInwhInit.jsp";
	/**
	 * 跳转到采购到货查询页面
	 */
	private static final String TO_PUR_RCV_PRINT_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurRcvPrintInit.jsp";
	/**
	 * 跳转到采购提货初始页面
	 */
	private static final String TO_PUR_PICK_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurPickInit.jsp";
	/**
	 * 跳转到采购提货查询初始页面
	 */
	private static final String TO_PUR_PICK_SELECT_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurPickSelectInit.jsp";
	/**
	 * 跳转到采购提货查询初始页面
	 */
	private static final String TO_PUR_PICK_PRINT_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurPickPrintInit.jsp";
	/**
	 * 跳转到提货单打印页面
	 */
	private static final String TO_PUR_PICK_PRINT = "/jsp/parts/planManager/purchaseOrderManager/toPurPickPrint.jsp";
	/**
	 * 跳转到采购订单查询页面
	 */
	private static final String TO_PUR_ORDER_FACTORY_SELECT_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurOrderFactorySelectInit.jsp";
	/**
	 * 跳转到采购订单关闭页面
	 */
	private static final String TO_PUR_ORDER_CLOSE_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurOrderCloseInit.jsp";
	/**
	 * 跳转到采购订单打印页面
	 */
	private static final String TO_PUR_ORDER_FACTORY_PRINT_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurOrderFactoryPrintInit.jsp";
	/**
	 * 打印采购送货单
	 */
	private static final String TO_PUR_ORDER_FACTORY_PRINT = "/jsp/parts/planManager/purchaseOrderManager/toPurOrderFactoryPrint.jsp";
	/**
	 * 跳转到供应商打印送货单页面
	 */
	private static final String TO_PUR_ORDER_VENDER_PRINT_INIT = "/jsp/parts/planManager/purchaseOrderManager/toPurOrderVenderPrintInit.jsp";
	/**
	 * 跳转到计划导入显示
	 */
	private static final String TO_PLAN_IMP_VIEW = "/jsp/parts/planManager/rollingPlanManager/toPlanImpView.jsp";
	/*=================================URL END=========================================*/
	/**
	 * 跳转到滚动计划编制页面
	 */
	public void toRollingPlanInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			String planYear = DateUtil.format(new Date(), "yyyy");
			String planMonth = DateUtil.format(new Date(), "M");
			act.setOutData("planYear", planYear);
			act.setOutData("planMonth", planMonth);
			
			
			
			act.setForword(TO_ROLLING_PLAN_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到滚动计划编制页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void toRollingPlanInitZ(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			String planYear = DateUtil.format(new Date(), "yyyy");
			String planMonth = DateUtil.format(new Date(), "M");
			act.setOutData("planYear", planYear);
			act.setOutData("planMonth", planMonth);
			
			
			
			act.setForword(TO_ROLLING_PLAN_INIT_Z);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到滚动计划编制页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到滚动计划编制页面
	 */
	public void toRollingPlanEdit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String planId = request.getParamValue("planId");
			String planTypes = request.getParamValue("planTypes");
            act.setOutData("planId", planId);
            act.setOutData("planTypes", planTypes);
			act.setForword(TO_ROLLING_PLAN_EDIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到滚动计划编制页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void toRollingPlanSh(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String planId = request.getParamValue("planId");
			String planTypes = request.getParamValue("planTypes");
            act.setOutData("planId", planId);
            act.setOutData("planTypes", planTypes);
			act.setForword(TO_ROLLING_PLAN_SH);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到滚动计划编制页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void toRollingPlanShZ(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String planId = request.getParamValue("planId");
			String planTypes = request.getParamValue("planTypes");
            act.setOutData("planId", planId);
            act.setOutData("planTypes", planTypes);
			act.setForword(TO_ROLLING_PLAN_SH_Z);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到滚动计划编制页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到滚动计划编制查看页面
	 */
	public void toRollingPlanSelect(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String planId = request.getParamValue("planId");
			String planTypes = request.getParamValue("planTypes");
            act.setOutData("planId", planId);
            act.setOutData("planTypes", planTypes);
			act.setForword(TO_ROLLING_PLAN_SELECT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到滚动计划编制查看页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询计划信息明细
	 */
	public void getRollingPlanInfoById(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getRollingPlanInfoById(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询计划信息明细异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 下载计划 EXECEL模板
	 */
	public void downRollingPlanTemp(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
		try{
			String PLAN_TYPES = request.getParamValue("PLAN_TYPES");
			String fileNamez = "";
			if(Constant.PART_PURCHASE_PLAN_TYPE_01.toString().equals(PLAN_TYPES)){
				fileNamez = "月度计划编制模板";
			}else{
				fileNamez = "周度计划编制模板";
			}
			
			List<Map<String, Object>> listRs = null;
			List<String> listHead = new ArrayList<String>();//导出模板第一列
            //标题
			listHead.add("配件编码 ");
			listHead.add("计划数量");
			listHead.add("配件备注");
            
            List<String> listKey = new ArrayList<String>();
            
			// 导出的文件名
            String fileName = fileNamez+DateUtil.format(new Date(), "yyyy_MM_dd")+".xls";
            // 分页导出
            pagingExportExcel(response,fileName,listHead,listKey,listRs,50000);
            
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"下载计划 EXECEL模板异常");
			logger.error(loginUser,e1);
			act.setException(e1);
			this.toRollingPlanInit();
		}
	}
	/**
     * @param :
     * @return :
     * @throws : liu
     * @Title :
     * @Description: 查询配件
     */
    public void showPartBase() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
            //分页方法 begin
            Integer curPage = req.getParamValue("curPage") != null ? Integer
                    .parseInt(req.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.showPartBase(req, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询配件失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    /**
     * @param :
     * @return :
     * @throws : liu
     * @Title :
     * @Description: 保存计划
     */
    public void savePlan() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            String[] partIdArr = req.getParamValues("cb");
            Long planId = Long.parseLong(SequenceManager.getSequence(""));
            String yearMonth = CommonUtils.checkNull(req.getParamValue("createDate"));//计划日期
            String planType = CommonUtils.checkNull(req.getParamValue("planType"));   //计划类型
            //String planState = CommonUtils.checkNull(req.getParamValue("planState"));
            String t = CommonUtils.checkNull(req.getParamValue("t"));
            String remark = CommonUtils.checkNull(req.getParamValue("remark"));  //备注
            //String planCycle = CommonUtils.checkNull(req.getParamValue("PLAN_CYCLE"));  //订货周期
            //String comeCycle = CommonUtils.checkNull(req.getParamValue("COME_CYCLE"));  //到货周期
            //String isUrgentIn = CommonUtils.checkNull(req.getParamValue("IS_URGENT_IN"));  //是否紧急入库
            //Long createBy = loginUser.getUserId(); //制单人，计划人
            //Long orgId = loginUser.getCompanyId(); //制单单位
            //String planCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_01);//获取单据编码
            
            
            
            String errInfos = "";
            List<Map<String, Object>> errList = new ArrayList<Map<String, Object>>();
            int scount = 0;//成功数量累计
            
            String planNo = "";
            
            //写入主数据
            TtPartPlanScrollPO mainPo = new TtPartPlanScrollPO();
            mainPo.setId(planId);
            if (Constant.PART_PURCHASE_PLAN_TYPE_01.toString().equals(planType)) {
            	//滚动计划
                mainPo.setPlanTypes(Constant.PART_PURCHASE_PLAN_TYPE_01);
                mainPo.setPlanNo(OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_60));
                //mainPo.setPlanType(Constant.ADD_PLAN_TYPE_GD);
            } else {
            	//补充计划
            	mainPo.setPlanTypes(Constant.PART_PURCHASE_PLAN_TYPE_04);
                mainPo.setPlanNo(OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_61));
                //mainPo.setPlanType(Constant.ADD_PLAN_TYPE_BC);
            }
            mainPo.setCreateType(Constant.PART_PURCHASE_PLAN_CREATE_TYPE_02);
            mainPo.setCreateBy(loginUser.getUserId());
            mainPo.setCreateDate(new Date());
            //YearMonth增加一个月
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONDAY, 1);
            date = cal.getTime();
            mainPo.setYearMonth(date);
            DateUtil dti=new DateUtil();
            String planDate=dti.format(date, "yyyy-MM-dd");
            String d=planDate.replace("-","");
            String d1=d.substring(0, 6);
            mainPo.setMonthDate(d1);
            mainPo.setRemark(remark);
            mainPo.setStatus(0);
            planNo = mainPo.getPlanNo();
            //提交
            if ("2".equals(t)) {
            	mainPo.setSubmitDate(new Date());
            	mainPo.setIsSubmit(Constant.IF_TYPE_YES);
            	mainPo.setSubmitBy(loginUser.getUserId());
            	//mainPo.setStatus(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_02);
            }else{
            	mainPo.setIsSubmit(Constant.IF_TYPE_NO);
            	//mainPo.setStatus(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_01);
            }
            dao.insert(mainPo);
            //添加从表
            List<TtPartPlanScrollDelPO> listInsert = new ArrayList<TtPartPlanScrollDelPO>();
            if (partIdArr != null) {
                for (int i = 0; i < partIdArr.length; i++) {
                	 String partId = partIdArr[i];
                     Long plineId = Long.parseLong(SequenceManager.getSequence(""));
                     String partCode = CommonUtils.checkNull(req.getParamValue("partCode_" + partId));//件号
                     String partOldcode = CommonUtils.checkNull(req.getParamValue("partOldcode_" + partId));   //配件编码
                     String partCname = CommonUtils.checkNull(req.getParamValue("partCname_" + partId)); //配件名称
                     String partType = CommonUtils.checkNull(req.getParamValue("partType_" + partId)); //配件属性
                     String venderId = CommonUtils.checkNull(req.getParamValue("venderId_" + partId)); //供应商id
                     String unit = CommonUtils.checkNull(req.getParamValue("unit_" + partId)); //单位
                     String minPackage = CommonUtils.checkNull(req.getParamValue("minPackage_" + partId)); //最小包装量
                     String salePrice3 = CommonUtils.checkNull(req.getParamValue("salePrice3_" + partId));//计划价
                     String planQty = CommonUtils.checkNull(req.getParamValue("plan_qty_" + partId));    //计划数量
                     String planAmount = CommonUtils.checkNull(req.getParamValue("planAmount_" + partId)); //计划金额
                     String itemQty = CommonUtils.checkNull(req.getParamValue("itemQty_" + partId)); //可用库存
                     String boQty = CommonUtils.checkNull(req.getParamValue("boQty_" + partId));//BO量
                     String avgQty = CommonUtils.checkNull(req.getParamValue("avgQty_" + partId)); //月均销量
                     String yearQty = CommonUtils.checkNull(req.getParamValue("yearQty_" + partId)); //年均销量
                     String hfyearQty = CommonUtils.checkNull(req.getParamValue("hfyearQty_" + partId)); //半年均销量
                     String quarterQty = CommonUtils.checkNull(req.getParamValue("quarterQty_" + partId)); //季均销量
                     String orderQty = CommonUtils.checkNull(req.getParamValue("orderQty_" + partId));//在途数量
                     String safetyStock = CommonUtils.checkNull(req.getParamValue("safetyStock_" + partId));//安全库存
                     String preArriveDate = CommonUtils.checkNull(req.getParamValue("preArriveDate_" + partId)); //预计到货日期
                     String detailRemark = CommonUtils.checkNull(req.getParamValue("remark_" + partId)); //备注
                
                     TtPartPlanScrollDelPO po = new TtPartPlanScrollDelPO();
                     po.setId(Long.parseLong(SequenceManager.getSequence(null)));
                     po.setPlanId(mainPo.getId());
                     po.setPlanNo(mainPo.getPlanNo());
                     po.setPlanOrgId(loginUser.getOrgId());
                     po.setPartId(Long.parseLong(partId));
                     po.setPlanNum(Integer.parseInt(planQty));
                     po.setPartOldcode(partOldcode);
                     po.setPlanTypes(Constant.PART_PURCHASE_PLAN_TYPE_04);
                     //po.setPlanMonthOne(Integer.parseInt(planMonthOne));
                     po.setPlanRemark(detailRemark);
                     po.setVenderId(Long.parseLong(venderId));
                     po.setExeclLine(i + 1);
                     DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                     po.setForecastDate(format1.parse(preArriveDate));
                     po.setCreateBy(loginUser.getUserId());
                     po.setCreateDate(new Date());
                     po.setPlanBy(loginUser.getUserId());
                    //po.setWhId(Long.parseLong(req.getParamValue("wh_id")));
                if (Constant.PART_PURCHASE_PLAN_TYPE_01.toString().equals(planType)) {
                    po.setPlanTypes(Constant.ADD_PLAN_TYPE_GD);
                } else {
                	po.setPlanTypes(Constant.ADD_PLAN_TYPE_BC);
                }
                po.setMonthDate(yearMonth);
                po.setCheckNum(Integer.parseInt(planQty));
                
                listInsert.add(po);
                scount++;
                }
            }
            dao.insert(listInsert);
            
            //写入配件ID，收货库房，供应商，订单周期 ，修改总成件状态
            dao.impExcelUpdatePart(planId+"");
            if("2".equals(t)){
            	//效验错误数据
    			String sql = "SELECT * FROM VW_PART_PLAN_IMP_ERR_1 V WHERE V.PLAN_ID = '"+planId+"'";
    			List<Map<String,Object>> listErr = dao.pageQuery(sql, null,null);
    			if(listErr!=null && !listErr.isEmpty() && listErr.size()>0){
    				act.setOutData("error", "提交失败！配件编码："+listErr.get(0).get("INFO").toString());
    				return;
    			}
            }
            //验证是否存在无ID的配件，即该配件不存在
            List<Map<String,Object>> noParts = dao.getNotSaveParts(planId+"");
            if(noParts!=null && !noParts.isEmpty() && noParts.size()>0){
            	for (int k = 0; k < noParts.size(); k++) {
            		Map<String,Object> mapt = noParts.get(k);
            		String partOldcodeT = CommonUtils.checkNull(mapt.get("PART_OLDCODE"));
            		if(StringUtil.notNull(partOldcodeT)){
            			errInfos += "配件["+partOldcodeT+"]不存在，请先维护基础信息到系统！<br>";
            		}
            		
				}
            }
            if(StringUtil.notNull(errInfos)){
            	throw new BizException(act, ErrorCodeConstant.QUERY_FAILURE_CODE, "导入滚动计划编制异常！");
            }
            
            //只保留一条重复数据，即最后出现的数据，其他删除，查询视图打上错误标识
            dao.delErrPlan(planId+"");
            
            //收货库房，供应商，订单周期,拆分总成件
			dao.allSub(planId+"", loginUser);
			
            //查询错误数据            
        	
/*            act.setOutData("errList", errList);
            act.setOutData("planId", planId);
            act.setOutData("planNo", planNo);
            act.setOutData("scount", scount);
            act.setOutData("ecount", errList.size());
            act.setOutData("PLAN_TYPES", PLAN_TYPES);
            act.setForword(TO_PLAN_IMP_VIEW);*/
            act.setOutData("success", "操作成功!");
           /* if (planType.equals(Constant.PART_PURCHASE_PLAN_TYPE_01.toString())) {
                act.setForword(purchasePlanSettingUrl);
            } else {
                act.setForword(purchasePlanSettingUrl2);
            }*/
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件计划保存失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setOutData("error", "保存失败!");
        }
    }
	/**
	 * 导入滚动计划编制
	 */
	@SuppressWarnings("unchecked")
	public void impRollingPlan(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		
		String errInfos = "";
		
        String MYYEAR = request.getParamValue("planYearImp");
        String MYMONTH = request.getParamValue("planMonthImp");
        String PLAN_TYPES = request.getParamValue("PLAN_TYPES");
        String uploadRemark = request.getParamValue("uploadRemark");
        if(StringUtil.notNull(uploadRemark)){
        	if(uploadRemark.length()>150){
        		uploadRemark = uploadRemark.substring(0, 150);
        	}
        }
        
        FileObject uploadFile = request.getParamObject("uploadFile");//获取导入文件
        if (uploadFile == null) {//文件为空报空指针异常
            return;
        }
        String fileName = uploadFile.getFileName();//获取文件名
        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());//截取文件名
        ByteArrayInputStream inputStream = new ByteArrayInputStream(uploadFile.getContent());//获取文件数据

        Workbook wb = null;
        try {
            wb = Workbook.getWorkbook(inputStream);
            inputStream.reset();
            Sheet[] sheets = wb.getSheets();
            Sheet sheet = sheets[0];
            int rowNum = sheet.getRows();

            List<Map<String, Object>> errList = new ArrayList<Map<String, Object>>();
            int scount = 0;//成功数量累计
            
            	String planId = SequenceManager.getSequence("");
            String planNo = "";
            
            //写入主数据
            TtPartPlanScrollPO mainPo = new TtPartPlanScrollPO();
            mainPo.setId(Long.valueOf(planId));
            if (Constant.PART_PURCHASE_PLAN_TYPE_01.toString().equals(PLAN_TYPES)) {
            	//滚动计划
                mainPo.setPlanTypes(Constant.PART_PURCHASE_PLAN_TYPE_01);
                mainPo.setPlanNo(OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_60));
                //mainPo.setPlanType(Constant.ADD_PLAN_TYPE_GD);
            } else {
            	//补充计划
            	mainPo.setPlanTypes(Constant.PART_PURCHASE_PLAN_TYPE_04);
                mainPo.setPlanNo(OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_61));
                //mainPo.setPlanType(Constant.ADD_PLAN_TYPE_BC);
            }
            mainPo.setCreateType(Constant.PART_PURCHASE_PLAN_CREATE_TYPE_01);
            mainPo.setCreateBy(loginUser.getUserId());
            mainPo.setCreateDate(new Date());
            mainPo.setMonthDate(MYYEAR + MYMONTH);
            //YearMonth增加一个月
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONDAY, 1);
            date = cal.getTime();
            mainPo.setYearMonth(date);
            mainPo.setMonthDate(MYYEAR + MYMONTH);
            mainPo.setRemark(uploadRemark);
            planNo = mainPo.getPlanNo();
            dao.insert(mainPo);
            //组合配件编码进行一次查询
            List<TtPartPlanScrollDelPO> listInsert = new ArrayList<TtPartPlanScrollDelPO>();
            for (int j = 1; j < rowNum; j++) {
                Cell[] cells = sheet.getRow(j);
                
                String partOldcode = "";
                if(cells.length>=1){
                	partOldcode = CommonUtils.checkNull(cells[0].getContents().trim());
                }
                String planMonthOne = "0";
                if(cells.length>=2){
                	planMonthOne = CommonUtils.checkNull(cells[1].getContents().trim());
                }
                String salesRemark = "";
                if(cells.length>=3){
                	salesRemark = CommonUtils.checkNull(cells[2].getContents().trim());
                }
                
                TtPartPlanScrollDelPO po = new TtPartPlanScrollDelPO();
                po.setId(Long.parseLong(SequenceManager.getSequence(null)));
                po.setPlanId(mainPo.getId());
                po.setPlanNo(mainPo.getPlanNo());
                po.setPlanOrgId(loginUser.getOrgId());
                po.setPartOldcode(partOldcode);
                po.setPlanNum(Integer.parseInt(planMonthOne));
                po.setSalesRemark(salesRemark);
                po.setExeclLine(j + 1);
                po.setCreateBy(loginUser.getUserId());
                po.setCreateDate(new Date());
                if (Constant.PART_PURCHASE_PLAN_TYPE_01.toString().equals(PLAN_TYPES)) {
                    po.setPlanTypes(Constant.ADD_PLAN_TYPE_GD);
                } else {
                	po.setPlanTypes(Constant.ADD_PLAN_TYPE_BC);
                }

                po.setMonthDate(MYYEAR + MYMONTH);
              //  po.setCheckNum(Integer.parseInt(planMonthOne));
                
                listInsert.add(po);
                scount++;
            }
            dao.insert(listInsert);
            
            //写入配件ID，收货库房，供应商，订单周期 ，修改总成件状态
            dao.impExcelUpdatePart(planId);
            
            //验证是否存在无ID的配件，即该配件不存在
            List<Map<String,Object>> noParts = dao.getNotSaveParts(planId);
            if(noParts!=null && !noParts.isEmpty() && noParts.size()>0){
            	for (int k = 0; k < noParts.size(); k++) {
            		Map<String,Object> mapt = noParts.get(k);
            		String partOldcodeT = CommonUtils.checkNull(mapt.get("PART_OLDCODE"));
            		if(StringUtil.notNull(partOldcodeT)){
            			errInfos += "配件["+partOldcodeT+"]不存在，请先维护基础信息到系统！<br>";
            		}
            		
				}
            }
            if(StringUtil.notNull(errInfos)){
            	throw new BizException(act, ErrorCodeConstant.QUERY_FAILURE_CODE, "导入滚动计划编制异常！");
            }
            
            //只保留一条重复数据，即最后出现的数据，其他删除，查询视图打上错误标识
            dao.delErrPlan(planId);
            
            //收货库房，供应商，订单周期,拆分总成件
			dao.allSub(planId, loginUser);
			
            //查询错误数据
            //errList = dao.queryErr(planId);
        	
            act.setOutData("errList", errList);
            act.setOutData("planId", planId);
            act.setOutData("planNo", planNo);
            act.setOutData("scount", scount);
            act.setOutData("ecount", errList.size());
            act.setOutData("PLAN_TYPES", PLAN_TYPES);
            act.setOutData("success", "操作成功!");
            act.setForword(TO_PLAN_IMP_VIEW);			
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "导入滚动计划编制异常！");
            if(StringUtil.notNull(errInfos)){
            	e1.setMessage(errInfos);
            }
            logger.error(loginUser, e1);
            act.setException(e1);
            this.toRollingPlanInit();
        }
		
	}
	
	/**
	 * 查询滚动计划信息
	 */
	public void getRollingPlanInfo() {
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
        try {
        	//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getRollingPlanInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询滚动计划信息异常！");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }
	
	/**
	 * 查询计划编制
	 */
	public void queryScrollSalesMain() {
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
        try {
            String MYYEAR = request.getParamValue("MYYEAR");
            String MYMONTH = request.getParamValue("MYMONTH");
            String planNo = CommonUtils.checkNull(request.getParamValue("planNo"));// 配件名称
            String radioSelect = request.getParamValue("RADIO_SELECT");

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            //dao.changeStatus();
            if (radioSelect.equals("1")) {
                act.setOutData("ps", dao.queryScrollSalesMain(loginUser, MYYEAR, MYMONTH, Constant.PAGE_SIZE, curPage, Constant.PART_PURCHASE_PLAN_TYPE_01, Constant.ADD_PLAN_TYPE_GD, planNo));
            }
            if (radioSelect.equals("2")) {
                act.setOutData("ps", dao.queryScrollSalesMain(loginUser, MYYEAR, MYMONTH, Constant.PAGE_SIZE, curPage, Constant.PART_PURCHASE_PLAN_TYPE_02, Constant.ADD_PLAN_TYPE_BC, planNo));
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "滚动计划编制");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }
	
	/**
	 * 提交计划到待确认
	 */
	@SuppressWarnings("unchecked")
	public void subPlanStayConfim(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String planId = request.getParamValue("planId");
			TtPartPlanScrollPO sc = new TtPartPlanScrollPO();
			sc.setId(Long.valueOf(planId));
			
			TtPartPlanScrollPO scs = (TtPartPlanScrollPO) dao.select(sc).get(0);
			if(scs.getIsSubmit().equals(Constant.IF_TYPE_YES)){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "该计划已提交，请勿重复提交！");
			}
			//收货库房，供应商，订单周期,拆分总成件
			dao.allSub(planId, loginUser);
			//查询无供应商
			List<Map<String,Object>> vList = dao.allVenderIn(planId);
			if(vList!=null && !vList.isEmpty() && vList.size()>0){
				String err = "";
				for (Map<String, Object> map : vList) {
					String partOldcode = CommonUtils.checkNull(map.get("PART_OLDCODE"));
					err+= "配件["+partOldcode+"]无供应商，请维护！<br>";
				}
				if(StringUtil.notNull(err)){
					throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
				}
			}
			
			//效验错误数据
			String sql = "SELECT * FROM VW_PART_PLAN_IMP_ERR_1 V WHERE V.PLAN_ID = '"+planId+"'";
			List<Map<String,Object>> listErr = dao.pageQuery(sql, null,null);
			if(listErr!=null && !listErr.isEmpty() && listErr.size()>0){
				act.setOutData("error", "提交失败！配件编码："+listErr.get(0).get("INFO").toString());
				return;
			}
			
			//自动平均拆分滚动周度订单数量
			List<Object> ins = new LinkedList<Object>();
			ins.add(planId);
			dao.callProcedure("PKG_PLAN.P_QXC_AUTO_PLAN_WEEK_SPLIT",ins,null);
			
			TtPartPlanScrollPO scx = new TtPartPlanScrollPO();
			scx.setIsSubmit(Constant.IF_TYPE_YES);//提交
			scx.setSubmitDate(new Date());
			scx.setSubmitBy(loginUser.getUserId());
			dao.update(sc, scx);
			
			/*if(Constant.PART_PURCHASE_PLAN_TYPE_02.equals(scs.getPlanTypes())){
				//补充计划，不需要确认，直接提交生成采购订单,总成不能采购，只能采购分总成
				TtPartPlanScrollDelPO dtl = new TtPartPlanScrollDelPO();
				dtl.setPlanId(Long.valueOf(planId));
				
				List<TtPartPlanScrollDelPO> dtlList = dao.select(dtl);
				for (int i = 0; i < dtlList.size(); i++) {
					TtPartPlanScrollDelPO dtls = dtlList.get(i);
					
					Long partId = dtls.getPartId();
					TtPartSplitDefinePO spl = new TtPartSplitDefinePO();
					spl.setPartId(partId);
					List<TtPartSplitDefinePO> splList = dao.select(spl);
					if(splList==null || splList.isEmpty() || splList.size()==0){
						//总成拆分件，不能生产订单
						TtPartPlanScrollDelPO dtlTemp = new TtPartPlanScrollDelPO();
						dtlTemp.setId(dtls.getId());
						
						TtPartPlanScrollDelPO dtlTempx = new TtPartPlanScrollDelPO();
						dtlTempx.setFirsrConfirmBy(loginUser.getUserId());
						dtlTempx.setFirstConfirmDate(new Date());
						dtlTempx.setFirstConfirmNum(Long.valueOf(dtls.getPlanMonthOne().toString()));//一月
						dtlTempx.setFirstConfirmState(Constant.IF_TYPE_YES);
						dtlTempx.setFirstConfirmRemark("补充计划系统确认！");
						
						dtlTempx.setLastConfirmBy(loginUser.getUserId());
						dtlTempx.setLastConfirmDate(new Date());
						dtlTempx.setLastConfirmNum(dtlTempx.getFirstConfirmNum());
						dtlTempx.setLastConfirmState(Constant.IF_TYPE_YES);
						dtlTempx.setLastConfirmRemark("补充计划系统确认！");
						
						dtlTempx.setStatus(Constant.SCROLL_PLAN_STATUS_02);
						dtlTempx.setCheckNum(Integer.valueOf(dtlTempx.getLastConfirmNum().toString()));
						dtlTempx.setCheckDate(new Date());
						dtlTempx.setCheckBy(loginUser.getUserId());
						
						dao.update(dtlTemp, dtlTempx);
						
						//写入ttpartoempoTemp临时表
						TtPartOemPoTempPO temp = new TtPartOemPoTempPO();
						temp.setPlanLineId(dtls.getId());
						temp.setPlanId(dtls.getPlanId());
						temp.setPartId(dtls.getPartId());
						temp.setVenderId(dtls.getVenderId());
						dao.insert(temp);
					}
					
				}
				//把写入ttpartoempoTemp临时表的数据转为采购订单
				dao.callProcedure("PKG_PLAN.P_QXC_CREATE_PURCHASE_ORDER",null,null);
				
			}
			*/
			
			act.setOutData("success", "提交成功！");
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "提交计划异常，请联系管理员！");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 配件计划确认（总装车间）
	 */
	public void toPartPlanValidationZzcj(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setOutData("SUPERIOR_PURCHASING_SELECT", Constant.PURCHASE_TYPE_04);
			act.setForword(TO_PART_PLAN_VALIDATION);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到配件计划确认（总装车间）异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 配件计划确认（涂装车间）
	 */
	public void toPartPlanValidationQxc(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setOutData("SUPERIOR_PURCHASING_SELECT", Constant.PURCHASE_TYPE_01);
			act.setForword(TO_PART_PLAN_VALIDATION);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到配件计划确认（涂装车间）异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 配件计划确认（专用车）
	 */
	public void toPartPlanValidationZyc(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setOutData("SUPERIOR_PURCHASING_SELECT", Constant.PURCHASE_TYPE_02);
			act.setForword(TO_PART_PLAN_VALIDATION);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到配件计划确认（专用车）异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 配件计划确认（供应商）
	 */
	public void toPartPlanValidationGys(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			//act.setOutData("SUPERIOR_PURCHASING_SELECT", Constant.PURCHASE_TYPE_03);
			act.setForword(TO_PART_PLAN_VALIDATION);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到配件计划确认（供应商）异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void toPartPlanValidationGysZ(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			//act.setOutData("SUPERIOR_PURCHASING_SELECT", Constant.PURCHASE_TYPE_03);
			act.setForword(TO_PART_PLAN_VALIDATION_z);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到配件计划确认（供应商）异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 配件计划确认（计划室）
	 */
	public void toPartPlanValidationJhs(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PART_PLAN_VALIDATION_JHS);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到配件计划确认（计划室）异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 配件计划确认（计划室）
	 */
	public void toPartPlanValidationJhsCreatePur(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword("/jsp/parts/planManager/rollingPlanManager/toPartPlanValidationJhsCreatePur.jsp");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到配件计划确认（计划室）异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 查询待确认计划的配件数据
	 */
	public void getPartPlanValidationInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPartPlanValidationInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询待确认计划的配件数据异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 查询待确认计划的配件数据（计划室）
	 */
	public void getPartPlanValidationInfoByJhs(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPartPlanValidationInfoByJhs(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询待确认计划的配件数据异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 确认计划
	 */
	@SuppressWarnings("unchecked")
	public void optFirsrConfirm(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		StringBuffer err = new StringBuffer();
		try{
			String[] ck = request.getParamValues("ck");
			if(ck==null || ck.length ==0){
				err.append("没有要确认的数据！");
			}else{
				for (int i = 0; i < ck.length; i++) {
					String planLineId = ck[i];
					
					TtPartPlanScrollDelPO dtl = new TtPartPlanScrollDelPO();
					dtl.setId(Long.valueOf(planLineId));
					
					TtPartPlanScrollDelPO dtls = (TtPartPlanScrollDelPO) dao.select(dtl).get(0);
					if(dtls.getFirstConfirmState().equals(Constant.IF_TYPE_YES)){
						err.append("配件["+dtls.getPartOldcode()+"]已确认，无法重复确认<br>");
					}else{
						String firtConfirmNum = CommonUtils.checkNull(request.getParamValue("FIRST_CONFIRM_NUM_"+planLineId));
						if(StringUtil.isNull(firtConfirmNum)){
							err.append("配件["+dtls.getPartOldcode()+"]，确认的数量为空<br>");
						}else{
							//验证数量是否正确
							if(Long.valueOf(firtConfirmNum)<=0 || Long.valueOf(firtConfirmNum) > dtls.getPlanMonthOne()){
								err.append("配件["+dtls.getPartOldcode()+"]确认的数量为"+firtConfirmNum+",正确数量必须大于等于0小于等于"+dtls.getPlanMonthOne()+"<br>");
							}
							
							String FIRST_CONFIRM_REMARK = request.getParamValue("FIRST_CONFIRM_REMARK_"+planLineId);
							
							TtPartPlanScrollDelPO dtlx = new TtPartPlanScrollDelPO();
							dtlx.setFirsrConfirmBy(loginUser.getUserId());
							dtlx.setFirstConfirmDate(new Date());
							dtlx.setFirstConfirmNum(Long.valueOf(firtConfirmNum));
							if(StringUtil.notNull(FIRST_CONFIRM_REMARK)){
								if(FIRST_CONFIRM_REMARK.length()>250){
									FIRST_CONFIRM_REMARK = FIRST_CONFIRM_REMARK.substring(0, 250);
								}
								dtlx.setFirstConfirmRemark(FIRST_CONFIRM_REMARK);
							}
							dtlx.setFirstConfirmState(Constant.IF_TYPE_YES);
							dao.update(dtl, dtlx);
						}
					}
				}
			}
			
			if(StringUtil.notNull(err.toString())){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
			}
			act.setOutData("success", "操作成功！");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "确认计划异常，请联系管理员！");
			if(StringUtil.notNull(err.toString())){
				e1.setMessage(err.toString());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 确认计划（计划室）
	 */
	@SuppressWarnings("unchecked")
	public void optLastConfirm(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		StringBuffer err = new StringBuffer();
		try{
			String[] ck = request.getParamValues("ck");
			if(ck==null || ck.length ==0){
				err.append("没有要确认的数据！");
			}else{
				for (int i = 0; i < ck.length; i++) {
					String planLineId = ck[i];
					
					TtPartPlanScrollDelPO dtl = new TtPartPlanScrollDelPO();
					dtl.setId(Long.valueOf(planLineId));
					
					TtPartPlanScrollDelPO dtls = (TtPartPlanScrollDelPO) dao.select(dtl).get(0);
					if(dtls.getLastConfirmState().equals(Constant.IF_TYPE_YES)){
						err.append("配件["+dtls.getPartOldcode()+"]已确认，无法重复确认<br>");
					}else{
						String lastConfirmNum = CommonUtils.checkNull(request.getParamValue("LAST_CONFIRM_NUM_"+planLineId));
						if(StringUtil.isNull(lastConfirmNum)){
							err.append("配件["+dtls.getPartOldcode()+"]已确认，确认的数量为空<br>");
						}else{
							//验证数量是否正确
							if(Long.valueOf(lastConfirmNum)<=0 || Long.valueOf(lastConfirmNum) > dtls.getPlanMonthOne()){
								err.append("配件["+dtls.getPartOldcode()+"]确认的数量为"+lastConfirmNum+",正确数量必须大于等于0小于等于"+dtls.getPlanMonthOne()+"<br>");
							}
							
							String LAST_CONFIRM_REMARK = request.getParamValue("LAST_CONFIRM_REMARK_"+planLineId);
							
							TtPartPlanScrollDelPO dtlx = new TtPartPlanScrollDelPO();
							dtlx.setLastConfirmBy(loginUser.getUserId());
							dtlx.setLastConfirmDate(new Date());
							dtlx.setLastConfirmNum(Long.valueOf(lastConfirmNum));
							if(StringUtil.notNull(LAST_CONFIRM_REMARK)){
								if(LAST_CONFIRM_REMARK.length()>250){
									LAST_CONFIRM_REMARK = LAST_CONFIRM_REMARK.substring(0, 250);
								}
								dtlx.setLastConfirmRemark(LAST_CONFIRM_REMARK);
							}
							dtlx.setLastConfirmState(Constant.IF_TYPE_YES);
							//dtlx.setPlanStatus(92981002);//滚动计划已确认
							dao.update(dtl, dtlx);
							
							if(dtls.getPlanMonthOne().intValue()!=dtlx.getLastConfirmNum().intValue()){
								//自动平均拆分滚动周度订单数量
								List<Object> ins = new LinkedList<Object>();
								ins.add(dtls.getId());
								dao.callProcedure("PKG_PLAN.P_QXC_AUTO_PLAN_WEEK_SPLIT_MX",ins,null);
							}
						}
					}
				}
			}
			
			if(StringUtil.notNull(err.toString())){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
			}
			act.setOutData("success", "操作成功！");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "确认计划异常，请联系管理员！");
			if(StringUtil.notNull(err.toString())){
				e1.setMessage(err.toString());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 计划室计划全部确认
	 */
	public void optLastConfirmAll(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			
			int rs = dao.optLastConfirmAll(loginUser,request);
			if(rs<=0){
				act.setOutData("success", "没有计划需要确认！");
			}else{
				act.setOutData("success", "确认成功！");
			}
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "计划室计划全部确认异常！");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		
	}
	
	
/*	*//**
	 * 查询可以生成采购订的数据
	 *//*
	public void getPartPlanValidationInfoByJhsPur(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPartPlanValidationInfoByJhsPur(request,loginUser,curPage,Constant.PAGE_SIZE_100);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询可以生成采购订的数据异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}*/
	
	/**
	 * 生成采购订单(月度)
	 *//*
	@SuppressWarnings("unchecked")
	public void createPurchase(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		StringBuffer err = new StringBuffer();
		try{
			String[] ck = request.getParamValues("ck");
			if(ck==null || ck.length ==0){
				err.append("没有要生成采购单的数据！");
			}else{
				Integer orderzq = 0;
				boolean orderzqFlag = true;
				for (int i = 0; i < ck.length; i++) {
					String planLineId = ck[i];
					TtPartPlanScrollDelPO dtl = new TtPartPlanScrollDelPO();
					dtl.setId(Long.valueOf(planLineId));
					TtPartPlanScrollDelPO dtls = (TtPartPlanScrollDelPO) dao.select(dtl).get(0);
					if(orderzq.intValue() == 0){
						orderzq = dtls.getOrderPeriod();
					}
					
					if(!orderzq.equals(dtls.getOrderPeriod())){
						orderzqFlag = false;
					}
				}
				
				if(orderzqFlag==false){
					err.append("配件转单周期不一致,无法生成采购单！<br>");
					throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
				}
				
				for (int i = 0; i < ck.length; i++) {
					String planLineId = ck[i];
					
					TtPartPlanScrollDelPO dtl = new TtPartPlanScrollDelPO();
					dtl.setId(Long.valueOf(planLineId));
					
					TtPartPlanScrollDelPO dtls = (TtPartPlanScrollDelPO) dao.select(dtl).get(0);
					if(dtls.getLastConfirmState().equals(Constant.IF_TYPE_NO)){
						err.append("配件["+dtls.getPartOldcode()+"]计划室没有确认！<br>");
					}else{
						if(Constant.SCROLL_PLAN_STATUS_02.equals(dtls.getStatus())){
							err.append("配件["+dtls.getPartOldcode()+"]计划已下发,已生成采购订单,无法再次生成采购单！<br>");
						}
						
						TtPartPlanScrollDelPO dtlx = new TtPartPlanScrollDelPO();
						Integer checkNumQty = Integer.valueOf(dtls.getLastConfirmNum().toString());
						if(Constant.PRODUCT_ORDER_TYPE_STATUS_01.equals(dtls.getOrderPeriod())){
							//周度转订单
							err.append("配件["+dtls.getPartOldcode()+"]周度转订单,无法手动生成采购订单！<br>");
						}else{
							//非周度状态改为已下发
							dtlx.setStatus(Constant.SCROLL_PLAN_STATUS_02);//计划已下发,已生成采购订单
						}
						
						dtlx.setCheckNum(checkNumQty);
						dtlx.setCheckDate(new Date());
						dtlx.setCheckBy(loginUser.getUserId());
						dao.update(dtl, dtlx);
						
						if(dtlx.getCheckNum().intValue()>0){
							//写入ttpartoempoTemp临时表
							TtPartOemPoTempPO temp = new TtPartOemPoTempPO();
							temp.setPlanLineId(dtls.getId());
							temp.setPlanId(dtls.getPlanId());
							temp.setPartId(dtls.getPartId());
							temp.setVenderId(dtls.getVenderId());
							dao.insert(temp);
						}
						
					}
				}
				if(Constant.PRODUCT_ORDER_TYPE_STATUS_01.equals(orderzq)){
					//周度订单生成采购单
					//dao.callProcedure("PKG_PLAN.P_QXC_CREATE_PURCHASE_ORDER2",null,null);
				}else{
					//非周度全部月度订单生成采购单
					dao.callProcedure("PKG_PLAN.P_QXC_CREATE_PURCHASE_ORDER",null,null);
				}
				
			}
			
			if(StringUtil.notNull(err.toString())){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
			}
			
			act.setOutData("success", "操作成功！");
			
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "生成采购订单异常");
			if(StringUtil.notNull(err.toString())){
				e1.setMessage(err.toString());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	*/
	/**
	 * 所有月滚动计划生成采购订单(月度)
	 *//*
	@SuppressWarnings("unchecked")
	public void createPurchaseAll(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		StringBuffer err = new StringBuffer();
		try{
			//查询所有的计划，制造物流处已确认，计划室已确认，滚动月度计划
			List<Map<String,Object>> list = dao.getCreatePurchaseAll(request);
			
			if(list!=null && !list.isEmpty() && list.size()>0){
				
				for (int i = 0; i < list.size(); i++) {
					Map<String,Object> mapl = list.get(i);
					String planLineId = CommonUtils.checkNull(mapl.get("PLAN_LINE_ID"));
					
					if(StringUtil.notNull(planLineId)){
						TtPartPlanScrollDelPO dtl = new TtPartPlanScrollDelPO();
						dtl.setId(Long.valueOf(planLineId));
						
						TtPartPlanScrollDelPO dtls = (TtPartPlanScrollDelPO) dao.select(dtl).get(0);
						if(dtls.getLastConfirmState().equals(Constant.IF_TYPE_NO)){
							err.append("配件["+dtls.getPartOldcode()+"]计划室没有确认！<br>");
						}else{
							if(Constant.SCROLL_PLAN_STATUS_02.equals(dtls.getStatus())){
								err.append("配件["+dtls.getPartOldcode()+"]计划已下发,已生成采购订单,无法再次生成采购单！<br>");
							}
							
							TtPartPlanScrollDelPO dtlx = new TtPartPlanScrollDelPO();
							Integer checkNumQty = Integer.valueOf(dtls.getLastConfirmNum().toString());
							if(Constant.PRODUCT_ORDER_TYPE_STATUS_01.equals(dtls.getOrderPeriod())){
								//周度转订单
								err.append("配件["+dtls.getPartOldcode()+"]周度转订单,无法手动生成采购订单！<br>");
							}else{
								//非周度状态改为已下发
								dtlx.setStatus(Constant.SCROLL_PLAN_STATUS_02);//计划已下发,已生成采购订单
							}
							
							dtlx.setCheckNum(checkNumQty);
							dtlx.setCheckDate(new Date());
							dtlx.setCheckBy(loginUser.getUserId());
							dao.update(dtl, dtlx);
							
							if(dtlx.getCheckNum().intValue()>0){
								//写入ttpartoempoTemp临时表
								TtPartOemPoTempPO temp = new TtPartOemPoTempPO();
								temp.setPlanLineId(dtls.getId());
								temp.setPlanId(dtls.getPlanId());
								temp.setPartId(dtls.getPartId());
								temp.setVenderId(dtls.getVenderId());
								dao.insert(temp);
							}
							
						}
					}
					
				}
				
				//非周度全部月度订单生成采购单
				dao.callProcedure("PKG_PLAN.P_QXC_CREATE_PURCHASE_ORDER",null,null);
			}else{
				err.append("该月度没有要生成订单的月度滚动计划!<br>");
			}
			
			if(StringUtil.notNull(err.toString())){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
			}
			
			act.setOutData("success", "操作成功！");
			
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "生成采购订单异常!");
			if(StringUtil.notNull(err.toString())){
				e1.setMessage(err.toString());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	*/
	/**
	 * 跳转到采购订单查询页面
	 */
	public void toPurchaseOrderInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			String nowUserId = loginUser.getUserId().toString();
			act.setOutData("nowUserId", nowUserId);
			List<Map<String,Object>> purUserList = dao.getPurUserInfos();
			act.setOutData("purUserList", purUserList);
			act.setForword(TO_PURCHASE_ORDER_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单查询页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 采购订单查询
	 */
	public void getPurchaseOrderInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurchaseOrderInit(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购订单查询异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到采购订单入库初始页面
	 */
	public void toPurchaseOrderInwhInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PURCHASE_ORDER_INWH_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单入库初始页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 采购订单入库
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public void purchaseOrderInWh(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String err = "";
		try{
			/**************************月结期间不允许入库********zhumingwei by 2017-01-25******************************/
        	String jzFlag= CommonDAO.getPara("10011001");
            if (jzFlag.equals("1") && loginUser.getDealerId() ==null) {
            	err += "总部结账中，暂停入库!<br>";
                BizException be = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "总部结账中，暂停入库!");
                throw be;
            }
            /**************************月结期间不允许入库********zhumingwei by 2017-01-25******************************/
            
            String[] cks = request.getParamValues("ck");
            if(cks==null || cks.length==0){
            	err += "请先选择入库数据在提交!<br>";
            	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "请先选择入库数据在提交!");
            }
            
            //入库单编码
            String inCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_04);
            
            //02件 + 自制件 --》总成拆分 分总成，只要1个分总成入库其他分总成, 都入库来源ls
            //多入库--多个poId--所有的都判断是否02自制件
            //单入库--单个poId--就判断是否为02自制件
            
            List<Map<String,Object>> listMapz = dao.getPoIdByPoIds(cks);
            String[] cksall = new String[listMapz.size()];
            if(listMapz!=null && !listMapz.isEmpty() && listMapz.size()>0){
            	for (int i = 0; i < listMapz.size(); i++) {
                	Map<String,Object> map = listMapz.get(i);
            		String poidz = CommonUtils.checkNull(map.get("PO_ID"));
            		cksall[i] = poidz;
    			}
            }
            
            
            for (int i = 0; i < cksall.length; i++) {
				String poId = cksall[i];
				String dInQty = request.getParamValue("dInQty_"+poId);
				String inRemark = request.getParamValue("inRemark_"+poId);
				if(StringUtil.notNull(inRemark)){
					if(inRemark.length()>100){
						inRemark = inRemark.substring(0, 100);
					}
				}
				
				TtPartOemPoPO po = new TtPartOemPoPO();
				po.setPoId(Long.valueOf(poId));
				
				TtPartOemPoPO pos = (TtPartOemPoPO) dao.select(po).get(0);
				
				/*==========================无采购价，不让入库===========================================*/
				TtPartOemPoPO opos = pos;
				//财务供应商
				String sql = "SELECT DECODE(VF.SUPERIOR_PURCHASING, 97141003, "+opos.getVenderId()+", VF.VENDER_ID) V_FCVENDERID FROM TT_PART_VENDER_FIN VF WHERE VF.SUPERIOR_PURCHASING = '"+opos.getSuperiorPurchasing()+"'";
				Map<String,Object> mapVender = dao.pageQueryMap(sql, null, "instockPurRcvParts");
				String venderIdFin = "";
				if(mapVender==null){
					err += "配件["+opos.getPartOldcode()+"]无供应商无法入库!<br>";
	            	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+opos.getPartOldcode()+"]无供应商无法入库!<br>!");
				}else{
					venderIdFin = CommonUtils.checkNull(mapVender.get("V_FCVENDERID"));
				}
				if(StringUtil.isNull(venderIdFin)){
					err += "配件["+opos.getPartOldcode()+"]无供应商无法入库!<br>";
	            	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+opos.getPartOldcode()+"]无供应商无法入库!<br>!");
				}
				//判断采购价
				TtPartBuyPricePO bup = new TtPartBuyPricePO();
				bup.setPartId(opos.getPartId());
				bup.setVenderId(Long.valueOf(venderIdFin));
				List<TtPartBuyPricePO> priceList = dao.select(bup);
				if(priceList!=null && !priceList.isEmpty() && priceList.size()>0){
					TtPartBuyPricePO buprice = priceList.get(0);
					if(buprice.getBuyPrice()<=0){
						err += "配件["+opos.getPartOldcode()+"]采购价异常，无法入库!<br>";
		            	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+opos.getPartOldcode()+"]采购价异常，无法入库!<br>");
					}
				}else{
					err += "配件["+opos.getPartOldcode()+"]无采购价，无法入库!<br>";
	            	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+opos.getPartOldcode()+"]无采购价，无法入库!<br>");
				}
				/*=========================无采购价，不让入库============================================*/
				
				List<Map<String,Object>> list1 = dao.getSplitPartInfoByPoId(poId);
				if(list1!=null && !list1.isEmpty() && list1.size()>0){
					if(list1.size()!=2){
						err += "配件["+pos.getPartOldcode()+"]为02自制件，该件只能有2个分总成，分总成异常!<br>";
						throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]为02自制件，该件只能有2个分总成，分总成异常!<br>");
					}
					//02自制件只能是一个材料件，一个费用件
					Map<String,Object> mapz1 = list1.get(0);
					String pid1 = CommonUtils.checkNull(mapz1.get("PO_ID"));
					String spn1 = CommonUtils.checkNull(mapz1.get("SPLIT_NUM"));
					
					Map<String,Object> mapz2 = list1.get(1);
					String pid2 = CommonUtils.checkNull(mapz2.get("PO_ID"));
					String spn2 = CommonUtils.checkNull(mapz2.get("SPLIT_NUM"));
					
					//--
					TtPartOemPoPO p1 = new TtPartOemPoPO();
					p1.setPoId(Long.valueOf(pid1));
					p1 = (TtPartOemPoPO) dao.select(p1).get(0);
					
					TtPartOemPoPO p2 = new TtPartOemPoPO();
					p2.setPoId(Long.valueOf(pid2));
					p2 = (TtPartOemPoPO) dao.select(p2).get(0);
					//--
					
					//判断2个分总成是否存在旧的cks
					boolean isAllInFlag1 = false;
					boolean isAllInFlag2 = false;
					for (int j = 0; j < cks.length; j++) {
						String poIdTempj = cks[j];
						if(poIdTempj.equals(pid1)){
							isAllInFlag1 = true;
						}
						if(poIdTempj.equals(pid2)){
							isAllInFlag2 = true;
						}
					}
					if(isAllInFlag1==true && isAllInFlag2==true){
						String diq1 = request.getParamValue("dInQty_"+pid1);
						String diq2 = request.getParamValue("dInQty_"+pid2);
						if(Double.valueOf(spn1)/Double.valueOf(spn2) == Double.valueOf(diq1)/Double.valueOf(diq2)){
							//比例正常，查看最低比的整数倍
							if(Double.valueOf(diq1)%Double.valueOf(spn1)!=0){
								//非整数倍
								err += "配件["+p1.getPartOldcode()+"]最低入库数为："+spn1+"，或者为"+spn1+"的整数倍，当前入库数："+diq1+"!<br>";
								throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+p1.getPartOldcode()+"]最低入库数为："+spn1+"，或者为"+spn1+"的整数倍，当前入库数："+diq1+"!<br>");
							}
							if(Double.valueOf(diq2)%Double.valueOf(spn2)!=0){
								//非整数倍
								err += "配件["+p2.getPartOldcode()+"]最低入库数为："+spn2+"，或者为"+spn2+"的整数倍，当前入库数："+diq2+"!<br>";
								throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+p2.getPartOldcode()+"]最低入库数为："+spn2+"，或者为"+spn2+"的整数倍，当前入库数："+diq2+"!<br>");
							}
							
						}else{
							//比例不正常
							err += "配件["+p1.getPartOldcode()+"]与配件["+p2.getPartOldcode()+"]入库比例不正确<br>正确比例["+spn1+":"+spn2+"]，输入比例["+diq1+":"+diq2+"]!<br>";
							throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+p1.getPartOldcode()+"]与配件["+p2.getPartOldcode()+"]入库比例不正确<br>正确比例["+spn1+":"+spn2+"]，输入比例["+diq1+":"+diq2+"]!<br>");
						}
					}
					
					
					//判断在旧的cks中该ID是否存在，存在就不管，不存在就取另一个分总成入库的件的数量*该件自己的拆和比例 来作为入库数量
					boolean isCzFlag = false;
					for (int j = 0; j < cks.length; j++) {
						String poIdTempj = cks[j];
						if(poId.equals(poIdTempj)){
							isCzFlag = true;
						}
					}
					
					if(isCzFlag==false){//不在cks中，
						if(!poId.equals(pid1)){
							String diq1 = request.getParamValue("dInQty_"+pid1);
							//比例判断
							if(Double.valueOf(diq1)%Double.valueOf(spn1)!=0){
								//非整数倍
								err += "配件["+p1.getPartOldcode()+"]最低入库数为："+spn1+"，或者为"+spn1+"的整数倍，当前入库数："+diq1+"!<br>";
								throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+p1.getPartOldcode()+"]最低入库数为："+spn1+"，或者为"+spn1+"的整数倍，当前入库数："+diq1+"!<br>");
							}
							
							Double diq2t = Double.valueOf(spn2)/Double.valueOf(spn1)*Double.valueOf(diq1);
							
							dInQty = String.valueOf(Math.round(diq2t));
							inRemark = "02自制件系统自动入库！";
						}
					}
					
					if(isCzFlag==false){
						if(!poId.equals(pid2)){
							String diq2 = request.getParamValue("dInQty_"+pid2);
							if(Double.valueOf(diq2)%Double.valueOf(spn2)!=0){
								//非整数倍
								err += "配件["+p2.getPartOldcode()+"]最低入库数为："+spn2+"，或者为"+spn2+"的整数倍，当前入库数："+diq2+"!<br>";
								throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+p2.getPartOldcode()+"]最低入库数为："+spn2+"，或者为"+spn2+"的整数倍，当前入库数："+diq2+"!<br>");
							}
							
							Double diq1t = Double.valueOf(spn1)/Double.valueOf(spn2)*Double.valueOf(diq2);
							dInQty = String.valueOf(Math.round(diq1t));;
							inRemark = "02自制件系统自动入库！";
						}
					}
					
				}
				
				Long dInQtyL = 0L;
				if(Long.valueOf(dInQty)<=0){
					err += "配件["+pos.getPartOldcode()+"]入库数量["+dInQty+"]必须大于0!<br>";
					throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]入库数量["+dInQty+"]必须大于0!<br>");
				}else{
					dInQtyL = Long.valueOf(dInQty);
				}
				
				Long krQty = pos.getCheckQty() - pos.getInQty();//可入库 = 采购 - 已入库
				if(dInQtyL>krQty){
					err += "配件["+pos.getPartOldcode()+"]入库数量["+dInQty+"]不能大于可入库数量["+krQty+"]!<br>";
					throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]入库数量["+dInQty+"]不能大于可入库数量["+krQty+"]!<br>");
				}
				
				if (pos.getState().equals(Constant.PART_PURCHASE_ORDERCHK_STATUS_02)) {
					err += "配件["+pos.getPartOldcode()+"]已经被关闭,请打开后再入库!<br>";
					throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]已经被关闭,请打开后再入库!<br>");
                }
				
				boolean isLocked = dao.isLocked(pos.getPartId(), pos.getWhId(), loginUser.getDealerId() == null ? loginUser.getOrgId() : Long.valueOf(loginUser.getDealerId()));//当前仓库中的配件是否被锁定
				if (isLocked) {//如果该配件在库存中已经被锁定
					err += "配件["+pos.getPartOldcode()+"]已经被锁定,请重新选择!<br>";
					throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]已经被锁定,请重新选择!<br>");
	            }else{
	            	//判断当前要入库的仓库是否有效
                    TtPartWarehouseDefinePO warehouseDefinePO = new TtPartWarehouseDefinePO();
                    warehouseDefinePO.setWhId(pos.getWhId());
                    warehouseDefinePO.setState(Constant.STATUS_ENABLE);
                    warehouseDefinePO.setStatus(1);

                    List<TtPartWarehouseDefinePO> wList = dao.select(warehouseDefinePO);
                    if(wList==null || wList.isEmpty() || wList.size() == 0){
                    	err += "配件["+pos.getPartOldcode()+"]对应的入库库房已经失效,请选择重新选择!<br>";
                        throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]对应的入库库房已经失效,请选择重新选择!<br>");
                    }else{
                    	//判断是否有对应货位信息
                    	TtPartOemPoPO pox = new TtPartOemPoPO();
                    	pox.setInQty(pos.getInQty() + dInQtyL);
                    	pox.setSpareQty(dInQtyL);//待入库
                    	if(pos.getCheckQty().intValue() == pox.getInQty()){
                    		pox.setState(Constant.PART_PURCHASE_ORDERCIN_STATUS_02);
                    	}
                    	pox.setRemark(inRemark);
                    	pox.setInBy(loginUser.getUserId());
                    	pox.setInDate(new Date());
                    	
                    	
	                	Long partId = pos.getPartId();//获取配件id
	                    Long orgId = pos.getOrgId();//获取单位id
	                    Long whId = pos.getWhId();
	                    List<OrgBean> orgBeanList = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
	                    Map<String, Object> locMap = PartDlrInstockDao.getInstance().getLoc(pos.getPartId().toString(), whId.toString(), orgBeanList.get(0).getOrgId().toString());
	                    Map<String, Object> map = dao.queryPartAndLocationInfo(partId, orgId, whId);//查询当前配件信息及其货位信息
	                    TtPartWarehouseDefinePO warehousePO = wList.get(0);
	                    
	                    //入库的时候要判断相同验收单号、相同配件、相同库房是否已经入过库了,如果已经入库,那再次入库就更新入库数量,否则新增入库信息
	                    //Map<String, Object> Imap = dao.getInfoByChkCodeAndWhIdAndPartId(pos.getOrderCode(), whId, partId);
	                    
	                    TtPartPoInPO inPo = new TtPartPoInPO();//需要将每次入库的信息保存到入库表中
	                    Long InId = CommonUtils.parseLong(SequenceManager.getSequence(""));
	                    inPo.setInId(InId);
	                    inPo.setInCode((inCode));
	                    inPo.setInType(Constant.PART_PURCHASE_ORDERCIN_TYPE_01);
	                    inPo.setPoId(pos.getPoId());//采购订单iD
	                    inPo.setCheckId(-1L);
	                    inPo.setOrderCode(pos.getOrderCode());//采购订单号
	                    inPo.setPlanCode(pos.getPlanCode());//计划单号
	                    if (map != null && map.get("BUYER_ID") != null) {
                            inPo.setBuyerId(pos.getBuyerId());//采购员ID
                        }
	                    TtPartDefinePO dp = new TtPartDefinePO();
                        dp.setPartOldcode(pos.getPartOldcode());
                        dp = (TtPartDefinePO) dao.select(dp).get(0);
                        
                        String up = CommonUtils.checkNull(dp.getSuperiorPurchasing());
                        if (!up.equals("") && up != null) {
                        	//财务供应商，结算用
                        	inPo.setFcvderId(pos.getVenderId());
                            inPo.setFcvderrCode(pos.getVenderCode());
                        } else {
                            inPo.setFcvderId(pos.getVenderId());
                            inPo.setFcvderrCode(pos.getVenderCode());
                        }
                        inPo.setPartType(pos.getPartType());//配件类型
                        inPo.setPartId(pos.getPartId());//配件ID
                        inPo.setPartCode(pos.getPartCode());//配件件号
                        inPo.setPartOldcode(pos.getPartOldcode());//配件编码
                        inPo.setPartCname(pos.getPartCname());//配件名称
                        inPo.setUnit(pos.getUnit());//配件包装单位
                        inPo.setProduceFac(Integer.valueOf(pos.getProduceFac()));
                        inPo.setSpareQty(dInQtyL);//待结算数量
                        inPo.setVenderId(pos.getVenderId());//配件供应商ID
                        inPo.setVenderCode(pos.getVenderCode());//配件供应商编码
                        inPo.setVenderName(pos.getVenderName());//配件供应商名称
                        //inPo.setMakerId(pos.getMakerId());//制造商id
                        inPo.setBuyPrice(pos.getBuyPrice());//配件采购单价，通过和供应商关系获取
                        inPo.setBuyQty(pos.getBuyQty());//采购数量
                        inPo.setCheckQty(pos.getCheckQty());//验货数量
                        inPo.setInQty(dInQtyL);//已入库数量
                        inPo.setBuyPriceNotax(Arith.round(Arith.div(pos.getBuyPrice(), Arith.add(1, Constant.PART_TAX_RATE)), 6));//无税单价
                        inPo.setSuperiorPurchasing(pos.getSuperiorPurchasing());
                        inPo.setProduceState(pos.getProduceState());
                        inPo.setInAmount(Arith.mul(pos.getBuyPrice(), inPo.getInQty()));//入库金额
                        /*
                        inPo.setErpNo(pos.getErpNo());
                        if (po.getErpNo() != null && !po.getErpNo().equals("")) {
                            dao.insert("  INSERT INTO XXPO_PMS_INPUT_ITF (SEQ_ID, PO_NUMBER, PO_QUANTITY, RECEIVE_DATE)   SELECT F_GETID, '" + po.getErpNo() + "', " + inPo.getInQty() + ", SYSDATE FROM DUAL");
                        }
                        */
                        inPo.setTaxRate(Constant.PART_TAX_RATE);//税率
                        inPo.setInAmountNotax(Arith.mul(inPo.getBuyPriceNotax(), inPo.getInQty()));//无税总金额
                        inPo.setWhId(whId);//库房iD
                        inPo.setWhName(warehousePO.getWhName());//库房2015032711875757名称
                        inPo.setLocCode((String) locMap.get("LOC_CODE"));//货位编码
                        inPo.setLocId(((BigDecimal) locMap.get("LOC_ID")).longValue());//货位ID
                        inPo.setCheckDate(new Date());
                        inPo.setCheckBy(loginUser.getUserId());
                        //查询库存数量
                        VwPartStockPO itemStockPO = new VwPartStockPO();
                        itemStockPO.setPartId(inPo.getPartId());
                        itemStockPO.setLocId(inPo.getLocId());
                        itemStockPO.setOrgId(inPo.getOrgId());
                        itemStockPO.setWhId(inPo.getWhId());
                        itemStockPO.setState(Constant.STATUS_ENABLE);
                        List<VwPartStockPO> list = dao.select(itemStockPO);
                        if (list.size() > 0) {
                            itemStockPO = (VwPartStockPO) list.get(0);
                            inPo.setItemQty(itemStockPO.getItemQty());
                        }
                        inPo.setRemark(inRemark);
                        inPo.setInDate(new Date());
                        inPo.setInBy(loginUser.getUserId());
                        inPo.setCreateDate((new Date()));
                        inPo.setCreateBy(loginUser.getUserId());
                        inPo.setOrgId(loginUser.getOrgId());
                        inPo.setOriginType(pos.getOriginType());
                        //配件只要入库，入库单的状态就变为结算中
                        inPo.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01);
                        
                        
                        
                        //插入出入库记录表 add by yuan 20130513
                        TtPartRecordPO ttPartRecordPO = new TtPartRecordPO();
                        ttPartRecordPO.setRecordId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                        ttPartRecordPO.setAddFlag(1);//入库标记
                        ttPartRecordPO.setState(1);//正常入库
                        ttPartRecordPO.setPartNum(dInQtyL);//入库数量
                        ttPartRecordPO.setTranstypeId(0l);//默认0
                        ttPartRecordPO.setPartId(pos.getPartId());//配件ID
                        ttPartRecordPO.setPartCode(pos.getPartCode());//配件件号
                        ttPartRecordPO.setPartOldcode(pos.getPartOldcode());//配件编码
                        ttPartRecordPO.setPartName(pos.getPartCname());//配件名称
                        ttPartRecordPO.setPartBatch("1306");//////////////////配件批次
                        ttPartRecordPO.setVenderId(21799l);///////////////////配件供应商
                        ttPartRecordPO.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_04));//入库单
                        ttPartRecordPO.setOrderId(InId);//入库单ID
                        ttPartRecordPO.setOrderCode(inCode);//入库单编码
                        //ttPartRecordPO.setLineId();
                        ttPartRecordPO.setOrgId(orgBeanList.get(0).getOrgId());
                        ttPartRecordPO.setOrgCode(orgBeanList.get(0).getOrgCode());
                        ttPartRecordPO.setOrgName(orgBeanList.get(0).getOrgName());
                        ttPartRecordPO.setWhId(whId);
                        ttPartRecordPO.setWhName(warehousePO.getWhName());
                        //modify by yuan 20130809
                        //ttPartRecordPO.setLocId(((BigDecimal) map.get("LOC_ID")).longValue());
                        //ttPartRecordPO.setLocCode((String) map.get("LOC_CODE"));
                        //ttPartRecordPO.setLocName((String) map.get("LOC_NAME"));
                        ttPartRecordPO.setLocId(Long.valueOf(CommonUtils.checkNull(locMap.get("LOC_ID"))));
                        ttPartRecordPO.setLocCode(CommonUtils.checkNull(locMap.get("LOC_CODE")));
                        ttPartRecordPO.setLocName(CommonUtils.checkNull(locMap.get("LOC_NAME")));
                        ttPartRecordPO.setOptDate(new Date());
                        ttPartRecordPO.setCreateDate(new Date());
                        ttPartRecordPO.setPersonId(loginUser.getUserId());
                        ttPartRecordPO.setPersonName(loginUser.getName());
                        ttPartRecordPO.setPartState(1);
                        
                        dao.insert(inPo);//新增订单入库信息
                        dao.insert(ttPartRecordPO);//新增出入库记录
                        dao.update(po, pox);//修改信息
                        //调用入库逻辑
                        List<Object> ins = new LinkedList<Object>();
                        ins.add(0, InId);
                        ins.add(1, Constant.PART_CODE_RELATION_04);
                        dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
                        
                    }
	            }
			}
            
            List<Object> ins5 = new LinkedList<Object>();
            ins5.add(0, inCode);
            dao.callProcedure("PKG_PART.P_AUTO_CREATE_DEMOLITION", ins5, null);
            
        	act.setOutData("success", "入库成功！");
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购订单入库异常!");
			if(StringUtil.notNull(err)){
				e1.setMessage("入库失败！"+err);
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到采购订单入库初始页面
	 */
	public void toRFPurchaseScanInStock(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_RF_PURCHASE_SCAN_IN_STOCK);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单入库初始页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据采购订单和配件编码获取采购信息
	 */
	public void getPoInfoByOrderAndPart(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			Map<String,Object> map = dao.getPoInfoByOrderAndPart(request);
			act.setOutData("rsMap", map);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单入库初始页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取采购订单汇总
	 */
	public void getPurcharOrderHz(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurcharOrderHz(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取采购订单汇总异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到采购订单明细查看页面
	 */
	public void toPurcharOrderMx(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String orderCode = request.getParamValue("orderCode");
			act.setOutData("orderCode", orderCode);
			act.setForword(TO_PURCHAR_ORDER_MX);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单明细查看页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据订单号获取采购订单明细
	 */
	public void getPurcharOrderMx(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurcharOrderMx(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "根据订单号获取采购订单明细异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到采购订单ASN单打印页面
	 */
	public void toPurcharOrderAsnPrint(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String orderCode = request.getParamValue("orderCode");
			act.setOutData("orderCode", orderCode);
			
			Map<String,Object> po = dao.getPurchaseOrderMainByOrderCode(orderCode);
			act.setOutData("po", po);
			List<Map<String,Object>> poList = dao.getPurchaseOrderMxByOrderCode(orderCode);
			act.setOutData("poList", poList);
			
			int listCount = (int)Math.ceil(Double.valueOf(poList.size())/Double.valueOf(5));
			List<Map<String,Object>> cfor = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < listCount; i++) {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("keyz", i);
				cfor.add(map);
			}
			act.setOutData("cfor", cfor);
			act.setForword(TO_PURCHAR_ORDER_ASN_PRINT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单ASN单打印页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到采购订单二维码打印页面
	 */
	public void toPurcharOrderQrPrint(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String inId =  CommonUtils.checkNull(request.getParamValue("inId"));
			act.setOutData("inId", inId);
			String partId =  CommonUtils.checkNull(request.getParamValue("partId"));
			act.setOutData("partId", partId);
			//旧的二维码打印方式，全部显示完，出现过一个配件采购2000以上，IE打印二维码吃力情况
			//List<Map<String,Object>> list = dao.getPurcharOrderQrByOrderCode(loginUser,inId, partId);
			
			//新的二维码打印方式，分页显示二维码2017-07-13  start
			String paravalue = 500+"";//CommonDAO.getPara("60241001"); //每页显示二维码个数，默认500
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurcharOrderQrMorePage(request,loginUser,curPage,paravalue);
			act.setOutData("ps", ps);
			//新的二维码打印方式，分页显示二维码2017-07-13  end
			List<Map<String,Object>> list = ps.getRecords();
			act.setOutData("qrList", list);
			
			act.setForword(TO_PURCHAR_ORDER_QR_PRINT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单二维码打印页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 跳转到中储采购订单查询页面
	 */
	public void toPurchaseOrderCMSTInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PURCHASE_ORDER_CMST_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到中储采购订单查询页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 根据中储采购订单查询
	 */
	public void getPurchaseOrderCMSTInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurchaseOrderCMSTInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "中储采购订单查询页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到采购订单涂装确认页面
	 */
	public void toPurchaseOrderPaintingConfirm(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PURCHASE_ORDER_PAINTING_CONFIRM);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单涂装确认页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取待涂装确认的数据
	 */
	public void getPurchaseOrderPaintingInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurchaseOrderPaintingInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取待涂装确认的数据异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 涂装确认
	 *//*
	@SuppressWarnings("unchecked")
	public void paintionConfirmSub(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String[] cks = request.getParamValues("ck");
			if(cks!=null && cks.length>0){
				for (int i = 0; i < cks.length; i++) {
					String poId = cks[i];
					TtPartOemPoPO po = new TtPartOemPoPO();
					po.setPoId(Long.valueOf(poId));
					
					TtPartOemPoPO pos = (TtPartOemPoPO) dao.select(po).get(0);
					if(!pos.getState().equals(Constant.PART_PURCHASE_ORDERCHK_STATUS_01)){
						throw new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "采购订单["+pos.getOrderCode()+"]中，配件["+pos.getPartOldcode()+"]已入库，无法再确认！<br>");
					}
					if(Constant.IF_TYPE_YES.equals(pos.getIsPainting())){
						throw new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "采购订单["+pos.getOrderCode()+"]中，配件["+pos.getPartOldcode()+"]已涂装确认，无法再确认！<br>");
					}
					
					TtPartOemPoPO pox = new TtPartOemPoPO();
					pox.setPaintingBy(loginUser.getUserId());
					pox.setIsPainting(Constant.IF_TYPE_YES);
					pox.setPaintingDate(new Date());
					
					dao.update(po, pox);
				}
				act.setOutData("success", "确认成功！");
			}else{
				throw new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "无待涂装确认的数据，请先选择数据在提交！");
			}
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "涂装确认异常！");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	*/
	/**
	 * 跳转到采购订单焊装确认页面
	 */
	public void toPurchaseOrderWeldingConfirm(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PURCHASE_ORDER_WELDING_CONFIRM);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单焊装确认页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取待涂装确认的数据
	 */
	public void getPurchaseOrderWeldingInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurchaseOrderWeldingInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取待涂装确认的数据异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 焊装确认
	 *//*
	@SuppressWarnings("unchecked")
	public void weldingConfirmSub(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String[] cks = request.getParamValues("ck");
			if(cks!=null && cks.length>0){
				for (int i = 0; i < cks.length; i++) {
					String poId = cks[i];
					TtPartOemPoPO po = new TtPartOemPoPO();
					po.setPoId(Long.valueOf(poId));
					
					TtPartOemPoPO pos = (TtPartOemPoPO) dao.select(po).get(0);
					if(!pos.getState().equals(Constant.PART_PURCHASE_ORDERCHK_STATUS_01)){
						throw new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "采购订单["+pos.getOrderCode()+"]中，配件["+pos.getPartOldcode()+"]已入库，无法再确认！<br>");
					}
					if(Constant.IF_TYPE_YES.equals(pos.getIsWelding())){
						throw new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "采购订单["+pos.getOrderCode()+"]中，配件["+pos.getPartOldcode()+"]已焊装确认，无法再确认！<br>");
					}
					
					TtPartOemPoPO pox = new TtPartOemPoPO();
					pox.setWeldingBy(loginUser.getUserId());
					pox.setIsWelding(Constant.IF_TYPE_YES);
					pox.setWeldingDate(new Date());
					
					dao.update(po, pox);
				}
				act.setOutData("success", "确认成功！");
			}else{
				throw new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "无待焊装确认的数据，请先选择数据在提交！");
			}
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "焊装确认异常！");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	*/
	/**
	 * 删除计划
	 */
	@SuppressWarnings("unchecked")
	public void delPlanByPlanId(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String planId = request.getParamValue("planId");
			if(StringUtil.notNull(planId)){
				TtPartPlanScrollPO po = new TtPartPlanScrollPO();
				po.setId(Long.valueOf(planId));
				List<TtPartPlanScrollPO> poList = dao.select(po);
				if(poList!=null && !poList.isEmpty() && poList.size()>0){
					TtPartPlanScrollPO pos = poList.get(0);
					if(pos.getIsSubmit().equals(Constant.IF_TYPE_YES)){
						throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "该计划已提交，无法删除!");
					}else{
						TtPartPlanScrollDelPO dtl = new TtPartPlanScrollDelPO();
						dtl.setPlanId(pos.getId());
						dao.delete(dtl);
						
						dao.delete(po);
						
						act.setOutData("success", "删除成功！");
					}
					
				}else{
					throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "该计划已删除，无法再次删除!");
				}
			}else{
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "请先选择要删除的计划，在提交删除请求!");
			}
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "删除计划异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到补充计划编制页面
	 */
	public void toSupplementPlanInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_SUPPLEMENT_PLAN_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到补充计划编制页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改拆分周度订单
	 *//*
	@SuppressWarnings("unchecked")
	public void saveFWeekParts(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		StringBuffer err = new StringBuffer();
		try{
			String[] ck = request.getParamValues("ck");
			if(ck==null || ck.length ==0){
				err.append("没有选择数据！");
			}else{
				
				for (int i = 0; i < ck.length; i++) {
					String planLineId = ck[i];
					
					TtPartPlanScrollDelPO dtlz = new TtPartPlanScrollDelPO();
					dtlz.setId(Long.valueOf(planLineId));
					
					TtPartPlanScrollDelPO dtls = (TtPartPlanScrollDelPO) dao.select(dtlz).get(0);
					if(Constant.SCROLL_PLAN_STATUS_02.equals(dtls.getStatus())){
						err.append("配件["+dtls.getPartOldcode()+"]计划已下发,无法修改！<br>");
					}else{
						if(dtls.getOrderPeriod().equals(Constant.PRODUCT_ORDER_TYPE_STATUS_01)){
							String WEEK_COUNT = request.getParamValue("WEEK_COUNT_"+planLineId);//计划月共有几周
							String NEXT_WEEK_ONE_MONTH = request.getParamValue("NEXT_WEEK_ONE_MONTH_"+planLineId);//下个周一的月份
							String NEXT_WEEK_ONE_NUM = request.getParamValue("NEXT_WEEK_ONE_NUM_"+planLineId);//下个周一是当月的第几周
							if(Long.valueOf(NEXT_WEEK_ONE_MONTH) <= Long.valueOf(dtls.getMonthDate())){
								//下个周一所在的月份为小于或等于计划月份，该月可修改
								String WEEK_ONE = CommonUtils.checkNull(request.getParamValue("WEEK_ONE_"+planLineId));
								String WEEK_TWO = CommonUtils.checkNull(request.getParamValue("WEEK_TWO_"+planLineId));
								String WEEK_THREE = CommonUtils.checkNull(request.getParamValue("WEEK_THREE_"+planLineId));
								String WEEK_FOUR = CommonUtils.checkNull(request.getParamValue("WEEK_FOUR_"+planLineId));
								String WEEK_FIVE = CommonUtils.checkNull(request.getParamValue("WEEK_FIVE_"+planLineId));
								
								if(1<=Integer.parseInt(WEEK_COUNT)){
									if(StringUtil.notNull(WEEK_ONE)){
										if(Integer.parseInt(WEEK_ONE) < 0){
											err.append("配件["+dtls.getPartOldcode()+"]"+Long.valueOf(dtls.getMonthDate())+"月第一周拆分数量小于0,无法修改！<br>");
										}
									}
								}
								if(2<=Integer.parseInt(WEEK_COUNT)){
									if(StringUtil.notNull(WEEK_TWO)){
										if(Integer.parseInt(WEEK_TWO) < 0){
											err.append("配件["+dtls.getPartOldcode()+"]"+Long.valueOf(dtls.getMonthDate())+"月第二周拆分数量小于0,无法修改！<br>");
										}
									}
								}
								if(3<=Integer.parseInt(WEEK_COUNT)){
									if(StringUtil.notNull(WEEK_THREE)){
										if(Integer.parseInt(WEEK_THREE) < 0){
											err.append("配件["+dtls.getPartOldcode()+"]"+Long.valueOf(dtls.getMonthDate())+"月第三周拆分数量小于0,无法修改！<br>");
										}
									}
								}
								if(4<=Integer.parseInt(WEEK_COUNT)){
									if(StringUtil.notNull(WEEK_FOUR)){
										if(Integer.parseInt(WEEK_FOUR) < 0){
											err.append("配件["+dtls.getPartOldcode()+"]"+Long.valueOf(dtls.getMonthDate())+"月第四周拆分数量小于0,无法修改！<br>");
										}
									}
								}
								if(5<=Integer.parseInt(WEEK_COUNT)){
									if(StringUtil.notNull(WEEK_FIVE)){
										if(Integer.parseInt(WEEK_FIVE) < 0){
											err.append("配件["+dtls.getPartOldcode()+"]"+Long.valueOf(dtls.getMonthDate())+"月第五周拆分数量小于0,无法修改！<br>");
										}
									}
								}
								
								if(Long.valueOf(NEXT_WEEK_ONE_MONTH) < Long.valueOf(dtls.getMonthDate())){
									//所有周可修改
									TtPartPlanScrollDelPO dtlx = new TtPartPlanScrollDelPO();
									
									if(1<=Integer.parseInt(WEEK_COUNT)){
										if(Constant.IF_TYPE_NO.equals(dtls.getIsWeek1())){
											dtlx.setWeekOne(Integer.valueOf(WEEK_ONE));
										}
									}
									if(2<=Integer.parseInt(WEEK_COUNT)){
										if(Constant.IF_TYPE_NO.equals(dtls.getIsWeek2())){
											dtlx.setWeekTow(Integer.valueOf(WEEK_TWO));
										}
										
									}
									if(3<=Integer.parseInt(WEEK_COUNT)){
										if(Constant.IF_TYPE_NO.equals(dtls.getIsWeek3())){
											dtlx.setWeekThree(Integer.valueOf(WEEK_THREE));
										}
										
									}
									if(4<=Integer.parseInt(WEEK_COUNT)){
										if(Constant.IF_TYPE_NO.equals(dtls.getIsWeek4())){
											dtlx.setWeekFour(Integer.valueOf(WEEK_FOUR));
										}
										
									}
									if(5<=Integer.parseInt(WEEK_COUNT)){
										if(Constant.IF_TYPE_NO.equals(dtls.getIsWeek5())){
											dtlx.setWeekFive(Integer.valueOf(WEEK_FIVE));
										}
										
									}
									
									dtlx.setUpdateBy(loginUser.getUserId());
									dtlx.setUpdateDate(new Date());
									dao.update(dtlz, dtlx);
									
									//验证拆分数量必须小于等于计划数量
									String info = dao.getSplitErrInfoByPlanLineId(planLineId);
									err.append(info);
								}else{
									//计划月份=下周一所在的月份， 下周周一是第几周就可以修改第几周和后面周度的数量
									TtPartPlanScrollDelPO dtlx = new TtPartPlanScrollDelPO();
									
									if(1<=Integer.parseInt(WEEK_COUNT)){
										if(1>=Integer.parseInt(NEXT_WEEK_ONE_NUM) && dtls.getIsWeek1().equals(Constant.IF_TYPE_NO)){
											dtlx.setWeekOne(Integer.valueOf(WEEK_ONE));
										}
									}
									if(2<=Integer.parseInt(WEEK_COUNT)){
										if(2>=Integer.parseInt(NEXT_WEEK_ONE_NUM) && dtls.getIsWeek2().equals(Constant.IF_TYPE_NO) ){
											dtlx.setWeekTow(Integer.valueOf(WEEK_TWO));
										}
									}
									if(3<=Integer.parseInt(WEEK_COUNT)){
										if(3>=Integer.parseInt(NEXT_WEEK_ONE_NUM) && dtls.getIsWeek3().equals(Constant.IF_TYPE_NO)){
											dtlx.setWeekThree(Integer.valueOf(WEEK_THREE));
										}
									}
									if(4<=Integer.parseInt(WEEK_COUNT)){
										if(4>=Integer.parseInt(NEXT_WEEK_ONE_NUM) && dtls.getIsWeek4().equals(Constant.IF_TYPE_NO)){
											dtlx.setWeekFour(Integer.valueOf(WEEK_FOUR));
										}
									}
									if(5<=Integer.parseInt(WEEK_COUNT)){
										if(5>=Integer.parseInt(NEXT_WEEK_ONE_NUM) && dtls.getIsWeek5().equals(Constant.IF_TYPE_NO)){
											dtlx.setWeekFive(Integer.valueOf(WEEK_FIVE));
										}
									}
									
									dtlx.setUpdateBy(loginUser.getUserId());
									dtlx.setUpdateDate(new Date());
									dao.update(dtlz, dtlx);
									
									//验证拆分数量必须小于等于计划数量
									String info = dao.getSplitErrInfoByPlanLineId(planLineId);
									err.append(info);
								}
								
							}else{
								err.append("配件["+dtls.getPartOldcode()+"]计划已过期,无法修改！<br>");
							}
							
						}else{
							err.append("配件["+dtls.getPartOldcode()+"]不是周度转订单,无法拆分！<br>");
						}
					}
				}
				
			}
			
			if(StringUtil.notNull(err.toString())){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
			}
			
			act.setOutData("success", "保存成功！");
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "修改拆分周度订单异常!");
			if(StringUtil.notNull(err.toString())){
				e1.setMessage(err.toString());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}*/
	
	/**
	 * 跳转到采购到货确认页面
	 */
	public void toPurRcvInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PUR_RCV_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购到货确认页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询采购到货待确认信息
	 */
	public void getPurRcvInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurRcvInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询采购到货待确认信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 保存采购确认到货数量
	 *//*
	@SuppressWarnings("unchecked")
	public void saveConfirmPurRcvQty(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String err = "";
		try{
			String[] cks = request.getParamValues("ck");
			if(cks==null || cks.length==0){
				err = "没有数据，无法保存！";
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
			}
			
			for (int i = 0; i < cks.length; i++) {
				String poId = cks[i];
				String dInQty = request.getParamValue("dInQty_"+poId);
				String inRemark = request.getParamValue("inRemark_"+poId);
				if(StringUtil.notNull(inRemark)){
					if(inRemark.length()>200){
						inRemark = inRemark.substring(0, 200);
					}
				}
				
				TtPartOemPoPO po = new TtPartOemPoPO();
				po.setPoId(Long.valueOf(poId));
				
				TtPartOemPoPO pos = (TtPartOemPoPO) dao.select(po).get(0);
				if(pos.getState().equals(Constant.PART_PURCHASE_ORDERCHK_STATUS_02)){
					err += "配件["+pos.getPartOldcode()+"]订单已关闭，无法确认到货！<br>";
				}
				Map<String,Object> mapDcon = dao.getConfimOemPoInfoByPoId(poId);
				if(mapDcon==null || mapDcon.isEmpty()){
					err += "配件["+pos.getPartOldcode()+"]数据异常，请联系管理员！<br>";
				}else{
					String DCON_QTY = CommonUtils.checkNull(mapDcon.get("DCON_QTY"));
					if(Long.valueOf(dInQty) > Long.valueOf(DCON_QTY)){
						err += "配件["+pos.getPartOldcode()+"]确认数量"+dInQty+"，大于可确认数量"+DCON_QTY+"！<br>";
					}
				}
				
				
				
				Long conQty = Long.valueOf(dInQty);
				Long SPAREIN_QTY = pos.getCheckQty() - pos.getInQty();
				if(conQty>SPAREIN_QTY){
					//err += "配件["+pos.getPartOldcode()+"]确认数量"+conQty+"，大于可确认数量"+SPAREIN_QTY+"！<br>";
				}
				
				TtPartOemPoConfirmPO con = new TtPartOemPoConfirmPO();
				con.setConId(Long.valueOf(SequenceManager.getSequence("")));
				con.setPoId(pos.getPoId());
				con.setConQty(Long.valueOf(Long.valueOf(dInQty)));
				con.setBuyQty(pos.getCheckQty());
				con.setPartId(pos.getPartId());
				con.setPartOldcode(pos.getPartOldcode());
				con.setPartCname(pos.getPartCname());
				con.setPartCode(pos.getPartCode());
				con.setRemark(inRemark);
				con.setCreateDate(new Date());
				con.setCreateBy(loginUser.getUserId());
				
				if(con.getConQty()>0){
					dao.insert(con);
				}else{
					err += "配件["+pos.getPartOldcode()+"]确认数量"+conQty+"，必须大于0！<br>";
				}
			}
			
			if(StringUtil.notNull(err)){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
			}
			act.setOutData("success", "保存成功！");
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存采购确认到货数量异常!");
			if(StringUtil.notNull(err)){
				e1.setMessage(err);
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	*/
	/**
	 * 跳转到采购到货查询页面
	 */
	public void toPurRcvSelectInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PUR_RCV_SELECT_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购到货查询页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询采购到货已确认信息
	 */
	public void getPurRcvSelectInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurRcvSelectInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询采购到货已确认信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 失效采购到货确认信息
	 *//*
	@SuppressWarnings("unchecked")
	public void disPurConfirm(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String err = "";
		try{
			String conId = request.getParamValue("conId");
			if(StringUtil.isNull(conId)){
				err = "没有数据，无法失效！";
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
			}
			
			TtPartOemPoConfirmPO pc = new TtPartOemPoConfirmPO();
			pc.setConId(Long.valueOf(conId));
			TtPartOemPoConfirmPO pcx = new TtPartOemPoConfirmPO();
			pcx.setStatus(Constant.STATUS_DISABLE);
			pcx.setUpdateBy(loginUser.getUserId());
			pcx.setUpdateDate(new Date());
			dao.update(pc, pcx);
			
			act.setOutData("success", "失效成功！");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "失效采购到货确认信息异常，请联系管理员!");
			if(StringUtil.notNull(err)){
				e1.setMessage(err);
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}*/
	
	/**
	 * 跳转到到货确认ASN单打印页面
	 */
	@SuppressWarnings("unchecked")
	public void toPurConfirmAsnPrint(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String conId = request.getParamValue("po_Id");
			TtPartPoMainPO pc = new TtPartPoMainPO();
			pc.setOrderId(Long.valueOf(conId));			
			TtPartPoMainPO pcx = new TtPartPoMainPO();
			pcx.setPrintBy(loginUser.getUserId());
			pcx.setPrintDate(new Date());
			dao.update(pc, pcx);
			
			Map<String,Object> po = dao.getPurConfirmPrintInfoByConId(conId);
			
			List<Map<String,Object>> listPo = new ArrayList<Map<String,Object>>();
			listPo.add(po);
			
			act.setOutData("listPo", listPo);
			
			act.setForword(TO_PUR_CONFIRM_ASN_PRINT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到到货确认ASN单打印页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 批量打印ASN单
	 */
	@SuppressWarnings("unchecked")
	public void toPurConfirmAsnPrints(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String[] cks = request.getParamValues("ck");
			List<Map<String,Object>> listPo = new ArrayList<Map<String,Object>>();
			if(cks!=null && cks.length>0){
				for (int i = 0; i < cks.length; i++) {
					String conId = cks[i];
					TtPartPoMainPO pc = new TtPartPoMainPO();
					pc.setOrderId(Long.valueOf(conId));			
					TtPartPoMainPO pcx = new TtPartPoMainPO();
					pcx.setPrintBy(loginUser.getUserId());
					pcx.setPrintDate(new Date());
					dao.update(pc, pcx);
					
					Map<String,Object> po = dao.getPurConfirmPrintInfoByConId(conId);
					
					listPo.add(po);
				}
				
			}
			
			act.setOutData("listPo", listPo);
			
			act.setForword(TO_PUR_CONFIRM_ASN_PRINT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "批量打印ASN单异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到采购到货入库初始页面
	 */
	public void toPurRcvInwhInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PUR_RCV_INWH_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购到货入库初始页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询采购到货入库信息
	 */
	public void getPurRcvInwhInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurRcvInwhInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询采购到货入库信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
/*	*//**
	 * 采购到货入库
	 *//*
	@SuppressWarnings("unchecked")
	public void instockPurRcvParts(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String err = "";
		try{
			*//**************************月结期间不允许入库********zhumingwei by 2017-01-25******************************//*
        	String jzFlag= CommonDAO.getPara("10011001");
            if (jzFlag.equals("1") && loginUser.getDealerId() ==null) {
            	err += "总部结账中，暂停入库!<br>";
                BizException be = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "总部结账中，暂停入库!");
                throw be;
            }
            *//**************************月结期间不允许入库********zhumingwei by 2017-01-25******************************//*
            
            String[] cks = request.getParamValues("ck");
            if(cks==null || cks.length==0){
            	err += "没有选择入库信息，无法入库!<br>";
            	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有选择入库信息，无法入库!");
            }
            
            //入库单编码
            String inCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_04);
            //保存自制件采购的集合
            List<TtPartOemPoConfirmPO> list02zz = new ArrayList<TtPartOemPoConfirmPO>();
            //循环先入库非自制件采购
        	for (int i = 0; i < cks.length; i++) {
				String conId = cks[i];
				TtPartOemPoConfirmPO cpo = new TtPartOemPoConfirmPO();
				cpo.setConId(Long.valueOf(conId));
				
				TtPartOemPoConfirmPO cpos = (TtPartOemPoConfirmPO) dao.select(cpo).get(0);
				
				TtPartOemPoPO opo = new TtPartOemPoPO();
				opo.setPoId(cpos.getPoId());
				
				TtPartOemPoPO opos = (TtPartOemPoPO) dao.select(opo).get(0);
				
				if(opos.getProduceFac().equals(Constant.PURCHASE_WAY_06)){
					//02自制 存入集合等待处理
					list02zz.add(cpos);
				}else{
					this.validateBuprice(act, opos);//验证采购价
					
					//非自制件采购，直接入库
					String dInQty = request.getParamValue("dInQty_"+conId);
					String inRemark = request.getParamValue("inRemark_"+conId);
					if(StringUtil.notNull(inRemark)){
						if(inRemark.length()>100){
							inRemark = inRemark.substring(0, 100);
						}
					}
					Long qty = Long.valueOf(dInQty);
					if(qty<=0){
						err += "配件["+cpos.getPartOldcode()+"]入库数"+qty+"，必须大于0，无法入库!<br>";
		            	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
					}
					if(qty > (cpos.getConQty()-cpos.getInQty())){
						err += "配件["+cpos.getPartOldcode()+"]入库数"+qty+"，大于可入库["+(cpos.getConQty()-cpos.getInQty())+"]，无法入库!<br>";
		            	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
					}
					
					TtPartOemPoConfirmPO  cpox = new TtPartOemPoConfirmPO();
					cpox.setInQty(cpos.getInQty()+qty);
					cpox.setUpdateBy(loginUser.getUserId());
					cpox.setUpdateDate(new Date());
					dao.update(cpo, cpox);
					
					//件入库
					this.instockMain(inCode,cpos.getPoId().toString(),dInQty,inRemark,err,act,loginUser);
				}
				
			}
            //处理自制件采购
        	if(list02zz!=null && !list02zz.isEmpty() && list02zz.size()>0){
        		for (int i = 0; i < list02zz.size(); i++) {
        			TtPartOemPoConfirmPO pf = list02zz.get(i);
					
					
        			TtPartOemPoConfirmPO cpo = new TtPartOemPoConfirmPO();
        			cpo.setConId(pf.getConId());
        			
					//自制件采购 --》总成拆分 分总成，只要1个分总成入库其他分总成, 都入库来源ls
					String dInQty = request.getParamValue("dInQty_"+pf.getConId());
					String inRemark = request.getParamValue("inRemark_"+pf.getConId());
					if(StringUtil.notNull(inRemark)){
						if(inRemark.length()>100){
							inRemark = inRemark.substring(0, 100);
						}
					}
					
					Long qty = Long.valueOf(dInQty);
					if(qty<=0){
						err += "配件["+pf.getPartOldcode()+"]入库数"+qty+"，必须大于0，无法入库!<br>";
		            	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
					}
					if(qty > (pf.getConQty()-pf.getInQty())){
						err += "配件["+pf.getPartOldcode()+"]入库数"+qty+"，大于可入库["+(pf.getConQty()-pf.getInQty())+"]，无法入库!<br>";
		            	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
					}
					
					TtPartOemPoConfirmPO  cpox = new TtPartOemPoConfirmPO();
					cpox.setInQty(pf.getInQty()+qty);
					cpox.setUpdateBy(loginUser.getUserId());
					cpox.setUpdateDate(new Date());
					dao.update(cpo, cpox);
					
					List<Map<String,Object>> splitList = dao.getSplitPartInfoByPoId(pf.getPoId().toString());
					if(splitList!=null && !splitList.isEmpty() && splitList.size()==2){
						
						//采购方式：自制件采购只能是一个材料件，一个费用件
						Map<String,Object> mapz1 = splitList.get(0);
						String pid1 = CommonUtils.checkNull(mapz1.get("PO_ID"));
						String spn1 = CommonUtils.checkNull(mapz1.get("SPLIT_NUM"));
						
						Map<String,Object> mapz2 = splitList.get(1);
						String pid2 = CommonUtils.checkNull(mapz2.get("PO_ID"));
						String spn2 = CommonUtils.checkNull(mapz2.get("SPLIT_NUM"));
						
						//--
						TtPartOemPoPO p1 = new TtPartOemPoPO();
						p1.setPoId(Long.valueOf(pid1));
						p1 = (TtPartOemPoPO) dao.select(p1).get(0);
						
						TtPartOemPoPO p2 = new TtPartOemPoPO();
						p2.setPoId(Long.valueOf(pid2));
						p2 = (TtPartOemPoPO) dao.select(p2).get(0);
						//--
						this.validateBuprice(act, p1);//验证采购价
						this.validateBuprice(act, p2);//验证采购价
						
						if(pf.getPoId().toString().equals(pid1)){
							//判断是否最低数或者是他的整数倍
							if(qty%Double.valueOf(spn1)!=0){
								//非整数倍
								err += "配件["+p1.getPartOldcode()+"]最低入库数为："+spn1+"，或者为"+spn1+"的整数倍，当前入库数："+qty+"!<br>";
								throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+p1.getPartOldcode()+"]最低入库数为："+spn1+"，或者为"+spn1+"的整数倍，当前入库数："+qty+"!<br>");
							}
							
							Double diq2t = Double.valueOf(spn2)/Double.valueOf(spn1)*qty;
							String q2 = String.valueOf(Math.round(diq2t));
							
							//写入材料费
							this.instockMain(inCode,pid1,dInQty,inRemark,err,act,loginUser);
							//写入加工费
							this.instockMain(inCode,pid2,q2,inRemark,err,act,loginUser);
						}
						
						if(pf.getPoId().toString().equals(pid2)){
							if(qty%Double.valueOf(spn2)!=0){
								//非整数倍
								err += "配件["+p2.getPartOldcode()+"]最低入库数为："+spn2+"，或者为"+spn2+"的整数倍，当前入库数："+qty+"!<br>";
								throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+p2.getPartOldcode()+"]最低入库数为："+spn2+"，或者为"+spn2+"的整数倍，当前入库数："+qty+"!<br>");
							}
							Double diq1t = Double.valueOf(spn1)/Double.valueOf(spn2)*qty;
							String q1 = String.valueOf(Math.round(diq1t));
							
							//写入材料费
							this.instockMain(inCode,pid2,dInQty,inRemark,err,act,loginUser);
							//写入加工费
							this.instockMain(inCode,pid1,q1,inRemark,err,act,loginUser);
						}
						
					}else{
						err += "配件["+pf.getPartOldcode()+"]为自制件采购，该件只能有2个分总成，分总成异常!<br>";
						throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pf.getPartOldcode()+"]为自制件采购，该件只能有2个分总成，分总成异常!<br>");
					}
		           
				}
        		
        	}
            
            List<Object> ins5 = new LinkedList<Object>();
            ins5.add(0, inCode);
            dao.callProcedure("PKG_PART.P_AUTO_CREATE_DEMOLITION", ins5, null);
            
        	act.setOutData("success", "入库成功！");
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购到货入库异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage("入库失败！"+e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}*/
	
	
	@SuppressWarnings("unchecked")
	private void validateBuprice(ActionContext act,TtPartOemPoPO opos) throws Exception{
		/*==========================无采购价，不让入库===========================================*/
		//财务供应商
		String sql = "SELECT DECODE(VF.SUPERIOR_PURCHASING, 97141003, "+opos.getVenderId()+", VF.VENDER_ID) V_FCVENDERID FROM TT_PART_VENDER_FIN VF WHERE VF.SUPERIOR_PURCHASING = '"+opos.getSuperiorPurchasing()+"'";
		Map<String,Object> mapVender = dao.pageQueryMap(sql, null, "instockPurRcvParts");
		String venderIdFin = "";
		if(mapVender==null){
        	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+opos.getPartOldcode()+"]无供应商无法入库!<br>!");
		}else{
			venderIdFin = CommonUtils.checkNull(mapVender.get("V_FCVENDERID"));
		}
		if(StringUtil.isNull(venderIdFin)){
        	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+opos.getPartOldcode()+"]无供应商无法入库!<br>!");
		}
		//判断采购价
		TtPartBuyPricePO bup = new TtPartBuyPricePO();
		bup.setPartId(opos.getPartId());
		bup.setVenderId(Long.valueOf(venderIdFin));
		List<TtPartBuyPricePO> priceList = dao.select(bup);
		if(priceList!=null && !priceList.isEmpty() && priceList.size()>0){
			TtPartBuyPricePO buprice = priceList.get(0);
			if(buprice.getBuyPrice()<=0){
            	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+opos.getPartOldcode()+"]采购价异常，无法入库!<br>");
			}
		}else{
        	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+opos.getPartOldcode()+"]无采购价，无法入库!<br>");
		}
		/*=========================无采购价，不让入库============================================*/
	}
	
	/*@SuppressWarnings("unchecked")
	private void instockMain(String inCode,String poId,String dInQty, String inRemark, String err,ActionContext act, AclUserBean loginUser) throws Exception{
		TtPartOemPoPO po = new TtPartOemPoPO();
		po.setPoId(Long.valueOf(poId));
		
		TtPartOemPoPO pos = (TtPartOemPoPO) dao.select(po).get(0);
		
		Long dInQtyL = 0L;
		if(Long.valueOf(dInQty)<=0){
			err += "配件["+pos.getPartOldcode()+"]入库数量["+dInQty+"]必须大于0!<br>";
			throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]入库数量["+dInQty+"]必须大于0!<br>");
		}else{
			dInQtyL = Long.valueOf(dInQty);
		}
		
		Long krQty = pos.getCheckQty() - pos.getInQty();//可入库 = 采购 - 已入库
		if(dInQtyL>krQty){
			err += "配件["+pos.getPartOldcode()+"]入库数量["+dInQty+"]不能大于可入库数量["+krQty+"]!<br>";
			throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]入库数量["+dInQty+"]不能大于可入库数量["+krQty+"]!<br>");
		}
		
		if (pos.getState().equals(Constant.PART_PURCHASE_ORDERCHK_STATUS_02)) {
			err += "配件["+pos.getPartOldcode()+"]已经被关闭,请打开后再入库!<br>";
			throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]已经被关闭,请打开后再入库!<br>");
        }
		
		boolean isLocked = dao.isLocked(pos.getPartId(), pos.getWhId(), loginUser.getDealerId() == null ? loginUser.getOrgId() : Long.valueOf(loginUser.getDealerId()));//当前仓库中的配件是否被锁定
		if (isLocked) {//如果该配件在库存中已经被锁定
			err += "配件["+pos.getPartOldcode()+"]已经被锁定,请重新选择!<br>";
			throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]已经被锁定,请重新选择!<br>");
        }else{
        	//判断当前要入库的仓库是否有效
            TtPartWarehouseDefinePO warehouseDefinePO = new TtPartWarehouseDefinePO();
            warehouseDefinePO.setWhId(pos.getWhId());
            warehouseDefinePO.setState(Constant.STATUS_ENABLE);
            warehouseDefinePO.setStatus(1);

            List<TtPartWarehouseDefinePO> wList = dao.select(warehouseDefinePO);
            if(wList==null || wList.isEmpty() || wList.size() == 0){
            	err += "配件["+pos.getPartOldcode()+"]对应的入库库房已经失效,请选择重新选择!<br>";
                throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]对应的入库库房已经失效,请选择重新选择!<br>");
            }else{
            	//判断是否有对应货位信息
            	TtPartOemPoPO pox = new TtPartOemPoPO();
            	pox.setInQty(pos.getInQty() + dInQtyL);
            	pox.setSpareQty(dInQtyL);//待入库
            	if(pos.getCheckQty().intValue() == pox.getInQty()){
            		pox.setState(Constant.PART_PURCHASE_ORDERCIN_STATUS_02);
            	}
            	pox.setRemark(inRemark);
            	pox.setInBy(loginUser.getUserId());
            	pox.setInDate(new Date());
            	
            	
            	Long partId = pos.getPartId();//获取配件id
                Long orgId = pos.getOrgId();//获取单位id
                Long whId = pos.getWhId();
                List<OrgBean> orgBeanList = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
                Map<String, Object> locMap = PartDlrInstockDao.getInstance().getLoc(pos.getPartId().toString(), whId.toString(), orgBeanList.get(0).getOrgId().toString());
                Map<String, Object> map = dao.queryPartAndLocationInfo(partId, orgId, whId);//查询当前配件信息及其货位信息
                TtPartWarehouseDefinePO warehousePO = wList.get(0);
                
                //入库的时候要判断相同验收单号、相同配件、相同库房是否已经入过库了,如果已经入库,那再次入库就更新入库数量,否则新增入库信息
                //Map<String, Object> Imap = dao.getInfoByChkCodeAndWhIdAndPartId(pos.getOrderCode(), whId, partId);
                
                TtPartPoInPO inPo = new TtPartPoInPO();//需要将每次入库的信息保存到入库表中
                Long InId = CommonUtils.parseLong(SequenceManager.getSequence(""));
                inPo.setInId(InId);
                inPo.setInCode((inCode));
                inPo.setInType(Constant.PART_PURCHASE_ORDERCIN_TYPE_01);
                inPo.setPoId(pos.getPoId());//采购订单iD
                inPo.setCheckId(-1L);
                inPo.setOrderCode(pos.getOrderCode());//采购订单号
                inPo.setPlanCode(pos.getPlanCode());//计划单号
                if (map != null && map.get("BUYER_ID") != null) {
                    inPo.setBuyerId(pos.getBuyerId());//采购员ID
                }
                TtPartDefinePO dp = new TtPartDefinePO();
                dp.setPartOldcode(pos.getPartOldcode());
                dp = (TtPartDefinePO) dao.select(dp).get(0);
                
                String up = CommonUtils.checkNull(dp.getSuperiorPurchasing());
                if (!up.equals("") && up != null) {
                	//财务供应商，结算用
                	inPo.setFcvderId(pos.getVenderId());
                    inPo.setFcvderrCode(pos.getVenderCode());
                } else {
                    inPo.setFcvderId(pos.getVenderId());
                    inPo.setFcvderrCode(pos.getVenderCode());
                }
                inPo.setPartType(pos.getPartType());//配件类型
                inPo.setPartId(pos.getPartId());//配件ID
                inPo.setPartCode(pos.getPartCode());//配件件号
                inPo.setPartOldcode(pos.getPartOldcode());//配件编码
                inPo.setPartCname(pos.getPartCname());//配件名称
                inPo.setUnit(pos.getUnit());//配件包装单位
                inPo.setProduceFac(Long.valueOf(pos.getProduceFac()));
                inPo.setSpareQty(dInQtyL);//待结算数量
                inPo.setVenderId(pos.getVenderId());//配件供应商ID
                inPo.setVenderCode(pos.getVenderCode());//配件供应商编码
                inPo.setVenderName(pos.getVenderName());//配件供应商名称
                //inPo.setMakerId(pos.getMakerId());//制造商id
                inPo.setBuyPrice(pos.getBuyPrice());//配件采购单价，通过和供应商关系获取
                inPo.setBuyQty(pos.getBuyQty());//采购数量
                inPo.setCheckQty(pos.getCheckQty());//验货数量
                inPo.setInQty(dInQtyL);//已入库数量
                inPo.setBuyPriceNotax(Arith.round(Arith.div(pos.getBuyPrice(), Arith.add(1, Constant.PART_TAX_RATE)), 6));//无税单价
                inPo.setSuperiorPurchasing(pos.getSuperiorPurchasing());
                inPo.setProduceState(pos.getProduceState());
                inPo.setInAmount(Arith.mul(pos.getBuyPrice(), inPo.getInQty()));//入库金额
                
                inPo.setErpNo(pos.getErpNo());
                if (po.getErpNo() != null && !po.getErpNo().equals("")) {
                    dao.insert("  INSERT INTO XXPO_PMS_INPUT_ITF (SEQ_ID, PO_NUMBER, PO_QUANTITY, RECEIVE_DATE)   SELECT F_GETID, '" + po.getErpNo() + "', " + inPo.getInQty() + ", SYSDATE FROM DUAL");
                }
                
                inPo.setTaxRate(Constant.PART_TAX_RATE);//税率
                inPo.setInAmountNotax(Arith.mul(inPo.getBuyPriceNotax(), inPo.getInQty()));//无税总金额
                inPo.setWhId(whId);//库房iD
                inPo.setWhName(warehousePO.getWhName());//库房2015032711875757名称
                inPo.setLocCode((String) locMap.get("LOC_CODE"));//货位编码
                inPo.setLocId(((BigDecimal) locMap.get("LOC_ID")).longValue());//货位ID
                
                *//**---------------------每次入库更新下货位（无实际意义）-----------------------------**//*
                pox.setLocCode(CommonUtils.checkNull(locMap.get("LOC_CODE")));
                pox.setLocId(((BigDecimal) locMap.get("LOC_ID")).longValue());//货位ID
                *//**--------------------------------------------------**//*
                
                inPo.setCheckDate(new Date());
                inPo.setCheckBy(loginUser.getUserId());
                //查询库存数量
                VwPartStockPO itemStockPO = new VwPartStockPO();
                itemStockPO.setPartId(inPo.getPartId());
                itemStockPO.setLocId(inPo.getLocId());
                itemStockPO.setOrgId(inPo.getOrgId());
                itemStockPO.setWhId(inPo.getWhId());
                itemStockPO.setState(Constant.STATUS_ENABLE);
                List<VwPartStockPO> list = dao.select(itemStockPO);
                if (list.size() > 0) {
                    itemStockPO = (VwPartStockPO) list.get(0);
                    inPo.setItemQty(itemStockPO.getItemQty());
                }
                inPo.setRemark(inRemark);
                inPo.setInDate(new Date());
                inPo.setInBy(loginUser.getUserId());
                inPo.setCreateDate((new Date()));
                inPo.setCreateBy(loginUser.getUserId());
                inPo.setOrgId(loginUser.getOrgId());
                inPo.setOriginType(pos.getOriginType());
                //配件只要入库，入库单的状态就变为结算中
                inPo.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01);
                
                
                
                //插入出入库记录表 add by yuan 20130513
                TtPartRecordPO ttPartRecordPO = new TtPartRecordPO();
                ttPartRecordPO.setRecordId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                ttPartRecordPO.setAddFlag(1);//入库标记
                ttPartRecordPO.setState(1);//正常入库
                ttPartRecordPO.setPartNum(dInQtyL);//入库数量
                ttPartRecordPO.setTranstypeId(0l);//默认0
                ttPartRecordPO.setPartId(pos.getPartId());//配件ID
                ttPartRecordPO.setPartCode(pos.getPartCode());//配件件号
                ttPartRecordPO.setPartOldcode(pos.getPartOldcode());//配件编码
                ttPartRecordPO.setPartName(pos.getPartCname());//配件名称
                ttPartRecordPO.setPartBatch("1306");//////////////////配件批次
                ttPartRecordPO.setVenderId(21799l);///////////////////配件供应商
                ttPartRecordPO.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_04));//入库单
                ttPartRecordPO.setOrderId(InId);//入库单ID
                ttPartRecordPO.setOrderCode(inCode);//入库单编码
                //ttPartRecordPO.setLineId();
                ttPartRecordPO.setOrgId(orgBeanList.get(0).getOrgId());
                ttPartRecordPO.setOrgCode(orgBeanList.get(0).getOrgCode());
                ttPartRecordPO.setOrgName(orgBeanList.get(0).getOrgName());
                ttPartRecordPO.setWhId(whId);
                ttPartRecordPO.setWhName(warehousePO.getWhName());
                //modify by yuan 20130809
                //ttPartRecordPO.setLocId(((BigDecimal) map.get("LOC_ID")).longValue());
                //ttPartRecordPO.setLocCode((String) map.get("LOC_CODE"));
                //ttPartRecordPO.setLocName((String) map.get("LOC_NAME"));
                ttPartRecordPO.setLocId(Long.valueOf(CommonUtils.checkNull(locMap.get("LOC_ID"))));
                ttPartRecordPO.setLocCode(CommonUtils.checkNull(locMap.get("LOC_CODE")));
                ttPartRecordPO.setLocName(CommonUtils.checkNull(locMap.get("LOC_NAME")));
                ttPartRecordPO.setOptDate(new Date());
                ttPartRecordPO.setCreateDate(new Date());
                ttPartRecordPO.setPersonId(loginUser.getUserId());
                ttPartRecordPO.setPersonName(loginUser.getName());
                ttPartRecordPO.setPartState(1);
                
                dao.insert(inPo);//新增订单入库信息
                dao.insert(ttPartRecordPO);//新增出入库记录
                dao.update(po, pox);//修改信息
                //调用入库逻辑
                List<Object> ins = new LinkedList<Object>();
                ins.add(0, InId);
                ins.add(1, Constant.PART_CODE_RELATION_04);
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
                
            }
        }
		
	}
	*/
	
	
	/**
	 * 跳转到中储采购订单查询页面
	 */
	public void toPurchaseOrderCMSTPrintInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PURCHASE_ORDER_CMST_PRINT_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到中储采购订单查询页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 根据中储采购订单查询
	 */
	public void getPurchaseOrderCMSTPrintInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurchaseOrderCMSTPrintInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "中储采购订单打印页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 跳转到中储采购订单打印
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void toPurchaseOrderCMSTPrint(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String[] poIns = request.getParamValues("ck");
			List<Map<String, Object>> printList = new ArrayList<Map<String, Object>>();
			if (poIns != null) {
				String poInStr = "";
				for (int i=0;i<poIns.length;i++ ){
					if(i==0){
						poInStr = poIns[i];
					}else{
						poInStr = poInStr+","+ poIns[i];
					}
				}
				List mainInfos = dao.getPurchaseOrderCMSTPrintMain(poInStr);
				for (int j = 0; j < mainInfos.size(); j++) {
					Map tmpMap =  (HashMap) mainInfos.get(j);
					String ORDER_ID = (java.math.BigDecimal) tmpMap.get("ORDER_ID")+"";
					List dtlList = dao.getPurchaseOrderCMSTPrintDtl(poInStr,ORDER_ID);
					
					for(int k = 0;k<dtlList.size();k++){
						
						Map dtlMap = (HashMap) dtlList.get(k); 
						String poid= (java.math.BigDecimal) dtlMap.get("PO_ID")+"";
						String printQty  = request.getParamValue("printQty_"+poid);
						dtlMap.put("PRINTQTY", printQty);
						dao.createOemOrderPrintLog(poid,loginUser,printQty);
					}
					tmpMap.put("dtlList", dtlList);
					tmpMap.put("pageNOs", dtlList.size());
					printList.add(tmpMap);
				}
			}
			
			act.setOutData("printList", printList);
			act.setForword(TO_PURCHASE_ORDER_CMST_PRINT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到中储采购订单查询页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 跳转到采购到货打印页面
	 */
	public void toPurRcvPrintInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PUR_RCV_PRINT_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购到货查询页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询采购到货已确认信息
	 */
	public void getPurRcvPrintInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurRcvPrintInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询采购到货已确认信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到采购提货确认始页面
	 */
	public void toPurPickInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			 String saler = "配件采购员";
			 List<Map<String, Object>> salerList = partPlannerQueryDao.getInstance().getUsers(saler, null);
			 act.setOutData("salerList", salerList);
			 act.setOutData("nowUserId", loginUser.getUserId());
			 act.setForword(TO_PUR_PICK_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购提货确认始页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询采购订单信息
	 */
	public void getPurPickInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurPickInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询采购订单信息异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 保存提货确认信息
	 *//*
	@SuppressWarnings("unchecked")
	public void savePurPickQty(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String[] cks = request.getParamValues("ck");
			if(cks!=null && cks.length>0){
				for (int i = 0; i < cks.length; i++) {
					String poId = cks[i];
					TtPartOemPoPO po = new TtPartOemPoPO();
					po.setPoId(Long.valueOf(poId));
					
					TtPartOemPoPO pos = (TtPartOemPoPO) dao.select(po).get(0);
					if(pos.getState().equals(Constant.PART_PURCHASE_ORDERCHK_STATUS_02)){
						throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]订单已关闭，无法确认提货!");
					}
					String dInQty = request.getParamValue("dInQty_"+poId);
					String inRemark = request.getParamValue("inRemark_"+poId);
					
					TtPartOemPoPickPO pick = new TtPartOemPoPickPO();
					pick.setPickId(Long.valueOf(SequenceManager.getSequence("")));
					pick.setPoId(pos.getPoId());
					pick.setPickQty(Long.valueOf(dInQty));
					pick.setBuyQty(pos.getBuyQty());
					pick.setPartId(pos.getPartId());
					pick.setPartOldcode(pos.getPartOldcode());
					pick.setPartCname(pos.getPartCname());
					pick.setPartCode(pos.getPartCode());
					if(StringUtil.notNull(inRemark)){
						if(inRemark.length()>200){
							inRemark = inRemark.substring(0, 200);
						}
						pick.setRemark(inRemark);
					}
					pick.setCreateDate(new Date());
					pick.setCreateBy(loginUser.getUserId());
					
					dao.insert(pick);
				}
				
			}else{
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有需要提货的信息!");
			}
			
			act.setOutData("success", "保存成功！");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存提货确认信息异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	*/
	
	/**
	 * 跳转到采购提货查询初始页面
	 */
	public void toPurPickSelectInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			 String saler = "配件采购员";
			 List<Map<String, Object>> salerList = partPlannerQueryDao.getInstance().getUsers(saler, null);
			 act.setOutData("salerList", salerList);
			 act.setOutData("nowUserId", loginUser.getUserId());
			 act.setForword(TO_PUR_PICK_SELECT_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购提货查询初始页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询采购提货确认信息
	 */
	public void getPurPickSelectInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurPickSelectInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询采购提货确认信息异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到采购提货打印初始页面
	 */
	public void toPurPickPrintInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			 String saler = "配件采购员";
			 List<Map<String, Object>> salerList = partPlannerQueryDao.getInstance().getUsers(saler, null);
			 act.setOutData("salerList", salerList);
			 act.setOutData("nowUserId", loginUser.getUserId());
			 act.setForword(TO_PUR_PICK_PRINT_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购提货确认始页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询采购提货确认信息
	 */
	public void getPurPickPrintInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurPickPrintInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询采购提货确认信息异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到提货单打印页面
	 *//*
	@SuppressWarnings("unchecked")
	public void toPurPickPrint(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String[] cks = request.getParamValues("ck");
			List<Map<String, Object>> printList = new ArrayList<Map<String, Object>>();
			if(cks!=null && cks.length>0){
				String pickIds = "";
				for (int i = 0; i < cks.length; i++) {
					String pickId = cks[i];
					
					TtPartOemPoPickPO pick = new TtPartOemPoPickPO();
					pick.setPickId(Long.valueOf(pickId));
					
					TtPartOemPoPickPO picks = (TtPartOemPoPickPO) dao.select(pick).get(0);
					
					TtPartOemPoPickPO pickx = new TtPartOemPoPickPO();
					pickx.setIsPrint(Constant.IF_TYPE_YES);
					pickx.setPrintBy(loginUser.getUserId());
					pickx.setPrintDate(new Date());
					pickx.setPrintNum(picks.getPrintNum() + 1);
					
					dao.update(pick, pickx);
					if(StringUtil.notNull(pickIds)){
						pickIds = pickIds +","+ pickId;
					}else{
						pickIds = pickId;
					}
				}
				
				List<Map<String,Object>> mainInfos = dao.getPurPickMain(pickIds);
				for (int j = 0; j < mainInfos.size(); j++) {
					Map<String,Object> tmpMap =  mainInfos.get(j);
					String ORDER_ID = CommonUtils.checkNull(tmpMap.get("ORDER_ID"));
					List<Map<String,Object>> dtlList = dao.getPurPickDtl(pickIds,ORDER_ID);
					
					tmpMap.put("dtlList", dtlList);
					tmpMap.put("pageNOs", dtlList.size());
					printList.add(tmpMap);
				}
				
			}
			
			act.setOutData("printList", printList);
			act.setForword(TO_PUR_PICK_PRINT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到提货单打印页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	*/
	
	/**
	 * 跳转到采购订单查询页面
	 * 专用车厂，总装车间，涂装车间 -接收订单
	 */
	public void toPurOrderFactorySelectInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			//根据人员查询上级采购单位--配件人员类型设置 
			List<Map<String,Object>> suList = dao.getSupSj(loginUser);
			act.setOutData("suList", suList);
			act.setForword(TO_PUR_ORDER_FACTORY_SELECT_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单查询页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询采购订单
	 * 专用车厂，总装车间，涂装车间 -接收订单
	 */
	public void getPurOrderFactorySelectInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurOrderFactorySelectInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询采购订单信息异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到采购订单关闭页面
	 */
	public void toPurOrderCloseInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PUR_ORDER_CLOSE_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单关闭页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询采购可关闭订单信息
	 */
	public void getPurOrderCloseInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurOrderCloseInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询采购可关闭订单信息异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 关闭订单配件
	 * (查出未确认，已关闭状态，只有未确认才能关闭，已关闭的能再开启，开启后是未确认状态)
	 */
	@SuppressWarnings("unchecked")
	public void optClosePurOrder(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String[] cks = request.getParamValues("ck");
			String CLOSE_REMARK = request.getParamValue("CLOSE_REMARK");
			if(CLOSE_REMARK.length()>100){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "关闭原因请在100字内，当前长度"+CLOSE_REMARK.length()+"!");
			}
			
			if(cks!=null && cks.length>0){
				for (int i = 0; i < cks.length; i++) {
					String polineId = cks[i];
					TtPartPoDtlPO po = new TtPartPoDtlPO();
					po.setPolineId(Long.valueOf(polineId));					
					
					TtPartPoDtlPO pos = (TtPartPoDtlPO) dao.select(po).get(0);
					if(Constant.PARTS_ORDER_OEM_STATUS_NO.equals(pos.getState().toString())||Constant.PARTS_ORDER_OEM_STATUS_WEI.equals(pos.getState().toString())){
						//if(pos.getCheckQty().intValue()>pos.getInQty().intValue()){
						TtPartPoDtlPO pox = new TtPartPoDtlPO();
							pox.setState(Integer.parseInt(Constant.PARTS_ORDER_OEM_STATUS_CLOSE));
							pox.setCloseRemark(CLOSE_REMARK);
							pox.setDisableDate(new Date());
							pox.setDisableBy(loginUser.getUserId());
							
							dao.update(po, pox);
							
						/*}else{
							throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]入库数量异常，无法关闭!");
						}*/
					}else{
						throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]状态不是未确认，无法关闭!");
					}
				}
				
			}else{
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有需要关闭的信息!");
			}
			act.setOutData("success", "关闭成功！");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "关闭订单配件异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 开启订单配件
	 */
	@SuppressWarnings("unchecked")
	public void optOpenPurOrder(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String[] cks = request.getParamValues("ck");
			String CLOSE_REMARK = request.getParamValue("CLOSE_REMARK");
			if(CLOSE_REMARK.length()>100){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "开启原因请在100字内，当前长度"+CLOSE_REMARK.length()+"!");
			}
			
			if(cks!=null && cks.length>0){
				for (int i = 0; i < cks.length; i++) {
					String polineId = cks[i];
					TtPartPoDtlPO po = new TtPartPoDtlPO();
					po.setPolineId(Long.valueOf(polineId));
					
					
					TtPartPoDtlPO pos = (TtPartPoDtlPO) dao.select(po).get(0);
					if(Constant.PARTS_ORDER_OEM_STATUS_CLOSE.equals(pos.getState().toString())){
							TtPartPoDtlPO pox = new TtPartPoDtlPO();
							if(pos.getCheckQty().intValue()>0){
							pox.setState(Integer.parseInt(Constant.PARTS_ORDER_OEM_STATUS_NO));
							}else{
								pox.setState(Integer.parseInt(Constant.PARTS_ORDER_OEM_STATUS_WEI));
							}
							pox.setOpenRemark(CLOSE_REMARK);
							pox.setUpdateDate(new Date());
							pox.setUpdateBy(loginUser.getUserId());
							dao.update(po, pox);
							
					}else{
						throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]状态不是已关闭，无法开启!");
					}
				}
				
			}else{
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有需要关闭的信息!");
			}
			act.setOutData("success", "开启成功！");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "开启订单配件异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 导出订单汇总信息
	 */
	public void exportPurOrderHz(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
		try{
			List<Map<String, Object>> listRs = dao.getPurcharOrderHz(request, loginUser, 1, Constant.PAGE_SIZE_MAX).getRecords();
			if(listRs!=null && !listRs.isEmpty() && listRs.size()>60000){
				throw new BizException(act,ErrorCodeConstant.QUERY_FAILURE_CODE,"导出订单汇总信息超过6万条，请输入查询条件，多次导出!");
			}
			List<String> listHead = new ArrayList<String>();//导出模板第一列
            //标题
			listHead.add("采购单号 ");
            listHead.add("计划单号");
            listHead.add("供应商编码");
            listHead.add("供应商名称");
            listHead.add("采购种类");
            listHead.add("采购数量");
            listHead.add("订单日期");
            
            List<String> listKey = new ArrayList<String>();
            listKey.add("ORDER_CODE");
            listKey.add("PLAN_CODE");
            listKey.add("VENDER_CODE");
            listKey.add("VENDER_NAME");
            listKey.add("PART_COUNT");
            listKey.add("CHECK_QTY_SUM");
            listKey.add("CREATE_DATE");
            
			// 导出的文件名
            String fileName = "采购订单汇总表"+DateUtil.format(new Date(), "yyyy_MM_dd")+".xls";
            // 分页导出
            pagingExportExcel(response,fileName,listHead,listKey,listRs,50000);
            
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"导出订单汇总信息异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser,e1);
			act.setException(e1);
			this.toPurchaseOrderInit();
		}
	}
	
	/**
	 * 下载周度计划批量修改模板
	 */
	public void expPlanWeekUpdateTemplate(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
		try{
			List<Map<String, Object>> listRs = dao.getPlanWeekUpdateTemplateInfo(request,null);
			List<String> listHead = new ArrayList<String>();//导出模板第一列
            //标题
			listHead.add("唯一标识 ");
            listHead.add("计划单号");
            listHead.add("计划月份");
            listHead.add("配件编码");
            listHead.add("配件名称");
            listHead.add("配件件号");
            listHead.add("计划室确认数");
            listHead.add("第一周");
            listHead.add("第二周");
            listHead.add("第三周");
            listHead.add("第四周");
            listHead.add("第五周");
            listHead.add("计划名称");
            listHead.add("配件备注");
            listHead.add("转单周期");
            listHead.add("周数");
            listHead.add("最小包装量");
            
            List<String> listKey = new ArrayList<String>();
            listKey.add("PLAN_LINE_ID");
            listKey.add("PLAN_NO");
            listKey.add("MONTH_DATE");
            listKey.add("PART_OLDCODE");
            listKey.add("PART_CNAME");
            listKey.add("PART_CODE");
            listKey.add("LAST_CONFIRM_NUM");
            listKey.add("WEEK_ONE");
            listKey.add("WEEK_TOW");
            listKey.add("WEEK_THREE");
            listKey.add("WEEK_FOUR");
            listKey.add("WEEK_FIVE");
            listKey.add("PLAN_NAME");
            listKey.add("SALES_REMARK");
            listKey.add("ORDER_PERIOD_CN");
            listKey.add("WEEK_COUNT");
            listKey.add("BUY_MIN_PKG");
            
			// 导出的文件名
            String fileName = "周度计划数量批量修改模板"+DateUtil.format(new Date(), "yyyy_MM_dd")+".xls";
            // 分页导出
            pagingExportExcel(response,fileName,listHead,listKey,listRs,50000);
            
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"下载周度计划批量修改模板异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser,e1);
			act.setException(e1);
			this.toPartPlanValidationJhsCreatePur();
		}
	}
	
	/**
	 * 导出采购到货确认
	 */
	public void exportPurRcv(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
		try{
			List<Map<String, Object>> listRs = dao.getPurRcvInfo(request, loginUser, 1, Constant.PAGE_SIZE_MAX).getRecords();
			List<String> listHead = new ArrayList<String>();//导出模板第一列
			
            //标题
			listHead.add("订单单号 ");
            listHead.add("配件编码");
            listHead.add("配件名称");
            listHead.add("配件件号");
            listHead.add("采购数量");
            listHead.add("已确认数量");
            listHead.add("待确认数量");
            listHead.add("已入库数量");
            listHead.add("入库备注");
            listHead.add("计划名称");
            listHead.add("计划备注");
            listHead.add("库房");
            listHead.add("货位");
            listHead.add("单位");
            listHead.add("供应商");
            listHead.add("中储名称");
            listHead.add("配件类型");
            listHead.add("制单日期");
            listHead.add("状态");
            
            List<String> listKey = new ArrayList<String>();
            listKey.add("ORDER_CODE");
            listKey.add("PART_OLDCODE");
            listKey.add("PART_CNAME");
            listKey.add("PART_CODE");
            listKey.add("BUY_QTY");
            listKey.add("YCON_QTY");
            listKey.add("DCON_QTY");
            listKey.add("IN_QTY");
            listKey.add("IN_REMARK");
            listKey.add("PLAN_NAME");
            listKey.add("PLAN_REMARK");
            listKey.add("WH_NAME");
            listKey.add("LOC_CODE");
            listKey.add("UNIT");
            listKey.add("VENDER_NAME");
            listKey.add("CMST_NAME");
            listKey.add("PART_TYPE_CN");
            listKey.add("CREATE_DATE");
            listKey.add("STATE_CN");
            
			// 导出的文件名
            String fileName = "到货确认"+DateUtil.format(new Date(), "yyyy_MM_dd")+".xls";
            // 分页导出
            pagingExportExcel(response,fileName,listHead,listKey,listRs,50000);
            
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"下载周度计划批量修改模板异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser,e1);
			act.setException(e1);
			this.toPurRcvInit();
		}
	}
	
	/**
	 * 跳转到采购订单打印页面
	 * 专用车厂，总装车间，涂装车间
	 */
	public void toPurOrderFactoryPrintInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			//根据人员查询上级采购单位--配件人员类型设置 
			List<Map<String,Object>> suList = dao.getSupSj(loginUser);
			act.setOutData("suList", suList);
			act.setForword(TO_PUR_ORDER_FACTORY_PRINT_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单打印页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询采购订单
	 * 专用车厂，总装车间，涂装车间 
	 */
	public void getPurOrderFactoryPrintInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurOrderFactoryPrintInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询采购订单信息异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 打印采购送货单
	 */
	public void toPurOrderFactoryPrint(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String[] poIns = request.getParamValues("ck");
			List<Map<String, Object>> printList = new ArrayList<Map<String, Object>>();
			if (poIns != null) {
				String poInStr = "";
				for (int i=0;i<poIns.length;i++ ){
					if(i==0){
						poInStr = poIns[i];
					}else{
						poInStr = poInStr+","+ poIns[i];
					}
				}
				List<Map<String, Object>> mainInfos = dao.getPurchaseOrderFactoryMain(poInStr);
				
				for (int j = 0; j < mainInfos.size(); j++) {
					Map<String, Object> tmpMap =  mainInfos.get(j);
					String ORDER_ID = CommonUtils.checkNull(tmpMap.get("ORDER_ID"));
					List<Map<String, Object>> dtlList = dao.getPurchaseOrderFactoryDtl(poInStr,ORDER_ID);
					
					for(int k = 0;k<dtlList.size();k++){
						Map<String, Object> dtlMap = dtlList.get(k); 
						String poid= CommonUtils.checkNull(dtlMap.get("PO_ID"));
						String printQty  = request.getParamValue("printQty_"+poid);
						dtlMap.put("PRINTQTY", printQty);
						dao.createOemOrderPrintLog(poid,loginUser,printQty);
					}
					tmpMap.put("dtlList", dtlList);
					tmpMap.put("pageNOs", dtlList.size());
					printList.add(tmpMap);
				}
			}
			
			act.setOutData("printList", printList);
			act.setForword(TO_PUR_ORDER_FACTORY_PRINT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到中储采购订单查询页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		
	}
	
	
	
	/**
	 * 跳转到采购订单打印页面
	 * 供应商
	 */
	public void toPurOrderVenderPrintInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PUR_ORDER_VENDER_PRINT_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单打印页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询采购订单
	 * 供应商
	 */
	public void getPurOrderVenderPrintInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurOrderVenderPrintInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询采购订单信息异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 导出采购订单（工厂）
	 */
	public void expExlPurOrderFactory(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
		try{
			List<Map<String, Object>> listRs = dao.getPurOrderFactorySelectInfo(request, loginUser, 1, Constant.PAGE_SIZE_MAX).getRecords();
			List<String> listHead = new ArrayList<String>();//导出模板第一列
			
            //标题
			listHead.add("订单单号 ");
            listHead.add("配件编码");
            listHead.add("配件名称");
            listHead.add("配件件号");
            listHead.add("采购数量");
            listHead.add("未入库数量");
            listHead.add("已入库数量");
            listHead.add("库房");
            listHead.add("货位");
            listHead.add("单位");
            listHead.add("供应商");
            listHead.add("上级单位");
            listHead.add("配件类型");
            listHead.add("自制配套");
            listHead.add("制单日期");
            listHead.add("状态");
            listHead.add("计划名称");
            listHead.add("计划备注");
            
            List<String> listKey = new ArrayList<String>();
            listKey.add("ORDER_CODE");
            listKey.add("PART_OLDCODE");
            listKey.add("PART_CNAME");
            listKey.add("PART_CODE");
            listKey.add("BUY_QTY");
            listKey.add("DIN_QTY");
            listKey.add("IN_QTY");
            listKey.add("WH_NAME");
            listKey.add("LOC_CODE");
            listKey.add("UNIT");
            listKey.add("VENDER_NAME");
            listKey.add("SUPERIOR_PURCHASING");
            listKey.add("PART_TYPE_CN");
            listKey.add("PRODUCE_STATE_CN");
            listKey.add("CREATE_DATE");
            listKey.add("STATE_CN");
            listKey.add("PLAN_NAME");
            listKey.add("SALES_REMARK");
            
			// 导出的文件名
            String fileName = "采购订单（车厂）"+DateUtil.format(new Date(), "yyyy_MM_dd")+".xls";
            // 分页导出
            pagingExportExcel(response,fileName,listHead,listKey,listRs,50000);
            
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"下载周度计划批量修改模板异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser,e1);
			act.setException(e1);
			this.toPurRcvInit();
		}
	}
	
	
	/**
	 * 计划室上传文件批量修改拆分周订单
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void uploadJhsUpdateWeekNum(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String err = "";
		StringBuffer errorInfo = new StringBuffer("");
	    try {
	    	long maxSize = 1024 * 1024 * 5;
	    	int errNum = insertIntoTmp(request, "uploadFile", 17, 2, maxSize,1);
	    	
	    	if (errNum != 0) {
				switch (errNum) {
				case 1:
					err += "文件列数过多!";
					break;
				case 2:
					err += "空行不能大于三行!";
					break;
				case 3:
					err += "文件不能为空!";
					break;
				case 4:
					err += "文件不能为空!";
					break;
				case 5:
					err += "文件不能大于!";
					break;
				default:
					break;
				}
			}
	    	
	    	if (!"".equals(err)) {
				BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "");
				throw e1;
			}else{
				List<Map> list = getMapList();
                List voList = new ArrayList();
                List<String> errList = new ArrayList<String>();
                loadVoListSplit(voList, list, errList);
                
                if (errList.size() > 0) {
                	err = errorInfo.toString();
                	act.setOutData("errList", errList);
                    BizException e1 = new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "");
                    throw e1;
                }
                
                for (int i = 0; i < voList.size(); i++) {
                	TtPartPlanScrollDelPO dtl = (TtPartPlanScrollDelPO) voList.get(i);
                	dtl.setUpdateBy(loginUser.getUserId());
                	dtl.setUpdateDate(new Date());
                	
                	TtPartPlanScrollDelPO dtlOld = new TtPartPlanScrollDelPO();
                	dtlOld.setId(dtl.getId());
                	
                	dao.update(dtlOld, dtl);
                	
                	//數量和不能大於拆分數量
                	TtPartPlanScrollDelPO dtlTemp = (TtPartPlanScrollDelPO) dao.select(dtlOld).get(0);
                	//效验是否大于计划室确认数量
			        Integer rsNum = this.toPaseInt(dtlTemp.getWeekOne());
			        rsNum = rsNum + this.toPaseInt(dtlTemp.getWeekTow());
			        rsNum = rsNum + this.toPaseInt(dtlTemp.getWeekThree());
			        rsNum = rsNum + this.toPaseInt(dtlTemp.getWeekFour());
			        rsNum = rsNum + this.toPaseInt(dtlTemp.getWeekFive());
			        if(rsNum.intValue()>dtlTemp.getLastConfirmNum().intValue()){
			        	errList.add("配件["+dtlTemp.getPartOldcode()+"]，拆分的数量和大于计划室确认数量！<br>");
			        }
                	
                }
                
                if (errList.size() > 0) {
                	err = errorInfo.toString();
                	act.setOutData("errList", errList);
                    BizException e1 = new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "");
                    throw e1;
                }
			}
	    	toPartPlanValidationJhsCreatePur();
	    }catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "上传配件中储信息异常");
            logger.error(loginUser, e1);
            if(StringUtil.notNull(err)){
            	act.setOutData("info", err);
            }else{
            	act.setOutData("info", "上传配件中储信息异常");
            }
            toPartPlanValidationJhsCreatePur();
        }
	}
	
	/**
	 * 循环获取cell，生成数据存入list
	 * @param request
	 * @param emerList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadVoListSplit(List voList, List<Map> list,  List<String> errList)throws Exception {
		if (null == list) {
            list = new ArrayList();
        }
		for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            if (null == map) {
                map = new HashMap<String, Cell[]>();
            }
            Set<String> keys = map.keySet();
            Iterator it = keys.iterator();
            String key = "";
            while (it.hasNext()) {
                key = (String) it.next();
                Cell[] cells = (Cell[]) map.get(key);
                parseCellsSplit(voList, key, cells, errList);
            }
        }
	}
	
	/**
     * 装载VO
     *
     * @param : @param list
     * @param : @param rowNum
     * @param : @param cells
     * @param : @param errorInfo
     * @return :
     * @throws Exception
     * @throws :
     * @Title :
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void parseCellsSplit(List list, String rowNum, Cell[] cells, List<String> errList) throws Exception {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		
		String plineId = "";
		String week1 = "";
		String week2 = "";
		String week3 = "";
		String week4 = "";
		String week5 = "";
		String remark = "";
		
		if(cells.length>=1){
			plineId = CommonUtils.checkNull(cells[0].getContents().trim());
		}
		
		if(cells.length>=8){
			week1 = CommonUtils.checkNull(cells[7].getContents().trim());
		}
		if(cells.length>=9){
			week2 = CommonUtils.checkNull(cells[8].getContents().trim());
		}
		if(cells.length>=10){
			week3 = CommonUtils.checkNull(cells[9].getContents().trim());
		}
		if(cells.length>=11){
			week4 = CommonUtils.checkNull(cells[10].getContents().trim());
		}
		if(cells.length>=12){
			week5 = CommonUtils.checkNull(cells[11].getContents().trim());
		}
		
		if(cells.length>=14){
			remark = CommonUtils.checkNull(cells[13].getContents().trim());
		}
		
		if(StringUtil.notNull(plineId)){
			List<Map<String, Object>> listRs = dao.getPlanWeekUpdateTemplateInfo(request,plineId);
			if(listRs!=null && !listRs.isEmpty() && listRs.size()>0){
				//已存在
				Map<String, Object> map = listRs.get(0);
				if(map!=null){
					//存储
					TtPartPlanScrollDelPO dtl = new TtPartPlanScrollDelPO();
					dtl.setId(Long.valueOf(plineId));
					
					String weekCount = CommonUtils.checkNull(map.get("WEEK_COUNT"));//该计划有几个周一
					String MONTH_DATE = CommonUtils.checkNull(map.get("MONTH_DATE"));//计划月
					String NEXT_WEEK_ONE_MONTH = CommonUtils.checkNull(map.get("NEXT_WEEK_ONE_MONTH"));
					String NEXT_WEEK_ONE_NUM = CommonUtils.checkNull(map.get("NEXT_WEEK_ONE_NUM"));//下周一时第几周
					String W1 = CommonUtils.checkNull(map.get("W1"));
					String W2 = CommonUtils.checkNull(map.get("W2"));
					String W3 = CommonUtils.checkNull(map.get("W3"));
					String W4 = CommonUtils.checkNull(map.get("W4"));
					String W5 = CommonUtils.checkNull(map.get("W5"));
					String BUY_MIN_PKG = CommonUtils.checkNull(map.get("BUY_MIN_PKG"));
					String LAST_CONFIRM_NUM = CommonUtils.checkNull(map.get("LAST_CONFIRM_NUM"));
					
					if(Integer.parseInt(weekCount)>0){
						if(MONTH_DATE.equals(NEXT_WEEK_ONE_MONTH)){
							//下个周一所在月为计划月
							if(StringUtil.notNull(week1)){
								if(Integer.parseInt(weekCount)>=1){
									if(Integer.parseInt(NEXT_WEEK_ONE_NUM) <=1){
										if(Constant.IF_TYPE_NO.toString().equals(W1)){
											if(Integer.parseInt(week1)<0){
												errList.add("第" + rowNum + "行，第一周拆分数量不能小于0，请修改!<br>");
									            return;
											}
											if(Integer.parseInt(week1)%Integer.parseInt(BUY_MIN_PKG)==0){
												dtl.setWeekOne(Integer.parseInt(week1));
											}else{
												errList.add("第" + rowNum + "行，第一周拆分数量不是最小包装量整数倍，请修改!<br>");
									            return;
											}
										}
									}
								}
							}
							
							if(StringUtil.notNull(week2)){
								if(Integer.parseInt(weekCount)>=2){
									if(Integer.parseInt(NEXT_WEEK_ONE_NUM) <=2){
										if(Constant.IF_TYPE_NO.toString().equals(W2)){
											if(Integer.parseInt(week2)<0){
												errList.add("第" + rowNum + "行，第二周拆分数量不能小于0，请修改!<br>");
									            return;
											}
											if(Integer.parseInt(week2)%Integer.parseInt(BUY_MIN_PKG)==0){
												dtl.setWeekTow(Integer.parseInt(week2));
											}else{
												errList.add("第" + rowNum + "行，第二周拆分数量不是最小包装量整数倍，请修改!<br>");
									            return;
											}
										}
									}
								}
							}
							
							if(StringUtil.notNull(week3)){
								if(Integer.parseInt(weekCount)>=3){
									if(Integer.parseInt(NEXT_WEEK_ONE_NUM) <=3){
										if(Constant.IF_TYPE_NO.toString().equals(W3)){
											if(Integer.parseInt(week3)<0){
												errList.add("第" + rowNum + "行，第三周拆分数量不能小于0，请修改!<br>");
									            return;
											}
											if(Integer.parseInt(week3)%Integer.parseInt(BUY_MIN_PKG)==0){
												dtl.setWeekThree(Integer.parseInt(week3));
											}else{
												errList.add("第" + rowNum + "行，第三周拆分数量不是最小包装量整数倍，请修改!<br>");
									            return;
											}
										}
									}
								}
							}
							
							if(StringUtil.notNull(week4)){
								if(Integer.parseInt(weekCount)>=4){
									if(Integer.parseInt(NEXT_WEEK_ONE_NUM) <=4){
										if(Constant.IF_TYPE_NO.toString().equals(W4)){
											if(Integer.parseInt(week4)<0){
												errList.add("第" + rowNum + "行，第四周拆分数量不能小于0，请修改!<br>");
									            return;
											}
											if(Integer.parseInt(week4)%Integer.parseInt(BUY_MIN_PKG)==0){
												dtl.setWeekFour(Integer.parseInt(week4));
											}else{
												errList.add("第" + rowNum + "行，第四周拆分数量不是最小包装量整数倍，请修改!<br>");
									            return;
											}
										}
									}
								}
							}
							
							if(StringUtil.notNull(week5)){
								if(Integer.parseInt(weekCount)>=5){
									if(Integer.parseInt(NEXT_WEEK_ONE_NUM) <=5){
										if(Constant.IF_TYPE_NO.toString().equals(W5)){
											if(Integer.parseInt(week5)<0){
												errList.add("第" + rowNum + "行，第五周拆分数量不能小于0，请修改!<br>");
									            return;
											}
											if(Integer.parseInt(week5)%Integer.parseInt(BUY_MIN_PKG)==0){
												dtl.setWeekFive(Integer.parseInt(week5));
											}else{
												errList.add("第" + rowNum + "行，第五周拆分数量不是最小包装量整数倍，请修改!<br>");
									            return;
											}
										}
									}
								}
							}
							
							if(StringUtil.notNull(remark)){
								if(remark.length()>100){
									remark.substring(0, 100);
								}
								dtl.setSalesRemark(remark);
							}
							
							//验证是否存在重复数据
					        for (int i = 0; i < list.size(); i++) {
								TtPartPlanScrollDelPO dtemp = (TtPartPlanScrollDelPO) list.get(i);
								if(dtemp.getId().equals(dtl.getId())){
									//同一个配件已经存在
									errList.add("第"+rowNum+"行，与其他行数据,重复！<br>");
									return;
								}
							}
							
					        //效验是否大于计划室确认数量
					        Integer rsNum = this.toPaseInt(dtl.getWeekOne());
					        rsNum = rsNum + this.toPaseInt(dtl.getWeekTow());
					        rsNum = rsNum + this.toPaseInt(dtl.getWeekThree());
					        rsNum = rsNum + this.toPaseInt(dtl.getWeekFour());
					        rsNum = rsNum + this.toPaseInt(dtl.getWeekFive());
					        Integer lastNum = Integer.parseInt(LAST_CONFIRM_NUM);
					        if(rsNum>lastNum){
					        	errList.add("第"+rowNum+"行，拆分的数量和大于计划室确认数量！<br>");
								return;
					        }
					        
					        list.add(dtl); 
					        
						}else{
							errList.add("第" + rowNum + "行，计划已超期，不能修改!<br>");
				            return;
						}
					}
					
				}else{
					errList.add("第" + rowNum + "行，唯一标识对应配件不存在,请修改后再上传!<br>");
		            return;
				}
			}else{
				errList.add("第" + rowNum + "行，唯一标识对应配件不存在,请修改后再上传!<br>");
	            return;
			}
		}else{
			errList.add("第" + rowNum + "行，唯一标识不能为空,请修改后再上传!<br>");
            return;
		}
		
	}
	
	private Integer toPaseInt(Object obj){
		try{
			Integer rs = Integer.parseInt(obj.toString());
			return rs;
		}catch(Exception e){
			return 0;
		}
	}
	
	
	/**
	 * 查询配件信息
	 */
	@SuppressWarnings("unchecked")
	public void getPartsByPartOldcode(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String partOldcode = request.getParamValue("PART_OLDCODE");
			if(StringUtil.notNull(partOldcode)){
				partOldcode = partOldcode.trim().toUpperCase();
				String planId = request.getParamValue("planId");
				
				TtPartDefinePO tpd = new TtPartDefinePO();
				tpd.setPartOldcode(partOldcode);
				
				List<TtPartDefinePO> tpdList = dao.select(tpd);
				if(tpdList!=null && !tpdList.isEmpty() && tpdList.size()>0){
					//验证该配件在计划中是否存在
					TtPartPlanScrollDelPO dtl = new TtPartPlanScrollDelPO();
					dtl.setPlanId(Long.valueOf(planId));
					dtl.setPartId(tpdList.get(0).getPartId());
					List<TtPartPlanScrollDelPO> dtlList = dao.select(dtl);
					if(dtlList!=null && !dtlList.isEmpty() && dtlList.size()>0){
						act.setOutData("error", "计划中已存在该配件，无法新增！");
					}else{
						act.setOutData("success", "1");
						act.setOutData("PART_ID", tpdList.get(0).getPartId());
						act.setOutData("PART_OLDCODE", tpdList.get(0).getPartOldcode());
						act.setOutData("PART_CNAME", tpdList.get(0).getPartCname());
						act.setOutData("PART_CODE", tpdList.get(0).getPartCode());
						act.setOutData("MIN_PKG", tpdList.get(0).getBuyMinPkg());
					}
				}else{
					act.setOutData("error", "输入的配件编码不存在，无法新增！");
				}
			}else{
				act.setOutData("error", "请输入配件编码！");
			}
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询配件信息异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 添加计划配件明细
	 */
	@SuppressWarnings("unchecked")
	public void addPlanByPlanId(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String PART_ID = request.getParamValue("PART_ID");
			String planId = request.getParamValue("planId");
			String PLAN_QTY = request.getParamValue("PLAN_QTY");
			String remark = request.getParamValue("remark");
			if(StringUtil.notNull(remark)){
				if(remark.length()>150){
					remark = remark.substring(0, 150);
				}
			}
			//验证配件是否存在
			TtPartDefinePO tpd = new TtPartDefinePO();
			tpd.setPartId(Long.valueOf(PART_ID));
			
			List<TtPartDefinePO> tpdList = dao.select(tpd);
			if(tpdList!=null && !tpdList.isEmpty() && tpdList.size()>0){
				//验证计划配件是否存在
				TtPartPlanScrollDelPO dtl = new TtPartPlanScrollDelPO();
				dtl.setPlanId(Long.valueOf(planId));
				dtl.setPartId(tpdList.get(0).getPartId());
				List<TtPartPlanScrollDelPO> dtlList = dao.select(dtl);
				if(dtlList!=null && !dtlList.isEmpty() && dtlList.size()>0){
					act.setOutData("error", "计划中已存在该配件，无法新增！");
				}else{
					//验证数量最小包装量的整数倍
					if(Long.valueOf(PLAN_QTY)%tpdList.get(0).getBuyMinPkg()==0){
						TtPartPlanScrollDelPO dtlT = new TtPartPlanScrollDelPO();
						dtlT.setPlanId(Long.valueOf(planId));
						
						TtPartPlanScrollDelPO planPo = (TtPartPlanScrollDelPO) dao.select(dtlT).get(0);
						
						TtPartPlanScrollDelPO dtlx = new TtPartPlanScrollDelPO();
						dtlx.setId(Long.valueOf(SequenceManager.getSequence("")));
						dtlx.setPlanId(planPo.getPlanId());
						dtlx.setPlanNo(planPo.getPlanNo());
						dtlx.setPlanOrgId(loginUser.getOrgId());
						dtlx.setPartOldcode(tpdList.get(0).getPartOldcode());
						dtlx.setPartId(tpdList.get(0).getPartId());
						dtlx.setPlanMonthOne(Integer.parseInt(PLAN_QTY));
						dtlx.setSalesRemark(remark);
						dtlx.setCreateBy(loginUser.getUserId());
						dtlx.setCreateDate(new Date());
						dtlx.setPlanTypes(planPo.getPlanTypes());
						dtlx.setMonthDate(planPo.getMonthDate());
						dtlx.setCheckNum(Integer.parseInt(PLAN_QTY));
						//dtlx.setOrderPeriod(tpdList.get(0).getOrderPeriod());
						dtlx.setWhId(tpdList.get(0).getWhId());
						
						dao.insert(dtlx);
						
						//写入配件ID，收货库房，供应商，订单周期 ，修改总成件状态
						dao.impExcelUpdatePart(planId);
						//把计划中重复的配件和成一个件，最后查询视图把错误标识打上
						dao.delErrPlan(planPo.getPlanId().toString());
						
						act.setOutData("success", "新增成功！");
					}else{
						act.setOutData("error", "计划数量不是最小包装量的整数倍，无法新增！");
					}
				}
			}else{
				act.setOutData("error", "输入的配件编码不存在，无法新增！");
			}
			
			
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "添加计划配件明细异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 删除计划明细
	 */
	@SuppressWarnings("unchecked")
	public void deletePlanByPlineId(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String[] cks = request.getParamValues("ck");
			if(cks!=null && cks.length>0){
				for (int i = 0; i < cks.length; i++) {
					String linid = cks[i];
					TtPartPlanScrollDelPO del = new TtPartPlanScrollDelPO();
					del.setId(Long.valueOf(linid));
					dao.delete(del);
				}
				
				//判断最后还有数据没，如果没有数据，把主表信息也删除了
				String planId = request.getParamValue("planId");
				TtPartPlanScrollDelPO dtl = new TtPartPlanScrollDelPO();
				dtl.setPlanId(Long.valueOf(planId));
				List<TtPartPlanScrollDelPO> planList = dao.select(dtl);
				if(planList==null || planList.isEmpty() || planList.size()==0){
					//明细已经全部删除，主表也没有存在的必要
					TtPartPlanScrollPO po = new TtPartPlanScrollPO();
					po.setId(Long.valueOf(planId));
					dao.delete(po);
					act.setOutData("success", "0");
				}else{
					act.setOutData("success", "1");
				}
				
			}else{
				act.setOutData("error", "无需要删除的数据！");
			}
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "删除计划明细异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 审核通过并生成采购订单
	 */
	public void shPlanByPlineId(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//更新计划状态
			String planId = request.getParamValue("planId");
			TtPartPlanScrollPO po = new TtPartPlanScrollPO();
			po.setId(Long.valueOf(planId));
			TtPartPlanScrollPO poValue = new TtPartPlanScrollPO();
			poValue.setStatus(1);
			poValue.setShBy(loginUser.getUserId());
			poValue.setShDate(new Date());
			dao.update(po,poValue);
			//查询无供应商
			List<Map<String,Object>> vList = dao.allVenderIn(planId);
			if(vList!=null && !vList.isEmpty() && vList.size()>0){
				String err = "";
				for (Map<String, Object> map : vList) {
					String partOldcode = CommonUtils.checkNull(map.get("PART_OLDCODE"));
					err+= "配件["+partOldcode+"]无供应商，请维护！<br>";
				}
				if(StringUtil.notNull(err)){
					throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
				}
			}
			//调用存储过程，计划明细转订单
            List<Object> ins = new LinkedList<Object>();
            ins.add(0, planId);
            System.out.println("planId:"+planId);
            dao.callProcedure("PROC_TT_PART_CREATE_ORDER", ins, null);
			
			act.setOutData("success", "1");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "审核计划异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	public void shPlanByPlineId1(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//更新计划状态
			String planId = request.getParamValue("planId");
			TtPartPlanScrollPO po = new TtPartPlanScrollPO();
			po.setId(Long.valueOf(planId));
			TtPartPlanScrollPO poValue = new TtPartPlanScrollPO();
			poValue.setStatus(1);
			poValue.setShBy(loginUser.getUserId());
			poValue.setShDate(new Date());
			dao.update(po,poValue);
			
			
			
			act.setOutData("success", "1");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "审核计划异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 保存明细
	 */
	@SuppressWarnings("unchecked")
	public void savePlanByPlineId(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String[] cks = request.getParamValues("ck");
			if(cks!=null && cks.length>0){
				for (int i = 0; i < cks.length; i++) {
					String linid = cks[i];
					String PLAN_QTY = request.getParamValue("PLAN_QTY_"+linid);
					String MIN_PKG = request.getParamValue("MIN_PKG_"+linid);
					String SALES_REMARK = request.getParamValue("SALES_REMARK_"+linid);
					if(StringUtil.notNull(SALES_REMARK)){
						if(SALES_REMARK.length()>150){
							SALES_REMARK = SALES_REMARK.substring(0, 150);
						}
					}
					if(Integer.parseInt(PLAN_QTY)%Integer.parseInt(MIN_PKG)==0){
						TtPartPlanScrollDelPO dtl = new TtPartPlanScrollDelPO();
						dtl.setId(Long.valueOf(linid));
						TtPartPlanScrollDelPO dtlx = new TtPartPlanScrollDelPO();
						dtlx.setPlanNum(Integer.parseInt(PLAN_QTY));
						dtlx.setUpdateBy(loginUser.getUserId());
						dtlx.setUpdateDate(new Date());
						dtlx.setSalesRemark(SALES_REMARK);
						dao.update(dtl, dtlx);
						act.setOutData("success", "修改保存成功！");
					}else{
						act.setOutData("error", "第"+(i+1)+"行，计划量不是最小包装量的整数倍！");
					}
				}
			}else{
				act.setOutData("error", "无需要修改的数据！");
			}
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存明细异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 导出计划
	 */
	public void expPlanById(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
		String PLAN_TYPES = request.getParamValue("PLAN_TYPES");
		try{
			List<Map<String, Object>> listRs = dao.getPlanInfoById(request, loginUser);
			List<String> listHead = new ArrayList<String>();//导出模板第一列
            //标题
            listHead.add("配件编码");
            listHead.add("计划数量");
            listHead.add("配件备注");
            
            List<String> listKey = new ArrayList<String>();
            listKey.add("PART_OLDCODE");
            listKey.add("PLAN_MONTH_ONE");
            listKey.add("SALES_REMARK");
            
			// 导出的文件名
            String fileName = "计划编制"+DateUtil.format(new Date(), "yyyy_MM_dd")+".xls";
            // 分页导出
            pagingExportExcel(response,fileName,listHead,listKey,listRs,50000);
            
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"导出计划异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser,e1);
			act.setException(e1);
			if(Constant.PART_PURCHASE_PLAN_TYPE_01.toString().equals(PLAN_TYPES)){
				this.toRollingPlanInit();
			}else{
				this.toSupplementPlanInit();
			}
		}
	}
	
	
	
	/**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-27
     * @Title :
     * @Description: 导出错误明细
     */
    
	/*public void exportErrorInfoExcel() {
    	ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
		String PLAN_TYPES = request.getParamValue("PLAN_TYPES");
        try {
        	List<Map<String, Object>> listRs = dao.getPlanErrBy(request);
			List<String> listHead = new ArrayList<String>();//导出模板第一列
            //标题
            listHead.add("序号");
            listHead.add("配件编码");
            listHead.add("失败原因");
            
            List<String> listKey = new ArrayList<String>();
            listKey.add("XH");
            listKey.add("PART_OLDCODE");
            listKey.add("INFO");
            
			// 导出的文件名
            String fileName = "错误信息"+DateUtil.format(new Date(), "yyyy_MM_dd")+".xls";
            // 分页导出
            pagingExportExcel(response,fileName,listHead,listKey,listRs,50000);

        } catch (Exception e) {
        	BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"导出错误明细异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser,e1);
			act.setException(e1);
			if(Constant.PART_PURCHASE_PLAN_TYPE_01.toString().equals(PLAN_TYPES)){
				this.toRollingPlanInit();
			}else{
				this.toSupplementPlanInit();
			}
        }
    }
    */
    
    /**
     * 制造物流出供应商计划导出
     */
    public void expZXWLC(){
    	ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
		String SUPERIOR_PURCHASING_SELECT = request.getParamValue("SUPERIOR_PURCHASING_SELECT");
        try {
        	List<Map<String, Object>> listRs = dao.getPartPlanValidationInfo(request, loginUser, 1, Constant.PAGE_SIZE_MAX).getRecords();
			List<String> listHead = new ArrayList<String>();//导出模板第一列
			//标题
            listHead.add("计划单号");
            listHead.add("计划月份");
            listHead.add("配件编码");
            listHead.add("配件名称");
            listHead.add("配件件号");
            listHead.add("配件类型");
            listHead.add("转单周期");
            listHead.add("最小包装量");
            listHead.add("计划一月");
            
            List<String> listKey = new ArrayList<String>();
            listKey.add("PLAN_NO");
            listKey.add("MONTH_DATE");
            listKey.add("PART_OLDCODE");
            listKey.add("PART_CNAME");
            listKey.add("PART_CODE");
            listKey.add("PART_TYPE_CN");
            listKey.add("ORDER_PERIOD_CN");
            listKey.add("BUY_MIN_PKG");
            listKey.add("PLAN_MONTH_ONE");
            
			// 导出的文件名
            String fileName = "计划编制"+DateUtil.format(new Date(), "yyyy_MM_dd")+".xls";
            // 分页导出
            pagingExportExcel(response,fileName,listHead,listKey,listRs,50000);

        } catch (Exception e) {
        	BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"导出异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser,e1);
			act.setException(e1);
			if(Constant.PURCHASE_TYPE_01.toString().equals(SUPERIOR_PURCHASING_SELECT)){
				//涂装
				this.toPartPlanValidationQxc();
			}else if(Constant.PURCHASE_TYPE_02.toString().equals(SUPERIOR_PURCHASING_SELECT)){
				//专用
				this.toPartPlanValidationZyc();
			}else if(Constant.PURCHASE_TYPE_03.toString().equals(SUPERIOR_PURCHASING_SELECT)){
				//供应商
				this.toPartPlanValidationGys();
			}else if(Constant.PURCHASE_TYPE_04.toString().equals(SUPERIOR_PURCHASING_SELECT)){
				//总装车间
				this.toPartPlanValidationZzcj();
			}
        }
    }
    /**
     * 新增
     */
    public void addPurchasePlan() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();

        try {
            String plan_type = req.getParamValue("PLAN_TYPES");
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = dao.getWareHouse(logonUser.getUserId());
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            String planCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_01);//获取单据编码
            req.setAttribute("wareHouseList", wareHouseList);
            req.setAttribute("name", logonUser.getName());
            req.setAttribute("now", now);
            req.setAttribute("planCode", planCode);
            req.setAttribute("beginTime2", 1);
            if (plan_type.equals(Constant.PART_PURCHASE_PLAN_TYPE_04.toString())) {
                act.setForword(toPlanSettingAddUrl);
            } else {
                act.setForword(toPlanSettingAddUrl2);
            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "计划新增失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(toPlanSettingAddUrl);
        }
    }
    /**
     * 采购送货明细导出
     */
    public void expVenderPirntExl(){
    	ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
        try {
        	List<Map<String, Object>> listRs = dao.getPurOrderVenderPrintInfo(request, loginUser, 1, Constant.PAGE_SIZE_MAX).getRecords();
			List<String> listHead = new ArrayList<String>();//导出模板第一列
			//标题
            listHead.add("订单单号");
            listHead.add("配件编码");
            listHead.add("配件名称");
            listHead.add("配件件号");
            listHead.add("采购数量");
            listHead.add("未入库数量");
            listHead.add("已入库数量");
            listHead.add("打印次数");
            listHead.add("打印送货数量");
            listHead.add("订单日期");
            listHead.add("交付期限");
            listHead.add("库房");
            listHead.add("单位");
            listHead.add("供应商");
            listHead.add("上级单位");
            listHead.add("中储名称");
            listHead.add("配件类型");
            listHead.add("状态");
            listHead.add("计划名称");
            listHead.add("计划备注");
            
            List<String> listKey = new ArrayList<String>();
            listKey.add("ORDER_CODE");
            listKey.add("PART_OLDCODE");
            listKey.add("PART_CNAME");
            listKey.add("PART_CODE");
            listKey.add("BUY_QTY");
            listKey.add("DIN_QTY");
            listKey.add("IN_QTY");
            listKey.add("PRINT_TIMES");
            listKey.add("PRINT_QTYS");
            listKey.add("CREATE_DATE");
            listKey.add("FORECAST_DATE");
            listKey.add("WH_NAME");
            listKey.add("UNIT");
            listKey.add("VENDER_NAME");
            listKey.add("SUPERIOR_PURCHASING_CN");
            listKey.add("CMST_NAME");
            listKey.add("PART_TYPE_CN");
            listKey.add("STATE_CN");
            listKey.add("PLAN_NAME");
            listKey.add("SALES_REMARK");
            
			// 导出的文件名
            String fileName = "采购送货明细"+DateUtil.format(new Date(), "yyyy_MM_dd")+".xls";
            // 分页导出
            pagingExportExcel(response,fileName,listHead,listKey,listRs,50000);

        } catch (Exception e) {
        	BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"导出异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser,e1);
			act.setException(e1);
			this.toPurOrderVenderPrintInit();
        }
    }
    
}
