package com.infodms.dms.service;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TrUserPosePO;
import com.infodms.dms.po.TtVsDriverPO;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public interface DriverAuditService {
     /**
      * 查询司机审核列表
      * @param request
     * @param loginUser 
      * @return
      */
	public PageResult<Map<String, Object>> getdriverAuditList(RequestWrapper request,AclUserBean loginUser, int pageSize,int currPage);
    /**
     * 司机审核
     * @param request
     * @param loginUser
     * @return
     */
	public String driverAudit(RequestWrapper request, AclUserBean loginUser) throws Exception;
	/**
	 * 司机批量审核
	 * @param request
	 * @param loginUser
	 * @return
	 * @throws Exception
	 */
	public String driverAuditAll(RequestWrapper request, AclUserBean loginUser) throws Exception;
	/**
	 * 司机审核页面数据查询
	 * @param request
	 * @param loginUser
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getdriverAuditByDriverId(RequestWrapper request, AclUserBean loginUser) throws Exception;
	/**
	 * 司机注册
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String driverRegister(RequestWrapper request) throws Exception;
	/**
	 * 获取承运商
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getTtSalesLogi(RequestWrapper request) throws Exception;
	/**
	 * 根据司机id获取司机注册信息
	 * @param driver_id
	 * @return
	 * @throws Exception
	 */
	public TtVsDriverPO getTtVsDriver(Long driver_id) throws Exception;
	
	/**
	 * 根据司机获取用户信息
	 * @param driver_id
	 * @return
	 * @throws Exception
	 */
	public TcUserPO getTcUserByDriverId(Long driver_id) throws Exception;
	/**
	 * 根据用户id获取用户职位关系
	 * @param user_id
	 * @return
	 * @throws Exception
	 */
	public TrUserPosePO getTrUserPosePoByUserId(Long user_id) throws Exception;
	/**
	 * 根据承运商id获取职位
	 * @param carrierId
	 * @return
	 * @throws Exception
	 */
	public TcPosePO getTcPosePOByLogiId(Long carrierId) throws Exception;
	/**
	 * 司机用户名校验
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String driverCalibration(RequestWrapper request) throws Exception;
	/**
	 * 司机登陆
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public TcUserPO driverLogin(RequestWrapper request)throws Exception;
	/**
	 * 根据用户ID获取承运商Id
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getLogiIdByUserId(String userId) throws Exception;
}
