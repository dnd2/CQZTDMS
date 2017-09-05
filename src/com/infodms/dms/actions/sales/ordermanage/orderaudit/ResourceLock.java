package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.audit.ResourceLockDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.ordermanage.resourceQuery.ResourceQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ResourceLock extends BaseDao<PO>{
	public Logger logger = Logger.getLogger(ResourceLock.class);
	private ActionContext act = ActionContext.getContext();
	private static final ResourceLockDao dao = new ResourceLockDao ();
	private final String  resourceQueryInitUrl = "/jsp/sales/ordermanage/orderaudit/resourceLockInit.jsp";
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * FUNCTION		:	所有车辆资源查询面初始化
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void resourceLockInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(resourceQueryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * FUNCTION		:	所有车辆资源查询:结果展示
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void resourceQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String resStatus = CommonUtils.checkNull(request.getParamValue("resStatus"));		//资源状态
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));						//VIN
			String materialCode =  CommonUtils.checkNull(request.getParamValue("materialCode"));   //物料代码
			Map<String ,String > map=new HashMap<String,String>();
			map.put("resStatus", resStatus);
			map.put("vin", vin);
			map.put("materialCode", materialCode);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize!=null?pageSize:"10";
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getResourceQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "所有车辆资源查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * FUNCTION		:	所有车辆资源查询:锁定车辆 修改车辆的锁定状态
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void lockVechile(){
		RequestWrapper request = act.getRequest();
		String vehicle_id=CommonUtils.checkNull(request.getParamValue("vehicleId"));
		String flag=CommonUtils.checkNull(request.getParamValue("flag"));
		try{
				TmVehiclePO tv =new TmVehiclePO();
				tv.setVehicleId(new Long(vehicle_id));
				TmVehiclePO tv1=new TmVehiclePO();
				if("1".equals(flag)){
					tv1.setLockStatus(Constant.LOCK_STATUS_12);
				}else{
					tv1.setLockStatus(Constant.LOCK_STATUS_01);
				}
				int a=dao.update(tv, tv1);
				if(a==1){
					act.setOutData("flag", 1);
				}else{
					act.setOutData("flag", 0);
				}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "所有车辆资源查询");
			act.setException(e1);
		}
			
	}
	
}
