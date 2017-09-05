package com.infodms.dms.dao.sales.planmanage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.actions.sales.planmanage.RequirementForecast.RequireForecastManage;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.po.TmOrgPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class OemRequireForecastReportDao extends BaseDao<PO> {
	
	public static Logger logger = Logger.getLogger(YearPlanDao.class);
	private static final OemRequireForecastReportDao dao = new OemRequireForecastReportDao ();
	public static final OemRequireForecastReportDao getInstance() {
		return dao;
	}
	

	
	/*
	 * 区域查询SQL，SUB单指DEALE上一级ORG
	 */
	
	private String getSubForecastSql(String orgId,List<Map<String, Object>> mapList,String companyId){
		StringBuffer sql=new StringBuffer("");


		sql.append("select Y.SERIES_CODE,\n");
		sql.append("       Y.SERIES_NAME,\n");  
		sql.append("       Y.MODEL_CODE,\n");  
		for(int i=0;i<mapList.size();i++){
			sql.append("       SUM(D"+i+") D"+i+",\n");  
			sql.append("       SUM(S"+i+") S"+i+",\n");  
		}
		sql.append("       Y.GROUP_ID,\n");  
		sql.append("       Y.MODEL_NAME\n");
		sql.append("from (\n");
		sql.append("select x.SERIES_CODE,\n");
		sql.append("       x.SERIES_NAME,\n");  
		sql.append("       x.MODEL_CODE,\n");  
		String month = PlanUtil.getRadomDate(1,"");//zxf
		for(int i=0;i<mapList.size();i++){			//zxf
			
		    Map<String, Object> map=mapList.get(i);
		    if(month.equals(map.get("MONTH"))){
		    	sql.append("       case\n");  
				sql.append("         when x.FORECAST_YEAR = "+map.get("YEAR")+" and x.FORECAST_MONTH = "+map.get("MONTH")+" and x.ORG_TYPE="+Constant.ORG_TYPE_DEALER+" and x.forecast_type = 60591001 then\n");  
				sql.append("          x.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          0\n");  
				sql.append("       end D"+i+",\n");
				sql.append("       case\n");  
				sql.append("         when x.FORECAST_YEAR = "+map.get("YEAR")+" and x.FORECAST_MONTH = "+map.get("MONTH")+" and x.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and x.forecast_type = 60591001 then\n");  
				sql.append("          x.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          0\n");  
				sql.append("       end S"+i+",\n"); 
			}
		    else{
				sql.append("       case\n");  
				sql.append("         when x.FORECAST_YEAR = "+map.get("YEAR")+" and x.FORECAST_MONTH = "+map.get("MONTH")+" and x.ORG_TYPE="+Constant.ORG_TYPE_DEALER+"  and x.forecast_type = 60591002 then\n");  
				sql.append("          x.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          0\n");  
				sql.append("       end D"+i+",\n");
				sql.append("       case\n");  
				sql.append("         when x.FORECAST_YEAR = "+map.get("YEAR")+" and x.FORECAST_MONTH = "+map.get("MONTH")+" and x.ORG_TYPE="+Constant.ORG_TYPE_OEM+"  and x.forecast_type = 60591002 then\n");  
				sql.append("          x.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          0\n");  
				sql.append("       end S"+i+",\n"); 
			}
		}
		sql.append("       x.GROUP_ID,\n");  
		sql.append("       x.MODEL_NAME\n");  
		sql.append("  from (select g.SERIES_CODE,\n");  
		sql.append("               g.SERIES_NAME,\n");  
		sql.append("               g.MODEL_CODE,\n");  
		sql.append("               g.GROUP_ID,\n");  
		sql.append("               g.MODEL_NAME,\n");  
		sql.append("               a.FORECAST_YEAR,\n");  
		sql.append("               a.FORECAST_MONTH,\n");  
		sql.append("               a.org_type,\n");  
		sql.append("               a.forecast_type,\n"); //zxf
		sql.append("               nvl(sum(a.FORECAST_AMOUNT), 0) amount\n");  
		sql.append("          from (select nvl(sum(d.FORECAST_AMOUNT),0) FORECAST_AMOUNT,\n");  
		sql.append("                       f.FORECAST_MONTH,\n");  
		sql.append("                       f.FORECAST_ID,\n");  
		sql.append("                       d.GROUP_ID,\n");  
		sql.append("                       f.ORG_TYPE,\n");  
		sql.append("                       f.FORECAST_YEAR,\n");  
		sql.append("                       f.forecast_type\n"); //zxf
		sql.append("                  from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                       TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                 where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
		sql.append("                   and f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
		sql.append("                   and f.DEALER_ID in\n");  
		sql.append("                       (select r.dealer_id\n");  
		sql.append("                          from vw_org_dealer r\n");  
		sql.append("                         where r.ROOT_ORG_ID = "+orgId+")\n");  
		sql.append("                group by d.GROUP_ID,f.FORECAST_ID, f.FORECAST_YEAR, f.ORG_TYPE, f.FORECAST_MONTH,f.forecast_type \n");//zxf
		sql.append("                union all\n");  
		sql.append("                select d.FORECAST_AMOUNT,\n");  
		sql.append("                       f.FORECAST_MONTH,\n");  
		sql.append("                       f.FORECAST_ID,\n");  
		sql.append("                       d.GROUP_ID,\n");  
		sql.append("                       f.ORG_TYPE,\n");  
		sql.append("                       f.FORECAST_YEAR,\n");  
		sql.append("                       f.forecast_type\n");  //zxf
		sql.append("                  from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                       TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                 where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_UNCONFIRM+"\n");  
		sql.append("                   and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
		sql.append("                   and f.ORG_ID = "+orgId+") a,\n");  
		sql.append("               (select g2.GROUP_ID,\n");  
		sql.append("                       g1.GROUP_CODE,\n");  
		sql.append("                       g1.GROUP_ID GROUP_ID1,\n");  
		sql.append("                       g3.GROUP_CODE SERIES_CODE,\n");  
		sql.append("                       g3.GROUP_NAME SERIES_NAME,\n");  
		sql.append("                       g2.GROUP_CODE MODEL_CODE,\n");  
		sql.append("                       g2.GROUP_NAME MODEL_NAME\n");  
		sql.append("                  from TM_VHCL_MATERIAL_GROUP g1,\n");  
		sql.append("                       TM_VHCL_MATERIAL_GROUP g2,\n");  
		sql.append("                       TM_VHCL_MATERIAL_GROUP g3\n");  
		sql.append("                 where g1.PARENT_GROUP_ID = g2.GROUP_ID\n");  
		sql.append("                   and g2.PARENT_GROUP_ID = g3.GROUP_ID\n");  
		sql.append("                   and g1.FORCAST_FLAG = 1\n");  
		sql.append("                   and g2.FORCAST_FLAG = 1\n");  
		sql.append("                   and g3.FORCAST_FLAG = 1\n");  
		sql.append("                   and g1.GROUP_LEVEL = 4\n");  
		sql.append("                   and g1.GROUP_ID in\n");  
		sql.append("                       (select T1.GROUP_ID\n");  
		sql.append("                          from tm_vhcl_material_group t1\n");  
		sql.append("                         WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("                           and t1.GROUP_LEVEL = 4\n");  
//		sql.append("                         start with t1.group_id IN\n");  //zxf
//		sql.append("                                    (SELECT TAP.MATERIAL_GROUP_ID\n");  
//		sql.append("                                       FROM tm_area_group tap\n");  
//		sql.append("                                      where tap.area_id = "+areaId+")\n");  
//		sql.append("                        connect by prior t1.group_id = t1.parent_group_id)) g\n");  
		sql.append("                 )) g\n");//zxf
		sql.append("         where a.GROUP_ID(+) = g.GROUP_ID1\n");  
		sql.append("         group by g.SERIES_CODE,\n");  
		sql.append("                  g.SERIES_NAME,\n");  
		sql.append("                  g.MODEL_CODE,\n");  
		sql.append("                  g.GROUP_ID,\n");  
		sql.append("                  g.MODEL_NAME,\n");  
		sql.append("                  a.FORECAST_YEAR,\n");  
		sql.append("                  a.org_type,\n");  
		sql.append("                  a.FORECAST_MONTH,a.forecast_type) X\n");  //zxf
		sql.append(" order by x.model_code");
		sql.append(") Y\n");
		sql.append(" GROUP BY Y.SERIES_CODE,\n");  
		sql.append("       Y.SERIES_NAME,\n");  
		sql.append("       Y.MODEL_CODE,\n");  
		sql.append("       Y.GROUP_ID,\n");  
		sql.append("       Y.MODEL_NAME");
		sql.append("       ORDER BY Y.series_code");
		
		return sql.toString();
	}
	
	/**
	 * 需求预测调整:微车分销中心
	 * @param porgId
	 * @param orgId
	 * @param areaId
	 * @param mapList
	 * @param companyId
	 * @return
	 */
	private String getSubForecastSql_CVS(String orgId,List<Map<String, Object>> mapList,String companyId){
		StringBuffer sql=new StringBuffer("");


		sql.append("select Y.SERIES_CODE,\n");
		sql.append("       Y.SERIES_NAME,\n");  
		sql.append("       Y.MODEL_CODE,\n");  
		for(int i=0;i<mapList.size();i++){
			sql.append("       SUM(D"+i+") D"+i+",\n");  
			sql.append("       SUM(S"+i+") S"+i+",\n");  
		}
		sql.append("       Y.GROUP_ID,\n");  
		sql.append("       Y.MODEL_NAME\n");
		sql.append("from (\n");
		sql.append("select x.SERIES_CODE,\n");
		sql.append("       x.SERIES_NAME,\n");  
		sql.append("       x.MODEL_CODE,\n");  
		for(int i=0;i<mapList.size();i++){
		    Map<String, Object> map=mapList.get(i);
			sql.append("       case\n");  
			sql.append("         when x.FORECAST_YEAR = "+map.get("YEAR")+" and x.FORECAST_MONTH = "+map.get("MONTH")+" and x.ORG_TYPE="+Constant.ORG_TYPE_DEALER+" then\n");  
			sql.append("          x.AMOUNT\n");  
			sql.append("         else\n");  
			sql.append("          0\n");  
			sql.append("       end D"+i+",\n");
			sql.append("       case\n");  
			sql.append("         when x.FORECAST_YEAR = "+map.get("YEAR")+" and x.FORECAST_MONTH = "+map.get("MONTH")+" and x.ORG_TYPE="+Constant.ORG_TYPE_OEM+" then\n");  
			sql.append("          x.AMOUNT\n");  
			sql.append("         else\n");  
			sql.append("          0\n");  
			sql.append("       end S"+i+",\n"); 
		}
		sql.append("       x.GROUP_ID,\n");  
		sql.append("       x.MODEL_NAME\n");  
		sql.append("  from (select g.SERIES_CODE,\n");  
		sql.append("               g.SERIES_NAME,\n");  
		sql.append("               g.MODEL_CODE,\n");  
		sql.append("               g.GROUP_ID,\n");  
		sql.append("               g.MODEL_NAME,\n");  
		sql.append("               a.FORECAST_YEAR,\n");  
		sql.append("               a.FORECAST_MONTH,\n");  
		sql.append("               a.org_type,\n");  
		sql.append("               nvl(sum(a.FORECAST_AMOUNT), 0) amount\n");  
		sql.append("          from (select nvl(sum(d.FORECAST_AMOUNT),0) FORECAST_AMOUNT,\n");  
		sql.append("                       f.FORECAST_MONTH,\n");  
		sql.append("                       f.FORECAST_ID,\n");  
		sql.append("                       d.GROUP_ID,\n");  
		sql.append("                       f.ORG_TYPE,\n");  
		sql.append("                       f.FORECAST_YEAR\n");  
		sql.append("                  from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                       TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                 where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
		sql.append("                   and f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
		//sql.append("                   and f.area_id = "+areaId+"\n"); //zxf 
		sql.append("                   and exists\n");  
		sql.append("                       (select r.dealer_id\n");  
		sql.append("                          from vw_org_dealer r\n");  
		sql.append("                         where r.dealer_id = f.DEALER_ID and r.root_org_id = "+orgId+")\n");  
		sql.append("                group by d.GROUP_ID,f.FORECAST_ID, f.FORECAST_YEAR, f.ORG_TYPE, f.FORECAST_MONTH \n");
		sql.append("                union all\n");  
		sql.append("                select d.FORECAST_AMOUNT,\n");  
		sql.append("                       f.FORECAST_MONTH,\n");  
		sql.append("                       f.FORECAST_ID,\n");  
		sql.append("                       d.GROUP_ID,\n");  
		sql.append("                       f.ORG_TYPE,\n");  
		sql.append("                       f.FORECAST_YEAR\n");  
		sql.append("                  from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                       TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                 where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_UNCONFIRM+"\n");  
		sql.append("                   and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
		//sql.append("                   and f.area_id = "+areaId+"\n");  zxf
		sql.append("                   and f.ORG_ID = "+orgId+") a,\n");  
		
//		sql.append("(select /*+ all_rows*/\n");
//		sql.append(" g2.GROUP_ID,\n");  
//		sql.append(" g1.GROUP_CODE,\n");  
//		sql.append(" g1.GROUP_ID   GROUP_ID1,\n");  
//		sql.append(" g3.GROUP_CODE SERIES_CODE,\n");  
//		sql.append(" g3.GROUP_NAME SERIES_NAME,\n");  
//		sql.append(" g2.GROUP_CODE MODEL_CODE,\n");  
//		sql.append(" g2.GROUP_NAME MODEL_NAME\n");  
//		sql.append("  from TM_VHCL_MATERIAL_GROUP g1,\n");  
//		sql.append("       TM_VHCL_MATERIAL_GROUP g2,\n");  
//		sql.append("       TM_VHCL_MATERIAL_GROUP g3\n");  
//		sql.append(" where g1.PARENT_GROUP_ID = g2.GROUP_ID\n");  
//		sql.append("   and g2.PARENT_GROUP_ID = g3.GROUP_ID\n");  
//		sql.append("   and g1.GROUP_LEVEL = 4\n");  
//		sql.append("   and g1.forcast_flag = 1\n");  
//		sql.append("   and g2.forcast_flag = 1\n");  
//		sql.append("   and g3.forcast_flag = 1\n");  
//		sql.append("   and g1.status = "+Constant.STATUS_ENABLE+"\n");  
//		sql.append("   and (exists (SELECT TAP.MATERIAL_GROUP_ID\n");  
//		sql.append("                  FROM tm_area_group tap\n");  
//		sql.append("                 where tap.area_id = ").append(areaId).append("\n");  
//		sql.append("                   and tap.material_group_id = g1.group_id) or exists\n");  
//		sql.append("        (SELECT TAP.MATERIAL_GROUP_ID\n");  
//		sql.append("           FROM tm_area_group tap\n");  
//		sql.append("          where tap.area_id = ").append(areaId).append("\n");  
//		sql.append("            and tap.material_group_id = g2.group_id) or exists\n");  
//		sql.append("        (SELECT TAP.MATERIAL_GROUP_ID\n");  
//		sql.append("           FROM tm_area_group tap\n");  
//		sql.append("          where tap.area_id = ").append(areaId).append("\n");  
//		sql.append("            and tap.material_group_id = g3.group_id))) g\n");
		
		sql.append("               (select g2.GROUP_ID,\n");  
		sql.append("                       g1.GROUP_CODE,\n");  
		sql.append("                       g1.GROUP_ID GROUP_ID1,\n");  
		sql.append("                       g3.GROUP_CODE SERIES_CODE,\n");  
		sql.append("                       g3.GROUP_NAME SERIES_NAME,\n");  
		sql.append("                       g2.GROUP_CODE MODEL_CODE,\n");  
		sql.append("                       g2.GROUP_NAME MODEL_NAME\n");  
		sql.append("                  from TM_VHCL_MATERIAL_GROUP g1,\n");  
		sql.append("                       TM_VHCL_MATERIAL_GROUP g2,\n");  
		sql.append("                       TM_VHCL_MATERIAL_GROUP g3\n");  
		sql.append("                 where g1.PARENT_GROUP_ID = g2.GROUP_ID\n");  
		sql.append("                   and g2.PARENT_GROUP_ID = g3.GROUP_ID\n");  
		sql.append("                   and g1.FORCAST_FLAG = 1\n");  
		sql.append("                   and g2.FORCAST_FLAG = 1\n");  
		sql.append("                   and g3.FORCAST_FLAG = 1\n");  
		sql.append("                   and g1.GROUP_LEVEL = 4\n");  
		sql.append("                   and g1.GROUP_ID in\n");  
		sql.append("                       (select T1.GROUP_ID\n");  
		sql.append("                          from tm_vhcl_material_group t1\n");  
		sql.append("                         WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("                           and t1.GROUP_LEVEL = 4\n");  
//		sql.append("                         start with t1.group_id IN\n");  //zxf
//		sql.append("                                    (SELECT TAP.MATERIAL_GROUP_ID\n");  
//		sql.append("                                       FROM tm_area_group tap\n");  
//		sql.append("                                      where tap.area_id = "+areaId+")\n");  
//		sql.append("                        connect by prior t1.group_id = t1.parent_group_id)) g\n"); 
		sql.append("                 )) g\n");//zxf
		sql.append("         where a.GROUP_ID(+) = g.GROUP_ID1\n");  
		sql.append("         group by g.SERIES_CODE,\n");  
		sql.append("                  g.SERIES_NAME,\n");  
		sql.append("                  g.MODEL_CODE,\n");  
		sql.append("                  g.GROUP_ID,\n");  
		sql.append("                  g.MODEL_NAME,\n");  
		sql.append("                  a.FORECAST_YEAR,\n");  
		sql.append("                  a.org_type,\n");  
		sql.append("                  a.FORECAST_MONTH) X\n");  
		sql.append(" order by x.model_code");
		sql.append(") Y\n");
		sql.append(" GROUP BY Y.SERIES_CODE,\n");  
		sql.append("       Y.SERIES_NAME,\n");  
		sql.append("       Y.MODEL_CODE,\n");  
		sql.append("       Y.GROUP_ID,\n");  
		sql.append("       Y.MODEL_NAME");
		sql.append("       ORDER BY Y.series_code");
		
		return sql.toString();
	}
	/*
	 * 总部查询SQL
	 */
	private String getDeptForecasetSql(String porgId,String orgId,List<Map<String, Object>> mapList,String companyId){
		StringBuffer sql=new StringBuffer("");


		sql.append("select Y.SERIES_CODE,\n");
		sql.append("       Y.SERIES_NAME,\n");  
		sql.append("       Y.MODEL_CODE,\n");  
		for(int i=0;i<mapList.size();i++){
			sql.append("       SUM(D"+i+") D"+i+",\n");  
			sql.append("       SUM(S"+i+") S"+i+",\n");  
		}
		sql.append("       Y.GROUP_ID,\n");  
		sql.append("       Y.MODEL_NAME\n");
		sql.append("from (\n");
		sql.append("select x.SERIES_CODE,\n");
		sql.append("       x.SERIES_NAME,\n");  
		sql.append("       x.MODEL_CODE,\n");  
		String month = PlanUtil.getRadomDate(1,"");//zxf
		for(int i=0;i<mapList.size();i++){			  			    	
		    Map<String, Object> map=mapList.get(i);
		    if(month.equals(map.get("MONTH"))){
		    	sql.append("       case\n");  
				sql.append("         when x.FORECAST_YEAR = "+map.get("YEAR")+" and x.FORECAST_MONTH = "+map.get("MONTH")+" and x.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and x.duty='sub' and x.forecast_type =60591001 then\n");  
				sql.append("          x.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          0\n");  
				sql.append("       end D"+i+",\n");
				sql.append("       case\n");  
				sql.append("         when x.FORECAST_YEAR = "+map.get("YEAR")+" and x.FORECAST_MONTH = "+map.get("MONTH")+" and x.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and x.duty='dept' and x.forecast_type =60591001 then\n");  
				sql.append("          x.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          0\n");  
				sql.append("       end S"+i+",\n"); 
		    }
		    else{
				sql.append("       case\n");  
				sql.append("         when x.FORECAST_YEAR = "+map.get("YEAR")+" and x.FORECAST_MONTH = "+map.get("MONTH")+" and x.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and x.duty='sub' and x.forecast_type =60591002 then\n");  
				sql.append("          x.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          0\n");  
				sql.append("       end D"+i+",\n");
				sql.append("       case\n");  
				sql.append("         when x.FORECAST_YEAR = "+map.get("YEAR")+" and x.FORECAST_MONTH = "+map.get("MONTH")+" and x.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and x.duty='dept' and x.forecast_type =60591002 then\n");  
				sql.append("          x.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          0\n");  
				sql.append("       end S"+i+",\n"); 
			}
		}
		sql.append("       x.GROUP_ID,\n");  
		sql.append("       x.MODEL_NAME\n");  
		sql.append("  from (select g.SERIES_CODE,\n");  
		sql.append("               g.SERIES_NAME,\n");  
		sql.append("               g.MODEL_CODE,\n");  
		sql.append("               g.GROUP_ID,\n");  
		sql.append("               g.MODEL_NAME,\n");  
		sql.append("               a.FORECAST_YEAR,\n");  
		sql.append("               a.FORECAST_MONTH,\n"); 
		sql.append("               a.duty,\n"); 
		sql.append("               a.org_type,\n");  
		sql.append("               a.forecast_type,\n"); 
		sql.append("               nvl(sum(a.FORECAST_AMOUNT), 0) amount\n");  
		sql.append("          from (select nvl(sum(d.FORECAST_AMOUNT),0) FORECAST_AMOUNT,\n");  
		sql.append("                       f.FORECAST_MONTH,\n");  
		sql.append("                       f.FORECAST_ID,\n");  
		sql.append("                       d.GROUP_ID,\n");  
		sql.append("                       f.ORG_TYPE,\n");
		sql.append("                       'sub' duty,\n");
		sql.append("                       f.FORECAST_YEAR,\n");  
		sql.append("                       f.FORECAST_TYPE\n"); //zxf
		sql.append("                  from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                       TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                 where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
		sql.append("                   and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
		if(!"('2010010100070674')".equals(orgId)){ // YH 2011.6.23 按大区统计
			sql.append("              and f.ORG_ID in "+orgId+" \n");
		}
		/*sql.append("                   and f.ORG_ID in\n");  
		sql.append("                   (select org.ORG_ID\n");
		sql.append("                          from TM_ORG org\n");  
		sql.append("                           where org.duty_type="+Constant.DUTY_TYPE_LARGEREGION+"\n");
		sql.append("                           and org.status="+Constant.STATUS_ENABLE+"\n");
		sql.append("                           connect by prior org.ORG_ID = org.PARENT_ORG_ID\n");  
		sql.append("                           start with org.org_id = "+porgId+")\n");*/
		sql.append("                group by d.GROUP_ID,f.FORECAST_ID, f.FORECAST_YEAR, f.ORG_TYPE, f.FORECAST_MONTH,f.forecast_type \n");//zxf
		sql.append("                union all\n");  
		sql.append("                select d.FORECAST_AMOUNT,\n");  
		sql.append("                       f.FORECAST_MONTH,\n");  
		sql.append("                       f.FORECAST_ID,\n");  
		sql.append("                       d.GROUP_ID,\n");  
		sql.append("                       f.ORG_TYPE,\n");
		sql.append("                       'dept' duty,\n");
		sql.append("                       f.FORECAST_YEAR,\n");
		sql.append("                       f.FORECAST_TYPE\n"); //zxf
		sql.append("                  from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                       TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                 where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_UNCONFIRM+"\n");  
		sql.append("                   and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
		sql.append("                   and f.ORG_ID in "+orgId+") a,\n");  
		sql.append("               (select g2.GROUP_ID,\n");  
		sql.append("                       g1.GROUP_CODE,\n");  
		sql.append("                       g1.GROUP_ID GROUP_ID1,\n");  
		sql.append("                       g3.GROUP_CODE SERIES_CODE,\n");  
		sql.append("                       g3.GROUP_NAME SERIES_NAME,\n");  
		sql.append("                       g2.GROUP_CODE MODEL_CODE,\n");  
		sql.append("                       g2.GROUP_NAME MODEL_NAME\n");  
		sql.append("                  from TM_VHCL_MATERIAL_GROUP g1,\n");  
		sql.append("                       TM_VHCL_MATERIAL_GROUP g2,\n");  
		sql.append("                       TM_VHCL_MATERIAL_GROUP g3\n");  
		sql.append("                 where g1.PARENT_GROUP_ID = g2.GROUP_ID\n");  
		sql.append("                   and g2.PARENT_GROUP_ID = g3.GROUP_ID\n"); 
		sql.append("                   and g1.FORCAST_FLAG = 1\n");  
		sql.append("                   and g2.FORCAST_FLAG = 1\n");  
		sql.append("                   and g3.FORCAST_FLAG = 1\n");   
		sql.append("                   and g1.GROUP_LEVEL = 4\n");  
		sql.append("                   and g1.GROUP_ID in\n");  
		sql.append("                       (select T1.GROUP_ID\n");  
		sql.append("                          from tm_vhcl_material_group t1\n");  
		sql.append("                         WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("                           and t1.GROUP_LEVEL = 4\n");  
//		sql.append("                         start with t1.group_id IN\n");  //zxf
//		sql.append("                                    (SELECT TAP.MATERIAL_GROUP_ID\n");  
//		sql.append("                                       FROM tm_area_group tap\n");  
//		sql.append("                                      where tap.area_id = "+areaId+")\n");  
//		sql.append("                        connect by prior t1.group_id = t1.parent_group_id)) g\n");  
		sql.append("                 )) g\n");//zxf
		sql.append("         where a.GROUP_ID(+) = g.GROUP_ID1\n");  
		sql.append("         group by g.SERIES_CODE,\n");  
		sql.append("                  g.SERIES_NAME,\n");  
		sql.append("                  g.MODEL_CODE,\n");  
		sql.append("                  g.GROUP_ID,\n");  
		sql.append("                  g.MODEL_NAME,\n");  
		sql.append("                  a.FORECAST_YEAR,\n");  
		sql.append("                  a.org_type,\n");  
		sql.append("                  a.duty,\n");
		sql.append("                  a.FORECAST_MONTH,a.forecast_type) X\n");  //zxf
		sql.append(" order by x.model_code");
		sql.append(") Y\n");
		sql.append(" GROUP BY Y.SERIES_CODE,\n");  
		sql.append("       Y.SERIES_NAME,\n");  
		sql.append("       Y.MODEL_CODE,\n");  
		sql.append("       Y.GROUP_ID,\n");  
		sql.append("       Y.MODEL_NAME");
		sql.append("       ORDER BY Y.series_code");
		
		return sql.toString();
	}
	
	/*
	 * 总部查询SQL
	 */
	private String getDeptForecasetSql_CVS(String porgId,String orgId,List<Map<String, Object>> mapList,String companyId){
		StringBuffer sql=new StringBuffer("");


		sql.append("select Y.SERIES_CODE,\n");
		sql.append("       Y.SERIES_NAME,\n");  
		sql.append("       Y.MODEL_CODE,\n");  
		for(int i=0;i<mapList.size();i++){
			sql.append("       SUM(D"+i+") D"+i+",\n");  
			sql.append("       SUM(S"+i+") S"+i+",\n");  
		}
		sql.append("       Y.GROUP_ID,\n");  
		sql.append("       Y.MODEL_NAME\n");
		sql.append("from (\n");
		sql.append("select x.SERIES_CODE,\n");
		sql.append("       x.SERIES_NAME,\n");  
		sql.append("       x.MODEL_CODE,\n");  
		for(int i=0;i<mapList.size();i++){
		    Map<String, Object> map=mapList.get(i);
			sql.append("       case\n");  
			sql.append("         when x.FORECAST_YEAR = "+map.get("YEAR")+" and x.FORECAST_MONTH = "+map.get("MONTH")+" and x.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and x.duty='sub' then\n");  
			sql.append("          x.AMOUNT\n");  
			sql.append("         else\n");  
			sql.append("          0\n");  
			sql.append("       end D"+i+",\n");
			sql.append("       case\n");  
			sql.append("         when x.FORECAST_YEAR = "+map.get("YEAR")+" and x.FORECAST_MONTH = "+map.get("MONTH")+" and x.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and x.duty='dept' then\n");  
			sql.append("          x.AMOUNT\n");  
			sql.append("         else\n");  
			sql.append("          0\n");  
			sql.append("       end S"+i+",\n"); 
		}
		sql.append("       x.GROUP_ID,\n");  
		sql.append("       x.MODEL_NAME\n");  
		sql.append("  from (select g.SERIES_CODE,\n");  
		sql.append("               g.SERIES_NAME,\n");  
		sql.append("               g.MODEL_CODE,\n");  
		sql.append("               g.GROUP_ID,\n");  
		sql.append("               g.MODEL_NAME,\n");  
		sql.append("               a.FORECAST_YEAR,\n");  
		sql.append("               a.FORECAST_MONTH,\n"); 
		sql.append("               a.duty,\n"); 
		sql.append("               a.org_type,\n");  
		sql.append("               nvl(sum(a.FORECAST_AMOUNT), 0) amount\n");  
		sql.append("          from (select nvl(sum(d.FORECAST_AMOUNT),0) FORECAST_AMOUNT,\n");  
		sql.append("                       f.FORECAST_MONTH,\n");  
		sql.append("                       f.FORECAST_ID,\n");  
		sql.append("                       d.GROUP_ID,\n");  
		sql.append("                       f.ORG_TYPE,\n");
		sql.append("                       'sub' duty,\n");
		sql.append("                       f.FORECAST_YEAR\n");  
		sql.append("                  from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                       TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                 where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
		sql.append("                   and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
		if(!"('4010010100070674')".equals(orgId)){ // YH 2011.6.23 按大区统计
			sql.append("              and f.ORG_ID in "+orgId+" \n");
		}
		/*sql.append("                   and f.ORG_ID in\n");  
		sql.append("                   (select org.ORG_ID\n");
		sql.append("                          from TM_ORG org\n");  
		sql.append("                           where org.duty_type="+Constant.DUTY_TYPE_LARGEREGION+"\n");
		sql.append("                           and org.status="+Constant.STATUS_ENABLE+"\n");
		sql.append("                           connect by prior org.ORG_ID = org.PARENT_ORG_ID\n");  
		sql.append("                           start with org.org_id = "+porgId+")\n");*/
		sql.append("                group by d.GROUP_ID,f.FORECAST_ID, f.FORECAST_YEAR, f.ORG_TYPE, f.FORECAST_MONTH \n");
		sql.append("                union all\n");  
		sql.append("                select d.FORECAST_AMOUNT,\n");  
		sql.append("                       f.FORECAST_MONTH,\n");  
		sql.append("                       f.FORECAST_ID,\n");  
		sql.append("                       d.GROUP_ID,\n");  
		sql.append("                       f.ORG_TYPE,\n");
		sql.append("                       'dept' duty,\n");
		sql.append("                       f.FORECAST_YEAR\n");  
		sql.append("                  from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                       TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                 where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_UNCONFIRM+"\n");  
		sql.append("                   and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
		sql.append("                   ) a,\n"); // zxf
		sql.append("               (select g2.GROUP_ID,\n");  
		sql.append("                       g1.GROUP_CODE,\n");  
		sql.append("                       g1.GROUP_ID GROUP_ID1,\n");  
		sql.append("                       g3.GROUP_CODE SERIES_CODE,\n");  
		sql.append("                       g3.GROUP_NAME SERIES_NAME,\n");  
		sql.append("                       g2.GROUP_CODE MODEL_CODE,\n");  
		sql.append("                       g2.GROUP_NAME MODEL_NAME\n");  
		sql.append("                  from TM_VHCL_MATERIAL_GROUP g1,\n");  
		sql.append("                       TM_VHCL_MATERIAL_GROUP g2,\n");  
		sql.append("                       TM_VHCL_MATERIAL_GROUP g3\n");  
		sql.append("                 where g1.PARENT_GROUP_ID = g2.GROUP_ID\n");  
		sql.append("                   and g2.PARENT_GROUP_ID = g3.GROUP_ID\n"); 
		sql.append("                   and g1.FORCAST_FLAG = 1\n");  
		sql.append("                   and g2.FORCAST_FLAG = 1\n");  
		sql.append("                   and g3.FORCAST_FLAG = 1\n");   
		sql.append("                   and g1.GROUP_LEVEL = 4\n");  
		sql.append("                   and g1.GROUP_ID in\n");  
		sql.append("                       (select T1.GROUP_ID\n");  
		sql.append("                          from tm_vhcl_material_group t1\n");  
		sql.append("                         WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("                           and t1.GROUP_LEVEL = 4\n");  
//		sql.append("                         start with t1.group_id IN\n");  zxf
//		sql.append("                                    (SELECT TAP.MATERIAL_GROUP_ID\n");  
//		sql.append("                                       FROM tm_area_group tap\n");  
//		sql.append("                                      where tap.area_id = "+areaId+")\n");  
//		sql.append("                        connect by prior t1.group_id = t1.parent_group_id)) g\n");  
		sql.append("                ) ) g\n");//zxf
		sql.append("         where a.GROUP_ID(+) = g.GROUP_ID1\n");  
		sql.append("         group by g.SERIES_CODE,\n");  
		sql.append("                  g.SERIES_NAME,\n");  
		sql.append("                  g.MODEL_CODE,\n");  
		sql.append("                  g.GROUP_ID,\n");  
		sql.append("                  g.MODEL_NAME,\n");  
		sql.append("                  a.FORECAST_YEAR,\n");  
		sql.append("                  a.org_type,\n");  
		sql.append("                  a.duty,\n");
		sql.append("                  a.FORECAST_MONTH) X\n");  
		sql.append(" order by x.model_code");
		sql.append(") Y\n");
		sql.append(" GROUP BY Y.SERIES_CODE,\n");  
		sql.append("       Y.SERIES_NAME,\n");  
		sql.append("       Y.MODEL_CODE,\n");  
		sql.append("       Y.GROUP_ID,\n");  
		sql.append("       Y.MODEL_NAME");
		sql.append("       ORDER BY Y.series_code");
		
		return sql.toString();
	}
	
	/*
	 * 区域查询SQL，SUB单指DEALE上一级ORG
	 */
	
	private String getSubForecastSaveSearchSql(String pgroupId,String orgId ,List<Map<String, Object>> mapList,String companyId){
		StringBuffer sql=new StringBuffer();
		sql.append("select Y.GROUP_ID,\n");
		sql.append("       Y.group_name,\n");  
		for(int i=0;i<mapList.size();i++){
			sql.append("       SUM(D"+i+") D"+i+",\n");  
			sql.append("       NVL(SUM(S"+i+"),SUM(D"+i+")) S"+i+",\n");  
		}
		sql.append("       Y.group_code\n");
		sql.append("from (\n");
		sql.append("select aa.group_id GROUP_ID,\n");
		sql.append("       aa.group_name,\n"); 
		String month = PlanUtil.getRadomDate(1,"");//zxf
		for(int i=0;i<mapList.size();i++){
		    Map<String, Object> map=mapList.get(i);
		    if(month.equals(map.get("MONTH"))){
		    	sql.append("       case\n");  
				sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+" and AA.ORG_TYPE="+Constant.ORG_TYPE_DEALER+" and AA.forecast_type=60591001 then\n");  
				sql.append("          aa.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          0\n");  
				sql.append("       end D"+i+",\n");
				
				sql.append("       case\n");  
				sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+" and AA.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and AA.forecast_type=60591001 then\n");  
				sql.append("          aa.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          null\n");  
				sql.append("       end S"+i+",\n");
		    }
		    else{
				sql.append("       case\n");  
				sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+" and AA.ORG_TYPE="+Constant.ORG_TYPE_DEALER+" and AA.forecast_type=60591002 then\n");  
				sql.append("          aa.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          0\n");  
				sql.append("       end D"+i+",\n");
				
				sql.append("       case\n");  
				sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+" and AA.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and AA.forecast_type=60591002 then\n");  
				sql.append("          aa.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          null\n");  
				sql.append("       end S"+i+",\n");
			}
		}
		  
		sql.append("       aa.group_code\n");  
		sql.append("  from (\n");  
		sql.append("        select b.group_id,\n");  
		sql.append("                b.group_code,\n");  
		sql.append("                b.group_name,\n"); 
		sql.append("                a.org_type,\n");
		sql.append("                nvl(a.FORECAST_AMOUNT, 0) amount,\n");  
		sql.append("                a.FORECAST_YEAR,\n");  
		sql.append("                a.FORECAST_MONTH,\n"); 
		sql.append("                a.FORECAST_TYPE\n");
		sql.append("          from (select  nvl(sum(d.FORECAST_AMOUNT),0) FORECAST_AMOUNT,\n");  
		sql.append("                        f.FORECAST_MONTH,\n");  
		sql.append("                        d.GROUP_ID,\n");
		sql.append("                        f.ORG_TYPE,\n"); 
		sql.append("                        f.FORECAST_YEAR,\n");  
		sql.append("                        f.FORECAST_TYPE\n");//zxf
		sql.append("                   from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                        TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                  where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
		sql.append("                   and f.DEALER_ID in\n");
		/**  原长安程序：
		sql.append("                       (select r.dealer_id\n");  
		sql.append("                          from TM_DEALER_ORG_RELATION r\n");  
		sql.append("                         where r.org_id = "+orgId+")\n"); 
		**/
		// 长安铃木程序：start   2012-11-19
		sql.append("                       (select r.dealer_id\n");  
		sql.append("                          from vw_org_dealer r\n");  
		sql.append("                         where r.ROOT_ORG_ID = "+orgId+")\n");		
		// 长安铃木程序：end
		sql.append("                group by d.GROUP_ID, f.FORECAST_YEAR, f.ORG_TYPE, f.FORECAST_MONTH,f.forecast_type \n");//zxf
		sql.append("                union all\n");  
		sql.append("                select d.FORECAST_AMOUNT,\n");  
		sql.append("                       f.FORECAST_MONTH,\n");  
		sql.append("                       d.GROUP_ID,\n");  
		sql.append("                       f.ORG_TYPE,\n");  
		sql.append("                       f.FORECAST_YEAR,\n");  
		sql.append("                       f.FORECAST_TYPE\n");//zxf
		sql.append("                  from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                       TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                 where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_UNCONFIRM+"\n");  
		sql.append("                   and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
		sql.append("                   and f.ORG_ID = "+orgId+") a,\n"); 
		
		sql.append("                (select T1.GROUP_ID, t1.GROUP_CODE, t1.group_name\n");  
		sql.append("                   from tm_vhcl_material_group t1\n");  
		sql.append("                  WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("                    and t1.GROUP_LEVEL = 4\n");  
		sql.append("                    and t1.FORCAST_FLAG = 1\n");  
		sql.append("                    and t1.PARENT_GROUP_ID = "+pgroupId+"\n");  
//		sql.append("                  start with t1.group_id IN\n");  //zxf
//		sql.append("                             (SELECT TAP.MATERIAL_GROUP_ID\n");  
//		sql.append("                                FROM tm_area_group tap\n");  
//		sql.append("                               where tap.area_id = "+areaId+")\n");  
//		sql.append("                 connect by prior t1.group_id = t1.parent_group_id) b\n"); 
//		sql.append("                 connect by prior t1.group_id = t1.parent_group_id) b\n"); 
		sql.append("                 ) b\n");
		sql.append("         where a.group_id(+) = b.group_id) aa\n");

		sql.append(") Y\n");
		sql.append(" GROUP BY Y.GROUP_ID,\n");  
		sql.append("       Y.GROUP_NAME,\n");  
		sql.append("       Y.GROUP_CODE\n");  
		sql.append("       ORDER BY Y.GROUP_CODE");
		
		return sql.toString();
	}
	
	
	private String getSubForecastSaveSearchSqlCVS(String pgroupId,String orgId ,List<Map<String, Object>> mapList,String companyId){
		StringBuffer sql=new StringBuffer();
		sql.append("select Y.GROUP_ID,\n");
		sql.append("       Y.group_name,\n");  
		for(int i=0;i<mapList.size();i++){
			sql.append("       SUM(D"+i+") D"+i+",\n");  
			sql.append("       NVL(SUM(S"+i+"),SUM(D"+i+")) S"+i+",\n");  
		}
		sql.append("       Y.group_code\n");
		sql.append("from (\n");
		sql.append("select aa.group_id GROUP_ID,\n");
		sql.append("       aa.group_name,\n");  
		
		
		String month = PlanUtil.getRadomDate(1,"");//zxf		
		for(int i=0;i<mapList.size();i++){
		    Map<String, Object> map=mapList.get(i);
		    if(month.equals(map.get("MONTH"))){
		    	sql.append("       case\n");  
				sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+" and AA.ORG_TYPE="+Constant.ORG_TYPE_DEALER+" and AA.forecast_type=60591001  then\n");  
				sql.append("          aa.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          0\n");  
				sql.append("       end D"+i+",\n");
				
				sql.append("       case\n");  
				sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+" and AA.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and AA.forecast_type=60591001 then\n");  
				sql.append("          aa.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          null\n");  
				sql.append("       end S"+i+",\n");
		    }
		    else{
				sql.append("       case\n");  
				sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+" and AA.ORG_TYPE="+Constant.ORG_TYPE_DEALER+" and AA.forecast_type=60591002 then\n");  
				sql.append("          aa.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          0\n");  
				sql.append("       end D"+i+",\n");
				
				sql.append("       case\n");  
				sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+" and AA.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and AA.forecast_type=60591002 then\n");  
				sql.append("          aa.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          null\n");  
				sql.append("       end S"+i+",\n");
			}
		}
		  
		sql.append("       aa.group_code\n");  
		sql.append("  from (\n");  
		sql.append("        select b.group_id,\n");  
		sql.append("                b.group_code,\n");  
		sql.append("                b.group_name,\n"); 
		sql.append("                a.org_type,\n");
		sql.append("                nvl(a.FORECAST_AMOUNT, 0) amount,\n");  
		sql.append("                a.FORECAST_YEAR,\n");  
		sql.append("                a.FORECAST_MONTH,\n");  //zxf
		sql.append("                a.FORECAST_type\n"); //zxf
		sql.append("          from (select  nvl(sum(d.FORECAST_AMOUNT),0) FORECAST_AMOUNT,\n");  
		sql.append("                        f.FORECAST_MONTH,\n");  
		sql.append("                        d.GROUP_ID,\n");
		sql.append("                        f.ORG_TYPE,\n"); 
		sql.append("                        f.FORECAST_YEAR,\n");  //zxf
		sql.append("                        f.FORECAST_type\n");//zxf
		sql.append("                   from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                        TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                  where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
		//sql.append("                   and f.area_id = "+areaId+"\n");  //zxf
		sql.append("                   and exists\n");  
		sql.append("                       (select r.dealer_id\n");  
		sql.append("                          from vw_org_dealer r\n");  
		sql.append("                         where r.dealer_id = f.DEALER_ID and r.root_org_id = "+orgId+")\n");  
		sql.append("                group by d.GROUP_ID, f.FORECAST_YEAR, f.ORG_TYPE, f.FORECAST_MONTH,f.forecast_type \n");
		sql.append("                union all\n");  
		sql.append("                select d.FORECAST_AMOUNT,\n");  
		sql.append("                       f.FORECAST_MONTH,\n");  
		sql.append("                       d.GROUP_ID,\n");  
		sql.append("                       f.ORG_TYPE,\n");  
		sql.append("                       f.FORECAST_YEAR,\n");  //zxf
		sql.append("                       f.FORECAST_type\n"); //zxf
		sql.append("                  from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                       TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                 where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_UNCONFIRM+"\n");  
		sql.append("                   and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n"); 
		//sql.append("                   and f.area_id = "+areaId+"\n");  //zxf
		sql.append("                   and f.ORG_ID = "+orgId+") a,\n"); 
		
		sql.append("                (select T1.GROUP_ID, t1.GROUP_CODE, t1.group_name\n");  
		sql.append("                   from tm_vhcl_material_group t1\n");  
		sql.append("                  WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("                    and t1.GROUP_LEVEL = 4\n");  
		sql.append("                    and t1.FORCAST_FLAG = 1\n");  
		sql.append("                    and t1.PARENT_GROUP_ID = "+pgroupId+"\n");  
//		sql.append("                  start with t1.group_id IN\n");  
//		sql.append("                             (SELECT TAP.MATERIAL_GROUP_ID\n");  
//		sql.append("                                FROM tm_area_group tap\n");  
//		sql.append("                               where tap.area_id = "+areaId+")\n");  //zxf
//		sql.append("                 connect by prior t1.group_id = t1.parent_group_id) b\n");  
		sql.append("                 ) b\n");
		sql.append("         where a.group_id(+) = b.group_id) aa\n");

		sql.append(") Y\n");
		sql.append(" GROUP BY Y.GROUP_ID,\n");  
		sql.append("       Y.GROUP_NAME,\n");  
		sql.append("       Y.GROUP_CODE\n");  
		sql.append("       ORDER BY Y.GROUP_CODE");
		
		return sql.toString();
	}
	
	/**
	 * 
	 */
	private String getSubForecastSaveSearchSql_CVS(String pgroupId,String orgId,String areaId,List<Map<String, Object>> mapList,String companyId){
		StringBuffer sql=new StringBuffer();
		sql.append("select Y.GROUP_ID,\n");
		sql.append("       Y.group_name,\n");  
		for(int i=0;i<mapList.size();i++){
			sql.append("       SUM(D"+i+") D"+i+",\n");  
			sql.append("       NVL(SUM(S"+i+"),SUM(D"+i+")) S"+i+",\n");  
		}
		sql.append("       Y.group_code\n");
		sql.append("from (\n");
		sql.append("select aa.group_id GROUP_ID,\n");
		sql.append("       aa.group_name,\n");  
		for(int i=0;i<mapList.size();i++){
		    Map<String, Object> map=mapList.get(i);
			sql.append("       case\n");  
			sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+" and AA.ORG_TYPE="+Constant.ORG_TYPE_DEALER+" then\n");  
			sql.append("          aa.AMOUNT\n");  
			sql.append("         else\n");  
			sql.append("          0\n");  
			sql.append("       end D"+i+",\n");
			
			sql.append("       case\n");  
			sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+" and AA.ORG_TYPE="+Constant.ORG_TYPE_OEM+" then\n");  
			sql.append("          aa.AMOUNT\n");  
			sql.append("         else\n");  
			sql.append("          null\n");  
			sql.append("       end S"+i+",\n");
		}
		  
		sql.append("       aa.group_code\n");  
		sql.append("  from (\n");  
		sql.append("        select b.group_id,\n");  
		sql.append("                b.group_code,\n");  
		sql.append("                b.group_name,\n"); 
		sql.append("                a.org_type,\n");
		sql.append("                nvl(a.FORECAST_AMOUNT, 0) amount,\n");  
		sql.append("                a.FORECAST_YEAR,\n");  
		sql.append("                a.FORECAST_MONTH\n");  
		sql.append("          from (select  nvl(sum(d.FORECAST_AMOUNT),0) FORECAST_AMOUNT,\n");  
		sql.append("                        f.FORECAST_MONTH,\n");  
		sql.append("                        d.GROUP_ID,\n");
		sql.append("                        f.ORG_TYPE,\n"); 
		sql.append("                        f.FORECAST_YEAR\n");  
		sql.append("                   from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                        TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                  where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
		sql.append("                   and f.DEALER_ID in\n");  
		sql.append("                       (select r.dealer_id\n");  
		sql.append("                          from vw_org_dealer r\n");  
		sql.append("                         where r.root_org_id = "+orgId+")\n");  
		sql.append("                group by d.GROUP_ID, f.FORECAST_YEAR, f.ORG_TYPE, f.FORECAST_MONTH \n");
		sql.append("                union all\n");  
		sql.append("                select d.FORECAST_AMOUNT,\n");  
		sql.append("                       f.FORECAST_MONTH,\n");  
		sql.append("                       d.GROUP_ID,\n");  
		sql.append("                       f.ORG_TYPE,\n");  
		sql.append("                       f.FORECAST_YEAR\n");  
		sql.append("                  from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                       TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                 where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_UNCONFIRM+"\n");  
		sql.append("                   and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
		sql.append("                   and f.ORG_ID = "+orgId+") a,\n"); 
		
		sql.append("                (select T1.GROUP_ID, t1.GROUP_CODE, t1.group_name\n");  
		sql.append("                   from tm_vhcl_material_group t1\n");  
		sql.append("                  WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("                    and t1.GROUP_LEVEL = 4\n");  
		sql.append("                    and t1.FORCAST_FLAG = 1\n");  
		sql.append("                    and t1.PARENT_GROUP_ID = "+pgroupId+"\n");  
		sql.append("                  start with t1.group_id IN\n");  
		sql.append("                             (SELECT TAP.MATERIAL_GROUP_ID\n");  
		sql.append("                                FROM tm_area_group tap\n");  
		sql.append("                               where tap.area_id = "+areaId+")\n");  
		sql.append("                 connect by prior t1.group_id = t1.parent_group_id) b\n");  
		sql.append("         where a.group_id(+) = b.group_id) aa\n");

		sql.append(") Y\n");
		sql.append(" GROUP BY Y.GROUP_ID,\n");  
		sql.append("       Y.GROUP_NAME,\n");  
		sql.append("       Y.GROUP_CODE\n");  
		sql.append("       ORDER BY Y.GROUP_CODE");
		
		return sql.toString();
	}
	/*
	 * 区域查询SQL，SUB单指DEALE上一级ORG
	 */
	
	private String getDeptForecastSaveSearchSql(String porgId,String pgroupId,String orgId ,List<Map<String, Object>> mapList,String companyId){
		StringBuffer sql=new StringBuffer();
		sql.append("select Y.GROUP_ID,\n");
		sql.append("       Y.group_name,\n");  
		for(int i=0;i<mapList.size();i++){
			sql.append("       SUM(D"+i+") D"+i+",\n");  
			sql.append("       NVL(SUM(S"+i+"),SUM(D"+i+")) S"+i+",\n");  
		}
		sql.append("       Y.group_code\n");
		sql.append("from (\n");
		sql.append("select aa.group_id GROUP_ID,\n");
		sql.append("       aa.group_name,\n");  
		String month = PlanUtil.getRadomDate(1,"");//zxf
		for(int i=0;i<mapList.size();i++){	    	
		    Map<String, Object> map=mapList.get(i);
		    if(month.equals(map.get("MONTH"))){
		    	sql.append("       case\n");  
				sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+" and AA.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and aa.duty='sub' and aa.forecast_type = 60591001 then\n");  
				sql.append("          aa.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          0\n");  
				sql.append("       end D"+i+",\n");
				
				sql.append("       case\n");  
				sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+" and AA.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and aa.duty='dept' and aa.forecast_type = 60591001 then\n");  
				sql.append("          aa.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          null\n");  
				sql.append("       end S"+i+",\n");
		    }
		    else{
				sql.append("       case\n");  
				sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+" and AA.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and aa.duty='sub' and aa.forecast_type = 60591002 then\n");  
				sql.append("          aa.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          0\n");  
				sql.append("       end D"+i+",\n");
				
				sql.append("       case\n");  
				sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+" and AA.ORG_TYPE="+Constant.ORG_TYPE_OEM+" and aa.duty='dept' and aa.forecast_type = 60591002 then\n");  
				sql.append("          aa.AMOUNT\n");  
				sql.append("         else\n");  
				sql.append("          null\n");  
				sql.append("       end S"+i+",\n");
			}
		}
		  
		sql.append("       aa.group_code\n");  
		sql.append("  from (\n");  
		sql.append("        select b.group_id,\n");  
		sql.append("                b.group_code,\n");  
		sql.append("                b.group_name,\n"); 
		sql.append("                a.org_type,\n");
		sql.append("                nvl(a.FORECAST_AMOUNT, 0) amount,\n");  
		sql.append("                a.FORECAST_YEAR,\n");  
		sql.append("                a.duty,\n");
		sql.append("                a.FORECAST_MONTH,\n");  
		sql.append("                a.FORECAST_TYPE\n"); //zxf
		sql.append("          from (select  nvl(sum(d.FORECAST_AMOUNT),0) FORECAST_AMOUNT,\n");  
		sql.append("                        f.FORECAST_MONTH,\n");  
		sql.append("                        d.GROUP_ID,\n");
		sql.append("                        f.ORG_TYPE,\n"); 
		sql.append("                        'sub' duty,\n");
		sql.append("                        f.FORECAST_YEAR,\n");  
		sql.append("                        f.FORECAST_TYPE\n");//zxf
		sql.append("                   from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                        TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                  where f.FORECAST_ID = d.FORECAST_ID\n"); 
		sql.append("                    and f.COMPANY_ID="+companyId+"\n"); 
		sql.append("                    and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
		sql.append("                    and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n"); 
		/*sql.append("                    and f.ORG_ID in\n");  
		sql.append("                    (select org.ORG_ID\n");
		sql.append("                          from TM_ORG org\n");  
		sql.append("                           where org.duty_type="+Constant.DUTY_TYPE_LARGEREGION+"\n");
		sql.append("                           and org.status="+Constant.STATUS_ENABLE+"\n");
		sql.append("                           connect by prior org.ORG_ID = org.PARENT_ORG_ID\n");  
		sql.append("                           start with org.org_id = "+porgId+")\n");*/
		sql.append("                group by d.GROUP_ID, f.FORECAST_YEAR, f.ORG_TYPE, f.FORECAST_MONTH,f.forecast_type \n");
		sql.append("                union all\n");  
		sql.append("                select d.FORECAST_AMOUNT,\n");  
		sql.append("                       f.FORECAST_MONTH,\n");  
		sql.append("                       d.GROUP_ID,\n");  
		sql.append("                       f.ORG_TYPE,\n");  
		sql.append("                       'dept' duty,\n");
		sql.append("                       f.FORECAST_YEAR,\n");  
		sql.append("                       f.FORECAST_TYPE\n");//zxf
		sql.append("                  from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                       TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                 where f.FORECAST_ID = d.FORECAST_ID\n"); 
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_UNCONFIRM+"\n");  
		sql.append("                   and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
		sql.append("                   and f.ORG_ID = "+orgId+") a,\n"); 
		
		sql.append("                (select T1.GROUP_ID, t1.GROUP_CODE, t1.group_name\n");  
		sql.append("                   from tm_vhcl_material_group t1\n");  
		sql.append("                  WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("                    and t1.GROUP_LEVEL = 4\n");  
		sql.append("                    and t1.FORCAST_FLAG = 1\n");  
		sql.append("                    and t1.PARENT_GROUP_ID = "+pgroupId+"\n");  
//		sql.append("                  start with t1.group_id IN\n");  //zxf
//		sql.append("                             (SELECT TAP.MATERIAL_GROUP_ID\n");  
//		sql.append("                                FROM tm_area_group tap\n");  
//		sql.append("                               where tap.area_id = "+areaId+")\n");  
//		sql.append("                 connect by prior t1.group_id = t1.parent_group_id) b\n");  
		sql.append("                 ) b\n");
		sql.append("         where a.group_id(+) = b.group_id) aa\n");

		sql.append(") Y\n");
		sql.append(" GROUP BY Y.GROUP_ID,\n");  
		sql.append("       Y.GROUP_NAME,\n");  
		sql.append("       Y.GROUP_CODE\n");  
		sql.append("       ORDER BY Y.GROUP_CODE");
		
		return sql.toString();
	}

	/*
	 * 组织查询预测信息，需根据参数计算查询哪些月份的数据
	 * orgLevel:dept部门，sub经销商上级
	 */
	public List<Map<String, Object>> selectOemRequireForecastModelInfo(String porgId,String orgId,List<Map<String, Object>> mapList,String orgLevel,String companyId){
		List<Object> params = new LinkedList<Object>();
		String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
		String sql="";
		if("sub".equals(orgLevel)){
			 if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				 sql=getSubForecastSql(orgId,  mapList,companyId);
				} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
					sql=getSubForecastSql_CVS(orgId,  mapList,companyId);
				} else {
					throw new RuntimeException("判断当前系统的系统参数错误！") ;
				}
		}else{
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				sql=getDeptForecasetSql(porgId,orgId,  mapList,companyId);
				} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
					sql=getDeptForecasetSql_CVS(porgId,orgId,  mapList,companyId);
				} else {
					throw new RuntimeException("判断当前系统的系统参数错误！") ;
				}
			
		}
		return dao.pageQuery(sql, params, getFunName());
	}
	
	/*
	 * 经销可预测操作的数据
	 * 查询 配置ID NAME 月预测数量
	 */
		public List<Map<String, Object>> selectOemForecastOpeList(String porgId,String pgroupId,String orgId,List<Map<String, Object>> mapList,String orgLevel,String companyId){
			List<Object> params = new LinkedList<Object>();
			String sql = "";
			if ("sub".equals(orgLevel)) {
				String para = CommonDAO.getPara(Constant.CHANA_SYS.toString());
				if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
					sql = getSubForecastSaveSearchSql(pgroupId, orgId,
							mapList, companyId);
				} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
					sql = getSubForecastSaveSearchSqlCVS(pgroupId, orgId,
							mapList, companyId);
				} else {
					throw new RuntimeException("判断当前系统的系统参数错误！");
				}
			} else {
				sql = getDeptForecastSaveSearchSql(porgId, pgroupId, orgId,
						mapList, companyId);
			}
			List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params,
					getFunName());
			return list;
		}
		/*
		 * 查询需要清除的组织预测信息
		 * bl预测的第一个月的信息必须未确认状态的。其他月份不分状态清掉，到下一个月时候可再预测，当前月就会变成历史数据
		 */
		public List<Map<String, Object>> selectClrOemForecast(String year,String month ,String orgId,String groupId,String companyId,boolean bl){
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer("");

			sql.append("select f.FORECAST_ID,d.detail_id\n");
			sql.append("  from TT_VS_MONTHLY_FORECAST_DETAIL d, TT_VS_MONTHLY_FORECAST f\n");  
			sql.append(" where d.FORECAST_ID = f.FORECAST_ID\n");  
			sql.append("   and f.FORECAST_YEAR = "+year+"\n");  
			sql.append("   and f.FORECAST_MONTH = "+month+"\n"); 
			String format_month = PlanUtil.getRadomDate(1,"");//zxf
			if(format_month.equals(month)){//正式预测
				sql.append("   and f.FORECAST_TYPE = 60591001\n"); 
			}
			else{
				sql.append("   and f.FORECAST_TYPE = 60591002\n"); 
			}
			if(bl){
				sql.append("   and f.STATUS = "+Constant.FORECAST_STATUS_UNCONFIRM+"\n");  
			}
			sql.append("   and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n"); 
			sql.append("   and f.COMPANY_ID = "+companyId+"\n");
			//sql.append("   and f.AREA_ID = "+areaId+"\n");  //zxf
			sql.append("   and f.ORG_ID = "+orgId+"\n");  
			if(groupId != null && !groupId.equals("")) 
				sql.append("   and d.GROUP_ID ="+groupId);

			return dao.pageQuery(sql.toString(), params, getFunName());
		}	
		
		/*
		 * 区域 查询未提报组织
		 */
		public PageResult<Map<String, Object>> selectSubRequireForecastUnreportedDealers(String orgId,String areaId,String companyId,List<Map<String, Object>> mapList,int curPage, int pageSize){
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			List<Object> params = new LinkedList<Object>();
			String sql= null ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				sql = selectSubRequireForecastUnreportedDealersSQL(orgId, areaId,companyId, mapList);
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				sql = selectSubRequireForecastUnreportedDealersSQLCVS(orgId, areaId,companyId, mapList);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			
			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
			return ps;
		}
		
		/**
		 * 区域 查询经销商车辆类型动态添加至Column		HXY 2011.12.22
		 * */
		public List<Map<String, Object>> findDealerVehicleTypeByGroupLevel(String group_level) {
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer("");
			sql.append("select ('ADD' || replace(replace(GROUP_CODE,'.','-'),'-','_')) as GROUP_CODE, GROUP_NAME, GROUP_LEVEL from TM_VHCL_MATERIAL_GROUP where GROUP_LEVEL = ");
			sql.append(group_level);
			sql.append(" and status=10011001 \n");
			sql.append(" order by GROUP_NAME");
			return dao.pageQuery(sql.toString(), params, getFunName());
		}
		/**
		 * 车厂 查询大区车辆类型动态添加至Column		HXY 2011.12.22
		 * */
		public List<Map<String, Object>> findAreaVehicleTypeByGroupLevel(String group_level) {
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer("");
			sql.append("select ('ADD' || GROUP_CODE) as GROUP_CODE, GROUP_NAME, GROUP_LEVEL from TM_VHCL_MATERIAL_GROUP where GROUP_LEVEL = ");
			sql.append(group_level);
			sql.append(" order by GROUP_NAME");
			return dao.pageQuery(sql.toString(), params, getFunName());
		}
		/*
		 * 区域 查询提报经销商数量 YH 2011.7.8
		 */
		public PageResult<Map<String, Object>> selectSubRequireForecastDealersDetail(String dealer_code,String dealer_name,String orgId,String areaId,String companyId,List<Map<String, Object>> mapList,int curPage, int pageSize){
			List<Object> params = new LinkedList<Object>();
			String sql= null ;
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			 if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				 sql= selectSubRequireForecastreportedDealersSQL(dealer_code,dealer_name,orgId, areaId,companyId, mapList);
				} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
					sql= selectSubRequireForecastreportedDealersSQL_CVS(dealer_code,dealer_name,orgId, areaId,companyId, mapList);
				} else {
					throw new RuntimeException("判断当前系统的系统参数错误！") ;
				}
			
			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
			List<Map<String,Object>>  list =  ps.getRecords();
			List<Map<String,Object>> list_f = new ArrayList<Map<String,Object>>();
			//查询经销商车辆类型
			StringBuffer vehicle_sql = new StringBuffer();
			vehicle_sql.append("select GROUP_CODE, GROUP_NAME, GROUP_LEVEL from TM_VHCL_MATERIAL_GROUP where GROUP_LEVEL = ");
			vehicle_sql.append(RequireForecastManage.DEALER_GROUP_LEVEL);
			List<Map<String, Object>> list_v = dao.pageQuery(vehicle_sql.toString(), params, getFunName());
			Map<String,Object> date_map = mapList.get(0);
			String year = date_map.get("YEAR").toString();
			String month = date_map.get("MONTH").toString();
			if(list != null) {
			for(int i = 0;i<list.size();i++){
			   Map<String, Object> map = list.get(i);
			   String dealer_id = map.get("DEALER_ID").toString();
			   map.put("ORG_NAME", map.get("ORG_NAME").toString());
			   map.put("DEALER_CODE",map.get("DEALER_CODE").toString());
			   map.put("DEALER_SHORTNAME", map.get("DEALER_SHORTNAME").toString());
			   map.put("FORECAST_MONTH", year+"年"+map.get("FORECAST_MONTH").toString()+"月");
			   for(int v=0; v<list_v.size(); v++) {
				   Map<String, Object> vehicleMap = list_v.get(v);
				   String materil_code1=vehicleMap.get("GROUP_CODE").toString().replace("-", "_");
				   materil_code1=materil_code1.replace(".", "_");
				   map.put("ADD" + materil_code1, 0);
			   }

			   List all_amount = this.getForecastAmountByDealer(year,month,dealer_id,areaId);
			   for(int j = 0;j<all_amount.size();j++){
				   Map map_code = (Map)all_amount.get(j);
				   String group_code = map_code.get("GROUP_CODE").toString();
				   
				   for(int v=0; v<list_v.size(); v++) {
					   Map vehicleMap = list_v.get(v);
					   if(vehicleMap.get("GROUP_CODE").toString().equals(group_code)) {
						   String materil_code2=group_code.replace("-", "_");
						   materil_code2=materil_code2.replace(".", "_");
						   map.put("ADD"+materil_code2, map_code.get("FORECAST_AMOUNT"));
					   }
				   }
			   }  
			   list_f.add(map); 
			}}
			ps.setRecords(list_f);
			return ps;
		}
		/*
		 * 车厂 查询提报大区数量 YH 2011.7.8
		 */
		public PageResult<Map<String, Object>> selectSubRequireForecastAreasDetail(String area_code,String area_name,String orgId,String areaId,String companyId,List<Map<String, Object>> mapList,int curPage, int pageSize){
			List<Object> params = new LinkedList<Object>();
			String sql= null ;
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			 if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				 sql= selectSubRequireForecastreportedAreasSQL(area_code,area_name,orgId, areaId,companyId, mapList);
				} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
					sql= selectSubRequireForecastreportedAreasSQL_CVS(area_code,area_name,orgId, areaId,companyId, mapList);
				} else {
					throw new RuntimeException("判断当前系统的系统参数错误！") ;
				}
			
			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
			List<Map<String,Object>>  list =  ps.getRecords();
			//去除车厂的数据
			if(list!=null){
				for(int i=0;i<list.size();i++){
					Map m=list.get(i);
					if("SUZUKI".equals(m.get("ORG_CODE").toString())){
						list.remove(i);
						break;
					}
				}
			}
			
			List<Map<String,Object>> list_f = new ArrayList<Map<String,Object>>();
			//查询经销商车辆类型
			StringBuffer vehicle_sql = new StringBuffer();
			vehicle_sql.append("select GROUP_CODE, GROUP_NAME, GROUP_LEVEL from TM_VHCL_MATERIAL_GROUP where GROUP_LEVEL = ");
			vehicle_sql.append(RequireForecastManage.DEALER_GROUP_LEVEL);
			vehicle_sql.append(" and status=10011001");
			List<Map<String, Object>> list_v = dao.pageQuery(vehicle_sql.toString(), null, getFunName());
			Map<String,Object> date_map = mapList.get(0);
			String year = date_map.get("YEAR").toString();
			String month = date_map.get("MONTH").toString();
			if(list != null) {
				for(int i = 0;i<list.size();i++){
					
				   Map<String, Object> map = null;
					   map=list.get(i);
				   map.put("ORG_NAME", map.get("ORG_NAME").toString());
				   map.put("AREA_CODE",map.get("ORG_CODE").toString());
				   map.put("FORECAST_MONTH", year+"年"+map.get("FORECAST_MONTH").toString()+"月");
				   for(int v=0; v<list_v.size(); v++) {
					   Map<String, Object> vehicleMap = list_v.get(v);
					 String str=  vehicleMap.get("GROUP_CODE").toString().replace(".", "-");
					 str=  str.replace("-", "_");
					   map.put("ADD" + str, 0);
				   }
	
				   List all_amount = this.getForecastAmountByArea(year,month,orgId,areaId);
				   for(int j = 0;j<all_amount.size();j++){
					   Map map_code = (Map)all_amount.get(j);
					   String group_code = map_code.get("GROUP_CODE").toString();
					   TmOrgPO tmorgpo=new TmOrgPO();
					  // String org_id=map_code.get("ORG_ID").toString();
					   tmorgpo.setOrgId(Long.parseLong(map_code.get("ORG_ID").toString()));
					   String orgCode=((TmOrgPO)factory.select(tmorgpo).get(0)).getOrgCode();
					   //按照区域分组加入
					   if(orgCode.equals(map.get("ORG_CODE").toString())){
						   for(int v=0; v<list_v.size(); v++) {
							   Map vehicleMap = list_v.get(v);
							   if(vehicleMap.get("GROUP_CODE").toString().equals(group_code)) {
								   	String tmpstr= vehicleMap.get("GROUP_CODE").toString().replace(".", "-");
								   	tmpstr=tmpstr.replace("-", "_");
									  map.put("ADD"+tmpstr, map_code.get("FORECAST_AMOUNT"));
							   }
						   }
					   }
					  
				   }  
				   list_f.add(map); 
				}
			}
			ps.setRecords(list_f);
			return ps;
		}
		
		//YH 2011.7.11
		public List<Map<String, Object>> getForecastAmountByDealer(String year,String month,String dealer_id,String areaId){
 			StringBuffer sql= new StringBuffer();
			sql.append("select distinct sum(fd.forecast_amount)forecast_amount ,mg3.group_code,mg3.group_name\n" );
			sql.append("  from tt_vs_monthly_forecast_detail fd,\n" );
			sql.append("       tm_vhcl_material_group     mg,\n" );
			sql.append("       tm_vhcl_material_group     mg2,\n" );
			sql.append("       tm_vhcl_material_group     mg3\n" );
			sql.append(" where fd.forecast_id in\n" );
			sql.append("       (select f.forecast_id\n" );
			sql.append("          from TT_VS_MONTHLY_FORECAST f\n" );
			sql.append("         where f.ORG_TYPE = 10191002\n" );
			//sql.append("           and f.AREA_ID = "+areaId+"\n" );
			sql.append("           and f.FORECAST_YEAR = "+year+"\n" );
			sql.append("           and f.FORECAST_MONTH = "+month+"\n" );
			sql.append("           and f.STATUS = 10301002\n" );
			sql.append("           and f.dealer_id = "+dealer_id+")\n" );
			sql.append("   and fd.group_id = mg.group_id\n" );
			sql.append("   and mg.parent_group_id = mg2.group_id\n" );
			sql.append("   and mg2.parent_group_id = mg3.group_id\n" );
			sql.append("   group by mg3.group_code,mg3.group_name");
             List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
			return list;
		}
		//查询大区提报的数量
		public List<Map<String, Object>> getForecastAmountByArea(String year,String month,String org_id,String areaId1){
// 			StringBuffer sql= new StringBuffer();
//			sql.append("select distinct sum(fd.forecast_amount)forecast_amount ,mg3.group_code,mg3.group_name\n" );
//			sql.append("  from tt_vs_monthly_forecast_detail fd,\n" );
//			sql.append("       tm_vhcl_material_group     mg,\n" );
//			sql.append("       tm_vhcl_material_group     mg2,\n" );
//			sql.append("       tm_vhcl_material_group     mg3\n" );
//			sql.append(" where fd.forecast_id =\n" );
//			sql.append("       (select f.forecast_id\n" );
//			sql.append("          from TT_VS_MONTHLY_FORECAST f\n" );
//			sql.append("         where f.ORG_TYPE = 10191001\n" );
//			sql.append("           and f.AREA_ID = "+areaId+"\n" );
//			sql.append("           and f.FORECAST_YEAR = "+year+"\n" );
//			sql.append("           and f.FORECAST_MONTH = "+month+"\n" );
//			sql.append("           and f.STATUS = 10301002\n" );
//			sql.append("           and f.ORG_ID ="+org_id+")\n" );
//			sql.append("   and fd.group_id = mg.group_id\n" );
//			sql.append("   and mg.parent_group_id = mg2.group_id\n" );
//			sql.append("   and mg2.parent_group_id = mg3.group_id\n" );
//			sql.append("   group by mg3.group_code,mg3.group_name");
 			StringBuffer sql= new StringBuffer();
 			sql.append("SELECT DISTINCT SUM(FD.FORECAST_AMOUNT)FORECAST_AMOUNT ,F.ORG_ID,MG3.GROUP_CODE,MG3.GROUP_NAME\n");
 			sql.append(" FROM TT_VS_MONTHLY_FORECAST F,\n");
 			sql.append("  TT_VS_MONTHLY_FORECAST_DETAIL FD,\n");
 			sql.append("   TM_VHCL_MATERIAL_GROUP     MG,\n");
 			sql.append("   TM_VHCL_MATERIAL_GROUP     MG2,\n");
 			sql.append("  TM_VHCL_MATERIAL_GROUP     MG3\n");
 			sql.append(" WHERE F.FORECAST_ID=FD.FORECAST_ID\n");
 			sql.append("AND F.ORG_ID IN (SELECT ORG_ID FROM TM_ORG TMG WHERE TMG.DUTY_TYPE=10431003)\n");
 			//sql.append("    AND F.AREA_ID = "+areaId+"\n");
 			sql.append("  AND F.FORECAST_YEAR = "+year+"\n");
 			sql.append("  AND F.FORECAST_MONTH = "+month+"\n");
 			sql.append("  AND F.STATUS = 10301002\n");
 			sql.append("   AND FD.GROUP_ID = MG.GROUP_ID\n");
 			sql.append(" AND MG.PARENT_GROUP_ID = MG2.GROUP_ID\n");
 			sql.append(" AND MG2.PARENT_GROUP_ID = MG3.GROUP_ID\n");
 			sql.append("  GROUP BY F.ORG_ID,MG3.GROUP_CODE,MG3.GROUP_NAME");


             List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
			return list;
		}
		
		public List<Map<String, Object>> downLoadSubRequireForecastUnreportedDealers(String orgId,String areaId,String companyId,List<Map<String, Object>> mapList){
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			List<Object> params = new LinkedList<Object>();
			String sql= null ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				sql = selectSubRequireForecastUnreportedDealersSQL(orgId, areaId,companyId, mapList);
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				sql = selectSubRequireForecastUnreportedDealersSQLCVS(orgId, areaId,companyId, mapList);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			
            List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
			return list;
		}
		public String  selectSubRequireForecastUnreportedDealersSQL(String orgId,String areaId,String companyId,List<Map<String, Object>> mapList){
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer("");

			for(int i=0;i<mapList.size();i++){
				Map<String, Object> map=mapList.get(i);

				sql.append("select td.DEALER_CODE,\n");
				sql.append("       td.DEALER_SHORTNAME,\n");  
				sql.append("       TMO.ORG_NAME,\n");  
				sql.append("       td.PHONE,\n");  
				sql.append("       td.LINK_MAN,\n");  
				sql.append("       '"+map.get("YEAR")+"年'||'"+map.get("MONTH")+"月' FORECAST_MONTH\n");  
				sql.append("  from TM_DEALER td, TM_DEALER_BUSINESS_AREA r, TM_DEALER_ORG_RELATION TDOR, TM_ORG TMO\n");  
				sql.append(" where td.DEALER_ID = r.DEALER_ID\n");
				sql.append("   and TD.DEALER_ID = TDOR.DEALER_ID\n");  
				sql.append("   and TDOR.ORG_ID = TMO.ORG_ID\n");  
				sql.append("   and td.OEM_COMPANY_ID="+companyId+"\n");
				sql.append("   and td.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+")\n");  
				//sql.append("   and r.AREA_ID = "+areaId+"\n");  
				sql.append("   and td.DEALER_ID in\n");  
				/* 长安原程序代码：
				sql.append("       (select r.dealer_id from TM_DEALER_ORG_RELATION r where r.org_id = "+orgId+")\n");  
				*/
				
				// 铃木修改程序代码：begin   2012-11-21
				sql.append("       (SELECT R.DEALER_ID FROM vw_org_dealer R WHERE R.ROOT_ORG_ID = "+orgId+")\n");  
				// 铃木修改程序代码：end	
				sql.append("   and td.DEALER_ID  not in (select f.DEALER_ID\n");  
				sql.append("                              from TT_VS_MONTHLY_FORECAST f\n");  
				sql.append("                             where f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
				//sql.append("                               and f.AREA_ID = "+areaId+"\n");  
				sql.append("                               and f.FORECAST_YEAR = "+map.get("YEAR")+"\n");  
				sql.append("                               and f.FORECAST_MONTH = "+map.get("MONTH")+"\n");  
				sql.append("                               and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+")\n");
				sql.append(" union \n");
			}
            sql.delete(sql.lastIndexOf("union"),sql.length());
            
            return sql.toString();
		}
		
		public String  selectSubRequireForecastUnreportedDealersSQLCVS(String orgId,String areaId,String companyId,List<Map<String, Object>> mapList){
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer("");

			for(int i=0;i<mapList.size();i++){
				Map<String, Object> map=mapList.get(i);

				sql.append("select td.DEALER_CODE,\n");
				sql.append("       td.DEALER_SHORTNAME,\n");  
				sql.append("       TMO.ORG_NAME,\n");  
				sql.append("       td.PHONE,\n");  
				sql.append("       td.LINK_MAN,\n");  
				sql.append("       '"+map.get("YEAR")+"年'||'"+map.get("MONTH")+"月' FORECAST_MONTH\n");  
				sql.append("  from TM_DEALER td, TM_DEALER_BUSINESS_AREA r, vw_org_dealer TDOR, TM_ORG TMO\n");  
				sql.append(" where td.DEALER_ID = r.DEALER_ID\n");
				sql.append("   and TD.DEALER_ID = TDOR.DEALER_ID\n");  
				sql.append("   and TDOR.root_ORG_ID = TMO.ORG_ID\n");  
				sql.append("   and td.OEM_COMPANY_ID="+companyId+"\n");
				sql.append("   and td.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+")\n");  
				//sql.append("   and r.AREA_ID = "+areaId+"\n");  
				sql.append("   and td.dealer_class = "+Constant.DEALER_CLASS_TYPE_12+"\n");  
				sql.append("   and td.DEALER_ID in\n");  
				sql.append("       (select r.dealer_id from vw_org_dealer r where r.root_org_id = "+orgId+")\n");  
				sql.append("   and td.DEALER_ID not in (select f.DEALER_ID\n");  
				sql.append("                              from TT_VS_MONTHLY_FORECAST f\n");  
				sql.append("                             where f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
			//	sql.append("                               and f.AREA_ID = "+areaId+"\n");  
				sql.append("                               and f.FORECAST_YEAR = "+map.get("YEAR")+"\n");  
				sql.append("                               and f.FORECAST_MONTH = "+map.get("MONTH")+"\n");  
				sql.append("                               and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+")\n");
				sql.append(" union \n");
			}
            sql.delete(sql.lastIndexOf("union"),sql.length());
            
            return sql.toString();
		}
		
		//查询已经提报的经销商SQL YH 2011.7.8
		public String  selectSubRequireForecastreportedDealersSQL(String dealer_code,String dealer_name,String orgId,String areaId,String companyId,List<Map<String, Object>> mapList){
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql = new StringBuffer("");
			for(int i=0;i<mapList.size();i++){
				Map<String, Object> map = mapList.get(i);
				sql.append("select td.dealer_id,td.DEALER_CODE,\n");
				sql.append("       td.DEALER_SHORTNAME,\n");  
				sql.append("       TMO.ORG_NAME,\n");  
				sql.append("       td.PHONE,\n");  
				sql.append("       td.LINK_MAN,\n");  
				sql.append("       '"+map.get("MONTH")+"' FORECAST_MONTH\n");  
				sql.append("  from TM_DEALER td, TM_DEALER_BUSINESS_AREA r, TM_DEALER_ORG_RELATION TDOR, TM_ORG TMO\n");  
				sql.append(" where td.DEALER_ID = r.DEALER_ID\n");
				sql.append("   and TD.DEALER_ID = TDOR.DEALER_ID\n");  
				sql.append("   and TDOR.ORG_ID = TMO.ORG_ID\n");  
				sql.append("   and td.OEM_COMPANY_ID="+companyId+"\n");
				sql.append("   and td.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+")\n");  
				//sql.append("   and r.AREA_ID = "+areaId+"\n");  
				sql.append("   and td.DEALER_ID in\n");  
				/* 长安原程序代码：
				sql.append("       (select r.dealer_id from TM_DEALER_ORG_RELATION r where r.org_id = "+orgId+")\n");  
				*/
				
				// 铃木修改程序代码：begin   2012-11-21
				sql.append("       (SELECT R.DEALER_ID FROM vw_org_dealer R WHERE R.ROOT_ORG_ID = "+orgId+")\n");  
				// 铃木修改程序代码：end					
				sql.append("   and td.DEALER_ID  in (select f.DEALER_ID\n");  
				sql.append("                              from TT_VS_MONTHLY_FORECAST f\n");  
				sql.append("                             where f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
				//sql.append("                               and f.AREA_ID = "+areaId+"\n");  
				sql.append("                               and f.FORECAST_YEAR = "+map.get("YEAR")+"\n");  
				sql.append("                               and f.FORECAST_MONTH = "+map.get("MONTH")+"\n");  
				sql.append("                               and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+")\n");
				if(null!= dealer_code&&!"".equals(dealer_code)){
					sql.append(" and td.dealer_code = '"+dealer_code+"'\n");  
				}
				if(null!= dealer_name&&!"".equals(dealer_name)){
					sql.append(" and td.dealer_name like '%"+dealer_name+"%'\n");  
				}
				if(i!=mapList.size()-1){//zxf add
					sql.append("union \n");
				}
 			}       
            return sql.toString();
		}
		//查询已经提报的大区SQL YH 2011.7.8
		public String  selectSubRequireForecastreportedAreasSQL(String area_code,String area_name,String orgId,String areaId,String companyId,List<Map<String, Object>> mapList){
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql = new StringBuffer("");
			for(int i=0;i<mapList.size();i++){
				Map<String, Object> map = mapList.get(i);

				sql.append("SELECT TM.ORG_CODE,TM.ORG_NAME,"+map.get("MONTH")+" FORECAST_MONTH FROM TM_ORG TM WHERE TM.ORG_ID IN(\n");
				sql.append("    SELECT F.ORG_ID\n");
				sql.append("  FROM TT_VS_MONTHLY_FORECAST F\n");
				sql.append("WHERE F.ORG_TYPE = 10191001\n");
				sql.append("AND F.FORECAST_YEAR = "+map.get("YEAR")+"\n");
				sql.append("AND F.FORECAST_MONTH = "+map.get("MONTH")+"\n");
				sql.append("AND F.STATUS = 10301002)");
				if(null!= area_code&&!"".equals(area_code)){
					sql.append(" and TM.ORG_CODE = '"+area_code+"'\n");  
				}
				if(null!= area_name&&!"".equals(area_name)){
					sql.append(" and TM.ORG_NAME like '%"+area_name+"%'\n");  
				}
				if(i!=mapList.size()-1){
					sql.append(" union \n");
				}
 			}       
            return sql.toString();
		}
		//查询已经提报的经销商SQL 
		public String  selectSubRequireForecastreportedDealersSQL_CVS(String dealer_code,String dealer_name,String orgId,String areaId,String companyId,List<Map<String, Object>> mapList){
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql = new StringBuffer("");
			for(int i=0;i<mapList.size();i++){
				Map<String, Object> map = mapList.get(i);
				sql.append("select td.dealer_id,td.DEALER_CODE,\n");
				sql.append("       td.DEALER_SHORTNAME,\n");  
				sql.append("       vod.root_org_name ORG_NAME,\n");  
				sql.append("       td.PHONE,\n");  
				sql.append("       td.LINK_MAN,\n");  
				sql.append("       '"+map.get("MONTH")+"' FORECAST_MONTH\n");  
				sql.append("  from TM_DEALER td, TM_DEALER_BUSINESS_AREA r, vw_org_dealer vod\n");  
				sql.append(" where td.DEALER_ID = r.DEALER_ID\n");
				sql.append("   and TD.DEALER_ID = vod.DEALER_ID\n");  
				//sql.append("   and TDOR.ORG_ID = TMO.ORG_ID\n");  
				sql.append("   and td.OEM_COMPANY_ID="+companyId+"\n");
				//sql.append("   and td.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+")\n");  
				//sql.append("   and r.AREA_ID = "+areaId+"\n");  
				sql.append("   and td.DEALER_ID in\n");  
				sql.append("       (select r.dealer_id from vw_org_dealer r where r.root_org_id = "+orgId+")\n");  
				sql.append("   and td.DEALER_ID  in (select f.DEALER_ID\n");  
				sql.append("                              from TT_VS_MONTHLY_FORECAST f\n");  
				sql.append("                             where f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
				//sql.append("                               and f.AREA_ID = "+areaId+"\n");  
				sql.append("                               and f.FORECAST_YEAR = "+map.get("YEAR")+"\n");  
				sql.append("                               and f.FORECAST_MONTH = "+map.get("MONTH")+"\n");  
				sql.append("                               and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+")\n");
				if(null!= dealer_code&&!"".equals(dealer_code)){
					sql.append(" and td.dealer_code = '"+dealer_code+"'\n");  
				}
				if(null!= dealer_name&&!"".equals(dealer_name)){
					sql.append(" and td.dealer_name like '%"+dealer_name+"%'\n");  
				}
				if(i!=mapList.size()-1){//zxf add
					sql.append("union \n");
				}
 			}       
            return sql.toString();
		}
		//查询已经提报的经销商SQL 
		public String  selectSubRequireForecastreportedAreasSQL_CVS(String dealer_code,String dealer_name,String orgId,String areaId,String companyId,List<Map<String, Object>> mapList){
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql = new StringBuffer("");
			for(int i=0;i<mapList.size();i++){
				Map<String, Object> map = mapList.get(i);
				sql.append("select td.dealer_id,td.DEALER_CODE,\n");
				sql.append("       td.DEALER_SHORTNAME,\n");  
				sql.append("       vod.root_org_name ORG_NAME,\n");  
				sql.append("       td.PHONE,\n");  
				sql.append("       td.LINK_MAN,\n");  
				sql.append("       '"+map.get("MONTH")+"' FORECAST_MONTH\n");  
				sql.append("  from TM_DEALER td, TM_DEALER_BUSINESS_AREA r, vw_org_dealer vod\n");  
				sql.append(" where td.DEALER_ID = r.DEALER_ID\n");
				sql.append("   and TD.DEALER_ID = vod.DEALER_ID\n");  
				//sql.append("   and TDOR.ORG_ID = TMO.ORG_ID\n");  
				sql.append("   and td.OEM_COMPANY_ID="+companyId+"\n");
				//sql.append("   and td.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+")\n");  
				//sql.append("   and r.AREA_ID = "+areaId+"\n");  
				sql.append("   and td.DEALER_ID in\n");  
				sql.append("       (select r.dealer_id from vw_org_dealer r where r.root_org_id = "+orgId+")\n");  
				sql.append("   and td.DEALER_ID  in (select f.DEALER_ID\n");  
				sql.append("                              from TT_VS_MONTHLY_FORECAST f\n");  
				sql.append("                             where f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
				//sql.append("                               and f.AREA_ID = "+areaId+"\n");  
				sql.append("                               and f.FORECAST_YEAR = "+map.get("YEAR")+"\n");  
				sql.append("                               and f.FORECAST_MONTH = "+map.get("MONTH")+"\n");  
				sql.append("                               and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+")\n");
				if(null!= dealer_code&&!"".equals(dealer_code)){
					sql.append(" and td.dealer_code = '"+dealer_code+"'\n");  
				}
				if(null!= dealer_name&&!"".equals(dealer_name)){
					sql.append(" and td.dealer_name like '%"+dealer_name+"%'\n");  
				}
				if(i!=mapList.size()-1){
					sql.append("union \n");
				}
 			}       
            return sql.toString();
		}
		/*
		 * 总部查询未提报组织名单，目前只有大区的情况
		 * orgId是车厂公司的ORGID
		 */
		public PageResult<Map<String, Object>> selectDeptRequireForecastUnreportedDealers(String orgId,String areaId,List<Map<String, Object>> mapList,int curPage, int pageSize){
			List<Object> params = new LinkedList<Object>();
			
			String sql=selectDeptRequireForecastUnreportedDealersSql(orgId, areaId, mapList);
			
			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
			return ps;
		}
		
		
		/*
		 * 总部查询未提大区名单
		 * orgId是车厂公司的ORGID
		 */
		public PageResult<Map<String, Object>> selectDeptRequireForecastUnreportedOrg(String orgId,String companyId,List<Map<String, Object>> mapList,int curPage, int pageSize){
			//List<Object> params = new LinkedList<Object>();
			
			String sql=selectDeptRequireForecastUnreportedOrgSql(orgId, companyId, mapList);
			
			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
			
			
			List<Map<String,Object>> list_f = new ArrayList<Map<String,Object>>();
			//查询经销商车辆类型
			StringBuffer vehicle_sql = new StringBuffer();
			vehicle_sql.append("select GROUP_CODE, GROUP_NAME, GROUP_LEVEL from TM_VHCL_MATERIAL_GROUP where GROUP_LEVEL = ");
			vehicle_sql.append(RequireForecastManage.DEALER_GROUP_LEVEL);
			vehicle_sql.append(" and status=10011001");
			List<Map<String, Object>> list_v = dao.pageQuery(vehicle_sql.toString(), null, getFunName());
			Map<String,Object> date_map = mapList.get(0);
			String year = date_map.get("YEAR").toString();
			String month = date_map.get("MONTH").toString();
			List<Map<String,Object>>  list =  ps.getRecords();
			if(list != null) {
				for(int i = 0;i<list.size();i++){
					
				   Map<String, Object> map = null;
					   map=list.get(i);
				   map.put("ORG_NAME", map.get("ORG_NAME").toString());
				   map.put("AREA_CODE",map.get("ORG_CODE").toString());
				   map.put("FORECAST_MONTH", year+"年"+map.get("FORECAST_MONTH").toString());
				   for(int v=0; v<list_v.size(); v++) {
					   Map<String, Object> vehicleMap = list_v.get(v);
					 String str=  vehicleMap.get("GROUP_CODE").toString().replace(".", "-");
					 str=  str.replace("-", "_");
					   map.put("ADD" + str, 0);
				   }
	
				   List all_amount = this.getForecastAmountByArea(year,month,orgId,"");
				   for(int j = 0;j<all_amount.size();j++){
					   Map map_code = (Map)all_amount.get(j);
					   String group_code = map_code.get("GROUP_CODE").toString();
					   TmOrgPO tmorgpo=new TmOrgPO();
					  // String org_id=map_code.get("ORG_ID").toString();
					   tmorgpo.setOrgId(Long.parseLong(map_code.get("ORG_ID").toString()));
					   String orgCode=((TmOrgPO)factory.select(tmorgpo).get(0)).getOrgCode();
					   //按照区域分组加入
					   if(orgCode.equals(map.get("ORG_CODE").toString())){
						   for(int v=0; v<list_v.size(); v++) {
							   Map vehicleMap = list_v.get(v);
							   if(vehicleMap.get("GROUP_CODE").toString().equals(group_code)) {
								   	String tmpstr= vehicleMap.get("GROUP_CODE").toString().replace(".", "-");
								   	tmpstr=tmpstr.replace("-", "_");
									  map.put("ADD"+tmpstr, map_code.get("FORECAST_AMOUNT"));
							   }
						   }
					   }
					  
				   }  
				   list_f.add(map); 
				}
			}
			ps.setRecords(list_f);
			
			
			return ps;
		}
		
		/*
		 * 总部查询未提大区名单全部数据
		 * orgId是车厂公司的ORGID
		 */
		public List<Map<String, Object>> selectDeptRequireForecastUnreportedOrgData(String orgId,String companyId,List<Map<String, Object>> mapList){				
			String sql=selectDeptRequireForecastUnreportedOrgSql(orgId, companyId, mapList);			
			List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());	
			
			List<Map<String,Object>> list_f = new ArrayList<Map<String,Object>>();
			//查询经销商车辆类型
			StringBuffer vehicle_sql = new StringBuffer();
			vehicle_sql.append("select GROUP_CODE, GROUP_NAME, GROUP_LEVEL from TM_VHCL_MATERIAL_GROUP where GROUP_LEVEL = ");
			vehicle_sql.append(RequireForecastManage.DEALER_GROUP_LEVEL);
			vehicle_sql.append(" and status=10011001");
			List<Map<String, Object>> list_v = dao.pageQuery(vehicle_sql.toString(), null, getFunName());
			Map<String,Object> date_map = mapList.get(0);
			String year = date_map.get("YEAR").toString();
			String month = date_map.get("MONTH").toString();
			
			if(list != null) {
				for(int i = 0;i<list.size();i++){
					
				   Map<String, Object> map = null;
					   map=list.get(i);
				   map.put("ORG_NAME", map.get("ORG_NAME").toString());
				   map.put("AREA_CODE",map.get("ORG_CODE").toString());
				   map.put("FORECAST_MONTH", year+"年"+map.get("FORECAST_MONTH").toString()+"月");
				   for(int v=0; v<list_v.size(); v++) {
					   Map<String, Object> vehicleMap = list_v.get(v);
					 String str=  vehicleMap.get("GROUP_CODE").toString().replace(".", "-");
					 str=  str.replace("-", "_");
					   map.put("ADD" + str, 0);
				   }
	
				   List all_amount = this.getForecastAmountByArea(year,month,orgId,"");
				   for(int j = 0;j<all_amount.size();j++){
					   Map map_code = (Map)all_amount.get(j);
					   String group_code = map_code.get("GROUP_CODE").toString();
					   TmOrgPO tmorgpo=new TmOrgPO();
					  // String org_id=map_code.get("ORG_ID").toString();
					   tmorgpo.setOrgId(Long.parseLong(map_code.get("ORG_ID").toString()));
					   String orgCode=((TmOrgPO)factory.select(tmorgpo).get(0)).getOrgCode();
					   //按照区域分组加入
					   if(orgCode.equals(map.get("ORG_CODE").toString())){
						   for(int v=0; v<list_v.size(); v++) {
							   Map vehicleMap = list_v.get(v);
							   if(vehicleMap.get("GROUP_CODE").toString().equals(group_code)) {
								   	String tmpstr= vehicleMap.get("GROUP_CODE").toString().replace(".", "-");
								   	tmpstr=tmpstr.replace("-", "_");
									  map.put("ADD"+tmpstr, map_code.get("FORECAST_AMOUNT"));
							   }
						   }
					   }
					  
				   }  
				   list_f.add(map); 
				}
			}
			
			return list_f;
		}
		
		/*
		 * 总部下载未提报组织名单，目前只有大区的情况
		 * orgId是车厂公司的ORGID
		 */
		public List<Map<String, Object>> downLoadDeptRequireForecastUnreportedDealers(String orgId,String areaId,List<Map<String, Object>> mapList){
			String sql=selectDeptRequireForecastUnreportedDealersSql(orgId, areaId, mapList);
			return dao.pageQuery(sql, null, getFunName());
		}
		/*
		 * 总部下载未提报组织名单，目前只有大区的情况SQL
		 * orgId是车厂公司的ORGID
		 */
		public String selectDeptRequireForecastUnreportedDealersSql(String orgId,String areaId,List<Map<String, Object>> mapList){
			StringBuffer sql=new StringBuffer("");
			sql.append("select * from(\n");
			for(int i=0;i<mapList.size();i++){
				Map<String, Object> map=mapList.get(i);

				sql.append("select distinct td.DEALER_ID,\n");
				sql.append("       org.org_name,\n");  
				sql.append("       td.DEALER_CODE,\n");  
				sql.append("       td.DEALER_SHORTNAME,\n");  
				sql.append("       td.PHONE,\n");  
				sql.append("       td.LINK_MAN,\n");  
				sql.append("       '"+map.get("YEAR")+"年'||'"+map.get("MONTH")+"月' FORECAST_MONTH\n");  
				sql.append("  from vw_org_dealer r,\n");  
				sql.append("       TM_ORG                 org,\n");  
				sql.append("       TM_DEALER_BUSINESS_AREA   a,\n");  
				sql.append("       TM_DEALER              td\n");  
				sql.append(" where r.root_org_id = org.ORG_ID\n");  
				sql.append("   and td.DEALER_ID = r.DEALER_ID\n");  
				sql.append("   and td.DEALER_TYPE = "+Constant.DEALER_TYPE_DVS+"\n");  
				sql.append("   and TD.DEALER_ID = a.DEALER_ID\n");  
				sql.append("   and org.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
				sql.append("   and org.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n");  
				sql.append("   and org.PARENT_ORG_ID = "+orgId+"\n");  

				/*如果分大区小区时用这个
				 * sql.append("and org.ORG_ID in\n");
				sql.append("      (select org1.org_id\n");  
				sql.append("         from tm_org org1\n");  
				sql.append("        where org1.TREE_CODE like\n");  
				sql.append("              (select og.tree_code\n");  
				sql.append("                 from tm_org og\n");  
				sql.append("                where og.ORG_ID = 2010010100070674) || '%'\n");  
				sql.append("        and org1.DUTY_TYPE not in (10431001, 10431002, 10431005)\n");  
				sql.append("      )");*/

				
				sql.append("   and org.STATUS = "+Constant.STATUS_ENABLE+"\n");  
				sql.append("   and td.STATUS = "+Constant.STATUS_ENABLE+"\n");  
				//sql.append("   and a.AREA_ID = "+areaId+"\n");  
				sql.append("   and td.DEALER_ID not in (select f.DEALER_ID\n");  
				sql.append("                              from TT_VS_MONTHLY_FORECAST f\n");  
				sql.append("                             where f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
				//sql.append("                               and f.AREA_ID = "+areaId+"\n");  
				sql.append("                               and f.FORECAST_YEAR = "+map.get("YEAR")+"\n");  
				sql.append("                               and f.FORECAST_MONTH = "+map.get("MONTH")+"\n");  
				sql.append("                               and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+")");
				//sql.append("   order by org.org_name\n"); 
				sql.append(" union \n");
			}			
            sql.delete(sql.lastIndexOf("union"),sql.length());
            sql.append(" )  org order by org.org_name \n");
            return sql.toString();
		}
		
		/*
		 * 未提大区明单
		 */
		public String selectDeptRequireForecastUnreportedOrgSql(String orgId,String company_id,List<Map<String, Object>> mapList){
			StringBuffer sql=new StringBuffer("");
			
			for(int i=0;i<mapList.size();i++){
				Map<String, Object> map=mapList.get(i);
					sql.append("select  tm.org_id,tm.org_code,tm.org_name, \n");					
					sql.append("  '"+map.get("YEAR")+"年'||'"+map.get("MONTH")+"月' FORECAST_MONTH\n"); 
					sql.append(" from( \n");
					//sql.append("select t.org_id,t.org_code,t.org_name from TM_ORG t where t.duty_type = 10431001 and t.company_id ="+company_id);
					//sql.append("\n union \n");
					sql.append("select  t.org_id,t.org_code,t.org_name  from( \n");
					sql.append("select * from TM_ORG j where j.duty_type= 10431003 \n");
					sql.append("order by j.org_name \n");
					sql.append(") t ) tm where tm.org_id not in( \n");
					sql.append(" SELECT F.ORG_ID \n");
					sql.append("FROM TT_VS_MONTHLY_FORECAST F \n");
					sql.append("WHERE F.ORG_TYPE = 10191001 \n");
					sql.append("AND F.FORECAST_YEAR = "+map.get("YEAR")+" \n");
					sql.append("AND F.FORECAST_MONTH = "+map.get("MONTH")+" \n");
					sql.append("AND F.STATUS = 10301002 ) \n");
					if(i!=mapList.size()-1){
						sql.append("union \n");
					}
			}
			return sql.toString();
		}
		/*
		 * 总部查询未提报组织
		 */
		public PageResult<Map<String, Object>> selectDeptForecastLessThanMonthPlanDealers(String orgId,String areaId,List<Map<String, Object>> mapList,int curPage, int pageSize){
			List<Object> params = new LinkedList<Object>();
			String sql=selectDeptForecastLessThanMonthPlanDealersSql(orgId, areaId, mapList);
			PageResult<Map<String, Object>> ps = pageQuery(sql, null, getFunName(), pageSize, curPage);
			return ps;
		}
		/*
		 * 总部查询未提报组织下载
		 */
		public List<Map<String, Object>> downLoadDeptForecastLessThanMonthPlanDealers(String orgId,String areaId,List<Map<String, Object>> mapList){
			//List<Object> params = new LinkedList<Object>();
			String sql=selectDeptForecastLessThanMonthPlanDealersSql(orgId, areaId, mapList);
			List<Map<String, Object>> list = pageQuery(sql, null, getFunName());
			return list;
		}
		/*
		 * 总部查询未提报组织sql update yinshunhui
		 */
		private String selectDeptForecastLessThanMonthPlanDealersSql(String orgId,String areaId,List<Map<String, Object>> mapList){
			StringBuffer sql=new StringBuffer("");
			
			for(int i=0;i<mapList.size();i++){
				Map<String, Object> map=mapList.get(i);

				sql.append("select a.DEALER_CODE,\n");
				sql.append("       a.DEALER_SHORTNAME,\n");  
				sql.append("       a.LINK_MAN,\n");  
				sql.append("       '"+map.get("YEAR")+"年'||'"+map.get("MONTH")+"月' FORECAST_MONTH,\n");  
				sql.append("       a.PHONE,\n");  
				sql.append("       tvmg.GROUP_CODE,\n");
				sql.append("       tvmg.GROUP_NAME,\n");
				sql.append("       a.SALE_AMOUNT,\n");
				sql.append("        nvl(b.AMOUNT,0) amount\n");  
				sql.append("  from (select tp.DEALER_ID,\n");  
				sql.append("               tpd.MATERIAL_GROUPID,\n");  
				sql.append("               tpd.SALE_AMOUNT,\n");  
				sql.append("               td.DEALER_CODE,\n");  
				sql.append("               td.DEALER_SHORTNAME,\n");  
				sql.append("               td.LINK_MAN,\n");  
				sql.append("               td.PHONE\n");  
				sql.append("          from TT_VS_MONTHLY_PLAN        tp,\n");  
				sql.append("               TT_VS_MONTHLY_PLAN_DETAIL tpd,\n");  
				sql.append("               TM_DEALER                 td\n");  
				sql.append("         where tp.PLAN_ID = tpd.PLAN_ID\n");  
				sql.append("           and td.DEALER_ID = tp.DEALER_ID\n");  
				sql.append("           and tp.PLAN_YEAR = "+map.get("YEAR")+"\n");  
				sql.append("           and tp.PLAN_MONTH = "+map.get("MONTH")+"\n");  
				sql.append("           and tp.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
				sql.append("           and tp.AREA_ID = "+areaId+"\n");  
				sql.append("           and tp.STATUS = "+Constant.PLAN_MANAGE_CONFIRM+"\n");  
				sql.append("           and tp.PLAN_VER = (select nvl(max(plan_ver), 0) plan_ver\n");  
				sql.append("                                from TT_VS_MONTHLY_PLAN\n");  
				sql.append("                               where plan_year = "+map.get("YEAR")+"\n");  
				sql.append("                                 and plan_month = "+map.get("MONTH")+"\n");  
				sql.append("                                 and AREA_ID = "+areaId+"\n");  
				sql.append("                                 and STATUS = "+Constant.PLAN_MANAGE_CONFIRM+"\n");  
				sql.append("                                 and ORG_TYPE = "+Constant.ORG_TYPE_DEALER+")) a,\n");  
				sql.append("       (select tf.DEALER_ID,\n");  
				sql.append("               tf.FORECAST_YEAR,\n");  
				sql.append("               tf.FORECAST_MONTH,\n");  
				sql.append("               tvmg3.GROUP_ID,\n");
//				sql.append("               tvmg3.GROUP_CODE,\n");
//				sql.append("               tvmg3.GROUP_NAME,\n");
				sql.append("               sum(tfd.FORECAST_amount) AMOUNT\n");
				sql.append("          from TT_VS_MONTHLY_FORECAST        tf,\n");  
				sql.append("               TT_VS_MONTHLY_FORECAST_DETAIL tfd,\n");  
				sql.append("               TM_VHCL_MATERIAL_GROUP        tvmg1,\n");  
				sql.append("               TM_VHCL_MATERIAL_GROUP        tvmg2,\n");
				sql.append("               TM_VHCL_MATERIAL_GROUP        tvmg3\n");
				sql.append("         where tf.FORECAST_ID = tfd.FORECAST_ID\n");
				sql.append("           and tfd.GROUP_ID = tvmg1.GROUP_ID\n");  
				sql.append("           and tf.FORECAST_year = "+map.get("YEAR")+"\n");  
				sql.append("           and tf.forecast_month = "+map.get("MONTH")+"\n");  
				sql.append("           and tf.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
				sql.append("           and tvmg2.group_id = tvmg1.parent_group_id\n");
				sql.append("           and tvmg3.group_id = tvmg2.parent_group_id\n");
				sql.append("           and tf.AREA_ID = "+areaId+"\n");  
				sql.append("         group by tf.DEALER_ID,\n");  
				sql.append("                  tf.FORECAST_YEAR,\n");  
				sql.append("                  tf.FORECAST_MONTH,\n");  
//				sql.append("                  tvmg3.GROUP_CODE,\n");
//				sql.append("                  tvmg3.GROUP_NAME,\n");
				sql.append("                  tvmg3.GROUP_ID) b,\n");
				sql.append("  TM_VHCL_MATERIAL_GROUP tvmg  ");
				sql.append(" where a.DEALER_ID = b.dealer_id(+)\n");
				sql.append("   and a.MATERIAL_GROUPID = b.group_id(+)\n");  
				sql.append("   and a.sale_amount > nvl(b.AMOUNT,0)");
				sql.append(" and a.MATERIAL_GROUPID=tvmg.group_id");
				//sql.append(" order by a.dealer_code");
				sql.append(" union \n");
			}
			sql.delete(sql.lastIndexOf("union"),sql.length());
			sql.append(") org order by org.org_name \n");
			return sql.toString();
		}
		/*
		 * 区域预测数量小月度目标下载
		 */
		public List<Map<String, Object>> downLoadSubForecastLessThanMonthPlanDealers(String orgId,String areaId,List<Map<String, Object>> mapList){
			String sql=selectSubForecastLessThanMonthPlanDealersSql(orgId, areaId, mapList);
			List<Map<String, Object>> list=dao.pageQuery(sql, null, getFunName());
			return list;
		}
		/*
		 * 区域预测数量小月度目标
		 */
		public PageResult<Map<String, Object>> selectSubForecastLessThanMonthPlanDealers(String orgId,String areaId,List<Map<String, Object>> mapList,int curPage, int pageSize){
			String sql=selectSubForecastLessThanMonthPlanDealersSql(orgId, areaId, mapList);
			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
			return ps;
		}
		/*
		 * 区域预测数量小月度目标SQL updte yinshunhui
		 */
		public String selectSubForecastLessThanMonthPlanDealersSql(String orgId,String areaId,List<Map<String, Object>> mapList){
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer("");
			
			for(int i=0;i<mapList.size();i++){
				Map<String, Object> map=mapList.get(i);

				sql.append("select a.DEALER_CODE,\n");
				sql.append("       a.DEALER_SHORTNAME,\n");  
				sql.append("       a.LINK_MAN,\n");  
				sql.append("       '"+map.get("YEAR")+"年'||'"+map.get("MONTH")+"月' FORECAST_MONTH,\n");  
				sql.append("       a.PHONE,\n");  
				sql.append("       tvmg.group_code,\n");
				sql.append("       tvmg.group_name,\n");
				sql.append("       a.SALE_AMOUNT,\n");
				sql.append(" nvl(b.AMOUNT,0) amount");
				sql.append("  from (select tp.DEALER_ID,\n");  
				sql.append("               tpd.MATERIAL_GROUPID,\n");  
				sql.append("               tpd.SALE_AMOUNT,\n");  
				sql.append("               td.DEALER_CODE,\n");  
				sql.append("               td.DEALER_SHORTNAME,\n");  
				sql.append("               td.LINK_MAN,\n");  
				sql.append("               td.PHONE\n");  
				sql.append("          from TT_VS_MONTHLY_PLAN        tp,\n");  
				sql.append("               TT_VS_MONTHLY_PLAN_DETAIL tpd,\n");  
				sql.append("               TM_DEALER                 td\n");  
				sql.append("         where tp.PLAN_ID = tpd.PLAN_ID\n");  
				sql.append("           and td.DEALER_ID = tp.DEALER_ID\n");
				/* 长安原程序代码：
				sql.append("           and td.dealer_id in (select dealer_id\n");
				sql.append("                                  from TM_DEALER_ORG_RELATION r\n");  
				sql.append("                                 where r.ORG_ID ="+orgId+")\n");
				*/
				// 铃木修改程序代码：begin   2012-11-21
				sql.append("           AND TD.DEALER_ID IN (SELECT DEALER_ID\n");
				sql.append("                                  FROM vw_org_dealer R\n");  
				sql.append("                                 WHERE R.ROOT_ORG_ID ="+orgId+")\n");				
				// 铃木修改程序代码：end	

				sql.append("           and tp.PLAN_YEAR = "+map.get("YEAR")+"\n");  
				sql.append("           and tp.PLAN_MONTH = "+map.get("MONTH")+"\n");  
				sql.append("           and tp.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
				sql.append("           and tp.AREA_ID = "+areaId+"\n");  
				sql.append("           and tp.STATUS = "+Constant.PLAN_MANAGE_CONFIRM+"\n");  
				sql.append("           and tp.PLAN_VER = (select nvl(max(plan_ver), 0) plan_ver\n");  
				sql.append("                                from TT_VS_MONTHLY_PLAN\n");  
				sql.append("                               where plan_year = "+map.get("YEAR")+"\n");  
				sql.append("                                 and plan_month = "+map.get("MONTH")+"\n");  
				sql.append("                                 and AREA_ID = "+areaId+"\n");  
				sql.append("                                 and STATUS = "+Constant.PLAN_MANAGE_CONFIRM+"\n");  
				sql.append("                                 and ORG_TYPE = "+Constant.ORG_TYPE_DEALER+")) a,\n");  
				sql.append("       (select tf.DEALER_ID,\n");  
				sql.append("               tf.FORECAST_YEAR,\n");  
				sql.append("               tf.FORECAST_MONTH,\n");  
				sql.append("               tvmg3.GROUP_ID,\n");
//				sql.append("               tvmg3.GROUP_CODE,\n");
//				sql.append("               tvmg3.GROUP_NAME,\n");
				sql.append("               sum(tfd.FORECAST_amount) AMOUNT\n");
				sql.append("          from TT_VS_MONTHLY_FORECAST        tf,\n");  
				sql.append("               TT_VS_MONTHLY_FORECAST_DETAIL tfd,\n");  
				sql.append("               TM_VHCL_MATERIAL_GROUP        tvmg1,\n");  
				sql.append("               TM_VHCL_MATERIAL_GROUP        tvmg2,\n");
				sql.append("               TM_VHCL_MATERIAL_GROUP        tvmg3\n");
				sql.append("         where tf.FORECAST_ID = tfd.FORECAST_ID\n");
				sql.append("           and tfd.GROUP_ID = tvmg1.GROUP_ID\n");  
				sql.append("           and tf.FORECAST_year = "+map.get("YEAR")+"\n");  
				sql.append("           and tf.forecast_month = "+map.get("MONTH")+"\n");  
				sql.append("           and tf.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
				sql.append("           and tvmg2.group_id = tvmg1.PARENT_GROUP_ID\n");
				sql.append("           and tvmg3.group_id = tvmg2.PARENT_GROUP_ID\n");
				sql.append("           and tf.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");
				sql.append("           and tf.AREA_ID = "+areaId+"\n");  
				sql.append("         group by tf.DEALER_ID,\n");  
				sql.append("                  tf.FORECAST_YEAR,\n");  
				sql.append("                  tf.FORECAST_MONTH,\n");  
//				sql.append("                  tvmg3.GROUP_CODE,\n");
//				sql.append("                  tvmg3.GROUP_NAME,\n");
				sql.append("                  tvmg3.GROUP_ID) b ,\n");
				sql.append("TM_VHCL_MATERIAL_GROUP tvmg");
				sql.append(" where a.DEALER_ID = b.dealer_id(+)\n");
				sql.append("   and a.MATERIAL_GROUPID = b.group_id(+)\n");  
				sql.append("   and a.MATERIAL_GROUPID=tvmg.group_id");
				sql.append("   and a.sale_amount > nvl(b.AMOUNT,0)");
				sql.append("order by a.dealer_code");
				sql.append(" union \n");
			}

			sql.delete(sql.lastIndexOf("union"),sql.length());
			
			return sql.toString();
		}
		/*
		 * 组织明细
		 */
		public PageResult<Map<String, Object>> oemSelectOrgRequireForecast(Map<String, Object> map,int pageSize,int curPage){
			String plan_year=(String)map.get("plan_year");
			String plan_month=(String)map.get("plan_month");
			String orgCode=(String)map.get("orgCode");
			String logonOrgType=(String)map.get("logonOrgType");
			String logonOrgId=(String)map.get("logonOrgId");
			String groupCode=map.get("groupCode").toString();
			//String areaId=map.get("areaId").toString();
			String companyId=map.get("companyId").toString();
			String forecast_type=(String)map.get("forecast_type");//zxf
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer();
			
			/*sql.append("select a.org_code,a.org_name,a.forecast_month,a.group_code,a.group_name,a.group_id,b.FORECAST_AMOUNT as DEALER_FORECAST_AMOUNT");
			
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
				sql.append(",a.forecast_amount \n");
			}
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"COMPANY".equals(logonOrgType)){
				sql.append(",decode(a.org_code,(select org_code  from tm_org where org_id=?),a.forecast_amount,'') as forecast_amount, C.FORECAST_AMOUNT AS LARGE_FORECAST_AMOUNT\n");
				params.add(companyId);
			}
			sql.append("FROM ");
			sql.append("  ( \n");
			
			sql.append("select org.ORG_CODE,\n");
			sql.append("       org.ORG_NAME,\n");  
			sql.append("       f.FORECAST_YEAR || '-' || f.FORECAST_MONTH FORECAST_MONTH,\n");  
			sql.append("       g.GROUP_CODE,\n");  
			sql.append("       g.GROUP_NAME,\n");  
			sql.append("       d.GROUP_ID,\n");  
			sql.append("       sum(d.FORECAST_AMOUNT) FORECAST_AMOUNT\n");  
			sql.append("  from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("       TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("       TM_ORG                        org,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP        g\n");  
			sql.append(" where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("   and f.ORG_ID = org.ORG_ID\n");  
			sql.append("   and f.forecast_type = "+forecast_type+"\n");//zxf
			sql.append("   and g.GROUP_ID = d.GROUP_ID\n");
			sql.append("      --     and d.FORECAST_AMOUNT > 0\n");
			sql.append("      -- and org.DUTY_TYPE = 10431003\n");  
			sql.append("   and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
				sql.append("      and f.ORG_ID=?\n");
				params.add(logonOrgId);
			}
			//sql.append("   and f.AREA_ID = ?\n");  
			//params.add(areaId);
			sql.append("   and f.COMPANY_ID = ?\n");  
			params.add(companyId);
			sql.append("   and f.FORECAST_YEAR = ?\n");  
			params.add(plan_year);
			sql.append("   and f.FORECAST_MONTH = ?\n");  
			params.add(plan_month);
			sql.append("   and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
			if(null!=orgCode&&!"".equals(orgCode)){
				sql.append("           and org.org_code in ("+PlanUtil.createSqlStr(orgCode)+")\n");  
			}
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append(" group by d.GROUP_ID,\n");  
			sql.append("          org.ORG_CODE,\n");  
			sql.append("          org.ORG_NAME,\n");  
			sql.append("          f.FORECAST_YEAR,\n");  
			sql.append("          f.FORECAST_MONTH,\n");  
			sql.append("          g.GROUP_CODE,\n");  
			sql.append("          g.GROUP_NAME\n");  
			//sql.append(" order by org.ORG_CODE, d.GROUP_ID");
			sql.append(") A \n");
			
			//显示大区数量
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"COMPANY".equals(logonOrgType)){
				sql.append(" left join (");
				sql.append("select org.ORG_CODE,\n");
				sql.append("       org.ORG_NAME,\n");  
				sql.append("       f.FORECAST_YEAR || '-' || f.FORECAST_MONTH FORECAST_MONTH,\n");  
				sql.append("       g.GROUP_CODE,\n");  
				sql.append("       g.GROUP_NAME,\n");  
				sql.append("       d.GROUP_ID,\n");  
				sql.append("       sum(d.FORECAST_AMOUNT) FORECAST_AMOUNT\n");  
				sql.append("  from TT_VS_MONTHLY_FORECAST        f,\n");  
				sql.append("       TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
				sql.append("       TM_ORG                        org,\n");  
				sql.append("       TM_VHCL_MATERIAL_GROUP        g\n");  
				sql.append(" where f.FORECAST_ID = d.FORECAST_ID\n");  
				sql.append("   and f.ORG_ID = org.ORG_ID\n");  
				sql.append("   and f.forecast_type = "+forecast_type+"\n");//zxf
				sql.append("   and g.GROUP_ID = d.GROUP_ID\n");
				sql.append("      --     and d.FORECAST_AMOUNT > 0\n");
				sql.append("      -- and org.DUTY_TYPE = 10431003\n");  
				sql.append("   and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
				if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"COMPANY".equals(logonOrgType)){
					sql.append("      and f.ORG_ID IN (SELECT ORG_ID FROM TM_ORG WHERE PARENT_ORG_ID=? AND duty_type= 10431003)\n");
					params.add(logonOrgId);
				}
				//sql.append("   and f.AREA_ID = ?\n");  
				//params.add(areaId);
				sql.append("   and f.COMPANY_ID = ?\n");  
				params.add(companyId);
				sql.append("   and f.FORECAST_YEAR = ?\n");  
				params.add(plan_year);
				sql.append("   and f.FORECAST_MONTH = ?\n");  
				params.add(plan_month);
				sql.append("   and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
				if(null!=orgCode&&!"".equals(orgCode)){
					sql.append("           and org.org_code in ("+PlanUtil.createSqlStr(orgCode)+")\n");  
				}
				if(null!=groupCode&&!"".equals(groupCode)){
					sql.append("           and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
				}
				sql.append(" group by d.GROUP_ID,\n");  
				sql.append("          org.ORG_CODE,\n");  
				sql.append("          org.ORG_NAME,\n");  
				sql.append("          f.FORECAST_YEAR,\n");  
				sql.append("          f.FORECAST_MONTH,\n");  
				sql.append("          g.GROUP_CODE,\n");  
				sql.append("          g.GROUP_NAME\n");  
				sql.append(" ) C ON A.GROUP_ID=C.GROUP_ID AND A.ORG_CODE=C.ORG_CODE");
			}
			//显示大区数量 end
			
			sql.append(" left join \n");
			// 显示经销商数量
			sql.append("( \n");
			sql.append("select \n");
			//sql.append("       TD.DEALER_NAME,\n"); 
			sql.append("          td.ROOT_ORG_CODE,\n");  
			sql.append("          TD.ROOT_ORG_NAME,\n"); 
			sql.append("       f.FORECAST_YEAR || '-' || f.FORECAST_MONTH FORECAST_MONTH,\n");  
			sql.append("       g.GROUP_CODE,\n");  
			sql.append("       g.GROUP_NAME,\n");  
			sql.append("       d.GROUP_ID,\n");  
			sql.append("       sum(d.FORECAST_AMOUNT) FORECAST_AMOUNT\n");  
			sql.append("  from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("       TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("       VW_ORG_DEALER_ALL_NEW            TD,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP        g\n");  
			sql.append(" where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("   and f.DEALER_ID = TD.DEALER_ID\n");  
			sql.append("   and f.forecast_type = "+forecast_type+"\n");//zxf
			sql.append("   and g.GROUP_ID = d.GROUP_ID\n");
			sql.append("           and d.FORECAST_AMOUNT > 0\n");
			sql.append("      -- and org.DUTY_TYPE = 10431003\n");  
			sql.append("   and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
				sql.append("      and f.ORG_ID IS NULL\n");
				//params.add(logonOrgId);
			}
			//sql.append("   and f.AREA_ID = ?\n");  
			//params.add(areaId);
			sql.append("   and f.COMPANY_ID = ?\n");  
			params.add(companyId);
			sql.append("   and f.FORECAST_YEAR = ?\n");  
			params.add(plan_year);
			sql.append("   and f.FORECAST_MONTH = ?\n");  
			params.add(plan_month);
			sql.append("   and f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
				sql.append("   and f.dealer_id in ( select dealer_id from vw_org_dealer_all_new where root_org_id=?) \n");
				params.add(logonOrgId);
			}
			if(null!=orgCode&&!"".equals(orgCode)){
				sql.append("      and f.dealer_id in ( select dealer_id from vw_org_dealer_all_new where root_org_code="+PlanUtil.createSqlStr(orgCode)+")\n");  
			}
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append(" group by d.GROUP_ID,\n");  
			sql.append("          td.ROOT_ORG_CODE,\n");  
			sql.append("          TD.ROOT_ORG_NAME,\n");  
			sql.append("          f.FORECAST_YEAR,\n");  
			sql.append("          f.FORECAST_MONTH,\n");  
			sql.append("          g.GROUP_CODE,\n");  
			sql.append("          g.GROUP_NAME\n");  
			sql.append(" ) B on a.group_id=b.group_id AND A.ORG_CODE=B.ROOT_ORG_CODE\n");
			sql.append(" where 1=1");
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"COMPANY".equals(logonOrgType)){
				sql.append(" and nvl(a.FORECAST_AMOUNT,0)+nvl(b.FORECAST_AMOUNT,0)+nvl(c.FORECAST_AMOUNT,0)>0");
			}
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
				sql.append(" and nvl(a.FORECAST_AMOUNT,0)+nvl(b.FORECAST_AMOUNT,0)>0");
			}
			sql.append(" order by a.ORG_CODE, a.GROUP_ID");
			//显示经销商数量end
*/
			//重新写查询语句
			sql.append("SELECT ORG_CODE,                                          \n");
			sql.append("       ORG_NAME,                                          \n");
			sql.append("       FORECAST_MONTH,                                    \n");
			sql.append("       GROUP_CODE,                                        \n");
			sql.append("       GROUP_NAME,                                        \n");
			sql.append("       GROUP_ID,                                          \n");
			//sql.append("       SUM(FORECAST_AMOUNT) FORECAST_AMOUNT,             \n");
			//sql.append("       SUM(LARGE_FORECAST_AMOUNT) LARGE_FORECAST_AMOUNT,  \n");
			//sql.append("       SUM(DEALER_FORECAST_AMOUNT) DEALER_FORECAST_AMOUNT \n");
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
				sql.append("       SUM(LARGE_FORECAST_AMOUNT) LARGE_FORECAST_AMOUNT,  \n");
				sql.append("       SUM(DEALER_FORECAST_AMOUNT) DEALER_FORECAST_AMOUNT \n");
			}
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"COMPANY".equals(logonOrgType)){
				sql.append("decode(org_code,(select org_code  from tm_org where org_id=?),SUM(FORECAST_AMOUNT),'') as FORECAST_AMOUNT,\n");
				params.add(companyId);
				sql.append("       SUM(LARGE_FORECAST_AMOUNT) LARGE_FORECAST_AMOUNT,  \n");
				sql.append("       SUM(DEALER_FORECAST_AMOUNT) DEALER_FORECAST_AMOUNT \n");
			}
			
			sql.append("  FROM ( \n");  
			//车厂端
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"COMPANY".equals(logonOrgType)){
			sql.append("  (select org.ORG_CODE, \n"); 
			sql.append("                org.ORG_NAME, \n"); 
			sql.append("                f.FORECAST_YEAR || '-' || f.FORECAST_MONTH FORECAST_MONTH, \n"); 
			sql.append("                g.GROUP_CODE, \n"); 
			sql.append("                g.GROUP_NAME, \n"); 
			sql.append("                d.GROUP_ID, \n"); 
			sql.append("                sum(d.FORECAST_AMOUNT) FORECAST_AMOUNT, \n"); 
			sql.append("                0 AS LARGE_FORECAST_AMOUNT, \n"); 
			sql.append("                0 AS DEALER_FORECAST_AMOUNT \n"); 
			sql.append("           from TT_VS_MONTHLY_FORECAST        f, \n"); 
			sql.append("                TT_VS_MONTHLY_FORECAST_DETAIL d, \n"); 
			sql.append("                TM_ORG                        org, \n"); 
			sql.append("                TM_VHCL_MATERIAL_GROUP        g \n"); 
			sql.append("          where f.FORECAST_ID = d.FORECAST_ID \n"); 
			sql.append("            and f.ORG_ID = org.ORG_ID \n"); 
			sql.append("            and f.forecast_type =  "+forecast_type+" \n"); 
			sql.append("            and g.GROUP_ID = d.GROUP_ID \n"); 
			sql.append("               --and d.FORECAST_AMOUNT > 0 \n"); 
			sql.append("               -- and org.DUTY_TYPE = 10431003 \n"); 
			sql.append("         and f.STATUS ="+Constant.FORECAST_STATUS_CONFIRM+" \n"); 
			sql.append("            and f.COMPANY_ID = ? \n"); 
			params.add(companyId);
			sql.append("            and f.FORECAST_YEAR = ? \n"); 
			params.add(plan_year);
			sql.append("            and f.FORECAST_MONTH = ? \n"); 
			params.add(plan_month);
			sql.append("            and f.ORG_TYPE = 10191001 \n"); 
			/*if(null!=orgCode&&!"".equals(orgCode)){
				sql.append("           and org.org_code in ("+PlanUtil.createSqlStr(orgCode)+")\n");  
			}*/
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append(" and org.org_code in (select org_code  from tm_org where org_id=?)");
			params.add(companyId);
			sql.append("          group by d.GROUP_ID, \n"); 
			sql.append("                   org.ORG_CODE, \n"); 
			sql.append("                   org.ORG_NAME, \n"); 
			sql.append("                   f.FORECAST_YEAR, \n"); 
			sql.append("                   f.FORECAST_MONTH, \n"); 
			sql.append("                   g.GROUP_CODE, \n"); 
			sql.append("                   g.GROUP_NAME) UNION \n"); 
			}
			//大区
			sql.append("         (select org.ORG_CODE, \n"); 
			sql.append("                org.ORG_NAME, \n"); 
			sql.append("                f.FORECAST_YEAR || '-' || f.FORECAST_MONTH FORECAST_MONTH,  \n"); 
			sql.append("                g.GROUP_CODE,                   \n"); 
			sql.append("                g.GROUP_NAME, \n"); 
			sql.append("                d.GROUP_ID, \n"); 
			sql.append("                0 AS FORECAST_AMOUNT, \n"); 
			sql.append("                sum(d.FORECAST_AMOUNT) LARGE_FORECAST_AMOUNT, \n"); 
			sql.append("                0 AS DEALER_FORECAST_AMOUNT \n"); 
			sql.append("           from TT_VS_MONTHLY_FORECAST        f, \n"); 
			sql.append("                TT_VS_MONTHLY_FORECAST_DETAIL d, \n"); 
			sql.append("                TM_ORG                        org, \n"); 
			sql.append("                TM_VHCL_MATERIAL_GROUP        g \n"); 
			sql.append("          where f.FORECAST_ID = d.FORECAST_ID \n"); 
			sql.append("            and f.ORG_ID = org.ORG_ID \n"); 
			sql.append("            and f.forecast_type =  "+forecast_type+" \n"); 
			sql.append("            and g.GROUP_ID = d.GROUP_ID \n"); 
			sql.append("            and d.FORECAST_AMOUNT > 0 \n"); 
			sql.append("               -- and org.DUTY_TYPE = 10431003 \n"); 
			sql.append("        and f.STATUS ="+Constant.FORECAST_STATUS_CONFIRM+" \n"); 
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"COMPANY".equals(logonOrgType)){
				sql.append("            and f.ORG_ID IN (SELECT ORG_ID \n"); 
				sql.append("                               FROM TM_ORG \n"); 
				sql.append("                              WHERE PARENT_ORG_ID = ? \n"); 
				params.add(companyId);
				sql.append("                                AND duty_type = 10431003) \n"); 
			}
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
				sql.append("      and f.ORG_ID=?\n");
				params.add(logonOrgId);
			}
			sql.append("            and f.COMPANY_ID = ?             \n"); 
			params.add(companyId);
			sql.append("            and f.FORECAST_YEAR = ? \n"); 
			params.add(plan_year);
			sql.append("            and f.FORECAST_MONTH = ?            \n");
			params.add(plan_month);
			sql.append("            and f.ORG_TYPE = 10191001 \n"); 
			if(null!=orgCode&&!"".equals(orgCode)){
				sql.append("           and org.org_code in ("+PlanUtil.createSqlStr(orgCode)+")\n");  
			}
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append("          group by d.GROUP_ID, \n"); 
			sql.append("                   org.ORG_CODE, \n"); 
			sql.append("                   org.ORG_NAME,             \n"); 
			sql.append("                   f.FORECAST_YEAR, \n"); 
			sql.append("                   f.FORECAST_MONTH, \n"); 
			sql.append("                   g.GROUP_CODE, \n"); 
			sql.append("                   g.GROUP_NAME)  \n"); 
			//经销商端
			sql.append("       UNION (select TD.ROOT_ORG_CODE, \n"); 
			sql.append("                TD.ROOT_ORG_NAME, \n"); 
			sql.append("                f.FORECAST_YEAR || '-' || f.FORECAST_MONTH FORECAST_MONTH,  \n"); 
			sql.append("                g.GROUP_CODE, \n"); 
			sql.append("                g.GROUP_NAME, \n"); 
			sql.append("                d.GROUP_ID, \n"); 
			sql.append("                0 AS FORECAST_AMOUNT, \n"); 
			sql.append("                0 AS LARGE_FORECAST_AMOUNT, \n"); 
			sql.append("                sum(d.FORECAST_AMOUNT) DEALER_FORECAST_AMOUNT \n"); 
			sql.append("           from TT_VS_MONTHLY_FORECAST        f,            \n"); 
			sql.append("                TT_VS_MONTHLY_FORECAST_DETAIL d, \n"); 
			sql.append("                VW_ORG_DEALER_ALL_NEW         TD, \n"); 
			sql.append("                TM_VHCL_MATERIAL_GROUP        g \n"); 
			sql.append("          where f.FORECAST_ID = d.FORECAST_ID \n"); 
			sql.append("            and f.DEALER_ID = TD.DEALER_ID \n"); 
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
				sql.append("      and TD.ROOT_ORG_ID=?\n");
				params.add(logonOrgId);
			}
			sql.append("            and f.forecast_type =  "+forecast_type+"  \n"); 
			sql.append("            and g.GROUP_ID = d.GROUP_ID \n"); 
			sql.append("            and d.FORECAST_AMOUNT > 0 \n"); 
			sql.append("               -- and org.DUTY_TYPE = 10431003 \n"); 
			sql.append("            and f.STATUS ="+Constant.FORECAST_STATUS_CONFIRM+" \n"); 
			sql.append("            and f.COMPANY_ID = ? \n"); 
			params.add(companyId);
			sql.append("            and f.FORECAST_YEAR = ? \n"); 
			params.add(plan_year);
			sql.append("            and f.FORECAST_MONTH = ? \n"); 
			params.add(plan_month);
			sql.append("            and f.ORG_TYPE = 10191002 \n");
			if(null!=orgCode&&!"".equals(orgCode)){
				sql.append("           and td.root_org_code in ("+PlanUtil.createSqlStr(orgCode)+")\n");  
			}
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append("          group by d.GROUP_ID, \n"); 
			sql.append("                   TD.ROOT_ORG_CODE, \n"); 
			sql.append("                   TD.ROOT_ORG_NAME, \n"); 
			sql.append("                   f.FORECAST_YEAR, \n"); 
			sql.append("                   f.FORECAST_MONTH, \n"); 
			sql.append("                   g.GROUP_CODE, \n"); 
			sql.append("                   g.GROUP_NAME)  \n"); 
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"COMPANY".equals(logonOrgType)){
			//车厂端统计大区加在车厂端后面
			sql.append("       UNION (select '40000' AS ORG_CODE, \n"); 
			sql.append("                '君马汽车销售有限公司' AS ORG_NAME, \n"); 
			sql.append("                f.FORECAST_YEAR || '-' || f.FORECAST_MONTH FORECAST_MONTH, \n"); 
			sql.append("                g.GROUP_CODE,  \n"); 
			sql.append("                g.GROUP_NAME, \n"); 
			sql.append("                d.GROUP_ID,          \n"); 
			sql.append("                0 AS FORECAST_AMOUNT, \n"); 
			sql.append("                sum(d.FORECAST_AMOUNT) LARGE_FORECAST_AMOUNT, \n"); 
			sql.append("                0 AS DEALER_FORECAST_AMOUNT \n"); 
			sql.append("           from TT_VS_MONTHLY_FORECAST        f, \n"); 
			sql.append("                TT_VS_MONTHLY_FORECAST_DETAIL d, \n"); 
			sql.append("                TM_ORG                        org, \n"); 
			sql.append("                TM_VHCL_MATERIAL_GROUP        g \n"); 
			sql.append("          where f.FORECAST_ID = d.FORECAST_ID \n"); 
			sql.append("            and f.ORG_ID = org.ORG_ID \n"); 
			sql.append("            and f.forecast_type =  "+forecast_type+" \n"); 
			sql.append("            and g.GROUP_ID = d.GROUP_ID \n"); 
			sql.append("            and d.FORECAST_AMOUNT > 0 \n"); 
			sql.append("               -- and org.DUTY_TYPE = 10431003 \n"); 
			sql.append("            and f.STATUS ="+Constant.FORECAST_STATUS_CONFIRM+" \n"); 
			sql.append("            and f.ORG_ID IN (SELECT ORG_ID \n"); 
			sql.append("                               FROM TM_ORG \n"); 
			sql.append("                              WHERE PARENT_ORG_ID = 2010010100070674 \n"); 
			sql.append("                                AND duty_type = 10431003) \n"); 
			sql.append("            and f.COMPANY_ID = 2010010100070674 \n"); 
			sql.append("            and f.FORECAST_YEAR = ? \n"); 
			params.add(plan_year);
			sql.append("            and f.FORECAST_MONTH = ? \n"); 
			params.add(plan_month);
			sql.append("            and f.ORG_TYPE = 10191001 \n"); 
			if(null!=orgCode&&!"".equals(orgCode)){
				sql.append("           and org.org_code in ("+PlanUtil.createSqlStr(orgCode)+")\n");  
			}
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append("          group by d.GROUP_ID, \n"); 
			sql.append("                   f.FORECAST_YEAR, \n"); 
			sql.append("                   f.FORECAST_MONTH, \n"); 
			sql.append("                   g.GROUP_CODE, \n"); 
			sql.append("                   g.GROUP_NAME)  \n"); 
			//车厂端统计经销商加在经销商数量后面
			sql.append("       UNION (select '40000' AS ORG_CODE, \n"); 
			sql.append("                '君马汽车销售有限公司' AS ORG_NAME, \n"); 
			sql.append("                f.FORECAST_YEAR || '-' || f.FORECAST_MONTH FORECAST_MONTH, \n"); 
			sql.append("                g.GROUP_CODE,   \n"); 
			sql.append("                g.GROUP_NAME, \n"); 
			sql.append("                d.GROUP_ID, \n"); 
			sql.append("                0 AS FORECAST_AMOUNT, \n"); 
			sql.append("                0 AS LARGE_FORECAST_AMOUNT, \n"); 
			sql.append("                sum(d.FORECAST_AMOUNT) DEALER_FORECAST_AMOUNT \n"); 
			sql.append("           from TT_VS_MONTHLY_FORECAST        f, \n"); 
			sql.append("                TT_VS_MONTHLY_FORECAST_DETAIL d, \n"); 
			sql.append("                VW_ORG_DEALER_ALL_NEW         TD, \n"); 
			sql.append("                TM_VHCL_MATERIAL_GROUP        g \n"); 
			sql.append("          where f.FORECAST_ID = d.FORECAST_ID \n"); 
			sql.append("            and f.DEALER_ID = TD.DEALER_ID \n"); 
			sql.append("            and f.forecast_type =  "+forecast_type+" \n"); 
			sql.append("            and g.GROUP_ID = d.GROUP_ID \n"); 
			sql.append("            and d.FORECAST_AMOUNT > 0             \n"); 
			sql.append("               -- and org.DUTY_TYPE = 10431003 \n"); 
			sql.append("            and f.STATUS ="+Constant.FORECAST_STATUS_CONFIRM+" \n"); 
			sql.append("            and f.COMPANY_ID = 2010010100070674 \n"); 
			sql.append("            and f.FORECAST_YEAR = ? \n"); 
			params.add(plan_year);
			sql.append("            and f.FORECAST_MONTH = ?               \n"); 
			params.add(plan_month);
			sql.append("            and f.ORG_TYPE = 10191002 \n"); 
			if(null!=orgCode&&!"".equals(orgCode)){
				sql.append("           and td.root_org_code in ("+PlanUtil.createSqlStr(orgCode)+")\n");  
			}
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append("          group by d.GROUP_ID, \n"); 
			sql.append("                   f.FORECAST_YEAR,      \n"); 
			sql.append("                   f.FORECAST_MONTH, \n"); 
			sql.append("                   g.GROUP_CODE, \n"); 
			sql.append("                   g.GROUP_NAME)         \n"); 
			}
			sql.append("   ) \n"); 
			sql.append("where 1=1 \n");
			/*if(null!=orgCode&&!"".equals(orgCode)){
				sql.append("           and org_code in ("+PlanUtil.createSqlStr(orgCode)+")\n");  
			}
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}*/
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"COMPANY".equals(logonOrgType)){
				sql.append("and nvl(forecast_amount,0)+nvl(large_forecast_amount,0)+nvl(dealer_forecast_amount,0)>0");
			}
			sql.append(" GROUP BY ORG_CODE, \n"); 
			sql.append("          ORG_NAME, \n"); 
			sql.append("          FORECAST_MONTH, \n"); 
			sql.append("          GROUP_CODE, \n"); 
			sql.append("          GROUP_NAME, \n"); 
			sql.append("          GROUP_ID \n"); 
			sql.append(" ORDER BY ORG_CODE, GROUP_CODE \n"); 

			return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		}
		
		/*
		 * dealer明细
		 */
		public PageResult<Map<String, Object>> oemSelectDealerRequireForecast(Map<String, Object> map,int pageSize,int curPage){
			String plan_year=(String)map.get("plan_year");
			String plan_month=(String)map.get("plan_month");
			String forecast_type=(String)map.get("forecast_type");//zxf
			String dealerCode=(String)map.get("dealerCode");
			String logonOrgType=(String)map.get("logonOrgType");
			String logonOrgId=(String)map.get("logonOrgId");
			String groupCode=map.get("groupCode").toString();
			//String areaId=map.get("areaId").toString();
			String companyId=map.get("companyId").toString();
			
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer();

			sql.append("select td.DEALER_CODE,\n");
			sql.append("       td.DEALER_SHORTNAME,\n");  
			sql.append("       f.FORECAST_YEAR || '-' || f.FORECAST_MONTH FORECAST_MONTH,\n");  
			sql.append("       g.GROUP_CODE,\n");  
			sql.append("       g.GROUP_NAME,\n");  
			sql.append("       d.GROUP_ID,\n");  
			sql.append("       sum(d.FORECAST_AMOUNT) FORECAST_AMOUNT\n");  
			sql.append("  from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("       TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("       TM_DEALER                       td,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP        g\n");  
			sql.append(" where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("   and f.DEALER_ID = td.DEALER_ID\n");  
			sql.append("   and g.GROUP_ID = d.GROUP_ID\n");
			sql.append("           and d.FORECAST_AMOUNT > 0\n");
			sql.append("   and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
				/* 长安原程序代码：
				sql.append("      and f.DEALER_ID in (select r.dealer_id\n");
				sql.append("                                    from TM_DEALER_ORG_RELATION r\n");  
				sql.append("                                   where r.ORG_ID = ?)\n");
				*/
                //铃木修改程序代码：begin   2012-11-23
				sql.append("      and f.DEALER_ID in (select r.dealer_id\n");
				sql.append("                                    from vw_org_dealer r\n");  
				sql.append("                                   where r.ROOT_ORG_ID = ?)\n");
				//铃木修改程序代码：end
				params.add(logonOrgId);
			}
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"SMALLREGION".equals(logonOrgType)){
                //铃木修改程序代码：begin   2012-11-23
				sql.append("      and f.DEALER_ID in (select r.dealer_id\n");
				sql.append("                                    from vw_org_dealer r\n");  
				sql.append("                                   where PQ_ORG_ID = ?)\n");
				//铃木修改程序代码：end
				params.add(logonOrgId);
			}
			//sql.append("   and f.AREA_ID = ?\n");  
			//params.add(areaId);
			sql.append("   and f.COMPANY_ID = ?\n");  
			params.add(companyId);
			sql.append("   and f.FORECAST_YEAR = ?\n");  
			params.add(plan_year);
			sql.append("   and f.FORECAST_MONTH = ?\n");  
			params.add(plan_month);
			sql.append("   and f.FORECAST_TYPE = ?\n");  //zxf
			params.add(forecast_type); //zxf
			sql.append("   and f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
			if(null!=dealerCode&&!"".equals(dealerCode)){
				sql.append("           and td.dealer_code in ("+PlanUtil.createSqlStr(dealerCode)+")\n");  
			}
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append(" group by d.GROUP_ID,\n");  
			sql.append("          td.DEALER_CODE,\n");  
			sql.append("          td.DEALER_SHORTNAME,\n");  
			sql.append("          f.FORECAST_YEAR,\n");  
			sql.append("          f.FORECAST_MONTH,\n");  
			sql.append("          g.GROUP_CODE,\n");  
			sql.append("          g.GROUP_NAME\n");  
			sql.append(" order by td.DEALER_CODE, d.GROUP_ID");

			return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		}
		
		/*
		 * dealer明细
		 */
		public PageResult<Map<String, Object>> oemSelectDealerRequireForecast_CVS(Map<String, Object> map,int pageSize,int curPage){
			String plan_year=(String)map.get("plan_year");
			String plan_month=(String)map.get("plan_month");
			String dealerCode=(String)map.get("dealerCode");
			String logonOrgType=(String)map.get("logonOrgType");
			String logonOrgId=(String)map.get("logonOrgId");
			String groupCode=map.get("groupCode").toString();
			//String areaId=map.get("areaId").toString();//zxf
			String companyId=map.get("companyId").toString();
			String forecast_type=(String)map.get("forecast_type");//zxf
			
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer();

			sql.append("select td.DEALER_CODE,\n");
			sql.append("       td.DEALER_SHORTNAME,\n");  
			sql.append("       f.FORECAST_YEAR || '-' || f.FORECAST_MONTH FORECAST_MONTH,\n");  
			sql.append("       g.GROUP_CODE,\n");  
			sql.append("       g.GROUP_NAME,\n");  
			sql.append("       d.GROUP_ID,\n");  
			sql.append("       sum(d.FORECAST_AMOUNT) FORECAST_AMOUNT\n");  
			sql.append("  from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("       TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("       TM_DEALER                       td,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP        g\n");  
			sql.append(" where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("   and f.DEALER_ID = td.DEALER_ID\n");  
			sql.append("   and g.GROUP_ID = d.GROUP_ID\n");  
			sql.append("   and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
				sql.append("          and F.dealer_id in\n" );
				sql.append("       (select m.dealer_id\n" );
				sql.append("          from tm_dealer m\n" );
				sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n" );
				sql.append("         START WITH m.status="+Constant.STATUS_ENABLE+" and m.dealer_id in\n" );
				/* 长安原程序代码：
				sql.append("                    (select rel.dealer_id\n" );
				sql.append("                       from tm_dealer_org_relation rel\n" );
				sql.append("                      where rel.org_id = ?))");
				*/
				//铃木修改程序代码：begin   2012-11-23
				sql.append("                    (select rel.dealer_id\n" );
				sql.append("                       from vw_org_dealer rel\n" );
				sql.append("                      where rel.ROOT_ORG_ID = ?))");
				//铃木修改程序代码：end
				params.add(logonOrgId);
			}
//			sql.append("   and f.AREA_ID = ?\n");  //zxf
//			params.add(areaId);
			sql.append("   and f.COMPANY_ID = ?\n");  
			params.add(companyId);
			sql.append("   and f.FORECAST_YEAR = ?\n");  
			params.add(plan_year);
			sql.append("   and f.FORECAST_MONTH = ?\n");  
			params.add(plan_month);
			sql.append("   and f.FORECAST_TYPE = ?\n");  //zxf
			params.add(forecast_type); //zxf
			sql.append("   and f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
			if(null!=dealerCode&&!"".equals(dealerCode)){
				sql.append("           and td.dealer_code in ("+PlanUtil.createSqlStr(dealerCode)+")\n");  
			}
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append(" group by d.GROUP_ID,\n");  
			sql.append("          td.DEALER_CODE,\n");  
			sql.append("          td.DEALER_SHORTNAME,\n");  
			sql.append("          f.FORECAST_YEAR,\n");  
			sql.append("          f.FORECAST_MONTH,\n");  
			sql.append("          g.GROUP_CODE,\n");  
			sql.append("          g.GROUP_NAME\n");  
			sql.append(" order by td.DEALER_CODE, d.GROUP_ID");

			return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		}
		
		/*
		 *部门汇总
		 */
		public PageResult<Map<String, Object>> oemSelectDeptRequireForecastTotal(Map<String, Object> map,int pageSize,int curPage){
			
			List<Object> params = new LinkedList<Object>();
			String sql=oemSelectDeptRequireForecastSql(map);

			return dao.pageQuery(sql, params, getFunName(), pageSize, curPage);
		}
		//部门汇总查询
		private String oemSelectDeptRequireForecastSql(Map<String, Object> map){
			String plan_year=(String)map.get("plan_year");
			String plan_month=(String)map.get("plan_month");
			String groupCode=map.get("groupCode").toString();
			String areaId=map.get("areaId").toString();
			String companyId=map.get("companyId").toString();
			String dutyType=map.get("dutyType").toString() ;
			
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer();

			sql.append("SELECT G1.GROUP_CODE SERIES_CODE,\n");
			sql.append("       G1.GROUP_NAME SERIES_NAME,\n");  
			sql.append("       G2.GROUP_CODE MODEL_CODE,\n");  
			sql.append("       G2.GROUP_NAME MODEL_NAME,\n");  
			sql.append("       G3.GROUP_CODE,\n");  
			sql.append("       G3.GROUP_NAME,\n");  
			sql.append("       nvl(AREA.REPORT_AMOUNT,0) REPORT_AMOUNT,\n");  
			sql.append("       nvl(DEPT.FORECAST_AMOUNT,0) FORECAST_AMOUNT\n");  
			sql.append("  FROM (select d.GROUP_ID, sum(d.FORECAST_AMOUNT) FORECAST_AMOUNT\n");  
			sql.append("          from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("               TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("               TM_ORG                        org\n");  
			sql.append("         where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("           and f.ORG_ID = org.ORG_ID\n");  
			sql.append("           and f.AREA_ID = "+areaId+"\n");  
			sql.append("           and f.COMPANY_ID = "+companyId+"\n"); 
			sql.append("           and org.DUTY_TYPE = "+dutyType+"\n");  
			sql.append("           and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			sql.append("   and f.FORECAST_YEAR = "+plan_year+"\n");  
			sql.append("   and f.FORECAST_MONTH = "+plan_month+"\n");  
			sql.append("           and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
			sql.append("         group by d.GROUP_ID) DEPT,\n");  
			sql.append("       (select d.GROUP_ID, sum(d.FORECAST_AMOUNT) REPORT_AMOUNT\n");  
			sql.append("          from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("               TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("               TM_ORG                        org\n");  
			sql.append("         where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("           and f.ORG_ID = org.ORG_ID\n"); 
			sql.append("           and f.AREA_ID = "+areaId+"\n");  
			sql.append("           and f.COMPANY_ID = "+companyId+"\n"); 
			sql.append("           and org.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n");  
			sql.append("           and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			sql.append("   and f.FORECAST_YEAR = "+plan_year+"\n");  
			sql.append("   and f.FORECAST_MONTH = "+plan_month+"\n");  
			sql.append("           and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
			sql.append("         group by d.GROUP_ID\n");  
			sql.append("\n");  
			sql.append("        ) AREA,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G2,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G3\n");  
			sql.append(" WHERE G1.GROUP_ID = G2.PARENT_GROUP_ID\n");  
			sql.append("   AND G2.GROUP_ID = G3.PARENT_GROUP_ID\n");  
			sql.append("   AND G3.GROUP_ID = DEPT.GROUP_ID\n");  
			sql.append("   AND DEPT.GROUP_ID = AREA.GROUP_ID(+)\n");  
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and G3.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append(" UNION\n");
			sql.append("SELECT G1.GROUP_CODE SERIES_CODE,\n");
			sql.append("       G1.GROUP_NAME SERIES_NAME,\n");  
			sql.append("       G2.GROUP_CODE MODEL_CODE,\n");  
			sql.append("       G2.GROUP_NAME MODEL_NAME,\n");  
			sql.append("       G3.GROUP_CODE,\n");  
			sql.append("       G3.GROUP_NAME,\n");  
			sql.append("       nvl(AREA.REPORT_AMOUNT,0) REPORT_AMOUNT,\n");  
			sql.append("       nvl(DEPT.FORECAST_AMOUNT,0) FORECAST_AMOUNT\n");  
			sql.append("  FROM (select d.GROUP_ID, sum(d.FORECAST_AMOUNT) FORECAST_AMOUNT\n");  
			sql.append("          from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("               TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("               TM_ORG                        org\n");  
			sql.append("         where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("           and f.ORG_ID = org.ORG_ID\n");  
			sql.append("           and f.AREA_ID = "+areaId+"\n");  
			sql.append("           and f.COMPANY_ID = "+companyId+"\n"); 
			sql.append("           and org.DUTY_TYPE = "+dutyType+"\n");  
			sql.append("           and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			sql.append("   and f.FORECAST_YEAR = "+plan_year+"\n");  
			sql.append("   and f.FORECAST_MONTH = "+plan_month+"\n");  
			sql.append("           and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
			sql.append("         group by d.GROUP_ID) DEPT,\n");  
			sql.append("       (select d.GROUP_ID, sum(d.FORECAST_AMOUNT) REPORT_AMOUNT\n");  
			sql.append("          from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("               TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("               TM_ORG                        org\n");  
			sql.append("         where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("           and f.ORG_ID = org.ORG_ID\n");  
			sql.append("           and f.AREA_ID = "+areaId+"\n");  
			sql.append("           and f.COMPANY_ID = "+companyId+"\n"); 
			sql.append("           and org.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n");  
			sql.append("           and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			sql.append("   and f.FORECAST_YEAR = "+plan_year+"\n");  
			sql.append("   and f.FORECAST_MONTH = "+plan_month+"\n");  
			sql.append("           and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
			sql.append("         group by d.GROUP_ID\n");  
			sql.append("\n");  
			sql.append("        ) AREA,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G2,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G3\n");  
			sql.append(" WHERE G1.GROUP_ID = G2.PARENT_GROUP_ID\n");  
			sql.append("   AND G2.GROUP_ID = G3.PARENT_GROUP_ID\n");  
			sql.append("   AND G3.GROUP_ID = AREA.GROUP_ID\n");  
			sql.append("   AND DEPT.GROUP_ID(+) = AREA.GROUP_ID\n");  
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and G3.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			
			sql.append(" ORDER BY SERIES_CODE");


			return sql.toString();
		}
		/*
		 *区域汇总
		 */
		public PageResult<Map<String, Object>> oemSelectAreaRequireForecastTotal(Map<String, Object> map,int pageSize,int curPage){
			
			List<Object> params = new LinkedList<Object>();
			String sql=getAreaTotalSearchSql(map);
			return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		}
		
		
		//区域汇总查询SQL
		private String getAreaTotalSearchSql(Map<String, Object> map){
			String plan_year=(String)map.get("plan_year");
			String plan_month=(String)map.get("plan_month");
			String logonOrgId=(String)map.get("logonOrgId");
			String groupCode=map.get("groupCode").toString();
			String areaId=map.get("areaId").toString();
			String companyId=map.get("companyId").toString();
			
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer();

			sql.append("SELECT G1.GROUP_CODE SERIES_CODE,\n");
			sql.append("       G1.GROUP_NAME SERIES_NAME,\n");  
			sql.append("       G2.GROUP_CODE MODEL_CODE,\n");  
			sql.append("       G2.GROUP_NAME MODEL_NAME,\n");  
			sql.append("       G3.GROUP_CODE,\n");  
			sql.append("       G3.GROUP_NAME,\n");  
			sql.append("       nvl(AREA.FORECAST_AMOUNT,0) FORECAST_AMOUNT,\n");  
			sql.append("       nvl(DEALER.REPORT_AMOUNT,0) REPORT_AMOUNT\n");  
			sql.append("  FROM (select d.GROUP_ID, sum(d.FORECAST_AMOUNT) REPORT_AMOUNT\n");  
			sql.append("          from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("               TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("               TM_DEALER                     td\n");
			sql.append("          where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("           and f.DEALER_ID = td.DEALER_ID");
			sql.append("           and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			sql.append("           and f.FORECAST_YEAR = "+plan_year+"\n");  
			sql.append("           and f.FORECAST_MONTH = "+plan_month+"\n");  
			sql.append("           and f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");
			sql.append("           and f.AREA_ID = "+areaId+"\n");  
			sql.append("           and f.COMPANY_ID = "+companyId+"\n"); 
			
			sql.append("         and f.DEALER_ID in\n");
			sql.append("               (select r.dealer_id\n");  
			sql.append("                  from TM_DEALER_ORG_RELATION r\n");  
			sql.append("                 where r.ORG_ID = "+logonOrgId+")\n");

			sql.append("         group by d.GROUP_ID) DEALER,\n");  
			sql.append("       (select d.GROUP_ID, sum(d.FORECAST_AMOUNT) FORECAST_AMOUNT\n");  
			sql.append("          from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("               TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("               TM_ORG                        org\n");  
			sql.append("         where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("           and f.ORG_ID = org.ORG_ID\n"); 
			sql.append("           and f.AREA_ID = "+areaId+"\n");  
			sql.append("           and f.COMPANY_ID = "+companyId+"\n");  
			sql.append("           and org.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n");  
			sql.append("           and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			sql.append("           and org.ORG_ID="+logonOrgId+"\n");
			sql.append("           and f.FORECAST_YEAR = "+plan_year+"\n");  
			sql.append("           and f.FORECAST_MONTH = "+plan_month+"\n");  
			sql.append("           and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
			sql.append("         group by d.GROUP_ID\n");  
			sql.append("\n");  
			sql.append("        ) AREA,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G2,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G3\n");  
			sql.append(" WHERE G1.GROUP_ID = G2.PARENT_GROUP_ID\n");  
			sql.append("   AND G2.GROUP_ID = G3.PARENT_GROUP_ID\n");  
			sql.append("   AND G3.GROUP_ID = DEALER.GROUP_ID\n");  
			sql.append("   AND DEALER.GROUP_ID = AREA.GROUP_ID(+)\n");  
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and G3.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append(" UNION\n");
			sql.append("SELECT G1.GROUP_CODE SERIES_CODE,\n");
			sql.append("       G1.GROUP_NAME SERIES_NAME,\n");  
			sql.append("       G2.GROUP_CODE MODEL_CODE,\n");  
			sql.append("       G2.GROUP_NAME MODEL_NAME,\n");  
			sql.append("       G3.GROUP_CODE,\n");  
			sql.append("       G3.GROUP_NAME,\n");  
			sql.append("       nvl(AREA.FORECAST_AMOUNT,0) FORECAST_AMOUNT,\n");  
			sql.append("       nvl(DEALER.REPORT_AMOUNT,0) REPORT_AMOUNT\n");  
			sql.append("  FROM (select d.GROUP_ID, sum(d.FORECAST_AMOUNT) REPORT_AMOUNT\n");  
			sql.append("          from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("               TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("               TM_DEALER                     td\n");
			sql.append("          where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("           and f.DEALER_ID = td.DEALER_ID");
			sql.append("           and f.AREA_ID = "+areaId+"\n");  
			sql.append("           and f.COMPANY_ID = "+companyId+"\n"); 
			sql.append("           and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			sql.append("           and f.FORECAST_YEAR = "+plan_year+"\n");  
			sql.append("           and f.FORECAST_MONTH = "+plan_month+"\n");  
			sql.append("           and f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n"); 
			sql.append("         and f.DEALER_ID in\n");
			sql.append("               (select r.dealer_id\n");  
			sql.append("                  from TM_DEALER_ORG_RELATION r\n");  
			sql.append("                 where r.ORG_ID = "+logonOrgId+")\n");
			sql.append("         group by d.GROUP_ID) DEALER,\n");  
			sql.append("       (select d.GROUP_ID, sum(d.FORECAST_AMOUNT) FORECAST_AMOUNT\n");  
			sql.append("          from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("               TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("               TM_ORG                        org\n");  
			sql.append("         where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("           and f.ORG_ID = org.ORG_ID\n");  
			sql.append("           and f.AREA_ID = "+areaId+"\n");  
			sql.append("           and f.COMPANY_ID = "+companyId+"\n"); 
			sql.append("           and org.DUTY_TYPE = "+Constant.DUTY_TYPE_LARGEREGION+"\n");  
			sql.append("           and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			sql.append("           and org.ORG_ID="+logonOrgId+"");
			params.add(logonOrgId);
			sql.append("           and f.FORECAST_YEAR = "+plan_year+"\n");  
			params.add(plan_year);
			sql.append("           and f.FORECAST_MONTH = "+plan_month+"\n");  
			params.add(plan_month);  
			sql.append("           and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
			sql.append("         group by d.GROUP_ID\n");  
			sql.append("\n");  
			sql.append("        ) AREA,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G2,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G3\n");  
			sql.append(" WHERE G1.GROUP_ID = G2.PARENT_GROUP_ID\n");  
			sql.append("   AND G2.GROUP_ID = G3.PARENT_GROUP_ID\n");  
			sql.append("   AND G3.GROUP_ID = AREA.GROUP_ID\n");  
			sql.append("   AND DEALER.GROUP_ID(+) = AREA.GROUP_ID\n");  
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and G3.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			
			sql.append(" ORDER BY SERIES_CODE");
			
			return sql.toString();
		}
		/*
		 *dealer查询
		 */
		public PageResult<Map<String, Object>> dealerSelectRequireForecast(Map<String, Object> map,int pageSize,int curPage){
			String plan_year=(String)map.get("plan_year");
			String plan_month=(String)map.get("plan_month");
			String forecast_type=(String)map.get("forecast_type");//zxf
			String dealerId=(String)map.get("dealerId");
			String groupCode=map.get("groupCode").toString();
			String companyId=map.get("companyId").toString();
			//String areaId=map.get("areaId").toString();//zxf
			
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer();

			sql.append("SELECT G1.GROUP_CODE SERIES_CODE,\n");
			sql.append("       G1.GROUP_NAME SERIES_NAME,\n");  
			sql.append("       G2.GROUP_CODE MODEL_CODE,\n");  
			sql.append("       G2.GROUP_NAME MODEL_NAME,\n");  
			sql.append("       G3.GROUP_CODE,\n");  
			sql.append("       G3.GROUP_NAME,\n");  
			sql.append("       DEALER.REPORT_AMOUNT\n");  
			sql.append("  FROM (select d.GROUP_ID, sum(d.FORECAST_AMOUNT) REPORT_AMOUNT\n");  
			sql.append("          from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("               TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("               TM_DEALER                     td\n");
			sql.append("          where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("           and f.DEALER_ID = td.DEALER_ID\n");
			sql.append("           and d.FORECAST_AMOUNT > 0\n");
			sql.append("           and f.COMPANY_ID="+companyId+"\n");
			sql.append("           and f.DEALER_ID in ("+PlanUtil.createSqlStr(dealerId)+")\n");
			sql.append("           and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			sql.append("           and f.FORECAST_YEAR = "+plan_year+"\n");  
			sql.append("           and f.FORECAST_MONTH = "+plan_month+"\n");  
			sql.append("           and f.FORECAST_TYPE = "+forecast_type+"\n"); //zxf
			//sql.append("           and f.AREA_ID = "+areaId+"\n");//zxf 
			sql.append("           and f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
			sql.append("         group by d.GROUP_ID) DEALER,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G2,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G3\n");  
			sql.append(" WHERE G1.GROUP_ID = G2.PARENT_GROUP_ID\n");  
			sql.append("   AND G2.GROUP_ID = G3.PARENT_GROUP_ID\n");  
			sql.append("   AND G3.GROUP_ID = DEALER.GROUP_ID\n");  
			if(null!=groupCode&&!"".equals(groupCode)&&!"1".equals(groupCode)){
				sql.append("           and G3.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append(" ORDER BY SERIES_CODE");

			return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		}
		
	/*
	 * -----------------------------------------------------------下载-----------------------------------------------
	 * 
	 */
		
		/*
		 * 部门明细
		 */
		public List<Map<String, Object>> oemSelectOrgRequireForecastDown(Map<String, Object> map){
			String plan_year=(String)map.get("plan_year");
			String plan_month=(String)map.get("plan_month");
			String orgCode=(String)map.get("orgCode");
			String logonOrgType=(String)map.get("logonOrgType");
			String logonOrgId=(String)map.get("logonOrgId");
			String groupCode=map.get("groupCode").toString();
			//String areaId=map.get("areaId").toString();
			String companyId=map.get("companyId").toString();
			
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer();

			sql.append("select org.ORG_CODE,\n");
			sql.append("       org.ORG_NAME,\n");  
			sql.append("       f.FORECAST_YEAR || '-' || f.FORECAST_MONTH FORECAST_MONTH,\n");  
			sql.append("       g.GROUP_CODE,\n");  
			sql.append("       g.GROUP_NAME,\n");  
			sql.append("       d.GROUP_ID,\n");  
			sql.append("       sum(d.FORECAST_AMOUNT) FORECAST_AMOUNT\n");  
			sql.append("  from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("       TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("       TM_ORG                        org,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP        g\n");  
			sql.append(" where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("   and f.ORG_ID = org.ORG_ID\n");  
			sql.append("   and g.GROUP_ID = d.GROUP_ID\n");
			sql.append("           and d.FORECAST_AMOUNT > 0\n");
			//sql.append("           and f.AREA_ID = "+areaId+"\n");  
			sql.append("           and f.COMPANY_ID = "+companyId+"\n"); 
			sql.append("      -- and org.DUTY_TYPE = 10431003\n");  
			sql.append("   and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
				sql.append("      and f.ORG_ID=?\n");
				params.add(logonOrgId);
			}
			//小区
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"SMALLREGION".equals(logonOrgType)){
				sql.append("          and TVMF.dealer_id in\n" );
				sql.append("       (select m.dealer_id\n" );
				sql.append("          from tm_dealer m\n" );
				sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n" );
				sql.append("         START WITH m.status="+Constant.STATUS_ENABLE+" and m.dealer_id in\n" );
				sql.append("                    (select rel.dealer_id\n" );
				sql.append("                       from vw_org_dealer rel\n" );
				sql.append("                      where rel.PQ_ORG_ID ="+logonOrgId+"))");
			}
			sql.append("   and f.FORECAST_YEAR = ?\n");  
			params.add(plan_year);
			sql.append("   and f.FORECAST_MONTH = ?\n");  
			params.add(plan_month);
			sql.append("   and f.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
			if(null!=orgCode&&!"".equals(orgCode)){
				sql.append("           and org.org_code in ("+PlanUtil.createSqlStr(orgCode)+")\n");  
			}
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append(" group by d.GROUP_ID,\n");  
			sql.append("          org.ORG_CODE,\n");  
			sql.append("          org.ORG_NAME,\n");  
			sql.append("          f.FORECAST_YEAR,\n");  
			sql.append("          f.FORECAST_MONTH,\n");  
			sql.append("          g.GROUP_CODE,\n");  
			sql.append("          g.GROUP_NAME\n");  
			sql.append(" order by org.ORG_CODE, d.GROUP_ID");

			return dao.pageQuery(sql.toString(), params, getFunName());
		}
		
		/*
		 * 区域明细
		 */
		public List<Map<String, Object>> oemSelectDealerRequireForecastDown(Map<String, Object> map){
			String plan_year=(String)map.get("plan_year");
			String plan_month=(String)map.get("plan_month");
			String dealerCode=(String)map.get("dealerCode");
			String logonOrgType=(String)map.get("logonOrgType");
			String logonOrgId=(String)map.get("logonOrgId");
			String groupCode=map.get("groupCode").toString();
		//	String areaId=map.get("areaId").toString();
			String companyId=map.get("companyId").toString();
			
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer();

			sql.append("select td.DEALER_CODE,\n");
			sql.append("       td.DEALER_SHORTNAME,\n");  
			sql.append("       f.FORECAST_YEAR || '-' || f.FORECAST_MONTH FORECAST_MONTH,\n");  
			sql.append("       g.GROUP_CODE,\n");  
			sql.append("       g.GROUP_NAME,\n");  
			sql.append("       d.GROUP_ID,\n");  
			sql.append("       sum(d.FORECAST_AMOUNT) FORECAST_AMOUNT\n");  
			sql.append("  from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("       TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("       TM_DEALER                       td,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP        g\n");  
			sql.append(" where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("   and f.DEALER_ID = td.DEALER_ID\n");  
			sql.append("   and g.GROUP_ID = d.GROUP_ID\n"); 
			sql.append("           and d.FORECAST_AMOUNT > 0\n");
		//	sql.append("   and f.AREA_ID = "+areaId+"\n");  
			sql.append("   and f.COMPANY_ID = "+companyId+"\n"); 
			sql.append("   and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
				sql.append("          and F.dealer_id in\n" );
				sql.append("       (select m.dealer_id\n" );
				sql.append("          from tm_dealer m\n" );
				sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n" );
				sql.append("         START WITH m.status="+Constant.STATUS_ENABLE+" and m.dealer_id in\n" );
				/* 长安原程序代码：
				sql.append("                    (select rel.dealer_id\n" );
				sql.append("                       from tm_dealer_org_relation rel\n" );
				sql.append("                      where rel.org_id = ?))");
				*/
				//铃木修改程序代码：begin   2012-11-23
				sql.append("                    (select rel.dealer_id\n" );
				sql.append("                       from vw_org_dealer rel\n" );
				sql.append("                      where rel.ROOT_ORG_ID = ?))");
				//铃木修改程序代码：end
				params.add(logonOrgId);
			}
			//小区
			if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"SMALLREGION".equals(logonOrgType)){
				sql.append("          and F.dealer_id in\n" );
				sql.append("       (select m.dealer_id\n" );
				sql.append("          from tm_dealer m\n" );
				sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n" );
				sql.append("         START WITH m.status="+Constant.STATUS_ENABLE+" and m.dealer_id in\n" );
				sql.append("                    (select rel.dealer_id\n" );
				sql.append("                       from vw_org_dealer rel\n" );
				sql.append("                      where rel.PQ_ORG_ID = ?))");
				params.add(logonOrgId);
			}
			sql.append("   and f.FORECAST_YEAR = ?\n");  
			params.add(plan_year);
			sql.append("   and f.FORECAST_MONTH = ?\n");  
			params.add(plan_month);
			sql.append("   and f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
			if(null!=dealerCode&&!"".equals(dealerCode)){
				sql.append("           and td.dealer_code in ("+PlanUtil.createSqlStr(dealerCode)+")\n");  
			}
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and g.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append(" group by d.GROUP_ID,\n");  
			sql.append("          td.DEALER_CODE,\n");  
			sql.append("          td.DEALER_SHORTNAME,\n");  
			sql.append("          f.FORECAST_YEAR,\n");  
			sql.append("          f.FORECAST_MONTH,\n");  
			sql.append("          g.GROUP_CODE,\n");  
			sql.append("          g.GROUP_NAME\n");  
			sql.append(" order by td.DEALER_CODE, d.GROUP_ID");

			return dao.pageQuery(sql.toString(), params, getFunName());
		}
		
		/*
		 *部门汇总
		 */
		public List<Map<String, Object>> oemSelectDeptRequireForecastTotalDown(Map<String, Object> map){
			String sql=oemSelectDeptRequireForecastSql(map);

			return dao.pageQuery(sql.toString(), null, getFunName());
		}
		
		/*
		 *区域汇总
		 */
		/*public List<Map<String, Object>> oemSelectAreaRequireForecastTotalDown(Map<String, Object> map,List<Map<String,Object>> serlist){
			
			List<Object> params = new LinkedList<Object>();
			// String sql=SelectOrgForecastTotal(map,serlist);

			return dao.pageQuery(sql.toString(), params, getFunName());
		}*/
		
		/*
		 *dealer查询
		 */
		public List<Map<String, Object>> dealerSelectRequireForecastDown(Map<String, Object> map){
			String plan_year=(String)map.get("plan_year");
			String plan_month=(String)map.get("plan_month");
			String dealerId=(String)map.get("dealerId");
			String groupCode=map.get("groupCode").toString();
			String areaId=map.get("areaId").toString();
			String companyId=map.get("companyId").toString();
			
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer();

			sql.append("SELECT G1.GROUP_CODE SERIES_CODE,\n");
			sql.append("       G1.GROUP_NAME SERIES_NAME,\n");  
			sql.append("       G2.GROUP_CODE MODEL_CODE,\n");  
			sql.append("       G2.GROUP_NAME MODEL_NAME,\n");  
			sql.append("       G3.GROUP_CODE,\n");  
			sql.append("       G3.GROUP_NAME,\n");  
			sql.append("       DEALER.REPORT_AMOUNT\n");  
			sql.append("  FROM (select d.GROUP_ID, sum(d.FORECAST_AMOUNT) REPORT_AMOUNT\n");  
			sql.append("          from TT_VS_MONTHLY_FORECAST        f,\n");  
			sql.append("               TT_VS_MONTHLY_FORECAST_DETAIL d,\n");  
			sql.append("               TM_DEALER                     td\n");
			sql.append("          where f.FORECAST_ID = d.FORECAST_ID\n");  
			sql.append("           and f.DEALER_ID = td.DEALER_ID\n");
			sql.append("           and d.FORECAST_AMOUNT > 0\n");
			
			sql.append("           and f.COMPANY_ID="+companyId+"\n");
			sql.append("           and f.DEALER_ID in ("+PlanUtil.createSqlStr(dealerId)+")\n");
			sql.append("           and f.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
			sql.append("           and f.FORECAST_YEAR = ?\n");  
			params.add(plan_year);
			sql.append("           and f.FORECAST_MONTH = ?\n");  
			params.add(plan_month);
			//sql.append("           and f.AREA_ID = "+areaId+"\n"); 
			sql.append("           and f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
			sql.append("         group by d.GROUP_ID) DEALER,\n");  

			sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G2,\n");  
			sql.append("       TM_VHCL_MATERIAL_GROUP G3\n");  
			sql.append(" WHERE G1.GROUP_ID = G2.PARENT_GROUP_ID\n");  
			sql.append("   AND G2.GROUP_ID = G3.PARENT_GROUP_ID\n");  //sql.append("   AND G2.GROUP_ID = G3.GROUP_ID\n");  
			sql.append("   AND G3.GROUP_ID = DEALER.GROUP_ID\n");  
			if(null!=groupCode&&!"".equals(groupCode)){
				sql.append("           and G3.GROUP_CODE in ("+PlanUtil.createSqlStr(groupCode)+")\n");  
			}
			sql.append(" ORDER BY SERIES_CODE");

			return dao.pageQuery(sql.toString(), params, getFunName());
		}
		
		/*
		 * 查询前三月的参考信息
		 */
		public PageResult<Map<String, Object>> selectHistoryMonth(String duty,String orgId,String groupId,List<Map<String, Object>> list,int pageSize,int curPage){
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer("");


			sql.append("SELECT GRO.GROUP_ID, --配置ID\n");
			sql.append("       GRO.GROUP_CODE,--配置代码\n");  
			sql.append("       GRO.GROUP_NAME,--配置名称\n");  
			sql.append("       NVL(FORE.FORECAST_AMOUNT, 0) AS FORECAST_AMOUNT, --前三个月平均预测数量\n");  
			sql.append("       NVL(ORD.ORDER_AMOUNT, 0) AS ORDER_AMOUNT, --前三月平均订单数量\n");  
			sql.append("       NVL(ORD.MATCH_AMOUNT, 0) AS MATCH_AMOUNT, --前三月平均实销量\n");  
			sql.append("       NVL(VEH.VEHICLE_AMOUNT, 0) AS VEHICLE_AMOUNT --有效库存数量\n");  
			sql.append("  FROM (SELECT TVMG.GROUP_CODE, TVMG.GROUP_ID, TVMG.GROUP_NAME\n");  
			sql.append("          FROM TM_VHCL_MATERIAL_GROUP TVMG\n");  
			sql.append("         WHERE TVMG.GROUP_ID = "+groupId+"--换成配置ID\n");  
			sql.append("         ) GRO\n");  
			sql.append("  LEFT JOIN (SELECT TVMGR.GROUP_ID,\n");  
			sql.append("                    ROUND(NVL(SUM(TVOD.ORDER_AMOUNT), 0) / 3, 0) AS ORDER_AMOUNT, --前三月平均订单数量\n");  
			sql.append("                    ROUND(NVL(SUM(TVOD.MATCH_AMOUNT), 0) / 3, 0) AS MATCH_AMOUNT --前三月平均实销量\n");  
			sql.append("               FROM TT_VS_ORDER              TVO,\n");  
			sql.append("                    TT_VS_ORDER_DETAIL       TVOD,\n");  
			sql.append("                    TM_VHCL_MATERIAL_GROUP_R TVMGR\n");  
			sql.append("              WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");  
			sql.append("                AND TVOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
			sql.append("                AND TVO.ORDER_STATUS IN("+Constant.ORDER_STATUS_03+","+Constant.ORDER_STATUS_05+") --只要审核通过的和已提报的\n");  
			if(null!=orgId&&!"".equals(orgId)&&duty.equals(Constant.DUTY_TYPE_LARGEREGION.toString())){
				sql.append("                AND TVO.ORDER_ORG_ID in (select r.dealer_id from TM_DEALER_ORG_RELATION r where r.ORG_ID = "+orgId+")\n");  
			}
			sql.append("                AND (\n");  
			for(int i=0;i<list.size();i++){
				Map<String, Object> map=list.get(i);
				sql.append("(TVO.ORDER_YEAR = "+map.get("YEAR")+" AND TVO.ORDER_MONTH = "+map.get("MONTH")+") OR\n");
			}
			sql.delete(sql.lastIndexOf("OR"), sql.length());
			sql.append("                    ) --取当前的月的前三个月和年一起考虑\n");  
			sql.append("              GROUP BY TVMGR.GROUP_ID) ORD ON GRO.GROUP_ID = ORD.GROUP_ID\n");  
			sql.append("  LEFT JOIN (SELECT TVMFD.GROUP_ID,\n");  
			sql.append("                    ROUND(NVL(SUM(TVMFD.FORECAST_AMOUNT), 0) / 3, 0) AS FORECAST_AMOUNT --预测数量\n");  
			sql.append("               FROM TT_VS_MONTHLY_FORECAST        TVMF,\n");  
			sql.append("                    TT_VS_MONTHLY_FORECAST_DETAIL TVMFD\n");  
			sql.append("              WHERE TVMF.FORECAST_ID = TVMFD.FORECAST_ID\n");  
			if(null!=orgId&&!"".equals(orgId)){
				sql.append("                AND TVMF.ORG_ID in ("+PlanUtil.createSqlStr(orgId)+") --换成dealer_id\n");
			}
			sql.append("                AND TVMF.ORG_TYPE="+Constant.ORG_TYPE_OEM+"\n");  
			sql.append("                AND TVMF.STATUS ="+Constant.FORECAST_STATUS_CONFIRM+" --取已提交的预测\n");  
			sql.append("                AND (\n");  
			for(int i=0;i<list.size();i++){
				Map<String, Object> map=list.get(i);
				sql.append("(TVMF.FORECAST_YEAR = "+map.get("YEAR")+" AND TVMF.FORECAST_MONTH = "+map.get("MONTH")+") OR\n");
			}
			sql.delete(sql.lastIndexOf("OR"), sql.length());
			sql.append("                   ) --取当前的月的前三个月和年一起考虑\n");  
			sql.append("              GROUP BY TVMFD.GROUP_ID) FORE ON GRO.GROUP_ID = FORE.GROUP_ID\n");  
			sql.append("\n");  
			sql.append("  LEFT JOIN (SELECT TVMGR.GROUP_ID, COUNT(TV.VEHICLE_ID) AS VEHICLE_AMOUNT\n");  
			sql.append("               FROM TM_VEHICLE TV, TM_VHCL_MATERIAL_GROUP_R TVMGR\n");  
			sql.append("              WHERE TV.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
			sql.append("                AND TV.ORG_TYPE IS NULL\n");  
			sql.append("                AND TV.ORG_ID IS NULL\n");  
			sql.append("                AND TV.DEALER_ID IS NULL\n");  
			sql.append("                AND TV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");  
			sql.append("                AND TV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_02+"\n");  
			sql.append("              GROUP BY TVMGR.GROUP_ID) VEH ON GRO.GROUP_ID = VEH.GROUP_ID");


			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
			return ps;
		}	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	// 车厂汇总
	public PageResult<Map<String, Object>> oemSelectDealerMonthForecastTotal(Map<String, Object> map,List<Map<String, Object>> serlist,int pageSize,int curPage){
		String forecast_year=(String)map.get("forecast_year");
		String forecast_month=(String)map.get("forecast_month");
		String forecast_type=(String)map.get("forecast_type");//zxf
		String dealerCode=(String)map.get("dealerCode");
		String logonOrgType=(String)map.get("logonOrgType");
		String logonOrgId=(String)map.get("logonOrgId");
		String companyId=map.get("companyId").toString();
		//String areaId=map.get("areaId").toString();zxf
		String groupCode=(String)map.get("groupCode");
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer();

		sql.append("\n");
		sql.append("SELECT YEARLY.DEALER_CODE,\n");  
		sql.append("       YEARLY.DEALER_SHORTNAME,\n");
		
		for(int i=0;i<serlist.size();i++){
			Map<String, Object> sermap=serlist.get(i);
			sql.append("       nvl(MAX(DECODE(YEARLY.GROUP_ID,"+sermap.get("GROUP_ID")+",YEARLY.FORE_AMOUNT)),0) S"+i+",\n");  
		}
		sql.append("       nvl(sum(YEARLY.FORE_AMOUNT),0) FA,\n");
		sql.append("       YEARLY.DEALER_ID\n");  
		sql.append("  FROM (SELECT TVMF.DEALER_ID,\n"); 
		sql.append("               TD.DEALER_CODE,\n");  
		sql.append("               TD.DEALER_SHORTNAME,\n");
		sql.append("               tvmg1.GROUP_ID,\n");  
		sql.append("               nvl(SUM(TVMFD.FORECAST_AMOUNT),0) AS FORE_AMOUNT\n");  
		sql.append("          FROM tt_vs_monthly_forecast    tvmf,\n");  
		sql.append("               tt_vs_monthly_forecast_detail  tvmfd,\n");  
		sql.append("               TM_DEALER                TD,\n"); 
		sql.append("				tm_vhcl_material_group tvmg1,\n");
		sql.append("				tm_vhcl_material_group tvmg2,\n");  
		sql.append("				tm_vhcl_material_group tvmg3\n");

		
		sql.append("         WHERE TVMF.FORECAST_ID = TVMFD.FORECAST_ID\n");  
		sql.append("           AND TVMF.DEALER_ID = TD.DEALER_ID\n");
		sql.append("			and tvmfd.group_id = tvmg3.group_id\n");
		sql.append("			and tvmg3.parent_group_id = tvmg2.group_id\n");  
		sql.append("			and tvmg2.parent_group_id = tvmg1.group_id\n");

//		sql.append("           AND TVMF.AREA_ID="+areaId+"\n");zxf
		sql.append("           AND TVMF.COMPANY_ID="+companyId+"\n");
		sql.append("           AND TVMF.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append("       and tvmg3.group_Code in ("+PlanUtil.createSqlStr(groupCode)+") \n");
		}
		if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
			sql.append("          and TVMF.dealer_id in\n" );
			sql.append("       (select m.dealer_id\n" );
			sql.append("          from tm_dealer m\n" );
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n" );
			sql.append("         START WITH m.status="+Constant.STATUS_ENABLE+" and m.dealer_id in\n" );
			sql.append("                    (select rel.dealer_id\n" );
			sql.append("                       from vw_org_dealer rel\n" );
			sql.append("                      where rel.ROOT_ORG_ID ="+logonOrgId+"))");
		}
		
		if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"SMALLREGION".equals(logonOrgType)){
			sql.append("          and TVMF.dealer_id in\n" );
			sql.append("       (select m.dealer_id\n" );
			sql.append("          from tm_dealer m\n" );
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n" );
			sql.append("         START WITH m.status="+Constant.STATUS_ENABLE+" and m.dealer_id in\n" );
			sql.append("                    (select rel.dealer_id\n" );
			sql.append("                       from vw_org_dealer rel\n" );
			sql.append("                      where rel.PQ_ORG_ID ="+logonOrgId+"))");
		}
		sql.append("           and TVMF.FORECAST_YEAR = "+forecast_year+"\n");  
		sql.append("           and TVMF.FORECAST_MONTH = "+forecast_month+"\n");  
		sql.append("           and TVMF.FORECAST_TYPE = "+forecast_type+"\n");	//zxf	
		sql.append("           and TVMF.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
		
		if(null!=dealerCode&&!"".equals(dealerCode)){
			sql.append("           and td.dealer_code in ("+PlanUtil.createSqlStr(dealerCode)+")\n");  
		}
		
		sql.append("         GROUP BY TVMF.DEALER_ID,\n");  
		sql.append("                  tvmg1.GROUP_ID,\n");  
		sql.append("                  TD.DEALER_CODE,\n");  
		sql.append("                  TD.DEALER_SHORTNAME) YEARLY\n"); 
		sql.append(" GROUP BY DEALER_ID, YEARLY.DEALER_CODE, YEARLY.DEALER_SHORTNAME \n");  
		sql.append(" order by dealer_id");

		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	public PageResult<Map<String, Object>> oemSelectOrgForecastTotal(Map<String, Object> map,List<Map<String, Object>> serlist,int pageSize,int curPage){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String forecast_year=(String)map.get("forecast_year");
		String forecast_month=(String)map.get("forecast_month");
		String forecast_type=(String)map.get("forecast_type");
		String orgCode=(String)map.get("orgCode");
		String logonOrgType=(String)map.get("logonOrgType");
		String logonOrgId=(String)map.get("logonOrgId");
		String companyId=map.get("companyId").toString();
		//String areaId=map.get("areaId").toString(); //zxf
		String groupCode=(String)map.get("groupCode");
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer();

		sql.append("SELECT YEARLY.ORG_ID,\n");
		sql.append("       YEARLY.ORG_CODE,\n");  
		sql.append("       YEARLY.ORG_NAME,\n");  
		for(int i=0;i<serlist.size();i++){
			Map<String, Object> sermap=serlist.get(i);
			sql.append("       nvl(MAX(DECODE(YEARLY.GROUP_ID,"+sermap.get("GROUP_ID")+",YEARLY.FORE_AMOUNT)),0) S"+i+",\n");  
		}
		sql.append("       nvl(sum(yearly.FORE_AMOUNT), 0) FA \n");  
		sql.append("  FROM (SELECT TOR.ORG_ID,\n");  
		sql.append("               TOR.ORG_CODE,\n");  
		sql.append("               TOR.ORG_NAME,\n"); 
		sql.append("               tvmg1.GROUP_ID,\n");  
		sql.append("               nvl(SUM(tvmfd.forecast_AMOUNT), 0) AS FORE_AMOUNT\n");  
		sql.append("          FROM tt_vs_monthly_forecast    tvmf,\n");  
		sql.append("               tt_vs_monthly_forecast_detail tvmfd,\n");   
		sql.append("               TM_ORG                    TOR,\n");  
		sql.append("		   tm_vhcl_material_group        tvmg1,\n");
		sql.append("		   tm_vhcl_material_group        tvmg2,\n");  
		sql.append("		   tm_vhcl_material_group        tvmg3\n");
		sql.append("         WHERE tvmf.forecast_ID = tvmfd.forecast_ID\n");  
		sql.append("		and tvmfd.group_id = tvmg3.group_id\n");
		sql.append("		and tvmg3.parent_group_id = tvmg2.group_id\n");  
		sql.append("		and tvmg2.parent_group_id = tvmg1.group_id\n");
		sql.append("		and tvmf.org_id = tor.org_id\n");  
		sql.append("           AND tvmf.COMPANY_ID = "+companyId+"\n");  
		//sql.append("           AND tvmf.AREA_ID = "+areaId+"\n");  //zxf
		sql.append("           AND tvmf.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
		if(map.get("dutyType").equals("10431001")){
			if(null!=orgCode&&!"".equals(orgCode)){
				sql.append("       and tor.org_code in ("+PlanUtil.createSqlStr(orgCode)+")");
			}
			
		}
		if(map.get("dutyType").equals("10431003")){
			if(null!=orgCode&&!"".equals(orgCode)){
				sql.append("       and tor.org_code in (select root_org_code from vw_org_dealer_all_new where root_org_code in ("+PlanUtil.createSqlStr(orgCode)+") and root_org_id="+logonUser.getOrgId()+" )");
			}else{
				sql.append("       and tvmf.org_id="+logonUser.getOrgId());
			}
		
		}
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append("       and tvmg3.group_Code in ("+PlanUtil.createSqlStr(groupCode)+") \n");
		}
		sql.append("           and TVMF.FORECAST_YEAR = "+forecast_year+"\n");  
		sql.append("           and TVMF.FORECAST_MONTH = "+forecast_month+"\n");  
		sql.append("           and TVMF.forecast_type = "+forecast_type+"\n");
		sql.append("           and TVMF.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n"); 
		
		sql.append("         GROUP BY tvmg1.GROUP_ID,\n");  
		sql.append("                  TOR.ORG_ID,\n");  
		sql.append("                  TOR.ORG_CODE,\n");  
		sql.append("                  TOR.ORG_NAME) YEARLY\n");  
		sql.append(" GROUP BY YEARLY.ORG_ID, YEARLY.ORG_CODE, YEARLY.ORG_NAME \n");  
		sql.append(" order by YEARLY.ORG_ID");

		/*return factory.pageQuery(sql.toString(), null,new DAOCallback<Map<String, Object>>() {
			public Map<String, Object> wrapper(ResultSet rs, int i) {
				Map<String,Object> map = new HashMap<String,Object>() ;

				try {
				map.put("ORG_ID", rs.getInt("ORG_ID")) ;
				System.out.println("+++++++++"+rs.getString("ORG_NAME"));
				map.put("ORG_CODE", rs.getString("ORG_CODE")) ;
				map.put("ORG_NAME", rs.getString("ORG_NAME")) ;
				map.put("S0", rs.getInt("S0")) ;
				map.put("S1", rs.getInt("S1")) ;
				map.put("S2", rs.getInt("S2")) ;
				map.put("S3", rs.getInt("S3")) ;
				map.put("SS", rs.getInt("SS")) ;
			} catch(Exception e) {
			}

			return map ;
			}
		}
, pageSize, curPage) ;*/
		return dao.pageQuery(sql.toString(), null, getFunName()+serlist.size(), pageSize, curPage);
	}
	
	// 区域汇总查询下载
	public List<Map<String, Object>> SelectOrgForecastTotal(Map<String, Object> map,List<Map<String, Object>> serlist){
		String forecast_year=(String)map.get("plan_year");
		String forecast_month=(String)map.get("plan_month");
		String orgCode=(String)map.get("orgCode");
		String logonOrgType=(String)map.get("logonOrgType");
		String logonOrgId=(String)map.get("logonOrgId");
		String companyId=map.get("companyId").toString();
		//String areaId=map.get("areaId").toString();
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer();

		sql.append("SELECT YEARLY.ORG_ID,\n");
		sql.append("       YEARLY.ORG_CODE,\n");  
		sql.append("       YEARLY.ORG_NAME,\n");  
		for(int i=0;i<serlist.size();i++){
			Map<String, Object> sermap=serlist.get(i);
			sql.append("       nvl(MAX(DECODE(YEARLY.GROUP_ID,"+sermap.get("GROUP_ID")+",YEARLY.FORE_AMOUNT)),0) S"+i+",\n");  
		}
		sql.append("       nvl(sum(yearly.FORE_AMOUNT), 0) FA \n");  
		sql.append("  FROM (SELECT TOR.ORG_ID,\n");  
		sql.append("               TOR.ORG_CODE,\n");  
		sql.append("               TOR.ORG_NAME,\n"); 
		sql.append("               tvmg1.GROUP_ID,\n");  
		sql.append("               nvl(SUM(tvmfd.forecast_AMOUNT), 0) AS FORE_AMOUNT\n");  
		sql.append("          FROM tt_vs_monthly_forecast    tvmf,\n");  
		sql.append("               tt_vs_monthly_forecast_detail tvmfd,\n");   
		sql.append("               TM_ORG                    TOR,\n");  
		sql.append("		   tm_vhcl_material_group        tvmg1,\n");
		sql.append("		   tm_vhcl_material_group        tvmg2,\n");  
		sql.append("		   tm_vhcl_material_group        tvmg3\n");
		sql.append("         WHERE tvmf.forecast_ID = tvmfd.forecast_ID\n");  
		sql.append("		and tvmfd.group_id = tvmg3.group_id\n");
		sql.append("		and tvmg3.parent_group_id = tvmg2.group_id\n");  
		sql.append("		and tvmg2.parent_group_id = tvmg1.group_id\n");
		sql.append("		and tvmf.org_id = tor.org_id\n");  
		sql.append("           AND tvmf.COMPANY_ID = "+companyId+"\n");  
		//sql.append("           AND tvmf.AREA_ID = "+areaId+"\n");  
		sql.append("           AND tvmf.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");  
		if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
			sql.append("          and tvmf.org_id in\n" );
			sql.append("       (select m.org_id\n" );
			sql.append("          from tm_org m\n" );
			sql.append("        CONNECT BY PRIOR m.org_id = m.parent_org_id\n" );
			sql.append("         START WITH m.org_id ="+logonOrgId+")\n" );
		}
		//小区
		if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"SMALLREGION".equals(logonOrgType)){
			sql.append("          and TVMF.dealer_id in\n" );
			sql.append("       (select m.dealer_id\n" );
			sql.append("          from tm_dealer m\n" );
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n" );
			sql.append("         START WITH m.status="+Constant.STATUS_ENABLE+" and m.dealer_id in\n" );
			sql.append("                    (select rel.dealer_id\n" );
			sql.append("                       from vw_org_dealer rel\n" );
			sql.append("                      where rel.PQ_ORG_ID ="+logonOrgId+"))");
		}
		sql.append("           and TVMF.FORECAST_YEAR = "+forecast_year+"\n");  
		sql.append("           and TVMF.FORECAST_MONTH = "+forecast_month+"\n");  
		sql.append("           and TVMF.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n"); 
		if(null!=orgCode&&!"".equals(orgCode)){
			sql.append("       and tor.org_code in ("+PlanUtil.createSqlStr(orgCode)+")");
		}
		sql.append("         GROUP BY tvmg1.GROUP_ID,\n");  
		sql.append("                  TOR.ORG_ID,\n");  
		sql.append("                  TOR.ORG_CODE,\n");  
		sql.append("                  TOR.ORG_NAME) YEARLY\n");  
		sql.append(" GROUP BY YEARLY.ORG_ID, YEARLY.ORG_CODE, YEARLY.ORG_NAME \n");  
		sql.append(" order by YEARLY.ORG_ID");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	//经销商汇总查询下载
	// 车厂汇总
	public List<Map<String, Object>> SelectDLRForecastTotal(Map<String, Object> map,List<Map<String, Object>> serlist){
		String forecast_year=(String)map.get("plan_year");
		String forecast_month=(String)map.get("plan_month");
		String dealerCode=(String)map.get("dealerCode");
		String logonOrgType=(String)map.get("logonOrgType");
		String logonOrgId=(String)map.get("logonOrgId");
		String companyId=map.get("companyId").toString();
		//String areaId=map.get("areaId").toString();
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer();

		sql.append("\n");
		sql.append("SELECT YEARLY.DEALER_CODE,\n");  
		sql.append("       YEARLY.DEALER_SHORTNAME,\n");
		
		for(int i=0;i<serlist.size();i++){
			Map<String, Object> sermap=serlist.get(i);
			sql.append("       nvl(MAX(DECODE(YEARLY.GROUP_ID,"+sermap.get("GROUP_ID")+",YEARLY.FORE_AMOUNT)),0) S"+i+",\n");  
		}
		sql.append("       nvl(sum(YEARLY.FORE_AMOUNT),0) FA,\n");
		sql.append("       YEARLY.DEALER_ID\n");  
		sql.append("  FROM (SELECT TVMF.DEALER_ID,\n"); 
		sql.append("               TD.DEALER_CODE,\n");  
		sql.append("               TD.DEALER_SHORTNAME,\n");
		sql.append("               tvmg1.GROUP_ID,\n");  
		sql.append("               nvl(SUM(TVMFD.FORECAST_AMOUNT),0) AS FORE_AMOUNT\n");  
		sql.append("          FROM tt_vs_monthly_forecast    tvmf,\n");  
		sql.append("               tt_vs_monthly_forecast_detail  tvmfd,\n");  
		sql.append("               TM_DEALER                TD,\n"); 
		sql.append("				tm_vhcl_material_group tvmg1,\n");
		sql.append("				tm_vhcl_material_group tvmg2,\n");  
		sql.append("				tm_vhcl_material_group tvmg3\n");

		
		sql.append("         WHERE TVMF.FORECAST_ID = TVMFD.FORECAST_ID\n");  
		sql.append("           AND TVMF.DEALER_ID = TD.DEALER_ID\n");
		sql.append("			and tvmfd.group_id = tvmg3.group_id\n");
		sql.append("			and tvmg3.parent_group_id = tvmg2.group_id\n");  
		sql.append("			and tvmg2.parent_group_id = tvmg1.group_id\n");

		//sql.append("           AND TVMF.AREA_ID="+areaId+"\n");
		sql.append("           AND TVMF.COMPANY_ID="+companyId+"\n");
		sql.append("           AND TVMF.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");
		
		if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"LARGEREGION".equals(logonOrgType)){
			sql.append("          and TVMF.dealer_id in\n" );
			sql.append("       (select m.dealer_id\n" );
			sql.append("          from tm_dealer m\n" );
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n" );
			sql.append("         START WITH m.status="+Constant.STATUS_ENABLE+" and m.dealer_id in\n" );
			/* 长安原程序代码：
			sql.append("                    (select rel.dealer_id\n" );
			sql.append("                       from tm_dealer_org_relation rel\n" );
			sql.append("                      where rel.org_id ="+logonOrgId+"))");
			*/
			//铃木修改程序代码：begin   2012-11-23
			sql.append("                    (select rel.dealer_id\n" );
			sql.append("                       from vw_org_dealer rel\n" );
			sql.append("                      where rel.ROOT_ORG_ID ="+logonOrgId+"))");
			//铃木修改程序代码：end
		}
		//小区
		if(null!=logonOrgId&&null!=logonOrgType&&!"".equals(logonOrgId)&&"SMALLREGION".equals(logonOrgType)){
			sql.append("          and TVMF.dealer_id in\n" );
			sql.append("       (select m.dealer_id\n" );
			sql.append("          from tm_dealer m\n" );
			sql.append("        CONNECT BY PRIOR m.dealer_id = m.parent_dealer_d\n" );
			sql.append("         START WITH m.status="+Constant.STATUS_ENABLE+" and m.dealer_id in\n" );
			sql.append("                    (select rel.dealer_id\n" );
			sql.append("                       from vw_org_dealer rel\n" );
			sql.append("                      where rel.PQ_ORG_ID ="+logonOrgId+"))");
		}
		sql.append("           and TVMF.FORECAST_YEAR = "+forecast_year+"\n");  
		sql.append("           and TVMF.FORECAST_MONTH = "+forecast_month+"\n");  
		sql.append("           and TVMF.STATUS = "+Constant.FORECAST_STATUS_CONFIRM+"\n");  
		
		if(null!=dealerCode&&!"".equals(dealerCode)){
			sql.append("           and td.dealer_code in ("+PlanUtil.createSqlStr(dealerCode)+")\n");  
		}
		
		sql.append("         GROUP BY TVMF.DEALER_ID,\n");  
		sql.append("                  tvmg1.GROUP_ID,\n");  
		sql.append("                  TD.DEALER_CODE,\n");  
		sql.append("                  TD.DEALER_SHORTNAME) YEARLY\n"); 
		sql.append(" GROUP BY DEALER_ID, YEARLY.DEALER_CODE, YEARLY.DEALER_SHORTNAME \n");  
		sql.append(" order by dealer_id");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	public int OemRebutOrg(String ordIds,String companyId,List<Map<String, Object>> cList){
		 StringBuffer sql= new StringBuffer();
		 sql.append("update  TT_VS_MONTHLY_FORECAST f set f.status = "+Constant.FORECAST_STATUS_UNCONFIRM +",f.sub_status = "+Constant.FORECAST_SUBSTATUS_4+" \n" ); //按大区驳回,总部已驳回，需重新上报
		 sql.append("  where f.COMPANY_ID="+companyId+"\n" );
		 sql.append("  and f.STATUS = "+ Constant.FORECAST_STATUS_CONFIRM + "\n" );
		 sql.append("  and f.ORG_TYPE = "+ Constant.ORG_TYPE_OEM + "\n" );
		 sql.append("  and f.ORG_ID in "+ordIds+" \n");
		// sql.append("  and f.area_id = "+areaId+" \n");//zxf
		 String month = PlanUtil.getRadomDate(1,"");//zxf z		
		 if(cList!=null&&cList.size()>0){//zxf
			 String year="";
			 int size = cList.size();
			 for(int i=0;i<size;i++){
				 Map<String, Object> map = cList.get(i);				 
				 if(i==size-1){
					year += (String) map.get("YEAR");						 
				 }
			     else{
			    	 year += (String) map.get("YEAR")+",";
				 }				 
			 }
			 sql.append("  and f.forecast_year in ("+year+") \n");
			 sql.append("  and f.forecast_id in \n");
			 
			 sql.append("  (select t.forecast_id from TT_VS_MONTHLY_FORECAST t where \n");
			 
			 for(int i=0;i<size;i++){
				 Map<String, Object> map = cList.get(i);				 
				 if(month.equals(map.get("MONTH"))){
					 sql.append("   (t.forecast_month="+map.get("MONTH") +"and t.forecast_type = 60591001) or\n");
				 }
				 else{
					 sql.append("    (t.forecast_month="+map.get("MONTH") +"and t.forecast_type = 60591002) )\n");
				 }
			 }
		 }
		 		 
		int i= dao.update(sql.toString(), null);		
		return i;
	}
	public int OrgRebutDealer(String dealers,String companyId,List<Map<String, Object>> cList){
		 StringBuffer sql= new StringBuffer();
		 sql.append("update  TT_VS_MONTHLY_FORECAST f set f.status = "+Constant.FORECAST_STATUS_UNCONFIRM+",f.sub_status = "+Constant.FORECAST_SUBSTATUS_3+" \n" ); //按经销商驳回,大区已驳回，需重新上报 
		 sql.append("  where f.COMPANY_ID="+companyId+"\n" );
		 sql.append("  and f.STATUS = "+ Constant.FORECAST_STATUS_CONFIRM + "\n" );
		 sql.append("  and f.ORG_TYPE = "+ Constant.ORG_TYPE_DEALER + "\n" );
		 sql.append("  and f.dealer_id in "+dealers+" \n");	
		 //sql.append("  and f.area_id = "+areaId+" \n");//zxf
				 
		 String month = PlanUtil.getRadomDate(1,"");//zxf z		
		 if(cList!=null&&cList.size()>0){//zxf
			 String year="";
			 int size = cList.size();
			 for(int i=0;i<size;i++){
				 Map<String, Object> map = cList.get(i);				 
				 if(i==size-1){
					year += (String) map.get("YEAR");						 
				 }
			     else{
			    	 year += (String) map.get("YEAR")+",";
				 }				 
			 }
			 sql.append("  and f.forecast_year in ("+year+") \n");
			 sql.append("  and f.forecast_id in \n");
			 
			 sql.append("  (select t.forecast_id from TT_VS_MONTHLY_FORECAST t where \n");
			 
			 for(int i=0;i<size;i++){
				 Map<String, Object> map = cList.get(i);				 
				 if(month.equals(map.get("MONTH"))){
					 sql.append("   (t.forecast_month="+map.get("MONTH") +"and t.forecast_type = 60591001) or\n");
				 }
				 else{
					 sql.append("    (t.forecast_month="+map.get("MONTH") +"and t.forecast_type = 60591002) )\n");
				 }
			 }
		 }
		 
//		 sql.append("  and f.forecast_year = "+year+" \n");
//		 sql.append("  and f.forecast_month = "+month+" \n");
		int i = dao.update(sql.toString(), null);		
		return i;
	}
	/**
	 * 上报前校验数据的是否已经调整过
	 */
	public List<Map<String, Object>> checkDataUnAdjust(int year,int month,String orgId,String[] groupCodes,String[] underNumber) throws Exception{
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> returnMap=new HashMap<String,Object>();
		String[] returnCodes=new String[groupCodes.length];
		//还一次也没有做过调整
		if(checkMainTable(year,month,orgId)==0){
			returnMap.put("count", 1);
			for(int i=0;i<groupCodes.length;i++){
				if(groupCodes[i]!=null&&0!=Integer.parseInt(underNumber[i])){
					returnCodes[i]=groupCodes[i];
				}
			}
			returnMap.put("dataSet",returnCodes);
		}else{
			
			boolean flag=false;
			for(int i=0;i<groupCodes.length;i++){
				if(groupCodes[i]!=null){
					int count=checkDetailTable(year,month,orgId,groupCodes[i]);
					//如果没有调整并且下级的数量不是0
					if(count==0&&0!=Integer.parseInt(underNumber[i])){
						flag=true;
						returnCodes[i]=groupCodes[i];
					}
				}
			}
			//明细表中有的没有调整
			 if(flag){
				 returnMap.put("count", 2);
				 returnMap.put("dataSet", returnCodes);
			 }else{
				 //所有的调整完成
				 returnMap.put("count", 0);
			 }
		}
		
		list.add(returnMap);
		return list;
	}
	/**
	 * 检测主表中是否有记录
	 * @return
	 */
	public int checkMainTable(int year,int month,String orgId) throws Exception{

		StringBuffer sql=new StringBuffer();
		
		sql.append("select count(*) as total \n");
		sql.append(" from tt_vs_monthly_forecast tvmf\n");
		sql.append("where tvmf.forecast_year = "+year+"\n");
		sql.append("and tvmf.forecast_month = "+month+"\n");
		sql.append(" and tvmf.org_id = "+orgId+"");
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, getFunName());
		String count=list.get(0).get("TOTAL").toString();
		int returnCount=0;
		returnCount=Integer.parseInt(count);
		return returnCount;
	}
	/**
	 * 检查明细表中是否有记录
	 * @param year
	 * @param month
	 * @param orgId
	 * @param groupCode
	 * @return
	 */
	public int checkDetailTable(int year,int month,String orgId,String groupCode)throws Exception{
		
		StringBuffer sql=new StringBuffer();
		
		sql.append("select count(1) as total\n");
		sql.append("from tt_vs_monthly_forecast_detail tvmfd\n");
		sql.append("where tvmfd.forecast_id =\n");
		sql.append(" (select tvmf.forecast_id\n");
		sql.append("  from tt_vs_monthly_forecast tvmf\n");
		sql.append(" where tvmf.forecast_year = "+year+"\n");
		sql.append(" and tvmf.forecast_month = "+month+"\n");
		sql.append(" and tvmf.forecast_type = 60591001\n");//zxf
		sql.append(" and tvmf.org_id = "+orgId+")\n");
		sql.append(" and tvmfd.group_id in\n");
		sql.append(" (select group_id\n");
		sql.append("  from tm_vhcl_material_group tvmg\n");
		sql.append(" where tvmg.parent_group_id in\n");
		sql.append(" (select group_id\n");
		sql.append("  from tm_vhcl_material_group\n");
		sql.append("  where group_code = '"+groupCode+"'\n");
		sql.append(" and group_level = 3))");
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, getFunName());
		String count=list.get(0).get("TOTAL").toString();
		int returnCount=0;
		returnCount=Integer.parseInt(count);
		return returnCount;
	}
	
	
	public PageResult<Map<String, Object>> OrgRequireFindAllDao(String orgCode, String orgName,String companyId,int pageSize,int curPage){
		StringBuffer sql=new StringBuffer();		
//		sql.append("select * from TM_ORG t where t.duty_type= 10431003\n");	
//		if(orgCode!=null&&orgCode!=""){
//			sql.append(" and  t.org_code like \'%"+orgCode+"%\'" );
//		}
//		if(orgName!=null&&orgName!=""){
//			sql.append(" and  t.org_name like \'%"+orgName+"%\'" );
//		}
//		sql.append(" order by t.org_name");
		sql.append("select * from ( \n");
		//sql.append("select t.org_id,t.org_code,t.org_name from TM_ORG t where t.duty_type = 10431001 and t.company_id = "+companyId+"\n" );
		//sql.append(" union \n");
		sql.append("select  t.org_id,t.org_code,t.org_name  from( \n");
		sql.append("select * from TM_ORG j where j.duty_type= 10431003 \n");
		sql.append("order by j.org_name \n");
		sql.append(") t \n");
		sql.append(") d \n");
		if(orgCode!=null&&orgCode!=""){
			sql.append(" where  d.org_code like \'%"+orgCode+"%\'" );
		}
		if(orgName!=null&&orgName!=""){
			if(orgCode!=null&&orgCode!=""){
				sql.append(" and  d.org_name like \'%"+orgName+"%\'" );
			}
			else{
				sql.append(" where   d.org_name like \'%"+orgName+"%\'" );
			}
		}
		
//		sql.append("select j.org_id,j.org_code,j.org_name  from TM_ORG j where j.duty_type= 10431003 \n");
//		if(orgCode!=null&&orgCode!=""){
//			sql.append(" and  j.org_code like \'%"+orgCode+"%\'" );
//		}
//		
//		if(orgName!=null&&orgName!=""){
//			sql.append(" and  j.org_name like \'%"+orgName+"%\'" );
//		}
//		sql.append(" order by j.org_name \n");
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	
	public PageResult<Map<String, Object>> OrgRequireFindAllNoOwnDao(String orgCode, String orgName,String companyId,int pageSize,int curPage){
		StringBuffer sql=new StringBuffer();				
		sql.append("select j.org_id,j.org_code,j.org_name  from TM_ORG j where j.duty_type= 10431003 \n");
		if(orgCode!=null&&orgCode!=""){
			sql.append(" and  j.org_code like \'%"+orgCode+"%\'" );
		}
		
		if(orgName!=null&&orgName!=""){
			sql.append(" and  j.org_name like \'%"+orgName+"%\'" );
		}
		sql.append(" order by j.org_name \n");
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	
}
