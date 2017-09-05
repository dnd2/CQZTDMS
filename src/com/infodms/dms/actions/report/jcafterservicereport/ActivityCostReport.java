package com.infodms.dms.actions.report.jcafterservicereport;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.StandardVipApplyManagerBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.report.jcafterservicereport.SpecialCostReportDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;
/**
 * 服务活动费用查询报表
 */
public class ActivityCostReport {

	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	SpecialCostReportDao dao = new SpecialCostReportDao();
	private final String initUrl = "/jsp/report/jcafterservicereport/activitycostreport.jsp";
	
	public void activityCostReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaList = dao.getAreaList();//区域
			act.setOutData("areaList", areaList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动费用查询报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryActivityCostReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String areaName = request.getParamValue("areaName");//大区
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			String campaignCode = request.getParamValue("campaignCode");
			String campaignName = request.getParamValue("campaignName");
			
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setAreaName(areaName);
			bean.setDealerCode(dealerCode);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setCampaignCode(campaignCode);
			bean.setCampaignName(campaignName);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryActivityCostReport(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动费用查询报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void activityCostReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String areaName = request.getParamValue("areaName");//大区
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			String campaignCode = request.getParamValue("campaignCode");
			String campaignName = request.getParamValue("campaignName");
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			String[] head=new String[7];
			head[0]="序号";
			head[1]="服务中心编码";
			head[2]="服务中心名称";
			head[3]="大区";
			head[4]="服务活动编码";
			head[5]="服务活动名称";
			head[6]="活动费用(元)";
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setAreaName(areaName);
			bean.setDealerCode(dealerCode);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setCampaignCode(campaignCode);
			bean.setCampaignName(campaignName);
			List<Map<String, Object>> list= dao.getActivityCostReportExcelList(bean);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[2];
						detail[0] = String.valueOf(map.get("NUM"));
						detail[1] = String.valueOf(map.get("DEALER_CODE"));
						detail[2] = String.valueOf(map.get("DEALER_SHORTNAME"));
						detail[3] = String.valueOf(map.get("ORG_NAME"));
						detail[4] = String.valueOf(map.get("CAMPAIGN_CODE"));
						detail[5] = String.valueOf(map.get("ACTIVITY_NAME"));
						detail[6] = String.valueOf(map.get("CAMPAIGN_FEE"));
						list1.add(detail);
			    	}
			      }
				com.infodms.dms.actions.claim.basicData.ToExcel.toExcel(ActionContext.getContext().getResponse(), request, head, list1);
		    }
		    act.setForword(initUrl);	
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车维修车辆报表(不包含服务商)");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	}
}
