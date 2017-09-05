package com.infodms.dms.actions.report.reportOne;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.report.dealerCheckReportQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**经销商开票报表
 * @author Administrator
 *
 */
public class DealerCheckReport {
	private Logger logger = Logger.getLogger(DealerCheckReport.class) ;
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest() ;
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private dealerCheckReportQueryDao dao = dealerCheckReportQueryDao.getInstance();
	
	private final String DEALER_CHECK_QUERY_URL = "/jsp/report/sales/dealercheckReportQuery.jsp";
	/*
	 * 经销商开票报表初始化
	 */
	public void dealerCheckReportInit(){
		try {
			/****************/
			String endDate = AjaxSelectDao.getInstance().getSimpleCurrentServerTime();
			String startDate = endDate.substring(0,8);
			startDate = startDate + "01";
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			/****************/
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			String dutyType = logonUser.getDutyType();
			//如果当前登录用户为经销商
			if(Constant.DUTY_TYPE_DEALER.toString().equals(dutyType)){
				String dealerId = logonUser.getDealerId();
				TmDealerPO po = new TmDealerPO();
				po.setDealerId(Long.valueOf(dealerId));
				po = (TmDealerPO)dao.select(po).get(0);
			    act.setOutData("command", "1");
			}
			//如果当前登录用户为车厂
			else if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)){
				act.setOutData("command", "3");//标示符 1-一级经销商 2-二级经销商 3-车厂用户 4-大区用户
			}
			//如果当前登录用户为大区用户
			else{
				act.setOutData("command", "4");
			}
			act.setOutData("areaList", areaList);
			act.setForword(DEALER_CHECK_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
   /**
    * 查询(经销商端)
    */
	public void getDealerSalesInfo(){
		try {
			Map<String,Object> param = new HashMap<String, Object>();
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//结束时间
			String command = CommonUtils.checkNull(request.getParamValue("command"));//判断当前用户信息的标示符
			param.put("startDate", startDate);
			param.put("endDate", endDate);
			param.put("command", command);
			param.put("dealerId", logonUser.getDealerId());
//			param.put("poseId", logonUser.getPoseId().toString());
			List<Map<String, Object>> result = dao.getDealerCheckInfo(param);
			PageResult<Map<String, Object>> ps = new PageResult<Map<String,Object>>();
			ps.setRecords(result);
			ps.setPageSize(Constant.PAGE_SIZE_MAX);
			ps.setCurPage(1);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商开票");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	   /**
	    * 查询(车厂端)
	    */
		public void getOemDealerSalesInfo(){
			try {
				Map<String,Object> param = new HashMap<String, Object>();
				String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//开始时间
				String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//结束时间
				String command = CommonUtils.checkNull(request.getParamValue("command"));//判断当前用户信息的标示符
				String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
				if(!"".equals(command) && "3".equals(command)){
					String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));//区域选择
					param.put("orgId", orgId);
				}
				param.put("startDate", startDate);
				param.put("endDate", endDate);
				param.put("command", command);
				param.put("dealerId", dealerId);
				param.put("poseId", logonUser.getPoseId().toString());
				List<Map<String, Object>> result = dao.getOrgDealerCheckInfo(param,logonUser);
				PageResult<Map<String, Object>> ps = new PageResult<Map<String,Object>>();
				ps.setRecords(result);
				ps.setPageSize(Constant.PAGE_SIZE_MAX);
				ps.setCurPage(1);
				act.setOutData("ps", ps);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商开票");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}

		
		public void getSeriesList(){
			try {
				List<Map<String, Object>> list = dao.getSeriesList();
				act.setOutData("list", list);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "车系");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		/**
		 * 经销商开票导出
		 */
		public void toDealerSalesExcel(){
			try {
				Map<String,Object> param = new HashMap<String, Object>();
				String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//开始时间
				String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//结束时间
				String command = CommonUtils.checkNull(request.getParamValue("command"));//判断当前用户信息的标示符
				param.put("startDate", startDate);
				param.put("endDate", endDate);
				param.put("command", command);
				param.put("dealerId", logonUser.getDealerId());
				List<Map<String, Object>> result = dao.getDealerCheckInfo(param);
				List<Map<String, Object>> seriesList = dao.getSeriesList();
				String[] excelHead = new String[seriesList.size()+2];
				String[] columns = new String[seriesList.size()+2];
				excelHead[0] = "经销商代码";
				excelHead[1] = "经销商名称";
				columns[0] = "DEALER_CODE";
				columns[1] = "DEALER_NAME";
				for(int i = 0 ; i < seriesList.size() ; i++){
					Map<String, Object> map = seriesList.get(i);
					excelHead[i+2] = map.get("GROUP_NAME").toString();
					columns[i+2] = map.get("GROUP_NAME").toString();
				}
				ToExcel.toReportExcel(act.getResponse(), request, "经销商开票.xls", excelHead, columns, result);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商开票");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		
		/**
		 * 车厂端开票导出
		 */
		public void toOemDealerSalesExcel(){
			try {
				Map<String,Object> param = new HashMap<String, Object>();
				String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//开始时间
				String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//结束时间
				String command = CommonUtils.checkNull(request.getParamValue("command"));//判断当前用户信息的标示符
				String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
				if(!"".equals(command) && "3".equals(command)){
					String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));//区域选择
					param.put("orgId", orgId);
				}
				param.put("startDate", startDate);
				param.put("endDate", endDate);
				param.put("command", command);
				param.put("dealerId", dealerId);
				param.put("poseId", logonUser.getPoseId().toString());
				List<Map<String, Object>> result = dao.getOrgDealerCheckInfo(param,logonUser);
				List<Map<String, Object>> seriesList = dao.getSeriesList();
				String[] excelHead = new String[seriesList.size()+3];
				String[] columns = new String[seriesList.size()+3];
				excelHead[0] = "省份";
				excelHead[1] = "经销商代码";
				excelHead[2] = "经销商名称";
				columns[0] = "ORG_NAME";
				columns[1] = "DEALER_CODE";
				columns[2] = "DEALER_NAME";
				for(int i = 0 ; i < seriesList.size() ; i++){
					Map<String, Object> map = seriesList.get(i);
					excelHead[i+3] = map.get("GROUP_NAME").toString();
					columns[i+3] = map.get("GROUP_NAME").toString();
				}
				ToExcel.toReportExcel(act.getResponse(), request, "经销商开票.xls", excelHead, columns, result);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商开票");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
}

