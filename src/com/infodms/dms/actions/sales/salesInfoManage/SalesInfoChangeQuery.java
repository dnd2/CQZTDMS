package com.infodms.dms.actions.sales.salesInfoManage;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.salesInfoManage.SalesInfoChangeQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : SalesInfoChangeQuery.java
 * @Package: com.infodms.dms.actions.sales.salesInfoManage
 * @Description: 实销信息更改查询OEM
 * @date   : 2010-6-30 
 * @version: V1.0   
 */
public class SalesInfoChangeQuery extends BaseDao{
	public Logger logger = Logger.getLogger(SalesInfoChangeQuery.class);
	private ActionContext act = ActionContext.getContext();
	private static final SalesInfoChangeQuery dao = new SalesInfoChangeQuery ();
	public static final SalesInfoChangeQuery getInstance() {
		return dao;
	}
	RequestWrapper request = act.getRequest();
	private final String  salesInfoChangeQueryInit = "/jsp/sales/salesInfoManage/salesInfoChangeQueryInit.jsp";
	
	
	/** 
	* @Title	  : salesInfoChangeQueryInit 
	* @Description: 实销信息更改查询页面初始化
	* @throws 
	* @LastUpdate :2010-6-30
	*/
	public void salesInfoChangeQueryInit(){
		AclUserBean logonUser = null;
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(salesInfoChangeQueryInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息查询页面初始化OEM");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : salesInfoChangeQueryList 
	* @Description: 实销信息更改查询结果展示
	* @throws 
	* @LastUpdate :2010-6-30
	*/
	public void salesInfoChangeQueryList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type")); 	// 客户类型
			String customer_name = CommonUtils.checkNull(request.getParamValue("customer_name")); 	// 客户名称
			String vin = CommonUtils.checkNull(request.getParamValue("vin")); 						// VIN
			String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone")); // 客户电话
			String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name")); 		// 集团客户名称
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")); 	// 选择物料组
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet")); 			// 是否集团客户
			String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no")); 		// 集团客户合同
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate")); 			// 上报日期:开始
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); 				// 上报日期 ：截止
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); 		// 经销商代码
			String checkStatus = CommonUtils.checkNull(request.getParamValue("checkStatus")); 		// 审核状态
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId")); 				//业务范围
			Map<String, String> map = new HashMap<String, String>();
			
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			map.put("orgId", orgId);
			map.put("customer_type", customer_type);
			map.put("customer_name", customer_name);
			map.put("vin", vin);
			map.put("customer_phone", customer_phone);
			map.put("fleet_name", fleet_name);
			map.put("materialCode", materialCode);
			map.put("is_fleet", is_fleet);
			map.put("contract_no", contract_no);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("dealerCode", dealerCode);
			map.put("checkStatus", checkStatus);
			map.put("areaId", areaId);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesInfoChangeQueryDAO.salesInfoChangeQueryList(map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询可审核信息 ");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
