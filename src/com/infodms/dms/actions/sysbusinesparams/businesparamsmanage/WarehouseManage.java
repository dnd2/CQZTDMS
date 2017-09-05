package com.infodms.dms.actions.sysbusinesparams.businesparamsmanage;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sysbusinesparams.businesparamsmanage.WarehouseManageDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVsAddressPO;
import com.infodms.dms.po.TmWarehousePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : StorageManage.java
 * @Package: com.infodms.dms.actions.sysbusinesparams.businesparamsmanage
 * @Description: 仓库信息维护
 * @date   : 2010-7-5 
 * @version: V1.0   
 */
public class WarehouseManage extends BaseDao{
	public Logger logger = Logger.getLogger(WarehouseManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final WarehouseManage dao = new WarehouseManage ();
	public static final WarehouseManage getInstance() {
		return dao;
	}
	
	private final String  warehouseManageInit = "/jsp/sysbusinesparams/businesparamsmanage/warehouseManageInit.jsp";
	private final String  warehouseInfoURL = "/jsp/sysbusinesparams/businesparamsmanage/warehouseInfo.jsp";
	
	
	/** 
	* @Title	  : VehicleDispatchInit 
	* @Description: 仓库信息维护页面初始化
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public void warehouseManageInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.setForword(warehouseManageInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "仓库信息维护页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	
	/** 
	* @Title	  : warehouseManageList 
	* @Description: 仓库信息列表
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public void warehouseManageList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String warehouseName =  CommonUtils.checkNull(request.getParamValue("warehouseName"));
			String warehouseType =  CommonUtils.checkNull(request.getParamValue("warehouseType"));
			Long companyId = logonUser.getCompanyId();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = WarehouseManageDAO.getwarehouseManageList(companyId,warehouseName, warehouseType, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "仓库信息列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : toEditwarehouseInfo 
	* @Description: 修改仓库信息PRE
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public void toEditwarehouseInfo(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String warehouse_id = CommonUtils.checkNull(request.getParamValue("warehouse_id"));
			Map<String,Object> warehouseInfo = WarehouseManageDAO.getWarehouseInfo(warehouse_id);
			act.setOutData("warehouseInfo", warehouseInfo);
			act.setForword(warehouseInfoURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "修改仓库信息PRE");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : warehouseEditSubmit 
	* @Description: 仓库信息修改提交
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public void warehouseEditSubmit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String warehouse_id = CommonUtils.checkNull(request.getParamValue("warehouse_id"));
			String warehouseCode = CommonUtils.checkNull(request.getParamValue("warehouseCode"));
			String warehouseName = CommonUtils.checkNull(request.getParamValue("warehouseName"));
			String warehouseType = CommonUtils.checkNull(request.getParamValue("warehouseType"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String warehouseLevel = CommonUtils.checkNull(request.getParamValue("warehouseLevel"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			if (null != warehouse_id && !"".equals(warehouse_id)) {
				TmWarehousePO tempPO  = new TmWarehousePO();
				tempPO.setWarehouseId(Long.parseLong(warehouse_id));
				
				TmWarehousePO valuePO  = new TmWarehousePO();
				valuePO.setWarehouseCode(warehouseCode.trim());
				valuePO.setWarehouseName(warehouseName.trim());
				valuePO.setWarehouseType(Integer.parseInt(warehouseType));
				valuePO.setStatus(Integer.parseInt(status));
				valuePO.setWarehouseLevel(Integer.parseInt(warehouseLevel));
				valuePO.setUpdateDate(new Date());
				valuePO.setUpdateBy(logonUser.getUserId());
				if(dealerId != null && !"".equals(dealerId)) {
					valuePO.setDealerId(Long.parseLong(dealerId)) ;
				}
				dao.update(tempPO, valuePO);
			}
			
			act.setForword(warehouseManageInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "仓库信息修改提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : toAddWarehouseInfo 
	* @Description: 新增仓库信息PRE
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public void toAddWarehouseInfo(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.setForword(warehouseInfoURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "新增仓库信息PRE");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : warehouseAddSubmit 
	* @Description: 新增仓库信息：提交
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public void warehouseAddSubmit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String warehouseCode = CommonUtils.checkNull(request.getParamValue("warehouseCode"));
			String warehouseName = CommonUtils.checkNull(request.getParamValue("warehouseName"));
			String warehouseType = CommonUtils.checkNull(request.getParamValue("warehouseType"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String warehouseLevel = CommonUtils.checkNull(request.getParamValue("warehouseLevel"));
			
			TmWarehousePO tmWarehousePO  = new TmWarehousePO();
			tmWarehousePO.setWarehouseId(Long.parseLong(SequenceManager.getSequence("")));
			tmWarehousePO.setWarehouseCode(warehouseCode.trim());
			tmWarehousePO.setWarehouseName(warehouseName.trim());
			tmWarehousePO.setWarehouseType(Integer.parseInt(warehouseType));
			tmWarehousePO.setCompanyId(logonUser.getCompanyId());
			tmWarehousePO.setStatus(Integer.parseInt(status));
			tmWarehousePO.setWarehouseLevel(Integer.parseInt(warehouseLevel));
			tmWarehousePO.setCreateDate(new Date());
			tmWarehousePO.setCreateBy(logonUser.getUserId());
			if(dealerId != null && !"".equals(dealerId)) {				
				tmWarehousePO.setDealerId(Long.parseLong(dealerId)) ;
			}
			dao.insert(tmWarehousePO);
			act.setForword(warehouseManageInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "仓库信息修改提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : validateDealerAddress 
	* @Description: 验证经销商地址是否存在
	* @throws 
	* @LastUpdate :2012-11-16
	*/
	public void validateDealerAddress() {
		AclUserBean logonUser = null;
		boolean isExistAddress = false;
		try {
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			if(dealerId == null || "".equals(dealerId)) {
				isExistAddress = true;
			}
			if(dealerId != null && !"".equals(dealerId)) {
				//TODO 判断经销商地址
				Map<String, Object> result = WarehouseManageDAO.getAddressByDealerId(dealerId);	
				if(result != null && result.size() != 0) {
					for(int i=0; i<result.size(); i++) {
						String address = (String) result.get("ADDRESS");
						if(address != null && !"".equals(address)) {
							isExistAddress = true;
							break;
						}
					}
				}
			}
			act.setOutData("isExistAddress", isExistAddress);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址验证");
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
