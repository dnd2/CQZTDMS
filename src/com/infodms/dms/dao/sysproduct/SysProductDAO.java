/**********************************************************************
 * <pre>
 * FILE : SysProductDAO.java
 * CLASS : SysProductDAO
 *
 * AUTHOR : yangyong
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2009-12-14|yangyong| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.dao.sysproduct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.po.TmBrandPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;


public class SysProductDAO {
	
	public static Logger logger = Logger.getLogger(SysPositionDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	public static PageResult<TmBrandPO> brandQuery(TmBrandPO brandPO, int curPage, int pageSize, String orderName, String da){
		StringBuffer querySQL = new StringBuffer();
		querySQL.append("SELECT T.BRAND_ID,T.BRAND_NAME,T.ENGLISH_NAME,T.BRAND_CODE,T.REMARK,T.BUSINESS_TYPE,T.BILL_TOTAL,");
		querySQL.append("T.STATUS,T.CREATE_DATE,T.UPDATE_DATE,T.CREATE_BY,T.UPDATE_BY,T.OLD_MODEL_SIFT FROM TM_BRAND T ");
		querySQL.append(" WHERE 1=1 ");
		List<Object> params = new ArrayList<Object>();
		
		if(null != brandPO.getBrandId() && !"".equals(brandPO.getBrandId())){
			params.add(brandPO.getBrandId());
			querySQL.append(" AND T.BRAND_ID = ?");
		}
		if(null != brandPO.getBrandName() && !"".equals(brandPO.getBrandName())){
			querySQL.append(" AND T.BRAND_NAME LIKE '%"+ brandPO.getBrandName() +"%'");
		}
		if(null != brandPO.getEnglishName() && !"".equals(brandPO.getEnglishName())){
			querySQL.append(" AND T.ENGLISH_NAME LIKE '%"+ brandPO.getEnglishName() +"%'");
		}
		if(null != brandPO.getBrandCode() && !"".equals(brandPO.getBrandCode())){
			querySQL.append(" AND T.BRAND_CODE LIKE '%" + brandPO.getBrandCode() +"%'");
		}		
		if(null != brandPO.getOldModelSift() && !"".equals(brandPO.getOldModelSift())){
			params.add(brandPO.getOldModelSift());
			querySQL.append(" AND T.OLD_MODEL_SIFT = ?");
		}
		if(null != brandPO.getStatus() && !"".equals(brandPO.getStatus())){
			params.add(brandPO.getStatus());
			querySQL.append(" AND T.STATUS = ?");
		}		
		String sql = OrderUtil.addOrderBy(querySQL.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmBrandPO>() {
			public TmBrandPO wrapper(ResultSet rs, int idx) {
				TmBrandPO bean = new TmBrandPO();
				try {
					bean.setBrandId(rs.getLong("BRAND_ID"));
					bean.setBrandName(rs.getString("BRAND_NAME"));
					bean.setEnglishName(rs.getString("ENGLISH_NAME"));
					bean.setBrandCode(rs.getString("BRAND_CODE"));
					bean.setRemark(rs.getString("REMARK"));
					bean.setBusinessType(rs.getString("BUSINESS_TYPE"));
					bean.setBillTotal(rs.getString("BILL_TOTAL"));
					bean.setStatus(rs.getInt("STATUS"));
					bean.setCreateDate(rs.getTimestamp("CREATE_DATE"));
					bean.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
					bean.setCreateBy(rs.getLong("CREATE_BY"));
					bean.setUpdateBy(rs.getLong("UPDATE_BY"));
					bean.setOldModelSift(rs.getInt("OLD_MODEL_SIFT"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, pageSize, curPage);		
	}	
	
	/**
	 * BY 品牌ID 查询出指定品牌信息
	 * @param brandId 品牌ID
	 * @return
	 */
	public static TmBrandPO getBrandInfoByBrandId(Long brandId){
		if(null != brandId && !"".equals(brandId)){
			TmBrandPO po = new TmBrandPO();
			po.setBrandId(brandId);
			List<TmBrandPO> list = factory.select(po);
			if(null != list && list.size() > 0){
				po = list.get(0);
			}
			return po;
		}else{
			return null;
		}		
	}
}
