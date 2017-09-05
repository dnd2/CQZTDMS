package com.infodms.dms.dao.report.jcafterservicereport;

import java.sql.Clob;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsReportInputparamPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

public class DynamicReportMngDao extends BaseDao{
	private DynamicReportMngDao(){}
	protected POFactory factory = POFactoryBuilder.getInstance();
	public static DynamicReportMngDao getInstance(){
		return new DynamicReportMngDao() ;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PageResult<Map<String,Object>> queryAllReport(String con,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n") ;
		sql.append(" select r.report_id,r.report_name,r.remark,r.create_by,r.create_date,r.oem_only,r.remark2,r.mention_person,r.mention_time,dbms_lob.substr(r.main_sql,3000,0) as MAIN_SQL from tt_as_report r where 1=1  ") ;
		if(StringUtil.notNull(con))
			sql.append(con) ;
		return this.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage) ;
	}
	
	public void insertMainSql(String sql,String tableName,String culName,Long id){
        Map<String,Object> parm = new HashMap<String, Object>();
        parm.put("REPORT_ID",id );
		factory.updateClob(sql, tableName, culName, parm);
	}
	public String readMainSql(Long report_id){
		Map<String,Object> parm = new HashMap<String, Object>();
		parm.put("REPORT_ID", report_id);
		String sql = factory.readClob("tt_as_report", "MAIN_SQL",parm );
		return sql;
		
	}
	public PageResult<Map<String, Object>> doQuery1(String sql,int pageSize,int curPage){
		return this.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage) ;
	}
	public List<Map<String, Object>> doQuery(String sql,int pageSize,int curPage){
		return this.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	public List<TtAsReportInputparamPO> getInput(Long id){
		StringBuffer sql = new StringBuffer("\n") ;
		sql.append("select *\n");
		sql.append("  from tt_as_report_inputparam i\n");  
		sql.append(" where i.param_type is not null\n");  
		sql.append("   and i.report_id = ").append(id).append("\n");
		return this.select(TtAsReportInputparamPO.class, sql.toString(), null);
	}
}
