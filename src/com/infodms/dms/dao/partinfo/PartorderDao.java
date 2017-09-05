package com.infodms.dms.dao.partinfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PartinfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: PartorderDao.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-10
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class PartorderDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(PartinfoDao.class);
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private static final PartorderDao dao = new PartorderDao ();
	public static final PartorderDao getInstance() {
		return dao;
	}
	protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//分页查询采购订单
	public PageResult<Map<String, Object>> queryPartOrderList(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT  B.DEALER_CODE, B.DEALER_NAME, A.ORDER_ID, A.ORDER_NO, A.ORDER_STATUS,\n");
		sql.append("        TO_CHAR(A.REQUIRE_DATE, 'YYYY-MM-DD') REQUEST_DATE,\n" );
		sql.append("        A.TRANS_TYPE, TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE,\n");
		sql.append("        TO_CHAR(A.UPDATE_DATE, 'YYYY-MM-DD') UPDATE_DATE,\n" );
		sql.append("        CASE WHEN A.HIGH_OPERATION_TYPE = ").append(Constant.ORG_TYPE_OEM).append("\n"); 
		sql.append("        THEN (SELECT E.ORG_NAME FROM TM_ORG E,TT_PT_ORDER F WHERE F.OPERATION_ID = E.ORG_ID AND F.ORDER_ID = A.ORDER_ID)\n" );
		sql.append("        WHEN A.HIGH_OPERATION_TYPE = ").append(Constant.ORG_TYPE_DEALER).append("\n"); 
		sql.append("        THEN (SELECT E.DEALER_SHORTNAME FROM TM_DEALER E, TT_PT_ORDER F WHERE F.OPERATION_ID = E.DEALER_ID AND F.ORDER_ID = A.ORDER_ID)\n" );
		sql.append("        END ORG_NAME\n" );
		sql.append("FROM TT_PT_ORDER A, TM_DEALER B\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		if(!bean.getOrderNo().equals("")){
			sql.append("AND A.ORDER_NO LIKE '%").append(bean.getOrderNo()).append("%'\n");
		}
		if(!bean.getBeginTime().equals("")){
			sql.append("AND A.CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append(" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(!bean.getEndTime().equals("")){
			sql.append("AND A.CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append(" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(!bean.getOrderStatus().equals("")){
			sql.append("AND A.ORDER_STATUS = ").append(bean.getOrderStatus()).append("\n");
		}
		sql.append("AND A.ORDER_STATUS <> ").append(Constant.PART_ORDER_STATUS_01).append("\n");
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		List<Object> params = new LinkedList<Object>();
		if (Utility.testString(bean.getDealerCode())) {
			sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params, "B", "DEALER_CODE"));
			
		} 
		if (Utility.testString(bean.getDealerName())) {
			sql.append("AND B.DEALER_NAME like '%").append(bean.getDealerName()).append("%'\n");
		}

		//经销商只查询自己的采购订单
		if (null != logonUser.getDealerId()) {
			//经销商用户查询
			sql.append("AND A.DEALER_ID in (").append(logonUser.getDealerId()).append(")\n"); 
		}
		sql.append("ORDER BY A.ORDER_ID DESC \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//查询以保存的配件订单
	public PageResult<Map<String, Object>> queryOrderList(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.ORDER_ID, A.ORDER_NO, B.DC_NAME, C.NAME,\n" );
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE,\n" );
		sql.append("       TO_CHAR(A.REQUIRE_DATE, 'YYYY-MM-DD') REQUIRE_DATE,\n" );
		sql.append("       TO_CHAR(A.ORDER_PRICE, 'FM99,999,990.00') ORDER_PRICE, A.ORDER_STATUS,\n" );
		sql.append("       CASE WHEN A.HIGH_OPERATION_TYPE = ").append(Constant.ORG_TYPE_OEM).append("\n"); 
		sql.append("       THEN (SELECT E.ORG_NAME FROM TM_ORG E,TT_PT_ORDER F WHERE F.OPERATION_ID = E.ORG_ID AND F.ORDER_ID = A.ORDER_ID)\n" );
		sql.append("       WHEN A.HIGH_OPERATION_TYPE = ").append(Constant.ORG_TYPE_DEALER).append("\n"); 
		sql.append("       THEN (SELECT E.DEALER_SHORTNAME FROM TM_DEALER E, TT_PT_ORDER F WHERE F.OPERATION_ID = E.DEALER_ID AND F.ORDER_ID = A.ORDER_ID)\n" );
		sql.append("       END OPERACTION_NAME\n" );
		sql.append("FROM TT_PT_ORDER A,TM_PT_DC B, TC_USER C\n" );
		sql.append("WHERE A.DC_ID = B.DC_ID\n" );
		sql.append("AND A.CREATE_BY = C.USER_ID\n");
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND B.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND C.USER_STATUS = " ).append(Constant.STATUS_ENABLE).append("\n");
		if(!bean.getOrderNo().equals("")){
			sql.append("AND A.ORDER_NO LIKE '%").append(bean.getOrderNo()).append("%'\n");
		}
		if(!bean.getBeginTime().equals("")){
			sql.append("AND A.CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append(" 00:00:00','YYYY-MM-DD') \n");
		}
		if(!bean.getEndTime().equals("")){
			sql.append("AND A.CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append(" 23:59:59','YYYY-MM-DD') \n");
		}
		if(!bean.getOrderStatus().equals("")){
			sql.append("AND A.ORDER_STATUS = ").append(bean.getOrderStatus()).append("\n");
		}
		if(!bean.getDcId().equals("")){
			sql.append("AND A.DC_ID = ").append(bean.getDcId()).append("\n");
		}
		sql.append("AND A.ORDER_STATUS IN (").append(Constant.PART_ORDER_STATUS_01).append(",").append(Constant.PART_ORDER_STATUS_05).append(")\n"); 
		sql.append("AND A.DEALER_ID in (").append(bean.getDealerId()).append(")\n"); 
		sql.append("ORDER BY A.ORDER_ID DESC \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//根据订单号查询订单详细
	public Map<String, Object> getOrderInfo (String orderId){
		StringBuffer sql = new StringBuffer(""); 
		sql.append("SELECT A.ORDER_ID, A.ORDER_NO, B.DEALER_CODE, B.DEALER_NAME, A.TRANS_TYPE, A.ORDER_PRICE, A.DC_ID, \n");
		sql.append("       A.ORDER_STATUS, C.SO_NO, C.SALES_SUM, A.REMARK\n" );
		sql.append("FROM TT_PT_ORDER A, TM_DEALER B, TT_PT_SALES C\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("AND A.ORDER_NO = C.ORDER_NO(+)\n");
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND B.STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		sql.append("AND A.ORDER_ID = ").append(orderId);
		
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	//根据订单号查询订单详细
	public Map<String, Object> getOrderPartInfo(String orderId){
		StringBuffer sql = new StringBuffer(""); 
		sql.append("SELECT A.ORDER_ID, A.ORDER_NO, A.TRANS_TYPE, A.DEALER_ID, A.REMARK,\n" );
		sql.append("       TO_CHAR(A.REQUIRE_DATE, 'YYYY-MM-DD') REQUIRE_DATE,\n" );
		sql.append("       B.DC_NAME, TO_CHAR(NVL(C.AMOUNT, 0), 'FM99,999,990.00') AMOUNT\n" );
		sql.append("FROM TT_PT_ORDER A, TM_PT_DC B, TM_DEALER_DC_FINANCE C\n" );
		sql.append("WHERE A.DC_ID = B.DC_ID\n" );
		sql.append("AND A.DEALER_ID = C.DEALER_ID(+)\n" );
		sql.append("AND A.DC_ID = C.DC_ID(+)\n" );
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND NVL(B.IS_DEL, ").append(Constant.IS_DEL_00).append(") = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND NVL(C.IS_DEL, ").append(Constant.IS_DEL_00).append(") = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.ORDER_ID = ").append(orderId);
		
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	//根据订单号查询订单中的配件列表
	public PageResult<Object> getOrderPartList(String orderId,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID, A.PART_CODE, A.PART_NAME, A.UNIT, A.MINI_PACK, B.DEALER_ID,\n" );
		sql.append("       A.SALE_PRICE, B.DISCOUNT_RATE,\n" );
		sql.append("       A.SALE_PRICE*B.DISCOUNT_RATE DIS_PRICE,\n" );
		sql.append("       C.ORDER_COUNT, C.REMARK,\n" );
		sql.append("       A.SALE_PRICE*B.DISCOUNT_RATE*C.ORDER_COUNT COUNT_PRICE\n" );
		sql.append("FROM TM_PT_PART_BASE A, TT_PT_ORDER B, TT_PT_ORDITEM C\n" );
		sql.append("WHERE B.ORDER_ID = C.ORDER_ID\n" );
		sql.append("AND C.PART_ID = A.PART_ID\n" );
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND B.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND B.ORDER_ID = ").append(orderId);

		PageResult<Object> ps = pageQueryObject(sql.toString(), null, pageSize, curPage);
		return ps;
	}
	
	protected Object wrapperObject(ResultSet rs, int idx) {
		PartinfoBean bean = new PartinfoBean();
		try {
			bean.setPartId(rs.getString("PART_ID"));
			bean.setPartCode(rs.getString("PART_CODE"));
			bean.setPartName(rs.getString("PART_NAME"));
			bean.setUnit(rs.getString("UNIT"));
			bean.setMiniPack(rs.getString("MINI_PACK"));
			bean.setSalePrice(rs.getString("SALE_PRICE"));
			bean.setDiscountRate(rs.getString("DISCOUNT_RATE"));
			bean.setDisPrice(rs.getString("DIS_PRICE"));
			bean.setOrderCount(rs.getString("ORDER_COUNT"));
			bean.setRemark(rs.getString("REMARK"));
			bean.setOrderPrice(rs.getString("COUNT_PRICE"));
			Map<String, Object> map = getQuantity(rs.getString("DEALER_ID"),rs.getString("PART_ID"));
			if(map != null && map.size()>0){
				bean.setPaperQuantity(map.get("PAPER_QUANTITY")==null?"0":String.valueOf(map.get("PAPER_QUANTITY")));
				bean.setSafeQuantity(map.get("SAFE_QUANTITY")==null?"0":String.valueOf(map.get("SAFE_QUANTITY")));
				bean.setSecondPaperQuantity(map.get("SECOND_PAPER_QUANTITY")==null?"0":String.valueOf(map.get("SECOND_PAPER_QUANTITY")));
				bean.setSecondSafeQuantity(map.get("SECOND_SAFE_QUANTITY")==null?"0":String.valueOf(map.get("SECOND_SAFE_QUANTITY")));
			}else{
				bean.setPaperQuantity("0");
				bean.setSafeQuantity("0");
				bean.setSecondPaperQuantity("0");
				bean.setSecondSafeQuantity("0");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public Map<String, Object> getQuantity(String dealerId, String partId){
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.PAPER_QUANTITY, A.SAFE_QUANTITY\n" );
		sql.append("FROM TT_PT_DLRSTOCK A\n" );
		sql.append("WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.DEALER_ID = ").append(dealerId);
		sql.append("AND A.PART_ID = ").append(partId);
		Map<String, Object> fmap = pageQueryMap(sql.toString(), null, getFunName());
		
		StringBuffer sql2= new StringBuffer();
		sql2.append("SELECT A.PAPER_QUANTITY SECOND_PAPER_QUANTITY, A.SAFE_QUANTITY SECOND_SAFE_QUANTITY\n" );
		sql2.append("FROM TT_PT_DLRSTOCK A\n" );
		sql2.append("WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql2.append("AND A.DEALER_ID = (\n" );
		sql2.append("    SELECT DEALER_ID\n" );
		sql2.append("    FROM TM_DEALER\n" );
		sql2.append("    WHERE DEALER_LEVEL = ").append(Constant.DEALER_LEVEL_01).append("\n");
		sql2.append("    START WITH DEALER_ID = ").append(dealerId);
		sql2.append("    CONNECT BY PRIOR PARENT_DEALER_D = DEALER_ID\n" );
		sql2.append(")\n");
		sql2.append("AND A.PART_ID = ").append(partId);
		Map<String, Object> smap = pageQueryMap(sql2.toString(), null, getFunName());
		
		if(fmap != null){
			map.putAll(fmap);
		}
		if(smap != null){
			map.putAll(smap);
		}
		return map;
	}
	
	//根据订单号查询订单中的配件列表
	public List<Map<String, Object>> getPartInfo(String orderId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT B.PART_CODE, B.PART_NAME, B.UNIT, A.ORDER_COUNT,\n" );
		sql.append("       A.ONFITTING_COUNT, A.CARRYING_COUNT, A.RECEIVED_COUNT,\n" );
		sql.append("       C.DISCOUNT_RATE, B.SALE_PRICE\n" );
		sql.append("FROM TT_PT_ORDITEM A, TM_PT_PART_BASE B, TT_PT_ORDER C\n" );
		sql.append("WHERE C.ORDER_ID = A.ORDER_ID\n" );
		sql.append("AND A.PART_ID = B.PART_ID\n");
		sql.append("AND C.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND B.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND C.ORDER_ID = ").append(orderId).append("\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//根据订单号查询订单审核流程
	public List<Map<String, Object>> getOrderLog(String orderId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT ROWNUM NUM, TO_CHAR(A.OPERTATE_DATE, 'YYYY-MM-DD') OPERTATE_DATE,\n" );
		sql.append("        A.NODE_STATUS,\n" );
		sql.append("        CASE WHEN A.ORG_TYPE = ").append(Constant.ORG_TYPE_OEM).append("\n"); 
		sql.append("        THEN (SELECT E.ORG_NAME FROM TM_ORG E,TT_PT_ORDER_LOG F WHERE F.OGR_ID = E.ORG_ID AND F.ORDER_ID = A.ORDER_ID)\n" );
		sql.append("        WHEN A.ORG_TYPE = ").append(Constant.ORG_TYPE_DEALER).append("\n"); 
		sql.append("        THEN (SELECT E.DEALER_SHORTNAME FROM TM_DEALER E, TT_PT_ORDER_LOG F WHERE F.OGR_ID = E.DEALER_ID AND F.ORDER_ID = A.ORDER_ID)\n" );
		sql.append("        END ORG_NAME\n" );
		sql.append("FROM TT_PT_ORDER_LOG A\n" );
		sql.append("WHERE A.ORDER_ID = ").append(orderId);
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//根据经销商ID查询与经销商相关联的供货方信息
	public List<Map<String, Object>> getDClist(String dealerId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.DC_ID, A.DC_NAME\n" );
		sql.append("FROM TM_PT_DC A, TM_DEALER_DC_RELATION B\n" );
		sql.append("WHERE A.DC_ID = B.DC_ID\n");
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND B.DEALER_ID in (").append(dealerId).append(")\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//根据经销商ID,供货方ID动态查询经销商在供货方的存款
	public Map<String, Object> getAmount(String dealerId, String dcId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TO_CHAR(A.AMOUNT, 'FM99,999,990.00') AMOUNT\n" );
		sql.append("FROM TM_DEALER_DC_FINANCE A\n" );
		sql.append("WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.DEALER_ID = ").append(dealerId).append("\n");
		sql.append("AND A.DC_ID = ").append(dcId).append("\n");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	//根据供货方ID查询供货方名称
	public Map<String, Object> getDCName(String dcId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.DC_ID, A.DC_NAME \n" );
		sql.append("FROM TM_PT_DC A \n" );
		sql.append("WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		if(!dcId.equals("")){
			sql.append("AND A.DC_ID = ").append(dcId);
		}
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	//查询供货方的配件库存
	public PageResult<Map<String, Object>> queryDCPartStock(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID, A.PART_CODE, A.PART_NAME, A.UNIT,\n" );
		sql.append("       A.MINI_PACK, DECODE(B.QUANTITY, 0,'无','有') QUANTITY\n" );
		sql.append("FROM TM_PT_PART_BASE A, TT_PT_DC_STOCK B\n" );
		sql.append("WHERE A.PART_ID = B.PART_ID\n" );
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND B.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		if(!bean.getDcId().equals("")){
			sql.append("AND B.DC_ID =").append(bean.getDcId());
		}
		if(!bean.getPartCode().equals("")){
			sql.append("AND A.PART_CODE LIKE '%").append(bean.getPartCode()).append("%'\n");
		}
		if(!bean.getPartName().equals("")){
			sql.append("AND A.PART_NAME LIKE '%").append(bean.getPartName()).append("%'\n");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//查询供货方的配件库存
	public PageResult<Map<String, Object>> queryDCPartStockBySignNo(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID, A.PART_CODE, A.PART_NAME, A.UNIT,\n" );
		sql.append("       A.MINI_PACK, B.SIGN_ID \n" );
		sql.append(" FROM TM_PT_PART_BASE A, TT_PT_DLR_SIGN B, TT_PT_DLR_SIGN_DETAIL C \n" );
		sql.append(" WHERE A.PART_ID = C.PART_ID\n" );
		sql.append(" AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append(" AND B.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append(" AND B.SIGN_NO = '").append(bean.getSignNo()).append("' \n");
		sql.append(" AND B.SIGN_ID = C.SIGN_ID \n");
		if(!bean.getPartCode().equals("")){
			sql.append("AND A.PART_CODE LIKE '%").append(bean.getPartCode()).append("%'\n");
		}
		if(!bean.getPartName().equals("")){
			sql.append("AND A.PART_NAME LIKE '%").append(bean.getPartName()).append("%'\n");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//查询配件全表 queryPartAllList
	public PageResult<Map<String, Object>> queryPartAllList(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID, A.PART_CODE, A.PART_NAME, A.UNIT,\n" );
		sql.append("       A.MINI_PACK\n" );
		sql.append("FROM TM_PT_PART_BASE A\n" );
		sql.append("WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		if(!bean.getPartCode().equals("")){
			sql.append("AND A.PART_CODE LIKE '%").append(bean.getPartCode()).append("%'\n");
		}
		if(!bean.getPartName().equals("")){
			sql.append("AND A.PART_NAME LIKE '%").append(bean.getPartName()).append("%'\n");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//分页查询货运单信息
	public PageResult<Map<String, Object>> queryDirsignList(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.DO_NO, C.ORDER_NO, TO_CHAR(C.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n" );
		sql.append("       TO_CHAR(C.REQUIRE_DATE, 'YYYY-MM-DD') REQUIRE_DATE, C.ORDER_ID,\n" );
		sql.append("       NVL(A.IS_SIGNED, 0) IS_SIGNED, COUNT(E.DO_NO) COUT\n" );
		sql.append("FROM TT_PT_SHIPPINGSHEET A, TT_PT_SALES B, TT_PT_ORDER C, TT_PT_SHIPPINGSHEETITEM E\n" );
		sql.append("WHERE A.SO_NO = B.SO_NO\n" );
		sql.append("AND B.ORDER_NO = C.ORDER_NO\n" );
		//sql.append("AND C.CREATE_BY = D.USER_ID\n" );
		sql.append("AND A.DO_NO = E.DO_NO\n" );
		sql.append("AND C.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		
		if(!bean.getOrderNo().equals("")){
			sql.append("AND C.ORDER_NO LIKE '%").append(bean.getOrderNo()).append("%'\n");
		}
		if(!bean.getDoNo().equals("")){
			sql.append("AND A.DO_NO LIKE '%").append(bean.getDoNo()).append("%'\n");
		}
		if(!bean.getBeginTime().equals("")){
			sql.append("AND C.RAISE_DATE >= TO_DATE('").append(bean.getBeginTime()).append(" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(!bean.getEndTime().equals("")){
			sql.append("AND C.RAISE_DATE <= TO_DATE('").append(bean.getEndTime()).append(" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
		}
		
		sql.append("AND C.DEALER_ID in (").append(bean.getDealerId()).append(")\n");
		sql.append("GROUP BY A.DO_NO, C.ORDER_NO, C.RAISE_DATE, C.REQUIRE_DATE, A.IS_SIGNED,C.ORDER_ID\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//根据订单号查询供货方的配件库存
	public PageResult<Map<String, Object>> queryDCPartStockByOrderId(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID, A.PART_CODE, A.PART_NAME, A.UNIT,\n" );
		sql.append("       A.MINI_PACK, DECODE(B.QUANTITY, 0,'无','有') QUANTITY\n" );
		sql.append("FROM TM_PT_PART_BASE A, TT_PT_DC_STOCK B, TT_PT_ORDITEM C \n" );
		sql.append("WHERE A.PART_ID = B.PART_ID\n" );
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND B.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append(" AND C.PART_ID = B.PART_ID").append("\n");
		if(!bean.getDcId().equals("")){
			sql.append("AND B.DC_ID =").append(bean.getDcId());
		}
		if(!bean.getPartCode().equals("")){
			sql.append("AND A.PART_CODE LIKE '%").append(bean.getPartCode()).append("%'\n");
		}
		if(!bean.getPartName().equals("")){
			sql.append("AND A.PART_NAME LIKE '%").append(bean.getPartName()).append("%'\n");
		}
		
		sql.append(" AND C.ORDER_ID = ").append(Long.parseLong(bean.getOrderId()));
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//根据经销商ID查询经销商级别
	public String getDealerLevel(String b){
		String dealerLevel = "";
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DEALER_LEVEL\n" );
		sql.append("FROM TM_DEALER\n" );
		sql.append("WHERE STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		sql.append("AND DEALER_ID = ").append(b);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		dealerLevel = String.valueOf(map.get("DEALER_LEVEL"));
		return dealerLevel;
	}
	
	//根据经销商ID获取上级经销商ID，无的话是自己本身
	public String getHighDealerId(String b){
		String a = "";
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DECODE(PARENT_DEALER_D, -1,DEALER_ID,PARENT_DEALER_D) DID\n" );
		sql.append("FROM TM_DEALER\n" );
		sql.append("WHERE STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		sql.append("AND DEALER_ID = ").append(b);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		a = String.valueOf(map.get("DID"));
		return a;
	}
	
	//根据订单ID查询配件ID
	public List<Map<String, Object>> getpartList(String orderId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT PART_ID FROM TT_PT_ORDITEM WHERE ORDER_ID = ").append(orderId);
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//查询代签收的配件信息
	public List<Map<String, Object>> getDoOrderList(String doNo){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID, A.PART_CODE, A.PART_NAME,A.UNIT,A.COUNT DO_COUNT,A.DO_NO,\n" );
		sql.append("       NVL(B.ORDER_COUNT, 0) ORDER_COUNT,A.ITEM_ID\n" );
		sql.append("FROM\n" );
		sql.append("(\n" );
		sql.append(" SELECT A.DO_NO,A.SO_NO,B.PART_CODE,B.ITEM_ID, B.PART_NAME, B.COUNT, C.PART_ID, C.UNIT\n" );
		sql.append(" FROM TT_PT_SHIPPINGSHEET A, TT_PT_SHIPPINGSHEETITEM B, TM_PT_PART_BASE C\n" );
		sql.append(" WHERE A.DO_NO = B.DO_NO\n" );
		sql.append(" AND B.PART_CODE = C.PART_CODE\n" );
		sql.append(" AND B.PART_NAME = C.PART_NAME\n" );
		sql.append(" AND NVL(A.IS_SIGNED, ").append(Constant.IS_SIGNED_00).append(") = ").append(Constant.IS_SIGNED_00).append("\n");
		sql.append(" AND C.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append(") A,\n" );
		sql.append("(\n" );
		sql.append(" SELECT A.SO_NO, C.ORDER_COUNT, C.PART_ID\n" );
		sql.append(" FROM TT_PT_SALES A, TT_PT_ORDER B, TT_PT_ORDITEM C\n" );
		sql.append(" WHERE A.ORDER_NO = B.ORDER_NO\n" );
		sql.append(" AND B.ORDER_ID = C.ORDER_ID\n" );
		sql.append(" AND B.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append(") B\n" );
		sql.append("WHERE A.SO_NO = B.SO_NO(+)\n" );
		sql.append("AND A.PART_ID = B.PART_ID(+)\n");
		sql.append("AND A.DO_NO = '").append(doNo).append("'");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//根据订单ID查询订单内容
	public Map<String, Object> getOrderMap(String orderId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.ORDER_ID, A.ORDER_NO, A.DC_ID, A.REQUIRE_DATE, A.TRANS_TYPE, A.REMARK,\n" );
		sql.append("       TO_CHAR(B.AMOUNT, 'FM99,999,990.00') AMOUNT\n" );
		sql.append("FROM TT_PT_ORDER A, TM_DEALER_DC_FINANCE B\n" );
		sql.append("WHERE A.DC_ID = B.DC_ID\n");
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.ORDER_ID = ").append(orderId).append("\n");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	//分页查询待上报的采购订单
	public PageResult<Map<String, Object>> queryPartOrderForword(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.ORDER_ID, A.ORDER_NO, B.DC_NAME,\n" );
		sql.append("       TO_CHAR(A.REQUIRE_DATE, 'YYYY-MM-DD') REQUIRE_DATE,\n" );
		sql.append("       A.ITEM_COUNT, C.NAME, TO_CHAR(A.ORDER_PRICE, 'FM99,999,990.00') ORDER_PRICE,\n" );
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE\n" );
		sql.append("FROM TT_PT_ORDER A, TM_PT_DC B, TC_USER C\n" );
		sql.append("WHERE A.DC_ID = B.DC_ID\n" );
		sql.append("AND A.CREATE_BY = C.USER_ID\n" );
		
		if(!bean.getOrderNo().equals("")){
			sql.append("AND A.ORDER_NO LIKE '%").append(bean.getOrderNo()).append("%'\n");
		}
		if(!bean.getBeginTime().equals("")){
			sql.append("AND A.CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("','YYYY-MM-DD') \n");
		}
		if(!bean.getEndTime().equals("")){
			sql.append("AND A.CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("','YYYY-MM-DD') \n");
		}
		if(!bean.getDcId().equals("")){
			sql.append("AND A.DC_ID = ").append(bean.getDcId()).append("\n");
		}
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND B.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.ORDER_STATUS = ").append(Constant.PART_ORDER_STATUS_02).append("\n");
		sql.append("AND A.DEALER_ID = ").append(bean.getDealerId()).append("\n");
		sql.append("ORDER BY A.ORDER_ID DESC \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//分页查询二级经销商提报的配件订单 
	public PageResult<Map<String, Object>> queryPartCarefully(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.ORDER_ID, A.ORDER_NO, B.DC_NAME,\n" );
		sql.append("       TO_CHAR(A.REQUIRE_DATE, 'YYYY-MM-DD') REQUIRE_DATE,\n" );
		sql.append("       A.ITEM_COUNT, C.NAME, TO_CHAR(A.ORDER_PRICE, 'FM99,999,990.00') ORDER_PRICE,\n" );
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE,\n" );
		sql.append("       D.DEALER_CODE, D.DEALER_SHORTNAME\n" );
		sql.append("FROM TT_PT_ORDER A, TM_PT_DC B, TC_USER C, TM_DEALER D\n" );
		sql.append("WHERE A.DC_ID = B.DC_ID\n" );
		sql.append("AND A.CREATE_BY = C.USER_ID\n" );
		sql.append("AND A.DEALER_ID = D.DEALER_ID\n" );
		sql.append("AND D.DEALER_ID IN (\n" );
		sql.append("    SELECT DEALER_ID\n" );
		sql.append("    FROM TM_DEALER\n" );
		sql.append("    WHERE DEALER_LEVEL <> ").append(Constant.DEALER_LEVEL_01).append("\n");
		sql.append("    START WITH DEALER_ID = ").append(bean.getDealerId()).append("\n");
		sql.append("    CONNECT BY PRIOR DEALER_ID = PARENT_DEALER_D\n" );
		sql.append(")\n" );
		
	
		if(!bean.getOrderNo().equals("")){
			sql.append("AND A.ORDER_NO LIKE '%").append(bean.getOrderNo()).append("%'\n");
		}
		if(!bean.getBeginTime().equals("")){
			sql.append("AND A.CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("','YYYY-MM-DD') \n");
		}
		if(!bean.getEndTime().equals("")){
			sql.append("AND A.CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("','YYYY-MM-DD') \n");
		}
		if(!bean.getDcId().equals("")){
			sql.append("AND A.DC_ID = ").append(bean.getDcId()).append("\n");
		}
		if(!bean.getDealerCode().equals("")){
			sql.append("AND D.DEALER_CODE LIKE '%").append(bean.getDealerCode()).append("%' \n");
		}
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND B.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND D.STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		sql.append("AND A.ORDER_STATUS = ").append(Constant.PART_ORDER_STATUS_03).append("\n");
		sql.append("ORDER BY A.ORDER_ID DESC \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//根据经销商ID查询上报规则
	public Map<String, Object> getOrderParam(String dealerId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.PARAM_ID, A.ORDER_MAX_LINES, A.ALLOW_SUBMIT_TIMES, A.CYCLE_TYPE\n" );
		sql.append("FROM TM_PT_ORDER_PARAM A\n" );
		sql.append("WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.DEALER_ID = ").append(dealerId).append("\n");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	//根据经销商ID查询已提报的次数
	public Integer getDealerPartCount(String dealerId,String min, String max){
		Integer count = 0;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT COUNT(*) COUNT\n" );
		sql.append("FROM TT_PT_ORDER A\n" );
		sql.append("WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.DEALER_ID = ").append(dealerId).append("\n");
		sql.append("AND A.ORDER_STATUS = ").append(Constant.PART_ORDER_STATUS_03).append("\n");
		sql.append("AND A.RAISE_DATE >= TO_DATE('").append(min).append("','YYYY-MM-DD') \n");
		sql.append("AND A.RAISE_DATE <= TO_DATE('").append(max).append("','YYYY-MM-DD') \n");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		count = Integer.parseInt(String.valueOf(map.get("COUNT")));
		return count;
	}
	
	//根据参数ID查询子表中上报时间
	public List<Map<String, Object>> getPartDateList(String paramId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.START_DATE, A.END_DATE\n" );
		sql.append("FROM TM_PT_ORDPARAM_DATE A\n" );
		sql.append("WHERE A.PARAM_ID = ").append(paramId);
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public Integer getRaiseTimes(String orderId){
		Integer cout = 0;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT NVL(A.RAISE_TIMES, 0) COUNT\n" );
		sql.append("FROM TT_PT_ORDER A\n" );
		sql.append("WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.ORDER_ID = ").append(orderId).append("\n");

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if(map.size()>0){
			cout = Integer.parseInt(String.valueOf(map.get("COUNT")));
		}
		cout = cout + 1;
		return cout;
	}
	
	//取订单ID
	public Long getOrderId(String orderNo){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ORDER_ID\n" );
		sql.append("FROM TT_PT_ORDER\n" );
		sql.append("WHERE ORDER_NO = '").append(orderNo).append("'");  
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		Long orderId = Long.parseLong(String.valueOf(map.get("ORDER_ID")));
		return orderId;
	}
	
	//货运明细
	public Map<String, Object> getDonoSignInfo(String doNo){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.DO_NO, B.SIGN_USER_ID,\n" );
		sql.append("       TO_CHAR(B.SIGN_DATE, 'YYYY-MM-DD') SIGN_DATE\n" );
		sql.append("FROM TT_PT_SHIPPINGSHEET A, TT_PT_DLR_SIGN B\n" );
		sql.append("WHERE A.DO_NO = B.DO_NO\n" );
		sql.append("AND A.DO_NO = '").append(doNo).append("'");

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
}
