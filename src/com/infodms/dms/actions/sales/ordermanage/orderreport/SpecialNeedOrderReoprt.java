/**
 * 订做车订单提报
 * */
package com.infodms.dms.actions.sales.ordermanage.orderreport;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.SpecialNeedOrderReoprtDAO;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SpecialNeedOrderReoprt extends BaseDao{

	public Logger logger = Logger.getLogger(SpecialNeedOrderReoprt.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final SpecialNeedOrderReoprtDAO dao = new SpecialNeedOrderReoprtDAO ();
	public static final SpecialNeedOrderReoprtDAO getInstance() {
		return dao;
	}
	
	private final String specialNeedOrderReoprtInitURL = "/jsp/sales/ordermanage/orderreport/specialNeedOrderReoprtInit.jsp";
	/**
	 * FUNCTION		:	订做车订单提报：页面初始化
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-23
	 */
	public void specialNeedOrderReoprtInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(specialNeedOrderReoprtInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询可提报的订做车需求列表
	 * */
	public void getCanReportSpecialList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			
			String startDate = request.getParamValue("startDate");		//起始时间
			String endDate = request.getParamValue("endDate");			//结束时间
			String dealerId = logonUser.getDealerId() ;
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = SpecialNeedOrderReoprtDAO.queryCanReportSpecialList(dealerId, startDate,endDate,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "查询可提报的订做车需求列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 订做车需求新增页面物料组查询
	 */
	public void addMaterial() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String materialCode = request.getParamValue("materialCode");		//物料CODE
			String reqId = request.getParamValue("reqId");		//需求ID
			String addedMaterialId = request.getParamValue("addedMaterialId"); //已添加配置ID
			Long companyId = logonUser.getCompanyId();
			List<Map<String, Object>> list = dao.getMaterialGroupInfo(materialCode, reqId, companyId, addedMaterialId);
			act.setOutData("info", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "订做车需求新增页面物料查询");
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
