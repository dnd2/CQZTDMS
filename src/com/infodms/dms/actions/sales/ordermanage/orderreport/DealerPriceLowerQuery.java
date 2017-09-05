package com.infodms.dms.actions.sales.ordermanage.orderreport;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.DealerPriceQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class DealerPriceLowerQuery {
	public Logger logger = Logger.getLogger(DealerPriceQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final DealerPriceQueryDAO dao = DealerPriceQueryDAO.getInstance();
	
	private final String dealerPriceLowerQueryInit_URL = "/jsp/sales/ordermanage/orderreport/dealerPriceLowerQueryInit.jsp";
	
	public void dealerPriceLowerQueryInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(dealerPriceLowerQueryInit_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "下级经销商价格查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryLowerDealer(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			if ("".equals(dealerId)) {
				dealerId = logonUser.getDealerId();
			}
			List<Map<String, Object>> dealerList = dao.getLowerDealerList(dealerId);
			act.setOutData("dealerList", dealerList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "查询下级经销商");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void dealerPriceLowerQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerName"));
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.getDealerPriceList(dealerId, modelCode, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商价格查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
