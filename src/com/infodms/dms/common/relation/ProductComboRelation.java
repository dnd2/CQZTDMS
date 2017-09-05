package com.infodms.dms.common.relation;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.relation.ProductComboRelationDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;


public class ProductComboRelation {
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	public Logger logger = Logger.getLogger(ProductComboRelation.class);
	ProductComboRelationDAO dao = ProductComboRelationDAO.getInstance() ;
	
	public void getProductStrByCompanyId() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String companyId = logonUser.getCompanyId().toString() ;
			
			List<Map<String, Object>> productList = dao.getProductListByCompany(companyId) ;
			
			StringBuffer productId = new StringBuffer("") ;
			StringBuffer productName = new StringBuffer("") ;
			StringBuffer productCode = new StringBuffer("") ;
			
			if(!CommonUtils.isNullList(productList)) {
				int length = productList.size() ;
				
				for(int i=0; i<length; i++) {
					if(productId.toString().length() == 0) {
						productId.append(productList.get(i).get("PRODUCT_ID").toString()) ;
						productName.append(productList.get(i).get("PACKAGE_NAME").toString()) ;
						productCode.append(productList.get(i).get("PACKAGE_CODE").toString()) ;
					} else {
						productId.append(",").append(productList.get(i).get("PRODUCT_ID").toString()) ;
						productName.append(",").append(productList.get(i).get("PACKAGE_NAME").toString()) ;
						productCode.append(",").append(productList.get(i).get("PACKAGE_CODE").toString()) ;
					}
				}
			}
			System.out.println("5555555555555555555555555" + productId.toString());
			act.setOutData("productId", productId.toString()) ;
			act.setOutData("productName", productName.toString()) ;
			act.setOutData("productCode", productCode.toString()) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
