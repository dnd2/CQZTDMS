package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.MonitorPartVO;
public class DeMonitorPartDao extends AbstractIFDao {

	public static Logger logger = Logger.getLogger(DeMonitorPartDao.class);
	private static final DeMonitorPartDao dao = new DeMonitorPartDao ();
	
	public static final DeMonitorPartDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: queryMonitorPart 
	* @Description: TODO(查询选定下发的预授权配件) 
	* @param ids 配件id列表
	* @return List<MonitorPartVO>  PART_CODE, PART_NAME 
	* @throws
	 */
	public List<MonitorPartVO> queryMonitorPart(String ids) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT PART_CODE, PART_NAME, OPEN_DATE, CLOSE_DATE FROM TT_AS_WR_FOREAPPROVALPT \n");
		str.append("WHERE ID IN (").append(ids).append(")");
		//str.append(" WHERE NVL(IS_SEND, 0) = ").append(Constant.DOWNLOAD_CODE_STATUS_03);
		List<MonitorPartVO> vos = select(str.toString(), null);
		return vos;
	}
	protected BaseVO wrapperVO(ResultSet rs, int idx) {
		MonitorPartVO vo = new MonitorPartVO();
		try {
			vo.setPartNo(rs.getString("PART_CODE"));
			vo.setPartName(rs.getString("PART_NAME"));
			vo.setOpenDate(rs.getDate("OPEN_DATE"));
			vo.setCloseDate(rs.getDate("CLOSE_DATE"));
			vo.setDownTimestamp(downTimestamp);
			vo.setIsValid(Constant.STATUS_ENABLE);
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}

}
