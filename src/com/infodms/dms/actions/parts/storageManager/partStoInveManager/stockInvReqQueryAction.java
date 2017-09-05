package com.infodms.dms.actions.parts.storageManager.partStoInveManager;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.util.CheckUtil;
import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.storageManager.partStoInveManager.stockInvReqQueryDao;
import com.infodms.dms.dao.parts.storageManager.partStoInveManager.stockInvReqSolveDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartCheckResultMainPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 处理配件库存盘点调整查询业务
 * 
 * @Date: 2013-5-6
 * 
 * @author huhcao
 * @version 1.0
 * @remark
 */
public class stockInvReqQueryAction extends BaseImport {
	public Logger logger = Logger.getLogger(stockInvReqQueryAction.class);
	private static final stockInvReqQueryDao dao = stockInvReqQueryDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final int PAGE_SIZE = 20;
	private static final int type1 = Constant.PART_STOCK_INVE_TYPE_01;//全部
	private static final int type2 = Constant.PART_STOCK_INVE_TYPE_02;//动态
	private static final int state1 = Constant.PART_STOCK_INVE_STATE_01;//已保存
	private static final int state2 = Constant.PART_STOCK_INVE_STATE_02;//盘点中
	private static final int state3 = Constant.PART_STOCK_INVE_STATE_03;//已盘点
	private static final int resolveType1 = Constant.PART_INVE_RESOLVE_TYPE_01;//封存
	private static final int resolveType2 = Constant.PART_INVE_RESOLVE_TYPE_02;//盈亏出入库
	private static final int orderState1 = Constant.PART_INVE_ORDER_STATE_01;//已保存
	private static final int orderState2 = Constant.PART_INVE_ORDER_STATE_02;//审核中
	private static final int orderState3 = Constant.PART_INVE_ORDER_STATE_03;//处理中
	private static final int orderState4 = Constant.PART_INVE_ORDER_STATE_04;//已驳回
	private static final int orderState5 = Constant.PART_INVE_ORDER_STATE_05;//已关闭
	private static final int orderState6 = Constant.PART_INVE_ORDER_STATE_06;//已完成
	private static final int orderState7 = Constant.PART_INVE_ORDER_STATE_07;//已作废
	private static final int limitLineNum = 300;
	
	//配件库存盘点调整查询
	private static final String PART_STOCK_MAIN = "/jsp/parts/storageManager/partStoInveManager/stockInvReqQuery/stockInvReqQueryMain.jsp";//配件库存盘点调整查询首页
	private static final String PART_STOCK_HANDLE = "/jsp/parts/storageManager/partStoInveManager/stockInvReqQuery/stockInvReqQueryView.jsp";//库存盘点调整查询查询页面

