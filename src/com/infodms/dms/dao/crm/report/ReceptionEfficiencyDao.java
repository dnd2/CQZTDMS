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

public class ReceptionEfficiencyDao extends BaseDao<PO>{
private static final ReceptionEfficiencyDao dao = new ReceptionEfficiencyDao();
private ActionContext act = ActionContext.getContext();
RequestWrapper request = act.getRequest();
	public static final ReceptionEfficiencyDao getInstance() {
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
		
		 sql.append(" select * from (with a as (  \n"); 
		 
		 sql.append(" select qk.* ,tu.name , tpg.group_id,tpg.group_name, VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME   \n" );
		 sql.append(" from ( \n" );
		sql.append(" select x.dealer_id, x.adviser,sum(x.jhyy) XSZLCOUNT,--线索邀约输\n" );
		sql.append("       sum(x.sjxsyy) SJXSCOUNT,--实际邀约数\n" );
		sql.append("       ROUND(sum(x.sjxsyy) / decode(sum(x.jhyy), 0, 1, sum(x.jhyy)) *100,2) ||'%'XSYYRATE ,\n" );
		sql.append("       sum(x.jhgz) JHSCOUNT,--计划跟踪数\n" );
		sql.append("       sum(x.sjgz) SJSCOUNT,--实际跟踪数\n" );
		sql.append("       round(sum(x.sjgz) / decode(sum(x.jhgz), 0, 1, sum(x.jhgz))*100,2)||'%'  GZRATE,\n" );
		sql.append("       sum(x.jhzcyy) ZYYCOUNT,--计划再次邀约数\n" );
		sql.append("       sum(x.sjzcyy) ZDDCOUNT,--实际再次邀约数\n" );
		sql.append("      round( sum(x.sjzcyy) / decode(sum(x.jhzcyy), 0, 1, sum(x.jhzcyy)) *100,2)||'%'ZYYRATE,\n" );
		sql.append("       sum(x.jhhf) HFCOUNT,--计划回访数\n" );
		sql.append("       sum(x.sjhf) HFWCCOUNT,--实际回访数\n" );
		sql.append("      round( sum(x.sjhf) / decode(sum(x.jhhf), 0, 1, sum(x.jhhf))*100,2)||'%' HFRATE \n" );
		sql.append("  from ( " );
		sql.append("  --线索总量\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          count(1) jhyy, 0 jhgz, 0 sjgz, 0 sjxsyy, 0 jhzcyy, 0 sjzcyy, 0 jhhf, 0 sjhf\n" );
		sql.append("   from t_pc_invite tpI, t_pc_customer tpc, t_pc_intent_vehicle tpiv\n" );
		sql.append("  WHERE tpI.customer_id = tpc.customer_id\n" );
		sql.append("    and TPI.invite_WAY = 60211001\n" );
		sql.append("    and tpiv.series_id(+) = tpc.intent_vehicle");

		 if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.Up_Series_Id  in ("+seriesId+")   \n" );
		     }
		 if(!CommonUtils.isNullString(startDate)){
	            sql.append("      		  AND TPi.PLAN_INVITE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	     if(!CommonUtils.isNullString(endDate)){
	            sql.append("      		  AND  TPi.PLAN_INVITE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	     if(!"".equals(dealerCode)&&dealerCode!=null){
			  String arr[]=dealerCode.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));
	            
			sql.append(" AND (tpc.DEALER_ID ="+dealerId+" " +
			"or tpc.DEALER_ID in(select dealer_ids from t_pc_company_group where par_dealer_id="+dealerId+" "+
           "and DEALER_CODES IN("+str+"))) \n" );
		}
	     if(!CommonUtils.isNullString(dealerId)  && ("".equals(dealerCode)&&dealerCode==null)){
		    	   sql.append(" and tpc.dealer_id="+dealerId+"");
		       }
		sql.append("   group by tpc.dealer_id, tpc.adviser\n" );
		sql.append("  union all\n" );
		sql.append("  --计划跟踪输\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          0, count(1),  0, 0, 0, 0, 0, 0\n" );
		sql.append("    from t_pc_follow tpf, t_pc_customer tpc, t_pc_intent_vehicle tpiv \n" );
		sql.append("   where tpf.customer_id = tpc.customer_id and tpiv.series_id(+) = tpc.intent_vehicle and tpf.old_level is not null \n" );
		 if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.Up_Series_Id in ("+seriesId+") \n" );
		     }
		 if(!CommonUtils.isNullString(startDate)){
	            sql.append("      		  AND TPf.follow_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	        if(!CommonUtils.isNullString(endDate)){
	            sql.append("      		  AND  TPf.follow_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	        if(!"".equals(dealerCode)&&dealerCode!=null){
				  String arr[]=dealerCode.split(",");
		            String str="";

		            for(int i=0;i<arr.length;i++){
		                str+="'"+arr[i]+"',";
		            }
		            str=str.substring(0,str.lastIndexOf(","));
		            
				sql.append(" AND (tpc.DEALER_ID ="+dealerId+" " +
				"or tpc.DEALER_ID in(select dealer_ids from t_pc_company_group where par_dealer_id="+dealerId+" "+
	           "and DEALER_CODES IN("+str+"))) \n" );
			}
	        if(!CommonUtils.isNullString(dealerId)  && ("".equals(dealerCode)&&dealerCode==null)){
		    	   sql.append(" and tpc.dealer_id="+dealerId+"");
		       }
		sql.append("   group by tpc.dealer_id, tpc.adviser\n" );
		sql.append("  union all\n" );
		sql.append("  --实际跟踪输\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          0, 0, count(1), 0, 0, 0, 0, 0\n" );
		sql.append("    from t_pc_follow tpf, t_pc_customer tpc, t_pc_intent_vehicle tpiv\n" );
		sql.append("   where tpf.customer_id = tpc.customer_id and tpf.task_status = 60171002 and tpf.old_level is not null\n" );
		sql.append("     and tpiv.series_id(+) = tpc.intent_vehicle\n" );
		 if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.Up_Series_Id in ("+seriesId+") \n" );
		     }
		  if(!CommonUtils.isNullString(startDate)){
	            sql.append("      		  AND TPf.follow_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	       if(!CommonUtils.isNullString(endDate)){
	            sql.append("      		  AND  TPf.follow_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	       if(!"".equals(dealerCode)&&dealerCode!=null){
				  String arr[]=dealerCode.split(",");
		            String str="";

		            for(int i=0;i<arr.length;i++){
		                str+="'"+arr[i]+"',";
		            }
		            str=str.substring(0,str.lastIndexOf(","));
		            
				sql.append(" AND (tpc.DEALER_ID ="+dealerId+" " +
				"or tpc.DEALER_ID in(select dealer_ids from t_pc_company_group where par_dealer_id="+dealerId+" "+
	           "and DEALER_CODES IN("+str+"))) \n" );
			}
	       if(!CommonUtils.isNullString(dealerId) && ("".equals(dealerCode)&&dealerCode==null)){
	    	   sql.append(" and tpc.dealer_id="+dealerId+"");
	       }
		sql.append("   group by tpc.dealer_id, tpc.adviser \n" );
		sql.append("  union all\n" );
		sql.append("  --实际线索邀约输\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          0, 0, 0,  count(1), 0, 0, 0, 0\n" );
		sql.append("    from t_pc_invite tpI, t_pc_customer tpc, t_pc_intent_vehicle tpiv,t_pc_invite_shop tpis\n" );
		sql.append("   WHERE tpI.customer_id = tpc.customer_id\n" );
		sql.append("     and TPI.invite_WAY = 60211001 and tpi.task_status = 60171002\n" );
		sql.append("     and tpiv.series_id(+) = tpc.intent_vehicle\n" );
		sql.append("     and tpi.NEXT_TASK=tpis.INVITE_SHOP_ID(+)  and tpis.if_shop=10041001  and tpi.old_level is not null \n" );
		sql.append("     and (tpi.DIRECTOR_AUDIT=60191002 or tpi.DIRECTOR_AUDIT=60191004)\n" );
		if(!CommonUtils.isNullString(seriesId)){
	    	sql.append("     		  AND TPIV.Up_Series_Id in ("+seriesId+")    \n" );
	     }
	  if(!CommonUtils.isNullString(startDate)){
            sql.append("      		  AND TPi.PLAN_INVITE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(endDate)){
            sql.append("      		  AND  TPi.PLAN_INVITE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!"".equals(dealerCode)&&dealerCode!=null){
			  String arr[]=dealerCode.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));
	            
			sql.append(" AND (tpc.DEALER_ID ="+dealerId+" " +
			"or tpc.DEALER_ID in(select dealer_ids from t_pc_company_group where par_dealer_id="+dealerId+" "+
         "and DEALER_CODES IN("+str+"))) \n" );
		}
        if(!CommonUtils.isNullString(dealerId) && ("".equals(dealerCode)&&dealerCode==null)){
	    	   sql.append(" and tpc.dealer_id="+dealerId+"");
	       }
		sql.append("   group by tpc.dealer_id, tpc.adviser\n" );
		sql.append("  --计划再次邀约\n" );
		sql.append("  UNION ALL\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          0, 0, 0,  0, count(1), 0, 0, 0\n" );
		sql.append("    from t_pc_invite tpI, t_pc_customer tpc, t_pc_intent_vehicle tpiv\n" );
		sql.append("   WHERE tpI.customer_id = tpc.customer_id and TPI.invite_WAY = 60211002\n" );
		sql.append("     and tpiv.series_id(+) = tpc.intent_vehicle and tpi.old_level is not null\n" );
		sql.append("     and (tpi.DIRECTOR_AUDIT=60191002 or tpi.DIRECTOR_AUDIT=60191004)\n" );
		if(!CommonUtils.isNullString(seriesId)){
	    	sql.append("     		  AND TPIV.Up_Series_Id in ("+seriesId+")  \n" );
	     }
	  if(!CommonUtils.isNullString(startDate)){
            sql.append("      		  AND TPi.PLAN_INVITE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(endDate)){
            sql.append("      		  AND  TPi.PLAN_INVITE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!"".equals(dealerCode)&&dealerCode!=null){
			  String arr[]=dealerCode.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));
	            
			sql.append(" AND (tpc.DEALER_ID ="+dealerId+" " +
			"or tpc.DEALER_ID in(select dealer_ids from t_pc_company_group where par_dealer_id="+dealerId+" "+
         "and DEALER_CODES IN("+str+"))) \n" );
		}
        if(!CommonUtils.isNullString(dealerId) && ("".equals(dealerCode)&&dealerCode==null)){
	    	   sql.append(" and tpc.dealer_id="+dealerId+"");
	       }
		sql.append("   group by tpc.dealer_id, tpc.adviser\n" );
		sql.append("  UNION ALL\n" );
		sql.append("  --实际再次邀约\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          0, 0, 0, 0, 0, count(1), 0, 0\n" );
		sql.append("    from t_pc_invite tpI, t_pc_customer tpc, t_pc_intent_vehicle tpiv,t_pc_invite_shop tpis\n" );
		sql.append("   WHERE tpI.customer_id = tpc.customer_id\n" );
		sql.append("     and TPI.task_status = 60171002 and TPI.invite_WAY = 60211002 \n" );
		sql.append("     and tpiv.series_id(+) = tpc.intent_vehicle\n" );
		sql.append("     and tpi.NEXT_TASK=tpis.INVITE_SHOP_ID(+)  and tpis.if_shop=10041001 and tpi.old_level is not null \n" );
		sql.append("     and (tpi.DIRECTOR_AUDIT=60191002 or tpi.DIRECTOR_AUDIT=60191004)\n" );
		if(!CommonUtils.isNullString(seriesId)){
	    	sql.append("     		  AND TPIV.Up_Series_Id  in ("+seriesId+")   \n" );
	     }
	  if(!CommonUtils.isNullString(startDate)){
            sql.append("      		  AND TPi.PLAN_INVITE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(endDate)){
            sql.append("      		  AND  TPi.PLAN_INVITE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!"".equals(dealerCode)&&dealerCode!=null){
			  String arr[]=dealerCode.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));
	            
			sql.append(" AND (tpc.DEALER_ID ="+dealerId+" " +
			"or tpc.DEALER_ID in(select dealer_ids from t_pc_company_group where par_dealer_id="+dealerId+" "+
         "and DEALER_CODES IN("+str+"))) \n" );
		}
        if(!CommonUtils.isNullString(dealerId) && ("".equals(dealerCode)&&dealerCode==null)){
	    	   sql.append(" and tpc.dealer_id="+dealerId+"" );
	       }
		sql.append("   group by tpc.dealer_id, tpc.adviser\n" );
		sql.append("  UNION ALL\n" );
		sql.append("  --计划回访\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          0, 0, 0, 0, 0, 0, COUNT(1), 0\n" );
		sql.append("    from t_pc_REVISIT tpR, t_pc_customer tpc, t_pc_intent_vehicle tpiv\n" );
		sql.append("   where tpr.customer_id = tpc.customer_id\n" );
		sql.append("     and tpiv.series_id(+) = tpc.intent_vehicle\n" );
		sql.append("     and TPR.task_status <> 60171003 " );
		if(!CommonUtils.isNullString(seriesId)){
	    	sql.append("     		  AND TPIV.Up_Series_Id in ("+seriesId+")   \n" );
	     }
	  if(!CommonUtils.isNullString(startDate)){
            sql.append("      		  AND TPr.revisit_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(endDate)){
            sql.append("      		  AND  TPr.revisit_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!"".equals(dealerCode)&&dealerCode!=null){
			  String arr[]=dealerCode.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));
	            
			sql.append(" AND (tpc.DEALER_ID ="+dealerId+" " +
			"or tpc.DEALER_ID in(select dealer_ids from t_pc_company_group where par_dealer_id="+dealerId+" "+
         "and DEALER_CODES IN("+str+"))) \n" );
		}
        if(!CommonUtils.isNullString(dealerId) && ("".equals(dealerCode)&&dealerCode==null)){
	    	   sql.append(" and tpc.dealer_id="+dealerId+"");
	       }
		sql.append("   group by tpc.dealer_id, tpc.adviser\n" );
		sql.append("  UNION ALL\n" );
		sql.append("  --实际回访\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          0, 0, 0, 0, 0, 0, 0, COUNT(1)\n" );
		sql.append("    from t_pc_REVISIT tpR, t_pc_customer tpc, t_pc_intent_vehicle tpiv\n" );
		sql.append("   where tpr.customer_id = tpc.customer_id\n" );
		sql.append("     and TPR.task_status = 60171002\n" );
		sql.append("     and tpiv.series_id(+) = tpc.intent_vehicle\n" );
		if(!CommonUtils.isNullString(seriesId)){
	    	sql.append("     		  AND TPIV.Up_Series_Id in ("+seriesId+")   \n" );
	     }
	  if(!CommonUtils.isNullString(startDate)){
            sql.append("      		  AND TPr.revisit_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(endDate)){
            sql.append("      		  AND  TPr.revisit_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!"".equals(dealerCode)&&dealerCode!=null){
			  String arr[]=dealerCode.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));
	            
			sql.append(" AND (tpc.DEALER_ID ="+dealerId+" " +
			"or tpc.DEALER_ID in(select dealer_ids from t_pc_company_group where par_dealer_id="+dealerId+" "+
         "and DEALER_CODES IN("+str+"))) \n" );
		}
        if(!CommonUtils.isNullString(dealerId) && ("".equals(dealerCode)&&dealerCode==null)){
	    	   sql.append(" and tpc.dealer_id="+dealerId+"");
	       }
		sql.append("   group by tpc.dealer_id, tpc.adviser \n" );
		sql.append("  	 ) X group by   x.dealer_id, x.adviser \n" );
		sql.append("  )qk LEFT JOIN VW_ORG_DEALER_ALL VW   ON qk.DEALER_ID =VW.DEALER_ID \n" ); 
		sql.append("  	   ,tc_user tu,t_pc_group tpg  where qk.ADVISER =tu.user_id  and  tu.group_id=tpg.group_id  \n" ); 
		 //判断是否顾问登陆
		   if(!CommonUtils.isNullString(userId)) {
				sql.append(" AND qk.ADVISER  IN ("+userId+") \n");
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

			sql.append("AND VW.DEALER_CODE IN("+str+")\n");
		}
		
		sql.append("   	)  \n" ); 
		 
		 sql.append("    select  a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.DEALER_CODE,a.DEALER_SHORTNAME, '2' a,a.name, a.group_name, \n");      
		 sql.append("    a.JHSCOUNT,a.SJSCOUNT,a.GZRATE,a.XSZLCOUNT,a.SJXSCOUNT,a.XSYYRATE,a.ZYYCOUNT,a.ZDDCOUNT,a.ZYYRATE, \n");
		 sql.append("    a.HFCOUNT,a.HFWCCOUNT,a.HFRATE from a  \n");
		 
		 if(!CommonUtils.isNullString(flag)) {
			 sql.append("    union all  \n");
			 sql.append("    select  a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.DEALER_CODE,a.DEALER_SHORTNAME,'1' a ,max(a.group_name)||'小计',  a.group_name , \n");  
			 sql.append("     sum(a.JHSCOUNT), sum(a.SJSCOUNT), DECODE(sum(A.SJSCOUNT),0,'0',ROUND(sum(A.SJSCOUNT)*100/sum(A.JHSCOUNT),2)) || '%'  GZRATE ,  \n");
			 sql.append("     sum(a.XSZLCOUNT), sum(a.SJXSCOUNT), DECODE(sum(A.SJXSCOUNT),0,'0',ROUND(sum(A.SJXSCOUNT)*100/sum(A.XSZLCOUNT),2)) || '%'  XSYYRATE ,  \n");
			 sql.append("     sum(a.ZYYCOUNT), sum(a.ZDDCOUNT), DECODE(sum(A.ZDDCOUNT),0,'0',ROUND(sum(A.ZDDCOUNT)*100/sum(A.ZYYCOUNT),2)) || '%' ZYYRATE , \n");
			 sql.append("     sum(a.HFCOUNT), sum(a.HFWCCOUNT), DECODE(sum(A.HFWCCOUNT),0,'0',ROUND(sum(A.HFWCCOUNT)*100/sum(A.HFCOUNT),2)) || '%'  HFRATE   \n");
			 sql.append("    from a  group by a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.DEALER_CODE,a.DEALER_SHORTNAME, a.group_name  \n");
		 
		  if(!CommonUtils.isNullString(manager)){
			 sql.append("    union all  \n");
			 sql.append("    select  a.ROOT_ORG_NAME,a.PQ_ORG_NAME,max(a.DEALER_CODE),max(a.DEALER_SHORTNAME),'3' a,max(a.DEALER_SHORTNAME)||'总计',  ' ' , \n");  
			 sql.append("     sum(a.JHSCOUNT), sum(a.SJSCOUNT), DECODE(sum(A.SJSCOUNT),0,'0',ROUND(sum(A.SJSCOUNT)*100/sum(A.JHSCOUNT),2)) || '%'  GZRATE ,  \n");
			 sql.append("     sum(a.XSZLCOUNT), sum(a.SJXSCOUNT), DECODE(sum(A.SJXSCOUNT),0,'0',ROUND(sum(A.SJXSCOUNT)*100/sum(A.XSZLCOUNT),2)) || '%'  XSYYRATE ,  \n");
			 sql.append("     sum(a.ZYYCOUNT), sum(a.ZDDCOUNT), DECODE(sum(A.ZDDCOUNT),0,'0',ROUND(sum(A.ZDDCOUNT)*100/sum(A.ZYYCOUNT),2)) || '%' ZYYRATE , \n");
			 sql.append("     sum(a.HFCOUNT), sum(a.HFWCCOUNT), DECODE(sum(A.HFWCCOUNT),0,'0',ROUND(sum(A.HFWCCOUNT)*100/sum(A.HFCOUNT),2)) || '%'  HFRATE   \n");
			 sql.append("    from a  group by a.ROOT_ORG_NAME,a.PQ_ORG_NAME   \n");
		  }
		 }
		 sql.append("     ) tmp  order by tmp.ROOT_ORG_NAME,tmp.PQ_ORG_NAME,tmp.DEALER_CODE, tmp.a ,tmp.group_name   \n");
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
		 sql.append(" select * from (with a as (  \n"); 
		 sql.append(" SELECT  VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME,  tj.* from (  \n");
		
	    sql.append(" select x.dealer_id,sum(x.jhyy) XSZLCOUNT,--线索邀约输\n" );
		sql.append("       sum(x.sjxsyy) SJXSCOUNT,--实际邀约数\n" );
		sql.append("       ROUND(sum(x.sjxsyy) / decode(sum(x.jhyy), 0, 1, sum(x.jhyy)) *100,2) ||'%'XSYYRATE ,\n" );
		sql.append("       sum(x.jhgz) JHSCOUNT,--计划跟踪数\n" );
		sql.append("       sum(x.sjgz) SJSCOUNT,--实际跟踪数\n" );
		sql.append("       round(sum(x.sjgz) / decode(sum(x.jhgz), 0, 1, sum(x.jhgz))*100,2)||'%'  GZRATE,\n" );
		sql.append("       sum(x.jhzcyy) ZYYCOUNT,--计划再次邀约数\n" );
		sql.append("       sum(x.sjzcyy) ZDDCOUNT,--实际再次邀约数\n" );
		sql.append("      round( sum(x.sjzcyy) / decode(sum(x.jhzcyy), 0, 1, sum(x.jhzcyy)) *100,2)||'%'ZYYRATE,\n" );
		sql.append("       sum(x.jhhf) HFCOUNT,--计划回访数\n" );
		sql.append("       sum(x.sjhf) HFWCCOUNT,--实际回访数\n" );
		sql.append("      round( sum(x.sjhf) / decode(sum(x.jhhf), 0, 1, sum(x.jhhf))*100,2)||'%' HFRATE \n" );
		sql.append("  from ( " );
		sql.append("  --线索总量\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          count(1) jhyy, 0 jhgz, 0 sjgz, 0 sjxsyy, 0 jhzcyy, 0 sjzcyy, 0 jhhf, 0 sjhf\n" );
		sql.append("   from t_pc_invite tpI, t_pc_customer tpc, t_pc_intent_vehicle tpiv\n" );
		sql.append("  WHERE tpI.customer_id = tpc.customer_id\n" );
		sql.append("    and TPI.invite_WAY = 60211001\n" );
		sql.append("    and tpiv.series_id(+) = tpc.intent_vehicle");

		 if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.Up_Series_Id in ("+seriesId+")   \n" );
		     }
		 if(!CommonUtils.isNullString(startDate)){
	            sql.append("      		  AND TPi.PLAN_INVITE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	     if(!CommonUtils.isNullString(endDate)){
	            sql.append("      		  AND  TPi.PLAN_INVITE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	     if(!CommonUtils.isNullString(dealerId)){
		    	   sql.append(" and tpc.dealer_id="+dealerId+"");
		       }
		sql.append("   group by tpc.dealer_id, tpc.adviser\n" );
		sql.append("  union all\n" );
		sql.append("  --计划跟踪输\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          0, count(1),  0, 0, 0, 0, 0, 0\n" );
		sql.append("    from t_pc_follow tpf, t_pc_customer tpc, t_pc_intent_vehicle tpiv \n" );
		sql.append("   where tpf.customer_id = tpc.customer_id and tpiv.series_id(+) = tpc.intent_vehicle and tpf.old_level is not null \n" );
		 if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.Up_Series_Id  in ("+seriesId+")   \n" );
		     }
		 if(!CommonUtils.isNullString(startDate)){
	            sql.append("      		  AND TPf.follow_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	        if(!CommonUtils.isNullString(endDate)){
	            sql.append("      		  AND  TPf.follow_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	        if(!CommonUtils.isNullString(dealerId)){
		    	   sql.append(" and tpc.dealer_id="+dealerId+"");
		       }
		sql.append("   group by tpc.dealer_id, tpc.adviser\n" );
		sql.append("  union all\n" );
		sql.append("  --实际跟踪输\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          0, 0, count(1), 0, 0, 0, 0, 0\n" );
		sql.append("    from t_pc_follow tpf, t_pc_customer tpc, t_pc_intent_vehicle tpiv\n" );
		sql.append("   where tpf.customer_id = tpc.customer_id and tpf.task_status = 60171002 and tpf.old_level is not null\n" );
		sql.append("     and tpiv.series_id(+) = tpc.intent_vehicle\n" );
		
		 if(!CommonUtils.isNullString(seriesId)){
			 sql.append("     		  AND TPIV.Up_Series_Id  in ("+seriesId+")   \n" );
		     }
		  if(!CommonUtils.isNullString(startDate)){
	            sql.append("      		  AND TPf.follow_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	       if(!CommonUtils.isNullString(endDate)){
	            sql.append("      		  AND  TPf.follow_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	       if(!CommonUtils.isNullString(dealerId)){
	    	   sql.append(" and tpc.dealer_id="+dealerId+"");
	       }
		sql.append("   group by tpc.dealer_id, tpc.adviser \n" );
		sql.append("  union all\n" );
		sql.append("  --实际线索邀约输\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          0, 0, 0,  count(1), 0, 0, 0, 0\n" );
		sql.append("    from t_pc_invite tpI, t_pc_customer tpc, t_pc_intent_vehicle tpiv,t_pc_invite_shop tpis \n" );
		sql.append("   WHERE tpI.customer_id = tpc.customer_id\n" );
		sql.append("     and TPI.invite_WAY = 60211001 and tpi.task_status = 60171002 \n" );
		sql.append("     and tpiv.series_id(+) = tpc.intent_vehicle\n" );
		sql.append("     and tpi.NEXT_TASK=tpis.INVITE_SHOP_ID(+)  and tpis.if_shop=10041001 \n" );
		if(!CommonUtils.isNullString(seriesId)){
			sql.append("     		  AND TPIV.Up_Series_Id  in ("+seriesId+")    \n" );
	     }
	  if(!CommonUtils.isNullString(startDate)){
            sql.append("      		  AND TPi.PLAN_INVITE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(endDate)){
            sql.append("      		  AND  TPi.PLAN_INVITE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(dealerId)){
	    	   sql.append(" and tpc.dealer_id="+dealerId+"");
	       }
		sql.append("   group by tpc.dealer_id, tpc.adviser\n" );
		sql.append("  --计划再次邀约\n" );
		sql.append("  UNION ALL\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          0, 0, 0,  0, count(1), 0, 0, 0\n" );
		sql.append("    from t_pc_invite tpI, t_pc_customer tpc, t_pc_intent_vehicle tpiv\n" );
		sql.append("   WHERE tpI.customer_id = tpc.customer_id and TPI.invite_WAY = 60211002\n" );
		sql.append("     and tpiv.series_id(+) = tpc.intent_vehicle and tpi.old_level is not  null \n" );
		if(!CommonUtils.isNullString(seriesId)){
			sql.append("     		  AND TPIV.Up_Series_Id  in ("+seriesId+")  \n" );
	     }
	  if(!CommonUtils.isNullString(startDate)){
            sql.append("      		  AND TPi.PLAN_INVITE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(endDate)){
            sql.append("      		  AND  TPi.PLAN_INVITE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(dealerId)){
	    	   sql.append(" and tpc.dealer_id="+dealerId+"");
	       }
		sql.append("   group by tpc.dealer_id, tpc.adviser\n" );
		sql.append("  UNION ALL\n" );
		sql.append("  --实际再次邀约\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          0, 0, 0, 0, 0, count(1), 0, 0\n" );
		sql.append("    from t_pc_invite tpI, t_pc_customer tpc, t_pc_intent_vehicle tpiv,t_pc_invite_shop tpis \n" );
		sql.append("   WHERE tpI.customer_id = tpc.customer_id\n" );
		sql.append("     and TPI.task_status = 60171002 and TPI.invite_WAY = 60211002 \n" );
		sql.append("     and tpi.NEXT_TASK=tpis.INVITE_SHOP_ID(+)  and tpis.if_shop=10041001 and tpi.old_level is not  null \n" );
		
		sql.append("     and tpiv.series_id(+) = tpc.intent_vehicle\n" );
		if(!CommonUtils.isNullString(seriesId)){
			sql.append("     		  AND TPIV.Up_Series_Id  in ("+seriesId+")    \n" );
	     }
	  if(!CommonUtils.isNullString(startDate)){
            sql.append("      		  AND TPi.PLAN_INVITE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(endDate)){
            sql.append("      		  AND  TPi.PLAN_INVITE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(dealerId)){
	    	   sql.append(" and tpc.dealer_id="+dealerId+"");
	       }
		sql.append("   group by tpc.dealer_id, tpc.adviser\n" );
		sql.append("  UNION ALL\n" );
		sql.append("  --计划回访\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          0, 0, 0, 0, 0, 0, COUNT(1), 0\n" );
		sql.append("    from t_pc_REVISIT tpR, t_pc_customer tpc, t_pc_intent_vehicle tpiv\n" );
		sql.append("   where tpr.customer_id = tpc.customer_id\n" );
		sql.append("     and tpiv.series_id(+) = tpc.intent_vehicle\n" );
		sql.append("     and TPR.task_status <> 60171003 \n" );
		
		if(!CommonUtils.isNullString(seriesId)){
	    	sql.append("     		  AND TPIV.Up_Series_Id  in ("+seriesId+")    \n" );
	     }
	  if(!CommonUtils.isNullString(startDate)){
            sql.append("      		  AND TPr.revisit_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(endDate)){
            sql.append("      		  AND  TPr.revisit_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(dealerId)){
	    	   sql.append(" and tpc.dealer_id="+dealerId+"");
	       }
		sql.append("   group by tpc.dealer_id, tpc.adviser\n" );
		sql.append("  UNION ALL\n" );
		sql.append("  --实际回访\n" );
		sql.append("  select to_char(tpc.dealer_id) dealer_id, to_char(tpc.adviser) adviser,\n" );
		sql.append("          0, 0, 0, 0, 0, 0, 0, COUNT(1)\n" );
		sql.append("    from t_pc_REVISIT tpR, t_pc_customer tpc, t_pc_intent_vehicle tpiv\n" );
		sql.append("   where tpr.customer_id = tpc.customer_id\n" );
		sql.append("     and TPR.task_status = 60171002\n" );
		sql.append("     and tpiv.series_id(+) = tpc.intent_vehicle\n" );
		if(!CommonUtils.isNullString(seriesId)){
			sql.append("     		  AND TPIV.Up_Series_Id  in ("+seriesId+")    \n" );
	     }
	  if(!CommonUtils.isNullString(startDate)){
            sql.append("      		  AND TPr.revisit_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(endDate)){
            sql.append("      		  AND  TPr.revisit_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
          }
        if(!CommonUtils.isNullString(dealerId)){
	    	   sql.append(" and tpc.dealer_id="+dealerId+"");
	       }
		sql.append("   group by tpc.dealer_id, tpc.adviser \n" );
		sql.append("  	 ) X group by   x.dealer_id \n" );
		 sql.append("    ) tj LEFT JOIN VW_ORG_DEALER_ALL VW   ON tj.DEALER_ID =VW.DEALER_ID WHERE 1=1 \n");
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
		 sql.append("         order by vw.DEALER_ID   \n");
		 sql.append("       )  \n");
		 sql.append("    select TO_CHAR(A.DEALER_ID) DEALER_ID, a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.DEALER_CODE,a.DEALER_SHORTNAME, '3' a, \n");      
		 sql.append("    a.JHSCOUNT,a.SJSCOUNT,a.GZRATE,a.XSZLCOUNT,a.SJXSCOUNT,a.XSYYRATE,a.ZYYCOUNT,a.ZDDCOUNT,a.ZYYRATE, \n");
		 sql.append("    a.HFCOUNT,a.HFWCCOUNT,a.HFRATE from a  \n");
		 if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||   Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("    union all  \n");
			 sql.append("    select  '',a.ROOT_ORG_NAME,a.PQ_ORG_NAME,' ',max(a.PQ_ORG_NAME)||'小计','3' a ,  \n");  
			 sql.append("     sum(a.JHSCOUNT), sum(a.SJSCOUNT), DECODE(sum(A.SJSCOUNT),0,'0',ROUND(sum(A.SJSCOUNT)*100/sum(A.JHSCOUNT),2)) || '%'  GZRATE ,  \n");
			 sql.append("     sum(a.XSZLCOUNT), sum(a.SJXSCOUNT), DECODE(sum(A.SJXSCOUNT),0,'0',ROUND(sum(A.SJXSCOUNT)*100/sum(A.XSZLCOUNT),2)) || '%'  XSYYRATE ,  \n");
			 sql.append("     sum(a.ZYYCOUNT), sum(a.ZDDCOUNT), DECODE(sum(A.ZDDCOUNT),0,'0',ROUND(sum(A.ZDDCOUNT)*100/sum(A.ZYYCOUNT),2)) || '%' ZYYRATE , \n");
			 sql.append("     sum(a.HFCOUNT), sum(a.HFWCCOUNT), DECODE(sum(A.HFWCCOUNT),0,'0',ROUND(sum(A.HFWCCOUNT)*100/sum(A.HFCOUNT),2)) || '%'  HFRATE   \n");
			 sql.append("    from a  group by a.ROOT_ORG_NAME,a.PQ_ORG_NAME    \n");
	      }  
		 if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||   Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("    union all  \n");
			 sql.append("    select  '',a.ROOT_ORG_NAME,' ',' ',max(a.ROOT_ORG_NAME)||'合计','2' a, \n");  
			 sql.append("     sum(a.JHSCOUNT), sum(a.SJSCOUNT), DECODE(sum(A.SJSCOUNT),0,'0',ROUND(sum(A.SJSCOUNT)*100/sum(A.JHSCOUNT),2)) || '%'  GZRATE ,  \n");
			 sql.append("     sum(a.XSZLCOUNT), sum(a.SJXSCOUNT), DECODE(sum(A.SJXSCOUNT),0,'0',ROUND(sum(A.SJXSCOUNT)*100/sum(A.XSZLCOUNT),2)) || '%'  XSYYRATE ,  \n");
			 sql.append("     sum(a.ZYYCOUNT), sum(a.ZDDCOUNT), DECODE(sum(A.ZDDCOUNT),0,'0',ROUND(sum(A.ZDDCOUNT)*100/sum(A.ZYYCOUNT),2)) || '%' ZYYRATE , \n");
			 sql.append("     sum(a.HFCOUNT), sum(a.HFWCCOUNT), DECODE(sum(A.HFWCCOUNT),0,'0',ROUND(sum(A.HFWCCOUNT)*100/sum(A.HFCOUNT),2)) || '%'  HFRATE   \n");
			 sql.append("    from a  group by a.ROOT_ORG_NAME   \n");
		 }  
		  if(  Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			  sql.append("    union all  \n");
				 sql.append("    select '', ' ',' ',' ', '全国总计','1' a, \n");  
				 sql.append("     sum(a.JHSCOUNT), sum(a.SJSCOUNT), DECODE(sum(A.SJSCOUNT),0,'0',ROUND(sum(A.SJSCOUNT)*100/sum(A.JHSCOUNT),2)) || '%'  GZRATE ,  \n");
				 sql.append("     sum(a.XSZLCOUNT), sum(a.SJXSCOUNT), DECODE(sum(A.SJXSCOUNT),0,'0',ROUND(sum(A.SJXSCOUNT)*100/sum(A.XSZLCOUNT),2)) || '%'  XSYYRATE ,  \n");
				 sql.append("     sum(a.ZYYCOUNT), sum(a.ZDDCOUNT), DECODE(sum(A.ZDDCOUNT),0,'0',ROUND(sum(A.ZDDCOUNT)*100/sum(A.ZYYCOUNT),2)) || '%' ZYYRATE , \n");
				 sql.append("     sum(a.HFCOUNT), sum(a.HFWCCOUNT), DECODE(sum(A.HFWCCOUNT),0,'0',ROUND(sum(A.HFWCCOUNT)*100/sum(A.HFCOUNT),2)) || '%'  HFRATE   \n");
				 sql.append("    from a    \n");
		  }

		 sql.append("     ) tmp  order by  tmp.a,tmp.ROOT_ORG_NAME, tmp.pq_org_name ,rownum  \n");
		 
		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	
}
