package com.infoservice.dms.chana.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.vo.ExamineTargetVO;

public class ExamineTargetDao extends AbstractIFDao implements DEConstant {
	public static Logger logger = Logger.getLogger(ExamineTargetDao.class);
	private static final ExamineTargetDao dao = new ExamineTargetDao ();
	
	public static final ExamineTargetDao getInstance() {
		return dao;
	}
	

	public Date getTime(String uploadDate,long companyId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM TT_ASSESS_GUIDELINE TAG WHERE TAG.COMPANY_ID ="+companyId);
		sql.append(" AND TRUNC(TAG.UPLOAD_DATE) =");
		sql.append(" to_date('"+uploadDate+"','yyyy-mm-dd')");
		Map<String,Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if(map!=null){
			return (Date)map.get("DOWN_TIMESTAMP");
		}
		return null;
	}
	
	public Float getSalesCar (String uploadDate,long companyId){
		StringBuffer sql = new StringBuffer();
		sql.append("select a.c - b.d as SALESCAR\n");
		sql.append("  from (select count(1) c\n");  
		sql.append("          from tt_dealer_actual_sales tdas, tm_dealer tmd\n");  
		sql.append("         where tdas.dealer_id = tmd.dealer_id\n");  
		sql.append("           and tmd.company_id = "+companyId+"\n");  
		sql.append("           and trunc(tdas.sales_date) = to_date('"+uploadDate+"','yyyy-mm-dd')) a,\n");  
		sql.append("       (select count(1) d\n");  
		sql.append("          from tt_vs_actual_sales_return tvas, tm_dealer tmd\n");  
		sql.append("         where tvas.dealer_id = tmd.dealer_id\n");  
		sql.append("           and tmd.company_id = "+companyId+"\n");   
		sql.append("           and trunc(tvas.update_date) = to_date('"+uploadDate+"','yyyy-mm-dd')) b");
		Map<String,Object> map = pageQueryMap(sql.toString(), null, getFunName());

		if(null == map) {
			return 0F;
		} else {
			return Float.parseFloat(map.get("SALESCAR").toString());
		}
	}
	
	public Float getClaimSheetCount (String uploadDate,long companyId){
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(tawa.id) CLAIMSHEETCOUNT\n");
		sql.append(" from tt_as_wr_application tawa, tm_dealer tmd\n");  
		sql.append("where tawa.dealer_id = tmd.dealer_id\n");  
		sql.append("  and tmd.company_id = "+companyId);  
		sql.append("  and trunc(tawa.create_date) = to_date('"+uploadDate+"','yyyy-mm-dd')\n");  
		sql.append("group by tmd.company_id\n");
		Map<String,Object> map = pageQueryMap(sql.toString(), null, getFunName());
		
		if(null == map) {
			return 0F;
		} else {
			return Float.parseFloat(map.get("CLAIMSHEETCOUNT").toString());
			}
	}
}
