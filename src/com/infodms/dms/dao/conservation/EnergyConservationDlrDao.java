package com.infodms.dms.dao.conservation;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class EnergyConservationDlrDao extends BaseDao {
	
	private static final EnergyConservationDlrDao dao = new EnergyConservationDlrDao();
	
	public static final EnergyConservationDlrDao getInstance() {
		return dao;
	}
	/**
	 * 节能惠民车辆查询
	 * @param dealerId
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	
	public PageResult<Map<String, Object>> saleVehicleQuery(String saleDate,
			String saleStartDate,String saleEndDate,
			String model,String areaId,
			String vin,
			String dealerId, int curPage, int pageSize) {		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.VEHICLE_ID,\n");
		sql.append("       A.VIN,\n");
		sql.append("       A.ORG_ID,\n");
		sql.append("       A.LIFE_CYCLE,\n");
		sql.append("       A.LICENSE_NO,\n");
		sql.append("       A.ENGINE_NO,\n");
		sql.append("       TO_CHAR(A.PURCHASED_DATE, 'YYYY-MM-dd') AS PURCHASED_DATE,\n");
		sql.append("       TO_CHAR(A.PRODUCT_DATE, 'YYYY-MM-dd') AS PRODUCT_DATE,\n");
		sql.append("       A.YIELDLY,\n");
		sql.append("       (SELECT E.GROUP_CODE\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP E\n");
		sql.append("         WHERE E.GROUP_ID = A.MODEL_ID) AS MODELCODE,\n");
		sql.append("       (SELECT F.GROUP_NAME\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP F\n");
		sql.append("         WHERE F.GROUP_ID = A.MODEL_ID) AS MODELNAME, B.DEALER_ID,\n");
		sql.append("       B.ORDER_ID,\n");
		sql.append("       B.PRICE,\n");
		sql.append("       B.VEHICLE_NO,\n");
		//sql.append("       C.DEALER_ID,\n");
		sql.append("       D.CTM_NAME,\n");
		sql.append("       D.MAIN_PHONE\n");
		
		//Modify by WHX,2012.10.17
		//================================================Start
		sql.append("  FROM TM_VEHICLE A, tt_dealer_actual_sales B, TM_DEALER C, TT_CUSTOMER D,tt_conservation_model tcm\n");
		//================================================End
		
//		sql.append("  MINUS SELECT G.VEHICLE_ID FROM tt_vs_energy_conservation G\n");
		sql.append(" WHERE A.VEHICLE_ID = B.VEHICLE_ID\n");
		sql.append("   AND A.DEALER_ID = C.DEALER_ID\n");
		sql.append("   AND B.CTM_ID = D.CTM_ID\n");
		sql.append("and a.model_id = tcm.group_id\n") ;
		
		//Modify by WHX,2012.10.17
		//================================================Start
		sql.append("   and b.sales_date >=\n") ;
		sql.append("       trunc(nvl(tcm.limit_date, to_date('19770101', 'yyyy-mm-dd')))\n") ;
		//================================================End

		sql.append("   AND B.is_return = ").append(Constant.IF_TYPE_NO).append("\n");
		sql.append("   AND A.DEALER_ID IN( "+dealerId+")\n");
		sql.append("   AND A.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_04+"\n");
		sql.append("   AND NOT EXISTS (SELECT G.VEHICLE_ID FROM tt_vs_energy_conservation G WHERE G.VEHICLE_ID=A.VEHICLE_ID and g.conservation_status in (").append(Constant.VEHICLE_ENERGY_CON_APPLY).append(",").append(Constant.VEHICLE_ENERGY_CON_SALEPASS).append(",").append(Constant.VEHICLE_ENERGY_CON_PASS).append("))\n");

		if(Utility.testString(model)){
			String[] materialCodes = model.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append(" AND EXISTS (SELECT X.GROUP_ID FROM TM_VHCL_MATERIAL_GROUP X WHERE X.GROUP_CODE IN ("+buffer+") AND X.GROUP_ID=A.MODEL_ID)\n");
			}
//			sql.append(" AND A.MODEL_ID LIKE '%"+model+"%' \n");
		}
		if (Utility.testString(saleStartDate)) {//提报开始日期
			sql.append("   AND A.PURCHASED_DATE >= TO_DATE('").append(saleStartDate.trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(saleEndDate)) {//提报结束日期
			sql.append("   AND A.PURCHASED_DATE <= TO_DATE('").append(saleEndDate.trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(saleDate)) {//提报结束日期
			sql.append("   AND b.sales_date >= TO_DATE('").append(saleDate.trim()).append("','YYYY-MM-DD') \n");
		}
		if(Utility.testString(areaId)){
			sql.append(" AND EXISTS (SELECT H.AREA_ID FROM TM_DEALER_BUSINESS_AREA H WHERE H.DEALER_ID=A.DEALER_ID AND H.AREA_ID LIKE '%"+areaId+"%') \n");
		}
		if(Utility.testString(vin)){
			sql.append(GetVinUtil.getVins(vin,"A")); //sql.append(" AND A.VIN LIKE '%"+vin+"%' \n");
		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 节能惠民车辆明细查询
	 * @param dealerId
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	
	public Map<String, Object> saleVehicleDetailQuery(String vehicleId) {		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.VIN,\n");
		sql.append("       A.VEHICLE_ID,\n");
		sql.append("       TO_CHAR(A.PURCHASED_DATE, 'YYYY-MM-dd') AS PURCHASED_DATE,\n");
		sql.append("       A.MODEL_ID,\n");
		sql.append("       (SELECT F.ROOT_ORG_ID ORG_ID FROM VW_ORG_DEALER F WHERE F.DEALER_ID=C.DEALER_ID) AS ORG_ID,\n");
		sql.append("       (SELECT E.GROUP_NAME\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP E\n");
		sql.append("         WHERE E.GROUP_ID = A.MODEL_ID) AS MODELNAME,\n");
		sql.append("       (SELECT E.GROUP_CODE\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP E\n");
		sql.append("         WHERE E.GROUP_ID = A.MODEL_ID) AS MODELCODE,\n");
		sql.append("       B.ORDER_ID AS SALES_ID,\n");
		sql.append("       B.VEHICLE_NO,\n");       
		sql.append("       substr(B.INVOICE_NO, length(B.INVOICE_NO)-5,6) INVOICE_NO,\n");
		sql.append("       B.CONTRACT_NO,\n");
		sql.append("       B.PRICE,\n");
		sql.append("       TO_CHAR(B.SALES_DATE, 'YYYY-MM-dd') AS SALES_DATE,\n");
		sql.append("       C.DEALER_ID,\n");
		sql.append("       C.DEALER_NAME,\n");
		sql.append("       C.DEALER_CODE,\n");
		sql.append("       C.ZIP_CODE,\n");
		sql.append("       C.PROVINCE_ID,\n");
		sql.append("       C.CITY_ID,\n");
		sql.append("       C.ADDRESS AS DEALER_ADDRESS,\n");
		sql.append("       C.PHONE,\n");
		sql.append("       C.LINK_MAN,\n");
		sql.append("       C.DEALER_SHORTNAME,\n");
		sql.append("       C.PROVINCE_ID,\n");
		sql.append("       C.CITY_ID,\n");
		sql.append("       D.CTM_NAME,\n");
		sql.append("       D.CTM_ID,\n");
		sql.append("       D.MAIN_PHONE,\n");
		sql.append("       D.COUNTRY,\n");
		sql.append("       D.PROVINCE,\n");
		sql.append("       D.CITY,\n");
		sql.append("       D.TOWN,\n");
		sql.append("       D.ADDRESS AS CTM_ADDRESS,\n");
		sql.append("       D.CTM_TYPE,\n");
		sql.append("       D.POST_CODE\n");
		sql.append("  FROM TM_VEHICLE A, tt_dealer_actual_sales B, TM_DEALER C, TT_CUSTOMER D\n");
		sql.append(" WHERE A.VEHICLE_ID = B.VEHICLE_ID\n");
		sql.append("   AND A.DEALER_ID = C.DEALER_ID\n");
		sql.append("   AND B.CTM_ID = D.CTM_ID\n");
		sql.append("   AND B.is_return = ").append(Constant.IF_TYPE_NO).append("\n");
		sql.append("   AND A.VEHICLE_ID in ("+vehicleId+")\n");


		Map<String, Object> map = this.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> mainQuery(String con,int pageSize,int curPage){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select s.CONSERVATION_ID,s.CONSERVATION_NO,\n" );
		sql.append("       to_char(s.CREATE_DATE,'yyyy-MM-dd') CREATE_DATE,\n" );		
		sql.append("       TC.CODE_DESC STATUS_CODE,\n" );		
		sql.append("       TG.GROUP_CODE VEHICLE_CODE,\n" );		
		sql.append("       TG.GROUP_NAME VEHICLE,\n" );
		sql.append("       TV.VIN   VEHICLE_NO,\n" );
		sql.append("       TCU.CTM_NAME CTM_ID,\n" );
		sql.append("       S.CTM_LINKMAN,\n");
		sql.append("       s.CTM_LINKTEL,\n" );		
		sql.append("       to_char(s.SALES_DATE,'yyyy-MM-dd') SALES_DATE,\n" );
		sql.append("       s.DEALER_ID\n" );
		sql.append("  from TT_VS_ENERGY_CONSERVATION s,TC_CODE TC," +
				" TM_VHCL_MATERIAL_GROUP TG,TM_VEHICLE TV,TT_CUSTOMER TCU \n" );
		
		sql.append("where S.STATUS=TC.CODE_ID AND TC.TYPE=8013 AND S.MODEL_ID=TG.GROUP_ID " +
		" AND S.VEHICLE_ID=TV.VEHICLE_ID AND S.CTM_ID=TCU.CTM_ID \n");
		
		if(StringUtil.notNull(con))
			
			sql.append(con);
		
		sql.append(" order by s.CONSERVATION_NO desc\n");
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	
	
	/**
	 * 节能惠民申请查询
	 * @param dealerId
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	
	public List<Map<String, Object>> energyConStatusInfo(String conservationId) {		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.CHANGE_ID, A.STATUS, A.DESCRIPTION, TO_CHAR(A.CREATE_DATE, 'YYYY-MM-dd') AS CREATE_DATE, (SELECT B.NAME FROM TC_USER B WHERE B.USER_ID=A.CREATE_BY) AS CREATE_BY\n");
		sql.append("  FROM tt_vs_energy_conservation_chg A\n");
		sql.append(" WHERE A.CONSERVATION_ID = "+conservationId+"\n");

		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * 节能惠民申请查询
	 * @param dealerId
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	
	public Map<String, Object> energyConDetailCheck(String conservationId) {		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.CONSERVATION_ID,\n");
		sql.append("       A.CONSERVATION_NO,\n");
		sql.append("       A.DEALER_ID, (SELECT B.DEALER_CODE\n");
		sql.append("                      FROM TM_DEALER B\n");
		sql.append("                     WHERE B.DEALER_ID = A.DEALER_ID) AS DEALERCODE,\n");
		sql.append("       (SELECT C.DEALER_NAME\n");
		sql.append("          FROM TM_DEALER C\n");
		sql.append("         WHERE C.DEALER_ID = A.DEALER_ID) AS DEALERNAME,\n");
		sql.append("       (SELECT D.GROUP_CODE\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP D\n");
		sql.append("         WHERE D.GROUP_ID = A.MODEL_ID) AS MODELCODE,\n");
		sql.append("       (SELECT E.GROUP_NAME\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP E\n");
		sql.append("         WHERE E.GROUP_ID = A.MODEL_ID) AS MODELNAME,\n");
		sql.append("       A.SALES_ID,\n");
		sql.append("       (SELECT G.VIN FROM TM_VEHICLE G WHERE G.VEHICLE_ID=A.VEHICLE_ID) AS VIN,\n");
		sql.append("       A.VEHICLE_ID,\n");
		sql.append("       A.MODEL_ID,\n");
		sql.append("       TO_CHAR(A.SALES_DATE, 'YYYY-MM-dd') AS SALES_DATE,\n");
		sql.append("       A.INVOCE_NO,\n");
		sql.append("       A.VEHICLE_NO,\n");
		sql.append("       A.FACTORY_PRICE,\n");
		sql.append("       A.SALES_PRICE,\n");
		sql.append("       TO_CHAR(A.PAY_DATE,'YYYY-MM-DD') AS PAY_DATE,\n");
		sql.append("       A.PAY_MONEY,\n");
		sql.append("       A.DLR_LINKMAN,\n");
		sql.append("       A.DLR_LINKTEL,\n");
		sql.append("       A.DLR_ZIP_CODE,\n");
		sql.append("       (SELECT H.REGION_NAME FROM TM_REGION H WHERE H.REGION_CODE=A.DLR_PROVICE_ID) AS DLR_PROVICE_ID,\n");
		sql.append("       (SELECT H.REGION_NAME FROM TM_REGION H WHERE H.REGION_CODE=A.DLR_CITY_ID) AS DLR_CITY_ID,\n");
		sql.append("       (SELECT H.REGION_NAME FROM TM_REGION H WHERE H.REGION_CODE=A.DLR_TOWN_ID) AS DLR_TOWN_ID,\n");
		sql.append("       A.DLR_ADDRESS,\n");
		sql.append("       (SELECT F.CTM_NAME FROM TT_CUSTOMER F WHERE F.CTM_ID=A.CTM_ID) AS CTM_NAME,\n");
		sql.append("       A.CTM_ID,\n");
		sql.append("       A.CTM_LINKMAN,\n");
		sql.append("       A.CTM_LINKTEL,\n");
		sql.append("       A.CTM_ZIP_CODE,\n");
		sql.append("       (SELECT H.REGION_NAME FROM TM_REGION H WHERE H.REGION_CODE=A.CTM_PROVICE_ID) AS CTM_PROVICE_ID,\n");
		sql.append("       (SELECT H.REGION_NAME FROM TM_REGION H WHERE H.REGION_CODE=A.CTM_CITY_ID) AS CTM_CITY_ID,\n");
		sql.append("       (SELECT H.REGION_NAME FROM TM_REGION H WHERE H.REGION_CODE=A.CTM_TOWN_ID) AS CTM_TOWN_ID,\n");
		sql.append("       A.CTM_ADDRESS,\n");
		sql.append("       A.ORG_ID,\n");
		sql.append("       A.CONSERVATION_STATUS,\n");
		sql.append("       A.STATUS,\n");
		sql.append("       A.IS_EXPORT,\n");
		sql.append("       A.EXPORT_FLAG,\n");
		sql.append("       A.EXPORT_USER,\n");
		sql.append("       A.EXPORT_POSE,\n");
		sql.append("       A.CREATE_BY,\n");
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-dd') AS CREATE_DATE,\n");
		sql.append("       A.REMARK\n");
		sql.append("  FROM tt_vs_energy_conservation A\n");
		sql.append(" WHERE A.CONSERVATION_ID="+conservationId+"\n");


		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	
	public Map<String, Object> energyConDetailCheckA(String conservationId) {		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.CONSERVATION_ID,\n");
		sql.append("       A.CONSERVATION_NO,\n");  
		sql.append("       A.DEALER_ID,\n");  
		sql.append("       (SELECT B.DEALER_CODE\n");  
		sql.append("          FROM TM_DEALER B\n");  
		sql.append("         WHERE B.DEALER_ID = A.DEALER_ID) AS DEALERCODE,\n");  
		sql.append("       (SELECT C.DEALER_NAME\n");  
		sql.append("          FROM TM_DEALER C\n");  
		sql.append("         WHERE C.DEALER_ID = A.DEALER_ID) AS DEALERNAME,\n");  
		sql.append("       (SELECT D.GROUP_CODE\n");  
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP D\n");  
		sql.append("         WHERE D.GROUP_ID = A.MODEL_ID) AS MODELCODE,\n");  
		sql.append("       (SELECT E.GROUP_NAME\n");  
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP E\n");  
		sql.append("         WHERE E.GROUP_ID = A.MODEL_ID) AS MODELNAME,\n");  
		sql.append("       A.SALES_ID,\n");  
		sql.append("       (SELECT G.VIN FROM TM_VEHICLE G WHERE G.VEHICLE_ID = A.VEHICLE_ID) AS VIN,\n");  
		sql.append("       A.VEHICLE_ID,\n");  
		sql.append("       A.MODEL_ID,\n");  
		sql.append("       TO_CHAR(A.SALES_DATE, 'YYYY-MM-DD') AS SALES_DATE,\n");  
		sql.append("       A.INVOCE_NO,\n");  
		sql.append("       A.VEHICLE_NO,\n");  
		sql.append("       A.FACTORY_PRICE,\n");  
		sql.append("       A.SALES_PRICE,\n");  
		sql.append("       TO_CHAR(A.PAY_DATE, 'YYYY-MM-DD') AS PAY_DATE,\n");  
		sql.append("       A.PAY_MONEY,\n");  
		sql.append("       A.DLR_LINKMAN,\n");  
		sql.append("       A.DLR_LINKTEL,\n");  
		sql.append("       A.DLR_ZIP_CODE,\n");  
		sql.append("       A.DLR_PROVICE_ID,\n");  
		sql.append("       A.DLR_CITY_ID,\n");  
		sql.append("       A.DLR_TOWN_ID,\n");  
		sql.append("       A.DLR_ADDRESS,\n");  
		sql.append("       (SELECT F.CTM_NAME FROM TT_CUSTOMER F WHERE F.CTM_ID = A.CTM_ID) AS CTM_NAME,\n");  
		sql.append("       A.CTM_ID,\n");  
		sql.append("       A.CTM_LINKMAN,\n");  
		sql.append("       A.CTM_LINKTEL,\n");  
		sql.append("       A.CTM_ZIP_CODE,\n");  
		sql.append("       A.CTM_PROVICE_ID,\n");  
		sql.append("       A.CTM_CITY_ID,\n");  
		sql.append("       A.CTM_TOWN_ID,\n");  
		sql.append("       A.CTM_ADDRESS,\n");  
		sql.append("       A.ORG_ID,\n");  
		sql.append("       A.CONSERVATION_STATUS,\n");  
		sql.append("       A.STATUS,\n");  
		sql.append("       A.IS_EXPORT,\n");  
		sql.append("       A.EXPORT_FLAG,\n");  
		sql.append("       A.EXPORT_USER,\n");  
		sql.append("       A.EXPORT_POSE,\n");  
		sql.append("       A.CREATE_BY,\n");  
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') AS CREATE_DATE,\n");  
		sql.append("       A.REMARK\n");  
		sql.append("  FROM TT_VS_ENERGY_CONSERVATION A\n");  
		sql.append(" WHERE A.CONSERVATION_ID="+conservationId+"\n");

		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * 节能惠民申请查询
	 * @param dealerId
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	
	public PageResult<Map<String, Object>> energyConViewQuery(
			String reportStartDate,String reportEndDate,
		    String model,String areaId,String status,
			String energyNo,String vin,String dealerId,
			int curPage, int pageSize) {		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.CONSERVATION_ID,\n");
		sql.append("       A.CONSERVATION_NO,\n");
		sql.append("       A.DEALER_ID, (SELECT B.DEALER_CODE\n");
		sql.append("                      FROM TM_DEALER B\n");
		sql.append("                     WHERE B.DEALER_ID = A.DEALER_ID) AS DEALERCODE,\n");
		sql.append("       (SELECT C.DEALER_NAME\n");
		sql.append("          FROM TM_DEALER C\n");
		sql.append("         WHERE C.DEALER_ID = A.DEALER_ID) AS DEALERNAME,\n");
		sql.append("       (SELECT D.GROUP_CODE\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP D\n");
		sql.append("         WHERE D.GROUP_ID = A.MODEL_ID) AS MODELCODE,\n");
		sql.append("       (SELECT E.GROUP_NAME\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP E\n");
		sql.append("         WHERE E.GROUP_ID = A.MODEL_ID) AS MODELNAME,\n");
		sql.append("        (SELECT W.GROUP_NAME FROM TM_VHCL_MATERIAL_GROUP W WHERE W.GROUP_ID=(\n");
		sql.append("       (SELECT E.PARENT_GROUP_ID\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP E\n");
		sql.append("         WHERE E.GROUP_ID = A.MODEL_ID))) AS SERIESNAME,\n");
		sql.append("       A.SALES_ID,\n");
		sql.append("       (SELECT G.VIN FROM TM_VEHICLE G WHERE G.VEHICLE_ID=A.VEHICLE_ID) AS VIN,\n");
		sql.append("       A.VEHICLE_ID,\n");
		sql.append("       A.MODEL_ID,\n");
		sql.append("       TO_CHAR(A.SALES_DATE, 'YYYY-MM-dd') AS SALES_DATE,\n");
		sql.append("       A.INVOCE_NO,\n");
		sql.append("       A.VEHICLE_NO,\n");
		sql.append("       A.FACTORY_PRICE,\n");
		sql.append("       A.SALES_PRICE,\n");
		sql.append("       TO_CHAR(A.PAY_DATE,'YYYY-MM-DD') AS PAY_DATE,\n");
		sql.append("       A.PAY_MONEY,\n");
		sql.append("       A.DLR_LINKMAN,\n");
		sql.append("       A.DLR_LINKTEL,\n");
		sql.append("       A.DLR_ZIP_CODE,\n");
		sql.append("       A.DLR_PROVICE_ID,\n");
		sql.append("       A.DLR_ADDRESS,\n");
		sql.append("       (SELECT F.CTM_NAME FROM TT_CUSTOMER F WHERE F.CTM_ID=A.CTM_ID) AS CTM_NAME,\n");
		sql.append("       A.CTM_ID,\n");
		sql.append("       A.CTM_LINKMAN,\n");
		sql.append("       A.CTM_LINKTEL,\n");
		sql.append("       A.CTM_ZIP_CODE,\n");
		sql.append("       A.CTM_PROVICE_ID,\n");
		sql.append("       A.CTM_ADDRESS,\n");
		sql.append("       A.ORG_ID,\n");
		sql.append("       A.CONSERVATION_STATUS,\n");
		sql.append("       A.STATUS,\n");
		sql.append("       A.IS_EXPORT,\n");
		sql.append("       A.EXPORT_FLAG,\n");
		sql.append("       A.EXPORT_USER,\n");
		sql.append("       A.EXPORT_POSE,\n");
		sql.append("       A.CREATE_BY,\n");
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-dd') AS CREATE_DATE,\n");
		sql.append("       A.REMARK\n");
		sql.append("  FROM tt_vs_energy_conservation A\n");
		sql.append(" WHERE 1=1 AND A.DEALER_ID IN ("+dealerId+")\n");

		if(Utility.testString(model)){
			String[] materialCodes = model.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append(" AND EXISTS (SELECT K.GROUP_ID FROM TM_VHCL_MATERIAL_GROUP K WHERE K.GROUP_ID=A.MODEL_ID AND K.GROUP_CODE IN("+buffer+"))\n");
			}
//			sql.append(" AND EXISTS (SELECT K.GROUP_ID FROM TM_VHCL_MATERIAL_GROUP K WHERE K.GROUP_ID=A.MODEL_ID AND K.GROUP_CODE LIKE '%"+model+"%') \n");
		}
		if (Utility.testString(reportStartDate)) {//提报开始日期
			sql.append("   AND A.CREATE_DATE >= TO_DATE('").append(reportStartDate.trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(reportEndDate)) {//提报结束日期
			sql.append("   AND A.CREATE_DATE <= TO_DATE('").append(reportEndDate.trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
//		if (Utility.testString(saleStartDate)) {//提报开始日期
//			sql.append("   AND A.SALES_DATE >= TO_DATE('").append(saleStartDate.trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
//		}
//		if (Utility.testString(saleEndDate)) {//提报结束日期
//			sql.append("   AND A.SALES_DATE <= TO_DATE('").append(saleEndDate.trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
//		}
//		if(Utility.testString(orgId)){
//			sql.append(" AND A.ORG_ID LIKE '%"+orgId+"%' \n");
//		}
		if(Utility.testString(areaId)){
			sql.append(" AND EXISTS (SELECT H.AREA_ID FROM TM_DEALER_BUSINESS_AREA H WHERE H.DEALER_ID=A.DEALER_ID AND H.AREA_ID LIKE '%"+areaId+"%') \n");
		}
		if(Utility.testString(energyNo)){
			sql.append(" AND A.CONSERVATION_NO LIKE '%"+energyNo+"%' \n");
		}
		if(Utility.testString(status)){
			sql.append(" AND A.CONSERVATION_STATUS = "+status+" \n");
		}
//		if(Utility.testString(dPro)){
//			sql.append(" AND A.DLR_PROVICE_ID = "+dPro+" \n");
//		}
//		if(Utility.testString(dCity)){
//			sql.append(" AND A.DLR_CITY_ID = "+dCity+" \n");
//		}
		if(Utility.testString(vin)){
			sql.append(" AND EXISTS (SELECT I.VEHICLE_ID FROM TM_VEHICLE I WHERE I.VEHICLE_ID=A.VEHICLE_ID "+GetVinUtil.getVins(vin,"I")+") \n");
		}
//		if(Utility.testString(dealerCode)){
//			sql.append(" AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE LIKE '%"+dealerCode+"%') \n");
//		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public List<Map<String, Object>> getEnergyList(Map<String, String> map) {
		String isLast = map.get("isLast") ;//是否取最后一条
		String dealerId = map.get("dealerId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		if(Constant.STATUS_ENABLE.toString().equals(isLast)) {
			sql.append("select tvec1.dealer_id,\n");
			sql.append("       tvec1.dlr_linktel,\n");  
			sql.append("       tvec1.dlr_linkman,\n");  
			sql.append("       tvec1.dlr_provice_id,\n");  
			sql.append("       tvec1.dlr_city_id,\n");  
			sql.append("       tvec1.dlr_town_id,\n");  
			sql.append("       tvec1.dlr_address\n");  
			sql.append("  from (\n");  
		}
		
		sql.append("  select tvec.dealer_id,\n");  
		sql.append("               tvec.dlr_linktel,\n");  
		sql.append("               tvec.dlr_linkman,\n");  
		sql.append("               tvec.dlr_provice_id,\n");  
		sql.append("               tvec.dlr_city_id,\n");  
		sql.append("               tvec.dlr_town_id,\n");  
		sql.append("               tvec.dlr_address\n");  
		sql.append("          from tt_vs_energy_conservation tvec\n");  
		sql.append("         where 1 = 1\n"); 
		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append("           and tvec.dealer_id = ").append(dealerId).append("\n");
		}
		
		if(Constant.STATUS_ENABLE.toString().equals(isLast)) {
			sql.append("         order by rownum desc) tvec1\n");  
			sql.append(" where rownum = 1\n");
		}

		
		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public List<Map<String, Object>> chkVhlNo(String vNo) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select 1\n");
		sql.append("  from tt_vs_energy_conservation tvec\n");  
		sql.append(" where tvec.vehicle_no = '").append(vNo).append("'\n");  
		sql.append("   and tvec.conservation_status <> ").append(Constant.VEHICLE_ENERGY_CON_CANCLE).append("\n");
		
		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
}
