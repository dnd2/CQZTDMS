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

public class ReturnVisitDao extends BaseDao<PO>{
private static final ReturnVisitDao dao = new ReturnVisitDao();
private ActionContext act = ActionContext.getContext();
RequestWrapper request = act.getRequest();
	public static final ReturnVisitDao getInstance() {
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
		String originId=map.get("originId");
		String follow_time=map.get("follow_time");
		StringBuilder sql= new StringBuilder(); 			
		sql.append("     select * from (with a as (    \n");
	     sql.append("   SELECT  VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME,DECODE(JD.ADVISER,'','销售经理',gw.name) name,gw.group_id, DECODE(JD.ADVISER,'','销售经理',gw.group_name) group_name,   \n");
	 	 sql.append(" JD.DEALER_ID,JD.ADVISER, to_number(JD.XSZL) XSZLCOUNT, \n");
		 sql.append(" to_number(JD.YXZL) YXZLCOUNT,to_number(JD.SCZL) SCZLCOUNT ,DECODE(JD.XSZL,0,'0',DECODE(JD.SCZL,0,'0',ROUND(JD.SCZL*100/JD.XSZL,2))) || '%' SCHFRATE,to_number(JD.WCZL) WCZLCOUNT,to_number(JD.XDZL) XDZLCOUNT ,to_number(JD.DYZL) DYZLCOUNT,  \n");
		 sql.append(" DECODE(JD.XSZL,0,'0',DECODE(JD.XDZL,0,'0',ROUND(JD.XDZL*100/JD.XSZL,2))) || '%' HFRATE   ");
	     
	     sql.append("  FROM (  SELECT TO_CHAR(JD.ADVISER)ADVISER,TO_CHAR(JD.DEALER_ID) DEALER_ID,  \n");
	     sql.append(" 	  sum(XSZL) XSZL,sum(YXZL) YXZL,sum(SCZL) SCZL,sum(WCZL) WCZL,sum(XDZL) XDZL,sum(DYZL) DYZL     \n");
	     sql.append("  FROM ( \n");
	     sql.append(" select tpt.adviser,tpt.dealer_id,count(1) XSZL,0 YXZL,0 SCZL,0 WCZL, 0 XDZL,0 DYZL from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id,adviser \n");
	     
	     sql.append("  union all   \n");
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,count(1) YXZL ,0 SCZL ,0 WCZL,0 XDZL,0 DYZL from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpl.leads_status='60161001' and tpt.status='10011001' \n");
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id,adviser \n");
	     
	     //经销商计算首次时长开始
	    
	     sql.append("  union all   \n");
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,count(1) SCZL,0 WCZL,0 XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	     //sql.append(" and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-tpl.create_date)* 24)<= \n");
	     sql.append("  and( case when tpt.confirm_date >= TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')  \n");
	     sql.append("  then  ceil((TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')-tpl.create_date)* 24) \n");
	     sql.append("  + ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24) \n");
	     sql.append(" else  case when to_CHAR(tpl.create_date,'hh24') in ('00','01','02','03','04','05','06','07','08') \n");
	     sql.append("      then ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 09:00:00','YYYY-MM-DD HH24:MI:SS'))* 24) \n");
	     sql.append("       else ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-tpl.create_date)* 24) \n");
	     sql.append("        end \n");
	     sql.append("  end )<= \n");
	     if(!"".equals(follow_time)&&follow_time!=null){
	    	 String sctime=follow_time.substring(follow_time.length()-1, follow_time.length());
	    	 int schours= Integer.parseInt(sctime);
	    	 sql.append(" '"+schours*2+"' ");
	     }else{
	    	 sql.append(" '"+4+"' ");
	     }
	    
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     //sql.append("  and  to_CHAR(tpl.create_date,'hh24') not in ('21','22','23','00','01','02','03','04','05','06','07','08') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id,adviser \n");
	     
	     /*
	     sql.append("  union all   \n");
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,count(1) SCZL,0 WCZL,0 XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24)<=  \n");
		 if(!"".equals(follow_time)&&follow_time!=null){
	    	 String sctime=follow_time.substring(follow_time.length()-1, follow_time.length());
	    	 int schours= Integer.parseInt(sctime);
	    	 sql.append(" '"+schours*2+"' ");
	     }else{
	    	 sql.append(" '"+4+"' ");
	     }
	    
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24')  in ('21','22','23') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id,adviser \n");
	     
	     sql.append("  union all   \n");
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,count(1) SCZL,0 WCZL,0 XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 09:00:00','YYYY-MM-DD HH24:MI:SS'))* 24)<=  \n");
		 if(!"".equals(follow_time)&&follow_time!=null){
	    	 String sctime=follow_time.substring(follow_time.length()-1, follow_time.length());
	    	 int schours= Integer.parseInt(sctime);
	    	 sql.append(" '"+schours*2+"' ");
	     }else{
	    	 sql.append(" '"+4+"' ");
	     }
	    
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24') in ('00','01','02','03','04','05','06','07','08') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id,adviser \n");
	     */
	   //经销商计算首次时长结束
	     
	     
	     
	     
	     
	     //经销商计算首次时长开始首次未处理量
		    
	     sql.append("  union all   \n");
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,count(1) WCZL,0 XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is  null \n");
	     sql.append(" and ceil((TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')-tpl.create_date)* 24)   \n");
	     sql.append("    + ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24)<=    \n");
	  
	     if(!"".equals(follow_time)&&follow_time!=null){
	    	 String sctime=follow_time.substring(follow_time.length()-1, follow_time.length());
	    	 int schours= Integer.parseInt(sctime);
	    	 sql.append(" '"+schours*2+"' ");
	     }else{
	    	 sql.append(" '"+4+"' ");
	     }
	    
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24') not in ('21','22','23','00','01','02','03','04','05','06','07','08') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id,adviser \n");
	     
	     sql.append("  union all   \n");
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,count(1) WCZL,0 XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is  null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24)<=  \n");
		 if(!"".equals(follow_time)&&follow_time!=null){
	    	 String sctime=follow_time.substring(follow_time.length()-1, follow_time.length());
	    	 int schours= Integer.parseInt(sctime);
	    	 sql.append(" '"+schours*2+"' ");
	     }else{
	    	 sql.append(" '"+4+"' ");
	     }
	    
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24')  in ('21','22','23') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id,adviser \n");
	     
