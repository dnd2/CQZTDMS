/**********************************************************************
 * <pre>
 * FILE : ServiceActivityManageTotalIssued.java
 * CLASS : ServiceActivityManageTotalIssued
 *
 * AUTHOR : PGM
 *
 * FUNCTION :服务活动管理---服务活动计划全量下发
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2010-06-09| PGM  | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
/**
 * $Id: ServiceActivityManageTotalIssued.java,v 1.1 2010/08/16 01:44:10 yuch Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;

import java.util.List;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageTotalIssuedDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsActivityDownlogPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infoservice.dms.chana.actions.OSC21;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

/**
 * Function : 服务活动管理---服务活动计划全量下发
 * 
 * @author : PGM CreateDate : 2010-06-10
 * @version : 0.1
 */
public class ServiceActivityManageTotalIssued {
	private Logger logger = Logger.getLogger(ServiceActivityManageTotalIssued.class);
	private ServiceActivityManageTotalIssuedDao dao=ServiceActivityManageTotalIssuedDao.getInstance();
	private ActionContext act = ActionContext.getContext();// 获取ActionContext
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private final String serviceActivityManageTotalIssuedInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageTotalIssued.jsp";// 查询页面

	/**
	 * Function : 服务活动管理---服务活动计划全量下发页面初始化
	 * 
	 * @param :
	 * @return : serviceActivityManageTotalIssuedInit
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	@SuppressWarnings("unchecked")
	public void serviceActivityManageTotalIssuedInit() {
		try {
			RequestWrapper request = act.getRequest();
			List<TtAsActivityPO> list = dao.serviceActivityCode();// 调用查询活动代码
			request.setAttribute("list", list);
			act.setForword(serviceActivityManageTotalIssuedInitUrl);
		} catch (Exception e) { BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动管理---服务活动计划下发页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function : 服务活动管理---服务活动计划全量下发页面
	 * @param :
	 * @return : TotalIssued
	 * @throws :
	 * Exception LastUpdate : 2010-07-22
	 */
	@SuppressWarnings("unchecked")
	public void TotalIssued() {
		try {
			RequestWrapper request = act.getRequest();
			String  activityId=request.getParamValue("activityId");//活动ID
			String  dealerIds=request.getParamValue("dealerIds");//经销商ID
			if (null!=dealerIds&&!"".equals(dealerIds)) {
				List<TtAsActivityDownlogPO> list=null;
				String [] dealers=dealerIds.split(",");
				String errors = "";
				for (int i = 0;i<dealers.length;i++) {
				 list=dao.TotalIssued(activityId, dealers[i]);//查询服务活动对应的经销商是否已经下发过
				 if(list.size()>0){
					 TmDealerPO dealerPO= dao.queryDealersCodeOrName(dealers[i]);//调用查询功能：查询经销商代码、名称
					 errors+=dealerPO.getDealerName()+" ";
				 }
				 list=null;
				}
				if(errors==null){
					//调用接口将activityId、dealerIds全部下发给接口！
					//commonDao.updateSendingFromComplete(tableName, colName, colValue)//将状态为2的更新为1
	     			//invoke interface 
	     			//commonDao.updateComplete("tt_as_activity", "id", activityId);//更新状态为2
					TtAsActivityPO ActivityPOOS=new TtAsActivityPO();
	     			ActivityPOOS.setActivityId(Long.parseLong(activityId));
	     			OSC21 os = new OSC21();
	     			os.execute(ActivityPOOS);
					act.setOutData("returnValue", 1);//returnValue 值：1，表示成功
				}else{//表示对应的服务活动和经销商已经下发过，不能重复下发
					//request.setAttribute("errors", errors);
					act.setOutData("returnValue", errors);//returnValue 值：2，表示失败
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动管理---服务活动计划下发页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}