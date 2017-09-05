package com.infodms.dms.common.materialManager;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.vehicle.AsSqlUtils;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;

public class MaterialGroupManagerDao extends BaseDao<PO> {
	private static final MaterialGroupManagerDao dao = new MaterialGroupManagerDao();
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	public static final MaterialGroupManagerDao getInstance()
	{
		return dao;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		
		//List<Map<String, Object>> list=getGroupDropDownBox("1000001599","2");
		//System.out.print(list.size());
		//System.out.print("1111");
		String materialGroupCode = "222,3333,222,2423,42";
		String[] materialCodes = materialGroupCode.split(",");
		for (int i = 0; i < materialCodes.length; i++)
		{
			materialCodes[i] = "'" + materialCodes[i] + "'";
		}
		System.out.println(materialCodes.toString());
	}
	
	/**
	 * 整车提车单审核列表“车系”查询条件SQL
	 * 方法描述 ： <br/>
	 * 
	 * @param orderIdColumn
	 * @param orderType
	 * @return
	 * @author wangsongwei
	 */
	public static String getGroupQuerySql(String orderIdColumn, String groupId, String orderType)
	{
		String tableName = "";
		if (orderType.equals(Constant.ORDER_INVOICE_TYPE_01.toString()))//整车订单
		{
			tableName = "TT_VS_ORDER_DETAIL";
		}
		else if (orderType.equals(Constant.ORDER_INVOICE_TYPE_02.toString()))
		{//中转库订单
			tableName = "TT_OUT_ORDER_DETAIL";
		}
		else
		{
			throw new RuntimeException("参数错误!");
		}
		StringBuffer sbSql = new StringBuffer();
		sbSql.append(" AND EXISTS( \n");
		sbSql.append("SELECT   1\n");
		sbSql.append("  FROM   VW_MATERIAL_GROUP_MAT A, " + tableName + " B\n");
		sbSql.append(" WHERE   A.SERIES_ID=" + groupId + " AND A.MATERIAL_ID = B.MATERIAL_ID AND B.ORDER_ID = " + orderIdColumn + "\n");
		sbSql.append(")\n");
		return sbSql.toString();
	}
	
	/**
	 * 获取物料组查询SQL (选择物料组查询时使用)
	 * 
	 * @param groupIdColumn
	 * @param materialGroupCode
	 * @return
	 */
	public static String getMaterialGroupQuerySql(String groupIdColumn, String materialGroupCode)
	{
		StringBuffer buffer = new StringBuffer();
		if (null != materialGroupCode && !"".equals(materialGroupCode))
		{
			String[] materialCodes = materialGroupCode.split(",");
			if (null != materialCodes && materialCodes.length > 0)
			{
				for (int i = 0; i < materialCodes.length; i++)
				{
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
			} 
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" AND EXISTS( ");
		sb.append("    SELECT   1\n");
		sb.append("      FROM   TM_VHCL_MATERIAL_GROUP A\n");
		sb.append("      WHERE  A.GROUP_ID=" + groupIdColumn + "\n");
		sb.append("START WITH   A.GROUP_ID IN (SELECT   B.GROUP_ID\n");
		sb.append("                              FROM   TM_VHCL_MATERIAL_GROUP B\n");
		sb.append("                             WHERE   B.GROUP_CODE IN(" + buffer.toString() + ") )\n");
		sb.append("CONNECT BY   PRIOR A.GROUP_ID = A.PARENT_GROUP_ID\n");
		sb.append(")\n");
		return sb.toString();
	}
	/**
	 * 获取物料组查询SQL (选择物料组查询时使用)
	 * 
	 * @param groupIdColumn
	 * @param materialGroupCode
	 * @return
	 */
	public static String getMaterialGroupQuerySqlByPar(String groupIdColumn, String materialGroupCode,List par)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(" AND EXISTS( ");
		sb.append("    SELECT   1\n");
		sb.append("      FROM   TM_VHCL_MATERIAL_GROUP AAA\n");
		sb.append("      WHERE  AAA.GROUP_ID=" + groupIdColumn + "\n");
		sb.append("START WITH   AAA.GROUP_ID IN (SELECT   BBB.GROUP_ID\n");
		sb.append("                              FROM   TM_VHCL_MATERIAL_GROUP BBB\n");
		sb.append("                             WHERE   1=1 \n");
		sb.append(Utility.getConSqlByParamForEqual(materialGroupCode,par,"BBB","GROUP_CODE"));
		sb.append("                                                                      )\n");
		sb.append("CONNECT BY   PRIOR AAA.GROUP_ID = AAA.PARENT_GROUP_ID\n");
		sb.append(")\n");
		return sb.toString();
	}
	
