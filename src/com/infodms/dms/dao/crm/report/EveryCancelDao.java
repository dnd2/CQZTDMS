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

public class EveryCancelDao extends BaseDao<PO>{
private static final EveryCancelDao dao = new EveryCancelDao();
private ActionContext act = ActionContext.getContext();
RequestWrapper request = act.getRequest();
	public static final EveryCancelDao getInstance() {
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
	     sql.append("   SELECT  VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME,tu.name, tpg.group_id,tpg.group_name,  \n");
	 	 sql.append(" JD.DEALER_ID,JD.ADVISER, to_number(JD.XSZL) XSZLCOUNT, \n");
		 sql.append(" to_number(JD.YXZL) YXZLCOUNT,to_number(JD.SCZL) SCZLCOUNT ,to_number(JD.XDZL) XDZLCOUNT ,to_number(JD.DYZL) DYZLCOUNT,  \n");
		 sql.append(" DECODE(JD.XSZL,0,'0',DECODE(JD.XDZL,0,'0',ROUND(JD.XDZL*100/JD.XSZL,2))) || '%' HFRATE   ");
	     
	     sql.append("  FROM (  SELECT TO_CHAR(JD.ADVISER)ADVISER,TO_CHAR(JD.DEALER_ID) DEALER_ID,  \n");
	     sql.append(" 	  sum(XSZL) XSZL,sum(YXZL) YXZL,sum(SCZL) SCZL,sum(XDZL) XDZL,sum(DYZL) DYZL     \n");
	     sql.append("  FROM ( \n");
	     sql.append(" select tpt.adviser,tpt.dealer_id,count(1) XSZL,0 YXZL,0 SCZL, 0 XDZL,0 DYZL from  \n");
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
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,count(1) YXZL ,0 SCZL ,0 XDZL,0 DYZL from  \n");
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
	     
	     sql.append("  union all   \n");
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,count(1) SCZL,0 XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpl.leads_status='60161001' and tpt.status='10011001' \n");
	     sql.append(" and tpt.if_confirm='60321002' and tpt.confirm_date is not null \n");
	     sql.append(" and (trunc(tpt.confirm_date)-trunc(tpl.create_date))<= \n");
	     if(!"".equals(follow_time)&&follow_time!=null){
	    	 String sctime=follow_time.substring(follow_time.length()-1, follow_time.length());
	    	 int schours= Integer.parseInt(sctime);
	    	 sql.append(" '"+schours*2+"' ");
	     }else{
	    	 sql.append(" '"+12+"' ");
	     }
	    
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
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,count(1) XDZL,0 DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpl.leads_status='60161001' and tpt.status='10011001' \n");
	     sql.append(" and tpt.if_confirm='60321002' and tpt.confirm_date is not null \n");
	     sql.append(" and (trunc(tpt.confirm_date)-trunc(tpl.create_date))<=24 \n");
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
	     
	     sql.append(" select tpt.adviser,tpt.dealer_id,0 XSZL,0 YXZL,0 SCZL,0 XDZL,count(1) DYZL  from  \n");
	     sql.append(" t_pc_leads tpl,t_pc_leads_allot tpt where tpl.leads_code=tpt.leads_code \n");
	     sql.append(" and tpl.leads_status='60161001' and tpt.status='10011001' \n");
	     sql.append(" and tpt.if_confirm='60321002' and tpt.confirm_date is not null \n");
	     sql.append(" and (trunc(tpt.confirm_date)-trunc(tpl.create_date))>24 \n");
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
		 sql.append("   A.XSZLCOUNT,A.YXZLCOUNT,A.SCZLCOUNT ,A.XDZLCOUNT ,A.DYZLCOUNT,A.HFRATE from a  \n");
		 if(!CommonUtils.isNullString(flag)) {
			 sql.append("  union all   \n");
			 sql.append("  select   A.ROOT_ORG_NAME,A.PQ_ORG_NAME,A.DEALER_CODE,A.DEALER_SHORTNAME,'1' ,max(a.group_name)||'小计', a.group_name ,  \n");
			 sql.append("  sum(A.XSZLCOUNT),sum(A.YXZLCOUNT),sum(A.SCZLCOUNT),sum(A.XDZLCOUNT),sum(A.DYZLCOUNT),  \n" );
			 sql.append(" DECODE(sum(A.XSZLCOUNT),0,'0',DECODE(sum(A.XDZLCOUNT),0,'0',ROUND(sum(A.XDZLCOUNT)*100/sum(A.XSZLCOUNT),2))) || '%' HFRATE  \n");
			 sql.append(" from a group by A.ROOT_ORG_NAME,A.PQ_ORG_NAME,A.DEALER_CODE,A.DEALER_SHORTNAME, a.group_name    \n");
			  if(!CommonUtils.isNullString(manager)){
				 sql.append("  union all   \n");
				 sql.append("  select   A.ROOT_ORG_NAME,A.PQ_ORG_NAME,max(A.DEALER_CODE),max(A.DEALER_SHORTNAME),'3'  ,max(a.DEALER_SHORTNAME)||'总计',' ',  \n");
				 sql.append("  sum(A.XSZLCOUNT),sum(A.YXZLCOUNT),sum(A.SCZLCOUNT),sum(A.XDZLCOUNT),sum(A.DYZLCOUNT),  \n" );
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
	
		StringBuilder sql= new StringBuilder(); 			
		sql.append("     select * from (with a as (    \n");
	     sql.append("   SELECT  VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME,  \n");
	     sql.append(" JD.DEALER_ID, to_number(JD.XSZL) XSZLCOUNT, \n");
	     sql.append(" to_number(JD.WFXS) WFXSCOUNT,to_number(JD.XFXS) XFXSCOUNT ,to_number(JD.HMZQ) HMZQCOUNT ,to_number(JD.HMCW) HMCWCOUNT,   \n");
	     sql.append(" DECODE(JD.XSZL,0,'0',DECODE(JD.HMZQ,0,'0',ROUND(JD.HMZQ*100/JD.XSZL,2))) || '%' HMZQRATE     FROM (    ");
	     
	  	 sql.append("   SELECT TO_CHAR(jd.Cus_Dea_Id) DEALER_ID,  sum(XSZL) XSZL,sum(WFXS) WFXS,sum(XFXS) XFXS,sum(HMZQ) HMZQ,sum(HMCW) HMCW     FROM (   \n");

	     sql.append(" select t.Cus_Dea_Id,count(1) XSZL,0 WFXS,0 XFXS,0 HMZQ,0 HMCW from INTERFACE.INTERFACE_CRM_CUSTOMER T where 1=1  \n");
	   
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and T.Cre_Time >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and T.Cre_Time <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	            	
	            	if(arr[i]=="60151017" || "60151017".equals(arr[i])){
	            		arr[i]="汽车之家";
	            	}
	            	if(arr[i]=="60151018" || "60151018".equals(arr[i])){
	            		arr[i]="易车网";
	            	}
	            	if(arr[i]=="60151003" || "60151003".equals(arr[i])){
	            		arr[i]="客户中心";
	            	}
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND TRIM(T.CUS_CHAN) IN("+str+")\n");
			}
	     sql.append("  group by Cus_Dea_Id \n");
	     
	     sql.append("  union all   \n");
	     
	     sql.append("  select t.Cus_Dea_Id,0 XSZL,count(1) WFXS,0 XFXS,0 HMZQ,0 HMCW from INTERFACE.INTERFACE_CRM_CUSTOMER T where T.Status in ('2','0') \n");
	   
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and T.Cre_Time >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and T.Cre_Time <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";


	            for(int i=0;i<arr.length;i++){
	            	
	            	if(arr[i]=="60151017" || "60151017".equals(arr[i])){
	            		arr[i]="汽车之家";
	            	}
	            	if(arr[i]=="60151018" || "60151018".equals(arr[i])){
	            		arr[i]="易车网";
	            	}
	            	if(arr[i]=="60151003" || "60151003".equals(arr[i])){
	            		arr[i]="客户中心";
	            	}
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND TRIM(T.CUS_CHAN) IN("+str+")\n");
			}
	     sql.append("  group by Cus_Dea_Id \n");
	     
	     sql.append("  union all   \n");
	     
	     sql.append(" select t.Cus_Dea_Id,0 XSZL,0 WFXS,count(1) XFXS,0 HMZQ,0 HMCW from INTERFACE.INTERFACE_CRM_CUSTOMER T where T.Status='1' \n");
	    
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and T.Cre_Time >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and T.Cre_Time <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";


	            for(int i=0;i<arr.length;i++){
	            	
	            	if(arr[i]=="60151017" || "60151017".equals(arr[i])){
	            		arr[i]="汽车之家";
	            	}
	            	if(arr[i]=="60151018" || "60151018".equals(arr[i])){
	            		arr[i]="易车网";
	            	}
	            	if(arr[i]=="60151003" || "60151003".equals(arr[i])){
	            		arr[i]="客户中心";
	            	}
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND TRIM(T.CUS_CHAN) IN("+str+")\n");
			}
	     sql.append("  group by Cus_Dea_Id \n");
	     
	     sql.append("  union all   \n");
	     
	     sql.append("  select t.Cus_Dea_Id,0 XSZL,0 WFXS,0 XFXS,count(1) HMZQ,0 HMCW from INTERFACE.INTERFACE_CRM_CUSTOMER T where T.Status='1' and T.Yi_Type is null  \n");
	   
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and T.Cre_Time >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and T.Cre_Time <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";


	            for(int i=0;i<arr.length;i++){
	            	
	            	if(arr[i]=="60151017" || "60151017".equals(arr[i])){
	            		arr[i]="汽车之家";
	            	}
	            	if(arr[i]=="60151018" || "60151018".equals(arr[i])){
	            		arr[i]="易车网";
	            	}
	            	if(arr[i]=="60151003" || "60151003".equals(arr[i])){
	            		arr[i]="客户中心";
	            	}
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND TRIM(T.CUS_CHAN) IN("+str+")\n");
			}
	     sql.append("  group by Cus_Dea_Id \n");
	     
	     sql.append("  union all   \n");
	     
	     sql.append("  select t.Cus_Dea_Id,0 XSZL,0 WFXS,0 XFXS,0 HMZQ,count(1) HMCW from INTERFACE.INTERFACE_CRM_CUSTOMER T where T.Status='1' and T.Yi_Type is not null  \n");
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("  and T.Cre_Time >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("  and T.Cre_Time <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!"".equals(originId)&&originId!=null){
	            String arr[]=originId.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	            	
	            	if(arr[i]=="60151017" || "60151017".equals(arr[i])){
	            		arr[i]="汽车之家";
	            	}
	            	if(arr[i]=="60151018" || "60151018".equals(arr[i])){
	            		arr[i]="易车网";
	            	}
	            	if(arr[i]=="60151003" || "60151003".equals(arr[i])){
	            		arr[i]="客户中心";
	            	}
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND TRIM(T.CUS_CHAN) IN("+str+")\n");
			}
	     sql.append("  group by Cus_Dea_Id \n");
	     
	     sql.append("     )jd  group by jd.Cus_Dea_Id\n");
	     
	     sql.append("      )JD  LEFT JOIN VW_ORG_DEALER_ALL_NEW VW    ON TRIM(JD.DEALER_ID) =VW.dealer_code  \n");
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
		 sql.append("   select  A.ROOT_ORG_NAME,A.PQ_ORG_NAME,A.DEALER_CODE,A.DEALER_SHORTNAME,A.DEALER_ID,'3' a,   \n");
		 sql.append("   A.XSZLCOUNT,A.WFXSCOUNT,A.XFXSCOUNT ,A.HMZQCOUNT ,A.HMCWCOUNT,A.HMZQRATE  from a  \n");
		 if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||   Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("  union all   \n");
			 sql.append("  select   A.ROOT_ORG_NAME,A.PQ_ORG_NAME,' ',max(A.PQ_ORG_NAME)||'小计','','3' ,   \n");
			 sql.append("  sum(A.XSZLCOUNT),sum(A.WFXSCOUNT),sum(A.XFXSCOUNT),sum(A.HMZQCOUNT),sum(A.HMCWCOUNT),  \n" );
			 sql.append(" DECODE(sum(A.XSZLCOUNT),0,'0',DECODE(sum(A.HMZQCOUNT),0,'0',ROUND(sum(A.HMZQCOUNT)*100/sum(A.XSZLCOUNT),2))) || '%' HMZQRATE   \n");
				 
			sql.append(" from a  group by A.ROOT_ORG_NAME,A.PQ_ORG_NAME    \n");
		 }  if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||   Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				 sql.append("  union all   \n");
				 sql.append("   select   A.ROOT_ORG_NAME,' ',' ',max(a.ROOT_ORG_NAME)||'合计','','2'  ,  \n");
				 sql.append("  sum(A.XSZLCOUNT),sum(A.WFXSCOUNT),sum(A.XFXSCOUNT),sum(A.HMZQCOUNT),sum(A.HMCWCOUNT),  \n" );
				 sql.append(" DECODE(sum(A.XSZLCOUNT),0,'0',DECODE(sum(A.HMZQCOUNT),0,'0',ROUND(sum(A.HMZQCOUNT)*100/sum(A.XSZLCOUNT),2))) || '%' HMZQRATE   \n");
				sql.append(" from a group by A.ROOT_ORG_NAME   \n");
		   }
		   if(  Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("  union all   \n");
			 sql.append("  select '',  ' ',' ' , ' ','全国合计','1' ,   \n");
			 sql.append("  sum(A.XSZLCOUNT),sum(A.WFXSCOUNT),sum(A.XFXSCOUNT),sum(A.HMZQCOUNT),sum(A.HMCWCOUNT),  \n" );
			 sql.append(" DECODE(sum(A.XSZLCOUNT),0,'0',DECODE(sum(A.HMZQCOUNT),0,'0',ROUND(sum(A.HMZQCOUNT)*100/sum(A.XSZLCOUNT),2))) || '%' HMZQRATE   \n");
			 sql.append(" from a   \n");
	 }
		 sql.append("  ) tmp  order by  tmp.a,tmp.ROOT_ORG_NAME, tmp.pq_org_name ,rownum  \n");
		 
		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
 
}
