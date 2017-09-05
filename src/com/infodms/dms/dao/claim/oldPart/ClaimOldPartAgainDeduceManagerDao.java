package com.infodms.dms.dao.claim.oldPart;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.ClaimOldPartDeduceListBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * 类说明：索赔旧件二次抵扣对应的DAO
 * 作者：  赵伦达
 */
@SuppressWarnings("unchecked")
public class ClaimOldPartAgainDeduceManagerDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ClaimOldPartAgainDeduceManagerDao.class);
	private static final ClaimOldPartAgainDeduceManagerDao dao = null;
	
	public static final ClaimOldPartAgainDeduceManagerDao getInstance() {
	   if(dao==null) return new ClaimOldPartAgainDeduceManagerDao();
	   return dao;
	}
    /**
     * Function：索赔旧件二次抵扣--通过条件查询
     * @param  ：	
     * @return:		@param params
     * @return:		@param curPage
     * @return:		@param pageSize
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-6-22
     */
	public PageResult<ClaimOldPartDeduceListBean> getAgainDeduceByConditionList(Map params,int curPage, int pageSize){
		
		return null;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
}