	     sql.append("  union all   \n");
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,count(1) WCZL,0 XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is  null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 09:00:00','YYYY-MM-DD HH24:MI:SS'))* 24)<=  \n");
		 if(!"".equals(follow_time)&&follow_time!=null){
	    	 String sctime=follow_time.substring(follow_time.length()-1, follow_time.length());
	    	 int schours= Integer.parseInt(sctime);
	    	 sql.append(" '"+schours*2+"' ");
	     }else{
	    	 sql.append(" '"+4+"' ");
	     }
	    
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24') in ('00','01','02','03','04','05','06','07','08') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id,adviser \n");
	     
	   //经销商计算首次时长结束首次未处理量
	     
	     
	     
	     
	     sql.append("  union all   \n");
	   //经销商计算小于等于12小时的首次时长开始
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 WCZL,count(1) XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	    // sql.append(" and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-tpl.create_date)* 24)<=12 \n");
	     sql.append("  and( case when tpt.confirm_date >= TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')  \n");
	     sql.append("  then  ceil((TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')-tpl.create_date)* 24) \n");
	     sql.append("  + ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24) \n");
	     sql.append(" else  case when to_CHAR(tpl.create_date,'hh24') in ('00','01','02','03','04','05','06','07','08') \n");
	     sql.append("      then ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 09:00:00','YYYY-MM-DD HH24:MI:SS'))* 24) \n");
	     sql.append("       else ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-tpl.create_date)* 24) \n");
	     sql.append("        end \n");
	     sql.append("  end )<=12 \n");
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     //sql.append("  and  to_CHAR(tpl.create_date,'hh24') not in ('21','22','23','00','01','02','03','04','05','06','07','08') ");
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id,adviser \n");
	     
