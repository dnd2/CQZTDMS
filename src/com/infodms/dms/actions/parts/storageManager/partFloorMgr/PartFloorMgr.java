package com.infodms.dms.actions.parts.storageManager.partFloorMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.storageManager.partFloorMgr.PartFloorMgrDao;
import com.infodms.dms.dao.parts.storageManager.partShelfMgr.PartShelfMgrDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartLocationFloorPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


/**
 * 层维护
 * @date 2014-3-3
 * @author wucl
 *
 */
public class PartFloorMgr {

	public Logger logger = Logger.getLogger(PartFloorMgr.class);
	private final PartFloorMgrDao plmDao = PartFloorMgrDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	

	
	private final String PART_FLOOR_MANAGE = "/jsp/parts/storageManager/partFloorMgr/partFloorMgr.jsp";
	private final String PART_FLOOR_ADD = "/jsp/parts/storageManager/partFloorMgr/addPartFloor.jsp";
	
	/**
	 *  层维护--初始化跳转页面
	 * @date 2014-3-3
	 * @author wucl
	 */
	public void init(){
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orgId = "";
			PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
			List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
			if (null != beanList && beanList.size() >= 0) {
				orgId = beanList.get(0).getOrgId() + "";
			}
			PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
			List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(orgId);
			request.setAttribute("wareHouseList", wareHouseList);
			
			act.setForword(this.PART_FLOOR_MANAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "层维护--初始化页面失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 层维护--查询数据
	 * @date 2014-3-3
	 * @author wucl
	 */
	public void partFloorQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String line_id =  CommonUtils.checkNull(request.getParamValue("LINE_ID"));
			String shelf_id =  CommonUtils.checkNull(request.getParamValue("SHELF_CODE")); 
			String floor_code =  CommonUtils.checkNull(request.getParamValue("FLOOR_CODE"));
			String whId = request.getParamValue("WH_ID"); // 仓库ID
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("LINE_ID", line_id);
			map.put("WH_ID", whId);
			map.put("SHELF_ID", shelf_id);
			map.put("FLOOR_CODE", floor_code);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = plmDao.getPartFloorQuery(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "货位管理--层维护--数据信息查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 层维护--新增初始化跳转页面
	 * @date 2014-3-3
	 * @author wucl
	 */	
	public void addInit(){
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orgId = "";
			PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
			List<OrgBean> beanList  = partWareHouseDao.getOrgInfo(loginUser);
			if( null != beanList && beanList.size() >= 0 ){
				orgId = beanList.get(0).getOrgId()+"";
			}
			PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
			List<Map<String,Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(orgId);
			request.setAttribute("wareHouseList", wareHouseList);
			
			act.setForword(this.PART_FLOOR_ADD);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "层维护--新增初始化页面失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 层维护--新增数据保存
	 * @date 2014-3-3
	 * @author wucl
	 */
	public void addSave(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String floor_code =  CommonUtils.checkNull(request.getParamValue("FLOOR_CODE"));
			String shelfId =  CommonUtils.checkNull(request.getParamValue("SHELF_ID"));
			String whId =  CommonUtils.checkNull(request.getParamValue("WH_ID"));
			
			int maxCode = PartShelfMgrDao.getInstance().getMaxCode("TT_PART_LOCATION_FLOOR", "SHELF_ID", shelfId,"FLOOR_CODE");
			int addNum = Integer.parseInt(floor_code);
			List<PO> addList = new ArrayList<PO>();
			for(int i=maxCode+1; i<=(maxCode+addNum); i++){
				//层信息
				TtPartLocationFloorPO FloorPO = new TtPartLocationFloorPO();
				String FloorId = SequenceManager.getSequence(null);//层序列
				FloorPO.setFloorId(Long.parseLong(FloorId));
				FloorPO.setFloorCode(i+"");
				FloorPO.setShelfId(Long.parseLong(shelfId));
				FloorPO.setWhId(Long.parseLong(whId));
				FloorPO.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
				FloorPO.setCreateBy(logonUser.getUserId());
				FloorPO.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
				FloorPO.setUpdateBy(logonUser.getUserId());			
				FloorPO.setStatus(1);
				FloorPO.setState(Constant.STATUS_ENABLE);
				addList.add(FloorPO);
			}
			plmDao.insert(addList);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"新增层信息失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	public void getSubCode(){
		String table =  CommonUtils.checkNull(request.getParamValue("table"));
		String column =  CommonUtils.checkNull(request.getParamValue("column"));
		String parentId =  CommonUtils.checkNull(request.getParamValue("parentId"));
		List<Map<String,Object>> rs = plmDao.getSubCodeList(table, column, parentId);
		act.setOutData("subcode", rs);
	}
}
