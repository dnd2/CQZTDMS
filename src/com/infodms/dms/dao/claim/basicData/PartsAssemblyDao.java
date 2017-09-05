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
 * $Id: PartsAssemblyDao.java,v 1.1 2013/07/07 09:43:14 wanghx Exp $
 */
package com.infodms.dms.dao.claim.basicData;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsRepairOrderExtPO;
import com.infodms.dms.po.TtAsWrPartsAssemblyPO;
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
public class PartsAssemblyDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ClaimRuleDao.class);
	private static final PartsAssemblyDao dao = new PartsAssemblyDao ();
	public  static final PartsAssemblyDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public PageResult<TtAsWrPartsAssemblyPO> getPartsAssemblyView(String code,String name,String status,Integer pageSize,Integer  curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from Tt_As_Wr_Parts_Assembly a where 1=1");
		if(code!=null&&!code.equals("")){
			sql.append("and a.parts_assembly_code LIKE '%"+code+"%'\n" );
		}
		if(name!=null&&!name.equals("")){
			sql.append("and a.parts_assembly_name LIKE '%"+name+"%'\n" );
		}
		if(status!=null&&!status.equals("")){
			sql.append("and a.status="+status+"\n" );
		}
		return pageQuery(TtAsWrPartsAssemblyPO.class, sql.toString(), null,
				pageSize, curPage);
	}
	
	public void delPartsAssembly(String ids){
		
		
	}
}