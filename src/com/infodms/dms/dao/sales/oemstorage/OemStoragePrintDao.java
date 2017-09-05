package com.infodms.dms.dao.sales.oemstorage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class OemStoragePrintDao  extends BaseDao {
	public static Logger logger = Logger.getLogger(OemStorageManageDao.class);
	private static final OemStoragePrintDao dao=new OemStoragePrintDao();
	public static final OemStoragePrintDao getInstance(){
		return dao;
	}
	
	public String getWareHouseAreaSql(String companyId, String areaId, String tableName){
		StringBuffer sql = new StringBuffer();

		sql.append("(SELECT DISTINCT TW.WAREHOUSE_ID, TW.WAREHOUSE_NAME, TW.WAREHOUSE_TYPE\n");
		sql.append("  FROM TM_WAREHOUSE TW, TM_BUSINESS_AREA TBA\n");  
		sql.append(" WHERE TW.ENTITY_CODE = TBA.ERP_CODE\n");  
		sql.append("   AND TW.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND TW.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND TBA.AREA_ID IN ("+areaId+")) "+tableName+"");

		return sql.toString();
	}
	
	public PageResult<Map<String, Object>> getCommandPrintList(String startDate,String endDate,String orderNo, String printFlag, Long companyId, String areaIds, int curPage,int pageSize) throws Exception{
		List <Object>params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT  A.STO_ID,A.ORDER_NO, --移库单号\n" );
		sql.append("b.WAREHOUSE_NAME TO_NAME,--目的仓库\n" );
		sql.append("TO_CHAR(A.STO_DATE, 'YYYY-MM-DD') STO_DATE,--发运日期\n" );
		sql.append("A.ERP_ORDER_NO--ERP订单号\n" );
		sql.append("FROM TT_STO A, "+getWareHouseAreaSql(companyId.toString(), areaIds, "B")+"\n" );
		sql.append("where a.to_warehouse_id=b.warehouse_id\n" );
		if(!"".equals(startDate)&&startDate!=null){
		sql.append("and a.sto_date>= TO_DATE('"+startDate+"', 'YYYY-MM-DD HH24:MI:SS') --移库日期\n" );
		}
		if(!"".equals(endDate)&&endDate!=null){
		sql.append("and a.sto_date<= TO_DATE('"+endDate+"', 'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(!"".equals(orderNo)&&orderNo!=null){
		sql.append("AND A.Order_No like '%"+orderNo+"%'  --移库单号\n" );
		}
		sql.append("AND A.STATUS="+Constant.STO_STATUS_02); 
		if(Utility.testString(printFlag)&&Integer.parseInt(printFlag)!=1){
		sql.append("AND (A.PRINT_FLAG="+printFlag+"  OR A.PRINT_FLAG IS NULL) --是否打印\n" );
		}else{
			sql.append("AND A.PRINT_FLAG="+printFlag);
		}
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public Map<String, Object> getstoInfoMap(String reqId){
		StringBuffer sql= new StringBuffer();
		/*sql.append("select  ts1.erp_order_no,ts1.order_no,td.dealer_name,tmo.org_name,tva.address,tva.tel,tsd.batch_no,tsd.amount,tsd.single_price,tw1.warehouse_name,to_char(ts1.sto_date,'yyyy-mm-dd') sto_date,TAXES_NO\n");
		sql.append("from tt_sto                 ts,\n");
		sql.append("tt_sto                 ts1,\n");
		sql.append("tt_sto_dtl             tsd,\n");
		sql.append("tm_warehouse           tw,\n");
		sql.append("tm_dealer              td,\n");
		sql.append("tm_org                 tmo,\n");
		sql.append("tm_dealer_org_relation tdor,\n");
		sql.append("tm_vs_address tva,\n");
		sql.append("tm_warehouse tw1\n");
		sql.append("where ts.to_warehouse_id=tw.warehouse_id\n");
		sql.append("and ts.sto_id=tsd.sto_id\n");
		sql.append("and  tva.dealer_id=td.dealer_id\n");
		sql.append("and tw.dealer_id=td.dealer_id\n");
		sql.append("and tmo.org_id=tdor.org_id\n");
		sql.append("and tdor.dealer_id=td.dealer_id\n");
		sql.append("and tw1.warehouse_id = ts1.from_warehouse_id\n");
		sql.append("and ts.sto_id="+reqId+"\n");
		sql.append("and ts1.sto_id="+reqId+"\n");*/

		sql.append("select ts.erp_order_no,\n");
		sql.append("       ts.order_no,\n");  
		sql.append("       td.dealer_name,\n");  
		sql.append("       tva.address,\n");  
		sql.append("       tva.tel,\n");  
		sql.append("       tsd.batch_no,\n");  
		sql.append("       tsd.amount,\n");  
		sql.append("       tsd.single_price,\n");  
		sql.append("       tw1.warehouse_name,\n");  
		sql.append("       to_char(ts.sto_date, 'yyyy-mm-dd') sto_date,\n");  
		sql.append("       TAXES_NO\n");  
		sql.append("  from tt_sto        ts,\n");  
		sql.append("       tt_sto_dtl    tsd,\n");  
		sql.append("       tm_dealer     td,\n");  
		sql.append("       tm_warehouse  tw,\n");  
		sql.append("       tm_vs_address tva,\n");  
		sql.append("       tm_warehouse  tw1\n");  
		sql.append(" where ts.to_warehouse_id = tw.warehouse_id\n");  
		sql.append("   and ts.sto_id = tsd.sto_id\n");  
		sql.append("   and tva.dealer_id(+) = td.dealer_id\n");  
		sql.append("   and tw.dealer_id = td.dealer_id\n");  
		sql.append("   and tw1.warehouse_id = ts.from_warehouse_id\n");  
		sql.append("   and ts.sto_id = "+reqId+"");

		Map<String, Object> map= dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;
		
	}
/*select ts.order_no, tw.warehouse_name, ts.sto_date
  from tt_sto ts, tm_warehouse tw
 where tw.warehouse_id = ts.from_warehouse_id
 * 查询发运单数据信息
 */
	
	public Map<String, Object> getdeliveryInfoMap(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT NVL(A.VER, 0) VER, A.REQ_ID, B.AREA_NAME, E.ORG_NAME, F.WAREHOUSE_NAME,\n" );
		sql.append("       D.ORDER_NO, D.ORDER_TYPE, A.DELIVERY_TYPE, D.ORDER_ID, A.FUND_TYPE, \n" );
		sql.append("       D.ORDER_YEAR||'年'||D.ORDER_WEEK||'周' ORDER_WEEK,A.ADDRESS_ID, \n" );
		sql.append("       G.DEALER_SHORTNAME DNAME1, H.DEALER_SHORTNAME DNAME2, H.DEALER_TYPE, H.TAXES_NO, \n" );
		sql.append("       N.TYPE_NAME ACCOUNT_NAME, A.PRICE_ID, NVL(A.OTHER_PRICE_REASON, '') OTHER_PRICE_REASON, \n" );
		sql.append("       TO_CHAR(I.AVAILABLE_AMOUNT, 'FM9999,999,999,990.00') AVAILABLE_AMOUNT,\n" );
		sql.append("       TO_CHAR(A.DISCOUNT, 'FM9999,999,999,990.00') DISCOUNT,\n" );
		sql.append("       NVL(A.DISCOUNT, 0) DCOUNT, D.ORDER_ORG_ID, O.ERP_ORDER,\n" );
		sql.append("       J.ADDRESS, J.RECEIVE_ORG, K.DEALER_SHORTNAME DNAME3, A.RECEIVER, \n" );
		sql.append("       NVL(J.LINK_MAN, '') LINK_MAN, NVL(J.TEL, '') TEL, L.PRICE_DESC, DECODE(D.IS_FLEET, 1,'是', '否') IS_FLEET,\n" );
		sql.append("       A.FLEET_ADDRESS, M.FLEET_NAME, D.REFIT_REMARK, D.ORDER_REMARK,\n" );
		sql.append("	   TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI') SDATE, D.BILLING_ORG_ID \n");
		sql.append("FROM TT_VS_DLVRY_REQ A, TM_BUSINESS_AREA B,\n" );
		sql.append("     TM_DEALER_ORG_RELATION C, TT_VS_ORDER D,\n" );
		sql.append("     TM_ORG E, TM_WAREHOUSE F, TM_DEALER G,\n" );
		sql.append("     TM_DEALER H, TT_VS_ACCOUNT I, TM_VS_ADDRESS J,\n" );
		sql.append("     TM_DEALER K, vw_TT_VS_PRICE L, TM_FLEET M, TT_VS_ACCOUNT_TYPE N, TT_VS_DLVRY O\n" );
		sql.append("WHERE A.AREA_ID = B.AREA_ID\n" );
		sql.append("AND A.ORDER_ID = D.ORDER_ID\n" );
		sql.append("AND D.BILLING_ORG_ID = C.DEALER_ID(+)\n" );
		sql.append("AND C.ORG_ID = E.ORG_ID(+)\n" );
		sql.append("AND D.BILLING_ORG_ID = G.DEALER_ID\n" );
		sql.append("AND D.ORDER_ORG_ID = H.DEALER_ID\n" );
		sql.append("AND A.FUND_TYPE = N.TYPE_ID\n" );
		sql.append("AND A.FUND_TYPE = I.ACCOUNT_TYPE_ID\n" );
		sql.append("AND D.BILLING_ORG_ID = I.DEALER_ID\n" );
		sql.append("AND A.PRICE_ID = L.PRICE_ID\n" );
		sql.append("AND A.WAREHOUSE_ID = F.WAREHOUSE_ID(+)\n");
		sql.append("AND A.ADDRESS_ID = J.ID(+)\n" );
		sql.append("AND A.FLEET_ID = M.FLEET_ID(+)\n" );
		sql.append("AND A.RECEIVER = K.DEALER_ID\n" );
		sql.append("AND A.REQ_ID = O.REQ_ID(+)\n" );
		sql.append("AND A.REQ_ID = ").append(id).append("\n"); 
		sql.append("ORDER BY O.DELIVERY_DATE ASC\n");
		
		Map<String, Object> map= dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	public List<Map<String, Object>> getstorageResourceReserveDetailList(String reqId){
		StringBuffer sql= new StringBuffer();
		sql.append("select tsd.batch_no,tsd.amount,tsd.single_price,tsd.total_price,tvm.material_code,\n" );
		sql.append("tvm.color_name from  tt_sto_dtl  tsd,tm_vhcl_material tvm where tsd.sto_id="+reqId+" and  tsd.material_id = tvm.material_id\n" );
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getorderResourceReserveDetailList(String reqId,String companyId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT tvdrd.detail_id,\n" );
		sql.append("       tvdrd.material_id,\n" );
		sql.append("       tvm.material_code,\n" );
		sql.append("       tvm.material_name,\n" );
		sql.append("       tvm.COLOR_NAME,\n" );
		sql.append("       tvdrd.patch_no special_batch_no,\n" );
		sql.append("       nvl(orr.amount, 0) delivery_amount,\n" );
		sql.append("       tvdrd.order_detail_id,\n" );
		sql.append("       nvl(tvdrd.reserve_amount, 0) reserve_amount,\n" );
		sql.append("       nvl(tvdrd.single_price, 0) single_price,\n" );
		sql.append("       nvl(orr.amount, 0) * nvl(tvdrd.single_price, 0) total_price,\n" );
		sql.append("       nvl(tvdrd.discount_rate, 0) discount_rate,\n" );
		sql.append("       nvl(tvdrd.discount_s_price, 0) discount_s_price,\n" );
		sql.append("	   nvl(orr.amount, 0) * nvl(tvdrd.single_price, 0) *\n" );
		sql.append("	   nvl(tvdrd.discount_rate, 0) / 100 discount_price,\n");
		sql.append("       tvdrd.ver,\n" );
		sql.append("       nvl(vvr.ava_stock, 0) ava_stock,\n" );
		sql.append("       nvl(torder.n_un_remain_amount, 0) general_amount,\n" );
		sql.append("       orr.batch_no\n" );
		sql.append("  FROM tt_vs_order_detail tvod,\n" );
		sql.append("       tt_vs_dlvry_req tvdr,\n" );
		sql.append("       tt_vs_dlvry_req_dtl tvdrd,\n" );
		sql.append("       tm_vhcl_material tvm,\n" );
		sql.append("       tt_vs_order_resource_reserve orr,\n" );
		sql.append("       (SELECT temp.material_id,\n" );
		sql.append("               SUM(temp.ava_stock) ava_stock\n" );
		sql.append("          FROM vw_vs_resource temp\n" );
		sql.append("         WHERE temp.company_id = ").append(companyId).append("\n");
		sql.append("         GROUP BY material_id) vvr,\n" );
		sql.append("       (select vvo.material_id, vvo.n_un_remain_amount\n");
		sql.append("          from vw_vs_order vvo\n");  
		sql.append("         where vvo.company_id = "+companyId+") torder");
		sql.append(" WHERE tvdr.req_id = tvdrd.req_id\n" );
		sql.append("   AND orr.req_detail_id(+) = tvdrd.detail_id\n" );
		sql.append("   AND tvod.detail_id = tvdrd.order_detail_id\n" );
		sql.append("   AND tvdrd.material_id = tvm.material_id\n" );
		sql.append("   AND tvdrd.material_id = vvr.material_id(+)\n" );
		sql.append("   AND tvdrd.material_id = torder.material_id(+)\n" );
		sql.append("   AND tvdrd.req_id = ").append(reqId).append("\n");
		
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
