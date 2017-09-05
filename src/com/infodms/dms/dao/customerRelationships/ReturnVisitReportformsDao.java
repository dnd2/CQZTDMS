package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;



@SuppressWarnings("unchecked")
public class ReturnVisitReportformsDao extends BaseDao{

	private static final ReturnVisitReportformsDao dao = new ReturnVisitReportformsDao();
	
	public static final ReturnVisitReportformsDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	
	
	public PageResult<Map<String,Object>> queryReturnVisitReportforms(String dealName,String dateStart,String dateEnd,int pageSize,int curPage){

		String sql  = returnSql(dealName,dateStart,dateEnd);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	} 
	
	
	public List<Map<String,Object>> queryReturnVisitReportformsList(String dealName,String dateStart,String dateEnd){

		String sql  = returnSql(dealName,dateStart,dateEnd);
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	

	

	private String returnSql(String dealName,
									String dateStart,String dateEnd){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select NVL(j.MODELNAME, '合计') MODELNAME,\r\n");
		sbSql.append("       sum(j.TOTAL) TOTAL,\r\n");
		sbSql.append("       sum(j.SUCC) SUCC,\r\n");
		sbSql.append("       sum(j.FAILC) FAILC,\r\n");
		sbSql.append("       sum(j.SUCR) SUCR,\r\n");
		sbSql.append("       sum(j.ONCESUCC) ONCESUCC,\r\n");
		sbSql.append("       sum(j.ONCEFAILC) ONCEFAILC,\r\n");
		sbSql.append("       sum(j.ONCESUCR) ONCESUCR,\r\n");
		sbSql.append("       sum(j.SATISC) SATISC,\r\n");
		sbSql.append("       sum(j.NOSATISC) NOSATISC,\r\n");
		sbSql.append("       sum(j.SATISR) SATISR,\r\n");
		sbSql.append("       sum(j.ONCESATISC) ONCESATISC,\r\n");
		sbSql.append("       sum(j.ONCESATISR) ONCESATISR,\r\n");
		sbSql.append("		 sum(j.FAIL1) FAIL1,\r\n");
		sbSql.append("       sum(j.FAIL1R) FAIL1R,\r\n");
		sbSql.append("       sum(j.FAIL2) FAIL2,\r\n");
		sbSql.append("       sum(j.FAIL2R) FAIL2R,\r\n");
		sbSql.append("       sum(j.FAIL3) FAIL3,\r\n");
		sbSql.append("       sum(j.FAIL3R) FAIL3R,\r\n");
		sbSql.append("       sum(j.FAIL4) FAIL4,\r\n");
		sbSql.append("       sum(j.FAIL4R) FAIL4R,\r\n");
		sbSql.append("		 sum(j.FAIL5) FAIL5,\r\n");
		sbSql.append("       sum(j.FAIL5R) FAIL5R,\r\n");
		sbSql.append("		 sum(j.FAIL6) FAIL6,\r\n");
		sbSql.append("		 sum(j.FAIL6R) FAIL6R,\r\n");
		sbSql.append("		 sum(j.FAIL7) FAIL7,\r\n");
		sbSql.append("		 sum(j.FAIL7R) FAIL7R"); 
		sbSql.append("  from (select c.group_name MODELNAME,\r\n");
		sbSql.append("               count(*) TOTAL,\r\n");
		sbSql.append("               d.SUCC SUCC,\r\n");
		sbSql.append("               e.FAILC FAILC,\r\n");
		sbSql.append("               to_char(round(d.SUCC / count(*) * 100, 4)) SUCR,\r\n");
		sbSql.append("               f.ONCESUCC,\r\n");
		sbSql.append("               count(*) - f.ONCESUCC ONCEFAILC,\r\n");
		sbSql.append("               to_char(round(f.ONCESUCC / count(*) * 100, 4)) ONCESUCR,\r\n");
		sbSql.append("               g.SATISC SATISC,\r\n");
		sbSql.append("               h.NOSATISC NOSATISC,\r\n");
		sbSql.append("               to_char(round(g.SATISC / count(*) * 100, 4)) SATISR,\r\n");
		sbSql.append("               i.ONCESATISC ONCESATISC,\r\n");
		sbSql.append("               to_char(round(i.ONCESATISC / count(*) * 100, 4)) ONCESATISR,\r\n");
		sbSql.append("				 l.FAIL1 FAIL1,\r\n");
		sbSql.append("				 to_char(round(l.FAIL1 / e.FAILC * 100, 4)) FAIL1R,\r\n");
		sbSql.append("				 n.FAIL2 FAIL2,\r\n");
		sbSql.append("				 to_char(round(n.FAIL2 / e.FAILC * 100, 4)) FAIL2R,\r\n");
		sbSql.append("				 m.FAIL3 FAIL3,\r\n");
		sbSql.append("				 to_char(round(m.FAIL3 / e.FAILC * 100, 4)) FAIL3R,\r\n");
		sbSql.append("				 o.FAIL4 FAIL4,\r\n");
		sbSql.append("				 to_char(round(o.FAIL4 / e.FAILC * 100, 4)) FAIL4R,\r\n");
		sbSql.append("				 p.FAIL5 FAIL5,\r\n");
		sbSql.append("				 to_char(round(p.FAIL5 / e.FAILC * 100, 4)) FAIL5R,\r\n");
		sbSql.append("				 q.FAIL6 FAIL6,\r\n");
		sbSql.append("				 to_char(round(q.FAIL6 / e.FAILC * 100, 4)) FAIL6R,\r\n");
		sbSql.append("				 r.FAIL7 FAIL7,\r\n");
		sbSql.append("				 to_char(round(r.FAIL7 / e.FAILC * 100, 4)) FAIL7R"); 

		sbSql.append("          from tt_crm_return_visit a\r\n");
		sbSql.append("          left join tm_vehicle b\r\n");
		sbSql.append("            on a.vin = b.vin\r\n");
		sbSql.append("          left join tm_vhcl_material_group c\r\n");
		sbSql.append("            on b.model_id = c.group_id\r\n");
		sbSql.append("          left join (select b.model_id MODELID, count(*) SUCC\r\n");
		sbSql.append("                      from tt_crm_return_visit a\r\n");
		sbSql.append("                      left join tm_vehicle b\r\n");
		sbSql.append("                        on a.vin = b.vin\r\n");
		sbSql.append("                     where a.rv_status = 95081004 and a.rv_result = 95121001 \r\n");
		sbSql = appendSpecilWhere(sbSql, dealName);
		sbSql.append("\r\n");
		sbSql.append("                     group by b.model_id) d\r\n");
		sbSql.append("            on d.MODELID = b.model_id\r\n");
		sbSql.append("          left join (select b.model_id MODELID, count(*) FAILC\r\n");
		sbSql.append("                      from tt_crm_return_visit a\r\n");
		sbSql.append("                      left join tm_vehicle b\r\n");
		sbSql.append("                        on a.vin = b.vin\r\n");
		sbSql.append("                     where a.rv_status not in (95081004, 95081005, 95081006) and a.rv_result not in (95121001)  \r\n");
		sbSql.append("                     group by b.model_id) e\r\n");
		sbSql.append("            on e.MODELID = b.model_id\r\n");
		sbSql.append("          left join (select b.model_id MODELID, count(*) ONCESUCC\r\n");
		sbSql.append("                      from tt_crm_return_visit a\r\n");
		sbSql.append("                      left join tm_vehicle b\r\n");
		sbSql.append("                        on a.vin = b.vin\r\n");
		sbSql.append("                      left join (select a.rv_id, count(*) times\r\n");
		sbSql.append("                                  from tt_crm_return_visit_record a\r\n");
		sbSql.append("                                 group by a.rv_id) c\r\n");
		sbSql.append("                        on c.rv_id = a.rv_id\r\n");
		sbSql.append("                     where a.rv_status = 95081004 and a.rv_result = 95121001 \r\n");
		sbSql.append("                       and c.times = 1\r\n");
		sbSql = appendSpecilWhere(sbSql, dealName);
		sbSql.append("\r\n");
		sbSql.append("                     group by b.model_id) f\r\n");
		sbSql.append("            on f.MODELID = b.model_id\r\n");
		sbSql.append("          left join (select b.model_id MODELID, count(*) SATISC\r\n");
		sbSql.append("                      from tt_crm_return_visit a\r\n");
		sbSql.append("                      left join tm_vehicle b\r\n");
		sbSql.append("                        on a.vin = b.vin\r\n");
		sbSql.append("                     where a.rv_status = 95081004 and a.rv_result = 95121001 \r\n");
		sbSql.append("                       and a.rv_satisfaction = 95111001\r\n");
		sbSql.append("                     group by b.model_id) g\r\n");
		sbSql.append("            on g.MODELID = b.model_id\r\n");
		sbSql.append("          left join (select b.model_id MODELID, count(*) NOSATISC\r\n");
		sbSql.append("                      from tt_crm_return_visit a\r\n");
		sbSql.append("                      left join tm_vehicle b\r\n");
		sbSql.append("                        on a.vin = b.vin\r\n");
		sbSql.append("                     where a.rv_status = 95081004 and a.rv_result = 95121001 \r\n");
		sbSql.append("                       and a.rv_satisfaction = 95111003\r\n");
		sbSql = appendSpecilWhere(sbSql, dealName);
		sbSql.append("\r\n");
		sbSql.append("                     group by b.model_id) h\r\n");
		sbSql.append("            on h.MODELID = b.model_id\r\n");
		sbSql.append("          left join (select b.model_id MODELID, count(*) ONCESATISC\r\n");
		sbSql.append("                      from tt_crm_return_visit a\r\n");
		sbSql.append("                      left join tm_vehicle b\r\n");
		sbSql.append("                        on a.vin = b.vin\r\n");
		sbSql.append("                      left join (select a.rv_id, count(*) times\r\n");
		sbSql.append("                                  from tt_crm_return_visit_record a\r\n");
		sbSql.append("                                 group by a.rv_id) c\r\n");
		sbSql.append("                        on c.rv_id = a.rv_id\r\n");
		sbSql.append("                     where a.rv_status = 95081004 and a.rv_result = 95121001 \r\n");
		sbSql.append("                       and a.rv_satisfaction = 95111003\r\n");
		sbSql.append("                       and c.times = 1\r\n");
		sbSql = appendSpecilWhere(sbSql, dealName);
		sbSql.append("\r\n");
		sbSql.append("                     group by b.model_id) i\r\n");
		sbSql.append("            on i.MODELID = b.model_id\r\n");
		
		sbSql.append("\r\n");
		sbSql.append("left join (select b.model_id MODELID, count(*) FAIL1\r\n");
		sbSql.append("            from tt_crm_return_visit a\r\n");
		sbSql.append("            left join tm_vehicle b\r\n");
		sbSql.append("              on a.vin = b.vin\r\n");
		sbSql.append("           where a.rv_status = 95081004 and a.rv_result = 95121002\r\n");
		sbSql = appendSpecilWhere(sbSql, dealName);
		sbSql.append("           group by b.model_id) l\r\n");
		sbSql.append("  on l.MODELID = b.model_id\r\n");
		sbSql.append("\r\n");
		sbSql.append("  left join (select b.model_id MODELID, count(*) FAIL2\r\n");
		sbSql.append("            from tt_crm_return_visit a\r\n");
		sbSql.append("            left join tm_vehicle b\r\n");
		sbSql.append("              on a.vin = b.vin\r\n");
		sbSql.append("           where a.rv_status = 95081004 and a.rv_result = 95121003\r\n");
		sbSql = appendSpecilWhere(sbSql, dealName);
		sbSql.append("           group by b.model_id) n\r\n");
		sbSql.append("  on n.MODELID = b.model_id\r\n");
		sbSql.append("\r\n");
		sbSql.append("  left join (select b.model_id MODELID, count(*) FAIL3\r\n");
		sbSql.append("            from tt_crm_return_visit a\r\n");
		sbSql.append("            left join tm_vehicle b\r\n");
		sbSql.append("              on a.vin = b.vin\r\n");
		sbSql.append("           where a.rv_status = 95081004 and a.rv_result = 95121004\r\n");
		sbSql = appendSpecilWhere(sbSql, dealName);
		sbSql.append("           group by b.model_id) m\r\n");
		sbSql.append("  on m.MODELID = b.model_id\r\n");
		sbSql.append("\r\n");
		sbSql.append("  left join (select b.model_id MODELID, count(*) FAIL4\r\n");
		sbSql.append("            from tt_crm_return_visit a\r\n");
		sbSql.append("            left join tm_vehicle b\r\n");
		sbSql.append("              on a.vin = b.vin\r\n");
		sbSql.append("           where a.rv_status = 95081004 and a.rv_result = 95121005\r\n");
		sbSql = appendSpecilWhere(sbSql, dealName);
		sbSql.append("           group by b.model_id) o\r\n");
		sbSql.append("  on o.MODELID = b.model_id\r\n");
		sbSql.append("\r\n");
		sbSql.append("  left join (select b.model_id MODELID, count(*) FAIL5\r\n");
		sbSql.append("            from tt_crm_return_visit a\r\n");
		sbSql.append("            left join tm_vehicle b\r\n");
		sbSql.append("              on a.vin = b.vin\r\n");
		sbSql.append("           where a.rv_status = 95081004 and a.rv_result = 95121006\r\n");
		sbSql = appendSpecilWhere(sbSql, dealName);
		sbSql.append("           group by b.model_id) p\r\n");
		sbSql.append("  on p.MODELID = b.model_id\r\n");
		sbSql.append("\r\n");
		sbSql.append("  left join (select b.model_id MODELID, count(*) FAIL6\r\n");
		sbSql.append("            from tt_crm_return_visit a\r\n");
		sbSql.append("            left join tm_vehicle b\r\n");
		sbSql.append("              on a.vin = b.vin\r\n");
		sbSql.append("           where a.rv_status = 95081004 and a.rv_result = 95121007\r\n");
		sbSql = appendSpecilWhere(sbSql, dealName);
		sbSql.append("           group by b.model_id) q\r\n");
		sbSql.append("  on q.MODELID = b.model_id\r\n");
		sbSql.append("\r\n");
		sbSql.append("  left join (select b.model_id MODELID, count(*) FAIL7\r\n");
		sbSql.append("            from tt_crm_return_visit a\r\n");
		sbSql.append("            left join tm_vehicle b\r\n");
		sbSql.append("              on a.vin = b.vin\r\n");
		sbSql.append("           where a.rv_status = 95081004 and a.rv_result = 95121008\r\n");
		sbSql = appendSpecilWhere(sbSql, dealName);
		sbSql.append("           group by b.model_id) r\r\n");
		sbSql.append("  on r.MODELID = b.model_id\r\n");

		
		sbSql.append("			inner join (select  distinct a.rv_id\r\n");
		sbSql.append("         		 		from tt_crm_return_visit_record a\r\n");
		sbSql.append("            	 		where 1=1\r\n");
		sbSql = appendWhere(sbSql, dateStart, dateEnd);
		sbSql.append("         	) k\r\n");
		sbSql.append("			on k.rv_id = a.rv_id \r\n"); 
		sbSql.append("         where 1 = 1\r\n");
		sbSql = appendSpecilWhere(sbSql, dealName);
		sbSql.append("\r\n");
		sbSql.append("         group by c.group_name,\r\n");
		sbSql.append("                  d.SUCC,\r\n");
		sbSql.append("                  e.FAILC,\r\n");
		sbSql.append("                  f.ONCESUCC,\r\n");
		sbSql.append("                  g.SATISC,\r\n");
		sbSql.append("                  h.NOSATISC,\r\n");
		sbSql.append("                  i.ONCESATISC,\r\n");
		sbSql.append("					l.FAIL1,\r\n");
		sbSql.append("                  n.FAIL2,\r\n");
		sbSql.append("                  m.FAIL3,\r\n");
		sbSql.append("                  o.FAIL4,\r\n");
		sbSql.append("                  p.FAIL5,\r\n");
		sbSql.append("                  q.FAIL6,\r\n");
		sbSql.append("                  r.FAIL7"); 
		sbSql.append(" ) j\r\n ");
		sbSql.append(" group by rollup(j.MODELNAME)"); 

		return sbSql.toString();
	}

	private StringBuffer appendSpecilWhere(StringBuffer sbSql,String dealName){
		if(StringUtil.notNull(dealName)){
			sbSql.append(" and a.RV_ASS_USER like '%"+dealName+"%' \r\n");
		}

		return sbSql;
	}
	
	private StringBuffer appendWhere(StringBuffer sbSql,
			String dateStart,String dateEnd){

		if(StringUtil.notNull(dateStart)){
			sbSql.append(" and a.RD_DATE >= to_date('"+dateStart+"','yyyy-MM-dd') \r\n");
		}
		if(StringUtil.notNull(dateEnd)){
			sbSql.append(" and a.RD_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n");
		}

		return sbSql;
	}

}
