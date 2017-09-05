package com.infodms.dms.actions.sales.customerInfoManage;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.GetCommonArea;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.customerInfoManage.SalesInfoChangeQueryDAO;
import com.infodms.dms.dao.sales.customerInfoManage.SalesReportDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmCompanyPactPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TtCustomerEditLogPO;
import com.infodms.dms.po.TtDealerActualSalesAuditPO;
import com.infodms.dms.po.TtFleetContractPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : SalesInfoChangeQuery.java
 * @Package: com.infodms.dms.actions.sales.customerInfoManage
 * @Description: 实销信息更改查询
 * @date   : 2010-6-29 
 * @version: V1.0   
 */
public class SalesInfoChangeQuery  extends BaseDao{
	public Logger logger = Logger.getLogger(SalesInfoChangeQuery.class);
	private ActionContext act = ActionContext.getContext();
	private static final SalesInfoChangeQuery dao = new SalesInfoChangeQuery();
	public static final SalesInfoChangeQuery getInstance() {
		return dao;
	}
	RequestWrapper request = act.getRequest();
	
	private final String  salesInfoChangeQueryInit = "/jsp/sales/customerInfoManage/salesInfoChangeQueryInit.jsp";
	private final String  salesInfoChangeQueryDetail = "/jsp/sales/customerInfoManage/salesInfoChangeQueryDetail.jsp";
	 
	/** 
	* @Title	  : salesInfoChangeQueryInit 
	* @Description: 实销信息更改查询页面初始化
	* @throws 
	* @LastUpdate :2010-6-29
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
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList1 = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
			act.setOutData("areaList", areaList1);
			act.setForword(salesInfoChangeQueryInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息更改申请页面初始化 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : salesInfoChangeQuery 
	* @Description: 实销信息更改查询:结果展示
	* @throws 
	* @LastUpdate :2010-6-29
	*/
	public void salesInfoChangeQuery(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.getSession().get(Constant.LOGON_USER);
			
			DealerRelation dr = new DealerRelation() ;
			String dealerId = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId());					//当前用户职位对应的经销商ID
			String areaId= CommonUtils.checkNull(request.getParamValue("areaId"));
			String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type"));	//客户类型
			String customer_name = CommonUtils.checkNull(request.getParamValue("customer_name"));	//客户名称
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));						//VIN
			String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone"));	//客户电话
			String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name"));			//集团客户名称
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));		//选择物料组
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet"));				//是否集团客户
			String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no"));		//集团客户合同
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));			//上报日期:开始
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));				//上报日期 ：截止
			String status = CommonUtils.checkNull(request.getParamValue("status"));					//状态
			Map<String,String> map = new HashMap<String, String>();
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
			map.put("status", status);
			map.put("logonDealerId", dealerId) ;
			map.put("areaId", areaId);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesInfoChangeQueryDAO.getChangeList(dealerId, map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息更改查询:结果展示 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : salesInfoChangeQueryDetail 
	* @Description: 实销信息更改查询:详细查询
	* @throws 
	* @LastUpdate :2010-6-29
	*/
	public void salesInfoChangeQueryDetail(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.getSession().get(Constant.LOGON_USER);
			
			String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicleId"));
			String isFleet = CommonUtils.checkNull(request.getParamValue("isFleet"));		 			//是否是集团客户
			String ctm_edit_id = CommonUtils.checkNull(request.getParamValue("ctm_edit_id"));			//"客户信息更改"id
			String log_id = CommonUtils.checkNull(request.getParamValue("log_id"));			 			//"实销信息更改"id
			
			Map<String,Object> vehicleInfo = SalesReportDAO.getVehicleInfo(vehicleId);					//1、车辆资料 
			act.setOutData("vehicleInfo", vehicleInfo);
			
			//通过实效id获取下面上传附件的文件地址
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.valueOf(log_id));
			List<FsFileuploadPO> lists = dao.select(detail);
			
			TtDealerActualSalesAuditPO auditPO = new TtDealerActualSalesAuditPO();
			auditPO.setLogId(Long.parseLong(log_id));
			List aList = dao.select(auditPO);
			if (null != aList && aList.size()>0) {
				TtDealerActualSalesAuditPO salesAuditInfo = (TtDealerActualSalesAuditPO)aList.get(0);	//2、销售信息
				act.setOutData("salesAuditInfo", salesAuditInfo);
				/*
				 * 如果是集团客户
				 * */
				if (null != isFleet && isFleet.equals(Constant.IF_TYPE_YES+"")) {
					Long fleetId = salesAuditInfo.getFleetId();										 	//集团客户id
					Long contractId = salesAuditInfo.getContractId();								 	//集团客户合同id
					
					TmCompanyPactPO compangPactPo=new TmCompanyPactPO();
					compangPactPo.setPactId(fleetId);
					List cList = dao.select(compangPactPo);
					if (null != cList && cList.size()>0) {
						String cName = ((TmCompanyPactPO)cList.get(0)).getPactName(); 	//集团客户名称
						act.setOutData("cName", cName);
					}
					TmFleetPO fleetPO = new TmFleetPO();
					fleetPO.setFleetId(fleetId);
					List fList = dao.select(fleetPO);
					if (null != fList && fList.size()>0) {
						String fleetName = ((TmFleetPO)fList.get(0)).getFleetName();				 	//集团客户名称
						act.setOutData("fleetName", fleetName);
					}
					TtFleetContractPO contractPO = new TtFleetContractPO();
					contractPO.setContractId(contractId);
					List conList = dao.select(contractPO);
					if (null != conList && conList.size()>0) {
						String contractNo = ((TtFleetContractPO)conList.get(0)).getContractNo();		//集团客户合同
						act.setOutData("contractNo", contractNo);
					}
				}
				
					TtCustomerEditLogPO editLogPO = new TtCustomerEditLogPO();
					editLogPO.setCtmEditId(Long.parseLong(ctm_edit_id));
					List cList = dao.select(editLogPO);
					if (null != cList && cList.size()>0) {
						TtCustomerEditLogPO cusInfo = (TtCustomerEditLogPO)cList.get(0);
						act.setOutData("cusInfo", cusInfo);												//3、客户信息
						
						List linkManList = SalesReportDAO.getLink_List(cusInfo.getCtmId()); 			//4、其他联系人信息
						act.setOutData("linkManList", linkManList);
					}
			}
			act.setOutData("lists",lists);
			act.setForword(salesInfoChangeQueryDetail);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息更改查询:详细查询 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
