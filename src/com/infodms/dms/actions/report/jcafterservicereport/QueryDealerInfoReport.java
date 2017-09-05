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
 * 轿车公司服务商信息表
 */
public class QueryDealerInfoReport {
	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
	private final String initUrl = "/jsp/report/jcafterservicereport/queryDealerInfoReport.jsp";
	
	public void queryDealerInfoReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaList = dao.getAreaList();//区域
			
			act.setOutData("areaList", areaList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车公司服务商信息表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryDealerInfoReportReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String areaName = request.getParamValue("areaName");//大区
			String province = request.getParamValue("province");
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码
			String dealerName = request.getParamValue("dealerName");
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setAreaName(areaName);
			bean.setDealerCode(dealerCode);
			bean.setDealerName(dealerName);
			bean.setProvince(province);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryDealerInfoReportReport(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车保养VIN报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryDealerInfoReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String areaName = request.getParamValue("areaName");//大区
			String province = request.getParamValue("province");
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码
			String dealerName = request.getParamValue("dealerName");
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setAreaName(areaName);
			bean.setDealerCode(dealerCode);
			bean.setDealerName(dealerName);
			bean.setProvince(province);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			String[] head=new String[8];
			head[0]="序号";
			head[1]="服务中心名称";
			head[2]="服务中心代码";
			head[3]="账号名称";
			head[4]="所属大区";
			head[5]="省份";
			head[6]="系统开通时间";
			head[7]="状态";
			List<Map<String, Object>> list= dao.queryDealerInfoReportExcel(bean);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[8];
						detail[0] = String.valueOf(map.get("NUM"));
						detail[1] = String.valueOf(map.get("DEALER_NAME"));
						detail[2] = String.valueOf(map.get("DEALER_CODE"));
						detail[3] = String.valueOf(map.get("ACNT"));
						detail[4] = String.valueOf(map.get("ORG_NAME"));
						detail[5] = String.valueOf(map.get("REGION_NAME"));
						detail[6] = String.valueOf(map.get("CREATE_DATE")==null?"":map.get("CREATE_DATE"));
						detail[7] = String.valueOf(map.get("CODE_DESC")==null?"":map.get("CODE_DESC"));
						list1.add(detail);
			    	}
			      }
				com.infodms.dms.actions.claim.basicData.ToExcel.toExcel(ActionContext.getContext().getResponse(), request, head, list1);
		    }
		    act.setForword(initUrl);	
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车保养VIN报表");
				logger.error(logonUser,e1);
				act.setException(e1);
			}	
	}
}
