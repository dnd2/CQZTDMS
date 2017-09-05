package com.infodms.dms.actions.sales.ordermanage.orderreport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.relation.DealerRelationDAO;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.LowOrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.salesConsultant.SalesConsultantDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqGeneralDtlPO;
import com.infodms.dms.po.TtVsDlvryReqGeneralPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetOrderNOUtil;
import com.infodms.dms.util.businessUtil.ReqLogUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

public class LowWeekGeneralOrderCall {
	public Logger logger = Logger.getLogger(LowWeekGeneralOrderCall.class);
	OrderDeliveryDao dao = OrderDeliveryDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private static final String INIT_URL = "/jsp/sales/ordermanage/orderreport/lowWeekGeneralOrderCallPre.jsp" ;
	private static final String LIST_URL = "/jsp/sales/ordermanage/orderreport/lowWeekGeneralOrderCall.jsp";
	
	/*---------------------------------------------------------- action方法 ----------------------------------------------------------*/
	
	public void LowWeekCallInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> dateList = dao.getGeneralDeliveryDateList_New(oemCompanyId);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString(), Constant.DEALER_LEVEL_02.toString()) ;
			
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			
			act.setForword(INIT_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "下级经销商周度常规订单启票初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void applyQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderYearWeek = request.getParamValue("orderYearWeek");
			String[] array = orderYearWeek.split("-");
			String orderYear = array[0];
			String orderWeek = array[1];
			String areaId = request.getParamValue("areaId"); // 业务范围
			String dealerId = request.getParamValue("dealerId"); // 经销商ID
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			
			DealerRelationDAO dr = DealerRelationDAO.getInstance() ;
			String firstDlr = dr.getFirstDlr(dealerId).get("DEALER_ID").toString() ;
			
			if(CommonUtils.isNullString(firstDlr)) {
				throw new BizException("该经销商不存在一级经销商！") ;
			}
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>() ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				list = dao.getWeekGeneralOrderCallList(orderYear, orderWeek, areaId, firstDlr, dealerId, orderNo, oemCompanyId);
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				list = dao.getWeekGeneralOrderCallListCVS(orderYear, orderWeek, areaId, firstDlr, dealerId, orderNo, oemCompanyId);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			
			List<Map<String, Object>> accountlist = dao.getDealerAccount(firstDlr);
			
			act.setOutData("firstDlr", firstDlr) ;
			act.setOutData("list", list);
			act.setOutData("accountlist", accountlist);
			act.setOutData("dealerId", dealerId);
			
			act.setForword(LIST_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "下级经销商月度常规订单启票查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void applySubmit() {

		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long userId = logonUser.getUserId();

		try {
			String[] detailId = request.getParamValues("detailId");
			String[] orderId = request.getParamValues("orderId");
			String[] areaId = request.getParamValues("areaId");
			String[] materialId = request.getParamValues("materialId");
			// String[] callAmount = request.getParamValues("callAmount");
			String[] applyAmount = request.getParamValues("applyAmount");

			String typeId = request.getParamValue("typeId"); // 账户类型ID
			String transportType = request.getParamValue("transportType"); // 发运方式
			String addressId = request.getParamValue("addressId"); // 发运地址ID
			String fleetId = request.getParamValue("fleetId"); // 大客户ID
			String address = request.getParamValue("address"); // 大客户地址
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver"));// 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));// 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));// 联系电话
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover")); // 是否代交车
			String reqTotalAmount = request.getParamValue("reqTotalAmount"); // 发运申请总量
			String dealerId = request.getParamValue("dealerId"); // 经销商id
			String firstDlr = CommonUtils.checkNull(request.getParamValue("firstDlr")) ; // 一级经销商id
			String reqRemark = CommonUtils.checkNull(request.getParamValue("orderRemark")) ;
			
			String returnValue = "1" ;	// 错误信息标识

			/*TtVsOrderDetailPO tsodpContion = new TtVsOrderDetailPO(); // 订单明细表
			TtVsOrderDetailPO tsodpValue = new TtVsOrderDetailPO();*/

			if (applyAmount != null && applyAmount.length != 0) {
				MonthGeneralOrderCall mgoc = new MonthGeneralOrderCall() ;
				String str = mgoc.getMatCode(detailId, applyAmount) ;
				
				if(!CommonUtils.isNullString(str)){
					act.setOutData("metStr", str);
					returnValue = "2" ;
				} else {
					TtVsDlvryReqPO tvdrpo = new TtVsDlvryReqPO(); // 发运申请表
					Long reqId = Long.parseLong(SequenceManager.getSequence(""));
					tvdrpo.setReqId(reqId);
					tvdrpo.setAreaId(new Long(areaId[0]));
					tvdrpo.setOrderId(new Long(orderId[0]));
					tvdrpo.setFundType(new Long(typeId));
					tvdrpo.setReqRemark(reqRemark) ;
					if (null != isCover && "1".equals(isCover)) {
						tvdrpo.setIsFleet(1);
						tvdrpo.setFleetId(Long.parseLong(fleetId));
						tvdrpo.setFleetAddress(address);
					} else {
						tvdrpo.setIsFleet(0);
						tvdrpo.setDeliveryType(Integer.parseInt(transportType));
						if (Integer.parseInt(transportType) == Constant.TRANSPORT_TYPE_02) {
							tvdrpo.setAddressId(Long.parseLong(addressId));
							tvdrpo.setReceiver(new Long(receiver));
							tvdrpo.setLinkMan(linkMan);
							tvdrpo.setTel(tel);
						} else {
							tvdrpo.setReceiver(new Long(dealerId));
						}
					}
					
					tvdrpo.setOrderDealerId(Long.parseLong(dealerId)) ;
					tvdrpo.setBilDealerId(Long.parseLong(firstDlr)) ;
					tvdrpo.setReqStatus(Constant.ORDER_REQ_STATUS_YSH);
					tvdrpo.setReqDate(new Date());
					tvdrpo.setReqTotalAmount(new Integer(reqTotalAmount));
					tvdrpo.setReqExecStatus(Constant.REQ_EXEC_STATUS_01);
					tvdrpo.setVer(new Integer(0));
					tvdrpo.setCreateBy(userId);
					tvdrpo.setCreateDate(new Date());
					
					// 获得业务范围编码
					Map<String, String> codeMap = GetOrderNOUtil.getAreaShortcode(areaId[0]);
					String areaCode = codeMap.get("AREA_SHORTCODE");
					// 获得经销商代码
					TmDealerPO tmDealerPO = new TmDealerPO();
					tmDealerPO.setDealerId(new Long(dealerId));
					List<PO> dealerList = dao.select(tmDealerPO);
					String dealerCode = ((TmDealerPO) dealerList.get(0)).getDealerCode();
					// 获得发运订单编号
					String dlvryReqNO = GetOrderNOUtil.getDlvryReqNO(areaCode, "D", dealerCode);
					tvdrpo.setDlvryReqNo(dlvryReqNO);
					tvdrpo.setCallLeavel(Constant.DEALER_LEVEL_02) ;
	
					dao.insert(tvdrpo);// 插入发运申请
					//向发运申请操作日志表写入日志信息
					ReqLogUtil.creatReqLog(reqId, Constant.REQ_LOG_TYPE_YTB, logonUser.getUserId());
					for (int i = 0; i < applyAmount.length; i++) {
						if (applyAmount[i] != null && !"".equals(applyAmount[i]) && Integer.parseInt(applyAmount[i]) > 0) {
							TtVsDlvryReqDtlPO tvdrdpo = new TtVsDlvryReqDtlPO(); // 发运申请明细表
							tvdrdpo.setDetailId(Long.parseLong(SequenceManager.getSequence("")));// 设置发运申请明细主键
							tvdrdpo.setReqId(tvdrpo.getReqId());
							tvdrdpo.setOrderDetailId(Long.parseLong(detailId[i]));
							tvdrdpo.setMaterialId(Long.parseLong(materialId[i]));
							tvdrdpo.setReqAmount(Integer.parseInt(applyAmount[i]));
							tvdrdpo.setVer(0);
							tvdrdpo.setCreateBy(userId);
							tvdrdpo.setCreateDate(new Date());
							
							dao.insert(tvdrdpo); // 插入发运申请明细表
	
							// tsodpContion.setDetailId(Long.parseLong(detailId[i]));
							// tsodpValue.setCallAmount(Integer.parseInt(callAmount[i]) + Integer.parseInt(applyAmount[i]));
							// tsodpValue.setUpdateBy(userId);
							// tsodpValue.setUpdateDate(new Date());
							// dao.update(tsodpContion, tsodpValue);// 更新订单明细表
						}
					}
					DealerRelationDAO dr = DealerRelationDAO.getInstance() ;
					if(Constant.DEALER_TYPE_JSZX == Integer.parseInt(dr.getFirstDlr(dealerId).get("DEALER_TYPE").toString())) {
						String newReqId = this.insertGeneralReq(tvdrpo) ;
						this.insertGeneralReqDtl(newReqId, reqId.toString()) ;
						
						// 调用发运存储过程
						List<Object> ins = new LinkedList<Object>();
						ins.add(newReqId);

						List<Integer> outs = new LinkedList<Integer>();
						dao.callProcedure("P_INSERTDATA_TO_DFS_CG_JSZX", ins, outs);
					}
				}
			}
			
			act.setOutData("returnValue", returnValue);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "下级经销商月度常规订单启票");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getGeneralOrderNO() {
		String pageDate = CommonUtils.checkNull(request.getParamValue("orderYearWeek")) ;
		String pageArea = CommonUtils.checkNull(request.getParamValue("area")) ;
		
		String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
		
		String pageYear = pageDate.split("-")[0] ;
		String pageWeek = pageDate.split("-")[1] ;
		String pageAreaId = pageArea.split("\\|")[0] ;
		String pageDealerId = pageArea.split("\\|")[1] ;
		
		String flag = "1" ;
		
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>() ;
		Map<String, String> map = new HashMap<String, String>() ;
		
		map.put("areaId", pageAreaId) ;
		map.put("year", pageYear) ;
		map.put("week", pageWeek) ;
		map.put("orderType", Constant.ORDER_TYPE_01.toString()) ;
		map.put("orderStatus", Constant.ORDER_STATUS_05.toString()) ;
		map.put("dealerId", pageDealerId) ;
		
		if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
			DealerRelationDAO dr = DealerRelationDAO.getInstance() ;
			map.put("dealerId", dr.getFirstDlr(pageDealerId).get("DEALER_ID").toString()) ;
			
			orderList = dao.getOrderNO(map) ;
		} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
			orderList = dao.getOrderNOByOrderUnit(map) ;
		} else {
			throw new RuntimeException("判断当前系统的系统参数错误！") ;
		}
		
		if(CommonUtils.isNullList(orderList)) {
			flag = "0" ;
		} 
		
		act.setOutData("orderList", orderList) ;
		act.setOutData("flag", flag) ;
	}
	
	/*---------------------------------------------------------- public方法 ----------------------------------------------------------*/
	
	/*---------------------------------------------------------- private方法 ----------------------------------------------------------*/
	private String insertGeneralReq(TtVsDlvryReqPO po) {
		TtVsDlvryReqGeneralPO tvdrg = new TtVsDlvryReqGeneralPO() ;
		
		Long reqId = Long.parseLong(SequenceManager.getSequence(""));
		tvdrg.setReqId(reqId) ;
		tvdrg.setAreaId(po.getAreaId()) ;
		tvdrg.setOrderId(po.getOrderId()) ;
		tvdrg.setFundType(po.getFundType()) ;
		tvdrg.setDeliveryType(po.getDeliveryType()) ;
		tvdrg.setAddressId(po.getAddressId()) ;
		tvdrg.setReqDate(new Date(System.currentTimeMillis())) ;
		tvdrg.setReqTotalAmount(po.getReqTotalAmount()) ;
		tvdrg.setReqExecStatus(po.getReqExecStatus()) ;
		tvdrg.setVer(po.getVer()) ;
		tvdrg.setCreateBy(po.getCreateBy()) ;
		tvdrg.setCreateDate(new Date(System.currentTimeMillis())) ;
		tvdrg.setFleetId(po.getFleetId()) ;
		tvdrg.setFleetAddress(po.getFleetAddress()) ;
		tvdrg.setPriceId(Long.parseLong(this.getPirceId(po.getReqId().toString()))) ;
		tvdrg.setOtherPriceReason(po.getOtherPriceReason()) ;
		tvdrg.setReceiver(po.getReceiver()) ;
		tvdrg.setLinkMan(po.getLinkMan()) ;
		tvdrg.setTel(po.getTel()) ;
		tvdrg.setDiscount(po.getDiscount()) ;
		tvdrg.setModifyFlag(po.getModifyFlag()) ;
		tvdrg.setWarehouseId(po.getWarehouseId()) ;
		tvdrg.setReqStatus(Long.parseLong(po.getReqStatus().toString())) ;
		//tvdrg.setReqTotalPrice(po.getReqTotalPrice()) ;
		tvdrg.setReserveTotalAmount(po.getReserveTotalAmount()) ;
		tvdrg.setIsFleet(po.getIsFleet()) ;
		tvdrg.setDlvryReqNo("CG" + po.getDlvryReqNo()) ;
		tvdrg.setOrderDealerId(po.getOrderDealerId()) ;
		tvdrg.setBilDealerId(po.getBilDealerId()) ;
		tvdrg.setCallLeavel(po.getCallLeavel()) ;
		tvdrg.setReqRemark(po.getReqRemark()) ;
		tvdrg.setOldReqId(po.getReqId()) ;
		
		dao.insert(tvdrg) ;
		
		return reqId.toString() ;
	}
	
	private int insertGeneralReqDtl(String reqId, String oldReqId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("INSERT INTO TT_VS_DLVRY_REQ_GENERAL_DTL\n");
		sql.append("SELECT F_GETID,\n");  
		sql.append("       ").append(reqId).append(",\n");  
		sql.append("       TVDRD.ORDER_DETAIL_ID,\n");  
		sql.append("       TVDRD.MATERIAL_ID,\n");  
		sql.append("       TVDRD.PATCH_NO,\n");  
		sql.append("       TVDRD.REQ_AMOUNT,\n");  
		sql.append("       TVDRD.DELIVERY_AMOUNT,\n");  
		sql.append("       TVDRD.VER,\n");  
		sql.append("       TVDRD.CREATE_BY,\n");  
		sql.append("       TVDRD.CREATE_DATE,\n");  
		sql.append("       TVDRD.UPDATE_BY,\n");  
		sql.append("       TVDRD.UPDATE_DATE,\n");  
		sql.append("       nvl(VTVPD.SALES_PRICE, -1) SALES_PRICE,\n");  
		sql.append("       TVDRD.REQ_AMOUNT * nvl(VTVPD.SALES_PRICE, -1) TOTAL_PRICE,\n");  
		sql.append("       TVDRD.DISCOUNT_RATE,\n");  
		sql.append("       ROUND(nvl(VTVPD.SALES_PRICE, -1) * (100 - TVDRD.DISCOUNT_RATE) / 100) DISCOUNT_S_PRICE,\n");  
		sql.append("       ROUND(nvl(VTVPD.SALES_PRICE, -1) * TVDRD.DISCOUNT_RATE / 100) DISCOUNT_PRICE,\n");  
		sql.append("       TVDRD.RESERVE_AMOUNT,\n"); 
		sql.append("       TVDRD.DETAIL_ID\n"); 
		sql.append("  FROM TT_VS_DLVRY_REQ_DTL      TVDRD,\n");  
		sql.append("         (select vtvpd.GROUP_ID, vtvpd.SALES_PRICE\n");  
		sql.append("            from VW_TT_VS_PRICE_DTL VTVPD\n");  
		sql.append("           where vtvpd.PRICE_ID = ").append(this.getPirceId(oldReqId)).append(") VTVPD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR\n");  
		sql.append(" WHERE TVDRD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
		sql.append("   AND TVMGR.GROUP_ID = VTVPD.GROUP_ID(+)\n");  
		//sql.append("   AND VTVPD.PRICE_ID = ").append(this.getPirceId(oldReqId)).append("\n");  
		sql.append("   AND TVDRD.REQ_ID = ").append(oldReqId).append("\n");

		dao.insert(sql.toString()) ;
		
		this.chkPrice(reqId) ;
		
		return 0 ;
	}
	
	public boolean chkPrice(String reqId) {
		TtVsDlvryReqGeneralDtlPO tvdrg = new TtVsDlvryReqGeneralDtlPO() ;
		tvdrg.setReqId(Long.parseLong(reqId)) ;
		
		List<TtVsDlvryReqGeneralDtlPO> list = dao.select(tvdrg) ;
		
		if(CommonUtils.isNullList(list)) {
			throw new RuntimeException("操作失败:dms-ins0,无数据更新!") ;
		} else {
			int len = list.size() ;
			
			for(int i=0; i<len; i++) {
				if(list.get(i).getSinglePrice() < 0) {
					throw new RuntimeException("操作失败:dms-mat" + list.get(i).getMaterialId() + ",对应价格列表未维护!") ;
				}
			}
		}
		
		return true ;
	}
	
	public String getPirceId(String reqId) {
		AccountBalanceDetailDao abd = new AccountBalanceDetailDao() ;
		
		String priceId = abd.getPriceId(abd.getParentDlrId(reqId)) ;
		
		if(CommonUtils.isNullString(priceId)) {
			throw new RuntimeException("對應結算中心下級經銷商價格列表未維護!") ;
		}
		
		return priceId ;
	}
}
