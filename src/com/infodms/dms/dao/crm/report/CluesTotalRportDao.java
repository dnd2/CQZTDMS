package com.infodms.dms.dao.crm.report;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

public class CluesTotalRportDao extends BaseDao<PO>{
private static final CluesTotalRportDao dao = new CluesTotalRportDao();
private ActionContext act = ActionContext.getContext();
RequestWrapper request = act.getRequest();
	public static final CluesTotalRportDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public List<Map<String, Object>> getClueReportDaoSelect(Map<String, String> map) throws UnsupportedEncodingException{

		String startDate=map.get("startDate");
		String endDate=map.get("endDate");
		String dealerCode=map.get("dealerCode");
		String dealerId=map.get("dealerId");
		String dutyType=map.get("dutyType");
		String orgId=map.get("orgId");
		String userId=map.get("userId");
		String flag=map.get("flag");
		String manager=map.get("manager");
		String seriesId=map.get("seriesId");
		String leadsOrigin=map.get("leadsOrigin");
		String originId=map.get("originId");
		 
		StringBuilder sql= new StringBuilder();
		
		 sql.append("  select * from (with a as ( \n");
		 sql.append("  select  vw.ROOT_ORG_NAME,vw.PQ_ORG_NAME,vw.DEALER_CODE,vw.DEALER_SHORTNAME, to_number(a.totalAll)+to_number(a.wclcount) tAll,   \n");
		 sql.append("   A.DEALER_ID ,DECODE(A.ADVISER,'','销售经理',gw.name) name,DECODE(A.ADVISER,'','销售经理',gw.group_name) group_name,gw.group_id, A.ADVISER,A.totalAll ,a.wclcount, \n");
		 sql.append(" COUNT60161001  YXCOUNT, \n");
		 sql.append(" B60161001  YXPROPORTION, \n");
		 sql.append(" COUNT60161002  ZBCOUNT, \n");
		 sql.append(" B60161002  ZBPROPORTION, \n");
		 sql.append(" COUNT60161003  SXCOUNT, \n");
		 sql.append(" B60161003  SXPROPORTION, \n");
		 sql.append(" COUNT60161004  WXCOUNT, \n");
		 sql.append(" B60161004  WXPROPORTION, \n");
		 sql.append(" COUNT60161005  CFCOUNT, \n");
		 sql.append(" B60161005  CFPROPORTION, \n");
		 
 		 sql.append("  A.cecount , A.HOURS,A.sccount, A.ddcount,A.DDPROPORTION, \n");
 		 sql.append("  DECODE(A.SCCOUNT,0,'0',ROUND(A.SCCOUNT *100/DECODE(totalAll,0,'1',totalAll) ,2)) || '%' SCPROPORTION \n");
		 sql.append("  FROM( \n");
		 
		 sql.append(" select xs.ADVISER,xs.DEALER_ID ,  sum(xs.CLCOUNT)  totalAll,sum(xs.wclcount) wclcount,  \n");
		  for(Map m : dao.getNameList("6016")){
				sql.append("	 SUM(DECODE(xs.LEADS_STATUS,'"+m.get("CODE_ID")+"',xs.CLCOUNT,0)) COUNT"+m.get("CODE_ID")+", \n");
				sql.append(" ROUND(decode(SUM(DECODE(xs.LEADS_STATUS,'"+m.get("CODE_ID")+"',xs.CLCOUNT,0)),0,0,(SUM(DECODE(xs.LEADS_STATUS,'"+m.get("CODE_ID")+"',xs.CLCOUNT,0))/SUM(xs.CLCOUNT))*100),2) || '%' as  B"+m.get("CODE_ID")+"  , \n");
			}
		 sql.append("    sum(xs.cecount) cecount,  DECODE(sum(xs.cecount),0,'0',ROUND(sum(xs.cecount)  *100/count(xs.LEADS_STATUS)+ sum(xs.wclcount)  ,2) ) || '%' HOURS,  \n");
		 sql.append("    sum(xs.sccount) sccount,-- DECODE(sum(xs.sccount),0,'0',ROUND(sum(xs.sccount) *100/SUM(DECODE(XS.LEADS_STATUS,'60161001',XS.clcount,'0')) ,2) ) || '%'  SCPROPORTION,   \n");
		 sql.append("    sum(xs.ddcount) ddcount,DECODE(sum(xs.ddcount),0,'0',ROUND(sum(xs.ddcount)  *100/sum(xs.sccount) ,2) ) || '%' DDPROPORTION  \n");
		 sql.append("          from   \n");
		 sql.append("        ( SELECT  A.DEALER_ID , A.ADVISER,a.LEADS_STATUS, \n");
		//修改部分
		 if(CommonUtils.isNullString(originId)){
		 sql.append("		a.leads_origin \n");
		 }else{
		 sql.append("		DECODE(c.sccount,'1',c.leads_origin,a.leads_origin) leads_origin \n");	 
		}	 
		 sql.append("       ,a.intent_vehicle,a.dname,a.clcount,a.wclcount,   \n");
		 sql.append("         DECODE(b.cecount,'',0,b.cecount) cecount, DECODE(c.sccount,'',0,c.sccount)  sccount,DECODE(c.ddcount,'',0,c.ddcount) ddcount  \n"); 
		 sql.append("         FROM ( SELECT   A.DEALER_ID , A.ADVISER, A.LEADS_STATUS,MAX(A.DEALER_ID ) AS DNAME ,COUNT(1) AS CLCOUNT ,0 WCLCOUNT ,a.LEADS_ORIGIN,a.INTENT_VEHICLE   \n");
		 sql.append("                ,a.LEADS_CODE  \n");
		 sql.append("              FROM (  SELECT B.CUSTOMER_NAME,B.TELEPHONE ,B.DEALER_ID , B.ADVISER, TL.LEADS_STATUS,tl.LEADS_ORIGIN ,tl.INTENT_VEHICLE,tl.LEADS_CODE  \n");     
		 sql.append("                FROM   T_PC_LEADS_ALLOT B,T_PC_LEADS TL     \n");
		 sql.append("                LEFT  JOIN T_PC_INTENT_VEHICLE TPIV ON Tl.INTENT_VEHICLE = TPIV.SERIES_ID     \n");
		 sql.append("                 WHERE   TL.LEADS_CODE = B.LEADS_CODE      AND  B.IF_CONFIRM  ='60321002'     \n");
			 if(!CommonUtils.isNullString(seriesId)) {
			 		sql.append(" and  tpiv.up_series_id in ("+seriesId+") \n");
			 }
		     if(!CommonUtils.isNullString(startDate)){
	            	   sql.append("     		  AND TL.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
			 	}
	         if(!CommonUtils.isNullString(endDate)){
	        	 	   sql.append("     		  AND TL.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
	         	}
		 sql.append("                )A    \n");
		 sql.append("             GROUP BY   A.DEALER_ID   , A.ADVISER, A.LEADS_STATUS ,a.LEADS_ORIGIN ,a.INTENT_VEHICLE,a.LEADS_CODE   \n");
		 sql.append("             union  all     \n");
		 sql.append("            SELECT B.DEALER_ID , B.ADVISER, TL.LEADS_STATUS ,MAX(b.DEALER_ID ) AS DNAME,0 XSCOUNT ,COUNT(1) AS WCLCOUNT ,tl.LEADS_ORIGIN,tl.INTENT_VEHICLE  \n"); 
		 sql.append("           ,tl.LEADS_CODE  \n");
		 sql.append("              FROM  T_PC_LEADS_ALLOT B, T_PC_LEADS TL     \n");
		 sql.append("              LEFT  JOIN T_PC_INTENT_VEHICLE TPIV ON Tl.INTENT_VEHICLE = TPIV.SERIES_ID    \n");
		 sql.append("             WHERE   TL.LEADS_CODE = B.LEADS_CODE  AND  B.IF_CONFIRM  ='60321001'     \n");
			 if(!CommonUtils.isNullString(seriesId)) {
			 		sql.append(" and  tpiv.up_series_id in ("+seriesId+") \n");
			 }
		 	if(!CommonUtils.isNullString(startDate)){
		 				sql.append("     		  AND TL.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
			 		}
		 	if(!CommonUtils.isNullString(endDate)){
		  	 	   	sql.append("     		  AND TL.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
		   		}
		 sql.append("          GROUP BY  B.DEALER_ID,B.ADVISER,TL.LEADS_STATUS ,tl.LEADS_ORIGIN ,tl.INTENT_VEHICLE ,tl.leads_code  \n");
		 sql.append("        ) A LEFT JOIN   \n");
		 sql.append("         (  SELECT  B.DEALER_ID , B.ADVISER, Tl.LEADS_STATUS,  COUNT(1) AS CECOUNT ,tl.leads_code   \n");
		 sql.append("         FROM TC_CODE TC ,T_PC_LEADS_ALLOT B, T_PC_LEADS TL    \n");
		 sql.append("          LEFT  JOIN T_PC_INTENT_VEHICLE TPIV ON Tl.INTENT_VEHICLE = TPIV.SERIES_ID   \n");
		 sql.append("              WHERE TC.CODE_ID = TL.LEADS_ORIGIN AND TL.LEADS_CODE = B.LEADS_CODE AND  B.IF_CONFIRM  ='60321002'   \n");
		 sql.append("      AND     ceil((B.CONFIRM_DATE - tl.Create_Date)* 24 ) <= 24      \n");
		 if(!CommonUtils.isNullString(seriesId)) {
			 		sql.append(" and  tpiv.up_series_id in ("+seriesId+") \n");
		 }
			if(!CommonUtils.isNullString(startDate)){
 					sql.append("     		  AND TL.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
	 		}
		 	if(!CommonUtils.isNullString(endDate)){
		  	 	   	sql.append("     		  AND TL.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
		   		}
		 sql.append("             GROUP BY   B.DEALER_ID ,  B.ADVISER, Tl.LEADS_STATUS ,tl.leads_code   \n");
		 sql.append("              ) B ON A.leads_code=B.leads_code  \n");
		 sql.append("              left join (   \n");
		 
		 
		 if(CommonUtils.isNullString(originId)){
		 sql.append("            SELECT tl.LEADS_ORIGIN,B.DEALER_ID , B.ADVISER, Tl.LEADS_STATUS,  COUNT(1) AS SCCOUNT ,count(tpo.order_id) ddcount,TL.INTENT_VEHICLE,tl.leads_code  \n");  
		 sql.append("            FROM T_PC_LEADS_ALLOT B , T_PC_LEADS TL left join T_PC_ORDER tpo on tl.customer_id=tpo.customer_id    \n");
		 sql.append("               LEFT  JOIN T_PC_INTENT_VEHICLE TPIV ON Tl.INTENT_VEHICLE = TPIV.SERIES_ID     \n");
		 sql.append("               WHERE  TL.LEADS_CODE = B.LEADS_CODE  and tl.jc_way='60021003'    AND  B.IF_CONFIRM  ='60321002'    \n");
		 }else{ //修改部分
			 sql.append(" SELECT tl.LEADS_ORIGIN LEADS_ORIGIN,tl.DEALER_ID,tl.ADVISER,Tl.LEADS_STATUS,tl.SCCOUNT,tl.ddcount,TL.INTENT_VEHICLE,tl.leads_code \n");   
			 sql.append(" from ( \n");  
			 sql.append("                      SELECT tl.LEADS_ORIGIN,tl.telephone,tl.create_date,B.DEALER_ID , B.ADVISER, Tl.LEADS_STATUS,  COUNT(1) AS SCCOUNT ,count(tpo.order_id) ddcount,TL.INTENT_VEHICLE,tl.leads_code  \n");  
			 sql.append("            FROM T_PC_LEADS_ALLOT B , T_PC_LEADS TL left join T_PC_ORDER tpo on tl.customer_id=tpo.customer_id    \n");
			 sql.append("                 LEFT  JOIN T_PC_INTENT_VEHICLE TPIV ON Tl.INTENT_VEHICLE = TPIV.SERIES_ID     \n");
			 sql.append("               WHERE  TL.LEADS_CODE = B.LEADS_CODE  and  tl.jc_way='60021003'   AND  B.IF_CONFIRM  ='60321002'   \n");
		 }

		 if(!CommonUtils.isNullString(seriesId)) {
			 		sql.append(" and  tpiv.up_series_id in ("+seriesId+") \n");
			 }
			if(!CommonUtils.isNullString(startDate)){
 				  sql.append("     		    AND TL.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
	 		}
		 	if(!CommonUtils.isNullString(endDate)){
		  	 	   	sql.append("     		  AND TL.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
		   		}
		
			if(CommonUtils.isNullString(originId)){
				 sql.append("               GROUP BY  tl.LEADS_ORIGIN,B.DEALER_ID , B.ADVISER, Tl.LEADS_STATUS ,TL.INTENT_VEHICLE ,tl.leads_code  \n");
			}else{
			 sql.append("               GROUP BY  tl.LEADS_ORIGIN,B.DEALER_ID , B.ADVISER, Tl.LEADS_STATUS ,TL.INTENT_VEHICLE ,tl.telephone,tl.create_date,tl.leads_code  \n");	
			}
			 //修改部分
			 if(!CommonUtils.isNullString(originId)){
				 String arr[]=originId.split(",");
		            String str="";

		            for(int i=0;i<arr.length;i++){
		                str+="'"+arr[i]+"',";
		            }
		            str=str.substring(0,str.lastIndexOf(","));
			 sql.append(" )tl   \n");
			 sql.append("where tl.telephone in(  \n");
			 sql.append(" select tpl.telephone from t_pc_leads tpl where tpl.leads_origin in ("+str+")  \n");
			 sql.append(" and tpl.CREATE_DATE<= tl.create_date )  \n");
			 }
		 
		 sql.append("              )c on a.leads_code =c.leads_code   \n");
		 sql.append("                )xs  \n");
		 if(!CommonUtils.isNullString(originId)){
			 String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));
		 sql.append(" where xs.leads_origin in ("+str+") \n"); 
		 }
		 sql.append("                group by xs.ADVISER,xs.DEALER_ID   \n");
		 sql.append("                )A   left join VW_ORG_DEALER_ALL vw   on a.DEALER_ID =to_char(vw.DEALER_ID)  \n");
		 
		 sql.append("   left join \n");
		 sql.append("   ( select tu.user_id,tu.name,tpg.group_id,tpg.group_name from tc_user tu,t_pc_group tpg  where   tu.group_id=tpg.group_id )gw  on A.ADVISER =gw.user_id   where 1=1  \n");
                 
		 //sql.append("                ,tc_user tu,t_pc_group tpg  where A.ADVISER =tu.user_id  and  tu.group_id=tpg.group_id  \n");
		 if(!CommonUtils.isNullString(userId)) {
				sql.append(" AND A.ADVISER  IN ("+userId+") \n");
			}
		
		    //经销商
		if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
			String str="'0',";
			if(!"".equals(dealerCode)&&dealerCode!=null){
	            String arr[]=dealerCode.split(",");
	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
			}
			str=str.substring(0,str.lastIndexOf(","));
			sql.append("  AND (VW.DEALER_ID ="+dealerId+" or vw.DEALER_CODE in("+str+") )\n");
			//大区
		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND VW.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
			//小区
		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND VW.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.PQ_ORG_ID="+orgId+") \n");
		}
		if(!"".equals(dealerCode)&&dealerCode!=null){
            String arr[]=dealerCode.split(",");
            String str="";
            
            for(int i=0;i<arr.length;i++){
                str+="'"+arr[i]+"',";
            }
            str=str.substring(0,str.lastIndexOf(","));

			sql.append("AND VW.DEALER_CODE IN("+str+") \n");
		}
		 sql.append("  order by vw.DEALER_ID ,gw.group_name  )            \n");
		 
		 sql.append(" select a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.DEALER_CODE,a.DEALER_SHORTNAME,a.totalAll ,a.wclcount, \n");
		 sql.append(" '2' a ,a.name,a.group_name,a.group_id,  \n");
		 sql.append("   YXCOUNT,  YXPROPORTION, \n");
		 sql.append("   ZBCOUNT,   ZBPROPORTION, \n");
		 sql.append("   SXCOUNT,   SXPROPORTION, \n");
		 sql.append("   WXCOUNT,  WXPROPORTION, \n");
		 sql.append("   CFCOUNT CFTOTAL,   CFPROPORTION, \n");
		 sql.append("  A.cecount, DECODE(A.cecount,0,'0',ROUND(A.cecount *100/ a.tAll ,2)) || '%' HOURS, \n");
		 sql.append("  A.sccount, a.SCPROPORTION, \n");
		 sql.append("  A.ddcount,A.DDPROPORTION   from a  \n");
		 if(!CommonUtils.isNullString(flag)) {
			 sql.append("        union all  \n");
			 sql.append("   select a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.DEALER_CODE,a.DEALER_SHORTNAME,sum(totalAll) totalAll, sum(a.wclcount) wclcount, \n");
			 sql.append("  	 '1' , max(a.group_name)||'小计', a.group_name, a.group_id,  \n");
			 sql.append("   sum(a.YXCOUNT), DECODE(sum(a.YXCOUNT),0,'0',ROUND(sum(a.YXCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  YXPROPORTION , \n");  
			 sql.append("   sum(a.ZBCOUNT), DECODE(sum(a.ZBCOUNT),0,'0',ROUND(sum(a.ZBCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  ZBPROPORTION , \n");
			 sql.append("   sum(a.SXCOUNT), DECODE(sum(a.SXCOUNT),0,'0',ROUND(sum(a.SXCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  SXPROPORTION , \n");  
			 sql.append("   sum(a.WXCOUNT), DECODE(sum(a.WXCOUNT),0,'0',ROUND(sum(a.WXCOUNT) *100/sum(a.totalAll)  ,2)) || '%' WXPROPORTION , \n"); 
			 sql.append("   sum(a.CFCOUNT) CFTOTAL , DECODE(sum(a.CFCOUNT),0,'0',ROUND(sum(a.CFCOUNT) *100/sum(a.totalAll)  ,2)) || '%' CFPROPORTION , \n"); 
			 sql.append(" 	sum(A.cecount) cecount,DECODE(sum(A.cecount),0,'0',ROUND(sum(A.cecount) *100/sum(a.tAll)  ,2)) || '%'  HOURS  ,  \n");
			 sql.append(" 	sum(A.sccount) sccount,DECODE(sum(A.sccount),0,'0',ROUND(sum(A.sccount) *100/sum(a.totalAll)  ,2)) || '%'  SCPROPORTION,  \n");
			 sql.append(" 	sum(A.ddcount) ddcount, DECODE(sum(A.ddcount),0,'0',ROUND(sum(A.ddcount) *100/sum(a.sccount)  ,2)) || '%' DDPROPORTION   \n");
			 sql.append("   from a  group by  a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.DEALER_CODE,a.DEALER_SHORTNAME, a.group_name,a.group_id  \n");
			if(!CommonUtils.isNullString(manager)){
				 sql.append("        union all  \n");
				 sql.append("   select a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.DEALER_CODE,max(a.DEALER_SHORTNAME),sum(totalAll) totalAll,  sum(a.wclcount) wclcount,   \n");
				 sql.append("  	 '3' , max(a.DEALER_SHORTNAME)||'总计', ' ', max(a.group_id),  \n");
				 sql.append("   sum(a.YXCOUNT), DECODE(sum(a.YXCOUNT),0,'0',ROUND(sum(a.YXCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  YXPROPORTION , \n");  
				 sql.append("   sum(a.ZBCOUNT), DECODE(sum(a.ZBCOUNT),0,'0',ROUND(sum(a.ZBCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  ZBPROPORTION , \n");
				 sql.append("   sum(a.SXCOUNT), DECODE(sum(a.SXCOUNT),0,'0',ROUND(sum(a.SXCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  SXPROPORTION , \n");  
				 sql.append("   sum(a.WXCOUNT), DECODE(sum(a.WXCOUNT),0,'0',ROUND(sum(a.WXCOUNT) *100/sum(a.totalAll)  ,2)) || '%' WXPROPORTION , \n"); 
				 sql.append("   sum(a.CFCOUNT) CFTOTAL, DECODE(sum(a.CFCOUNT),0,'0',ROUND(sum(a.CFCOUNT) *100/sum(a.totalAll)  ,2)) || '%' CFPROPORTION , \n"); 
				 sql.append(" 		sum(A.cecount) cecount,DECODE(sum(A.cecount),0,'0',ROUND(sum(A.cecount) *100/sum(a.tAll)   ,2)) || '%'  HOURS  ,  \n");
				 sql.append(" 		sum(A.sccount) sccount,DECODE(sum(A.sccount),0,'0',ROUND(sum(A.sccount) *100/sum(a.totalAll)  ,2)) || '%'  SCPROPORTION,  \n");
				 sql.append(" 		sum(A.ddcount) ddcount, DECODE(sum(A.ddcount),0,'0',ROUND(sum(A.ddcount) *100/sum(a.sccount)  ,2)) || '%' DDPROPORTION   \n");
				 sql.append("   from a  group by  a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.DEALER_CODE   \n");
			}
		 }
		 sql.append("   ) tmp order by tmp.ROOT_ORG_NAME,tmp.PQ_ORG_NAME,tmp.DEALER_CODE, tmp.a ,tmp.group_name   \n");

		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	public List<Map<String, Object>> getClueReportDaoSelectAll(Map<String, String> map) throws UnsupportedEncodingException{

		String startDate=map.get("startDate");
		String endDate=map.get("endDate");
		String dealerCode=map.get("dealerCode");
		String dealerId=map.get("dealerId");
		String dutyType=map.get("dutyType");
		String orgId=map.get("orgId");
		String seriesId=map.get("seriesId");
		String leadsOrigin=map.get("leadsOrigin");
		String originId=map.get("originId");
		StringBuilder sql= new StringBuilder();
		
		 sql.append("  select * from (with a as ( \n");
		 sql.append("  select  vw.ROOT_ORG_NAME,vw.PQ_ORG_NAME,vw.DEALER_CODE,vw.DEALER_SHORTNAME,  \n");
		 sql.append("   A.DEALER_ID ,A.totalAll ,a.wclcount, to_number(a.totalAll)+to_number(a.wclcount) tAll, \n");
		 sql.append(" COUNT60161001  YXCOUNT, \n");
		 sql.append(" B60161001  YXPROPORTION, \n");
		 sql.append(" COUNT60161002  ZBCOUNT, \n");
		 sql.append(" B60161002  ZBPROPORTION, \n");
		 sql.append(" COUNT60161003  SXCOUNT, \n");
		 sql.append(" B60161003  SXPROPORTION, \n");
		 sql.append(" COUNT60161004  WXCOUNT, \n");
		 sql.append(" B60161004  WXPROPORTION, \n");
		 sql.append(" COUNT60161005  CFCOUNT, \n");
		 sql.append(" B60161005  CFPROPORTION, \n");
		 
 		 sql.append("  A.cecount , A.HOURS,A.sccount, A.ddcount,A.DDPROPORTION, \n");
 		 sql.append("  DECODE(A.SCCOUNT,0,'0',ROUND(A.SCCOUNT *100/DECODE(totalAll,0,'1',totalAll) ,2)) || '%' SCPROPORTION \n");
		 sql.append("  FROM( \n");
		 
		 sql.append(" select  xs.DEALER_ID ,  sum(xs.CLCOUNT)  totalAll,sum(xs.wclcount) wclcount,  \n");
		  for(Map m : dao.getNameList("6016")){
				sql.append("	 SUM(DECODE(xs.LEADS_STATUS,'"+m.get("CODE_ID")+"',xs.CLCOUNT,0)) COUNT"+m.get("CODE_ID")+", \n");
				sql.append(" ROUND(decode(SUM(DECODE(xs.LEADS_STATUS,'"+m.get("CODE_ID")+"',xs.CLCOUNT,0)),0,0,(SUM(DECODE(xs.LEADS_STATUS,'"+m.get("CODE_ID")+"',xs.CLCOUNT,0))/SUM(xs.CLCOUNT))*100),2) || '%' as  B"+m.get("CODE_ID")+"  , \n");
			}
		 sql.append("    sum(xs.cecount) cecount,  DECODE(sum(xs.cecount),0,'0',ROUND(sum(xs.cecount)  *100/count(xs.LEADS_STATUS)+ sum(xs.wclcount)  ,2) ) || '%' HOURS,  \n");
		 sql.append("    sum(xs.sccount) sccount,-- DECODE(sum(xs.sccount),0,'0',ROUND(sum(xs.sccount) *100/SUM(DECODE(XS.LEADS_STATUS,'60161001',XS.clcount,'0')) ,2) ) || '%'  SCPROPORTION,   \n");
		 sql.append("    sum(xs.ddcount) ddcount,DECODE(sum(xs.ddcount),0,'0',ROUND(sum(xs.ddcount)  *100/sum(xs.sccount) ,2) ) || '%' DDPROPORTION  \n");
		 sql.append("          from   \n");
		 sql.append("        ( SELECT  A.DEALER_ID , A.ADVISER,a.LEADS_STATUS, \n");
		 //修改部分
		 if(CommonUtils.isNullString(leadsOrigin)){
		 sql.append("		a.leads_origin \n");
		 }else{
		 sql.append("		DECODE(c.sccount,'1',c.leads_origin,a.leads_origin) leads_origin \n");	 
		 }
		 sql.append("  		,a.intent_vehicle,a.dname,a.clcount,a.wclcount,   \n");
		 sql.append("         DECODE(b.cecount,'',0,b.cecount) cecount, DECODE(c.sccount,'',0,c.sccount)  sccount,DECODE(c.ddcount,'',0,c.ddcount) ddcount  \n"); 
		 sql.append("         FROM ( SELECT   A.DEALER_ID , A.ADVISER, A.LEADS_STATUS,MAX(A.DEALER_ID ) AS DNAME ,COUNT(1) AS CLCOUNT ,0 WCLCOUNT ,a.LEADS_ORIGIN,a.INTENT_VEHICLE   \n");
		 sql.append("                ,a.LEADS_CODE  \n");
		 sql.append("              FROM (  SELECT B.CUSTOMER_NAME,B.TELEPHONE ,B.DEALER_ID , B.ADVISER, TL.LEADS_STATUS,tl.LEADS_ORIGIN ,tl.INTENT_VEHICLE,tl.LEADS_CODE  \n");     
		 sql.append("                FROM   T_PC_LEADS_ALLOT B ,T_PC_LEADS TL     \n");
		 sql.append("                 LEFT  JOIN T_PC_INTENT_VEHICLE TPIV ON Tl.INTENT_VEHICLE = TPIV.SERIES_ID     \n");
		 sql.append("                 WHERE   TL.LEADS_CODE = B.LEADS_CODE     AND  B.IF_CONFIRM  ='60321002'     \n");
			 if(!CommonUtils.isNullString(seriesId)) {
			 		sql.append(" and  tpiv.up_series_id in ("+seriesId+") \n");
			 }
		     if(!CommonUtils.isNullString(startDate)){
	            	   sql.append("     		  AND TL.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
			 	}
	         if(!CommonUtils.isNullString(endDate)){
	        	 	   sql.append("     		  AND TL.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
	        	 	   
	         	}
	         if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
		 			sql.append("  AND (B.DEALER_ID ="+dealerId+")\n");
		 			//大区
		 		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
		 			
		 			sql.append(" AND B.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
		 			//小区
		 		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
		 			sql.append(" AND B.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.PQ_ORG_ID="+orgId+") \n");
		 		}
		 sql.append("                )A    \n");
		 sql.append("             GROUP BY   A.DEALER_ID   , A.ADVISER, A.LEADS_STATUS ,a.LEADS_ORIGIN ,a.INTENT_VEHICLE,a.LEADS_CODE   \n");
		 sql.append("            union  all    \n");
		 sql.append("            SELECT B.DEALER_ID , B.ADVISER, TL.LEADS_STATUS ,MAX(b.DEALER_ID ) AS DNAME,0 XSCOUNT ,COUNT(1) AS WCLCOUNT ,tl.LEADS_ORIGIN,tl.INTENT_VEHICLE  \n"); 
		 sql.append("           ,tl.LEADS_CODE  \n");
		 sql.append("              FROM   T_PC_LEADS_ALLOT B ,T_PC_LEADS TL     \n");
		 sql.append("                 LEFT  JOIN T_PC_INTENT_VEHICLE TPIV ON Tl.INTENT_VEHICLE = TPIV.SERIES_ID     \n");
		 sql.append("             WHERE   TL.LEADS_CODE = B.LEADS_CODE  AND  B.IF_CONFIRM  ='60321001'     \n");
			 if(!CommonUtils.isNullString(seriesId)) {
			 		sql.append(" and  tpiv.up_series_id in ("+seriesId+") \n");
			 }
		 
		 	if(!CommonUtils.isNullString(startDate)){
		 				sql.append("     		  AND TL.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
			 		}
		 	if(!CommonUtils.isNullString(endDate)){
		  	 	   	sql.append("     		  AND TL.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
		   		}
		 	 if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
		 			sql.append("  AND (B.DEALER_ID ="+dealerId+")\n");
		 			//大区
		 		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
		 			
		 			sql.append(" AND B.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
		 			//小区
		 		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
		 			sql.append(" AND B.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.PQ_ORG_ID="+orgId+") \n");
		 		}
		 sql.append("          GROUP BY  B.DEALER_ID,B.ADVISER,TL.LEADS_STATUS ,tl.LEADS_ORIGIN ,tl.INTENT_VEHICLE ,tl.leads_code  \n");
		 sql.append("        ) A LEFT JOIN   \n");
		 sql.append("         (  SELECT  B.DEALER_ID , B.ADVISER, Tl.LEADS_STATUS,  COUNT(1) AS CECOUNT ,tl.leads_code   \n");
		 sql.append("         FROM TC_CODE TC ,T_PC_LEADS_ALLOT B , T_PC_LEADS TL   \n");
		 sql.append("          LEFT  JOIN T_PC_INTENT_VEHICLE TPIV ON Tl.INTENT_VEHICLE = TPIV.SERIES_ID   \n");
		 sql.append("              WHERE TC.CODE_ID = TL.LEADS_ORIGIN AND TL.LEADS_CODE = B.LEADS_CODE AND  B.IF_CONFIRM  ='60321002'   \n");
		 sql.append("      AND    ceil((B.CONFIRM_DATE - tl.Create_Date)* 24 ) <= 24      \n");
			 if(!CommonUtils.isNullString(seriesId)) {
				 		sql.append(" and  tpiv.up_series_id in ("+seriesId+") \n");
			 }
			if(!CommonUtils.isNullString(startDate)){
 					sql.append("     		  AND TL.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
	 		}
		 	if(!CommonUtils.isNullString(endDate)){
		  	 	   	sql.append("     		  AND TL.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
		  	 	   	
		   		}
		 	 if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
		 			sql.append("  AND (B.DEALER_ID ="+dealerId+")\n");
		 			//大区
		 		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
		 			
		 			sql.append(" AND B.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
		 			//小区
		 		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
		 			sql.append(" AND B.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.PQ_ORG_ID="+orgId+") \n");
		 		}
		 sql.append("             GROUP BY   B.DEALER_ID ,  B.ADVISER, Tl.LEADS_STATUS ,tl.leads_code   \n");
		 sql.append("              ) B ON A.leads_code=B.leads_code  \n");
		 sql.append("              left join (   \n");
		 
		 if(CommonUtils.isNullString(originId)){
		 sql.append("                      SELECT tl.LEADS_ORIGIN,B.DEALER_ID , B.ADVISER, Tl.LEADS_STATUS,  COUNT(1) AS SCCOUNT ,count(tpo.order_id) ddcount,TL.INTENT_VEHICLE,tl.leads_code  \n");  
		 sql.append("            FROM T_PC_LEADS_ALLOT B , T_PC_LEADS TL left join T_PC_ORDER tpo on tl.customer_id=tpo.customer_id    \n");
		 sql.append("                 LEFT  JOIN T_PC_INTENT_VEHICLE TPIV ON Tl.INTENT_VEHICLE = TPIV.SERIES_ID     \n");
		 sql.append("               WHERE  TL.LEADS_CODE = B.LEADS_CODE  and  tl.jc_way='60021003'   AND  B.IF_CONFIRM  ='60321002'   \n");
		 }else{ //修改部分
			 sql.append(" SELECT tl.LEADS_ORIGIN LEADS_ORIGIN,tl.DEALER_ID,tl.ADVISER,Tl.LEADS_STATUS,tl.SCCOUNT,tl.ddcount,TL.INTENT_VEHICLE,tl.leads_code \n");   
			 sql.append(" from ( \n");  
			 sql.append("                      SELECT tl.LEADS_ORIGIN,tl.telephone,tl.create_date,B.DEALER_ID , B.ADVISER, Tl.LEADS_STATUS,  COUNT(1) AS SCCOUNT ,count(tpo.order_id) ddcount,TL.INTENT_VEHICLE,tl.leads_code  \n");  
			 sql.append("            FROM T_PC_LEADS_ALLOT B , T_PC_LEADS TL left join T_PC_ORDER tpo on tl.customer_id=tpo.customer_id    \n");
			 sql.append("                 LEFT  JOIN T_PC_INTENT_VEHICLE TPIV ON Tl.INTENT_VEHICLE = TPIV.SERIES_ID     \n");
			 sql.append("               WHERE  TL.LEADS_CODE = B.LEADS_CODE  and  tl.jc_way='60021003'   AND  B.IF_CONFIRM  ='60321002'   \n");
		 }
		 
			 if(!CommonUtils.isNullString(seriesId)) {
			 		sql.append(" and  tpiv.up_series_id in ("+seriesId+") \n");
			 }
			if(!CommonUtils.isNullString(startDate)){
 				  sql.append("     		    AND TL.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
	 		}
		 	if(!CommonUtils.isNullString(endDate)){
		  	 	   	sql.append("     		  AND TL.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
		   		}
		 	 if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
		 			sql.append("  AND (B.DEALER_ID ="+dealerId+")\n");
		 			//大区
		 		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
		 			
		 			sql.append(" AND B.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
		 			//小区
		 		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
		 			sql.append(" AND B.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.PQ_ORG_ID="+orgId+") \n");
		 		}
		if(CommonUtils.isNullString(originId)){
		 sql.append("               GROUP BY  tl.LEADS_ORIGIN,B.DEALER_ID , B.ADVISER, Tl.LEADS_STATUS ,TL.INTENT_VEHICLE ,tl.leads_code  \n");
		}else{
		 sql.append("               GROUP BY  tl.LEADS_ORIGIN,B.DEALER_ID , B.ADVISER, Tl.LEADS_STATUS ,TL.INTENT_VEHICLE ,tl.telephone,tl.create_date,tl.leads_code  \n");	
		}
		 
		 //修改部分
		 if(!CommonUtils.isNullString(originId)){
			  String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));
		 sql.append(" )tl   \n");
		 sql.append("where tl.telephone in(  \n");
		 sql.append(" select tpl.telephone from t_pc_leads tpl where tpl.leads_origin in ("+str+")  \n");
		 sql.append(" and tpl.CREATE_DATE<= tl.create_date )  \n");
		 }
		 sql.append("              )c on a.leads_code =c.leads_code   \n");
		 sql.append("                )xs  \n");
		 if(!CommonUtils.isNullString(originId)){
			   String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));
			 sql.append(" where xs.leads_origin in("+str+") \n"); 
			 }
		 sql.append("                group by xs.DEALER_ID   \n");
		 sql.append("                )A   left join VW_ORG_DEALER_ALL vw   on a.DEALER_ID =to_char(vw.DEALER_ID)  \n");
		 sql.append("          where 1=1  \n");
	 
		    //经销商
		if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
		 
			sql.append("  AND (VW.DEALER_ID ="+dealerId+"   )\n");
			//大区
		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND VW.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
			//小区
		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND VW.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.PQ_ORG_ID="+orgId+") \n");
		}
		if(!"".equals(dealerCode)&&dealerCode!=null){
            String arr[]=dealerCode.split(",");
            String str="";
            
            for(int i=0;i<arr.length;i++){
                str+="'"+arr[i]+"',";
            }
            str=str.substring(0,str.lastIndexOf(","));

			sql.append("AND VW.DEALER_CODE IN("+str+") \n");
		}
		 sql.append("  order by vw.DEALER_ID   )            \n");
		 
		 sql.append(" select TO_CHAR(A.DEALER_ID) DEALER_ID,a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.DEALER_CODE,a.DEALER_SHORTNAME,a.totalAll ,a.wclcount,'3' a , \n");
		 sql.append("   YXCOUNT,  YXPROPORTION, \n");
		 sql.append("   ZBCOUNT,   ZBPROPORTION, \n");
		 sql.append("   SXCOUNT,   SXPROPORTION, \n");
		 sql.append("   WXCOUNT,  WXPROPORTION, \n");
		 sql.append("   CFCOUNT CFTOTAL,   CFPROPORTION, \n");
		 sql.append("  A.cecount, DECODE(A.cecount,0,'0',ROUND(A.cecount *100/ a.tAll ,2)) || '%' HOURS, \n");
		 sql.append("  A.sccount, a.SCPROPORTION, \n");
		 sql.append("  A.ddcount,A.DDPROPORTION   from a  \n");
		 if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType) || Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("        union all  \n");
			 sql.append("   select '',a.ROOT_ORG_NAME,a.PQ_ORG_NAME,' ',max(a.PQ_ORG_NAME)||'小计',sum(totalAll) totalAll, sum(a.wclcount) wclcount, '3' ,   \n");
			 sql.append("   sum(a.YXCOUNT), DECODE(sum(a.YXCOUNT),0,'0',ROUND(sum(a.YXCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  YXPROPORTION , \n");  
			 sql.append("   sum(a.ZBCOUNT), DECODE(sum(a.ZBCOUNT),0,'0',ROUND(sum(a.ZBCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  ZBPROPORTION , \n");
			 sql.append("   sum(a.SXCOUNT), DECODE(sum(a.SXCOUNT),0,'0',ROUND(sum(a.SXCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  SXPROPORTION , \n");  
			 sql.append("   sum(a.WXCOUNT), DECODE(sum(a.WXCOUNT),0,'0',ROUND(sum(a.WXCOUNT) *100/sum(a.totalAll)  ,2)) || '%' WXPROPORTION , \n"); 
			 sql.append("   sum(a.CFCOUNT) CFTOTAL , DECODE(sum(a.CFCOUNT),0,'0',ROUND(sum(a.CFCOUNT) *100/sum(a.totalAll)  ,2)) || '%' CFPROPORTION , \n"); 
			 sql.append(" 	sum(A.cecount) cecount,DECODE(sum(A.cecount),0,'0',ROUND(sum(A.cecount) *100/sum(a.tAll)  ,2)) || '%'  HOURS  ,  \n");
			 sql.append(" 	sum(A.sccount) sccount,DECODE(sum(A.sccount),0,'0',ROUND(sum(A.sccount) *100/sum(a.totalAll) ,2)) || '%'  SCPROPORTION,  \n");
			 sql.append(" 	sum(A.ddcount) ddcount, DECODE(sum(A.ddcount),0,'0',ROUND(sum(A.ddcount) *100/DECODE(sum(a.sccount),0,'1',sum(a.sccount)) ,2)) || '%' DDPROPORTION   \n");
			 sql.append("   from a  group by  a.ROOT_ORG_NAME,a.PQ_ORG_NAME   \n");
		   }  
		  if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				 
				 sql.append("        union all  \n");
				 sql.append("   select '',a.ROOT_ORG_NAME,' ',' ',max(a.ROOT_ORG_NAME)||'合计',sum(totalAll) totalAll,  sum(a.wclcount) wclcount, '2' ,  \n");
				 sql.append("   sum(a.YXCOUNT), DECODE(sum(a.YXCOUNT),0,'0',ROUND(sum(a.YXCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  YXPROPORTION , \n");  
				 sql.append("   sum(a.ZBCOUNT), DECODE(sum(a.ZBCOUNT),0,'0',ROUND(sum(a.ZBCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  ZBPROPORTION , \n");
				 sql.append("   sum(a.SXCOUNT), DECODE(sum(a.SXCOUNT),0,'0',ROUND(sum(a.SXCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  SXPROPORTION , \n");  
				 sql.append("   sum(a.WXCOUNT), DECODE(sum(a.WXCOUNT),0,'0',ROUND(sum(a.WXCOUNT) *100/sum(a.totalAll)  ,2)) || '%' WXPROPORTION , \n"); 
				 sql.append("   sum(a.CFCOUNT) CFTOTAL, DECODE(sum(a.CFCOUNT),0,'0',ROUND(sum(a.CFCOUNT) *100/sum(a.totalAll)  ,2)) || '%' CFPROPORTION , \n"); 
				 sql.append(" 		sum(A.cecount) cecount,DECODE(sum(A.cecount),0,'0',ROUND(sum(A.cecount) *100/sum(a.tAll)   ,2)) || '%'  HOURS  ,  \n");
				 sql.append(" 		sum(A.sccount) sccount,DECODE(sum(A.sccount),0,'0',ROUND(sum(A.sccount) *100/sum(a.totalAll) ,2)) || '%'  SCPROPORTION,  \n");
				 sql.append(" 		sum(A.ddcount) ddcount, DECODE(sum(A.ddcount),0,'0',ROUND(sum(A.ddcount) *100/DECODE(sum(a.sccount),0,'1',sum(a.sccount))  ,2)) || '%' DDPROPORTION   \n");
				 sql.append("   from a  group by  a.ROOT_ORG_NAME  \n");
		 }
		  if( Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				 
				 sql.append("        union all  \n");
				 sql.append("   select '',' ',' ',' ','全国总计',sum(totalAll) totalAll,  sum(a.wclcount) wclcount, '1' ,  \n");
				 sql.append("   sum(a.YXCOUNT), DECODE(sum(a.YXCOUNT),0,'0',ROUND(sum(a.YXCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  YXPROPORTION , \n");  
				 sql.append("   sum(a.ZBCOUNT), DECODE(sum(a.ZBCOUNT),0,'0',ROUND(sum(a.ZBCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  ZBPROPORTION , \n");
				 sql.append("   sum(a.SXCOUNT), DECODE(sum(a.SXCOUNT),0,'0',ROUND(sum(a.SXCOUNT) *100/sum(a.totalAll)  ,2)) || '%'  SXPROPORTION , \n");  
				 sql.append("   sum(a.WXCOUNT), DECODE(sum(a.WXCOUNT),0,'0',ROUND(sum(a.WXCOUNT) *100/sum(a.totalAll)  ,2)) || '%' WXPROPORTION , \n"); 
				 sql.append("   sum(a.CFCOUNT) CFTOTAL, DECODE(sum(a.CFCOUNT),0,'0',ROUND(sum(a.CFCOUNT) *100/sum(a.totalAll)  ,2)) || '%' CFPROPORTION , \n"); 
				 sql.append(" 		sum(A.cecount) cecount,DECODE(sum(A.cecount),0,'0',ROUND(sum(A.cecount) *100/sum(a.tAll)   ,2)) || '%'  HOURS  ,  \n");
				 sql.append(" 		sum(A.sccount) sccount,DECODE(sum(A.sccount),0,'0',ROUND(sum(A.sccount) *100/sum(a.totalAll)  ,2)) || '%'  SCPROPORTION,  \n");
				 sql.append(" 		sum(A.ddcount) ddcount, DECODE(sum(A.ddcount),0,'0',ROUND(sum(A.ddcount)  *100/DECODE(sum(a.sccount),0,'1',sum(a.sccount))  ,2)) || '%' DDPROPORTION   \n");
				 sql.append("   from a    \n");
		 }
		 sql.append("   ) tmp order by   tmp.a,tmp.ROOT_ORG_NAME, tmp.pq_org_name ,rownum      \n");

		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getClueReportDaoSelectAll_(Map<String, String> map) throws UnsupportedEncodingException{
		String startDate=map.get("startDate");
		String endDate=map.get("endDate");
		String dealerCode=map.get("dealerCode");
		String dealerId=map.get("dealerId");
		String dutyType=map.get("dutyType");
		String orgId=map.get("orgId");
		String seriesId=map.get("seriesId");
		
		StringBuilder sql= new StringBuilder();
		 sql.append("  select  vw.ROOT_ORG_NAME,vw.PQ_ORG_NAME,vw.DEALER_CODE,vw.DEALER_SHORTNAME, A.DEALER_ID  ,  \n");
		 sql.append("  (A.XSTOTAL+A.CFTOTAL) totalAll, \n");
		 sql.append(" (a.yxCOUNT+a.zbCOUNT+a.sxCOUNT+a.wxCOUNT+A.CFTOTAL) CLTOTAL,\n");
		 sql.append(" ROUND(decode(a.CECOUNT,0,0,(a.CECOUNT/(A.XSTOTAL+A.CFTOTAL))*100)) || '%'   AS HOURS ,\n");
		 sql.append(" A.yxCOUNT , ROUND( decode(A.yxCOUNT,0,0,(A.yxCOUNT/(A.XSTOTAL+A.CFTOTAL))*100  ,2 )) || '%'  YXPROPORTION,\n");
		 sql.append("  A.zbCOUNT, ROUND( decode(A.zbCOUNT,0,0,(A.zbCOUNT/(A.XSTOTAL+A.CFTOTAL))*100  ,2 )) || '%' ZBPROPORTION,\n");
		 sql.append("  A.sxCOUNT, ROUND( decode(A.sxCOUNT,0,0,(A.sxCOUNT/(A.XSTOTAL+A.CFTOTAL))*100  ,2 )) || '%'   SXPROPORTION,\n");
		 sql.append(" A.wxCOUNT , ROUND( decode(A.wxCOUNT,0,0,(A.wxCOUNT/(A.XSTOTAL+A.CFTOTAL))*100  ,2 )) || '%'    WXPROPORTION,\n");
		 sql.append(" A.CFTOTAL , ROUND( DECODE(A.CFTOTAL,0,0,(A.CFTOTAL/(A.XSTOTAL+A.CFTOTAL))*100  ,2 )) || '%'   CFPROPORTION, \n");
		 sql.append(" A.SCCOUNT , ROUND( decode(A.SCCOUNT,0,0,(A.SCCOUNT/(A.XSTOTAL+A.CFTOTAL))*100  ,2 )) || '%' SCPROPORTION, \n");
		 sql.append(" A.DDCOUNT , ROUND( decode(A.ddcount,0,0,(A.ddcount/A.SCCOUNT)*100)) || '%' as DDPROPORTION \n");
		 sql.append(" from (select a.DEALER_ID,count(a.ADVISER), sum(a.CECOUNT) CECOUNT,sum(XSTOTAL) XSTOTAL, \n");
		 sql.append("		 sum(a.SCCOUNT) SCCOUNT, sum(a.CFTOTAL) CFTOTAL,sum(a.yxCOUNT) yxCOUNT , sum(a.zbCOUNT) zbCOUNT , sum(ddcount) ddcount,sum(a.sxCOUNT) sxCOUNT, sum(a.wxCOUNT) wxCOUNT \n");
		 sql.append("  FROM( \n");
		 sql.append("  SELECT XS.DEALER_ID ,XS.ADVISER ,SUM(xs.CECOUNT)as CECOUNT,SUM(XS.XSCOUNT) XSTOTAL,  \n");
		 sql.append("    SUM(DECODE(XS.SCCOUNT,NULL,0,XS.SCCOUNT)) as SCCOUNT, \n");
		 sql.append("    SUM(DECODE(XS.CFCOUNT,NULL,0,XS.CFCOUNT)) CFTOTAL, \n");
		 sql.append("    SUM(DECODE(XS.LEADS_STATUS,'60161001',XS.XSCOUNT,0)) yxCOUNT,  \n");
		 sql.append("    SUM(DECODE(XS.LEADS_STATUS,'60161002',XS.XSCOUNT,0)) zbCOUNT,  \n");
		 sql.append("    SUM(DECODE(XS.LEADS_STATUS,'60161003',XS.XSCOUNT,0)) sxCOUNT,  \n");
		 sql.append("    SUM(DECODE(XS.LEADS_STATUS,'60161004',XS.XSCOUNT,0)) wxCOUNT, \n");
		 sql.append("    sum(DECODE(XS.ddcount,NULL,0,XS.ddcount)) ddcount     \n");
		 sql.append(" FROM (  \n");
		 sql.append("         SELECT A.ADVISER, A.DEALER_ID , A.LEADS_STATUS, A.DNAME ,A.XSCOUNT, B.CFCOUNT , sc.SCCOUNT,ce.CECOUNT,sc.ddcount  \n");
		 sql.append("            FROM (   \n");
		 sql.append("                SELECT  TO_CHAR(A.DEALER_ID || A.ADVISER|| A.TELEPHONE  ) AS DEALER_ADVISER , \n");  
		 sql.append("                         A.DEALER_ID , A.ADVISER, A.LEADS_STATUS,MAX(A.DEALER_ID ) AS DNAME ,COUNT(1) AS XSCOUNT     \n");
		 sql.append("                 FROM (  \n");
		 sql.append("                       SELECT    B.TELEPHONE ,B.DEALER_ID , B.ADVISER, TL.LEADS_STATUS      \n");
		 sql.append("                       FROM  T_PC_LEADS TL ,T_PC_LEADS_ALLOT B  \n");
		 sql.append("		           ,T_PC_INTENT_VEHICLE tpiv  left join T_PC_INTENT_VEHICLE tpiv1  on tpiv.up_series_id=tpiv1.series_id  \n");
		 sql.append("                       WHERE   TL.LEADS_CODE = B.LEADS_CODE  AND   B.IF_CONFIRM  ='60321002'  AND B.IS_REPEAT ='10041002' \n");
		    sql.append("		         AND TL.INTENT_VEHICLE=TPIV.SERIES_ID    \n");
		    if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV1.SERIES_ID='"+seriesId+"'   \n" );
		    }
		 if(!CommonUtils.isNullString(startDate)){
            sql.append("     		  AND B.CONFIRM_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
		 	}
         if(!CommonUtils.isNullString(endDate)){
            sql.append("     		  AND B.CONFIRM_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
         	}
		 sql.append("                       )A  \n");
		 sql.append("                   GROUP BY A.DEALER_ID || A.ADVISER||  A.TELEPHONE   , A.DEALER_ID ,A.DEALER_ID , A.ADVISER, A.LEADS_STATUS  \n"); 
		 sql.append("                 )A left join (   \n");
		 sql.append("                   SELECT TO_CHAR(B.DEALER_ID || B.ADVISER||   B.TELEPHONE  ) AS DEALER_ADVISER,  \n");
		 sql.append("                           B.DEALER_ID , B.ADVISER, TL.LEADS_STATUS , COUNT(1)  AS CFCOUNT    \n");
		 sql.append("                     FROM T_PC_LEADS TL ,T_PC_LEADS_ALLOT B   \n");
		 sql.append("		           ,T_PC_INTENT_VEHICLE tpiv  left join T_PC_INTENT_VEHICLE tpiv1  on tpiv.up_series_id=tpiv1.series_id  \n");
		 sql.append("                   WHERE   TL.LEADS_CODE = B.LEADS_CODE AND B.IS_REPEAT ='10041001'   \n");
		    sql.append("		         AND TL.INTENT_VEHICLE=TPIV.SERIES_ID    \n");
		    if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV1.SERIES_ID='"+seriesId+"'   \n" );
		    }
		    if(!CommonUtils.isNullString(startDate)){
	            sql.append("     		  AND B.CONFIRM_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
	          }
	        if(!CommonUtils.isNullString(endDate)){
	            sql.append("     		  AND B.CONFIRM_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
	          }
		 
		 sql.append("                       GROUP BY B.DEALER_ID || B.ADVISER|| B.TELEPHONE  , B.DEALER_ID,B.ADVISER,TL.LEADS_STATUS  \n");
		 sql.append("                 )B on  A.DEALER_ADVISER=B.DEALER_ADVISER \n");
		 sql.append("                 left join ( \n");
		 sql.append("                  SELECT TO_CHAR(B.DEALER_ID || B.ADVISER||  B.TELEPHONE  ) AS DEALER_ADVISER, \n");    
		 sql.append("                       B.DEALER_ID , B.ADVISER, Tl.LEADS_STATUS,  COUNT(1) AS SCCOUNT ,count(tpo.order_id) ddcount \n");
		 sql.append("                 FROM  T_PC_LEADS_ALLOT B ,T_PC_INTENT_VEHICLE tpiv    \n");
		 sql.append("		            left join T_PC_INTENT_VEHICLE tpiv1  on tpiv.up_series_id=tpiv1.series_id  \n");
		 sql.append("    		   , T_PC_LEADS TL left join T_PC_ORDER tpo on tl.customer_id=tpo.customer_id  \n");
		 sql.append("                      WHERE   TL.LEADS_CODE = B.LEADS_CODE  AND tl.LEADS_STATUS='60161001' and tl.jc_way in ('60021001','60021003','60021004','60021008')  \n");
		    sql.append("		         AND TL.INTENT_VEHICLE=TPIV.SERIES_ID    \n");
		    if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV1.SERIES_ID='"+seriesId+"'   \n" );
		    }
		 sql.append("                   	AND B.IS_REPEAT ='10041002'  \n");
		 
			 if(!CommonUtils.isNullString(startDate)){
		            sql.append("     		  AND B.CONFIRM_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
	           }
	        if(!CommonUtils.isNullString(endDate)){
	            sql.append("     		  AND B.CONFIRM_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
	          }
		 sql.append("                       GROUP BY B.DEALER_ID || B.ADVISER||  B.TELEPHONE, B.DEALER_ID , B.ADVISER, Tl.LEADS_STATUS    \n");
		 
		 sql.append("                 )sc on A.DEALER_ADVISER=sc.DEALER_ADVISER \n");
		 sql.append("                 left join ( \n");
		 sql.append("                     SELECT TO_CHAR(B.DEALER_ID || B.ADVISER||  B.TELEPHONE  ) AS DEALER_ADVISER,   \n");
		 sql.append("                          B.DEALER_ID , B.ADVISER, Tl.LEADS_STATUS,  COUNT(1) AS CECOUNT   \n");
		 sql.append("                 FROM  T_PC_LEADS TL ,T_PC_LEADS_ALLOT B   \n");   
		 sql.append("		           ,T_PC_INTENT_VEHICLE tpiv  left join T_PC_INTENT_VEHICLE tpiv1  on tpiv.up_series_id=tpiv1.series_id  \n");
		 sql.append("                       WHERE   TL.LEADS_CODE = B.LEADS_CODE  and  ceil((b.allot_adviser_date - tl.collect_date)* 24 ) <= 24     \n");
		    sql.append("		         AND TL.INTENT_VEHICLE=TPIV.SERIES_ID    \n");
		    if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV1.SERIES_ID='"+seriesId+"'   \n" );
		    }
		 sql.append("                            AND B.IS_REPEAT ='10041002'   \n");
		 	if(!CommonUtils.isNullString(startDate)){
	            sql.append("     		  AND B.CONFIRM_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
	          }
	        if(!CommonUtils.isNullString(endDate)){
	            sql.append("     		  AND B.CONFIRM_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
	          }
		 sql.append("                      GROUP BY B.DEALER_ID || B.ADVISER|| B.TELEPHONE   , B.DEALER_ID ,  B.ADVISER, Tl.LEADS_STATUS    \n");
		 sql.append("              )CE on A.DEALER_ADVISER=CE.DEALER_ADVISER \n");
		 sql.append("                )XS  GROUP BY XS.DEALER_ID|| XS.ADVISER,XS.DEALER_ID,XS.DNAME ,XS.ADVISER   \n");
		 sql.append("              )A   group by a.DEALER_ID \n");
		 sql.append("                )A   left join VW_ORG_DEALER_ALL vw   on a.DEALER_ID =to_char(vw.DEALER_ID)  \n");
		 sql.append("               where 1=1  \n");
		    //经销商
		if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
			sql.append("  AND (VW.DEALER_ID ="+dealerId+"  )\n");
			//大区
		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND VW.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
			//小区
		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND VW.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.PQ_ORG_ID="+orgId+") \n");
		}
		if(!"".equals(dealerCode)&&dealerCode!=null){
            String arr[]=dealerCode.split(",");
            String str="";

            for(int i=0;i<arr.length;i++){
                str+="'"+arr[i]+"',";
            }
            str=str.substring(0,str.lastIndexOf(","));

			sql.append("AND VW.DEALER_CODE IN("+str+")\n");
		}
		 sql.append("     order by vw.DEALER_ID                  ");
		 
		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	//经销商
	  public List<Map<String,Object>> getNameList(String codeID){
	        StringBuilder sql=new StringBuilder();
	        sql.append("select code_id,code_desc from tc_code a where 1=1 "); 
	        sql.append("   and a.status='10011001' "); 
	        if(!CommonUtils.isNullString(codeID))
	        {
	        	sql.append(" and  type='"+codeID+"' ");
	        	
	        }else{
	        	
	        	sql.append(" and  type in ('6015','6016') "); 
	        	
	        }
	        sql.append(" order by code_id");
	        return dao.pageQuery(sql.toString(),null,null);
	   }
}
