/**   
* @Title: ClaimOtherFeeDao.java 
* @Package com.infodms.dms.dao.claim.basicData 
* @Description: TODO(索赔其它费用维护DAO) 
* @author wangjinbao   
* @date 2010-5-31 下午04:05:34 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrOtherfeePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimOtherFeeDao 
 * @Description: TODO(索赔其它费用维护DAO) 
 * @author wangjinbao 
 * @date 2010-5-31 下午04:05:34 
 *  
 */
public class ClaimOtherFeeDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ClaimOtherFeeDao.class);
	private static final ClaimOtherFeeDao dao = new ClaimOtherFeeDao ();
	public static final ClaimOtherFeeDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: claimOtherFeeQuery 
	* @Description: TODO(索赔其它费用维护查询) 
	* @param @param pageSize
	* @param @param curPage
	* @param @return
	* @param @throws Exception   
	* @return PageResult<Map<String,Object>>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> claimOtherFeeQuery(int pageSize, int curPage, String whereSql,List<Object> params,Long oemCompanyId) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sb = new StringBuffer();
		sb.append(" select tawo.fee_id,tawo.fee_code,tawo.fee_name,tawo.create_by from TT_AS_WR_OTHERFEE tawo ");
		sb.append(" where tawo.is_del=0    ");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}		
		sb.append(" order by tawo.fee_id desc ");//是否排序
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), params, getFunName(), pageSize, curPage);
		return result;
	}
	/**
	 * 
	* @Title: insertClaimOtherFee 
	* @Description: TODO(索赔其它费用维护增加) 
	* @param @param po   
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void insertClaimOtherFee(TtAsWrOtherfeePO po){
		dao.insert(po);
	}
	/**
	 * 
	* @Title: updateClaimOtherFee 
	* @Description: TODO(索赔其它费用维护修改) 
	* @param @param selpo
	* @param @param updatepo   
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void updateClaimOtherFee(TtAsWrOtherfeePO selpo,TtAsWrOtherfeePO updatepo){
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
