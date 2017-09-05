package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class OrgStorageDetailQueryDao extends BaseDao {
	public static final Logger logger = Logger
			.getLogger(BillDetailTicketDao.class);
	public static OrgStorageDetailQueryDao dao = new OrgStorageDetailQueryDao();

	public static OrgStorageDetailQueryDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map<String, Object>> getOrgStorageDetailReportInfo(Map<String, Object> param,List<Map<String, Object>> kList,List<Map<String, Object>> sList,List<Map<String, Object>> list,
			Integer pageSize, Integer curPage) {
		String areaId = (String)param.get("areaId");
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("WITH R_ORG_MODEL AS (\n");
		sbSql.append("       SELECT ORG.ORG_ID,ORG.ORG_NAME,\n");
		sbSql.append("              VMG.GROUP_ID MODEL_ID,VMG.GROUP_CODE MODEL_CODE,VMG.GROUP_NAME MODEL_NAME\n");
		sbSql.append("         FROM TM_ORG ORG,\n");
		sbSql.append("              TM_VHCL_MATERIAL_GROUP VMG\n");
		sbSql.append("        WHERE ORG.ORG_LEVEL = 2\n");
		if(areaId != null && !"".equals(areaId)){
			sbSql.append("    AND VMG.PARENT_GROUP_ID IN (SELECT MATERIAL_GROUP_ID FROM TM_AREA_GROUP WHERE AREA_ID = "+areaId+")\n");
		}
		sbSql.append("          AND VMG.GROUP_LEVEL = 3 ),--大区与车型的笛卡尔积\n");
		sbSql.append("     R_ORG_MODEL_C AS (\n");
		sbSql.append("       SELECT  ORG2.ORG_ID,ORG2.ORG_NAME,\n");
		sbSql.append("               TV.MODEL_ID,COUNT(TV.VEHICLE_ID) AMOUNT\n");
		sbSql.append("         FROM  TM_VEHICLE TV,TM_DEALER_ORG_RELATION DOR,\n");
		sbSql.append("               TM_ORG ORG, TM_ORG ORG2, TM_DEALER TD\n");
		sbSql.append("        WHERE  TV.DEALER_ID = TD.DEALER_ID AND (TD.DEALER_ID = DOR.DEALER_ID OR TD.PARENT_DEALER_D = DOR.DEALER_ID )\n");
		sbSql.append("          AND ((TV.LIFE_CYCLE = 10321002 AND TV.LOCK_STATUS=10241008) OR (TV.LIFE_CYCLE = 10321005 AND TV.LOCK_STATUS=10241001) OR (TV.LIFE_CYCLE = 10321003))\n");
		sbSql.append("          AND TV.DEALER_ID>0 \n");//没有挂上经销商的车辆不纳入统计
		sbSql.append("          AND  DOR.ORG_ID = ORG.ORG_ID\n");
		sbSql.append("          AND  ORG.PARENT_ORG_ID = ORG2.ORG_ID\n");
		if(areaId != null && !"".equals(areaId)){
			sbSql.append("    AND TV.YIELDLY = "+areaId+"\n");
		}
		sbSql.append("         GROUP BY ORG2.ORG_ID,ORG2.ORG_NAME,TV.MODEL_ID),--查询车辆表，按大区与车型分组\n");
		sbSql.append("   ORG_CENSUS  AS (  SELECT  A.ORG_ID,A.ORG_NAME,\n");
		//商用车、乘用车循环
		if(kList != null && kList.size()>0){
			String tempString = "";
			for(int j = 0 ; j < kList.size() ; j++){
				Map<String, Object> mapp = kList.get(j);
				String vehicleKind = mapp.get("VEHICLE_KIND").toString();	
				String tempStr = "";
				//车系循环
				if(sList != null && sList.size()>0){
					for(int k = 0 ; k < sList.size() ; k++){
						Map<String, Object> map = sList.get(k);
						String vehicle_kind = map.get("VEHICLE_KIND").toString();
						if(vehicleKind.equals(vehicle_kind)){
							String series_id = map.get("GROUP_ID").toString();
							String series_name = map.get("GROUP_NAME").toString();
							String temp = "";
							//车型循环
							if(list != null && list.size()>0){
								for(int i = 0 ; i < list.size() ; i++){
									Map<String, Object> map_1 = list.get(i);
									String seriesId = map_1.get("SERIES_ID").toString();
									//循环当前车系下的车型
									if(series_id.equals(seriesId)){
										Long MODEL_ID = Long.valueOf(map_1.get("MODEL_ID").toString());
										//String MODEL_NAME = (String)map_1.get("MODEL_NAME");
										sbSql.append("SUM(DECODE(B.MODEL_ID,"+MODEL_ID+",NVL(B.AMOUNT,0),0)) AS A"+MODEL_ID+",\n");
										temp += "\nSUM(DECODE(B.MODEL_ID,"+MODEL_ID+",NVL(B.AMOUNT,0),0))+" ;
										tempStr += "\nSUM(DECODE(B.MODEL_ID,"+MODEL_ID+",NVL(B.AMOUNT,0),0))+";
										tempString += "\nSUM(DECODE(B.MODEL_ID,"+MODEL_ID+",NVL(B.AMOUNT,0),0))+";
									}					
								}
							    
							}
							if(!"".equals(temp)){
								sbSql.append(temp.substring(0,temp.length()-1)+" AS "+series_name+"小计,\n");
							}
						}				
					}
				}
				if(!"".equals(tempStr)){
					sbSql.append(tempStr.substring(0,tempStr.length()-1)+" AS "+vehicleKind+"合计,\n");
				}		
			}
			if(!"".equals(tempString)){
				sbSql.append(tempString.substring(0,tempString.length()-1)+" AS 总计 \n");
			}	
		}
//		if(sList != null && sList.size()>0){
//			for(int k = 0 ; k < sList.size() ; k++){
//				Map<String, Object> map = sList.get(k);
//				String series_id = map.get("GROUP_ID").toString();
//				String series_name = map.get("GROUP_NAME").toString();
//				String temp = "";
//				if(list != null && list.size()>0){
//					for(int i = 0 ; i < list.size() ; i++){
//						Map<String, Object> map_1 = list.get(i);
//						String seriesId = map_1.get("SERIES_ID").toString();
//						//循环当前车系下的车型
//						if(series_id.equals(seriesId)){
//							Long MODEL_ID = Long.valueOf(map_1.get("MODEL_ID").toString());
//							String MODEL_NAME = (String)map_1.get("MODEL_NAME");
//							sbSql.append("SUM(DECODE(B.MODEL_ID,"+MODEL_ID+",NVL(B.AMOUNT,0),0)) AS "+MODEL_NAME+",\n");
//							temp += "\nSUM(DECODE(B.MODEL_ID,"+MODEL_ID+",NVL(B.AMOUNT,0),0))+" ;
//						}					
//					}
//				    
//				}
//				sbSql.append(temp.substring(0,temp.length()-1)+" AS "+series_name+"小计,\n");
//			}
//		}
		sbSql.append("       FROM  R_ORG_MODEL A, R_ORG_MODEL_C B\n");
		sbSql.append("      WHERE  A.ORG_ID = B.ORG_ID(+)\n");
		sbSql.append("        AND  A.MODEL_ID = B.MODEL_ID(+)\n");
		sbSql.append("      GROUP BY A.ORG_ID,A.ORG_NAME )"); 
		sbSql.append("SELECT * FROM ORG_CENSUS \n");
		sbSql.append("   UNION ALL SELECT -1 ORG_ID,'总计' ORG_NAME,"); 
		//商用车、乘用车循环
		if(kList != null && kList.size()>0){
			String tempString = "";
			for(int j = 0 ; j < kList.size() ; j++){
				Map<String, Object> mapp = kList.get(j);
				String vehicleKind = mapp.get("VEHICLE_KIND").toString();	
				String tempStr = "";
				//车系循环
				if(sList != null && sList.size()>0){
					for(int k = 0 ; k < sList.size() ; k++){
						Map<String, Object> map = sList.get(k);
						String vehicle_kind = map.get("VEHICLE_KIND").toString();
						if(vehicleKind.equals(vehicle_kind)){
							String series_id = map.get("GROUP_ID").toString();
							String series_name = map.get("GROUP_NAME").toString();
							String temp = "";
							//车型循环
							if(list != null && list.size()>0){
								for(int i = 0 ; i < list.size() ; i++){
									Map<String, Object> map_1 = list.get(i);
									String seriesId = map_1.get("SERIES_ID").toString();
									//循环当前车系下的车型
									if(series_id.equals(seriesId)){
										Long MODEL_ID = Long.valueOf(map_1.get("MODEL_ID").toString());
										sbSql.append("SUM(A"+MODEL_ID+") AS A"+MODEL_ID+",\n");
										temp += "\nSUM(DECODE(B.MODEL_ID,"+MODEL_ID+",NVL(B.AMOUNT,0),0))+" ;
										tempStr += "\nSUM(DECODE(B.MODEL_ID,"+MODEL_ID+",NVL(B.AMOUNT,0),0))+";
										tempString += "\nSUM(DECODE(B.MODEL_ID,"+MODEL_ID+",NVL(B.AMOUNT,0),0))+";
									}					
								}
							    
							}
							if(!"".equals(temp)){
								sbSql.append("SUM("+series_name+"小计) AS "+series_name+"小计,\n");
							}
						}				
					}
				}
				if(!"".equals(tempStr)){
					sbSql.append("SUM("+vehicleKind+"合计) AS "+vehicleKind+"小计 ,\n");
				}		
			}
			if(!"".equals(tempString)){
				sbSql.append("SUM(总计) AS 总计  \n");
			}	
		}
		sbSql.append(" FROM ORG_CENSUS \n");
		return pageQuery(sbSql.toString(), null, getFunName());
	}
	
	public List<Map<String, Object>> getModelList(Map<String, Object> param){
		String areaId = (String)param.get("areaId");
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT VMG2.GROUP_ID SERIES_ID, VMG2.GROUP_NAME SERIES_NAME,VMG.GROUP_ID MODEL_ID,VMG.GROUP_CODE MODEL_CODE,VMG.GROUP_NAME MODEL_NAME \n");
		sbSql.append("   FROM TM_VHCL_MATERIAL_GROUP VMG,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP VMG2\n");
		sbSql.append("  WHERE VMG.GROUP_LEVEL = 3\n");
		sbSql.append("    AND VMG.PARENT_GROUP_ID = VMG2.GROUP_ID\n");
		sbSql.append("    AND VMG.STATUS = "+Constant.STATUS_ENABLE+"\n");
		sbSql.append("    AND VMG2.STATUS = "+Constant.STATUS_ENABLE+"\n");
		if(areaId != null && !"".equals(areaId)){
			sbSql.append("    AND VMG2.GROUP_ID IN (SELECT MATERIAL_GROUP_ID FROM TM_AREA_GROUP WHERE AREA_ID = "+areaId+")\n");
		}
		sbSql.append("  ORDER BY VMG2.GROUP_NAME ,VMG.GROUP_NAME DESC"); 
		return pageQuery(sbSql.toString(), null, getFunName());
	}
	
	public List<Map<String, Object>> getSeriesList(Map<String, Object> param){
		String areaId = (String)param.get("areaId");
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT  T.GROUP_ID,T.GROUP_CODE,T.GROUP_NAME , TAG.AREA_ID,\n");
		sbSql.append("             DECODE(TBA.AREA_NAME,'合肥','商用车','乘用车') VEHICLE_KIND\n");
		sbSql.append("       FROM  TM_VHCL_MATERIAL_GROUP T , TM_AREA_GROUP TAG ,TM_BUSINESS_AREA TBA\n");
		sbSql.append("      WHERE  T.GROUP_LEVEL = 2\n");
		sbSql.append("        AND  TAG.MATERIAL_GROUP_ID = T.GROUP_ID\n");
		sbSql.append("        AND  TAG.AREA_ID = TBA.AREA_ID");
		if(areaId != null && !"".equals(areaId)){
			sbSql.append("        AND TAG.AREA_ID = "+areaId+"\n");
		}
		sbSql.append(" AND    T.STATUS = "+Constant.STATUS_ENABLE+"\n"); 
		return pageQuery(sbSql.toString(), null, getFunName());
	}
	
	public List<Map<String, Object>> getVehicleKinds(Map<String, Object> param){
		String areaId = (String)param.get("areaId");
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT VEHICLE_KIND\n");
		sbSql.append("       FROM (SELECT TBA.AREA_ID,\n");
		sbSql.append("               DECODE(TBA.AREA_NAME,'合肥','商用车','乘用车') VEHICLE_KIND\n");
		sbSql.append("         FROM  TM_BUSINESS_AREA TBA WHERE 1 = 1 \n");
		if(areaId != null && !"".equals(areaId)){
			sbSql.append("         AND TBA.AREA_ID = "+areaId+"\n");
		}
		sbSql.append("         AND TBA.STATUS = "+Constant.STATUS_ENABLE+")\n");
		sbSql.append("       GROUP BY VEHICLE_KIND"); 
		return pageQuery(sbSql.toString(), null, getFunName());
	}
}
