package com.infodms.dms.dao.sales.marketmanage.plancommon;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.marketmanage.planmanage.ActivitiesPlanManageDao;
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
 * @author yangpo
 * @mail 
 * @version 1.0
 * @remark 
 */
public class ActivitiesCommonDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ActivitiesCommonDao.class);
	private static final ActivitiesCommonDao dao = new ActivitiesCommonDao ();
	private static POFactory factory = POFactoryBuilder.getInstance();
	public static final ActivitiesCommonDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function         : 活动详细信息查询
	 * @return          : 活动详细信息
	 * @param           : 活动ID
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-29
	 */
	public Map<String, Object> selectCampaignDetial(String executeId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("select tce.EXECUTE_ID, --执行方案ID\n");
		sql.append("       tce.CAMPAIGN_ID, --活动方案ID\n"); 
		sql.append("       tce.DEALER_ID, --经销商ID\n");  
		sql.append("       tce.EXEC_ADD_DESC, --活动地点\n");
		sql.append("       td.DEALER_CODE, --经销商代码\n");  
		sql.append("       td.DEALER_NAME, --经销商名称\n");  
		sql.append("       tce.EXECUTE_DESC, --执行描述\n");  
		sql.append("       tce.CHECK_STATUS, --方案审批状态\n");  
		sql.append("       code.CODE_DESC, --方案审批状态\n");  
		sql.append("       tce.EVALUATE_DESC, --活动评估\n");  
		sql.append("       tce.ADVICE_DESC, --建议及整改措施\n");      
		sql.append("       tc.CAMPAIGN_NO, --活动编号\n");  
		sql.append("       tc.CAMPAIGN_NAME, --活动名称\n");  
		sql.append("       tc.CAMPAIGN_TYPE, --活动类型\n");  
		sql.append("       tc.AREA_ID, --活动类型\n");
		sql.append("        to_char(tc.START_DATE,'yyyy-mm-dd') START_DATE, --活动开始日期\n");  
		sql.append("       to_char(tc.END_DATE,'yyyy-mm-dd') END_DATE, --活动结束日期\n");  
		sql.append("       tc.CAMPAIGN_OBJECT, --活动对象\n");  
		sql.append("       tc.CAMPAIGN_PURPOSE, --活动目的\n");  
		sql.append("       tc.CAMPAIGN_NEED, --活动要求\n");  
		sql.append("       tc.CAMPAIGN_DESC, --活动主要内容\n");
		sql.append("       tc.campaign_subject, --活动主题\n");	
		sql.append("       tce.REMARK, --备注\n");	
		sql.append("       tce.ACTIVICE_SUMMARY_DESC --活动主题\n");	
		sql.append("  from TT_CAMPAIGN         tc,\n");  
		sql.append("       TT_CAMPAIGN_EXECUTE tce,\n");   
		sql.append("       TM_DEALER           td,\n");  
		sql.append("       TC_CODE             code\n");  
		sql.append(" where tc.CAMPAIGN_ID = tce.CAMPAIGN_ID\n");    
		sql.append("   and tce.DEALER_ID = td.DEALER_ID\n");  
		sql.append("   and tce.CHECK_STATUS = code.CODE_ID\n");
		sql.append("   and tce.execute_id = "+executeId);

		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, getFunName());
		return rs;
	}
	
	/**
	 * Function         : 活动详细信息查询(区域)
	 * @return          : 活动详细信息
	 * @param           : 活动ID
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-29
	 */
	public Map<String, Object> selectSpaceCampaignDetial(String executeId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("select TCSE.SPACE_ID EXECUTE_ID, --执行方案ID\n");
		sql.append("       TCSE.CAMPAIGN_ID, --活动方案ID\n"); 
		sql.append("       TCSE.ORG_ID DEALER_ID,\n");  
		sql.append("       TCSE.EXEC_ADD_DESC, --活动地点\n");
		sql.append("       TMO.ORG_CODE DEALER_CODE, \n");  
		sql.append("       TMO.ORG_NAME DEALER_NAME, --经销商名称\n");  
		sql.append("       TCSE.EXECUTE_DESC, --执行描述\n");  
		sql.append("       TCSE.CHECK_STATUS, --方案审批状态\n");  
		sql.append("       code.CODE_DESC, --方案审批状态\n");  
		sql.append("       TCSE.EVALUATE_DESC, --活动评估\n");  
		sql.append("       TCSE.ADVICE_DESC, --建议及整改措施\n");      
		sql.append("       tc.CAMPAIGN_NO, --活动编号\n");  
		sql.append("       tc.CAMPAIGN_NAME, --活动名称\n");  
		sql.append("       tc.CAMPAIGN_TYPE, --活动类型\n");  
		sql.append("       tc.AREA_ID, --活动类型\n");
		sql.append("        to_char(tc.START_DATE,'yyyy-mm-dd') START_DATE, --活动开始日期\n");  
		sql.append("       to_char(tc.END_DATE,'yyyy-mm-dd') END_DATE, --活动结束日期\n");  
		sql.append("       tc.CAMPAIGN_OBJECT, --活动对象\n");  
		sql.append("       tc.CAMPAIGN_PURPOSE, --活动目的\n");  
		sql.append("       tc.CAMPAIGN_NEED, --活动要求\n");  
		sql.append("       tc.CAMPAIGN_DESC, --活动主要内容\n");
		sql.append("       tc.campaign_subject, --活动主题\n");	
		sql.append("       TCSE.REMARK, --备注\n");	
		sql.append("       TCSE.ACTIVICE_SUMMARY_DESC --活动主题\n");	
		sql.append("  from TT_CAMPAIGN         tc,\n");  
		sql.append("       TT_CAMPAIGN_SPACE_EXECUTE TCSE,\n");   
		sql.append("       TM_ORG           TMO,\n");  
		sql.append("       TC_CODE             code\n");  
		sql.append(" where tc.CAMPAIGN_ID = TCSE.CAMPAIGN_ID\n");    
		sql.append("   and TCSE.ORG_ID = TMO.ORG_ID\n");  
		sql.append("   and TCSE.CHECK_STATUS = code.CODE_ID\n");
		sql.append("   and TCSE.SPACE_ID = "+executeId);

		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, getFunName());
		return rs;
	}
	
	/**
	 * Function         : 市场活动费用查询
	 * @return          : 市场活动费用
	 * @param           : Map 包含费用ID,活动ID,执行方案ID
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-29
	 */
	public List<Map<String, Object>> selectCampaignCost(Map<String, Object> map) throws Exception{
		String campaignId=map.get("campaignId").toString();
		String costId=map.get("costId").toString();
		String executeId=map.get("executeId").toString();
		StringBuffer sql= new StringBuffer();


		sql.append("select tccc.COST_ID, --费用ID\n");
		sql.append("       tccc.CAMPAIGN_ID, --活动ID\n"); 
		sql.append("	   TCCC.ACTIVITY_TYPE ,\n");
		sql.append("	   TCCC.ACTIVITY_CONTENT,\n");
		sql.append("       tccc.EXECUTE_ID, --执行方案ID\n");  
		sql.append("       tccc.COST_TYPE, --费用类型\n");  
		sql.append("       tccc.ITEM_NAME, --项目名称\n");  
		sql.append("       tccc.ITEM_REMARK, --项目备注\n");  
		sql.append("       tccc.ITEM_PRICE, --项目单价\n");  
		sql.append("       tccc.ITEM_COUNT, --项目数量\n");
		sql.append("       tccc.region, \n");
		sql.append("       TO_CHAR(TCCC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");
		sql.append("       TO_CHAR(TCCC.END_DATE, 'YYYY-MM-DD') END_DATE,\n");
		sql.append("       nvl(tccc.PLAN_COST, 0) PLAN_COST, --计划费用\n");  
		sql.append("       nvl(tccc.REAL_COST, 0) REAL_COST, --实际费用\n");  
		sql.append("       nvl(tccc.ITEM_COST, 0) ITEM_COST, --补贴费用\n");  
		sql.append("       nvl(tccc.COST_ACCOUNT, 0) COST_ACCOUNT, --费用来源\n");  
		sql.append("       tcc.CODE_DESC --费用来源\n");  
		sql.append("  from TT_CAM_CAMPAIGN_COST tccc, TT_CAMPAIGN tc, TC_CODE tcc\n");  
		sql.append(" where tccc.CAMPAIGN_ID = tc.CAMPAIGN_ID\n");  
		sql.append("   and tccc.COST_ACCOUNT = tcc.CODE_ID(+)\n");  
		if(null!=campaignId&&!"".equals(campaignId)){
			sql.append("   and tccc.CAMPAIGN_ID = "+campaignId+"\n");
		}
		if(null!=costId&&!"".equals(costId)){
			sql.append("   and tccc.COST_ID = "+costId+"\n");
		}
		if(null!=executeId&&!"".equals(executeId)){
			sql.append("   and tccc.EXECUTE_ID = "+executeId+"\n");
		}

		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName());
		return rs;
	}
	
	/**
	 * Function         : 媒体投放费用查询
	 * @return          : 媒体投放费用
	 * @param           : Map 包含费用ID,执行方案ID
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-29
	 */
	public List<Map<String, Object>> selectMediaCost(Map<String, Object> map) throws Exception{
		//String campaignId=map.get("campaignId").toString();
		//String costId=map.get("costId").toString();
		String executeId=map.get("executeId").toString();
		StringBuffer sql= new StringBuffer();

		sql.append("SELECT TCMC.COST_ID,\n");
		sql.append("       TCMC.EXECUTE_ID,\n");  
		sql.append("       TCMC.COST_TYPE,\n");  
		sql.append("       TCMC.ADV_SUBJECT,\n");  
		sql.append("       TO_CHAR(TCMC.ADV_DATE,'YYYY-MM-DD') ADV_DATE,\n");  
		sql.append("       TO_CHAR(TCMC.END_DATE,'YYYY-MM-DD') END_DATE,\n");  
		sql.append("       TCMC.PAYMENT_ACCOUNT,\n");  
		sql.append("       NVL(TCMC.ITEM_PRICE,0) ITEM_PRICE,\n");  
		sql.append("       NVL(TCMC.ITEM_COUNT,0) ITEM_COUNT,\n");  
		sql.append("       NVL(TCMC.PLAN_COST,0) PLAN_COST,\n");  
		sql.append("       NVL(TCMC.REAL_COST,0) REAL_COST,\n");  
		sql.append("       NVL(TCMC.ITEM_COST,0) ITEM_COST,\n");  
		sql.append("       TCMC.MEDIA_TYPE,\n");  
		sql.append("       TCMC.REGION,\n");  
		sql.append("       TMR.REGION_NAME,\n");  
		sql.append("       TCMC.MEDIA_MODEL,\n");  
		sql.append("       TCMC.MEDIA_NAME,\n");  
		sql.append("       TCMC.MEDIA_PUBLISH,\n");  
		sql.append("       TCMC.MEDIA_SIZE,\n");  
		sql.append("       TCMC.MEDIA_COLUMN,\n");  
		sql.append("       NVL(TCMC.TOTAL_COUNT,0) TOTAL_COUNT\n");  
		sql.append("  FROM TT_CAM_MEDIA_COST TCMC, TM_REGION TMR\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND TCMC.REGION = TMR.REGION_ID(+)\n");  
		
		if(null!=executeId&&!"".equals(executeId)){
			sql.append("   AND TCMC.EXECUTE_ID = ").append(executeId).append("\n");
		}

		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName());
		return rs;
	}
	
	/**
	 * Function         : 市场活动目标查询
	 * @return          : 市场活动目标
	 * @param           : Map 包含总结ID,执行方案ID
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-29
	 */
	public Map<String, Object> selectCampaignTarget(Map<String, Object> map) throws Exception{
		//String campaignId=map.get("campaignId").toString();
		String targetId=map.get("targetId").toString();
		String executeId=map.get("executeId").toString();
		StringBuffer sql= new StringBuffer();


		sql.append("select tct.TARGET_ID, --总结ID\n");
		sql.append("       --tct.CAMPAIGN_ID,--CAMPAIGN_ID\n");  
		sql.append("       tct.EXECUTE_ID, --执行方案ID\n");  
		sql.append("       nvl(tct.CALLS_HOUSES_CNT_TGT, 0) CALLS_HOUSES_CNT_TGT, --来店来电数(目标)\n");  
		sql.append("       nvl(tct.RESERVE_CNT_TGT, 0) RESERVE_CNT_TGT, --信息留存量(目标)\n");  
		sql.append("       nvl(tct.RESERVE_CNT_ACT, 0) RESERVE_CNT_ACT, --信息留存量(实际)\n");  
		sql.append("       nvl(tct.ORDER_CNT_TGT, 0) ORDER_CNT_TGT, --订单量(目标)\n");  
		sql.append("       nvl(tct.ORDER_CNT_ACT, 0) ORDER_CNT_ACT, --订单量(实际)\n");  
		sql.append("       nvl(tct.CALLS_HOUSES_CNT_ACT, 0) CALLS_HOUSES_CNT_ACT, --来电来电数(实际)\n");  
		sql.append("       nvl(tct.DELIVERY_CNT_TGT, 0) DELIVERY_CNT_TGT, --交车量(目标)\n");  
		sql.append("       nvl(tct.DELIVERY_CNT_ACT, 0) DELIVERY_CNT_ACT --交车量(实际)\n");  
		sql.append("  from TT_CAMPAIGN_TARGET tct\n");  
		sql.append(" where 1 = 1\n");  

		if(null!=targetId&&!"".equals(targetId)){
			sql.append("   and tct.TARGET_ID = "+targetId+"\n");
		}
		if(null!=executeId&&!"".equals(executeId)){
			sql.append("   and tct.EXECUTE_ID = "+executeId+"\n");
		}

		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, getFunName());
		return rs;
	}
	/**
	 * 市场活动方案附件,FS_FILEUPLOAD中查询
	 * @param  :执行ID
	 * @return :审核信息
	 */
	public List<Map<String, Object>> getAttachInfo(String executeId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT FSF.FJID,FSF.FILEURL,FSF.FILENAME FROM FS_FILEUPLOAD FSF WHERE FSF.YWZJ ="+executeId+"\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	/**
	 * 市场活动方案附件,TT_CAMPAIGN_ATTACH中查询
	 * @param  :执行ID
	 * @return :审核信息
	 */
	public List<Map<String, Object>> selectAttachment(String campaignId) throws Exception{
		StringBuffer sql= new StringBuffer();

		sql.append("select tca.ATTACHMENT_ID, --附件ID\n");
		sql.append("       tca.CAMPAIGN_ID, --活动方案ID\n");  
		sql.append("       tca.ATTACHMENT_PATH, --附件路径\n");  
		sql.append("       tca.ATTACHMENT_TYPE, --附件类型\n");  
		sql.append("       tc.CODE_DESC, --附件类型\n");  
		sql.append("       tca.ATTACHMENT_NAME, --附件显示名称\n");  
		sql.append("       tca.FILE_ID --文件ID\n");  
		sql.append("  from TT_CAMPAIGN_ATTACH tca, tc_code tc\n");  
		sql.append(" where tca.ATTACHMENT_TYPE = tc.CODE_ID");

		sql.append("   and tca.CAMPAIGN_ID = "+campaignId);

		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName());
		return rs;
	}
	/**
	 * Function         : 市场活动方案附件查询
	 * @return          : 市场活动方案附件
	 * @param           : 活动ID
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-29
	 */
	public Map<String, Object> selectAttachmentById(String attachmentId) throws Exception{
		StringBuffer sql= new StringBuffer();

		sql.append("select tca.ATTACHMENT_ID, --附件ID\n");
		sql.append("       tca.CAMPAIGN_ID, --活动方案ID\n");  
		sql.append("       tca.ATTACHMENT_PATH, --附件路径\n");  
		sql.append("       tca.ATTACHMENT_TYPE, --附件类型\n");  
		sql.append("       tc.CODE_DESC, --附件类型\n");  
		sql.append("       tca.ATTACHMENT_NAME, --附件显示名称\n");  
		sql.append("       tca.FILE_ID --文件ID\n");  
		sql.append("  from TT_CAMPAIGN_ATTACH tca, tc_code tc\n");  
		sql.append(" where tca.ATTACHMENT_TYPE = tc.CODE_ID");

		sql.append("   and tca.ATTACHMENT_ID = "+attachmentId);

		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, getFunName());
		return rs;
	}
	/**
	 * 根据执行ID查询审核信息
	 * @param  :执行ID
	 * @return :审核信息
	 */
	public List<Map<String, Object>> getCheckInfo(String executeId,String campaignId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TCEC.CHECK_ID,\n" );
		sql.append("       TO_CHAR(TCEC.CHECK_DATE, 'YYYY-MM-DD HH24:MI:SS') CHECK_DATE,\n" );
		sql.append("       TCEC.CHECK_DESC,\n" );
		sql.append("       TCEC.CHECK_STATUS,\n" );
		sql.append("       TCU.NAME,\n" );
		sql.append("       TMO.ORG_NAME,\n" );
		sql.append("       FSF.FJID,\n" );
		sql.append("       FSF.FILEURL,\n" );
		sql.append("       FSF.FILENAME,\n" );
		sql.append("       A.POSE_NAME\n" );
		sql.append("  FROM TT_CAMPAIGN_EXECUTE_CHK TCEC,\n" );
		sql.append("       TM_ORG                  TMO,\n" );
		sql.append("       TC_USER                 TCU,\n" );
		sql.append("       FS_FILEUPLOAD           FSF,\n" );
		sql.append("       TC_POSE                 A\n" );
		sql.append(" WHERE TCEC.CHECK_ORG_ID = TMO.ORG_ID\n" );
		sql.append("   AND TCEC.CHECK_USER_ID = TCU.USER_ID\n" );
		sql.append("   AND TCEC.CHECK_ID = FSF.YWZJ(+)\n" );
		sql.append("   AND TCEC.CHECK_POSITION_ID = A.POSE_ID(+)\n" );
		sql.append("   AND TCEC.EXECUTE_ID ="+executeId+"\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	public List<Map<String, Object>> getMyCheckInfo(String executeId,String campaignId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TCSC.CHECK_ID,\n" );
		sql.append("       TO_CHAR(TCSC.CHECK_DATE, 'YYYY-MM-DD HH24:MI:SS') CHECK_DATE,\n" );
		sql.append("       TCSC.CHECK_DESC,\n" );
		sql.append("       TCSC.CHECK_STATUS,\n" );
		sql.append("       TCU.NAME,\n" );
		sql.append("       TMO.ORG_NAME,\n" );
		sql.append("       FSF.FJID,\n" );
		sql.append("       FSF.FILEURL,\n" );
		sql.append("       FSF.FILENAME,\n" );
		sql.append("       A.POSE_NAME\n" );
		sql.append("  FROM TT_CAMPAIGN_SUMMERY_CHK TCSC,\n" );
		sql.append("       TM_ORG                  TMO,\n" );
		sql.append("       TC_USER                 TCU,\n" );
		sql.append("       FS_FILEUPLOAD           FSF,\n" );
		sql.append("       TC_POSE                 A\n" );
		sql.append(" WHERE TCSC.CHECK_ORG_ID = TMO.ORG_ID\n" );
		sql.append("   AND TCSC.CHECK_USER_ID = TCU.USER_ID\n" );
		sql.append("   AND TCSC.CHECK_ID = FSF.YWZJ(+)\n" );
		sql.append("   AND TCSC.CHECK_POSITION_ID = A.POSE_ID(+)\n" );
		sql.append("   AND TCSC.EXECUTE_ID ="+executeId+"\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
}
