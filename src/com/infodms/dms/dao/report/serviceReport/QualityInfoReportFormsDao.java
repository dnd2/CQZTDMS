package com.infodms.dms.dao.report.serviceReport;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class QualityInfoReportFormsDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(QualityInfoReportFormsDao.class);
	public static QualityInfoReportFormsDao dao = new QualityInfoReportFormsDao();
	public static QualityInfoReportFormsDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public List<Map<String, Object>> getQualityInfoReportFormsList(String verifyBy,String AdateStart,String AdateEnd,
			String VdateStart,String VdateEnd,String series,String model,String dealerName,String tmorg,
			String tmOrgSmall,String dealersH,String importantLevelsH ){
		String sql = queryQuliatyInfoSql(verifyBy, AdateStart, AdateEnd, VdateStart, VdateEnd, series, model, dealerName, tmorg, tmOrgSmall, dealersH, importantLevelsH);
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	public PageResult<Map<String,Object>> queryQualityInfoReportForms(String verifyBy,String AdateStart,String AdateEnd,
			String VdateStart,String VdateEnd,String series,String model,String dealerName,String tmorg,
			String tmOrgSmall,String dealersH,String importantLevelsH,int pageSize,int curPage ){
		String sql = queryQuliatyInfoSql(verifyBy, AdateStart, AdateEnd, VdateStart, VdateEnd, series, model, dealerName, tmorg, tmOrgSmall, dealersH, importantLevelsH);
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
	}
	
	private String queryQuliatyInfoSql(String verifyBy,String AdateStart,String AdateEnd,
			String VdateStart,String VdateEnd,String series,String model,String dealerName,String tmorg,
			String tmOrgSmall,String dealersH,String importantLevelsH){
		StringBuffer sql = new StringBuffer();


		sql.append("select a.important_level IMPORTANTLEVEL, --重要度\r\n" );
		sql.append("       c.group_name SERIESNAME, --车系\r\n" );
		sql.append("       d.group_name MODELNAME, --车型\r\n" );
		sql.append("       f.root_org_name ROOTORGNAME, --大区\r\n" );
		sql.append("       f.org_name ORGNAME, --小区\r\n" );
		sql.append("       f.dealer_code DEALERCODE, --经销商CODE\r\n" );
		sql.append("       f.dealer_name DEALERNAME, --经销商全称\r\n" );
		sql.append("       a.FAULT_NAME FAULTNAME, --故障名称\r\n" );
		sql.append("       a.CONDITION CONDITION, --条件及现象\r\n" );
		sql.append("       a.CHECK_RESULT CHECKRESULT, --检查结论\r\n" );
		sql.append("       a.content CONTENT, --处理方法\r\n" );
		sql.append("       e.PART_OLDCODE PARTOLDCODE, --配件代码\r\n" );
		sql.append("       e.part_cname PARTCNAME, --配件名称\r\n" );
		sql.append("       g.maker_code MAKERCODE, --制造商代码\r\n" );
		sql.append("       g.maker_name MAKERNAME, --制造商名称\r\n" );
		sql.append("       a.remark REMARK, --补充说明\r\n" );
		sql.append("       to_char(a.applay_date,'yyyy-MM-dd') APPLYADATE, --上报时间\r\n" );
		sql.append("       to_char(a.purchased_date,'yyyy-MM-dd') PURCHASEDDATE, --销售日期\r\n" );
		sql.append("       to_char(a.fault_date,'yyyy-MM-dd') FAULTDATE, --故障日期\r\n" );
		sql.append("       a.vin VIN, --VIN\r\n" );
		sql.append("       a.engine_no ENGINENO, --发动机号\r\n" );
		sql.append("       a.purpose PURPOSE, --用途\r\n" );
		sql.append("       a.road ROAD, --道路状况\r\n" );
		sql.append("       a.temperature TEMP, --温度\r\n" );
		sql.append("       a.happen_time || a.happen_speed HAPPENTIMESPEED, --发生的时机速度\r\n" );
		sql.append("       a.rain RAIN, --雨水状况\r\n" );
		sql.append("       a.air_condition_status AIRSTATUS, --空调状态\r\n" );
		sql.append("       a.used USED, --平时使用状况\r\n" );
		sql.append("       h.code_desc VERIFYSTAUTS, --审核状态\r\n" );
		sql.append("       i.name VERIFYNAME, --审核人\r\n" );
		sql.append("       to_char(a.verify_date,'yyyy-MM-dd') VERIFYDATE --审核时间\r\n" );
		sql.append("  from TT_SALES_QUALITY_INFO_REPORT a\r\n" );
		sql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP D\r\n" );
		sql.append("    ON a.MODEL_ID = D.GROUP_ID\r\n" );
		sql.append("   AND d.group_level = 3\r\n" );
		sql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP C\r\n" );
		sql.append("    ON c.group_id = d.parent_group_id\r\n" );
		sql.append("  LEFT JOIN VW_ORG_DEALER_SERVICE F\r\n" );
		sql.append("    ON A.DEALER_ID = F.DEALER_ID\r\n" );
		sql.append("  LEFT JOIN TT_PART_DEFINE E\r\n" );
		sql.append("    ON E.PART_ID = A.PART_ID\r\n" );
		sql.append("  LEFT JOIN TT_PART_MAKER_DEFINE G\r\n" );
		sql.append("    ON G.MAKER_ID = A.MARKER_ID\r\n" );
		sql.append("  LEFT JOIN TC_CODE H\r\n" );
		sql.append("    ON H.CODE_ID = A.VERIFY_STATUS\r\n" );
		sql.append("  LEFT JOIN TC_USER I\r\n" );
		sql.append("    ON I.USER_ID = A.VERIFY_BY");

		sql.append("  where 1=1 ");
		
		if(StringUtil.notNull(verifyBy)){
			sql.append(" and i.name like '%"+verifyBy+"%' \r\n");
		}
		if(StringUtil.notNull(AdateStart)){
			sql.append(" and a.applay_date >=to_date('"+AdateStart+"','yyyy-MM-dd') \r\n");
		}
		if(StringUtil.notNull(AdateEnd)){
			sql.append(" and a.applay_date<=to_date('"+AdateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \r\n");
		}
		if(StringUtil.notNull(VdateStart)){
			sql.append(" and a.verify_date >=to_date('"+VdateStart+"','yyyy-MM-dd') \r\n");
		}
		if(StringUtil.notNull(VdateEnd)){
			sql.append(" and a.verify_date<=to_date('"+VdateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \r\n");
		}
		if(StringUtil.notNull(series)){
			sql.append(" and c.group_id = "+series+"\r\n");
		}
		if(StringUtil.notNull(model)){
			sql.append(" and d.group_id = "+model+"\r\n");
		}
		if(StringUtil.notNull(dealerName)){
			sql.append(" and f.dealer_name like '%"+dealerName+"%'\r\n");
		}
		if(StringUtil.notNull(tmorg)){
			sql.append(" and f.root_org_id = "+tmorg+"\r\n");
		}
		if(StringUtil.notNull(tmOrgSmall)){
			sql.append(" and f.org_id = "+tmOrgSmall+"\r\n");
		}
		if(StringUtil.notNull(dealersH)){
			sql.append(" and f.dealer_code in ('"+dealersH+"')\r\n");
		}
		if(StringUtil.notNull(importantLevelsH)){
			sql.append(" and a.important_level in ('"+importantLevelsH+"')\r\n");
		}
		
		
		return sql.toString();
	}

}
