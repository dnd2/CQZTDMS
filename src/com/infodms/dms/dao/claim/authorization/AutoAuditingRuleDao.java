/**   
* @Title: AutoAuditingRuleDao.java 
* @Package com.infodms.dms.dao.claim.authorization 
* @Description: TODO(索赔自动审核规则管理DAO) 
* @author wangjinbao   
* @date 2010-6-11 上午10:15:13 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.authorization;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: AutoAuditingRuleDao 
 * @Description: TODO(索赔自动审核规则管理DAO) 
 * @author wangjinbao 
 * @date 2010-6-11 上午10:15:13 
 *  
 */
@SuppressWarnings("unchecked")
public class AutoAuditingRuleDao extends BaseDao {
	public static Logger logger = Logger.getLogger(AutoAuditingRuleDao.class);
	private static final AutoAuditingRuleDao dao = new AutoAuditingRuleDao ();
	public static final AutoAuditingRuleDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: autoAuditingRuleQuery 
	* @Description: TODO(索赔自动审核规则管理查询) 
	* @param @param pageSize
	* @param @param curPage
	* @param @return
	* @param @throws Exception   
	* @return PageResult<Map<String,Object>>  
	* @throws
	 */
	public   PageResult<Map<String, Object>> autoAuditingRuleQuery(String oemCompanyId,int pageSize, int curPage) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append(" select tawca.auto_id,tawca.auto_type,tawca.status,tawca.remark,\n " );
		sql.append(" decode(tawca.status,"+Constant.STATUS_ENABLE+",'disabled',' ') as do,\n " );
		sql.append(" decode(tawca.status,"+Constant.STATUS_DISABLE+",'disabled',' ') as undo,\n " );
		sql.append(" tawca.auth_code,tawca.is_use,tawca.auth_desc\n");
		sql.append(" from tt_as_wr_claim_auto tawca\n " );
		sql.append(" where tawca.oem_company_id=").append(oemCompanyId).append("\n");
		sql.append(" and tawca.is_use = ").append(Constant.STATUS_ENABLE).append("\n");
		sql.append(" order by tawca.auto_type,tawca.auto_id ");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}

	/* (非 Javadoc) 
	 * <p>Title: wrapperPO</p> 
	 * <p>Description: </p> 
	 * @param rs
	 * @param idx
	 * @return 
	 * @see com.infodms.dms.dao.common.BaseDao#wrapperPO(java.sql.ResultSet, int) 
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成方法存根
		return null;
	}

}
