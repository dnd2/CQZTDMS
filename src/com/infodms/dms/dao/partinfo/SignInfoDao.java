package com.infodms.dms.dao.partinfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.PartinfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: SignInfoDao.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-29
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class SignInfoDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(SignInfoDao.class);
	private static final SignInfoDao dao = new SignInfoDao ();
	public static final SignInfoDao getInstance() {
		return dao;
	}
	protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public PageResult<Map<String, Object>> querySignInfoList(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT V1.PART_CODE, V1.PART_NAME, V1.UNIT, V1.SIGN_QUANTITY, NVL(V2.COUNT, 0) COUNT, NVL(V3.ORDER_COUNT, 0) ORDER_COUNT\n" );
		sql.append("  FROM (SELECT A.PART_ID,\n" );
		sql.append("               C.PART_CODE,\n" );
		sql.append("               C.PART_NAME,\n" );
		sql.append("               C.UNIT,\n" );
		sql.append("               A.SIGN_QUANTITY,\n" );
		sql.append("               B.DO_NO\n" );
		sql.append("          FROM TT_PT_DLR_SIGN_DETAIL A, TT_PT_DLR_SIGN B, TM_PT_PART_BASE C\n" );
		sql.append("         WHERE A.SIGN_ID = B.SIGN_ID\n" );
		sql.append("		   AND B.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("		   AND C.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("           AND A.PART_ID = C.PART_ID\n" );
		sql.append("           AND A.SIGN_ID = (SELECT SIGN_ID FROM TT_PT_DLR_SIGN WHERE DO_NO = '").append(bean.getDoNo()).append("'\n");
		sql.append("       ) ) V1,\n" );
		sql.append("       (SELECT B1.PART_CODE, B1.COUNT, A1.DO_NO\n" );
		sql.append("          FROM TT_PT_SHIPPINGSHEET A1, TT_PT_SHIPPINGSHEETITEM B1\n" );
		sql.append("         WHERE A1.DO_NO = B1.DO_NO\n" );
		sql.append("           AND A1.DO_NO = '").append(bean.getDoNo()).append("'\n");
		sql.append("       ) V2,\n" );
		sql.append("       (SELECT A2.PART_ID, A2.ORDER_COUNT\n" );
		sql.append("          FROM TT_PT_ORDITEM A2\n" );
		sql.append("         WHERE A2.ORDER_ID = ").append(bean.getOrderId()).append("\n");
		sql.append("     ) V3\n" );
		sql.append(" WHERE V1.PART_CODE = V2.PART_CODE(+)\n" );
		sql.append("   AND V1.PART_ID = V3.PART_ID(+)");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

/*	public PageResult<Object> querySignInfoList(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT C.SIGN_QUANTITY, D.PART_ID, D.PART_CODE, D.PART_NAME, D.UNIT, A.ORDER_ID, A.DO_NO\n" );
		sql.append("FROM TT_PT_DLR_SIGN A, TT_PT_SHIPPINGSHEET B, TT_PT_DLR_SIGN_DETAIL C, TM_PT_PART_BASE D\n" );
		sql.append("WHERE A.DO_NO = B.DO_NO\n" );
		sql.append("AND A.SIGN_ID = C.SIGN_ID\n" );
		sql.append("AND C.PART_ID = D.PART_ID\n" );
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND B.IS_SIGNED = ").append(Constant.IS_SIGNED_01).append("\n"); 
		sql.append("AND D.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND B.DO_NO = ").append(bean.getDoNo()); 
		PageResult<Object> ps = pageQueryObject(sql.toString(), null, pageSize, curPage);
		return ps;
	}
	
	protected Object wrapperObject(ResultSet rs, int idx) {
		PartinfoBean bean = new PartinfoBean();
		try {
			bean.setSignCount(rs.getString("SIGN_QUANTITY"));
			bean.setPartId(rs.getString("PART_ID"));
			bean.setPartCode(rs.getString("PART_CODE"));
			bean.setPartName(rs.getString("PART_NAME"));
			bean.setUnit(rs.getString("UNIT"));
			bean.setOrderId(rs.getString("ORDER_ID"));
			bean.setDoNo(rs.getString("DO_NO"));
			Map<String, Object> map = getQuantity(rs.getString("ORDER_ID"),rs.getString("PART_ID"),rs.getString("DO_NO"), rs.getString("PART_CODE"));
			if(map != null && map.size()>0){
				bean.setOrderCount(map.get("ORDER_COUNT")==null?"0":String.valueOf(map.get("ORDER_COUNT")));
				bean.setDoCount(map.get("COUNT")==null?"0":String.valueOf(map.get("COUNT")));
			}else{
				bean.setOrderCount("0");
				bean.setDoCount("0");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public Map<String, Object> getQuantity(String orderId, String partId, String doNo, String partCode){
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT COUNT\n" );
		sql.append("FROM TT_PT_SHIPPINGSHEETITEM\n" );
		sql.append("WHERE DO_NO = '").append(doNo).append("'\n"); 
		sql.append("AND PART_CODE = '").append(partCode).append("'\n"); 
		Map<String, Object> fmap = pageQueryMap(sql.toString(), null, getFunName());
		
		StringBuffer sql2= new StringBuffer();
		sql2.append("SELECT ORDER_COUNT\n" );
		sql2.append("FROM TT_PT_ORDITEM\n" );
		sql2.append("WHERE ORDER_ID = ").append(orderId).append("\n");
		sql2.append("AND PART_ID = ").append(partId).append("\n");
		Map<String, Object> smap = pageQueryMap(sql2.toString(), null, getFunName());
		
		if(fmap != null){
			map.putAll(fmap);
		}
		if(smap != null){
			map.putAll(smap);
		}
		return map;
	}*/
}
