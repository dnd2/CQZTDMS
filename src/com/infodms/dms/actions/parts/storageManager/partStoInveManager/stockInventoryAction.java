package com.infodms.dms.actions.parts.storageManager.partStoInveManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.infodms.dms.util.CheckUtil;
import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.storageManager.partStoInveManager.stockInventoryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartCheckDtlPO;
import com.infodms.dms.po.TtPartCheckMainPO;
import com.infodms.dms.po.TtPartItemStockPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 处理配件库存盘点业务
 * 
 * @Date: 2013-5-3
 * 
 * @author huhcao
 * @version 1.0
 * @remark
 */
public class stockInventoryAction extends BaseImport {
	public Logger logger = Logger.getLogger(stockInventoryAction.class);
	private static final stockInventoryDao dao = stockInventoryDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final int PAGE_SIZE = 20;
	private static final int type1 = Constant.PART_STOCK_INVE_TYPE_01;
	private static final int type2 = Constant.PART_STOCK_INVE_TYPE_02;
	private static final int state1 = Constant.PART_STOCK_INVE_STATE_01;//已保存
	private static final int state2 = Constant.PART_STOCK_INVE_STATE_02;//盘点中
	private static final int state3 = Constant.PART_STOCK_INVE_STATE_03;//已盘点
	private static final int status1 = Constant.PART_STATUS_EN_ABLE;//盘点单可用
	private static final int status2 = Constant.PART_STATUS_UN_ABLE;//盘点单不可用
	private static final int unLockedVal = Constant.PART_STATE_UN_LOCKED;  //配件未锁定
	private static final int lockedVal = Constant.PART_STATE_LOCKED;  //配件未锁定
	
	//配件库存盘点
	private static final String PART_STOCK_MAIN = "/jsp/parts/storageManager/partStoInveManager/partStockInventory/partStoInveMain.jsp";//配件库存盘点首页
	private static final String PART_STOCK_INVE_VIEW = "/jsp/parts/storageManager/partStoInveManager/partStockInventory/partStoInveView.jsp";//库存盘点查看页面
	private static final String PART_STOCK_INVE_MOD = "/jsp/parts/storageManager/partStoInveManager/partStockInventory/partStocInveModify.jsp";//库存盘点修改页面
	private static final String PART_STOCK_INVE_ADD = "/jsp/parts/storageManager/partStoInveManager/partStockInventory/partStocInveAdd.jsp";///库存盘点新增页面
	private static final String INPUT_ERROR_URL= "/jsp/parts/storageManager/partStoInveManager/partStockInventory/inputError.jsp";//数据导入出错页面
	private static final String PART_STOCK_PREV_INVE = "/jsp/parts/storageManager/partStoInveManager/partStockInventory/partStoPrevInve.jsp";//库存初盘页面
	private static final String PART_STOCK_PREV_INVE_PRT = "/jsp/parts/storageManager/partStoInveManager/partStockInventory/partStoPrevInvePrt.jsp";//库存初盘打印页面
	private static final String PART_STOCK_AGAIN_INVE = "/jsp/parts/storageManager/partStoInveManager/partStockInventory/partStoAgainInve.jsp";//库存复盘页面
	private static final String PART_STOCK_AGAIN_INVE_PRT = "/jsp/parts/storageManager/partStoInveManager/partStockInventory/partStoAgainInvePrt.jsp";//库存复盘页面

