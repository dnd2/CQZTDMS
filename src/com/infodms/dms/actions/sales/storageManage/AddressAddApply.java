package com.infodms.dms.actions.sales.storageManage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.relation.DealerRelationDAO;
import com.infodms.dms.dao.sales.storageManage.AddressAddApplyDAO;
import com.infodms.dms.dao.sales.storageManage.DealerAddressCheckDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVsAddressPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * FUNCTION		:	经销商新增地址申请：页面初始化
 * @param 		:
 * @return		:
 * @throws		:
 * LastUpdate	:	2010-5-20
 */
public class AddressAddApply {
	public Logger logger = Logger.getLogger(AddressAddApply.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final AddressAddApplyDAO dao = AddressAddApplyDAO.getInstance();
	public static final AddressAddApplyDAO getInstance() {
		return dao;
	}
	private final String  addressAddApplyInitUrl = "/jsp/sales/storageManage/addressAddApplyInit.jsp";
	private final String  addressAddApplyInitDoUrl = "/sales/storageManage/AddressAddApply/addressAddApplyInit.do";
	private final String  addressInfoUrl = "/jsp/sales/storageManage/addressInfo.jsp";
	private final String  allAddListUrl = "/jsp/sales/storageManage/allAddList.jsp";
	private final String  addressUpdateUrl = "/jsp/sales/storageManage/addressUpdateInit.jsp" ;

	public void addressAddApplyInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
//			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
//			act.setOutData("areaList", areaList);

//			int isFlag ;

//			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
//
//			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
//				isFlag = 1 ;
//			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
//				isFlag = 0 ;
//			} else {
//				throw new RuntimeException("判断当前系统的系统参数错误！") ;
//			}

//			act.setOutData("isFlag", isFlag) ;

			act.setForword(addressAddApplyInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增地址申请：页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void checkCanAddList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//			String dealerId = "";
//			String onlyAreaId = "" ;
//			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String address = CommonUtils.checkNull(request.getParamValue("address"));
//			String[] areaId_Name = areaId.split("\\|");
			String dealerId = logonUser.getDealerId();
//			if ("".equals(areaId)) {
//				dealerId = dealerIds;
//			}else{
//				dealerId = areaId_Name[0];
//
//				onlyAreaId = areaId_Name[3];
//			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getCanAddList(dealerId, address, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增地址申请：结果展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void toUpdateAddress(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String pageCount = CommonUtils.checkNull(request.getParamValue("pageCount")) ;
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String[] areaId_Name = areaId.split("\\|");
			String areaName = areaId_Name[1];
			String isSpecialCar = "0";
			if (areaId_Name.length>=3) {
				String areaType = areaId_Name[2];
				//业务范围是否是“专用车”：0=否，1=是
				if ((Constant.AREA_YTPE_01+"").equals(areaType)) {
					isSpecialCar = "1";
				}
			}

			List<Map<String,String>> dealerList = AddressAddApplyDAO.getDealerList(dealerId);	//得到下级经销商
			Map<String,String> addressInfo = AddressAddApplyDAO.getAddressInfo(id);

			DealerAddressCheckDAO dac = DealerAddressCheckDAO.getInstance();
			List<Map<String,Object>> logList = dac.addressCheckQuery(id) ;

			String addressStr = queryAddName(id) ;

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
			act.setOutData("logList", logList) ;
			act.setOutData("isSpecialCar", isSpecialCar);
			act.setOutData("dealerList", dealerList);
			act.setOutData("province", province);
			act.setOutData("city", city);
			act.setOutData("area", area);
			act.setOutData("areaName", areaName);
			act.setOutData("addressInfo", addressInfo);
			act.setOutData("status", status);
			act.setOutData("addCode", addCode);
			act.setOutData("logFlag", "1") ;
			act.setOutData("oldPageCount", pageCount) ;
			act.setForword(addressInfoUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增地址申请：修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void saveOrSubmit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;

			String flag = CommonUtils.checkNull(request.getParamValue("flag"));				//flag: 1=保存，2=提交
			String addressId = CommonUtils.checkNull(request.getParamValue("addressId"));	//地址id
			//String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String addCode = CommonUtils.checkNull(request.getParamValue("addCode"));
			String address = CommonUtils.checkNull(request.getParamValue("address")).trim();
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String receiveOrg = CommonUtils.checkNull(request.getParamValue("receiveOrg"));
			// String dealerId = CommonUtils.checkNull(request.getParamValue("dlrList"));
			String province = CommonUtils.checkNull(request.getParamValue("province"));
			String city = CommonUtils.checkNull(request.getParamValue("city"));
			String area = CommonUtils.checkNull(request.getParamValue("area"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String addressUse = CommonUtils.checkNull(request.getParamValue("addressUse"));
			String limitType = CommonUtils.checkNull(request.getParamValue("limit"));
			String limitStartDate = CommonUtils.checkNull(request.getParamValue("limitStartDate"));
			String limitEndDate = CommonUtils.checkNull(request.getParamValue("limitEndDate"));
			String[] areas = request.getParamValues("addressAreas") ;

			address = getAddDet(province, city, area) + address ;

			TmVsAddressPO addressPO = new TmVsAddressPO();
			if ((Constant.STATUS_ENABLE+"").equals(status)) {
				addressPO.setLinkMan(linkMan.trim());
				addressPO.setTel(tel.trim());
				if (!"".equals(remark)) {
					addressPO.setRemark(remark);
				}
			}else{
				if ("".equals(addressId)) {
					addressPO.setId(Long.parseLong(SequenceManager.getSequence("")));
				} /*else {
					String addressStr = queryAddName(addressId) ;

					if(CommonUtils.isNullString(addressStr)) {
						address = getAddDet(province, city, area) + address ;
					} else {
						address = address.replaceFirst(addressStr, getAddDet(province, city, area)) ;
					}
				}*/

				if(areas != null) {
					int len = areas.length ;

					for(int i=0; i<len; i++) {
						String areaId = areas[i].split("\\|")[0] ;
						String dlrId = areas[i].split("\\|")[1] ;

						addressPO.setAddCode(addCode.trim());
						addressPO.setAddress(address.trim());
						addressPO.setLinkMan(linkMan.trim());
						addressPO.setTel(tel.trim());

						if (!"".equals(remark)) {
							addressPO.setRemark(remark);
						}

						addressPO.setReceiveOrg(receiveOrg.trim());

						if (!"".equals(dlrId)) {
							addressPO.setDealerId(Long.parseLong(dlrId));
						}

						if (!"".equals(province)) {
							addressPO.setProvinceId(Long.parseLong(province));
						}

						if (!"".equals(city)) {
							addressPO.setCityId(Long.parseLong(city));
						}

						if (!"".equals(area)) {
							addressPO.setAreaId(Long.parseLong(area));
						}

						if(!CommonUtils.isNullString(addressUse)) {
							addressPO.setAddressUse(addressUse) ;
						}

						addressPO.setBAreaId(Long.parseLong(areaId)) ;

						if(!CommonUtils.isNullString(limitType)) {
							addressPO.setLimitType(Long.parseLong(limitType)) ;

							if(Constant.ADDRESS_TIME_LIMIT_TEMP.toString().equals(limitType)) {
								addressPO.setStartTime(sdf.parse(limitStartDate)) ;
								addressPO.setEndTime(sdf.parse(limitEndDate)) ;
							}
						}
					}
				}
			}
			//新增操作
			if ("".equals(addressId)) {
				// addressPO.setBAreaId(Long.parseLong(areaId));
				addressPO.setCreateBy(logonUser.getUserId());
				addressPO.setCreateDate(new Date());
				if ("1".equals(flag)) {
					addressPO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_01);

				}else{
					addressPO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_02);
				}
				dao.insert(addressPO);
			}else{
				TmVsAddressPO tempPO = new TmVsAddressPO();
				tempPO.setId(Long.parseLong(addressId));
				addressPO.setUpdateBy(logonUser.getUserId());
				addressPO.setUpdateDate(new Date());

				//修改有效的地址，不需要审核，否则需要审核
				if (!(Constant.STATUS_ENABLE+"").equals(status) && "2".equals(flag)) {
					addressPO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_02);
				} else {
					if ("1".equals(flag)) {
						addressPO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_01);

					}else{
						addressPO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_02);
					}
				}
				dao.update(tempPO, addressPO);

				/*Map<String, String> map = new HashMap<String, String>() ;

				map.put("addressId", tempPO.getId().toString()) ;
				map.put("userId", logonUser.getUserId().toString()) ;

				AddressAreaDAO.getInstance().addressAreaDelete(map) ;

				if(areas != null) {
					int len = areas.length ;

					for(int i=0; i<len; i++) {
						map.put("areaId", areas[i]) ;

						AddressAreaDAO.getInstance().addressAreaInsert(map) ;
					}
				}*/
			}
			act.setOutData("returnValue", 1);
			//act.setForword(addressAddApplyInitDoUrl);
		} catch (Exception e) {
			act.setOutData("returnValue", 2);
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增地址申请：修改或提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void addAddress(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String dealerId = areaId.split("\\|")[0];
			String areaName = areaId.split("\\|")[1];
			String areaIdValue = areaId.split("\\|")[3];
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerId(Long.parseLong(dealerId));
			List dList = dao.select(dealerPO);
			if (null != dList && dList.size()>0) {
				String addCode = "";
				TmDealerPO resPO = (TmDealerPO)dList.get(0);
				String dealerCode = resPO.getDealerCode();
				/*String maxAddCode = ((Map<String,String>)dao.getMaxAddCodeByDealerId(dealerId)).get("MAX_ADD_CODE");
				if (null != maxAddCode && !"".equals(maxAddCode)) {
					String[] array = maxAddCode.split("-");
					if (null != array && array.length == 2) {
						int addNo = Integer.parseInt(array[1])+1;
						String addNO = addNo+"";
						if (addNO.length() == 1) {
							addNO = addNO.format("00"+addNo, addNo);
						}
						if (addNO.length() == 2 ) {
							addNO = addNO.format("0"+addNo, addNo);
						}
						addCode = dealerCode + "-"+addNO;
					}else{
						addCode = dealerCode + "-"+"001";
					}
				}else{
					addCode = dealerCode + "-"+"001";
				}*/
				StringBuffer maxIndex = dao.getMaxCode(dealerCode, dealerId) ;

				if(!"".equals(maxIndex)) {
					Integer Index = Integer.parseInt(maxIndex.toString()) + 1  ;

					maxIndex = new StringBuffer(Index.toString()) ;

					int len = maxIndex.length() ;

					for(int i=0; i<3-len; i++) {
						maxIndex.insert(0, "0") ;
					}

					addCode = dealerCode + "-" + maxIndex.toString() ;
				} else {
					addCode = dealerCode + "-" + "001" ;
				}

				act.setOutData("addCode", addCode);
			}
			List<Map<String,String>> dealerList = dao.getDealerList(dealerId);

			Map<String, String> conMap = new HashMap<String, String>() ;
			conMap.put("perp", Constant.ADDRESS_TIME_LIMIT_PERP.toString()) ;
			conMap.put("temp", Constant.ADDRESS_TIME_LIMIT_TEMP.toString()) ;

			act.setOutData("conMap", conMap) ;
			act.setOutData("dealerList", dealerList);
			act.setOutData("dealerId", dealerId);
			act.setOutData("areaName", areaName);
			act.setOutData("areaId", areaIdValue);
			act.setForword(addressInfoUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增地址申请：修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void submitAction(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			TmVsAddressPO tempPO = new TmVsAddressPO();
			tempPO.setId(Long.parseLong(id));
			TmVsAddressPO valuePO = new TmVsAddressPO();
			valuePO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_02);
			valuePO.setUpdateBy(logonUser.getUserId());
			valuePO.setUpdateDate(new Date());
			dao.update(tempPO, valuePO);
			act.setOutData("returnValue", "1");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增地址申请：提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void deleteAction(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			TmVsAddressPO tempPO = new TmVsAddressPO();
			tempPO.setId(Long.parseLong(id));
			dao.delete(tempPO);
			act.setOutData("returnValue", "1");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增地址申请：删除");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void useAgainAction(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			TmVsAddressPO tempPO = new TmVsAddressPO();
			tempPO.setId(Long.parseLong(id));
			TmVsAddressPO valuePO = new TmVsAddressPO();
			valuePO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_02);
			dao.update(tempPO, valuePO);
			act.setOutData("returnValue", "1");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增维护：重新启用");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void useStopAction(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			TmVsAddressPO tempPO = new TmVsAddressPO();
			tempPO.setId(Long.parseLong(id));
			TmVsAddressPO valuePO = new TmVsAddressPO();
			valuePO.setStatus(Constant.STATUS_DISABLE);
			dao.update(tempPO, valuePO);
			act.setOutData("returnValue", "1");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增维护：停止使用");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void queryAllAdd(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerId = logonUser.getDealerId();
			List<Map<String,Object>> addList = dao.getAllAddList(dealerId);

			if(!CommonUtils.isNullList(addList)) {
				StringBuffer address_s = new StringBuffer("") ;

				int len = addList.size() ;

				for(int i=0; i<len; i++) {
					String addressStr = queryAddName(addList.get(i).get("ID").toString()) ;

					if(address_s.length() <= 0) {
						address_s.append(addList.get(i).get("ADDRESS").toString().replaceFirst(addressStr, "")) ;
					} else {
						address_s.append(",").append(addList.get(i).get("ADDRESS").toString().replaceFirst(addressStr, "")) ;
					}
				}

				act.setOutData("address_s", address_s) ;
			}

			act.setOutData("addList", addList);
			act.setForword(allAddListUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增维护：查询已维护地址");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public String queryAddName(String addressId) {
		Map<String, Object> addMap = dao.queryAddName(addressId) ;
		String province = "" ;
		String city = "" ;
		String area = "" ;

		if(null != addMap.get("PROVINCE_ID")) {
			province = addMap.get("PROVINCE_ID").toString() ;
		}

		if(null != addMap.get("CITY_ID")) {
			city = addMap.get("CITY_ID").toString() ;
		}

		if(null != addMap.get("AREA_ID")) {
			area = addMap.get("AREA_ID").toString() ;
		}

		return getAddDet(province, city, area) ;
	}

	public String getAddDet(String province, String city, String area) {
		StringBuffer str = new StringBuffer("") ;

		return str.append(dao.getAddName(province)).append(dao.getAddName(city)).append(dao.getAddName(area)).toString() ;
	}

	public void addAddressNew(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId = logonUser.getCompanyId() ;
			Long poseId = logonUser.getPoseId() ;

			DealerRelation dr = new DealerRelation() ;

			List<Map<String, Object>> dlrList = dr.getDealerPoseRelation(companyId, poseId) ;

			Map<String, String> conMap = new HashMap<String, String>() ;
			conMap.put("perp", Constant.ADDRESS_TIME_LIMIT_PERP.toString()) ;
			conMap.put("temp", Constant.ADDRESS_TIME_LIMIT_TEMP.toString()) ;

			act.setOutData("conMap", conMap) ;
			act.setOutData("dlrList", dlrList);

			act.setForword(addressInfoUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增地址申请：修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void toUpdateAddressNew(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String pageCount = CommonUtils.checkNull(request.getParamValue("pageCount")) ;
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));

			//Long companyId = logonUser.getCompanyId() ;
			Long poseId = logonUser.getPoseId() ;

		//	DealerRelation dr = new DealerRelation() ;

			//List<Map<String, Object>> dlrList = dr.getDealerPoseRelation(companyId, poseId) ;

			Map<String,String> addressInfo = AddressAddApplyDAO.getAddressInfo(id);

			DealerAddressCheckDAO dac = DealerAddressCheckDAO.getInstance();
			List<Map<String,Object>> logList = dac.addressCheckQuery(id) ;

			String addressStr = queryAddName(id) ;

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

//			act.setOutData("dlrList", dlrList) ;
			act.setOutData("conMap", conMap) ;
			act.setOutData("addressUse", addressUse) ;
			act.setOutData("address", address) ;
			act.setOutData("logList", logList) ;
			act.setOutData("province", province);
			act.setOutData("city", city);
			act.setOutData("area", area);
			act.setOutData("addressInfo", addressInfo);
			act.setOutData("status", status);
			act.setOutData("addCode", addCode);
			act.setOutData("logFlag", "1") ;
			act.setOutData("oldPageCount", pageCount) ;
			act.setOutData("dealerId", dealerId) ;
			act.setForword(addressUpdateUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增地址申请：修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public String getAddressCode(String dealerId) {
		TmDealerPO dealerPO = new TmDealerPO();
		dealerPO.setDealerId(Long.parseLong(dealerId));
		List dList = dao.select(dealerPO);

		String addCode = "";

		if (null != dList && dList.size()>0) {
			TmDealerPO resPO = (TmDealerPO)dList.get(0);
			String dealerCode = resPO.getDealerCode();
			String maxAddCode = ((Map<String,String>)dao.getMaxAddCodeByDealerId(dealerId)).get("MAX_ADD_CODE");
			if (null != maxAddCode && !"".equals(maxAddCode)) {
				String[] array = maxAddCode.split("-");
				if (null != array && array.length == 2) {
					int addNo = Integer.parseInt(array[1])+1;
					String addNO = addNo+"";
					if (addNO.length() == 1) {
						addNO = addNO.format("00"+addNo, addNo);
					}
					if (addNO.length() == 2 ) {
						addNO = addNO.format("0"+addNo, addNo);
					}
					addCode = dealerCode + "-"+addNO;
				}else{
					addCode = dealerCode + "-"+"001";
				}
			}else{
				addCode = dealerCode + "-"+"001";
			}
			StringBuffer maxIndex = dao.getMaxCode(dealerCode, dealerId) ;

			if(!"".equals(maxIndex)) {
				Integer Index = Integer.parseInt(maxIndex.toString()) + 1  ;

				maxIndex = new StringBuffer(Index.toString()) ;

				int len = maxIndex.length() ;

				for(int i=0; i<3-len; i++) {
					maxIndex.insert(0, "0") ;
				}

				addCode = dealerCode + "-" + maxIndex.toString() ;
			} else {
				addCode = dealerCode + "-" + "001" ;
			}
		}

		return addCode ;
	}

	public void getAreas(String dealerId, String addressId) {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		StringBuffer areaIds = new StringBuffer("") ;
		StringBuffer areaNames = new StringBuffer("") ;
		StringBuffer checkAreas = new StringBuffer("") ;
		List<Map<String, Object>> areaList = DealerRelationDAO.getInstance().getAreaByDlrAndPose(dealerId, logonUser.getPoseId().toString()) ;

		if(!CommonUtils.isNullString(addressId)) {
			Map<String, String> addressMap = dao.getAddressInfo(addressId) ;

			if(!CommonUtils.isNullMap(addressMap)) {
				checkAreas.append(addressMap.get("THE_AREA_ID") == null ? "" : addressMap.get("THE_AREA_ID")).append("|").append(addressMap.get("DEALER_ID") == null ? "" : addressMap.get("DEALER_ID")) ;
			}

			/*List<TtAddressAreaRPO> taarList = AddressAreaDAO.getInstance().addressAreaQuery(addressId) ;

			if(!CommonUtils.isNullList(taarList)) {
				int len = taarList.size() ;

				for(int i=0; i<len; i++) {
					if(i == 0) {
						checkAreas.append(taarList.get(i).getAreaId().toString()) ;
					} else {
						checkAreas.append(",").append(taarList.get(i).getAreaId().toString()) ;
					}
				}
			}*/

			act.setOutData("checkAreas", checkAreas.toString()) ;
		}

		if(!CommonUtils.isNullList(areaList)) {
			int len = areaList.size() ;

			for(int i=0; i<len; i++) {
				if(i == 0) {
					areaIds.append(areaList.get(i).get("AREA_ID").toString() + "|" + areaList.get(i).get("DEALER_ID").toString()) ;
					areaNames.append(areaList.get(i).get("AREA_NAME").toString()) ;
				} else {
					areaIds.append(",").append(areaList.get(i).get("AREA_ID").toString() + "|" + areaList.get(i).get("DEALER_ID").toString()) ;
					areaNames.append(",").append(areaList.get(i).get("AREA_NAME").toString()) ;
				}
			}
		}

		act.setOutData("areaIds", areaIds.toString()) ;
		act.setOutData("areaNames", areaNames.toString()) ;
	}

	public void dlrSelChange() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		//String dealerId = CommonUtils.checkNull(request.getParamValue("dlrList")) ;
		String addressId = CommonUtils.checkNull(request.getParamValue("addressId")) ;
		//String addCode = CommonUtils.checkNull(request.getParamValue("addCode")) ;

		/*if(CommonUtils.isNullString(addressId)) {
			act.setOutData("addCode", this.getAddressCode(dealerId)) ;
		} else {
			act.setOutData("addCode", addCode) ;
		}*/

		getAreas(logonUser.getDealerId(), addressId) ;
	}




	public boolean chkAddressUpdate(Map<String, String> map) {
		Map<String, Object> theMap = dao.chkUpdateSame(map) ;

		if(!CommonUtils.isNullMap(theMap)) {
			return true ;
		} else {
			return false ;
		}
	}

	public void chkAddressSame() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String flagStr = null ;
		String addressId = CommonUtils.checkNull(request.getParamValue("addressId")) ;
		String address = CommonUtils.checkNull(request.getParamValue("address")).trim();
		String province = CommonUtils.checkNull(request.getParamValue("province"));
		String city = CommonUtils.checkNull(request.getParamValue("city"));
		String area = CommonUtils.checkNull(request.getParamValue("area"));
		address = getAddDet(province, city, area) + address ;
		String dealerId = logonUser.getDealerId();
		Map<String, String> map = new HashMap<String, String>();
		map.put("dealerId", dealerId);
		map.put("address", address);
		//新增
		if(null != addressId && !"".equals(addressId)){
			map.put("addressId", addressId);
		}		
		List<Map<String, Object>> list = dao.chkSame(map) ;
		if(null != list && list.size() >0){
			list.get(0).get("");
		}
//		if(areas != null) {
//			int len = areas.length ;
//
//			Map<String, String> map = new HashMap<String, String>() ;
//
//			map.put("address", address) ;
//
//			for(int i=0; i<len; i++) {
//				String areaId = areas[i].split("\\|")[0] ;
//				String dlrId = areas[i].split("\\|")[1] ;
//
//				map.put("areaId", areaId) ;
//				map.put("dealerId", dlrId) ;
//
//
//				if(!CommonUtils.isNullList(list)) {
//					flagStr = "error" ;
//
//					if(areaStr.length() == 0) {
//						areaStr.append(list.get(0).get("AREA_NAME").toString()) ;
//					} else {
//						areaStr.append(",").append(list.get(0).get("AREA_NAME").toString()) ;
//					}
//				}
//			}
//		}
//
//		if(!CommonUtils.isNullString(addressId)) {
//			Map<String, String> theMap = dao.getAddressInfo(addressId) ;
//
//			Map<String, String> map = new HashMap<String, String>() ;
//
//			map.put("addressId", addressId) ;
//			map.put("address", address) ;
//			map.put("areaId", theMap.get("THE_AREA_ID")) ;
//			map.put("dealerId", theMap.get("DEALER_ID")) ;
//
//			List<Map<String, Object>> list = dao.chkSame(map) ;
//
//			if(!CommonUtils.isNullList(list)) {
//				flagStr = "error" ;
//
//				if(areaStr.length() == 0) {
//					areaStr.append(list.get(0).get("AREA_NAME").toString()) ;
//				} else {
//					areaStr.append(",").append(list.get(0).get("AREA_NAME").toString()) ;
//				}
//			}
//		}

		act.setOutData("flagStr", flagStr) ;
	}

	public void insertAction(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;

			String flag = CommonUtils.checkNull(request.getParamValue("flag"));				//flag: 1=保存，2=提交
//			String addressId = CommonUtils.checkNull(request.getParamValue("addressId"));	//地址id
//			String addCode = CommonUtils.checkNull(request.getParamValue("addCode"));
			String address = CommonUtils.checkNull(request.getParamValue("address")).trim();
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));
			String mobilePhone = CommonUtils.checkNull(request.getParamValue("mobilePhone"));
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String receiveOrg = CommonUtils.checkNull(request.getParamValue("receiveOrg"));
			// String dealerId = CommonUtils.checkNull(request.getParamValue("dlrList"));
			String province = CommonUtils.checkNull(request.getParamValue("province"));
			String city = CommonUtils.checkNull(request.getParamValue("city"));
			String area = CommonUtils.checkNull(request.getParamValue("area"));
//			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String addressUse = CommonUtils.checkNull(request.getParamValue("addressUse"));
			String limitType = CommonUtils.checkNull(request.getParamValue("limit"));
			String limitStartDate = CommonUtils.checkNull(request.getParamValue("limitStartDate"));
			String limitEndDate = CommonUtils.checkNull(request.getParamValue("limitEndDate"));
			String isCrossing = CommonUtils.checkNull(request.getParamValue("isCrossing"));
			String[] areas = request.getParamValues("addressAreas") ;

			address = getAddDet(province, city, area) + address ;

			TmVsAddressPO addressPO = new TmVsAddressPO();
			addressPO.setId(Long.parseLong(SequenceManager.getSequence("")));
			addressPO.setAddCode(getAddressCode(logonUser.getDealerId()));
			addressPO.setDealerId(Long.valueOf(logonUser.getDealerId()));
			addressPO.setAddress(address.trim());  //地址
			addressPO.setLinkMan(linkMan.trim());//联系人
			addressPO.setTel(tel.trim());//电话
			addressPO.setMobilePhone(mobilePhone.trim());// 手机
			addressPO.setReceiveOrg(receiveOrg.trim());//收车单位
			if (!"".equals(isCrossing)) {
				addressPO.setIsCrossing(Integer.parseInt(isCrossing));
			}
	
			if (!"".equals(remark)) {
				addressPO.setRemark(remark);
			}
			if (!"".equals(province)) {
				addressPO.setProvinceId(Long.parseLong(province));
			}
	
			if (!"".equals(city)) {
				addressPO.setCityId(Long.parseLong(city));
			}
	
			if (!"".equals(area)) {
				addressPO.setAreaId(Long.parseLong(area));
			}
	
			if(!CommonUtils.isNullString(addressUse)) {
				addressPO.setAddressUse(addressUse) ;
			}
			
			if(!CommonUtils.isNullString(limitType)) {
				addressPO.setLimitType(Long.parseLong(limitType)) ;

				if(Constant.ADDRESS_TIME_LIMIT_TEMP.toString().equals(limitType)) {
					addressPO.setStartTime(sdf.parse(limitStartDate)) ;
					addressPO.setEndTime(sdf.parse(limitEndDate)) ;
				}
			}
			addressPO.setCreateBy(logonUser.getUserId());
			addressPO.setCreateDate(new Date());
		if ("1".equals(flag)) {
			addressPO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_01);

		}else{
			addressPO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_02);
		}

		dao.insert(addressPO);
