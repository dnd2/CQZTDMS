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
public class PartReportFormsDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(PartReportFormsDao.class);
	public static PartReportFormsDao dao = new PartReportFormsDao();
	public static PartReportFormsDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public List<Map<String, Object>> getPartReportFormsList(String dealerCode,String dealerName,String partCode,
			String partName,String yieldly,String dateStart,String dateEnd){
		String sql = queryPartReportFormsSql(dealerCode, dealerName, partCode, partName, yieldly,dateStart, dateEnd);
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	public PageResult<Map<String,Object>> queryQualityInfoReportForms(String dealerCode,String dealerName,String partCode,
			String partName,String yieldly,String dateStart,String dateEnd,int pageSize,int curPage ){
		String sql = queryPartReportFormsSql(dealerCode, dealerName, partCode, partName,yieldly, dateStart, dateEnd);
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
	}
	
	private String queryPartReportFormsSql(String dealerCode,String dealerName,String partCode,
			String partName,String yieldly,String dateStart,String dateEnd){
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT DS.DEALER_CODE, /*服务商代码*/\r\n" );
		sql.append("       DS.DEALER_NAME, /*服务商名称*/\r\n" );
		sql.append("       P.PART_CODE, /*配件代码*/\r\n" );
		sql.append("       P.PART_NAME, /*配件名称*/\r\n" );
		sql.append("       SUM(NVL(P.QUANTITY, 0)) QUANTITY /*换上件数量*/\r\n" );
		sql.append("  FROM TT_AS_WR_APPLICATION  A,\r\n" );
		sql.append("       VW_ORG_DEALER_SERVICE DS,\r\n" );
		sql.append("       TT_AS_WR_PARTSITEM    P\r\n" );
		sql.append(" WHERE A.DEALER_ID = DS.DEALER_ID\r\n" );
		sql.append("   AND A.ID = P.ID\r\n" );
		sql.append("   AND a.status >= "+Constant.CLAIM_APPLY_ORD_TYPE_02+"\r\n" );
		
		
		if(StringUtil.notNull(dealerCode)){
			sql.append("   AND ds.dealer_code LIKE '%"+dealerCode+"%'\r\n" );
		}
		if(StringUtil.notNull(dealerName)){
			sql.append("AND ds.dealer_name LIKE '%"+dealerName+"%'\r\n" );
		}
		if(StringUtil.notNull(partCode)){
			sql.append("AND p.part_code IN ('"+partCode+"')\r\n" );
		}
		if(StringUtil.notNull(partName)){
			sql.append("AND p.part_name LIKE '%"+partName+"%'\r\n" );
		}
		if(StringUtil.notNull(yieldly)){
			sql.append("AND A.balance_yieldly = '"+yieldly+"' \r\n" );
		}
		if(StringUtil.notNull(dateStart)){
			sql.append("AND a.report_date >= to_date('"+dateStart+"','yyyy-mm-dd')\r\n" );
		}
		if(StringUtil.notNull(dateEnd)){
			sql.append("AND a.report_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n" );
		}
		
		sql.append(" GROUP BY DS.DEALER_CODE, DS.DEALER_NAME, P.PART_CODE, P.PART_NAME\r\n" );
		sql.append(" ORDER BY 5 DESC");
		return sql.toString();
	}

}
