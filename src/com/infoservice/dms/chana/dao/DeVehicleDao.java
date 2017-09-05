package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.VehicleVO;

/**
 * @Title: DeVehicleDao.java
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
public class DeVehicleDao extends AbstractIFDao {
	public static Logger logger = Logger.getLogger(DeVehicleDao.class);
	private static final DeVehicleDao dao = new DeVehicleDao ();
	
	public static final DeVehicleDao getInstance() {
		return dao;
	}
	
	public List<VehicleVO> getDeVehicle(VehicleVO vo){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.VIN, A.LICENSE_NO, A.ENGINE_NO, A.GEARBOX_NO,\n" );
		sql.append("       A.COLOR, B.BRAND_CODE, B.SERIES_CODE, B.MODEL_CODE,\n" );
		sql.append("       B.PACKAGE_CODE, A.MODEL_YEAR, A.PRODUCT_DATE,\n" );
		sql.append("       A.YIELDLY, A.HISTORY_MILE \n" );
		sql.append("FROM TM_VEHICLE A, VW_MATERIAL_GROUP B, TM_VHCL_MATERIAL_GROUP_R C\n" );
		sql.append("WHERE A.MATERIAL_ID = C.MATERIAL_ID\n" );
		sql.append("AND C.GROUP_ID = B.PACKAGE_ID\n");
		sql.append("AND A.VIN = '").append(vo.getVin()).append("'\n");

		List<VehicleVO> list = select(sql.toString(), null);
		return list;
	}
	
	protected VehicleVO wrapperVO(ResultSet rs, int idx) {
		VehicleVO vo = new VehicleVO();
		try {
			vo.setVin(rs.getString("VIN"));
			vo.setLicense(rs.getString("LICENSE_NO"));
			vo.setEngineNo(rs.getString("ENGINE_NO"));
			vo.setGearBox(rs.getString("GEARBOX_NO"));
			vo.setBrand(rs.getString("BRAND_CODE"));
			vo.setSeries(rs.getString("SERIES_CODE"));
			vo.setModel(rs.getString("MODEL_CODE"));
			vo.setColor(rs.getString("COLOR"));
			vo.setApackage(rs.getString("PACKAGE_CODE"));
			vo.setModelYear(rs.getString("MODEL_YEAR"));
			vo.setProductDate(rs.getDate("PRODUCT_DATE"));
			vo.setYieldly(rs.getString("YIELDLY"));
			vo.setMileage(rs.getDouble("HISTORY_MILE"));
			vo.setDownTimestamp(downTimestamp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
}
