/**   
* @Title: AccPriceMainDao.java 
* @Package com.infodms.dms.dao.claim.basicData 
* @Description: TODO(索赔工时维护DAO) 
* @author Administrator   
* @date 2010-6-1 下午03:02:44 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrWrlabinfoPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: AccPriceMainDao 
 * @Description: TODO(索赔工时维护DAO) 
 * @author Administrator 
 * @date 2010-6-1 下午03:02:44 
 *  
 */
public class AccPriceMainDao extends BaseDao {
	public static Logger logger = Logger.getLogger(AccPriceMainDao.class);
	private static final AccPriceMainDao dao = new AccPriceMainDao ();
	public static final AccPriceMainDao getInstance() {
		return dao;
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> laborPriceQuery(int pageSize, int curPage,String workhour_code, String workhour_name) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT C.ID,C.WORKHOUR_CODE,C.WORKHOUR_NAME,C.PRICE,C.ADD_TIME,C.UPDATE_TIME,C.STATUS, ");
		sb.append(" (select NAME from TC_USER where USER_ID=C.ADD_BY) ADD_BY ,");
		sb.append(" (select NAME from TC_USER where USER_ID=C.UPDATE_BY) UPDATE_BY ");
		sb.append(" FROM TT_ACCESSORY_PRICE_MAINTAIN C  WHERE 1=1");
		
		//条件
		if (Utility.testString(workhour_code)) {
			sb.append(" AND C.WORKHOUR_CODE = '"+workhour_code+"' ");
		}
		if (Utility.testString(workhour_name)) {
			sb.append(" AND C.WORKHOUR_NAME LIKE '%"+workhour_name+"%' ");
		}
		
		sb.append(" ORDER BY C.ADD_TIME DESC");
		PageResult<Map<String, Object>> result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
