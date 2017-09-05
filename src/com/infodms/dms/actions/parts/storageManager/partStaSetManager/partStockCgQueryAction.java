package com.infodms.dms.actions.parts.storageManager.partStaSetManager;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import com.infodms.dms.dao.parts.storageManager.partStaSetManager.partStockCgQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 处理库存状态变更查询业务
 * 
 * @Date: 2013-4-24
 * 
 * @author huhcao
 * @version 1.0
 * @remark
 */
public class partStockCgQueryAction extends BaseImport {
	public Logger logger = Logger.getLogger(partStockCgQueryAction.class);
	private static final partStockCgQueryDao dao = partStockCgQueryDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	//配件来货错误处理
	private static final String PART_STOCK_SETTING = "/jsp/parts/storageManager/partStaSetManager/partStockCgQuery/partStockCgQuery.jsp";//库存状态变更查询首页
	private static final String PART_STOCK_VIEW = "/jsp/parts/storageManager/partStaSetManager/partStockCgQuery/partStockCgQueryView.jsp";//库存状态变更查询查看页面

	
	/**
	 * 
	 * @Title : 跳转至库存状态变更查询页面
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-24
	 */
	public void partStockCgQueryInit() {
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
			act.setForword(PART_STOCK_SETTING);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存状态变更查询页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 查询库存状态变更信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public void partStockCgQuerySearch() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String searchType = CommonUtils.checkNull(request
					.getParamValue("searchType")); // 查询类型
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
			
			if(!"detail".equalsIgnoreCase(searchType))
			{
				String changeCode = CommonUtils.checkNull(request
						.getParamValue("changeCode")); // 变更单号
				String checkSDate = CommonUtils.checkNull(request
						.getParamValue("checkSDate")); // 开始时间
				String checkEDate = CommonUtils.checkNull(request
						.getParamValue("checkEDate")); // 截止时间
				String whId = CommonUtils.checkNull(request
						.getParamValue("whId")); // 截止时间
				String businessType = CommonUtils.checkNull(request
						.getParamValue("businessType1")); // 业务类型
				String remark = CommonUtils.checkNull(request
						.getParamValue("remark1")); // 备注
				String isFinish = CommonUtils.checkNull(request
						.getParamValue("isFinish1")); // 完成状态
				
				StringBuffer sbString = new StringBuffer();
				if(null != changeCode && !"".equals(changeCode))
				{
					sbString.append(" AND UPPER(TM.CHANGE_CODE) LIKE '%" + changeCode.toUpperCase() + "%' ");
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
				if(null != businessType && !"".equals(businessType))
				{
					sbString.append(" AND TM.CHG_TYPE = '" + businessType + "' ");
				}
				if(null != remark && !"".equals(remark))
				{
					sbString.append(" AND TM.REMARK LIKE '%" + remark.trim() + "%' ");
				}
				if(null != isFinish && !"".equals(isFinish))
				{
					sbString.append(" AND TM.STATE = '" + isFinish + "' ");
				}
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.queryPartStockStuatus(
						sbString.toString(), Constant.PAGE_SIZE, curPage);

				act.setOutData("ps", ps);
			}
			else
			{
				String partCode = CommonUtils.checkNull(request
						.getParamValue("partCode")); // 件号
				String partOldcode = CommonUtils.checkNull(request
						.getParamValue("partOldcode")); // 配件编码
				String partCname = CommonUtils.checkNull(request
						.getParamValue("partCname")); // 配件名称
				String handleSDate = CommonUtils.checkNull(request
						.getParamValue("handleSDate")); // 开始时间
				String handleEDate = CommonUtils.checkNull(request
						.getParamValue("handleEDate")); // 截止时间
				String changeType = CommonUtils.checkNull(request
						.getParamValue("changeType")); // 调整类型
				String businessType = CommonUtils.checkNull(request
						.getParamValue("businessType2")); // 业务类型
				String remark = CommonUtils.checkNull(request
						.getParamValue("remark2")); // 备注
				String isFinish = CommonUtils.checkNull(request
						.getParamValue("isFinish2")); // 完成状态
				String maker = CommonUtils.checkNull(request
						.getParamValue("maker")); // 制单人
				
				StringBuffer sbString = new StringBuffer();
				if(null != partCode && !"".equals(partCode))
				{
					sbString.append(" AND UPPER(TD.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
				}
				if(null != partOldcode && !"".equals(partOldcode))
				{
					sbString.append(" AND UPPER(TD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
				}
				if(null != partCname && !"".equals(partCname))
				{
					sbString.append(" AND UPPER(TD.PART_CNAME) LIKE '%" + partCname.toUpperCase() + "%' ");
				}
				if(null != remark && !"".equals(remark))
				{
					sbString.append(" AND UPPER(TD.REMARK) LIKE '%" + remark.toUpperCase() + "%' ");
				}
				if(null != handleSDate && !"".equals(handleSDate))
				{
					sbString.append(" AND TO_CHAR(TD.UPDATE_DATE,'yyyy-MM-dd') >= '" + handleSDate + "' ");
				}
				if(null != handleEDate && !"".equals(handleEDate))
				{
					sbString.append(" AND TO_CHAR(TD.UPDATE_DATE,'yyyy-MM-dd') <= '" + handleEDate + "' ");
				}
				if(null != changeType && !"".equals(changeType))
				{
					sbString.append(" AND TD.CHANGE_TYPE = '" + changeType + "' ");
				}
				if(null != businessType && !"".equals(businessType))
				{
					sbString.append(" AND TD.CHANGE_REASON = '" + businessType + "' ");
				}
				if(null != parentOrgId && !"".equals(parentOrgId))
				{
					sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
				}
				if(null != isFinish && !"".equals(isFinish))
				{
					sbString.append(" AND TD.STATUS = '" + isFinish + "' ");
				}
				if(null != maker && !"".equals(maker))
				{
					sbString.append(" AND U.NAME LIKE '%" + maker.trim() + "%' ");
				}
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.staSetCntQuery(
						sbString.toString(), Constant.PAGE_SIZE, curPage);

				act.setOutData("ps", ps);
			}
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询库存状态变更信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 跳转至库存状态变更查询查看页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public void viewStockDeatilInint() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));// 变更单ID
			StringBuffer sbString = new StringBuffer();
			sbString.append(" AND TM.CHANGE_ID = '" + changeId + "' ");
			Map<String, Object> map = dao.queryAllPartStockStatus(sbString.toString()).get(0);
			
			act.setOutData("map", map);
			act.setForword(PART_STOCK_VIEW);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存状态变更查看页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 
	 * @Title      :库存状态变更详情查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public void partStockDetailSearch() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId")); // 变更单ID
			
			StringBuffer sbString = new StringBuffer();
			if(null != changeId && !"".equals(changeId))
			{
				sbString.append(" AND TD.CHANGE_ID = '" + changeId + "' ");
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartStockDeatil(
					sbString.toString(), Constant.PAGE_SIZE, curPage);

			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存状态变更详细信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title :导出库存状态变更信息
	 * @param : @return
	 * @return :
	 * @throws : LastDate : 2013-4-24
	 */
	public void exportPartStockStatusExcel() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
			String changeCode = CommonUtils.checkNull(request
					.getParamValue("changeCode")); // 变更单号
			String checkSDate = CommonUtils.checkNull(request
					.getParamValue("checkSDate")); // 开始时间
			String checkEDate = CommonUtils.checkNull(request
					.getParamValue("checkEDate")); // 截止时间
			String whId = CommonUtils.checkNull(request
					.getParamValue("whId")); // 截止时间
			String businessType = CommonUtils.checkNull(request
					.getParamValue("businessType1")); // 业务类型
			String remark = CommonUtils.checkNull(request
					.getParamValue("remark1")); // 备注
			String isFinish = CommonUtils.checkNull(request
					.getParamValue("isFinish1")); // 完成状态
			
			StringBuffer sbString = new StringBuffer();
			if(null != changeCode && !"".equals(changeCode))
			{
				sbString.append(" AND UPPER(TM.CHANGE_CODE) LIKE '%" + changeCode.toUpperCase() + "%' ");
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
			if(null != businessType && !"".equals(businessType))
			{
				sbString.append(" AND TM.CHG_TYPE = '" + businessType + "' ");
			}
			if(null != remark && !"".equals(remark))
			{
				sbString.append(" AND TM.REMARK LIKE '%" + remark.trim() + "%' ");
			}
			if(null != isFinish && !"".equals(isFinish))
			{
				sbString.append(" AND TM.STATE = '" + isFinish + "' ");
			}
			String[] head = new String[10];
			head[0] = "序号";
			head[1] = "变更单号";
			head[2] = "业务类型";
			head[3] = "制单单位";
			head[4] = "制单人";
			head[5] = "制单日期";
			head[6] = "备注";
			head[7] = "完成状态";
			head[8] = "仓库";
			List<Map<String, Object>> list = dao.queryAllPartStockStatus(sbString.toString());
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[10];
						detail[0] = CommonUtils.checkNull(i+1);
						detail[1] = CommonUtils.checkNull(map
								.get("CHANGE_CODE"));
						if(null != map.get("CHG_TYPE"))
						{
							int bzType = Integer.parseInt(map.get("CHG_TYPE").toString());
							
							if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01 == bzType)
							{
								detail[2] = "正常";
							}
							else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02 == bzType)
							{
								detail[2] = "盘盈";
							}
							else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03 == bzType)
							{
								detail[2] = "盘亏";
							}
							else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_04 == bzType)
							{
								detail[2] = "来货错误";
							}
							else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_05 == bzType)
							{
								detail[2] = "质量问题";
							}
							else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_06 == bzType)
							{
								detail[2] = "借条";
							}
							else
							{
								detail[2] = "现场BO";
							}
						}
						detail[3] = CommonUtils
								.checkNull(map.get("CHGORG_CNAME"));
						detail[8] = CommonUtils
								.checkNull(map.get("WH_CNAME"));
						detail[4] = CommonUtils.checkNull(map
								.get("NAME"));
						detail[5] = CommonUtils.checkNull(map
								.get("CREATE_DATE"));
						detail[6] = CommonUtils
							.checkNull(map.get("REMARK"));
						if(null != map.get("STATE") && "1".equals(map.get("STATE").toString()))
						{
							detail[7] = "未完成";
						}
						else
						{
							detail[7] = "已完成";
						}
						list1.add(detail);
					}
				}
			}
			
			String fileName = "库存状态变更信息";
			this.exportEx(fileName, ActionContext.getContext().getResponse(),
					request, head, list1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "导出库存状态变更信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 汇总查询导出
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-7-1
	 */
	public void exportStaSetDetExcel() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
			String partCode = CommonUtils.checkNull(request
					.getParamValue("partCode")); // 件号
			String partOldcode = CommonUtils.checkNull(request
					.getParamValue("partOldcode")); // 配件编码
			String partCname = CommonUtils.checkNull(request
					.getParamValue("partCname")); // 配件名称
			String handleSDate = CommonUtils.checkNull(request
					.getParamValue("handleSDate")); // 开始时间
			String handleEDate = CommonUtils.checkNull(request
					.getParamValue("handleEDate")); // 截止时间
			String changeType = CommonUtils.checkNull(request
					.getParamValue("changeType")); // 调整类型
			String businessType = CommonUtils.checkNull(request
					.getParamValue("businessType2")); // 业务类型
			String remark = CommonUtils.checkNull(request
					.getParamValue("remark2")); // 备注
			String isFinish = CommonUtils.checkNull(request
					.getParamValue("isFinish2")); // 完成状态
			String maker = CommonUtils.checkNull(request
					.getParamValue("maker")); // 制单人
			
			StringBuffer sbString = new StringBuffer();
			if(null != partCode && !"".equals(partCode))
			{
				sbString.append(" AND UPPER(TD.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
			}
			if(null != partOldcode && !"".equals(partOldcode))
			{
				sbString.append(" AND UPPER(TD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
			}
			if(null != partCname && !"".equals(partCname))
			{
				sbString.append(" AND UPPER(TD.PART_CNAME) LIKE '%" + partCname.toUpperCase() + "%' ");
			}
			if(null != remark && !"".equals(remark))
			{
				sbString.append(" AND UPPER(TD.REMARK) LIKE '%" + remark.toUpperCase() + "%' ");
			}
			if(null != handleSDate && !"".equals(handleSDate))
			{
				sbString.append(" AND TO_CHAR(TD.UPDATE_DATE,'yyyy-MM-dd') >= '" + handleSDate + "' ");
			}
			if(null != handleEDate && !"".equals(handleEDate))
			{
				sbString.append(" AND TO_CHAR(TD.UPDATE_DATE,'yyyy-MM-dd') <= '" + handleEDate + "' ");
			}
			if(null != changeType && !"".equals(changeType))
			{
				sbString.append(" AND TD.CHANGE_TYPE = '" + changeType + "' ");
			}
			if(null != businessType && !"".equals(businessType))
			{
				sbString.append(" AND TD.CHANGE_REASON = '" + businessType + "' ");
			}
			if(null != parentOrgId && !"".equals(parentOrgId))
			{
				sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
			}
			if(null != isFinish && !"".equals(isFinish))
			{
				sbString.append(" AND TD.STATUS = '" + isFinish + "' ");
			}
			if(null != maker && !"".equals(maker))
			{
				sbString.append(" AND U.NAME LIKE '%" + maker.trim() + "%' ");
			}
			
			String[] head = new String[20];
			head[0] = "序号";
			head[1] = "件号";
			head[2] = "配件编码";
			head[3] = "配件名称";
			head[4] = "可用库存";
			head[5] = "业务类型";
			head[6] = "调整方式";
			head[7] = "调整数量";
			head[8] = "创建日期";
			head[9] = "备注(原因)";
			head[10] = "制单人";
			head[11] = "已处理数量";
			head[12] = "可处理数量";
			head[13] = "处理日期";
			head[14] = "处理原因";
			head[15] = "完成状态";
			head[16] = "仓库";
			List<Map<String, Object>> list = dao.staSetCntQuyList(sbString.toString());
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[20];
						detail[0] = CommonUtils.checkNull(i+1);
						detail[1] = CommonUtils.checkNull(map.get("PART_CODE"));
						detail[2] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
						detail[3] = CommonUtils.checkNull(map.get("PART_CNAME"));
						detail[4] = CommonUtils.checkNull(map.get("NORMAL_QTY"));
						int bzType = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01;
						int cgType = Constant.PART_STOCK_STATUS_CHANGE_TYPE_01;
						Object bzTypeStr = map.get("CHANGE_REASON");
						Object bgTypeStr = map.get("CHANGE_TYPE");
						if(null != bzTypeStr && !"".equals(bzTypeStr))
						{
							bzType = Integer.parseInt(bzTypeStr.toString());
						}
						if(null != bgTypeStr && !"".equals(bgTypeStr))
						{
							cgType = Integer.parseInt(bgTypeStr.toString());
						}
						
						if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01 == bzType)
						{
							detail[5] = "正常";
						}
						else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02 == bzType)
						{
							detail[5] = "盘盈";
						}
						else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03 == bzType)
						{
							detail[5] = "盘亏";
						}
						else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_04 == bzType)
						{
							detail[5] = "来货错误";
						}
						else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_05 == bzType)
						{
							detail[5] = "质量问题";
						}
						else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_06 == bzType)
						{
							detail[5] = "借条";
						}
						else
						{
							detail[5] = "现场BO";
						}
						
						if(Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 == cgType)
						{
							detail[6] = "封存";
						}
						else
						{
							detail[6] = "解封";
						}
						detail[7] = CommonUtils.checkNull(map.get("RETURN_QTY"));
						detail[8] = CommonUtils.checkNull(map.get("CREATE_DATE_FM"));
						detail[9] = CommonUtils.checkNull(map.get("REMARK"));
						detail[10] = CommonUtils.checkNull(map.get("NAME"));
						detail[11] = CommonUtils.checkNull(map.get("COLSE_QTY"));
						detail[12] = CommonUtils.checkNull(map.get("UNCLS_QTY"));
						detail[13] = CommonUtils.checkNull(map.get("UPDATE_DATE_FM"));
						detail[14] = CommonUtils.checkNull(map.get("REMARK2"));
						if(null != map.get("STATUS") && "1".equals(map.get("STATUS").toString()))
						{
							detail[15] = "未完成";
						}
						else
						{
							detail[15] = "已完成";
						}
						detail[16] = CommonUtils.checkNull(map.get("WH_CNAME"));
						
						list1.add(detail);
					}
				}
			}
			
			String fileName = "库存状态变更汇总信息";
			this.exportEx(fileName, ActionContext.getContext().getResponse(),
					request, head, list1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "导出库存状态变更汇总信息");
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
	 * LastDate    : 2013-4-24
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
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
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
