package com.infodms.dms.dao.relation;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class ProductComboRelationDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(ProductComboRelationDAO.class);
	private ProductComboRelationDAO() {
		
	}
	
	private static class ProductComboDAOSingleton {
		private static ProductComboRelationDAO dao = new ProductComboRelationDAO() ;
	}
	
	public static ProductComboRelationDAO getInstance() {
		return ProductComboDAOSingleton.dao ;
	}
	
	public List<Map<String, Object>> getProductListByCompany(String companyId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TAWPP.PRODUCT_ID, TAWPP.PACKAGE_CODE, TAWPP.PACKAGE_NAME\n");
		sql.append("  FROM TT_PRODUCT_DISTRIBUTION TPD, TT_AS_WR_PRODUCT_PACKAGE TAWPP\n");  
		sql.append(" WHERE TPD.PRODUCT_ID = TAWPP.PRODUCT_ID\n");
		sql.append("   and tpd.company_id in (").append(companyId).append(")\n");
		sql.append("   and tawpp.status = ").append(Constant.STATUS_ENABLE).append("\n");

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
