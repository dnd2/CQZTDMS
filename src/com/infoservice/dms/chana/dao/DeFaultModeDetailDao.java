package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.infodms.dms.common.Constant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.FaultModeDetailVO;
import com.infoservice.po3.bean.PO;

/**
 * 
 * @ClassName     : DeFaultModeDetailDao 
 * @Description   : 失效模式信息下发
 * @author        : luole
 * CreateDate     : 2013-5-24
 */
public class DeFaultModeDetailDao extends AbstractIFDao {
	
	private static final DeFaultModeDetailDao dao = new DeFaultModeDetailDao ();
	
	public static final DeFaultModeDetailDao getInstance() {
		return dao;
	}

	public List<FaultModeDetailVO> queryModelDetail() {
		StringBuffer sql = new StringBuffer();
		sql.append("select m.failure_mode_code,m.failure_mode_name, f.fault_code, f.fault_name, m.status\n");
		sql.append("  from tt_as_wr_fault_mode_detail m, tt_as_wr_fault_legal f\n");
		sql.append(" where m.fault_id = f.fault_id\n");
		sql.append("   and m.is_de = ").append(Constant.IS_SEND_OUT_00);
		List<FaultModeDetailVO> vos = select(sql.toString(), null);
		return vos;
	}
	public List<FaultModeDetailVO> queryModelDetail1() {
		StringBuffer sql = new StringBuffer();
		sql.append("select m.failure_mode_code,m.failure_mode_name, f.fault_code, f.fault_name, m.status\n");
		sql.append("  from tt_as_wr_fault_mode_detail m, tt_as_wr_fault_legal f\n");
		sql.append(" where m.fault_id = f.fault_id\n");
		List<FaultModeDetailVO> vos = select(sql.toString(), null);
		return vos;
	}
	/**
	 * @Title      :  更新
	 * @Description: TODO 
	 * @param      : @param fCode :法定名称CODE
	 * @param      : @param mCode ：失效模式code
	 * @return     :    
	 * @throws     : luole
	 * LastDate    : 2013-5-24
	 */
	public void updateModelDetail(String fCode,String mCode){
		StringBuffer sql = new StringBuffer();
		sql.append("update tt_as_wr_fault_mode_detail m\n");
		sql.append("   set m.is_de = ").append(Constant.IS_SEND_OUT_01).append("\n");
		sql.append(" where m.id in (select d.id\n" );
		sql.append("                 from tt_as_wr_fault_legal f, tt_as_wr_fault_mode_detail d\n" );
		sql.append("                where f.fault_id = d.fault_id\n" ); 
		sql.append("                  and d.failure_mode_code = '").append(mCode).append("'\n" ); 
		sql.append("                  and f.fault_code = '").append(fCode).append("')\n" ); 
		update(sql.toString(), null);
	}
	
	@Override
	protected FaultModeDetailVO wrapperVO(ResultSet rs, int idx) {
		FaultModeDetailVO vo = new FaultModeDetailVO();
		try {
			vo.setFaultCode(rs.getString("FAULT_CODE"));
			vo.setFaultName(rs.getString("FAULT_NAME"));
			vo.setFailureModeCode(rs.getString("FAILURE_MODE_CODE"));
			vo.setFailureModeName(rs.getString("FAILURE_MODE_NAME"));
			vo.setStatus(rs.getInt("STATUS"));
			vo.setDownTimestamp(downTimestamp);
			vo.setIsValid(0);
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