	     /*	   
	     sql.append("  union all   \n");
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 WCZL,count(1) XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24)<=12 \n");
		 if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24') in ('21','22','23') ");
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id,adviser \n");
	     
	      
	     sql.append("  union all   \n");
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 WCZL,count(1) XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 09:00:00','YYYY-MM-DD HH24:MI:SS'))* 24)<=12 \n");
		 if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24') in ('00','01','02','03','04','05','06','07','08') ");
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id,adviser \n");
	     */
	     //经销商计算小于等于12小时的首次时长 结束
	     sql.append("  union all   \n");
	     //经销商计算大于12小时的首次时长 开始
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 WCZL,0 XDZL,count(1) DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is null \n");
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }

	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}    
	     sql.append(" group by adviser,dealer_id \n");
	     
	     sql.append("  union all   \n");
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 WCZL,0 XDZL,count(1) DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	    // sql.append(" and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-tpl.create_date)* 24)>12 \n");
	     sql.append("  and( case when tpt.confirm_date >= TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')  \n");
	     sql.append("  then  ceil((TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')-tpl.create_date)* 24) \n");
	     sql.append("  + ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24) \n");
	     sql.append(" else  case when to_CHAR(tpl.create_date,'hh24') in ('00','01','02','03','04','05','06','07','08') \n");
	     sql.append("      then ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 09:00:00','YYYY-MM-DD HH24:MI:SS'))* 24) \n");
	     sql.append("       else ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-tpl.create_date)* 24) \n");
	     sql.append("        end \n");
	     sql.append("  end )>12 \n");
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     //sql.append("  and  to_CHAR(tpl.create_date,'hh24') not in ('21','22','23','00','01','02','03','04','05','06','07','08') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}    
	     sql.append(" group by adviser,dealer_id \n");
	     
	     /*
	     sql.append("  union all   \n");
	     
	     //经销商计算大于12小时的首次时长 开始
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 WCZL,0 XDZL,count(1) DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24)>12 \n");
		 if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24') in ('21','22','23') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}    
	     sql.append(" group by adviser,dealer_id \n");
	     
	     sql.append("  union all   \n");
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 WCZL,0 XDZL,count(1) DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 09:00:00','YYYY-MM-DD HH24:MI:SS'))* 24)>12 \n");
		 if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24') in ('00','01','02','03','04','05','06','07','08') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}    
	     sql.append(" group by adviser,dealer_id \n");
	     
	 */
	     
	     
	     sql.append("     )jd  group by jd.DEALER_ID ,JD.ADVISER\n");
	     
	     sql.append("      )JD  LEFT JOIN VW_ORG_DEALER_ALL_NEW VW   ON JD.DEALER_ID =VW.DEALER_ID  \n");
		 sql.append("   left join \n");
		 sql.append("   ( select tu.user_id,tu.name,tpg.group_id,tpg.group_name from tc_user tu,t_pc_group tpg  where   tu.group_id=tpg.group_id )gw  on JD.ADVISER =gw.user_id   where 1=1  \n");
                 
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
		 sql.append("   A.XSZLCOUNT,A.YXZLCOUNT,A.SCZLCOUNT ,A.SCHFRATE,A.WCZLCOUNT,A.XDZLCOUNT ,A.DYZLCOUNT,A.HFRATE from a  \n");
		 if(!CommonUtils.isNullString(flag)) {
			 sql.append("  union all   \n");
			 sql.append("  select   A.ROOT_ORG_NAME,A.PQ_ORG_NAME,A.DEALER_CODE,A.DEALER_SHORTNAME,'1' ,max(a.group_name)||'小计', a.group_name ,  \n");
			 sql.append("  sum(A.XSZLCOUNT),sum(A.YXZLCOUNT),sum(A.SCZLCOUNT),  DECODE(sum(A.XSZLCOUNT),0,'0',DECODE(sum(A.SCZLCOUNT),0,'0',ROUND(sum(A.SCZLCOUNT)*100/sum(A.XSZLCOUNT),2))) || '%' SCHFRATE,sum(A.WCZLCOUNT), sum(A.XDZLCOUNT),sum(A.DYZLCOUNT),  \n" );
			 sql.append(" DECODE(sum(A.XSZLCOUNT),0,'0',DECODE(sum(A.XDZLCOUNT),0,'0',ROUND(sum(A.XDZLCOUNT)*100/sum(A.XSZLCOUNT),2))) || '%' HFRATE  \n");
			 sql.append(" from a group by A.ROOT_ORG_NAME,A.PQ_ORG_NAME,A.DEALER_CODE,A.DEALER_SHORTNAME, a.group_name    \n");
			  if(!CommonUtils.isNullString(manager)){
				 sql.append("  union all   \n");
				 sql.append("  select   A.ROOT_ORG_NAME,A.PQ_ORG_NAME,max(A.DEALER_CODE),max(A.DEALER_SHORTNAME),'3'  ,max(a.DEALER_SHORTNAME)||'总计',' ',  \n");
				 sql.append("  sum(A.XSZLCOUNT),sum(A.YXZLCOUNT),sum(A.SCZLCOUNT),  DECODE(sum(A.XSZLCOUNT),0,'0',DECODE(sum(A.SCZLCOUNT),0,'0',ROUND(sum(A.SCZLCOUNT)*100/sum(A.XSZLCOUNT),2))) || '%' SCHFRATE,sum(A.WCZLCOUNT), sum(A.XDZLCOUNT),sum(A.DYZLCOUNT),  \n" );
				 sql.append(" DECODE(sum(A.XSZLCOUNT),0,'0',DECODE(sum(A.XDZLCOUNT),0,'0',ROUND(sum(A.XDZLCOUNT)*100/sum(A.XSZLCOUNT),2))) || '%' HFRATE  \n");
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
		String originId=map.get("originId");
		String follow_time=map.get("follow_time");
		System.out.println("时长是:"+follow_time);
		StringBuilder sql= new StringBuilder(); 			
		sql.append("     select * from (with a as (    \n");
	     sql.append("   SELECT  VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME,  \n");
	     sql.append(" JD.DEALER_ID, to_number(JD.XSZL) XSZLCOUNT, \n");
	     sql.append(" to_number(JD.YXZL) YXZLCOUNT,to_number(JD.SCZL) SCZLCOUNT,DECODE(JD.XSZL,0,'0',DECODE(JD.SCZL,0,'0',ROUND(JD.SCZL*100/JD.XSZL,2))) || '%' SCHFRATE,to_number(JD.WCZL) WCZLCOUNT,to_number(JD.XDZL) XDZLCOUNT ,to_number(JD.DYZL) DYZLCOUNT,  \n");
	     sql.append(" DECODE(JD.XSZL,0,'0',DECODE(JD.XDZL,0,'0',ROUND(JD.XDZL*100/JD.XSZL,2))) || '%' HFRATE   ");
	     
	  	 sql.append("  FROM (  SELECT TO_CHAR(JD.DEALER_ID) DEALER_ID,  \n");
	     sql.append(" 	  sum(XSZL) XSZL,sum(YXZL) YXZL,sum(SCZL) SCZL,sum(WCZL) WCZL,sum(XDZL) XDZL,sum(DYZL) DYZL     \n");
	     sql.append("  FROM ( \n");
	     sql.append(" select tpt.dealer_id,count(1) XSZL,0 YXZL,0 SCZL,0 WCZL, 0 XDZL,0 DYZL from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     
	     sql.append("  union all   \n");
	     
	     sql.append(" select tpt.dealer_id,0 XSZL,count(1) YXZL ,0 SCZL ,0 WCZL,0 XDZL,0 DYZL from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpl.leads_status='60161001' and tpt.status='10011001' \n");
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     
	     sql.append("  union all   \n");
	     
	     
	     //计算首次时长当日21点后到次日9点前的线索首次时长按照次日9点开始计算首次时长 开始
	     
	 
	     
	     
	     sql.append(" select tpt.dealer_id,0 XSZL,0 YXZL,count(1) SCZL,0 WCZL,0 XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	    // sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-tpl.create_date)* 24)<=  \n");
	     sql.append("  and( case when tpt.confirm_date >= TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')  \n");
	     sql.append("  then  ceil((TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')-tpl.create_date)* 24) \n");
	     sql.append("  + ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24) \n");
	     sql.append(" else  case when to_CHAR(tpl.create_date,'hh24') in ('00','01','02','03','04','05','06','07','08') \n");
	     sql.append("      then ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 09:00:00','YYYY-MM-DD HH24:MI:SS'))* 24) \n");
	     sql.append("       else ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-tpl.create_date)* 24) \n");
	     sql.append("        end \n");
	     sql.append("  end )<= \n");
	     if(!"".equals(follow_time)&&follow_time!=null){
	    	 String sctime=follow_time.substring(follow_time.length()-1, follow_time.length());
	    	 int schours= Integer.parseInt(sctime);
	    	 sql.append(" '"+schours*2+"' ");
	     }else{
	    	 sql.append(" '"+4+"' ");
	     }
	    
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	    // sql.append("  and  to_CHAR(tpl.create_date,'hh24') not in ('21','22','23','00','01','02','03','04','05','06','07','08') ");
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     
	     /*
	     sql.append("  union all   \n");
	     
	     
	     sql.append(" select tpt.dealer_id,0 XSZL,0 YXZL,count(1) SCZL,0 WCZL,0 XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24)<=  \n");
	     if(!"".equals(follow_time)&&follow_time!=null){
	    	 String sctime=follow_time.substring(follow_time.length()-1, follow_time.length());
	    	 int schours= Integer.parseInt(sctime);
	    	 sql.append(" '"+schours*2+"' ");
	     }else{
	    	 sql.append(" '"+4+"' ");
	     }
	    
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24')  in ('21','22','23') ");
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     
	     sql.append("  union all   \n");
	     
	     
	     
	     sql.append(" select tpt.dealer_id,0 XSZL,0 YXZL,count(1) SCZL,0 WCZL,0 XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 09:00:00','YYYY-MM-DD HH24:MI:SS'))* 24)<=  \n");
	     if(!"".equals(follow_time)&&follow_time!=null){
	    	 String sctime=follow_time.substring(follow_time.length()-1, follow_time.length());
	    	 int schours= Integer.parseInt(sctime);
	    	 sql.append(" '"+schours*2+"' ");
	     }else{
	    	 sql.append(" '"+4+"' ");
	     }
	    
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24') in ('00','01','02','03','04','05','06','07','08') ");
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     */
	     
		   //计算首次时长当日21点后到次日9点前的线索首次时长按照次日9点开始计算首次时长 结束
		     
		  sql.append("  union all   \n");   
	     
	     
     //计算首次时长当日21点后到次日9点前的线索首次时长按照次日9点开始计算首次时长 开始计算首次未处理量
	     
	 
	     
	     
	     sql.append(" select tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,count(1) WCZL,0 XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is null \n");
	     sql.append(" and ceil((TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')-tpl.create_date)* 24)   \n");
	     sql.append("    + ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24)<=    \n");
	     if(!"".equals(follow_time)&&follow_time!=null){
	    	 String sctime=follow_time.substring(follow_time.length()-1, follow_time.length());
	    	 int schours= Integer.parseInt(sctime);
	    	 sql.append(" '"+schours*2+"' ");
	     }else{
	    	 sql.append(" '"+4+"' ");
	     }
	    
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24') not in ('21','22','23','00','01','02','03','04','05','06','07','08') ");
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     
	     sql.append("  union all   \n");
	     
	     
	     sql.append(" select tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,count(1) WCZL,0 XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is  null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24)<=  \n");
	     if(!"".equals(follow_time)&&follow_time!=null){
	    	 String sctime=follow_time.substring(follow_time.length()-1, follow_time.length());
	    	 int schours= Integer.parseInt(sctime);
	    	 sql.append(" '"+schours*2+"' ");
	     }else{
	    	 sql.append(" '"+4+"' ");
	     }
	    
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24')  in ('21','22','23') ");
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     
	     sql.append("  union all   \n");
	     
	     
	     
	     sql.append(" select tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,count(1) WCZL,0 XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 09:00:00','YYYY-MM-DD HH24:MI:SS'))* 24)<=  \n");
	     if(!"".equals(follow_time)&&follow_time!=null){
	    	 String sctime=follow_time.substring(follow_time.length()-1, follow_time.length());
	    	 int schours= Integer.parseInt(sctime);
	    	 sql.append(" '"+schours*2+"' ");
	     }else{
	    	 sql.append(" '"+4+"' ");
	     }
	    
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24') in ('00','01','02','03','04','05','06','07','08') ");
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     
	     
	   //计算首次时长当日21点后到次日9点前的线索首次时长按照次日9点开始计算首次时长 计算首次未处理量结束
	     
	     sql.append("  union all   \n");   
	     
	   //计算首次时长小于12小时的开始计算
	     
	  
	    
	     
	     sql.append(" select tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 WCZL,count(1) XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	     //sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-tpl.create_date)* 24)<=12 \n");
	     sql.append("  and( case when tpt.confirm_date >= TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')  \n");
	     sql.append("  then  ceil((TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')-tpl.create_date)* 24) \n");
	     sql.append("  + ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24) \n");
	     sql.append(" else  case when to_CHAR(tpl.create_date,'hh24') in ('00','01','02','03','04','05','06','07','08') \n");
	     sql.append("      then ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 09:00:00','YYYY-MM-DD HH24:MI:SS'))* 24) \n");
	     sql.append("       else ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-tpl.create_date)* 24) \n");
	     sql.append("        end \n");
	     sql.append("  end )<=12 \n");
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	    // sql.append("  and  to_CHAR(tpl.create_date,'hh24') not in ('21','22','23','00','01','02','03','04','05','06','07','08') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     
	     
	     
	     /*
	     sql.append("  union all   \n");
	     
	     
	     sql.append(" select tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 WCZL,count(1) XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24)<=12 \n");
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24') in ('21','22','23') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     
	     sql.append("  union all   \n");
	     
	     
	     sql.append(" select tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 WCZL,count(1) XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 09:00:00','YYYY-MM-DD HH24:MI:SS'))* 24)<=12 \n");
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24') in ('00','01','02','03','04','05','06','07','08') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     */
	     //计算首次时长小于12小时的结束
	     
	     sql.append("  union all   \n");
	     
	     //计算首次时长大于12小时的开始
	     sql.append(" select tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 WCZL,0 XDZL,count(1) DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is null  \n");
	     
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     
	     sql.append("  union all   \n");
	     
	     sql.append(" select tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 WCZL,0 XDZL,count(1) DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	    // sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-tpl.create_date)* 24)>12 \n");
	     sql.append("  and( case when tpt.confirm_date >= TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')  \n");
	     sql.append("  then  ceil((TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')-tpl.create_date)* 24) \n");
	     sql.append("  + ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24) \n");
	     sql.append(" else  case when to_CHAR(tpl.create_date,'hh24') in ('00','01','02','03','04','05','06','07','08') \n");
	     sql.append("      then ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 09:00:00','YYYY-MM-DD HH24:MI:SS'))* 24) \n");
	     sql.append("       else ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-tpl.create_date)* 24) \n");
	     sql.append("        end \n");
	     sql.append("  end )>12 \n");
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	    // sql.append("  and  to_CHAR(tpl.create_date,'hh24') not in ('21','22','23','00','01','02','03','04','05','06','07','08') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     
	     /*
	     sql.append("  union all   \n");
	     
	     
	     sql.append(" select tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 WCZL,0 XDZL,count(1) DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-(TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 21:00:00','YYYY-MM-DD HH24:MI:SS')+12/24))* 24)>12 \n");
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24') in ('21','22','23') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     
	     sql.append("  union all   \n");
	     
	     
	     sql.append(" select tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 WCZL,0 XDZL,count(1) DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpt.confirm_date is not null \n");
	     sql.append("  and ceil((DECODE(tpt.confirm_date,'',sysdate,tpt.confirm_date)-TO_DATE(TO_CHAR(tpl.create_date,'YYYY-MM-DD') || ' 09:00:00','YYYY-MM-DD HH24:MI:SS'))* 24)>12 \n");
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and tpl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and tpl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     sql.append("  and  to_CHAR(tpl.create_date,'hh24') in ('00','01','02','03','04','05','06','07','08') ");
	     
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND tpl.leads_origin IN("+str+")\n");
			}
	     sql.append("  group by dealer_id \n");
	     */
	     //计算首次时长大于12小时的结束
	     
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
		 sql.append("   A.XSZLCOUNT,A.YXZLCOUNT,A.SCZLCOUNT ,SCHFRATE,A.WCZLCOUNT,A.XDZLCOUNT ,A.DYZLCOUNT,A.HFRATE  from a  \n");
		 if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||   Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("  union all   \n");
			 sql.append("  select   '',A.ROOT_ORG_NAME,A.PQ_ORG_NAME,' ',max(A.PQ_ORG_NAME)||'小计','3' ,   \n");
			 sql.append("  sum(A.XSZLCOUNT),sum(A.YXZLCOUNT),sum(A.SCZLCOUNT),DECODE(sum(A.XSZLCOUNT),0,'0',DECODE(sum(A.SCZLCOUNT),0,'0',ROUND(sum(A.SCZLCOUNT)*100/sum(A.XSZLCOUNT),2))) || '%' SCHFRATE,sum(A.WCZLCOUNT), sum(A.XDZLCOUNT),sum(A.DYZLCOUNT),  \n" );
			 sql.append(" DECODE(sum(A.XSZLCOUNT),0,'0',DECODE(sum(A.XDZLCOUNT),0,'0',ROUND(sum(A.XDZLCOUNT)*100/sum(A.XSZLCOUNT),2))) || '%' HFRATE  \n");
				 
			sql.append(" from a  group by A.ROOT_ORG_NAME,A.PQ_ORG_NAME    \n");
		 }  if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||   Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				 sql.append("  union all   \n");
				 sql.append("   select   '',A.ROOT_ORG_NAME,' ',' ',max(a.ROOT_ORG_NAME)||'合计','2'  ,  \n");
				 
				 sql.append("  sum(A.XSZLCOUNT),sum(A.YXZLCOUNT),sum(A.SCZLCOUNT),DECODE(sum(A.XSZLCOUNT),0,'0',DECODE(sum(A.SCZLCOUNT),0,'0',ROUND(sum(A.SCZLCOUNT)*100/sum(A.XSZLCOUNT),2))) || '%' SCHFRATE,sum(A.WCZLCOUNT), sum(A.XDZLCOUNT),sum(A.DYZLCOUNT),  \n" );
				 sql.append(" DECODE(sum(A.XSZLCOUNT),0,'0',DECODE(sum(A.XDZLCOUNT),0,'0',ROUND(sum(A.XDZLCOUNT)*100/sum(A.XSZLCOUNT),2))) || '%' HFRATE  \n");
				 sql.append(" from a group by A.ROOT_ORG_NAME   \n");
		   }
		   if(  Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("  union all   \n");
			 sql.append("  select '',  ' ',' ' , ' ','全国合计','1' ,   \n");
			 
			 sql.append("  sum(A.XSZLCOUNT),sum(A.YXZLCOUNT),sum(A.SCZLCOUNT),DECODE(sum(A.XSZLCOUNT),0,'0',DECODE(sum(A.SCZLCOUNT),0,'0',ROUND(sum(A.SCZLCOUNT)*100/sum(A.XSZLCOUNT),2))) || '%' SCHFRATE,sum(A.WCZLCOUNT), sum(A.XDZLCOUNT),sum(A.DYZLCOUNT),  \n" );
			 sql.append(" DECODE(sum(A.XSZLCOUNT),0,'0',DECODE(sum(A.XDZLCOUNT),0,'0',ROUND(sum(A.XDZLCOUNT)*100/sum(A.XSZLCOUNT),2))) || '%' HFRATE  \n");
			 sql.append(" from a   \n");
	 }
		 sql.append("  ) tmp  order by  tmp.a,tmp.ROOT_ORG_NAME, tmp.pq_org_name ,rownum  \n");
		 
		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
 
}
