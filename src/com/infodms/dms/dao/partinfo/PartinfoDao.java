package com.infodms.dms.dao.partinfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.PartinfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerDcRelationPO;
import com.infodms.dms.po.TmPtOrderParamPO;
import com.infodms.dms.po.TmPtOrdparamDatePO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmPtPartTypePO;
import com.infodms.dms.po.TtAsWrLabourPricePO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

/**
 * @Title: PartinfoDao.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-3
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class PartinfoDao extends BaseDao {
	public static Logger logger = Logger.getLogger(PartinfoDao.class);
	
	protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//分页查询配件信息
	public PageResult<Map<String, Object>> queryPartInfoList(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID, A.PART_CODE, A.PART_NAME, A.PART_TYPE, NVL(B.PART_CODE,'无') REPLACE_PART_ID,\n" );
		sql.append("       DECODE(A.STOP_FLAG,1,'是',0,'否') STOP_FLAG, DECODE(A.IS_NEW_PART,1,'是',0,'否') AS IS_NEW_PART,\n" );
		sql.append("       A.SALE_PRICE, A.CUSTOMER_PRICE,A.CLAIM_PRICE\n" );
		sql.append("FROM TM_PT_PART_BASE A, TM_PT_PART_BASE B,\n" );
		sql.append("     TM_PT_SUPPLIER D, TM_PT_PART_SUP_RELATION E\n" );
		sql.append("WHERE A.REPLACE_PART_ID = B.PART_ID(+)\n" );
		sql.append("AND A.PART_ID = E.ORDER_ID\n" );
		sql.append("AND E.SUPPLIER_ID = D.SUPPLIER_ID\n");
		sql.append("AND D.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND NVL(B.IS_DEL,").append(Constant.IS_DEL_00).append(")=").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND E.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		if(!bean.getPartCode().equals("")){
			sql.append("AND A.PART_CODE LIKE '%").append(bean.getPartCode()).append("%'\n"); 
		}
		if(!bean.getPartName().equals("")){
			sql.append("AND A.PART_NAME LIKE '%").append(bean.getPartName()).append("%'\n");
		}
		if(!bean.getSupplierCode().equals("")){
			sql.append("AND D.SUPPLIER_CODE IN (").append(bean.getSupplierCode()).append(")\n");
		}
		if(!bean.getSupplierName().equals("")){
			sql.append("AND D.SUPPLIER_NAME LIKE '%").append(bean.getSupplierName()).append("%'\n");
		}

		sql.append("GROUP BY A.PART_ID, A.PART_CODE, A.PART_NAME, A.PART_TYPE, B.PART_CODE,\n");
		sql.append("         A.STOP_FLAG,A.IS_NEW_PART, A.SALE_PRICE, A.CUSTOMER_PRICE,A.CLAIM_PRICE \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//分页查询配件信息 单表
	public PageResult<Map<String, Object>> queryPartInfoListOnly(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID, A.PART_CODE, A.PART_NAME, A.PART_TYPE, NVL(B.PART_CODE,'无') REPLACE_PART_ID,\n" );
		sql.append("       DECODE(A.STOP_FLAG,1,'是',0,'否') STOP_FLAG,DECODE(A.IS_NEW_PART,1,'是',0,'否') AS IS_NEW_PART,\n" );
		sql.append("       A.SALE_PRICE, A.CUSTOMER_PRICE,A.CLAIM_PRICE\n" );
		sql.append("FROM TM_PT_PART_BASE A, TM_PT_PART_BASE B\n" );
		sql.append("WHERE A.REPLACE_PART_ID = B.PART_ID(+)\n" );
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND NVL(B.IS_DEL,").append(Constant.IS_DEL_00).append(")=").append(Constant.IS_DEL_00).append("\n");
		if(!bean.getPartCode().equals("")){
			sql.append("AND A.PART_CODE LIKE '%").append(bean.getPartCode()).append("%'\n"); 
		}
		if(!bean.getPartName().equals("")){
			sql.append("AND A.PART_NAME LIKE '%").append(bean.getPartName()).append("%'\n");
		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

	public PageResult<TmPtPartBasePO> getPartInfo(String con,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select p.part_id,p.part_code,p.part_name from tm_pt_part_base p\n");
		sql.append("where p.is_del=").append(Constant.IS_DEL_00).append("\n");
		if(StringUtil.notNull(con))
			sql.append(con);
		return this.pageQuery(TmPtPartBasePO.class, sql.toString(), null, pageSize, curPage);		
	}
	//查询配件详细信息
	public Map<String, Object> getPartDetail(String partId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID,  A.PART_CODE, NVL(B.PART_CODE,'无') REPLACE_PART_ID,\n" );
		sql.append("       A.PART_NAME, A.PART_TYPE, A.UNIT,\n" );
		sql.append("       A.MINI_PACK, A.CHANGE_CODE, \n" );
		sql.append("       A.CAR_AMOUNT, A.STOCK_PRICE,\n" );
		sql.append("       A.SALE_PRICE, A.CUSTOMER_PRICE, A.CLAIM_PRICE,\n" );
		sql.append("       DECODE(A.STOP_FLAG,1,'是',0,'否') STOP_FLAG, A.REMARK, A.IS_RETURN,A.IS_NEW_PART\n" );
		sql.append("FROM TM_PT_PART_BASE A, TM_PT_PART_BASE B\n" );
		sql.append("WHERE A.REPLACE_PART_ID = B.PART_ID(+)\n" );
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND NVL(B.IS_DEL,").append(Constant.IS_DEL_00).append(")=").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.PART_ID = ").append(partId);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	//查询配件与订单表关系
	public Map<String, Object> queryPartDetailList(String partId, String orderId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID, A.PART_CODE, A.PART_NAME, A.UNIT\n" );
		sql.append("FROM TM_PT_PART_BASE A\n" );
		sql.append("WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.PART_ID = ").append(partId);
		Map<String, Object> partMap = pageQueryMap(sql.toString(), null, getFunName());
		
		StringBuffer sql2 = new StringBuffer("");
		sql2.append("SELECT ORDER_COUNT\n" );
		sql2.append("FROM TT_PT_ORDITEM\n" );
		sql2.append("WHERE ORDER_ID = ").append(orderId).append("\n");
		sql2.append("AND PART_ID = ").append(partId);
		Map<String, Object> ordreMap = pageQueryMap(sql2.toString(), null, getFunName());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(partMap);
		if(ordreMap != null && ordreMap.size()>0){
			map.putAll(ordreMap);
		}
		return map;
	}
	
	//查看经销商库存，折扣及配件相关信息
	public Map<String, Object> getPartDetail(String partId, String dealerId){
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID, A.PART_CODE, A.PART_NAME, A.UNIT,A.MINI_PACK, A.REMARK, \n" );
		sql.append("	   TO_CHAR(A.SALE_PRICE, 'FM99,999,990.00') SALE_PRICE \n");
		sql.append("FROM TM_PT_PART_BASE A\n" );
		sql.append("WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.PART_ID = ").append(partId);
		Map<String, Object> partMap = pageQueryMap(sql.toString(), null, getFunName());
		
		StringBuffer sql2 = new StringBuffer("");
		sql2.append("SELECT PAPER_QUANTITY, SAFE_QUANTITY\n" );
		sql2.append("FROM TT_PT_DLRSTOCK A\n" );
		sql2.append("WHERE A.DEALER_ID = ").append(dealerId).append("\n");
		sql2.append("AND A.PART_ID = ").append(partId).append("\n");
		sql2.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		Map<String, Object> stockMap = pageQueryMap(sql2.toString(), null, getFunName());
		
		StringBuffer sql3 = new StringBuffer("");
		sql3.append("SELECT  TO_CHAR(A.DISCOUNT_RATE, 'FM99999990.00') DISCOUNT_RATE\n" );
		sql3.append("FROM TM_PT_ORDER_PARAM A\n" );
		sql3.append("WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql3.append("AND DEALER_ID = ").append(dealerId);
		Map<String, Object> paramMap = pageQueryMap(sql3.toString(), null, getFunName());
		
		if(partMap != null){
			map.putAll(partMap);
		}
		if(stockMap != null){
			map.putAll(stockMap);
		}
		if(paramMap != null){
			map.putAll(paramMap);
		}
		return map;
	}
	
	//查看经销商库存，折扣及配件相关信息
	public Map<String, Object> getPartDetailInfo(String partId, String orderId, String dealerId){
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID, A.PART_CODE, A.PART_NAME, A.UNIT,A.MINI_PACK, A.REMARK, \n" );
		sql.append("	   TO_CHAR(A.SALE_PRICE, 'FM99,999,990.00') SALE_PRICE \n");
		sql.append("FROM TM_PT_PART_BASE A\n" );
		sql.append("WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.PART_ID = ").append(partId);
		Map<String, Object> partMap = pageQueryMap(sql.toString(), null, getFunName());
		
		StringBuffer sql2 = new StringBuffer("");
		sql2.append("SELECT PAPER_QUANTITY, SAFE_QUANTITY\n" );
		sql2.append("FROM TT_PT_DLRSTOCK A\n" );
		sql2.append("WHERE A.DEALER_ID = ").append(dealerId).append("\n");
		sql2.append("AND A.PART_ID = ").append(partId).append("\n");
		sql2.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		Map<String, Object> stockMap = pageQueryMap(sql2.toString(), null, getFunName());
		
		StringBuffer sql3 = new StringBuffer("");
		sql3.append("SELECT  TO_CHAR(A.DISCOUNT_RATE, 'FM99999990.00') DISCOUNT_RATE\n" );
		sql3.append("FROM TM_PT_ORDER_PARAM A\n" );
		sql3.append("WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql3.append("AND DEALER_ID = ").append(dealerId);
		Map<String, Object> paramMap = pageQueryMap(sql3.toString(), null, getFunName());
		
		StringBuffer sql4 = new StringBuffer("");
		sql4.append("SELECT A.ORDER_COUNT, A.REMARK,\n" );
		sql4.append("       TO_CHAR(B.ORDER_PRICE, 'FM99,999,990.00') ORDER_PRICE\n" );
		sql4.append("FROM TT_PT_ORDITEM A, TT_PT_ORDER B\n" );
		sql4.append("WHERE A.ORDER_ID = B.ORDER_ID\n" );
		sql4.append("AND B.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql4.append("AND A.ORDER_ID = ").append(orderId).append("\n");
		sql4.append("AND A.PART_ID = ").append(partId).append("\n");
		Map<String, Object> infoMap = pageQueryMap(sql4.toString(), null, getFunName());
		
		if(partMap != null){
			map.putAll(partMap);
		}
		if(stockMap != null){
			map.putAll(stockMap);
		}
		if(paramMap != null){
			map.putAll(paramMap);
		}
		if(infoMap != null){
			map.putAll(infoMap);
		}
		return map;
	}
	
	//查询审批详细
	public PageResult<Map<String, Object>> getPartRelation(String partId,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT B.RELATION_ID, A.SUPPLIER_CODE, A.SUPPLIER_NAME, A.SHORT_NAME,\n" );
		sql.append("       A.LINK_MAN, A.PHONE_NUMBER\n" );
		sql.append("FROM TM_PT_SUPPLIER A, TM_PT_PART_SUP_RELATION B\n" );
		sql.append("WHERE A.SUPPLIER_ID = B.SUPPLIER_ID\n");
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND B.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND B.ORDER_ID = ").append(partId).append("\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//分页供应商信息
	public PageResult<Map<String, Object>> querySupplierInfoList(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.SUPPLIER_ID, A.SUPPLIER_CODE, A.SUPPLIER_NAME, A.SHORT_NAME, A.ADDRESS,\n" );
		sql.append("       A.ZIPCODE,A.FAX,A.LINK_MAN,A.PHONE_NUMBER,A.REMARK\n" );
		sql.append("FROM TM_PT_SUPPLIER A\n" );
		sql.append("WHERE A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		if(!bean.getSupplierCode().equals("")){
			sql.append("AND A.SUPPLIER_CODE IN (").append(bean.getSupplierCode()).append(")\n");
		}
		if(!bean.getSupplierName().equals("")){
			sql.append("AND A.SUPPLIER_NAME LIKE '%").append(bean.getSupplierName()).append("%'\n");
		}

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//分页查询经销商库存
	public PageResult<Map<String, Object>> queryDealerDlrstockList(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID,A.PART_CODE, A.PART_NAME, A.UNIT, NVL(B.PART_CODE, '无') REPLACE_PART_ID,\n" );
		sql.append("       A.SALE_PRICE, A.MINI_PACK,D.DEALER_SHORTNAME DEALER_NAME,D.DEALER_ID, C.PAPER_QUANTITY, C.ACTUAL_QUANTITY\n" );
		sql.append("FROM TM_PT_PART_BASE A, TM_PT_PART_BASE B, TT_PT_DLRSTOCK C, TM_DEALER D\n" );
		sql.append("WHERE A.REPLACE_PART_ID = B.PART_ID(+)\n" );
		sql.append("AND A.PART_ID = C.PART_ID\n" );
		sql.append("AND C.DEALER_ID = D.DEALER_ID\n" );
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND NVL(B.IS_DEL,").append(Constant.IS_DEL_00).append(")=").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND C.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND D.STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		if(!bean.getDealerCode().equals("")){
			sql.append("AND D.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
		}
		if(!bean.getDealerName().equals("")){
			sql.append("AND D.DEALER_NAME LIKE '%").append(bean.getDealerName()).append("%'\n");
		}
		if(!bean.getPartCode().equals("")){
			sql.append("AND A.PART_CODE LIKE '%").append(bean.getPartCode()).append("%'\n");
		}
		if(!bean.getPartName().equals("")){
			sql.append("AND A.PART_NAME LIKE '%").append(bean.getPartName()).append("%'\n");
		}
		
		sql.append("ORDER BY D.DEALER_SHORTNAME \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//分页查询经销商库存 按大区查询
	public PageResult<Map<String, Object>> queryOrgDlrstockList(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID,A.PART_CODE, A.PART_NAME, A.UNIT, NVL(B.PART_CODE, '无') REPLACE_PART_ID,\n" );
		sql.append("       A.SALE_PRICE, A.MINI_PACK,D.DEALER_SHORTNAME DEALER_NAME,D.DEALER_ID, C.PAPER_QUANTITY, C.ACTUAL_QUANTITY\n" );
		sql.append("FROM TM_PT_PART_BASE A, TM_PT_PART_BASE B, TT_PT_DLRSTOCK C, TM_DEALER D,TM_ORG E, TM_DEALER_ORG_RELATION F \n" );
		sql.append("WHERE A.REPLACE_PART_ID = B.PART_ID(+)\n" );
		sql.append("AND A.PART_ID = C.PART_ID\n" );
		sql.append("AND C.DEALER_ID = D.DEALER_ID\n" );
		sql.append("AND D.DEALER_ID = F.DEALER_ID\n" );
		sql.append("AND F.ORG_ID = E.ORG_ID\n" );
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND NVL(B.IS_DEL,").append(Constant.IS_DEL_00).append(")=").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND C.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND D.STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		if(!bean.getPartCode().equals("")){
			sql.append("AND A.PART_CODE LIKE '%").append(bean.getPartCode()).append("%'\n");
		}
		if(!bean.getPartName().equals("")){
			sql.append("AND A.PART_NAME LIKE '%").append(bean.getPartName()).append("%'\n");
		}
		if(!bean.getOrgCode().equals("")){
			sql.append("AND E.ORG_CODE IN (").append(bean.getOrgCode()).append(")\n");
		}
		sql.append("ORDER BY D.DEALER_SHORTNAME \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//分页查询配件库存明细
	public PageResult<Map<String, Object>> queryPartDlrmoveDetailInfo(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_CODE, A.PART_NAME, A.UNIT,\n" );
		sql.append("       C.QUANTITY, TO_CHAR(C.DO_DATE, 'YYYY-MM-DD') DO_DATE , C.DO_STATUS  \n" );
		sql.append("FROM TM_PT_PART_BASE A, TM_PT_PART_BASE B, TT_PT_DLRMOVE_DETAIL C, TM_DEALER D\n" );
		sql.append("WHERE A.REPLACE_PART_ID = B.PART_ID(+)\n" );
		sql.append("AND A.PART_ID = C.PART_ID\n" );
		sql.append("AND C.DEALER_ID = D.DEALER_ID\n");
		if(!bean.getBeginTime().equals("")){
			sql.append(" AND C.DO_DATE >= TO_DATE('").append(bean.getBeginTime()).append(" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(!bean.getEndTime().equals("")){
			sql.append(" AND C.DO_DATE <= TO_DATE('").append(bean.getEndTime()).append(" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(!bean.getDoStatus().equals("")){
			sql.append(" AND C.DO_STATUS = ").append(bean.getDoStatus()).append("\n"); 
		}
		sql.append(" AND A.PART_ID = ").append(bean.getPartId()).append("\n");
		sql.append(" AND D.DEALER_ID = ").append(bean.getDealerId());
		sql.append(" AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append(" AND NVL(B.IS_DEL,").append(Constant.IS_DEL_00).append(")=").append(Constant.IS_DEL_00).append("\n");
		sql.append(" AND C.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append(" AND D.STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		sql.append(" ORDER BY C.DO_STATUS, A.PART_ID\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//分页查询配件库存明细
	public PageResult<Map<String, Object>> queryDCRelactionInfo(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.DC_ID, A.DC_CODE,A.SHORT_NAME,A.PHONE_NUMBER, A.LINK_MAN, A.FAX, A.ZIPCODE,\n" );
		sql.append("       COUNT(B.DEALER_ID) COUNT\n" );
		sql.append("FROM TM_PT_DC A, TM_DEALER_DC_RELATION B\n" );
		sql.append("WHERE A.DC_ID = B.DC_ID\n" );

		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		
		if(!bean.getDcCode().equals("")){
			sql.append("AND A.DC_CODE LIKE '%").append(bean.getDcCode()).append("%' \n");
		}
		if(!bean.getDcName().equals("")){
			sql.append("AND A.DC_NAME LIKE '%").append(bean.getDcName()).append("%' \n");
		}
		
		sql.append("GROUP BY A.DC_ID, A.DC_CODE,A.SHORT_NAME,A.PHONE_NUMBER, A.LINK_MAN, A.FAX, A.ZIPCODE\n" );
		sql.append("ORDER BY A.DC_ID DESC");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//分页查询配件库存明细
	public PageResult<Map<String, Object>> queryRelactionInfo(String dcId,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.DEALER_ID, A.DEALER_CODE, A.DEALER_NAME, A.DEALER_LEVEL, C.ORG_NAME\n" );
		sql.append("FROM TM_DEALER A, TM_DEALER_DC_RELATION B, TM_ORG C, TM_DEALER_ORG_RELATION D\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("AND A.DEALER_ID = D.DEALER_ID\n" );
		sql.append("AND D.ORG_ID = C.ORG_ID\n" );
		sql.append("AND A.STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		sql.append("AND C.STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		sql.append("AND B.DC_ID = ").append(dcId);

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//查询供货方信息
	public Map<String, Object> getDcInfo(String dcId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DC_ID, DC_CODE, DC_NAME\n" );
		sql.append("FROM TM_PT_DC\n" );
		sql.append("WHERE DC_ID = ").append(dcId); 

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	//分页查询经销商信息
	public PageResult<Map<String, Object>> queryDealerInfo(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.DEALER_ID, A.DEALER_CODE, A.DEALER_NAME,A.DEALER_LEVEL,\n" );
		sql.append("       B.ORG_NAME\n" );
		sql.append("FROM TM_DEALER A, TM_ORG B, TM_DEALER_ORG_RELATION C\n" );
		sql.append("WHERE A.DEALER_ID = C.DEALER_ID\n" );
		sql.append("AND C.ORG_ID = B.ORG_ID\n" );
		sql.append("AND NOT EXISTS(\n" );
		sql.append("     SELECT 'X' FROM TM_DEALER_DC_RELATION D\n" );
		sql.append("     WHERE A.DEALER_ID = D.DEALER_ID\n" );
		sql.append("     AND D.DC_ID = ").append(bean.getDcId()).append("\n"); 
		sql.append(")");

		if(!bean.getDealerCode().equals("")){
			sql.append("AND A.DEALER_CODE IN (").append(bean.getDealerCode()).append(") \n");
		}
		if(!bean.getDealerName().equals("")){
			sql.append("AND A.DEALER_NAME LIKE '%").append(bean.getDealerName()).append("%' \n");
		}
		if(!bean.getDealerLevel().equals("")){
			sql.append("AND A.DEALER_LEVEL = ").append(bean.getDealerLevel()).append(" \n");           
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//新增供货方和经销商关系
	public void conserveReaction(String dcId, String[] dealerIds, Long createBy){
		if(dealerIds != null && !dealerIds.equals("")){
			for(int i=0;i<dealerIds.length;i++){
				TmDealerDcRelationPO po = new TmDealerDcRelationPO();
				po.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
				po.setDcId(new Long(dcId));
				po.setDealerId(new Long(dealerIds[i]));
				po.setCreateBy(createBy);
				po.setCreateDate(new Date(System.currentTimeMillis()));
				insert(po);
			}
		}
	}
	
	//删除供货方与供应商关系
	public void deleteReaction(String dcId, String[] dealerIds){
		if(dealerIds != null && !dealerIds.equals("")){
			for(int i=0;i<dealerIds.length;i++){
				TmDealerDcRelationPO po = new TmDealerDcRelationPO();
				po.setDcId(new Long(dcId));
				po.setDealerId(new Long(dealerIds[i]));
				delete(po);
			}
		}
	}
	
	//添加规则主信息
	public void insertOrderParam(TmPtOrderParamPO po){
		insert(po);
	}
	
	//添加规则上报时间
	public void insertOrderParamDate(TmPtOrdparamDatePO po){
		insert(po);
	}
	
	//修改前删除规则
	public void delParamDealerId(String dealerIds){
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT PARAM_ID FROM TM_PT_ORDER_PARAM WHERE DEALER_ID IN (").append(dealerIds).append(") \n");  
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> bean = (Map<String, Object>)list.get(i);
			str = str + bean.get("PARAM_ID");
			if(i<list.size()-1){
				str=str+",";
			}
		}
		String[] erId = dealerIds.split(",");
		if(erId != null && !erId.equals("")){
			for(int i=0;i<erId.length;i++){
				TmPtOrderParamPO po = new TmPtOrderParamPO();
				po.setDealerId(new Long(erId[i]));
				delete(po);
			}
		}
		if(!str.equals("")&& str.length()>0){
			String[] pd = str.split(",");
			if(pd !=null && !pd.equals("")){
				for(int j=0;j<pd.length;j++){
					TmPtOrdparamDatePO po = new TmPtOrdparamDatePO();
					po.setParamId(new Long(pd[j]));
					delete(po);	
				}
			}
		}
	}
	
	public PageResult<PartinfoBean> queryOrderParamInfo(PartinfoBean bean,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT B.DEALER_ID, B.DEALER_CODE, B.DEALER_SHORTNAME, A.PARAM_ID,\n" );
		sql.append("       TO_CHAR(A.DISCOUNT_RATE,'FM99999990.00') DISCOUNT_RATE, A.ORDER_MAX_LINES, A.ALLOW_SUBMIT_TIMES,\n" );
		sql.append("       A.CYCLE_TYPE\n" );
		sql.append("FROM TM_PT_ORDER_PARAM A, TM_DEALER B\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n");
		if(!bean.getDealerCode().equals("")){
			sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
		}
		if(!bean.getDealerName().equals("")){
			sql.append("AND B.DEALER_NAME LIKE '%").append(bean.getDealerName()).append("%'\n"); 
		}

		return factory.pageQuery(sql.toString(), null,new DAOCallback<PartinfoBean>() {
			public PartinfoBean wrapper(ResultSet rs, int idx){
				PartinfoBean bean = new PartinfoBean();
				try {
					bean.setDealerId(rs.getString("DEALER_ID"));
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerName(rs.getString("DEALER_SHORTNAME"));
					bean.setParamId(rs.getString("PARAM_ID"));
					bean.setDiscountRate(rs.getString("DISCOUNT_RATE"));
					bean.setOrderMaxLines(rs.getString("ORDER_MAX_LINES"));
					bean.setAllowSubmitTimes(rs.getString("ALLOW_SUBMIT_TIMES"));
					bean.setCycleType(rs.getString("CYCLE_TYPE"));
					bean.setSehDate(getSehDate(rs.getString("PARAM_ID"))); 
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, pageSize,curPage);
	}
	
	public String getSehDate(String paramId){
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT START_DATE||'/'||END_DATE||'/'||HANDLE_DATE SEH_DATE\n" );
		sql.append("FROM TM_PT_ORDPARAM_DATE\n" );
		sql.append("WHERE PARAM_ID = ").append(paramId); 
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> bean = (Map<String, Object>)list.get(i);
			str=str+bean.get("SEH_DATE");
			if(i<list.size()-1){
				str=str+",";
			}
		}
		
		return str;
	}

	/*
	 * 判断数据是否已经存在数据库中
	 */
	@SuppressWarnings("unchecked")
	public TmPtPartTypePO existTypeCode(TmPtPartTypePO po){
		List<TmPtPartTypePO> lists = this.select(po);
		if(lists.size()>0) return lists.get(0);
		return null ;
	}
	
	public List<Map<String, Object>> getHaveParamDealer(String id){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT DEALER_CODE, DEALER_NAME FROM TM_DEALER\n" );
		sql.append("WHERE DEALER_ID IN (").append(id).append(")\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public PageResult<Map<String, Object>> queryMainPartType(String partCode,String partName,String typeId,String isReturn, int pageSize, int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT  T.ID,T.PARTTYPE_CODE,T.PARTTYPE_NAME,T.PART_NUM,DECODE(T.IS_RETURN,1,'是','否') AS IS_RETURN,DECODE(T.IS_MAX,1,'是','否') AS IS_MAX FROM TM_PT_PART_TYPE T\n" );
		if(!partCode.equals("")||!partName.equals("")){
		  sql.append(" ,TM_PT_PART_BASE B ");
		}
		sql.append("WHERE 1=1\n");
		if(!partCode.equals("")){
			sql.append(" and T.PARTTYPE_CODE LIKE '%"+partCode+"%'");
		}
		if(!partName.equals("")){
			sql.append(" and B.PART_NAME LIKE '%"+partName+"%'");
		}
		if(!typeId.equals("")){
			sql.append(" and T.ID IN ("+typeId+")");
		}
		if(!isReturn.equals("")){
			sql.append(" and T.IS_RETURN ="+isReturn+"");
		}
		if(!partCode.equals("")||!partName.equals("")){
			  sql.append(" AND T.ID=B.PART_TYPE_ID ");
		}
		System.out.println(sql.toString());
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}		
	/**
	 * 
	* @Title: delPartVenderReByIds 
	* @Description: TODO(根据id删除接口配件表) 
	* @param @param ids    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void delPartInfoByIds(String ids) {
		StringBuffer sql= new StringBuffer();
		sql.append("DELETE FROM T_SELLPART_DEFINE\n");
		sql.append(" WHERE SELLPART_ID IN (").append(ids).append(")");
		delete(sql.toString(), null);
	}
	
	public void upPartInfoByIds(String ids) {
		StringBuffer sql= new StringBuffer();
		sql.append("update  T_SELLPART_DEFINE set IS_HANDLE=1\n");
		sql.append(" WHERE SELLPART_ID IN (").append(ids).append(")");
		delete(sql.toString(), null);
	}
	
	/**
	 * 
	* @Title: queryWrSysRule 
	* @Description: TODO(查询三包规则表  系统规则) 
	* @return List<Map<String,Object>>    返回类型 
	* @throws
	 */
	public List<Map<String, Object>> queryWrSysRule() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ID,RULE_CODE FROM TT_AS_WR_RULE\n" );
		sql.append(" WHERE RULE_TYPE = ").append(Constant.RULE_TYPE_01);
		List<Map<String, Object>> maps = pageQuery(sql.toString(), null, getFunName());
		return maps;
	}
	
}
