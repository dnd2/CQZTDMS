package com.infodms.dms.dao.claim.laborList;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.SummaryReportBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class LaborListSummaryReportDao extends BaseDao {
	private LaborListSummaryReportDao(){}
	public static LaborListSummaryReportDao getInstance(){
		return new LaborListSummaryReportDao();
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * 劳务清单汇总报表主页面主查询
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> mainQuery(String con,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select s.sum_parameter_id,\n" );
		sql.append("       s.sum_parameter_no,\n" );
		sql.append("       s.taxable_service_sum_id,\n" );
		sql.append("       s.purchaser_id,\n" );
		sql.append("       s.purchaser_name,\n" );
		sql.append("       s.sales_id,\n" );
		sql.append("       d.dealer_code as dealer_code,\n");
		sql.append("       d.dealer_name,\n" );
		sql.append("       s.invoice_no,\n" );
		sql.append("       s.tax_rate,\n" );
		sql.append("       s.oem_company_id,\n" );
		sql.append("       s.statistics_date, \n" );
		sql.append("       s.auth_status,s.amount \n" );
		sql.append("  from tt_taxable_service_sum s,tm_dealer d\n" );
		sql.append(" where s.dlr_id = d.dealer_id\n");
		if(StringUtil.notNull(con))
			sql.append(con);
		sql.append("  order by s.sum_parameter_no desc\n");
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	//zhumingwei 2011-04-18
	public PageResult<Map<String,Object>> mainQuery11(String con,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("with  tmp  as (select  sa.auth_person_name name,sa.auth_time time,sa.balance_id  from tt_taxable_sum_authitem sa where sa.auth_status="+Constant.TAXABLE_SERVICE_SUM_GET+" ) ");
		sql.append("select s.sum_parameter_id,\n" );
		sql.append("       s.sum_parameter_no,\n" );
		sql.append("       s.taxable_service_sum_id,\n" );
		sql.append("       s.purchaser_id,\n" );
		sql.append("       s.purchaser_name,\n" );
		sql.append("       s.sales_id,\n" );
		sql.append("       d.dealer_code as dealer_code,\n");
		sql.append("       d.dealer_name,\n" );
		sql.append("       s.invoice_no,\n" );
		sql.append("       s.tax_rate,\n" );
		sql.append("       s.oem_company_id,\n" );
		sql.append("       s.statistics_date, \n" );
		sql.append("       s.auth_status,s.amount,a.name,a.time,s.status,(select aa.status from tt_labor_list aa where aa.report_id=s.sum_parameter_id) list_status\n" );
		sql.append("  from tt_taxable_service_sum s,tm_dealer d,tmp a  \n" );
		sql.append(" where s.dlr_id = d.dealer_id and s.sum_parameter_id=a.balance_id(+)\n");
		if(StringUtil.notNull(con))
			sql.append(con);
		sql.append("  order by s.sum_parameter_no desc\n");
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	/*
	 * 劳务清单参数设置弹出框查询方法
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> getReport(String con,int pageSize,int curPage){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select l.report_id,\n" );
		sql.append("       l.report_code,\n" );
		sql.append("       l.dealer_id,\n" );
		sql.append("       l.yieldly,\n" );
		sql.append("       l.receive_date,\n" );
		sql.append("       l.receive_man,\n" );
		sql.append("       l.amount,\n" );
		sql.append("       l.invoice_code,\n" );
		sql.append("       l.make_man,\n" );
		sql.append("       l.status,\n" );
		sql.append("       d.dealer_code,\n");
		sql.append("       d.dealer_name\n");
		sql.append("  from tt_labor_list l,tm_dealer d\n" );
		sql.append(" where l.dealer_id=d.dealer_id\n");
		if(StringUtil.notNull(con))
			sql.append(con);
		sql.append(" order by report_code desc");
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	/*
	 * 针对生成按钮执行的查询操作
	 */
	@SuppressWarnings("unchecked")
	public List<SummaryReportBean> getMyMap(String code){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("  with  TM_MOMO as ( \n" );
		sbSql.append("select c.claim_id,c.balance_id from TR_BALANCE_CLAIM c where exists ( \n");
		sbSql.append("select ld.balance_id from TT_LABOR_LIST ll,TT_LABOR_LIST_DETAIL ld where ll.report_code='"+code+"' \n");
		sbSql.append("and ld.report_id =ll.report_id  and c.balance_id = ld.balance_id \n");
		sbSql.append(")) \n");
		sbSql.append("select '1' as ord, \n");
		sbSql.append("       '走保费' as type, \n");
		sbSql.append("       t.model_code, \n");
		sbSql.append("       sum(BALANCE_AMOUNT) balance_amount \n");
		sbSql.append("  from tt_as_wr_application   t,TM_MOMO a \n");
		sbSql.append(" where claim_type = 10661002 \n");
		sbSql.append("      and t.id = a.claim_id \n");
		sbSql.append(" GROUP BY  t.model_code \n");
		sbSql.append("union \n");
		sbSql.append("select '2' as ord, '工时费',  t.model_code, sum(LABOUR_AMOUNT) \n");
		sbSql.append("  from tt_as_wr_application t,TM_MOMO a \n");
		sbSql.append(" where  t.id = a.claim_id \n");
		sbSql.append(" GROUP BY  t.model_code \n");
		sbSql.append("union \n");
		sbSql.append("select '3' as ord, '材料费',  t.model_code, sum(PART_AMOUNT) \n");
		sbSql.append("  from tt_as_wr_application   t,TM_MOMO a \n");
		sbSql.append(" where \n");
		sbSql.append("      t.id = a.claim_id \n");
		sbSql.append(" GROUP BY  t.model_code \n");
		sbSql.append("union \n");
		sbSql.append("select '4' as ord, '救急费', '', sum(SPEOUTFEE_AMOUNT + OTHER_AMOUNT) \n");
		sbSql.append("  from TT_AS_WR_CLAIM_BALANCE,TM_MOMO a  \n");
		sbSql.append(" where  id=a.balance_id \n");
		sbSql.append("union \n");
		sbSql.append("select '5' as ord, '三包经济补偿费', '', sum(SERVICE_TOTAL_AMOUNT) \n");
		sbSql.append("  from TT_AS_WR_CLAIM_BALANCE,TM_MOMO a  \n");
		sbSql.append(" where  id=a.balance_id \n");
		sbSql.append("union \n");
		sbSql.append("select '6' as ord, '旧件运费 ', '', SUM(RETURN_AMOUNT) \n");
		sbSql.append("   from TT_AS_WR_CLAIM_BALANCE,TM_MOMO a  \n");
		sbSql.append(" where  id=a.balance_id \n");
		sbSql.append("union \n");
		sbSql.append("select '7' as ord, \n");
		sbSql.append("       '三包维修折让', \n");
		sbSql.append("       '', \n");
		sbSql.append("       sum(market_amount + APPEND_AMOUNT + APPEND_LABOUR_AMOUNT) \n");
		sbSql.append("   from TT_AS_WR_CLAIM_BALANCE,TM_MOMO a  \n");
		sbSql.append(" where  id=a.balance_id \n");
		sbSql.append("union \n");
		sbSql.append("select '8' as ord, \n");
		sbSql.append("       '财务扣款', \n");
		sbSql.append("       '', \n");
		sbSql.append("       sum(OLD_DEDUCT + SERVICE_DEDUCT + FREE_DEDUCT + CHECK_DEDUCT + \n");
		sbSql.append("           ADMIN_DEDUCT + FINANCIAL_DEDUCT) \n");
		sbSql.append("   from TT_AS_WR_CLAIM_BALANCE,TM_MOMO a  \n");
		sbSql.append(" where  id=a.balance_id \n");
		sbSql.append("union \n");
		sbSql.append("select '9' as ord, '合计', '', sum(BALANCE_AMOUNT) - sum(nvl(FINANCIAL_DEDUCT,0)) \n");
		sbSql.append("   from TT_AS_WR_CLAIM_BALANCE cb  \n");
		sbSql.append(" where  exists  (select * from TM_MOMO where cb.id = balance_id ) \n");


		System.out.println(sbSql.toString());
		
		return this.select(SummaryReportBean.class, sbSql.toString(), null);
	}
	
	/*
	 * OEM 明细按钮查询方法  主数据
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> getDetail(String id){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select s.taxable_service_sum_id,\n" );
		sql.append("       s.sum_parameter_id,\n" );
		sql.append("       s.sum_parameter_no,\n" );
		sql.append("       s.purchaser_id,\n" );
		sql.append("       s.sales_id,\n" );
		sql.append("       s.invoice_no,\n" );
		sql.append("       s.tax_rate,\n" );
		sql.append("       d.dealer_code,\n" );
		sql.append("       d.dealer_name\n" );
		sql.append("  from tt_taxable_service_sum s,tm_dealer d\n" );
		sql.append(" where s.dlr_id = d.dealer_id\n");
		sql.append("   and s.sum_parameter_id=").append(id).append("\n");
		return this.pageQuery(sql.toString(),null, this.getFunName(), 2, 1);
	}
	
	//zhumingwei 2011-04-18
	public PageResult<Map<String,Object>> getDetail11(String id){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select s.taxable_service_sum_id,\n" );
		sql.append("       s.sum_parameter_id,\n" );
		sql.append("       s.sum_parameter_no,\n" );
		sql.append("       s.purchaser_id,\n" );
		sql.append("       s.sales_id,\n" );
		sql.append("       s.invoice_no,\n" );
		sql.append("       s.amount,\n" );
		sql.append("       s.auth_status,\n" );
		sql.append("       s.tax_rate,\n" );
		sql.append("       d.dealer_code,\n" );
		sql.append("       d.dealer_name\n" );
		sql.append("  from tt_taxable_service_sum s,tm_dealer d\n" );
		sql.append(" where s.dlr_id = d.dealer_id\n");
		sql.append("   and s.sum_parameter_id=").append(id).append("\n");
		return this.pageQuery(sql.toString(),null, this.getFunName(), 2, 1);
	}
	
	/*
	 * SQL
	 */
	public void insertPrintDetail(Long id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("insert into tt_taxable_sum_print_tmp\n");
		sql.append("  (id, list_id, order_type, order_desc, model_code, balance_amount)\n");  
		sql.append("  select * from (WITH ININ AS (\n");  
		sql.append("SELECT /*+ORDERED*/\n" );
		sql.append(" TBC.CLAIM_ID\n" );
		sql.append("  FROM TT_LABOR_LIST_DETAIL   TLLD,\n" );
		sql.append("       TT_AS_WR_CLAIM_BALANCE TAWCB,\n" );
		sql.append("       TR_BALANCE_CLAIM       TBC\n" );
		sql.append(" WHERE TBC.BALANCE_ID = TAWCB.ID\n" );
		sql.append("   AND TAWCB.ID = TLLD.BALANCE_ID\n" );
		sql.append("   AND TLLD.REPORT_ID = "+id+"");

		sql.append(")\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '1' as ord,\n");  
		sql.append("                  '走保费' as type,\n");  
		sql.append("                  T.MODEL_CODE model_code,\n");  
		sql.append("                  sum(BALANCE_AMOUNT) balance_amount\n");  
		sql.append("             from tt_as_wr_application t\n");  
		sql.append("            where claim_type = 10661002\n");  
		sql.append("              AND EXISTS\n");  
		sql.append("            (SELECT 1 FROM ININ WHERE ININ.CLAIM_ID = T.ID)\n");  
		sql.append("            GROUP BY T.MODEL_CODE\n");  
		sql.append("           union\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '2' as ord,\n");  
		sql.append("                  '工时费',\n");  
		sql.append("                  T.MODEL_CODE model_code,\n");  
		sql.append("                  sum(nvl(T.BALANCE_LABOUR_AMOUNT,0)+nvl(T.Append_Amount,0) +nvl(T.Appendlabour_Amount,0))  \n");  
		sql.append("             from tt_as_wr_application t\n");  
		sql.append("            where T.CLAIM_TYPE IN (10661001, 10661007, 10661009)\n");  
		sql.append("              AND EXISTS\n");  
		sql.append("            (SELECT 1 FROM ININ WHERE ININ.CLAIM_ID = T.ID)\n");  
		sql.append("            GROUP BY T.MODEL_CODE\n");  
		sql.append("\n");  
		sql.append("           union\n");  
		sql.append("\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '3' as ord,\n");  
		sql.append("                  '材料费',\n");  
		sql.append("                  T.MODEL_CODE model_code,\n");  
		sql.append("                  sum(T.BALANCE_PART_AMOUNT)\n");  
		sql.append("             from tt_as_wr_application t\n");  
		sql.append("            where T.CLAIM_TYPE IN (10661001, 10661007, 10661009)\n");  
		sql.append("              AND EXISTS\n");  
		sql.append("            (SELECT 1 FROM ININ WHERE ININ.CLAIM_ID = T.ID)\n");  
		sql.append("            GROUP BY T.MODEL_CODE\n");  
		sql.append("\n");  
		sql.append("           union\n");  
		sql.append("\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '4' as ord,\n");  
		sql.append("                  '救急费',\n");  
		sql.append("                  '',\n");  
		sql.append("                  SUM(TAWCB.SPEOUTFEE_AMOUNT + TAWCB.OTHER_AMOUNT)\n");  
		sql.append("             from TT_AS_WR_CLAIM_BALANCE TAWCB\n");  
		sql.append("            where EXISTS (SELECT 1\n");  
		sql.append("                     FROM TT_LABOR_LIST_DETAIL TLLD, TT_LABOR_LIST TLL\n");  
		sql.append("                    WHERE TLLD.REPORT_ID = TLL.REPORT_ID\n");  
		sql.append("                      AND TLL.REPORT_id = "+id+"\n");  
		sql.append("                      AND TLLD.BALANCE_ID = TAWCB.ID)\n");  
		sql.append("\n");  
		sql.append("           union\n");  
		sql.append("\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '7' as ord,\n");  
		sql.append("                  '三包维修折让',\n");  
		sql.append("                  '',\n");  
		sql.append("                  sum(TAWCB.SERVICE_TOTAL_AMOUNT)\n");  
		sql.append("             from TT_AS_WR_CLAIM_BALANCE TAWCB\n");  
		sql.append("            where EXISTS (SELECT 1\n");  
		sql.append("                     FROM TT_LABOR_LIST_DETAIL TLLD, TT_LABOR_LIST TLL\n");  
		sql.append("                    WHERE TLLD.REPORT_ID = TLL.REPORT_ID\n");  
		sql.append("                      AND TLL.REPORT_id = "+id+"\n");  
		sql.append("                      AND TLLD.BALANCE_ID = TAWCB.ID)\n");  
		sql.append("\n");  
		sql.append("           union\n");  
		sql.append("\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '5' as ord,\n");  
		sql.append("                  '运费 ',\n");  
		sql.append("                  '',\n");  
		sql.append("                  SUM(nvl(TAWCB.RETURN_AMOUNT_BAK,0))\n");  
		sql.append("             from TT_AS_WR_CLAIM_BALANCE TAWCB\n");  
		sql.append("            where EXISTS (SELECT 1\n");  
		sql.append("                     FROM TT_LABOR_LIST_DETAIL TLLD, TT_LABOR_LIST TLL\n");  
		sql.append("                    WHERE TLLD.REPORT_ID = TLL.REPORT_ID\n");  
		sql.append("                      AND TLL.REPORT_id = "+id+"\n");  
		sql.append("                      AND TLLD.BALANCE_ID = TAWCB.ID)\n");  
		sql.append("\n");  
		sql.append("           union\n");  
		sql.append("\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '6' as ord,\n");  
		sql.append("                  '三包经济补偿',\n");  
		sql.append("                  '',\n");  
		sql.append("                  sum(TAWCB.market_amount\n");  
		sql.append("                   )\n");  
		sql.append("             from TT_AS_WR_CLAIM_BALANCE TAWCB\n");  
		sql.append("            where EXISTS (SELECT 1\n");  
		sql.append("                     FROM TT_LABOR_LIST_DETAIL TLLD, TT_LABOR_LIST TLL\n");  
		sql.append("                    WHERE TLLD.REPORT_ID = TLL.REPORT_ID\n");  
		sql.append("                      AND TLL.REPORT_id = "+id+"\n");  
		sql.append("                      AND TLLD.BALANCE_ID = TAWCB.ID)\n");  
		sql.append("\n");  
		sql.append("           union\n");  
		sql.append("\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '8' as ord,\n");  
		sql.append("                  '审核扣款',\n");  
		sql.append("                  '',\n");  
		sql.append("                  sum(NVL(TAWCB.OLD_DEDUCT,0) + NVL(TAWCB.SERVICE_DEDUCT,0) +\n");  
		sql.append("                      NVL(TAWCB.FREE_DEDUCT,0) + NVL(TAWCB.CHECK_DEDUCT,0) +\n");  
		sql.append("                      NVL(TAWCB.ADMIN_DEDUCT,0) + NVL(TAWCB.FINANCIAL_DEDUCT,0) + NVL(TAWCB.RETURN_AMOUNT_BAK,0) - NVL(TAWCB.RETURN_AMOUNT,0))\n");  
		sql.append("             from TT_AS_WR_CLAIM_BALANCE TAWCB\n");  
		sql.append("            where EXISTS (SELECT 1\n");  
		sql.append("                     FROM TT_LABOR_LIST_DETAIL TLLD, TT_LABOR_LIST TLL\n");  
		sql.append("                    WHERE TLLD.REPORT_ID = TLL.REPORT_ID\n");  
		sql.append("                      AND TLL.REPORT_id = "+id+"\n");  
		sql.append("                      AND TLLD.BALANCE_ID = TAWCB.ID)\n");  
		sql.append("           union\n");  
		sql.append("\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '9' as ord,\n");  
		sql.append("                  '总计',\n");  
		sql.append("                  '',\n");  
		sql.append("                  sum(NVL(TAWCB.BALANCE_AMOUNT,0)) - sum(NVL(TAWCB.FINANCIAL_DEDUCT,0))\n");  
		sql.append("             from TT_AS_WR_CLAIM_BALANCE TAWCB\n");  
		sql.append("            where EXISTS (SELECT 1\n");  
		sql.append("                     FROM TT_LABOR_LIST_DETAIL TLLD, TT_LABOR_LIST TLL\n");  
		sql.append("                    WHERE TLLD.REPORT_ID = TLL.REPORT_ID\n");  
		sql.append("                      AND TLL.REPORT_id = "+id+"\n");  
		sql.append("                      AND TLLD.BALANCE_ID = TAWCB.ID))\n");
		this.insert(sql.toString());
	}
	
	
	public void insertPrintDetail2(Long id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("insert into tt_taxable_sum_print_tmp\n");
		sql.append("  (id, list_id, order_type, order_desc, model_code, balance_amount)\n");  
		sql.append("  select * from (WITH ININ AS (\n");  
		sql.append("select /*+ index (TR_BALANCE_CLAIM FK_TR_BALANCE_ID)*/ * from TR_BALANCE_CLAIM tbc where exists (\n" );
		sql.append("select TAWCB.Id from TT_AS_WR_CLAIM_BALANCE TAWCB ,TT_LABOR_LIST_DETAIL TLLD\n" );
		sql.append("where\n" );
		sql.append("TLLD.REPORT_ID="+id+" and   TAWCB.ID = TLLD.BALANCE_ID\n" );
		sql.append("and tbc.balance_id=TAWCB.Id\n" );
		sql.append(")");
		sql.append(")\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '1' as ord,\n");  
		sql.append("                  '走保费' as type,\n");  
		sql.append("                  T.MODEL_CODE model_code,\n");  
		sql.append("                  sum(BALANCE_AMOUNT) balance_amount\n");  
		sql.append("             from tt_as_wr_application t\n");  
		sql.append("            where claim_type = 10661002\n");  
		sql.append("              AND EXISTS\n");  
		sql.append("            (SELECT 1 FROM ININ WHERE ININ.CLAIM_ID = T.ID)\n");  
		sql.append("            GROUP BY T.MODEL_CODE\n");  
		sql.append("           union\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '2' as ord,\n");  
		sql.append("                  '工时费',\n");  
		sql.append("                  T.MODEL_CODE model_code,\n");  
		sql.append("                  sum(nvl(T.BALANCE_LABOUR_AMOUNT,0)+nvl(T.Append_Amount,0) +nvl(T.Appendlabour_Amount,0))  \n");  
		sql.append("             from tt_as_wr_application t\n");  
		sql.append("            where T.CLAIM_TYPE IN (10661001, 10661007, 10661009)\n");  
		sql.append("              AND EXISTS\n");  
		sql.append("            (SELECT 1 FROM ININ WHERE ININ.CLAIM_ID = T.ID)\n");  
		sql.append("            GROUP BY T.MODEL_CODE\n");  
		sql.append("\n");  
		sql.append("           union\n");  
		sql.append("\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '3' as ord,\n");  
		sql.append("                  '材料费',\n");  
		sql.append("                  T.MODEL_CODE model_code,\n");  
		sql.append("                  sum(T.BALANCE_PART_AMOUNT)\n");  
		sql.append("             from tt_as_wr_application t\n");  
		sql.append("            where T.CLAIM_TYPE IN (10661001, 10661007, 10661009)\n");  
		sql.append("              AND EXISTS\n");  
		sql.append("            (SELECT 1 FROM ININ WHERE ININ.CLAIM_ID = T.ID)\n");  
		sql.append("            GROUP BY T.MODEL_CODE\n");  
		sql.append("\n");  
		sql.append("           union\n");  
		sql.append("\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '4' as ord,\n");  
		sql.append("                  '救急费',\n");  
		sql.append("                  '',\n");  
		sql.append("                  SUM(TAWCB.SPEOUTFEE_AMOUNT + TAWCB.OTHER_AMOUNT)\n");  
		sql.append("             from TT_AS_WR_CLAIM_BALANCE TAWCB\n");  
		sql.append("            where EXISTS (SELECT 1\n");  
		sql.append("                     FROM TT_LABOR_LIST_DETAIL TLLD, TT_LABOR_LIST TLL\n");  
		sql.append("                    WHERE TLLD.REPORT_ID = TLL.REPORT_ID\n");  
		sql.append("                      AND TLL.REPORT_id = "+id+"\n");  
		sql.append("                      AND TLLD.BALANCE_ID = TAWCB.ID)\n");  
		sql.append("\n");  
		sql.append("           union\n");  
		sql.append("\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '7' as ord,\n");  
		sql.append("                  '三包维修折让',\n");  
		sql.append("                  '',\n");  
		sql.append("                  sum(TAWCB.SERVICE_TOTAL_AMOUNT)\n");  
		sql.append("             from TT_AS_WR_CLAIM_BALANCE TAWCB\n");  
		sql.append("            where EXISTS (SELECT 1\n");  
		sql.append("                     FROM TT_LABOR_LIST_DETAIL TLLD, TT_LABOR_LIST TLL\n");  
		sql.append("                    WHERE TLLD.REPORT_ID = TLL.REPORT_ID\n");  
		sql.append("                      AND TLL.REPORT_id = "+id+"\n");  
		sql.append("                      AND TLLD.BALANCE_ID = TAWCB.ID)\n");  
		sql.append("\n");  
		sql.append("           union\n");  
		sql.append("\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '5' as ord,\n");  
		sql.append("                  '运费 ',\n");  
		sql.append("                  '',\n");  
		sql.append("                  SUM(nvl(TAWCB.RETURN_AMOUNT_BAK,0))\n");  
		sql.append("             from TT_AS_WR_CLAIM_BALANCE TAWCB\n");  
		sql.append("            where EXISTS (SELECT 1\n");  
		sql.append("                     FROM TT_LABOR_LIST_DETAIL TLLD, TT_LABOR_LIST TLL\n");  
		sql.append("                    WHERE TLLD.REPORT_ID = TLL.REPORT_ID\n");  
		sql.append("                      AND TLL.REPORT_id = "+id+"\n");  
		sql.append("                      AND TLLD.BALANCE_ID = TAWCB.ID)\n");  
		sql.append("\n");  
		sql.append("           union\n");  
		sql.append("\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '6' as ord,\n");  
		sql.append("                  '三包经济补偿',\n");  
		sql.append("                  '',\n");  
		sql.append("                  sum(TAWCB.market_amount\n");  
		sql.append("                   )\n");  
		sql.append("             from TT_AS_WR_CLAIM_BALANCE TAWCB\n");  
		sql.append("            where EXISTS (SELECT 1\n");  
		sql.append("                     FROM TT_LABOR_LIST_DETAIL TLLD, TT_LABOR_LIST TLL\n");  
		sql.append("                    WHERE TLLD.REPORT_ID = TLL.REPORT_ID\n");  
		sql.append("                      AND TLL.REPORT_id = "+id+"\n");  
		sql.append("                      AND TLLD.BALANCE_ID = TAWCB.ID)\n");  
		sql.append("\n");  
		sql.append("           union\n");  
		sql.append("\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '8' as ord,\n");  
		sql.append("                  '审核扣款',\n");  
		sql.append("                  '',\n");  
		sql.append("                  sum(NVL(TAWCB.OLD_DEDUCT,0) + NVL(TAWCB.SERVICE_DEDUCT,0) +\n");  
		sql.append("                      NVL(TAWCB.FREE_DEDUCT,0) - NVL(TAWCB.CHECK_DEDUCT,0) +\n");  
		sql.append("                      NVL(TAWCB.ADMIN_DEDUCT,0) + NVL(TAWCB.FINANCIAL_DEDUCT,0) + NVL(TAWCB.RETURN_AMOUNT_BAK,0) - NVL(TAWCB.RETURN_AMOUNT,0))\n");  
		sql.append("             from TT_AS_WR_CLAIM_BALANCE TAWCB\n");  
		sql.append("            where EXISTS (SELECT 1\n");  
		sql.append("                     FROM TT_LABOR_LIST_DETAIL TLLD, TT_LABOR_LIST TLL\n");  
		sql.append("                    WHERE TLLD.REPORT_ID = TLL.REPORT_ID\n");  
		sql.append("                      AND TLL.REPORT_id = "+id+"\n");  
		sql.append("                      AND TLLD.BALANCE_ID = TAWCB.ID)\n");  
		sql.append("           union\n");  
		sql.append("\n");  
		sql.append("           select f_getid,\n");  
		sql.append("                  '"+id+"',\n");  
		sql.append("                  '9' as ord,\n");  
		sql.append("                  '总计',\n");  
		sql.append("                  '',\n");  
		sql.append("                  sum(NVL(TAWCB.BALANCE_AMOUNT,0)) - sum(NVL(TAWCB.FINANCIAL_DEDUCT,0))\n");  
		sql.append("             from TT_AS_WR_CLAIM_BALANCE TAWCB\n");  
		sql.append("            where EXISTS (SELECT 1\n");  
		sql.append("                     FROM TT_LABOR_LIST_DETAIL TLLD, TT_LABOR_LIST TLL\n");  
		sql.append("                    WHERE TLLD.REPORT_ID = TLL.REPORT_ID\n");  
		sql.append("                      AND TLL.REPORT_id = "+id+"\n");  
		sql.append("                      AND TLLD.BALANCE_ID = TAWCB.ID))\n");
		this.insert(sql.toString());
	}
	
	/*
	 * 查询通过存储过程写进去的值以供打印显示
	 */
	public List<SummaryReportBean> getPrintDetail(Long id){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.order_type ord,\n");
		sql.append("       t.order_desc type,\n");  
		sql.append("       GG.GROUP_NAME model_code,\n");  
		sql.append("       sum(balance_amount) balance_amount\n");  
		sql.append("  from tt_taxable_sum_print_tmp t, TM_VHCL_MATERIAL_GROUP G ,TM_VHCL_MATERIAL_GROUP GG\n");  
		sql.append(" where t.model_code = g.group_code(+)\n");  
		sql.append("   and t.list_id = "+id+"\n");  
		sql.append("   AND G.PARENT_GROUP_ID = GG.GROUP_ID(+)\n");   
		sql.append(" group by  Gg.GROUP_NAME ,t.order_type,t.order_desc\n");  
		sql.append(" order by Gg.GROUP_NAME,t.order_type asc\n");
		return this.select(SummaryReportBean.class, sql.toString(), null);
	}
	
	//小写转大写
	public  String numToChinese(String input){
		if(input.indexOf(".")!=-1){
			//取小数
	        int templen=input.indexOf(".");
	        int templen3=input.length();
	        if(templen3<(templen+3)){
	        	for(int i=0;((templen+3)-templen3)>i;i++){
	        		input=input+"0";
	        	}
	        }
	        return this.parseMoneyF(input);
	    }else{
	    	int templen2=input.length();
	        if(templen2<10){
	        	for(int i=0;8-templen2>i;i++){
	        		input="0"+input;
	        	}
	        }
	        return this.parseMoneyInt(input);
	    }
	}
	public String parseMoneyF(String input){//float的时候
		String tempnum=input;
	    int len=0;
	    String fcount="";
	    String convertnum="";
	    len=tempnum.indexOf(".");
	    fcount=tempnum.substring(len+1, len+3); 
	    String s1="零壹贰叁肆伍陆柒捌玖";
	    int nunlen=0;
	    String numString="";
	    numString=tempnum.substring(0, len);//取整数
	    nunlen=numString.length();
	    for(int i=0;nunlen>i;i++){
	    	String convertS1=String.valueOf(s1.charAt(Integer.valueOf(String.valueOf(numString.charAt((nunlen-i-1))))));
	        //转中文
	        if(i==0){
	        	convertnum=convertS1+"元"+""+convertnum;
	        }
	        if(i==1){
	            convertnum=convertS1+"拾"+""+convertnum;
	        }
	        if(i==2){
	            convertnum=convertS1+"佰"+""+convertnum;
	        }
	        if(i==3){
	            convertnum=convertS1+"仟"+""+convertnum;
	        }
	        if(i==4){
	            convertnum=convertS1+"万"+""+convertnum;
	        }
	        if(i==5){
	            convertnum=convertS1+"拾"+""+convertnum;
	        }
	        if(i==6){
	            convertnum=convertS1+"佰"+""+convertnum;
	        }
	        if(i==7){
	            convertnum=convertS1+"仟"+""+convertnum;
	        }
	        if(i==8){
	            convertnum=convertS1+"亿"+""+convertnum;
	        }
	        if(i==9){
	            convertnum=convertS1+"拾"+""+convertnum;
	        }
	    }
	    for(int i2=0;fcount.length()>i2;i2++){
	    	String convertX1= String.valueOf(s1.charAt(Integer.valueOf(String.valueOf(fcount.charAt(i2)))));
	        if(i2==0){
	        	convertnum=convertnum+convertX1+"角"+"";
	        }
	        if(i2==1){
	            convertnum=convertnum+convertX1+"分"+"";
	        }
	    }
	    return convertnum;
	}
	public String parseMoneyInt(String input){  //整数时
	    String tempnum=input;
	    String convertnum="";  
	    String s1="零壹贰叁肆伍陆柒捌玖";
	    int nunlen=0;
	    nunlen=tempnum.length();
	    for(int i=0;nunlen>i;i++){
	    	String convertS1=String.valueOf(s1.charAt(Integer.valueOf(String.valueOf(tempnum.charAt((nunlen-i-1))))));
	        //转中文
	        if(i==0){
	            convertnum=convertS1+"元"+""+convertnum;
	        }
	        if(i==1){
	            convertnum=convertS1+"拾"+""+convertnum;
	        }
	        if(i==2){
	            convertnum=convertS1+"佰"+""+convertnum;
	        }
	        if(i==3){
	            convertnum=convertS1+"仟"+""+convertnum;
	        }
	        if(i==4){
	            convertnum=convertS1+"万"+""+convertnum;
	        }
	        if(i==5){
	            convertnum=convertS1+"拾"+""+convertnum;
	        }
	        if(i==6){
	            convertnum=convertS1+"佰"+""+convertnum;
	        }
	        if(i==7){
	            convertnum=convertS1+"仟"+""+convertnum;
	        }
	        if(i==8){
	            convertnum=convertS1+"亿"+""+convertnum;
	        }
	        if(i==9){
	            convertnum=convertS1+"拾"+""+convertnum;
	        }
	   }
	   return convertnum+"  零角  零分";
	}
	
	public List<Map<String, Object>> getRegionName(String regionCode) {
		StringBuffer sql = new StringBuffer("\n") ;
		sql.append("SELECT a.region_name FROM Tm_Region a where a.region_code="+regionCode+"\n");
		return pageQuery(sql.toString(), null, super.getFunName()) ;
	}
}
