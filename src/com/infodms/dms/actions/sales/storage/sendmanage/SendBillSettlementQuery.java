package com.infodms.dms.actions.sales.storage.sendmanage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBillManageDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 运单结算查询Action
 * @author xieyujia
 * 2013-5-21
 */
public class SendBillSettlementQuery {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private SendBillManageDao sbmDao = SendBillManageDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final String sendBillSettlementQueryInitUrl = "/jsp/sales/storage/sendmanage/sendBillManage/sendBillSettlementListQuery.jsp";
	private final String settlementDetailUrl = "/jsp/sales/storage/sendmanage/sendBillManage/settlementDetail.jsp";
	private final String PRINT_DEAIL_URL = "/jsp/sales/storage/sendmanage/sendBillManage/balancePrintDetail.jsp";
	
	public void sendBillSettlementQueryInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
		    List<Map<String, Object>> areaList  =	MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
		    act.setOutData("areaList", areaList);
		    List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			act.setForword(sendBillSettlementQueryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"结算单查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void settlementListQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
		   String areaId = request.getParamValue("areaId");
		   String logiId = request.getParamValue("logiId");
		   String balNo = request.getParamValue("balNo");
		   String balStartDate = request.getParamValue("balStartDate");
		   String balEndDate = request.getParamValue("balEndDate");
		   Map<String, Object> map = new HashMap<String, Object>();
		   if(areaId != null && !"".equals(areaId)){
			   map.put("areaId", areaId);
		   }
		   if(logiId != null && !"".equals(logiId)){
			   map.put("logiId", logiId);
		   }
		   if(balNo != null && !"".equals(balNo)){
			   map.put("balNo", balNo);
		   }
		   if(balStartDate != null && !"".equals(balStartDate)){
			   map.put("balStartDate", balStartDate);
		   }
		   if(balEndDate != null && !"".equals(balEndDate)){
			   map.put("balEndDate", balEndDate);
		   }
		   map.put("poseId", logonUser.getPoseId().toString());
		   Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
		   PageResult<Map<String, Object>> ps = sbmDao.getSettlementList(map, curPage, Constant.PAGE_SIZE);
		   act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"结算单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 结算单查看
	 */
	public void toShowSettlementDetail(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			   String balId = request.getParamValue("balId");
			   if(balId != null && !"".equals(balId)){
				   //按车辆明细得到结算明细
				   List<Map<String , Object>> detail_list = sbmDao.getBalDetailByVclDetail(balId);
				   act.setOutData("detail_list", detail_list);
				   //按车系得到结算明细
				   List<Map<String, Object>> series_list = sbmDao.getBalDetailByVclSeries(balId);
				   act.setOutData("series_list", series_list);
				   //计算结算总额
				   Float sum_amount = 0f;
				   for(int i = 0 ; i < series_list.size() ; i++){
					   Map<String,Object> map = series_list.get(i);
					   Float temp = ((BigDecimal)map.get("BAL_AMOUNT")).floatValue();
					   sum_amount += temp;
				   }
				   DecimalFormat df = new DecimalFormat( "#,###,###,###,###,###.00 ");
					String sum = df.format(sum_amount);
				   act.setOutData("sum_amount", sum);
			   } 
			   /**是否取消运单标示符**/
			   String command = CommonUtils.checkNull(request.getParamValue("command"));
			   if("1".equals(command)){
				   act.setOutData("command", command);
			   }else{
				   act.setOutData("command", 0);
			   }
			    act.setOutData("balId", balId);
				act.setForword(settlementDetailUrl);
//		   }
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"结算单查看");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void  balancePrint(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String bal_id = request.getParamValue("balId");
			List<Map<String, Object>> list = sbmDao.getPrintVehicleDetail(bal_id);//得到结算按详细（按车辆）
			Map<String, Object> bal_info = sbmDao.getBalDetail(bal_id);
		    TtSalesWaybillPO po = new TtSalesWaybillPO();
		    po.setBalId(Long.valueOf(bal_id));
		    List<PO> list_1 = sbmDao.select(po);	
			   String billIds = "";
			   if(list_1 != null && list_1.size() > 0){
				   for(int i = 0; i< list_1.size();i++){
					   TtSalesWaybillPO po_ = (TtSalesWaybillPO)list_1.get(i);
					   billIds += po_.getBillId() + ",";
				   }
			   }
		    List<Map<String, Object>> series_list = sbmDao.getBalDetailByVclSeries(bal_id);
		    //计算结算总额
		    Double sum_amount = 0d,sum_fj_amount=0d,hj_sum_amount=0d;
		    Long vehicle_num = new Long(0);
		    for(int i = 0 ; i < series_list.size() ; i++){
			    Map<String,Object> map = series_list.get(i);
			    Double temp = ((BigDecimal)map.get("BAL_AMOUNT")).doubleValue();
			    Long v_num = ((BigDecimal)map.get("VEHICLE_NUM")).longValue();
			    Double temp_fj = ((BigDecimal)map.get("FJ_AMOUNT")).doubleValue();
			    sum_amount += temp;
			    vehicle_num += v_num;
			    sum_fj_amount+=temp_fj;
			    hj_sum_amount+=temp+temp_fj;
		    }
		    DecimalFormat df = new DecimalFormat( "#,###,###,###,###,###.00 ");
			String sum = df.format(sum_amount);
			String fj_sum = df.format(sum_fj_amount);
			String hj_sum = df.format(hj_sum_amount);
			act.setOutData("sum", sum);
			act.setOutData("bal_info", bal_info);
			act.setOutData("list", list);
			act.setOutData("series_list", series_list);
			act.setOutData("vehicle_num", vehicle_num);
			act.setOutData("fj_sum", fj_sum.trim().equals(".00")?"0":fj_sum);
			act.setOutData("hj_sum", hj_sum);
			act.setForword(PRINT_DEAIL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"结算单打印");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void  cancleBalance(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String bal_id = request.getParamValue("balId");
			//取消结算时，更改运单的状态及初始化结算单ID
		    String sql  = "UPDATE TT_SALES_WAYBILL SET SEND_STATUS = "+Constant.SEND_STATUS_02+",\n"
		    									   +"  BAL_ID = NULL ,\n"
		    									   +"  UPDATE_DATE = SYSDATE,\n"
		    									   +"  UPDATE_BY = "+logonUser.getUserId()+"\n"
		    			+ "WHERE BILL_ID IN (SELECT BILL_ID FROM TT_SALES_WAYBILL WHERE BAL_ID = "+bal_id+")";
		    sbmDao.update(sql, null);
		    //删除生成的结算单
		    String sql_ = "DELETE FROM TT_SALES_BALANCE WHERE BAL_ID = "+bal_id;
		    sbmDao.delete(sql_,null);
		    act.setOutData("result",1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.FAILURE_CODE,
					"结算单取消");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void toExcel(){
		 AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		 try {	
	 		String bal_id = request.getParamValue("balId");
	 		List<Map<String, Object>> list = sbmDao.getPrintVehicleDetail(bal_id);//得到结算按详细（按车辆）
	 		Map<String, Object> bal_info = sbmDao.getBalDetail(bal_id);
	 		 TtSalesWaybillPO po = new TtSalesWaybillPO();
			    po.setBalId(Long.valueOf(bal_id));
			    List<PO> list_1 = sbmDao.select(po);	
				   String billIds = "";
				   if(list_1 != null && list_1.size() > 0){
					   for(int i = 0; i< list_1.size();i++){
						   TtSalesWaybillPO po_ = (TtSalesWaybillPO)list_1.get(i);
						   billIds += po_.getBillId() + ",";
					   }
				   }
			List<Map<String, Object>> series_list = sbmDao.getBalDetailByVclSeries(billIds.substring(0,billIds.length()-1));
			 //计算结算总额
		    Double sum_amount = 0d,sum_fj_amount=0d;
		    Long vehicle_num = new Long(0);
		    for(int i = 0 ; i < series_list.size() ; i++){
			    Map<String,Object> map = series_list.get(i);
			    Double temp = ((BigDecimal)map.get("BAL_AMOUNT")).doubleValue();
			    Long v_num = ((BigDecimal)map.get("VEHICLE_NUM")).longValue();
			    Double temp_fj = ((BigDecimal)map.get("FJ_AMOUNT")).doubleValue();
			    sum_amount += temp;
			    vehicle_num += v_num;
			    sum_fj_amount+=temp_fj;
		    }
		    //DecimalFormat df = new DecimalFormat( "#,###,###,###,###,###.00 ");
			///String sum = df.format(sum_amount);
			//String fj_sum = df.format(sum_fj_amount);
	 		ToExcel.toBalanceExcel(act.getResponse(), request, "结算单详细.xls", bal_info,list,series_list,vehicle_num,sum_amount,sum_fj_amount);
		 }catch (Exception e) {
			 	BizException e1 = new BizException(act, e, ErrorCodeConstant.FAILURE_CODE,"结算单导出EXCEL");
				logger.error(logonUser, e1);
				act.setException(e1);
		 }	      	
	}
	

}
