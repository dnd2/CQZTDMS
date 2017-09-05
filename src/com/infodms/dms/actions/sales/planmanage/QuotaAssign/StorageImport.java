package com.infodms.dms.actions.sales.planmanage.QuotaAssign;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtilDao;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.planmanage.QuotaAssignDao;
import com.infodms.dms.dao.sales.planmanage.StorageImportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmpVsQuotaPO;
import com.infodms.dms.po.TtVsQuotaPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * 2012-12-4
 * @author yinshunhui
 *
 */
public class StorageImport extends BaseImport {
	private Logger logger=Logger.getLogger(StorageImport.class);
	private ActionContext act=ActionContext.getContext();
	RequestWrapper request=act.getRequest();
	ResponseWrapper response=act.getResponse();
	private final StorageImportDao  dao=StorageImportDao.getInstance();
	private static final QuotaAssignDao quotaAssignDao = new QuotaAssignDao();
	private final String storageImportUrl = "/jsp/sales/planmanage/quotaassign/storageImport.jsp";
	private final String storageCheckFailureUrl = "/jsp/sales/planmanage/quotaassign/storageCheckFailure.jsp";
	private final String storageCheckSuccessUrl = "/jsp/sales/planmanage/quotaassign/storageCheckSuccess.jsp";
	private final String storageImportCompleteUrl = "/jsp/sales/planmanage/quotaassign/storageImportComplete.jsp";
	/**
	 * 车厂配额导入之前
	 */
	public void storageImportPre(){
		AclUserBean logonUser=(AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> dateList = dao
					.getStorageDateList(oemCompanyId);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getPoseIdBusiness(poseId.toString());
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			act.setForword(storageImportUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车厂配额导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 车厂配额模版导出
	 */
	public void downLoad(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		QuotaAssignDao dao = QuotaAssignDao.getInstance();
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			
			// 公司ID
			String companyId = CommonUtils.checkNull(logonUser.getCompanyId());
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String quotaMonth = CommonUtils.checkNull(request.getParamValue("quotaMonth")) ;
			String moterIds=CommonUtils.checkNull(request.getParamValue("modelId"));
			String year = "" ;
			String month = "" ;
			if(null != quotaMonth && !"".equals(quotaMonth)) {
				year = quotaMonth.split("-")[0].toString() ;
				month = quotaMonth.split("-")[1].toString() ;
			} 
			Map<String, Object> map = new HashMap<String, Object>() ;
			map.put("companyId", companyId) ;
			map.put("areaId", areaId) ;
			map.put("year", year) ;
			map.put("month", month) ;
			map.put("moterIds", moterIds);
			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("配置代码");
			listHead.add("配置名称");
			List<Map<String, Object>> downWeeks = quotaAssignDao.getTempDownWeek2(map);
			for(int i=0;i<downWeeks.size();i++){
				listHead.add("第"+downWeeks.get(i).get("SET_WEEK") != null ? downWeeks.get(i).get("SET_WEEK") : ""+"周");
			}
			list.add(listHead);

			List<Map<String, Object>> orgSeriesList = dao.getTempDownload2(map);
			for(int i=0; i<orgSeriesList.size();i++){
				List<Object> listValue = new LinkedList<Object>();
				listValue.add(orgSeriesList.get(i).get("GROUP_CODE") != null ? orgSeriesList.get(i).get("GROUP_CODE") : "");
				listValue.add(orgSeriesList.get(i).get("GROUP_NAME") != null ? orgSeriesList.get(i).get("GROUP_NAME") : "");
				for(int j=0;j<downWeeks.size();j++){
				listValue.add("0");
				}
				list.add(listValue);
			}
			
			// 导出的文件名
			String fileName = "车厂配额模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
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
	 * 车厂配额导入临时表
	 */
	public void storageExcelOperate(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String quotaMonth = request.getParamValue("quotaMonth");
			String[] str = quotaMonth.split("-");
			String year = str[0];
			String month = str[1];
			String areaId = request.getParamValue("areaId");

			Map<String, Object> delMap = new HashMap<String, Object>();
			delMap.put("areaId", areaId);
			delMap.put("year", year);
			delMap.put("month", month);
			delMap.put("userId", logonUser.getUserId().toString());
			//delMap.put("companyId", logonUser.getOemCompanyId().toString());

			
			dao.deleteOrgQuota(delMap);
			
			// 清空临时表中的数据
			TmpVsQuotaPO po = new TmpVsQuotaPO();
			po.setUserId(logonUser.getUserId().toString());
			dao.delete(po);
			// 判断是否有已下发的配额
			TtVsQuotaPO quota = new TtVsQuotaPO();
			quota.setQuotaYear(new Integer(year));
			quota.setQuotaMonth(new Integer(month));
			quota.setAreaId(new Long(areaId));
			quota.setCompanyId(logonUser.getCompanyId());
			quota.setQuotaType(Constant.QUOTA_TYPE_03);
			quota.setStatus(Constant.QUOTA_STATUS_02);
			List<PO> quotaList = dao.select(quota);

			List<ExcelErrors> errorList = new ArrayList<ExcelErrors>();
			if(quotaList.size()!=0){
				ExcelErrors ees = new ExcelErrors();
				ees.setRowNum(0);
				ees.setErrorDesc("您要导入的该月、该业务范围的配额已经确认，不能再次导入");
				errorList.add(ees);
			}else{
				Map<String, Object> map = new HashMap<String, Object>() ;
				long maxSize = 1024 * 1024 * 5;
				String companyId = CommonUtils.checkNull(logonUser.getCompanyId());
				map.put("companyId", companyId) ;
				map.put("areaId", areaId) ;
				map.put("year", year) ;
				map.put("month", month) ;
				
				List<Map<String, Object>> downWeeks = dao.getTempDownWeek(map);
				
				insertIntoTmp(request, "uploadFile", 2+downWeeks.size(), 3, maxSize);
				errorList = getErrList();
			}
				
			if (errorList != null && errorList.size() > 0) {
				act.setOutData("errorList", errorList);
				act.setForword(storageCheckFailureUrl);
			} else {
				List<Map> list = getMapList();
				// 将数据插入临时表
				insertTmp(list, new Long(areaId), logonUser.getUserId(), year, month);
				// 校验临时表数据
				errorList = checkData(logonUser.getUserId(), logonUser.getCompanyId(), new Long(areaId));
				if (errorList != null && errorList.size() != 0) {
					act.setOutData("errorList", errorList);
					act.setForword(storageCheckFailureUrl);
				} else {
					act.setOutData("year", year);
					act.setOutData("month", month);
					act.setOutData("areaId", areaId);
					act.setForword(storageCheckSuccessUrl);
				}
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE, "文件读取错误");
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
		Map<String, Object> map = new HashMap<String, Object>() ;
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String companyId = CommonUtils.checkNull(logonUser.getCompanyId());
		map.put("companyId", companyId) ;
		map.put("areaId", areaId) ;
		map.put("year", year) ;
		map.put("month", month) ;

		List<Map<String, Object>> downWeeks = dao.getTempDownWeek(map);
		
		for (int i = 0; i < downWeeks.size(); i++) {
			TmpVsQuotaPO po = new TmpVsQuotaPO();
			po.setRowNumber(rowNum.trim());
			po.setAreaId(areaId);
			po.setQuotaYear(year.trim());
			po.setQuotaMonth(month.trim());
			po.setQuotaWeek(downWeeks.get(i).get("SET_WEEK").toString());
			// 获得组织名称
			Long org_id=logonUser.getOrgId();
			TmOrgPO orgPo = new TmOrgPO();
			orgPo.setOrgId(org_id);
			orgPo.setStatus(Constant.STATUS_ENABLE);
			List<PO> listpo = dao.select(orgPo);
			orgPo = listpo.size() != 0 ? (TmOrgPO) listpo.get(0) : null;
			po.setOrgCode(orgPo != null ? orgPo.getOrgCode() : null);
			po.setOrgName(orgPo != null ? orgPo.getOrgName() : null);
			// 获得配置名称
			String groupCode = subCell(cells[0].getContents().trim());
			po.setGroupCode(groupCode);
			TmVhclMaterialGroupPO groupPo = new TmVhclMaterialGroupPO();
			groupPo.setGroupCode(groupCode);
			List<PO> materGroupList=dao.select(groupPo);
			if(materGroupList.get(0)!=null){
				po.setGroupName(((TmVhclMaterialGroupPO)materGroupList.get(0)).getGroupName());
			}
			//获取数量
            if (cells.length > i+2) {
                po.setQuotaAmt(subCell(cells[i + 2].getContents().trim()));
            }
            //设置用户的id
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
	private List<ExcelErrors> checkData(Long userId, Long companyId, Long areaId) {
		TmpVsQuotaPO quotaPo = new TmpVsQuotaPO();
		quotaPo.setUserId(userId.toString());
		List<PO> list = dao.select(quotaPo);

		ExcelErrors errors = null;
		TmpVsQuotaPO po = null;
		StringBuffer errorInfo = new StringBuffer("");
		boolean isError = false;

		List<ExcelErrors> errorList = new ArrayList<ExcelErrors>();
		for (int i = 0; i < list.size(); i++) {
			errors = new ExcelErrors();
			// 取得TmpVsQuotaPO
			po = (TmpVsQuotaPO) list.get(i);
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
		//String groupArea = PlanUtil.getAllGroupInArea(areaId.toString(), companyId.toString(), "2", 4);
		PlanUtilDao pu = new PlanUtilDao() ;
		String groupArea = pu.selectAreaGroupStr(areaId.toString(), null, 4);
		conMap.put("userId", userId.toString());
		conMap.put("companyId", companyId.toString());
//		conMap.put("areaId", areaId.toString());
		conMap.put("groupArea", groupArea);
		conMap.put("quotaType", Constant.QUOTA_TYPE_01.toString());

		// 配额导入校验配置代码是否存在
//		List<Map<String, Object>> notExistsGroupList = dao
//				.quotaImportCheckGroup(conMap);
//		if (null != notExistsGroupList && notExistsGroupList.size() > 0) {
//			for (int i = 0; i < notExistsGroupList.size(); i++) {
//				Map<String, Object> map = notExistsGroupList.get(i);
//				isError = true;
//				ExcelErrors err = new ExcelErrors();
//				err.setRowNum(Integer
//						.parseInt(map.get("ROW_NUMBER").toString()));
//				err.setErrorDesc("配置代码不存在");
//				errorList.add(err);
//			}
//		}
		// 配额导入校验配置是否与业务范围一致
//		List<Map<String, Object>> notExistsGroupAreaList = dao
//				.storageImportCheckGroupArea(conMap);
//		if (null != notExistsGroupAreaList && notExistsGroupAreaList.size() > 0) {
//			for (int i = 0; i < notExistsGroupAreaList.size(); i++) {
//				Map<String, Object> map = notExistsGroupAreaList.get(i);
//				isError = true;
//				ExcelErrors err = new ExcelErrors();
//				err.setRowNum(Integer
//						.parseInt(map.get("ROW_NUMBER").toString()));
//				err.setErrorDesc("配置与业务范围不一致");
//				errorList.add(err);
//			}
//		}
		// 校验周次在TM_DATE_SET工作日历中是否存在
		List<Map<String, Object>> checkDateSetWeek = dao
				.checkDateSetWeek(conMap);
		if (null != checkDateSetWeek && checkDateSetWeek.size() > 0) {
			for (int i = 0; i < checkDateSetWeek.size(); i++) {
				Map<String, Object> map = checkDateSetWeek.get(i);
				isError = true;
				ExcelErrors err = new ExcelErrors();
				err.setRowNum(Integer
						.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("周次错误");
				errorList.add(err);
			}
		}
		// 临时表校验重复数据
//		List<Map<String, Object>> dumpList = dao.talbeCheckDump(conMap);
//		if (null != dumpList && dumpList.size() > 0) {
//
//			String r1 = "";
//			String r2 = "";
//			List<String> tmp = new ArrayList<String>();
//			String s1 = "";
//			String s2 = "";
//			for (int i = 0; i < dumpList.size(); i++) {
//				Map<String, Object> map = dumpList.get(i);
//				r1 = map.get("ROW_NUMBER1").toString();
//				r2 = map.get("ROW_NUMBER2").toString();
//				s1 = r1 + "," + r2;
//				s2 = r2 + "," + r1;
//				if (tmp.contains(s1) || tmp.contains(s2)) {
//					continue;
//				} else {
//					isError = true;
//					ExcelErrors err = new ExcelErrors();
//					err.setRowNum(Integer.parseInt(r1));
//					err.setErrorDesc("与" + r2 + "行数据重复");
//					errorList.add(err);
//					tmp.add(s1);
//				}
//
//			}
//		}

		if (isError) {
			return errorList;
		} else {
			return null;
		}
	}
	/*
	 * 导入业务表 update 2012.12.12 start-end
	 */
	public void importExcel() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String year = request.getParamValue("year");
			String month = request.getParamValue("month");
			String areaId = request.getParamValue("areaId") ;
			String userId = logonUser.getUserId().toString();
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areaId", areaId);
			map.put("year", year);
			map.put("month", month);
			map.put("userId", userId);
			map.put("companyId", companyId);

			if (null == areaId || "".equals(areaId)) {
				throw new Exception("导入失败!");
			}
			if (null == year || "".equals(year)) {
				throw new Exception("导入失败!");
			}
			if (null == month || "".equals(month)) {
				throw new Exception("导入失败!");
			}
			// 清空业务表中，该用户已导入未下发的配额
			dao.deleteOrgQuota(map);
			// 查询要插入到业务表的临时表数据

			dao.insertAreaQuota(map);
			dao.insertAreaQuotaDetail(map);
			
			//删除临时表中的数据start
			TmpVsQuotaPO po = new TmpVsQuotaPO();
			po.setUserId(logonUser.getUserId().toString());
			po.setQuotaYear(year);
			po.setQuotaMonth(month);
			//查询orgcode
			Long orgId=logonUser.getOrgId();
			TmOrgPO tmp=new TmOrgPO();
			tmp.setOrgId(orgId);
			tmp=(TmOrgPO)dao.select(tmp).get(0);
			po.setOrgCode(tmp.getOrgCode());
			po.setAreaId(Long.parseLong(areaId));
			dao.delete(po);//end
			
			//设置页面用的参数
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setForword(storageImportCompleteUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车厂配额导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 导入完成数据展示
	 */
	public void storageTempQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String year = CommonUtils.checkNull(request.getParamValue("year"));
			String month = CommonUtils
					.checkNull(request.getParamValue("month"));
			String userId = logonUser.getUserId().toString();
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			map.put("userId", userId);
			map.put("companyId", companyId);
			//查询组织编码code
			TmOrgPO top=new TmOrgPO();
			Long orgId=logonUser.getOrgId();
			top.setOrgId(orgId);
			List<PO> tmOrgPOList =dao.select(top);
			String orgCode=null;
			if(tmOrgPOList.get(0)!=null){
				 orgCode=((TmOrgPO)tmOrgPOList.get(0)).getOrgCode();
			}
			
			// 配额计算查询
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getStorageImportTempList(map, curPage,
							Constant.PAGE_SIZE_MAX,orgCode);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配额导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
}
