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

public class CallOpportunityBuyDao extends BaseDao<PO>{
private static final CallOpportunityBuyDao dao = new CallOpportunityBuyDao();
private ActionContext act = ActionContext.getContext();
RequestWrapper request = act.getRequest();
	public static final CallOpportunityBuyDao getInstance() {
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
		StringBuilder sql= new StringBuilder(); 	
		sql.append("	 select * from ( with a as   ( \n");
		sql.append("		 select VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME, a.code_desc,to_char(a.dealer_id) dealer_id, \n");
	    List<Map<String,Object>> seriesList=this.getSeriesList();
	    for (int i = 0; i < seriesList.size(); i++) {
	    	Map  mp =(Map)seriesList.get(i);
	         sql.append("	  count(DECODE(up_series_id,'"+mp.get("SERIES_ID")+"', up_series_id)) count"+mp.get("SERIES_ID")+", \n");
		}
	    sql.append("	  count(1) total \n");
		 sql.append("		 from ( \n");
		 sql.append("		 select tc.code_desc, b.up_series_id ,tpc.dealer_id,tpc.adviser from  \n");
		 sql.append("		 ( select a.series_id,a.series_name  from T_PC_INTENT_VEHICLE a   start with a.up_series_id is null \n");
		 sql.append("		    connect by nocycle prior a.series_id = a.up_series_id )a,  T_PC_INTENT_VEHICLE b,T_PC_CUSTOMER tpc   \n");
		 sql.append("		,tc_code tc  where a.series_id =b.series_id and tpc.intent_vehicle=b.series_id  \n");
		 sql.append("		    and tpc.come_reason=tc.code_id   and tc.status='10011001' \n");
		   if(!CommonUtils.isNullString(startDate)){
	            sql.append("      		    AND tpc.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	       if(!CommonUtils.isNullString(endDate)){
	            sql.append("      		  AND tpc.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	          }
 
		 sql.append("	   )a  LEFT JOIN VW_ORG_DEALER_ALL VW   ON to_char(a.DEALER_ID) =to_char(VW.DEALER_ID)  \n");
		 
		 sql.append("	   WHERE  1=1 \n");
		//判断是否顾问登陆
		   if(!CommonUtils.isNullString(userId)) {
				sql.append(" AND a.ADVISER  IN ("+userId+") \n");
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
		 sql.append("	 group by     VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME,a.code_desc,a.dealer_id   \n");
		 sql.append("	  order by VW.ROOT_ORG_NAME,VW.DEALER_SHORTNAME  ) \n");
		 sql.append("	 select ROOT_ORG_NAME, PQ_ORG_NAME, DEALER_CODE, DEALER_SHORTNAME, '1' a, a.code_desc,   \n");
		 for (int i = 0; i < seriesList.size(); i++) {
		    	Map  mp =(Map)seriesList.get(i);
		         sql.append(" count"+mp.get("SERIES_ID")+", \n");
		         sql.append(" DECODE( count"+mp.get("SERIES_ID")+",0,'0',ROUND(  count"+mp.get("SERIES_ID")+"*100/total,2) ) || '%' Rate"+mp.get("SERIES_ID")+", \n");
			}
		 sql.append("	 total from a  \n");
	 
		 if(!CommonUtils.isNullString(manager)){
			 sql.append("  union all   \n");
			 sql.append("select ROOT_ORG_NAME, PQ_ORG_NAME, max(DEALER_CODE), max(DEALER_SHORTNAME),'2' a, '合计',    \n"); 
			 for (int i = 0; i < seriesList.size(); i++) {
			    	Map  mp =(Map)seriesList.get(i);
			         sql.append(" sum(count"+mp.get("SERIES_ID")+"), \n");
			         sql.append(" DECODE( sum(count"+mp.get("SERIES_ID")+"),0,'0',ROUND(sum(count"+mp.get("SERIES_ID")+")*100/sum(total),2) ) || '%' Rate"+mp.get("SERIES_ID")+", \n");
				}
			 sql.append(" sum(total) from a  \n"); 
			 sql.append(" group by  ROOT_ORG_NAME, PQ_ORG_NAME    \n");
		 }
		 sql.append("     ) tmp   order by tmp.ROOT_ORG_NAME,tmp.PQ_ORG_NAME,tmp.DEALER_CODE ,tmp.a, code_desc   \n");
	     	
		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	public List<Map<String,Object>> getSeriesList(){
	        StringBuilder sql=new StringBuilder();
	        sql.append("     SELECT SERIES_ID,SERIES_NAME  FROM  T_PC_INTENT_VEHICLE WHERE STATUS=10011001  AND UP_SERIES_ID IS NULL    order by is_foreign desc ");
	        return dao.pageQuery(sql.toString(),null,null);
	    }
	public List<Map<String, Object>> getStatisticsDaoSelectAll(Map<String, String> map) throws UnsupportedEncodingException{
		String startDate=map.get("startDate");
		String endDate=map.get("endDate");
		String dealerCode=map.get("dealerCode");
		String dealerId=map.get("dealerId");
		String dutyType=map.get("dutyType");
		String orgId=map.get("orgId");
		StringBuilder sql= new StringBuilder(); 	
		sql.append("	 select * from ( with a as   ( \n");
		sql.append("		 select a.code_id,VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME, \n");
		sql.append("		 a.code_desc,to_char(a.dealer_id) dealer_id, \n");
	    List<Map<String,Object>> seriesList=this.getSeriesList();
	    for (int i = 0; i < seriesList.size(); i++) {
	    	Map  mp =(Map)seriesList.get(i);
	         sql.append("	  count(DECODE(up_series_id,'"+mp.get("SERIES_ID")+"', up_series_id)) count"+mp.get("SERIES_ID")+", \n");
		}
	    sql.append("	  count(1) total \n");
		 sql.append("		 from ( \n");
		 sql.append("		 select tc.code_id,tc.code_desc, b.up_series_id ,tpc.dealer_id,tpc.adviser from  \n");
		 sql.append("		 ( select a.series_id,a.series_name  from T_PC_INTENT_VEHICLE a   start with a.up_series_id is null \n");
		 sql.append("		    connect by nocycle prior a.series_id = a.up_series_id )a,  T_PC_INTENT_VEHICLE b,T_PC_CUSTOMER tpc   \n");
		 sql.append("		,tc_code tc  where a.series_id =b.series_id and tpc.intent_vehicle=b.series_id  \n");
		 sql.append("		    and tpc.come_reason=tc.code_id   and tc.status='10011001' \n");
		   if(!CommonUtils.isNullString(startDate)){
	            sql.append("      		    AND tpc.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	       if(!CommonUtils.isNullString(endDate)){
	            sql.append("      		  AND tpc.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	          }
 
		 sql.append("	   )a  LEFT JOIN VW_ORG_DEALER_ALL VW   ON to_char(a.DEALER_ID) =to_char(VW.DEALER_ID)    \n");
		 sql.append("	   WHERE  1=1 \n");
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
		 sql.append("	 group by     VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME,a.code_desc,a.dealer_id,a.code_id  \n");
		 sql.append("	 ) \n");
		 sql.append("	 select code_id,dealer_id,'3' a,ROOT_ORG_NAME, PQ_ORG_NAME, DEALER_CODE, DEALER_SHORTNAME,  \n");
		 sql.append("	 a.code_desc,    \n");
		 for (int i = 0; i < seriesList.size(); i++) {
		    	Map  mp =(Map)seriesList.get(i);
		         sql.append(" count"+mp.get("SERIES_ID")+", \n");
		         sql.append(" DECODE( count"+mp.get("SERIES_ID")+",0,'0',ROUND(  count"+mp.get("SERIES_ID")+"*100/total,2) ) || '%' Rate"+mp.get("SERIES_ID")+", \n");
			}
		 sql.append("	 total from a  \n");
		 if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType) || Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("        union all  \n");
			 sql.append("	  select max(code_id),'', '3' a, a.ROOT_ORG_NAME,a.PQ_ORG_NAME,' ',max(a.PQ_ORG_NAME)||'小计',  \n");
			 sql.append("	 a.code_desc,  \n");
			 for (int i = 0; i < seriesList.size(); i++) {
			    	Map  mp =(Map)seriesList.get(i);
			         sql.append(" sum(count"+mp.get("SERIES_ID")+"), \n");
			         sql.append(" DECODE( sum(count"+mp.get("SERIES_ID")+"),0,'0',ROUND(  sum(count"+mp.get("SERIES_ID")+")*100/sum(total),2) ) || '%' Rate"+mp.get("SERIES_ID")+", \n");
				}
			 sql.append("	 sum(total)  \n");
			 sql.append("   from a  group by  a.ROOT_ORG_NAME,a.PQ_ORG_NAME,a.code_desc   \n");

		 }
		  if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				 sql.append("        union all  \n");
			  sql.append("	  select max(code_id),'','2' a,  a.ROOT_ORG_NAME,' ',' ',max(a.ROOT_ORG_NAME)||'合计',  \n");
				 sql.append("	 a.code_desc,  \n");
				 for (int i = 0; i < seriesList.size(); i++) {
				    	Map  mp =(Map)seriesList.get(i);
				         sql.append(" sum(count"+mp.get("SERIES_ID")+"), \n");
				         sql.append(" DECODE( sum(count"+mp.get("SERIES_ID")+"),0,'0',ROUND(  sum(count"+mp.get("SERIES_ID")+")*100/sum(total),2) ) || '%' Rate"+mp.get("SERIES_ID")+", \n");
					}
				 sql.append("	 sum(total)  \n");
				 sql.append("   from a  group by  a.ROOT_ORG_NAME,a.code_desc    \n");
		  }
		  if( Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				 sql.append("        union all  \n");
			  sql.append("	  select max(code_id),'', '1' a, ' ',' ',' ','全国总计',  \n");
				 sql.append("	 a.code_desc,  \n");
				 for (int i = 0; i < seriesList.size(); i++) {
				    	Map  mp =(Map)seriesList.get(i);
				         sql.append(" sum(count"+mp.get("SERIES_ID")+"), \n");
				         sql.append(" DECODE( sum(count"+mp.get("SERIES_ID")+"),0,'0',ROUND(  sum(count"+mp.get("SERIES_ID")+")*100/sum(total),2) ) || '%' Rate"+mp.get("SERIES_ID")+", \n");
					}
				 sql.append("	 sum(total)  \n");
				 sql.append("   from a  group by  a.code_desc  \n");
		  }

			 
			 sql.append("   ) tmp order by   tmp.a,tmp.ROOT_ORG_NAME , tmp.pq_org_name ,dealer_code desc , code_id,rownum   \n");
		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
}
