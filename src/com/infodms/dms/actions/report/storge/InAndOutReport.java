package com.infodms.dms.actions.report.storge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.report.storge.InAndOutDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**入库出库报表
 * @author RANJIAN
 * @deprecated 2013-9-16
 */
public class InAndOutReport {
	private Logger logger = Logger.getLogger(InAndOutReport.class) ;
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest() ;
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private InAndOutDao dao = InAndOutDao.getInstance();
	
	private final String IN_QUERY_URL = "/jsp/report/storge/inVehicleQuery.jsp";
	private final String  IN_INFO = "/jsp/report/storge/inVehicleInfo.jsp";
	private final String  OUT_INFO = "/jsp/report/storge/outVehicleInfo.jsp";
	/*
	 *物流运输报表查询初始化
	 */
	public void inAndOutReportInit(){
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			act.setForword(IN_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "入库出库报表初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void inAndOutReportInfo(){
		try {
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); //产地
			String common = CommonUtils.checkNull(request.getParamValue("common")); // 处理方式
			String inoutType = CommonUtils.checkNull(request.getParamValue("INOUT_TYPE")); // 查询方式
			String startdate = CommonUtils.checkNull(request.getParamValue("START_DATE")); // 开始时间
			String enddate = CommonUtils.checkNull(request.getParamValue("END_DATE")); // 结束时间
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("yieldly", yieldly);
			map.put("startdate", startdate);
			map.put("enddate", enddate);
			map.put("inoutType", inoutType);
			map.put("poseId", logonUser.getPoseId().toString());
			if(Constant.INOUT_TYPE_02.toString().equals(inoutType)){//出库
				List<Map<String, Object>> valueMapList = dao.getOutInfo(map);
				if(common.equals("1")){
					String[] headExport={"车辆型号","台数合计"};
					String[] columns={"ERP_MODEL","VEH_COUNT"};
					ToExcel.toReportExcel(act.getResponse(), request,"出库报表.xls", headExport,columns,valueMapList);
				}else{
					act.setOutData("valueMapList", valueMapList);
					act.setForword(OUT_INFO);
				}
			}else{//入库
				List<Map<String, Object>> valueMapList = dao.getInInfo(map);
				if(common.equals("1")){
					String[] headExport={"车辆型号","内部型号","台数合计"};
					String[] columns={"ERP_MODEL","ERP_PACKAGE","VEH_COUNT"};
					ToExcel.toReportExcel(act.getResponse(), request,"入库报表.xls", headExport,columns,valueMapList);
				}else{
					act.setOutData("valueMapList", valueMapList);
					act.setForword(IN_INFO);
				}
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "查看信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}

