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

import com.infodms.dms.bean.SeriesBean;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.po.TmSeriesPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

public class SysSeriesDAO {

	public static Logger logger = Logger.getLogger(SysPositionDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	public static PageResult<SeriesBean> seriesQuery(TmSeriesPO seriesPO, int curPage, int pageSize, String orderName, String da){
		StringBuffer querySQL = new StringBuffer();
		querySQL.append("SELECT T.SERIES_ID,T.BRAND_ID,B.BRAND_NAME,T.SERIES_NAME,T.ENGLISH_NAME,T.SERIES_CODE,T.REMARK,T.STATUS,");
		querySQL.append("T.CREATE_DATE,T.UPDATE_DATE,T.CREATE_BY,T.UPDATE_BY,T.OLD_MODEL_SIFT FROM TM_SERIES T,TM_BRAND B ");
		querySQL.append(" WHERE T.BRAND_ID = B.BRAND_ID ");
		List<Object> params = new ArrayList<Object>();
		
		if(null != seriesPO.getSeriesId() && !"".equals(seriesPO.getSeriesId())){
			params.add(seriesPO.getSeriesId());
			querySQL.append(" AND T.SERIES_ID = ?");
		}
		
		if(null != seriesPO.getBrandId() && !"".equals(seriesPO.getBrandId())){
			params.add(seriesPO.getBrandId());
			querySQL.append(" AND T.BRAND_ID = ?");
		}
		
		if(null != seriesPO.getSeriesName() && !"".equals(seriesPO.getSeriesName())){
			querySQL.append(" AND T.SERIES_NAME LIKE '%"+ seriesPO.getSeriesName() +"%'");
		}
		if(null != seriesPO.getEnglishName() && !"".equals(seriesPO.getEnglishName())){
			querySQL.append(" AND T.ENGLISH_NAME LIKE '%" + seriesPO.getEnglishName() +"%'");
		}
		if(null != seriesPO.getOldModelSift() && !"".equals(seriesPO.getOldModelSift())){
			params.add("'" + seriesPO.getOldModelSift() +"'");
			querySQL.append(" AND T.OLD_MODEL_SIFT = ?");
		}
		if(null != seriesPO.getSeriesCode() && !"".equals(seriesPO.getSeriesCode())){
			params.add("'" + seriesPO.getSeriesCode() +"'");
			querySQL.append(" AND T.SERIES_CODE = ?");
		}
		if(null != seriesPO.getStatus() && !"".equals(seriesPO.getStatus())){
			params.add(seriesPO.getStatus());
			querySQL.append(" AND T.STATUS = ?");
		}		
		String sql = OrderUtil.addOrderBy(querySQL.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<SeriesBean>() {
			public SeriesBean wrapper(ResultSet rs, int idx) {
				SeriesBean bean = new SeriesBean();
				try {					
					bean.setSeriesId(rs.getLong("SERIES_ID"));
					bean.setBrandId(rs.getLong("BRAND_ID"));
					bean.setBrandName(rs.getString("BRAND_NAME"));
					bean.setSeriesName(rs.getString("SERIES_NAME"));
					bean.setEnglishName(rs.getString("ENGLISH_NAME"));
					bean.setSeriesCode(rs.getString("SERIES_CODE"));
					bean.setRemark(rs.getString("REMARK"));					
					bean.setStatus(rs.getInt("STATUS"));
					bean.setCreateDate(rs.getTimestamp("CREATE_DATE"));
					bean.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
					bean.setCreateUser(rs.getLong("CREATE_BY"));
					bean.setUpdateUser(rs.getLong("UPDATE_BY"));
					bean.setOldModelSift(rs.getInt("OLD_MODEL_SIFT"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, pageSize, curPage);		
	}	
	
	/**
	 * BY 系列ID 查询出指定系列信息
	 * @param seriesId 系列ID
	 * @return
	 */
	public static TmSeriesPO getSeriesInfoBySeriesId(Long seriesId){
		if(null != seriesId && !"".equals(seriesId)){
			TmSeriesPO po = new TmSeriesPO();
			po.setSeriesId(seriesId);
			List<TmSeriesPO> list = factory.select(po);
			if(null != list && list.size() > 0){
				po = list.get(0);
			}
			return po;
		}else{
			return null;
		}		
	}	
}