	/**
	 * 
	 * @Title : 跳转至配件库存盘点调整查询页面
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-5-6
	 */
	public void stockInvReqQueryInit() {
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
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件库存盘点调整查询页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 查询配件库存盘点调整查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-7
	 */
	public void stockInvReqQuerySearch() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String inveCode = CommonUtils.checkNull(request
					.getParamValue("inveCode")); // 盘点单号
			String resultCode = CommonUtils.checkNull(request
					.getParamValue("resultCode")); // 申请单号
			String whId = CommonUtils.checkNull(request
					.getParamValue("whId")); // 仓库ID
			String orderState = CommonUtils.checkNull(request
					.getParamValue("orderState")); // 单据状态
			String inveType = CommonUtils.checkNull(request
					.getParamValue("inveType")); // 盘点类型
			String improterName = CommonUtils.checkNull(request
					.getParamValue("improterName")); // 导入人
			String impSDate = CommonUtils.checkNull(request
					.getParamValue("impSDate")); // 导入开始时间
			String impEDate = CommonUtils.checkNull(request
					.getParamValue("impEDate")); // 导入截止时间
			String commitName = CommonUtils.checkNull(request
					.getParamValue("commitName")); // 提交人
			String comSDate = CommonUtils.checkNull(request
					.getParamValue("comSDate")); // 提交开始时间
			String comEDate = CommonUtils.checkNull(request
					.getParamValue("comEDate")); // 提交截止时间
			String checkName = CommonUtils.checkNull(request
					.getParamValue("checkName")); // 审核人
			String cheSDate = CommonUtils.checkNull(request
					.getParamValue("cheSDate")); // 审核开始时间
			String cheEDate = CommonUtils.checkNull(request
					.getParamValue("cheEDate")); // 审核截止时间
			String handleName = CommonUtils.checkNull(request
					.getParamValue("handleName")); // 处理人
			String hanSDate = CommonUtils.checkNull(request
					.getParamValue("hanSDate")); // 处理开始时间
			String hanEDate = CommonUtils.checkNull(request
					.getParamValue("hanEDate")); // 处理截止时间
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
			StringBuffer sbString = new StringBuffer();
			if(null != inveCode && !"".equals(inveCode))
			{
				sbString.append(" AND TM.CHANGE_CODE like '%" + inveCode + "%' ");
			}
			if(null != resultCode && !"".equals(resultCode))
			{
				sbString.append(" AND TM.RESULT_CODE LIKE '%" + resultCode + "%' ");
			}
			if(null != improterName && !"".equals(improterName))
			{
				sbString.append(" AND LU.NAME LIKE '%" + improterName + "%' ");
			}
			if(null != impSDate && !"".equals(impSDate))
			{
				sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') >= '" + impSDate + "' ");
			}
			if(null != impEDate && !"".equals(impEDate))
			{
				sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') <= '" + impEDate + "' ");
			}
			
			if(null != commitName && !"".equals(commitName))
			{
				sbString.append(" AND CU.NAME LIKE '%" + commitName + "%' ");
			}
			if(null != comSDate && !"".equals(comSDate))
			{
				sbString.append(" AND TO_CHAR(TM.COMMIT_DATE,'yyyy-MM-dd') >= '" + comSDate + "' ");
			}
			if(null != comEDate && !"".equals(comEDate))
			{
				sbString.append(" AND TO_CHAR(TM.COMMIT_DATE,'yyyy-MM-dd') <= '" + comEDate + "' ");
			}
			
			if(null != checkName && !"".equals(checkName))
			{
				sbString.append(" AND KU.NAME LIKE '%" + checkName + "%' ");
			}
			if(null != cheSDate && !"".equals(cheSDate))
			{
				sbString.append(" AND TO_CHAR(TM.CHECK_DATE,'yyyy-MM-dd') >= '" + cheSDate + "' ");
			}
			if(null != cheEDate && !"".equals(cheEDate))
			{
				sbString.append(" AND TO_CHAR(TM.CHECK_DATE,'yyyy-MM-dd') <= '" + cheEDate + "' ");
			}
			
			if(null != handleName && !"".equals(handleName))
			{
				sbString.append(" AND HU.NAME LIKE '%" + handleName + "%' ");
			}
			if(null != hanSDate && !"".equals(hanSDate))
			{
				sbString.append(" AND TO_CHAR(TM.HANDLE_DATE,'yyyy-MM-dd') >= '" + hanSDate + "' ");
			}
			if(null != hanEDate && !"".equals(hanEDate))
			{
				sbString.append(" AND TO_CHAR(TM.HANDLE_DATE,'yyyy-MM-dd') <= '" + hanEDate + "' ");
			}
			
			if(null != whId && !"".equals(whId))
			{
				sbString.append(" AND TM.WH_ID = '" + whId + "' ");
			}
			if(null != orderState && !"".equals(orderState))
			{
				sbString.append(" AND TM.STATE = '" + orderState + "' ");
			}
			if(null != inveType && !"".equals(inveType))
			{
				sbString.append(" AND TM.CHECK_TYPE = '" + inveType + "' ");
			}
			if(null != parentOrgId && !"".equals(parentOrgId))
			{
				sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 查询当前页
			PageResult<Map<String, Object>> ps = dao.queryPartStockInve(
					sbString.toString(), Constant.PAGE_SIZE, curPage);

			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件库存盘点调整查询信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 跳转至库存盘点调整查询查看页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-6
	 */
	public void viewStockDeatilInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String resultId = CommonUtils.checkNull(request.getParamValue("resultId"));// 盘点结果单ID
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 盘点结果单ID
			StringBuffer sbString = new StringBuffer();
			sbString.append(" AND TM.RESULT_ID = '" + resultId + "' ");
			Map<String, Object> map = dao.queryAllPartStockInve(sbString.toString()).get(0);
			
			int type = Integer.parseInt(map.get("CHECK_TYPE").toString());
			int orederState = Integer.parseInt(map.get("STATE").toString());
			
			if(null == map.get("HANDLE_TYPE") || "".equals(map.get("HANDLE_TYPE")))
			{
				map.put("HANDLE_TYPE", "");
			}
			else
			{
				int handleType = Integer.parseInt(map.get("HANDLE_TYPE").toString());
				if(resolveType1 == handleType)
				{
					map.put("HANDLE_TYPE", "封存");
				}
				else if(resolveType2 == handleType)
				{
					map.put("HANDLE_TYPE", "处理");
				}
			}
			
			if(type1 == type)
			{
				map.put("CHECK_TYPE", "全部");
			}
			else if(type2 == type)
			{
				map.put("CHECK_TYPE", "动态");
			}
			
			if(orderState1 == orederState)
			{
				map.put("STATE", "已保存");
			}
			else if(orderState2 == orederState)
			{
				map.put("STATE", "审核中");
			}
			else if(orderState3 == orederState)
			{
				map.put("STATE", "处理中");
			}
			else if(orderState4 == orederState)
			{
				map.put("STATE", "已驳回");
			}
			else if(orderState5 == orederState)
			{
				map.put("STATE", "已关闭");
			}
			else if(orderState6 == orederState)
			{
				map.put("STATE", "已完成");
			}
			else if(orderState7 == orederState)
			{
				map.put("STATE", "已作废");
			}
			
			act.setOutData("map", map);
			act.setForword(PART_STOCK_HANDLE);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存盘点调整查询查看页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 提交或者作废库存盘点调整查询结果
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-6
	 */
	public void commitCheckResult(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		act.getResponse().setContentType("application/json");
		RequestWrapper req = act.getRequest();
	try {
		String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
		if("".equals(curPage)){
			curPage = "1";
		}
		String resultId = CommonUtils.checkNull(req.getParamValue("resultId")); //盘点单ID
		String checkResult = CommonUtils.checkNull(req.getParamValue("checkResult")); //查询结果
		String handleType = CommonUtils.checkNull(req.getParamValue("handleType")); //查询方式
		Long userId = logonUser.getUserId(); //操作人ID
		Date date = new Date();
		
		TtPartCheckResultMainPO selCMPo = new TtPartCheckResultMainPO();
		TtPartCheckResultMainPO updCMPo = new TtPartCheckResultMainPO();
		
		selCMPo.setResultId(Long.parseLong(resultId));
		
		updCMPo.setUpdateBy(userId);
		updCMPo.setUpdateDate(date);
		updCMPo.setHandleType(Integer.parseInt(handleType));
		updCMPo.setState(Integer.parseInt(checkResult));
		
		dao.update(selCMPo, updCMPo);
		
		act.setOutData("success", "true");
		act.setOutData("curPage", curPage);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"提交库存盘点申请查询结果");
			logger.error(logonUser,e1);
			act.setException(e1);
			act.setOutData("errorExist", "操作失败!");//返回错误信息
		}
	}
	
	
	/**
	 * 
	 * @Title      : 库存盘点调整查询详情查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-6
	 */
	public void partStockDetailSearch() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String resultId = CommonUtils.checkNull(request.getParamValue("resultId")); // 盘点结果单ID
			
			StringBuffer sbString = new StringBuffer();
			if(null != resultId && !"".equals(resultId))
			{
				sbString.append(" AND TM.RESULT_ID = '" + resultId + "' ");
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 查询当前页
			PageResult<Map<String, Object>> ps = null;
			
			ps = dao.queryPartStockDeatil(sbString.toString(), Constant.PAGE_SIZE, curPage);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件库存盘点调整查询信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title :导出配件库存盘点调整查询信息
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
			String inveCode = CommonUtils.checkNull(request
					.getParamValue("inveCode")); // 盘点单号
			String resultCode = CommonUtils.checkNull(request
					.getParamValue("resultCode")); // 申请单号
			String whId = CommonUtils.checkNull(request
					.getParamValue("whId")); // 仓库ID
			String orderState = CommonUtils.checkNull(request
					.getParamValue("orderState")); // 单据状态
			String inveType = CommonUtils.checkNull(request
					.getParamValue("inveType")); // 盘点类型
			String improterName = CommonUtils.checkNull(request
					.getParamValue("improterName")); // 导入人
			String impSDate = CommonUtils.checkNull(request
					.getParamValue("impSDate")); // 导入开始时间
			String impEDate = CommonUtils.checkNull(request
					.getParamValue("impEDate")); // 导入截止时间
			String commitName = CommonUtils.checkNull(request
					.getParamValue("commitName")); // 提交人
			String comSDate = CommonUtils.checkNull(request
					.getParamValue("comSDate")); // 提交开始时间
			String comEDate = CommonUtils.checkNull(request
					.getParamValue("comEDate")); // 提交截止时间
			String checkName = CommonUtils.checkNull(request
					.getParamValue("checkName")); // 审核人
			String cheSDate = CommonUtils.checkNull(request
					.getParamValue("cheSDate")); // 审核开始时间
			String cheEDate = CommonUtils.checkNull(request
					.getParamValue("cheEDate")); // 审核截止时间
			String handleName = CommonUtils.checkNull(request
					.getParamValue("handleName")); // 处理人
			String hanSDate = CommonUtils.checkNull(request
					.getParamValue("hanSDate")); // 处理开始时间
			String hanEDate = CommonUtils.checkNull(request
					.getParamValue("hanEDate")); // 处理截止时间
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
			StringBuffer sbString = new StringBuffer();
			if(null != inveCode && !"".equals(inveCode))
			{
				sbString.append(" AND TM.CHANGE_CODE like '%" + inveCode + "%' ");
			}
			if(null != resultCode && !"".equals(resultCode))
			{
				sbString.append(" AND TM.RESULT_CODE LIKE '%" + resultCode + "%' ");
			}
			if(null != improterName && !"".equals(improterName))
			{
				sbString.append(" AND LU.NAME LIKE '%" + improterName + "%' ");
			}
			if(null != impSDate && !"".equals(impSDate))
			{
				sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') >= '" + impSDate + "' ");
			}
			if(null != impEDate && !"".equals(impEDate))
			{
				sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') <= '" + impEDate + "' ");
			}
			
			if(null != commitName && !"".equals(commitName))
			{
				sbString.append(" AND CU.NAME LIKE '%" + commitName + "%' ");
			}
			if(null != comSDate && !"".equals(comSDate))
			{
				sbString.append(" AND TO_CHAR(TM.COMMIT_DATE,'yyyy-MM-dd') >= '" + comSDate + "' ");
			}
			if(null != comEDate && !"".equals(comEDate))
			{
				sbString.append(" AND TO_CHAR(TM.COMMIT_DATE,'yyyy-MM-dd') <= '" + comEDate + "' ");
			}
			
			if(null != checkName && !"".equals(checkName))
			{
				sbString.append(" AND KU.NAME LIKE '%" + checkName + "%' ");
			}
			if(null != cheSDate && !"".equals(cheSDate))
			{
				sbString.append(" AND TO_CHAR(TM.CHECK_DATE,'yyyy-MM-dd') >= '" + cheSDate + "' ");
			}
			if(null != cheEDate && !"".equals(cheEDate))
			{
				sbString.append(" AND TO_CHAR(TM.CHECK_DATE,'yyyy-MM-dd') <= '" + cheEDate + "' ");
			}
			
			if(null != handleName && !"".equals(handleName))
			{
				sbString.append(" AND HU.NAME LIKE '%" + handleName + "%' ");
			}
			if(null != hanSDate && !"".equals(hanSDate))
			{
				sbString.append(" AND TO_CHAR(TM.HANDLE_DATE,'yyyy-MM-dd') >= '" + hanSDate + "' ");
			}
			if(null != hanEDate && !"".equals(hanEDate))
			{
				sbString.append(" AND TO_CHAR(TM.HANDLE_DATE,'yyyy-MM-dd') <= '" + hanEDate + "' ");
			}
			
			if(null != whId && !"".equals(whId))
			{
				sbString.append(" AND TM.WH_ID = '" + whId + "' ");
			}
			if(null != orderState && !"".equals(orderState))
			{
				sbString.append(" AND TM.STATE = '" + orderState + "' ");
			}
			if(null != inveType && !"".equals(inveType))
			{
				sbString.append(" AND TM.CHECK_TYPE = '" + inveType + "' ");
			}
			if(null != parentOrgId && !"".equals(parentOrgId))
			{
				sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
			}
			String[] head = new String[14];
			head[0] = "序号";
			head[1] = "盘点单号";
			head[2] = "申请单号";
			head[3] = "盘点类型";
			head[4] = "盘点仓库";
			head[5] = "导入人";
			head[6] = "导入日期";
			head[7] = "提交人";
			head[8] = "提交日期";
			head[9] = "审核人";
			head[10] = "审核日期";
			head[11] = "处理人";
			head[12] = "处理日期";
			head[13] = "单据状态";
			List<Map<String, Object>> list = dao.queryAllPartStockInve(sbString.toString());
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[15];
						detail[0] = CommonUtils.checkNull(i+1);
						detail[1] = CommonUtils.checkNull(map
								.get("CHANGE_CODE"));
						detail[2] = CommonUtils.checkNull(map
								.get("RESULT_CODE"));
						int type = Integer.parseInt(CommonUtils.checkNull(map.get("CHECK_TYPE")).toString());
						if(type1 == type)
						{
							detail[3] = "全部";
						}
						else if(type2 == type)
						{
							detail[3] = "动态";
						}
						detail[4] = CommonUtils.checkNull(map
								.get("WH_CNAME"));
						detail[5] = CommonUtils
							.checkNull(map.get("IMP_NAME"));
						detail[6] = CommonUtils.checkNull(map
								.get("CREATE_DATE"));
						detail[7] = CommonUtils
							.checkNull(map.get("COMM_NAME"));
						detail[8] = CommonUtils.checkNull(map
								.get("COMMIT_DATE"));
						detail[9] = CommonUtils
							.checkNull(map.get("CHE_NAME"));
						detail[10] = CommonUtils.checkNull(map
								.get("CHECK_DATE"));
						detail[11] = CommonUtils
							.checkNull(map.get("HAN_NAME"));
						detail[12] = CommonUtils.checkNull(map
								.get("HANDLE_DATE"));
						
						int orederState = Integer.parseInt(CommonUtils.checkNull(map.get("STATE")));
						if(orderState1 == orederState)
						{
							detail[13] = "已保存";
						}
						else if(orderState2 == orederState)
						{
							detail[13] = "审核中";
						}
						else if(orderState3 == orederState)
						{
							detail[13] = "处理中";
						}
						else if(orderState4 == orederState)
						{
							detail[13] = "已驳回";
						}
						else if(orderState5 == orederState)
						{
							detail[13] = "已关闭";
						}
						else if(orderState6 == orederState)
						{
							detail[13] = "已完成";
						}
						else if(orderState7 == orederState)
						{
							detail[13] = "已作废";
						}
						
						list1.add(detail);
					}
				}
			}
			
			String fileName = "配件库存盘点调整查询信息";
			this.exportEx(fileName, ActionContext.getContext().getResponse(),
					request, head, list1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "导出配件库存盘点调整查询");
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
