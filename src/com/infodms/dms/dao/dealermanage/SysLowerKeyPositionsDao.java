/**********************************************************************
 * <pre>
 * FILE : SysLowerKeyPositionsDao.java
 * CLASS : SysLowerKeyPositionsDao
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
 * 		    |2009-12-25|yangyong| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.dao.dealermanage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.TmStationInfosBean;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.po.TmStationInfosChangePO;
import com.infodms.dms.po.TmStationMappingsPO;
import com.infodms.dms.po.TmStationPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

public class SysLowerKeyPositionsDao {
	public static Logger logger = Logger.getLogger(SysPositionDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	public static PageResult<TmStationInfosBean> findAllKeyPositionsInfo(TmStationInfosBean stationBean, int curPage, int pageSize, String orderName, String da){
		StringBuffer sb = new StringBuffer();
		List<Object> params = new ArrayList<Object>();		
//		querySQL.append("SELECT T.STATION_ID AS STA_ID,T.NAME AS STA_NAME,C.AUDIT_STATUS,S.STATION_INFOS_ID,S.STATION_ID,");
//		querySQL.append("S.DEALER_ID,S.NAME,S.LINK_TEL,S.GENDER,S.PERSONAL,S.WORK,S.TRAIN,S.OTHER,S.AUDIT_USER,");
//		querySQL.append("TO_CHAR(S.AUDIT_DATE,'yyyy-mm-dd') AS AUDIT_DATE,S.CREATE_BY,TO_CHAR(S.CREATE_DATE,'yyyy-mm-dd') AS CREATE_DATE,");
//		querySQL.append("TO_CHAR(S.UPDATE_DATE,'yyyy-mm-dd') AS UPDATE_DATE,S.UPDATE_BY,S.EMAIL,S.INFOS_STATUS ");
//		querySQL.append(" FROM TM_STATION T LEFT JOIN TM_STATION_INFOS S ON T.STATION_ID = S.STATION_ID ");
//		querySQL.append(" LEFT JOIN (SELECT * FROM TM_STATION_INFOS_CHANGE SIC WHERE SIC.AUDIT_STATUS = '40001001') C ");
//		querySQL.append(" ON S.STATION_INFOS_ID = C.STATION_INFOS_ID AND S.STATION_ID = T.STATION_ID ORDER BY T.NAME ");
		
		sb.append("SELECT M.STATION_MAPPING_ID,M.STATUS,S.STATION_ID,S.NAME AS STA_NAME,C.RECORD_ID,C.DEALER_ID,C.NAME,");
		sb.append("C.GENDER,C.LINK_TEL,C.EMAIL,C.PERSONAL,C.WORK,C.TRAIN,C.OTHER,C.INFOS_STATUS,C.AUDIT_USER,");
		sb.append("TO_CHAR(C.AUDIT_DATE,'yyyy-mm-dd') AS AUDIT_DATE,C.AUDIT_STATUS,C.CREATE_BY,");
		sb.append("TO_CHAR(C.CREATE_DATE,'yyyy-mm-dd') AS CREATE_DATE,C.UPDATE_BY,");
		sb.append("TO_CHAR(C.UPDATE_DATE,'yyyy-mm-dd') AS UPDATE_DATE ");
		sb.append(" FROM TM_STATION_MAPPINGS M LEFT JOIN TM_STATION S ON M.STATION_ID = S.STATION_ID ");
		sb.append(" LEFT JOIN TM_STATION_INFOS_CHANGE C ON M.RECORD_ID = C.RECORD_ID ");
		sb.append(" ORDER BY S.NAME ");
		
		String sql = OrderUtil.addOrderBy(sb.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmStationInfosBean>() {
			public TmStationInfosBean wrapper(ResultSet rs, int idx) {
				TmStationInfosBean bean = new TmStationInfosBean();
				try {
					bean.setStationMappingId(rs.getLong("STATION_MAPPING_ID"));
					bean.setStatus(rs.getInt("STATUS"));
					
					bean.setStationId(rs.getLong("STATION_ID"));
					bean.setStaName(rs.getString("STA_NAME"));				
					
					bean.setRecordId(rs.getLong("RECORD_ID"));				
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setName(rs.getString("NAME"));
					bean.setGender(rs.getInt("GENDER"));
					bean.setLinkTel(rs.getString("LINK_TEL"));
					bean.setEmail(rs.getString("EMAIL"));					
					bean.setPersonal(rs.getString("PERSONAL"));
					bean.setWork(rs.getString("WORK"));
					bean.setTrain(rs.getString("TRAIN"));
					bean.setOther(rs.getString("OTHER"));
					bean.setInfosStatus(rs.getInt("INFOS_STATUS"));					
					bean.setAuditUser(rs.getLong("AUDIT_USER"));
					bean.setAuditDate(rs.getString("AUDIT_DATE"));
					bean.setAuditStatus(rs.getInt("AUDIT_STATUS"));						
					bean.setCreateBy(rs.getLong("CREATE_BY"));
					bean.setCreateDate(rs.getString("CREATE_DATE"));
					bean.setUpdateDate(rs.getString("UPDATE_DATE"));
					bean.setUpdateBy(rs.getLong("UPDATE_BY"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, pageSize, curPage);	
	}
	
	/**
	 * BY 关键岗位ID 查询出指定关键岗位信息
	 * @param stationId 关键岗位信息ID : stationId
	 * @return
	 */
	public static TmStationPO getStationByStaId(Long stationId){
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
	
	/**
	 * BY 关键岗位人员信息ID 查询出指定关键岗位人员信息
	 * @param recordId 关键岗位人员信息ID : recordId
	 * @return
	 */
	public static TmStationInfosChangePO getStationInfosChangeByRecordId(Long recordId){
		if(null != recordId && !"".equals(recordId)){
			TmStationInfosChangePO po = new TmStationInfosChangePO();
			po.setRecordId(recordId);
			List<TmStationInfosChangePO> list = factory.select(po);
			if(null != list && list.size() > 0){
				po = list.get(0);
			}
			return po;
		}else{
			return null;
		}		
	}
	
	/**
	 * 通过关键岗位ID查询出对应的关系
	 * @param stationId
	 * @return
	 */
	public static TmStationMappingsPO getStationMappingByStationId(Long stationId){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT T.STATION_MAPPING_ID,T.STATION_ID,T.RECORD_ID,T.STATUS ");
		sb.append(" FROM TM_STATION_MAPPINGS T WHERE T.STATION_ID = ? ");
		
		List<Object> params = new ArrayList<Object>();
		params.add(stationId);		
		
		List<TmStationMappingsPO> list = factory.select(sb.toString(), params,new DAOCallback<TmStationMappingsPO>() {
			public TmStationMappingsPO wrapper(ResultSet rs, int idx){
				TmStationMappingsPO po = new TmStationMappingsPO();
				try {
					po.setStationMappingId(rs.getLong("STATION_MAPPING_ID"));
					po.setStationId(rs.getLong("STATION_ID"));
					po.setRecordId(rs.getLong("RECORD_ID"));
					po.setStatus(rs.getInt("STATUS"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return po;
			}
		});
		
		if(list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
}
