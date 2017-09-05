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

import com.infodms.dms.bean.TmModelBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.po.TmCarShortNamePO;
import com.infodms.dms.po.TmModelPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;


public class SysCarModelDAO {

	public static Logger logger = Logger.getLogger(SysPositionDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	public static PageResult<TmModelBean> carModelQuery(TmModelPO modelPo, int curPage, int pageSize, String orderName, String da){
		StringBuffer querySQL = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		querySQL.append("SELECT TB.BRAND_ID,TB.BRAND_NAME,TS.SERIES_ID,TS.SERIES_NAME,TC.CAR_NAME_ID,");
		querySQL.append("TC.CHINESE_NAME AS CAR_NAME,TN.CAR_TYPE_ID,TN.CHINESE_NAME AS CAR_TYPE_NAME,");
		querySQL.append("TM.MODEL_ID,TM.STATUS,TM.MODEL_NAME,TM.MODEL_CODE,TM.CREATE_TYPE_AB,TM.CREATE_DATE,");
		querySQL.append("TM.UPDATE_DATE,TM.CREATE_BY,TM.UPDATE_BY,TM.OLD_MODEL_SIFT ");
		querySQL.append(" FROM TM_MODEL TM,TM_CAR_SHORT_NAME TN,TM_CAR_NAME TC,TM_SERIES TS,TM_BRAND TB ");
		querySQL.append(" WHERE TM.CAR_TYPE_ID = TN.CAR_TYPE_ID AND TN.CAR_NAME_ID = TC.CAR_NAME_ID ");
		querySQL.append(" AND TC.SERIES_ID = TS.SERIES_ID AND TS.BRAND_ID = TB.BRAND_ID ");
		if(null != modelPo.getModelId() && !"".equals(modelPo.getModelId())){
			params.add(modelPo.getModelId());
			querySQL.append(" AND TM.MODEL_ID = ?");
		}		
		if(null != modelPo.getModelName() && !"".equals(modelPo.getModelName())){			
			querySQL.append(" AND TM.MODEL_NAME LIKE '%" + modelPo.getModelName() + "%'");
		}		
		if(null != modelPo.getModelCode() && !"".equals(modelPo.getModelCode())){			
			querySQL.append(" AND TM.MODEL_CODE LIKE '%" + modelPo.getModelCode() + "%'");
		}	
		if(null != modelPo.getOldModelSift() && !"".equals(modelPo.getOldModelSift())){			
			params.add(modelPo.getOldModelSift());
			querySQL.append(" AND TM.OLD_MODEL_SIFT = ?");
		}
		if(null != modelPo.getCreateTypeAb() && !"".equals(modelPo.getCreateTypeAb())){
			params.add(modelPo.getCreateTypeAb());
			querySQL.append(" AND TM.CREATE_TYPE_AB = ?");
		}		
		querySQL.append(" ORDER BY TM.MODEL_ID DESC");		
		
		String sql = OrderUtil.addOrderBy(querySQL.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmModelBean>() {
			public TmModelBean wrapper(ResultSet rs, int idx) {
				TmModelBean bean = new TmModelBean();
				try {
					bean.setBrandId(rs.getLong("BRAND_ID"));
					bean.setBrandName(rs.getString("BRAND_NAME"));
					bean.setSeriesId(rs.getLong("SERIES_ID"));
					bean.setSeriesName(rs.getString("SERIES_NAME"));
					bean.setCarNameId(rs.getLong("CAR_NAME_ID"));
					bean.setCarName(rs.getString("CAR_NAME"));
					bean.setCarTypeId(rs.getLong("CAR_TYPE_ID"));
					bean.setCarTypeName(rs.getString("CAR_TYPE_NAME"));	
					
					bean.setModelId(rs.getLong("MODEL_ID"));
					bean.setStatus(rs.getInt("STATUS"));
					bean.setModelName(rs.getString("MODEL_NAME"));
					bean.setModelCode(rs.getString("MODEL_CODE"));
					bean.setCreateTypeAb(rs.getInt("CREATE_TYPE_AB"));
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
	 * BY 车型ID 查询出指定车型信息
	 * @param modelId 车型ID:modelId
	 * @return
	 */
	public static TmModelPO getCarModelInfoByModelId(Long modelId){
		if(null != modelId && !"".equals(modelId)){
			TmModelPO po = new TmModelPO();
			po.setModelId(modelId);
			List<TmModelPO> list = factory.select(po);
			if(null != list && list.size() > 0){
				po = list.get(0);
			}
			return po;
		}else{
			return null;
		}		
	}
	
	/**
	 * 查询出所有有效的车型简称信息
	 * @return
	 */
	public static List<TmCarShortNamePO> queryAllCarShortNames(){		
		TmCarShortNamePO po = new TmCarShortNamePO();
		//查询有效的车型简称
		po.setStatus(Constant.STATUS_ENABLE);
		return factory.select(po);
	}
}
