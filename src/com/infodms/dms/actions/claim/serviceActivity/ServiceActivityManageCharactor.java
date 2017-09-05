/**********************************************************************
* <pre>
* FILE : ServiceActivityManageCharactor.java
* CLASS : ServiceActivityManageCharactor
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理--车辆性质列表
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-03| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageCharactor.java,v 1.1 2010/08/16 01:44:11 yuch Exp $
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
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageCharactorDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsActivityCharactorPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

/**
 * Function       :  服务活动管理--车辆性质列表
 * @author        :  PGM
 * CreateDate     :  2010-06-02
 * @version       :  0.1
 */
public class ServiceActivityManageCharactor {
	private Logger logger = Logger.getLogger(ServiceActivityManageCharactor.class);
	private ServiceActivityManageCharactorDao dao = ServiceActivityManageCharactorDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ServiceActivityCharactorInitUrl = "/jsp/claim/serviceActivity/serviceActivityCharactor.jsp";//查询页面
	private final String ServiceActivityCharactorSuccessUrl = "/jsp/claim/serviceActivity/serviceActivityCharactorSuccess.jsp";//查询页面
	private final String ServiceActivityCharactorFailureUrl =  "/jsp/claim/serviceActivity/serviceActivityCharactorFailure.jsp";//查询页面
	
	/**
	 * Function       :  根据条件查询服务活动管理中符合条件的信息，其中包括：车辆性质列表
	 * @param         :  request---活动ID
	 * @return        :  服务活动管理--车辆性质列表
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-02
	 */
	public void serviceActivityManageCharactorQuery(){ 
		RequestWrapper request = act.getRequest();
		String activityId=request.getParamValue("activityId");//活动ID
		try {
			List<TtAsActivityBean> CharactorPoList=dao.getAllServiceActivityManageCharactorInfo(activityId);//车辆性质列表
			List<TtAsActivityBean> CharactorList=dao.getAllServiceActivityManageChangeCodeInfo(activityId);//查询 车辆性质列表
			
			StringBuffer permissions = new StringBuffer();//全选回显
			Iterator<TtAsActivityBean> it= CharactorList.iterator();
			while(it.hasNext()){
				TtAsActivityBean mb=(TtAsActivityBean) it.next();
				if(it.hasNext()){
					permissions.append(mb.getCarCharactor()+",");
				}else{
					permissions.append(mb.getCarCharactor());
				}
			}
	        request.setAttribute("Charactor", permissions.toString());
			request.setAttribute("CharactorPoList", CharactorPoList);
			request.setAttribute("activityId", activityId);
			act.setForword(ServiceActivityCharactorInitUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理--车辆性质列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		 }
		}
	/**
	 * Function       :  增加服务活动管理信息
	 * @param         :  request---活动ID、车辆性质  
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-02
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageCharactorOption(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId =request.getParamValue("activityId");         //活动ID
			String carCharactor =request.getParamValue("carCharactor");     //车辆性质  
			TtAsActivityBean beforeVehicle=dao.getChangeBeforeVehicle(activityId);
			if("11321001".equals(beforeVehicle.getVehicleArea())){//服务活动管理--服务活动车辆范围为:售前车
				act.setRedirect(ServiceActivityCharactorFailureUrl);
			}
			else if (carCharactor!=null&&!"".equals(carCharactor)) {
				 String[] cct= carCharactor.split(",");
				if(cct[0].equals("sum"))
				{
					TtAsActivityCharactorPO CharactorPO=new TtAsActivityCharactorPO();
					CharactorPO.setActivityId(Long.parseLong(activityId));
					dao.delete(CharactorPO);
					
				}else
				{
					String [] carCharactorArray = carCharactor.split(",");      
					TtAsActivityCharactorPO CharactorPO=new TtAsActivityCharactorPO();
					CharactorPO.setActivityId(Long.parseLong(activityId));
					CharactorPO.setCreateBy(logonUser.getUserId());
					CharactorPO.setCreateDate(new Date());
					CharactorPO.setUpdateBy(logonUser.getUserId());
					CharactorPO.setUpdateDate(new Date());
					dao.serviceActivityManageCharactorOption(carCharactorArray,CharactorPO);
				}
				
				act.setRedirect(ServiceActivityCharactorSuccessUrl);
			}
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理车辆性质列表信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}