package com.infodms.dms.dao.claim.authorization;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.application.BalanceStatusRecord;
import com.infodms.dms.bean.BalanceYieldlyBean;
import com.infodms.dms.bean.TtIfMarketBean;
import com.infodms.dms.bean.auditBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.claim.oldPart.ClaimOldPartOutStorageDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.partinfo.PartinfoDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TtAsWrBalanceAuthitemPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrGatherBalancePO;
import com.infodms.dms.po.TtBillNoPO;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.po.TtPartBillDefinePO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class BalanceMainDao extends BaseDao {
	public static Logger logger = Logger.getLogger(BalanceMainDao.class);
	
	public static BalanceMainDao dao = new BalanceMainDao();
	
	public static final BalanceMainDao getInstance() {
	   if(dao==null) dao = new BalanceMainDao();
	   return dao;
	}
	protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	public List<BalanceYieldlyBean> getPartBillDefine(String dealer_id)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select A.DEALER_NAME,\n" );
		sql.append("       A.ADDR,\n" );
		sql.append("       A.TEL,\n" );
		sql.append("       A.BANK,\n" );
		sql.append("       A.ACCOUNT,\n" );
		sql.append("       A.TAX_NO,\n" );
		sql.append("       B.AREA_NAME TAX_NAME,\n" );
		sql.append("       B.AREA_ID yieldly\n");
		sql.append("  from tt_part_bill_define A, tm_business_area b\n" );
		sql.append(" where A.YIELDLY = B.AREA_ID\n" );
		sql.append("   and A.DEALER_ID = 1");
		 List<TtPartBillDefinePO> list= super.select(TtPartBillDefinePO.class, sql.toString(), null);
		 
		 List<BalanceYieldlyBean> res = new ArrayList<BalanceYieldlyBean>();
		 
			 BalanceYieldlyBean kp = new BalanceYieldlyBean();
			 kp.setKp("开票信息");
			 kp.setJj("<center>"+list.get(0).getTaxName()+"</center>");
			 kp.setJdz("<center>"+list.get(1).getTaxName()+"</center>");
			 
			 res.add(kp);
			 
			 BalanceYieldlyBean qc = new BalanceYieldlyBean();
			 qc.setKp("全称");
			 qc.setJj(list.get(0).getDealerName());
			 qc.setJdz(list.get(1).getDealerName());
			 
			 res.add(qc);
			 
			 BalanceYieldlyBean dz = new BalanceYieldlyBean();
			 dz.setKp("地址");
			 dz.setJj(list.get(0).getAddr());
			 dz.setJdz(list.get(1).getAddr());
			 
			 res.add(dz);
			 
			 BalanceYieldlyBean tel = new BalanceYieldlyBean();
			 tel.setKp("电话");
			 tel.setJj(list.get(0).getTel());
			 tel.setJdz(list.get(1).getTel());
			 
			 res.add(tel);
			 
			 BalanceYieldlyBean kh = new BalanceYieldlyBean();
			 kh.setKp("开户行");
			 kh.setJj(list.get(0).getBank());
			 kh.setJdz(list.get(1).getBank());
			 
			 res.add(kh);
			 
			 BalanceYieldlyBean no = new BalanceYieldlyBean();
			 no.setKp("账号");
			 no.setJj(list.get(0).getAccount());
			 no.setJdz(list.get(1).getAccount());
			 
			 res.add(no);
			 
			 BalanceYieldlyBean tax = new BalanceYieldlyBean();
			 tax.setKp("税号");
			 tax.setJj(list.get(0).getTaxNo());
			 tax.setJdz(list.get(1).getTaxNo());
			 
			 res.add(tax);
		 
		 return res;

	}
	
	
	public String number_ap(String acent,String year ,String month,String dealerCode)
	{
		String year1 = year;
		String month1 = month;
		Date date = new Date();
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(date);
		year= dateStr.substring(0, 4);
		month = dateStr.substring(5, 7);
		TtBillNoPO ttBillNoPO = new TtBillNoPO();
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from tt_bill_no t where t.YEAR = '"+dateStr.substring(0, 4)+"' and  t.MONTH= '"+dateStr.substring(5, 7)+"' and  t.DEALER_ID= '"+acent+"' ");
		List<TtBillNoPO> list= super.select(TtBillNoPO.class,sql.toString(),null);
		if(list.size() > 0)
		{
			ttBillNoPO = list.get(0);
			String num = ""+ttBillNoPO.getId();
			for(int i = num.length() ;i < 3;i++)
			{
				num = "0"+num;
			}
			TtBillNoPO ttBillNoPO1 = new TtBillNoPO();
			ttBillNoPO1.setBillNoId(ttBillNoPO.getBillNoId());
			TtBillNoPO ttBillNoPO2 = new TtBillNoPO();
			ttBillNoPO2.setId(1l+Long.parseLong(num));
			super.update(ttBillNoPO1, ttBillNoPO2);
			return dealerCode+year1+month1 +num;
			
		}else
		{
			ttBillNoPO.setBillNoId(Long.parseLong(SequenceManager.getSequence("")));
			ttBillNoPO.setMonth(month);
			ttBillNoPO.setYear(year);
			ttBillNoPO.setDealerId(acent);
			ttBillNoPO.setId(2l);
			super.insert(ttBillNoPO);
		    return dealerCode+year1+month1 +"001";
		}
		
		
	}                 
	
	public List<Map<String, Object>> queryDealerBalanceList02(String  REMARK){
		StringBuffer sql= new StringBuffer();
		
		sql.append("select ") ;
		sql.append("sum(nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +nvl(t.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(t.PACKGE_CHANGE_SUM, 0)-nvl(t.BARCODE_SUM, 0)+nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(t.SERVICE_OTHER_AMOUNT, 0) \n" );
		sql.append("           ) LABOUR_AMOUNT,\n" );
		sql.append("       sum(nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) +nvl(t.FREE_PART_AMOUNT, 0)+\n" );
		sql.append("         nvl(t.PLUS_MINUS_DATUM_SUM, 0)+nvl(t.SPECIAL_DATUM_SUM, 0)- nvl(t.OLD_DEDUCT, 0)) PART_AMOUNT,\n" );
		
		sql.append("       sum(t.AMOUNT_SUM) AMOUNT_SUM,\n" );
		
		sql.append("       sum(nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) +nvl(t.FREE_PART_AMOUNT, 0)+\n" );
		sql.append("           nvl(t.PLUS_MINUS_DATUM_SUM,0)+ nvl(t.SPECIAL_DATUM_SUM,0) - nvl(t.OLD_DEDUCT,0))*0.9 PART_AMOUNT01 ,\n" );
		
		sql.append("sum((nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +nvl(t.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(t.PACKGE_CHANGE_SUM, 0)-nvl(t.BARCODE_SUM, 0)+nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(t.SERVICE_OTHER_AMOUNT, 0))+ (nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0)  \n" );
		sql.append("          + nvl(t.PLUS_MINUS_DATUM_SUM,0)+ nvl(t.SPECIAL_DATUM_SUM,0)+ nvl(t.FREE_PART_AMOUNT, 0)- nvl(t.OLD_DEDUCT,0) )*0.9) AMOUNT_SUM01,\n" );
		sql.append("       B.AREA_NAME,min(t.REMARK) REMARK,\n" );
		sql.append("  min(t.CREATE_DATE) CREATE_DATE ,min(DEALER_CODE) DEALER_CODE ");
		sql.append("\n" );
		sql.append("  from TT_AS_WR_CLAIM_BALANCE_FINE t,TM_BUSINESS_AREA B\n" );
		sql.append(" where 1 = 1 and t.YIELDLY =B.AREA_ID  and  B.AREA_ID = 2010010100000002 \n" );
		sql.append("  and  t.REMARK =  "+ REMARK);
		
		sql.append(" GROUP by  t.YIELDLY,B.AREA_NAME \n" );
		sql.append(" UNION All ") ;
		sql.append("select ") ;
		sql.append("sum(nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +nvl(t.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(t.PACKGE_CHANGE_SUM, 0)-nvl(t.BARCODE_SUM, 0)+nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(t.SERVICE_OTHER_AMOUNT, 0)  \n" );
		sql.append("           ) LABOUR_AMOUNT,\n" );
		sql.append("       sum(nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) + nvl(t.FREE_PART_AMOUNT, 0) +\n" );
		sql.append("         nvl(t.PLUS_MINUS_DATUM_SUM, 0)  +nvl(t.SPECIAL_DATUM_SUM, 0)- nvl(t.OLD_DEDUCT, 0)) PART_AMOUNT,\n" );
		
		sql.append("       sum(t.AMOUNT_SUM) AMOUNT_SUM,\n" );
		
		sql.append("       sum((nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_DATUM_SUM,0)+ nvl(t.SPECIAL_DATUM_SUM,0) +nvl(t.FREE_PART_AMOUNT, 0)- nvl(t.OLD_DEDUCT,0) )*0.9) PART_AMOUNT01 ,\n" );
		
		sql.append("  sum((nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +nvl(t.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(t.PACKGE_CHANGE_SUM, 0)-nvl(t.BARCODE_SUM, 0)+nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(t.SERVICE_OTHER_AMOUNT, 0))+ (nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0)  \n" );
		sql.append("          + nvl(t.PLUS_MINUS_DATUM_SUM,0)+ nvl(t.SPECIAL_DATUM_SUM,0)+ nvl(t.FREE_PART_AMOUNT, 0)- nvl(t.OLD_DEDUCT,0) )*0.9  ) AMOUNT_SUM01,\n" );
		sql.append("       min(B.AREA_NAME) AREA_NAME,min(t.REMARK) REMARK,\n" );
		sql.append("  min(t.CREATE_DATE) CREATE_DATE ,min(DEALER_CODE) DEALER_CODE ");
		sql.append("\n" );
		sql.append("  from TT_AS_WR_CLAIM_BALANCE_FINE t , TM_BUSINESS_AREA B\n" );
		
		sql.append(" where 1 = 1 and t.YIELDLY =B.AREA_ID     and  B.AREA_ID != 2010010100000002 \n" );
		sql.append(" and  t.REMARK =  "+ REMARK);

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
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
		sql.append("       (select count(1)  from  tt_as_wr_special_apply   sp where sp.dealer_id=A.DEALER_ID  and  sp.balance_no=A.BALANCE_NO) SPECIL_TYPE_01,\n" );
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
	
	
	public Map<String, Object> queryCLAIM(String FK_DEALER_ID,String startDate,String endDate)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT\n" );
		sql.append("     ( nvl(A.special_labour_sum,0)+\n" );
		sql.append("       nvl(A.special_datum_sum,0)-\n" );
		sql.append("       nvl(A.OLD_DEDUCT,0)\n" );
		sql.append("      ) otherAccount,\n" );
		sql.append("      A.YIELDLY,\n" );
		sql.append("  (  nvl(A.PLUS_MINUS_DATUM_SUM,0)+ nvl(A.PLUS_MINUS_LABOUR_SUM,0) ) PLUS_MINUS_SUM, ");
		sql.append(" to_char( ((  nvl(A.PLUS_MINUS_DATUM_SUM,0)+ nvl(A.PLUS_MINUS_LABOUR_SUM,0) )/1.17),'9999999.99') FINE_AMOUNT_OF_MONEY, ");
		sql.append(" to_char(((  nvl(A.PLUS_MINUS_DATUM_SUM,0)+ nvl(A.PLUS_MINUS_LABOUR_SUM,0) ) -((  nvl(A.PLUS_MINUS_DATUM_SUM,0)+ nvl(A.PLUS_MINUS_LABOUR_SUM,0) )/1.17)),'9999999.99') FINE_TAX_RATE_MONEY, ");
		
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
	
	
	
	
	
	
	public List<Map<String, Object>> queryDealerBalanceList02(auditBean bean){
		StringBuffer sql= new StringBuffer();
		
		sql.append("select ") ;
		sql.append("	round(sum(nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +nvl(t.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(t.PACKGE_CHANGE_SUM, 0)-nvl(t.BARCODE_SUM, 0)+nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(t.SERVICE_OTHER_AMOUNT, 0) \n" );
		sql.append("           ),2) LABOUR_AMOUNT,\n" );
		sql.append("       round(sum(nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) +nvl(t.FREE_PART_AMOUNT, 0)+\n" );
		sql.append("         nvl(t.PLUS_MINUS_DATUM_SUM, 0)+nvl(t.SPECIAL_DATUM_SUM, 0)- nvl(t.OLD_DEDUCT, 0)),2) PART_AMOUNT,\n" );
		
		sql.append("      round( sum(t.AMOUNT_SUM),2) AMOUNT_SUM,\n" );
		
		sql.append("       round(sum(nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) +nvl(t.FREE_PART_AMOUNT, 0)+\n" );
		sql.append("           nvl(t.PLUS_MINUS_DATUM_SUM,0)+ nvl(t.SPECIAL_DATUM_SUM,0) - nvl(t.OLD_DEDUCT,0))*0.9,2) PART_AMOUNT01 ,\n" );
		
		sql.append(" round(sum((nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +nvl(t.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(t.PACKGE_CHANGE_SUM, 0)-nvl(t.BARCODE_SUM, 0)+nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(t.SERVICE_OTHER_AMOUNT, 0))+ (nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0)  \n" );
		sql.append("          + nvl(t.PLUS_MINUS_DATUM_SUM,0)+ nvl(t.SPECIAL_DATUM_SUM,0)+ nvl(t.FREE_PART_AMOUNT, 0)- nvl(t.OLD_DEDUCT,0) )*0.9),2) AMOUNT_SUM01,\n" );
		sql.append("       B.AREA_NAME,min(t.REMARK) REMARK\n" );
		sql.append("\n" );
		sql.append("  from tt_as_wr_claim_balance t,TM_BUSINESS_AREA B\n" );
		sql.append(" where 1 = 1 and t.YIELDLY =B.AREA_ID  and  B.AREA_ID = 2010010100000002 \n" );
		
		/******mod by liuxh 20101115 ******/
		
		/******mod by liuxh 20101115 ******/
		if(bean.getYieldly() != "1")
		{
			sql.append(" and t.BALANCE_YIELDLY = "+bean.getYieldly()+"\n" );
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND t.DEALER_ID = ").append(bean.getDealerCode()).append("\n");
		}
		
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND t.START_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND t.END_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(" GROUP by  t.YIELDLY,B.AREA_NAME \n" );
		sql.append(" UNION All ") ;
		sql.append("select ") ;
		sql.append("round(sum(nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +nvl(t.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(t.PACKGE_CHANGE_SUM, 0)-nvl(t.BARCODE_SUM, 0)+nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(t.SERVICE_OTHER_AMOUNT, 0) + nvl(D.labour_sum,0) -  nvl(E.labour_sum,0) \n" );
		sql.append("           ),2) LABOUR_AMOUNT,\n" );
		sql.append("       round(sum(nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) + nvl(t.FREE_PART_AMOUNT, 0) +\n" );
		sql.append("         nvl(t.PLUS_MINUS_DATUM_SUM, 0) + nvl(D.datum_sum,0) - nvl(E.datum_sum,0) +nvl(t.SPECIAL_DATUM_SUM, 0)- nvl(t.OLD_DEDUCT, 0)),2) PART_AMOUNT,\n" );
		
		sql.append("      round( sum(t.AMOUNT_SUM + nvl(D.labour_sum,0) -  nvl(E.labour_sum,0) + nvl(D.datum_sum,0) - nvl(E.datum_sum,0)),2) AMOUNT_SUM,\n" );
		
		sql.append("      round( sum((nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_DATUM_SUM,0)+ nvl(t.SPECIAL_DATUM_SUM,0) +nvl(t.FREE_PART_AMOUNT, 0)- nvl(t.OLD_DEDUCT,0) + nvl(D.datum_sum,0) - nvl(E.datum_sum,0))*0.9),2) PART_AMOUNT01 ,\n" );
		
		sql.append(" round( sum((nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +nvl(t.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(t.PACKGE_CHANGE_SUM, 0)-nvl(t.BARCODE_SUM, 0)+nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(t.SERVICE_OTHER_AMOUNT, 0))+ (nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0)  \n" );
		sql.append("          + nvl(t.PLUS_MINUS_DATUM_SUM,0)+ nvl(t.SPECIAL_DATUM_SUM,0)+ nvl(t.FREE_PART_AMOUNT, 0)- nvl(t.OLD_DEDUCT,0)  + nvl(D.datum_sum,0) - nvl(E.datum_sum,0))*0.9 + nvl(D.labour_sum,0) -  nvl(E.labour_sum,0)  ),2) AMOUNT_SUM01,\n" );
		sql.append("       min(B.AREA_NAME) AREA_NAME,min(t.REMARK) REMARK\n" );
		sql.append("\n" );
		sql.append("  from tt_as_wr_claim_balance t \n" );
		sql.append(" left join TM_BUSINESS_AREA B on t.YIELDLY =B.AREA_ID ");
		sql.append(" LEFT join tt_as_wr_administrative_charge D on D.BALANCE_ODER = t.REMARK ");
		sql.append(" LEFT join tt_as_wr_administrative_charge E on E.id = t.charge_id ");
		sql.append(" where 1 = 1      and  B.AREA_ID != 2010010100000002 \n" );
		
		/******mod by liuxh 20101115 ******/
		
		/******mod by liuxh 20101115 ******/
		if(bean.getYieldly() != "1")
		{
			sql.append(" and t.BALANCE_YIELDLY = "+bean.getYieldly()+"\n" );
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND t.DEALER_ID = ").append(bean.getDealerCode()).append("\n");
		}
		
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND t.START_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND t.END_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		//sql.append(" GROUP by  t.YIELDLY,B.AREA_NAME \n" );

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	} 
	
	
	public List<Map<String, Object>> queryDealerBalanceList03(auditBean bean){
        StringBuffer sql= new StringBuffer();
        sql.append("select round((C.LABOUR_AMOUNT + nvl(D.labour_sum,0) -  nvl(E.labour_sum,0)),2) LABOUR_AMOUNT,round((C.PART_AMOUNT + nvl(D.datum_sum,0) - nvl(E.datum_sum,0) ),2) PART_AMOUNT,round((C.AMOUNT_SUM + nvl(D.labour_sum,0) -  nvl(E.labour_sum,0) + nvl(D.datum_sum,0) - nvl(E.datum_sum,0) ),2) AMOUNT_SUM , ");
        if(bean.getYieldly().equals("95411001") )
        {
        	sql.append(" round(((C.PART_AMOUNT + nvl(D.datum_sum,0) - nvl(E.datum_sum,0) )*0.9),2) PART_AMOUNT01,round((C.LABOUR_AMOUNT + nvl(D.labour_sum,0) -  nvl(E.labour_sum,0)+(C.PART_AMOUNT + nvl(D.datum_sum,0) - nvl(E.datum_sum,0) )*0.9 ),2)  AMOUNT_SUM01");
        }else if(bean.getYieldly().equals("95411002"))
        {
        	sql.append(" round(((C.PART_AMOUNT + nvl(D.datum_sum,0) - nvl(E.datum_sum,0) )*1.06),2) PART_AMOUNT01,round((C.LABOUR_AMOUNT + nvl(D.labour_sum,0) -  nvl(E.labour_sum,0)+(C.PART_AMOUNT + nvl(D.datum_sum,0) - nvl(E.datum_sum,0) )*1.06 ),2)  AMOUNT_SUM01");
        }
        
        
        sql.append(" from ");
		sql.append("(select ") ;
		sql.append("sum(nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +nvl(t.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(t.PACKGE_CHANGE_SUM, 0)-nvl(t.BARCODE_SUM, 0)+nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(t.SERVICE_OTHER_AMOUNT, 0) \n" );
		sql.append("           ) LABOUR_AMOUNT,\n" );
		sql.append("       sum(nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) +\n" );
		sql.append("         nvl(t.PLUS_MINUS_DATUM_SUM, 0)+nvl(t.SPECIAL_DATUM_SUM, 0)+nvl(t.FREE_PART_AMOUNT, 0)- nvl(t.OLD_DEDUCT, 0)) PART_AMOUNT,\n" );
		
		sql.append("       sum(t.AMOUNT_SUM) AMOUNT_SUM,\n" );
		
		sql.append("       sum(nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_DATUM_SUM,0)+ nvl(t.SPECIAL_DATUM_SUM,0)+ nvl(t.FREE_PART_AMOUNT, 0) - nvl(t.OLD_DEDUCT,0))*0.9 PART_AMOUNT01 ,\n" );
		
		sql.append("sum((nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +nvl(t.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(t.PACKGE_CHANGE_SUM, 0)-nvl(t.BARCODE_SUM, 0)+nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(t.SERVICE_OTHER_AMOUNT, 0))+ (nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0)  \n" );
		sql.append("          + nvl(t.PLUS_MINUS_DATUM_SUM,0)+ nvl(t.SPECIAL_DATUM_SUM,0)+nvl(t.FREE_PART_AMOUNT, 0) - nvl(t.OLD_DEDUCT,0) )*0.9) AMOUNT_SUM01,\n" );
		sql.append("       min(t.REMARK) REMARK , MIN(T.charge_id) charge_id \n" );
		sql.append("\n" );
		sql.append("  from tt_as_wr_claim_balance t,TM_BUSINESS_AREA B\n" );
		sql.append(" where 1 = 1 and t.YIELDLY =B.AREA_ID  \n" );
		
		/******mod by liuxh 20101115 ******/
		
		/******mod by liuxh 20101115 ******/
		if(!(bean.getYieldly().equals( "1")))
		{
			sql.append(" and t.BALANCE_YIELDLY = "+bean.getYieldly()+"\n" );
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND t.DEALER_ID = ").append(bean.getDealerCode()).append("\n");
		}
		
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND t.START_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND t.END_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(" ) C");
		sql.append(" LEFT join tt_as_wr_administrative_charge D on D.BALANCE_ODER = C.REMARK ");
		sql.append(" LEFT join tt_as_wr_administrative_charge E on E.id = C.charge_id ");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	} 
	
	
	public List<Map<String, Object>> queryDealerBalanceSendList01(auditBean bean)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select sum(t.AMOUNT_SUM) as AMOUNT_SUM,sum(t.SERVICE_CHARGE) SERVICE_CHARGE, sum(t.MATERIAL_CHARGE) MATERIAL_CHARGE, t.SECOND_DEALER_CODE\n" );
		sql.append("  from tt_as_wr_claim_send t where 1=1   ");
		if(bean.getYieldly() != "1")
		{
			sql.append(" and t.BALANCE_YIELDLY = "+bean.getYieldly()+"\n" );
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND t.DEALER_ID = ").append(bean.getDealerCode()).append("\n");
		}
		
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND  t.STARTDATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND t.ENDDATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(" GROUP by t.SECOND_DEALER_ID,t.SECOND_DEALER_CODE\n" );
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;

	}
