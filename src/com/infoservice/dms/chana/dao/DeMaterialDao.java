package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.MaterialVO;

/**
 * @Title: DeMaterialDao.java
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
public class DeMaterialDao extends AbstractIFDao {
	public static Logger logger = Logger.getLogger(DeMaterialDao.class);
	//private static final DeMaterialDao dao = new DeMaterialDao ();
	
	/*public static final DeMaterialDao getInstance() {
		return dao;
	}*/
	
	public static final DeMaterialDao getInstance() {
		return new DeMaterialDao();
	}
	
	public List<MaterialVO> getDeMaterial(TmVhclMaterialPO po){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.MATERIAL_CODE, A.MATERIAL_NAME, B.BRAND_CODE, B.SERIES_CODE,\n" );
		sql.append("       B.MODEL_CODE, B.PACKAGE_CODE,A.COLOR_CODE, A.VHCL_PRICE, A.DISABLE_DATE,\n" );
		sql.append("       A.ISSUE_DATE, A.REMARK, A.STATUS\n" );
		sql.append("FROM TM_VHCL_MATERIAL A, VW_MATERIAL_GROUP B, TM_VHCL_MATERIAL_GROUP_R C\n" );
		sql.append("WHERE A.MATERIAL_ID = C.MATERIAL_ID\n" );
		sql.append("AND B.PACKAGE_ID = C.GROUP_ID\n" );
		//sql.append("AND A.IF_STATUS = ").append(DEConstant.IF_STATUS_0);
		sql.append(" AND A.MATERIAL_ID = ").append(po.getMaterialId());
		List<MaterialVO> list = select(sql.toString(), null);
		return list;
	}
	/**
	 * 
	* @Title: getDeMaterial 
	* @Description: TODO(查询所有的物料信息) 
	* @return List<MaterialVO>    返回类型 
	* @throws
	 */
	public List<MaterialVO> getDeMaterial() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.MATERIAL_CODE, A.MATERIAL_NAME, B.BRAND_CODE, B.SERIES_CODE,\n" );
		sql.append("       B.MODEL_CODE, B.PACKAGE_CODE,A.COLOR_CODE, A.VHCL_PRICE, A.DISABLE_DATE,\n" );
		sql.append("       A.ISSUE_DATE, A.REMARK, A.STATUS\n" );
		sql.append("FROM TM_VHCL_MATERIAL A, VW_MATERIAL_GROUP B, TM_VHCL_MATERIAL_GROUP_R C\n" );
		sql.append("WHERE A.MATERIAL_ID = C.MATERIAL_ID\n" );
		sql.append("AND B.PACKAGE_ID = C.GROUP_ID\n" );
		sql.append("AND A.STATUS = ").append(Constant.STATUS_ENABLE);
		List<MaterialVO> list = select(sql.toString(), null);
		return list;
	}
	
	protected MaterialVO wrapperVO(ResultSet rs, int idx) {
		MaterialVO vo = new MaterialVO();
		try {
			vo.setProductCode(rs.getString("MATERIAL_CODE"));
			vo.setProductName(rs.getString("MATERIAL_NAME"));
			//vo.setProductStatus(rs.getInt("STATUS"));
			vo.setBrandCode(rs.getString("BRAND_CODE"));
			vo.setSeriesCode(rs.getString("SERIES_CODE"));
			vo.setModelCode(rs.getString("MODEL_CODE"));
			vo.setConfigCode(rs.getString("PACKAGE_CODE"));
			vo.setColorCode(rs.getString("COLOR_CODE"));
			vo.setOemDirectivePrice(rs.getDouble("VHCL_PRICE"));
			vo.setExeuntDate(rs.getDate("DISABLE_DATE"));
			vo.setEnterDate(rs.getDate("ISSUE_DATE"));
			vo.setRemark(rs.getString("REMARK"));
			vo.setDownTimestamp(downTimestamp);
			vo.setIsValid(rs.getInt("STATUS"));
//			if(rs.getInt("STATUS") == Constant.STATUS_ENABLE){
//				vo.setIsValid(Constant.STATUS_ENABLE);//数据有效
//			}else{
//				vo.setIsValid(Constant.STATUS_DISABLE);//数据无效
//			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
}
