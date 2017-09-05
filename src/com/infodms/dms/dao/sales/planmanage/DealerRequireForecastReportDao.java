package com.infodms.dms.dao.sales.planmanage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;





import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DealerRequireForecastReportDao extends BaseDao<PO> {
	
	public static Logger logger = Logger.getLogger(YearPlanDao.class);
	private static final DealerRequireForecastReportDao dao = new DealerRequireForecastReportDao ();
	public static final DealerRequireForecastReportDao getInstance() {
		return dao;
	}
	
	public PageResult<Map<String, Object>> selectRequireForecastModelInfo(String dealerId,List<Map<String, Object>> mapList,String companyId,int curPage, int pageSize){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");


		sql.append("select t.SERIES_CODE,\n");
		sql.append("       t.SERIES_NAME,\n");  
		sql.append("       t.MODEL_CODE,\n");  
		 for(int i=0;i<mapList.size();i++){
			 sql.append("sum(s"+i+") s"+i+",\n");
		 }
		sql.append("       GROUP_ID,\n");  
		sql.append("       t.MODEL_NAME\n");  
		sql.append("  from (");

		sql.append("select x.SERIES_CODE,\n");
		sql.append("       x.SERIES_NAME,\n");  
		sql.append("       x.MODEL_CODE,\n");  
        for(int i=0;i<mapList.size();i++){
			Map<String, Object> map=mapList.get(i);
			String month = PlanUtil.getRadomDate(1,"");
			sql.append("       case\n");  
			if(month.equals(map.get("MONTH")+"")){ //zxf
				sql.append("         when x.FORECAST_YEAR = "+map.get("YEAR")+" and x.FORECAST_MONTH = "+map.get("MONTH")+"and  x.forecast_type = 60591001 then\n");
			}
			else{
				sql.append("         when x.FORECAST_YEAR = "+map.get("YEAR")+" and x.FORECAST_MONTH = "+map.get("MONTH")+" then\n");  
			}
			sql.append("          x.AMOUNT\n");  
			sql.append("         else\n");  
			sql.append("          0\n");  
			sql.append("       end S"+i+",\n");  
		}
        sql.append("       x.GROUP_ID ||',"+dealerId+"'||',' GROUP_ID,\n");//zxf
		sql.append("       x.MODEL_NAME\n");  
		sql.append("  from (select g.SERIES_CODE,\n");  
		sql.append("               g.SERIES_NAME,\n");  
		sql.append("               g.MODEL_CODE,\n"); 
		sql.append("               g.GROUP_ID,\n");
		sql.append("               g.MODEL_NAME,\n");  
		sql.append("               a.FORECAST_YEAR,\n");  
		sql.append("               a.FORECAST_MONTH,\n");  
		sql.append("               a.FORECAST_TYPE,\n");//zxf
		sql.append("               nvl(sum(a.FORECAST_AMOUNT), 0) amount\n");  
		sql.append("          from (select f.FORECAST_YEAR,\n");  
		sql.append("                       f.FORECAST_MONTH,\n");  
		sql.append("                       f.FORECAST_TYPE,\n");  //zxf
		sql.append("                       f.FORECAST_ID,\n");  
		sql.append("                       d.GROUP_ID,\n");  
		sql.append("                       d.FORECAST_AMOUNT\n");  
		sql.append("                  from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                       TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                 where f.FORECAST_ID = d.FORECAST_ID\n"); 
		sql.append("                   and f.COMPANY_ID="+companyId+"\n");
		//sql.append("                   and f.STATUS = "+Constant.FORECAST_STATUS_UNCONFIRM+"\n"); 
		sql.append("                   and f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n"); 
		sql.append("                   and f.DEALER_ID in ("+dealerId+")) a,\n");  //zxf
		//params.add(dealerId);
		
		sql.append("(select /*+ all_rows*/\n");
		sql.append(" g2.GROUP_ID,\n");  
		sql.append(" g1.GROUP_CODE,\n");  
		sql.append(" g1.GROUP_ID   GROUP_ID1,\n");  
		sql.append(" g3.GROUP_CODE SERIES_CODE,\n");  
		sql.append(" g3.GROUP_NAME SERIES_NAME,\n");  
		sql.append(" g2.GROUP_CODE MODEL_CODE,\n");  
		sql.append(" g2.GROUP_NAME MODEL_NAME\n");  
		sql.append("  from TM_VHCL_MATERIAL_GROUP g1,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP g2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP g3\n");  
		sql.append(" where g1.PARENT_GROUP_ID = g2.GROUP_ID\n");  
		sql.append("   and g2.PARENT_GROUP_ID = g3.GROUP_ID\n");  
		sql.append("   and g1.GROUP_LEVEL = 4\n");  
		sql.append("   and g1.forcast_flag = 1\n");  
		sql.append("   and g2.forcast_flag = 1\n");  
		sql.append("   and g3.forcast_flag = 1\n");  
		sql.append("   and g1.status = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   and (exists (SELECT TAP.MATERIAL_GROUP_ID\n");  
		sql.append("                  FROM tm_area_group tap\n");  
		//sql.append("                 where tap.area_id = ").append(areaId).append("\n");  //zxf
		sql.append("                   where tap.material_group_id = g1.group_id) or exists\n");  
		sql.append("        (SELECT TAP.MATERIAL_GROUP_ID\n");  
		sql.append("           FROM tm_area_group tap\n");  
		//sql.append("          where tap.area_id = ").append(areaId).append("\n");   //zxf
		sql.append("            where tap.material_group_id = g2.group_id) or exists\n");  
		sql.append("        (SELECT TAP.MATERIAL_GROUP_ID\n");  
		sql.append("           FROM tm_area_group tap\n");  
		//sql.append("          where tap.area_id = ").append(areaId).append("\n");   //zxf
		sql.append("            where tap.material_group_id = g3.group_id))) g\n");


		/*sql.append("               (select g2.GROUP_ID,\n");  
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
		sql.append("                   and g1.GROUP_LEVEL = 4\n");  
		sql.append("                   and g1.forcast_flag = 1\n");  
		sql.append("                   and g2.forcast_flag = 1\n");  
		sql.append("                   and g3.forcast_flag = 1\n");  
		sql.append("                   and g1.GROUP_ID in\n");  
		sql.append("                       (select T1.GROUP_ID\n");  
		sql.append("                          from tm_vhcl_material_group t1\n");  
		sql.append("                         WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("                           and t1.GROUP_LEVEL = 4\n");  
		sql.append("                         start with t1.group_id IN\n");  
		sql.append("                                    (SELECT TAP.MATERIAL_GROUP_ID\n");  
		sql.append("                                       FROM tm_area_group tap\n");  
		sql.append("                                      where tap.area_id = "+areaId+")\n"); 
		//params.add(areaId);
		sql.append("                        connect by prior t1.group_id = t1.parent_group_id)) g\n");  */
		sql.append("         where a.GROUP_ID(+) = g.GROUP_ID1\n");  
		sql.append("         group by g.SERIES_CODE,\n");  
		sql.append("                  g.SERIES_NAME,\n");  
		sql.append("                  g.MODEL_CODE,\n");  
		sql.append("                  g.GROUP_ID,\n");
		sql.append("                  g.MODEL_NAME,\n");  
		sql.append("                  a.FORECAST_YEAR,\n");  
		sql.append("                  a.FORECAST_MONTH,a.FORECAST_TYPE) X\n");  
	
		sql.append("        ) t\n");
		sql.append("group by t.SERIES_CODE,\n");  
		sql.append("         t.SERIES_NAME,\n");  
		sql.append("         t.MODEL_CODE,\n");  
		sql.append("         GROUP_ID,\n");  
		sql.append("         t.MODEL_NAME");
		sql.append(" order by t.SERIES_CODE ");
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
/*
 * 经销可预测操作的数据
 * 查询 配置ID NAME 月预测数量
 */
	public List<Map<String, Object>> selectForecastOpeList(String pgroupId,String dealerId,List<Map<String, Object>> mapList,String companyId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");


		sql.append("select t.group_id GROUP_ID,\n");
		sql.append("       t.group_name,\n");  
		for(int i=0;i<mapList.size();i++){
			sql.append("       sum(d"+i+") d"+i+",\n"); 
		}
		sql.append("       t.group_code\n");

		sql.append(" from (\n");

		sql.append("select aa.group_id GROUP_ID,\n");
		sql.append("       aa.group_name,\n");  
		for(int i=0;i<mapList.size();i++){
		    Map<String, Object> map=mapList.get(i);
			sql.append("       case\n"); 
			String month = PlanUtil.getRadomDate(1,"");
			if(month.equals(map.get("MONTH"))){//zxf
				sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+"and aa.forecast_type = 60591001"+" then\n");
			}
			else{
				sql.append("         when aa.FORECAST_YEAR = "+map.get("YEAR")+" and aa.FORECAST_MONTH = "+map.get("MONTH")+"and aa.forecast_type = 60591002"+ " then\n");  
			}
			sql.append("          aa.AMOUNT\n");  
			sql.append("         else\n");  
			sql.append("          0\n");  
			sql.append("       end D"+i+",\n");  
		}
		  
		sql.append("       aa.group_code\n");  
		sql.append("  from (\n");  
		sql.append("        select b.group_id,\n");  
		sql.append("                b.group_code,\n");  
		sql.append("                b.group_name,\n");  
		sql.append("                nvl(a.FORECAST_AMOUNT, 0) amount,\n");  
		sql.append("                a.FORECAST_YEAR,\n");  
		sql.append("                a.FORECAST_MONTH,\n");  
		sql.append("                a.FORECAST_TYPE\n");//zxf
		sql.append("          from (select d.FORECAST_AMOUNT,\n");  
		sql.append("                        d.GROUP_ID,\n");  
		sql.append("                        f.FORECAST_YEAR,\n");  
		sql.append("                        f.FORECAST_MONTH,\n"); 
		sql.append("                        f.FORECAST_TYPE\n"); //zxf
		sql.append("                   from TT_VS_MONTHLY_FORECAST        f,\n");  
		sql.append("                        TT_VS_MONTHLY_FORECAST_DETAIL d\n");  
		sql.append("                  where f.FORECAST_ID = d.FORECAST_ID\n");  
		sql.append("		            and f.COMPANY_ID="+companyId+"\n");
		sql.append("                    and f.DEALER_ID = "+dealerId+"\n");  
		sql.append("                    /*and f.STATUS = "+Constant.FORECAST_STATUS_UNCONFIRM+"*/)a,\n");  
		sql.append("                (select T1.GROUP_ID, t1.GROUP_CODE, t1.group_name\n");  
		sql.append("                   from tm_vhcl_material_group t1\n");  
		sql.append("                  WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("                    and t1.GROUP_LEVEL = 4\n");  
		sql.append("                    and t1.forcast_flag = 1\n");  
		sql.append("                    and t1.PARENT_GROUP_ID = "+pgroupId+"\n");  
  
		//sql.append("                 connect by prior t1.group_id = t1.parent_group_id) b\n");//zxf
		sql.append("                 ) b\n");//zxf
		sql.append("         where a.group_id(+) = b.group_id) aa\n");
		

		sql.append("       ) t\n");
		sql.append("group by t.group_id,\n");  
		sql.append("         t.group_name,\n");  
		sql.append("\n");  
		sql.append("         t.group_code\n");  
		sql.append("order by t.group_code\n");
		
		List<Map<String, Object>> list=dao.pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	/*
	 * 查询需要清除的经销商预测信息
	 */
	public List<Map<String, Object>> selectClrDealerForecast(String year,String month,String dealerId,String groupId,boolean bl,boolean isFromMonth){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select f.FORECAST_ID,f.FORECAST_TYPE,d.detail_id\n");
		sql.append("  from TT_VS_MONTHLY_FORECAST_DETAIL d, TT_VS_MONTHLY_FORECAST f\n");  
		sql.append(" where d.FORECAST_ID = f.FORECAST_ID\n");  
		sql.append("   and f.FORECAST_YEAR = "+year+"\n");  
		sql.append("   and f.FORECAST_MONTH = "+month+"\n");  
		if(bl){
			sql.append("   and f.STATUS = "+Constant.FORECAST_STATUS_UNCONFIRM+"\n");  
		}
		sql.append("   and f.ORG_TYPE = "+Constant.ORG_TYPE_DEALER+"\n");  
		sql.append("   and f.DEALER_ID = "+dealerId+"\n");
		if(groupId != null && !groupId.equals("")) 
			sql.append("   and d.GROUP_ID ="+groupId);
		
		if(isFromMonth){ //zxf
			sql.append("   and f.FORECAST_TYPE ="+60591001);
		}
		else{
			sql.append("   and f.FORECAST_TYPE ="+60591002);
		}
		
		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * 查点垃圾数据，为了让PS返回空
	 */
	public PageResult<Map<String, Object>> selectRubbish(int curPage, int pageSize){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select * from TT_VS_MONTHLY_FORECAST where 1=2");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	/*
	 * 查询前三月的参考信息
	 */
	public PageResult<Map<String, Object>> selectHistoryMonth(String dealerId,String groupId,List<Map<String, Object>> list,int pageSize,int curPage){
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
		sql.append("                AND TVO.ORDER_ORG_ID in ("+PlanUtil.createSqlStr(dealerId)+") --换成dealer_id\n");  
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
		sql.append("                AND TVMF.DEALER_ID in ("+PlanUtil.createSqlStr(dealerId)+") --换成dealer_id\n");  
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
		sql.append("           		AND TV.DEALER_ID IN (" + PlanUtil.createSqlStr(dealerId) + ")\n");
		sql.append("           		AND TV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_03 + ", " + Constant.VEHICLE_LIFE_05 + ")\n");
		sql.append("                AND TV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");  
		sql.append("              GROUP BY TVMGR.GROUP_ID) VEH ON GRO.GROUP_ID = VEH.GROUP_ID");


		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}	
	
	public static Long getOrgId(String dealerId) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select vod.root_org_id from vw_org_dealer vod where vod.root_dealer_id = ?\n");
		params.add(dealerId) ;

		Map<String, Object> orgMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;
		
		if(!CommonUtils.isNullMap(orgMap)) {
			return Long.parseLong(orgMap.get("ROOT_ORG_ID").toString()) ;
		}
		
		return null ;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
