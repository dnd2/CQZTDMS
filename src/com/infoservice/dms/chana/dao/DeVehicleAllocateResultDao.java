package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.VehicleAllocateResultVO;

/**
 * @Title: DeVehicleAllocateResultDao.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-27
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class DeVehicleAllocateResultDao extends AbstractIFDao {
	public static Logger logger = Logger.getLogger(DeVehicleAllocateResultDao.class);
	private static final DeVehicleAllocateResultDao dao = new DeVehicleAllocateResultDao ();
	
	public static final DeVehicleAllocateResultDao getInstance() {
		return dao;
	}
	
	DeCommonDao comDao = new DeCommonDao();
	
	public List<VehicleAllocateResultVO> getDeVehicleAllocateResult(){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.TRANSFER_ID, D.DEALER_CODE OUT_CODE, D.DEALER_NAME OUT_NAME,\n" );
		sql.append("       E.DEALER_CODE IN_CODE, E.DEALER_NAME IN_NAME, F.MATERIAL_CODE,\n" );
		sql.append("       C.VIN, C.ENGINE_NO, C.PRODUCT_DATE, C.FACTORY_DATE, F.VHCL_PRICE\n" );
		sql.append("FROM TT_VS_VEHICLE_TRANSFER_CHK A, TT_VS_VEHICLE_TRANSFER B,\n" );
		sql.append("     TM_VEHICLE C, TM_DEALER D, TM_DEALER E, TM_VHCL_MATERIAL F\n" );
		sql.append("WHERE A.TRANSFER_ID = B.TRANSFER_ID\n" );
		sql.append("AND B.VEHICLE_ID = C.VEHICLE_ID\n" );
		sql.append("AND B.OUT_DEALER_ID = D.DEALER_ID\n" );
		sql.append("AND B.IN_DEALER_ID = E.DEALER_ID\n" );
		sql.append("AND C.MATERIAL_ID = F.MATERIAL_ID\n");
		sql.append("AND A.IF_STATUS = ").append(DEConstant.IF_STATUS_0).append("\n");
		
		List<VehicleAllocateResultVO> list = select(sql.toString(), null);
		return list;
	}
	
	protected VehicleAllocateResultVO wrapperVO(ResultSet rs, int idx) {
		VehicleAllocateResultVO vo = new VehicleAllocateResultVO();
		try {
			vo.setAllocateOutNo(rs.getString("TRANSFER_ID"));
			try {
				Map<String, Object> map = comDao.getDmsDealerCode(rs.getString("OUT_CODE"));
				vo.setAllocateOutEntityCode(String.valueOf(map.get("DMS_CODE")));
				vo.setAllocateOutEntityName(String.valueOf(map.get("COMPANY_SHORTNAME")));
				Map<String, Object> inmap = comDao.getDmsDealerCode(rs.getString("IN_CODE"));
				vo.setAllocateInEntityCode(String.valueOf(inmap.get("DMS_CODE")));
				vo.setAllocateInEntityName(String.valueOf(inmap.get("COMPANY_SHORTNAME")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			vo.setProductCode(rs.getString("MATERIAL_CODE"));
			vo.setVin(rs.getString("VIN"));
			vo.setEngineNo(rs.getString("ENGINE_NO"));
			vo.setManufactureDate(rs.getDate("PRODUCT_DATE"));
			vo.setFactoryDate(rs.getDate("FACTORY_DATE"));
			vo.setVehiclePrice(rs.getDouble("VHCL_PRICE"));
			vo.setRemark(rs.getString("TRANSFER_ID"));
			vo.setDownTimestamp(downTimestamp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
}