	/**
	 * Function : 查询预测关系
	 * 
	 * @param :
	 *            职位ID
	 */
	public static List<Map<String, Object>> getForecastType(Integer type) {
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.code_id,t.code_desc from tc_code t where t.type ="+  type+"\n");
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * 获取物料组查询SQL (选择物料组查询时使用)
	 * 
	 * @param groupIdColumn
	 * @param materialGroupCode
	 * @return
	 */
	public static String getOrgQuerySql(String orgIdColumn, String orgCode)
	{
		StringBuffer buffer = new StringBuffer();
		if (null != orgCode && !"".equals(orgCode))
		{
			String[] orgCodes = orgCode.split(",");
			if (null != orgCodes && orgCodes.length > 0)
			{
				for (int i = 0; i < orgCodes.length; i++)
				{
					buffer.append("'").append(orgCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" AND EXISTS( ");
		sb.append("    SELECT   1\n");
		sb.append("      FROM   TM_ORG A\n");
		sb.append("      WHERE  A.ORG_ID=" + orgIdColumn + "\n");
		sb.append("START WITH   A.ORG_ID IN (SELECT   B.ORG_ID\n");
		sb.append("                              FROM   TM_ORG B\n");
		sb.append("                             WHERE   B.ORG_CODE IN(" + buffer.toString() + ") )\n");
		sb.append("CONNECT BY   PRIOR A.ORG_ID = A.PARENT_ORG_ID\n");
		sb.append(")\n");
		return sb.toString();
	}
	
	/**
	 * 获取物料查询SQL (选择物料查询时使用)
	 * 
	 * @param materialIdColumn
	 * @param materialCode
	 * @return
	 */
	public static String getMaterialQuerySql(String materialIdColumn, String materialCode)
	{
		StringBuffer buffer = new StringBuffer();
		if (null != materialCode && !"".equals(materialCode))
		{
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0)
			{
				for (int i = 0; i < materialCodes.length; i++)
				{
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
			}
		}
		StringBuffer sbSql = new StringBuffer();
		sbSql.append(" AND EXISTS(\n");
		sbSql.append(" SELECT 1 FROM TM_VHCL_MATERIAL A\n");
		sbSql.append(" WHERE A.MATERIAL_CODE IN(" + buffer.toString() + ") AND A.MATERIAL_ID=" + materialIdColumn + "\n");
		sbSql.append(")\n");
		return sbSql.toString();
	}
	
	/**
	 * Function : 查询车厂职位对应产地
	 * 
	 * @param : 职位ID
	 */
	public static List<Map<String, Object>> getPoseIdBusiness(String poseId)
	{
		List<Map<String, Object>> list = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TBA.AREA_ID,\n");
		sql.append("       TBA.AREA_CODE,\n");
		sql.append("       TBA.AREA_NAME,\n");
		sql.append("       TPBA.POSE_ID\n");
		sql.append("  FROM TM_POSE_BUSINESS_AREA TPBA, TM_BUSINESS_AREA TBA\n");
		sql.append(" WHERE TPBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TBA.STATUS =?\n");
		sql.append("   AND TPBA.POSE_ID = ?");
		List par=new ArrayList();
		par.add(Constant.STATUS_ENABLE);
		par.add(poseId);
		list = dao.pageQuery(sql.toString(), par, dao.getFunName());
		return list;
	}
	
	/**
	 * Function : 查询车厂职位对应仓库
	 * 
	 * @param : 职位ID
	 */
	public static List<Map<String, Object>> getWarehouseByPoseId(String poseId)
	{
		List<Map<String, Object>> list = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT WH.WAREHOUSE_ID AREA_ID,\n");
		sql.append("       WH.WAREHOUSE_CODE AREA_CODE,\n");
		sql.append("       WH.WAREHOUSE_NAME AREA_NAME,\n");
		sql.append("       TPBA.POSE_ID\n");
		sql.append("  FROM TM_POSE_BUSINESS_AREA TPBA, TM_BUSINESS_AREA TBA, TM_WAREHOUSE WH\n");
		sql.append(" WHERE TPBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TBA.AREA_ID = WH.AREA_ID\n");
		sql.append("   AND TBA.STATUS =?\n");
		sql.append("   AND TPBA.POSE_ID = ?");
		List par=new ArrayList();
		par.add(Constant.STATUS_ENABLE);
		par.add(poseId);
		list = dao.pageQuery(sql.toString(), par, dao.getFunName());
		return list;
	}
	
	/**
	 * 获取所有内部仓库
	 * @return
	 */
	public static List<Map<String, Object>> getWarehouseList(String poseId,String poseBusType)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		if(poseBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//物流商
			sql.append("SELECT TW.*\n" );
			sql.append("  FROM TC_POSE TP, TT_SALES_LOGI TSL, TM_WAREHOUSE TW\n" );
			sql.append(" WHERE TP.LOGI_ID = TSL.LOGI_ID\n" );
			sql.append("   AND TSL.YIELDLY = TW.AREA_ID\n" );
			sql.append("   AND TP.POSE_STATUS = 10011001\n" );
			sql.append("   AND TP.POSE_BUS_TYPE = 10781007\n" );
			sql.append("   AND TW.WAREHOUSE_TYPE = 14011001\n" );
			sql.append("   AND TW.status=10011001\n" );
			sql.append("   AND TP.POSE_ID = '"+poseId+"'\n");

		}else{
			sql.append("select t.* " +
					"from tm_warehouse t " +
					"where t.warehouse_type = 14011001 and t.status=10011001");
		}
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	/**
	 * Function : 取得经销商合同所对应的产地
	 * 
	 * @param : 职位ID
	 */
	public static List<Map<String, Object>> getDealerBusinessArea(String dealerId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT   A.AREA_ID,\n");
		sbSql.append("         A.AREA_CODE,\n");
		sbSql.append("         A.AREA_NAME\n");
		sbSql.append("  FROM   TM_BUSINESS_AREA A\n");
		sbSql.append(" WHERE   EXISTS\n");
		sbSql.append("            (SELECT   1\n");
		sbSql.append("               FROM   TM_AREA_GROUP B,\n");
		sbSql.append("                      TT_SALES_CONTRACT C,\n");
		sbSql.append("                      TT_SALES_CONTRACT_DTL D\n");
		sbSql.append("              WHERE       B.MATERIAL_GROUP_ID = D.GROUP_ID\n");
		sbSql.append("                      AND D.CONTRACT_ID = C.CONTRACT_ID\n");
		sbSql.append("                      AND A.AREA_ID = B.AREA_ID\n");
		sbSql.append("                      AND C.CONTRACT_YEAR = TO_CHAR (SYSDATE, 'YYYY')\n");
		sbSql.append("                      AND C.STATUS = ?\n");
		sbSql.append("                      AND C.DEALER_ID = ?) ORDER BY A.AREA_ID ASC\n");
		
		List<Object> par = new ArrayList<Object>();
		par.add(Constant.STATUS_ENABLE);
		par.add(new Long(dealerId));
		
		list = dao.pageQuery(sbSql.toString(), par, dao.getFunName());
		return list;
	}
	
	/**
	 * 车厂产地权限SQL
	 * 
	 * @param poseId
	 * @param yieldlyColumn
	 * @return
	 */
	public static String getPoseIdBusinessSql(String yieldlyColumn, String poseId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("AND EXISTS (SELECT 1 \n");
		sql.append("  FROM TM_POSE_BUSINESS_AREA TPBA, TM_BUSINESS_AREA TBA\n");
		sql.append(" WHERE TPBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TPBA.POSE_ID = " + Long.parseLong(poseId)+")" );//" AND TBA.AREA_ID=" + yieldlyColumn + ")\n"  2017-7-27
		return sql.toString();
	}
	
	public static String getPoseIdBusinessSql4AS(String yieldlyColumn, String poseId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("AND EXISTS (SELECT 1 \n");
		sql.append("  FROM TM_POSE_BUSINESS_AREA TPBA\n");
		sql.append(" WHERE TPBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TPBA.POSE_ID = " + Long.parseLong(poseId)  + ")\n");
		return sql.toString();
	}
	/**
	 * 车厂产地权限SQL
	 * 
	 * @param poseId
	 * @param yieldlyColumn
	 * @return
	 */
	public static String getPoseIdBusinessSqlByPar(String yieldlyColumn, String poseId,List par)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("AND EXISTS (SELECT 1 \n");
		sql.append("  FROM TM_POSE_BUSINESS_AREA TPBA, TM_BUSINESS_AREA TBA\n");
		sql.append(" WHERE TPBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TPBA.POSE_ID = ? AND TBA.AREA_ID=" + yieldlyColumn + ")\n");
		par.add(Long.valueOf(poseId));
		return sql.toString();
	}
	/**
	 * vin查询（储运用）
	 * 
	 * @param poseId
	 * @param yieldlyColumn
	 * @return
	 */
	public static String geVinSql(String vinColumn, String vin)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("AND EXISTS(SELECT 1\n");
		sbSql.append("         FROM TT_SALES_ASS_DETAIL M, TT_SALES_ALLOCA_DE S, TM_VEHICLE N\n");
		sbSql.append("        WHERE M.ASS_DETAIL_ID = S.ASS_DETAIL_ID\n");
		sbSql.append("          AND S.VEHICLE_ID = N.VEHICLE_ID\n");
		sbSql.append("          AND B.ASS_DETAIL_ID = M.ASS_DETAIL_ID\n");
		sbSql.append("       AND "+vinColumn+" = M.ASS_DETAIL_ID\n");
		sbSql.append("       AND N.VIN='"+vin+"'\n");
		//sbSql.append("       AND M.IS_ENABLE="+Constant.IF_TYPE_YES+")"); 
		return sbSql.toString();
	} 
	/**
	 * vin查询（储运用）
	 * 
	 * @param poseId
	 * @param yieldlyColumn
	 * @return
	 */
	public static String geVinSqlPar(String vinColumn, String vin,List par)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("AND EXISTS(SELECT 1\n");
		sbSql.append("         FROM TT_SALES_ASS_DETAIL M, TT_SALES_ALLOCA_DE S, TM_VEHICLE N\n");
		sbSql.append("        WHERE M.ASS_DETAIL_ID = S.ASS_DETAIL_ID\n");
		sbSql.append("          AND S.VEHICLE_ID = N.VEHICLE_ID\n");
		sbSql.append("          AND B.ASS_DETAIL_ID = M.ASS_DETAIL_ID\n");
		sbSql.append("       AND "+vinColumn+" = M.ASS_DETAIL_ID\n");
		sbSql.append("       AND N.VIN=?)\n");
	//	sbSql.append("       AND M.IS_ENABLE="+Constant.IF_TYPE_YES+")"); 
		par.add(vin);
		return sbSql.toString();
	}
	/**
	 * 经销商产地权限SQL
	 * 
	 * @param poseId
	 * @param yieldlyColumn
	 * @return
	 */
	public static String getDealerBusinessSql(String yieldlyColumn, String poseId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
//		sql.append(" AND EXISTS(\n");
//		sql.append("SELECT TBA.AREA_CODE, TBA.AREA_ID, TBA.AREA_NAME, TD.DEALER_ID,TBA.AREA_TYPE\n");
//		sql.append("  FROM TC_POSE                 TP,\n");
//		sql.append("       TM_DEALER               TD,\n");
//		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");
//		sql.append("       TM_BUSINESS_AREA        TBA,\n");
//		sql.append("       TM_POSE_BUSINESS_AREA   TPBA\n");
//		sql.append(" WHERE TP.ORG_ID = TD.DEALER_ORG_ID\n");
//		sql.append("   AND TD.DEALER_ID = TDBA.DEALER_ID\n");
//		sql.append("   AND TDBA.AREA_ID = TBA.AREA_ID\n");
//		sql.append("   AND TPBA.POSE_ID = TP.POSE_ID\n");
//		sql.append("   AND TDBA.AREA_ID = TPBA.AREA_ID\n");
//		sql.append("   AND TD.STATUS = " + Constant.STATUS_ENABLE + "\n");
//		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
//		sql.append("   AND (TD.DEALER_TYPE = " + Constant.DEALER_TYPE_DVS + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_JSZX + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_QYZDL + ")\n");
//		sql.append("   AND TP.POSE_ID = " + poseId + "\n");
//		sql.append("   AND TBA.AREA_ID=" + yieldlyColumn + "\n");
//		sql.append(") \n");
		return sql.toString();
	}
	
	/**
	 * Function : 查询经销商业务范围
	 * 
	 * @param : 职位ID
	 */
	public static List<Map<String, Object>> getDealerBusiness(String poseId)
	{
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TBA.AREA_CODE, TBA.AREA_ID, TBA.AREA_NAME, TD.DEALER_ID,TBA.AREA_TYPE\n");
		sql.append("  FROM TC_POSE                 TP,\n");
		sql.append("       TM_DEALER               TD,\n");
		sql.append("       TM_BUSINESS_AREA        TBA,\n");
		sql.append("       TM_POSE_BUSINESS_AREA   TPBA\n");
		sql.append(" WHERE TP.COMPANY_ID = TD.DEALER_ID\n");
		sql.append("   AND TPBA.POSE_ID = TP.POSE_ID\n");
		sql.append("   AND TBA.AREA_ID = TPBA.AREA_ID\n");
		sql.append("   AND TD.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND (TD.DEALER_TYPE = " + Constant.DEALER_TYPE_DVS + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_JSZX + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_QYZDL + ")\n");
		
		if (!"".equals(poseId) && poseId != null)
		{
			sql.append("   AND TP.POSE_ID = ?\n"); params.add(poseId);
		}
		
		return dao.pageQuery(sql.toString(), params, dao.getFunName());
	}
	
	/**
	 * Function : 查询所有产地
	 * 
	 * @param : 公司ID
	 */
	public static List<Map<String, Object>> getAllBusiness(String companyId)
	{
		List<Map<String, Object>> list = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TBA.AREA_ID,\n");
		sql.append("       TBA.AREA_CODE,\n");
		sql.append("       TBA.AREA_NAME\n");
		sql.append("  FROM TM_BUSINESS_AREA TBA\n");
		sql.append(" WHERE TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		//sql.append("   AND TBA.COMPANY_ID= "+companyId+"\n" );
		
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * 机构数据权限限定
	 * 
	 * @param dealerIdColumnName
	 * @param orgId
	 * @return
	 */
	public static String getOrgDealerLimitSql(String dealerIdColumnName, AclUserBean logonUser)
	{
		Long orgId = logonUser.getOrgId();
		Integer chooseDealer = logonUser.getChooseDealer() == null ? 0 : logonUser.getChooseDealer();
		StringBuffer sbSql = new StringBuffer();
		String dutyType=logonUser.getDutyType();
		if(dutyType.equals("10431003")||dutyType.equals("10431004")){
			if (chooseDealer.longValue() == Constant.IF_TYPE_YES.longValue())
			{//限制当前职位所选择管理的经销商
				sbSql.append(" AND EXISTS(\n");
	
				sbSql.append("SELECT   TPD.DEALER_ID \n"); 
				sbSql.append("  FROM   TR_POSE_DEALER TPD\n"); 
				sbSql.append(" WHERE   TPD.POSE_ID = "+logonUser.getPoseId()+" AND TPD.DEALER_ID="+dealerIdColumnName+"\n"); 
				sbSql.append("UNION ALL\n"); 
				sbSql.append("SELECT   TMDE.DEALER_ID\n"); 
				sbSql.append("  FROM   TM_DEALER TMDE, TR_POSE_DEALER TPPD\n"); 
				sbSql.append(" WHERE       TMDE.PARENT_DEALER_D = TPPD.DEALER_ID\n"); 
				sbSql.append("         AND TMDE.DEALER_TYPE = 10771001\n"); 
				sbSql.append("         AND TMDE.DEALER_LEVEL = 10851002\n"); 
				sbSql.append("         AND TPPD.POSE_ID = "+logonUser.getPoseId()+" AND TMDE.DEALER_ID="+dealerIdColumnName+"\n");
				sbSql.append(")\n");
			}
			else
			{
				sbSql.append(" and exists \n");
				sbSql.append("          (select 1 \n");
				sbSql.append("             from tr_pose_region t1, tm_dealer_org_relation t2 \n");
				sbSql.append("            where t1.region_id = t2.org_id \n");
				sbSql.append("              and t2.dealer_id = "+dealerIdColumnName+" \n");
				sbSql.append("              and t1.pose_id = "+logonUser.getPoseId()+") \n");
			} 
		}
		return sbSql.toString();
		
	}
	/**
	 * 机构数据权限限定(售后)
	 * 
	 * @param dealerIdColumnName
	 * @param orgId
	 * @return
	 */
	public static String getOrgDealerLimitSqlForService(String dealerIdColumnName, AclUserBean logonUser)
	{
		Long orgId = logonUser.getOrgId();
		Integer chooseDealer = logonUser.getChooseDealer() == null ? 0 : logonUser.getChooseDealer();
		StringBuffer sbSql = new StringBuffer();
		String dutyType=logonUser.getDutyType();
		if(dutyType.equals("10431003")||dutyType.equals("10431004")){
			if (chooseDealer.longValue() == Constant.IF_TYPE_YES.longValue())
			{//限制当前职位所选择管理的经销商
				sbSql.append(" AND EXISTS(\n");
	
				sbSql.append("SELECT   TPD.DEALER_ID \n"); 
				sbSql.append("  FROM   TR_POSE_REGION_DLR_SERVICE TPD\n"); 
				sbSql.append(" WHERE   TPD.POSE_ID = "+logonUser.getPoseId()+" AND TPD.DEALER_ID="+dealerIdColumnName+"\n"); 
				sbSql.append("UNION ALL\n"); 
				sbSql.append("SELECT   TMDE.DEALER_ID\n"); 
				sbSql.append("  FROM   TM_DEALER TMDE, TR_POSE_REGION_DLR_SERVICE TPPD\n"); 
				sbSql.append(" WHERE       TMDE.PARENT_DEALER_D = TPPD.DEALER_ID\n"); 
				sbSql.append("         AND TMDE.DEALER_TYPE = 10771002\n"); 
				sbSql.append("         AND TMDE.DEALER_LEVEL = 10851002\n"); 
				sbSql.append("         AND TPPD.POSE_ID = "+logonUser.getPoseId()+" AND TMDE.DEALER_ID="+dealerIdColumnName+"\n");
				sbSql.append(")\n");
	
				
			}
			else
			{//其它类型只限制机构数据权限，管理所选省份机构下所有经销商
	//			sbSql.append("AND EXISTS(    SELECT   1 \n");
	//			sbSql.append("       FROM   TM_DEALER KK\n");
	//			sbSql.append("      WHERE   KK.DEALER_ID =  " + dealerIdColumnName + "\n");
	//			sbSql.append(" START WITH   DEALER_ID IN\n");
	//			sbSql.append("                    (SELECT   RELA.DEALER_ID\n");
	//			sbSql.append("                       FROM   TM_DEALER_ORG_RELATION RELA,TR_POSE_REGION TPR,TM_DEALER TD\n");
	//			sbSql.append("                       WHERE RELA.ORG_ID=TPR.REGION_ID\n");
	//			sbSql.append("                       AND  RELA.DEALER_ID=TD.DEALER_ID AND TD.DEALER_TYPE=10771001 AND TD.DEALER_LEVEL=10851001\n");
	//			sbSql.append("                       AND TPR.POSE_ID=" + logonUser.getPoseId() + ")\n");
	//			sbSql.append(" CONNECT BY   PRIOR KK.DEALER_ID = KK.PARENT_DEALER_D)\n");
				sbSql.append("AND EXISTS(SELECT 1 FROM TR_POSE_REGION_DLR_SERVICE  TRD WHERE TRD.DEALER_ID="+dealerIdColumnName+" AND TRD.POSE_ID="+logonUser.getPoseId()+")\n");
				
				
	//			sbSql.append(" AND EXISTS(SELECT 1 FROM ( ");
	//			sbSql.append("SELECT   TD.DEALER_ID \n"); 
	//			sbSql.append("  FROM   TM_DEALER_ORG_RELATION RELA, TR_POSE_REGION TPR, TM_DEALER TD\n"); 
	//			sbSql.append(" WHERE       RELA.DEALER_ID = TD.DEALER_ID\n"); 
	//			sbSql.append("         AND RELA.ORG_ID = TPR.REGION_ID\n"); 
	//			sbSql.append("         AND TD.DEALER_TYPE = 10771001\n"); 
	//			sbSql.append("         AND TD.DEALER_LEVEL = 10851001 AND TPR.POSE_ID="+logonUser.getPoseId()+"\n"); 
	//			sbSql.append("UNION ALL\n"); 
	//			sbSql.append("SELECT   TD.DEALER_ID\n"); 
	//			sbSql.append("  FROM   TM_DEALER_ORG_RELATION RELA, TR_POSE_REGION TPR, TM_DEALER TD\n"); 
	//			sbSql.append(" WHERE   RELA.DEALER_ID = TD.PARENT_DEALER_D " +
	//					"AND RELA.ORG_ID = TPR.REGION_ID AND TPR.POSE_ID="+logonUser.getPoseId()+"\n");
	//			sbSql.append(" ) ABC WHERE ABC.DEALER_ID="+dealerIdColumnName+")\n");
	
			} 
		}
		return sbSql.toString();
		
	}
	/**
	 * 机构数据权限限定
	 * 
	 * @param dealerIdColumnName
	 * @param orgId
	 * @return
	 */
	public static String getOrgDealerLimitSqlByPar(String dealerIdColumnName, AclUserBean logonUser,List par)
	{
		Long orgId = logonUser.getOrgId();
		Integer chooseDealer = logonUser.getChooseDealer() == null ? 0 : logonUser.getChooseDealer();
		StringBuffer sbSql = new StringBuffer();
		String dutyType=logonUser.getDutyType();
		if(dutyType.equals("10431003")||dutyType.equals("10431004")){//只有大区和小区才限制管理经销商权限
			if (chooseDealer.longValue() == Constant.IF_TYPE_YES.longValue())
			{//限制当前职位所选择管理的经销商
				sbSql.append(" AND EXISTS(\n");
	
				sbSql.append("SELECT   TPD.DEALER_ID \n"); 
				sbSql.append("  FROM   TR_POSE_DEALER TPD\n"); 
				sbSql.append(" WHERE   TPD.POSE_ID = ? AND TPD.DEALER_ID="+dealerIdColumnName+"\n"); 
				par.add(logonUser.getPoseId());
				sbSql.append("UNION ALL\n"); 
				sbSql.append("SELECT   TMDE.DEALER_ID\n"); 
				sbSql.append("  FROM   TM_DEALER TMDE, TR_POSE_DEALER TPPD\n"); 
				sbSql.append(" WHERE       TMDE.PARENT_DEALER_D = TPPD.DEALER_ID\n"); 
				sbSql.append("         AND TMDE.DEALER_TYPE = 10771001\n"); 
				sbSql.append("         AND TMDE.DEALER_LEVEL = 10851002\n"); 
				sbSql.append("         AND TPPD.POSE_ID = ? AND TMDE.DEALER_ID="+dealerIdColumnName+"\n");
				sbSql.append(")\n");
				par.add(logonUser.getPoseId());
				
			}
			else
			{//其它类型只限制机构数据权限，管理所选省份机构下所有经销商
	//			sbSql.append("AND EXISTS(    SELECT   1 \n");
	//			sbSql.append("       FROM   TM_DEALER KK\n");
	//			sbSql.append("      WHERE   KK.DEALER_ID =  " + dealerIdColumnName + "\n");
	//			sbSql.append(" START WITH   DEALER_ID IN\n");
	//			sbSql.append("                    (SELECT   RELA.DEALER_ID\n");
	//			sbSql.append("                       FROM   TM_DEALER_ORG_RELATION RELA,TR_POSE_REGION TPR,TM_DEALER TD\n");
	//			sbSql.append("                       WHERE RELA.ORG_ID=TPR.REGION_ID\n");
	//			sbSql.append("                       AND  RELA.DEALER_ID=TD.DEALER_ID AND TD.DEALER_TYPE=10771001 AND TD.DEALER_LEVEL=10851001\n");
	//			sbSql.append("                       AND TPR.POSE_ID=" + logonUser.getPoseId() + ")\n");
	//			sbSql.append(" CONNECT BY   PRIOR KK.DEALER_ID = KK.PARENT_DEALER_D)\n");
				sbSql.append("AND EXISTS(SELECT 1 FROM TR_POSE_REGION_DEALER  TRD WHERE TRD.DEALER_ID="+dealerIdColumnName+" AND TRD.POSE_ID=? )\n");
				par.add(logonUser.getPoseId());
				
				
	//			sbSql.append(" AND EXISTS(SELECT 1 FROM ( ");
	//			sbSql.append("SELECT   TD.DEALER_ID \n"); 
	//			sbSql.append("  FROM   TM_DEALER_ORG_RELATION RELA, TR_POSE_REGION TPR, TM_DEALER TD\n"); 
	//			sbSql.append(" WHERE       RELA.DEALER_ID = TD.DEALER_ID\n"); 
	//			sbSql.append("         AND RELA.ORG_ID = TPR.REGION_ID\n"); 
	//			sbSql.append("         AND TD.DEALER_TYPE = 10771001\n"); 
	//			sbSql.append("         AND TD.DEALER_LEVEL = 10851001 AND TPR.POSE_ID="+logonUser.getPoseId()+"\n"); 
	//			sbSql.append("UNION ALL\n"); 
	//			sbSql.append("SELECT   TD.DEALER_ID\n"); 
	//			sbSql.append("  FROM   TM_DEALER_ORG_RELATION RELA, TR_POSE_REGION TPR, TM_DEALER TD\n"); 
	//			sbSql.append(" WHERE   RELA.DEALER_ID = TD.PARENT_DEALER_D " +
	//					"AND RELA.ORG_ID = TPR.REGION_ID AND TPR.POSE_ID="+logonUser.getPoseId()+"\n");
	//			sbSql.append(" ) ABC WHERE ABC.DEALER_ID="+dealerIdColumnName+")\n");
	
			} 
		}
		return sbSql.toString();
		
	}
	
	/**
	 * 机构数据权限限定(售后服务大区预授权限定经销商)
	 * 
	 * @param dealerIdColumnName
	 * @param mark 用来标识 是取的 dealer_id 对应表的别名
	 * @return
	 */
	public static String getOrgDealerLimitSqlByService(String mark, AclUserBean logonUser)
	{
		Integer chooseDealer = logonUser.getChooseDealer() == null ? 0 : logonUser.getChooseDealer();
		StringBuffer sbSql = new StringBuffer();
		
		if (chooseDealer.longValue() == Constant.IF_TYPE_YES.longValue())
		{//限制当前职位所选择管理的经销商
			sbSql.append(" AND "+mark+".DEALER_ID IN (\n");
			sbSql.append("SELECT   TPD.DEALER_ID \n"); 
			sbSql.append("  FROM   TR_POSE_DEALER TPD\n"); 
			sbSql.append(" WHERE   TPD.POSE_ID = "+logonUser.getPoseId()+")\n"); 
		}
		else
		{
			sbSql.append(" AND "+mark+".DEALER_ID IN( \n");
			sbSql.append("SELECT TRD.DEALER_ID FROM TR_POSE_REGION_DLR_SERVICE  TRD ");
		    sbSql.append("WHERE  TRD.POSE_ID="+logonUser.getPoseId()+")\n");
		} 
		return sbSql.toString();
		
	}
	
	public static String getOrgDealerLimitSqlBySpecial(String mark, AclUserBean logonUser)
	{
		Integer chooseDealer = logonUser.getChooseDealer() == null ? 0 : logonUser.getChooseDealer();
		StringBuffer sbSql = new StringBuffer();
		
		if (chooseDealer.longValue() == Constant.IF_TYPE_YES.longValue())
		{//限制当前职位所选择管理的经销商
			sbSql.append(" AND "+mark+".dealer_id_f IN (\n");
			sbSql.append("SELECT   TPD.DEALER_ID \n"); 
			sbSql.append("  FROM   TR_POSE_DEALER TPD\n"); 
			sbSql.append(" WHERE   TPD.POSE_ID = "+logonUser.getPoseId()+"\n"); 
		}
		else
		{
			sbSql.append(" AND "+mark+".dealer_id_f IN( \n");
			sbSql.append("SELECT TRD.DEALER_ID FROM TR_POSE_REGION_DLR_SERVICE  TRD ");
		    sbSql.append("WHERE  TRD.POSE_ID="+logonUser.getPoseId()+")\n");
		} 
		return sbSql.toString();
		
	}
	//区域限制 增加是如果选择经销商 把服务站dealer_id转成经销商dealer_ID
	public static String getOrgDealerLimitSqlByServiceManager(String mark, AclUserBean logonUser)
	{
		Integer chooseDealer = logonUser.getChooseDealer() == null ? 0 : logonUser.getChooseDealer();
		Integer poseBusType = logonUser.getPoseBusType();
		StringBuffer sbSql = new StringBuffer();
		if (chooseDealer.longValue() == Constant.IF_TYPE_YES.longValue()) {
//			sbSql.append(" AND " + mark + ".DEALER_ID IN (\n");
//			sbSql.append("SELECT CASE\n" );
//			sbSql.append("         WHEN SUBSTR(T1.DEALER_CODE, -1, 1) = 'F' THEN\n" );
//			sbSql.append("          (SELECT DEALER_ID\n" );
//			sbSql.append("             FROM TM_DEALER\n" );
//			sbSql.append("            WHERE DEALER_CODE = REPLACE(T1.DEALER_CODE, 'F', 'S'))\n" );
//			sbSql.append("         ELSE\n" );
//			sbSql.append("          T1.DEALER_ID\n" );
//			sbSql.append("       END\n" );
//			sbSql.append("  FROM TR_POSE_DEALER T, TM_DEALER T1\n" );
//			sbSql.append(" WHERE T.DEALER_ID = T1.DEALER_ID\n" );
//			sbSql.append("   AND T.POSE_ID = " + logonUser.getPoseId() + ")");
			
			//根据绑定关系查询经销商id
			if (Constant.POSE_BUS_TYPE_WR == poseBusType.intValue()) {
				sbSql.append(" AND " + mark + ".DEALER_ID IN (\n");
				sbSql.append(" select xs_dealer_id from TT_DEALER_RELATION where "
						+ "xs_dealer_id in (select dealer_id from TR_POSE_DEALER where pose_id = " + logonUser.getPoseId() + " group by dealer_id) or "
						+ "sh_dealer_id in (select dealer_id from TR_POSE_DEALER where pose_id = " + logonUser.getPoseId() + " group by dealer_id)");
				sbSql.append(" )");
			} else {
				sbSql.append(" AND " + mark + ".DEALER_ID IN (\n");
				sbSql.append("select dealer_id from TR_POSE_DEALER where pose_id = " + logonUser.getPoseId() + " group by dealer_id ");
				sbSql.append(" )");
			}

		} else  {
			sbSql.append(" AND " + mark + ".DEALER_ID IN( \n");
			sbSql.append("SELECT TRD.DEALER_ID FROM TR_POSE_REGION_DEALER  TRD ");
			sbSql.append("WHERE  TRD.POSE_ID=" + logonUser.getPoseId() + ")\n");
		}
		return sbSql.toString();
	}
	
	public static String getOrgDealerLimitSqlByServiceA(String mark, AclUserBean logonUser)
	{
		StringBuffer sbSql = new StringBuffer();
		
		
		sbSql.append(" AND "+mark+".DEALER_ID IN( \n");
		sbSql.append("SELECT TRD.DEALER_ID FROM TR_POSE_REGION_DLR_SERVICE  TRD ");
	    sbSql.append("WHERE  TRD.POSE_ID="+logonUser.getPoseId()+")\n");
		return sbSql.toString();
		
	}
	
	public static String getOrgDealerLimitSqlByServiceD(String mark, long poseid)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TRD.DEALER_ID FROM TR_POSE_REGION_DLR_SERVICE  TRD ");
	    sbSql.append("WHERE  TRD.POSE_ID="+poseid+"\n");
		return sbSql.toString();
		
	}
	
	public static String getOrgDealerLimitSqlByServiceC(String mark, AclUserBean logonUser)
	{
		StringBuffer sbSql = new StringBuffer();
		
		
		sbSql.append(" AND "+mark+".DEALERID IN( \n");
		sbSql.append("SELECT TRD.DEALER_ID FROM TR_POSE_REGION_DLR_SERVICE  TRD ");
	    sbSql.append("WHERE  TRD.POSE_ID="+logonUser.getPoseId()+")\n");
	    System.out.println(sbSql.toString());
		return sbSql.toString();
		
	}
	
	public static String getOrgDealerLimitSqlByServiceB(String mark, AclUserBean logonUser)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TRD.DEALER_ID FROM TR_POSE_REGION_DLR_SERVICE  TRD ");
	    sbSql.append("WHERE  TRD.POSE_ID="+logonUser.getPoseId()+"\n");
		return sbSql.toString();
		
	}
	
	/**
	 * 按机构查询条件(无经销商)
	 * 
	 * @param orgIdColumnName
	 * @param orgCode
	 * @return
	 */
	public static String orgQueryCon(String orgIdColumnName, String orgCode)
	{
		String orgCodeStr = "";
		if (!"".equals(orgCode) && orgCode != null)
		{
			String[] array = orgCode.split(",");
			for (int i = 0; i < array.length; i++)
			{
				orgCodeStr += "'" + array[i] + "',";
			}
		}
		orgCodeStr = orgCodeStr.substring(0, orgCodeStr.length() - 1);
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("       AND EXISTS (    SELECT   1\n");
		sbSql.append("                       FROM   TM_ORG TTO\n");
		sbSql.append("                      WHERE   TTO.STATUS = "
						+ Constant.STATUS_ENABLE + "\n");
		sbSql.append("                              AND TTO.ORG_ID=" + orgIdColumnName + "\n");
		sbSql.append("                 START WITH   TTO.ORG_CODE IN(" + orgCodeStr + ")");
		
		sbSql.append("                 CONNECT BY   PRIOR TTO.ORG_ID = TTO.PARENT_ORG_ID)\n");
		return sbSql.toString();
		
	}
	
	/**
	 * 按机构查询条件(有经销商)
	 * 
	 * @param orgIdColumnName
	 * @param orgCode
	 * @return
	 */
	public static String orgQueryConHaveDealer(String dealerIdColumnName, String orgCode)
	{
		String orgCodeStr = "";
		if (!"".equals(orgCode) && orgCode != null)
		{
			String[] array = orgCode.split(",");
			for (int i = 0; i < array.length; i++)
			{
				orgCodeStr += "'" + array[i] + "',";
			}
		}
		orgCodeStr = orgCodeStr.substring(0, orgCodeStr.length() - 1);
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("AND EXISTS\n");
		sbSql.append("(    SELECT   1\n");
		sbSql.append("       FROM   TM_DEALER KK\n");
		sbSql.append("      WHERE   KK.DEALER_ID = " + dealerIdColumnName + " \n");
		sbSql.append(" START WITH   DEALER_ID IN\n");
		sbSql.append("                    (SELECT   RELA.DEALER_ID\n");
		sbSql.append("                       FROM   TM_DEALER_ORG_RELATION RELA\n");
		sbSql.append("                      WHERE   EXISTS\n");
		sbSql.append("                                 (    SELECT   1\n");
		sbSql.append("                                        FROM   TM_ORG TTO\n");
		sbSql.append("                                       WHERE   RELA.ORG_ID = TTO.ORG_ID\n");
		sbSql.append("                                               AND TTO.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sbSql.append("                                  START WITH   TTO.ORG_CODE IN(" + orgCodeStr + ")\n");
		sbSql.append("                                  CONNECT BY   PRIOR TTO.ORG_ID =\n");
		sbSql.append("                                                  TTO.PARENT_ORG_ID))\n");
		sbSql.append(" CONNECT BY   PRIOR KK.DEALER_ID = KK.PARENT_DEALER_D)\n");
		return sbSql.toString();
		
	}
	
	/**
	 * Function : 查询物料组下拉框共用方法
	 * 
	 * @param : 职位ID
	 * @param : 显示物料等级
	 */
	public static List<Map<String, Object>> getGroupDropDownBox(String poseId, String level)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
		sql.append("  FROM TM_BUSINESS_AREA       TBA,\n");
		sql.append("       TM_POSE_BUSINESS_AREA  TPBA,\n");
		sql.append("       TM_AREA_GROUP          TAP,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append(" WHERE TBA.AREA_ID = TPBA.AREA_ID\n");
		sql.append("   AND TPBA.AREA_ID = TAP.AREA_ID\n");
		sql.append("   AND TAP.MATERIAL_GROUP_ID = TVMG.GROUP_ID\n");
		if (!"".equals(poseId) && poseId != null)
		{
			sql.append("   AND TPBA.POSE_ID = " + poseId + "\n");
		}
		if (!"".equals(level) && level != null)
		{
			sql.append("   AND TVMG.GROUP_LEVEL = " + level + "\n");
		}
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * 取得当前职位下级所有物料组
	 * 
	 * @param poseId
	 * @param level
	 * @return
	 */
	public static String getMaterialsByPose(String poseId, String level)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT TVMG.GROUP_ID \n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append(" WHERE TVMG.GROUP_LEVEL = " + level + "\n");
		sql.append("   AND TVMG.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append(" START WITH TVMG.GROUP_ID IN\n");
		sql.append("            (SELECT TAG.MATERIAL_GROUP_ID\n");
		sql.append("               FROM TM_AREA_GROUP TAG, TM_POSE_BUSINESS_AREA TPBA\n");
		sql.append("              WHERE TAG.AREA_ID = TPBA.AREA_ID\n");
		sql.append("                AND TPBA.POSE_ID = " + poseId + ")\n");
		sql.append("CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID");
		return sql.toString();
	}
	
	public static List<Map<String, Object>> getGroupDropDownBox(String poseId, String level, String companyId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append(" WHERE TVMG.GROUP_LEVEL = " + level + "\n");
		//sql.append("   AND TVMG.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND TVMG.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append(" START WITH TVMG.GROUP_ID IN\n");
		sql.append("            (SELECT TAG.MATERIAL_GROUP_ID\n");
		sql.append("               FROM TM_AREA_GROUP TAG, TM_POSE_BUSINESS_AREA TPBA\n");
		sql.append("              WHERE TAG.AREA_ID = TPBA.AREA_ID\n");
		sql.append("                AND TPBA.POSE_ID = " + poseId + ")\n");
		sql.append("CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID\n");  //向上
		sql.append("UNION\n");
		sql.append("SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append(" WHERE TVMG.GROUP_LEVEL = " + level + "\n");
		//sql.append("   AND TVMG.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND TVMG.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append(" START WITH TVMG.GROUP_ID IN\n");
		sql.append("            (SELECT TAG.MATERIAL_GROUP_ID\n");
		sql.append("               FROM TM_AREA_GROUP TAG, TM_POSE_BUSINESS_AREA TPBA\n");
		sql.append("              WHERE TAG.AREA_ID = TPBA.AREA_ID\n");
		sql.append("                AND TPBA.POSE_ID = " + poseId + ")\n");
		sql.append("CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID");//向下
		
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	public static List<Map<String, Object>> getGroupByAreaId(String areaId, String level, String companyId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append(" WHERE TVMG.GROUP_LEVEL = " + level + "\n");
		//sql.append("   AND TVMG.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND TVMG.STATUS = " + Constant.STATUS_ENABLE + "\n");
		if (null != areaId && !"".equals(areaId))
		{
			sql.append(" START WITH TVMG.GROUP_ID IN\n");
			sql.append("            (SELECT TAG.MATERIAL_GROUP_ID\n");
			sql.append("               FROM TM_AREA_GROUP TAG\n");
			sql.append("              WHERE TAG.AREA_ID = " + areaId + ")\n");
			sql.append("CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID\n");
		}
		sql.append("UNION\n");
		sql.append("SELECT TVMG.GROUP_ID, TVMG.GROUP_CODE, TVMG.GROUP_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append(" WHERE TVMG.GROUP_LEVEL = " + level + "\n");
		//sql.append("   AND TVMG.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND TVMG.STATUS = " + Constant.STATUS_ENABLE + "\n");
		if (null != areaId && !"".equals(areaId))
		{
			sql.append(" START WITH TVMG.GROUP_ID IN\n");
			sql.append("            (SELECT TAG.MATERIAL_GROUP_ID\n");
			sql.append("               FROM TM_AREA_GROUP TAG\n");
			sql.append("              WHERE TAG.AREA_ID = " + areaId + ")\n");
			sql.append("CONNECT BY PRIOR TVMG.PARENT_GROUP_ID = TVMG.GROUP_ID\n");
		}
		
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * Function : 查询经销商业务范围
	 * 
	 * @param : 职位ID
	 */
	public static List<Map<String, Object>> getDealerBusiness(String poseId, String dlrLevel)
	{
		List<Map<String, Object>> list = null;
		
		StringBuffer sql = new StringBuffer("\n");
		
		sql.append("SELECT TBA.AREA_CODE, TBA.AREA_ID, TBA.AREA_NAME, TD.DEALER_ID,TBA.AREA_TYPE\n");
		sql.append("  FROM TC_POSE                 TP,\n");
		sql.append("       TM_DEALER               TD,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");
		sql.append("       TM_BUSINESS_AREA        TBA,\n");
		sql.append("       TM_POSE_BUSINESS_AREA   TPBA\n");
		sql.append(" WHERE TP.ORG_ID = TD.DEALER_ORG_ID\n");
		sql.append("   AND TD.DEALER_ID = TDBA.DEALER_ID\n");
		sql.append("   AND TDBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TPBA.POSE_ID = TP.POSE_ID\n");
		sql.append("   AND TDBA.AREA_ID = TPBA.AREA_ID\n");
		sql.append("   AND TD.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND (TD.DEALER_TYPE = " + Constant.DEALER_TYPE_DVS + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_JSZX + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_QYZDL + ")\n");
		
		if (!"".equals(poseId) && poseId != null)
		{
			sql.append("   AND TP.POSE_ID = " + poseId + "\n");
			;
		}
		
		if (!CommonUtils.isNullString(dlrLevel))
		{
			sql.append(" AND DEALER_LEVEL = ").append(dlrLevel).append("\n");
		}
		
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		
		return list;
	}
	
	/**
	 * 查询业务范围和资金类型
	 */
	public static List<Map<String, Object>> getDealerBusinessAndAccountType(String poseId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TBA.AREA_CODE,\n");
		sql.append("       TBA.AREA_ID,\n");
		sql.append("       TBA.AREA_NAME,\n");
		sql.append("       TD.DEALER_ID,\n");
		sql.append("       TVAT.TYPE_NAME,\n");
		sql.append("       TVAT.TYPE_ID\n");
		sql.append("  FROM TC_POSE                 TP,\n");
		sql.append("       TM_DEALER               TD,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");
		sql.append("       TM_BUSINESS_AREA        TBA,\n");
		sql.append("       TM_POSE_BUSINESS_AREA   TPBA,\n");
		sql.append("       TT_VS_ACCOUNT           TVA,\n");
		sql.append("       TT_VS_ACCOUNT_TYPE      TVAT\n");
		sql.append(" WHERE TP.ORG_ID = TD.DEALER_ORG_ID\n");
		sql.append("   AND TD.DEALER_ID = TDBA.DEALER_ID\n");
		sql.append("   AND TDBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TPBA.POSE_ID = TP.POSE_ID\n");
		sql.append("   AND TDBA.AREA_ID = TPBA.AREA_ID\n");
		sql.append("   AND TD.DEALER_ID = TVA.DEALER_ID\n");
		sql.append("   AND TVA.ACCOUNT_TYPE_ID = TVAT.TYPE_ID\n");
		sql.append("   AND TD.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND (TD.DEALER_TYPE = " + Constant.DEALER_TYPE_DVS + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_JSZX + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_QYZDL + ")\n");
		if (!"".equals(poseId) && poseId != null)
		{
			sql.append("   AND TP.POSE_ID = " + poseId + "\n");
			;
		}
		sql.append(" ORDER BY TBA.AREA_NAME DESC\n");
		
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	public static List<Map<String, Object>> getJszxDealerBusiness(String poseId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TBA.AREA_CODE, TBA.AREA_ID, TBA.AREA_NAME, TD.DEALER_ID\n");
		sql.append("  FROM TC_POSE                 TP,\n");
		sql.append("       TM_DEALER               TD,\n");
		sql.append("       TM_DEALER               TD1,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");
		sql.append("       TM_BUSINESS_AREA        TBA,\n");
		sql.append("       vw_org_dealer        vod,\n");
		sql.append("       TM_POSE_BUSINESS_AREA   TPBA\n");
		sql.append(" WHERE TP.ORG_ID = TD.DEALER_ORG_ID\n");
		sql.append("   AND TD.DEALER_ID = TDBA.DEALER_ID\n");
		sql.append("   AND TDBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TPBA.POSE_ID = TP.POSE_ID\n");
		sql.append("   AND TDBA.AREA_ID = TPBA.AREA_ID\n");
		sql.append("   AND TD.dealer_id = vod.DEALER_ID\n");
		sql.append("   AND TD1.dealer_id = vod.root_DEALER_ID\n");
		sql.append("   AND TD.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		//sql.append("   AND TD.DEALER_TYPE = "+Constant.DEALER_TYPE_DVS+"\n");
		sql.append("   AND (TD.DEALER_TYPE = " + Constant.DEALER_TYPE_DVS + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_JSZX + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_QYZDL + ")\n");
		sql.append("   AND TD1.DEALER_TYPE = " + Constant.DEALER_TYPE_JSZX + "\n");
		if (!"".equals(poseId) && poseId != null)
		{
			sql.append("   AND TP.POSE_ID = " + poseId + "\n");
			;
		}
		
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * 获得非结算中心业务范围
	 * 
	 * @param poseId
	 * @return
	 */
	public static List<Map<String, Object>> getDealerBusinessNoJszx(String poseId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TBA.AREA_CODE, TBA.AREA_ID, TBA.AREA_NAME, TD.DEALER_ID\n");
		sql.append("  FROM TC_POSE                 TP,\n");
		sql.append("       TM_DEALER               TD,\n");
		sql.append("       TM_DEALER               TD1,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");
		sql.append("       TM_BUSINESS_AREA        TBA,\n");
		sql.append("       TM_POSE_BUSINESS_AREA   TPBA,VW_ORG_DEALER VOD\n");
		sql.append(" WHERE TP.ORG_ID = TD.DEALER_ORG_ID\n");
		sql.append("   AND TD.DEALER_ID = TDBA.DEALER_ID\n");
		sql.append("   AND TDBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TPBA.POSE_ID = TP.POSE_ID\n");
		sql.append("   AND TDBA.AREA_ID = TPBA.AREA_ID\n");
		sql.append("   AND VOD.DEALER_ID = TD.DEALER_ID\n");
		sql.append("   AND VOD.ROOT_DEALER_ID = TD1.DEALER_ID\n");
		sql.append("   AND TD.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		//sql.append("   AND TD.DEALER_TYPE = "+Constant.DEALER_TYPE_DVS+"\n");
		sql.append("   AND (TD.DEALER_TYPE = " + Constant.DEALER_TYPE_DVS + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_JSZX + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_QYZDL + ")\n");
		sql.append("   AND TD1.DEALER_TYPE <> " + Constant.DEALER_TYPE_JSZX + "\n");
		if (!"".equals(poseId) && poseId != null)
		{
			sql.append("   AND TP.POSE_ID = " + poseId + "\n");
			;
		}
		
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	public static List<Map<String, Object>> getDealerBusinessAll(String poseId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TBA.AREA_ID, TBA.AREA_CODE, TBA.AREA_NAME, TPBA.POSE_ID\n");
		sql.append("  FROM TM_POSE_BUSINESS_AREA TPBA, TM_BUSINESS_AREA TBA\n");
		sql.append(" WHERE TPBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TPBA.POSE_ID = " + poseId + "\n");
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * Function : 查询一级经销商业务范围
	 * 
	 * @param : 职位ID
	 */
	public static List<Map<String, Object>> getDealerLevelBusiness(String poseId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TBA.AREA_CODE, TBA.AREA_ID, TBA.AREA_NAME, TD.DEALER_ID\n");
		sql.append("  FROM TC_POSE                 TP,\n");
		sql.append("       TM_DEALER               TD,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");
		sql.append("       TM_BUSINESS_AREA        TBA,\n");
		sql.append("       TM_POSE_BUSINESS_AREA   TPBA\n");
		sql.append(" WHERE TP.ORG_ID = TD.DEALER_ORG_ID\n");
		sql.append("   AND TD.DEALER_ID = TDBA.DEALER_ID\n");
		sql.append("   AND TDBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TPBA.POSE_ID = TP.POSE_ID\n");
		sql.append("   AND TDBA.AREA_ID = TPBA.AREA_ID\n");
		sql.append("   AND TD.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TD.DEALER_LEVEL = " + Constant.DEALER_LEVEL_01 + "\n");
		sql.append("   AND (TD.DEALER_TYPE = " + Constant.DEALER_TYPE_DVS + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_JSZX + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_QYZDL + ")\n");
		if (!"".equals(poseId) && poseId != null)
		{
			sql.append("   AND TP.POSE_ID = " + poseId + "\n");
			;
		}
		
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * Function : 查询所有经销商业务范围
	 * 
	 * @param : 职位ID
	 */
	public static List<Map<String, Object>> getDealerLevelBusinessAll(String poseId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TBA.AREA_CODE, TBA.AREA_ID, TBA.AREA_NAME, TD.DEALER_ID\n");
		sql.append("  FROM TC_POSE                 TP,\n");
		sql.append("       TM_DEALER               TD,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");
		sql.append("       TM_BUSINESS_AREA        TBA,\n");
		sql.append("       TM_POSE_BUSINESS_AREA   TPBA\n");
		sql.append(" WHERE TP.ORG_ID = TD.DEALER_ORG_ID\n");
		sql.append("   AND TD.DEALER_ID = TDBA.DEALER_ID\n");
		sql.append("   AND TDBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TPBA.POSE_ID = TP.POSE_ID\n");
		sql.append("   AND TDBA.AREA_ID = TPBA.AREA_ID\n");
		sql.append("   AND TD.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND (TD.DEALER_TYPE = " + Constant.DEALER_TYPE_DVS + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_JSZX + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_QYZDL + ")\n");
		if (!"".equals(poseId) && poseId != null)
		{
			sql.append("   AND TP.POSE_ID = " + poseId + "\n");
			;
		}
		
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * 需求预测:旗舰店上报预测
	 * 
	 * @param poseId
	 * @return
	 */
	public static List<Map<String, Object>> getDealerQJD(String poseId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TBA.AREA_CODE, TBA.AREA_ID, TBA.AREA_NAME, TD.DEALER_ID\n");
		sql.append("  FROM TC_POSE                 TP,\n");
		sql.append("       TM_DEALER               TD,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");
		sql.append("       TM_BUSINESS_AREA        TBA,\n");
		sql.append("       TM_POSE_BUSINESS_AREA   TPBA\n");
		sql.append(" WHERE TP.ORG_ID = TD.DEALER_ORG_ID\n");
		sql.append("   AND TD.DEALER_ID = TDBA.DEALER_ID\n");
		sql.append("   AND TDBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TPBA.POSE_ID = TP.POSE_ID\n");
		sql.append("   AND TDBA.AREA_ID = TPBA.AREA_ID\n");
		sql.append("   AND TD.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TD.DEALER_LEVEL = " + Constant.DEALER_LEVEL_02 + "\n");
		sql.append("   and td.dealer_class = " + Constant.DEALER_CLASS_TYPE_12 + "\n");
		if (!"".equals(poseId) && poseId != null)
		{
			sql.append("   AND TP.POSE_ID = " + poseId + "\n");
			;
		}
		
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * Function : 查询经销商业务范围id字符串
	 * 
	 * @param : 职位ID
	 */
	public static String getDealerBusinessIdStr(String poseId)
	{
		List<Map<String, Object>> list = getDealerBusiness(poseId);
		String areaIds = "";
		for (int i = 0; i < list.size(); i++)
		{
			BigDecimal temp = (BigDecimal) list.get(i).get("AREA_ID");
			areaIds += temp;
			if (i != list.size() - 1)
			{
				areaIds += ",";
			}
		}
		return areaIds;
	}
	
	public static String getDWRDealerBusinessIdStr(String poseId)
	{
		List<Map<String, Object>> list = getDealerBusinessAll(poseId);
		String areaIds = "";
		for (int i = 0; i < list.size(); i++)
		{
			BigDecimal temp = (BigDecimal) list.get(i).get("AREA_ID");
			areaIds += temp;
			if (i != list.size() - 1)
			{
				areaIds += ",";
			}
		}
		return areaIds;
	}
	
	/**
	 * Function : 查询一级经销商业务范围id字符串
	 * 
	 * @param : 职位ID
	 */
	public static String getDealerLevelBusinessIdStr(String poseId)
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		String para = CommonDAO.getPara(Constant.CHANA_SYS.toString());
		
		if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase()))
		{
			list = getDealerLevelBusiness(poseId);
		}
		else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase()))
		{
			list = getDealerLevelBusinessAll(poseId);
		}
		else
		{
			throw new RuntimeException("判断当前系统的系统参数错误！");
		}
		
		String areaIds = "";
		for (int i = 0; i < list.size(); i++)
		{
			BigDecimal temp = (BigDecimal) list.get(i).get("AREA_ID");
			areaIds += temp;
			if (i != list.size() - 1)
			{
				areaIds += ",";
			}
		}
		return areaIds;
	}
	
	public static String getMyPoseBusiness(String poseId)
	{
		String areaId = "";
		if (MaterialGroupManagerDao.getPoseIdBusiness(poseId).size() > 0)
		{
			for (int i = 0; i < MaterialGroupManagerDao.getPoseIdBusiness(poseId).size(); i++)
			{
				if (i + 1 != MaterialGroupManagerDao.getPoseIdBusiness(poseId).size())
				{
					areaId = MaterialGroupManagerDao.getPoseIdBusiness(poseId).get(i).get("AREA_ID").toString() + "," + areaId;
				}
				else
				{
					areaId = areaId + MaterialGroupManagerDao.getPoseIdBusiness(poseId).get(i).get("AREA_ID")
									.toString();
				}
			}
		}
		return areaId;
	}
	
	/**
	 * Function : 查询生产基地
	 * 
	 * @param : 职位ID
	 */
	public static List<Map<String, Object>> getProduceAdd()
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DECODE(TBA.ERP_CODE,\n");
		sql.append("726,\n");
		sql.append("'重庆',\n");
		sql.append("82,\n");
		sql.append("'重庆',\n");
		sql.append("142,\n");
		sql.append("'河北',\n");
		sql.append("197,\n");
		sql.append("'南京') CODE_DESC\n");
		sql.append("FROM TM_BUSINESS_AREA TBA\n");
		sql.append("WHERE TBA.ERP_CODE IS NOT NULL\n");
		sql.append("GROUP BY TBA.ERP_CODE\n");
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * 查询当前经销商的职位
	 * 和业务范围
	 */
	public static List<Map<String, Object>> getPoseIdDealerBusiness(String poseId, String dealerId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TBA.AREA_ID, TBA.AREA_CODE, TBA.AREA_NAME, TPBA.POSE_ID\n");
		sql.append("  FROM TM_POSE_BUSINESS_AREA TPBA, TM_BUSINESS_AREA TBA\n");
		sql.append(" WHERE TPBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TPBA.POSE_ID = " + poseId + "\n");
		sql.append(" AND TPBA.POSE_ID=TP.POSE_ID\n");
		sql.append(" AND TP.ORG_ID=TDOR.ORG_ID\n");
		sql.append(" AND TDOR.DEALER_ID IN(" + dealerId + ")\n");
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * Function : 查询车厂职位业务范围id字符串
	 * 
	 * @param : 职位ID
	 */
	public static String getPoseIdBusinessIdStr(String poseId)
	{
		List<Map<String, Object>> list = getPoseIdBusiness(poseId);
		String areaIds = "";
		for (int i = 0; i < list.size(); i++)
		{
			BigDecimal temp = (BigDecimal) list.get(i).get("AREA_ID");
			areaIds += temp;
			if (i != list.size() - 1)
			{
				areaIds += ",";
			}
		}
		return areaIds;
	}
	
	public static TcPosePO getTcPosePO(Long poseId)
	{
		TcPosePO po = new TcPosePO();
		po.setPoseId(poseId);
		List<TcPosePO> list = factory.select(po);
		return list.size() != 0 ? list.get(0) : null;
	}
	
	public static String getAreaIdsByPoseId(Long poseId)
	{
		TcPosePO posePo = getTcPosePO(poseId);
		String areaIds = "";
		if (posePo != null)
		{
			// 判断职位是经销商还是车厂端
			if (posePo.getPoseType().intValue() == Constant.SYS_USER_SGM.intValue())
			{
				areaIds = getPoseIdBusinessIdStr(poseId.toString());
			}
			else
			{
				//add by liuqiang
				//判断如果是售后经销商,可以查看全部经销商,售后没有经销商用户选择经销商的功能,在车辆变更记录里加了这样的功能
				//所以这里做这样的判断,查询全部的经销商
				if (posePo.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_DWR)
				{
					areaIds = getDWRDealerBusinessIdStr(poseId.toString());
				}
				else
				{
					areaIds = getDealerBusinessIdStr(poseId.toString());
				}
			}
		}
		return areaIds;
	}
	
	/**
	 * Function : 查询物料组弹出树
	 * 
	 * @param : 职位ID
	 */
	public static List<TmVhclMaterialGroupPO> getMaterialGroupTree(Long poseId, String groupLevel, Long companyId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT GROUP_ID,GROUP_NAME,GROUP_CODE,PARENT_GROUP_ID\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T\n");
		sql.append(" WHERE T.STATUS=" + Constant.STATUS_ENABLE + " \n");
		//sql.append("  AND  T.COMPANY_ID="+companyId+" \n"); 
		
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append(" AND T.GROUP_LEVEL <= " + groupLevel + "\n");
		}
		
		sql.append(" ORDER BY GROUP_ID \n");
		
		List<TmVhclMaterialGroupPO> list = factory.select(sql.toString(), null,
						new DAOCallback<TmVhclMaterialGroupPO>() {
							public TmVhclMaterialGroupPO wrapper(ResultSet rs, int idx)
							{
								TmVhclMaterialGroupPO bean = new TmVhclMaterialGroupPO();
								try
								{
									bean.setGroupId(Long.valueOf(rs.getString("GROUP_ID")));
									if (rs.getString("PARENT_GROUP_ID") != null)
									{
										bean.setParentGroupId(Long.valueOf(rs.getString("PARENT_GROUP_ID")));
									}
									else
									{
										bean.setParentGroupId(new Long(-1));
									}
									
									bean.setGroupName(rs.getString("GROUP_NAME"));
									bean.setGroupCode(rs.getString("GROUP_CODE"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	
	/**
	 * 订单新增查询物料树
	 * @param poseId
	 * @param groupLevel
	 * @param companyId
	 * @param isAllArea
	 * @param logonUser
	 * @return
	 */
	public List<TmVhclMaterialGroupPO> getMaterialGroupTreeByOrder(Long poseId, String groupLevel,
			Long companyId, String warehouseId, AclUserBean logonUser)
		{
		StringBuffer sql = new StringBuffer();
		List par=new ArrayList();
		sql.append("select T.GROUP_ID, T.GROUP_CODE, T.GROUP_NAME, T.PARENT_GROUP_ID, T.GROUP_LEVEL \n");
		sql.append(" from tm_vhcl_material_group T \n");
		sql.append("WHERE T.STATUS = 10011001 \n");
		sql.append(" AND T.GROUP_LEVEL <= "+groupLevel+" \n");
		sql.append(" AND T.GROUP_ID IN \n");
		sql.append("  (select T.group_id \n");
		sql.append("     from tm_vhcl_material_group t \n");
		sql.append("    start with T.group_id IN \n");
		sql.append("(select group_id from tm_vhcl_material_group) \n");
		sql.append("   connect by prior T.group_id = T.parent_group_id ) \n");
		if(!"".equals(warehouseId)&&warehouseId!=null){
			sql.append("and t.group_id in \n");
			sql.append("  ((SELECT GROUP_ID \n");
			sql.append("   FROM tm_vhcl_material_group \n");
			sql.append("    START WITH group_id in (select material_group_id \n");
			sql.append("                           from tm_warehouse_group \n");
			sql.append("                          where warehouse_id = "+warehouseId+") \n");
			sql.append(" CONNECT BY PRIOR parent_group_id = group_id \n");
			sql.append(" union \n");
			sql.append("  SELECT GROUP_ID \n");
			sql.append("   FROM tm_vhcl_material_group \n");
			sql.append("  START WITH group_id in (select material_group_id \n");
			sql.append("                           from tm_warehouse_group \n");
			sql.append("                         where warehouse_id = "+warehouseId+") \n");
			sql.append(" CONNECT BY parent_group_id = PRIOR group_id)) \n");
		}
		sql.append("   ORDER BY GROUP_ID  \n");
		List<TmVhclMaterialGroupPO> list = factory.select(sql.toString(), par,
			new DAOCallback<TmVhclMaterialGroupPO>() {
			public TmVhclMaterialGroupPO wrapper(ResultSet rs, int idx)
			{
				TmVhclMaterialGroupPO bean = new TmVhclMaterialGroupPO();
				try
				{
					bean.setGroupId(Long.valueOf(rs.getString("GROUP_ID")));
					if (rs.getString("PARENT_GROUP_ID") != null)
					{
						bean.setParentGroupId(Long.valueOf(rs.getString("PARENT_GROUP_ID")));
					}
					else
					{
						bean.setParentGroupId(new Long(-1));
					}
					
					bean.setGroupName(rs.getString("GROUP_NAME"));
					bean.setGroupCode(rs.getString("GROUP_CODE"));
					bean.setGroupLevel(rs.getInt("GROUP_LEVEL"));
						}
						catch (SQLException e)
						{
							throw new DAOException(e);
						}
						return bean;
					}
				});
		return list;
		}
	
	/**
	 * Function : 查询物料组弹出树过滤用户业务范围
	 * 
	 * @param : 职位ID
	 */
	public static List<TmVhclMaterialGroupPO> getMaterialGroupTreeByPoseArea(Long poseId, String groupLevel,
					Long companyId, String isAllArea, AclUserBean logonUser)
	{
		StringBuffer sql = new StringBuffer();
		List par=new ArrayList();
		sql.append("select T.GROUP_ID, T.GROUP_CODE, T.GROUP_NAME, T.PARENT_GROUP_ID, T.GROUP_LEVEL\n");
		sql.append("  from tm_vhcl_material_group T\n");
		sql.append(" WHERE T.STATUS = " + Constant.STATUS_ENABLE + "\n");
		//sql.append("   AND T.COMPANY_ID = "+companyId+"\n");
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append(" AND T.GROUP_LEVEL <= ?\n");
			par.add(Integer.valueOf(groupLevel));
		}
		if (!isAllArea.toLowerCase().equals("true"))
		{
			String areaIds = getAreaIdsByPoseId(poseId);
			
			if (areaIds != null && !areaIds.equals(""))
			{
				sql.append(" AND T.GROUP_ID IN\n");
				sql.append("    (select T.group_id\n");
				sql.append("       from tm_vhcl_material_group t\n");
				sql.append("      start with T.group_id IN\n");
				
				if (logonUser.getPoseType().intValue() == Constant.SYS_USER_DEALER.intValue())
				{//经销商只限定合同
					sql.append("(SELECT   B.GROUP_ID\n");
					sql.append("  FROM   TT_SALES_CONTRACT A, TT_SALES_CONTRACT_DTL B,tm_area_group c\n");
					sql.append(" WHERE       A.CONTRACT_ID = B.CONTRACT_ID AND B.GROUP_ID=C.MATERIAL_GROUP_ID\n");
					sql.append(Utility.getConSqlByParamForEqual(areaIds, par,"C", "AREA_ID"));
					sql.append("         AND A.DEALER_ID = " + logonUser.getDealerId() + "\n");
					sql.append("         AND A.CONTRACT_YEAR = TO_CHAR(SYSDATE,'YYYY'))\n");
				}
				else
				{
					sql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
					sql.append("                    FROM tm_area_group tap\n");
					sql.append("                   where 1=1\n");
					sql.append(Utility.getConSqlByParamForEqual(areaIds, par,"TAP", "AREA_ID"));
					sql.append("                  )\n");
				}
				
				sql.append("     connect by prior T.group_id = T.parent_group_id\n");
				sql.append("     union\n");
				sql.append("     select T.group_id\n");
				sql.append("       from tm_vhcl_material_group t\n");
				sql.append("      start with T.group_id IN\n");
				
				if (logonUser.getPoseType().intValue() == Constant.SYS_USER_DEALER.intValue())
				{//经销商只限定合同
					sql.append("(SELECT   B.GROUP_ID\n");
					sql.append("  FROM   TT_SALES_CONTRACT A, TT_SALES_CONTRACT_DTL B,tm_area_group c\n");
					sql.append(" WHERE       A.CONTRACT_ID = B.CONTRACT_ID AND B.GROUP_ID=C.MATERIAL_GROUP_ID \n");
					sql.append(Utility.getConSqlByParamForEqual(areaIds, par,"C", "AREA_ID"));
					
					sql.append("         AND A.DEALER_ID = ?\n");
					sql.append("         AND A.CONTRACT_YEAR = TO_CHAR(SYSDATE,'YYYY'))\n");
					par.add(logonUser.getDealerId());
				}
				else
				{
					sql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
					sql.append("                    FROM tm_area_group tap\n");
					sql.append("                   where 1=1\n");
					sql.append(Utility.getConSqlByParamForEqual(areaIds, par,"tap", "AREA_ID"));
					sql.append("                  )\n");
				}
				
				sql.append("     connect by prior T.parent_group_id = T.group_id)");
			}
		}
		sql.append(" ORDER BY GROUP_ID \n");
		
		List<TmVhclMaterialGroupPO> list = factory.select(sql.toString(), par,
						new DAOCallback<TmVhclMaterialGroupPO>() {
							public TmVhclMaterialGroupPO wrapper(ResultSet rs, int idx)
							{
								TmVhclMaterialGroupPO bean = new TmVhclMaterialGroupPO();
								try
								{
									bean.setGroupId(Long.valueOf(rs.getString("GROUP_ID")));
									if (rs.getString("PARENT_GROUP_ID") != null)
									{
										bean.setParentGroupId(Long.valueOf(rs.getString("PARENT_GROUP_ID")));
									}
									else
									{
										bean.setParentGroupId(new Long(-1));
									}
									
									bean.setGroupName(rs.getString("GROUP_NAME"));
									bean.setGroupCode(rs.getString("GROUP_CODE"));
									bean.setGroupLevel(rs.getInt("GROUP_LEVEL"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	/**
	 * Function : 查询物料组弹出树过滤用户业务范围
	 * 
	 * @param : 职位ID
	 */
	public static List<TmVhclMaterialGroupPO> getMaterialGroupTreeByPoseAreaAndConf(Long poseId, String groupLevel,
					Long companyId, String areaId, AclUserBean logonUser)
	{
		StringBuffer sql = new StringBuffer();
		
		sql.append("select T.GROUP_ID, T.GROUP_CODE, T.GROUP_NAME, T.PARENT_GROUP_ID, T.GROUP_LEVEL\n");
		sql.append("  from tm_vhcl_material_group T\n");
		sql.append(" WHERE T.STATUS = " + Constant.STATUS_ENABLE + "\n");
		//sql.append("   AND T.COMPANY_ID = "+companyId+"\n");
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append(" AND T.GROUP_LEVEL <= " + groupLevel + "\n");
		}
		
		if (areaId != null && !areaId.equals(""))
		{
			sql.append(" AND T.GROUP_ID IN\n");
			sql.append("    (select T.group_id\n");
			sql.append("       from tm_vhcl_material_group t\n");
			sql.append("      start with T.group_id IN\n");
			
			if (logonUser.getPoseType().intValue() == Constant.SYS_USER_DEALER.intValue())
			{//经销商只限定合同
				sql.append("(SELECT   B.GROUP_ID\n");
				sql.append("  FROM   TT_SALES_CONTRACT A, TT_SALES_CONTRACT_DTL B,tm_area_group c\n");
				sql.append(" WHERE       A.CONTRACT_ID = B.CONTRACT_ID AND B.GROUP_ID=C.MATERIAL_GROUP_ID AND C.AREA_ID IN(" + areaId + ")\n");
				sql.append("         AND A.DEALER_ID = " + logonUser.getDealerId() + "\n");
				sql.append("         AND A.CONTRACT_YEAR = TO_CHAR(SYSDATE,'YYYY'))\n");
			}
			else
			{
				sql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
				sql.append("                    FROM tm_area_group tap\n");
				sql.append("                   where tap.area_id in (" + areaId + "))\n");
			}
			
			sql.append("     connect by prior T.group_id = T.parent_group_id\n");
			sql.append("     union\n");
			sql.append("     select T.group_id\n");
			sql.append("       from tm_vhcl_material_group t\n");
			sql.append("      start with T.group_id IN\n");
			
			if (logonUser.getPoseType().intValue() == Constant.SYS_USER_DEALER.intValue())
			{//经销商只限定合同
				sql.append("(SELECT   B.GROUP_ID\n");
				sql.append("  FROM   TT_SALES_CONTRACT A, TT_SALES_CONTRACT_DTL B,tm_area_group c\n");
				sql.append(" WHERE       A.CONTRACT_ID = B.CONTRACT_ID AND B.GROUP_ID=C.MATERIAL_GROUP_ID AND C.AREA_ID IN(" + areaId + ")\n");
				sql.append("         AND A.DEALER_ID = " + logonUser.getDealerId() + "\n");
				sql.append("         AND A.CONTRACT_YEAR = TO_CHAR(SYSDATE,'YYYY'))\n");
			}
			else
			{
				sql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
				sql.append("                    FROM tm_area_group tap\n");
				sql.append("                   where tap.area_id in (" + areaId + "))\n");
				
			}
			sql.append("     connect by prior T.parent_group_id = T.group_id)");
		}
		sql.append(" ORDER BY GROUP_ID \n");
		
		List<TmVhclMaterialGroupPO> list = factory.select(sql.toString(), null,
						new DAOCallback<TmVhclMaterialGroupPO>() {
							public TmVhclMaterialGroupPO wrapper(ResultSet rs, int idx)
							{
								TmVhclMaterialGroupPO bean = new TmVhclMaterialGroupPO();
								try
								{
									bean.setGroupId(Long.valueOf(rs.getString("GROUP_ID")));
									if (rs.getString("PARENT_GROUP_ID") != null)
									{
										bean.setParentGroupId(Long.valueOf(rs.getString("PARENT_GROUP_ID")));
									}
									else
									{
										bean.setParentGroupId(new Long(-1));
									}
									
									bean.setGroupName(rs.getString("GROUP_NAME"));
									bean.setGroupCode(rs.getString("GROUP_CODE"));
									bean.setGroupLevel(rs.getInt("GROUP_LEVEL"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * Function : 供报表使用的查询物料组弹出树过滤用户业务范围
	 * 
	 * @param : 职位ID
	 *        2012-02-07 HXY
	 */
	public static List<TmVhclMaterialGroupPO> getMaterialGroupTreeByReport(Long poseId, String groupLevel,
					Long companyId, String isAllArea)
	{
		StringBuffer sql = new StringBuffer();
		
		sql.append("select T.GROUP_ID, T.GROUP_CODE, T.GROUP_NAME, T.PARENT_GROUP_ID, T.GROUP_LEVEL\n");
		sql.append("  from tm_vhcl_material_group T\n");
		sql.append(" WHERE T.STATUS = " + Constant.STATUS_ENABLE + "\n");
		//sql.append("   AND T.COMPANY_ID = "+companyId+"\n");
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append(" AND T.GROUP_LEVEL <= " + groupLevel + "\n");
		}
		/* if(!isAllArea.toLowerCase().equals("true")){
		 * String areaIds = getAreaIdsByPoseId(poseId);
		 * if(areaIds !=null && !areaIds.equals("")){
		 * sql.append(" AND T.GROUP_ID IN\n");
		 * sql.append("    (select T.group_id\n");
		 * sql.append("       from tm_vhcl_material_group t\n");
		 * sql.append("      start with T.group_id IN\n");
		 * sql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
		 * sql.append("                    FROM tm_area_group tap\n");
		 * sql.append("                   where tap.area_id in ("+areaIds+"))\n");
		 * sql.append("     connect by prior T.group_id = T.parent_group_id\n");
		 * sql.append("     union\n");
		 * sql.append("     select T.group_id\n");
		 * sql.append("       from tm_vhcl_material_group t\n");
		 * sql.append("      start with T.group_id IN\n");
		 * sql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
		 * sql.append("                    FROM tm_area_group tap\n");
		 * sql.append("                   where tap.area_id in ("+areaIds+"))\n");
		 * sql.append("     connect by prior T.parent_group_id = T.group_id)");
		 * }
		 * } */
		sql.append(" ORDER BY GROUP_ID \n");
		
		List<TmVhclMaterialGroupPO> list = factory.select(sql.toString(), null,
						new DAOCallback<TmVhclMaterialGroupPO>() {
							public TmVhclMaterialGroupPO wrapper(ResultSet rs, int idx)
							{
								TmVhclMaterialGroupPO bean = new TmVhclMaterialGroupPO();
								try
								{
									bean.setGroupId(Long.valueOf(rs.getString("GROUP_ID")));
									if (rs.getString("PARENT_GROUP_ID") != null)
									{
										bean.setParentGroupId(Long.valueOf(rs.getString("PARENT_GROUP_ID")));
									}
									else
									{
										bean.setParentGroupId(new Long(-1));
									}
									
									bean.setGroupName(rs.getString("GROUP_NAME"));
									bean.setGroupCode(rs.getString("GROUP_CODE"));
									bean.setGroupLevel(rs.getInt("GROUP_LEVEL"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * Function : 供报表使用物料组列表
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 车系、车型、配置CODE
	 * @param : 车系、车型、配置名称
	 *        2012-02-07 HXY
	 */
	public static PageResult<Map<String, Object>> getGroupListByReport(String poseId, String groupId, String groupCode,
					String groupName, String groupLevel, int curPage, int pageSize, Long companyId, String isAllArea)
	{
		StringBuffer sql = new StringBuffer("\n");
		boolean flag = false;
		String areaIds = "";
		
		if (!isAllArea.toLowerCase().equals("true"))
		{
			
			areaIds = getAreaIdsByPoseId(new Long(poseId));
			
			if (areaIds != null && !areaIds.equals(""))
			{
				flag = true;
			}
		}
		
		sql.append("SELECT T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");
		sql.append("       T.GROUP_NAME,\n");
		sql.append("       T.PARENT_GROUP_ID,\n");
		sql.append("       T.TREE_CODE,\n");
		sql.append("       T.Group_Level\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T");
		
		if (flag)
		{
			sql.append(",\n");
			sql.append("(select T.group_id\n");
			sql.append("          from tm_vhcl_material_group t\n");
			sql.append("         start with T.group_id IN\n");
			sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                       FROM tm_area_group tap\n");
			sql.append("                      where tap.area_id in (" + areaIds + "))\n");
			sql.append("        connect by prior T.group_id = T.parent_group_id\n");
			sql.append("        union\n");
			sql.append("        select T.group_id\n");
			sql.append("          from tm_vhcl_material_group t\n");
			sql.append("         start with T.group_id IN\n");
			sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                       FROM tm_area_group tap\n");
			sql.append("                      where tap.area_id in (" + areaIds + "))\n");
			sql.append("        connect by prior T.parent_group_id = T.group_id) t2\n");
		}
		
		//sql.append(" WHERE T.COMPANY_ID = " + companyId + "\n");
		sql.append(" WHERE 1=1 \n");
		if (!"".equals(groupCode) && groupCode != null)
		{
			sql.append("   AND T.GROUP_CODE LIKE '%" + groupCode + "%'\n");
		}
		if (!"".equals(groupName) && groupName != null)
		{
			sql.append("   AND T.GROUP_NAME LIKE '%" + groupName + "%'\n");
		}
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append("   AND T.GROUP_LEVEL = " + groupLevel + "\n");
		}
		
		sql.append("   AND T.STATUS = " + Constant.STATUS_ENABLE + "\n");
		
		if (flag)
		{
			sql.append("AND T.GROUP_ID = t2.GROUP_ID\n");
		}
		
		if (!"".equals(groupId) && groupId != null)
		{
			if ("".equals(groupLevel) || groupLevel == null || "null".equals(groupLevel))
			{
				sql.append(" AND  T.PARENT_GROUP_ID = " + groupId + "\n");
			}
			else
			{
				sql.append(" START WITH T.GROUP_ID = " + groupId + "\n");
				sql.append("CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID\n");
			}
			
		}
		
		sql.append(" ORDER BY T.GROUP_LEVEL, T.GROUP_CODE\n");
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function : 订做车需求提报使用配置(物料组)列表
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 车系、车型、配置CODE
	 * @param : 车系、车型、配置名称
	 *        2012-02-07 HXY
	 */
	public static PageResult<Map<String, Object>> getGroupListByConf(String poseId, String groupId, String groupCode,
					String groupName, String groupLevel, int curPage, int pageSize, Long companyId, String areaId,
					AclUserBean logonUser)
	{
		StringBuffer sql = new StringBuffer("\n");
		boolean flag = false;
		
		if (areaId != null && !areaId.equals(""))
		{
			flag = true;
		}
		
		sql.append("SELECT T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");
		sql.append("       T.GROUP_NAME,\n");
		sql.append("       T.PARENT_GROUP_ID,\n");
		sql.append("       T.TREE_CODE,\n");
		sql.append("       T.Group_Level\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T");
		
		if (flag)
		{
			sql.append(",\n");
			sql.append("(select T.group_id\n");
			sql.append("          from tm_vhcl_material_group t\n");
			sql.append("         start with T.group_id IN\n");
			
			if (logonUser.getPoseType().intValue() == Constant.SYS_USER_DEALER.intValue())
			{//经销商只限定合同
				sql.append("(SELECT   B.GROUP_ID\n");
				sql.append("  FROM    TT_SALES_CONTRACT A, TT_SALES_CONTRACT_DTL B,tm_area_group c\n");
				sql.append(" WHERE       A.CONTRACT_ID = B.CONTRACT_ID AND B.GROUP_ID=C.MATERIAL_GROUP_ID AND C.AREA_ID IN(" + areaId + ")\n");
				sql.append("         AND A.DEALER_ID = " + logonUser.getDealerId() + "\n");
				sql.append("         AND A.CONTRACT_YEAR = TO_CHAR(SYSDATE,'YYYY'))\n");
			}
			else
			{
				sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
				sql.append("                       FROM tm_area_group tap\n");
				sql.append("                      where tap.area_id in (" + areaId + "))\n");
			}
			
			sql.append("        connect by prior T.group_id = T.parent_group_id\n");
			sql.append("        union\n");
			sql.append("        select T.group_id\n");
			sql.append("          from tm_vhcl_material_group t\n");
			sql.append("         start with T.group_id IN\n");
			if (logonUser.getPoseType().intValue() == Constant.SYS_USER_DEALER.intValue())
			{//经销商只限定合同
				sql.append("(SELECT   B.GROUP_ID\n");
				sql.append("  FROM   TT_SALES_CONTRACT A, TT_SALES_CONTRACT_DTL B,tm_area_group c\n");
				sql.append(" WHERE       A.CONTRACT_ID = B.CONTRACT_ID AND B.GROUP_ID=C.MATERIAL_GROUP_ID AND C.AREA_ID IN(" + areaId + ")\n");
				sql.append("         AND A.DEALER_ID = " + logonUser.getDealerId() + "\n");
				sql.append("         AND A.CONTRACT_YEAR = TO_CHAR(SYSDATE,'YYYY'))\n");
			}
			else
			{
				sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
				sql.append("                       FROM tm_area_group tap\n");
				sql.append("                      where tap.area_id in (" + areaId + "))\n");
			}
			
			sql.append("        connect by prior T.parent_group_id = T.group_id) t2\n");
		}
		
		//sql.append(" WHERE T.COMPANY_ID = " + companyId + "\n");
		sql.append(" WHERE 1=1 \n");
		if (!"".equals(groupCode) && groupCode != null)
		{
			sql.append("AND  T.GROUP_CODE LIKE '%" + groupCode + "%'\n");
		}
		if (!"".equals(groupName) && groupName != null)
		{
			sql.append("   AND T.GROUP_NAME LIKE '%" + groupName + "%'\n");
		}
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append("   AND T.GROUP_LEVEL = " + groupLevel + "\n");
		}
		
		sql.append("   AND T.STATUS = " + Constant.STATUS_ENABLE + "\n");
		
		if (flag)
		{
			sql.append("AND T.GROUP_ID = t2.GROUP_ID\n");
		}
		
		if (!"".equals(groupId) && groupId != null)
		{
			if ("".equals(groupLevel) || groupLevel == null || "null".equals(groupLevel))
			{
				sql.append(" AND  T.PARENT_GROUP_ID = " + groupId + "\n");
			}
			else
			{
				sql.append(" START WITH T.GROUP_ID = " + groupId + "\n");
				sql.append("CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID\n");
			}
			
		}
		
		sql.append(" ORDER BY T.GROUP_LEVEL, T.GROUP_CODE\n");
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function : 查询物料组弹出树:过滤已有物料组
	 * 
	 * @param : 职位ID
	 */
	public static List<TmVhclMaterialGroupPO> getMaterialCarTypeGroupTree_Sel(Long poseId, String groupLevel,
					String area_id, Long companyId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("/*左侧树*/ \n");
		sql.append("SELECT T.GROUP_ID, \n");
		sql.append("       T.GROUP_CODE, \n");
		sql.append("       T.GROUP_NAME, \n");
		sql.append("       T.PARENT_GROUP_ID, \n");
		sql.append("       T.TREE_CODE \n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T\n");
		sql.append(" WHERE T.STATUS=" + Constant.STATUS_ENABLE + " \n");
		//sql.append(" AND   T.COMPANY_ID="+companyId+" \n"); 
		//if(!"".equals(groupLevel)&&groupLevel!=null&&!"null".equals(groupLevel))
		//{
		sql.append(" AND T.GROUP_LEVEL <= " + 3 + "\n");
		//}
		
		if (null != area_id && !"".equals(area_id))
		{
			sql.append("AND  EXISTS (SELECT TG.GROUP_ID \n");
			sql.append("          FROM TM_AREA_GROUP AG, TM_VHCL_MATERIAL_GROUP TG \n");
			sql.append("         WHERE AG.MATERIAL_GROUP_ID = TG.GROUP_ID \n");
			sql.append("           AND AG.AREA_ID IN( " + area_id + ",2011012613562666" + ") \n");
			sql.append("           AND TG.GROUP_ID = T.GROUP_ID) \n");
		}
		
		sql.append(" ORDER BY GROUP_ID \n");
		List<TmVhclMaterialGroupPO> list = factory.select(sql.toString(), null,
						new DAOCallback<TmVhclMaterialGroupPO>() {
							public TmVhclMaterialGroupPO wrapper(ResultSet rs, int idx)
							{
								TmVhclMaterialGroupPO bean = new TmVhclMaterialGroupPO();
								try
								{
									bean.setGroupId(Long.valueOf(rs.getString("GROUP_ID")));
									if (rs.getString("PARENT_GROUP_ID") != null)
									{
										bean.setParentGroupId(Long.valueOf(rs.getString("PARENT_GROUP_ID")));
									}
									else
									{
										bean.setParentGroupId(new Long(-1));
									}
									bean.setGroupName(rs.getString("GROUP_NAME"));
									bean.setGroupCode(rs.getString("GROUP_CODE"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * Function : 查询物料组弹出树:过滤已有物料组
	 * 
	 * @param : 职位ID
	 */
	public static List<TmVhclMaterialGroupPO> getMaterialGroupTree_Sel(Long poseId, String groupLevel, String area_id,
					Long companyId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("/*左侧树*/ \n");
		sql.append("SELECT T.GROUP_ID, \n");
		sql.append("       T.GROUP_CODE, \n");
		sql.append("       T.GROUP_NAME, \n");
		sql.append("       T.PARENT_GROUP_ID, \n");
		sql.append("       T.TREE_CODE, \n");
		sql.append("       T.GROUP_LEVEL \n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T\n");
		sql.append(" WHERE T.STATUS=" + Constant.STATUS_ENABLE + " \n");
		//sql.append(" AND   T.COMPANY_ID="+companyId+" \n"); 
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append(" AND T.GROUP_LEVEL <= " + groupLevel + "\n");
		}
		
		if (null != area_id && !"".equals(area_id))
		{
			sql.append("AND NOT EXISTS (SELECT TG.GROUP_ID \n");
			sql.append("          FROM TM_AREA_GROUP AG, TM_VHCL_MATERIAL_GROUP TG \n");
			sql.append("         WHERE AG.MATERIAL_GROUP_ID = TG.GROUP_ID \n");
			sql.append("           AND AG.AREA_ID = " + area_id + " \n");
			sql.append("           AND TG.GROUP_ID = T.GROUP_ID) \n");
		}
		
		sql.append(" ORDER BY GROUP_ID \n");
		List<TmVhclMaterialGroupPO> list = factory.select(sql.toString(), null,
						new DAOCallback<TmVhclMaterialGroupPO>() {
							public TmVhclMaterialGroupPO wrapper(ResultSet rs, int idx)
							{
								TmVhclMaterialGroupPO bean = new TmVhclMaterialGroupPO();
								try
								{
									bean.setGroupId(Long.valueOf(rs.getString("GROUP_ID")));
									if (rs.getString("PARENT_GROUP_ID") != null)
									{
										bean.setParentGroupId(Long.valueOf(rs.getString("PARENT_GROUP_ID")));
									}
									else
									{
										bean.setParentGroupId(new Long(-1));
									}
									bean.setGroupName(rs.getString("GROUP_NAME"));
									bean.setGroupCode(rs.getString("GROUP_CODE"));
									bean.setGroupLevel(rs.getInt("GROUP_LEVEL"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * Function : 查询配置
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 车系、车型、配置CODE
	 * @param : 车系、车型、配置名称
	 */
	public static PageResult<Map<String, Object>> getGroupList(String groupId, String groupCode, String groupName,
					String groupLevel, int curPage, int pageSize, Long companyId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");
		sql.append("       T.GROUP_NAME,\n");
		sql.append("       T.PARENT_GROUP_ID,\n");
		sql.append("       T.TREE_CODE\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T\n");
		//sql.append(" WHERE T.COMPANY_ID = "+companyId+"\n");  
		sql.append(" WHERE 1=1\n");
		if (!"".equals(groupCode) && groupCode != null)
		{
			sql.append("   AND T.GROUP_CODE LIKE '%" + groupCode + "%'\n");
		}
		if (!"".equals(groupName) && groupName != null)
		{
			sql.append("   AND T.GROUP_NAME LIKE '%" + groupName + "%'\n");
		}
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append("   AND T.GROUP_LEVEL = " + groupLevel + "\n");
		}
		sql.append("   AND T.STATUS = " + Constant.STATUS_ENABLE + "\n");
		if (!"".equals(groupId) && groupId != null)
		{
			if ("".equals(groupLevel) || groupLevel == null || "null".equals(groupLevel))
			{
				sql.append(" AND  T.PARENT_GROUP_ID = " + groupId + "\n");
			}
			else
			{
				sql.append(" START WITH T.GROUP_ID = " + groupId + "\n");
				sql.append("CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID\n");
			}
			
		}
		
		sql.append(" ORDER BY T.GROUP_ID\n");
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function : 查询配置
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 车系、车型、配置CODE
	 * @param : 车系、车型、配置名称
	 */
	public static PageResult<Map<String, Object>> getGroupListByPoseArea(String poseId, String groupId,
					String groupCode, String groupName, String groupLevel, int curPage, int pageSize, Long companyId,
					String isAllArea)
	{
		StringBuffer sql = new StringBuffer("\n");
		boolean flag = false;
		String areaIds = "";
		List par=new ArrayList();
		if (!isAllArea.toLowerCase().equals("true"))
		{
			
			areaIds = getAreaIdsByPoseId(new Long(poseId));
			
			if (areaIds != null && !areaIds.equals(""))
			{
				flag = true;
			}
		}
		
		sql.append("SELECT T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");
		sql.append("       T.GROUP_NAME,\n");
		sql.append("       T.PARENT_GROUP_ID,\n");
		sql.append("       T.TREE_CODE,\n");
		sql.append("       T.Group_Level\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T");
		
		if (flag)
		{
			sql.append(",\n");
			sql.append("(select T.group_id\n");
			sql.append("          from tm_vhcl_material_group t\n");
			sql.append("         start with T.group_id IN\n");
			sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                       FROM tm_area_group tap\n");
			sql.append("                   where 1=1\n");
			sql.append(Utility.getConSqlByParamForEqual(areaIds, par,"tap", "area_id"));
			sql.append("                    )\n");
			sql.append("        connect by prior T.group_id = T.parent_group_id\n");
			sql.append("        union\n");
			sql.append("        select T.group_id\n");
			sql.append("          from tm_vhcl_material_group t\n");
			sql.append("         start with T.group_id IN\n");
			sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                       FROM tm_area_group tap\n");
			sql.append("                   where 1=1\n");
			sql.append(Utility.getConSqlByParamForEqual(areaIds, par,"tap", "area_id"));
			sql.append("                    )\n");
			sql.append("        connect by prior T.parent_group_id = T.group_id) t2\n");
		}
		
		//sql.append(" WHERE T.COMPANY_ID = " + companyId + "\n");
		sql.append(" WHERE 1=1 \n");
		
		if (!"".equals(groupCode) && groupCode != null)
		{
			sql.append("   AND T.GROUP_CODE LIKE ?\n");
			par.add("%"+groupCode+"%");
		}
		if (!"".equals(groupName) && groupName != null)
		{
			sql.append("   AND T.GROUP_NAME LIKE ?\n");
			par.add("%"+groupName+"%");
		}
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append("   AND T.GROUP_LEVEL = ?\n");
			par.add(Integer.parseInt(groupLevel));
		}
		
		sql.append("   AND T.STATUS = " + Constant.STATUS_ENABLE + "\n");
		
		if (flag)
		{
			sql.append("AND T.GROUP_ID = t2.GROUP_ID\n");
		}
		
		if (!"".equals(groupId) && groupId != null)
		{
			if ("".equals(groupLevel) || groupLevel == null || "null".equals(groupLevel))
			{
				sql.append(" AND  T.PARENT_GROUP_ID = ?\n");
				par.add(Long.valueOf(groupId));
			}
			else
			{
				sql.append(" START WITH T.GROUP_ID = ?\n");
				sql.append("CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID\n");
				par.add(Long.valueOf(groupId));
			}
			
		}
		
		/* if(!isAllArea.toLowerCase().equals("true")){
		 * String areaIds = getAreaIdsByPoseId(new Long(poseId));
		 * if(areaIds!=null && !areaIds.equals("")){
		 * sql.append(" AND T.GROUP_ID IN\n");
		 * sql.append("    (select T.group_id\n");
		 * sql.append("       from tm_vhcl_material_group t\n");
		 * sql.append("      start with T.group_id IN\n");
		 * sql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
		 * sql.append("                    FROM tm_area_group tap\n");
		 * sql.append("                   where tap.area_id in ("+areaIds+"))\n");
		 * sql.append("     connect by prior T.group_id = T.parent_group_id\n");
		 * sql.append("     union\n");
		 * sql.append("     select T.group_id\n");
		 * sql.append("       from tm_vhcl_material_group t\n");
		 * sql.append("      start with T.group_id IN\n");
		 * sql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
		 * sql.append("                    FROM tm_area_group tap\n");
		 * sql.append("                   where tap.area_id in ("+areaIds+"))\n");
		 * sql.append("     connect by prior T.parent_group_id = T.group_id)");
		 * }
		 * } */
		
		sql.append(" ORDER BY T.GROUP_LEVEL, T.GROUP_CODE\n");
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), par, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function : 查询配置
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 车系、车型、配置CODE
	 * @param : 车系、车型、配置名称
	 */
	public static PageResult<Map<String, Object>> getGroupListByArea(String poseId, String groupId, String groupCode,
					String groupName, String groupLevel, int curPage, int pageSize, Long companyId, String areaId)
	{
		StringBuffer sql = new StringBuffer("\n");
		boolean flag = false;
		//String areaIds = "" ;
		
//		if(!isAllArea.toLowerCase().equals("true")){
//
//			areaIds = getAreaIdsByPoseId(new Long(poseId));
//	
//			if(areaIds != null && !areaIds.equals("")){
//				flag = true ;
//			}
//		}
		
		sql.append("SELECT T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");
		sql.append("       T.GROUP_NAME,\n");
		sql.append("       T.PARENT_GROUP_ID,\n");
		sql.append("       T.TREE_CODE,\n");
		sql.append("       T.Group_Level\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T");
		
		if (!"".equals(areaId))
		{
			sql.append(",\n");
			sql.append("(select T.group_id\n");
			sql.append("          from tm_vhcl_material_group t\n");
			sql.append("         start with T.group_id IN\n");
			sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                       FROM tm_area_group tap\n");
//			sql.append("                      where tap.area_id in (" + areaId + "))\n");  
			sql.append("   )     connect by prior T.group_id = T.parent_group_id\n");
			sql.append("        union\n");
			sql.append("        select T.group_id\n");
			sql.append("          from tm_vhcl_material_group t\n");
			sql.append("         start with T.group_id IN\n");
			sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                       FROM tm_area_group tap\n");
//			sql.append("                      where tap.area_id in (" + areaId + "))\n");  
			sql.append("  )      connect by prior T.parent_group_id = T.group_id) t2\n");
		}
		
		//sql.append(" WHERE T.COMPANY_ID = " + companyId + "\n");
		sql.append(" WHERE 1=1 \n");
		sql.append("AND T.GROUP_ID = t2.GROUP_ID\n");
		if (!"".equals(groupCode) && groupCode != null)
		{
			sql.append("   AND T.GROUP_CODE LIKE '%" + groupCode + "%'\n");
		}
		if (!"".equals(groupName) && groupName != null)
		{
			sql.append("   AND T.GROUP_NAME LIKE '%" + groupName + "%'\n");
		}
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append("   AND T.GROUP_LEVEL = " + groupLevel + "\n");
		}
		
		sql.append("   AND T.STATUS = " + Constant.STATUS_ENABLE + "\n");
		
		if (flag)
		{
			sql.append("AND T.GROUP_ID = t2.GROUP_ID\n");
		}
		
		if (!"".equals(groupId) && groupId != null)
		{
			if ("".equals(groupLevel) || groupLevel == null || "null".equals(groupLevel))
			{
				sql.append(" AND  T.PARENT_GROUP_ID = " + groupId + "\n");
			}
			else
			{
				sql.append(" START WITH T.GROUP_ID = " + groupId + "\n");
				sql.append("CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID\n");
			}
			
		}
		
		/* if(!isAllArea.toLowerCase().equals("true")){
		 * String areaIds = getAreaIdsByPoseId(new Long(poseId));
		 * if(areaIds!=null && !areaIds.equals("")){
		 * sql.append(" AND T.GROUP_ID IN\n");
		 * sql.append("    (select T.group_id\n");
		 * sql.append("       from tm_vhcl_material_group t\n");
		 * sql.append("      start with T.group_id IN\n");
		 * sql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
		 * sql.append("                    FROM tm_area_group tap\n");
		 * sql.append("                   where tap.area_id in ("+areaIds+"))\n");
		 * sql.append("     connect by prior T.group_id = T.parent_group_id\n");
		 * sql.append("     union\n");
		 * sql.append("     select T.group_id\n");
		 * sql.append("       from tm_vhcl_material_group t\n");
		 * sql.append("      start with T.group_id IN\n");
		 * sql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
		 * sql.append("                    FROM tm_area_group tap\n");
		 * sql.append("                   where tap.area_id in ("+areaIds+"))\n");
		 * sql.append("     connect by prior T.parent_group_id = T.group_id)");
		 * }
		 * } */
		
		sql.append(" ORDER BY T.GROUP_LEVEL, T.GROUP_CODE\n");
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function : 查询配置
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 车系、车型、配置CODE
	 * @param : 车系、车型、配置名称
	 */
	public static PageResult<Map<String, Object>> getGroupListByAreaPro(String poseId, String groupId,
					String groupCode,
					String groupName, String groupLevel, int curPage, int pageSize, Long companyId, String areaId)
	{
		StringBuffer sql = new StringBuffer("\n");
		boolean flag = false;
		//String areaIds = "" ;
		List par=new ArrayList();
		sql.append("SELECT T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");
		sql.append("       T.GROUP_NAME,\n");
		sql.append("       T.PARENT_GROUP_ID,\n");
		sql.append("       T.TREE_CODE,\n");
		sql.append("       T.Group_Level\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T");
		
		if (!"".equals(areaId))
		{
			sql.append(",\n");
			sql.append("(select T.group_id\n");
			sql.append("          from tm_vhcl_material_group t\n");
			sql.append("         start with T.group_id IN\n");
			sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                       FROM tm_area_group tap\n");
			sql.append("                      where 1=1\n");
			sql.append(Utility.getConSqlByParamForEqual(areaId, par,"tap", "area_id"));
			sql.append("                     )\n");
			sql.append("        connect by prior T.group_id = T.parent_group_id\n");
			sql.append("        union\n");
			sql.append("        select T.group_id\n");
			sql.append("          from tm_vhcl_material_group t\n");
			sql.append("         start with T.group_id IN\n");
			sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                       FROM tm_area_group tap\n");
			sql.append("                      where 1=1\n");
			sql.append(Utility.getConSqlByParamForEqual(areaId, par,"tap", "area_id"));
			sql.append("                     )\n");
			sql.append("       connect by prior T.parent_group_id = T.group_id) t2\n");
		}
		
		//sql.append(" WHERE T.COMPANY_ID = " + companyId + "\n");
		sql.append(" WHERE 1=1 \n");
		sql.append("AND T.GROUP_ID = t2.GROUP_ID\n");
		if (!"".equals(groupCode) && groupCode != null)
		{
			sql.append("   AND T.GROUP_CODE LIKE ?\n");
			par.add("%"+groupCode+"%");
		}
		if (!"".equals(groupName) && groupName != null)
		{
			sql.append("   AND T.GROUP_NAME LIKE ?\n");
			par.add("%"+groupName+"%");
		}
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append("   AND T.GROUP_LEVEL = ?\n");
			par.add(Integer.valueOf(groupLevel));
		}
		
		sql.append("   AND T.STATUS = " + Constant.STATUS_ENABLE + "\n");
		
		if (flag)
		{
			sql.append("AND T.GROUP_ID = t2.GROUP_ID\n");
		}
		
		if (!"".equals(groupId) && groupId != null)
		{
			if ("".equals(groupLevel) || groupLevel == null || "null".equals(groupLevel))
			{
				sql.append(" AND  T.PARENT_GROUP_ID = ?\n");
				par.add(Long.valueOf(groupId));
			}
			else
			{
				sql.append(" START WITH T.GROUP_ID = ?\n");
				par.add(Long.valueOf(groupId));
				sql.append("CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID\n");
			}
			
		}
		
		sql.append(" ORDER BY T.GROUP_LEVEL, T.GROUP_CODE\n");
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), par, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	//zhumingwei add 2011-6-22
	public static PageResult<Map<String, Object>> getGroupListByPoseArea1(String groupIdXi, String cxzid,
					String poseId, String groupId, String groupCode, String groupName, String groupLevel, int curPage,
					int pageSize, Long companyId, String isAllArea)
	{
		StringBuffer sql = new StringBuffer("\n");
		boolean flag = false;
		String areaIds = "";
		if (!isAllArea.toLowerCase().equals("true"))
		{
			areaIds = getAreaIdsByPoseId(new Long(poseId));
			if (areaIds != null && !areaIds.equals(""))
			{
				flag = true;
			}
		}
		sql.append("SELECT T.GROUP_ID,t8.group_Name as PARENT_CODE,wg.WRGROUP_NAME,\n");
		sql.append("       T.GROUP_CODE,\n");
		sql.append("       T.GROUP_NAME,\n");
		sql.append("       T.PARENT_GROUP_ID,\n");
		sql.append("       T.TREE_CODE,\n");
		sql.append("       T.Group_Level\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T,TM_VHCL_MATERIAL_GROUP t8,TT_AS_WR_MODEL_GROUP wg,TT_AS_WR_MODEL_ITEM wm");
		if (flag)
		{
			sql.append(",\n");
			sql.append("(select T.group_id\n");
			sql.append("          from tm_vhcl_material_group t\n");
			sql.append("         start with T.group_id IN\n");
			sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                       FROM tm_area_group tap\n");
			sql.append("                      where tap.area_id in (" + areaIds + "))\n");
			sql.append("        connect by prior T.group_id = T.parent_group_id\n");
			sql.append("        union\n");
			sql.append("        select T.group_id\n");
			sql.append("          from tm_vhcl_material_group t\n");
			sql.append("         start with T.group_id IN\n");
			sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                       FROM tm_area_group tap\n");
			sql.append("                      where tap.area_id in (" + areaIds + "))\n");
			sql.append("        connect by prior T.parent_group_id = T.group_id) t2\n");
		}
		sql.append(" WHERE wm.wrgroup_id = wg.wrgroup_id and t.group_id = wm.model_id and wg.wrgroup_type = '10451001' and t.parent_group_id = t8.group_id\n");
		//sql.append(" and T.COMPANY_ID = " + companyId + "\n");
		if (!"".equals(groupIdXi) && groupIdXi != null)
		{
			sql.append("   AND T8.GROUP_id ='" + groupIdXi + "'\n");
		}
		if (!"".equals(cxzid) && cxzid != null)
		{
			sql.append("   AND wg.wrgroup_name = '" + cxzid + "'\n");
		}
		if (!"".equals(groupCode) && groupCode != null)
		{
			sql.append("   AND T.GROUP_CODE LIKE '%" + groupCode + "%'\n");
		}
		if (!"".equals(groupName) && groupName != null)
		{
			sql.append("   AND T.GROUP_NAME LIKE '%" + groupName + "%'\n");
		}
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append("   AND T.GROUP_LEVEL = " + groupLevel + "\n");
		}
		sql.append("   AND T.STATUS = " + Constant.STATUS_ENABLE + "\n");
		if (flag)
		{
			sql.append("AND T.GROUP_ID = t2.GROUP_ID\n");
		}
		
		if (!"".equals(groupId) && groupId != null)
		{
			if ("".equals(groupLevel) || groupLevel == null || "null".equals(groupLevel))
			{
				sql.append(" AND  T.PARENT_GROUP_ID = " + groupId + "\n");
			}
			else
			{
				sql.append(" START WITH T.GROUP_ID = " + groupId + "\n");
				sql.append("CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID\n");
			}
		}
		sql.append(" ORDER BY T.GROUP_LEVEL, T.GROUP_CODE\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(), 1000, curPage);
		return rs;
	}
	
	/**
	 * Function : 查询配置:过滤已有物料组
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 车系、车型、配置CODE
	 * @param : 车系、车型、配置名称
	 */
	public static PageResult<Map<String, Object>> getGroupCarTypeList_Sel(String groupId, String groupCode,
					String groupName, String groupLevel, String area_id, int curPage, int pageSize, Long companyId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("--右侧展示\n");
		sql.append("SELECT T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");
		sql.append("       T.GROUP_NAME,\n");
		sql.append("       T.PARENT_GROUP_ID,\n");
		sql.append("       T.TREE_CODE\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T\n");
		//sql.append(" WHERE T.COMPANY_ID = "+companyId+"\n");  
		sql.append(" WHERE 1=1 \n");
		
//		if (null != treeCodeList && treeCodeList.size()>0) {
//			sql.append("  AND NOT EXISTS(SELECT TVG.TREE_CODE FROM TM_VHCL_MATERIAL_GROUP TVG WHERE 1=1  \n");
//			for (int i = 0; i < treeCodeList.size(); i++) {
//				sql.append("   OR  SUBSTR(TVG.TREE_CODE,0,"+((Map)treeCodeList.get(i)).get("CODE_LENGTH")+")='"+((Map)treeCodeList.get(i)).get("TREE_CODE")+"'\n");
//			}
//			sql.append("\n)");
//		}
		
		if (null != area_id && !"".equals(area_id))
		{
			sql.append(" AND EXISTS \n");
			sql.append("                (SELECT 1 \n");
			sql.append("                   FROM (    SELECT TG.GROUP_ID \n");
			sql.append("                               FROM TM_VHCL_MATERIAL_GROUP TG \n");
			sql.append("                         START WITH TG.GROUP_ID IN \n");
			sql.append("                                       (SELECT AG.MATERIAL_GROUP_ID \n");
			sql.append("                                          FROM TM_AREA_GROUP AG \n");
			sql.append("                                         WHERE 1=1  \n");
			if (area_id != null && !area_id.equals(" "))
			{
				sql.append(" AND AG.AREA_ID = " + area_id + "\n");
			}
			sql.append("                         )CONNECT BY PRIOR TG.GROUP_ID = TG.PARENT_GROUP_ID) DD \n");
			sql.append("                  WHERE DD.GROUP_ID = T.GROUP_ID) \n");
			
		}
		
		if (!"".equals(groupCode) && groupCode != null)
		{
			sql.append("   AND T.GROUP_CODE LIKE '%" + groupCode + "%'\n");
		}
		if (!"".equals(groupName) && groupName != null)
		{
			sql.append("   AND T.GROUP_NAME LIKE '%" + groupName + "%'\n");
		}
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append("   AND T.GROUP_LEVEL = " + groupLevel + "\n");
		}
		sql.append("   AND T.STATUS = " + Constant.STATUS_ENABLE + "\n");
		if (!"".equals(groupId) && groupId != null)
		{
			if ("".equals(groupLevel) || groupLevel == null || "null".equals(groupLevel))
			{
				sql.append(" AND  T.PARENT_GROUP_ID = " + groupId + "\n");
			}
			else
			{
				sql.append(" START WITH T.GROUP_ID = " + groupId + "\n");
				sql.append("CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID\n");
			}
			
		}
		sql.append(" ORDER BY T.GROUP_ID\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function : 查询配置:过滤已有物料组
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 车系、车型、配置CODE
	 * @param : 车系、车型、配置名称
	 */
	public static PageResult<Map<String, Object>> getGroupList_Sel(String groupId, String groupCode, String groupName,
					String groupLevel, String area_id, int curPage, int pageSize, Long companyId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("--右侧展示\n");
		sql.append("SELECT T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");
		sql.append("       T.GROUP_NAME,\n");
		sql.append("       T.PARENT_GROUP_ID,\n");
		sql.append("       T.TREE_CODE,\n");
		sql.append("       T.GROUP_LEVEL\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T\n");
		//sql.append(" WHERE T.COMPANY_ID = "+companyId+"\n");  
		sql.append(" WHERE 1=1\n");
		
//		if (null != treeCodeList && treeCodeList.size()>0) {
//			sql.append("  AND NOT EXISTS(SELECT TVG.TREE_CODE FROM TM_VHCL_MATERIAL_GROUP TVG WHERE 1=1  \n");
//			for (int i = 0; i < treeCodeList.size(); i++) {
//				sql.append("   OR  SUBSTR(TVG.TREE_CODE,0,"+((Map)treeCodeList.get(i)).get("CODE_LENGTH")+")='"+((Map)treeCodeList.get(i)).get("TREE_CODE")+"'\n");
//			}
//			sql.append("\n)");
//		}
		
		if (null != area_id && !"".equals(area_id))
		{
			sql.append(" AND NOT EXISTS \n");
			sql.append("                (SELECT 1 \n");
			sql.append("                   FROM (    SELECT TG.GROUP_ID \n");
			sql.append("                               FROM TM_VHCL_MATERIAL_GROUP TG \n");
			sql.append("                         START WITH TG.GROUP_ID IN \n");
			sql.append("                                       (SELECT AG.MATERIAL_GROUP_ID \n");
			sql.append("                                          FROM TM_AREA_GROUP AG \n");
			sql.append("                                         WHERE AG.AREA_ID = " + area_id + ") \n");
			sql.append("                         CONNECT BY PRIOR TG.GROUP_ID = TG.PARENT_GROUP_ID) DD \n");
			sql.append("                  WHERE DD.GROUP_ID = T.GROUP_ID) \n");
			
		}
		
		if (!"".equals(groupCode) && groupCode != null)
		{
			sql.append("   AND upper(T.GROUP_CODE) LIKE '%" + groupCode.toUpperCase() + "%'\n");
		}
		if (!"".equals(groupName) && groupName != null)
		{
			sql.append("   AND T.GROUP_NAME LIKE '%" + groupName + "%'\n");
		}
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append("   AND T.GROUP_LEVEL = " + groupLevel + "\n");
		}
		sql.append("   AND T.STATUS = " + Constant.STATUS_ENABLE + "\n");
		if (!"".equals(groupId) && groupId != null)
		{
			if ("".equals(groupLevel) || groupLevel == null || "null".equals(groupLevel))
			{
				sql.append(" AND  T.PARENT_GROUP_ID = " + groupId + "\n");
			}
			else
			{
				sql.append(" START WITH T.GROUP_ID = " + groupId + "\n");
				sql.append("CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID\n");
			}
			
		}
		sql.append(" ORDER BY T.GROUP_LEVEL\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	//zhumingwei 2011-10-24
	public static PageResult<Map<String, Object>> getGroupList_Sel111(String groupId, String groupCode,
					String groupName, String groupLevel, String area_id, int curPage, int pageSize, Long companyId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("--右侧展示\n");
		sql.append("SELECT T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");
		sql.append("       T.GROUP_NAME,\n");
		sql.append("       T.PARENT_GROUP_ID,\n");
		sql.append("       T.TREE_CODE,\n");
		sql.append("       T.GROUP_LEVEL\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T\n");
		//sql.append(" WHERE T.COMPANY_ID = "+companyId+"\n"); 
		sql.append(" WHERE 1=1\n");
		
//		if (null != treeCodeList && treeCodeList.size()>0) {
//			sql.append("  AND NOT EXISTS(SELECT TVG.TREE_CODE FROM TM_VHCL_MATERIAL_GROUP TVG WHERE 1=1  \n");
//			for (int i = 0; i < treeCodeList.size(); i++) {
//				sql.append("   OR  SUBSTR(TVG.TREE_CODE,0,"+((Map)treeCodeList.get(i)).get("CODE_LENGTH")+")='"+((Map)treeCodeList.get(i)).get("TREE_CODE")+"'\n");
//			}
//			sql.append("\n)");
//		}
		
		if (null != area_id && !"".equals(area_id))
		{
			sql.append(" AND NOT EXISTS \n");
			sql.append("                (SELECT 1 \n");
			sql.append("                   FROM (    SELECT TG.GROUP_ID \n");
			sql.append("                               FROM TM_VHCL_MATERIAL_GROUP TG \n");
			sql.append("                         START WITH TG.GROUP_ID IN \n");
			sql.append("                                       (SELECT AG.material_id \n");
			sql.append("                                          FROM tt_product_Material AG \n");
			sql.append("                                         WHERE AG.product_id = " + area_id + ") \n");
			sql.append("                         CONNECT BY PRIOR TG.GROUP_ID = TG.PARENT_GROUP_ID) DD \n");
			sql.append("                  WHERE DD.GROUP_ID = T.GROUP_ID) \n");
			
		}
		
		if (!"".equals(groupCode) && groupCode != null)
		{
			sql.append("   AND T.GROUP_CODE LIKE '%" + groupCode + "%'\n");
		}
		if (!"".equals(groupName) && groupName != null)
		{
			sql.append("   AND T.GROUP_NAME LIKE '%" + groupName + "%'\n");
		}
		if (!"".equals(groupLevel) && groupLevel != null && !"null".equals(groupLevel))
		{
			sql.append("   AND T.GROUP_LEVEL = " + groupLevel + "\n");
		}
		sql.append("   AND T.STATUS = " + Constant.STATUS_ENABLE + "\n");
		if (!"".equals(groupId) && groupId != null)
		{
			if ("".equals(groupLevel) || groupLevel == null || "null".equals(groupLevel))
			{
				sql.append(" AND  T.PARENT_GROUP_ID = " + groupId + "\n");
			}
			else
			{
				sql.append(" START WITH T.GROUP_ID = " + groupId + "\n");
				sql.append("CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID\n");
			}
			
		}
		sql.append(" ORDER BY T.GROUP_ID\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function : 查询物料
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 物料CODE
	 * @param : 物料名称
	 */
	public static PageResult<Map<String, Object>> getMaterialList(String groupId, String materialCode,
					String materialName, int curPage, int pageSize, Long companyId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVM.MATERIAL_CODE,TVM.MATERIAL_ID,TVM.MATERIAL_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL TVM\n");
		sql.append(" WHERE TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");
		//sql.append("  AND TVM.COMPANY_ID = "+companyId+"\n");
		if (!"".equals(materialCode) && materialCode != null)
		{
			sql.append("   AND TVM.MATERIAL_CODE LIKE '%" + materialCode + "%'\n");
		}
		if (!"".equals(materialName) && materialName != null)
		{
			sql.append("   AND TVM.MATERIAL_NAME LIKE '%" + materialName + "%'\n");
		}
		if (!"".equals(groupId) && groupId != null)
		{
			sql.append("   AND TVM.MATERIAL_ID IN\n");
			sql.append("       (SELECT TVMGR.MATERIAL_ID\n");
			sql.append("          FROM TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
			sql.append("         WHERE TVMGR.GROUP_ID IN\n");
			sql.append("               (SELECT TVMG.GROUP_ID\n");
			sql.append("                  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
			sql.append("                 START WITH TVMG.GROUP_ID = " + groupId + "\n");
			sql.append("                CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID\n");
			sql.append("                       AND TVMG.STATUS = " + Constant.STATUS_ENABLE + "))\n");
		}
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function : 查询物料
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 物料CODE
	 * @param : 物料名称
	 */
	public static PageResult<Map<String, Object>> QueryColorName(String group_id,String poseId, String groupId,
					String materialCode, String materialName, int curPage, int pageSize, Long companyId,
					String isAllArea){
	
		List<Object> par=new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct TVM.COLOR_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL TVM, TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
		sql.append(" WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVM.STATUS = 10011001\n");
		sql.append("   AND TVM.MATERIAL_ID IN\n");
		sql.append("       (SELECT TVMGR.MATERIAL_ID\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
		sql.append("         WHERE TVMGR.GROUP_ID IN\n");
		sql.append("               (SELECT TVMG.GROUP_ID\n");
		sql.append("                  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append("                 START WITH TVMG.GROUP_ID = ?\n");
		par.add(group_id.split("_")[0]);
		sql.append("                CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID\n");
		sql.append("                       AND TVMG.STATUS = 10011001))\n");
			
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), par, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	public static PageResult<Map<String, Object>> QueryPackageName(
			String group_id, String poseId, String groupId,
			String materialCode, String materialName, int curPage,
			int pageSize, Long companyId, String isAllArea) {

		List<Object> par = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct package_name, package_id from VW_MATERIAL_GROUP where MODEL_ID = ?\n");
		par.add(group_id.split("_")[0]);
		sql.append("union all select distinct '特殊                                                            ' as package_name , -1 as package_id from VW_MATERIAL_GROUP\n");
		sql.append("order by package_id\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), par,
				dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	public static PageResult<Map<String, Object>> getMaterialListByPoseArea(
			String poseId, String groupId, String materialCode,
			String materialName, int curPage, int pageSize, Long companyId,
			String isAllArea) {

		List par = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVM.MATERIAL_CODE,TVM.MATERIAL_ID,TVM.MATERIAL_NAME,TVM.COLOR_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL TVM, TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
		sql.append(" WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("  AND TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");

		// /** 物料显示的时候如果不是出口经销商则需要屏蔽出口的物料数据 add by wangsw */
		// if(poseType == Constant.SYS_USER_DEALER.intValue()) {
		// if(logonUser.getOrgId() == 2013082956190007L) {
		// sql.append(" AND TVM.EXPORT_SALES_FLAG = " + Constant.IF_TYPE_YES +
		// "\n");
		// }
		// else
		// {
		// sql.append(" AND TVM.EXPORT_SALES_FLAG <> " + Constant.IF_TYPE_YES +
		// "\n");
		// }
		// }

		// sql.append("  AND TVM.COMPANY_ID = "+companyId+"\n");
		if (!"".equals(materialCode) && materialCode != null) {
			sql.append("   AND TVM.MATERIAL_CODE LIKE ?\n");
			par.add("%" + materialCode + "%");
		}
		if (!"".equals(materialName) && materialName != null) {
			sql.append("   AND TVM.MATERIAL_NAME LIKE ?\n");
			par.add("%" + materialName + "%");
		}
		if (!"".equals(groupId) && groupId != null) {
			sql.append("   AND TVM.MATERIAL_ID IN\n");
			sql.append("       (SELECT TVMGR.MATERIAL_ID\n");
			sql.append("          FROM TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
			sql.append("         WHERE TVMGR.GROUP_ID IN\n");
			sql.append("               (SELECT TVMG.GROUP_ID\n");
			sql.append("                  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
			sql.append("                 START WITH TVMG.GROUP_ID = ?\n");
			sql.append("                CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID\n");
			sql.append("                       AND TVMG.STATUS = "
					+ Constant.STATUS_ENABLE + "))\n");
			par.add(Long.valueOf(groupId));
		}

		if (!isAllArea.toLowerCase().equals("true")) {
			String areaIds = getAreaIdsByPoseId(new Long(poseId));

			sql.append(" AND TVMGR.GROUP_ID IN\n");
			sql.append("    (select T.group_id\n");
			sql.append("       from tm_vhcl_material_group t\n");
			sql.append("      start with T.group_id IN\n");
			sql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                    FROM tm_area_group tap\n");
			sql.append("                   where 1=1\n");
			sql.append(Utility.getConSqlByParamForEqual(areaIds, par, "tap",
					"area_id"));
			sql.append("                 )\n");
			sql.append("     connect by prior T.group_id = T.parent_group_id)\n");
		}

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), par,
				dao.getFunName(), pageSize, curPage);
		return rs;
	}	
	/**
	 * 根据仓库查询物料数据
	 * @param groupId
	 * @param materialCode
	 * @param materialName
	 * @param curPage
	 * @param pageSize
	 * @param sendWare
	 * @param receiveWare
	 * @return
	 */
	public static PageResult<Map<String, Object>> getMaterialListByWarehouse(
			String groupId, String materialCode,
			String materialName, int curPage, int pageSize, String sendWare,
			String receiveWare) {

		List par = new ArrayList();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DISTINCT TVM.MATERIAL_ID,--物料ID\n" );
		sql.append("       TVM.MATERIAL_CODE,--物料编码\n" );
		sql.append("       TVM.MATERIAL_NAME,--物料名称\n" );
		sql.append("       TVM.COLOR_CODE,--颜色代码\n" );
		sql.append("       TVM.COLOR_NAME,--颜色名称\n" );
		sql.append("       VGM.SERIES_ID,--车系ID\n" );
		sql.append("       VGM.SERIES_CODE,--车系代码\n" );
		sql.append("       VGM.SERIES_NAME,--车系名称\n" );
		sql.append("       VGM.MODEL_ID,--车型ID\n" );
		sql.append("       VGM.MODEL_CODE,--车型代码\n" );
		sql.append("       VGM.MODEL_NAME,--车型名称\n" );
		sql.append("       VGM.PACKAGE_ID,--配置ID\n" );
		sql.append("       VGM.PACKAGE_CODE,--配置代码\n" );
		sql.append("       VGM.PACKAGE_NAME--配置名称\n" );
		sql.append("  FROM TM_VHCL_MATERIAL TVM,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT VGM\n" );
		sql.append(" WHERE TVM.MATERIAL_ID=VGM.MATERIAL_ID\n" );
		sql.append("  AND TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");
		if (!"".equals(materialCode) && materialCode != null) {
			sql.append("   AND TVM.MATERIAL_CODE LIKE ?\n");
			par.add("%" + materialCode + "%");
		}
		if (!"".equals(materialName) && materialName != null) {
			sql.append("   AND TVM.MATERIAL_NAME LIKE ?\n");
			par.add("%" + materialName + "%");
		}
		if (!"".equals(groupId) && groupId != null) {
			sql.append("   AND VGM.PACKAGE_ID IN\n");
			sql.append("               (SELECT TVMG.GROUP_ID\n");
			sql.append("                  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
			sql.append("                 START WITH TVMG.GROUP_ID = ?\n");
			sql.append("                CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID\n");
			sql.append("                       AND TVMG.STATUS = "+ Constant.STATUS_ENABLE + ")\n");
			par.add(Long.valueOf(groupId));
		}

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), par, dao.getFunName(), pageSize, curPage);
		return rs;
	}	
	/**
	 * Function : 查询物料组弹出树
	 * 
	 * @param : 职位ID
	 */
	public static List<TmVhclMaterialGroupPO> getMaterialGroupTreeByAreaId(String areaId, Long companyId,
					String groupLevel, AclUserBean logonUser)
	{
		StringBuffer sql = new StringBuffer();
		int poseType = logonUser.getPoseType().intValue();
		List par=new ArrayList();
		sql.append("select T1.GROUP_ID, T1.GROUP_CODE, T1.GROUP_NAME, T1.PARENT_GROUP_ID, T1.GROUP_LEVEL\n");
		sql.append("  from tm_vhcl_material_group t1\n");
		sql.append(" WHERE T1.STATUS = " + Constant.STATUS_ENABLE + "AND T1.FORCAST_FLAG=1\n");
		//sql.append("   AND T1.COMPANY_ID = "+companyId+"\n"); 
		if (groupLevel != null && !groupLevel.equals(""))
		{
			sql.append("   AND T1.GROUP_LEVEL <= ?\n");
			par.add(Integer.valueOf(groupLevel));
		}
		sql.append(" start with t1.group_id IN\n");
		
		if (poseType == Constant.SYS_USER_DEALER.intValue())
		{//经销商只限定合同
			sql.append("(SELECT   B.GROUP_ID\n");
			sql.append("  FROM   TT_SALES_CONTRACT A, TT_SALES_CONTRACT_DTL B,tm_area_group c\n");
			sql.append(" WHERE       A.CONTRACT_ID = B.CONTRACT_ID AND B.GROUP_ID=C.MATERIAL_GROUP_ID\n");
			sql.append(Utility.getConSqlByParamForEqual(areaId, par,"c", "AREA_ID"));
			sql.append("         AND A.DEALER_ID = ?\n");
			sql.append("         AND A.CONTRACT_YEAR = TO_CHAR(SYSDATE,'YYYY'))\n");
			par.add(Long.valueOf(logonUser.getDealerId()));
		}
		else
		{
			sql.append("            (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("               FROM tm_area_group tap\n");
			sql.append("              where tap.area_id = ?)\n");
			par.add(Long.valueOf(areaId));
		}
		
		sql.append("connect by prior t1.group_id = t1.parent_group_id\n");
		sql.append("UNION\n");
		sql.append("select T1.GROUP_ID, T1.GROUP_CODE, T1.GROUP_NAME, T1.PARENT_GROUP_ID, T1.GROUP_LEVEL\n");
		sql.append("  from tm_vhcl_material_group t1\n");
		sql.append(" WHERE T1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		//sql.append("   AND T1.COMPANY_ID = "+companyId+"\n");
		if (groupLevel != null && !groupLevel.equals(""))
		{
			sql.append("   AND T1.GROUP_LEVEL <= ?\n");
			par.add(Integer.valueOf(groupLevel));
		}
		sql.append(" start with t1.group_id IN\n");
		if (poseType == Constant.SYS_USER_DEALER.intValue())
		{//经销商只限定合同
//			sql.append("(SELECT   B.GROUP_ID\n");
//			sql.append("  FROM   TT_SALES_CONTRACT A, TT_SALES_CONTRACT_DTL B, tm_area_group C\n");
//			sql.append(" WHERE       A.CONTRACT_ID = B.CONTRACT_ID AND A.STATUS=" + Constant.STATUS_ENABLE + "\n");
//			sql.append("         AND A.DEALER_ID = " + logonUser.getDealerId() + "\n");
//			sql.append("         AND A.CONTRACT_YEAR = TO_CHAR(SYSDATE,'YYYY'))\n");
//			sql.append("		 AND AND C.AREA_ID IN(" + areaId + ")");
			sql.append("(SELECT   B.GROUP_ID\n");
			sql.append("  FROM   TT_SALES_CONTRACT A, TT_SALES_CONTRACT_DTL B,tm_area_group c\n");
			sql.append(" WHERE       A.CONTRACT_ID = B.CONTRACT_ID AND B.GROUP_ID=C.MATERIAL_GROUP_ID\n");
			sql.append(Utility.getConSqlByParamForEqual(areaId, par,"c", "AREA_ID"));
			sql.append("         AND A.DEALER_ID = ?\n");
			sql.append("         AND A.CONTRACT_YEAR = TO_CHAR(SYSDATE,'YYYY'))\n"); 
			par.add(Long.valueOf(logonUser.getDealerId()));
		}
		else
		{
			sql.append("            (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("               FROM tm_area_group tap\n");
			sql.append("              where tap.area_id = ?)\n");
			par.add(Long.valueOf(areaId));
		}
		sql.append("connect by prior t1.parent_group_id = t1.group_id");
		
		List<TmVhclMaterialGroupPO> list = factory.select(sql.toString(), par,
						new DAOCallback<TmVhclMaterialGroupPO>() {
							public TmVhclMaterialGroupPO wrapper(ResultSet rs, int idx)
							{
								TmVhclMaterialGroupPO bean = new TmVhclMaterialGroupPO();
								try
								{
									//System.out.println("错错："+rs.getString("GROUP_ID"));
									bean.setGroupId(Long.valueOf(rs.getString("GROUP_ID")));
									bean.setParentGroupId(Long.valueOf(rs.getString("PARENT_GROUP_ID")));
									bean.setGroupName(rs.getString("GROUP_NAME"));
									bean.setGroupCode(rs.getString("GROUP_CODE"));
									bean.setGroupLevel(rs.getInt("GROUP_LEVEL"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * Function : 查询物料组弹出树--微车
	 * 
	 * @param : 职位ID
	 */
	public static List<TmVhclMaterialGroupPO> getMaterialGroupTreeByAreaId_Mini(String areaId, Long companyId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT T1.GROUP_ID,\n");
		sql.append("                T1.GROUP_CODE,\n");
		sql.append("                T1.GROUP_NAME,\n");
		sql.append("                T1.PARENT_GROUP_ID\n");
		sql.append("  FROM (SELECT *\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP T2\n");
		sql.append("         START WITH T2.GROUP_ID IN\n");
		sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
		sql.append("                       FROM TM_AREA_GROUP TAP\n");
		sql.append("                      WHERE TAP.AREA_ID = " + areaId + ")\n");
		sql.append("        CONNECT BY PRIOR T2.GROUP_ID = T2.PARENT_GROUP_ID) T1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");
		sql.append("       TM_VHCL_MATERIAL TVM\n");
		sql.append(" WHERE T1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		//sql.append("   AND T1.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND T1.GROUP_ID = R.GROUP_ID(+)\n");
		sql.append("   AND TVM.NORMAL_ORDER_FLAG(+) = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND R.MATERIAL_ID = TVM.MATERIAL_ID(+)\n");
		sql.append("UNION\n");
		sql.append("SELECT DISTINCT T1.GROUP_ID,\n");
		sql.append("                T1.GROUP_CODE,\n");
		sql.append("                T1.GROUP_NAME,\n");
		sql.append("                T1.PARENT_GROUP_ID\n");
		sql.append("  FROM (SELECT *\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP T2\n");
		sql.append("         START WITH T2.GROUP_ID IN\n");
		sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
		sql.append("                       FROM TM_AREA_GROUP TAP\n");
		sql.append("                      WHERE TAP.AREA_ID = " + areaId + ")\n");
		sql.append("        CONNECT BY PRIOR T2.PARENT_GROUP_ID = T2.GROUP_ID) T1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");
		sql.append("       TM_VHCL_MATERIAL TVM\n");
		sql.append(" WHERE T1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		//sql.append("   AND T1.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND T1.GROUP_ID = R.GROUP_ID(+)\n");
		sql.append("   AND TVM.NORMAL_ORDER_FLAG(+) = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND R.MATERIAL_ID = TVM.MATERIAL_ID(+)\n");
		
		List<TmVhclMaterialGroupPO> list = factory.select(sql.toString(), null,
						new DAOCallback<TmVhclMaterialGroupPO>() {
							public TmVhclMaterialGroupPO wrapper(ResultSet rs, int idx)
							{
								TmVhclMaterialGroupPO bean = new TmVhclMaterialGroupPO();
								try
								{
									bean.setGroupId(Long.valueOf(rs.getString("GROUP_ID")));
									bean.setParentGroupId(Long.valueOf(rs.getString("PARENT_GROUP_ID")));
									bean.setGroupName(rs.getString("GROUP_NAME"));
									bean.setGroupCode(rs.getString("GROUP_CODE"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * Function : 查询物料
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 物料CODE
	 * @param : 物料名称
	 */
	public static PageResult<Map<String, Object>> getMaterialListByAreaId(String productId, String areaId, String ids,
					String groupId, String materialCode, String materialName, int curPage, int pageSize,
					Long companyId, AclUserBean logonUser,String matType)
	{
		StringBuffer sql = new StringBuffer();
		int poseType = logonUser.getPoseType().intValue();
		sql.append("SELECT TVM.MATERIAL_CODE,TVM.MATERIAL_ID,TVM.MATERIAL_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL TVM, TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
		sql.append(" WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVM.RUSH_ORDER_FLAG = " + Constant.NASTY_ORDER_REPORT_TYPE_01 + "\n");
		sql.append("   AND TVM.ORDER_FLAG = " + Constant.NASTY_ORDER_REPORT_TYPE_01 + "\n");
		sql.append("   AND TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");
		//sql.append("   AND TVM.COMPANY_ID = "+companyId+"\n");
		sql.append("   AND TVMGR.GROUP_ID IN\n");
		sql.append("       (select T1.GROUP_ID\n");
		sql.append("          from tm_vhcl_material_group t1\n");
		sql.append("         WHERE T1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("         start with t1.group_id IN\n");
		if (poseType == Constant.SYS_USER_DEALER.intValue())
		{//经销商只限定合同
			sql.append("(SELECT   B.GROUP_ID\n");
			sql.append("  FROM   TT_SALES_CONTRACT A, TT_SALES_CONTRACT_DTL B,tm_area_group c\n");
			sql.append(" WHERE       A.CONTRACT_ID = B.CONTRACT_ID AND A.STATUS=" + Constant.STATUS_ENABLE + "\n");
			sql.append("         AND A.DEALER_ID = " + logonUser.getDealerId() + "\n");
			sql.append("         AND B.GROUP_ID=C.MATERIAL_GROUP_ID AND C.AREA_ID IN(" + areaId + ")\n");
			sql.append("         AND A.CONTRACT_YEAR = TO_CHAR(SYSDATE,'YYYY'))\n");
		}
		else
		{
			sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                       FROM tm_area_group tap\n");
			sql.append("                      where tap.area_id = " + areaId + ")\n");
		}
		sql.append("        connect by prior t1.group_id = t1.parent_group_id)");
		
		if (!"".equals(materialCode) && materialCode != null)
		{
			sql.append("   AND TVM.MATERIAL_CODE LIKE '%" + materialCode + "%'\n");
		}
		if (!"".equals(materialName) && materialName != null)
		{
			sql.append("   AND TVM.MATERIAL_NAME LIKE '%" + materialName + "%'\n");
		}
		if (ids != null && !"".equals(ids))
		{
			sql.append("   AND TVM.MATERIAL_ID NOT IN (" + ids + ")");
		}
		if (matType != null &&!"".equals(matType))
		{
			sql.append("   AND TVM.MAT_TYPE=" + matType + "\n");
		}
		if (!CommonUtils.isNullString(productId) && !"-1".equals(productId) && !"null".equals(productId) && !"undefined"
						.equals(productId))
		{
			sql.append("   AND TVMGR.GROUP_ID IN\n");
			sql.append("       (select T1.GROUP_ID\n");
			sql.append("          from tm_vhcl_material_group t1\n");
			sql.append("         WHERE T1.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("         start with t1.group_id IN\n");
			sql.append("                    (SELECT tpm.material_id\n");
			sql.append("                       FROM tt_product_material tpm\n");
			sql.append("                      where tpm.product_id = " + productId + ")\n");
			sql.append("        connect by prior t1.group_id = t1.parent_group_id)");
		}
		
		if (!"".equals(groupId) && groupId != null)
		{
			sql.append(" AND TVMGR.GROUP_ID IN\n");
			sql.append("       (SELECT T.GROUP_ID\n");
			sql.append("          FROM TM_VHCL_MATERIAL_GROUP T\n");
			sql.append("         WHERE T.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("         START WITH T.GROUP_ID = " + groupId + "\n");
			sql.append("        CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID)");
		}
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	/**
	 * Function : 查询物料（查询可以用于生产的物料）
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 物料CODE
	 * @param : 物料名称
	 * 
	 * @author wangsw
	 */
	public static PageResult<Map<String, Object>> getMaterialListByAreaIdForProduct(Map<String, Object> paramMap,
					 int curPage, int pageSize, AclUserBean logonUser)
	{
		String groupId = CommonUtils.checkNull(paramMap.get("groupId"));
		String areaId = CommonUtils.checkNull(paramMap.get("areaId"));
//		String matType = CommonUtils.checkNull(paramMap.get("matType"));
		String materialCode = CommonUtils.checkNull(paramMap.get("materialCode"));
		String materialName = CommonUtils.checkNull(paramMap.get("materialName"));
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT TVM.MATERIAL_CODE,TVM.MATERIAL_ID,TVM.MATERIAL_NAME,TVM.ORDER_FLAG\n");
		sbSql.append("  FROM TM_VHCL_MATERIAL TVM, TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
		sbSql.append(" WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sbSql.append("   AND TVM.PROCUCT_FLAG = 96891001\n");
		sbSql.append("   AND TVM.STATUS = 10011001\n");
		
		if(!areaId.equals("")) {
			sbSql.append("   AND TVMGR.GROUP_ID IN\n");
			sbSql.append("       (select T1.GROUP_ID\n");
			sbSql.append("          from tm_vhcl_material_group t1\n");
			sbSql.append("         WHERE T1.STATUS = 10011001\n");
			sbSql.append("         start with t1.group_id in\n");
			sbSql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
			sbSql.append("                       FROM tm_area_group tap\n");
			sbSql.append("                      where tap.area_id = ?)\n");
			sbSql.append("        connect by prior t1.group_id = t1.parent_group_id)");
			
			params.add(areaId); // 物料的产地限制
		}
		
		if (!"".equals(materialCode))
		{
			sbSql.append("   AND TVM.MATERIAL_CODE LIKE ?\n");
			params.add("%" + materialCode + "%");
		}
		
		if (!"".equals(materialName))
		{
			sbSql.append("   AND TVM.MATERIAL_NAME LIKE ?\n");
			params.add("%" + materialName + "%");
		}
		
		if (!"".equals(groupId))
		{
			sbSql.append(" AND TVMGR.GROUP_ID IN\n");
			sbSql.append("       (SELECT T.GROUP_ID\n");
			sbSql.append("          FROM TM_VHCL_MATERIAL_GROUP T\n");
			sbSql.append("         WHERE T.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sbSql.append("         START WITH T.GROUP_ID = ?\n");
			sbSql.append("        CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID)");
			params.add(groupId);
		}
		
		return dao.pageQuery(sbSql.toString(), params, dao.getFunName(), pageSize, curPage);
	}
	
	/**
	 * Function : 查询物料(带价格)
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 物料CODE
	 * @param : 物料名称
	 */
	public static PageResult<Map<String, Object>> getMaterialListWithPriceByAreaId(String productId, String areaId,
					String dealerId,
					String ids,
					String groupId, String materialCode, String materialName, int curPage, int pageSize,
					Long companyId, AclUserBean logonUser,String resAmount)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		int poseType = logonUser.getPoseType().intValue();
		
		sql.append("WITH " + AsSqlUtils.getAsSqlVehicleResourceBuffer() );
		sql.append("SELECT TVM.MATERIAL_CODE,TVM.MATERIAL_ID,TVM.MATERIAL_NAME,TVM.ERP_PACKAGE,TVM.ERP_NAME,TVM.REMARK2,NVL(TVM.VHCL_PRICE,0) + \n");
		sql.append("       NVL((\n");
		sql.append("             SELECT   TSAP.AMOUNT\n");
		sql.append("             FROM\n");
		sql.append("                      TM_REGION                     TR,\n");
		sql.append("                      TT_SALES_AREA_PRICE           TSAP,\n");
		sql.append("                      TM_DEALER                     TD\n");
		sql.append("             WHERE\n");
		sql.append("                      TR.REGION_CODE = TSAP.AREA_ID(+)\n");
		sql.append("                      AND TR.REGION_TYPE = " + Constant.REGION_TYPE_02 + "\n");
		sql.append("                      AND TSAP.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("                      AND TD.PROVINCE_ID = TR.REGION_CODE\n");
		sql.append("                      AND TD.DEALER_ID = ?\n");
		sql.append("                      AND TSAP.YIELDLY = ?\n");
		params.add(Long.valueOf(dealerId));
		params.add(Long.valueOf(areaId));
		sql.append(" 	   ),0)  PRICE,\n");
		sql.append("       CASE WHEN NVL(VS.VEHICLE_AMOUNT,0) > 0 THEN '有' ELSE '无' END VEHICLE_AMOUNT");
		sql.append("  FROM TM_VHCL_MATERIAL TVM, TM_VHCL_MATERIAL_GROUP_R TVMGR, VEHICLE_STOCK VS\n");
		sql.append(" WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = VS.MATERIAL_ID(+)");
		sql.append("   AND TVM.RUSH_ORDER_FLAG = " + Constant.NASTY_ORDER_REPORT_TYPE_01 + "\n");
		sql.append("   AND TVM.ORDER_FLAG = " + Constant.NASTY_ORDER_REPORT_TYPE_01 + "\n");
		sql.append("   AND TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TVMGR.GROUP_ID IN\n");
		sql.append("       (select T1.GROUP_ID\n");
		sql.append("          from tm_vhcl_material_group t1\n");
		sql.append("         WHERE T1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("         start with t1.group_id in\n");
		
		if (poseType == Constant.SYS_USER_DEALER.intValue())
		{//经销商只限定合同
			sql.append("(SELECT   B.GROUP_ID\n");
			sql.append("  FROM   TT_SALES_CONTRACT A, TT_SALES_CONTRACT_DTL B,tm_area_group c\n");
			sql.append(" WHERE       A.CONTRACT_ID = B.CONTRACT_ID AND A.STATUS=" + Constant.STATUS_ENABLE + "\n");
			sql.append("         AND A.DEALER_ID = ?\n");
			sql.append("         AND B.GROUP_ID=C.MATERIAL_GROUP_ID AND C.AREA_ID IN(?)\n");
			sql.append("         AND A.CONTRACT_YEAR = TO_CHAR(SYSDATE,'YYYY'))\n");
			params.add(Long.valueOf(dealerId));
			params.add(Long.valueOf(areaId));
		}
		else
		{
			sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                       FROM tm_area_group tap\n");
			sql.append("                      where tap.area_id = ?)\n");
			params.add(Long.valueOf(areaId));
		}
		sql.append("        connect by prior t1.group_id = t1.parent_group_id)");
		
		if (!"".equals(materialCode) && materialCode != null)
		{
			sql.append("   AND TVM.MATERIAL_CODE LIKE ?\n");
			params.add("%"+materialCode+"%");
		}
		if (!"".equals(materialName) && materialName != null)
		{
			sql.append("   AND TVM.MATERIAL_NAME LIKE ?\n");
			params.add("%"+materialName+"%");
		}
		if (ids != null && !"".equals(ids))
		{
			sql.append("   AND TVM.MATERIAL_ID NOT IN (" + ids + ")\n");
		}
		
		if (!"".equals(groupId) && groupId != null)
		{
			sql.append(" AND TVMGR.GROUP_ID IN\n");
			sql.append("       (SELECT T.GROUP_ID\n");
			sql.append("          FROM TM_VHCL_MATERIAL_GROUP T\n");
			sql.append("         WHERE T.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("         START WITH T.GROUP_ID = ?\n");
			sql.append("        CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID)");
			params.add(groupId);
		}
		
		if(!"".endsWith(resAmount) && !"-1".equals(resAmount)) {
			if(resAmount.equals("1")) { 
				sql.append(" AND NVL(VS.VEHICLE_AMOUNT,0) > 0");
			} else if(resAmount.equals("0")) {
				sql.append(" AND NVL(VS.VEHICLE_AMOUNT,0) <= 0");
			}
		}
		
		return dao.pageQuery(sql.toString(), params, dao.getFunName(), pageSize, curPage);
	}
	
	/**
	 * Function : 查询物料
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 物料CODE
	 * @param : 物料名称
	 */
	public static PageResult<Map<String, Object>> getMaterialListByAreaIdAndGroupId(String productId, String areaId,
					String ids, String groupId, String materialCode, String materialName, int curPage, int pageSize,
					Long companyId)
	{
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT TVM.MATERIAL_ID, TVM.MATERIAL_CODE, TVM.MATERIAL_NAME, TVM.MODEL_CODE FROM\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL TVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
		sql.append("       WHERE TVMGR.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("       AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		if (ids != null && !"".equals(ids))
		{
			sql.append("       AND TVMG.GROUP_ID in ( ");
			sql.append(ids);
			sql.append(" )\n");
		}
		if (!"".equals(materialCode) && materialCode != null)
		{
			sql.append("   AND TVM.MATERIAL_CODE LIKE '%" + materialCode + "%'\n");
		}
		if (!"".equals(materialName) && materialName != null)
		{
			sql.append("   AND TVM.MATERIAL_NAME LIKE '%" + materialName + "%'\n");
		}
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	public static PageResult<Map<String, Object>> getMaterialListByAreaIdAndOrderType(String productId, String areaId,
					String ids, String groupId, String materialCode, String materialName, String orderType,
					int curPage, int pageSize, Long companyId)
	{
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT TVM.MATERIAL_CODE,TVM.MATERIAL_ID,TVM.MATERIAL_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL TVM, TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
		sql.append(" WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		if (orderType != null && !orderType.equals(""))
		{
			if (Integer.parseInt(orderType) == Constant.ORDER_TYPE_01.intValue())
			{
				sql.append("   AND TVM.NORMAL_ORDER_FLAG = " + Constant.IF_TYPE_YES + "\n");
			}
			else if (Integer.parseInt(orderType) == Constant.ORDER_TYPE_02.intValue())
			{
				sql.append("   AND TVM.RUSH_ORDER_FLAG = " + Constant.NASTY_ORDER_REPORT_TYPE_01 + "\n");
			}
//			else if (Integer.parseInt(orderType) == Constant.ORDER_TYPE_03.intValue())
//			{
//				sql.append("   AND TVM.SPECIAL_ORDER_FLAG = " + Constant.IF_TYPE_YES + "\n");
//			}
		}
		sql.append("   AND TVM.ORDER_FLAG = " + Constant.NASTY_ORDER_REPORT_TYPE_01 + "\n");
		sql.append("   AND TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");
		//sql.append("   AND TVM.COMPANY_ID = "+companyId+"\n");
		sql.append("   AND TVMGR.GROUP_ID IN\n");
		sql.append("       (select T1.GROUP_ID\n");
		sql.append("          from tm_vhcl_material_group t1\n");
		sql.append("         WHERE T1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("         start with t1.group_id IN\n");
		sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
		sql.append("                       FROM tm_area_group tap\n");
		sql.append("                      where tap.area_id = " + areaId + ")\n");
		sql.append("        connect by prior t1.group_id = t1.parent_group_id)");
		
		if (!"".equals(materialCode) && materialCode != null)
		{
			sql.append("   AND TVM.MATERIAL_CODE LIKE '%" + materialCode + "%'\n");
		}
		if (!"".equals(materialName) && materialName != null)
		{
			sql.append("   AND TVM.MATERIAL_NAME LIKE '%" + materialName + "%'\n");
		}
		if (ids != null && !"".equals(ids))
		{
			sql.append("   AND TVM.MATERIAL_ID NOT IN (" + ids + ")");
		}
		
		if (!CommonUtils.isNullString(productId) && !"-1".equals(productId) && !"null".equals(productId) && !"undefined"
						.equals(productId))
		{
			sql.append("   AND TVMGR.GROUP_ID IN\n");
			sql.append("       (select T1.GROUP_ID\n");
			sql.append("          from tm_vhcl_material_group t1\n");
			sql.append("         WHERE T1.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("         start with t1.group_id IN\n");
			sql.append("                    (SELECT tpm.material_id\n");
			sql.append("                       FROM tt_product_material tpm\n");
			sql.append("                      where tpm.product_id = " + productId + ")\n");
			sql.append("        connect by prior t1.group_id = t1.parent_group_id)");
		}
		
		if (!"".equals(groupId) && groupId != null)
		{
			sql.append(" AND TVMGR.GROUP_ID IN\n");
			sql.append("       (SELECT T.GROUP_ID\n");
			sql.append("          FROM TM_VHCL_MATERIAL_GROUP T\n");
			sql.append("         WHERE T.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("         START WITH T.GROUP_ID = " + groupId + "\n");
			sql.append("        CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID)");
		}
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function : 查询物料
	 * 
	 * @param : 车系、车型、配置ID
	 * @param : 物料CODE
	 * @param : 物料名称
	 *        微车
	 */
	public static PageResult<Map<String, Object>> getMaterialListByAreaId_Mini(String productId, String areaId,
					String ids, String groupId, String materialCode, String materialName, int curPage, int pageSize,
					Long companyId)
	{
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT TVM.MATERIAL_CODE,TVM.MATERIAL_ID,TVM.MATERIAL_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL TVM, TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
		sql.append(" WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		// sql.append("   AND TVM.RUSH_ORDER_FLAG = "+Constant.NASTY_ORDER_REPORT_TYPE_01+"\n");
		sql.append("   AND TVM.ORDER_FLAG = " + Constant.NASTY_ORDER_REPORT_TYPE_01 + "\n");
		sql.append("   AND TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");
		//sql.append("   AND TVM.COMPANY_ID = "+companyId+"\n");
		sql.append("   AND TVMGR.GROUP_ID IN\n");
		sql.append("       (select T1.GROUP_ID\n");
		sql.append("          from tm_vhcl_material_group t1\n");
		sql.append("         WHERE T1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("         start with t1.group_id IN\n");
		sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");
		sql.append("                       FROM tm_area_group tap\n");
		sql.append("                      where tap.area_id = " + areaId + ")\n");
		sql.append("        connect by prior t1.group_id = t1.parent_group_id)");
		
		if (!"".equals(materialCode) && materialCode != null)
		{
			sql.append("   AND TVM.MATERIAL_CODE LIKE '%" + materialCode + "%'\n");
		}
		if (!"".equals(materialName) && materialName != null)
		{
			sql.append("   AND TVM.MATERIAL_NAME LIKE '%" + materialName + "%'\n");
		}
		if (ids != null && !"".equals(ids))
		{
			sql.append("   AND TVM.MATERIAL_ID NOT IN (" + ids + ")");
		}
		
		if (!CommonUtils.isNullString(productId) && !"-1".equals(productId) && !"null".equals(productId) && !"undefined"
						.equals(productId))
		{
			sql.append("   AND TVMGR.GROUP_ID IN\n");
			sql.append("       (select T1.GROUP_ID\n");
			sql.append("          from tm_vhcl_material_group t1\n");
			sql.append("         WHERE T1.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("         start with t1.group_id IN\n");
			sql.append("                    (SELECT tpm.material_id\n");
			sql.append("                       FROM tt_product_material tpm\n");
			sql.append("                      where tpm.product_id = " + productId + ")\n");
			sql.append("        connect by prior t1.group_id = t1.parent_group_id)");
		}
		
		if (!"".equals(groupId) && groupId != null)
		{
			sql.append(" AND TVMGR.GROUP_ID IN\n");
			sql.append("       (SELECT T.GROUP_ID\n");
			sql.append("          FROM TM_VHCL_MATERIAL_GROUP T\n");
			sql.append("         WHERE T.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("         START WITH T.GROUP_ID = " + groupId + "\n");
			sql.append("        CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID)");
		}
		sql.append("   AND TVM.NORMAL_ORDER_FLAG = " + Constant.IF_TYPE_YES + "\n");
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return rs;
	}
	
	public static List<Map<String, Object>> getTreeCodeByAreaId(String area_id)
	{
		StringBuffer sql = new StringBuffer();
		if (null != area_id && !"".equals(area_id))
		{
			sql.append("SELECT G.TREE_CODE, LENGTH (G.TREE_CODE) CODE_LENGTH \n");
			sql.append("  FROM TM_VHCL_MATERIAL_GROUP G \n");
			sql.append(" WHERE G.GROUP_ID IN (SELECT AG.MATERIAL_GROUP_ID \n");
			sql.append("                        FROM TM_AREA_GROUP AG \n");
			sql.append("                       WHERE AG.AREA_ID = " + area_id + ") \n");
			return dao.pageQuery(sql.toString(), null, dao.getFunName());
		}
		else
		{
			return null;
		}
		
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// 查询所有能与业务范围相关联的车系
	public List<Map<String, Object>> MateGroupQuery()
	{
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select tvmg.group_id, tvmg.group_code, tvmg.group_name, tag.area_id\n");
		sql.append("  from tm_vhcl_material_group tvmg, TM_AREA_GROUP tag\n");
		sql.append(" where tvmg.group_level = 2\n");
		sql.append("   and tvmg.group_id = tag.material_group_id\n");
		sql.append(" order by tvmg.group_name,tvmg.group_code,tvmg.group_id\n");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	/**
	 * Function : 查询当前经销商
	 * 
	 * @param : 职位ID
	 */
	public static List<Map<String, Object>> getDealerId(String comId, String poseId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TBA.AREA_CODE, TBA.AREA_ID, TBA.AREA_NAME, TD.DEALER_ID,TD.DEALER_CODE\n");
		sql.append("  FROM TC_POSE                 TP,\n");
		sql.append("       TM_DEALER               TD,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");
		sql.append("       TM_BUSINESS_AREA        TBA,\n");
		sql.append("       TM_POSE_BUSINESS_AREA   TPBA\n");
		sql.append(" WHERE TP.ORG_ID = TD.DEALER_ORG_ID\n");
		sql.append("   AND TD.DEALER_ID = TDBA.DEALER_ID\n");
		sql.append("   AND TDBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TPBA.POSE_ID = TP.POSE_ID\n");
		sql.append("   AND TDBA.AREA_ID = TPBA.AREA_ID\n");
		sql.append("   AND TD.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		//sql.append("   AND TD.DEALER_TYPE = "+Constant.DEALER_TYPE_DVS+"\n");
		sql.append("   AND (TD.DEALER_TYPE = " + Constant.DEALER_TYPE_DVS + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_JSZX + " OR TD.DEALER_TYPE = " + Constant.DEALER_TYPE_QYZDL + ")\n");
		if (!"".equals(poseId) && poseId != null)
		{
			sql.append("   AND TP.POSE_ID = " + poseId + "\n");
			;
		}
		if (!"".equals(comId) && comId != null)
		{
			sql.append("   AND TD.Company_ID = " + comId + "\n");
			;
		}
		sql.append("   ORDER BY TD.DEALER_NAME,TD.DEALER_CODE\n");
		
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * 查询月度常规订单提报的起止日期
	 */
	public static List<Map<String, Object>> getCommonOrderDate(Long oemCompanyId)
	{
		StringBuffer sql = new StringBuffer();
		if (null != oemCompanyId && !"".equals(oemCompanyId))
		{
			sql.append("SELECT BP.PARA_VALUE\n");
			sql.append("  FROM TM_BUSINESS_PARA BP\n");
			sql.append(" WHERE BP.OEM_COMPANY_ID = " + oemCompanyId + "\n");
			sql.append("   AND BP.TYPE_CODE = 2021\n");
			sql.append(" ORDER BY BP.PARA_ID\n");
			
			return dao.pageQuery(sql.toString(), null, dao.getFunName());
		}
		else
		{
			return null;
		}
		
	}
	
	// 查询所有下级物料组(点击展开图标)
	public List<Map<String, Object>> querySubMaterialGroupList(String poseId, String groupId, String groupLevel,
					String isAllArea)
	{
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select T.GROUP_ID,\n");
		sbSql.append("       T.GROUP_CODE,\n");
		sbSql.append("       T.GROUP_NAME,\n");
		sbSql.append("       T.PARENT_GROUP_ID,\n");
		sbSql.append("       T.TREE_CODE,\n");
		sbSql.append("       T.GROUP_LEVEL\n");
		sbSql.append("from tm_vhcl_material_group t\n");
		sbSql.append("where 1=1 \n");
//		sbSql.append(" and T.FORCAST_FLAG = 0\n");
		sbSql.append("and T.STATUS = ?\n");
		params.add(Constant.STATUS_ENABLE);
		sbSql.append("and t.parent_group_id = ?\n");
		params.add(Long.valueOf(groupId));
		sbSql.append("and t.group_level = (?+1)");
		params.add(Integer.valueOf(groupLevel));
		if (!isAllArea.toLowerCase().equals("true"))
		{
			String areaIds = getAreaIdsByPoseId(new Long(poseId));
			
			if (areaIds != null && !areaIds.equals(""))
			{
				sbSql.append(" AND T.GROUP_ID IN\n");
				sbSql.append("    (select T.group_id\n");
				sbSql.append("       from tm_vhcl_material_group t\n");
				sbSql.append("      start with T.group_id IN\n");
				sbSql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
				sbSql.append("                    FROM tm_area_group tap\n");
				sbSql.append("                   where 1=1\n");
				sbSql.append(Utility.getConSqlByParamForEqual(areaIds, params,"tap", "area_id"));
				sbSql.append("                                                         )\n");
				sbSql.append("     connect by prior T.group_id = T.parent_group_id\n");
				sbSql.append("     union\n");
				sbSql.append("     select T.group_id\n");
				sbSql.append("       from tm_vhcl_material_group t\n");
				sbSql.append("      start with T.group_id IN\n");
				sbSql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
				sbSql.append("                    FROM tm_area_group tap\n");
				sbSql.append("                   where 1=1\n");
				sbSql.append(Utility.getConSqlByParamForEqual(areaIds, params,"tap", "area_id"));
				sbSql.append("                                                         )\n");
				sbSql.append("     connect by prior T.parent_group_id = T.group_id)");
			}
		}
		
		return dao.pageQuery(sbSql.toString(), params, dao.getFunName());
	}
	
	/**
	 * 查询所有下级物料组
	 * 
	 * @param poseId
	 *            职位ID
	 * @param groupId
	 *            物料组ID
	 * @param groupLevel
	 *            物料组等级
	 * @param areaId
	 *            业务范围
	 *            HXY
	 *            2012-05-10
	 */
	public List<Map<String, Object>> querySubMaterialGroupListByConf(String poseId, String groupId, String groupLevel,
					String areaId)
	{
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");
		sql.append("       T.GROUP_NAME,\n");
		sql.append("       T.PARENT_GROUP_ID,\n");
		sql.append("       T.TREE_CODE,\n");
		sql.append("       T.GROUP_LEVEL\n");
		sql.append("from tm_vhcl_material_group t\n");
		sql.append("where 1=1\n");
		sql.append("and t.parent_group_id = " + groupId + "\n");
		sql.append("and t.group_level = (" + groupLevel + "+1)");
		
		if (areaId != null && !areaId.equals(""))
		{
			sql.append(" AND T.GROUP_ID IN\n");
			sql.append("    (select T.group_id\n");
			sql.append("       from tm_vhcl_material_group t\n");
			sql.append("      start with T.group_id IN\n");
			sql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                    FROM tm_area_group tap\n");
			sql.append("                   where tap.area_id in (" + areaId + "))\n");
			sql.append("     connect by prior T.group_id = T.parent_group_id\n");
			sql.append("     union\n");
			sql.append("     select T.group_id\n");
			sql.append("       from tm_vhcl_material_group t\n");
			sql.append("      start with T.group_id IN\n");
			sql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                    FROM tm_area_group tap\n");
			sql.append("                   where tap.area_id in (" + areaId + "))\n");
			sql.append("     connect by prior T.parent_group_id = T.group_id)");
		}
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	/**
	 * 获经销商查询SQL (选择经销商查询时使用（多选）)
	 * 
	 * @param dealerColumn
	 * @param dealerCode
	 * @return
	 */
	public static String getDealerQuerySql(String dealerColumn, String dealerCode)
	{
		StringBuffer buffer = new StringBuffer();
		if (null != dealerCode && !"".equals(dealerCode))
		{
			String[] dealerCodes = dealerCode.split(",");
			if (null != dealerCodes && dealerCodes.length > 0)
			{
				for (int i = 0; i < dealerCodes.length; i++)
				{
					buffer.append("'").append(dealerCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("AND EXISTS(\n");
		sb.append("    SELECT 1 FROM TM_DEALER A\n");
		sb.append("    WHERE A.DEALER_CODE IN(" + buffer.toString() + ") AND A.DEALER_ID=" + dealerColumn + "\n");
		sb.append("   )");
		return sb.toString();
	}
	/**
	 * 物流商权限SQL
	 * 
	 * @author ranj 2013 - 8-24
	 * @param poseId
	 * @param yieldlyColumn
	 * @return
	 * 
	 */
	public static String getPoseIdLogiSql(String logiColumn, String poseId)
	{
		TcPosePO posePo = getTcPosePO(Long.parseLong(poseId));
		StringBuffer sql = new StringBuffer();
		if (posePo.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
		{
			sql.append(" and  exists (\n");
			sql.append("     select TPS.logi_id from tc_pose TPS where TPS.logi_id="+logiColumn+" and TPS.pose_id="+poseId+" and TPS.pose_status="+Constant.STATUS_ENABLE+"\n");
			sql.append(" )\n"); 
		}
		return sql.toString();
	}
	/**
	 * 物流商权限SQL(分派专用)
	 * 
	 * @author ranj 2013 - 8-24
	 * @param poseId
	 * @param yieldlyColumn
	 * @return
	 * 
	 */
	public static String getPoseIdLogiAssSql(String poseId)
	{
		TcPosePO posePo = getTcPosePO(Long.parseLong(poseId));
		StringBuffer sql = new StringBuffer();
		if (posePo.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
		{
			sql.append("and s.ass_id in(\r\n");
			sql.append("select t.ass_id from tt_sales_ass_detail t where 1=1\r\n");
			sql.append(" and  exists (\n");
			sql.append("     select TPS.logi_id from tc_pose TPS where TPS.logi_id=t.logi_id and TPS.pose_id="+poseId+" and TPS.pose_status="+Constant.STATUS_ENABLE+"\n");
			sql.append(" )\n"); 
			sql.append(")"); 

		}
		return sql.toString();
	}
	public static String getPoseIdLogiSqlByPar(String logiColumn, String poseId,List par)
	{
		TcPosePO posePo = getTcPosePO(Long.parseLong(poseId));
		StringBuffer sql = new StringBuffer();
		if (posePo.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
		{
			sql.append(" and  exists (\n");
			sql.append("     select TPS.logi_id from tc_pose TPS where TPS.logi_id="+logiColumn+" and TPS.pose_id=? and TPS.pose_status="+Constant.STATUS_ENABLE+"\n");
			sql.append(" )\n"); 
			par.add(Long.valueOf(poseId));
		}
		return sql.toString();
	}
	/**
	 * VIN查询（模糊查询）
	 * @author liufazhong
	 */
	public static String vinSqlLike(String vinColumn, String vin) {
		StringBuffer sql = new StringBuffer();
		sql.append("AND EXISTS(SELECT BO_ID\n");
		sql.append("  FROM TT_SALES_BO_DETAIL M, TT_SALES_ALLOCA_DE B, TM_VEHICLE N\n");
		sql.append(" WHERE M.BO_DE_ID = B.BO_DE_ID\n");
		sql.append("   AND B.VEHICLE_ID = N.VEHICLE_ID\n");
		sql.append("   AND "+vinColumn+" = M.BO_ID\n");
		sql.append("   AND N.VIN like '%"+vin+"%' \n");
		sql.append("   AND M.IS_ENABLE="+Constant.IF_TYPE_YES+")");
		return sql.toString();
	} 
	
	public static PageResult<Map<String, Object>> getMaterialListByOrder(
			String poseId, String groupId, String materialCode,
			String materialName, int curPage, int pageSize, Long companyId,
			String isAllArea,String ids) {

		List par = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVM.MATERIAL_CODE,TVM.MATERIAL_ID,TVM.MATERIAL_NAME,TVM.COLOR_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL TVM, TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
		sql.append(" WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("  AND TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");
		if (!"".equals(materialCode) && materialCode != null) {
			sql.append("   AND TVM.MATERIAL_CODE LIKE ?\n");
			par.add("%" + materialCode + "%");
		}
		if (!"".equals(materialName) && materialName != null) {
			sql.append("   AND TVM.MATERIAL_NAME LIKE ?\n");
			par.add("%" + materialName + "%");
		}
		if(!"".equals(ids)&&ids!=null&&!ids.equals("null")){
			sql.append("   AND TVM.MATERIAL_ID NOT IN ( \n");
			sql.append(""+ ids +"");
			sql.append(")\n");
		}
		if (!"".equals(groupId) && groupId != null) {
			sql.append("   AND TVM.MATERIAL_ID IN\n");
			sql.append("       (SELECT TVMGR.MATERIAL_ID\n");
			sql.append("          FROM TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
			sql.append("         WHERE TVMGR.GROUP_ID IN\n");
			sql.append("               (SELECT TVMG.GROUP_ID\n");
			sql.append("                  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");
			sql.append("                 START WITH TVMG.GROUP_ID = ?\n");
			sql.append("                CONNECT BY PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID\n");
			sql.append("                       AND TVMG.STATUS = "
					+ Constant.STATUS_ENABLE + "))\n");
			par.add(Long.valueOf(groupId));
		}
		//isAllArea 仓库ID
		if (!"".equals(isAllArea)&&isAllArea!=null) {
			//String areaIds = getAreaIdsByPoseId(new Long(poseId));

			sql.append(" AND TVMGR.GROUP_ID IN\n");
			sql.append("    (select T.group_id\n");
			sql.append("       from tm_vhcl_material_group t\n");
			sql.append("      start with T.group_id IN\n");
			sql.append("                 (SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("                    FROM TM_WAREHOUSE_GROUP tap\n");
			sql.append("                   where 1=1\n");
			sql.append(Utility.getConSqlByParamForEqual(isAllArea, par, "tap",
					"warehouse_id"));
			sql.append("                 )\n");
			sql.append("     connect by prior T.group_id = T.parent_group_id)\n");
		}

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), par,
				dao.getFunName(), pageSize, curPage);
		return rs;
	}	
}
