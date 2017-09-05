/**
 * @Title: MaterialManage.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-1
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sysproduct.productmanage;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.productmanage.ProductManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVehicleModelPO;
import com.infodms.dms.po.TrVehicleModelPackagePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class VehicleModelManage {
	private Logger logger = Logger.getLogger(VehicleModelManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final String VEHICLE_MODEL_QUERY_URL = "/jsp/sysproduct/productmanage/vehicleModelManageQuery.jsp";
	private final String VEHICLE_MODEL_ADD = "/jsp/sysproduct/productmanage/vehicleModelAdd.jsp";
	private final String VEHICLE_MODEL_MODIFY = "/jsp/sysproduct/productmanage/vehicleModelModify.jsp";
	private final String VEHICLE_MODEL_DETAIL_QUERY = "/jsp/sysproduct/productmanage/vehicleModelDetail.jsp";
	private final ProductManageDao dao = ProductManageDao.getInstance();

	public void vehicleModelManageInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList1 = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
			List<Map<String, Object>> list = dao.getSeriesList();
			act.setOutData("list", list);
			act.setOutData("areaList", areaList1);
			act.setForword(VEHICLE_MODEL_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆型号维护初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void getVehicleModelList(){
		AclUserBean logonUser  = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String series = CommonUtils.checkNull(request.getParamValue("series"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String materialName = CommonUtils.checkNull(request.getParamValue("materialName"));
			StringBuffer sql  =  new StringBuffer();
			sql.append("SELECT A.VEHICLE_MODEL_ID,A.VEHICLE_MODEL_CODE,A.VEHICLE_MODEL_NAME,B.GROUP_NAME FROM TM_VEHICLE_MODEL A,TM_VHCL_MATERIAL_GROUP B WHERE A.SERIES_ID = B.GROUP_ID  ");
			if(!"".equals(materialCode)){
				sql.append(" AND A.VEHICLE_MODEL_CODE LIKE '%"+materialCode+"%'");
			}
			if(!"".equals(materialName)){
				sql.append(" AND A.VEHICLE_MODEL_NAME LIKE '%"+materialName+"%'");
			}
			if(!"".equals(series)){
				sql.append(" AND A.SERIES_ID = "+series+"\n");
			}
			List<Map<String, Object>> result = dao.pageQuery(sql.toString(), null, dao.getFunName());
			PageResult<Map<String, Object>> ps = new PageResult<Map<String,Object>>();
			ps.setRecords(result);
			ps.setPageSize(Constant.PAGE_SIZE_MAX);
			ps.setCurPage(1);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆型号");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 新增初始化
	 */
	public void addVehicleModelInit(){
		AclUserBean logonUser  = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> seriesList = dao.getSeriesList();
			act.setOutData("list", seriesList);
			act.setForword(VEHICLE_MODEL_ADD);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆型号新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 修改初始化
	 */
	public void modifyVehicleModelInit(){
		AclUserBean logonUser  = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id = request.getParamValue("id");
			TmVehicleModelPO po = new TmVehicleModelPO();
			po.setVehicleModelId(Long.valueOf(id));
			po = (TmVehicleModelPO)dao.select(po).get(0);
			String sql = "SELECT B.GROUP_ID,B.GROUP_CODE,B.GROUP_NAME FROM TR_VEHICLE_MODEL_PACKAGE A, TM_VHCL_MATERIAL_GROUP B WHERE A.VEHICLE_MODEL_ID = "+id+" AND A.GROUP_ID = B.GROUP_ID";
			List<Map<String, Object>> list = dao.pageQuery(sql, null, dao.getFunName());
			List<Map<String, Object>> seriesList = dao.getSeriesList1(po.getSeriesId());
			act.setOutData("seriesName", seriesList.get(0).get("GROUP_NAME"));
			act.setOutData("po", po);
			act.setOutData("list", list);
			act.setForword(VEHICLE_MODEL_MODIFY);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆型号修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 新增操作
	 */
	public void addVehicleModel(){
		AclUserBean logonUser  = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));
			String modelName = CommonUtils.checkNull(request.getParamValue("modelName"));
			String series = CommonUtils.checkNull(request.getParamValue("series"));
			String[] packageIds = request.getParamValues("packageIds");
			String value = "";
			TmVehicleModelPO po_1 = new TmVehicleModelPO();
			po_1.setVehicleModelCode(modelCode);
			List list = dao.select(po_1);
			
			TmVehicleModelPO po_2 = new TmVehicleModelPO();
			po_2.setVehicleModelCode(modelName);
			List list_ = dao.select(po_2);
			
			if((list != null && list.size()>0) || (list_ != null && list_.size()>0) ){
				value = "1";
			}else{
				TmVehicleModelPO vmpo = new TmVehicleModelPO();
				vmpo.setVehicleModelCode(modelCode);
				vmpo.setVehicleModelName(modelName);
				vmpo.setSeriesId(Long.valueOf(series));
				String id = SequenceManager.getSequence("");
				vmpo.setVehicleModelId(Long.valueOf(id));
				dao.insert(vmpo);
				if(packageIds != null && packageIds.length>0){
					for(int i = 0 ; i < packageIds.length ; i++){
						String pid = packageIds[i];
						TrVehicleModelPackagePO vmppo = new TrVehicleModelPackagePO();
						vmppo.setGroupId(Long.valueOf(pid));
						vmppo.setVehicleModelId(Long.valueOf(id));
						vmppo.setId(Long.valueOf(SequenceManager.getSequence("")));
						dao.insert(vmppo);
					}
				}
				value = "2";
			}
			act.setOutData("value", value);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆型号新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改
	 */
	public void modifyVehicleModel(){
		AclUserBean logonUser  = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String modelCode = CommonUtils.checkNull(request.getParamValue("vehicleModelCode"));
			String modelName = CommonUtils.checkNull(request.getParamValue("vehicleModelName"));
			String[] packageIds = request.getParamValues("packageIds");
			String vehicleModelId = request.getParamValue("vehicleModelId");
			String value = "";
			TmVehicleModelPO po_1 = new TmVehicleModelPO();
			String sql_code = "SELECT * FROM TM_VEHICLE_MODEL WHERE VEHICLE_MODEL_ID <> "+vehicleModelId+" AND VEHICLE_MODEL_CODE = '"+modelCode+"'";
			List list = dao.pageQuery(sql_code, null, dao.getFunName());
			
			TmVehicleModelPO po_2 = new TmVehicleModelPO();
			String sql_name = "SELECT * FROM TM_VEHICLE_MODEL WHERE VEHICLE_MODEL_ID <> "+vehicleModelId+" AND VEHICLE_MODEL_NAME = '"+modelName+"'";
			List list_ = dao.pageQuery(sql_name, null, dao.getFunName());
			
			if((list != null && list.size()>0) || (list_ != null && list_.size()>0) ){
				value = "1";
			}else{
				TmVehicleModelPO vmpo = new TmVehicleModelPO();
				vmpo.setVehicleModelCode(modelCode);
				vmpo.setVehicleModelName(modelName);
				TmVehicleModelPO p_1 = new TmVehicleModelPO();
				p_1.setVehicleModelId(Long.valueOf(vehicleModelId));
//				String id = SequenceManager.getSequence("");
//				vmpo.setVehicleModelId(Long.valueOf(id));
				dao.update(p_1,vmpo);
				if(packageIds != null && packageIds.length>0){
					String sql = "DELETE FROM TR_VEHICLE_MODEL_PACKAGE WHERE VEHICLE_MODEL_ID = "+vehicleModelId;
					dao.delete(sql, null);
					for(int i = 0 ; i < packageIds.length ; i++){
						String pid = packageIds[i];
						TrVehicleModelPackagePO vmppo = new TrVehicleModelPackagePO();
						vmppo.setGroupId(Long.valueOf(pid));
						vmppo.setVehicleModelId(Long.valueOf(vehicleModelId));
						vmppo.setId(Long.valueOf(SequenceManager.getSequence("")));
						dao.insert(vmppo);
					}
				}
				value = "2";
			}
			act.setOutData("value", value);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆型号修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryVehicleModelInit(){
		AclUserBean logonUser  = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id = request.getParamValue("id");
			TmVehicleModelPO po = new TmVehicleModelPO();
			po.setVehicleModelId(Long.valueOf(id));
			po = (TmVehicleModelPO)dao.select(po).get(0);
			String sql = "SELECT B.GROUP_ID,B.GROUP_CODE,B.GROUP_NAME FROM TR_VEHICLE_MODEL_PACKAGE A, TM_VHCL_MATERIAL_GROUP B WHERE A.VEHICLE_MODEL_ID = "+id+" AND A.GROUP_ID = B.GROUP_ID";
			List<Map<String, Object>> list = dao.pageQuery(sql, null, dao.getFunName());
			List<Map<String, Object>> seriesList = dao.getSeriesList1(po.getSeriesId());
			act.setOutData("seriesName", seriesList.get(0).get("GROUP_NAME"));
			act.setOutData("po", po);
			act.setOutData("list", list);
			act.setForword(VEHICLE_MODEL_DETAIL_QUERY);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆型号修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 删除
	 */
	public void deletedVehicleModel(){
		AclUserBean logonUser  = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id = request.getParamValue("id");
			TrVehicleModelPackagePO po = new TrVehicleModelPackagePO();
			po.setVehicleModelId(Long.valueOf(id));
			dao.delete(po);
			TmVehicleModelPO mpo = new TmVehicleModelPO();
			mpo.setVehicleModelId(Long.valueOf(id));
			dao.delete(mpo);
			act.setForword(VEHICLE_MODEL_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.FAILURE_CODE, "车辆型号删除");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 新增或修改车辆型号时，验证配置是否已经存在
	 */
	public void checkPackage(){
		AclUserBean logonUser  = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String pid = request.getParamValue("pid");
			String vehicleModelId = CommonUtils.checkNull(request.getParamValue("vehicleModelId"));
			String sql = "SELECT B.VEHICLE_MODEL_NAME,C.GROUP_CODE,C.GROUP_NAME FROM TR_VEHICLE_MODEL_PACKAGE A, TM_VEHICLE_MODEL B , TM_VHCL_MATERIAL_GROUP C WHERE A.VEHICLE_MODEL_ID = B.VEHICLE_MODEL_ID AND A.GROUP_ID = C.GROUP_ID AND  A.GROUP_ID IN ("+pid+")";
			if(!"".equals(vehicleModelId)){
				sql = sql + "AND A.VEHICLE_MODEL_ID <> "+vehicleModelId+"\n";
			}
			List<Map<String, Object>> list = dao.pageQuery(sql, null, dao.getFunName());
			if(list != null && list.size() > 0){
				String str = "";
				for(int i = 0 ; i < list.size() ; i++){
					Map<String, Object> map = list.get(i);
					String groupCode = map.get("GROUP_CODE").toString();
					String vehicleModelName = map.get("VEHICLE_MODEL_NAME").toString();
					str += "配置: " + groupCode + "  在车辆型号: " + vehicleModelName + "  中已经存在!</br>";
				}
				act.setOutData("returnValue", str);
			}else{
				act.setOutData("returnValue", "1");
			}
			
			act.setForword(VEHICLE_MODEL_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.FAILURE_CODE, "配置验证");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
