package com.infodms.dms.actions.sales.planmanage.PlanUtil;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infoservice.po3.bean.PO;

public class PlanUtilDao extends BaseDao {

	public static Logger logger = Logger.getLogger(YearPlanDao.class);
	private static final PlanUtilDao dao = new PlanUtilDao ();
	public static final PlanUtilDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	 * 查询所有车系
	 */
	public List<Map<String, Object>> selectSeries(String companyId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer();

		sql.append("select g.GROUP_NAME,g.GROUP_ID\n");
		sql.append("  from TM_VHCL_MATERIAL_GROUP g\n");  
		sql.append(" where g.GROUP_level = 2\n");  
		sql.append("    and g.COMPANY_ID="+companyId+"\n");  
		sql.append(" order by g.GROUP_ID");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/*
	 * 校验组织是否与业务范围匹配
	 */
	public boolean isOrgInArea(String orgCode,Long areaId){
		boolean bl=true;
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select org.ORG_ID\n");  
		sql.append("  from TM_BUSINESS_AREA area, TM_ORG org, TM_ORG_BUSINESS_AREA r\n");  
		sql.append(" where area.AREA_ID = r.AREA_ID\n");  
		sql.append("   and org.ORG_ID = r.ORG_ID\n");  
		sql.append("   and org.ORG_CODE = ?");
		params.add(orgCode);
		sql.append("   and area.AREA_ID = ?");
		params.add(areaId);
		
		List<Map<String, Object>> list=dao.pageQuery(sql.toString(), params, getFunName());
		if(null==list||list.size()==0){
			bl=false;
		}
		return bl;
	}
	
	/*
	 * 校验组织是否与业务范围匹配
	 */
	public boolean isGroupInArea(String code,Long areaId){
		boolean bl=true;
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select a.AREA_ID\n");
		sql.append("  from TM_AREA_GROUP r, TM_BUSINESS_AREA a, TM_VHCL_MATERIAL_GROUP g\n");  
		sql.append(" where r.MATERIAL_GROUP_ID = g.GROUP_ID\n");  
		sql.append("   and r.AREA_ID = a.AREA_ID\n");  
		sql.append("   and g.GROUP_CODE = ?\n");
		params.add(code);
		sql.append("   and g.GROUP_CODE IN (");
		
		sql.append("SELECT TVMG.GROUP_CODE\n");
		sql.append("    FROM TM_VHCL_MATERIAL_GROUP TVMG\n");  
		sql.append("  WHERE TVMG.GROUP_LEVEL = 2\n");  
		sql.append("    AND TVMG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   START WITH TVMG.GROUP_ID IN (SELECT TAG.MATERIAL_GROUP_ID\n");  
		sql.append("                                FROM TM_AREA_GROUP TAG\n");  
		sql.append("                               WHERE TAG.AREA_ID = ?)\n");  
		params.add(areaId);
		sql.append("   CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID ---根据业范围往上找，找到车系级别\n");  
		sql.append(" UNION\n");  
		sql.append("   SELECT TVMG.GROUP_CODE\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");  
		sql.append(" WHERE TVMG.GROUP_LEVEL = 2\n");  
		sql.append("   AND TVMG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append(" START WITH TVMG.GROUP_ID IN (SELECT TAG.MATERIAL_GROUP_ID\n");  
		sql.append("                                FROM TM_AREA_GROUP TAG\n");  
		sql.append("                               WHERE TAG.AREA_ID = ?)\n");  
		params.add(areaId);
		sql.append(" CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID ----根据业务范围往下找，找到车系\n");
		sql.append(" )");
		
		List<Map<String, Object>> list=dao.pageQuery(sql.toString(), params, getFunName());
		if(null==list||list.size()==0){
			bl=false;
		}
		return bl;
	}
	
	/*
	 * 查询业务范围下GROUP
	 */
	public List<Map<String, Object>> selectAreaGroup(String companyId,int groupLevel){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
		sql.append("    FROM TM_VHCL_MATERIAL_GROUP TVMG\n");  
		sql.append("  WHERE TVMG.GROUP_LEVEL = "+groupLevel+"\n");  
		sql.append("    AND TVMG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		/*sql.append("   START WITH TVMG.GROUP_ID IN (SELECT TAG.MATERIAL_GROUP_ID\n");  
		sql.append("                                FROM TM_AREA_GROUP TAG\n");  
		sql.append("                               WHERE TAG.AREA_ID = ?)\n");  
		params.add(areaId);
		sql.append("   CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID ---根据业范围往上找，找到车系级别\n");  
		sql.append(" UNION\n");  
		sql.append("   SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");  
		sql.append(" WHERE TVMG.GROUP_LEVEL = "+groupLevel+"\n");  
		sql.append("   AND TVMG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append(" START WITH TVMG.GROUP_ID IN (SELECT TAG.MATERIAL_GROUP_ID\n");  
		sql.append("                                FROM TM_AREA_GROUP TAG\n");  
		sql.append("                               WHERE TAG.AREA_ID = ?)\n");  
		params.add(areaId);
		sql.append(" CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID ----根据业务范围往下找，找到车系\n");  
		sql.append("------union 原因是为了支持设置业务范围的时候可以设置到品牌，车系，车型，配置");*/

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	public String selectAreaGroupStr(String areaId,String companyId,int groupLevel){
		StringBuffer sql=new StringBuffer("");
		
		sql.append("SELECT TVMG.GROUP_CODE\n");
		sql.append("    FROM TM_VHCL_MATERIAL_GROUP TVMG\n");  
		sql.append("  WHERE TVMG.GROUP_LEVEL = "+groupLevel+"\n");  
		sql.append("    AND TVMG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   START WITH TVMG.GROUP_ID IN (SELECT TAG.MATERIAL_GROUP_ID\n");  
		sql.append("                                FROM TM_AREA_GROUP TAG\n");  
		sql.append("                               WHERE TAG.AREA_ID = " + areaId + ")\n");  
		sql.append("   CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID ---根据业范围往上找，找到车系级别\n");  
		sql.append(" UNION\n");  
		sql.append("   SELECT TVMG.GROUP_CODE\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");  
		sql.append(" WHERE TVMG.GROUP_LEVEL = "+groupLevel+"\n");  
		sql.append("   AND TVMG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append(" START WITH TVMG.GROUP_ID IN (SELECT TAG.MATERIAL_GROUP_ID\n");  
		sql.append("                                FROM TM_AREA_GROUP TAG\n");  
		sql.append("                               WHERE TAG.AREA_ID = " + areaId + ")\n");  
		sql.append(" CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID \n");  

		return sql.toString() ;
	}
	
	/*
	 * 查询业务范围下车型
	 */
	public List<Map<String, Object>> selectAreaModel(String areaId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");
		

		sql.append("SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
		sql.append("    FROM TM_VHCL_MATERIAL_GROUP TVMG\n");  
		sql.append("  WHERE TVMG.GROUP_LEVEL = 3\n");  
		sql.append("    AND TVMG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   START WITH TVMG.GROUP_ID IN (SELECT TAG.MATERIAL_GROUP_ID\n");  
		sql.append("                                FROM TM_AREA_GROUP TAG\n");  
		sql.append("                               WHERE TAG.AREA_ID = ?)\n");  
		params.add(areaId);
		sql.append("   CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID ---根据业范围往上找，找到车系级别\n");  
		sql.append(" UNION\n");  
		sql.append("   SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");  
		sql.append(" WHERE TVMG.GROUP_LEVEL = 3\n");  
		sql.append("   AND TVMG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append(" START WITH TVMG.GROUP_ID IN (SELECT TAG.MATERIAL_GROUP_ID\n");  
		sql.append("                                FROM TM_AREA_GROUP TAG\n");  
		sql.append("                               WHERE TAG.AREA_ID = ?)\n");  
		params.add(areaId);
		sql.append(" CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID ----根据业务范围往下找，找到车系\n");  
		sql.append("------union 原因是为了支持设置业务范围的时候可以设置到品牌，车系，车型，配置");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * 查询配置是否在业务范围内
	 * groupId配置ID
	 * areaId业务范围ID
	 * return: boolean true否在，false不否在
	 */
	public boolean isConfigInArea(String groupId,String groupCode,String areaId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");
		boolean bl=false;

		
		sql.append("select a.group_id\n");
		sql.append("  from (select T1.GROUP_ID,T1.GROUP_CODE\n");  
		sql.append("          from tm_vhcl_material_group t1\n");  
		sql.append("         WHERE T1.STATUS = 10011001\n");  
		sql.append("           and t1.GROUP_LEVEL = 4\n");  
		sql.append("         start with t1.group_id IN\n");  
		sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");  
		sql.append("                       FROM tm_area_group tap\n");  
		sql.append("                      where tap.area_id = "+areaId+")\n");  
		sql.append("        connect by prior t1.group_id = t1.parent_group_id) a\n");  
		sql.append(" where 1=1");
		if(null!=groupId&&!"".equals(groupId)){
			sql.append(" and a.group_id = "+groupId+"\n");
		}
		if(null!=groupCode&&!"".equals(groupCode)){
			sql.append(" and a.group_code = '"+groupCode+"'\n");
		}

		List<Map<String, Object>> list=dao.pageQuery(sql.toString(), params, getFunName());
		if(null!=list&&list.size()>0){
			bl=true;
		}

		return bl;
	}
	
	/**
	 * 判断经销商代码和业务范围是否相符
	 * @param dealerCode
	 * @param areaId
	 * @return
	 */
	public boolean isDealerAreaAccord(String dealerCode, String areaId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		boolean bl = false;

		sql.append("SELECT TD.DEALER_ID\n");
		sql.append("  FROM TM_DEALER TD, TM_DEALER_BUSINESS_AREA TDBA\n");  
		sql.append(" WHERE TD.DEALER_ID = TDBA.DEALER_ID\n");  
		sql.append("   AND TD.DEALER_CODE = '" + dealerCode + "'\n");  
		sql.append("   AND TDBA.AREA_ID = " + areaId + "");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
		if(null != list && list.size() > 0){
			bl = true;
		}
		return bl;
	}
	
	/**
	 * 判断组织代码和业务范围是否相符
	 * @param orgCode
	 * @param areaId
	 * @return
	 */
	public boolean isOrgAreaAccord(String orgCode, String areaId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		boolean bl = false;

		sql.append("SELECT TOR.ORG_ID\n");
		sql.append("  FROM TM_ORG TOR, TM_ORG_BUSINESS_AREA TOBA\n");  
		sql.append(" WHERE TOR.ORG_ID = TOBA.ORG_ID\n");  
		sql.append("   AND TOR.ORG_CODE = '" + orgCode + "'\n");  
		sql.append("   AND TOBA.AREA_ID = " + areaId + "");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
		if(null != list && list.size() > 0){
			bl = true;
		}
		return bl;
	}
	
	/**
	 * 判断经销商是否在此区域内
	 * @param orgId
	 * @param dealerCode
	 * @return
	 */
	public boolean isOrgDealerAccord(String orgId, String dealerCode){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		boolean bl = false;

		sql.append("SELECT TD.DEALER_ID\n");
		sql.append("  FROM TM_DEALER TD\n");  
		sql.append(" WHERE TD.STATUS = " + Constant.STATUS_ENABLE + "\n");  
		sql.append("   AND TD.DEALER_TYPE IN (" + Constant.DEALER_TYPE_DVS + ", "+Constant.DEALER_TYPE_QYZDL+")\n");  
		sql.append("   AND TD.DEALER_CODE = '" + dealerCode + "'\n");  
		sql.append(" START WITH DEALER_ID IN\n");  
		sql.append("            (SELECT TDOR.DEALER_ID\n");  
		sql.append("               FROM TM_DEALER_ORG_RELATION TDOR\n");  
		sql.append("              WHERE TDOR.ORG_ID IN\n");  
		sql.append("                    (SELECT ORG_ID\n");  
		sql.append("                       FROM TM_ORG TMO\n");  
		sql.append("                      WHERE TMO.STATUS = " + Constant.STATUS_ENABLE + "\n");  
		sql.append("                      START WITH TMO.ORG_ID = " + orgId + "\n");  
		sql.append("                     CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID))\n");  
		sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
		if(null != list && list.size() > 0){
			bl = true;
		}
		return bl;
	}
	
	/*
	 * 查询预测参数
	 */
	public  List<TmBusinessParaPO> selectForecastPara(){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select para.PARA_ID, para.PARA_VALUE\n");
		sql.append("  from TM_BUSINESS_PARA para\n");  
		sql.append(" where para.TYPE_CODE in (2006, 2007,2015)");

		return dao.select(TmBusinessParaPO.class, sql.toString(), params);
	}
	/*
	 * 查询工作日历周次
	 */
	public List<Map<String, Object>> selectDateSetWeekList(TmDateSetPO conPo){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");


		sql.append("select s.SET_WEEK WEEK\n");
		sql.append("  from TM_DATE_SET s\n");  
		sql.append(" where s.SET_YEAR = "+conPo.getSetYear()+"\n");  
		sql.append("   and s.SET_MONTH = "+conPo.getSetMonth()+"\n");  
		sql.append("   and s.COMPANY_ID = "+conPo.getCompanyId()+"\n");
		sql.append(" group by s.SET_WEEK");
		sql.append(" order by s.SET_WEEK");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	/*
	 * 查询工作日历月份
	 */
	public Map<String, Object> selectDateSetMonth(TmDateSetPO conpo){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select s.SET_MONTH\n");
		sql.append("  from TM_DATE_SET s\n");  
		sql.append(" where s.SET_YEAR = ?\n");  
		params.add(conpo.getSetYear());
		sql.append("   and s.SET_WEEK = ?");
		params.add(conpo.getSetWeek());
		
		return dao.pageQueryMap(sql.toString(), params, getFunName());
	}
	/*
	 * 查询所有车系PlanUtilDao dao=PlanUtilDao.getInstance();
	 */
	public List<Map<String, Object>> selectSeries(){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer();

		sql.append("select g.GROUP_NAME,g.GROUP_ID\n");
		sql.append("  from TM_VHCL_MATERIAL_GROUP g\n");  
		sql.append(" where g.GROUP_level = 2\n");  
		sql.append(" order by g.GROUP_ID");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	//查询SET_DATE为YYYYMM的所有周次
	public List<Map<String, Object>>  getSetDateWeekList(String setDate){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer();

		sql.append("select s.SET_WEEK WEEK\n");
		sql.append("  from TM_DATE_SET s\n");  
		sql.append(" where s.SET_DATE like '"+setDate+"%'\n");  
		sql.append(" group by s.SET_WEEK");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	//查询YYYYMMDD的周次
	public Map<String, Object> getSetDateCurWeek(String setDate,String companyId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer();

		sql.append("select s.SET_WEEK WEEK,s.SET_YEAR\n");
		sql.append("  from TM_DATE_SET s\n");  
		sql.append(" where s.SET_DATE = "+setDate+"\n"); 
		sql.append("    and s.COMPANY_ID="+companyId+"\n");
		sql.append(" group by s.SET_WEEK,SET_YEAR");

		return dao.pageQueryMap(sql.toString(), params, getFunName());
	}
	//查询年最大的周次
	public Map<String, Object> getSetDateMaxWeek(String year,String companyId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer();
		
		sql.append("select nvl(max(to_number(s.SET_WEEK)), 0) MAX_WEEK\n");
		sql.append("  from TM_DATE_SET s\n");  
		sql.append(" where s.SET_YEAR = ?");
		params.add(year);
		sql.append("       and s.COMPANY_ID=?");
		params.add(companyId);

		return dao.pageQueryMap(sql.toString(), params, getFunName());
	}
	/*
	 * 查询TM_BUSINESS_PARA
	 * codeStr :对应表内CODE_TYPE 如果多个CODE_TYPE,用“，”分开
	 * oemCompanyId 车厂公司ID
	 */
	public List<Map<String, Object>>  selectBussinessPara(String codeStr,String oemCompanyId){
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer();

		sql.append("select p.PARA_ID, p.PARA_VALUE\n");
		sql.append("  from TM_BUSINESS_PARA p\n");  
		sql.append(" where 1=1\n");  
		if(codeStr.indexOf(",")!=-1){
			sql.append("   and p.TYPE_CODE in ("+PlanUtil.createSqlStr(codeStr)+")\n");  
		}else{
			sql.append("   and p.TYPE_CODE = "+codeStr+"\n"); 
		}
		sql.append("   and p.OEM_COMPANY_ID = "+oemCompanyId+"\n");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
}
