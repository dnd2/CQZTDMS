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

public class LiuShopStatisticsDao extends BaseDao<PO>{
private static final LiuShopStatisticsDao dao = new LiuShopStatisticsDao();
private ActionContext act = ActionContext.getContext();
RequestWrapper request = act.getRequest();
	public static final LiuShopStatisticsDao getInstance() {
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
		String orgId=map.get("orgId");
		String userId=map.get("userId");
		String manager=map.get("manager");
		String flag=map.get("flag");
		String seriesId=map.get("seriesId");
		StringBuilder sql= new StringBuilder();
		sql.append("select group_name, name,temp.ROOT_ORG_NAME, temp.PQ_ORG_NAME, temp.DEALER_CODE, temp.DEALER_SHORTNAME, sk SCCOUNT,\n" );
		sql.append("       yy yykl, mn15 ld15,DECODE(to_number(sk),0,'0',ROUND( mn15 *100/to_number(sk),2))||'%'  rate15 ,\n" );
		sql.append("       mn30 ld30,   DECODE(to_number(sk),0,'0',ROUND(mn30 *100/to_number(sk),2))||'%'  rate30,\n" );
		sql.append("       mn35 ld35, DECODE(to_number(sk),0,'0',ROUND(mn35 *100/to_number(sk),2))||'%' rate35, " );
		sql.append("  (mn30jk + mn15jk + mn35jk) jkcount,\n" );
		sql.append("       mn15jk jk15,  mn30jk jk30, mn35jk jk35,\n" );
		sql.append("       DECODE(to_number(sk),0,'0',ROUND(mn15jk *100/to_number(sk),2))||'%' BuildRate15,\n" );
		sql.append("       DECODE(to_number(sk),0,'0',ROUND(mn30jk *100/to_number(sk),2))||'%' BuildRate30,\n" );
		sql.append("         DECODE(to_number(sk),0,'0',ROUND(mn35jk *100/to_number(sk),2))||'%' BuildRate35,\n" );
		sql.append("      DECODE((mn15+mn30+mn35),0,' ',round((mn30jk + mn15jk + mn35jk)/(mn15+mn30+mn35)*100,2))||'%' BuildRate \n" );
		sql.append("  from (with mingxi as (select jk.DEALER_ID,jk.ADVISER, tpg.group_id,tpg.group_name, tu.name, sum(sk) sk, sum(yy) yy, sum(mn15) mn15,\n" );
		sql.append("                               sum(mn30) mn30, sum(mn35) mn35, sum(mn15jk) mn15jk,\n" );
		sql.append("                               sum(mn30jk) mn30jk, sum(mn35jk) mn35jk \n" );
		sql.append("                          from (select DEALER_ID,  ADVISER, LEAVE_DATE,\n" );
		sql.append("                                       COME_DATE, LEADS_STATUS, JC_WAY,\n" );
		sql.append("                                       decode(JC_WAY, 60021002, 0, 1) sk,\n" );
		sql.append("                                       decode(JC_WAY, 60021002, 1, 0) yy,\n" );
		sql.append("                                       if_confirm,\n" );
		sql.append("                                       (case  when mn < 0  then 0  else mn END) mn, \n" );
		sql.append("                                       (case  when mn <= 15 and jc_way in('60021001','60021003','60021004','60021008') then  1 else  0 END) mn15,\n" );
		sql.append("                                       (case  when mn > 15 and mn <= 30 and jc_way in('60021001','60021003','60021004','60021008') then 1 else 0 END) mn30,\n" );
		sql.append("                                       (case  when mn > 30 and jc_way in('60021001','60021003','60021004','60021008') then 1  else  0 END) mn35,\n" );
		sql.append("                                       (case  when mn <= 15 and  if_confirm = 60321002 and LEADS_STATUS = 60161001 and jc_way in('60021001','60021003','60021004','60021008') then\n" );
		sql.append("                                          1 else  0 END) mn15jk,\n" );
		sql.append("                                       (case when mn > 15 and mn <= 30 and\n" );
		sql.append("                                              if_confirm = 60321002 and LEADS_STATUS = 60161001 and jc_way in('60021001','60021003','60021004','60021008') then\n" );
		sql.append("                                          1 else 0  END) mn30jk,\n" );
		sql.append("                                       (case when mn > 30 and if_confirm = 60321002  and jc_way in('60021001','60021003','60021004','60021008') and\n" );
		sql.append("                                              LEADS_STATUS = 60161001 then  1\n" );
		sql.append("                                         else 0 END) mn35jk\n" );
		sql.append("                                  from (SELECT B.DEALER_ID,  B.ADVISER,\n" );
		sql.append("                                               TL.LEAVE_DATE, TL.COME_DATE,\n" );
		sql.append("                                               TL.LEADS_STATUS, TL.JC_WAY, b.if_confirm,\n" );
		sql.append("                                               decode(TL.COME_DATe, null,\n" );
		sql.append("                                                      0, CEIL((decode(TL.LEAVE_DATE, null,\n" );
		sql.append("                                                                   sysdate, TL.LEAVE_DATE) -\n" );
		sql.append("                                                           TL.COME_DATE) * 24 * 60)) mn\n" );
		sql.append("                                          FROM T_PC_LEADS          TL, T_PC_LEADS_ALLOT    B,\n" );
		sql.append("                                               T_PC_INTENT_VEHICLE tpiv\n" );
		sql.append("                                         WHERE TL.LEADS_CODE = B.LEADS_CODE   AND Tl.LEADS_ORIGIN = '60151011'\n" );
		sql.append("                                           AND TL.INTENT_VEHICLE = TPIV.SERIES_ID(+)\n" );
		if(!CommonUtils.isNullString(userId)){
			sql.append(" and b.adviser in("+userId+")");
		}
		if(!CommonUtils.isNullString(seriesId)){
	    	      sql.append("     		  AND TPIV.Up_Series_Id in ("+seriesId+")   \n" );
	    }
	   if(!"".equals(dealerCode)&&dealerCode!=null){
			  String arr[]=dealerCode.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));
	            
