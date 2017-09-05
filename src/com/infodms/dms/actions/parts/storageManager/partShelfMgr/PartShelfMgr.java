package com.infodms.dms.actions.parts.storageManager.partShelfMgr;

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
import com.infodms.dms.dao.parts.storageManager.partShelfMgr.PartShelfMgrDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartLocationShelfPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


/**
 * 架维护
 * @date 2014-3-3
 * @author wucl
 *
 */
public class PartShelfMgr {

	public Logger logger = Logger.getLogger(PartShelfMgr.class);
	private final PartShelfMgrDao plmDao = PartShelfMgrDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	

	
	private final String PART_SHELF_MANAGE = "/jsp/parts/storageManager/partShelfMgr/partShelfMgr.jsp";
	private final String PART_SHELF_ADD = "/jsp/parts/storageManager/partShelfMgr/addPartShelf.jsp";
	
	/**
	 *  货架维护--初始化跳转页面
	 * @date 2014-3-3
	 * @author wucl
	 */
	public void init(){
		AclUserBean loginUser = (AclUserBean) act.getSession().get( Constant.LOGON_USER);
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
			
			act.setForword(this.PART_SHELF_MANAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "货架维护--初始化页面失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 货架维护--查询数据
	 * @date 2014-3-3
	 * @author wucl
	 */
	public void partShelfQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String ShelfCode = request.getParamValue("SHELF_CODE");
			String lineCode = request.getParamValue("LINE_CODE");
			String whId = request.getParamValue("WH_ID"); // 仓库ID
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("SHELF_CODE", ShelfCode);
			map.put("LINE_CODE", lineCode);
			map.put("WH_ID", whId);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = plmDao.getPartShelfQuery(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "货架维护--数据信息查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 货架维护--新增初始化跳转页面
	 * @date 2014-3-3
	 * @author wucl
	 */	
	public void addInit(){
		AclUserBean loginUser = (AclUserBean) act.getSession().get( Constant.LOGON_USER);
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
			
			act.setForword(this.PART_SHELF_ADD);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "货架维护--数据信息查询失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 货架维护--新增数据保存
	 * @date 2014-3-3
	 * @author wucl
	 */
	public void addSave(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String ShelfCode =  CommonUtils.checkNull(request.getParamValue("SHELF_CODE")); //货架代码
			String lineId =  CommonUtils.checkNull(request.getParamValue("LINE_ID")); //排代码
			String whId =  CommonUtils.checkNull(request.getParamValue("WH_ID")); //排代码
			
			int maxCode = plmDao.getMaxCode("TT_PART_LOCATION_SHELF", "LINE_ID", lineId, "SHELF_CODE");
			int addNum = Integer.parseInt(ShelfCode);
			List<PO> addList = new ArrayList<PO>();
			for(int i=maxCode+1; i<=(maxCode+addNum); i++){
				TtPartLocationShelfPO ShelfPO = new TtPartLocationShelfPO();
				String ShelfId = SequenceManager.getSequence(null);//货架序列
				ShelfPO.setShelfId(Long.parseLong(ShelfId));
				ShelfPO.setLineId(Long.parseLong(lineId));
				ShelfPO.setWhId(Long.parseLong(whId));
				ShelfPO.setShelfCode(i+"");
				ShelfPO.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
				ShelfPO.setCreateBy(logonUser.getUserId());
				ShelfPO.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
				ShelfPO.setUpdateBy(logonUser.getUserId());			
				ShelfPO.setStatus(1);
				ShelfPO.setState(Constant.STATUS_ENABLE);
				addList.add(ShelfPO);
			}
			plmDao.insert(addList);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 新增货架信息失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}	
}
