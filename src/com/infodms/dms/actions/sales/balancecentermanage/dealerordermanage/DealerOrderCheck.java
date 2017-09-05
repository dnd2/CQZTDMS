package com.infodms.dms.actions.sales.balancecentermanage.dealerordermanage;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.financemanage.AccountOpera;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage.DealerOrderCheckDAO;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmpScMatchPO;
import com.infodms.dms.po.TmpScMatchVehiclePO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsAccountTypePO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderCheckPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsScMatchPO;
import com.infodms.dms.po.TtVsVehicleTransferPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetOrderNOUtil;
import com.infodms.dms.util.businessUtil.ReqLogUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import static com.infodms.dms.actions.sales.financemanage.AccountOpera.*;

/**
 * @Title : DealerOrderCheck.java
 * @Package: com.infodms.dms.actions.sales.balancecentermanage.dealerordermanage
 * @Description: 经销商订单审核
 * @date : 2010-6-23
 * @version: V1.0
 */
public class DealerOrderCheck extends BaseDao {
	public Logger logger = Logger.getLogger(DealerOrderCheck.class);
	private ActionContext act = ActionContext.getContext();
	ResponseWrapper response = act.getResponse();
	private static final DealerOrderCheck dao = new DealerOrderCheck();

	public static final DealerOrderCheck getInstance() {
		return dao;
	}

	RequestWrapper request = act.getRequest();
	private final OrderQueryDao orderQueryDao = OrderQueryDao.getInstance();
	private final OrderReportDao orderReportDao = OrderReportDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	private final String dealerOrderCheckInitURL = "/jsp/sales/balancecentermanage/dealerordermanage/dealerOrderCheckInit.jsp";
	private final String dealerOrderInfoURL = "/jsp/sales/balancecentermanage/dealerordermanage/dealerOrderInfo.jsp";
	private final String orderDetailVehicleListURL = "/jsp/sales/balancecentermanage/dealerordermanage/orderDetailVehicleList.jsp";
	private final String toQueryDealerListURL = "/jsp/sales/balancecentermanage/dealerordermanage/toQueryDealerList.jsp";
	private final String dealerOrderCheckInitDo = "/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/dealerOrderCheckInit.do";
	private final String dealerOrderInfoLowerURL = "/jsp/sales/balancecentermanage/dealerordermanage/dealerOrderInfoLower.jsp";
	
