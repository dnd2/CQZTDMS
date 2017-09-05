package com.infodms.dms.dao.sales.planmanage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtCampaignExecutePO;
import com.infodms.dms.po.TtCampaignPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SpacePlanManageDao extends BaseDao{
	public static Logger logger = Logger.getLogger(SpacePlanManageDao.class);
	private static final SpacePlanManageDao dao = new SpacePlanManageDao ();
	public static final SpacePlanManageDao getInstance() {
		return dao;
	}
	
	/**
	 * Function         : OEM活动信息查询
	 * @param           : 方案编号
	 * @param           : 方案名称
	 * @param           : 活动主题
	 * @param           : 活动类别
	 * @param           : 活动时间
	 * @param           : 活动方案状态
	 * @param           : 经销商CODE
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-04
	 */
	public PageResult<Map<String, Object>> activitiesQuery(Long poseId,String areaId,Long orgId,String campaignNo,String campaignName,String campaignSubject,String campaignType,String startDate,
			String endDate,String dealerCode,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer("\n");
		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append("       TTC.CAMPAIGN_NO,\n" );
		sql.append("       TTC.CAMPAIGN_NAME,\n" );
		sql.append("       TTC.CAMPAIGN_TYPE,\n" );
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n" );
		sql.append("       TCSE.SPACE_ID,\n" );
		sql.append("       TCSE.ORG_ID,\n" );
		sql.append("       TCSE.CHECK_STATUS,\n" );
		sql.append("       TMO.Org_Name,\n" );
		sql.append("       TMO.ORG_CODE,\n" );
		sql.append("       TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
		sql.append("       TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE\n" );
		sql.append("  FROM TT_CAMPAIGN TTC, Tt_Campaign_Space_Execute TCSE,TM_ORG TMO, TC_POSE TCP, TM_POSE_BUSINESS_AREA TMPBA\n" );
		sql.append("  WHERE TTC.CAMPAIGN_ID = TCSE.CAMPAIGN_ID\n" );
		sql.append("    AND TMO.ORG_ID = TCSE.ORG_ID\n" );
		sql.append("AND TTC.AREA_ID = TMPBA.AREA_ID\n");
		sql.append("AND TMPBA.POSE_ID = TCP.POSE_ID\n");
		sql.append("AND TTC.CAMPAIGN_TYPE = ").append(Constant.CAMPAIGN_TYPE_01).append("\n");
		sql.append("AND TCP.POSE_ID = " + poseId + "\n");
		sql.append("   AND TCSE.CHECK_STATUS="+Constant.CAMPAIGN_CHECK_STATUS_02+"\n" );
//		if(!"-1".equals(checkStatus)&&!"".equals(checkStatus)&&checkStatus!=null){
//			sql.append("  AND TCE.CHECK_STATUS = "+checkStatus+"\n");
//		}
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
		if (!"".equals(dealerCode)&&null!=dealerCode){
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if(!"".equals(orgId)&&null!=orgId){
			sql.append(" AND TMO.ORG_ID = "+orgId+"\n");
		}
		if(!"".equals(areaId)&&null!=areaId){
			sql.append(" AND TTC.AREA_ID="+areaId+"\n");
		}
		sql.append("ORDER BY TTC.CREATE_DATE DESC, TTC.CAMPAIGN_ID\n"); 
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 方案车型查询
	 * @param           : 方案ID
	 * @return          : 方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-07-05
	 */
	public String getModelName(String campaignId) throws Exception{
		String modelName ="";
		StringBuffer sql= new StringBuffer();
		sql.append("select tvmg.group_name\n" );
		sql.append("  from tt_campaign_group_r tcgr, tm_vhcl_material_group tvmg\n" );
		sql.append(" where tcgr.campaign_model = tvmg.group_id\n" );
		sql.append("   and tcgr.campaign_id ="+campaignId+"\n");
		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName());
		if(rs.size()>0&&rs!=null){
			for(int i=0;i<rs.size();i++){
				Map<String,Object> map = rs.get(i);
				if("".endsWith(modelName)){
					modelName= map.get("GROUP_NAME").toString();
				}else{
					modelName= map.get("GROUP_NAME").toString()+","+modelName;
				}
			}
		}
		return modelName;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 根据执行ID查询附件信息
	 * @param  :执行ID
	 * @return :附件信息
	 */
	public List<Map<String, Object>> getAttachInfo(String executeId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT FSF.FJID,FSF.FILEURL,FSF.FILENAME FROM FS_FILEUPLOAD FSF WHERE FSF.YWZJ ="+executeId+"\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	/**
	 * 根据执行ID查询附件信息
	 * @param  :执行ID
	 * @return :附件信息
	 */
	public Map<String, Object> getExecuteInfo(String campaign){
		StringBuffer sql= new StringBuffer();
		sql.append(" select * from tt_campaign_execute tce where tce.campaign_id="+campaign+"\n");
		Map<String, Object> list =pageQueryMap(sql.toString(),null,getFunName());
		return list;
	}
	
	/**
	 * 通过活动方案id查询活动方案信息
	 * @param campaignId
	 * @return
	 */
	public Map<String, Object> getCompaignPlanById(String campaignId){
		StringBuffer sql= new StringBuffer();
		//原有逻辑：
//		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
//		sql.append("       TTC.CAMPAIGN_MODEL,\n" );
//		sql.append("       TCP.PROD_NAME,\n" );
//		sql.append("       TTC.GROUP_ID,\n" );
//		sql.append("       TVMG.GROUP_NAME,\n" );
//		sql.append("       TCP.PROD_NAME,\n" );
//		sql.append("       TTC.CAMPAIGN_NO,\n" );
//		sql.append("       TTC.CAMPAIGN_NAME,\n" );
//		sql.append("       TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
//		sql.append("       TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE,\n" );
//		sql.append("       TTC.CAMPAIGN_SUBJECT,\n" );
//		sql.append("       TTC.CAMPAIGN_OBJECT,\n" );
//		sql.append("       TTC.CAMPAIGN_PURPOSE,\n" );
//		sql.append("       TTC.CAMPAIGN_NEED,\n" );
//		sql.append("       TTC.CAMPAIGN_DESC,\n" );
//		sql.append("       TTC.CAMPAIGN_TYPE\n" );
//		sql.append(" FROM TT_CAMPAIGN TTC,TM_CAMPAIGN_PROD TCP,TM_VHCL_MATERIAL_GROUP TVMG\n" );
//		sql.append(" WHERE TTC.CAMPAIGN_MODEL = TCP.PROD_ID\n" );
//		sql.append(" AND TVMG.GROUP_ID = TTC.GROUP_ID\n" );
//		sql.append(" AND TTC.CAMPAIGN_ID =");
//		sql.append(campaignId);
//		sql.append("\n");
		
		//长安汽车：
		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append("       TTC.GROUP_ID,\n" );
		sql.append("       TTC.CAMPAIGN_NO,\n" );
		sql.append("       TTC.CAMPAIGN_NAME,\n" );
		sql.append("       TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
		sql.append("       TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE,\n" );
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_OBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_PURPOSE,\n" );
		sql.append("       TTC.CAMPAIGN_NEED,\n" );
		sql.append("       TTC.CAMPAIGN_DESC,\n" );
		sql.append("       TTC.CAMPAIGN_TYPE,\n" );
		sql.append("	   TTC.AREA_ID");
		sql.append(" FROM TT_CAMPAIGN TTC\n" );
		sql.append(" WHERE TTC.CAMPAIGN_ID =");
		sql.append(campaignId);
		sql.append("\n");

		Map<String, Object> rs1 = dao.pageQueryMap(sql.toString(), null, getFunName());
		Map<String, Object> rs  = getModelByCampaignId(rs1);
		return rs;
	}
	
	private  Map<String, Object> getModelByCampaignId(Map<String, Object> rs) {
		if (rs != null && !"".equals(rs.get("CAMPAIGN_ID"))) {
			String modelId = "";
			String modelCode = "";
			StringBuffer sql= new StringBuffer();
			sql.append("select r.relation_id, r.campaign_id, r.campaign_model,t.group_code\n" );
			sql.append("  from tt_campaign_group_r r, tm_vhcl_material_group t\n" );
			sql.append(" where r.campaign_model = t.group_id\n");
			sql.append(" and r.campaign_id = "+rs.get("CAMPAIGN_ID")+" \n");
			
			List<Map<String, Object>> modelList = dao.pageQuery(sql.toString(), null,getFunName());
			for (int j = 0; j < modelList.size(); j++) {
				modelId += modelList.get(j).get("CAMPAIGN_MODEL") + ",";
				modelCode += modelList.get(j).get("GROUP_CODE") + ",";
			}
			if (!"".equals(modelId) && modelId.length() > 0) {
				modelId = modelId.substring(0, modelId.length() - 1);
			}
			if (!"".equals(modelCode) && modelCode.length() > 0) {
				modelCode = modelCode.substring(0, modelCode.length() - 1);
			}			
			rs.put("MODELID", modelId);//车型ID
			rs.put("MODELCODE", modelCode);//车型代码
		}
		return rs;
	}
	
	/**
	 * Function         : 方案信息查询
	 * @param           : 方案ID
	 * @return          : 方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-07-05
	 */
	public Map<String, Object> activitiesInfoQuery(String campaignId,String executeId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append("       TTC.CAMPAIGN_MODEL,\n" );
		sql.append("       TVMG.GROUP_NAME,\n" );
//		sql.append("       TCP.PROD_NAME,\n" );
		sql.append("       TTC.CAMPAIGN_NO,\n" );
		sql.append("       TTC.CAMPAIGN_NAME,\n" );
		sql.append("       TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
		sql.append("       TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE,\n" );
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_OBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_PURPOSE,\n" );
		sql.append("       TTC.CAMPAIGN_NEED,\n" );
		sql.append("       TTC.CAMPAIGN_DESC,\n" );
		sql.append("       TTC.CAMPAIGN_TYPE,\n" );
		sql.append("       TSCE.ORG_ID,\n" );
		sql.append("       TMO.ORG_CODE,\n" );
		sql.append("       TMO.ORG_NAME,\n" );
		sql.append("       TSCE.SPACE_ID,\n" );
		sql.append("       TSCE.EVALUATE_DESC,\n" );
		sql.append("       TSCE.ADVICE_DESC\n" );
		sql.append(" FROM TT_CAMPAIGN TTC,TT_CAMPAIGN_SPACE_EXECUTE TSCE," +
//				"TM_CAMPAIGN_PROD TCP," +
				"TM_ORG TMO,TM_VHCL_MATERIAL_GROUP TVMG\n" );
		sql.append("  WHERE TTC.CAMPAIGN_ID = TSCE.CAMPAIGN_ID\n");
//		sql.append(" AND TTC.CAMPAIGN_MODEL = TCP.PROD_ID\n");
		sql.append("  AND TSCE.ORG_ID = TMO.ORG_ID\n");
		sql.append("  AND TVMG.GROUP_ID = TTC.GROUP_ID\n");
		if(!"".equals(campaignId)&&campaignId!=null){
			sql.append(" AND TTC.CAMPAIGN_ID ="+campaignId+"\n");
		}
		if(!"".equals(executeId)&&executeId!=null){
			sql.append(" AND TSCE.SPACE_ID ="+executeId+"\n");
		}
		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, getFunName());
		return rs;
	}
	
	
	//查询区域树
	public static PageResult<Map<String, Object>> getAllDRLByDeptId(Long companyId,int pageSize,int curPage,Integer poseBusType,String orgName) throws Exception {
		StringBuffer query = new StringBuffer();
		StringBuffer sql=new StringBuffer("SELECT TMO.* FROM TM_ORG TMO WHERE 1=1");
		if(orgName!=null&&orgName!=""){
			sql.append("AND TMO.ORG_NAME='"+orgName+"'");
		}
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(),curPage,pageSize );
		return rs;
	}
	/**
	 * 通过活动方案id找到与之相关联的执行方案
	 * @param campaignId
	 * @return
	 */
	public List<Map<String, Object>> getExecutePlanByCmpId(String campaignId,String dealerId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TCE.execute_id, TCE.DEALER_ID, TD.DEALER_CODE,TD.DEALER_SHORTNAME,TD.PROVINCE_ID,ORG.ORG_NAME,TCE.CHECK_STATUS, TCE.CAMPAIGN_ID\n" );
		sql.append("  FROM tt_campaign_execute TCE,TM_DEALER TD,TM_DEALER_ORG_RELATION TDOR,TM_ORG ORG\n" );
		sql.append(" WHERE TCE.DEALER_ID = TD.DEALER_ID\n" );
		sql.append("   AND TD.DEALER_ID = TDOR.DEALER_ID\n" );
//		sql.append("   AND TDOR.BUSINESS_TYPE = " );
//		sql.append(Constant.ORG_TYPE_OEM);
		sql.append("   AND TDOR.ORG_ID = ORG.ORG_ID\n" );
		sql.append("   AND ORG.ORG_TYPE = " );
		sql.append(Constant.ORG_TYPE_OEM);
		sql.append("\n");
		sql.append("   AND ORG.STATUS = " );
		sql.append(Constant.STATUS_ENABLE);
		sql.append("\n");
		sql.append("   AND TCE.CAMPAIGN_ID = ");
		sql.append(campaignId);
		sql.append("\n");

		
		if(null!=dealerId && !"".equals(dealerId)){
			sql.append(" AND TCE.DEALER_ID = " );
			sql.append(dealerId);
			sql.append("\n");
		}

		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	/**
	 * 通过活动方案id找到与之相关联的执行方案
	 * @param campaignId
	 * @return
	 */
	public List<Map<String, Object>> getMyExecutePlanByCmpId(String campaignId){
		StringBuffer sql= new StringBuffer();
		sql.append(" select td.*,tmd.org_name from tm_dealer_org_relation tdor,tm_dealer td,tm_org tmd  where td.dealer_id=tdor.dealer_id AND tdor.org_id=tmd.org_id and tdor.dealer_id in (select tce.dealer_id from tt_campaign_execute tce where 1=1  AND TCE.CAMPAIGN_ID = "+campaignId+") \n" );
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	/**
	 * 活动执行方案新增
	 * @param po
	 */
	public void addExecutePlan(TtCampaignExecutePO po){
		dao.insert(po);
	}
	/**
	 * 活动执行方案删除
	 * @param po
	 */
	public void delExecutePlan(TtCampaignExecutePO po){
		dao.delete(po);
	}
	/**
	 * 更新活动方案
	 * @param po1
	 * @param po2
	 */
	public void updateCampaignPlan(TtCampaignPO po1,TtCampaignPO po2){
		dao.update(po1, po2);
	}
	/**
	 * 活动执行方案更新
	 * @param po1
	 * @param po2
	 */
	public void updateExecutePlan(TtCampaignExecutePO po1,TtCampaignExecutePO po2){
		dao.update(po1, po2);
	}
}
