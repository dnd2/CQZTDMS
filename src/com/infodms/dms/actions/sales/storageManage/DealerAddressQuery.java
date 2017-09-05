/**
 * 经销商地址查询
 * */
package com.infodms.dms.actions.sales.storageManage;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.storageManage.DealerAddressQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class DealerAddressQuery {
	public Logger logger = Logger.getLogger(DealerAddressQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final DealerAddressQueryDAO dao = new DealerAddressQueryDAO ();
	public static final DealerAddressQueryDAO getInstance() {
		return dao;
	}
	private final String dealerAddressQueryInitUrl = "/jsp/sales/storageManage/dealerAddressQueryInit.jsp";
	
	public void dealerAddressQueryInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);
			act.setForword(dealerAddressQueryInitUrl);
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
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao.getAddressList(orgId,dealerCode, address, areaId, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址更改审核：查询可审核信息列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
