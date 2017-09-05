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

import com.infodms.dms.bean.TmCarShortNameBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.po.TmCarNamePO;
import com.infodms.dms.po.TmCarShortNamePO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;


public class SysCarShortNameDAO {

	public static Logger logger = Logger.getLogger(SysPositionDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	public static PageResult<TmCarShortNameBean> carShortNameQuery(TmCarShortNameBean shortNameBean, int curPage, int pageSize, String orderName, String da){
		StringBuffer querySQL = new StringBuffer();
		querySQL.append("SELECT B.BRAND_ID,B.BRAND_NAME,S.SERIES_ID,S.SERIES_NAME,C.CAR_NAME_ID,C.CHINESE_NAME AS CAR_NAME_SERIES,");
		querySQL.append("T.CAR_TYPE_ID,T.CAR_TYPE_CODE,T.CHINESE_NAME,T.ENGLISH_NAME,T.REMARK,T.STATUS,T.CREATE_DATE,T.UPDATE_DATE,");
		querySQL.append("T.CREATE_BY,T.UPDATE_BY,T.OLD_MODEL_SIFT FROM TM_CAR_SHORT_NAME T,TM_CAR_NAME C,TM_SERIES S,TM_BRAND B ");
		querySQL.append(" WHERE T.CAR_NAME_ID = C.CAR_NAME_ID AND C.SERIES_ID = S.SERIES_ID AND S.BRAND_ID = B.BRAND_ID ");
		List<Object> params = new ArrayList<Object>();
		
		if(null != shortNameBean.getBrandId() && !"".equals(shortNameBean.getBrandId())){
			params.add(shortNameBean.getBrandId());
			querySQL.append(" AND B.BRAND_ID = ? ");
		}
		if(null != shortNameBean.getSeriesId() && !"".equals(shortNameBean.getSeriesId())){
			params.add(shortNameBean.getSeriesId());
			querySQL.append(" AND S.SERIES_ID = ? ");
		}
		if(null != shortNameBean.getCarNameId() && !"".equals(shortNameBean.getCarNameId())){
			params.add(shortNameBean.getCarNameId());
			querySQL.append(" AND C.CAR_NAME_ID = ? ");
		}
		if(null != shortNameBean.getChineseName() && !"".equals(shortNameBean.getChineseName())){			
			querySQL.append(" AND T.CHINESE_NAME LIKE '%" + shortNameBean.getChineseName() + "%'");
		}		
		if(null != shortNameBean.getCarTypeCode() && !"".equals(shortNameBean.getCarTypeCode())){			
			querySQL.append(" AND T.CAR_TYPE_CODE LIKE '%" + shortNameBean.getCarTypeCode() + "%'");
		}	
		if(null != shortNameBean.getOldModelSift() && !"".equals(shortNameBean.getOldModelSift())){			
			params.add(shortNameBean.getOldModelSift());
			querySQL.append(" AND T.OLD_MODEL_SIFT = ?");
		}
		querySQL.append(" ORDER BY T.CAR_TYPE_ID DESC");
		
				
		String sql = OrderUtil.addOrderBy(querySQL.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmCarShortNameBean>() {
			public TmCarShortNameBean wrapper(ResultSet rs, int idx) {
				TmCarShortNameBean bean = new TmCarShortNameBean();
				try {
					bean.setBrandId(rs.getLong("BRAND_ID"));
					bean.setBrandName(rs.getString("BRAND_NAME"));
					bean.setSeriesId(rs.getLong("SERIES_ID"));
					bean.setSeriesName(rs.getString("SERIES_NAME"));
					bean.setCarNameId(rs.getLong("CAR_NAME_ID"));
					bean.setCarName(rs.getString("CAR_NAME_SERIES"));
					bean.setCarTypeId(rs.getLong("CAR_TYPE_ID"));
					bean.setCarTypeCode(rs.getString("CAR_TYPE_CODE"));
					bean.setChineseName(rs.getString("CHINESE_NAME"));
					bean.setEnglishName(rs.getString("ENGLISH_NAME"));
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
	 * BY 车型简称ID 查询出指定车型简称信息
	 * @param csnId 车型简称ID：CarShortNameId
	 * @return
	 */
	public static TmCarShortNamePO getCarShortInfoByCsnId(Long csnId){
		if(null != csnId && !"".equals(csnId)){
			TmCarShortNamePO po = new TmCarShortNamePO();
			po.setCarTypeId(csnId);
			List<TmCarShortNamePO> list = factory.select(po);
			if(null != list && list.size() > 0){
				po = list.get(0);
			}
			return po;
		}else{
			return null;
		}		
	}
	
	/**
	 * 查询出所有有效的车名系列信息
	 * @return
	 */
	public static List<TmCarNamePO> queryAllCarNames(){		
		TmCarNamePO po = new TmCarNamePO();
		//查询有效的品牌
		po.setStatus(Constant.STATUS_ENABLE);
		return factory.select(po);
	}
}
