package com.infodms.dms.dao.report.jcafterservicereport;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.report.jcafterservicereport.FinancialCreditReport;
import com.infodms.dms.bean.AAABean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class FinancialCreditReportDao extends BaseDao{
	private Logger logger = Logger.getLogger(FinancialCreditReport.class);
	private FinancialCreditReportDao(){}
	public static FinancialCreditReportDao getInstance(){
		return new FinancialCreditReportDao();
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PageResult<Map<String,Object>> getFinancialCreditReport(String con,String con1,String yieldly,String year,int pageSize,int curPage){
		List<Object> ins = new LinkedList<Object>();
		ins.add(yieldly);
		ins.add(year);
		this.callProcedure("P_CREDIT_REPORT", ins,null) ;

		StringBuffer sql = new StringBuffer("\n");
		sql.append("select * from tmp_financial a where 1=1");
		if(StringUtil.notNull(con1))
			sql.append(con);
		
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
}
