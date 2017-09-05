package com.infodms.dms.dao.conservation;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class EnergyConservationOemDao extends BaseDao{
	
	private static final EnergyConservationOemDao dao = new EnergyConservationOemDao();
	
	public static final EnergyConservationOemDao getInstance() {
		return dao;
	}
	/**
	 * 节能惠民申请查询
	 * @param dealerId
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	
	public PageResult<Map<String, Object>> energyConApplyQuery(
			String saleStartDate,
			String saleEndDate,String reportStartDate,String reportEndDate,
			String dealerCode,String model,String orgId,String areaId,
			String energyNo,String vin,String dPro,String dCity,
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
		sql.append(" WHERE 1=1 AND A.CONSERVATION_STATUS="+Constant.VEHICLE_ENERGY_CON_APPLY+"\n");

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
		if (Utility.testString(saleStartDate)) {//提报开始日期
			sql.append("   AND A.SALES_DATE >= TO_DATE('").append(saleStartDate.trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(saleEndDate)) {//提报结束日期
			sql.append("   AND A.SALES_DATE <= TO_DATE('").append(saleEndDate.trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(Utility.testString(orgId)){
			sql.append(" AND A.ORG_ID = "+orgId+" \n");
		}
		if(Utility.testString(areaId)){
			sql.append(" AND EXISTS (SELECT H.AREA_ID FROM TM_DEALER_BUSINESS_AREA H WHERE H.DEALER_ID=A.DEALER_ID AND H.AREA_ID LIKE '%"+areaId+"%') \n");
		}
		if(Utility.testString(energyNo)){
			sql.append(" AND A.CONSERVATION_NO LIKE '%"+energyNo+"%' \n");
		}
		if(Utility.testString(dPro)){
			sql.append(" AND A.DLR_PROVICE_ID = "+dPro+" \n");
		}
		if(Utility.testString(dCity)){
			sql.append(" AND A.DLR_CITY_ID = "+dCity+" \n");
		}
		if(Utility.testString(vin)){
			sql.append(" AND EXISTS (SELECT I.VEHICLE_ID FROM TM_VEHICLE I WHERE I.VEHICLE_ID=A.VEHICLE_ID "+GetVinUtil.getVins(vin,"I")+") \n");
		}
		if(Utility.testString(dealerCode)){
			if (null != dealerCode && !"".equals(dealerCode)) {
				String[] array = dealerCode.split(",");
				sql.append("   AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append("))\n");
			}
//			sql.append(" AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE LIKE '%"+dealerCode+"%') \n");
		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> energyConExportQuery(String isEx,
			String reportStartDate,String reportEndDate,
			String dealerCode,String model,String orgId,String areaId,
			String energyNo,String vin,String dPro,String dCity,
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
		sql.append("       A.DLR_LINKMAN,\n");
		sql.append("       A.DLR_LINKTEL,\n");
		sql.append("       A.DLR_ZIP_CODE,\n");
		sql.append("       (SELECT H.REGION_NAME FROM TM_REGION H WHERE H.REGION_CODE=A.DLR_PROVICE_ID) AS DLR_PROVICE_NAME,\n");
		sql.append("       (SELECT H.REGION_NAME FROM TM_REGION H WHERE H.REGION_CODE=A.DLR_CITY_ID) AS DLR_CITY_NAME,\n");
		sql.append("       (SELECT H.REGION_NAME FROM TM_REGION H WHERE H.REGION_CODE=A.DLR_TOWN_ID) AS DLR_TOWN_NAME,\n");
		sql.append("	   (SELECT H.REGION_NAME FROM TM_REGION H WHERE H.REGION_CODE=A.DLR_ZIP_CODE) AS DLR_ZIP_NAME,\n");
		sql.append("       A.DLR_PROVICE_ID,\n");
		sql.append("       A.DLR_ADDRESS,\n");
		sql.append("       (SELECT F.CTM_NAME FROM TT_CUSTOMER F WHERE F.CTM_ID=A.CTM_ID) AS CTM_NAME,\n");
		sql.append("       A.CTM_ID,\n");
		sql.append("       A.CTM_LINKMAN,\n");
		sql.append("       A.CTM_LINKTEL,\n");
		sql.append("       A.CTM_ZIP_CODE,\n");
		sql.append("       (SELECT Z.REGION_NAME FROM TM_REGION Z WHERE Z.REGION_CODE=A.CTM_PROVICE_ID) AS CTM_PRO_NAME,\n");
		sql.append("       (SELECT Z.REGION_NAME FROM TM_REGION Z WHERE Z.REGION_CODE=A.CTM_CITY_ID) AS CTM_CITY_NAME,\n");
		sql.append("       (SELECT Z.REGION_NAME FROM TM_REGION Z WHERE Z.REGION_CODE=A.CTM_TOWN_ID) AS CTM_TOWN_NAME,\n");
		sql.append("	   (SELECT Z.REGION_NAME FROM TM_REGION Z WHERE Z.REGION_CODE=A.CTM_ZIP_CODE) AS CTM_ZIP_NAME,\n");
		sql.append("       A.CTM_PROVICE_ID,\n");
		sql.append("       A.CTM_CITY_ID,\n");
		sql.append("       A.CTM_TOWN_ID,\n");
		sql.append("       A.CTM_ADDRESS,\n");
		sql.append("       A.ORG_ID,\n");
		sql.append("       A.CONSERVATION_STATUS,\n");
		sql.append("       A.STATUS,\n");
		sql.append("       A.IS_EXPORT,\n");
		sql.append("       decode(A.EXPORT_FLAG, 0, '') EXPORT_FLAG,\n");
		sql.append("       A.EXPORT_USER,\n");
		sql.append("       A.EXPORT_POSE,\n");
		sql.append("       A.CREATE_BY,\n");
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-dd') AS CREATE_DATE,\n");
		sql.append("       A.REMARK\n");
		sql.append("  FROM tt_vs_energy_conservation A\n");
		sql.append(" WHERE 1=1 AND A.CONSERVATION_STATUS="+Constant.VEHICLE_ENERGY_CON_SALEPASS+"\n");
		
		if(!"-1".equals(isEx)) {
			sql.append("  AND A.IS_EXPORT = ").append(isEx).append("\n");
		}

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
		if(Utility.testString(orgId)){
			sql.append(" AND A.ORG_ID = "+orgId+" \n");
		}
		if(Utility.testString(areaId)){
			sql.append(" AND EXISTS (SELECT H.AREA_ID FROM TM_DEALER_BUSINESS_AREA H WHERE H.DEALER_ID=A.DEALER_ID AND H.AREA_ID LIKE '%"+areaId+"%') \n");
		}
		if(Utility.testString(energyNo)){
			sql.append(" AND A.CONSERVATION_NO LIKE '%"+energyNo+"%' \n");
		}
		if(Utility.testString(dPro)){
			sql.append(" AND A.DLR_PROVICE_ID = "+dPro+" \n");
		}
		if(Utility.testString(dCity)){
			sql.append(" AND A.DLR_CITY_ID = "+dCity+" \n");
		}
		if(Utility.testString(vin)){
			sql.append(" AND EXISTS (SELECT I.VEHICLE_ID FROM TM_VEHICLE I WHERE I.VEHICLE_ID=A.VEHICLE_ID "+GetVinUtil.getVins(vin,"I")+") \n");
		}
		if(Utility.testString(dealerCode)){
			if (null != dealerCode && !"".equals(dealerCode)) {
				String[] array = dealerCode.split(",");
				sql.append("   AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append("))\n");
			}
//			sql.append(" AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE LIKE '%"+dealerCode+"%') \n");
		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
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
		sql.append("       TO_CHAR(A.PAY_DATE,'YYYY-MM-DD')AS PAY_DATE,\n");
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
	
	public PageResult<Map<String, Object>> energyConOutCheckQuery(
			String expVer,String saleStartDate,
			String saleEndDate,String reportStartDate,String reportEndDate,
			String dealerCode,String model,String orgId,String areaId,
			String energyNo,String vin,String dPro,String dCity,
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
		sql.append("       tcu.name,\n");
		sql.append("       TO_CHAR(A.UPDATE_DATE, 'YYYY-MM-dd') AS UPDATE_DATE,\n");
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-dd') AS CREATE_DATE,\n");
		sql.append("       A.REMARK\n");
		sql.append("  FROM tt_vs_energy_conservation A, tc_user tcu\n");
		sql.append(" WHERE 1=1 and a.EXPORT_USER = tcu.user_id AND A.CONSERVATION_STATUS="+Constant.VEHICLE_ENERGY_CON_SALEPASS+"\n");
		sql.append("  AND A.IS_EXPORT=1\n");
		
		if(Utility.testString(expVer)){
			sql.append(" AND A.EXPORT_FLAG like '%"+expVer+"%' \n");
		}
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
		if (Utility.testString(saleStartDate)) {//提报开始日期
			sql.append("   AND A.SALES_DATE >= TO_DATE('").append(saleStartDate.trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(saleEndDate)) {//提报结束日期
			sql.append("   AND A.SALES_DATE <= TO_DATE('").append(saleEndDate.trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(Utility.testString(orgId)){
			sql.append(" AND A.ORG_ID = "+orgId+" \n");
		}
		if(Utility.testString(dPro)){
			sql.append(" AND A.DLR_PROVICE_ID = "+dPro+" \n");
		}
		if(Utility.testString(dCity)){
			sql.append(" AND A.DLR_CITY_ID = "+dCity+" \n");
		}
		if(Utility.testString(areaId)){
			sql.append(" AND EXISTS (SELECT H.AREA_ID FROM TM_DEALER_BUSINESS_AREA H WHERE H.DEALER_ID=A.DEALER_ID AND H.AREA_ID LIKE '%"+areaId+"%') \n");
		}
		if(Utility.testString(energyNo)){
			sql.append(" AND A.CONSERVATION_NO LIKE '%"+energyNo+"%' \n");
		}
		if(Utility.testString(vin)){
			sql.append(" AND EXISTS (SELECT I.VEHICLE_ID FROM TM_VEHICLE I WHERE I.VEHICLE_ID=A.VEHICLE_ID "+GetVinUtil.getVins(vin,"I")+") \n");
		}
		if(Utility.testString(dealerCode)){
			if (null != dealerCode && !"".equals(dealerCode)) {
				String[] array = dealerCode.split(",");
				sql.append("   AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append("))\n");
			}
//			sql.append(" AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE LIKE '%"+dealerCode+"%') \n");
		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 节能惠民申请查询
	 * @param dealerId
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	
	public List<Map<String, Object>> energyConExportVerNoQuery(String userId) {		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT A.EXPORT_FLAG FROM TT_VS_ENERGY_CONSERVATION A WHERE A.EXPORT_USER="+userId+" AND A.CONSERVATION_STATUS="+Constant.VEHICLE_ENERGY_CON_SALEPASS+"\n");

		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 节能惠民申请查询
	 * @param dealerId
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	
	public PageResult<Map<String, Object>> energyConViewQuery(
			String saleStartDate, String saleEndDate,String orgId,
			String dPro, String dCity, String dealerCode,
			String reportStartDate,String reportEndDate,
		    String model,String areaId,String status,
			String energyNo,String vin,String postId,
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
		sql.append("       decode(A.EXPORT_FLAG, 0, '', A.EXPORT_FLAG) EXPORT_FLAG,\n");
		sql.append("       A.EXPORT_USER,\n");
		sql.append("       A.EXPORT_POSE,\n");
		sql.append("       A.CREATE_BY,\n");
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-dd') AS CREATE_DATE,\n");
		sql.append("       A.REMARK\n");
		sql.append("  FROM tt_vs_energy_conservation A\n");
		sql.append(" WHERE 1=1 \n");

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
		if (Utility.testString(saleStartDate)) {//提报开始日期
			sql.append("   AND A.SALES_DATE >= TO_DATE('").append(saleStartDate.trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(saleEndDate)) {//提报结束日期
			sql.append("   AND A.SALES_DATE <= TO_DATE('").append(saleEndDate.trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(orgId.equals(Constant.DUTY_TYPE_LARGEREGION)){
			sql.append(" AND A.ORG_ID = "+orgId+" \n");
		}
		if(Utility.testString(areaId)){
			sql.append(" AND EXISTS (SELECT H.AREA_ID FROM TM_DEALER_BUSINESS_AREA H WHERE H.DEALER_ID=A.DEALER_ID AND H.AREA_ID LIKE '%"+areaId+"%') \n");
		}
		if(Utility.testString(energyNo)){
			sql.append(" AND A.CONSERVATION_NO LIKE '%"+energyNo+"%' \n");
		}
		if(Utility.testString(status)){
			sql.append(" AND A.CONSERVATION_STATUS = "+status+" \n");
		}
		if(Utility.testString(dPro)){
			sql.append(" AND A.DLR_PROVICE_ID = "+dPro+" \n");
		}
		if(Utility.testString(dCity)){
			sql.append(" AND A.DLR_CITY_ID = "+dCity+" \n");
		}
		if(Utility.testString(vin)){
			sql.append(" AND EXISTS (SELECT I.VEHICLE_ID FROM TM_VEHICLE I WHERE I.VEHICLE_ID=A.VEHICLE_ID "+GetVinUtil.getVins(vin,"I")+") \n");
		}
		if(Utility.testString(dealerCode)){
			if (null != dealerCode && !"".equals(dealerCode)) {
				String[] array = dealerCode.split(",");
				sql.append("   AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append("))\n");
			}
//			sql.append(" AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE LIKE '%"+dealerCode+"%') \n");
		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 节能惠民申请汇总查询
	 * @param dealerId
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	
	public PageResult<Map<String, Object>> energyConViewTotalQuery(
			String saleStartDate, String saleEndDate,String orgId,
			String dPro, String dCity, String dealerCode,
			String reportStartDate,String reportEndDate,
		    String model,String areaId,String status,
			String energyNo,String vin,String postId,
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
		sql.append("       A.PAY_DATE,\n");
		sql.append("       A.PAY_MONEY,\n");
		sql.append("       A.FACTORY_PRICE,\n");
		sql.append("       A.SALES_PRICE,\n");
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
		sql.append(" WHERE 1=1 \n");

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
		if (Utility.testString(saleStartDate)) {//提报开始日期
			sql.append("   AND A.SALES_DATE >= TO_DATE('").append(saleStartDate.trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(saleEndDate)) {//提报结束日期
			sql.append("   AND A.SALES_DATE <= TO_DATE('").append(saleEndDate.trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(orgId.equals(Constant.DUTY_TYPE_LARGEREGION)){
			sql.append(" AND A.ORG_ID = "+orgId+" \n");
		}
		if(Utility.testString(areaId)){
			sql.append(" AND EXISTS (SELECT H.AREA_ID FROM TM_DEALER_BUSINESS_AREA H WHERE H.DEALER_ID=A.DEALER_ID AND H.AREA_ID LIKE '%"+areaId+"%') \n");
		}
		if(Utility.testString(energyNo)){
			sql.append(" AND A.CONSERVATION_NO LIKE '%"+energyNo+"%' \n");
		}
		if(Utility.testString(status)){
			sql.append(" AND A.CONSERVATION_STATUS = "+status+" \n");
		}
		if(Utility.testString(dPro)){
			sql.append(" AND A.DLR_PROVICE_ID = "+dPro+" \n");
		}
		if(Utility.testString(dCity)){
			sql.append(" AND A.DLR_CITY_ID = "+dCity+" \n");
		}
		if(Utility.testString(vin)){
			sql.append(" AND EXISTS (SELECT I.VEHICLE_ID FROM TM_VEHICLE I WHERE I.VEHICLE_ID=A.VEHICLE_ID "+GetVinUtil.getVins(vin,"I")+") \n");
		}
		if(Utility.testString(dealerCode)){
			if (null != dealerCode && !"".equals(dealerCode)) {
				String[] array = dealerCode.split(",");
				sql.append("   AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append("))\n");
			}
//			sql.append(" AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE LIKE '%"+dealerCode+"%') \n");
		}
		sql.append(" ORDER BY A.DEALER_ID,A.CREATE_DATE DESC\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> getTotal(Map<String, String> map, int curPage, int pageSize) {
		String model = map.get("model") ;
		String reportStartDate = map.get("reportStartDate") ;
		String reportEndDate = map.get("reportEndDate") ;
		String saleStartDate = map.get("saleStartDate") ;
		String saleEndDate = map.get("saleEndDate") ;
		String orgId = map.get("orgId") ;
		String areaId = map.get("areaId") ;
		String energyNo = map.get("energyNo") ;
		String status = map.get("status") ;
		String dPro = map.get("dPro") ;
		String vin = map.get("vin") ;
		String dealerCode = map.get("dealerCode") ;
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvec.dlr_provice_id,\n");
		sql.append("       tmo.org_name,\n");  
		sql.append("       tmd.dealer_shortname,\n");  
		sql.append("       tvmg1.group_code,\n");  
		sql.append("       tmr.region_name DLR_PROVICE_NAME,\n");  
		sql.append("       tvmg1.group_name,\n");  
		sql.append("       count(1) amount,\n");  
		sql.append("       sum(nvl(tvec.pay_money, 0)) totolprice\n");
		sql.append("  from TT_VS_ENERGY_CONSERVATION tvec,\n");  
		sql.append("       tm_dealer                 tmd,\n");  
		sql.append("       tm_org                    tmo,\n");  
		sql.append("       tm_region                 tmr,\n");  
		sql.append("       tm_vhcl_material_group    tvmg,\n"); 
		sql.append("       tm_vhcl_material_group    tvmg1\n"); 
		sql.append(" where tvec.dealer_id = tmd.dealer_id\n");  
		sql.append("   and tvec.org_id = tmo.org_id\n");  
		sql.append("   and tvec.model_id = tvmg.group_id\n"); 
		sql.append("   and tvec.dlr_provice_id = tmr.region_code\n"); 
		sql.append("   and tvmg.parent_group_id = tvmg1.group_id\n"); 
		
		if(Utility.testString(model)){
			String[] materialCodes = model.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append(" AND EXISTS (SELECT K.GROUP_ID FROM TM_VHCL_MATERIAL_GROUP K WHERE K.GROUP_ID=tvec.MODEL_ID AND K.GROUP_CODE IN("+buffer+"))\n");
			}
//			sql.append(" AND EXISTS (SELECT K.GROUP_ID FROM TM_VHCL_MATERIAL_GROUP K WHERE K.GROUP_ID=A.MODEL_ID AND K.GROUP_CODE LIKE '%"+model+"%') \n");
		}
		if (Utility.testString(reportStartDate)) {//提报开始日期
			sql.append("   AND tvec.CREATE_DATE >= TO_DATE('").append(reportStartDate.trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(reportEndDate)) {//提报结束日期
			sql.append("   AND tvec.CREATE_DATE <= TO_DATE('").append(reportEndDate.trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(saleStartDate)) {//提报开始日期
			sql.append("   AND tvec.SALES_DATE >= TO_DATE('").append(saleStartDate.trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(saleEndDate)) {//提报结束日期
			sql.append("   AND tvec.SALES_DATE <= TO_DATE('").append(saleEndDate.trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(orgId.equals(Constant.DUTY_TYPE_LARGEREGION)){
			sql.append(" AND tvec.ORG_ID = "+orgId+" \n");
		}
		if(Utility.testString(areaId)){
			sql.append(" AND EXISTS (SELECT H.AREA_ID FROM TM_DEALER_BUSINESS_AREA H WHERE H.DEALER_ID=tvec.DEALER_ID AND H.AREA_ID LIKE '%"+areaId+"%') \n");
		}
		if(Utility.testString(energyNo)){
			sql.append(" AND tvec.CONSERVATION_NO LIKE '%"+energyNo+"%' \n");
		}
		if(Utility.testString(status)){
			sql.append(" AND tvec.CONSERVATION_STATUS = "+status+" \n");
		}
		if(Utility.testString(dPro)){
			sql.append(" AND tvec.DLR_PROVICE_ID = "+dPro+" \n");
		}
		if(Utility.testString(vin)){
			sql.append(" AND EXISTS (SELECT I.VEHICLE_ID FROM TM_VEHICLE I WHERE I.VEHICLE_ID=tvec.VEHICLE_ID "+GetVinUtil.getVins(vin,"I")+") \n");
		}
		if(Utility.testString(dealerCode)){
			if (null != dealerCode && !"".equals(dealerCode)) {
				String[] array = dealerCode.split(",");
				sql.append("   AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=tvec.DEALER_ID AND J.DEALER_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append("))\n");
			}
//			sql.append(" AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE LIKE '%"+dealerCode+"%') \n");
		}
		
		sql.append(" group by tvec.dlr_provice_id,\n");  
		sql.append("          tmo.org_name,\n");  
		sql.append("          tmd.dealer_shortname,\n");  
		sql.append("          tvmg1.group_code,tmr.region_name,\n");  
		sql.append("          tvmg1.group_name\n");

		return super.pageQuery(sql.toString(), null, super.getFunName(), pageSize, curPage) ;
	}
	
	public PageResult<Map<String, Object>> energyConExport(Map<String, String> map, int curPage, int pageSize) {	
		String saleStartDate = map.get("saleStartDate") ;
		String saleEndDate = map.get("saleEndDate") ;
		String reportStartDate = map.get("reportStartDate") ;
		String reportEndDate = map.get("reportEndDate") ;
		String dealerCode = map.get("dealerCode") ;
		String model = map.get("model") ;
		String orgId = map.get("orgId") ;
		String areaId = map.get("areaId") ;
		String energyNo = map.get("energyNo") ;
		String vin = map.get("vin") ;
		String status = map.get("status") ;
		String dPro = map.get("dPro") ;
		String dCity = map.get("dCity") ;
		String isEx = map.get("isEx") ;
		String dealerId = map.get("dealerId") ;
		
		StringBuffer sql = new StringBuffer("\n");
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
		sql.append("        tmv.vin,\n");
		sql.append("       A.VEHICLE_ID,\n");
		sql.append("       A.MODEL_ID,\n");
		sql.append("       TO_CHAR(A.SALES_DATE, 'YYYY-MM-dd') AS SALES_DATE,\n");
		sql.append("       A.INVOCE_NO,\n");
		sql.append("       A.VEHICLE_NO,\n");
		sql.append("       A.FACTORY_PRICE,\n");
		sql.append("       A.SALES_PRICE,\n");
		sql.append("       A.DLR_LINKMAN,\n");
		sql.append("       A.DLR_LINKTEL,\n");
		sql.append("       A.DLR_ZIP_CODE,\n");
		sql.append("       (SELECT H.REGION_NAME FROM TM_REGION H WHERE H.REGION_CODE=A.DLR_PROVICE_ID) AS DLR_PROVICE_NAME,\n");
		sql.append("       (SELECT H.REGION_NAME FROM TM_REGION H WHERE H.REGION_CODE=A.DLR_CITY_ID) AS DLR_CITY_NAME,\n");
		sql.append("       (SELECT H.REGION_NAME FROM TM_REGION H WHERE H.REGION_CODE=A.DLR_TOWN_ID) AS DLR_TOWN_NAME,\n");
		sql.append("	   (SELECT H.REGION_NAME FROM TM_REGION H WHERE H.REGION_CODE=A.DLR_ZIP_CODE) AS DLR_ZIP_NAME,\n");
		sql.append("       A.DLR_PROVICE_ID,\n");
		sql.append("       A.DLR_ADDRESS,\n");
		sql.append("       (SELECT F.CTM_NAME FROM TT_CUSTOMER F WHERE F.CTM_ID=A.CTM_ID) AS CTM_NAME,\n");
		sql.append("       A.CTM_ID,\n");
		sql.append("       A.CTM_LINKMAN,\n");
		sql.append("       A.CTM_LINKTEL,\n");
		sql.append("       A.CTM_ZIP_CODE,\n");
		sql.append("       (SELECT Z.REGION_NAME FROM TM_REGION Z WHERE Z.REGION_CODE=A.CTM_PROVICE_ID) AS CTM_PRO_NAME,\n");
		sql.append("       (SELECT Z.REGION_NAME FROM TM_REGION Z WHERE Z.REGION_CODE=A.CTM_CITY_ID) AS CTM_CITY_NAME,\n");
		sql.append("       (SELECT Z.REGION_NAME FROM TM_REGION Z WHERE Z.REGION_CODE=A.CTM_TOWN_ID) AS CTM_TOWN_NAME,\n");
		sql.append("	   (SELECT Z.REGION_NAME FROM TM_REGION Z WHERE Z.REGION_CODE=A.CTM_ZIP_CODE) AS CTM_ZIP_NAME,\n");
		sql.append("       A.CTM_PROVICE_ID,\n");
		sql.append("       A.CTM_CITY_ID,\n");
		sql.append("       A.CTM_TOWN_ID,\n");
		sql.append("       A.CTM_ADDRESS,\n");
		sql.append("       A.ORG_ID,\n");
		sql.append("       A.CONSERVATION_STATUS,\n");
		sql.append("       A.STATUS,\n");
		sql.append("       A.IS_EXPORT,\n");
		sql.append("       decode(A.EXPORT_FLAG, 0, '') EXPORT_FLAG,\n");
		sql.append("       A.EXPORT_USER,\n");
		sql.append("       A.EXPORT_POSE,\n");
		sql.append("       A.CREATE_BY,\n");
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-dd') AS CREATE_DATE,\n");
		sql.append("       A.REMARK\n");
		sql.append("  FROM tt_vs_energy_conservation A, tm_vehicle tmv\n");
		sql.append(" WHERE 1=1 and a.vehicle_id = tmv.vehicle_id \n");
		

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
		if (Utility.testString(saleStartDate)) {
			sql.append("   AND trunc(a.sales_date) >= TO_DATE('").append(saleStartDate.trim()).append("','YYYY-MM-DD') \n");
		}
		if (Utility.testString(saleEndDate)) {
			sql.append("   AND trunc(a.sales_date) <= TO_DATE('").append(saleEndDate.trim()).append("','YYYY-MM-DD') \n");
		}
		if (Utility.testString(reportStartDate)) {//提报开始日期
			sql.append("   AND A.CREATE_DATE >= TO_DATE('").append(reportStartDate.trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(reportEndDate)) {//提报结束日期
			sql.append("   AND A.CREATE_DATE <= TO_DATE('").append(reportEndDate.trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(Utility.testString(orgId)){
			sql.append(" AND A.ORG_ID = "+orgId+" \n");
		}
		if(Utility.testString(status)){
			sql.append(" AND A.CONSERVATION_STATUS = "+status+" \n");
		}
		if(Utility.testString(areaId)){
			sql.append(" AND EXISTS (SELECT H.AREA_ID FROM TM_DEALER_BUSINESS_AREA H WHERE H.DEALER_ID=A.DEALER_ID AND H.AREA_ID LIKE '%"+areaId+"%') \n");
		}
		if(Utility.testString(energyNo)){
			sql.append(" AND A.CONSERVATION_NO LIKE '%"+energyNo+"%' \n");
		}
		if(Utility.testString(dPro)){
			sql.append(" AND A.DLR_PROVICE_ID = "+dPro+" \n");
		}
		if(Utility.testString(dCity)){
			sql.append(" AND A.DLR_CITY_ID = "+dCity+" \n");
		}
		if(Utility.testString(vin)){
			sql.append(" AND EXISTS (SELECT I.VEHICLE_ID FROM TM_VEHICLE I WHERE I.VEHICLE_ID=A.VEHICLE_ID "+GetVinUtil.getVins(vin,"I")+") \n");
		}
		if(Utility.testString(dealerId)){
			sql.append(" AND A.DEALER_ID in ("+dealerId+")\n");
		}
		if(Utility.testString(dealerCode)){
			if (null != dealerCode && !"".equals(dealerCode)) {
				String[] array = dealerCode.split(",");
				sql.append("   AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append("))\n");
			}
//			sql.append(" AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE LIKE '%"+dealerCode+"%') \n");
		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public int energyConApplyUpadate(String saleStartDate, String saleEndDate,String reportStartDate,String reportEndDate, String dealerCode,String model,String orgId,String areaId, String energyNo,String vin,String dPro,String dCity) {		
		StringBuffer sql = new StringBuffer("\n");
		sql.append("  update  tt_vs_energy_conservation A SET A.CONSERVATION_STATUS = " + Constant.VEHICLE_ENERGY_CON_SALEPASS + "\n");
		sql.append(" WHERE 1=1 AND A.CONSERVATION_STATUS="+Constant.VEHICLE_ENERGY_CON_APPLY+"\n");

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
		if (Utility.testString(saleStartDate)) {//提报开始日期
			sql.append("   AND A.SALES_DATE >= TO_DATE('").append(saleStartDate.trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(saleEndDate)) {//提报结束日期
			sql.append("   AND A.SALES_DATE <= TO_DATE('").append(saleEndDate.trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(Utility.testString(orgId)){
			sql.append(" AND A.ORG_ID = "+orgId+" \n");
		}
		if(Utility.testString(areaId)){
			sql.append(" AND EXISTS (SELECT H.AREA_ID FROM TM_DEALER_BUSINESS_AREA H WHERE H.DEALER_ID=A.DEALER_ID AND H.AREA_ID LIKE '%"+areaId+"%') \n");
		}
		if(Utility.testString(energyNo)){
			sql.append(" AND A.CONSERVATION_NO LIKE '%"+energyNo+"%' \n");
		}
		if(Utility.testString(dPro)){
			sql.append(" AND A.DLR_PROVICE_ID = "+dPro+" \n");
		}
		if(Utility.testString(dCity)){
			sql.append(" AND A.DLR_CITY_ID = "+dCity+" \n");
		}
		if(Utility.testString(vin)){
			sql.append(" AND EXISTS (SELECT I.VEHICLE_ID FROM TM_VEHICLE I WHERE I.VEHICLE_ID=A.VEHICLE_ID "+GetVinUtil.getVins(vin,"I")+") \n");
		}
		if(Utility.testString(dealerCode)){
			if (null != dealerCode && !"".equals(dealerCode)) {
				String[] array = dealerCode.split(",");
				sql.append("   AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append("))\n");
			}
//			sql.append(" AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE LIKE '%"+dealerCode+"%') \n");
		}
		
		return super.update(sql.toString(), null) ;
	}
	
	public int energyConOutCheckUpdate(String expVer,String saleStartDate, String saleEndDate,String reportStartDate,String reportEndDate, String dealerCode,String model,String orgId,String areaId, String energyNo,String vin,String dPro,String dCity) {		
		StringBuffer sql = new StringBuffer("\n");
		sql.append("  update tt_vs_energy_conservation A set A.CONSERVATION_STATUS="+Constant.VEHICLE_ENERGY_CON_PASS+"\n");
		sql.append(" WHERE 1=1 and A.CONSERVATION_STATUS="+Constant.VEHICLE_ENERGY_CON_SALEPASS+"\n");
		sql.append("  AND A.IS_EXPORT=1\n");
		
		if(Utility.testString(expVer)){
			sql.append(" AND A.EXPORT_FLAG like '%"+expVer+"%' \n");
		}
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
		if (Utility.testString(saleStartDate)) {//提报开始日期
			sql.append("   AND A.SALES_DATE >= TO_DATE('").append(saleStartDate.trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(saleEndDate)) {//提报结束日期
			sql.append("   AND A.SALES_DATE <= TO_DATE('").append(saleEndDate.trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(Utility.testString(orgId)){
			sql.append(" AND A.ORG_ID = "+orgId+" \n");
		}
		if(Utility.testString(dPro)){
			sql.append(" AND A.DLR_PROVICE_ID = "+dPro+" \n");
		}
		if(Utility.testString(dCity)){
			sql.append(" AND A.DLR_CITY_ID = "+dCity+" \n");
		}
		if(Utility.testString(areaId)){
			sql.append(" AND EXISTS (SELECT H.AREA_ID FROM TM_DEALER_BUSINESS_AREA H WHERE H.DEALER_ID=A.DEALER_ID AND H.AREA_ID LIKE '%"+areaId+"%') \n");
		}
		if(Utility.testString(energyNo)){
			sql.append(" AND A.CONSERVATION_NO LIKE '%"+energyNo+"%' \n");
		}
		if(Utility.testString(vin)){
			sql.append(" AND EXISTS (SELECT I.VEHICLE_ID FROM TM_VEHICLE I WHERE I.VEHICLE_ID=A.VEHICLE_ID "+GetVinUtil.getVins(vin,"I")+") \n");
		}
		if(Utility.testString(dealerCode)){
			if (null != dealerCode && !"".equals(dealerCode)) {
				String[] array = dealerCode.split(",");
				sql.append("   AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append("))\n");
			}
//			sql.append(" AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_ID=A.DEALER_ID AND J.DEALER_CODE LIKE '%"+dealerCode+"%') \n");
		}
		
		return super.update(sql.toString(), null) ;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
