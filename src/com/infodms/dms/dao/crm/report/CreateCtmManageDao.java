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

public class CreateCtmManageDao extends BaseDao<PO>{
private static final CreateCtmManageDao dao = new CreateCtmManageDao();
private ActionContext act = ActionContext.getContext();
RequestWrapper request = act.getRequest();
	public static final CreateCtmManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 车厂端
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCtmManageInfo(Map<String, String> map){
		String startDate=map.get("startDate");
		String endDate=map.get("endDate");
		String dealerCode=map.get("dealerCode");
		String dealerId=map.get("dealerId");
		String dutyType=map.get("dutyType");
		String orgId=map.get("orgId");
		StringBuilder sql= new StringBuilder();
				sql.append("with tpc as (    \n");
				sql.append("   SELECT  VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME,  \n"); 
				sql.append(" tpc.DEALER_ID,tpc.adviser,tpc.ctm_rank,tpc.create_date,tpc.ctm_type   \n");
				sql.append(" from t_pc_customer tpc,VW_ORG_DEALER_ALL_NEW vw   where tpc.dealer_id=vw.dealer_id  \n");
				if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
		 			sql.append("  AND VW.DEALER_ID ="+dealerId+"\n");
		 			//大区
		 		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
		 			
		 			sql.append(" AND VW.DEALER_ID IN (SELECT V.DEALER_ID FROM VW_ORG_DEALER_ALL_NEW V WHERE  V.ROOT_ORG_ID="+orgId+")\n");
		 			//小区
		 		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
		 			sql.append(" AND VW.DEALER_ID IN (SELECT V.DEALER_ID FROM VW_ORG_DEALER_ALL_NEW V WHERE  V.PQ_ORG_ID="+orgId+") \n");
		 		}
				if(!"".equals(dealerCode)&&dealerCode!=null){
		            String arr[]=dealerCode.split(",");
		            String str="";
		            
		            for(int i=0;i<arr.length;i++){
		                str+="'"+arr[i]+"',";
		            }
		            str=str.substring(0,str.lastIndexOf(","));

		            sql.append("     and  vw.dealer_code in ("+str+") \n");
				}
				if(!CommonUtils.isNullString(startDate)){
		            sql.append("   and tpc.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
				 	}
			    if(!CommonUtils.isNullString(endDate)){
			        sql.append("   and tpc.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
			    }
				sql.append("     order by vw.DEALER_ID        \n");
				sql.append("          )   \n");
				sql.append("SELECT dealer_id,nvl(root_org_name,' ') root_org_name,nvl(pq_org_name,' ') pq_org_name,nvl(dealer_code,' ') dealer_code ,nvl(dealer_shortname,' ') dealer_shortname, a,\n" );
				sql.append("TOTAL_ACCOUNT,BYKH_ACCOUNT,BYKH_RATE,YWKH_ACCOUNT,YWKH_RATE,ZBKH_ACCOUNT,ZBKH_RATE,SXKH_ACCOUNT,SXKH_RATE \n");
				sql.append(" FROM (           \n");
				sql.append("select  dealer_id,root_org_name,pq_org_name,dealer_code ,dealer_shortname, a, \n");
				sql.append("TOTAL_ACCOUNT, \n");
				sql.append("BYKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(BYKH_ACCOUNT,0,'0',ROUND(BYKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' BYKH_RATE, \n");
				sql.append("YWKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(YWKH_ACCOUNT,0,'0',ROUND(YWKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' YWKH_RATE, \n");
				sql.append("ZBKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(ZBKH_ACCOUNT,0,'0',ROUND(ZBKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' ZBKH_RATE, \n");
				sql.append("SXKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(SXKH_ACCOUNT,0,'0',ROUND(SXKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' SXKH_RATE \n");
				sql.append("from ( \n");
				sql.append("select dealer_id,root_org_name,pq_org_name,dealer_code ,dealer_shortname, a, \n");
				sql.append("sum(BYKH) BYKH_ACCOUNT,sum(YWKH) + sum(CGKH) YWKH_ACCOUNT,sum(ZBKH) ZBKH_ACCOUNT,sum(SXKH) SXKH_ACCOUNT, sum(BYKH)+sum(YWKH)+sum(CGKH)+sum(ZBKH)+sum(SXKH) TOTAL_ACCOUNT \n");
				sql.append("from ( \n");
				sql.append("select TO_CHAR(DEALER_ID) dealer_id,root_org_name,pq_org_name,dealer_code ,dealer_shortname, '3' a,  \n");
				sql.append("decode(CTM_TYPE,60341001,1,0) BYKH,  \n");
				sql.append("decode(CTM_TYPE,60341002,1,0) YWKH,  \n");
				sql.append("decode(CTM_TYPE,60341003,1,0) CGKH,  \n");
				sql.append("decode(CTM_TYPE,60341004,1,0) ZBKH,  \n");
				sql.append("decode(CTM_TYPE,60341005,1,0) SXKH  \n");
				sql.append("from (  \n");
				sql.append(" select tpc.root_org_name,tpc.pq_org_name,tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,ctm_type from tpc where 1=1  \n");
				sql.append(" order by tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,ctm_type  \n");
				sql.append(" )  \n");
				sql.append(" ) group by dealer_id,root_org_name,pq_org_name,dealer_code ,dealer_shortname, a \n");
				sql.append(" ) a \n");
				sql.append(" UNION ALL \n");
				sql.append(" select  '',root_org_name,pq_org_name,'' ,pq_org_name||'小计', '3' a, \n");
				sql.append("TOTAL_ACCOUNT, \n");
				sql.append("BYKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(BYKH_ACCOUNT,0,'0',ROUND(BYKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' BYKH_RATE, \n");
				sql.append("YWKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(YWKH_ACCOUNT,0,'0',ROUND(YWKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' YWKH_RATE, \n");
				sql.append("ZBKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(ZBKH_ACCOUNT,0,'0',ROUND(ZBKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' ZBKH_RATE, \n");
				sql.append("SXKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(SXKH_ACCOUNT,0,'0',ROUND(SXKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' SXKH_RATE \n");
				sql.append("from ( \n");
				sql.append(" select  '',root_org_name,pq_org_name,'' ,pq_org_name||'小计', '3' a, \n");
				sql.append("sum(BYKH) BYKH_ACCOUNT,sum(YWKH) + sum(CGKH) YWKH_ACCOUNT,sum(ZBKH) ZBKH_ACCOUNT,sum(SXKH) SXKH_ACCOUNT, sum(BYKH)+sum(YWKH)+sum(CGKH)+sum(ZBKH)+sum(SXKH) TOTAL_ACCOUNT \n");
				sql.append("from ( \n");
				sql.append("select  '',root_org_name,pq_org_name,'' ,pq_org_name||'小计', '3' a, \n");
				sql.append("decode(CTM_TYPE,60341001,1,0) BYKH,  \n");
				sql.append("decode(CTM_TYPE,60341002,1,0) YWKH,  \n");
				sql.append("decode(CTM_TYPE,60341003,1,0) CGKH,  \n");
				sql.append("decode(CTM_TYPE,60341004,1,0) ZBKH,  \n");
				sql.append("decode(CTM_TYPE,60341005,1,0) SXKH \n");
				sql.append("from (  \n");
				sql.append(" select tpc.root_org_name,tpc.pq_org_name,tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,ctm_type from tpc where 1=1  \n");
				sql.append(" order by tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,ctm_type  \n");
				sql.append(" )  \n");
				sql.append(" ) group by root_org_name,pq_org_name,a \n");
				sql.append(" ) b \n");
				sql.append(" union all \n");
				sql.append("select '',root_org_name,'','' ,ROOT_ORG_NAME||'合计', '2' a,  \n");
				sql.append("TOTAL_ACCOUNT, \n");
				sql.append("BYKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(BYKH_ACCOUNT,0,'0',ROUND(BYKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' BYKH_RATE, \n");
				sql.append("YWKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(YWKH_ACCOUNT,0,'0',ROUND(YWKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' YWKH_RATE, \n");
				sql.append("ZBKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(ZBKH_ACCOUNT,0,'0',ROUND(ZBKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' ZBKH_RATE, \n");
				sql.append("SXKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(SXKH_ACCOUNT,0,'0',ROUND(SXKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' SXKH_RATE \n");
				sql.append("from ( \n");
				sql.append("select '',root_org_name,'','' ,ROOT_ORG_NAME||'合计', '2' a,  \n");
				sql.append("sum(BYKH) BYKH_ACCOUNT,sum(YWKH) + sum(CGKH) YWKH_ACCOUNT,sum(ZBKH) ZBKH_ACCOUNT,sum(SXKH) SXKH_ACCOUNT, sum(BYKH)+sum(YWKH)+sum(CGKH)+sum(ZBKH)+sum(SXKH) TOTAL_ACCOUNT \n");
				sql.append("from ( \n");
				sql.append("select '',root_org_name,'','' ,ROOT_ORG_NAME||'合计', '2' a,  \n");
				sql.append("decode(CTM_TYPE,60341001,1,0) BYKH,  \n");
				sql.append("decode(CTM_TYPE,60341002,1,0) YWKH,  \n");
				sql.append("decode(CTM_TYPE,60341003,1,0) CGKH,  \n");
				sql.append("decode(CTM_TYPE,60341004,1,0) ZBKH,  \n");
				sql.append("decode(CTM_TYPE,60341005,1,0) SXKH \n");
				sql.append("from (  \n");
				sql.append(" select tpc.root_org_name,tpc.pq_org_name,tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,ctm_type from tpc where 1=1  \n");
				sql.append(" order by tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,ctm_type  \n");
				sql.append(" )  \n");
				sql.append(" ) group by root_org_name,a \n");
				sql.append(" ) c \n");
				sql.append("union all \n");
				sql.append(" select '','','','' ,'全国合计', '1' a,  \n");
				sql.append("TOTAL_ACCOUNT, \n");
				sql.append("BYKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(BYKH_ACCOUNT,0,'0',ROUND(BYKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' BYKH_RATE, \n");
				sql.append("YWKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(YWKH_ACCOUNT,0,'0',ROUND(YWKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' YWKH_RATE, \n");
				sql.append("ZBKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(ZBKH_ACCOUNT,0,'0',ROUND(ZBKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' ZBKH_RATE, \n");
				sql.append("SXKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(SXKH_ACCOUNT,0,'0',ROUND(SXKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' SXKH_RATE \n");
				sql.append("from ( \n");
				sql.append(" select '','','','' ,'全国合计', '1' a,   \n");
				sql.append("sum(BYKH) BYKH_ACCOUNT,sum(YWKH) + sum(CGKH) YWKH_ACCOUNT,sum(ZBKH) ZBKH_ACCOUNT,sum(SXKH) SXKH_ACCOUNT, sum(BYKH)+sum(YWKH)+sum(CGKH)+sum(ZBKH)+sum(SXKH) TOTAL_ACCOUNT  \n");
				sql.append("from ( \n");
				sql.append(" select '','','','' ,'全国合计', '1' a,  \n");
				sql.append("decode(CTM_TYPE,60341001,1,0) BYKH,  \n");
				sql.append("decode(CTM_TYPE,60341002,1,0) YWKH,  \n");
				sql.append("decode(CTM_TYPE,60341003,1,0) CGKH,  \n");
				sql.append("decode(CTM_TYPE,60341004,1,0) ZBKH,  \n");
				sql.append("decode(CTM_TYPE,60341005,1,0) SXKH \n");
				sql.append("from (  \n");
				sql.append(" select tpc.root_org_name,tpc.pq_org_name,tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,ctm_type from tpc where 1=1  \n");
				sql.append(" order by tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,ctm_type  \n");
				sql.append(" ) )) \n");
				sql.append(" )  tmp   order by  tmp.a,tmp.ROOT_ORG_NAME, tmp.pq_org_name ,rownum   \n");
		List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	/**
	 * 经销商端
	 */
	
	public List<Map<String, Object>> getDealerCtmManageInfo(Map<String, String> map){
		String startDate=map.get("startDate");
		String endDate=map.get("endDate");
		String dealerId=map.get("dealerId");
		String dealerCode=map.get("dealerCode");
		String userId=map.get("userId");
		String dutyType=map.get("dutyType");
		TcUserPO tc=new TcUserPO();
		tc.setUserId(Long.parseLong(userId));
		tc=(TcUserPO) dao.select(tc).get(0);
		String poseRank=tc.getPoseRank().toString();
		StringBuilder sql= new StringBuilder();
			    sql.append("with tpc as (    \n");
				sql.append("   SELECT  VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME,tpg.group_id,tpg.group_name,tu.name , \n");
				sql.append(" tpc.DEALER_ID,tpc.adviser,tpc.ctm_rank,tpc.create_date,tpc.ctm_type  \n");
				sql.append(" from VW_ORG_DEALER_ALL_NEW vw,t_pc_customer tpc left join tc_user tu on  tu.user_id=tpc.adviser\n");
				sql.append("  left join  t_pc_group tpg   on  tu.group_id=tpg.group_id\n");
				sql.append("     where tpc.dealer_id=vw.dealer_id \n");
				if(!"".equals(dealerCode)&&dealerCode!=null){
		            String arr[]=dealerCode.split(",");
		            String str="";
		            
		            for(int i=0;i<arr.length;i++){
		                str+="'"+arr[i]+"',";
		            }
		            str=str.substring(0,str.lastIndexOf(","));

		            sql.append("     and (vw.dealer_id="+dealerId+" or vw.dealer_code in ("+str+")) \n");
				}else{
					sql.append("     and vw.dealer_id="+dealerId+"  \n");
				}
				
				
				
				
				 if(!CommonUtils.isNullString(poseRank)&&poseRank.equals("60281003")){
						sql.append("and tu.group_id in (select group_id from tc_user where user_id="+userId+") \n");
				}
				if(!CommonUtils.isNullString(poseRank)&&poseRank.equals("60281004")){
					sql.append("and tu.user_id="+userId+" \n");
				}
				if(!CommonUtils.isNullString(startDate)){
		            sql.append("   and tpc.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
				 	}
			    if(!CommonUtils.isNullString(endDate)){
			        sql.append("   and tpc.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
			    }	
				sql.append("     order by vw.DEALER_ID        \n");
				sql.append("          )   \n");
				sql.append("SELECT dealer_id,nvl(root_org_name,' ') root_org_name,nvl(pq_org_name,' ') pq_org_name,nvl(dealer_code,' ') dealer_code ,nvl(dealer_shortname,' ') dealer_shortname, a,nvl(group_name,' ') group_name,nvl(name,' ') name,\n" );
				sql.append("TOTAL_ACCOUNT,BYKH_ACCOUNT,BYKH_RATE,YWKH_ACCOUNT,YWKH_RATE,ZBKH_ACCOUNT,ZBKH_RATE,SXKH_ACCOUNT,SXKH_RATE\n");
				sql.append(" FROM ( \n");
				sql.append("select dealer_id,root_org_name,pq_org_name,dealer_code ,dealer_shortname,a,group_id,group_name,adviser,name, \n");
				sql.append("TOTAL_ACCOUNT, \n");
				sql.append("BYKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(BYKH_ACCOUNT,0,'0',ROUND(BYKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' BYKH_RATE, \n");
				sql.append("YWKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(YWKH_ACCOUNT,0,'0',ROUND(YWKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' YWKH_RATE, \n");
				sql.append("ZBKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(ZBKH_ACCOUNT,0,'0',ROUND(ZBKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' ZBKH_RATE, \n");
				sql.append("SXKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(SXKH_ACCOUNT,0,'0',ROUND(SXKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' SXKH_RATE \n");
				sql.append("from ( \n");
				sql.append("select dealer_id,root_org_name,pq_org_name,dealer_code ,dealer_shortname, a,group_id,group_name,adviser,name, \n");
				sql.append("sum(BYKH) BYKH_ACCOUNT,sum(YWKH) + sum(CGKH) YWKH_ACCOUNT,sum(ZBKH) ZBKH_ACCOUNT,sum(SXKH) SXKH_ACCOUNT, sum(BYKH)+sum(YWKH)+sum(CGKH)+sum(ZBKH)+sum(SXKH) TOTAL_ACCOUNT \n");
				sql.append("from ( \n");
				sql.append("select TO_CHAR(DEALER_ID) dealer_id,root_org_name,pq_org_name,dealer_code ,dealer_shortname, '3' a,group_id,group_name,adviser,name, \n");
				sql.append("decode(CTM_TYPE,60341001,1,0) BYKH,  \n");
				sql.append("decode(CTM_TYPE,60341002,1,0) YWKH,  \n");
				sql.append("decode(CTM_TYPE,60341003,1,0) CGKH,  \n");
				sql.append("decode(CTM_TYPE,60341004,1,0) ZBKH,  \n");
				sql.append("decode(CTM_TYPE,60341005,1,0) SXKH \n");
				sql.append("from (  \n");
				sql.append(" select tpc.root_org_name,tpc.pq_org_name,tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,ctm_type,group_id,group_name,adviser,name from tpc where 1=1  \n");
				sql.append(" order by tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,ctm_type  \n");
				sql.append(" )  \n");
				sql.append(" ) group by dealer_id,root_org_name,pq_org_name,dealer_code ,dealer_shortname,a,group_id,group_name,adviser,name \n");
				sql.append(" ) a \n");
				sql.append(" \n");
				sql.append(" UNION ALL \n");
				sql.append("   \n");
				sql.append("select dealer_id,root_org_name,pq_org_name,dealer_code,dealer_shortname,a,group_id,group_name,0,group_name||'小计', \n");
				sql.append("TOTAL_ACCOUNT, \n");
				sql.append("BYKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(BYKH_ACCOUNT,0,'0',ROUND(BYKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' BYKH_RATE, \n");
				sql.append("YWKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(YWKH_ACCOUNT,0,'0',ROUND(YWKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' YWKH_RATE, \n");
				sql.append("ZBKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(ZBKH_ACCOUNT,0,'0',ROUND(ZBKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' ZBKH_RATE, \n");
				sql.append("SXKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(SXKH_ACCOUNT,0,'0',ROUND(SXKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' SXKH_RATE \n");
				sql.append("from ( \n");
				sql.append("select TO_CHAR(DEALER_ID) dealer_id,root_org_name,pq_org_name,dealer_code ,dealer_shortname,'2' a,group_id,group_name,'',group_name||'小计', \n");
				sql.append("sum(BYKH) BYKH_ACCOUNT,sum(YWKH) + sum(CGKH) YWKH_ACCOUNT,sum(ZBKH) ZBKH_ACCOUNT,sum(SXKH) SXKH_ACCOUNT, sum(BYKH)+sum(YWKH)+sum(CGKH)+sum(ZBKH)+sum(SXKH) TOTAL_ACCOUNT \n");
				sql.append("from ( \n");
				sql.append("select dealer_id,root_org_name,pq_org_name,dealer_code ,dealer_shortname,'2' a,group_id,group_name,'',group_name||'小计',  \n");
				sql.append("decode(CTM_TYPE,60341001,1,0) BYKH,  \n");
				sql.append("decode(CTM_TYPE,60341002,1,0) YWKH,  \n");
				sql.append("decode(CTM_TYPE,60341003,1,0) CGKH,  \n");
				sql.append("decode(CTM_TYPE,60341004,1,0) ZBKH,  \n");
				sql.append("decode(CTM_TYPE,60341005,1,0) SXKH \n");
				sql.append("from (  \n");
				sql.append(" select tpc.root_org_name,tpc.pq_org_name,tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,ctm_type,group_id,group_name from tpc where 1=1  \n");
				sql.append(" order by tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,ctm_type  \n");
				sql.append(" )  \n");
				sql.append(" ) group by dealer_id,root_org_name,pq_org_name,dealer_code,dealer_shortname,a,group_id,group_name \n");
				sql.append(" ) b \n");
				sql.append("union all \n");
				sql.append("select dealer_id,root_org_name,pq_org_name,dealer_code,dealer_shortname,'1' a,0,'',0,'合计', \n");
				sql.append("TOTAL_ACCOUNT, \n");
				sql.append("BYKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(BYKH_ACCOUNT,0,'0',ROUND(BYKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' BYKH_RATE, \n");
				sql.append("YWKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(YWKH_ACCOUNT,0,'0',ROUND(YWKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' YWKH_RATE, \n");
				sql.append("ZBKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(ZBKH_ACCOUNT,0,'0',ROUND(ZBKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' ZBKH_RATE, \n");
				sql.append("SXKH_ACCOUNT, \n");
				sql.append("DECODE(TOTAL_ACCOUNT,0,'0',DECODE(SXKH_ACCOUNT,0,'0',ROUND(SXKH_ACCOUNT*100/TOTAL_ACCOUNT,2)))|| '%' SXKH_RATE \n");
				sql.append("from ( \n");
				sql.append("select TO_CHAR(DEALER_ID) dealer_id,root_org_name,pq_org_name,dealer_code,dealer_shortname,'1' a,0,'',0,'合计',  \n");
				sql.append("sum(BYKH) BYKH_ACCOUNT,sum(YWKH) + sum(CGKH) YWKH_ACCOUNT,sum(ZBKH) ZBKH_ACCOUNT,sum(SXKH) SXKH_ACCOUNT, sum(BYKH)+sum(YWKH)+sum(CGKH)+sum(ZBKH)+sum(SXKH) TOTAL_ACCOUNT \n");
				sql.append("from ( \n");
				sql.append("select dealer_id,root_org_name,pq_org_name,dealer_code,dealer_shortname,'1' a,0,'',0,'合计', \n");
				sql.append("decode(CTM_TYPE,60341001,1,0) BYKH,  \n");
				sql.append("decode(CTM_TYPE,60341002,1,0) YWKH,  \n");
				sql.append("decode(CTM_TYPE,60341003,1,0) CGKH,  \n");
				sql.append("decode(CTM_TYPE,60341004,1,0) ZBKH,  \n");
				sql.append("decode(CTM_TYPE,60341005,1,0) SXKH \n");
				sql.append("from (  \n");
				sql.append(" select tpc.root_org_name,tpc.pq_org_name,tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,ctm_type from tpc where 1=1  \n");
				sql.append(" order by tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,ctm_type  \n");
				sql.append(" ) \n");
				sql.append("  ) \n");
				sql.append("  group by dealer_id,root_org_name,pq_org_name,dealer_code,dealer_shortname,a \n");
				sql.append("  ) \n");
				sql.append(" )  tmp   order by  tmp.a,tmp.dealer_id,tmp.group_id, tmp.adviser ,rownum   \n");
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
	  //合作经销商
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
	
}
