package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.MonitorLabourVO;

public class DeMonitorLabourDao extends AbstractIFDao {
	public static Logger logger = Logger.getLogger(DeMonitorLabourDao.class);
	private static final DeMonitorLabourDao dao = new DeMonitorLabourDao ();
	
	public static final DeMonitorLabourDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: queryMonitorPart 
	* @Description: TODO(查询选定的预授权工时) 
	* @param ids 监控工时id列表
	* @return List<MonitorPartVO>  OPERATION_CODE, OPERATION_DESC 
	* @throws
	 */
	public List<MonitorLabourVO> queryMonitorLabour(String ids) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT OPERATION_CODE, OPERATION_DESC, OPEN_DATE, CLOSE_DATE FROM TT_AS_WR_FOREAPPROVALLAB \n");
		str.append("WHERE ID IN (").append(ids).append(")");
		//str.append(" WHERE NVL(IS_SEND, 0) = ").append(Constant.DOWNLOAD_CODE_STATUS_03);
		List<MonitorLabourVO> vos = select(str.toString(), null);
		return vos;
	}
	protected BaseVO wrapperVO(ResultSet rs, int idx) {
		MonitorLabourVO vo = new MonitorLabourVO();
		try {
			vo.setLabourCode(rs.getString("OPERATION_CODE"));
			vo.setLabourName(rs.getString("OPERATION_DESC"));
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
