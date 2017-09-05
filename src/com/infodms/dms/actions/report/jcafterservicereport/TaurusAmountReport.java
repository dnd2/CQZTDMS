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
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;
/**
 *金牛星修量统计报表
 */
public class TaurusAmountReport {

	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
	private final String initUrl = "/jsp/report/jcafterservicereport/taurusamountreport.jsp";
	
	public void taurusAmountReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId()); //取得该用户拥有的产地权限
			act.setOutData("yieldly", yieldly);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"金牛星修量统计报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryTaurusAmountReportReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
				
			String beginTime = request.getParamValue("beginTime"); //时间
			String endTime = request.getParamValue("endTime");

			String partYes = request.getParamValue("partYes"); //零件授权  是
			String partNo = request.getParamValue("partNo");//零件授权 否
			String NoNeed = request.getParamValue("NoNeed"); //零件授权 不需要
			
			String yieldly = Utility.getCODES(request.getParamValue("yieldly"));//生产基地
			
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}						
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryTaurusAmountReport(beginTime,endTime,partYes,partNo,NoNeed,yieldly,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务商走保维修量统计报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void getTaurusAmountReportReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {			
			String beginTime = request.getParamValue("beginTime"); //时间
			String endTime = request.getParamValue("endTime");

			String partYes = request.getParamValue("partYes"); //零件授权  是
			String partNo = request.getParamValue("partNo");//零件授权 否
			String NoNeed = request.getParamValue("NoNeed"); //零件授权 不需要
			
			String yieldly = Utility.getCODES(request.getParamValue("yieldly"));//生产基地
			
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}				
			
			String[] head=new String[7];
			head[0]="索赔类型";
			head[1]="维修车辆数";
			
			
			List<Map<String, Object>> list= dao.queryTaurusAmountReportExcel(beginTime,endTime,partYes,partNo,NoNeed,yieldly);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[7];
						detail[0] = String.valueOf(map.get("CLAIM_TYPE"));
						detail[1] = String.valueOf(map.get("AMOUNT"));						
						list1.add(detail);
			    	}
			      }
				com.infodms.dms.actions.claim.basicData.ToExcel.toNewExcel(ActionContext.getContext().getResponse(), request, head, list1,"金牛星修量统计报表.xls");
		    }
		    String yieldlys = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId()); //取得该用户拥有的产地权限
			act.setOutData("yieldly", yieldlys);
		    act.setForword(initUrl);	
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"金牛星修量统计报表");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	}
}