//				if(areas != null) {
//					int len = areas.length ;
//
//					for(int i=0; i<len; i++) {
//						addressPO.setId(Long.parseLong(SequenceManager.getSequence("")));
//
//						String areaId = areas[i].split("\\|")[0] ;
//						String dlrId = areas[i].split("\\|")[1] ;
//				//		String dlrId=logonUser.getDealerId();
//						addressPO.setAddCode(getAddressCode(dlrId));
//						addressPO.setAddress(address.trim());
//						addressPO.setLinkMan(linkMan.trim());
//						addressPO.setTel(tel.trim());
//
//						if (!"".equals(isCrossing)) {
//							addressPO.setIsCrossing(Integer.parseInt(isCrossing));
//						}
//
//						if (!"".equals(remark)) {
//							addressPO.setRemark(remark);
//						}
//
//						addressPO.setReceiveOrg(receiveOrg.trim());
//
//						if (!"".equals(dlrId)) {
//							addressPO.setDealerId(Long.parseLong(dlrId));
//						}
//
//						if (!"".equals(province)) {
//							addressPO.setProvinceId(Long.parseLong(province));
//						}
//
//						if (!"".equals(city)) {
//							addressPO.setCityId(Long.parseLong(city));
//						}
//
//						if (!"".equals(area)) {
//							addressPO.setAreaId(Long.parseLong(area));
//						}
//
//						if(!CommonUtils.isNullString(addressUse)) {
//							addressPO.setAddressUse(addressUse) ;
//						}
//
//						addressPO.setBAreaId(Long.parseLong(areaId)) ;
//
//						if(!CommonUtils.isNullString(limitType)) {
//							addressPO.setLimitType(Long.parseLong(limitType)) ;
//
//							if(Constant.ADDRESS_TIME_LIMIT_TEMP.toString().equals(limitType)) {
//								addressPO.setStartTime(sdf.parse(limitStartDate)) ;
//								addressPO.setEndTime(sdf.parse(limitEndDate)) ;
//							}
//						}
//
//						addressPO.setCreateBy(logonUser.getUserId());
//						addressPO.setCreateDate(new Date());
//
//						if ("1".equals(flag)) {
//							addressPO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_01);
//
//						}else{
//							addressPO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_02);
//						}
//
//						dao.insert(addressPO);
//					}
//				}
			act.setOutData("returnValue", 1);
			//act.setForword(addressAddApplyInitDoUrl);
		} catch (Exception e) {
			act.setOutData("returnValue", 2);
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新增地址申请：修改或提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void updateAction() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));				//flag: 1=保存，2=提交
			String addressId = CommonUtils.checkNull(request.getParamValue("addressId"));	//地址id
			String address = CommonUtils.checkNull(request.getParamValue("address")).trim();
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));
			String mobilePhone = CommonUtils.checkNull(request.getParamValue("mobilePhone"));
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String receiveOrg = CommonUtils.checkNull(request.getParamValue("myaddress"));

			// 解决调用界面收货单位控件名称不一致的问题
			if(CommonUtils.isNullString(receiveOrg)) {
				receiveOrg = CommonUtils.checkNull(request.getParamValue("receiveOrg"));
			}

			String province = CommonUtils.checkNull(request.getParamValue("province"));
			String city = CommonUtils.checkNull(request.getParamValue("city"));
			String area = CommonUtils.checkNull(request.getParamValue("area"));
			String addressUse = CommonUtils.checkNull(request.getParamValue("addressUse"));
			String limitType = CommonUtils.checkNull(request.getParamValue("limit"));
			String limitStartDate = CommonUtils.checkNull(request.getParamValue("limitStartDate"));
			String limitEndDate = CommonUtils.checkNull(request.getParamValue("limitEndDate"));
			String isCrossing = CommonUtils.checkNull(request.getParamValue("isCrossing"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String[] areas = request.getParamValues("addressAreas") ;

			address = getAddDet(province, city, area) + address ;

			if(!CommonUtils.isNullString(addressId)) {
				Map<String, String> map = new HashMap<String, String>() ;

				map.put("addressId", addressId) ;
				map.put("address", address) ;
				map.put("receiveOrg", receiveOrg) ;
				map.put("addressUse", addressUse) ;
				map.put("limitType", limitType) ;
				map.put("limitStartDate", limitStartDate) ;
				map.put("limitEndDate", limitEndDate) ;
				map.put("province", province) ;
				map.put("city", city) ;
				map.put("area", area) ;

				TmVsAddressPO oldAddress = new TmVsAddressPO() ;
				oldAddress.setId(Long.parseLong(addressId)) ;

				TmVsAddressPO newAddress = new TmVsAddressPO() ;

				if(!chkAddressUpdate(map)) {
					newAddress.setProvinceId(Long.parseLong(province)) ;
					newAddress.setCityId(Long.parseLong(city)) ;
					newAddress.setAreaId(Long.parseLong(area)) ;
					newAddress.setAddress(address) ;
					newAddress.setReceiveOrg(receiveOrg) ;
					newAddress.setLimitType(Long.parseLong(limitType)) ;

					if ("1".equals(flag)) {
						newAddress.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_01);
					} else if(!"1".equals(flag)) {
						newAddress.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_02);
					}

					if(Constant.ADDRESS_TIME_LIMIT_TEMP.toString().equals(limitType)) {
						newAddress.setStartTime(sdf.parse(limitStartDate)) ;
						newAddress.setEndTime(sdf.parse(limitEndDate)) ;
					}
				} else {
					if("1".equals(flag) && !Constant.STATUS_ENABLE.toString().equals(status)) {
						newAddress.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_01);
					}

					if(!"1".equals(flag) && !Constant.STATUS_ENABLE.toString().equals(status)) {
						newAddress.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_02);
					}
				}

				newAddress.setAddressUse(addressUse) ;
				if(!"".equals(isCrossing)){
					newAddress.setIsCrossing(Integer.parseInt(isCrossing));
				}	
				newAddress.setLinkMan(linkMan) ;
				newAddress.setTel(tel) ;
				newAddress.setMobilePhone(mobilePhone);
				newAddress.setRemark(remark) ;
				newAddress.setUpdateDate(new Date(System.currentTimeMillis())) ;
				newAddress.setUpdateBy(logonUser.getUserId()) ;

				dao.update(oldAddress, newAddress) ;
			}

