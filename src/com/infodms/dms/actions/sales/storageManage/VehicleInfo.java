package com.infodms.dms.actions.sales.storageManage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.dao.sales.storageManage.VehicleInfoDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

public class VehicleInfo extends BaseDao{
	public Logger logger = Logger.getLogger(VehicleInfo.class);
	private ActionContext act = ActionContext.getContext();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO ();
	public static final CheckVehicleDAO getInstance() {
		return dao;
	}
	private final String  vehicleInfoURL = "/jsp/sales/storageManage/vehicleInfo.jsp";
	private final String  vehicleStatusInfoURL = "/jsp/sales/storageManage/vehicleStatusInfo.jsp";
	
	
	//详细车籍查询
	public void vehicleInfoQuery(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.getSession().get(Constant.LOGON_USER);
			
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			
			//1.车辆属性
			Map<String,Object> vehicleInfo = VehicleInfoDAO.getVehicleInfo(vin);
			
			//2.库存状态变更日志
			List<Map<String,Object>> storageChangeList = VehicleInfoDAO.getStorageChangeList(vin);
			
			//3.车辆销售状态日志
			List<Map<String,Object>> salesList = VehicleInfoDAO.getSalesList(vin);
			
			//4.取得车辆订单号
			Map<String,Object> vehicleOrderNo = VehicleInfoDAO.getVehicleSalesOrderNo(vin);
			 
			
			act.setOutData("vehicleInfo", vehicleInfo);
			act.setOutData("storageChangeList", storageChangeList);
			act.setOutData("salesList", salesList);
			act.setOutData("vehicleOrderNo", vehicleOrderNo);
			act.setForword(vehicleInfoURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "详细车籍查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//详细车籍查询
	public void vehicleStatusInfoQuery(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.getSession().get(Constant.LOGON_USER);
			
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			
			//1.车辆属性
			Map<String,Object> vehicleInfo = VehicleInfoDAO.getVehicleInfo(vin);
			
			//2.库存状态变更日志
			List<Map<String,Object>> storageChangeList = VehicleInfoDAO.getStorageChangeList(vin);
			
			//3.车辆销售状态日志
			List<Map<String,Object>> salesList = VehicleInfoDAO.getSalesList(vin);
			
			//4.取得车辆订单号
			Map<String,Object> vehicleOrderNo = VehicleInfoDAO.getVehicleStatusSalesOrderNo(vin);
			 
			
			act.setOutData("vehicleInfo", vehicleInfo);
			act.setOutData("storageChangeList", storageChangeList);
			act.setOutData("salesList", salesList);
			act.setOutData("vehicleOrderNo", vehicleOrderNo);
			act.setForword(vehicleStatusInfoURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "详细车籍查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
