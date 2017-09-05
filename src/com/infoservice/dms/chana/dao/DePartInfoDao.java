package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.PartInfoVO;

/**
 * @Title: DePartInfoDao.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-28
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class DePartInfoDao extends AbstractIFDao {
	public static Logger logger = Logger.getLogger(DePartInfoDao.class);
	private static final DePartInfoDao dao = new DePartInfoDao ();
	
	public static final DePartInfoDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: getPartInfo 
	* @Description: TODO(定时任务下发配件,只查询未下发的配件) 
	* @param @return    设定文件 
	* @return List<PartInfoVO>    返回类型 
	* @throws
	 */
	public List<PartInfoVO> getPartInfo(){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.PART_CODE, A.PART_NAME, A.PART_TYPE, A.UNIT,\n" );
		sql.append("       A.CHANGE_CODE, B.PART_CODE REPLACE_PART_CODE,\n" );
		sql.append("       A.CAR_AMOUNT, A.STOCK_PRICE, A.CLAIM_PRICE,\n" );
		sql.append("       A.REMARK, A.STOP_FLAG, A.MINI_PACK, A.CUSTOMER_PRICE\n" );
		sql.append("  FROM TM_PT_PART_BASE A, TM_PT_PART_BASE B\n" );
		sql.append(" WHERE ROWNUM <= 1000 ");
		sql.append("  AND A.REPLACE_PART_ID = B.PART_ID(+)\n");
		sql.append("  AND A.IF_STATUS = ").append(DEConstant.IF_STATUS_0).append("\n");
		List<PartInfoVO> list = select(sql.toString(), null);
		return list;
	}
	
	/**
	 * 
	* @Title: getPartInfoAll 
	* @Description: TODO(查询配件基础信息表,用做全量下发,每次都发全部的数据) 
	* @return List<PartInfoVO>    返回类型 
	* @throws
	 */
	public List<PartInfoVO> getPartInfoAll(){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.PART_CODE, A.PART_NAME, A.PART_TYPE, A.UNIT,\n" );
		sql.append("       A.CHANGE_CODE, B.PART_CODE REPLACE_PART_CODE,\n" );
		sql.append("       A.CAR_AMOUNT, A.STOCK_PRICE, A.CLAIM_PRICE,\n" );
		sql.append("       A.REMARK, A.STOP_FLAG, A.MINI_PACK, A.CUSTOMER_PRICE\n" );
		sql.append(" FROM TM_PT_PART_BASE A, TM_PT_PART_BASE B\n" );
		sql.append("WHERE A.REPLACE_PART_ID = B.PART_ID(+)\n");
		//sql.append("  AND A.IF_STATUS = ").append(DEConstant.IF_STATUS_0).append("\n");
		//sql.append("  AND A.PART_ID = 16103");
		List<PartInfoVO> list = select(sql.toString(), null);
		return list;
	}
	
	/**
	 * 
	* @Title: getPartInfoadd 
	* @Description: TODO(查询配件基础信息表,发送未下发的数据) 
	* @return List<PartInfoVO>    返回类型 
	* @throws
	 */
	public List<PartInfoVO> getPartInfoAdd(){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.PART_CODE, A.PART_NAME, A.PART_TYPE, A.UNIT,\n" );
		sql.append("       A.CHANGE_CODE, B.PART_CODE REPLACE_PART_CODE,\n" );
		sql.append("       A.CAR_AMOUNT, A.STOCK_PRICE, A.CLAIM_PRICE,\n" );
		sql.append("       A.REMARK, A.STOP_FLAG, A.MINI_PACK, A.CUSTOMER_PRICE\n" );
		sql.append("  FROM TM_PT_PART_BASE A, TM_PT_PART_BASE B\n" );
		sql.append("  WHERE A.REPLACE_PART_ID = B.PART_ID(+)\n");
		sql.append("  AND A.IF_STATUS = ").append(DEConstant.IF_STATUS_0).append("\n");
		List<PartInfoVO> list = select(sql.toString(), null);
		return list;
	}
	
	protected PartInfoVO wrapperVO(ResultSet rs, int idx) {
		PartInfoVO vo = new PartInfoVO();
		try {
			vo.setPartNo(rs.getString("PART_CODE"));
			vo.setPartName(rs.getString("PART_NAME"));
			vo.setPartGroupCode(rs.getInt("PART_TYPE"));
			vo.setUnitName(rs.getString("UNIT"));
			vo.setOptionRelation(rs.getString("CHANGE_CODE"));
			vo.setOptionNo(rs.getString("REPLACE_PART_CODE"));
			vo.setQuantityPerCar(rs.getString("CAR_AMOUNT"));
			vo.setPlanPrice(rs.getDouble("STOCK_PRICE"));
			vo.setClaimPrice(rs.getDouble("CLAIM_PRICE"));
			vo.setRemark(rs.getString("REMARK"));
			vo.setPartStatus(rs.getInt("STOP_FLAG"));
			vo.setMinPackage(rs.getDouble("MINI_PACK"));
			vo.setInstructPrice(rs.getDouble("CUSTOMER_PRICE"));
			vo.setDownTimestamp(downTimestamp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RpcException(e.getMessage());
		}
		return vo;
	}

}