//			if (areas != null) {
//				TmVsAddressPO addressPO = new TmVsAddressPO();
//				int len = areas.length;
//
//				for (int i = 0; i < len; i++) {
//					addressPO.setId(Long.parseLong(SequenceManager.getSequence("")));
//
//					String areaId = areas[i].split("\\|")[0];
//					String dlrId = areas[i].split("\\|")[1];
//
//					addressPO.setAddCode(getAddressCode(dlrId));
//					addressPO.setAddress(address.trim());
//					addressPO.setLinkMan(linkMan.trim());
//					addressPO.setTel(tel.trim());
//
//					if (!"".equals(isCrossing)) {
//						addressPO.setIsCrossing(Integer.parseInt(isCrossing));
//					}
//
//					if (!"".equals(remark)) {
//						addressPO.setRemark(remark);
//					}
//
//					addressPO.setReceiveOrg(receiveOrg.trim());
//
//					if (!"".equals(dlrId)) {
//						addressPO.setDealerId(Long.parseLong(dlrId));
//					}
//
//					if (!"".equals(province)) {
//						addressPO.setProvinceId(Long.parseLong(province));
//					}
//
//					if (!"".equals(city)) {
//						addressPO.setCityId(Long.parseLong(city));
//					}
//
//					if (!"".equals(area)) {
//						addressPO.setAreaId(Long.parseLong(area));
//					}
//
//					if (!CommonUtils.isNullString(addressUse)) {
//						addressPO.setAddressUse(addressUse);
//					}
//
//					addressPO.setBAreaId(Long.parseLong(areaId));
//
//					if (!CommonUtils.isNullString(limitType)) {
//						addressPO.setLimitType(Long.parseLong(limitType));
//
//						if (Constant.ADDRESS_TIME_LIMIT_TEMP.toString().equals(limitType)) {
//							addressPO.setStartTime(sdf.parse(limitStartDate));
//							addressPO.setEndTime(sdf.parse(limitEndDate));
//						}
//					}
//
//					addressPO.setCreateBy(logonUser.getUserId());
//					addressPO.setCreateDate(new Date());
//
//					if ("1".equals(flag)) {
//						addressPO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_01);
//
//					} else {
//						addressPO.setStatus(Constant.DEALER_ADDRESS_CHANGE_STATUS_02);
//					}
//
//					dao.insert(addressPO);
//				}
//			}
			act.setOutData("returnValue", 1);
			//act.setForword(addressAddApplyInitDoUrl);
		} catch (Exception e) {
			act.setOutData("returnValue", 2);
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商修改地址申请：修改或提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void cheAddress() {
		String addressId = CommonUtils.checkNull(request.getParamValue("addressId")) ;

		String flag ;

		if(chkAddOrder(addressId)) {
			flag = "success" ;

			if(chkAddReq(addressId)) {
				flag = "success" ;

				if(chkAddDlv(addressId)) {
					flag = "success" ;
				} else {
					flag = "error" ;
				}
			} else {
				flag = "error" ;
			}
		} else {
			flag = "error" ;
		}


		act.setOutData("flag", flag) ;
	}

	public boolean chkAddOrder(String addressId) {
		List<Map<String, Object>> chkList = dao.chkAddOrder(addressId) ;

		if(CommonUtils.isNullList(chkList)) {
			return true ;
		} else {
			return false ;
		}
	}

	public boolean chkAddReq(String addressId) {
		List<Map<String, Object>> chkList = dao.chkAddReq(addressId) ;

		if(CommonUtils.isNullList(chkList)) {
			return true ;
		} else {
			return false ;
		}
	}

	public boolean chkAddDlv(String addressId) {
		List<Map<String, Object>> chkList = dao.chkAddDlv(addressId) ;

		if(CommonUtils.isNullList(chkList)) {
			return true ;
		} else {
			return false ;
		}
	}
}
