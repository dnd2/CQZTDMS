/**********************************************************************
 * <pre>
 * FILE : OrderDeliveryDao.java
 * CLASS : OrderDeliveryDao
 * AUTHOR : 
 * FUNCTION : 订单发运
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2010-05-21|            | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.dao.sales.ordermanage.delivery;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TmVsAddressBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TmVsAddressPO;
import com.infodms.dms.po.TtVsDsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 订单发运DAO
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-21
 * @author
 * @mail
 * @version 1.0
 * @remark
 */
public class DsOrderDeliveryDao extends BaseDao {

	public static Logger logger = Logger.getLogger(DsOrderDeliveryDao.class);
	private static final DsOrderDeliveryDao dao = new DsOrderDeliveryDao();

	public static final DsOrderDeliveryDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	

	/**
	 * Function : 电商订单真实发运查询数据
	 * 
	 * @param :
	 *            
	 * @param :
	 *            
	 * @param :
	 *            车系
	 * @return : 满足条件的常规订单信息
	 * @throws :
	 *             Exception LastUpdate : 2010-05-24
	 */
	public List<Map<String, Object>> getDeliveryApplyList(String orderYear, String orderWeek, String areaId, String dealerId, Long oemCompanyId, String orderNO) throws Exception {
		StringBuilder sql= new StringBuilder();
		sql.append("select td.dealer_code,\n" );
		sql.append("       td.dealer_name,\n" );
		sql.append("       tvmg2.group_name series_name,\n" );
		sql.append("       tvm.material_id,\n" );
		sql.append("       tvm.material_code,\n" );
		sql.append("       tvm.material_name,\n" );
		sql.append("       tvm.color_name,\n" );
		sql.append("       vds.RESOUCE_COUNT ,\n" );
		sql.append("       nvl(vdd.req_amount,0) req_amount,\n" );
		sql.append("       vds.resouce_count-nvl(vdd.req_amount,0) ABLE_APPLY,\n" );
		//sql.append("       // F_GET_MATPRICE(1328,TVMG.GROUP_CODE)SINGLE_PRICE, ---物料单价\n" );
		//sql.append("       //F_GET_PRICELISTID(1328,TVMG.GROUP_CODE) PRICTLIST_ID ---物料单价id\n" );
		sql.append("        '13877' SINGLE_PRICE, ---物料单价\n" );
		sql.append("       '1157' PRICTLIST_ID ---物料单价id\n" );
		sql.append("  from view_dealer_source       vds,\n" );
		sql.append("       tm_dealer                td,\n" );
		sql.append("       tm_vhcl_material         tvm,\n" );
		sql.append("       tm_vhcl_material_group   tvmg,\n" );
		sql.append("       tm_vhcl_material_group_r tvmgr,\n" );
		sql.append("       tm_vhcl_material_group   tvmg1,\n" );
		sql.append("       tm_vhcl_material_group   tvmg2,\n" );
		sql.append("       view_dealer_delvy      vdd\n" );
		sql.append(" where vds.ds_dealer_id = td.dealer_id\n" );
		sql.append("   and vds.material_id = tvm.material_id\n" );
		sql.append("   and tvmgr.group_id = tvmg.group_id\n" );
		sql.append("   and tvmgr.material_id = tvm.material_id\n" );
		sql.append("   and tvmg1.group_id = tvmg.parent_group_id\n" );
		sql.append("   and tvmg2.group_id = tvmg1.parent_group_id\n" );
		sql.append("   and vdd.material_id(+)=vds.material_id\n" );
		sql.append("   and vdd.dealer_id(+)=vds.ds_dealer_id\n" );
		sql.append("   and td.dealer_id='" + dealerId + "'");
		if (!"".equals(orderYear) && orderYear != null) {
			//sql.append(" AND TSO.ORDER_YEAR = '" + orderYear + "'\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			//sql.append(" AND TSO.ORDER_WEEK = '" + orderWeek + "'\n");
		}
		if (!"-1".equals(areaId) && !"".equals(areaId) && areaId != null) {
			//sql.append(" AND TSO.AREA_ID =" + areaId + "\n");
		}
		if (!CommonUtils.isNullString(orderNO)) {
			//sql.append(" AND TSO.ORDER_NO = '" + orderNO + "'\n");
		}
		sql.append(" and (vds.resouce_count-nvl(vdd.req_amount,0))>0 ");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * Function : 电商订单真实发运查询数据
	 * 
	 * @param :
	 *            
	 * @param :
	 *            
	 * @param :
	 *            车系
	 * @return : 满足条件的常规订单信息
	 * @throws :
	 *             Exception LastUpdate : 2010-05-24
	 */
	public void sendDataToErp(TtVsDsDlvryReqPO tvdrpo,TtVsDsDlvryReqDtlPO tvdrdpo,AclUserBean logonUser,int lineNumber) throws Exception {
		//获取customer_name 
		TmDealerPO td =new TmDealerPO();
		td.setDealerId(new Long(logonUser.getDealerId()));
		td=(TmDealerPO) dao.select(td).get(0);
		//获取item_code
		TmVhclMaterialPO tvm=new TmVhclMaterialPO();
		tvm.setMaterialId(tvdrdpo.getMaterialId());
		tvm=(TmVhclMaterialPO) dao.select(tvm).get(0);
		TmVsAddressPO tva=new TmVsAddressPO();
		//tva.setDealerId(new Long(logonUser.getDealerId()));
		tva.setDealerId(tvdrpo.getReceiver());
		tva=(TmVsAddressPO) dao.select(tva).get(0);
		
		StringBuilder sql= new StringBuilder();
		sql.append("insert into CUX_ERP_ORDERS_DELIVER_1@DMS2EBS2\n" );
		sql.append("  (seq_id,\n" );
		sql.append("   customer_name,\n" );
		sql.append("   customer_number,\n" );
		sql.append("   ship_to_location,\n" );
		sql.append("   order_number,\n" );
		sql.append("   order_type,\n" );
		sql.append("   order_date,\n" );
		sql.append("   price_list_id,\n" );
		sql.append("   item_code,\n" );
		sql.append("   orig_order_quantity,\n" );
		sql.append("   ordered_quantity,\n" );
		sql.append("   request_date,\n" );
		sql.append("   schedule_ship_date,\n"); //录入时间
		sql.append("   funds_type,\n" );
		sql.append("   plate_number,\n" );
		sql.append("   remark,\n" );
		sql.append("   status,\n" );
		sql.append("   attribute1,\n" );
		sql.append("   attribute2,\n" );
		sql.append("   attribute3,\n" );
		sql.append("   attribute4,\n" );
		sql.append("   attribute5,\n" );
		sql.append("   attribute6,\n" );
		sql.append("   creation_date,\n" );
		sql.append("   table_type,\n" );
		sql.append("   line_number)\n" );
		sql.append("values\n" );
		sql.append("  ("+tvdrdpo.getDetailId()+",\n" );
		sql.append("   '"+td.getDealerName()+"',\n" );
		sql.append("   "+logonUser.getDealerCode()+",\n" );
		sql.append("   "+tva.getAddCode()+",\n" );
		sql.append("   '"+tvdrpo.getDlvryReqNo()+"',\n" );
		sql.append("   'D',\n" );
		sql.append("   sysdate,\n" );
		sql.append("   "+tvdrdpo.getPriceId()+",\n" );
		sql.append("   '"+tvm.getMaterialCode()+"',\n" );//item_code
		sql.append("   "+tvdrdpo.getReqAmount()+",\n" );//orig_order_quanity
		sql.append("   "+tvdrdpo.getReqAmount()+",\n" );//order_quantity
		sql.append("   sysdate,\n" );
		sql.append("   sysdate,\n" );
		sql.append("   "+tvdrpo.getFundType()+",\n" );
		sql.append("   "+tvdrpo.getTmpLicenseAmount()+",\n" );
		sql.append("   '"+tvdrpo.getReqRemark()+"',\n" );
		sql.append("   0,\n" );
		sql.append("   null,\n" );
		sql.append("   null,\n" );
		sql.append("   null,\n" );
		sql.append("   null,\n" );
		sql.append("   null,\n" );
		sql.append("   null,\n" );
		sql.append("   sysdate,\n" );
		sql.append("   'ITEM',\n" );
		sql.append("   "+lineNumber+")");
		update(sql.toString(),null);
	}
	/**
	 * Function : 经销商账户可用余额查询
	 * 
	 * @param :
	 *            经销商ID
	 * @return : 经销商账户信息
	 * @throws :
	 *             Exception LastUpdate : 2010-05-25
	 */
	public List<Map<String, Object>> getDealerAccount(String dealerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TSA.ACCOUNT_ID,TSA.AVAILABLE_AMOUNT, TSA.FREEZE_AMOUNT, TSAT.TYPE_ID,TSAT.TYPE_NAME\n");
		sql.append("  FROM TT_VS_ACCOUNT TSA, TT_VS_ACCOUNT_TYPE TSAT\n");
		sql.append(" WHERE TSA.ACCOUNT_TYPE_ID = TSAT.TYPE_ID\n");
		sql.append("   AND TSA.DEALER_ID IN(" + dealerId + ")\n");
		//sql.append("   AND TSAT.STATUS =" + Constant.STATUS_ENABLE + "\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * Function : 经销商地址查询
	 * 
	 * @param :
	 *            经销商ID
	 * @return : 经销商地址信息
	 * @throws :
	 *             Exception LastUpdate : 2010-05-25
	 */
	public List<Map<String, Object>> getDealerAddress(String dealerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TMA.ID, TMA.ADDRESS\n");
		sql.append("  FROM TM_VS_ADDRESS TMA\n");
		sql.append("   WHERE TMA.DEALER_ID IN(" + dealerId + ")\n");
		sql.append("   AND TMA.STATUS =" + Constant.STATUS_ENABLE + "\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