	/**
	 * @Title : dealerOrderCheckInit
	 * @Description: 经销商订单审核页面初始化
	 * @throws
	 * @LastUpdate :2010-6-23
	 */
	public void dealerOrderCheckInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
			Long orgId = logonUser.getOrgId();
			List<String> years = orderQueryDao.getYearList();
			List<String> weeks = orderQueryDao.getWeekList();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			Calendar c = Calendar.getInstance();
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(),
					oemCompanyId);


			act.setOutData("orgId", orgId);
			act.setOutData("years", years);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear()
					: "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek()
					: "");
			act.setOutData("areaList", areaList);
			act.setForword(dealerOrderCheckInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单审核页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * @Title : dealerOrderCheckInit_Query
	 * @Description: 经销商订单审核：查询可审核的订单
	 * @throws
	 * @LastUpdate :2010-7-14
	 */
	public void dealerOrderCheckInit_Query() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String dealerId = logonUser.getDealerId();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);//订单周度
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);		  //业务范围
			map.put("groupCode", groupCode);  //物料组
			map.put("orderNo", orderNo);	  //订单号
			map.put("dealerCode", dealerCode);    
			map.put("dealerId", dealerId);    

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = DealerOrderCheckDAO.getDealerOrderCheckInit_Query(map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单审核：查询可审核的订单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * @Title : dealerOrderCheckInit_Query
	 * @Description: 经销商订单审核：查询可审核的订单下载
	 * @throws
	 * @LastUpdate :2010-7-14
	 */
	public void dealerOrderCheckInit_QueryLoad() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String dealerId = logonUser.getDealerId();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);//订单周度
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);		  //业务范围
			map.put("groupCode", groupCode);  //物料组
			map.put("orderNo", orderNo);	  //订单号
			map.put("dealerCode", dealerCode);    
			map.put("dealerId", dealerId);    

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			// 导出的文件名
			String fileName = "下级经销商订单审核.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("订货方");
			listTemp.add("开票方");
			listTemp.add("订单号");
			listTemp.add("提报日期");
			listTemp.add("订单周度");
			listTemp.add("订单类型");
			listTemp.add("订单状态");
			listTemp.add("提报数量");
			listTemp.add("待审核数量");
			list.add(listTemp);

			List<Map<String, Object>> results = DealerOrderCheckDAO.getDealerOrderCheckInit_QueryLoad(map, Constant.PAGE_SIZE, curPage);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("D_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("K_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("RAISE_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_WEEK")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_STATUS")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("NO_CHECK_AMOUNT")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单审核：查询可审核的订单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/** 
	* @Title	  : toCheck 
	* @Description: 查询要审核订单详细信息
	* @param      : 设定文件 
	* @return     : void返回类型 
	* @throws 
	* @LastUpdate :2010-6-23
	*/
	public void toCheck(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			DealerOrderCheckDAO dao=new DealerOrderCheckDAO();
			String order_id = CommonUtils.checkNull(request.getParamValue("order_id"));
			TtVsOrderPO po = dao.getTtSalesOrder(order_id);
//			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> dateList = orderReportDao.getUrgentDateList(oemCompanyId);
			Map dataMap = dateList.get(0);
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			//1.订单信息
			Map<String,Object> orderInfo = DealerOrderCheckDAO.getOrderInfo(order_id);
			String areaId = String.valueOf(orderInfo.get("AREA_ID"));
			String is_fleet = orderInfo.get("IS_FLEET")+"";
			String k_dealer = String.valueOf(orderInfo.get("DEALER_ID"));
			if (null != is_fleet && "1".equals(is_fleet)) {
				Map<String,String> fleetInfo = (Map<String,String>)DealerOrderCheckDAO.getFleetInfo(order_id);
				String fleet_id = String.valueOf(fleetInfo.get("FLEET_ID"));
				String fleet_name = String.valueOf(fleetInfo.get("FLEET_NAME"));
				String address = String.valueOf(fleetInfo.get("ADDRESS"));
				
				act.setOutData("fleet_id", fleet_id);
				act.setOutData("fleet_name", fleet_name);
				act.setOutData("address", address);
			}
			//2.订单明细信息
			List orderDetailList = DealerOrderCheckDAO.getOrderDetailList(order_id, logonUser.getDealerId()+"");
			//3.订单审核日志信息
			List checkHisList =  DealerOrderCheckDAO.getCheckHisList(order_id);
			
			//查看是否有上级经销商:isHasParent==-1 说明没有上级经销商，否则有
			String isHasParent = String.valueOf(DealerOrderCheckDAO.checkParentDealer(logonUser.getDealerId(),areaId).get("PARENT_DEALER_D"));
			
			if(null != isHasParent && !"".equals(isHasParent) && !"-1".equals(isHasParent)){
				act.setOutData("orderInfo", orderInfo);
				act.setOutData("orderDetailList", orderDetailList);
				act.setOutData("checkHisList", checkHisList);
				act.setOutData("areaId", areaId);
				act.setForword(dealerOrderInfoLowerURL);
			}else{
				Long poseId = logonUser.getPoseId();
				List<Map<String, Object>> areaList = MaterialGroupManagerDao
						.getDealerBusiness(poseId.toString());// 业务范围列表
				//List<Map<String, Object>> fundTypeList = orderReportDao.getFundTypeList();// 资金类型列表
				List<Map<String, Object>> fundTypeList = AccountBalanceDetailDao.getInstance().getNoDiscountAccountInfoByDealerId(k_dealer);
	
				// 获得是否需要资金检查
				TmBusinessParaPO para = orderReportDao.getTmBusinessParaPO(
						Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, oemCompanyId);
				String isCheck = para.getParaValue();
				
				// 获得订单启票最大折扣点
				TmBusinessParaPO para2 = orderReportDao.getTmBusinessParaPO(
						Constant.ORDER_REPORT_MAX_DISCOUNT_RATE_PARA, oemCompanyId);
				String ratePara = para2.getParaValue();
				
				act.setOutData("areaList", areaList);// 业务范围列表
				act.setOutData("fundTypeList", fundTypeList);// 资金类型列表
				act.setOutData("isCheck", isCheck);
				
				act.setOutData("orderInfo", orderInfo);
				act.setOutData("orderDetailList", orderDetailList);
				act.setOutData("checkHisList", checkHisList);
				act.setOutData("areaId", areaId);
				act.setOutData("order", po);
				act.setOutData("ratePara", ratePara);
				act.setOutData("wareFlag", OrderReportDao.getInstance().getWarehouseFlag(order_id)) ;
				act.setForword(dealerOrderInfoURL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单审核：查询要审核订单详细信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询审核订单详细信息
	 * */
	public void toCheck__(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String order_id = CommonUtils.checkNull(request.getParamValue("order_id"));
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> dateList = orderReportDao.getUrgentDateList(oemCompanyId);
			Map dataMap = dateList.get(0);
			
			//2.订单明细信息
			//List orderDetailList__ = DealerOrderCheckDAO.getOrderDetailList_PageResult(order_id, logonUser.getDealerId()+"");
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = DealerOrderCheckDAO.getOrderDetailList_PageResult(order_id, logonUser.getDealerId()+"",Constant.PAGE_SIZE_MAX, curPage);
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单审核：查询要审核订单详细信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : toCheckDetail 
	* @Description: 审核订单明细
	* @throws 
	* @LastUpdate :2010-6-24
	*/
	public void toCheckDetail(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String detail_id = CommonUtils.checkNull(request.getParamValue("detail_id"));			    //订单明细id
			String order_amount = CommonUtils.checkNull(request.getParamValue("order_amount"));         //订单数量
			String material_id = CommonUtils.checkNull(request.getParamValue("material_id"));			//物料id
			String group_id = CommonUtils.checkNull(request.getParamValue("group_id")); 				//物料组id
			String series_id = CommonUtils.checkNull(request.getParamValue("series_id")); 				//车系id
			String order_id = CommonUtils.checkNull(request.getParamValue("order_id")); 				//订单id
			
			Long companyId = logonUser.getCompanyId();
			List<Map<String,Object>> w_List = DealerOrderCheckDAO.getDealerWarehouseList(companyId);
			
			act.setOutData("w_List", w_List);
			act.setOutData("detail_id", detail_id);
			act.setOutData("order_id", order_id);
			act.setOutData("order_amount", order_amount);
			act.setOutData("material_id", material_id);
			act.setOutData("group_id", group_id);
			act.setOutData("series_id", series_id);
			act.setForword(orderDetailVehicleListURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单审核：查询要审核订单详细信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : getVehicleList 
	* @Description: 结算中心管理：经销商订单审核--车辆资源列表
	* @param      : 设定文件 
	* @return     : void返回类型 
	* @throws 
	* @LastUpdate :2010-6-24
	*/
	public void getVehicleList(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String warehouse_id = CommonUtils.checkNull(request.getParamValue("warehouse_id"));
			String material_id = CommonUtils.checkNull(request.getParamValue("material_id"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					
			PageResult<Map<String, Object>> ps = DealerOrderCheckDAO.getVehicleList(logonUser.getDealerId(), warehouse_id,material_id ,Constant.PAGE_SIZE_MAX, curPage);
			act.setOutData("ps", ps);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算中心管理：经销商订单审核--车辆资源列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : orderDetailCheck 
	* @Description: 结算中心：经销商订单审核--审核订单明细
	*               向TMP_SC_MATCH(配车临时表)及TMP_SC_MATCH_VEHICLE (配车临时车辆表)写入车辆信息
	* @param      : 设定文件 
	* @return     : void返回类型 
	* @throws 
	* @LastUpdate :2010-6-25
	*/
	public void orderDetailCheck(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String order_id = CommonUtils.checkNull(request.getParamValue("order_id"));			//订单id
			String material_id = CommonUtils.checkNull(request.getParamValue("material_id"));	//物料id
			String vehicleid = CommonUtils.checkNull(request.getParamValue("vehicleid"));
			String[] vehicleids = vehicleid.split(",");
			int check_Number = vehicleids.length;
			TmpScMatchPO old_MatchPO = new TmpScMatchPO();
			old_MatchPO.setOrderId(Long.parseLong(order_id));
			old_MatchPO.setMaterialId(Long.parseLong(material_id));
			List oldList = dao.select(old_MatchPO);
			Long tmp_match_id = new Long(0);
			if (null != oldList && oldList.size()>0) {
				for (int i = 0; i < oldList.size(); i++) {
					TmpScMatchPO rPO = (TmpScMatchPO)oldList.get(0);
					tmp_match_id = rPO.getTmpMatchId();
					TmpScMatchVehiclePO old_MatchVehiclePO = new TmpScMatchVehiclePO();
					old_MatchVehiclePO.setTmpMatchId(tmp_match_id);
					dao.delete(old_MatchVehiclePO);
					
					TmpScMatchPO d_TmpScMatchPO = new TmpScMatchPO();
					d_TmpScMatchPO.setTmpMatchId(tmp_match_id);
					dao.delete(d_TmpScMatchPO);
				}
			}
			if (null != vehicleids && vehicleids.length>0 && !"".equals(vehicleids[0])) {
				
				TmpScMatchPO matchPO = new TmpScMatchPO();
				Long tmpMatchId = Long.parseLong(SequenceManager.getSequence(""));
				matchPO.setTmpMatchId(tmpMatchId);
				matchPO.setOrderId(Long.parseLong(order_id));
				matchPO.setMaterialId(Long.parseLong(material_id));
				matchPO.setMatchNumber(check_Number);
				dao.insert(matchPO);
			
				for (int i = 0; i < check_Number; i++) {
					if (null != vehicleids[i] && !"".equals(vehicleids[i]) ) {
						TmpScMatchVehiclePO matchVehiclePO = new TmpScMatchVehiclePO();
						matchVehiclePO.setTmpVehicleId(Long.parseLong(SequenceManager.getSequence("")));
						matchVehiclePO.setVehicleId(Long.parseLong(vehicleids[i]));
						matchVehiclePO.setTmpMatchId(tmpMatchId);
						dao.insert(matchVehiclePO);
					}
				}
			}
			act.setOutData("returnValue", 1);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算中心：经销商订单审核--审核订单明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : checkSubmitAction 
	* @Description: 结算中心：经销商订单审核：提交 
	* @throws 
	* @LastUpdate :2010-6-25 
	* @Refactor By WangLiang @101118
	*/
	public void checkSubmitAction(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER); /////
		try {
			
			String order_id = CommonUtils.checkNull(request.getParamValue("order_id"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNO"));
			String check_desc = CommonUtils.checkNull(request.getParamValue("check_desc"));
			String[] detail_id = request.getParamValues("detail_id");										//订单明细id
			String[] material_id = request.getParamValues("material_id");									//物料id
			String d_dealerId = CommonUtils.checkNull(request.getParamValue("d_dealerId"));					//订货方
			String k_dealerId = CommonUtils.checkNull(request.getParamValue("k_dealerId"));					//开票方
			String caigouMoneyAll = CommonUtils.checkNull(request.getParamValue("totalPrice_"));    		//采购总价
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover"));						//是否代交车
			String totalDiscountPrice = CommonUtils.checkNull(request.getParamValue("totalDiscountPrice"));	//折扣总额
			String fundType = CommonUtils.checkNull(request.getParamValue("fundTypeId")); 					// 选择资金类型
			String isBingcai = CommonUtils.checkNull(request.getParamValue("isBingcai"));//资金类型：1=兵财，0=非兵财
			String[] buyNO = request.getParamValues("buyNO");
			//查看是否有上级经销商:isHasParent==-1 说明没有上级经销商，否则有
			String isHasParent = String.valueOf(DealerOrderCheckDAO.checkParentDealer(logonUser.getDealerId(),areaId).get("PARENT_DEALER_D"));		
			String caigouAllNumber = CommonUtils.checkNull(request.getParamValue("caigouAllNumber"));//采购总数量
			
			boolean flag = false ;
			
			/*1.1 结算中心(一级经销商)向车厂进行采购时*/
			if (null != caigouAllNumber && !"0".equals(caigouAllNumber) && !"".equals(caigouAllNumber)) {
				
				//得到 系统可变参数,判断是否在此校验资金
				TmBusinessParaPO para = orderReportDao.getTmBusinessParaPO(
						Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, GetOemcompanyId.getOemCompanyId(logonUser));
				String isCheck = para.getParaValue();
				
				/*//判断结算中心资金账户是否足够
				if(!isMoneyEnough(logonUser, k_dealerId,caigouMoneyAll, fundType, isBingcai ,isCheck)){
					act.setOutData("returnValue", 2);
					return;
				}*/
				
				String caigouOrderId = SequenceManager.getSequence("");				
				String orderNO_ = generateOrderNo(k_dealerId, areaId);	
				//创建结算中心采购订单
				Map orderDetailIdMap = createOrder(totalDiscountPrice,caigouOrderId,caigouMoneyAll,areaId,orderNO_);
				
				
				//为了支持三级经销商,当使用此功能的经销商还有上级经销商时不会写发运申请表
				if (null != isHasParent && "-1".equals(isHasParent)) {
					
					//-----------------写入发运申请表-----------------
					Map<String, String> infoMap = composeParamsMap(caigouMoneyAll, areaId, caigouOrderId);
					
					Long req_id = Long.parseLong(SequenceManager.getSequence(""));
					//String dlvryReqNO = GetOrderNOUtil.getDlvryReqNO(orderNO_);
					String dlvryReqNO = orderNO_;
					urgentDlvryReq(infoMap, req_id, isCover,dlvryReqNO,logonUser.getUserId());
					
					String[] applyedAmount = request.getParamValues("applyedAmount");
					//String[] buyNO = request.getParamValues("buyNO");
					String[] singlePrice = request.getParamValues("singlePrice");
					String[] acountPrices_ = request.getParamValues("acountPrices_");
					String[] discount_rate = request.getParamValues("discount_rate");
					String[] discount_price = request.getParamValues("discount_price");
					String[] materialId = request.getParamValues("materialId");
					if(orderDetailIdMap == null){
						BizException e1 = new BizException(act, "", ErrorCodeConstant.QUERY_FAILURE_CODE, "发运申请明显数据异常");
						throw  e1;
					}
					for (int i = 0; i < buyNO.length; i++) {
						if (null != buyNO[i] && !"".equals(buyNO[i]) && !"0".equals(buyNO[i])) {
							Long newDetailId = (Long) orderDetailIdMap.get(Long.parseLong(material_id[i]));
							urgentDlvryReqDetail(req_id+"", newDetailId+"", material_id[i], "", buyNO[i], singlePrice[i], acountPrices_[i],discount_rate[i],request.getParamValue("discount_s_price"+materialId[i]), discount_price[i],logonUser.getUserId());
							
							//更新TtVsOrderDetailPO的审核数量：CHECK_AMOUNT;非库存车满足时,实现将变更信息发送到dfs. date:2011-11-28 by:WangHanxian
							updateDlrOrderCheckAmount(order_id, detail_id[i], material_id[i],buyNO[i]);
						}
					}
					
					//-----------------写入发运申请表完毕-----------------
					
					//冻结结算中心(一级经销商)账户资金
					syncBillDealerAccountFreeze(logonUser, k_dealerId,caigouMoneyAll, areaId, totalDiscountPrice,
							fundType, isBingcai, isCheck, req_id);
					//向发运申请操作日志表写入日志信息
					ReqLogUtil.creatReqLog(req_id, Constant.REQ_LOG_TYPE_01, logonUser.getUserId());
					
					
				}
				
				flag = true ;
			} 
			
			/*1.2 结算中心(一级经销商)用库存车满足时*/
			for (int i = 0; i < material_id.length; i++) {
				List<Map<String,Object>> vehicleList = DealerOrderCheckDAO.getTmpVehicleList(order_id, material_id[i],logonUser.getDealerId());
				
				if (null != vehicleList && vehicleList.size()>0) {
					//向结算中心配车表写入车辆信息
					for (int j = 0; j < vehicleList.size(); j++) {
						TtVsScMatchPO matchPO = new TtVsScMatchPO();
						matchPO.setMatchId(Long.parseLong(SequenceManager.getSequence("")));
						matchPO.setOrderDetailId(Long.parseLong(detail_id[i]));
						matchPO.setVehicleId(Long.parseLong(String.valueOf(vehicleList.get(j).get("VEHICLE_ID"))));
						matchPO.setCreateBy(logonUser.getUserId());
						matchPO.setCreateDate(new Date());
						dao.insert(matchPO);
						
						//自动调拨
						TtVsVehicleTransferPO transferPO = new TtVsVehicleTransferPO();
						String transferId = SequenceManager.getSequence("");
						String transferNo = "PF"+transferId;
						transferPO.setTransferId(Long.parseLong(transferId));
						transferPO.setVehicleId(Long.parseLong(String.valueOf(vehicleList.get(j).get("VEHICLE_ID"))));
						transferPO.setOutDealerId(Long.parseLong(String.valueOf(vehicleList.get(j).get("DEALER_ID"))));
						transferPO.setTransferDate(new Date());
						transferPO.setCheckStatus(Constant.DISPATCH_STATUS_03);
						transferPO.setInDealerId(Long.parseLong(d_dealerId));
						transferPO.setTransferReason("库存车满足下级订单:\""+orderNO+" \"");
						transferPO.setCreateBy(logonUser.getUserId());
						transferPO.setCreateDate(new Date());
						transferPO.setTransferNo(transferNo);
						dao.insert(transferPO);
						
						//调拨后，修改车辆新的dealerId
						TmVehiclePO tempVehiclePO = new TmVehiclePO();
						tempVehiclePO.setVehicleId(Long.parseLong(String.valueOf(vehicleList.get(j).get("VEHICLE_ID"))));
						TmVehiclePO valueVehiclePO = new TmVehiclePO();
						valueVehiclePO.setDealerId(Long.parseLong(d_dealerId));
						dao.update(tempVehiclePO, valueVehiclePO);
//						List<Object> ins = new LinkedList<Object>();
//						ins.add(transferId);
						//dao.callProcedure("p_vehicle_transferdata_to_erp", ins, null);
					
				}

				
				//更新TtVsOrderDetailPO的审核数量：CHECK_AMOUNT
				updateDlrOrderCheckAmount(order_id, detail_id[i], material_id[i],buyNO[i]);
				
				flag = true ;
			}
		}
			
			//modify by WHX,2012.09.21
			//======================================================START
			//使冻结资金时，依据一致
			
			//modify by WHX,2012.09.22
			//======================================================START
			//资金冻结
			String wareFlag = CommonUtils.checkNull(request.getParamValue("wareFlag")) ;
			if("1".equals(wareFlag)) {
				//======================================================END
				Map<String, String> map = new HashMap<String, String>() ;
				
				map.put("orderId", order_id) ;
				map.put("userId", logonUser.getUserId().toString()) ;
				Long lReqId = this.reqInsert(map) ;
				
				map.put("reqId", lReqId.toString()) ;
				this.reqDtlInsert(map) ;
				
				//modify by WHX,2012.09.22
				//======================================================START
				//资金冻结
				map.put("fundClass", Constant.ACCOUNT_CLASS_CSGC.toString()) ;
				this.storePriceFreezeOfJSZX(map) ;
				//======================================================END
			}
			//======================================================END
			
			if(flag) {
				/*if(null != isHasParent && "-1".equals(isHasParent)){
					//更新订单状态：审核通过
					TtVsOrderPO tempOrderPO = new TtVsOrderPO();
					tempOrderPO.setOrderId(Long.parseLong(order_id));
					TtVsOrderPO valueOrderPO = new TtVsOrderPO();
					valueOrderPO.setOrderStatus(Constant.ORDER_STATUS_05);
					dao.update(tempOrderPO, valueOrderPO);
				}*/
				
				//更新订单状态：审核通过
				updateDealerOrderStatus(order_id);
				
				//同步下级经销商订单
				AccountBalanceDetailDao.getInstance().
					syncOrderToDFS(Long.parseLong(order_id), false, logonUser.getUserId(), false);
				
				//新增审核日志信息
				createOrderCheckRecord(logonUser, order_id, check_desc);
	
				if(null == caigouAllNumber || "0".equals(caigouAllNumber) || "".equals(caigouAllNumber)){
					List<Object> ins = new LinkedList<Object>();
					ins.add(order_id);
					dao.callProcedure("p_transferdata_to_erp_js", ins, null);
				}
				
				//clean tmp
				cleanScTmp(order_id);
				//act.setForword(dealerOrderCheckInitDo);
			} else {
				act.setOutData("returnValue", 1);
			}
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算中心：经销商订单审核：提交");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 清除为二级经销商订单配车表的记录
	 * @param order_id
	 */
	private void cleanScTmp(String order_id) {
		TmpScMatchPO tempTmpPO = new TmpScMatchPO();
		tempTmpPO.setOrderId(Long.parseLong(order_id));
		dao.delete(tempTmpPO);
	}
	
	/**
	 * 创建订单审核记录
	 * @param logonUser
	 * @param order_id
	 * @param check_desc
	 */
	private void createOrderCheckRecord(AclUserBean logonUser, String order_id,
			String check_desc) {
		TtVsOrderCheckPO checkPO = new TtVsOrderCheckPO();
		checkPO.setCheckId(Long.parseLong(SequenceManager.getSequence("")));//审核ID
		checkPO.setOrderId(Long.parseLong(order_id)); 						//订单ID
		checkPO.setCheckOrgId(logonUser.getOrgId());						//审核组织
		checkPO.setCheckPositionId(logonUser.getPoseId());					//审核职位
		checkPO.setCheckUserId(logonUser.getUserId());						//审核人员
		checkPO.setCheckDate(new Date());									//审核日期
		checkPO.setCheckStatus(Constant.ORDER_STATUS_05);					//审核状态
		if (null != check_desc && !"".equals(check_desc)) {
			checkPO.setCheckDesc(check_desc.trim());						//审核描述
		}
		checkPO.setCreateBy(logonUser.getUserId());
		checkPO.setCreateDate(new Date());
		dao.insert(checkPO);
	}
	/**
	 * 更新下级经销商订单的状态为已提报
	 * @param order_id
	 */
	private void updateDealerOrderStatus(String order_id) {
		TtVsOrderPO tempOrderPO = new TtVsOrderPO();
		tempOrderPO.setOrderId(Long.parseLong(order_id));
		TtVsOrderPO valueOrderPO = new TtVsOrderPO();
		valueOrderPO.setOrderStatus(Constant.ORDER_STATUS_05);
		dao.update(tempOrderPO, valueOrderPO);
	}
	/**
	 * 更新下级经销商订单的CHECK_AMOUNT
	 * 当库存车满足时还应更新发运数量
	 * @param order_id
	 * @param detail_id
	 * @param material_id
	 * @param buyNO
	 * @param i
	 */
	private void updateDlrOrderCheckAmount(String order_id, String detail_id,
			String material_id, String buyNO) {
		TtVsOrderDetailPO tempDetailPO = new TtVsOrderDetailPO();
		tempDetailPO.setDetailId(Long.parseLong(detail_id));
		List detailList = dao.select(tempDetailPO);
		
		TtVsOrderDetailPO nowrderDetailPO = (TtVsOrderDetailPO) detailList.get(0);
		
		TtVsOrderDetailPO valueDetailPO = new TtVsOrderDetailPO();
		if(buyNO != null && !buyNO.equals("")){
			valueDetailPO.setCheckAmount(new Integer(buyNO));
		}
		else{
			TmpScMatchPO tmpScMatchPO = new TmpScMatchPO();
			tmpScMatchPO.setOrderId(Long.parseLong(order_id));
			tmpScMatchPO.setMaterialId(Long.parseLong(material_id));
			List tList = dao.select(tmpScMatchPO);
			int tmpMatchNumer = 0;
			if (null != tList && tList.size()>0) {
				tmpMatchNumer = ((TmpScMatchPO)tList.get(0)).getMatchNumber();
			}
			valueDetailPO.setCheckAmount(nowrderDetailPO.getCheckAmount() + tmpMatchNumer);
			//库存车满足时应在原始订单中写入"发运数量" Modify By WangLiang @101120
			valueDetailPO.setDeliveryAmount(nowrderDetailPO.getCheckAmount() + tmpMatchNumer);
			valueDetailPO.setMatchAmount(nowrderDetailPO.getCheckAmount() + tmpMatchNumer);
		}
		dao.update(tempDetailPO, valueDetailPO);
	}
	/**
	 * 冻结结算中心的账户金额
	 * @param logonUser
	 * @param k_dealerId
	 * @param caigouMoneyAll
	 * @param areaId
	 * @param totalDiscountPrice
	 * @param fundType
	 * @param isBingcai
	 * @param isCheck
	 * @param req_id
	 */
	private void syncBillDealerAccountFreeze(AclUserBean logonUser,
			String k_dealerId, String caigouMoneyAll, String areaId,
			String totalDiscountPrice, String fundType, String isBingcai,
			String isCheck, Long req_id) {
		String parentDealerId = "";
		Map<String,String> parInfo = DealerOrderCheckDAO.checkParentDealer(k_dealerId + "", areaId);
		if (null != parInfo && parInfo.size()>0) {
			parentDealerId = String.valueOf(DealerOrderCheckDAO.checkParentDealer(k_dealerId + "", areaId).get("PARENT_DEALER_D"));
		}else{
			parentDealerId = "-1";
		}

		if (isCheck.equals("0") && "-1".equals(parentDealerId) && !"1".equals(isBingcai)) {
			TtVsAccountPO accountPO = new TtVsAccountPO();
			//String k_dealerId = CommonUtils.checkNull(request.getParamValue("k_dealerId"));
			accountPO.setDealerId(Long.parseLong(k_dealerId));
			accountPO.setAccountTypeId(Long.parseLong(fundType));
			accountPO = orderReportDao.geTtVsAccountPO(accountPO);
			
			dmsFreezePrice_Report(req_id+"", accountPO.getAccountId()+"", new BigDecimal(caigouMoneyAll), logonUser.getUserId()+"");
			// 扣折扣账户
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));
			if (!discountAccountId.equals("")) {
				dmsFreezePrice_Report(req_id+"", discountAccountId, new BigDecimal(totalDiscountPrice), logonUser.getUserId().toString());
			}
			/*TtVsAccountPO accountPO = new TtVsAccountPO();
			//String k_dealerId = CommonUtils.checkNull(request.getParamValue("k_dealerId"));
			accountPO.setDealerId(Long.parseLong(k_dealerId));
			accountPO.setAccountTypeId(Long.parseLong(fundType));
			accountPO = orderReportDao.geTtVsAccountPO(accountPO);
			
			AccountBalanceDetailDao abdo = AccountBalanceDetailDao.getInstance();
			abdo.syncAccountFreeze(req_id+"", accountPO.getAccountId()+"", new BigDecimal(caigouMoneyAll), logonUser.getUserId()+"");
			
			// 扣折扣账户
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));
			if (!discountAccountId.equals("")) {
				abdo.syncAccountFreeze(req_id+"", discountAccountId, new BigDecimal(totalDiscountPrice), logonUser.getUserId().toString());
			}*/
		}
	}
	/**
	 * 检查结算中心资金账户是否足够
	 * @param logonUser
	 * @param k_dealerId
	 * @param caigouMoneyAll
	 * @param fundType
	 * @param isBingcai
	 * @return
	 */
	private boolean isMoneyEnough(AclUserBean logonUser, String k_dealerId,
			String caigouMoneyAll, String fundType, String isBingcai,String isCheck) {

		if (isCheck.equals("0") &&  !"1".equals(isBingcai)) {//若在提报时校验资金，并且付款方式不是兵财
			TtVsAccountPO accountPO = new TtVsAccountPO();
			accountPO.setDealerId(Long.parseLong(k_dealerId));
			accountPO.setAccountTypeId(Long.parseLong(fundType));
			accountPO = orderReportDao.geTtVsAccountPO(accountPO);
			if (accountPO != null) {
				if (accountPO.getAvailableAmount().doubleValue() < Double.parseDouble(caigouMoneyAll)) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}
	/**
	 * 生成订单号
	 * @param k_dealerId 开票方
	 * @param areaId  业务范围
	 * @return
	 */
	private String generateOrderNo(String k_dealerId, String areaId) {
		TmDealerPO tmDealerPO = new TmDealerPO();
		tmDealerPO.setDealerId(new Long(k_dealerId));
		List dealerList = dao.select(tmDealerPO);
		String dealerCode = ((TmDealerPO)dealerList.get(0)).getDealerCode();
		Map<String,String> codeMap  = GetOrderNOUtil.getAreaShortcode(areaId);
		String areaCode = codeMap.get("AREA_SHORTCODE");
		String orderNO_ = GetOrderNOUtil.getOrderNO(areaCode,Constant.ORDER_TYPE_02+"", "D", dealerCode);
		return orderNO_;
	}
	/**
	 * @param caigouMoneyAll
	 * @param areaId
	 * @param caigouOrderId
	 * @return
	 */
	private Map<String, String> composeParamsMap(String caigouMoneyAll,
			String areaId, String caigouOrderId) {
		Map<String,String> infoMap = new HashMap<String, String>();
		infoMap.put("area_id", areaId);
		infoMap.put("order_id", caigouOrderId);
		
		if (null != request.getParamValue("orderRemark") && !"".equals(request.getParamValue("orderRemark").trim())) {
			infoMap.put("reqRemark", request.getParamValue("orderRemark").trim());
		}
		
		if (null != request.getParamValue("fundTypeId") && !"".equals(request.getParamValue("fundTypeId"))) {
			infoMap.put("fund_type", request.getParamValue("fundTypeId"));
		}
		
		String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType"));
		if (!"".equals(deliveryType)) {
			infoMap.put("delivery_type", deliveryType);
		}
		String deliveryAddress = CommonUtils.checkNull(request.getParamValue("deliveryAddress"));
		if (!"".equals(deliveryAddress)) {
			infoMap.put("address_id", deliveryAddress);
		}
		String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));
		if (!"".equals(fleetId)) {
			infoMap.put("fleet_id", fleetId);
		}
		String fleetAddress = CommonUtils.checkNull(request.getParamValue("fleetAddress"));
		if (!"".equals(fleetAddress)) {
			infoMap.put("fleet_address", fleetAddress);
		}
		String caigouAllNumbers = request.getParamValue("caigouAllNumber");
		if (null != caigouAllNumbers && !"".equals(caigouAllNumbers)) {
			infoMap.put("req_total_amount", caigouAllNumbers);
		}
		
		String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));
		if (!"".equals(priceId)) {
			infoMap.put("price_id", priceId);
		}
		String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason"));
		if (!"".equals(otherPriceReason)) {
			infoMap.put("other_price_reason", otherPriceReason);
		}
		infoMap.put("receiver", request.getParamValue("receiver"));
		String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));
		if (!"".equals(linkMan)) {
			infoMap.put("link_man", linkMan);
		}
		String tel = CommonUtils.checkNull(request.getParamValue("tel"));
		if (!"".equals(tel)) {
			infoMap.put("tel", tel);
		}
		if (!"".equals(caigouMoneyAll)) {
			infoMap.put("total", caigouMoneyAll);
		}
		return infoMap;
	}
	
	/** 
	* @Title	  : toQueryDealerList 
	* @Description: 查询下属经销商
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void toQueryDealerList(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(toQueryDealerListURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询下属经销商");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : queryDealerList 
	* @Description: 查询下属经销商结果展示
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void queryDealerList(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = logonUser.getDealerId();
			String dealerCode =  CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String dealerName =  CommonUtils.checkNull(request.getParamValue("dealerName"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					
			PageResult<Map<String, Object>> ps = DealerOrderCheckDAO.getDealerList(dealerId, dealerCode, dealerName,  Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询下属经销商结果展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//生成采购单
	public Map<Long,Long> createOrder(String totalDiscountPrice, String caigouOrderId, String caigouMoneyAll, String areaId, String orderNO_) {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//		try {
			Integer orgType = logonUser.getOrgType();
			String dealerId = CommonUtils.checkNull(request.getParamValue("d_dealerId"));
			String k_dealerId = CommonUtils.checkNull(request.getParamValue("k_dealerId"));
			String order_id = CommonUtils.checkNull(request.getParamValue("order_id"));
			TmDealerPO dealerPO = orderReportDao.getTmDealer(dealerId);

			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = orderReportDao.getTmDateSetPO(new Date(), oemCompanyId);
			int year = Integer.parseInt(dateSet.getSetYear());
			int week = Integer.parseInt(dateSet.getSetWeek());

			String fundType = CommonUtils.checkNull(request.getParamValue("fundTypeId")); 				// 选择资金类型
			String totalPrice = CommonUtils.checkNull(request.getParamValue("totalPrice_")); 			// 总价
			String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType")); 		// 运送方式
			String deliveryAddress = CommonUtils.checkNull(request.getParamValue("deliveryAddress")); 	// 运送地点
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId")); 					// 价格类型
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason")); // 使用其他价格原因
			String discount = CommonUtils.checkNull(request.getParamValue("discount")); 				// 使用折让
			String receiver = CommonUtils.checkNull(request.getParamValue("d_dealerId")); 				// 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan")); 					// 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel")); 							// 联系电话
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover"));
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));
			String fleetAddress = CommonUtils.checkNull(request.getParamValue("fleetAddress"));
			String orderRemark = CommonUtils.checkNull(request.getParamValue("orderRemark"));//

			// 得到当前月份
			TmDateSetPO datePO = new TmDateSetPO();
			datePO.setSetYear(year + "");
			datePO.setSetWeek(week + "");
			datePO = orderReportDao.geTmDateSetPO(datePO);
			Integer month = datePO != null ? new Integer(datePO.getSetMonth()) : null;

			Map<Long,Long> map = new HashMap<Long, Long>();
			// 查看是否有上级经销商:parentDealerId==-1 说明没有上级经销商，否则有
			String parentDealerId = "";
			Map<String,String> parInfo = DealerOrderCheckDAO.checkParentDealer(k_dealerId + "", areaId);
			if (null != parInfo && parInfo.size()>0) {
				parentDealerId =  String.valueOf(DealerOrderCheckDAO.checkParentDealer(k_dealerId + "", areaId).get("PARENT_DEALER_D"));
			}else{
				parentDealerId = "-1";
			}
			
			String[] detail_id = request.getParamValues("detail_id"); // 订单明细id
			if ("-1".equals(parentDealerId)) {

				TtVsOrderPO po = new TtVsOrderPO();
				po.setOrderId(new Long(caigouOrderId));
				po.setCompanyId(GetOemcompanyId.getOemCompanyId(logonUser));
				po.setAreaId(new Long(areaId));
				po.setOrderNo(orderNO_);
				po.setOrderType(Constant.ORDER_TYPE_02);
				po.setIsRefitOrder(0);
				po.setOrderYear(new Integer(year));
				po.setOrderMonth(month);
				po.setOrderWeek(new Integer(week));
				po.setOrderOrgType(orgType);
				po.setOrderOrgId(Long.parseLong(dealerId));	// 定货方
				po.setBillingOrgType(new Long(orgType));
				po.setBillingOrgId(new Long(k_dealerId));	// 开票方

				po.setFundTypeId(new Long(fundType));
				po.setPriceId(priceId);
				if (null != otherPriceReason && !"".equals(otherPriceReason)) {
					po.setOtherPriceReason(otherPriceReason.trim());
				}

				po.setOrderPrice(new Double(totalPrice));
				if("1".equals(isCover)){
					po.setIsFleet(1);
					po.setFleetId(new Long(fleetId));
					po.setFleetAddress(fleetAddress);
				}
				else{
					po.setIsFleet(0);
					po.setDeliveryType(new Integer(deliveryType));
					if(Constant.TRANSPORT_TYPE_01.toString().equals(deliveryType)) {
						
					} else {
						po.setReceiver(new Long(receiver));
						po.setDeliveryAddress(new Long(deliveryAddress));
					}
				}
				po.setLinkMan(linkMan);
				po.setTel(tel);
				po.setOrderStatus(Constant.ORDER_STATUS_03);
				po.setVer(new Integer(0));
				po.setCreateDate(new Date());
				po.setCreateBy(logonUser.getUserId());
				po.setDiscount(new Double(totalDiscountPrice));
				po.setRaiseDate(new Date());
				po.setOrderRemark(orderRemark);
				po.setOldOrderId(Long.parseLong(order_id));
				dao.insert(po);

				String[] material_ids = request.getParamValues("material_id"); 				// 物料ID
				String[] buyNO = request.getParamValues("buyNO"); 							// 提报数量
				String[] singlePrice = request.getParamValues("singlePrice"); 				// 物料单价
				
				for (int i = 0; i < buyNO.length; i++) {
					if (null != buyNO[i] && !"".equals(buyNO[i]) && !"0".equals(buyNO[i])) {
						TtVsOrderDetailPO detailPO = new TtVsOrderDetailPO();
						detailPO.setDetailId(new Long(SequenceManager.getSequence(""))); 	// 明细ID
						detailPO.setOrderId(po.getOrderId()); 								// 订单ID
						detailPO.setMaterialId(Long.parseLong(material_ids[i])); 			// 物料ID
						detailPO.setOrderAmount(Integer.parseInt(buyNO[i])); 				// 提报数量
						detailPO.setSinglePrice(Double.parseDouble(singlePrice[i])); 		// 物料单价
						detailPO.setTotalPrice(Double.parseDouble(buyNO[i]) * Double.parseDouble(singlePrice[i]));// 订单总价
						detailPO.setVer(new Integer(0));
						detailPO.setCreateDate(new Date());
						detailPO.setCreateBy(logonUser.getUserId());
						dao.insert(detailPO);
						
						map.put(detailPO.getMaterialId(), detailPO.getDetailId());
						
						TtVsOrderDetailPO tempPo = new TtVsOrderDetailPO();
						tempPo.setDetailId(Long.parseLong(detail_id[i]));
						List list = dao.select(tempPo);
						if (null != list && list.size() > 0) {
							TtVsOrderDetailPO orderDetailPO = (TtVsOrderDetailPO) list.get(0);
							Integer oldAmount = orderDetailPO.getApplyedAmount();

							TtVsOrderDetailPO valuePo = new TtVsOrderDetailPO();
							valuePo.setApplyedAmount(oldAmount + Integer.parseInt(buyNO[i]));

							dao.update(tempPo, valuePo);

						}
					}
				}

			} else {
				TtVsOrderPO po = new TtVsOrderPO();
				po.setOrderId(new Long(caigouOrderId));
				po.setCompanyId(GetOemcompanyId.getOemCompanyId(logonUser));
				po.setAreaId(new Long(areaId));
				po.setOrderNo(orderNO_);
				po.setOrderType(Constant.ORDER_TYPE_02);
				po.setIsRefitOrder(0);
				po.setOrderYear(new Integer(year));
				po.setOrderMonth(month);
				po.setOrderWeek(new Integer(week));
				po.setOrderOrgType(orgType);
				po.setOrderOrgId(Long.parseLong(dealerId));
				po.setBillingOrgType(new Long(orgType));
				po.setBillingOrgId(new Long(k_dealerId));
				
				po.setDeliveryType(new Integer(deliveryType));
				if(Constant.TRANSPORT_TYPE_01.toString().equals(deliveryType)) {
					
				} else {
					po.setReceiver(new Long(receiver));
					po.setDeliveryAddress(new Long(deliveryAddress));
				}
				po.setLinkMan(linkMan);
				po.setTel(tel);
				po.setOrderStatus(Constant.ORDER_STATUS_02);
				po.setVer(new Integer(0));
				po.setCreateDate(new Date());
				po.setCreateBy(logonUser.getUserId());
				po.setRaiseDate(new Date());
				po.setOldOrderId(Long.parseLong(order_id));
				dao.insert(po);

				String[] material_ids = request.getParamValues("material_id"); 					// 物料ID
				String[] buyNO = request.getParamValues("buyNO"); 								// 提报数量
				for (int i = 0; i < buyNO.length; i++) {
					if (null != buyNO[i] && !"".equals(buyNO[i]) && !"0".equals(buyNO[i])) {
						TtVsOrderDetailPO detailPO = new TtVsOrderDetailPO();
						detailPO.setDetailId(new Long(SequenceManager.getSequence(""))); 		// 明细ID
						detailPO.setOrderId(po.getOrderId()); 									// 订单ID
						detailPO.setMaterialId(Long.parseLong(material_ids[i])); 				// 物料ID
						detailPO.setOrderAmount(Integer.parseInt(buyNO[i])); 					// 提报数量
						detailPO.setVer(new Integer(0));
						detailPO.setCreateDate(new Date());
						detailPO.setCreateBy(logonUser.getUserId());
						dao.insert(detailPO);
						TtVsOrderDetailPO tempPo = new TtVsOrderDetailPO();
						tempPo.setDetailId(Long.parseLong(detail_id[i]));
						List list = dao.select(tempPo);
						if (null != list && list.size() > 0) {
							TtVsOrderDetailPO orderDetailPO = (TtVsOrderDetailPO) list.get(0);
							Integer oldAmount = orderDetailPO.getApplyedAmount();

							TtVsOrderDetailPO valuePo = new TtVsOrderDetailPO();
							valuePo.setApplyedAmount(oldAmount + Integer.parseInt(buyNO[i]));

							dao.update(tempPo, valuePo);
						}
					}
				}
			}
			return map;
		/*} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单生成");
			logger.error(logonUser, e1);
			act.setException(e1);
		}*/
	}
	
	public void checkClose(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String order_id =  CommonUtils.checkNull(request.getParamValue("order_id"));
			
			updateDealerOrderStatus(order_id);
			act.setForword(dealerOrderCheckInitDo);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询下属经销商结果展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商订单审核提交时写入发运申请主表
	 * */
	public void urgentDlvryReq(Map<String,String> infoMap ,Long req_id,String isCover,String dlvryReqNO,Long userId){
		try {
			
			TtVsDlvryReqPO dlvryReqPO = new TtVsDlvryReqPO();
			dlvryReqPO.setDlvryReqNo(dlvryReqNO);											//发运申请单号
			dlvryReqPO.setReqId(req_id);
			dlvryReqPO.setAreaId(Long.parseLong(infoMap.get("area_id")));					//业务范围ID	
			dlvryReqPO.setOrderId(Long.parseLong(infoMap.get("order_id")));					//订单ID
			dlvryReqPO.setReqRemark(infoMap.get("reqRemark")) ;
			if (null != infoMap.get("fund_type") && !"".equals(infoMap.get("fund_type"))) {
				dlvryReqPO.setFundType(Long.parseLong(infoMap.get("fund_type")));			//资金类型
			}
			
			if (null != infoMap.get("delivery_type") && !"".equals(infoMap.get("delivery_type"))) {
				dlvryReqPO.setDeliveryType(Integer.parseInt(infoMap.get("delivery_type"))); //发运方式
				
				if (Constant.TRANSPORT_TYPE_01.toString().equals(infoMap.get("delivery_type"))) {
					
				} else {
					dlvryReqPO.setAddressId(Long.parseLong(infoMap.get("address_id")));			//发运地址ID
					dlvryReqPO.setReceiver(Long.parseLong(infoMap.get("receiver")));				//收货方
				}
			}
			
			/*if (null != infoMap.get("isFleet") && "1".equals(infoMap.get("isFleet"))) {
				dlvryReqPO.setFleetId(Long.parseLong(infoMap.get("fleet_id")));				//集团客户ID
				dlvryReqPO.setFleetAddress(infoMap.get("fleet_address"));					//集团客户地址
			}*/
			
			dlvryReqPO.setReqDate(new Date());												//申请时间
			if (null != infoMap.get("req_total_amount") && !"".equals(infoMap.get("req_total_amount"))) {
				dlvryReqPO.setReqTotalAmount(Integer.parseInt(infoMap.get("req_total_amount")));//申请数量合计
			}
			
			dlvryReqPO.setReqExecStatus(Constant.REQ_EXEC_STATUS_01);						//申请执行状态
			dlvryReqPO.setVer(0);															//版本控制
			if (null != infoMap.get("price_id") && !"".equals(infoMap.get("price_id"))) {
				dlvryReqPO.setPriceId(infoMap.get("price_id"));				//价格类型
			}
			
			if (null != infoMap.get("other_price_reason") && !"".equals(infoMap.get("other_price_reason"))) {
				dlvryReqPO.setOtherPriceReason(infoMap.get("other_price_reason").trim());	//使用其他价格原因
			}
			
			if (null != infoMap.get("link_man") && !"".equals(infoMap.get("link_man"))) {
				dlvryReqPO.setLinkMan(infoMap.get("link_man")); 							//联系人
			}
			if (null != infoMap.get("tel") && !"".equals(infoMap.get("tel"))) {
				dlvryReqPO.setTel(infoMap.get("tel"));										//联系电话
			}
			if (null != infoMap.get("total") && !"".equals(infoMap.get("total"))) {
				dlvryReqPO.setReqTotalPrice(Double.parseDouble(infoMap.get("total")));		//订单总价
			}
			
			if (null != isCover && "1".equals(isCover)) {
				dlvryReqPO.setReqStatus(Constant.ORDER_REQ_STATUS_08);						//发运申请状态
				dlvryReqPO.setIsFleet(1);
				dlvryReqPO.setFleetId(Long.parseLong(infoMap.get("fleet_id")));				//集团客户ID
				dlvryReqPO.setFleetAddress(infoMap.get("fleet_address"));					//集团客户地址
			}else{
				dlvryReqPO.setReqStatus(Constant.ORDER_REQ_STATUS_01);						
			}
			dlvryReqPO.setReqExecStatus(Constant.REQ_EXEC_STATUS_01);
			dlvryReqPO.setCreateBy(userId);								
			dlvryReqPO.setCreateDate(new Date());
			
			dao.insert(dlvryReqPO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, " 经销商订单审核提交时写入发运申请主表");
//			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商订单审核提交时写入发运申请明细表
	 * */
	public void urgentDlvryReqDetail(String reqId,String orderDetailId,String materialId,String patchNo,
			String reqAmount,String singlePrice,String totalPrice,String discountRate,String discountSPrice,String discountPrice,Long userId){
		try {
			Long detail_id = Long.parseLong(SequenceManager.getSequence(""));				
			TtVsDlvryReqDtlPO dlvryReqDtlPO = new TtVsDlvryReqDtlPO();						
			dlvryReqDtlPO.setDetailId(detail_id);											//明细ID
			dlvryReqDtlPO.setReqId(Long.parseLong(reqId));									//申请ID
			dlvryReqDtlPO.setOrderDetailId(Long.parseLong(orderDetailId));					//订单明细ID
			dlvryReqDtlPO.setMaterialId(Long.parseLong(materialId));						//物料ID
			if (null != patchNo && !"".equals(patchNo)) {
				dlvryReqDtlPO.setPatchNo(patchNo);											//批次号
			}
			
			if (null != reqAmount && !"".equals(reqAmount)) {
				dlvryReqDtlPO.setReqAmount(Integer.parseInt(reqAmount));						//申请数量
			}
			
			dlvryReqDtlPO.setVer(0);
			if (null != singlePrice && !"".equals(singlePrice)) {
				dlvryReqDtlPO.setSinglePrice(Double.parseDouble(singlePrice));					//物料单价
				dlvryReqDtlPO.setTotalPrice(Double.parseDouble(totalPrice)); 					//订单总价
			}
			
			if (null != discountRate && !"".equals(discountRate)) {
				dlvryReqDtlPO.setDiscountRate(Float.parseFloat(discountRate.trim()));       //折扣率
			}
			if (null != discountSPrice && !"".equals(discountSPrice)) {
				dlvryReqDtlPO.setDiscountSPrice(Double.parseDouble(discountSPrice.trim()));	//折扣后单价
			}
			if (null != discountPrice && !"".equals(discountPrice)) {
				dlvryReqDtlPO.setDiscountPrice(Double.parseDouble(discountPrice.trim()));	//折扣额
			}
			
			dlvryReqDtlPO.setCreateBy(userId);
			dlvryReqDtlPO.setCreateDate(new Date());
			
			dao.insert(dlvryReqDtlPO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "  经销商订单审核提交时写入发运申请明细表");
//			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 结算中心审核下级订单驳回
	 */
	public void retBack() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try {
			String order_id = CommonUtils.checkNull(request.getParamValue("order_id"));
			String check_desc = CommonUtils.checkNull(request.getParamValue("check_desc"));//
			
			TtVsOrderPO orderA = new TtVsOrderPO() ;
			orderA.setOrderId(Long.parseLong(order_id)) ;
			TtVsOrderPO order = new TtVsOrderPO() ;
			order.setOrderStatus(Constant.ORDER_STATUS_04) ;
			
			dao.update(orderA, order) ;
			
			//同步下级经销商订单
			AccountBalanceDetailDao.getInstance().
				syncOrderToDFS(Long.parseLong(order_id), false, logonUser.getUserId(), true);
			
			//新增审核日志信息
			TtVsOrderCheckPO checkPO = new TtVsOrderCheckPO();
			checkPO.setCheckId(Long.parseLong(SequenceManager.getSequence("")));//审核ID
			checkPO.setOrderId(Long.parseLong(order_id)); 						//订单ID
			checkPO.setCheckOrgId(logonUser.getOrgId());						//审核组织
			checkPO.setCheckPositionId(logonUser.getPoseId());					//审核职位
			checkPO.setCheckUserId(logonUser.getUserId());						//审核人员
			checkPO.setCheckDate(new Date());									//审核日期
			checkPO.setCheckStatus(Constant.ORDER_STATUS_04);					//审核状态
			if (null != check_desc && !"".equals(check_desc)) {
				checkPO.setCheckDesc(check_desc.trim());						//审核描述
			}
			checkPO.setCreateBy(logonUser.getUserId());
			checkPO.setCreateDate(new Date());
			dao.insert(checkPO);
			
			dealerOrderCheckInit() ;
			// act.setForword(dealerOrderCheckInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算中心：经销商订单审核：驳回");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//modify by WHX,2012.09.21
	//======================================================START
	public Long reqInsert(Map<String, String> map) {
		String orderId = map.get("orderId") ;
		String userId = map.get("userId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		Long reqId = Long.parseLong(SequenceManager.getSequence("")) ;
		
		sql.append("insert into tt_vs_dlvry_req\n") ;
		sql.append("  select ").append(reqId).append(",\n") ;
		sql.append("         tvo.area_id,\n") ;
		sql.append("         tvo.order_id,\n") ;
		sql.append("         tvo.fund_type_id,\n") ;
		sql.append("         tvo.delivery_type,\n") ;
		sql.append("         tvo.delivery_address,\n") ;
		sql.append("         sysdate,\n") ;
		sql.append("         (select sum(tvod.check_amount)\n") ;
		sql.append("            from tt_vs_order_detail tvod\n") ;
		sql.append("           where tvod.order_id = tvo.order_id),\n") ;
		sql.append("         ").append(Constant.REQ_EXEC_STATUS_02).append(",\n") ;
		sql.append("         0,\n") ;
		sql.append("         ").append(userId).append(",\n") ;
		sql.append("         sysdate,\n") ;
		sql.append("         '',\n") ;
		sql.append("         '',\n") ;
		sql.append("         tvo.fleet_id,\n") ;
		sql.append("         tvo.fleet_address,\n") ;
		sql.append("         tvo.price_id,\n") ;
		sql.append("         tvo.other_price_reason,\n") ;
		sql.append("         tvo.receiver,\n") ;
		sql.append("         tvo.link_man,\n") ;
		sql.append("         tvo.tel,\n") ;
		sql.append("         tvo.discount,\n") ;
		sql.append("         0,\n") ;
		sql.append("         '',\n") ;
		sql.append("         ").append(Constant.ORDER_REQ_STATUS_06).append(",\n") ;
		sql.append("         tvo.order_price,\n") ;
		sql.append("         0,\n") ;
		sql.append("         tvo.is_fleet,\n") ;
		sql.append("         tvo.order_no,\n") ;
		sql.append("         tvo.order_org_id,\n") ;
		sql.append("         tvo.billing_org_id,\n") ;
		sql.append("         ").append(Constant.DEALER_LEVEL_02).append(",\n") ;
		sql.append("         tvo.order_remark\n") ;
		sql.append("    from tt_vs_order tvo\n") ;
		sql.append("   where tvo.order_id = ").append(orderId).append("\n") ;

		super.insert(sql.toString()) ;
		
		return reqId ;
	}
	
	public void reqDtlInsert(Map<String, String> map) {
		String reqId = map.get("reqId") ;
		String userId = map.get("userId") ;
		String orderId = map.get("orderId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("insert into tt_vs_dlvry_req_dtl\n") ;
		sql.append("  select f_getid,\n") ;
		sql.append("         ").append(reqId).append(",\n") ;
		sql.append("         tvod.detail_id,\n") ;
		sql.append("         tvod.material_id,\n") ;
		sql.append("         '',\n") ;
		sql.append("         tvod.check_amount,\n") ;
		sql.append("         tvod.check_amount,\n") ;
		sql.append("         0,\n") ;
		sql.append("         ").append(userId).append(",\n") ;
		sql.append("         sysdate,\n") ;
		sql.append("         '',\n") ;
		sql.append("         '',\n") ;
		sql.append("         tvod.single_price,\n") ;
		sql.append("         tvod.check_amount * tvod.single_price,\n") ;
		sql.append("         tvod.discount_rate,\n") ;
		sql.append("         tvod.single_price,\n") ;
		sql.append("         tvod.discount_price,\n") ;
		sql.append("         ''\n") ;
		sql.append("    from tt_vs_order_detail tvod, tt_vs_order tvo\n") ;
		sql.append("   where tvod.order_id = tvo.order_id\n") ;
		sql.append("     and tvo.order_id = ").append(orderId).append("\n") ;

		super.insert(sql.toString()) ;
	}
	
	public BigDecimal getStoreTotalPrice(String reqId) {
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select sum(tvdrd.total_price) total_price\n") ;
		sql.append("  from tt_vs_dlvry_req_dtl tvdrd\n") ;
		sql.append(" where tvdrd.req_id = ").append(reqId).append("") ;

		Map<String, Object> map = super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
		
		return new BigDecimal(map.get("TOTAL_PRICE").toString()) ;
	}
	//======================================================END
	
	//modify by WHX,2012.09.22
	//======================================================START
	//资金冻结
	public void storePriceFreezeOfJSZX(Map<String, String> map) {
		String orderId = map.get("orderId") ;
		String reqId = map.get("reqId") ;
		String fundClass = map.get("fundClass") ;
		String userId = map.get("userId") ;
		
		TtVsOrderPO tvo = new TtVsOrderPO() ;
		tvo.setOrderId(new Long(orderId)) ;
		
		tvo = (TtVsOrderPO)super.select(tvo).get(0) ;
		
		Long fundType = tvo.getFundTypeId() ;
		
		if(!CommonUtils.isNullString(fundClass) && fundClass.matches(this.getTypeClass(fundType.toString()))) {
			Long orderDlrId = tvo.getOrderOrgId() ;
			
			TtVsAccountPO tva = new TtVsAccountPO() ;
			tva.setDealerId(orderDlrId) ;
			tva.setAccountTypeId(fundType) ;
			
			List<?> list = super.select(tva) ;
			
			if(CommonUtils.isNullList(list)) {
				throw new RuntimeException("采购单位不存在对应资金账户，无法冻结资金！") ;
			}
			tva = (TtVsAccountPO)list.get(0) ;
			
			AccountOpera.dmsFreezePrice(reqId, tva.getAccountId().toString(), this.getStoreTotalPrice(reqId), userId) ;
		}
	}
	
	public String getTypeClass(String fundType) {
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select tvat.type_class from tt_vs_account_type tvat where tvat.type_id = ").append(fundType).append("") ;
		
		Map<String, Object> map = super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
		
		return map.get("TYPE_CLASS").toString() ;
	}
	//======================================================END
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//God Bless INFOSERVICE-CHANA-TEAM
	//And Bye Brothers!@1120 Nick
}
