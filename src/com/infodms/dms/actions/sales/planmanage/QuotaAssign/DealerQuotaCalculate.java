/**
 * @Title: DealerQuotaCalculate.java
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.planmanage.QuotaAssignDao;
import com.infodms.dms.dao.sales.salesConsultant.SalesConsultantDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class DealerQuotaCalculate {
	private Logger logger = Logger.getLogger(DealerQuotaCalculate.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final QuotaAssignDao dao = QuotaAssignDao.getInstance();

	private final String DEALER_QUOTA_CALCULATE_QUERY_URL = "/jsp/sales/planmanage/quotaassign/dealerQuotaCalculateQuery.jsp";// 经销商配额计算查询页面

	// 区域配额计算pre
	public void dealerQuotaCalculatePre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> dateList = dao
					.getQuotaDateList(oemCompanyId);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getPoseIdBusiness(poseId.toString());
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			act.setForword(DEALER_QUOTA_CALCULATE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商配额计算");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void dealerQuotaCalculate() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String quotaDate = CommonUtils.checkNull(request
					.getParamValue("quotaDate"));
			String[] array = quotaDate.split("-");
			String year = array[0];
			String month = array[1];
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String curType = CommonUtils.checkNull(request
					.getParamValue("curType"));
			String companyId = logonUser.getCompanyId().toString();
			String orgId = logonUser.getOrgId().toString();
			String userId = logonUser.getUserId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			map.put("areaId", areaId);
			map.put("curType", curType);
			map.put("userId", userId);
			map.put("companyId", companyId);

			// 调用配额计算存储过程
			List<Object> ins = new LinkedList<Object>();
			ins.add(areaId);
			ins.add(companyId);
			ins.add(orgId);
			ins.add(userId);

			List<Integer> outs = new LinkedList<Integer>();

			if (curType.equals(String.valueOf(Constant.QUOTA_CALCULATE_TYPE_01))) {
				dao.callProcedure("P_CALCULATE_DLR_QUOTA_JQPJ", ins, outs);
			} else {
				dao.callProcedure("P_CALCULATE_DLR_QUOTA_KCSD", ins, outs);
			}

			// 配额计算查询
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getDealerQuotaCalculateQueryList(map, curPage,
							Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商配额计算");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void dealerQuotaCalculateQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String quotaDate = CommonUtils.checkNull(request
					.getParamValue("quotaDate"));
			String[] array = quotaDate.split("-");
			String year = array[0];
			String month = array[1];
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String curType = CommonUtils.checkNull(request
					.getParamValue("curType"));
			String userId = logonUser.getUserId().toString();
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			map.put("areaId", areaId);
			map.put("curType", curType);
			map.put("userId", userId);
			map.put("companyId", companyId);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getDealerQuotaCalculateQueryList(map, curPage,
							Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商配额计算");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void toChk() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		String quotaDate = CommonUtils.checkNull(request.getParamValue("quotaDate"));
		String[] array = quotaDate.split("-");
		String year = array[0];
		String month = array[1];
		String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
		String curType = CommonUtils.checkNull(request.getParamValue("curType"));
		String userId = logonUser.getUserId().toString();
		String companyId = logonUser.getOemCompanyId();
		String dutyType = logonUser.getDutyType() ;
		String orgId = logonUser.getOrgId().toString() ;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("year", year);
		map.put("month", month);
		map.put("areaId", areaId);
		map.put("curType", curType);
		map.put("userId", userId);
		map.put("companyId", companyId);
		map.put("dutyType", dutyType) ;
		map.put("orgId", orgId) ;

		List<Map<String, Object>> headList = dao.getHeadList(map) ;
		
		if(CommonUtils.isNullList(headList)) 
			act.setOutData("flag", "error") ;
	}

	public void dealerQuotaCalculateExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String quotaDate = CommonUtils.checkNull(request.getParamValue("quotaDate"));
			String[] array = quotaDate.split("-");
			String year = array[0];
			String month = array[1];
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String curType = CommonUtils.checkNull(request.getParamValue("curType"));
			String userId = logonUser.getUserId().toString();
			String companyId = logonUser.getOemCompanyId();
			String dutyType = logonUser.getDutyType() ;
			String orgId = logonUser.getOrgId().toString() ;

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			map.put("areaId", areaId);
			map.put("curType", curType);
			map.put("userId", userId);
			map.put("companyId", companyId);
			map.put("dutyType", dutyType) ;
			map.put("orgId", orgId) ;
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			//if(!CommonUtils.isNullList(headList)) {
				// 导出的文件名
				String fileName = "经销商配额计算.xls";
				// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
				response.setContentType("Application/text/csv");
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
				
				List<List<Object>> list = new LinkedList<List<Object>>();
				
				List<Map<String, Object>> weekList = dao.getWeek(map) ;
				
				int length = weekList.size() ;
				
				List<Object> listTemp = new LinkedList<Object>();
				listTemp.add("经销商代码");
				listTemp.add("经销商名称");
				listTemp.add("省份");
				listTemp.add("配置代码");
				// listTemp.add("年度");
				
				for(int i=0; i<length; i++) {
					listTemp.add(weekList.get(i).get("QUOTA_WEEK").toString() + "周") ;
				}
				
				// listTemp.add("周度");
				// listTemp.add("配额数量");
				list.add(listTemp);
	
				List<Map<String, Object>> headList = new ArrayList<Map<String, Object>>() ;
				List<Map<String, Object>> results = new ArrayList<Map<String, Object>>() ;
				
				if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
					headList = dao.getHeadList(map) ;
					results = dao.getDealerQuotaCalculateExportList(map);
				} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
					headList = dao.getHeadList_CVS(map) ;
					results = dao.getDealerQuotaCalculateExportList_CVS(map);
				} else {
					throw new RuntimeException("判断当前系统的系统参数错误！") ;
				}
				
				for (int i = 0; i < headList.size(); i++) {
					Map<String, Object> record = headList.get(i);
					
					listTemp = new LinkedList<Object>();
					listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
					listTemp.add(CommonUtils.checkNull(record.get("DEALER_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("REGION_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
					// listTemp.add(CommonUtils.checkNull(record.get("QUOTA_YEAR")));
					
					for(int j=0; j<length; j++) {
						int n = i * length + j ;
						listTemp.add(results.get(n).get("QUOTA_AMT").toString()) ;
					}
					
					// listTemp.add(CommonUtils.checkNull(record.get("QUOTA_WEEK")));
					// listTemp.add(CommonUtils.checkNull(record.get("QUOTA_AMT")));
					list.add(listTemp);
				}
				
				os = response.getOutputStream();
				CsvWriterUtil.createXlsFile(list, os);
				os.flush();
			//} 
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商配额计算");
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
}
