package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.WarrantyPartVO;
import com.infoservice.dms.chana.vo.WarrantyQueryVO;

/**
 * @Title: DeWarrantyQueryDao.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-29
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class DeWarrantyQueryDao extends AbstractIFDao {
	public static Logger logger = Logger.getLogger(DeWarrantyQueryDao.class);
	private static final DeWarrantyQueryDao dao = new DeWarrantyQueryDao ();
	
	public static final DeWarrantyQueryDao getInstance() {
		return dao;
	}
	
	public List<WarrantyQueryVO> getDeWarrantyQuery(WarrantyQueryVO vo){
		StringBuffer sql= new StringBuffer();
		sql.append("AND A.VIN = '").append(vo.getVin()).append("'\n");

		List<WarrantyQueryVO> list = select(sql.toString(), null);
		return list;
	}
	
	protected WarrantyQueryVO wrapperVO(ResultSet rs, int idx) {
		WarrantyQueryVO vo = new WarrantyQueryVO();
		try {
			vo.setVin(rs.getString("VIN"));
			vo.setDownTimestamp(downTimestamp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
	/**
	 * 
	* @Title: getVinInfo 
	* @Description: TODO(根据VIN查询相关信息) 
	* @param @param vo
	* @param @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	public Map<String, Object> getVinInfo(WarrantyQueryVO vo){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.PURCHASED_DATE, A.MILEAGE, A.MODEL_ID,\n" );
		sql.append("       B.PROVINCE_ID, B.OEM_COMPANY_ID\n" );
		sql.append("FROM TM_VEHICLE A, TM_DEALER B\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("AND B.STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		sql.append("AND A.VIN = '").append(vo.getVin()).append("'\n");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	public Map<String, Object> getPartGurn(Map<String, Object> map, WarrantyPartVO vo){
		StringBuffer sql= new StringBuffer();
		sql.append(" SELECT B.PART_CODE,\n" );
		sql.append("      MAX(CLAIM_MONTH) GURN_MONTH,\n" );
		sql.append("      MAX(CLAIM_MELIEAGE) GURN_MILE\n" );
		sql.append(" FROM TT_AS_WR_RULE A, TT_AS_WR_RULE_LIST B\n" );
		sql.append("WHERE A.ID = B.RULE_ID\n" );
		sql.append("  AND A.RULE_STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		sql.append("  AND A.RULE_TYPE = ").append(Constant.RULE_TYPE_02).append("\n");
		sql.append("  and B.PART_CODE = '").append(vo.getPartNo()).append("'\n");
		sql.append("  AND EXISTS (SELECT A2.ID\n" );
		sql.append("         FROM TT_AS_WR_GAME A2\n" );
		sql.append("        WHERE A2.ID IN (SELECT MAX(A1.ID)\n" );
		sql.append("                          FROM TT_AS_WR_GAME A1\n" );
		sql.append("                         WHERE 1 = 1\n" );
		sql.append("                           AND EXISTS (SELECT C1.ID\n" );
		sql.append("                                  FROM TT_AS_WR_GAMEMODEL C1\n" );
		sql.append("                                 WHERE C1.GAME_ID = A1.ID\n" );
		sql.append("                                   AND C1.MODEL_ID = ").append(map.get("MODEL_ID")).append("\n");
		sql.append(")\n");
		sql.append("                           AND EXISTS (SELECT D1.ID\n" );
		sql.append("                                  FROM TT_AS_WR_GAMEPRO D1\n" );
		sql.append("                                 WHERE D1.GAME_ID = A1.ID\n" );
		sql.append("                                   AND D1.PROVINCE_ID = ").append(map.get("PROVINCE_ID")).append("\n");
		sql.append(")\n");
		sql.append("                           AND A1.START_DATE <= TO_DATE('").append(map.get("PURCHASED_DATE")).append("', 'YYYY-MM-DD HH24:MI:SS')").append("\n");
		sql.append("                           AND A1.END_DATE >= TO_DATE('").append(map.get("PURCHASED_DATE")).append("', 'YYYY-MM-DD HH24:MI:SS')").append("\n");
		sql.append(")\n");
		sql.append("          AND A2.RULE_ID = A.ID)\n" );
		sql.append("GROUP BY B.PART_CODE\n");

		Map<String, Object> partMap = pageQueryMap(sql.toString(), null, getFunName());
		return partMap;
	}
	
	public Map<String, Object> getPartSysGurn(Map<String, Object> map, WarrantyPartVO vo){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT B.PART_CODE,MAX(CLAIM_MONTH) GURN_MONTH,MAX(CLAIM_MELIEAGE) GURN_MILE\n" );
		sql.append("FROM TT_AS_WR_RULE A, TT_AS_WR_RULE_LIST B\n" );
		sql.append("WHERE A.ID = B.RULE_ID\n" );
		sql.append("AND A.RULE_STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		sql.append("AND A.RULE_TYPE = ").append(Constant.RULE_TYPE_01).append("\n");
		sql.append("AND B.PART_CODE = '").append(vo.getPartNo()).append("'\n");
		sql.append("GROUP BY B.PART_CODE");

		Map<String, Object> partMap = pageQueryMap(sql.toString(), null, getFunName());
		return partMap;
	}
	
}
