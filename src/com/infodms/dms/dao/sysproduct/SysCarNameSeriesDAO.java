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

import com.infodms.dms.bean.TmCarNameBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.po.TmCarNamePO;
import com.infodms.dms.po.TmSeriesPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;


public class SysCarNameSeriesDAO {

	public static Logger logger = Logger.getLogger(SysPositionDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	public static PageResult<TmCarNameBean> carNameSeriesQuery(TmCarNameBean carNameBean, int curPage, int pageSize, String orderName, String da){
		StringBuffer querySQL = new StringBuffer();
		querySQL.append("SELECT T.CAR_NAME_ID,T.SERIES_ID,S.SERIES_NAME,B.BRAND_ID,B.BRAND_NAME,T.CHINESE_NAME,T.ENGLISH_NAME,");
		querySQL.append("T.CAR_NAME_CODE,T.REMARK,T.STATUS,T.CREATE_DATE,T.UPDATE_DATE,T.CREATE_BY,T.UPDATE_BY,T.OLD_MODEL_SIFT ");
		querySQL.append(" FROM TM_CAR_NAME T,TM_BRAND B,TM_SERIES S WHERE T.SERIES_ID = S.SERIES_ID AND S.BRAND_ID = B.BRAND_ID ");
		List<Object> params = new ArrayList<Object>();
		
		if(null != carNameBean.getBrandId() && !"".equals(carNameBean.getBrandId())){
			params.add(carNameBean.getBrandId());
			querySQL.append(" AND B.BRAND_ID = ?");
		}		
		if(null != carNameBean.getSeriesId() && !"".equals(carNameBean.getSeriesId())){
			params.add(carNameBean.getSeriesId());
			querySQL.append(" AND S.SERIES_ID = ?");
		}		
		
		if(null != carNameBean.getChineseName() && !"".equals(carNameBean.getChineseName())){
			querySQL.append(" AND T.CHINESE_NAME LIKE '%" + carNameBean.getChineseName() + "%'");
		}		
		if(null != carNameBean.getCarNameCode() && !"".equals(carNameBean.getCarNameCode())){
			querySQL.append(" AND T.CAR_NAME_CODE LIKE '%" + carNameBean.getCarNameCode() + "%'");
		}		
		if(null != carNameBean.getOldModelSift() && !"".equals(carNameBean.getOldModelSift())){
			params.add(carNameBean.getOldModelSift());
			querySQL.append(" AND T.OLD_MODEL_SIFT = ?");
		}		
		querySQL.append(" ORDER BY T.CAR_NAME_ID DESC");
		
		String sql = OrderUtil.addOrderBy(querySQL.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmCarNameBean>() {
			public TmCarNameBean wrapper(ResultSet rs, int idx) {
				TmCarNameBean bean = new TmCarNameBean();
				try {
					bean.setCarNameId(rs.getLong("CAR_NAME_ID"));
					bean.setSeriesId(rs.getLong("SERIES_ID"));
					bean.setSeriesName(rs.getString("SERIES_NAME"));
					bean.setBrandId(rs.getLong("BRAND_ID"));
					bean.setBrandName(rs.getString("BRAND_NAME"));
					bean.setChineseName(rs.getString("CHINESE_NAME"));
					bean.setEnglishName(rs.getString("ENGLISH_NAME"));
					bean.setCarNameCode(rs.getString("CAR_NAME_CODE"));
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
	 * BY 车名系列ID 查询出指定车名系列信息
	 * @param CnsId 车名系列ID：CarNameSeriesId
	 * @return
	 */
	public static TmCarNamePO getCarNameSeriesInfoByCnsId(Long cnsId){
		if(null != cnsId && !"".equals(cnsId)){
			TmCarNamePO po = new TmCarNamePO();
			po.setCarNameId(cnsId);
			List<TmCarNamePO> list = factory.select(po);
			if(null != list && list.size() > 0){
				po = list.get(0);
			}
			return po;
		}else{
			return null;
		}		
	}
	
	/**
	 * 查询出所有有效的系列信息
	 * @return
	 */
	public static List<TmSeriesPO> queryAllSeriesNames(){		
		TmSeriesPO po = new TmSeriesPO();
		//查询有效的品牌
		po.setStatus(Constant.STATUS_ENABLE);
		return factory.select(po);
	}
}
