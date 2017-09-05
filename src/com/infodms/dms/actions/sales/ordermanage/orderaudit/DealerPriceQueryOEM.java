package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.ordermanage.orderreport.DealerPriceQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class DealerPriceQueryOEM {
	public Logger logger = Logger.getLogger(DealerPriceQueryOEM.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final DealerPriceQueryDAO dao = DealerPriceQueryDAO.getInstance();
	
	private final String DealerPriceQueryOEMInit_URL = "/jsp/sales/ordermanage/orderaudit/dealerPriceQueryOEMInit.jsp";
	
	public void dealerPriceQueryOEMInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String dutyType=logonUser.getDutyType();
        long orgId=logonUser.getOrgId();
		try {
            act.setOutData("dutyType",dutyType);
            act.setOutData("orgId",orgId);
			act.setForword(DealerPriceQueryOEMInit_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商价格查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void dealerPriceOEMQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));
            String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));

			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			
//			Map<String,Object> dealerIdMap = dao.getDealerId(dealerCode);
//			BigDecimal dealerId = (BigDecimal) (dealerIdMap.size() != 0 ? dealerIdMap.get("DEALER_ID") : null);
			//Map<String,Object> priceIdMap = dao.getPriceId(dealerId.toString());
			//BigDecimal priceId = (BigDecimal) (priceIdMap.size() != 0 ? priceIdMap.get("PRICE_ID") : null);
			//PageResult<Map<String, Object>> ps = dao.getDealerPriceListOEM(dealerId.toString(),  modelCode,Integer.parseInt(pageSize), curPage);
			PageResult<Map<String, Object>> ps=CommonUtils.getMaterialPirceList(modelCode,dealerCode.toString(),Integer.parseInt(pageSize),Integer.parseInt(curPage.toString()));
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商价格查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
