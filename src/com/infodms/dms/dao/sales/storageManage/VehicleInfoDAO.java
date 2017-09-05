package com.infodms.dms.dao.sales.storageManage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;

public class VehicleInfoDAO extends BaseDao{
	public static Logger logger = Logger.getLogger(VehicleInfoDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO ();
	public static final CheckVehicleDAO getInstance() {
		return dao;
	}
	
	/***
	 * 车辆属性查询
	 * @param : 
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Object> getVehicleInfo(String vin){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMV.VIN,                          --VIN \n");
		sql.append("       TMV.ENGINE_NO,                    --发动机号 \n");
		sql.append("       G1.GROUP_NAME SERIES_NAME,        --车系 \n");
		sql.append("       G2.GROUP_NAME MODEL_NAME,         --车型 \n");
		sql.append("       TMVM.MATERIAL_CODE,               --物料代码 \n");
		sql.append("       TMVM.MATERIAL_NAME,               --物料名称 \n");
		sql.append("       TMV.PRODUCT_DATE,                        --生产日期 \n");
		sql.append("       TMV.FACTORY_DATE,                        --出产日期 \n");
		sql.append("       TMV.BODY_COLOR,                         --车身颜色 \n");
		sql.append("       TMV.COLOR                         --颜色 \n");
		sql.append("  FROM TM_VEHICLE TMV, \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1, \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G2, \n");
		sql.append("       TM_VHCL_MATERIAL TMVM \n");
		sql.append(" WHERE     TMV.SERIES_ID = G1.GROUP_ID \n");
		sql.append("       AND TMV.MODEL_ID = G2.GROUP_ID \n");
		sql.append("       AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID \n");
		sql.append("       AND TMV.VIN = '"+vin+"' \n");
		
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	
	/***
	 * 库存状态变更日志
	 * @param : 
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String,Object>> getStorageChangeList(String vin){
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT TTVC.CHANGE_NAME, --改变名称\n");
		sql.append("       DECODE(CHANGE_NAME, " + Constant.STORAGE_CHANGE_TYPE_06 + ", TCU.NAME, " +Constant.STORAGE_CHANGE_TYPE_07+ ", TCU.NAME) CHECK_NAME, --Constant.STORAGE_CHANGE_TYPE_06  调拨审批通过\n");  
		sql.append("       TTVC.CHANGE_DESC, --改变描述\n");  
		sql.append("       TTVC.DOC_NO, --相关单据号\n");  
		sql.append("       TO_CHAR(TTVC.CHANGE_DATE, 'yyyy-MM-dd hh24:mi') CHANGE_DATE, --节点更新时间\n");  
		sql.append("       TMD.DEALER_SHORTNAME,\n");  
		sql.append("       TMW.WAREHOUSE_NAME\n");  
		sql.append("  FROM TM_VEHICLE      TMV,\n");  
		sql.append("       TT_VS_VHCL_CHNG TTVC,\n");  
		sql.append("       TM_DEALER       TMD,\n");  
		sql.append("       TC_USER         TCU,\n");  
		sql.append("       TM_WAREHOUSE    TMW\n");  
		sql.append(" WHERE TMV.VEHICLE_ID = TTVC.VEHICLE_ID\n");  
		sql.append("   AND TCU.USER_ID = TTVC.CREATE_BY\n");  
		sql.append("   AND TMV.VIN = '" + vin + "'\n");  
		sql.append("   AND TTVC.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID\n");  
		sql.append("   AND TTVC.CHANGE_CODE = " + Constant.STORAGE_CHANGE_TYPE + "\n");  
		sql.append(" ORDER BY TTVC.CHANGE_DATE DESC\n");

		return dao.pageQuery(sql.toString(), null,dao.getFunName() );
	}
	/***
	 * 车辆销售状态日志
	 * @param : 
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String,Object>> getSalesList(String vin){
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT TTVC.CHANGE_NAME,                                              --改变名称 \n");
		sql.append("        TO_CHAR (TTVC.CHANGE_DATE, 'yyyy-MM-dd hh24:mi') CHANGE_DATE,  --改变时间 \n");
		sql.append("        TTVC.CHANGE_DESC,                                              --改变描述 \n");
		sql.append("        TTVC.DOC_NO                                                   --相关单据号 \n");
		sql.append("    FROM TM_VEHICLE TMV, TT_VS_VHCL_CHNG TTVC \n");
		sql.append("   WHERE     TMV.VEHICLE_ID = TTVC.VEHICLE_ID \n");
		sql.append("         AND TMV.VIN = '"+vin+"' \n");
		sql.append("         AND TTVC.CHANGE_CODE = "+Constant.SALES_STATUS_CHANGE_TYPE+" \n");
		sql.append("ORDER BY TTVC.CHANGE_DATE DESC\n");

		return dao.pageQuery(sql.toString(), null,dao.getFunName() );
	}
	
	/***
	 * 取得销售车辆ERP销售订单号 送车交接单号 DMS订单号
	 * @param : 
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Object> getVehicleSalesOrderNo(String vin){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT C.ORDER_NUMBER,C.SENDCAR_ORDER_NUMBER,D.ERP_ORDER, D.DELIVERY_NO\n");
		sql.append("  FROM TM_VEHICLE      A,\n");
		sql.append("       TT_VS_DLVRY_MCH B,\n");
		sql.append("       TT_VS_DLVRY_ERP C,\n");
		sql.append("       TT_VS_DLVRY     D,\n");
		sql.append("       TT_VS_DLVRY_DTL E\n");
		sql.append(" WHERE A.VEHICLE_ID = B.VEHICLE_ID\n");
		sql.append("   AND B.ERP_SENDCAR_ID = C.SENDCAR_HEADER_ID\n");
		sql.append("   AND B.DELIVERY_DETAIL_ID = E.DETAIL_ID\n");
		sql.append("   AND E.DELIVERY_ID = D.DELIVERY_ID\n");
		sql.append("   AND A.VIN = '"+vin+"'\n");
		sql.append("   AND A.LIFE_CYCLE IN ( '"+Constant.VEHICLE_LIFE_03+"','"+Constant.VEHICLE_LIFE_04+"','"+Constant.VEHICLE_LIFE_05+"')");


		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	/***
	 * 取得销售车辆ERP销售订单号 送车交接单号 DMS订单号
	 * @param : 
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Object> getVehicleStatusSalesOrderNo(String vin){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT DISTINCT TVDE.ORDER_NUMBER,\n" );
		sql.append("                TVDE.SENDCAR_ORDER_NUMBER,\n" );
		sql.append("                TVD.ERP_ORDER,\n" );
		sql.append("                TVD.DELIVERY_NO\n" );
		sql.append("  FROM TT_VS_DLVRY_ERP     TVDE,\n" );
		sql.append("       TT_VS_DLVRY_ERP_DTL TVDED,\n" );
		sql.append("       TT_VS_DLVRY_REQ     TVDR,\n" );
		sql.append("       TT_VS_DLVRY_REQ_DTL TVDRD,\n" );
		sql.append("       TM_DEALER           TD,\n" );
		sql.append("       TM_VEHICLE          TV,\n" );
		sql.append("       TT_VS_DLVRY         TVD\n" );
		sql.append(" WHERE TVDE.SENDCAR_HEADER_ID = TVDED.SENDCAR_HEADER_ID\n" );
		sql.append("   AND TVDED.REQ_ID = TVDR.REQ_ID\n" );
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n" );
		sql.append("   AND TD.DEALER_ID = TVDR.RECEIVER\n" );
		sql.append("   AND TV.VEHICLE_ID = TVDED.VEHICLE_ID\n" );
		sql.append("   AND TVD.REQ_ID(+) = TVDR.REQ_ID");
		sql.append("   AND TV.VIN = '"+vin+"'\n");
		sql.append("   AND TV.LIFE_CYCLE IN ( '"+Constant.VEHICLE_LIFE_03+"','"+Constant.VEHICLE_LIFE_04+"','"+Constant.VEHICLE_LIFE_05+"')");


		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	
	/**
	 * 判断当前车辆的业务范围与车辆所有者的业务范围是否一致，若一致则返回查询记录条数，否则返回0
	 * @param vId 车辆id
	 * @return Integer:返回记录条数
	 */
	public static Integer chkArea(String vId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TMV.VEHICLE_ID,\n");
		sql.append("       TMV.DEALER_ID,\n");  
		sql.append("       TMV.AREA_ID,\n");  
		sql.append("       tba1.produce_base,\n");  
		sql.append("       tba2.produce_base\n");  
		sql.append("  FROM TM_VEHICLE              TMV,\n");  
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");  
		sql.append("       tm_business_area        tba1,\n");  
		sql.append("       tm_business_area        tba2\n");  
		sql.append(" WHERE TMV.DEALER_ID = TDBA.DEALER_ID\n");  
		sql.append("   AND TMV.AREA_ID = tba1.area_id\n");  
		sql.append("   and tdba.area_id = tba2.area_id\n");  
		sql.append("   and tba1.produce_base = tba2.produce_base\n");

		sql.append("   AND TMV.VEHICLE_ID = ").append(vId).append("\n");
		
		List<Map<String, Object>> vList = dao.pageQuery(sql.toString(), null,dao.getFunName() );
		
		if (CommonUtils.isNullList(vList)) {
			return 0 ;
		}
		
		return vList.size() ;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