	/**
	 * 
	 * @Title : 跳转至配件库存盘点页面
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-5-3
	 */
	public void stockInventoryInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String parentOrgId = "";//父机构（销售单位）ID
			String parentOrgCode = "";//父机构（销售单位）编码
			String companyName = ""; //制单单位
			//判断主机厂与服务商
			String comp = logonUser.getOemCompanyId();
			if (null == comp ){
				
				parentOrgId = Constant.OEM_ACTIVITIES;
				parentOrgCode = Constant.ORG_ROOT_CODE;
				companyName = dao.getMainCompanyName(parentOrgId);
			}else {
				parentOrgId = logonUser.getDealerId();
				parentOrgCode = logonUser.getDealerCode();
				companyName = dao.getDealerName(parentOrgId);
			}
			StringBuffer sbString = new StringBuffer();
			sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
			List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());
			
			act.setOutData("parentOrgId", parentOrgId);
			act.setOutData("parentOrgCode", parentOrgCode);
			act.setOutData("companyName", companyName);
			act.setOutData("WHList", WHList);
			act.setForword(PART_STOCK_MAIN);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件库存盘点页面初始化失败!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 查询配件库存盘点信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	public void stockInventorySearch() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String changeCode = CommonUtils.checkNull(request
					.getParamValue("changeCode")); // 盘点单号
			String checkSDate = CommonUtils.checkNull(request
					.getParamValue("checkSDate")); // 开始时间
			String checkEDate = CommonUtils.checkNull(request
					.getParamValue("checkEDate")); // 截止时间
			String whId = CommonUtils.checkNull(request
					.getParamValue("whId")); // 仓库ID
			String inveName = CommonUtils.checkNull(request
					.getParamValue("inveName")); // 盘点人
			String inveType = CommonUtils.checkNull(request
					.getParamValue("inveType")); // 盘点类型
			String inveState = CommonUtils.checkNull(request
					.getParamValue("inveState")); // 盘点状态
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
			StringBuffer sbString = new StringBuffer();
			if(null != changeCode && !"".equals(changeCode))
			{
				sbString.append(" AND TM.CHANGE_CODE LIKE '%" + changeCode + "%' ");
			}
			if(null != inveName && !"".equals(inveName))
			{
				sbString.append(" AND U.NAME LIKE '%" + inveName + "%' ");
			}
			if(null != checkSDate && !"".equals(checkSDate))
			{
				sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
			}
			if(null != checkEDate && !"".equals(checkEDate))
			{
				sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
			}
			if(null != whId && !"".equals(whId))
			{
				sbString.append(" AND TM.WH_ID = '" + whId + "' ");
			}
			if(null != inveType && !"".equals(inveType))
			{
				sbString.append(" AND TM.CHECK_TYPE = '" + inveType + "' ");
			}
			if(null != inveState && !"".equals(inveState))
			{
				sbString.append(" AND TM.STATE = '" + inveState + "' ");
			}
			if(null != parentOrgId && !"".equals(parentOrgId))
			{
				sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartStockInve(
					sbString.toString(), Constant.PAGE_SIZE, curPage);

			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件库存盘点信息失败!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 跳转至库存盘点查看页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	public void viewStockDeatilInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));// 变更单ID
			String invType = CommonUtils.checkNull(request.getParamValue("invType"));// 盘点类型
			StringBuffer sbString = new StringBuffer();
			sbString.append(" AND TM.CHANGE_ID = '" + changeId + "' ");
			Map<String, Object> map = dao.queryAllPartStockInve(sbString.toString()).get(0);
			
			int type = Integer.parseInt(map.get("CHECK_TYPE").toString());
			int state = Integer.parseInt(map.get("STATE").toString());
			
			if(type1 == type)
			{
				map.put("CHECK_TYPE", "全部");
			}
			else if(type2 == type)
			{
				map.put("CHECK_TYPE", "动态");
			}
			
			if(state1 == state)
			{
				map.put("STATE", "已保存");
			}
			else if(state2 == state)
			{
				map.put("STATE", "盘点中");
			}
			else if(state3 == state)
			{
				map.put("STATE", "已盘点");
			}
			act.setOutData("invType", invType);
			act.setOutData("map", map);
			act.setForword(PART_STOCK_INVE_VIEW);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存盘点查看页面初始化失败!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 跳转至库存盘点新增页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	public void partStockAddInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String actionURL = CommonUtils.checkNull(request.getParamValue("actionURL"));// action地址
			String parentOrgId = "";//父机构（销售单位）ID
			String parentOrgCode = "";//父机构（销售单位）编码
			String companyName = ""; //制单单位
			//判断主机厂与服务商
			String comp = logonUser.getOemCompanyId();
			if (null == comp ){
				
				parentOrgId = Constant.OEM_ACTIVITIES;
				parentOrgCode = Constant.ORG_ROOT_CODE;
				companyName = dao.getMainCompanyName(parentOrgId);
			}else {
				parentOrgId = logonUser.getDealerId();
				parentOrgCode = logonUser.getDealerCode();
				companyName = dao.getDealerName(parentOrgId);
			}
			StringBuffer sbString = new StringBuffer();
			sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
			List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());
			
			String marker = logonUser.getName();
			String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_22);//获取盘点单号
			List<Map<String,String>> voList = null;
			
			act.setOutData("parentOrgId", parentOrgId);
			act.setOutData("parentOrgCode", parentOrgCode);
			act.setOutData("changeCode", changeCode);
			act.setOutData("actionURL", actionURL);
			act.setOutData("marker", marker);
			act.setOutData("companyName", companyName);
			act.setOutData("WHList", WHList);
			act.setOutData("list", voList);
			act.setForword(PART_STOCK_INVE_ADD);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存盘点新增页面初始化失败!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 跳转至库存盘点修改页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-4
	 */
	public void modifyStockDeatilInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));// 变更单ID
			StringBuffer sbString = new StringBuffer();
			sbString.append(" AND TM.CHANGE_ID = '" + changeId + "' ");
			Map<String, Object> map = dao.queryAllPartStockInve(sbString.toString()).get(0);
			String selWhId = map.get("WH_ID").toString();
			
			String parentOrgId = "";//父机构（销售单位）ID
			String parentOrgCode = "";//父机构（销售单位）编码
			String companyName = ""; //制单单位
			//判断主机厂与服务商
			String comp = logonUser.getOemCompanyId();
			if (null == comp ){
				
				parentOrgId = Constant.OEM_ACTIVITIES;
				parentOrgCode = Constant.ORG_ROOT_CODE;
				companyName = dao.getMainCompanyName(parentOrgId);
			}else {
				parentOrgId = logonUser.getDealerId();
				parentOrgCode = logonUser.getDealerCode();
				companyName = dao.getDealerName(parentOrgId);
			}
			StringBuffer sbStr = new StringBuffer();
			sbStr.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
			List<Map<String, Object>> WHList = dao.getWareHouses(sbStr.toString());
			
			String marker = logonUser.getName();
			List<Map<String,Object>> voList = dao.queryPartStockDeatil(type2, sbString.toString());
			
			act.setOutData("parentOrgId", parentOrgId);
			act.setOutData("parentOrgCode", parentOrgCode);
			act.setOutData("marker", marker);
			act.setOutData("companyName", companyName);
			act.setOutData("selectedWhId", selWhId);
			act.setOutData("WHList", WHList);
			act.setOutData("list", voList);
			act.setOutData("map", map);
			act.setForword(PART_STOCK_INVE_MOD);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存盘点修改页面初始化失败!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 跳转至初盘或复盘页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	public void prevOrAginInveInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));// 盘点单ID
			String optType = CommonUtils.checkNull(request.getParamValue("optType"));// 盘点种类
			String invType = CommonUtils.checkNull(request.getParamValue("invType"));// 盘点类型
			
			act.setOutData("changeId", changeId);
			act.setOutData("invType", invType);
			if("previous".equalsIgnoreCase(optType))
			{
				act.setForword(PART_STOCK_PREV_INVE);
			}
			else if("again".equalsIgnoreCase(optType))
			{
				act.setForword(PART_STOCK_AGAIN_INVE);
			}
			else if("prevPrint".equalsIgnoreCase(optType))
			{
				act.setForword(PART_STOCK_PREV_INVE_PRT);
			}
			else if("againPrint".equalsIgnoreCase(optType))
			{
				act.setForword(PART_STOCK_AGAIN_INVE_PRT);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存初盘或复盘页面初始化失败!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 显示仓库配件库存信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public void showPartStockBase(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		act.getResponse().setContentType("application/json");
		RequestWrapper req = act.getRequest();
		StringBuffer sbStr = new StringBuffer();
		try {
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构ID
			String whId = CommonUtils.checkNull(request.getParamValue("whId"));// 仓库ID
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));// 件号
			String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));// 配件编码
			String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));// 配件名称
			
			if(null != parentOrgId && !"".equals(parentOrgId))
			{
				sbStr.append(" AND TD.ORG_ID = '" + parentOrgId + "' ");
			}
			if(null != whId && !"".equals(whId))
			{
				sbStr.append(" AND TD.WH_ID = '" + whId + "' ");
			}
			if(null != partCode && !"".equals(partCode))
			{
				sbStr.append(" AND TD.PART_CODE LIKE '%" + partCode + "%' ");
			}
			if(null != partOldcode && !"".equals(partOldcode))
			{
				sbStr.append(" AND TD.PART_OLDCODE LIKE '%" + partOldcode + "%' ");
			}
			if(null != partCname && !"".equals(partCname))
			{
				sbStr.append(" AND TD.PART_CNAME LIKE '%" + partCname + "%' ");
			}
			
			sbStr.append(" AND TD.IS_LOCKED = '" + unLockedVal + "' "); //获取未锁定的配件
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.showPartStockBase(
					sbStr.toString(), Constant.PAGE_SIZE, curPage);
			
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"显示仓库配件库存信息失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 开始或者结束库存盘点
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	public void startOrEndStockInve(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		act.getResponse().setContentType("application/json");
		RequestWrapper req = act.getRequest();
		String errorExist = "";
    	try {
    		String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
    		if("".equals(curPage)){
    			curPage = "1";
    		}
    		String changeId = CommonUtils.checkNull(req.getParamValue("changeId")); //盘点单ID
    		String optionType = CommonUtils.checkNull(req.getParamValue("optionType")); //操作类型
    		int invType = Integer.parseInt(CommonUtils.checkNull(request.getParamValue("invType"))); // 盘点类型
    		
    		StringBuffer sbString = new StringBuffer();
    		if(null != changeId && !"".equals(changeId))
    		{
    			sbString.append(" AND TM.CHANGE_ID = '" + changeId + "' ");
    		}
    		
    		List<Map<String, Object>> prtStckInvList = dao.queryAllPartStockInve(sbString.toString());
    		List<Map<String, Object>> prtStckIdsList = dao.getPartStockIdsList(invType, sbString.toString());
    		
    		Long userId = logonUser.getUserId(); //操作人ID
    		Date date = new Date();
    		
    		if(null != prtStckInvList && prtStckInvList.size() == 1)
    		{
    			TtPartCheckMainPO selCMPo = null;
    			TtPartCheckMainPO updCMPo = null;
    			//对仓库配件锁定与解锁功能
    			TtPartItemStockPO selISPo = null;
    			TtPartItemStockPO updISPo = null;
    			String stckId = ""; 
    			
    			int state = Integer.parseInt(prtStckInvList.get(0).get("STATE").toString());//盘点单状态
    			String invCode = prtStckInvList.get(0).get("CHANGE_CODE").toString();//盘点单号
    			
    			
    			if("start".equalsIgnoreCase(optionType))
    			{
    				if(state1 == state)
    				{
    					if(null != prtStckIdsList)
    					{
    						selCMPo = new TtPartCheckMainPO();
    						updCMPo = new TtPartCheckMainPO();
    						
    						selCMPo.setChangeId(Long.parseLong(changeId));
    						
    						updCMPo.setUpdateBy(userId);
    						updCMPo.setUpdateDate(date);
    						updCMPo.setState(state2);
    						updCMPo.setStatus(status1);
    						
    						dao.update(selCMPo, updCMPo);
    						
    						//对仓库配件锁定功能
    						for(Map map : prtStckIdsList)
    						{
    							stckId = map.get("STOCK_ID").toString();
    							
    							selISPo = new TtPartItemStockPO();
    							updISPo = new TtPartItemStockPO();
    							
    							selISPo.setStockId(Long.parseLong(stckId));
    							
    							updISPo.setUpdateBy(userId);
    							updISPo.setUpdateDate(date);
    							updISPo.setIsLocked(lockedVal); //锁定
    							
    							dao.update(selISPo, updISPo);
    						}
    					}
    					else
    					{
    						errorExist = "操作失败,请联系管理员!";
    					}
    				}
    				else if(state2 == state)
    				{
    					errorExist = "盘点单号【"+ invCode +"】已开始盘点!";
    				}
    				else if(state3 == state)
    				{
    					errorExist = "盘点单号【"+ invCode +"】已结束盘点!";
    				}
    				else
    				{
    					errorExist = "操作失败,请联系管理员!";
    				}
    				
    			}
    			else if("end".equalsIgnoreCase(optionType))
    			{
    				if(state1 == state || state2 == state)
    				{
    					if(null != prtStckIdsList)
    					{
    						selCMPo = new TtPartCheckMainPO();
    						updCMPo = new TtPartCheckMainPO();
    						
    						selCMPo.setChangeId(Long.parseLong(changeId));
    						
    						updCMPo.setUpdateBy(userId);
    						updCMPo.setUpdateDate(date);
    						updCMPo.setState(state3);
    						updCMPo.setStatus(status2);
    						
    						dao.update(selCMPo, updCMPo);
    						
    						//对仓库配件解锁功能
    						for(Map map : prtStckIdsList)
    						{
    							stckId = map.get("STOCK_ID").toString();
    							
    							selISPo = new TtPartItemStockPO();
    							updISPo = new TtPartItemStockPO();
    							
    							selISPo.setStockId(Long.parseLong(stckId));
    							
    							updISPo.setUpdateBy(userId);
    							updISPo.setUpdateDate(date);
    							updISPo.setIsLocked(unLockedVal); //解锁
    							
    							dao.update(selISPo, updISPo);
    						}
    						
    						//是否 需要对 还在 盘点处理 中的单据 进行处理？
    					}
    				}
    				else if(state3 == state)
    				{
    					errorExist = "盘点单号【"+ invCode +"】已结束盘点!";
    				}
    				else
    				{
    					errorExist = "操作失败,请联系管理员!";
    				}
    			}
    		}
    		else
    		{
    			errorExist = "操作失败,请联系管理员!";
    		}
    		
    		act.setOutData("errorExist", errorExist);//返回错误信息
    		act.setOutData("success", "true");
    		act.setOutData("curPage", curPage);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"保存新增配件零售/领用出库信息失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 保存新增配件库存盘点信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	@SuppressWarnings("unchecked")
    public void saveStockInfos(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		act.getResponse().setContentType("application/json");
		RequestWrapper req = act.getRequest();
		try {
			String parentOrgId = CommonUtils.checkNull(req.getParamValue("parentOrgId")); //制单单位ID
			String parentOrgCode = CommonUtils.checkNull(req.getParamValue("parentOrgCode")); //制单单位编码
			String chgorgCname = CommonUtils.checkNull(req.getParamValue("chgorgCname")); //制单单位名称
			String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_22);//获取盘点单号
			Long userId = logonUser.getUserId(); //制单人ID
			String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
			String inveType = CommonUtils.checkNull(req.getParamValue("inveType")); //盘点类型
			String remark = CommonUtils.checkNull(req.getParamValue("remark")); //备注
			String[] partIdArr = req.getParamValues("cb");
			List<Map<String, Object>> partList = null;
			
			Date date = new Date();
			Long changeId = Long.parseLong(SequenceManager.getSequence(""));
			
			StringBuffer sbStr = new StringBuffer();
			if(null != whId && !"".equals(whId))
			{
				sbStr.append(" AND TM.WH_ID = '" + whId + "' ");
			}
			Map<String, Object> mapWH = dao.getWareHouses(sbStr.toString()).get(0);
			String whName = mapWH.get("WH_CNAME").toString();
			
			TtPartCheckMainPO inserCMPo = new TtPartCheckMainPO();
			
			inserCMPo.setChangeId(changeId);
			inserCMPo.setChangeCode(changeCode);
			inserCMPo.setChgorgId(Long.parseLong(parentOrgId));
			inserCMPo.setChgorgCode(parentOrgCode);
			inserCMPo.setChgorgCname(chgorgCname);
			inserCMPo.setWhId(Long.parseLong(whId));
			inserCMPo.setWhCname(whName);
			inserCMPo.setCheckType(Integer.parseInt(inveType));
			inserCMPo.setRemark(remark);
			inserCMPo.setCreateBy(userId);
			inserCMPo.setCreateDate(date);
			inserCMPo.setUpdateBy(userId);
			inserCMPo.setUpdateDate(date);
			inserCMPo.setState(Constant.PART_STOCK_INVE_STATE_01);//已保存
			
			dao.insert(inserCMPo);
			
			TtPartCheckDtlPO insertCDPo = null;
			
			if(null!= partIdArr)
			{
				for(int i =0 ;i<partIdArr.length;i++){   
					insertCDPo = new TtPartCheckDtlPO();
					
					String partAndLocId = partIdArr[i];
					String partId = partAndLocId.substring(0, partAndLocId.indexOf("_RNUM")); // 配件id
					Long dtlId = Long.parseLong(SequenceManager.getSequence("")); 
					String partCode = CommonUtils.checkNull(req.getParamValue("partCode_"+partAndLocId));//件号                                                                       
					String partOldcode =  CommonUtils.checkNull(req.getParamValue("partOldcode_"+partAndLocId));   //配件编码                                                         
					String partCname =  CommonUtils.checkNull(req.getParamValue("partCname_"+partAndLocId)); //配件名称
					String locId = CommonUtils.checkNull(req.getParamValue("locId_"+partAndLocId)); // 货位id
					String locCode = CommonUtils.checkNull(req.getParamValue("locCode_"+partAndLocId)); // 货位编码
					String locName = CommonUtils.checkNull(req.getParamValue("locName_"+partAndLocId)); // 货位名称
					String itemQty =  CommonUtils.checkNull(req.getParamValue("itemQty_"+partAndLocId)); //当前账面库存
					String unit =  CommonUtils.checkNull(req.getParamValue("unit_"+partAndLocId)); //单位
					String batchCode =  CommonUtils.checkNull(req.getParamValue("batchCode_"+partAndLocId)); //批次号
					String venderId =  CommonUtils.checkNull(req.getParamValue("venderId_"+partAndLocId)); //供应商id
					String deRemark =  CommonUtils.checkNull(req.getParamValue("remark_"+partAndLocId)); //明细备注
                    
					insertCDPo.setDtlId(dtlId);
					insertCDPo.setCheckId(changeId);
					insertCDPo.setPartId(Long.parseLong(partId));
					insertCDPo.setLineNo(Long.parseLong("0"));
					insertCDPo.setPartCode(partCode);
					insertCDPo.setPartOldcode(partOldcode);
					insertCDPo.setPartCname(partCname);
					insertCDPo.setLocId(Long.parseLong(locId));
					insertCDPo.setLocCode(locCode);
					insertCDPo.setLocName(locName);
					insertCDPo.setStockQty(Long.parseLong(itemQty));
					insertCDPo.setUnit(unit);
					insertCDPo.setBatchCode(batchCode);
					insertCDPo.setVenderId(Long.parseLong(venderId));
					insertCDPo.setRemark(deRemark);
					insertCDPo.setCreateBy(userId);
					insertCDPo.setCreateDate(date);
					
					dao.insert(insertCDPo);
				}
			}
			act.setOutData("success", "true");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"保存新增配件库存盘点信息失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
			act.setOutData("errorExist", "保存失败!");
		}
	}
	
	/**
	 * 
	 * @Title      : 保存修改的配件库存盘点信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-4
	 */
	@SuppressWarnings("unchecked")
    public void saveModifyStockInfos(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		act.getResponse().setContentType("application/json");
		RequestWrapper req = act.getRequest();
		try {
			Long changeId = Long.parseLong(CommonUtils.checkNull(req.getParamValue("changeId"))); //盘点单ID
			Long userId = logonUser.getUserId(); //修改人ID
			String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
			String inveType = CommonUtils.checkNull(req.getParamValue("inveType")); //盘点类型
			String remark = CommonUtils.checkNull(req.getParamValue("remark")); //备注
			String[] partIdArr = req.getParamValues("cb");
			List<Map<String, Object>> partList = null;
			
			Date date = new Date();
			
			StringBuffer sbStr = new StringBuffer();
			if(null != whId && !"".equals(whId))
			{
				sbStr.append(" AND TM.WH_ID = '" + whId + "' ");
			}
			Map<String, Object> mapWH = dao.getWareHouses(sbStr.toString()).get(0);
			String whName = mapWH.get("WH_CNAME").toString();
			
			TtPartCheckMainPO selCMPo = new TtPartCheckMainPO();
			TtPartCheckMainPO updCMPo = new TtPartCheckMainPO();
			
			selCMPo.setChangeId(changeId);
			
			updCMPo.setWhId(Long.parseLong(whId));
			updCMPo.setWhCname(whName);
			updCMPo.setCheckType(Integer.parseInt(inveType));
			updCMPo.setRemark(remark);
			updCMPo.setUpdateBy(userId);
			updCMPo.setUpdateDate(date);
			
			dao.update(selCMPo, updCMPo);
			
			TtPartCheckDtlPO delCDPo = new TtPartCheckDtlPO();
			
			delCDPo.setCheckId(changeId);
			
			dao.delete(delCDPo);
			
			Thread.sleep(2000);
			
			TtPartCheckDtlPO insertCDPo = null;
			
			if(null!= partIdArr)
			{
				for(int i =0 ;i<partIdArr.length;i++){   
					insertCDPo = new TtPartCheckDtlPO();
					
                    String partAndLocId = partIdArr[i];
                    String partId = partAndLocId.substring(0, partAndLocId.indexOf("_RNUM")); // 配件id                                                                                                   
					Long dtlId = Long.parseLong(SequenceManager.getSequence("")); 
					String partCode = CommonUtils.checkNull(req.getParamValue("partCode_"+partAndLocId));//件号                                                                       
					String partOldcode =  CommonUtils.checkNull(req.getParamValue("partOldcode_"+partAndLocId));   //配件编码                                                         
					String partCname =  CommonUtils.checkNull(req.getParamValue("partCname_"+partAndLocId)); //配件名称
                    String locId = CommonUtils.checkNull(req.getParamValue("locId_"+partAndLocId)); // 货位id
                    String locCode = CommonUtils.checkNull(req.getParamValue("locCode_"+partAndLocId)); // 货位编码
                    String locName = CommonUtils.checkNull(req.getParamValue("locName_"+partAndLocId)); // 货位名称
					String itemQty =  CommonUtils.checkNull(req.getParamValue("itemQty_"+partAndLocId)); //当前账面库存
					String unit =  CommonUtils.checkNull(req.getParamValue("unit_"+partAndLocId)); //单位
                    String batchCode =  CommonUtils.checkNull(req.getParamValue("batchCode_"+partAndLocId)); //批次号
                    String venderId =  CommonUtils.checkNull(req.getParamValue("venderId_"+partAndLocId)); //供应商id
					String deRemark =  CommonUtils.checkNull(req.getParamValue("remark_"+partAndLocId)); //明细备注
					
					insertCDPo.setDtlId(dtlId);
					insertCDPo.setCheckId(changeId);
					insertCDPo.setPartId(Long.parseLong(partId));
					insertCDPo.setLineNo(Long.parseLong("0"));
					insertCDPo.setPartCode(partCode);
					insertCDPo.setPartOldcode(partOldcode);
					insertCDPo.setPartCname(partCname);
                    insertCDPo.setLocId(Long.parseLong(locId));
                    insertCDPo.setLocCode(locCode);
                    insertCDPo.setLocName(locName);
					insertCDPo.setStockQty(Long.parseLong(itemQty));
					insertCDPo.setUnit(unit);
                    insertCDPo.setBatchCode(batchCode);
                    insertCDPo.setVenderId(Long.parseLong(venderId));
					insertCDPo.setRemark(deRemark);
					insertCDPo.setCreateBy(userId);
					insertCDPo.setCreateDate(date);
					insertCDPo.setUpdateBy(userId);
					insertCDPo.setUpdateDate(date);
					
					dao.insert(insertCDPo);
				}
			}
			act.setOutData("success", "true");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"保存修改的配件库存盘点信息失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
			act.setOutData("errorExist", "保存失败!");
		}
	}
	
	/**
	 * 
	 * @Title      : 导出配件库存盘点 EXECEL模板
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public void exportExcelTemplate(){

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			
		
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();//导出模板第一列
			listHead.add("配件编码");
			listHead.add("货位编码");
//			listHead.add("调整数量 ");
			listHead.add("备注");
			list.add(listHead);
			// 导出的文件名
			String fileName = "配件库存盘点模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @Title      : 配件库存盘点 -> 导入文件
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public void stockInventoryUpload(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper req = act.getRequest();
		try {
			String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
			String actionURL = CommonUtils.checkNull(request.getParamValue("actionURL"));// action地址
			String parentOrgId = "";//父机构（销售单位）ID
			String parentOrgCode = "";//父机构（销售单位）编码
			String companyName = ""; //制单单位
			//判断主机厂与服务商
			String comp = logonUser.getOemCompanyId();
			if (null == comp ){
				
				parentOrgId = Constant.OEM_ACTIVITIES;
				parentOrgCode = Constant.ORG_ROOT_CODE;
				companyName = dao.getMainCompanyName(parentOrgId);
			}else {
				parentOrgId = logonUser.getDealerId();
				parentOrgCode = logonUser.getDealerCode();
				companyName = dao.getDealerName(parentOrgId);
			}
			List<Map<String,String>> errorInfo= new ArrayList<Map<String,String>>();
			List<Map<String,String>> maxLineErro= new ArrayList<Map<String,String>>();
			long maxSize=1024*1024*5;
			int errNum = insertIntoTmp(request, "uploadFile",3,1,maxSize);
			
			String err="";
			
			if(errNum!=0){
				switch (errNum) {
				case 1:
					err+="文件列数过多!";
					break;
				case 2:
					err+="空行不能大于一行!";
					break;
				case 3:
					err+="文件不能为空!";
					break;
				case 4:
					err+="文件不能为空!";
					break;
				case 5:
					err+="文件不能大于!";
					break;
				default:
					break;
				}
			}
			if(!"".equals(err)){
				act.setOutData("error", err);
				act.setOutData("actionURL", actionURL);
				act.setForword(INPUT_ERROR_URL);
			}else{
				List<Map> list=getMapList();
				List<Map<String,String>> voList = new ArrayList<Map<String,String>>();
				loadVoList(voList,list, errorInfo, maxLineErro, parentOrgId, whId);
				if(maxLineErro.size() > 0){
					err = maxLineErro.get(0).get("1").toString();
					act.setOutData("error", err);
					act.setOutData("actionURL", actionURL);
					act.setForword(INPUT_ERROR_URL);
				} else if(errorInfo.size()>0){
					act.setOutData("errorInfo", errorInfo);
					act.setOutData("actionURL", actionURL);
					act.setForword(INPUT_ERROR_URL);
				}else{
					
					StringBuffer sbString = new StringBuffer();
					sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
					List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());
					
					String marker = logonUser.getName();
					String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_22);//获取盘点单号
					
					act.setOutData("selectedWhId", whId);
					act.setOutData("parentOrgId", parentOrgId);
					act.setOutData("parentOrgCode", parentOrgCode);
					act.setOutData("changeCode", changeCode);
					act.setOutData("marker", marker);
					act.setOutData("companyName", companyName);
					act.setOutData("WHList", WHList);
					act.setOutData("list", voList);
					act.setOutData("actionURL", actionURL);
					act.setForword(PART_STOCK_INVE_ADD);
				}
				
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = null;
			if(e instanceof BizException){
				e1 = (BizException)e;
			}else{
				new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"文件读取错误");
			}
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 读取CELL
	 * @param      : @param voList
	 * @param      : @param list
	 * @param      : @param errorInfo      
	 * @return     :    
	 * LastDate    : 2013-4-12
	 */
	private void loadVoList(List<Map<String,String>> voList,List<Map> list,List<Map<String,String>> errorInfo, List<Map<String,String>> maxLineErro, String parentOrgId, String whId){
		if(null==list){
			list=new ArrayList();
		}
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while (it.hasNext()) {
				key = (String) it.next();
				Cell[] cells = (Cell[]) map.get(key);
				Map<String, String> tempmap = new HashMap<String, String>();
				int maxLineNum = Constant.PART_STOCK_STATUS_CHANGE_MAX_LINENUM;
				if (Integer.parseInt(key) > maxLineNum) {
					Map<String, String> errormap = new HashMap<String, String>();
					errormap.put("1","导入数据行数不能超过 " + maxLineNum + "行!");
					maxLineErro.add(errormap);
					return;
				}  
				if ("".equals(cells[0].getContents().trim())) {
					Map<String, String> errormap = new HashMap<String, String>();
					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
					errormap.put("2", "配件编码");
					errormap.put("3", "空");
					errorInfo.add(errormap);
					return;
				} 
				if ("".equals(cells[1].getContents().trim())) {
				    Map<String, String> errormap = new HashMap<String, String>();
				    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
				    errormap.put("2", "货");
				    errormap.put("3", "空");
				    errorInfo.add(errormap);
				    return;
				} 
				
				List<Map<String, Object>> partCheck = dao.checkOldCode(cells[0].getContents().trim());
				if (partCheck.size() == 1) {
					List<Map<String, Object>> partList = dao.getPartStockInfos(cells[0].getContents().trim(), parentOrgId, whId, cells[1].getContents().trim());
					if(partList.size() == 1) {
						tempmap.put("partOldcode", cells[0].getContents().trim());
						if(null != partList.get(0).get("PART_CODE") && !"".equals(partList.get(0).get("PART_CODE")))
						{
							tempmap.put("partCode", partList.get(0).get("PART_CODE").toString());
						}
						else
						{
							tempmap.put("partCode", "");
						}
						
						if(null != partList.get(0).get("PART_CNAME") && !"".equals(partList.get(0).get("PART_CNAME")))
						{
							tempmap.put("partCname", partList.get(0).get("PART_CNAME").toString());
						}
						else
						{
							tempmap.put("partCname", "");
						}
						
						if(null != partList.get(0).get("UNIT") && !"".equals(partList.get(0).get("UNIT")))
						{
							tempmap.put("unit", partList.get(0).get("UNIT").toString());
						}
						else
						{
							tempmap.put("unit", "");
						}
						tempmap.put("partId", partList.get(0).get("PART_ID").toString());
						tempmap.put("itemQty", partList.get(0).get("ITEM_QTY").toString());
						tempmap.put("locId", partList.get(0).get("LOC_ID").toString());
						tempmap.put("locCode", partList.get(0).get("LOC_CODE").toString());
						tempmap.put("locName", partList.get(0).get("LOC_NAME").toString());
					} else {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "配件编码");
						errormap.put("3", "不在当前登录用户权限范围！");
						errorInfo.add(errormap);
	                    return;
					}
					
				} else {
					Map<String, String> errormap = new HashMap<String, String>();
					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
					errormap.put("2", "配件编码");
					errormap.put("3", "不存在");
					errorInfo.add(errormap);
                    return;
				}

				tempmap.put("remark", cells.length < 3 || null == cells[2].getContents() ? "" : cells[2].getContents().trim());
				voList.add(tempmap);
			}
		}
	}
	
	/**
	 * 
	 * @Title      : 库存盘点详情查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	public void partStockDetailSearch() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId")); // 变更单ID
			String pageMark = CommonUtils.checkNull(request.getParamValue("pageMark")); // 页面标识
			int invType = Integer.parseInt(CommonUtils.checkNull(request.getParamValue("invType"))); // 盘点类型
			
			StringBuffer sbString = new StringBuffer();
			if(null != changeId && !"".equals(changeId))
			{
				sbString.append(" AND TM.CHANGE_ID = '" + changeId + "' ");
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = null;
			
			
			if("view".equalsIgnoreCase(pageMark))
			{
				ps = dao.queryPartStockDeatil(
						invType, sbString.toString(), Constant.PAGE_SIZE, curPage);
			}
			else if("print".equalsIgnoreCase(pageMark))
			{
				ps = dao.queryPartStockDeatil(
						invType, sbString.toString(), PAGE_SIZE, curPage);
			}
			
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件库存盘点信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title :导出配件库存盘点信息
	 * @param : @return
	 * @return :
	 * @throws : LastDate : 2013-5-3
	 */
	public void exportPartStockStatusExcel() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String changeCode = CommonUtils.checkNull(request
					.getParamValue("changeCode")); // 盘点单号
			String checkSDate = CommonUtils.checkNull(request
					.getParamValue("checkSDate")); // 开始时间
			String checkEDate = CommonUtils.checkNull(request
					.getParamValue("checkEDate")); // 截止时间
			String whId = CommonUtils.checkNull(request
					.getParamValue("whId")); // 截止时间
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
			StringBuffer sbString = new StringBuffer();
			if(null != changeCode && !"".equals(changeCode))
			{
				sbString.append(" AND TM.CHANGE_CODE LIKE '%" + changeCode + "%' ");
			}
			if(null != checkSDate && !"".equals(checkSDate))
			{
				sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
			}
			if(null != checkEDate && !"".equals(checkEDate))
			{
				sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
			}
			if(null != whId && !"".equals(whId))
			{
				sbString.append(" AND TM.WH_ID = '" + whId + "' ");
			}
			if(null != parentOrgId && !"".equals(parentOrgId))
			{
				sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
			}
			String[] head = new String[7];
			head[0] = "序号";
			head[1] = "盘点单号";
			head[2] = "盘点人";
			head[3] = "盘点日期";
			head[4] = "盘点类型";
			head[5] = "盘点仓库";
			head[6] = "盘点状态";
			List<Map<String, Object>> list = dao.queryAllPartStockInve(sbString.toString());
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[9];
						detail[0] = CommonUtils.checkNull(i+1);
						detail[1] = CommonUtils.checkNull(map
								.get("CHANGE_CODE"));
						detail[2] = CommonUtils
								.checkNull(map.get("NAME"));
						detail[3] = CommonUtils.checkNull(map
								.get("CREATE_DATE"));
						int type = Integer.parseInt(CommonUtils.checkNull(map.get("CHECK_TYPE")).toString());
						if(type1 == type)
						{
							detail[4] = "全部";
						}
						else if(type2 == type)
						{
							detail[4] = "动态";
						}
						detail[5] = CommonUtils.checkNull(map
								.get("WH_CNAME"));
						int state = Integer.parseInt(CommonUtils.checkNull(map.get("STATE")).toString());
						if(state1 == state)
						{
							detail[6] = "已保存";
						}
						else if(state2 == state)
						{
							detail[6] = "盘点中";
						}
						else if(state3 == state)
						{
							detail[6] = "已盘点";
						}
						list1.add(detail);
					}
				}
			}
			
			String fileName = "配件库存盘点信息";
			this.exportEx(fileName, ActionContext.getContext().getResponse(),
					request, head, list1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "导出配件库存盘点失败!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 导出初盘信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-4
	 */
	public void expPrevInvExcel() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId")); // 盘点单号
			int invType = Integer.parseInt(CommonUtils.checkNull(request.getParamValue("invType"))); // 盘点类型
			StringBuffer sbString = new StringBuffer();
			
			if(null != changeId && !"".equals(changeId))
			{
				sbString.append(" AND TM.CHANGE_ID = '" + changeId + "' ");
			}
			String[] head = new String[9];
			head[0] = "序号";
			head[1] = "盘点单号";
			head[2] = "盘点仓库";
			head[3] = "件号";
			head[4] = "配件编码";
			head[5] = "配件名称";
			head[6] = "单位";
			head[7] = "账面库存";
			head[8] = "备注";
			List<Map<String, Object>> list = dao.queryPartStockDeatil(invType, sbString.toString());
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[9];
						detail[0] = CommonUtils.checkNull(i+1);
						detail[1] = CommonUtils.checkNull(map
								.get("CHANGE_CODE"));
						detail[2] = CommonUtils
								.checkNull(map.get("WH_CNAME"));
						detail[3] = CommonUtils.checkNull(map
								.get("PART_CODE"));
						detail[4] = CommonUtils.checkNull(map
								.get("PART_OLDCODE")).toString();
						detail[5] = CommonUtils.checkNull(map
								.get("PART_CNAME"));
						detail[6] = CommonUtils.checkNull(map
								.get("UNIT")).toString();
						detail[7] = CommonUtils.checkNull(map
								.get("ITEM_QTY")).toString();
						detail[8] = CommonUtils.checkNull(map
								.get("REMARK")).toString();
						list1.add(detail);
					}
				}
			}
			
			String fileName = "配件库存初盘信息";
			this.exportEx(fileName, ActionContext.getContext().getResponse(),
					request, head, list1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "导出配件库存初盘信息失败!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 导出复盘信息/导出盘点明细
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-4
	 */
	public void expAgainInvExcel() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId")); // 盘点单号
			int invType = Integer.parseInt(CommonUtils.checkNull(request.getParamValue("invType"))); // 盘点类型
			StringBuffer sbString = new StringBuffer();
			
			if(null != changeId && !"".equals(changeId))
			{
				sbString.append(" AND TM.CHANGE_ID = '" + changeId + "' ");
			}
			String[] head = new String[20];
			head[0] = "序号";
			head[1] = "盘点单号";
			head[2] = "盘点仓库";
			head[3] = "货位";
			head[4] = "配件编码";
			head[5] = "配件名称";
			head[6] = "件号";
			head[7] = "单位";
			head[8] = "可用库存";
			head[9] = "占用库存";
			head[10] = "正常封存";
			head[11] = "盘亏封存";
			head[12] = "账面库存";
			head[13] = "盘盈库存";
			head[14] = "备注";
			List<Map<String, Object>> list = dao.queryPartStockDeatil(invType, sbString.toString());
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[20];
						detail[0] = CommonUtils.checkNull(i+1);
						detail[1] = CommonUtils.checkNull(map
								.get("CHANGE_CODE"));
						detail[2] = CommonUtils
								.checkNull(map.get("WH_CNAME"));
						detail[3] = CommonUtils.checkNull(map
								.get("LOC_NAME")).toString();
						detail[4] = CommonUtils.checkNull(map
								.get("PART_OLDCODE")).toString();
						detail[5] = CommonUtils.checkNull(map
								.get("PART_CNAME"));
						detail[6] = CommonUtils.checkNull(map
								.get("PART_CODE"));
						detail[7] = CommonUtils.checkNull(map
								.get("UNIT")).toString();
						detail[8] = CommonUtils.checkNull(map
								.get("NORMAL_QTY")).toString();
						detail[9] = CommonUtils.checkNull(map
								.get("BOOKED_QTY")).toString();
						detail[10] = CommonUtils.checkNull(map
								.get("ZCFC_QTY")).toString();
						detail[11] = CommonUtils.checkNull(map
								.get("PKFC_QTY")).toString();
						detail[12] = CommonUtils.checkNull(map
								.get("ITEM_QTY")).toString();
						detail[13] = CommonUtils.checkNull(map
								.get("PDFC_QTY")).toString();
						detail[14] = CommonUtils.checkNull(map
						        .get("REMARK")).toString();
						list1.add(detail);
					}
				}
			}
			
			String fileName = "配件盘点明细信息";
			this.exportEx(fileName, ActionContext.getContext().getResponse(),
					request, head, list1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "导出配件库存初盘信息失败!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	

	/**
	 * 
	 * @Title      : 文件导出为xls文件
	 * @param      : @param response
	 * @param      : @param request
	 * @param      : @param head
	 * @param      : @param list
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	public static Object exportEx(String fileName, ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = fileName + ".xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize = list.size() / 30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
						/*ws.addCell(new Label(i, z, str[i]));*/ //modify by yuan
                     if(CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])){
                       ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i].replace(",",""))));
                    }else{
                        ws.addCell(new Label(i, z, str[i]));
                    }
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
}
