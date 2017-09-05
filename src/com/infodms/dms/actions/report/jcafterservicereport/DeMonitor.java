package com.infodms.dms.actions.report.jcafterservicereport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.report.jcafterservicereport.DeMonitorDataDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 *DE接口表情况
 */
public class DeMonitor {

	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	DeMonitorDataDao dao =  DeMonitorDataDao.getInstance();
	private final String initUrl = "/jsp/report/jcafterservicereport/demonitor.jsp";
	
	public void DeMonitorInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
 			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"DE接口表情况");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryDeMonitor(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空				
			String beginTime = request.getParamValue("beginTime"); //创建时间
			String endTime = request.getParamValue("endTime");
			
			String msg_id = request.getParamValue("msg_id");//消息号
			String biz_type = request.getParamValue("biz_type");//处理接口
			String process = request.getParamValue("process");//是否执行
			String msg_from = request.getParamValue("msg_from");//起始点
			String msg_to = request.getParamValue("msg_to");//目标点
									 
			//创建时间开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryDemonitorStatus(beginTime, endTime,process, msg_id, biz_type,msg_from ,msg_to,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"DE接口数据情况");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void forstData(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {						
			String beginTime = request.getParamValue("beginTime"); //创建时间
			String endTime = request.getParamValue("endTime");		
			String msg_id = request.getParamValue("msg_id");//消息号
			String biz_type = request.getParamValue("biz_type");//处理接口
			String process = request.getParamValue("process");//是否执行
			String msg_from = request.getParamValue("msg_from");//起始点
			String msg_to = request.getParamValue("msg_to");//目标点
									 
			//创建时间开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			StringBuffer sql= new StringBuffer();		 
 			sql.append(" update de_msg_info de set de.process = 1,de.try_times = 9 where 1=1 ");
 			
			if(Utility.testString(process)){
				sql.append("  and de.process="+process+"  \n");	
			}
			if(Utility.testString(msg_id)){
				sql.append("  and de.msg_id="+msg_id+"  \n");	
			}
				if(Utility.testString(biz_type)){
				sql.append("  and de.biz_type='"+biz_type+"'  \n");	
			}
				if(Utility.testString(msg_from)){
				sql.append("   and de.msg_from='"+msg_from+"'  \n");	
			} 
				if(Utility.testString(beginTime)){
				sql.append("     and de.create_date>=to_date('"+beginTime+"','YYYY-MM-DD HH24:MI:SS')  \n");	
			}  
			if(Utility.testString(endTime)){
				sql.append("     and de.create_date<=to_date('"+endTime+"','YYYY-MM-DD HH24:MI:SS')  \n");	
			}  
				if(Utility.testString(msg_to)){
				sql.append("   and de.msg_to='"+msg_to+"'  \n");	
			}
			int i =  dao.update(sql.toString(), null);
			if(1==i) {
			  act.setOutData("flag", true);
			}else {
			  act.setOutData("flag", false);
			} 
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证寄送情况");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
