package com.infodms.dms.dao.crm.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ClueReportDao extends BaseDao<PO>{
private static final ClueReportDao dao = new ClueReportDao();
private ActionContext act = ActionContext.getContext();
RequestWrapper request = act.getRequest();
	public static final ClueReportDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	//  经销商
	public List<Map<String, Object>> getClueReportDaoSelect(Map<String, String> map){
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
		StringBuilder sql= new StringBuilder();
		
		  sql.append(" select rownum, tmp.* from (with a as ( \n");
		  sql.append(" select  vw.ROOT_ORG_NAME,vw.PQ_ORG_NAME,vw.DEALER_CODE,vw.DEALER_SHORTNAME,xs.DNAME,tpg.group_name,xs.group_id,xs.LYTOTAL,(xs.XSTOTAL+ xs.WCLTOTAL) xstotal, \n");
 			List  allList= dao.getNameList("");
			for(Map ma : dao.getNameList("")){
				sql.append(" xs.COUNT"+ma.get("CODE_ID")+", \n"); 
				sql.append(" xs.B"+ma.get("CODE_ID")+", \n"); 
			}
			sql.append(" xs.WCLTOTAL from (   \n");
		    sql.append(" SELECT TS.DEALER_ID, TS.ADVISER, tu.name dname,tu.group_id,  SUM(TS.ZRLD) AS LYTOTAL,   \n");
		    for(Map m : dao.getNameList("6015")){
				sql.append("  SUM(DECODE(TS.LO,'"+m.get("CODE_ID")+"',TS.ZRLD,0))  COUNT"+m.get("CODE_ID")+", \n"); 
				sql.append("  ROUND(decode(SUM(DECODE(TS.LO,'"+m.get("CODE_ID")+"',TS.ZRLD,0)),0,0,(SUM(DECODE(TS.LO,'"+m.get("CODE_ID")+"',TS.ZRLD,0))/SUM(TS.ZRLD))*100),2) || '%' as  B"+m.get("CODE_ID")+" , \n"); 
			}
		    for(Map m : dao.getNameList("6016")){
				sql.append("		             SUM(DECODE(TS.LEADS_STATUS,'"+m.get("CODE_ID")+"',ts.XSCOUNT,0)) COUNT"+m.get("CODE_ID")+", \n");
				sql.append(" ROUND(decode(SUM(DECODE(TS.LEADS_STATUS,'"+m.get("CODE_ID")+"',ts.XSCOUNT,0)),0,0,(SUM(DECODE(ts.LEADS_STATUS,'"+m.get("CODE_ID")+"',ts.XSCOUNT,0))/SUM(ts.XSCOUNT))*100),2) || '%' as  B"+m.get("CODE_ID")+"  , \n");
			}
		    sql.append("  SUM(TS.XSCOUNT) XSTOTAL, SUM(DECODE(TS.WCLCOUNT,NULL,0,TS.WCLCOUNT)) WCLTOTAL   \n");
		   
		    sql.append("      from  ( \n");
		    sql.append("     SELECT   B.DEALER_ID ,B.ADVISER ,tl.leads_status,  0 XSCOUNT ,0 WCLCOUNT, COUNT(1) AS ZRLD ,TL.LEADS_ORIGIN  AS LO  \n"); 
		    sql.append("      FROM   T_PC_LEADS_ALLOT B,T_PC_LEADS TL    \n"); 
		    sql.append("   left JOIN T_PC_INTENT_VEHICLE TPIV  ON TL.INTENT_VEHICLE = TPIV.SERIES_ID  \n");

		    sql.append("    WHERE   TL.LEADS_CODE = B.LEADS_CODE  and B.IF_CONFIRM  ='60321002'      \n"); 
		    if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
		    }
		    if(!CommonUtils.isNullString(startDate)){
	            sql.append("     		  AND TL.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
			 	}
	         if(!CommonUtils.isNullString(endDate)){
	            sql.append("     		  AND TL.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
	         	}
		    
		    sql.append("    GROUP BY TL.LEADS_ORIGIN,B.DEALER_ID,ADVISER,tl.leads_status,TL.LEADS_ORIGIN    \n"); 

		    sql.append("    union  all   \n"); 

		    sql.append("   select b.DEALER_ID,b.ADVISER,b.LEADS_STATUS ,b.XSCOUNT,b.WCLCOUNT,0 ZRLD,b.LEADS_ORIGIN  AS LO  \n"); 
		    sql.append("    from ( \n"); 
		    sql.append("   SELECT   A.DEALER_ID , A.ADVISER, A.LEADS_STATUS,MAX(A.DEALER_ID ) AS DNAME ,COUNT(1) AS XSCOUNT ,0 WCLCOUNT ,a.LEADS_ORIGIN  \n");     
		    sql.append("       FROM (  SELECT B.CUSTOMER_NAME,B.TELEPHONE ,B.DEALER_ID , B.ADVISER, TL.LEADS_STATUS,tl.LEADS_ORIGIN    \n"); 
		    sql.append("      FROM   T_PC_LEADS_ALLOT B,T_PC_LEADS TL    \n"); 
		    sql.append("   left JOIN T_PC_INTENT_VEHICLE TPIV  ON TL.INTENT_VEHICLE = TPIV.SERIES_ID  \n");

		    sql.append("           WHERE   TL.LEADS_CODE = B.LEADS_CODE   AND  B.IF_CONFIRM  ='60321002'    \n");
		    if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
		    }
		    if(!CommonUtils.isNullString(startDate)){
	            sql.append("     		  AND TL.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
			 	}
	         if(!CommonUtils.isNullString(endDate)){
	            sql.append("     		  AND TL.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
	         	}

		    sql.append("          )A  \n"); 
		    sql.append("       GROUP BY   A.DEALER_ID   , A.ADVISER, A.LEADS_STATUS ,a.LEADS_ORIGIN  \n");  

		    sql.append("      union  all     \n"); 

		    sql.append("      SELECT B.DEALER_ID , B.ADVISER, TL.LEADS_STATUS ,MAX(b.DEALER_ID ) AS DNAME,0 XSCOUNT ,COUNT(1) AS WCLCOUNT ,tl.LEADS_ORIGIN \n");      
		    sql.append("      FROM   T_PC_LEADS_ALLOT B,T_PC_LEADS TL    \n"); 
		    sql.append("   left JOIN T_PC_INTENT_VEHICLE TPIV  ON TL.INTENT_VEHICLE = TPIV.SERIES_ID  \n");

		    sql.append("      WHERE   TL.LEADS_CODE = B.LEADS_CODE  AND  B.IF_CONFIRM  ='60321001'  \n");
		    if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
		    }
		    if(!CommonUtils.isNullString(startDate)){
	            sql.append("     		  AND TL.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
			 	}
	         if(!CommonUtils.isNullString(endDate)){
	            sql.append("     		  AND TL.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
	         	}
		    
		    sql.append("    GROUP BY  B.DEALER_ID,B.ADVISER,TL.LEADS_STATUS ,tl.LEADS_ORIGIN   \n");  
		    sql.append("   )b \n"); 
		    sql.append("  )ts   \n"); 
		  
		    sql.append("  , tc_user tu    \n"); 
		    sql.append("   where ts.ADVISER= tu.user_id    \n"); 
		    
		    if(!CommonUtils.isNullString(userId)) {
				sql.append(" AND ts.ADVISER  IN ("+userId+") \n");
			}
		   
		    sql.append(" group by TS.DEALER_ID, TS.ADVISER ,tu.NAME,tu.group_id \n"); 
		    
		    
		    sql.append(" )xs left join VW_ORG_DEALER_ALL vw   on xs.DEALER_ID =to_char(vw.DEALER_ID)  ,t_pc_group tpg   \n"); 
		    sql.append("  WHERE    xs.group_id=tpg.group_id  \n"); 
		    
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
		sql.append("     order by VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME,tpg.group_name               ");
		sql.append("  )  select   a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.DEALER_CODE,a.DEALER_SHORTNAME,'2' as a ,a.DNAME,to_char(a.group_id) group_id,a.group_name,a.LYTOTAL,a.xstotal,             ");
		
		for(int i=0; i<allList.size() ;i++){
			Map  ma = (Map) allList.get(i);
				sql.append(" a.COUNT"+ma.get("CODE_ID")+", \n"); 
				sql.append(" a.B"+ma.get("CODE_ID")+", \n"); 
		}
		  sql.append(" a.WCLTOTAL  from  a ");
		   
		  if(!CommonUtils.isNullString(flag)) {
			  sql.append("   union all   "); 
			  sql.append("   select a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.DEALER_CODE,a.DEALER_SHORTNAME,'1' a,a.group_name||'小计',to_char(a.group_id) group_id,a.group_name,sum(a.LYTOTAL),SUM(a.xstotal),    ");
			  for(int i=0; i<allList.size() ;i++){
					Map  ma = (Map) allList.get(i);
						sql.append(" sum(a.COUNT"+ma.get("CODE_ID")+"), \n"); 
						sql.append(" DECODE(  sum(a.COUNT"+ma.get("CODE_ID")+"),0,'0',ROUND( sum(a.COUNT"+ma.get("CODE_ID")+")*100/sum(a.LYTOTAL),2) ) || '%' as B"+ma.get("CODE_ID")+", \n"); 
					 
				}
			  sql.append(" sum(a.WCLTOTAL) WCLTOTAL  \n");
			  sql.append(" from a group by    a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.DEALER_CODE,a.DEALER_SHORTNAME, a.group_name,a.group_id \n");
		     }
			  if(!CommonUtils.isNullString(manager)){
			
			  sql.append("   union all   ");
			  sql.append("   select a.ROOT_ORG_NAME,a.PQ_ORG_NAME,max(a.DEALER_CODE),max(a.DEALER_SHORTNAME),'3' a,max(a.DEALER_SHORTNAME)||'总计',max(to_char(a.group_id)) group_id,' ',sum(a.LYTOTAL),SUM(a.xstotal),    ");
			  for(int i=0; i<allList.size() ;i++){
					Map  ma = (Map) allList.get(i);
						sql.append(" sum(a.COUNT"+ma.get("CODE_ID")+"), \n"); 
						sql.append(" DECODE(  sum(a.COUNT"+ma.get("CODE_ID")+"),0,'0',ROUND( sum(a.COUNT"+ma.get("CODE_ID")+")*100/sum(a.LYTOTAL),2) ) || '%' as B"+ma.get("CODE_ID")+", \n"); 
					 
				}
			  sql.append(" sum(a.WCLTOTAL) WCLTOTAL  \n");
			  sql.append(" from a group by    a.ROOT_ORG_NAME,a.PQ_ORG_NAME  \n");
			  }
		 
		  sql.append("    ) tmp  order by tmp.ROOT_ORG_NAME,tmp.PQ_ORG_NAME,tmp.DEALER_CODE, tmp.a ,tmp.group_name   \n");
		  
		  
		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	//经销商 -- 车厂
	public List<Map<String, Object>> getClueReportDaoSelectAll(Map<String, String> map){
		String startDate=map.get("startDate");
		String endDate=map.get("endDate");
		String dealerCode=map.get("dealerCode");
		String dealerId=map.get("dealerId");
		String dutyType=map.get("dutyType");
		String orgId=map.get("orgId");
		String seriesId=map.get("seriesId");
		StringBuilder sql= new StringBuilder();
		 sql.append(" select rownum, tmp.* from (with a as ( \n");
		  sql.append(" select  to_char(vw.DEALER_ID) DEALER_ID ,vw.ROOT_ORG_NAME,vw.PQ_ORG_NAME,vw.DEALER_CODE,vw.DEALER_SHORTNAME, xs.LYTOTAL,(xs.XSTOTAL+ xs.WCLTOTAL) xstotal, \n");
			List  allList= dao.getNameList("");
			for(Map ma : dao.getNameList("")){
				sql.append(" xs.COUNT"+ma.get("CODE_ID")+", \n"); 
				sql.append(" xs.B"+ma.get("CODE_ID")+", \n"); 
			}
			sql.append(" xs.WCLTOTAL from (   \n");
		    sql.append(" SELECT TS.DEALER_ID,    SUM(TS.ZRLD) AS LYTOTAL,   \n");
		    for(Map m : dao.getNameList("6015")){
				sql.append("  SUM(DECODE(TS.LO,'"+m.get("CODE_ID")+"',TS.ZRLD,0))  COUNT"+m.get("CODE_ID")+", \n"); 
				sql.append("  ROUND(decode(SUM(DECODE(TS.LO,'"+m.get("CODE_ID")+"',TS.ZRLD,0)),0,0,(SUM(DECODE(TS.LO,'"+m.get("CODE_ID")+"',TS.ZRLD,0))/SUM(TS.ZRLD))*100),2) || '%' as  B"+m.get("CODE_ID")+" , \n"); 
			}
		    for(Map m : dao.getNameList("6016")){
				sql.append("		             SUM(DECODE(TS.LEADS_STATUS,'"+m.get("CODE_ID")+"',ts.XSCOUNT,0)) COUNT"+m.get("CODE_ID")+", \n");
				sql.append(" ROUND(decode(SUM(DECODE(TS.LEADS_STATUS,'"+m.get("CODE_ID")+"',ts.XSCOUNT,0)),0,0,(SUM(DECODE(ts.LEADS_STATUS,'"+m.get("CODE_ID")+"',ts.XSCOUNT,0))/SUM(ts.XSCOUNT))*100),2) || '%' as  B"+m.get("CODE_ID")+"  , \n");
			}
		    sql.append("  SUM(TS.XSCOUNT) XSTOTAL, SUM(DECODE(TS.WCLCOUNT,NULL,0,TS.WCLCOUNT)) WCLTOTAL  \n");
		    
		    sql.append("    from  (   SELECT   B.DEALER_ID ,B.ADVISER ,tl.leads_status,  0 XSCOUNT ,0 WCLCOUNT, COUNT(1) AS ZRLD ,TL.LEADS_ORIGIN  AS LO  \n"); 
		    sql.append("      FROM   T_PC_LEADS_ALLOT B,T_PC_LEADS TL    \n"); 
		    sql.append("   left JOIN T_PC_INTENT_VEHICLE TPIV  ON TL.INTENT_VEHICLE = TPIV.SERIES_ID  \n");
		    sql.append("    WHERE   TL.LEADS_CODE = B.LEADS_CODE  and B.IF_CONFIRM  ='60321002'      \n"); 
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
	         if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
			    }
		    sql.append("    GROUP BY TL.LEADS_ORIGIN,B.DEALER_ID,ADVISER,tl.leads_status,TL.LEADS_ORIGIN      \n"); 

		    sql.append("   union  all     \n"); 

		    sql.append("   select b.DEALER_ID,b.ADVISER,b.LEADS_STATUS ,b.XSCOUNT,b.WCLCOUNT,0 ZRLD,b.LEADS_ORIGIN  AS LO   \n"); 
		    sql.append("    from ( \n"); 
		    sql.append("   SELECT   A.DEALER_ID , A.ADVISER, A.LEADS_STATUS,MAX(A.DEALER_ID ) AS DNAME ,COUNT(1) AS XSCOUNT ,0 WCLCOUNT ,a.LEADS_ORIGIN \n");     
		    sql.append("       FROM (  SELECT B.CUSTOMER_NAME,B.TELEPHONE ,B.DEALER_ID , B.ADVISER, TL.LEADS_STATUS,tl.LEADS_ORIGIN    \n"); 
		    sql.append("      FROM   T_PC_LEADS_ALLOT B,T_PC_LEADS TL    \n"); 
		    sql.append("   left JOIN T_PC_INTENT_VEHICLE TPIV  ON TL.INTENT_VEHICLE = TPIV.SERIES_ID  \n");
		    sql.append("           WHERE   TL.LEADS_CODE = B.LEADS_CODE    AND  B.IF_CONFIRM  ='60321002'    \n"); 
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
	         if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
			    }
		    sql.append("          )A  \n"); 
		    sql.append("       GROUP BY   A.DEALER_ID   , A.ADVISER, A.LEADS_STATUS ,a.LEADS_ORIGIN   \n");  

		    sql.append("        union  all     \n"); 

		    sql.append("      SELECT B.DEALER_ID , B.ADVISER, TL.LEADS_STATUS ,MAX(b.DEALER_ID ) AS DNAME,0 XSCOUNT ,COUNT(1) AS WCLCOUNT ,tl.LEADS_ORIGIN  \n");      
		    sql.append("      FROM   T_PC_LEADS_ALLOT B,T_PC_LEADS TL    \n"); 
		    sql.append("   left JOIN T_PC_INTENT_VEHICLE TPIV  ON TL.INTENT_VEHICLE = TPIV.SERIES_ID  \n");
		    sql.append("      WHERE   TL.LEADS_CODE = B.LEADS_CODE  AND  B.IF_CONFIRM  ='60321001'  \n");
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
	        if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
		    }
		    
		    sql.append("    GROUP BY  B.DEALER_ID,B.ADVISER,TL.LEADS_STATUS ,tl.LEADS_ORIGIN    \n");  
		    sql.append("   )b \n"); 
		    sql.append("  )ts     \n"); 
		  
		    
		    sql.append(" group by TS.DEALER_ID \n"); 
		 
		    
		    sql.append(" )xs left join VW_ORG_DEALER_ALL vw   on xs.DEALER_ID =to_char(vw.DEALER_ID)     \n"); 
		    sql.append("  WHERE    1=1  \n"); 
		    
		    //经销商
		if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
			sql.append("  AND (VW.DEALER_ID ="+dealerId+")\n");
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
		sql.append("     order by VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE  \n");;
		
	    sql.append("  )  select a.DEALER_ID,  a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.DEALER_CODE,a.DEALER_SHORTNAME,'3' as a ,a.LYTOTAL,a.xstotal, \n");
		
		for(int i=0; i<allList.size() ;i++){
			Map  ma = (Map) allList.get(i);
				sql.append(" a.COUNT"+ma.get("CODE_ID")+", \n"); 
				sql.append(" a.B"+ma.get("CODE_ID")+", \n"); 
		}
		  sql.append(" a.WCLTOTAL  from  a  \n");
		   
		  if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)||Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				  sql.append("   union all    \n"); 
				  sql.append("   select '',a.ROOT_ORG_NAME,a.PQ_ORG_NAME,' ',a.PQ_ORG_NAME||'小计','3' a, sum(a.LYTOTAL),SUM(a.xstotal),   \n");
				  for(int i=0; i<allList.size() ;i++){
						Map  ma = (Map) allList.get(i);
							sql.append(" sum(a.COUNT"+ma.get("CODE_ID")+"), \n"); 
							sql.append(" DECODE(  sum(a.COUNT"+ma.get("CODE_ID")+"),0,'0',ROUND( sum(a.COUNT"+ma.get("CODE_ID")+")*100/sum(a.LYTOTAL),2) ) || '%' as B"+ma.get("CODE_ID")+", \n"); 
						 
					}
				  sql.append(" sum(a.WCLTOTAL) WCLTOTAL  \n");
				  sql.append(" from a group by  a.ROOT_ORG_NAME,a.PQ_ORG_NAME   \n");
			    //小区
			 }  
		    if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				  sql.append("   union all   ");
				  sql.append("   select '',a.ROOT_ORG_NAME,' ',' ',max(a.ROOT_ORG_NAME)||'合计','2' a,sum(a.LYTOTAL),SUM(a.xstotal),    ");
				  for(int i=0; i<allList.size() ;i++){
						Map  ma = (Map) allList.get(i);
							sql.append(" sum(a.COUNT"+ma.get("CODE_ID")+"), \n"); 
							sql.append(" DECODE(  sum(a.COUNT"+ma.get("CODE_ID")+"),0,'0',ROUND( sum(a.COUNT"+ma.get("CODE_ID")+")*100/sum(a.LYTOTAL),2) ) || '%' as B"+ma.get("CODE_ID")+", \n"); 
						 
					}
				  sql.append(" sum(a.WCLTOTAL) WCLTOTAL  \n");
				  sql.append(" from a group by  a.ROOT_ORG_NAME  \n");
			  }  
		    if( Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				  sql.append("   union all   ");
				  sql.append("   select '',' ',' ',' ','全国总计','1' a,sum(a.LYTOTAL),SUM(a.xstotal),    ");
				  for(int i=0; i<allList.size() ;i++){
						Map  ma = (Map) allList.get(i);
							sql.append(" sum(a.COUNT"+ma.get("CODE_ID")+"), \n"); 
							sql.append(" DECODE(  sum(a.COUNT"+ma.get("CODE_ID")+"),0,'0',ROUND( sum(a.COUNT"+ma.get("CODE_ID")+")*100/sum(a.LYTOTAL),2) ) || '%' as B"+ma.get("CODE_ID")+", \n"); 
						 
					}
				  sql.append(" sum(a.WCLTOTAL) WCLTOTAL  \n");
				  sql.append(" from a   \n");
			  }
		  sql.append("     ) tmp  order by tmp.a,tmp.ROOT_ORG_NAME, tmp.pq_org_name ,rownum   \n");

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
	  //公司
	  public List<Map<String,Object>> getNameCompanyList(String codeID){
		  StringBuilder sql=new StringBuilder();
	        sql.append("select code_id,code_desc from tc_code a where 1=1 "); 
	        sql.append("   and a.status='10011001' "); 
	        if(!CommonUtils.isNullString(codeID))
	        {
	        	sql.append(" and  type in ('"+codeID+"') ");
	        	
	        }else{
	        	
	        	sql.append(" and  type in ('60151001','6016') "); 
	        	
	        }
	        sql.append(" order by code_id");
	        return dao.pageQuery(sql.toString(),null,null);
	   }
	  public List<Map<String, Object>> getClueReportCompanySelect(Map<String, String> map){
			String startDate=map.get("startDate");
			String endDate=map.get("endDate");
			String dealerCode=map.get("dealerCode");
			String dealerId=map.get("dealerId");
			String dutyType=map.get("dutyType");
			String orgId=map.get("orgId");
			StringBuilder sql= new StringBuilder();
			
			
				sql.append("  select  vw.ROOT_ORG_NAME,vw.PQ_ORG_NAME,vw.DEALER_CODE,vw.DEALER_SHORTNAME,LY.DNAME,ly.LYTOTAL,(xs.XSTOTAL+ xs.CFTOTAL) xstotal, \n");
				List  allList= dao.getNameCompanyList("");
				for(int i=0; i<allList.size() ;i++){
					int size = dao.getNameCompanyList("60151001").size();
					Map  ma = (Map) allList.get(i);
					if(size > i ){
						sql.append(" ly.COUNT"+ma.get("CODE_ID")+", \n"); 
						sql.append(" ly.B"+ma.get("CODE_ID")+", \n"); 
					}else{
						sql.append(" xs.COUNT"+ma.get("CODE_ID")+", \n"); 
						sql.append(" xs.B"+ma.get("CODE_ID")+", \n"); 
					}
				}
				sql.append(" xs.CFTOTAL FROM(  \n");
			    sql.append("		   SELECT TS.DEALER_ID, TS.ADVISER,SUM(TS.ZRLDCOUNT) AS LYTOTAL, \n");
			    for(Map m : dao.getNameCompanyList("60151001")){
					sql.append("  SUM(DECODE(TS.LEADS_ORIGIN,'"+m.get("CODE_ID")+"',TS.ZRLDCOUNT,0))  COUNT"+m.get("CODE_ID")+", \n"); 
					sql.append("  ROUND(decode(SUM(DECODE(TS.LEADS_ORIGIN,'"+m.get("CODE_ID")+"',TS.ZRLDCOUNT,0)),0,0,(SUM(DECODE(TS.LEADS_ORIGIN,'"+m.get("CODE_ID")+"',TS.ZRLDCOUNT,0))/SUM(TS.ZRLDCOUNT))*100)) || '%' as  B"+m.get("CODE_ID")+" , \n"); 
				}
			    sql.append("      TS.DNAME    FROM ( \n");
			    sql.append("		         SELECT TO_CHAR(B.DEALER_ID  ||   B.TELEPHONE ) AS DEALER_ADVISER, B.DEALER_ID ,B.ADVISER , tu.name AS DNAME, \n");  
			    sql.append("		                COUNT(1) AS ZRLDCOUNT ,TC.CODE_ID  AS LEADS_ORIGIN \n");
			    sql.append("		          FROM  TC_CODE TC, T_PC_LEADS TL , tc_user tu,T_PC_LEADS_ALLOT B LEFT JOIN TM_DEALER C ON B.DEALER_ID = C.DEALER_ID \n");
			    sql.append("		           WHERE   b.adviser=tu.user_id   and  TC.CODE_ID = TL.LEADS_ORIGIN AND TL.LEADS_CODE = B.LEADS_CODE AND TC.TYPE='60151002' \n");
			    if(!CommonUtils.isNullString(startDate)){
		            sql.append("     		  AND B.CONFIRM_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
		        }
		        if(!CommonUtils.isNullString(endDate)){
		            sql.append("     		  AND B.CONFIRM_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
		        }
			    sql.append("		          GROUP BY B.DEALER_ID  || B.TELEPHONE ,TC.CODE_ID,B.DEALER_ID,ADVISER,tu.name \n");
			    sql.append("		         )TS  GROUP BY TS.DEALER_ID, TS.ADVISER, TS.DNAME \n");
			    sql.append("		         )LY, \n");
			    sql.append("		       (  \n");
			    sql.append("		        SELECT XS.DEALER_ID ,XS.ADVISER , SUM(XS.XSCOUNT) XSTOTAL, \n");
			    for(Map m : dao.getNameList("6016")){
					sql.append("		             SUM(DECODE(XS.LEADS_STATUS,'"+m.get("CODE_ID")+"',XS.XSCOUNT,0)) COUNT"+m.get("CODE_ID")+", \n");
					sql.append(" ROUND(decode(SUM(DECODE(XS.LEADS_STATUS,'"+m.get("CODE_ID")+"',XS.XSCOUNT,0)),0,0,(SUM(DECODE(XS.LEADS_STATUS,'"+m.get("CODE_ID")+"',XS.XSCOUNT,0))/SUM(XS.XSCOUNT))*100)) || '%' as  B"+m.get("CODE_ID")+"  , \n");
				}
			    sql.append("		             SUM(DECODE(XS.CFCOUNT,NULL,0,XS.CFCOUNT)) CFTOTAL \n");
			    sql.append("		       FROM ( \n");
			    sql.append("		              SELECT A.ADVISER, A.DEALER_ID , A.LEADS_STATUS, A.DNAME ,A.XSCOUNT, B.CFCOUNT  \n");
			    sql.append("		         FROM ( \n");
			    sql.append("		             SELECT  TO_CHAR(A.DEALER_ID || A.ADVISER|| A.CUSTOMER_NAME || A.TELEPHONE  ) AS DEALER_ADVISER ,  \n");
			    sql.append("		                      A.DEALER_ID , A.ADVISER, A.LEADS_STATUS,MAX(A.DEALER_ID   ) AS DNAME ,COUNT(1) AS XSCOUNT, 0 AS CFCOUNT  \n");
			    sql.append("		              FROM ( \n");
			    sql.append("		                    SELECT DISTINCT    B.CUSTOMER_NAME,B.TELEPHONE ,B.DEALER_ID , B.ADVISER, TL.LEADS_STATUS    \n");
			    sql.append("		                    FROM TC_CODE TC, T_PC_LEADS TL ,T_PC_LEADS_ALLOT B  \n");
			    sql.append("		                    WHERE TC.CODE_ID = TL.LEADS_ORIGIN AND TL.LEADS_CODE = B.LEADS_CODE \n");
			    sql.append("		         --                 AND TL.LEADS_STATUS IS NOT NULL  \n");
			    sql.append("		                          AND   B.IF_CONFIRM  ='60321002' \n");
			    if(!CommonUtils.isNullString(startDate)){
		            sql.append("     		  				  AND B.CONFIRM_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
		        }
		        if(!CommonUtils.isNullString(endDate)){
		            sql.append("     		 				  AND B.CONFIRM_DATE <=  TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
		        }
			    
			    sql.append("		                    )A \n");
			    sql.append("		                GROUP BY A.DEALER_ID || A.ADVISER|| A.CUSTOMER_NAME || A.TELEPHONE   , A.DEALER_ID ,A.DEALER_ID , A.ADVISER, A.LEADS_STATUS \n");
			    sql.append("		              )A left join (  \n");
			    sql.append("		               SELECT TO_CHAR(B.DEALER_ID || B.ADVISER|| B.CUSTOMER_NAME || B.TELEPHONE  ) AS DEALER_ADVISER, \n"); 
			    sql.append("		                        B.DEALER_ID , B.ADVISER, TL.LEADS_STATUS , COUNT(1) AS CFCOUNT   \n");
			    sql.append("		                FROM TC_CODE TC, T_PC_LEADS TL ,T_PC_LEADS_ALLOT B , T_PC_CONTACT_POINT tcp,T_PC_CUSTOMER tpc \n");
			    sql.append("		                WHERE TC.CODE_ID = TL.LEADS_ORIGIN AND TL.LEADS_CODE = B.LEADS_CODE  and tcp.ctm_id=tpc.customer_id\n");
			    sql.append("		                  and  b.dealer_id  || b.adviser ||B.CUSTOMER_NAME || b.telephone = tcp.dealer_id  || tcp.adviser ||tpc.customer_name ||tpc.telephone  \n");
			    sql.append("		                 and tcp.POINT_CONTENT = '公司类重复线索' \n");
			    sql.append("		                      --AND   B.IF_CONFIRM  != '60321002' \n");
			    sql.append("		                   AND  TC.CODE_ID = TL.LEADS_ORIGIN AND TL.LEADS_CODE = B.LEADS_CODE AND TC.TYPE='60151002' \n");
			    if(!CommonUtils.isNullString(startDate)){
		            sql.append("     		  			AND B.CONFIRM_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
		        }
		        if(!CommonUtils.isNullString(endDate)){
		            sql.append("     		  			AND B.CONFIRM_DATE <=  TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
		        }
			    sql.append("		                    GROUP BY B.DEALER_ID || B.ADVISER|| B.CUSTOMER_NAME || B.TELEPHONE  , B.DEALER_ID,B.ADVISER,TL.LEADS_STATUS \n");
			    sql.append("		              )B on A.DEALER_ADVISER=B.DEALER_ADVISER \n" );
			    
			    
				sql.append("		             )XS \n");
			    sql.append("		             GROUP BY XS.DEALER_ID|| XS.ADVISER,XS.DEALER_ID,XS.DNAME ,XS.ADVISER  \n");
			    sql.append("		             )XS  left join VW_ORG_DEALER_ALL vw   on xs.DEALER_ID =to_char(vw.DEALER_ID)  \n");
			    sql.append("		      WHERE LY.DEALER_ID || LY.ADVISER = XS.DEALER_ID || XS.ADVISER \n");
			
			    //经销商
			if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
				sql.append("  AND (VW.DEALER_ID ="+dealerId+" or vw.DEALER_CODE in("+CommonUtils.getGroupDealerCodes(dealerId)+") )\n");
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
	  public List<Map<String,Object>> getSeriesList(){
	        StringBuilder sql=new StringBuilder();
	        sql.append("     SELECT SERIES_ID,SERIES_NAME  FROM  T_PC_INTENT_VEHICLE WHERE STATUS=10011001  AND UP_SERIES_ID IS NULL   order by is_foreign desc ");
	        return dao.pageQuery(sql.toString(),null,null);
	    }
	  public PageResult<Map<String, Object>> getDealerCode(Map<String, String> map, Integer pageSize, Integer curPage){
		  StringBuilder sql=new StringBuilder();
		  String userId=map.get("userId");
		  String dealer_id=map.get("dealerId");
		  TcUserPO tc=new TcUserPO();
		  tc.setUserId(Long.parseLong(userId));
		  tc=(TcUserPO) dao.select(tc).get(0);
		  String poseRank=tc.getPoseRank().toString();
		  sql.append("    select b.par_dealer_id,b.dealer_codes,vw.DEALER_SHORTNAME  from t_pc_company_group b LEFT JOIN VW_ORG_DEALER_ALL VW  ");
		  sql.append(" on b.dealer_codes=vw.DEALER_CODE  where 1=1  ");
		  if(CommonUtils.judgeMgrLogin(userId) || "60281001".equals(poseRank)){//ture 为经理 false 
			  sql.append(" and  b.par_dealer_id='"+dealer_id+"'  ");
		  }else {
			  sql.append(" and  b.par_dealer_id='-1'  ");
		  }
		  PageResult<Map<String, Object>> ps=dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		  return ps;
	  }
		 public PageResult<Map<String, Object>> getSeriesListAll(Map<String, String> map, Integer pageSize, Integer curPage){

	        StringBuilder sql=new StringBuilder();
	        sql.append(" SELECT SERIES_ID,SERIES_NAME,decode(IS_FOREIGN, 10041001, '进口车', 10041002, '国产车') as IS_FOREIGN  FROM  T_PC_INTENT_VEHICLE WHERE STATUS=10011001  AND UP_SERIES_ID IS NULL   order by is_foreign  ");
			  PageResult<Map<String, Object>> ps=dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
			  return ps;

	    }
		 
		 
		 public PageResult<Map<String, Object>> getLeadsOriginListAll(Map<String, String> map, Integer pageSize, Integer curPage){

		        StringBuilder sql=new StringBuilder();
		        sql.append("  select tc.code_id,tc.code_desc from tc_code tc where type='6015' and STATUS=10011001 order by tc.code_id ");
				  PageResult<Map<String, Object>> ps=dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
				  return ps;

		    }
		 
}
