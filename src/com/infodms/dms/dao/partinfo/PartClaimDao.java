package com.infodms.dms.dao.partinfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.PartClaimBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPtClaimPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PageResult;
/**
 * 
* @ClassName: PartClaimApply 
* @Description: TODO(配件索赔申请) 
* @author liuqiang 
* @date Jun 13, 2010 10:34:15 AM 
*
 */
public class PartClaimDao extends BaseDao<TtPtClaimPO> {
	
	public static final Logger logger = Logger.getLogger(PartClaimDao.class);
	private static final PartClaimDao dao = new PartClaimDao();
	
	public static final PartClaimDao getInstance() {
		return dao;
	}
	
	@Override
	protected TtPtClaimPO wrapperPO(ResultSet rs, int idx) {
		TtPtClaimPO po = new TtPtClaimPO();
		try {
			po.setClaimId(rs.getLong("CLAIM_ID"));
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
		return po;
	}
	
	public TtPtClaimPO queryClaimPO(TtPtClaimPO po) {
		List<TtPtClaimPO> pos = select(po, 0);
		return pos.size() > 0 ? pos.get(0) : null;
	}
	
	//分页查询货运单
//	public PageResult<Map<String, Object>> partClaimApplyQuery(PartClaimBean bean, int curPage, int pageSize) {		
//		StringBuffer sql = new StringBuffer("");
//		sql.append(" SELECT A.ORDER_NO, A.ORDER_ID, A.TRANS_TYPE, TO_CHAR(D.SIGN_DATE, 'YYYY-MM-DD') SIGN_DATE, D.SIGN_COUNT, D.DO_NO, E.SIGN_NO \n");
//		sql.append(" FROM TT_PT_ORDER A, \n");
//		sql.append(" (SELECT B.DO_NO, B.CARTON_COUNT, B.SIGN_DATE, B.SIGN_COUNT, C.ORDER_NO FROM TT_PT_SHIPPINGSHEET B, TT_PT_SALES C where B.SO_NO = B.SO_NO) D, \n");
//		sql.append("  TT_PT_DLR_SIGN E \n") ;
//		sql.append(" WHERE A.ORDER_NO = D.ORDER_NO \n");
//		sql.append(" AND E.DO_NO = D.DO_NO \n");
//		sql.append(" AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
//		sql.append(" AND E.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
//		sql.append(" AND A.DEALER_ID = " + bean.getDealerId());
//		if (Utility.testString(bean.getSighNo())) {
//			sql.append(" AND E.SIGN_NO LIKE '%").append(bean.getSighNo()).append("%' \n");
//		}
//		if (Utility.testString(bean.getOrderNo())) {
//			sql.append(" AND A.ORDER_NO LIKE '%").append(bean.getOrderNo()).append("%' \n");
//		}
//		if (Utility.testString(bean.getDoNo())) {
//			sql.append(" AND D.DO_NO LIKE '%").append(bean.getDoNo()).append("%' \n");
//		}
//		if (Utility.testString(bean.getBeginDate())) {
//			sql.append(" AND D.SIGN_DATE >= TO_DATE('").append(bean.getBeginDate().trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
//		}
//		if (Utility.testString(bean.getEndDate())) {
//			sql.append(" AND D.SIGN_DATE <= TO_DATE('").append(bean.getEndDate().trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
//		}
//		System.out.println(sql.toString());
//		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
//		return ps;
//	}
	//分页查询签收单
	public PageResult<Map<String, Object>> partClaimApplyQuery(PartClaimBean bean, int curPage, int pageSize) {		
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.SIGN_NO, TO_CHAR(B.SIGN_DATE, 'YYYY-MM-DD')SIGN_DATE, B.DO_NO, \n");
		sql.append(" B.SHIPPING_CONDITION, B.SIGN_COUNT, D.ORDER_NO, E.CLAIM_ID, F.ORDER_ID \n");
		sql.append(" FROM TT_PT_DLR_SIGN A, TT_PT_SHIPPINGSHEET B, \n");
		sql.append(" (SELECT DO_NO, C.ORDER_NO FROM TT_PT_SHIPPINGSHEET T,  \n");
		sql.append(" (SELECT SO_NO, ORDER_NO FROM TT_PT_SALES \n");
		sql.append(" WHERE ORDER_NO IN (SELECT ORDER_NO FROM TT_PT_ORDER))C  \n") ;
		sql.append(" WHERE T.SO_NO = C.SO_NO)D, \n");
		sql.append(" TT_PT_CLAIM E, TT_PT_ORDER F \n");
		sql.append(" WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		//sql.append(" AND E.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append(" AND A.DO_NO = D.DO_NO \n");
		sql.append(" AND A.DO_NO = B.DO_NO \n");
		sql.append(" AND A.DEALER_ID = " + bean.getDealerId()).append("\n");
		sql.append(" AND A.DO_NO = E.DO_NO(+)");
		sql.append(" AND D.ORDER_NO = F.ORDER_NO");
		if (Utility.testString(bean.getSighNo())) {
			sql.append(" AND A.SIGN_NO LIKE '%").append(bean.getSighNo()).append("%' \n");
		}
		if (Utility.testString(bean.getOrderNo())) {
			sql.append(" AND D.ORDER_NO LIKE '%").append(bean.getOrderNo()).append("%' \n");
		}
		if (Utility.testString(bean.getDoNo())) {
			sql.append(" AND B.DO_NO LIKE '%").append(bean.getDoNo()).append("%' \n");
		}
		if (Utility.testString(bean.getBeginDate())) {
			sql.append(" AND B.SIGN_DATE >= TO_DATE('").append(bean.getBeginDate().trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(bean.getEndDate())) {
			sql.append(" AND B.SIGN_DATE <= TO_DATE('").append(bean.getEndDate().trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		sql.append(" AND E.STATUS IS NULL");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//分页查询索赔申请单
	public PageResult<Map<String, Object>> partClaimCheckQuery(PartClaimBean bean, int curPage, int pageSize) {
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT A.CLAIM_ID, A.CLAIM_NO, A.DEALER_ID, A.STATUS, A.DO_NO, \n");
		sql.append(" TO_CHAR(A.APPLY_DATE, 'YYYY-MM-DD')APPLY_DATE, B.DEALER_NAME, TO_CHAR(C.SIGN_DATE, 'YYYY-MM-DD')SIGN_DATE, C.SIGN_COUNT, D.SIGN_NO, E.CLAIM_COUNT \n");
		sql.append(" FROM TT_PT_CLAIM A, TM_DEALER B, TT_PT_SHIPPINGSHEET C, TT_PT_DLR_SIGN D, \n");
		sql.append("( SELECT D.CLAIM_ID, COUNT(D.CLAIM_ID) CLAIM_COUNT FROM TT_PT_CLAIM_ITEM D GROUP BY D.CLAIM_ID) E \n");
		sql.append(" WHERE A.DEALER_ID = B.DEALER_ID \n");
		sql.append(" AND A.DO_NO = C.DO_NO \n");
		sql.append(" AND D.DO_NO = C.DO_NO \n");
		sql.append(" AND A.CLAIM_ID = E.CLAIM_ID \n");
		if (Utility.testString(bean.getDealerCodes())) {
			//车厂查询页面
			sql.append(" AND B.DEALER_CODE IN (").append(StringUtil.compileStr(bean.getDealerCodes())).append(") \n");
		} else {
			//经销商查询页面
			if (null != bean.getDealerId()) {
				sql.append(" AND A.DEALER_ID = ").append(bean.getDealerId()).append(" \n");
			}
		}
		if (Utility.testString(bean.getDealerName())) {
			sql.append(" AND B.DEALER_NAME LIKE '%").append(bean.getDealerName()).append("%'\n");
		}
		//按区域查询
		if (Utility.testString(bean.getOrgCodes())) {
			sql.append(" AND B.DEALER_ID IN ( \n");
			sql.append(" SELECT DEALER_ID FROM TM_DEALER_ORG_RELATION WHERE ORG_ID IN( \n");
			sql.append(" SELECT ORG_ID FROM TM_ORG WHERE ORG_CODE IN (\n");
			sql.append(StringUtil.compileStr(bean.getOrgCodes())).append("))) \n");
		}
		if (Utility.testString(bean.getClaimNo())) {
			sql.append(" AND A.CLAIM_NO LIKE '%").append(bean.getClaimNo()).append("%'");
		}
		if (Utility.testString(bean.getDoNo())) {
			sql.append(" AND A.DO_NO LIKE '%").append(bean.getDoNo()).append("%'");
		}
		if (Utility.testString(bean.getBeginDate())) {
			sql.append(" AND A.APPLY_DATE >= TO_DATE('").append(bean.getBeginDate().trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(bean.getEndDate())) {
			sql.append(" AND A.APPLY_DATE <= TO_DATE('").append(bean.getEndDate().trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(bean.getCheckStatus())) {
			//查询全部
			if (Constant.PART_CLAIM_STATUS_01 != Integer.parseInt(bean.getCheckStatus())) {
				sql.append(" AND A.STATUS = ").append(Integer.parseInt(bean.getCheckStatus()));
			}
		} else {
			sql.append(" AND A.STATUS = ").append(Constant.PART_CLAIM_STATUS_02);
		}
		sql.append(" ORDER BY A.CLAIM_ID DESC");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public Map<String, Object> partClaimDetail(PartClaimBean bean) {
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT A.CLAIM_ID, A.CLAIM_NO, A.DEALER_ID, A.DO_NO, \n");
		sql.append(" TO_CHAR(A.APPLY_DATE, 'YYYY-MM-DD')APPLY_DATE, B.DEALER_NAME, B.DEALER_CODE, TO_CHAR(C.SIGN_DATE, 'YYYY-MM-DD')SIGN_DATE, D.ORDER_NO, D.ORDER_ID, E.SIGN_NO \n");
		sql.append(" FROM TT_PT_CLAIM A, TM_DEALER B, TT_PT_SHIPPINGSHEET C, TT_PT_ORDER D, TT_PT_DLR_SIGN E \n");
		sql.append(" WHERE A.DEALER_ID = B.DEALER_ID \n");
		sql.append(" AND A.DO_NO = C.DO_NO \n");
		sql.append(" AND A.CLAIM_ID = ").append(bean.getClaimId()).append("\n");
		sql.append(" AND A.ORDER_ID = D.ORDER_ID \n");
		sql.append(" AND E.DO_NO = C.DO_NO");
		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	
	public Map<String, Object> getClaimItemsCount(int claimId) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT COUNT(A.ITEM_ID) C_ITEM_ID FROM TT_PT_CLAIM_ITEM A");
		sql.append(" WHERE A.CLAIM_ID = ").append(claimId);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}

}
