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
 * 保养次数查询报表
 */
public class DealerKeepAmountReport {

	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
	private final String initUrl = "/jsp/report/jcafterservicereport/dealerkeepamountreport.jsp";
	
	public void dealerKeepAmountInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {			
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"保养次数统计查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryKeepAmountReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空			
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}						
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryKeepAmountReport(beginTime,endTime,dealerCode,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"保养次数统计查询报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void getKeepAmountReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {			
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			String[] head=new String[8];
			head[0]="经销商代码";
			head[1]="经销商名称";
			head[2]="首保次数";
			head[3]="例保次数";
			head[4]="首保结算次数";
			head[5]="例保结算次数";
			head[6]="保养次数合计";

			List<Map<String, Object>> list= dao.getKeepAmountReportExcelList(beginTime,endTime,dealerCode);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[8];
						detail[0] = String.valueOf(map.get("DEALER_CODE"));
						detail[1] = String.valueOf(map.get("DEALER_NAME"));
						detail[2] = String.valueOf(map.get("SOUBAO"));
						detail[3] = String.valueOf(map.get("LIBAO"));
						detail[4] = String.valueOf(map.get("SOUBAOJIESUAN"));
						detail[5] = String.valueOf(map.get("LIBAOJIESUAN"));
						detail[6] = String.valueOf(map.get("QUANBU"));
						list1.add(detail);
			    	}
			      }
				com.infodms.dms.actions.claim.basicData.ToExcel.toNewExcel(ActionContext.getContext().getResponse(), request, head, list1,"保养次数查询报表.xls");
		    }
		    act.setForword(initUrl);	
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"保养次数统计报表");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	}
}
