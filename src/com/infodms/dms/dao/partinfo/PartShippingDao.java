package com.infodms.dms.dao.partinfo;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
/**
 * 
* @ClassName: PartClaimApply 
* @Description: TODO(配件货运表) 
* @author liuqiang 
* @date Jun 13, 2010 10:34:15 AM 
*
 */
public class PartShippingDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(PartShippingDao.class);
	private static final PartShippingDao dao = new PartShippingDao();
	
	public static final PartShippingDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 
	* @Title: getOrderInfo 
	* @Description: TODO(根据货运单号取货运单信息) 
	 */
	public Map<String, Object> getShippingInfo(String doNo, String orderNo) {
		StringBuffer sql = new StringBuffer(); 
		sql.append("SELECT A.DO_NO, A.ORDER_NO, TO_CHAR(A.SIGN_DATE, 'YYYY-MM-DD')SIGN_DATE, B.SIGN_NO, C.ORDER_ID \n");
		sql.append(" FROM TT_PT_SHIPPINGSHEET A, TT_PT_DLR_SIGN B, TT_PT_ORDER C \n");
		sql.append(" WHERE A.DO_NO = '").append(doNo).append("'");
		sql.append(" AND A.DO_NO = B.DO_NO \n");
		sql.append(" AND A.ORDER_NO = '").append(orderNo).append("'");
		sql.append(" AND A.ORDER_NO = C.ORDER_NO");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	/**
	 * 
	* @Title: getShippingInfo 
	* @Description: TODO(根据货运单号取采购单信息) 
	* @param @param doNo
	 */
	public Map<String, Object> getOrderInfoByDo(String doNo) {
		StringBuffer sql = new StringBuffer(); 
		sql.append("SELECT D.ORDER_ID, C.ORDER_NO FROM TT_PT_ORDER D,");
		sql.append(" (SELECT B.ORDER_NO FROM TT_PT_SHIPPINGSHEET A, TT_PT_SALES B where A.SO_NO = B.SO_NO AND A.DO_NO = '");
		sql.append(doNo);
		sql.append("') C WHERE D.ORDER_NO = C.ORDER_NO");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
}
