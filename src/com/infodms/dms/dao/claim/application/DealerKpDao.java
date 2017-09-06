package com.infodms.dms.dao.claim.application;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.DealerBalanceBean;
import com.infodms.dms.bean.auditBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmRegionPO;
import com.infodms.dms.po.TtAsPaymentPO;
import com.infodms.dms.po.TtAsWrAdministrativeChargePO;
import com.infodms.dms.po.TtAsWrBalanceAuthitemPO;
import com.infodms.dms.po.TtAsWrBalanceDetailTemPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrClaimBalanceTmpPO;
import com.infodms.dms.po.TtAsWrFinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class DealerKpDao extends BaseDao {
	
	private static DealerKpDao blanceDao = new DealerKpDao();
	
	public static DealerKpDao getInstance(){
	if(blanceDao==null){
		blanceDao = new DealerKpDao();
	}
	return blanceDao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	/**
	 * 经销商结算统计查询
	 * @param condition 查询条件
	 * @return PageResult<Map<String,Object>>
	 */
	/*public PageResult<Map<String,Object>> dealerBalanceStatisQuery(DealerBalanceBean condition,String yieldly){
		
		List<Object> paramList = new ArrayList<Object>();
		
		StringBuilder sql= new StringBuilder();

		sql.append("SELECT AREA_NAME,STATUS,SUM(CLAIMCOUNT) CLAIMCOUNT,SUM(LABOUR_AMOUNT) LABOUR_AMOUNT,SUM(PDI) PDI,SUM(PART_AMOUNT) PART_AMOUNT,\n" );
		sql.append("SUM(NETITEM_AMOUNT) NETITEM_AMOUNT,SUM(FREE_M_PRICE) FREE_M_PRICE,SUM(CAMPAIGN_FEE) CAMPAIGN_FEE,\n" );
		sql.append("SUM(REPAIR_TOTAL) REPAIR_TOTAL,SUM(BALANCE_AMOUNT) BALANCE_AMOUNT\n" );
		//查询非服务活动可结算金额
		sql.append("FROM (SELECT 0 PDI,B.AREA_NAME,A.STATUS,COUNT(*) CLAIMCOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.BALANCE_APPENDLABOUR_AMOUNT),0)+sum(nvl(A.ACCESSORIES_PRICE,0))) LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) PART_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) NETITEM_AMOUNT,NVL(SUM(A.FREE_M_PRICE),0) FREE_M_PRICE,NVL(SUM(A.CAMPAIGN_FEE),0) CAMPAIGN_FEE,\n" );
		sql.append("NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_BUSINESS_AREA B \n" );
		sql.append("WHERE 1=1\n" );
		sql.append(" AND A.YIELDLY = B.AREA_ID ");
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.CLAIM_TYPE <> "+Constant.CLA_TYPE_06+"\n" );
		sql.append("AND A.CLAIM_TYPE <> "+Constant.CLA_TYPE_11+"\n" );
		sql.append("AND A.CLAIM_TYPE <> "+Constant.CLA_TYPE_02+"\n" );
		sql.append("AND  A.STATUS not in (10791006,10791009,10791008,10791012,10791010,10791014,10791005)  ");
		sql.append(" AND A.BALANCE_YIELDLY = "+yieldly);
		paramList.add(condition.getDealerId());
		if(Utility.testString(condition.getEndBalanceDate())){
			paramList.add(condition.getEndBalanceDate());
			sql.append(" AND A.REPORT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(Utility.testString(condition.getConEndDay())){
			paramList.add(condition.getConEndDay());
			sql.append(" AND A.REPORT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		sql.append("GROUP BY A.YIELDLY,A.STATUS,B.AREA_NAME \n" );
		
		sql.append("UNION ALL\n" );
		//查询服务活动可结算的金额
		
		sql.append("SELECT 0 PDI,B.AREA_NAME,A.STATUS,COUNT(*) CLAIMCOUNT,sum(nvl( LABOUR_AMOUNT ,0))LABOUR_AMOUNT,sum(nvl(PART_AMOUNT,0)) PART_AMOUNT,\n" );
		sql.append("0 NETITEM_AMOUNT,0 FREE_M_PRICE,(SUM("); 
		sql.append("NVL(A.BALANCE_NETITEM_AMOUNT,0))) CAMPAIGN_FEE,\n" );
		sql.append("NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A , TM_BUSINESS_AREA B \n" );
		sql.append("WHERE 1=1\n" );
		sql.append(" AND A.YIELDLY = B.AREA_ID ");
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_06+"\n" );
		//sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		sql.append("AND A.STATUS not in (10791006,10791009,10791008,10791012,10791010,10791014,10791005)  ");
		sql.append(" AND A.BALANCE_YIELDLY = "+yieldly);
		paramList.add(condition.getDealerId());
		if(Utility.testString(condition.getEndBalanceDate())){
			paramList.add(condition.getEndBalanceDate());
			sql.append(" AND A.REPORT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(Utility.testString(condition.getConEndDay())){
			paramList.add(condition.getConEndDay());
			sql.append(" AND A.REPORT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		sql.append("GROUP BY A.YIELDLY,A.STATUS,B.AREA_NAME \n" );
		
		sql.append("UNION ALL\n" );
		//查询免费保养可结算的金额
		
		sql.append("SELECT 0 PDI,B.AREA_NAME,A.STATUS,COUNT(*) CLAIMCOUNT,0 LABOUR_AMOUNT,0 PART_AMOUNT,\n" );
		sql.append("0 NETITEM_AMOUNT,(SUM(NVL(A.BALANCE_AMOUNT,0)+NVL(A.BALANCE_LABOUR_AMOUNT,0)+"); 
		sql.append("NVL(A.BALANCE_PART_AMOUNT,0)+NVL(A.BALANCE_NETITEM_AMOUNT,0))+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) FREE_M_PRICE,0 CAMPAIGN_FEE,\n" );
		sql.append("NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_BUSINESS_AREA B \n" );
		sql.append("WHERE 1=1\n" );
		sql.append(" AND A.YIELDLY = B.AREA_ID ");
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_02+"\n" );
		sql.append("AND A.STATUS not in (10791006,10791009,10791008,10791012,10791010,10791014,10791005)  ");
		sql.append(" AND A.BALANCE_YIELDLY = "+yieldly);
		paramList.add(condition.getDealerId());
		if(Utility.testString(condition.getEndBalanceDate())){
			paramList.add(condition.getEndBalanceDate());
			sql.append(" AND A.REPORT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(Utility.testString(condition.getConEndDay())){
			paramList.add(condition.getConEndDay());
			sql.append(" AND A.REPORT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		sql.append("GROUP BY A.YIELDLY,A.STATUS,B.AREA_NAME\n" );
		sql.append(" UNION ALL \n" );
		sql.append("SELECT SUM(NVL(A.BALANCE_NETITEM_AMOUNT,0)) PDI,B.AREA_NAME,A.STATUS,COUNT(*) CLAIMCOUNT,0 LABOUR_AMOUNT,0 PART_AMOUNT,\n" );
		sql.append("0 NETITEM_AMOUNT,"); 
		sql.append("0 FREE_M_PRICE,0 CAMPAIGN_FEE,\n" );
		sql.append("NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_BUSINESS_AREA B \n" );
		sql.append("WHERE 1=1\n" );
		sql.append(" AND A.YIELDLY = B.AREA_ID ");
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_11+"\n" );
		sql.append("AND A.STATUS not in (10791006,10791009,10791008,10791012,10791010,10791014,10791005)  ");
		sql.append(" AND A.BALANCE_YIELDLY = "+yieldly);
		paramList.add(condition.getDealerId());
		if(Utility.testString(condition.getEndBalanceDate())){
			paramList.add(condition.getEndBalanceDate());
			sql.append(" AND A.REPORT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(Utility.testString(condition.getConEndDay())){
			paramList.add(condition.getConEndDay());
			sql.append(" AND A.REPORT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		sql.append("GROUP BY A.YIELDLY,A.STATUS,B.AREA_NAME\n" );
		
		sql.append(")GROUP BY AREA_NAME,STATUS");
		return this.pageQuery(sql.toString(), paramList, this.getFunName(), condition.getPageSize(), condition.getCurPage());
	}*/
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> dealerBalanceStatisQuery(DealerBalanceBean condition,String dealerId){
		StringBuffer sql= new StringBuffer();
		sql.append("select\n" );
		sql.append("       sum (claim_num) as claim_num,\n" );
		sql.append("       sum(hours_settlement_amount) as hours_settlement_amount,\n" );
		sql.append("       sum(part_settlement_amount) as part_settlement_amount,\n" );
		sql.append("       sum(pdi_settlement_amount) as pdi_settlement_amount,\n" );
		sql.append("       sum(first_settlement_amount) as first_settlement_amount,\n" );
		sql.append("       sum(activitie_settlement_amount) as activitie_settlement_amount,\n" );
		sql.append("       sum(outward_settlement_amount) as outward_settlement_amount,\n" );
		sql.append("       sum(settlement_total_amount) as settlement_total_amount,\n" );//结算总金额（不包括特殊费用）
		sql.append("  sum(pn_exaction) as pn_exaction,\n" );//正负激励总金额
		sql.append("  sum(good_claim_amount) as good_claim_amount,");//善于索赔总金额
		sql.append("  sum(LAST_ADMINISTRATION_AMOUNT) as LAST_ADMINISTRATION_AMOUNT,");//上次行政扣款金额
		sql.append("  sum(AUTH_PRICE) as AUTH_PRICE,\n");//运费
		sql.append("   decode(sign(round(sum(settlement_total_amount)+sum(pn_exaction)+sum(good_claim_amount)-sum(LAST_ADMINISTRATION_AMOUNT)+sum(AUTH_PRICE),2)),0,'0',1,'0',round(sum(settlement_total_amount)+sum(pn_exaction)+sum(good_claim_amount)-sum(LAST_ADMINISTRATION_AMOUNT)+sum(AUTH_PRICE),2)) as THIS_ADMINISTRATION_AMOUNT,");//本次行政扣款金额
		sql.append("  round(sum(settlement_total_amount)+sum(pn_exaction)+sum(good_claim_amount)-sum(LAST_ADMINISTRATION_AMOUNT)+sum(AUTH_PRICE),2) as claim_amount");//索赔总金额
		sql.append("\n" );
		sql.append("       from(SELECT count(wac.id) as claim_num,\n" );//索赔单个数
		sql.append("       sum(wac.hours_settlement_amount) as hours_settlement_amount,\n" );//工时结算金额
		sql.append("       sum(wac.part_settlement_amount) as part_settlement_amount,\n" );//配件结算金额
		sql.append("       sum(wac.pdi_settlement_amount) as pdi_settlement_amount,\n" );//pdi结算金额
		sql.append("       sum(wac.first_settlement_amount) as first_settlement_amount,\n" );//首保结算金额
		sql.append("       sum(wac.activitie_settlement_amount) as activitie_settlement_amount,\n" );//服务活动结算金额
		sql.append("       sum(wac.outward_settlement_amount) as outward_settlement_amount,\n" );//外出维修结算金额
		sql.append("       sum(wac.settlement_total_amount) as settlement_total_amount,\n" );//结算总金额（不包含特殊费用）
		sql.append("       0 as pn_exaction,\n" );
		sql.append("       0 as good_claim_amount,\n" );
		sql.append("       0 as LAST_ADMINISTRATION_AMOUNT,\n" );
		sql.append("       0 as AUTH_PRICE\n");
		
		sql.append("  from TT_AS_WR_APPLICATION_CLAIM wac\n" );
		sql.append(" where (wac.REPAIR_TYPE in\n" );
		sql.append("       (11441004, 11441008) or\n" );
		sql.append("       (wac.REPAIR_TYPE = 11441005 and wac.activity_type != 96281001))\n" );
		sql.append("   and to_date(to_char(wac.AUTH_AUDIT_DATE, 'yyyy-mm-dd'), 'yyyy-mm-dd') <=\n" );
		sql.append("       TO_DATE('"+condition.getConEndDay()+"','YYYY-MM-DD')\n" );
		sql.append("   and wac.STATUS in (19991005)\n" );
		sql.append("   and wac.is_bill=10041002\n" );
		sql.append("   and to_date(to_char(wac.AUTH_AUDIT_DATE, 'yyyy-mm-dd'), 'yyyy-mm-dd') >=\n" );
		sql.append("       TO_DATE('"+condition.getEndBalanceDate()+"','YYYY-MM-DD')\n" );
		sql.append("   and wac.dealer_id="+dealerId+"--无旧件的\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("   union all\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("   SELECT count(wac.id) as claim_num,\n" );
		sql.append("       sum(wac.hours_settlement_amount) as hours_settlement_amount,\n" );
		sql.append("       sum(wac.part_settlement_amount) as part_settlement_amount,\n" );
		sql.append("       sum(wac.pdi_settlement_amount) as pdi_settlement_amount,\n" );
		sql.append("       sum(wac.first_settlement_amount) as first_settlement_amount,\n" );
		sql.append("       sum(wac.activitie_settlement_amount) as activitie_settlement_amount,\n" );
		sql.append("       sum(wac.outward_settlement_amount) as outward_settlement_amount,\n" );
		sql.append("       sum(wac.settlement_total_amount) as settlement_total_amount,\n" );
		sql.append("       0 as pn_exaction,\n" );
		sql.append("       0 as good_claim_amount,\n" );
		sql.append("       0 as LAST_ADMINISTRATION_AMOUNT,\n" );
		sql.append("       0 as AUTH_PRICE\n");
		sql.append("  from TT_AS_WR_APPLICATION_CLAIM wac\n" );
		sql.append("  inner join\n" );
		sql.append("  (select distinct od.claim_id from TT_AS_WR_RETURNED_ORDER o\n" );
		sql.append("          inner join Tt_As_Wr_Returned_Order_Detail od\n" );
		sql.append("          on o.id=od.return_id\n" );
		sql.append("          where o.dealer_id="+dealerId+"\n" );
		sql.append("          and to_char(o.wr_start_date, 'yyyy-mm-dd') = '"+condition.getEndBalanceDate()+"'\n" );
		sql.append("          and to_char(o.return_end_date, 'yyyy-mm-dd') = '"+condition.getConEndDay()+"'\n" );
		sql.append("          and o.status=10811005\n" );
		sql.append("          and o.IS_BILL=10041002\n" );
		sql.append("          ) odo\n" );
		sql.append("  on wac.id=odo.claim_id\n" );
		sql.append(" where wac.REPAIR_TYPE in\n" );
		sql.append("       (11441001, 11441002, 11441003, 11441006, 11441009)\n" );
		sql.append("   and to_date(to_char(wac.AUTH_AUDIT_DATE, 'yyyy-mm-dd'), 'yyyy-mm-dd') <=\n" );
		sql.append("       TO_DATE('"+condition.getConEndDay()+"','YYYY-MM-DD')\n" );
		sql.append("   and wac.STATUS in (19991005)\n" );
		sql.append("   and wac.is_bill=10041002\n" );
		sql.append("   and to_date(to_char(wac.AUTH_AUDIT_DATE, 'yyyy-mm-dd'), 'yyyy-mm-dd') >=\n" );
		sql.append("       TO_DATE('"+condition.getEndBalanceDate()+"','YYYY-MM-DD')\n" );
		sql.append("   and wac.dealer_id="+dealerId+"--去除(首保，pdi,服务活动)有旧件的\n" );
		
		sql.append("   union all\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("   SELECT count(wac.id) as claim_num,\n" );
		sql.append("      0 as hours_settlement_amount,\n" );
		sql.append("      0 as part_settlement_amount,\n" );
		sql.append("      0 as pdi_settlement_amount,\n" );
		sql.append("      0 as first_settlement_amount,\n" );
		sql.append("      round(sum(wac.hours_settlement_amount)+sum(wac.part_settlement_amount),2) as activitie_settlement_amount,\n" );
		sql.append("      0 as outward_settlement_amount,\n" );
		sql.append("      sum(wac.settlement_total_amount) as settlement_total_amount,\n" );
		sql.append("      0 as pn_exaction,\n" );
		sql.append("      0 as good_claim_amount,\n" );
		sql.append("      0 as LAST_ADMINISTRATION_AMOUNT,\n");
		sql.append("      0 as AUTH_PRICE\n");
		sql.append("  from TT_AS_WR_APPLICATION_CLAIM wac\n" );
		sql.append("  inner join\n" );
		sql.append("  (select distinct od.claim_id from TT_AS_WR_RETURNED_ORDER o\n" );
		sql.append("          inner join Tt_As_Wr_Returned_Order_Detail od\n" );
		sql.append("          on o.id=od.return_id\n" );
		sql.append("          where o.dealer_id="+dealerId+"\n" );
		sql.append("          and to_char(o.wr_start_date, 'yyyy-mm-dd') = '"+condition.getEndBalanceDate()+"'\n" );
		sql.append("          and to_char(o.return_end_date, 'yyyy-mm-dd') = '"+condition.getConEndDay()+"'\n" );
		sql.append("          and o.status=10811005\n" );
		sql.append("          and o.IS_BILL=10041002\n" );
		sql.append("          ) odo\n" );
		sql.append("  on wac.id=odo.claim_id\n" );
		sql.append(" where \n" );
		sql.append("       wac.REPAIR_TYPE = 11441005 and wac.activity_type = 96281001\n" );
		sql.append("   and to_date(to_char(wac.AUTH_AUDIT_DATE, 'yyyy-mm-dd'), 'yyyy-mm-dd') <=\n" );
		sql.append("       TO_DATE('"+condition.getConEndDay()+"','YYYY-MM-DD')\n" );
		sql.append("   and wac.STATUS in (19991005)\n" );
		sql.append("   and wac.is_bill=10041002\n" );
		sql.append("   and to_date(to_char(wac.AUTH_AUDIT_DATE, 'yyyy-mm-dd'), 'yyyy-mm-dd') >=\n" );
		sql.append("       TO_DATE('"+condition.getEndBalanceDate()+"','YYYY-MM-DD')\n" );
		sql.append("   and wac.dealer_id="+dealerId+"--服务活动技术升级（有旧件的）\n" );
		sql.append("union all\n" );
		sql.append("   select 0,0,0,0,0,0,0,0,sum(af.labour_sum) as pn_exaction,0,0,0 from tt_as_wr_fine af where af.pay_status=11511001 and af.dealer_id="+dealerId+"\n" );//正负激励总金额
		sql.append("\n" );
		sql.append("   union all\n" );
		sql.append("   select 0,0,0,0,0,0,0,0,0,sum(t.audit_amount) as good_claim_amount,0,0 from tt_as_wr_Special t where t.DEALER_ID="+dealerId+" and t.status=20331014 and t.is_claim=10041002 and t.claim_no is null");//善于索赔总金额
		sql.append("   union all\n" );
		sql.append("   select 0,0,0,0,0,0,0,0,0,0,t.labour_sum as LAST_ADMINISTRATION_AMOUNT,0 from tt_as_wr_administrative_charge t where t.dealerid="+dealerId+" and t.status=94151002");//上次行政扣款金额
		sql.append("   union all\n" );
		sql.append("   select 0,0,0,0,0,0,0,0,0,0,0,o.AUTH_PRICE as AUTH_PRICE from TT_AS_WR_RETURNED_ORDER o where o.dealer_id = "+dealerId+"\n");
		sql.append("          and to_char(o.wr_start_date, 'yyyy-mm-dd') = '"+condition.getEndBalanceDate()+"' and to_char(o.return_end_date, 'yyyy-mm-dd') = '"+condition.getConEndDay()+"' and o.status = 10811005 and o.IS_BILL = 10041002");//运费

		sql.append("   )");

			return this.pageQuery(sql.toString(), null, this.getFunName(), condition.getPageSize(), condition.getCurPage());
		}
	/**
	 * zyw 重新优化逻辑改造
	 * @param dealerId
	 * @param startTime
	 * @param endTime
	 * @param yld
	 * @param yieldly
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List dealerBalanOrder(String dealerId, String startTime, String endTime, long yld,String yieldly){
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT COUNT(*) CLAIMCOUNT,\n" );
		sb.append("       (NVL(SUM(A.BALANCE_LABOUR_AMOUNT), 0) +\n" );
		sb.append("       NVL(SUM(A.BALANCE_APPENDLABOUR_AMOUNT), 0)) LABOUR_AMOUNT,\n" );
		sb.append("       NVL(SUM(A.BALANCE_PART_AMOUNT), 0) PART_AMOUNT,\n" );
		sb.append("       NVL(SUM(A.BALANCE_NETITEM_AMOUNT), 0) NETITEM_AMOUNT,\n" );
		sb.append("       NVL(SUM(A.FREE_M_PRICE), 0) FREE_M_PRICE,\n" );
		sb.append("       NVL(SUM(A.CAMPAIGN_FEE), 0) CAMPAIGN_FEE,\n" );
		sb.append("       NVL(SUM(A.REPAIR_TOTAL), 0) REPAIR_TOTAL,\n" );
		sb.append("       NVL(SUM(A.BALANCE_AMOUNT), 0) BALANCE_AMOUNT,\n" );
		sb.append("       0 labour_price,\n" );
		sb.append("       0 part_price\n" );
		sb.append("  FROM TT_AS_WR_APPLICATION A, TM_BUSINESS_AREA B\n" );
		sb.append(" WHERE 1 = 1\n" );
		sb.append("   and A.IS_IMPORT = 10041002\n" );
		sb.append("   and A.is_invoice = 0\n" );
		sb.append("   AND A.YIELDLY = B.AREA_ID\n" );
		sb.append("   AND A.CLAIM_TYPE not in (10661006)\n" );
		sb.append("   AND A.STATUS = 10791007\n" );
		sb.append("   AND A.BALANCE_YIELDLY = 95411001\n" );
		DaoFactory.getsql(sb, "A.DEALER_ID", dealerId, 1);
		DaoFactory.getsql(sb, "A.YIELDLY", String.valueOf(yld), 1);
		DaoFactory.getsql(sb, "A.REPORT_DATE", startTime, 3);
		DaoFactory.getsql(sb, "A.REPORT_DATE", endTime, 4);
		return this.pageQuery(sb.toString(), null, this.getFunName());
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> claimSe(String ids,String APP_CLAIM_NO,int curPage,int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select t.id,--索赔单ID\n" );
		sql.append("       t.APP_CLAIM_NO,--索赔单号\n" );
		sql.append("       t.service_order_code,--服务工单编码\n" );
		sql.append("       t.service_order_id,--服务工单id\n" );
		sql.append("       t.dealer_id,--经销商id\n" );
		sql.append("       d.dealer_code,--经销商代码\n" );
		sql.append("       d.dealer_name,--经销商名称\n" );
		sql.append("       d.PHONE,--经销商电话\n" );
		sql.append("       t.repair_type,--维修类型\n" );
		sql.append("       (SELECT c.code_desc FROM tc_code c where c.code_id=t.repair_type) as repair_name,--维修类型\n" );
		sql.append("       TO_CHAR(t.arrival_date, 'YYYY-MM-DD') as arrival_date,--进站时间\n" );
		sql.append("       TO_CHAR(t.repair_date_begin, 'YYYY-MM-DD') as repair_date_begin,--维修开始时间\n" );
		sql.append("       TO_CHAR(t.repair_date_end, 'YYYY-MM-DD') as repair_date_end,--维修结束时间\n" );
		sql.append("       nvl(t.mileage, 0) as mileage,--进站里程数\n" );
		sql.append("       t.receptionist_man,--接待人\n" );
		sql.append("       t.vin,--车架号\n" );
		sql.append("       v.engine_no,--发动机号\n" );
		sql.append("       v.license_no,--车牌号\n" );
		sql.append("B.BRAND_NAME, --品牌\n" );
		sql.append("B.SERIES_NAME, --车系名称\n" );
		sql.append("B.MODEL_NAME, --车型名称\n" );
		sql.append("B.PACKAGE_NAME, --配置\n" );
		sql.append("TO_CHAR(w.E_START_DATE, 'YYYY-MM-DD') as E_START_DATE, --救援开始时间\n" );
		sql.append("TO_CHAR(w.E_END_DATE, 'YYYY-MM-DD') as E_END_DATE, --救援结束时间\n" );
		sql.append("w.E_NUM, --救援人数\n" );
		sql.append("w.E_NAME, --救援人姓名\n" );
		sql.append("w.E_LICENSE_NO, --派车车牌号\n" );
		sql.append("       TO_CHAR(v.PURCHASED_DATE, 'YYYY-MM-DD')as PURCHASED_DATE,--购车日期\n" );
		sql.append("       TO_CHAR(v.PRODUCT_DATE, 'YYYY-MM-DD')as PRODUCT_DATE,--生产日期\n" );
		sql.append("        E.CTM_NAME,--车主姓名\n" );
		sql.append("       G.RULE_NAME,--三包规则名称\n" );		
		sql.append("       t.deliverer_man_name,--送修人姓名\n" );
		sql.append("       t.deliverer_man_phone,--送修人电话\n" );
		sql.append("       a.AREA_NAME, --产地名称\n" );
		sql.append("       t.fault_desc,--故障描述\n" );
		sql.append("       t.fault_reason,--故障原因\n" );
		sql.append("       t.repair_method,--申请内容维修措施\n" );
		sql.append("       t.Apply_remark,--申请内容，申请备注\n" );
		sql.append("       t.Apply_remark AS remark,--申请内容，申请备注\n" );
		sql.append("       t.auth_audit_by,--授权审核人\n" );
		sql.append("       t.auth_audit_date,--授权审核时间\n" );
		sql.append("       t.status,\n" );
		sql.append("       (SELECT c.code_desc FROM tc_code c where c.code_id=t.status) as status_name,--状态\n" );
		sql.append("       nvl(t.OUTWARD_APPLY_AMOUNT, 0) as OUTWARD_APPLY_AMOUNT,--外出申请费用\n" );
		sql.append("       nvl(t.OUTWARD_SETTLEMENT_AMOUNT, 0) as OUTWARD_SETTLEMENT_AMOUNT,--外出结算费用\n" );
		sql.append("       nvl(t.PDI_APPLY_AMOUNT, 0) as PDI_APPLY_AMOUNT,--pdi金额\n" );
		sql.append("       nvl(t.PDI_SETTLEMENT_AMOUNT, 0) as PDI_SETTLEMENT_AMOUNT,--pdi结算金额\n" );
		sql.append("       t.PDI_RESULT as PDI_REMARK,--pdi结果\n" );
		sql.append("       wa.ACTIVITY_TYPE,--服务活动类型\n" );
		sql.append("       (SELECT c.code_desc FROM tc_code c where c.code_id=wa.ACTIVITY_TYPE) as ACTIVITY_NAME,--服务活动类型\n" );
		sql.append("       wa.ACTIVITY_DISCOUNT,--服务活动折扣率\n" );
		sql.append("       nvl(t.ACTIVITIE_APPLY_AMOUNT, 0) as ACTIVITIE_APPLY_AMOUNT,--服务活动金额(折扣价)\n" );
		sql.append("       nvl(t.ACTIVITIE_SETTLEMENT_AMOUNT, 0) as ACTIVITIE_SETTLEMENT_AMOUNT,--服务活动金额(折扣价)\n" );
		sql.append("       t.MAINTENANCE_TIME as CUR_FREE_TIMES,--保养次数\n" );
		sql.append("       nvl(t.FIRST_APPLY_AMOUNT, 0) as APPLY_MAINTAIN_PRICE,--保养费用\n" );
		sql.append("       nvl(t.FIRST_SETTLEMENT_AMOUNT, 0) as FIRST_SETTLEMENT_AMOUNT,--保养费用\n" );
		sql.append("       t.EGRESS_ID,--外出维修id\n" );
		sql.append("       nvl(t.PART_APPLY_AMOUNT, 0) as PART_APPLY_AMOUNT,--配件申请费用\n" );
		sql.append("       nvl(t.PART_SETTLEMENT_AMOUNT, 0) as PART_SETTLEMENT_AMOUNT,--配件结算费用\n" );
		sql.append("       nvl(t.HOURS_APPLY_AMOUNT, 0) as HOURS_APPLY_AMOUNT,--工时申请费用\n" );
		sql.append("       nvl(t.HOURS_SETTLEMENT_AMOUNT, 0) as HOURS_SETTLEMENT_AMOUNT,--工时结算费用\n" );
		sql.append("       nvl(t.APPLY_TOTAL_AMOUNT, 0) as APPLY_TOTAL_AMOUNT,--申请总费用\n" );
		sql.append("       nvl(t.SETTLEMENT_TOTAL_AMOUNT, 0) as SETTLEMENT_TOTAL_AMOUNT,--结算总费用\n" );
		sql.append("       TO_CHAR(t.create_date, 'YYYY-MM-DD') as create_date--创建时间\n" );
		sql.append("  from TT_AS_WR_APPLICATION_CLAIM t\n" );
		sql.append("  left join tm_dealer d on t.dealer_id=d.dealer_id \n" );
		sql.append("  left join tm_vehicle v on t.vin=v.vin \n" );
		sql.append("  LEFT JOIN TM_BUSINESS_AREA a ON a.AREA_ID = v.AREA_ID \n" );
		sql.append("  LEFT JOIN TT_AS_WR_GAME F  ON  v.CLAIM_TACTICS_ID = F.ID\n" );
		sql.append("  LEFT JOIN TT_AS_WR_RULE G ON F.RULE_ID = G.ID\n" );
		sql.append("LEFT JOIN TT_DEALER_ACTUAL_SALES s ON s.VEHICLE_ID = v.VEHICLE_ID\n" );
		sql.append("LEFT JOIN TT_CUSTOMER E ON s.CTM_ID = E.CTM_ID\n" );
		sql.append("LEFT JOIN TT_AS_EGRESS w ON w.ID = t.EGRESS_ID\n" );//外出维修
		sql.append("LEFT JOIN TT_AS_WR_ACTIVITY wa ON t.ACTIVITY_ID = wa.ACTIVITY_ID\n" );//服务活动	
		sql.append("  LEFT JOIN (SELECT DISTINCT PACKAGE_ID, PACKAGE_NAME, MODEL_ID, MODEL_NAME, SERIES_NAME ,BRAND_NAME FROM VW_MATERIAL_GROUP_MAT) B ON v.PACKAGE_ID = B.PACKAGE_ID\n" );
		sql.append(" where 1=1 \n" );
		if(!"".equals(APP_CLAIM_NO) && APP_CLAIM_NO!=null){
			sql.append(" and t.APP_CLAIM_NO like '%"+APP_CLAIM_NO+"%'\n" );
		}
		sql.append(" and t.ID in ("+ids+")");
		sql.append(" order by t.create_date desc");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}


public List dealerBalanOrderSend(String dealerId, String startTime, String endTime, long yld,String yieldly){
	
	List<Object> paramList = new ArrayList<Object>();
	
	StringBuilder sql= new StringBuilder();
	sql.append(" SELECT sum(CLAIMCOUNT) CLAIMCOUNT, SECOND_DEALER_ID ,SECOND_DEALER_CODE,sum(LABOUR_AMOUNT) LABOUR_AMOUNT, ");
	sql.append(" sum(PART_AMOUNT) PART_AMOUNT , sum(NETITEM_AMOUNT) NETITEM_AMOUNT ,sum(FREE_M_PRICE) FREE_M_PRICE ,sum(CAMPAIGN_FEE) CAMPAIGN_FEE, sum(REPAIR_TOTAL) REPAIR_TOTAL, sum(BALANCE_AMOUNT) BALANCE_AMOUNT from (  ");
	
	sql.append("SELECT COUNT(*) CLAIMCOUNT,A.SECOND_DEALER_ID SECOND_DEALER_ID,A.SECOND_DEALER_CODE SECOND_DEALER_CODE,(NVL(SUM(NVL(A.BALANCE_AMOUNT,0)),0)- NVL(SUM(NVL(A.BALANCE_PART_AMOUNT,0)),0)-NVL(SUM(decode(A.CLAIM_TYPE,10661002,mg.part_price,0) ),0)) LABOUR_AMOUNT,(NVL(SUM(NVL(A.BALANCE_PART_AMOUNT,0)),0) + NVL(SUM(decode(A.CLAIM_TYPE,10661002,mg.part_price,0) ),0)  ) PART_AMOUNT,\n" );
	sql.append("NVL(SUM(NVL(A.BALANCE_NETITEM_AMOUNT,0)),0) NETITEM_AMOUNT,NVL(SUM(NVL(A.FREE_M_PRICE,0)),0) FREE_M_PRICE,NVL(SUM(NVL(A.CAMPAIGN_FEE,0)),0) CAMPAIGN_FEE,\n" );
	sql.append("NVL(SUM(NVL(A.REPAIR_TOTAL,0)),0) REPAIR_TOTAL,NVL(SUM(NVL(A.BALANCE_AMOUNT,0)),0) BALANCE_AMOUNT\n" );
	sql.append("FROM TT_AS_WR_APPLICATION A, TM_BUSINESS_AREA B,TT_AS_WR_MODEL_ITEM mi, tm_vehicle v ,tt_as_wr_model_group mg \n" );
	sql.append("WHERE 1=1\n" );
	
	sql.append(" and v.package_id = mi.model_id\n" );
	sql.append("  and mg.wrgroup_id = mi.wrgroup_id\n" );
	sql.append("  and v.vin=A.VIN\n" );
	sql.append(" AND  MG.WRGROUP_TYPE=10451001");
	
	sql.append(" AND A.YIELDLY =" + yld);
	sql.append(" AND A.YIELDLY = B.AREA_ID ");
	sql.append("AND A.DEALER_ID = ?\n");
	sql.append("AND  A.STATUS not in (10791006,10791009,10791008,10791012,10791010,10791014,10791016,10791005)  ");
	sql.append(" AND A.BALANCE_YIELDLY = "+yieldly+"  " );
	sql.append(" AND A.CLAIM_TYPE != 10661006     ");
	sql.append(" AND A.SECOND_DEALER_ID is not null ");
	paramList.add(dealerId);
	if(Utility.testString(startTime)){
		paramList.add(startTime);
		sql.append("AND A.REPORT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
	}
	if(Utility.testString(endTime)){
		paramList.add(endTime);
		sql.append("AND A.REPORT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
	}
	sql.append("  GROUP by A.SECOND_DEALER_ID,A.SECOND_DEALER_CODE"); 
	
	sql.append(" UNION ");
	
	sql.append("SELECT COUNT(*) CLAIMCOUNT,A.SECOND_DEALER_ID SECOND_DEALER_ID,A.SECOND_DEALER_CODE SECOND_DEALER_CODE,(NVL(SUM(NVL(A.BALANCE_AMOUNT,0)),0)- NVL(SUM(NVL(A.BALANCE_PART_AMOUNT,0)),0)) LABOUR_AMOUNT,NVL(SUM(NVL(A.BALANCE_PART_AMOUNT,0)),0) PART_AMOUNT,\n" );
	sql.append("NVL(SUM(NVL(A.BALANCE_NETITEM_AMOUNT,0)),0) NETITEM_AMOUNT,NVL(SUM(NVL(A.FREE_M_PRICE,0)),0) FREE_M_PRICE,NVL(SUM(NVL(A.CAMPAIGN_FEE,0)),0) CAMPAIGN_FEE,\n" );
	sql.append("NVL(SUM(NVL(A.REPAIR_TOTAL,0)),0) REPAIR_TOTAL,NVL(SUM(NVL(A.BALANCE_AMOUNT,0)),0) BALANCE_AMOUNT\n" );
	sql.append("FROM TT_AS_WR_APPLICATION A, TM_BUSINESS_AREA B \n" );
	sql.append("WHERE 1=1\n" );
	sql.append(" AND A.YIELDLY =" + yld);
	sql.append(" AND A.YIELDLY = B.AREA_ID ");
	sql.append("AND A.DEALER_ID = ?\n");
	sql.append("AND  A.STATUS not in (10791006,10791009,10791008,10791012,10791010,10791014,10791016,10791005)  ");
	sql.append(" AND A.BALANCE_YIELDLY = "+yieldly+"  " );
	sql.append(" AND A.CLAIM_TYPE = 10661006     ");
	sql.append(" AND A.SECOND_DEALER_ID is not null ");
	paramList.add(dealerId);
	if(Utility.testString(startTime)){
		paramList.add(startTime);
		sql.append("AND A.REPORT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
	}
	if(Utility.testString(endTime)){
		paramList.add(endTime);
		sql.append("AND A.REPORT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
	}
	sql.append("  GROUP by A.SECOND_DEALER_ID,A.SECOND_DEALER_CODE )"); 
	sql.append(" GROUP by SECOND_DEALER_ID,SECOND_DEALER_CODE ");
	
	
	return this.pageQuery(sql.toString(), paramList, this.getFunName());
}

public List<Map<String,Object>> get_administrative_charge (String dealerId)
{
	StringBuffer sql= new StringBuffer();
	sql.append("select *\n" );
	sql.append("  from tt_as_wr_administrative_charge t\n" );
	sql.append(" where t.DEALERID in(select d.dealer_id from tm_dealer d start with d.dealer_id = "+dealerId+" connect by PRIOR d.dealer_id = d.parent_dealer_d )\n" );
	sql.append("   and t.STATUS = 94151002");
	 List<Map<String,Object>> list= this.pageQuery(sql.toString(), null, this.getFunName());
	return list;
}


public List<Map<String,Object>> getyxc_sum(String dealerId)
{
	double d = 0d;
	StringBuffer sql= new StringBuffer();
	sql.append("select (nvl(c.BALANCE_AMOUNT,0)) BALANCE_AMOUNT \n" );
	sql.append("  from   \n" );
	sql.append("          \n" );
	sql.append("       TT_AS_ACTIVITY_SUBJECT b,\n" );
	sql.append("       tt_as_subjiet_evaluate c\n" );
	sql.append(" where b.EVALUATE = 2\n" );
	sql.append("   and b.SUBJECT_ID = c.SUBJECT_ID\n" );
	sql.append("   and c.BALANCE_ODER is null    and c.DEALER_ID = \n" + dealerId);
	List<Map<String,Object>> map= this.pageQuery(sql.toString(), null, this.getFunName());
	if(map != null && map.size() > 0)
	{
		return map;
	}
    return null;
}

public List<Map<String,Object>> getyxc_sum_second_dealer_id(String dealerId)
{
	double d = 0d;
	StringBuffer sql= new StringBuffer();
	sql.append("select (sum(nvl(t.BALANCE_AMOUNT,0) ) - min(nvl(c.BALANCE_AMOUNT,0))) BALANCE_AMOUNT,\n" );
	sql.append("       min(c.TAX_RATE_FEE) TAX_RATE_FEE\n" );
	sql.append("  from tt_as_wr_application   t,\n" );
	sql.append("       TT_AS_ACTIVITY         a,\n" );
	sql.append("       TT_AS_ACTIVITY_SUBJECT b,\n" );
	sql.append("       tt_as_subjiet_evaluate c\n" );
	sql.append(" where b.EVALUATE = 2\n" );
	sql.append("   and b.SUBJECT_ID = c.SUBJECT_ID\n" );
	sql.append("   and a.SUBJECT_ID = b.SUBJECT_ID\n" );
	sql.append("   and t.CAMPAIGN_CODE = a.ACTIVITY_CODE\n" );
	sql.append("   and t.DEALER_ID = c.DEALER_ID\n" );
	sql.append("   and t.SECOND_DEALER_ID = \n" + dealerId);
	sql.append("   and t.STATUS != 10791006\n" );
	sql.append("   group by c.SUBJECT_ID");
   
	List<Map<String,Object>> map= this.pageQuery(sql.toString(), null, this.getFunName());
	if(map != null && map.size() > 0)
	{
		return map;
	}
    return null;
}


@SuppressWarnings("unchecked")
public List dealerBalanOrderFWJ(String dealerId, String startTime, String endTime, long yld,String yieldly){
	
	List<Object> paramList = new ArrayList<Object>();
	
	StringBuilder sql= new StringBuilder();
	//查询非服务活动可结算金额
	sql.append("SELECT COUNT(*) CLAIMCOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.BALANCE_APPENDLABOUR_AMOUNT),0)) LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) PART_AMOUNT,\n" );
	sql.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) NETITEM_AMOUNT,NVL(SUM(A.FREE_M_PRICE),0) FREE_M_PRICE,NVL(SUM(A.CAMPAIGN_FEE),0) CAMPAIGN_FEE,\n" );
	sql.append("NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT\n" );
	sql.append("FROM TT_AS_WR_APPLICATION A, TM_BUSINESS_AREA B \n" );
	sql.append("WHERE 1=1\n" );
	sql.append("  and A.is_invoice = 0 AND A.YIELDLY =" + yld);
	sql.append(" AND A.YIELDLY = B.AREA_ID ");
	sql.append("AND A.DEALER_ID = ? and A.IS_IMPORT=10041002\n");
	sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_06+"\n" );
	sql.append("AND  A.STATUS =10791007  ");
	sql.append(" AND A.BALANCE_YIELDLY = "+yieldly+"  " );
	paramList.add(dealerId);
	if(Utility.testString(startTime)){
		paramList.add(startTime);
		sql.append("AND A.REPORT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
	}
	if(Utility.testString(endTime)){
		paramList.add(endTime);
		sql.append("AND A.REPORT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
	}
	return this.pageQuery(sql.toString(), paramList, this.getFunName());
}

public List dealerBalanOrderFWY(String dealerId, String startTime, String endTime, long yld,String yieldly){
	
	TtAsWrBalanceDetailTemPO temPO = new TtAsWrBalanceDetailTemPO(); 
	
	List<Object> paramList = new ArrayList<Object>();
	
	StringBuilder sql= new StringBuilder();
	//查询非服务活动可结算金额
	sql.append("SELECT COUNT(*) CLAIMCOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.BALANCE_APPENDLABOUR_AMOUNT),0)) LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) PART_AMOUNT,\n" );
	sql.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) NETITEM_AMOUNT,NVL(SUM(A.FREE_M_PRICE),0) FREE_M_PRICE,NVL(SUM(A.CAMPAIGN_FEE),0) CAMPAIGN_FEE,\n" );
	sql.append("NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT\n" );
	sql.append("FROM TT_AS_WR_APPLICATION A, TM_BUSINESS_AREA B \n" );
	sql.append("WHERE 1=1\n" );
	sql.append(" AND A.YIELDLY =" + yld);
	sql.append(" AND A.YIELDLY = B.AREA_ID ");
	sql.append("AND A.DEALER_ID = ?\n");
	sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_06+"\n" );
	sql.append("AND  A.STATUS not in (10791006,10791009,10791008,10791012,10791010,10791014,10791016,10791005)  ");
	sql.append(" AND A.BALANCE_YIELDLY = "+yieldly+"  " );
	sql.append(" AND A.CAMPAIGN_CODE not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
	paramList.add(dealerId);
	if(Utility.testString(startTime)){
		paramList.add(startTime);
		sql.append("AND A.REPORT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
	}
	if(Utility.testString(endTime)){
		paramList.add(endTime);
		sql.append("AND A.REPORT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
	}
	
	return this.pageQuery(sql.toString(), paramList, this.getFunName());
}

public List dealerBalanOrderJJD(String dealerId, String startTime, String endTime, long yld,String yieldly){
	
	List<Object> paramList = new ArrayList<Object>();
	
	StringBuffer sql= new StringBuffer();
	sql.append("select nvl( sum( t.DISCOUNT) , 0)  DISCOUNT  from tt_as_wr_discount t\n" );
	sql.append("where     t.DEALER_ID = ? and t.DISCOUNT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
	sql.append("and t.DISCOUNT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
	paramList.add(dealerId);
    paramList.add(startTime);
	paramList.add(endTime);
	return this.pageQuery(sql.toString(), paramList, this.getFunName());
}
	
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getArrClaim(long dealerId,String endBalanceDate,String conEndDay){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append(" SELECT * FROM TT_AS_WR_APPLICATION A  \n" );
		sbSql.append(" WHERE  1=1 \n");
		DaoFactory.getsql(sbSql, "A.DEALER_ID", String.valueOf(dealerId), 1);
		DaoFactory.getsql(sbSql, "A.report_DATE", endBalanceDate, 3);
		DaoFactory.getsql(sbSql, "A.report_DATE", conEndDay, 4);
		DaoFactory.getsql(sbSql, "A.STATUS", "10791009,10791008", 8);
		return this.pageQuery(sbSql.toString(), null, this.getFunName());
	}
	/**
	 * 查询开票经销商
	 * @param dealerId 经销商ID
	 * @return TmDealerPO
	 */
	public TmDealerPO queryInvoiceMaker(Long dealerId) throws Exception{
		
		TmDealerPO resultPO = null;
		/*******add by liuxh 20101115 根据经销商开票级别取得经销商信息******/
		TmDealerPO dealerCon=new TmDealerPO();
		dealerCon.setDealerId(dealerId);
		dealerCon=factory.select(dealerCon).get(0);
		String dealerLevel=dealerCon.getDealerLevel().toString();//经销商等级
		String invoiceLevel=CommonUtils.checkNull(dealerCon.getInvoiceLevel()); //经销商开票级别
		if(dealerLevel.equals(Constant.DEALER_LEVEL_01.toString())) //一级经销商
		{
			resultPO=dealerCon;//一级经销商开票单位为自己
		}
		else//非一级经销商
		{		
			if(invoiceLevel.equals(Constant.INVOICE_LEVEL_SELF)){//独立开票
				resultPO=dealerCon;
			}else if(invoiceLevel.equals(Constant.INVOICE_LEVEL_HIGH)){//上级开票
				StringBuilder sql= new StringBuilder();
				sql.append("SELECT A.* FROM TM_DEALER A\n" );
				sql.append("WHERE A.DEALER_TYPE =? \n");
				sql.append("START WITH A.DEALER_ID = ?\n" );
				sql.append("CONNECT BY A.DEALER_ID = PRIOR A.PARENT_DEALER_D"); //向上层遍历
				
				List<Object> paramList = new ArrayList<Object>();
				paramList.add(Constant.DEALER_TYPE_DWR);
				paramList.add(dealerId);
				List<TmDealerPO> resultList = this.select(TmDealerPO.class,sql.toString(),paramList);
				for(TmDealerPO dealer:resultList){
					String curInvoiceLevel=dealer.getInvoiceLevel();
					if(curInvoiceLevel.equals(Constant.INVOICE_LEVEL_SELF)){
						resultPO=dealer;
						break;
					}else{
						String curDealerLevel=dealer.getDealerLevel().toString();
						if(curDealerLevel.equals(Constant.DEALER_LEVEL_01.toString())){//如果一级经销商设置为上级开票 取一级经销商为开票单位
							resultPO=dealer;
							break;
						}
						continue;
					}
				}
				
			}else{
				throw new BizException("开票级别值设置错误!"+invoiceLevel);
			}
			/*******add by liuxh 20101115 根据经销商开票级别取得经销商信息******/
		}
		return resultPO;
	}
	/**
	 * 查询结算经销商
	 * @param dealerId 经销商ID
	 * @return TmDealerPO
	 */
	/******add by liuxh 20101115 增加查询结算单位******/
	public TmDealerPO queryBalanceMaker(Long dealerId) throws Exception{
		
		TmDealerPO resultPO = null;
		TmDealerPO dealerCon=new TmDealerPO();
		dealerCon.setDealerId(dealerId);
		dealerCon=factory.select(dealerCon).get(0);
		String invoiceLevel=CommonUtils.checkNull(dealerCon.getBalanceLevel()); //经销商结算
		if(invoiceLevel.equals(Constant.BALANCE_LEVEL_SELF)){//独立结算
			resultPO=dealerCon;
		}else if(invoiceLevel.equals(Constant.BALANCE_LEVEL_HIGH)){//上级结算
			StringBuilder sql= new StringBuilder();
			sql.append("SELECT A.* FROM TM_DEALER A\n" );
			sql.append("WHERE 1=1\n" );
			sql.append("AND A.DEALER_LEVEL = ?\n" );
			sql.append("START WITH A.DEALER_ID = ?\n" );
			sql.append("CONNECT BY A.DEALER_ID = PRIOR A.PARENT_DEALER_D");
			
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(Constant.DEALER_LEVEL_01);//查询一级经销商
			paramList.add(dealerId);

			List<TmDealerPO> resultList = this.select(TmDealerPO.class,sql.toString(),paramList);
			if(resultList!=null && resultList.size()>0)
				resultPO = resultList.get(0);
		}else{
			throw new BizException("结算级别值设置错误!"+invoiceLevel);
		}
		
		return resultPO;
	}
	
	
	/**
	 * 查询省份信息
	 * @param regionCode 地区代码
	 * @return
	 */
	public TmRegionPO queryProvince(String regionCode){
		
		String sql = "SELECT REGION_ID,REGION_CODE,REGION_NAME\n" +
				" FROM TM_REGION WHERE REGION_CODE = ?";
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(regionCode==null?"":regionCode);
		
		TmRegionPO resultPO = new TmRegionPO();
		List<TmRegionPO> regionList = this.select(TmRegionPO.class, sql, paramList);
		
		if(regionList!=null && regionList.size()>0)
			resultPO = regionList.get(0);
		return resultPO;
	}
	
	/**
	 * 按车系统计索赔单明细信息
	 * 注意：只统计索赔单状态为"审核通过"
	 * @param dealerId 经销商ID
	 * @param yieldly 产地ID
	 * @param startTime 结算单统计开始时间
	 * @param endTime 结算单统计终止时间
     * @param companyId 区分微车和轿车
     * @param freeLabourAmount 免费保养类型索赔单中工时费用(系统中业务参数中设定)
	 * @return
	 */
	public List<Map<String,Object>> queryBalanceGroupSeries(Long dealerId,String yieldly,
			String startTime,String endTime,Long companyId,Double freeLabourAmount){
		
		String status = Constant.CLAIM_APPLY_ORD_TYPE_07.toString();
		StringBuilder sql= new StringBuilder();

		sql.append("SELECT SERIES_CODE,NVL(SERIES_NAME,'--') SERIES_NAME,SUM(BEFORE_LABOUR_AMOUNT) BEFORE_LABOUR_AMOUNT,SUM(BEFORE_PART_AMOUNT) BEFORE_PART_AMOUNT,\n" );
		sql.append("SUM(BEFORE_OTHER_AMOUNT) BEFORE_OTHER_AMOUNT,SUM(AFTER_LABOUR_AMOUNT) AFTER_LABOUR_AMOUNT,SUM(AFTER_PART_AMOUNT) AFTER_PART_AMOUNT,\n" );
		sql.append("SUM(AFTER_OTHER_AMOUNT) AFTER_OTHER_AMOUNT,SUM(FREE_AMOUNT) FREE_AMOUNT,SUM(FREE_COUNT) FREE_COUNT,SUM(SERVICE_COUNT) SERVICE_COUNT,\n" );
		sql.append("SUM(SERVICE_LABOUR_AMOUNT) SERVICE_LABOUR_AMOUNT,SUM(SERVICE_PART_AMOUNT) SERVICE_PART_AMOUNT,SUM(SERVICE_OTHER_AMOUNT) SERVICE_OTHER_AMOUNT,\n" );
		sql.append("SUM(SERVICE_FIXED_AMOUNT) SERVICE_FIXED_AMOUNT,SUM(BEFORE_CLAIM_COUNT) BEFORE_CLAIM_COUNT,SUM(AFTER_CLAIM_COUNT) AFTER_CLAIM_COUNT,\n" );
		sql.append("SUM(TOTAL_AMOUNT) TOTAL_AMOUNT,SUM(TOTAL_COUNT) TOTAL_COUNT\n" );
		sql.append(",SUM(FREE_LABOUR_AMOUNT) FREE_LABOUR_AMOUNT,(SUM(FREE_AMOUNT)-SUM(FREE_LABOUR_AMOUNT)) FREE_PART_AMOUNT\n" );
		sql.append(",(SUM(SERVICE_LABOUR_AMOUNT)+SUM(SERVICE_PART_AMOUNT)+SUM(SERVICE_OTHER_AMOUNT)+SUM(SERVICE_FIXED_AMOUNT)) SERVICE_TOTAL_AMOUNT\n" );
		//免费保养结算统计（保养费用，保养次数）
		sql.append("FROM(SELECT A.SERIES_CODE,A.SERIES_NAME,0 BEFORE_LABOUR_AMOUNT, 0 BEFORE_PART_AMOUNT,");
		sql.append("0 BEFORE_OTHER_AMOUNT, 0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("(NVL(SUM(FREE_M_PRICE),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) FREE_AMOUNT,COUNT(*) FREE_COUNT,0 SERVICE_COUNT, 0 SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("0 SERVICE_PART_AMOUNT, 0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,\n" );
		sql.append("0 AFTER_CLAIM_COUNT,NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(","+freeLabourAmount+"*(COUNT(*)) FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.YIELDLY = ?\n" );
		sql.append("AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_02+")\n" );
		sql.append("AND (A.STATUS = "+status+" OR A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_10+")\n " );
		sql.append("AND A.ACCOUNT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("AND A.ACCOUNT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("GROUP BY A.SERIES_CODE,A.SERIES_NAME\n" );
		sql.append("UNION ALL\n" );
		//PDI索赔结算统计（售前信息统计）
		sql.append("SELECT A.SERIES_CODE,A.SERIES_NAME,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) BEFORE_LABOUR_AMOUNT,");
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) BEFORE_PART_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql.append("0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,COUNT(*) BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.YIELDLY = ?\n" );
		sql.append("AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_07+")\n" );
		sql.append("AND (A.STATUS = "+status+" OR A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_10+")\n " );
		sql.append("AND A.ACCOUNT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("AND A.ACCOUNT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("GROUP BY A.SERIES_CODE,A.SERIES_NAME\n" );
		sql.append("UNION ALL\n" );
		//服务活动结算统计（统计服务活动费用）
		sql.append("SELECT A.SERIES_CODE,A.SERIES_NAME,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql.append("0 BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("0 FREE_AMOUNT,0 FREE_COUNT,COUNT(*) SERVICE_COUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) SERVICE_PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) SERVICE_OTHER_AMOUNT,\n" );
		sql.append("NVL(SUM(A.CAMPAIGN_FEE),0) SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.YIELDLY = ?\n" );
		sql.append("AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_06+")\n" );
		sql.append("AND (A.STATUS = "+status+" OR A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_10+")\n " );
		sql.append("AND A.ACCOUNT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("AND A.ACCOUNT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("GROUP BY A.SERIES_CODE,A.SERIES_NAME\n" );
		sql.append("UNION ALL\n" );
		//统计除以上其他类型索赔单结算费用
		sql.append("SELECT A.SERIES_CODE,A.SERIES_NAME,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql.append("0 BEFORE_OTHER_AMOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) AFTER_LABOUR_AMOUNT,");
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) AFTER_PART_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) AFTER_OTHER_AMOUNT,\n" );
		sql.append("0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql.append("0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,COUNT(*) AFTER_CLAIM_COUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.YIELDLY = ?\n" );
		sql.append("AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("AND A.CLAIM_TYPE NOT IN ("+Constant.CLA_TYPE_02+","+Constant.CLA_TYPE_06+","+Constant.CLA_TYPE_07+")\n" );
		sql.append("AND (A.STATUS = "+status+" OR A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_10+")\n " );
		sql.append("AND A.ACCOUNT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("AND A.ACCOUNT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("GROUP BY A.SERIES_CODE,A.SERIES_NAME)\n" );
		sql.append("GROUP BY SERIES_CODE,SERIES_NAME");
		
		//System.out.println("sqlsql"+sql);
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealerId);paramList.add(yieldly);paramList.add(companyId);paramList.add(startTime);paramList.add(endTime);
		paramList.add(dealerId);paramList.add(yieldly);paramList.add(companyId);paramList.add(startTime);paramList.add(endTime);
		paramList.add(dealerId);paramList.add(yieldly);paramList.add(companyId);paramList.add(startTime);paramList.add(endTime);
		paramList.add(dealerId);paramList.add(yieldly);paramList.add(companyId);paramList.add(startTime);paramList.add(endTime);
		
		return this.pageQuery(sql.toString(), paramList, this.getFunName());
	}

	/**
	 * 保存索赔单信息
	 * @param balancePO
	 */
	public void saveBalanceOrder(TtAsWrClaimBalancePO balancePO){
		
		if(balancePO==null)
			return;
		this.insert(balancePO);
	}

	/**
	 * 查询对应选择时间范围和产地内可以结算的索赔单保存到索赔单同结算单关系表中
	 * 注意：只统计索赔单状态为"审核通过",默认STATUS为无效，即未审核通过
	 * @param dealerId 经销商ID
	 * @param yieldly 产地ID
	 * @param startTime 结算单统计开始时间
	 * @param endTime 结算单统计终止时间
	 * @param companyId 轿车和微车公司ID
	 * @param userId 操作用户
	 * @param balanceId 结算单ID
	 */
	public void saveRelaBetweenClaimAndBalance(Long dealerId,String yieldly,
			String startTime,String endTime,Long companyId,Long userId,String balanceId){
		String status = Constant.CLAIM_APPLY_ORD_TYPE_07.toString();
		StringBuffer sb=new StringBuffer();
		sb.append("DELETE FROM TR_BALANCE_CLAIM_TMP WHERE BALANCE_ID=?"); 
		List list=new ArrayList();
		list.add(new Long(balanceId));
		this.delete(sb.toString(), list);//首先清除临时表的记录
		
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("DELETE FROM TR_BALANCE_CLAIM_TMP C WHERE EXISTS (SELECT * FROM \n" );
		sbSql.append("      TT_AS_WR_APPLICATION A \n");
		sbSql.append("      WHERE NOT EXISTS ( \n");
		sbSql.append("      SELECT B.ID \n");
		sbSql.append("      FROM TR_BALANCE_CLAIM B \n");
		sbSql.append("      WHERE B.CLAIM_ID = A.ID) \n");
		sbSql.append("AND A.DEALER_ID = ? AND A.YIELDLY = ? AND A.OEM_COMPANY_ID = ? AND A.STATUS = "+status+"\n");
		sbSql.append("AND A.ACCOUNT_DATE >= TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') \n");
		sbSql.append("AND A.ACCOUNT_DATE <= TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') \n");
		sbSql.append("AND C.CLAIM_ID=A.ID \n");
		sbSql.append(") \n");
		
		List<Object> paramListTmp = new ArrayList<Object>();
		paramListTmp.add(dealerId);paramListTmp.add(yieldly);paramListTmp.add(companyId);paramListTmp.add(startTime);paramListTmp.add(endTime);
		this.update(sbSql.toString(), paramListTmp);
		
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TR_BALANCE_CLAIM_TMP\n" );
		sql.append("  (ID, CLAIM_ID, BALANCE_ID, UPDATE_BY, UPDATE_DATE, CREATE_BY, CREATE_DATE,STATUS)\n" );
		sql.append("  SELECT F_GETID() ID,A.ID CLAIM_ID,'"+balanceId+"' BALANCE_ID,\n" );
		sql.append("         '"+userId.toString()+"',SYSDATE,'"+userId.toString()+"',SYSDATE,'"+Constant.STATUS_DISABLE+"'\n" );
		sql.append("    FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("   WHERE 1 = 1\n" );
		sql.append("	 AND NOT EXISTS (SELECT B.ID FROM TR_BALANCE_CLAIM B WHERE B.CLAIM_ID = A.ID)\n");
		sql.append("	 AND A.DEALER_ID = ?\n" );
		sql.append("     AND A.YIELDLY = ?\n" );
		sql.append("     AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("     AND A.STATUS = "+status+"\n" );
		sql.append("     AND A.ACCOUNT_DATE >= TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("     AND A.ACCOUNT_DATE <= TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')\n");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealerId);paramList.add(yieldly);paramList.add(companyId);paramList.add(startTime);paramList.add(endTime);
		
		this.update(sql.toString(), paramList);
	}
	
	/**
	 * 将生成过结算单的索赔单状态修改为“结算审核中”
	 * @param balanceId 结算单ID
	 */
	public void updateClaimStatus(Long balanceId,Integer status){
		
		if(balanceId==null || status==null)
			return;
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_APPLICATION A\n" );
		sql.append("   SET A.STATUS = "+status+"\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND EXISTS (SELECT B.ID\n" );
		sql.append("          FROM TR_BALANCE_CLAIM B\n" );
		sql.append("         WHERE B.CLAIM_ID = A.ID\n" );
		sql.append("           AND B.BALANCE_ID = ?)");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		
		this.update(sql.toString(), paramList);
	}
	
	/**
	 * 保存结算单明细
	 * @param balanceId 结算单ID
	 * @param userId 用户Id 
	 * @param freeLabourAmount 免费保养类型索赔单中工时费用(系统中业务参数中设定)
	 */
	public void saveBalanceDetail(Long balanceId,Long userId,Double freeLabourAmount){
		
		if(balanceId==null)
			return;
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TT_CLAIM_BALANCE_DETAIL_TMP(ID,BALANCE_ID,series_id,SERIES_CODE,SERIES_NAME,BEFORE_LABOUR_AMOUNT,BEFORE_PART_AMOUNT,\n" );
		sql.append("BEFORE_OTHER_AMOUNT,AFTER_LABOUR_AMOUNT,AFTER_PART_AMOUNT,AFTER_OTHER_AMOUNT,FREE_CLAIM_COUNT,FREE_CLAIM_AMOUNT,\n" );
		sql.append("SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,BEFORE_CLAIM_COUNT,\n" );
		sql.append("AFTER_CLAIM_COUNT,SERVICE_CLAIM_COUNT,UPDATE_BY,UPDATE_DATE,CREATE_BY,CREATE_DATE,FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT,TOTAL_AMOUNT)\n" );
		//查询需要插入的数据
		sql.append("SELECT F_GETID() ID,"+balanceId+" BALANCE_ID,SERIES_ID,mg.group_CODE,mg.group_NAME,BEFORE_LABOUR_AMOUNT,BEFORE_PART_AMOUNT,\n" );
		sql.append("BEFORE_OTHER_AMOUNT,AFTER_LABOUR_AMOUNT,AFTER_PART_AMOUNT,AFTER_OTHER_AMOUNT,FREE_COUNT FREE_CLAIM_COUNT,FREE_AMOUNT FREE_CLAIM_AMOUNT,\n" );
		sql.append("SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,BEFORE_CLAIM_COUNT,\n" );
		sql.append("AFTER_CLAIM_COUNT,SERVICE_COUNT SERVICE_CLAIM_COUNT,'"+userId+"' UPDATE_BY,SYSDATE UPDATE_DATE,'"+userId+"' CREATE_BY,SYSDATE CREATE_DATE\n" );
		sql.append(",FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT,TOTAL_AMOUNT\n");
		sql.append("FROM(SELECT SERIES_ID,SUM(BEFORE_LABOUR_AMOUNT) BEFORE_LABOUR_AMOUNT,SUM(BEFORE_PART_AMOUNT) BEFORE_PART_AMOUNT,\n" );
		sql.append("SUM(BEFORE_OTHER_AMOUNT) BEFORE_OTHER_AMOUNT,SUM(AFTER_LABOUR_AMOUNT) AFTER_LABOUR_AMOUNT,SUM(AFTER_PART_AMOUNT) AFTER_PART_AMOUNT,\n" );
		sql.append("SUM(AFTER_OTHER_AMOUNT) AFTER_OTHER_AMOUNT,SUM(FREE_AMOUNT) FREE_AMOUNT,SUM(FREE_COUNT) FREE_COUNT,SUM(SERVICE_COUNT) SERVICE_COUNT,\n" );
		sql.append("SUM(SERVICE_LABOUR_AMOUNT) SERVICE_LABOUR_AMOUNT,SUM(SERVICE_PART_AMOUNT) SERVICE_PART_AMOUNT,SUM(SERVICE_OTHER_AMOUNT) SERVICE_OTHER_AMOUNT,\n" );
		sql.append("SUM(SERVICE_FIXED_AMOUNT) SERVICE_FIXED_AMOUNT,SUM(BEFORE_CLAIM_COUNT) BEFORE_CLAIM_COUNT,SUM(AFTER_CLAIM_COUNT) AFTER_CLAIM_COUNT,\n" );
		sql.append("SUM(TOTAL_AMOUNT) TOTAL_AMOUNT,SUM(TOTAL_COUNT) TOTAL_COUNT\n" );
		sql.append(",SUM(FREE_LABOUR_AMOUNT) FREE_LABOUR_AMOUNT,(SUM(FREE_AMOUNT)-SUM(FREE_LABOUR_AMOUNT)) FREE_PART_AMOUNT\n" );
		sql.append(",(SUM(SERVICE_LABOUR_AMOUNT)+SUM(SERVICE_PART_AMOUNT)+SUM(SERVICE_OTHER_AMOUNT)+SUM(SERVICE_FIXED_AMOUNT)) SERVICE_TOTAL_AMOUNT\n" );
		//统计免费保养费用
		sql.append("FROM(SELECT a.SERIES_ID,0 BEFORE_LABOUR_AMOUNT, 0 BEFORE_PART_AMOUNT,0 BEFORE_OTHER_AMOUNT,");
		sql.append(" 0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("(NVL(SUM(A.FREE_M_PRICE),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) FREE_AMOUNT,COUNT(*) FREE_COUNT,0 SERVICE_COUNT, 0 SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("0 SERVICE_PART_AMOUNT, 0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,\n" );
		sql.append("0 AFTER_CLAIM_COUNT,NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(","+freeLabourAmount+"*(COUNT(*)) FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.CLAIM_ID\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_02+")\n" );
		sql.append("AND B.BALANCE_ID = ?\n" );
		sql.append("GROUP BY A.SERIES_ID\n" );
		sql.append("UNION ALL\n" );
		//统计售前维修费用
		sql.append("SELECT A.SERIES_ID,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) BEFORE_LABOUR_AMOUNT,");
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) BEFORE_PART_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql.append("0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,COUNT(*) BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.CLAIM_ID\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_07+")\n" );
		sql.append("AND B.BALANCE_ID = ?\n" );
		sql.append("GROUP BY A.SERIES_ID\n" );
		//统计服务活动维修费用
		sql.append("UNION ALL\n" );
		sql.append("SELECT A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql.append("0 BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("0 FREE_AMOUNT,0 FREE_COUNT,COUNT(*) SERVICE_COUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) SERVICE_PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) SERVICE_OTHER_AMOUNT,\n" );
		sql.append("NVL(SUM(A.CAMPAIGN_FEE),0) SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.CLAIM_ID\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_06+")\n" );
		sql.append("AND B.BALANCE_ID = ?\n" );
		sql.append("GROUP BY A.SERIES_ID\n" );
		//统计除以上三中类型索赔的费用
		sql.append("UNION ALL\n" );
		sql.append("SELECT A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql.append("0 BEFORE_OTHER_AMOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) AFTER_LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) AFTER_PART_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) AFTER_OTHER_AMOUNT,\n" );
		sql.append("0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql.append("0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,COUNT(*) AFTER_CLAIM_COUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.CLAIM_ID\n" );
		sql.append("AND A.CLAIM_TYPE NOT IN ("+Constant.CLA_TYPE_02+","+Constant.CLA_TYPE_06+","+Constant.CLA_TYPE_07+")\n" );
		sql.append("AND B.BALANCE_ID = ?\n" );
		sql.append("GROUP BY A.SERIES_ID)\n" );
		sql.append(" GROUP BY SERIES_ID), tm_vhcl_material_group mg  where mg.group_id = SERIES_ID ");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
		
		this.update(sql.toString(), paramList);
		/****add xiongchuan 2011-03-14****插入结算申请明细***/
		StringBuilder sql1= new StringBuilder();
		sql1.append("INSERT INTO TT_BALANCE_DETAIL_BAK_TMP (ID,BALANCE_ID,series_id,SERIES_CODE,SERIES_NAME,BEFORE_LABOUR_AMOUNT,BEFORE_PART_AMOUNT,\n" );
		sql1.append("BEFORE_OTHER_AMOUNT,AFTER_LABOUR_AMOUNT,AFTER_PART_AMOUNT,AFTER_OTHER_AMOUNT,FREE_CLAIM_COUNT,FREE_CLAIM_AMOUNT,\n" );
		sql1.append("SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,BEFORE_CLAIM_COUNT,\n" );
		sql1.append("AFTER_CLAIM_COUNT,SERVICE_CLAIM_COUNT,UPDATE_BY,UPDATE_DATE,CREATE_BY,CREATE_DATE,FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT,TOTAL_AMOUNT)\n" );
		//查询需要插入的数据
		sql1.append("SELECT F_GETID() ID,"+balanceId+" BALANCE_ID,SERIES_ID,mg.group_CODE,mg.group_NAME,BEFORE_LABOUR_AMOUNT,BEFORE_PART_AMOUNT,\n" );
		sql1.append("BEFORE_OTHER_AMOUNT,AFTER_LABOUR_AMOUNT,AFTER_PART_AMOUNT,AFTER_OTHER_AMOUNT,FREE_COUNT FREE_CLAIM_COUNT,FREE_AMOUNT FREE_CLAIM_AMOUNT,\n" );
		sql1.append("SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,BEFORE_CLAIM_COUNT,\n" );
		sql1.append("AFTER_CLAIM_COUNT,SERVICE_COUNT SERVICE_CLAIM_COUNT,'"+userId+"' UPDATE_BY,SYSDATE UPDATE_DATE,'"+userId+"' CREATE_BY,SYSDATE CREATE_DATE\n" );
		sql1.append(",FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT,TOTAL_AMOUNT\n");
		sql1.append("FROM(SELECT SERIES_ID,SUM(BEFORE_LABOUR_AMOUNT) BEFORE_LABOUR_AMOUNT,SUM(BEFORE_PART_AMOUNT) BEFORE_PART_AMOUNT,\n" );
		sql1.append("SUM(BEFORE_OTHER_AMOUNT) BEFORE_OTHER_AMOUNT,SUM(AFTER_LABOUR_AMOUNT) AFTER_LABOUR_AMOUNT,SUM(AFTER_PART_AMOUNT) AFTER_PART_AMOUNT,\n" );
		sql1.append("SUM(AFTER_OTHER_AMOUNT) AFTER_OTHER_AMOUNT,SUM(FREE_AMOUNT) FREE_AMOUNT,SUM(FREE_COUNT) FREE_COUNT,SUM(SERVICE_COUNT) SERVICE_COUNT,\n" );
		sql1.append("SUM(SERVICE_LABOUR_AMOUNT) SERVICE_LABOUR_AMOUNT,SUM(SERVICE_PART_AMOUNT) SERVICE_PART_AMOUNT,SUM(SERVICE_OTHER_AMOUNT) SERVICE_OTHER_AMOUNT,\n" );
		sql1.append("SUM(SERVICE_FIXED_AMOUNT) SERVICE_FIXED_AMOUNT,SUM(BEFORE_CLAIM_COUNT) BEFORE_CLAIM_COUNT,SUM(AFTER_CLAIM_COUNT) AFTER_CLAIM_COUNT,\n" );
		sql1.append("SUM(TOTAL_AMOUNT) TOTAL_AMOUNT,SUM(TOTAL_COUNT) TOTAL_COUNT\n" );
		sql1.append(",SUM(FREE_LABOUR_AMOUNT) FREE_LABOUR_AMOUNT,(SUM(FREE_AMOUNT)-SUM(FREE_LABOUR_AMOUNT)) FREE_PART_AMOUNT\n" );
		sql1.append(",(SUM(SERVICE_LABOUR_AMOUNT)+SUM(SERVICE_PART_AMOUNT)+SUM(SERVICE_OTHER_AMOUNT)+SUM(SERVICE_FIXED_AMOUNT)) SERVICE_TOTAL_AMOUNT\n" );
		//统计免费保养费用
		sql1.append("FROM(SELECT a.SERIES_ID,0 BEFORE_LABOUR_AMOUNT, 0 BEFORE_PART_AMOUNT,0 BEFORE_OTHER_AMOUNT,");
		sql1.append(" 0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql1.append("(NVL(SUM(A.FREE_M_PRICE),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) FREE_AMOUNT,COUNT(*) FREE_COUNT,0 SERVICE_COUNT, 0 SERVICE_LABOUR_AMOUNT,\n" );
		sql1.append("0 SERVICE_PART_AMOUNT, 0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,\n" );
		sql1.append("0 AFTER_CLAIM_COUNT,NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql1.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql1.append(","+freeLabourAmount+"*(COUNT(*)) FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql1.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql1.append("WHERE 1=1\n" );
		sql1.append("AND A.ID = B.CLAIM_ID\n" );
		sql1.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_02+")\n" );
		sql1.append("AND B.BALANCE_ID = ?\n" );
		sql1.append("GROUP BY A.SERIES_ID\n" );
		sql1.append("UNION ALL\n" );
		//统计售前维修费用
		sql1.append("SELECT A.SERIES_ID,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) BEFORE_LABOUR_AMOUNT,");
		sql1.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) BEFORE_PART_AMOUNT,\n" );
		sql1.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql1.append("0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql1.append("0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,COUNT(*) BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql1.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql1.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql1.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql1.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql1.append("WHERE 1=1\n" );
		sql1.append("AND A.ID = B.CLAIM_ID\n" );
		sql1.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_07+")\n" );
		sql1.append("AND B.BALANCE_ID = ?\n" );
		sql1.append("GROUP BY A.SERIES_ID\n" );
		//统计服务活动维修费用
		sql1.append("UNION ALL\n" );
		sql1.append("SELECT A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql1.append("0 BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql1.append("0 FREE_AMOUNT,0 FREE_COUNT,COUNT(*) SERVICE_COUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) SERVICE_LABOUR_AMOUNT,\n" );
		sql1.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) SERVICE_PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) SERVICE_OTHER_AMOUNT,\n" );
		sql1.append("NVL(SUM(A.CAMPAIGN_FEE),0) SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql1.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql1.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql1.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql1.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql1.append("WHERE 1=1\n" );
		sql1.append("AND A.ID = B.CLAIM_ID\n" );
		sql1.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_06+")\n" );
		sql1.append("AND B.BALANCE_ID = ?\n" );
		sql1.append("GROUP BY A.SERIES_ID\n" );
		//统计除以上三中类型索赔的费用
		sql1.append("UNION ALL\n" );
		sql1.append("SELECT A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql1.append("0 BEFORE_OTHER_AMOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) AFTER_LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) AFTER_PART_AMOUNT,\n" );
		sql1.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) AFTER_OTHER_AMOUNT,\n" );
		sql1.append("0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql1.append("0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,COUNT(*) AFTER_CLAIM_COUNT,\n" );
		sql1.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql1.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql1.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql1.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql1.append("WHERE 1=1\n" );
		sql1.append("AND A.ID = B.CLAIM_ID\n" );
		sql1.append("AND A.CLAIM_TYPE NOT IN ("+Constant.CLA_TYPE_02+","+Constant.CLA_TYPE_06+","+Constant.CLA_TYPE_07+")\n" );
		sql1.append("AND B.BALANCE_ID = ?\n" );
		sql1.append("GROUP BY A.SERIES_ID)\n" );
		sql1.append(" GROUP BY SERIES_ID), tm_vhcl_material_group mg  where mg.group_id = SERIES_ID ");
		List<Object> paramList1 = new ArrayList<Object>();
		paramList1.add(balanceId);
		paramList1.add(balanceId);
		paramList1.add(balanceId);
		paramList1.add(balanceId);
		this.update(sql1.toString(), paramList1);
		
		/****add xiongchuan 2011-03-14*******/
	}
	
	/**
	 * 将备注信息写入到结算单中
	 * @param seriesCode 车系代码
	 * @param remark 备注
	 * @param balanceId 结算单ID
	 */
	public void modifyBalanceDetail(String seriesCode,String remark,Long balanceId){
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(CommonUtils.checkNull(remark));
		
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE_DETAIL\n" );
		sql.append("SET REMARK = ?\n" );
		sql.append("WHERE 1=1\n" );
		if(seriesCode==null || "".equals(seriesCode)){
			sql.append("AND SERIES_CODE IS NULL\n");
		}else{
			sql.append("AND SERIES_CODE = ?\n");
			paramList.add(CommonUtils.checkNull(seriesCode));
		}
		sql.append("AND BALANCE_ID = ?\n" );
		
		paramList.add(balanceId);
		
		this.update(sql.toString(), paramList);
	}
	
	/**
	 * 根据结算单ID重新计算对应结算单的个项费用信息
	 * @param balanceId
	 * @param freeLabourAmount 免费保养类型索赔单中工时费用(系统中业务参数中设定)
	 */
	public void reCheckBalanceAmount(Long balanceId,Double freeLabourAmount){
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE_TMP\n" );
		sql.append("SET (LABOUR_AMOUNT,PART_AMOUNT,OTHER_AMOUNT,FREE_AMOUNT,SERVICE_FIXED_AMOUNT,\n" );
		sql.append("SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,AMOUNT_SUM,CLAIM_COUNT,BALANCE_AMOUNT,");
		sql.append("APPEND_LABOUR_AMOUNT,APPEND_AMOUNT,FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT,SERVICE_TOTAL_AMOUNT) = (\n" );
		
		sql.append("SELECT LABOUR_AMOUNT,PART_AMOUNT,NETITEM_AMOUNT OTHER_AMOUNT,FREE_M_PRICE FREE_AMOUNT,\n" );
		sql.append("CAMPAIGN_FEE SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,\n" );
		sql.append("SERVICE_NETITEM_AMOUNT SERVICE_OTHER_AMOUNT,BALANCE_AMOUNT AMOUNT_SUM,CLAIMCOUNT CLAIM_COUNT,BALANCE_AMOUNT\n" );
		sql.append(",APPENDLABOUR_AMOUNT,APPEND_AMOUNT,FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT,SERVICE_TOTAL_AMOUNT\n");
		sql.append("FROM(SELECT A1.YIELDLY,SUM(A1.CLAIMCOUNT) CLAIMCOUNT,SUM(A1.REPAIR_TOTAL) REPAIR_TOTAL,\n" );
		sql.append("SUM(A1.FREE_M_PRICE) FREE_M_PRICE,\n" );
		sql.append("SUM(A1.SERVICE_LABOUR_AMOUNT) SERVICE_LABOUR_AMOUNT,SUM(A1.SERVICE_PART_AMOUNT) SERVICE_PART_AMOUNT,\n" );
		sql.append("SUM(A1.SERVICE_NETITEM_AMOUNT) SERVICE_NETITEM_AMOUNT,SUM(A1.CAMPAIGN_FEE) CAMPAIGN_FEE,\n" );
		sql.append("SUM(A1.LABOUR_AMOUNT) LABOUR_AMOUNT,SUM(A1.PART_AMOUNT) PART_AMOUNT,\n" );
		sql.append("SUM(A1.NETITEM_AMOUNT) NETITEM_AMOUNT,SUM(A1.BALANCE_AMOUNT) BALANCE_AMOUNT\n" );
		sql.append(",SUM(APPENDLABOUR_AMOUNT) APPENDLABOUR_AMOUNT,SUM(APPEND_AMOUNT) APPEND_AMOUNT\n" );
		sql.append(",SUM(FREE_LABOUR_AMOUNT) FREE_LABOUR_AMOUNT,(SUM(FREE_M_PRICE)-SUM(FREE_LABOUR_AMOUNT)) FREE_PART_AMOUNT\n" );
		sql.append(",(SUM(SERVICE_LABOUR_AMOUNT)+SUM(SERVICE_PART_AMOUNT)+SUM(SERVICE_NETITEM_AMOUNT)+SUM(CAMPAIGN_FEE)) SERVICE_TOTAL_AMOUNT\n" );
		sql.append("FROM(\n" );
		//免费保养
		sql.append("SELECT A.YIELDLY,COUNT(*) CLAIMCOUNT,0 LABOUR_AMOUNT,0 PART_AMOUNT,0 NETITEM_AMOUNT,\n" );
		sql.append("0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,0 SERVICE_NETITEM_AMOUNT,\n" );
		sql.append("0 CAMPAIGN_FEE,(NVL(SUM(A.FREE_M_PRICE),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) FREE_M_PRICE,NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(","+freeLabourAmount+"*(COUNT(*)) FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.CLAIM_ID\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_02+")\n" );
		sql.append("AND B.BALANCE_ID = ?\n" );
		sql.append("GROUP BY A.YIELDLY\n" );
		sql.append("UNION ALL\n" );
		//服务活动
		sql.append("SELECT A.YIELDLY,COUNT(*) CLAIMCOUNT,0 LABOUR_AMOUNT,0 PART_AMOUNT,0 NETITEM_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0) SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) SERVICE_PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) SERVICE_NETITEM_AMOUNT,\n" );
		sql.append("NVL(SUM(A.CAMPAIGN_FEE),0) CAMPAIGN_FEE,0 FREE_M_PRICE,NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.CLAIM_ID\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_06+")\n" );
		sql.append("AND B.BALANCE_ID = ?\n" );
		sql.append("GROUP BY A.YIELDLY\n" );
		sql.append("UNION ALL\n" );
		//除以上两种类型
		sql.append("SELECT A.YIELDLY,COUNT(*) CLAIMCOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) LABOUR_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) NETITEM_AMOUNT,\n" );
		sql.append("0 FREE_M_PRICE,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,0 SERVICE_NETITEM_AMOUNT,\n" );
		sql.append("0 CAMPAIGN_FEE,NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.CLAIM_ID\n" );
		sql.append("AND A.CLAIM_TYPE NOT IN ("+Constant.CLA_TYPE_02+","+Constant.CLA_TYPE_06+")\n" );
		sql.append("AND B.BALANCE_ID = ?\n" );
		sql.append("GROUP BY A.YIELDLY) A1\n" );
		sql.append("GROUP BY YIELDLY) A2\n" );
		sql.append("WHERE ROWNUM = 1)\n" );
		sql.append("WHERE ID = ?");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
		
		this.update(sql.toString(), paramList);
		
		StringBuilder sql1= new StringBuilder();
		sql1.append("UPDATE TT_AS_WR_CLAIM_BALANCE_TMP A\n" );
		sql1.append("SET A.BALANCE_AMOUNT = (NVL(A.BALANCE_AMOUNT,0)+NVL(A.RETURN_AMOUNT,0)+NVL(A.MARKET_AMOUNT,0)+NVL(A.SPEOUTFEE_AMOUNT,0)),\n" );
		sql1.append("A.AMOUNT_SUM = (NVL(A.BALANCE_AMOUNT,0)+NVL(A.RETURN_AMOUNT,0)+NVL(A.MARKET_AMOUNT,0)+NVL(A.SPEOUTFEE_AMOUNT,0))\n" );
		sql1.append("WHERE 1=1\n" );
		sql1.append("AND A.ID = ?");
		
		List<Object> paramList2 = new ArrayList<Object>();
		paramList2.add(balanceId);
		
		this.update(sql1.toString(), paramList2);
		
		/********add by liuxh 20101127 更新结算单结算总金额  BALANCE_AMOUNT(结算金额) AMOUNT_SUM(索赔单金额)*******/
		StringBuffer sbSqlBlance=new StringBuffer();
		sbSqlBlance.append("UPDATE TT_AS_WR_CLAIM_BALANCE_TMP SET BALANCE_AMOUNT=(NVL(LABOUR_AMOUNT,0)+NVL(PART_AMOUNT,0)+NVL(OTHER_AMOUNT,0)+NVL(FREE_AMOUNT,0)");
		sbSqlBlance.append("+NVL(RETURN_AMOUNT,0)+NVL(MARKET_AMOUNT,0)+NVL(SPEOUTFEE_AMOUNT,0)+NVL(APPEND_AMOUNT,0)+NVL(SERVICE_TOTAL_AMOUNT,0)), ");
		sbSqlBlance.append("AMOUNT_SUM=(NVL(LABOUR_AMOUNT,0)+NVL(PART_AMOUNT,0)+NVL(OTHER_AMOUNT,0)+NVL(FREE_AMOUNT,0)");
		sbSqlBlance.append("+NVL(APPEND_AMOUNT,0)+NVL(SERVICE_TOTAL_AMOUNT,0)) ");
		sbSqlBlance.append("WHERE ID=?");
		List<Object> paramList3 = new ArrayList<Object>();
		paramList3.add(balanceId);
		this.update(sbSqlBlance.toString(), paramList3);
		/********add by liuxh 20101127 更新结算单结算总金额*******/
		/*****add by liuxh 20101213 重新计算索赔单数量******/
		this.setClaimCount(balanceId);//重新统计CLAIM_COUNT
	
	}
	/*****add by liuxh 20101213 增加统计索赔单数量******/
	public void setClaimCount(Long balanceId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("UPDATE TT_AS_WR_CLAIM_BALANCE_TMP A SET A.CLAIM_COUNT=(SELECT COUNT(B.CLAIM_ID) FROM TR_BALANCE_CLAIM_TMP B WHERE A.ID=B.BALANCE_ID) \n");
		sbSql.append("WHERE A.ID=? ");
		List listPar=new ArrayList();
		listPar.add(balanceId);
		this.update(sbSql.toString(), listPar);
	}
	/*****add by liuxh 20101213 增加统计索赔单数量******/
	
	/**
	 * 查询对应结算单下的索赔单数
	 * @param balanceId 结算单ID
	 * @return List<Map<String,Object>>
	 *         键名：CLAIM_ID
	 */
	public List<Map<String,Object>> queryClaimByBalanceId(Long balanceId){
		
		StringBuilder sql= new StringBuilder();
		/*****mod by liuxh 20101212 审核结算单下的索赔单时,只审核标志为0(未审核)的索赔单******/
//		sql.append("SELECT CLAIM_ID\n" );
//		sql.append("FROM TR_BALANCE_CLAIM\n" );
//		sql.append("WHERE 1=1\n" );
//		sql.append("AND BALANCE_ID = ? AND TASK_FLAG='0' ");

		sql.append("SELECT B.CLAIM_ID FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B \n");
		sql.append("WHERE A.ID=B.CLAIM_ID AND B.BALANCE_ID=? AND A.TASK_FLAG='0' ");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		return this.pageQuery(sql.toString(), paramList, this.getFunName());
		/*****mod by liuxh 20101212 审核结算单下的索赔单时,只审核标志为0(未审核)的索赔单******/
	}
	/*****add by liuxh 20101212 判断结算单下是否还有未审核完成的索赔单*****/
	public long getClaimCount(Long balanceId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT COUNT(A.ID) AS COUNT FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B \n");
		sbSql.append("WHERE A.ID=B.CLAIM_ID AND B.BALANCE_ID=? AND A.TASK_FLAG='0' \n");
		List listPar=new ArrayList();
		listPar.add(balanceId);
		List list=this.pageQuery(sbSql.toString(), listPar, this.getFunName());
		Map map=(Map)list.get(0);
		Long count=((BigDecimal)map.get("COUNT")).longValue();
		return count;
	}
	/*****add by liuxh 20101212 判断汇总单下是否还有未自动审核完的结算单*****/
	public long getGatherCount(Long gatherId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT COUNT(B.ID) AS COUNT FROM TR_GATHER_BALANCE A,TT_AS_WR_CLAIM_BALANCE B WHERE A.BALANCE_ID=B.ID \n");
		sbSql.append("AND A.GATHER_ID=? AND B.STATUS=? ");
		List listPar=new ArrayList();
		listPar.add(gatherId);
		listPar.add(Constant.ACC_STATUS_06);
		List list=this.pageQuery(sbSql.toString(), listPar, this.getFunName());
		Map map=(Map)list.get(0);
		Long count=((BigDecimal)map.get("COUNT")).longValue();
		return count;
	}
	/*********add by liuxh 统计申请金额**********/
	public void balancrApplyAmount(Long balanceId){
		StringBuilder sql= new StringBuilder();
		sql.append("update Tt_As_Wr_Claim_Balance_TMP c\n");
		sql.append("   set c.apply_amount = nvl(c.LABOUR_AMOUNT,0) + nvl(PART_AMOUNT,0) + nvl(OTHER_AMOUNT,0) +\n" );
		sql.append("                        nvl(FREE_AMOUNT,0) + nvl(RETURN_AMOUNT,0) + nvl(MARKET_AMOUNT,0) +\n" );
		sql.append("                        nvl(SPEOUTFEE_AMOUNT,0) + \n" );
		sql.append("                        nvl(APPEND_AMOUNT,0) + nvl(SERVICE_TOTAL_AMOUNT,0)\n" );
		sql.append("\n" );
		sql.append(" where c.id =? ");
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
	    this.update(sql.toString(), paramList);
	}
	/******add by liuxh 20101129 增加经销商新增结算单时保存金额备份字段*******/
	public void updateBalanceBak(Long balanceId){
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE_TMP SET LABOUR_AMOUNT_BAK=LABOUR_AMOUNT,PART_AMOUNT_BAK=PART_AMOUNT,\n " );
		sql.append("OTHER_AMOUNT_BAK=OTHER_AMOUNT,FREE_AMOUNT_BAK=FREE_AMOUNT,RETURN_AMOUNT_BAK=RETURN_AMOUNT,\n " );
		sql.append("MARKET_AMOUNT_BAK=MARKET_AMOUNT,SPEOUTFEE_AMOUNT_BAK=SPEOUTFEE_AMOUNT,APPEND_AMOUNT_BAK=APPEND_AMOUNT,\n ");
	    sql.append("SERVICE_TOTAL_AMOUNT_BAK=SERVICE_TOTAL_AMOUNT,APPEND_LABOUR_AMOUNT_BAK=APPEND_LABOUR_AMOUNT WHERE ID=? ") ;
	    
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
	    this.update(sql.toString(), paramList);
	}
	
	public void updateBalanceetailBakAmount(Long balanceId){
		StringBuffer sql= new StringBuffer();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE_TMP\n" );
		sql.append("   SET (labour_amount_bak, part_amount_bak, other_amount_bak, free_amount_bak,\n" );
		sql.append("         append_amount_bak,service_total_amount_bak,append_labour_amount_bak) =\n" );
		sql.append("          (SELECT labour_amount, part_amount, netitem_amount other_amount,\n" );
		sql.append("                  free_m_price free_amount,\n" );
		sql.append("                  append_amount, service_total_amount,\n" );
		sql.append("                  appendlabour_amount\n" );
		sql.append("             FROM (SELECT   a1.yieldly, SUM (a1.claimcount) claimcount,\n" );
		sql.append("                            SUM (a1.repair_total) repair_total,\n" );
		sql.append("                            SUM (a1.free_m_price) free_m_price,\n" );
		sql.append("                            SUM\n" );
		sql.append("                               (a1.service_labour_amount\n" );
		sql.append("                               ) service_labour_amount,\n" );
		sql.append("                            SUM (a1.service_part_amount) service_part_amount,\n" );
		sql.append("                            SUM\n" );
		sql.append("                               (a1.service_netitem_amount\n" );
		sql.append("                               ) service_netitem_amount,\n" );
		sql.append("                            SUM (a1.campaign_fee) campaign_fee,\n" );
		sql.append("                            SUM (a1.labour_amount) labour_amount,\n" );
		sql.append("                            SUM (a1.part_amount) part_amount,\n" );
		sql.append("                            SUM (a1.netitem_amount) netitem_amount,\n" );
		sql.append("                            SUM (a1.balance_amount) balance_amount,\n" );
		sql.append("                            SUM (appendlabour_amount) appendlabour_amount,\n" );
		sql.append("                            SUM (append_amount) append_amount,\n" );
		sql.append("                            SUM (free_labour_amount) free_labour_amount,\n" );
		sql.append("                            (SUM (free_m_price) - SUM (free_labour_amount)\n" );
		sql.append("                            ) free_part_amount,\n" );
		sql.append("                            (  SUM (service_labour_amount)\n" );
		sql.append("                             + SUM (service_part_amount)\n" );
		sql.append("                             + SUM (service_netitem_amount)\n" );
		sql.append("                             + SUM (campaign_fee)\n" );
		sql.append("                            ) service_total_amount\n" );
		sql.append("                       FROM (SELECT   a.yieldly, COUNT (*) claimcount,\n" );
		sql.append("                                      0 labour_amount, 0 part_amount,\n" );
		sql.append("                                      0 netitem_amount,\n" );
		sql.append("                                      0 service_labour_amount,\n" );
		sql.append("                                      0 service_part_amount,\n" );
		sql.append("                                      0 service_netitem_amount,\n" );
		sql.append("                                      0 campaign_fee,\n" );
		sql.append("                                      (  NVL (SUM (a.free_m_price), 0)\n" );
		sql.append("                                       + NVL (SUM (a.appendlabour_amount), 0)\n" );
		sql.append("                                      ) free_m_price,\n" );
		sql.append("                                      NVL (SUM (a.repair_total),\n" );
		sql.append("                                           0\n" );
		sql.append("                                          ) repair_total,\n" );
		sql.append("                                      NVL\n" );
		sql.append("                                         (SUM (a.balance_amount),\n" );
		sql.append("                                          0\n" );
		sql.append("                                         ) balance_amount,\n" );
		sql.append("                                      NVL\n" );
		sql.append("                                         (SUM (a.appendlabour_amount),\n" );
		sql.append("                                          0\n" );
		sql.append("                                         ) appendlabour_amount,\n" );
		sql.append("                                      NVL\n" );
		sql.append("                                         (SUM (a.append_amount),\n" );
		sql.append("                                          0\n" );
		sql.append("                                         ) append_amount,\n" );
		sql.append("                                      50.0 * (COUNT (*)) free_labour_amount,\n" );
		sql.append("                                      0 free_part_amount\n" );
		sql.append("                                 FROM tt_as_wr_application a,\n" );
		sql.append("                                      TR_BALANCE_CLAIM_TMP b\n" );
		sql.append("                                WHERE 1 = 1\n" );
		sql.append("                                  AND a.ID = b.claim_id\n" );
		sql.append("                                  AND a.claim_type IN (10661002)\n" );
		sql.append("                                  AND b.balance_id = ?\n" );
		sql.append("                             GROUP BY a.yieldly\n" );
		sql.append("                             UNION ALL\n" );
		sql.append("                             SELECT   a.yieldly, COUNT (*) claimcount,\n" );
		sql.append("                                      0 labour_amount, 0 part_amount,\n" );
		sql.append("                                      0 netitem_amount,\n" );
		sql.append("                                      NVL\n" );
		sql.append("                                         (SUM (a.labour_amount),\n" );
		sql.append("                                          0\n" );
		sql.append("                                         ) service_labour_amount,\n" );
		sql.append("                                      NVL\n" );
		sql.append("                                         (SUM (a.part_amount),\n" );
		sql.append("                                          0\n" );
		sql.append("                                         ) service_part_amount,\n" );
		sql.append("                                      NVL\n" );
		sql.append("                                         (SUM (a.netitem_amount),\n" );
		sql.append("                                          0\n" );
		sql.append("                                         ) service_netitem_amount,\n" );
		sql.append("                                      NVL (SUM (a.campaign_fee),\n" );
		sql.append("                                           0\n" );
		sql.append("                                          ) campaign_fee,\n" );
		sql.append("                                      0 free_m_price,\n" );
		sql.append("                                      NVL (SUM (a.repair_total),\n" );
		sql.append("                                           0\n" );
		sql.append("                                          ) repair_total,\n" );
		sql.append("                                      NVL\n" );
		sql.append("                                         (SUM (a.balance_amount),\n" );
		sql.append("                                          0\n" );
		sql.append("                                         ) balance_amount,\n" );
		sql.append("                                      NVL\n" );
		sql.append("                                         (SUM (a.appendlabour_amount),\n" );
		sql.append("                                          0\n" );
		sql.append("                                         ) appendlabour_amount,\n" );
		sql.append("                                      NVL\n" );
		sql.append("                                         (SUM (a.append_amount),\n" );
		sql.append("                                          0\n" );
		sql.append("                                         ) append_amount,\n" );
		sql.append("                                      0 free_labour_amount,\n" );
		sql.append("                                      0 free_part_amount\n" );
		sql.append("                                 FROM tt_as_wr_application a,\n" );
		sql.append("                                      TR_BALANCE_CLAIM_TMP b\n" );
		sql.append("                                WHERE 1 = 1\n" );
		sql.append("                                  AND a.ID = b.claim_id\n" );
		sql.append("                                  AND a.claim_type IN (10661006)\n" );
		sql.append("                                  AND b.balance_id = ?\n" );
		sql.append("                             GROUP BY a.yieldly\n" );
		sql.append("                             UNION ALL\n" );
		sql.append("                             SELECT   a.yieldly, COUNT (*) claimcount,\n" );
		sql.append("                                      (  NVL (SUM (a.labour_amount),\n" );
		sql.append("                                              0)\n" );
		sql.append("                                       + NVL (SUM (a.appendlabour_amount), 0)\n" );
		sql.append("                                      ) labour_amount,\n" );
		sql.append("                                      NVL\n" );
		sql.append("                                         (SUM (a.part_amount),\n" );
		sql.append("                                          0\n" );
		sql.append("                                         ) part_amount,\n" );
		sql.append("                                      NVL\n" );
		sql.append("                                         (SUM (a.netitem_amount),\n" );
		sql.append("                                          0\n" );
		sql.append("                                         ) netitem_amount,\n" );
		sql.append("                                      0 free_m_price, 0 service_labour_amount,\n" );
		sql.append("                                      0 service_part_amount,\n" );
		sql.append("                                      0 service_netitem_amount,\n" );
		sql.append("                                      0 campaign_fee,\n" );
		sql.append("                                      NVL (SUM (a.repair_total),\n" );
		sql.append("                                           0\n" );
		sql.append("                                          ) repair_total,\n" );
		sql.append("                                      NVL\n" );
		sql.append("                                         (SUM (a.balance_amount),\n" );
		sql.append("                                          0\n" );
		sql.append("                                         ) balance_amount,\n" );
		sql.append("                                      NVL\n" );
		sql.append("                                         (SUM (a.appendlabour_amount),\n" );
		sql.append("                                          0\n" );
		sql.append("                                         ) appendlabour_amount,\n" );
		sql.append("                                      NVL\n" );
		sql.append("                                         (SUM (a.append_amount),\n" );
		sql.append("                                          0\n" );
		sql.append("                                         ) append_amount,\n" );
		sql.append("                                      0 free_labour_amount,\n" );
		sql.append("                                      0 free_part_amount\n" );
		sql.append("                                 FROM tt_as_wr_application a,\n" );
		sql.append("                                      TR_BALANCE_CLAIM_TMP b\n" );
		sql.append("                                WHERE 1 = 1\n" );
		sql.append("                                  AND a.ID = b.claim_id\n" );
		sql.append("                                  AND a.claim_type NOT IN\n" );
		sql.append("                                                         (10661002, 10661006)\n" );
		sql.append("                                  AND b.balance_id = ?\n" );
		sql.append("                             GROUP BY a.yieldly) a1\n" );
		sql.append("                   GROUP BY yieldly) a2\n" );
		sql.append("            WHERE ROWNUM = 1)\n" );
		sql.append(" WHERE ID = ?");
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
	    this.update(sql.toString(), paramList);
	}
	
	
	public void updateBalanceBakAmount(Long balanceId){
		StringBuffer sql= new StringBuffer();
		sql.append("update TT_AS_WR_CLAIM_BALANCE_TMP c set c.apply_amount = nvl(c.LABOUR_AMOUNT_BAK,0) + nvl(PART_AMOUNT_BAK,0) + nvl(OTHER_AMOUNT_BAK,0) +\n" );
		sql.append("nvl(FREE_AMOUNT_BAK,0) + nvl(RETURN_AMOUNT_BAK,0) + nvl(MARKET_AMOUNT_BAK,0) +\n" );
		sql.append("nvl(SPEOUTFEE_AMOUNT_BAK,0) +\n" );
		sql.append("nvl(APPEND_AMOUNT_BAK,0) + nvl(SERVICE_TOTAL_AMOUNT_BAK,0) WHERE ID = ?\n" );
		sql.append("");
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
	    this.update(sql.toString(), paramList);
	}
	/******add by liuxh 20101129 增加经销商新增结算单时保存金额备份字段*******/
	/**
	 * 查询对应结算单下的索赔单数
	 * @param balanceId 结算单ID
	 * @param status 索赔单状态
	 * @param authCode 需要授权级别
	 * @return List<Map<String,Object>>
	 *         键名：CLAIM_ID
	 */
	public List<Map<String,Object>> queryClaimByBalanceId(Long balanceId,String status,String authCode){
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT CLAIM_ID\n" );
		sql.append("FROM TR_BALANCE_CLAIM A,TT_AS_WR_APPLICATION B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.CLAIM_ID = B.ID\n");
		sql.append("AND B.STATUS = ?\n");
		sql.append("AND B.AUTH_CODE = ?\n");
		sql.append("AND A.BALANCE_ID = ?");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(CommonUtils.checkNull(status));
		paramList.add(CommonUtils.checkNull(authCode));
		paramList.add(balanceId);
		
		return this.pageQuery(sql.toString(), paramList, this.getFunName());
	}
	
	/**
	 * 根据结算单ID重新计算对应结算单的个项费用信息
	 */
	public void writeBackBalanceAmount(Long balanceId){
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE\n" );
		sql.append("   SET BALANCE_AMOUNT = (SELECT SUM(A.BALANCE_AMOUNT)\n" );
		sql.append("                           FROM TT_AS_WR_APPLICATION A, TR_BALANCE_CLAIM B\n" );
		sql.append("                          WHERE A.ID = B.CLAIM_ID\n" );
		sql.append("                            AND B.BALANCE_ID = ?)\n" );
		sql.append(" WHERE ID = ?");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		paramList.add(balanceId);
		
		this.update(sql.toString(), paramList);
	}
	
	/**
	 * 查询在指定的月份限制内，是否该经销商存在未回应配件
	 * 注：1、如果存在，则只返回1条记录
	 *        不存在，记录为空
	 *     2、只检测需要回运的配件记录
	 * @param dealerId
	 * @param limitMonths
	 * @return List
	 */
	public List loadReturnPartRecord(String dealerId,Integer limitMonths){
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT A.ID\n" );
		sql.append("  FROM TT_AS_WR_APPLICATION C, TT_AS_WR_PARTSITEM A, TM_PT_PART_BASE B\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND C.ID = A.ID\n" );
		sql.append("   AND A.PART_CODE = B.PART_CODE\n" );
		sql.append("   AND NVL(A.RETURN_NUM,0) < NVL(A.BALANCE_QUANTITY,0)\n" );
		sql.append("   AND B.IS_RETURN = "+Constant.IS_NEED_RETURN+"\n" );
		sql.append("   AND C.REPORT_DATE < ADD_MONTHS(SYSDATE, 0 - ?)\n" );
		sql.append("   AND C.DEALER_ID = ?\n");
		sql.append("   AND ROWNUM<2");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(limitMonths);
		paramList.add(dealerId);
		
		return this.pageQuery(sql.toString(), paramList, this.getFunName());
	}
	
	/**
	 * 经销商查询对应自己的结算单
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> queryAccAuditList(auditBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		/*****mod by liuxh 20101115 经销商只能查本身  和 下级经销商 为 上级结算的经销商*****/
		sql.append("WITH TT_DEALER_SET AS(\n" );
		sql.append("SELECT A.DEALER_ID,A.PARENT_DEALER_D,A.DEALER_LEVEL\n" );
		sql.append("FROM TM_DEALER A\n" );
		sql.append("WHERE 1=1\n");
		sql.append("AND A.DEALER_ID=?\n");
		sql.append("UNION\n");
		sql.append("SELECT A.DEALER_ID,A.PARENT_DEALER_D,A.DEALER_LEVEL\n" );
		sql.append("FROM TM_DEALER A\n" );
		sql.append("WHERE 1=1\n" );
		
		sql.append("AND A.BALANCE_LEVEL=?\n");
		sql.append("START WITH A.DEALER_ID = ?\n" );
		sql.append("CONNECT BY PRIOR A.DEALER_ID = A.PARENT_DEALER_D)\n");
		/*****mod by liuxh 20101115 经销商只能查本身  和 下级经销商 为 上级结算的经销商*****/
		
		
		sql.append("SELECT ROWNUM NUM, A.DEALER_ID,A.ID, A.BALANCE_NO, A.DEALER_CODE, A.DEALER_NAME, A.CLAIM_COUNT,\n" );
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, A.STATUS, A.YIELDLY,A.BALANCE_AMOUNT,a.apply_amount,A.AMOUNT_SUM\n" );
		sql.append("       ,TO_CHAR(A.START_DATE, 'YYYY-MM-DD') START_DATE, TO_CHAR(A.END_DATE, 'YYYY-MM-DD') END_DATE,A.NOTE_AMOUNT KP_AMOUNT \n" );
		//sql.append("(SELECT C.STATUS FROM TR_GATHER_BALANCE B,TT_AS_WR_GATHER_BALANCE C WHERE B.GATHER_ID=C.ID AND B.BALANCE_ID=A.ID) GATHERSTATUS\n");
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE A,TT_DEALER_SET B \n");
		sql.append("WHERE 1=1\n");
		sql.append("AND a.OEM_COMPANY_ID = ").append(bean.getOemCompanyId()).append("\n");
		sql.append("AND A.DEALER_ID = B.DEALER_ID\n");
		
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND CREATE_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND CREATE_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND YIELDLY = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getStatus())){
			sql.append("AND STATUS = ").append(bean.getStatus()).append("\n");
		}
		if(Utility.testString(bean.getBalanceNo())){
			sql.append("AND BALANCE_NO LIKE '%").append(bean.getBalanceNo()).append("%'\n");
		}

		sql.append("ORDER BY ID DESC");
		
		List<Object> paramList = new ArrayList<Object>();
		//paramList.add(Constant.BALANCE_LEVEL_SELF);//独立结算
		paramList.add(bean.getDealerId());
		paramList.add(Constant.BALANCE_LEVEL_HIGH);//上级结算
		paramList.add(bean.getDealerId());
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), paramList, getFunName(), pageSize, curPage);
		return ps;
	}
	public PageResult<Map<String, Object>>  DealerCountKpQuery(RequestWrapper request,String dealerId, String ORG_NAME,String startDate,String endDate,String DEALER_NAME,String DEALER_CODE, int curPage, int pageSize, AclUserBean logonUser)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select * from (\n" );
		sql.append("\n" );
		sql.append("select M.*, K.Return_Date,k.Sign_Date,k.IN_WARHOUSE_DATE,p.create_date,p.COLLECT_TICKETS_DATE,p.CHECK_TICKETS_DATE,p.TRANSFER_TICKETS_DATE, p.creat_dates from (\n" );
		sql.append("\n" );
		sql.append("select max( a.dealer_id ) dealer_id ,max(a.dealer_code) dealer_code , max(c.org_name) org_name, max(a.dealer_shortname) dealer_name ,to_date( to_char( t.sub_date,'yyyy-mm') || '-01','yyyy-mm-dd' ) k_sub_date,\n" );
		sql.append(" to_date( to_char( last_day(max(t.sub_date ) ),'yyyy-mm-dd'),'yyyy-mm-dd') j_sub_date , sum(nvl(t.repair_total,0 )) repair_total,\n" );
		sql.append(" sum(nvl(  decode ( t.claim_type ,10791005, 0,10791006,0,t.balance_amount),0)) balance_amount, count(1) CLAIMCOUNT\n" );
		sql.append("from tt_as_wr_application t ,tm_dealer a ,vw_org_dealer_service  b , tm_org c   where t.sub_date < to_date('2015-01-01','yyyy-mm-dd')\n" );
		sql.append("and a.dealer_id = b.dealer_id and c.org_id = b.root_org_id  and t.dealer_id = a.dealer_id  and a.dealer_id != 2014042694293110\n" );
		sql.append("group by  to_char( t.sub_date,'yyyy-mm') ,t.dealer_id   ) M\n" );
		sql.append("\n" );
		sql.append("left join (select a.dealer_id ,to_date(a.wr_start_date,'yyyy-mm-dd') wr_start_date,\n" );
		sql.append("a.return_end_date ,c.RETURN_DATE,c.Sign_Date ,  c.IN_WARHOUSE_DATE\n" );
		sql.append("from   tt_as_wr_returned_order a , Tr_Return_Logistics b, tt_as_wr_old_returned c\n" );
		sql.append("where a.id = b.return_id and b.logictis_id = c.id    and a.RETURN_TYPE = 10731002 and  a.dealer_id != 2014042694293110)    K on\n" );
		sql.append("k.dealer_id = M.dealer_id and k.wr_start_date <= M.k_sub_date and k.return_end_date >= M.j_sub_date\n" );
		sql.append("left join (select a.dealer_id,a.create_date , a.START_DATE,a.END_DATE, b.* from tt_as_wr_claim_balance a left join    (\n" );
		sql.append("select\n" );
		sql.append("   max(COLLECT_TICKETS_DATE) COLLECT_TICKETS_DATE,\n" );
		sql.append("   max(CHECK_TICKETS_DATE) CHECK_TICKETS_DATE,\n" );
		sql.append("   max(TRANSFER_TICKETS_DATE) TRANSFER_TICKETS_DATE,\n" );
		sql.append("   balance_oder, b.creat_date as creat_dates\n" );
		sql.append(" from tt_as_payment b  group by b.balance_oder, b.creat_date\n" );
		sql.append(") B\n" );
		sql.append("on  A.Remark = b.balance_oder ) P on P.dealer_id = M.dealer_id\n" );
		sql.append("and p.START_DATE = M.k_sub_date and P.END_DATE = M.j_sub_date\n" );
		sql.append("union all\n" );
		sql.append("select M.*, K.Return_Date,k.Sign_Date,k.IN_WARHOUSE_DATE,p.create_date,p.COLLECT_TICKETS_DATE,p.CHECK_TICKETS_DATE,p.TRANSFER_TICKETS_DATE, p.creat_dates from (\n" );
		sql.append("\n" );
		sql.append("select\n" );
		sql.append("    max( a.dealer_id ) dealer_id ,\n" );
		sql.append("    max(a.dealer_code) dealer_code ,\n" );
		sql.append("    max(c.org_name) org_name,\n" );
		sql.append("    max(a.dealer_shortname) dealer_name ,\n" );
		sql.append("    to_date(   to_char( add_months(to_date(t.sub_date,'yyyy-mm-dd'),-1),'yyyy-mm')   || '-26','yyyy-mm-dd' ) k_sub_date,\n" );
		sql.append("    to_date(  t.sub_date ,'yyyy-mm-dd') j_sub_date ,\n" );
		sql.append("   sum(nvl(t.repair_total,0 )) repair_total,\n" );
		sql.append(" sum(nvl(  decode(t.status,10791005,0 ,10791006,0,t.balance_amount) ,0)) balance_amount, count(1) CLAIMCOUNT\n" );
		sql.append("from\n" );
		sql.append(" (\n" );
		sql.append("  select\n" );
		sql.append("      case when\n" );
		sql.append("      to_number(to_char( t.report_date,'dd'))  < = 25\n" );
		sql.append("      then to_char(t.report_date,'yyyy-mm') || + '-25'\n" );
		sql.append("      else\n" );
		sql.append("      to_char( trunc(add_months(t.report_date, 1)),'yyyy-mm' ) || + '-25'\n" );
		sql.append("      end  sub_date,\n" );
		sql.append("      t.dealer_id,\n" );
		sql.append("      t.status ,\n" );
		sql.append("      t.balance_amount ,\n" );
		sql.append("      t.repair_total\n" );
		sql.append("      from tt_as_wr_application t\n" );
		sql.append("      where  t.sub_date > to_date('2015-01-01','yyyy-mm-dd')\n" );
		sql.append("      and t.report_date >= to_date('2014-12-26','yyyy-mm-dd') ) t ,tm_dealer a ,vw_org_dealer_service  b , tm_org c\n" );
		sql.append("  where  a.dealer_id = b.dealer_id and c.org_id = b.root_org_id  and t.dealer_id = a.dealer_id  and a.dealer_id != 2014042694293110\n" );
		sql.append("group by   t.sub_date ,t.dealer_id  ) M\n" );
		sql.append("\n" );
		sql.append("left join (select a.dealer_id ,to_date(a.wr_start_date,'yyyy-mm-dd') wr_start_date,\n" );
		sql.append("a.return_end_date ,c.RETURN_DATE,c.Sign_Date ,  c.IN_WARHOUSE_DATE\n" );
		sql.append("from   tt_as_wr_returned_order a , Tr_Return_Logistics b, tt_as_wr_old_returned c\n" );
		sql.append("where a.id = b.return_id and b.logictis_id = c.id    and a.RETURN_TYPE = 10731002 and  a.dealer_id != 2014042694293110)    K on\n" );
		sql.append("k.dealer_id = M.dealer_id and k.wr_start_date <= M.k_sub_date and k.return_end_date >= M.j_sub_date\n" );
		sql.append("left join (select a.dealer_id,a.create_date , a.START_DATE,a.END_DATE, b.* from tt_as_wr_claim_balance a left join    (\n" );
		sql.append("select\n" );
		sql.append("   max(COLLECT_TICKETS_DATE) COLLECT_TICKETS_DATE,\n" );
		sql.append("   max(CHECK_TICKETS_DATE) CHECK_TICKETS_DATE,\n" );
		sql.append("   max(TRANSFER_TICKETS_DATE) TRANSFER_TICKETS_DATE,\n" );
		sql.append("   balance_oder, b.creat_date as creat_dates\n" );
		sql.append(" from tt_as_payment b  group by b.balance_oder, b.creat_date\n" );
		sql.append(") B\n" );
		sql.append(" on  A.Remark = b.balance_oder ) P on P.dealer_id = M.dealer_id\n" );
		sql.append("and p.START_DATE = M.k_sub_date and P.END_DATE = M.j_sub_date ) M where 1=1 " );
		if(Utility.testString(dealerId)){
			sql.append( " and M.dealer_id =  "+dealerId );
		}else
		{
			if (this.getPoseRoleId(logonUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
			       sql.append(CommonUtils.getOrgDealerLimitSqlByPose("M", logonUser));
			}
		}	
		if(Utility.testString(ORG_NAME))
			sql.append( " and M.ORG_NAME like '%"+ORG_NAME+"%' " );
		if(Utility.testString(DEALER_NAME))
			sql.append( " and M.DEALER_NAME like '%"+DEALER_NAME+"%' " );
		if(Utility.testString(DEALER_CODE))
			sql.append( " and M.DEALER_CODE like '%"+DEALER_CODE+"%' " );
		DaoFactory.getsql(sql, "M.K_SUB_DATE", startDate, 3);
		DaoFactory.getsql(sql, "M.J_SUB_DATE", endDate, 4);
		DaoFactory.getsql(sql, "M.CREAT_DATES", DaoFactory.getParam(request, "Creat_Date_start"), 3);//发票上报日期
		DaoFactory.getsql(sql, "M.CREAT_DATES", DaoFactory.getParam(request, "Creat_Date_end"), 4);
		sql.append(" order by M.k_sub_date\n\n" );
		

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 结算室审核过后，更新结算单子表数据
	 * @param balanceId
	 * @param freeLabourAmount 免费保养类型索赔单中工时费用(系统中业务参数中设定)
	 */
	public void reCheckBalanceDetail(Long balanceId,Double freeLabourAmount){
		if(balanceId==null)
			return;
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_CLAIM_BALANCE_DETAIL_TMP A1\n" );
		sql.append("   SET (A1.BEFORE_LABOUR_AMOUNT, A1.BEFORE_PART_AMOUNT, A1.BEFORE_OTHER_AMOUNT,\n" );
		sql.append("   A1.AFTER_LABOUR_AMOUNT, A1.AFTER_PART_AMOUNT, A1.AFTER_OTHER_AMOUNT,\n" );
		sql.append("   A1.FREE_CLAIM_COUNT, A1.FREE_CLAIM_AMOUNT,\n" );
		sql.append("   A1.SERVICE_FIXED_AMOUNT, A1.SERVICE_LABOUR_AMOUNT, A1.SERVICE_PART_AMOUNT, A1.SERVICE_OTHER_AMOUNT,\n" );
		sql.append("   A1.BEFORE_CLAIM_COUNT, A1.AFTER_CLAIM_COUNT, A1.SERVICE_CLAIM_COUNT,A1.FREE_LABOUR_AMOUNT,A1.FREE_PART_AMOUNT,A1.TOTAL_AMOUNT) = (\n" );
		
		sql.append("SELECT BEFORE_LABOUR_AMOUNT,BEFORE_PART_AMOUNT,BEFORE_OTHER_AMOUNT,\n" );
		sql.append("     AFTER_LABOUR_AMOUNT,AFTER_PART_AMOUNT,AFTER_OTHER_AMOUNT,\n" );
		sql.append("     FREE_COUNT FREE_CLAIM_COUNT,FREE_AMOUNT FREE_CLAIM_AMOUNT,\n" );
		sql.append("    SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,\n" );
		sql.append("    BEFORE_CLAIM_COUNT, AFTER_CLAIM_COUNT,SERVICE_COUNT SERVICE_CLAIM_COUNT,FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT, TOTAL_AMOUNT\n" );
		sql.append("    FROM(SELECT BALANCE_ID,SERIES_ID,SUM(BEFORE_LABOUR_AMOUNT) BEFORE_LABOUR_AMOUNT,SUM(BEFORE_PART_AMOUNT) BEFORE_PART_AMOUNT,\n" );
		sql.append("    SUM(BEFORE_OTHER_AMOUNT) BEFORE_OTHER_AMOUNT,SUM(AFTER_LABOUR_AMOUNT) AFTER_LABOUR_AMOUNT,SUM(AFTER_PART_AMOUNT) AFTER_PART_AMOUNT,\n" );
		sql.append("    SUM(AFTER_OTHER_AMOUNT) AFTER_OTHER_AMOUNT,SUM(FREE_AMOUNT) FREE_AMOUNT,SUM(FREE_COUNT) FREE_COUNT,SUM(SERVICE_COUNT) SERVICE_COUNT,\n" );
		sql.append("    SUM(SERVICE_LABOUR_AMOUNT) SERVICE_LABOUR_AMOUNT,SUM(SERVICE_PART_AMOUNT) SERVICE_PART_AMOUNT,SUM(SERVICE_OTHER_AMOUNT) SERVICE_OTHER_AMOUNT,\n" );
		sql.append("    SUM(SERVICE_FIXED_AMOUNT) SERVICE_FIXED_AMOUNT,SUM(BEFORE_CLAIM_COUNT) BEFORE_CLAIM_COUNT,SUM(AFTER_CLAIM_COUNT) AFTER_CLAIM_COUNT,\n" );
		sql.append("    SUM(TOTAL_AMOUNT) TOTAL_AMOUNT,SUM(TOTAL_COUNT) TOTAL_COUNT\n" );
		sql.append("   ,SUM(FREE_LABOUR_AMOUNT) FREE_LABOUR_AMOUNT,(SUM(FREE_AMOUNT)-SUM(FREE_LABOUR_AMOUNT)) FREE_PART_AMOUNT\n" );
		sql.append("   ,(SUM(SERVICE_LABOUR_AMOUNT)+SUM(SERVICE_PART_AMOUNT)+SUM(SERVICE_OTHER_AMOUNT)+SUM(SERVICE_FIXED_AMOUNT)) SERVICE_TOTAL_AMOUNT\n" );
		
		//统计免费保养费用
		sql.append("    FROM(SELECT B.BALANCE_ID,A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT, 0 BEFORE_PART_AMOUNT,0 BEFORE_OTHER_AMOUNT,\n" );
		sql.append("     0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("    (NVL(SUM(A.FREE_M_PRICE),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) FREE_AMOUNT,COUNT(*) FREE_COUNT,0 SERVICE_COUNT, 0 SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("    0 SERVICE_PART_AMOUNT, 0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,\n" );
		sql.append("    0 AFTER_CLAIM_COUNT,NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append("    ,NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append("    ,"+freeLabourAmount+"*(COUNT(*)) FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("    FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql.append("    WHERE 1=1\n" );
		sql.append("    AND A.ID = B.CLAIM_ID\n" );
		sql.append("    AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_02+")\n" );
		sql.append("    AND B.BALANCE_ID = ?\n" );
		sql.append("    GROUP BY B.BALANCE_ID,A.SERIES_ID\n" );
		sql.append("    UNION ALL\n" );
		//统计售前维修费用
		sql.append("    SELECT B.BALANCE_ID,A.SERIES_ID,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) BEFORE_LABOUR_AMOUNT,\n" );
		sql.append("    NVL(SUM(A.BALANCE_PART_AMOUNT),0) BEFORE_PART_AMOUNT,\n" );
		sql.append("    NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("    0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql.append("    0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,COUNT(*) BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql.append("    NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append("    ,NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append("    ,0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("    FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql.append("    WHERE 1=1\n" );
		sql.append("    AND A.ID = B.CLAIM_ID\n" );
		sql.append("    AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_07+")\n" );
		sql.append("    AND B.BALANCE_ID = ?\n" );
		sql.append("    GROUP BY B.BALANCE_ID,A.SERIES_ID\n" );
		sql.append("    UNION ALL\n" );
		//统计服务活动维修费用
		sql.append("    SELECT B.BALANCE_ID,A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql.append("    0 BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("    0 FREE_AMOUNT,0 FREE_COUNT,COUNT(*) SERVICE_COUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("    NVL(SUM(A.BALANCE_PART_AMOUNT),0) SERVICE_PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) SERVICE_OTHER_AMOUNT,\n" );
		sql.append("    NVL(SUM(A.CAMPAIGN_FEE),0) SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql.append("    NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append("    ,NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append("    ,0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("    FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql.append("    WHERE 1=1\n" );
		sql.append("    AND A.ID = B.CLAIM_ID\n" );
		sql.append("    AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_06+")\n" );
		sql.append("    AND B.BALANCE_ID = ?\n" );
		sql.append("    GROUP BY B.BALANCE_ID,A.SERIES_ID\n" );
		sql.append("    UNION ALL\n" );
		//统计除以上三中类型索赔的费用
		sql.append("    SELECT B.BALANCE_ID,A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql.append("    0 BEFORE_OTHER_AMOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) AFTER_LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) AFTER_PART_AMOUNT,\n" );
		sql.append("    NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) AFTER_OTHER_AMOUNT,\n" );
		sql.append("    0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql.append("    0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,COUNT(*) AFTER_CLAIM_COUNT,\n" );
		sql.append("    NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append("    ,NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append("    ,0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("    FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM_TMP B\n" );
		sql.append("    WHERE 1=1\n" );
		sql.append("    AND A.ID = B.CLAIM_ID\n" );
		sql.append("    AND A.CLAIM_TYPE NOT IN ("+Constant.CLA_TYPE_02+","+Constant.CLA_TYPE_06+","+Constant.CLA_TYPE_07+")\n" );
		sql.append("    AND B.BALANCE_ID = ?\n" );
		sql.append("    GROUP BY B.BALANCE_ID,A.SERIES_ID)\n" );
		sql.append("GROUP BY BALANCE_ID,SERIES_ID) B1\n" );
		sql.append("WHERE B1.BALANCE_ID = A1.BALANCE_ID\n" );
		sql.append("AND B1.SERIES_ID = A1.SERIES_ID)\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND A1.BALANCE_ID = ?");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
		
		this.update(sql.toString(), paramList);
	}
	
	/**
	 * 取得设定的保养索赔单中索赔工时费用占用比例
	 * @param paraId
	 * @return
	 */
	public Double queryFreeClaimFixedAmount(Integer paraId){
		
		Double result = 0.0;
		TmBusinessParaPO conditionPO = new TmBusinessParaPO();
		conditionPO.setParaId(paraId);
		
		List<TmBusinessParaPO> businessList = this.select(conditionPO);
		if(businessList!=null && businessList.size()>0){
			TmBusinessParaPO resultPO = businessList.get(0);
			try {
				result = Utility.getDouble(resultPO.getParaValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	/**
	 * 经销商查询结算汇总单信息
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> queryGatherBalanceOrder(auditBean bean,int curPage, int pageSize){
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT r.region_name,A.*,TO_CHAR(A.CREATE_DATE,'YYYY-MM-DD') CREATEDATE,\n" );
		sql.append("  B.DEALER_CODE,B.DEALER_SHORTNAME DEALER_NAME,to_char(B.BEGIN_BALANCE_DATE,'yyyy-MM-dd') as BEGIN_BALANCE_DATE,to_char(B.END_BALANCE_DATE,'yyyy-MM-dd') as END_BALANCE_DATE,\n" );
		sql.append("  to_char(d.START_DATE,'yyyy-MM-dd') as START_DATE,to_char(d.END_DATE,'yyyy-MM-dd') as END_DATE\n" );
		sql.append("  FROM TT_AS_WR_GATHER_BALANCE A,TM_DEALER B,TR_GATHER_BALANCE c,tt_as_wr_claim_balance d,tm_region r\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("   AND c.GATHER_ID = A.ID\n" );
		sql.append("   AND c.balance_ID = d.ID\n" );
		sql.append("   and r.region_code = b.province_id\n");
		sql.append("   AND A.OEM_COMPANY_ID = ?\n" );

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(bean.getOemCompanyId());
		if(Utility.testString(bean.getDealerId())){//经销商ID(经销商端使用)
			sql.append("   AND A.DEALER_ID = ?\n" );
			paramList.add(bean.getDealerId());
		}
		if(Utility.testString(bean.getDealerCode())){//经销商代码(车厂端使用)
			//用于同时查询多个经销商(不能模糊)sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), paramList, "B", "DEALER_CODE")).append("\n");
			sql.append("   AND B.DEALER_CODE LIKE '%"+bean.getDealerCode()+"%'\n" );//用于模糊查询一个经销商
		}
		if(Utility.testString(bean.getBalanceNo())){//结算汇总单号
			sql.append("   AND A.TOTAL_NO LIKE '%"+bean.getBalanceNo()+"%'\n" );
		}
		if(Utility.testString(bean.getStatus())){//结算汇总单号
			sql.append("   AND A.STATUS = ?\n" );
			paramList.add(bean.getStatus());
		}
		if(Utility.testString(bean.getYieldlys())){//结算产地
			sql.append("   AND A.YIELDLY IN("+bean.getYieldlys()+")\n" );
		}
		if(Utility.testString(bean.getYieldly())){//结算产地
			sql.append("   AND A.YIELDLY = ?\n" );
			paramList.add(bean.getYieldly());
		}
		if(Utility.testString(bean.getStartDate())){//结算汇总单创建开始日期
			sql.append("   AND A.CREATE_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			paramList.add(bean.getStartDate());
		}
		if(Utility.testString(bean.getEndDate())){//结算汇总单创建结束日期
			sql.append("   AND A.CREATE_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			paramList.add(bean.getEndDate());
		}
		if(Utility.testString(bean.getStartReportDate())){//结算汇总单上报开始日期
			sql.append("   AND A.REPORT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			paramList.add(bean.getStartReportDate());
		}
		if(Utility.testString(bean.getEndReportDate())){//结算汇总单上报结束日期
			sql.append("   AND A.REPORT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			paramList.add(bean.getEndReportDate());
		}
		
		sql.append(" ORDER BY A.ID DESC \n");
		
		return this.pageQuery(sql.toString(), paramList, this.getFunName(), pageSize, curPage);
	}
	
	/**
	 * 查询该经销商可以生成结算汇总单的结算单
	 * 注：结算单状态为 "已完成"
	 * @return
	 */
	public PageResult<Map<String, Object>> queryBalanceOrderForGather(auditBean bean,int curPage, int pageSize){
	    StringBuffer sql= new StringBuffer();
	    /******add by liuxh 20101115 经销商可汇总自身 独立结算 和下级为上级结算的单子*******/
	    sql.append("WITH TT_DEALER_SET AS(\n" );
	    sql.append("SELECT A.DEALER_ID,A.PARENT_DEALER_D,A.DEALER_LEVEL\n" );
		sql.append("FROM TM_DEALER A\n" );
		sql.append("WHERE 1=1\n");
		sql.append("AND A.BALANCE_LEVEL=?\n");
		sql.append("AND A.DEALER_ID=?\n");
		sql.append("UNION\n");
	    
	    sql.append("SELECT A.DEALER_ID,A.PARENT_DEALER_D,A.DEALER_LEVEL\n" );
	    sql.append("FROM TM_DEALER A\n" );
	    sql.append("WHERE 1=1\n" );
	    sql.append("AND A.BALANCE_LEVEL=?\n");
	    sql.append("START WITH A.DEALER_ID = ?\n" );
	    sql.append("CONNECT BY PRIOR A.DEALER_ID = A.PARENT_DEALER_D)\n");
	    
	    sql.append("SELECT ROWNUM NUM, A.DEALER_ID,A.ID, A.BALANCE_NO, A.DEALER_CODE, A.DEALER_NAME, A.CLAIM_COUNT,\n" );
	    sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, A.STATUS, A.YIELDLY,A.BALANCE_AMOUNT,A.AMOUNT_SUM\n" );
	    sql.append("       ,A.DEALER_LEVEL,TO_CHAR(A.START_DATE, 'YYYY-MM-DD') START_DATE, TO_CHAR(A.END_DATE, 'YYYY-MM-DD') END_DATE\n" );
	    sql.append("FROM TT_AS_WR_CLAIM_BALANCE A,TT_DEALER_SET B\n");
	    sql.append("WHERE 1=1\n");
	    sql.append("AND A.OEM_COMPANY_ID = ?\n");
	    sql.append("AND A.DEALER_ID = B.DEALER_ID\n");
	    sql.append("AND A.YIELDLY = ?\n");
	    sql.append("AND A.STATUS = "+Constant.ACC_STATUS_09+"\n");
	    
	    List<Object> paramList = new ArrayList<Object>();
	    paramList.add(Constant.BALANCE_LEVEL_SELF);
	    paramList.add(bean.getDealerId());
	    paramList.add(Constant.BALANCE_LEVEL_HIGH);
	    paramList.add(bean.getDealerId());
	    paramList.add(bean.getOemCompanyId());
	    paramList.add(CommonUtils.checkNull(bean.getYieldly()));
		return this.pageQuery(sql.toString(), paramList, this.getFunName(), pageSize, curPage);
	}
	
	/**
	 * 根据汇总单通结算单关系，生成汇总单
	 */
	public void createGatherBalanceOrder(auditBean bean,Long userId){
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TT_AS_WR_GATHER_BALANCE(ID,TOTAL_NO,YIELDLY,DEALER_ID,STATUS,LABOUR_AMOUNT,PART_AMOUNT,\n" );
		sql.append("OTHER_AMOUNT,FREE_AMOUNT,SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,\n" );
		sql.append("RETURN_AMOUNT,AMOUNT_SUM,CLAIM_COUNT,APPEND_LABOUR_AMOUNT,APPEND_AMOUNT,FREE_LABOUR_AMOUNT,\n" );
		sql.append("FREE_PART_AMOUNT,SERVICE_TOTAL_AMOUNT,BALANCE_COUNT,BALANCE_AMOUNT,OEM_COMPANY_ID,CREATE_BY,CREATE_DATE)\n" );
		sql.append("SELECT '"+bean.getId()+"' ID,'"+bean.getBalanceNo()+"' TOTAL_NO,'"+bean.getYieldly()+"' YIELDLY,'");
		sql.append(bean.getDealerId()+"' DEALER_ID,'"+bean.getStatus()+"' STATUS,LABOUR_AMOUNT,PART_AMOUNT,\n" );
		sql.append("OTHER_AMOUNT,FREE_AMOUNT,SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,\n" );
		sql.append("RETURN_AMOUNT,AMOUNT_SUM,CLAIM_COUNT,APPEND_LABOUR_AMOUNT,APPEND_AMOUNT,FREE_LABOUR_AMOUNT,\n" );
		sql.append("FREE_PART_AMOUNT,SERVICE_TOTAL_AMOUNT,BALANCE_COUNT,BALANCE_AMOUNT,\n" );
		sql.append("'"+bean.getOemCompanyId()+"' OEM_COMPANY_ID,'"+userId+"' CREATE_BY,SYSDATE CREATE_DATE\n" );
		sql.append("FROM(\n" );
		sql.append("SELECT SUM(LABOUR_AMOUNT) LABOUR_AMOUNT,SUM(PART_AMOUNT) PART_AMOUNT,\n" );
		sql.append("SUM(OTHER_AMOUNT) OTHER_AMOUNT,SUM(FREE_AMOUNT) FREE_AMOUNT,SUM(SERVICE_FIXED_AMOUNT) SERVICE_FIXED_AMOUNT,\n" );
		sql.append("SUM(SERVICE_LABOUR_AMOUNT) SERVICE_LABOUR_AMOUNT,SUM(SERVICE_PART_AMOUNT) SERVICE_PART_AMOUNT,\n" );
		sql.append("SUM(SERVICE_OTHER_AMOUNT) SERVICE_OTHER_AMOUNT,SUM(RETURN_AMOUNT) RETURN_AMOUNT,SUM(APPLY_AMOUNT) AMOUNT_SUM,\n" );
		sql.append("SUM(CLAIM_COUNT) CLAIM_COUNT,SUM(APPEND_LABOUR_AMOUNT) APPEND_LABOUR_AMOUNT,SUM(APPEND_AMOUNT) APPEND_AMOUNT,\n" );
		sql.append("SUM(FREE_LABOUR_AMOUNT) FREE_LABOUR_AMOUNT,SUM(FREE_PART_AMOUNT) FREE_PART_AMOUNT,\n" );
		sql.append("SUM(SERVICE_TOTAL_AMOUNT) SERVICE_TOTAL_AMOUNT,COUNT(*) BALANCE_COUNT,SUM(BALANCE_AMOUNT) BALANCE_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE A,TR_GATHER_BALANCE B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.BALANCE_ID\n" );
		sql.append("AND B.GATHER_ID = "+bean.getId()+")");
		
		this.update(sql.toString(), null);
	}
	
	/**
	 * 查询该经销商可以生成结算汇总单的结算单
	 * 注：结算单状态为 "已完成"
	 * @return
	 */
	public PageResult<Map<String, Object>> queryGatherBalanceOrderDetail(Long gatherId,int curPage, int pageSize){
	    StringBuffer sql= new StringBuffer();

	    sql.append("SELECT ROWNUM NUM,r.region_name, A.DEALER_ID,A.ID, A.BALANCE_NO, A.DEALER_CODE, A.DEALER_NAME, A.CLAIM_COUNT,B.REMARK,\n" );
	    sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, A.STATUS, A.YIELDLY,A.BALANCE_AMOUNT,A.APPLY_AMOUNT,A.AMOUNT_SUM\n" );
	    sql.append("       ,A.DEALER_LEVEL,TO_CHAR(A.START_DATE, 'YYYY-MM-DD') START_DATE, TO_CHAR(A.END_DATE, 'YYYY-MM-DD') END_DATE\n" );
	    sql.append("FROM TT_AS_WR_CLAIM_BALANCE A,TR_GATHER_BALANCE B,tm_region r,tm_dealer d\n");
	    sql.append("WHERE 1=1\n");
	    sql.append("AND A.ID = B.BALANCE_ID\n");
	    sql.append("and a.dealer_id = d.dealer_id\n");
	    sql.append("and d.province_id = r.region_code\n");
	    sql.append("AND B.GATHER_ID = ?\n");
	    
	    List<Object> paramList = new ArrayList<Object>();
	    paramList.add(CommonUtils.checkNull(gatherId));
		return this.pageQuery(sql.toString(), paramList, this.getFunName(), pageSize, curPage);
	}
	
	/**
	 * 根据结算汇总单ID和经销商级别，通过汇总单通结算单关系表，更加对应结算单状态
	 * 注：经销商级别为该级别
	 * @param gatherId 结算汇总单ID
	 * @param status 需要修改到状态
	 * @param dealerLevel 经销商级别=dealerLevel
	 */
	public void modifyBalanceOrderByGatherId(Long gatherId,Integer status,Integer dealerLevel){
		
		if(gatherId==null || status==null)
			return;
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE A\n" );
		sql.append("SET A.STATUS = ?\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID IN (SELECT B.BALANCE_ID FROM TR_GATHER_BALANCE B\n" );
		sql.append("WHERE B.GATHER_ID = ?)\n");
		sql.append("AND A.DEALER_LEVEL = ?");

		List<Object> param = new ArrayList<Object>();
		param.add(status);
		param.add(gatherId);
		param.add(dealerLevel);
		this.update(sql.toString(), param);
	}
	
	/**
	 * 根据结算汇总单ID和经销商级别，通过汇总单通结算单关系表，更加对应结算单状态
	 * 注：经销商级别不是该级别
	 * @param gatherId 结算汇总单ID
	 * @param status 需要修改到状态
	 * @param dealerLevel 经销商级别<>dealerLevel
	 */
	public void modifyBalanceOrderByGatherId2(Long gatherId,Integer status,Integer dealerLevel){
		
		if(gatherId==null || status==null)
			return;
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE A\n" );
		sql.append("SET A.STATUS = ?\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID IN (SELECT B.BALANCE_ID FROM TR_GATHER_BALANCE B\n" );
		sql.append("WHERE B.GATHER_ID = ?)\n");
		sql.append("AND (A.DEALER_LEVEL >?");
		sql.append("OR A.DEALER_LEVEL <?)");

		List<Object> param = new ArrayList<Object>();
		param.add(status);
		param.add(gatherId);
		param.add(dealerLevel);
		param.add(dealerLevel);
		this.update(sql.toString(), param);
	}
	
	/**
	 * 标识特殊费用到对应结算单中
	 * 注：现在只标识 市场工单费用 和 特殊外出费用(通过FEE_TYPE)
	 *     特殊费用状态为 "已提报" 的
	 *     其他未被其他索赔单标识的 
	 * @param dealerId 经销商ID
	 * @param balanceId 结算单ID
	 */
	public void markSpecialFeeToBalanceOrder(Long dealerId,Long balanceId,String startTime,String endTime,String yieldly){
		/**********mod by liuxh 20101116 修改特殊费用根据时间类型进行修改**************/
		//////市场公单费用
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_SPEFEE A\n" );
		sql.append("SET A.CLAIMBALANCE_ID_TMP = ?\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("and a.yield="+yieldly+"\n");
		sql.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sql.append("AND A.FEE_TYPE = "+Constant.FEE_TYPE_01+"\n" );
		sql.append("AND A.STATUS = "+Constant.SPEFEE_STATUS_06+"\n");
		sql.append("AND A.FEE_CHANNEL<>"+Constant.FEE_TYPE1_01+"\n");
		sql.append("AND A.FEE_CHANNEL<>"+Constant.FEE_TYPE1_03+"\n");
		sql.append("AND A.DEALER_ID = ? \n");
		sql.append("AND A.BALANCE_AUDIT_DATE>=TO_DATE('"+startTime+"','YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.BALANCE_AUDIT_DATE<=TO_DATE('"+endTime+"','YYYY-MM-DD HH24:MI:SS')\n");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		paramList.add(dealerId);
		this.update(sql.toString(), paramList);
	    //////市场公单费用
		
		//////市场公单费用
		StringBuilder sql2= new StringBuilder();
		sql2.append("UPDATE TT_AS_WR_SPEFEE A\n" );
		sql2.append("SET A.CLAIMBALANCE_ID_TMP = ?\n" );
		sql2.append("WHERE 1=1\n" );
		sql2.append("and a.yield="+yieldly+"\n");
		sql2.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sql2.append("AND A.FEE_TYPE = "+Constant.FEE_TYPE_01+"\n" );
		sql2.append("AND A.STATUS = "+Constant.SPEFEE_STATUS_06+"\n");
		sql2.append("AND A.FEE_CHANNEL IN("+Constant.FEE_TYPE1_01+","+Constant.FEE_TYPE1_03+")\n");
		sql2.append("AND A.DEALER_ID = ? \n");
		sql2.append("AND BALANCE_AUDIT_DATE>=TO_DATE('"+startTime+"','YYYY-MM-DD HH24:MI:SS')\n");
		sql2.append("AND BALANCE_AUDIT_DATE<=TO_DATE('"+endTime+"','YYYY-MM-DD HH24:MI:SS')\n");
		
		List<Object> paramList2 = new ArrayList<Object>();
		paramList2.add(balanceId);
		paramList2.add(dealerId);
		this.update(sql2.toString(), paramList2);
		
		//////特殊外出
		
		StringBuilder sqlSp= new StringBuilder();
		sqlSp.append("UPDATE TT_AS_WR_SPEFEE A\n" );
		sqlSp.append("SET A.CLAIMBALANCE_ID_TMP = ?\n" );
		sqlSp.append("WHERE 1=1\n" );
		sqlSp.append("and a.yield="+yieldly+"\n");
		sqlSp.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sqlSp.append("AND A.FEE_TYPE = "+Constant.FEE_TYPE_02+"\n" );
		sqlSp.append("AND A.STATUS = "+Constant.SPEFEE_STATUS_06+"\n");
		sqlSp.append("AND A.DEALER_ID = ? \n");
		sqlSp.append("AND EXISTS (SELECT B.STATUS FROM TT_AS_WR_SPEFEE_AUDITING B\n");
		sqlSp.append("WHERE B.FEE_ID=A.ID AND B.STATUS="+Constant.SPEFEE_STATUS_06+"\n ");
		sqlSp.append("AND B.AUDITING_DATE>=TO_DATE('"+startTime+"','YYYY-MM-DD HH24:MI:SS')\n ");
		sqlSp.append("AND B.AUDITING_DATE<=TO_DATE('"+endTime+"','YYYY-MM-DD HH24:MI:SS')\n ");
		sqlSp.append(")");
		
		List<Object> paramListSp = new ArrayList<Object>();
		paramListSp.add(balanceId);
		paramListSp.add(dealerId);
		this.update(sqlSp.toString(), paramListSp);
	    //////特殊外出
		
		/**********mod by liuxh 20101116 修改特殊费用根据时间类型进行修改**************/
		
	}
	/**
	 * 查询特殊费用列表zyw 2015-5-25 修改新版
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getSpecialFeeToBalanceOrder(String dealerId,String startTime,String endTime){
		StringBuffer sb= new StringBuffer();
		sb.append("select \n" );
		sb.append("       b.area_name,\n" );
		sb.append("       count(*) as count,\n" );
		sb.append("       decode(a.special_type, 1, '善意索赔', 0, '退(换)车') as special_type\n" );
		sb.append("  from tt_as_wr_special_apply a, tm_business_area b\n" );
		sb.append(" where a.yieldly = b.area_id\n" );
		DaoFactory.getsql(sb, "a.dealer_id",dealerId, 1);
		DaoFactory.getsql(sb, "a.audit_date",startTime, 3);
		DaoFactory.getsql(sb, "a.audit_date",endTime, 4);
		sb.append(" group by b.area_name, a.special_type");
		List<Map<String, String>> list=this.pageQuery(sb.toString(), null, this.getFunName());
		return list;
	}
	private String checkNull(Object obj,String repalce){
		String result = "";
		if(obj==null || "".equals(obj.toString()) || "null".equalsIgnoreCase(obj.toString())){
			result = repalce;
		}else{
			result = obj.toString();
		}
		return result;
	}
	
	public void getvModel(Long dealerId,String startTime,String endTime,TtAsWrClaimBalanceTmpPO balancePO)
	{
		TtAsWrBalanceDetailTemPO balanceDetailTemPO = new TtAsWrBalanceDetailTemPO();
		DealerKpDao dao = new DealerKpDao();
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT NVL(SUM(DECLARE_SUM1),0) AS DECLARE_SUM1,max(V_MODEL) V_MODEL,sum(COUNT) AS COUNT from (" );
		sql.append("SELECT NVL(SUM(A.DECLARE_SUM1),0) AS DECLARE_SUM1,A.V_MODEL V_MODEL,COUNT(*) AS COUNT FROM TT_AS_WR_SPEFEE A,TM_BUSINESS_AREA B\n" );
		sql.append("WHERE  \n");
		sql.append("  A.YIELD = B.AREA_ID ");
			sql.append(" AND A.YIELD =" + balancePO.getYieldly());
		sql.append("AND A.STATUS = 11841004 and A.DECLARE_SUM <=1000 \n");
		sql.append("AND A.DEALER_ID = ? \n");
		sql.append("AND A.BALANCE_AUDIT_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.BALANCE_AUDIT_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("GROUP BY A.V_MODEL \n");
		sql.append(" UNION all ");
		sql.append("SELECT NVL(SUM(A.DECLARE_SUM1),0) AS DECLARE_SUM1,A.V_MODEL V_MODEL,COUNT(*) AS COUNT FROM TT_AS_WR_SPEFEE A,TM_BUSINESS_AREA B\n" );
		sql.append("WHERE  \n");
		sql.append("  A.YIELD = B.AREA_ID ");
			sql.append(" AND A.YIELD =" + balancePO.getYieldly());
		sql.append("AND A.STATUS = 11841006 and A.DECLARE_SUM >1000 and A.DECLARE_SUM1 <=3000 \n");
		sql.append("AND A.DEALER_ID = ? \n");
		sql.append("AND A.BALANCE_AUDIT_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.BALANCE_AUDIT_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("GROUP BY A.V_MODEL \n");
		sql.append(" UNION all ");
		sql.append("SELECT NVL(SUM(A.DECLARE_SUM1),0) AS DECLARE_SUM1,A.V_MODEL V_MODEL,COUNT(*) AS COUNT FROM TT_AS_WR_SPEFEE A,TM_BUSINESS_AREA B\n" );
		sql.append("WHERE  \n");
		sql.append("  A.YIELD = B.AREA_ID ");
			sql.append(" AND A.YIELD =" + balancePO.getYieldly());
		sql.append("AND A.STATUS = 11841008 and A.DECLARE_SUM1 >3000 \n");
		sql.append("AND A.DEALER_ID = ? \n");
		sql.append("AND A.BALANCE_AUDIT_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.BALANCE_AUDIT_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("GROUP BY A.V_MODEL) GROUP BY V_MODEL \n");
		paramList.add(dealerId);
		paramList.add(startTime);
		paramList.add(endTime);
		paramList.add(dealerId);
		paramList.add(startTime);
		paramList.add(endTime);
		paramList.add(dealerId);
		paramList.add(startTime);
		paramList.add(endTime);
		List<Map<String,Object>> list=this.pageQuery(sql.toString(), paramList, this.getFunName());
		if(list.size() >0 )
		{
			for(Map<String,Object> feeMap : list)
			{
				long id = CommonUtils.parseLong(SequenceManager.getSequence(""));
				balanceDetailTemPO.setAfterPartAmount((CommonUtils.parseDouble(checkNull(String.valueOf(CommonUtils.getDataFromMap(feeMap,"DECLARE_SUM1")),"0"))));
				balanceDetailTemPO.setSeriesName(String.valueOf(CommonUtils.getDataFromMap(feeMap,"V_MODEL")));
				balanceDetailTemPO.setId(id);
				balanceDetailTemPO.setBalanceId(balancePO.getId());
				dao.insert(balanceDetailTemPO);
			}
			
		}
		
		sql = new StringBuilder();
		paramList = new ArrayList<Object>();
		sql.append("  SELECT COUNT(*) CLAIMCOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.BALANCE_APPENDLABOUR_AMOUNT),0)) LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) PART_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) NETITEM_AMOUNT,NVL(SUM(A.FREE_M_PRICE),0) FREE_M_PRICE,NVL(SUM(A.CAMPAIGN_FEE),0) CAMPAIGN_FEE,\n" );
		sql.append("NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT,A.MODEL_NAME V_MODEL \n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_BUSINESS_AREA B \n" );
		sql.append("WHERE 1=1\n" );
			sql.append(" AND A.YIELDLY =" + balancePO.getYieldly());
		sql.append(" AND A.YIELDLY = B.AREA_ID ");
		sql.append(" AND A.DEALER_ID = ?\n");
		sql.append(" AND A.CLAIM_TYPE <> "+Constant.CLA_TYPE_06+"\n" );
		sql.append(" AND (A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_13+")");
		paramList.add(dealerId);
		if(Utility.testString(startTime)){
			paramList.add(startTime);
			sql.append(" AND A.ACCOUNT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(Utility.testString(endTime)){
			paramList.add(endTime);
			sql.append(" AND A.ACCOUNT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		sql.append("GROUP BY A.MODEL_NAME\n");
		
		List<Map<String,Object>> listapp=this.pageQuery(sql.toString(), paramList, this.getFunName());
		if(listapp.size() >0 )
		{
			for(Map<String,Object> feeMap : listapp)
			{
				String V_MODEL= String.valueOf(CommonUtils.getDataFromMap(feeMap,"V_MODEL"));
				balanceDetailTemPO = new TtAsWrBalanceDetailTemPO();
				balanceDetailTemPO.setSeriesName(V_MODEL);
				balanceDetailTemPO.setBalanceId(balancePO.getId());
				
				List<TtAsWrBalanceDetailTemPO> listtem = dao.select(balanceDetailTemPO);
				if(listtem.size() > 0 )
				{
					balanceDetailTemPO = listtem.get(0);
					TtAsWrBalanceDetailTemPO balanceDetailTemPO1 = new TtAsWrBalanceDetailTemPO();
					balanceDetailTemPO1.setId(balanceDetailTemPO.getId());
					
					TtAsWrBalanceDetailTemPO balanceDetailTemPO2 = new TtAsWrBalanceDetailTemPO();
					
					String LabourAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"LABOUR_AMOUNT"));//request.getParamValue("SERVICE_LABOUR_AMOUNT");//服务活动工时金额
					String PartAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"PART_AMOUNT"));//request.getParamValue("SERVICE_PART_AMOUNT");//服务活动配件金额
					String OtherAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"NETITEM_AMOUNT"));//request.getParamValue("SERVICE_OTHER_AMOUNT");//服务活动其他金额
					String freeAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"FREE_M_PRICE"));
					balanceDetailTemPO2.setBeforeLabourAmount(CommonUtils.parseDouble(checkNull(LabourAmount,"0")));
					balanceDetailTemPO2.setBeforePartAmount(CommonUtils.parseDouble(checkNull(PartAmount,"0")));
					balanceDetailTemPO2.setBeforeOtherAmount(CommonUtils.parseDouble(checkNull(OtherAmount,"0")));
					balanceDetailTemPO2.setAfterLabourAmount(CommonUtils.parseDouble(checkNull(freeAmount,"0")));
					dao.update(balanceDetailTemPO1, balanceDetailTemPO2);
					
				}else
				{
					long id = CommonUtils.parseLong(SequenceManager.getSequence(""));
					balanceDetailTemPO.setId(id);
					String LabourAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"LABOUR_AMOUNT"));//request.getParamValue("SERVICE_LABOUR_AMOUNT");//服务活动工时金额
					String PartAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"PART_AMOUNT"));//request.getParamValue("SERVICE_PART_AMOUNT");//服务活动配件金额
					String OtherAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"NETITEM_AMOUNT"));//request.getParamValue("SERVICE_OTHER_AMOUNT");//服务活动其他金额
					String freeAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"FREE_M_PRICE"));
					balanceDetailTemPO.setBeforeLabourAmount(CommonUtils.parseDouble(checkNull(LabourAmount,"0")));
					balanceDetailTemPO.setBeforePartAmount(CommonUtils.parseDouble(checkNull(PartAmount,"0")));
					balanceDetailTemPO.setBeforeOtherAmount(CommonUtils.parseDouble(checkNull(OtherAmount,"0")));
					balanceDetailTemPO.setAfterLabourAmount(CommonUtils.parseDouble(checkNull(freeAmount,"0")));
					dao.insert(balanceDetailTemPO);
				}
				
			}
		}
		sql = new StringBuilder();
		paramList = new ArrayList<Object>();
		sql.append("SELECT COUNT(*) CLAIMCOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.BALANCE_APPENDLABOUR_AMOUNT),0)) LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) PART_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) NETITEM_AMOUNT,NVL(SUM(A.FREE_M_PRICE),0) FREE_M_PRICE,NVL(SUM(A.CAMPAIGN_FEE),0) CAMPAIGN_FEE,\n" );
		sql.append("NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT,A.MODEL_NAME V_MODEL \n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_BUSINESS_AREA B \n" );
		sql.append("WHERE 1=1\n" );
			sql.append(" AND A.YIELDLY =" + balancePO.getYieldly());
		sql.append(" AND A.YIELDLY = B.AREA_ID ");
		sql.append(" AND A.DEALER_ID = ?\n");
		sql.append(" AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_06+"\n" );
		sql.append(" AND (A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_13+")");
		paramList.add(dealerId);
		if(Utility.testString(startTime)){
			paramList.add(startTime);
			sql.append(" AND A.ACCOUNT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(Utility.testString(endTime)){
			paramList.add(endTime);
			sql.append(" AND A.ACCOUNT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		sql.append("GROUP BY A.MODEL_NAME \n");
		
		
		List<Map<String,Object>> listFw=this.pageQuery(sql.toString(), paramList, this.getFunName());
		if(listFw.size() >0 )
		{
			for(Map<String,Object> feeMap : listFw)
			{
				String V_MODEL= String.valueOf(CommonUtils.getDataFromMap(feeMap,"V_MODEL"));
				balanceDetailTemPO = new TtAsWrBalanceDetailTemPO();
				balanceDetailTemPO.setSeriesName(V_MODEL);
				balanceDetailTemPO.setBalanceId(balancePO.getId());
				
				List<TtAsWrBalanceDetailTemPO> listtem = dao.select(balanceDetailTemPO);
				if(listtem.size() > 0 )
				{
					balanceDetailTemPO = listtem.get(0);
					TtAsWrBalanceDetailTemPO balanceDetailTemPO1 = new TtAsWrBalanceDetailTemPO();
					balanceDetailTemPO1.setId(balanceDetailTemPO.getId());
					
					TtAsWrBalanceDetailTemPO balanceDetailTemPO2 = new TtAsWrBalanceDetailTemPO();
					
					String LabourAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"LABOUR_AMOUNT"));//request.getParamValue("SERVICE_LABOUR_AMOUNT");//服务活动工时金额
					String PartAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"PART_AMOUNT"));//request.getParamValue("SERVICE_PART_AMOUNT");//服务活动配件金额
					String OtherAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"NETITEM_AMOUNT"));//request.getParamValue("SERVICE_OTHER_AMOUNT");//服务活动其他金额
					balanceDetailTemPO2.setServiceLabourAmount(CommonUtils.parseDouble(checkNull(LabourAmount,"0")));
					balanceDetailTemPO2.setServicePartAmount(CommonUtils.parseDouble(checkNull(PartAmount,"0")));
					balanceDetailTemPO2.setServiceOtherAmount(CommonUtils.parseDouble(checkNull(OtherAmount,"0")));
					dao.update(balanceDetailTemPO1, balanceDetailTemPO2);
					
				}else
				{
					long id = CommonUtils.parseLong(SequenceManager.getSequence(""));
					balanceDetailTemPO.setId(id);
					String LabourAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"LABOUR_AMOUNT"));//request.getParamValue("SERVICE_LABOUR_AMOUNT");//服务活动工时金额
					String PartAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"PART_AMOUNT"));//request.getParamValue("SERVICE_PART_AMOUNT");//服务活动配件金额
					String OtherAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"NETITEM_AMOUNT"));//request.getParamValue("SERVICE_OTHER_AMOUNT");//服务活动其他金额
					balanceDetailTemPO.setServiceLabourAmount(CommonUtils.parseDouble(checkNull(LabourAmount,"0")));
					balanceDetailTemPO.setServicePartAmount(CommonUtils.parseDouble(checkNull(PartAmount,"0")));
					balanceDetailTemPO.setServiceOtherAmount(CommonUtils.parseDouble(checkNull(OtherAmount,"0")));
					dao.insert(balanceDetailTemPO);
				}
				
			}
		}
		
		
	} 
	/**
	 * 取得特殊费用 zyw 2014-9-15
	 * @param dealerId
	 * @param startTime
	 * @param endTime
	 * @param yieldly
	 * @return
	 */
	public List getSpecialFeeToBalanceOrder(Long dealerId,String startTime,String endTime,long yieldly){
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT NVL(SUM(DECLARE_SUM1),0) AS DECLARE_SUM1,max(AREA_NAME),sum(COUNT) AS COUNT from (" );
		sql.append("SELECT NVL(SUM(A.DECLARE_SUM),0) AS DECLARE_SUM1,B.AREA_NAME,COUNT(*) AS COUNT FROM TT_AS_WR_SPEFEE A,TM_BUSINESS_AREA B\n" );
		sql.append("WHERE  \n");
		sql.append(" A.YIELD =" + yieldly);
		sql.append(" AND A.YIELD = B.AREA_ID ");
		sql.append("AND (A.STATUS = A.O_STATUS)   and A.DECLARE_SUM <=1000 \n");
		sql.append("AND A.DEALER_ID  in(select d.dealer_id from tm_dealer d start with d.dealer_id = ? connect by PRIOR d.dealer_id = d.parent_dealer_d ) \n");
		sql.append("AND A.balance_audit_date>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.balance_audit_date<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append(" AND A.BALANCE_FEE_TYPE = 94141001 AND A.O_STATUS=A.STATUS \n");
		sql.append(" GROUP BY B.AREA_NAME \n");
		sql.append(" UNION all ");
		sql.append("SELECT NVL(SUM(A.DECLARE_SUM),0) AS DECLARE_SUM1,B.AREA_NAME,COUNT(*) AS COUNT FROM TT_AS_WR_SPEFEE A,TM_BUSINESS_AREA B\n" );
		sql.append("WHERE  \n");
		sql.append(" A.YIELD =" + yieldly);
		sql.append(" AND A.YIELD = B.AREA_ID ");
		sql.append("AND  (A.STATUS= A.O_STATUS) and A.DECLARE_SUM >1000 and A.DECLARE_SUM <=3000 \n");
		sql.append("AND A.DEALER_ID  in(select d.dealer_id from tm_dealer d start with d.dealer_id = ? connect by PRIOR d.dealer_id = d.parent_dealer_d ) \n");
		sql.append("AND A.balance_audit_date>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.balance_audit_date<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append(" AND A.BALANCE_FEE_TYPE = 94141001 AND A.O_STATUS=A.STATUS ");
		sql.append(" GROUP BY B.AREA_NAME \n");
		sql.append(" UNION all ");
		sql.append("SELECT NVL(SUM(A.DECLARE_SUM1),0) AS DECLARE_SUM1,B.AREA_NAME,COUNT(*) AS COUNT FROM TT_AS_WR_SPEFEE A,TM_BUSINESS_AREA B\n" );
		sql.append("WHERE  \n");
		sql.append(" A.YIELD =" + yieldly);
		sql.append(" AND A.YIELD = B.AREA_ID ");
		sql.append("AND  (A.STATUS = A.O_STATUS) and A.DECLARE_SUM >3000 \n");
		sql.append("AND A.DEALER_ID  in(select d.dealer_id from tm_dealer d start with d.dealer_id = ? connect by PRIOR d.dealer_id = d.parent_dealer_d ) \n");
		sql.append("AND A.balance_audit_date>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.balance_audit_date<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.BALANCE_FEE_TYPE = 94141001 AND A.O_STATUS=A.STATUS ");
		sql.append(" GROUP BY B.AREA_NAME)\n");
		paramList.add(dealerId);
		paramList.add(startTime);
		paramList.add(endTime);
		paramList.add(dealerId);
		paramList.add(startTime);
		paramList.add(endTime);
		paramList.add(dealerId);
		paramList.add(startTime);
		paramList.add(endTime);
		List list=this.pageQuery(sql.toString(), paramList, this.getFunName());
		return list;
	}
	
	/**
	 * 查询特殊费用总经费材料费
	 * @param dealerId
	 * @param startTime
	 * @param endTime
	 * @param yieldly
	 * @return
	 */
	public List getSpecialFeeToBalancepOrder(Long dealerId,String startTime,String endTime,long yieldly){
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT NVL(SUM(DECLARE_SUM1),0) AS DECLARE_SUM1,max(AREA_NAME),sum(COUNT) AS COUNT from (" );
		sql.append("SELECT NVL(SUM(A.declare_sum),0) AS DECLARE_SUM1,B.AREA_NAME,COUNT(*) AS COUNT FROM TT_AS_WR_SPEFEE A,TM_BUSINESS_AREA B\n" );
		sql.append("WHERE  \n");
		sql.append(" A.YIELD =" + yieldly);
		sql.append(" AND A.YIELD = B.AREA_ID ");
		sql.append("AND (A.STATUS = A.O_STATUS) and A.DECLARE_SUM <=1000 \n");
		sql.append("AND A.DEALER_ID in(select d.dealer_id from tm_dealer d start with d.dealer_id = ? connect by PRIOR d.dealer_id = d.parent_dealer_d ) \n");
		sql.append("AND A.balance_audit_date>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.balance_audit_date<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.BALANCE_FEE_TYPE = 94141002  AND A.O_STATUS=A.STATUS \n");
		sql.append(" GROUP BY B.AREA_NAME \n");
		sql.append(" UNION all ");
		sql.append("SELECT NVL(SUM(A.declare_sum),0) AS DECLARE_SUM1,B.AREA_NAME,COUNT(*) AS COUNT FROM TT_AS_WR_SPEFEE A,TM_BUSINESS_AREA B\n" );
		sql.append("WHERE  \n");
		sql.append(" A.YIELD =" + yieldly);
		sql.append(" AND A.YIELD = B.AREA_ID ");
		sql.append("AND (A.STATUS = A.O_STATUS) and A.DECLARE_SUM >1000 and A.DECLARE_SUM1 <=3000 \n");
		sql.append("AND A.DEALER_ID in(select d.dealer_id from tm_dealer d start with d.dealer_id = ? connect by PRIOR d.dealer_id = d.parent_dealer_d ) \n");
		sql.append("AND A.balance_audit_date>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.balance_audit_date<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.BALANCE_FEE_TYPE = 94141002  AND A.O_STATUS=A.STATUS \n");
		sql.append(" GROUP BY B.AREA_NAME \n");
		sql.append(" UNION all ");
		sql.append("SELECT NVL(SUM(A.declare_sum),0) AS DECLARE_SUM1,B.AREA_NAME,COUNT(*) AS COUNT FROM TT_AS_WR_SPEFEE A,TM_BUSINESS_AREA B\n" );
		sql.append("WHERE  \n");
		sql.append(" A.YIELD =" + yieldly);
		sql.append(" AND A.YIELD = B.AREA_ID ");
		sql.append("AND (A.STATUS = A.O_STATUS) and A.DECLARE_SUM1 >3000 \n");
		sql.append("AND A.DEALER_ID in(select d.dealer_id from tm_dealer d start with d.dealer_id = ? connect by PRIOR d.dealer_id = d.parent_dealer_d ) \n");
		sql.append("AND A.balance_audit_date>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.balance_audit_date<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.BALANCE_FEE_TYPE = 94141002  AND A.O_STATUS=A.STATUS \n");
		sql.append(" GROUP BY B.AREA_NAME)\n");
		paramList.add(dealerId);
		paramList.add(startTime);
		paramList.add(endTime);
		paramList.add(dealerId);
		paramList.add(startTime);
		paramList.add(endTime);
		paramList.add(dealerId);
		paramList.add(startTime);
		paramList.add(endTime);
		List list=this.pageQuery(sql.toString(), paramList, this.getFunName());
		return list;
	}
	
	public double getpackgechange(Long dealerId,String startTime,String endTime)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select sum(t.AUDIT_ACOUNT) AUDIT_ACOUNT\n" );
		sql.append("  from tt_as_packge_change_apply t\n" );
		sql.append(" where t.APPLY_STATUS = 95441006\n" );
		sql.append("   and t.UPDATE_DATE >= to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS') \n" );
		sql.append("   and t.UPDATE_DATE <= to_date('"+endTime+"','YYYY-MM-DD HH24:MI:SS')");
		sql.append(" and t.APPLY_BY in(select d.dealer_id from tm_dealer d start with d.dealer_id = "+dealerId+" connect by PRIOR d.dealer_id = d.parent_dealer_d ) ");
		Map<String,Object> map = this.pageQueryMap(sql.toString(), null , this.getFunName());
		double d = 0d;
		if(map != null && map.get("AUDIT_ACOUNT") != null)
		{
			d = Double.parseDouble(map.get("AUDIT_ACOUNT").toString());
		}
		return d;
	}
	
	public double getbarcodesum(Long dealerId,String startTime,String endTime)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select sum(t.AUDIT_ACOUNT) AUDIT_ACOUNT\n" );
		sql.append("  from tt_as_barcode_apply t\n" );
		sql.append(" where t.APPLY_STATUS = 95451004\n" );
		sql.append("   and t.APPLY_BY in(select d.dealer_id from tm_dealer d start with d.dealer_id = "+dealerId+" connect by PRIOR d.dealer_id = d.parent_dealer_d ) \n" );
		sql.append("   and t.AUDIT_DATE >= to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS') \n" );
		sql.append("   and t.AUDIT_DATE <= to_date('"+endTime+"','YYYY-MM-DD HH24:MI:SS')");
		Map<String,Object> map = this.pageQueryMap(sql.toString(), null , this.getFunName());
		double d = 0d;
		if(map != null && map.get("AUDIT_ACOUNT") != null)
		{
			d = Double.parseDouble(map.get("AUDIT_ACOUNT").toString());
		}
		return d;
		
	}
	
	public double[] getwrfineOrder(Long dealerId,String startTime,String endTime,long yieldly){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT sum(t.datum_sum)  datum_sum,sum(t.labour_sum) labour_sum, t.FINE_TYPE\n" );
		sql.append("  from tt_as_wr_fine t\n" );
		sql.append(" where\n" );
		sql.append("    t.PAY_STATUS = 11511001 ");
		sql.append("   and t.YIELDLY ="+yieldly+" \n" );
		DaoFactory.getsql(sql, "t.FINE_DATE", startTime, 3);
		DaoFactory.getsql(sql, "t.FINE_DATE", endTime, 4);
		sql.append("   and  t.DEALER_ID in(select d.dealer_id from tm_dealer d start with d.dealer_id = "+dealerId+" connect by PRIOR d.dealer_id = d.parent_dealer_d ) \n" );
		sql.append(" GROUP by t.FINE_TYPE");
		double[] d = new double[2];
		d[0] = 0d;
		d[1] = 0d;
		DealerKpDao dao = new DealerKpDao();
		 List<TtAsWrFinePO> list= dao.select(TtAsWrFinePO.class, sql.toString(), null);
		 
		 if(list.size() >0 )
		 {
			 for(TtAsWrFinePO finePO : list)
			 {
				 String sum ="0";
				 double sum1 = 0d;
				 String sum2 ="0";
				 double sum3 = 0d;
				 if((""+Constant.FINE_TYPE_01).equals(""+ finePO.getFineType()) )
				 {
					 sum = "-"+ finePO.getLabourSum();
				 }else
				 {
					 sum1 =  finePO.getLabourSum();
				 }
				 
				 if((""+Constant.FINE_TYPE_01).equals(""+ finePO.getFineType()))
				 {
					 sum2 = "-"+ finePO.getDatumSum();
				 }else
				 {
					 sum3 =  finePO.getDatumSum();
				 }
				 d[0] = d[0]+ Double.parseDouble(sum) + sum1;
				 d[1] = d[1]+ Double.parseDouble(sum2) + sum3;
			 }
			
			 
		 }
		 return d;

	} 
	
	
	
	/*******add by liuxh 20101211 增加特殊费用列表显示*******/
	/**
	 * 统计指定结算单标识的特殊费用
	 * @param balanceId 结算单ID
	 * @return List<Map<String,Object>> 
	 *         KEY : (FEE_TYPE:费用类型  DECLARE_SUM:审核申报金额  DECLARE_SUM1:申请申报金额  FEETYPECOUNT:对应类型工单数)
	 */
	public List<Map<String,Object>> getSpecialFeeByBalanceId(Long balanceId){
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT FEE_TYPE,NVL(SUM(DECLARE_SUM),0) DECLARE_SUM,NVL(SUM(DECLARE_SUM1),0) DECLARE_SUM1\n" );
		sql.append(",COUNT(*) FEETYPECOUNT\n" );
		sql.append("FROM TT_AS_WR_SPEFEE\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND CLAIMBALANCE_ID_TMP = ?\n" );
		sql.append("GROUP BY FEE_TYPE");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		List<Map<String,Object>> resultList = this.pageQuery(sql.toString(), paramList, this.getFunName());
		return resultList;
	}
	
	/**
	 * 统计指定结算单标识的特殊费用
	 * @param balanceId 结算单ID
	 * @return List<Map<String,Object>> 
	 *         KEY : (FEE_TYPE:费用类型  DECLARE_SUM:审核申报金额  DECLARE_SUM1:申请申报金额  FEETYPECOUNT:对应类型工单数)
	 */
	public List<Map<String,Object>> getSpecialFeeByBalanceIdStatus(Long balanceId){
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT FEE_TYPE,NVL(SUM(DECLARE_SUM),0) DECLARE_SUM,NVL(SUM(DECLARE_SUM1),0) DECLARE_SUM1\n" );
		sql.append(",COUNT(*) FEETYPECOUNT\n" );
		sql.append("FROM TT_AS_WR_SPEFEE\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND CLAIMBALANCE_ID_TMP = ? AND STATUS=?\n" );
		sql.append("GROUP BY FEE_TYPE");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		paramList.add(Constant.SPEFEE_STATUS_06);
		List<Map<String,Object>> resultList = this.pageQuery(sql.toString(), paramList, this.getFunName());
		return resultList;
	}
	/**
	 * 将特殊费用统计对应结算单中，同时标识对应特殊费用合计到该结算单
	 * @param balanceId 结算单ID
	 * @param marketFee 市场工单特殊费用
	 * @param outFee 外出特殊费用
	 * @param feeCount 特殊费用工单数
	 */
	public void addSpecialFeeToBalanceOrder(Long balanceId,Double marketFee,Double outFee,Integer feeCount){
		
		if(marketFee==null)
			marketFee = 0d;
		if(outFee==null)
			outFee = 0d;
		
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE_TMP\n" );
		sql.append("SET MARKET_AMOUNT = "+marketFee+",SPEOUTFEE_AMOUNT = "+outFee+",\n" );
		sql.append("BALANCE_AMOUNT = BALANCE_AMOUNT + ("+marketFee+"+"+outFee+"),\n" );
		sql.append("SPEC_FEE_COUNT = "+feeCount+"\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND ID = "+balanceId);
		
		this.update(sql.toString(), null);
	}
	
	public void addSpecialFeeToBalanceOrder2(Long balanceId,Double marketFee,Double outFee,Double marketFeeBak, Double outFeeBak,Integer feeCount){
		
		if(marketFee==null)
			marketFee = 0d;
		if(outFee==null)
			outFee = 0d;
		
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE_TMP\n" );
		sql.append("SET MARKET_AMOUNT = "+marketFee+",SPEOUTFEE_AMOUNT = "+outFee+",MARKET_AMOUNT_BAK="+marketFeeBak+",SPEOUTFEE_AMOUNT_BAK="+outFeeBak+",\n" );
		sql.append("BALANCE_AMOUNT = BALANCE_AMOUNT + ("+marketFee+"+"+outFee+"),\n" );
		sql.append("SPEC_FEE_COUNT = "+feeCount+"\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND ID = "+balanceId);
		
		this.update(sql.toString(), null);
	}
	/*****add by xiongchuan 20110221 判断汇总单下是否还有未自动审核完的结算单*****/
	public long getBalCount(Long gatherId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("select count(1) as COUNT from Tt_As_Wr_Claim_Balance c,tr_gather_balance g where c.status="+Constant.ACC_STATUS_09+" \n");
		sbSql.append(" and g.balance_id = c.id  and g.gather_id="+gatherId+" \n");
		List list=this.pageQuery(sbSql.toString(), null, this.getFunName());
		Map map=(Map)list.get(0);
		Long count=((BigDecimal)map.get("COUNT")).longValue();
		return count;
	}
	
	public void updateBalanceStatus(Long gatherId){
		StringBuffer sql=new StringBuffer();
		sql.append("  update Tt_As_Wr_Claim_Balance c set c.status="+Constant.ACC_STATUS_06+" where  \n");
		sql.append("  c.id in (select gb.balance_id from tr_gather_balance gb where gb.gather_id="+gatherId+") \n");
		this.update(sql.toString(), null);
	}
	/**
	 * 标识特殊费用到对应结算单中
	 * 注：现在只标识 市场工单费用 和 特殊外出费用(通过FEE_TYPE)
	 *     特殊费用状态为 "已提报" 的
	 *     其他未被其他索赔单标识的 
	 * @param dealerId 经销商ID
	 * 
	 * 
	 * 市场公单费用
	 */
	/******add by liuxh 20101116 修改市场公单费用*********/
	public Map<String, Object> queryMarketFeeToBalanceOrder(Long dealerId,String startTime,String endTime){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_01+",DECLARE_SUM,0)),0) MARKETFEE,NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_02+",DECLARE_SUM,0)),0) OUTFEE\n" );
		sql.append("FROM TT_AS_WR_SPEFEE A\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sql.append("AND A.FEE_TYPE ="+Constant.FEE_TYPE_01+"\n" );
		sql.append("AND A.STATUS = "+Constant.SPEFEE_STATUS_02+"\n");
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.MAKE_DATE>=TO_DATE('"+startTime+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.MAKE_DATE<=TO_DATE('"+endTime+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealerId);
		
		return this.pageQueryMap(sql.toString(), paramList, this.getFunName());
	}
	public Map<String, Object> queryMarketFeeToBalanceOrder2(Long dealerId,String startTime,String endTime,String yieldly){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_01+",DECLARE_SUM,0)),0) MARKETFEE,NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_02+",DECLARE_SUM,0)),0) OUTFEE\n" );
		sql.append("FROM TT_AS_WR_SPEFEE A\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sql.append("AND A.FEE_TYPE ="+Constant.FEE_TYPE_01+"\n" );
		sql.append("and a.yield="+yieldly+"\n");
		sql.append("AND A.STATUS = "+Constant.SPEFEE_STATUS_06+"\n");
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.BALANCE_AUDIT_DATE>=TO_DATE('"+startTime+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.BALANCE_AUDIT_DATE<=TO_DATE('"+endTime+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealerId);
		
		return this.pageQueryMap(sql.toString(), paramList, this.getFunName());
	}
	/******add by liuxh 20101116 修改市场公单费用*********/
	
	/**
	 * 标识特殊费用到对应结算单中
	 * 注：现在只标识 市场工单费用 和 特殊外出费用(通过FEE_TYPE)
	 *     特殊费用状态为 "已提报" 的
	 *     其他未被其他索赔单标识的 
	 * @param dealerId 经销商ID
	 * 
	 * 
	 * 特殊公单费用
	 */
	/******add by liuxh 20101116 修改特殊公单费用*********/
	public Map<String, Object> querySpecialFeeToBalanceOrder(Long dealerId,String startTime,String endTime){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_01+",DECLARE_SUM,0)),0) MARKETFEE,NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_02+",DECLARE_SUM,0)),0) OUTFEE\n" );
		sql.append("FROM TT_AS_WR_SPEFEE A\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sql.append("AND A.FEE_TYPE ="+Constant.FEE_TYPE_02+"\n" );
		sql.append("AND A.STATUS = "+Constant.SPEFEE_STATUS_04+"\n");
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND EXISTS\n");
		sql.append("(SELECT B.STATUS FROM TT_AS_WR_SPEFEE_AUDITING B\n");
		sql.append("WHERE B.FEE_ID=A.ID AND B.STATUS="+Constant.SPEFEE_STATUS_04+"\n ");
		sql.append("AND B.AUDITING_DATE>=TO_DATE('"+startTime+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n ");
		sql.append("AND B.AUDITING_DATE<=TO_DATE('"+endTime+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n ");
		sql.append(")");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealerId);
		
		return this.pageQueryMap(sql.toString(), paramList, this.getFunName());
	}
	public Map<String, Object> querySpecialFeeToBalanceOrder2(Long dealerId,String startTime,String endTime,String yieldly){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_01+",DECLARE_SUM,0)),0) MARKETFEE,NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_02+",DECLARE_SUM,0)),0) OUTFEE\n" );
		sql.append("FROM TT_AS_WR_SPEFEE A\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sql.append("and a.yield="+yieldly+"\n");
		sql.append("AND A.FEE_TYPE ="+Constant.FEE_TYPE_02+"\n" );
		sql.append("AND A.STATUS = "+Constant.SPEFEE_STATUS_06+"\n");
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND EXISTS\n");
		sql.append("(SELECT B.STATUS FROM TT_AS_WR_SPEFEE_AUDITING B\n");
		sql.append("WHERE B.FEE_ID=A.ID AND B.STATUS="+Constant.SPEFEE_STATUS_06+"\n ");
		sql.append("AND B.AUDITING_DATE>=TO_DATE('"+startTime+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n ");
		sql.append("AND B.AUDITING_DATE<=TO_DATE('"+endTime+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n ");
		sql.append(")");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealerId);
		
		return this.pageQueryMap(sql.toString(), paramList, this.getFunName());
	}
	/******add by liuxh 20101116 修改特殊公单费用*********/
	
	/**
	 * 删除结算单通特殊费用关系,同时将特殊费用工单状态修改为"已提报"
	 * 原因：结算单只统计"已提报"的特殊费用，防止该笔费用审核过后，不能再次使用。
	 */
	public void deleteRelationOfSpecFeeAndBalance(Long balanceId){
		/********mod by liuxh 20101127 删除结算单时根据不同类型更新状态不同   市场工单 更新为 已提报 特殊外出更新为 大区审核通过********/
//		StringBuilder sql= new StringBuilder();
//		sql.append("UPDATE TT_AS_WR_SPEFEE\n" );
//		sql.append("   SET CLAIMBALANCE_ID = NULL,\n" );
//		sql.append("   STATUS = "+Constant.SPEFEE_STATUS_02+"\n" );
//		sql.append(" WHERE CLAIMBALANCE_ID = ?");
//		
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(balanceId);
//		
//		this.update(sql.toString(), paramList);
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_SPEFEE\n" );
		sql.append("   SET CLAIMBALANCE_ID = NULL,\n" );
		sql.append("   STATUS = ?\n" );
		sql.append(" WHERE FEE_TYPE=? AND CLAIMBALANCE_ID = ?");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(Constant.SPEFEE_STATUS_02);
		paramList.add(Constant.FEE_TYPE_01);
		paramList.add(balanceId);
		this.update(sql.toString(), paramList);
		
		List<Object> paramList2 = new ArrayList<Object>();
		paramList2.add(Constant.SPEFEE_STATUS_04);
		paramList2.add(Constant.FEE_TYPE_02);
		paramList2.add(balanceId);
		this.update(sql.toString(), paramList2);
		
		/********mod by liuxh 20101127 删除结算单时根据不同类型更新状态不同   市场工单 更新为 已提报 特殊外出更新为 大区审核通过********/
	}
	
	/**
	 * 查询结算汇总单明细
	 * @param gatherId 结算汇总单ID
	 * @return
	 */
	public List<Map<String,Object>> printBalanceGatherOrderDetail(String gatherId){
		if(gatherId==null)
			return null;
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT A.GATHER_ID,TO_CHAR(B.START_DATE,'YYYY-MM-DD') F_START_DATE,\n" );
		sql.append("TO_CHAR(B.END_DATE,'YYYY-MM-DD') F_END_DATE,B.*\n" );
		sql.append("FROM TR_GATHER_BALANCE A,TT_AS_WR_CLAIM_BALANCE B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.BALANCE_ID = B.ID\n" );
		sql.append("AND A.GATHER_ID = ?");

	    List<Object> paramList = new ArrayList<Object>();
	    paramList.add(gatherId);
	    return this.pageQuery(sql.toString(), paramList, this.getFunName());
	}
	/*****
	 * add by liuxh 20101227 增加特殊费用 市场公单 和活 动公单字段。审核完成动作更新字段
	 */
	public int updateMarkSpeeActiveFee(long balanceId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("UPDATE   TT_AS_WR_CLAIM_BALANCE_TMP K \n" );
		sbSql.append("   SET   K.MARKET_MARKET_AMOUNT = \n");
		sbSql.append("            (SELECT   NVL(SUM (NVL (L.DECLARE_SUM, 0)),0) \n");
		sbSql.append("               FROM   TT_AS_WR_SPEFEE L \n");
		sbSql.append("              WHERE       L.FEE_TYPE = ? \n");         //市场公单费用
		sbSql.append("                      AND L.FEE_CHANNEL IN (?,?) \n");   //市场公单
		sbSql.append("                      AND L.STATUS = ? \n");            //审核通过
		sbSql.append("                      AND K.ID = L.CLAIMBALANCE_ID_TMP), \n");
		sbSql.append("         K.MARKET_ACTIVITY_AMOUNT = \n");
		sbSql.append("            (SELECT   NVL(SUM (NVL (L.DECLARE_SUM, 0)),0) \n");
		sbSql.append("               FROM   TT_AS_WR_SPEFEE L \n");
		sbSql.append("              WHERE       L.FEE_TYPE = ? \n");        //市场公单费用
		sbSql.append("                      AND L.FEE_CHANNEL = ? \n");       //市场活动公单费用
		sbSql.append("                      AND L.STATUS = ? \n");            //审核通过
		sbSql.append("                      AND K.ID = L.CLAIMBALANCE_ID_TMP) \n");
		sbSql.append("   WHERE K.ID=? ");
		List listPar=new ArrayList();
		listPar.add(Constant.FEE_TYPE_01);
		listPar.add(Constant.FEE_TYPE1_01);
		listPar.add(Constant.FEE_TYPE1_03);
		listPar.add(Constant.SPEFEE_STATUS_06);
		
		listPar.add(Constant.FEE_TYPE_01);
		listPar.add(Constant.FEE_TYPE1_02);
		listPar.add(Constant.SPEFEE_STATUS_06);
		
		listPar.add(balanceId);
		return this.update(sbSql.toString(), listPar);
	}
	
	/*********Iverson add By 2010-11-18 通过索赔汇总单ID查询结算开始和结束时间以及哪个经销商结算的*************************/
	public List<Map<String, Object>> selectTime(String orderId) {
		String sql="select c.start_date,c.end_date ,c.dealer_id from Tt_As_Wr_Claim_Balance c where c.id in(select b.balance_id from tr_gather_balance b where b.gather_id='"+orderId+"')";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	/**
	 * Iverson add By 2010-11-22 
	 * 取出最大的时间
	 * @param dealerId
	 * @return
	 */
	public String getMaxDate(String dealerId,Integer yieldly){
		String startTime="";
		String endTime="";
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select a.end_date from tt_as_wr_claim_balance a where a.dealer_id =? and a.yieldly=? order by a.end_date desc");
		List parList=new ArrayList();
		parList.add(Long.parseLong(dealerId));
		parList.add(yieldly);
		List<Map<String,Object>> rsList= pageQuery(sqlStr.toString(), parList, getFunName());
		if(rsList.size()>0){
			endTime = rsList.get(0).get("END_DATE").toString();
		}else{
			//取得原始旧件起止时间
			TmBusinessParaPO po = new TmBusinessParaPO();
			po.setTypeCode(Constant.TYPE_CODE);
			TmBusinessParaPO poValue = (TmBusinessParaPO)this.select(po).get(0);
			startTime = poValue.getParaValue().substring(0, 10);
			endTime = poValue.getParaValue().substring(11,poValue.getParaValue().length());
		}
		return endTime;
	}
	/*********Iverson add By 2010-11-18 通过索赔汇总单ID查询结算开始和结束时间以及哪个经销商结算的*************************/
	/*********Iverson add By 2010-11-18 通过索赔汇总单ID查询结算开始和结束时间以及哪个经销商结算的*************************/
	
	
	/*********add by liuxh 20101126 判断结算单下的索赔单和特殊费用单是否已全部审核完成**********/
	/**
	 * 索赔单是否全部审核完成
	 */
	public boolean queryClaimByBanlanceId(long banlanceId) {
		boolean flag=true;
		String sql="select a.status from TT_AS_WR_APPLICATION a,TR_BALANCE_CLAIM b where a.id=b.balance_id and b.balance_id=?";
		List parList=new ArrayList();
		parList.add(banlanceId);
		List<Map> list= pageQuery(sql, parList, this.getFunName());
		for(Map map:list){
			String status=((BigDecimal)map.get("STATUS")).toString();
			if(!status.equals(Constant.CLAIM_APPLY_ORD_TYPE_07)){
				flag=false;
				break;
			}
		}
		return flag;
	}
	/**
	 * 特殊费用全完审核判断
	 * @param banlanceId
	 * @return
	 */
	public boolean querySpecByBanlanceId(long banlanceId) {
		boolean flag=true;
		String sql="SELECT D.STATUS FROM TT_AS_WR_SPEFEE D WHERE D.CLAIMBALANCE_ID=?";// AND D.STATUS="+Constant.SPEFEE_STATUS_06+")";
		List parList=new ArrayList();
		parList.add(banlanceId);
		List<Map> list= pageQuery(sql, parList, this.getFunName());
		for(Map map:list){
			String status=((BigDecimal)map.get("STATUS")).toString();
			if(!status.equals(Constant.SPEFEE_STATUS_06)&&!status.equals(Constant.SPEFEE_STATUS_05)){
				flag=false;
				break;
			}
		}
		return flag;
	}
	/*********add by liuxh 20101126 判断结算单下的索赔单和特殊费用单是否已全部审核完成**********/
	
	/*********Iverson add 2010-12-01 根据经销商ID和基地查询吃此经销商是否被停止结算*********/
	public String getDealerStatus(long dealerId,long yieldly) throws Exception{
		String sql="SELECT STATUS FROM TT_AS_DEALER_TYPE WHERE DEALER_ID=? AND YIELDLY=?";
		List parList=new ArrayList();
		parList.add(Long.valueOf(dealerId));
		parList.add(Long.valueOf(yieldly));
		List list=this.pageQuery(sql, parList, this.getFunName());
		return (String)((Map)list.get(0)).get("STATUS");
	}
	/*********Iverson add 2010-12-01 根据经销商ID和基地查询吃此经销商是否被停止结算*********/
	
	/**
	 * Iverson add By 2010-12-21
	 * 结算单扣款明细查询
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> queryClaimBalanceOrder(String dealerId ,String balanceNo,String startDate,String endDate,String dealerCode,String dealerName,String yieldlys,int curPage, int pageSize){
		StringBuilder sql= new StringBuilder();
		sql.append("select a.id,b.dealer_code,b.dealer_name,a.yieldly,to_char(a.start_date,'yyyy-MM-dd') as start_date,to_char(a.end_date,'yyyy-MM-dd') as end_date,nvl(a.balance_no,0) as balance_no,nvl(a.apply_amount,0) as apply_amount,nvl(a.balance_amount,0) as balance_amount,(nvl(a.apply_amount,0)-nvl(a.balance_amount,0)) as kk\n" );
		sql.append("from tt_as_wr_claim_balance a,tm_dealer b where a.dealer_id=b.dealer_id and a.status IN ("+Constant.ACC_STATUS_04+","+Constant.ACC_STATUS_05+")\n" );
		if(Utility.testString(dealerCode)){
			sql.append("   and b.dealer_code like '%"+dealerCode+"%'\n" );
		}
		if(Utility.testString(dealerName)){
			sql.append("   and b.dealer_name like '%"+dealerName+"%'\n" );
		}
		if(Utility.testString(dealerId)){
			sql.append("   and a.dealer_id='"+dealerId+"'\n" );
		}
		if(Utility.testString(yieldlys)){
			sql.append("   and a.yieldly in ("+yieldlys+")\n" );
		}
		if(Utility.testString(balanceNo)){
			sql.append("   AND a.balance_no LIKE '%"+balanceNo+"%'\n" );//用于模糊查询
		}
		if(Utility.testString(startDate)){
			sql.append("   AND a.start_date>=TO_DATE('"+startDate+"','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endDate)){
			sql.append("   AND a.end_date<=TO_DATE('"+endDate+"','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(" ORDER BY A.ID DESC \n");
		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单号查询索赔单明细
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> detailByBalanceId(String balanceId) {
		String sql="select b.id,b.claim_no, nvl(b.repair_total,0) as repair_total,nvl(b.balance_amount,0) as balance_amount,(nvl(b.repair_total,0)-nvl(b.balance_amount,0))as sub_amount from tt_as_wr_application b where b.id in(select d.claim_id from tr_balance_claim d where d.balance_id='"+balanceId+"') order by sub_amount desc";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	/**
	 * Iverson add By 2010-12-23
	 * 根据结算单ID查找结算单信息
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> balanceInfoByBalanceId(String balanceId) {
		//String sql="select * from tt_as_wr_claim_balance a where a.id ='"+balanceId+"'";
		String sql="select a.*,t.dealer_code,t.dealer_name from tt_as_wr_claim_balance a,TM_DEALER t where a.dealer_id = t.dealer_id and a.id ='"+balanceId+"'";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单号查询特殊费用明细
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> spefeeInfoByBalanceId(String balanceId) {
		String sql="select c.id,c.fee_no,c.FEE_TYPE, nvl(c.declare_sum1,0) as declare_sum1,nvl(declare_sum,0) as declare_sum,(nvl(declare_sum1,0)-nvl(declare_sum,0))as fee_amount from tt_as_wr_spefee c where c.claimbalance_id ='"+balanceId+"' order by fee_amount desc";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	/**********
	 * add xiongchuan 2011-03-10
	 * 将审核人插入到索赔单的状态记录表中
	 * 
	 */
	
	
	public void updateStatusApplication(long balanceId,String name){
		
		StringBuffer sql= new StringBuffer();
		sql.append("update Tt_As_Wr_Appauthitem a set a.approval_person='"+name+"' where a.id in (\n" );
		sql.append("select b.claim_id from tr_balance_claim b ,Tt_As_Wr_Application wa where b.balance_id="+balanceId+"\n" );
		sql.append("and wa.id = b.claim_id and wa.claim_type in ("+Constant.CLA_TYPE_02+","+Constant.CLA_TYPE_06+")  and wa.status="+Constant.CLAIM_APPLY_ORD_TYPE_07+"\n" );
		sql.append("and a.approval_result=10791007 )");
		this.update(sql.toString(), null);
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单ID查询索赔单信息(分页)
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> claimInfo(String balanceId,int curPage, int pageSize){
		StringBuilder sql= new StringBuilder();
		sql.append("select b.id,b.claim_no, nvl(b.repair_total,0) as repair_total,nvl(b.balance_amount,0) as balance_amount,(nvl(b.repair_total,0)-nvl(b.balance_amount,0))as sub_amount from tt_as_wr_application b where b.id in(select d.claim_id from tr_balance_claim d where d.balance_id='"+balanceId+"') " );
		sql.append("order by sub_amount desc");
		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单ID查询特殊费用明细(分页)
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> feeInfo(String balanceId,int curPage, int pageSize){
		StringBuilder sql= new StringBuilder();
		sql.append("select c.id,c.fee_no,c.FEE_TYPE, nvl(c.declare_sum1,0) as declare_sum1,nvl(declare_sum,0) as declare_sum,(nvl(declare_sum1,0)-nvl(declare_sum,0))as fee_amount from tt_as_wr_spefee c where c.claimbalance_id ='"+balanceId+"' " );
		sql.append("order by fee_amount desc");
		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	/**
	 * Iverson add By 2010-12-23
	 * 根据结算单ID查找旧件扣款信息
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> deductInfoByBalanceId(String balanceId) {
		String sql="select a.id,a.balance_no,a.dealer_code,a.dealer_name,a.DEDUCT_COUNT,nvl(a.TOTAL_AMOUNT,0) as TOTAL_AMOUNT from tt_as_wr_deduct_balance a where a.claimbalance_id ='"+balanceId+"'";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单ID查询考核扣款明细
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> fineByBalanceId(String balanceId) {
		String sql="select b.fine_id,T.DEALER_CODE,T.DEALER_NAME,nvl(b.FINE_SUM,0) as FINE_SUM from tt_as_wr_fine b,TM_DEALER T where T.DEALER_ID=b.dealer_id AND b.claimbalance_id ='"+balanceId+"'";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单ID查询行政扣款明细
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> adminDeductInfoByBalanceId(String balanceId) {
		String sql="select c.id,c.dealer_code,c.dealer_name,nvl(c.DEDUCT_AMOUNT,0) as DEDUCT_AMOUNT from tt_as_wr_admin_deduct c where c.claimbalance_id ='"+balanceId+"'";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	
	/*********Iverson add By 2010-12-25 查看旧件明细(分页)*************************/
	public PageResult<Map<String, Object>> deductInfo(String deductId,int curPage, int pageSize){
		StringBuilder sql= new StringBuilder();
		sql.append("select c.id,c.DEDUCT_NO,c.PART_AMOUNT,c.DEDUCT_AMOUNT,c.MANHOUR_MONEY,c.MATERIAL_MONEY,c.OTHER_MONEY from TT_AS_WR_DEDUCT c where c.deduct_balance_id ='"+deductId+"' " );
		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	/**
	 * Iverson add By 2010-12-31
	 * 三包结算统计表
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> selectClaimBalance(String dealerCode,String DEALER_NAME,String yieldly,String startDate,String endDate,Double fixedAmount,int curPage, int pageSize){
		StringBuilder sql= new StringBuilder();
		sql.append("select c.*,\n" );
		sql.append("       BY_LABOUR_amount + by_part_amount as FREE_AMOUNT,\n" );
		sql.append("       labour_amount + BY_LABOUR_amount as amount_labour,\n" );
		sql.append("       part_amount + by_part_amount as amount_part,\n" );
		sql.append("       BY_LABOUR_amount + by_part_amount + part_amount + labour_amount as SUM_BALANCE_AMOUNT\n" );
		sql.append("\n" );
		sql.append("  from (select t.dealer_code,\n" );
		sql.append("               t.dealer_name,\n" );
		sql.append("               a.yieldly,\n" );
		sql.append("               sum(nvl(a.balance_labour_amount, 0) +\n" );
		sql.append("                   nvl(a.appendlabour_amount, 0)) as labour_amount, --工时费\n" );
		sql.append("               sum(nvl(a.balance_part_amount, 0)) as part_amount, --材料费,\n" );
		sql.append("               sum((select count(1)\n" );
		sql.append("                     from Tt_As_Wr_Application aa\n" );
		sql.append("                    where aa.id = a.id\n" );
		sql.append("                      and aa.claim_type = "+Constant.CLA_TYPE_02+")) as con,\n" );
		sql.append("               sum((select count(1)\n" );
		sql.append("                      from Tt_As_Wr_Application aa\n" );
		sql.append("                     where aa.id = a.id\n" );
		sql.append("                       and aa.claim_type = "+Constant.CLA_TYPE_02+") * "+fixedAmount+") as BY_LABOUR_amount,\n" );
		sql.append("               sum((select SUM(AA.BALANCE_AMOUNT)\n" );
		sql.append("                     from Tt_As_Wr_Application aa\n" );
		sql.append("                    where aa.id = a.id\n" );
		sql.append("                      and aa.claim_type = "+Constant.CLA_TYPE_02+")) -\n" );
		sql.append("               sum((select count(1)\n" );
		sql.append("                      from Tt_As_Wr_Application aa\n" );
		sql.append("                     where aa.id = a.id\n" );
		sql.append("                       and aa.claim_type = "+Constant.CLA_TYPE_02+") * "+fixedAmount+") as by_part_amount\n" );
		sql.append("          from tt_as_wr_application a, tm_dealer t\n" );
		sql.append("         where 1 = 1\n" );
		sql.append("           and a.dealer_id = t.dealer_id\n" );
		if(Utility.testString(dealerCode)){
			sql.append("   and t.dealer_code like '%"+dealerCode+"%'\n" );
		}
		if(Utility.testString(DEALER_NAME)){
			sql.append("   and t.dealer_name like '%"+DEALER_NAME+"%'\n" );
		}
		if(Utility.testString(yieldly)){
			sql.append("   and a.yieldly='"+yieldly+"'\n" );
		}
		if(Utility.testString(startDate)){
			sql.append("   AND a.auditing_date>=TO_DATE('"+startDate+"','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endDate)){
			sql.append("   AND a.auditing_date<=TO_DATE('"+endDate+"','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("         group by a.dealer_id, a.yieldly, t.dealer_code, t.dealer_name) c");

		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	public List<Map<String, Object>> selectClaimBalanceExcelList(String dealerCode,String DEALER_NAME,String yieldly,String startDate,String endDate,Double fixedAmount){
		StringBuilder sql= new StringBuilder();
		sql.append("select c.*,\n" );
		sql.append("       BY_LABOUR_amount + by_part_amount as FREE_AMOUNT,\n" );
		sql.append("       labour_amount + BY_LABOUR_amount as amount_labour,\n" );
		sql.append("       part_amount + by_part_amount as amount_part,\n" );
		sql.append("       BY_LABOUR_amount + by_part_amount + part_amount + labour_amount as SUM_BALANCE_AMOUNT\n" );
		sql.append("\n" );
		sql.append("  from (select t.dealer_code,\n" );
		sql.append("               t.dealer_name,\n" );
		sql.append("               a.yieldly,\n" );
		sql.append("               sum(nvl(a.balance_labour_amount, 0) +\n" );
		sql.append("                   nvl(a.appendlabour_amount, 0)) as labour_amount, --工时费\n" );
		sql.append("               sum(nvl(a.balance_part_amount, 0)) as part_amount, --材料费,\n" );
		sql.append("               sum((select count(1)\n" );
		sql.append("                     from Tt_As_Wr_Application aa\n" );
		sql.append("                    where aa.id = a.id\n" );
		sql.append("                      and aa.claim_type = "+Constant.CLA_TYPE_02+")) as con,\n" );
		sql.append("               sum((select count(1)\n" );
		sql.append("                      from Tt_As_Wr_Application aa\n" );
		sql.append("                     where aa.id = a.id\n" );
		sql.append("                       and aa.claim_type = "+Constant.CLA_TYPE_02+") * "+fixedAmount+") as BY_LABOUR_amount,\n" );
		sql.append("               sum((select SUM(AA.BALANCE_AMOUNT)\n" );
		sql.append("                     from Tt_As_Wr_Application aa\n" );
		sql.append("                    where aa.id = a.id\n" );
		sql.append("                      and aa.claim_type = "+Constant.CLA_TYPE_02+")) -\n" );
		sql.append("               sum((select count(1)\n" );
		sql.append("                      from Tt_As_Wr_Application aa\n" );
		sql.append("                     where aa.id = a.id\n" );
		sql.append("                       and aa.claim_type = "+Constant.CLA_TYPE_02+") * "+fixedAmount+") as by_part_amount\n" );
		sql.append("          from tt_as_wr_application a, tm_dealer t\n" );
		sql.append("         where 1 = 1\n" );
		sql.append("           and a.dealer_id = t.dealer_id\n" );
		if(Utility.testString(dealerCode)){
			sql.append("   and t.dealer_code like '%"+dealerCode+"%'\n" );
		}
		if(Utility.testString(DEALER_NAME)){
			sql.append("   and t.dealer_name like '%"+DEALER_NAME+"%'\n" );
		}
		if(Utility.testString(yieldly)){
			sql.append("   and a.yieldly='"+yieldly+"'\n" );
		}
		if(Utility.testString(startDate)){
			sql.append("   AND a.auditing_date>=TO_DATE('"+startDate+"','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endDate)){
			sql.append("   AND a.auditing_date<=TO_DATE('"+endDate+"','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("         group by a.dealer_id, a.yieldly, t.dealer_code, t.dealer_name) c");

		System.out.println("sqlsql=="+sql.toString());
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	/**********
	 * add xionghcuan 
	 * @param id 
	 * 查询汇总单下所有类型为保养和服务活动的索赔单
	 */
	
	public List<Map<String, Object>> auditApplication(long  id){
		StringBuffer sql= new StringBuffer();
		sql.append("select a.id from Tt_As_Wr_Application a where 1=1 and exists (\n" );
		sql.append("select b.claim_id from tr_balance_claim b where 1=1 and exists (\n" );
		sql.append("select gb.balance_id from  tr_gather_balance gb where gb.gather_id="+id+" and gb.balance_id = b.balance_id)\n" );
		sql.append("and a.id = b.claim_id\n" );
		sql.append(")  and a.claim_type in (10661002,10661006)");
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	
	
	//Iverson add by 2011-01-18
	public PageResult<Map<String, Object>> queryDealerInfo(String dealerCode,String dealerName,String DEALER_LEVEL,String STATUS,String area_code,String province,int curPage, int pageSize){
		StringBuilder sql= new StringBuilder();
		sql.append("select b.root_org_name,\n");
		sql.append("       c.region_name,\n");  
		sql.append("       a.dealer_code,\n");  
		sql.append("       a.dealer_name as one_name,\n");  
		sql.append("       a.dealer_level,\n");  
		sql.append("       ac.dealer_name as two_name,\n");  
		sql.append("       a.status,\n");  
		sql.append("       (select max(cb.stationer_tel) from tt_as_wr_claim_balance cb where cb.dealer_id=a.dealer_id) as stationer_tel,\n");  
		sql.append("       (select max(cb.claimer_tel) from tt_as_wr_claim_balance cb where cb.dealer_id=a.dealer_id) as claimer_tel\n");  
		sql.append("  from tm_dealer              a,\n");  
		sql.append("       vw_org_dealer_service  b,\n");  
		sql.append("       tm_region              c,\n");  
		sql.append("       tm_dealer              ac\n");  
		sql.append(" where a.dealer_type= "+Constant.DEALER_TYPE_DWR+" and a.province_id = c.region_code\n");  
		sql.append("   and a.dealer_id = b.dealer_id\n");  
		sql.append("   and a.parent_dealer_d = ac.dealer_id(+)\n");

		if(Utility.testString(dealerCode)){
			sql.append("   AND a.DEALER_code like '%"+dealerCode+"%'\n" );
		}
		if(Utility.testString(dealerName)){
			sql.append("   AND a.dealer_name LIKE '%"+dealerName+"%'\n" );
		}
		if(Utility.testString(DEALER_LEVEL)){
			sql.append("   AND a.dealer_level = '"+DEALER_LEVEL+"'\n" );
		}
		if(Utility.testString(STATUS)){
			sql.append("   AND a.STATUS = '"+STATUS+"'\n" );
		}
		if(Utility.testString(area_code)){
			sql.append("   AND b.root_org_code like '%"+area_code+"%'\n" );
		}
		if(Utility.testString(province)){
			sql.append("   AND c.region_code = '"+province+"'\n" );
		}
		sql.append(" ORDER BY a.DEALER_code DESC \n");
		System.out.println(sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	/*public PageResult<Map<String, Object>> queryDealerBalanceList(RequestWrapper request,AclUserBean logonUser,int curPage, int pageSize){
		StringBuffer sb= new StringBuffer();
		sb.append("with tb_check as\n" );
		sb.append("(select c.*,(c.AMOUNT_SUM-c.pdiAndKeepFitMoney-c.partAndAccMoney)as ohersMoney from(");
		sb.append(" (select B.LABOUR_AMOUNT,\n" );
		sb.append("         B.APPLY_PERSON_NAME,\n" );
		sb.append("         B.PART_AMOUNT,\n" );
		sb.append("         (B.AMOUNT_SUM + nvl(A.labour_sum, 0) + nvl(A.datum_sum, 0)) AMOUNT_SUM,\n" );
		sb.append("         B.DEALER_NAME,\n" );
		sb.append("         B.DEALER_CODE,\n" );
		sb.append("         B.REMARK REMARK,\n" );
		
		sb.append("(select nvl(sum(a.balance_amount), 0) as balance_amount\n" );
		sb.append("    from Tt_As_Wr_Application a\n" );
		sb.append("    where a.status = 10791007\n" );
		sb.append("    and a.is_invoice = 1\n" );
		sb.append("    and a.is_import = 10041002\n" );
		sb.append("    and a.claim_type in (10661011, 10661002)\n" );
		sb.append("    and  a.balance_no =B.REMARK) as pdiAndKeepFitMoney,\n" );
		sb.append("   (select nvl(sum(a.balance_part_amount), 0)+nvl(sum(a.accessories_price), 0)\n" );
		sb.append("  from Tt_As_Wr_Application a\n" );
		sb.append(" where a.status = 10791007\n" );
		sb.append("   and a.is_invoice = 1\n" );
		sb.append("   and a.is_import = 10041002\n" );
		sb.append("   and a.claim_type not in (10661011, 10661002)\n" );
		sb.append(" and a.balance_no  =B.REMARK) as partAndAccMoney,\n");

		sb.append("(select count(1) from (select distinct d.auth_price,r.balance_oder,d.return_no\n" );
		sb.append("            from tt_balance_return_relation r, tt_as_wr_old_returned d\n" );
		sb.append("           where d.id = r.id) c where c.balance_oder=b.REMARK) as count_return,");
		sb.append("         B.DEALER_ID,\n" );
		sb.append("         D.STATUS,\n" );
		sb.append("         to_char(B.CREATE_DATE, 'yyyy-mm-dd') CREATE_DATE,\n" );
		sb.append("         B.START_DATE,\n" );
		sb.append("         B.END_DATE,\n" );
		sb.append("         (nvl(A.datum_sum, 0) + nvl(A.labour_sum, 0)) datum_sum,\n" );
		sb.append("         (nvl(C.datum_sum, 0) + nvl(C.labour_sum, 0)) labour_sum,\n" );
		sb.append("         E.NAME COLLECT_TICKETS,\n" );
		sb.append("         F.NAME CHECK_TICKETS,\n" );
		sb.append("         G.NAME TRANSFER_TICKETS,\n" );
		sb.append("         to_char(D.COLLECT_TICKETS_DATE, 'YYYY-MM-DD HH24:MI') COLLECT_TICKETS_DATE,\n" );
		sb.append("         to_char(D.CHECK_TICKETS_DATE, 'YYYY-MM-DD HH24:MI') CHECK_TICKETS_DATE,\n" );
		sb.append("         to_char(D.TRANSFER_TICKETS_DATE, 'YYYY-MM-DD HH24:MI') TRANSFER_TICKETS_DATE\n" );
		sb.append("    from (select sum(nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sb.append("                     nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sb.append("                     nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +\n" );
		sb.append("                     nvl(t.SPECIAL_LABOUR_SUM, 0) -\n" );
		sb.append("                     nvl(t.PACKGE_CHANGE_SUM, 0) - nvl(t.BARCODE_SUM, 0) +\n" );
		sb.append("                     nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sb.append("                     nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sb.append("                     nvl(t.SERVICE_OTHER_AMOUNT, 0) +\n" );
		sb.append("                     nvl(t.accessories_price, 0) +\n" );
		sb.append("                     nvl(t.compensation_money, 0)) LABOUR_AMOUNT,\n" );
		sb.append("                 t.REMARK,\n" );
		sb.append("                 min(t.charge_id) charge_id,\n" );
		sb.append("                 sum(nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) +\n" );
		sb.append("                     nvl(t.FREE_PART_AMOUNT, 0) +\n" );
		sb.append("                     nvl(t.PLUS_MINUS_DATUM_SUM, 0) +\n" );
		sb.append("                     nvl(t.SPECIAL_DATUM_SUM, 0) - nvl(t.OLD_DEDUCT, 0)) PART_AMOUNT,\n" );
		sb.append("                 sum(t.AMOUNT_SUM) AMOUNT_SUM,\n" );
		sb.append("                 min(t.DEALER_NAME) DEALER_NAME,\n" );
		sb.append("                 min(t.DEALER_CODE) DEALER_CODE,\n" );
		sb.append("                 t.DEALER_ID,\n" );
		sb.append("                 min(t.CREATE_DATE) CREATE_DATE,\n" );
		sb.append("                 to_char(min(t.START_DATE), 'yyyy-mm-dd') START_DATE,\n" );
		sb.append("                 to_char(min(t.END_DATE), 'yyyy-mm-dd') END_DATE,\n" );
		sb.append("                 t.APPLY_PERSON_NAME\n" );
		sb.append("            from tt_as_wr_claim_balance t\n" );
		sb.append("           where 1 = 1\n" );
		sb.append("             and t.BALANCE_YIELDLY = 95411001\n" );
		DaoFactory.getsql(sb, "t.APPLY_PERSON_NAME", DaoFactory.getParam(request, "applyPersonName"), 2);
		DaoFactory.getsql(sb, "t.CREATE_DATE", DaoFactory.getParam(request, "startDate"), 3);
		DaoFactory.getsql(sb, "t.CREATE_DATE", DaoFactory.getParam(request, "endDate"), 4);
		DaoFactory.getsql(sb, "t.DEALER_CODE", DaoFactory.getParam(request, "dealerCode"), 6);
		DaoFactory.getsql(sb, "t.REMARK", DaoFactory.getParam(request, "REMARK"), 1);
		String return_no = DaoFactory.getParam(request, "return_no");
		String claim_no = DaoFactory.getParam(request, "claim_no");
		if(BaseUtils.testString(return_no)){
			sb.append("             and t.remark in\n" );
			sb.append("                 (select r.balance_oder\n" );
			sb.append("                    from tt_balance_return_relation r, tt_as_wr_old_returned d\n" );
			sb.append("                   where d.id = r.id\n" );
			sb.append("                     and d.return_no = '"+return_no+"')\n" );
		}
		if(BaseUtils.testString(claim_no)){
			sb.append("             and t.remark in\n" );
			sb.append("                 (select a.balance_no\n" );
			sb.append("                    from tt_as_wr_application a\n" );
			sb.append("                   where a.claim_no = '"+claim_no+"')\n");
		}
		sb.append("           GROUP by t.DEALER_ID,\n" );
		sb.append("                    t.START_DATE,\n" );
		sb.append("                    t.END_DATE,\n" );
		sb.append("                    t.REMARK,\n" );
		sb.append("                    t.APPLY_PERSON_NAME) B\n" );
		sb.append("    LEFT join tt_as_wr_administrative_charge A\n" );
		sb.append("      on A.BALANCE_ODER = B.REMARK\n" );
		sb.append("    LEFT join tt_as_wr_administrative_charge C\n" );
		sb.append("      on A.id = B.charge_id\n" );
		sb.append("    LEFT join TT_AS_PAYMENT D\n" );
		sb.append("      on D.BALANCE_ODER = B.REMARK\n" );
		sb.append("    left join TC_USER E\n" );
		sb.append("      on E.USER_ID = D.COLLECT_TICKETS\n" );
		sb.append("    left join TC_USER F\n" );
		sb.append("      on F.USER_ID = D.CHECK_TICKETS\n" );
		sb.append("    left join TC_USER G\n" );
		sb.append("      on G.USER_ID = D.TRANSFER_TICKETS\n" );
		sb.append("   ORDER by B.CREATE_DATE desc)\n" );
		
		sb.append(") c where 1=1 )");
		sb.append("select distinct * from tb_check");
		PageResult<Map<String, Object>> ps = pageQuery(sb.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}*/
	public PageResult<Map<String, Object>> queryDealerBalanceList(RequestWrapper request,AclUserBean logonUser,int curPage, int pageSize){
		List<Object> listPar=new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select t.id,\n" );
		sql.append("       t.balance_no,--单号\n" );
		sql.append("       t.dealer_id,\n" );
		sql.append("       d.dealer_code,\n" );
		sql.append("       d.dealer_name,\n" );
		sql.append("       t.status,--状态\n" );
		sql.append("       t.labour_amount,--工时金额\n" );
		sql.append("       t.part_amount,--配件金额\n" );
		sql.append("       t.free_amount,--保养金额\n" );
		sql.append("       t.service_fixed_amount,--服务活动\n" );
		sql.append("       t.return_amount,--运费\n" );
		sql.append("       t.amount_sum,--二次抵扣\n" );
		sql.append("       t.claim_count,--索赔单数\n" );
		sql.append("       to_char(t.start_date,'YYYY-MM-DD') AS start_date,--开始时间\n" );
		sql.append("       to_char(t.end_date,'YYYY-MM-DD') AS end_date,--结束时间\n" );
		sql.append("       to_char(t.create_date,'YYYY-MM-DD') AS create_date,--创建时间\n" );
		sql.append("       t.ADMIN_DEDUCT,--上次行政抵扣\n" );
		sql.append("       t.FINANCIAL_DEDUCT,--本次行政抵扣\n" );
		sql.append("       t.NOTE_AMOUNT,--开票总金额\n" );
		sql.append("       t.MARKET_AMOUNT,--pdi金额\n" );
		sql.append("       t.SPEOUTFEE_AMOUNT,--外出金额\n" );
		sql.append("       t.APPEND_LABOUR_AMOUNT,--正负激励\n" );
		sql.append("       u.name as REVIEW_APPLICATION_BY,--收票人\n" );
		sql.append("       to_char(t.REVIEW_APPLICATION_TIME,'YYYY-MM-DD hh24:mi:ss') as COLLECT_TICKETS_DATE,--收票时间\n" );		
		sql.append("       t.APPEND_AMOUNT--善于索赔\n" );
		sql.append("  from tt_as_wr_claim_balance t\n" );
		sql.append("  left join tm_dealer d\n" );
		sql.append("    on t.dealer_id = d.dealer_id\n" );
		sql.append("  left join tc_user u\n" );
		sql.append("    on t.REVIEW_APPLICATION_BY = u.user_id\n" );
		sql.append("    where 1=1\n" );
		if(request.getParamValue("BalanceNo")!=null && !"".equals(request.getParamValue("BalanceNo"))){
			sql.append(" and t.balance_no like '%"+request.getParamValue("BalanceNo")+"%' ");
		}
		if(request.getParamValue("startDate")!=null && !"".equals(request.getParamValue("startDate"))){
			sql.append(" and to_date(to_char(t.CREATE_DATE,'yyyy-mm-dd'),'yyyy-mm-dd') >= to_date(?,'yyyy-mm-dd') \n");
			listPar.add(request.getParamValue("startDate"));
		}
		if(request.getParamValue("endDate")!=null && !"".equals(request.getParamValue("endDate"))){
			sql.append(" and to_date(to_char(T.CREATE_DATE,'yyyy-mm-dd'),'yyyy-mm-dd') <= to_date(?,'yyyy-mm-dd') \n");
			listPar.add(request.getParamValue("endDate"));
		}
		if(request.getParamValue("dealerCode")!=null && !"".equals(request.getParamValue("dealerCode"))){
			sql.append(" and d.dealer_code = '"+request.getParamValue("dealerCode")+"' ");
		}
		sql.append("    order by t.create_date desc,t.status asc");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), listPar, getFunName(), pageSize, curPage);
		return ps;
	}
	public List<Map<String, Object>> queryDealerBalanceListExport(auditBean bean){
		StringBuffer sql= new StringBuffer();
		sql.append(" select B.LABOUR_AMOUNT,B.APPLY_PERSON_NAME,B.PART_AMOUNT,(B.AMOUNT_SUM+nvl(A.labour_sum,0)+nvl(A.datum_sum,0)) AMOUNT_SUM ,B.DEALER_NAME,B.DEALER_CODE, B.REMARK REMARK," );
		sql.append(" B.DEALER_ID, D.STATUS,to_char( B.CREATE_DATE,'yyyy-mm-dd'  ) CREATE_DATE ,B.START_DATE,B.END_DATE,(nvl(A.datum_sum,0)+nvl(A.labour_sum,0)) datum_sum,(nvl(C.datum_sum,0)+nvl(C.labour_sum,0)) labour_sum, ");
		sql.append(" E.NAME COLLECT_TICKETS ,F.NAME CHECK_TICKETS, G.NAME TRANSFER_TICKETS, to_char(D.COLLECT_TICKETS_DATE,'YYYY-MM-DD HH24:MI') COLLECT_TICKETS_DATE, to_char(D.CHECK_TICKETS_DATE,'YYYY-MM-DD HH24:MI') CHECK_TICKETS_DATE,to_char(D.TRANSFER_TICKETS_DATE,'YYYY-MM-DD HH24:MI') TRANSFER_TICKETS_DATE   from  ");
		sql.append("(select sum(nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +nvl(t.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(t.PACKGE_CHANGE_SUM, 0)-nvl(t.BARCODE_SUM, 0)+nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(t.SERVICE_OTHER_AMOUNT, 0) \n" );
		sql.append("           ) LABOUR_AMOUNT,\n" );
		sql.append("  t.REMARK,min(t.charge_id) charge_id,");
		sql.append("       sum(nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) +nvl(t.FREE_PART_AMOUNT, 0)+ \n" );
		sql.append("         nvl(t.PLUS_MINUS_DATUM_SUM, 0)+nvl(t.SPECIAL_DATUM_SUM, 0)- nvl(t.OLD_DEDUCT, 0)) PART_AMOUNT,\n" );
		sql.append("       sum(t.AMOUNT_SUM) AMOUNT_SUM,min(t.DEALER_NAME) DEALER_NAME,min(t.DEALER_CODE) DEALER_CODE,\n" );
		sql.append("       t.DEALER_ID,\n" );
		sql.append("       min(t.CREATE_DATE) CREATE_DATE,\n" );
		sql.append("       to_char(min(t.START_DATE),'yyyy-mm-dd') START_DATE,\n" );
		sql.append("       to_char(min(t.END_DATE),'yyyy-mm-dd') END_DATE,t.APPLY_PERSON_NAME\n" );
		sql.append("\n" );
		sql.append("  from tt_as_wr_claim_balance t\n" );
		sql.append(" where 1 = 1 and t.BALANCE_YIELDLY = 95411001\n" );
		if(Utility.testString(bean.getPerson())){
			sql.append("AND t.APPLY_PERSON_NAME like '%"+bean.getPerson()+"%'\n");
		}
		/******mod by liuxh 20101115 ******/
		
		/******mod by liuxh 20101115 ******/
		String dealerCode = bean.getDealerCode();
		if(Utility.testString(dealerCode)){
			sql.append("AND t.DEALER_CODE in ('"+dealerCode.replace(",", "','")+"')\n");
		}
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND t.CREATE_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND t.CREATE_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(" GROUP by t.DEALER_ID, t.START_DATE, t.END_DATE,t.REMARK,t.APPLY_PERSON_NAME ) B\n" );
		sql.append(" LEFT join tt_as_wr_administrative_charge A on A.BALANCE_ODER = B.REMARK ");
		sql.append(" LEFT join tt_as_wr_administrative_charge C on A.id = B.charge_id ");
		sql.append(" LEFT join TT_AS_PAYMENT D on D.BALANCE_ODER = B.REMARK ");
		sql.append("left join TC_USER E on E.USER_ID = D.COLLECT_TICKETS\n" );
		sql.append(" left join TC_USER F on F.USER_ID = D.CHECK_TICKETS\n" );
		sql.append(" left join TC_USER G on G.USER_ID = D.TRANSFER_TICKETS");

		sql.append(" ORDER by  B.CREATE_DATE desc");

		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	public static Object toExceVender(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list,String name,String name2,String strSet)
			throws Exception {

		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet(name2, 0);
			WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(Alignment.CENTRE);
			
			WritableFont font1 = new WritableFont(WritableFont.ARIAL,12,WritableFont.NO_BOLD);
			WritableCellFormat wcf1 = new WritableCellFormat(font1);
			wcf1.setAlignment(Alignment.CENTRE);
			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i],wcf1));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i],wcf));
				}
			}
			if(!"".equalsIgnoreCase(strSet)){
				String[] ss = strSet.split(",");
				for(int i=0;i<ss.length;i++){
					ws.setColumnView(Integer.parseInt(ss[i]), 25);
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	
	
	public PageResult<Map<String, Object>> queryDealerTickets(RequestWrapper request,int curPage, int pageSize){
		StringBuffer sb= new StringBuffer();
		sb.append("with tb_a as\n" );
		sb.append(" (select B.LABOUR_AMOUNT,\n" );
		sb.append("         B.PART_AMOUNT,\n" );
		sb.append("         (B.AMOUNT_SUM + nvl(A.labour_sum, 0) + nvl(A.datum_sum, 0)) AMOUNT_SUM,\n" );
		sb.append("         B.DEALER_NAME,\n" );
		sb.append("         B.DEALER_CODE,\n" );
		sb.append("         B.REMARK REMARK,\n" );
		sb.append("         B.DEALER_ID,\n" );
		sb.append("         D.STATUS,\n" );
		sb.append("         d.balance_oder,\n" );
		sb.append("         d.notes,\n" );
		sb.append("         to_char(B.CREATE_DATE, 'yyyy-mm-dd') CREATE_DATE,\n" );
		sb.append("         B.START_DATE,\n" );
		sb.append("         B.END_DATE,\n" );
		sb.append("         (nvl(A.datum_sum, 0) + nvl(A.labour_sum, 0)) datum_sum,\n" );
		sb.append("         (nvl(C.datum_sum, 0) + nvl(C.labour_sum, 0)) labour_sum,\n" );
		sb.append("         E.NAME COLLECT_TICKETS,\n" );
		sb.append("         F.NAME CHECK_TICKETS,\n" );
		sb.append("         G.NAME TRANSFER_TICKETS,\n" );
		sb.append("         to_char(D.COLLECT_TICKETS_DATE, 'YYYY-MM-DD HH24:MI') COLLECT_TICKETS_DATE,\n" );
		sb.append("         to_char(D.CHECK_TICKETS_DATE, 'YYYY-MM-DD HH24:MI') CHECK_TICKETS_DATE,\n" );
		sb.append("         to_char(D.TRANSFER_TICKETS_DATE, 'YYYY-MM-DD HH24:MI') TRANSFER_TICKETS_DATE,\n" );
		sb.append("         decode(D.STATUS,\n" );
		sb.append("                1,\n" );
		sb.append("                '等待收票',\n" );
		sb.append("                2,\n" );
		sb.append("                '等待验票',\n" );
		sb.append("                3,\n" );
		sb.append("                '等待转账',\n" );
		sb.append("                4,\n" );
		sb.append("                '已转账',\n" );
		sb.append("                '') state\n" );
		sb.append("    from (select sum(nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sb.append("                     nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sb.append("                     nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +\n" );
		sb.append("                     nvl(t.SPECIAL_LABOUR_SUM, 0) -\n" );
		sb.append("                     nvl(t.PACKGE_CHANGE_SUM, 0) - nvl(t.BARCODE_SUM, 0) +\n" );
		sb.append("                     nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sb.append("                     nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sb.append("                     nvl(t.SERVICE_OTHER_AMOUNT, 0)) LABOUR_AMOUNT,\n" );
		sb.append("                 t.REMARK,\n" );
		sb.append("                 min(t.charge_id) charge_id,\n" );
		sb.append("                 sum(nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) +\n" );
		sb.append("                     nvl(t.FREE_PART_AMOUNT, 0) +\n" );
		sb.append("                     nvl(t.PLUS_MINUS_DATUM_SUM, 0) +\n" );
		sb.append("                     nvl(t.SPECIAL_DATUM_SUM, 0) - nvl(t.OLD_DEDUCT, 0)) PART_AMOUNT,\n" );
		sb.append("                 sum(t.AMOUNT_SUM) AMOUNT_SUM,\n" );
		sb.append("                 min(t.DEALER_NAME) DEALER_NAME,\n" );
		sb.append("                 min(t.DEALER_CODE) DEALER_CODE,\n" );
		sb.append("                 t.DEALER_ID,\n" );
		sb.append("                 min(t.CREATE_DATE) CREATE_DATE,\n" );
		sb.append("                 to_char(min(t.START_DATE), 'yyyy-mm-dd') START_DATE,\n" );
		sb.append("                 to_char(min(t.END_DATE), 'yyyy-mm-dd') END_DATE\n" );
		sb.append("\n" );
		sb.append("            from tt_as_wr_claim_balance t\n" );
		sb.append("           where 1 = 1\n" );
		sb.append("             and t.BALANCE_YIELDLY = 95411001\n" );
		DaoFactory.getsql(sb, "t.dealer_code", DaoFactory.getParam(request, "dealerCode"), 2);
		String dealer_shortname = DaoFactory.getParam(request, "dealer_shortname");
		if(BaseUtils.testString(dealer_shortname)){
			sb.append(" and t.dealer_id in (select tm.dealer_id from tm_dealer tm where 1=1 ");
			DaoFactory.getsql(sb, "tm.dealer_shortname", dealer_shortname, 2);
			sb.append(" )");
		}
		DaoFactory.getsql(sb, "t.CREATE_DATE", DaoFactory.getParam(request, "startDate"), 3);
		DaoFactory.getsql(sb, "t.CREATE_DATE", DaoFactory.getParam(request, "endDate"), 4);
		sb.append("           GROUP by t.DEALER_ID, t.START_DATE, t.END_DATE, t.REMARK) B\n" );
		sb.append("    LEFT join tt_as_wr_administrative_charge A\n" );
		sb.append("      on A.BALANCE_ODER = B.REMARK\n" );
		sb.append("    LEFT join tt_as_wr_administrative_charge C\n" );
		sb.append("      on A.id = B.charge_id\n" );
		sb.append("    LEFT join TT_AS_PAYMENT D\n" );
		sb.append("      on D.BALANCE_ODER = B.REMARK\n" );
		sb.append("    left join TC_USER E\n" );
		sb.append("      on E.USER_ID = D.COLLECT_TICKETS\n" );
		sb.append("    left join TC_USER F\n" );
		sb.append("      on F.USER_ID = D.CHECK_TICKETS\n" );
		sb.append("    left join TC_USER G\n" );
		sb.append("      on G.USER_ID = D.TRANSFER_TICKETS\n" );
		sb.append("   where D.STATUS in (1, 2, 3, 4) \n" );
		DaoFactory.getsql(sb, "D.COLLECT_TICKETS_DATE", DaoFactory.getParam(request, "collectStartDate"), 3);
		DaoFactory.getsql(sb, "D.COLLECT_TICKETS_DATE", DaoFactory.getParam(request, "collectEndDate"), 4);
		DaoFactory.getsql(sb, "D.CHECK_TICKETS_DATE", DaoFactory.getParam(request, "checkStartDate"), 3);
		DaoFactory.getsql(sb, "D.CHECK_TICKETS_DATE", DaoFactory.getParam(request, "checkEndDate"), 4);
		DaoFactory.getsql(sb, "D.TRANSFER_TICKETS_DATE", DaoFactory.getParam(request, "transferStartDate"), 3);
		DaoFactory.getsql(sb, "D.TRANSFER_TICKETS_DATE", DaoFactory.getParam(request, "transferEndDate"), 4);
		DaoFactory.getsql(sb, "E.NAME", DaoFactory.getParam(request, "collectTickets"), 2);
		DaoFactory.getsql(sb, "F.NAME", DaoFactory.getParam(request, "checkTickets"), 2);
		DaoFactory.getsql(sb, "G.NAME", DaoFactory.getParam(request, "transferTickets"), 2);
		DaoFactory.getsql(sb, "D.STATUS", DaoFactory.getParam(request, "state"), 1);
		DaoFactory.getsql(sb, "B.REMARK", DaoFactory.getParam(request, "REMARK"), 2);
		sb.append("   ORDER by B.CREATE_DATE desc)\n" );
		sb.append("select distinct * from tb_a");
		PageResult<Map<String, Object>> ps = pageQuery(sb.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	} 
	
	
	public PageResult<Map<String, Object>> queryDealerBalanceList02(auditBean bean,int curPage, int pageSize){
		List<Object> listPar=new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select t.id,\n" );
		sql.append("       t.balance_no,--单号\n" );
		sql.append("       t.dealer_id,\n" );
		sql.append("       d.dealer_code,\n" );
		sql.append("       d.dealer_name,\n" );
		sql.append("       d.PHONE,\n" );
		sql.append("       t.status,--状态\n" );
		sql.append("       t.labour_amount,--工时金额\n" );
		sql.append("       t.part_amount,--配件金额\n" );
		sql.append("       t.free_amount,--保养金额\n" );
		sql.append("       t.service_fixed_amount,--服务活动\n" );
		sql.append("       t.return_amount,--运费\n" );
		sql.append("       t.amount_sum,--二次抵扣\n" );
		sql.append("       t.claim_count,--索赔单数\n" );
		sql.append("       t.FUNANCIAL_REMARK,--审核备注\n" );
		sql.append("       to_char(t.start_date,'YYYY-MM-DD') AS start_date,--开始时间\n" );
		sql.append("       to_char(t.end_date,'YYYY-MM-DD') AS end_date,--结束时间\n" );
		sql.append("       to_char(t.create_date,'YYYY-MM-DD') AS create_date,--创建时间\n" );
		sql.append("       t.ADMIN_DEDUCT,--上次行政抵扣\n" );
		sql.append("       t.FINANCIAL_DEDUCT,--本次行政抵扣\n" );
		sql.append("       t.NOTE_AMOUNT,--开票总金额\n" );
		sql.append("       t.MARKET_AMOUNT,--pdi金额\n" );
		sql.append("       t.SPEOUTFEE_AMOUNT,--外出金额\n" );
		sql.append("       t.APPEND_LABOUR_AMOUNT,--正负激励\n" );
		sql.append("       u.name as REVIEW_APPLICATION_BY,--收票人\n" );
		sql.append("       to_char(t.REVIEW_APPLICATION_TIME,'YYYY-MM-DD hh24:mi:ss') as COLLECT_TICKETS_DATE,--收票时间\n" );		
		sql.append("       t.APPEND_AMOUNT--善于索赔\n" );
		sql.append("  from tt_as_wr_claim_balance t\n" );
		sql.append("  left join tm_dealer d\n" );
		sql.append("    on t.dealer_id = d.dealer_id\n" );
		sql.append("  left join tc_user u\n" );
		sql.append("    on t.REVIEW_APPLICATION_BY = u.user_id\n" );
		sql.append("    where 1=1\n" );
		if(Utility.testString(bean.getBalanceNo())){
			sql.append(" and t.balance_no like '%"+bean.getBalanceNo()+"%' ");
		}
		if(Utility.testString(bean.getStartDate())){
			sql.append(" and to_date(to_char(t.CREATE_DATE,'yyyy-mm-dd'),'yyyy-mm-dd') >= to_date(?,'yyyy-mm-dd') \n");
			listPar.add(bean.getStartDate());
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append(" and to_date(to_char(T.CREATE_DATE,'yyyy-mm-dd'),'yyyy-mm-dd') <= to_date(?,'yyyy-mm-dd') \n");
			listPar.add(bean.getEndDate());
		}
		if(Utility.testString(bean.getDealerId())){
			sql.append(" and t.dealer_id = "+bean.getDealerId()+" ");
		}
		sql.append("    order by t.create_date desc,t.status asc");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), listPar, getFunName(), pageSize, curPage);
		return ps;
	} 
	public List<Map<String, Object>> seBill(String BalanceNo){		
		StringBuffer sql= new StringBuffer();
		sql.append("select t.id,\n" );
		sql.append("       t.balance_no,\n" );
		sql.append("       t.pc_no,\n" );
		sql.append("       t.bill_no,\n" );
		sql.append("       t.money,\n" );
		sql.append("       t.tax_money,\n" );
		sql.append("       t.total,\n" );
		sql.append("       t.remark\n" );
		sql.append("  from tt_as_wr_balance_bill t\n" );
		sql.append(" where t.balance_no = '"+BalanceNo+"'");

		List<Map<String, Object>> list = pageQuery(sql.toString(),null, getFunName());
		return list;
	}
	public List<Map<String, Object>> claimNum(String BalanceNo){		
		StringBuffer sql= new StringBuffer();
		sql.append("select c.id,c.repair_type\n" );
		sql.append("  from TT_AS_WR_APPLICATION_CLAIM c\n" );
		sql.append(" inner join tt_as_wr_claim_balance t\n" );
		sql.append("    on c.bill_no = t.balance_no\n" );
		sql.append(" where t.balance_no = '"+BalanceNo+"'");

		List<Map<String, Object>> list = pageQuery(sql.toString(),null, getFunName());
		return list;
	} 
	
	public PageResult<Map<String, Object>> queryDealerBalanceList01(auditBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append(" select B.LABOUR_AMOUNT,B.PART_AMOUNT,(B.AMOUNT_SUM+nvl(A.labour_sum,0)+nvl(A.datum_sum,0)) AMOUNT_SUM ,B.DEALER_NAME,B.DEALER_CODE, " );
		sql.append(" B.DEALER_ID,B.CREATE_DATE,B.START_DATE,B.END_DATE,(nvl(A.datum_sum,0)+nvl(A.labour_sum,0)) datum_sum,(nvl(C.datum_sum,0)+nvl(C.labour_sum,0)) labour_sum from ");
		sql.append("(select sum(nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +nvl(t.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(t.PACKGE_CHANGE_SUM, 0)-nvl(t.BARCODE_SUM, 0)+nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(t.SERVICE_OTHER_AMOUNT, 0) \n" );
		sql.append("           ) LABOUR_AMOUNT,\n" );
		sql.append("  t.REMARK,min(t.charge_id) charge_id,");
		sql.append("       sum(nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) +\n" );
		sql.append("         nvl(t.PLUS_MINUS_DATUM_SUM, 0)+nvl(t.SPECIAL_DATUM_SUM, 0)- nvl(t.OLD_DEDUCT, 0)) PART_AMOUNT,\n" );
		sql.append("       sum(t.AMOUNT_SUM) AMOUNT_SUM,min(t.DEALER_NAME) DEALER_NAME,min(t.DEALER_CODE) DEALER_CODE,\n" );
		sql.append("       t.DEALER_ID,\n" );
		sql.append("       to_char(min(t.CREATE_DATE),'yyyy-mm-dd') CREATE_DATE,\n" );
		sql.append("       to_char(min(t.START_DATE),'yyyy-mm-dd') START_DATE,\n" );
		sql.append("       to_char(min(t.END_DATE),'yyyy-mm-dd') END_DATE\n" );
		sql.append("\n" );
		sql.append("  from tt_as_wr_claim_balance t\n" );
		sql.append(" where 1 = 1 and t.BALANCE_YIELDLY = 95411002\n" );
		/******mod by liuxh 20101115 ******/
		
		/******mod by liuxh 20101115 ******/
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND t.DEALER_ID = ").append(bean.getDealerCode()).append("\n");
		}
		
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND t.CREATE_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND t.CREATE_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(" GROUP by t.DEALER_ID, t.START_DATE, t.END_DATE,t.REMARK ) B\n" );
		sql.append(" LEFT join tt_as_wr_administrative_charge A on A.BALANCE_ODER = B.REMARK ");
		sql.append(" LEFT join tt_as_wr_administrative_charge C on A.id = B.charge_id ");
		sql.append(" ORDER by  B.START_DATE asc");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	} 
	
	
	public Map<String, Object> getBalanceMainMapView(String id){
		StringBuffer sql = new StringBuffer();
		TcCodePO code = new TcCodePO() ;
		code.setType("8008") ;
		List listCode = this.select(code) ;
		if(listCode.size()>0){
			code = (TcCodePO)listCode.get(0);
		
			//轿车添加配件是不是监控判断
			if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
				sql.append("SELECT B.AUTH_PERSON_NAME,B.AUTH_TIME,b.auth_status,A.*, (NVL(A.FREE_DEDUCT,0) + NVL(A.SERVICE_DEDUCT, 0) + NVL(A.CHECK_DEDUCT,0) + NVL(A.OLD_DEDUCT,0) + NVL(A.ADMIN_DEDUCT,0)) DEDUCT,\n" );
			 }
			else{
				sql.append("SELECT B.AUTH_PERSON_NAME,B.AUTH_TIME,b.auth_status,A.*, (NVL(A.FREE_DEDUCT,0) + NVL(A.SERVICE_DEDUCT, 0) - NVL(A.CHECK_DEDUCT,0) + NVL(A.OLD_DEDUCT,0) + NVL(A.ADMIN_DEDUCT,0)) DEDUCT,\n" );
			}
		}
	
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATEDATE,\n" );
		sql.append("       TO_CHAR(A.START_DATE, 'YYYY-MM-DD') STARTDATE,\n" );
		sql.append("	   (NVL(A.MARKET_AMOUNT, 0) + NVL(A.APPEND_AMOUNT, 0)) DECLARE_SUM1, \n");
		/*********add by liuxh 20101209 增加汇总结算单打印取值***********/
		sql.append("      (SELECT K.DEALER_CODE FROM TM_DEALER K WHERE K.DEALER_ID=A.DEALER_ID) AS DEALER_CODE_WX,"); //维修站代码
		sql.append("      (SELECT K.DEALER_NAME FROM TM_DEALER K WHERE K.DEALER_ID=A.DEALER_ID) AS DEALER_NAME_WX,"); //维修站名称
		sql.append("      (SELECT K.DEALER_CODE FROM TM_DEALER K WHERE K.DEALER_ID=A.KP_DEALER_ID) AS DEALER_CODE_KP,A.STATIONER_TEL,"); //开票单位代码 . 站长电话
		sql.append("      (SELECT COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM_TMP L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS SQ_REPAIR,\n"); //售前维修
		sql.append("      (SELECT COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM_TMP L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS YB_REPAIR,\n"); //一般维修
		sql.append("      (SELECT COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM_TMP L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS WC_REPAIR,\n"); //外出维修
		sql.append("      (SELECT COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM_TMP L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS BY_REPAIR,\n"); //免费保养
		sql.append("      (SELECT COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM_TMP L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS FW_REPAIR,\n"); //服务活动
		sql.append("      (SELECT COUNT(1) FROM TT_AS_WR_SPEFEE K WHERE K.FEE_TYPE=? AND K.CLAIMBALANCE_ID_TMP=A.ID) AS SPEC_OUT_COUNT,\n"); //特殊外出数量
		sql.append("      (SELECT COUNT(1) FROM TT_AS_WR_SPEFEE K WHERE K.FEE_TYPE=? AND K.CLAIMBALANCE_ID_TMP=A.ID) AS SPEC_FEE_COUNT,\n"); //特殊费用数量
		sql.append("      ((SELECT COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM_TMP L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID)+");//总单据数 索赔单+特殊费用
		sql.append("      (SELECT COUNT(1) FROM TT_AS_WR_SPEFEE K WHERE K.CLAIMBALANCE_ID_TMP=A.ID)) AS ALL_COUNT,");//总单据数 索赔单+特殊费用
		
		sql.append("      (SELECT SUM(NVL(K.DECLARE_SUM,0)) FROM TT_AS_WR_SPEFEE K WHERE K.FEE_TYPE=? AND K.CLAIMBALANCE_ID_TMP=A.ID) AS SPEC_FEE_MARKET,\n"); //特殊外出费用
		sql.append("      (SELECT SUM(NVL(K.DECLARE_SUM,0)) FROM TT_AS_WR_SPEFEE K WHERE K.FEE_TYPE=? AND K.CLAIMBALANCE_ID_TMP=A.ID) AS SPEC_FEE_OUT,\n"); //特殊费用 市场公单
		
		/*********add by liuxh 20101209 增加汇总结算单打印取值***********/
		sql.append("	   (NVL(A.SERVICE_FIXED_AMOUNT, 0) + NVL(A.SERVICE_LABOUR_AMOUNT, 0) + NVL(A.SERVICE_PART_AMOUNT, 0) + NVL(A.SERVICE_OTHER_AMOUNT, 0)) SERVICE_AMOUNT,");
		sql.append("       TO_CHAR(A.END_DATE, 'YYYY-MM-DD') ENDDATE,\n" );
		sql.append("	   NVL(A.NOTE_AMOUNT, A.BALANCE_AMOUNT) NOTEAMOUNT,a.invoice_maker\n");
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE_TMP A\n" );
		sql.append(" LEFT OUTER JOIN TT_AS_WR_BALANCE_AUTHITEM B ON A.ID=B.BALANCE_ID \n");
		sql.append("WHERE A.ID = ? \n");
		sql.append(" order by b.auth_time desc");
		
		List parList=new ArrayList();
		parList.add(Constant.CLA_TYPE_07);//售前维修
		parList.add(Constant.CLA_TYPE_01);//一般维修
		parList.add(Constant.CLA_TYPE_09);//外出维修
		parList.add(Constant.CLA_TYPE_02);//免费保养
		parList.add(Constant.CLA_TYPE_06);//服务活动
		parList.add(Constant.FEE_TYPE_02);//特殊费用 外出
		parList.add(Constant.FEE_TYPE_01);//特殊费用  市场公单
		parList.add(Constant.FEE_TYPE_02);//特殊费用 外出
		parList.add(Constant.FEE_TYPE_01);//特殊费用  市场公单
		parList.add(Long.valueOf(id));
		
		Map<String, Object> map = pageQueryMap(sql.toString(), parList, getFunName());
		return map;
	}
	public List<Map<String, Object>> getBalanceMainList(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.*, (NVL(A.SERVICE_FIXED_AMOUNT, 0) + NVL(A.SERVICE_LABOUR_AMOUNT, 0) + NVL(A.SERVICE_PART_AMOUNT, 0) + NVL(A.SERVICE_OTHER_AMOUNT, 0)) SERVICE_AMOUNT FROM Tt_Balance_Detail_Bak_TMP A ");
		sql.append("WHERE BALANCE_ID = ").append(id);
		//logger.info("------------"+sql.toString());
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public Double testClaimBalance(Long balanceId){
		String sql="SELECT LABOUR_AMOUNT FROM TT_AS_WR_CLAIM_BALANCE_TMP WHERE ID="+balanceId;
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return ((java.math.BigDecimal)((Map)list.get(0)).get("LABOUR_AMOUNT")).doubleValue();
	}
	public Map<String, Object> getBalanceMainMap(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.*, NVL(D.DAMOUNT, 0) DAMOUNT,\n" );
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATEDATE,\n" );
		sql.append("       TO_CHAR(A.START_DATE, 'YYYY-MM-DD') STARTDATE,\n" );
		sql.append("       TO_CHAR(A.END_DATE, 'YYYY-MM-DD') ENDDATE,\n" );
		/*
		sql.append("	   (NVL(E.DECLARE_SUM1, 0) + NVL(A.APPEND_AMOUNT, 0)) DECLARE_SUM1, \n");
		sql.append("       NVL(F.DECLARE_SUM2, 0) DECLARE_SUM2, \n");
		*/
		sql.append("	   (NVL(A.MARKET_AMOUNT, 0) + NVL(A.APPEND_AMOUNT, 0)) DECLARE_SUM1, \n");
		sql.append("       NVL(A.SPEOUTFEE_AMOUNT, 0) DECLARE_SUM2, \n");
		sql.append("	   (NVL(A.SERVICE_FIXED_AMOUNT, 0) + NVL(A.SERVICE_LABOUR_AMOUNT, 0) + NVL(A.SERVICE_PART_AMOUNT, 0) + NVL(A.SERVICE_OTHER_AMOUNT, 0)) SERVICE_AMOUNT,");
		sql.append("       NVL(B.TOTAL_AMOUNT, 0) TOTAL_AMOUNT, NVL(C.FINE_SUM, 0) FINE_SUM, \n" );
		sql.append("       (NVL(B.TOTAL_AMOUNT, 0) + NVL(C.FINE_SUM, 0) + NVL(D.DAMOUNT, 0)) TAMOUNT, \n");
		sql.append("	   (A.AMOUNT_SUM) AMOUNTSUM,\n");
		sql.append("	   (A.AMOUNT_SUM - NVL(B.TOTAL_AMOUNT, 0) - NVL(C.FINE_SUM, 0) - NVL(D.DAMOUNT, 0)) AAMOUNT\n");
		/*
		sql.append("	   (A.AMOUNT_SUM + NVL(E.DECLARE_SUM1, 0) + NVL(F.DECLARE_SUM2, 0)) AMOUNTSUM,\n");
		sql.append("	   (A.AMOUNT_SUM + NVL(E.DECLARE_SUM1, 0) + NVL(F.DECLARE_SUM2, 0) - NVL(B.TOTAL_AMOUNT, 0) - NVL(C.FINE_SUM, 0) - NVL(D.DAMOUNT, 0)) AAMOUNT\n");
		*/
		sql.append("	   ,(NVL(A.APPLY_AMOUNT,0)-NVL(A.BALANCE_AMOUNT,0)) AUTHDEDUCT\n");
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE_TMP A,\n" );
		sql.append("     (\n" );
		sql.append("      SELECT DEALER_CODE,YIELDLY, SUM(TOTAL_AMOUNT) TOTAL_AMOUNT\n" );
		sql.append("      FROM TT_AS_WR_DEDUCT_BALANCE\n" );
		sql.append("      WHERE STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		sql.append("      GROUP BY DEALER_CODE,YIELDLY\n" );
		sql.append("      ) B,\n" );
		sql.append("     (\n" );
		sql.append("      SELECT DEALER_ID,YIELDLY, SUM(FINE_SUM) FINE_SUM\n" );
		sql.append("      FROM TT_AS_WR_FINE\n" );
		sql.append("      WHERE PAY_STATUS = ").append(Constant.PAY_STATUS_01).append("\n");
		sql.append("      GROUP BY DEALER_ID,YIELDLY\n" );
		sql.append("      ) C,\n" );
		sql.append("	 (\n");
		sql.append("	  SELECT DEALER_ID,YIELDLY, SUM(DEDUCT_AMOUNT) DAMOUNT\n");
		sql.append("	  FROM TT_AS_WR_ADMIN_DEDUCT\n");
		sql.append("	  WHERE DEDUCT_STATUS = ").append(Constant.ADMIN_STATUS_01).append("\n");
		sql.append("      AND CLAIMBALANCE_ID IS NULL \n");
		sql.append("	  GROUP BY DEALER_ID,YIELDLY\n");
		sql.append("	  ) D\n");
		//2010-11-04 屏蔽  现在特殊外出费用和市场工单费用已经在结算单生成时加入加入结算单中
		/*		
        sql.append("	 (\n");
		sql.append("	  SELECT DEALER_ID,YIELD, SUM(DECLARE_SUM) DECLARE_SUM1\n" );
		sql.append("      FROM TT_AS_WR_SPEFEE A\n" );
		sql.append("      WHERE A.FEE_TYPE = ").append(Constant.FEE_TYPE_01).append("\n");
		sql.append("      AND A.STATUS = ").append(Constant.SPEFEE_STATUS_06).append("\n");
		sql.append("      GROUP BY DEALER_ID,YIELD\n");
		sql.append("      ) E, \n");
		sql.append("	 (\n");
		sql.append("	  SELECT DEALER_ID,YIELD, SUM(DECLARE_SUM) DECLARE_SUM2\n" );
		sql.append("      FROM TT_AS_WR_SPEFEE A\n" );
		sql.append("      WHERE A.FEE_TYPE = ").append(Constant.FEE_TYPE_02).append("\n");
		sql.append("      AND A.STATUS = ").append(Constant.SPEFEE_STATUS_06).append("\n");
		sql.append("      GROUP BY DEALER_ID,YIELD\n");
		sql.append("      ) F \n");*/
		sql.append("WHERE A.DEALER_CODE = B.DEALER_CODE(+)\n" );
		sql.append("AND A.YIELDLY = B.YIELDLY(+)");
		sql.append("AND A.DEALER_ID = C.DEALER_ID(+)\n" );
		sql.append("AND A.YIELDLY = C.YIELDLY(+)\n" );
		sql.append("AND A.DEALER_ID = D.DEALER_ID(+)\n" );
		sql.append("AND A.YIELDLY = D.YIELDLY(+)\n" );
		/*
		sql.append("AND A.DEALER_ID = E.DEALER_ID(+)\n");
		sql.append("AND A.YIELDLY = E.YIELD(+)\n" );
		sql.append("AND A.DEALER_ID = F.DEALER_ID(+)\n");
		sql.append("AND A.YIELDLY = F.YIELD(+)\n" );
		*/
		sql.append("AND A.ID = ").append(id).append("\n");
		sql.append("ORDER BY A.CREATE_DATE DESC\n");

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	public PageResult<Map<String, Object>> kpClaimList(long balanceId,int pageSize,int curPage){
		
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT   a.ID,a.claim_no, \n" );
		sbSql.append("         NVL(A.REPAIR_TOTAL,0) AS REPAIR_TOTAL, \n");
		sbSql.append("         NVL(A.BALANCE_AMOUNT,0) AS BALANCE_AMOUNT, \n");
		sbSql.append("         (NVL(REPAIR_TOTAL,0)-NVL(BALANCE_AMOUNT,0)) AS kongkuan \n");
		sbSql.append("  FROM   tt_as_wr_application a \n");
		sbSql.append(" WHERE   EXISTS (SELECT   B.CLAIM_ID \n");
		sbSql.append("                   FROM   TR_BALANCE_CLAIM_TMP b \n");
		sbSql.append("                  WHERE   a.id = b.claim_id AND b.balance_id = ?) \n");

		List list=new ArrayList();
		list.add(balanceId);
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), list, getFunName(), pageSize, curPage);
		return ps;
	} 
	
	
	public String viewFineDecude(String dealerId,String yilile){
		/******获取奖励金额*****************************/
		StringBuffer sql= new StringBuffer();
		sql.append("select nvl(sum(nvl(f.fine_sum,0)),0) fin_num from Tt_As_Wr_Fine f\n" );
		sql.append("  where\n" );
		sql.append("        f.dealer_id = ?\n" );
		sql.append("        and f.pay_status=?\n" );
		sql.append("        and f.yieldly=?\n" );
		sql.append("        and f.fine_type=?  ");
		List list=new ArrayList();
		list.add(dealerId);
		list.add(Constant.PAY_STATUS_01);
		list.add(yilile);
		list.add(Constant.FINE_TYPE_02);
		
		/******获取扣款金额*****************************/
		StringBuffer sql2= new StringBuffer();
		sql2.append("select nvl(sum(nvl(f.fine_sum,0)),0) fin_num from Tt_As_Wr_Fine f\n" );
		sql2.append("  where\n" );
		sql2.append("        f.dealer_id = ?\n" );
		sql2.append("        and f.pay_status=?\n" );
		sql2.append("        and f.yieldly=?\n" );
		sql2.append("        and f.fine_type=?  ");
		
		List list2=new ArrayList();
		list2.add(dealerId);
		list2.add(Constant.PAY_STATUS_01);
		list2.add(yilile);
		list2.add(Constant.FINE_TYPE_01);
		
		List<Map<String, Object>> listadd = pageQuery(sql.toString(), list, getFunName());
		List<Map<String, Object>> listKou = pageQuery(sql2.toString(), list2, getFunName());
		double kAmount = ((java.math.BigDecimal)((Map)listadd.get(0)).get("FIN_NUM")).doubleValue();
		double jAmount = ((java.math.BigDecimal)((Map)listKou.get(0)).get("FIN_NUM")).doubleValue();
		
		return String.valueOf(kAmount-jAmount);
		
	}
	public List<Map<String, Object>> matchBalanceId(String balanceId){
		StringBuffer viewSql = new StringBuffer();
		viewSql.append(" select * from tr_balance_claim_tmp  tt where tt.balance_id="+balanceId+"\n");
		return this.pageQuery(viewSql.toString(), null, this.getFunName());
	}
	//zhumingwei 2011-11-01
	public PageResult<Map<String, Object>> queryDealerApplication(Map<String, String> map,Long userId,int pageSize, int curPage) {
		StringBuffer sqlStr = new StringBuffer();
		String dealerId = map.get("dealerId");
		String yieldly = map.get("yieldly");
		String CHECK_NO = map.get("CHECK_NO");
		String STATUS = map.get("STATUS");
		String BALANCE_NO = map.get("BALANCE_NO");
		String startDate = map.get("startDate");
		String endDate = map.get("endDate");
		String balanceStartDate = map.get("balanceStartDate");
		String balanceEndDate = map.get("balanceEndDate");
		String areaIds = map.get("areaIds");
		sqlStr.append("select a.sb_status,t.dealer_code,t.dealer_name,a.id,a.CHECK_NO,a.CHECK_COUNT,a.check_date,cb.yieldly,a.BALANCE_NO,cb.START_DATE,cb.END_DATE,a.STATUS,cb.BALANCE_AMOUNT from tt_as_wr_check_application a,tt_as_wr_claim_balance cb,tm_dealer t,TC_USER_REGION_RELATION j\n" );
		sqlStr.append(" WHERE 1 = 1 and t.PROVINCE_ID = J.REGION_CODE and J.USER_ID="+userId+" and cb.balance_no=a.balance_no and a.delaer_id=t.dealer_id\n" );
		sqlStr.append("  AND EXISTS (SELECT 1 FROM TM_DEALER_BUSINESS_AREA TDBA\n" );
		sqlStr.append("  WHERE TDBA.DEALER_ID = A.delaer_id AND TDBA.AREA_ID IN ("+areaIds+"))\n" );
		
		if (Utility.testString(dealerId)) {
			sqlStr.append(" and a.delaer_id="+dealerId+" \n" );
		}
		if (Utility.testString(yieldly)) {
			sqlStr.append(" and cb.yieldly="+yieldly+" \n" );
		}
		if (Utility.testString(BALANCE_NO)) {
			sqlStr.append(" and a.balance_no like '%"+BALANCE_NO+"%'");
		}
		if (Utility.testString(CHECK_NO)) {
			sqlStr.append(" and a.check_no like '%"+CHECK_NO+"%'");
		}
		if (Utility.testString(STATUS)) {
			sqlStr.append(" AND A.sb_status='" + STATUS + "' \n");
		}
		if (Utility.testString(startDate)) {
			sqlStr.append(" AND a.check_date>=to_date('" + startDate + " 00:00:00','yyyy-MM-dd HH24:MI:SS') \n");
		}
		if (Utility.testString(endDate)) {
			sqlStr.append(" AND a.check_date<=to_date('" + endDate + " 23:59:59','yyyy-MM-dd HH24:MI:SS') \n");
		}
		if (Utility.testString(balanceStartDate)) {
			sqlStr.append(" AND cb.start_date>=to_date('" + balanceStartDate + " 00:00:00','yyyy-MM-dd HH24:MI:SS') \n");
		}
		if (Utility.testString(balanceEndDate)) {
			sqlStr.append(" AND cb.end_date<=to_date('" + balanceEndDate + " 23:59:59','yyyy-MM-dd HH24:MI:SS') \n");
		}
		sqlStr.append(" ORDER BY A.ID DESC ");
		System.out.println("sqlsql=="+sqlStr.toString());
		PageResult<Map<String, Object>> ps = pageQuery(sqlStr.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//zhumingwei 2011-11-01
	public PageResult<Map<String, Object>> queryDealerApplication1(Map<String, String> map,int pageSize, int curPage) {
		StringBuffer sqlStr = new StringBuffer();
		String dealerId = map.get("dealerId");
		String BALANCE_NO = map.get("BALANCE_NO");
		String CHECK_NO = map.get("CHECK_NO");
		String STATUS = map.get("STATUS");

		String yieldly = map.get("yieldly");
		String startDate = map.get("startDate");
		String endDate = map.get("endDate");
		String balanceStartDate = map.get("balanceStartDate");
		String balanceEndDate = map.get("balanceEndDate");
		
		sqlStr.append("select a.id,a.check_no,a.check_count,a.balance_no,a.check_date,a.status,b.yieldly,b.start_date,b.end_date,b.balance_amount,a.sb_status from tt_as_wr_check_application a,tt_as_wr_claim_balance b\n" );
		sqlStr.append(" WHERE 1 = 1 and a.balance_no=b.balance_no\n" );
		
		if (Utility.testString(dealerId)) {
			sqlStr.append(" and a.delaer_id="+dealerId+" \n" );
		}
		if (Utility.testString(BALANCE_NO)) {
			sqlStr.append(" and a.balance_no like '%"+BALANCE_NO+"%'");
		}
		if (Utility.testString(CHECK_NO)) {
			sqlStr.append(" and a.check_no like '%"+CHECK_NO+"%'");
		}
		if (Utility.testString(STATUS)) {
			sqlStr.append(" AND A.status='" + STATUS + "' \n");
		}
		
		if (Utility.testString(yieldly)) {
			sqlStr.append(" AND b.yieldly='" + yieldly + "' \n");
		}
		if (Utility.testString(startDate)) {
			sqlStr.append(" AND a.check_date>=to_date('" + startDate + " 00:00:00','yyyy-MM-dd HH24:MI:SS') \n");
		}
		if (Utility.testString(endDate)) {
			sqlStr.append(" AND a.check_date<=to_date('" + endDate + " 23:59:59','yyyy-MM-dd HH24:MI:SS') \n");
		}
		if (Utility.testString(balanceStartDate)) {
			sqlStr.append(" AND B.start_date>=to_date('" + balanceStartDate + " 00:00:00','yyyy-MM-dd HH24:MI:SS') \n");
		}
		if (Utility.testString(balanceEndDate)) {
			sqlStr.append(" AND B.end_date<=to_date('" + balanceEndDate + " 23:59:59','yyyy-MM-dd HH24:MI:SS') \n");
		}
		
		sqlStr.append(" ORDER BY A.ID DESC ");
		System.out.println("sqlsql=="+sqlStr.toString());
		PageResult<Map<String, Object>> ps = pageQuery(sqlStr.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//zhumingwei 2011-11-01
	public List<Map<String, Object>> getCheckApplication(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select b.start_date,b.end_date,b.yieldly,A.balance_no,t.dealer_code, t.dealer_name, a.check_count, a.check_no,a.check_date,a.balance_no,a.id,vo.region_name,a.check_date,vo.root_org_name");
		sql.append(" from tt_as_wr_check_application A, tm_dealer t, vw_org_dealer_service vo,tt_as_wr_claim_balance b");
		sql.append(" where a.delaer_id = t.dealer_id and t.dealer_id=vo.dealer_id and A.balance_no=b.balance_no and a.id="+id+"");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//zhumingwei 2011-11-01
	public List<Map<String, Object>> getCheckApplicationDetail(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select c.vin,mg.group_code,tvm.material_code,c.yieldly,c.claim_type,c.claim_no,c.balance_amount,b.claim_id,b.status from tt_as_wr_check_detail b,tt_as_wr_application c,tm_vehicle tv,tm_vhcl_material tvm,tm_vhcl_material_group mg WHERE 1 = 1 and mg.group_id = c.model_id and c.vin=tv.vin and tv.material_id=tvm.material_id and b.claim_id=c.id and b.check_id="+id);
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	//zhumingwei 2011-12-22
	public List<Map<String, Object>> getCheckApplicationDetailOme(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select c.vin,mg.group_code,tvm.material_code,c.yieldly,c.claim_type,c.claim_no,c.balance_amount,b.claim_id,b.status from tt_as_wr_check_detail b,tt_as_wr_application c,tm_vehicle tv,tm_vhcl_material tvm,tm_vhcl_material_group mg WHERE 1 = 1 and mg.group_id=c.model_id and c.vin=tv.vin and tv.material_id=tvm.material_id and b.claim_id=c.id and b.check_id="+id);
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//zhumingwei 2011-11-08
	public List<Map<String, Object>> getCodeType(String type){
		StringBuffer sql = new StringBuffer();
		sql.append("select code_id,CODE_DESC from tc_code WHERE 1 = 1 and type="+type);
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//zhumingwei 2011-11-08
	public List<Map<String, Object>> getCodeType1(String type){
		StringBuffer sql = new StringBuffer();
		sql.append("select code_id,CODE_DESC from tc_code WHERE 1 = 1 and type="+type);
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//zhumingwei 2011-11-08
	public List<Map<String, Object>> getCodeType2(String type){
		StringBuffer sql = new StringBuffer();
		sql.append("select code_id,CODE_DESC from tc_code WHERE 1 = 1 and type="+type);
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//zhumingwei 2011-11-08
	public List<Map<String, Object>> getCodeType3(String type){
		StringBuffer sql = new StringBuffer();
		sql.append("select code_id,CODE_DESC from tc_code WHERE 1 = 1 and type="+type);
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//zhumingwei 2011-12-30
	public List<Map<String, Object>> getCodeType4(String type){
		StringBuffer sql = new StringBuffer();
		sql.append("select code_id,CODE_DESC from tc_code WHERE 1 = 1 and type="+type);
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//zhumingwei 2011-11-11
	public List<Map<String, Object>> getCheckSit(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select check_situation from tt_as_wr_auth_check_app WHERE 1 = 1 and claim_id="+id);
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//zhumingwei 2011-12-28
	public PageResult<Map<String, Object>> queryDealerAppDetail(Map<String, String> map,int pageSize, int curPage) {
		StringBuffer sqlStr = new StringBuffer();
		String dealerId = map.get("dealerId");
		String CHECK_NO = map.get("CHECK_NO");
		String BALANCE_NO = map.get("BALANCE_NO");
		String yieldly = map.get("yieldly");
		String checkDateBegin = map.get("checkDateBegin");
		String checkDateEnd = map.get("checkDateEnd");
		String authDateBegin = map.get("authDateBegin");
		String authDateEnd = map.get("authDateEnd");
		String STATUS = map.get("STATUS");
		
		String upfile = map.get("upfile");
		String sh_status = map.get("sh_status");

		sqlStr.append("select a.yieldly,a.check_no,a.check_date,a.auth_date,a.balance_no,a.start_date,a.end_date,a.status,a.claim_type,a.claim_no,a.balance_amount,a.remark,\n" );
		sqlStr.append("a.t1||'  '||a.t2||'  '||a.t3||'  '||a.t4||'  '||a.t5||'  '||a.t6||'  '||a.t7 as code1,\n" );
		sqlStr.append("a.s1||'  '||a.s2||'  '||a.s3||'  '||a.s4||'  '||a.s5 as code2,\n" );
		sqlStr.append("a.w1||'  '||a.w2||'  '||a.w3 as code3,\n" );
		sqlStr.append("a.q1||'  '||a.q2||'  '||a.q3||'  '||a.q4||'  '||a.q5||'  '||a.q6||'  '||a.q7||'  '||a.q8||'  '||a.q9 as code4,a.h1 code5\n" );
		
		sqlStr.append("from (\n" );
		
		sqlStr.append("select b.yieldly,a.check_no,a.check_date,a.auth_date,a.balance_no,b.start_date,b.end_date,a.status,c.claim_type,c.claim_no,c.balance_amount,d.remark,\n" );
		sqlStr.append("max(decode(p.check_situation,'90991001',tc.code_desc)) as t1,\n" );
		sqlStr.append("max(decode(p.check_situation,'90991002',tc.code_desc)) as t2,\n" );
		sqlStr.append("max(decode(p.check_situation,'90991003',tc.code_desc)) as t3,\n" );
		sqlStr.append("max(decode(p.check_situation,'90991004',tc.code_desc)) as t4,\n" );
		sqlStr.append("max(decode(p.check_situation,'90991005',tc.code_desc)) as t5,\n" );
		sqlStr.append("max(decode(p.check_situation,'90991006',tc.code_desc)) as t6,\n" );
		sqlStr.append("max(decode(p.check_situation,'90991007',tc.code_desc)) as t7,\n" );
		sqlStr.append("max(decode(p.check_situation,'91001001',tc.code_desc)) as s1,\n" );
		sqlStr.append("max(decode(p.check_situation,'91001002',tc.code_desc)) as s2,\n" );
		sqlStr.append("max(decode(p.check_situation,'91001003',tc.code_desc)) as s3,\n" );
		sqlStr.append("max(decode(p.check_situation,'91001004',tc.code_desc)) as s4,\n" );
		sqlStr.append("max(decode(p.check_situation,'91001005',tc.code_desc)) as s5,\n" );
		sqlStr.append("max(decode(p.check_situation,'91011001',tc.code_desc)) as w1,\n" );
		sqlStr.append("max(decode(p.check_situation,'91011002',tc.code_desc)) as w2,\n" );
		sqlStr.append("max(decode(p.check_situation,'91011003',tc.code_desc)) as w3,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021001',tc.code_desc)) as q1,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021002',tc.code_desc)) as q2,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021003',tc.code_desc)) as q3,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021004',tc.code_desc)) as q4,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021005',tc.code_desc)) as q5,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021006',tc.code_desc)) as q6,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021007',tc.code_desc)) as q7,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021008',tc.code_desc)) as q8,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021009',tc.code_desc)) as q9,\n" );
		sqlStr.append("max(decode(p.check_situation,'91031001',tc.code_desc)) as h1\n" );
		sqlStr.append("from tt_as_wr_check_application a,tt_as_wr_claim_balance     b,tt_as_wr_application       c,tt_as_wr_check_detail      d,tt_as_wr_auth_check_app    p,tc_code                    tc\n" );
		sqlStr.append("WHERE 1 = 1 and a.balance_no = b.balance_no and a.id = d.check_id and c.id = d.claim_id and d.claim_id = p.claim_id(+) and tc.code_id(+) = p.check_situation\n" );
		
		if (Utility.testString(dealerId)) {
			sqlStr.append(" and a.delaer_id="+dealerId+" \n" );
		}
		if (Utility.testString(CHECK_NO)) {
			sqlStr.append(" and a.check_no like '%"+CHECK_NO+"%'");
		}
		if (Utility.testString(BALANCE_NO)) {
			sqlStr.append(" and a.balance_no like '%"+BALANCE_NO+"%'");
		}
		if (Utility.testString(yieldly)) {
			sqlStr.append(" AND b.yieldly='" + yieldly + "' \n");
		}
		if (Utility.testString(checkDateBegin)) {
			sqlStr.append(" AND a.check_date>=to_date('" + checkDateBegin + " 00:00:00','yyyy-MM-dd HH24:MI:SS') \n");
		}
		if (Utility.testString(checkDateEnd)) {
			sqlStr.append(" AND a.check_date<=to_date('" + checkDateEnd + " 23:59:59','yyyy-MM-dd HH24:MI:SS') \n");
		}
		if (Utility.testString(authDateBegin)) {
			sqlStr.append(" AND a.auth_date>=to_date('" + authDateBegin + " 00:00:00','yyyy-MM-dd HH24:MI:SS') \n");
		}
		if (Utility.testString(authDateEnd)) {
			sqlStr.append(" AND a.auth_date<=to_date('" + authDateEnd + " 23:59:59','yyyy-MM-dd HH24:MI:SS') \n");
		}
		if (Utility.testString(STATUS)) {
			sqlStr.append(" AND A.status='" + STATUS + "' \n");
		}
		if (Utility.testString(upfile)) {
			sqlStr.append(" AND d.upfile_status='" + upfile + "' \n");
		}
		if (Utility.testString(sh_status)) {
			sqlStr.append(" AND d.status='" + sh_status + "' \n");
		}
		sqlStr.append("group by b.yieldly,a.check_no,a.check_date,a.auth_date,a.balance_no,b.start_date,b.end_date,a.status,c.claim_type,c.claim_no,c.balance_amount,d.remark) a\n" );
		System.out.println("sqlsql=="+sqlStr.toString());
		PageResult<Map<String, Object>> ps = pageQuery(sqlStr.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//zhumingwei 2011-12-28
	public PageResult<Map<String, Object>> queryDealerAppDetailOme(Map<String, String> map,int pageSize, int curPage) {
		StringBuffer sqlStr = new StringBuffer();
		String dealerId = map.get("dealerId");
		String orgId = map.get("orgId");
		String CHECK_NO = map.get("CHECK_NO");
		String STATUS = map.get("STATUS");
		String BALANCE_NO = map.get("BALANCE_NO");
		String yieldly = map.get("yieldly");
		String checkDateBegin = map.get("checkDateBegin");
		String checkDateEnd = map.get("checkDateEnd");
		String authDateBegin = map.get("authDateBegin");
		String authDateEnd = map.get("authDateEnd");
		String province = map.get("province");
		String upfile = map.get("upfile");
		String sh_status = map.get("sh_status");

		sqlStr.append("select a.yieldly,a.check_no,a.check_date,a.auth_date,a.balance_no,a.start_date,a.end_date,a.status,a.claim_type,a.claim_no,a.balance_amount,a.root_org_name,a.region_name,a.dealer_code,a.dealer_name,a.remark,\n" );
		sqlStr.append("a.t1||'  '||a.t2||'  '||a.t3||'  '||a.t4||'  '||a.t5||'  '||a.t6||'  '||a.t7 as code1,\n" );
		sqlStr.append("a.s1||'  '||a.s2||'  '||a.s3||'  '||a.s4||'  '||a.s5 as code2,\n" );
		sqlStr.append("a.w1||'  '||a.w2||'  '||a.w3 as code3,\n" );
		sqlStr.append("a.q1||'  '||a.q2||'  '||a.q3||'  '||a.q4||'  '||a.q5||'  '||a.q6||'  '||a.q7||'  '||a.q8||'  '||a.q9 as code4,a.h1 code5\n" );
		
		sqlStr.append("from (\n" );
		
		sqlStr.append("select b.yieldly,a.check_no,a.check_date,a.auth_date,a.balance_no,b.start_date,b.end_date,a.status,c.claim_type,c.claim_no,c.balance_amount,vw.root_org_name,r.region_name,td.dealer_code,td.dealer_name,d.remark,\n" );
		sqlStr.append("max(decode(p.check_situation,'90991001',tc.code_desc)) as t1,\n" );
		sqlStr.append("max(decode(p.check_situation,'90991002',tc.code_desc)) as t2,\n" );
		sqlStr.append("max(decode(p.check_situation,'90991003',tc.code_desc)) as t3,\n" );
		sqlStr.append("max(decode(p.check_situation,'90991004',tc.code_desc)) as t4,\n" );
		sqlStr.append("max(decode(p.check_situation,'90991005',tc.code_desc)) as t5,\n" );
		sqlStr.append("max(decode(p.check_situation,'90991006',tc.code_desc)) as t6,\n" );
		sqlStr.append("max(decode(p.check_situation,'90991007',tc.code_desc)) as t7,\n" );
		sqlStr.append("max(decode(p.check_situation,'91001001',tc.code_desc)) as s1,\n" );
		sqlStr.append("max(decode(p.check_situation,'91001002',tc.code_desc)) as s2,\n" );
		sqlStr.append("max(decode(p.check_situation,'91001003',tc.code_desc)) as s3,\n" );
		sqlStr.append("max(decode(p.check_situation,'91001004',tc.code_desc)) as s4,\n" );
		sqlStr.append("max(decode(p.check_situation,'91001005',tc.code_desc)) as s5,\n" );
		sqlStr.append("max(decode(p.check_situation,'91011001',tc.code_desc)) as w1,\n" );
		sqlStr.append("max(decode(p.check_situation,'91011002',tc.code_desc)) as w2,\n" );
		sqlStr.append("max(decode(p.check_situation,'91011003',tc.code_desc)) as w3,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021001',tc.code_desc)) as q1,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021002',tc.code_desc)) as q2,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021003',tc.code_desc)) as q3,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021004',tc.code_desc)) as q4,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021005',tc.code_desc)) as q5,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021006',tc.code_desc)) as q6,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021007',tc.code_desc)) as q7,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021008',tc.code_desc)) as q8,\n" );
		sqlStr.append("max(decode(p.check_situation,'91021009',tc.code_desc)) as q9,\n" );
		sqlStr.append("max(decode(p.check_situation,'91031001',tc.code_desc)) as h1\n" );
		sqlStr.append("from tt_as_wr_check_application a,tt_as_wr_claim_balance     b,tt_as_wr_application       c,tt_as_wr_check_detail      d,tt_as_wr_auth_check_app    p,tc_code                    tc,vw_org_dealer_service      vw,tm_dealer                  td,tm_region                  r\n" );
		sqlStr.append("WHERE 1 = 1 and td.dealer_id=a.delaer_id and td.province_id=r.region_code and vw.dealer_id=a.delaer_id and a.balance_no = b.balance_no and a.id = d.check_id and c.id = d.claim_id and d.claim_id = p.claim_id(+) and tc.code_id(+) = p.check_situation\n" );
		
		if (Utility.testString(dealerId)) {
			sqlStr.append(" and a.delaer_id="+dealerId+" \n" );
		}
		if (Utility.testString(orgId)) {
			sqlStr.append(" and vw.root_org_id in("+orgId+") \n" );
		}
		if (Utility.testString(CHECK_NO)) {
			sqlStr.append(" and a.check_no like '%"+CHECK_NO+"%'");
		}
		if (Utility.testString(BALANCE_NO)) {
			sqlStr.append(" and a.balance_no like '%"+BALANCE_NO+"%'");
		}
		if (Utility.testString(yieldly)) {
			sqlStr.append(" AND b.yieldly='" + yieldly + "' \n");
		}
		if (Utility.testString(checkDateBegin)) {
			sqlStr.append(" AND a.check_date>=to_date('" + checkDateBegin + " 00:00:00','yyyy-MM-dd HH24:MI:SS') \n");
		}
		if (Utility.testString(checkDateEnd)) {
			sqlStr.append(" AND a.check_date<=to_date('" + checkDateEnd + " 23:59:59','yyyy-MM-dd HH24:MI:SS') \n");
		}
		if (Utility.testString(authDateBegin)) {
			sqlStr.append(" AND a.auth_date>=to_date('" + authDateBegin + " 00:00:00','yyyy-MM-dd HH24:MI:SS') \n");
		}
		if (Utility.testString(authDateEnd)) {
			sqlStr.append(" AND a.auth_date<=to_date('" + authDateEnd + " 23:59:59','yyyy-MM-dd HH24:MI:SS') \n");
		}
		if (Utility.testString(STATUS)) {
			sqlStr.append(" AND A.status='" + STATUS + "' \n");
		}
		if (Utility.testString(province)) {
			sqlStr.append(" AND r.region_code='" + province + "' \n");
		}
		if (Utility.testString(upfile)) {
			sqlStr.append(" AND d.upfile_status='" + upfile + "' \n");
		}
		if (Utility.testString(sh_status)) {
			sqlStr.append(" AND d.status='" + sh_status + "' \n");
		}
		sqlStr.append("group by b.yieldly,a.check_no,a.check_date,a.auth_date,a.balance_no,b.start_date,b.end_date,a.status,c.claim_type,c.claim_no,c.balance_amount,vw.root_org_name,r.region_name,td.dealer_code,td.dealer_name,d.remark) a\n" );
		System.out.println("sqlsql=="+sqlStr.toString());
		PageResult<Map<String, Object>> ps = pageQuery(sqlStr.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	//zhumingwei20120521
	public List<Map<String, Object>> getDetail(Long id) {
		String sql="select a.upfile_status,a.id,b.claim_no,a.check_id from Tt_As_Wr_Check_Detail a,tt_as_wr_application b where a.claim_id=b.id and a.check_id="+id+" order by a.id desc";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	
	//zhumingwei 2012-5-30
	public List<Map<String, Object>> ztAudit(Map<String, String> map){
		StringBuffer sqlStr = new StringBuffer();
		String id = map.get("id");
		
		sqlStr.append("select b.claim_id,c.vin,mg.group_code,tvm.material_code,c.yieldly,c.claim_type,c.claim_no,c.balance_amount,b.status,b.id from tt_as_wr_check_detail b,tt_as_wr_application c,tm_vehicle tv,tm_vhcl_material tvm,tm_vhcl_material_group mg WHERE 1 = 1 and mg.group_id=c.model_id and c.vin=tv.vin and tv.material_id=tvm.material_id and b.claim_id=c.id and b.check_id="+id+"");
		sqlStr.append(" ORDER BY b.ID DESC ");
		System.out.println("sqlsql=="+sqlStr.toString());
		List<Map<String, Object>> ps = pageQuery(sqlStr.toString(), null, getFunName());
		return ps;
	}
	//zhumingwei 2012-5-31
	public List<Map<String, Object>> getClaim(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select b.upfile_status,b.claim_id,c.vin,mg.group_code,tvm.material_code,c.yieldly,c.claim_type,c.claim_no,c.balance_amount,b.claim_id,b.status from tt_as_wr_check_detail b,tt_as_wr_application c,tm_vehicle tv,tm_vhcl_material tvm,tm_vhcl_material_group mg WHERE 1 = 1 and mg.group_id=c.model_id and c.vin=tv.vin and tv.material_id=tvm.material_id and b.claim_id=c.id and b.claim_id="+id);
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//zhumingwei 2012-5-31
	public List<FsFileuploadPO> queryAttachFileInfo(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A ");
		sql.append(" WHERE A.YWZJ='"+id+"'");
		List<FsFileuploadPO> ls= select (FsFileuploadPO.class,sql.toString(),null);
		return ls;
	}
	
	//zhumingwei 2013-01-08
	//zhumingwei 2012-12-25
	public String getCount1(String dealerId,String yieldly){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.status from tt_as_dealer_type a where a.dealer_id="+dealerId+"\n");
		sql.append("and a.yieldly="+yieldly+"\n" );
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list.get(0).get("STATUS").toString();
	}
	/**
	 * 查出反索赔表的材料费，劳务费，总经额
	 * @param dealerId
	 * @param startTime
	 * @param endTime
	 */
	public List<Map<String, Object>> selectCliam(String dealerId, String startTime, String endTime) {
		String sql="select nvl(sum(t.balance_amount),0) balance_amount, nvl(sum(t.balance_part_amount),0) part_amount, nvl(sum(t.balance_labour_amount),0) labour_amount from tt_as_wr_application_counter t where t.dealer_id="+dealerId+" and is_invoice = 0  and t.REPORT_DATE>=to_date('"+startTime+"','yyyy-MM-dd HH24:MI:SS') and t.REPORT_DATE<=(to_date('"+endTime+"','yyyy-MM-dd HH24:MI:SS'))";
		List<Map<String, Object>> list = pageQuery(sql, null, getFunName());
		return list;
	}
	/**
	 * 查询出辅料费的
	 * @param dealerId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> accessories_price(String dealerId,
			String startTime, String endTime) {
		String sql="select nvl(sum(t.accessories_price),0) accessories_price from tt_as_wr_application t where t.status=10791007 and is_invoice = 0  and  t.dealer_id="+dealerId+" and t.REPORT_DATE>=to_date('"+startTime+"','yyyy-MM-dd HH24:MI:SS') and t.REPORT_DATE<=(to_date('"+endTime+"','yyyy-MM-dd HH24:MI:SS'))";
		List<Map<String, Object>> list = pageQuery(sql, null, getFunName());
		return list;
	}
	/**
	 * 查询出切换件的工时费
	 * @param dealerId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> qhprice(String dealerId, String startTime,
			String endTime) {
		String sql="select nvl(sum(t.balance_labour_amount),0) balance_labour_amount from tt_as_wr_application t where t.status=10791007 and t.claim_type=10661006 and  t.dealer_id="+dealerId+" and t.REPORT_DATE>=to_date('"+startTime+"','yyyy-MM-dd HH24:MI:SS') and is_invoice = 0 and t.REPORT_DATE<=(to_date('"+endTime+"','yyyy-MM-dd HH24:MI:SS'))";
		List<Map<String, Object>> list = pageQuery(sql, null, getFunName());
		return list;
	}
	/**
	 * 查询所有开票的
	 * @param request
	 * @param currDealerId
	 * @param pageSize
	 * @param currPage
	 * @return
	 */
	/*public PageResult<Map<String, Object>> delaerKpAllQuery(RequestWrapper request, String currDealerId, Integer pageSize,Integer currPage)  {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*\n" );
		sb.append("  from (select M.*,\n" );
		sb.append("               (select count(1)\n" );
		sb.append("                  from tt_as_wr_old_returned   a,\n" );
		sb.append("                       tt_as_wr_returned_order b,\n" );
		sb.append("                       tr_return_logistics     c,\n" );
		sb.append("                       tm_dealer               d\n" );
		sb.append("                 where b.id = c.return_id\n" );
		sb.append("                   and a.id = c.logictis_id\n" );
		sb.append("                   and a.dealer_id = d.dealer_id\n" );
		sb.append("                   and a.dealer_id = m.dealer_id\n" );
		sb.append("                   and a.is_invoice = 0\n" );
		sb.append("                   and a.auth_price is not null\n" );
		sb.append("                   and a.status in (10811003, 10811004, 10811005, 10811006)\n" );
		sb.append("                   and a.status not in (10811007)\n" );
		sb.append("                   and (a.return_type = 10731001 or a.return_type = 10731002)) as countAll\n" );
		sb.append("          from (SELECT B.Dealer_Id,\n" );
		sb.append("                       B.DEALER_CODE,\n" );
		sb.append("                       A.CLAIM_IS,\n" );
		sb.append("                       B.DEALER_NAME,\n" );
		sb.append("                       to_char(add_months(to_date(A.REPORT_DATE, 'yyyy-mm-dd'),\n" );
		sb.append("                                          -1),\n" );
		sb.append("                               'yyyy-mm') || '-26' SDATE,\n" );
		sb.append("                       A.REPORT_DATE EDATE\n" );
		sb.append("                  from (SELECT t.DEALER_ID DEALER_ID,\n" );
		sb.append("                               version REPORT_DATE,\n" );
		sb.append("                               max(t.STATUS) STATUS_01,\n" );
		sb.append("                               min(t.STATUS) STATUS_02,\n" );
		sb.append("                               max(t.is_invoice) is_invoice_01,\n" );
		sb.append("                               min(t.is_invoice) is_invoice_02,\n" );
		sb.append("                               decode(max(case\n" );
		sb.append("                                            when t.CLAIM_TYPE in (10661011, 10661002) then\n" );
		sb.append("                                             0\n" );
		sb.append("                                            else\n" );
		sb.append("                                             1\n" );
		sb.append("                                          end),\n" );
		sb.append("                                      1,\n" );
		sb.append("                                      '有',\n" );
		sb.append("                                      '无') CLAIM_IS\n" );
		sb.append("                          from (SELECT DEALER_ID DEALER_ID,\n" );
		sb.append("                                       case\n" );
		sb.append("                                         when to_number(to_char(t.report_date,\n" );
		sb.append("                                                                'dd')) < = 25 then\n" );
		sb.append("                                          to_char(t.report_date, 'yyyy-mm') || +\n" );
		sb.append("                                          '-25'\n" );
		sb.append("                                         else\n" );
		sb.append("                                          to_char(trunc(add_months(t.report_date,\n" );
		sb.append("                                                                   1)),\n" );
		sb.append("                                                  'yyyy-mm') || + '-25'\n" );
		sb.append("                                       end version,\n" );
		sb.append("                                       t.STATUS,\n" );
		sb.append("                                       t.is_invoice,\n" );
		sb.append("                                       t.claim_type\n" );
		sb.append("                                  from TT_AS_WR_APPLICATION t\n" );
		sb.append("                                 where t.IS_IMPORT = 10041002\n" );
		sb.append("                                   and t.report_date >\n" );
		sb.append("                                       to_date('2014-12-31', 'yyyy-mm-dd')\n" );
		sb.append("                                   and t.report_date <\n" );
		sb.append("                                       to_date(to_char(sysdate, 'yyyy-mm') ||\n" );
		sb.append("                                               '-26',\n" );
		sb.append("                                               'yyyy-mm-dd')) t\n" );
		sb.append("                         group by t.version, t.dealer_id) A,\n" );
		sb.append("                       TM_DEALER B\n" );
		sb.append("                 where A.DEALER_ID = B.DEALER_ID\n" );
		sb.append("                   and A.STATUS_01 != 10791008\n" );
		sb.append("                   and A.is_invoice_02 = 0\n" );
		DaoFactory.getsql(sb, "B.DEALER_CODE", DaoFactory.getParam(request, "dealerCode"), 2);
		DaoFactory.getsql(sb, "B.DEALER_NAME", DaoFactory.getParam(request, "dealerName"), 2);
		sb.append("                   and (A.STATUS_02 = 10791005 or A.STATUS_02 = 10791007)) M\n" );
		sb.append("         where to_date(M.EDATE, 'yyyy-mm-dd') < sysdate - 1\n" );
		sb.append("           and m.DEALER_CODE <> '802399-F') t\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and t.countAll = 0");
		return pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
	}*/
	public PageResult<Map<String, Object>> delaerKpAllQuery(RequestWrapper request, String currDealerId, Integer pageSize,Integer currPage)  {
	StringBuffer sql= new StringBuffer();
	sql.append("select t.id,\n" );
	sql.append("       t.dealer_id,\n" );
	sql.append("       d.dealer_code,\n" );
	sql.append("       d.dealer_name,\n" );
	sql.append("       d.dealer_shortname,\n" );
	sql.append("       to_char(t.wr_start_date,'yyyy-mm-dd') as SDATE,\n" );
	sql.append("       to_char(t.return_end_date,'yyyy-mm-dd') as EDATE,\n" );
	sql.append("       decode(t.part_amount,0,'无','有') as CLAIM_IS\n" );
	sql.append("  from tt_as_wr_returned_order t\n" );
	sql.append("  left join tm_dealer d\n" );
	sql.append("    on t.dealer_id = d.dealer_id\n" );
	sql.append(" where t.status = 10811005\n" );
	sql.append("   and t.IS_BILL = 10041002");
	
	return pageQuery(sql.toString(), null,getFunName(), pageSize, currPage);
	}
	public List<Map<String, Object>> seClaimId(String dealerId,String returnId,String endBalanceDate,String CON_END_DAY)  {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT wac.id\n" );
		sql.append("  from TT_AS_WR_APPLICATION_CLAIM wac\n" );
		sql.append(" where (wac.REPAIR_TYPE in\n" );
		sql.append("       (11441004, 11441008) or\n" );
		sql.append("       (wac.REPAIR_TYPE = 11441005 and wac.activity_type != 96281001))\n" );
		sql.append("   and to_date(to_char(wac.AUTH_AUDIT_DATE, 'yyyy-mm-dd'), 'yyyy-mm-dd') <=\n" );
		sql.append("       TO_DATE('"+CON_END_DAY+"','YYYY-MM-DD')\n" );
		sql.append("   and wac.STATUS in (19991005)\n" );
		sql.append("   and wac.is_bill=10041002\n" );
		sql.append("   and to_date(to_char(wac.AUTH_AUDIT_DATE, 'yyyy-mm-dd'), 'yyyy-mm-dd') >=\n" );
		sql.append("       TO_DATE('"+endBalanceDate+"','YYYY-MM-DD')\n" );
		sql.append("   and wac.dealer_id="+dealerId+"\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("   union all\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("  select distinct od.claim_id as id from TT_AS_WR_RETURNED_ORDER o\n" );
		sql.append("          inner join Tt_As_Wr_Returned_Order_Detail od\n" );
		sql.append("          on o.id=od.return_id\n" );
		sql.append("          where o.dealer_id="+dealerId+"\n" );
		sql.append("          and o.status=10811005\n" );
		sql.append("          and o.id="+returnId+"\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("");
	
	return pageQuery(sql.toString(), null,getFunName());
	}
	/**
	 * 算这个月结算的钱是否有误差
	 * @param request
	 * @return
	 */
	public Double dealerCheckMoney(RequestWrapper request) {
		String start_date = DaoFactory.getParam(request, "endBalanceDate");
		String end_date = DaoFactory.getParam(request, "CON_END_DAY");
		String dealerId = DaoFactory.getParam(request, "dealerId");
		StringBuffer sb= new StringBuffer();
		sb.append("select nvl((sum(nvl(a.balance_amount,0)) - sum(nvl(a.balance_labour_amount,0)) -\n" );
		sb.append("       sum(nvl(a.balance_part_amount,0)) - sum(nvl(a.balance_netitem_amount,0)) - sum(nvl(a.winter_money,0))-\n" );
		sb.append("       sum(nvl(a.accessories_price,0)) - sum(nvl(a.compensation_money,0))),0) as result\n" );
		sb.append("  from Tt_As_Wr_Application a where 1=1 \n" );
		sb.append("   and a.status >= 10791007\n" );
		sb.append("   and a.is_import = 10041002 \n");
		DaoFactory.getsql(sb, "a.report_date", start_date, 3);
		DaoFactory.getsql(sb, "a.report_date", end_date, 4);
		DaoFactory.getsql(sb, "a.dealer_id", dealerId, 1);
		Map map = this.pageQueryMap(sb.toString(), null, getFunName());
		BigDecimal result = (BigDecimal) map.get("RESULT");
		return result.doubleValue(); 
	}

	public List gettickets(RequestWrapper request) {
		String dealerid = request.getParamValue("dealerId");//经销商ID
		String endbalancedate = request.getParamValue("endBalanceDate");//数据库结算终止时间
		String conendday = request.getParamValue("CON_END_DAY");//现在结算终止时间
		StringBuffer sql= new StringBuffer();
		sql.append("select nvl(a.auth_price,0) as auth_price, b.wr_start_date, to_char(b.return_end_date,'yyyy-mm-dd') return_end_date,(b.wr_start_date||'至'||to_char(b.return_end_date, 'yyyy-mm-dd') ) return_date, a.return_no, d.dealer_code,a.id \n" );
		sql.append("  from tt_as_wr_old_returned   a,\n" );
		sql.append("       tt_as_wr_returned_order b,\n" );
		sql.append("       tr_return_logistics     c,\n" );
		sql.append("       tm_dealer               d\n" );
		sql.append(" where b.id = c.return_id\n" );
		sql.append("   and a.id = c.logictis_id\n" );
		sql.append("   and a.dealer_id = d.dealer_id\n" );
		sql.append("   and a.dealer_id = "+dealerid+"\n" );
		sql.append(" and a.is_invoice = 0 and a.auth_price is not null  and a.status>=10811003 and (a.return_type = 10731001 ");
		sql.append(" or a.return_type = 10731002 ");
		sql.append("   \n" );
		sql.append("       \n" );
		sql.append("   )");
		List  tisList= pageQuery(sql.toString(), null, getFunName());
		return tisList;
	}
	/**
	 * 通过经销商id去取二次入库后补偿给服务站的钱
	 * @param dealerId
	 * @param remark 
	 * @return
	 */
	public List<Map<String, Object>> findcompensationBydealer(String dealerId, String remark) {
		String sql="";
		if("".equals(remark)){
			 sql="select nvl(sum(nvl(d.amount,0)),0) as amount from tt_as_second_in_store_detail d where 1=1 and d.balance_no is null and  d.dealer_id='"+dealerId+"'";
		}else{
			 sql="select nvl(sum(nvl(d.amount,0)),0) as amount from tt_as_second_in_store_detail d where 1=1 and d.balance_no='"+remark+"' and  d.dealer_id='"+dealerId+"'";
		}
		List<Map<String, Object>>  list= pageQuery(sql, null, getFunName());
		return list;
	}
	/**
	 * 查询服务站操作人操作时间记录
	 * @param request
	 * @param currPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> dealerKpRecordData(RequestWrapper request, Integer currPage, Integer pageSize) {
		String balance_no = DaoFactory.getParam(request, "balance_no");
		String dealerId = DaoFactory.getParam(request, "dealerId");
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT D.*,TM.DEALER_CODE,TM.DEALER_SHORTNAME FROM TT_AS_DEALER_CLICK_RECORD D ,TM_DEALER TM WHERE D.DEARID=TM.DEALER_ID");
		DaoFactory.getsql(sb, "D.BALANCE_NO", balance_no, 2);
		DaoFactory.getsql(sb, "TM.DEALER_ID", dealerId, 6);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	public Map<String, Object> queryDealerMes(String FK_DEALER_ID)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT t.SPY_MAN,\n" );
		sql.append("       t.PHONE,\n" );
		sql.append("       t.DEALER_CODE,\n" );
		sql.append("       a.SER_MANAGER_NAME,\n" );
		sql.append("       a.FINANCE_MANAGER_NAME,t.DEALER_NAME,\n" );
		sql.append("       t.taxpayer_nature\n" );
		sql.append("  from TM_DEALER t, TM_DEALER_DETAIL a  where t.DEALER_ID = a.FK_DEALER_ID  and t.DEALER_ID = " + FK_DEALER_ID);
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		if(list.size() >  0 )
		{
			return list.get(0);
		}
		return  null;

	}
	
	
	public Map<String, Object> queryApption(String FK_DEALER_ID,String REMARK)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT sum(decode(A.CLAIM_TYPE, 10661002, 1, 0)) CLAIM_TYPE_02,  count(A.id) countSUM,  \n" );
		sql.append("       sum(decode(A.CLAIM_TYPE, 10661002, A.BALANCE_AMOUNT, 0)) CLAIM_TYPE_AMOUNT_02,\n" );
		sql.append("       sum(decode(A.CLAIM_TYPE, 10661011, 1, 0)) CLAIM_TYPE_11,\n" );
		sql.append("       sum(decode(A.CLAIM_TYPE, 10661011, A.BALANCE_AMOUNT, 0)) CLAIM_TYPE_AMOUNT_11,\n" );
		sql.append("       sum(decode(A.CLAIM_TYPE, 10661006, 1, 0)) CLAIM_TYPE_06,\n" );
		sql.append("    sum(decode(A.CLAIM_TYPE, 10661002,A.FREE_LABOUR_AMOUNT, 0)) CLAIM_TYPE_AMOUNT_FREE_02 ,");
		sql.append("       sum(decode(A.CLAIM_TYPE, 10661006, A.BALANCE_AMOUNT, 0)) CLAIM_TYPE_AMOUNT_06,\n" );
		sql.append("      sum( case\n" );
		sql.append("         when A.CLAIM_TYPE not in (10661002, 10661011, 10661006) then\n" );
		sql.append("          1\n" );
		sql.append("         else\n" );
		sql.append("          0\n" );
		sql.append("       end )CLAIM_TYPE_01,\n" );
		sql.append("       sum(case\n" );
		sql.append("         when A.CLAIM_TYPE not in (10661002, 10661011, 10661006) then\n" );
		sql.append("          A.BALANCE_AMOUNT\n" );
		sql.append("         else\n" );
		sql.append("          0\n" );
		sql.append("       end) CLAIM_TYPE_AMOUNT_01,\n" );
		sql.append("       (select count(1)  from  tt_as_wr_special_apply   sp where sp.dealer_id=A.DEALER_ID  ) SPECIL_TYPE_01,\n" );
		sql.append("        (select count(1) from tt_as_wr_application_counter ac where ac.dealer_id=A.DEALER_ID and ac.balance_no=a.balance_no) COUNTER_TYPE_01\n" );
		sql.append("  from TT_AS_WR_APPLICATION A \n" );
		sql.append(" where A.DEALER_ID = \n" +FK_DEALER_ID );
		sql.append("   and A.BALANCE_NO = '"+REMARK+"' \n" );
		sql.append("  and A.status!=10791005  GROUP by A.DEALER_ID,A.BALANCE_NO");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		if(list.size() >  0 )
		{
			return list.get(0);
		}
		return  null;


	}
	
	
	public List<Map<String, Object>> DealerUnitKpXiao(String CHECK_TICKETS_DATE_S,String CHECK_TICKETS_DATE,double sl,double cs )
	{
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("select * from (\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("select c.part_code,c.part_name,count(c.balance_quantity) balance_quantity,\n" );
		sql.append(" round( max(c.price)*"+cs+"/"+sl+",8) price,\n" );
		sql.append("round(sum(c.balance_amount)*"+cs+"  /"+sl+",2) balance_amount,\n" );
		sql.append("( round(sum(c.balance_amount)*"+cs+"    ,2)  - round(sum(c.balance_amount) *"+cs+" /"+sl+",2)  ) s_balance_amount\n" );
		sql.append("from tt_as_wr_application a , tt_as_wr_partsitem c\n" );
		sql.append("where  c.id = a.id\n" );
		sql.append(" and  a.status != 10791005 and a.status != 10791006 and  a.claim_type !=10661002 and  a.status != 10791005\n" );
		sql.append("and a.balance_no in ( select t.balance_oder from tt_as_payment t where t.TRANSFER_TICKETS_DATE >= to_date('"+CHECK_TICKETS_DATE_S+"','yyyy-mm-dd') and  t.TRANSFER_TICKETS_DATE < to_date('"+CHECK_TICKETS_DATE+"','yyyy-mm-dd')+1  ) and c.balance_amount > 0\n" );
		sql.append("group by c.part_code,c.part_name\n" );
		sql.append("\n" );
		sql.append("union all\n" );
		sql.append("\n" );
		sql.append("select b.WORKHOUR_CODE,b.WORKHOUR_NAME,count(1),\n" );
		sql.append("round(max(b.price)*"+cs+" /"+sl+",8),\n" );
		sql.append("round(sum(b.price) *"+cs+"/"+sl+",2),\n" );
		sql.append(" round(sum(b.price)*"+cs+"  ,2) - round(sum(b.price) *"+cs+"/"+sl+",2)\n" );
		sql.append("from tt_as_wr_application a ,tt_claim_accessory_dtl b\n" );
		sql.append("where a.claim_no = b.claim_no\n" );
		sql.append(" and  a.status != 10791005 and a.status != 10791006 and a.claim_type !=10661002 and  a.status != 10791005\n" );
		sql.append("and a.balance_no in (select t.balance_oder from tt_as_payment t where t.TRANSFER_TICKETS_DATE >= to_date('"+CHECK_TICKETS_DATE_S+"','yyyy-mm-dd') and  t.TRANSFER_TICKETS_DATE < to_date('"+CHECK_TICKETS_DATE+"','yyyy-mm-dd')+1)  and b.price > 0\n" );
		sql.append("group by b.workhour_code,b.workhour_name  ) a\n" );
		
		
		sql.append("");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;



	}
	
	
	
	public List<Map<String, Object>> AppprintXiao(String BALANCE_ODER,double sl,double cs )
	{
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("select * from (\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("select c.part_code,c.part_name,count(c.balance_quantity) balance_quantity,\n" );
		sql.append(" round( max(c.price)*"+cs+"/"+sl+",8) price,\n" );
		sql.append("round(sum(c.balance_amount)*"+cs+"  /"+sl+",2) balance_amount,\n" );
		sql.append("( round(sum(c.balance_amount)*"+cs+"    ,2)  - round(sum(c.balance_amount) *"+cs+" /"+sl+",2)  ) s_balance_amount\n" );
		sql.append("from tt_as_wr_application a , tt_as_wr_partsitem c\n" );
		sql.append("where  c.id = a.id\n" );
		sql.append(" and  a.claim_type !=10661002 and  a.status != 10791005\n" );
		sql.append("and a.balance_no = '"+BALANCE_ODER+"' and c.balance_amount > 0\n" );
		sql.append("group by c.part_code,c.part_name\n" );
		sql.append("\n" );
		sql.append("union all\n" );
		sql.append("\n" );
		sql.append("select b.WORKHOUR_CODE,b.WORKHOUR_NAME,count(1),\n" );
		sql.append("round(max(b.price)*"+cs+" /"+sl+",8),\n" );
		sql.append("round(sum(b.price) *"+cs+"/"+sl+",2),\n" );
		sql.append(" round(sum(b.price)*"+cs+"  ,2) - round(sum(b.price) *"+cs+"/"+sl+",2)\n" );
		sql.append("from tt_as_wr_application a ,tt_claim_accessory_dtl b\n" );
		sql.append("where a.claim_no = b.claim_no\n" );
		sql.append("and a.claim_type !=10661002 and  a.status != 10791005\n" );
		sql.append("and a.balance_no = '"+BALANCE_ODER+"' and b.price > 0\n" );
		sql.append("group by b.workhour_code,b.workhour_name  ) a\n" );
		
		
		sql.append("");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;



	}
	
	
	
	public List<Map<String, Object>> AppprintRk(String BALANCE_ODER)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("select * from (\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("select c.part_code,c.part_name,count(c.balance_quantity) balance_quantity, max(a.CLAIM_NO) CLAIM_NO  \n" );
		sql.append("from tt_as_wr_application a , tt_as_wr_partsitem c\n" );
		sql.append("where  c.id = a.id\n" );
		sql.append(" and  a.claim_type !=10661002 and a.status != 10791005\n" );
		sql.append("and a.balance_no = '"+BALANCE_ODER+"' and c.balance_amount > 0\n" );
		sql.append("group by c.part_code,c.part_name\n" );
		sql.append("\n" );
		sql.append("union all\n" );
		sql.append("\n" );
		sql.append("select b.WORKHOUR_CODE,b.WORKHOUR_NAME,count(1),max(a.CLAIM_NO) CLAIM_NO \n" );
		
		sql.append("from tt_as_wr_application a ,tt_claim_accessory_dtl b\n" );
		sql.append("where a.claim_no = b.claim_no\n" );
		sql.append("and a.claim_type !=10661002 and  a.status != 10791005\n" );
		sql.append("and a.balance_no = '"+BALANCE_ODER+"' and b.price > 0\n" );
		sql.append("group by b.workhour_code,b.workhour_name  ) a\n" );
		
		
		sql.append("");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;



	}
	
	public Map<String, Object> DealerUnitKpXiaoZ(String CHECK_TICKETS_DATE_S,String CHECK_TICKETS_DATE,double sl,double cs)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select null,null,null,null,sum(a.balance_amount) balance_amount,sum(a.s_balance_amount)  s_balance_amount from (\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("select c.part_code,c.part_name,count(c.balance_quantity) balance_quantity,\n" );
		sql.append("round(max(c.price)*"+cs+"/"+sl+",9) price,\n" );
		sql.append("round(sum(c.balance_amount) *"+cs+" /"+sl+",2) balance_amount,\n" );
		sql.append("( round(sum(c.balance_amount)*"+cs+"    ,2)  - round(sum(c.balance_amount) *"+cs+" /"+sl+",2)  ) s_balance_amount\n" );
		sql.append("from tt_as_wr_application a , tt_as_wr_partsitem c\n" );
		sql.append("where  c.id = a.id\n" );
		sql.append(" and  a.status != 10791005 and a.status != 10791006  and a.claim_type !=10661002 and a.status != 10791005\n" );
		sql.append("and a.balance_no in (select t.balance_oder from tt_as_payment t where t.TRANSFER_TICKETS_DATE >= to_date('"+CHECK_TICKETS_DATE_S+"','yyyy-mm-dd') and  t.TRANSFER_TICKETS_DATE < to_date('"+CHECK_TICKETS_DATE+"','yyyy-mm-dd')+1) and c.balance_amount > 0\n" );
		sql.append("group by c.part_code,c.part_name\n" );
		sql.append("\n" );
		sql.append("union all\n" );
		sql.append("\n" );
		sql.append("select b.WORKHOUR_CODE,b.WORKHOUR_NAME,count(1),\n" );
		sql.append("round(max(b.price) *"+cs+"/"+sl+",9),\n" );
		sql.append("round(sum(b.price) *"+cs+"/"+sl+",2),\n" );
		sql.append(" round(sum(b.price)*"+cs+"  ,2) - round(sum(b.price) *"+cs+"/"+sl+",2)\n" );
		sql.append("from tt_as_wr_application a ,tt_claim_accessory_dtl b\n" );
		sql.append("where a.claim_no = b.claim_no\n" );
		sql.append(" and  a.status != 10791005 and a.status != 10791006 and  a.claim_type !=10661002 and a.status != 10791005\n" );
		sql.append("and a.balance_no in (select t.balance_oder from tt_as_payment t where t.TRANSFER_TICKETS_DATE >= to_date('"+CHECK_TICKETS_DATE_S+"','yyyy-mm-dd') and  t.TRANSFER_TICKETS_DATE < to_date('"+CHECK_TICKETS_DATE+"','yyyy-mm-dd')+1) and b.price > 0\n" );
		sql.append("group by b.workhour_code,b.workhour_name  ) a\n" );
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;



	}
	
	
	public Map<String, Object> AppprintXiaoZ(String BALANCE_ODER,double sl,double cs)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select null,null,null,null,sum(a.balance_amount) balance_amount,sum(a.s_balance_amount)  s_balance_amount from (\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("select c.part_code,c.part_name,count(c.balance_quantity) balance_quantity,\n" );
		sql.append("round(max(c.price)*"+cs+"/"+sl+",9) price,\n" );
		sql.append("round(sum(c.balance_amount) *"+cs+" /"+sl+",2) balance_amount,\n" );
		sql.append("( round(sum(c.balance_amount)*"+cs+"    ,2)  - round(sum(c.balance_amount) *"+cs+" /"+sl+",2)  ) s_balance_amount\n" );
		sql.append("from tt_as_wr_application a , tt_as_wr_partsitem c\n" );
		sql.append("where  c.id = a.id\n" );
		sql.append(" and a.claim_type !=10661002 and a.status != 10791005\n" );
		sql.append("and a.balance_no = '"+BALANCE_ODER+"' and c.balance_amount > 0\n" );
		sql.append("group by c.part_code,c.part_name\n" );
		sql.append("\n" );
		sql.append("union all\n" );
		sql.append("\n" );
		sql.append("select b.WORKHOUR_CODE,b.WORKHOUR_NAME,count(1),\n" );
		sql.append("round(max(b.price) *"+cs+"/"+sl+",9),\n" );
		sql.append("round(sum(b.price) *"+cs+"/"+sl+",2),\n" );
		sql.append(" round(sum(b.price)*"+cs+"  ,2) - round(sum(b.price) *"+cs+"/"+sl+",2)\n" );
		sql.append("from tt_as_wr_application a ,tt_claim_accessory_dtl b\n" );
		sql.append("where a.claim_no = b.claim_no\n" );
		sql.append("and  a.claim_type !=10661002 and a.status != 10791005\n" );
		sql.append("and a.balance_no = '"+BALANCE_ODER+"' and b.price > 0\n" );
		sql.append("group by b.workhour_code,b.workhour_name  ) a\n" );
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;



	}
	
	
	public Map<String, Object> queryCLAIM(String FK_DEALER_ID,String startDate,String endDate)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT\n" );
	
		sql.append("     ( nvl(A.special_labour_sum,0)+\n" );
		sql.append("       nvl(A.special_datum_sum,0)-\n" );
		sql.append("       nvl(A.OLD_DEDUCT,0)\n" );
		sql.append("      ) otherAccount,\n" );
		sql.append("  (  nvl(A.PLUS_MINUS_DATUM_SUM,0)+ nvl(A.PLUS_MINUS_LABOUR_SUM,0) ) PLUS_MINUS_SUM, ");
		sql.append(" to_char( ((  nvl(A.PLUS_MINUS_DATUM_SUM,0)+ nvl(A.PLUS_MINUS_LABOUR_SUM,0) )/1.17),'9999999.99') FINE_AMOUNT_OF_MONEY, ");
		sql.append(" to_char(((  nvl(A.PLUS_MINUS_DATUM_SUM,0)+ nvl(A.PLUS_MINUS_LABOUR_SUM,0) ) -((  nvl(A.PLUS_MINUS_DATUM_SUM,0)+ nvl(A.PLUS_MINUS_LABOUR_SUM,0) )/1.17)),'9999999.99') FINE_TAX_RATE_MONEY, ");
		sql.append("     A.invoice_id,\n" );
		sql.append("     A.selectment_amount,\n" );
		sql.append("     A.invoice_taxrate,\n" );
		sql.append("       nvl(A.RETURN_AMOUNT,0) as RETURN_AMOUNT,A.APPEND_AMOUNT,\n" );
		sql.append("       A.AMOUNT_SUM as AMOUNT_SUM ,A.REMARK,\n" );
		
		sql.append("       to_char( ((A.AMOUNT_SUM)/1.17),'9999999.99') AMOUNT_OF_MONEY ,\n" );
		sql.append("      to_char( ( (A.AMOUNT_SUM)  - ((A.AMOUNT_SUM)/1.17)),'9999999.99') TAX_RATE_MONEY ,\n" );
		
		sql.append("       (A.AMOUNT_SUM-nvl(A.PART_AMOUNT,0)-nvl(a.SERVICE_PART_AMOUNT,0)) labour_Accout,\n" );
		sql.append("       (nvl(A.PART_AMOUNT,0)+ nvl(a.SERVICE_PART_AMOUNT,0)) part_account\n" );
		sql.append("\n" );
		sql.append("  from TT_AS_WR_CLAIM_BALANCE A\n" );
		sql.append("\n" );
		sql.append("  where A.DEALER_ID =\n" + FK_DEALER_ID);
		sql.append("  and A.START_DATE >= to_date('"+startDate+"','yyyy-mm-dd')\n" );
		sql.append("  and A.END_DATE < (to_date('"+endDate+"','yyyy-mm-dd')+1)\n" );
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		if(list.size() >  0 )
		{
			return list.get(0);
		}
		return  null;

	}

	public Map<String, Object> getBalancePrintMap(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.*, (NVL(A.FREE_DEDUCT,0) + NVL(A.SERVICE_DEDUCT, 0) + NVL(A.CHECK_DEDUCT,0) + NVL(A.OLD_DEDUCT,0) + NVL(A.ADMIN_DEDUCT,0)) DEDUCT,\n" );
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATEDATE,\n" );
		sql.append("       TO_CHAR(A.START_DATE, 'YYYY-MM-DD') STARTDATE,\n" );
		sql.append("       (A.AMOUNT_SUM - A.RETURN_AMOUNT) ABC,\n" );
		sql.append("       (NVL(A.MARKET_AMOUNT, 0) + NVL(A.APPEND_AMOUNT, 0)) DECLARE_SUM1,\n" );
		sql.append("       (NVL(A.SERVICE_FIXED_AMOUNT, 0) + NVL(A.SERVICE_LABOUR_AMOUNT, 0) + NVL(A.SERVICE_PART_AMOUNT, 0) + NVL(A.SERVICE_OTHER_AMOUNT, 0)) SERVICE_AMOUNT,\n" );
		sql.append("       TO_CHAR(A.END_DATE, 'YYYY-MM-DD') ENDDATE,\n" );
		sql.append("       NVL(A.NOTE_AMOUNT, A.BALANCE_AMOUNT) NOTEAMOUNT,\n" );
		sql.append("       NVL(B.BCOUNT, 0) BCOUNT, NVL(C.CCOUNT, 0) CCOUNT,NVL(D.DCOUNT, 0) DCOUNT,NVL(E.ECOUNT, 0) ECOUNT,NVL(F.FCOUNT, 0) FCOUNT,\n" );
		/*******add by liuxh 20201207 增加*******/
		sql.append("       (SELECT T.DEALER_CODE FROM TM_DEALER T WHERE A.KP_DEALER_ID=T.DEALER_ID) AS KP_DEALER_CODE,\n ");
		sql.append("       (SELECT COUNT(1) FROM TT_AS_WR_SPEFEE K WHERE K.CLAIMBALANCE_ID=A.ID) AS SP_COUNT,\n ");//特殊工单数
		sql.append("       ((NVL(MARKET_AMOUNT_BAK,0)-NVL(MARKET_AMOUNT,0))+(NVL(SPEOUTFEE_AMOUNT_BAK,0)-NVL(SPEOUTFEE_AMOUNT,0))) AS SP_QK,\n"); //特殊费用扣款
		sql.append("       (NVL(RETURN_AMOUNT_BAK,0)-NVL(RETURN_AMOUNT,0)) AS RETURN_QK,\n"); //运费扣款
		 
//		sql.append("       (NVL(LABOUR_AMOUNT_BAK,0)+NVL(PART_AMOUNT_BAK,0)+NVL(OTHER_AMOUNT_BAK,0)+NVL(FREE_AMOUNT_BAK,0)+NVL(MARKET_AMOUNT_BAK,0)+NVL(SPEOUTFEE_AMOUNT_BAK,0)+NVL(APPEND_AMOUNT_BAK,0)+NVL(SERVICE_TOTAL_AMOUNT_BAK,0)+NVL(APPEND_LABOUR_AMOUNT_BAK,0))-"); //审核扣款
//		sql.append("(NVL(LABOUR_AMOUNT,0)+NVL(PART_AMOUNT,0)+NVL(OTHER_AMOUNT,0)+NVL(FREE_AMOUNT,0)+NVL(MARKET_AMOUNT,0)+NVL(SPEOUTFEE_AMOUNT,0)+NVL(APPEND_AMOUNT,0)+NVL(SERVICE_TOTAL_AMOUNT,0)+NVL(APPEND_LABOUR_AMOUNT,0)) AS CHECK_KKS,\n"); //
		sql.append("       (NVL(LABOUR_AMOUNT_BAK,0)+NVL(PART_AMOUNT_BAK,0)+NVL(OTHER_AMOUNT_BAK,0)+NVL(FREE_AMOUNT_BAK,0)+NVL(APPEND_AMOUNT_BAK,0)+NVL(SERVICE_TOTAL_AMOUNT_BAK,0)+NVL(APPEND_LABOUR_AMOUNT_BAK,0))-"); //审核扣款
		sql.append("(NVL(LABOUR_AMOUNT,0)+NVL(PART_AMOUNT,0)+NVL(OTHER_AMOUNT,0)+NVL(FREE_AMOUNT,0)+NVL(APPEND_AMOUNT,0)+NVL(SERVICE_TOTAL_AMOUNT,0)+NVL(APPEND_LABOUR_AMOUNT,0)) AS CHECK_KKS,\n"); //
		/*******add by liuxh 20201207 增加*******/ 
		/*********Iverson add 2010-11-24 添加查询出经销商代码和审核人********************/
		sql.append("       (select t.dealer_code from tm_dealer t where A.dealer_id=t.dealer_id) AS dealer_code, ");
		sql.append("       (select distinct y.auth_person_name from Tt_As_Wr_Balance_Authitem y where y.balance_id = '"+id+"'  and y.auth_time=(select max(g.AUTH_TIME) FROM Tt_As_Wr_Balance_Authitem g WHERE g.balance_id=Y.balance_id)) as USER_NAME ");
		/*********Iverson add 2010-11-24 添加查询出经销商代码和审核人********************/
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE A,tm_dealer t,\n" );
		sql.append("     (\n" );
		sql.append("       SELECT /*+RULE*/B.BALANCE_ID, COUNT(A.ID) BCOUNT\n" );
		sql.append("       FROM TT_AS_WR_APPLICATION A, TR_BALANCE_CLAIM B\n" );
		sql.append("       WHERE A.ID = B.CLAIM_ID\n" );
		sql.append("       AND A.CLAIM_TYPE = ").append(Constant.CLA_TYPE_01).append("--售前维修\n" );
		sql.append("       AND B.BALANCE_ID = ").append(id).append("\n");
		sql.append("       GROUP BY B.BALANCE_ID\n" );
		sql.append("      ) B,\n" );
		sql.append("     (\n" );
		sql.append("       SELECT /*+RULE*/B.BALANCE_ID, COUNT(A.ID) CCOUNT\n" );
		sql.append("       FROM TT_AS_WR_APPLICATION A, TR_BALANCE_CLAIM B\n" );
		sql.append("       WHERE A.ID = B.CLAIM_ID\n" );
		sql.append("       AND A.CLAIM_TYPE = ").append(Constant.CLA_TYPE_07).append("--售前维修\n" );
		sql.append("       AND B.BALANCE_ID = ").append(id).append("\n");
		sql.append("       GROUP BY B.BALANCE_ID\n" );
		sql.append("      ) C,\n" );
		sql.append("     (\n" );
		sql.append("       SELECT /*+RULE*/B.BALANCE_ID, COUNT(A.ID) DCOUNT\n" );
		sql.append("       FROM TT_AS_WR_APPLICATION A, TR_BALANCE_CLAIM B\n" );
		sql.append("       WHERE A.ID = B.CLAIM_ID\n" );
		sql.append("       AND A.CLAIM_TYPE = ").append(Constant.CLA_TYPE_09).append("--外出维修\n" );
		sql.append("       AND B.BALANCE_ID = ").append(id).append("\n");
		sql.append("       GROUP BY B.BALANCE_ID\n" );
		sql.append("      ) D,\n" );
		sql.append("     (\n" );
		sql.append("       SELECT /*+RULE*/B.BALANCE_ID, COUNT(A.ID) ECOUNT\n" );
		sql.append("       FROM TT_AS_WR_APPLICATION A, TR_BALANCE_CLAIM B\n" );
		sql.append("       WHERE A.ID = B.CLAIM_ID\n" );
		sql.append("       AND A.CLAIM_TYPE = ").append(Constant.CLA_TYPE_02).append("--免费保养\n" );
		sql.append("       AND B.BALANCE_ID = ").append(id).append("\n");
		sql.append("       GROUP BY B.BALANCE_ID\n" );
		sql.append("      ) E,\n" );
		sql.append("     (\n" );
		sql.append("       SELECT /*+RULE*/B.BALANCE_ID, COUNT(A.ID) FCOUNT\n" );
		sql.append("       FROM TT_AS_WR_APPLICATION A, TR_BALANCE_CLAIM B\n" );
		sql.append("       WHERE A.ID = B.CLAIM_ID\n" );
		sql.append("       AND A.CLAIM_TYPE = ").append(Constant.CLA_TYPE_06).append("--服务活动\n" );
		sql.append("       AND B.BALANCE_ID = ").append(id).append("\n");
		sql.append("       GROUP BY B.BALANCE_ID\n" );
		sql.append("      ) F\n" );
		sql.append("WHERE A.ID = B.BALANCE_ID(+)\n" );
		sql.append("AND A.ID = C.BALANCE_ID(+)\n" );
		sql.append("AND A.ID = D.BALANCE_ID(+)\n" );
		sql.append("AND A.ID = E.BALANCE_ID(+)\n" );
		sql.append("AND A.ID = F.BALANCE_ID(+)\n" );
		sql.append("AND A.dealer_id = t.dealer_id\n" );
		sql.append("AND A.ID = ").append(id).append("\n");

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}

	public List<Map<String, Object>> getBalanceMainList1(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.*, (NVL(A.SERVICE_FIXED_AMOUNT, 0) + NVL(A.SERVICE_LABOUR_AMOUNT, 0) + NVL(A.SERVICE_PART_AMOUNT, 0) + NVL(A.SERVICE_OTHER_AMOUNT, 0)) SERVICE_AMOUNT FROM Tt_As_Wr_Claim_Balance_Detail A ");
		sql.append("WHERE BALANCE_ID = ").append(id);
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	public List getDate(String id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select * from tt_as_wr_balance_authitem b where b.balance_id=? and b.auth_status = ? ");
		List list=new ArrayList();
		list.add(Long.valueOf(id));
		list.add(com.infodms.dms.actions.claim.application.BalanceStatusRecord.STATUS_06);
		
		return this.select(TtAsWrBalanceAuthitemPO.class, sql.toString(), list);
	}

	public void findReturnAndBackUp(String id, String jsNO) {
		blanceDao.insert("insert into tt_balance_return_relation values ('"+id+"','"+jsNO+"')");
	}

	public void findAdministrativeAndBackUp(String dealerId,String jsNO) {
		String sql="select * from   tt_as_wr_administrative_charge t  where   t.STATUS != 94151001 and    t.DEALERID ="+dealerId;
		List<TtAsWrAdministrativeChargePO> list = blanceDao.select(TtAsWrAdministrativeChargePO.class, sql, null);
		if(list!=null && list.size()>0){
			for (TtAsWrAdministrativeChargePO t : list) {
				blanceDao.insert("insert into tt_balance_charge_relation values('"+t.getId()+"','"+jsNO+"')");
			}
		}
	}

	public void expotdelaerKpMainQueryData(ActionContext act,PageResult<Map<String, Object>> list) {
		try {
			String[] head = new String[19];
			head[0] = "结算编号";
			head[1] = "经销商代码";
			head[2] = "经销商名称";
			head[3] = "开始时间";
			head[4] = "结束时间";
			head[5] = "结算人";
			head[6] = "结算日期";
			head[7] = "材料费";
			head[8] = "维修工时费";
			head[9] = "保养费";
			head[10] = "上次行政扣款总金额";
			head[11] = "本次行政扣款总金额";
			head[12] = "总计（元）";
			head[13] = "收票人";
			head[14] = "收票时间";
			head[15] = "验票人";
			head[16] = "验票时间";
			head[17] = "转账人";
			head[18] = "转账时间";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail = new String[19];
					//========================================zyw 接口数据查询2014-10-31
					/*InvoiceService invoiceService = new InvoiceServiceImpl();
					Map<String, Double> InfoMoney = invoiceService.invoiceInfoMoneyByNO(CommonUtils.checkNull(map.get("REMARK")));
					Double partAndAccMoney = InfoMoney.get("partAndAccMoney");
					Double pdiAndKeepFitMoney = InfoMoney.get("pdiAndKeepFitMoney");
					Double ohersMoney = InfoMoney.get("ohersMoney");*/
					//========================================
					detail[0] = CommonUtils.checkNull(map.get("REMARK"));
					detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
					detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
					detail[3] = CommonUtils.checkNull(map.get("START_DATE"));
					detail[4] = CommonUtils.checkNull(map.get("END_DATE"));
					detail[5] = CommonUtils.checkNull(map.get("APPLY_PERSON_NAME"));
					detail[6] = CommonUtils.checkNull(map.get("CREATE_DATE"));
					detail[7] = CommonUtils.checkNull(map.get("PARTANDACCMONEY"));
					detail[8] = CommonUtils.checkNull(map.get("OHERSMONEY"));
					detail[9] = CommonUtils.checkNull(map.get("PDIANDKEEPFITMONEY"));
					detail[10] = CommonUtils.checkNull(map.get("LABOUR_SUM"));
					detail[11] = CommonUtils.checkNull(map.get("DATUM_SUM"));
					detail[12] = CommonUtils.checkNull(map.get("AMOUNT_SUM"));
					detail[13] = CommonUtils.checkNull(map.get("COLLECT_TICKETS"));
					detail[14] = CommonUtils.checkNull(map.get("COLLECT_TICKETS_DATE"));
					detail[15] = CommonUtils.checkNull(map.get("CHECK_TICKETS"));
					detail[16] = CommonUtils.checkNull(map.get("CHECK_TICKETS_DATE"));
					detail[17] = CommonUtils.checkNull(map.get("TRANSFER_TICKETS"));
					detail[18] = CommonUtils.checkNull(map.get("TRANSFER_TICKETS_DATE"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "开票通知单"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Map<String,Object>> findExceptionPartRepair(RequestWrapper request) {
		String start_date = DaoFactory.getParam(request, "endBalanceDate");
		String end_date = DaoFactory.getParam(request, "CON_END_DAY");
		String dealerId = DaoFactory.getParam(request, "dealerId");
		StringBuffer sb= new StringBuffer();
		sb.append("select *\n" );
		sb.append("  from Tt_As_Wr_Application a, Tt_As_Wr_Partsitem p\n" );
		sb.append(" where p.id = a.id\n" );
		sb.append("   and p.part_use_type = 0");
		sb.append("   and a.status >= 10791007\n" );
		sb.append("   and a.is_import = 10041002 and p.balance_amount>0\n");
		DaoFactory.getsql(sb, "a.sub_date", start_date, 3);
		DaoFactory.getsql(sb, "a.sub_date", end_date, 4);
		DaoFactory.getsql(sb, "a.dealer_id", dealerId, 1);
		List<Map<String,Object>> list = this.pageQuery(sb.toString(), null, getFunName());
		return list;
	}
	public List<TtAsPaymentPO> findBalance(String balance) {
		StringBuffer sb= new StringBuffer();
		sb.append("  SELECT * FROM Tt_As_Payment WHERE 1=1  AND Balance_Oder='"+balance+"'\n" );
		List<TtAsPaymentPO> list = this.pageQuery(sb.toString(), null, getFunName());
		return list;
	}
	public List<Map<String, Object>> balanceRecord(String id) {
		StringBuffer sql= new StringBuffer();
		sql.append("select g.id,\n" );
		sql.append("       g.balance_ID,\n" );
		sql.append("       g.audit_record,\n" );
		sql.append("       TO_CHAR(g.audit_date, 'YYYY-MM-DD hh24:mi:ss') audit_date,\n" );
		sql.append("       g.audit_by,\n" );
		sql.append("       u.name,\n" );
		sql.append("       (SELECT c.code_desc FROM tc_code c where c.code_id=g.opera_ststus) as opera_ststus\n" );
		sql.append("  from tt_as_wr_balance_record g left join tc_user u on g.audit_by=u.user_Id \n" );
		sql.append(" where 1=1 ");

		DaoFactory.getsql(sql, "g.balance_ID", id, 1);
		List<Map<String,Object>> list = this.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}