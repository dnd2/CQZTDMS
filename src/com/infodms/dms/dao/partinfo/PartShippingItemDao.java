package com.infodms.dms.dao.partinfo;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.partsmanage.common.PartSqlUtil;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class PartShippingItemDao extends BaseDao<PO> {

	private static final PartShippingItemDao dao = new PartShippingItemDao();
	
	public static final PartShippingItemDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	* @Title: queryShippingItem 
	* @Description: TODO(根据货运单号查询货运单明细) 
	 */
	public List<Map<String, Object>> getShippingItem(String doNo) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.ITEM_ID, A.DO_NO, A.PART_CODE, A.PART_NAME, A.COUNT, B.UNIT, B.PART_ID, C.ORDER_COUNT, D.SIGN_QUANTITY FROM TT_PT_SHIPPINGSHEETITEM A, TM_PT_PART_BASE B, TT_PT_ORDITEM C, TT_PT_DLR_SIGN_DETAIL D");
		sql.append(" WHERE A.DO_NO = '").append(doNo).append("'");
		sql.append(" AND A.PART_CODE = B.PART_CODE");
		sql.append(" AND B.PART_ID = C.PART_ID");
		sql.append(" AND B.PART_ID = D.PART_ID");
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * 
	* @Title: getShippingItem 
	* @Description: TODO(根据采购订单号和货运单号查询货运单明细) 
	* @param @param doNo (货运单号), orderId(采购订单ID)
	 */
//	public List<Map<String, Object>> getShippingItem(String doNo, long orderId) {
//		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT A.ITEM_ID, A.DO_NO, A.PART_CODE, A.PART_NAME, A.COUNT, B.UNIT, B.PART_ID, C.ORDER_COUNT, D.SIGN_QUANTITY, E.CLAIM_COUNT, E.CLAIM_ID, E.CLAIM_TYPE_ID, E.REMARK ");
//		sql.append(" FROM TT_PT_SHIPPINGSHEETITEM A, TM_PT_PART_BASE B, TT_PT_ORDITEM C, TT_PT_DLR_SIGN_DETAIL D, TT_PT_CLAIM_ITEM E");
//		sql.append(" WHERE A.DO_NO = '").append(doNo).append("'");
//		sql.append(" AND A.PART_CODE = B.PART_CODE");
//		sql.append(" AND B.PART_ID = C.PART_ID");
//		sql.append(" AND B.PART_ID = D.PART_ID");
//		sql.append(" AND B.PART_ID = E.PART_ID(+)");
//		sql.append(" AND C.ORDER_ID = ").append(orderId);
//		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
//		return ps;
//	}
	
//	public Map<String, Object> getShippingItem(long partId, long orderId) {
//		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT A.ITEM_ID, A.DO_NO, A.PART_CODE, A.PART_NAME, A.COUNT, B.UNIT, B.PART_ID, C.ORDER_COUNT, D.SIGN_QUANTITY, E.CLAIM_COUNT, E.CLAIM_ID, E.CLAIM_TYPE_ID, E.REMARK ");
//		sql.append(" FROM TT_PT_SHIPPINGSHEETITEM A, TM_PT_PART_BASE B, TT_PT_ORDITEM C, TT_PT_DLR_SIGN_DETAIL D, TT_PT_CLAIM_ITEM E");
//		sql.append(" WHERE B.PART_ID = ").append(partId).append("\n");
//		sql.append(" AND C.ORDER_ID = ").append(orderId);
//		sql.append(" AND A.PART_CODE = B.PART_CODE").append("\n");
//		sql.append(" AND B.PART_ID = C.PART_ID").append("\n");
//		sql.append(" AND B.PART_ID = D.PART_ID").append("\n");
//		sql.append(" AND B.PART_ID = E.PART_ID(+)");
//		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
//		return ps;
//	}
	/**
	* @Title: getSignDetail 
	* @Description: TODO(根据签收单号查询签收明细,订单明细,货运单明细,索赔单明细) 
	* @param @param signNo (签收单号), claimId (索赔单Id)
	 */
	public List<Map<String, Object>> getSignDetail(String signNo, String claimId) {
		StringBuffer sql = new StringBuffer();
		//查询的字段
		sql.append("SELECT A.PART_ID, A.PART_CODE, A.PART_NAME, A.UNIT, A.SIGN_QUANTITY, A.ORDER_ID, \n");
		sql.append(" NVL(B.COUNT,0)COUNT, NVL(C.ORDER_COUNT,0)ORDER_COUNT, D.CLAIM_COUNT, D.REMARK, D.CLAIM_TYPE_ID FROM \n");
		//根据签收单号查询签收明细项
		sql.append(" (SELECT T1.PART_ID, T1.PART_CODE, T1.PART_NAME, T1.UNIT, T2.SIGN_QUANTITY, T3.DO_NO, T3.ORDER_ID \n");
		sql.append(" FROM TM_PT_PART_BASE T1, TT_PT_DLR_SIGN_DETAIL T2, TT_PT_DLR_SIGN T3 \n");
		sql.append(" WHERE T1.PART_ID = T2.PART_ID \n");
		sql.append(" AND T3.Sign_No = '").append(signNo).append("'\n");
		sql.append(" AND T3.SIGN_ID = T2.SIGN_ID \n");
		sql.append(" AND T3.IS_DEL = 0) A, \n");
		//查询货运单明细项
		sql.append(" (SELECT TS1.PART_CODE, TS2.DO_NO, TS1.COUNT \n");
		sql.append(" FROM TT_PT_SHIPPINGSHEETITEM TS1, TT_PT_SHIPPINGSHEET TS2 \n");
		sql.append(" WHERE TS1.DO_NO = TS2.DO_NO) B, \n");
		//查询采购订单明细项
		sql.append(" (SELECT TP1.ORDER_ID, TP2.ORDER_COUNT, TP2.PART_ID \n");
		sql.append(" FROM TT_PT_ORDER TP1, TT_PT_ORDITEM TP2 \n");
		sql.append(" WHERE TP1.ORDER_ID = TP2.ORDER_ID \n");
		sql.append(" AND TP1.IS_DEL = 0) C, \n");
		//查询索赔单明细项
		sql.append(" (SELECT TC1.ORDER_ID, TC2.PART_ID, TC2.CLAIM_COUNT, TC2.REMARK, TC2.CLAIM_TYPE_ID \n");
		sql.append(" FROM TT_PT_CLAIM TC1, TT_PT_CLAIM_ITEM TC2 \n");
		sql.append(" WHERE TC1.CLAIM_ID = TC2.CLAIM_ID \n");
		sql.append(" AND TC1.IS_DEL = 0) D \n");
		//查询条件
		sql.append(" WHERE A.PART_CODE = B.PART_CODE(+) \n");
		sql.append(" AND A.DO_NO = B.DO_NO(+) \n");
		sql.append(" AND A.ORDER_ID = C.ORDER_ID(+) \n");
		sql.append(" AND A.PART_ID = C.PART_ID(+) \n");
		if ("".equals(claimId)) {
			//没有点过保存
			sql.append(" AND A.ORDER_ID = D.ORDER_ID(+) \n");
			sql.append(" AND A.PART_ID = D.PART_ID(+) \n");
		} else {
			//已经索赔过
			sql.append(" AND A.ORDER_ID = D.ORDER_ID \n");
			sql.append(" AND A.PART_ID = D.PART_ID \n");
		}
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * 
	* @Title: getSignDetail 
	* @Description: TODO(根据签收单号和配件ID查询签收明细,订单明细,货运单明细,索赔单明细) 
	* @param @param signNo 签收单号
	* @param @param partId 配件ID
	 */
	public Map<String, Object> getSignDetail(String signNo, long partId, String claimId) {
		StringBuffer sql = new StringBuffer();
		//查询的字段
		sql.append("SELECT A.PART_ID, A.PART_CODE, A.PART_NAME, A.UNIT, A.SIGN_QUANTITY, A.ORDER_ID, \n");
		sql.append(" NVL(B.COUNT,0)COUNT, NVL(C.ORDER_COUNT,0)ORDER_COUNT, D.CLAIM_COUNT, D.REMARK, D.CLAIM_TYPE_ID FROM \n");
		//根据签收单号查询签收明细项
		sql.append(" (SELECT T1.PART_ID, T1.PART_CODE, T1.PART_NAME, T1.UNIT, T2.SIGN_QUANTITY, T3.DO_NO, T3.ORDER_ID \n");
		sql.append(" FROM TM_PT_PART_BASE T1, TT_PT_DLR_SIGN_DETAIL T2, TT_PT_DLR_SIGN T3 \n");
		sql.append(" WHERE T1.PART_ID = T2.PART_ID \n");
		sql.append(" AND T3.Sign_No = '").append(signNo).append("'\n");
		sql.append(" AND T3.SIGN_ID = T2.SIGN_ID \n");
		sql.append(" AND T3.IS_DEL = 0) A, \n");
		//查询货运单明细项
		sql.append(" (SELECT TS1.PART_CODE, TS2.DO_NO, TS1.COUNT \n");
		sql.append(" FROM TT_PT_SHIPPINGSHEETITEM TS1, TT_PT_SHIPPINGSHEET TS2 \n");
		sql.append(" WHERE TS1.DO_NO = TS2.DO_NO) B, \n");
		//查询采购订单明细项
		sql.append(" (SELECT TP1.ORDER_ID, TP2.ORDER_COUNT, TP2.PART_ID \n");
		sql.append(" FROM TT_PT_ORDER TP1, TT_PT_ORDITEM TP2 \n");
		sql.append(" WHERE TP1.ORDER_ID = TP2.ORDER_ID \n");
		sql.append(" AND TP1.IS_DEL = 0) C, \n");
		//查询索赔单明细项
		sql.append(" (SELECT TC1.ORDER_ID, TC2.PART_ID, TC2.CLAIM_COUNT, TC2.REMARK, TC2.CLAIM_TYPE_ID \n");
		sql.append(" FROM TT_PT_CLAIM TC1, TT_PT_CLAIM_ITEM TC2 \n");
		sql.append(" WHERE TC1.CLAIM_ID = TC2.CLAIM_ID \n");
		sql.append(" AND TC1.IS_DEL = 0) D \n");
		//查询条件
		sql.append(" WHERE A.PART_CODE = B.PART_CODE(+) \n");
		sql.append(" AND A.DO_NO = B.DO_NO(+) \n");
		sql.append(" AND A.ORDER_ID = C.ORDER_ID(+) \n");
		sql.append(" AND A.PART_ID = C.PART_ID(+) \n");
		sql.append(" AND A.ORDER_ID = D.ORDER_ID(+) \n");
		sql.append(" AND A.PART_ID = D.PART_ID(+) \n");
		sql.append(" AND A.PART_ID = ").append(partId);
		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	
//	public PageResult<Map<String, Object>> getClaimItem(long claimId, long orderId, int curPage, int pageSize) {
//		StringBuffer sql = new StringBuffer();
//		sql.append(" SELECT A.PART_ID, A.CLAIM_COUNT, A.CLAIM_TYPE_ID, B.PART_CODE, B.PART_NAME, B.UNIT, C.ORDER_COUNT, \n");
//		sql.append(" D.COUNT, E.SIGN_QUANTITY, F.CLAIM_TYPE_NAME \n");
//		sql.append(" FROM TT_PT_CLAIM_ITEM A, TM_PT_PART_BASE B, TT_PT_ORDITEM C, \n");
//		sql.append(PartSqlUtil.genSqlByOrderId(orderId)).append(" D,");
//		sql.append(" TT_PT_DLR_SIGN_DETAIL E, TM_PT_CLAIM_TYPE F \n");
//		sql.append(" WHERE A.CLAIM_ID = ").append(claimId);
//		sql.append(" AND C.ORDER_ID = ").append(orderId);
//		sql.append(" AND A.PART_ID = B.PART_ID \n");
//		sql.append(" AND A.PART_ID = C.PART_ID \n");
//		sql.append(" AND B.PART_CODE = D.PART_CODE \n");
//		sql.append(" AND A.PART_ID = E.PART_ID \n");
//		sql.append(" AND A.CLAIM_TYPE_ID = F.CLAIM_TYPE_ID \n");
//		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
//		return ps;
//	} 
	
	public PageResult<Map<String, Object>> getClaimItem(String signNo, String claimId, int curPage, int pageSize) {
		StringBuffer sql = new StringBuffer();
		//查询的字段
		sql.append("SELECT A.PART_ID, A.PART_CODE, A.PART_NAME, A.UNIT, A.SIGN_QUANTITY, A.ORDER_ID, \n");
		sql.append(" NVL(B.COUNT,0)COUNT, NVL(C.ORDER_COUNT,0)ORDER_COUNT, D.CLAIM_COUNT, D.REMARK, D.CLAIM_TYPE_NAME FROM \n");
		//根据签收单号查询签收明细项
		sql.append(" (SELECT T1.PART_ID, T1.PART_CODE, T1.PART_NAME, T1.UNIT, T2.SIGN_QUANTITY, T3.DO_NO, T3.ORDER_ID \n");
		sql.append(" FROM TM_PT_PART_BASE T1, TT_PT_DLR_SIGN_DETAIL T2, TT_PT_DLR_SIGN T3 \n");
		sql.append(" WHERE T1.PART_ID = T2.PART_ID \n");
		sql.append(" AND T3.Sign_No = '").append(signNo).append("'\n");
		sql.append(" AND T3.SIGN_ID = T2.SIGN_ID \n");
		sql.append(" AND T3.IS_DEL = 0) A, \n");
		//查询货运单明细项
		sql.append(" (SELECT TS1.PART_CODE, TS2.DO_NO, TS1.COUNT \n");
		sql.append(" FROM TT_PT_SHIPPINGSHEETITEM TS1, TT_PT_SHIPPINGSHEET TS2 \n");
		sql.append(" WHERE TS1.DO_NO = TS2.DO_NO) B, \n");
		//查询采购订单明细项
		sql.append(" (SELECT TP1.ORDER_ID, TP2.ORDER_COUNT, TP2.PART_ID \n");
		sql.append(" FROM TT_PT_ORDER TP1, TT_PT_ORDITEM TP2 \n");
		sql.append(" WHERE TP1.ORDER_ID = TP2.ORDER_ID \n");
		sql.append(" AND TP1.IS_DEL = 0) C, \n");
		//查询索赔单明细项
		sql.append(" (SELECT TC1.ORDER_ID, TC2.PART_ID, TC2.CLAIM_COUNT, TC2.REMARK, TC3.CLAIM_TYPE_NAME \n");
		sql.append(" FROM TT_PT_CLAIM TC1, TT_PT_CLAIM_ITEM TC2, TM_PT_CLAIM_TYPE TC3 \n");
		sql.append(" WHERE TC1.CLAIM_ID = TC2.CLAIM_ID \n");
		sql.append(" AND TC2.CLAIM_TYPE_ID = TC3.CLAIM_TYPE_ID \n");
		sql.append(" AND TC1.IS_DEL = 0) D \n");
		//查询条件
		sql.append(" WHERE A.PART_CODE = B.PART_CODE(+) \n");
		sql.append(" AND A.DO_NO = B.DO_NO(+) \n");
		sql.append(" AND A.ORDER_ID = C.ORDER_ID(+) \n");
		sql.append(" AND A.PART_ID = C.PART_ID(+) \n");
		if ("".equals(claimId)) {
			//没有点过保存
			sql.append(" AND A.ORDER_ID = D.ORDER_ID(+) \n");
			sql.append(" AND A.PART_ID = D.PART_ID(+) \n");
		} else {
			//已经索赔过
			sql.append(" AND A.ORDER_ID = D.ORDER_ID \n");
			sql.append(" AND A.PART_ID = D.PART_ID \n");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

}
