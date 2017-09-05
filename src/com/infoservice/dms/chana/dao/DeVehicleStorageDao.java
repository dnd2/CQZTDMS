package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.common.Constant;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.VehicleStorageVO;

/**
 * @Title: DeVehicleShippingDao.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-26
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class DeVehicleStorageDao extends AbstractIFDao implements DEConstant {
	public static Logger logger = Logger.getLogger(DeVehicleStorageDao.class);
	private static final DeVehicleStorageDao dao = new DeVehicleStorageDao ();
	private static String stockInType;//入库方式
	private static String remark;
	private static Map<String, Object> outDealer;//供应商（批发调拨入库专用）
	public static final DeVehicleStorageDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: getDeVehicleShipping 
	* @Description: TODO(查询订单发运接口表未下发的数据) 
	* @param @return    设定文件 
	* @return List<VehicleShippingVO>    返回类型 
	* @throws
	 */
	/*public List<VehicleStorageVO> getDeVehicleShipping() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DISTINCT(B.DELIVERY_NO), B.DELIVERY_DATE, B.DELIVERY_TYPE, B.RECEIVER, \n");
		sql.append("     C.ORDER_NO, D.SENDCAR_ORDER_NUMBER, E.ADDRESS, F.DEALER_CODE \n");
		//sql.append("FROM TT_VS_DLVRY_RPC A, TM_VEHICLE B, TT_VS_DLVRY C, TT_VS_ORDER D,\n");
		sql.append("FROM TT_VS_DLVRY_RPC A, TT_VS_DLVRY B, TT_VS_ORDER C, Tt_Vs_Dlvry_Erp D,\n");
		sql.append("     TM_VS_ADDRESS E, TM_DEALER F \n");
		sql.append("WHERE A.IF_STATUS = ").append(DEConstant.IF_STATUS_0).append("\n");
		//sql.append("  AND A.VIN = B.VIN \n");
		sql.append("  AND A.DELIVERY_NO = B.DELIVERY_NO \n");
		sql.append("  AND B.ORDER_ID = C.ORDER_ID \n");
		sql.append("  AND B.DELIVERY_ID = D.DELIVERY_ID \n");
		sql.append("  AND B.ADDRESS_ID = E.ID \n");
		sql.append("  AND B.RECEIVER = F.DEALER_ID AND B.DELIVERY_NO='BDJBC0091912011403'");
		List<VehicleStorageVO> list = select(sql.toString(), null, VEH_SHIP);
		return list;
	}*/
	
	public List<VehicleStorageVO> getDeVehicleSorage(List<String> vins,String stockInType,String remark,Map<String, Object> outDealer) {
		this.stockInType = stockInType;
		this.remark = remark;
		this.outDealer = outDealer;
		StringBuffer sql = new StringBuffer();
		sql.append("WITH X AS (SELECT MAX(TVVO.DLVRY_ID) DLVRY_ID, TVVO.VIN FROM TT_VS_VHLE_ORDER TVVO \n");
		sql.append("WHERE TVVO.VIN IN ('");
		int i =1;
		for(String vin:vins){
			if(i==vins.size()){
				sql.append(vin).append("')");
			}else{
				sql.append(vin).append("','");
			}
			i++;
		}
		sql.append(" GROUP BY TVVO.VIN) \n");
		sql.append("SELECT A.SENDCAR_NUMBER,\n");
		sql.append("       A.ORDER_NO,\n");  
		sql.append("       A.DLVRY_NO,\n");  
		sql.append("       A.DELIVERYDATE,\n");  
		sql.append("       A.DELIVERY_TYPE,\n");  
		sql.append("       A.SHIP_METHOD,\n");  
		sql.append("       A.FLATCAR_ID,\n");  
		sql.append("       A.MOTORMAN,\n");  
		sql.append("       A.MOTORMANPHONE,\n");  
		sql.append("       A.VIN,\n");  
		sql.append("       A.SINGLE_PRICE,\n");  
		sql.append("       B.ENGINE_NO,\n");  
		sql.append("       B.HEGEZHENG_CODE,\n");  
		sql.append("       B.PRODUCT_DATE,\n");  
		sql.append("       B.FACTORY_DATE,\n");  
		sql.append("       C.MATERIAL_CODE,\n");  
		sql.append("       D.CODE_DESC,\n");  
		sql.append("       E.REQ_DATE,\n");  
		sql.append("       F.ADDRESS,\n");  
		sql.append("       G.DEALER_CODE\n");  
		sql.append("  FROM TT_VS_VHLE_ORDER A,\n");  
		sql.append("       TM_VEHICLE       B,\n");  
		sql.append("       TM_VHCL_MATERIAL C,\n");  
		sql.append("       TC_CODE          D,\n");  
		sql.append("       TT_VS_DLVRY_REQ  E,\n");  
		sql.append("       TM_VS_ADDRESS    F,\n");  
		sql.append("       TM_DEALER G,\n");  
		sql.append("       X\n");  
		sql.append(" WHERE A.VIN = B.VIN\n");  
		sql.append("   AND A.REQ_ID = E.REQ_ID\n");  
		sql.append("   AND B.MATERIAL_ID = C.MATERIAL_ID\n");  
		sql.append("   AND B.YIELDLY = D.CODE_ID\n");  
		sql.append("   AND E.ADDRESS_ID = F.ID\n");  
		sql.append("   AND A.ORDER_DEALER_ID = G.DEALER_ID\n");  
		sql.append("   AND A.DLVRY_ID = X.DLVRY_ID\n");
		sql.append("   AND A.VIN = X.VIN");
		System.out.print(sql.toString());
		List<VehicleStorageVO> list = select(sql.toString(), null);
		return list;
	}
	
	public String getDealerCode(Long dealerId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.DEALER_CODE FROM TM_DEALER A WHERE A.DEALER_ID =\n");
		sql.append(dealerId);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return (String)map.get("DEALER_CODE");
	}
	/**
	 * 
	* @Title: getVehicleList 
	* @Description: TODO(货运单号去接口表查询VIN 详细信息) 
	* @param @param deNo 货运单号
	* @param @return    设定文件 
	* @return List<BaseVO>    返回类型 
	* @throws
	 */
