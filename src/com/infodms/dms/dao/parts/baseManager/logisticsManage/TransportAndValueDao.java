package com.infodms.dms.dao.parts.baseManager.logisticsManage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.parts.baseManager.mainData.mainDataMaintenance;
import com.infodms.dms.bean.ActMater;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesLogiAreaPO;
import com.infodms.dms.po.TtSalesLogiPO;
import com.infodms.dms.po.TtTransportValuationPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

/**
 * 运输方式与计价维护dao
 * @author hyy 
 * @version 2017-7-6
 * @see 
 * @since 
 */
public class TransportAndValueDao  extends BaseDao<PO>{

	private static final TransportAndValueDao dao = new TransportAndValueDao();

	public static final TransportAndValueDao getInstance() {
		return dao;
	}
	private TransportAndValueDao() {}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * 根据运输方式code和计价方式code查询关系是否存在
	 * @param transportCode
	 * @param valuationCode
	 * @return
	 */
	public List<TtTransportValuationPO> selTtransportValuaByCode(String transportCode,String valuationCode,String tvId) {
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT TV_ID FROM TT_TRANSPORT_VALUATION WHERE TRANSPORT_CODE='"+transportCode+"' AND VALUATION_CODE='"+valuationCode+"'");
		if(tvId!=null && !tvId.equals("")){
			sql.append(" AND TV_ID !='"+tvId+"'");
		}
		return factory.select(sql.toString(), null,
				new DAOCallback<TtTransportValuationPO>() {
					public TtTransportValuationPO wrapper(ResultSet rs, int idx) {
						TtTransportValuationPO bean = new TtTransportValuationPO();
						try {
							bean.setTvId(rs.getString("TV_ID"));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				});
	}
	
	/**
	 * 根据序号id查询运输方式和计价方式
	 * @param tvId
	 * @return
	 */
	public Map<String, Object> selTtransportValuaByTvId(String tvId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TV_ID,TV_NAME,TRANSPORT_CODE,VALUATION_CODE,STATUS FROM TT_TRANSPORT_VALUATION WHERE TV_ID='"+tvId+"' ");
		Map<String, Object> map = pageQueryMap(sql.toString(), null,
				getFunName());
		return map;
	}
	
	
	
	/**
	 * 查询运输方式和计价方式列表
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> selTtransportValua(Map<String, Object> map, int curPage, int pageSize)
			throws Exception {
		String transportCode = (String) map.get("TRANSPORT_CODE"); //运输方式
		String valuationCode = (String) map.get("VALUATION_CODE");//计价方式
		List<Object> params = new LinkedList<Object>();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TV_ID,TV_NAME,TRANSPORT_CODE,VALUATION_CODE,STATUS FROM TT_TRANSPORT_VALUATION WHERE 1=1 ");
		if (transportCode != null && !"".equals(transportCode)) {
			sql.append("   AND TRANSPORT_CODE  LIKE '%" + transportCode + "%'\n");
		}
		if (valuationCode != null && !"".equals(valuationCode)) {
			sql.append("   AND VALUATION_CODE  LIKE '%" + valuationCode+ "%'\n");
		}
		 sql.append(" ORDER BY UPDATE_DATE DESC");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(),
				params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 添加运输和计费方式
	 * @param ttTransportValuationPO
	 */
	public void transportValueAdd(TtTransportValuationPO ttTransportValuationPO) {
		dao.insert(ttTransportValuationPO);
	}
	
	/**
	 * 修改运输和计费方式
	 * @param tv_id 运输方式和计费方式关系表序号
	 */
	public int transportValueUpdate(TtTransportValuationPO po) {
		String sql="UPDATE TT_TRANSPORT_VALUATION SET TV_NAME='"+po.getTvName()+"',STATUS="+po.getStatus()+",UPDATE_DATE=sysdate,UPDATE_BY="+po.getUpdateBy()+" WHERE TV_ID='"+po.getTvId()+"' ";
		return dao.update(sql, null);
	}
	
	/**
	 * 查询运输方式和计价方式关系（用于运输方式维护下拉列表）
	 * @return
	 * @throws Exception
	 */
	 public List selTvRelation() throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT TV_ID,TV_NAME FROM TT_TRANSPORT_VALUATION WHERE STATUS=10011001 GROUP BY TV_ID,TV_NAME ORDER BY 1 ");

            return pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }
	 
	/**
	 * 状态失效statusDisable
	 * @param po
	 * @return
	 */
//	public int updStatusDisable(String tvId) {
//		String sql="UPDATE TT_TRANSPORT_VALUATION SET STATUS='10011002' WHERE TV_ID='"+tvId+"' ";
//		return dao.update(sql, null);
//	}
	
	/**
	 * 状态有效statusEnable
	 * @param po
	 * @return
	 */
//	public int updStatusEnable(String tvId) {
//		String sql="UPDATE TT_TRANSPORT_VALUATION SET STATUS='10011001' WHERE TV_ID='"+tvId+"' ";
//		return dao.update(sql, null);
//	}
}
