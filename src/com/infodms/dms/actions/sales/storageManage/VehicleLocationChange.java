package com.infodms.dms.actions.sales.storageManage;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.repairOrder.WrRuleUtil;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.dao.sales.storageManage.VehicleLocationChangeDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtVsInspectionPO;
import com.infodms.dms.po.TtVsVhclChngPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class VehicleLocationChange extends BaseDao{
	public Logger logger = Logger.getLogger(VehicleLocationChange.class);
	private ActionContext act = ActionContext.getContext();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO ();
	public static final CheckVehicleDAO getInstance() {
		return dao;
	}
	private final String  VehicleLocationChangeUrl = "/jsp/sales/storageManage/vehicleLocationChange.jsp";
	
	/**
	 * FUNCTION		:	车辆位置变更页面初始化
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-27
	 */
	public void VehicleLocationChangeInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId() ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerId(comId.toString(),poseId.toString());
			String dealerIds__ = "";
			for(int i=0; i<areaList.size();i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
			}
			dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;				//当前用户职位对应的经销商ID
			List<Map<String,Object>> list = VehicleLocationChangeDAO.warehouseQuery(dealerIds__,"") ;
			act.setOutData("list", list) ;
			act.setForword(VehicleLocationChangeUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆位置变更页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	经销商仓库位置下拉列表
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-20
	 */
	public void warehouseList() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String warehouseType = act.getRequest().getParamValue("warehouseType") ;
			if (warehouseType == null || warehouseType.equals("")) {
				warehouseType = "" ;
			}
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId() ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerId(comId.toString(),poseId.toString());
			String dealerIds__ = "";
			for(int i=0; i<areaList.size();i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
			}
			dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;
			List<Map<String,Object>> list = VehicleLocationChangeDAO.warehouseQuery(dealerIds__,"") ;
			act.setOutData("list", list) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 更具车辆id获取对应经销商渠道的仓库
	 */
	public void getWareNew() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest() ;
			String vhclId = request.getParamValue("vhclId") ;
			String wareType = request.getParamValue("warehouseType") ;
			
			List<Map<String, Object>> wareList__A = VehicleLocationChangeDAO.getWareNew(vhclId, wareType) ;
			
			act.setOutData("wareList__A", wareList__A) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "获取仓库");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	车辆位置变更：查询展示
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-27
	 */
	public void VehicleLocationChangeQuery(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String materialCode__ = CommonUtils.checkNull(request.getParamValue("materialCode__")); //物料
			String days = CommonUtils.checkNull(request.getParamValue("days"));
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId() ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerId(comId.toString(),poseId.toString());
			String dealerIds__ = "";
			for(int i=0; i<areaList.size();i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
			}
			dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;			//当前用户职位对应的经销商ID
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = VehicleLocationChangeDAO.vehicleLocationChangeQuery(dealerIds__,materialCode,materialCode__,days,vin, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
			//act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆位置变更：查询展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	车辆位置变更：变更提交
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-27
	 */
	public void VehicleLocationChangeSubmit(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String ids = CommonUtils.checkNull(request.getParamValue("ids__"));
			String warehouseId = CommonUtils.checkNull(request.getParamValue("warehouse__")) ;
			// String vehicleAreas = CommonUtils.checkNull(request.getParamValue("newvehicleArea"));
			
			String[] ids__ = ids.split(",");
			if (null != ids__ && ids__.length>0) {
				for (int i = 0; i < ids__.length; i++) {
					
					TmVehiclePO tpo = new TmVehiclePO();
					tpo.setVehicleId(Long.parseLong(ids__[i]));
					
					TmVehiclePO vpo = new TmVehiclePO();
					
					if(warehouseId != null && !warehouseId.equals(""))
						vpo.setVehicleArea(warehouseId) ;
					// vpo.setVehicleArea(vehicleAreas.trim());

					dao.update(tpo, vpo);
					
					//向TT_VS_VHCL_CHNG写入变更日志
					TmVehiclePO tmVehiclePO = new TmVehiclePO();
					tmVehiclePO.setVehicleId(Long.parseLong(ids__[i]));
					List vehicleList = dao.select(tmVehiclePO);
					Long dealerID = ((TmVehiclePO)vehicleList.get(0)).getDealerId();
					TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
					Long vhclChangeId = Long.parseLong(SequenceManager.getSequence(""));
					chngPO.setVhclChangeId(vhclChangeId);				//改变序号
					chngPO.setVehicleId(Long.parseLong(ids__[i]));      //车辆ID
					chngPO.setOrgType(logonUser.getOrgType());			//组织类型
					chngPO.setOrgId(logonUser.getOrgId());				//组织ID
					chngPO.setDealerId(dealerID);						//经销商ID
					chngPO.setChangeCode(Constant.STORAGE_CHANGE_TYPE);	//改变类型:库存状态更改
					chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_05+"");//改变名称:车辆位置变更
					chngPO.setChangeDate(new Date());					//改变时间
					chngPO.setCreateDate(new Date());		
					chngPO.setCreateBy(logonUser.getUserId());
					dao.insert(chngPO);
				}
			}
			act.setOutData("returnValue", 1);
			act.setForword(VehicleLocationChangeUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆位置变更：变更提交");
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
