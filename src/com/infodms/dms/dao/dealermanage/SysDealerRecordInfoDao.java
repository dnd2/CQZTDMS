/**********************************************************************
 * <pre>
 * FILE : SysDealerRecordInfoDao.java
 * CLASS : SysDealerRecordInfoDao
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
 * 		    |2009-12-31|yangyong| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.dao.dealermanage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.po.TmDealerRecordChangePO;
import com.infodms.dms.po.TmDealerRecordPO;
import com.infodms.dms.po.TmShuntDealerChangePO;
import com.infodms.dms.po.TmShuntDealerPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

public class SysDealerRecordInfoDao {

	public static Logger logger = Logger.getLogger(SysDealerDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	/**
	 * 查询经销商信息记录
	 * @param drPo
	 * @param curPage
	 * @param pageSize
	 * @param orderName
	 * @param da
	 * @return
	 */
	public static PageResult<TmDealerRecordPO> findDealerRecordInfos(TmDealerRecordPO drPo, int curPage, int pageSize, String orderName, String da){
		StringBuffer sb = new StringBuffer();
		List<Object> params = new ArrayList<Object>();	

		sb.append("SELECT T.RECORD_ID,T.DEALER_ID,T.DEALER_NAME,T.DEALER_SHORTNAME,");
		sb.append("T.DEALER_CODE,T.ADDRESS,T.PHONE,T.FAX_NO,T.LEGAL_PERSON ");
		sb.append(" FROM TM_DEALER_RECORD T WHERE 1=1 ");		
		if(null != drPo.getDealerId() && !"".equals(drPo.getDealerId())){
			params.add(drPo.getDealerId());
			sb.append(" AND T.DEALER_ID = ? ");
		}		
		sb.append(" ORDER BY T.DEALER_SHORTNAME ");

		String sql = OrderUtil.addOrderBy(sb.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmDealerRecordPO>() {
			public TmDealerRecordPO wrapper(ResultSet rs, int idx) {
				TmDealerRecordPO bean = new TmDealerRecordPO();
				try {
					bean.setRecordId(rs.getLong("RECORD_ID"));
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setAddress(rs.getString("ADDRESS"));
					bean.setPhone(rs.getString("PHONE"));
					bean.setFaxNo(rs.getString("FAX_NO"));
					bean.setLegalPerson(rs.getString("LEGAL_PERSON"));					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, pageSize, curPage);	
	}
	
	/**
	 * 查询代理商信息变更历史
	 * @param drPo
	 * @param curPage
	 * @param pageSize
	 * @param orderName
	 * @param da
	 * @return
	 */
	public static PageResult<TmDealerRecordChangePO> findDealerRecordChangeInfos(TmDealerRecordChangePO drcPo, int curPage, int pageSize, String orderName, String da){
		StringBuffer sb = new StringBuffer();
		List<Object> params = new ArrayList<Object>();	

		sb.append("SELECT T.CHANGE_ID,T.RECORD_ID,T.DEALER_ID,T.DEALER_NAME,");
		sb.append("T.DEALER_SHORTNAME,T.DEALER_CODE,T.ADDRESS,T.PHONE,T.FAX_NO,");
		sb.append("T.LEGAL_PERSON,T.VERSION FROM TM_DEALER_RECORD_CHANGE T WHERE 1=1 ");
		if(null != drcPo.getDealerId() && !"".equals(drcPo.getDealerId())){
			params.add(drcPo.getDealerId());
			sb.append(" AND T.DEALER_ID = ? ");
		}		
		sb.append(" ORDER BY T.DEALER_SHORTNAME ");

		String sql = OrderUtil.addOrderBy(sb.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmDealerRecordChangePO>() {
			public TmDealerRecordChangePO wrapper(ResultSet rs, int idx) {
				TmDealerRecordChangePO bean = new TmDealerRecordChangePO();
				try {
					bean.setChangeId(rs.getLong("CHANGE_ID"));
					bean.setRecordId(rs.getLong("RECORD_ID"));
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setAddress(rs.getString("ADDRESS"));
					bean.setPhone(rs.getString("PHONE"));
					bean.setFaxNo(rs.getString("FAX_NO"));
					bean.setLegalPerson(rs.getString("LEGAL_PERSON"));	
					bean.setVersion(rs.getLong("VERSION"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, pageSize, curPage);	
	}
	
	
	/**
	 * 查询经销商下所有的分销商记录
	 * @param drPo
	 * @param curPage
	 * @param pageSize
	 * @param orderName
	 * @param da
	 * @return
	 */
	public static PageResult<TmShuntDealerPO> findShuntDealerInfos(TmShuntDealerPO sdPo, int curPage, int pageSize, String orderName, String da){
		StringBuffer sb = new StringBuffer();
		List<Object> params = new ArrayList<Object>();	

		sb.append("SELECT S.RECORD_ID,S.SHUNT_NAME,S.ADDRESS,S.PHONE,S.ZIP_CODE,S.FAX_NO,S.LINK_MAN,");
		sb.append("S.COM_LEVEL,S.COOPERATION_NATURE, S.CREATE_DATE,S.CREATE_BY,S.UPDATE_DATE,S.UPDATE_BY ");
		sb.append(" FROM TM_SHUNT_DEALER S WHERE 1=1 ");	
		
		if(null != sdPo.getRecordId() && !"".equals(sdPo.getRecordId())){
			params.add(sdPo.getRecordId());
			sb.append(" AND S.RECORD_ID = ? ");
		}		
		sb.append(" ORDER BY S.SHUNT_NAME ");

		String sql = OrderUtil.addOrderBy(sb.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmShuntDealerPO>() {
			public TmShuntDealerPO wrapper(ResultSet rs, int idx) {
				TmShuntDealerPO bean = new TmShuntDealerPO();
				try {
					bean.setRecordId(rs.getLong("RECORD_ID"));
					bean.setShuntName(rs.getString("SHUNT_NAME"));
					bean.setAddress(rs.getString("ADDRESS"));
					bean.setPhone(rs.getString("PHONE"));
					bean.setZipCode(rs.getString("ZIP_CODE"));
					bean.setFaxNo(rs.getString("FAX_NO"));
					bean.setLinkMan(rs.getString("LINK_MAN"));
					bean.setComLevel(rs.getString("COM_LEVEL"));
					bean.setCooperationNature(rs.getString("COOPERATION_NATURE"));
					bean.setCreateDate(rs.getTimestamp("CREATE_DATE"));
					bean.setCreateBy(rs.getLong("CREATE_BY"));
					bean.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
					bean.setUpdateBy(rs.getLong("UPDATE_BY"));									
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, pageSize, curPage);	
	}
	
	/**
	 * 查询经销商下所有的分销商历史变更记录
	 * @param drPo
	 * @param curPage
	 * @param pageSize
	 * @param orderName
	 * @param da
	 * @return
	 */
	public static PageResult<TmShuntDealerChangePO> findShuntDealerChangeInfos(TmShuntDealerChangePO sdcPo, int curPage, int pageSize, String orderName, String da){
		StringBuffer sb = new StringBuffer();
		List<Object> params = new ArrayList<Object>();	

		sb.append("SELECT S.CHANGE_ID,S.SHUNT_NAME,S.ADDRESS,S.PHONE,S.ZIP_CODE,S.FAX_NO,S.LINK_MAN,");
		sb.append("S.COM_LEVEL,S.COOPERATION_NATURE, S.CREATE_DATE,S.CREATE_BY,S.UPDATE_DATE,S.UPDATE_BY ");
		sb.append(" FROM TM_SHUNT_DEALER_CHANGE S WHERE 1=1 ");	
		
		if(null != sdcPo.getChangeId() && !"".equals(sdcPo.getChangeId())){
			params.add(sdcPo.getChangeId());
			sb.append(" AND S.CHANGE_ID = ? ");
		}		
		sb.append(" ORDER BY S.SHUNT_NAME ");

		String sql = OrderUtil.addOrderBy(sb.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmShuntDealerChangePO>() {
			public TmShuntDealerChangePO wrapper(ResultSet rs, int idx) {
				TmShuntDealerChangePO bean = new TmShuntDealerChangePO();
				try {
					bean.setChangeId(rs.getLong("CHANGE_ID"));
					bean.setShuntName(rs.getString("SHUNT_NAME"));
					bean.setAddress(rs.getString("ADDRESS"));
					bean.setPhone(rs.getString("PHONE"));
					bean.setZipCode(rs.getString("ZIP_CODE"));
					bean.setFaxNo(rs.getString("FAX_NO"));
					bean.setLinkMan(rs.getString("LINK_MAN"));
					bean.setComLevel(rs.getString("COM_LEVEL"));
					bean.setCooperationNature(rs.getString("COOPERATION_NATURE"));
					bean.setCreateDate(rs.getTimestamp("CREATE_DATE"));
					bean.setCreateBy(rs.getLong("CREATE_BY"));
					bean.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
					bean.setUpdateBy(rs.getLong("UPDATE_BY"));									
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, pageSize, curPage);	
	}

	/**
	 * BY 代理商信息ID 查询出指定代理商信息
	 * @param recordId 代理商信息ID : recordId
	 * @return
	 */
	public static TmDealerRecordPO getDealerRecordInfoByRecordId(Long recordId){
		if(null != recordId && !"".equals(recordId)){
			TmDealerRecordPO po = new TmDealerRecordPO();
			po.setRecordId(recordId);
			List<TmDealerRecordPO> list = factory.select(po);
			if(null != list && list.size() > 0){
				po = list.get(0);
			}
			return po;
		}else{
			return null;
		}		
	}
	
	/**
	 * BY 代理商历史记录ID 查询出指定代理商历史记录信息
	 * @param changeId 代理商历史记录ID : changeId
	 * @return
	 */
	public static TmDealerRecordChangePO getDealerRecordChangeInfoByChangeId(Long changeId){
		if(null != changeId && !"".equals(changeId)){
			TmDealerRecordChangePO po = new TmDealerRecordChangePO();
			po.setChangeId(changeId);
			List<TmDealerRecordChangePO> list = factory.select(po);
			if(null != list && list.size() > 0){
				po = list.get(0);
			}
			return po;
		}else{
			return null;
		}		
	}
	
	/**
	 * 取得历史变更表中最新的版本号
	 * @return
	 */
	public static Long getNewVersionNo(Long dealerId){
		
		StringBuffer sb = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sb.append("SELECT NVL(MAX(T.VERSION)+1,1) AS VERSION FROM TM_DEALER_RECORD_CHANGE T ");
		if(null != dealerId && !"".equals(dealerId)){
			params.add(dealerId);
			sb.append(" WHERE T.DEALER_ID = ?");
		}
		
		List<TmDealerRecordChangePO> list = factory.select(sb.toString(), params,new DAOCallback<TmDealerRecordChangePO>() {
			public TmDealerRecordChangePO wrapper(ResultSet rs, int idx){
				TmDealerRecordChangePO po = new TmDealerRecordChangePO();
				try {
					po.setVersion(rs.getLong("VERSION"));						
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return po;
			}
		});
		
		if(list.size() > 0){
			return list.get(0).getVersion();
		}else{
			return null;
		}		
	}
	
	/**
	 * By 经销商记录ID查询出所属分销商记录
	 * @param recordId
	 * @return
	 */
	public static List<TmShuntDealerPO> findAllShuntDealers(Long recordId){
		
		StringBuffer sb = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sb.append("SELECT T.RECORD_ID,T.SHUNT_NAME,T.ADDRESS,T.PHONE,T.ZIP_CODE,T.FAX_NO,T.LINK_MAN,");
		sb.append("T.COM_LEVEL,T.COOPERATION_NATURE,T.CREATE_DATE,T.CREATE_BY,T.UPDATE_DATE,T.UPDATE_BY ");
		sb.append(" FROM TM_SHUNT_DEALER T WHERE 1=1 ");
		
		if(null != recordId && !"".equals(recordId)){
			params.add(recordId);
			sb.append(" AND T.RECORD_ID = ? ");
		}		
		
		return factory.select(sb.toString(), params,new DAOCallback<TmShuntDealerPO>() {
			public TmShuntDealerPO wrapper(ResultSet rs, int idx){
				TmShuntDealerPO po = new TmShuntDealerPO();
				try {
					po.setRecordId(rs.getLong("RECORD_ID"));	
					po.setShuntName(rs.getString("SHUNT_NAME"));
					po.setAddress(rs.getString("ADDRESS"));
					po.setPhone(rs.getString("PHONE"));
					po.setZipCode(rs.getString("ZIP_CODE"));
					po.setFaxNo(rs.getString("FAX_NO"));
					po.setLinkMan(rs.getString("LINK_MAN"));
					po.setComLevel(rs.getString("COM_LEVEL"));
					po.setCooperationNature(rs.getString("COOPERATION_NATURE"));
					po.setCreateBy(rs.getLong("CREATE_BY"));
					po.setCreateDate(rs.getTimestamp("CREATE_DATE"));
					po.setUpdateBy(rs.getLong("UPDATE_BY"));
					po.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return po;
			}
		});
	}
}
