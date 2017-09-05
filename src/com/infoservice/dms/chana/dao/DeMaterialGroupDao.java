package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.MaterialGroupVO;


/**
 * @Title: DeMaterialGroupDao.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-23
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class DeMaterialGroupDao extends AbstractIFDao {
	public static Logger logger = Logger.getLogger(DeMaterialGroupDao.class);
	private static final DeMaterialGroupDao dao = new DeMaterialGroupDao ();
	public static final DeMaterialGroupDao getInstance() {
		return dao;
	}
	
	public List<MaterialGroupVO> getMaterialGroup(TmVhclMaterialGroupPO po){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT GROUP_CODE, GROUP_NAME, GROUP_LEVEL\n" );
		sql.append("FROM TM_VHCL_MATERIAL_GROUP\n" );
		sql.append("START WITH GROUP_ID = ").append(po.getParentGroupId());
		sql.append("CONNECT BY PRIOR PARENT_GROUP_ID = GROUP_ID\n" );
		sql.append("ORDER BY GROUP_LEVEL DESC\n");

		List<MaterialGroupVO> list = select(sql.toString(), null, 1);
		return list;
	}
	/**
	 * 
	* @Title: getMaterialGroup 
	* @Description: TODO(查询全部物料组) 
	* @return List<MaterialGroupVO>    返回类型 
	* @throws
	 */
	public List<MaterialGroupVO> getMaterialGroup(){
		downTimestamp = new Date();//下发时间
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT G1.GROUP_CODE BRAND_CODE,\n" );
		sql.append("       G1.GROUP_NAME BRAND_NAME,\n" );
		sql.append("       NULL          SERIES_CODE,\n" );
		sql.append("       NULL          SERIES_NAME,\n" );
		sql.append("       NULL          MODEL_CODE,\n" );
		sql.append("       NULL          MODEL_NAME,\n" );
		sql.append("       NULL          PACKAGE_CODE,\n" );
		sql.append("       NULL          PACKAGE_NAME, G1.STATUS\n" );
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP G1\n" );
		//sql.append(" WHERE G1.STATUS = " ).append(Constant.STATUS_ENABLE).append("\n");
		sql.append("   WHERE G1.GROUP_LEVEL = 1\n" );
		sql.append("UNION ALL\n" );
		sql.append("SELECT G1.GROUP_CODE BRAND_CODE,\n" );
		sql.append("       G1.GROUP_NAME BRAND_NAME,\n" );
		sql.append("       G2.GROUP_CODE SERIES_CODE,\n" );
		sql.append("       G2.GROUP_NAME SERIES_NAME,\n" );
		sql.append("       NULL          MODEL_CODE,\n" );
		sql.append("       NULL          MODEL_NAME,\n" );
		sql.append("       NULL          PACKAGE_CODE,\n" );
		sql.append("       NULL          PACKAGE_NAME, G1.STATUS\n" );
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP G1, TM_VHCL_MATERIAL_GROUP G2\n" );
		sql.append(" WHERE G1.GROUP_ID = G2.PARENT_GROUP_ID\n" );
//		sql.append("   AND G1.STATUS = " ).append(Constant.STATUS_ENABLE).append("\n");
//		sql.append("   AND G2.STATUS = " ).append(Constant.STATUS_ENABLE).append("\n");
		sql.append("   AND G1.GROUP_LEVEL = 1\n" );
		sql.append("   AND G2.GROUP_LEVEL = 2\n" );
		sql.append("UNION ALL\n" );
		sql.append("SELECT G1.GROUP_CODE BRAND_CODE,\n" );
		sql.append("       G1.GROUP_NAME BRAND_NAME,\n" );
		sql.append("       G2.GROUP_CODE SERIES_CODE,\n" );
		sql.append("       G2.GROUP_NAME SERIES_NAME,\n" );
		sql.append("       G3.GROUP_CODE MODEL_CODE,\n" );
		sql.append("       G3.GROUP_NAME MODEL_NAME,\n" );
		sql.append("       NULL          PACKAGE_CODE,\n" );
		sql.append("       NULL          PACKAGE_NAME, G1.STATUS\n" );
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP G1,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP G2,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP G3\n" );
		sql.append(" WHERE G1.GROUP_ID = G2.PARENT_GROUP_ID\n" );
		sql.append("   AND G2.GROUP_ID = G3.PARENT_GROUP_ID\n" );
//		sql.append("   AND G1.STATUS = " ).append(Constant.STATUS_ENABLE).append("\n");
//		sql.append("   AND G2.STATUS = " ).append(Constant.STATUS_ENABLE).append("\n");
//		sql.append("   AND G3.STATUS = " ).append(Constant.STATUS_ENABLE).append("\n");
		sql.append("   AND G1.GROUP_LEVEL = 1\n" );
		sql.append("   AND G2.GROUP_LEVEL = 2\n" );
		sql.append("   AND G3.GROUP_LEVEL = 3\n" );
		sql.append("UNION ALL\n" );
		sql.append("SELECT G1.GROUP_CODE BRAND_CODE,\n" );
		sql.append("       G1.GROUP_NAME BRAND_NAME,\n" );
		sql.append("       G2.GROUP_CODE SERIES_CODE,\n" );
		sql.append("       G2.GROUP_NAME SERIES_NAME,\n" );
		sql.append("       G3.GROUP_CODE MODEL_CODE,\n" );
		sql.append("       G3.GROUP_NAME MODEL_NAME,\n" );
		sql.append("       G4.GROUP_CODE PACKAGE_CODE,\n" );
		sql.append("       G4.GROUP_NAME PACKAGE_NAME, G1.STATUS\n" );
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP G1,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP G2,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP G3,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP G4\n" );
		sql.append(" WHERE G1.GROUP_ID = G2.PARENT_GROUP_ID\n" );
		sql.append("   AND G2.GROUP_ID = G3.PARENT_GROUP_ID\n" );
		sql.append("   AND G3.GROUP_ID = G4.PARENT_GROUP_ID\n" );
//		sql.append("   AND G1.STATUS = " ).append(Constant.STATUS_ENABLE).append("\n");
//		sql.append("   AND G2.STATUS = " ).append(Constant.STATUS_ENABLE).append("\n");
//		sql.append("   AND G3.STATUS = " ).append(Constant.STATUS_ENABLE).append("\n");
//		sql.append("   AND G4.STATUS = " ).append(Constant.STATUS_ENABLE).append("\n");
		sql.append("   AND G1.GROUP_LEVEL = 1\n" );
		sql.append("   AND G2.GROUP_LEVEL = 2\n" );
		sql.append("   AND G3.GROUP_LEVEL = 3\n" );
		sql.append("   AND G4.GROUP_LEVEL = 4");
		List<MaterialGroupVO> list = select(sql.toString(), null, 2);
		return list;
	}
	
	protected BaseVO wrapperVO(ResultSet rs, int idx, int flag) {
		switch (flag) {
		//查询单个物料组
		case 1 :
			return wrapperSingleVO(rs, idx);
		//查询所有物料组
		case 2 : 
			return wrapperAllVO(rs, idx);
		}
		return null;
	}
	/**
	 * 
	* @Title: wrapperSingleVO 
	* @Description: TODO(单个物料组) 
	* @return MaterialGroupVO    返回类型 
	* @throws
	 */
	protected MaterialGroupVO wrapperSingleVO(ResultSet rs, int idx) {
		MaterialGroupVO vo = new MaterialGroupVO();
		try {
			vo.setBrandCode(rs.getString("GROUP_CODE"));
			vo.setBrandName(rs.getString("GROUP_NAME"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	/**
	 * 
	* @Title: wrapperAllVO 
	* @Description: TODO(组装所有的物料组 包括品牌 车型 车系 配置) 
	* @return MaterialGroupVO    返回类型 
	* @throws
	 */
	protected MaterialGroupVO wrapperAllVO(ResultSet rs, int idx) {
		MaterialGroupVO vo = new MaterialGroupVO();
		try {
			vo.setBrandCode(rs.getString("BRAND_CODE"));
			vo.setBrandName(rs.getString("BRAND_NAME"));
			vo.setSeriesCode(rs.getString("SERIES_CODE"));
			vo.setSeriesName(rs.getString("SERIES_NAME"));
			vo.setModelCode(rs.getString("MODEL_CODE"));
			vo.setModelName(rs.getString("MODEL_NAME"));
			vo.setConfigCode(rs.getString("PACKAGE_CODE"));
			vo.setConfigName(rs.getString("PACKAGE_NAME"));
			vo.setDownTimestamp(downTimestamp);
			vo.setIsValid(rs.getInt("STATUS"));
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
}
