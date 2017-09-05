package com.infodms.dms.actions.report.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.report.reportmng.DynamicReportMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
@SuppressWarnings("unchecked")
public class ActivityReport {

	private Logger logger = Logger.getLogger(DynamicReportMng.class) ;
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper req = act.getRequest();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER); ;
	ClaimReportDao dao  = ClaimReportDao.getInstance();
	
	//uRL
	private final String MAIN_URL = "/jsp/report/service/activityReportPer.jsp" ;//主查询页面
	private final String ACTIVITY_REPORT_SUBJECT = "/jsp/report/service/subjectSelect.jsp" ;//活动主题选择
	
	public void activityTotal(){
		try {
			act.setForword(MAIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("static-access")
	public void activityReportQuery(){
		try {
			String type = req.getParamValue("type");
			String avtivityCode = req.getParamValue("subjectId");
			
			Map<String, String> map1 =new HashMap();
			map1.put("avtivityCode", avtivityCode);
			
			String name = "客户关怀活动小项明细.xls";
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage"))
					: 1; // 处理当前页
			if("0".equalsIgnoreCase(type)){
				PageResult<Map<String,Object>> pr = dao.getActivityDetailList( map1,Constant.PAGE_SIZE,curPage);
				act.setOutData("ps", pr);
			}else {
				String[] head=new String[10];
				head[0]="服务商简称";
				head[1]="主题名称";
				head[2]="主题开始时间";
				head[3]="主题结束时间";
				head[4]="车主";
				head[5]="车主电话";
				head[6]="活动项目";
				head[7]="实际数量";
				head[8]="实际金额(元)";
				head[9]="备注";
				
				PageResult<Map<String,Object>> pr = dao.getActivityDetailList( map1,99999,curPage);
				List<Map<String, Object>> list2 = pr.getRecords();
				
				String setList = "0,1,2,3,4,5,6,9";
				 List list1=new ArrayList();
				    if(list2!=null&&list2.size()!=0){
						for(int i=0;i<list2.size();i++){
					    	Map map =(Map)list2.get(i);
							String[]detail=new String[10];
							
							detail[0]=(String) map.get("DEALER_SHORTNAME");
							detail[1]=(String) map.get("SUBJECT_NAME");
							detail[2]=(String)(map.get("SDATE"));
							detail[3]=(String) map.get("EDATE");
							detail[4]=(String) map.get("OWNER_NAME");
							detail[5]=(String) map.get("OWNER_PHONE");
							detail[6]=(String)(map.get("PRO_NAME"));
							detail[7]=String.valueOf(map.get("TOTAL"));
							detail[8]=String.valueOf( map.get("AMOUNT"));
							detail[9]=(String) map.get("REMARK");
							list1.add(detail);
					      }
						//dao.toExceVender(ActionContext.getContext().getResponse(), req, head, list1,name);
					    }
				    dao.toExceVender(ActionContext.getContext().getResponse(), req, head, list1,name,"客户关怀活动小项明细",setList);
				    act.setForword(MAIN_URL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询条件,活动主题选择
	 */
	public void subjectOpen(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(ACTIVITY_REPORT_SUBJECT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
