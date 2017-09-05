package com.infodms.dms.actions.parts.storageManager.partPositionMgr;

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
import com.infodms.dms.dao.parts.storageManager.partPositionMgr.PartPositionMgrDao;
import com.infodms.dms.dao.parts.storageManager.partShelfMgr.PartShelfMgrDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartLocationPO;
import com.infodms.dms.po.TtPartLocationPositionPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


/**
 * 位维护
 * @date 2014-3-3
 * @author wucl
 *
 */
public class PartPositionMgr {

	public Logger logger = Logger.getLogger(PartPositionMgr.class);
	private final PartPositionMgrDao plmDao = PartPositionMgrDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	

	
	private final String PART_POSITION_MANAGE = "/jsp/parts/storageManager/partPositionMgr/partPositionMgr.jsp";
	private final String PART_POSITION_ADD = "/jsp/parts/storageManager/partPositionMgr/addPartPosition.jsp";
	
	/**
	 *  位维护--初始化跳转页面
	 * @date 2014-3-3
	 * @author wucl
	 */
	public void init(){
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
			
			act.setForword(this.PART_POSITION_MANAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "位维护--新增初始化页面失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 位维护--查询数据
	 * @date 2014-3-3
	 * @author wucl
	 */
	public void partPositionQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String whId = request.getParamValue("WH_ID");
			String lineID = request.getParamValue("LINE_ID");
			String shelfID = request.getParamValue("SHELF_ID");
			String floorID = request.getParamValue("FLOOR_ID"); 
			String positionCode = request.getParamValue("POSITION_CODE");

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("WH_ID", whId);
			map.put("LINE_ID", lineID);
			map.put("SHELF_ID", shelfID);
			map.put("FLOOR_ID", floorID);
			map.put("POSITION_CODE", positionCode);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = plmDao.getPartPositionQuery(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "位维护--数据信息查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 *位维护--新增初始化跳转页面
	 * @date 2014-3-3
	 * @author wucl
	 */	
	public void addInit(){
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
			
			act.setForword(this.PART_POSITION_ADD);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "位维护--新增初始化页面失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 位维护--新增数据保存
	 * @date 2014-3-3
	 * @author wucl
	 */
	public void addSave(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String whId =  CommonUtils.checkNull(request.getParamValue("WH_ID"));
			String lineID =  CommonUtils.checkNull(request.getParamValue("LINE_ID"));
			String shelfID =  CommonUtils.checkNull(request.getParamValue("SHELF_ID"));
			String floorID =  CommonUtils.checkNull(request.getParamValue("FLOOR_ID"));
			String positionCode =  CommonUtils.checkNull(request.getParamValue("POSITION_CODE"));
			
			int maxCode = PartShelfMgrDao.getInstance().getMaxCode("TT_PART_LOCATION_POSITION", "FLOOR_ID", floorID,"POSITION_CODE");
			int addNum = Integer.parseInt(positionCode);
			List<PO> addList = new ArrayList<PO>();
			List<PO> addListtplp = new ArrayList<PO>();
			String locCode = plmDao.getCodeSequenceList(lineID, shelfID, floorID);
			
			for(int i=maxCode+1; i<=(maxCode+addNum); i++){
				//位信息
				TtPartLocationPositionPO positionPO = new TtPartLocationPositionPO();
				String positionId = SequenceManager.getSequence(null);//位序列
				positionPO.setPositionId(Long.parseLong(positionId));
				positionPO.setPositionCode(i+"");
				positionPO.setFloorId(Long.parseLong(floorID));
				positionPO.setWhId(Long.parseLong(whId));
				positionPO.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
				positionPO.setCreateBy(logonUser.getUserId());
				positionPO.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
				positionPO.setUpdateBy(logonUser.getUserId());			
				positionPO.setStatus(Long.parseLong("1"));
				positionPO.setState(Constant.STATUS_ENABLE);
				addList.add(positionPO);
				
				TtPartLocationPO tplp = new TtPartLocationPO();
				String locId = SequenceManager.getSequence(null);
				tplp.setLocId(Long.parseLong(locId));
				tplp.setPositionId(Long.parseLong(positionId));
				String code = locCode + "-" + i;//生成货位编码
				tplp.setLocCode(code);
				tplp.setLocName(code);
				tplp.setWhId(Long.parseLong(whId));
				tplp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
				tplp.setCreateBy(logonUser.getUserId());
				tplp.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
				tplp.setUpdateBy(logonUser.getUserId());			
				tplp.setStatus(Long.parseLong("1"));
				tplp.setState(Constant.STATUS_ENABLE);
				addListtplp.add(tplp);
			}			
			plmDao.insert(addList);			
			plmDao.insert(addListtplp);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 新增货位信息失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
}
