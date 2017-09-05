package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.util.List;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.dms.chana.vo.DeliveryOrderVO;
import com.infoservice.dms.chana.vo.DeliveryPartVO;

/**
 * 
* @ClassName: DeSignDao 
* @Description: TODO(接口配件采购订单签收) 
* @author liuqiang 
* @date Sep 5, 2010 2:50:46 PM 
*
 */
public class DeSignDao extends AbstractIFDao {
	//货运单
	private static final int DEL_OR = 1;
	//货运单配件
	private static final int DEL_PART = 2;
	private static final DeSignDao dao = new DeSignDao();
	public static DeSignDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: querySign 
	* @Description: TODO(根据签收单号查询) 
	* @param @param signNo    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public List<DeliveryOrderVO> querySign(String signNo) { //B.DELIVERY_PDC,B.DELIVERY_COMPANY,B.REMARK,
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.SIGN_ID, A.DO_NO, A.SIGN_USER_ID, A.SIGN_DATE,\n");
		sql.append(" B.SHIPPING_CONDITION,B.CREATE_DATE,C.DEALER_CODE\n");
		sql.append("FROM TT_PT_DLR_SIGN A, TT_PT_SHIPPINGSHEET B, TM_DEALER C, TT_PT_ORDER D\n");
		sql.append("WHERE A.DO_NO = B.DO_NO\n");
		sql.append("  AND A.SIGN_NO = '").append(signNo).append("'\n");
		sql.append("  AND A.ORDER_ID = D.ORDER_ID\n");
		sql.append("  AND C.DEALER_ID = D.DEALER_ID\n");
		List<DeliveryOrderVO> vos = select(sql.toString(), null, DEL_OR);
		return vos;
	}
	
	public List<BaseVO> querySignDetail(Long signId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.DO_NO, B.SIGN_QUANTITY, C.PART_CODE, C.PART_NAME, C.UNIT,\n");
		sql.append("	   D.ORDER_NO, E.COUNT, E.CARTON_NO, E.PRICE, E.TOTAL_PRICE,\n");
		sql.append("       E.INSTRUCT_PRICE, E.REMARK, F.ORDER_COUNT\n");
		sql.append("FROM TT_PT_DLR_SIGN A, TT_PT_DLR_SIGN_DETAIL B, TM_PT_PART_BASE C,\n");
		sql.append("     TT_PT_ORDER D, TT_PT_SHIPPINGSHEETITEM E, TT_PT_ORDITEM F\n");
		sql.append("WHERE B.SIGN_ID = ").append(signId);
		sql.append("  AND A.SIGN_ID = ").append(signId);
		sql.append("  AND B.PART_ID = C.PART_ID\n");
		sql.append("  AND A.ORDER_ID = D.ORDER_ID\n");
		sql.append("  AND A.DO_NO = E.DO_NO\n");
		sql.append("  AND A.ORDER_ID = F.ORDER_ID\n");
		sql.append("  AND B.PART_ID = F.PART_ID");
		List<BaseVO> vos = select(sql.toString(), null, DEL_PART);
		return vos;
	}
	
	protected BaseVO wrapperVO(ResultSet rs, int idx, int flag) {
		switch (flag) {
		//服务活动
		case DEL_OR :
			return wrapperDelVO(rs, idx);
		//维修项目
		case DEL_PART : 
			return wrapperDelPartVO(rs, idx);
		}
		return null;
	}
	private BaseVO wrapperDelVO(ResultSet rs, int idx) {
		DeliveryOrderVO vo = new DeliveryOrderVO();
		try {
			vo.setEntityCode(rs.getString("DEALER_CODE"));
			vo.setDoNo(rs.getString("DO_NO"));
			vo.setShippingWay(rs.getInt("SHIPPING_CONDITION"));
			vo.setDeliveryPdc(rs.getString("DELIVERY_PDC"));
			vo.setDeliveryCompany(rs.getString("DELIVERY_COMPANY"));
			vo.setSignforPerson(rs.getString("SIGN_USER_ID"));
			vo.setSignforDate(rs.getDate("SIGN_DATE"));
			vo.setDeliveryTime(rs.getDate("CREATE_DATE"));
			vo.setRemark(rs.getString("REMARK"));
			vo.setDownTimestamp(downTimestamp);
			vo.setIsValid(0);
			vo.setDeliveryPartVoList(DEUtil.transType(querySignDetail(rs.getLong("SIGN_ID"))));
		} catch (Exception e) {
			throw new RpcException(e);
		}
		return vo;
	}
	
	private BaseVO wrapperDelPartVO(ResultSet rs, int idx) {
		DeliveryPartVO vo = new DeliveryPartVO();
		try {
			vo.setOrderNo(rs.getString("ORDER_NO"));
			vo.setPartNo(rs.getString("PART_CODE"));
			vo.setPartName(rs.getString("PART_NAME"));
			vo.setUnitName(rs.getString("UNIT"));
			//vo.setSupplyQty(Double.valueOf(rs.getInt("COUNT")));
			vo.setSupplyQty(Double.valueOf(rs.getInt("SIGN_QUANTITY")));
			vo.setCount(Double.valueOf(rs.getInt("ORDER_COUNT")));
			vo.setCaseNo(rs.getString("CARTON_NO"));
			vo.setRemark(rs.getString("REMARK"));
			vo.setPlanPrice(rs.getDouble("PRICE"));
			vo.setInstructPrice(rs.getDouble("INSTRUCT_PRICE"));
			vo.setAmount(rs.getDouble("TOTAL_PRICE"));
		} catch (Exception e) {
			throw new RpcException(e);
		}
		return vo;
	}
}
