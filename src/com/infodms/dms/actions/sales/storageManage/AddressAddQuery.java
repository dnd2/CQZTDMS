package com.infodms.dms.actions.sales.storageManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.storageManage.AddressAddQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class AddressAddQuery {
	public Logger logger = Logger.getLogger(AddressAddQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final AddressAddQueryDAO dao = new AddressAddQueryDAO ();
	public static final AddressAddQueryDAO getInstance() {
		return dao;
	}
	private final String  addressAddQueryInitUrl = "/jsp/sales/storageManage/addressAddQueryInit.jsp";
	private final String  OEM_ADDRESS_INIT_URL = "/jsp/sales/storageManage/oemAddressQuery.jsp";
	
	
	public void oemAddressQueryInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String orgId = logonUser.getOrgId().toString() ;
			
			Long poseId = logonUser.getPoseId();
			StringBuffer areaIdStr = new StringBuffer() ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			
			if(!CommonUtils.isNullList(areaList)) {
				int len = areaList.size() ;
				
				for(int i=0; i<len; i++) {
					if(areaIdStr.length() == 0) {
						areaIdStr.append(areaList.get(i).get("AREA_ID")) ;
					} else {
						areaIdStr.append(",").append(areaList.get(i).get("AREA_ID")) ;
					}
				}
			}
			
			act.setOutData("areaIdStr", areaIdStr.toString()) ;
			act.setOutData("orgId", orgId) ;
			act.setOutData("areaList", areaList);
			
			act.setForword(OEM_ADDRESS_INIT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "地址查询：页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	public void addressAddQueryInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
			
			int isFlag ;
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				isFlag = 1 ;
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				isFlag = 0 ;
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			
			act.setOutData("isFlag", isFlag) ;
			
			act.setOutData("areaList", areaList);
			act.setForword(addressAddQueryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址更改申请查询：页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	/**
	 * 车厂地址查询
	 * */
	public void oemAddressQuery(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String dutyType = logonUser.getDutyType() ;
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String areaIdStr = CommonUtils.checkNull(request.getParamValue("areaIdStr"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String limit = CommonUtils.checkNull(request.getParamValue("limit"));
			String addressName = CommonUtils.checkNull(request.getParamValue("address"));
			String dealerIds = CommonUtils.checkNull(request.getParamValue("dealerIds"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			
			if(CommonUtils.isNullString(areaId)) {
				areaId = areaIdStr ;
			}
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			
			map.put("areaId", areaId) ;
			map.put("status", status) ;
			map.put("limit", limit) ;
			map.put("addressName", addressName) ;
			map.put("dealerIds", dealerIds) ;
			map.put("orgId", orgId) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.addressQuery(map, Constant.PAGE_SIZE, curPage) ;
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车厂地址查询");
			logger.error(logonUser,e1);
			act.setException(e1) ;
		}
	}
	
	/**
	 * 查询修改历史
	 * */
	public void changeList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId = logonUser.getCompanyId() ;
			Long poseId = logonUser.getPoseId() ;
			
			String dealerId = "";
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String address = CommonUtils.checkNull(request.getParamValue("address"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			DealerRelation dr = new DealerRelation() ;
			String dealerIds = dr.getDealerIdByPose(companyId, poseId) ;
			if ("".equals(areaId)) {
				dealerId = dealerIds;
			}else{
				dealerId = areaId.split("\\|")[1];
				
				areaId = areaId.split("\\|")[0];
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getChangeList(areaId, status,dealerId, address, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询修改历史");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
