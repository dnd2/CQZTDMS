/**   
* @Title: LevelDao.java 
* @Package com.infodms.dms.dao.claim.authorization 
* @Description: TODO(授权级别维护DAO) 
* @author wangjinbao   
* @date 2010-6-8 上午09:12:55 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.authorization;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrAuthinfoPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: LevelDao 
 * @Description: TODO(授权级别维护DAO) 
 * @author wangjinbao 
 * @date 2010-6-8 上午09:12:55 
 *  
 */
public class LevelDao extends BaseDao {
	public static Logger logger = Logger.getLogger(LevelDao.class);
	private static final LevelDao dao = new LevelDao ();
	public static final LevelDao getInstance() {
		return dao;
	}
	
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> levelQuery(Long oemCompanyId,int pageSize, int curPage,String type) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sb = new StringBuffer();
		sb.append(" select tawa.approval_level_code,tawa.approval_level_name,tawa.approval_level_tier from tt_as_wr_authinfo tawa  ");
		sb.append(" where tawa.approval_level_code<>100 \n");
		sb.append(" and tawa.type = ").append(type).append("\n");
		sb.append(" order by to_number(tawa.approval_level_code) ");
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	
	/**
	 * 
	* @Title: updateLevel 
	* @Description: TODO(授权级别维护修改) 
	* @param @param selpo
	* @param @param updatepo   
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void updateLevel(TtAsWrAuthinfoPO selpo,TtAsWrAuthinfoPO updatepo){
		dao.update(selpo, updatepo);
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
