package com.infodms.dms.util;

import java.util.Date;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderreport.UrgentOrderReport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
//import com.infodms.dms.util.dao.CustomerUtilDao;
import com.infodms.dms.diff.CustomerUtilDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

/**
 * 公用类,操作日志
 * @author tanv
 * 2013-01-28
 * */
public class CustomerUtil {
	
	private Logger logger = Logger.getLogger(CustomerUtil.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static CustomerUtilDao dao = CustomerUtilDao.getInstance();
	/**
	 * 写入日志
	 * @author tanv
	 * 2013-01-28
	 * @param start_time 
	 * */
	public static String logInfo(String msgId,String bizType,String status,String logcomment, Date start_time){
		String flag = dao.insertLog(msgId,bizType,status,logcomment,start_time);
		return "success"; 
	}
	/**
	 * 格式化异常信息
	 * @author tanv
	 * 2013-01-28
	 * */
	public static String eToString(Exception e) {
		StringBuilder result = new StringBuilder();
		if(e.getCause()==null){
			result.append(e.toString()+"\n");
		}else{
			result.append(e.getCause().toString()+"\n");
			result.append(e.getMessage()+"\n");
			if(e.getStackTrace()!=null && e.getStackTrace().length>0){
				for(int i=0;i<e.getStackTrace().length;i++){
					result.append(e.getStackTrace()[i].toString()+"\n");
				}
			}
		}
		result.append("\n");
		return result.toString();
	}
}