public List dealerBalanOrder(String dealerId, String startTime, String endTime,String yieldly){
		
		List<Object> paramList = new ArrayList<Object>();
		
		StringBuilder sql= new StringBuilder();
		//查询非服务活动可结算金额
		sql.append(" SELECT sum(T.LABOUR_AMOUNT) LABOUR_AMOUNT,sum(T.PART_AMOUNT) PART_AMOUNT,T.CLAIM_TYPE from (");
		
		
		sql.append("SELECT (NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.BALANCE_APPENDLABOUR_AMOUNT),0)+NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0)+NVL(SUM( decode( A.CLAIM_TYPE  ,10661002,  mg.labour_price,0)),0)+NVL(SUM(A.CAMPAIGN_FEE),0)) LABOUR_AMOUNT,(NVL(SUM(A.BALANCE_PART_AMOUNT),0)+ NVL(SUM( decode( A.CLAIM_TYPE  ,10661002,mg.part_price,0)),0)) PART_AMOUNT,\n" );
		sql.append(" A.CLAIM_TYPE\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TT_AS_WR_MODEL_ITEM mi, tm_vehicle v ,tt_as_wr_model_group mg \n" );
		sql.append("WHERE 1=1\n" );
		sql.append(" and v.package_id = mi.model_id\n" );
		sql.append("      and mg.wrgroup_id = mi.wrgroup_id\n" );
		sql.append("     and v.vin=A.vin\n" );
		sql.append("     AND MG.WRGROUP_TYPE=10451001");
		sql.append("  AND A.DEALER_ID = ? \n");
		sql.append(" AND A.CLAIM_TYPE <> "+Constant.CLA_TYPE_06+"\n" );
		sql.append(" AND A.CLAIM_TYPE <> "+Constant.CLA_TYPE_01+"\n" );
		sql.append(" AND A.CLAIM_TYPE <> "+Constant.CLA_TYPE_09+"\n" );
		sql.append(" AND  A.STATUS not in (10791006,10791009,10791008,10791012,10791010,10791014,10791016)  ");
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
		sql.append(" GROUP by A.CLAIM_TYPE");
		sql.append(" UNION all ");
		
		sql.append("SELECT (NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0)+NVL(SUM(A.FREE_M_PRICE),0)+NVL(SUM(A.CAMPAIGN_FEE),0)) LABOUR_AMOUNT,NVL(null,0) PART_AMOUNT,\n" );
		sql.append(" A.CLAIM_TYPE\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A \n" );
		sql.append("WHERE 1=1\n" );
		sql.append(" AND A.DEALER_ID = ?\n");
		sql.append(" AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_09+"\n" );
		sql.append(" AND  A.STATUS not in (10791006,10791009,10791008,10791012,10791010,10791014,10791016)  ");
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
		sql.append(" GROUP by A.CLAIM_TYPE");
		
        sql.append(" UNION all ");
        
        sql.append(" SELECT sum(F.LABOUR_AMOUNT) LABOUR_AMOUNT,sum(F.PART_AMOUNT) PART_AMOUNT,F.CLAIM_TYPE from (");
        sql.append("SELECT (NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.BALANCE_APPENDLABOUR_AMOUNT),0)+NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0)+NVL(SUM(A.FREE_M_PRICE),0)+NVL(SUM(A.CAMPAIGN_FEE),0)) LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) PART_AMOUNT,\n" );
		sql.append(" A.CLAIM_TYPE\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A \n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_01+"\n" );
		sql.append("AND  A.STATUS not in (10791006,10791009,10791008,10791012,10791010,10791014,10791016)  ");
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
		sql.append(" GROUP by A.CLAIM_TYPE");
		sql.append(" UNION all ");
		sql.append("SELECT (NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.BALANCE_APPENDLABOUR_AMOUNT),0)) LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) PART_AMOUNT,\n" );
		sql.append(" decode(A.CLAIM_TYPE,10661009,10661001,10661001)\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A \n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_09+"\n" );
		sql.append("AND  A.STATUS not in (10791006,10791009,10791008,10791012,10791010,10791014,10791016)  ");
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
		sql.append(" GROUP by A.CLAIM_TYPE");
		sql.append(" ) F ");
		sql.append(" GROUP by F.CLAIM_TYPE");
		
		sql.append(" UNION all ");
		
		
		sql.append("SELECT (NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.BALANCE_APPENDLABOUR_AMOUNT),0)+NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0)+NVL(SUM(A.FREE_M_PRICE),0)+NVL(SUM(A.CAMPAIGN_FEE),0)) LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) PART_AMOUNT,\n" );
		sql.append(" A.CLAIM_TYPE\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A \n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_06+"\n" );
		sql.append("AND  A.STATUS not in (10791006,10791009,10791008,10791012,10791010,10791014,10791016)  ");
		sql.append(" AND A.BALANCE_YIELDLY = "+yieldly+"  " );
		sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		paramList.add(dealerId);
		if(Utility.testString(startTime)){
			paramList.add(startTime);
			sql.append("AND A.REPORT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(Utility.testString(endTime)){
			paramList.add(endTime);
			sql.append("AND A.REPORT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		sql.append(" GROUP by A.CLAIM_TYPE ) T");
		sql.append(" GROUP by T.CLAIM_TYPE ");
		
		return this.pageQuery(sql.toString(), paramList, this.getFunName());
	}

public Map<String, Object> getorder(String dealerId, String startTime, String endTime,String yieldly)
{
	StringBuffer sql= new StringBuffer();
	sql.append("SELECT to_char(MAX(R.IN_WARHOUSE_DATE),'yyyy-mm-dd') IN_WARHOUSE_DATE FROM TT_AS_WR_OLD_RETURNED R,tt_as_wr_old_returned_detail A , TT_AS_WR_APPLICATION B \n" );
	sql.append("WHERE   r.id = A.return_id  \n" );
	sql.append("AND       B.CLAIM_NO = A.CLAIM_NO and B.REPORT_DATE >= to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS')  and  B.REPORT_DATE <= to_date('"+endTime+"','YYYY-MM-DD HH24:MI:SS')  \n" );
	sql.append("and r.yieldly="+yieldly+"\n" );
	sql.append("and r.dealer_id="+dealerId);
	System.out.println(sql.toString());
	return this.pageQueryMap(sql.toString(),null, this.getFunName());
}
public Map<String, Object> gettitkes(String dealerId, String startTime, String endTime,String yieldly)
{
	StringBuffer sql= new StringBuffer();
	sql.append("select sum(t.APLCOUNT) APLCOUNT,\n" );
	sql.append("       to_char(max(t.ENDDATE), 'yyyy-mm-dd') ENDDATE\n" );
	sql.append("  from tt_as_wr_tickets t\n" );
	sql.append(" where to_date(substr(t.GOODSNUM,\n" );
	sql.append("                      length(t.GOODSNUM) - 5,\n" );
	sql.append("                      length(t.GOODSNUM)),\n" );
	sql.append("               'yyyy-mm') >= to_date('"+startTime+"', 'yyyy-mm')\n" );
	sql.append("\n" );
	sql.append("   and to_date(substr(t.GOODSNUM,\n" );
	sql.append("                      length(t.GOODSNUM) - 5,\n" );
	sql.append("                      length(t.GOODSNUM)),\n" );
	sql.append("               'yyyy-mm') <= to_date('"+endTime+"', 'yyyy-mm')\n" );
	sql.append("\n" );
	sql.append("   and t.BALANCE_YIELDLY = "+yieldly+"\n" );
	sql.append("   and t.DEALERID = "+ dealerId);
	return this.pageQueryMap(sql.toString(),null, this.getFunName());
}


public List dealerDiscountOrder(String dealerId, String startTime, String endTime,String yieldly){
	
	List<Object> paramList = new ArrayList<Object>();
	
	StringBuilder sql= new StringBuilder();
	//查询非服务活动可结算金额
	sql.append(" SELECT sum(T.DISCOUNT) DISCOUNT from (");
	sql.append("SELECT sum(A.DISCOUNT) DISCOUNT\n" );
	sql.append("FROM TT_AS_WR_APPLICATION A \n" );
	sql.append("WHERE 1=1\n" );
	sql.append("AND A.DEALER_ID = ?\n");
	sql.append("AND A.CLAIM_TYPE <> "+Constant.CLA_TYPE_06+"\n" );
	sql.append("AND  A.STATUS  in (10791010,10791014,10791016)  ");
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
	
	sql.append(" UNION all ");
	
	sql.append("SELECT sum(A.DISCOUNT) DISCOUNT\n" );
	sql.append("FROM TT_AS_WR_APPLICATION A \n" );
	sql.append("WHERE 1=1\n" );
	sql.append("AND A.DEALER_ID = ?\n");
	sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_06+"\n" );
	sql.append("AND  A.STATUS  in (10791010,10791014,10791016)  ");
	sql.append(" AND A.BALANCE_YIELDLY = "+yieldly+"  " );
	sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
	paramList.add(dealerId);
	if(Utility.testString(startTime)){
		paramList.add(startTime);
		sql.append("AND A.REPORT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
	}
	if(Utility.testString(endTime)){
		paramList.add(endTime);
		sql.append("AND A.REPORT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
	}
	sql.append(") T");
	return this.pageQuery(sql.toString(), paramList, this.getFunName());
}
	
	
	
	public List<Map<String, Object>> queryDealerBalanceList01(auditBean bean){
		StringBuffer sql= new StringBuffer();
	    sql.append(" select B.DEALER_CODE,B.SERVICE_TOTAL_AMOUNT_MARKET,B.plus_minus_datum_sum,B.plus_minus_labour_sum,B.RETURN_AMOUNT,  ");
	    sql.append(" B.PACKGE_CHANGE_SUM,B.BARCODE_sum,B.special_datum_sum,B.special_labour_sum, ");
	    sql.append(" (B.ALL_LABOUR_AMOUNT + nvl(A.labour_sum,0) -nvl(C.labour_sum,0) ) ALL_LABOUR_AMOUNT, (B.ALL_PART_AMOUNT+ nvl(A.datum_sum,0) -nvl(C.datum_sum,0)) ALL_PART_AMOUNT,nvl(A.datum_sum,0) datum_sum , nvl(A.labour_sum,0) labour_sum,nvl(C.datum_sum,0) datum_sum1,nvl(C.labour_sum,0) labour_sum1 from ");
		sql.append("(select t.DEALER_CODE,t.START_DATE,t.REMARK,min(t.charge_id) charge_id ,sum(nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_LABOUR_AMOUNT, 0)  +\n" );
		sql.append("           - nvl(t.OLD_DEDUCT, 0)) LABOUR_AMOUNT,\n" );
		sql.append("       sum(nvl(t.PART_AMOUNT, 0)) PART_AMOUNT,\n" );
		sql.append("       sum(nvl(t.SERVICE_LABOUR_AMOUNT, 0) +  nvl(t.SERVICE_OTHER_AMOUNT, 0)) SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("       sum(nvl(t.SERVICE_PART_AMOUNT, 0)) SERVICE_PART_AMOUNT,\n" );
		sql.append("       sum(  nvl(t.SERVICE_LABOUR_AMOUNT_MARKET, 0) + nvl(t.SERVICE_OTHER_AMOUNT_MARKET, 0)) SERVICE_LABOUR_AMOUNT_MARKET,\n" );
		sql.append("       sum(nvl(t.SERVICE_PART_AMOUNT_MARKET,0) ) SERVICE_PART_AMOUNT_MARKET,\n" );
		sql.append("       sum( nvl(t.RETURN_AMOUNT, 0) ) RETURN_AMOUNT,\n" );
		sql.append(" sum( nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) ) SERVICE_TOTAL_AMOUNT_MARKET ," );
		sql.append("       sum(nvl(t.plus_minus_datum_sum, 0)) plus_minus_datum_sum,\n" );
		sql.append("       sum( nvl(t.plus_minus_labour_sum , 0) ) plus_minus_labour_sum,\n" );
		sql.append("       sum(nvl(t.PACKGE_CHANGE_SUM   , 0)) PACKGE_CHANGE_SUM ,\n" );
		sql.append("       sum( nvl(t.BARCODE_sum  , 0) ) BARCODE_sum ,\n" );
		
		sql.append("       sum(nvl(t.special_datum_sum  , 0)) special_datum_sum,\n" );
		sql.append("       sum( nvl(t.special_labour_sum , 0) ) special_labour_sum,\n" );
		
		sql.append("  sum(nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +nvl(t.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(t.PACKGE_CHANGE_SUM, 0)-nvl(t.BARCODE_SUM, 0)+nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(t.SERVICE_OTHER_AMOUNT, 0) ) ALL_LABOUR_AMOUNT ,\n" );
		
		sql.append("       sum(nvl(t.PART_AMOUNT, 0) + nvl(t.SERVICE_PART_AMOUNT, 0) +\n" );
		sql.append("         nvl(t.PLUS_MINUS_DATUM_SUM, 0)+nvl(t.SPECIAL_DATUM_SUM, 0)+nvl(t.FREE_PART_AMOUNT, 0)- nvl(t.OLD_DEDUCT, 0)) ALL_PART_AMOUNT \n" );

		sql.append("\n" );
		sql.append("  from tt_as_wr_claim_balance t\n" );
		sql.append(" where 1 = 1 ");
		/******mod by liuxh 20101115 ******/
		
		/******mod by liuxh 20101115 ******/
		if(bean.getYieldly() != "1")
		{
			sql.append(" and t.BALANCE_YIELDLY = "+bean.getYieldly()+"\n" );
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND t.DEALER_ID = ").append(bean.getDealerCode()).append("\n");
		}
		
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND  t.START_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND t.END_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(" GROUP by t.DEALER_ID, t.START_DATE, t.END_DATE,t.DEALER_CODE,t.REMARK ) B \n" );
		sql.append(" LEFT join tt_as_wr_administrative_charge A on A.BALANCE_ODER = B.REMARK ");
		sql.append(" LEFT join tt_as_wr_administrative_charge C on A.id = B.charge_id ");
		
		sql.append(" ORDER by  B.START_DATE asc");

		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	} 
	
	
	public List<Map<String, Object>> getClaimtypeCount(auditBean bean)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select t.CLAIM_TYPE CLAIM_TYPE, COUNT(*) conut\n" );
		sql.append("  from tt_as_wr_application t\n" );
		sql.append(" where 1=1  ");
		if(bean.getYieldly() != "1")
		{
			sql.append(" and t.BALANCE_YIELDLY = "+bean.getYieldly()+"\n" );
		}
		sql.append("\n" );
		if(Utility.testString(bean.getDealerCode()))
		{
			sql.append(" and t.DEALER_ID = "+bean.getDealerCode()+"\n" );
		}
		
		if(Utility.testString(bean.getStartDate())){
			sql.append(" AND  t.REPORT_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append(" AND t.REPORT_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("   and t.STATUS not in (10791009, 10791010, 10791014,10791016)\n" );
		sql.append(" and t.CLAIM_TYPE != 10661006");
		sql.append(" GROUP by t.CLAIM_TYPE");
		sql.append(" UNION ");
		sql.append("select t.CLAIM_TYPE, COUNT(*) conut\n" );
		sql.append("  from tt_as_wr_application t\n" );
		sql.append(" where 1=1  ");
		if(bean.getYieldly() != "1")
		{
			sql.append(" and t.BALANCE_YIELDLY = "+bean.getYieldly()+"\n" );
		}
		sql.append("\n" );
		if(Utility.testString(bean.getDealerCode()))
		{
			sql.append(" and t.DEALER_ID = "+bean.getDealerCode()+"\n" );
		}
		if(Utility.testString(bean.getStartDate())){
			sql.append(" AND  t.REPORT_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append(" AND t.REPORT_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		 sql.append("   and t.STATUS not in (10791009, 10791010, 10791014,10791016)\n" );
		sql.append(" and t.CLAIM_TYPE = 10661006");
		sql.append(" AND t.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		sql.append(" GROUP by t.CLAIM_TYPE");
		
		
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	
	
	
	public PageResult<Map<String, Object>> queryAccAuditList(auditBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();

		sql.append("WITH person as (\n");
		sql.append("select distinct aa.balance_id,aa.auth_person_name,auth_status from tt_as_wr_balance_authitem aa where aa.auth_status=11861004\n" );
		sql.append(")");

		sql.append("SELECT /*+RULE*/ROWNUM NUM,r.region_name, a.ID, a.BALANCE_NO, a.DEALER_CODE, a.DEALER_NAME, NVL(a.CLAIM_COUNT,0) AS CLAIM_COUNT,\n" );
		sql.append("       TO_CHAR(a.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, a.STATUS ,NVL(a.SPEC_FEE_COUNT,0) SPEC_FEE_COUNT,");
		sql.append("       NVL(a.SPEC_FEE_AUTH_COUNT,0) SPEC_FEE_AUTH_COUNT,TO_CHAR(t.BEGIN_BALANCE_DATE,'YYYY-MM-DD') AS BEGIN_BALANCE_DATE,TO_CHAR(t.END_BALANCE_DATE,'YYYY-MM-DD') AS END_BALANCE_DATE,\n" );
		
		/*********add by liuxh 20101127 增加索赔单已审核数量，特殊费用单数量和已审核数量************/
		sql.append("(SELECT COUNT (1) FROM tt_as_wr_application b, tr_balance_claim c WHERE b.ID = c.claim_id\n ");
		sql.append(" AND c.balance_id = a.ID AND b.status = "+Constant.CLAIM_APPLY_ORD_TYPE_07+") AS claim_count_al,\n");
		sql.append("(SELECT COUNT(1) FROM TT_AS_WR_SPEFEE D WHERE D.CLAIMBALANCE_ID=A.ID) AS SP_COUNT,\n");
		sql.append("(SELECT COUNT(1) FROM TT_AS_WR_SPEFEE D WHERE D.CLAIMBALANCE_ID=A.ID AND D.STATUS IN("+Constant.SPEFEE_STATUS_06+","+Constant.SPEFEE_STATUS_05+")) AS SP_COUNT_AL,\n");
		/*********add by liuxh 20101127 增加索赔单已审核数量，特殊费用单数量和已审核数量**********/
		
		sql.append("TO_CHAR(a.START_DATE,'YYYY-MM-DD') AS START_DATE,TO_CHAR(a.END_DATE,'YYYY-MM-DD') AS END_DATE, p.auth_person_name\n");
		sql.append(" FROM TT_AS_WR_CLAIM_BALANCE a ,tm_dealer t,tm_region r ,person p\n");
		sql.append("WHERE 1=1\n");
		sql.append(" AND a.YIELDLY IN (").append(bean.getYieldlys()).append(") and a.dealer_id=t.dealer_id\n");
		sql.append(" and t.province_id = r.region_code\n");
		if(Utility.testString(bean.getDealerCode())){
			//拼字符串(同时查询多个经销商，不可模糊)sql.append("AND a.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
			sql.append("AND a.DEALER_CODE LIKE '%").append(bean.getDealerCode()).append("%'\n");
		}
		
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND a.CREATE_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND a.CREATE_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND a.YIELDLY = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getBalanceNo())){
			sql.append("AND a.BALANCE_NO LIKE '%").append(bean.getBalanceNo()).append("%'\n");
		}
		if(Utility.testString(bean.getStatus())){
			sql.append("AND a.STATUS = ").append(bean.getStatus()).append("\n");
		}else{
			sql.append("and a.status not in ("+Constant.ACC_STATUS_01+","+Constant.ACC_STATUS_07+","+Constant.ACC_STATUS_08+","+Constant.ACC_STATUS_09+")\n");
		}
		sql.append("and  a.id=p.balance_id(+)");
		if(Utility.testString(bean.getPerson())){
			sql.append("and p.auth_person_name='"+bean.getPerson()+"'");
		}
		sql.append(" order by a.update_date desc\n");
		

		System.out.println("sqlsql=="+sql);
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> queryDealerBalanceList(auditBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.ID, A.BALANCE_NO, D.DEALER_CODE, D.DEALER_NAME, A.CLAIM_COUNT,\n" );
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, \n" );
		sql.append("	   TO_CHAR(A.BALANCE_AMOUNT, 'FM99,999,990.00') BALANCE_AMOUNT,\n");
		sql.append("	   TO_CHAR(A.FINANCIAL_DEDUCT, 'FM99,999,990.00') FINANCIAL_DEDUCT,\n");
		sql.append("	   TO_CHAR(A.NOTE_AMOUNT, 'FM99,999,990.00') NOTE_AMOUNT,\n");
		sql.append("	   A.FUNANCIAL_REMARK, A.STATUS,E.DEALER_NAME AS INVOICE_MAKER,TO_CHAR(A.START_DATE,'YYYY-MM-DD') START_DATE,\n");
		sql.append("	   TO_CHAR(A.END_DATE,'YYYY-MM-DD') END_DATE,C.AUTH_PERSON_NAME,TO_CHAR(C.CREATE_DATE,'YYYY-MM-DD') AUTH_DATE\n");
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE A,TT_LABOR_LIST_DETAIL B,TT_TAXABLE_SUM_AUTHITEM C,TM_DEALER D,TM_DEALER E \n");
		sql.append(" WHERE 1 = 1\n" );
		sql.append(" AND A.DEALER_ID=D.DEALER_ID AND A.KP_DEALER_ID=E.DEALER_ID \n");
		sql.append(" AND A.BALANCE_NO = B.BALANCE_NO(+)\n" );
		sql.append(" AND B.REPORT_ID = C.BALANCE_ID(+)\n" );
		sql.append(" AND C.AUTH_STATUS(+) = "+Constant.TAXABLE_SERVICE_SUM_GET+"\n");
		
		/******mod by liuxh 20101115 ******/
		//sql.append("AND A.DEALER_ID = ").append(bean.getDealerCode()).append("\n");
		sql.append("AND A.TOP_DEALER_ID = ").append(bean.getDealerCode()).append("\n");
		/******mod by liuxh 20101115 ******/
		
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND A.CREATE_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND A.CREATE_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND A.YIELDLY = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getBalanceNo())){
			sql.append("AND A.BALANCE_NO LIKE '%").append(bean.getBalanceNo()).append("%'\n");
		}
		sql.append("AND A.STATUS IN (").append(Constant.ACC_STATUS_04).append(",").append(Constant.ACC_STATUS_05).append(")\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	} 
	
	public PageResult<Map<String, Object>> queryOemBalanceList(auditBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM,r.region_name, A.ID, A.BALANCE_NO, A.DEALER_CODE, A.DEALER_NAME, A.CLAIM_COUNT,\n" );
		sql.append("        to_char(ba.auth_time,'YYYY-MM-DD') create_date, \n" );
		sql.append("	   TO_CHAR(A.BALANCE_AMOUNT, 'FM99,999,990.00') BALANCE_AMOUNT,\n");
		sql.append("	   TO_CHAR(A.FINANCIAL_DEDUCT, 'FM99,999,990.00') FINANCIAL_DEDUCT,\n");
		sql.append("	   TO_CHAR(A.NOTE_AMOUNT, 'FM99,999,990.00') NOTE_AMOUNT,\n");
		sql.append("	   A.FUNANCIAL_REMARK, A.STATUS,A.INVOICE_MAKER,TO_CHAR(A.START_DATE,'YYYY-MM-DD') START_DATE,\n");
		sql.append("	   TO_CHAR(A.END_DATE,'YYYY-MM-DD') END_DATE,C.AUTH_PERSON_NAME,TO_CHAR(C.CREATE_DATE,'YYYY-MM-DD') AUTH_DATE\n");
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE A,TT_LABOR_LIST_DETAIL B,TT_TAXABLE_SUM_AUTHITEM C,tm_region r,Tt_As_Wr_Balance_Authitem ba \n");
		sql.append(" WHERE 1 = 1\n" );
		sql.append(" AND A.BALANCE_NO = B.BALANCE_NO(+)\n" );
		sql.append(" AND B.REPORT_ID = C.BALANCE_ID(+)\n" );
		sql.append(" and r.region_code = a.province\n");
		sql.append("and ba.balance_id = a.id\n" );
		sql.append("and ba.auth_status=11861007");

		sql.append(" AND C.AUTH_STATUS(+) = "+Constant.TAXABLE_SERVICE_SUM_GET+"\n");
		sql.append(" AND A.STATUS IN (").append(Constant.ACC_STATUS_04).append(",").append(Constant.ACC_STATUS_05).append(")\n");
		sql.append(" AND A.YIELDLY IN (").append(bean.getYieldlys()).append(")\n");
		
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND ba.auth_time >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND ba.auth_time <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND A.YIELDLY = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getBalanceNo())){
			sql.append("AND A.BALANCE_NO LIKE '%").append(bean.getBalanceNo()).append("%'\n");
		}
		if(Utility.testString(bean.getDealerId())){
			sql.append("AND A.DEALER_ID IN (").append(bean.getDealerId()).append(")\n");
		}

		sql.append("ORDER BY A.ID DESC");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> queryOemBalanceListStop(auditBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM,r.region_name, A.ID, A.BALANCE_NO, A.DEALER_CODE, A.DEALER_NAME, A.CLAIM_COUNT,\n" );
		sql.append("        to_char(ba.auth_time,'YYYY-MM-DD') create_date, \n" );
		sql.append("	   TO_CHAR(A.BALANCE_AMOUNT, 'FM99,999,990.00') BALANCE_AMOUNT,\n");
		sql.append("	   TO_CHAR(A.FINANCIAL_DEDUCT, 'FM99,999,990.00') FINANCIAL_DEDUCT,\n");
		sql.append("	   TO_CHAR(A.NOTE_AMOUNT, 'FM99,999,990.00') NOTE_AMOUNT,\n");
		sql.append("	   A.FUNANCIAL_REMARK, A.STATUS,A.INVOICE_MAKER,TO_CHAR(A.START_DATE,'YYYY-MM-DD') START_DATE,\n");
		sql.append("	   TO_CHAR(A.END_DATE,'YYYY-MM-DD') END_DATE,C.AUTH_PERSON_NAME,TO_CHAR(C.CREATE_DATE,'YYYY-MM-DD') AUTH_DATE,(select count(1) from tt_labor_list_detail ld where ld.balance_id=a.id) cou,nvl(bs.status,0) stopStatus \n");
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE A,TT_LABOR_LIST_DETAIL B,TT_TAXABLE_SUM_AUTHITEM C,tm_region r,Tt_As_Wr_Balance_Authitem ba,Tt_As_Wr_Balance_Stop bs  \n");
		sql.append(" WHERE 1 = 1\n" );
		sql.append(" AND A.BALANCE_NO = B.BALANCE_NO(+)\n" );
		sql.append(" AND B.REPORT_ID = C.BALANCE_ID(+)\n" );
		sql.append(" and r.region_code = a.province and a.id=bs.balance_id(+)\n");
		sql.append("and ba.balance_id = a.id\n" );
		sql.append("and ba.auth_status=11861007");

		sql.append(" AND C.AUTH_STATUS(+) = "+Constant.TAXABLE_SERVICE_SUM_GET+"\n");
		sql.append(" AND A.STATUS IN (").append(Constant.ACC_STATUS_04).append(",").append(Constant.ACC_STATUS_05).append(")\n");
		sql.append(" AND A.YIELDLY IN (").append(bean.getYieldlys()).append(")\n");
		
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND ba.auth_time >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND ba.auth_time <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND A.YIELDLY = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getBalanceNo())){
			sql.append("AND A.BALANCE_NO LIKE '%").append(bean.getBalanceNo()).append("%'\n");
		}
		if(Utility.testString(bean.getDealerId())){
			sql.append("AND A.DEALER_ID IN (").append(bean.getDealerId()).append(")\n");
		}
		if(Utility.testString(bean.getStatus())){
			if("1".equals(bean.getStatus())){
				sql.append("and exists (select 1 from Tt_As_Wr_Balance_Stop h where h.balance_id=a.id and h.status=1)\n");
			}else{
				sql.append("and not exists (select 1 from Tt_As_Wr_Balance_Stop h where h.balance_id=a.id and h.status=1)\n");
			}
		}

		sql.append("ORDER BY A.ID DESC");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> queryFinancialMain(auditBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT /*+INDEX(TT_AS_WR_CLAIM_BALANCE,IDX_TT_CLAIM_BALANCE_STATUS)*/ROWNUM NUM, ID, BALANCE_NO, DEALER_CODE, DEALER_NAME, CLAIM_COUNT,\n" );
		sql.append("       TO_CHAR(CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, STATUS,REVIEW_APPLICATION_BY,TO_CHAR(REVIEW_APPLICATION_TIME,'YYYY-MM-DD') AS REVIEW_APPLICATION_TIME\n" );
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE\n");
		sql.append("WHERE 1=1\n");
		sql.append(" AND YIELDLY IN (").append(bean.getYieldlys()).append(")\n");
		
		if(Utility.testString(bean.getDealerCode())){
			//拼字符串(同时查询多个经销商，不可模糊)sql.append("AND DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
			sql.append("AND DEALER_CODE LIKE '%").append(bean.getDealerCode()).append("%'\n");
		}
		/*********iverson add by 2011-01-20***************/
		if(Utility.testString(bean.getReviewBy())){
			sql.append("AND REVIEW_APPLICATION_BY LIKE '%").append(bean.getReviewBy()).append("%'\n");
		}
		if(Utility.testString(bean.getReviewBeginDate())){
			sql.append("AND REVIEW_APPLICATION_TIME >= TO_DATE('").append(bean.getReviewBeginDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getReviewEndDate())){
			sql.append("AND REVIEW_APPLICATION_TIME <= TO_DATE('").append(bean.getReviewEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		/*********iverson add by 2011-01-20***************/
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND CREATE_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND CREATE_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND YIELDLY = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getBalanceNo())){
			sql.append("AND BALANCE_NO LIKE '%").append(bean.getBalanceNo()).append("%'\n");
		}
		if(Utility.testString(bean.getStatus())){
			sql.append("AND STATUS = ").append(bean.getStatus());
		}else{
			sql.append("and status not in ("+Constant.ACC_STATUS_07+","+Constant.ACC_STATUS_08+","+Constant.ACC_STATUS_09+")\n");
		}
		
		//sql.append("AND STATUS IN ( ").append(Constant.ACC_STATUS_03).append(",").append(Constant.ACC_STATUS_04).append(")\n");
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
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
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE A,\n" );
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
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS SQ_REPAIR,\n"); //售前维修
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS YB_REPAIR,\n"); //一般维修
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS WC_REPAIR,\n"); //外出维修
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS BY_REPAIR,\n"); //免费保养
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS FW_REPAIR,\n"); //服务活动
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_SPEFEE K WHERE K.FEE_TYPE=? AND K.CLAIMBALANCE_ID=A.ID) AS SPEC_OUT_COUNT,\n"); //特殊外出数量
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_SPEFEE K WHERE K.FEE_TYPE=? AND K.CLAIMBALANCE_ID=A.ID) AS SPEC_FEE_COUNT,\n"); //特殊费用数量
		sql.append("      ((SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID)+");//总单据数 索赔单+特殊费用
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_SPEFEE K WHERE K.CLAIMBALANCE_ID=A.ID)) AS ALL_COUNT,");//总单据数 索赔单+特殊费用
		
		sql.append("      (SELECT SUM(NVL(K.DECLARE_SUM,0)) FROM TT_AS_WR_SPEFEE K WHERE K.FEE_TYPE=? AND K.CLAIMBALANCE_ID=A.ID) AS SPEC_FEE_MARKET,\n"); //特殊外出费用
		sql.append("      (SELECT SUM(NVL(K.DECLARE_SUM,0)) FROM TT_AS_WR_SPEFEE K WHERE K.FEE_TYPE=? AND K.CLAIMBALANCE_ID=A.ID) AS SPEC_FEE_OUT,\n"); //特殊费用 市场公单
		
		/*********add by liuxh 20101209 增加汇总结算单打印取值***********/
		sql.append("	   (NVL(A.SERVICE_FIXED_AMOUNT, 0) + NVL(A.SERVICE_LABOUR_AMOUNT, 0) + NVL(A.SERVICE_PART_AMOUNT, 0) + NVL(A.SERVICE_OTHER_AMOUNT, 0)) SERVICE_AMOUNT,");
		sql.append("       TO_CHAR(A.END_DATE, 'YYYY-MM-DD') ENDDATE,\n" );
		sql.append("	   NVL(A.NOTE_AMOUNT, A.BALANCE_AMOUNT) NOTEAMOUNT,a.invoice_maker\n");
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE A\n" );
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
	public Map<String, Object> getBalanceMainMapView2(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.AUTH_PERSON_NAME,B.AUTH_TIME,b.auth_status,A.*, (NVL(A.FREE_DEDUCT,0) + NVL(A.SERVICE_DEDUCT, 0) + NVL(A.CHECK_DEDUCT,0) + NVL(A.OLD_DEDUCT,0) + NVL(A.ADMIN_DEDUCT,0)) DEDUCT,\n" );
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATEDATE,\n" );
		sql.append("       TO_CHAR(A.START_DATE, 'YYYY-MM-DD') STARTDATE,\n" );
		sql.append("	   (NVL(A.MARKET_AMOUNT_bak, 0) + NVL(A.APPEND_AMOUNT_bak, 0)) DECLARE_SUM1, \n");
		sql.append("     (select O.REGION_NAME FROM TM_REGION O WHERE O.REGION_CODE = A.PROVINCE) as PROVINCE_NAME,");
		/*********add by liuxh 20101209 增加汇总结算单打印取值***********/
		sql.append("      (SELECT K.DEALER_CODE FROM TM_DEALER K WHERE K.DEALER_ID=A.DEALER_ID) AS DEALER_CODE_WX,"); //维修站代码
		sql.append("      (SELECT K.DEALER_NAME FROM TM_DEALER K WHERE K.DEALER_ID=A.DEALER_ID) AS DEALER_NAME_WX,"); //维修站名称
		sql.append("      (SELECT K.DEALER_CODE FROM TM_DEALER K WHERE K.DEALER_ID=A.KP_DEALER_ID) AS DEALER_CODE_KP,A.STATIONER_TEL,"); //开票单位代码 . 站长电话
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS SQ_REPAIR,\n"); //售前维修
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS YB_REPAIR,\n"); //一般维修
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS WC_REPAIR,\n"); //外出维修
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS BY_REPAIR,\n"); //免费保养
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID AND K.CLAIM_TYPE=?) AS FW_REPAIR,\n"); //服务活动
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_SPEFEE K WHERE K.FEE_TYPE=? AND K.CLAIMBALANCE_ID=A.ID) AS SPEC_OUT_COUNT,\n"); //特殊外出数量
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_SPEFEE K WHERE K.FEE_TYPE=? AND K.CLAIMBALANCE_ID=A.ID) AS SPEC_FEE_COUNT2,\n"); //特殊费用数量
		sql.append("      ((SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_APPLICATION K,TR_BALANCE_CLAIM L WHERE K.ID=L.CLAIM_ID AND L.BALANCE_ID=A.ID)+");//总单据数 索赔单+特殊费用
		sql.append("      (SELECT /*+RULE*/COUNT(1) FROM TT_AS_WR_SPEFEE K WHERE K.CLAIMBALANCE_ID=A.ID)) AS ALL_COUNT,");//总单据数 索赔单+特殊费用
		
		sql.append("      (SELECT SUM(NVL(K.DECLARE_SUM,0)) FROM TT_AS_WR_SPEFEE K WHERE K.FEE_TYPE=? AND K.CLAIMBALANCE_ID=A.ID) AS SPEC_FEE_MARKET,\n"); //特殊外出费用
		sql.append("      (SELECT SUM(NVL(K.DECLARE_SUM,0)) FROM TT_AS_WR_SPEFEE K WHERE K.FEE_TYPE=? AND K.CLAIMBALANCE_ID=A.ID) AS SPEC_FEE_OUT,\n"); //特殊费用 市场公单
		
		/*********add by liuxh 20101209 增加汇总结算单打印取值***********/
		sql.append("	   (NVL(A.SERVICE_FIXED_AMOUNT, 0) + NVL(A.SERVICE_LABOUR_AMOUNT, 0) + NVL(A.SERVICE_PART_AMOUNT, 0) + NVL(A.SERVICE_OTHER_AMOUNT, 0)) SERVICE_AMOUNT,");
		sql.append("       TO_CHAR(A.END_DATE, 'YYYY-MM-DD') ENDDATE,\n" );
		sql.append("	   NVL(A.NOTE_AMOUNT, A.BALANCE_AMOUNT) NOTEAMOUNT,a.invoice_maker\n");
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE A\n" );
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
	
	public List<Map<String, Object>> getBalanceMainList(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.*, (NVL(A.SERVICE_FIXED_AMOUNT, 0) + NVL(A.SERVICE_LABOUR_AMOUNT, 0) + NVL(A.SERVICE_PART_AMOUNT, 0) + NVL(A.SERVICE_OTHER_AMOUNT, 0)) SERVICE_AMOUNT FROM Tt_As_Wr_Balance_Detail_Bak A ");
		sql.append("WHERE BALANCE_ID = ").append(id);
		logger.info("------------"+sql.toString());
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public List<Map<String, Object>> getBalanceMainList1(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.*, (NVL(A.SERVICE_FIXED_AMOUNT, 0) + NVL(A.SERVICE_LABOUR_AMOUNT, 0) + NVL(A.SERVICE_PART_AMOUNT, 0) + NVL(A.SERVICE_OTHER_AMOUNT, 0)) SERVICE_AMOUNT FROM Tt_As_Wr_Claim_Balance_Detail A ");
		sql.append("WHERE BALANCE_ID = ").append(id);
		logger.info("------------"+sql.toString());
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public String getNewVar(String id, String var){
		String i = "";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT VAR FROM TT_AS_WR_CLAIM_BALANCE\n");
		sql.append("WHERE ID = ").append(id).append("\n");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if(map!=null&&map.size()>0){
			i = String.valueOf(map.get("VAR"));
		}
		return i;
	}
	
	public Map<String, Object> getDealerQueryInfo(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TO_CHAR(A.BALANCE_AMOUNT, 'FM99,999,990.00') BALANCEAMOUNT,\n");
		sql.append("       TO_CHAR(A.FINANCIAL_DEDUCT, 'FM99,999,990.00') FINANCIALDEDUCT,\n");
		sql.append("       TO_CHAR(A.NOTE_AMOUNT, 'FM99,999,990.00') NOTEAMOUNT,\n");
		sql.append("       TO_CHAR(A.UPDATE_DATE, 'YYYY-MM-DD') UPDATE_DATE,\n");
		sql.append("       A.*, B.NAME,NVL(C.DEALER_CODE, A.DEALER_CODE) DEALER_CODE, c.DEALER_NAME dealer_name1\n");
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE A, TC_USER B, TM_DEALER C\n");
		sql.append("WHERE A.UPDATE_BY = B.USER_ID(+)\n");
		sql.append("AND A.DEALER_ID = C.DEALER_ID(+)\n");
		sql.append("AND A.ID = ").append(id).append("\n"); 
		

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	/**
	 * 
	* @Title: getMarketFee 
	* @Description: TODO(获取市场工单处理费用) 
	* @param TtIfMarketBean: dealerId 
	* @return Map<String,Object>    返回状态为[技术支持室通过]的市场工单总金额
	* @throws 
	 */
	public Map<String, Object> getMarketFee(TtIfMarketBean bean) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SUM(MONEY) SUM_MONEY\n" );
		sql.append("FROM TT_IF_MARKET\n" );
		sql.append("WHERE DEALER_ID = ").append(bean.getDealer_id()).append("\n"); 
		sql.append("  AND STATUS = ").append(Constant.MARKET_BACK_STATUS_TECH_PASS);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	/**
	 * 
	* @Title: getinvoiceView 
	* @Description: TODO() 
	* @param String: id 
	* @return Map<String,Object>  
	* @throws TT_AS_WR_CLAIM_BALANCE
	 */
	public List<Map<String, Object>> getinvoiceView(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT b.*,u.name,D.DEALER_NAME AS DEALER_NAME1,E.DEALER_NAME AS KP_DEALER_NAME,D.DEALER_CODE AS NEW_DEALER_CODE FROM TT_AS_WR_CLAIM_BALANCE b,tc_user u,TM_DEALER D,TM_DEALER E where D.DEALER_ID = B.DEALER_ID AND E.DEALER_ID=B.KP_DEALER_ID AND b.update_by=u.user_id and id="+id);
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	@Deprecated
	public List<Map<String, Object>> getinvoiceUnit(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select *\n");
		sql.append("  from tm_dealer de where de.dealer_level=10851001\n" );
		sql.append(" start with de.dealer_id = 2010092000801713\n" );
		sql.append("connect by prior de.dealer_id = de.parent_dealer_d");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 根据结算单ID查询结算单
	 * @param balanceId 结算ID
	 * @return
	 */
	public TtAsWrClaimBalancePO getClaimBalancePOById(Long balanceId){
		
		TtAsWrClaimBalancePO resultPO = null;
		
		TtAsWrClaimBalancePO conditionPO = new TtAsWrClaimBalancePO();
		conditionPO.setId(balanceId);
		
		List<TtAsWrClaimBalancePO> resultList = this.select(conditionPO);
		if(resultList!=null && resultList.size()>0){
			resultPO = resultList.get(0);
		}
		
		return resultPO;
	}

	/**
	 * 查询已经结算的特殊费用工单数和费用，用于检测是否所有特殊费用都已检测完成
	 * @param balanceId 结算单ID
	 * @return Map<String,Object>
	 *         KEY : {ALLCOUNT:该结算单标识的所有特殊费用工单数,AUTH_COUNT:审核通过的特殊费用工单数,
	 *               MARKETFEE:该结算单标识的所有市场工单费用,OUTFEE:该结算单标识的所有外出费用,
	 *               AUTH_MARKETFEE:审核通过的市场工单费用,AUTH_OUTFEE:审核通过的外出费用}
	 */
	public Map<String,Object> queryAuthSpecAmount(Long balanceId){
		
		if(balanceId==null)
			return new HashMap<String, Object>();
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT COUNT(*) ALLCOUNT,\n" );
		//大区退回、结算室退回和已结算状态的特殊费用工单为终止状态
		sql.append("       COUNT(DECODE(STATUS,"+Constant.SPEFEE_STATUS_03+",1 ,"+Constant.SPEFEE_STATUS_05+", 1, "
				+Constant.SPEFEE_STATUS_06+", 1, NULL)) AUTH_COUNT,\n" );
		//统计所有市场工单费用
		sql.append("       NVL(SUM(DECODE(FEE_TYPE, "+Constant.FEE_TYPE_01+", DECLARE_SUM, 0)), 0) MARKETFEE,\n" );
		//统计所有外出特殊费用
		sql.append("       NVL(SUM(DECODE(FEE_TYPE, "+Constant.FEE_TYPE_02+", DECLARE_SUM, 0)), 0) OUTFEE,\n" );
		//统计审核通过的市场工单费用
		sql.append("       NVL(SUM(CASE WHEN FEE_TYPE="+Constant.FEE_TYPE_01+" AND STATUS="+Constant.SPEFEE_STATUS_06+" THEN DECLARE_SUM\n" );
		sql.append("       ELSE 0 END),0) AUTH_MARKETFEE,\n" );
		//统计审核通过的外出费用
		sql.append("       NVL(SUM(CASE WHEN FEE_TYPE="+Constant.FEE_TYPE_02+" AND STATUS="+Constant.SPEFEE_STATUS_06+" THEN DECLARE_SUM\n" );
		sql.append("       ELSE 0 END),0) AUTH_OUTFEE\n" );
		sql.append("  FROM TT_AS_WR_SPEFEE\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND CLAIMBALANCE_ID = ?");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		return this.pageQueryMap(sql.toString(), paramList, this.getFunName());
	}
	/**
	 * Iverson add By 2010-12-02
	 * 用于判断限制复核申请的条件
	 * @param balanceId
	 * @return
	 */
	public String dealerTimeJuge(long balanceId){
		String msg="";
		TtAsWrClaimBalancePO ba=new TtAsWrClaimBalancePO();
		ba.setId(Long.valueOf(balanceId));
		ba=(TtAsWrClaimBalancePO)this.select(ba).get(0);
		Date dateEnd=ba.getEndDate();//本次结算时间
		String sql="SELECT END_DATE AS END_DATE FROM TT_AS_WR_CLAIM_BALANCE WHERE DEALER_ID=? AND  ID<>? AND STATUS IN" +
				"(?,?,?,?,?,?,?) ORDER BY END_DATE DESC";
		List parList=new ArrayList();
		parList.add(ba.getDealerId());
		parList.add(balanceId);
		parList.add(Constant.ACC_STATUS_01);
		parList.add(Constant.ACC_STATUS_02);
		parList.add(Constant.ACC_STATUS_05);
		parList.add(Constant.ACC_STATUS_06);
		parList.add(Constant.ACC_STATUS_07);
		parList.add(Constant.ACC_STATUS_08);
		parList.add(Constant.ACC_STATUS_09);
		List listSel=this.pageQuery(sql, parList, this.getFunName());
		//判断在此结算单结算室审核中和结算室审核完成之前是否还有未复核申请的结算单(根据经销商ID和产地)
		//if(listSel.size()>0){
			//Date maxDate=(Date)(((Map)listSel.get(0)).get("END_DATE"));
			//if(dateEnd.after(maxDate)){
				//msg="01";
			//}
		//}
		//判断旧件审核时间加上6个月是否小于现在的结算时间，是表示为过期可以结算，反之不能结算
		if(msg.equals("")){
			sql="SELECT OLD_REVIEW_DATE FROM TT_AS_DEALER_TYPE WHERE  DEALER_ID=? AND YIELDLY=?";
			List listPar2=new ArrayList();
			listPar2.add(ba.getDealerId());
			listPar2.add(ba.getYieldly());
			List listDate=this.pageQuery(sql, listPar2, this.getFunName());
			Date parDate=(Date)((Map)listDate.get(0)).get("OLD_REVIEW_DATE");
		
			Calendar calendar = Calendar.getInstance();//公用类，加年月日的
			calendar.setTime(parDate);
			calendar.add(Calendar.MONTH, 9);//加9个月(本来是6个月 2011-05-17开始改为9个月)//zhumingwei update
			parDate = calendar.getTime();//得到加一天后的值
			if(parDate.before(dateEnd)){
				msg="02";
			}	
		}
		//判断结算的开始时间是否等于上一次结算时间加一天
		if(msg.equals("")){
			Date startDate=ba.getStartDate();
			sql="SELECT BALANCE_REVIEW_DATE FROM TT_AS_DEALER_TYPE WHERE  DEALER_ID=? AND YIELDLY=?";
			List listPar2=new ArrayList();
			listPar2.add(ba.getDealerId());
			listPar2.add(ba.getYieldly());
			List listDate=this.pageQuery(sql, listPar2, this.getFunName());
			Date parDate=(Date)((Map)listDate.get(0)).get("BALANCE_REVIEW_DATE");
		
			Calendar calendar = Calendar.getInstance();//公用类，加年月日的
			calendar.setTime(parDate);
			calendar.add(Calendar.DAY_OF_MONTH, 1);//时间加一天
			parDate = calendar.getTime();//得到加一天后的值
			if(!parDate.equals(startDate)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(startDate);
				calendar.add(Calendar.DAY_OF_MONTH, -1);//时间减一天
				startDate = calendar.getTime();//得到减一天后的值
				String sql1="SELECT count(*) as count FROM tt_as_changedate WHERE CHANGE_REVIEW_DATE in(to_date('"+sdf.format(startDate)+"','YYYY-MM-DD')) and DEALER_ID='"+ba.getDealerId()+"' AND YIELDLY='"+ba.getYieldly()+"'";
				System.out.println("sqlslq=="+sql1);
				List time=this.pageQuery(sql1, null, this.getFunName());
				String count=((Map)time.get(0)).get("COUNT").toString();
				if(Integer.parseInt(count)>0){
					//判断在此时间之前还有没有没有需要复核申请的结算单
					//如果有就提示它不能跨月复核
					//没有就可以复核
					String sql12="SELECT change_date FROM tt_as_changedate WHERE CHANGE_REVIEW_DATE =to_date('"+sdf.format(startDate)+"','YYYY-MM-DD') and DEALER_ID='"+ba.getDealerId()+"' AND YIELDLY='"+ba.getYieldly()+"'";
					System.out.println("sqlslq=="+sql12);
					List time2=this.pageQuery(sql12, null, this.getFunName());
					String change_date=((Map)time2.get(0)).get("CHANGE_DATE").toString();
					try{
						calendar.setTime(sdf.parse(change_date));
					}catch(Exception e){
						e.printStackTrace();
					}
					calendar.add(Calendar.DAY_OF_MONTH, -1);//时间减一天
					Date change_date1 = calendar.getTime();//得到减一天后的值
					String sql13="SELECT count(*) as count FROM tt_as_wr_claim_balance WHERE end_DATE in(to_date('"+sdf.format(change_date1)+"','YYYY-MM-DD')) and DEALER_ID='"+ba.getDealerId()+"' AND STATUS IN("+Constant.ACC_STATUS_01+","+Constant.ACC_STATUS_02+","+Constant.ACC_STATUS_06+","+Constant.ACC_STATUS_07+","+Constant.ACC_STATUS_08+","+Constant.ACC_STATUS_09+")  AND YIELDLY='"+ba.getYieldly()+"'";
					System.out.println("sqlslq=="+sql13);
					List time3=this.pageQuery(sql13, null, this.getFunName());
					String count3=((Map)time3.get(0)).get("COUNT").toString();
					if(Integer.parseInt(count3)>0){
						msg="03";
					}
				}else{
					msg="03";
				}
			}	
		}
		//判断经销商时候被停职结算
		if(msg.equals("")){
			sql="SELECT STATUS FROM tt_as_dealer_type WHERE  DEALER_ID=? AND YIELDLY=?";
			List listPar3=new ArrayList();
			listPar3.add(ba.getDealerId());
			listPar3.add(ba.getYieldly());
			List listDate=this.pageQuery(sql, listPar3, this.getFunName());
			String status=(String)((Map)listDate.get(0)).get("STATUS");
			if(status.equals(Constant.IF_TYPE_YES.toString())){
				msg="04";
			}	
		}
		
		return msg;
	}
	public PageResult<Map<String,Object>> getClaimPrint(String bDate,String eDate,String name,Long comId,int pageSize,int curPage,Long poseId){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select /*+RULE*/b.total_no,\n");
		sql.append("       d.dealer_code,\n");  
		sql.append("       d.dealer_name,\n");  
		sql.append("       to_char(d.begin_balance_date,'yyyy-MM-dd') b_date,\n");  
		sql.append("       to_char(d.end_balance_date,'yyyy-MM-dd') e_date,\n");  
		sql.append("       b.claim_count,\n");  
		sql.append("       to_char(b.sign_date,'yyyy-MM-dd') sign_date,\n");
		sql.append("       r.region_name,u.name\n");  
		sql.append("  from Tt_As_Wr_Gather_Balance b, tc_user u, tm_dealer d,tm_region r\n");  
		sql.append(" where b.dealer_id = d.dealer_id\n");  
		sql.append("   and b.status in ("+Constant.BALANCE_GATHER_TYPE_03+","+Constant.BALANCE_GATHER_TYPE_04+")\n");  
		sql.append("   and u.user_id = b.sign_person\n");  
		if(StringUtil.notNull(bDate))
			sql.append("   and b.sign_date>=to_date('"+bDate+"','yyyy-MM-dd')\n");
		if(StringUtil.notNull(eDate))
			sql.append("   and b.sign_date<=to_date('"+eDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		if(StringUtil.notNull(name))
			sql.append("   and u.name like '%"+name+"%'\n");
		sql.append("   and d.Province_ID = r.region_code\n");
		sql.append(" and  exists (select t.produce_base \n" );
		sql.append("       from tm_business_area t,tm_pose_business_area t1 \n");
		sql.append("       where T.AREA_ID=T1.AREA_ID \n");
		sql.append("       and T1.POSE_ID="+poseId+" \n");
		sql.append("       and t.status = 10011001 \n");
		sql.append("       and B.YIELDLY=T.PRODUCE_BASE \n");
		sql.append("   ) \n");

		sql.append(" order by d.province_id,u.user_id,d.dealer_code desc\n");
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage);

	}
	
	/**
	 * Iverson add By 2010-12-16
	 * 打印收票报表(查询分页)
	 */
	public PageResult<Map<String,Object>> printPorterQuery(String bDate,String eDate,String name,String report_code,String financeName,String gzDate,String gzDate1,Long comId,int pageSize,int curPage,Long poseId,String dealerCode,String dealerName,String flag){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select ttt.report_code,ttt.dealer_code,ttt.dealer_name,ttt.region_name,ttt.invoice_code,ttt.amount,ttt.tax_rate,ttt.auth_time,min(ttt.auth_person_name) auth_person_name,ttt.a_name,to_char(ttt.gzTime,'yyyy-MM-dd') as gzTime from (\n");
		sql.append(" select /*+use_hash(b,a,t,r,tt,bb)*/ b.report_code ,t.dealer_code,t.dealer_name,r.region_name,b.invoice_code,b.amount,tt.tax_rate,to_char(a.auth_time, 'yyyy-MM-dd HH24:MI:SS') as auth_time,bb.auth_person_name,a.auth_person_name as a_name, \n");
		sql.append("(select max(fff.auth_time) from tt_taxable_sum_authitem fff where fff.auth_status='"+Constant.TAXABLE_SERVICE_SUM_FANCE+"' and a.balance_id=fff.balance_id) gzTime \n");
		sql.append("from tt_taxable_sum_authitem a,tt_labor_list b ,tm_dealer t,tm_region r, \n");  
		sql.append("tt_taxable_service_sum tt,tt_as_wr_balance_authitem bb,tt_labor_list_detail cc,tt_as_wr_claim_balance dd \n");  
		sql.append("where a.balance_id=b.report_id \n");  
		sql.append("and b.dealer_id=t.dealer_id and t.province_id=r.region_code and a.balance_id=tt.sum_parameter_id \n");  
		sql.append("and dd.balance_no=cc.balance_no and bb.balance_id=dd.id and cc.report_id = b.report_id and cc.balance_id = dd.id and bb.auth_status='"+BalanceStatusRecord.STATUS_07+"' and a.auth_status = '"+Constant.TAXABLE_SERVICE_SUM_GET+"' \n");  
		if(StringUtil.notNull(bDate)){
			sql.append("and a.auth_time>=to_date('"+bDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(StringUtil.notNull(eDate)){
			sql.append("and a.auth_time<=to_date('"+eDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(StringUtil.notNull(name)){
			sql.append("and a.auth_person_name like '%"+name+"%' \n");
		}
		if(StringUtil.notNull(financeName)){
			sql.append("and bb.auth_person_name like '%"+financeName+"%' \n");
		}
		if(StringUtil.notNull(report_code)){
			sql.append("and b.report_code like '%"+report_code+"%' \n");
		}
		
		if(StringUtil.notNull(dealerCode)){
			sql.append("and t.dealer_code like '%"+dealerCode+"%' \n");
		}
		if(StringUtil.notNull(dealerName)){
			sql.append("and t.dealer_name like '%"+dealerName+"%' \n");
		}
		
		sql.append("and dd.yieldly in (select t.produce_base from tm_business_area t where t.area_id in (select area_id from tm_pose_business_area where pose_id = "+poseId+") and t.status = "+Constant.STATUS_ENABLE+")) ttt where 1=1\n");
		if(StringUtil.notNull(gzDate)){
			sql.append("and ttt.gzTime >= to_date('"+gzDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(StringUtil.notNull(gzDate1)){
			sql.append("and ttt.gzTime <= to_date('"+gzDate1+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \n");
		}
		sql.append("group by ttt.report_code,\n");
		sql.append("      ttt.dealer_code,\n");  
		sql.append("      ttt.dealer_name,\n");  
		sql.append("      ttt.region_name,\n");  
		sql.append("      ttt.invoice_code,\n");  
		sql.append("      ttt.amount,\n");  
		sql.append("      ttt.tax_rate,\n");  
		sql.append("      ttt.auth_time,\n");  
		sql.append("      ttt.a_name,\n");   
		sql.append("      ttt.gzTime\n");   
		if(flag.equals("1")){
			 ClaimBillMaintainDAO dao = new ClaimBillMaintainDAO();
			 List<Map<String,Object>> list = dao.queryTcCode();
			 if(Integer.valueOf(list.get(0).get("CODE_ID").toString())==(Constant.chana_jc)){
				 sql.append("order by min(ttt.auth_person_name),ttt.region_name \n");
			 }
			 else{
				 //sql.append("order by min(ttt.auth_person_name),ttt.auth_time,ttt.region_name,ttt.dealer_name\n");
				 sql.append("order by ttt.region_name,ttt.dealer_name,min(ttt.auth_person_name),ttt.auth_time\n");
			 }
		}
		if(flag.equals("2")){
			 sql.append("order by ttt.auth_time desc \n");
		}
		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage);
	}
	
	//zhumingwei 2012-08-29
	public PageResult<Map<String,Object>> queryBalanceDetailQuery(String report_code,String status,String bDate,String eDate,String gzDate,String gzDate1,String dealerCode,String yielyld,String sp,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select b.root_org_name,b.region_name,c.dealer_code,c.dealer_name,c.dealer_name as name,a.report_code,a.auth_amount,\n");
		sql.append("(select e.code_desc from tc_code e where e.code_id=d.auth_status) auth_status,(select g.auth_time from tt_taxable_sum_authitem g where g.auth_status=11881001 and g.balance_id = a.report_id) auth_time,(select g.auth_person_name from tt_taxable_sum_authitem g where g.auth_status=11881001 and g.balance_id = a.report_id) auth_person_name,(select ddd.code_desc from tc_code ddd where ddd.code_id=a.yieldly) yieldly\n");
		sql.append("from tt_labor_list a,vw_org_dealer_service b,tm_dealer c,tt_taxable_service_sum d,tt_taxable_sum_authitem f\n");
		sql.append("where a.dealer_id=b.dealer_id and a.dealer_id=c.dealer_id and a.report_id=d.sum_parameter_id(+) and f.balance_id(+)=a.report_id\n");
		sql.append("and d.create_date>=to_date('2011-12-11 00:00:00','yyyy-MM-dd hh24:mi:ss')\n");
		if(StringUtil.notNull(report_code)){
			sql.append("and a.report_code like '%"+report_code+"%' \n");
		}
		if(StringUtil.notNull(status)){
			sql.append("and d.auth_status = "+status+" \n");
		}
		if(StringUtil.notNull(bDate)){
			sql.append("and f.auth_time>=to_date('"+bDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(StringUtil.notNull(eDate)){
			sql.append("and f.auth_time<=to_date('"+eDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and d.auth_status=11881001\n");
		}
		if(StringUtil.notNull(gzDate)){
			sql.append("and d.create_date>=to_date('"+gzDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(StringUtil.notNull(gzDate1)){
			sql.append("and d.create_date<=to_date('"+gzDate1+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(StringUtil.notNull(dealerCode)){
			sql.append("and c.dealer_code like '%"+dealerCode+"%' \n");
		}
		if(StringUtil.notNull(yielyld)){
			sql.append("and a.yieldly ="+yielyld+" \n");
		}
		if(StringUtil.notNull(sp)){
			sql.append("and f.auth_person_name like '%"+sp+"%' \n");
		}
		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage);
	}
	
	//zhumingwei 2012-09-03
	public PageResult<Map<String,Object>> queryDealerBalanceDetailQuery(String report_code,String status,String dealerCode,String balance,String yielyld,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select b.root_org_name,b.region_name,h.dealer_code,h.dealer_name,c.dealer_name as name,a.report_code,g.balance_no,TO_CHAR(h.start_date,'YYYY-MM-DD') START_DATE,TO_CHAR(h.end_date,'YYYY-MM-DD') end_date,g.invoice_amount,(select ddd.code_desc from tc_code ddd where ddd.code_id=a.yieldly) yieldly,\n");
		sql.append("(select e.code_desc from tc_code e where e.code_id=d.auth_status) auth_status,(select g.auth_time from tt_taxable_sum_authitem g where g.auth_status = 11881001 and g.balance_id = a.report_id) auth_time,(select g.auth_person_name from tt_taxable_sum_authitem g where g.auth_status = 11881001 and g.balance_id = a.report_id) auth_person_name\n");
		sql.append("from tt_labor_list a,vw_org_dealer_service b,tm_dealer c,tt_taxable_service_sum d,tt_taxable_sum_authitem f,tt_labor_list_detail g,tt_as_wr_claim_balance h\n");
		sql.append("where a.dealer_id=b.dealer_id and a.dealer_id=c.dealer_id and a.report_id=d.sum_parameter_id(+) and f.balance_id(+)=a.report_id and a.report_id=g.report_id and g.balance_no=h.balance_no\n");
		sql.append("and h.start_date>=to_date('2011-12-11 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		if(StringUtil.notNull(report_code)){
			sql.append("and a.report_code like '%"+report_code+"%' \n");
		}
		if(StringUtil.notNull(status)){
			sql.append("and d.auth_status = "+status+" \n");
		}
		if(StringUtil.notNull(dealerCode)){
			sql.append("and h.dealer_code like '%"+dealerCode+"%' \n");
		}
		if(StringUtil.notNull(balance)){
			sql.append("and h.balance_no like '%"+balance+"%' \n");
		}
		if(StringUtil.notNull(yielyld)){
			sql.append("and a.yieldly = "+yielyld+" \n");
		}
		sql.append("order by a.report_code,a.create_date desc\n");
		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage);
	}
	
	public List<Map<String,Object>> exportExcel(String bDate,String eDate,String name,String report_code,String financeName,String gzDate,String gzDate1,Long comId,int pageSize,int curPage,Long poseId){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select ttt.report_code,ttt.dealer_code,ttt.dealer_name,ttt.region_name,ttt.invoice_code,ttt.amount,ttt.tax_rate,ttt.auth_time,min(ttt.auth_person_name) auth_person_name,ttt.a_name,to_char(ttt.gzTime,'yyyy-MM-dd') as gzTime from (\n");
		sql.append("select b.report_code ,t.dealer_code,t.dealer_name,r.region_name,b.invoice_code,b.amount,tt.tax_rate,to_char(a.auth_time,'yyyy-MM-dd') as auth_time,bb.auth_person_name,a.auth_person_name as a_name, \n");
		sql.append("(select max(fff.auth_time) from tt_taxable_sum_authitem fff where fff.auth_status='"+Constant.TAXABLE_SERVICE_SUM_FANCE+"' and a.balance_id=fff.balance_id) gzTime \n");
		sql.append("from tt_taxable_sum_authitem a,tt_labor_list b ,tm_dealer t,tm_region r, \n");  
		sql.append("tt_taxable_service_sum tt,tt_as_wr_balance_authitem bb,tt_labor_list_detail cc,tt_as_wr_claim_balance dd \n");  
		sql.append("where a.balance_id=b.report_id \n");  
		sql.append("and b.dealer_id=t.dealer_id and t.province_id=r.region_code and a.balance_id=tt.sum_parameter_id \n");  
		sql.append("and dd.balance_no=cc.balance_no and bb.balance_id=dd.id and cc.report_id = b.report_id and cc.balance_id = dd.id and bb.auth_status='"+BalanceStatusRecord.STATUS_07+"' and a.auth_status = '"+Constant.TAXABLE_SERVICE_SUM_GET+"' \n");  
		if(StringUtil.notNull(bDate)){
			sql.append("and a.auth_time>=to_date('"+bDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(StringUtil.notNull(eDate)){
			sql.append("and a.auth_time<=to_date('"+eDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(StringUtil.notNull(name)){
			sql.append("and a.auth_person_name like '%"+name+"%' \n");
		}
		if(StringUtil.notNull(financeName)){
			sql.append("and bb.auth_person_name like '%"+financeName+"%' \n");
		}
		if(StringUtil.notNull(report_code)){
			sql.append("and b.report_code like '%"+report_code+"%' \n");
		}
		sql.append("and dd.yieldly in (select t.produce_base from tm_business_area t where t.area_id in (select area_id from tm_pose_business_area where pose_id = "+poseId+") and t.status = "+Constant.STATUS_ENABLE+")) ttt where 1=1\n");
		if(StringUtil.notNull(gzDate)){
			sql.append("and ttt.gzTime >= to_date('"+gzDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(StringUtil.notNull(gzDate1)){
			sql.append("and ttt.gzTime <= to_date('"+gzDate1+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \n");
		}
		sql.append("group by ttt.report_code,\n");
		sql.append("      ttt.dealer_code,\n");  
		sql.append("      ttt.dealer_name,\n");  
		sql.append("      ttt.region_name,\n");  
		sql.append("      ttt.invoice_code,\n");  
		sql.append("      ttt.amount,\n");  
		sql.append("      ttt.tax_rate,\n");  
		sql.append("      ttt.auth_time,\n");  
		sql.append("      ttt.a_name,\n");   
		sql.append("      ttt.gzTime\n");   
		ClaimBillMaintainDAO dao = new ClaimBillMaintainDAO();
		 List<Map<String,Object>> list = dao.queryTcCode();
		 if(Integer.valueOf(list.get(0).get("CODE_ID").toString())==(Constant.chana_jc)){
			 sql.append("order by min(ttt.auth_person_name),ttt.region_name \n");
		 }
		 else{
			 sql.append("order by min(ttt.auth_person_name),ttt.auth_time,ttt.region_name,ttt.dealer_name\n");
		 }
		System.out.println("sqlsql=="+sql.toString());
		
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	//zhumingwei 2012-08-29
	public List<Map<String,Object>> exportExcel1(String report_code,String status,String bDate,String eDate,String gzDate,String gzDate1,String dealerCode,String yielyld,String sp,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select b.root_org_name,b.region_name,c.dealer_code,c.dealer_name,c.dealer_name as name,a.report_code,a.auth_amount,\n");
		sql.append("(select e.code_desc from tc_code e where e.code_id=d.auth_status) auth_status,(select g.auth_time from tt_taxable_sum_authitem g where g.auth_status=11881001 and g.balance_id = a.report_id) auth_time,(select g.auth_person_name from tt_taxable_sum_authitem g where g.auth_status=11881001 and g.balance_id = a.report_id) auth_person_name,(select ddd.code_desc from tc_code ddd where ddd.code_id=a.yieldly) yieldly\n");
		sql.append("from tt_labor_list a,vw_org_dealer_service b,tm_dealer c,tt_taxable_service_sum d,tt_taxable_sum_authitem f\n");
		sql.append("where a.dealer_id=b.dealer_id and a.dealer_id=c.dealer_id and a.report_id=d.sum_parameter_id(+) and f.balance_id(+)=a.report_id\n");
		sql.append("and d.create_date>=to_date('2011-12-11 00:00:00','yyyy-MM-dd hh24:mi:ss')\n");
		if(StringUtil.notNull(report_code)){
			sql.append("and a.report_code like '%"+report_code+"%' \n");
		}
		if(StringUtil.notNull(status)){
			sql.append("and d.auth_status = "+status+" \n");
		}
		if(StringUtil.notNull(bDate)){
			sql.append("and f.auth_time>=to_date('"+bDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(StringUtil.notNull(eDate)){
			sql.append("and f.auth_time<=to_date('"+eDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and d.auth_status=11881001\n");
		}
		if(StringUtil.notNull(gzDate)){
			sql.append("and d.create_date>=to_date('"+gzDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(StringUtil.notNull(gzDate1)){
			sql.append("and d.create_date<=to_date('"+gzDate1+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(StringUtil.notNull(dealerCode)){
			sql.append("and c.dealer_code like '%"+dealerCode+"%' \n");
		}
		if(StringUtil.notNull(yielyld)){
			sql.append("and a.yieldly ="+yielyld+" \n");
		}
		if(StringUtil.notNull(sp)){
			sql.append("and f.auth_person_name like '%"+sp+"%' \n");
		}
		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	//zhumingwei 2012-09-03
	public List<Map<String,Object>> exportExcel2(String report_code,String status,String dealerCode,String balance,String yielyld,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select b.root_org_name,b.region_name,h.dealer_code,h.dealer_name,c.dealer_name as name,a.report_code,g.balance_no,TO_CHAR(h.start_date,'YYYY-MM-DD') START_DATE,TO_CHAR(h.end_date,'YYYY-MM-DD') end_date,g.invoice_amount,(select ddd.code_desc from tc_code ddd where ddd.code_id=a.yieldly) yieldly,\n");
		sql.append("(select e.code_desc from tc_code e where e.code_id=d.auth_status) auth_status,(select g.auth_time from tt_taxable_sum_authitem g where g.auth_status = 11881001 and g.balance_id = a.report_id) auth_time,(select g.auth_person_name from tt_taxable_sum_authitem g where g.auth_status = 11881001 and g.balance_id = a.report_id) auth_person_name\n");
		sql.append("from tt_labor_list a,vw_org_dealer_service b,tm_dealer c,tt_taxable_service_sum d,tt_taxable_sum_authitem f,tt_labor_list_detail g,tt_as_wr_claim_balance h\n");
		sql.append("where a.dealer_id=b.dealer_id and a.dealer_id=c.dealer_id and a.report_id=d.sum_parameter_id(+) and f.balance_id(+)=a.report_id and a.report_id=g.report_id and g.balance_no=h.balance_no\n");
		sql.append("and h.start_date>=to_date('2011-12-11 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		if(StringUtil.notNull(report_code)){
			sql.append("and a.report_code like '%"+report_code+"%' \n");
		}
		if(StringUtil.notNull(status)){
			sql.append("and d.auth_status = "+status+" \n");
		}
		if(StringUtil.notNull(dealerCode)){
			sql.append("and h.dealer_code like '%"+dealerCode+"%' \n");
		}
		if(StringUtil.notNull(balance)){
			sql.append("and h.balance_no like '%"+balance+"%' \n");
		}
		if(StringUtil.notNull(yielyld)){
			sql.append("and a.yieldly = "+yielyld+" \n");
		}
		sql.append("order by a.report_code,a.create_date desc\n");
		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	public List getDate(String id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select * from tt_as_wr_balance_authitem b where b.balance_id=? and b.auth_status = ? ");
		List list=new ArrayList();
		list.add(Long.valueOf(id));
		list.add(com.infodms.dms.actions.claim.application.BalanceStatusRecord.STATUS_06);
		
		return this.select(TtAsWrBalanceAuthitemPO.class, sql.toString(), list);
	}
	public List getDate2(String id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select * from tt_as_wr_balance_authitem b where b.balance_id=? and b.auth_status = ? ");
		List list=new ArrayList();
		list.add(Long.valueOf(id));
		list.add(com.infodms.dms.actions.claim.application.BalanceStatusRecord.STATUS_07);
		
		return this.select(TtAsWrBalanceAuthitemPO.class, sql.toString(), list);
	}
	public List getSignInfo(String id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select u.name remark,b.sign_date\n");
		sql.append("  from tt_as_wr_claim_balance  a,\n");  
		sql.append("       tt_as_wr_gather_balance b,\n");  
		sql.append("       tr_gather_balance       c,tc_user u\n");  
		sql.append(" where a.id ="+id+"\n");  
		sql.append("   and a.id = c.balance_id\n");  
		sql.append("   and b.id = c.gather_id and u.user_id=b.sign_person\n");
		return this.select(TtAsWrGatherBalancePO.class, sql.toString(), null);
	}
	
	public Map<String,Object> checkNum(String sql){
		return pageQueryMap(sql, null, getFunName());
	}
	
	public PageResult<Map<String, Object>> queryConstractNumber(String dealerId,String yile,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select * from Tt_As_Wr_Contract s where 1=1\n" );
		if(dealerId!=null&&!dealerId.equals("")){
			sql.append("and s.dealer_id ="+dealerId+"\n" );
		}
		if(yile!=null&&!yile.equals("")){
			sql.append("and s.yieldly="+yile+"");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//zhumingwei 2012-12-12
	public List<Map<String, Object>> getStopAuth(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select (select u.name from tc_user u where u.user_id=a.stop_by) stop_by,a.stop_date,a.remark,decode(a.status,0,'取消暂停','被暂停') status from tt_as_wr_balance_stop_auth a where a.stop_id="+id+"\n");
		logger.info("------------"+sql.toString());
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public int checkMoneyByBalanceNo(String balanecNo) {
		int res=1;
		Double amount=0.0d;
		Double balance_amount=0.0d;
		try {
			StringBuffer sb= new StringBuffer();
			sb.append("select (rr.amount_sum-nvl(rr.return_amount,0)-nvl(rr.plus_minus_datum_sum,0)-nvl(rr.plus_minus_labour_sum,0)\n" );
			sb.append("---nvl(rr.special_datum_sum,0)\n" );
			sb.append("---nvl(rr.special_labour_sum,0)\n" );
			sb.append("-nvl(rr.compensation_dealer_money,0)---nvl(rr.append_amount,0)\n" );//
			sb.append(") as amount \n" );
			sb.append(" from Tt_As_Wr_Claim_Balance rr where rr.remark='"+balanecNo+"'");
			List<Map<String, Object>> list = pageQuery(sb.toString(), null, getFunName());
			if(list!=null && list.size()>0){
				Map<String, Object> map = list.get(0);
				BigDecimal b= (BigDecimal) map.get("AMOUNT");
				amount=b.doubleValue();
			}
			StringBuffer sb1= new StringBuffer();
			sb1.append("select sum(nvl(balance_amount,0)) BALANCE_AMOUNT from (select sum(nvl(a.balance_amount, 0)) as balance_amount\n" );
			sb1.append("  from Tt_As_Wr_Application a\n" );
			sb1.append(" where 1 = 1\n" );
			sb1.append("   and a.status = 10791007\n" );
			sb1.append("   and a.balance_no = '"+balanecNo+"'\n" );
			sb1.append("union all\n" );
			sb1.append("select 0-sum(nvl(aa.balance_amount, 0)) as balance_amount\n" );
			sb1.append("  from Tt_As_Wr_Application_Counter aa\n" );
			sb1.append(" where 1 = 1\n" );
			sb1.append("   and aa.status = 10791007\n" );
			sb1.append("   and aa.balance_no = '"+balanecNo+"'\n");
			sb1.append("union all\n" );
			sb1.append("select 0-sum(nvl(c.datum_sum, 0)+nvl(c.labour_sum,0))as balance_amount\n" );
			sb1.append("  from  tt_as_wr_administrative_charge  c left join tt_balance_charge_relation re on c.id=re.id \n" );
			sb1.append(" where re.balance_oder = '"+balanecNo+"'\n" );
			sb1.append("union all\n" );
			sb1.append("select sum(nvl(ap.approval_amount,0))as balance_amount\n" );
			sb1.append("  from  tt_as_wr_special_apply ap \n" );
			sb1.append(" where ap.balance_no= '"+balanecNo+"'\n" );
			sb1.append(" )");

			List<Map<String, Object>> list1 = pageQuery(sb1.toString(), null, getFunName());
			if(list1!=null && list1.size()>0){
				Map<String, Object> map = list1.get(0);
				BigDecimal b= (BigDecimal) map.get("BALANCE_AMOUNT");
				balance_amount=b.doubleValue();
			}
			if(amount-balance_amount!=0){
				res=-1;
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}
		return res;
	}
	
	public void deleteFinancialInfoView(String dealerId ,String startDate , String endDate , String remark) throws Exception{
		//更新索赔单
		StringBuffer sb = new StringBuffer();
		sb.append("update  Tt_As_Wr_Application aa set aa.is_invoice =0,aa.balance_no=null where aa.dealer_id='"+dealerId+"' and aa.balance_no= '"+remark+"' ");
		factory.update(sb.toString(), null);
		
		//更新反索赔单
		StringBuffer sb1 = new StringBuffer();
		sb1.append("update  Tt_As_Wr_Application_Counter aa set aa.is_invoice =0,aa.balance_no=null  where aa.dealer_id='"+dealerId+"' and aa.balance_no= '"+remark+"'  ");
		factory.update(sb1.toString(), null);
		//更新特殊费用表
		StringBuffer sb5 = new StringBuffer();
		sb5.append("update  tt_as_wr_special_apply  aa set aa.is_invoice =0,aa.balance_no=null  where aa.dealer_id='"+dealerId+"' and aa.balance_no= '"+remark+"'  ");
		factory.update(sb5.toString(), null);
		
		//删除结算单
		StringBuffer sb2 = new StringBuffer();
		sb2.append("delete from tt_as_wr_claim_balance_tmp t where t.remark = '"+remark+"'");
		factory.delete(sb2.toString(), null);
		StringBuffer sb3 = new StringBuffer();
		sb3.append("delete from tt_as_wr_claim_balance t where t.remark = '"+remark+"'");
		factory.delete(sb3.toString(), null);
		
		StringBuffer sb4 = new StringBuffer();
		sb4.append("UPDATE tt_as_wr_discount t  set t.BALANCE_ODER=null where t.BALANCE_ODER ='"+remark+"' ");
		factory.update(sb4.toString(), null);
		
		StringBuffer sb6 = new StringBuffer();
		sb6.append(" UPDATE  tt_as_wr_fine  t  set  t.BALANCE_ODER  =  null  ,t.PAY_STATUS  =11511001 \n");
		sb6.append(" where t.BALANCE_ODER=  '"+remark+"' \n");
		factory.update(sb6.toString(), null);
		

		//二次入库的
		StringBuffer sb9 = new StringBuffer();
		sb9.append(" update  Tt_As_Second_In_Store_Detail d \n");
		sb9.append(" set d.balance_no=null \n");
		sb9.append(" where  d.balance_no='"+remark+"' \n");
		factory.update(sb9.toString(), null);
		
		//======================================
		StringBuffer sb7 = new StringBuffer();
		sb7.append(" UPDATE  tt_as_wr_administrative_charge t set \n");
		sb7.append(" t.STATUS = 94151002 where t.id in (select id from tt_balance_charge_relation where balance_oder='"+remark+"' )");
		factory.update(sb7.toString(), null);
		//回滚运费
		StringBuffer sb10 = new StringBuffer();
		
		sb10.append(" update  Tt_As_Wr_Old_Returned a  set a.is_invoice=0  where a.id in (select id from tt_balance_return_relation where balance_oder='"+remark+"' )");
		factory.update(sb10.toString(), null);
	  
	}
}

