/**   
* @Title: ClaimLaborBigClassDao.java 
* @Package com.infodms.dms.dao.claim.basicData 
* @Description: TODO(索赔工时大类维护DAO) 
* @author wangjinbao   
* @date 2010-7-14 下午02:03:27 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.ConditionBean;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimLaborBigClassDao 
 * @Description: TODO(索赔工时大类维护DAO) 
 * @author wangjinbao 
 * @date 2010-7-14 下午02:03:27 
 *  
 */
public class ClaimLaborBigClassDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ClaimLaborBigClassDao.class);
	private static final ClaimLaborBigClassDao dao = new ClaimLaborBigClassDao ();
	public static final ClaimLaborBigClassDao getInstance() {
		return dao;
	}
	
	/**
	 * 
	* @Title: laborBigClassQuery 
	* @Description: TODO(索赔工时大类查询) 
	* @param @param pageSize
	* @param @param curPage
	* @param @param bean
	* @param @return
	* @param @throws Exception   
	* @return PageResult<Map<String,Object>>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> laborBigClassQuery(int pageSize, int curPage, ConditionBean bean,Long oemCompanyId) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select t.id,\n" );
		sql.append("       t.labour_code,\n" );
		sql.append("       t.cn_des,\n" );
		sql.append("       p.cn_des as p_cn_des,\n" );
		sql.append("       p.labour_code as p_labour_code,\n" );
		sql.append("       t.tree_code,\n" );
		sql.append("       t.pater_id,\n" );
		sql.append("       t.is_del\n" );
		sql.append("  from TT_AS_WR_WRLABINFO t\n" );
		sql.append("  left outer join TT_AS_WR_WRLABINFO p on (t.pater_id = p.id)\n" );
		sql.append(" where t.tree_code in (1, 2)  \n" );
		sql.append("   and t.is_del = 0\n" );

		if(Utility.testString(bean.getConOne())){//工时代码不为空
			sql.append(" and upper(t.labour_code) like '%"+bean.getConOne().toUpperCase()+"%'\n");
		}		
		if(Utility.testString(bean.getConTwo())){//工时大类名称不为空
			sql.append(" and t.cn_des like '%"+bean.getConTwo().toUpperCase()+"%'\n");
		}
		sql.append(" order by t.id desc ");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	/**
	 * 
	* @Title: getPaterClassByCode 
	* @Description: TODO(根据CODE选择索赔工时大类) 
	* @param @param code
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getPaterClassByCode(String code){
		StringBuffer sql= new StringBuffer();
		sql.append(" select t.*\n" );
		sql.append("  from TT_AS_WR_WRLABINFO t\n" );
		sql.append(" where t.tree_code in (1, 2)\n" );
		sql.append("   and t.is_del = 0\n" );
		sql.append("   and t.labour_code = '"+code+"'\n" );
		sql.append(" order by t.id desc ");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	/**
	 * 
	* @Title: getLaborBigClassById 
	* @Description: TODO(根据ID查询索赔工时大类) 
	* @param @param id
	* @param @return   
	* @return HashMap  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public HashMap getLaborBigClassById(Long id){
		HashMap hm = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select t.id,\n" );
		sql.append("       t.labour_code,\n" );
		sql.append("       t.cn_des,\n" );
		sql.append("       p.cn_des as p_cn_des,\n" );
		sql.append("       p.labour_code as p_labour_code,\n" );
		sql.append("       t.tree_code,\n" );
		sql.append("       t.pater_id,\n" );
		sql.append("       t.remark,\n" );
		sql.append("       t.is_del\n" );
		sql.append("  from TT_AS_WR_WRLABINFO t\n" );
		sql.append("  left outer join TT_AS_WR_WRLABINFO p on (t.pater_id = p.id)\n" );
		sql.append(" where t.tree_code in (1, 2)\n" );
		sql.append("   and t.is_del = 0\n");
		sql.append("   and t.id = "+id+"\n" );
		sql.append(" order by t.id desc");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		if(list != null && list.size() > 0){
			hm = (HashMap)list.get(0);
		}
		return hm;
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
