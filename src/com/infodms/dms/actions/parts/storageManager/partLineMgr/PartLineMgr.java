package com.infodms.dms.actions.parts.storageManager.partLineMgr;

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
import com.infodms.dms.dao.parts.storageManager.partLineMgr.PartLineMgrDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartLocationLinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


/**
 * 排维护
 * @date 2014-3-3
 * @author wucl
 *
 */
public class PartLineMgr {

	public Logger logger = Logger.getLogger(PartLineMgr.class);
	private final PartLineMgrDao plmDao = PartLineMgrDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	

	
	private final String PART_LINE_MANAGE = "/jsp/parts/storageManager/partLineMgr/partLineMgr.jsp";
	private final String PART_LINE_ADD = "/jsp/parts/storageManager/partLineMgr/addPartLine.jsp";
	private final String PART_LINE_EDIT = "/jsp/parts/storageManager/partLineMgr/editPartLine.jsp";
	
	/**
	 *  排维护--初始化跳转页面
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
			act.setForword(this.PART_LINE_MANAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "排维护--初始化页面失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 排维护--查询数据
	 * @date 2014-3-3
	 * @author wucl
	 */
	public void partLineQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String areaCode = request.getParamValue("LINE_CODE"); // 库区代码
			String areaName = request.getParamValue("LINE_NAME"); // 库区名称
			String whId = request.getParamValue("WH_ID"); // 仓库ID
			String type = request.getParamValue("TYPE"); // 库区类型

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("LINE_CODE", areaCode);
			map.put("LINE_NAME", areaName);
			map.put("WH_ID", whId);
			map.put("TYPE", type);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = plmDao.getPartLineQuery(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "排维护--数据信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 排维护--新增初始化跳转页面
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
			act.setForword(this.PART_LINE_ADD);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "排维护--新增初始化页面失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 货位管理--排维护--新增数据保存
	 * @date 2014-3-3
	 * @author wucl
	 */
	public void addSave(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String lineCode =  CommonUtils.checkNull(request.getParamValue("LINE_CODE")); //货位排代码
			String lineName =  CommonUtils.checkNull(request.getParamValue("LINE_NAME")); //货位排名称
			String dutyPer =  CommonUtils.checkNull(request.getParamValue("DUTY_PER"));  //责任人
			String dutyTel =  CommonUtils.checkNull(request.getParamValue("DUTY_TEL")); //责任人电话
			String type = CommonUtils.checkNull(request.getParamValue("TYPE")); //库区类型
			String wh_id = CommonUtils.checkNull(request.getParamValue("WH_ID")); //库房ID
			//判断货位排代码是否有重复，重复不能添加
 			TtPartLocationLinePO linePO = new TtPartLocationLinePO();
 			linePO.setLineCode(lineCode);
			List<PO> list = plmDao.select(linePO);
			if(list!=null && list.size()>0){
				act.setOutData("returnValue", 3);//该货位排名称已被占用，请重新输入
				return ;
			}
			//排信息
			String lineId = SequenceManager.getSequence(null);//货位排序列
			linePO.setLineId(Long.parseLong(lineId));
			linePO.setLineCode(lineCode);
			linePO.setLineName(lineName);
			linePO.setDutyPer(dutyPer==""?null:dutyPer);
			linePO.setDutyTel(dutyTel==""?null:dutyTel);
			linePO.setType(Long.parseLong(type));
			linePO.setWhId(Long.parseLong(wh_id));
			linePO.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			linePO.setCreateBy(logonUser.getUserId());
			linePO.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			linePO.setUpdateBy(logonUser.getUserId());			
			linePO.setStatus(1);
			linePO.setState(Constant.STATUS_ENABLE);
			plmDao.addPartLine(linePO);
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 新增货位排信息失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 货位管理--排维护--修改页面
	 * @date 2014-3-3
	 * @author wucl
	 */
	public void edit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = request.getParamValue("Id");
			Map<String, Object> map = plmDao.getObjectById(id);
			act.setOutData("map", map);
			act.setForword(this.PART_LINE_EDIT);
		} catch (Exception e) {
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "排维护--修改页面信息初始化失败");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	/**
	 * 货位管理--排维护--修改页面保存方法
	 * @date 2014-3-3
	 * @author wucl
	 */
	public void editSave(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String lineId = CommonUtils.checkNull(request.getParamValue("LINE_ID"));
			String lineCode = CommonUtils.checkNull(request.getParamValue("LINE_CODE"));
			String lineName = CommonUtils.checkNull(request.getParamValue("LINE_NAME"));
			String dutyPer = CommonUtils.checkNull(request.getParamValue("DUTY_PER"));
			String dutyTel = CommonUtils.checkNull(request.getParamValue("DUTY_TEL")); 
			String type = CommonUtils.checkNull(request.getParamValue("TYPE")); 
			String state = CommonUtils.checkNull(request.getParamValue("STATE")); 
			//排信息
			TtPartLocationLinePO tapo=new TtPartLocationLinePO();		
			tapo.setLineId(Long.parseLong(lineId));
			tapo.setLineCode(lineCode);
			tapo.setLineName(lineName);
			tapo.setDutyPer(dutyPer==""?null:dutyPer);
			tapo.setDutyTel(dutyTel==""?null:dutyTel);
			tapo.setType(Long.parseLong(type));
			tapo.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			tapo.setCreateBy(logonUser.getUserId());
			tapo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			tapo.setUpdateBy(logonUser.getUserId());
			tapo.setStatus(1);
			tapo.setState(Integer.parseInt(state));
			//条件
			TtPartLocationLinePO seachpo=new TtPartLocationLinePO();
			seachpo.setLineId(Long.parseLong(lineId));
			plmDao.update(seachpo, tapo);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"排维护--修改保存时失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	
}
