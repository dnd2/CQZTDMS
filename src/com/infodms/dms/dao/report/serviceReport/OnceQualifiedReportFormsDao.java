package com.infodms.dms.dao.report.serviceReport;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class OnceQualifiedReportFormsDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(OnceQualifiedReportFormsDao.class);
	public static OnceQualifiedReportFormsDao dao = new OnceQualifiedReportFormsDao();
	public static OnceQualifiedReportFormsDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public List<Map<String, Object>> getOnceQualifiedReportFormsList(String typeH,String dateStart,String dateEnd,
			String dealerCode,String dealerName ){
		String sql = queryOnceQualifiedReportFormsSql(typeH, dateStart, dateEnd, dealerCode, dealerName);
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	public PageResult<Map<String,Object>> queryOnceQualifiedReportForms(String typeH,String dateStart,String dateEnd,
			String dealerCode,String dealerName,int pageSize,int curPage ){
		String sql = queryOnceQualifiedReportFormsSql(typeH, dateStart, dateEnd, dealerCode, dealerName);
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
	}
	
	private String queryOnceQualifiedReportFormsSql(String typeH,String dateStart,String dateEnd,
			String dealerCode,String dealerName){
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT CT.CODE_DESC CLAIM_TYPE, /*单据类型*/\r\n" );
		sql.append("         DS.DEALER_CODE, /*服务站代码*/\r\n" );
		sql.append("         DS.DEALER_NAME, /*服务站全称*/\r\n" );
		sql.append("         SUM(DECODE(A.STATUS, "+Constant.CLAIM_APPLY_ORD_TYPE_06+", 1, 0)) N_TOTAL, /*不合格数量*/\r\n" );
		sql.append("         SUM(DECODE(A.STATUS, "+Constant.CLAIM_APPLY_ORD_TYPE_13+", 1, "+Constant.CLAIM_APPLY_ORD_TYPE_16+", 1, 0)) Y_TOTAL, /*合格数量*/\r\n" );
		sql.append("         COUNT(1) A_TOTAL, /*总数量*/\r\n" );
		sql.append("         ROUND(SUM(DECODE(A.STATUS, "+Constant.CLAIM_APPLY_ORD_TYPE_13+", 1,  "+Constant.CLAIM_APPLY_ORD_TYPE_16+", 1, 0)) * 100 /\r\n" );
		sql.append("               COUNT(1),\r\n" );
		sql.append("               2) || '%' Y_PERCENT /*合格率*/\r\n" );
		sql.append("          FROM TT_AS_WR_APPLICATION A, TM_DEALER DS, TC_CODE CT\r\n" );
		sql.append("         WHERE  A.DEALER_ID = DS.DEALER_ID\r\n" );
		sql.append("           AND A.CLAIM_TYPE = CT.CODE_ID\r\n" );
		sql.append("           AND A.balance_yieldly = "+Constant.PART_IS_CHANGHE_01+"\r\n" );
		sql.append("           AND CT.TYPE = "+Constant.CLA_TYPE+"\r\n" );
		sql.append("    ANd A.CLAIM_TYPE not in (10661002,10661006)  ");
		sql.append("           AND A.STATUS IN ( "+Constant.CLAIM_APPLY_ORD_TYPE_06+", "+Constant.CLAIM_APPLY_ORD_TYPE_13+", "+Constant.CLAIM_APPLY_ORD_TYPE_16+")\r\n" );
		
		
		if(StringUtil.notNull(typeH)){
			sql.append("        AND a.claim_type IN ('"+typeH+"')\r\n" );
		}
		if(StringUtil.notNull(dateStart)){
			sql.append("        and a.FI_DATE >= to_date('"+dateStart+"','yyyy-mm-dd')\r\n" );
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append("        AND a.FI_DATE <= to_Date('"+dateEnd+" 23:59:59' ,'yyyy-mm-dd hh24:mi:ss')\r\n" );
		}
		if(StringUtil.notNull(dealerCode)){
			sql.append("        AND ds.dealer_code LIKE '%"+dealerCode+"%'\r\n" );
		}
		if(StringUtil.notNull(dealerName)){
			sql.append("        AND ds.dealer_name LIKE '%"+dealerName+"%'\r\n" );
		}
		sql.append("         GROUP BY CT.CODE_DESC, DS.DEALER_CODE, DS.DEALER_NAME\r\n" );
		sql.append("         ORDER BY 6 DESC");
		
		return sql.toString();
	}

}
