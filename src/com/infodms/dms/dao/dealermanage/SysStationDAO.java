/**********************************************************************
* <pre>
* FILE : SysPositionDAO.java
* CLASS : SysPositionDAO
* 
* AUTHOR : ChenLiang
*
* FUNCTION : 系统职位DAO.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-08-23| ChenLiang  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.dao.dealermanage;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.po.TmStationPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

public class SysStationDAO {
	public static Logger logger = Logger.getLogger(SysStationDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	/**
	 * 查询出所有有效的关键岗位信息
	 * @param curPage
	 * @param pageSize
	 * @param orderName
	 * @param da
	 * @return
	 * @throws Exception
	 */
	public static PageResult<TmStationPO> sysStationQuery(int curPage,int pageSize, String orderName, String da) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT T.STATION_ID,T.NAME,T.REMARK,T.STATUS FROM TM_STATION T ");
		sb.append(" WHERE T.STATUS = ? ORDER BY T.NAME ");
		//参数：有效
		List<Object> params = new ArrayList<Object>();
		params.add(Constant.STATUS_ENABLE);
		String sql = OrderUtil.addOrderBy(sb.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmStationPO>() {
			public TmStationPO wrapper(ResultSet rs, int idx) {
				TmStationPO bean = new TmStationPO();
				try {
					bean.setStationId(rs.getLong("STATION_ID"));
					bean.setName(rs.getString("NAME"));
					bean.setRemark(rs.getString("REMARK"));	
					bean.setStatus(rs.getInt("STATUS"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, pageSize, curPage);
	}
	
	/**
	 * BY 关键岗位Id 查询出指定的关键岗位信息
	 * @param stationId 岗位ID:StationId
	 * @return
	 */
	public static TmStationPO getStationInfoByStationId(Long stationId){
		if(null != stationId && !"".equals(stationId)){
			TmStationPO po = new TmStationPO();
			po.setStationId(stationId);
			List<TmStationPO> list = factory.select(po);
			if(null != list && list.size() > 0){
				po = list.get(0);
			}
			return po;
		}else{
			return null;
		}		
	}
}
