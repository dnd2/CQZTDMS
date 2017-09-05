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
import com.infodms.dms.po.TmpVsResourcesPO;
import com.infoservice.po3.bean.PO;

public class AreaResourceDao extends BaseDao{
	public static Logger logger = Logger.getLogger(YearPlanDao.class);
	public static final AreaResourceDao dao=new AreaResourceDao();
	public static final AreaResourceDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	 * 月度任务校验车系是否与业务范围一致
	 */
	public List<Map<String, Object>> oemProductPlanCheckGroupArea(Map<String, Object> map){
		String year=map.get("year").toString();
		String month=map.get("month").toString();
		String userId=map.get("userId").toString();
		String groupArea=map.get("groupArea").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from tmp_vs_resources p\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and p.PLAN_YEAR = "+year+"\n");  
		sql.append("   and p.PLAN_MONTH = "+month+"\n");  
		sql.append("   and p.USER_ID = "+userId+"\n");  
		sql.append("   and p.GROUP_CODE not in("+PlanUtil.createSqlStr(groupArea)+")\n");  
		sql.append(" order by to_number(p.ROW_NUMBER) asc");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	/*
	 * 查询业务范围内最大版本号，已确认
	 */
	public Integer selectMaxPlanVer(String year,String month,String areaId,String companyId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select nvl(max(plan.plan_ver),0) plan_ver\n");
		sql.append("  from tt_vs_resources_plan plan\n");  
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
	 * 查询最大版本号，已确认
	 */
	public Integer selectMaxPlanVer(){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select nvl(max(plan.plan_ver),0) plan_ver\n");
		sql.append("  from tt_vs_production_plan plan\n");  
		sql.append(" where 1=1\n");  
		sql.append("   and plan.STATUS = ?");
		params.add(Constant.PLAN_MANAGE_CONFIRM);

		Map<String, Object> map=dao.pageQueryMap(sql.toString(), params, getFunName());
		
		Integer planVer=new Integer(map.get("PLAN_VER").toString());
		return planVer;
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
		sql.append("  from tmp_vs_resources p\n");  
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
		sql.append("  from tmp_vs_resources p\n");  
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
		sql.append("  from tmp_vs_resources p1, tmp_vs_resources p2\n");  
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
	public List<Map<String, Object>> selectCheckSuccessTmp(TmpVsResourcesPO po,String areaId,List<Map<String, Object>> weekList){
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
		sql.append("  from tmp_vs_resources p, TM_VHCL_MATERIAL_GROUP g\n");  
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
}
