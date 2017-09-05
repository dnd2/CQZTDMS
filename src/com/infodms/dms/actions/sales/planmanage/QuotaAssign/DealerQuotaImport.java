/**
 * @Title: DealerQuotaImport.java
 *
 * @Description:
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-22
 *
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sales.planmanage.QuotaAssign;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileDownloadAction;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtilDao;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.common.FileUpLoadDAO;
import com.infodms.dms.dao.orgmng.GetOrgIdsOrDealerIdsDAO;
import com.infodms.dms.dao.sales.paraConfigDao.TemplateDownLoadDao;
import com.infodms.dms.dao.sales.planmanage.QuotaAssignDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmpVsQuotaPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.filestore.FileStore;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 *
 */
public class DealerQuotaImport extends BaseImport {

	public Logger logger = Logger.getLogger(AreaQuotaImport.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final QuotaAssignDao dao = QuotaAssignDao.getInstance();

	// --- 作为经销商配额导入优化 ---
	private String dealerCode__ = "";
	private String dealerName__ = "";
	private List<Map<String, String>> groupList__ = new ArrayList<Map<String, String>>();

	private final String dealerQuotaImportUrl = "/jsp/sales/planmanage/quotaassign/dealerQuotaImport.jsp";
	private final String dealerQuotaCheckSuccessUrl = "/jsp/sales/planmanage/quotaassign/dealerQuotaCheckSuccess.jsp";
	private final String dealerQuotaCheckFailureUrl = "/jsp/sales/planmanage/quotaassign/dealerQuotaCheckFailure.jsp";
	private final String dealerQuotaCheckNotEqualUrl = "/jsp/sales/planmanage/quotaassign/dealerQuotaCheckNotEqual.jsp";
	private final String dealerQuotaImportCompleteUrl = "/jsp/sales/planmanage/quotaassign/dealerQuotaImportComplete.jsp";

	/**
	 * 经销商配额导入pre
	 */
	public void dealerQuotaImportPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> dateList = dao.getQuotaDateList(oemCompanyId);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			act.setForword(dealerQuotaImportUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商配额导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 经销商配额导入临时表
	 */
	public void dealerQuotaExcelOperate() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String quotaMonth = request.getParamValue("quotaMonth");
			String[] str = quotaMonth.split("-");
			String year = str[0];
			String month = str[1];
			String areaId = request.getParamValue("areaId");

			Map<String, Object> delMap = new HashMap<String, Object>();
			delMap.put("userId", logonUser.getUserId().toString());
			// delMap.put("companyId", logonUser.getOemCompanyId().toString());
			delMap.put("year", year);
			delMap.put("month", month);
			delMap.put("orgId", logonUser.getOrgId().toString());
			delMap.put("areaId", areaId);

			dao.delDealerQuotaDtl(delMap);
			dao.delDealerQuota(delMap);

			// 清空临时表中的数据
			TmpVsQuotaPO po = new TmpVsQuotaPO();
			po.setUserId(logonUser.getUserId().toString());
			dao.delete(po);

			// 判断是否有已下发的配额
			String companyId = logonUser.getCompanyId().toString();
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsStr(orgId, parentOrgId, dutyType, "tvq");

			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("quotaYear", year);
			paraMap.put("quotaMonth", month);
			paraMap.put("areaId", areaId);
			paraMap.put("companyId", companyId);
			paraMap.put("dealerSql", dealerSql);

			List<Map<String, Object>> quotaList = dao.getDealerQuotaIssuedList(paraMap);

			List<ExcelErrors> errorList = new ArrayList<ExcelErrors>();

			// 判断是否有已下发的配额
			if (quotaList.size() != 0) {
				ExcelErrors ees = new ExcelErrors();
				ees.setRowNum(0);
				ees.setErrorDesc("您要导入的该月、该业务范围的配额已经下发，不能再次导入");
				errorList.add(ees);
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				long maxSize = 1024 * 1024 * 5;
				map.put("companyId", companyId);
				map.put("areaId", areaId);
				map.put("year", year);
				map.put("month", month);

				List<Map<String, Object>> downWeeks = dao.getTempDownWeek(map);

				insertIntoTmp(request, "uploadFile", 6 + downWeeks.size(), 3, maxSize);
				errorList = getErrList();
			}

			if (errorList != null && errorList.size() > 0) {
				act.setOutData("errorList", errorList);
				act.setForword(dealerQuotaCheckFailureUrl);
			} else {
				List<Map> list = getMapList();
				// 将数据插入临时表
				insertTmp(list, new Long(areaId), logonUser.getUserId(), year, month);
				// 校验临时表数据
				errorList = checkData(logonUser, new Long(areaId));
				if (errorList != null && errorList.size() != 0) {
					act.setOutData("errorList", errorList);
					act.setForword(dealerQuotaCheckFailureUrl);
				} else {
					paraMap = new HashMap<String, Object>();

					dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsStr(orgId, parentOrgId, dutyType, "tda");

					paraMap.put("areaId", areaId);
					paraMap.put("quotaYear", year);
					paraMap.put("quotaMonth", month);
					paraMap.put("orgId", logonUser.getOrgId().toString());
					paraMap.put("companyId", logonUser.getCompanyId().toString());
					paraMap.put("dealerSql", dealerSql);
					paraMap.put("userId", logonUser.getUserId().toString());

					List<Map<String, Object>> notEqualList = dao.getAreaDealerNotEqualList(paraMap);

					if (notEqualList != null && notEqualList.size() != 0) {
						act.setOutData("notEqualList", notEqualList);
						act.setForword(dealerQuotaCheckNotEqualUrl);
					} else {
						Map<String, String> map = new HashMap<String, String>();
						map.put("orgId", orgId);
						map.put("areaId", areaId);
						map.put("year", year);
						map.put("month", month);
						this.fileUpload(map);

						act.setOutData("year", year);
						act.setOutData("month", month);
						act.setOutData("areaId", areaId);
						act.setForword(dealerQuotaCheckSuccessUrl);
					}
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE, "文件读取错误");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void dealerQuotaTempQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String year = CommonUtils.checkNull(request.getParamValue("year"));
			String month = CommonUtils.checkNull(request.getParamValue("month"));
			String userId = logonUser.getUserId().toString();
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			map.put("userId", userId);
			map.put("companyId", companyId);

			// 配额计算查询
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getDealerQuotaImportTempList(map, curPage, Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商配额导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 把所有导入记录插入TMP_VS_QUOTA
	 */
	private void insertTmp(List<Map> list, Long areaId, Long userId, String year, String month) throws Exception {
		if (null == list) {
			list = new ArrayList();
		}
		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			if (null == map) {
				map = new HashMap<String, Cell[]>();
			}
			Set<String> keys = map.keySet();
			Iterator it = keys.iterator();
			String key = "";
			while (it.hasNext()) {
				key = (String) it.next();
				Cell[] cells = (Cell[]) map.get(key);
				parseCells(key, cells, areaId, userId, year, month);
			}
		}

	}

	/*
	 * 每一行插入TMP_VS_QUOTA 数字只截取30位
	 */
	private void parseCells(String rowNum, Cell[] cells, Long areaId, Long userId, String year, String month) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String companyId = CommonUtils.checkNull(logonUser.getCompanyId());
		map.put("companyId", companyId);
		map.put("areaId", areaId);
		map.put("year", year);
		map.put("month", month);

		List<Map<String, Object>> downWeeks = dao.getTempDownWeek(map);

		for (int i = 0; i < downWeeks.size(); i++) {
			TmpVsQuotaPO po = new TmpVsQuotaPO();
			po.setRowNumber(rowNum.trim());
			po.setAreaId(areaId);
			po.setQuotaYear(year.trim());
			po.setQuotaMonth(month.trim());
			po.setQuotaWeek(subCell(downWeeks.get(i).get("SET_WEEK").toString()));
			// 获得经销商名称
			String dealerCode = subCell(cells[0].getContents().trim());

			if (dealerCode__.equals(dealerCode)) {
				po.setDealerCode(dealerCode);
				po.setDealerName(dealerName__);
			} else {
				TmDealerPO dealerPo = new TmDealerPO();
				dealerPo.setDealerCode(dealerCode);
				dealerPo.setStatus(Constant.STATUS_ENABLE);
				List<PO> list = dao.select(dealerPo);
				dealerPo = list.size() != 0 ? (TmDealerPO) list.get(0) : null;

				dealerCode__ = dealerCode;
				dealerName__ = dealerPo == null ? "" : dealerPo.getDealerShortname();

				po.setDealerCode(dealerCode);
				po.setDealerName(dealerName__);
			}

			TmVhclMaterialGroupPO groupPo = new TmVhclMaterialGroupPO();
			String groupCode = subCell(cells[3].getContents().trim());

			int length = groupList__.size();
			boolean isSame = false;

			for (int m = 0; m < length; m++) {
				if (groupList__.get(m).get(groupCode) != null) {
					po.setGroupCode(groupCode);
					po.setGroupName(groupList__.get(m).get(groupCode));

					isSame = true;

					break;
				}
			}

			if (!isSame) {
				// 获得配置名称
				groupPo.setGroupCode("'"+groupCode+"'");
				groupPo.setStatus(Constant.STATUS_ENABLE);
				List<PO> list = dao.select(groupPo);
				groupPo = list.size() != 0 ? (TmVhclMaterialGroupPO) list.get(0) : null;

				Map<String, String> groupMap = new HashMap<String, String>();
				groupMap.put(groupCode, groupPo == null ? "" : groupPo.getGroupName());
				groupList__.add(groupMap);

				po.setGroupCode(groupCode);
				po.setGroupName(groupMap.get(groupCode));
			}

            if (cells.length>i+5) {
                po.setQuotaAmt(subCell(cells[i + 5].getContents().trim()));
            }
            po.setUserId(userId.toString());
			dao.insert(po);
		}
	}

	/*
	 * 将输入字符截取最多30位
	 */
	private String subCell(String orgAmt) {
		String newAmt = "";
		if (null == orgAmt || "".equals(orgAmt)) {
			return newAmt;
		}
		if (orgAmt.length() > 30) {
			newAmt = orgAmt.substring(0, 30);
		} else {
			newAmt = orgAmt;
		}
		return newAmt;
	}

	/*
	 * 校验临时表中数据是否符合导入标准
	 */
	private List<ExcelErrors> checkData(AclUserBean userBean, Long areaId) {
		TmpVsQuotaPO quotaPo = new TmpVsQuotaPO();
		quotaPo.setUserId(userBean.getUserId().toString());
		List<PO> list = dao.select(quotaPo);

		StringBuffer errorInfo = new StringBuffer();
		boolean isError = false;
		List<ExcelErrors> errorList = new ArrayList<ExcelErrors>();

		for (int i = 0; i < list.size(); i++) {
			ExcelErrors errors = new ExcelErrors();
			// 取得TmpVsQuotaPO
			TmpVsQuotaPO po = (TmpVsQuotaPO) list.get(i);
			// 取得行号
			String rowNum = po.getRowNumber();
			try {
				if (po.getQuotaYear().indexOf("-") != -1) {
					isError = true;
					errorInfo.append("年度必须是正整数,");
				}
				new Integer(po.getQuotaYear());
			} catch (Exception e) {
				isError = true;
				errorInfo.append("年度必须是正整数,");
			}
			try {
				if (po.getQuotaMonth().indexOf("-") != -1) {
					isError = true;
					errorInfo.append("月度必须是正整数,");
				}
				new Integer(po.getQuotaMonth());
			} catch (Exception e) {
				isError = true;
				errorInfo.append("月度必须是正整数,");
			}
			try {
				if (po.getQuotaWeek().indexOf("-") != -1) {
					isError = true;
					errorInfo.append("周度必须是正整数,");
				}
				new Integer(po.getQuotaWeek());
			} catch (Exception e) {
				isError = true;
				errorInfo.append("周度必须是正整数,");
			}
			try {
				if (po.getQuotaAmt().indexOf("-") != -1) {
					isError = true;
					errorInfo.append("配额数量必须是正整数,");
				}
				new Integer(po.getQuotaAmt());
			} catch (Exception e) {
				isError = true;
				errorInfo.append("配额数量必须是正整数,");
			}

			if (errorInfo.length() > 0) {
				String info = errorInfo.substring(0, errorInfo.length() - 1);
				errors.setRowNum(new Integer(rowNum));
				errors.setErrorDesc(info);
				errorList.add(errors);
				errorInfo.delete(0, errorInfo.length());
			}
		}

		Map<String, Object> conMap = new HashMap<String, Object>();
		// String groupArea = PlanUtil.getAllGroupInArea(areaId.toString(),
		// userBean.getCompanyId().toString(), "2", 4);
		PlanUtilDao pu = new PlanUtilDao();
		String groupArea = pu.selectAreaGroupStr(areaId.toString(), userBean.getCompanyId().toString(), 4);
		conMap.put("userId", userBean.getUserId().toString());
		conMap.put("companyId", userBean.getCompanyId().toString());
		conMap.put("areaId", areaId.toString());
		conMap.put("groupArea", groupArea);
		conMap.put("orgId", userBean.getOrgId().toString());
		conMap.put("quotaType", Constant.QUOTA_TYPE_02.toString());

		// 配额导入校验经销商是否存在
		List<Map<String, Object>> notExistsOrgList = new ArrayList<Map<String, Object>>();
		String para = CommonDAO.getPara(Constant.CHANA_SYS.toString());

		if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
			notExistsOrgList = dao.quotaImportCheckDealer(conMap);
		} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
			notExistsOrgList = dao.quotaImportCheckDealer_CVS(conMap);
		} else {
			throw new RuntimeException("判断当前系统的系统参数错误！");
		}

		if (null != notExistsOrgList && notExistsOrgList.size() > 0) {
			for (int i = 0; i < notExistsOrgList.size(); i++) {
				Map<String, Object> map = notExistsOrgList.get(i);
				isError = true;
				ExcelErrors err = new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("经销商代码不存在");
				errorList.add(err);
			}
		}
		// 配额导入校验配置代码是否存在
		List<Map<String, Object>> notExistsGroupList = dao.quotaImportCheckGroup(conMap);
		if (null != notExistsGroupList && notExistsGroupList.size() > 0) {
			for (int i = 0; i < notExistsGroupList.size(); i++) {
				Map<String, Object> map = notExistsGroupList.get(i);
				isError = true;
				ExcelErrors err = new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("配置代码不存在");
				errorList.add(err);
			}
		}
		// 配额导入校验配置是否与业务范围一致
		List<Map<String, Object>> notExistsGroupAreaList = dao.quotaImportCheckGroupArea(conMap);
		if (null != notExistsGroupAreaList && notExistsGroupAreaList.size() > 0) {
			for (int i = 0; i < notExistsGroupAreaList.size(); i++) {
				Map<String, Object> map = notExistsGroupAreaList.get(i);
				isError = true;
				ExcelErrors err = new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("配置与业务范围不一致");
				errorList.add(err);
			}
		}
		// 配额导入校验经销商是否与业务范围一致
		/*
		 * List<Map<String, Object>> notExistsOrgAreaList = dao
		 * .quotaImportCheckDealerArea(conMap); if (null != notExistsOrgAreaList &&
		 * notExistsOrgAreaList.size() > 0) { for (int i = 0; i <
		 * notExistsOrgAreaList.size(); i++) { Map<String, Object> map =
		 * notExistsOrgAreaList.get(i); isError = true; ExcelErrors err = new
		 * ExcelErrors(); err.setRowNum(Integer
		 * .parseInt(map.get("ROW_NUMBER").toString()));
		 * err.setErrorDesc("经销商不具有此业务范围"); errorList.add(err); } }
		 */
		// 校验周次在TM_DATE_SET工作日历中是否存在
		List<Map<String, Object>> checkDateSetWeek = dao.checkDateSetWeek(conMap);
		if (null != checkDateSetWeek && checkDateSetWeek.size() > 0) {
			for (int i = 0; i < checkDateSetWeek.size(); i++) {
				Map<String, Object> map = checkDateSetWeek.get(i);
				isError = true;
				ExcelErrors err = new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("周次错误");
				errorList.add(err);
			}
		}

		// chkQuataAmount(userBean, areaId, errorInfo, isError) ;
		// 临时表校验重复数据
		List<Map<String, Object>> dumpList = dao.talbeCheckDump(conMap);
		if (null != dumpList && dumpList.size() > 0) {

			String r1 = "";
			String r2 = "";
			List<String> tmp = new ArrayList<String>();
			String s1 = "";
			String s2 = "";
			for (int i = 0; i < dumpList.size(); i++) {
				Map<String, Object> map = dumpList.get(i);
				r1 = map.get("ROW_NUMBER1").toString();
				r2 = map.get("ROW_NUMBER2").toString();
				s1 = r1 + "," + r2;
				s2 = r2 + "," + r1;
				if (tmp.contains(s1) || tmp.contains(s2)) {
					continue;
				} else {
					isError = true;
					ExcelErrors err = new ExcelErrors();
					err.setRowNum(Integer.parseInt(r1));
					err.setErrorDesc("与" + r2 + "行数据重复");
					errorList.add(err);
					tmp.add(s1);
				}

			}
		}

		if (isError) {
			return errorList;
		} else {
			return null;
		}
	}

