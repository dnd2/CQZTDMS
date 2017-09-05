package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.infodms.dms.common.Constant;

import com.infoservice.dms.chana.common.RpcException;

import com.infoservice.dms.chana.vo.FaultPartsVO;
import com.infoservice.po3.bean.PO;
/**
 * 
 * @ClassName     : DeFaultPartsDao 
 * @Description   : 配件信息下发
 * @author        : luole
 * CreateDate     : 2013-5-24
 */
public class DeFaultPartsDao extends AbstractIFDao {

	private static final DeFaultPartsDao dao = new DeFaultPartsDao();

	public static final DeFaultPartsDao getInstance() {
		return dao;
	}

	public List<FaultPartsVO> queryPart() {
		StringBuffer sql = new StringBuffer();
		sql.append("select p.part_code, p.part_name, f.fault_code, f.fault_name, p.status\n");
		sql.append("  from tt_as_wr_fault_parts p, tt_as_wr_fault_legal f\n");
		sql.append(" where f.fault_id = p.fault_id\n");
		sql.append("   and p.is_de = ").append(Constant.IS_SEND_OUT_00);
		List<FaultPartsVO> vos = select(sql.toString(), null);
		return vos;
	}
	public List<FaultPartsVO> queryPart1() {
		StringBuffer sql = new StringBuffer();
		sql.append("select p.part_code, p.part_name, f.fault_code, f.fault_name, p.status\n");
		sql.append("  from tt_as_wr_fault_parts p, tt_as_wr_fault_legal f\n");
		sql.append(" where f.fault_id = p.fault_id\n");
		List<FaultPartsVO> vos = select(sql.toString(), null);
		return vos;
	}
	public void updateDePart(String fCode,String pCode){
		StringBuffer sql = new StringBuffer();
		sql.append("update tt_as_wr_fault_parts m\n");
		sql.append("   set m.is_de = ").append(Constant.IS_SEND_OUT_01).append("\n");
		sql.append(" where m.id in (select d.id\n" );
		sql.append("                 from tt_as_wr_fault_legal f, tt_as_wr_fault_parts d\n" );
		sql.append("                where f.fault_id = d.fault_id\n" ); 
		sql.append("                  and d.part_code = '").append(pCode).append("'\n" ); 
		sql.append("                  and f.fault_code = '").append(fCode).append("')\n" ); 
		update(sql.toString(), null);
	}
	@Override
	protected FaultPartsVO wrapperVO(ResultSet rs, int idx) {
		FaultPartsVO vo = new FaultPartsVO();
		try {
			vo.setFaultCode(rs.getString("FAULT_CODE"));
			vo.setFaultName(rs.getString("FAULT_NAME"));
			vo.setPartCode(rs.getString("PART_CODE"));
			vo.setPartName(rs.getString("PART_NAME"));
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
