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

public class PotentialCustomerAnalysisDao extends BaseDao<PO>{
private static final PotentialCustomerAnalysisDao dao = new PotentialCustomerAnalysisDao();
private ActionContext act = ActionContext.getContext();
RequestWrapper request = act.getRequest();
	public static final PotentialCustomerAnalysisDao getInstance() {
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
		sql.append("     select * from (with tpc as (    \n");
	     sql.append("   SELECT  VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME,  \n");
	     sql.append(" tpc.DEALER_ID,tpc.adviser,tu.name,tpg.group_id,tpg.group_name,tpc.ctm_rank, ROUND(SYSDATE - tpc.create_date,2) KL,tpc.create_date,CASE WHEN  tpc.create_date<trunc(sysdate, 'mm') THEN 0 else 1 end status  \n"); 
	     sql.append(" from t_pc_customer tpc,VW_ORG_DEALER_ALL_NEW vw ,tc_user tu,t_pc_group tpg  where   TPC.CTM_TYPE IN ('60341002','60341003')  and tpc.dealer_id=vw.dealer_id  \n");
	    
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("   AND tpc.intent_vehicle in (select tpiv.series_id from t_pc_intent_vehicle tpiv  where tpiv.up_series_id in ("+seriesId+")  )   \n" );
	     }
		 if(!CommonUtils.isNullString(startDate)){
	      sql.append("      		    AND  tpc.CREATE_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	     }
	     if(!CommonUtils.isNullString(endDate)){
	      sql.append("      		  AND  tpc.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	     }
	     
	     sql.append(" and tpc.ADVISER =tu.user_id  and  tu.group_id=tpg.group_id     \n");
		 
	     
		 //判断是否顾问登陆
		   if(!CommonUtils.isNullString(userId)) {
				sql.append(" AND tpc.ADVISER  IN ("+userId+") \n");
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
		 
		 
		 
		 sql.append(" select TO_CHAR(DEALER_ID) dealer_id,TO_CHAR(adviser) adviser,root_org_name,pq_org_name,dealer_code ,dealer_shortname, '2' a,name,group_name,  \n");
		 sql.append(" HXZ,HPJKL,HLC,HLCKL, \n");
		 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.HXZ,0,'0',ROUND(JD.HXZ*100/JD.HABCXZ,2))) || '%' HXZRATE, \n");
		 sql.append(" AXZ,APJKL,ALC,ALCKL, \n");
		 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.AXZ,0,'0',ROUND(JD.AXZ*100/JD.HABCXZ,2))) || '%' AXZRATE, \n");
		 sql.append(" BXZ,BPJKL,BLC,BLCKL, \n");
		 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.BXZ,0,'0',ROUND(JD.BXZ*100/JD.HABCXZ,2))) || '%' BXZRATE, \n");
		 sql.append(" CXZ,CPJKL,CLC,CLCKL, \n");
		 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.CXZ,0,'0',ROUND(JD.CXZ*100/JD.HABCXZ,2))) || '%' CXZRATE, \n");
		 
		 sql.append(" OXZ,OPJKL,OLC,OLCKL, \n");
		 sql.append(" EXZ,EPJKL,ELC,ELCKL, \n"); 
		 sql.append(" LXZ,LPJKL,LLC,LLCKL, \n"); 
		 
		 sql.append(" HABXZ,HABLC,HABXZLC,HABCXZ,HABCLC,HABCXZLC \n");
		 sql.append(" from ( \n");
		 sql.append(" select root_org_name,pq_org_name,dealer_id,adviser,dealer_code ,dealer_shortname, '2' a,name,group_name, \n");
		 sql.append(" sum(HXZ) HXZ, DECODE(sum(HXZKL),0,'0',DECODE(sum(HXZ),0,'0',ROUND(sum(HXZKL)/sum(HXZ),2))) HPJKL, \n");
		 sql.append(" sum(HLC) HLC, DECODE(sum(HLCKL),0,'0',DECODE(sum(HLC),0,'0',ROUND(sum(HLCKL)/sum(HLC),2))) HLCKL, \n");
		  
		 sql.append(" sum(AXZ) AXZ,DECODE(sum(AXZKL),0,'0',DECODE(sum(AXZ),0,'0',ROUND(sum(AXZKL)/sum(AXZ),2)))  APJKL, \n");
		 sql.append(" sum(ALC) ALC, DECODE(sum(ALCKL),0,'0',DECODE(sum(ALC),0,'0',ROUND(sum(ALCKL)/sum(ALC),2))) ALCKL, \n");


		 sql.append(" sum(BXZ) BXZ,DECODE(sum(BXZKL),0,'0',DECODE(sum(BXZ),0,'0',ROUND(sum(BXZKL)/sum(BXZ),2))) BPJKL, \n");
		 sql.append(" sum(BLC) BLC, DECODE(sum(BLCKL),0,'0',DECODE(sum(BLC),0,'0',ROUND(sum(BLCKL)/sum(BLC),2))) BLCKL, \n");

		 sql.append(" sum(CXZ) CXZ,DECODE(sum(CXZKL),0,'0',DECODE(sum(CXZ),0,'0',ROUND(sum(CXZKL)/sum(CXZ),2))) CPJKL, \n");
		 sql.append(" sum(CLC) CLC, DECODE(sum(CLCKL),0,'0',DECODE(sum(CLC),0,'0',ROUND(sum(CLCKL)/sum(CLC),2))) CLCKL, \n");
		 
		 sql.append(" sum(OXZ) OXZ,DECODE(sum(OXZKL),0,'0',DECODE(sum(OXZ),0,'0',ROUND(sum(OXZKL)/sum(OXZ),2))) OPJKL, \n");
		 sql.append(" sum(OLC) OLC, DECODE(sum(OLCKL),0,'0',DECODE(sum(OLC),0,'0',ROUND(sum(OLCKL)/sum(OLC),2))) OLCKL, \n");
		 
		 sql.append(" sum(EXZ) EXZ,DECODE(sum(EXZKL),0,'0',DECODE(sum(EXZ),0,'0',ROUND(sum(EXZKL)/sum(EXZ),2))) EPJKL, \n");
		 sql.append(" sum(ELC) ELC, DECODE(sum(ELCKL),0,'0',DECODE(sum(ELC),0,'0',ROUND(sum(ELCKL)/sum(ELC),2))) ELCKL, \n");
		 
		 sql.append(" sum(LXZ) LXZ,DECODE(sum(LXZKL),0,'0',DECODE(sum(LXZ),0,'0',ROUND(sum(LXZKL)/sum(LXZ),2))) LPJKL, \n");
		 sql.append(" sum(LLC) LLC, DECODE(sum(LLCKL),0,'0',DECODE(sum(LLC),0,'0',ROUND(sum(LLCKL)/sum(LLC),2))) LLCKL, \n");
		 

		 sql.append(" sum(HXZ+AXZ+BXZ) HABXZ,sum(HLC+ALC+BLC) HABLC, sum(HXZ+AXZ+BXZ+HLC+ALC+BLC) HABXZLC,  \n");
		 sql.append(" sum(HXZ+AXZ+BXZ+CXZ) HABCXZ,sum(HLC+ALC+BLC+CLC) HABCLC, sum(HXZ+AXZ+BXZ+CXZ+HLC+ALC+BLC+CLC) HABCXZLC  \n"); 
		 sql.append(" from ( \n");
		 sql.append(" select root_org_name,pq_org_name,dealer_id,adviser,dealer_code ,dealer_shortname, '2' a,name,group_name, \n");
		 sql.append(" decode(ctm_rank,60101001,(decode(status,1,sl,0)),0) HXZ, \n");
		 sql.append(" decode(ctm_rank,60101001,(decode(status,1,kl,0)),0) HXZKL, \n");
		 sql.append(" decode(ctm_rank,60101001,(decode(status,0,sl,0)),0) HLC, \n");
		 sql.append(" decode(ctm_rank,60101001,(decode(status,0,kl,0)),0) HLCKL, \n");

		 sql.append(" decode(ctm_rank,60101002,(decode(status,1,sl,0)),0) AXZ, \n");
		 sql.append(" decode(ctm_rank,60101002,(decode(status,1,kl,0)),0) AXZKL, \n");
		 sql.append(" decode(ctm_rank,60101002,(decode(status,0,sl,0)),0) ALC, \n");
		 sql.append(" decode(ctm_rank,60101002,(decode(status,0,kl,0)),0) ALCKL, \n");

		 sql.append(" decode(ctm_rank,60101003,(decode(status,1,sl,0)),0) BXZ, \n");
		 sql.append(" decode(ctm_rank,60101003,(decode(status,1,kl,0)),0) BXZKL, \n");
		 sql.append(" decode(ctm_rank,60101003,(decode(status,0,sl,0)),0) BLC, \n");
		 sql.append(" decode(ctm_rank,60101003,(decode(status,0,kl,0)),0) BLCKL, \n");

		 sql.append(" decode(ctm_rank,60101004,(decode(status,1,sl,0)),0) CXZ, \n");
		 sql.append(" decode(ctm_rank,60101004,(decode(status,1,kl,0)),0) CXZKL, \n");
		 sql.append(" decode(ctm_rank,60101004,(decode(status,0,sl,0)),0) CLC, \n");
		 sql.append(" decode(ctm_rank,60101004,(decode(status,0,kl,0)),0) CLCKL, \n");
		 
		 sql.append(" decode(ctm_rank,60101005,(decode(status,1,sl,0)),0) OXZ,\n");
		 sql.append(" decode(ctm_rank,60101005,(decode(status,1,kl,0)),0) OXZKL, \n");
		 sql.append(" decode(ctm_rank,60101005,(decode(status,0,sl,0)),0) OLC, \n");
		 sql.append(" decode(ctm_rank,60101005,(decode(status,0,kl,0)),0) OLCKL, \n");
		 
		 sql.append(" decode(ctm_rank,60101006,(decode(status,1,sl,0)),0) EXZ,\n");
		 sql.append(" decode(ctm_rank,60101006,(decode(status,1,kl,0)),0) EXZKL,\n"); 
		 sql.append(" decode(ctm_rank,60101006,(decode(status,0,sl,0)),0) ELC, \n");
		 sql.append(" decode(ctm_rank,60101006,(decode(status,0,kl,0)),0) ELCKL,\n");
		 
		 sql.append(" decode(ctm_rank,60101007,(decode(status,1,sl,0)),0) LXZ,\n");
		 sql.append(" decode(ctm_rank,60101007,(decode(status,1,kl,0)),0) LXZKL, \n");
		 sql.append(" decode(ctm_rank,60101007,(decode(status,0,sl,0)),0) LLC, \n");
		 sql.append(" decode(ctm_rank,60101007,(decode(status,0,kl,0)),0) LLCKL\n");

		 sql.append(" from ( \n");
		 sql.append(" select tpc.root_org_name,tpc.pq_org_name,tpc.adviser,tpc.name,tpc.group_name,tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,status,sum(1) sl,sum(KL) KL from tpc where 1=1 group by tpc.root_org_name,tpc.pq_org_name,tpc.adviser,tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,status,name,group_name  \n");
		 sql.append(" order by tpc.adviser,tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,status,name,group_name \n");
		 sql.append(" ) \n");
		 sql.append(" ) jd where 1=1 group by jd.root_org_name,jd.pq_org_name,jd.adviser,jd.dealer_id,jd.dealer_code,jd.dealer_shortname,name,group_name \n");
		 sql.append(" )JD \n");
		 
		 sql.append(" union all  \n");
		 
		 sql.append(" select  '','',root_org_name,pq_org_name,dealer_code,dealer_shortname,'1',group_name||'小计', group_name,  \n");
		 sql.append(" HXZ,HPJKL,HLC,HLCKL, \n");
		 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.HXZ,0,'0',ROUND(JD.HXZ*100/JD.HABCXZ,2))) || '%' HXZRATE, \n");
		 sql.append(" AXZ,APJKL,ALC,ALCKL, \n");
		 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.AXZ,0,'0',ROUND(JD.AXZ*100/JD.HABCXZ,2))) || '%' AXZRATE, \n");
		 sql.append(" BXZ,BPJKL,BLC,BLCKL, \n");
		 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.BXZ,0,'0',ROUND(JD.BXZ*100/JD.HABCXZ,2))) || '%' BXZRATE, \n");
		 sql.append(" CXZ,CPJKL,CLC,CLCKL, \n");
		 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.CXZ,0,'0',ROUND(JD.CXZ*100/JD.HABCXZ,2))) || '%' CXZRATE, \n");
		 
		 sql.append(" OXZ,OPJKL,OLC,OLCKL, \n");
		 sql.append(" EXZ,EPJKL,ELC,ELCKL, \n"); 
		 sql.append(" LXZ,LPJKL,LLC,LLCKL, \n"); 
		 
		 sql.append(" HABXZ,HABLC,HABXZLC,HABCXZ,HABCLC,HABCXZLC \n");
		 sql.append(" from ( \n");
		 sql.append(" select '','',root_org_name,pq_org_name,dealer_code ,dealer_shortname, '1' a, group_name, \n");
		 sql.append(" sum(HXZ) HXZ, DECODE(sum(HXZKL),0,'0',DECODE(sum(HXZ),0,'0',ROUND(sum(HXZKL)/sum(HXZ),2))) HPJKL, \n");
		 sql.append(" sum(HLC) HLC, DECODE(sum(HLCKL),0,'0',DECODE(sum(HLC),0,'0',ROUND(sum(HLCKL)/sum(HLC),2))) HLCKL, \n");
		  
		 sql.append(" sum(AXZ) AXZ,DECODE(sum(AXZKL),0,'0',DECODE(sum(AXZ),0,'0',ROUND(sum(AXZKL)/sum(AXZ),2)))  APJKL, \n");
		 sql.append(" sum(ALC) ALC, DECODE(sum(ALCKL),0,'0',DECODE(sum(ALC),0,'0',ROUND(sum(ALCKL)/sum(ALC),2))) ALCKL, \n");


		 sql.append(" sum(BXZ) BXZ,DECODE(sum(BXZKL),0,'0',DECODE(sum(BXZ),0,'0',ROUND(sum(BXZKL)/sum(BXZ),2))) BPJKL, \n");
		 sql.append(" sum(BLC) BLC, DECODE(sum(BLCKL),0,'0',DECODE(sum(BLC),0,'0',ROUND(sum(BLCKL)/sum(BLC),2))) BLCKL, \n");

		 sql.append(" sum(CXZ) CXZ,DECODE(sum(CXZKL),0,'0',DECODE(sum(CXZ),0,'0',ROUND(sum(CXZKL)/sum(CXZ),2))) CPJKL, \n");
		 sql.append(" sum(CLC) CLC, DECODE(sum(CLCKL),0,'0',DECODE(sum(CLC),0,'0',ROUND(sum(CLCKL)/sum(CLC),2))) CLCKL, \n");
		 
		 sql.append(" sum(OXZ) OXZ,DECODE(sum(OXZKL),0,'0',DECODE(sum(OXZ),0,'0',ROUND(sum(OXZKL)/sum(OXZ),2))) OPJKL, \n");
		 sql.append(" sum(OLC) OLC, DECODE(sum(OLCKL),0,'0',DECODE(sum(OLC),0,'0',ROUND(sum(OLCKL)/sum(OLC),2))) OLCKL, \n");
		 
		 sql.append(" sum(EXZ) EXZ,DECODE(sum(EXZKL),0,'0',DECODE(sum(EXZ),0,'0',ROUND(sum(EXZKL)/sum(EXZ),2))) EPJKL, \n");
		 sql.append(" sum(ELC) ELC, DECODE(sum(ELCKL),0,'0',DECODE(sum(ELC),0,'0',ROUND(sum(ELCKL)/sum(ELC),2))) ELCKL, \n");
		 
		 sql.append(" sum(LXZ) LXZ,DECODE(sum(LXZKL),0,'0',DECODE(sum(LXZ),0,'0',ROUND(sum(LXZKL)/sum(LXZ),2))) LPJKL, \n");
		 sql.append(" sum(LLC) LLC, DECODE(sum(LLCKL),0,'0',DECODE(sum(LLC),0,'0',ROUND(sum(LLCKL)/sum(LLC),2))) LLCKL, \n");
		 

		 sql.append(" sum(HXZ+AXZ+BXZ) HABXZ,sum(HLC+ALC+BLC) HABLC, sum(HXZ+AXZ+BXZ+HLC+ALC+BLC) HABXZLC,  \n");
		 sql.append(" sum(HXZ+AXZ+BXZ+CXZ) HABCXZ,sum(HLC+ALC+BLC+CLC) HABCLC, sum(HXZ+AXZ+BXZ+CXZ+HLC+ALC+BLC+CLC) HABCXZLC  \n"); 
		 sql.append(" from ( \n");
		 sql.append(" select '','',root_org_name,pq_org_name,dealer_code ,dealer_shortname, '1' a, group_name,  \n");
		 sql.append(" decode(ctm_rank,60101001,(decode(status,1,sl,0)),0) HXZ, \n");
		 sql.append(" decode(ctm_rank,60101001,(decode(status,1,kl,0)),0) HXZKL, \n");
		 sql.append(" decode(ctm_rank,60101001,(decode(status,0,sl,0)),0) HLC, \n");
		 sql.append(" decode(ctm_rank,60101001,(decode(status,0,kl,0)),0) HLCKL, \n");

		 sql.append(" decode(ctm_rank,60101002,(decode(status,1,sl,0)),0) AXZ, \n");
		 sql.append(" decode(ctm_rank,60101002,(decode(status,1,kl,0)),0) AXZKL, \n");
		 sql.append(" decode(ctm_rank,60101002,(decode(status,0,sl,0)),0) ALC, \n");
		 sql.append(" decode(ctm_rank,60101002,(decode(status,0,kl,0)),0) ALCKL, \n");

		 sql.append(" decode(ctm_rank,60101003,(decode(status,1,sl,0)),0) BXZ, \n");
		 sql.append(" decode(ctm_rank,60101003,(decode(status,1,kl,0)),0) BXZKL, \n");
		 sql.append(" decode(ctm_rank,60101003,(decode(status,0,sl,0)),0) BLC, \n");
		 sql.append(" decode(ctm_rank,60101003,(decode(status,0,kl,0)),0) BLCKL, \n");

		 sql.append(" decode(ctm_rank,60101004,(decode(status,1,sl,0)),0) CXZ, \n");
		 sql.append(" decode(ctm_rank,60101004,(decode(status,1,kl,0)),0) CXZKL, \n");
		 sql.append(" decode(ctm_rank,60101004,(decode(status,0,sl,0)),0) CLC, \n");
		 sql.append(" decode(ctm_rank,60101004,(decode(status,0,kl,0)),0) CLCKL, \n");
		 
		 sql.append(" decode(ctm_rank,60101005,(decode(status,1,sl,0)),0) OXZ,\n");
		 sql.append(" decode(ctm_rank,60101005,(decode(status,1,kl,0)),0) OXZKL, \n");
		 sql.append(" decode(ctm_rank,60101005,(decode(status,0,sl,0)),0) OLC, \n");
		 sql.append(" decode(ctm_rank,60101005,(decode(status,0,kl,0)),0) OLCKL, \n");
		 
		 sql.append(" decode(ctm_rank,60101006,(decode(status,1,sl,0)),0) EXZ,\n");
		 sql.append(" decode(ctm_rank,60101006,(decode(status,1,kl,0)),0) EXZKL,\n"); 
		 sql.append(" decode(ctm_rank,60101006,(decode(status,0,sl,0)),0) ELC, \n");
		 sql.append(" decode(ctm_rank,60101006,(decode(status,0,kl,0)),0) ELCKL,\n");
		 
		 sql.append(" decode(ctm_rank,60101007,(decode(status,1,sl,0)),0) LXZ,\n");
		 sql.append(" decode(ctm_rank,60101007,(decode(status,1,kl,0)),0) LXZKL, \n");
		 sql.append(" decode(ctm_rank,60101007,(decode(status,0,sl,0)),0) LLC, \n");
		 sql.append(" decode(ctm_rank,60101007,(decode(status,0,kl,0)),0) LLCKL\n");

		 sql.append(" from ( \n");
		 sql.append(" select tpc.root_org_name,tpc.pq_org_name,tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.group_name,tpc.ctm_rank,status,sum(1) sl,sum(KL) KL from tpc where 1=1 group by tpc.root_org_name,tpc.pq_org_name,tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.group_name,tpc.ctm_rank,status   \n");
		 sql.append(" order by tpc.root_org_name,tpc.pq_org_name,tpc.dealer_code,tpc.dealer_shortname,tpc.group_name,tpc.ctm_rank,status \n");
		 sql.append(" ) \n");
		 sql.append(" ) jd where 1=1 group by root_org_name,pq_org_name,dealer_code,dealer_shortname,group_name \n");
		 sql.append(" )JD \n");
		 
		 sql.append(" union all  \n");
		 
		 
		 sql.append(" select  '','',root_org_name,pq_org_name,dealer_code,dealer_shortname,'3',DEALER_SHORTNAME||'总计', '',   \n");
		 sql.append(" HXZ,HPJKL,HLC,HLCKL, \n");
		 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.HXZ,0,'0',ROUND(JD.HXZ*100/JD.HABCXZ,2))) || '%' HXZRATE, \n");
		 sql.append(" AXZ,APJKL,ALC,ALCKL, \n");
		 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.AXZ,0,'0',ROUND(JD.AXZ*100/JD.HABCXZ,2))) || '%' AXZRATE, \n");
		 sql.append(" BXZ,BPJKL,BLC,BLCKL, \n");
		 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.BXZ,0,'0',ROUND(JD.BXZ*100/JD.HABCXZ,2))) || '%' BXZRATE, \n");
		 sql.append(" CXZ,CPJKL,CLC,CLCKL, \n");
		 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.CXZ,0,'0',ROUND(JD.CXZ*100/JD.HABCXZ,2))) || '%' CXZRATE, \n");
		 
		 sql.append(" OXZ,OPJKL,OLC,OLCKL, \n");
		 sql.append(" EXZ,EPJKL,ELC,ELCKL, \n"); 
		 sql.append(" LXZ,LPJKL,LLC,LLCKL, \n"); 
		 
		 sql.append(" HABXZ,HABLC,HABXZLC,HABCXZ,HABCLC,HABCXZLC \n");
		 sql.append(" from ( \n");
		 sql.append(" select '','',root_org_name,pq_org_name,DEALER_CODE,DEALER_SHORTNAME, '3' , \n");
		 sql.append(" sum(HXZ) HXZ, DECODE(sum(HXZKL),0,'0',DECODE(sum(HXZ),0,'0',ROUND(sum(HXZKL)/sum(HXZ),2))) HPJKL, \n");
		 sql.append(" sum(HLC) HLC, DECODE(sum(HLCKL),0,'0',DECODE(sum(HLC),0,'0',ROUND(sum(HLCKL)/sum(HLC),2))) HLCKL, \n");
		  
		 sql.append(" sum(AXZ) AXZ,DECODE(sum(AXZKL),0,'0',DECODE(sum(AXZ),0,'0',ROUND(sum(AXZKL)/sum(AXZ),2)))  APJKL, \n");
		 sql.append(" sum(ALC) ALC, DECODE(sum(ALCKL),0,'0',DECODE(sum(ALC),0,'0',ROUND(sum(ALCKL)/sum(ALC),2))) ALCKL, \n");


		 sql.append(" sum(BXZ) BXZ,DECODE(sum(BXZKL),0,'0',DECODE(sum(BXZ),0,'0',ROUND(sum(BXZKL)/sum(BXZ),2))) BPJKL, \n");
		 sql.append(" sum(BLC) BLC, DECODE(sum(BLCKL),0,'0',DECODE(sum(BLC),0,'0',ROUND(sum(BLCKL)/sum(BLC),2))) BLCKL, \n");

		 sql.append(" sum(CXZ) CXZ,DECODE(sum(CXZKL),0,'0',DECODE(sum(CXZ),0,'0',ROUND(sum(CXZKL)/sum(CXZ),2))) CPJKL, \n");
		 sql.append(" sum(CLC) CLC, DECODE(sum(CLCKL),0,'0',DECODE(sum(CLC),0,'0',ROUND(sum(CLCKL)/sum(CLC),2))) CLCKL, \n");
		 
		 sql.append(" sum(OXZ) OXZ,DECODE(sum(OXZKL),0,'0',DECODE(sum(OXZ),0,'0',ROUND(sum(OXZKL)/sum(OXZ),2))) OPJKL, \n");
		 sql.append(" sum(OLC) OLC, DECODE(sum(OLCKL),0,'0',DECODE(sum(OLC),0,'0',ROUND(sum(OLCKL)/sum(OLC),2))) OLCKL, \n");
		 
		 sql.append(" sum(EXZ) EXZ,DECODE(sum(EXZKL),0,'0',DECODE(sum(EXZ),0,'0',ROUND(sum(EXZKL)/sum(EXZ),2))) EPJKL, \n");
		 sql.append(" sum(ELC) ELC, DECODE(sum(ELCKL),0,'0',DECODE(sum(ELC),0,'0',ROUND(sum(ELCKL)/sum(ELC),2))) ELCKL, \n");
		 
		 sql.append(" sum(LXZ) LXZ,DECODE(sum(LXZKL),0,'0',DECODE(sum(LXZ),0,'0',ROUND(sum(LXZKL)/sum(LXZ),2))) LPJKL, \n");
		 sql.append(" sum(LLC) LLC, DECODE(sum(LLCKL),0,'0',DECODE(sum(LLC),0,'0',ROUND(sum(LLCKL)/sum(LLC),2))) LLCKL, \n");
		 

		 sql.append(" sum(HXZ+AXZ+BXZ) HABXZ,sum(HLC+ALC+BLC) HABLC, sum(HXZ+AXZ+BXZ+HLC+ALC+BLC) HABXZLC,  \n");
		 sql.append(" sum(HXZ+AXZ+BXZ+CXZ) HABCXZ,sum(HLC+ALC+BLC+CLC) HABCLC, sum(HXZ+AXZ+BXZ+CXZ+HLC+ALC+BLC+CLC) HABCXZLC  \n"); 
		 sql.append(" from ( \n");
		 sql.append(" select '','',root_org_name,pq_org_name,DEALER_CODE,DEALER_SHORTNAME, '3' ,  \n");
		 sql.append(" decode(ctm_rank,60101001,(decode(status,1,sl,0)),0) HXZ, \n");
		 sql.append(" decode(ctm_rank,60101001,(decode(status,1,kl,0)),0) HXZKL, \n");
		 sql.append(" decode(ctm_rank,60101001,(decode(status,0,sl,0)),0) HLC, \n");
		 sql.append(" decode(ctm_rank,60101001,(decode(status,0,kl,0)),0) HLCKL, \n");

		 sql.append(" decode(ctm_rank,60101002,(decode(status,1,sl,0)),0) AXZ, \n");
		 sql.append(" decode(ctm_rank,60101002,(decode(status,1,kl,0)),0) AXZKL, \n");
		 sql.append(" decode(ctm_rank,60101002,(decode(status,0,sl,0)),0) ALC, \n");
		 sql.append(" decode(ctm_rank,60101002,(decode(status,0,kl,0)),0) ALCKL, \n");

		 sql.append(" decode(ctm_rank,60101003,(decode(status,1,sl,0)),0) BXZ, \n");
		 sql.append(" decode(ctm_rank,60101003,(decode(status,1,kl,0)),0) BXZKL, \n");
		 sql.append(" decode(ctm_rank,60101003,(decode(status,0,sl,0)),0) BLC, \n");
		 sql.append(" decode(ctm_rank,60101003,(decode(status,0,kl,0)),0) BLCKL, \n");

		 sql.append(" decode(ctm_rank,60101004,(decode(status,1,sl,0)),0) CXZ, \n");
		 sql.append(" decode(ctm_rank,60101004,(decode(status,1,kl,0)),0) CXZKL, \n");
		 sql.append(" decode(ctm_rank,60101004,(decode(status,0,sl,0)),0) CLC, \n");
		 sql.append(" decode(ctm_rank,60101004,(decode(status,0,kl,0)),0) CLCKL, \n");
		 
		 sql.append(" decode(ctm_rank,60101005,(decode(status,1,sl,0)),0) OXZ,\n");
		 sql.append(" decode(ctm_rank,60101005,(decode(status,1,kl,0)),0) OXZKL, \n");
		 sql.append(" decode(ctm_rank,60101005,(decode(status,0,sl,0)),0) OLC, \n");
		 sql.append(" decode(ctm_rank,60101005,(decode(status,0,kl,0)),0) OLCKL, \n");
		 
		 sql.append(" decode(ctm_rank,60101006,(decode(status,1,sl,0)),0) EXZ,\n");
		 sql.append(" decode(ctm_rank,60101006,(decode(status,1,kl,0)),0) EXZKL,\n"); 
		 sql.append(" decode(ctm_rank,60101006,(decode(status,0,sl,0)),0) ELC, \n");
		 sql.append(" decode(ctm_rank,60101006,(decode(status,0,kl,0)),0) ELCKL,\n");
		 
		 sql.append(" decode(ctm_rank,60101007,(decode(status,1,sl,0)),0) LXZ,\n");
		 sql.append(" decode(ctm_rank,60101007,(decode(status,1,kl,0)),0) LXZKL, \n");
		 sql.append(" decode(ctm_rank,60101007,(decode(status,0,sl,0)),0) LLC, \n");
		 sql.append(" decode(ctm_rank,60101007,(decode(status,0,kl,0)),0) LLCKL\n");

		 sql.append(" from ( \n");
		 sql.append(" select tpc.root_org_name,tpc.pq_org_name,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,status,sum(1) sl,sum(KL) KL from tpc where 1=1 group by tpc.root_org_name,tpc.pq_org_name,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,status   \n");
		 sql.append(" order by tpc.root_org_name,tpc.pq_org_name,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,status \n");
		 sql.append(" ) \n");
		 sql.append(" ) jd where 1=1 group by root_org_name,pq_org_name,dealer_code,dealer_shortname \n");
		 sql.append(" )JD \n");
		 
		 sql.append("  ) tmp  order by tmp.ROOT_ORG_NAME,tmp.PQ_ORG_NAME,tmp.DEALER_CODE, tmp.a ,tmp.group_name\n");
		 
		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getStatisticsDaoSelectAllbak(Map<String, String> map) throws UnsupportedEncodingException{
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
	     sql.append(" JD.DEALER_ID, to_number(JD.HXZ) HXZ,to_number(JD.HPJKL) HPJKL,to_number(JD.HLC) HLC, to_number(JD.HLCKL) HLCKL,  \n");
	     sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.HXZ,0,'0',ROUND(JD.HXZ*100/JD.HABCXZ,2))) || '%' HXZRATE, \n");
	     //sql.append(" DECODE(JD.SCCOUNT,0,'0',DECODE(JD.JCCOUNT,0,'0',ROUND(JD.JCCOUNT*100/JD.SCCOUNT,2))) || '%'  JCRATE, \n");
	     sql.append(" to_number(JD.AXZ) AXZ,to_number(JD.APJKL) APJKL,to_number(JD.ALC) ALC, to_number(JD.ALCKL) ALCKL,  \n");
	     sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.AXZ,0,'0',ROUND(JD.AXZ*100/JD.HABCXZ,2))) || '%' AXZRATE, \n");
	     sql.append(" to_number(JD.BXZ) BXZ,to_number(JD.BPJKL) BPJKL,to_number(JD.BLC) BLC, to_number(JD.BLCKL) BLCKL,  \n");
	     sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.BXZ,0,'0',ROUND(JD.BXZ*100/JD.HABCXZ,2))) || '%' BXZRATE, \n");
	     sql.append(" to_number(JD.CXZ) CXZ,to_number(JD.CPJKL) CPJKL,to_number(JD.CLC) CLC, to_number(JD.CLCKL) CLCKL,  \n");
	     sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.CXZ,0,'0',ROUND(JD.CXZ*100/JD.HABCXZ,2))) || '%' CXZRATE, \n");
	     sql.append(" to_number(JD.HABXZ) HABXZ,to_number(JD.HABLC) HABLC, to_number(JD.HABXZLC) HABXZLC,  \n");
	     sql.append(" to_number(JD.HABCXZ) HABCXZ,to_number(JD.HABCLC) HABCLC, to_number(JD.HABCXZLC) HABCXZLC \n");
	       
	     //sql.append("  FROM (  SELECT to_char(JD.DEALER_ID) DEALER_ID, SUM(JD.SCCOUNT) SCCOUNT,SUM(JD.YYCOUNT) YYCOUNT, SUM(JD.KLTOTAL) KLTOTAL,SUM(JD.YJSCOUNT) YJSCOUNT,SUM(JD.KLCOUNT) KLCOUNT,   \n");
	     //sql.append("  SUM(JD.JKCOUNT) JKCOUNT,SUM(JD.DDCOUNT) DDCOUNT,SUM(JD.JCCOUNT) JCCOUNT,SUM(JD.SJCOUNT) SJCOUNT,SUM(JD.ZHCOUNT) ZHCOUNT,SUM(JD.CDCOUNT)  CDCOUNT  \n");  
	     sql.append("  FROM (   \n");
	     sql.append(" select dd.dealer_id,sum(HXZ) HXZ,sum(HPJKL) HPJKL,sum(HLC) HLC,sum(HLCKL) HLCKL,sum(AXZ) AXZ,sum(APJKL) APJKL,sum(ALC) ALC,sum(ALCKL) ALCKL,  \n");
	     sql.append(" sum(BXZ) BXZ,sum(BPJKL) BPJKL,sum(BLC) BLC,sum(BLCKL) BLCKL,sum(CXZ) CXZ,sum(CPJKL) CPJKL,sum(CLC) CLC,sum(CLCKL) CLCKL, \n");
	     sql.append(" sum(HXZ+AXZ+BXZ) HABXZ,sum(HLC+ALC+BLC) HABLC, sum(HXZ+AXZ+BXZ+HLC+ALC+BLC) HABXZLC,  \n");
	     sql.append(" sum(HXZ+AXZ+BXZ+CXZ) HABCXZ,sum(HLC+ALC+BLC+CLC) HABCLC, sum(HXZ+AXZ+BXZ+CXZ+HLC+ALC+BLC+CLC) HABCXZLC \n");
	     sql.append("		from(  \n");

	     sql.append(" select tpc.dealer_id,sum(decode(tpc.ctm_rank, 60101001, 1, 0))  HXZ,round(sum(to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')-to_date(to_char(tpc.create_date,'yyyy-MM-dd'),'yyyy-MM-dd'))/sum(decode(tpc.ctm_rank, 60101001, 1, 0))) HPJKL, \n");
	     sql.append(" 0 HLC, 0 HLCKL,0 AXZ,0 APJKL,0 ALC,0 ALCKL,0 BXZ,0 BPJKL,0 BLC,0 BLCKL,0 CXZ,0 CPJKL,0 CLC,0 CLCKL  \n");
	     sql.append(" from t_pc_customer tpc  where tpc.ctm_rank='60101001' and tpc.create_date>=trunc(sysdate, 'mm') group by tpc.dealer_id \n");
	     sql.append(" union all \n");
	     sql.append(" select  tpc.dealer_id,0 HXZ,0 HPJKL,sum(decode(tpc.ctm_rank, 60101001, 1, 0)) as HLC, \n");
	     sql.append(" round(sum(to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')-to_date(to_char(tpc.create_date,'yyyy-MM-dd'),'yyyy-MM-dd'))/sum(decode(tpc.ctm_rank, 60101001, 1, 0))) HLCKL, \n");
	     sql.append(" 0 AXZ,0 APJKL,0 ALC,0 ALCKL,0 BXZ,0 BPJKL,0 BLC,0 BLCKL,0 CXZ,0 CPJKL,0 CLC,0 CLCKL \n");
	     sql.append(" from t_pc_customer tpc  where tpc.ctm_rank='60101001' and tpc.create_date<=trunc(sysdate, 'mm') group by tpc.dealer_id \n");
	     
	     sql.append(" union all \n");
	     
	    //A级客户统计
	     sql.append(" select tpc.dealer_id,0 HXZ,0 HPJKL,0 HLC,0 HLCKL,sum(decode(tpc.ctm_rank, 60101002, 1, 0))  AXZ, \n");
	     sql.append(" round(sum(to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')-to_date(to_char(tpc.create_date,'yyyy-MM-dd'),'yyyy-MM-dd'))/sum(decode(tpc.ctm_rank, 60101002, 1, 0))) AJKL, \n");
	     sql.append(" 0 ALC,0 ALCKL,0 BXZ,0 BPJKL,0 BLC,0 BLCKL,0 CXZ,0 CPJKL,0 CLC,0 CLCKL \n");
	     sql.append(" from t_pc_customer tpc  where tpc.ctm_rank='60101002' and tpc.create_date>=trunc(sysdate, 'mm') group by tpc.dealer_id \n");
	
	     sql.append(" union all \n");
	     sql.append(" select tpc.dealer_id,0 HXZ,0 HPJKL,0 HLC,0 HLCKL,0 AXZ,0 APJKL,sum(decode(tpc.ctm_rank, 60101002, 1, 0)) as ALC, \n");
	     sql.append(" round(sum(to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')-to_date(to_char(tpc.create_date,'yyyy-MM-dd'),'yyyy-MM-dd'))/sum(decode(tpc.ctm_rank, 60101002, 1, 0))) ALCKL, \n");
	     sql.append(" 0 BXZ,0 BPJKL,0 BLC,0 BLCKL,0 CXZ,0 CPJKL,0 CLC,0 CLCKL \n"); 
	     sql.append(" from t_pc_customer tpc  where tpc.ctm_rank='60101002' and tpc.create_date<=trunc(sysdate, 'mm') group by tpc.dealer_id \n"); 

	     sql.append(" union all \n");
	     // B级客户统计
	     sql.append(" select tpc.dealer_id,0 HXZ,0 HPJKL,0 HLC,0 HLCKL,0 AXZ,0 APJKL,0 ALC,0 ALCKL,sum(decode(tpc.ctm_rank, 60101003, 1, 0))  BXZ,\n"); 
	     sql.append(" round(sum(to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')-to_date(to_char(tpc.create_date,'yyyy-MM-dd'),'yyyy-MM-dd'))/sum(decode(tpc.ctm_rank, 60101003, 1, 0))) BJKL, \n"); 
	     sql.append(" 0 BLC,0 BLCKL,0 CXZ,0 CPJKL,0 CLC,0 CLCKL \n"); 
	     sql.append(" from t_pc_customer tpc  where tpc.ctm_rank='60101003' and tpc.create_date>=trunc(sysdate, 'mm') group by tpc.dealer_id \n");
	     
	     sql.append(" union all \n");
	     sql.append(" select tpc.dealer_id,0 HXZ,0 HPJKL,0 HLC,0 HLCKL,0 AXZ,0 APJKL,0 ALC,0 ALCKL,0 BXZ,0 BPJKL,sum(decode(tpc.ctm_rank, 60101003, 1, 0)) as BLC,\n"); 
	     sql.append(" round(sum(to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')-to_date(to_char(tpc.create_date,'yyyy-MM-dd'),'yyyy-MM-dd'))/sum(decode(tpc.ctm_rank, 60101003, 1, 0))) BLCKL,\n"); 
	     sql.append(" 0 CXZ,0 CPJKL,0 CLC,0 CLCKL \n");
	     sql.append(" from t_pc_customer tpc  where tpc.ctm_rank='60101003' and tpc.create_date<=trunc(sysdate, 'mm') group by tpc.dealer_id \n"); 

	     sql.append(" union all \n");
	     //C级客户统计
	     sql.append(" select tpc.dealer_id,0 HXZ,0 HPJKL,0 HLC, 0 HLCKL,0 AXZ,0 APJKL,0 ALC,0 ALCKL,0 BXZ,0 BPJKL,0 BLC,0 BLCKL,sum(decode(tpc.ctm_rank, 60101004, 1, 0))  CXZ,\n"); 
	     sql.append(" round(sum(to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')-to_date(to_char(tpc.create_date,'yyyy-MM-dd'),'yyyy-MM-dd'))/sum(decode(tpc.ctm_rank, 60101004, 1, 0))) CJKL,\n"); 
	     sql.append(" 0 CLC, 0 CLCKL from t_pc_customer tpc  where tpc.ctm_rank='60101004' and tpc.create_date>=trunc(sysdate, 'mm') group by tpc.dealer_id \n"); 
	     sql.append(" union all \n");
	     sql.append(" select tpc.dealer_id,0 HXZ,0 HPJKL,0 HLC,0 HLCKL,0 AXZ,0 APJKL,0 ALC,0 ALCKL,0 BXZ,0 BPJKL,0 BLC,0 BLCKL,0 CXZ,0 CPJKL,sum(decode(tpc.ctm_rank, 60101004, 1, 0)) as CLC,\n"); 
	     sql.append(" round(sum(to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')-to_date(to_char(tpc.create_date,'yyyy-MM-dd'),'yyyy-MM-dd'))/sum(decode(tpc.ctm_rank, 60101004, 1, 0))) CLCKL \n"); 
	     sql.append(" from t_pc_customer tpc  where tpc.ctm_rank='60101004' and tpc.create_date<=trunc(sysdate, 'mm') group by tpc.dealer_id \n");
	    
	     sql.append(" )dd group by dd.dealer_id \n");
	     
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
		 sql.append("   A.HXZ,A.HPJKL,A.HLC,A.HLCKL,HXZRATE,A.AXZ,A.APJKL,A.ALC,A.ALCKL,AXZRATE,A.BXZ,A.BPJKL,A.BLC,A.BLCKL,BXZRATE,A.CXZ,A.CPJKL,A.CLC,A.CLCKL,CXZRATE, \n");
		 sql.append("	A.HABXZ,A.HABLC,A.HABXZLC,A.HABCXZ,A.HABCLC,A.HABCXZLC from a  \n");
		 if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||   Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("  union all   \n");
			 sql.append("  select   '',A.ROOT_ORG_NAME,A.PQ_ORG_NAME,' ',max(A.PQ_ORG_NAME)||'小计','3' ,   \n");
			 sql.append(" sum(A.HXZ), sum(A.HPJKL),sum(A.HLC), sum(A.HLCKL), \n" );
			 sql.append(" DECODE(sum(A.HABCXZ),0,'0',DECODE(sum(A.HXZ),0,'0',ROUND(sum(A.HXZ)*100/sum(A.HABCXZ),2))) || '%' HXZRATE, \n");
			 sql.append(" sum(A.AXZ), sum(A.APJKL),sum(A.ALC), sum(A.ALCKL), \n" );
			 sql.append(" DECODE(sum(A.HABCXZ),0,'0',DECODE(sum(A.AXZ),0,'0',ROUND(sum(A.AXZ)*100/sum(A.HABCXZ),2))) || '%' AXZRATE, \n");
			 sql.append(" sum(A.BXZ), sum(A.BPJKL),sum(A.BLC), sum(A.BLCKL), \n" );
			 sql.append(" DECODE(sum(A.HABCXZ),0,'0',DECODE(sum(A.BXZ),0,'0',ROUND(sum(A.BXZ)*100/sum(A.HABCXZ),2))) || '%' BXZRATE, \n");
			 sql.append(" sum(A.CXZ), sum(A.CPJKL),sum(A.CLC), sum(A.CLCKL), \n" );
			 sql.append(" DECODE(sum(A.HABCXZ),0,'0',DECODE(sum(A.CXZ),0,'0',ROUND(sum(A.CXZ)*100/sum(A.HABCXZ),2))) || '%' CXZRATE, \n");
			 sql.append(" sum(A.HABXZ),sum(A.HABLC),sum(A.HABXZLC),sum(A.HABCXZ),sum(A.HABCLC),sum(A.HABCXZLC)  \n");
			 
			sql.append(" from a  group by A.ROOT_ORG_NAME,A.PQ_ORG_NAME    \n");
		 }  if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||   Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				 sql.append("  union all   \n");
				 sql.append("   select   '',A.ROOT_ORG_NAME,' ',' ',max(a.ROOT_ORG_NAME)||'合计','2'  ,  \n");
				 
				 sql.append(" sum(A.HXZ), sum(A.HPJKL),sum(A.HLC), sum(A.HLCKL), \n" );
				 sql.append(" DECODE(sum(A.HABCXZ),0,'0',DECODE(sum(A.HXZ),0,'0',ROUND(sum(A.HXZ)*100/sum(A.HABCXZ),2))) || '%' HXZRATE, \n");

				 sql.append(" sum(A.AXZ), sum(A.APJKL),sum(A.ALC), sum(A.ALCKL), \n" );
				 sql.append(" DECODE(sum(A.HABCXZ),0,'0',DECODE(sum(A.AXZ),0,'0',ROUND(sum(A.AXZ)*100/sum(A.HABCXZ),2))) || '%' AXZRATE, \n");

				 sql.append(" sum(A.BXZ), sum(A.BPJKL),sum(A.BLC), sum(A.BLCKL), \n" );
				 sql.append(" DECODE(sum(A.HABCXZ),0,'0',DECODE(sum(A.BXZ),0,'0',ROUND(sum(A.BXZ)*100/sum(A.HABCXZ),2))) || '%' BXZRATE, \n");

				 sql.append(" sum(A.CXZ), sum(A.CPJKL),sum(A.CLC), sum(A.CLCKL), \n" );
				 sql.append(" DECODE(sum(A.HABCXZ),0,'0',DECODE(sum(A.CXZ),0,'0',ROUND(sum(A.CXZ)*100/sum(A.HABCXZ),2))) || '%' CXZRATE, \n");

				 sql.append(" sum(A.HABXZ),sum(A.HABLC),sum(A.HABXZLC),sum(A.HABCXZ),sum(A.HABCLC),sum(A.HABCXZLC)  \n");
				 
			sql.append(" from a group by A.ROOT_ORG_NAME   \n");
		   }
		   if(  Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("  union all   \n");
			 sql.append("  select '',  ' ',' ' , ' ','全国合计','1' ,   \n");
			 
			 sql.append(" sum(A.HXZ), sum(A.HPJKL),sum(A.HLC), sum(A.HLCKL), \n" );
			 sql.append(" DECODE(sum(A.HABCXZ),0,'0',DECODE(sum(A.HXZ),0,'0',ROUND(sum(A.HXZ)*100/sum(A.HABCXZ),2))) || '%' HXZRATE, \n");

			 sql.append(" sum(A.AXZ), sum(A.APJKL),sum(A.ALC), sum(A.ALCKL), \n" );
			 sql.append(" DECODE(sum(A.HABCXZ),0,'0',DECODE(sum(A.AXZ),0,'0',ROUND(sum(A.AXZ)*100/sum(A.HABCXZ),2))) || '%' AXZRATE, \n");

			 sql.append(" sum(A.BXZ), sum(A.BPJKL),sum(A.BLC), sum(A.BLCKL), \n" );
			 sql.append(" DECODE(sum(A.HABCXZ),0,'0',DECODE(sum(A.BXZ),0,'0',ROUND(sum(A.BXZ)*100/sum(A.HABCXZ),2))) || '%' BXZRATE, \n");

			 sql.append(" sum(A.CXZ), sum(A.CPJKL),sum(A.CLC), sum(A.CLCKL), \n" );
			 sql.append(" DECODE(sum(A.HABCXZ),0,'0',DECODE(sum(A.CXZ),0,'0',ROUND(sum(A.CXZ)*100/sum(A.HABCXZ),2))) || '%' CXZRATE, \n");

			 sql.append(" sum(A.HABXZ),sum(A.HABLC),sum(A.HABXZLC),sum(A.HABCXZ),sum(A.HABCLC),sum(A.HABCXZLC) from a  \n");
			 
			 
	 }
		 sql.append("  ) tmp  order by  tmp.a,tmp.ROOT_ORG_NAME, tmp.pq_org_name ,rownum  \n");
		 
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
	sql.append("     select * from (with tpc as (    \n");
     sql.append("   SELECT  VW.ROOT_ORG_NAME,VW.PQ_ORG_NAME,VW.DEALER_CODE,VW.DEALER_SHORTNAME,  \n");
     sql.append(" tpc.DEALER_ID,tpc.adviser,tpc.ctm_rank, ROUND(SYSDATE - tpc.create_date,2) KL,tpc.create_date,CASE WHEN  tpc.create_date<trunc(sysdate, 'mm') THEN 0 else 1 end status   \n");
     sql.append(" from t_pc_customer tpc,VW_ORG_DEALER_ALL_NEW vw  where  TPC.CTM_TYPE IN ('60341002','60341003')   and tpc.dealer_id=vw.dealer_id  \n");
	 
     if(!CommonUtils.isNullString(seriesId)){
	    	sql.append("   and tpc.intent_vehicle in (select tpiv.series_id from t_pc_intent_vehicle tpiv  where tpiv.up_series_id in ("+seriesId+")  )   \n" );
     }
	 if(!CommonUtils.isNullString(startDate)){
      sql.append("      		    AND  tpc.CREATE_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
     }
     if(!CommonUtils.isNullString(endDate)){
      sql.append("      		  AND  tpc.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
     }
     
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
	 
	 
	 sql.append(" select TO_CHAR(DEALER_ID) dealer_id,root_org_name,pq_org_name,dealer_code ,dealer_shortname, '3' a, \n");
	 sql.append(" HXZ,HPJKL,HLC,HLCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.HXZ,0,'0',ROUND(JD.HXZ*100/JD.HABCXZ,2))) || '%' HXZRATE, \n");
	 sql.append(" AXZ,APJKL,ALC,ALCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.AXZ,0,'0',ROUND(JD.AXZ*100/JD.HABCXZ,2))) || '%' AXZRATE, \n");
	 sql.append(" BXZ,BPJKL,BLC,BLCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.BXZ,0,'0',ROUND(JD.BXZ*100/JD.HABCXZ,2))) || '%' BXZRATE, \n");
	 sql.append(" CXZ,CPJKL,CLC,CLCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.CXZ,0,'0',ROUND(JD.CXZ*100/JD.HABCXZ,2))) || '%' CXZRATE, \n");
	 
	 sql.append(" OXZ,OPJKL,OLC,OLCKL, \n");
	 //sql.append("DECODE(JD.HABCXZ,0,'0',DECODE(JD.OXZ,0,'0',ROUND(JD.OXZ*100/JD.HABCXZ,2))) || '%' OXZRATE,\n"); 
	 sql.append(" EXZ,EPJKL,ELC,ELCKL, \n"); 
	// sql.append("DECODE(JD.HABCXZ,0,'0',DECODE(JD.EXZ,0,'0',ROUND(JD.EXZ*100/JD.HABCXZ,2))) || '%' EXZRATE, \n");
	 sql.append(" LXZ,LPJKL,LLC,LLCKL, \n"); 
	// sql.append("DECODE(JD.HABCXZ,0,'0',DECODE(JD.LXZ,0,'0',ROUND(JD.LXZ*100/JD.HABCXZ,2))) || '%' LXZRATE, \n");
	 
	 sql.append(" HABXZ,HABLC,HABXZLC,HABCXZ,HABCLC,HABCXZLC \n");
	 sql.append(" from ( \n");
	 sql.append(" select '',root_org_name,pq_org_name,dealer_id,dealer_code ,dealer_shortname, '3' a, \n");
	 sql.append(" sum(HXZ) HXZ, DECODE(sum(HXZKL),0,'0',DECODE(sum(HXZ),0,'0',ROUND(sum(HXZKL)/sum(HXZ),2))) HPJKL, \n");
	 sql.append(" sum(HLC) HLC, DECODE(sum(HLCKL),0,'0',DECODE(sum(HLC),0,'0',ROUND(sum(HLCKL)/sum(HLC),2))) HLCKL, \n");
	  
	 sql.append(" sum(AXZ) AXZ,DECODE(sum(AXZKL),0,'0',DECODE(sum(AXZ),0,'0',ROUND(sum(AXZKL)/sum(AXZ),2)))  APJKL, \n");
	 sql.append(" sum(ALC) ALC, DECODE(sum(ALCKL),0,'0',DECODE(sum(ALC),0,'0',ROUND(sum(ALCKL)/sum(ALC),2))) ALCKL, \n");


	 sql.append(" sum(BXZ) BXZ,DECODE(sum(BXZKL),0,'0',DECODE(sum(BXZ),0,'0',ROUND(sum(BXZKL)/sum(BXZ),2))) BPJKL, \n");
	 sql.append(" sum(BLC) BLC, DECODE(sum(BLCKL),0,'0',DECODE(sum(BLC),0,'0',ROUND(sum(BLCKL)/sum(BLC),2))) BLCKL, \n");

	 sql.append(" sum(CXZ) CXZ,DECODE(sum(CXZKL),0,'0',DECODE(sum(CXZ),0,'0',ROUND(sum(CXZKL)/sum(CXZ),2))) CPJKL, \n");
	 sql.append(" sum(CLC) CLC, DECODE(sum(CLCKL),0,'0',DECODE(sum(CLC),0,'0',ROUND(sum(CLCKL)/sum(CLC),2))) CLCKL, \n");

	 
	 sql.append(" sum(OXZ) OXZ,DECODE(sum(OXZKL),0,'0',DECODE(sum(OXZ),0,'0',ROUND(sum(OXZKL)/sum(OXZ),2))) OPJKL, \n");
	 sql.append(" sum(OLC) OLC, DECODE(sum(OLCKL),0,'0',DECODE(sum(OLC),0,'0',ROUND(sum(OLCKL)/sum(OLC),2))) OLCKL, \n");
	 
	 sql.append(" sum(EXZ) EXZ,DECODE(sum(EXZKL),0,'0',DECODE(sum(EXZ),0,'0',ROUND(sum(EXZKL)/sum(EXZ),2))) EPJKL, \n");
	 sql.append(" sum(ELC) ELC, DECODE(sum(ELCKL),0,'0',DECODE(sum(ELC),0,'0',ROUND(sum(ELCKL)/sum(ELC),2))) ELCKL, \n");
	 
	 sql.append(" sum(LXZ) LXZ,DECODE(sum(LXZKL),0,'0',DECODE(sum(LXZ),0,'0',ROUND(sum(LXZKL)/sum(LXZ),2))) LPJKL, \n");
	 sql.append(" sum(LLC) LLC, DECODE(sum(LLCKL),0,'0',DECODE(sum(LLC),0,'0',ROUND(sum(LLCKL)/sum(LLC),2))) LLCKL, \n");
	 
	 sql.append(" sum(HXZ+AXZ+BXZ) HABXZ,sum(HLC+ALC+BLC) HABLC, sum(HXZ+AXZ+BXZ+HLC+ALC+BLC) HABXZLC,  \n");
	 sql.append(" sum(HXZ+AXZ+BXZ+CXZ) HABCXZ,sum(HLC+ALC+BLC+CLC) HABCLC, sum(HXZ+AXZ+BXZ+CXZ+HLC+ALC+BLC+CLC) HABCXZLC  \n"); 
	 sql.append(" from ( \n");
	 sql.append(" select '',root_org_name,pq_org_name,dealer_id,dealer_code ,dealer_shortname, '3' a, \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,1,sl,0)),0) HXZ, \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,1,kl,0)),0) HXZKL, \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,0,sl,0)),0) HLC, \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,0,kl,0)),0) HLCKL, \n");

	 sql.append(" decode(ctm_rank,60101002,(decode(status,1,sl,0)),0) AXZ, \n");
	 sql.append(" decode(ctm_rank,60101002,(decode(status,1,kl,0)),0) AXZKL, \n");
	 sql.append(" decode(ctm_rank,60101002,(decode(status,0,sl,0)),0) ALC, \n");
	 sql.append(" decode(ctm_rank,60101002,(decode(status,0,kl,0)),0) ALCKL, \n");

	 sql.append(" decode(ctm_rank,60101003,(decode(status,1,sl,0)),0) BXZ, \n");
	 sql.append(" decode(ctm_rank,60101003,(decode(status,1,kl,0)),0) BXZKL, \n");
	 sql.append(" decode(ctm_rank,60101003,(decode(status,0,sl,0)),0) BLC, \n");
	 sql.append(" decode(ctm_rank,60101003,(decode(status,0,kl,0)),0) BLCKL, \n");

	 sql.append(" decode(ctm_rank,60101004,(decode(status,1,sl,0)),0) CXZ, \n");
	 sql.append(" decode(ctm_rank,60101004,(decode(status,1,kl,0)),0) CXZKL, \n");
	 sql.append(" decode(ctm_rank,60101004,(decode(status,0,sl,0)),0) CLC, \n");
	 sql.append(" decode(ctm_rank,60101004,(decode(status,0,kl,0)),0) CLCKL, \n");
	 
	 sql.append(" decode(ctm_rank,60101005,(decode(status,1,sl,0)),0) OXZ,\n");
	 sql.append(" decode(ctm_rank,60101005,(decode(status,1,kl,0)),0) OXZKL, \n");
	 sql.append(" decode(ctm_rank,60101005,(decode(status,0,sl,0)),0) OLC, \n");
	 sql.append(" decode(ctm_rank,60101005,(decode(status,0,kl,0)),0) OLCKL, \n");
	 
	 sql.append(" decode(ctm_rank,60101006,(decode(status,1,sl,0)),0) EXZ,\n");
	 sql.append(" decode(ctm_rank,60101006,(decode(status,1,kl,0)),0) EXZKL,\n"); 
	 sql.append(" decode(ctm_rank,60101006,(decode(status,0,sl,0)),0) ELC, \n");
	 sql.append(" decode(ctm_rank,60101006,(decode(status,0,kl,0)),0) ELCKL,\n");
	 
	 sql.append(" decode(ctm_rank,60101007,(decode(status,1,sl,0)),0) LXZ,\n");
	 sql.append(" decode(ctm_rank,60101007,(decode(status,1,kl,0)),0) LXZKL, \n");
	 sql.append(" decode(ctm_rank,60101007,(decode(status,0,sl,0)),0) LLC, \n");
	 sql.append(" decode(ctm_rank,60101007,(decode(status,0,kl,0)),0) LLCKL\n");

	 sql.append(" from ( \n");
	 sql.append(" select tpc.root_org_name,tpc.pq_org_name,tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,status,sum(1) sl,sum(KL) KL from tpc where 1=1 group by tpc.root_org_name,tpc.pq_org_name,tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,status  \n");
	 sql.append(" order by tpc.dealer_id,tpc.dealer_code,tpc.dealer_shortname,tpc.ctm_rank,status \n");
	 sql.append(" ) \n");
	 sql.append(" ) jd where 1=1 group by jd.root_org_name,jd.pq_org_name,jd.dealer_id,jd.dealer_code,jd.dealer_shortname \n");
	 sql.append(" )JD \n");
	 
	 sql.append(" union all  \n");
	 
	 sql.append(" select  '',root_org_name,pq_org_name,'',PQ_ORG_NAME||'小计', '3', \n");
	 sql.append(" HXZ,HPJKL,HLC,HLCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.HXZ,0,'0',ROUND(JD.HXZ*100/JD.HABCXZ,2))) || '%' HXZRATE, \n");
	 sql.append(" AXZ,APJKL,ALC,ALCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.AXZ,0,'0',ROUND(JD.AXZ*100/JD.HABCXZ,2))) || '%' AXZRATE, \n");
	 sql.append(" BXZ,BPJKL,BLC,BLCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.BXZ,0,'0',ROUND(JD.BXZ*100/JD.HABCXZ,2))) || '%' BXZRATE, \n");
	 sql.append(" CXZ,CPJKL,CLC,CLCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.CXZ,0,'0',ROUND(JD.CXZ*100/JD.HABCXZ,2))) || '%' CXZRATE, \n");
	 
	 sql.append(" OXZ,OPJKL,OLC,OLCKL, \n");
	 //sql.append("DECODE(JD.HABCXZ,0,'0',DECODE(JD.OXZ,0,'0',ROUND(JD.OXZ*100/JD.HABCXZ,2))) || '%' OXZRATE,\n"); 
	 sql.append(" EXZ,EPJKL,ELC,ELCKL, \n"); 
	// sql.append("DECODE(JD.HABCXZ,0,'0',DECODE(JD.EXZ,0,'0',ROUND(JD.EXZ*100/JD.HABCXZ,2))) || '%' EXZRATE, \n");
	 sql.append(" LXZ,LPJKL,LLC,LLCKL, \n"); 
	// sql.append("DECODE(JD.HABCXZ,0,'0',DECODE(JD.LXZ,0,'0',ROUND(JD.LXZ*100/JD.HABCXZ,2))) || '%' LXZRATE, \n");
	
	 sql.append(" HABXZ,HABLC,HABXZLC,HABCXZ,HABCLC,HABCXZLC \n");
	 sql.append(" from ( \n");
	 sql.append(" select '',root_org_name,pq_org_name,'','', '3', \n");
	 sql.append(" sum(HXZ) HXZ, DECODE(sum(HXZKL),0,'0',DECODE(sum(HXZ),0,'0',ROUND(sum(HXZKL)/sum(HXZ),2))) HPJKL, \n");
	 sql.append(" sum(HLC) HLC, DECODE(sum(HLCKL),0,'0',DECODE(sum(HLC),0,'0',ROUND(sum(HLCKL)/sum(HLC),2))) HLCKL, \n");
	  
	 sql.append(" sum(AXZ) AXZ,DECODE(sum(AXZKL),0,'0',DECODE(sum(AXZ),0,'0',ROUND(sum(AXZKL)/sum(AXZ),2)))  APJKL, \n");
	 sql.append(" sum(ALC) ALC, DECODE(sum(ALCKL),0,'0',DECODE(sum(ALC),0,'0',ROUND(sum(ALCKL)/sum(ALC),2))) ALCKL, \n");


	 sql.append(" sum(BXZ) BXZ,DECODE(sum(BXZKL),0,'0',DECODE(sum(BXZ),0,'0',ROUND(sum(BXZKL)/sum(BXZ),2))) BPJKL, \n");
	 sql.append(" sum(BLC) BLC, DECODE(sum(BLCKL),0,'0',DECODE(sum(BLC),0,'0',ROUND(sum(BLCKL)/sum(BLC),2))) BLCKL, \n");

	 sql.append(" sum(CXZ) CXZ,DECODE(sum(CXZKL),0,'0',DECODE(sum(CXZ),0,'0',ROUND(sum(CXZKL)/sum(CXZ),2))) CPJKL, \n");
	 sql.append(" sum(CLC) CLC, DECODE(sum(CLCKL),0,'0',DECODE(sum(CLC),0,'0',ROUND(sum(CLCKL)/sum(CLC),2))) CLCKL, \n");
	 

	 sql.append(" sum(OXZ) OXZ,DECODE(sum(OXZKL),0,'0',DECODE(sum(OXZ),0,'0',ROUND(sum(OXZKL)/sum(OXZ),2))) OPJKL, \n");
	 sql.append(" sum(OLC) OLC, DECODE(sum(OLCKL),0,'0',DECODE(sum(OLC),0,'0',ROUND(sum(OLCKL)/sum(OLC),2))) OLCKL, \n");
	 
	 sql.append(" sum(EXZ) EXZ,DECODE(sum(EXZKL),0,'0',DECODE(sum(EXZ),0,'0',ROUND(sum(EXZKL)/sum(EXZ),2))) EPJKL, \n");
	 sql.append(" sum(ELC) ELC, DECODE(sum(ELCKL),0,'0',DECODE(sum(ELC),0,'0',ROUND(sum(ELCKL)/sum(ELC),2))) ELCKL, \n");
	 
	 sql.append(" sum(LXZ) LXZ,DECODE(sum(LXZKL),0,'0',DECODE(sum(LXZ),0,'0',ROUND(sum(LXZKL)/sum(LXZ),2))) LPJKL, \n");
	 sql.append(" sum(LLC) LLC, DECODE(sum(LLCKL),0,'0',DECODE(sum(LLC),0,'0',ROUND(sum(LLCKL)/sum(LLC),2))) LLCKL, \n");
	 

	 sql.append(" sum(HXZ+AXZ+BXZ) HABXZ,sum(HLC+ALC+BLC) HABLC, sum(HXZ+AXZ+BXZ+HLC+ALC+BLC) HABXZLC,  \n");
	 sql.append(" sum(HXZ+AXZ+BXZ+CXZ) HABCXZ,sum(HLC+ALC+BLC+CLC) HABCLC, sum(HXZ+AXZ+BXZ+CXZ+HLC+ALC+BLC+CLC) HABCXZLC  \n"); 
	 sql.append(" from ( \n");
	 sql.append(" select '',root_org_name,pq_org_name,'','', '3',  \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,1,sl,0)),0) HXZ, \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,1,kl,0)),0) HXZKL, \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,0,sl,0)),0) HLC, \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,0,kl,0)),0) HLCKL, \n");

	 sql.append(" decode(ctm_rank,60101002,(decode(status,1,sl,0)),0) AXZ, \n");
	 sql.append(" decode(ctm_rank,60101002,(decode(status,1,kl,0)),0) AXZKL, \n");
	 sql.append(" decode(ctm_rank,60101002,(decode(status,0,sl,0)),0) ALC, \n");
	 sql.append(" decode(ctm_rank,60101002,(decode(status,0,kl,0)),0) ALCKL, \n");

	 sql.append(" decode(ctm_rank,60101003,(decode(status,1,sl,0)),0) BXZ, \n");
	 sql.append(" decode(ctm_rank,60101003,(decode(status,1,kl,0)),0) BXZKL, \n");
	 sql.append(" decode(ctm_rank,60101003,(decode(status,0,sl,0)),0) BLC, \n");
	 sql.append(" decode(ctm_rank,60101003,(decode(status,0,kl,0)),0) BLCKL, \n");

	 sql.append(" decode(ctm_rank,60101004,(decode(status,1,sl,0)),0) CXZ, \n");
	 sql.append(" decode(ctm_rank,60101004,(decode(status,1,kl,0)),0) CXZKL, \n");
	 sql.append(" decode(ctm_rank,60101004,(decode(status,0,sl,0)),0) CLC, \n");
	 sql.append(" decode(ctm_rank,60101004,(decode(status,0,kl,0)),0) CLCKL, \n");
	 
	 sql.append(" decode(ctm_rank,60101005,(decode(status,1,sl,0)),0) OXZ,\n");
	 sql.append(" decode(ctm_rank,60101005,(decode(status,1,kl,0)),0) OXZKL, \n");
	 sql.append(" decode(ctm_rank,60101005,(decode(status,0,sl,0)),0) OLC, \n");
	 sql.append(" decode(ctm_rank,60101005,(decode(status,0,kl,0)),0) OLCKL, \n");
	 
	 sql.append(" decode(ctm_rank,60101006,(decode(status,1,sl,0)),0) EXZ,\n");
	 sql.append(" decode(ctm_rank,60101006,(decode(status,1,kl,0)),0) EXZKL,\n"); 
	 sql.append(" decode(ctm_rank,60101006,(decode(status,0,sl,0)),0) ELC, \n");
	 sql.append(" decode(ctm_rank,60101006,(decode(status,0,kl,0)),0) ELCKL,\n");
	 
	 sql.append(" decode(ctm_rank,60101007,(decode(status,1,sl,0)),0) LXZ,\n");
	 sql.append(" decode(ctm_rank,60101007,(decode(status,1,kl,0)),0) LXZKL, \n");
	 sql.append(" decode(ctm_rank,60101007,(decode(status,0,sl,0)),0) LLC, \n");
	 sql.append(" decode(ctm_rank,60101007,(decode(status,0,kl,0)),0) LLCKL\n");


	 sql.append(" from ( \n");
	 sql.append(" select tpc.root_org_name,tpc.pq_org_name,tpc.ctm_rank,status,sum(1) sl,sum(KL) KL from tpc where 1=1 group by tpc.root_org_name,tpc.pq_org_name,tpc.ctm_rank,status   \n");
	 sql.append(" order by tpc.root_org_name,tpc.pq_org_name,tpc.ctm_rank,status \n");
	 sql.append(" ) \n");
	 sql.append(" ) jd where 1=1 group by root_org_name,pq_org_name \n");
	 sql.append(" )JD \n");
	 
	 sql.append(" union all  \n");
	 
	 sql.append(" select  '',root_org_name,'','',ROOT_ORG_NAME||'合计','2', \n");
	 sql.append(" HXZ,HPJKL,HLC,HLCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.HXZ,0,'0',ROUND(JD.HXZ*100/JD.HABCXZ,2))) || '%' HXZRATE, \n");
	 sql.append(" AXZ,APJKL,ALC,ALCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.AXZ,0,'0',ROUND(JD.AXZ*100/JD.HABCXZ,2))) || '%' AXZRATE, \n");
	 sql.append(" BXZ,BPJKL,BLC,BLCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.BXZ,0,'0',ROUND(JD.BXZ*100/JD.HABCXZ,2))) || '%' BXZRATE, \n");
	 sql.append(" CXZ,CPJKL,CLC,CLCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.CXZ,0,'0',ROUND(JD.CXZ*100/JD.HABCXZ,2))) || '%' CXZRATE, \n");
	 
	 sql.append(" OXZ,OPJKL,OLC,OLCKL, \n");
	// sql.append("DECODE(JD.HABCXZ,0,'0',DECODE(JD.OXZ,0,'0',ROUND(JD.OXZ*100/JD.HABCXZ,2))) || '%' OXZRATE,\n"); 
	 sql.append(" EXZ,EPJKL,ELC,ELCKL, \n"); 
	// sql.append("DECODE(JD.HABCXZ,0,'0',DECODE(JD.EXZ,0,'0',ROUND(JD.EXZ*100/JD.HABCXZ,2))) || '%' EXZRATE, \n");
	 sql.append(" LXZ,LPJKL,LLC,LLCKL, \n"); 
	// sql.append("DECODE(JD.HABCXZ,0,'0',DECODE(JD.LXZ,0,'0',ROUND(JD.LXZ*100/JD.HABCXZ,2))) || '%' LXZRATE, \n");
	
	 sql.append(" HABXZ,HABLC,HABXZLC,HABCXZ,HABCLC,HABCXZLC \n");
	 sql.append(" from ( \n");
	 sql.append(" select '',root_org_name,'','','','2', \n");
	 sql.append(" sum(HXZ) HXZ, DECODE(sum(HXZKL),0,'0',DECODE(sum(HXZ),0,'0',ROUND(sum(HXZKL)/sum(HXZ),2))) HPJKL, \n");
	 sql.append(" sum(HLC) HLC, DECODE(sum(HLCKL),0,'0',DECODE(sum(HLC),0,'0',ROUND(sum(HLCKL)/sum(HLC),2))) HLCKL, \n");
	  
	 sql.append(" sum(AXZ) AXZ,DECODE(sum(AXZKL),0,'0',DECODE(sum(AXZ),0,'0',ROUND(sum(AXZKL)/sum(AXZ),2)))  APJKL, \n");
	 sql.append(" sum(ALC) ALC, DECODE(sum(ALCKL),0,'0',DECODE(sum(ALC),0,'0',ROUND(sum(ALCKL)/sum(ALC),2))) ALCKL, \n");


	 sql.append(" sum(BXZ) BXZ,DECODE(sum(BXZKL),0,'0',DECODE(sum(BXZ),0,'0',ROUND(sum(BXZKL)/sum(BXZ),2))) BPJKL, \n");
	 sql.append(" sum(BLC) BLC, DECODE(sum(BLCKL),0,'0',DECODE(sum(BLC),0,'0',ROUND(sum(BLCKL)/sum(BLC),2))) BLCKL, \n");

	 sql.append(" sum(CXZ) CXZ,DECODE(sum(CXZKL),0,'0',DECODE(sum(CXZ),0,'0',ROUND(sum(CXZKL)/sum(CXZ),2))) CPJKL, \n");
	 sql.append(" sum(CLC) CLC, DECODE(sum(CLCKL),0,'0',DECODE(sum(CLC),0,'0',ROUND(sum(CLCKL)/sum(CLC),2))) CLCKL, \n");
	 

	 sql.append(" sum(OXZ) OXZ,DECODE(sum(OXZKL),0,'0',DECODE(sum(OXZ),0,'0',ROUND(sum(OXZKL)/sum(OXZ),2))) OPJKL, \n");
	 sql.append(" sum(OLC) OLC, DECODE(sum(OLCKL),0,'0',DECODE(sum(OLC),0,'0',ROUND(sum(OLCKL)/sum(OLC),2))) OLCKL, \n");
	 
	 sql.append(" sum(EXZ) EXZ,DECODE(sum(EXZKL),0,'0',DECODE(sum(EXZ),0,'0',ROUND(sum(EXZKL)/sum(EXZ),2))) EPJKL, \n");
	 sql.append(" sum(ELC) ELC, DECODE(sum(ELCKL),0,'0',DECODE(sum(ELC),0,'0',ROUND(sum(ELCKL)/sum(ELC),2))) ELCKL, \n");
	 
	 sql.append(" sum(LXZ) LXZ,DECODE(sum(LXZKL),0,'0',DECODE(sum(LXZ),0,'0',ROUND(sum(LXZKL)/sum(LXZ),2))) LPJKL, \n");
	 sql.append(" sum(LLC) LLC, DECODE(sum(LLCKL),0,'0',DECODE(sum(LLC),0,'0',ROUND(sum(LLCKL)/sum(LLC),2))) LLCKL, \n");
	 

	 sql.append(" sum(HXZ+AXZ+BXZ) HABXZ,sum(HLC+ALC+BLC) HABLC, sum(HXZ+AXZ+BXZ+HLC+ALC+BLC) HABXZLC,  \n");
	 sql.append(" sum(HXZ+AXZ+BXZ+CXZ) HABCXZ,sum(HLC+ALC+BLC+CLC) HABCLC, sum(HXZ+AXZ+BXZ+CXZ+HLC+ALC+BLC+CLC) HABCXZLC  \n"); 
	 sql.append(" from ( \n");
	 sql.append(" select '',root_org_name,'','','','2', \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,1,sl,0)),0) HXZ, \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,1,kl,0)),0) HXZKL, \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,0,sl,0)),0) HLC, \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,0,kl,0)),0) HLCKL, \n");

	 sql.append(" decode(ctm_rank,60101002,(decode(status,1,sl,0)),0) AXZ, \n");
	 sql.append(" decode(ctm_rank,60101002,(decode(status,1,kl,0)),0) AXZKL, \n");
	 sql.append(" decode(ctm_rank,60101002,(decode(status,0,sl,0)),0) ALC, \n");
	 sql.append(" decode(ctm_rank,60101002,(decode(status,0,kl,0)),0) ALCKL, \n");

	 sql.append(" decode(ctm_rank,60101003,(decode(status,1,sl,0)),0) BXZ, \n");
	 sql.append(" decode(ctm_rank,60101003,(decode(status,1,kl,0)),0) BXZKL, \n");
	 sql.append(" decode(ctm_rank,60101003,(decode(status,0,sl,0)),0) BLC, \n");
	 sql.append(" decode(ctm_rank,60101003,(decode(status,0,kl,0)),0) BLCKL, \n");

	 sql.append(" decode(ctm_rank,60101004,(decode(status,1,sl,0)),0) CXZ, \n");
	 sql.append(" decode(ctm_rank,60101004,(decode(status,1,kl,0)),0) CXZKL, \n");
	 sql.append(" decode(ctm_rank,60101004,(decode(status,0,sl,0)),0) CLC, \n");
	 sql.append(" decode(ctm_rank,60101004,(decode(status,0,kl,0)),0) CLCKL, \n");
	 
	 sql.append(" decode(ctm_rank,60101005,(decode(status,1,sl,0)),0) OXZ,\n");
	 sql.append(" decode(ctm_rank,60101005,(decode(status,1,kl,0)),0) OXZKL, \n");
	 sql.append(" decode(ctm_rank,60101005,(decode(status,0,sl,0)),0) OLC, \n");
	 sql.append(" decode(ctm_rank,60101005,(decode(status,0,kl,0)),0) OLCKL, \n");
	 
	 sql.append(" decode(ctm_rank,60101006,(decode(status,1,sl,0)),0) EXZ,\n");
	 sql.append(" decode(ctm_rank,60101006,(decode(status,1,kl,0)),0) EXZKL,\n"); 
	 sql.append(" decode(ctm_rank,60101006,(decode(status,0,sl,0)),0) ELC, \n");
	 sql.append(" decode(ctm_rank,60101006,(decode(status,0,kl,0)),0) ELCKL,\n");
	 
	 sql.append(" decode(ctm_rank,60101007,(decode(status,1,sl,0)),0) LXZ,\n");
	 sql.append(" decode(ctm_rank,60101007,(decode(status,1,kl,0)),0) LXZKL, \n");
	 sql.append(" decode(ctm_rank,60101007,(decode(status,0,sl,0)),0) LLC, \n");
	 sql.append(" decode(ctm_rank,60101007,(decode(status,0,kl,0)),0) LLCKL\n");


	 sql.append(" from ( \n");
	 sql.append(" select tpc.root_org_name,tpc.ctm_rank,status,sum(1) sl,sum(KL) KL from tpc where 1=1 group by tpc.root_org_name,tpc.ctm_rank,status   \n");
	 sql.append(" order by tpc.root_org_name,tpc.ctm_rank,status \n");
	 sql.append(" ) \n");
	 sql.append(" ) jd where 1=1 group by root_org_name \n");
	 sql.append(" )JD \n");
	 
	 sql.append(" union all  \n");
	 
	 sql.append(" select  '','','','','全国合计','1', \n");
	 sql.append(" HXZ,HPJKL,HLC,HLCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.HXZ,0,'0',ROUND(JD.HXZ*100/JD.HABCXZ,2))) || '%' HXZRATE, \n");
	 sql.append(" AXZ,APJKL,ALC,ALCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.AXZ,0,'0',ROUND(JD.AXZ*100/JD.HABCXZ,2))) || '%' AXZRATE, \n");
	 sql.append(" BXZ,BPJKL,BLC,BLCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.BXZ,0,'0',ROUND(JD.BXZ*100/JD.HABCXZ,2))) || '%' BXZRATE, \n");
	 sql.append(" CXZ,CPJKL,CLC,CLCKL, \n");
	 sql.append(" DECODE(JD.HABCXZ,0,'0',DECODE(JD.CXZ,0,'0',ROUND(JD.CXZ*100/JD.HABCXZ,2))) || '%' CXZRATE, \n");
	 
	 sql.append(" OXZ,OPJKL,OLC,OLCKL, \n");
	// sql.append("DECODE(JD.HABCXZ,0,'0',DECODE(JD.OXZ,0,'0',ROUND(JD.OXZ*100/JD.HABCXZ,2))) || '%' OXZRATE,\n"); 
	 sql.append(" EXZ,EPJKL,ELC,ELCKL, \n"); 
	 //sql.append("DECODE(JD.HABCXZ,0,'0',DECODE(JD.EXZ,0,'0',ROUND(JD.EXZ*100/JD.HABCXZ,2))) || '%' EXZRATE, \n");
	 sql.append(" LXZ,LPJKL,LLC,LLCKL, \n"); 
	// sql.append("DECODE(JD.HABCXZ,0,'0',DECODE(JD.LXZ,0,'0',ROUND(JD.LXZ*100/JD.HABCXZ,2))) || '%' LXZRATE, \n");
	
	 sql.append(" HABXZ,HABLC,HABXZLC,HABCXZ,HABCLC,HABCXZLC \n");
	 sql.append(" from ( \n");
	 sql.append(" select  \n");
	 sql.append(" sum(HXZ) HXZ, DECODE(sum(HXZKL),0,'0',DECODE(sum(HXZ),0,'0',ROUND(sum(HXZKL)/sum(HXZ),2))) HPJKL, \n");
	 sql.append(" sum(HLC) HLC, DECODE(sum(HLCKL),0,'0',DECODE(sum(HLC),0,'0',ROUND(sum(HLCKL)/sum(HLC),2))) HLCKL, \n");
	  
	 sql.append(" sum(AXZ) AXZ,DECODE(sum(AXZKL),0,'0',DECODE(sum(AXZ),0,'0',ROUND(sum(AXZKL)/sum(AXZ),2)))  APJKL, \n");
	 sql.append(" sum(ALC) ALC, DECODE(sum(ALCKL),0,'0',DECODE(sum(ALC),0,'0',ROUND(sum(ALCKL)/sum(ALC),2))) ALCKL, \n");


	 sql.append(" sum(BXZ) BXZ,DECODE(sum(BXZKL),0,'0',DECODE(sum(BXZ),0,'0',ROUND(sum(BXZKL)/sum(BXZ),2))) BPJKL, \n");
	 sql.append(" sum(BLC) BLC, DECODE(sum(BLCKL),0,'0',DECODE(sum(BLC),0,'0',ROUND(sum(BLCKL)/sum(BLC),2))) BLCKL, \n");

	 sql.append(" sum(CXZ) CXZ,DECODE(sum(CXZKL),0,'0',DECODE(sum(CXZ),0,'0',ROUND(sum(CXZKL)/sum(CXZ),2))) CPJKL, \n");
	 sql.append(" sum(CLC) CLC, DECODE(sum(CLCKL),0,'0',DECODE(sum(CLC),0,'0',ROUND(sum(CLCKL)/sum(CLC),2))) CLCKL, \n");
	 

	 sql.append(" sum(OXZ) OXZ,DECODE(sum(OXZKL),0,'0',DECODE(sum(OXZ),0,'0',ROUND(sum(OXZKL)/sum(OXZ),2))) OPJKL, \n");
	 sql.append(" sum(OLC) OLC, DECODE(sum(OLCKL),0,'0',DECODE(sum(OLC),0,'0',ROUND(sum(OLCKL)/sum(OLC),2))) OLCKL, \n");
	 
	 sql.append(" sum(EXZ) EXZ,DECODE(sum(EXZKL),0,'0',DECODE(sum(EXZ),0,'0',ROUND(sum(EXZKL)/sum(EXZ),2))) EPJKL, \n");
	 sql.append(" sum(ELC) ELC, DECODE(sum(ELCKL),0,'0',DECODE(sum(ELC),0,'0',ROUND(sum(ELCKL)/sum(ELC),2))) ELCKL, \n");
	 
	 sql.append(" sum(LXZ) LXZ,DECODE(sum(LXZKL),0,'0',DECODE(sum(LXZ),0,'0',ROUND(sum(LXZKL)/sum(LXZ),2))) LPJKL, \n");
	 sql.append(" sum(LLC) LLC, DECODE(sum(LLCKL),0,'0',DECODE(sum(LLC),0,'0',ROUND(sum(LLCKL)/sum(LLC),2))) LLCKL, \n");
	 

	 sql.append(" sum(HXZ+AXZ+BXZ) HABXZ,sum(HLC+ALC+BLC) HABLC, sum(HXZ+AXZ+BXZ+HLC+ALC+BLC) HABXZLC,  \n");
	 sql.append(" sum(HXZ+AXZ+BXZ+CXZ) HABCXZ,sum(HLC+ALC+BLC+CLC) HABCLC, sum(HXZ+AXZ+BXZ+CXZ+HLC+ALC+BLC+CLC) HABCXZLC  \n"); 
	 sql.append(" from ( \n");
	 sql.append(" select  \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,1,sl,0)),0) HXZ, \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,1,kl,0)),0) HXZKL, \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,0,sl,0)),0) HLC, \n");
	 sql.append(" decode(ctm_rank,60101001,(decode(status,0,kl,0)),0) HLCKL, \n");

	 sql.append(" decode(ctm_rank,60101002,(decode(status,1,sl,0)),0) AXZ, \n");
	 sql.append(" decode(ctm_rank,60101002,(decode(status,1,kl,0)),0) AXZKL, \n");
	 sql.append(" decode(ctm_rank,60101002,(decode(status,0,sl,0)),0) ALC, \n");
	 sql.append(" decode(ctm_rank,60101002,(decode(status,0,kl,0)),0) ALCKL, \n");

	 sql.append(" decode(ctm_rank,60101003,(decode(status,1,sl,0)),0) BXZ, \n");
	 sql.append(" decode(ctm_rank,60101003,(decode(status,1,kl,0)),0) BXZKL, \n");
	 sql.append(" decode(ctm_rank,60101003,(decode(status,0,sl,0)),0) BLC, \n");
	 sql.append(" decode(ctm_rank,60101003,(decode(status,0,kl,0)),0) BLCKL, \n");

	 sql.append(" decode(ctm_rank,60101004,(decode(status,1,sl,0)),0) CXZ, \n");
	 sql.append(" decode(ctm_rank,60101004,(decode(status,1,kl,0)),0) CXZKL, \n");
	 sql.append(" decode(ctm_rank,60101004,(decode(status,0,sl,0)),0) CLC, \n");
	 sql.append(" decode(ctm_rank,60101004,(decode(status,0,kl,0)),0) CLCKL, \n");
	 
	 sql.append(" decode(ctm_rank,60101005,(decode(status,1,sl,0)),0) OXZ,\n");
	 sql.append(" decode(ctm_rank,60101005,(decode(status,1,kl,0)),0) OXZKL, \n");
	 sql.append(" decode(ctm_rank,60101005,(decode(status,0,sl,0)),0) OLC, \n");
	 sql.append(" decode(ctm_rank,60101005,(decode(status,0,kl,0)),0) OLCKL, \n");
	 
	 sql.append(" decode(ctm_rank,60101006,(decode(status,1,sl,0)),0) EXZ,\n");
	 sql.append(" decode(ctm_rank,60101006,(decode(status,1,kl,0)),0) EXZKL,\n"); 
	 sql.append(" decode(ctm_rank,60101006,(decode(status,0,sl,0)),0) ELC, \n");
	 sql.append(" decode(ctm_rank,60101006,(decode(status,0,kl,0)),0) ELCKL,\n");
	 
	 sql.append(" decode(ctm_rank,60101007,(decode(status,1,sl,0)),0) LXZ,\n");
	 sql.append(" decode(ctm_rank,60101007,(decode(status,1,kl,0)),0) LXZKL, \n");
	 sql.append(" decode(ctm_rank,60101007,(decode(status,0,sl,0)),0) LLC, \n");
	 sql.append(" decode(ctm_rank,60101007,(decode(status,0,kl,0)),0) LLCKL\n");


	 sql.append(" from ( \n");
	 sql.append(" select tpc.ctm_rank,status,sum(1) sl,sum(KL) KL from tpc where 1=1 group by tpc.ctm_rank,status    \n");
	 sql.append(" order by tpc.ctm_rank,status \n");
	 sql.append(" ) \n");
	 sql.append(" ) jd where 1=1  \n");
	 sql.append(" )JD \n");
	 
	 sql.append("  ) tmp  order by  tmp.a,tmp.ROOT_ORG_NAME, tmp.pq_org_name ,rownum  \n");
	 
	 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
	return list;
}

}

