package com.infodms.dms.actions.sales.storage.storagemanage;

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
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
import com.infodms.dms.dao.sales.storage.storagemanage.VehicleSiteDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 车辆库龄查询
 * @author syh
 * 2017-7-12
 */
public class StorageTimeManage {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final VehicleSiteDao reDao = VehicleSiteDao.getInstance();
	private final String reportQuery = "/jsp/sales/storage/storagemanage/storageTimeManage/storageTimeQuery.jsp";


	/**
	 * 查询初始化
	 * 
	 */
	public void queryInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getWarehouseByPoseId(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			act.setForword(reportQuery);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"车辆库龄查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询
	 * 
	 */
	public void storageDaysQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try {
			String _type = CommonUtils.checkNull(request.getParamValue("_type")); // 处理类型(2代表导出)
			/******************************页面查询字段start**************************/
			String YIELDLY = CommonUtils.checkNull(request.getParamValue("YIELDLY")); //仓库
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));//车系
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));//物料
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));//车型
			String packageCode = CommonUtils.checkNull(request.getParamValue("packageCode"));//配置
			String orgStartDate = CommonUtils.checkNull(request.getParamValue("ORG_STORAGE_STARTDATE")); // 入库日期开始
			String orgEndDate = CommonUtils.checkNull(request.getParamValue("ORG_STORAGE_ENDDATE")); // 入库日期结束
			String vin = CommonUtils.checkNull(request.getParamValue("VIN")); // vin
			String storageDays = CommonUtils.checkNull(request.getParamValue("storageDays"));//库龄
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("modelCode", modelCode);
			map.put("packageCode", packageCode);
			map.put("orgStartDate", orgStartDate);
			map.put("orgEndDate", orgEndDate);
			map.put("vin", vin);
			map.put("poseId", logonUser.getPoseId());
			map.put("YIELDLY", YIELDLY);	
			map.put("storageDays", storageDays);
			if("2".equals(_type)){//导出
				List<Map<String, Object>> list = reDao.getStorageDaysExport(map);
				String[] headExport={"库存状态","锁定状态","仓库","车型","配置","物料编码","颜色","VIN","入库日期","库龄"};
				String[] columns={"LIFE_CYCLE_NAME","LOCK_NAME","AREA_NAME","MODEL_NAME","PACKAGE_NAME","MATERIAL_CODE","COLOR_NAME","VIN","ORG_STORAGE_DATE","STORAGE_DAYS"};
				ToExcel.toReportExcel(act.getResponse(), request,"车辆库龄查询导出.xls", headExport,columns,list);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getStorageDaysQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆库龄查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
