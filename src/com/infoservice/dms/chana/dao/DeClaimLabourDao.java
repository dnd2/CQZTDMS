package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.ClaimLabourVO;

public class DeClaimLabourDao extends AbstractIFDao {
	public static Logger logger = Logger.getLogger(DeClaimLabourDao.class);
	private static final DeClaimLabourDao dao = new DeClaimLabourDao ();
	
	public static final DeClaimLabourDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: queryClaimLabour 
	* @Description: TODO(根据车型组id列表和公司Id查询需要下发的索赔工时) 
	* @param ids 车型组id列表, companyId 公司id
	* @return List<ClaimLabourVO>  LABOUR_CODE, CN_DES 
	* @throws
	 */
	public List<ClaimLabourVO> queryClaimLabour(String ids, Long companyId) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT A.LABOUR_CODE, A.CN_DES, A.LABOUR_HOUR, A.IS_DEL,\n ");
		str.append("       B.WRGROUP_CODE, B.WRGROUP_NAME \n");
		str.append("  FROM TT_AS_WR_WRLABINFO A, TT_AS_WR_MODEL_GROUP B\n");
		str.append(" WHERE A.WRGROUP_ID IN (").append(ids).append(") \n");
		str.append("   AND A.IS_DEL = 0");
		//str.append("   AND NVL(A.IF_STATUS, 0) <> ").append(DEConstant.IF_STATUS_2).append("\n");
		str.append("   AND A.WRGROUP_ID = B.WRGROUP_ID");
		//str.append(" AND NVL(IS_SEND, 0) <> ").append(Constant.DOWNLOAD_CODE_STATUS_03);
		//类型为主要工时
		str.append(" AND TREE_CODE = 3");
		List<ClaimLabourVO> vos = select(str.toString(), null);
		return vos;
	}
	protected BaseVO wrapperVO(ResultSet rs, int idx) {
		ClaimLabourVO vo = new ClaimLabourVO();
		try {
			vo.setLabourCode(rs.getString("LABOUR_CODE"));
			vo.setLabourName(rs.getString("CN_DES"));
			vo.setLabourHour(rs.getFloat("LABOUR_HOUR"));
			vo.setWrGroupCode(rs.getString("WRGROUP_CODE"));
			vo.setWrGroupName(rs.getString("WRGROUP_NAME"));
			vo.setDownTimestamp(downTimestamp);
			vo.setIsValid(rs.getInt("IS_DEL"));
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
}
