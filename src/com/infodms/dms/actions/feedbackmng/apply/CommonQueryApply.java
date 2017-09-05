/**********************************************************************
* <pre>
* FILE : CommonQueryApply.java
* CLASS : CommonQueryApply
*
* AUTHOR : PGM
*
* FUNCTION : 奖惩审批表维护（申请单位）.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-21| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: CommonQueryApply.java,v 1.1 2010/08/16 01:44:07 yuch Exp $
 */
package com.infodms.dms.actions.feedbackmng.apply;


import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CommonQueryApplyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.feedbackMng.CommonQueryApplyDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  查询申请单位代码、名称
 * @author        :  PGM
 * CreateDate     :  2010-05-21
 * @version       :  0.1
 */
public class CommonQueryApply {
	private Logger logger = Logger.getLogger(CommonQueryApply.class);
	private CommonQueryApplyDao dao = CommonQueryApplyDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String MantainInit = "/jsp/feedbackMng/query/applySearch.jsp";//申请单位弹出选择页面
	
	/**
	 * Function       :  查询申请单位名称弹出框
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-21
	 */
	public void queryApply() throws Exception{
		try {
			act.setForword(MantainInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"申请单位信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
	/**
	 * Function       :  查询申请单位名称弹出框
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-21
	 */
	public void queryApplyMantain() throws Exception{
		try {
			RequestWrapper request = act.getRequest();
			String userId = request.getParamValue("userId");
			String name = request.getParamValue("name");
			String phone = request.getParamValue("phone");
			CommonQueryApplyBean  ApplyBean=new CommonQueryApplyBean();
			ApplyBean.setUserId(userId);
			ApplyBean.setName(name);
			ApplyBean.setPhone(phone);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> list = dao.queryApplyMantain(ApplyBean,curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"查询经销商信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
}