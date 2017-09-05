package com.infodms.dms.dao.report.jcafterservicereport;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.claim.oldPart.ClaimBackListDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ServicerBalanceReportDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ClaimBillMaintainDAO.class);
	private static final ServicerBalanceReportDao dao = new ServicerBalanceReportDao();

	public static final ServicerBalanceReportDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public PageResult<Map<String,Object>>  viewServicer(String dealerCode,String dealerName,String orgId,String PROVICE_ID,String YILYIE,String year,int pageSize,int curPage ){
		StringBuffer sql= new StringBuffer();
		TcCodePO po1 = new TcCodePO();
		po1.setCodeId(Constant.DAY_12_31);
		ClaimBackListDao claimBackdao=ClaimBackListDao.getInstance();
		TcCodePO poValue1 = (TcCodePO)claimBackdao.select(po1).get(0);
		String day1 = poValue1.getCodeDesc();
		sql.append("select a.dealer_id,\n" );
		sql.append("       a.dealer_code,\n" );
		sql.append("       a.dealer_name,\n" );
		sql.append("       a.yieldly,\n" );
		sql.append("       s.region_name,\n" );
		sql.append("       s.root_org_name,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '01', a.status))),'--') AS status_1,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '02', a.status))),'--') AS status_2,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '03', a.status))),'--') AS status_3,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '04', a.status))),'--') AS status_4,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '05', a.status))),'--') AS status_5,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '06', a.status))),'--') AS status_6,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '07', a.status))),'--') AS status_7,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '08', a.status))),'--') AS status_8,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '09', a.status))),'--') AS status_9,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '10', a.status))),'--') AS status_10,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '11', a.status))),'--') AS status_11,\n" );
		//sql.append("       nvl(to_char(sum(DECODE(a.MMDD, '12-"+poValue1+"', a.status))),'--') AS status_12,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MMDD, '12-10', a.status))),'--') AS status_12,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MMDD, '12-31', a.status))),'--') AS status_13\n" );
		 
		sql.append("  from (\n" );
		
		
		sql.append("select d.dealer_id,\n" );
		sql.append("               d.dealer_code,\n" );
		sql.append("               d.dealer_name,\n" );
		sql.append("               to_char(b.yieldly) yieldly,\n" );
		sql.append("               to_char(b.end_date, 'MM') as MM,  to_char(b.end_date, 'MM-DD') as MMDD, \n" );
		sql.append("               to_char(b.status) status\n" );
		sql.append("            from Tt_As_Wr_Claim_Balance b,tm_dealer d  where 1=1 and b.dealer_id=d.dealer_id \n" );
		if(YILYIE!=null&&!YILYIE.equals("")){
			sql.append("  and  b.yieldly ="+YILYIE+"\n" );
		}
		if(year!=null&&!year.equals("")){
			sql.append("  and to_char(b.start_date,'YYYY')='"+year+"'\n" );
		}
		sql.append("  union all\n" );
		sql.append("\n" );
		sql.append(" select d.dealer_id, d.dealer_code, d.dealer_name,tc.code_id as  yieldly , '' as MM,'' as MMDD, '' as  status\n" );
		sql.append("   from tm_dealer d,tc_code tc\n" );
		sql.append("  where not exists (select 1\n" );
		sql.append("           from Tt_As_Wr_Claim_Balance c\n" );
		sql.append("          where c.dealer_id = d.dealer_id\n" );
		sql.append("  and to_char(c.start_date,'YYYY')='"+year+"')\n" );
		sql.append("    and d.status = 10011001\n" );
		sql.append("    and d.dealer_type = 10771002\n" );
		sql.append("    and tc.type=1131\n" );
		
		if(YILYIE!=null&&!YILYIE.equals("")){
			sql.append("   and tc.code_id ="+YILYIE+"\n" );
		}
		  TcCodePO po = new TcCodePO();
		  po.setType(Constant.chana+"");
		  TcCodePO poValue = (TcCodePO)this.select(po).get(0);
		  String codeId = poValue.getCodeId();
		  if(codeId==null){
			  codeId="0";
		  }
		  if(Integer.parseInt(codeId)==Constant.chana_jc){
			  sql.append(" and   tc.code_id ="+11311001+"\n" );
		  }
		
		sql.append("          ) a,\n" );
		sql.append("       tm_dealer d,\n" );
		sql.append("      vw_org_dealer_service s\n" );
		sql.append(" where a.dealer_id = d.dealer_id\n" );
		if(dealerCode!=null&&!dealerCode.equals("")){
			sql.append("  and a.dealer_code like '%"+dealerCode+"%'   \n");
		}
		if(dealerName!=null&&!dealerName.equals("")){
			sql.append("  and a.dealer_name like '%"+dealerName+"%'   \n");
		}
		if(orgId!=null&&!orgId.equals("")){
		    sql.append("  and s.root_org_id="+orgId+" ");
		}
		if(PROVICE_ID!=null&&!PROVICE_ID.equals("")){
		    sql.append("  and s.region_name="+PROVICE_ID+" ");
		}
		sql.append("    and s.dealer_id = a.dealer_id\n" );
		sql.append(" group by  a.dealer_id,a.dealer_code, a.dealer_name, a.yieldly, s.region_name,s.root_org_name");
		PageResult<Map<String,Object>> result = null;
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);

		
		
	}
	
	
	
	public List<Map<String,Object>>  viewServicerExportExcel(String dealerCode,String dealerName,String orgId,String PROVICE_ID,String YILYIE,String year,int pageSize,int curPage ){
		StringBuffer sql= new StringBuffer();
		TcCodePO po1 = new TcCodePO();
		po1.setCodeId(Constant.DAY_12_31);
		ClaimBackListDao claimBackdao=ClaimBackListDao.getInstance();
		TcCodePO poValue1 = (TcCodePO)claimBackdao.select(po1).get(0);
		String day1 = poValue1.getCodeDesc();
		sql.append("select a.dealer_id,\n" );
		sql.append("       a.dealer_code,\n" );
		sql.append("       a.dealer_name,\n" );
		sql.append("       a.yieldly,\n" );
		sql.append("       s.region_name,\n" );
		sql.append("       s.root_org_name,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '01', a.status))),'--') AS status_1,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '02', a.status))),'--') AS status_2,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '03', a.status))),'--') AS status_3,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '04', a.status))),'--') AS status_4,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '05', a.status))),'--') AS status_5,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '06', a.status))),'--') AS status_6,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '07', a.status))),'--') AS status_7,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '08', a.status))),'--') AS status_8,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '09', a.status))),'--') AS status_9,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '10', a.status))),'--') AS status_10,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MM, '11', a.status))),'--') AS status_11,\n" );
		//sql.append("       nvl(to_char(sum(DECODE(a.MMDD, '12-"+poValue1+"', a.status))),'--') AS status_12,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MMDD, '12-10', a.status))),'--') AS status_12,\n" );
		sql.append("       nvl(to_char(sum(DECODE(a.MMDD, '12-31', a.status))),'--') AS status_13\n" );
		 
		sql.append("  from (\n" );
		
		
		sql.append("select d.dealer_id,\n" );
		sql.append("               d.dealer_code,\n" );
		sql.append("               d.dealer_name,\n" );
		sql.append("               to_char(b.yieldly) yieldly,\n" );
		sql.append("               to_char(b.end_date, 'MM') as MM,  to_char(b.end_date, 'MM-DD') as MMDD, \n" );
		sql.append("               to_char(b.status) status\n" );
		sql.append("            from Tt_As_Wr_Claim_Balance b,tm_dealer d  where 1=1 and b.dealer_id=d.dealer_id \n" );
		if(YILYIE!=null&&!YILYIE.equals("")){
			sql.append("  and  b.yieldly ="+YILYIE+"\n" );
		}
		if(year!=null&&!year.equals("")){
			sql.append("  and to_char(b.start_date,'YYYY')='"+year+"'\n" );
		}
		sql.append("  union all\n" );
		sql.append("\n" );
		sql.append(" select d.dealer_id, d.dealer_code, d.dealer_name,tc.code_id as  yieldly , '' as MM,'' as MMDD, '' as  status\n" );
		sql.append("   from tm_dealer d,tc_code tc\n" );
		sql.append("  where not exists (select 1\n" );
		sql.append("           from Tt_As_Wr_Claim_Balance c\n" );
		sql.append("          where c.dealer_id = d.dealer_id\n" );
		sql.append("  and to_char(c.start_date,'YYYY')='"+year+"')\n" );
		sql.append("    and d.status = 10011001\n" );
		sql.append("    and d.dealer_type = 10771002\n" );
		sql.append("    and tc.type=1131\n" );
		
		if(YILYIE!=null&&!YILYIE.equals("")){
			sql.append("   and tc.code_id ="+YILYIE+"\n" );
		}
		  TcCodePO po = new TcCodePO();
		  po.setType(Constant.chana+"");
		  TcCodePO poValue = (TcCodePO)this.select(po).get(0);
		  String codeId = poValue.getCodeId();
		  if(codeId==null){
			  codeId="0";
		  }
		  if(Integer.parseInt(codeId)==Constant.chana_jc){
			  sql.append(" and   tc.code_id ="+11311001+"\n" );
		  }
		
		sql.append("          ) a,\n" );
		sql.append("       tm_dealer d,\n" );
		sql.append("      vw_org_dealer_service s\n" );
		sql.append(" where a.dealer_id = d.dealer_id\n" );
		if(dealerCode!=null&&!dealerCode.equals("")){
			sql.append("  and a.dealer_code like '%"+dealerCode+"%'   \n");
		}
		if(dealerName!=null&&!dealerName.equals("")){
			sql.append("  and a.dealer_name like '%"+dealerName+"%'   \n");
		}
		if(orgId!=null&&!orgId.equals("")){
		    sql.append("  and s.root_org_id="+orgId+" ");
		}
		if(PROVICE_ID!=null&&!PROVICE_ID.equals("")){
		    sql.append("  and s.region_name="+PROVICE_ID+" ");
		}
		sql.append("    and s.dealer_id = a.dealer_id\n" );
		sql.append(" group by  a.dealer_id,a.dealer_code, a.dealer_name, a.yieldly, s.region_name,s.root_org_name");
		List<Map<String,Object>> result = null;
		return this.pageQuery(sql.toString(), null, this.getFunName());

		
		
	}
}