/*	public List<VehicleStorageVO> getVehicleList(String deNo) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.VIN, A.ENGINE_NO, A.FACTORY_DATE, A.PRODUCT_DATE,\n" );
		sql.append("       B.MATERIAL_CODE, B.VHCL_PRICE, D.CODE_ID, D.CODE_DESC\n" );
		sql.append(" FROM TM_VEHICLE A, TM_VHCL_MATERIAL B, TT_VS_DLVRY_RPC C, TC_CODE D\n" );
		sql.append("WHERE C.DELIVERY_NO = '").append(deNo).append("'");
		sql.append("  AND A.VIN = C.VIN\n");
		sql.append("  AND A.MATERIAL_ID = B.MATERIAL_ID\n");
		sql.append("  AND A.YIELDLY = D.CODE_ID\n");
		List<VehicleStorageVO> list = select(sql.toString(), null, VEH_SHIP_DETAIL);
		return list;
	}*/
	
	protected BaseVO wrapperVO(ResultSet rs, int idx) {
		VehicleStorageVO vo = new VehicleStorageVO();
		try {
			vo.setDownTimestamp(downTimestamp);
			vo.setIsValid(Constant.STATUS_ENABLE);
			vo.setSendCarNo(rs.getString("SENDCAR_NUMBER"));
			vo.setOrderNo(rs.getString("ORDER_NO"));
			vo.setDeliveryNo(rs.getString("DLVRY_NO"));
			vo.setPurchaseDate(rs.getDate("REQ_DATE"));
			vo.setShippingDate(rs.getDate("DELIVERYDATE"));
			vo.setDeliveryType(rs.getString("DELIVERY_TYPE"));
			vo.setShipperName(rs.getString("SHIP_METHOD"));
			vo.setShipperLicense(rs.getString("FLATCAR_ID"));
			vo.setDeliverymanName(rs.getString("MOTORMAN"));
			vo.setDeliverymanPhone(rs.getString("MOTORMANPHONE"));
			vo.setShippingAddress(rs.getString("ADDRESS"));
			vo.setStockInType(stockInType);
			vo.setVin(rs.getString("VIN"));
			vo.setProductCode(rs.getString("MATERIAL_CODE"));
			vo.setEngineNo(rs.getString("ENGINE_NO"));
			vo.setCertificateNumber(rs.getString("HEGEZHENG_CODE"));
			vo.setManufactureDate(rs.getDate("PRODUCT_DATE"));
			vo.setFactoryDate(rs.getDate("FACTORY_DATE"));
			vo.setVehiclePrice(rs.getDouble("SINGLE_PRICE"));
			vo.setProductingAreaName(rs.getString("CODE_DESC"));
			vo.setEntityCode(rs.getString("DEALER_CODE"));
			if(outDealer!=null){
				vo.setVendorCode((String)outDealer.get("DEALER_CODE"));
				vo.setVendorName((String)outDealer.get("DEALER_NAME"));
			}
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
	public Map<String, Object> getDealer(Long dealerId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.DEALER_CODE,A.DEALER_NAME FROM TM_DEALER A WHERE A.DEALER_ID =\n");
		sql.append(dealerId);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}

	/*protected BaseVO wrapperVO(ResultSet rs, int idx, int flag) {
		switch(flag) {
		case VEH_SHIP : 
			return wrapperVehicleShippingVO(rs, idx);
		case VEH_SHIP_DETAIL : 
			return wrapperVehicleShippingDetailVO(rs, idx);
		}
		return null;
	}*/
	
	/*private VehicleStorageVO wrapperVehicleShippingVO(ResultSet rs, int idx) {
		VehicleStorageVO vo = new VehicleStorageVO();
		try {
			vo.setShippingOrderNo(rs.getString("SENDCAR_ORDER_NUMBER"));//送车交接单号
			vo.setPoNo(rs.getString("ORDER_NO"));//采购单号
			vo.setShippingDate(rs.getDate("DELIVERY_DATE"));
			vo.setDeliveryType(rs.getString("DELIVERY_TYPE"));
			vo.setShippingAddress(rs.getString("ADDRESS"));
			vo.setEntityCode(rs.getString("DEALER_CODE"));
			vo.setDownTimestamp(downTimestamp);
			vo.setIsValid(Constant.STATUS_ENABLE);
			vo.setRemark(rs.getString("DELIVERY_NO"));//发运单号
			vo.setVehicleVoList(DEUtil.transType(getVehicleList(rs.getString("DELIVERY_NO")))); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
	private VehicleShippingDetailVO wrapperVehicleShippingDetailVO(ResultSet rs, int idx) {
		VehicleShippingDetailVO vo = new VehicleShippingDetailVO();
		try {
			vo.setVin(rs.getString("VIN"));
			vo.setProductCode(rs.getString("MATERIAL_CODE"));
			vo.setEngineNo(rs.getString("ENGINE_NO"));
			vo.setManufactureDate(rs.getDate("PRODUCT_DATE"));
			vo.setFactoryDate(rs.getDate("FACTORY_DATE"));
			vo.setVehiclePrice(rs.getDouble("VHCL_PRICE"));
			vo.setProductingAreaCode(String.valueOf(rs.getInt("CODE_ID")));
			vo.setProductingAreaName(rs.getString("CODE_DESC"));
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}*/
	
}
