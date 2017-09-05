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
import com.infodms.dms.dao.report.DealerStorageDetailQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 经销商库存明细
 * @author Administrator
 *
 */
public class DealerStorageDetailReport {
	private Logger logger = Logger.getLogger(DealerStorageDetailReport.class) ;
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest() ;
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private DealerStorageDetailQueryDao dao = DealerStorageDetailQueryDao.getInstance();
	private AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();
	private final String DEALER_STORAGE_DETAIL_INIT = "/jsp/report/sales/dealerStorgeDetailQuery.jsp";
	//private final String DEALER_STORAGE_DETAIL_INFO = "/jsp/report/sales/dealerStorageDetailInfo.jsp";
	/*
	 * 经销商库存查询初始化
	 */
	public void dealerStorageDetailQuery(){
		try {
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			List<Map<String, Object>> serisList = ajaxDao.getSerisList(logonUser.getPoseId().toString(), 2);
			String dutyType = logonUser.getDutyType();
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)){
				act.setOutData("command", "1");
			}else{
				act.setOutData("command", "2");
			}
			act.setOutData("serislist", serisList);
			act.setForword(DEALER_STORAGE_DETAIL_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
   /**
    * 查询
    */
	public void getDealerStorageDetailInfo(){
		try {
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String series = CommonUtils.checkNull(request.getParamValue("series"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String haveCon = CommonUtils.checkNull(request.getParamValue("haveCon"));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("vin", vin);
			map.put("areaId", areaId);
			map.put("dealerId", dealerId);
			map.put("series", series);
			map.put("orgId", orgId);
			map.put("haveCon", haveCon);
			map.put("logonUser", logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getDealerStorageDetailInfo(map,Constant.PAGE_SIZE_MIDDLE, curPage);
			act.setOutData("ps", ps);
			//act.setForword(DEALER_STORAGE_DETAIL_INFO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "生产销售快报信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 导出
	 */
	public void toExcel(){
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String series = CommonUtils.checkNull(request.getParamValue("series"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String haveCon = CommonUtils.checkNull(request.getParamValue("haveCon"));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("vin", vin);
			map.put("areaId", areaId);
			map.put("dealerId", dealerId);
			map.put("series", series);
			map.put("orgId", orgId);
			map.put("haveCon", haveCon);
			map.put("logonUser", logonUser);
			PageResult<Map<String, Object>> ps = dao.getDealerStorageDetailInfo(map,Constant.PAGE_SIZE_MAX, curPage);
			List<Map<String, Object>> result = ps.getRecords();
			String[] excelHead = {"大区","车系","车型","配置","省份","分销商","底盘号","生产日期","一级商家","库存类别","库存时间","类别"};
			String[] columns = {"ORG_NAME","SERIES_NAME","MODEL_NAME","PACKAGE_NAME","REGION_NAME","DEALER_NAME","VIN","PRODUCT_DATE","P_DEALER_NAME","CODE_DESC","STORAGE_AGE","VEHICLE_KIND"};
			ToExcel.toReportExcel(act.getResponse(), request, "经销商库存明细.xls",excelHead,columns,result);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "导出");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}

