package com.infodms.dms.dao.sales.marketmanage.planissued;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtCampaignPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 活动方案管理DAO
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-30
 * @author 
 * @mail 
 * @version 1.0
 * @remark 
 */
public class ActivitiesSummaryIssuedDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ActivitiesSummaryIssuedDao.class);
	private static final ActivitiesSummaryIssuedDao dao = new ActivitiesSummaryIssuedDao ();
	private static POFactory factory = POFactoryBuilder.getInstance();
	public static final ActivitiesSummaryIssuedDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function         : 方案信息查询
	 * @param           : 方案ID
	 * @return          : 方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-08-25
	 */
	@SuppressWarnings("unchecked")
	public  List<Map<String,Object>>  queryDealerCost(String dealerId, String areaId){
			StringBuilder sql = new StringBuilder();
			sql.append("  select COST_ID,DEALER_ID,FREEZE_AMOUNT,AVAILABLE_AMOUNT  from tt_vs_cost v   \n");
			sql.append("  WHERE  1=1 ");
			if (dealerId!=null&&!("").equals(dealerId)){
				sql.append(" AND v.DEALER_ID IN("+dealerId+")\n");
			}
			if (null != areaId && !"".equals(areaId)) {
				sql.append(" AND v.AREA_ID = " + areaId + "\n");
			}
			List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
			return list;
	}
	public  List<Map<String,Object>>  queryOemCost(Long orgId, String areaId, Integer costType){
		StringBuffer sql= new StringBuffer();
		
		sql.append("SELECT TVC.COST_ID,TVC.COST_TYPE, TMO.ORG_ID,TVC.FREEZE_AMOUNT,TVC.AVAILABLE_AMOUNT\n" );
		sql.append("   FROM TT_VS_COST TVC,TM_ORG TMO\n" );
		sql.append("  WHERE TVC.ORG_ID = TMO.ORG_ID\n" );
		sql.append("    AND TMO.ORG_ID = " + orgId + "\n" );
		sql.append("    AND TVC.AREA_ID = " + areaId + "\n" );
		sql.append("    AND TVC.COST_TYPE = " + costType + "\n" );
		
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public  List<Map<String,Object>>  queryAreaCost(String dealerId,String areaId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVC.COST_ID, TMO.ORG_ID, TVC.FREEZE_AMOUNT,TVC.AVAILABLE_AMOUNT\n" );
		sql.append("   FROM TT_VS_COST TVC, TM_DEALER_ORG_RELATION TDOR, TM_ORG TMO\n" );
		sql.append("  WHERE TVC.ORG_ID = TDOR.ORG_ID\n" );
		sql.append("    AND TDOR.ORG_ID = TMO.ORG_ID\n" );
		sql.append("    AND TVC.COST_TYPE = "+Constant.COST_TYPE_02+"\n" );
		sql.append("    AND TDOR.DEALER_ID IN ("+dealerId+")\n");
		sql.append("    AND TVC.AREA_ID="+areaId);
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * Function         : 品牌查询
	 * @return          : 品牌信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-29
	 */
	public Map<String, Object> selectPinPai() throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVMG.GROUP_NAME\n" );
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n" );
		sql.append(" WHERE TVMG.GROUP_LEVEL = 1");
		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, getFunName());
		return rs;
	}
	/**
	 * Function         : area待审批方案查询
	 * @param           : 方案编号
	 * @param           : 方案名称
	 * @param           : 活动主题
	 * @param           : 活动类别
	 * @param           : 活动时间
	 * @param           : 经销商ID
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-04
	 */
	public PageResult<Map<String, Object>> areaActivitiesSummaryIssuedQuery(Long poseId, String areaId,String orgId,String campaignNo,String campaignName,String campaignSubject,String campaignType,String startDate,
			String endDate,String dealerCode,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTC.CAMPAIGN_ID,\n");
		sql.append("       TCE.EXECUTE_ID,\n");  
		sql.append("       TD.DEALER_ID,\n");  
		sql.append("       TD.DEALER_CODE,\n");  
		sql.append("       TD.DEALER_NAME,\n");  
		sql.append("       TTC.CAMPAIGN_NO,\n");  
		sql.append("       TTC.CAMPAIGN_NAME,\n");  
		sql.append("       TTC.CAMPAIGN_TYPE,\n");  
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n");  
		sql.append("	   TCU.NAME,\n");
		sql.append("       TO_CHAR(TCE.SUBMITS_DATE, 'YYYY-MM-DD HH24:MI:SS') SUBMITS_DATE,\n");
		sql.append("       TO_CHAR(TTC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");  
		sql.append("       TO_CHAR(TTC.END_DATE, 'YYYY-MM-DD') END_DATE\n");  
		sql.append("  FROM TT_CAMPAIGN           TTC,\n");  
		sql.append("       TT_CAMPAIGN_EXECUTE   TCE,\n");  
		sql.append("       TM_DEALER             TD,\n");  
		sql.append("	   TC_USER               TCU,\n");
		sql.append("       TM_POSE_BUSINESS_AREA TMPBA\n");  
		sql.append(" WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n");  
		sql.append("   AND TTC.AREA_ID = TMPBA.AREA_ID\n");  
		sql.append("   AND TCE.DEALER_ID = TD.DEALER_ID\n");
		sql.append("   AND TTC.CREATE_BY = TCU.USER_ID\n");
		sql.append("   AND TMPBA.POSE_ID = " + poseId + "\n");  
		sql.append("   AND TCE.ORG_ID = " + orgId + "\n");

		sql.append("   AND TCE.CHECK_STATUS IN("+Constant.CAMPAIGN_CHECK_STATUS_07+")\n" );
		// sql.append("   AND TCE.DEALER_ID =TD.DEALER_ID\n");
		// sql.append("   AND TD.DEALER_ID IN \n");
		// sql.append("       (select r.dealer_id from TM_DEALER_ORG_RELATION r where r.ORG_ID ="+orgId+")\n");
		
		if(!"".equals(dealerCode)&&dealerCode!=null){
			sql.append("   AND TCE.DEALER_ID IN("+dealerCode+")\n");
		}
		if(!"".equals(areaId)&&areaId!=null){
			sql.append("   AND TTC.AREA_ID ="+areaId+"\n");
		}
		if(!"".equals(campaignNo)&&campaignNo!=null){
			sql.append("AND TTC.CAMPAIGN_NO LIKE'%"+campaignNo+"%'\n");
		}
		if(!"".equals(campaignName)&&campaignName!=null){
			sql.append("AND TTC.CAMPAIGN_NAME LIKE'%"+campaignName+"%'\n");
		}
		if(!"".equals(campaignSubject)&&campaignSubject!=null){
			sql.append("AND TTC.CAMPAIGN_SUBJECT LIKE'%"+campaignSubject+"%'\n");
		}
		if(!"-1".equals(campaignType)&&!"".equals(campaignType)&&campaignType!=null){
			sql.append("AND TTC.CAMPAIGN_TYPE ="+campaignType+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TTC.START_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TTC.END_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function         : oem待审批方案查询
	 * @param           : 方案编号
	 * @param           : 方案名称
	 * @param           : 活动主题
	 * @param           : 活动类别
	 * @param           : 活动时间
	 * @param           : 经销商ID
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-04
	 */
	public PageResult<Map<String, Object>> oemActivitiesSummaryIssuedQuery(String areaId, Long poseId, String orgId,String campaignNo,String campaignName,String campaignSubject,String campaignType,String startDate,
			String endDate,String dealerCode,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer("\n");
		sql.append("SELECT *\n");  
		sql.append("FROM ( \n");
		sql.append("SELECT TTC.CAMPAIGN_ID,\n");
		sql.append("       TCE.EXECUTE_ID,\n"); 
		sql.append("       TTC.AREA_ID,\n");
		sql.append("       TD.DEALER_ID,\n");  
		sql.append("       TD.DEALER_CODE,\n");  
		sql.append("       TD.DEALER_NAME,\n");  
		sql.append("       TTC.CAMPAIGN_NO,\n");  
		sql.append("       TTC.CAMPAIGN_NAME,\n");  
		sql.append("       TTC.CAMPAIGN_TYPE,\n");  
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n");  
		sql.append("	   TCU.NAME,\n");
		sql.append("       TO_CHAR(TCE.SUBMITS_DATE, 'YYYY-MM-DD HH24:MI:SS') SUBMITS_DATE,\n");
		sql.append("       TO_CHAR(TTC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");  
		sql.append("       TO_CHAR(TTC.END_DATE, 'YYYY-MM-DD') END_DATE\n");  
		sql.append("  FROM TT_CAMPAIGN TTC, TT_CAMPAIGN_EXECUTE TCE, TM_DEALER TD, TC_USER TCU, TM_POSE_BUSINESS_AREA TMPBA\n");  
		sql.append(" WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n");  
		sql.append("   AND TTC.AREA_ID = TMPBA.AREA_ID\n");
		sql.append("   AND TCE.CREATE_BY = TCU.USER_ID\n");
		sql.append("   AND (TTC.CAMPAIGN_TYPE = " + Constant.CAMPAIGN_TYPE_01 + " OR TTC.CAMPAIGN_TYPE = " + Constant.CAMPAIGN_TYPE_02 + ")\n"); 
		sql.append("   AND TCE.CHECK_STATUS IN("+Constant.CAMPAIGN_CHECK_STATUS_08+")\n" );
		sql.append("   AND TCE.DEALER_ID =TD.DEALER_ID\n");
		sql.append("   AND TMPBA.POSE_ID = " + poseId + "\n");
		sql.append("UNION ALL\n");
		sql.append("SELECT TTC.CAMPAIGN_ID,\n");  
		sql.append("       TCSE.SPACE_ID EXECUTE_ID,\n"); 
		sql.append("       TTC.AREA_ID,\n");
		sql.append("       TMO.ORG_ID DEALER_ID,\n");  
		sql.append("       TMO.ORG_CODE DEALER_CODE,\n");  
		sql.append("       TMO.ORG_NAME DEALER_NAME,\n");  
		sql.append("       TTC.CAMPAIGN_NO,\n");  
		sql.append("       TTC.CAMPAIGN_NAME,\n");  
		sql.append("       TTC.CAMPAIGN_TYPE,\n");  
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n");  
		sql.append("	   TCU.NAME,\n");
		sql.append("       TO_CHAR(TCSE.SUBMITS_DATE, 'YYYY-MM-DD HH24:MI:SS') SUBMITS_DATE,\n");
		sql.append("       TO_CHAR(TTC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");  
		sql.append("       TO_CHAR(TTC.END_DATE, 'YYYY-MM-DD') END_DATE\n");  
		sql.append("  FROM TT_CAMPAIGN               TTC,\n");  
		sql.append("       TT_CAMPAIGN_SPACE_EXECUTE TCSE,\n");  
		sql.append("       TM_ORG                    TMO,\n"); 
		sql.append("	   TC_USER               TCU,\n");
		sql.append("       TM_POSE_BUSINESS_AREA     TMPBA\n");  
		sql.append(" WHERE TTC.CAMPAIGN_ID = TCSE.CAMPAIGN_ID\n");  
		sql.append("   AND TTC.AREA_ID = TMPBA.AREA_ID\n"); 
		sql.append("   AND TCSE.CREATE_BY = TCU.USER_ID\n");
		sql.append("   AND TTC.CAMPAIGN_TYPE = " + Constant.CAMPAIGN_TYPE_03 + "\n");  
		sql.append("   AND TCSE.CHECK_STATUS IN("+Constant.CAMPAIGN_CHECK_STATUS_08+")\n" );
		sql.append("   AND TCSE.ORG_ID = TMO.ORG_ID\n");
		sql.append("   AND TMPBA.POSE_ID = " + poseId + "\n");
		sql.append(") TTC \n");
		
		sql.append(" WHERE 1 = 1\n");  
		
		if(null != areaId && !"".equals(areaId)) {
			sql.append("   AND TTC.AREA_ID = " + areaId + "\n");
		}
		
		if(!"".equals(dealerCode)&&dealerCode!=null){
			sql.append("   AND TTC.DEALER_ID IN("+dealerCode+")\n");
		}
		if(!"".equals(campaignNo)&&campaignNo!=null){
			sql.append("AND TTC.CAMPAIGN_NO LIKE'%"+campaignNo+"%'\n");
		}
		if(!"".equals(campaignName)&&campaignName!=null){
			sql.append("AND TTC.CAMPAIGN_NAME LIKE'%"+campaignName+"%'\n");
		}
		if(!"".equals(campaignSubject)&&campaignSubject!=null){
			sql.append("AND TTC.CAMPAIGN_SUBJECT LIKE'%"+campaignSubject+"%'\n");
		}
		if(!"-1".equals(campaignType)&&!"".equals(campaignType)&&campaignType!=null){
			sql.append("AND TTC.CAMPAIGN_TYPE ="+campaignType+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TTC.START_DATE >= '"+startDate+"'\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TTC.END_DATE <= '"+endDate+"'\n");
		}
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * 活动方案删除
	 * @param po
	 */
	public void deleteCampaign(TtCampaignPO po){
		 dao.delete(po);
	}
	/*
	 * 总结审核通过后，更新媒体投放费用
	 */
	public int updateMediaCost(Map<String, Object> map){
		
		String itemCost=map.get("itemCost").toString();
		String costId=map.get("costId").toString();
		
		StringBuffer sql=new StringBuffer("");

		sql.append("merge into TT_VS_COST tvc\n");
		sql.append("using (select tcc.PLAN_COST,\n");  
		sql.append("              tcc.REAL_COST,\n");  
		sql.append("              to_number('"+itemCost+"') item_cost,\n");  
		sql.append("              tcc.PAYMENT_ACCOUNT,\n");  
		sql.append("              tcc.COST_TYPE,\n");  
		sql.append("              tce.DEALER_ID\n");  
		sql.append("         from TT_CAMPAIGN_EXECUTE tce, TT_CAM_MEDIA_COST tcc\n");  
		sql.append("        where tce.EXECUTE_ID = tcc.EXECUTE_ID\n");  
		sql.append("          and tcc.COST_ID = "+costId+") a\n");  
		
		sql.append("on ((tvc.DEALER_ID = a.DEALER_ID and a.COST_TYPE = tvc.COST_TYPE and a.PAYMENT_ACCOUNT = tvc.COST_SOURCE)\n");
		sql.append("or\n");  
		sql.append("(a.COST_TYPE = tvc.COST_TYPE and a.PAYMENT_ACCOUNT = tvc.COST_SOURCE and tvc.COST_TYPE = "+Constant.COST_TYPE_02+" and tvc.COST_SOURCE = "+Constant.COST_SOURCE_03+")");

		//使用区域用户
		/*如果充许二级经销商使用帐户注释掉下边的OR。使用这个SQL
		 * sql.append("or\n");
		sql.append("(a.COST_TYPE = tvc.COST_TYPE\n");  
		sql.append("and a.PAYMENT_ACCOUNT = tvc.COST_SOURCE\n");  
		sql.append("and tvc.COST_TYPE = 11291001\n");  
		sql.append("and tvc.COST_SOURCE = 11301002\n");  
		sql.append("and a.DEALER_ID in (select r.DEALER_ID from TM_DEALER_ORG_RELATION r where r.ORG_ID=tvc.ORG_ID\n");  
		sql.append("and r.DEALER_ID =\n");  
		sql.append("(select d.dealer_id from tm_dealer d start with d.dealer_id=a.dealer_id connect by prior d.DEALER_ID=d.PARENT_DEALER_D\n");  
		sql.append("and d.DEALER_LEVEL=1)\n");  
		sql.append(")\n");  
		sql.append(")");*/

		//只有一级经销商能使用帐户时用这个SQL
		sql.append("or\n");
		sql.append("(a.COST_TYPE = tvc.COST_TYPE\n");  
		sql.append("and a.PAYMENT_ACCOUNT = tvc.COST_SOURCE\n");  
		sql.append("and tvc.COST_TYPE = "+Constant.COST_TYPE_01+"\n");  
		sql.append("and tvc.COST_SOURCE = "+Constant.COST_SOURCE_02+"\n");  
		sql.append("and a.DEALER_ID in (select td.DEALER_ID from TM_DEALER_ORG_RELATION r ,tm_dealer td\n");  
		sql.append("where r.DEALER_ID=td.DEALER_ID and r.ORG_ID=tvc.ORG_ID)\n");  
		sql.append("))");

		sql.append("when matched then\n");  
		sql.append("  update\n");  
		sql.append("     set tvc.COST_AMOUNT      = tvc.COST_AMOUNT - a.REAL_COST - a.ITEM_COST,\n");  
		sql.append("         tvc.FREEZE_AMOUNT    = tvc.FREEZE_AMOUNT - a.PLAN_COST,\n");  
		sql.append("         tvc.AVAILABLE_AMOUNT = tvc.COST_AMOUNT - a.REAL_COST - a.ITEM_COST - tvc.FREEZE_AMOUNT\n");  
		sql.append("                               ");

		return dao.update(sql.toString(), null);
	}
	
	/*
	 * 总结审核通过后，更新市场活动费用
	 */
	public int updateCampCost(Map<String, Object> map){
		
		String itemCost=map.get("itemCost").toString();
		String costId=map.get("costId").toString();
		String userId=map.get("userId").toString();
		
		StringBuffer sql=new StringBuffer("");

		sql.append("merge into TT_VS_COST tvc\n");
		sql.append("using (select tcc.PLAN_COST,\n");  
		sql.append("              tcc.REAL_COST,\n");  
		sql.append("              to_number('"+itemCost+"') item_cost,\n");  
		sql.append("              tcc.COST_ACCOUNT,\n");  
		sql.append("              tcc.COST_TYPE,\n");  
		sql.append("              tce.DEALER_ID\n");  
		sql.append("         from TT_CAMPAIGN_EXECUTE tce, TT_CAM_CAMPAIGN_COST tcc\n");  
		sql.append("        where tce.EXECUTE_ID = tcc.EXECUTE_ID\n");  
		sql.append("          and tcc.COST_ID = "+costId+") a\n");  

		sql.append("on ((tvc.DEALER_ID = a.DEALER_ID and a.COST_TYPE = tvc.COST_TYPE and a.COST_ACCOUNT = tvc.COST_SOURCE)\n");
		sql.append("or\n");  
		sql.append("(a.COST_TYPE = tvc.COST_TYPE and a.COST_ACCOUNT = tvc.COST_SOURCE and tvc.COST_TYPE = "+Constant.COST_TYPE_02+" and tvc.COST_SOURCE = "+Constant.COST_SOURCE_03+")");
		
		//使用区域用户
		/*如果充许二级经销商使用帐户注释掉下边的OR。使用这个SQL
		 * sql.append("or\n");
		sql.append("(a.COST_TYPE = tvc.COST_TYPE\n");  
		sql.append("and a.PAYMENT_ACCOUNT = tvc.COST_SOURCE\n");  
		sql.append("and tvc.COST_TYPE = 11291001\n");  
		sql.append("and tvc.COST_SOURCE = 11301002\n");  
		sql.append("and a.DEALER_ID in (select r.DEALER_ID from TM_DEALER_ORG_RELATION r where r.ORG_ID=tvc.ORG_ID\n");  
		sql.append("and r.DEALER_ID =\n");  
		sql.append("(select d.dealer_id from tm_dealer d start with d.dealer_id=a.dealer_id connect by prior d.DEALER_ID=d.PARENT_DEALER_D\n");  
		sql.append("and d.DEALER_LEVEL=1)\n");  
		sql.append(")\n");  
		sql.append(")");*/

		//只有一级经销商能使用帐户时用这个SQL
		
		sql.append("or\n");
		sql.append("(a.COST_TYPE = tvc.COST_TYPE\n");  
		sql.append("and a.COST_ACCOUNT = tvc.COST_SOURCE\n");  
		sql.append("and tvc.COST_TYPE = "+Constant.COST_TYPE_01+"\n");  
		sql.append("and tvc.COST_SOURCE = "+Constant.COST_SOURCE_02+"\n");  
		sql.append("and a.DEALER_ID in (select td.DEALER_ID from TM_DEALER_ORG_RELATION r ,tm_dealer td\n");  
		sql.append("where r.DEALER_ID=td.DEALER_ID and r.ORG_ID=tvc.ORG_ID)\n");  
		sql.append("))");
		
		sql.append("when matched then\n");  
		sql.append("  update\n");  
		sql.append("     set tvc.COST_AMOUNT      = tvc.COST_AMOUNT - a.REAL_COST - a.ITEM_COST,\n");  
		sql.append("         UPDATE_BY="+userId+",UPDATE_DATE=SYSDATE,\n");
		sql.append("         tvc.FREEZE_AMOUNT    = tvc.FREEZE_AMOUNT - a.PLAN_COST,\n");  
		sql.append("         tvc.AVAILABLE_AMOUNT = tvc.COST_AMOUNT - a.REAL_COST - a.ITEM_COST - tvc.FREEZE_AMOUNT\n");  
		sql.append("                               ");

		return dao.update(sql.toString(), null);
	}
}
