package com.infodms.dms.actions.sales.storage.sendmanage;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.StorageUtil;
import com.infodms.dms.dao.sales.storage.sendManage.AllocaDao;
import com.infodms.dms.dao.sales.storage.sendManage.RemovalStorageDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardSeachDao;
import com.infodms.dms.dao.sales.storage.sendManage.WaybillManageDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesAssignPO;
import com.infodms.dms.po.TtSalesBoDetailPO;
import com.infodms.dms.po.TtSalesBoardPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : WaybillManage
 * @Description   : 运单生成管理Action 
 * @author        : wenyd
 * CreateDate     : 2013-5-16
 */
public class WaybillManage {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final AllocaDao reDao = AllocaDao.getInstance();
	private final RemovalStorageDao rsDao = RemovalStorageDao.getInstance();
	private final WaybillManageDao wmDao = WaybillManageDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	//运单管理查询页面
	private final String waybillManageInitUtl = "/jsp/sales/storage/sendmanage/waybillManage/waybillManageInit.jsp";
	//生成运单详情页面
	private final String allocaAdjustInitUtl = "/jsp/sales/storage/sendmanage/waybillManage/waybillManagejust.jsp";
	//配车详细信息查看页面
	private final String allocaseachInitUtl = "/jsp/sales/storage/sendmanage/waybillManage/waybillManageSeach.jsp";



