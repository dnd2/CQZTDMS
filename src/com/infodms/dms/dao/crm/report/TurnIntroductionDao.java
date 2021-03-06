package com.infodms.dms.dao.crm.report;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

 
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

public class TurnIntroductionDao extends BaseDao<PO>{
private static final TurnIntroductionDao dao = new TurnIntroductionDao();
private ActionContext act = ActionContext.getContext();
RequestWrapper request = act.getRequest();
	public static final TurnIntroductionDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public List<Map<String, Object>> getStatisticsDaoSelect(Map<String, String> map) throws UnsupportedEncodingException{
		String startDate=map.get("startDate");
		String endDate=map.get("endDate");
		String dealerCode=map.get("dealerCode");
		String dealerId=map.get("dealerId");
		String dutyType=map.get("dutyType");
		String userId=map.get("userId");
		String flag=map.get("flag");
		String orgId=map.get("orgId");
		String manager=map.get("manager");
		String seriesId=map.get("seriesId");
		StringBuilder sql= new StringBuilder(); 			
		sql.append("     select * from (with a as (    \n");
	     sql.append("   SELECT  VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME,tu.name, tpg.group_id,tpg.group_name,  \n");
	 	 sql.append(" JD.DEALER_ID,JD.ADVISER, to_number(JD.DDCOUNT) DDCOUNT, \n");
	     sql.append(" to_number(JD.LKCOUNT) LKCOUNT,DECODE(JD.DDCOUNT,0,'0',DECODE(JD.LKCOUNT,0,'0',ROUND(JD.LKCOUNT*100/JD.DDCOUNT,2))) || '%' LKRATE,   \n");
	     sql.append(" to_number(JD.QBCOUNT) QBCOUNT,DECODE(JD.DDCOUNT,0,'0',DECODE(JD.QBCOUNT,0,'0',ROUND(JD.QBCOUNT*100/JD.DDCOUNT,2))) || '%' QBRATE ");
	     
	     
	     
	     
	     
	  	 sql.append("  FROM (  SELECT JD.ADVISER ,TO_CHAR(JD.DEALER_ID) DEALER_ID,  \n");
	     sql.append(" 	  SUM(JD.LK) LKCOUNT,SUM(JD.QB) QBCOUNT,SUM(JD.DD) DDCOUNT    \n");
	     
	     sql.append("  FROM (  select  to_char(adviser) adviser,dealer_id,sum(num) LK,0 QB,0 DD  from (   \n");
	     sql.append("	 select to_char(adviser) adviser,A.dealer_id,A.num from ( \n");
	     sql.append(" select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
	     sql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id  ");
		 if(Utility.testString(seriesId)){
			 sql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }	
	     
		 sql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,vwm.color_name color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
		 sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
		 sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id  ");
	     if(Utility.testString(seriesId)){
	    	 sql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
	    	 sql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,vwm.color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
	    	 sql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sql.append("  ) A, TC_USER tu,VW_ORG_DEALER_ALL_new vw,t_Pc_Link_Man tplm  where  A.ADVISER = tu.USER_ID and A.dealer_id=vw.dealer_id  and A.customer_id=tplm.ctm_id   ");
	     
	     
	    if(!CommonUtils.isNullString(startDate)){
	       sql.append("      		    AND  A.order_date  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	    }
	    if(!CommonUtils.isNullString(endDate)){
	       sql.append("      		  AND  A.order_date  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	    }
	 
	     
	     sql.append("	and tplm.relation_code='60581001' )  where num<>0  group by adviser,dealer_id     \n");
		 
	     sql.append("	    UNION  ALL  \n");
	     
	     
	     sql.append(" select to_char(adviser) adviser,dealer_id,0 LK,sum(num) QB,0 DD  from (   \n");
	     sql.append(" select to_char(adviser) adviser,A.dealer_id,A.num from ( \n");
	     sql.append(" select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
	     sql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id  ");
		 if(Utility.testString(seriesId)){
			 sql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }	
	     
		 sql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,vwm.color_name color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
		 sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
		 sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id  ");
	     if(Utility.testString(seriesId)){
	    	 sql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
	    	 sql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,vwm.color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
	    	 sql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sql.append("  ) A, TC_USER tu,VW_ORG_DEALER_ALL_new vw,t_Pc_Link_Man tplm  where  A.ADVISER = tu.USER_ID and A.dealer_id=vw.dealer_id  and A.customer_id=tplm.ctm_id   ");
	     
	     
		    if(!CommonUtils.isNullString(startDate)){
		       sql.append("      		    AND  A.order_date  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
		    }
		    if(!CommonUtils.isNullString(endDate)){
		       sql.append("      		  AND  A.order_date  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
		    }
		 
	     
	     sql.append("	and tplm.relation_code='60581002' )  where num<>0  group by adviser,dealer_id     \n");
		  
	    
	      sql.append("  union all \n");
	    
	     sql.append(" select to_char(adviser) adviser,dealer_id,0 LK,0 QB ,sum(num) DD  from ( \n");
	     sql.append(" select tpc.adviser,tpc.dealer_id,tpod.num ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id");  
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND   tpod.create_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND   tpod.create_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("  union all \n");
	     sql.append(" select tpc.adviser,tpc.dealer_id,tpod.num ");
         sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
         sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
         
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND   tpod.create_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND   tpod.create_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     
	     sql.append("  union all \n");
	     sql.append(" select tpc.adviser,tpc.dealer_id,0-tpod.num num ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id ");
         
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND   tpod.orderd_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND   tpod.orderd_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     
	     sql.append("  union all \n");
	     sql.append(" select tpc.adviser,tpc.dealer_id,0-tpod.num num ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND   tpod.orderd_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND   tpod.orderd_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     sql.append(" )  where num<>0 \n");
	     sql.append(" group by adviser,dealer_id \n");
	     
	     
	     sql.append("     )jd  group by jd.DEALER_ID ,JD.ADVISER\n");
	     
	     sql.append("      )JD  LEFT JOIN VW_ORG_DEALER_ALL_NEW VW   ON JD.DEALER_ID =VW.DEALER_ID  \n");
		 sql.append("       ,tc_user tu,t_pc_group tpg  where JD.ADVISER =tu.user_id  and  tu.group_id=tpg.group_id   \n");
		 //判断是否顾问登陆
		   if(!CommonUtils.isNullString(userId)) {
				sql.append(" AND JD.ADVISER  IN ("+userId+") \n");
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
			sql.append(" AND VW.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL_NEW VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
			//小区
		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND VW.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL_NEW VW WHERE  VW.PQ_ORG_ID="+orgId+") \n");
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
		 sql.append("     order by vw.DEALER_ID       \n");
		 sql.append("          )  \n");
		 sql.append("   select   A.ROOT_ORG_NAME,A.PQ_ORG_NAME,A.DEALER_CODE,A.DEALER_SHORTNAME,'2' a,A.name, a.group_name,   \n");
		 sql.append("   A.DDCOUNT,A.LKCOUNT,A.LKRATE,A.QBCOUNT,A.QBRATE  from a  \n");
		 if(!CommonUtils.isNullString(flag)) {
			 sql.append("  union all   \n");
			 sql.append("  select   A.ROOT_ORG_NAME,A.PQ_ORG_NAME,A.DEALER_CODE,A.DEALER_SHORTNAME,'1' ,max(a.group_name)||'小计', a.group_name ,  \n");
			 sql.append("  sum(A.DDCOUNT),  \n" );
			 sql.append(" sum(A.LKCOUNT),DECODE(sum(A.DDCOUNT),0,'0',DECODE(sum(A.LKCOUNT),0,'0',ROUND(sum(A.LKCOUNT)*100/sum(A.DDCOUNT),2))) || '%' LKRATE,  \n");
			 sql.append(" sum(A.QBCOUNT),DECODE(sum(A.DDCOUNT),0,'0',DECODE(sum(A.QBCOUNT),0,'0',ROUND(sum(A.QBCOUNT)*100/sum(A.DDCOUNT),2))) || '%' QBRATE   \n");
			 sql.append(" from a group by A.ROOT_ORG_NAME,A.PQ_ORG_NAME,A.DEALER_CODE,A.DEALER_SHORTNAME, a.group_name    \n");
			  if(!CommonUtils.isNullString(manager)){
				 sql.append("  union all   \n");
				 sql.append("  select   A.ROOT_ORG_NAME,A.PQ_ORG_NAME,max(A.DEALER_CODE),max(A.DEALER_SHORTNAME),'3'  ,max(a.DEALER_SHORTNAME)||'总计',' ',  \n");
				 sql.append("  sum(A.DDCOUNT),  \n" );
				 sql.append(" sum(A.LKCOUNT),DECODE(sum(A.DDCOUNT),0,'0',DECODE(sum(A.LKCOUNT),0,'0',ROUND(sum(A.LKCOUNT)*100/sum(A.DDCOUNT),2))) || '%' LKRATE,  \n");
				 sql.append(" sum(A.QBCOUNT),DECODE(sum(A.DDCOUNT),0,'0',DECODE(sum(A.QBCOUNT),0,'0',ROUND(sum(A.QBCOUNT)*100/sum(A.DDCOUNT),2))) || '%' QBRATE   \n");
				 sql.append(" from a group by A.ROOT_ORG_NAME,A.PQ_ORG_NAME  \n");
			 }
		 }
		 sql.append("  ) tmp  order by tmp.ROOT_ORG_NAME,tmp.PQ_ORG_NAME,tmp.DEALER_CODE, tmp.a ,tmp.group_name\n");
		 
		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getStatisticsDaoSelectAll(Map<String, String> map) throws UnsupportedEncodingException{
		String startDate=map.get("startDate");
		String endDate=map.get("endDate");
		String dealerCode=map.get("dealerCode");
		String dealerId=map.get("dealerId");
		String dutyType=map.get("dutyType");
		String orgId=map.get("orgId");
		String seriesId=map.get("seriesId");
		StringBuilder sql= new StringBuilder(); 			
		sql.append("     select * from (with a as (    \n");
	     sql.append("   SELECT  VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME,  \n");
	     sql.append(" JD.DEALER_ID, to_number(JD.DDCOUNT) DDCOUNT, \n");
	     sql.append(" to_number(JD.LKCOUNT) LKCOUNT,DECODE(JD.DDCOUNT,0,'0',DECODE(JD.LKCOUNT,0,'0',ROUND(JD.LKCOUNT*100/JD.DDCOUNT,2))) || '%' LKRATE,   \n");
	     sql.append(" to_number(JD.QBCOUNT) QBCOUNT,DECODE(JD.DDCOUNT,0,'0',DECODE(JD.QBCOUNT,0,'0',ROUND(JD.QBCOUNT*100/JD.DDCOUNT,2))) || '%' QBRATE ");
	     
	  	 sql.append("  FROM (  SELECT TO_CHAR(JD.DEALER_ID) DEALER_ID,  \n");
	     sql.append(" 	  SUM(JD.LK) LKCOUNT,SUM(JD.QB) QBCOUNT,SUM(JD.DD) DDCOUNT    \n");
	     
	     sql.append("  FROM (  select dealer_id,sum(num) LK,0 QB,0 DD  from (   \n");
	     sql.append("	 select A.dealer_id,A.num from ( \n");
	     sql.append(" select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
	     sql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id  ");
		 if(Utility.testString(seriesId)){
			 sql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }	
	     
		 sql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,vwm.color_name color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
		 sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
		 sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id  ");
	     if(Utility.testString(seriesId)){
	    	 sql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
	    	 sql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,vwm.color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
	    	 sql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sql.append("  ) A, TC_USER tu,VW_ORG_DEALER_ALL_new vw,t_Pc_Link_Man tplm  where  A.ADVISER = tu.USER_ID and A.dealer_id=vw.dealer_id  and A.customer_id=tplm.ctm_id   ");
	     
	     
	    if(!CommonUtils.isNullString(startDate)){
	       sql.append("      		    AND  A.order_date  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	    }
	    if(!CommonUtils.isNullString(endDate)){
	       sql.append("      		  AND  A.order_date  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	    }
	 
	     
	     sql.append("	and tplm.relation_code='60581001' )  where num<>0  group by dealer_id     \n");
		 
	     sql.append("	    UNION  ALL  \n");
	     
	     
	     sql.append(" select dealer_id,0 LK,sum(num) QB,0 DD  from (   \n");
	     sql.append(" select A.dealer_id,A.num from ( \n");
	     sql.append(" select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
	     sql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id  ");
		 if(Utility.testString(seriesId)){
			 sql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }	
	     
		 sql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,vwm.color_name color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
		 sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
		 sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id  ");
	     if(Utility.testString(seriesId)){
	    	 sql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
	    	 sql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,vwm.color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
	    	 sql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sql.append("  ) A, TC_USER tu,VW_ORG_DEALER_ALL_new vw,t_Pc_Link_Man tplm  where  A.ADVISER = tu.USER_ID and A.dealer_id=vw.dealer_id  and A.customer_id=tplm.ctm_id   ");
	     
	     
		    if(!CommonUtils.isNullString(startDate)){
		       sql.append("      		    AND  A.order_date  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
		    }
		    if(!CommonUtils.isNullString(endDate)){
		       sql.append("      		  AND  A.order_date  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
		    }
		 
	     
	     sql.append("	and tplm.relation_code='60581002' )  where num<>0  group by dealer_id     \n");
		  
	    
	      sql.append("  union all \n");
	    
	     sql.append(" select dealer_id,0 LK,0 QB ,sum(num) DD  from ( \n");
	     sql.append(" select tpc.dealer_id,tpod.num ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id");  
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND   tpod.create_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND   tpod.create_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("  union all \n");
	     sql.append(" select tpc.dealer_id,tpod.num ");
         sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
         sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
         
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND   tpod.create_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND   tpod.create_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     
	     sql.append("  union all \n");
	     sql.append(" select tpc.dealer_id,0-tpod.num num ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id ");
         
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND   tpod.orderd_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND   tpod.orderd_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     
	     sql.append("  union all \n");
	     sql.append(" select tpc.dealer_id,0-tpod.num num ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND   tpod.orderd_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND   tpod.orderd_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     sql.append(" )  where num<>0 \n");
	     sql.append(" group by dealer_id \n");
	     
	 
	     sql.append("     )jd  group by jd.DEALER_ID\n");
	     
	     sql.append("      )JD  LEFT JOIN VW_ORG_DEALER_ALL_NEW VW   ON JD.DEALER_ID =VW.DEALER_ID  \n");
 		 sql.append("      where 1=1    \n");
		   
		 //经销商
		if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
			sql.append("  AND (VW.DEALER_ID ="+dealerId+" )\n");
			//大区
		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND VW.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL_NEW VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
			//小区
		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND VW.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL_NEW VW WHERE  VW.PQ_ORG_ID="+orgId+") \n");
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
		 sql.append("     order by vw.DEALER_ID       \n");
		 sql.append("          )  \n");
		 sql.append("   select  TO_CHAR(A.DEALER_ID) DEALER_ID, A.ROOT_ORG_NAME,A.PQ_ORG_NAME,A.DEALER_CODE,A.DEALER_SHORTNAME,'3' a,   \n");
		 sql.append("   A.DDCOUNT,A.LKCOUNT,A.LKRATE,A.QBCOUNT,A.QBRATE  from a  \n");
		 if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||   Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("  union all   \n");
			 sql.append("  select   '',A.ROOT_ORG_NAME,A.PQ_ORG_NAME,' ',max(A.PQ_ORG_NAME)||'小计','3' ,   \n");
			 sql.append("  sum(A.DDCOUNT),  \n" );
			 sql.append(" sum(A.LKCOUNT),DECODE(sum(A.DDCOUNT),0,'0',DECODE(sum(A.LKCOUNT),0,'0',ROUND(sum(A.LKCOUNT)*100/sum(A.DDCOUNT),2))) || '%' LKRATE,  \n");
			 sql.append(" sum(A.QBCOUNT),DECODE(sum(A.DDCOUNT),0,'0',DECODE(sum(A.QBCOUNT),0,'0',ROUND(sum(A.QBCOUNT)*100/sum(A.DDCOUNT),2))) || '%' QBRATE   \n");
			 
			sql.append(" from a  group by A.ROOT_ORG_NAME,A.PQ_ORG_NAME    \n");
		 }  if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||   Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				 sql.append("  union all   \n");
				 sql.append("   select   '',A.ROOT_ORG_NAME,' ',' ',max(a.ROOT_ORG_NAME)||'合计','2'  ,  \n");
				 
				 sql.append("  sum(A.DDCOUNT),  \n" );
				 sql.append(" sum(A.LKCOUNT),DECODE(sum(A.DDCOUNT),0,'0',DECODE(sum(A.LKCOUNT),0,'0',ROUND(sum(A.LKCOUNT)*100/sum(A.DDCOUNT),2))) || '%' LKRATE,  \n");
				 sql.append(" sum(A.QBCOUNT),DECODE(sum(A.DDCOUNT),0,'0',DECODE(sum(A.QBCOUNT),0,'0',ROUND(sum(A.QBCOUNT)*100/sum(A.DDCOUNT),2))) || '%' QBRATE   \n");
				 sql.append(" from a group by A.ROOT_ORG_NAME   \n");
		   }
		   if(  Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("  union all   \n");
			 sql.append("  select '',  ' ',' ' , ' ','全国合计','1' ,   \n");
			 
			 sql.append("  sum(A.DDCOUNT),  \n" );
			 sql.append(" sum(A.LKCOUNT),DECODE(sum(A.DDCOUNT),0,'0',DECODE(sum(A.LKCOUNT),0,'0',ROUND(sum(A.LKCOUNT)*100/sum(A.DDCOUNT),2))) || '%' LKRATE,  \n");
			 sql.append(" sum(A.QBCOUNT),DECODE(sum(A.DDCOUNT),0,'0',DECODE(sum(A.QBCOUNT),0,'0',ROUND(sum(A.QBCOUNT)*100/sum(A.DDCOUNT),2))) || '%' QBRATE   \n");
			 sql.append(" from a   \n");
	 }
		 sql.append("  ) tmp  order by  tmp.a,tmp.ROOT_ORG_NAME, tmp.pq_org_name ,rownum  \n");
		 
		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
 
}
