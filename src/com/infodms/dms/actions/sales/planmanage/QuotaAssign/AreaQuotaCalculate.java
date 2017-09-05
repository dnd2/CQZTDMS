/**
 * @Title: AreaQuotaCalculate.java
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.planmanage.QuotaAssignDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
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
public class AreaQuotaCalculate {
	private Logger logger = Logger.getLogger(AreaQuotaCalculate.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final QuotaAssignDao dao = QuotaAssignDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	private final String AREA_QUOTA_CALCULATE_IMPORT_OUT_URL = "/jsp/sales/planmanage/quotaassign/areaImportOutResourck.jsp";
	private final String AREA_QUOTA_CALCULATE_PARA_SET_PRE_URL = "/jsp/sales/planmanage/quotaassign/areaQuotaCalculateParaSetPre.jsp";// 配额模型参数设置页面
	private final String AREA_QUOTA_CALCULATE_QUERY_URL = "/jsp/sales/planmanage/quotaassign/areaQuotaCalculateQuery.jsp";// 区域配额计算查询页面
	private final String AREA_QUOTA_CALCULATE_IMPORT_URL = "/jsp/sales/planmanage/quotaassign/areaImportResource.jsp";// 待分配资源导入页面

	// 区域配额计算pre
	public void areaQuotaCalculatePre() {
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
			act.setForword(AREA_QUOTA_CALCULATE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "区域配额计算");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 配额模型参数设置pre
	public void areaQuotaCalculateParaSetPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmBusinessParaPO paraPO1 = reportDao.getTmBusinessParaPO(
					Constant.QUTOA_FACT_WEIGHT_PARA, oemCompanyId);// 实销权重
			TmBusinessParaPO paraPO2 = reportDao.getTmBusinessParaPO(
					Constant.QUTOA_HOPE_WEIGHT_PARA, oemCompanyId);// 预测权重
			TmBusinessParaPO paraPO3 = reportDao.getTmBusinessParaPO(
					Constant.QUTOA_AIM_WEIGHT_PARA, oemCompanyId);// 目标权重
			act.setOutData("paraPO1", paraPO1);
			act.setOutData("paraPO2", paraPO2);
			act.setOutData("paraPO3", paraPO3);
			act.setForword(AREA_QUOTA_CALCULATE_PARA_SET_PRE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "区域配额计算");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 配额模型参数设置保存
	public void areaQuotaCalculateParaSet() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String paraValue1 = CommonUtils.checkNull(request
					.getParamValue("paraValue1"));
			String paraValue2 = CommonUtils.checkNull(request
					.getParamValue("paraValue2"));
			String paraValue3 = CommonUtils.checkNull(request
					.getParamValue("paraValue3"));

			// 销量指数保存
			TmBusinessParaPO condition1 = new TmBusinessParaPO();
			condition1.setParaId(Constant.QUTOA_FACT_WEIGHT_PARA);
			condition1.setOemCompanyId(logonUser.getCompanyId());

			TmBusinessParaPO value1 = new TmBusinessParaPO();
			value1.setParaValue(paraValue1);

			dao.update(condition1, value1);

			// 预测指数保存
			TmBusinessParaPO condition2 = new TmBusinessParaPO();
			condition2.setParaId(Constant.QUTOA_HOPE_WEIGHT_PARA);
			condition2.setOemCompanyId(logonUser.getCompanyId());

			TmBusinessParaPO value2 = new TmBusinessParaPO();
			value2.setParaValue(paraValue2);

			dao.update(condition2, value2);

			// 目标指数保存
			TmBusinessParaPO condition3 = new TmBusinessParaPO();
			condition3.setParaId(Constant.QUTOA_AIM_WEIGHT_PARA);
			condition3.setOemCompanyId(logonUser.getCompanyId());

			TmBusinessParaPO value3 = new TmBusinessParaPO();
			value3.setParaValue(paraValue3);

			dao.update(condition3, value3);

			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "区域配额计算");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 区域配额计算
	public void areaQuotaCalculate() {
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
			String userId = logonUser.getUserId().toString();
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			map.put("areaId", areaId);
			map.put("userId", userId);
			map.put("companyId", companyId);

			// 调用配额计算存储过程
			List<Object> ins = new LinkedList<Object>();
			ins.add(areaId);
			ins.add(logonUser.getCompanyId().toString());
			ins.add(logonUser.getUserId().toString());

			List<Integer> outs = new LinkedList<Integer>();

			dao.callProcedure("P_CALCULATE_ORG_QUOTA", ins, outs);

			// 配额计算查询
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getAreaQuotaCalculateQueryList(map, curPage,
							Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "区域配额计算");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 区域配额计算查询
	public void areaQuotaCalculateQuery() {
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
			String userId = logonUser.getUserId().toString();
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			map.put("areaId", areaId);
			map.put("userId", userId);
			map.put("companyId", companyId);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getAreaQuotaCalculateQueryList(map, curPage,
							Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "区域配额计算");
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
		String userId = logonUser.getUserId().toString();
		String companyId = logonUser.getCompanyId().toString();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("year", year);
		map.put("month", month);
		map.put("areaId", areaId);
		map.put("userId", userId);
		map.put("companyId", companyId);

		List<Map<String, Object>> headList = dao.getAreaHead(map) ;
		
		if(CommonUtils.isNullList(headList)) 
			act.setOutData("flag", "error") ;
	}

	// 区域配额计算导出
	public void areaQuotaCalculateExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String quotaDate = CommonUtils.checkNull(request.getParamValue("quotaDate"));
			String[] array = quotaDate.split("-");
			String year = array[0];
			String month = array[1];
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String userId = logonUser.getUserId().toString();
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			map.put("areaId", areaId);
			map.put("userId", userId);
			map.put("companyId", companyId);

			List<Map<String, Object>> headList = dao.getAreaHead(map) ;
			
			//if(!CommonUtils.isNullList(headList)) {
				// 导出的文件名
				String fileName = "区域配额计算.csv";
				// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
				response.setContentType("Application/text/csv");
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
				
				List<List<Object>> list = new LinkedList<List<Object>>();
				
				List<Map<String, Object>> weekList = dao.getWeek(map) ;
				
				int length = weekList.size() ;
				
				List<Object> listTemp = new LinkedList<Object>();
				listTemp.add("区域代码");
				listTemp.add("区域名称");
				listTemp.add("配置代码");
				// listTemp.add("年度");
				
				for(int i=0; i<length; i++) {
					listTemp.add(weekList.get(i).get("QUOTA_WEEK").toString() + "周") ;
				}
				
				// listTemp.add("周度");
				// listTemp.add("配额数量");
				list.add(listTemp);
				
				List<Map<String, Object>> results = dao.getAreaQuotaCalculateExportList(map);
				for (int i = 0; i < results.size(); i++) {
					Map<String, Object> record = headList.get(i);
					listTemp = new LinkedList<Object>();
					listTemp.add(CommonUtils.checkNull(record.get("ORG_CODE")));
					listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
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
				CsvWriterUtil.writeCsv(list, os);
				os.flush();
			//}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "区域配额计算");
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
	//导入分配资源
	public void myImportResource(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			int curDay=PlanUtil.getCurrentDay();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String codeStr="2011,2012";
			Map<String, Object> map=PlanUtil.selectBussinessPara(codeStr, logonUser.getCompanyId().toString());
			int startDay=Integer.parseInt(map.get("20121001").toString());
			int endDay=Integer.parseInt(map.get("20121002").toString());
			
			if(startDay>curDay||endDay<curDay){
				act.setForword(AREA_QUOTA_CALCULATE_IMPORT_OUT_URL);
			}else{
				String year=PlanUtil.getCurrentYear();
				int month=Integer.parseInt(PlanUtil.getCurrentMonth());
				int pre=Integer.parseInt(map.get("20111001").toString());
				List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
				List<Map<String, Object>> dateList = dao
				.getQuotaDateList(oemCompanyId);
				act.setOutData("dateList", dateList);
				act.setOutData("areaBusList", areaBusList);
				act.setOutData("year", year);
				act.setOutData("month", month+pre+"");
				act.setForword(AREA_QUOTA_CALCULATE_IMPORT_URL);
			}
	} catch (Exception e) {
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.QUERY_FAILURE_CODE, "导入分配资源跳转！");
		logger.error(logonUser, e1);
		act.setException(e1);
	}
	}
	
	
}