	/**
	 * 
	 * @Title      : 
	 * @Description: 运单生成管理初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-16
	 */
	public void WaybillManageInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());

		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			if(list_logi != null && list_logi.size() > 0)
			{
				Map<String, Object> obj = list_logi.get(0);
				act.setOutData("LOGI_NAME", obj.get("LOGI_NAME"));
			}
			act.setForword(waybillManageInitUtl);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运单生成管理初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 运单管理查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void allocaAdjustQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerId"));//经销商
			String orgCode = CommonUtils.checkNull(request.getParamValue("ORGID"));//区域
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupId"));//物料组
			String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
			String orderNo = CommonUtils.checkNull(request.getParamValue("ORDER_NO"));//订单编号
			String boNo = CommonUtils.checkNull(request.getParamValue("BO_NO")); // 组板编号
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME"));//物流公司
			String invoiceNo = CommonUtils.checkNull(request.getParamValue("INVOICE_NO")); //发票号
			String boStartDate = CommonUtils.checkNull(request.getParamValue("BO_STARTDATE")); //组板日期开始
			String boEndDate = CommonUtils.checkNull(request.getParamValue("BO_ENDDATE")); // 组板日期结束
			String allocaStartDate = CommonUtils.checkNull(request.getParamValue("ALLOCA_STARTDATE")); //配车日期开始
			String allocaEndDate = CommonUtils.checkNull(request.getParamValue("ALLOCA_ENDDATE")); // 配车日期结束
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			
			
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dealerCode", dealerCode);
			map.put("orgCode", orgCode);
			map.put("groupCode", groupCode);
			map.put("orderType", orderType);
			map.put("orderNo", orderNo);
			map.put("logiName", logiName);
			map.put("invoiceNo", invoiceNo);
			map.put("yieldly", yieldly);
			map.put("boStartDate", boStartDate);
			map.put("boEndDate", boEndDate);
			map.put("boNo", boNo);
			map.put("allocaStartDate", allocaStartDate);
			map.put("allocaEndDate", allocaEndDate);
			map.put("poseId", logonUser.getPoseId().toString());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = wmDao.getWaybillManageQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运单管理查询信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description:初始化运单生成页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void  allocaAdjustUpdate(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {	
		String []groupIds =request.getParamValues("groupIds");
		
         String boid="";
	      for(int i=0;i<groupIds.length;i++){
	    	  if(i==(groupIds.length-1)){
	    		  boid+=groupIds[i];
	    	  }else{
		    	  boid+=groupIds[i]+",";  
	    	  }

	      }
			Map<Object,List<Map<String, Object>>>  boByVeMap= new HashMap<Object,List<Map<String, Object>>>() ;//根据某组板明细获取该明细下的车辆信息
	        List<Map<String, Object>> list = wmDao.getSendBoardMatListQuery(boid);
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String, Object> map=list.get(i);
					List<Object> paramsVe=new ArrayList<Object>();
					paramsVe.add(map.get("BO_DE_ID"));//组板详细表ID
					List<Map<String, Object>> vehicleList= reDao.getVehicleQueryByBoDeId(paramsVe); //查询配车明细表
					boByVeMap.put(map.get("BO_DE_ID"), vehicleList);
				}
			}
			List<Object> params=new ArrayList<Object>();
			params.add(boid);
			Map<String,Object> sendBoardMap=SendBoardSeachDao.getInstance().getBoardByBoId(params);
			act.setOutData("boByVeMap", boByVeMap);
			act.setOutData("list", list);
			act.setOutData("BOIDS", boid);
			act.setOutData("sendBoardMap", sendBoardMap);
			act.setForword(allocaAdjustInitUtl);
	} catch (Exception e) {// 异常方法
		BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运单加载信息");
		logger.error(logonUser, e1);
		act.setException(e1);
	}
		
		
		
	}
	
	
	/**
	 * 
	 * @Title      : 
	 * @Description:初始化明细页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */

	public void updateAllocaSeachInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String boId = CommonUtils.checkNull(request.getParamValue("Id"));//组板ID
			List<Object> params=new ArrayList<Object>();
			params.add(boId);
			Map<String,Object> sendBoardMap=SendBoardSeachDao.getInstance().getBoardByBoId(params);
			Map<Object,List<Map<String, Object>>>  boByVeMap= new HashMap<Object,List<Map<String, Object>>>() ;//根据某组板明细获取该明细下的车辆信息
			List<Map<String, Object>> list = wmDao.getSendBoardMatListQuery(boId);//查询组板明细信息
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String, Object> map=list.get(i);
					List<Object> paramsVe=new ArrayList<Object>();
					paramsVe.add(map.get("BO_DE_ID"));//组板详细表ID
					List<Map<String, Object>> vehicleList= reDao.getVehicleQueryByBoDeId(paramsVe); //查询配车明细表
					boByVeMap.put(map.get("BO_DE_ID"), vehicleList);
				}
			}	
			act.setOutData("sendBoardMap", sendBoardMap);
			act.setOutData("boByVeMap", boByVeMap);
			act.setOutData("list", list);
			act.setOutData("boId", boId);
			
			act.setForword(allocaseachInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"初始化运单生成明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description:生成运单
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void allWaybillManageUpdate(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
		String boIDs = request.getParamValue("boids");
		String carNo = CommonUtils.checkNull(request.getParamValue("carNo"));
		String loads = CommonUtils.checkNull(request.getParamValue("loads"));
		String carTeam = CommonUtils.checkNull(request.getParamValue("carTeam"));
		String policyNo = CommonUtils.checkNull(request.getParamValue("policyNo"));
		String policyType = CommonUtils.checkNull(request.getParamValue("policyType"));
		String driverName = CommonUtils.checkNull(request.getParamValue("driverName"));
		String driverTel = CommonUtils.checkNull(request.getParamValue("driverTel"));
		TtSalesBoardPO po_1 = new TtSalesBoardPO();
		po_1.setBoId(Long.valueOf(boIDs));
		TtSalesBoardPO po_2 = new TtSalesBoardPO();
		if(!"".equals(carNo)){
			po_2.setCarNo(carNo);
		}
		if(!"".equals(loads)){
			po_2.setLoads(loads);
		}
		if(!"".equals(carTeam)){
			po_2.setCarTeam(carTeam);
		}
		if(!"".equals(policyNo)){
			po_2.setPolicyNo(policyNo);
		}
		if(!"".equals(policyType)){
			po_2.setPolicyType(Long.valueOf(policyType));
		}
		if(!"".equals(driverName)){
			po_2.setDriverName(driverName);
		}
		if(!"".equals(driverTel)){
			po_2.setDriverTel(driverTel);
		}
		wmDao.update(po_1, po_2);
//		po_.setCarNo(carNo);
		//返回运单号（用逗号隔开）
		String returnBillNo="";
		if(boIDs != null && !"".equals(boIDs)){
			List<Map<String, Object>> list = wmDao.getInfoForWayBill(boIDs);
			if(list != null && list.size() > 0){
				for(int i = 0 ; i < list.size(); i++){
					Map<String, Object> map = list.get(i);
					BigDecimal recDealerId = (BigDecimal)map.get("REC_DEALER_ID");  //发运经销商ID
					BigDecimal dealerId = (BigDecimal)map.get("DEALER_ID");  //订货经销商ID
					BigDecimal addressId = (BigDecimal)map.get("ADDRESS_ID");  //发运地址
					String vehicleNum = (String)map.get("VEHICLE_NUM"); //车辆数量
					String boNum = (String)map.get("BO_NUM"); //组板数
					BigDecimal logiId = (BigDecimal)map.get("LOGI_ID"); //承运商
					BigDecimal areaId = (BigDecimal)map.get("AREA_ID"); //产地
					TtSalesWaybillPO po = new TtSalesWaybillPO();
					String chPrId= SequenceManager.getSequence(null);
					po.setBillId(Long.valueOf(chPrId));
					po.setOrDealerId(dealerId.longValue());//订货经销商ID
					po.setSendDealerId(recDealerId.longValue());//发运经销商IDs
					po.setLogiId(logiId.longValue());
					po.setAreaId(areaId.longValue());
					po.setAddressId(addressId.longValue());
					po.setVehNum(Integer.valueOf(vehicleNum));
					po.setBoNum(Integer.valueOf(boNum));
					po.setStatus(Long.parseLong(Constant.STATUS_ENABLE.toString()));
					po.setBillCrtDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
					po.setBillCrtPer(logonUser.getUserId());
					
					po.setBillNo(CommonUtils.getBusNo(Constant.NOCRT_SEND_ORDER_NO,areaId.longValue()));
					//运单状态调整为“在途”
					po.setSendStatus(Long.parseLong(Constant.SEND_STATUS_01));
					po.setCreateBy(logonUser.getUserId());
					po.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
					
					//2013.11.9新加
					po.setIsConfirm(Constant.IF_TYPE_NO);//否代表还没确认
					wmDao.insert(po);
					//回写发运组板明细表运单ID
					String sql = "UPDATE TT_SALES_BO_DETAIL SET BILL_ID = "+Long.valueOf(chPrId)+ " WHERE 1 = 1 \n"
								+" AND REC_DEALER_ID = "+recDealerId+" AND ADDRESS_ID = "+addressId+" AND DEALER_ID="+dealerId+"\n"
								+" AND AREA_ID = "+areaId + " AND LOGI_ID = "+logiId
								+" AND BO_ID IN ("+boIDs+")";
					wmDao.update(sql,null);
					
					String sql_1 = "UPDATE TT_SALES_ALLOCA_DE SET IS_SEND = "+Constant.IF_TYPE_YES + ", \n"
								+" SEND_DATE = sysdate , SEND_BY = "+logonUser.getUserId()+"\n"
								+" WHERE STATUS="+Constant.STATUS_ENABLE+" AND BO_DE_ID IN (SELECT BO_DE_ID FROM TT_SALES_BO_DETAIL WHERE BO_ID IN ("+boIDs+") "
								+" AND REC_DEALER_ID = "+recDealerId+" AND ADDRESS_ID = "+addressId+" AND DEALER_ID = "+dealerId+")";
					wmDao.update(sql_1, null);
					returnBillNo+=po.getBillNo()+",";//获取运单号
				}
			}
//			// 发运数量回填
			String[] attr = boIDs.split(",");
			for(int j=0;j<attr.length;j++){
				//修改车辆表状态
				wmDao.updateVehicleStatus(Long.parseLong(attr[j]),logonUser.getUserId());
				//修改发运数量
				StorageUtil.getInstance().updateSendNum(Long.parseLong(attr[j]),logonUser.getUserId());
				//修改配车明细表运费（发运后生成结算费用，记得取消运单时候清除掉）
				
			}
//			String[] attr = boIDs.split(",");
//			for(int j=0;j<attr.length;j++){
//			 //修改车辆表状态
//			  wmDao.updateVehicleStatus(Long.parseLong(attr[j]),logonUser.getUserId());
//			}
			act.setOutData("returnBillNo", returnBillNo.equals("")?"":returnBillNo.substring(0,returnBillNo.length()-1));//操作成功
			act.setOutData("returnValue", 1);//操作成功
		}else{
			act.setOutData("returnValue", 2);//数据错误
		}
	} catch (Exception e) {
		BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
				"运单生成信息");
		logger.error(logonUser, e1);
		act.setException(e1);
	}

		
	}
	
	public void updateLoinfo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String boId = request.getParamValue("boId");
			String logoId = request.getParamValue("logoinfo");
			Long logoIdLong = Utility.getLong(logoId);
			//修改组版明细的承运商信息
			TtSalesBoDetailPO detailCondtion = new TtSalesBoDetailPO();
			detailCondtion.setBoId(Utility.getLong(boId));
			TtSalesBoDetailPO updatePo = new TtSalesBoDetailPO();
			updatePo.setLogiId(logoIdLong);
			wmDao.update(detailCondtion, updatePo);
			//修改发运分派表的承运商信息
			List<Map<String,Object>> orderids = wmDao.getOrderIdsByBoId(boId);
			for(Map<String,Object> map : orderids){
				String orderid = CommonUtils.checkNull(map.get("ORDER_ID"));
				TtSalesAssignPO acon = new TtSalesAssignPO();
				acon.setOrderId(Utility.getLong(orderid));
				
				TtSalesAssignPO updateAss = new TtSalesAssignPO();
				updateAss.setLogiId(logoIdLong);
				wmDao.update(acon, updateAss);
			}
			act.setOutData("result", "1");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
			"承运商修改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
