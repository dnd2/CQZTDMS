package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.po.TtDealerActualSalesPO;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.SalesVehicleApplyResultVO;

/**
 * @Title: DeDeSalesVehicleApplyResultDao.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-26
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class DeDeSalesVehicleApplyResultDao extends AbstractIFDao {
	public static Logger logger = Logger.getLogger(DeDeSalesVehicleApplyResultDao.class);
	private static final DeDeSalesVehicleApplyResultDao dao = new DeDeSalesVehicleApplyResultDao ();
	private static final int DELAER_LEVEL_1 = 1;
	private static final int DELAER_LEVEL_2 = 2;
	public static final DeDeSalesVehicleApplyResultDao getInstance() {
		return dao;
	}
	
	DeCommonDao comDao = new DeCommonDao();
	
	/**
	 * 
	* @Title: getDealerlevel 
	* @Description: TODO(获取订单的经销商级别) 
	* @param @param po
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String getDealerlevel(TtDealerActualSalesPO po) {
		String dealerLevel = null;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT B.DEALER_LEVEL\n" );
		sql.append("FROM TT_DEALER_ACTUAL_SALES A, TM_DEALER B\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("AND A.ORDER_ID = ").append(po.getOrderId());
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if (null == map) {
			throw new RpcException("Cann't find dealer in TT_DEALER_ACTUAL_SALES by " + po.getOrderId());
		}
		dealerLevel = String.valueOf(map.get("DEALER_LEVEL"));
		return dealerLevel;
	}
	
	public List<SalesVehicleApplyResultVO> getDealerLevel01Result(TtDealerActualSalesPO po){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.SO_NO, A.STATUS, A.SALES_DATE, B.VIN, C.DEALER_CODE, C.DEALER_NAME\n" );
		sql.append("FROM TT_DEALER_ACTUAL_SALES A, TM_VEHICLE B, TM_DEALER C\n" );
		sql.append("WHERE A.VEHICLE_ID = B.VEHICLE_ID\n" );
		sql.append("AND A.DEALER_ID = C.DEALER_ID\n");
		sql.append("AND A.ORDER_ID = ").append(po.getOrderId());
		List<SalesVehicleApplyResultVO> list = select(sql.toString(), null, DELAER_LEVEL_1);
		return list;
	}
	
	public List<SalesVehicleApplyResultVO> getDealerLevel02Result(TtDealerActualSalesPO po){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.SO_NO, A.STATUS, A.SALES_DATE, B.VIN, C.DEALER_CODE SUB_CODE, C.DEALER_NAME SUB_NAME,\n" );
		sql.append("       D.DEALER_CODE, D.DEALER_NAME\n" );
		sql.append("FROM TT_DEALER_ACTUAL_SALES A, TM_VEHICLE B, TM_DEALER C,TM_DEALER D\n" );
		sql.append("WHERE A.VEHICLE_ID = B.VEHICLE_ID\n" );
		sql.append("AND A.DEALER_ID = C.DEALER_ID\n" );
		sql.append("AND C.PARENT_DEALER_D = D.DEALER_ID\n");
		sql.append("AND A.ORDER_ID = ").append(po.getOrderId());
		List<SalesVehicleApplyResultVO> list = select(sql.toString(), null, DELAER_LEVEL_2);
		return list;
	}
	
	protected BaseVO wrapperVO(ResultSet rs, int idx, int flag) {
		switch(flag) {
		case DELAER_LEVEL_1 : 
			return wrapperSales1VO(rs, idx);
		case DELAER_LEVEL_2 : 
			return wrapperSales2VO(rs, idx);
		}
		return null;
	}
	
	protected SalesVehicleApplyResultVO wrapperSales2VO(ResultSet rs, int idx) {
		SalesVehicleApplyResultVO vo = new SalesVehicleApplyResultVO();
		try {
			vo.setSoNo(rs.getString("SO_NO"));
			vo.setVin(rs.getString("VIN"));
			vo.setSalesDate(rs.getDate("SALES_DATE"));
/*			Map<String, Object> map = comDao.getDmsDealerCode(rs.getString("DEALER_CODE"));
			vo.setEntityCode(String.valueOf(map.get("DMS_CODE")));
			vo.setEntityName(String.valueOf(map.get("COMPANY_SHORTNAME")));*/
			Map<String, Object> submap = comDao.getDmsDealerCode(rs.getString("SUB_CODE"));
			vo.setSubEntityCode(String.valueOf(submap.get("DMS_CODE")));
			vo.setSubEntityName(String.valueOf(submap.get("COMPANY_SHORTNAME")));
			vo.setApplyResult(DEConstant.APPLY_RESULT_01);
			vo.setBusinessType(DEConstant.BUSINESS_TYPE_00);
			vo.setDownTimestamp(downTimestamp);
			vo.setIsValid(rs.getInt("STATUS"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RpcException(e);
		}
		return vo;
	}

	protected SalesVehicleApplyResultVO wrapperSales1VO(ResultSet rs, int idx) {
		SalesVehicleApplyResultVO vo = new SalesVehicleApplyResultVO();
		try {
			vo.setSoNo(rs.getString("SO_NO"));
			vo.setVin(rs.getString("VIN"));
			vo.setSalesDate(rs.getDate("SALES_DATE"));
			Map<String, Object> map = comDao.getDmsDealerCode(rs.getString("DEALER_CODE"));
			vo.setEntityCode(String.valueOf(map.get("DMS_CODE")));
			vo.setEntityName(String.valueOf(map.get("COMPANY_SHORTNAME")));
			vo.setApplyResult(DEConstant.APPLY_RESULT_01);
			vo.setBusinessType(DEConstant.BUSINESS_TYPE_00);
			vo.setDownTimestamp(downTimestamp);
			vo.setIsValid(rs.getInt("STATUS"));
		} catch (Exception e) {
			throw new RpcException(e);
		}
		return vo;
	}
}
