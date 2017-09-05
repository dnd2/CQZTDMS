package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.TmClaimConfirmBillVO;
import com.infoservice.dms.chana.vo.TmClaimReceiveDetailVO;

public class TmClaimConfirmBillDao extends AbstractIFDao implements DEConstant{
	public static Logger logger = Logger.getLogger(TmClaimConfirmBillDao.class);
	private static final TmClaimConfirmBillDao dao = new TmClaimConfirmBillDao ();
	private static final int bill = 0;
	private static final int bill_detail = 1;
	
	public static final TmClaimConfirmBillDao getInstance() {
		return dao;
	}
	
	public List<TmClaimConfirmBillVO> getTmClaimConfirmBill(String balanceNo){
		StringBuffer sql= new StringBuffer();
		sql.append("select a.id,\n");
		sql.append("       a.balance_no,\n"); 
		sql.append("       a.start_date,\n");  
		sql.append("       a.end_date,\n");  
		sql.append("       decode(a.yieldly,\n");  
		sql.append("              11311001,\n");  
		sql.append("              '重庆',\n");  
		sql.append("              11311002,\n");  
		sql.append("              '河北',\n");  
		sql.append("              11311003,\n");  
		sql.append("              '南京') as PRODUCEBASE,\n");  
		sql.append("       a.claim_count,\n");  
		sql.append("       a.labour_amount_bak + a.part_amount_bak + a.other_amount_bak +\n");  
		sql.append("       a.service_total_amount_bak + a.free_amount_bak +\n");  
		sql.append("       a.append_labour_amount_bak + a.append_amount_bak as TOTALEXPENSE,\n");  
		sql.append("       a.market_amount_bak + a.service_total_amount_bak +\n");  
		sql.append("       a.return_amount_bak as OTHEREXPENSE,\n");  
		sql.append("       a.apply_amount - a.balance_amount as CUTEXPENSE,\n");  
		sql.append("       a.note_amount\n");  
		sql.append("  from Tt_As_Wr_Claim_Balance a\n");
		sql.append("  where a.balance_no = '"+balanceNo+"'");
		List<TmClaimConfirmBillVO> list = select(sql.toString(), null, bill);
		return list;
	}
	
	public List<TmClaimReceiveDetailVO> getReceiveVoList(Long balanceId){
		StringBuffer sql= new StringBuffer();
		sql.append("select b.claim_no, b.ro_no, b.vin, b.license_no,c.dealer_code,c.is_de\n");
		sql.append("  from tr_balance_claim a, tt_as_wr_application b,tt_as_repair_order c\n");  
		sql.append(" where a.claim_id = b.id and b.ro_no = c.ro_no\n");  
		sql.append("   and a.balance_id = "+balanceId);
		List<TmClaimReceiveDetailVO> list = select(sql.toString(), null, bill_detail);
		return list;
	}
	
	protected BaseVO wrapperVO(ResultSet rs, int idx, int flag) {
	switch(flag) {
	case bill : 
		return wrapperBillVO(rs, idx);
	case bill_detail : 
		return wrapperBillDetalVO(rs, idx);
	}
	return null;
	}
	
	private TmClaimConfirmBillVO wrapperBillVO(ResultSet rs, int idx){
		TmClaimConfirmBillVO vo = new TmClaimConfirmBillVO();
		try{
			vo.setBillNo(rs.getString("balance_no"));
			vo.setBalanceBegin(rs.getDate("start_date"));
			vo.setBalanceEnd(rs.getDate("end_date"));
			vo.setProduceBase(rs.getString("PRODUCEBASE"));
			vo.setTotalBillSum(rs.getInt("claim_count"));
			vo.setTotalExpense(rs.getDouble("TOTALEXPENSE"));
			vo.setOtherExpense(rs.getDouble("OTHEREXPENSE"));
			vo.setCutExpense(rs.getDouble("CUTEXPENSE"));
			vo.setBillAmount(rs.getDouble("note_amount"));
			vo.setClaimReceiveVoList(DEUtil.transType(getReceiveVoList(rs.getLong("id"))));
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
	private TmClaimReceiveDetailVO wrapperBillDetalVO(ResultSet rs, int idx){
		TmClaimReceiveDetailVO vo = new TmClaimReceiveDetailVO();
		try{
			vo.setClaimNo(rs.getString("claim_no"));
			vo.setRoNo(rs.getString("ro_no"));
			vo.setVin(rs.getString("vin"));
			vo.setLicense(rs.getString("license_no"));
			if(rs.getInt("is_de")==1){
				vo.setDmsRoNo(vo.getRoNo().replace(rs.getString("dealer_code")+"DR", "RO"));
			}
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
}