			sql.append(" AND (b.DEALER_ID ="+dealerId+" " +
			"or b.DEALER_ID in(select dealer_ids from t_pc_company_group where par_dealer_id="+dealerId+" "+
            "and DEALER_CODES IN("+str+"))) \n" );
		}
		if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType) && ("".equals(dealerCode)||dealerCode==null)){
			
			sql.append("  AND (b.DEALER_ID ="+dealerId+")\n");
			//大区
		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND b.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
			//小区
		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND b.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.PQ_ORG_ID="+orgId+") \n");
		}
		if(!CommonUtils.isNullString(startDate)){
            sql.append("     		  AND tl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
          }
        if(!CommonUtils.isNullString(endDate)){
            sql.append("     		  AND tl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
          }
		  if(!CommonUtils.isNullString(seriesId)){
		    	 sql.append("     		  AND TPIV.up_SERIES_ID in ("+seriesId+")   \n" );
		     }
		 
		sql.append("   AND (TL.JC_WAY in ('60021001','60021002', '60021003','60021004','60021008')) and b.if_confirm = '60321002'\n" );
		sql.append("      ) a)jk  ,tc_user tu,t_pc_group tpg  where JK.ADVISER =tu.user_id  and  tu.group_id=tpg.group_id  \n" );
		sql.append("          group by jk.DEALER_ID ,jk.ADVISER,tpg.group_id,tpg.group_name, tu.name )\n" );
		sql.append("         select '2' sort,\n" );
		sql.append("                vod.ROOT_ORG_NAME,  vod.PQ_ORG_NAME, vod.DEALER_CODE,group_name,name, \n" );
		sql.append("                to_char(vod.DEALER_SHORTNAME) DEALER_SHORTNAME,\n" );
		sql.append("                sk,\n" );
		sql.append("                yy,\n" );
		sql.append("                sk + yy hh,\n" );
		sql.append("                mn15,\n" );
		sql.append("                mn30,\n" );
		sql.append("                mn35, mn15jk,\n" );
		sql.append("                mn30jk,\n" );
		sql.append("                mn35jk \n" );
		sql.append("           from mingxi, vw_org_dealer_all vod\n" );
		sql.append("          where vod.DEALER_ID = mingxi.dealer_id \n" );
		 if(!"".equals(dealerCode)&&dealerCode!=null){
	            String arr[]=dealerCode.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND vod.DEALER_CODE IN("+str+")\n");
			}
		sql.append("\n" ); 
		 if(!CommonUtils.isNullString(flag)) {
			 sql.append("         union all \n" );
				sql.append("         select '1',ROOT_ORG_NAME,  PQ_ORG_NAME,  DEALER_CODE,group_name,group_name||'小计',DEALER_SHORTNAME, \n" );
				sql.append("                sk,\n" );
				sql.append("                yy,\n" );
				sql.append("                sk + yy hh,\n" );
				sql.append("                mn15,\n" );
				sql.append("                mn30,\n" );
				sql.append("                mn35, mn15jk,\n" );
				sql.append("                mn30jk,\n" );
				sql.append("                mn35jk  \n" );
				sql.append("           from (select  vod.ROOT_ORG_NAME,  vod.PQ_ORG_NAME, vod.DEALER_CODE,group_name,vod.DEALER_SHORTNAME, \n" );
				sql.append("                        sum(sk) sk,\n" );
				sql.append("                        sum(yy) yy,\n" );
				sql.append("                        sum(mn15) mn15,\n" );
				sql.append("                        sum(mn30) mn30,\n" );
				sql.append("                        sum(mn35) mn35,\n" );
				sql.append("                        sum(mn15jk) mn15jk,\n" );
				sql.append("                        sum(mn30jk) mn30jk,\n" );
				sql.append("                        sum(mn35jk) mn35jk\n" );
				sql.append("                   from mingxi, VW_ORG_DEALER_ALL vod\n" );
				sql.append("                  where mingxi.DEALER_ID = vod.dealer_id\n" );
				 if(!"".equals(dealerCode)&&dealerCode!=null){
			            String arr[]=dealerCode.split(",");
			            String str="";

			            for(int i=0;i<arr.length;i++){
			                str+="'"+arr[i]+"',";
			            }
			            str=str.substring(0,str.lastIndexOf(","));

						sql.append("AND vod.DEALER_CODE IN("+str+")\n");
					}
				sql.append("                  group by vod.ROOT_ORG_NAME,vod.PQ_ORG_NAME,vod.DEALER_CODE,group_name,vod.DEALER_SHORTNAME )\n" );
			 
				if(!CommonUtils.isNullString(manager)){
			 sql.append("         union all\n" );
				sql.append("         select '3',ROOT_ORG_NAME, PQ_ORG_NAME,  ' ', ' ',  '总计','', \n" );
				sql.append("                sk,\n" );
				sql.append("                yy,\n" );
				sql.append("                sk + yy hh,\n" );
				sql.append("                mn15,\n" );
				sql.append("                mn30,\n" );
				sql.append("                mn35, mn15jk,\n" );
				sql.append("                mn30jk,\n" );
				sql.append("                mn35jk  \n" );
				sql.append("           from (select dealer.ROOT_ORG_NAME,dealer.PQ_ORG_NAME, \n" );
				sql.append("                        sum(sk) sk,\n" );
				sql.append("                        sum(yy) yy,\n" );
				sql.append("                        sum(mn15) mn15,\n" );
				sql.append("                        sum(mn30) mn30,\n" );
				sql.append("                        sum(mn35) mn35,\n" );
				sql.append("                        sum(mn15jk) mn15jk,\n" );
				sql.append("                        sum(mn30jk) mn30jk,\n" );
				sql.append("                        sum(mn35jk) mn35jk \n" );
				sql.append("                   from mingxi, VW_ORG_DEALER_ALL dealer \n" );
				sql.append("                  where mingxi.DEALER_ID = dealer.dealer_id\n" );
				 if(!"".equals(dealerCode)&&dealerCode!=null){
			            String arr[]=dealerCode.split(",");
			            String str="";

			            for(int i=0;i<arr.length;i++){
			                str+="'"+arr[i]+"',";
			            }
			            str=str.substring(0,str.lastIndexOf(","));

						sql.append("AND dealer.DEALER_CODE IN("+str+")\n");
					}
				sql.append("                  group by dealer.root_org_name ,dealer.PQ_ORG_NAME )\n" );
				}  
		 }
		 sql.append("                 ) temp\n" );
		 sql.append("          order by temp.sort, temp.ROOT_ORG_NAME,temp.PQ_ORG_NAME,temp.DEALER_CODE ");
		 
		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	public List<Map<String, Object>> getStatisticsSelectAll(Map<String, String> map) throws UnsupportedEncodingException{
		String startDate=map.get("startDate");
		String endDate=map.get("endDate");
		String dealerCode=map.get("dealerCode");
		String dealerId=map.get("dealerId");
		String dutyType=map.get("dutyType");
		String orgId=map.get("orgId");
		String seriesId=map.get("seriesId");
		StringBuilder sql= new StringBuilder();
		sql.append("select temp.DEALER_ID,temp.ROOT_ORG_NAME, temp.PQ_ORG_NAME, temp.DEALER_CODE, temp.DEALER_SHORTNAME, sk SCCOUNT,\n" );
		sql.append("       yy yykl, mn15 ld15,DECODE(to_number(sk),0,'0',ROUND( mn15 *100/to_number(sk),2))||'%'  rate15 ,\n" );
		sql.append("       mn30 ld30,   DECODE(to_number(sk),0,'0',ROUND(mn30 *100/to_number(sk),2))||'%'  rate30,\n" );
		sql.append("       mn35 ld35, DECODE(to_number(sk),0,'0',ROUND(mn35 *100/to_number(sk),2))||'%' rate35, " );
		sql.append("  (mn30jk + mn15jk + mn35jk) jkcount,\n" );
		sql.append("       mn15jk jk15,  mn30jk jk30, mn35jk jk35,\n" );
		sql.append("       DECODE(to_number(sk),0,'0',ROUND(mn15jk *100/to_number(sk),2))||'%' BuildRate15,\n" );
		sql.append("       DECODE(to_number(sk),0,'0',ROUND(mn30jk *100/to_number(sk),2))||'%' BuildRate30,\n" );
		sql.append("         DECODE(to_number(sk),0,'0',ROUND(mn35jk *100/to_number(sk),2))||'%' BuildRate35,\n" );
		sql.append("      decode(to_number(sk),0,'0',round((mn30jk + mn15jk + mn35jk)/sk*100,2))||'%' BuildRate \n" );
		sql.append("  from (with mingxi as (select DEALER_ID, sum(sk) sk, sum(yy) yy, sum(mn15) mn15,\n" );
		sql.append("                               sum(mn30) mn30, sum(mn35) mn35, sum(mn15jk) mn15jk,\n" );
		sql.append("                               sum(mn30jk) mn30jk, sum(mn35jk) mn35jk \n" );
		sql.append("                          from (select DEALER_ID,  ADVISER, LEAVE_DATE,\n" );
		sql.append("                                       COME_DATE, LEADS_STATUS, JC_WAY,\n" );
		sql.append("                                       decode(JC_WAY, 60021002, 0, 1) sk,\n" );
		sql.append("                                       decode(JC_WAY, 60021002, 1, 0) yy,\n" );
		sql.append("                                       if_confirm,\n" );
		sql.append("                                       (case  when mn < 0 then 0  else mn END) mn, \n" );
		sql.append("                                       (case  when mn <= 15 and jc_way in('60021001','60021003','60021004','60021008') then  1 else  0 END) mn15,\n" );
		sql.append("                                       (case  when mn > 15 and jc_way in('60021001','60021003','60021004','60021008') and mn <= 30 then 1 else 0 END) mn30,\n" );
		sql.append("                                       (case  when mn > 30 and jc_way in('60021001','60021003','60021004','60021008') then 1  else  0 END) mn35,\n" );
		sql.append("                                       (case  when mn <= 15 and  if_confirm = 60321002 and LEADS_STATUS = 60161001 and jc_way in('60021001','60021003','60021004','60021008') then\n" );
		sql.append("                                          1 else  0 END) mn15jk,\n" );
		sql.append("                                       (case when mn > 15 and mn <= 30 and\n" );
		sql.append("                                              if_confirm = 60321002 and LEADS_STATUS = 60161001 and jc_way in('60021001','60021003','60021004','60021008') then\n" );
		sql.append("                                          1 else 0  END) mn30jk,\n" );
		sql.append("                                       (case when mn > 30 and if_confirm = 60321002 and jc_way in('60021001','60021003','60021004','60021008') and\n" );
		sql.append("                                              LEADS_STATUS = 60161001 then  1\n" );
		sql.append("                                         else 0 END) mn35jk\n" );
		sql.append("                                  from (SELECT B.DEALER_ID,  B.ADVISER,\n" );
		sql.append("                                               TL.LEAVE_DATE, TL.COME_DATE,\n" );
		sql.append("                                               TL.LEADS_STATUS, TL.JC_WAY, b.if_confirm,\n" );
		sql.append("                                               decode(TL.COME_DATe, null,\n" );
		sql.append("                                                      0, CEIL((decode(TL.LEAVE_DATE, null,\n" );
		sql.append("                                                                   sysdate, TL.LEAVE_DATE) -\n" );
		sql.append("                                                           TL.COME_DATE) * 24 * 60)) mn\n" );
		sql.append("                                          FROM T_PC_LEADS          TL, T_PC_LEADS_ALLOT    B,\n" );
		sql.append("                                               T_PC_INTENT_VEHICLE tpiv\n" );
		sql.append("                                         WHERE TL.LEADS_CODE = B.LEADS_CODE AND Tl.LEADS_ORIGIN = '60151011'\n" );
		sql.append("                                           AND TL.INTENT_VEHICLE = TPIV.SERIES_ID(+) \n" );
		if(!CommonUtils.isNullString(seriesId)){
	    	      sql.append("     		  AND TPIV.Up_Series_Id in ("+seriesId+")   \n" );
	    }
		if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
			sql.append("  AND (b.DEALER_ID ="+dealerId+")\n");
			//大区
		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND b.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
			//小区
		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND b.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL VW WHERE  VW.PQ_ORG_ID="+orgId+") \n");
		}
		if(!CommonUtils.isNullString(startDate)){
            sql.append("     		  AND tl.create_date >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n" );
          }
        if(!CommonUtils.isNullString(endDate)){
            sql.append("     		  AND tl.create_date <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n" );
          }
		  if(!CommonUtils.isNullString(seriesId)){
		    	 sql.append("     		  AND TPIV.up_SERIES_ID in ("+seriesId+")   \n" );
		     }
		 
		sql.append("   AND (TL.JC_WAY in ('60021001','60021002', '60021003','60021004','60021008')) and b.if_confirm = '60321002'\n" );
		sql.append("      ) a)\n" );
		sql.append("          group by DEALER_ID)\n" );
		sql.append("         select TO_CHAR(MINGXI.DEALER_ID) DEALER_ID,'3' sort,\n" );
		sql.append("                vod.ROOT_ORG_NAME,\n" );
		sql.append("                vod.PQ_ORG_NAME,\n" );
		sql.append("                vod.DEALER_CODE,\n" );
		sql.append("                to_char(vod.DEALER_SHORTNAME) DEALER_SHORTNAME,\n" );
		sql.append("                sk,\n" );
		sql.append("                yy,\n" );
		sql.append("                sk + yy hh,\n" );
		sql.append("                mn15,\n" );
		sql.append("                mn30,\n" );
		sql.append("                mn35, mn15jk,\n" );
		sql.append("                mn30jk,\n" );
		sql.append("                mn35jk \n" );
		sql.append("           from mingxi, vw_org_dealer_all vod \n" );
		sql.append("          where vod.DEALER_ID = mingxi.dealer_id  \n" );
		 if(!"".equals(dealerCode)&&dealerCode!=null){
	            String arr[]=dealerCode.split(",");
	            String str="";

	            for(int i=0;i<arr.length;i++){
	                str+="'"+arr[i]+"',";
	            }
	            str=str.substring(0,str.lastIndexOf(","));

				sql.append("AND vod.DEALER_CODE IN("+str+")\n");
			}
		sql.append("\n" ); 
		 if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType) ||Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||  Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType) ){
			 sql.append("         union all\n" );
				sql.append("         --小区\n" );
				sql.append("         select '','3',\n" );
				sql.append("                ROOT_ORG_NAME,\n" );
				sql.append("                PQ_ORG_NAME,\n" );
				sql.append("                '',\n" );
				sql.append("                PQ_ORG_NAME||'小计',\n" );
				sql.append("                sk,\n" );
				sql.append("                yy,\n" );
				sql.append("                sk + yy hh,\n" );
				sql.append("                mn15,\n" );
				sql.append("                mn30,\n" );
				sql.append("                mn35, mn15jk,\n" );
				sql.append("                mn30jk,\n" );
				sql.append("                mn35jk  \n" );
				sql.append("           from (select vod.ROOT_ORG_NAME,vod.PQ_ORG_NAME,\n" );
				sql.append("                        sum(sk) sk,\n" );
				sql.append("                        sum(yy) yy,\n" );
				sql.append("                        sum(mn15) mn15,\n" );
				sql.append("                        sum(mn30) mn30,\n" );
				sql.append("                        sum(mn35) mn35,\n" );
				sql.append("                        sum(mn15jk) mn15jk,\n" );
				sql.append("                        sum(mn30jk) mn30jk,\n" );
				sql.append("                        sum(mn35jk) mn35jk\n" );
				sql.append("                   from mingxi, VW_ORG_DEALER_ALL vod\n" );
				sql.append("                  where mingxi.DEALER_ID = vod.dealer_id\n" );
					 if(!"".equals(dealerCode)&&dealerCode!=null){
				            String arr[]=dealerCode.split(",");
				            String str="";
	
				            for(int i=0;i<arr.length;i++){
				                str+="'"+arr[i]+"',";
				            }
				            str=str.substring(0,str.lastIndexOf(","));
	
							sql.append("AND vod.DEALER_CODE IN("+str+")\n");
						}
				sql.append("                  group by ROOT_ORG_NAME,PQ_ORG_NAME)\n" );
			 
		 }   if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)||  Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("         union all\n" );
				sql.append("         --大区\n" );
				sql.append("\n" );
				sql.append("         select '','2',\n" );
				sql.append("                ROOT_ORG_NAME,\n" );
				sql.append("                ' ',\n" );
				sql.append("                ' ',\n" );
				sql.append("                ROOT_ORG_NAME||'合计',\n" );
				sql.append("                sk,\n" );
				sql.append("                yy,\n" );
				sql.append("                sk + yy hh,\n" );
				sql.append("                mn15,\n" );
				sql.append("                mn30,\n" );
				sql.append("                mn35, mn15jk,\n" );
				sql.append("                mn30jk,\n" );
				sql.append("                mn35jk  \n" );
				sql.append("           from (select root_org_name,\n" );
				sql.append("                        sum(sk) sk,\n" );
				sql.append("                        sum(yy) yy,\n" );
				sql.append("                        sum(mn15) mn15,\n" );
				sql.append("                        sum(mn30) mn30,\n" );
				sql.append("                        sum(mn35) mn35,\n" );
				sql.append("                        sum(mn15jk) mn15jk,\n" );
				sql.append("                        sum(mn30jk) mn30jk,\n" );
				sql.append("                        sum(mn35jk) mn35jk \n" );
				sql.append("                   from mingxi, VW_ORG_DEALER_ALL dealer\n" );
				sql.append("                  where mingxi.DEALER_ID = dealer.dealer_id\n" );
				 if(!"".equals(dealerCode)&&dealerCode!=null){
			            String arr[]=dealerCode.split(",");
			            String str="";

			            for(int i=0;i<arr.length;i++){
			                str+="'"+arr[i]+"',";
			            }
			            str=str.substring(0,str.lastIndexOf(","));

						sql.append("AND dealer.DEALER_CODE IN("+str+")\n");
					}
				sql.append("                  group by root_org_name)\n" );
		 } if(  Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("         union all\n" );
				sql.append("         --全国\n" );
				sql.append("         select '','1' sort,\n" );
				sql.append("                ' ',\n" );
				sql.append("                ' ',\n" );
				sql.append("                ' ',\n" );
				sql.append("                '全国总计',\n" );
				sql.append("                sk,\n" );
				sql.append("                yy,\n" );
				sql.append("                sk + yy hh,\n" );
				sql.append("                mn15,\n" );
				sql.append("                mn30,\n" );
				sql.append("                mn35, mn15jk,\n" );
				sql.append("                mn30jk,\n" );
				sql.append("                mn35jk \n" );
				sql.append("           from (select '1' sort,\n" );
				sql.append("                        '',\n" );
				sql.append("                        '',\n" );
				sql.append("                        '',\n" );
				sql.append("                        '',\n" );
				sql.append("                        '',\n" );
				sql.append("                        sum(sk) sk,\n" );
				sql.append("                        sum(yy) yy,\n" );
				sql.append("                        sum(mn15) mn15,\n" );
				sql.append("                        sum(mn30) mn30,\n" );
				sql.append("                        sum(mn35) mn35,\n" );
				sql.append("                        sum(mn15jk) mn15jk,\n" );
				sql.append("                        sum(mn30jk) mn30jk,\n" );
				sql.append("                        sum(mn35jk) mn35jk\n" );
				sql.append("                   from mingxi, VW_ORG_DEALER_ALL dealer  where mingxi.DEALER_ID = dealer.dealer_id  \n" );
				 if(!"".equals(dealerCode)&&dealerCode!=null){
			            String arr[]=dealerCode.split(",");
			            String str="";

			            for(int i=0;i<arr.length;i++){
			                str+="'"+arr[i]+"',";
			            }
			            str=str.substring(0,str.lastIndexOf(","));

						sql.append("AND dealer.DEALER_CODE IN("+str+")\n");
					}
		 }
		 
		 if(Constant.DUTY_TYPE_COMPANY.intValue()==Integer.parseInt(dutyType)){
			 sql.append("                    )\n" );
		 }
		
		 sql.append("                 ) temp\n" );
		 sql.append("          order by temp.sort, temp.ROOT_ORG_NAME, temp.PQ_ORG_NAME, rownum");
		 
		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
}
