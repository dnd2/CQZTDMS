package com.infodms.dms.dao.report.serviceReport;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class BaseReportDao extends BaseDao {
	public static final Logger logger = Logger.getLogger(BaseReportDao.class);
	public static final BaseReportDao dao = new BaseReportDao();
	public static final BaseReportDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public PageResult<Map<String, Object>> SettlementWorkloadQueryData(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		List par=new ArrayList();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DEALER_CODE,\n" );
		sql.append("      MAX(M.ROOT_DEALER_NAME) DEALER_NAME,\n" );
		sql.append("      max(M.REGION_NAME) REGION_NAME,\n" );
		sql.append("      sum(M.appsum) appsum,\n" );
		sql.append("      sum(M.Sfeesum) Sfeesum,\n" );
		sql.append("      sum(M.countsum) countsum,\n" );
		sql.append("      sum(nvl(M.LABOUR_AMOUNT,0)) LABOUR_AMOUNT,\n" );
		sql.append("      sum(nvl(M.PART_AMOUNT,0)) PART_AMOUNT,\n" );
		sql.append("      sum(nvl(M.NETITEM_AMOUNT,0) ) NETITEM_AMOUNT ,\n" );
		sql.append("      sum(nvl(M.REPAIR_TOTAL,0)) REPAIR_TOTAL,\n" );
		sql.append("      sum(nvl(M.BALANCE_LABOUR_AMOUNT,0)) BALANCE_LABOUR_AMOUNT,\n" );
		sql.append("      sum(nvl(M.BALANCE_PART_AMOUNT,0)) BALANCE_PART_AMOUNT,\n" );
		sql.append("      sum(nvl(M.BALANCE_NETITEM_AMOUNT,0)) BALANCE_NETITEM_AMOUNT,\n" );
		sql.append("      sum(nvl(M.BALANCE_AMOUNT,0)) BALANCE_AMOUNT,\n" );
		sql.append("      sum(nvl(M.DIS_LABOUR_AMOUNT,0)) DIS_LABOUR_AMOUNT,\n" );
		sql.append("      sum(nvl(M.DIS_PART_AMOUNT,0)) DIS_PART_AMOUNT,\n" );
		sql.append("      sum(nvl(M.DIS_NETITEM_AMOUNT,0)) DIS_NETITEM_AMOUNT,\n" );
		sql.append("      sum(nvl(M.DIS_AMOUNT,0)) DIS_AMOUNT\n" );
		sql.append("      from(\n" );
		sql.append("SELECT DS.ROOT_DEALER_CODE DEALER_CODE,\n" );
		sql.append("       MAX(DS.ROOT_DEALER_NAME) ROOT_DEALER_NAME,\n" );
		sql.append("       max(DS.REGION_NAME) REGION_NAME,\n" );
		sql.append("       count(A.ID) appsum,\n" );
		sql.append("       0 Sfeesum,\n" );
		sql.append("       count(A.ID) countsum,\n" );
		sql.append("       sum(A.REPAIR_TOTAL - A.PART_AMOUNT -\n" );
		sql.append("           decode(A.CLAIM_TYPE, 10661002, mg.PART_PRICE, 0) -\n" );
		sql.append("           decode(A.CLAIM_TYPE, 10661009, A.NETITEM_AMOUNT, 0)) LABOUR_AMOUNT,\n" );
		sql.append("       sum(A.PART_AMOUNT + decode(A.CLAIM_TYPE, 10661002, mg.PART_PRICE, 0)) PART_AMOUNT,\n" );
		sql.append("       sum(decode(A.CLAIM_TYPE, 10661009, A.NETITEM_AMOUNT, 0)) NETITEM_AMOUNT,\n" );
		sql.append("       sum(A.REPAIR_TOTAL) REPAIR_TOTAL,\n" );
		sql.append("       sum(\n" );
		sql.append("          decode(  A.STATUS ,10791006,0, A.BALANCE_AMOUNT - A.BALANCE_PART_AMOUNT -\n" );
		sql.append("           decode(A.CLAIM_TYPE, 10661002, mg.PART_PRICE, 0) -\n" );
		sql.append("           decode(A.CLAIM_TYPE, 10661009, A.BALANCE_NETITEM_AMOUNT, 0)\n" );
		
		sql.append("           )  ) BALANCE_LABOUR_AMOUNT,\n" );
		sql.append("       sum(decode(  A.STATUS ,10791006,0,A.BALANCE_PART_AMOUNT +\n" );
		sql.append("           decode(A.CLAIM_TYPE, 10661002, mg.PART_PRICE, 0))) BALANCE_PART_AMOUNT,\n" );
		sql.append("       sum(decode(  A.STATUS ,10791006,0,decode(A.CLAIM_TYPE, 10661009, A.BALANCE_NETITEM_AMOUNT, 0))) BALANCE_NETITEM_AMOUNT,\n" );
		sql.append("       sum(decode(  A.STATUS ,10791006,0,A.BALANCE_AMOUNT\n" );
		sql.append("           )) BALANCE_AMOUNT,\n" );
		sql.append("       sum((A.REPAIR_TOTAL - A.PART_AMOUNT -\n" );
		sql.append("           decode(A.CLAIM_TYPE, 10661002, mg.PART_PRICE, 0) -\n" );
		sql.append("           decode(A.CLAIM_TYPE, 10661009, A.NETITEM_AMOUNT, 0)) -\n" );
		sql.append("           (decode(  A.STATUS ,10791006,0,A.BALANCE_AMOUNT - A.BALANCE_PART_AMOUNT -\n" );
		sql.append("           decode(A.CLAIM_TYPE, 10661002, mg.PART_PRICE, 0) -\n" );
		sql.append("           decode(A.CLAIM_TYPE, 10661009, A.BALANCE_NETITEM_AMOUNT, 0) \n" );
		sql.append("           ))) DIS_LABOUR_AMOUNT,\n" );
		sql.append("       sum((A.PART_AMOUNT +\n" );
		sql.append("           decode(A.CLAIM_TYPE, 10661002, mg.PART_PRICE, 0)) -\n" );
		sql.append("           (decode(  A.STATUS ,10791006,0,A.BALANCE_PART_AMOUNT +\n" );
		sql.append("           decode(A.CLAIM_TYPE, 10661002, mg.PART_PRICE, 0)))) DIS_PART_AMOUNT,\n" );
		sql.append("       sum((decode(A.CLAIM_TYPE, 10661009, A.NETITEM_AMOUNT, 0)) -\n" );
		sql.append("           (decode(  A.STATUS ,10791006,0,decode(A.CLAIM_TYPE, 10661009, A.BALANCE_NETITEM_AMOUNT, 0)))) DIS_NETITEM_AMOUNT,\n" );
		sql.append("       sum(A.REPAIR_TOTAL - (decode(  A.STATUS ,10791006,0,A.BALANCE_AMOUNT \n" );
		sql.append("             ))) DIS_AMOUNT\n" );
		sql.append("  from TT_AS_WR_MODEL_ITEM   mi,\n" );
		sql.append("       tm_vehicle            vv,\n" );
		sql.append("       tt_as_wr_model_group  mg,\n" );
		sql.append("       VW_ORG_DEALER_SERVICE DS,\n" );
		sql.append("       TT_AS_WR_APPLICATION  A\n" );
		sql.append("  LEFT JOIN TT_AS_ACTIVITY TA ON A.CAMPAIGN_CODE = TA.ACTIVITY_CODE\n" );
		sql.append("  LEFT JOIN TT_AS_ACTIVITY_SUBJECT TS ON TA.SUBJECT_ID = TS.SUBJECT_ID\n" );
		sql.append(" where vv.package_id = mi.model_id\n" );
		sql.append("   and mg.wrgroup_id = mi.wrgroup_id\n" );
		sql.append("   and vv.vin = A.VIN\n" );
		sql.append("   AND MG.WRGROUP_TYPE = 10451001\n" );
		sql.append("   AND a.create_date > to_date('2013-08-26', 'yyyy-mm-dd')\n" );
		sql.append("   AND (a.claim_type != 10661006 OR\n" );
		sql.append("       (a.claim_type = 10661006 AND ts.activity_type = 10561001))\n" );
		sql.append("   and a.STATUS IN (10791006, 10791013)\n" );
		sql.append("   and a.FI_DATE is not null\n" );
		sql.append("   and A.DEALER_ID = DS.DEALER_ID\n" );
		if( Utility.testString( map.get("bDate").toString()))
		{
			sql.append("   and a.FI_DATE >= to_date(?, 'yyyy-mm-dd')\n" );
			par.add(map.get("bDate").toString());
		}
		if( Utility.testString( map.get("eDate").toString()))
		{
			sql.append("   and a.FI_DATE <= (to_date(?, 'yyyy-mm-dd')+1)\n" );
			par.add(map.get("eDate").toString());
		}		
		if( Utility.testString( map.get("YIELDLY_TYPE").toString()))
		{
			par.add(map.get("YIELDLY_TYPE").toString());
			sql.append("  and A.BALANCE_YIELDLY = ?\n" );
		}
		
		if( Utility.testString( map.get("supply_name").toString()))
		{
			par.add("%"+ map.get("supply_name").toString()+"%");
			sql.append(" AND DS.DEALER_NAME LIKE ?\n" );
		}
		
		if( Utility.testString( map.get("supply_code").toString()))
		{
			par.add( "%"+ map.get("supply_code").toString()+"%");
			sql.append(" AND DS.DEALER_CODE LIKE ?\n" );
		}
		if( Utility.testString( map.get("small_org").toString()))
		{
			par.add(map.get("small_org").toString());
			sql.append(" AND DS.ORG_ID = ? \n" );
		}

		sql.append(" GROUP by DS.ROOT_DEALER_CODE\n" );
		sql.append("UNION All\n" );
		sql.append("SELECT\n" );
		sql.append("       DS.ROOT_DEALER_CODE DEALER_CODE,\n" );
		sql.append("       MAX(DS.ROOT_DEALER_NAME) ROOT_DEALER_NAME,\n" );
		sql.append("       max(DS.REGION_NAME) REGION_NAME,\n" );
		sql.append("       0 appsum,\n" );
		sql.append("       count(S.ID) Sfeesum,\n" );
		sql.append("       count(S.ID) countsum,\n" );
		sql.append("       sum(DECODE(S.BALANCE_FEE_TYPE, 94141002, S.DECLARE_SUM1, 0)),\n" );
		sql.append("       sum(DECODE(S.BALANCE_FEE_TYPE, 94141001, S.DECLARE_SUM1, 0)),\n" );
		sql.append("       0,\n" );
		sql.append("       sum(S.DECLARE_SUM),\n" );
		sql.append("       sum(DECODE(S.BALANCE_FEE_TYPE, 94141002, S.OFFICE_DECLARE_SUM, 0)),\n" );
		sql.append("      sum( DECODE(S.BALANCE_FEE_TYPE, 94141001, S.OFFICE_DECLARE_SUM, 0)),\n" );
		sql.append("       0,\n" );
		sql.append("       sum(S.OFFICE_DECLARE_SUM),\n" );
		sql.append("       sum(DECODE(S.BALANCE_FEE_TYPE, 94141002, S.DECLARE_SUM1, 0)-   DECODE(S.BALANCE_FEE_TYPE, 94141002, S.OFFICE_DECLARE_SUM, 0)),\n" );
		sql.append("       sum(DECODE(S.BALANCE_FEE_TYPE, 94141001, S.DECLARE_SUM1, 0)-   DECODE(S.BALANCE_FEE_TYPE, 94141001, S.OFFICE_DECLARE_SUM, 0)),\n" );
		sql.append("       0,\n" );
		sql.append("       sum(S.DECLARE_SUM - S.OFFICE_DECLARE_SUM)\n" );
		sql.append("     FROM vw_org_dealer_service ds,TT_AS_WR_SPEFEE S\n" );
		sql.append("      LEFT JOIN ( SELECT SAA.FEE_ID,max(SAA.STATUS) STATUS ,min(SAA.AUDITING_DATE) AUDITING_DATE  from  TT_AS_WR_SPEFEE_AUDITING SAA group by SAA.FEE_ID ) SA ON S.ID = SA.FEE_ID AND SA.STATUS = 11841010\n" );
		sql.append("     WHERE  S.STATUS = 11841010 and S.DEALER_ID = ds.DEALER_ID and ds.dealer_type = 10771002\n" );
		
		if( Utility.testString( map.get("bDate").toString()))
		{
			sql.append("     and SA.AUDITING_DATE >= to_date(?, 'yyyy-mm-dd')\n" );
			par.add(map.get("bDate").toString());
		}
		if( Utility.testString( map.get("eDate").toString()))
		{
			sql.append("     and SA.AUDITING_DATE <= to_date(?, 'yyyy-mm-dd')\n" );
			par.add(map.get("eDate").toString());
		}		
		if( Utility.testString( map.get("YIELDLY_TYPE").toString()))
		{
			if(map.get("YIELDLY_TYPE").toString().equals("95411001"))
			{
				sql.append("  and 1=1  ");
			}else
			{
				sql.append("  and 1!=1  ");
			}
		}
		if( Utility.testString( map.get("supply_name").toString()))
		{
			par.add("%"+ map.get("supply_name").toString()+"%");
			sql.append(" AND DS.DEALER_NAME LIKE ?\n" );
		}
		
		if( Utility.testString( map.get("supply_code").toString()))
		{
			par.add( "%"+ map.get("supply_code").toString()+"%");
			sql.append(" AND DS.DEALER_CODE LIKE ?\n" );
		}
		if( Utility.testString( map.get("small_org").toString()))
		{
			par.add(map.get("small_org").toString());
			sql.append(" AND DS.ORG_ID = ? \n" );
		}
		sql.append("     GROUP by DS.ROOT_DEALER_CODE) M\n" );
		sql.append("     group by M.DEALER_CODE");
		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), par, getFunName(),pageSize, curPage);
		return ps;
	}
	
	
	
	
	public PageResult<Map<String, Object>> SettlementCountQueryData(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		List par=new ArrayList();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT\n" );
		sql.append("   D.DEALER_CODE,\n" );
		sql.append("   D.REPORT_DATE,\n" );
		sql.append("   D.BALANCE_ODER,\n" );
		sql.append("   D.ROOT_DEALER_NAME,\n" );
		sql.append("   D.CLA_TYPE_01,\n" );
		sql.append("   D.CLA_TYPE_07 ,\n" );
		sql.append("   D.CLA_TYPE_10,\n" );
		sql.append("   D.CLA_TYPE_09,\n" );
		sql.append("   D.BALANCE_LABOUR_AMOUNT,\n" );
		sql.append("   D.BALANCE_PART_AMOUNT,\n" );
		sql.append("   D.BALANCE_NETITEM_AMOUNT,\n" );
		sql.append("   D.CLA_TYPE_02,\n" );
		sql.append("   D.LABOUR_PRICE,\n" );
		sql.append("   D.PART_PRICE,\n" );
		sql.append("   D.COUNT_PRICE,\n" );
		sql.append("   D.CLA_TYPE_06,\n" );
	    sql.append(" decode(TAA.BALANCE_YIELDLY,95411001, nvl( TAA.CARRIAGE,0),NVL(TAA.DA_CARRIAGE,0)      )  YUNFEI, "  );
		sql.append("   D.BALANCE_LABOUR_AMOUNT_06,\n" );
		sql.append("   D.BALANCE_PART_AMOUNT_06,\n" );
		sql.append("   D.BALANCE_NETITEM_AMOUNT_06,\n" );
		sql.append("   D.BALANCE_AMOUNT_06,\n" );
		sql.append("   D.SPEE_COUNT  ,D.SPEE_LABOUR ,D.SPEE_PART,D.SPEE_BALANCE ,D.PING_COUNT,\n" );
		sql.append("   D.PING_BALANCE,\n" );
		sql.append("   nvl(TD.DISCOUNT,0)  DISCOUNT, ");
		sql.append("   (D.FINE_COUNT+   decode( D.r,1, nvl(F.COUNT_SUM,0),0 )  ) FINE_COUNT , (D.FINE_LABOUR+  decode( D.r,1, nvl(F.LABOUR_SUM,0),0  )    ) FINE_LABOUR, (   D.FINE_PART+  decode( D.r,1, nvl(F.DATUM_SUM,0) ,0 )  ) FINE_PART ,\n" );
		sql.append("   (D.COUNT_SUM+  decode( D.r,1,  nvl(F.COUNT_SUM,0),0 )  ) COUNT_SUM,\n" );
		sql.append("    (D.BALANCE_LABOUR_AMOUNT_COUNT+decode( D.r,1, nvl(F.LABOUR_SUM,0),0  ) +decode(TAA.BALANCE_YIELDLY,95411001, nvl( TAA.CARRIAGE,0),NVL(TAA.DA_CARRIAGE,0)      ) ) BALANCE_LABOUR_AMOUNT_COUNT\n" );
		sql.append("   ,(D.BALANCE_PART_AMOUNT_COUNT + nvl(TD.DISCOUNT,0) +  decode( D.r,1, nvl(F.DATUM_SUM,0) ,0 ) ) BALANCE_PART_AMOUNT_COUNT , (D.BALANCE_AMOUNT_COUNT+ nvl(TD.DISCOUNT,0) +decode( D.r,1, nvl(F.DATUM_SUM,0) ,0 )  + decode( D.r,1, nvl(F.LABOUR_SUM,0),0  )  +decode(TAA.BALANCE_YIELDLY,95411001, nvl( TAA.CARRIAGE,0),NVL(TAA.DA_CARRIAGE,0)      )) BALANCE_AMOUNT_COUNT from (\n" );
		sql.append("\n" );
		sql.append(" SELECT\n" );
		sql.append("   M.DEALER_CODE DEALER_CODE,\n" );
		sql.append("   M.REPORT_DATE REPORT_DATE,\n" );
		sql.append("   max(M.BALANCE_ODER) BALANCE_ODER,\n" );
		sql.append(" DENSE_RANK() OVER(PARTITION BY  M.BALANCE_ODER ORDER BY min(M.REPORT_DATE)) r , ");
		sql.append("   MAX(M.ROOT_DEALER_NAME) ROOT_DEALER_NAME,\n" );
		sql.append("   sum(M.CLA_TYPE_01) CLA_TYPE_01,\n" );
		sql.append("   sum(M.CLA_TYPE_07) CLA_TYPE_07 ,\n" );
		sql.append("   sum(M.CLA_TYPE_10) CLA_TYPE_10,\n" );
		sql.append("   sum(M.CLA_TYPE_09) CLA_TYPE_09,\n" );
		sql.append("   nvl(sum(M.BALANCE_LABOUR_AMOUNT),0) BALANCE_LABOUR_AMOUNT,\n" );
		sql.append("   nvl(sum(M.BALANCE_PART_AMOUNT),0) BALANCE_PART_AMOUNT,\n" );
		sql.append("   nvl(sum(M.BALANCE_NETITEM_AMOUNT),0) BALANCE_NETITEM_AMOUNT,\n" );
		sql.append("   nvl(sum(M.CLA_TYPE_02),0)  CLA_TYPE_02,\n" );
		sql.append("   nvl(sum(M.LABOUR_PRICE),0) LABOUR_PRICE,\n" );
		sql.append("   nvl(sum(M.PART_PRICE),0) PART_PRICE,\n" );
		sql.append("   nvl(sum(M.COUNT_PRICE),0) COUNT_PRICE,\n" );
		sql.append("   nvl(sum(M.CLA_TYPE_06),0)  CLA_TYPE_06,\n" );
		sql.append("   nvl(sum(M.BALANCE_LABOUR_AMOUNT_06),0)  BALANCE_LABOUR_AMOUNT_06,\n" );
		sql.append("   nvl(sum(M.BALANCE_PART_AMOUNT_06),0)  BALANCE_PART_AMOUNT_06,\n" );
		sql.append("   nvl(sum(M.BALANCE_NETITEM_AMOUNT_06),0)  BALANCE_NETITEM_AMOUNT_06,\n" );
		sql.append("   nvl(sum(BALANCE_AMOUNT_06),0) BALANCE_AMOUNT_06,\n" );
		sql.append("   sum(M.SPEE_COUNT) SPEE_COUNT ,sum(M.SPEE_LABOUR) SPEE_LABOUR ,sum(M.SPEE_PART) SPEE_PART,sum(M.SPEE_BALANCE) SPEE_BALANCE ,sum(M.PING_COUNT) PING_COUNT,\n" );
		sql.append("   sum(M.PING_BALANCE)  PING_BALANCE,sum(M.FINE_COUNT)  FINE_COUNT,sum(M.FINE_LABOUR)  FINE_LABOUR,sum(M.FINE_PART) FINE_PART,sum(M.COUNT_SUM)  COUNT_SUM, sum(M.BALANCE_LABOUR_AMOUNT_COUNT) BALANCE_LABOUR_AMOUNT_COUNT\n" );
		sql.append("   ,sum(M.BALANCE_PART_AMOUNT_COUNT) BALANCE_PART_AMOUNT_COUNT, sum(M.BALANCE_AMOUNT_COUNT) BALANCE_AMOUNT_COUNT\n" );
		sql.append(" from (\n" );
		sql.append("\n" );
		sql.append(" SELECT DS.ROOT_DEALER_CODE DEALER_CODE,\n" );
		sql.append("       to_char(A.REPORT_DATE,'yyyy-mm') REPORT_DATE,\n" );
		sql.append("       max(B.REMARK) BALANCE_ODER,\n" );
		sql.append("       MAX(DS.ROOT_DEALER_NAME) ROOT_DEALER_NAME,\n" );
		sql.append("       sum(decode(A.CLAIM_TYPE,10661001,1,0 )) CLA_TYPE_01,\n" );
		sql.append("       sum(decode(A.CLAIM_TYPE,10661007,1,0 )) CLA_TYPE_07,\n" );
		sql.append("       sum(decode(A.CLAIM_TYPE,10661010,1,0 )) CLA_TYPE_10,\n" );
		sql.append("       sum(decode(A.CLAIM_TYPE,10661009,1,0 )) CLA_TYPE_09,\n" );
		sql.append("       nvl(sum(case when A.CLAIM_TYPE in (10661001,10661007,10661009,10661010) then A.BALANCE_LABOUR_AMOUNT  end),0) BALANCE_LABOUR_AMOUNT,\n" );
		sql.append("       nvl(sum(case when A.CLAIM_TYPE in (10661001,10661007,10661009,10661010) then A.BALANCE_PART_AMOUNT  end),0) BALANCE_PART_AMOUNT,\n" );
		sql.append("       nvl(sum(case when A.CLAIM_TYPE in (10661001,10661007,10661009,10661010) then A.BALANCE_NETITEM_AMOUNT  end),0) BALANCE_NETITEM_AMOUNT,\n" );
		sql.append("       nvl(sum(decode(A.CLAIM_TYPE ,10661002,1,0)),0)  CLA_TYPE_02,\n" );
		sql.append("       nvl(sum(decode(A.CLAIM_TYPE , 10661002,mg.LABOUR_PRICE,0 )),0) LABOUR_PRICE,\n" );
		sql.append("       nvl(sum(decode(A.CLAIM_TYPE , 10661002,mg.PART_PRICE,0 )),0) PART_PRICE,\n" );
		sql.append("       nvl(sum(decode(A.CLAIM_TYPE , 10661002,mg.PART_PRICE+mg.LABOUR_PRICE,0 )),0) COUNT_PRICE,\n" );
		sql.append("       nvl(sum(decode(A.CLAIM_TYPE , 10661006,1,0)),0)  CLA_TYPE_06,\n" );
		sql.append("       nvl(sum(decode(A.CLAIM_TYPE , 10661006,A.BALANCE_LABOUR_AMOUNT,0)),0)  BALANCE_LABOUR_AMOUNT_06,\n" );
		sql.append("       nvl(sum(decode(A.CLAIM_TYPE , 10661006,A.BALANCE_PART_AMOUNT,0)),0)  BALANCE_PART_AMOUNT_06,\n" );
		sql.append("       nvl(sum(decode(A.CLAIM_TYPE , 10661006,A.BALANCE_NETITEM_AMOUNT,0)),0)  BALANCE_NETITEM_AMOUNT_06,\n" );
		sql.append("       nvl(sum(decode(A.CLAIM_TYPE ,10661006,A.BALANCE_AMOUNT,0)),0) BALANCE_AMOUNT_06,\n" );
		sql.append("       0 SPEE_COUNT ,0 SPEE_LABOUR ,0 SPEE_PART,0 SPEE_BALANCE ,0  PING_COUNT,0  PING_BALANCE,0  FINE_COUNT,0  FINE_LABOUR,0 FINE_PART,count(A.ID)  COUNT_SUM, sum(A.BALANCE_AMOUNT-nvl(A.BALANCE_PART_AMOUNT,0)- decode(A.CLAIM_TYPE , 10661002,mg.PART_PRICE,0 )) BALANCE_LABOUR_AMOUNT_COUNT\n" );
		sql.append("       ,sum(nvl(A.BALANCE_PART_AMOUNT,0)+decode(A.CLAIM_TYPE , 10661002,mg.PART_PRICE,0 )) BALANCE_PART_AMOUNT_COUNT, sum(A.BALANCE_AMOUNT) BALANCE_AMOUNT_COUNT\n" );
		sql.append("\n" );
		sql.append("  from TT_AS_WR_MODEL_ITEM   mi,\n" );
		sql.append("       tm_vehicle            vv,\n" );
		sql.append("       tt_as_wr_model_group  mg,\n" );
		sql.append("       VW_ORG_DEALER_SERVICE DS,\n" );
		
		sql.append("       (SELECT t.REMARK ,max(t.DEALER_ID) DEALER_ID,max(t.START_DATE) START_DATE,max(t.END_DATE)  END_DATE  from   TT_AS_WR_CLAIM_BALANCE t  where 1=1  ");
		if( Utility.testString( map.get("YIELDLY_TYPE").toString()))
		{
			par.add(map.get("YIELDLY_TYPE").toString());
			sql.append("  and T.BALANCE_YIELDLY = ?\n" );
		}
		sql.append("          group by t.REMARK )  B,\n" );
		
		sql.append("       TT_AS_WR_APPLICATION  A\n" );
		sql.append("  LEFT JOIN TT_AS_ACTIVITY TA ON A.CAMPAIGN_CODE = TA.ACTIVITY_CODE\n" );
		sql.append("  LEFT JOIN TT_AS_ACTIVITY_SUBJECT TS ON TA.SUBJECT_ID = TS.SUBJECT_ID\n" );
		sql.append(" where vv.package_id = mi.model_id\n" );
		sql.append("   and mg.wrgroup_id = mi.wrgroup_id\n" );
		sql.append("   and vv.vin = A.VIN\n" );
		sql.append("   and A.REPORT_DATE >= B.START_DATE\n" );
		sql.append("   and A.REPORT_DATE <= (B.END_DATE+1)\n" );
		sql.append("   and A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("   AND MG.WRGROUP_TYPE = 10451001\n" );
		sql.append("   AND a.create_date > to_date('2013-08-26', 'yyyy-mm-dd')\n" );
		sql.append("   AND (a.claim_type != 10661006 OR\n" );
		sql.append("       (a.claim_type = 10661006 AND ts.activity_type = 10561001))\n" );
		sql.append("   and a.STATUS IN (10791006, 10791013)\n" );
		sql.append("   and a.FI_DATE is not null\n" );
		sql.append("    and A.DEALER_ID = DS.DEALER_ID\n" );
		
		if( Utility.testString( map.get("bDate").toString()))
		{
			sql.append("   and a.FI_DATE >= to_date(?, 'yyyy-mm-dd')\n" );
			par.add(map.get("bDate").toString());
		}
		if( Utility.testString( map.get("eDate").toString()))
		{
			sql.append("   and a.FI_DATE <= (to_date(?, 'yyyy-mm-dd')+1)\n" );
			par.add(map.get("eDate").toString());
		}		
		if( Utility.testString( map.get("YIELDLY_TYPE").toString()))
		{
			par.add(map.get("YIELDLY_TYPE").toString());
			sql.append("  and A.BALANCE_YIELDLY = ?\n" );
		}
		
		if( Utility.testString( map.get("supply_name").toString()))
		{
			par.add("%"+ map.get("supply_name").toString()+"%");
			sql.append(" AND DS.DEALER_NAME LIKE ?\n" );
		}
		
		if( Utility.testString( map.get("supply_code").toString()))
		{
			par.add( "%"+ map.get("supply_code").toString()+"%");
			sql.append(" AND DS.DEALER_CODE LIKE ?\n" );
		}
		if( Utility.testString( map.get("small_org").toString()))
		{
			par.add(map.get("small_org").toString());
			sql.append(" AND DS.ORG_ID = ? \n" );
		}
		if(Utility.testString( map.get("big_org").toString()))
		{
			sql.append(" AND DS.ROOT_ORG_ID = ? \n" );
			par.add(map.get("big_org").toString());
		}
		
		sql.append(" GROUP by DS.ROOT_DEALER_CODE,to_char(A.REPORT_DATE,'yyyy-mm')\n" );
		sql.append("UNION All\n" );
		sql.append("SELECT\n" );
		sql.append("       DS.ROOT_DEALER_CODE DEALER_CODE,\n" );
		sql.append("       to_char( S.MAKE_DATE, 'yyyy-mm'),\n" );
		sql.append("       '',\n" );
		sql.append("       MAX(DS.ROOT_DEALER_NAME) ROOT_DEALER_NAME,\n" );
		sql.append("       0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,\n" );
		sql.append("       count(S.ID),\n" );
		sql.append("       sum(DECODE(S.BALANCE_FEE_TYPE, 94141001, S.OFFICE_DECLARE_SUM, 0)),\n" );
		sql.append("       sum(DECODE(S.BALANCE_FEE_TYPE, 94141002, S.OFFICE_DECLARE_SUM, 0)),\n" );
		sql.append("       sum( nvl(S.OFFICE_DECLARE_SUM,0) ),\n" );
		sql.append("       0,0,0,0,0, count(S.ID),\n" );
		sql.append("        sum(DECODE(S.BALANCE_FEE_TYPE, 94141001, S.OFFICE_DECLARE_SUM, 0)),\n" );
		sql.append("        sum(DECODE(S.BALANCE_FEE_TYPE, 94141002, S.OFFICE_DECLARE_SUM, 0)),\n" );
		sql.append("       sum( nvl(S.OFFICE_DECLARE_SUM,0) )\n" );
		sql.append("     FROM vw_org_dealer_service ds,TT_AS_WR_SPEFEE S\n" );
		sql.append("      LEFT JOIN ( SELECT SAA.FEE_ID,max(SAA.STATUS) STATUS ,min(SAA.AUDITING_DATE) AUDITING_DATE  from  TT_AS_WR_SPEFEE_AUDITING SAA group by SAA.FEE_ID ) SA ON S.ID = SA.FEE_ID AND SA.STATUS = 11841010\n" );
		sql.append("     WHERE  S.STATUS = 11841010 and S.DEALER_ID = ds.DEALER_ID and ds.dealer_type = 10771002\n" );
		
		if( Utility.testString( map.get("bDate").toString()))
		{
			sql.append("     and SA.AUDITING_DATE >= to_date(?, 'yyyy-mm-dd')\n" );
			par.add(map.get("bDate").toString());
		}
		if( Utility.testString( map.get("eDate").toString()))
		{
			sql.append("     and SA.AUDITING_DATE <= to_date(?, 'yyyy-mm-dd')\n" );;
			par.add(map.get("eDate").toString());
		}		
		if( Utility.testString( map.get("YIELDLY_TYPE").toString()))
		{
			if(map.get("YIELDLY_TYPE").toString().equals("95411001"))
			{
				sql.append("  and 1=1  ");
			}else
			{
				sql.append("  and 1!=1  ");
			}
		}
		
		if( Utility.testString( map.get("supply_name").toString()))
		{
			par.add("%"+ map.get("supply_name").toString()+"%");
			sql.append(" AND DS.DEALER_NAME LIKE ?\n" );
		}
		
		if( Utility.testString( map.get("supply_code").toString()))
		{
			par.add( "%"+ map.get("supply_code").toString()+"%");
			sql.append(" AND DS.DEALER_CODE LIKE ?\n" );
		}
		if( Utility.testString( map.get("small_org").toString()))
		{
			par.add(map.get("small_org").toString());
			sql.append(" AND DS.ORG_ID = ? \n" );
		}
		if(Utility.testString( map.get("big_org").toString()))
		{
			sql.append(" AND DS.ROOT_ORG_ID = ? \n" );
			par.add(map.get("big_org").toString());
		}

		sql.append("     GROUP by DS.ROOT_DEALER_CODE , to_char( S.MAKE_DATE, 'yyyy-mm')\n" );
		sql.append("UNION ALl\n" );
		sql.append("SELECT\n" );
		sql.append("      DS.ROOT_DEALER_CODE DEALER_CODE,\n" );
		sql.append("      to_char( A.REPORT_DATE, 'yyyy-mm'),\n" );
		sql.append("       '',\n" );
		sql.append("      MAX(DS.ROOT_DEALER_NAME) ROOT_DEALER_NAME,\n" );
		sql.append("      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,\n" );
		sql.append("      count(A.ID),sum(nvl(A.AUDIT_ACOUNT,0))*-1,0\n" );
		sql.append("      ,0,0, count(A.ID),sum(nvl(A.AUDIT_ACOUNT,0))*-1,0, sum(nvl(A.AUDIT_ACOUNT,0))*-1\n" );
		sql.append("   FROM\n" );
		sql.append("  TT_AS_PACKGE_CHANGE_APPLY A ,vw_org_dealer_service ds\n" );
		sql.append("  where  A.APPLY_STATUS = 95441006\n" );
		sql.append("       and A.APPLY_BY = ds.DEALER_ID\n" );
		if( Utility.testString( map.get("YIELDLY_TYPE").toString()))
		{
			if(map.get("YIELDLY_TYPE").toString().equals("95411001"))
			{
				sql.append("  and 1=1  ");
			}else
			{
				sql.append("  and 1!=1  ");
			}
		}
		
		if( Utility.testString( map.get("supply_name").toString()))
		{
			par.add("%"+ map.get("supply_name").toString()+"%");
			sql.append(" AND DS.DEALER_NAME LIKE ?\n" );
		}
		
		if( Utility.testString( map.get("supply_code").toString()))
		{
			par.add( "%"+ map.get("supply_code").toString()+"%");
			sql.append(" AND DS.DEALER_CODE LIKE ?\n" );
		}
		if( Utility.testString( map.get("small_org").toString()))
		{
			par.add(map.get("small_org").toString());
			sql.append(" AND DS.ORG_ID = ? \n" );
		}
		if(Utility.testString( map.get("big_org").toString()))
		{
			sql.append(" AND DS.ROOT_ORG_ID = ? \n" );
			par.add(map.get("big_org").toString());
		}
		sql.append("   GROUP by DS.ROOT_DEALER_CODE , to_char( A.REPORT_DATE, 'yyyy-mm') ) M\n" );
		sql.append("   GROUP BY M.DEALER_CODE,M.REPORT_DATE ) D\n" );
		sql.append("\n" );
		sql.append("   left join  ((SELECT T.BALANCE_ODER,count(T.FINE_ID) COUNT_SUM,\n" );
		sql.append("sum(  decode ( T.FINE_TYPE ,80641002,decode(T.LABOUR_TYPE, 94141001 ,T.LABOUR_SUM,0 ) ,  decode(T.LABOUR_TYPE, 94141001 ,T.LABOUR_SUM,0 ) *-1 )   ) LABOUR_SUM,\n" );
		sql.append("sum(  decode ( T.FINE_TYPE ,80641002, decode(T.LABOUR_TYPE, 94141002 ,T.DATUM_SUM,0 ) ,decode(T.LABOUR_TYPE, 94141002 ,T.DATUM_SUM,0 )*-1 )   ) DATUM_SUM\n" );
		sql.append("  from TT_AS_WR_FINE T\n" );
		sql.append(" where T.BALANCE_ODER is not null\n" );
		sql.append(" GROUP BY T.BALANCE_ODER))  F ON F.BALANCE_ODER = D.BALANCE_ODER\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("");
	    sql.append(" LEFT JOIN TT_AS_WR_TICKETS  TAA on  TAA.GOODSNUM = ( D.DEALER_CODE ||    replace( D.REPORT_DATE, '-'))\n" );
	    sql.append("and TAA.BALANCE_YIELDLY = 95411001");
	    sql.append("  LEFT JOIN   ( select (sum( nvl( t.DISCOUNT , 0)) *-1)   DISCOUNT,  t.BALANCE_ODER  from tt_as_wr_discount t group by t.BALANCE_ODER) TD  ON  TD.BALANCE_ODER =   D.BALANCE_ODER  ");

		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), par, getFunName(),pageSize, curPage);
		return ps;
	}
	
	
	
	
	

	/**
	 * @param map
	 * @param pageSize
	 * @param curPage
	 * @return
	 * 平均故障里程查询页面
	 */
	public PageResult<Map<String, Object>> QueryAverageFaultDistance(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT S.GROUP_NAME SERIES,\n" );
		sql.append("       MG.GROUP_NAME MODEL,\n" );
		sql.append("       ROUND(SUM(DECODE(M.R, 1, M.IN_MILEAGE, 0)) / COUNT(DISTINCT VIN), 2) F_MILEAGE,\n" );
		sql.append("       ROUND(SUM(DECODE(M.DR, 1, M.IN_MILEAGE, 0)) / COUNT(VIN), 2) D_MILEAGE\n" );
		sql.append("  FROM (SELECT V.SERIES_ID,\n" );
		sql.append("               V.MODEL_ID,\n" );
		sql.append("               A.VIN,\n" );
		sql.append("               A.IN_MILEAGE,\n" );
		sql.append("               DENSE_RANK() OVER(PARTITION BY A.VIN ORDER BY A.CREATE_DATE, A.ID) R,\n" );
		sql.append("               DENSE_RANK() OVER(PARTITION BY A.VIN ORDER BY A.CREATE_DATE DESC, A.ID DESC) DR\n" );
		sql.append("          FROM TT_AS_WR_APPLICATION A, TM_VEHICLE V\n" );
		sql.append("         WHERE A.VIN = V.VIN\n" );
		sql.append("           AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_01+", "+Constant.CLA_TYPE_09+", "+Constant.CLA_TYPE_10+")\n" );
		sql.append("           AND A.STATUS >= "+Constant.CLAIM_APPLY_ORD_TYPE_02+"\n" );
		List par=new ArrayList();


		   if(!CommonUtils.isNullString(map.get("serisid"))) {
				sql.append(" AND v.series_id = ?\n"); 
				par.add(""+map.get("serisid").trim()+"");
			}
	   
	
		if(!CommonUtils.isNullString(map.get("groupCode"))) {
			
			String[] array = map.get("groupCode").toString().split(",");
	
			StringBuffer str = new StringBuffer();
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					str.append(""+getGroupIdByGroupCode(array[i])+"");
						if (i != array.length - 1) {
							str.append(",");
						}	
				}
					sql.append(Utility.getConSqlByParamForEqual(str.toString(), par,"V", "model_id"));
				}
			}

			
			if(Utility.testString(map.get("bDate"))){
				sql.append("and v.product_date>=to_date(?,'yyyy-mm-dd')\n"); 
				par.add(map.get("bDate"));

			}
		
		if(Utility.testString(map.get("eDate"))){
			sql.append("and v.product_date<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
			par.add(map.get("eDate")+" 23:59:59");
		}
		
		sql.append("        ) M\n" );
		sql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP S ON S.GROUP_ID = M.SERIES_ID AND S.GROUP_LEVEL = 2\n" );
		sql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP MG ON MG.GROUP_ID = M.MODEL_ID AND MG.GROUP_LEVEL = 3\n" );
		sql.append(" GROUP BY S.GROUP_NAME, MG.GROUP_NAME");
		
		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), par, getFunName(),pageSize, curPage);
		return ps;
	}
	public String getGroupIdByGroupCode(String groupCode){
		String sql="select  t.MODEL_ID  from vw_material_group_mat t where t.MODEL_CODE='"+ groupCode +"'";
		List<Map<String,Object>> list = null;
		String GroupId = "";
		try {
			list = this.pageQuery(sql, null, getFunName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list!=null && list.size()>0) GroupId = list.get(0).get("MODEL_ID").toString();
		return GroupId;
	}
	//维修频次
	public PageResult<Map<String, Object>> QueryMaintenanceFrequency(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		
		/**
		 *  艾春 2013.11.25 修改维修频次统计结构SQL, 把原有的存在关联修改为了全关联
		 *  
		 */
		List par=new ArrayList();		
		par.add(Integer.parseInt(map.get("bgDate")));
		par.add(Integer.parseInt(map.get("bgDate")));
		
		StringBuffer sql= new StringBuffer();
		
		sql.append("SELECT T.GROUP_NAME,\n" );
		sql.append("\n" );
		sql.append("       count(DISTINCT v.vin) s_total, /*销售台数*/\n" );
		sql.append("       COUNT(a.vin) r_total, /*维修台次*/\n" );
		sql.append("      	round(sum(NVL(CASE WHEN a.di_date BETWEEN v.purchased_date AND v.purchased_date + ? /*销售日期查询条件,若有抵扣在选定区间,需扣掉*/\n" );
		sql.append("         THEN NVL(a.apply_repair_total,0)- decode(NVL(a.discount,0),0,0,-1*NVL(a.discount,0))\n" );
		sql.append("         ELSE NVL(a.apply_repair_total,0) END,0)),2) r_amount, /*总费用*/\n" );
		sql.append("    	 round(COUNT(a.vin)/count(DISTINCT v.vin),4) rd_times, /*单车频次=维修台次/销售台数*/\n" );
		sql.append("		round(sum(NVL(CASE WHEN a.di_date BETWEEN v.purchased_date AND v.purchased_date + ? /*销售日期查询条件*/\n" );
		sql.append("         THEN NVL(a.apply_repair_total,0) - NVL(a.discount,0)\n" );
		sql.append("         ELSE NVL(a.apply_repair_total,0) END,0))/count(DISTINCT v.vin),2) rd_amount/*单车费用=总费用/销售台数*/");
		sql.append("  FROM TM_VEHICLE V\n" );
		sql.append("  JOIN tt_dealer_actual_sales das ON v.vehicle_id = das.vehicle_id AND das.Is_Return ="+Constant.IF_TYPE_NO+"  /*未退车*/\n" );
		sql.append("  LEFT  JOIN TM_VHCL_MATERIAL_GROUP T ON T.GROUP_ID=V.SERIES_ID AND T.GROUP_LEVEL = 2\n" );
		sql.append("  LEFT JOIN ( SELECT C.ID, C.VIN, C.REPORT_DATE, C.SERIES_CODE, C.STATUS, C.CLAIM_TYPE, C.APPLY_REPAIR_TOTAL, C.DISCOUNT,\n" );
		sql.append("   C.DI_DATE, DENSE_RANK() OVER(PARTITION BY C.ID ORDER BY P.PART_ID) R \n" );
		sql.append("   FROM TT_AS_WR_APPLICATION C \n" );
		// 艾春 2013.11.27 修改配件为左关联, 如果没有配件也可查询出来
		sql.append("   LEFT JOIN TT_AS_WR_PARTSITEM P ON C.ID = P.ID \n" );
		if (null != map.get("partcode") && !"".equals(map.get("partcode"))) {
			String[] array = map.get("partcode").toString().split(",");
			StringBuffer str = new StringBuffer();
			if(array.length>0){
				sql.append(" AND p.part_code IN ?\n");
				str.append("(");
				for (int i = 0; i < array.length; i++) {
					str.append("'"+array[i]+"'");
						if (i != array.length - 1) {
							str.append(",");
						}	
				}
				str.append(")");
				par.add(str.toString());
			}
		}
		sql.append("   WHERE C.STATUS >= "+Constant.CLAIM_APPLY_ORD_TYPE_02+" /*已上报的索赔单*/ \n" );
		sql.append("   AND C.CLAIM_TYPE IN ("+Constant.CLA_TYPE_01+", "+Constant.CLA_TYPE_07+", "+Constant.CLA_TYPE_09+") /*正常/外派/售前*/\n" );
		
		if(!CommonUtils.isNullString(map.get("clatype"))) {
			sql.append(Utility.getConSqlByParamForEqual(map.get("clatype"), par,"C", "claim_type"));
		}
		
		sql.append("   ) A ON v.vin = a.vin AND A.R = 1\n" );

		if(Utility.testString(map.get("bgDate"))){
			sql.append(" AND a.report_date BETWEEN v.purchased_date AND v.purchased_date + ?/*销售日期标准过滤*/\n");
			par.add(""+map.get("bgDate")+"");
		}
		
		sql.append(" WHERE 1= 1\n" );
		if(Utility.testString(map.get("bgDate"))){
			sql.append("and v.purchased_date<= trunc(SYSDATE)+1-1/24/60/60-?\n");
	
			par.add(Integer.parseInt(map.get("bgDate")));
		}
		
		if(Utility.testString(map.get("bDate"))){
			sql.append("and v.product_date>=to_date(?,'yyyy-mm-dd')\n"); 
			par.add(map.get("bDate"));

		}
		if(Utility.testString(map.get("eDate"))){
			sql.append("and v.product_date<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
			par.add(map.get("eDate")+" 23:59:59");
		}

	   if(!CommonUtils.isNullString(map.get("serisid"))) {
			sql.append(" AND v.series_id = ?\n"); 
			par.add(""+map.get("serisid").trim()+"");
		}

		sql.append(" GROUP BY T.GROUP_NAME");
		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), par, getFunName(),pageSize, curPage);
		return ps;
	}
	/**
	 * @param map
	 * @param pageSize
	 * @param curPage
	 * @return
	 * 出门证及索赔通知单查询
	 */
	public PageResult<Map<String, Object>> QueryShipperClaimQuery(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT D.OUT_NO, /*出门证号*/\n" );
		sql.append("       TO_CHAR(D.CREATE_DATE, 'YYYY-MM-DD') DOOR_DATE, /*出门证生成日期*/\n" );
		sql.append("       D.OUT_COMPANY, /*出门单位*/\n" );
		sql.append("       DD.MODEL_NAME, /*型号*/\n" );
		sql.append("       DD.PART_CODE, /*配件代码*/\n" );
		sql.append("       DD.PART_NAME, /*配件名称*/\n" );
		sql.append("       DD.OUT_NUM, /*出门证数量*/\n" );
		sql.append("       N.NOTICE_NO, /*索赔通知单号*/\n" );
		sql.append("       TO_CHAR(N.CREATE_DATE,'YYYY-MM-DD') NOTICE_DATE, /*开票时间*/\n" );
		sql.append("       ND.OUT_NUM OUT_NUM1, /*索赔数量*/\n" );
		sql.append("       ND.CLAIM_PRICE PART_PRICE, /*索赔单价*/\n" );
		sql.append("       ND.LABOUR_PRICE, /*工时定额*/\n" );
		sql.append("       ND.TOTAL, /*合计金额*/\n" );
		sql.append("        TC.NAME, /*创建人*/\n" );
		sql.append("       DD.OUT_REMARK /*备注*/\n" );
		sql.append("  FROM TT_AS_WR_OLD_OUT_DOOR D\n" );
		sql.append("  JOIN TT_AS_WR_OLD_OUT_DOOR_DETAIL DD ON D.DOOR_ID = DD.DOOR_ID\n" );
		sql.append("  LEFT JOIN TT_AS_WR_OLD_OUT_NOTICE N ON D.OUT_NO = N.OUT_NO\n" );
		sql.append("  LEFT JOIN TT_AS_WR_OLD_OUT_NOTICE_DETAIL ND ON N.NOTICE_ID = ND.NOTICE_ID\n" );
		sql.append("   LEFT JOIN TC_USER TC ON TC.USER_ID=N.CREATE_BY \n" );
		sql.append("   AND  ND.PART_CODE = DD.PART_CODE AND ND.MODEL_NAME = DD.MODEL_NAME\n" );
		sql.append(" WHERE 1 =1\n" );
		List par=new ArrayList();

		if(!CommonUtils.isNullString(map.get("yieldlyType"))) {
			sql.append(" and D.YIELDLY = ?\n"); 
			par.add(Integer.parseInt(map.get("yieldlyType").trim()));
		}

		if(!CommonUtils.isNullString(map.get("modelName"))) {
			sql.append(" and DD.MODEL_NAME LIKE  ?\n"); 
			par.add("%"+map.get("modelName").trim()+"%");
		}

		if(!CommonUtils.isNullString(map.get("outCompany"))) {
			sql.append(" and  D.OUT_COMPANY LIKE  ?\n"); 
			par.add("%"+map.get("outCompany").trim()+"%");
		}

		if(!CommonUtils.isNullString(map.get("noticeNo"))) {
			sql.append(" and  N.NOTICE_NO LIKE  ?\n"); 
			par.add("%"+map.get("noticeNo").trim()+"%");
		}

		if(Utility.testString(map.get("bDate"))){
			sql.append("and D.CREATE_DATE>=to_date(?,'yyyy-mm-dd')\n"); 
			par.add(map.get("bDate"));

		}
		if(Utility.testString(map.get("eDate"))){
			sql.append("and D.CREATE_DATE<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
			par.add(map.get("eDate")+" 23:59:59");
		}
		sql.append("UNION ALL\n" );
		sql.append("SELECT N.OUT_NO,\n" );
		sql.append("       NULL,\n" );
		sql.append("       N.NOTICE_COMPANY,\n" );
		sql.append("       ND.MODEL_NAME,\n" );
		sql.append("       ND.PART_CODE,\n" );
		sql.append("       ND.PART_NAME,\n" );
		sql.append("       NULL,\n" );
		sql.append("       N.NOTICE_NO,\n" );
		sql.append("       TO_CHAR(N.CREATE_DATE,'YYYY-MM-DD') NOTICE_DATE, /*开票时间*/\n" );
		sql.append("       ND.OUT_NUM OUT_NUM1, /*索赔数量*/\n" );
		sql.append("       ND.PART_PRICE PART_PRICE, /*索赔单价*/\n" );
		sql.append("       ND.LABOUR_PRICE, /*工时定额*/\n" );
		sql.append("       ND.TOTAL, /*合计金额*/\n" );
		sql.append("       TC.NAME, /*创建人*/\n" );
		sql.append("       NULL\n" );
		sql.append("  FROM TT_AS_WR_OLD_OUT_NOTICE N\n" );
		sql.append("  JOIN TT_AS_WR_OLD_OUT_NOTICE_DETAIL ND ON N.NOTICE_ID = ND.NOTICE_ID\n" );
		sql.append("  LEFT JOIN TC_USER TC ON TC.USER_ID=N.CREATE_BY \n" );
		sql.append(" WHERE N.OUT_NO IS NULL\n" );

		if(!CommonUtils.isNullString(map.get("yieldlyType"))) {
			sql.append(" and N.YIELDLY = ?\n"); 
			par.add(Integer.parseInt(map.get("yieldlyType").trim()));
		}
		

		if(!CommonUtils.isNullString(map.get("modelName"))) {
			sql.append(" and  ND.MODEL_NAME  LIKE  ?\n"); 
			par.add("%"+map.get("modelName").trim()+"%");
		}


		if(!CommonUtils.isNullString(map.get("outCompany"))) {
			sql.append(" and  N.NOTICE_COMPANY LIKE  ?\n"); 
			par.add("%"+map.get("outCompany").trim()+"%");
		}

		if(!CommonUtils.isNullString(map.get("noticeNo"))) {
			sql.append(" and  N.NOTICE_NO LIKE  ?\n"); 
			par.add("%"+map.get("noticeNo").trim()+"%");
		}

		if(Utility.testString(map.get("bDate"))){
			sql.append("and N.CREATE_DATE>=to_date(?,'yyyy-mm-dd')\n"); 
			par.add(map.get("bDate"));

		}
		if(Utility.testString(map.get("eDate"))){
			sql.append("and N.CREATE_DATE<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
			par.add(map.get("eDate")+" 23:59:59");
		}

		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), par, getFunName(),pageSize, curPage);
		return ps;
	}
	
	
	//二次索赔开票查询
	public PageResult<Map<String, Object>> TwoClaimInvoiceQuery(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TO_CHAR(N.CREATE_DATE,'YYYY-MM-DD') NOTICE_DATE, /*开票时间*/\n" );
		sql.append("       N.NOTICE_NO, /*索赔通知单号*/\n" );
		sql.append("       N.NOTICE_CODE, /*供应商代码*/\n" );
		sql.append("       MD.MAKER_NAME, /*供应商名称*/\n" );
		sql.append("       ND.MODEL_NAME, /*车型*/\n" );
		sql.append("       ND.PART_CODE, /*配件代码*/\n" );
		sql.append("       ND.PART_NAME, /*配件名称*/\n" );
		sql.append("       ND.OUT_NUM, /*配件数量*/\n" );
		sql.append("       ND.CLAIM_PRICE, /*索赔单价*/\n" );
		sql.append("       ND.PART_PRICE, /*配件材料费*/\n" );
		sql.append("       ND.CLAIM_LABOUR, /*工时*/\n" );
		sql.append("       ND.LABOUR_PRICE, /*三包工时费*/\n" );
		sql.append("       ND.OTHER_PRICE, /*包装托运费*/\n" );
		sql.append("       ND.SMALL_TOTAL, /*小计*/\n" );
		sql.append("       ND.TAX_TOTAL, /*税额*/\n" );
		sql.append("       ND.TOTAL /*金额总计*/\n" );
		sql.append("  FROM TT_AS_WR_OLD_OUT_NOTICE N\n" );
		sql.append("  JOIN TT_AS_WR_OLD_OUT_NOTICE_DETAIL ND ON N.NOTICE_ID = ND.NOTICE_ID\n" );
		sql.append("  JOIN TT_PART_MAKER_DEFINE MD ON N.NOTICE_CODE = MD.MAKER_CODE\n" );
		sql.append(" WHERE 1 = 1\n" );

		List par=new ArrayList();

		if(!CommonUtils.isNullString(map.get("yieldlyType"))) {
			sql.append(" and N.YIELDLY = ?\n"); 
			par.add(Integer.parseInt(map.get("yieldlyType").trim()));
		}

		if(!CommonUtils.isNullString(map.get("modelName"))) {
			sql.append(" and  ND.MODEL_NAME LIKE  ?\n"); 
			par.add("%"+map.get("modelName").trim()+"%");
		}

		if(!CommonUtils.isNullString(map.get("supplyName"))) {
			sql.append(" and  MD.MAKER_NAME LIKE  ?\n"); 
			par.add("%"+map.get("supplyName").trim()+"%");
		}

		if(!CommonUtils.isNullString(map.get("supplyCode"))) {
			sql.append(" and  N.NOTICE_CODE LIKE  ?\n"); 
			par.add("%"+map.get("supplyCode").trim()+"%");
		}
		
		if(Utility.testString(map.get("bDate"))){
			sql.append("and N.CREATE_DATE>=to_date(?,'yyyy-mm-dd')\n"); 
			par.add(map.get("bDate"));

		}
		if(Utility.testString(map.get("eDate"))){
			sql.append("and N.CREATE_DATE<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
			par.add(map.get("eDate")+" 23:59:59");
		}

		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), par, getFunName(),pageSize, curPage);
		return ps;
	}
	
	
	//二次索赔开票汇总查询
	public PageResult<Map<String, Object>> TwoClaimInvoiceTotalQuery(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT N.NOTICE_CODE, /*供应商代码*/\n" );
		sql.append("       MD.MAKER_NAME, /*供应商名称*/\n" );
		sql.append("       ND.MODEL_NAME, /*车型*/\n" );
		sql.append("       SUM(ND.OUT_NUM) OUT_NUM, /*索赔数量*/\n" );
		sql.append("       SUM(ND.PART_PRICE) PART_PRICE, /*配件材料费*/\n" );
		sql.append("       SUM(ND.LABOUR_PRICE) LABOUR_PRICE, /*三包工时费*/\n" );
		sql.append("       SUM(ND.OTHER_PRICE) OTHER_PRICE, /*包装托运费*/\n" );
		sql.append("       SUM(ND.TOTAL) AMOUNT /*金额总计*/\n" );
		sql.append("  FROM TT_AS_WR_OLD_OUT_NOTICE N\n" );
		sql.append("  JOIN TT_AS_WR_OLD_OUT_NOTICE_DETAIL ND ON N.NOTICE_ID = ND.NOTICE_ID\n" );
		sql.append("  JOIN TT_PART_MAKER_DEFINE MD ON N.NOTICE_CODE = MD.MAKER_CODE\n" );
		sql.append(" WHERE 1 = 1\n" );
		List par=new ArrayList();
		if(!CommonUtils.isNullString(map.get("yieldlyType"))) {
			sql.append(" and N.YIELDLY = ?\n"); 
			par.add(Integer.parseInt(map.get("yieldlyType").trim()));
		}

		if(!CommonUtils.isNullString(map.get("modelName"))) {
			sql.append(" and  ND.MODEL_NAME LIKE  ?\n"); 
			par.add("%"+map.get("modelName").trim()+"%");
		}

		if(!CommonUtils.isNullString(map.get("supplyName"))) {
			sql.append(" and  MD.MAKER_NAME LIKE  ?\n"); 
			par.add("%"+map.get("supplyName").trim()+"%");
		}

		if(!CommonUtils.isNullString(map.get("supplyCode"))) {
			sql.append(" and  N.NOTICE_CODE LIKE  ?\n"); 
			par.add("%"+map.get("supplyCode").trim()+"%");
		}
		
		if(Utility.testString(map.get("bDate"))){
			sql.append("and N.CREATE_DATE>=to_date(?,'yyyy-mm-dd')\n"); 
			par.add(map.get("bDate"));

		}
		if(Utility.testString(map.get("eDate"))){
			sql.append("and N.CREATE_DATE<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
			par.add(map.get("eDate")+" 23:59:59");
		}
		sql.append("GROUP BY N.NOTICE_CODE, MD.MAKER_NAME, ND.MODEL_NAME");

		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), par, getFunName(),pageSize, curPage);
		return ps;
	}
	//结算数量金额明细查询
	public PageResult<Map<String, Object>> SettlementAmountSubsidiaryQuery(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		 StringBuffer sql= new StringBuffer();
		 List par=new ArrayList();
		    sql.append("SELECT Z.SERIES_NAME, /*车系*/\n" );
		    sql.append("      Z.PART_CODE, /*配件代码*/\n" );
		    sql.append("      Z.PART_NAME, /*配件名称*/\n" );
		    sql.append("      za_concat( Z.mal_name || '--' || Z.BALANCE_QUANTITY ) mal_name , /*故障名称*/\n" );
		    sql.append("      za_concat( Z.maker_name || '--' || Z.BALANCE_QUANTITY ) maker_name , /*供应商*/\n" );
		    sql.append("     sum( Z.BALANCE_QUANTITY) BALANCE_QUANTITY, /*结算数量*/\n" );
		    sql.append("     sum( Z.BALANCE_AMOUNT) BALANCE_AMOUNT /*结算金额*/ from   (  SELECT y.SERIES_NAME, /*车系*/\n" );
		    sql.append("       y.PART_CODE, /*配件代码*/\n" );
		    sql.append("       y.PART_NAME, /*配件名称*/\n" );
		    sql.append("       wm.mal_name, /*故障名称*/\n" );
		    sql.append("       NVL(md.maker_name,y.producer_name) maker_name, /*供应商*/\n" );
		    sql.append("       BALANCE_QUANTITY, /*结算数量*/\n" );
		    sql.append("       BALANCE_AMOUNT /*结算金额*/\n" );
		    sql.append(" FROM ( SELECT K.SERIES_NAME,\n" );
		    sql.append("               K.PART_CODE,\n" );
		    sql.append("               K.PART_NAME,\n" );
		    sql.append("               K.TROUBLE_CODE,\n" );
		    sql.append("               K.PRODUCER_CODE,\n" );
		    sql.append("               max(K.producer_name) producer_name,\n" );
		    sql.append("               sum(K.BALANCE_QUANTITY) BALANCE_QUANTITY,\n" );
		    sql.append("               sum(K.BALANCE_AMOUNT + decode (K.r,1,K.L_BALANCE_AMOUNT,0 ) ) BALANCE_AMOUNT\n" );
		    sql.append("             from   ( SELECT G.GROUP_NAME SERIES_NAME,\n" );
		    sql.append("             P.DOWN_PART_CODE PART_CODE,\n" );
		    sql.append("             PB.PART_NAME,\n" );
		    sql.append("             L.TROUBLE_CODE,\n" );
		    sql.append("             P.PRODUCER_CODE,\n" );
		    sql.append("             MAX(p.producer_name) producer_name,\n" );
		    sql.append("             SUM(NVL(P.BALANCE_QUANTITY,0)) BALANCE_QUANTITY,\n" );
		    sql.append("             SUM(NVL(P.BALANCE_AMOUNT,0)) BALANCE_AMOUNT,\n" );
		    sql.append("             sum(nvl(l.BALANCE_AMOUNT,0))  L_BALANCE_AMOUNT,\n" );
		    sql.append("             DENSE_RANK() OVER(PARTITION BY A.ID, L.WR_LABOURCODE ORDER BY min(P.PART_ID)) r\n" );
		    sql.append("        FROM TT_AS_WR_APPLICATION A\n" );
		    sql.append("        JOIN TT_AS_WR_LABOURITEM L ON A.ID = L.ID\n" );
		    sql.append("        JOIN TT_AS_WR_PARTSITEM P ON A.ID = P.ID AND P.WR_LABOURCODE = L.WR_LABOURCODE\n" );
		    sql.append("        LEFT JOIN TM_VHCL_MATERIAL_GROUP G ON A.SERIES_CODE = G.GROUP_CODE AND G.GROUP_LEVEL = 2\n" );
		    sql.append("        LEFT JOIN TM_PT_PART_BASE PB ON P.DOWN_PART_CODE = PB.PART_CODE\n" );
		    sql.append("       WHERE A.STATUS = 10791013\n" );
		    sql.append("  and A.CLAIM_TYPE != 10661006   ");
		    sql.append("        AND A.BALANCE_YIELDLY = 95411001 /*结算基地筛选*/\n" );
		    
		    if(Utility.testString(map.get("bDate"))){
		          sql.append("and A.FI_DATE>=to_date(?,'yyyy-mm-dd')\n"); 
		          par.add(map.get("bDate"));

		        }
		        if(Utility.testString(map.get("eDate"))){
		          sql.append("and A.FI_DATE<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
		          par.add(map.get("eDate")+" 23:59:59");
		        }
		    
		    sql.append("       GROUP BY G.GROUP_NAME, P.DOWN_PART_CODE, PB.PART_NAME, L.TROUBLE_CODE, P.PRODUCER_CODE,A.ID,L.WR_LABOURCODE ) K group by  K.SERIES_NAME, K.PART_CODE, K.PART_NAME, K.TROUBLE_CODE, K.PRODUCER_CODE  ) Y\n" );
		    sql.append("  LEFT JOIN tt_as_wr_malfunction wm ON y.TROUBLE_CODE = wm.mal_id\n" );
		    sql.append("  LEFT JOIN tt_part_maker_define md ON y.PRODUCER_CODE = md.maker_code Order by 1,2 ) Z\n" );
		    sql.append(" group by Z.SERIES_NAME,Z.PART_CODE,Z.PART_NAME");


		    PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), par, getFunName(),pageSize, curPage);
		    return ps;
	}
	
	/**
	 * 结算汇总明细
	 * @param map 
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> SettlementSummaryDelQuery(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		
		
		StringBuffer sql= new StringBuffer();
		List par=new ArrayList();

		sql.append("-- 条码补办/三包凭证/特殊费用 结算明细统计SQL\n" );
		sql.append("SELECT m.TYPE, /*维修类型*/\n" );
		sql.append("       m.ORDER_NO, /*单据号码*/\n" );
		sql.append("       ds.root_dealer_code, /*一级站代码*/\n" );
		sql.append("       ds.dealer_code, /*服务站代码*/\n" );
		sql.append("       m.ACTIVITY_NAME, /*活动名称*/\n" );
		sql.append("       vs.group_name SERIES, /*车系*/\n" );
		sql.append("       vm.group_code MODEL, /*车型*/\n" );
		sql.append("       v.engine_no, /*发动机号*/\n" );
		sql.append("       v.VIN, /*VIN*/\n" );
		sql.append("       to_char(v.purchased_date,'yyyy-mm-dd') buy_Date, /*销售日期*/\n" );
		sql.append("       to_char(m.FINE_DATE,'yyyy-mm-dd') FINE_DATE, /*维修日期*/\n" );
		sql.append("       m.part_code,/*换上件代码*/\n" );
		sql.append("       m.PART_NAME, /*换上件名称*/\n" );
		sql.append("       m.NUM, /*数量*/\n" );
		sql.append("       m.LABOUR_CODE, /*工时代码*/\n" );
		sql.append("       m.LABOUR_NAME, /*工时名称*/\n" );
		sql.append("       m.LABOUR_SUM, /*上报工时费*/\n" );
		sql.append("       m.PART_SUM, /*上报材料费*/\n" );
		sql.append("       m.SEND_SUM, /*上报派出费*/\n" );
		sql.append("       m.FREE_SUM, /*上报赠送费*/\n" );
		sql.append("       m.B_FREE_SUM, /*赠送费用*/\n" );
		sql.append("       m.B_SEND_SUM, /*外派费*/\n" );
		sql.append("       m.B_PART_SUM, /*材料费*/\n" );
		sql.append("       m.B_LABOUR_SUM, /*工时费*/\n" );

		sql.append("       m.OTHER_SUM, /*其他费用*/\n" );
		sql.append("       decode(m.BALANCE_YIELD,"+Constant.PART_IS_CHANGHE_01+",'昌河',"+Constant.PART_IS_CHANGHE_02+",'东安','') BALANCE_YIELD, /*结算基地*/\n" );
		sql.append("       to_char(m.FI_DATE,'yyyy-mm') FI_DATE, /*系统确认时间*/\n" );
		sql.append("       m.REMARK /*备注*/\n" );
		sql.append("        FROM (\n" );
		sql.append("      /*条码补办*/\n" );
		sql.append("      SELECT '条码补办' TYPE ,'BB' || BA.ID ORDER_NO, ba.APPLY_BY DEALER_ID,'条码补办费用' ACTIVITY_NAME, ba.vin,\n" );
		sql.append("             BA.APPLY_DATE FINE_DATE,'' PART_CODE,'' PART_NAME, 0 NUM,'' LABOUR_CODE,'' LABOUR_NAME,\n" );
		sql.append("             0 LABOUR_SUM,\n" );
		sql.append("             0 PART_SUM,0 SEND_SUM,0 FREE_SUM,0 B_FREE_SUM,0 B_PART_SUM,\n" );
		sql.append("             0 B_LABOUR_SUM,\n" );
		sql.append("             0 B_SEND_SUM,\n" );
		sql.append("             DECODE(BA.APPLY_STATUS, "+Constant.BARCODE_APPLY_STATUS_04+", DECODE(NVL(BA.AUDIT_ACOUNT, 0) , 0, 0, -1 * NVL(BA.AUDIT_ACOUNT, 0)), 0)  OTHER_SUM,\n" );
		sql.append("             0 TRAN_SUM,"+Constant.PART_IS_CHANGHE_01+" BALANCE_YIELD,BA.AUDIT_DATE FI_DATE,'' REMARK\n" );
		sql.append("        FROM TT_AS_BARCODE_APPLY BA\n" );
		sql.append("       WHERE BA.APPLY_STATUS = "+Constant.BARCODE_APPLY_STATUS_04+"\n" );
		sql.append("      UNION ALL /*三包凭证*/\n" );
		sql.append("      SELECT '三包凭证补办','PZ'||PA.ID, pa.APPLY_BY,'三包凭证补办费用', pa.vin,\n" );
		sql.append("             PA.APPLY_DATE,'','',0,'','',\n" );
		sql.append("             0,0,0,0,0,0, 0, 0,\n" );
		sql.append("             DECODE(PA.APPLY_STATUS, "+Constant.PACKGE_CHANGE_STATUS_06+", DECODE(NVL(PA.AUDIT_ACOUNT,0),0,0,-1*NVL(PA.AUDIT_ACOUNT,0)), 0),\n" );
		sql.append("             0,95411001 BALANCE_YIELD,PD.AUDIT_DATE,''\n" );
		sql.append("       FROM TT_AS_PACKGE_CHANGE_APPLY PA\n" );
		sql.append("        LEFT JOIN TT_AS_PACKGE_CHANGE_DETAIL PD ON PA.ID = PD.APPLY_ID AND PD.AUDIT_STATUS = "+Constant.PACKGE_CHANGE_STATUS_06+"\n" );
		sql.append("       WHERE PA.APPLY_STATUS = "+Constant.PACKGE_CHANGE_STATUS_06+"\n" );
		sql.append("      UNION ALL /*特殊费用*/\n" );
		sql.append("      SELECT ft.code_desc, FEE_NO, s.dealer_id, '', s.vin,\n" );
		sql.append("             s.make_date, s.part_code, s.part_name, 1, '', '',\n" );
		sql.append("             DECODE(S.BALANCE_FEE_TYPE, "+Constant.TAX_RATE_FEE_01+", S.DECLARE_SUM1, 0),\n" );
		sql.append("             DECODE(S.BALANCE_FEE_TYPE, "+Constant.TAX_RATE_FEE_02+", S.DECLARE_SUM1, 0),\n" );
		sql.append("             0,0,0,DECODE(S.BALANCE_FEE_TYPE, "+Constant.TAX_RATE_FEE_02+", S.OFFICE_DECLARE_SUM, 0),\n" );
		sql.append("             DECODE(S.BALANCE_FEE_TYPE, "+Constant.TAX_RATE_FEE_01+", S.OFFICE_DECLARE_SUM, 0),0,0,0,"+Constant.PART_IS_CHANGHE_01+",SA.AUDITING_DATE,''\n" );
		sql.append("       FROM TT_AS_WR_SPEFEE S\n" );
		sql.append("        LEFT JOIN ( SELECT SAA.FEE_ID,max(SAA.STATUS) STATUS ,min(SAA.AUDITING_DATE) AUDITING_DATE  from  TT_AS_WR_SPEFEE_AUDITING SAA group by SAA.FEE_ID ) SA ON S.ID = SA.FEE_ID AND SA.STATUS = "+Constant.SPEFEE_STATUS_10+"\n" );
		sql.append("        LEFT JOIN tc_code ft ON s.fee_type = ft.code_id AND ft.TYPE = "+Constant.FEE_TYPE+"\n" );
		sql.append("       WHERE S.FEE_TYPE != "+Constant.FEE_TYPE_02+"\n" );
		sql.append("        AND S.STATUS = "+Constant.SPEFEE_STATUS_10+") M\n" );
		sql.append("  JOIN vw_org_dealer_service ds ON m.dealer_id = ds.dealer_id AND ds.dealer_type = "+Constant.DEALER_TYPE_DWR+"\n" );
		sql.append("  LEFT JOIN TM_VEHICLE V ON m.VIN = V.VIN\n" );
		sql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP vs ON V.SERIES_ID = vs.GROUP_ID AND vs.GROUP_LEVEL = 2\n" );
		sql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP vm ON V.MODEL_ID = vm.GROUP_ID AND vm.GROUP_LEVEL = 3\n" );
		sql.append(" WHERE 1 = 1\n" );

		   if(!CommonUtils.isNullString(map.get("serisid"))) {
				sql.append(" AND v.series_id = ?\n"); 
				par.add(""+map.get("serisid").trim()+"");
			}
	   
	
		if(!CommonUtils.isNullString(map.get("groupCode"))) {
			
			String[] array = map.get("groupCode").toString().split(",");
	
			StringBuffer str = new StringBuffer();
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					str.append(""+getGroupIdByGroupCode(array[i])+"");
						if (i != array.length - 1) {
							str.append(",");
						}	
				}
					sql.append(Utility.getConSqlByParamForEqual(str.toString(), par,"V", "model_id"));
				}
			}

		   if(!CommonUtils.isNullString(map.get("yieldlyType"))) {
				sql.append(" AND m.BALANCE_YIELD = ?\n"); 
				par.add(""+map.get("yieldlyType").trim()+"");
			}
			if(!CommonUtils.isNullString(map.get("settlementNo"))) {
				sql.append(" and  m.ORDER_NO LIKE  ?\n"); 
				par.add("%"+map.get("settlementNo").trim()+"%");
			}
			
			if(Utility.testString(map.get("bDate"))){
				sql.append("and m.fi_date >=to_date(?,'yyyy-mm-dd')\n"); 
				par.add(map.get("bDate"));

			}
			if(Utility.testString(map.get("eDate"))){
				sql.append("and m.fi_date <=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
				par.add(map.get("eDate")+" 23:59:59");
			}
			if(!CommonUtils.isNullString(map.get("supplyCode"))) {
				sql.append(" and  ds.dealer_code LIKE  ?\n"); 
				par.add("%"+map.get("supplyCode").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("supplyName"))) {
				sql.append(" and  ds.dealer_name LIKE  ?\n"); 
				par.add("%"+map.get("supplyName").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("partCode"))) {
				sql.append(" and   m.part_code LIKE  ?\n"); 
				par.add("%"+map.get("partCode").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("partName"))) {
				sql.append(" and  m.part_name LIKE  ?\n"); 
				par.add("%"+map.get("partName").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("homeworkCode"))) {
				sql.append(" and   m.LABOUR_CODE LIKE  ?\n"); 
				par.add("%"+map.get("homeworkCode").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("homeworkName"))) {
				sql.append(" and  m.LABOUR_NAME LIKE  ?\n"); 
				par.add("%"+map.get("homeworkName").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("vin"))) {
				sql.append(" and  v.vin LIKE  ?\n"); 
				par.add("%"+map.get("vin").trim()+"%");
			}

		sql.append("UNION ALL\n" );
		sql.append("SELECT m.TYPE, /*维修类型*/\n" );
		sql.append("       m.ORDER_NO, /*单据号码*/\n" );
		sql.append("       ds.root_dealer_code, /*一级站代码*/\n" );
		sql.append("       ds.dealer_code, /*服务站代码*/\n" );
		sql.append("       m.ACTIVITY_NAME, /*活动名称*/\n" );
		sql.append("       m.SERIES, /*车系*/\n" );
		sql.append("       m.MODEL, /*车型*/\n" );
		sql.append("       m.engine_no, /*发动机号*/\n" );
		sql.append("       m.VIN, /*VIN*/\n" );
		sql.append("       m.BUY_DATE, /*销售日期*/\n" );
		sql.append("       to_char(m.FINE_DATE,'yyyy-mm-dd') FINE_DATE, /*维修日期*/\n" );
		sql.append("       m.part_code,/*换上件代码*/\n" );
		sql.append("       m.PART_NAME, /*换上件名称*/\n" );
		sql.append("       m.NUM, /*数量*/\n" );
		sql.append("       m.LABOUR_CODE, /*工时代码*/\n" );
		sql.append("       m.LABOUR_NAME, /*工时名称*/\n" );
		sql.append("       m.LABOUR_SUM, /*上报工时费*/\n" );
		sql.append("       m.PART_SUM, /*上报材料费*/\n" );
		sql.append("       m.SEND_SUM, /*上报派出费*/\n" );
		sql.append("       m.FREE_SUM, /*上报赠送费*/\n" );
		sql.append("       m.B_FREE_SUM, /*赠送费用*/\n" );
		sql.append("       m.B_SEND_SUM, /*外派费*/\n" );
		sql.append("       m.B_PART_SUM, /*材料费*/\n" );
		sql.append("       m.B_LABOUR_SUM, /*工时费*/\n" );

		sql.append("       m.OTHER_SUM, /*其他费用*/\n" );
		sql.append("       decode(m.BALANCE_YIELD,"+Constant.PART_IS_CHANGHE_01+",'昌河',"+Constant.PART_IS_CHANGHE_02+",'东安','') BALANCE_YIELD, /*结算基地*/\n" );
		sql.append("       to_char(m.FI_DATE,'yyyy-mm') FI_DATE, /*系统确认时间*/\n" );
		sql.append("       m.REMARK /*备注*/\n" );
		sql.append(" FROM (\n" );
		sql.append("      SELECT '正负激励' TYPE, TO_CHAR(F.LABOUR_BH) ORDER_NO, F.DEALER_ID, /*正负激励*/\n" );
		sql.append("             F.REMARK ACTIVITY_NAME,'' SERIES,'' MODEL,'' ENGINE_NO,'' VIN,\n" );
		sql.append("             '' BUY_DATE,F.CREATE_DATE FINE_DATE,'' PART_CODE,'' PART_NAME,\n" );
		sql.append("             0 NUM,'' LABOUR_CODE,'' LABOUR_NAME,decode(F.FINE_TYPE,80641002 ,NVL(F.LABOUR_SUM, 0), NVL(F.LABOUR_SUM, 0) *-1)   LABOUR_SUM,\n" );
		sql.append("             decode( F.FINE_TYPE,80641002 ,  NVL(F.DATUM_SUM, 0),  NVL(F.DATUM_SUM, 0)*-1 )  PART_SUM,0 SEND_SUM,0 FREE_SUM,0 B_FREE_SUM,\n" );
		sql.append("             decode( F.FINE_TYPE,80641002 , DECODE(F.FI_DATE, NULL, 0, NVL(F.DATUM_SUM, 0)),  DECODE(F.FI_DATE, NULL, 0, NVL(F.DATUM_SUM, 0)) *-1 )   B_PART_SUM,\n" );
		sql.append("             decode( F.FINE_TYPE,80641002 ,DECODE(F.FI_DATE, NULL, 0, NVL(F.LABOUR_SUM, 0)) ,DECODE(F.FI_DATE, NULL, 0, NVL(F.LABOUR_SUM, 0))*-1 )    B_LABOUR_SUM,0 B_SEND_SUM,0 OTHER_SUM,\n" );
		sql.append("             0 TRAN_SUM,"+Constant.PART_IS_CHANGHE_01+" BALANCE_YIELD, F.FI_DATE,F.FINE_REASON REMARK\n" );
		sql.append("        FROM TT_AS_WR_FINE F\n" );
		
		
		sql.append("      UNION ALL /*抵扣费用*/\n" );
		
		sql.append("      SELECT '抵扣费用' TYPE, F.CLAIM_NO ORDER_NO, F.DEALER_ID, /*抵扣费用*/\n" );
		sql.append("             '' ACTIVITY_NAME,'' SERIES,'' MODEL,'' ENGINE_NO,'' VIN,\n" );
		sql.append("             '' BUY_DATE,F.CREATE_DATE FINE_DATE,F.DOWN_PART_CODE PART_CODE,F.DOWN_PART_NAME PART_NAME,\n" );
		sql.append("             F.DISCOUNT_SUM NUM,'' LABOUR_CODE,'' LABOUR_NAME,0   LABOUR_SUM,\n" );
		sql.append("             0 PART_SUM,0 SEND_SUM,0 FREE_SUM,0 B_FREE_SUM,\n" );
		sql.append("             nvl(F.DISCOUNT,0)*-1   B_PART_SUM,\n" );
		sql.append("             0    B_LABOUR_SUM,0 B_SEND_SUM,0 OTHER_SUM,\n" );
		sql.append("             0 TRAN_SUM,"+Constant.PART_IS_CHANGHE_01+" BALANCE_YIELD, F.FI_DATE,F.DEDUCT_RESON REMARK\n" );
		sql.append("        FROM TT_AS_WR_DISCOUNT F\n" );
		
		
		
		
		sql.append("      UNION ALL /*特殊费用(三包外的外派服务)*/\n" );
		sql.append("      SELECT ft.code_desc, FEE_NO, s.dealer_id, '', '', '', '', '', '',\n" );
		sql.append("             s.make_date, s.part_code, s.part_name, 1, '', '',\n" );
		sql.append("             DECODE(S.BALANCE_FEE_TYPE, "+Constant.TAX_RATE_FEE_01+", S.DECLARE_SUM1, 0),\n" );
		sql.append("             DECODE(S.BALANCE_FEE_TYPE, "+Constant.TAX_RATE_FEE_02+", S.DECLARE_SUM1, 0),\n" );
		sql.append("             0,0,0,DECODE(S.BALANCE_FEE_TYPE, 94141002, S.DECLARE_SUM, 0),\n" );
		sql.append("             DECODE(S.BALANCE_FEE_TYPE, "+Constant.TAX_RATE_FEE_01+", S.DECLARE_SUM, 0),0,0,0, "+Constant.PART_IS_CHANGHE_01+",SA.AUDITING_DATE,''\n" );
		sql.append("       FROM TT_AS_WR_SPEFEE S\n" );
		sql.append("        LEFT JOIN ( SELECT SAA.FEE_ID,max(SAA.STATUS) STATUS ,min(SAA.AUDITING_DATE) AUDITING_DATE  from  TT_AS_WR_SPEFEE_AUDITING SAA group by SAA.FEE_ID ) SA ON S.ID = SA.FEE_ID AND SA.STATUS = "+Constant.SPEFEE_STATUS_10+"\n" );
		sql.append("        LEFT JOIN tc_code ft ON s.fee_type = ft.code_id AND ft.TYPE = "+Constant.FEE_TYPE+"\n" );
		sql.append("       WHERE S.FEE_TYPE = "+Constant.FEE_TYPE_02+"\n" );
		sql.append("        AND S.STATUS = "+Constant.SPEFEE_STATUS_10+") M\n" );
		sql.append("  JOIN vw_org_dealer_service ds ON m.dealer_id = ds.dealer_id AND ds.dealer_type = "+Constant.DEALER_TYPE_DWR+"\n" );
		sql.append("  WHERE 1 = 1\n" );
		   if(!CommonUtils.isNullString(map.get("serisid"))) {
				sql.append(" AND m.series = ?\n"); 
				par.add(""+map.get("serisid").trim()+"");
			}
	   
	
		if(!CommonUtils.isNullString(map.get("groupCode"))) {
			
			String[] array = map.get("groupCode").toString().split(",");
	
			StringBuffer str = new StringBuffer();
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					str.append(""+getGroupIdByGroupCode(array[i])+"");
						if (i != array.length - 1) {
							str.append(",");
						}	
				}
					sql.append(Utility.getConSqlByParamForEqual(str.toString(), par,"m", "model"));
				}
			}

		   if(!CommonUtils.isNullString(map.get("yieldlyType"))) {
				sql.append(" AND m.BALANCE_YIELD = ?\n"); 
				par.add(""+map.get("yieldlyType").trim()+"");
			}
			if(!CommonUtils.isNullString(map.get("settlementNo"))) {
				sql.append(" and  m.ORDER_NO LIKE  ?\n"); 
				par.add("%"+map.get("settlementNo").trim()+"%");
			}
			
			if(Utility.testString(map.get("bDate"))){
				sql.append("and m.fi_date>=to_date(?,'yyyy-mm-dd')\n"); 
				par.add(map.get("bDate"));

			}
			if(Utility.testString(map.get("eDate"))){
				sql.append("and m.fi_date<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
				par.add(map.get("eDate")+" 23:59:59");
			}
			if(!CommonUtils.isNullString(map.get("supplyCode"))) {
				sql.append(" and  ds.dealer_code LIKE  ?\n"); 
				par.add("%"+map.get("supplyCode").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("supplyName"))) {
				sql.append(" and  ds.dealer_name LIKE  ?\n"); 
				par.add("%"+map.get("supplyName").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("partCode"))) {
				sql.append(" and   m.part_code LIKE  ?\n"); 
				par.add("%"+map.get("partCode").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("partName"))) {
				sql.append(" and  m.part_name LIKE  ?\n"); 
				par.add("%"+map.get("partName").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("homeworkCode"))) {
				sql.append(" and   m.LABOUR_CODE LIKE  ?\n"); 
				par.add("%"+map.get("homeworkCode").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("homeworkName"))) {
				sql.append(" and  m.LABOUR_NAME LIKE  ?\n"); 
				par.add("%"+map.get("homeworkName").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("vin"))) {
				sql.append(" and  m.vin LIKE  ?\n"); 
				par.add("%"+map.get("vin").trim()+"%");
			}
		sql.append("UNION ALL\n" );
		sql.append("SELECT M.TYPE,\n" );
		sql.append("       m.claim_no order_no,\n" );
		sql.append("       m.root_dealer_code,\n" );
		sql.append("       m.dealer_code,\n" );
		sql.append("       m.activity_name,\n" );
		sql.append("       m.SERIES,\n" );
		sql.append("       m.MODEL,\n" );
		sql.append("       m.engine_no,\n" );
		sql.append("       m.VIN, /*VIN*/\n" );
		sql.append("       m.buy_date,\n" );
		sql.append("       m.FINE_DATE,\n" );
		sql.append("       m.part_code,\n" );
		sql.append("       m.part_name,\n" );
		sql.append("       round(m.quantity) quantity,\n" );
		sql.append("       decode(m.lr, 1, m.wr_labourcode, NULL) wr_labourcode,\n" );
		sql.append("       decode(m.lr, 1, m.wr_labourname, NULL) wr_labourname,\n" );
		sql.append("       decode(m.lr, 1, m.LABOUR_SUM, 0) LABOUR_SUM,\n" );
		sql.append("       m.PART_SUM,\n" );
		sql.append("       decode(m.cr, 1, m.SEND_SUM, 0) SEND_SUM,\n" );
		sql.append("       decode(m.cr, 1, m.FREE_SUM, 0) FREE_SUM,\n" );
		sql.append("       decode(m.cr, 1, m.B_FREE_SUM, 0) B_FREE_SUM,\n" );
		sql.append("       decode(m.cr, 1, m.B_SEND_SUM, 0) B_SEND_SUM,\n" );
		sql.append("       m.B_PART_SUM,\n" );
		sql.append("       decode(m.lr, 1, m.B_LABOUR_SUM, 0) B_LABOUR_SUM,\n" );
		sql.append("       M.other_sum,\n" );
		sql.append("       decode(M.balance_yieldly,"+Constant.PART_IS_CHANGHE_01+",'昌河',"+Constant.PART_IS_CHANGHE_02+",'东安','') balance_yieldly,\n" );
		sql.append("       m.fi_date,\n" );
		sql.append("       m.remark\n" );
		sql.append(" FROM (SELECT ct.code_desc TYPE, a.claim_no, ds.root_dealer_code, ds.dealer_code, ta.activity_name, vs.group_name SERIES,\n" );
		sql.append("             a.model_code MODEL, v.engine_no, a.vin, to_char(v.purchased_date,'yyyy-mm-dd') buy_date,\n" );
		sql.append("             to_char(a.report_date,'yyyy-mm-dd') FINE_DATE, p.part_code, p.part_name,  NVL(p.balance_quantity,0) quantity,\n" );
		sql.append("             l.wr_labourcode, l.wr_labourname,decode ( a.CLAIM_TYPE,10661002  , NVL(mg.PART_PRICE,0) , NVL(l.apply_amount, 0) ) LABOUR_SUM, decode ( a.CLAIM_TYPE,10661002  , NVL(mg.LABOUR_PRICE,0) , NVL(p.apply_amount, 0)) PART_SUM,\n" );
		sql.append("             decode(a.claim_type,"+Constant.CLA_TYPE_09+",NVL(a.apply_netitem_amount,0),0) SEND_SUM,\n" );
		sql.append("             NVL(CASE WHEN a.claim_type != "+Constant.CLA_TYPE_09+" THEN NVL(a.apply_netitem_amount,0) ELSE 0 END,0) FREE_SUM,\n" );
		sql.append("             NVL(CASE WHEN a.claim_type != "+Constant.CLA_TYPE_09+" THEN nvl(a.balance_netitem_amount,0) ELSE 0 END,0) B_FREE_SUM,\n" );
		sql.append("             decode ( a.CLAIM_TYPE,10661002  , NVL(mg.PART_PRICE,0) ,NVL(p.balance_amount, 0)) B_PART_SUM,\n" );
		sql.append("             decode ( a.CLAIM_TYPE,10661002  , NVL(mg.LABOUR_PRICE,0) , NVL(l.balance_amount, 0)) B_LABOUR_SUM,\n" );
		sql.append("             decode(a.claim_type,"+Constant.CLA_TYPE_09+",nvl(a.balance_netitem_amount,0),0) B_SEND_SUM,\n" );
		sql.append("             0 other_sum,\n" );
		sql.append("             a.balance_yieldly,\n" );
		sql.append("             to_char(a.fi_date,'yyyy-mm') fi_date,\n" );
		sql.append("             '' remark,\n" );
		sql.append("             dense_rank() over(PARTITION BY a.claim_no, l.wr_labourcode ORDER BY p.responsibility_type, p.part_code) lr,\n" );
		sql.append("             dense_rank() over(PARTITION BY a.claim_no ORDER BY p.responsibility_type, p.part_code) cr\n" );
		sql.append("       FROM  TT_AS_WR_MODEL_ITEM mi, tm_vehicle vv ,tt_as_wr_model_group mg , tt_as_wr_application a\n" );
		sql.append("        JOIN vw_org_dealer_service ds ON NVL(a.SECOND_DEALER_ID ,  a.dealer_id) = ds.dealer_id AND ds.dealer_type = "+Constant.MSG_TYPE_2+"\n" );
		sql.append("        JOIN tc_code ct ON a.claim_type = ct.code_id AND ct.TYPE = "+Constant.CLA_TYPE+"\n" );
		sql.append("        LEFT JOIN tt_as_wr_labouritem l ON a.ID = l.ID\n" );
		sql.append("        LEFT JOIN tt_as_wr_partsitem p ON p.ID = a.ID AND p.wr_labourcode = l.wr_labourcode\n" );
		sql.append("        /*LEFT JOIN (SELECT n.Id, sum(NVL(n.apply_amount,0)) apply_amount, SUM(nvl(n.balance_amount,0)) balance_amount\n" );
		sql.append("                    FROM Tt_As_Wr_Netitem n GROUP BY n.ID) an ON an.ID = a.ID*/\n" );
		sql.append("        LEFT JOIN tt_as_activity ta ON a.campaign_code = ta.activity_code\n" );
		sql.append("        LEFT JOIN tt_as_activity_subject ts ON ta.subject_id = ts.subject_id AND ts.activity_type = "+Constant.SERVICEACTIVITY_TYPE_01+"\n" );
		sql.append("        LEFT JOIN tm_vehicle v ON a.vin = v.vin\n" );
		sql.append("        LEFT JOIN tm_vhcl_material_group vs ON v.series_id = vs.group_id AND vs.group_level = 2\n" );
		sql.append("       WHERE A.STATUS IN ("+Constant.CLAIM_APPLY_ORD_TYPE_13+","+Constant.CLAIM_APPLY_ORD_TYPE_16+")\n" );
		sql.append("      and vv.package_id = mi.model_id\n" );
		sql.append("\t\t  and mg.wrgroup_id = mi.wrgroup_id\n" );
		sql.append("\t\t  and vv.vin=A.VIN\n" );
		sql.append("\t AND  MG.WRGROUP_TYPE=10451001");
		sql.append("        AND a.create_date > to_date('2013-08-26','yyyy-mm-dd')\n" );
		sql.append("        AND (a.claim_type != "+Constant.CLA_TYPE_06+" OR (a.claim_type = "+Constant.CLA_TYPE_06+" AND ts.activity_type = "+Constant.SERVICEACTIVITY_TYPE_01+"))\n" );

		   if(!CommonUtils.isNullString(map.get("serisid"))) {
				sql.append(" AND v.series_id = ?\n"); 
				par.add(""+map.get("serisid").trim()+"");
			}
	   
	
		if(!CommonUtils.isNullString(map.get("groupCode"))) {
			
			String[] array = map.get("groupCode").toString().split(",");
	
			StringBuffer str = new StringBuffer();
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					str.append(""+getGroupIdByGroupCode(array[i])+"");
						if (i != array.length - 1) {
							str.append(",");
						}	
				}
					sql.append(Utility.getConSqlByParamForEqual(str.toString(), par,"V", "model_id"));
				}
			}

		   if(!CommonUtils.isNullString(map.get("yieldlyType"))) {
				sql.append(" AND a.balance_yieldly = ?\n"); 
				par.add(""+map.get("yieldlyType").trim()+"");
			}
			if(!CommonUtils.isNullString(map.get("settlementNo"))) {
				sql.append(" and  a.claim_no LIKE  ?\n"); 
				par.add("%"+map.get("settlementNo").trim()+"%");
			}
			
			if(Utility.testString(map.get("bDate"))){
				sql.append("and a.fi_date>=to_date(?,'yyyy-mm-dd')\n"); 
				par.add(map.get("bDate"));

			}
			if(Utility.testString(map.get("eDate"))){
				sql.append("and a.fi_date<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
				par.add(map.get("eDate")+" 23:59:59");
			}
			if(!CommonUtils.isNullString(map.get("supplyCode"))) {
				sql.append(" and  ds.dealer_code LIKE  ?\n"); 
				par.add("%"+map.get("supplyCode").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("supplyName"))) {
				sql.append(" and  ds.dealer_name LIKE  ?\n"); 
				par.add("%"+map.get("supplyName").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("partCode"))) {
				sql.append(" and   p.part_code LIKE  ?\n"); 
				par.add("%"+map.get("partCode").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("partName"))) {
				sql.append(" and  p.part_name LIKE  ?\n"); 
				par.add("%"+map.get("partName").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("homeworkCode"))) {
				sql.append(" and   l.LABOUR_CODE LIKE  ?\n"); 
				par.add("%"+map.get("homeworkCode").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("homeworkName"))) {
				sql.append(" and  l.LABOUR_NAME LIKE  ?\n"); 
				par.add("%"+map.get("homeworkName").trim()+"%");
			}
			if(!CommonUtils.isNullString(map.get("vin"))) {
				sql.append(" and  v.vin LIKE  ?\n"); 
				par.add("%"+map.get("vin").trim()+"%");
			}
		sql.append("   ) M");


		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), par, getFunName(),pageSize, curPage);
		return ps;
	}
	//结算汇总
	public PageResult<Map<String, Object>> SettlementSummaryQuery(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		StringBuffer sql= new StringBuffer();
		List par=new ArrayList();

		sql.append("SELECT y.TYPE,\n" );
		sql.append("       y.SERIES,\n" );
		sql.append("       y.MODEL,\n" );
		sql.append("       y.fi_month,\n" );
		sql.append("       SUM(Y.LABOUR_SUM) LABOUR_SUM,\n" );
		sql.append("       SUM(Y.PART_SUM) PART_SUM,\n" );
		sql.append("       SUM(Y.SEND_SUM) SEND_SUM,\n" );
		sql.append("       SUM(Y.FREE_SUM) FREE_SUM,\n" );
		sql.append("       SUM(Y.B_FREE_SUM) B_FREE_SUM,\n" );
		sql.append("       SUM(Y.B_PART_SUM) B_PART_SUM,\n" );
		sql.append("       SUM(Y.B_LABOUR_SUM) B_LABOUR_SUM,\n" );
		sql.append("       SUM(Y.B_SEND_SUM) B_SEND_SUM,\n" );
		sql.append("       SUM(Y.OTHER_SUM) OTHER_SUM,\n" );
		sql.append("       SUM(Y.dk_sum) dk_sum \n" );
		// sql.append("       SUM(decode(wt.balance_yieldly,"+Constant.PART_IS_CHANGHE_01+",NVL(wt.sum_carriage,0),"+Constant.PART_IS_CHANGHE_02+",NVL(wt.da_carriage,0),0)) tran_sum\n" );
		sql.append(" FROM(SELECT z.TYPE,\n" );
		sql.append("             z.SERIES,\n" );
		sql.append("             z.MODEL,\n" );
		sql.append("             z.BALANCE_YIELD,\n" );
		sql.append("             z.dealer_id,\n" );
		sql.append("             z.GOODSNUM,\n" );
		sql.append("             z.fi_month,\n" );
		sql.append("             SUM(z.LABOUR_SUM) LABOUR_SUM,\n" );
		sql.append("             SUM(z.PART_SUM) PART_SUM,\n" );
		sql.append("             SUM(z.SEND_SUM) SEND_SUM,\n" );
		sql.append("             SUM(z.FREE_SUM) FREE_SUM,\n" );
		sql.append("             SUM(z.B_FREE_SUM) B_FREE_SUM,\n" );
		sql.append("             SUM(z.B_PART_SUM) B_PART_SUM,\n" );
		sql.append("             SUM(z.B_LABOUR_SUM) B_LABOUR_SUM,\n" );
		sql.append("             SUM(z.B_SEND_SUM) B_SEND_SUM,\n" );
		sql.append("             SUM(z.OTHER_SUM) OTHER_SUM,\n" );
		sql.append("             SUM(z.dk_sum) dk_sum\n" );
		sql.append("       FROM(SELECT m.TYPE, /*维修类型*/   /*条码补办/三包凭证/特殊费用 结算明细统计SQL*/\n" );
		sql.append("                   vs.group_name SERIES, /*车系*/\n" );
		sql.append("                   vm.group_code MODEL, /*车型*/\n" );
		sql.append("                   m.LABOUR_SUM, /*上报工时费用*/\n" );
		sql.append("                   m.PART_SUM, /*上报材料费用*/\n" );
		sql.append("                   m.SEND_SUM, /*上报派出费用*/\n" );
		sql.append("                   m.FREE_SUM, /*上报赠送费用*/\n" );
		sql.append("                   m.B_FREE_SUM, /*赠送费用*/\n" );
		sql.append("                   m.B_PART_SUM, /*材料费用*/\n" );
		sql.append("                   m.B_LABOUR_SUM, /*工时费用*/\n" );
		sql.append("                   m.B_SEND_SUM, /*外派费用*/\n" );
		sql.append("                   m.OTHER_SUM, /*其他费用*/\n" );
		sql.append("                   0 dk_SUM, /*抵扣费用*/\n" );
		sql.append("                   m.BALANCE_YIELD, /*结算基地*/\n" );
		sql.append("                   m.dealer_id, /*服务站ID*/\n" );
		sql.append("                   ds.dealer_code||to_char(m.FINE_DATE,'yyyymm') GOODSNUM, /*服务站运费年月*/\n" );
		sql.append("                   to_char(m.fi_Date,'yyyy-mm') fi_month /*系统确认年月*/\n" );
		sql.append("              FROM (SELECT '条码补办' TYPE , ba.vin, /*条码补办*/\n" );
		sql.append("                         BA.APPLY_DATE FINE_DATE,\n" );
		sql.append("                         ba.APPLY_BY DEALER_ID,\n" );
		sql.append("                         0 LABOUR_SUM,\n" );
		sql.append("                         0 PART_SUM,\n" );
		sql.append("                         0 SEND_SUM,\n" );
		sql.append("                         0 FREE_SUM,\n" );
		sql.append("                         0 B_FREE_SUM,\n" );
		sql.append("                         0 B_PART_SUM,\n" );
		sql.append("                         0 B_LABOUR_SUM,\n" );
		sql.append("                         0 B_SEND_SUM,\n" );
		sql.append("                         DECODE(BA.APPLY_STATUS, "+Constant.BARCODE_APPLY_STATUS_04+", DECODE(NVL(BA.AUDIT_ACOUNT, 0) , 0, 0, -1 * NVL(BA.AUDIT_ACOUNT, 0)), 0)  OTHER_SUM,\n" );
		sql.append("                         95411001 BALANCE_YIELD,\n" );
		sql.append("                         BA.AUDIT_DATE FI_DATE\n" );
		sql.append("                    FROM TT_AS_BARCODE_APPLY BA\n" );
		sql.append("                   WHERE BA.APPLY_STATUS = "+Constant.BARCODE_APPLY_STATUS_04+"\n" );
		sql.append("                  UNION ALL /*三包凭证*/\n" );
		sql.append("                  SELECT '三包凭证补办',\n" );
		sql.append("                         pa.vin,\n" );
		sql.append("                         PA.APPLY_DATE,\n" );
		sql.append("                         pa.APPLY_BY,\n" );
		sql.append("                         0,0,0,0,0,0, 0, 0,\n" );
		sql.append("                         DECODE(PA.APPLY_STATUS, "+Constant.PACKGE_CHANGE_STATUS_06+", DECODE(NVL(PA.AUDIT_ACOUNT,0),0,0,-1*NVL(PA.AUDIT_ACOUNT,0)), 0),\n" );
		sql.append("                         95411001 BALANCE_YIELD,\n" );
		sql.append("                         PD.AUDIT_DATE\n" );
		sql.append("                   FROM TT_AS_PACKGE_CHANGE_APPLY PA\n" );
		sql.append("                    LEFT JOIN TT_AS_PACKGE_CHANGE_DETAIL PD ON PA.ID = PD.APPLY_ID AND PD.AUDIT_STATUS = "+Constant.PACKGE_CHANGE_STATUS_06+"\n" );
		sql.append("                   WHERE PA.APPLY_STATUS = "+Constant.PACKGE_CHANGE_STATUS_06+"\n" );
		sql.append("                  UNION ALL /*特殊费用*/\n" );
		sql.append("                  SELECT ft.code_desc,\n" );
		sql.append("                         s.vin,\n" );
		sql.append("                         s.make_date,\n" );
		sql.append("                         s.dealer_id,\n" );
		sql.append("                         DECODE(S.BALANCE_FEE_TYPE,  "+Constant.TAX_RATE_FEE_01+", S.DECLARE_SUM1, 0),\n" );
		sql.append("                         DECODE(S.BALANCE_FEE_TYPE,  "+Constant.TAX_RATE_FEE_02+", S.DECLARE_SUM1, 0),\n" );
		sql.append("                         0,0,0,DECODE(S.BALANCE_FEE_TYPE,  "+Constant.TAX_RATE_FEE_02+", S.OFFICE_DECLARE_SUM, 0),\n" );
		sql.append("                         DECODE(S.BALANCE_FEE_TYPE,  "+Constant.TAX_RATE_FEE_01+", S.OFFICE_DECLARE_SUM, 0),0,0, "+Constant.PART_IS_CHANGHE_01+",SA.AUDITING_DATE\n" );
		sql.append("                   FROM TT_AS_WR_SPEFEE S\n" );
		sql.append("                    LEFT JOIN ( SELECT SAA.FEE_ID,max(SAA.STATUS) STATUS ,min(SAA.AUDITING_DATE) AUDITING_DATE  from  TT_AS_WR_SPEFEE_AUDITING SAA group by SAA.FEE_ID ) SA ON S.ID = SA.FEE_ID AND SA.STATUS = "+Constant.SPEFEE_STATUS_10+"\n" );
		sql.append("                    LEFT JOIN tc_code ft ON s.fee_type = ft.code_id AND ft.TYPE = "+Constant.FEE_TYPE+"\n" );
		sql.append("                   WHERE S.FEE_TYPE != "+Constant.FEE_TYPE_02+"\n" );
		sql.append("                    AND S.STATUS = "+Constant.SPEFEE_STATUS_10+") M\n" );
		sql.append("              JOIN vw_org_dealer_service ds ON m.dealer_id = ds.dealer_id AND ds.dealer_type = "+Constant.DEALER_TYPE_DWR+"\n" );
		sql.append("              LEFT JOIN TM_VEHICLE V ON m.VIN = V.VIN\n" );
		sql.append("              LEFT JOIN TM_VHCL_MATERIAL_GROUP vs ON V.SERIES_ID = vs.GROUP_ID AND vs.GROUP_LEVEL = 2\n" );
		sql.append("              LEFT JOIN TM_VHCL_MATERIAL_GROUP vm ON V.MODEL_ID = vm.GROUP_ID AND vm.GROUP_LEVEL = 3\n" );
		sql.append("             WHERE 1 = 1\n" );

		   if(!CommonUtils.isNullString(map.get("serisid"))) {
				sql.append(" AND v.series_id  = ?\n"); 
				par.add(""+map.get("serisid").trim()+"");
			}
	   
	
		if(!CommonUtils.isNullString(map.get("groupCode"))) {
			
			String[] array = map.get("groupCode").toString().split(",");
	
			StringBuffer str = new StringBuffer();
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					str.append(""+getGroupIdByGroupCode(array[i])+"");
						if (i != array.length - 1) {
							str.append(",");
						}	
				}
					sql.append(Utility.getConSqlByParamForEqual(str.toString(), par,"v", "model_id "));
				}
			}

		   if(!CommonUtils.isNullString(map.get("yieldlyType"))) {
				sql.append(" AND m.BALANCE_YIELD = ?\n"); 
				par.add(""+map.get("yieldlyType").trim()+"");
			}
		if(Utility.testString(map.get("bDate"))){
			sql.append("and m.fi_date>=to_date(?,'yyyy-mm-dd')\n"); 
			par.add(map.get("bDate"));

		}
		if(Utility.testString(map.get("eDate"))){
			sql.append("and m.fi_date<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
			par.add(map.get("eDate")+" 23:59:59");
		}
		if(!CommonUtils.isNullString(map.get("costType"))) {
			sql.append(" and  m.type LIKE  ?\n"); 
			par.add("%"+map.get("costType").trim()+"%");
		}
		
		sql.append("            UNION ALL\n" );
		sql.append("            SELECT m.TYPE, /*维修类型*/\n" );
		sql.append("                   m.SERIES, /*车系*/\n" );
		sql.append("                   m.MODEL, /*车型*/\n" );
		sql.append("                   m.LABOUR_SUM, /*上报工时费*/\n" );
		sql.append("                   m.PART_SUM, /*上报材料费*/\n" );
		sql.append("                   m.SEND_SUM, /*上报派出费*/\n" );
		sql.append("                   m.FREE_SUM, /*上报赠送费*/\n" );
		sql.append("                   m.B_FREE_SUM, /*赠送费用*/\n" );
		sql.append("                   m.B_PART_SUM, /*材料费*/\n" );
		sql.append("                   m.B_LABOUR_SUM, /*工时费*/\n" );
		sql.append("                   m.B_SEND_SUM, /*外派费*/\n" );
		sql.append("                   m.OTHER_SUM, /*其他费用*/\n" );
		sql.append("                   0 dk_SUM, /*抵扣费用*/\n" );
		sql.append("                   m.BALANCE_YIELD, /*结算基地*/\n" );
		sql.append("                   m.dealer_id, /*服务站ID*/\n" );
		sql.append("                   ds.dealer_code||to_char(m.FINE_DATE,'yyyymm') GOODSNUM, /*服务站运费年月*/\n" );
		sql.append("                   to_char(m.fi_Date,'yyyy-mm') fi_month /*系统确认年月*/\n" );
		sql.append("             FROM (\n" );
		sql.append("                  SELECT '正负激励' TYPE, F.DEALER_ID, /*正负激励*/\n" );
		sql.append("                         '' SERIES,'' MODEL, F.CREATE_DATE FINE_DATE,\n" );
		sql.append("                         decode( F.FINE_TYPE,80641002 , NVL(F.LABOUR_SUM, 0) , NVL(F.LABOUR_SUM, 0) *-1) LABOUR_SUM,\n" );
		sql.append("                         decode( F.FINE_TYPE,80641002 , NVL(F.DATUM_SUM, 0),NVL(F.DATUM_SUM, 0) *-1) PART_SUM,0 SEND_SUM,0 FREE_SUM,0 B_FREE_SUM,\n" );
		sql.append("                          decode( F.FINE_TYPE,80641002 , DECODE(F.FI_DATE, NULL, 0, NVL(F.DATUM_SUM, 0)), DECODE(F.FI_DATE, NULL, 0, NVL(F.DATUM_SUM, 0)) *-1 ) B_PART_SUM,\n" );
		sql.append("                          decode( F.FINE_TYPE,80641002 , DECODE(F.FI_DATE, NULL, 0, NVL(F.LABOUR_SUM, 0)),  DECODE(F.FI_DATE, NULL, 0, NVL(F.LABOUR_SUM, 0))*-1 ) B_LABOUR_SUM,0 B_SEND_SUM,0 OTHER_SUM,\n" );
		sql.append("                         0 TRAN_SUM,95411001 BALANCE_YIELD, F.FI_DATE\n" );
		sql.append("                    FROM TT_AS_WR_FINE F\n" );
		

		sql.append("      UNION ALL /*抵扣费用*/\n" );
		
		sql.append("      SELECT '抵扣费用' TYPE, F.DEALER_ID, /*抵扣费用*/\n" );
		sql.append("             '' SERIES,'' MODEL,\n" );
		sql.append("              F.CREATE_DATE FINE_DATE,\n" );
		sql.append("              0   LABOUR_SUM,\n" );
		sql.append("             0 PART_SUM,0 SEND_SUM,0 FREE_SUM,0 B_FREE_SUM,\n" );
		sql.append("             nvl(F.DISCOUNT,0)*-1   B_PART_SUM,\n" );
		sql.append("             0    B_LABOUR_SUM,0 B_SEND_SUM,0 OTHER_SUM,\n" );
		sql.append("             0 TRAN_SUM,"+Constant.PART_IS_CHANGHE_01+" BALANCE_YIELD, F.FI_DATE\n" );
		sql.append("        FROM TT_AS_WR_DISCOUNT F\n" );
		
		sql.append("                  UNION ALL /*特殊费用(三包外的外派服务)*/\n" );
		sql.append("                  SELECT ft.code_desc, s.dealer_id, '', '', s.make_date,\n" );
		sql.append("                         DECODE(S.BALANCE_FEE_TYPE, "+Constant.TAX_RATE_FEE_01+", S.DECLARE_SUM1, 0),\n" );
		sql.append("                         DECODE(S.BALANCE_FEE_TYPE, "+Constant.TAX_RATE_FEE_02+", S.DECLARE_SUM1, 0),\n" );
		sql.append("                         0,0,0,DECODE(S.BALANCE_FEE_TYPE, "+Constant.TAX_RATE_FEE_02+", S.DECLARE_SUM, 0),\n" );
		sql.append("                         DECODE(S.BALANCE_FEE_TYPE, "+Constant.TAX_RATE_FEE_01+", S.DECLARE_SUM, 0),0,0,0, "+Constant.PART_IS_CHANGHE_01+",SA.AUDITING_DATE\n" );
		sql.append("                   FROM TT_AS_WR_SPEFEE S\n" );
		sql.append("                    LEFT JOIN ( SELECT SAA.FEE_ID,max(SAA.STATUS) STATUS ,min(SAA.AUDITING_DATE) AUDITING_DATE  from  TT_AS_WR_SPEFEE_AUDITING SAA group by SAA.FEE_ID ) SA ON S.ID = SA.FEE_ID AND SA.STATUS = "+Constant.SPEFEE_STATUS_10+"\n" );
		sql.append("                    LEFT JOIN tc_code ft ON s.fee_type = ft.code_id AND ft.TYPE = "+Constant.FEE_TYPE+"\n" );
		sql.append("                   WHERE S.FEE_TYPE = "+Constant.FEE_TYPE_02+"\n" );
		sql.append("                    AND S.STATUS = "+Constant.SPEFEE_STATUS_10+") M\n" );
		sql.append("              JOIN vw_org_dealer_service ds ON m.dealer_id = ds.dealer_id AND ds.dealer_type = "+Constant.DEALER_TYPE_DWR+"\n" );
		sql.append("              WHERE 1 = 1\n" );


		   if(!CommonUtils.isNullString(map.get("serisid"))) {
				sql.append(" AND m.series  = ?\n"); 
				par.add(""+map.get("serisid").trim()+"");
			}
	   
	
		if(!CommonUtils.isNullString(map.get("groupCode"))) {
			
			String[] array = map.get("groupCode").toString().split(",");
	
			StringBuffer str = new StringBuffer();
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					str.append(""+getGroupIdByGroupCode(array[i])+"");
						if (i != array.length - 1) {
							str.append(",");
						}	
				}
					sql.append(Utility.getConSqlByParamForEqual(str.toString(), par,"m", "model "));
				}
			}

		   if(!CommonUtils.isNullString(map.get("yieldlyType"))) {
				sql.append(" AND m.BALANCE_YIELD = ?\n"); 
				par.add(""+map.get("yieldlyType").trim()+"");
			}
		if(Utility.testString(map.get("bDate"))){
			sql.append("and m.fi_date>=to_date(?,'yyyy-mm-dd')\n"); 
			par.add(map.get("bDate"));

		}
		if(Utility.testString(map.get("eDate"))){
			sql.append("and m.fi_date<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
			par.add(map.get("eDate")+" 23:59:59");
		}
		if(!CommonUtils.isNullString(map.get("costType"))) {
			sql.append(" and  m.type LIKE  ?\n"); 
			par.add("%"+map.get("costType").trim()+"%");
		}
		   
		sql.append("            UNION ALL\n" );
		
		sql.append("       SELECT M.TYPE,\n" );
		sql.append("       m.SERIES,\n" );
		sql.append("       m.MODEL,\n" );
		// sql.append("       m.FINE_DATE,\n" );
		sql.append("       decode(m.lr, 1, m.LABOUR_SUM, 0) LABOUR_SUM,\n" );
		sql.append("       m.PART_SUM,\n" );
		sql.append("       decode(m.cr, 1, m.SEND_SUM, 0) SEND_SUM,\n" );
		sql.append("       decode(m.cr, 1, m.FREE_SUM, 0) FREE_SUM,\n" );
		sql.append("       decode(m.cr, 1, m.B_FREE_SUM, 0) B_FREE_SUM,\n" );
		sql.append("       m.B_PART_SUM,\n" );
		sql.append("       decode(m.lr, 1, m.B_LABOUR_SUM, 0) B_LABOUR_SUM,\n" );
		sql.append("       decode(m.cr, 1, m.B_SEND_SUM, 0)   B_SEND_SUM, \n" );
		sql.append("       M.other_sum,\n" );
		sql.append("       0 dk_SUM,  ");
		sql.append("       M.balance_yieldly ,\n" );
		sql.append("       M.DEALER_ID,\n" );
		sql.append("      '' GOODSNUM,\n" );
		sql.append("       m.fi_date\n" );
		sql.append(" FROM (SELECT decode( a.CLAIM_TYPE, 10661006, a.CAMPAIGN_CODE ||'-'|| ta.ACTIVITY_NAME  ,   ct.code_desc ) TYPE, a.claim_no, a.DEALER_ID,ds.root_dealer_code, ds.dealer_code, ta.activity_name, vs.group_name SERIES,\n" );
		sql.append("             a.model_code MODEL, v.engine_no, a.vin, to_char(v.purchased_date,'yyyy-mm-dd') buy_date,\n" );
		sql.append("             to_char(a.report_date,'yyyy-mm-dd') FINE_DATE, p.part_code, p.part_name,  NVL(p.balance_quantity,0) quantity,\n" );
		sql.append("             l.wr_labourcode, l.wr_labourname,decode ( a.CLAIM_TYPE,10661002  , NVL(mg.PART_PRICE,0) , NVL(l.apply_amount, 0) ) LABOUR_SUM, decode ( a.CLAIM_TYPE,10661002  , NVL(mg.LABOUR_PRICE,0) , NVL(p.apply_amount, 0)) PART_SUM,\n" );
		sql.append("             decode(a.claim_type,10661009,NVL(a.apply_netitem_amount,0),0) SEND_SUM,\n" );
		sql.append("             NVL(CASE WHEN a.claim_type != 10661009 THEN NVL(a.apply_netitem_amount,0) ELSE 0 END,0) FREE_SUM,\n" );
		sql.append("             NVL(CASE WHEN a.claim_type != 10661009 THEN nvl(a.balance_netitem_amount,0) ELSE 0 END,0) B_FREE_SUM,\n" );
		sql.append("             decode ( a.CLAIM_TYPE,10661002  , NVL(mg.PART_PRICE,0) ,NVL(p.balance_amount, 0)) B_PART_SUM,\n" );
		sql.append("             decode ( a.CLAIM_TYPE,10661002  , NVL(mg.LABOUR_PRICE,0) , NVL(l.balance_amount, 0)) B_LABOUR_SUM,\n" );
		sql.append("             decode(a.claim_type,10661009,nvl(a.balance_netitem_amount,0),0) B_SEND_SUM,\n" );
		sql.append("             0 other_sum,\n" );
		sql.append("             a.balance_yieldly,\n" );
		sql.append("             to_char(a.fi_date,'yyyy-mm') fi_date,\n" );
		sql.append("             '' remark,\n" );
		sql.append("             dense_rank() over(PARTITION BY a.claim_no, l.wr_labourcode ORDER BY p.responsibility_type, p.part_code) lr,\n" );
		sql.append("             dense_rank() over(PARTITION BY a.claim_no ORDER BY p.responsibility_type, p.part_code) cr\n" );
		sql.append("       FROM  TT_AS_WR_MODEL_ITEM mi, tm_vehicle vv ,tt_as_wr_model_group mg , tt_as_wr_application a\n" );
		sql.append("        JOIN vw_org_dealer_service ds ON NVL(a.SECOND_DEALER_ID ,  a.dealer_id) = ds.dealer_id AND ds.dealer_type = 10771002\n" );
		sql.append("        JOIN tc_code ct ON a.claim_type = ct.code_id AND ct.TYPE = 1066\n" );
		sql.append("        LEFT JOIN tt_as_wr_labouritem l ON a.ID = l.ID\n" );
		sql.append("        LEFT JOIN tt_as_wr_partsitem p ON p.ID = a.ID AND p.wr_labourcode = l.wr_labourcode\n" );
		sql.append("        /*LEFT JOIN (SELECT n.Id, sum(NVL(n.apply_amount,0)) apply_amount, SUM(nvl(n.balance_amount,0)) balance_amount\n" );
		sql.append("                    FROM Tt_As_Wr_Netitem n GROUP BY n.ID) an ON an.ID = a.ID*/\n" );
		sql.append("        LEFT JOIN tt_as_activity ta ON a.campaign_code = ta.activity_code\n" );
		sql.append("        LEFT JOIN tt_as_activity_subject ts ON ta.subject_id = ts.subject_id AND ts.activity_type = 10561001\n" );
		sql.append("        LEFT JOIN tm_vehicle v ON a.vin = v.vin\n" );
		sql.append("        LEFT JOIN tm_vhcl_material_group vs ON v.series_id = vs.group_id AND vs.group_level = 2\n" );
		sql.append("       WHERE A.STATUS IN (10791013,10791016)\n" );
		sql.append("      and vv.package_id = mi.model_id\n" );
		sql.append("      and mg.wrgroup_id = mi.wrgroup_id\n" );
		sql.append("      and vv.vin=A.VIN\n" );
		
		if(!CommonUtils.isNullString(map.get("serisid"))) {
			sql.append(" AND v.series_id  = ?\n"); 
			par.add(""+map.get("serisid").trim()+"");
		}
   

	if(!CommonUtils.isNullString(map.get("groupCode"))) {
		
		String[] array = map.get("groupCode").toString().split(",");

		StringBuffer str = new StringBuffer();
		if(array.length>0){
			for (int i = 0; i < array.length; i++) {
				str.append(""+getGroupIdByGroupCode(array[i])+"");
					if (i != array.length - 1) {
						str.append(",");
					}	
			}
				sql.append(Utility.getConSqlByParamForEqual(str.toString(), par,"v", "model_id "));
			}
		}

		   if(!CommonUtils.isNullString(map.get("yieldlyType"))) {
				sql.append(" AND a.balance_yieldly = ?\n"); 
				par.add(""+map.get("yieldlyType").trim()+"");
			}
	if(Utility.testString(map.get("bDate"))){
		sql.append("and a.fi_date>=to_date(?,'yyyy-mm-dd')\n"); 
		par.add(map.get("bDate"));

	}
	if(Utility.testString(map.get("eDate"))){
		sql.append("and a.fi_date<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
		par.add(map.get("eDate")+" 23:59:59");
	}
	if(!CommonUtils.isNullString(map.get("costType"))) {
		sql.append(" and (ct.code_desc like ? or ta.activity_name like ?)\n"); 
		par.add("%"+map.get("costType").trim()+"%");
		par.add("%"+map.get("costType").trim()+"%");
	}
	sql.append("   AND  MG.WRGROUP_TYPE=10451001        AND a.create_date > to_date('2013-08-26','yyyy-mm-dd')\n" );
	sql.append("        AND (a.claim_type != 10661006 OR (a.claim_type = 10661006 AND ts.activity_type = 10561001))) M");
	sql.append("     )　Z   GROUP BY z.TYPE, z.SERIES, z.MODEL, z.BALANCE_YIELD, z.dealer_id, z.GOODSNUM, z.fi_month) Y\n" );
	sql.append("   LEFT JOIN tt_as_wr_tickets wt ON y.dealer_id = wt.dealerid AND y.BALANCE_YIELD = wt.balance_yieldly\n" );
	sql.append("     AND y.GOODSNUM = wt.goodsnum\n" );
	sql.append("   GROUP BY y.TYPE, y.SERIES, y.MODEL, y.fi_month\n" );
	sql.append(" ORDER BY 1, 2, 3");


		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), par, getFunName(),pageSize, curPage);
		return ps;
	}

}
