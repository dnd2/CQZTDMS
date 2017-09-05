package com.infodms.dms.dao.sales.ordermanage.orderdetail;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

public class OrderDetailInfoQueryDao extends BaseDao{
	public static Logger logger = Logger.getLogger(OrderDetailInfoQueryDao.class);
	private static final OrderDetailInfoQueryDao dao = new OrderDetailInfoQueryDao();

	public static final OrderDetailInfoQueryDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Map<String, Object> orderInfo(String orderId,String orderNo)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT T.*, M.TYPE_NAME, NVL(M.AVAILABLE_AMOUNT,0) AVAILABLE_AMOUNT\n" );
		sql.append("  FROM (SELECT TVO.ORDER_NO,\n" );
		sql.append("               TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n" );
		sql.append("               TVO.ORDER_ORG_ID,\n" );
		sql.append("               TVO.ORDER_TYPE,\n" );
		sql.append("               TVO.DELIVERY_TYPE,\n" );
		sql.append("               TVO.PAY_REMARK,\n" );
		sql.append("               TVO.ORDER_REMARK,\n" );
		sql.append("               TVO.REFIT_REMARK,\n" );
		sql.append("               TVO.FUND_TYPE_ID,\n" );
		sql.append("               TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("               TMD.DEALER_LEVEL,\n" );
		sql.append("               TMVA.ADDRESS,\n" );
		sql.append("               TVO.LINK_MAN,\n" );
		sql.append("               TVO.TEL,\n" );
		sql.append("               TMF.FLEET_NAME,\n" );
		sql.append("               TVO.FLEET_ADDRESS,\n" );
		//sql.append("               TVP.PRICE_DESC,\n" );
		sql.append("               TD1.DEALER_SHORTNAME,\n" );
		sql.append("               TVO.Other_Price_Reason\n" );
		sql.append("          FROM TT_VS_ORDER   TVO,\n" );
		sql.append("               TM_DEALER     TMD,\n" );
		sql.append("               TM_VS_ADDRESS TMVA,\n" );
		sql.append("               TM_FLEET      TMF,\n" );
		sql.append("               TM_DEALER TD1\n" );
	//	sql.append("               vw_TT_VS_PRICE TVP\n" );
		sql.append("         WHERE TVO.ORDER_ORG_ID = TMD.DEALER_ID\n" );
		sql.append("           AND TVO.DELIVERY_ADDRESS = TMVA.ID(+)\n" );
		sql.append("           AND TVO.FLEET_ID = TMF.FLEET_ID(+)\n" );
		sql.append("           AND TD1.DEALER_ID(+)=TVO.RECEIVER\n" );
//		sql.append("           AND TVP.PRICE_ID(+)=TVO.PRICE_ID\n" );
		if(!"".equals(orderId)&&orderId!=null){
			sql.append("   AND TVO.ORDER_ID = "+orderId+"\n" );
		}
		if(!"".equals(orderNo)&&orderNo!=null){
			sql.append("   AND TVO.ORDER_NO = '"+orderNo+"'\n");
		}
		sql.append("           ) T\n" );
		sql.append("  LEFT JOIN (SELECT TTVA.DEALER_ID,\n" );
		sql.append("                    TTVA.ACCOUNT_TYPE_ID,\n" );
		sql.append("                    TVAT.TYPE_NAME,\n" );
		sql.append("                    TTVA.AVAILABLE_AMOUNT\n" );
		sql.append("               FROM TT_VS_ACCOUNT TTVA, TT_VS_ACCOUNT_TYPE TVAT\n" );
		sql.append("              WHERE TTVA.ACCOUNT_TYPE_ID = TVAT.TYPE_ID) M ON T.ORDER_ORG_ID =\n" );
		sql.append("                                                              M.DEALER_ID\n" );
		sql.append("                                                          AND T.FUND_TYPE_ID =\n" );
		sql.append("                                                              M.ACCOUNT_TYPE_ID");
		Map<String, Object> map= dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	/*
	 * 获取联系电话，联系人，收货方，价格类型，使用折让
	 * 价格类型
	 * 
	 */
	public Map<String, Object> getMyOrderDetail(String orderNo){
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT  TVP.PRICE_DESC,TVO.TEL,TVO.LINK_MAN, TVO.RECEIVER,TVO.PRICE_ID,TVO.DISCOUNT\n");
		sql.append("  FROM TT_VS_ORDER TVO,vw_TT_VS_PRICE TVP\n");
		sql.append(" WHERE 1 = 1\n");
		sql.append(" AND TVP.PRICE_ID=TVO.PRICE_ID");
		sql.append(" AND TVO.ORDER_NO='"+orderNo+"'");
		Map<String,Object> list=dao.pageQueryMap(sql.toString(),null, getFunName());
		return list;
	}
	
	
	
	
	public List<Map<String, Object>> orderDetail(String orderId,String orderNo)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVMG1.GROUP_NAME,\n" );
		sql.append("       TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       TVM.COLOR_NAME,\n" );
		sql.append("       TVM.MATERIAL_ID,\n" );
		sql.append("       TVOD.ORDER_AMOUNT,\n" );
		//sql.append("       NVL(TVOD.CHECK_AMOUNT,0) CHECK_AMOUNT,\n" );
		sql.append("       NVL(TVOD.CALL_AMOUNT,0) CHECK_AMOUNT,\n" );
		sql.append("       NVL(TVOD.MATCH_AMOUNT,0) MATCH_AMOUNT,");
		sql.append("       TVOD.SINGLE_PRICE,\n" );
		sql.append("       TVOD.ORDER_AMOUNT * TVOD.SINGLE_PRICE TOTAIL_PRICE\n" );
		sql.append("  FROM TT_VS_ORDER              TVO,\n" );
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n" );
		sql.append("       TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG1,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG2,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG3,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR\n" );
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n" );
		sql.append("   AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append("   AND TVMGR.GROUP_ID = TVMG3.GROUP_ID\n" );
		sql.append("   AND TVMG3.PARENT_GROUP_ID = TVMG2.GROUP_ID\n" );
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG1.GROUP_ID\n" );
		if(!"".equals(orderId)&&orderId!=null){
			sql.append("   AND TVO.ORDER_ID = "+orderId+"\n" );
		}
		if(!"".equals(orderNo)&&orderNo!=null){
			sql.append("   AND TVO.ORDER_NO = '"+orderNo+"'\n");
		}
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getMatchList(Map<String, Object> map) {
		
		String orderNo = (String)map.get("orderNo");
		String matId = (String)map.get("matId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TV.VIN,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TV.NODE_CODE,\n");  
		sql.append("       TV.BATCH_NO,\n");  
		sql.append("       TO_CHAR(TV.NODE_DATE, 'YYYY-MM-DD HH24:MI:SS') NODE_DATE\n");  
		sql.append("  FROM TT_VS_ORDER      TVO,\n");  
		sql.append("       TT_VS_DLVRY      TVD,\n");  
		sql.append("       TT_VS_DLVRY_DTL  TVDD,\n");  
		sql.append("       TT_VS_DLVRY_MCH  TVDM,\n");  
		sql.append("       TM_VEHICLE       TV,\n");  
		sql.append("       TM_VHCL_MATERIAL TVM\n");  
		sql.append(" WHERE TVO.ORDER_ID = TVD.ORDER_ID\n");  
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n");  
		sql.append("   AND TVDD.DETAIL_ID = TVDM.DELIVERY_DETAIL_ID\n");  
		sql.append("   AND TVDM.VEHICLE_ID = TV.VEHICLE_ID\n");  
		sql.append("   AND TV.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVO.ORDER_NO = '" + orderNo + "'");
		
		if(!CommonUtils.isNullString(matId))
			sql.append("   AND tvm.MATERIAL_id = " + matId + "");
		
		sql.append("UNION ALL\n");
		sql.append("SELECT TMV.VIN,\n");  
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TMV.NODE_CODE,\n");  
		sql.append("       TMV.BATCH_NO,\n");  
		sql.append("       TO_CHAR(TMV.NODE_DATE, 'YYYY-MM-DD HH24:MI:SS') NODE_DATE\n");  
		sql.append("  FROM TT_VS_ORDER        TVO,\n");  
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");  
		sql.append("       TT_VS_SC_MATCH     TVSM,\n");  
		sql.append("       TM_VEHICLE         TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL   TVM\n");  
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");  
		sql.append("   AND TVOD.DETAIL_ID = TVSM.ORDER_DETAIL_ID\n");  
		sql.append("   AND TVSM.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVO.ORDER_NO = '").append(orderNo).append("'\n");
		
		if(!CommonUtils.isNullString(matId))
			sql.append("   AND tvm.MATERIAL_id = " + matId + "");


		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public List<Map<String, Object>> getMatchList2(Map<String, Object> map) {
		String deliveryId = (String)map.get("deliveryId");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TV.VIN,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TV.NODE_CODE,\n");  
		sql.append("       TV.BATCH_NO,\n");  
		sql.append("       TO_CHAR(TV.NODE_DATE, 'YYYY-MM-DD HH24:MI:SS') NODE_DATE\n");  
		sql.append("  FROM TT_VS_ORDER      TVO,\n");  
		sql.append("       TT_VS_DLVRY      TVD,\n");  
		sql.append("       TT_VS_DLVRY_DTL  TVDD,\n");  
		sql.append("       TT_VS_DLVRY_MCH  TVDM,\n");  
		sql.append("       TM_VEHICLE       TV,\n");  
		sql.append("       TM_VHCL_MATERIAL TVM\n");  
		sql.append(" WHERE TVO.ORDER_ID = TVD.ORDER_ID\n");  
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n");  
		sql.append("   AND TVDD.DETAIL_ID = TVDM.DELIVERY_DETAIL_ID\n");  
		sql.append("   AND TVDM.VEHICLE_ID = TV.VEHICLE_ID\n");  
		sql.append("   AND TV.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVD.DELIVERY_ID = " + deliveryId + "\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getRemark(String orderId,String orderNo, String dealerId) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvdr.dlvry_req_no, --发运申请编号\n");
		sql.append("       tvdr.req_status,  --发运申请状态\n");  
		sql.append("       to_char(tvdr.create_date,'yyyy-mm-dd') req_date,  --发运申请状态\n");  
		sql.append("       decode(tvdr.call_leavel, ?, rod.dealer_code, ood.dealer_code) DCODE, --采购单位代码\n");  
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("       decode(tvdr.call_leavel, ?, rod.dealer_shortname, ood.dealer_shortname) DSNAME, --采购单位简称\n");  
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("       tvdr.req_remark --备注\n");  
		sql.append("  from tt_vs_dlvry_req tvdr, tt_vs_order tvo, tm_dealer ood, tm_dealer rod\n");  
		sql.append(" where tvdr.order_id = tvo.order_id\n");  
		sql.append("   and tvdr.order_dealer_id = rod.dealer_id(+)\n");  
		sql.append("   and tvo.order_org_id = ood.dealer_id\n");  
		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append("   and (tvo.order_org_id in (").append(dealerId).append(") or tvdr.order_dealer_id in (").append(dealerId).append(") or tvo.billing_org_id in (").append(dealerId).append("))\n");
		}
		
		if(!CommonUtils.isNullString(orderId)) {
			sql.append("   and tvdr.order_id = ?\n");
			params.add(orderId) ;
		}
		
		if(!CommonUtils.isNullString(orderNo)) {
			sql.append("   and tvo.order_no = ?\n");
			params.add(orderNo) ;
		}
		
		sql.append("   order by to_char(tvdr.create_date,'yyyy-mm-dd'), decode(tvdr.call_leavel, ?, rod.dealer_code, ood.dealer_code), tvdr.dlvry_req_no\n");
		params.add(Constant.DEALER_LEVEL_02) ;
		
		List<Map<String, Object>> remarkList = dao.pageQuery(sql.toString(), params, dao.getFunName()) ;

		return remarkList ;
	}
	
	public List<Map<String, Object>> getRemark(String orderId,String orderNo) {
		return getRemark(orderId, orderNo, null) ;
	}
}
