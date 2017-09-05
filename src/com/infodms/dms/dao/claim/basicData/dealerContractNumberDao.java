/**********************************************************************
* <pre>
* FILE : ClaimRuleDao.java
* CLASS : ClaimRuleDao
*
* AUTHOR : PGM
*
* FUNCTION : 三包规则维护.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-07-14| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: dealerContractNumberDao.java,v 1.1 2012/08/15 07:27:26 xiongc Exp $
 */
package com.infodms.dms.dao.claim.basicData;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.po.TtAsWrRuleListTmpPO;
import com.infodms.dms.po.TtAsWrRulePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  三包规则维护 
 * @author        :  PGM
 * CreateDate     :  2010-07-14
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class dealerContractNumberDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ClaimRuleDao.class);
	private static final dealerContractNumberDao dao = new dealerContractNumberDao ();
	public  static final dealerContractNumberDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public PageResult<Map<String,Object>> getDealerContractNumberQuery(String id,String name,String yieldly){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from Tt_As_Wr_Contract c where 1=1");
		if(id!=null&&!id.equals("")){
			sql.append("and c.dealer_id in ("+id+")\n" );
		}
		if(name!=null&&!name.equals("")){
			sql.append("and c.dealer_name like '%%'");
		}
		if(yieldly!=null&&!yieldly.equals("")){
			sql.append("and c.yieldly\n" );
		}
		
		

		return null;
	}
}