	/*
	 * 导入业务表 update 2012.12.12
	 */
	public void importExcel() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String year = request.getParamValue("year");
			String month = request.getParamValue("month");
			String areaId = request.getParamValue("areaId");
			String orgId = logonUser.getOrgId().toString();
			String userId = logonUser.getUserId().toString();
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("companyId", companyId);
			map.put("year", year);
			map.put("month", month);
			map.put("orgId", orgId);
			map.put("areaId", areaId);

			if (null == year || "".equals(year)) {
				throw new Exception("导入失败!");
			}
			if (null == month || "".equals(month)) {
				throw new Exception("导入失败!");
			}
			// 清空业务表中，该用户已导入未下发的配额
			dao.delDealerQuotaDtl(map);
			dao.delDealerQuota(map);
			dao.insertDealerQuota(map);
			dao.insertDealerQuotaDetail(map);

			//删除临时表中的数据start
			TmpVsQuotaPO po = new TmpVsQuotaPO();
			po.setUserId(logonUser.getUserId().toString());
			po.setQuotaYear(year);
			po.setQuotaMonth(month);
			//查询orgcode
			TmOrgPO tmp=new TmOrgPO();
			tmp.setOrgId(Long.parseLong(orgId));
			tmp=(TmOrgPO)dao.select(tmp).get(0);
			po.setOrgCode(tmp.getOrgCode());
			po.setAreaId(Long.parseLong(areaId));
			dao.delete(po);
			//end
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setForword(dealerQuotaImportCompleteUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商配额导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 下载模板
	 */
	public void templateDownLoad() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			TemplateDownLoadDao dao = TemplateDownLoadDao.getInstance();
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			String fileId = String.valueOf(Constant.PLAN_TEMPLATE_01);
			String companyId = GetOemcompanyId.getOemCompanyId(logonUser).toString();
			Map<String, Object> map = dao.selectTemplateParaConfigMap(fileId, companyId);

			String filePath = map.get("PARA_VALUE").toString() + "\\";
			String fileName = "";
			String old = map.get("PARA_NAME").toString() + ".xls";
			FileDownloadAction.downloadTemplate(response, fileName, filePath, old);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void downLoadDEL() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		QuotaAssignDao dao = QuotaAssignDao.getInstance();
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();

			String orgId = CommonUtils.checkNull(logonUser.getOrgId());
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String quotaMonth = CommonUtils.checkNull(request.getParamValue("quotaMonth"));
			String companyId = CommonUtils.checkNull(logonUser.getCompanyId());
			String year = "";
			String month = "";
			if (null != quotaMonth && !"".equals(quotaMonth)) {
				year = quotaMonth.split("-")[0].toString();
				month = quotaMonth.split("-")[1].toString();
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("companyId", companyId);
			map.put("orgId", orgId);
			map.put("areaId", areaId);
			map.put("year", year);
			map.put("month", month);
            map.put("userId",logonUser.getUserId().toString());
			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();

			// 标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("经销商代码");
			listHead.add("经销商名称");
			listHead.add("省份");
			listHead.add("配置代码");
			listHead.add("配置名称");

            List<Map<String, Object>> downWeeks = dao.getTempDownWeek2(map);
            for (int i = 0; i < downWeeks.size(); i++) {
                listHead.add("第" + downWeeks.get(i).get("SET_WEEK") != null ? downWeeks.get(i).get("SET_WEEK") : "" + "周");
            }
            listHead.add("预测数量");
            list.add(listHead);
//该配额计算存储过程已移动到车厂下发区域配额之后调用
/*			// 调用配额计算存储过程
			List<Object> ins = new LinkedList<Object>();
			ins.add(logonUser.getUserId());
			ins.add(areaId);
			ins.add(orgId);
			ins.add(year);
			ins.add(month);

			List<Integer> outs = new LinkedList<Integer>();

			dao.callProcedure("P_AUTO_DISP_QUOTA_TO_DLR", ins, outs);*/


			List<Map<String, Object>> orgSeriesList = dao.downLoadDEL(map);
			for (int i = 0; i < orgSeriesList.size(); i++) {
				List<Object> listValue = new LinkedList<Object>();
				listValue.add(orgSeriesList.get(i).get("DEALER_CODE") != null ? orgSeriesList.get(i).get("DEALER_CODE") : "");
				listValue.add(orgSeriesList.get(i).get("DEALER_NAME") != null ? orgSeriesList.get(i).get("DEALER_NAME") : "");
				listValue.add(orgSeriesList.get(i).get("PROVINCE") != null ? orgSeriesList.get(i).get("PROVINCE") : "");
				listValue.add(orgSeriesList.get(i).get("GROUP_CODE") != null ? orgSeriesList.get(i).get("GROUP_CODE") : "");
				listValue.add(orgSeriesList.get(i).get("GROUP_NAME") != null ? orgSeriesList.get(i).get("GROUP_NAME") : "");

                List<Map<String, Object>> downWeeks2 = dao.getTempDownWeek(map);

                for (int j = 0; j < downWeeks2.size(); j++) {
                    int week=((BigDecimal)downWeeks2.get(j).get("SET_WEEK")).intValue();
                    String str="A"+week;
                    listValue.add(orgSeriesList.get(i).get(str) != null ? orgSeriesList.get(i).get(str) : "");
				}
                listValue.add(orgSeriesList.get(i).get("FORECAST_AMOUNT") != null ? orgSeriesList.get(i).get("FORECAST_AMOUNT") : "");
				list.add(listValue);
			}

			// 导出的文件名
			String fileName = "经销商配额模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			os = response.getOutputStream();
			// CsvWriterUtil.writeCsv(list, os);
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE, "文件读取错误");
			logger.error(logonUser, e1);
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

	/*
	 * public void chkQuataAmount(AclUserBean userBean,Long areaId,StringBuffer
	 * errorInfo,boolean isError) { Map<String, String> mapS1 = new HashMap<String,
	 * String>() ; mapS1.put("userId", userBean.getUserId().toString()) ;
	 *
	 * List<Map<String, Object>> ListS1 = dao.getOrgQuata(mapS1) ;
	 *
	 * int s1Len = ListS1.size() ; if(s1Len == 0) { isError = true ;
	 * errorInfo.append("对应大区经销商不存在,") ; } else { Map<String, String> mapS2 =
	 * new HashMap<String, String>() ; mapS2.put("year",
	 * ListS1.get(0).get("QUOTA_YEAR").toString()) ; mapS2.put("month",
	 * ListS1.get(0).get("QUOTA_MONTH").toString()) ; mapS2.put("orgId",
	 * ListS1.get(0).get("ORG_ID").toString()) ; mapS2.put("quotaType",
	 * Constant.QUOTA_TYPE_01.toString()) ; mapS2.put("areaId",
	 * areaId.toString()) ;
	 *
	 * List<Map<String, Object>> ListS2 = dao.getOrgQuataByOem(mapS2) ;
	 *
	 * int s2Len = ListS2.size() ;
	 *
	 * if(s1Len != s2Len) { isError = true ;
	 * errorInfo.append("大区下发配置配额与总部下发配置配额不匹配,") ; }
	 *
	 * for(int i=0; i<s2Len; i++) { Map<String, Object> mS2 = ListS2.get(i) ;
	 *
	 * String theWeek = mS2.get("QUOTA_WEEK").toString() ; String theGroup =
	 * mS2.get("GROUP_CODE").toString() ; String theAmount =
	 * mS2.get("AMOUNT").toString() ; for(int j=0; j<s1Len; j++) { Map<String,
	 * Object> mS1 = ListS1.get(j) ; String tempWeek =
	 * mS1.get("QUOTA_WEEK").toString() ; String tempGroup =
	 * mS1.get("GROUP_CODE").toString() ; String tempAmount =
	 * mS1.get("AMOUNT").toString() ;
	 *
	 * if(tempGroup == theGroup && tempWeek == theWeek) { if(tempAmount !=
	 * theAmount) { isError = true ; errorInfo.append("第" + theWeek + "周配置代码为" +
	 * theGroup + "与总部下发数量不匹配,") ; } } } } } }
	 */

	public void fileUpload(Map<String, String> map) throws Exception {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			// added by andy.ten@tom.com
			if (act.getException() != null) {
				throw new Exception(act.getException().getMessage());
			}
			// end
			RequestWrapper request = act.getRequest();
			FileObject uploadFile = request.getParamObject("uploadFile");
			/*
			 * if(uploadFile.getLength() > 1024*1000*2) { throw new
			 * Exception("上传文件限制大小为2M，服务器资源有限，请节约使用！"); }
			 */
			String fileName = uploadFile.getFileName();
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
			// ByteArrayInputStream is = new
			// ByteArrayInputStream(uploadFile.getContent());
			FileStore store = FileStore.getInstance();
			// 上传到文件服务器并获取文件ID
			String fileid = store.write(uploadFile.getFileName(), uploadFile.getContent());

			String fileUrl = store.getDomainURL(fileid);
			FsFileuploadPO po = new FsFileuploadPO();
			po.setFileid(fileid);
			po.setFileurl(fileUrl);
			po.setFilename(this.getFileName(map));
			po.setYwzj(new Long("20120101"));
			FileUpLoadDAO dao = new FileUpLoadDAO();
			dao.addDisableFile(po, logonUser);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "文件上传失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public String getFileName(Map<String, String> map) {
		StringBuffer StrBuf = new StringBuffer("");
		String orgId = map.get("orgId");
		String areaId = map.get("areaId");
		String year = map.get("year");
		String month = map.get("month");

		TmOrgPO top = new TmOrgPO();
		top.setOrgId(Long.parseLong(orgId));

		top = (TmOrgPO) dao.select(top).get(0);

		StrBuf.append(top.getOrgName());

		TmBusinessAreaPO tba = new TmBusinessAreaPO();
		tba.setAreaId(Long.parseLong(areaId));

		tba = (TmBusinessAreaPO) dao.select(tba).get(0);

		StrBuf.append(tba.getAreaName());

		StrBuf.append(year).append("-").append(month);

		return StrBuf.toString();
	}
}
