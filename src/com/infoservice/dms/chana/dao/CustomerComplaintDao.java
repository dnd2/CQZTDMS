package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.CustomerComplaintDetailVO;
import com.infoservice.dms.chana.vo.CustomerComplaintVO;

public class CustomerComplaintDao extends AbstractIFDao {
	public static Logger logger = Logger.getLogger(CustomerComplaintDao.class);
	private static final CustomerComplaintDao dao = new CustomerComplaintDao();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	private static final int CUSTOM_COMPLAINT = 0;
	private static final int CUSTOM_COMPLAINT_DETAIL = 1;
	
	public static final CustomerComplaintDao getInstance() {
		return dao;
	}
	
	/**
	 * 
	* @Title: queryCustomerComplaint 
	* @Description: TODO(查询未下发的投诉信息) 
	* @param codes 投诉信息code '123','234','567'
	* @return List<CustomerComplaintVO>    返回类型 
	* @throws
	 */
	public List<CustomerComplaintVO> queryCustomerComplaint(String codes, Long dealerId) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT A.COMP_ID, A.COMP_CODE, A.LINK_MAN, A.SEX, A.TEL, A.COMP_TYPE, A.COM_TYPE, \n");
		str.append("A.CREATE_DATE, A.COMP_CONTENT, A.STATUS, A.COMP_LEVEL, A.LICENSE_NO, A.VIN, A.ENGINE_NO, \n");
		str.append("A.COMP_SOURCE, A.IS_DEL, B.DEALER_CODE \n");
		str.append(" FROM TT_CR_COMPLAINTS A, TM_DEALER B \n");
		str.append(" WHERE COMP_CODE IN (").append(codes).append(") \n");
		str.append("AND B.DEALER_ID = ").append(dealerId);
		//str.append("AND B.COMPANY_ID = C.COMPANY_ID");
		//str.append(" WHERE IF_STATUS = ").append(DEConstant.IF_STATUS_1);
		List<CustomerComplaintVO> vos = select(str.toString(), null, CUSTOM_COMPLAINT);
		return vos;
	}
	
	/**
	 * 
	* @Title: queryCustomerComplaintDetail 
	* @Description: TODO(根据投诉ID查询投诉详细信息) 
	* @param @return 
	* @return List<CustomerComplaintDetailVO>    返回类型 
	* @throws
	 */
	public List<BaseVO> queryCustomerComplaintDetail(Long comId) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT A.AUDIT_DATE, A.CREATE_BY, A.AUDIT_CONTENT, B.NAME \n");
		str.append(" FROM TT_CR_COMPLAINTS_AUDIT A, TC_USER B \n");
		str.append(" WHERE COMP_ID = ").append(comId).append("\n");
		str.append(" AND NVL (A.UPDATE_BY, A.CREATE_BY) = B.USER_ID");
		List<BaseVO> vos = select(str.toString(), null, CUSTOM_COMPLAINT_DETAIL);
		return vos;
	}
	
	protected BaseVO wrapperVO(ResultSet rs, int idx, int flag) {
		switch (flag) {
		//投诉主表
		case CUSTOM_COMPLAINT :
			return wrapperCustomerComplaintVO(rs, idx);
		//维修项目
		case CUSTOM_COMPLAINT_DETAIL : 
			return wrapperCustomerComplaintDetailVO(rs, idx);
		}
		return null;
	}
	
	private BaseVO wrapperCustomerComplaintDetailVO(ResultSet rs, int idx) {
		CustomerComplaintDetailVO vo = new CustomerComplaintDetailVO();
		try {
			vo.setDealDate(rs.getDate("AUDIT_DATE"));
			vo.setDealer(rs.getString("NAME"));
			vo.setDealResult(rs.getString("AUDIT_CONTENT"));
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
	private BaseVO wrapperCustomerComplaintVO(ResultSet rs, int idx) {
		CustomerComplaintVO vo = new CustomerComplaintVO();
		try {
			Map<String, Object> map = deCommonDao.getDmsDealerCode(rs.getString("DEALER_CODE"));
			vo.setEntityCode(map.get("DMS_CODE").toString());
			vo.setComplaintNo(rs.getString("COMP_CODE"));
			vo.setComplaintName(rs.getString("LINK_MAN"));
			vo.setComplaintGender(rs.getInt("SEX"));
			vo.setComplaintPhone(rs.getString("TEL"));
			vo.setComplaintType(rs.getInt("COM_TYPE"));
			vo.setComplaintDate(rs.getDate("CREATE_DATE"));
			vo.setComplaintSummary(rs.getString("COMP_CONTENT"));
			vo.setDealStatus(rs.getInt("STATUS"));
			vo.setComplaintSerious(rs.getInt("COMP_LEVEL"));
			vo.setLicense(rs.getString("LICENSE_NO"));
			vo.setVin(rs.getString("VIN"));
			vo.setEngineNo(rs.getString("ENGINE_NO"));
			vo.setComplaintOrigin(rs.getInt("COMP_SOURCE"));
			vo.setDownTimestamp(downTimestamp);
			vo.setIsValid(rs.getInt("IS_DEL"));
			vo.setDealVoList(DEUtil.transType(queryCustomerComplaintDetail(rs.getLong("COMP_ID"))));
		} catch (Exception e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
}
