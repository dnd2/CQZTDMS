package com.infodms.dms.actions.customerRelationships.complaintConsult;

import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptDao;
import com.infodms.dms.dao.customerRelationships.ComplaintConsultDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
/**
 * 
 * @ClassName     : ComplaintConsult 
 * @Description   : 投诉咨询
 * @author        : wangming
 * CreateDate     : 2013-4-15
 */
public class ComplaintConsult {
	private static Logger logger = Logger.getLogger(ComplaintConsult.class);
	//投拆咨询查看页面
	private final String ComplaintConsultWatchUrl = "/jsp/customerRelationships/complaintConsult/complaintConsultWatch.jsp";
	
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	//投诉咨询记录详细
	public void complaintConsultWatch(){
		try{
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid")); 
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid")); 
			String openPage = CommonUtils.checkNull(request.getParamValue("openPage"));
			ComplaintConsultDao dao = ComplaintConsultDao.getInstance();
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			Map<String,Object> complaintConsult = dao.queryComplaintConsult(cpid,ctmid);
			act.setOutData("dealRecordList", complaintAcceptDao.queryDealRecord(Long.parseLong(cpid)));
			act.setOutData("returnRecordList", complaintAcceptDao.queryReturnRecord(Long.parseLong(cpid)));
			act.setOutData("verifyRecordList", complaintAcceptDao.queryVerifyRecord(Long.parseLong(cpid)));
			act.setOutData("complaintConsult", complaintConsult);
			act.setOutData("openPage", openPage);
			act.setForword(ComplaintConsultWatchUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉咨询记录");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
}
