package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.infodms.dms.common.Constant;

import com.infoservice.dms.chana.common.RpcException;

import com.infoservice.dms.chana.vo.DefaultParaTempVO;
import com.infoservice.dms.chana.vo.DefaultParaVO;
import com.infoservice.dms.chana.vo.FaultPartsVO;
import com.infoservice.po3.bean.PO;
/**
 * 
 * @ClassName     : DefaultParaDao 
 * @Description   : 配件库房验证
 * @author        : luole
 * CreateDate     : 2013-7-10
 */
public class DefaultParaDao extends AbstractIFDao {

	private static final DefaultParaDao dao = new DefaultParaDao();

	public static final DefaultParaDao getInstance() {
		return dao;
	}

	public List<DefaultParaTempVO> queryPart(String dealerCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.dealer_code, t.is_oem, t.is_allocation, t.is_rechard\n");
		sql.append("  from tt_as_wr_warehouse t\n");
		sql.append(" where t.dealer_code = '").append(dealerCode).append("'\n");
		List<DefaultParaTempVO> vos = select(sql.toString(), null);
		return vos;
	}
	@Override
	protected DefaultParaTempVO wrapperVO(ResultSet rs, int idx) {
		DefaultParaTempVO vo = new DefaultParaTempVO();
		try {
			vo.setEntityCode(rs.getString("DEALER_CODE"));
			vo.setIsOem(rs.getInt("IS_OEM")+"");
			vo.setIsAllocation(rs.getInt("IS_ALLOCATION")+"");
			vo.setIsRechard(rs.getInt("IS_RECHARD")+"");
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
