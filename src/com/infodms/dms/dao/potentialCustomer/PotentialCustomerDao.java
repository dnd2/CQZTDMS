package com.infodms.dms.dao.potentialCustomer;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibm.mq.jms.PCF;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmPotentialCustomerPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class PotentialCustomerDao extends BaseDao  {

	public static final Logger logger = Logger.getLogger(PotentialCustomerDao.class);
	private static final  PotentialCustomerDao dao = new PotentialCustomerDao();
	private static  POFactory factory = POFactoryBuilder.getInstance();

	public static PotentialCustomerDao getInstance() {
		return dao;
	}
	
	

	/**
	 * 
	* @Title: applyQuery 
	* @Description: TODO(查询) 
	* @param @param con
	* @param @param curpage
	* @param @param pagesize
	* @param @return PageResult
	* @return PageResult<TtAsRepairOrderProblemPO>    返回类型 
	* @throws
	 */
	public  PageResult<Map<String, Object>> applyQuery(String con ,int curpage,int pagesize) {		
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct\n" );
		sql.append("  pc.id,\n" );
		sql.append("  pc.customer_no,\n" );
		sql.append("  pc.customer_name,\n" );
		sql.append("  pc.gender,\n" );
		sql.append("  pc.country_code,\n" );
		sql.append("  pc.contactor_mobile,\n" );
		sql.append("  pc.init_level,\n" );
		sql.append("  pc.intent_level,\n" );
		sql.append("  pc.create_date\n" );
		sql.append(" from Tm_Potential_Customer pc");
        sql.append(" where 1=1 \n"); 
		if (con!=null&&!("").equals(con)){
		sql.append(con);
		}
		sql.append(" order by pc.create_date desc ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(),pagesize, curpage);
		return ps;
	}

    
	/**
	 * 
	* @Title: applyQuery 
	* @Description: TODO(查询) 
	* @param @param con
	* @param @param curpage
	* @param @param pagesize
	* @param @return PageResult
	* @return PageResult<TtAsRepairOrderProblemPO>    返回类型 
	* @throws
	 */
	public  PageResult<Map<String, Object>> applyOemQuery(String con ,int curpage,int pagesize,List par) {		
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct\n" );
		sql.append(" pc.id,\n" );
		sql.append(" pc.customer_no,\n" );
		sql.append(" pc.customer_name,\n" );
		sql.append(" pc.gender,\n" );
		sql.append(" pc.country_code,\n" );
		sql.append(" pc.contactor_mobile,\n" );
		sql.append(" pc.init_level,\n" );
		sql.append(" pc.intent_level,\n" );
		sql.append(" pc.cus_source,\n" );
		sql.append(" pc.media_type,\n" );
		sql.append(" pc.sold_by,\n" );
		sql.append(" to_char(pc.create_date, 'yyyy-mm-dd') create_date,\n" );
		sql.append(" vod.ORG_NAME ROOT_ORG_NAME,\n" );
		sql.append(" vod.DEALER_NAME\n" );
		sql.append("from Tm_Potential_Customer pc,VW_ORG_DEALER vod \n" );
		sql.append("where 1=1 and pc.entity_code = vod.DEALER_CODE \n");
		if (con!=null&&!("").equals(con)){
		sql.append(con);
		}
		sql.append(" order by to_char(pc.create_date, 'yyyy-mm-dd') desc ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), par, getFunName(),pagesize, curpage);
		return ps;
	}

	/**
	 * 
	* @Title: applyOemQueryExcel 
	* @Description: TODO(excel查询) 
	* @param @param con
	* @param @param curpage
	* @param @param pagesize
	* @param @return PageResult
	* @throws
	 */
	public  PageResult<Map<String, Object>> applyOemQueryCount(String con ,int curpage,int pagesize,List par) {
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT * FROM ( ");
		sql.append("select B.ORG_NAME ROOT_ORG_NAME,B.REGION_NAME,B.DEALER_NAME,SUM(B.SALE) SALE,SUM(B.FRIEND) FRIEND,SUM(B.AD) AD,SUM(B.TV) TV,SUM(B.INTERNET) INTERNET,\n" );
		sql.append("    SUM(B.NEWSPAPER) NEWSPAPER, SUM(B.SHOW) SHOW, SUM(B.RADIO) RADIO,SUM(B.OUTDOORS) OUTDOORS,SUM(B.DM) DM ,SUM(B.NOTE) NOTE,\n" );
		sql.append("    SUM(B.OTHER) OTHER, SUM(B.AGAIN) AGAIN,SUM(B.H) H, SUM(B.A) A,SUM(B.B) B,SUM(B.C) C,SUM(B.D) D,\n" );
		sql.append("    SUM(B.F) F,SUM(B.F0) F0,SUM(B.N) N,SUM(B.O) O from\n" );
		sql.append(" (select\n" );
		sql.append("    org.ORG_NAME,\n" );
		sql.append("    tr.REGION_NAME,td.DEALER_NAME,\n" );
		sql.append("    nvl(decode(pc.cus_source,13111001, count(pc.cus_source)),0) SALE,\n" );//展厅活动
		sql.append("    nvl(decode(pc.cus_source,13111002, count(pc.cus_source)),0) FRIEND,\n" );//报纸杂志
		sql.append("    nvl(decode(pc.cus_source,13111003, count(pc.cus_source)),0) AD,\n" );//市场展示及促销活动
		sql.append("    nvl(decode(pc.cus_source,13111004, count(pc.cus_source)),0) TV,\n" );//重复购买
		sql.append("    nvl(decode(pc.cus_source,13111005, count(pc.cus_source)),0) INTERNET,\n" );//朋友介绍
		sql.append("    nvl(decode(pc.cus_source,13111006, count(pc.cus_source)),0) NEWSPAPER,\n" );//老客户介绍
		sql.append("    nvl(decode(pc.cus_source,13111007, count(pc.cus_source)),0) SHOW,\n" );//其他
		sql.append("    nvl(decode(pc.cus_source,13111008, count(pc.cus_source)),0) RADIO,\n" );//陌生拜访
		sql.append("    nvl(decode(pc.cus_source,13111009, count(pc.cus_source)),0) OUTDOORS,\n" );//电台广告
		sql.append("    nvl(decode(pc.cus_source,13111010, count(pc.cus_source)),0) DM,\n" );//电视广告
		sql.append("    nvl(decode(pc.cus_source,13111011, count(pc.cus_source)),0) NOTE,\n" );//路牌广告
		sql.append("    nvl(decode(pc.cus_source,13111012, count(pc.cus_source)),0) OTHER,\n" );//网络广告
		sql.append("    nvl(decode(pc.cus_source,13111013, count(pc.cus_source)),0) AGAIN,\n" );//路过
		sql.append("    nvl(decode(pc.intent_level,13101001, count(pc.intent_level)),0) H,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101002, count(pc.intent_level)),0) A,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101003, count(pc.intent_level)),0) B,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101004, count(pc.intent_level)),0) C,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101009, count(pc.intent_level)),0) D,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101007, count(pc.intent_level)),0) F,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101006, count(pc.intent_level)),0) F0,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101005, count(pc.intent_level)),0) N,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101008, count(pc.intent_level)),0) O\n" );
		sql.append("from Tm_Potential_Customer pc,TM_DEALER_ORG_RELATION oda, TM_ORG org ,TM_DEALER td,tm_region tr,VW_ORG_DEALER vod \n" );
		sql.append("where 1=1 and pc.entity_code = td.DEALER_CODE and td.dealer_id=vod.dealer_id\n");
		sql.append(" and oda.dealer_id = td.dealer_id \n");
		sql.append(" and oda.org_id = org.org_id \n");
		sql.append(" and td.province_id = tr.region_code \n");
//		sql.append("   from Tm_Potential_Customer pc,TT_ORG_DEALER_ALL oda\n" );
//		sql.append("   where 1=1 and pc.entity_code = oda.DEALER_CODE\n" );
		if (con!=null&&!("").equals(con)){
		sql.append(con);
		}
		sql.append("   group by  org.ORG_NAME,tr.REGION_NAME,td.DEALER_NAME,pc.cus_source,pc.intent_level ) B\n" );
		sql.append("   group by B.ORG_NAME,B.REGION_NAME,B.DEALER_NAME");
		sql.append(" ) D order by D.ROOT_ORG_NAME DESC");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), par, getFunName(),pagesize, curpage);

		return ps;
	}

	/**
	 * 
	* @Title: applyOemQueryExcel 
	* @Description: TODO(excel查询) 
	* @param @param con
	* @param @param curpage
	* @param @param pagesize
	* @param @return PageResult
	* @throws
	 */
	public  List<Map<String,Object>> applyOemQueryExcel(String con ,int curpage,int pagesize,List par) {		
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct\n" );
		sql.append(" pc.id,\n" );
		sql.append(" pc.customer_no,\n" );
		sql.append(" pc.customer_name,\n" );
		sql.append(" decode(pc.gender,10031001,'男',10031002,'女',' ') gender,\n" );
		sql.append(" pc.country_code,\n" );
		sql.append(" pc.contactor_mobile,\n" );
		sql.append("decode(pc.init_level,13101001,'H级',13101002,'A级',13101003,'B级',13101004,'C级',13101005,'N级',13101006,'F0级',13101007,'F级',13101008,'O级',13101009,'D级') init_level,\n" );
		sql.append("decode(pc.intent_level,13101001,'H级',13101002,'A级',13101003,'B级',13101004,'C级',13101005,'N级',13101006,'F0级',13101007,'F级',13101008,'O级',13101009,'D级') intent_level,\n" );
		sql.append(" pc.sold_by,\n" );
		sql.append(" tc1.code_desc cus_source,\n" );
		sql.append(" tc2.code_desc media_type,\n" );
		sql.append(" pc.create_date,\n" );
		sql.append(" vod.org_name ROOT_ORG_NAME,\n" );
		sql.append(" vod.DEALER_NAME\n" );
		sql.append("from Tm_Potential_Customer pc,VW_ORG_DEALER vod,tc_code tc1,tc_code tc2 \n" );
		sql.append("where 1=1 and pc.entity_code = vod.DEALER_CODE \n");
//		sql.append(" and oda.dealer_id = td.dealer_id \n");
//		sql.append(" and oda.org_id = org.org_id \n");
		sql.append(" and pc.cus_source = tc1.code_id and pc.media_type = tc2.code_id \n");
//		sql.append("from Tm_Potential_Customer pc,TT_ORG_DEALER_ALL oda,tc_code tc1,tc_code tc2\n" );
//		sql.append("where 1=1 and pc.entity_code = oda.DEALER_CODE and pc.cus_source = tc1.code_id and pc.media_type = tc2.code_id");

		if (con!=null&&!("").equals(con)){
		sql.append(con);
		}
		sql.append(" order by pc.create_date desc ");
		
		return this.pageQuery(sql.toString(), par, this.getFunName());
	}

	
	/**
	 * 
	* @Title: applyOemQueryExcel 
	* @Description: TODO(excel查询) 
	* @param @param con
	* @param @param curpage
	* @param @param pagesize
	* @param @return PageResult
	* @throws
	 */
	public  List<Map<String,Object>> applyOemQueryCountExcel(String con ,int curpage,int pagesize,List par) {
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT * FROM ( ");
		sql.append("select B.ORG_NAME ROOT_ORG_NAME,B.REGION_NAME,B.DEALER_NAME,SUM(B.SALE) SALE,SUM(B.FRIEND) FRIEND,SUM(B.AD) AD,SUM(B.TV) TV,SUM(B.INTERNET) INTERNET,\n" );
		sql.append("    SUM(B.NEWSPAPER) NEWSPAPER, SUM(B.SHOW) SHOW, SUM(B.RADIO) RADIO,SUM(B.OUTDOORS) OUTDOORS,SUM(B.DM) DM ,SUM(B.NOTE) NOTE,\n" );
		sql.append("    SUM(B.OTHER) OTHER, SUM(B.AGAIN) AGAIN,SUM(B.H) H, SUM(B.A) A,SUM(B.B) B,SUM(B.C) C,SUM(B.D) D,\n" );
		sql.append("    SUM(B.F) F,SUM(B.F0) F0,SUM(B.N) N,SUM(B.O) O from\n" );
		sql.append(" (select\n" );
		sql.append("    vod.ORG_NAME,\n" );
		sql.append("    tr.REGION_NAME,td.DEALER_NAME,\n" );
		sql.append("    nvl(decode(pc.cus_source,13111001, count(pc.cus_source)),0) SALE,\n" );//展厅活动
		sql.append("    nvl(decode(pc.cus_source,13111002, count(pc.cus_source)),0) FRIEND,\n" );//报纸杂志
		sql.append("    nvl(decode(pc.cus_source,13111003, count(pc.cus_source)),0) AD,\n" );//市场展示及促销活动
		sql.append("    nvl(decode(pc.cus_source,13111004, count(pc.cus_source)),0) TV,\n" );//重复购买
		sql.append("    nvl(decode(pc.cus_source,13111005, count(pc.cus_source)),0) INTERNET,\n" );//朋友介绍
		sql.append("    nvl(decode(pc.cus_source,13111006, count(pc.cus_source)),0) NEWSPAPER,\n" );//老客户介绍
		sql.append("    nvl(decode(pc.cus_source,13111007, count(pc.cus_source)),0) SHOW,\n" );//其他
		sql.append("    nvl(decode(pc.cus_source,13111008, count(pc.cus_source)),0) RADIO,\n" );//陌生拜访
		sql.append("    nvl(decode(pc.cus_source,13111009, count(pc.cus_source)),0) OUTDOORS,\n" );//电台广告
		sql.append("    nvl(decode(pc.cus_source,13111010, count(pc.cus_source)),0) DM,\n" );//电视广告
		sql.append("    nvl(decode(pc.cus_source,13111011, count(pc.cus_source)),0) NOTE,\n" );//路牌广告
		sql.append("    nvl(decode(pc.cus_source,13111012, count(pc.cus_source)),0) OTHER,\n" );//网络广告
		sql.append("    nvl(decode(pc.cus_source,13111013, count(pc.cus_source)),0) AGAIN,\n" );//路过
		sql.append("    nvl(decode(pc.intent_level,13101001, count(pc.intent_level)),0) H,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101002, count(pc.intent_level)),0) A,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101003, count(pc.intent_level)),0) B,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101004, count(pc.intent_level)),0) C,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101009, count(pc.intent_level)),0) D,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101007, count(pc.intent_level)),0) F,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101006, count(pc.intent_level)),0) F0,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101005, count(pc.intent_level)),0) N,\n" );
		sql.append("    nvl(decode(pc.intent_level,13101008, count(pc.intent_level)),0) O\n" );
		sql.append("from Tm_Potential_Customer pc,TM_DEALER td,tm_region tr,VW_ORG_DEALER vod \n" );
		sql.append("where 1=1 and pc.entity_code = td.DEALER_CODE \n");
		sql.append(" and vod.dealer_id = td.dealer_id \n");
		sql.append(" and td.province_id = tr.region_code \n");
		if (con!=null&&!("").equals(con)){
		sql.append(con);
		}
		sql.append("   group by  vod.ORG_NAME,tr.REGION_NAME,td.DEALER_NAME,pc.cus_source,pc.intent_level ) B\n" );
		sql.append("   group by B.ORG_NAME,B.REGION_NAME,B.DEALER_NAME");
//		sql.append("   from Tm_Potential_Customer pc,TT_ORG_DEALER_ALL oda\n" );
//		sql.append("   where 1=1 and pc.entity_code = oda.DEALER_CODE\n" );
//		if (con!=null&&!("").equals(con)){
//		sql.append(con);
//		}
//		sql.append("   group by  oda.ROOT_ORG_NAME,oda.REGION_NAME,oda.DEALER_NAME,pc.cus_source,pc.intent_level ) B\n" );
//		sql.append("   group by B.ROOT_ORG_NAME,B.REGION_NAME,B.DEALER_NAME");
		sql.append(" ) D order by D.ROOT_ORG_NAME DESC");

		return this.pageQuery(sql.toString(), par, this.getFunName());
	}

	
	/**
	 * 
	* @Title: applyOemQueryCountRegion 
	* @Description: TODO(excel查询) 
	* @param @param con
	* @param @param curpage
	* @param @param pagesize
	* @param @return PageResult
	* @throws
	 */
	public  List<Map<String,Object>> applyOemQueryCountRegion(String con ,int curpage,int pagesize,List par) {
		
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("select C.ROOT_ORG_NAME,C.REGION_NAME,SUM(C.SALE) SALE,SUM(C.FRIEND) FRIEND,SUM(C.AD) AD,SUM(C.TV) TV,SUM(C.INTERNET) INTERNET,\n" );
		sql.append("     SUM(C.NEWSPAPER) NEWSPAPER, SUM(C.SHOW) SHOW, SUM(C.RADIO) RADIO,SUM(C.OUTDOORS) OUTDOORS,SUM(C.DM) DM ,SUM(C.NOTE) NOTE,\n" );
		sql.append("     SUM(C.OTHER) OTHER, SUM(C.AGAIN) AGAIN,SUM(C.H) H, SUM(C.A) A,SUM(C.B) B,SUM(C.C) C,SUM(C.D) D,\n" );
		sql.append("     SUM(C.F) F,SUM(C.F0) F0,SUM(C.N) N,SUM(C.O) O from (\n" );
		sql.append("     select B.ORG_NAME ROOT_ORG_NAME,B.REGION_NAME,B.DEALER_NAME,SUM(B.SALE) SALE,SUM(B.FRIEND) FRIEND,SUM(B.AD) AD,SUM(B.TV) TV,SUM(B.INTERNET) INTERNET,\n" );
		sql.append("         SUM(B.NEWSPAPER) NEWSPAPER, SUM(B.SHOW) SHOW, SUM(B.RADIO) RADIO,SUM(B.OUTDOORS) OUTDOORS,SUM(B.DM) DM ,SUM(B.NOTE) NOTE,\n" );
		sql.append("         SUM(B.OTHER) OTHER, SUM(B.AGAIN) AGAIN,SUM(B.H) H, SUM(B.A) A,SUM(B.B) B,SUM(B.C) C,SUM(B.D) D,\n" );
		sql.append("         SUM(B.F) F,SUM(B.F0) F0,SUM(B.N) N,SUM(B.O) O from\n" );
		sql.append("      (select\n" );
		sql.append("         vod.ORG_NAME,\n" );
		sql.append("         tr.REGION_NAME,\n" );
		sql.append("         td.DEALER_NAME,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111001, count(pc.cus_source)),0) SALE,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111002, count(pc.cus_source)),0) FRIEND,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111003, count(pc.cus_source)),0) AD,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111004, count(pc.cus_source)),0) TV,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111005, count(pc.cus_source)),0) INTERNET,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111006, count(pc.cus_source)),0) NEWSPAPER,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111007, count(pc.cus_source)),0) SHOW,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111008, count(pc.cus_source)),0) RADIO,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111009, count(pc.cus_source)),0) OUTDOORS,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111010, count(pc.cus_source)),0) DM,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111011, count(pc.cus_source)),0) NOTE,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111012, count(pc.cus_source)),0) OTHER,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111013, count(pc.cus_source)),0) AGAIN,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101001, count(pc.intent_level)),0) H,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101002, count(pc.intent_level)),0) A,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101003, count(pc.intent_level)),0) B,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101004, count(pc.intent_level)),0) C,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101009, count(pc.intent_level)),0) D,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101007, count(pc.intent_level)),0) F,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101006, count(pc.intent_level)),0) F0,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101005, count(pc.intent_level)),0) N,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101008, count(pc.intent_level)),0) O\n" );
//		sql.append("        from Tm_Potential_Customer pc,TT_ORG_DEALER_ALL oda\n" );
//		sql.append("        where 1=1 and pc.entity_code = oda.DEALER_CODE\n" );
		sql.append("        from tm_potential_customer pc,TM_DEALER td,tm_region tr,VW_ORG_DEALER vod \n");
		sql.append("        where 1 = 1 and pc.entity_code = td.dealer_code \n");
		sql.append("        and vod.dealer_id = td.dealer_id \n");
		sql.append("        and td.province_id = tr.region_code \n");
		if (con!=null&&!("").equals(con)){
			sql.append(con);
			}
		sql.append("        group by vod.org_name , tr.region_name,td.dealer_name,pc.cus_source,pc.intent_level ) B \n");
//		sql.append("        group by  oda.ROOT_ORG_NAME,oda.REGION_NAME,oda.DEALER_NAME,pc.cus_source,pc.intent_level ) B\n" );
		sql.append("        group by B.ORG_NAME,B.REGION_NAME,B.DEALER_NAME ) C\n" );
		sql.append("        group by C.ROOT_ORG_NAME,C.REGION_NAME");
		return this.pageQuery(sql.toString(), par, this.getFunName());
	}
	/**
	 * 
	* @Title: applyOemQueryCountRegion 
	* @Description: TODO(excel查询) 
	* @param @param con
	* @param @param curpage
	* @param @param pagesize
	* @param @return PageResult
	* @throws
	 */
	public  List<Map<String,Object>> applyOemQueryCountOrg(String con ,int curpage,int pagesize,List par) {
		
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("\n" );
		sql.append("\n" );
		sql.append("select C.org_name ROOT_ORG_NAME,SUM(C.SALE) SALE,SUM(C.FRIEND) FRIEND,SUM(C.AD) AD,SUM(C.TV) TV,SUM(C.INTERNET) INTERNET,\n" );
		sql.append("     SUM(C.NEWSPAPER) NEWSPAPER, SUM(C.SHOW) SHOW, SUM(C.RADIO) RADIO,SUM(C.OUTDOORS) OUTDOORS,SUM(C.DM) DM ,SUM(C.NOTE) NOTE,\n" );
		sql.append("     SUM(C.OTHER) OTHER, SUM(C.AGAIN) AGAIN,SUM(C.H) H, SUM(C.A) A,SUM(C.B) B,SUM(C.C) C,SUM(C.D) D,\n" );
		sql.append("     SUM(C.F) F,SUM(C.F0) F0,SUM(C.N) N,SUM(C.O) O from (\n" );
		sql.append("     select B.ORG_NAME,B.REGION_NAME,B.DEALER_NAME,SUM(B.SALE) SALE,SUM(B.FRIEND) FRIEND,SUM(B.AD) AD,SUM(B.TV) TV,SUM(B.INTERNET) INTERNET,\n" );
		sql.append("         SUM(B.NEWSPAPER) NEWSPAPER, SUM(B.SHOW) SHOW, SUM(B.RADIO) RADIO,SUM(B.OUTDOORS) OUTDOORS,SUM(B.DM) DM ,SUM(B.NOTE) NOTE,\n" );
		sql.append("         SUM(B.OTHER) OTHER, SUM(B.AGAIN) AGAIN,SUM(B.H) H, SUM(B.A) A,SUM(B.B) B,SUM(B.C) C,SUM(B.D) D,\n" );
		sql.append("         SUM(B.F) F,SUM(B.F0) F0,SUM(B.N) N,SUM(B.O) O from\n" );
		sql.append("      (select\n" );
		sql.append("         vod.ORG_NAME,\n" );
		sql.append("         tr.REGION_NAME,\n" );
		sql.append("         td.DEALER_NAME,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111001, count(pc.cus_source)),0) SALE,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111002, count(pc.cus_source)),0) FRIEND,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111003, count(pc.cus_source)),0) AD,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111004, count(pc.cus_source)),0) TV,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111005, count(pc.cus_source)),0) INTERNET,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111006, count(pc.cus_source)),0) NEWSPAPER,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111007, count(pc.cus_source)),0) SHOW,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111008, count(pc.cus_source)),0) RADIO,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111009, count(pc.cus_source)),0) OUTDOORS,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111010, count(pc.cus_source)),0) DM,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111011, count(pc.cus_source)),0) NOTE,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111012, count(pc.cus_source)),0) OTHER,\n" );
		sql.append("         nvl(decode(pc.cus_source,13111013, count(pc.cus_source)),0) AGAIN,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101001, count(pc.intent_level)),0) H,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101002, count(pc.intent_level)),0) A,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101003, count(pc.intent_level)),0) B,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101004, count(pc.intent_level)),0) C,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101009, count(pc.intent_level)),0) D,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101007, count(pc.intent_level)),0) F,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101006, count(pc.intent_level)),0) F0,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101005, count(pc.intent_level)),0) N,\n" );
		sql.append("         nvl(decode(pc.intent_level,13101008, count(pc.intent_level)),0) O\n" );
		sql.append("        from tm_potential_customer pc,TM_DEALER td,tm_region tr,VW_ORG_DEALER vod \n");
		sql.append("        where 1 = 1 and pc.entity_code = td.dealer_code \n");
		sql.append("        and vod.dealer_id = td.dealer_id \n");
		sql.append("        and td.province_id = tr.region_code \n");
		sql.append("        group by vod.org_name,tr.region_name ,td.dealer_name , pc.cus_source,pc.intent_level ) B \n");
		sql.append("        group by b.org_name,b.region_name,b.dealer_name ) C \n");
		//		sql.append("        from Tm_Potential_Customer pc,TT_ORG_DEALER_ALL oda\n" );
//		sql.append("        where 1=1 and pc.entity_code = oda.DEALER_CODE\n" );
//		if (con!=null&&!("").equals(con)){
//			sql.append(con);
//			}
//		sql.append("        group by  oda.ROOT_ORG_NAME,oda.REGION_NAME,oda.DEALER_NAME,pc.cus_source,pc.intent_level ) B\n" );
//		sql.append("        group by B.ROOT_ORG_NAME,B.REGION_NAME,B.DEALER_NAME ) C\n" );
		sql.append("        group by C.ORG_NAME");
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
    
	/******
	 * 新增
	 */
	public void insertPotentialCustomer(TmPotentialCustomerPO pc) {        
		   factory.insert(pc);	
		}
	/******
	 * 更新
	 */
	@SuppressWarnings("unchecked")
	public int updatePotentialCustomer(TmPotentialCustomerPO pc) {        
		   StringBuffer sql= new StringBuffer();
			sql.append("UPDATE Tm_Potential_Customer\n" );
			sql.append("   SET Address            = '"+pc.getAddress()+"',\n" );
			sql.append("       Update_Date        = ? ,\n" );
			sql.append("       Zip_Code           = '"+pc.getZipCode()+"',\n" );
			sql.append("       Gender             = '"+pc.getGender()+"',\n" );
			sql.append("       Fax                = '"+pc.getFax()+"',\n" );
			sql.append("       Remark             = '"+pc.getRemark()+"',\n" );
			sql.append("       Customer_No        = '"+pc.getCustomerNo()+"',\n" );
			sql.append("       Entity_Code        = '"+pc.getEntityCode()+"',\n" );
			sql.append("       Customer_Name      = '"+pc.getCustomerName()+"',\n" );
			sql.append("       Customer_Type      = '"+pc.getCustomerType()+"',\n" );
			sql.append("       Ct_Code            = '"+pc.getCtCode()+"',\n" );
			sql.append("       Certificate_No     = '"+pc.getCertificateNo()+"',\n" );
			sql.append("       Province           = '"+pc.getProvince()+"',\n" );
			sql.append("       City               = '"+pc.getCity()+"',\n" );
			sql.append("       District           = '"+pc.getDistrict()+"',\n" );
			sql.append("       Contactor_Phone    = '"+pc.getContactorPhone()+"',\n" );
			sql.append("       Contactor_Mobile   = '"+pc.getContactorMobile()+"',\n" );
			sql.append("       E_Mail             = '"+pc.getEMail()+"',\n" );
			sql.append("       Owner_Marriage     = '"+pc.getOwnerMarriage()+"',\n" );
			sql.append("       Education_Level    = '"+pc.getEducationLevel()+"',\n" );
			sql.append("       Industry_First     = '"+pc.getIndustryFirst()+"',\n" );
			sql.append("       Vocation_Type      = '"+pc.getVocationType()+"',\n" );
			sql.append("       Position_Name      = '"+pc.getPositionName()+"',\n" );
			sql.append("       Family_Income      = '"+pc.getFamilyIncome()+"',\n" );
			sql.append("       Cus_Source         = '"+pc.getCusSource()+"',\n" );
			sql.append("       Media_Type         = '"+pc.getMediaType()+"',\n" );
			sql.append("       Buy_Purpose        = '"+pc.getBuyPurpose()+"',\n" );
			sql.append("       Init_Level         = '"+pc.getInitLevel()+"',\n" );
			sql.append("       Intent_Level       = '"+pc.getIntentLevel()+"',\n" );
			sql.append("       Sold_By            = '"+pc.getSoldBy()+"',\n" );
			sql.append("       Is_First_Buy       = '"+pc.getIsFirstBuy()+"',\n" );
			sql.append("       Has_Driver_License = '"+pc.getHasDriverLicense()+"', \n" );
			/**增加的字段**/
			sql.append("       IS_TRY_DRIVE = "+pc.getIsTryDrive()+", \n" );
			sql.append("       IS_FIRST_COME = "+pc.getIsFirstCome()+", \n" );
			sql.append("       CUSTOMER_NUM = "+pc.getCustomerNum()+", \n" );
			sql.append("       GROUP_ID = "+pc.getGroupId()+", \n" );
			sql.append("       COLOR_NAME = '"+pc.getColorName()+"', \n" );
			sql.append("       COME_TIME = ?, \n" );
			sql.append("       LEAVE_TIME = ?, \n" );
			sql.append("       STAY_MINUTE = "+pc.getStayMinute()+" \n" );
			sql.append(" WHERE 1 = 1\n" );
			sql.append("   AND Customer_No = '"+pc.getCustomerNo()+"'");
		List list = new ArrayList();
		list.add(new Date());
		list.add(pc.getComeTime());
		list.add(pc.getLeaveTime());
         int i = factory.update(sql.toString(), list);
         return i;		
	}
	
	/******
	 * 删除
	 */
	public int delPotentialCustomer(String customer_no){
		String sql = "delete Tm_Potential_Customer c where c.customer_no='"+customer_no+"'";
		int i = factory.delete(sql, null);
		return i;
	}
}
