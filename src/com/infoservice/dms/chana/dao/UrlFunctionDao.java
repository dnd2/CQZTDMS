package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.UrlFunctionVO;

public class UrlFunctionDao extends AbstractIFDao {
	private static final UrlFunctionDao dao = new UrlFunctionDao();
	
	public static final UrlFunctionDao getInstance() {
		return dao;
	}
	
	/**
	 * 
	* @Title: queryUrlFunction 
	* @Description: TODO(根据id列表查询url功能列表) 
	* @param @param ids 
	* @param @return  FUNC_ID, PAR_FUNC_ID, FUNC_CODE, FUNC_NAME, SORT_ORDER
	* @return List<UrlFunctionVO>    返回类型 
	* @throws
	 */
	public List<UrlFunctionVO> queryUrlFunction() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT FUNC_ID, PAR_FUNC_ID, FUNC_CODE, FUNC_NAME, SORT_ORDER \n");
		sql.append(" FROM TC_URL_FUNC \n");
		//sql.append(" WHERE URL_FUNC_ID IN (").append(ids).append(")");
		List<UrlFunctionVO> list = select(sql.toString(), null);
		return list;
	}
	
	protected UrlFunctionVO wrapperVO(ResultSet rs, int idx) {
		UrlFunctionVO vo = new UrlFunctionVO();
		try {
			vo.setFunctionName(rs.getString("FUNC_NAME"));
			vo.setUrl(rs.getString("FUNC_CODE"));
			vo.setParentFunctionCode(String.valueOf(rs.getInt("PAR_FUNC_ID")));
			vo.setDcsFunctionId(String.valueOf(rs.getInt("FUNC_ID")));
			vo.setSort(rs.getInt("SORT_ORDER"));
			vo.setDownTimestamp(downTimestamp);
		} catch (SQLException e) {
			throw new RpcException(e);
		}
		return vo;
	}
	
}
