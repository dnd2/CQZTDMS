package com.infodms.dms.actions.sales.storageManage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

//import com.infodms.dms.actions.sales.marketmanage.planmanage.ActivitiesCamCost;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.storageManage.AddressAddApplyDAO;
import com.infodms.dms.dao.sales.storageManage.DealerAddressCheckDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVsAddressPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class DealerAddressCheck {
	public Logger logger = Logger.getLogger(DealerAddressCheck.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final DealerAddressCheckDAO dao = new DealerAddressCheckDAO ();
	public static final DealerAddressCheckDAO getInstance() {
		return dao;
	}
	private final String  dealerAddressCheckInitUrl = "/jsp/sales/storageManage/dealerAddressCheckInit.jsp";
	private final String  dealerAddressCheckInitLogisticsUrl = "/jsp/sales/storageManage/dealerAddressCheckInitLogistics.jsp";
	private final String  dealerAddressChangeInfoUrl = "/jsp/sales/storageManage/dealerAddressChangeInfo.jsp";
	private final String  dealerAddressChangeInfoLogisticsUrlUrl = "/jsp/sales/storageManage/dealerAddressChangeInfoLogisticsUrl.jsp";
	private final String  ADDRESS_DTL_URL = "/jsp/sales/storageManage/addressDtl.jsp";
	private final String  ADDRESS_DTL_URL_Logistics = "/jsp/sales/storageManage/addressDtlLogistics.jsp";
	private final String  ADDRESS_DTL_QUERY_URL = "/jsp/sales/storageManage/addressDtlQuery.jsp";
	
	private final String  dealerPassAddressInfoUrl = "/jsp/sales/storageManage/dealerPassAddressInfo.jsp";
	
	public void dealerAddressCheckInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("dutyType", logonUser.getDutyType()) ;
			act.setOutData("areaBusList", areaBusList);
			act.setForword(dealerAddressCheckInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址更改审核：页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	public void queryAddress(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String address = CommonUtils.checkNull(request.getParamValue("address"));
			List<Map<String, Object>> areaBusList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String dutyType = logonUser.getDutyType();
			String orgId = "";
			if (dutyType.equals(Constant.DUTY_TYPE_LARGEREGION+"")) {
				orgId = logonUser.getOrgId()+"";
			}
			
			if(CommonUtils.isNullString(areaId)) {
				List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
				
//				ActivitiesCamCost acc = new ActivitiesCamCost() ;
//				areaId = acc.getAreaStr(areaList) ;
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getAddressList(dutyType, orgId,dealerCode, address, areaId, Constant.PAGE_SIZE, curPage);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址更改审核：查询可审核信息列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//查询该经销商审核通过的地址
	public void queryPassAddressByDealerId(){
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			// String dealerShortName = new String(CommonUtils.checkNull(request.getParamValue("dealerShortName")).getBytes("ISO-8859-1"),"gb2312");
			TmDealerPO td = new TmDealerPO() ;
			td.setDealerId(Long.parseLong(dealerId)) ;
			List<TmDealerPO> dlrList = dao.select(td) ;
			String dealerShortName = dlrList.get(0).getDealerShortname() ;
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
//			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
//			String address = CommonUtils.checkNull(request.getParamValue("address"));
			if(!"".equals(COMMAND)){
				
//				List<Map<String, Object>> areaBusList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
				String dutyType = logonUser.getDutyType();
				String orgId = "";
				if (dutyType.equals(Constant.DUTY_TYPE_LARGEREGION+"")) {
					orgId = logonUser.getOrgId()+"";
				}
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.getPassAddressListByDealerId(orgId, dealerId, areaId, Constant.PAGE_SIZE, curPage);
				act.setOutData("ps", ps);
			}else{
				act.setOutData("dealerCode", dealerCode);
				act.setOutData("dealerShortName", dealerShortName);
				act.setOutData("dealerId", dealerId);
				act.setOutData("areaId", areaId);
				act.setForword(dealerPassAddressInfoUrl);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址更改审核：查看详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void toCheckAddress(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			Map<String,Object> addressInfo = dao.getAddressInfo(id);
			String province = String.valueOf(addressInfo.get("PROVINCE_ID"));
			String city = String.valueOf(addressInfo.get("CITY_ID")); 
			String area = String.valueOf(addressInfo.get("AREA_ID")); 
			act.setOutData("addressInfo", addressInfo);
			act.setOutData("province", province);
			act.setOutData("city", city);
			act.setOutData("area", area);
			act.setForword(dealerAddressChangeInfoUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址更改审核：查看详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void checkSubmit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("addressId"));
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));//1=通过，2=驳回
			String desc = CommonUtils.checkNull(request.getParamValue("desc"));
			String idsArray = CommonUtils.checkNull(request.getParamValue("idsArray"));
			String status = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			if (!"".equals(idsArray)) {
				String[] ids = idsArray.split(",");
				if (null != ids && ids.length>0) {
					for (int j = 0; j < ids.length; j++) {
						TmVsAddressPO tempPO = new TmVsAddressPO();
						tempPO.setId(Long.parseLong(ids[j]));
						TmVsAddressPO valuePO = new TmVsAddressPO();
						if ("1".equals(flag)) {
							status = Constant.ADDRESS_CHECK_PASS.toString() ;
							// 若当前操作为大区审核通过，则修改地址审核状态为物流部审核
							if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
								valuePO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_FIRST_CHK);
								valuePO.setLowAudit(logonUser.getUserId()) ;
								valuePO.setLowCheckDate(new Date(System.currentTimeMillis())) ;
							// 若当前操作为车厂审核，则修改地址审核状态为有效
							} else if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
								valuePO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_Logistics);
								valuePO.setFactoryAudit(logonUser.getUserId()) ;
								valuePO.setFactoryCheckDate(new Date(System.currentTimeMillis())) ;
							}
						}else{
							status = Constant.ADDRESS_CHECK_BACK.toString() ;
							valuePO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_03);
							
							if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
								valuePO.setLowAudit(logonUser.getUserId()) ;
								valuePO.setLowCheckDate(new Date(System.currentTimeMillis())) ;
							// 若当前操作为车厂审核，则修改地址审核状态为有效
							} else if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
								valuePO.setFactoryAudit(logonUser.getUserId()) ;
								valuePO.setFactoryCheckDate(new Date(System.currentTimeMillis())) ;
							}
						}
						valuePO.setUpdateBy(logonUser.getUserId());
						valuePO.setUpdateDate(new Date());
						dao.update(tempPO, valuePO);
						
						Map<String, String> map = new HashMap<String, String>() ;
						map.put("status", status) ;
						map.put("desc", desc) ;
						map.put("addressId", ids[j]) ;
						map.put("userId", logonUser.getUserId().toString()) ;
						
						dao.checkInfoInsert(map) ;
					}
					
				}
			}
			act.setOutData("returnValue", 1);
			//act.setForword(dealerAddressCheckInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址更改审核：提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void checkSubmitOne(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("addressId"));
			String desc = CommonUtils.checkNull(request.getParamValue("desc"));
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));//1=通过，2=驳回
			// String idsArray = CommonUtils.checkNull(request.getParamValue("idsArray"));
			String status = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			TmVsAddressPO tempPO = new TmVsAddressPO();
			tempPO.setId(Long.parseLong(id));
			TmVsAddressPO valuePO = new TmVsAddressPO();
			if ("1".equals(flag)) {
				status = Constant.ADDRESS_CHECK_PASS.toString() ;
				// 若当前操作为大区审核通过，则修改地址审核状态为物流部审核
				if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
					valuePO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_FIRST_CHK);
					valuePO.setLowAudit(logonUser.getUserId()) ;
					valuePO.setLowCheckDate(new Date(System.currentTimeMillis())) ;
				// 若当前操作为车厂审核，则修改地址审核状态为有效
				} else if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
					valuePO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_Logistics);
					valuePO.setFactoryAudit(logonUser.getUserId()) ;
					valuePO.setFactoryCheckDate(new Date(System.currentTimeMillis())) ;
				}
			}else{
				status = Constant.ADDRESS_CHECK_BACK.toString() ;
				
				valuePO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_03);
			}
			valuePO.setUpdateBy(logonUser.getUserId());
			valuePO.setUpdateDate(new Date());
			dao.update(tempPO, valuePO);
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("status", status) ;
			map.put("desc", desc) ;
			map.put("addressId", id) ;
			map.put("userId", logonUser.getUserId().toString()) ;
			
			dao.checkInfoInsert(map) ;
					
			// act.setOutData("returnValue", 1);
			// act.setForword(dealerAddressCheckInitUrl);
			dealerAddressCheckInit() ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址更改审核：提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void toUpdateAddress(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			// String status = CommonUtils.checkNull(request.getParamValue("status"));
			// String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			/*String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String[] areaId_Name = areaId.split("\\|");
			String areaName = areaId_Name[1];*/
			
			// List<Map<String,String>> dealerList = dao.getDealerList(dealerId);	//得到下级经销商
			Map<String,String> addressInfo = AddressAddApplyDAO.getAddressInfo(id);
			
			List<Map<String,Object>> logList = dao.addressCheckQuery(id) ;
			
			AddressAddApply aaa = new AddressAddApply() ;
			String addressStr = aaa.queryAddName(id) ;
			
			String address = addressInfo.get("ADDRESS") ;
			
			String addressUse = addressInfo.get("ADDRESS_USE") ;
			
			address = address.replaceFirst(addressStr, "") ;
			
			String province = String.valueOf(addressInfo.get("PROVINCE_ID"));
			String city = String.valueOf(addressInfo.get("CITY_ID"));
			String area = String.valueOf(addressInfo.get("AREA_ID"));
			String addCode = String.valueOf(addressInfo.get("ADD_CODE"));
			
			Map<String, String> conMap = new HashMap<String, String>() ;
			conMap.put("perp", Constant.ADDRESS_TIME_LIMIT_PERP.toString()) ;
			conMap.put("temp", Constant.ADDRESS_TIME_LIMIT_TEMP.toString()) ;
			
			act.setOutData("conMap", conMap) ;
			act.setOutData("addressUse", addressUse) ;
			act.setOutData("address", address) ;
			// act.setOutData("dealerList", dealerList);
			act.setOutData("province", province);
			act.setOutData("city", city);
			act.setOutData("area", area);
			// act.setOutData("areaName", areaName);
			act.setOutData("addressInfo", addressInfo);
			// act.setOutData("status", status);
			act.setOutData("addCode", addCode);
			act.setOutData("logList", logList) ;
			act.setForword(ADDRESS_DTL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增地址审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void addressDtlQuery(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			// String status = CommonUtils.checkNull(request.getParamValue("status"));
			// String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			/*String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String[] areaId_Name = areaId.split("\\|");
			String areaName = areaId_Name[1];*/
			
			// List<Map<String,String>> dealerList = dao.getDealerList(dealerId);	//得到下级经销商
			Map<String,String> addressInfo = AddressAddApplyDAO.getAddressInfo(id);
			
			List<Map<String,Object>> logList = dao.addressCheckQuery(id) ;
			
			AddressAddApply aaa = new AddressAddApply() ;
			String addressStr = aaa.queryAddName(id) ;
			
			String address = addressInfo.get("ADDRESS") ;
			
			String addressUse = addressInfo.get("ADDRESS_USE") ;
			
			address = address.replaceFirst(addressStr, "") ;
			
			String province = String.valueOf(addressInfo.get("PROVINCE_ID"));
			String city = String.valueOf(addressInfo.get("CITY_ID"));
			String area = String.valueOf(addressInfo.get("AREA_ID"));
			String addCode = String.valueOf(addressInfo.get("ADD_CODE"));
			
			Map<String, String> conMap = new HashMap<String, String>() ;
			conMap.put("perp", Constant.ADDRESS_TIME_LIMIT_PERP.toString()) ;
			conMap.put("temp", Constant.ADDRESS_TIME_LIMIT_TEMP.toString()) ;
			
			act.setOutData("conMap", conMap) ;
			act.setOutData("addressUse", addressUse) ;
			act.setOutData("address", address) ;
			// act.setOutData("dealerList", dealerList);
			act.setOutData("province", province);
			act.setOutData("city", city);
			act.setOutData("area", area);
			// act.setOutData("areaName", areaName);
			act.setOutData("addressInfo", addressInfo);
			// act.setOutData("status", status);
			act.setOutData("addCode", addCode);
			act.setOutData("logList", logList) ;
			act.setForword(ADDRESS_DTL_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增地址明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void dealerAddressCheckInitLogistics(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("dutyType", logonUser.getDutyType()) ;
			act.setOutData("areaBusList", areaBusList);
			act.setForword(dealerAddressCheckInitLogisticsUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址更改审核：页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	public void queryAddressLogistics(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String address = CommonUtils.checkNull(request.getParamValue("address"));
			List<Map<String, Object>> areaBusList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String dutyType = logonUser.getDutyType();
			String orgId = "";
			if (dutyType.equals(Constant.DUTY_TYPE_LARGEREGION+"")) {
				orgId = logonUser.getOrgId()+"";
			}
			
			if(CommonUtils.isNullString(areaId)) {
				List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
				
//				ActivitiesCamCost acc = new ActivitiesCamCost() ;
//				areaId = acc.getAreaStr(areaList) ;
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getAddressListLogistics(dutyType, orgId,dealerCode, address, areaId, Constant.PAGE_SIZE, curPage);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址更改审核：查询可审核信息列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void checkSubmitLogistics(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("addressId"));
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));//1=通过，2=驳回
			String desc = CommonUtils.checkNull(request.getParamValue("desc"));
			String idsArray = CommonUtils.checkNull(request.getParamValue("idsArray"));
			String status = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			if (!"".equals(idsArray)) {
				String[] ids = idsArray.split(",");
				if (null != ids && ids.length>0) {
					for (int j = 0; j < ids.length; j++) {
						TmVsAddressPO tempPO = new TmVsAddressPO();
						tempPO.setId(Long.parseLong(ids[j]));
						TmVsAddressPO valuePO = new TmVsAddressPO();
						if ("1".equals(flag)) {
							status = Constant.ADDRESS_CHECK_PASS.toString() ;
							// 修改地址审核状态为物流部审核
							
								valuePO.setStatus(Constant.STATUS_ENABLE);
								/*valuePO.setLowAudit(logonUser.getUserId()) ;
								valuePO.setLowCheckDate(new Date(System.currentTimeMillis())) ;*/
						
						}else{
							status = Constant.ADDRESS_CHECK_BACK.toString() ;
							valuePO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_03);
							
							/*if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
								valuePO.setLowAudit(logonUser.getUserId()) ;
								valuePO.setLowCheckDate(new Date(System.currentTimeMillis())) ;
							// 若当前操作为车厂审核，则修改地址审核状态为有效
							} else if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
								valuePO.setFactoryAudit(logonUser.getUserId()) ;
								valuePO.setFactoryCheckDate(new Date(System.currentTimeMillis())) ;
							}*/
						}
						valuePO.setUpdateBy(logonUser.getUserId());
						valuePO.setUpdateDate(new Date());
						dao.update(tempPO, valuePO);
						
						Map<String, String> map = new HashMap<String, String>() ;
						map.put("status", status) ;
						map.put("desc", desc) ;
						map.put("addressId", ids[j]) ;
						map.put("userId", logonUser.getUserId().toString()) ;
						
						dao.checkInfoInsert(map) ;
					}
					
				}
			}
			act.setOutData("returnValue", 1);
			//act.setForword(dealerAddressCheckInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址更改审核：提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void checkSubmitOneLogistics(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("addressId"));
			String desc = CommonUtils.checkNull(request.getParamValue("desc"));
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));//1=通过，2=驳回
			// String idsArray = CommonUtils.checkNull(request.getParamValue("idsArray"));
			String status = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			TmVsAddressPO tempPO = new TmVsAddressPO();
			tempPO.setId(Long.parseLong(id));
			TmVsAddressPO valuePO = new TmVsAddressPO();
			if ("1".equals(flag)) {
				status=Constant.ADDRESS_CHECK_PASS.toString();
					valuePO.setStatus(Constant.STATUS_ENABLE);
					valuePO.setLowAudit(logonUser.getUserId()) ;
					valuePO.setLowCheckDate(new Date(System.currentTimeMillis())) ;
				
			}else{
				status = Constant.ADDRESS_CHECK_BACK.toString() ;
				
				valuePO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_03);
			}
			valuePO.setUpdateBy(logonUser.getUserId());
			valuePO.setUpdateDate(new Date());
			dao.update(tempPO, valuePO);
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("status", status) ;
			map.put("desc", desc) ;
			map.put("addressId", id) ;
			map.put("userId", logonUser.getUserId().toString()) ;
			
			
			dao.checkInfoInsert(map) ;
					
			// act.setOutData("returnValue", 1);
			// act.setForword(dealerAddressCheckInitUrl);
			dealerAddressCheckInitLogistics() ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址更改审核：提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void toCheckAddressLogistics(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			Map<String,Object> addressInfo = dao.getAddressInfo(id);
			String province = String.valueOf(addressInfo.get("PROVINCE_ID"));
			String city = String.valueOf(addressInfo.get("CITY_ID")); 
			String area = String.valueOf(addressInfo.get("AREA_ID")); 
			act.setOutData("addressInfo", addressInfo);
			act.setOutData("province", province);
			act.setOutData("city", city);
			act.setOutData("area", area);
			act.setForword(dealerAddressChangeInfoLogisticsUrlUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址更改审核：查看详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void toUpdateAddressLogistics(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			// String status = CommonUtils.checkNull(request.getParamValue("status"));
			// String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			/*String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String[] areaId_Name = areaId.split("\\|");
			String areaName = areaId_Name[1];*/
			
			// List<Map<String,String>> dealerList = dao.getDealerList(dealerId);	//得到下级经销商
			Map<String,String> addressInfo = AddressAddApplyDAO.getAddressInfo(id);
			
			List<Map<String,Object>> logList = dao.addressCheckQuery(id) ;
			
			AddressAddApply aaa = new AddressAddApply() ;
			String addressStr = aaa.queryAddName(id) ;
			
			String address = addressInfo.get("ADDRESS") ;
			
			String addressUse = addressInfo.get("ADDRESS_USE") ;
			
			address = address.replaceFirst(addressStr, "") ;
			
			String province = String.valueOf(addressInfo.get("PROVINCE_ID"));
			String city = String.valueOf(addressInfo.get("CITY_ID"));
			String area = String.valueOf(addressInfo.get("AREA_ID"));
			String addCode = String.valueOf(addressInfo.get("ADD_CODE"));
			
			Map<String, String> conMap = new HashMap<String, String>() ;
			conMap.put("perp", Constant.ADDRESS_TIME_LIMIT_PERP.toString()) ;
			conMap.put("temp", Constant.ADDRESS_TIME_LIMIT_TEMP.toString()) ;
			
			act.setOutData("conMap", conMap) ;
			act.setOutData("addressUse", addressUse) ;
			act.setOutData("address", address) ;
			// act.setOutData("dealerList", dealerList);
			act.setOutData("province", province);
			act.setOutData("city", city);
			act.setOutData("area", area);
			// act.setOutData("areaName", areaName);
			act.setOutData("addressInfo", addressInfo);
			// act.setOutData("status", status);
			act.setOutData("addCode", addCode);
			act.setOutData("logList", logList) ;
			act.setForword(ADDRESS_DTL_URL_Logistics);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增地址审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
