/**********************************************************************
* <pre>
* FILE : ServiceActivityManageVehicle.java
* CLASS : ServiceActivityManageVehicle
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理---车辆信息确认.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-17| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageVehicle.java,v 1.1 2010/08/16 01:44:10 yuch Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsActivityVehicleBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageVehicleDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityVehiclePO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  服务活动管理---车辆信息确认
 * @author        :  PGM
 * CreateDate     :  2010-06-09
 * @version       :  0.1
 */
public class ServiceActivityManageVehicle {
	private Logger logger = Logger.getLogger(ServiceActivityManageVehicle.class);
	private ServiceActivityManageVehicleDao dao = ServiceActivityManageVehicleDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ServiceActivityVehicleInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageVehicle.jsp";//查询页面
	private final String ServiceActivityVehicleUpdateInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageVehicleModify.jsp";//车辆信息确认修改
	
	/**
	 * Function       :  服务活动管理车辆信息确认页面初始化
	 * @param         :  
	 * @return        :  serviceActivityManageVehicleInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	@SuppressWarnings("unchecked")
	public void serviceActivityManageVehicleInit(){
		try {
			RequestWrapper request =act.getRequest();
			List<TtAsActivityPO> list=dao.serviceActivityCode();
			request.setAttribute("list", list);
			act.setForword(ServiceActivityVehicleInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询服务活动管理车辆信息确认中符合条件的信息，其中包括：尚未发布状态
	 * @param         :  request-活动编号、维修状态、销售状态、车辆责任经销商不在执行经销商列表中
	 * @return        :  服务活动管理---车辆信息确认
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	public void serviceActivityManageVehicleQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String activityCodes = request.getParamValue("activityCodes");   //活动编号
			String activityCode = request.getParamValue("activityCode");    //活动名称
			String repairStatus = request.getParamValue("repairStatus");    //维修状态
			String saleStatus = request.getParamValue("saleStatus");        //销售状态
			String checkedDealer=request.getParamValue("checkedDealer");    //车辆责任经销商不在执行经销商列表中
			String status = request.getParamValue("status");                //发布状态
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);      //公司ID
			TtAsActivityVehicleBean VehicleBean = new TtAsActivityVehicleBean();
			if(null!=activityCodes&&!"".equals(activityCodes)){
				VehicleBean.setActivityCodes(activityCodes);
			}
			if(null!=activityCode&&!"".equals(activityCode)){
				VehicleBean.setActivityCode(activityCode);
			}
			if(null!=repairStatus&&!"".equals(repairStatus)){
				VehicleBean.setRepairStatus(repairStatus);
			}
			if(null!=saleStatus&&!"".equals(saleStatus)){
				VehicleBean.setSaleStatus(saleStatus);
			}
			if(null!=status&&!"".equals(status)){
				VehicleBean.setStatus(status);
			}
			VehicleBean.setCompanyId(String.valueOf(companyId));
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getAllServiceActivityManageVehicleInfo(VehicleBean,checkedDealer,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ServiceActivityVehicleInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---车辆信息确认");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  服务活动管理车辆信息确认页面修改初始化
	 * @param         :  request---活动ID
	 * @return        :  serviceActivityManageModifyInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	public void serviceActivityManageModifyInit(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId = request.getParamValue("activityId");     //活动ID
			String dealerCode = request.getParamValue("dealerCode");     //经销商代码
			String vin = request.getParamValue("vin");     //活动ID
			request.setAttribute("activityId", activityId);
			request.setAttribute("dealerCode", dealerCode);
			TtAsActivityVehiclePO vehiclePO =dao.serviceActivityManageVehicleSelectStatus(activityId,vin);//功能：调用查询方法，查询销售、维修状态，作用：回显
			request.setAttribute("vehiclePO", vehiclePO);
			act.setForword(ServiceActivityVehicleUpdateInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---车辆信息确认");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  服务活动管理车辆信息确认修改
	 * @param         :  request---活动ID、经销商ID、维修状态、销售状态
	 * @return        :  serviceActivityManageModify
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	@SuppressWarnings("unchecked")
	public void serviceActivityManageModify(){
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");                      //ID
			//String activityId = request.getParamValue("activityId");     //活动ID
			String dealerCode = request.getParamValue("dealerCode");       //经销商代码
			String repairStatus = request.getParamValue("repairStatus"); //维修状态
			String saleStatus = request.getParamValue("saleStatus");     //销售状态
			TtAsActivityVehiclePO VehiclePO=new TtAsActivityVehiclePO();
			//VehiclePO.setActivityId(Long.parseLong(activityId));
			if(null!=id&&!"".equals(id)){
				VehiclePO.setId(Long.parseLong(id));
			}
			
			TtAsActivityVehiclePO Vehicle=new TtAsActivityVehiclePO();
			if(null!=dealerCode&&!"".equals(dealerCode)){
				 Vehicle=dao.serviceActivityManageQueryDealerID(dealerCode);//根据经销商代码，查询经销商ID
			}
			if(null!=Vehicle.getDealerId()&&!"".equals(Vehicle.getDealerId())){
				 VehiclePO.setDealerId(Vehicle.getDealerId());
			}
			if(null!=repairStatus&&!"".equals(repairStatus)){
				VehiclePO.setRepairStatus(Integer.parseInt(repairStatus));
			}
			if(null!=saleStatus&&!"".equals(saleStatus)){
				VehiclePO.setSaleStatus(Integer.parseInt(saleStatus));
			}
			dao.serviceActivityManageModify(VehiclePO);//调用修改方法
			List<TtAsActivityPO> list=dao.serviceActivityCode();//活动编号下拉列表查询
			request.setAttribute("list", list);			
			act.setForword(ServiceActivityVehicleInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---车辆信息确认");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  删除车辆信息
	 * @param         :  request-工单号
	 * @return        :  服务活动管理
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-06-09
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageDelete(){ 
		RequestWrapper request = act.getRequest();
		String orderId=request.getParamValue("orderIds");//工单号
		try{
			if (orderId!=null&&!"".equals(orderId)) {
				String [] orderIdArray = orderId.split(","); //取得所有orderId放在数组中
				dao.serviceActivityManageDelete(orderIdArray);
			}
			act.setForword(ServiceActivityVehicleInitUrl);
			//act.setOutData("returnValue", 1);//returnValue 值：1，表示成功
		  }catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"删除奖惩细信息");
			logger.error(logonUser,e1);
		 	act.setException(e1);
		  }
	}
}