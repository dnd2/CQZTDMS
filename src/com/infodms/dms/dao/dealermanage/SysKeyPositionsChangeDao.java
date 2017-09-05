/**********************************************************************
 * <pre>
 * FILE : SysKeyPositionsChangeDao.java
 * CLASS : SysKeyPositionsChangeDao
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
import com.infodms.dms.po.TmStationPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

public class SysKeyPositionsChangeDao {
	public static Logger logger = Logger.getLogger(SysPositionDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	/**
	 * 查询出所有的关键岗位信息
	 * @return
	 */
	public static List<TmStationPO> findAllStationInfo(){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT T.STATION_ID,T.NAME FROM TM_STATION T ORDER BY T.NAME");
				
		return factory.select(sb.toString(), null,new DAOCallback<TmStationPO>() {
			public TmStationPO wrapper(ResultSet rs, int idx){
				TmStationPO po = new TmStationPO();
				try {
					po.setStationId(rs.getLong("STATION_ID"));
					po.setName(rs.getString("NAME"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return po;
			}
		});		
	}
	
	public static PageResult<TmStationInfosBean> findAllKeyPositionsChangeInfo(TmStationInfosBean stationBean, int curPage, int pageSize, String orderName, String da){
		StringBuffer sb = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sb.append("SELECT D.DEALER_CODE,D.DEALER_SHORTNAME,S.STATION_ID,S.NAME AS STA_NAME,C.RECORD_ID,");
		sb.append("C.DEALER_ID,C.NAME,C.GENDER,C.LINK_TEL,C.EMAIL,C.PERSONAL,C.WORK,C.TRAIN,C.OTHER,");
		sb.append("C.INFOS_STATUS,C.AUDIT_USER,TO_CHAR(C.AUDIT_DATE,'yyyy-mm-dd') AS AUDIT_DATE,");
		sb.append("C.AUDIT_STATUS,C.CREATE_BY,TO_CHAR(C.CREATE_DATE,'yyyy-mm-dd') AS CREATE_DATE,");
		sb.append("C.UPDATE_BY,TO_CHAR(C.UPDATE_DATE,'yyyy-mm-dd') AS UPDATE_DATE ");
		sb.append(" FROM TM_STATION_INFOS_CHANGE C LEFT JOIN TM_DEALER D ON C.DEALER_ID = D.DEALER_ID ");
		sb.append(" LEFT JOIN TM_STATION S ON C.STATION_ID = S.STATION_ID WHERE 1=1 ");
		
		if(null != stationBean.getDealerId() && !"".equals(stationBean.getDealerId())){
			params.add(stationBean.getDealerId());
			sb.append(" AND C.DEALER_ID = ? ");
		}		
		if(null != stationBean.getStationId() && !"".equals(stationBean.getStationId())){
			params.add(stationBean.getStationId());
			sb.append(" AND S.STATION_ID = ? ");	
		}		
		if(null != stationBean.getAuditStatus() && !"".equals(stationBean.getAuditStatus())){
			params.add(stationBean.getAuditStatus());
			sb.append(" AND C.AUDIT_STATUS = ? ");
		}		
		if(null != stationBean.getApproveDateStart() && !"".equals(stationBean.getApproveDateStart())){
			params.add(stationBean.getApproveDateStart());
			sb.append(" AND TO_CHAR(C.CREATE_DATE,'yyyy-mm-dd') >= ? ");
		}		
		if(null != stationBean.getApproveDateEnd()&& !"".equals(stationBean.getApproveDateEnd())){
			params.add(stationBean.getApproveDateEnd());
			sb.append(" AND TO_CHAR(C.CREATE_DATE,'yyyy-mm-dd') <= ? ");
		}	
		
		sb.append(" ORDER BY S.NAME ");		
			
		
		String sql = OrderUtil.addOrderBy(sb.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmStationInfosBean>() {
			public TmStationInfosBean wrapper(ResultSet rs, int idx) {
				TmStationInfosBean bean = new TmStationInfosBean();
				try {
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					
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
	
	public static PageResult<TmStationInfosBean> findAllSysKeyPositionsApplyHistoryInfoByStationId(Long stationId, int curPage, int pageSize, String orderName, String da){
		
		StringBuffer sb = new StringBuffer();		
		sb.append("SELECT T.RECORD_ID,T.STATION_ID,S.NAME AS STA_NAME,T.DEALER_ID,D.DEALER_SHORTNAME,T.NAME,T.GENDER,");
		sb.append("T.LINK_TEL,T.EMAIL,T.PERSONAL,T.WORK,T.TRAIN,T.OTHER,T.INFOS_STATUS,T.AUDIT_USER,U.NAME AS AUDIT_NAME,");
		sb.append("TO_CHAR(T.AUDIT_DATE,'yyyy-mm-dd') AS AUDIT_DATE,T.AUDIT_STATUS,T.CREATE_BY,");
		sb.append("TO_CHAR(T.CREATE_DATE,'yyyy-mm-dd') AS CREATE_DATE,T.UPDATE_BY,");
		sb.append("TO_CHAR(T.UPDATE_DATE,'yyyy-mm-dd') AS UPDATE_DATE ");
		sb.append(" FROM TM_STATION_INFOS_CHANGE T LEFT JOIN TM_STATION S ON T.STATION_ID = S.STATION_ID ");
		sb.append(" LEFT JOIN TC_USER U ON T.AUDIT_USER = U.USER_ID ");
		sb.append(" LEFT JOIN TM_DEALER D ON T.DEALER_ID = D.DEALER_ID WHERE T.STATION_ID = ? ");
		sb.append(" ORDER BY T.CREATE_DATE DESC ");
		
		List<Object> params = new ArrayList<Object>();
		params.add(stationId);			
		String sql = OrderUtil.addOrderBy(sb.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmStationInfosBean>() {
			public TmStationInfosBean wrapper(ResultSet rs, int idx) {
				TmStationInfosBean bean = new TmStationInfosBean();
				try {
					bean.setRecordId(rs.getLong("RECORD_ID"));	
					bean.setStationId(rs.getLong("STATION_ID"));
					bean.setStaName(rs.getString("STA_NAME"));	
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
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
					bean.setAuditName(rs.getString("AUDIT_NAME"));
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
}
