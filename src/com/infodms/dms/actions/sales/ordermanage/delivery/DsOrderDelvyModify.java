/**
 * 
 */
package com.infodms.dms.actions.sales.ordermanage.delivery;

import static com.infodms.dms.actions.sales.financemanage.AccountOpera.dmsReleasePrice;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderreport.SpecialNeedConfirm;
import com.infodms.dms.actions.sales.planmanage.QuotaAssign.ResourceReserveQuery;
import com.infodms.dms.base.OrderBase;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.base.OrderDAO;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.common.VerFlagDao;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.DsOrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.DsOrderModifyDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.SpecialNeedDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.CuxErpDeliverPO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmMaterialPricePO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmVhclMaterialGroupRPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TmVsAddressPO;
import com.infodms.dms.po.TmWarehousePO;
import com.infodms.dms.po.TtOrdersDeliverPO;
import com.infodms.dms.po.TtProcInterfaceMonitorPO;
import com.infodms.dms.po.TtVsAccountTypePO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsDsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsOrderResourceReservePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.OrderCode;
import com.infodms.dms.util.businessUtil.ReqLogUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author Administrator
 * 
 */
public class DsOrderDelvyModify {
	private Logger logger = Logger.getLogger(DsOrderDelvyModify.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final DsOrderModifyDao dao = DsOrderModifyDao.getInstance();
	private static final DsOrderDeliveryDao ddDao = DsOrderDeliveryDao.getInstance();
	private static final OrderReportDao orderDao = OrderReportDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	private final AccountBalanceDetailDao accoutDao = AccountBalanceDetailDao.getInstance();

	private final String ORDER_RESOURCE_RESERVE_QUERY_URL = "/jsp/sales/ordermanage/delivery/dsOrderDeliveryModifyQuery.jsp";// 真实发运订单订单修改查询
	private final String ORDER_RESOURCE_RESERVE_DETAIL_QUERY_URL = "/jsp/sales/ordermanage/delivery/dsOrderDeliveryModifyDetail.jsp";//真实发运订单修改明细
	private final String ORDER_RESOURCE_RESERVE_PATCH_NO_QUERY_URL = "/jsp/sales/ordermanage/orderaudit/orderDeliveryModifyPatchNoQuery.jsp";// 订单资源审核批次号选择页

	
	//实际发运申请修改 界面初始化  
	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);

			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new java.util.Date(), oemCompanyId);
			String dateStr = DateTimeUtil.parseDateToDate(new Date());
			act.setOutData("dateStr", dateStr);

			List<Map<String, Object>> dateList = dao.getNastyDateList(oemCompanyId);
			List<Map<String, Object>> areaBusList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			String duty = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			if (Constant.DUTY_TYPE_DEPT.toString().equals(duty)) {
				orgId = logonUser.getParentOrgId();
			}
			// 回显查询条件
			String dealerCode = request.getParamValue("dealerCode"); // 经销商CODE
			String areaId = request.getParamValue("areaId"); // 业务范围ID
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String orderTypeSel = request.getParamValue("orderTypeSel"); // 订单类型
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			String reqStatus = request.getParamValue("reqStatus"); // 状态
			String orgCode = request.getParamValue("orgCode");// 区域代码
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			// 获得是否需要资金检查
			TmBusinessParaPO para = dao.getTmBusinessParaPO(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, oemCompanyId);
			String isCheck = para.getParaValue();
			act.setOutData("dateList", dateList);
			act.setOutData("areaBusList", areaBusList);
			act.setOutData("isCheck", isCheck);
			act.setOutData("orgId", orgId);
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("areaId", areaId);
			act.setOutData("groupCode", groupCode);
			act.setOutData("orderTypeSel", orderTypeSel);
			act.setOutData("orderNo", orderNo);
			act.setOutData("reqStatus", reqStatus);
			act.setOutData("orgCode", orgCode);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setForword(ORDER_RESOURCE_RESERVE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 发运申请单修改查询
	 */
	public void getOrderDeliveryModifyQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String areaId = request.getParamValue("areaId"); // 业务范围ID
			String area = request.getParamValue("area"); // 业务范围IDS
			String dealerCode = request.getParamValue("dealerCode"); // 经销商CODE
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String orderType = request.getParamValue("orderTypeSel"); // 订单类型
			String dlvryReqNo = request.getParamValue("dlvryReqNo"); // 发运申请号码
			String reqStatus = request.getParamValue("reqStatus"); // 状态
			// String orgCode = request.getParamValue("orgCode");// 区域代码
			String orgIdN = request.getParamValue("orgId");// 区域代码
			String beginTime = request.getParamValue("startDate");// 开始日期
			String endTime = request.getParamValue("endDate");// 结束日期

			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String dutyType = logonUser.getDutyType();
			String orgId = "";

			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize"));

			if (Constant.DUTY_TYPE_LARGEREGION.toString().equalsIgnoreCase(dutyType)) {
				orgId = logonUser.getOrgId().toString();
			} else {
				if (!CommonUtils.isNullString(orgIdN)) {
					orgId = orgIdN;
				}
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areaId", areaId);
			map.put("area", area);
			map.put("groupCode", groupCode);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("dlvryReqNo", dlvryReqNo);
			map.put("companyId", companyId.toString());
			map.put("reqStatus", reqStatus);
			// map.put("orgCode", orgCode);
			map.put("dutyType", dutyType);
			map.put("orgId", orgId);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("dealerId", logonUser.getDealerId());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getOrderDeliveryModifyQuery(map, curPage, Integer.parseInt(pageSize));
			act.setOutData("ps", ps);
			act.setOutData("orderType", orderType);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运申请修改查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 真实发运申请修改明细页面
	 */
	public void orderDeliveryModifyDetailQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {

			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();

			String paraSys = CommonDAO.getPara(Constant.CHANA_SYS.toString());

			if (Constant.COMPANY_CODE_JC.equals(paraSys.toUpperCase())) {
				act.setOutData("returnValue", 2);
			} else if (Constant.COMPANY_CODE_CVS.equals(paraSys.toUpperCase())) {
				act.setOutData("returnValue", 1);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！");
			}

			String par = CommonDAO.getPara(Constant.CHG_SINGLE_PRICE.toString());

			act.setOutData("par", par);
			// 订单资源审核保留资源控制
			String parReserve = CommonDAO.getPara(Constant.IS_ALLOW_RESERVE_MORE.toString());

			act.setOutData("parReserve", parReserve);

			String orderId = request.getParamValue("orderId"); // 订单ID
			String todId = request.getParamValue("todId"); // 发运申请修改接收ID
			String reqId = request.getParamValue("reqId"); // 发车申请ID
			String orderYearWeek = request.getParamValue("orderYearWeek"); // 订单年周
			String dealerCode = request.getParamValue("dealerCode"); // 经销商CODE
			String areaId = request.getParamValue("areaId"); // 业务范围ID
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String orderType = request.getParamValue("orderType"); // 订单类型
			String orderTypeSel = request.getParamValue("orderTypeSel");
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			String reqStatus = request.getParamValue("reqStatus"); // 状态
			String orgCode = request.getParamValue("orgCode");// 区域代码
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			String historyCount = request.getParamValue("historyCount");

			Map<String, Object> map = dao.getOrderInfoByReqId(reqId);
			//String areaGet = ((BigDecimal) map.get("AREA_ID")).toString();
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			//areaPO.setAreaId(new Long(areaGet));
			List<PO> areaList = dao.select(areaPO);
			areaPO=(TmBusinessAreaPO) areaList.get(0);
			areaId=areaPO.getAreaId().toString();
			List<Map<String, Object>> list1 = dao.getDeliveryApplyList( logonUser.getDealerId(),reqId);
			//List<Map<String, Object>> list2 = dao.getReqCheckList(reqId);
		//	List<Map<String, Object>> wareHouseList = dao.getWareHouseList(logonUser.getCompanyId().toString(), areaGet);// 仓库列表
			TmBusinessParaPO para = dao.getTmBusinessParaPO(Constant.RESOURCE_RESERVE_CHECK_GENERAL_ORDER_PARA, logonUser.getCompanyId());
			// 获得订单启票最大折扣点
			TmBusinessParaPO para2 = dao.getTmBusinessParaPO(Constant.ORDER_REPORT_MAX_DISCOUNT_RATE_PARA, logonUser.getCompanyId());
			String ratePara = para2.getParaValue();
			//根据orderid查询priceId价格类型
			String priceId="";
			TmDealerPO td =new TmDealerPO();
			td.setDealerId(new Long(logonUser.getDealerId()));
			td=(TmDealerPO) dao.select(td).get(0);
			priceId=td.getPersonCharge();
			
			List<Map<String,Object>> accountlist = ddDao.getDealerAccount(logonUser.getDealerId());
			List<Map<String,Object>> addresslist = ddDao.getDealerAddress(logonUser.getDealerId());
			act.setOutData("priceId", td.getPersonCharge());
			act.setOutData("map", map);
			act.setOutData("orderId", orderId);
			act.setOutData("reqId", reqId);
			act.setOutData("todId", todId);
			act.setOutData("list1", list1);
			//act.setOutData("list2", list2);
			act.setOutData("orderYearWeek", orderYearWeek);
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("areaId", areaId);
			act.setOutData("groupCode", groupCode);
			act.setOutData("orderType", orderType);
			act.setOutData("orderTypeSel", orderTypeSel);
			act.setOutData("orderNo", orderNo);
			act.setOutData("reqStatus", reqStatus);
			act.setOutData("orgCode", orgCode);
			//act.setOutData("wareHouseList", wareHouseList);
			act.setOutData("checkGeneral", para != null ? para.getParaValue() : "0");
			act.setOutData("ratePara", ratePara);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setOutData("historyCount", historyCount);
			act.setOutData("accountlist", accountlist);
			act.setOutData("addresslist",addresslist);

			act.setForword(ORDER_RESOURCE_RESERVE_DETAIL_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 真实发运申请修改功能
	 */
	public void orderResourceModifySave() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long userId = logonUser.getUserId();
		try{
			String reqId=request.getParamValue("reqId"); //真实发运申请id
			String applyAmounts = request.getParamValue("applysAmounts");			//申请发运数量
			String oldApplyAmounts = request.getParamValue("oldApplyAmounts"); //原发运申请数量
			String callAmounts = request.getParamValue("cAmounts");		    //已申请数量
			String materialIds = request.getParamValue("materialIds");		//物料ID
			String detailIds = request.getParamValue("detailIds");
			String singlePrices = request.getParamValue("singlePrices");	//物料单价
			String typeId = request.getParamValue("typeId");				//账户类型ID
			String accountId = request.getParamValue("accountId");			//经销商账户ID
			String availableAmount = request.getParamValue("availableAmount");//经销商账户可用余额
			String freezeAmount = request.getParamValue("freezeAmount");	//经销商账户预扣
			String deliveryType = request.getParamValue("deliveryType");	//发运方式
			String addressId = request.getParamValue("addressId");			//发运地址ID
			String areasIds = request.getParamValue("areaIds");				//业务范围
			String isCheck = request.getParamValue("isCheck");				//资金开关
			String fleetId = request.getParamValue("fleetId");			    //集团客户ID
			String address = request.getParamValue("address");			    //集团客户地址
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));// 价格类型
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason"));// 使用其他价格原因
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver"));// 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));// 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));// 联系电话
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));// 折扣账户
			String reqRemark = CommonUtils.checkNull(request.getParamValue("orderRemark")).trim() ; //备注说明
			String checkRemark = CommonUtils.checkNull(request.getParamValue("checkRemark")).trim() ; //修改说明
			String tmp_license_amount = CommonUtils.checkNull(request.getParamValue("tmp_license_amount")) ; //临牌数量
			String totailAccount = CommonUtils.checkNull(request.getParamValue("totailAccount"));//订单总价
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover"));            //是否代交车
			String orderPriceSums = request.getParamValue("orderPriceSums");   //订单价格合计
			String[] applyAmount = applyAmounts.split(",");					//取得所有数量放在数组中
			String[] oldApplyAmount = oldApplyAmounts.split(",");
			String[] singlePrice = singlePrices.split(",");
			String[] materialId = materialIds.split(",");
			String[] detailId=detailIds.split(",");
			//String[] callAmount = callAmounts.split(",");
			String[] priceListIds=request.getParamValue("priceListIds").split(",");
			String[] orderPriceSum = orderPriceSums.split(",");
			TtVsDsDlvryReqPO tvdrpo = new TtVsDsDlvryReqPO();					//发运申请表
			TtVsDsDlvryReqPO tvdrpoNew = new TtVsDsDlvryReqPO();					//发运申请表
			
			String orderIdflg = "";
			Integer reqTotalAmount = 0;
			Double reqTotalPrice = new Double(0);
			Double reqDiscountPrice = new Double(0);
			
			tvdrpo.setReqId(Long.parseLong(reqId));//设置申请主键
			tvdrpoNew = (TtVsDsDlvryReqPO)dao.select(tvdrpo).get(0);
			tvdrpoNew.setDeliveryType(Integer.valueOf(deliveryType));
			tvdrpoNew.setTmpLicenseAmount(Integer.valueOf(tmp_license_amount));
			tvdrpoNew.setReqRemark(reqRemark+"---"+checkRemark);
		
			dao.update(tvdrpo, tvdrpoNew);
			int linenumber = 1;
			//得到行号：如果detailId[i]的值为空说明是新增的产品，不需要累加
			for (int i = 0, len = detailId.length; i < len; i++) {
				if (!"".equals(detailId[i])) {
					linenumber++;
				}
			}
			
			for(int i=0;i<applyAmount.length;i++){		
				
					if("".equals(detailId[i])){
						TtVsDsDlvryReqDtlPO tvdrdpo = new TtVsDsDlvryReqDtlPO();			//发运申请明细表
						reqTotalAmount = 0;
						reqTotalPrice = new Double(0);
						reqDiscountPrice = new Double(0);
						tvdrdpo.setDetailId(Long.parseLong(SequenceManager.getSequence("")));//设置发运申请明细主键
						tvdrdpo.setMaterialId(Long.parseLong(materialId[i]));
						tvdrdpo.setPriceId(priceListIds[i]==null||"".equals(priceListIds[i])?null:new Long(priceListIds[i]));
						tvdrdpo.setReqId(tvdrpoNew.getReqId());
						tvdrdpo.setReqAmount(Integer.parseInt(applyAmount[i]));
						tvdrdpo.setOldReqAmount(Integer.parseInt(oldApplyAmount[i]));
						tvdrdpo.setDeliveryAmount(Integer.parseInt(applyAmount[i]));
						tvdrdpo.setCreateBy(userId);
						tvdrdpo.setCreateDate(new Date(System.currentTimeMillis()));
						tvdrdpo.setSinglePrice(Double.parseDouble(singlePrice[i]));
						tvdrdpo.setTotalPrice(Double.parseDouble(orderPriceSum[i]));
						tvdrdpo.setVer(0);
						dao.insert(tvdrdpo);//插入真实发运发运申请明细表
						//向接口表中写入记录
						sendDataToErp(tvdrpoNew,tvdrdpo,logonUser,linenumber);
						linenumber++;
					}else{
						TtVsDsDlvryReqDtlPO tvdrdpo = new TtVsDsDlvryReqDtlPO();			//发运申请明细表
						reqTotalAmount = 0;
						reqTotalPrice = new Double(0);
						reqDiscountPrice = new Double(0);
						//tvdrdpo.setDetailId(Long.parseLong(SequenceManager.getSequence("")));//设置发运申请明细主键
						tvdrdpo.setDetailId(new Long(detailId[i]));
						tvdrdpo.setMaterialId(Long.parseLong(materialId[i]));
						tvdrdpo.setPriceId(priceListIds[i]==null||"".equals(priceListIds[i])?null:new Long(priceListIds[i]));
						tvdrdpo.setReqId(tvdrpoNew.getReqId());
						tvdrdpo.setReqAmount(Integer.parseInt(applyAmount[i]));
						tvdrdpo.setOldReqAmount(Integer.parseInt(oldApplyAmount[i]));
						tvdrdpo.setDeliveryAmount(Integer.parseInt(applyAmount[i]));
						tvdrdpo.setCreateBy(userId);
						tvdrdpo.setCreateDate(new Date(System.currentTimeMillis()));
						tvdrdpo.setSinglePrice(Double.parseDouble(singlePrice[i]));
						//tvdrdpo.setTotalPrice(Double.parseDouble(orderPriceSum[i]));
						tvdrdpo.setVer(0);
						TtVsDsDlvryReqDtlPO tvdrd1=new TtVsDsDlvryReqDtlPO();
						tvdrd1.setDetailId(new Long(detailId[i]));
						dao.update(tvdrd1, tvdrdpo);
						updateDataFromErp(tvdrpoNew,tvdrdpo,logonUser,i);
					}
					
				
			}			
			act.setOutData("returnValue", 1);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单发运申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//向接口表中写入记录
	private void sendDataToErp(TtVsDsDlvryReqPO tvdrpo,TtVsDsDlvryReqDtlPO tvdrdpo,AclUserBean logonUser,int lineNumber)throws Exception {
		dao.sendDataToErp(tvdrpo,tvdrdpo, logonUser,lineNumber);
	}
	//向接口表中写入记录
	private void updateDataFromErp(TtVsDsDlvryReqPO tvdrpo,TtVsDsDlvryReqDtlPO tvdrdpo,AclUserBean logonUser,int lineNumber)throws Exception {
		dao.updateDataFromErp(tvdrpo,tvdrdpo, logonUser,lineNumber);
	}
	
	/**
	 * 经销商订单发运申请提报表（CUX_ERP_DELIVER）
	 * 
	 * @param orderDealerId :
	 *            订货方经销商ID （TT_VS_ORDER 表 ORDER_ORG_ID）
	 * @param receiverDealerId :
	 *            收货方经销商ID （TT_VS_DLVRY_REQ 表 RECEIVER）
	 * @param orderTypeName :
	 *            订单类型
	 * @param warehouseId :
	 *            仓库ID
	 * @param reqId :
	 *            发运申请ID
	 * @param todId :
	 * 			  发运申请修改接收ID            
	 */
	private void dmsSendToErp(String modifyRemark,String orderDealerId, String receiverDealerId, String orderTypeName, String warehouseId, String reqId, String todId) {
		logger.info("====*********************DMS -> ERP 对接口表 经销商订单发运申请提报表【CUX_ERP_DELIVER】 下发开始 ************************************* ====");
		try {
			TmWarehousePO wpo = new TmWarehousePO();// 仓库PO
			wpo.setWarehouseId(new Long(warehouseId));
			wpo = (TmWarehousePO) dao.getPoObject(wpo);
			TmDealerPO dpo = dao.getDlrByID(new Long(orderDealerId));// 经销商PO
			TmVsAddressPO apo = dao.getDlrAddressByID(new Long(receiverDealerId));// 经销商地址PO
			TtVsDlvryReqPO repo = new TtVsDlvryReqPO();// 发运申请PO
			repo.setReqId(new Long(reqId));
			repo = (TtVsDlvryReqPO) dao.getPoObject(repo);
			TtVsDlvryReqDtlPO redpo = new TtVsDlvryReqDtlPO();// 发运申请明细PO
			redpo.setReqId(new Long(reqId));
			List list = dao.select(redpo);
			if (list != null && list.size() > 0) {
				if (dpo != null && apo != null && repo != null && !"".equals(todId)) {

					for (int i = 0; i < list.size(); i++) {
						CuxErpDeliverPO cedpo = new CuxErpDeliverPO();// 接口表PO
						
						cedpo.setCustomerNumber(dpo.getDealerCode());// 经销商代码
						cedpo.setCustomerName(dpo.getDealerName());// 经销商名称
						cedpo.setShipToLocation(apo.getAddCode());// 收货地址编号
						// 收货方(RECEIVER)地址编号
						//cedpo.setInvoiceToLocation(dpo.getBillAddress());// 订货方的 开票地址					
						cedpo.setOrderNumber(repo.getDlvryReqNo());// 编号必须为数字类型
						// (申请单编号)
						if("常规订单".equals(orderTypeName)){
							cedpo.setOrderType("1");
						}else if("补充订单".equals(orderTypeName)){
							cedpo.setOrderType("2");
						}else if("特殊订单".equals(orderTypeName)){
							cedpo.setOrderType("3");
						}
//						cedpo.setOrderType(orderTypeName);// 订单类型（文字)
						cedpo.setOrderDate(repo.getReqDate());// 订购日期
						// 经销商发运申请提报日期
//						if (wpo != null) {
//							cedpo.setShipFrom(wpo.getWarehouseName());// 出货工厂
//							// 仓库名称
//						}
						cedpo.setRequestDate(repo.getReqDate());// 请求日期
						// 默认为经销商申请提报日期
						cedpo.setScheduleShipDate(repo.getReqDate());// 计划发运日期
						// 默认为经销商申请提报日期
						if (repo.getFundType() != null) {
							TtVsAccountTypePO tpo = new TtVsAccountTypePO();
							tpo.setTypeId(repo.getFundType());
							tpo = (TtVsAccountTypePO) dao.getPoObject(tpo);
							if (tpo != null) {
//								cedpo.setFundsType(tpo.getTypeCode());// 资金类型在ERP
								cedpo.setFundsType(tpo.getMarkCode());
								// 维护
								// （code）
							}
						}
						cedpo.setPlateNumber(BigDecimal.valueOf(repo.getTmpLicenseAmount()));// 临时牌照的数量
//						cedpo.setStatus("W");// 处理标识“W”

						redpo = (TtVsDlvryReqDtlPO) list.get(i);
						cedpo.setPriceListId(BigDecimal.valueOf(redpo.getPriceId()));// 价目表ID（子表中信息）
						TmVhclMaterialPO mpo = new TmVhclMaterialPO();// 物料PO
						mpo.setMaterialId(redpo.getMaterialId());
						mpo = (TmVhclMaterialPO) dao.getPoObject(mpo);
						if (mpo != null) {
							cedpo.setItemCode(mpo.getMaterialCode());// 整车的物料编码
						}
						cedpo.setRemark(modifyRemark);
						cedpo.setOrderedQuantity(BigDecimal.valueOf(redpo.getReqAmount()));// 申请数量
						cedpo.setCreationDate(new Date());// 创建时间
						if(isExist(redpo.getDetailId())){
							CuxErpDeliverPO selpo = new CuxErpDeliverPO();
							selpo.setSeqId(new BigDecimal(redpo.getDetailId()));
							cedpo.setStatus("W");// 处理标识“W”
							dao.update(selpo, cedpo);
							selpo = null;
						}else{
							String lineNum = this.getLineNumber(repo.getDlvryReqNo());//获取最大行号
							cedpo.setSeqId(new BigDecimal(redpo.getDetailId()));//新增 ： seq_id
							cedpo.setStatus("W");
							cedpo.setLineNumber(new BigDecimal(Integer.parseInt(lineNum) + 1));
							dao.insert(cedpo);
						}
						cedpo = null;
					}
					TtProcInterfaceMonitorPO log = new TtProcInterfaceMonitorPO();
					log.setInterfaceName("DMS->ERP 经销商订单发运申请提报表【CUX_ERP_DELIVER】修改下发");
					log.setProcName("DMS->ERP【CUX_ERP_DELIVER】 MODIFY SEND");
					log.setCount(list.size());
					log.setCreateDate(new Date());
					log.setStatus(new Integer(1));
					dao.insert(log);
					/* 修改‘发运申请修改接收表’ TT_ORDERS_DELIVER  状态制成 5  */
					TtOrdersDeliverPO po = new TtOrdersDeliverPO();
					po.setOrderNo(new Long(todId));
					TtOrdersDeliverPO uppo = new TtOrdersDeliverPO();
					uppo.setStatus("5");
					uppo.setUpdateDate(new Date());
					dao.update(po, uppo);
					logger.info("====*****DMS -> ERP 对接口表 经销商订单发运申请提报表【CUX_ERP_DELIVER】 下发结束,共修改下发【" + list.size() + "】条数据 ***************** ====");
				} else {
					String erro = "";
					if (dpo == null) {
						erro = "经销商信息";
					} else if (apo == null) {
						erro = "经销商地址信息";
					} else if (repo == null) {
						erro = "订单发运申请信息";
					} else if("".equals(todId)){
						erro = "发运申请修改接收表ID";
					} else {
						erro = "未知错误";
					}
					throw new RuntimeException("====*****DMS -> ERP 对接口表 经销商订单发运申请提报表 【CUX_ERP_DELIVER】 修改下发失败了,【" + erro + " 为 NULL】 *** ====");
				}

			} else {
				throw new RuntimeException("====*****DMS -> ERP 对接口表 经销商订单发运申请提报表 【CUX_ERP_DELIVER】 修改下发失败了,【订单发运申请明细信息长度为 0 】 *** ====");
			}
		} catch (NumberFormatException e) {
			// TODO 自动生成 catch 块
			TtProcInterfaceMonitorPO log = new TtProcInterfaceMonitorPO();
			log.setInterfaceName("DMS->ERP 经销商订单发运申请提报表【CUX_ERP_DELIVER】修改下发");
			log.setProcName("DMS->ERP【CUX_ERP_DELIVER】 MODIFY SEND");
			log.setCount(new Integer(0));
			log.setCreateDate(new Date());
			log.setStatus(new Integer(0));
			dao.insert(log);
			e.printStackTrace();
			logger.info("====*****DMS -> ERP 对接口表 经销商订单发运申请提报表【CUX_ERP_DELIVER】 修改下发失败了 ****************************** ====");
		}
	}
	/**
	 * 判断接口表中是否存在发运申请明细
	 * @param seqId
	 * @return
	 */
	private boolean isExist(long seqId){
		boolean flag = false;
		CuxErpDeliverPO po = new CuxErpDeliverPO();
		po.setSeqId(new BigDecimal(seqId));
		List list = dao.select(po);
		if(list != null && list.size() > 0){
			flag = true;
		}
		return flag;
	}
	/**
	 * 接口表查询最大行号
	 * @param reqNo
	 * @return
	 * @throws Exception 
	 */
	private String getLineNumber(String reqNo){
		String lineNo = "";
		try {
			lineNo = dao.getLinNum(reqNo);
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return lineNo;
	}


	/**
	 * 批次号选择（发运申请修改）
	 */
	public void patchNoSelect() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String wareHouseId = CommonUtils.checkNull(request.getParamValue("wareHouseId"));// 仓库id
			String materalId = CommonUtils.checkNull(request.getParamValue("materalId")); // 订单数量
			String batchNo = CommonUtils.checkNull(request.getParamValue("batchNo")); // 物料id
			String amount = CommonUtils.checkNull(request.getParamValue("amount")); // 已配车数量(保留数量)
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 订单类型
			String specialBatchNo = CommonUtils.checkNull(request.getParamValue("specialBatchNo")); // 特殊批次号
			String initNo = CommonUtils.checkNull(request.getParamValue("initNo")); // 初始批次
			String reqAmount = CommonUtils.checkNull(request.getParamValue("reqAmount")); // 申请数量
			String shipNumber = CommonUtils.checkNull(request.getParamValue("shipNumber")); // 已发运数量
			String checkAmount = CommonUtils.checkNull(request.getParamValue("checkAmount")); // 审核数量最终的
			String callAmount = CommonUtils.checkNull(request.getParamValue("callAmount")); // 累计的提报发运数量
			Long companyId = logonUser.getCompanyId();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("wareHouseId", wareHouseId);
			map.put("materalId", materalId);
			map.put("orderType", orderType);
			map.put("specialBatchNo", specialBatchNo);
			map.put("companyId", companyId.toString());

			List<Map<String, Object>> w_List = dao.getPatchNoSelectQuery(map);

			act.setOutData("w_List", w_List);
			act.setOutData("batchNo", batchNo);
			act.setOutData("materalId", materalId);
			act.setOutData("amount", amount);
			act.setOutData("initNo", initNo);
			act.setOutData("reqAmount", reqAmount);
			act.setOutData("shipNumber", shipNumber);
			act.setOutData("checkAmount", checkAmount);
			act.setOutData("callAmount", callAmount);
			act.setOutData("orderType", orderType);
			act.setForword(ORDER_RESOURCE_RESERVE_PATCH_NO_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核批次号选择");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void orderResourceModifyCancel() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reqVer = request.getParamValue("reqVer");// 发运申请VER
			String orderId = request.getParamValue("orderId"); // 订单ID
			String reqId = request.getParamValue("reqId"); // 发运申请ID
			String orderType = request.getParamValue("orderType"); // 订单类型
			String[] reqAmount = request.getParamValues("reqAmount");// 申请数量
			String[] detailId = request.getParamValues("detailId");// 发运申请明细ID
			String[] orderDetailId = request.getParamValues("orderDetailId");// 订单明细ID
			String isBingcai = CommonUtils.checkNull(request.getParamValue("isBingcai")); // 资金类型:1=兵财存货融资;
			// 0=非兵财存货融资

			String returnValue = "1";

			boolean verFlag = true;
			verFlag = VerFlagDao.getVerFlag("TT_VS_DLVRY_REQ", "REQ_ID", reqId, reqVer);
			if (verFlag) {
				// 释放资金
				if (!"1".equals(isBingcai)) {
					dmsReleasePrice(reqId, logonUser.getUserId().toString());
				}

				// 发运申请更新
				TtVsDlvryReqPO tvdrp1 = new TtVsDlvryReqPO();
				tvdrp1.setReqId(new Long(reqId));
				TtVsDlvryReqPO tvdrp2 = new TtVsDlvryReqPO();
				tvdrp2.setReqStatus(Constant.ORDER_REQ_STATUS_07);// 已取消
				tvdrp2.setVer(new Integer(Integer.parseInt(reqVer) + 1));
				tvdrp2.setUpdateBy(logonUser.getUserId());
				tvdrp2.setUpdateDate(new Date(System.currentTimeMillis()));
				dao.update(tvdrp1, tvdrp2);

				// 释放保留资源
				for (int i = 0; i < detailId.length; i++) {
					if (detailId[i] != null && !detailId.equals("")) {
						TtVsOrderResourceReservePO tvorrp1 = new TtVsOrderResourceReservePO();
						tvorrp1.setReqDetailId(new Long(detailId[i]));
						TtVsOrderResourceReservePO tvorrp2 = new TtVsOrderResourceReservePO();
						tvorrp2.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_02);// 已取消
						dao.update(tvorrp1, tvorrp2);

						// 常规订单更新订单明细
						if (Integer.parseInt(orderType) == Constant.ORDER_TYPE_01.intValue()) {
							TtVsOrderDetailPO tvodp = reportDao.getTtSalesOrderDetail(orderDetailId[i]);

							TtVsOrderDetailPO tvodp1 = new TtVsOrderDetailPO();
							tvodp1.setDetailId(tvodp.getDetailId());
							TtVsOrderDetailPO tvodp2 = new TtVsOrderDetailPO();
							tvodp2.setCallAmount(tvodp.getCallAmount().intValue() - Integer.parseInt(reqAmount[i]));
							tvodp2.setUpdateBy(logonUser.getUserId());
							tvodp2.setUpdateDate(new Date(System.currentTimeMillis()));
							dao.update(tvodp1, tvodp2);
						}
					}
				}

				// 非常规订单更新订单
				if (Integer.parseInt(orderType) != Constant.ORDER_TYPE_01.intValue()) {
					TtVsOrderPO tvop1 = new TtVsOrderPO();
					tvop1.setOrderId(new Long(orderId));
					TtVsOrderPO tvop2 = new TtVsOrderPO();
					tvop2.setOrderStatus(Constant.ORDER_STATUS_06);// 已取消
					tvop2.setUpdateBy(logonUser.getUserId());
					tvop2.setUpdateDate(new Date(System.currentTimeMillis()));
					dao.update(tvop1, tvop2);

					OrderBase ob = new OrderBase();
					String oldOrderId = ob.getOldOrderId(orderId);

					if (!CommonUtils.isNullString(oldOrderId)) {
						OrderDAO odao = OrderDAO.getInstance();
						Map<String, String> map = new HashMap<String, String>();
						map.put("orderStatus", Constant.ORDER_STATUS_06.toString());
						map.put("updateBy", logonUser.getUserId().toString());
						map.put("updateDate", (String.valueOf(new Date(System.currentTimeMillis()).getTime())));
						odao.orderModify(oldOrderId, map);
					}

					// add by WHX,2012.09.12
					// ======================================================START
					SpecialNeedDao snDao = SpecialNeedDao.getInstance();
					TtVsOrderPO tvo = snDao.orderQuery(Long.parseLong(orderId));

					int order_Type = tvo.getOrderType();

					if (order_Type == Constant.ORDER_TYPE_03.intValue()) {
						Map<String, String> oMap = new HashMap<String, String>();
						oMap.put("reqId", tvo.getSpecialReqId().toString());
						oMap.put("status", Constant.SPECIAL_NEED_STATUS_08.toString());
						oMap.put("userId", logonUser.getUserId().toString());

						SpecialNeedConfirm snc = new SpecialNeedConfirm();
						snc.spcialReqStatusUpdate(oMap);
					}
					// ======================================================END
				}

				/*
				 * // 释放资金 if (!"1".equals(isBingcai)) { AccountBalanceDetailDao
				 * dao = AccountBalanceDetailDao.getInstance();
				 * dao.releaseAllFreezeAmountByReqId(reqId,
				 * logonUser.getUserId().toString()); }
				 */

				// 向发运申请操作日志表写入日志信息
				ReqLogUtil.creatReqLog(Long.parseLong(reqId), Constant.REQ_LOG_TYPE_09, logonUser.getUserId());
				// 同步结算中心下级经销商订单
				if (Integer.parseInt(orderType) != Constant.ORDER_TYPE_01.intValue()) {
					AccountBalanceDetailDao.getInstance().syncOrderToDFS(Long.valueOf(orderId), true, logonUser.getUserId(), true);
				} else {
					AccountBalanceDetailDao.getInstance().syncReqToDFS(Long.parseLong(reqId), true);
				}
			} else {
				returnValue = "2";
			}
			act.setOutData("returnValue", returnValue);
		} catch (RuntimeException e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.PUTIN_FAILURE_CODE, e.getMessage());
			logger.error(logonUser, e1);
			act.setException(e);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SAVE_FAILURE_CODE, "订单资源审核保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void orderResourceModifyBack() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reqVer = request.getParamValue("reqVer");// 发运申请VER
			String orderId = request.getParamValue("orderId"); // 订单ID
			String reqId = request.getParamValue("reqId"); // 发运申请ID
			String orderType = request.getParamValue("orderType"); // 订单类型
			String[] detailId = request.getParamValues("detailId");// 发运申请明细ID
			String isBingcai = CommonUtils.checkNull(request.getParamValue("isBingcai")); // 资金类型:1=兵财存货融资;
			// 0=非兵财存货融资

			String returnValue = "1";

			boolean verFlag = true;
			verFlag = VerFlagDao.getVerFlag("TT_VS_DLVRY_REQ", "REQ_ID", reqId, reqVer);
			if (verFlag) {
				// 释放资金
				if (!"1".equals(isBingcai)) {
					dmsReleasePrice(reqId, logonUser.getUserId().toString());
				}

				// 发运申请更新
				TtVsDlvryReqPO tvdrp1 = new TtVsDlvryReqPO();
				tvdrp1.setReqId(new Long(reqId));
				TtVsDlvryReqPO tvdrp2 = new TtVsDlvryReqPO();
				tvdrp2.setReqStatus(Constant.ORDER_REQ_STATUS_BH);// 驳回
				tvdrp2.setVer(new Integer(Integer.parseInt(reqVer) + 1));
				tvdrp2.setUpdateBy(logonUser.getUserId());
				tvdrp2.setUpdateDate(new Date(System.currentTimeMillis()));
				dao.update(tvdrp1, tvdrp2);

				// 释放保留资源
				for (int i = 0; i < detailId.length; i++) {
					if (detailId[i] != null && !detailId.equals("")) {
						TtVsOrderResourceReservePO tvorrp1 = new TtVsOrderResourceReservePO();
						tvorrp1.setReqDetailId(new Long(detailId[i]));
						TtVsOrderResourceReservePO tvorrp2 = new TtVsOrderResourceReservePO();
						tvorrp2.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_02);// 已取消
						dao.update(tvorrp1, tvorrp2);
					}
				}

				// 订做车订单
				if (Integer.parseInt(orderType) == Constant.ORDER_TYPE_03.intValue()) {
					TtVsOrderPO tvop1 = new TtVsOrderPO();
					tvop1.setOrderId(new Long(orderId));
					TtVsOrderPO tvop2 = new TtVsOrderPO();
					tvop2.setOrderStatus(Constant.ORDER_STATUS_04);// 驳回
					tvop2.setUpdateBy(logonUser.getUserId());
					tvop2.setUpdateDate(new Date(System.currentTimeMillis()));
					dao.update(tvop1, tvop2);

					OrderBase ob = new OrderBase();
					String oldOrderId = ob.getOldOrderId(orderId);

					if (!CommonUtils.isNullString(oldOrderId)) {
						OrderDAO odao = OrderDAO.getInstance();
						Map<String, String> map = new HashMap<String, String>();
						map.put("orderStatus", Constant.ORDER_STATUS_06.toString());
						map.put("updateBy", logonUser.getUserId().toString());
						map.put("updateDate", (String.valueOf(new Date(System.currentTimeMillis()).getTime())));
						odao.orderModify(oldOrderId, map);
					}

					// add by WHX,2012.09.12
					// ======================================================START
					SpecialNeedDao snDao = SpecialNeedDao.getInstance();
					TtVsOrderPO tvo = snDao.orderQuery(Long.parseLong(orderId));

					int order_Type = tvo.getOrderType();

					if (order_Type == Constant.ORDER_TYPE_03.intValue()) {
						Map<String, String> oMap = new HashMap<String, String>();
						oMap.put("reqId", tvo.getSpecialReqId().toString());
						oMap.put("status", Constant.SPECIAL_NEED_STATUS_08.toString());
						oMap.put("userId", logonUser.getUserId().toString());

						SpecialNeedConfirm snc = new SpecialNeedConfirm();
						snc.spcialReqStatusUpdate(oMap);
					}
					// ======================================================END
				}

				// 向发运申请操作日志表写入日志信息
				ReqLogUtil.creatReqLog(Long.parseLong(reqId), Constant.REQ_LOG_TYPE_BH, logonUser.getUserId());
			} else {
				returnValue = "2";
			}
			act.setOutData("returnValue", returnValue);
		} catch (RuntimeException e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.PUTIN_FAILURE_CODE, e.getMessage());
			logger.error(logonUser, e1);
			act.setException(e);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "订做车订单资源审核驳回");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// ---------------------------------------------by Fengalon
	public BigDecimal getLowJSZXPriceNormal(String orderId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderId", orderId);

		Map<String, Object> theMap = accoutDao.getJSZXNormalPrice(map);

		if (!CommonUtils.isNullMap(theMap)) {
			BigDecimal oldPrice = new BigDecimal(theMap.get("OLDPRICE").toString());

			return oldPrice;
		} else {
			// return -999,for no data exception
			return new BigDecimal("-999");
		}
	}

	public BigDecimal getLowJSZXPriceUrgent(String orderId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderId", orderId);

		Map<String, Object> theMap = accoutDao.getJSZXUrgentPrice(map);

		if (!CommonUtils.isNullMap(theMap)) {
			BigDecimal oldPrice = new BigDecimal(theMap.get("OLDPRICE").toString());

			return oldPrice;
		} else {
			// return -999,for no data exception
			return new BigDecimal("-999");
		}
	}

	public int toComparePrice(BigDecimal newPrice, BigDecimal oldPrice) {
		return newPrice.compareTo(oldPrice);
	}

	public void compareToAction() {
/*		String orderType = request.getParamValue("orderType");
		String orderId = request.getParamValue("orderId");

		String[] materialId = request.getParamValues("materialId");
		String[] reserveAmount = request.getParamValues("reserveAmount");

		BigDecimal newPrice = new BigDecimal("0");

		if (materialId.length == 0) {
			act.setOutData("priComparaFlag", "-2");

			return;
		} else {
			int len = materialId.length;

			String priceId = null;
			Map<String, Object> map = null;

			if (Constant.ORDER_TYPE_01.toString().equals(orderType)) {
				map = accoutDao.getOldNormalPrice(orderId);
			} else if (Constant.ORDER_TYPE_02.toString().equals(orderType)) {
				//map = accoutDao.getOldUrgentPrice(orderId);
			}

			if (!CommonUtils.isNullMap(map)) {
				priceId = map.get("PRICE_ID").toString();
			} else {
				act.setOutData("priComparaFlag", "2");

				return;
			}

//			for (int i = 0; i < len; i++) {
//				BigDecimal thePrice = new BigDecimal(accoutDao.getSingleCountPrice(priceId, materialId[i]).get("SALES_PRICE").toString()).multiply(new BigDecimal(reserveAmount[i]));
//				newPrice = newPrice.add(thePrice);
//			}
		}

		// defind param, for oldPrice being "-999"
		BigDecimal oldFlag = new BigDecimal("-999");

		// 常规订单判断
		if (Constant.ORDER_TYPE_01.toString().equals(orderType)) {
			BigDecimal oldPrice = new BigDecimal("0");

			oldPrice = this.getLowJSZXPriceNormal(orderId);

			if (oldFlag.compareTo(oldPrice) != 0) {
				if (this.toComparePrice(newPrice, oldPrice) == 1) {
					act.setOutData("priComparaFlag", "-1");

					return;
				}
			}
			// 补充订单判断
		} else if (Constant.ORDER_TYPE_02.toString().equals(orderType)) {
			BigDecimal oldPrice = new BigDecimal("0");

			oldPrice = this.getLowJSZXPriceUrgent(orderId);

			if (oldFlag.compareTo(oldPrice) != 0) {
				if (this.toComparePrice(newPrice, oldPrice) == 1) {
					act.setOutData("priComparaFlag", "-1");

					return;
				}
			}
		}
*/
		act.setOutData("priComparaFlag", "1");
	}
	/**
	 * 验证在接口表中是否存在该类型
	 */
	public void checkExists() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String typeId=request.getParamValue("typeId");
				String dealerId=request.getParamValue("dealerId");
				TtVsAccountTypePO tvat=new TtVsAccountTypePO();
				tvat.setTypeId(new Long(typeId));
				tvat=(TtVsAccountTypePO) dao.select(tvat).get(0);
				String typeCode=tvat.getTypeCode();
				Map<String ,Object> map=CommonUtils.getAmounts(dealerId, typeCode);
				act.setOutData("Flag", map);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询视图表中是否存在该资金类型异常！！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//增加产品
	public void addMaterial() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			// 查看日期配置表中当天的记录
			TmDateSetPO dateSet = dao.getTmDateSetPO(new Date(), companyId);
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String checkAmount = CommonUtils.checkNull(request.getParamValue("checkAmount"));
			String callAmount = CommonUtils.checkNull(request.getParamValue("callAmount"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("orderDealerId"));
			//获取价格
			String groupCode="";
			TmVhclMaterialPO tvmp=new TmVhclMaterialPO();
			tvmp.setMaterialCode(materialCode);
			tvmp=(TmVhclMaterialPO) dao.select(tvmp).get(0);
			TmVhclMaterialGroupRPO tmgr=new TmVhclMaterialGroupRPO();
			tmgr.setMaterialId(tvmp.getMaterialId());
			tmgr=(TmVhclMaterialGroupRPO) dao.select(tmgr).get(0);
			TmVhclMaterialGroupPO tvmg=new TmVhclMaterialGroupPO();
			tvmg.setGroupId(tmgr.getGroupId());
			tvmg=(TmVhclMaterialGroupPO) dao.select(tvmg).get(0);
			groupCode=tvmg.getGroupCode();

			
			String priceId = CommonUtils.checkNull(
					request.getParamValue("priceId")).equals("") ? "0"
					: CommonUtils.checkNull(request.getParamValue("priceId"));
			priceId="0";
			String entityCode = CommonUtils.checkNull(request.getParamValue("entityCode"));
			String year = dateSet != null ? dateSet.getSetYear() : "";
			String week = dateSet != null ? dateSet.getSetWeek() : "";
			String dealerCode1 = "";
			String orderDealerId = CommonUtils.checkNull(request.getParamValue("orderDealerId"));
			if(!"".equals(orderDealerId)){
				TmDealerPO dpo = dao.getDlrByID(new Long(orderDealerId));// 经销商PO
				if(dpo != null){
					dealerCode1 = dpo.getDealerCode();
				}
			}
			
			Map<String,Object> maps=new HashMap<String,Object>();
			if(CommonUtils.getMaterialPirce(groupCode, dealerCode1)!=null){
				TmMaterialPricePO tmpp=new TmMaterialPricePO();
				tmpp=CommonUtils.getMaterialPirce(groupCode, dealerCode1);
				maps.put("OPERAND", tmpp.getOperand());
				maps.put("LIST_HEADER_ID", tmpp.getListHeaderId());
			}else{
				maps.put("OPERAND", Constant.MATERIAL_PRICE_MAX+1);
			}			
			
			Map<String, Object> map = dao.getMaterialInfo(materialCode, year,
					week, priceId, companyId.toString(), entityCode,dealerCode1,orderType,orderId,dealerId);
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			int isFlag;
			String isReturn="";
			Calendar cal = Calendar.getInstance(); 
			int month = cal.get(Calendar.MONTH )+1; 
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				isFlag = 1 ;
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				isFlag = 0 ;
				isReturn = orderDao.viewOrderCount(year,week, request.getParamValue("dealerId"), materialCode);
			
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			map.put("CHECK_AMOUNT", checkAmount);
			map.put("CALL_AMOUNT", callAmount);
			map.put("DEALER_ID", dealerId);
			act.setOutData("info", map);
			act.setOutData("infos", maps);
			act.setOutData("isFlag", isFlag);
			act.setOutData("isReturn",isReturn);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
