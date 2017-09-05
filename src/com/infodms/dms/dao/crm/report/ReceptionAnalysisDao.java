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

public class ReceptionAnalysisDao extends BaseDao<PO>{
private static final ReceptionAnalysisDao dao = new ReceptionAnalysisDao();
private ActionContext act = ActionContext.getContext();
RequestWrapper request = act.getRequest();
	public static final ReceptionAnalysisDao getInstance() {
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
	     sql.append(" JD.DEALER_ID,JD.ADVISER,to_number(JD.SCCOUNT) SCCOUNT,to_number(JD.YYCOUNT) YYCOUNT,to_number(JD.KLTOTAL) KLTOTAL, to_number(JD.YJSCOUNT) YJSCOUNT, \n");
	     sql.append(" to_number(JD.DRKLCOUNT) DRKLCOUNT,DECODE(JD.KLCOUNT,0,'0',DECODE(JD.DRKLCOUNT,0,'0',ROUND(JD.DRKLCOUNT*100/JD.KLCOUNT,2))) || '%' DRKLRATE,  \n");
	     sql.append("  to_number(JD.KLCOUNT) KLCOUNT,DECODE(JD.KLCOUNT,0,'0',DECODE(JD.YJSCOUNT,0,'0',ROUND(JD.YJSCOUNT*100/JD.KLCOUNT,2))) || '%' KLRATE, \n");
	     sql.append(" to_number(JD.JKCOUNT) JKCOUNT,DECODE(JD.SCCOUNT,0,'0',DECODE(JD.JKCOUNT,0,'0',ROUND(JD.JKCOUNT*100/JD.SCCOUNT,2))) || '%'  JKRATE, \n");
	     sql.append(" to_number(JD.SJCOUNT) SJCOUNT,DECODE(JD.KLTOTAL,0,'0',DECODE(JD.SJCOUNT,0,'0',ROUND(JD.SJCOUNT*100/JD.KLTOTAL,2))) || '%'  SJRATE, \n");
	     sql.append(" to_number(JD.DDCOUNT) DDCOUNT,DECODE(JD.SCCOUNT,0,'0',DECODE(JD.DDCOUNT,0,'0',ROUND(JD.DDCOUNT*100/JD.SCCOUNT,2))) || '%'  DDRATE, \n");
	     sql.append(" to_number(JD.JCCOUNT) JCCOUNT,DECODE(JD.SCCOUNT,0,'0',DECODE(JD.JCCOUNT,0,'0',ROUND(JD.JCCOUNT*100/JD.SCCOUNT,2))) || '%'  JCRATE, \n");
	     sql.append(" to_number(JD.WJCCOUNT) WJCCOUNT,");
	     sql.append(" TO_NUMBER(JD.ZJCOUNT) ZJCOUNT,DECODE(JD.JCCOUNT,0,'0',DECODE(JD.ZJCOUNT,0,'0',ROUND(JD.ZJCOUNT*100/JD.JCCOUNT,2))) || '%'  ZJRATE, \n");
	     sql.append(" to_number(JD.ZHCOUNT) ZHCOUNT,DECODE(JD.JCCOUNT,0,'0',DECODE(JD.ZHCOUNT,0,'0',ROUND(JD.ZHCOUNT*100/JD.JCCOUNT,2))) || '%'  ZHRATE , \n");
	     sql.append(" to_number(JD.CDCOUNT) CDCOUNT,DECODE(JD.JCCOUNT,0,'0',DECODE(JD.CDCOUNT,0,'0',ROUND(JD.CDCOUNT*100/JD.JCCOUNT,2))) || '%'  CDRATE \n");
	     
	     
	     sql.append("  FROM (  SELECT JD.ADVISER ,TO_CHAR(JD.DEALER_ID) DEALER_ID, SUM(JD.SK) SCCOUNT,SUM(JD.HK) YYCOUNT, SUM(TO_NUMBER(JD.SK))+SUM(TO_NUMBER(JD.HK)) KLTOTAL,SUM(JD.QQ) YJSCOUNT,SUM(JD.DRKL) DRKLCOUNT,SUM(JD.KL) KLCOUNT,  \n");   
	     sql.append(" 	  SUM(JD.JK) JKCOUNT,SUM(JD.DD) DDCOUNT,SUM(JD.JC) JCCOUNT,SUM(JD.WJC) WJCCOUNT,SUM(JD.ZJ) ZJCOUNT,SUM(JD.SJ) SJCOUNT,SUM(JD.ZH) ZHCOUNT,SUM(JD.CD)  CDCOUNT    \n");
	     
	     sql.append("  FROM ( SELECT  to_char(TPLA.ADVISER) ADVISER,to_char(TPLA.DEALER_ID) DEALER_ID,1 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD FROM T_PC_LEADS_ALLOT TPLA,T_PC_LEADS TPL  \n");
	     sql.append("		    LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPL.INTENT_VEHICLE =TPIV.SERIES_ID  \n");
	     sql.append("	    WHERE TPL.LEADS_CODE=TPLA.LEADS_CODE AND TPL.JC_WAY IN('60021001','60021003','60021004','60021008')     \n");
	     sql.append("	      and TPLA.if_confirm = '60321002' AND tpl.LEADS_ORIGIN = '60151011'     \n");
		    if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
		      }
	     	if(!CommonUtils.isNullString(startDate)){
	            sql.append("      		    AND  TPLA.Confirm_Date  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	        if(!CommonUtils.isNullString(endDate)){
	            sql.append("      		  AND  TPLA.Confirm_Date  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	     sql.append("	    UNION  ALL  \n");
	     sql.append("		    SELECT  to_char(TPLA.ADVISER) ADVISER,to_char(TPLA.DEALER_ID) DEALER_ID,0 SK,1 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD FROM T_PC_LEADS_ALLOT TPLA,T_PC_LEADS TPL  \n");
	     sql.append("		    LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPL.INTENT_VEHICLE =TPIV.SERIES_ID  \n");
	     sql.append("		    WHERE TPL.LEADS_CODE=TPLA.LEADS_CODE AND TPL.JC_WAY IN('60021002')   \n");
	     sql.append("	      and TPLA.if_confirm = '60321002' AND tpl.LEADS_ORIGIN = '60151011'     \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPLA.Confirm_Date  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPLA.Confirm_Date  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("	    UNION ALL   \n");
	     sql.append("	    SELECT to_char(TPLA.ADVISER) ADVISER,to_char(TPLA.DEALER_ID) DEALER_ID,0 SK,0 HK,1 QQ,0 DRKL,0 KL,0 JK,0SJ,0DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD FROM T_PC_LEADS_ALLOT TPLA,T_PC_LEADS TPL  \n");
	     sql.append("	    LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPL.INTENT_VEHICLE =TPIV.SERIES_ID  \n");
	     sql.append("	    WHERE TPL.LEADS_CODE=TPLA.LEADS_CODE AND TPL.COME_MEET=10041001  \n");
	     sql.append("	    AND tpl.LEADS_ORIGIN = '60151011'     \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPL.CREATE_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPL.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("	    UNION ALL  \n");
	     sql.append("	    SELECT  to_char(TPLA.ADVISER) ADVISER,to_char(TPLA.DEALER_ID) DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,0 KL,1 JK,0SJ,0DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD FROM T_PC_LEADS_ALLOT TPLA,T_PC_LEADS TPL  \n");
	     sql.append("	    LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPL.INTENT_VEHICLE =TPIV.SERIES_ID  \n");
	     sql.append("	    WHERE TPL.LEADS_CODE=TPLA.LEADS_CODE  AND TPL.JC_WAY IN('60021001','60021003','60021004','60021008') AND (TPL.LEADS_STATUS=60161001 or TPL.LEADS_STATUS=60161005 ) \n");
	     sql.append("	      and TPLA.if_confirm = '60321002' AND tpl.LEADS_ORIGIN = '60151011'     \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")    \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPLA.Confirm_Date  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPLA.Confirm_Date  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     
	     sql.append("	    UNION ALL   \n");
	     sql.append("   SELECT to_char(TPLA.ADVISER) ADVISER,to_char(TPLA.DEALER_ID) DEALER_ID,0 SK,0 HK,0 QQ,1 DRKL,0 KL,0 JK,0SJ,0DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD FROM T_PC_LEADS_ALLOT TPLA,T_PC_LEADS TPL \n");  
	     sql.append("     LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPL.INTENT_VEHICLE =TPIV.SERIES_ID   \n");
	     sql.append("   WHERE TPL.LEADS_CODE=TPLA.LEADS_CODE     \n");
	     sql.append("	      and TPLA.if_confirm = '60321002' AND tpl.LEADS_ORIGIN = '60151011'     AND TO_CHAR(TPL.CREATE_DATE,'YYYY-MM-DD')=TO_CHAR(TPLA.CONFIRM_DATE,'YYYY-MM-DD')    \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPL.CREATE_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND TPL.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     sql.append("	    UNION ALL   \n");
	     sql.append("   SELECT to_char(TPLA.ADVISER) ADVISER,to_char(TPLA.DEALER_ID) DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,1 KL,0 JK,0SJ,0DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD FROM T_PC_LEADS_ALLOT TPLA,T_PC_LEADS TPL \n");  
	     sql.append("     LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPL.INTENT_VEHICLE =TPIV.SERIES_ID   \n");
	     sql.append("   WHERE TPL.LEADS_CODE=TPLA.LEADS_CODE     \n");
	     sql.append("	      AND tpl.LEADS_ORIGIN = '60151011'     \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPL.CREATE_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND TPL.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("   UNION ALL  \n");
	     
	     sql.append(" SELECT to_char(a.ADVISER) ADVISER,to_char(a.DEALER_ID) DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,to_number(sj) SJ,0 DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD FROM  (  \n" );
	     sql.append("		  select  b.adviser,  b.DEALER_ID,to_char(b.sj) sj from (  \n" );
	     sql.append("		       SELECT  tpc.adviser,TPC.DEALER_ID  ,  CASE WHEN  TPC.If_Drive = '10041001' THEN '0' ELSE '1' end SJ ,tpo.TEST_DRIVING FROM T_PC_ORDER TPO,T_PC_CUSTOMER TPC  \n" );  
	     sql.append("		       LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPC.INTENT_VEHICLE =TPIV.SERIES_ID    \n" );
	     sql.append("		      WHERE TPO.CUSTOMER_ID=TPC.CUSTOMER_ID AND   tpo.TEST_DRIVING = 10041001   \n" );
	     sql.append("		       		and tpc.jc_way  IN('60021001','60021002','60021003','60021004','60021008')    \n" );
		     if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
		      }
		  	 if(!CommonUtils.isNullString(startDate)){
		         sql.append("      		    AND  TPC.CREATE_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
		       }
		     if(!CommonUtils.isNullString(endDate)){
		         sql.append("      		  AND TPC.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
		       }
	     sql.append("		      )b where sj=1         \n" );
	     sql.append("		      UNION ALL   \n" );
	     sql.append("	 select tpc.adviser,TPC.DEALER_ID   ,to_char(1) sj  from    T_PC_LEADS tpl , t_pc_leads_allot tla,T_PC_CUSTOMER tpc    \n" );
	     sql.append("	  LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPC.INTENT_VEHICLE =TPIV.SERIES_ID    \n" );
	     sql.append("	  where tpl.leads_code=tla.leads_code  AND TPC.IF_DRIVE=10041001   \n" );
	     sql.append("	  and tpl.customer_id=tpc.customer_id   \n" );
	     sql.append("	  and tla.if_confirm = '60321002' AND tpl.LEADS_ORIGIN = '60151011'   \n" );
	     sql.append("	  and tpl.customer_id=tpc.customer_id    and TPL.JC_WAY IN('60021001','60021002','60021003','60021004','60021008')   \n" );
		     if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
		      }
		  	 if(!CommonUtils.isNullString(startDate)){
		         sql.append("      		    AND  TPL.CREATE_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
		       }
		     if(!CommonUtils.isNullString(endDate)){
		         sql.append("      		  AND TPL.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
		       }
	     sql.append("	      )a  \n" );
	     
	     
	     
	     sql.append("	    UNION ALL  \n");
	     
	     sql.append(" select to_char(adviser) adviser,to_char(dealer_id) dealer_id,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,sum(num) DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD  from ( \n");
	     
	     sql.append(" select tpc.adviser,tpc.dealer_id,tpod.num ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id");  
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
         sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
         
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
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id ");
         
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
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     
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
	     /////////////////////////////
	     
	     
	     
		   //交车数据统计
			     sql.append("	    UNION ALL  \n");
			     sql.append("	    SELECT to_char(adviser) adviser,to_char(dealer_id) dealer_id,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,sum(jcs) JC,0 WJC,0 ZJ,0 ZH,0 CD FROM (  \n");
			 	//交车数据确定车架号的交车数据
			     sql.append("  select tpc.adviser,tpc.dealer_id,delv.jcs jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv, ( \n");
		          sql.append(" select jc.order_detail_id,sum(jc.jcs) jcs from ( select tpd1.order_detail_id,1 jcs from t_pc_delvy tpd1 where 1=1 \n" );
		          
	    	     if(!CommonUtils.isNullString(startDate)){
	    	         sql.append("      		    AND   tpd1.create_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	    	       }
	    	     if(!CommonUtils.isNullString(endDate)){
	    	         sql.append("      		  AND   tpd1.create_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	    	       }
	    	   //确定车架号的退车数据
	          	 sql.append(" union all select tpd2.order_detail_id,-1 jcs from t_pc_delvy tpd2 where tpd2.delivery_status=60571004 \n" );
	          	 
	    	     if(!CommonUtils.isNullString(startDate)){
	    	         sql.append("      		    AND   tpd2.fail_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	    	       }
	    	     if(!CommonUtils.isNullString(endDate)){
	    	         sql.append("      		  AND   tpd2.fail_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	    	       }
	          	sql.append(" ) jc group by jc.order_detail_id  ) delv  \n" );
	          	sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id \n" );
	          	sql.append(" and tpod.order_detail_id=delv.order_detail_id  \n" );
	          	if(!CommonUtils.isNullString(seriesId)){
	 		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
	          	}	
	          //交车数据未确定车架号的交车数据
	          sql.append(" union all  \n");
	          sql.append("  select tpc.adviser,tpc.dealer_id,delv.jcs jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv, ( \n");
		      sql.append(" select jc.order_detail_id,sum(jc.jcs) jcs from ( select tpd1.order_detail_id,1 jcs from t_pc_delvy tpd1 where 1=1 \n" );
		      
	   	     if(!CommonUtils.isNullString(startDate)){
	   	         sql.append("      		    AND   tpd1.create_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	   	       }
	   	     if(!CommonUtils.isNullString(endDate)){
	   	         sql.append("      		  AND   tpd1.create_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	   	       }
	   	     //未确定车架号的退车数据
	         sql.append(" union all select tpd2.order_detail_id,-1 jcs from t_pc_delvy tpd2 where tpd2.delivery_status=60571004 \n" );
	         		  
	   	     if(!CommonUtils.isNullString(startDate)){
	   	         sql.append("      		    AND   tpd2.fail_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	   	       }
	   	     if(!CommonUtils.isNullString(endDate)){
	   	         sql.append("      		  AND   tpd2.fail_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	   	       }
	         	sql.append(" ) jc group by jc.order_detail_id  ) delv  \n" );
	         	sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id \n" );
	         	sql.append(" and tpod.order_detail_id=delv.order_detail_id  \n" );
	        if(!CommonUtils.isNullString(seriesId)){
	    		    	sql.append("     		  AND TPIV.SERIES_ID in ("+seriesId+")  \n" );
	    	    }	
	         //进口车的交车数据
	         sql.append(" union all  \n");
	         sql.append("  select tpc.adviser,tpc.dealer_id,tpod.delivery_number jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv  where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  \n");
	         sql.append(" and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id    and tpod.intent_model in (select series_id from t_pc_intent_vehicle where is_foreign =10041001 )  \n");
	    	 if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
		      }
		     if(!CommonUtils.isNullString(startDate)){
		         sql.append("      		    AND   tpod.create_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
		       }
		     if(!CommonUtils.isNullString(endDate)){
		         sql.append("      		  AND   tpod.create_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
		       }
		     //进口车的退车数据
		     sql.append(" union all  \n");
	         sql.append("  select tpc.adviser,tpc.dealer_id,-tpod.delivery_number jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv  where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  \n");
	         sql.append(" and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id    and tpod.intent_model in (select series_id from t_pc_intent_vehicle where is_foreign =10041001 ) and tpod.task_status=60171003 \n");
	    	 if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
		      }
		     if(!CommonUtils.isNullString(startDate)){
		         sql.append("      		    AND   tpod.orderd_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
		       }
		     if(!CommonUtils.isNullString(endDate)){
		         sql.append("      		  AND   tpod.orderd_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
		       }
	           
		     sql.append(" )group by adviser,dealer_id \n");
		     
		     
		     
		     
		     
		     
		     //未交车数据统计------------------------------------------------
		     //交车数据未确定车架号的交车数据
		     sql.append("  union all \n");
		     sql.append("SELECT to_char(adviser) adviser,to_char(dealer_id) dealer_id,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,0 JC,sum(wjc) WJC,0 ZJ,0 ZH,0 CD FROM (  \n");
		     sql.append("select adviser,dealer_id,num wjc from ( ");
		     sql.append(" select tpc.adviser,tpc.dealer_id,tpod.num ");
		     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv ");
		     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id");  
		     if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
		      }
		  
		     sql.append("  union all \n");
		     sql.append(" select tpc.adviser,tpc.dealer_id,tpod.num ");
	         sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	         sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	         
		     if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.SERIES_ID in ("+seriesId+")  \n" );
		      }

		     sql.append("  union all \n");
		     sql.append(" select tpc.adviser,tpc.dealer_id,0-tpod.num num ");
		     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv ");
		     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id ");
	      
		     if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
		      }
		     
		     sql.append("  union all \n");
		     sql.append(" select tpc.adviser,tpc.dealer_id,0-tpod.num num ");
		     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
		     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
		     
		     if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.SERIES_ID in ("+seriesId+")  \n" );
		      }
		     sql.append(" ) ");
		     
		     //订单数减去交车数
		     sql.append(" union all  \n");
		     sql.append(" select adviser,dealer_id,-jcs wjc from( ");
		     sql.append("  select tpc.adviser,tpc.dealer_id,delv.jcs jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv, ( \n");
	          sql.append(" select jc.order_detail_id,sum(jc.jcs) jcs from ( select tpd1.order_detail_id,1 jcs from t_pc_delvy tpd1 where 1=1 \n" );
	          
		   
		   //确定车架号的退车数据
	      	 sql.append(" union all select tpd2.order_detail_id,-1 jcs from t_pc_delvy tpd2 where tpd2.delivery_status=60571004 \n" );
	      	
	      	sql.append(" ) jc group by jc.order_detail_id  ) delv  \n" );
	      	sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id \n" );
	      	sql.append(" and tpod.order_detail_id=delv.order_detail_id  \n" );
	      	if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
	      	}	
	         sql.append(" union all  \n");
	         sql.append("  select tpc.adviser,tpc.dealer_id,delv.jcs jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv, ( \n");
		      sql.append(" select jc.order_detail_id,sum(jc.jcs) jcs from ( select tpd1.order_detail_id,1 jcs from t_pc_delvy tpd1 where 1=1 \n" );
		    
	  	     //未确定车架号的退车数据
	        sql.append(" union all select tpd2.order_detail_id,-1 jcs from t_pc_delvy tpd2 where tpd2.delivery_status=60571004 \n" );
	        		  
	        	sql.append(" ) jc group by jc.order_detail_id  ) delv  \n" );
	        	sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id \n" );
	        	sql.append(" and tpod.order_detail_id=delv.order_detail_id  \n" );
	       if(!CommonUtils.isNullString(seriesId)){
	   		    	sql.append("     		  AND TPIV.SERIES_ID in ("+seriesId+")  \n" );
	   	    }	
	        //进口车的交车数据
	        sql.append(" union all  \n");
	        sql.append("  select tpc.adviser,tpc.dealer_id,tpod.delivery_number jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv  where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  \n");
	        sql.append(" and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id    and tpod.intent_model in (select series_id from t_pc_intent_vehicle where is_foreign =10041001 )  \n");
	   	 if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
		      }
		     //进口车的退车数据
		     sql.append(" union all  \n");
	        sql.append("  select tpc.adviser,tpc.dealer_id,-tpod.delivery_number jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv  where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  \n");
	        sql.append(" and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id    and tpod.intent_model in (select series_id from t_pc_intent_vehicle where is_foreign =10041001 ) and tpod.task_status=60171003 \n");
	   	 if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
		      }
		     
		     sql.append(" ) "); 
		     sql.append(" )group by adviser,dealer_id \n");
		     

	     /*
	     sql.append("	    UNION ALL  \n");
	     sql.append("	    SELECT to_char(TPC.ADVISER) ADVISER,to_char(TPC.DEALER_ID) DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,1 JC,0 ZJ,0 ZH,0 CD FROM T_PC_DELVY TPD,T_PC_CUSTOMER TPC  \n");
	     sql.append("	    ,vw_material_info vwmi  \n");
	     sql.append("	    WHERE TPD.CUSTOMER_ID=TPC.CUSTOMER_ID  and tpd.material=vwmi.material_id  and tpd.status <> 10011002   \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND vwmi.intent_series in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPD.DELIVERY_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPD.DELIVERY_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     
	     sql.append("	    UNION ALL  \n");
	     sql.append("	    SELECT to_char(TPC.ADVISER) ADVISER,to_char(TPC.DEALER_ID) DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,-1 JC,0 ZJ,0 ZH,0 CD FROM T_PC_DELVY TPD,T_PC_CUSTOMER TPC  \n");
	     sql.append("	    ,vw_material_info vwmi  \n");
	     sql.append("	    WHERE TPD.CUSTOMER_ID=TPC.CUSTOMER_ID  and tpd.material=vwmi.material_id  and tpd.delivery_status = '60571004'   \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND vwmi.intent_series in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPD.Fail_Date  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPD.Fail_Date  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     */
	     
	     
	     sql.append("	     UNION ALL  \n");
	     sql.append("     SELECT  to_char(TPLA.ADVISER) ADVISER,to_char(TPLA.DEALER_ID) DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0SJ,0 DD ,0 JC,0 WJC,1 ZJ,0 ZH,0 CD FROM T_PC_LEADS_ALLOT TPLA,T_PC_LEADS TPL  \n");
	     sql.append("     LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPL.INTENT_VEHICLE =TPIV.SERIES_ID  \n");
	     sql.append("    WHERE TPL.LEADS_CODE=TPLA.LEADS_CODE AND TPL.JC_WAY IN('60021004')  \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPL.CREATE_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPL.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("	    UNION ALL  \n");
	     sql.append("	    SELECT to_char(TPC.ADVISER) ADVISER,to_char(TPC.DEALER_ID) DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,0 JC,0 WJC,0 ZJ,1 ZH,0 CD FROM T_PC_ORDER TPO,T_PC_CUSTOMER TPC  \n");
	     sql.append("	     LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPC.INTENT_VEHICLE =TPIV.SERIES_ID  \n");
	     sql.append("	    WHERE TPO.CUSTOMER_ID=TPC.CUSTOMER_ID  AND TPO.DEAL_TYPE in (select '60051002' code_id from dual union all select code_id from tc_code start with type='60051002' connect by  prior code_id=type )  \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")    \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPo.order_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPo.order_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("      		  AND  TPO.ORDER_STATUS='60231005'  \n");
	     
	     sql.append("	    UNION ALL   \n");
	     sql.append("	    SELECT to_char(TPC.ADVISER) ADVISER,to_char(TPC.DEALER_ID) DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,0 JC,0 WJC,0 ZJ,0 ZH,1 CD FROM T_PC_ORDER TPO,T_PC_CUSTOMER TPC  \n");
	     sql.append("	     LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPC.INTENT_VEHICLE =TPIV.SERIES_ID  \n");
	     sql.append("	    WHERE TPO.CUSTOMER_ID=TPC.CUSTOMER_ID  AND  TPO.DEAL_TYPE IN('600510011001','600510011002','600510011003','600510011004','600510011005','600510011006','60051001')  \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPo.order_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPo.order_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("      		  AND  TPO.ORDER_STATUS='60231005'  \n");
	     
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
		 sql.append("     and (sccount<>0 or yycount<>0 or kltotal<>0 or yjscount<>0 or drklcount<>0 or klcount<>0 or jkcount<>0 or ddcount<>0 or jccount<>0 or wjccount<>0 or sjcount<>0 or zhcount<>0 or cdcount<>0 " +
				"  or  user_status='10011001' )       \n");
		 sql.append("     order by vw.DEALER_ID       \n");
		 sql.append("          )  \n");
		 sql.append("   select   A.ROOT_ORG_NAME,A.PQ_ORG_NAME,A.DEALER_CODE,A.DEALER_SHORTNAME,'2' a,A.name, a.group_name,   \n");
		 sql.append("   A.SCCOUNT,A.YYCOUNT,A.DRKLCOUNT,A.DRKLRATE,A.KLTOTAL, A.YJSCOUNT,  A.KLCOUNT ,A.KLRATE, A.JKCOUNT ,A.JKRATE, A.SJCOUNT   \n");
		 sql.append("    ,A.SJRATE, A.DDCOUNT ,A.DDRATE, A.JCCOUNT ,A.JCRATE, A.WJCCOUNT,A.ZJCOUNT,A.ZJRATE, A.ZHCOUNT ,A.ZHRATE , A.CDCOUNT ,A.CDRATE from a  \n");
		 if(!CommonUtils.isNullString(flag)) {
			 sql.append("  union all   \n");
			 sql.append("  select   A.ROOT_ORG_NAME,A.PQ_ORG_NAME,A.DEALER_CODE,A.DEALER_SHORTNAME,'1' ,max(a.group_name)||'小计', a.group_name ,  \n");
			 sql.append("    sum(A.SCCOUNT), sum(A.YYCOUNT), \n" );
			 sql.append(" sum(A.DRKLCOUNT),DECODE(sum(A.DRKLCOUNT),0,'0',DECODE(sum(A.DRKLCOUNT),0,'0',ROUND(sum(A.DRKLCOUNT)*100/sum(A.KLCOUNT),2))) || '%' DRKLRATE,  \n");
			 sql.append("   sum(A.KLTOTAL), sum(A.YJSCOUNT),   \n");
			 
			 sql.append("   sum(A.KLCOUNT),DECODE(sum(A.KLCOUNT),0,'0',DECODE(sum(A.YJSCOUNT),0,'0',ROUND(sum(A.YJSCOUNT)*100/sum(A.KLCOUNT),2))) || '%' KLRATE,   \n");
			 sql.append("  sum(A.JKCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.JKCOUNT),0,'0',ROUND(sum(A.JKCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  JKRATE,   \n");
			 sql.append("   sum(A.SJCOUNT),DECODE(sum(A.KLTOTAL),0,'0',DECODE(sum(A.SJCOUNT),0,'0',ROUND(sum(A.SJCOUNT)*100/sum(A.KLTOTAL),2))) || '%'  SJRATE,   \n");
			 sql.append("  sum(A.DDCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.DDCOUNT),0,'0',ROUND(sum(A.DDCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  DDRATE,   \n");
			 sql.append("  sum(A.JCCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.JCCOUNT),0,'0',ROUND(sum(A.JCCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  JCRATE,   \n");
			 sql.append("  sum(A.WJCCOUNT), ");
			 sql.append("  sum(A.ZJCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.ZJCOUNT),0,'0',ROUND(sum(A.ZJCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  ZJRATE ,   \n");
			 sql.append("  sum(A.ZHCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.ZHCOUNT),0,'0',ROUND(sum(A.ZHCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  ZHRATE ,   \n");
			 sql.append("  sum(A.CDCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.CDCOUNT),0,'0',ROUND(sum(A.CDCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  CDRAT  \n");
			 sql.append(" from a group by A.ROOT_ORG_NAME,A.PQ_ORG_NAME,A.DEALER_CODE,A.DEALER_SHORTNAME, a.group_name    \n");
			  if(!CommonUtils.isNullString(manager)){
				 sql.append("  union all   \n");
				 sql.append("  select   A.ROOT_ORG_NAME,A.PQ_ORG_NAME,max(A.DEALER_CODE),max(A.DEALER_SHORTNAME),'3'  ,max(a.DEALER_SHORTNAME)||'总计',' ',  \n");
				 sql.append("    sum(A.SCCOUNT), sum(A.YYCOUNT), " );
				 sql.append(" sum(A.DRKLCOUNT),DECODE(sum(A.DRKLCOUNT),0,'0',DECODE(sum(A.DRKLCOUNT),0,'0',ROUND(sum(A.DRKLCOUNT)*100/sum(A.KLCOUNT),2))) || '%' DRKLRATE,  \n");
				 sql.append(" sum(A.KLTOTAL), sum(A.YJSCOUNT),   \n");
				 
				 sql.append("   sum(A.KLCOUNT),DECODE(sum(A.KLCOUNT),0,'0',DECODE(sum(A.YJSCOUNT),0,'0',ROUND(sum(A.YJSCOUNT)*100/sum(A.KLCOUNT),2))) || '%' KLRATE,   \n");
				 sql.append("  sum(A.JKCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.JKCOUNT),0,'0',ROUND(sum(A.JKCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  JKRATE,   \n");
				 sql.append("   sum(A.SJCOUNT),DECODE(sum(A.KLTOTAL),0,'0',DECODE(sum(A.SJCOUNT),0,'0',ROUND(sum(A.SJCOUNT)*100/sum(A.KLTOTAL),2))) || '%'  SJRATE,   \n");
				 sql.append("  sum(A.DDCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.DDCOUNT),0,'0',ROUND(sum(A.DDCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  DDRATE,   \n");
				 sql.append("  sum(A.JCCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.JCCOUNT),0,'0',ROUND(sum(A.JCCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  JCRATE,   \n");
				 sql.append("  sum(A.WJCCOUNT), ");
				 sql.append("  sum(A.ZJCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.ZJCOUNT),0,'0',ROUND(sum(A.ZJCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  ZJRATE ,   \n");
				 sql.append("  sum(A.ZHCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.ZHCOUNT),0,'0',ROUND(sum(A.ZHCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  ZHRATE ,   \n");
				 sql.append("  sum(A.CDCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.CDCOUNT),0,'0',ROUND(sum(A.CDCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  CDRAT  \n");
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
	     sql.append(" JD.DEALER_ID, to_number(JD.SCCOUNT) SCCOUNT,to_number(JD.YYCOUNT) YYCOUNT,to_number(JD.KLTOTAL) KLTOTAL, to_number(JD.YJSCOUNT) YJSCOUNT,  \n");
	     sql.append(" to_number(JD.DRKLCOUNT) DRKLCOUNT,DECODE(JD.KLCOUNT,0,'0',DECODE(JD.DRKLCOUNT,0,'0',ROUND(JD.DRKLCOUNT*100/JD.KLCOUNT,2))) || '%' DRKLRATE,  \n");
	     sql.append("  to_number(JD.KLCOUNT) KLCOUNT,DECODE(JD.KLCOUNT,0,'0',DECODE(JD.YJSCOUNT,0,'0',ROUND(JD.YJSCOUNT*100/JD.KLCOUNT,2))) || '%' KLRATE, \n");
	     sql.append(" to_number(JD.JKCOUNT) JKCOUNT,DECODE(JD.SCCOUNT,0,'0',DECODE(JD.JKCOUNT,0,'0',ROUND(JD.JKCOUNT*100/JD.SCCOUNT,2))) || '%'  JKRATE, \n");
	     sql.append(" to_number(JD.SJCOUNT) SJCOUNT,DECODE(JD.KLTOTAL,0,'0',DECODE(JD.SJCOUNT,0,'0',ROUND(JD.SJCOUNT*100/JD.KLTOTAL,2))) || '%'  SJRATE, \n");
	     sql.append(" to_number(JD.DDCOUNT) DDCOUNT,DECODE(JD.SCCOUNT,0,'0',DECODE(JD.DDCOUNT,0,'0',ROUND(JD.DDCOUNT*100/JD.SCCOUNT,2))) || '%'  DDRATE, \n");
	     sql.append(" to_number(JD.JCCOUNT) JCCOUNT,DECODE(JD.SCCOUNT,0,'0',DECODE(JD.JCCOUNT,0,'0',ROUND(JD.JCCOUNT*100/JD.SCCOUNT,2))) || '%'  JCRATE, \n");
	     sql.append(" to_number(JD.WJCCOUNT) WJCCOUNT, \n");
	     sql.append("   TO_NUMBER(JD.ZJCOUNT) ZJCOUNT,DECODE(JD.JCCOUNT,0,'0',DECODE(JD.ZJCOUNT,0,'0',ROUND(JD.ZJCOUNT*100/JD.JCCOUNT,2))) || '%'  ZJRATE, \n");
	     sql.append(" to_number(JD.ZHCOUNT) ZHCOUNT,DECODE(JD.JCCOUNT,0,'0',DECODE(JD.ZHCOUNT,0,'0',ROUND(JD.ZHCOUNT*100/JD.JCCOUNT,2))) || '%'  ZHRATE , \n");
	     sql.append(" to_number(JD.CDCOUNT) CDCOUNT,DECODE(JD.JCCOUNT,0,'0',DECODE(JD.CDCOUNT,0,'0',ROUND(JD.CDCOUNT*100/JD.JCCOUNT,2))) || '%'  CDRATE \n");
	     
	     //sql.append("  FROM (  SELECT to_char(JD.DEALER_ID) DEALER_ID, SUM(JD.SCCOUNT) SCCOUNT,SUM(JD.YYCOUNT) YYCOUNT, SUM(JD.KLTOTAL) KLTOTAL,SUM(JD.YJSCOUNT) YJSCOUNT,SUM(JD.KLCOUNT) KLCOUNT,   \n");
	     //sql.append("  SUM(JD.JKCOUNT) JKCOUNT,SUM(JD.DDCOUNT) DDCOUNT,SUM(JD.JCCOUNT) JCCOUNT,SUM(JD.SJCOUNT) SJCOUNT,SUM(JD.ZHCOUNT) ZHCOUNT,SUM(JD.CDCOUNT)  CDCOUNT  \n");  
	     sql.append("  FROM (  SELECT TO_CHAR(JD.DEALER_ID) DEALER_ID, SUM(JD.SK) SCCOUNT,SUM(JD.HK) YYCOUNT, SUM(TO_NUMBER(JD.SK))+SUM(TO_NUMBER(JD.HK)) KLTOTAL,SUM(JD.QQ) YJSCOUNT,SUM(JD.DRKL) DRKLCOUNT,SUM(JD.KL) KLCOUNT,  \n");   
	     sql.append(" 	  SUM(JD.JK) JKCOUNT,SUM(JD.DD) DDCOUNT,SUM(JD.JC) JCCOUNT,SUM(JD.WJC) WJCCOUNT,SUM(JD.ZJ) ZJCOUNT,SUM(JD.SJ) SJCOUNT,SUM(JD.ZH) ZHCOUNT,SUM(JD.CD)  CDCOUNT    \n");
	     
	     sql.append("  FROM ( SELECT  TO_NUMBER(TPLA.DEALER_ID) DEALER_ID,1 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD FROM T_PC_LEADS_ALLOT TPLA,T_PC_LEADS TPL  \n");
	     sql.append("		    LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPL.INTENT_VEHICLE =TPIV.SERIES_ID  \n");
	     sql.append("	    WHERE TPL.LEADS_CODE=TPLA.LEADS_CODE AND TPL.JC_WAY IN('60021001','60021003','60021004','60021008')     \n");
	     sql.append("	      and TPLA.if_confirm = '60321002' AND tpl.LEADS_ORIGIN = '60151011'     \n");
		    if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")    \n" );
		      }
	     	if(!CommonUtils.isNullString(startDate)){
	            sql.append("      		    AND  TPLA.Confirm_Date  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	        if(!CommonUtils.isNullString(endDate)){
	            sql.append("      		  AND  TPLA.Confirm_Date  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	          }
	     sql.append("	    UNION  ALL  \n");
	     sql.append("		    SELECT  TO_NUMBER(TPLA.DEALER_ID),0 SK,1 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD FROM T_PC_LEADS_ALLOT TPLA,T_PC_LEADS TPL  \n");
	     sql.append("		    LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPL.INTENT_VEHICLE =TPIV.SERIES_ID  \n");
	     sql.append("		    WHERE TPL.LEADS_CODE=TPLA.LEADS_CODE AND TPL.JC_WAY IN('60021002')   \n");
	     sql.append("	      and TPLA.if_confirm = '60321002' AND tpl.LEADS_ORIGIN = '60151011'     \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID  in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPLA.Confirm_Date  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPLA.Confirm_Date  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("	    UNION ALL   \n");
	     sql.append("	    SELECT TO_NUMBER(TPLA.DEALER_ID),0 SK,0 HK,1 QQ,0 DRKL,0 KL,0 JK,0SJ,0DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD FROM T_PC_LEADS_ALLOT TPLA,T_PC_LEADS TPL  \n");
	     sql.append("	    LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPL.INTENT_VEHICLE =TPIV.SERIES_ID  \n");
	     sql.append("	    WHERE TPL.LEADS_CODE=TPLA.LEADS_CODE AND TPL.COME_MEET=10041001  \n");
	     sql.append("	    AND tpl.LEADS_ORIGIN = '60151011'     \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPL.CREATE_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPL.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("	    UNION ALL  \n");
	     sql.append("	    SELECT  TO_NUMBER(TPLA.DEALER_ID),0 SK,0 HK,0 QQ,0 DRKL,0 KL,1 JK,0SJ,0DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD FROM T_PC_LEADS_ALLOT TPLA,T_PC_LEADS TPL  \n");
	     sql.append("	    LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPL.INTENT_VEHICLE =TPIV.SERIES_ID  \n");
	     sql.append("	    WHERE TPL.LEADS_CODE=TPLA.LEADS_CODE  AND TPL.JC_WAY IN('60021001','60021003','60021004','60021008') AND (TPL.LEADS_STATUS=60161001 or TPL.LEADS_STATUS=60161005 )  \n");
	     sql.append("	      and TPLA.if_confirm = '60321002' AND tpl.LEADS_ORIGIN = '60151011'     \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPLA.Confirm_Date  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPLA.Confirm_Date  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     ///////
	     sql.append("	    UNION ALL   \n");
	     sql.append("   SELECT TO_NUMBER(TPLA.DEALER_ID) DEALER_ID,0 SK,0 HK,0 QQ,1 DRKL,0 KL,0 JK,0SJ,0DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD FROM T_PC_LEADS_ALLOT TPLA,T_PC_LEADS TPL \n");  
	     sql.append("     LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPL.INTENT_VEHICLE =TPIV.SERIES_ID   \n");
	     sql.append("   WHERE TPL.LEADS_CODE=TPLA.LEADS_CODE     \n");
	     sql.append("	      and TPLA.if_confirm = '60321002' AND tpl.LEADS_ORIGIN = '60151011'     AND TO_CHAR(TPL.CREATE_DATE,'YYYY-MM-DD')=TO_CHAR(TPLA.CONFIRM_DATE,'YYYY-MM-DD')    \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPL.CREATE_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND TPL.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     ////////
	     sql.append("	    UNION ALL   \n");
	     sql.append("   SELECT TO_NUMBER(TPLA.DEALER_ID),0 SK,0 HK,0 QQ,0 DRKL,1 KL,0 JK,0 SJ,0 DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD FROM T_PC_LEADS_ALLOT TPLA,T_PC_LEADS TPL \n");  
	     sql.append("     LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPL.INTENT_VEHICLE =TPIV.SERIES_ID   \n");
	     sql.append("   WHERE TPL.LEADS_CODE=TPLA.LEADS_CODE AND TPL.LEADS_ORIGIN  =60151011   \n");
	     sql.append("	AND tpl.LEADS_ORIGIN = '60151011'     \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPL.CREATE_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND TPL.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("   UNION ALL  \n");
	     
	     sql.append(" SELECT a.DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,to_number(sj) SJ,0 DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD FROM  (  \n" );
	     sql.append("		  select  b.DEALER_ID  DEALER_ID,to_char(b.sj) sj from (  \n" );
	     sql.append("		       SELECT  TPC.DEALER_ID ,  CASE WHEN  TPC.If_Drive = '10041001' THEN '0' ELSE '1' end SJ ,tpo.TEST_DRIVING FROM T_PC_ORDER TPO,T_PC_CUSTOMER TPC  \n" );  
	     sql.append("		       LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPC.INTENT_VEHICLE =TPIV.SERIES_ID    \n" );
	     sql.append("		      WHERE TPO.CUSTOMER_ID=TPC.CUSTOMER_ID AND   tpo.TEST_DRIVING = 10041001   \n" );
	     sql.append("		       		and tpc.jc_way  IN('60021001','60021002','60021003','60021004','60021008')    \n" );
		     if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
		      }
		  	 if(!CommonUtils.isNullString(startDate)){
		         sql.append("      		    AND  TPC.CREATE_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
		       }
		     if(!CommonUtils.isNullString(endDate)){
		         sql.append("      		  AND TPC.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
		       }
	     sql.append("		      )b where sj=1         \n" );
	     sql.append("		      UNION ALL   \n" );
	     sql.append("	 select tpc.DEALER_ID  ,to_char(1) sj  from    T_PC_LEADS tpl , t_pc_leads_allot tla,T_PC_CUSTOMER tpc    \n" );
	     sql.append("	  LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPC.INTENT_VEHICLE =TPIV.SERIES_ID    \n" );
	     sql.append("	  where tpl.leads_code=tla.leads_code  AND TPC.IF_DRIVE=10041001   \n" );
	     sql.append("	  and tpl.customer_id=tpc.customer_id   \n" );
	     sql.append("	  and tla.if_confirm = '60321002' AND tpl.LEADS_ORIGIN = '60151011'    \n" );
	     sql.append("	  and tpl.customer_id=tpc.customer_id    and TPL.JC_WAY IN('60021001','60021002','60021003','60021004','60021008')     \n" );
		     if(!CommonUtils.isNullString(seriesId)){
			    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
		      }
		  	 if(!CommonUtils.isNullString(startDate)){
		         sql.append("      		    AND  TPL.CREATE_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
		       }
		     if(!CommonUtils.isNullString(endDate)){
		         sql.append("      		  AND TPL.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
		       }
	     sql.append("	      )a  \n" );
	     
	     
	     sql.append("	    UNION ALL  \n");
	     
	     sql.append(" select dealer_id,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,sum(num) DD ,0 JC,0 WJC,0 ZJ,0 ZH,0 CD from ( \n");
	     sql.append(" select tpc.dealer_id,tpod.num ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id");  
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
         sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
         
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
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id ");
         
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
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     
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
	     
	     
	   //交车数据统计
		     sql.append("	    UNION ALL  \n");
		     sql.append("	    SELECT DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,sum(jcs) JC,0 WJC,0 ZJ,0 ZH,0 CD FROM (  \n");
		 	//交车数据确定车架号的交车数据
		     sql.append("  select tpc.dealer_id,delv.jcs jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv, ( \n");
	          sql.append(" select jc.order_detail_id,sum(jc.jcs) jcs from ( select tpd1.order_detail_id,1 jcs from t_pc_delvy tpd1 where 1=1 \n" );
	          
    	     if(!CommonUtils.isNullString(startDate)){
    	         sql.append("      		    AND   tpd1.create_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
    	       }
    	     if(!CommonUtils.isNullString(endDate)){
    	         sql.append("      		  AND   tpd1.create_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
    	       }
    	   //确定车架号的退车数据
          	 sql.append(" union all select tpd2.order_detail_id,-1 jcs from t_pc_delvy tpd2 where tpd2.delivery_status=60571004 \n" );
          	 
    	     if(!CommonUtils.isNullString(startDate)){
    	         sql.append("      		    AND   tpd2.fail_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
    	       }
    	     if(!CommonUtils.isNullString(endDate)){
    	         sql.append("      		  AND   tpd2.fail_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
    	       }
          	sql.append(" ) jc group by jc.order_detail_id  ) delv  \n" );
          	sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id \n" );
          	sql.append(" and tpod.order_detail_id=delv.order_detail_id  \n" );
          	if(!CommonUtils.isNullString(seriesId)){
 		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
          	}	
          //交车数据未确定车架号的交车数据
          sql.append(" union all  \n");
          sql.append("  select tpc.dealer_id,delv.jcs jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv, ( \n");
	      sql.append(" select jc.order_detail_id,sum(jc.jcs) jcs from ( select tpd1.order_detail_id,1 jcs from t_pc_delvy tpd1 where 1=1 \n" );
	      
   	     if(!CommonUtils.isNullString(startDate)){
   	         sql.append("      		    AND   tpd1.create_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
   	       }
   	     if(!CommonUtils.isNullString(endDate)){
   	         sql.append("      		  AND   tpd1.create_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
   	       }
   	     //未确定车架号的退车数据
         sql.append(" union all select tpd2.order_detail_id,-1 jcs from t_pc_delvy tpd2 where tpd2.delivery_status=60571004 \n" );
         		  
   	     if(!CommonUtils.isNullString(startDate)){
   	         sql.append("      		    AND   tpd2.fail_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
   	       }
   	     if(!CommonUtils.isNullString(endDate)){
   	         sql.append("      		  AND   tpd2.fail_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
   	       }
         	sql.append(" ) jc group by jc.order_detail_id  ) delv  \n" );
         	sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id \n" );
         	sql.append(" and tpod.order_detail_id=delv.order_detail_id  \n" );
        if(!CommonUtils.isNullString(seriesId)){
    		    	sql.append("     		  AND TPIV.SERIES_ID in ("+seriesId+")  \n" );
    	    }	
         //进口车的交车数据
         sql.append(" union all  \n");
         sql.append("  select tpc.dealer_id,tpod.delivery_number jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv  where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  \n");
         sql.append(" and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id    and tpod.intent_model in (select series_id from t_pc_intent_vehicle where is_foreign =10041001 )  \n");
    	 if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND   tpod.create_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND   tpod.create_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     //进口车的退车数据
	     sql.append(" union all  \n");
         sql.append("  select tpc.dealer_id,-tpod.delivery_number jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv  where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') \n");
         sql.append(" and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id    and tpod.intent_model in (select series_id from t_pc_intent_vehicle where is_foreign =10041001 ) and tpod.task_status=60171003 \n");
    	 if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND   tpod.orderd_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND   tpod.orderd_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
           
	     sql.append(" )group by dealer_id \n");
	     
	     
	     
	     //未交车数据统计------------------------------------------------
	     //交车数据未确定车架号的交车数据
	     sql.append("  union all \n");
	     sql.append("SELECT DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,0 JC,sum(wjc) WJC,0 ZJ,0 ZH,0 CD FROM (  \n");
	     sql.append("select dealer_id,num wjc from ( ");
	     sql.append(" select tpc.dealer_id,tpod.num ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id");  
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	  
	     sql.append("  union all \n");
	     sql.append(" select tpc.dealer_id,tpod.num ");
         sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
         sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
         
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.SERIES_ID in ("+seriesId+")  \n" );
	      }

	     sql.append("  union all \n");
	     sql.append(" select tpc.dealer_id,0-tpod.num num ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id ");
      
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sql.append("  union all \n");
	     sql.append(" select tpc.dealer_id,0-tpod.num num ");
	     sql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     sql.append(" ) ");
	     
	     //订单数减去交车数
	     sql.append(" union all  \n");
	     sql.append(" select dealer_id,-jcs wjc from( ");
	     sql.append("  select tpc.dealer_id,delv.jcs jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv, ( \n");
          sql.append(" select jc.order_detail_id,sum(jc.jcs) jcs from ( select tpd1.order_detail_id,1 jcs from t_pc_delvy tpd1 where 1=1 \n" );
          
	   
	   //确定车架号的退车数据
      	 sql.append(" union all select tpd2.order_detail_id,-1 jcs from t_pc_delvy tpd2 where tpd2.delivery_status=60571004 \n" );
      	
      	sql.append(" ) jc group by jc.order_detail_id  ) delv  \n" );
      	sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id \n" );
      	sql.append(" and tpod.order_detail_id=delv.order_detail_id  \n" );
      	if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
      	}	
         sql.append(" union all  \n");
         sql.append("  select tpc.dealer_id,delv.jcs jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv, ( \n");
	      sql.append(" select jc.order_detail_id,sum(jc.jcs) jcs from ( select tpd1.order_detail_id,1 jcs from t_pc_delvy tpd1 where 1=1 \n" );
	    
  	     //未确定车架号的退车数据
        sql.append(" union all select tpd2.order_detail_id,-1 jcs from t_pc_delvy tpd2 where tpd2.delivery_status=60571004 \n" );
        		  
        	sql.append(" ) jc group by jc.order_detail_id  ) delv  \n" );
        	sql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id \n" );
        	sql.append(" and tpod.order_detail_id=delv.order_detail_id  \n" );
       if(!CommonUtils.isNullString(seriesId)){
   		    	sql.append("     		  AND TPIV.SERIES_ID in ("+seriesId+")  \n" );
   	    }	
        //进口车的交车数据
        sql.append(" union all  \n");
        sql.append("  select tpc.dealer_id,tpod.delivery_number jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv  where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  \n");
        sql.append(" and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id    and tpod.intent_model in (select series_id from t_pc_intent_vehicle where is_foreign =10041001 )  \n");
   	 if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     //进口车的退车数据
	     sql.append(" union all  \n");
        sql.append("  select tpc.dealer_id,-tpod.delivery_number jcs from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv  where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011')  \n");
        sql.append(" and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id    and tpod.intent_model in (select series_id from t_pc_intent_vehicle where is_foreign =10041001 ) and tpod.task_status=60171003 \n");
   	 if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sql.append(" ) "); 
	     sql.append(" )group by dealer_id \n");
	     
	     /*
	     sql.append("	    SELECT TPC.DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,1 JC,0 ZJ,0 ZH,0 CD FROM T_PC_DELVY TPD,T_PC_CUSTOMER TPC  \n");
	     sql.append("	   ,vw_material_info vwmi   \n");
	     sql.append("	    WHERE TPD.CUSTOMER_ID=TPC.CUSTOMER_ID and tpd.material=vwmi.material_id and tpd.status <> 10011002   \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		   AND vwmi.intent_series in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPD.DELIVERY_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPD.DELIVERY_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
	     
	     sql.append("	    UNION ALL  \n");
	     sql.append("	    SELECT TPC.DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,-1 JC,0 ZJ,0 ZH,0 CD FROM T_PC_DELVY TPD,T_PC_CUSTOMER TPC  \n");
	     sql.append("	   ,vw_material_info vwmi   \n");
	     sql.append("	    WHERE TPD.CUSTOMER_ID=TPC.CUSTOMER_ID and tpd.material=vwmi.material_id and tpd.delivery_status = '60571004'   \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		   AND vwmi.intent_series in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPD.Fail_Date  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPD.Fail_Date  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     */
	     
	     sql.append("	     UNION ALL  \n");
	     sql.append("     SELECT  TO_NUMBER(TPLA.DEALER_ID),0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,0 JC,0 WJC,1 ZJ,0 ZH,0 CD FROM T_PC_LEADS_ALLOT TPLA,T_PC_LEADS TPL  \n");
	     sql.append("     LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPL.INTENT_VEHICLE =TPIV.SERIES_ID  \n");
	     sql.append("    WHERE TPL.LEADS_CODE=TPLA.LEADS_CODE AND TPL.JC_WAY IN('60021004')  \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")    \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPL.CREATE_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPL.CREATE_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("	    UNION ALL  \n");
	     sql.append("	    SELECT TPC.DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,0 JC,0 WJC,0 ZJ,1 ZH,0 CD FROM T_PC_ORDER TPO,T_PC_CUSTOMER TPC  \n");
	     sql.append("	     LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPC.INTENT_VEHICLE =TPIV.SERIES_ID  \n");
	     sql.append("	    WHERE TPO.CUSTOMER_ID=TPC.CUSTOMER_ID  AND TPO.DEAL_TYPE in (select '60051002' code_id from dual union all select code_id from tc_code start with type='60051002' connect by  prior code_id=type )  \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")    \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  TPo.order_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPo.order_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("      		  AND  TPO.ORDER_STATUS='60231005'  \n");
	     
	     sql.append("	    UNION ALL   \n");
	     sql.append("	    SELECT TPC.DEALER_ID,0 SK,0 HK,0 QQ,0 DRKL,0 KL,0 JK,0 SJ,0 DD ,0 JC,0 WJC,0 ZJ,0 ZH,1 CD FROM T_PC_ORDER TPO,T_PC_CUSTOMER TPC  \n");
	     sql.append("	     LEFT JOIN T_PC_INTENT_VEHICLE TPIV ON TPC.INTENT_VEHICLE =TPIV.SERIES_ID  \n");
	     sql.append("	    WHERE TPO.CUSTOMER_ID=TPC.CUSTOMER_ID  AND  TPO.DEAL_TYPE IN('600510011001','600510011002','600510011003','600510011004','600510011005','600510011006','60051001')  \n");
	     if(!CommonUtils.isNullString(seriesId)){
		    	sql.append("     		  AND TPIV.UP_SERIES_ID in ("+seriesId+")   \n" );
	      }
	  	 if(!CommonUtils.isNullString(startDate)){
	         sql.append("      		    AND  Tpo.order_DATE  >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(!CommonUtils.isNullString(endDate)){
	         sql.append("      		  AND  TPo.order_DATE  <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sql.append("      		     AND TPO.ORDER_STATUS='60231005'  \n");
	     
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
		 sql.append("   A.SCCOUNT,A.YYCOUNT,A.DRKLCOUNT,A.DRKLRATE,A.KLTOTAL, A.YJSCOUNT,  A.KLCOUNT ,A.KLRATE, A.JKCOUNT ,A.JKRATE, A.SJCOUNT   \n");
		 sql.append("    ,A.SJRATE, A.DDCOUNT ,A.DDRATE, A.JCCOUNT ,A.JCRATE, A.WJCCOUNT , A.ZJCOUNT ,A.ZJRATE , A.ZHCOUNT ,A.ZHRATE , A.CDCOUNT ,A.CDRATE from a  \n");
		 if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)|| Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||   Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("  union all   \n");
			 sql.append("  select   '',A.ROOT_ORG_NAME,A.PQ_ORG_NAME,' ',max(A.PQ_ORG_NAME)||'小计','3' ,   \n");
			 sql.append("    sum(A.SCCOUNT), sum(A.YYCOUNT), \n" );
			 sql.append(" sum(A.DRKLCOUNT),DECODE(sum(A.DRKLCOUNT),0,'0',DECODE(sum(A.DRKLCOUNT),0,'0',ROUND(sum(A.DRKLCOUNT)*100/sum(A.KLCOUNT),2))) || '%' DRKLRATE,  \n");
			 sql.append(" sum(A.KLTOTAL), sum(A.YJSCOUNT),   \n");
			 
			 sql.append("   sum(A.KLCOUNT),DECODE(sum(A.KLCOUNT),0,'0',DECODE(sum(A.YJSCOUNT),0,'0',ROUND(sum(A.YJSCOUNT)*100/sum(A.KLCOUNT),2))) || '%' KLRATE,   \n");
			 sql.append("  sum(A.JKCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.JKCOUNT),0,'0',ROUND(sum(A.JKCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  JKRATE,   \n");
			 sql.append("   sum(A.SJCOUNT),DECODE(sum(A.KLTOTAL),0,'0',DECODE(sum(A.SJCOUNT),0,'0',ROUND(sum(A.SJCOUNT)*100/sum(A.KLTOTAL),2))) || '%'  SJRATE,   \n");
			 sql.append("  sum(A.DDCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.DDCOUNT),0,'0',ROUND(sum(A.DDCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  DDRATE,   \n");
			 sql.append("  sum(A.JCCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.JCCOUNT),0,'0',ROUND(sum(A.JCCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  JCRATE,   \n");
			 sql.append("  sum(A.WJCCOUNT), ");
			 sql.append("  sum(A.ZJCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.ZJCOUNT),0,'0',ROUND(sum(A.ZJCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  ZJRATE ,   \n");
			 sql.append("  sum(A.ZHCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.ZHCOUNT),0,'0',ROUND(sum(A.ZHCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  ZHRATE ,   \n");
			 sql.append("  sum(A.CDCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.CDCOUNT),0,'0',ROUND(sum(A.CDCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  CDRAT  \n");
			 sql.append(" from a  group by A.ROOT_ORG_NAME,A.PQ_ORG_NAME    \n");
		 }  if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||   Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
				 sql.append("  union all   \n");
				 sql.append("   select   '',A.ROOT_ORG_NAME,' ',' ',max(a.ROOT_ORG_NAME)||'合计','2'  ,  \n");
				 
				 sql.append("    sum(A.SCCOUNT), sum(A.YYCOUNT), \n" );
				 sql.append(" sum(A.DRKLCOUNT),DECODE(sum(A.DRKLCOUNT),0,'0',DECODE(sum(A.DRKLCOUNT),0,'0',ROUND(sum(A.DRKLCOUNT)*100/sum(A.KLCOUNT),2))) || '%' DRKLRATE,  \n");
				 sql.append(" sum(A.KLTOTAL), sum(A.YJSCOUNT),   \n");
				  
				 sql.append("   sum(A.KLCOUNT),DECODE(sum(A.KLCOUNT),0,'0',DECODE(sum(A.YJSCOUNT),0,'0',ROUND(sum(A.YJSCOUNT)*100/sum(A.KLCOUNT),2))) || '%' KLRATE,   \n");
				 sql.append("  sum(A.JKCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.JKCOUNT),0,'0',ROUND(sum(A.JKCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  JKRATE,   \n");
				 sql.append("   sum(A.SJCOUNT),DECODE(sum(A.KLTOTAL),0,'0',DECODE(sum(A.SJCOUNT),0,'0',ROUND(sum(A.SJCOUNT)*100/sum(A.KLTOTAL),2))) || '%'  SJRATE,   \n");
				 sql.append("  sum(A.DDCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.DDCOUNT),0,'0',ROUND(sum(A.DDCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  DDRATE,   \n");
				 sql.append("  sum(A.JCCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.JCCOUNT),0,'0',ROUND(sum(A.JCCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  JCRATE,   \n");
				 sql.append("  sum(A.WJCCOUNT), ");
				 sql.append("  sum(A.ZJCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.ZJCOUNT),0,'0',ROUND(sum(A.ZJCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  ZJRATE ,   \n");
				 sql.append("  sum(A.ZHCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.ZHCOUNT),0,'0',ROUND(sum(A.ZHCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  ZHRATE ,   \n");
				 sql.append("  sum(A.CDCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.CDCOUNT),0,'0',ROUND(sum(A.CDCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  CDRAT  \n");
				 sql.append(" from a group by A.ROOT_ORG_NAME   \n");
		   }
		   if(  Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("  union all   \n");
			 sql.append("  select '',  ' ',' ' , ' ','全国合计','1' ,   \n");
			 
			 sql.append("    sum(A.SCCOUNT), sum(A.YYCOUNT), \n" );
			 sql.append(" sum(A.DRKLCOUNT),DECODE(sum(A.DRKLCOUNT),0,'0',DECODE(sum(A.DRKLCOUNT),0,'0',ROUND(sum(A.DRKLCOUNT)*100/sum(A.KLCOUNT),2))) || '%' DRKLRATE,  \n");
			 sql.append(" sum(A.KLTOTAL), sum(A.YJSCOUNT),   \n");
			 
			 sql.append("   sum(A.KLCOUNT),DECODE(sum(A.KLCOUNT),0,'0',DECODE(sum(A.YJSCOUNT),0,'0',ROUND(sum(A.YJSCOUNT)*100/sum(A.KLCOUNT),2))) || '%' KLRATE,   \n");
			 sql.append("  sum(A.JKCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.JKCOUNT),0,'0',ROUND(sum(A.JKCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  JKRATE,   \n");
			 sql.append("   sum(A.SJCOUNT),DECODE(sum(A.KLTOTAL),0,'0',DECODE(sum(A.SJCOUNT),0,'0',ROUND(sum(A.SJCOUNT)*100/sum(A.KLTOTAL),2))) || '%'  SJRATE,   \n");
			 sql.append("  sum(A.DDCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.DDCOUNT),0,'0',ROUND(sum(A.DDCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  DDRATE,   \n");
			 sql.append("  sum(A.JCCOUNT),DECODE(sum(A.SCCOUNT),0,'0',DECODE(sum(A.JCCOUNT),0,'0',ROUND(sum(A.JCCOUNT)*100/sum(A.SCCOUNT),2))) || '%'  JCRATE,   \n");
			 sql.append("  sum(A.WJCCOUNT), ");
			 sql.append("  sum(A.ZJCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.ZJCOUNT),0,'0',ROUND(sum(A.ZJCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  ZJRATE ,   \n");
			 sql.append("  sum(A.ZHCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.ZHCOUNT),0,'0',ROUND(sum(A.ZHCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  ZHRATE ,   \n");
			 sql.append("  sum(A.CDCOUNT),DECODE(sum(A.JCCOUNT),0,'0',DECODE(sum(A.CDCOUNT),0,'0',ROUND(sum(A.CDCOUNT)*100/sum(A.JCCOUNT),2))) || '%'  CDRAT  \n");
			 sql.append(" from a   \n");
	 }
		 sql.append("  ) tmp  order by  tmp.a,tmp.ROOT_ORG_NAME, tmp.pq_org_name ,rownum  \n");
		 
		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
 
}
