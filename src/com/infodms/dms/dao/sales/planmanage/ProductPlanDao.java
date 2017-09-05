package com.infodms.dms.dao.sales.planmanage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmpVsProductionPlanPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ProductPlanDao extends BaseDao{

	public static Logger logger = Logger.getLogger(YearPlanDao.class);
	private static final ProductPlanDao dao = new ProductPlanDao ();
	public static final ProductPlanDao getInstance() {
		return dao;
	}
	
	/*
	 * 月度任务校验车系是否存在
	 */
	public  List<Map<String, Object>> oemProductPlanCheckGroup(Map<String, Object> map){
		String year=map.get("year").toString();
		String month=map.get("month").toString();
		String userId=map.get("userId").toString();
		String companyId=map.get("companyId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_PRODUCTION_PLAN p\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and p.PLAN_YEAR = "+year+"\n");  
		sql.append("   and p.PLAN_MONTH = "+month+"\n");  
		sql.append("   and p.USER_ID ="+userId+"\n");  
		
		sql.append("   and not exists (select 1\n");
		sql.append("       from TM_VHCL_MATERIAL_GROUP g\n");  
		sql.append("      where g.GROUP_CODE = p.GROUP_CODE\n"); 
		sql.append("        and g.STATUS = "+Constant.STATUS_ENABLE+"\n");//有效
		sql.append("        and g.COMPANY_ID = "+companyId+")\n");  
		sql.append("   order by to_number(p.ROW_NUMBER) asc");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	/*
	 * 月度任务校验车系是否与业务范围一致
	 */
	public  List<Map<String, Object>> oemProductPlanCheckGroupArea(Map<String, Object> map){
		String year=map.get("year").toString();
		String month=map.get("month").toString();
		String userId=map.get("userId").toString();
		String groupArea=map.get("groupArea").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_PRODUCTION_PLAN p\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and p.PLAN_YEAR = "+year+"\n");  
		sql.append("   and p.PLAN_MONTH = "+month+"\n");  
		sql.append("   and p.USER_ID = "+userId+"\n");  
		sql.append("   and p.GROUP_CODE not in("+PlanUtil.createSqlStr(groupArea)+")\n");  
		sql.append(" order by to_number(p.ROW_NUMBER) asc");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	/*
	 * 查询临时表中是否有相同周导入相同的配置
	 * 返回所有重复数据集合
	 */
	public List<Map<String, Object>> talbeCheckDump(Map<String, Object> map){
		String year=map.get("year").toString();
		String month=map.get("month").toString();
		String userId=map.get("userId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");


		sql.append("select p1.ROW_NUMBER ROW_NUMBER1, p2.ROW_NUMBER ROW_NUMBER2\n");
		sql.append("  from TMP_VS_PRODUCTION_PLAN p1, TMP_VS_PRODUCTION_PLAN p2\n");  
		sql.append(" where \n");  
		sql.append("   p1.GROUP_CODE = p2.GROUP_CODE\n");  
		sql.append("   and p1.ROW_NUMBER <> p2.ROW_NUMBER\n");  
		sql.append("   and p1.PLAN_YEAR = p2.PLAN_YEAR\n");  
		sql.append("   and p1.PLAN_MONTH = p2.PLAN_MONTH\n");
		sql.append("   and p1.PLAN_WEEK = p2.PLAN_WEEK\n");
		sql.append("   and p1.USER_ID = p2.USER_ID\n");  
		sql.append("   and p1.PLAN_YEAR = ?\n");  
		params.add(year);
		sql.append("   and p1.PLAN_MONTH = ?\n");  
		params.add(month);
		sql.append("   and p1.USER_ID=?\n");
		params.add(userId);
		sql.append("   order by p1.ROW_NUMBER\n");

		List<Map<String, Object>> list=dao.pageQuery(sql.toString(), params, getFunName());
		
		return list;
	}
	/*
	 * 插入主表数据
	 */
	public int insertProductPlan(Map<String, Object> map) {
		String year=map.get("year").toString();
		String month=map.get("month").toString();
		String userId=map.get("userId").toString();
		String areaId=map.get("areaId").toString();
		String companyId=map.get("companyId").toString();

		StringBuffer sql = new StringBuffer();

		sql.append("merge into TT_VS_PRODUCTION_PLAN tp\n");
		sql.append("using (select F_GETID() PLAN_ID,\n");  
		sql.append("              "+companyId+" COMPANY_ID,\n");  
		sql.append("              "+areaId+" AREA_ID,\n");  
		sql.append("              aa.PLAN_YEAR,\n");  
		sql.append("              aa.PLAN_MONTH,\n");  
		sql.append("              aa.PLAN_WEEK,\n");  
		sql.append("              "+Constant.PLAN_MANAGE_UNCONFIRM+" STATUS,\n");  
		sql.append("              0 VER,\n");  
		sql.append("              "+userId+" CREATE_BY,\n");  
		sql.append("              sysdate CREATE_DATE\n");  
		sql.append("         from (select distinct p.PLAN_YEAR, p.PLAN_MONTH, p.PLAN_WEEK\n");  
		sql.append("                 from TMP_VS_PRODUCTION_PLAN p\n");  
		sql.append("                where p.PLAN_YEAR = "+year+"\n");  
		sql.append("                  and p.PLAN_MONTH = "+month+") aa) a\n");  
		sql.append("on (tp.COMPANY_ID = a.COMPANY_ID and tp.AREA_ID = a.AREA_ID and tp.PLAN_YEAR = a.PLAN_YEAR and tp.PLAN_MONTH = a.PLAN_MONTH and tp.PLAN_WEEK = a.PLAN_WEEK and tp.STATUS = a.STATUS)\n");  
		sql.append(" WHEN MATCHED THEN UPDATE SET TP.CREATE_DATE=TP.CREATE_DATE\n");
		sql.append("when not matched then\n");  
		sql.append("  insert\n");  
		sql.append("    (PLAN_ID,\n");  
		sql.append("     COMPANY_ID,\n");  
		sql.append("     AREA_ID,\n");  
		sql.append("     PLAN_YEAR,\n");  
		sql.append("     PLAN_MONTH,\n");  
		sql.append("     PLAN_WEEK,\n");  
		sql.append("     STATUS,\n");  
		sql.append("     VER,\n");  
		sql.append("     CREATE_BY,\n");  
		sql.append("     CREATE_DATE)\n");  
		sql.append("  values\n");  
		sql.append("    (a.PLAN_ID,\n");  
		sql.append("     a. COMPANY_ID,\n");  
		sql.append("     a.AREA_ID,\n");  
		sql.append("     A.PLAN_YEAR,\n");  
		sql.append("     A.PLAN_MONTH,\n");  
		sql.append("     A.PLAN_WEEK,\n");  
		sql.append("     a.STATUS,\n");  
		sql.append("     a.VER,\n");  
		sql.append("     a.CREATE_BY,\n");  
		sql.append("     a. CREATE_DATE)");

		return update(sql.toString(), null);
	}
	
	/*
	 * 插入明细表数据
	 */
	public int insertProductPlanDetail(Map<String, Object> map) {
		String year=map.get("year").toString();
		String month=map.get("month").toString();
		String userId=map.get("userId").toString();
		String areaId=map.get("areaId").toString();
		String companyId=map.get("companyId").toString();

		StringBuffer sql = new StringBuffer();

		sql.append("MERGE into TT_VS_PRODUCTION_PLAN_DETAIL a\n");
		sql.append("using (select tpp.PLAN_ID, tvmg.GROUP_ID, tmpp.PLAN_AMT\n");  
		sql.append("         from TT_VS_PRODUCTION_PLAN  tpp,\n");  
		sql.append("              TMP_VS_PRODUCTION_PLAN tmpp,\n");  
		sql.append("              TM_VHCL_MATERIAL_GROUP tvmg\n");  
		sql.append("        where tvmg.GROUP_CODE = tmpp.GROUP_CODE\n");  
		sql.append("          and tmpp.GROUP_CODE = tvmg.GROUP_CODE\n");  
		sql.append("          and tpp.PLAN_YEAR = tmpp.PLAN_YEAR\n");  
		sql.append("          and tpp.PLAN_MONTH = tmpp.PLAN_MONTH\n");  
		sql.append("          and tpp.PLAN_WEEK = tmpp.PLAN_WEEK\n");  
		sql.append("          and tpp.COMPANY_ID=tvmg.COMPANY_ID\n");  
		sql.append("          and tpp.COMPANY_ID ="+companyId+"\n");  
		sql.append("          and tpp.AREA_ID ="+areaId+"\n");  
		sql.append("          and tpp.STATUS = "+Constant.PLAN_MANAGE_UNCONFIRM+"\n");  
		sql.append("          and tmpp.USER_ID ="+userId+") c\n");  
		sql.append("on (a.PLAN_ID = c.PLAN_ID and a.GROUP_ID = c.GROUP_ID)\n");  
		sql.append("when MATCHED then\n");  
		sql.append("  update\n");  
		sql.append("     set a.PLAN_AMOUNT = c.PLAN_AMT,\n");  
		sql.append("         a.UPDATE_BY   = "+userId+",\n");  
		sql.append("         a.UPDATE_DATE = sysdate\n");  
		sql.append("when not matched then\n");  
		sql.append("  INSERT\n");  
		sql.append("    (a.DETAIL_ID,\n");  
		sql.append("     a.PLAN_ID,\n");  
		sql.append("     a.GROUP_ID,\n");  
		sql.append("     a.PLAN_AMOUNT,\n");  
		sql.append("     a.CREATE_BY,\n");  
		sql.append("     a.CREATE_DATE)\n");  
		sql.append("  values\n");  
		sql.append("    (F_GETID(), c.PLAN_ID, c.GROUP_ID, c.PLAN_AMT, "+userId+", sysdate)");

		return update(sql.toString(), null);
	}
	/*
	 * 查询临时表中周次是否在工作日历中存在
	 * 返回所有重复数据集合
	 */
	public List<Map<String, Object>> checkDateSetWeek(Map<String, Object> map){
		String year=map.get("year").toString();
		String month=map.get("month").toString();
		String userId=map.get("userId").toString();
		String companyId=map.get("companyId").toString();
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_PRODUCTION_PLAN p\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and p.PLAN_YEAR = "+year+"\n");  
		sql.append("   and p.PLAN_MONTH = "+month+"\n");  
		sql.append("   and p.USER_ID = "+userId+"\n");  
		sql.append("   and not exists (select 1\n");  
		sql.append("          from TM_DATE_SET tds\n");  
		sql.append("         where tds.SET_YEAR = p.PLAN_YEAR\n");  
		sql.append("           and tds.SET_MONTH = p.PLAN_MONTH\n");  
		sql.append("           and tds.SET_WEEK = p.PLAN_WEEK\n");  
		sql.append("           and tds.COMPANY_ID = "+companyId+")\n");  
		sql.append(" order by p.ROW_NUMBER asc");

		List<Map<String, Object>> list=dao.pageQuery(sql.toString(), params, getFunName());
		
		return list;
	}
	
	/*
	 * 查询每条数据在临时表是否重复
	 * return: boolean true不重复，false重复
	 */
	public boolean isDateDump(TmpVsProductionPlanPO paraPo){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");
		boolean bl=true;

		sql.append("select count(*) amt \n");
		sql.append("  from TMP_VS_PRODUCTION_PLAN p\n");  
		sql.append(" where p.PLAN_YEAR = ?\n");  
		params.add(paraPo.getPlanYear());
		sql.append("   and p.PLAN_MONTH = ?\n");  
		params.add(paraPo.getPlanMonth());
		sql.append("   and p.PLAN_WEEK = ?\n");  
		params.add(paraPo.getPlanWeek());
		sql.append("   and p.GROUP_CODE =?");
        params.add(paraPo.getGroupCode());
        sql.append("   and p.USER_ID =?");
        params.add(paraPo.getUserId());
		
		List<Map<String, Object>> list=dao.pageQuery(sql.toString(), params, getFunName());
		if(null!=list&&list.size()>0){
			Map<String, Object> map=list.get(0);
			Integer amt=new Integer(map.get("AMT").toString());
			if(amt.intValue()>1){
				bl=false;
			}
		}
		return bl;
	}
	
	/*
	 * 查询年月内工作周次，如果按临时表查询执行SQL1，如果按工作日历查执行SQL2
	 */
	//SQL1
	public List<Map<String, Object>> selectPlanWeekList(TmpVsProductionPlanPO conPo){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select p.PLAN_WEEK WEEK\n");
		sql.append("  from TMP_VS_PRODUCTION_PLAN p\n");  
		sql.append(" where p.PLAN_YEAR = ?\n");  
		params.add(conPo.getPlanYear());
		sql.append("   and p.PLAN_MONTH = ?\n");  
		params.add(conPo.getPlanMonth());
		sql.append("   and p.USER_ID =?\n");
		params.add(conPo.getUserId());
		sql.append(" order by p.PLAN_WEEK");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	//SQL2
	public List<Map<String, Object>> selectDateSetWeekList(TmDateSetPO conPo){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");


		sql.append("select distinct s.SET_WEEK WEEK\n");
		sql.append("  from TM_DATE_SET s\n");  
		sql.append(" where s.SET_YEAR = ?\n");  
		params.add(conPo.getSetYear());
		sql.append("   and s.SET_MONTH = ?\n");  
		params.add(conPo.getSetMonth());
		sql.append(" order by s.SET_WEEK");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	/*
	 * 查询工作日历某周次七天日期
	 */
	public List<TmDateSetPO> selectDateSetDateList(TmDateSetPO conPo){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");


		sql.append("select distinct s.SET_DATE SET_DATE\n");
		sql.append("  from TM_DATE_SET s\n");  
		sql.append(" where s.SET_YEAR = ?\n");  
		params.add(conPo.getSetYear());
		sql.append("   and s.SET_WEEK = ?\n");  
		params.add(conPo.getSetWeek());
		sql.append("   and s.COMPANY_ID = ?\n");  
		params.add(conPo.getCompanyId());
		sql.append(" order by to_number(s.SET_DATE)");

		return dao.select(TmDateSetPO.class,sql.toString(), params);
	}
	
	public List<Map<String, Object>> selectCheckSuccessTmp(TmpVsProductionPlanPO po,String areaId,List<Map<String, Object>> weekList){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select (select area.AREA_NAME\n");
		sql.append("          from TM_BUSINESS_AREA area\n");  
		sql.append("         where area.AREA_ID = ?) area_name,\n");  
		params.add(areaId);
		sql.append("       g.GROUP_CODE,\n");  
		for(int i=0;i<weekList.size();i++){
			Map<String, Object> map=weekList.get(i);
			sql.append("       sum(decode(p.PLAN_WEEK, "+map.get("WEEK")+", p.PLAN_AMT, 0)) w"+i+",\n");  
		}
		
		sql.append("       g.GROUP_NAME\n");  
		sql.append("  from TMP_VS_PRODUCTION_PLAN p, TM_VHCL_MATERIAL_GROUP g\n");  
		sql.append(" where p.GROUP_CODE = g.GROUP_CODE\n");  
		sql.append("   and p.PLAN_YEAR = ?\n");  
		params.add(po.getPlanYear());
		sql.append("   and p.PLAN_MONTH = ?\n");  
		params.add(po.getPlanMonth());
		sql.append("   and p.USER_ID = ?\n"); 
		params.add(po.getUserId());
		sql.append(" group by g.GROUP_CODE, g.GROUP_NAME");

		return dao.pageQuery(sql.toString(), params, getFunName()+"time:"+System.currentTimeMillis());
	}
	/*
	 * 查询未确认生产计划
	 */
	public List<Map<String, Object>> selectUnconfirmProductPlan(String posId,String companyId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");


		sql.append("select s.AREA_NAME,\n");
		sql.append("       s.AREA_ID,\n");  
		sql.append("       s.PLAN_YEAR,\n");  
		sql.append("       s.PLAN_MONTH,\n");  
		sql.append("       sum(s.PLAN_AMOUNT) PLAN_AMOUNT\n");  
		//sql.append("       s.MAXVER\n");
  
		sql.append("  from (select a.AREA_NAME,\n");  
		sql.append("               a.AREA_ID,\n");  
		sql.append("               p.PLAN_YEAR,\n");  
		sql.append("               p.PLAN_MONTH,\n");  
		sql.append("               d.PLAN_AMOUNT PLAN_AMOUNT\n");  
		/*sql.append("         (select nvl(max(tp.PLAN_VER), 0)\n");
		sql.append("          from TT_VS_PRODUCTION_PLAN tp\n");  
		sql.append("         where tp.PLAN_YEAR = p.PLAN_YEAR\n");  
		sql.append("           and tp.PLAN_MONTH = p.PLAN_MONTH\n");  
		sql.append("           and tp.COMPANY_ID = p.COMPANY_ID\n");
		sql.append("           and tp.AREA_ID=p.AREA_ID\n"); 
		sql.append("           and tp.STATUS = "+Constant.PLAN_MANAGE_CONFIRM+") MAXVER\n");*/
		sql.append("          from TT_VS_PRODUCTION_PLAN        p,\n");  
		sql.append("               TT_VS_PRODUCTION_PLAN_DETAIL d,\n");  
		sql.append("               TM_BUSINESS_AREA             a\n");  
		sql.append("         where p.PLAN_ID = d.PLAN_ID\n");  
		sql.append("           and p.AREA_ID = a.AREA_ID\n");  
		sql.append("           and p.COMPANY_ID = "+companyId+"\n");  
		sql.append("           and p.STATUS = "+Constant.PLAN_MANAGE_UNCONFIRM+"\n");  
		sql.append("           and a.AREA_ID in (select r.AREA_ID\n");  
		sql.append("                               from TM_POSE_BUSINESS_AREA r\n");  
		sql.append("                              where r.POSE_ID = "+posId+")) s\n");  
		sql.append(" group by s.AREA_NAME, s.AREA_ID, s.PLAN_YEAR, s.PLAN_MONTH");



		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	/*
	 * 查询未确认生产计划
	 */
	public List<Map<String, Object>> selectUnconfirmProductPlanDetail(String year,String month,String areaId,List<Map<String, Object>> weekList,String companyId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");


		sql.append("select a.GROUP_CODE, \n");
		for(int i=0;i<weekList.size();i++){
			sql.append("sum(a.sw"+i+") w"+i+", ");
		}
		sql.append("       a.GROUP_NAME");
		sql.append("  from (select g.GROUP_CODE,\n");  
		sql.append("               p.PLAN_WEEK,\n");  
		for(int i=0;i<weekList.size();i++){
			Map<String, Object> map=weekList.get(i);
			sql.append("               decode(p.PLAN_WEEK, "+map.get("WEEK")+", d.PLAN_AMOUNT, 0) sw"+i+",\n");  
		}
		sql.append("               g.GROUP_NAME\n");  
		sql.append("          from TT_VS_PRODUCTION_PLAN        p,\n");  
		sql.append("               TT_VS_PRODUCTION_PLAN_DETAIL d,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP       g\n");  
		sql.append("         where p.PLAN_ID = d.PLAN_ID\n");  
		sql.append("           and d.GROUP_ID = g.GROUP_ID\n"); 
		sql.append("           and p.COMPANY_ID="+companyId+"\n");
		sql.append("           and p.STATUS="+Constant.PLAN_MANAGE_UNCONFIRM+"\n");
		sql.append("           and p.PLAN_YEAR = "+year+"\n");  
		sql.append("           and p.PLAN_MONTH = "+month+"\n");  
		sql.append("           and d.GROUP_ID in\n");  
		sql.append("               (select T1.GROUP_ID\n");  
		sql.append("                  from tm_vhcl_material_group t1\n");  
		sql.append("                 WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("                   and t1.GROUP_LEVEL = 4\n");  
		sql.append("                 start with t1.group_id IN\n");  
		sql.append("                            (SELECT TAP.MATERIAL_GROUP_ID\n");  
		sql.append("                               FROM tm_area_group tap\n");  
		sql.append("                              where tap.area_id = "+areaId+")\n");  
		sql.append("                connect by prior t1.group_id = t1.parent_group_id)\n");  
		sql.append("         order by g.GROUP_ID) a\n");  
		sql.append(" group by a.GROUP_CODE, a.GROUP_NAME");

		logger.info(sql.toString());

		return dao.pageQuery(sql.toString(), params, getFunName()+"time:"+System.currentTimeMillis());
	}
	/*
	 * 查询最大版本号，已确认
	 */
	public Integer selectMaxPlanVer(){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select nvl(max(plan.plan_ver),0) plan_ver\n");
		sql.append("  from TT_VS_PRODUCTION_PLAN plan\n");  
		sql.append(" where 1=1\n");  
		sql.append("   and plan.STATUS = ?");
		params.add(Constant.PLAN_MANAGE_CONFIRM);

		Map<String, Object> map=dao.pageQueryMap(sql.toString(), params, getFunName());
		
		Integer planVer=new Integer(map.get("PLAN_VER").toString());
		return planVer;
	}
	/*
	 * 查询业务范围内最大版本号，已确认
	 */
	public Integer selectMaxPlanVer(String year,String month,String areaId,String companyId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select nvl(max(plan.plan_ver),0) plan_ver\n");
		sql.append("  from TT_VS_PRODUCTION_PLAN plan\n");  
		sql.append(" where 1=1\n");  
		sql.append("   and plan.PLAN_YEAR = ?\n");  
		params.add(year);
		sql.append("   and plan.COMPANY_ID = ?\n");  
		params.add(companyId);
		sql.append("   and plan.PLAN_MONTH = ?\n");  
		params.add(month);
		sql.append("   and plan.AREA_ID = ?\n");  
		params.add(areaId);
		sql.append("   and plan.STATUS = ?");
		params.add(Constant.PLAN_MANAGE_CONFIRM);

		Map<String, Object> map=dao.pageQueryMap(sql.toString(), params, getFunName());
		
		Integer planVer=new Integer(map.get("PLAN_VER").toString());
		return planVer;
	}
	/*
	 * 查询配置的滚动计划，GROUP_ID必须是月生产计划中排的
	 */
	public PageResult<Map<String, Object>> selectWeeklyProPlanConfig(Map<String, Object> map, int curPage, int pageSize){
		String year=(String)map.get("year");
		String month=(String)map.get("month");
		String week=(String)map.get("week");
		String areaId=(String)map.get("areaId");
		String companyId=map.get("companyId").toString();
		String mateGroupId = map.get("mateGroupId").toString() ;
		
		StringBuffer sql = new StringBuffer();
		List<Object> params = new LinkedList<Object>();


		sql.append("select g.GROUP_ID, g.GROUP_NAME, g.GROUP_CODE, nvl(tt.sum_amount,0) sum_amount,nvl(tv.week_amount,0) week_amount\n");
		sql.append("  from TM_VHCL_MATERIAL_GROUP g,\n"); 
		sql.append("(select tvppd.group_id,sum(tvppd.plan_amount) sum_amount\n");
		sql.append(" from TT_VS_PRODUCTION_PLAN tvpp,TT_VS_PRODUCTION_PLAN_DETAIL tvppd\n");  
		sql.append(" where tvpp.plan_id = tvppd.plan_id\n");  
		sql.append(" and tvpp.plan_year="+year+" and tvpp.plan_month="+month+"\n"); 
		sql.append("AND tvpp.PLAN_VER =\n");
		sql.append("               (select max(tvrp1.PLAN_VER)\n");  
		sql.append("                  from TT_VS_PRODUCTION_PLAN tvrp1\n");  
		sql.append("                 where tvrp1.PLAN_YEAR = TVPP.PLAN_YEAR\n");  
		sql.append("                   AND TVRP1.PLAN_MONTH = TVPP.PLAN_MONTH\n");
		sql.append("and tvrp1.AREA_ID = "+areaId+")\n");
		sql.append("    group by tvppd.group_id\n");  
		sql.append(")tt,\n");  
		sql.append("(select tvwpp.group_id,sum(tvwpp.week_amt) week_amount\n");  
		sql.append(" from tt_vs_weely_prod_plan tvwpp\n");  
		sql.append(" where tvwpp.plan_year="+year+" and tvwpp.plan_month="+month+"\n"); 
		sql.append(" group by tvwpp.group_id\n");  
		sql.append(" ) tv\n");

		sql.append(" where g.GROUP_ID in\n");  
//		sql.append("       (select T1.GROUP_ID\n");  
//		sql.append("          from tm_vhcl_material_group t1,tm_vhcl_material_group t2,tm_vhcl_material_group t3,tm_area_group tap\n");  
//		sql.append("         WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  	
//		sql.append("			and t3.GROUP_ID = t2.PARENT_GROUP_ID\n");
//		sql.append("			and t2.GROUP_ID = t1.PARENT_GROUP_ID\n");
//		sql.append("			and t3.GROUP_ID = tap.MATERIAL_GROUP_ID\n");
//		if (areaId != null && !"".equals(areaId)) {
//			sql.append("			and tap.area_id = "+areaId+"\n");  
//		}
//		if (mateGroupId != null && !"".equals(mateGroupId)) {
//			sql.append("			and t3.GROUP_ID = " + mateGroupId + "\n");
//		}
//		sql.append("           and t1.GROUP_LEVEL = 4)\n");   
		//修改20110321车系下级数据漏查
		sql.append("(select distinct vmg.PACKAGE_ID from tm_area_group tag,vw_material_group vmg\n");
		sql.append("where 1=1 and (tag.material_group_id=vmg.SERIES_ID or tag.material_group_id=vmg.MODEL_ID or tag.material_group_id=vmg.PACKAGE_ID)\n");
		if (areaId != null && !"".equals(areaId)) {
			sql.append("			and tag.area_id = "+areaId+"\n");  
		}
		if (mateGroupId != null && !"".equals(mateGroupId)) {
			sql.append("			and vmg.SERIES_ID = " + mateGroupId + "\n");
		}
		
		sql.append(")");
		//修改20110321车系下级数据漏查
		sql.append("   and exists \n");  
		sql.append("       (\n");  
		sql.append("\n");  
		sql.append("        select 1\n");  
		sql.append("          from TT_VS_PRODUCTION_PLAN_DETAIL d, TT_VS_PRODUCTION_PLAN p\n");  
		sql.append("         where p.PLAN_ID = d.PLAN_ID\n");  
		sql.append("           and d.group_id=g.group_id\n");  
		sql.append("           and p.PLAN_YEAR = "+year+"\n"); 
		sql.append("           and p.PLAN_MONTH = "+month+"\n");  
		sql.append("           and p.PLAN_WEEK = "+week+"\n");  
		sql.append("           and p.COMPANY_ID = "+companyId+"\n");
		sql.append("           and p.STATUS = "+Constant.PLAN_MANAGE_CONFIRM+")");
		sql.append("			and tv.group_id(+) = g.group_id\n");
		sql.append("			and tt.group_id(+) = g.group_id\n");
		sql.append("			order by g.group_code,g.group_name,g.group_id");



		sql.append("\n");  
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(),params,getFunName(),pageSize, curPage);
		return ps;
	}
	/*
	 * 周滚动计划录入查询
	 */
	public List<Map<String, Object>> selectWeeklyProPlanToSave(Map<String, Object> map){
		String groupId=(String)map.get("groupId");
		String year=(String)map.get("year");
		String month=(String)map.get("month");
		String week=(String)map.get("week");
		String companyId=(String)map.get("companyId");
		StringBuffer sql = new StringBuffer();

		sql.append("select mgroup.GROUP_NAME,\n");
		sql.append("       mgroup.MATERIAL_CODE,\n"); 
		sql.append("       mgroup.MATERIAL_ID,\n");
		sql.append("       mgroup.COLOR_NAME,\n");  
		sql.append("       sum(nvl(plan.WEEK_AMT, 0)) WEEK_AMT,\n");
		sql.append("       sum(nvl(plan.WEEK_AMT, 0)) MONTH_AMT,\n");  
		sql.append("       nvl(plan.ONE_AMT, 0) ONE_AMT,\n");  
		sql.append("       nvl(plan.TWO_AMT, 0) TWO_AMT,\n");  
		sql.append("       nvl(plan.THREE_AMT, 0) THREE_AMT,\n");  
		sql.append("       nvl(plan.FOUR_AMT, 0) FOUR_AMT,\n");  
		sql.append("       nvl(plan.FIVE_AMT, 0) FIVE_AMT,\n");  
		sql.append("       nvl(plan.SIX_AMT, 0) SIX_AMT,\n");  
		sql.append("       nvl(plan.SEVEN_AMT, 0) SEVEN_AMT,\n");  
		sql.append("       nvl(house.WAR_AMT, 0) WAR_AMT\n");  
		sql.append("  from (select g.GROUP_ID,\n");  
		sql.append("               m.MATERIAL_ID,\n");  
		sql.append("               g.GROUP_NAME,\n");  
		sql.append("               m.MATERIAL_CODE,\n");  
		sql.append("               m.COLOR_NAME\n");  
		sql.append("          from TM_VHCL_MATERIAL         m,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP   g,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP_R r\n");  
		sql.append("         where m.MATERIAL_ID = r.MATERIAL_ID\n");  
		sql.append("           and g.GROUP_ID = r.GROUP_ID\n");
		sql.append("           and g.COMPANY_ID="+companyId+"\n");
		sql.append("           and g.GROUP_ID in("+groupId+")\n");  
		sql.append("\n");  
		sql.append("        ) mgroup,\n");  
		sql.append("       (select tp.MATERIAL_ID,\n");  
		sql.append("               tp.WEEK_AMT,\n");  
		sql.append("               tp.ONE_AMT,\n");  
		sql.append("               tp.TWO_AMT,\n");  
		sql.append("               tp.THREE_AMT,\n");  
		sql.append("               tp.FOUR_AMT,\n");  
		sql.append("               tp.FIVE_AMT,\n");  
		sql.append("               tp.SIX_AMT,\n");  
		sql.append("               tp.SEVEN_AMT\n");  
		sql.append("          from TT_VS_WEELY_PROD_PLAN tp\n");  
		sql.append("         where tp.PLAN_YEAR = "+year+"\n");  
		sql.append("           and tp.PLAN_MONTH = "+month+"\n");  
		sql.append("           and tp.PLAN_WEEK = "+week+") plan,\n");  
		sql.append("       (select count(tv.VEHICLE_ID) WAR_AMT, tv.MATERIAL_ID\n");  
		sql.append("          from TM_VEHICLE tv\n");  
		sql.append("         where tv.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_02+"\n");  
		sql.append("           and tv.DEALER_ID is null\n");  
		sql.append("           and tv.ORG_TYPE is null\n");  
		sql.append("           and tv.ORG_ID is null\n");  
		sql.append("           and tv.LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");  
		sql.append("         group by tv.MATERIAL_ID) house\n");  
		sql.append(" where mgroup.MATERIAL_ID = plan.MATERIAL_ID(+)\n");  
		sql.append("   and mgroup.MATERIAL_ID = house.MATERIAL_ID(+)\n");  
		sql.append(" group by mgroup.GROUP_NAME,\n");  
		sql.append("          MATERIAL_CODE,\n");  
		sql.append("          COLOR_NAME,\n");  
		sql.append("          ONE_AMT,\n");  
		sql.append("          TWO_AMT,\n");  
		sql.append("          THREE_AMT,\n");  
		sql.append("          FOUR_AMT,\n");  
		sql.append("          FIVE_AMT,\n");  
		sql.append("          SIX_AMT,\n");  
		sql.append("          SEVEN_AMT,\n");  
		sql.append("          mgroup.MATERIAL_ID,\n");
		sql.append("          WAR_AMT\n");
		sql.append("          order by mgroup.MATERIAL_ID\n");
		
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/*
	 * 周滚动计划查询
	 */
	public PageResult<Map<String, Object>> selectWeeklyPorPlanSearch(Map<String, Object> map, int curPage, int pageSize){
		String groupCode=(String)map.get("groupCode");
		String year=(String)map.get("year");
		String month=(String)map.get("month");
		String week=(String)map.get("week");
		String companyId=map.get("companyId").toString();
		
		StringBuffer sql = new StringBuffer();
		List<Object> params = new LinkedList<Object>();

		sql.append("select g.GROUP_CODE,\n");
		sql.append("       g.GROUP_NAME,\n");  
		sql.append("       m.MATERIAL_CODE,\n");  
		sql.append("       m.COLOR_NAME,\n");  
		sql.append("       p.WEEK_AMT,\n");  
		sql.append("       p.ONE_AMT,\n");  
		sql.append("       p.TWO_AMT,\n");  
		sql.append("       p.THREE_AMT,\n");  
		sql.append("       p.FOUR_AMT,\n");  
		sql.append("       p.FIVE_AMT,\n");  
		sql.append("       p.SIX_AMT,\n");  
		sql.append("       p.SEVEN_AMT\n");  
		sql.append("  from TT_VS_WEELY_PROD_PLAN    p,\n");  
		sql.append("       TM_VHCL_MATERIAL         m,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R r,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   g\n");  
		sql.append(" where r.MATERIAL_ID = m.MATERIAL_ID\n");  
		sql.append("   and p.MATERIAL_ID = m.MATERIAL_ID\n");  
		sql.append("   and r.GROUP_ID = g.GROUP_ID\n");  
		sql.append("   and p.COMPANY_ID="+companyId+"\n"); 
		if(null!=year&&!"".equals(year)){
			sql.append("   and p.PLAN_YEAR = ?\n"); 
			params.add(year);
		}
		if(null!=month&&!"".equals(month)){
			sql.append("   and p.PLAN_MONTH = ?\n");  
			params.add(month);
		}
		if(null!=week&&!"".equals(week)){
			sql.append("   and p.PLAN_WEEK = ?\n");  
			params.add(week);
		}
		if(null!=groupCode&&!"".equals(groupCode)){
			sql.append("   and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")");
		}

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(),params,getFunName(),pageSize, curPage);
		return ps;
	}
	/*
	 * 周滚动计划下载
	 */
	public List<Map<String, Object>> selectWeeklyPorPlanDownLoad(Map<String, Object> map){
		String groupCode=(String)map.get("groupCode");
		String year=(String)map.get("year");
		String month=(String)map.get("month");
		String week=(String)map.get("week");
		String companyId=map.get("companyId").toString();
		
		StringBuffer sql = new StringBuffer();
		List<Object> params = new LinkedList<Object>();

		sql.append("select g.GROUP_CODE,\n");
		sql.append("       g.GROUP_NAME,\n");  
		sql.append("       m.MATERIAL_CODE,\n");  
		sql.append("       m.COLOR_NAME,\n");  
		sql.append("       p.WEEK_AMT,\n");  
		sql.append("       p.ONE_AMT,\n");  
		sql.append("       p.TWO_AMT,\n");  
		sql.append("       p.THREE_AMT,\n");  
		sql.append("       p.FOUR_AMT,\n");  
		sql.append("       p.FIVE_AMT,\n");  
		sql.append("       p.SIX_AMT,\n");  
		sql.append("       p.SEVEN_AMT\n");  
		sql.append("  from TT_VS_WEELY_PROD_PLAN    p,\n");  
		sql.append("       TM_VHCL_MATERIAL         m,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R r,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   g\n");  
		sql.append(" where r.MATERIAL_ID = m.MATERIAL_ID\n");  
		sql.append("   and p.MATERIAL_ID = m.MATERIAL_ID\n");  
		sql.append("   and r.GROUP_ID = g.GROUP_ID\n");
		sql.append("   and p.COMPANY_ID="+companyId+"\n");
		sql.append("   and g.COMPANY_ID=?\n");  
		params.add(companyId);
		if(null!=year&&!"".equals(year)){
			sql.append("   and p.PLAN_YEAR = ?\n"); 
			params.add(year);
		}
		/*if(null!=month&&!"".equals(month)){
			sql.append("   and p.PLAN_MONTH = ?\n");  
			params.add(month);
		}*/
		if(null!=week&&!"".equals(week)){
			sql.append("   and p.PLAN_WEEK = ?\n");  
			params.add(week);
		}
		if(null!=groupCode&&!"".equals(groupCode)){
			sql.append("   and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")");
		}

		return dao.pageQuery(sql.toString(),params,getFunName());
	}
	/*
	 * 生产计划查询
	 */
	public PageResult<Map<String, Object>> productPlanSearch(Map<String, Object> conMap,List<Map<String, Object>> weekList, int pageSize,int curPage){
		String year=CommonUtils.checkNull(conMap.get("year"));
		String month=CommonUtils.checkNull(conMap.get("month"));
		String areaId=CommonUtils.checkNull(conMap.get("areaId"));
		String groupCode=CommonUtils.checkNull(conMap.get("groupCode"));
		String ver=CommonUtils.checkNull(conMap.get("ver"));
		String companyId=CommonUtils.checkNull(conMap.get("companyId"));
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");


		sql.append("select a.GROUP_CODE, \n");
		for(int i=0;i<weekList.size();i++){
			sql.append("sum(w"+i+") w"+i+", ");
		}
		sql.append("       sum(PLAN_AMOUNT)  AMOUNT,");
		sql.append("       a.GROUP_NAME");
		sql.append("  from (select g.GROUP_CODE,\n");  
		sql.append("               p.PLAN_WEEK,\n"); 
		sql.append("               d.PLAN_AMOUNT,\n");
		for(int i=0;i<weekList.size();i++){
			Map<String, Object> map=weekList.get(i);
			sql.append("               decode(p.PLAN_WEEK, "+map.get("WEEK")+", d.PLAN_AMOUNT, 0) w"+i+",\n");  
		}
		sql.append("               g.GROUP_NAME\n");  
		sql.append("          from TT_VS_PRODUCTION_PLAN        p,\n");  
		sql.append("               TT_VS_PRODUCTION_PLAN_DETAIL d,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP       g\n");  
		sql.append("         where p.PLAN_ID = d.PLAN_ID\n");  
		sql.append("           and d.GROUP_ID = g.GROUP_ID\n"); 
		sql.append("           and p.COMPANY_ID="+companyId+"\n"); 
		sql.append("           and p.STATUS="+Constant.PLAN_MANAGE_CONFIRM+"\n");
		sql.append("           and p.PLAN_VER=?\n");
		params.add(ver);
		sql.append("           and p.PLAN_YEAR = "+year+"\n");  
		sql.append("           and p.PLAN_MONTH = "+month+"\n");  
		sql.append("           and d.GROUP_ID in\n");  
		sql.append("               (select T1.GROUP_ID\n");  
		sql.append("                  from tm_vhcl_material_group t1\n");  
		sql.append("                 WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("                   and t1.GROUP_LEVEL = 4\n");  
		sql.append("                 start with t1.group_id IN\n");  
		sql.append("                            (SELECT TAP.MATERIAL_GROUP_ID\n");  
		sql.append("                               FROM tm_area_group tap\n");  
		sql.append("                              where tap.area_id = "+areaId+")\n");  
		sql.append("                connect by prior t1.group_id = t1.parent_group_id)\n");  
		if(null!=groupCode&&!"".equals(groupCode)){
			sql.append("           and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
		}
		sql.append("         order by g.GROUP_ID) a\n");  
		sql.append(" group by a.GROUP_CODE, a.GROUP_NAME");

		logger.info(sql.toString());

		return dao.pageQuery(sql.toString(),params,getFunName()+System.currentTimeMillis(),pageSize, curPage);
	}
	/*
	 * 生产计划查询
	 */
	public List<Map<String, Object>> productPlanDownLoadSearch(Map<String, Object> conMap,List<Map<String, Object>> weekList){
		String year=CommonUtils.checkNull(conMap.get("year"));
		String month=CommonUtils.checkNull(conMap.get("month"));
		String areaId=CommonUtils.checkNull(conMap.get("areaId"));
		String groupCode=CommonUtils.checkNull(conMap.get("groupCode"));
		String ver=CommonUtils.checkNull(conMap.get("ver"));
		String companyId=CommonUtils.checkNull(conMap.get("companyId"));
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");


		sql.append("select a.GROUP_CODE, \n");
		for(int i=0;i<weekList.size();i++){
			sql.append("sum(w"+i+") w"+i+", ");
		}
		sql.append("       sum(PLAN_AMOUNT)  AMOUNT,");
		sql.append("       a.GROUP_NAME");
		sql.append("  from (select g.GROUP_CODE,\n");  
		sql.append("               p.PLAN_WEEK,\n"); 
		sql.append("               d.PLAN_AMOUNT,\n");
		for(int i=0;i<weekList.size();i++){
			Map<String, Object> map=weekList.get(i);
			sql.append("               decode(p.PLAN_WEEK, "+map.get("WEEK")+", d.PLAN_AMOUNT, 0) w"+i+",\n");  
		}
		sql.append("               g.GROUP_NAME\n");  
		sql.append("          from TT_VS_PRODUCTION_PLAN        p,\n");  
		sql.append("               TT_VS_PRODUCTION_PLAN_DETAIL d,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP       g\n");  
		sql.append("         where p.PLAN_ID = d.PLAN_ID\n");  
		sql.append("           and d.GROUP_ID = g.GROUP_ID\n"); 
		sql.append("           and p.COMPANY_ID="+companyId+"\n"); 
		sql.append("           and p.STATUS="+Constant.PLAN_MANAGE_CONFIRM+"\n");
		sql.append("           and p.PLAN_VER=?\n");
		params.add(ver);
		sql.append("           and p.PLAN_YEAR = "+year+"\n");  
		sql.append("           and p.PLAN_MONTH = "+month+"\n");  
		sql.append("           and d.GROUP_ID in\n");  
		sql.append("               (select T1.GROUP_ID\n");  
		sql.append("                  from tm_vhcl_material_group t1\n");  
		sql.append("                 WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("                   and t1.GROUP_LEVEL = 4\n");  
		sql.append("                 start with t1.group_id IN\n");  
		sql.append("                            (SELECT TAP.MATERIAL_GROUP_ID\n");  
		sql.append("                               FROM tm_area_group tap\n");  
		sql.append("                              where tap.area_id = "+areaId+")\n");  
		sql.append("                connect by prior t1.group_id = t1.parent_group_id)\n");  
		if(null!=groupCode&&!"".equals(groupCode)){
			sql.append("           and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
		}
		sql.append("         order by g.GROUP_ID) a\n");  
		sql.append(" group by a.GROUP_CODE, a.GROUP_NAME");

		logger.info(sql.toString());

		return dao.pageQuery(sql.toString(),params,getFunName());
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	//生产模版导入中的物料查询
	public List<Map<String, Object>> getTempDownload(String companyId,String areaId) {

//		StringBuffer sql = new StringBuffer();
		
//		sql.append("select tvmg3.GROUP_CODE, tvmg3.COMPANY_ID, tag.AREA_ID\n");
//		sql.append("  from TM_VHCL_MATERIAL_GROUP tvmg1,\n");  
//		sql.append("       TM_VHCL_MATERIAL_GROUP tvmg2,\n");  
//		sql.append("       TM_VHCL_MATERIAL_GROUP tvmg3,\n");  
//		sql.append("       TM_AREA_GROUP          tag\n");  
//		sql.append(" where tvmg1.GROUP_ID = tag.MATERIAL_GROUP_ID\n");  
//		sql.append("   and tvmg1.COMPANY_ID = "+companyId+" \n") ;
//		sql.append("   and tvmg3.PARENT_GROUP_ID = tvmg2.GROUP_ID\n");  
//		sql.append("   and tvmg2.PARENT_GROUP_ID = tvmg1.GROUP_ID\n");  
//		sql.append("   and tvmg3.STATUS = "+Constant.STATUS_ENABLE+" \n") ;
//		sql.append("   and tag.AREA_ID = "+areaId+"\n");  
//		sql.append("   and tvmg3.GROUP_LEVEL = 4\n");  
//		sql.append(" order by tvmg3.GROUP_CODE\n");
		//修改20110321车系下级数据漏查
		StringBuffer sql = new StringBuffer();
		sql.append("select DISTINCT TMG.PACKAGE_CODE AS GROUP_CODE, TMG.COMPANY_ID, TAG.AREA_ID\n");
		sql.append("  from VW_MATERIAL_GROUP TMG, TM_AREA_GROUP tag\n");
		sql.append("where 1=1\n");
		if(null!=companyId&&!"".equals(companyId)){
			sql.append(" AND TMG.COMPANY_ID = "+companyId+"\n");  
		}
		sql.append("   AND (TMG.SERIES_ID = TAG.MATERIAL_GROUP_ID OR\n");
		sql.append("       TMG.MODEL_ID = TAG.MATERIAL_GROUP_ID OR\n");
		sql.append("       TMG.PACKAGE_ID = TAG.MATERIAL_GROUP_ID)\n");
		if(null!=areaId&&!"".equals(areaId)){
			sql.append("   and tag.AREA_ID = "+areaId+"\n");
		}
		sql.append(" order by TMG.PACKAGE_CODE");


		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	public List<Map<String,Object>> getWeekofYear(String year, String month) {
		StringBuffer sql = new StringBuffer() ;
		
		sql.append("select distinct tds.SET_WEEK\n");
		sql.append("  from TM_DATE_SET tds\n");  
		sql.append(" where tds.SET_YEAR = "+ year +"\n");  
		sql.append("   and tds.SET_MONTH = "+ month +"\n");  
		sql.append(" order by tds.SET_WEEK\n");
		
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
}
