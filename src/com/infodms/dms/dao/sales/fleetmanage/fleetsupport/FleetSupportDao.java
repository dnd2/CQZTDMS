
package com.infodms.dms.dao.sales.fleetmanage.fleetsupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.CompanyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;
/**
 * @Title: 集团客户支持DAO
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-22
 * @author 
 * @mail 
 * @version 1.0
 * @remark 
 */
public class FleetSupportDao extends BaseDao{
	public static Logger logger = Logger.getLogger(FleetSupportDao.class);
	private static final FleetSupportDao dao = new FleetSupportDao ();
	private static POFactory factory = POFactoryBuilder.getInstance();
	public static final FleetSupportDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Function         : 集团客户支持申请查询
	 * @param           : 客户名称
	 * @param           : 客户类型
	 * @param           : 起始时间
	 * @param           : 结束时间
	 * @param           : 经销商公司ID
	 * @param           : 分页参数
	 * @return          : 集团客户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-22
	 */
	public PageResult<Map<String, Object>> fleetSupportApplyQuery(String fleetName,String fleetType,String startDate,String endDate,String dlrCompanyId,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TMF.FLEET_NAME,\n" );
		sql.append("       TMF.REGION,\n" );
		sql.append("	   TMF.FLEET_CODE,");
		sql.append("       TMF.FLEET_TYPE,\n" );
		sql.append("       TMF.MAIN_LINKMAN,\n" );
		sql.append("       TMF.MAIN_PHONE,\n" );
		sql.append("       TO_CHAR(TMF.SUBMIT_DATE, 'YYYY-MM-DD') SUBMIT_DATE,\n" );
		sql.append("       TFS.SUPPORT_STATUS,\n");
		sql.append("       TMF.FLEET_ID\n" );
		sql.append("  FROM TM_FLEET TMF JOIN TT_FLEET_SUPPORT TFS \n" );
		sql.append("  ON TFS.FLEET_ID(+)=TMF.FLEET_ID\n");
		sql.append(" WHERE TMF.STATUS = "+Constant.FLEET_INFO_TYPE_03+"\n" );
		sql.append("   AND TMF.DLR_COMPANY_ID = "+dlrCompanyId+"\n" );
		sql.append("  AND  (TFS.SUPPORT_STATUS IS NULL OR TFS.SUPPORT_STATUS="+Constant.SUPPORT_INFO_STATUS_05+")\n");
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null&&!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TMF.SUBMIT_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
//		sql.append("   AND NOT EXISTS (SELECT 1\n" );
//		sql.append("          FROM TT_FLEET_INTENT TFI\n" );
//		sql.append("         WHERE TFI.STATUS IN ("+Constant.FLEET_SUPPORT_STATUS_02+", "+Constant.FLEET_SUPPORT_STATUS_03+", "+Constant.FLEET_SUPPORT_STATUS_04+", "+Constant.FLEET_SUPPORT_STATUS_06+", "+Constant.FLEET_SUPPORT_STATUS_07+", "+Constant.FLEET_SUPPORT_STATUS_08+")\n" );
//		sql.append("           AND TFI.FLEET_ID = TMF.FLEET_ID)");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * 根据客户ID查询客户信息
	 * @param  :客户ID
	 * @return :集团客户信息
	 */
	public Map<String, Object> getFleetInfobyId(String fleetId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TMF.FLEET_ID,\n" );
		sql.append("       TMF.FLEET_NAME,\n" );
		sql.append("       TMF.FLEET_TYPE,\n" );
		sql.append("       TMF.REGION,\n" );
		sql.append("       TMF.MAIN_LINKMAN,\n" );
		sql.append("       TMF.MAIN_PHONE,\n" );
		sql.append("       TMF.SUBMIT_USER,\n" );
		sql.append("       TMF.FLEET_CODE,\n" );
		sql.append("       TO_CHAR(TFS.SUPPORT_DATE,'YYYY-MM-DD') SUBMIT_DATE,\n" );
		sql.append("       TMF.MAIN_BUSINESS,\n" );
		sql.append("       TMF.FUND_SIZE,\n" );
		sql.append("       TMF.STAFF_SIZE,\n" );
		sql.append("       TMF.PURPOSE,\n" );
		sql.append("       TMF.ADDRESS,\n" );
		sql.append("       TMF.MAIN_JOB,\n" );
		sql.append("       TMF.ZIP_CODE,\n" );
		sql.append("       TMF.MAIN_EMAIL,\n" );
		sql.append("       TMF.STATUS,\n" );
		sql.append("       TMF.OTHER_LINKMAN,\n" );
		sql.append("       TMF.OTHER_PHONE,\n" );
		sql.append("       TMF.OTHER_JOB,\n" );
		sql.append("       TMF.OTHER_EMAIL,\n" );
		sql.append("       TMF.REQ_REMARK,\n" );
		sql.append("       TMF.AUDIT_DATE,\n" );
		sql.append("       TMF.AUDIT_USER_ID,\n" );
		sql.append("       TMF.AUDIT_REMARK,\n" );
		sql.append("       TMF.IS_DEL,\n" );
		sql.append("       TMF.PACT_MANAGE NAME,\n" );
		sql.append("       TMF.PACT_MANAGE_PHONE,\n" );
		sql.append("       TMF.PACT_MANAGE_EMAIL,\n" );
		sql.append("       TMF.DLR_COMPANY_ID,\n");
		sql.append("       TMF.is_pact,\n");
		sql.append("       TMF.pact_id,\n");
		sql.append("       TMF.MARKET_INFO,\n");
		sql.append("       TMF.CONFIG_RQUIRE,\n");
		sql.append("       TMF.FLEETREQ_DISCOUNT,\n");
		sql.append("       TMF.OTHERCOMP_FAVORPOL,\n");
		sql.append("       TFS.SUPPORT_REMARK,\n");
		sql.append("       tcp.pact_name,\n");
		sql.append("       TMC.COMPANY_SHORTNAME,\n" );
		sql.append("       TO_CHAR(TMF.VISIT_DATE, 'YYYY-MM-DD') VISIT_DATE,\n" );
		sql.append("       NVL(D.GROUP_NAME, '全系') GROUP_NAME,\n" );
		sql.append("       TMF.SERIES_COUNT\n" );
		sql.append("  FROM TM_FLEET TMF, TC_USER TCU, TM_COMPANY TMC, TM_VHCL_MATERIAL_GROUP D, Tm_Company_Pact tcp,TT_FLEET_SUPPORT TFS\n" );
		sql.append(" WHERE TMF.IS_DEL = 0\n" );
		sql.append("   AND TMF.SUBMIT_USER = TCU.USER_ID(+)\n" );
		sql.append("   AND TMF.DLR_COMPANY_ID = TMC.COMPANY_ID");
		sql.append("   AND TMF.SERIES_ID = D.GROUP_ID(+)\n");
		sql.append("   AND TMF.pact_id = tcp.pact_id(+)\n");
		sql.append("   AND TFS.FLEET_ID(+)=TMF.FLEET_ID");
		if(!"".equals(fleetId)&&fleetId!=null){
			sql.append(" AND TMF.FLEET_ID ="+fleetId+"\n");
		}
		
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	
	public Map<String, Object> getFleetConById(String fleetId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT * FROM TT_FLEET_CONTRACT A\n");
		if(!"".equals(fleetId)&&fleetId!=null){
			sql.append(" WHERE A.FLEET_ID ="+fleetId+"\n");
		}
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	
	/**
	 * 根据客户ID查询客户意向信息
	 * @param  :客户ID
	 * @return :集团客户意向信息
	 */
	public Map<String, Object> getFleetIntentInfobyId(String fleetId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TFI.INTENT_ID,\n" );
		sql.append("       TO_CHAR(TFI.PURCHASE_DATE,'YYYY-MM-DD') PURCHASE_DATE,\n" );
		sql.append("       TO_CHAR(TFI.PUR_END_DATE,'YYYY-MM-DD') PUR_END_DATE,\n" );
		sql.append("       TFI.INFO_GIVING_MAN,\n" );
		sql.append("       TFI.COMPETE_REMARK,\n" );
		sql.append("       TFI.INFO_REMARK\n" );
		sql.append("  FROM TT_FLEET_INTENT TFI\n" );
		sql.append(" WHERE 1 = 1");
		if(!"".equals(fleetId)&&fleetId!=null){
			sql.append(" AND TFI.FLEET_ID ="+fleetId+"\n");
		}
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	
	public Map<String, Object> getCompanyPactInfoById(String fleetId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT *\n" );
		sql.append("FROM TM_COMPANY_PACT\n" );
		sql.append("WHERE PACT_ID = ").append(fleetId).append("\n");
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	
	/**
	 * 根据客户ID查询客户合同信息
	 * @param  :客户ID
	 * @return :集团客户合同信息
	 */
	public Map<String, Object> getContractInfobyId(String fleetId, String intentId,String contractId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.*,\n" );
		sql.append("       TO_CHAR(A.CHECK_DATE, 'YYYY-MM-DD') MYCHECK_DATE,\n" );
		sql.append("       TO_CHAR(A.START_DATE, 'YYYY-MM-DD') MYSTART_DATE,\n" );
		sql.append("       TO_CHAR(A.END_DATE, 'YYYY-MM-DD') MYEND_DATE,\n" );
		sql.append("       A.BACK_RESON BACK_RESON\n" );
		sql.append("FROM TT_FLEET_CONTRACT A\n" );
		sql.append("WHERE A.FLEET_ID = ").append(fleetId).append("\n");
		sql.append("      AND A.CONTRACT_ID= "+contractId);
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	/**
	 * 根据客户ID查询客户意向明细信息
	 * @param  :客户ID
	 * @param  :意向ID
	 * @return :集团客户意向明细信息
	 */
	public List<Map<String, Object>> getFleetIntentDetailInfobyId(String fleetId,String intentId,String contractId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.INTENT_COUNT,\n" );
		sql.append("	   A.SERIES_ID,\n");
		sql.append("       NVL(B.GROUP_NAME, '全系') GROUP_NAME,\n" );
		sql.append("       A.INTENT_AMOUNT,\n" );
		sql.append("       A.NORM_AMOUNT,\n" );
		sql.append("       A.COUNT_AMOUNT,\n" );
		sql.append("       A.INTENT_POINT,\n" );
		sql.append("       A.REMARK\n" );
		sql.append("  FROM TT_FLEET_INTENT_NEW A,TM_VHCL_MATERIAL_GROUP B\n" );
		sql.append(" WHERE A.SERIES_ID = B.GROUP_ID(+)\n" );
		if(!"".equals(fleetId)&&fleetId!=null){
			sql.append("   AND A.FLEET_ID ="+fleetId+"\n" );
		}
		if(!"".equals(contractId)&&contractId!=null){
		sql.append("       AND A.CONTRACT_ID="+contractId);
		}
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	
	/**
	 * 根据客户ID查询客户意向审核信息
	 * @param  :客户ID
	 * @param  :意向ID
	 * @return :集团客户意向审核信息
	 */
	public List<Map<String, Object>> getFleetIntentCheckInfobyId(String fleetId,String intentId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TO_CHAR(TFIA.AUDIT_DATE, 'YYYY-MM-DD') AUDIT_DATE,\n" );
		sql.append("       TMO.ORG_NAME,\n" );
		sql.append("       TCU.NAME USER_NAME,\n" );
		sql.append("       TCC.CODE_DESC CHECK_STATUS,\n" );
		sql.append("       TFIA.AUDIT_REMARK\n" );
		sql.append("  FROM TT_FLEET_INTENT_AUDIT TFIA, TC_USER TCU, TC_CODE TCC,TM_ORG TMO\n" );
		sql.append(" WHERE TFIA.AUDIT_USER_ID = TCU.USER_ID\n" );
		sql.append("   AND TFIA.AUDIT_RESULT = TCC.CODE_ID\n" );
		sql.append("   AND TFIA.ORG_ID = TMO.ORG_ID\n" );
		if(!"".equals(fleetId)&&fleetId!=null){
			sql.append("   AND TFIA.FLEET_ID ="+fleetId+"\n" );
		}
		if(!"".equals(intentId)&&intentId!=null){
			sql.append("   AND TFIA.INTENT_ID ="+intentId+"\n");
		}
		sql.append("   ORDER BY TFIA.AUDIT_DATE DESC\n" );
		
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getFleetFollowbyId(String fleetId,String intentId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.FOLLOW_ID,A.FOLLOW_REMARK, B.NAME USER_NAME,\n" );
		sql.append("  A.FOLLOW_PERSON,A.FOLLOW_THEME,A.FOLLOW_RANK,");
		sql.append("       TO_CHAR(A.FOLLOW_DATE, 'YYYY-MM-DD') FOLLOW_DATE\n" );
		sql.append("FROM TM_FLEET_FOLLOW A, TC_USER B\n" );
		sql.append("WHERE A.STATUS = ").append(Constant.STATUS_ENABLE);
		sql.append("AND A.CREATE_BY = B.USER_ID\n");
		if(!"".equals(fleetId)&&fleetId!=null){
			sql.append("   AND A.FLEET_ID ="+fleetId+"\n" );
		}
		if(!"".equals(intentId)&&intentId!=null){
			sql.append("   AND A.INTENT_ID ="+intentId+"\n");
		}
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	/**
	 * Function         : 查询物料组弹出树
	 * @param           : 职位ID
	 */
	public static List<TmVhclMaterialGroupPO> getMaterialGroupTree(Long poseId,String groupLevel)
	{
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT GROUP_ID,GROUP_NAME,GROUP_CODE,PARENT_GROUP_ID,TREE_CODE\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T\n");  
		sql.append(" WHERE T.STATUS="+Constant.STATUS_ENABLE+" \n"); 
		if(!"".equals(groupLevel)&&groupLevel!=null&&!"null".equals(groupLevel))
		{
			sql.append(" AND T.GROUP_LEVEL <= "+groupLevel+"\n");
		}
		sql.append(" ORDER BY GROUP_ID \n");
		List<TmVhclMaterialGroupPO> list=factory.select(sql.toString(), null,
				new DAOCallback<TmVhclMaterialGroupPO>() {
			public TmVhclMaterialGroupPO wrapper(ResultSet rs, int idx) {
				TmVhclMaterialGroupPO bean = new TmVhclMaterialGroupPO();
				try {
					bean.setGroupId(Long.valueOf(rs.getString("GROUP_ID")));
					bean.setParentGroupId(Long.valueOf(rs.getString("PARENT_GROUP_ID")));
					bean.setGroupName(rs.getString("GROUP_NAME"));
					bean.setGroupCode(rs.getString("GROUP_CODE"));
				} catch (SQLException e) {
					throw new DAOException(e);
				}
				return bean;
			}
		});
		return list;
	}
	/**
	 * Function         : 查询配置
	 * @param           : 车系、车型、配置ID
	 * @param           : 车系、车型、配置CODE
	 * @param           : 车系、车型、配置名称
	 */
	public static PageResult<Map<String, Object>> getGroupList(Long poseId,String groupIds,String groupId,String groupCode,String groupName,String groupLevel,int curPage,int pageSize)
	{
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");  
		sql.append("       T.GROUP_NAME,\n");  
		sql.append("       T.PARENT_GROUP_ID,\n");  
		sql.append("       T.TREE_CODE\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T\n");  
		sql.append(" WHERE 1 = 1\n");  
		if(!"".equals(groupCode)&&groupCode!=null)
		{
		sql.append("   AND T.GROUP_CODE LIKE '%"+groupCode+"%'\n");	
		}
		if(!"".equals(groupName)&&groupName!=null)
		{
		sql.append("   AND T.GROUP_NAME LIKE '%"+groupName+"%'\n"); 
		}
		if(!"".equals(groupLevel)&&groupLevel!=null&&!"null".equals(groupLevel))
		{
		sql.append("   AND T.GROUP_LEVEL = "+groupLevel+"\n");
		}
		sql.append("   AND T.STATUS = "+Constant.STATUS_ENABLE+"\n");
		if(!"".equals(groupIds)&&groupIds!=null&&!"null".equals(groupIds))
		{
			sql.append("   AND T.GROUP_ID NOT IN( "+groupIds+")\n");
		}
		sql.append("   AND T.GROUP_ID  IN( \n");
		sql.append("SELECT T1.GROUP_ID\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T1\n");  
		sql.append(" WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append(" START WITH T1.GROUP_ID IN\n");  
		sql.append("            (SELECT TAG.MATERIAL_GROUP_ID\n");  
		sql.append("               FROM TM_POSE_BUSINESS_AREA TPBA, TM_AREA_GROUP TAG\n");  
		sql.append("              WHERE TPBA.AREA_ID = TAG.AREA_ID\n");  
		sql.append("                AND TPBA.POSE_ID = "+poseId+")\n");  
		sql.append("CONNECT BY PRIOR T1.GROUP_ID = T1.PARENT_GROUP_ID)\n");
		if(!"".equals(groupId)&&groupId!=null)
		{
		sql.append(" START WITH T.GROUP_ID = "+groupId+"\n");  
		sql.append("CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID\n"); 
		}
		sql.append(" ORDER BY T.GROUP_ID\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function 经销商公司查询
	 * @param companyCode
	 * @param companyName
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public static PageResult<CompanyBean> selectCompany(String companyCode,
			String companyName, int pageSize, int curPage) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select t.COMPANY_ID, t.COMPANY_CODE, t.COMPANY_NAME, t.COMPANY_SHORTNAME from TM_COMPANY t ");
		sql.append(" where  t.COMPANY_TYPE <> ");
		sql.append(Constant.COMPANY_TYPE_SGM);
		sql.append(" and t.STATUS = ");
		sql.append(Constant.STATUS_ENABLE);
		sql.append(companyCode != null ? ("		and t.COMPANY_CODE like '%"
						+ companyCode + "%'") : "");
		sql.append(companyName != null ? ("		and t.COMPANY_NAME like '%"
						+ companyName + "%'") : "");
		
		PageResult<CompanyBean> rs = factory.pageQuery(sql.toString(), null,
				new DAOCallback<CompanyBean>() {
					public CompanyBean wrapper(ResultSet rs, int idx) {
						CompanyBean bean = new CompanyBean();
						try {
							bean.setCompanyId(rs.getString("COMPANY_ID"));
							bean.setCompanyCode(rs.getString("COMPANY_CODE"));
							bean.setCompanyName(rs.getString("COMPANY_NAME"));
							bean.setCompanyShortname(rs.getString("COMPANY_SHORTNAME"));
						} catch (SQLException e) {
							throw new DAOException(e);
						}
						return bean;
					}
				}, pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 集团客户支持申请审核查询
	 * @param           : 客户名称
	 * @param           : 客户类型
	 * @param           : 起始时间
	 * @param           : 结束时间
	 * @param           : 经销商公司ID
	 * @param           : 分页参数
	 * @return          : 集团客户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-22
	 */
	public PageResult<Map<String, Object>> fleetSupportApplyCheckQuery(String orgId, String dutyType, String fleetName,String fleetType,String startDate,String endDate,String companyId,int curPage,int pageSize) throws Exception{
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT\n" );
		sql.append("       TMF.FLEET_ID,\n" );
		sql.append("       TO_CHAR(tFS.support_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("       TMF.FLEET_NAME,\n" );
		sql.append("       TMF.FLEET_TYPE,\n" );
		sql.append("       TMF.REGION,\n" );
		sql.append("       TMF.MAIN_LINKMAN,\n" );
		sql.append("       TMF.MAIN_PHONE,\n" );
		sql.append("        TMF.FLEET_CODE, \n  ");
		sql.append("       tmf.dlr_company_id,\n" );
		sql.append("       TFS.support_status\n" );
		sql.append("  FROM TM_FLEET TMF, TT_FLEET_SUPPORT TFS, TM_COMPANY TMC,tm_dealer td,vw_org_dealer vod\n" );
		sql.append(" WHERE   TFS.FLEET_ID(+)=TMF.FLEET_ID\n" );
		sql.append("   AND TMC.COMPANY_ID = TMF.DLR_COMPANY_ID\n" );
		sql.append("   and td.company_id=tmc.company_id\n" );
		sql.append("   and vod.DEALER_ID=td.dealer_id");

//		if(Utility.testString(dutyType)){
//			if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
//				sql.append("   AND TFI.STATUS = "+Constant.FLEET_SUPPORT_STATUS_02+"\n");
//			}else{
//				sql.append("   AND TFI.STATUS = "+Constant.FLEET_SUPPORT_STATUS_08+"\n");
//			}
//		}
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null&&!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TFs.support_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(dutyType)){
			if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
				sql.append("  and vod.root_ORG_ID="+orgId+"\n" );
				sql.append("  and TFS.support_status="+Constant.SUPPORT_INFO_STATUS_02);

			}
			if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_SMALLREGION))){
				sql.append("  and vod.pq_ORG_ID="+orgId+"\n" );
				sql.append("  and TFS.support_status="+Constant.SUPPORT_INFO_STATUS_01);

			}
			if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_COMPANY))){
				sql.append("  and TFS.support_status="+Constant.SUPPORT_INFO_STATUS_03);

			}
		}
		if(!"".equals(companyId)&&companyId!=null){
			sql.append("   AND TMF.DLR_COMPANY_ID IN ("+companyId+")\n");
		}
		sql.append(" order by 	tfs.support_date desc  ");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 集团客户支持申请审核查询
	 * @param           : 客户名称
	 * @param           : 客户类型
	 * @param           : 起始时间
	 * @param           : 结束时间
	 * @param           : 经销商公司ID
	 * @param           : 分页参数
	 * @return          : 集团客户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-22
	 */
	public PageResult<Map<String, Object>> fleetSupportApplyFinancialCheckQuery(String fleetName,String fleetType,String startDate,String endDate,String companyId,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TFI.INTENT_ID,\n" );
		sql.append("       TMF.FLEET_ID,\n" );
		sql.append("       TO_CHAR(TFI.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("       TMF.FLEET_NAME,\n" );
		sql.append("       TMF.FLEET_TYPE,\n" );
		sql.append("       TMF.REGION,\n" );
		sql.append("       TMF.MAIN_LINKMAN,\n" );
		sql.append("       TMF.MAIN_PHONE\n" );
		sql.append("  FROM TM_FLEET TMF, TT_FLEET_INTENT TFI, TM_COMPANY TMC\n" );
		sql.append(" WHERE TMF.FLEET_ID = TFI.FLEET_ID\n" );
		sql.append("   AND TMC.COMPANY_ID = TMF.DLR_COMPANY_ID\n" );
		sql.append("   AND TFI.STATUS = "+Constant.FLEET_SUPPORT_STATUS_03+"\n");
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null&&!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TFI.REPORT_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(companyId)&&companyId!=null){
			sql.append("   AND TMF.DLR_COMPANY_ID IN ("+companyId+")\n");
		}
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public PageResult<Map<String, Object>> fleetFollowCheckQuery(String orgId, String dutyType, String fleetName,String fleetType,String startDate,String endDate,String companyId,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DISTINCT TMF.FLEET_ID,\n" );
		sql.append("       TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("       TMF.FLEET_NAME,\n" );
		sql.append("       TMF.FLEET_TYPE,\n" );
		sql.append("       TMF.FLEET_CODE,\n" );
		sql.append("       TMF.REGION,\n" );
		sql.append("       TMF.MAIN_LINKMAN,\n" );
		sql.append("       TMF.MAIN_PHONE,TFC.CONTRACT_ID\n" );
		sql.append("  FROM TM_FLEET TMF, TM_COMPANY TMC,\n" );
		sql.append("  	   TM_DEALER A, TM_ORG B, TM_DEALER_ORG_RELATION C,TT_FLEET_CONTRACT TFC\n");
		sql.append(" WHERE TMC.COMPANY_ID = TMF.DLR_COMPANY_ID\n" );
		sql.append(" AND TMF.DLR_COMPANY_ID = A.COMPANY_ID AND  TFC.FLEET_ID(+)=TMF.FLEET_ID\n");
		sql.append(" AND f_get_pid(A.DEALER_ID) = C.DEALER_ID\n");
		sql.append(" AND B.ORG_ID = C.ORG_ID\n");
		//sql.append(" AND B.IS_COUNT = 0\n");
		sql.append("   AND TMF.STATUS = "+Constant.FLEET_INFO_TYPE_03+"\n");
		//sql.append(" and exists (select 1 from tm_fleet_follow ff where ff.fleet_id = tmf.fleet_id)\n");
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(Utility.testString(startDate)){
			sql.append(" AND TMF.SUBMIT_DATE >= TO_DATE('").append(startDate).append(" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endDate)){
			sql.append(" AND TMF.SUBMIT_DATE <= TO_DATE('").append(endDate).append(" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_DEALER))){
			if(!"".equals(companyId)&&companyId!=null){
				sql.append("   AND TMF.DLR_COMPANY_ID = "+companyId+"\n");
			}
		}else{
			if(Utility.testString(dutyType)){
				if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
					sql.append("       AND B.ORG_ID = ").append(orgId).append("\n"); 
				}
			}
		}
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public PageResult<Map<String, Object>> fleetFollowCheckQueryList(String orgId, String dutyType, String fleetName,String fleetType,String startDate,String endDate,String followStartDate,String followEndDate,String companyId,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append(" SELECT * FROM (");
		sql.append("SELECT DISTINCT TMF.FLEET_ID,\n" );
		sql.append("       TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("       TMF.FLEET_NAME,\n" );
		sql.append("       TMF.FLEET_CODE,\n" );
		sql.append("       TMF.FLEET_TYPE,\n" );
		sql.append("       TMF.REGION,\n" );
		sql.append("       TMF.MAIN_LINKMAN,\n" );
		sql.append("       TMF.MAIN_PHONE,\n" );
		sql.append("       TMF.SUBMIT_DATE\n" );
		sql.append("  FROM TM_FLEET TMF, TM_COMPANY TMC,\n" );
		sql.append("  	   TM_DEALER A, TM_ORG B, TM_DEALER_ORG_RELATION C\n");
		sql.append(" WHERE TMC.COMPANY_ID = TMF.DLR_COMPANY_ID\n" );
		sql.append(" AND TMF.DLR_COMPANY_ID = A.COMPANY_ID\n");
		sql.append(" AND f_get_pid(A.DEALER_ID) = C.DEALER_ID\n");
		sql.append(" AND B.ORG_ID = C.ORG_ID\n");
		//sql.append(" AND B.IS_COUNT = 0\n");
		sql.append("   AND TMF.STATUS = "+Constant.FLEET_INFO_TYPE_03+"\n");
		sql.append(" and exists (select 1 from tm_fleet_follow ff where ff.fleet_id = tmf.fleet_id");
		
		if(Utility.testString(followStartDate)){
			sql.append(" AND ff.FOLLOW_DATE >= TO_DATE('").append(followStartDate).append(" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(followEndDate)){
			sql.append(" AND ff.FOLLOW_DATE <= TO_DATE('").append(followEndDate).append(" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(")\n");
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(Utility.testString(startDate)){
			sql.append(" AND TMF.SUBMIT_DATE >= TO_DATE('").append(startDate).append(" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endDate)){
			sql.append(" AND TMF.SUBMIT_DATE <= TO_DATE('").append(endDate).append(" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_DEALER))){
			if(!"".equals(companyId)&&companyId!=null){
				sql.append("   AND TMF.DLR_COMPANY_ID = "+companyId+"\n");
			}
		}else{
			if(Utility.testString(dutyType)){
				if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
					sql.append("       AND B.ORG_ID = ").append(orgId).append("\n"); 
				}
				if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_SMALLREGION))){
					sql.append("AND A.DEALER_ID IN");
					sql.append("  (SELECT DEALER_ID FROM vw_org_dealer ");
					sql.append("WHERE PQ_ORG_ID="+orgId+")");
				}
			}
		}
		sql.append(") ORDER BY SUBMIT_DATE DESC");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	
	/**
	 * Function         : 集团客户支持查询（非经销商）
	 * @param           : 客户名称
	 * @param           : 客户类型
	 * @param           : 起始时间
	 * @param           : 结束时间
	 * @param           : 经销商公司ID
	 * @param           : 申请状态
	 * @param           : 分页参数
	 * @return          : 集团客户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-22
	 */
	public PageResult<Map<String, Object>> fleetSupportQuery(String fleetName,String fleetType,String startDate,String endDate,String companyId,String dutyType,String orgId,String supportStatus,int curPage,int pageSize) throws Exception{
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TMF.FLEET_ID,\n" );
		sql.append("       TO_CHAR(TFS.SUPPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("       TMF.FLEET_NAME,\n" );
		sql.append("       TMF.FLEET_TYPE,\n" );
		sql.append("       TFS.SUPPORT_STATUS,\n" );
		sql.append("       TMF.REGION,\n" );
		sql.append("       TMF.MAIN_LINKMAN,\n" );
		sql.append("       TMF.MAIN_PHONE,\n" );
		sql.append("       TMF.FLEET_CODE,\n" );
		sql.append("       VW.DEALER_ID,\n" );
		sql.append("       C.TOTAL REQUEST_COUNT,\n" );
		sql.append("       D.FINISH_TOTAL FINISH_COUNT\n" );
		sql.append("  FROM TM_FLEET TMF, TM_COMPANY TMC, TM_DEALER TD,TT_FLEET_SUPPORT TFS, vw_org_dealer VW,\n" );
		
		
		sql.append("(SELECT FLEET_ID, TOTAL\n" );
		sql.append("           FROM (SELECT TF.FLEET_ID FLEET_ID, SUM(NVL(TFRD.AMOUNT, 0)) TOTAL\n" );
		sql.append("                   FROM TM_FLEET TF\n" );
		sql.append("                   JOIN TT_FLEET_SUPPORT_INFO TFRD\n" );
		sql.append("                     ON TFRD.FLEET_ID = TF.FLEET_ID\n" );
		sql.append("                  GROUP BY TF.FLEET_ID)) C,\n" );
		sql.append("          (SELECT FLEET_ID,NVL(FINISH_TOTAL,0) FINISH_TOTAL  FROM( SELECT TF.FLEET_ID, COUNT(*) FINISH_TOTAL\n" );
		sql.append("   FROM TT_DEALER_ACTUAL_SALES TDAS\n" );
		sql.append("   JOIN TM_FLEET TF\n" );
		sql.append("     ON TDAS.FLEET_ID = TF.FLEET_ID\n" );
		sql.append("    AND TDAS.FLEET_ID IS NOT NULL\n" );
		sql.append("    AND TDAS.IS_RETURN = 10041002\n" );
		sql.append("    AND TDAS.IS_FLEET = 10041001\n" );
		sql.append("  GROUP BY TF.FLEET_ID))    D\n");
		
		sql.append(" WHERE TMC.COMPANY_ID = TMF.DLR_COMPANY_ID\n" );
		sql.append("   AND TD.COMPANY_ID = TMC.COMPANY_ID\n" );
		sql.append("   AND TD.DEALER_ID = VW.DEALER_ID");
		sql.append("   AND TFS.FLEET_ID = TMF.FLEET_ID");
		sql.append("      AND C.FLEET_ID(+)=TMF.FLEET_ID\n");
		sql.append("       AND D.FLEET_ID(+)=TMF.FLEET_ID\n");
//		if(!"-1".equals(supportStatus)&&!"".equals(supportStatus)&&supportStatus!=null){
//			sql.append("   AND TFI.STATUS = "+supportStatus+"\n");
//		}else{
//			sql.append("   AND TFI.STATUS <> "+Constant.FLEET_SUPPORT_STATUS_01+"\n");
//		}
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!"".equals(supportStatus)&&supportStatus!=null){
			sql.append("   AND TFS.SUPPORT_STATUS = "+supportStatus+"\n");
		}
		//开始时间
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TFS.SUPPORT_DATE > TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
		}
		//结束时间
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TFS.SUPPORT_DATE < TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(orgId)&&orgId!=null){
			if(Constant.DUTY_TYPE_SMALLREGION.toString().equals(dutyType)){
				sql.append("   AND VW.DEALER_ID IN ( SELECT VOD.DEALER_ID FROM vw_org_dealer VOD WHERE VOD.PQ_ORG_ID="+orgId+" )\n");
			}
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
				sql.append("   AND VW.DEALER_ID IN ( SELECT VOD.DEALER_ID FROM vw_org_dealer VOD WHERE VOD.ROOT_ORG_ID="+orgId+" )\n");
			}
			
		}
		if(!"".equals(companyId)&&companyId!=null){
			String companyIds=CommonUtils.getSplitStringForIn(companyId);
			sql.append(" AND TMC.COMPANY_ID in("+companyIds+")\n");
		}
		sql.append(" ORDER BY TFS.CREATE_DATE DESC");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 集团客户支持查询(经销商)
	 * @param           : 客户名称
	 * @param           : 客户类型
	 * @param           : 起始时间
	 * @param           : 结束时间
	 * @param           : 经销商公司ID
	 * @param           : 申请状态
	 * @param           : 分页参数
	 * @return          : 集团客户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-22
	 */
	public PageResult<Map<String, Object>> fleetSupportSearch(String fleetName,String fleetType,String startDate,String endDate,String companyId,String supportStatus,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT \n" );
		sql.append("       TMF.FLEET_ID,\n" );
		sql.append("       TMF.FLEET_CODE,\n" );
		//sql.append("       TO_CHAR(TFI.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TO_CHAR(TFS.SUPPORT_DATE,'YYYY-MM-DD') REPORT_DATE,\n ");
		sql.append("       TMF.FLEET_NAME,\n" );
		sql.append("       TFS.SUPPORT_STATUS,\n" );
		sql.append("       TMF.FLEET_TYPE,\n" );
		sql.append("       TMF.MAIN_LINKMAN,\n" );
		sql.append("       TMF.MAIN_PHONE,\n" );
		sql.append("       C.TOTAL REQUEST_COUNT,\n" );
		sql.append("       D.FINISH_TOTAL FINISH_COUNT\n" );
		sql.append("  FROM TM_FLEET TMF,TT_FLEET_SUPPORT TFS,\n" );
		
		sql.append("(SELECT FLEET_ID, TOTAL\n" );
		sql.append("           FROM (SELECT TF.FLEET_ID FLEET_ID, SUM(NVL(TFRD.AMOUNT, 0)) TOTAL\n" );
		sql.append("                   FROM TM_FLEET TF\n" );
		sql.append("                   JOIN TT_FLEET_SUPPORT_INFO TFRD\n" );
		sql.append("                     ON TFRD.FLEET_ID = TF.FLEET_ID\n" );
		sql.append("                  GROUP BY TF.FLEET_ID)) C,\n" );
		sql.append("          (SELECT FLEET_ID,NVL(FINISH_TOTAL,0) FINISH_TOTAL  FROM( SELECT TF.FLEET_ID, COUNT(*) FINISH_TOTAL\n" );
		sql.append("   FROM TT_DEALER_ACTUAL_SALES TDAS\n" );
		sql.append("   JOIN TM_FLEET TF\n" );
		sql.append("     ON TDAS.FLEET_ID = TF.FLEET_ID\n" );
		sql.append("    AND TDAS.FLEET_ID IS NOT NULL\n" );
		sql.append("    AND TDAS.IS_RETURN = 10041002\n" );
		sql.append("    AND TDAS.IS_FLEET = 10041001\n" );
		sql.append("  GROUP BY TF.FLEET_ID))    D\n");
		
		sql.append(" WHERE  TFS.FLEET_ID=TMF.FLEET_ID\n" );
		sql.append("      AND C.FLEET_ID(+)=TMF.FLEET_ID\n");
		sql.append("       AND D.FLEET_ID(+)=TMF.FLEET_ID\n");
		sql.append("    AND TMF.DLR_COMPANY_ID = "+companyId+"\n");
//		if(!"-1".equals(supportStatus)&&!"".equals(supportStatus)&&supportStatus!=null){
//			sql.append("   AND TFI.STATUS = "+supportStatus+"\n");
//		}else{
//			sql.append("   AND TFI.STATUS <> "+Constant.FLEET_SUPPORT_STATUS_01+"\n");
//		}
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!"".equals(supportStatus)&&supportStatus!=null){
			sql.append("   AND TFS.SUPPORT_STATUS = "+supportStatus+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null&&!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TFS.SUPPORT_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function         : 集团客户支持合同维护查询
	 * @param           : 客户名称
	 * @param           : 客户类型
	 * @param           : 起始时间
	 * @param           : 结束时间
	 * @param           : 经销商公司ID
	 * @param           : 分页参数
	 * @return          : 集团客户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-22
	 */
	public PageResult<Map<String, Object>> fleetContractsMaintainQuery(String conStatus, String oemCompanyId, String dutyType, String orgId,String checkSDate,String checkEDate,String fleetName,String fleetType,String startDate,String endDate,String companyId,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
	
		sql.append("SELECT DISTINCT TMF.FLEET_ID,\n" );
		sql.append("       TO_CHAR(TFC.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE,\n" );
		sql.append("       TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("       TMF.FLEET_NAME,\n" );
		sql.append("       TMF.FLEET_TYPE,\n" );
		sql.append("       TFC.CONTRACT_ID,\n" );
		sql.append("       TMF.REGION,\n" );
		sql.append("       TMF.MAIN_LINKMAN,\n" );
		sql.append("       TMF.MAIN_PHONE,\n" );
		sql.append("       TFC.STATUS\n" );
		sql.append("  FROM TM_FLEET TMF, TM_COMPANY TMC, TT_FLEET_CONTRACT TFC,\n" );
		sql.append("   	   vw_org_dealer VOD, TM_DEALER C, TM_DEALER_ORG_RELATION B\n");
		sql.append(" WHERE TMF.FLEET_ID = TFC.FLEET_ID\n" );
		sql.append("   AND TMC.COMPANY_ID = TMF.DLR_COMPANY_ID\n" );
		sql.append("   AND TMF.CON_STATUS = 1\n");
		sql.append("   AND TMF.DLR_COMPANY_ID = C.COMPANY_ID\n");
		sql.append("   AND VOD.DEALER_ID = C.DEALER_ID\n");
		sql.append("   AND VOD.ROOT_DEALER_ID = B.DEALER_ID\n");
		sql.append("   AND VOD.ROOT_ORG_ID = B.ORG_ID\n");
		//sql.append("   AND A.IS_COUNT = 0\n");
		sql.append("   AND TFC.STATUS IN (").append(Constant.FLEET_CON_STATUS_01).append(",").append(Constant.FLEET_CON_STATUS_03).append(")\n");
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(Utility.testString(conStatus)){
			sql.append("   AND TFC.STATUS = ").append(conStatus).append("\n");
		}
		if(!"".equals(companyId)&&companyId!=null){
			sql.append("   AND TMF.DLR_COMPANY_ID IN ("+companyId+")\n");
		}
		if(!"".equals(checkSDate)&&checkSDate!=null){
			sql.append("   AND TFC.CHECK_DATE >= TO_DATE('"+checkSDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(checkEDate)&&checkEDate!=null){
			sql.append("   AND TFC.CHECK_DATE <= TO_DATE('"+checkEDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public PageResult<Map<String, Object>> firstContractsMaintainQuery(String checkSDate,String checkEDate,String fleetName,String fleetType,String startDate,String endDate,String companyId,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TMF.FLEET_ID,\n" );
		sql.append("       TO_CHAR(TFC.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE,\n" );
		sql.append("       TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("       TMF.FLEET_NAME,\n" );
		sql.append("       TMF.FLEET_TYPE,\n" );
		sql.append("       TMF.REGION,\n" );
		sql.append("       TMF.MAIN_LINKMAN,\n" );
		sql.append("       TFC.CONTRACT_ID,\n" );
		sql.append("       TMF.MAIN_PHONE,\n" );
		sql.append("       TFC.STATUS\n" );
		sql.append("  FROM TM_FLEET TMF, TM_COMPANY TMC, TT_FLEET_CONTRACT TFC\n" );
		sql.append(" WHERE TMF.FLEET_ID = TFC.FLEET_ID\n" );
		sql.append("   AND TMC.COMPANY_ID = TMF.DLR_COMPANY_ID\n" );
		sql.append("   AND TMF.CON_STATUS = 1\n");
		sql.append("   AND TFC.STATUS = ").append(Constant.FLEET_CON_STATUS_01).append("\n");
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!"".equals(companyId)&&companyId!=null){
			sql.append("   AND TMF.DLR_COMPANY_ID IN ("+companyId+")\n");
		}
		if(!"".equals(checkSDate)&&checkSDate!=null){
			sql.append("   AND TFC.CHECK_DATE >= TO_DATE('"+checkSDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(checkEDate)&&checkEDate!=null){
			sql.append("   AND TFC.CHECK_DATE <= TO_DATE('"+checkEDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	
	public PageResult<Map<String, Object>> dealerContractsMaintainQuery(String checkSDate, String checkEDate, String fleetName,String fleetType,String startDate,String endDate,String companyId,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		
		sql.append("WITH  X AS (\n");
//		sql.append("SELECT  FLEET_ID,COMPANY_NAME,\n" );
//		sql.append("FLEET_NAME,FLEET_TYPE,REGION,MAIN_LINKMAN,\n" );
//		sql.append("MAIN_PHONE,MYFLEETNAME,\n" );
//		sql.append("SUBMIT_DATE\n" );
//		sql.append("FROM (\n" );
		sql.append("SELECT A.FLEET_ID,B.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("       A.FLEET_NAME,A.FLEET_TYPE,A.REGION,A.MAIN_LINKMAN,\n" );
		sql.append("       A.MAIN_PHONE,'合同维护' MYFLEETNAME,\n" );
		sql.append("       TO_CHAR(A.SUBMIT_DATE, 'YYYY-MM-DD') SUBMIT_DATE\n" );
		sql.append("  FROM TM_FLEET A, TM_COMPANY B\n" );
		sql.append(" WHERE A.DLR_COMPANY_ID = B.COMPANY_ID\n" );
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND A.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND A.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND A.SUBMIT_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND A.SUBMIT_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("   AND A.CON_STATUS = 0\n");
		sql.append("   AND A.STATUS = ").append(Constant.FLEET_INFO_TYPE_03).append("\n");
		sql.append("   AND A.DLR_COMPANY_ID = ").append(companyId);
		sql.append("\n ),Y AS ( \n");
		sql.append("SELECT A.FLEET_ID,B.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("       A.FLEET_NAME,A.FLEET_TYPE,A.REGION,A.MAIN_LINKMAN,\n" );
		sql.append("       A.MAIN_PHONE,'合同维护' MYFLEETNAME,\n" );
		sql.append("       TO_CHAR(A.SUBMIT_DATE, 'YYYY-MM-DD') SUBMIT_DATE\n" );
		sql.append("  FROM TM_FLEET A, TM_COMPANY B\n" );
		sql.append(" WHERE A.DLR_COMPANY_ID = B.COMPANY_ID\n" );
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND A.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND A.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND A.SUBMIT_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND A.SUBMIT_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("   AND A.CON_STATUS = 1\n");
		//sql.append("   AND A.STATUS = ").append(Constant.FLEET_INFO_TYPE_03).append("\n");
		sql.append("   AND A.DLR_COMPANY_ID = ").append(companyId);
		sql.append(")");
		sql.append("SELECT \n");
		sql.append("NVL(X.FLEET_ID,Y.FLEET_ID) FLEET_ID,\n");
		sql.append("NVL(X.COMPANY_NAME,Y.COMPANY_NAME) COMPANY_NAME,\n");
		sql.append("NVL(X.FLEET_NAME,Y.FLEET_NAME) FLEET_NAME,\n");
		sql.append("NVL(X.FLEET_TYPE,Y.FLEET_TYPE) FLEET_TYPE,\n");
		sql.append("NVL(X.REGION,Y.REGION) REGION,\n");
		sql.append("NVL(X.MAIN_LINKMAN,Y.MAIN_LINKMAN) MAIN_LINKMAN,\n");
		sql.append("NVL(X.MAIN_PHONE,Y.MAIN_PHONE) MAIN_PHONE,\n");
		sql.append("NVL(X.MYFLEETNAME,Y.MYFLEETNAME) MYFLEETNAME,\n");
		sql.append("NVL(X.SUBMIT_DATE,Y.SUBMIT_DATE) SUBMIT_DATE\n");
		sql.append("FROM X FULL OUTER JOIN Y\n");
		sql.append("ON X.FLEET_ID=Y.FLEET_ID\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 集团客户支持合同查询
	 * @param           : 客户名称
	 * @param           : 客户类型
	 * @param           : 起始时间
	 * @param           : 结束时间
	 * @param           : 经销商公司ID
	 * @param           : 合同编号
	 * @param           : 分页参数
	 * @return          : 集团客户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-22
	 */
	public PageResult<Map<String, Object>> fleetContractsQuery(String auditSDate,String auditEDate,String dutyType, String oemCompanyId, String orgId, String fleetStatus, String checkSDate, String checkEDate, String fleetName,String fleetType,String startDate,String endDate,String companyId,String contractNo,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT T.FLEET_ID,\n" );
		sql.append("       T.DLR_COMPANY_ID,T.AUDIT_DATE,\n" );
		sql.append("       T.CONTRACT_ID,\n" );
		sql.append("       T.CONTRACT_NO,\n" );
		sql.append("       T.CONTRACT_AMOUNT,\n" );
		sql.append("       T.DISCOUNT,\n" );
		sql.append("       T.COMPANY_NAME,\n" );
		sql.append("       T.FLEET_NAME,\n" );
		sql.append("       T.FLEET_TYPE,\n" );
		sql.append("       T.MAIN_LINKMAN,\n" );
		sql.append("       T.MAIN_PHONE,\n" );
		sql.append("       T.START_DATE,\n" );
		sql.append("       T.END_DATE,\n" );
		sql.append("       T.CHECK_DATE,\n" );
		sql.append("       T.NAME,\n" );
		sql.append("       T.STATUS,\n" );
		sql.append("       COUNT(TDAS.ORDER_ID) AMOUNT\n" );
		sql.append("  FROM (SELECT TMF.FLEET_ID,\n" );
		sql.append("               TMF.DLR_COMPANY_ID,\n" );
		sql.append("               TFC.CONTRACT_ID,\n" );
		sql.append("               TFC.CONTRACT_NO,\n" );
		sql.append("               TFC.CONTRACT_AMOUNT,TMF.AUDIT_DATE,\n" );
		sql.append("               TFC.DISCOUNT,\n" );
		sql.append("               TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("               TMF.FLEET_NAME,\n" );
		sql.append("               TMF.FLEET_TYPE,\n" );
		sql.append("       		   TMF.MAIN_LINKMAN,\n" );
		sql.append("               TMF.MAIN_PHONE,\n" );
		sql.append("               TFC.STATUS,\n" );
		sql.append("               TCU.NAME,\n" );
		sql.append("               TO_CHAR(TFC.START_DATE, 'YYYY-MM-DD') START_DATE,\n" );
		sql.append("               TO_CHAR(TFC.END_DATE, 'YYYY-MM-DD') END_DATE,\n" );
		sql.append("               TO_CHAR(TFC.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE\n" );
		sql.append("          FROM TM_FLEET          TMF,\n" );
		sql.append("               TM_COMPANY        TMC,\n" );
		sql.append("               TT_FLEET_CONTRACT TFC,\n" );
		sql.append("       		   TM_DEALER E,\n" );
		sql.append("               vw_org_dealer VOD,\n" );
		sql.append("               TC_USER                TCU,\n" );
		sql.append("               TM_DEALER_ORG_RELATION G\n" );
		sql.append("         WHERE TMF.FLEET_ID = TFC.FLEET_ID\n" );
		sql.append("           AND TMC.COMPANY_ID = TMF.DLR_COMPANY_ID\n" );
		sql.append("           AND TMF.DLR_COMPANY_ID = E.COMPANY_ID\n" );
		sql.append("           AND TFC.CHECK_USER = TCU.USER_ID(+)\n" );
		sql.append("		   AND VOD.DEALER_ID=E.DEALER_ID");
		sql.append("       	   AND VOD.ROOT_DEALER_ID = G.DEALER_ID\n" );
		sql.append("   		   AND VOD.ROOT_ORG_ID = G.ORG_ID\n" );
		//sql.append("   		   AND F.IS_COUNT = 0\n");
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TFC.START_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TFC.END_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(dutyType)){
			if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
				sql.append("   AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
			}
		}
		if(!"".equals(companyId)&&companyId!=null){
			sql.append("   AND TMF.DLR_COMPANY_ID IN ("+companyId+")\n");
		}
		if(!"".equals(fleetStatus)&&fleetStatus!=null){
			sql.append("   AND TFC.STATUS = ").append(fleetStatus).append("\n");
		}
		if(!"".equals(contractNo)&&contractNo!=null){
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contractNo+"%'\n");
		}
		if(!"".equals(checkSDate)&&checkSDate!=null){
			sql.append("   AND TFC.CHECK_DATE >= TO_DATE('"+checkSDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(checkEDate)&&checkEDate!=null){
			sql.append("   AND TFC.CHECK_DATE <= TO_DATE('"+checkEDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(auditSDate)&&auditSDate!=null){
			sql.append("   AND TFC.AUDIT_DATE >= TO_DATE('"+auditSDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(auditEDate)&&auditEDate!=null){
			sql.append("   AND TFC.AUDIT_DATE <= TO_DATE('"+auditEDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("           AND TMF.STATUS = ").append(Constant.FLEET_INFO_TYPE_03).append("\n");
		sql.append("		   AND TMF.CON_STATUS = 1\n");
		sql.append(") T\n");
		sql.append("  LEFT JOIN TT_DEALER_ACTUAL_SALES TDAS ON T.DLR_COMPANY_ID =\n" );
		sql.append("                                           TDAS.DLR_COMPANY_ID\n" );
		sql.append("                                       AND T.FLEET_ID = TDAS.FLEET_ID\n" );
		sql.append("                                       AND T.CONTRACT_ID = TDAS.CONTRACT_ID\n" );
		sql.append("                                       AND TDAS.IS_FLEET = "+Constant.IF_TYPE_YES+"\n" );
		sql.append(" GROUP BY T.FLEET_ID,\n" );
		sql.append("          T.DLR_COMPANY_ID,\n" );
		sql.append("          T.CONTRACT_ID,\n" );
		sql.append("          T.CONTRACT_NO,\n" );
		sql.append("          T.CONTRACT_AMOUNT,\n" );
		sql.append("          T.DISCOUNT,\n" );
		sql.append("          T.COMPANY_NAME,\n" );
		sql.append("          T.FLEET_NAME,T.AUDIT_DATE,\n" );
		sql.append("          T.FLEET_TYPE,\n" );
		sql.append("          T.MAIN_LINKMAN,\n" );
		sql.append("          T.MAIN_PHONE,\n" );
		sql.append("          T.START_DATE,\n" );
		sql.append("          T.END_DATE,\n");
		sql.append("          T.CHECK_DATE,");
		sql.append("          T.NAME,");
		sql.append("          T.STATUS");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 集团客户支持合同查询
	 * @param           : 客户名称
	 * @param           : 客户类型
	 * @param           : 起始时间
	 * @param           : 结束时间
	 * @param           : 经销商公司ID
	 * @param           : 合同编号
	 * @param           : 分页参数
	 * @return          : 集团客户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-22
	 */
	public PageResult<Map<String, Object>> fleetContractsSearch(String fleetStatus, String checkSDate, String checkEDate, String fleetName,String fleetType,String startDate,String endDate,String companyId,String contractNo,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT T.FLEET_ID,\n" );
		sql.append("       T.DLR_COMPANY_ID,\n" );
		sql.append("       T.CONTRACT_ID,\n" );
		sql.append("       T.CONTRACT_NO,\n" );
		sql.append("       T.CONTRACT_AMOUNT,\n" );
		sql.append("       T.DISCOUNT,\n" );
		sql.append("       T.COMPANY_NAME,\n" );
		sql.append("       T.FLEET_NAME,\n" );
		sql.append("       T.FLEET_TYPE,\n" );
		sql.append("       T.MAIN_LINKMAN,\n" );
		sql.append("       T.MAIN_PHONE,\n" );
		sql.append("       T.START_DATE,\n" );
		sql.append("       T.STATUS,\n" );
		sql.append("       T.END_DATE,\n" );
		sql.append("       T.CHECK_DATE,T.INTENT_ID,\n" );
		sql.append("       COUNT(TDAS.ORDER_ID) AMOUNT\n" );
		sql.append("  FROM (SELECT TMF.FLEET_ID,TFC.INTENT_ID,\n" );
		sql.append("               TMF.DLR_COMPANY_ID,\n" );
		sql.append("               TFC.CONTRACT_ID,\n" );
		sql.append("               TFC.CONTRACT_NO,\n" );
		sql.append("               TFC.CONTRACT_AMOUNT,\n" );
		sql.append("               TFC.DISCOUNT,\n" );
		sql.append("               TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("               TMF.FLEET_NAME,\n" );
		sql.append("               TMF.FLEET_TYPE,\n" );
		sql.append("               TFC.STATUS,\n" );
		sql.append("       		   TMF.MAIN_LINKMAN,\n" );
		sql.append("               TMF.MAIN_PHONE,\n" );
		sql.append("               TO_CHAR(TFC.START_DATE, 'YYYY-MM-DD') START_DATE,\n" );
		sql.append("               TO_CHAR(TFC.END_DATE, 'YYYY-MM-DD') END_DATE,\n" );
		sql.append("               TO_CHAR(TFC.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE\n" );
		sql.append("          FROM TM_FLEET          TMF,\n" );
		sql.append("               TM_COMPANY        TMC,\n" );
		sql.append("               TT_FLEET_CONTRACT TFC\n" );
		sql.append("         WHERE TMF.FLEET_ID = TFC.FLEET_ID\n" );
		sql.append("           AND TMC.COMPANY_ID = TMF.DLR_COMPANY_ID\n" );
		sql.append("           AND TMF.DLR_COMPANY_ID ="+companyId+"\n");
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(Utility.testString(fleetStatus)){
			sql.append("   AND TFC.STATUS = ").append(fleetStatus).append("\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TFC.START_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TFC.END_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(contractNo)&&contractNo!=null){
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contractNo+"%'\n");
		}
		if(!"".equals(checkSDate)&&checkSDate!=null){
			sql.append("   AND TFC.CHECK_DATE >= TO_DATE('"+checkSDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(checkEDate)&&checkEDate!=null){
			sql.append("   AND TFC.CHECK_DATE <= TO_DATE('"+checkEDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("           AND TMF.STATUS = ").append(Constant.FLEET_INFO_TYPE_03).append("\n");
		sql.append("		   AND TMF.CON_STATUS = 1\n");
		sql.append(") T\n");
		sql.append("  LEFT JOIN TT_DEALER_ACTUAL_SALES TDAS ON T.DLR_COMPANY_ID =\n" );
		sql.append("                                           TDAS.DLR_COMPANY_ID\n" );
		sql.append("                                       AND T.FLEET_ID = TDAS.FLEET_ID\n" );
		sql.append("                                       AND T.CONTRACT_ID = TDAS.CONTRACT_ID\n" );
		sql.append("                                       AND TDAS.IS_FLEET = "+Constant.IF_TYPE_YES+"\n" );
		sql.append(" GROUP BY T.FLEET_ID,\n" );
		sql.append("          T.DLR_COMPANY_ID,\n" );
		sql.append("          T.CONTRACT_ID,\n" );
		sql.append("          T.CONTRACT_NO,\n" );
		sql.append("          T.CONTRACT_AMOUNT,\n" );
		sql.append("          T.DISCOUNT,\n" );
		sql.append("          T.COMPANY_NAME,\n" );
		sql.append("          T.FLEET_NAME,\n" );
		sql.append("          T.FLEET_TYPE,\n" );
		sql.append("          T.MAIN_LINKMAN,\n" );
		sql.append("          T.MAIN_PHONE,\n" );
		sql.append("          T.STATUS,\n" );
		sql.append("          T.START_DATE,\n" );
		sql.append("          T.END_DATE,\n");
		sql.append("          T.CHECK_DATE,T.INTENT_ID");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 集团客户实销信息审核查询
	 * @param           : 客户名称
	 * @param           : 客户类型
	 * @param           : 起始时间
	 * @param           : 结束时间
	 * @param           : 经销商公司ID
	 * @param           : 经销商公司ID
	 * @param           : 经销商公司ID
	 * @param           : 分页参数
	 * @return          : 集团客户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-22
	 */
	public PageResult<Map<String, Object>> fleetSalesInfoCheckQuery(String fleetName,String fleetType,String startDate,String endDate,String companyId,String groupCode,String contractNo,String vin,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		if(null == fleetType) {
			fleetType = "" ;
		}
		if("".equals(fleetType)) {
			sql.append("SELECT CP.PACT_ID FLEET_ID,\n" );
			sql.append("       CP.PACT_NAME FLEET_NAME,\n" );
			sql.append("       -1 FLEET_TYPE,\n" );
			sql.append("       CP.PACT_NAME CONTRACT_NO,\n" );
			sql.append("       CP.PACT_ID CONTRACT_ID,\n" );
			sql.append("       TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
			sql.append("       TVMG.GROUP_CODE MATERIAL_CODE,\n" );
			sql.append("       TVMG.GROUP_NAME MATERIAL_NAME,\n" );
			sql.append("       Tmv.Color COLOR_NAME,\n" );
			sql.append("       TMV.VIN,\n" );
			sql.append("       TDAS.ORDER_ID,\n" );
			sql.append("       TO_CHAR(TDAS.SALES_DATE, 'YYYY-MM-DD') SALES_DATE\n" );
			sql.append("  FROM TM_COMPANY             TMC,\n" );
			sql.append("       TT_DEALER_ACTUAL_SALES TDAS,\n" );
			sql.append("       TM_VEHICLE             TMV,\n" );
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );
//			sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
//			sql.append("       TM_VHCL_MATERIAL       TVM,\n" );
			sql.append("       TM_COMPANY_PACT CP\n" );
			sql.append(" WHERE CP.PACT_ID = TDAS.FLEET_ID\n" );
			sql.append("   AND TMC.COMPANY_ID = TDAS.DLR_COMPANY_ID\n" );
			sql.append("   AND TMV.VEHICLE_ID = TDAS.VEHICLE_ID\n" );
			sql.append("   and tmv.package_id = TVMG.GROUP_ID\n");
//			sql.append("   AND TVM.MATERIAL_ID = TMV.MATERIAL_ID\n" );
//			sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
//			sql.append("   AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n" );
			sql.append("   AND TDAS.FLEET_STATUS = ").append(Constant.Fleet_SALES_CHECK_STATUS_01).append("\n");
			sql.append("   AND TDAS.IS_FLEET = ").append(Constant.IF_TYPE_YES).append("\n");
			sql.append("   AND TDAS.IS_RETURN = ").append(Constant.IF_TYPE_NO).append("\n");
	
			if(!"".equals(fleetName)&&fleetName!=null){
				sql.append("   AND CP.PACT_NAME LIKE '%"+fleetName+"%'\n");
			}
			if(!"".equals(startDate)&&startDate!=null&&!"".equals(endDate)&&endDate!=null){
				sql.append("   AND TDAS.SALES_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
			}
			if(!"".equals(companyId)&&companyId!=null){
				sql.append("   AND TDAS.DLR_COMPANY_ID IN ("+companyId+")\n");
			}
			if(!"".equals(contractNo)&&contractNo!=null){
				sql.append("   AND CP.PACT_NAME LIKE '%"+contractNo+"%'\n");
			}
			if(!"".equals(groupCode)&&groupCode!=null){
				sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
			}
			if (null != vin && !"".equals(vin)) {
				sql.append(GetVinUtil.getVins(vin,"TMV")); 
			}
			sql.append("UNION ALL\n" );
		}
		sql.append("SELECT TMF.FLEET_ID,TMF.FLEET_NAME,TMF.FLEET_TYPE,TFC.CONTRACT_NO,\n" );
		sql.append("       TFC.CONTRACT_ID,TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("       TVMG.GROUP_CODE MATERIAL_CODE,TVMG.GROUP_NAME MATERIAL_NAME,\n" );
		sql.append("       Tmv.Color COLOR_NAME,TMV.VIN,TDAS.ORDER_ID,\n" );
		sql.append("       TO_CHAR(TDAS.SALES_DATE, 'YYYY-MM-DD') SALES_DATE\n" );
		sql.append("  FROM TM_FLEET               TMF,\n" );
		sql.append("       TT_FLEET_CONTRACT      TFC,\n" );
		sql.append("       TM_COMPANY             TMC,\n" );
		sql.append("       TT_DEALER_ACTUAL_SALES TDAS,\n" );
		sql.append("       TM_VEHICLE             TMV,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG\n" );
//		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
//		sql.append("       TM_VHCL_MATERIAL       TVM\n" );
		sql.append(" WHERE TMF.FLEET_ID = TDAS.FLEET_ID\n" );
		sql.append("   AND TFC.CONTRACT_ID = TDAS.CONTRACT_ID\n" );
		sql.append("   AND TMC.COMPANY_ID = TDAS.DLR_COMPANY_ID\n" );
		sql.append("   AND TMV.VEHICLE_ID = TDAS.VEHICLE_ID\n" );
//		sql.append("   AND TVM.MATERIAL_ID = TMV.MATERIAL_ID\n" );
//		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
//		sql.append("   AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n" );
		sql.append("   and tmv.package_id = TVMG.GROUP_ID\n");
		sql.append("   AND TDAS.FLEET_STATUS = ").append(Constant.Fleet_SALES_CHECK_STATUS_01).append("\n");
		sql.append("   AND TDAS.IS_FLEET = ").append(Constant.IF_TYPE_YES).append("\n");
		sql.append("   AND TDAS.IS_RETURN = ").append(Constant.IF_TYPE_NO).append("\n");
		
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null&&!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TDAS.SALES_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(companyId)&&companyId!=null){
			sql.append("   AND TDAS.DLR_COMPANY_ID IN ("+companyId+")\n");
		}
		if(!"".equals(contractNo)&&contractNo!=null){
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contractNo+"%'\n");
		}
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin,"TMV")); 
		}
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * 根据客户ID查询客户信息
	 * @param  :客户ID
	 * @return :集团客户信息
	 */
	public Map<String, Object> getVehicleInfobyId(String orderId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TDAS.VEHICLE_NO,\n" );
		sql.append("       TDAS.CONTRACT_NO,\n" );
		sql.append("       TO_CHAR(TDAS.SALES_DATE,'YYYY-MM-DD') SALES_DATE,\n" );
		sql.append("       TMD.DEALER_CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       TO_CHAR(TDAS.INVOICE_DATE, 'YYYY-MM-DD') INVOICE_DATE,\n" );
		sql.append("       TDAS.INVOICE_NO,\n" );
		sql.append("       TDAS.INSURANCE_COMPANY,\n" );
		sql.append("       TO_CHAR(TDAS.INSURANCE_DATE, 'YYYY-MM-DD') INSURANCE_DATE,\n" );
		sql.append("       TO_CHAR(TDAS.CONSIGNATION_DATE, 'YYYY-MM-DD') CONSIGNATION_DATE,\n" );
		sql.append("       TDAS.MILES,\n" );
		sql.append("       TDAS.PAYMENT,\n" );
		sql.append("       TDAS.PRICE,\n" );
		sql.append("       TDAS.MEMO,\n" );
		sql.append("       TMV.VIN,\n" );
		sql.append("       TMV.ENGINE_NO,\n" );
		sql.append("       TVMG1.GROUP_NAME SERIES_NAME,\n" );
		sql.append("       TVMG2.GROUP_NAME MODEL_NAME,\n" );
		sql.append("       TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       TC1.CTM_TYPE,TC1.COMPANY_PHONE,TC1.MAIN_PHONE,TC1.CTM_NAME,\n" );
		sql.append("       TVM.COLOR_NAME\n" );
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TDAS,\n" );
		sql.append("       TM_DEALER             TMD,\n" );
		sql.append("       TM_VEHICLE             TMV,\n" );
		sql.append("       TM_VHCL_MATERIAL       TVM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG1,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n" );
		sql.append("       TT_CUSTOMER TC1\n" );
		sql.append(" WHERE TDAS.VEHICLE_ID = TMV.VEHICLE_ID\n" );
		sql.append("   AND TDAS.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TMV.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TC1.CTM_ID=TDAS.CTM_ID\n");
		sql.append("   AND TMV.SERIES_ID = TVMG1.GROUP_ID\n" );
		sql.append("   AND TMV.MODEL_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TDAS.IS_FLEET = "+Constant.IF_TYPE_YES+"\n" );
		if(!"".equals(orderId)&&orderId!=null){
			sql.append("   AND TDAS.ORDER_ID = "+orderId+"\n");
		}
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	/*
	 * 集团客户实销信息下载
	 */
	public List<Map<String, Object>> queryReportInfoExportList(
			Map<String, String> map) {
		String customer_type = (String) map.get("customer_type"); // 客户类型
		String vin = (String) map.get("vin"); // VIN
		String materialCode = (String) map.get("materialCode"); // 选择物料组
		String fleet_name = (String) map.get("fleet_name"); // 集团客户名称
		String contract_no = (String) map.get("contract_no"); // 集团客户合同
		String startDate = (String) map.get("startDate"); // 上报日期:开始
		String endDate = (String) map.get("endDate"); // 上报日期 ：截止
		String checkStatus = (String) map.get("checkStatus");// 审核状态
		String companyId = (String) map.get("companyId");// 经销商
		String logsdate = (String) map.get("logsdate");// 审核开始日期
		String logedate = (String) map.get("logedate");// 审核结束日期
		String orgId = (String) map.get("orgId");// 大区
		
		StringBuffer sql = new StringBuffer("\n");
		
		sql.append("SELECT       XX.DEALER_CODE, --经销商代码\n");
		sql.append("XX.GROUPSTATUS,XX.LIFE_CYCLE,XX.ROOT_ORG_NAME, --大区\n");
		sql.append("XX.ROOT_DEALER_NAME,--上级单位\n");
		sql.append("XX.FLEETSTATUSNAME,XX.ROOT_DEALER_ID, --上级单位\n");
		sql.append("XX.REGION_NAME, --省份\n");
		sql.append("XX.CITY_NAME, --城市\n");
		sql.append("XX.TOWN,XX.FLEET_STATUS, --省份\n");
		sql.append("XX.AREA_NAME, --业务范围\n");
		sql.append("TO_CHAR(XX.SALES_DATE,'YYYY-MM-DD') SALES_DATE, --销售日期\n");
		sql.append("TO_CHAR(XX.PRODUCT_DATE,'YYYY-MM-DD') PRODUCT_DATE, --生产日期\n");
		sql.append("TO_CHAR(XX.INVOICE_DATE,'YYYY-MM-DD') INVOICE_DATE, --开票日期\n");
		sql.append("XX.DEALER_SHORTNAME, --销售单位\n");
		sql.append("XX.CTM_NAME, --客户名称\n");
		sql.append("XX.MAIN_PHONE, \n");
		sql.append("XX.CTM_TYPE, --客户类型\n");
		sql.append("XX.IS_FLEET, --是否集团客户\n");
		sql.append("XX.FLEET_NAME, --集团客户名称\n");
		sql.append("XX.CONTRACT_NO, --集团客户合同\n");
		sql.append("XX.SERNAME, --车型系列\n");
		sql.append("XX.MODNAME, --车型类别\n");
		sql.append("XX.MODCODE, --车型编码\n");
		sql.append("XX.PACKNAME,XX.PACKCODE, --状态\n");
		sql.append("XX.PARTFLEET_TYPE,\n");
		sql.append("XX.FLEET_TYPE,\n");
		sql.append("XX.FLEET_ID,\n");
		sql.append("XX.COLOR,\n");
		sql.append("XX.VIN,\n");
		sql.append("XX.AUDITNAME  --审核人,\n");		//2012-08-20 新增下载时增加审批人字段 韩晓宇
		sql.append("FROM (SELECT TMD.DEALER_CODE, --经销商代码\n"); // YH 解决下载极慢问题
																// 2011.6.9
		sql.append("decode(TTS.FLEET_STATUS,11221001,'未审核',11221002,'审核通过',11221003,'驳回') FLEETSTATUSNAME,SUBSTR(TVMG2.Group_Code,instr(TVMG2.Group_Code, '.')+1) GROUPSTATUS,TC3.CODE_DESC LIFE_CYCLE,VOD.ROOT_ORG_NAME, --大区\n");
		sql.append("VOD.ROOT_DEALER_NAME,--上级单位\n");
		sql.append("VOD.ROOT_DEALER_ID, --上级单位\n");
		sql.append("VOD.REGION_NAME, --省份\n");
		sql.append(" VOD.CITY_NAME, --城市\n");
		sql.append("TMR.REGION_NAME TOWN,TTS.FLEET_STATUS, --省份\n");
		sql.append(" TBA.AREA_NAME, --业务范围\n");
		sql.append(" TTS.SALES_DATE, --销售日期\n");
		sql.append(" TMV.PRODUCT_DATE, --生产日期\n");
		sql.append("TVDM.BILLING_DATE INVOICE_DATE, --开票日期\n");
		sql.append(" TMD.DEALER_SHORTNAME, --销售单位\n");
		sql.append(" TTC.CTM_NAME, --客户名称\n");
		sql.append(" TTC.MAIN_PHONE, \n");
		sql.append("decode(TTC.CTM_TYPE,10831001,'个人客户',10831002,'公司客户') CTM_TYPE, --客户类型\n");
		sql.append("TTS.IS_FLEET, --是否集团客户\n");
		sql.append("TF.FLEET_ID,TF.FLEET_NAME, --集团客户名称\n");
		sql.append("  TFC.CONTRACT_NO, --集团客户合同\n");
		sql.append(" TVMG.GROUP_NAME SERNAME, --车型系列\n");
		sql.append(" TVMG1.GROUP_NAME MODNAME, --车型类别\n");
		sql.append(" TVMG1.GROUP_CODE MODCODE, --车型编码\n");
		sql.append(" TVMG2.GROUP_NAME PACKNAME, TVMG2.GROUP_CODE PACKCODE, --状态\n");
		sql.append(" TC2.CODE_DESC PARTFLEET_TYPE,\n");
		sql.append(" nvl(tcp.pact_name, TC2.CODE_DESC) FLEET_TYPE,\n");
		sql.append(" TMV.COLOR,\n");
		sql.append(" TMV.VIN,\n");
		sql.append(" TCU.NAME             AUDITNAME\n");	//2012-08-20 新增下载时增加审批人字段 韩晓宇
		sql.append("FROM TT_DEALER_ACTUAL_SALES TTS,\n");

		if ((!"".equals(logsdate) && logsdate != null)
				|| (!"".equals(logedate) && logedate != null)) {
			sql.append(" TT_DEALER_ACTUAL_SALES_LOG TDASL,\n");
		}

		sql.append(" TM_DEALER              TMD,\n");
		sql.append(" TT_CUSTOMER            TTC,\n");
//		sql.append("(SELECT VEHICLE_ID,max(DELIVERY_DETAIL_ID) DELIVERY_DETAIL_ID FROM  TT_VS_DLVRY_MCH TVDM WHERE TVDM.IF_INSPECTION=1 group by VEHICLE_ID) TVDM,\n");
//		sql.append("TT_VS_DLVRY_DTL TVDD,\n");
		sql.append("(select VEHICLE_ID, max(tvvo.billing_date) billing_date\n");
		sql.append("                  from tt_vs_vhle_order tvvo\n");  
		sql.append("                 group by VEHICLE_ID) TVDM,\n");
		sql.append("TM_VEHICLE             TMV,\n");
		sql.append("TM_VHCL_MATERIAL       M,\n");
		sql.append("TM_BUSINESS_AREA       TBA,\n");
		sql.append("TM_FLEET               TF,\n");
		sql.append("TT_FLEET_CONTRACT      TFC,\n");
		sql.append("vw_org_dealer          VOD,\n");
		sql.append("TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("Tm_Company_Pact tcp,\n");
		sql.append("TC_CODE TC2,\n");
		sql.append("TC_CODE TC3,\n");
		sql.append("TC_USER TCU,\n");	//2012-08-20 新增下载时增加审批人字段 韩晓宇
		sql.append("(select * from TM_REGION where region_type=")
				.append(Constant.REGION_TYPE_04).append(") TMR\n");
		sql.append("WHERE TTS.DEALER_ID = TMD.DEALER_ID AND TC3.CODE_ID=TMV.LIFE_CYCLE and tf.pact_id = tcp.pact_id(+)\n");

		if ((!"".equals(logsdate) && logsdate != null)
				|| (!"".equals(logedate) && logedate != null)) {
			sql.append(" AND TDASL.ORDER_ID(+) = TTS.ORDER_ID\n");
		}

		sql.append(" AND TC2.CODE_ID=TF.FLEET_TYPE\n");
		sql.append("AND TVMG.GROUP_ID = TMV.SERIES_ID\n");
		sql.append("AND TVMG1.GROUP_ID = TMV.MODEL_ID\n");
		sql.append("AND TVMG2.GROUP_ID = TMV.PACKAGE_ID\n");
		sql.append("AND TBA.AREA_ID = TMV.AREA_ID\n");
		sql.append("AND VOD.DEALER_ID = TMD.DEALER_ID \n");
		
//		sql.append("AND TVDM.VEHICLE_ID(+)=TMV.VEHICLE_ID AND TVDD.DETAIL_ID(+)=TVDM.DELIVERY_DETAIL_ID\n");
		sql.append("AND TVDM.VEHICLE_ID(+)=TMV.VEHICLE_ID\n");
		
		sql.append("AND TTS.CTM_ID = TTC.CTM_ID\n");
		sql.append("AND TMR.REGION_CODE(+)=TTC.TOWN\n");
		sql.append("AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");
		sql.append("AND TTS.FLEET_ID = TF.FLEET_ID\n");
		sql.append("AND TTS.IS_RETURN = 10041002\n");
//		sql.append("AND TTS.IS_FLEET = 10041001\n");
		sql.append("AND TTS.CONTRACT_ID = TFC.CONTRACT_ID\n");
//		sql.append("AND TMV.LIFE_CYCLE = 10321004\n");
		if (!"".equals(contract_no) && contract_no != null) {
			sql.append("AND TFC.CONTRACT_NO ='" + contract_no + "'\n");
		}
		if (!"".equals(fleet_name) && fleet_name != null) {
			sql.append("AND TTC.CTM_NAME ='" + fleet_name + "'\n");
		}
		if (!"".equals(customer_type) && customer_type != null) {
			sql.append("AND TF.FLEET_TYPE ='" + customer_type + "'\n");
		}
		if (!"".equals(vin) && vin != null) {
			sql.append("AND TMV.VIN LIKE '%" + vin + "%'\n");
		}
		if (!"".equals(materialCode) && materialCode != null) {
			sql.append("AND TVMG.GROUP_CODE='" + materialCode + "'\n");
		}
		if (!"".equals(companyId) && companyId != null) {
			sql.append(" AND TTS.DLR_COMPANY_ID IN(" + companyId + ")\n");
		}
		if (!"".equals(checkStatus) && checkStatus != null) {
			sql.append("AND TTS.FLEET_STATUS ='" + checkStatus + "'\n");
		}
		if (!"".equals(startDate) && startDate != null) {
			sql.append("AND TTS.SALES_DATE >= TO_DATE('" + startDate
					+ "  00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (!"".equals(endDate) && endDate != null) {
			sql.append("AND TTS.SALES_DATE  <= TO_DATE('" + endDate
					+ " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (!"".equals(logsdate) && logsdate != null) {
			sql.append("AND TDASL.LOG_DATE >=TO_DATE('" + logsdate
					+ " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (!"".equals(logedate) && logedate != null) {
			sql.append("AND TDASL.LOG_DATE  <= TO_DATE('" + logedate
					+ " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		sql.append("AND TTS.UPDATE_BY = TCU.USER_ID(+)\n");	//2012-08-20 新增下载时增加审批人字段 韩晓宇
		if (!"".equals(orgId) && orgId != null) {
			sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		sql.append("\n");
		
		if ("".equals(contract_no) || contract_no == null) {
			sql.append("UNION\n");
			
			
			sql.append("SELECT TMD.DEALER_CODE, --经销商代码\n"); // YH
																		// 解决下载极慢问题
																		// 2011.6.9
			sql.append("decode(TTS.FLEET_STATUS,11221001,'未审核',11221002,'审核通过',11221003,'驳回') FLEETSTATUSNAME,SUBSTR(TVMG2.Group_Code,instr(TVMG2.Group_Code, '.')+1) GROUPSTATUS, TC4.CODE_DESC LIFE_CYCLE,VOD.ROOT_ORG_NAME, --大区\n");
			sql.append("VOD.ROOT_DEALER_NAME,--上级单位\n");
			sql.append("VOD.ROOT_DEALER_ID, --上级单位\n");
			sql.append("VOD.REGION_NAME, --省份\n");
			sql.append(" VOD.CITY_NAME, --城市\n");
			sql.append(" TMR.REGION_NAME TOWN,TTS.FLEET_STATUS, --省份\n");
			sql.append(" TBA.AREA_NAME, --业务范围\n");
			sql.append(" TTS.SALES_DATE, --销售日期\n");
			sql.append(" TMV.PRODUCT_DATE, --生产日期\n");
			sql.append("TVDM.BILLING_DATE INVOICE_DATE, --开票日期\n");
			sql.append(" TMD.DEALER_SHORTNAME, --销售单位\n");
			sql.append(" TTC.CTM_NAME, --客户名称\n");
			sql.append(" TTC.MAIN_PHONE, \n");
			sql.append("  decode(TTC.CTM_TYPE,10831001,'个人客户',10831002,'公司客户') CTM_TYPE, --客户类型\n");
			sql.append(" TTS.IS_FLEET, --是否集团客户\n");
			sql.append("TCP.PACT_ID FLEET_ID,TCP.PACT_NAME FLEET_NAME, --集团客户名称\n");
			sql.append("'' CONTRACT_NO, --集团客户合同\n");
			sql.append("TVMG.GROUP_NAME SERNAME, --车型系列\n");
			sql.append("TVMG1.GROUP_NAME MODNAME, --车型类别\n");
			sql.append(" TVMG1.GROUP_CODE MODCODE, --车型编码\n");
			sql.append(" TVMG2.GROUP_NAME PACKNAME, TVMG2.GROUP_CODE PACKCODE, --状态\n");
			sql.append(" TC3.CODE_DESC PARTFLEET_TYPE,\n");
			sql.append("TCP.PACT_NAME FLEET_TYPE,\n");
			sql.append("TMV.COLOR,\n");
			sql.append("TMV.VIN,\n");		
			sql.append("TCU.NAME            AUDITNAME\n");	//2012-08-20 新增下载时增加审批人字段 韩晓宇
			sql.append("FROM TT_DEALER_ACTUAL_SALES TTS,\n");
			if ((!"".equals(logsdate) && logsdate != null)
					|| (!"".equals(logedate) && logedate != null)) {
				sql.append("TT_DEALER_ACTUAL_SALES_LOG TDASL,\n");
			}
			sql.append("TM_DEALER  TMD,\n");
			sql.append("TT_CUSTOMER  TTC,\n");
			sql.append("TM_VEHICLE             TMV,\n");
			sql.append("TM_VHCL_MATERIAL       M,\n");
			// sql.append("TM_FLEET               TF,\n");
			// sql.append(" TT_FLEET_CONTRACT      TFC,\n");
			sql.append("TM_COMPANY_PACT        TCP,\n");
			
			sql.append("(select VEHICLE_ID, max(tvvo.billing_date) billing_date\n");
			sql.append("                  from tt_vs_vhle_order tvvo\n");  
			sql.append("                 group by VEHICLE_ID) TVDM,\n");
	
			/*sql.append("(SELECT VEHICLE_ID,max(DELIVERY_DETAIL_ID) DELIVERY_DETAIL_ID FROM  TT_VS_DLVRY_MCH TVDM WHERE TVDM.IF_INSPECTION=1 group by VEHICLE_ID) TVDM,\n");
			sql.append("TT_VS_DLVRY_DTL TVDD,\n");*/
			sql.append("vw_org_dealer          VOD,\n");
			sql.append("TM_BUSINESS_AREA       TBA,\n");
			sql.append("TM_VHCL_MATERIAL_GROUP TVMG,\n");
			sql.append("TM_VHCL_MATERIAL_GROUP TVMG1,\n");
			sql.append("TM_VHCL_MATERIAL_GROUP TVMG2,\n");
			sql.append("TC_CODE TC3,\n");
			sql.append("TC_CODE TC4,\n");
			sql.append("TC_USER TCU,\n");	//2012-08-20 新增下载时增加审批人字段 韩晓宇
			sql.append("(select * from TM_REGION where region_type=")
					.append(Constant.REGION_TYPE_04).append(") TMR\n");
			sql.append(" WHERE  TTS.DEALER_ID = TMD.DEALER_ID\n");
	
			if ((!"".equals(logsdate) && logsdate != null)
					|| (!"".equals(logedate) && logedate != null)) {
				sql.append(" AND TDASL.ORDER_ID(+)=TTS.ORDER_ID\n");
			}
	
			sql.append(" AND TMR.REGION_CODE(+)=TTC.TOWN \n");
			
			//sql.append(" AND TVDD.DETAIL_ID(+)=TVDM.DELIVERY_DETAIL_ID AND TVDM.VEHICLE_ID(+)=TMV.VEHICLE_ID\n");
			sql.append(" AND TVDM.VEHICLE_ID(+)=TMV.VEHICLE_ID\n");
			
			sql.append(" AND TC3.CODE_ID=TCP.PARENT_FLEET_TYPE\n");
			sql.append(" AND TC4.CODE_ID=TMV.LIFE_CYCLE \n");
			sql.append(" AND TVMG.GROUP_ID = TMV.SERIES_ID\n");
			sql.append(" AND TBA.AREA_ID = TMV.AREA_ID\n");
			sql.append(" AND TVMG1.GROUP_ID = TMV.MODEL_ID\n");
			sql.append(" AND TVMG2.GROUP_ID = TMV.PACKAGE_ID\n");
			sql.append("AND VOD.DEALER_ID = TMD.DEALER_ID\n");
			sql.append("AND TTS.CTM_ID = TTC.CTM_ID\n");
			sql.append("AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
			sql.append("AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");
			sql.append(" AND TTS.FLEET_ID = TCP.PACT_ID\n");
			sql.append("AND TTS.IS_RETURN = 10041002\n");
			//sql.append(" AND TTS.IS_FLEET = 10041001\n");
			//sql.append("AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");
			//sql.append("AND TMV.LIFE_CYCLE = 10321004\n");
//			if (!"".equals(contract_no) && contract_no != null) {
//				sql.append(" AND TFC.CONTRACT_NO ='" + contract_no + "'\n");
//			}
			if (!"".equals(fleet_name) && fleet_name != null) {
				sql.append("AND TTC.CTM_NAME ='" + fleet_name + "'\n");
			}
			if (!"".equals(customer_type) && customer_type != null) {
				sql.append(" AND TC3.CODE_ID ='" + customer_type + "'\n");
			}
			if (!"".equals(vin) && vin != null) {
				sql.append(" AND TMV.VIN LIKE '%" + vin + "%'\n");
			}
			if (!"".equals(materialCode) && materialCode != null) {
				sql.append("AND TVMG.GROUP_CODE='" + materialCode + "'\n");
			}
			if (!"".equals(companyId) && companyId != null) {
				sql.append(" AND TTS.DLR_COMPANY_ID IN(" + companyId + ")\n");
			}
			if (!"".equals(checkStatus) && checkStatus != null) {
				sql.append("AND TTS.FLEET_STATUS ='" + checkStatus + "'\n");
			}
			if (!"".equals(startDate) && startDate != null) {
				sql.append("AND TTS.SALES_DATE >= TO_DATE('" + startDate
						+ "  00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (!"".equals(endDate) && endDate != null) {
				sql.append("AND TTS.SALES_DATE  <= TO_DATE('" + endDate
						+ " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (!"".equals(logsdate) && logsdate != null) {
				sql.append("AND TDASL.LOG_DATE >=TO_DATE('" + logsdate
						+ "   00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (!"".equals(logedate) && logedate != null) {
				sql.append("AND TDASL.LOG_DATE  <= TO_DATE('" + logedate
						+ " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
			sql.append("AND TTS.UPDATE_BY = TCU.USER_ID(+)\n");	//2012-08-20 新增下载时增加审批人字段 韩晓宇
			if (!"".equals(orgId) && orgId != null) {
				sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
			}
		}
		
		sql.append("\n");
		sql.append("\n");
		sql.append(") XX\n");
		//sql.append("WHERE (XX.IS_FLEET=10041001 AND XX.FLEET_NAME IS NOT NULL) OR XX.IS_FLEET=10041002\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
//	/*
//	 * 集团客户实销信息下载
//	 */
//	public List<Map<String, Object>> queryReportInfoExportList(Map<String,String> map) {
//		String customer_type = (String) map.get("customer_type"); 	// 客户类型
//		String vin = (String) map.get("vin"); 						// VIN
//		String materialCode = (String) map.get("materialCode"); 	// 选择物料组
//		String fleet_name = (String) map.get("fleet_name"); 		// 集团客户名称
//		String contract_no = (String) map.get("contract_no"); 		// 集团客户合同
//		String startDate = (String) map.get("startDate"); 			// 上报日期:开始
//		String endDate = (String) map.get("endDate"); 				// 上报日期 ：截止
//		String checkStatus=(String) map.get("checkStatus");//审核状态
//		String companyId=(String)map.get("companyId");//经销商
//		String logsdate=(String)map.get("logsdate");//审核开始日期
//		String logedate=(String)map.get("logedate");//审核结束日期
//		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT       XX.DEALER_CODE, --经销商代码\n");
//		sql.append("XX.ROOT_ORG_NAME, --大区\n");
//        sql.append("XX.ROOT_DEALER_NAME,--上级单位\n");
//        sql.append("XX.ROOT_DEALER_ID, --上级单位\n");
//        sql.append("XX.REGION_NAME, --省份\n");
//        sql.append("XX.CITY_NAME, --城市\n");
//        sql.append("XX.TOWN, --省份\n");
//        sql.append("XX.AREA_NAME, --业务范围\n");
//        sql.append("TO_CHAR(XX.SALES_DATE,'YYYY-MM-DD') SALES_DATE, --销售日期\n");
//        sql.append("TO_CHAR(XX.PRODUCT_DATE,'YYYY-MM-DD') PRODUCT_DATE, --生产日期\n");
//        sql.append("TO_CHAR(XX.INVOICE_DATE,'YYYY-MM-DD') INVOICE_DATE, --开票日期\n");
//        sql.append("XX.DEALER_SHORTNAME, --销售单位\n");
//        sql.append("XX.CTM_NAME, --客户名称\n");
//        sql.append("XX.CTM_TYPE, --客户类型\n");
//        sql.append("XX.IS_FLEET, --是否集团客户\n");
//        sql.append("XX.FLEET_NAME, --集团客户名称\n");
//        sql.append("XX.CONTRACT_NO, --集团客户合同\n");
//        sql.append("XX.SERNAME, --车型系列\n");
//        sql.append("XX.MODNAME, --车型类别\n");
//        sql.append("XX.MODCODE, --车型编码\n");
//        sql.append("XX.PACKNAME, --状态\n");
//        sql.append("XX.PARTFLEET_TYPE,\n");
//        sql.append("XX.FLEET_TYPE,\n");
//        sql.append("XX.PARTFLEET_TYPE,\n");
//        sql.append("XX.COLOR,\n");
//        sql.append("XX.VIN\n");
//        sql.append("FROM (SELECT TMD.DEALER_CODE, --经销商代码\n");
//		sql.append("VOD.ROOT_ORG_NAME, --大区\n");
//        sql.append("VOD.ROOT_DEALER_NAME,--上级单位\n");
//        sql.append("VOD.ROOT_DEALER_ID, --上级单位\n");
//        sql.append("VOD.REGION_NAME, --省份\n");
//        sql.append(" VOD.CITY_NAME, --城市\n");
//        sql.append("TMR.REGION_NAME TOWN, --省份\n");
//        sql.append(" TBA.AREA_NAME, --业务范围\n");
//        sql.append(" TTS.SALES_DATE, --销售日期\n");
//        sql.append(" TMV.PRODUCT_DATE, --生产日期\n");
//        sql.append("TTS.INVOICE_DATE, --开票日期\n");
//        sql.append(" TMD.DEALER_SHORTNAME, --销售单位\n");
//        sql.append(" TTC.CTM_NAME, --客户名称\n");
//        sql.append("TC1.CODE_DESC CTM_TYPE, --客户类型\n");
//        sql.append("TTS.IS_FLEET, --是否集团客户\n");
//        sql.append("TF.FLEET_NAME, --集团客户名称\n");
//        sql.append("  TFC.CONTRACT_NO, --集团客户合同\n");
//        sql.append(" TVMG.GROUP_NAME SERNAME, --车型系列\n");
//        sql.append(" TVMG1.GROUP_NAME MODNAME, --车型类别\n");
//        sql.append(" TVMG1.GROUP_CODE MODCODE, --车型编码\n");
//        sql.append(" TVMG2.GROUP_NAME PACKNAME, --状态\n");
//        sql.append(" TC2.CODE_DESC PARTFLEET_TYPE,\n");
//        sql.append(" TC2.CODE_DESC FLEET_TYPE,\n");
//        sql.append(" TMV.COLOR,\n");
//        sql.append(" TMV.VIN\n");
//        sql.append("FROM TT_DEALER_ACTUAL_SALES TTS,\n");
//        sql.append(" TT_DEALER_ACTUAL_SALES_LOG TDASL,\n");
//        sql.append(" TM_DEALER              TMD,\n");
//        sql.append(" TT_CUSTOMER            TTC,\n");
//        sql.append("TM_VEHICLE             TMV,\n");
//        sql.append("TM_VHCL_MATERIAL       M,\n");
//        sql.append("TM_BUSINESS_AREA       TBA,\n");
//        sql.append("TM_FLEET               TF,\n");
//        sql.append("TT_FLEET_CONTRACT      TFC,\n");
//        sql.append("vw_org_dealer          VOD,\n");
//        sql.append("TM_VHCL_MATERIAL_GROUP TVMG,\n");
//        sql.append("TM_VHCL_MATERIAL_GROUP TVMG1,\n");
//        sql.append("TM_VHCL_MATERIAL_GROUP TVMG2,\n");
//        sql.append("TC_CODE TC1,\n");
//        sql.append("TC_CODE TC2,\n");
//        sql.append("TM_REGION TMR\n");
//        sql.append("WHERE TTS.DEALER_ID = TMD.DEALER_ID\n");
//    sql.append("AND TDASL.ORDER_ID(+)=TTS.ORDER_ID\n");
//    sql.append(" AND TC2.CODE_ID=TF.FLEET_TYPE\n");
//    sql.append("AND TC1.CODE_ID=TTC.CTM_TYPE\n");
//    sql.append("AND TVMG.GROUP_ID = TMV.SERIES_ID\n");
//    sql.append("AND TVMG1.GROUP_ID = TMV.MODEL_ID\n");
//    sql.append("AND TVMG2.GROUP_ID = TMV.PACKAGE_ID\n");
//    sql.append("AND TBA.AREA_ID(+) = TMV.AREA_ID\n");
//    sql.append("AND VOD.DEALER_ID = TMD.DEALER_ID\n");
//    sql.append("AND TTS.CTM_ID = TTC.CTM_ID\n");
//    sql.append("AND TMR.REGION_CODE=TTC.TOWN\n");
//    sql.append("AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
//    sql.append("AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");
//    sql.append("AND TTS.FLEET_ID = TF.FLEET_ID(+)\n");
//    sql.append("AND TTS.IS_RETURN = '10041002'\n");
//    sql.append("AND TTS.IS_FLEET = 10041001\n");
//    sql.append("AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");
//    sql.append("AND TMV.LIFE_CYCLE = 10321004\n");
//    if(!"".equals(contract_no)&&contract_no!=null){
//    sql.append("AND TFC.CONTRACT_NO ='"+contract_no+"'\n");
//    }
//    if(!"".equals(fleet_name)&&fleet_name!=null){
//    sql.append("AND TTC.CTM_NAME ='"+fleet_name+"'\n");
//    }
//    if(!"".equals(customer_type)&&customer_type!=null){
//    sql.append("AND TF.FLEET_TYPE ='"+customer_type+"'\n");
//    }
//    if(!"".equals(vin)&&vin!=null){
//    sql.append("AND TMV.VIN LIKE '%"+vin+"%'\n");
//    }
//    if(!"".equals(materialCode)&&materialCode!=null){
//    sql.append("AND TVMG.GROUP_CODE='"+materialCode+"'\n");
//    }
//    if(!"".equals(companyId)&&companyId!=null){
//    sql.append(" AND TTS.DLR_COMPANY_ID IN("+companyId+")\n");
//    }
//    if(!"".equals(checkStatus)&&checkStatus!=null){
//    sql.append("AND TTS.FLEET_STATUS ='"+checkStatus+"'\n");
//    }
//    if(!"".equals(startDate)&&startDate!=null){
//    sql.append("AND TTS.SALES_DATE >= TO_DATE('"+startDate+"','yyyy-MM-dd HH24:MI:SS')\n");
//    }
//    if(!"".equals(endDate)&&endDate!=null){
//    sql.append("AND TTS.SALES_DATE  <= TO_DATE('"+endDate+"','yyyy-MM-dd HH24:MI:SS')\n");
//    }
//    if(!"".equals(logsdate)&&logsdate!=null){
//    sql.append("AND TDASL.LOG_DATE >=TO_DATE('"+logsdate+"','yyyy-MM-dd HH24:MI:SS')\n");
//    }
//    if(!"".equals(logedate)&&logedate!=null){
//    sql.append("AND TDASL.LOG_DATE  <= TO_DATE('"+logedate+"','yyyy-MM-dd HH24:MI:SS')\n");
//    }
//    sql.append("\n");
// sql.append("UNION\n");
// sql.append("SELECT DISTINCT  TMD.DEALER_CODE, --经销商代码\n");
// sql.append("VOD.ROOT_ORG_NAME, --大区\n");
//        sql.append("VOD.ROOT_DEALER_NAME,--上级单位\n");
//        sql.append("VOD.ROOT_DEALER_ID, --上级单位\n");
//		sql.append("VOD.REGION_NAME, --省份\n");
//        sql.append(" VOD.CITY_NAME, --城市\n");
//        sql.append(" TMR.REGION_NAME TOWN, --省份\n");
//        sql.append(" TBA.AREA_NAME, --业务范围\n");
//        sql.append(" TTS.SALES_DATE, --销售日期\n");
//        sql.append(" TMV.PRODUCT_DATE, --生产日期\n");
//        sql.append("TTS.INVOICE_DATE, --开票日期\n");
//        sql.append(" TMD.DEALER_SHORTNAME, --销售单位\n");
//        sql.append(" TTC.CTM_NAME, --客户名称\n");
//        sql.append(" TC2.CODE_DESC CTM_TYPE, --客户类型\n");
//        sql.append(" TTS.IS_FLEET, --是否集团客户\n");
//        sql.append("TCP.PACT_NAME FLEET_NAME, --集团客户名称\n");
//        sql.append("TFC.CONTRACT_NO CONTRACT_NO, --集团客户合同\n");
//        sql.append("TVMG.GROUP_NAME SERNAME, --车型系列\n");
//        sql.append("TVMG1.GROUP_NAME MODNAME, --车型类别\n");
//        sql.append(" TVMG1.GROUP_CODE MODCODE, --车型编码\n");
//        sql.append(" TVMG2.GROUP_NAME PACKNAME, --状态\n");
//        sql.append(" TC3.CODE_DESC PARTFLEET_TYPE,\n");
//        sql.append("TCP.PACT_NAME FLEET_TYPE,\n");
//        sql.append("TMV.COLOR,\n");
//        sql.append("TMV.VIN\n");
//        sql.append("FROM TT_DEALER_ACTUAL_SALES TTS,\n");
//   sql.append("TT_DEALER_ACTUAL_SALES_LOG TDASL,\n");
//        sql.append("TM_DEALER              TMD,\n");
//sql.append("TT_CUSTOMER            TTC,\n");
//        sql.append("TM_VEHICLE             TMV,\n");
//        sql.append("TM_VHCL_MATERIAL       M,\n");
//        sql.append("TM_FLEET               TF,\n");
//        sql.append(" TT_FLEET_CONTRACT      TFC,\n");
//        sql.append("TM_COMPANY_PACT        TCP,\n");
//        sql.append("vw_org_dealer          VOD,\n");
//        sql.append("TM_BUSINESS_AREA       TBA,\n");
//        sql.append("TM_VHCL_MATERIAL_GROUP TVMG,\n");
//        sql.append("TM_VHCL_MATERIAL_GROUP TVMG1,\n");
//        sql.append("TM_VHCL_MATERIAL_GROUP TVMG2,\n");
//        sql.append("TC_CODE TC2,\n");
//        sql.append("TC_CODE TC3,\n");
//        sql.append("TM_REGION TMR\n");
//        sql.append(" WHERE  TTS.DEALER_ID = TMD.DEALER_ID\n");
//    sql.append(" AND TDASL.ORDER_ID(+)=TTS.ORDER_ID\n");
//    sql.append(" AND TMR.REGION_CODE=TTC.TOWN\n");
//    sql.append(" AND TC3.CODE_ID=TCP.PARENT_FLEET_TYPE\n");
//    sql.append(" AND TC2.CODE_ID=TTC.CTM_TYPE\n");
//    sql.append(" AND TVMG.GROUP_ID = TMV.SERIES_ID\n");
//    sql.append(" AND TBA.AREA_ID(+) = TMV.AREA_ID\n");
//    sql.append(" AND TVMG1.GROUP_ID = TMV.MODEL_ID\n");
//    sql.append(" AND TVMG2.GROUP_ID = TMV.PACKAGE_ID\n");
//    sql.append("AND VOD.DEALER_ID = TMD.DEALER_ID\n");
//	sql.append("AND TTS.CTM_ID = TTC.CTM_ID\n");
//    sql.append("AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
//    sql.append("AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");
//    sql.append(" AND TTS.FLEET_ID = TCP.PACT_ID\n");
//    sql.append("AND TTS.IS_RETURN = '10041002'\n");
//    sql.append(" AND TTS.IS_FLEET = 10041001\n");
//    sql.append("AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");
//    sql.append("AND TMV.LIFE_CYCLE = 10321004\n");
//    if(!"".equals(contract_no)&&contract_no!=null){
//    sql.append(" AND TFC.CONTRACT_NO ='"+contract_no+"'\n");
//    }
//    if(!"".equals(fleet_name)&&fleet_name!=null){
//    sql.append("AND TTC.CTM_NAME ='"+fleet_name+"'\n");
//    }
//    if(!"".equals(customer_type)&&customer_type!=null){
//    sql.append(" AND TC3.CODE_ID ='"+customer_type+"'\n");
//    }
//    if(!"".equals(vin)&&vin!=null){
//    sql.append(" AND TMV.VIN LIKE '%"+vin+"%'\n");
//    }
//    if(!"".equals(materialCode)&&materialCode!=null){
//    sql.append("AND TVMG.GROUP_CODE='"+materialCode+"'\n");
//    }
//    if(!"".equals(companyId)&&companyId!=null){
//    sql.append(" AND TTS.DLR_COMPANY_ID IN("+companyId+")\n");
//    }
//    if(!"".equals(checkStatus)&&checkStatus!=null){
//    sql.append("AND TTS.FLEET_STATUS ='"+checkStatus+"'\n");
//    }
//    if(!"".equals(startDate)&&startDate!=null){
//    sql.append("AND TTS.SALES_DATE >= TO_DATE('"+startDate+"','yyyy-MM-dd HH24:MI:SS')\n");
//    }
//    if(!"".equals(endDate)&&endDate!=null){
//    sql.append("AND TTS.SALES_DATE  <= TO_DATE('"+endDate+"','yyyy-MM-dd HH24:MI:SS')\n");
//    }
//    if(!"".equals(logsdate)&&logsdate!=null){
//    sql.append("AND TDASL.LOG_DATE >=TO_DATE('"+logsdate+"','yyyy-MM-dd HH24:MI:SS')\n");
//    }
//    if(!"".equals(logedate)&&logedate!=null){
//    sql.append("AND TDASL.LOG_DATE  <= TO_DATE('"+logedate+"','yyyy-MM-dd HH24:MI:SS')\n");
//    }
//    sql.append("\n");
//    sql.append("\n");
//    sql.append(") XX\n");
//    sql.append("WHERE (XX.IS_FLEET=10041001 AND XX.FLEET_NAME IS NOT NULL) OR XX.IS_FLEET=10041002\n");
//		return dao.pageQuery(sql.toString(), null, dao.getFunName());
//	}
	
	/*
	 * 集团客户实销信息下载
	 */
	public PageResult<Map<String, Object>>  queryReportInfoList(Map<String,String> map,int curPage,int pageSize) {
		String customer_type = (String) map.get("customer_type"); 	// 客户类型
		String vin = (String) map.get("vin"); 						// VIN
		String materialCode = (String) map.get("materialCode"); 	// 选择物料组
		String fleet_name = (String) map.get("fleet_name"); 		// 集团客户名称
		String contract_no = (String) map.get("contract_no"); 		// 集团客户合同
		String startDate = (String) map.get("startDate"); 			// 上报日期:开始
		String endDate = (String) map.get("endDate"); 				// 上报日期 ：截止
		String checkStatus=(String) map.get("checkStatus");//审核状态
		String companyId=(String)map.get("companyId");//经销商
		String logsdate=(String)map.get("logsdate");//审核开始日期
		String logedate=(String)map.get("logedate");//审核结束日期
		String orgId=(String)map.get("orgId");//大区
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT /*+ first_rows */ XX.DEALER_CODE, --经销商代码\n");
		sql.append("XX.ROOT_ORG_NAME, --大区\n");
        sql.append("XX.ROOT_DEALER_NAME,--上级单位\n");
        sql.append("XX.ROOT_DEALER_ID, --上级单位\n");
        sql.append("XX.REGION_NAME, --省份\n");
        sql.append("XX.CITY_NAME, --城市\n");
        sql.append("XX.TOWN,XX.FLEET_STATUS, --省份\n");
        sql.append("XX.AREA_NAME, --业务范围\n");
        sql.append("TO_CHAR(XX.SALES_DATE,'YYYY-MM-DD') SALES_DATE, --销售日期\n");
        sql.append("TO_CHAR(XX.PRODUCT_DATE,'YYYY-MM-DD') PRODUCT_DATE, --生产日期\n");
        sql.append("TO_CHAR(XX.INVOICE_DATE,'YYYY-MM-DD') INVOICE_DATE, --开票日期\n");
        sql.append("XX.DEALER_SHORTNAME, --销售单位\n");
        sql.append("XX.CTM_NAME, --客户名称\n");
        sql.append("XX.MAIN_PHONE,\n");
        sql.append("XX.CTM_TYPE, --客户类型\n");
        sql.append("XX.IS_FLEET, --是否集团客户\n");
        sql.append("XX.FLEET_NAME, --集团客户名称\n");
        sql.append("XX.CONTRACT_NO, --集团客户合同\n");
        sql.append("XX.SERNAME, --车型系列\n");
        sql.append("XX.MODNAME, --车型类别\n");
        sql.append("XX.MODCODE, --车型编码\n");
        sql.append("XX.PACKNAME,XX.PACKCODE, --状态\n");
        sql.append("XX.PARTFLEET_TYPE,\n");
        sql.append("XX.FLEET_TYPE,\n");
        sql.append("XX.FLEET_ID,\n");
        sql.append("XX.COLOR,\n");
        sql.append("TO_CHAR(XX.Log_Date,'YYYY-MM-DD') Log_Date,--审核时间\n");  //YH 2011.6.7

        sql.append("xx.auditName,\n") ;
        sql.append("xx.remark,") ;

        sql.append("XX.VIN\n");
        sql.append("FROM (SELECT /*+ first_rows */ TMD.DEALER_CODE, --经销商代码\n");
		sql.append("VOD.ROOT_ORG_NAME, --大区\n");
        sql.append("VOD.ROOT_DEALER_NAME,--上级单位\n");
        sql.append("VOD.ROOT_DEALER_ID, --上级单位\n");
        sql.append("VOD.REGION_NAME, --省份\n");
        sql.append(" VOD.CITY_NAME, --城市\n");
        sql.append("TMR.REGION_NAME TOWN,TTS.FLEET_STATUS, --省份\n");
        sql.append(" TBA.AREA_NAME, --业务范围\n");
        sql.append(" TTS.SALES_DATE, --销售日期\n");
        sql.append(" TMV.PRODUCT_DATE, --生产日期\n");
        sql.append("TTS.INVOICE_DATE, --开票日期\n");
        sql.append(" TMD.DEALER_SHORTNAME, --销售单位\n");
        sql.append(" TTC.CTM_NAME, --客户名称\n");
        sql.append("TTC.MAIN_PHONE,\n");
        sql.append("TC1.CODE_DESC CTM_TYPE, --客户类型\n");
        sql.append("TTS.IS_FLEET, --是否集团客户\n");
        sql.append("TF.FLEET_ID,TF.FLEET_NAME, --集团客户名称\n");
        sql.append("  TFC.CONTRACT_NO, --集团客户合同\n");
        sql.append(" TVMG.GROUP_NAME SERNAME, --车型系列\n");
        sql.append(" TVMG1.GROUP_NAME MODNAME, --车型类别\n");
        sql.append(" TVMG1.GROUP_CODE MODCODE, --车型编码\n");
        sql.append(" TVMG2.GROUP_NAME PACKNAME, TVMG2.GROUP_CODE PACKCODE, --状态\n");
        sql.append(" TC2.CODE_DESC PARTFLEET_TYPE,\n");
        sql.append(" TC2.CODE_DESC FLEET_TYPE,\n");
        sql.append(" TMV.COLOR,\n");
        sql.append(" TDASL.Log_Date Log_Date, --审核时间\n");

        sql.append("tcu.name             auditName,\n") ;
        sql.append("         tdasl.remark,") ;

        sql.append(" TMV.VIN\n");
        sql.append("FROM TT_DEALER_ACTUAL_SALES TTS,\n");
        sql.append(" TT_DEALER_ACTUAL_SALES_LOG TDASL,\n");
        sql.append(" TM_DEALER              TMD,\n");
        sql.append(" TT_CUSTOMER            TTC,\n");
        sql.append("TM_VEHICLE             TMV,\n");
        sql.append("TM_VHCL_MATERIAL       M,\n");
        sql.append("TM_BUSINESS_AREA       TBA,\n");
        sql.append("TM_FLEET               TF,\n");
        sql.append("TT_FLEET_CONTRACT      TFC,\n");
        sql.append("vw_org_dealer          VOD,\n");
        sql.append("TM_VHCL_MATERIAL_GROUP TVMG,\n");
        sql.append("TM_VHCL_MATERIAL_GROUP TVMG1,\n");
        sql.append("TM_VHCL_MATERIAL_GROUP TVMG2,\n");
        sql.append("TC_CODE TC1,\n");
        sql.append("TC_CODE TC2,\n");

        sql.append("tc_user tcu,") ;

        sql.append("(select region_code, region_name from TM_REGION where region_type=").append(Constant.REGION_TYPE_04).append(") TMR\n");
        sql.append("WHERE TTS.DEALER_ID = TMD.DEALER_ID\n");
    sql.append("AND TDASL.ORDER_ID(+)=TTS.ORDER_ID\n");
    sql.append(" AND TC2.CODE_ID=TF.FLEET_TYPE\n");
    sql.append("AND TC1.CODE_ID=TTC.CTM_TYPE\n");
    sql.append("AND TVMG.GROUP_ID = TMV.SERIES_ID\n");
    sql.append("AND TVMG1.GROUP_ID = TMV.MODEL_ID\n");
    sql.append("AND TVMG2.GROUP_ID = TMV.PACKAGE_ID\n");
    sql.append("AND TBA.AREA_ID = TMV.AREA_ID\n");
    sql.append("AND VOD.DEALER_ID = TMD.DEALER_ID\n");
    sql.append("AND TTS.CTM_ID = TTC.CTM_ID\n");
    sql.append("AND TMR.REGION_CODE(+)=TTC.TOWN\n");
    sql.append("AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
    sql.append("AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");
    sql.append("AND TTS.FLEET_ID = TF.FLEET_ID(+)\n");

    sql.append("and tts.update_by = tcu.user_id(+)") ;

    sql.append("AND TTS.IS_RETURN = 10041002\n");
    //sql.append("AND TTS.IS_FLEET = 10041001\n");
    sql.append("AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");
    //sql.append("AND TMV.LIFE_CYCLE = 10321004\n");
    if(!"".equals(contract_no)&&contract_no!=null){
    sql.append("AND TFC.CONTRACT_NO ='"+contract_no+"'\n");
    }
    if(!"".equals(fleet_name)&&fleet_name!=null){
    sql.append("AND TTC.CTM_NAME ='"+fleet_name+"'\n");
    }
    if(!"".equals(customer_type)&&customer_type!=null){
    sql.append("AND TF.FLEET_TYPE ='"+customer_type+"'\n");
    }
    if(!"".equals(vin)&&vin!=null){
    sql.append("AND TMV.VIN LIKE '%"+vin+"%'\n");
    }
    if(!"".equals(materialCode)&&materialCode!=null){
    sql.append("AND TVMG.GROUP_CODE='"+materialCode+"'\n");
    }
    if(!"".equals(companyId)&&companyId!=null){
    sql.append(" AND TTS.DLR_COMPANY_ID IN("+companyId+")\n");
    }
    if(!"".equals(checkStatus)&&checkStatus!=null){
    sql.append("AND TTS.FLEET_STATUS ='"+checkStatus+"'\n");
    }
    if(!"".equals(startDate)&&startDate!=null){
    sql.append("AND TTS.SALES_DATE >= TO_DATE('"+startDate+"  00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
    }
    if(!"".equals(endDate)&&endDate!=null){
    sql.append("AND TTS.SALES_DATE  <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
    }
    if(!"".equals(logsdate)&&logsdate!=null){
    sql.append("AND TDASL.LOG_DATE >=TO_DATE('"+logsdate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
    }
    if(!"".equals(logedate)&&logedate!=null){
    sql.append("AND TDASL.LOG_DATE  <= TO_DATE('"+logedate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
    }
    if(!"".equals(orgId)&&orgId!=null){
    	 sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
    }
    sql.append("\n");
		 sql.append("UNION\n");
		 sql.append("SELECT /*+ first_rows */ TMD.DEALER_CODE, --经销商代码\n");
		 sql.append("VOD.ROOT_ORG_NAME, --大区\n");
        sql.append("VOD.ROOT_DEALER_NAME,--上级单位\n");
        sql.append("VOD.ROOT_DEALER_ID, --上级单位\n");
		sql.append("VOD.REGION_NAME, --省份\n");
        sql.append(" VOD.CITY_NAME, --城市\n");
        sql.append(" TMR.REGION_NAME TOWN,TTS.FLEET_STATUS, --省份\n");
        sql.append(" TBA.AREA_NAME, --业务范围\n");
        sql.append(" TTS.SALES_DATE, --销售日期\n");
        sql.append(" TMV.PRODUCT_DATE, --生产日期\n");
        sql.append("TTS.INVOICE_DATE, --开票日期\n");
        sql.append(" TMD.DEALER_SHORTNAME, --销售单位\n");
        sql.append(" TTC.CTM_NAME, --客户名称\n");
        sql.append("TTC.MAIN_PHONE,\n");
        sql.append(" TC2.CODE_DESC CTM_TYPE, --客户类型\n");
        sql.append(" TTS.IS_FLEET, --是否集团客户\n");
        sql.append("TCP.PACT_ID FLEET_ID,TCP.PACT_NAME FLEET_NAME, --集团客户名称\n");
        sql.append("TFC.CONTRACT_NO CONTRACT_NO, --集团客户合同\n");
        sql.append("TVMG.GROUP_NAME SERNAME, --车型系列\n");
        sql.append("TVMG1.GROUP_NAME MODNAME, --车型类别\n");
        sql.append(" TVMG1.GROUP_CODE MODCODE, --车型编码\n");
        sql.append(" TVMG2.GROUP_NAME PACKNAME, TVMG2.GROUP_CODE PACKCODE, --状态\n");
        sql.append(" TC3.CODE_DESC PARTFLEET_TYPE,\n");
        sql.append("TCP.PACT_NAME FLEET_TYPE,\n");
        sql.append("TMV.COLOR,\n");
        sql.append("TDASL.Log_Date Log_Date, --审核时间\n");

        sql.append("tcu.name             auditName,\n") ;
        sql.append("         tdasl.remark,") ;

        sql.append("TMV.VIN\n");
        sql.append("FROM TT_DEALER_ACTUAL_SALES TTS,\n");
        sql.append("TT_DEALER_ACTUAL_SALES_LOG TDASL,\n");
        sql.append("TM_DEALER              TMD,\n");
        sql.append("TT_CUSTOMER            TTC,\n");
        sql.append("TM_VEHICLE             TMV,\n");
        sql.append("TM_VHCL_MATERIAL       M,\n");
        //sql.append("TM_FLEET               TF,\n");
        sql.append(" TT_FLEET_CONTRACT      TFC,\n");
        sql.append("TM_COMPANY_PACT        TCP,\n");
        sql.append("vw_org_dealer          VOD,\n");
        sql.append("TM_BUSINESS_AREA       TBA,\n");
        sql.append("TM_VHCL_MATERIAL_GROUP TVMG,\n");
        sql.append("TM_VHCL_MATERIAL_GROUP TVMG1,\n");
        sql.append("TM_VHCL_MATERIAL_GROUP TVMG2,\n");
        sql.append("TC_CODE TC2,\n");
        sql.append("TC_CODE TC3,\n");

        sql.append("tc_user tcu,") ;

        sql.append("(select region_code, region_name from TM_REGION where region_type=").append(Constant.REGION_TYPE_04).append(") TMR\n");
        sql.append(" WHERE  TTS.DEALER_ID = TMD.DEALER_ID\n");
        sql.append(" AND TDASL.ORDER_ID(+)=TTS.ORDER_ID\n");
        sql.append(" AND TMR.REGION_CODE(+)=TTC.TOWN\n");
        sql.append(" AND TC3.CODE_ID=TCP.PARENT_FLEET_TYPE\n");
	    sql.append(" AND TC2.CODE_ID=TTC.CTM_TYPE\n");
	    sql.append(" AND TVMG.GROUP_ID = TMV.SERIES_ID\n");
	    sql.append(" AND TBA.AREA_ID(+) = TMV.AREA_ID\n");
	    sql.append(" AND TVMG1.GROUP_ID = TMV.MODEL_ID\n");
	    sql.append(" AND TVMG2.GROUP_ID = TMV.PACKAGE_ID\n");
	    sql.append("AND VOD.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("AND TTS.CTM_ID = TTC.CTM_ID\n");
	    sql.append("AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
	    sql.append("AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");
	    sql.append(" AND TTS.FLEET_ID = TCP.PACT_ID\n");

	    sql.append("and tts.update_by = tcu.user_id(+)") ;

	    sql.append("AND TTS.IS_RETURN = 10041002\n");
	    //sql.append(" AND TTS.IS_FLEET = 10041001\n");
	    sql.append("AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");
	    //sql.append("AND TMV.LIFE_CYCLE = 10321004\n");
    if(!"".equals(contract_no)&&contract_no!=null){
    sql.append(" AND TFC.CONTRACT_NO ='"+contract_no+"'\n");
    }
    if(!"".equals(fleet_name)&&fleet_name!=null){
    sql.append("AND TTC.CTM_NAME ='"+fleet_name+"'\n");
    }
    if(!"".equals(customer_type)&&customer_type!=null){
    sql.append(" AND TC3.CODE_ID ='"+customer_type+"'\n");
    }
    if(!"".equals(vin)&&vin!=null){
    sql.append(" AND TMV.VIN LIKE '%"+vin+"%'\n");
    }
    if(!"".equals(materialCode)&&materialCode!=null){
    sql.append("AND TVMG.GROUP_CODE='"+materialCode+"'\n");
    }
    if(!"".equals(companyId)&&companyId!=null){
    sql.append(" AND TTS.DLR_COMPANY_ID IN("+companyId+")\n");
    }
    if(!"".equals(checkStatus)&&checkStatus!=null){
    sql.append("AND TTS.FLEET_STATUS ='"+checkStatus+"'\n");
    }
    if(!"".equals(startDate)&&startDate!=null){
    sql.append("AND TTS.SALES_DATE >= TO_DATE('"+startDate+"  00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
    }
    if(!"".equals(endDate)&&endDate!=null){
    sql.append("AND TTS.SALES_DATE  <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
    }
    if(!"".equals(logsdate)&&logsdate!=null){
    sql.append("AND TDASL.LOG_DATE >=TO_DATE('"+logsdate+"   00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
    }
    if(!"".equals(logedate)&&logedate!=null){
    sql.append("AND TDASL.LOG_DATE  <= TO_DATE('"+logedate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
    }
    if(!"".equals(orgId)&&orgId!=null){
   	 sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
   }
    sql.append("\n");
    sql.append("\n");
    sql.append(") XX\n");
//    sql.append("WHERE (XX.IS_FLEET=10041001 AND XX.FLEET_NAME IS NOT NULL) OR XX.IS_FLEET=10041002\n");
    PageResult<Map<String, Object>> rs =dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
    return rs;	
	}
	
	
	/**
	 * Function         : 集团客户实销信息审核查询
	 * @param           : 客户名称
	 * @param           : 客户类型
	 * @param           : 起始时间
	 * @param           : 结束时间
	 * @param           : 经销商公司ID
	 * @param           : 经销商公司ID
	 * @param           : 经销商公司ID
	 * @param           : 分页参数
	 * @return          : 集团客户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-22
	 */
	public PageResult<Map<String, Object>> fleetSalesInfoCheckQueryList(String dutyType, String orgId, String oemCompanyId, String logsdate, String logedate,String fleetName,String fleetType,String startDate,String endDate,String companyId,String groupCode,String contractNo,String vin,String checkStatus,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		if(null == fleetType) {
			fleetType = "" ;
		}
		if("".equals(fleetType)) {
			sql.append("SELECT DISTINCT CP.PACT_ID FLEET_ID,\n" );
			sql.append("       CP.PACT_NAME FLEET_NAME,\n" );
			sql.append("       -1 FLEET_TYPE,\n" );
			sql.append("       CP.PACT_NAME CONTRACT_NO,\n" );
			sql.append("       CP.PACT_ID CONTRACT_ID,\n" );
			sql.append("       TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
			sql.append("       TVMG.GROUP_CODE MATERIAL_CODE,\n" );
			sql.append("       TVMG.GROUP_NAME MATERIAL_NAME,\n" );
			sql.append("       TVM.COLOR_NAME,\n" );
			sql.append("       TMV.VIN,\n" );
			sql.append("       TDAS.ORDER_ID,TDAS.FLEET_STATUS,\n" );
			sql.append("       TO_CHAR(TDAS.SALES_DATE, 'YYYY-MM-DD') SALES_DATE\n" );
			sql.append("  FROM TM_COMPANY             TMC,\n" );
			sql.append("       TT_DEALER_ACTUAL_SALES TDAS,\n" );
			sql.append("       TM_VEHICLE             TMV,\n" );
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );
			sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
			sql.append("       TM_VHCL_MATERIAL       TVM,\n" );
			sql.append("       TM_COMPANY_PACT CP,\n" );
			//sql.append("	   TT_DEALER_ACTUAL_SALES_LOG TDASL,");
			sql.append("       tm_dealer A, vw_org_dealer VOD , tm_dealer_org_relation C,TM_ORG B\n");
			sql.append(" WHERE CP.PACT_ID = TDAS.FLEET_ID\n" );
			sql.append("   AND B.ORG_ID=VOD.ROOT_ORG_ID\n");
			sql.append("   AND TMC.COMPANY_ID = TDAS.DLR_COMPANY_ID\n" );
			//sql.append("   AND TDASL.ORDER_ID=TDAS.ORDER_ID\n");
			sql.append("   AND TMV.VEHICLE_ID = TDAS.VEHICLE_ID\n" );
			sql.append("   AND TVM.MATERIAL_ID = TMV.MATERIAL_ID\n" );
			sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
			sql.append("   AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n" );
			sql.append("   AND TDAS.DEALER_ID = A.DEALER_ID\n");
			sql.append("   AND VOD.DEALER_ID = A.DEALER_ID");
			sql.append("   AND VOD.ROOT_DEALER_ID = C.DEALER_ID\n");
			sql.append("   AND C.ORG_ID = VOD.ROOT_ORG_ID\n");
			sql.append("   AND B.IS_COUNT = 0\n");
			sql.append("   AND TDAS.IS_FLEET = ").append(Constant.IF_TYPE_YES).append("\n");
			if(!"".equals(fleetName)&&fleetName!=null){
				sql.append("   AND CP.PACT_NAME LIKE '%"+fleetName+"%'\n");
			}
			if(!"".equals(startDate)&&startDate!=null&&!"".equals(endDate)&&endDate!=null){
				sql.append("   AND TDAS.SALES_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
			}
			if(!"".equals(companyId)&&companyId!=null){
				sql.append("   AND TDAS.DLR_COMPANY_ID IN ("+companyId+")\n");
			}
			if(!"".equals(groupCode)&&groupCode!=null){
				sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
			}
			if(!"".equals(contractNo)&&contractNo!=null){
				sql.append("   AND CP.PACT_NAME LIKE '%"+contractNo+"%'\n");
			}
			if (null != vin && !"".equals(vin)) {
				sql.append(GetVinUtil.getVins(vin,"TMV")); 
			}
			if(!"-1".equals(checkStatus)&&checkStatus!=null&&!"".equals(checkStatus)){
				sql.append("   AND TDAS.FLEET_STATUS ="+checkStatus+"\n");
				if(checkStatus.equals(String.valueOf(Constant.Fleet_SALES_CHECK_STATUS_02))){
					if(!"".equals(logsdate)&&logsdate!=null){
						sql.append("   AND TDASL.LOG_DATE >= TO_DATE('"+logsdate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
					}
					if(!"".equals(logedate)&&logedate!=null){
						sql.append("   AND TDASL.LOG_DATE <= TO_DATE('"+logedate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
					}
				}
			}
			if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
				sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
			}
			sql.append("UNION \n" );
		}
		sql.append("SELECT DISTINCT TMF.FLEET_ID,TMF.FLEET_NAME,TMF.FLEET_TYPE,TFC.CONTRACT_NO,\n" );
		sql.append("       TFC.CONTRACT_ID,TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("       TVMG.GROUP_CODE MATERIAL_CODE,TVMG.GROUP_NAME MATERIAL_NAME,\n" );
		sql.append("       TVM.COLOR_NAME,TMV.VIN,TDAS.ORDER_ID, TDAS.FLEET_STATUS,\n" );
		sql.append("       TO_CHAR(TDAS.SALES_DATE, 'YYYY-MM-DD') SALES_DATE\n" );
		sql.append("  FROM TM_FLEET               TMF,\n" );
		sql.append("       TT_FLEET_CONTRACT      TFC,\n" );
		sql.append("       TM_COMPANY             TMC,\n" );
		sql.append("       TT_DEALER_ACTUAL_SALES TDAS,\n" );
		sql.append("       TM_VEHICLE             TMV,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		//sql.append("TT_DEALER_ACTUAL_SALES_LOG TDASL,\n");
		sql.append("       TM_VHCL_MATERIAL       TVM, \n" );
		sql.append("       tm_dealer A,  vw_org_dealer VOD, tm_dealer_org_relation C,TM_ORG B");
		sql.append(" WHERE TMF.FLEET_ID = TDAS.FLEET_ID\n" );
		sql.append("   AND TFC.CONTRACT_ID = TDAS.CONTRACT_ID\n" );
		sql.append("   AND TMC.COMPANY_ID = TDAS.DLR_COMPANY_ID\n" );
		//sql.append("  AND TDASL.ORDER_ID=TDAS.ORDER_ID \n");
		sql.append("   AND TMV.VEHICLE_ID = TDAS.VEHICLE_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TMV.MATERIAL_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append("   AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n" );
		sql.append("   AND TMF.DLR_COMPANY_ID = A.COMPANY_ID\n");
		sql.append("   AND VOD.DEALER_ID=A.DEALER_ID \n");
		sql.append("   AND VOD.ROOT_DEALER_ID = C.DEALER_ID\n");
		sql.append("   AND C.ORG_ID = VOD.ROOT_ORG_ID\n");
		sql.append("   AND B.ORG_ID=VOD.ROOT_ORG_ID\n");
		sql.append("   AND B.IS_COUNT = 0\n");
		sql.append("   AND TDAS.IS_FLEET = ").append(Constant.IF_TYPE_YES).append("\n");
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null&&!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TDAS.SALES_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(companyId)&&companyId!=null){
			sql.append("   AND TDAS.DLR_COMPANY_ID IN ("+companyId+")\n");
		}
		if(!"".equals(contractNo)&&contractNo!=null){
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contractNo+"%'\n");
		}
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin,"TMV")); 
		}
		if(!"-1".equals(checkStatus)&&checkStatus!=null&&!"".equals(checkStatus)){
			sql.append("   AND TDAS.FLEET_STATUS ="+checkStatus+"\n");
			if(checkStatus.equals(String.valueOf(Constant.Fleet_SALES_CHECK_STATUS_02))){
				if(!"".equals(logsdate)&&logsdate!=null){
					sql.append("   AND TDASL.LOG_DATE >= TO_DATE('"+logsdate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
				}
				if(!"".equals(logedate)&&logedate!=null){
					sql.append("   AND TDASL.LOG_DATE <= TO_DATE('"+logedate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
				}
			}
		}
		if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
			sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function         : 集团客户实销信息审核查询
	 * @param           : 客户名称
	 * @param           : 客户类型
	 * @param           : 起始时间
	 * @param           : 结束时间
	 * @param           : 经销商公司ID
	 * @param           : 经销商公司ID
	 * @param           : 经销商公司ID
	 * @param           : 分页参数
	 * @return          : 集团客户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-22
	 */
	public List<Map<String, Object>>  getMyTempDownloadByOrg(String dutyType, String orgId, String oemCompanyId, String logsdate, String logedate,String fleetName,String fleetType,String startDate,String endDate,String companyId,String groupCode,String contractNo,String vin,String checkStatus) throws Exception{
		StringBuffer sql= new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		if(null == fleetType) {
			fleetType = "" ;
		}
		if("".equals(fleetType)) {
			sql.append("SELECT DISTINCT CP.PACT_ID FLEET_ID,VOD.ROOT_ORG_NAME ORG_NAME,\n" );
			sql.append("       CP.PACT_NAME FLEET_NAME,\n" );
			sql.append("       '-1' FLEET_TYPE,\n" );
			sql.append("       CP.PACT_NAME CONTRACT_NO,\n" );
			sql.append("       CP.PACT_ID CONTRACT_ID,\n" );
			sql.append("       TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
			sql.append("       TVMG.GROUP_CODE MATERIAL_CODE,\n" );
			sql.append("       TVMG.GROUP_NAME MATERIAL_NAME,\n" );
			sql.append("       TVM.COLOR_NAME,\n" );
			sql.append("       TMV.VIN,\n" );
			sql.append("       TDAS.ORDER_ID,TDAS.FLEET_STATUS,\n" );
			sql.append("       TO_CHAR(TDAS.SALES_DATE, 'YYYY-MM-DD') SALES_DATE\n" );
			sql.append("  FROM TM_COMPANY             TMC,\n" );
			sql.append("       TT_DEALER_ACTUAL_SALES TDAS,\n" );
			sql.append("       TM_VEHICLE             TMV,\n" );
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );
			sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
			sql.append("       TM_VHCL_MATERIAL       TVM,\n" );
			sql.append("       TM_COMPANY_PACT CP,\n" );
			//sql.append("	   TT_DEALER_ACTUAL_SALES_LOG TDASL,");
			sql.append("       tm_dealer A, vw_org_dealer VOD , tm_dealer_org_relation C,TM_ORG B\n");
			sql.append(" WHERE CP.PACT_ID = TDAS.FLEET_ID\n" );
			sql.append("   AND B.ORG_ID=VOD.ROOT_ORG_ID\n");
			sql.append("   AND TMC.COMPANY_ID = TDAS.DLR_COMPANY_ID\n" );
			//sql.append("   AND TDASL.ORDER_ID=TDAS.ORDER_ID\n");
			sql.append("   AND TMV.VEHICLE_ID = TDAS.VEHICLE_ID\n" );
			sql.append("   AND TVM.MATERIAL_ID = TMV.MATERIAL_ID\n" );
			sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
			sql.append("   AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n" );
			sql.append("   AND TDAS.DEALER_ID = A.DEALER_ID\n");
			sql.append("   AND VOD.DEALER_ID = A.DEALER_ID");
			sql.append("   AND VOD.ROOT_DEALER_ID = C.DEALER_ID\n");
			sql.append("   AND C.ORG_ID = VOD.ROOT_ORG_ID\n");
			sql.append("   AND B.IS_COUNT = 0\n");
			sql.append("   AND TDAS.IS_FLEET = ").append(Constant.IF_TYPE_YES).append("\n");
			if(!"".equals(fleetName)&&fleetName!=null){
				sql.append("   AND CP.PACT_NAME LIKE '%"+fleetName+"%'\n");
			}
			if(!"".equals(startDate)&&startDate!=null&&!"".equals(endDate)&&endDate!=null){
				sql.append("   AND TDAS.SALES_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
			}
			if(!"".equals(companyId)&&companyId!=null){
				sql.append("   AND TDAS.DLR_COMPANY_ID IN ("+companyId+")\n");
			}
			if(!"".equals(groupCode)&&groupCode!=null){
				sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
			}
			if(!"".equals(contractNo)&&contractNo!=null){
				sql.append("   AND CP.PACT_NAME LIKE '%"+contractNo+"%'\n");
			}
			if (null != vin && !"".equals(vin)) {
				sql.append(GetVinUtil.getVins(vin,"TMV")); 
			}
			if(!"-1".equals(checkStatus)&&checkStatus!=null&&!"".equals(checkStatus)){
				sql.append("   AND TDAS.FLEET_STATUS ="+checkStatus+"\n");
				if(checkStatus.equals(String.valueOf(Constant.Fleet_SALES_CHECK_STATUS_02))){
					if(!"".equals(logsdate)&&logsdate!=null){
						sql.append("   AND TDASL.LOG_DATE >= TO_DATE('"+logsdate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
					}
					if(!"".equals(logedate)&&logedate!=null){
						sql.append("   AND TDASL.LOG_DATE <= TO_DATE('"+logedate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
					}
				}
			}
			if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
				sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
			}
			sql.append("UNION \n" );
		}
		sql.append("SELECT DISTINCT TMF.FLEET_ID,VOD.ROOT_ORG_NAME ORG_NAME,TMF.FLEET_NAME,TC2.CODE_DESC FLEET_TYPE,TFC.CONTRACT_NO,\n" );
		sql.append("       TFC.CONTRACT_ID,TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("       TVMG.GROUP_CODE MATERIAL_CODE,TVMG.GROUP_NAME MATERIAL_NAME,\n" );
		sql.append("       TVM.COLOR_NAME,TMV.VIN,TDAS.ORDER_ID, TDAS.FLEET_STATUS,\n" );
		sql.append("       TO_CHAR(TDAS.SALES_DATE, 'YYYY-MM-DD') SALES_DATE\n" );
		sql.append("  FROM TM_FLEET               TMF,\n" );
		sql.append("       TT_FLEET_CONTRACT      TFC,\n" );
		sql.append("       TM_COMPANY             TMC,\n" );
		sql.append("       TT_DEALER_ACTUAL_SALES TDAS,\n" );
		sql.append("       TM_VEHICLE             TMV,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		//sql.append("TT_DEALER_ACTUAL_SALES_LOG TDASL,\n");
		sql.append("       TM_VHCL_MATERIAL       TVM, \n" );
		sql.append("       tm_dealer A,  vw_org_dealer VOD, tm_dealer_org_relation C,TM_ORG B,TC_CODE TC2 ");
		sql.append(" WHERE TMF.FLEET_ID = TDAS.FLEET_ID\n" );
		sql.append("   AND TFC.CONTRACT_ID = TDAS.CONTRACT_ID\n" );
		sql.append("   AND TMC.COMPANY_ID = TDAS.DLR_COMPANY_ID\n" );
		//sql.append("  AND TDASL.ORDER_ID=TDAS.ORDER_ID \n");
		sql.append("   AND TMV.VEHICLE_ID = TDAS.VEHICLE_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TMV.MATERIAL_ID AND TC2.CODE_ID=TMF.FLEET_TYPE\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append("   AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n" );
		sql.append("   AND TMF.DLR_COMPANY_ID = A.COMPANY_ID\n");
		sql.append("   AND VOD.DEALER_ID=A.DEALER_ID \n");
		sql.append("   AND VOD.ROOT_DEALER_ID = C.DEALER_ID\n");
		sql.append("   AND C.ORG_ID = VOD.ROOT_ORG_ID\n");
		sql.append("   AND B.ORG_ID=VOD.ROOT_ORG_ID\n");
		sql.append("   AND B.IS_COUNT = 0\n");
		sql.append("   AND TDAS.IS_FLEET = ").append(Constant.IF_TYPE_YES).append("\n");
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null&&!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TDAS.SALES_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(companyId)&&companyId!=null){
			sql.append("   AND TDAS.DLR_COMPANY_ID IN ("+companyId+")\n");
		}
		if(!"".equals(contractNo)&&contractNo!=null){
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contractNo+"%'\n");
		}
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin,"TMV")); 
		}
		if(!"-1".equals(checkStatus)&&checkStatus!=null&&!"".equals(checkStatus)){
			sql.append("   AND TDAS.FLEET_STATUS ="+checkStatus+"\n");
			if(checkStatus.equals(String.valueOf(Constant.Fleet_SALES_CHECK_STATUS_02))){
				if(!"".equals(logsdate)&&logsdate!=null){
					sql.append("   AND TDASL.LOG_DATE >= TO_DATE('"+logsdate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
				}
				if(!"".equals(logedate)&&logedate!=null){
					sql.append("   AND TDASL.LOG_DATE <= TO_DATE('"+logedate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
				}
			}
		}
		if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
			sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		
		List<Map<String, Object>>  rs = dao.pageQuery(sql.toString(), params, getFunName());
		return rs;
	}
	
	
	
	/**
	 * Function         : 集团客户实销信息审核查询
	 * @param           : 客户名称
	 * @param           : 客户类型
	 * @param           : 起始时间
	 * @param           : 结束时间
	 * @param           : 经销商公司ID
	 * @param           : 经销商公司ID
	 * @param           : 经销商公司ID
	 * @param           : 分页参数
	 * @return          : 集团客户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-22
	 */
	public PageResult<Map<String, Object>> fleetSalesInfoCheckSearchList(String fleetName,String fleetType,String startDate,String endDate,String companyId,String groupCode,String contractNo,String vin,String checkStatus,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		if(CommonUtils.isNullString(fleetType) && CommonUtils.isNullString(contractNo)){
			sql.append("SELECT CP.PACT_ID FLEET_ID,\n" );
			sql.append("       CP.PACT_NAME FLEET_NAME,\n" );
			sql.append("       -1 FLEET_TYPE,\n" );
			sql.append("       CP.PACT_NAME CONTRACT_NO,\n" );
			sql.append("       CP.PACT_ID CONTRACT_ID,\n" );
			sql.append("       TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
			sql.append("       TVMG.GROUP_CODE MATERIAL_CODE,\n" );
			sql.append("       TVMG.GROUP_NAME MATERIAL_NAME,\n" );
			sql.append("       TVM.COLOR_NAME,\n" );
			sql.append("       TMV.VIN,\n" );
			sql.append("       TDAS.ORDER_ID,TDAS.FLEET_STATUS,\n" );
			sql.append("       TO_CHAR(TDAS.SALES_DATE, 'YYYY-MM-DD') SALES_DATE,\n" );
			sql.append("	   TDASL.REMARK AUDIT_REMARK,\n");
			sql.append("	   TO_CHAR(TDASL.LOG_DATE, 'YYYY-MM-DD') AUDIT_DATE\n");
			sql.append("  FROM TM_COMPANY             TMC,\n" );
			sql.append("       TT_DEALER_ACTUAL_SALES TDAS,\n" );
			sql.append("       TM_VEHICLE             TMV,\n" );
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );
			sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
			sql.append("       TM_VHCL_MATERIAL       TVM,\n" );
			sql.append("	   TM_FLEET                 TMF,\n");
			sql.append("       TM_COMPANY_PACT CP,\n" );
			sql.append("	   TT_DEALER_ACTUAL_SALES_LOG TDASL\n");
			sql.append(" WHERE CP.PACT_ID = TDAS.FLEET_ID\n" );
			sql.append("   AND TMF.FLEET_ID(+) = TDAS.FLEET_ID\n");
			sql.append("   AND TMC.COMPANY_ID = TDAS.DLR_COMPANY_ID\n" );
			sql.append("   AND TMV.VEHICLE_ID = TDAS.VEHICLE_ID\n" );
			sql.append("   AND TVM.MATERIAL_ID = TMV.MATERIAL_ID\n" );
			sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
			sql.append("   AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n" );
			sql.append("   AND TDAS.IS_FLEET = ").append(Constant.IF_TYPE_YES).append("\n");
			if(!"".equals(fleetName)&&fleetName!=null){
				sql.append("   AND CP.PACT_NAME LIKE '%"+fleetName+"%'\n");
			}
			
			if(!"".equals(startDate)&&startDate!=null) {
				sql.append("   AND TDAS.SALES_DATE >= TO_DATE('");
				sql.append(startDate);
				sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
			}
			if(!"".equals(endDate)&&endDate!=null){
				sql.append(" AND TDAS.SALES_DATE <= TO_DATE('");
				sql.append(endDate);
				sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
			}
			if(!"".equals(companyId)&&companyId!=null) {
				sql.append("   AND TDAS.DLR_COMPANY_ID ="+companyId+"\n");
			}
			
			if(!"".equals(groupCode)&&groupCode!=null){
				sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
			}
			if (null != vin && !"".equals(vin)) {
				sql.append(GetVinUtil.getVins(vin,"TMV")); 
			}
			if(!"-1".equals(checkStatus)&&checkStatus!=null&&!"".equals(checkStatus)){
				sql.append("   AND TDAS.FLEET_STATUS ="+checkStatus+"\n");
			}
			sql.append("AND TDASL.ORDER_ID(+) = TDAS.ORDER_ID\n");
			sql.append("UNION ALL\n" );
		}
		sql.append("SELECT TMF.FLEET_ID,TMF.FLEET_NAME,TMF.FLEET_TYPE,TFC.CONTRACT_NO,\n" );
		sql.append("       TFC.CONTRACT_ID,TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("       TVMG.GROUP_CODE MATERIAL_CODE,TVMG.GROUP_NAME MATERIAL_NAME,\n" );
		sql.append("       TVM.COLOR_NAME,TMV.VIN,TDAS.ORDER_ID, TDAS.FLEET_STATUS,\n" );
		sql.append("       TO_CHAR(TDAS.SALES_DATE, 'YYYY-MM-DD') SALES_DATE,\n" );
		sql.append("	   TDASL.REMARK AUDIT_REMARK,\n");
		sql.append("	   TO_CHAR(TDASL.LOG_DATE, 'YYYY-MM-DD') AUDIT_DATE\n");
		sql.append("  FROM TM_FLEET               TMF,\n" );
		sql.append("       TT_FLEET_CONTRACT      TFC,\n" );
		sql.append("       TM_COMPANY             TMC,\n" );
		sql.append("       TT_DEALER_ACTUAL_SALES TDAS,\n" );
		sql.append("       TM_VEHICLE             TMV,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		sql.append("       TM_VHCL_MATERIAL       TVM,\n" );
		sql.append("       TT_DEALER_ACTUAL_SALES_LOG TDASL\n");
		sql.append(" WHERE TMF.FLEET_ID(+) = TDAS.FLEET_ID\n" );
		sql.append("   AND TFC.CONTRACT_ID = TDAS.CONTRACT_ID\n" );
		sql.append("   AND TMC.COMPANY_ID = TDAS.DLR_COMPANY_ID\n" );
		sql.append("   AND TMV.VEHICLE_ID = TDAS.VEHICLE_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TMV.MATERIAL_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append("   AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n" );
		sql.append("   AND TDAS.IS_FLEET = ").append(Constant.IF_TYPE_YES).append("\n");
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null) {
			sql.append("   AND TDAS.SALES_DATE >= TO_DATE('");
			sql.append(startDate);
			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null) {
			sql.append(" AND TDAS.SALES_DATE <= TO_DATE('");
			sql.append(endDate);
			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(companyId)&&companyId!=null){
			sql.append("   AND TDAS.DLR_COMPANY_ID ="+companyId+"\n");
		}
		if(!"".equals(contractNo)&&contractNo!=null){
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contractNo+"%'\n");
		}
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin,"TMV")); 
		}
		if(!"-1".equals(checkStatus)&&checkStatus!=null&&!"".equals(checkStatus)){
			sql.append("   AND TDAS.FLEET_STATUS ="+checkStatus+"\n");
		}
		sql.append("AND TDASL.ORDER_ID(+) = TDAS.ORDER_ID\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public String getCount(String a,String b){
		String count = "";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) COUNT\n" );
		sql.append("FROM TT_FLEET_CONTRACT\n" );
		sql.append("WHERE FLEET_ID = ").append(a).append("\n");

		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		count = String.valueOf(map.get("COUNT"));
		return count;
	}
	
    public List<Map<String, Object>> queryAttachFileInfo(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.FJID, A.FILEURL, A.FILENAME, A.FILEID FROM FS_FILEUPLOAD A ");
		sql.append(" WHERE A.YWZJ='"+id+"'");
		List<Map<String, Object>> ls= pageQuery(sql.toString(), null, getFunName());
		return ls;
	}
    /*
     * 查询所有的集团客户编码
     */
    
	public int getContractCode(StringBuffer myContractCode,Long dearCommpanyId)
	{
		int count=0;
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT max(substr(tfc.contract_no,"+(myContractCode.length()+1)+")) MYMAX FROM Tt_Fleet_Contract tfc WHERE 1=1  AND tfc.Dlr_Company_Id="+dearCommpanyId+" AND substr(tfc.contract_no, 0,"+myContractCode.length()+") ='"+myContractCode+"'");
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		//System.out.println(map.get("MYMAX").toString());
		if(map.get("MYMAX")!=null&&map.get("MYMAX")!=""){
			count =Integer.parseInt(map.get("MYMAX").toString());
		}
		return count;
	}
	
	public PageResult<Map<String, Object>> getGroupDropDownBox(String poseId,String level,String companyId, int curPage,int pageSize)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT -1 GROUP_ID, '全系' GROUP_CODE, '全系' GROUP_NAME FROM DUAL\n" );
		sql.append("UNION ALL\n");
		sql.append("SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");  
		sql.append(" WHERE TVMG.GROUP_LEVEL = "+level+"\n");  
		sql.append("   AND TVMG.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND TVMG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append(" START WITH TVMG.GROUP_ID IN\n");  
		sql.append("            (SELECT TAG.MATERIAL_GROUP_ID\n");  
		sql.append("               FROM TM_AREA_GROUP TAG, TM_POSE_BUSINESS_AREA TPBA\n");  
		sql.append("              WHERE TAG.AREA_ID = TPBA.AREA_ID\n");  
		sql.append("                AND TPBA.POSE_ID = "+poseId+")\n");  
		sql.append("CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID\n");  
		sql.append("UNION\n");  
		sql.append("SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");  
		sql.append(" WHERE TVMG.GROUP_LEVEL = "+level+"\n");  
		sql.append("   AND TVMG.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND TVMG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append(" START WITH TVMG.GROUP_ID IN\n");  
		sql.append("            (SELECT TAG.MATERIAL_GROUP_ID\n");  
		sql.append("               FROM TM_AREA_GROUP TAG, TM_POSE_BUSINESS_AREA TPBA\n");  
		sql.append("              WHERE TAG.AREA_ID = TPBA.AREA_ID\n");  
		sql.append("                AND TPBA.POSE_ID = "+poseId+")\n");  
		sql.append("CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID");

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public List<Map<String, Object>> contractsDownload(Map<String, String> map) {
		String fleetName = map.get("fleetName") ;
		String fleetType = map.get("fleetType") ;
		String startDate = map.get("startDate") ;
		String endDate = map.get("endDate") ;
		String checkSDate = map.get("checkSDate") ;
		String checkEDate = map.get("checkEDate") ;
		String companyId = map.get("companyId") ;
		String contractNo = map.get("contractNo") ;
		String fleetStatus = map.get("fleetStatus") ;
		String auditSDate = map.get("auditSDate") ;
		String auditEDate = map.get("auditEDate") ;
		String orgId = map.get("orgId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("with series as\n");
		sql.append(" (select tvmg.group_id,\n");  
		sql.append("         tvmg.group_code,\n");  
		sql.append("         tvmg.group_name,\n");  
		sql.append("         tba.area_id,\n");  
		sql.append("         tba.area_name\n");  
		sql.append("    from tm_vhcl_material_group tvmg, tm_area_group ta, tm_business_area tba\n");  
		sql.append("   where 1 = 1\n");  
		sql.append("     and tvmg.group_id = ta.material_group_id\n");  
		sql.append("     and ta.area_id = tba.area_id\n");  
		sql.append("     and tba.area_id in (2010010100000001, 2010010100000002)\n");  
		sql.append("     and tvmg.group_level = 2\n");  
		sql.append("  union\n");  
		sql.append("  select -1, '全系', '全系', null, null from dual),\n");  
		sql.append("areainfo as\n");  
		sql.append(" (select tcu.user_id, min(tba.area_id) area_id, min(tba.area_name) area_name\n");  
		sql.append("    from tc_user               tcu,\n");  
		sql.append("         tr_user_pose          tup,\n");  
		sql.append("         tm_pose_business_area tpa,\n");  
		sql.append("         tm_business_area      tba\n");  
		sql.append("   where tcu.user_id = tup.user_id\n");  
		sql.append("     and tup.pose_id = tpa.pose_id\n");  
		sql.append("     and tpa.area_id = tba.area_id\n");  
		sql.append("     and tba.area_id in (2010010100000001, 2010010100000002)\n");  
		sql.append("     and tcu.user_type = ").append(Constant.SYS_USER_DEALER).append("\n");  
		sql.append("   group by tcu.user_id),\n");  
		sql.append("info as\n");  
		sql.append(" (select tmd.company_id,\n");  
		sql.append("         min(tba.area_id) area_id,\n");  
		sql.append("         min(tba.area_name) area_name,\n");  
		sql.append("         vod.root_org_id,\n");  
		sql.append("         vod.root_org_name\n");  
		sql.append("    from tm_dealer               tmd,\n");  
		sql.append("         tm_dealer_business_area tdba,\n");  
		sql.append("         vw_org_dealer           vod,\n");  
		sql.append("         tm_business_area        tba\n");  
		sql.append("   where tmd.dealer_id = tdba.dealer_id\n");  
		sql.append("     and tdba.area_id = tba.area_id\n");  
		sql.append("     and tmd.dealer_id = vod.dealer_id\n");  
		sql.append("     and tba.area_id in (2010010100000001, 2010010100000002)\n");  
		sql.append("   group by tmd.company_id, vod.root_org_id, vod.root_org_name)\n");  
		sql.append("select tfc.contract_no,\n");  
		sql.append("       info.root_org_name,\n");  
		sql.append("       nvl(nvl(series.area_name, areainfo.area_name), info.area_name) area_name,\n");  
		sql.append("       tmc.company_name,\n");  
		sql.append("       tf.fleet_name,\n");  
		sql.append("       cus_type.code_desc,\n");  
		sql.append("       tf.main_linkman,\n");  
		sql.append("       tf.main_phone,\n");  
		sql.append("       to_char(tfc.check_date, 'yyyy/mm/dd') check_date,\n");  
		sql.append("       series.group_name,\n");  
		sql.append("       tfin.intent_count,\n");  
		sql.append("       to_char(tfc.start_date, 'yyyy/mm/dd') || ' 至 ' ||\n");  
		sql.append("       to_char(tfc.end_date, 'yyyy/mm/dd') status_date,\n");  
		sql.append("       decode(tfin.intent_point, null, '', tfin.intent_point || '%') intent_point\n");  
		sql.append("  from tt_fleet_contract   tfc,\n");  
		sql.append("       tm_fleet            tf,\n");  
		sql.append("       tt_fleet_intent_new tfin,\n");  
		sql.append("       series,\n");  
		sql.append("       areainfo,\n");  
		sql.append("       info,\n");  
		sql.append("       tm_company          tmc,\n");  
		sql.append("       tc_code             cus_type\n");  
		sql.append(" where tfc.fleet_id = tf.fleet_id\n");  
		sql.append("   and tf.dlr_company_id = tmc.company_id\n");  
		sql.append("   and tf.fleet_type = cus_type.code_id(+)\n");  
		sql.append("   and tfc.contract_id = tfin.contract_id(+)\n");  
		sql.append("   and tfin.series_id = series.group_id\n");  
		sql.append("   and tfc.create_by = areainfo.user_id(+)\n");  
		sql.append("   and tfc.dlr_company_id = info.company_id\n");
		
		if(!CommonUtils.isNullString(fleetName)){
			sql.append("   AND tf.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!CommonUtils.isNullString(fleetType)){
			sql.append("   AND tf.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!CommonUtils.isNullString(startDate)){
			sql.append("   AND tfc.START_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!CommonUtils.isNullString(endDate)){
			sql.append("   AND tfc.END_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(!CommonUtils.isNullString(orgId)){
			sql.append("   AND info.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		
		if(!CommonUtils.isNullString(companyId)){
			sql.append("   AND tf.DLR_COMPANY_ID IN ("+companyId+")\n");
		}
		if(!CommonUtils.isNullString(fleetStatus)){
			sql.append("   AND TFC.STATUS = ").append(fleetStatus).append("\n");
		}
		if(!CommonUtils.isNullString(contractNo)){
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contractNo+"%'\n");
		}
		if(!CommonUtils.isNullString(checkSDate)){
			sql.append("   AND TFC.CHECK_DATE >= TO_DATE('"+checkSDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!CommonUtils.isNullString(checkEDate)){
			sql.append("   AND TFC.CHECK_DATE <= TO_DATE('"+checkEDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!CommonUtils.isNullString(auditSDate)){
			sql.append("   AND TFC.AUDIT_DATE >= TO_DATE('"+auditSDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!CommonUtils.isNullString(auditEDate)){
			sql.append("   AND TFC.AUDIT_DATE <= TO_DATE('"+auditEDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	/**
	 * 查询车厂直接添加的大客户
	 * @param m
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> fleetClientQuery(Map<String,String> m, int curPage,int pageSize){
		String fleetName=m.get("fleetName");
		String fleetType=m.get("fleetType");
		String companyId=m.get("companyId");
		String startDate=m.get("startDate");
		String endDate=m.get("endDate");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TF.FLEET_NAME,TF.FLEET_ID,\n" );
		sql.append("       TF.FLEET_TYPE,\n" );
		sql.append("TO_CHAR(TF.CREATE_DATE,'YYYY-MM-DD') CREATE_DATE,\n");
		sql.append("       TC.COMPANY_NAME,\n" );
		sql.append("       TF.REGION,\n" );
		sql.append("       TF.MAIN_PHONE,\n" );
		sql.append("       TF.MAIN_LINKMAN\n" );
		sql.append("  FROM TM_FLEET TF, TM_COMPANY TC\n" );
		sql.append(" WHERE TF.FLEET_CODE IS NULL AND TC.COMPANY_ID=TF.DLR_COMPANY_ID\n" );
		sql.append("   AND TF.STATUS = "+Constant.FLEET_INFO_TYPE_03+"\n" );
		if(fleetName!=null&&!"".equals(fleetName)){
			sql.append("   AND TF.FLEET_NAME LIKE '%"+fleetName+"%'\n" );
		}
		if(fleetType!=null&&!"".equals(fleetType)){
			sql.append("   AND TF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(companyId!=null&&!"".equals(companyId)){
			String companyIds=CommonUtils.getSplitStringForIn(companyId);
			sql.append(" AND TC.COMPANY_ID IN("+companyIds+")\n");
		}
		if(startDate!=null&&!"".equals(startDate)){
			sql.append("   AND TF.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(endDate!=null&&!"".equals(endDate)){
			sql.append("   AND TF.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		
		PageResult<Map<String, Object>> rs=dao.pageQuery(sql.toString(), null, super.getFunName(),pageSize, curPage);
		return rs;

	}
	/**
	 * Function         : 集团客户支持查询（非经销商）
	 * @param           : 客户名称
	 * @param           : 客户类型
	 * @param           : 起始时间
	 * @param           : 结束时间
	 * @param           : 经销商公司ID
	 * @param           : 申请状态
	 * @param           : 分页参数
	 * @return          : 集团客户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-22
	 */
	public PageResult<Map<String, Object>> fleetSupportPrintQuery(String fleetName,String fleetType,String startDate,String endDate,String companyId,String dutyType,String orgId,String supportStatus,int curPage,int pageSize) throws Exception{
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TMF.FLEET_ID,\n" );
		sql.append("       TO_CHAR(TFS.SUPPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TMC.COMPANY_SHORTNAME COMPANY_NAME,\n" );
		sql.append("       TMF.FLEET_NAME,\n" );
		sql.append("       TMF.FLEET_TYPE,\n" );
		sql.append("       TFS.SUPPORT_STATUS,\n" );
		sql.append("       TMF.REGION,\n" );
		sql.append("       TMF.MAIN_LINKMAN,\n" );
		sql.append("       TMF.MAIN_PHONE,\n" );
		sql.append("       TMF.FLEET_CODE,\n" );
		sql.append("       DECODE(TMF.IS_PRINT,1,'已打印','未打印') IS_PRINT,\n");
		sql.append("       VW.DEALER_ID,\n" );
		sql.append("       C.TOTAL REQUEST_COUNT,\n" );
		sql.append("       D.FINISH_TOTAL FINISH_COUNT\n" );
		sql.append("  FROM TM_FLEET TMF, TM_COMPANY TMC, TM_DEALER TD,TT_FLEET_SUPPORT TFS, vw_org_dealer VW,\n" );
		
		
		sql.append("(SELECT FLEET_ID, TOTAL\n" );
		sql.append("           FROM (SELECT TF.FLEET_ID FLEET_ID, SUM(NVL(TFRD.AMOUNT, 0)) TOTAL\n" );
		sql.append("                   FROM TM_FLEET TF\n" );
		sql.append("                   JOIN TT_FLEET_SUPPORT_INFO TFRD\n" );
		sql.append("                     ON TFRD.FLEET_ID = TF.FLEET_ID\n" );
		sql.append("                  GROUP BY TF.FLEET_ID)) C,\n" );
		sql.append("          (SELECT FLEET_ID,NVL(FINISH_TOTAL,0) FINISH_TOTAL  FROM( SELECT TF.FLEET_ID, COUNT(*) FINISH_TOTAL\n" );
		sql.append("   FROM TT_DEALER_ACTUAL_SALES TDAS\n" );
		sql.append("   JOIN TM_FLEET TF\n" );
		sql.append("     ON TDAS.FLEET_ID = TF.FLEET_ID\n" );
		sql.append("    AND TDAS.FLEET_ID IS NOT NULL\n" );
		sql.append("    AND TDAS.IS_RETURN = 10041002\n" );
		sql.append("    AND TDAS.IS_FLEET = 10041001\n" );
		sql.append("  GROUP BY TF.FLEET_ID))    D\n");
		
		sql.append(" WHERE TMC.COMPANY_ID = TMF.DLR_COMPANY_ID\n" );
		sql.append("   AND TD.COMPANY_ID = TMC.COMPANY_ID\n" );
		sql.append("   AND TD.DEALER_ID = VW.DEALER_ID");
		sql.append("   AND TFS.FLEET_ID = TMF.FLEET_ID");
		sql.append("      AND C.FLEET_ID(+)=TMF.FLEET_ID\n");
		sql.append("       AND D.FLEET_ID(+)=TMF.FLEET_ID\n");
		sql.append("   AND ( TFS.SUPPORT_STATUS = "+Constant.SUPPORT_INFO_STATUS_04+"\n");
		sql.append("   OR TFS.SUPPORT_STATUS = "+Constant.SUPPORT_INFO_STATUS_03+")\n");
//		if(!"-1".equals(supportStatus)&&!"".equals(supportStatus)&&supportStatus!=null){
//			sql.append("   AND TFI.STATUS = "+supportStatus+"\n");
//		}else{
//			sql.append("   AND TFI.STATUS <> "+Constant.FLEET_SUPPORT_STATUS_01+"\n");
//		}
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n");
		}
		if(!"".equals(fleetType)&&fleetType!=null){
			sql.append("   AND TMF.FLEET_TYPE = "+fleetType+"\n");
		}
		if(!"".equals(supportStatus)&&supportStatus!=null){
			sql.append("   AND TFS.SUPPORT_STATUS = "+supportStatus+"\n");
		}
		//开始时间
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TFS.SUPPORT_DATE > TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
		}
		//结束时间
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TFS.SUPPORT_DATE < TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(orgId)&&orgId!=null){
			if(Constant.DUTY_TYPE_SMALLREGION.toString().equals(dutyType)){
				sql.append("   AND VW.DEALER_ID IN ( SELECT VOD.DEALER_ID FROM vw_org_dealer VOD WHERE VOD.PQ_ORG_ID="+orgId+" )\n");
			}
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
				sql.append("   AND VW.DEALER_ID IN ( SELECT VOD.DEALER_ID FROM vw_org_dealer VOD WHERE VOD.ROOT_ORG_ID="+orgId+" )\n");
			}
			
		}
		if(!"".equals(companyId)&&companyId!=null){
			String companyIds=CommonUtils.getSplitStringForIn(companyId);
			sql.append(" AND TMC.COMPANY_ID in("+companyIds+")\n");
		}
		sql.append(" ORDER BY TFS.CREATE_DATE DESC");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
}
