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
 *服务商走保维修量统计报表
 */
public class WalkKeepAmountReport {

	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
	private final String initUrl = "/jsp/report/jcafterservicereport/walkkeepamountreport.jsp";
	
	public void walkKeepAmountInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaList = dao.getAreaList();//区域
			act.setOutData("areaList", areaList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务商走保维修量统计报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryWalkKeepAmountReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空			
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码			
			String areaId = request.getParamValue("areaId");//大区	
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			String[] yieldlys = request.getParamValues("yieldly");
			StringBuffer sb = new StringBuffer();
			if( null != yieldlys ){			
				for(int i=0;i<yieldlys.length;i++){
					  sb.append(yieldlys[i]+",");
					}
			}
			String yieldly  = Utility.getCODES(sb.toString());
			String province = request.getParamValue("province");
			
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}						
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryWalkKeepAmountReport(beginTime,endTime,dealerCode,yieldly,province,areaId,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务商走保维修量统计报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void getWalkKeepAmountReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {			
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码
			String areaId = request.getParamValue("areaId");//大区
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");			
			String[] yieldlys = request.getParamValues("yieldly");
			StringBuffer sb = new StringBuffer();
			if( null != yieldlys ){			
				for(int i=0;i<yieldlys.length;i++){
					  sb.append(yieldlys[i]+",");
					}
			}
			String yieldly  = Utility.getCODES(sb.toString());
			String province = request.getParamValue("province");
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			String[] head=new String[7];
			head[0]="事业部";
			head[1]="省份";
			head[2]="单位代码";
			head[3]="单位名称";
			head[4]="走保车辆数";
			head[5]="维修车辆数";
			head[6]="维修次数";
			
			List<Map<String, Object>> list= dao.queryWalkKeepAmountReportExcel(beginTime,endTime,dealerCode,yieldly,province,areaId);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[7];
						detail[0] = String.valueOf(map.get("SYB"));
						detail[1] = String.valueOf(map.get("SF"));
						detail[2] = String.valueOf(map.get("DCODE"));
						detail[3] = String.valueOf(map.get("DNAME"));
						detail[4] = String.valueOf(map.get("ZBS"));
						detail[5] = String.valueOf(map.get("WXS"));
						detail[6] = String.valueOf(map.get("WXCS"));
						list1.add(detail);
			    	}
			      }
				com.infodms.dms.actions.claim.basicData.ToExcel.toNewExcel(ActionContext.getContext().getResponse(), request, head, list1,"服务商走保维修量统计报表.xls");
		    }
		    act.setForword(initUrl);	
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务商走保维修量统计报表");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	}
}
