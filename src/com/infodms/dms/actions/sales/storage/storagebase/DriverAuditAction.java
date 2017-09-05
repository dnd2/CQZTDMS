package com.infodms.dms.actions.sales.storage.storagebase;

import java.util.List;
import java.util.Map;


import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.service.DriverAuditService;
import com.infodms.dms.service.impl.DriverAuditServiceImpl;
import com.infodms.dms.service.ontheway.OnTheWayService;
import com.infodms.dms.service.ontheway.OnTheWayServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.po3.bean.PageResult;


public class DriverAuditAction extends BaseAction{
	/**
	 * 司机审核列表
	 */
	private static final String DRIVER_AUDIT_LIST_URL = "/jsp/sales/storage/storagebase/driverAudit/driver_audit_list.jsp";
	/**
	 * 审核页面
	 */
	private static final String DRIVER_AUDIT_PAGE_URL = "/jsp/sales/storage/storagebase/driverAudit/driver_audit_page.jsp";

	/**
	 * 司机审核列表
	 */
	public void driverAuditList() {
		String type = DaoFactory.getParam(request, "query");
		if ("query".equals(type)) {
			DriverAuditService service  = new DriverAuditServiceImpl();
			PageResult<Map<String, Object>> list = service.getdriverAuditList(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}else {
			OnTheWayService onTheWayService = new OnTheWayServiceImpl();
			TcPosePO tcPost = onTheWayService.getTcPostByPostId(loginUser);
			if(null!=tcPost&&null!=tcPost.getLogiId()&&0!=tcPost.getLogiId()){
				act.setOutData("logi", tcPost.getLogiId());
			}else{
				act.setOutData("logi", null);
			}
			List<Map<String, Object>>  list_logi = onTheWayService.getLogiName();
			act.setOutData("list_logi", list_logi);
			act.setForword(DRIVER_AUDIT_LIST_URL);
		}
	}
	/**
	 * 司机审核页面
	 */
	public void driverAuditPage() {
		DriverAuditService service  = new DriverAuditServiceImpl();
		try {
			Map<String, Object> map = service.getdriverAuditByDriverId(request,loginUser);
			act.setOutData("map", map);
			act.setForword(DRIVER_AUDIT_PAGE_URL);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.PUTIN_FAILURE_CODE, "审核页面加载失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 司机审核(单条审核)
	 */
	public void driverAudit() {
		DriverAuditService service  = new DriverAuditServiceImpl();
		try {
			String res =  service.driverAudit(request,loginUser);
			act.setOutData("reslut", res);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.PUTIN_FAILURE_CODE, "审核失败");
			logger.error(loginUser, e1);
			//act.setException(e1);
			act.setOutData("reslut", e.getMessage());
		}
	}
	/**
	 * 批量审核
	 */
	public void driverAuditAll() {
		DriverAuditService service  = new DriverAuditServiceImpl();
		try {
			String res = service.driverAuditAll(request,loginUser);
			act.setOutData("reslut", res);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.PUTIN_FAILURE_CODE, "批量审核失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
}
