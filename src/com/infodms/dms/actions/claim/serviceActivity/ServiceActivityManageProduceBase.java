/**********************************************************************
* <pre>
* FILE : ServiceActivityManageProduceBase.java
* CLASS : ServiceActivityManageProduceBase
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理--生产基地列表
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-07-09| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageProduceBase.java,v 1.1 2010/08/16 01:44:11 yuch Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageProduceBaseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityYieldlyPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

/**
 * Function       :  服务活动管理--生产基地
 * @author        :  PGM
 * CreateDate     :  2010-07-09
 * @version       :  0.1
 */
public class ServiceActivityManageProduceBase {
	private Logger logger = Logger.getLogger(ServiceActivityManageProduceBase.class);
	private ServiceActivityManageProduceBaseDao dao = ServiceActivityManageProduceBaseDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ServiceActivityProduceBaseInitUrl = "/jsp/claim/serviceActivity/serviceActivityProduceBase.jsp";//查询页面
	private final String ServiceActivityProduceBaseSuccessUrl = "/jsp/claim/serviceActivity/serviceActivityProduceBaseSuccess.jsp";//查询页面
	
	/**
	 * Function       :  根据条件查询服务活动管理中符合条件的信息，其中包括：生产基地列表
	 * @param         :  request---活动ID
	 * @return        :  服务活动管理--生产基地列表
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-09
	 */
	public void serviceActivityManageProduceBaseQuery(){ 
		RequestWrapper request = act.getRequest();
		String activityId=request.getParamValue("activityId");//活动ID
		String type=request.getParamValue("type");//活动ID
		act.setOutData("type",type);
		try {
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			List<TmBusinessAreaPO> lista = dao.select(areaPO);
			List<TtAsActivityBean> CharactorList=dao.getAllServiceActivityManageChangeCodeInfo(activityId);//查询 生产基地列表
			
			StringBuffer permissions = new StringBuffer();//全选回显
			Iterator<TtAsActivityBean> it= CharactorList.iterator();
			while(it.hasNext()){
				TtAsActivityBean mb=(TtAsActivityBean) it.next();
				if(it.hasNext()){
					permissions.append(mb.getCarYieldly()+",");
				}else{
					permissions.append(mb.getCarYieldly());
				}
			}
	        request.setAttribute("Charactor", permissions.toString());
			request.setAttribute("CharactorPoList", lista);
			request.setAttribute("activityId", activityId);
			act.setForword(ServiceActivityProduceBaseInitUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理--生产基地列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		 }
		}
	
	public void serviceActivityManageProduceBasejude()
	{
		RequestWrapper request = act.getRequest();
		String activityId=request.getParamValue("activityId");//活动ID
		try {
			 TtAsActivityYieldlyPO yieldlyPO = new TtAsActivityYieldlyPO();
			 yieldlyPO.setActivityId(Long.parseLong(activityId));
			 if(dao.select(yieldlyPO).size() == 0)
			 {
				 act.setOutData("ret","false");
			 }else
			 {
				 act.setOutData("ret","true");
			 }
			
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理--生产基地列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		 }
	}
	/**
	 * Function       :  增加服务活动管理---生产基地信息
	 * @param         :  request---活动ID、车辆性质  
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-09
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageProduceBaseOption(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId =request.getParamValue("activityId");         //活动ID
			String carYieldly =request.getParamValue("carYieldly");        //生产基地
			if (carYieldly!=null&&!"".equals(carYieldly)) {
				String [] carYieldlyArray = carYieldly.split(",");      
				TtAsActivityYieldlyPO CarYieldlyPO=new TtAsActivityYieldlyPO();
				CarYieldlyPO.setActivityId(Long.parseLong(activityId));
				CarYieldlyPO.setCreateBy(logonUser.getUserId());
				CarYieldlyPO.setCreateDate(new Date());
				CarYieldlyPO.setUpdateBy(logonUser.getUserId());
				CarYieldlyPO.setUpdateDate(new Date());
				dao.serviceActivityManageCarYieldlyOption(carYieldlyArray,CarYieldlyPO);
				act.setRedirect(ServiceActivityProduceBaseSuccessUrl);
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理生产基地列表信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}