/**********************************************************************
* <pre>
* FILE : ServiceActivityManageVehicleStatus.java
* CLASS : ServiceActivityManageVehicleStatus
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理---服务活动管理---服务车辆信息及状态查询
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
 * $Id: ServiceActivityManageVehicleStatus.java,v 1.1 2010/08/16 01:44:11 yuch Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.bean.TtAsActivityVehicleBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageVehicleStatusDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityVehiclePO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  服务活动管理---服务车辆信息及状态查询
 * @author        :  PGM
 * CreateDate     :  2010-06-09
 * @version       :  0.1
 */
public class ServiceActivityManageVehicleStatus {
	private Logger logger = Logger.getLogger(ServiceActivityManageVehicleStatus.class);
	private ServiceActivityManageVehicleStatusDao dao = ServiceActivityManageVehicleStatusDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ServiceActivityehicleStatusInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageVehicleStatus.jsp";//查询页面
	private final String ServiceActivityehicleStatusInfoUrl = "/jsp/claim/serviceActivity/serviceActivityManageVehicleStatusDetail.jsp";//服务活动维护-明细
	private final String ServiceActivityehicleVinInitUrl = "/jsp/claim/serviceActivity/vehicleVinInfo.jsp";//Vin查询页面
	/**
	 * Function       :  服务活动管理---服务活动管理---服务车辆信息及状态查询页面初始化
	 * @param         :  
	 * @return        :  serviceActivityManageVehicleStatusInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	@SuppressWarnings("unchecked")
	public void serviceActivityManageVehicleStatusInit(){
		try {
			RequestWrapper request =act.getRequest();
			List<TtAsActivityPO> list=dao.serviceActivityCode();
			request.setAttribute("list", list);
			act.setForword(ServiceActivityehicleStatusInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---经销商管理页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询服务活动管理---服务车辆信息及状态查询中符合条件的信息
	 * @param         :  request-活动编号、活动责任经销商、客户名称
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	public void serviceActivityManageVehicleStatusQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String activityCode = request.getParamValue("activityCode");    //活动名称
			String dealerCode = request.getParamValue("dealerCode");       //活动责任经销商代码
			String dealerName = request.getParamValue("dealerName");       //活动责任经销商名称
			String customerName = request.getParamValue("customerName");   //客户名称
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);      //公司ID
			TtAsActivityBean ActivityBean = new TtAsActivityBean();
			ActivityBean.setActivityCode(activityCode);
			ActivityBean.setDealerName(dealerName);
			TmDealerPO dealerPO =new TmDealerPO();
			if(null!=dealerCode&&!"".equals(dealerCode)){
				 dealerPO = dao.QueryDealerID(dealerCode);		   //调用查询方法，查询dealer_id
				 if(null!=dealerPO.getDealerId()&&!"".equals(dealerPO.getDealerId())){
						ActivityBean.setDealerId(String.valueOf(dealerPO.getDealerId()));
					}
			}
			ActivityBean.setCustomerName(customerName);
			ActivityBean.setCompanyId(String.valueOf(companyId));
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityManageVehicleStatusQuery(ActivityBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ServiceActivityehicleStatusInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---经销商管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据ID查询服务活动管理---VIN车辆对应的明细信息
	 * @param         :  request-活动ID
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	public void serviceActivityManageVehicleStatusInfo(){
		RequestWrapper request = act.getRequest();
		try {
		String id = request.getParamValue("id");    //活动ID
		TtAsActivityVehicleBean VehicleBean= dao.serviceActivityManageVehicleStatusInfo(id);
		request.setAttribute("VehicleBean", VehicleBean);
		act.setForword(ServiceActivityehicleStatusInfoUrl);//跳转到明细页面
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---经销商管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  修改车辆状态为已经完成
	 * @param         :  request-活动车辆ID
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
    @SuppressWarnings("static-access")
	public void  serviceActivityManageVehicleCarStatus(){
    	RequestWrapper request = act.getRequest();
    	try {
    		String id = request.getParamValue("id");    //活动车辆ID
    		TtAsActivityVehiclePO VehiclePo =new TtAsActivityVehiclePO();//条件
    		VehiclePo.setId(Long.parseLong(id));
    		TtAsActivityVehiclePO VehiclePoContent =new TtAsActivityVehiclePO();//内容
    		VehiclePoContent.setUpdateBy(logonUser.getUserId()); //修改人
			VehiclePoContent.setUpdateDate(new Date());          //修改时间
			VehiclePoContent.setCarStatus(Constant.SERVICEACTIVITY_CAR_STATUS_02);//已经完成
			dao.serviceActivityManageVehicleCarStatus(VehiclePo, VehiclePoContent);
			act.setOutData("returnValue", 1);
	    }catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---修改车辆状态");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
  }
    /**
	 * Function       :  根据VIN查询车辆维修历史明细信息
	 * @param         :  request-工单号
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	public void serviceActivityManageVehicleVin(){ 
		RequestWrapper request = act.getRequest();
		try {
			String vin=request.getParamValue("vin");//VIN
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityManageVehicleVin(vin,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ServiceActivityehicleVinInitUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩工单详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据VIN查询车辆维修历史明细信息初始化页面
	 * @param         :  request-活动ID
	 * @return        :  服务活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public void getActivityVehicleVinInfoInit(){ 
		RequestWrapper request = act.getRequest();
		String vin=request.getParamValue("vin");//活动ID
		request.setAttribute("vin", vin);
		act.setForword(ServiceActivityehicleVinInitUrl);
	}
}