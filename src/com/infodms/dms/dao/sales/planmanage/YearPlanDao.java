package com.infodms.dms.dao.sales.planmanage;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmpVsYearPlanNewPO;
import com.infodms.dms.po.TmpVsYearlyPlanPO;
import com.infodms.dms.po.TtVsYearlyPlanDetailPO;
import com.infodms.dms.po.TtVsYearlyPlanPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class YearPlanDao
  extends BaseDao
{
  public static Logger logger = Logger.getLogger(YearPlanDao.class);
  private static final YearPlanDao dao = new YearPlanDao();
  
  public static final YearPlanDao getInstance()
  {
    return dao;
  }
  
  public int clrTable(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String planType = map.get("planType").toString();
    String orgType = map.get("orgType").toString();
    String areaId = map.get("areaId").toString();
    String companyId = map.get("companyId").toString();
    StringBuffer sql = new StringBuffer();
    sql.append("delete from TT_VS_YEARLY_PLAN p\n");
    sql.append(" where p.PLAN_YEAR = " + year + "\n");
    sql.append("   and p.COMPANY_ID = " + companyId + "\n");
    sql.append("   and p.ORG_TYPE = " + orgType + "\n");
    sql.append("   and p.AREA_ID = " + areaId + "\n");
    sql.append("   and p.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM + "\n");
    sql.append("   and p.PLAN_TYPE = " + planType + "\n");
    return dao.update(sql.toString(), null);
  }
  
  public int clrDetailTable(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String planType = map.get("planType").toString();
    String orgType = map.get("orgType").toString();
    String areaId = map.get("areaId").toString();
    String companyId = map.get("companyId").toString();
    StringBuffer sql = new StringBuffer();
    
    sql.append("delete TT_VS_YEARLY_PLAN_DETAIL cascade\n");
    sql.append(" where not exists (select 1\n");
    sql.append("   from TT_VS_YEARLY_PLAN p\n");
    sql.append(" where \n");
    
    sql.append("   p.PLAN_ID = PLAN_ID  ) \n");
    
    return dao.update(sql.toString(), null);
  }
  
  public int sbuClrTable(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String planType = map.get("planType").toString();
    String orgType = map.get("orgType").toString();
    String areaId = map.get("areaId").toString();
    String orgId = map.get("orgId").toString();
    String companyId = map.get("companyId").toString();
    StringBuffer sql = new StringBuffer();
    
    sql.append("delete TT_VS_YEARLY_PLAN p\n");
    sql.append(" where p.PLAN_YEAR = " + year + "\n");
    sql.append("   and p.COMPANY_ID = " + companyId + "\n");
    sql.append("   and p.ORG_TYPE = " + orgType + "\n");
    sql.append("   and p.AREA_ID = " + areaId + "\n");
    sql.append("   and p.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM + "\n");
    sql.append("   and p.PLAN_TYPE = " + planType + "\n");
    sql.append("   and p.DEALER_ID in\n");
    sql.append("       (select d.DEALER_ID\n");
    sql.append("          from tm_dealer d\n");
    sql.append("         start with d.DEALER_ID in\n");
    sql.append("                    (select td.DEALER_ID\n");
    sql.append("                       from TM_DEALER td, TM_DEALER_ORG_RELATION r\n");
    sql.append("                      where td.dealer_id = r.dealer_id\n");
    sql.append("                        and td.OEM_COMPANY_ID = p.COMPANY_ID\n");
    sql.append("                        and td.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("                        and r.ORG_ID = " + orgId + ")\n");
    sql.append("connect by prior d.DEALER_ID = d.PARENT_DEALER_D\n");
    sql.append("        )");
    
    return dao.update(sql.toString(), null);
  }
  
  public int sbuClrDetailTable(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String planType = map.get("planType").toString();
    String orgType = map.get("orgType").toString();
    String areaId = map.get("areaId").toString();
    String companyId = map.get("companyId").toString();
    StringBuffer sql = new StringBuffer();
    
    sql.append("delete TT_VS_YEARLY_PLAN_DETAIL cascade\n");
    sql.append(" where not exists (select 1\n");
    sql.append("   from TT_VS_YEARLY_PLAN p\n");
    sql.append(" where \n");
    
    sql.append("   p.PLAN_ID = PLAN_ID  ) \n");
    
    return dao.update(sql.toString(), null);
  }
  
  public int updateForConfrim(Map<String, Object> map, TtVsYearlyPlanPO po)
  {
    String year = map.get("year").toString();
    String planType = map.get("planType").toString();
    String areaId = map.get("areaId").toString();
    String orgId = map.get("orgId").toString();
    String companyId = map.get("companyId").toString();
    
    StringBuffer sql = new StringBuffer();
    
    sql.append("update TT_VS_YEARLY_PLAN p\n");
    sql.append("   set p.PLAN_VER = " + po.getPlanVer() + ", p.PLAN_DESC = '" + po.getPlanDesc() + "', p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + ",p.UPDATE_BY=" + po.getUpdateBy() + ",p.UPDATE_DATE=sysdate\n");
    sql.append(" where p.PLAN_TYPE = " + planType + "\n");
    sql.append("   and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
    sql.append("   and p.PLAN_YEAR = " + year + "\n");
    sql.append("   and p.COMPANY_ID = " + companyId + "\n");
    sql.append("   and p.AREA_ID = " + areaId);
    sql.append("   and p.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM + "\n");
    sql.append("   and p.DEALER_ID in\n");
    sql.append("      (select d.DEALER_ID\n");
    sql.append("         from tm_dealer d\n");
    sql.append("        start with d.DEALER_ID in\n");
    sql.append("                   (select td.DEALER_ID\n");
    sql.append("                      from TM_DEALER td, TM_DEALER_ORG_RELATION r\n");
    sql.append("                     where td.dealer_id = r.dealer_id\n");
    sql.append("                       and td.OEM_COMPANY_ID = " + companyId + "\n");
    sql.append("                       and td.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("                       and r.ORG_ID = " + orgId + ")\n");
    sql.append("   connect by prior d.DEALER_ID = d.PARENT_DEALER_D)\n");
    
    return dao.update(sql.toString(), null);
  }
  
  public int insertPlan(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    // String areaId = map.get("areaId").toString();
    String companyId = map.get("companyId").toString();
  //   String planType = map.get("planType").toString();
    StringBuffer sql = new StringBuffer();
    sql.append("merge into TT_VS_YEARLY_PLAN tp\n");
    sql.append("using (select F_GETID() PLAN_ID,\n");
    sql.append("              " + companyId + " COMPANY_ID,\n");
    // sql.append("              " + areaId + " AREA_ID,\n");
    sql.append("              aa.PLAN_YEAR,\n");
    sql.append("              " + Constant.ORG_TYPE_DEALER + " ORG_TYPE,\n");
    sql.append("              aa.DEALER_ID,\n");
    sql.append("             null PLAN_TYPE,\n");
    sql.append("              " + Constant.PLAN_MANAGE_UNCONFIRM + " STATUS,\n");
    sql.append("              0 VER,\n");
    sql.append("              " + userId + " CREATE_BY,\n");
    sql.append("              sysdate CREATE_DATE\n");
    sql.append("         from (select distinct p.PLAN_YEAR,org.DEALER_ID\n");
    sql.append("                 from TMP_VS_YEAR_PLAN_NEW p,TM_DEALER org\n");
    sql.append("                where p.DEALER_CODE=org.DEALER_CODE and p.PLAN_YEAR = " + year + "\n");
    sql.append("                  ) aa) a\n");
    sql.append("on (tp.COMPANY_ID = a.COMPANY_ID  and tp.PLAN_YEAR = a.PLAN_YEAR and tp.DEALER_ID = a.DEALER_ID  and tp.STATUS = a.STATUS)\n");
    sql.append("when not matched then\n");
    sql.append("  insert\n");
    sql.append("    (PLAN_ID,\n");
    sql.append("     COMPANY_ID,\n");
    // sql.append("     AREA_ID,\n");
    sql.append("     PLAN_YEAR,\n");
    sql.append("     ORG_TYPE,\n");
    sql.append("     DEALER_ID,\n");
    sql.append("     STATUS,\n");
    sql.append("     VER,\n");
    sql.append("     PLAN_TYPE,\n");
    sql.append("     CREATE_BY,\n");
    sql.append("     CREATE_DATE)\n");
    sql.append("  values\n");
    sql.append("    (a.PLAN_ID,\n");
    sql.append("     a.COMPANY_ID,\n");
    // sql.append("     a.AREA_ID,\n");
    sql.append("     a.PLAN_YEAR,\n");
    sql.append("     a.ORG_TYPE,\n");
    sql.append("     a.DEALER_ID,\n");
    sql.append("     a.STATUS,\n");
    sql.append("     a.VER,\n");
    sql.append("     a.PLAN_TYPE,\n");
    sql.append("     a.CREATE_BY,\n");
    sql.append("     a.CREATE_DATE)");
    return update(sql.toString(), null);
  }
  
  public int insertPlanDetail(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    String dealerSql = map.get("dealerSql").toString();
    String companyId = map.get("companyId").toString();
    StringBuffer sql = new StringBuffer();
    sql.append("MERGE into TT_VS_YEARLY_PLAN_DETAIL a\n");
    sql.append("using (select distinct tpp.PLAN_ID, tvmg.GROUP_ID, tmpp.SUM_AMT\n");
    sql.append("        from TT_VS_YEARLY_PLAN  tpp,\n");
    sql.append("             TMP_VS_YEAR_PLAN_NEW tmpp,\n");
    sql.append("             TM_DEALER                 TD,\n");
    sql.append("             TM_VHCL_MATERIAL_GROUP tvmg\n");
    sql.append("       where tvmg.GROUP_CODE = tmpp.GROUP_CODE\n");
    sql.append("         and tmpp.GROUP_CODE = tvmg.GROUP_CODE\n");
    sql.append("         and tpp.DEALER_ID=TD.DEALER_ID\n");
    sql.append("         and tmpp.DEALER_CODE=TD.DEALER_CODE\n");
    sql.append("         and tpp.PLAN_YEAR = tmpp.PLAN_YEAR\n");
    sql.append("         and tpp.COMPANY_ID = tvmg.COMPANY_ID");
    sql.append("\t\t and tmpp.plan_year = " + year + "\n");
    sql.append("          and tpp.COMPANY_ID =" + companyId + "\n");
    sql.append("          and exists(" + dealerSql + " and v.DEALER_ID=tpp.dealer_id) \n");
   // sql.append("          and tpp.AREA_ID =" + areaId + "\n");
    sql.append("          and tpp.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM + "\n");
    sql.append("          and tmpp.USER_ID =" + userId + ") c\n");
    sql.append("on (a.PLAN_ID = c.PLAN_ID and a.MATERIAL_GROUPID = c.GROUP_ID)\n");
    sql.append("when MATCHED then\n");
    sql.append("  update\n");
    sql.append("     set a.SALE_AMOUNT = to_number(c.SUM_AMT),\n");
    sql.append("         a.UPDATE_BY   = " + userId + ",\n");
    sql.append("         a.UPDATE_DATE = sysdate\n");
    sql.append("when not matched then\n");
    sql.append("  INSERT\n");
    sql.append("    (a.DETAIL_ID,\n");
    sql.append("     a.PLAN_ID,\n");
    sql.append("     a.MATERIAL_GROUPID,\n");
    sql.append("     a.SALE_AMOUNT,\n");
    sql.append("     a.CREATE_BY,\n");
    sql.append("     a.CREATE_DATE)\n");
    sql.append("  values\n");
    sql.append("    (F_GETID(), c.PLAN_ID, c.GROUP_ID, c.SUM_AMT, " + userId + ", sysdate)");
    return update(sql.toString(), null);
  }
  
  public List<Map<String, Object>> unConfirmSelectSearch(Map<String, Object> map)
  {
    String companyId = map.get("companyId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select DISTINCT p.PLAN_YEAR || ',' || p.AREA_ID || ',' || p.PLAN_TYPE keyStr,\n");
    sql.append("                p.PLAN_YEAR || '-' || a.AREA_NAME || '-' || c.CODE_DESC valStr\n");
    sql.append("  from TT_VS_YEARLY_PLAN p, TM_BUSINESS_AREA a, TC_CODE c\n");
    sql.append(" where p.AREA_ID = a.AREA_ID\n");
    sql.append("   and p.PLAN_TYPE = c.CODE_ID\n");
    sql.append("   and p.COMPANY_ID = " + companyId + "\n");
    sql.append("   and p.ORG_TYPE = " + Constant.ORG_TYPE_OEM + "\n");
    sql.append("   and p.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM + "\n");
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> oemYearPlanCheckOrg(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    String companyId = map.get("companyId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select p.ROW_NUMBER\n");
    sql.append("  from TMP_VS_YEARLY_PLAN p\n");
    sql.append(" where 1 = 1\n");
    sql.append("   and p.PLAN_YEAR = " + year + "\n");
    sql.append("   and p.USER_ID =" + userId + "\n");
    sql.append("   and p.DEALER_CODE is null\n");
    sql.append("   and not exists (select 1\n");
    sql.append("          from tm_org org\n");
    sql.append("         where org.ORG_CODE = p.ORG_CODE\n");
    sql.append("           and org.COMPANY_ID = " + companyId + "\n");
    sql.append("           and org.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("           and org.ORG_TYPE = " + Constant.ORG_TYPE_OEM + "\n");
    sql.append("           and org.DUTY_TYPE = " + Constant.DUTY_TYPE_LARGEREGION + ")\n");
    sql.append("   order by to_number(p.ROW_NUMBER) asc");
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> oemYearPlanCheckOrgArea(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    String companyId = map.get("companyId").toString();
    String areaId = map.get("areaId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select p.ROW_NUMBER\n");
    sql.append("  from TMP_VS_YEARLY_PLAN p\n");
    sql.append(" where 1 = 1\n");
    sql.append("   and p.PLAN_YEAR = " + year + "\n");
    sql.append("   and p.USER_ID =" + userId + "\n");
    sql.append("   and p.DEALER_CODE is null\n");
    sql.append("   and not exists (select 1\n");
    sql.append("        from tm_org org, TM_ORG_BUSINESS_AREA area\n");
    sql.append("        where org.ORG_CODE = p.ORG_CODE\n");
    sql.append("           and org.ORG_ID = area.ORG_ID\n");
    sql.append("           and org.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("           and area.AREA_ID = " + areaId + "\n");
    sql.append("           and org.COMPANY_ID = " + companyId + "\n");
    sql.append("           and org.ORG_TYPE = " + Constant.ORG_TYPE_OEM + "\n");
    sql.append("           and org.DUTY_TYPE = " + Constant.DUTY_TYPE_LARGEREGION + ")\n");
    sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> oemYearPlanCheckGroup(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    String companyId = map.get("companyId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select p.ROW_NUMBER\n");
    sql.append("  from TMP_VS_YEARLY_PLAN p\n");
    sql.append(" where 1 = 1\n");
    sql.append("   and p.PLAN_YEAR = " + year + "\n");
    sql.append("   and p.USER_ID =" + userId + "\n");
    sql.append("   and p.DEALER_CODE is null\n");
    sql.append("   and not exists (select 1\n");
    sql.append("       from TM_VHCL_MATERIAL_GROUP g\n");
    sql.append("      where g.GROUP_CODE = p.GROUP_CODE\n");
    sql.append("        and g.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("        and g.COMPANY_ID = " + companyId + ")\n");
    sql.append("   order by to_number(p.ROW_NUMBER) asc");
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> oemYearPlanCheckGroupArea(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    String groupArea = map.get("groupArea").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select p.ROW_NUMBER\n");
    sql.append("  from TMP_VS_YEARLY_PLAN p\n");
    sql.append(" where 1 = 1\n");
    sql.append("   and p.PLAN_YEAR = " + year + "\n");
    sql.append("   and p.USER_ID = " + userId + "\n");
    sql.append("   and p.DEALER_CODE is null\n");
    sql.append("   and p.GROUP_CODE not in(" + PlanUtil.createSqlStr(groupArea) + ")\n");
    sql.append(" order by TO_NUMBER(p.ROW_NUMBER) asc");
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> sbuYearPlanCheckOrg(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    String orgId = map.get("orgId").toString();
    String companyId = map.get("companyId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select p.ROW_NUMBER\n");
    sql.append("  from TMP_VS_YEARLY_PLAN p\n");
    sql.append(" where 1 = 1\n");
    sql.append("   and p.PLAN_YEAR = " + year + "\n");
    sql.append("   and p.USER_ID =" + userId + "\n");
    sql.append("   and p.DEALER_CODE is not null\n");
    
    sql.append("and p.DEALER_CODE not in\n");
    sql.append("      (select d.DEALER_CODE\n");
    sql.append("         from tm_dealer d\n");
    sql.append("        start with d.DEALER_ID in\n");
    sql.append("                   (select td.DEALER_ID\n");
    sql.append("                      from TM_DEALER td, TM_DEALER_ORG_RELATION r\n");
    sql.append("                     where td.dealer_id = r.dealer_id\n");
    sql.append("                       and td.OEM_COMPANY_ID = " + companyId + "\n");
    sql.append("                       and td.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("                       and r.ORG_ID = " + orgId + ")\n");
    sql.append("connect by prior d.DEALER_ID = d.PARENT_DEALER_D\n");
    sql.append("        )");
    sql.append("   order by to_number(p.ROW_NUMBER) asc");
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> sbuYearPlanCheckDlrArea(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    String companyId = map.get("companyId").toString();
    String areaId = map.get("areaId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select p.ROW_NUMBER\n");
    sql.append("  from TMP_VS_YEARLY_PLAN p\n");
    sql.append(" where 1 = 1\n");
    sql.append("   and p.PLAN_YEAR = " + year + "\n");
    sql.append("   and p.USER_ID =" + userId + "\n");
    sql.append("   and p.DEALER_CODE is not null\n");
    sql.append("   and p.ORG_CODE is null\n");
    sql.append("   and not exists (select 1\n");
    sql.append("        from TM_DEALER td, TM_DEALER_BUSINESS_AREA area\n");
    sql.append("        where td.DEALER_CODE = p.DEALER_CODE\n");
    sql.append("           and td.DEALER_ID = area.DEALER_ID\n");
    sql.append("           and td.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("           and area.AREA_ID = " + areaId + "\n");
    sql.append("           and td.OEM_COMPANY_ID = " + companyId + "\n");
    sql.append("           )\n");
    
    sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> sbuYearPlanCheckGroup(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    String companyId = map.get("companyId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select p.ROW_NUMBER\n");
    sql.append("  from TMP_VS_YEARLY_PLAN p\n");
    sql.append(" where 1 = 1\n");
    sql.append("   and p.PLAN_YEAR = " + year + "\n");
    sql.append("   and p.USER_ID =" + userId + "\n");
    sql.append("   and p.DEALER_CODE is null\n");
    sql.append("   and not exists (select 1\n");
    sql.append("       from TM_VHCL_MATERIAL_GROUP g\n");
    sql.append("      where g.GROUP_CODE = p.GROUP_CODE\n");
    sql.append("        and g.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("        and g.COMPANY_ID = " + companyId + ")\n");
    sql.append("   order by to_number(p.ROW_NUMBER) asc");
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> oemTalbeCheckDump(String year, String userId)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select p1.ROW_NUMBER ROW_NUMBER1, p2.ROW_NUMBER ROW_NUMBER2\n");
    sql.append("  from TMP_VS_YEARLY_PLAN p1, TMP_VS_YEARLY_PLAN p2\n");
    sql.append(" where p1.org_CODE = p2.org_CODE\n");
    sql.append("   and p1.GROUP_CODE = p2.GROUP_CODE\n");
    sql.append("   and p1.ROW_NUMBER <> p2.ROW_NUMBER\n");
    sql.append("   and p1.PLAN_YEAR = p2.PLAN_YEAR\n");
    sql.append("   and p1.USER_ID = p2.USER_ID\n");
    sql.append("   and p1.ORG_CODE is not null\n");
    sql.append("   and p1.PLAN_YEAR = ?\n");
    params.add(year);
    sql.append("   and p1.USER_ID=?");
    params.add(userId);
    
    List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
    
    return list;
  }
  
  public List<Map<String, Object>> sbuTalbeCheckDump(String year, String userId)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select p1.ROW_NUMBER ROW_NUMBER1, p2.ROW_NUMBER ROW_NUMBER2\n");
    sql.append("  from TMP_VS_YEARLY_PLAN p1, TMP_VS_YEARLY_PLAN p2\n");
    sql.append(" where p1.DEALER_CODE = p2.DEALER_CODE\n");
    sql.append("   and p1.GROUP_CODE = p2.GROUP_CODE\n");
    sql.append("   and p1.ROW_NUMBER <> p2.ROW_NUMBER\n");
    sql.append("   and p1.PLAN_YEAR = p2.PLAN_YEAR\n");
    sql.append("   and p1.USER_ID = p2.USER_ID\n");
    sql.append("   and p1.ORG_CODE is  null\n");
    sql.append("   and p1.DEALER_CODE is not null\n");
    sql.append("   and p1.PLAN_YEAR = ?\n");
    params.add(year);
    sql.append("   and p1.USER_ID=?");
    params.add(userId);
    
    List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
    
    return list;
  }
  
  public List<Map<String, Object>> deptTalbeCheckGroupArea(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select tp.ROW_NUMBER\n");
    sql.append("  from TMP_VS_YEARLY_PLAN tp\n");
    sql.append(" where tp.PLAN_YEAR = " + year + "\n");
    sql.append("   and tp.USER_ID = " + userId + "\n");
    sql.append("   and tp.ORG_CODE is not null\n");
    sql.append("   and tp.DEALER_CODE is  null\n");
    sql.append("   and not\n");
    sql.append("        exists((SELECT 1\n");
    sql.append("                  from TM_VHCL_MATERIAL_GROUP TVMG\n");
    sql.append("                 WHERE TVMG.GROUP_LEVEL = 2\n");
    sql.append("                   AND TVMG.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("                   and tvmg.GROUP_CODE = tp.GROUP_CODE\n");
    sql.append("                 START WITH TVMG.GROUP_ID IN\n");
    sql.append("                            (SELECT TAG.MATERIAL_GROUP_ID\n");
    sql.append("                               FROM TM_AREA_GROUP TAG\n");
    sql.append("                              WHERE TAG.AREA_ID in\n");
    sql.append("                                    (select area_id\n");
    sql.append("                                       from TM_ORG_BUSINESS_AREA area,\n");
    sql.append("                                            TM_ORG               org\n");
    sql.append("                                      where org.ORG_ID = area.ORG_ID\n");
    sql.append("                                        and org.ORG_CODE = tp.ORG_CODE))\n");
    sql.append("                CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID)\n");
    sql.append("                union\n");
    sql.append("               (SELECT 1\n");
    sql.append("                  from TM_VHCL_MATERIAL_GROUP TVMG\n");
    sql.append("                 WHERE TVMG.GROUP_LEVEL = 2\n");
    sql.append("                   AND TVMG.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("                   and tvmg.GROUP_CODE = tp.GROUP_CODE\n");
    sql.append("                 START WITH TVMG.GROUP_ID IN\n");
    sql.append("                            (SELECT TAG.MATERIAL_GROUP_ID\n");
    sql.append("                               FROM TM_AREA_GROUP TAG\n");
    sql.append("                              WHERE TAG.AREA_ID in\n");
    sql.append("                                    (select area_id\n");
    sql.append("                                       from TM_ORG_BUSINESS_AREA area,\n");
    sql.append("                                            TM_ORG               org\n");
    sql.append("                                      where org.ORG_ID = area.ORG_ID\n");
    sql.append("                                        and org.ORG_CODE = tp.ORG_CODE))\n");
    sql.append("                CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID)\n");
    sql.append("               )\n");
    sql.append("order by to_number(tp.ROW_NUMBER)");
    
    List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
    
    return list;
  }
  
  public List<Map<String, Object>> sbuTalbeCheckGroupArea(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select tp.ROW_NUMBER\n");
    sql.append("  from TMP_VS_YEARLY_PLAN tp\n");
    sql.append(" where tp.PLAN_YEAR = " + year + "\n");
    sql.append("   and tp.USER_ID = " + userId + "\n");
    sql.append("   and tp.ORG_CODE is null\n");
    sql.append("   and tp.DEALER_CODE is not null\n");
    sql.append("   and not\n");
    sql.append("        exists((SELECT 1\n");
    sql.append("                  from TM_VHCL_MATERIAL_GROUP TVMG\n");
    sql.append("                 WHERE TVMG.GROUP_LEVEL = 2\n");
    sql.append("                   AND TVMG.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("                   and tvmg.GROUP_CODE = tp.GROUP_CODE\n");
    sql.append("                 START WITH TVMG.GROUP_ID IN\n");
    sql.append("                            (SELECT TAG.MATERIAL_GROUP_ID\n");
    sql.append("                               FROM TM_AREA_GROUP TAG\n");
    sql.append("                              WHERE TAG.AREA_ID in\n");
    sql.append("                                    (select area_id\n");
    sql.append("                                       from TM_DEALER_BUSINESS_AREA area,\n");
    sql.append("                                            TM_DEALER               td\n");
    sql.append("                                      where td.DEALER_ID = area.DEALER_ID\n");
    sql.append("                                        and td.DEALER_CODE = tp.DEALER_CODE))\n");
    sql.append("                CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID)\n");
    sql.append("                union\n");
    sql.append("               (SELECT 1\n");
    sql.append("                  from TM_VHCL_MATERIAL_GROUP TVMG\n");
    sql.append("                 WHERE TVMG.GROUP_LEVEL = 2\n");
    sql.append("                   AND TVMG.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("                   and tvmg.GROUP_CODE = tp.GROUP_CODE\n");
    sql.append("                 START WITH TVMG.GROUP_ID IN\n");
    sql.append("                            (SELECT TAG.MATERIAL_GROUP_ID\n");
    sql.append("                               FROM TM_AREA_GROUP TAG\n");
    sql.append("                              WHERE TAG.AREA_ID in\n");
    sql.append("                                    (select area_id\n");
    sql.append("                                       from TM_DEALER_BUSINESS_AREA area,\n");
    sql.append("                                            TM_DEALER               td\n");
    sql.append("                                      where td.DEALER_ID = area.DEALER_ID\n");
    sql.append("                                        and td.DEALER_CODE = tp.DEALER_CODE))\n");
    sql.append("                CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID)\n");
    sql.append("               )\n");
    sql.append("order by to_number(tp.ROW_NUMBER)");
    
    List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
    
    return list;
  }
  
  public void deleteTmpYearPlan(String year, Long userId)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE TMP_VS_YEARLY_PLAN TMP WHERE TMP.PLAN_YEAR = ?\n");
    params.add(year);
    sql.append(" AND USER_ID=?");
    params.add(userId);
    logger.info("����������" + sql.toString());
    dao.delete(sql.toString(), params);
  }
  
  public void insertTmpYearPlan(TmpVsYearlyPlanPO po)
  {
    dao.insert(po);
    logger.info("insert tmp_yearly_plan success!");
  }
  
  public boolean selectSingleDealer(Long orgId, String dealerCode)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    
    sql.append("select td.DEALER_ID, oa.ORG_ID, td.DEALER_CODE, da.AREA_ID, oa.AREA_ID\n");
    sql.append("  from TM_DEALER_BUSINESS_AREA da, TM_DEALER td, TM_ORG_BUSINESS_AREA oa\n");
    sql.append(" where da.DEALER_ID = td.DEALER_ID\n");
    sql.append("   and da.AREA_ID = oa.AREA_ID\n");
    sql.append("   and oa.ORG_ID = ?\n");
    params.add(orgId);
    sql.append("   and td.dealer_code = ?");
    params.add(dealerCode);
    sql.append("and td.DEALER_ID in\n");
    sql.append("       (select r.DEALER_ID from TM_DEALER_ORG_RELATION r where r.ORG_ID = ?)");
    params.add(orgId);
    
    List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
    boolean isExists = true;
    if ((list == null) || (list.size() == 0)) {
      isExists = false;
    }
    return isExists;
  }
  
  public boolean selectDealerOrg(Long orgId, String dealerCode)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    
    sql.append("select *\n");
    sql.append("  from TM_dealer td, TM_DEALER_ORG_RELATION r, tm_org org\n");
    sql.append(" where td.DEALER_ID = r.DEALER_ID\n");
    sql.append("   and org.ORG_ID = r.ORG_ID\n");
    sql.append("   and org.ORG_ID = ?\n");
    params.add(orgId);
    sql.append("   and td.DEALER_CODE = ?\n");
    params.add(dealerCode);
    
    List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
    boolean isExists = true;
    if ((list == null) || (list.size() == 0)) {
      isExists = false;
    }
    return isExists;
  }
  
  public boolean selectSingleOrg(TmOrgPO po)
  {
    List<TmOrgPO> list = dao.select(po);
    boolean isExists = true;
    if ((list == null) || (list.size() == 0)) {
      isExists = false;
    }
    return isExists;
  }
  
  public boolean selectOrgArea(Long orgId, String orgCode)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    
    sql.append("select *\n");
    sql.append("  from TM_ORG org, TM_ORG_BUSINESS_AREA oa1, TM_ORG_BUSINESS_AREA oa2\n");
    sql.append(" where org.ORG_ID = oa1.ORG_ID\n");
    sql.append("   and oa1.AREA_ID = oa2.ORG_ID\n");
    sql.append("   and oa2.ORG_ID = ?\n");
    params.add(orgId);
    sql.append("   and org.ORG_CODE = ?");
    params.add(orgCode);
    
    List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
    boolean isExists = true;
    if ((list == null) || (list.size() == 0)) {
      isExists = false;
    }
    return isExists;
  }
  
  public List<TmVhclMaterialGroupPO> selectSingleMaterialGroup(TmVhclMaterialGroupPO po)
  {
    return dao.select(po);
  }
  
  public void clearUserYearlyPlan(TtVsYearlyPlanPO po)
  {
    TtVsYearlyPlanPO cPo = new TtVsYearlyPlanPO();
    cPo.setPlanId(po.getPlanId());
    dao.delete(cPo);
  }
  
  public List<TmpVsYearPlanNewPO> selectTmpYearlyPlanNew(TmpVsYearPlanNewPO po)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    sql.append("SELECT *\n");
    sql.append("  FROM TMP_VS_YEAR_PLAN_NEW\n");
    sql.append(" WHERE 1 = 1\n");
    sql.append("   AND User_Id = ?\n");
    params.add(po.getUserId());
    
    sql.append(" order by row_number");
    return dao.select(TmpVsYearPlanNewPO.class, sql.toString(), params);
  }
  
  public List<TmpVsYearlyPlanPO> selectTmpYearlyPlan(TmpVsYearlyPlanPO po)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    sql.append("SELECT *\n");
    sql.append("  FROM Tmp_Vs_Yearly_Plan\n");
    sql.append(" WHERE 1 = 1\n");
    sql.append("   AND User_Id = ?\n");
    params.add(po.getUserId());
    
    sql.append(" order by row_number");
    return dao.select(TmpVsYearlyPlanPO.class, sql.toString(), params);
  }
  
  public List<Map<String, Object>> selectTmpYearPlan(String year, Long userId)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    sql.append("SELECT TD.DEALER_SHORTNAME,\n");
    sql.append("       TVMG.GROUP_NAME,\n");
    sql.append("       TMP.ROW_NUMBER,\n");
    sql.append("       TMP.SUM_AMT,\n");
    sql.append("       TMP.JAN_AMT,\n");
    sql.append("       TMP.FEB_AMT,\n");
    sql.append("       TMP.MAR_AMT,\n");
    sql.append("       TMP.APR_AMT,\n");
    sql.append("       TMP.MAY_AMOUNT,\n");
    sql.append("       TMP.JUN_AMT,\n");
    sql.append("       TMP.JUL_AMT,\n");
    sql.append("       TMP.AUG_AMT,\n");
    sql.append("       TMP.SEP_AMT,\n");
    sql.append("       TMP.OCT_AMT,\n");
    sql.append("       TMP.NOV_AMT,\n");
    sql.append("       TMP.DEC_AMT\n");
    sql.append("  FROM TMP_VS_YEARLY_PLAN TMP, TM_DEALER TD, TM_VHCL_MATERIAL_GROUP TVMG\n");
    sql.append(" WHERE TMP.DEALER_CODE = TD.DEALER_CODE\n");
    sql.append("   AND TVMG.GROUP_CODE = TMP.GROUP_CODE\n");
    sql.append("   AND tmp.PLAN_YEAR=?\n");
    params.add(year);
    sql.append("   AND tmp.USER_ID=?");
    params.add(userId);
    
    logger.info(sql.toString());
    
    return dao.pageQuery(sql.toString(), params, "com.infodms.dms.dao.sales.planmanage.selectTmpYearPlan");
  }
  
  public List<Map<String, Object>> oemSelectTmpYearPlan(String year, Long userId)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    sql.append("SELECT ORG.ORG_NAME,\n");
    sql.append("       TVMG.GROUP_NAME,\n");
    sql.append("       TMP.ROW_NUMBER,\n");
    sql.append("       TMP.SUM_AMT,\n");
    sql.append("       TMP.JAN_AMT,\n");
    sql.append("       TMP.FEB_AMT,\n");
    sql.append("       TMP.MAR_AMT,\n");
    sql.append("       TMP.APR_AMT,\n");
    sql.append("       TMP.MAY_AMOUNT,\n");
    sql.append("       TMP.JUN_AMT,\n");
    sql.append("       TMP.JUL_AMT,\n");
    sql.append("       TMP.AUG_AMT,\n");
    sql.append("       TMP.SEP_AMT,\n");
    sql.append("       TMP.OCT_AMT,\n");
    sql.append("       TMP.NOV_AMT,\n");
    sql.append("       TMP.DEC_AMT\n");
    sql.append("  FROM TMP_VS_YEARLY_PLAN TMP, TM_ORG ORG, TM_VHCL_MATERIAL_GROUP TVMG\n");
    sql.append(" WHERE TMP.ORG_CODE = ORG.ORG_CODE\n");
    sql.append("   AND TVMG.GROUP_CODE = TMP.GROUP_CODE\n");
    sql.append("   AND tmp.PLAN_YEAR=?\n");
    params.add(year);
    sql.append("   AND tmp.USER_ID=?\n");
    sql.append("   ORDER BY ORG.ORG_ID");
    params.add(userId);
    
    logger.info(sql.toString());
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> subSelectTmpYearPlan(String year, Long userId)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("SELECT DEALER.DEALER_SHORTNAME DEALER_NAME,\n");
    sql.append("       TVMG.GROUP_NAME,\n");
    sql.append("       TMP.ROW_NUMBER,\n");
    sql.append("       TMP.SUM_AMT,\n");
    sql.append("       TMP.JAN_AMT,\n");
    sql.append("       TMP.FEB_AMT,\n");
    sql.append("       TMP.MAR_AMT,\n");
    sql.append("       TMP.APR_AMT,\n");
    sql.append("       TMP.MAY_AMOUNT,\n");
    sql.append("       TMP.JUN_AMT,\n");
    sql.append("       TMP.JUL_AMT,\n");
    sql.append("       TMP.AUG_AMT,\n");
    sql.append("       TMP.SEP_AMT,\n");
    sql.append("       TMP.OCT_AMT,\n");
    sql.append("       TMP.NOV_AMT,\n");
    sql.append("       TMP.DEC_AMT\n");
    sql.append("  FROM TMP_VS_YEARLY_PLAN TMP, TM_DEALER DEALER, TM_VHCL_MATERIAL_GROUP TVMG\n");
    sql.append(" WHERE TMP.DEALER_CODE = DEALER.DEALER_CODE\n");
    sql.append("   AND TVMG.GROUP_CODE = TMP.GROUP_CODE\n");
    sql.append("   AND tmp.PLAN_YEAR=?\n");
    params.add(year);
    sql.append("   AND tmp.USER_ID=?\n");
    sql.append("   ORDER BY DEALER.DEALER_ID");
    params.add(userId);
    
    logger.info(sql.toString());
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public void insertTtYearlyPlan(TtVsYearlyPlanPO po)
  {
    dao.insert(po);
  }
  
  public List<Map<String, Object>> selectTmpYearPlanUnConfirmed(String year, Long userId)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select porg.ORG_ID,\n");
    sql.append("       porg.org_name,\n");
    sql.append("       plan.GROUP_ID,\n");
    sql.append("       sum(plan.PLAN_AMOUNT) amount\n");
    sql.append("  from TM_ORG                 org,\n");
    sql.append("       TT_VS_YEARLY_PLAN         plan,\n");
    sql.append("       TM_DEALER_ORG_RELATION r,\n");
    sql.append("       tm_org                 porg\n");
    sql.append(" where org.ORG_ID = r.ORG_ID\n");
    sql.append("   and porg.ORG_ID = org.PARENT_ORG_ID\n");
    sql.append("   and plan.DEALER_ID = r.DEALER_ID\n");
    sql.append("   and porg.ORG_TYPE = ?\n");
    params.add(Constant.ORG_TYPE_OEM);
    sql.append("   and plan.plan_year = ?\n");
    params.add(year);
    sql.append("   and plan.create_by = ?\n");
    params.add(userId);
    sql.append(" group by porg.ORG_ID, porg.ORG_NAME, plan.GROUP_ID\n");
    sql.append(" order by porg.ORG_ID");
    
    logger.info(sql.toString());
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> selectTmpYearPlanUnConfirmed(List<Map<String, Object>> list, Map<String, Object> conMap)
  {
    String companyId = conMap.get("companyId").toString();
    String areaId = conMap.get("areaId").toString();
    String planType = conMap.get("planType").toString();
    String year = conMap.get("year").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select\n");
    for (int i = 0; i < list.size(); i++)
    {
      Map<String, Object> map = (Map)list.get(i);
      sql.append("       sum(decode(a.group_id, '" + map.get("GROUP_ID") + "', a.amount, '0')) a" + map.get("GROUP_ID") + ",\n");
    }
    sql.append("       c.org_id org_id,\n");
    sql.append("       c.org_name org_name\n");
    
    sql.append("  from (select d.PLAN_ID,\n");
    sql.append("              d.MATERIAL_GROUPID group_id,\n");
    sql.append("              sum(d.SALE_AMOUNT) amount\n");
    sql.append("         from TT_VS_YEARLY_PLAN_DETAIL d\n");
    sql.append("        group by d.PLAN_ID, d.MATERIAL_GROUPID) a,\n");
    sql.append("      TT_VS_YEARLY_PLAN b,\n");
    sql.append("      TM_ORG c\n");
    sql.append("  where b.PLAN_ID = a.plan_id\n");
    sql.append("    AND c.ORG_ID = B.ORG_ID\n");
    sql.append("    AND b.COMPANY_ID=" + companyId + "\n");
    sql.append("    AND b.AREA_ID=" + areaId + "\n");
    sql.append("    AND b.PLAN_TYPE=" + planType + "\n");
    sql.append("    and b.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM + "\n");
    sql.append("    and b.PLAN_YEAR = " + year + "\n");
    sql.append("  group by c.org_name, c.org_id");
    sql.append("  order by c.org_id");
    
    logger.info(sql.toString());
    
    return dao.pageQuery(sql.toString(), params, getFunName() + System.currentTimeMillis());
  }
  
  public List<Map<String, Object>> selectSubYearPlanUnConfirmed(List<Map<String, Object>> list, String year, String buss_area, String planTypes, Long userId, Long orgId, String companyId)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select\n");
    for (int i = 0; i < list.size(); i++)
    {
      Map<String, Object> map = (Map)list.get(i);
      sql.append("       sum(decode(a.group_id, '" + map.get("GROUP_ID") + "', a.amount, '0')) a" + map.get("GROUP_ID") + ",\n");
    }
    sql.append("       c.DEALER_SHORTNAME DEALER_SHORTNAME,\n");
    sql.append("       c.dealer_id dealer_id\n");
    sql.append("  from (select d.PLAN_ID,\n");
    sql.append("               d.MATERIAL_GROUPID group_id,\n");
    sql.append("               sum(d.SALE_AMOUNT) amount\n");
    sql.append("          from TT_VS_YEARLY_PLAN_DETAIL d\n");
    sql.append("         group by d.PLAN_ID, d.MATERIAL_GROUPID) a,\n");
    sql.append("       TT_VS_YEARLY_PLAN b,\n");
    sql.append("       TM_DEALER c\n");
    sql.append(" where b.PLAN_ID = a.plan_id\n");
    sql.append("   AND c.DEALER_ID = b.DEALER_ID\n");
    sql.append("   AND b.COMPANY_ID = " + companyId + "\n");
    
    sql.append("   and b.DEALER_ID in\n");
    sql.append("       (select d.DEALER_ID\n");
    sql.append("          from tm_dealer d\n");
    sql.append("         start with d.DEALER_ID in\n");
    sql.append("                    (select td.DEALER_ID\n");
    sql.append("                       from TM_DEALER td, TM_DEALER_ORG_RELATION r\n");
    sql.append("                      where td.dealer_id = r.dealer_id\n");
    sql.append("                        and td.OEM_COMPANY_ID = b.COMPANY_ID\n");
    sql.append("                        and td.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("                        and r.ORG_ID = " + orgId + ")\n");
    sql.append("connect by prior d.DEALER_ID = d.PARENT_DEALER_D\n");
    sql.append("        )");
    
    sql.append("    and b.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM + "\n");
    sql.append("    and b.PLAN_YEAR = ? \n");
    params.add(year);
    sql.append("    and b.area_id = ?  \n");
    params.add(buss_area);
    sql.append("    and b.plan_type = ?  \n");
    params.add(planTypes);
    sql.append(" group by c.DEALER_SHORTNAME, c.dealer_id\n");
    sql.append(" order by c.dealer_id");
    
    logger.info(sql.toString());
    
    return dao.pageQuery(sql.toString(), params, getFunName() + System.currentTimeMillis());
  }
  
  public List<Map<String, Object>> selectAllSeries()
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    
    sql.append("select g.GROUP_ID, g.GROUP_NAME\n");
    sql.append("  from TM_VHCL_MATERIAL_GROUP g\n");
    sql.append(" where g.GROUP_LEVEL = 2;");
    
    logger.info(sql.toString());
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<TmVhclMaterialGroupPO> selectAllSeries(TmVhclMaterialGroupPO po)
  {
    List<TmVhclMaterialGroupPO> list = new LinkedList();
    list = dao.select(po);
    return list;
  }
  
  public Integer selectMaxPlanVer(String year, Long userId, Integer status, String companyId, String orgType, String areaId, String planType)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    sql.append("select nvl(max(p.plan_ver),0) plan_ver\n");
    sql.append("  from TT_VS_YEARLY_PLAN p\n");
    sql.append(" where p.STATUS = ?\n");
    params.add(status);
    sql.append("   and p.PLAN_YEAR = ?\n");
    params.add(year);
    sql.append("   and p.COMPANY_ID = ?\n");
    params.add(companyId);
    sql.append("   and p.ORG_TYPE = ?\n");
    params.add(orgType);
    sql.append("   and p.PLAN_TYPE = ?\n");
    params.add(planType);
    sql.append("   and p.AREA_ID = " + areaId + "\n");
    Map<String, Object> map = dao.pageQueryMap(sql.toString(), params, "");
    Integer val = new Integer(map.get("PLAN_VER").toString());
    return val;
  }
  
  public void insertTtYearlyPlanDetial(TtVsYearlyPlanDetailPO po)
  {
    dao.insert(po);
  }
  
  public String selectMaxPlanVer(String year, String companyId)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    
    sql.append("select nvl(max(plan_ver),0) plan_ver \n");
    sql.append("  from TT_VS_YEARLY_PLAN t\n");
    sql.append(" where t.PLAN_YEAR = ?\n");
    params.add(year);
    sql.append("   and t.company_Id=?");
    params.add(companyId);
    sql.append("   and t.STATUS = " + Constant.PLAN_MANAGE_CONFIRM);
    
    Map<String, Object> map = dao.pageQueryMap(sql.toString(), params, "");
    
    return map.get("PLAN_VER").toString();
  }
  
  public String selectOemMaxPlanVer(String year, String companyId, String dealerId, String planType)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    Integer orgType = new Integer(0);
    sql.append("select nvl(max(plan_ver),0) PLAN_VER \n");
    sql.append("  from TT_VS_YEARLY_PLAN t\n");
    sql.append(" where t.PLAN_YEAR = "+year+" \n");
    // sql.append("   and t.company_Id="+companyId+" ");
    sql.append("   and exists("+dealerId+" and v.DEALER_ID=t.dealer_id) ");
    //sql.append("   and t.area_id=?");
    // params.add(areaId);
    sql.append("   and t.STATUS = " + Constant.PLAN_MANAGE_CONFIRM);
    sql.append("   order by plan_ver desc");
    Map<String, Object> map = dao.pageQueryMap(sql.toString(), params, getFunName());
    return map.get("PLAN_VER").toString();
  }
  
  public List<Map<String, Object>> selectDealerMaxPlanVer(String year, String companyId, String dealerId, String areaId, String planType)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    
    sql.append("select distinct nvl(plan_ver,0) plan_ver \n");
    sql.append("  from TT_VS_YEARLY_PLAN t\n");
    sql.append(" where t.PLAN_YEAR = ?\n");
    params.add(year);
    sql.append("   and t.company_Id=?");
    params.add(companyId);
    sql.append("   and t.AREA_ID=?");
    params.add(areaId);
    sql.append("   and t.PLAN_TYPE=?");
    params.add(planType);
    sql.append("   and t.dealer_Id in (" + PlanUtil.createSqlStr(dealerId) + ")");
    sql.append("   and t.STATUS = " + Constant.PLAN_MANAGE_CONFIRM);
    sql.append("   order by plan_ver desc");
    
    List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
    
    return list;
  }
  
  public boolean isAreaOrg(Long orgId)
  {
    List<Object> params = new LinkedList();
    boolean isAreaOrg = true;
    StringBuffer sql = new StringBuffer();
    
    sql.append("select distinct org_type\n");
    sql.append("  from tm_org org\n");
    sql.append(" where org.PARENT_ORG_ID = ?");
    params.add(orgId);
    
    List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, "");
    if ((list == null) || (list.size() == 0))
    {
      isAreaOrg = false;
    }
    else
    {
      Map<String, Object> map = (Map)list.get(0);
      Integer orgType = (Integer)map.get("ORG_TYPE");
      if (orgType.intValue() != Constant.ORG_TYPE_DEALER.intValue()) {
        isAreaOrg = false;
      }
    }
    return isAreaOrg;
  }
  
  public PageResult<Map<String, Object>> oemSelectYearlyPlan(Map<String, Object> map, int pageSize, int curPage)
  {
    String plan_year = (String)map.get("plan_year");
    String plan_ver = (String)map.get("plan_ver");
    String plan_desc = (String)map.get("plan_desc");
    String buss_area = (String)map.get("buss_area");
    String planType = (String)map.get("planType");
    String orgCode = (String)map.get("orgCode");
    String logonOrgType = (String)map.get("logonOrgType");
    String logonOrgId = (String)map.get("logonOrgId");
    String companyId = map.get("companyId").toString();
    
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    
    sql.append("select a.ORG_CODE,\n");
    sql.append("       a.ORG_NAME,\n");
    sql.append("       b.group_code,\n");
    sql.append("       b.group_name,\n");
    sql.append("       c.amount,\n");
    sql.append("       b.m1,\n");
    sql.append("       b.m2,\n");
    sql.append("       b.m3,\n");
    sql.append("       b.m4,\n");
    sql.append("       b.m5,\n");
    sql.append("       b.m6,\n");
    sql.append("       b.m7,\n");
    sql.append("       b.m8,\n");
    sql.append("       b.m9,\n");
    sql.append("       b.m10,\n");
    sql.append("       b.m11,\n");
    sql.append("       b.m12\n");
    sql.append("  from (select p.PLAN_ID, org.org_code, org.ORG_NAME\n");
    sql.append("          from TT_VS_YEARLY_PLAN p, TM_ORG org\n");
    sql.append("         where p.ORG_ID = org.ORG_ID\n");
    sql.append("               and p.ORG_TYPE = " + Constant.ORG_TYPE_OEM + "\n");
    sql.append("               and p.COMPANY_ID = " + companyId + "\n");
    if ((logonOrgId != null) && (logonOrgType != null) && (!"".equals(logonOrgId)) && ("LARGEREGION".equals(logonOrgType)))
    {
      sql.append("           and p.org_id =?\n");
      params.add(logonOrgId);
    }
    sql.append("           and p.plan_year = ?\n");
    params.add(plan_year);
    sql.append("           and p.STATUS = ?\n");
    params.add(Constant.PLAN_MANAGE_CONFIRM);
    if ((plan_ver != null) && (!"".equals(plan_ver)))
    {
      sql.append("           and p.PLAN_VER = ?\n");
      params.add(plan_ver);
    }
    if ((plan_desc != null) && (!"".equals(plan_desc)))
    {
      sql.append("           and p.PLAN_DESC like ?\n");
      params.add(plan_desc);
    }
    if ((buss_area != null) && (!"".equals(buss_area)))
    {
      sql.append("           and p.area_id = ?\n");
      params.add(buss_area);
    }
    if ((planType != null) && (!"".equals(planType)))
    {
      sql.append("           and p.plan_type = ?\n");
      params.add(planType);
    }
    if ((orgCode != null) && (!"".equals(orgCode))) {
      sql.append("           and org.org_code in (" + PlanUtil.createSqlStr(orgCode) + ")\n");
    }
    sql.append(") A, \n");
    sql.append("       (select d.PLAN_ID,\n");
    sql.append("               g.GROUP_CODE,\n");
    sql.append("               g.GROUP_NAME,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 1, d.SALE_AMOUNT, 0)) m1,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 2, d.SALE_AMOUNT, 0)) m2,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 3, d.SALE_AMOUNT, 0)) m3,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 4, d.SALE_AMOUNT, 0)) m4,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 5, d.SALE_AMOUNT, 0)) m5,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 6, d.SALE_AMOUNT, 0)) m6,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 7, d.SALE_AMOUNT, 0)) m7,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 8, d.SALE_AMOUNT, 0)) m8,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 9, d.SALE_AMOUNT, 0)) m9,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 10, d.SALE_AMOUNT, 0)) m10,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 11, d.SALE_AMOUNT, 0)) m11,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 12, d.SALE_AMOUNT, 0)) m12\n");
    sql.append("          from TT_VS_YEARLY_PLAN_DETAIL d, TM_VHCL_MATERIAL_GROUP g\n");
    sql.append("         where g.GROUP_ID = d.MATERIAL_GROUPID\n");
    sql.append("         group by d.PLAN_ID, g.GROUP_CODE, g.GROUP_NAME\n");
    sql.append("         order by d.PLAN_ID) B,\n");
    sql.append("       (select d1.plan_id plan_id, sum(d1.sale_amount) amount\n");
    sql.append("          from TT_VS_YEARLY_PLAN_DETAIL d1\n");
    sql.append("         group by d1.PLAN_ID) C\n");
    sql.append(" where a.PLAN_ID = b.plan_id\n");
    sql.append("   and a.plan_id = c.plan_id\n");
    sql.append("   order by a.ORG_CODE");
    
    return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
  }
  
  public PageResult<Map<String, Object>> oemSelectDealerYearlyPlan(Map<String, Object> map, int pageSize, int curPage)
  {
    String plan_year = (String)map.get("plan_year");
    String plan_ver = (String)map.get("plan_ver");
    String plan_desc = (String)map.get("plan_desc");
    String buss_area = (String)map.get("buss_area");
    String planType = (String)map.get("planType");
    String dealerCode = (String)map.get("dealerCode");
    String logonOrgType = (String)map.get("logonOrgType");
    String logonOrgId = (String)map.get("logonOrgId");
    String companyId = map.get("companyId").toString();
    
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    
    sql.append("select a.ORG_CODE,\n");
    sql.append("       a.ORG_NAME,\n");
    sql.append("       a.DEALER_CODE,\n");
    sql.append("       a.DEALER_SHORTNAME,\n");
    sql.append("       b.group_code,\n");
    sql.append("       b.group_name,\n");
    sql.append("       c.amount,\n");
    sql.append("       b.m1,\n");
    sql.append("       b.m2,\n");
    sql.append("       b.m3,\n");
    sql.append("       b.m4,\n");
    sql.append("       b.m5,\n");
    sql.append("       b.m6,\n");
    sql.append("       b.m7,\n");
    sql.append("       b.m8,\n");
    sql.append("       b.m9,\n");
    sql.append("       b.m10,\n");
    sql.append("       b.m11,\n");
    sql.append("       b.m12\n");
    
    sql.append("from (select  p.PLAN_ID,\n");
    sql.append("              org.org_code,\n");
    sql.append("              org.ORG_NAME,\n");
    sql.append("              td.DEALER_CODE,\n");
    sql.append("              td.DEALER_SHORTNAME\n");
    sql.append("         from TT_VS_YEARLY_PLAN         p,\n");
    sql.append("              TM_ORG                 org,\n");
    sql.append("              TM_DEALER              td,\n");
    sql.append("              TM_DEALER_ORG_RELATION r\n");
    sql.append("        where p.DEALER_ID = td.DEALER_ID\n");
    sql.append("          and r.org_id = org.ORG_ID\n");
    
    sql.append("          and (select t.dealer_id\n");
    sql.append("                  from TM_DEALER t\n");
    sql.append("                 where t.DEALER_LEVEL = " + Constant.DEALER_LEVEL_01 + "\n");
    sql.append("                 start with t.DEALER_ID = p.dealer_id\n");
    sql.append("                connect by prior t.PARENT_DEALER_D = t.DEALER_ID) =r.DEALER_ID\n");
    
    sql.append("          and p.COMPANY_ID = " + companyId + "\n");
    sql.append("          and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
    if ((logonOrgId != null) && (logonOrgType != null) && (!"".equals(logonOrgId)) && ("LARGEREGION".equals(logonOrgType)))
    {
      sql.append("           and r.org_id =?\n");
      params.add(logonOrgId);
    }
    sql.append("           and p.plan_year = ?\n");
    params.add(plan_year);
    sql.append("           and p.STATUS = ?\n");
    params.add(Constant.PLAN_MANAGE_CONFIRM);
    if ((plan_ver != null) && (!"".equals(plan_ver)))
    {
      sql.append("           and p.PLAN_VER = ?\n");
      params.add(plan_ver);
    }
    if ((plan_desc != null) && (!"".equals(plan_desc)))
    {
      sql.append("           and p.PLAN_DESC like ?\n");
      params.add(plan_desc);
    }
    if ((buss_area != null) && (!"".equals(buss_area)))
    {
      sql.append("           and p.area_id = ?\n");
      params.add(buss_area);
    }
    if ((planType != null) && (!"".equals(planType)))
    {
      sql.append("           and p.plan_type = ?\n");
      params.add(planType);
    }
    if ((dealerCode != null) && (!"".equals(dealerCode))) {
      sql.append("           and td.dealer_code in (" + PlanUtil.createSqlStr(dealerCode) + ")\n");
    }
    sql.append(") A, \n");
    sql.append("       (select d.PLAN_ID,\n");
    sql.append("               g.GROUP_CODE,\n");
    sql.append("               g.GROUP_NAME,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 1, d.SALE_AMOUNT, 0)) m1,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 2, d.SALE_AMOUNT, 0)) m2,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 3, d.SALE_AMOUNT, 0)) m3,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 4, d.SALE_AMOUNT, 0)) m4,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 5, d.SALE_AMOUNT, 0)) m5,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 6, d.SALE_AMOUNT, 0)) m6,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 7, d.SALE_AMOUNT, 0)) m7,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 8, d.SALE_AMOUNT, 0)) m8,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 9, d.SALE_AMOUNT, 0)) m9,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 10, d.SALE_AMOUNT, 0)) m10,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 11, d.SALE_AMOUNT, 0)) m11,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 12, d.SALE_AMOUNT, 0)) m12\n");
    sql.append("          from TT_VS_YEARLY_PLAN_DETAIL d, TM_VHCL_MATERIAL_GROUP g\n");
    sql.append("         where g.GROUP_ID = d.MATERIAL_GROUPID\n");
    sql.append("         group by d.PLAN_ID, g.GROUP_CODE, g.GROUP_NAME\n");
    sql.append("         order by d.PLAN_ID) B,\n");
    sql.append("       (select d1.plan_id plan_id, sum(d1.sale_amount) amount\n");
    sql.append("          from TT_VS_YEARLY_PLAN_DETAIL d1\n");
    sql.append("         group by d1.PLAN_ID) C\n");
    sql.append(" where a.PLAN_ID = b.plan_id\n");
    sql.append("   and a.plan_id = c.plan_id\n");
    sql.append("   order by a.ORG_CODE");
    
    return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
  }
  
  public List<Map<String, Object>> selectSeries(String companyId)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    
    sql.append("select g.GROUP_NAME,g.GROUP_ID\n");
    sql.append("  from TM_VHCL_MATERIAL_GROUP g\n");
    sql.append(" where g.GROUP_level = 2\n");
    sql.append("    and g.COMPANY_ID=" + companyId + "\n");
    sql.append(" order by g.GROUP_ID");
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  protected PO wrapperPO(ResultSet rs, int idx)
  {
    return null;
  }
  
  public PageResult<Map<String, Object>> oemSelectDealerYearlyPlanTotal(Map<String, Object> map, List<Map<String, Object>> serlist, int pageSize, int curPage)
  {
    String plan_year = (String)map.get("plan_year");
    String plan_ver = (String)map.get("plan_ver");
    String plan_desc = (String)map.get("plan_desc");
    String buss_area = (String)map.get("buss_area");
    String planType = (String)map.get("planType");
    String dealerCode = (String)map.get("dealerCode");
    String logonOrgType = (String)map.get("logonOrgType");
    String logonOrgId = (String)map.get("logonOrgId");
    String companyId = map.get("companyId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    sql.append("\n");
    sql.append("SELECT YEARLY.DEALER_CODE,\n");
    sql.append("       YEARLY.DEALER_SHORTNAME,\n");
    for (int i = 0; i < serlist.size(); i++)
    {
      Map<String, Object> sermap = (Map)serlist.get(i);
      sql.append("       nvl(MAX(DECODE(YEARLY.MATERIAL_GROUPID,\n");
      sql.append("                  " + sermap.get("GROUP_ID") + ",\n");
      sql.append("                  YEARLY.SALE_AMOUNT)),0) S" + i + ",\n");
    }
    sql.append("       sum(yearly.SALE_AMOUNT) SALE_AMOUNT,\n");
    sql.append("       YEARLY.DEALER_ID\n");
    sql.append("  FROM (SELECT TVYP.DEALER_ID,\n");
    sql.append("               TD.DEALER_CODE,\n");
    sql.append("               TD.DEALER_SHORTNAME,\n");
    sql.append("               TVYPD.MATERIAL_GROUPID,\n");
    sql.append("               nvl(SUM(TVYPD.SALE_AMOUNT),0) AS SALE_AMOUNT\n");
    sql.append("          FROM TT_VS_YEARLY_PLAN        TVYP,\n");
    sql.append("               TT_VS_YEARLY_PLAN_DETAIL TVYPD,\n");
    sql.append("               TM_DEALER                TD\n");
    sql.append("         WHERE TVYP.PLAN_ID = TVYPD.PLAN_ID\n");
    sql.append("           AND TVYP.DEALER_ID = TD.DEALER_ID\n");
    sql.append("           AND TVYP.COMPANY_ID=" + companyId + "\n");
    sql.append("           AND TVYP.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
    if ((logonOrgId != null) && (logonOrgType != null) && (!"".equals(logonOrgId)) && ("LARGEREGION".equals(logonOrgType)))
    {
      sql.append("      and TVYP.DEALER_ID in \n");
      sql.append("       (select d.DEALER_ID\n");
      sql.append("          from tm_dealer d\n");
      sql.append("         start with d.DEALER_ID in\n");
      sql.append("                    (select td.DEALER_ID\n");
      sql.append("                       from TM_DEALER td, TM_DEALER_ORG_RELATION r\n");
      sql.append("                      where td.dealer_id = r.dealer_id\n");
      sql.append("                        and td.OEM_COMPANY_ID =TVYP.COMPANY_ID\n");
      sql.append("                        and td.STATUS = " + Constant.STATUS_ENABLE + "\n");
      sql.append("                        and r.ORG_ID = " + logonOrgId + ")\n");
      sql.append("connect by prior d.DEALER_ID = d.PARENT_DEALER_D\n");
      sql.append("        )");
    }
    sql.append("           and TVYP.plan_year = ?\n");
    params.add(plan_year);
    sql.append("           and TVYP.STATUS = ?\n");
    params.add(Constant.PLAN_MANAGE_CONFIRM);
    if ((plan_ver != null) && (!"".equals(plan_ver)))
    {
      sql.append("           and TVYP.PLAN_VER = ?\n");
      params.add(plan_ver);
    }
    if ((plan_desc != null) && (!"".equals(plan_desc)))
    {
      sql.append("           and TVYP.PLAN_DESC like ?\n");
      params.add(plan_desc);
    }
    if ((buss_area != null) && (!"".equals(buss_area)))
    {
      sql.append("           and TVYP.area_id = ?\n");
      params.add(buss_area);
    }
    if ((planType != null) && (!"".equals(planType)))
    {
      sql.append("           and TVYP.plan_type = ?\n");
      params.add(planType);
    }
    if ((dealerCode != null) && (!"".equals(dealerCode))) {
      sql.append("           and td.dealer_code in (" + PlanUtil.createSqlStr(dealerCode) + ")\n");
    }
    sql.append("         GROUP BY TVYP.DEALER_ID,\n");
    sql.append("                  TVYPD.MATERIAL_GROUPID,\n");
    sql.append("                  TD.DEALER_CODE,\n");
    sql.append("                  TD.DEALER_SHORTNAME) YEARLY\n");
    sql.append(" GROUP BY DEALER_ID, YEARLY.DEALER_CODE, YEARLY.DEALER_SHORTNAME\n");
    sql.append(" order by dealer_id");
    
    return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
  }
  
  public PageResult<Map<String, Object>> oemSelectOrgYearlyPlanTotal(Map<String, Object> map, List<Map<String, Object>> serlist, int pageSize, int curPage)
  {
    String plan_year = (String)map.get("plan_year");
    String plan_ver = (String)map.get("plan_ver");
    String plan_desc = (String)map.get("plan_desc");
    String buss_area = (String)map.get("buss_area");
    String planType = (String)map.get("planType");
    String orgCode = (String)map.get("orgCode");
    String logonOrgType = (String)map.get("logonOrgType");
    String logonOrgId = (String)map.get("logonOrgId");
    String companyId = map.get("companyId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    sql.append("\n");
    sql.append("SELECT YEARLY.ORG_CODE,\n");
    sql.append("       YEARLY.ORG_NAME,\n");
    for (int i = 0; i < serlist.size(); i++)
    {
      Map<String, Object> sermap = (Map)serlist.get(i);
      sql.append("       nvl(MAX(DECODE(YEARLY.MATERIAL_GROUPID,\n");
      sql.append("                  " + sermap.get("GROUP_ID") + ",\n");
      sql.append("                  YEARLY.SALE_AMOUNT)),0) S" + i + ",\n");
    }
    sql.append("       sum(yearly.SALE_AMOUNT) SALE_AMOUNT,\n");
    sql.append("       YEARLY.ORG_ID\n");
    sql.append("  FROM (SELECT TVYP.ORG_ID,\n");
    sql.append("               ORG.ORG_CODE,\n");
    sql.append("               ORG.ORG_NAME,\n");
    sql.append("               TVYPD.MATERIAL_GROUPID,\n");
    sql.append("               nvl(SUM(TVYPD.SALE_AMOUNT), 0) AS SALE_AMOUNT\n");
    sql.append("          FROM TT_VS_YEARLY_PLAN        TVYP,\n");
    sql.append("               TT_VS_YEARLY_PLAN_DETAIL TVYPD,\n");
    sql.append("               TM_ORG                   ORG\n");
    sql.append("         WHERE TVYP.PLAN_ID = TVYPD.PLAN_ID\n");
    sql.append("           AND TVYP.ORG_ID = ORG.ORG_ID\n");
    sql.append("           AND TVYP.COMPANY_ID=" + companyId + "\n");
    sql.append("           AND TVYP.ORG_TYPE = " + Constant.ORG_TYPE_OEM + "\n");
    sql.append("           and TVYP.plan_year = ?\n");
    params.add(plan_year);
    sql.append("           and TVYP.STATUS = ?\n");
    params.add(Constant.PLAN_MANAGE_CONFIRM);
    if ((logonOrgId != null) && (logonOrgType != null) && (!"".equals(logonOrgId)) && ("LARGEREGION".equals(logonOrgType)))
    {
      sql.append("       and TVYP.ORG_ID=?\n");
      params.add(logonOrgId);
    }
    if ((plan_ver != null) && (!"".equals(plan_ver)))
    {
      sql.append("           and TVYP.PLAN_VER = ?\n");
      params.add(plan_ver);
    }
    if ((plan_desc != null) && (!"".equals(plan_desc)))
    {
      sql.append("           and TVYP.PLAN_DESC like ?\n");
      params.add(plan_desc);
    }
    if ((buss_area != null) && (!"".equals(buss_area)))
    {
      sql.append("           and TVYP.area_id = ?\n");
      params.add(buss_area);
    }
    if ((planType != null) && (!"".equals(planType)))
    {
      sql.append("           and TVYP.plan_type = ?\n");
      params.add(planType);
    }
    if ((orgCode != null) && (!"".equals(orgCode))) {
      sql.append("           and org.ORG_CODE in (" + PlanUtil.createSqlStr(orgCode) + ")\n");
    }
    sql.append("         GROUP BY TVYP.ORG_ID,\n");
    sql.append("                  TVYPD.MATERIAL_GROUPID,\n");
    sql.append("                  ORG.ORG_CODE,\n");
    sql.append("                  ORG.ORG_NAME) YEARLY\n");
    sql.append(" GROUP BY YEARLY.ORG_ID, YEARLY.ORG_CODE, YEARLY.ORG_NAME\n");
    sql.append(" order by YEARLY.ORG_ID");
    
    return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
  }
  
  public List<Map<String, Object>> oemSelectDealerYearlyPlanTotalDown(Map<String, Object> map, List<Map<String, Object>> serlist)
  {
    String plan_year = (String)map.get("plan_year");
    String plan_ver = (String)map.get("plan_ver");
    String plan_desc = (String)map.get("plan_desc");
    String buss_area = (String)map.get("buss_area");
    String planType = (String)map.get("planType");
    String dealerCode = (String)map.get("dealerCode");
    String logonOrgType = (String)map.get("logonOrgType");
    String logonOrgId = (String)map.get("logonOrgId");
    String companyId = map.get("companyId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    sql.append("\n");
    sql.append("SELECT YEARLY.DEALER_CODE,\n");
    sql.append("       YEARLY.DEALER_SHORTNAME,\n");
    for (int i = 0; i < serlist.size(); i++)
    {
      Map<String, Object> sermap = (Map)serlist.get(i);
      sql.append("       nvl(MAX(DECODE(YEARLY.MATERIAL_GROUPID,\n");
      sql.append("                  " + sermap.get("GROUP_ID") + ",\n");
      sql.append("                  YEARLY.SALE_AMOUNT)),0) S" + i + ",\n");
    }
    sql.append("       sum(yearly.SALE_AMOUNT) SALE_AMOUNT,\n");
    sql.append("       YEARLY.DEALER_ID\n");
    sql.append("  FROM (SELECT TVYP.DEALER_ID,\n");
    sql.append("               TD.DEALER_CODE,\n");
    sql.append("               TD.DEALER_SHORTNAME,\n");
    sql.append("               TVYPD.MATERIAL_GROUPID,\n");
    sql.append("               nvl(SUM(TVYPD.SALE_AMOUNT),0) AS SALE_AMOUNT\n");
    sql.append("          FROM TT_VS_YEARLY_PLAN        TVYP,\n");
    sql.append("               TT_VS_YEARLY_PLAN_DETAIL TVYPD,\n");
    sql.append("               TM_DEALER                TD\n");
    sql.append("         WHERE TVYP.PLAN_ID = TVYPD.PLAN_ID\n");
    sql.append("           AND TVYP.DEALER_ID = TD.DEALER_ID\n");
    sql.append("           AND TVYP.COMPANY_ID=" + companyId + "\n");
    sql.append("           AND TVYP.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
    if ((logonOrgId != null) && (logonOrgType != null) && (!"".equals(logonOrgId)) && ("LARGEREGION".equals(logonOrgType)))
    {
      sql.append("      and TVYP.DEALER_ID in \n");
      sql.append("       (select d.DEALER_ID\n");
      sql.append("          from tm_dealer d\n");
      sql.append("         start with d.DEALER_ID in\n");
      sql.append("                    (select td.DEALER_ID\n");
      sql.append("                       from TM_DEALER td, TM_DEALER_ORG_RELATION r\n");
      sql.append("                      where td.dealer_id = r.dealer_id\n");
      sql.append("                        and td.OEM_COMPANY_ID =TVYP.COMPANY_ID\n");
      sql.append("                        and td.STATUS = " + Constant.STATUS_ENABLE + "\n");
      sql.append("                        and r.ORG_ID = " + logonOrgId + ")\n");
      sql.append("connect by prior d.DEALER_ID = d.PARENT_DEALER_D\n");
      sql.append("        )");
    }
    sql.append("           and TVYP.plan_year = ?\n");
    params.add(plan_year);
    sql.append("           and TVYP.STATUS = ?\n");
    params.add(Constant.PLAN_MANAGE_CONFIRM);
    if ((plan_ver != null) && (!"".equals(plan_ver)))
    {
      sql.append("           and TVYP.PLAN_VER = ?\n");
      params.add(plan_ver);
    }
    if ((plan_desc != null) && (!"".equals(plan_desc)))
    {
      sql.append("           and TVYP.PLAN_DESC like ?\n");
      params.add(plan_desc);
    }
    if ((buss_area != null) && (!"".equals(buss_area)))
    {
      sql.append("           and TVYP.area_id = ?\n");
      params.add(buss_area);
    }
    if ((planType != null) && (!"".equals(planType)))
    {
      sql.append("           and TVYP.plan_type = ?\n");
      params.add(planType);
    }
    if ((dealerCode != null) && (!"".equals(dealerCode))) {
      sql.append("           and td.dealer_code in (" + PlanUtil.createSqlStr(dealerCode) + ")\n");
    }
    sql.append("         GROUP BY TVYP.DEALER_ID,\n");
    sql.append("                  TVYPD.MATERIAL_GROUPID,\n");
    sql.append("                  TD.DEALER_CODE,\n");
    sql.append("                  TD.DEALER_SHORTNAME) YEARLY\n");
    sql.append(" GROUP BY DEALER_ID, YEARLY.DEALER_CODE, YEARLY.DEALER_SHORTNAME\n");
    sql.append(" order by dealer_id");
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> oemSelectOrgYearlyPlanTotalDown(Map<String, Object> map, List<Map<String, Object>> serlist)
  {
    String plan_year = (String)map.get("plan_year");
    String plan_ver = (String)map.get("plan_ver");
    String plan_desc = (String)map.get("plan_desc");
    String buss_area = (String)map.get("buss_area");
    String planType = (String)map.get("planType");
    String orgCode = (String)map.get("orgCode");
    String logonOrgType = (String)map.get("logonOrgType");
    String logonOrgId = (String)map.get("logonOrgId");
    String companyId = map.get("companyId").toString();
    
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    
    sql.append("\n");
    sql.append("SELECT YEARLY.ORG_CODE,\n");
    sql.append("       YEARLY.ORG_NAME,\n");
    for (int i = 0; i < serlist.size(); i++)
    {
      Map<String, Object> sermap = (Map)serlist.get(i);
      sql.append("       nvl(MAX(DECODE(YEARLY.MATERIAL_GROUPID,\n");
      sql.append("                  " + sermap.get("GROUP_ID") + ",\n");
      sql.append("                  YEARLY.SALE_AMOUNT)),0) S" + i + ",\n");
    }
    sql.append("       sum(yearly.SALE_AMOUNT) SALE_AMOUNT,\n");
    sql.append("       YEARLY.ORG_ID\n");
    sql.append("  FROM (SELECT TVYP.ORG_ID,\n");
    sql.append("               ORG.ORG_CODE,\n");
    sql.append("               ORG.ORG_NAME,\n");
    sql.append("               TVYPD.MATERIAL_GROUPID,\n");
    sql.append("               nvl(SUM(TVYPD.SALE_AMOUNT), 0) AS SALE_AMOUNT\n");
    sql.append("          FROM TT_VS_YEARLY_PLAN        TVYP,\n");
    sql.append("               TT_VS_YEARLY_PLAN_DETAIL TVYPD,\n");
    sql.append("               TM_ORG                   ORG\n");
    sql.append("         WHERE TVYP.PLAN_ID = TVYPD.PLAN_ID\n");
    sql.append("           AND TVYP.ORG_ID = ORG.ORG_ID\n");
    sql.append("           AND TVYP.COMPANY_ID=" + companyId + "\n");
    sql.append("           AND TVYP.ORG_TYPE = " + Constant.ORG_TYPE_OEM + "\n");
    sql.append("           and TVYP.plan_year = ?\n");
    params.add(plan_year);
    sql.append("           and TVYP.STATUS = ?\n");
    params.add(Constant.PLAN_MANAGE_CONFIRM);
    if ((logonOrgId != null) && (logonOrgType != null) && (!"".equals(logonOrgId)) && ("LARGEREGION".equals(logonOrgType)))
    {
      sql.append("       and TVYP.ORG_ID=?\n");
      params.add(logonOrgId);
    }
    if ((plan_ver != null) && (!"".equals(plan_ver)))
    {
      sql.append("           and TVYP.PLAN_VER = ?\n");
      params.add(plan_ver);
    }
    if ((plan_desc != null) && (!"".equals(plan_desc)))
    {
      sql.append("           and TVYP.PLAN_DESC like ?\n");
      params.add(plan_desc);
    }
    if ((buss_area != null) && (!"".equals(buss_area)))
    {
      sql.append("           and TVYP.area_id = ?\n");
      params.add(buss_area);
    }
    if ((planType != null) && (!"".equals(planType)))
    {
      sql.append("           and TVYP.plan_type = ?\n");
      params.add(planType);
    }
    if ((orgCode != null) && (!"".equals(orgCode))) {
      sql.append("           and org.ORG_CODE in (" + PlanUtil.createSqlStr(orgCode) + ")\n");
    }
    sql.append("         GROUP BY TVYP.ORG_ID,\n");
    sql.append("                  TVYPD.MATERIAL_GROUPID,\n");
    sql.append("                  ORG.ORG_CODE,\n");
    sql.append("                  ORG.ORG_NAME) YEARLY\n");
    sql.append(" GROUP BY YEARLY.ORG_ID, YEARLY.ORG_CODE, YEARLY.ORG_NAME\n");
    sql.append(" order by YEARLY.ORG_ID");
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> oemSelectOrgDetailYearlyPlanDown(Map<String, Object> map)
  {
    String plan_year = (String)map.get("plan_year");
    String plan_ver = (String)map.get("plan_ver");
    String plan_desc = (String)map.get("plan_desc");
    String buss_area = (String)map.get("buss_area");
    String planType = (String)map.get("planType");
    String orgCode = (String)map.get("orgCode");
    String logonOrgType = (String)map.get("logonOrgType");
    String logonOrgId = (String)map.get("logonOrgId");
    String companyId = map.get("companyId").toString();
    
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    
    sql.append("select a.ORG_CODE,\n");
    sql.append("       a.ORG_NAME,\n");
    sql.append("       b.group_code,\n");
    sql.append("       b.group_name,\n");
    sql.append("       c.amount,\n");
    sql.append("       b.m1,\n");
    sql.append("       b.m2,\n");
    sql.append("       b.m3,\n");
    sql.append("       b.m4,\n");
    sql.append("       b.m5,\n");
    sql.append("       b.m6,\n");
    sql.append("       b.m7,\n");
    sql.append("       b.m8,\n");
    sql.append("       b.m9,\n");
    sql.append("       b.m10,\n");
    sql.append("       b.m11,\n");
    sql.append("       b.m12\n");
    sql.append("  from (select p.PLAN_ID, org.org_code, org.ORG_NAME\n");
    sql.append("          from TT_VS_YEARLY_PLAN p, TM_ORG org\n");
    sql.append("         where p.ORG_ID = org.ORG_ID\n");
    sql.append("           \t   AND p.COMPANY_ID=" + companyId + "\n");
    sql.append("               and p.ORG_TYPE = " + Constant.ORG_TYPE_OEM + "\n");
    if ((logonOrgId != null) && (logonOrgType != null) && (!"".equals(logonOrgId)) && ("LARGEREGION".equals(logonOrgType)))
    {
      sql.append("           and p.org_id =?\n");
      params.add(logonOrgId);
    }
    sql.append("           and p.plan_year = ?\n");
    params.add(plan_year);
    sql.append("           and p.STATUS = ?\n");
    params.add(Constant.PLAN_MANAGE_CONFIRM);
    if ((plan_ver != null) && (!"".equals(plan_ver)))
    {
      sql.append("           and p.PLAN_VER = ?\n");
      params.add(plan_ver);
    }
    if ((plan_desc != null) && (!"".equals(plan_desc)))
    {
      sql.append("           and p.PLAN_DESC like ?\n");
      params.add(plan_desc);
    }
    if ((buss_area != null) && (!"".equals(buss_area)))
    {
      sql.append("           and p.area_id = ?\n");
      params.add(buss_area);
    }
    if ((planType != null) && (!"".equals(planType)))
    {
      sql.append("           and p.plan_type = ?\n");
      params.add(planType);
    }
    if ((orgCode != null) && (!"".equals(orgCode))) {
      sql.append("           and org.org_code in (" + PlanUtil.createSqlStr(orgCode) + ")\n");
    }
    sql.append(") A, \n");
    sql.append("       (select d.PLAN_ID,\n");
    sql.append("               g.GROUP_CODE,\n");
    sql.append("               g.GROUP_NAME,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 1, d.SALE_AMOUNT, 0)) m1,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 2, d.SALE_AMOUNT, 0)) m2,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 3, d.SALE_AMOUNT, 0)) m3,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 4, d.SALE_AMOUNT, 0)) m4,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 5, d.SALE_AMOUNT, 0)) m5,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 6, d.SALE_AMOUNT, 0)) m6,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 7, d.SALE_AMOUNT, 0)) m7,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 8, d.SALE_AMOUNT, 0)) m8,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 9, d.SALE_AMOUNT, 0)) m9,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 10, d.SALE_AMOUNT, 0)) m10,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 11, d.SALE_AMOUNT, 0)) m11,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 12, d.SALE_AMOUNT, 0)) m12\n");
    sql.append("          from TT_VS_YEARLY_PLAN_DETAIL d, TM_VHCL_MATERIAL_GROUP g\n");
    sql.append("         where g.GROUP_ID = d.MATERIAL_GROUPID\n");
    sql.append("         group by d.PLAN_ID, g.GROUP_CODE, g.GROUP_NAME\n");
    sql.append("         order by d.PLAN_ID) B,\n");
    sql.append("       (select d1.plan_id plan_id, sum(d1.sale_amount) amount\n");
    sql.append("          from TT_VS_YEARLY_PLAN_DETAIL d1\n");
    sql.append("         group by d1.PLAN_ID) C\n");
    sql.append(" where a.PLAN_ID = b.plan_id\n");
    sql.append("   and a.plan_id = c.plan_id\n");
    sql.append("   order by a.ORG_CODE");
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> oemSelectDealerDetailYearlyPlanDown(Map<String, Object> map)
  {
    String plan_year = (String)map.get("plan_year");
    String plan_ver = (String)map.get("plan_ver");
    String plan_desc = (String)map.get("plan_desc");
    String buss_area = (String)map.get("buss_area");
    String planType = (String)map.get("planType");
    String dealerCode = (String)map.get("dealerCode");
    String logonOrgType = (String)map.get("logonOrgType");
    String logonOrgId = (String)map.get("logonOrgId");
    String companyId = map.get("companyId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    sql.append("select a.ORG_CODE,\n");
    sql.append("       a.ORG_NAME,\n");
    sql.append("       a.DEALER_CODE,\n");
    sql.append("       a.DEALER_SHORTNAME,\n");
    sql.append("       b.group_code,\n");
    sql.append("       b.group_name,\n");
    sql.append("       c.amount,\n");
    sql.append("       b.m1,\n");
    sql.append("       b.m2,\n");
    sql.append("       b.m3,\n");
    sql.append("       b.m4,\n");
    sql.append("       b.m5,\n");
    sql.append("       b.m6,\n");
    sql.append("       b.m7,\n");
    sql.append("       b.m8,\n");
    sql.append("       b.m9,\n");
    sql.append("       b.m10,\n");
    sql.append("       b.m11,\n");
    sql.append("       b.m12\n");
    
    sql.append("from (select  p.PLAN_ID,\n");
    sql.append("              org.org_code,\n");
    sql.append("              org.ORG_NAME,\n");
    sql.append("              td.DEALER_CODE,\n");
    sql.append("              td.DEALER_SHORTNAME\n");
    sql.append("         from TT_VS_YEARLY_PLAN         p,\n");
    sql.append("              TM_ORG                 org,\n");
    sql.append("              TM_DEALER              td,\n");
    sql.append("              TM_DEALER_ORG_RELATION r\n");
    sql.append("        where p.DEALER_ID = td.DEALER_ID\n");
    sql.append("          and r.org_id = org.ORG_ID\n");
    
    sql.append("          and (select t.dealer_id\n");
    sql.append("                  from TM_DEALER t\n");
    sql.append("                 where t.DEALER_LEVEL = " + Constant.DEALER_LEVEL_01 + "\n");
    sql.append("                 start with t.DEALER_ID = p.dealer_id\n");
    sql.append("                connect by prior t.PARENT_DEALER_D = t.DEALER_ID) =r.DEALER_ID\n");
    sql.append("          AND p.COMPANY_ID=" + companyId + "\n");
    sql.append("          and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
    if ((logonOrgId != null) && (logonOrgType != null) && (!"".equals(logonOrgId)) && ("LARGEREGION".equals(logonOrgType)))
    {
      sql.append("           and r.org_id =?\n");
      params.add(logonOrgId);
    }
    sql.append("           and p.plan_year = ?\n");
    params.add(plan_year);
    sql.append("           and p.STATUS = ?\n");
    params.add(Constant.PLAN_MANAGE_CONFIRM);
    if ((plan_ver != null) && (!"".equals(plan_ver)))
    {
      sql.append("           and p.PLAN_VER = ?\n");
      params.add(plan_ver);
    }
    if ((plan_desc != null) && (!"".equals(plan_desc)))
    {
      sql.append("           and p.PLAN_DESC like ?\n");
      params.add(plan_desc);
    }
    if ((buss_area != null) && (!"".equals(buss_area)))
    {
      sql.append("           and p.area_id = ?\n");
      params.add(buss_area);
    }
    if ((planType != null) && (!"".equals(planType)))
    {
      sql.append("           and p.plan_type = ?\n");
      params.add(planType);
    }
    if ((dealerCode != null) && (!"".equals(dealerCode))) {
      sql.append("           and td.dealer_code in (" + PlanUtil.createSqlStr(dealerCode) + ")\n");
    }
    sql.append(") A, \n");
    sql.append("       (select d.PLAN_ID,\n");
    sql.append("               g.GROUP_CODE,\n");
    sql.append("               g.GROUP_NAME,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 1, d.SALE_AMOUNT, 0)) m1,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 2, d.SALE_AMOUNT, 0)) m2,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 3, d.SALE_AMOUNT, 0)) m3,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 4, d.SALE_AMOUNT, 0)) m4,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 5, d.SALE_AMOUNT, 0)) m5,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 6, d.SALE_AMOUNT, 0)) m6,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 7, d.SALE_AMOUNT, 0)) m7,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 8, d.SALE_AMOUNT, 0)) m8,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 9, d.SALE_AMOUNT, 0)) m9,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 10, d.SALE_AMOUNT, 0)) m10,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 11, d.SALE_AMOUNT, 0)) m11,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 12, d.SALE_AMOUNT, 0)) m12\n");
    sql.append("          from TT_VS_YEARLY_PLAN_DETAIL d, TM_VHCL_MATERIAL_GROUP g\n");
    sql.append("         where g.GROUP_ID = d.MATERIAL_GROUPID\n");
    sql.append("         group by d.PLAN_ID, g.GROUP_CODE, g.GROUP_NAME\n");
    sql.append("         order by d.PLAN_ID) B,\n");
    sql.append("       (select d1.plan_id plan_id, sum(d1.sale_amount) amount\n");
    sql.append("          from TT_VS_YEARLY_PLAN_DETAIL d1\n");
    sql.append("         group by d1.PLAN_ID) C\n");
    sql.append(" where a.PLAN_ID = b.plan_id\n");
    sql.append("   and a.plan_id = c.plan_id\n");
    sql.append("   order by a.ORG_CODE");
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public PageResult<Map<String, Object>> dealerSelectDetailYearlyPlan(Map<String, Object> map, int pageSize, int curPage)
  {
    String plan_year = (String)map.get("plan_year");
    String plan_ver = (String)map.get("plan_ver");
    String plan_desc = (String)map.get("plan_desc");
    String buss_area = (String)map.get("buss_area");
    String planType = (String)map.get("planType");
    String dealerId = (String)map.get("dealerId");
    String companyId = map.get("companyId").toString();
    
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    
    sql.append("select a.ORG_CODE,\n");
    sql.append("       a.ORG_NAME,\n");
    sql.append("       a.DEALER_CODE,\n");
    sql.append("       a.DEALER_SHORTNAME,\n");
    sql.append("       b.group_code,\n");
    sql.append("       b.group_name,\n");
    sql.append("       c.amount,\n");
    sql.append("       b.m1,\n");
    sql.append("       b.m2,\n");
    sql.append("       b.m3,\n");
    sql.append("       b.m4,\n");
    sql.append("       b.m5,\n");
    sql.append("       b.m6,\n");
    sql.append("       b.m7,\n");
    sql.append("       b.m8,\n");
    sql.append("       b.m9,\n");
    sql.append("       b.m10,\n");
    sql.append("       b.m11,\n");
    sql.append("       b.m12\n");
    
    sql.append("from (select  p.PLAN_ID,\n");
    sql.append("              org.org_code,\n");
    sql.append("              org.ORG_NAME,\n");
    sql.append("              td.DEALER_CODE,\n");
    sql.append("              td.DEALER_SHORTNAME\n");
    sql.append("         from TT_VS_YEARLY_PLAN         p,\n");
    sql.append("              TM_ORG                 org,\n");
    sql.append("              TM_DEALER              td,\n");
    sql.append("              TM_DEALER_ORG_RELATION r\n");
    sql.append("        where p.DEALER_ID = td.DEALER_ID\n");
    sql.append("          and r.org_id = org.ORG_ID\n");
    sql.append("          and td.DEALER_ID = r.DEALER_ID\n");
    sql.append("          AND p.COMPANY_ID=" + companyId + "\n");
    sql.append("          and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
    
    sql.append("          and p.DEALER_ID in\n");
    sql.append("            (select d.DEALER_ID\n");
    sql.append("               from tm_dealer d\n");
    sql.append("             start with d.DEALER_ID in\n");
    sql.append("                   (" + PlanUtil.createSqlStr(dealerId) + ")\n");
    sql.append("   connect by prior d.DEALER_ID = d.PARENT_DEALER_D)\n");
    
    sql.append("           and p.plan_year = ?\n");
    params.add(plan_year);
    sql.append("           and p.STATUS = ?\n");
    params.add(Constant.PLAN_MANAGE_CONFIRM);
    if ((plan_ver != null) && (!"".equals(plan_ver)))
    {
      sql.append("           and p.PLAN_VER = ?\n");
      params.add(plan_ver);
    }
    if ((plan_desc != null) && (!"".equals(plan_desc)))
    {
      sql.append("           and p.PLAN_DESC like ?\n");
      params.add(plan_desc);
    }
    if ((buss_area != null) && (!"".equals(buss_area)))
    {
      sql.append("           and p.area_id = ?\n");
      params.add(buss_area);
    }
    if ((planType != null) && (!"".equals(planType)))
    {
      sql.append("           and p.plan_type = ?\n");
      params.add(planType);
    }
    sql.append(") A, \n");
    sql.append("       (select d.PLAN_ID,\n");
    sql.append("               g.GROUP_CODE,\n");
    sql.append("               g.GROUP_NAME,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 1, d.SALE_AMOUNT, 0)) m1,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 2, d.SALE_AMOUNT, 0)) m2,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 3, d.SALE_AMOUNT, 0)) m3,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 4, d.SALE_AMOUNT, 0)) m4,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 5, d.SALE_AMOUNT, 0)) m5,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 6, d.SALE_AMOUNT, 0)) m6,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 7, d.SALE_AMOUNT, 0)) m7,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 8, d.SALE_AMOUNT, 0)) m8,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 9, d.SALE_AMOUNT, 0)) m9,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 10, d.SALE_AMOUNT, 0)) m10,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 11, d.SALE_AMOUNT, 0)) m11,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 12, d.SALE_AMOUNT, 0)) m12\n");
    sql.append("          from TT_VS_YEARLY_PLAN_DETAIL d, TM_VHCL_MATERIAL_GROUP g\n");
    sql.append("         where g.GROUP_ID = d.MATERIAL_GROUPID\n");
    sql.append("         group by d.PLAN_ID, g.GROUP_CODE, g.GROUP_NAME\n");
    sql.append("         order by d.PLAN_ID) B,\n");
    sql.append("       (select d1.plan_id plan_id, sum(d1.sale_amount) amount\n");
    sql.append("          from TT_VS_YEARLY_PLAN_DETAIL d1\n");
    sql.append("         group by d1.PLAN_ID) C\n");
    sql.append(" where a.PLAN_ID = b.plan_id\n");
    sql.append("   and a.plan_id = c.plan_id\n");
    sql.append("   order by a.ORG_CODE");
    
    return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
  }
  
  public List<Map<String, Object>> dealerSelectDetailYearlyPlanDown(Map<String, Object> map)
  {
    String plan_year = (String)map.get("plan_year");
    String plan_ver = (String)map.get("plan_ver");
    String plan_desc = (String)map.get("plan_desc");
    String buss_area = (String)map.get("buss_area");
    String planType = (String)map.get("planType");
    String dealerId = (String)map.get("dealerId");
    String companyId = map.get("companyId").toString();
    
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    
    sql.append("select a.ORG_CODE,\n");
    sql.append("       a.ORG_NAME,\n");
    sql.append("       a.DEALER_CODE,\n");
    sql.append("       a.DEALER_SHORTNAME,\n");
    sql.append("       b.group_code,\n");
    sql.append("       b.group_name,\n");
    sql.append("       c.amount,\n");
    sql.append("       b.m1,\n");
    sql.append("       b.m2,\n");
    sql.append("       b.m3,\n");
    sql.append("       b.m4,\n");
    sql.append("       b.m5,\n");
    sql.append("       b.m6,\n");
    sql.append("       b.m7,\n");
    sql.append("       b.m8,\n");
    sql.append("       b.m9,\n");
    sql.append("       b.m10,\n");
    sql.append("       b.m11,\n");
    sql.append("       b.m12\n");
    
    sql.append("from (select  p.PLAN_ID,\n");
    sql.append("              org.org_code,\n");
    sql.append("              org.ORG_NAME,\n");
    sql.append("              td.DEALER_CODE,\n");
    sql.append("              td.DEALER_SHORTNAME\n");
    sql.append("         from TT_VS_YEARLY_PLAN         p,\n");
    sql.append("              TM_ORG                 org,\n");
    sql.append("              TM_DEALER              td,\n");
    sql.append("              TM_DEALER_ORG_RELATION r\n");
    sql.append("        where p.DEALER_ID = td.DEALER_ID\n");
    sql.append("          and r.org_id = org.ORG_ID\n");
    sql.append("          and td.DEALER_ID = r.DEALER_ID\n");
    sql.append("          AND p.COMPANY_ID=" + companyId + "\n");
    sql.append("          and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
    
    sql.append("          and p.DEALER_ID in\n");
    sql.append("            (select d.DEALER_ID\n");
    sql.append("               from tm_dealer d\n");
    sql.append("             start with d.DEALER_ID in\n");
    sql.append("                   (" + PlanUtil.createSqlStr(dealerId) + ")\n");
    sql.append("   connect by prior d.DEALER_ID = d.PARENT_DEALER_D)\n");
    sql.append("           and p.plan_year = ?\n");
    params.add(plan_year);
    sql.append("           and p.STATUS = ?\n");
    params.add(Constant.PLAN_MANAGE_CONFIRM);
    if ((plan_ver != null) && (!"".equals(plan_ver)))
    {
      sql.append("           and p.PLAN_VER = ?\n");
      params.add(plan_ver);
    }
    if ((plan_desc != null) && (!"".equals(plan_desc)))
    {
      sql.append("           and p.PLAN_DESC like ?\n");
      params.add(plan_desc);
    }
    if ((buss_area != null) && (!"".equals(buss_area)))
    {
      sql.append("           and p.area_id = ?\n");
      params.add(buss_area);
    }
    if ((planType != null) && (!"".equals(planType)))
    {
      sql.append("           and p.plan_type = ?\n");
      params.add(planType);
    }
    sql.append(") A, \n");
    sql.append("       (select d.PLAN_ID,\n");
    sql.append("               g.GROUP_CODE,\n");
    sql.append("               g.GROUP_NAME,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 1, d.SALE_AMOUNT, 0)) m1,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 2, d.SALE_AMOUNT, 0)) m2,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 3, d.SALE_AMOUNT, 0)) m3,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 4, d.SALE_AMOUNT, 0)) m4,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 5, d.SALE_AMOUNT, 0)) m5,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 6, d.SALE_AMOUNT, 0)) m6,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 7, d.SALE_AMOUNT, 0)) m7,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 8, d.SALE_AMOUNT, 0)) m8,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 9, d.SALE_AMOUNT, 0)) m9,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 10, d.SALE_AMOUNT, 0)) m10,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 11, d.SALE_AMOUNT, 0)) m11,\n");
    sql.append("               sum(decode(d.PLAN_MONTH, 12, d.SALE_AMOUNT, 0)) m12\n");
    sql.append("          from TT_VS_YEARLY_PLAN_DETAIL d, TM_VHCL_MATERIAL_GROUP g\n");
    sql.append("         where g.GROUP_ID = d.MATERIAL_GROUPID\n");
    sql.append("         group by d.PLAN_ID, g.GROUP_CODE, g.GROUP_NAME\n");
    sql.append("         order by d.PLAN_ID) B,\n");
    sql.append("       (select d1.plan_id plan_id, sum(d1.sale_amount) amount\n");
    sql.append("          from TT_VS_YEARLY_PLAN_DETAIL d1\n");
    sql.append("         group by d1.PLAN_ID) C\n");
    sql.append(" where a.PLAN_ID = b.plan_id\n");
    sql.append("   and a.plan_id = c.plan_id\n");
    sql.append("   order by a.ORG_CODE");
    
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> getTempDownload(String companyId, String areaId)
  {
    StringBuffer sql = new StringBuffer();
    
    sql.append("SELECT A.ORG_CODE, A.ORG_NAME, B.GROUP_CODE, B.GROUP_NAME\n");
    sql.append("  FROM (SELECT TOR.ORG_ID, TOR.ORG_CODE, TOR.ORG_NAME\n");
    sql.append("          FROM TM_ORG TOR,TM_ORG_BUSINESS_AREA C\n");
    sql.append("         WHERE TOR.ORG_ID = C.ORG_ID \n");
    sql.append("           AND TOR.ORG_TYPE = " + Constant.ORG_TYPE_OEM + "\n");
    sql.append("           AND TOR.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("           AND TOR.DUTY_TYPE = " + Constant.DUTY_TYPE_LARGEREGION + "\n");
    sql.append("           AND C.AREA_ID = ").append(areaId).append("\n");
    sql.append("           AND TOR.COMPANY_ID = " + companyId + ") A,\n");
    sql.append("       (SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
    sql.append("          FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
    sql.append("         WHERE TVMG.GROUP_LEVEL = 2\n");
    sql.append("           AND TVMG.COMPANY_ID = " + companyId + "\n");
    sql.append("           AND TVMG.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("         START WITH TVMG.GROUP_ID IN\n");
    sql.append("                    (SELECT TAG.MATERIAL_GROUP_ID\n");
    sql.append("                       FROM TM_AREA_GROUP TAG\n");
    sql.append("                      WHERE TAG.AREA_ID = " + areaId + ")\n");
    sql.append("        CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID ---������������������������������\n");
    sql.append("        UNION\n");
    sql.append("        SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
    sql.append("          FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
    sql.append("         WHERE TVMG.GROUP_LEVEL = 2\n");
    sql.append("           AND TVMG.COMPANY_ID = " + companyId + "\n");
    sql.append("           AND TVMG.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("         START WITH TVMG.GROUP_ID IN\n");
    sql.append("                    (SELECT TAG.MATERIAL_GROUP_ID\n");
    sql.append("                       FROM TM_AREA_GROUP TAG\n");
    sql.append("                      WHERE TAG.AREA_ID = " + areaId + ")\n");
    sql.append("        CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID) B\n");
    sql.append("ORDER BY A.ORG_CODE, B.GROUP_CODE");
    
    return dao.pageQuery(sql.toString(), null, getFunName());
  }
  
  public List<Map<String, Object>> getGroupByOrg(String orgId, String companyId, String areaId)
  {
    StringBuffer sql = new StringBuffer("\n");
    sql.append("SELECT distinct B.GROUP_CODE, B.GROUP_NAME FROM  ");
    sql.append("       (SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
    sql.append("          FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
    sql.append("         WHERE TVMG.GROUP_LEVEL = 2\n");
    /*if ((companyId != null) && (!"".equals(companyId))) {
      sql.append("           AND TVMG.COMPANY_ID = " + companyId + "\n");
    }*/
    sql.append("           AND TVMG.STATUS =  " + Constant.STATUS_ENABLE + "\n");
   /* sql.append("         START WITH TVMG.GROUP_ID IN\n");
    sql.append("                    (SELECT TAG.MATERIAL_GROUP_ID\n");
    sql.append("                       FROM TM_AREA_GROUP TAG\n");
    if ((areaId != null) && (!"".equals(areaId))) {
      sql.append("                      WHERE TAG.AREA_ID = " + areaId + ")\n");
    }
    sql.append("        CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID \n");*/
    sql.append("        ) B\n");
    sql.append("  ORDER BY B.GROUP_CODE, B.GROUP_NAME");
    return dao.pageQuery(sql.toString(), null, getFunName());
  }
  
  public List<Map<String, Object>> getTempDownloadByOrg(String orgId, String companyId, String dealerId)
  {
    StringBuffer sql = new StringBuffer("\n");
    sql.append("SELECT distinct A.ORG_NAME, A.DEALER_CODE, A.DEALER_NAME,A.REGION_NAME\n");
    sql.append("  FROM (SELECT D.DEALER_ID, D.DEALER_CODE, D.DEALER_NAME, VOD.ROOT_ORG_NAME ORG_NAME,VOD.PQ_ORG_NAME REGION_NAME\n");
    sql.append("          FROM                           \n");
    sql.append("               TM_DEALER               D,\n");
    // sql.append("               TM_DEALER_BUSINESS_AREA C,\n");
    sql.append("               VW_ORG_DEALER_ALL_NEW VOD\n");
    sql.append("         WHERE 1=1 \n");
    sql.append("           AND D.DEALER_ID = VOD.ROOT_DEALER_ID\n");
    // sql.append("           AND TCR.ROLE_ID=TRP.ROLE_ID AND TCR.Role_Desc NOT  LIKE '%撤站%' AND TCP.COMPANY_ID=D.COMPANY_ID AND TCP.POSE_ID=TRP.POSE_ID\n");
    sql.append("           AND D.DEALER_TYPE IN (10771001, 10771004)\n");
    sql.append("           AND D.STATUS = " + Constant.STATUS_ENABLE + "\n");
    if ((orgId != null) && (!"".equals(orgId))) {
      sql.append("\t   AND A.ORG_ID = ").append(orgId).append("\n");
    }
    sql.append("           AND exists(" + dealerId + " and v.DEALER_ID=VOD.dealer_id)\n");
   /* if ((areaId != null) && (!"".equals(areaId))) {
      sql.append("           AND C.AREA_ID = " + areaId + "\n");
    }*/
    //sql.append("           AND D.DEALER_CLASS = " + Constant.DEALER_CLASS_TYPE_12 + "\n");
    /*if ((companyId != null) && (!"".equals(companyId))) {
      sql.append("           AND D.OEM_COMPANY_ID = " + companyId + "\n");
    }*/
    sql.append("\t\t) A\n");
    sql.append(" ORDER BY A.ORG_NAME, A.DEALER_CODE\n");
    return dao.pageQuery(sql.toString(), null, getFunName());
  }
  
  public List<Map<String, Object>> oemMonthPlanCheckDlr(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    String companyId = map.get("companyId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    sql.append("select p.ROW_NUMBER\n");
    sql.append("  from TMP_VS_YEAR_PLAN_NEW p\n");
    sql.append(" where 1 = 1\n");
    sql.append("   and p.PLAN_YEAR = " + year + "\n");
    sql.append("   and p.USER_ID =" + userId + "\n");
    sql.append("   and p.DEALER_CODE is not null\n");
    sql.append("   and p.ORG_CODE is null\n");
    sql.append("   and not exists (select 1\n");
    sql.append("          from tm_dealer td\n");
    sql.append("         where td.DEALER_CODE = p.DEALER_CODE\n");
    sql.append("           and td.OEM_COMPANY_ID = " + companyId + "\n");
    sql.append("           and td.STATUS = " + Constant.STATUS_ENABLE + "\n");
    
    sql.append("          )\n");
    sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> sbuMonthPlanCheckDlrArea(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    String companyId = map.get("companyId").toString();
    String areaId = map.get("areaId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    sql.append("select p.ROW_NUMBER\n");
    sql.append("  from TMP_VS_YEAR_PLAN_NEW p\n");
    sql.append(" where 1 = 1\n");
    sql.append("   and p.PLAN_YEAR = " + year + "\n");
    sql.append("   and p.USER_ID =" + userId + "\n");
    sql.append("   and p.DEALER_CODE is not null\n");
    sql.append("   and p.ORG_CODE is null\n");
    sql.append("   and not exists (select 1\n");
    sql.append("        from TM_DEALER td, TM_DEALER_BUSINESS_AREA area\n");
    sql.append("        where td.DEALER_CODE = p.DEALER_CODE\n");
    sql.append("           and td.DEALER_ID = area.DEALER_ID\n");
    sql.append("           and td.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("           and area.AREA_ID = " + areaId + "\n");
    sql.append("           and td.OEM_COMPANY_ID = " + companyId + "\n");
    sql.append("           )\n");
    
    sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> oemMonthPlanCheckGroup(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    String companyId = map.get("companyId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    sql.append("select p.ROW_NUMBER\n");
    sql.append("  from TMP_VS_YEAR_PLAN_NEW p\n");
    sql.append(" where 1 = 1\n");
    sql.append("   and p.PLAN_YEAR = " + year + "\n");
    sql.append("   and p.USER_ID =" + userId + "\n");
    sql.append("   and p.DEALER_CODE is null\n");
    sql.append("   and not exists (select 1\n");
    sql.append("       from TM_VHCL_MATERIAL_GROUP g\n");
    sql.append("      where g.GROUP_CODE = p.GROUP_CODE\n");
    sql.append("        and g.STATUS = " + Constant.STATUS_ENABLE + "\n");
    sql.append("        and g.COMPANY_ID = " + companyId + ")\n");
    sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> oemMonthPlanCheckGroupArea(Map<String, Object> map)
  {
    String year = map.get("year").toString();
    String userId = map.get("userId").toString();
    String groupArea = map.get("groupArea").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    sql.append("select p.ROW_NUMBER\n");
    sql.append("  from TMP_VS_YEAR_PLAN_NEW p\n");
    sql.append(" where 1 = 1\n");
    sql.append("   and p.PLAN_YEAR = " + year + "\n");
    sql.append("   and p.USER_ID = " + userId + "\n");
    sql.append("   and p.DEALER_CODE is null\n");
    sql.append("   and p.GROUP_CODE not in(" + PlanUtil.createSqlStr(groupArea) + ")\n");
    sql.append(" order by TO_NUMBER(p.ROW_NUMBER) asc");
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> talbeCheckDump(String year, String userId)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    sql.append("select p1.ROW_NUMBER ROW_NUMBER1, p2.ROW_NUMBER ROW_NUMBER2\n");
    sql.append("  from TMP_VS_YEAR_PLAN_NEW p1, TMP_VS_YEAR_PLAN_NEW p2\n");
    sql.append(" where p1.org_CODE = p2.org_CODE\n");
    sql.append("   and p1.GROUP_CODE = p2.GROUP_CODE\n");
    sql.append("   and p1.ROW_NUMBER <> p2.ROW_NUMBER\n");
    sql.append("   and p1.PLAN_YEAR = p2.PLAN_YEAR\n");
    sql.append("   and p1.USER_ID = p2.USER_ID\n");
    sql.append("   and p1.ORG_CODE is not null\n");
    sql.append("   and p1.PLAN_YEAR = ?\n");
    params.add(year);
    sql.append("   and p1.USER_ID=?");
    params.add(userId);
    List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
    return list;
  }
  
  public List<Map<String, Object>> oemSelectTmpMonthPlan(String year, Long userId)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    sql.append("select d.DEALER_CODE,d.DEALER_SHORTNAME, g.GROUP_CODE, g.GROUP_NAME, plan.SUM_AMT\n");
    sql.append("  from TMP_VS_YEAR_PLAN_NEW plan, TM_DEALER d, TM_VHCL_MATERIAL_GROUP g\n");
    sql.append(" where plan.DEALER_CODE = d.DEALER_CODE\n");
    sql.append("   and plan.GROUP_CODE = g.GROUP_CODE\n");
    sql.append("   and plan.PLAN_YEAR = ?\n");
    params.add(year);
    sql.append("   and plan.USER_ID = ?\n");
    params.add(userId);
    sql.append(" order by plan.dealer_code,plan.group_code");

    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public PageResult<Map<String, Object>> selectUnconfirmYearPlan(Map<String, Object> map, int curPage, int pageSize)
  {
    List<Object> params = new LinkedList();
    String companyId = map.get("companyId").toString();
    String dealerId = map.get("dealerId").toString();
    StringBuffer sql = new StringBuffer("");
    sql.append("select plan.PLAN_YEAR,\n");
    sql.append("       plan.company_id AREA_ID,\n");
    sql.append("       plan.PLAN_TYPE,\n");
    sql.append("       plan.PLAN_YEAR as PLAN_YM,\n");
    sql.append("      tcy.company_shortname AREA_NAME,\n");
    sql.append("       sum(detail.SALE_AMOUNT) amount,\n");
    sql.append("       tc.CODE_DESC\n");
    sql.append("  from TT_VS_YEARLY_PLAN        plan,\n");
    sql.append("       TT_VS_YEARLY_PLAN_DETAIL detail,\n");
    sql.append("       TC_CODE                   tc,\n");
    sql.append("       TM_DEALER TMD,\n");
    sql.append("       tm_company tcy \n");
    sql.append(" where plan.PLAN_Id = detail.PLAN_ID\n");
    sql.append("   and plan.STATUS = tc.CODE_ID\n");
    sql.append("   and PLAN.DEALER_ID=TMD.DEALER_ID");
    sql.append("   and plan.company_id=tcy.company_id ");
    sql.append("   and plan.COMPANY_ID = " + companyId + "\n");
    sql.append("   and exists( " + dealerId + " and v.DEALER_ID=plan.dealer_id)\n");
   //  sql.append("   and plan.AREA_ID = " + areaId + "\n");
    sql.append("   and plan.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
    sql.append("   and plan.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM + "\n");
    sql.append("   and tmd.status=" + Constant.STATUS_ENABLE + "\n");
    sql.append("group by plan.PLAN_YEAR,\n");
    sql.append("         plan.PLAN_TYPE,\n");
    sql.append("         plan.company_id,\n");
    sql.append("        tcy.company_shortname,\n");
    sql.append("         tc.CODE_DESC");
    PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
    return ps;
  }
  
  public List<Map<String, Object>> oemSelectUnconfirmMonthPlan(String year, Long userId, String companyId, String dealerId, String planType)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    sql.append("select td.DEALER_SHORTNAME,\n");
    sql.append("       td.DEALER_CODE,\n");
    sql.append("       material.GROUP_CODE,\n");
    sql.append("       material.GROUP_NAME,\n");
    sql.append("       detail.SALE_AMOUNT\n");
    sql.append("  from TM_DEALER                 td,\n");
    sql.append("       TM_VHCL_MATERIAL_GROUP material,\n");
    sql.append("       TT_VS_YEARLY_PLAN        plan,\n");
    sql.append("       TT_VS_YEARLY_PLAN_DETAIL detail\n");
    sql.append(" where TD.DEALER_ID = plan.DEALER_ID\n");
    sql.append("   and plan.PLAN_ID = detail.PLAN_ID\n");
    sql.append("   and material.GROUP_ID = detail.MATERIAL_GROUPID\n");
   //  sql.append("   and plan.AREA_ID=" + areaId + "\n");
    sql.append("   and exists(" + dealerId + " and v.DEALER_ID=plan.dealer_id)\n");
    sql.append("   and plan.COMPANY_ID=" + companyId + "\n");
    sql.append("   and plan.PLAN_YEAR = " + year + "\n");
    sql.append("   and plan.STATUS = " + Constant.PLAN_MANAGE_UNCONFIRM);
   //  sql.append("   and plan.PLAN_TYPE=" + planType + "\n");
    sql.append("   order by td.DEALER_CODE asc ");
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public Integer selectMaxPlanVer(String year, Long userId, String companyId, String dealerId, String planType)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    sql.append("select nvl(max(plan.plan_ver),0) plan_ver\n");
    sql.append("  from TT_VS_YEARLY_PLAN plan\n");
    sql.append(" where 1=1\n");
    sql.append("   and plan.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
    // sql.append("   and plan.AREA_ID=" + areaId + "\n");
    sql.append("   and plan.COMPANY_ID=" + companyId + "\n");
    sql.append("   and exists (" + dealerId + " and v.DEALER_ID=plan.dealer_id)\n");
    sql.append("   and plan.PLAN_YEAR = " + year + "\n");
    sql.append("   and plan.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
   //  sql.append("   and plan.PLAN_TYPE = " + planType + "\n");
    Map<String, Object> map = dao.pageQueryMap(sql.toString(), params, getFunName());
    Integer planVer = new Integer(map.get("PLAN_VER").toString());
    return planVer;
  }
  
  public Integer setUnEnable(String year, Long userId, String companyId, String dealerId, String planType)
  {
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer("");
    sql.append("  update TT_VS_YEARLY_PLAN plan set plan.plan_desc = " + Constant.STATUS_DISABLE + "\n");
    sql.append(" where 1=1\n");
    sql.append("   and plan.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
    // sql.append("   and plan.AREA_ID=" + areaId + "\n");
    sql.append("   and plan.COMPANY_ID=" + companyId + "\n");
    sql.append("   and plan.PLAN_YEAR = " + year + "\n");
    sql.append("   and plan.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
    // sql.append("   and plan.PLAN_TYPE = " + planType + "\n");
    sql.append("   and exists (" + dealerId + " and v.DEALER_ID=plan.dealer_id)\n");
    return Integer.valueOf(super.update(sql.toString(), null));
  }
  
  public PageResult<Map<String, Object>> oemSelectDealerYearPlan(String dutyType, String org_id, Map<String, Object> map, int pageSize, int curPage)
  {
    String plan_year = (String)map.get("plan_year");
    String plan_ver = (String)map.get("plan_ver");
    String plan_desc = (String)map.get("plan_desc");
    String dealerCode = (String)map.get("dealerCode");
    String logonOrgType = (String)map.get("logonOrgType");
    String logonOrgId = (String)map.get("logonOrgId");
    // String companyId = map.get("companyId").toString();
    String dealerId = (String)map.get("dealerId");
    // String planType = map.get("planType").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    sql.append("select ORG.PQ_ORG_CODE AS ORG_CODE,\n");
    sql.append("       ORG.PQ_ORG_NAME as ORG_NAME,\n");
    sql.append("       ORG.ROOT_DEALER_CODE AS DEALER_CODE,\n");
    sql.append("       ORG.ROOT_DEALER_SHORTNAME AS DEALER_SHORTNAME,\n");
    sql.append("       p.PLAN_TYPE,\n");
    sql.append("       d.MATERIAL_GROUPID,\n");
    sql.append("       g.GROUP_NAME,\n");
    sql.append("       g.GROUP_CODE,\n");
    sql.append("       nvl(sum(d.SALE_AMOUNT), 0) SALE_AMOUNT\n");
    sql.append("  from TT_VS_YEARLY_PLAN        p,\n");
    sql.append("       TT_VS_YEARLY_PLAN_DETAIL d,\n");
    sql.append("       TM_VHCL_MATERIAL_GROUP    g,\n");
    sql.append("       VW_ORG_DEALER_ALL_NEW             ORG\n");
    sql.append(" where p.PLAN_ID = d.PLAN_ID\n");
    sql.append("   and p.DEALER_ID = ORG.DEALER_ID\n");
   /* if ((areaId != null) && (!"".equals(areaId))) {
      sql.append("   and p.AREA_ID = " + areaId + "\n");
    }*/
    sql.append("   and d.MATERIAL_GROUPID = g.GROUP_ID\n");
    sql.append("   and exists(" + dealerId + " and v.DEALER_ID=p.dealer_id)\n");
    // sql.append("   and p.COMPANY_ID=" + companyId + "\n");
    sql.append("   and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
    sql.append("   and p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
    if (Constant.DUTY_TYPE_COMPANY.equals(new Integer(dutyType))) {
      sql.append("   AND ORG.COMPANY_ID = " + org_id + "\n");
    }
    if (Constant.DUTY_TYPE_LARGEREGION.equals(new Integer(dutyType))) {
      sql.append("   AND ORG.ROOT_ORG_ID = " + org_id + "\n");
    }
    if (Constant.DUTY_TYPE_SMALLREGION.equals(new Integer(dutyType))) {
      sql.append("   AND ORG.PQ_ORG_ID = " + org_id + "\n");
    }
    if ((plan_ver != null) && (!"".equals(plan_ver)))
    {
      sql.append("   and p.PLAN_VER = ?\n");
      params.add(plan_ver);
    }
    sql.append("   and p.PLAN_YEAR = ?\n");
    params.add(plan_year);
    if ((plan_desc != null) && (!"".equals(plan_desc)))
    {
      sql.append("           and p.PLAN_DESC like ?\n");
      params.add(plan_desc);
    }
    /*if ((planType != null) && (!"".equals(planType))) {
      sql.append("   and p.plan_Type = " + planType + "\n");
    }*/
    if ((dealerCode != null) && (!"".equals(dealerCode))) {
      sql.append("           and ORG.ROOT_dealer_code in (" + PlanUtil.createSqlStr(dealerCode) + ")\n");
    }
    sql.append("group by ORG.PQ_ORG_CODE,\n");
    sql.append("         ORG.PQ_ORG_NAME,\n");
    sql.append("         ORG.ROOT_DEALER_CODE,\n");
    sql.append("         ORG.ROOT_DEALER_SHORTNAME,\n");
    sql.append("         p.PLAN_TYPE,\n");
    sql.append("         d.MATERIAL_GROUPID,\n");
    sql.append("         g.GROUP_NAME,\n");
    sql.append("         g.GROUP_CODE\n");
    sql.append("order by ORG.ROOT_DEALER_SHORTNAME,ORG.ROOT_DEALER_CODE\n");
    return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
  }
  
  public PageResult<Map<String, Object>> oemSelectDealerYearPlanTotal(String dutyType, String org_id, Map<String, Object> map, List<Map<String, Object>> serlist, int pageSize, int curPage)
  {
    String plan_year = (String)map.get("plan_year");
    String plan_ver = (String)map.get("plan_ver");
    String plan_desc = (String)map.get("plan_desc");
    String dealerCode = (String)map.get("dealerCode");
    String logonOrgType = (String)map.get("logonOrgType");
    String logonOrgId = (String)map.get("logonOrgId");
    // String companyId = map.get("companyId").toString();
    String dealerId = map.get("dealerId").toString();
    // String planType = map.get("planType").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    sql.append("\n");
    sql.append("SELECT YEARLY.DEALER_CODE,\n");
    sql.append("       YEARLY.DEALER_SHORTNAME,\n");
    sql.append("       null PLAN_TYPE,\n");
    for (int i = 0; i < serlist.size(); i++)
    {
      Map<String, Object> sermap = (Map)serlist.get(i);
      sql.append("       nvl(MAX(DECODE(YEARLY.MATERIAL_GROUPID,\n");
      sql.append("                  " + sermap.get("GROUP_ID") + ",\n");
      sql.append("                  YEARLY.SALE_AMOUNT)),0) S" + i + ",\n");
    }
    sql.append("       nvl(sum(yearly.SALE_AMOUNT),0) SALE_AMOUNT,\n");
    sql.append("       YEARLY.DEALER_ID\n");
    sql.append("  FROM (SELECT TVYP.DEALER_ID,\n");
    sql.append("               D.ROOT_DEALER_CODE AS DEALER_CODE,\n");
    sql.append("               D.ROOT_DEALER_SHORTNAME AS DEALER_SHORTNAME,\n");
   // sql.append("               TVYP.PLAN_TYPE,\n");
    sql.append("               TVYPD.MATERIAL_GROUPID,\n");
    sql.append("               nvl(SUM(TVYPD.SALE_AMOUNT),0) AS SALE_AMOUNT\n");
    sql.append("          FROM TT_VS_YEARLY_PLAN        TVYP,\n");
    sql.append("               TT_VS_YEARLY_PLAN_DETAIL TVYPD,\n");
    sql.append("      \t\t   VW_ORG_DEALER_ALL_NEW             D\n");
    sql.append("         WHERE TVYP.PLAN_ID = TVYPD.PLAN_ID\n");
    sql.append("           AND TVYP.DEALER_ID = D.DEALER_ID\n");
   /* if ((areaId != null) && (!"".equals(areaId))) {
      sql.append("           AND TVYP.AREA_ID=" + areaId + "\n");
    }*/
    sql.append("           AND  exists(" +  map.get("dealerId").toString() + " and v.DEALER_ID=TVYP.dealer_id)\n");
    // sql.append("           AND TVYP.COMPANY_ID=" + companyId + "\n");
    sql.append("           AND TVYP.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
    if (Constant.DUTY_TYPE_COMPANY.equals(new Integer(dutyType))) {
      sql.append("   AND D.COMPANY_ID = " + org_id + "\n");
    }
    if (Constant.DUTY_TYPE_LARGEREGION.equals(new Integer(dutyType))) {
      sql.append("   AND D.ROOT_ORG_ID = " + org_id + "\n");
    }
    if (Constant.DUTY_TYPE_SMALLREGION.equals(new Integer(dutyType))) {
      sql.append("   AND D.PQ_ORG_ID = " + org_id + "\n");
    }
    sql.append("           and TVYP.plan_year = " + plan_year + "\n");
    sql.append("           and TVYP.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
    if ((plan_ver != null) && (!"".equals(plan_ver)))
    {
      sql.append("           and TVYP.PLAN_VER = " + plan_ver + "\n");
      params.add(plan_ver);
    }
    /*if ((planType != null) && (!"".equals(planType))) {
      sql.append("           and TVYP.PLAN_TYPE = " + planType + "\n");
    }*/
    if ((plan_desc != null) && (!"".equals(plan_desc))) {
      sql.append("           and TVYP.PLAN_DESC like " + plan_desc + "\n");
    }
    if ((dealerCode != null) && (!"".equals(dealerCode))) {
      sql.append("           and D.ROOT_dealer_code in (" + PlanUtil.createSqlStr(dealerCode) + ")\n");
    }
    sql.append("         GROUP BY TVYP.DEALER_ID,\n");
    sql.append("                  TVYPD.MATERIAL_GROUPID,\n");
    sql.append("                  D.ROOT_DEALER_CODE,\n");
    sql.append("                  D.ROOT_DEALER_SHORTNAME\n");
    sql.append("                  ) YEARLY\n");
    sql.append(" GROUP BY DEALER_ID, YEARLY.DEALER_CODE, YEARLY.DEALER_SHORTNAME\n");
    sql.append(" order by dealer_id");
    return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
  }
  
  public List<Map<String, Object>> oemSelectDealerTotalYearPlanTotalDown(Map<String, Object> map, List<Map<String, Object>> serlist)
  {
    String plan_year = (String)map.get("plan_year");
    String plan_ver = (String)map.get("plan_ver");
    String plan_desc = (String)map.get("plan_desc");
    String dealerCode = (String)map.get("dealerCode");
    String logonOrgType = (String)map.get("logonOrgType");
    String logonOrgId = (String)map.get("logonOrgId");
    // String companyId = map.get("companyId").toString();
   // String areaId = map.get("buss_area").toString();
    String dealerId =  map.get("dealerId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    // String planType = map.get("planType").toString();
    sql.append("\n");
    sql.append("SELECT YEARLY.DEALER_CODE,\n");
    sql.append("       YEARLY.DEALER_SHORTNAME,\n");
    // sql.append("       YEARLY.PLAN_TYPE,\n");
    for (int i = 0; i < serlist.size(); i++)
    {
      Map<String, Object> sermap = (Map)serlist.get(i);
      sql.append("       nvl(MAX(DECODE(YEARLY.MATERIAL_GROUPID,\n");
      sql.append("                  " + sermap.get("GROUP_ID") + ",\n");
      sql.append("                  YEARLY.SALE_AMOUNT)),0) S" + i + ",\n");
    }
    sql.append("       nvl(sum(yearly.SALE_AMOUNT),0) SALE_AMOUNT,\n");
    sql.append("       YEARLY.DEALER_ID\n");
    sql.append("  FROM (SELECT TVYP.DEALER_ID,\n");
    sql.append("               TD.DEALER_CODE,\n");
    sql.append("               TD.DEALER_SHORTNAME,\n");
   // sql.append("               TC.CODE_DESC PLAN_TYPE,\n");
    sql.append("               TVYPD.MATERIAL_GROUPID,\n");
    sql.append("               nvl(SUM(TVYPD.SALE_AMOUNT),0) AS SALE_AMOUNT\n");
    sql.append("          FROM TT_VS_YEARLY_PLAN        TVYP,\n");
    sql.append("               TT_VS_YEARLY_PLAN_DETAIL TVYPD,\n");
    sql.append("               TM_DEALER                TD,\n");
   // sql.append("               TC_CODE                TC,\n");
    sql.append("\t\t\t   \t  VW_ORG_DEALER_ALL_NEW          D\n");
    sql.append("         WHERE TVYP.PLAN_ID = TVYPD.PLAN_ID\n");
    sql.append("           AND TVYP.DEALER_ID = TD.DEALER_ID\n");
    sql.append("\t\t   AND TD.DEALER_ID = D.DEALER_ID");
    if (Constant.DUTY_TYPE_COMPANY.equals(new Integer(map.get("dutyType").toString()))) {
      sql.append("   AND D.COMPANY_ID = " + map.get("org_id") + "\n");
    }
    if (Constant.DUTY_TYPE_LARGEREGION.equals(new Integer(map.get("dutyType").toString()))) {
      sql.append("   AND D.ROOT_ORG_ID = " + map.get("org_id") + "\n");
    }
    if (Constant.DUTY_TYPE_SMALLREGION.equals(new Integer(map.get("dutyType").toString()))) {
      sql.append("   AND D.PQ_ORG_ID = " + map.get("org_id") + "\n");
    }
    // sql.append("           AND TVYP.PLAN_TYPE=TC.CODE_ID\n");
    // sql.append("           AND TVYP.AREA_ID=" + areaId + "\n");
    sql.append("           AND exists(" + dealerId + " and v.DEALER_ID=TVYP.dealer_id)\n");
   //  sql.append("           AND TVYP.COMPANY_ID=" + companyId + "\n");
    sql.append("           AND TVYP.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
    if ((logonOrgId != null) && (logonOrgType != null) && (!"".equals(logonOrgId)) && ("LARGEREGION".equals(logonOrgType)))
    {
      sql.append("          and TVYP.dealer_id in\n");
      sql.append("       (select m.dealer_id\n");
      sql.append("          from tm_dealer m\n");
      sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n");
      sql.append("         START WITH m.status=" + Constant.STATUS_ENABLE + " and m.dealer_id in\n");
      sql.append("                    (select rel.dealer_id\n");
      sql.append("                       from vw_org_dealer rel\n");
      sql.append("                      where rel.root_org_id =" + logonOrgId + "))");
    }
    sql.append("           and TVYP.plan_year = ?\n");
    params.add(plan_year);
    sql.append("           and TVYP.STATUS = ?\n");
    params.add(Constant.PLAN_MANAGE_CONFIRM);
    if ((plan_ver != null) && (!"".equals(plan_ver)))
    {
      sql.append("           and TVYP.PLAN_VER = ?\n");
      params.add(plan_ver);
    }
   /* if ((planType != null) && (!"".equals(planType)))
    {
      sql.append("           and TVYP.PLAN_TYPE = ?\n");
      params.add(planType);
    }*/
    if ((plan_desc != null) && (!"".equals(plan_desc)))
    {
      sql.append("           and TVYP.PLAN_DESC like ?\n");
      params.add(plan_desc);
    }
    if ((dealerCode != null) && (!"".equals(dealerCode))) {
      sql.append("           and td.dealer_code in (" + PlanUtil.createSqlStr(dealerCode) + ")\n");
    }
    sql.append("         GROUP BY TVYP.DEALER_ID,\n");
    sql.append("                  TVYPD.MATERIAL_GROUPID,\n");
    sql.append("                  TD.DEALER_CODE,\n");
    sql.append("                  TD.DEALER_SHORTNAME\n");
    sql.append("                  ) YEARLY\n");
    sql.append(" GROUP BY DEALER_ID, YEARLY.DEALER_CODE, YEARLY.DEALER_SHORTNAME\n");
    sql.append(" order by dealer_id");
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
  
  public List<Map<String, Object>> oemSelectDealerDetailMonthPlanDown(Map<String, Object> map)
  {
    String plan_year = (String)map.get("plan_year");
    String plan_ver = (String)map.get("plan_ver");
    String plan_desc = (String)map.get("plan_desc");
    String dealerCode = (String)map.get("dealerCode");
    String logonOrgType = (String)map.get("logonOrgType");
    String logonOrgId = (String)map.get("logonOrgId");
    // String companyId = map.get("companyId").toString();
    // String areaId = map.get("buss_area").toString();
   // String planType = map.get("planType").toString();
    String dealerId = map.get("dealerId").toString();
    List<Object> params = new LinkedList();
    StringBuffer sql = new StringBuffer();
    sql.append("select td.DEALER_CODE,\n");
    sql.append("       td.DEALER_SHORTNAME,\n");
    sql.append("       ORG.ORG_CODE,\n");
    sql.append("       ORG.ORG_NAME,\n");
    // sql.append("       TC.CODE_DESC PLAN_TYPE,\n");
    sql.append("       d.MATERIAL_GROUPID,\n");
    sql.append("       g.GROUP_NAME,\n");
    sql.append("       g.GROUP_CODE,\n");
    sql.append("       nvl(sum(d.SALE_AMOUNT),0) SALE_AMOUNT\n");
    sql.append("  from TT_VS_YEARLY_PLAN        p,\n");
    sql.append("       TT_VS_YEARLY_PLAN_DETAIL d,\n");
    sql.append("       TM_DEALER                 td,\n");
    // sql.append("       TC_CODE                 TC,\n");
    sql.append("       TM_ORG ORG,\n");
    sql.append("\t   \t  VW_ORG_DEALER_ALL_NEW          DORG,\n");
    sql.append("       TM_DEALER_ORG_RELATION TDOR,\n");
    sql.append("       TM_VHCL_MATERIAL_GROUP    g\n");
    sql.append(" where p.PLAN_ID = d.PLAN_ID\n");
    sql.append("   and p.DEALER_ID = td.DEALER_ID\n");
    // sql.append("   and p.PLAN_TYPE = TC.CODE_ID\n");
    sql.append("   and TD.DEALER_ID = TDOR.DEALER_ID\n");
    sql.append("   and DORG.DEALER_ID = TD.DEALER_ID\n");
    if (Constant.DUTY_TYPE_COMPANY.equals(new Integer(map.get("dutyType").toString()))) {
      sql.append("   AND DORG.COMPANY_ID = " + map.get("org_id") + "\n");
    }
    if (Constant.DUTY_TYPE_LARGEREGION.equals(new Integer(map.get("dutyType").toString()))) {
      sql.append("   AND DORG.ROOT_ORG_ID = " + map.get("org_id") + "\n");
    }
    if (Constant.DUTY_TYPE_SMALLREGION.equals(new Integer(map.get("dutyType").toString()))) {
      sql.append("   AND DORG.PQ_ORG_ID = " + map.get("org_id") + "\n");
    }
    sql.append("   and ORG.ORG_ID = TDOR.ORG_ID\n");
    // sql.append("   and p.AREA_ID = " + areaId + "\n");
    sql.append("   and d.MATERIAL_GROUPID = g.GROUP_ID\n");
    sql.append("   AND exists (" + dealerId + " and v.DEALER_ID=p.dealer_id)\n");
    // sql.append("   and p.COMPANY_ID=" + companyId + "\n");
    sql.append("   and p.ORG_TYPE = " + Constant.ORG_TYPE_DEALER + "\n");
    sql.append("   and p.STATUS = " + Constant.PLAN_MANAGE_CONFIRM + "\n");
    if ((plan_ver != null) && (!"".equals(plan_ver)))
    {
      sql.append("   and p.PLAN_VER = ?\n");
      params.add(plan_ver);
    }
    sql.append("   and p.PLAN_YEAR = ?\n");
    params.add(plan_year);
    if ((logonOrgId != null) && (logonOrgType != null) && (!"".equals(logonOrgId)) && ("LARGEREGION".equals(logonOrgType)))
    {
      sql.append("          and p.dealer_id in\n");
      sql.append("       (select m.dealer_id\n");
      sql.append("          from tm_dealer m\n");
      sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n");
      sql.append("         START WITH m.dealer_id in\n");
      sql.append("                    (select rel.dealer_id\n");
      sql.append("                       from vw_org_dealer rel\n");
      sql.append("                      where rel.root_org_id = " + logonOrgId + "))");
    }
    if ((plan_desc != null) && (!"".equals(plan_desc)))
    {
      sql.append("           and p.PLAN_DESC like ?\n");
      params.add(plan_desc);
    }
    /*if ((planType != null) && (!"".equals(planType))) {
      sql.append("   and p.plan_Type = " + planType + "\n");
    }*/
    if ((dealerCode != null) && (!"".equals(dealerCode))) {
      sql.append("           and td.dealer_code in (" + PlanUtil.createSqlStr(dealerCode) + ")\n");
    }
    sql.append(" group by td.DEALER_CODE,\n");
    sql.append("          td.DEALER_SHORTNAME,\n");
    sql.append("          \n");
    sql.append("          d.MATERIAL_GROUPID,\n");
    sql.append("          g.GROUP_NAME,ORG.ORG_CODE,ORG.ORG_NAME,\n");
    sql.append("          g.GROUP_CODE\n");
    sql.append("   order by td.DEALER_CODE");
    return dao.pageQuery(sql.toString(), params, getFunName());
  }
}
