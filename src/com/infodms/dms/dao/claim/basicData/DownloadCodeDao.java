/**   
* @Title: DownloadCodeDao.java 
* @Package com.infodms.dms.dao.claim.basicData 
* @Description: 下发代码维护DAO 
* @author wangjinbao   
* @date 2010-5-31 下午09:54:10 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmBusinessChngCodePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: DownloadCodeDao 
 * @Description: 下发代码维护DAO 
 * @author wangjinbao 
 * @date 2010-5-31 下午09:54:10 
 *  
 */
public class DownloadCodeDao extends BaseDao {
	public static Logger logger = Logger.getLogger(DownloadCodeDao.class);
	private static final DownloadCodeDao dao = new DownloadCodeDao ();
	public static final DownloadCodeDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: downloadCodeQuery 
	* @Description: 下发代码维护查询
	* @param  pageSize           ：每页显示条数
	* @param  curPage            ：当前页数
	* @param  whereSql           ：查询条件
	* @param  params             ：查询条件对应的参数
	* @param @throws Exception    设定文件 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> downloadCodeQuery(int pageSize, int curPage,Long companyId, String whereSql,List<Object> params) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sb = new StringBuffer();
		sb.append(" select tbcc.type_code,tbcc.code,tbcc.code_name,tbcc.business_code_id ");
		sb.append(" from tm_business_chng_code tbcc where tbcc.is_del = 0    ");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		sb.append(" order by tbcc.business_code_id desc ");
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), params, getFunName(), pageSize, curPage);
		return result;
	}
	/**
	 * 
	* @Title: insertDownloadCode 
	* @Description: TODO(下发代码维护新增) 
	* @param @param po   
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void insertDownloadCode(TmBusinessChngCodePO po){
		dao.insert(po);
	}
	/**
	 * 
	* @Title: updateDownloadCode 
	* @Description: TODO 下发代码维护修改
	* @param  selpo      ：  查询条件PO
	* @param  updatepo   ：  修改后的PO 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void updateDownloadCode(TmBusinessChngCodePO selpo,TmBusinessChngCodePO updatepo){
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
