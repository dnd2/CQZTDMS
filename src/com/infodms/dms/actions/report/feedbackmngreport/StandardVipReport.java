/**
 * 
 */
package com.infodms.dms.actions.report.feedbackmngreport;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.feedbackMng.StandardVipReportDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;


/**
 * @Title: StandardVipReport.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-10-9
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class StandardVipReport {
	public Logger logger = Logger.getLogger(StandardVipReport.class);
	private StandardVipReportDao dao = new StandardVipReportDao();
	
	private String initUrl = "/jsp/report/feedbackmngreport/querystandardvipreport.jsp";
	
	public void standardVipReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证处理情况");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
