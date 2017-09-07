package com.infodms.dms.dao.sales.storage.ontheway;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TtSalesWayBillDtlPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.yxdms.dao.IBaseDao;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class OnTheWayDao extends  IBaseDao {
	private static final OnTheWayDao dao = new OnTheWayDao();
	public static final OnTheWayDao getInstance(){
		if (dao == null) {
			return new OnTheWayDao();
		}
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
    /**
     * 在途位置查询
     * @param request
     * @param loginUser
     * @param pageSize
     * @param currPage
     * @return
     */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getOnTheWayList(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("  SELECT B.LOGI_NAME,\n" );
		sql.append("      A.BILL_NO,\n" );
		sql.append("      TO_CHAR(A.BILL_CRT_DATE, 'yyyy-MM-dd') BILL_CRT_DATE,\n" );
		sql.append("      A.BILL_ID,\n" );
		sql.append("      nvl(C.DEALER_NAME,tm.warehouse_name) DEALER_NAME,\n" );
		sql.append("      a.tel TEL_PHONE,\n" );
		sql.append("      a.siji LINK_MAN,\n" );
		sql.append("      a.address_info  ADDRESS\n" );
		sql.append(" FROM TT_SALES_WAYBILL A, TT_SALES_LOGI B, TM_DEALER C,tm_warehouse  tm\n" );
		sql.append("WHERE A.LOGI_ID = B.LOGI_ID\n" );
		sql.append("  AND A.or_dealer_id = C.DEALER_ID(+)\n" );
		sql.append("  and a.or_dealer_id=tm.warehouse_id(+)");
		sql.append("  and a.send_status !=99981003");//已交车的不要
		
		DaoFactory.getsql(sql, "a.bill_id", DaoFactory.getParam(request, "bill_id"), 1);
		DaoFactory.getsql(sql, "a.bill_no", DaoFactory.getParam(request, "shipNo"), 2);//交接单号
		DaoFactory.getsql(sql, "b.logi_id", DaoFactory.getParam(request, "LOGI_NAME_SEACH"), 2);
		DaoFactory.getsql(sql, "c.dealer_code", DaoFactory.getParam(request, "DEALER_CODE"),6 );
		DaoFactory.getsql(sql, "a.BILL_CRT_DATE", DaoFactory.getParam(request, "startDate"),3 );
		DaoFactory.getsql(sql, "a.BILL_CRT_DATE", DaoFactory.getParam(request, "endDate"),4);
		sql.append(" order by a.bill_id desc");
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
	}
    /**
     * 查询交接单明细
     * @param request
     * @param loginUser
     * @param pageSize
     * @param currPage
     * @return
     */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getTtSalesWayBillDtlpo(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("select  tt.*  from  TT_SALES_WAY_BILL_DTL  tt  where 1=1 ");
        DaoFactory.getsql(sql, "tt.bill_id", DaoFactory.getParam(request, "bill_id"), 1);
		return dao.pageQueryObject(sql.toString(), null, pageSize, currPage);
	}
    /**
     * 
     * @param request
     * @param page_size
     * @param curr_page
     * @return
     */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getTtSalesWaybill(RequestWrapper request, Integer page_size,
			Integer curr_page) {
//		StringBuffer sql= new StringBuffer();
//		sql.append("  SELECT B.LOGI_NAME,\n" );
//		sql.append("      A.BILL_NO,\n" );
//		sql.append("      TO_CHAR(A.BILL_CRT_DATE, 'yyyy-MM-dd') BILL_CRT_DATE,\n" );
//		sql.append("      A.BILL_ID,\n" );
//		sql.append("      nvl(C.DEALER_NAME,tm.warehouse_name) DEALER_NAME,\n" );
//		sql.append("      a.tel PHONE,\n" );
//		sql.append("      a.siji LINK_MAN,\n" );
//		sql.append("      a.address_info  ADDRESS\n" );
//		sql.append(" FROM TT_SALES_WAYBILL A, TT_SALES_LOGI B, TM_DEALER C,tm_warehouse  tm\n" );
//		sql.append("WHERE A.LOGI_ID = B.LOGI_ID\n" );
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT distinct B.LOGI_NAME,\n" );
		sql.append("                A.BILL_NO,\n" );
		sql.append("                TO_CHAR(A.BILL_CRT_DATE, 'yyyy-MM-dd') BILL_CRT_DATE,\n" );
		sql.append("                A.BILL_ID,\n" );
		sql.append("                nvl(C.DEALER_NAME, tm.warehouse_name) DEALER_NAME,\n" );
		sql.append("                decode(a.tel, null, ts.driver_tel, a.tel) PHONE,\n" );
		sql.append("                decode(a.siji, null, ts.driver_name, a.siji) LINK_MAN,\n" );
		sql.append("                a.address_info ADDRESS\n" );
		sql.append("  FROM TT_SALES_WAYBILL   A,\n" );
		sql.append("       TT_SALES_LOGI      B,\n" );
		sql.append("       TM_DEALER          C,\n" );
		sql.append("       tm_warehouse       tm,\n" );
		sql.append("       tt_sales_bo_detail tsb,\n" );
		sql.append("       tt_sales_board     ts\n" );
		sql.append(" WHERE A.LOGI_ID = B.LOGI_ID\n" );
		sql.append("   and tsb.bill_id = a.bill_id\n" );
		sql.append("   and tsb.bo_id = ts.bo_id\n" );
		sql.append("  AND A.or_dealer_id = C.DEALER_ID(+)\n" );
		sql.append("  and a.or_dealer_id=tm.warehouse_id(+)");
		sql.append("  and a.send_status !=99981003");//已交车的不要
		DaoFactory.getsql(sql, "a.bill_id", DaoFactory.getParam(request, "bill_id"), 1);
		DaoFactory.getsql(sql, "b.logi_id", DaoFactory.getParam(request, "logi_id"), 1);
		sql.append("  order by a.bill_id desc ");
		return dao.pageQuery(sql.toString(), null, getFunName(), page_size, curr_page);
	}
    /**
     * 获取交接单明细
     * @param request
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTtSalesWayBillDtl(RequestWrapper request) {
		StringBuffer sql= new StringBuffer();
		sql.append("select d.*,\n" );
		sql.append("       mt.SERIES_CODE,\n" );
		sql.append("       mt.SERIES_NAME,\n" );
		sql.append("       mt.MODEL_NAME,\n" );
		sql.append("       mt.COLOR_NAME,\n" );
		sql.append("       mt.MATERIAL_CODE,\n" );
		sql.append("       mt.MATERIAL_NAME\n" );
		sql.append("  from tt_sales_way_bill_dtl d, Vw_Material_Group_Mat mt\n" );
		sql.append(" where mt.MATERIAL_ID = d.mat_id");

		DaoFactory.getsql(sql, "d.bill_id", DaoFactory.getParam(request, "bill_id"), 1);
		DaoFactory.getsql(sql, "d.dtl_id", DaoFactory.getParam(request, "dtl_id"), 1);
		return dao.pageQuery(sql.toString(), null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMateriaDetail(String billId) {
		StringBuffer sql= new StringBuffer();
		sql.append("select VMT.SERIES_NAME,\n" );
		sql.append("       VMT.PACKAGE_NAME,\n" );
		sql.append("       a.vin,\n" );
		sql.append("       a.status,\n" );
		sql.append("       e.order_no,\n" );
		sql.append("       tsw.address_info address,\n" );
		sql.append("       a.dtl_id,\n" );
		sql.append("       F_GET_WAY_ADDRESS(dtl_id) this_address\n" );
		sql.append("  from tt_sales_way_bill_dtl a,\n" );
		sql.append("       TT_SALES_WAYBILL      tsw,\n" );
		sql.append("       tt_sales_bo_detail    e,\n" );
		sql.append("\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT VMT\n" );
		sql.append(" where a.order_detail_id = e.or_de_id\n" );
		sql.append("   and a.bill_id = tsw.bill_id\n" );
		sql.append("   and e.bill_id = tsw.bill_id\n" );
		sql.append("   and e.mat_id = vmt.MATERIAL_ID");

		sql.append("   and A.BILL_ID = "+billId+"\n");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
    /**
     * 根据交接单id查询交接单
     * @param bill_id
     * @return
     */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getTtSalesWaybillByBillId(RequestWrapper request) {
		StringBuffer sql= new StringBuffer();
//		sql.append("SELECT B.LOGI_NAME,\n" );
//		sql.append("       A.BILL_NO,\n" );
//		sql.append("       TO_CHAR(A.BILL_CRT_DATE, 'yyyy-MM-dd') BILL_CRT_DATE,\n" );
//		sql.append("       A.BILL_ID,\n" );
//		sql.append("       nvl(C.DEALER_NAME, tm.warehouse_name) DEALER_NAME,\n" );
//		sql.append("       decode(a.tel, null, ts.driver_tel, a.tel) TEL_PHONE,\n" );
//		sql.append("       decode(a.siji, null, ts.driver_name, a.siji) LINK_MAN,\n" );
//		sql.append("       a.address_info ADDRESS\n" );
//		sql.append("  FROM TT_SALES_WAYBILL   A,\n" );
//		sql.append("       TT_SALES_LOGI      B,\n" );
//		sql.append("       TM_DEALER          C,\n" );
//		sql.append("       tm_warehouse       tm,\n" );
//		sql.append("       tt_sales_bo_detail tsb,\n" );
//		sql.append("       tt_sales_board     ts\n" );
//		sql.append(" WHERE A.LOGI_ID = B.LOGI_ID\n" );
//		sql.append("   and tsb.bill_id = a.bill_id\n" );
//		sql.append("   and tsb.bo_id = ts.bo_id");
		sql.append("SELECT B.LOGI_NAME,\n" );
		sql.append("       A.BILL_NO,\n" );
		sql.append("       TO_CHAR(A.BILL_CRT_DATE, 'yyyy-MM-dd') BILL_CRT_DATE,\n" );
		sql.append("       A.BILL_ID,\n" );
		sql.append("       nvl(C.DEALER_NAME, tm.warehouse_name) DEALER_NAME,\n" );
		sql.append("       decode(a.tel, null, ts.driver_tel, a.tel) TEL_PHONE,\n" );
		sql.append("       decode(a.siji, null, ts.driver_name, a.siji) LINK_MAN,\n" );
		sql.append("       a.address_info ADDRESS\n" );
		sql.append("  FROM TT_SALES_WAYBILL   A,\n" );
		sql.append("       TT_SALES_LOGI      B,\n" );
		sql.append("       TM_DEALER          C,\n" );
		sql.append("       tm_warehouse       tm,\n" );
		sql.append("       tt_sales_bo_detail tsb,\n" );
		sql.append("       tt_sales_board     ts\n" );
		sql.append(" WHERE A.LOGI_ID = B.LOGI_ID\n" );
		sql.append("   and tsb.bill_id = a.bill_id\n" );
		sql.append("   and tsb.bo_id = ts.bo_id\n");
		sql.append("  AND A.or_dealer_id = C.DEALER_ID(+)\n" );
		sql.append("  and a.or_dealer_id=tm.warehouse_id(+)");
		DaoFactory.getsql(sql, "a.bill_id", DaoFactory.getParam(request, "bill_id"), 1);
		sql.append("  order by a.bill_id desc ");
		return dao.pageQueryMap(sql.toString(), null, getFunName());
	}
    /**
     * 查询在途位置信息
     * @param request
     * @param loginUser
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> showOntheWayAddress(RequestWrapper request, AclUserBean loginUser) {
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("select  t.*,f_get_tccode_desc(t.is_sub) is_sub_name  from  Tt_Sales_Way_Bill_Address t\n" );
		sql.append(" where 1=1\n" );
		DaoFactory.getsql(sql, "t.dtl_id", DaoFactory.getParam(request, "dtl_id"), 1);
		sql.append("order by address_date desc");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
    /**
     * 修改交接单明细状态
     * @param dtlPO
     */
	@SuppressWarnings("unchecked")
	public void updateTtSalesWayBillDtlPO(TtSalesWayBillDtlPO dtlPO) {
		TtSalesWayBillDtlPO billDtlPO = new  TtSalesWayBillDtlPO();
		billDtlPO.setDtlId(dtlPO.getDtlId());
		dao.update(billDtlPO, dtlPO);
	}
    /**
     * 批量修改交接单明细
     * @param listTtSalesWayBillDtlPO
     */
	@SuppressWarnings("unchecked")
	public void updateTtSalesWayBillDtlPOList(List<TtSalesWayBillDtlPO> listTtSalesWayBillDtlPO) {
		if(!CommonUtils.isNullList(listTtSalesWayBillDtlPO) &&listTtSalesWayBillDtlPO.size()>0){
			TtSalesWayBillDtlPO dtlPO;
			for (TtSalesWayBillDtlPO ttSalesWayBillDtlPO : listTtSalesWayBillDtlPO) {
				dtlPO = new TtSalesWayBillDtlPO();
				dtlPO.setDtlId(ttSalesWayBillDtlPO.getDtlId());
				dao.update(dtlPO, ttSalesWayBillDtlPO);
			}
		}
	}
    /**
     * 
     * @param bill_id
     * @param i 
     * @param waybillDtlStatus01
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<TtSalesWayBillDtlPO> getTtSalesWayBillDtlPObyStatusAndbillId(String bill_id,
			Integer Status, int i) {
		StringBuffer sql= new StringBuffer();
		sql.append("select  t.*  from  tt_sales_way_bill_dtl  t  where 1=1");
        DaoFactory.getsql(sql, "t.bill_id", bill_id, 1);
        DaoFactory.getsql(sql, "t.status", String.valueOf(Status), 1);
        if(i==1){
        	sql.append(" and t.status is null ");
        }
		return dao.select(TtSalesWayBillDtlPO.class, sql.toString(), null);
	}
    /**
     * 修改交接单
     * @param salesWaybillPO
     */
	@SuppressWarnings("unchecked")
	public void updateTtSalesWaybillPO(TtSalesWaybillPO salesWaybillPO) {
		TtSalesWaybillPO po = new TtSalesWaybillPO();
		po.setBillId(salesWaybillPO.getBillId());
		dao.update(po, salesWaybillPO);
	}

	@SuppressWarnings("unchecked")
	public void updateTtSalesWayBillDtlPOSql(TtSalesWayBillDtlPO dtlPO) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" update tt_sales_way_bill_dtl t set t.status =null ,t.update_by='"+dtlPO.getUpdateBy()+"',t.update_date=sysdate where t.dtl_id = '"+dtlPO.getDtlId()+"'");
		dao.update(buffer.toString(), null);
	}
	
	
	/**
	 *  在途位置维护取得物流商
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  List<Map<String, Object>> getLogiName(){
		List<Map<String, Object>> list = null;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSL.LOGI_CODE,TSL.LOGI_NAME,LOGI_ID FROM TT_SALES_LOGI TSL\n" );
		sql.append(" WHERE TSL.STATUS = "+Constant.STATUS_ENABLE+"\n" );
		list=dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
    /**
     * 在途位置查询
     * @param request
     * @param loginUser
     * @param pageSize
     * @param currPage
     * @return
     */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getCarFactoryOnTheWayList(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("select VMT.SERIES_NAME,\n" );
		sql.append("       VMT.PACKAGE_NAME,\n" );
		sql.append("       a.vin,\n" );
		sql.append("       vmt.MODEL_NAME,\n" );
		sql.append("       a.report_address,\n" );
		sql.append("       a.report_date,\n" );
		sql.append("       a.driver_bind_date,\n" );
		sql.append("       DECODE(a.driver_phone,NULL,bo.driver_tel,a.driver_phone) driver_phone,\n" );
		sql.append("       e.order_no,\n" );
		sql.append("        f_get_tccode_desc(a.status) STATUS_NAME,\n" );
		sql.append("       tsw.bill_id,tsw.bill_no,\n" );
		sql.append("       bo.bo_no,\n" );
		sql.append("       tsw.address_info ADDRESS,\n" );
//		sql.append("       F.ADDRESS,\n" );
		sql.append("       a.dtl_id,\n" );
		sql.append("       a.status,\n" );
		sql.append("       vmt.COLOR_NAME,\n" );
		sql.append("       vmt.MATERIAL_NAME,\n" );
		sql.append("       nvl(d.dealer_shortname,tm.WAREHOUSE_NAME) dealer_name\n" );
		sql.append("  from tt_sales_way_bill_dtl a,\n" );
		sql.append("       TT_SALES_WAYBILL      tsw,\n" );
		sql.append("       tt_sales_bo_detail    e,\n" );
		sql.append("       tt_sales_board  bo,\n" );
//		sql.append("       TM_VS_ADDRESS         f,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT VMT,\n" );
		sql.append("       tm_dealer  d,\n" );
		sql.append("       tm_warehouse  tm\n" );
		sql.append("\n" );
		sql.append(" where a.order_detail_id = e.or_de_id\n" );
		sql.append("   and e.bo_id=bo.bo_id\n" );
		sql.append("   and a.bill_id = tsw.bill_id\n" );
		sql.append("   and a.mat_id = e.mat_id\n" );
		sql.append("   and e.mat_id = vmt.MATERIAL_ID\n" );
//		sql.append("   and tsw.address_id = f.id\n" );
		sql.append("   and tsw.or_dealer_id=d.dealer_id(+)");
		sql.append("   and tsw.or_dealer_id=tm.warehouse_id(+)");
		sql.append(" and tsw.logi_id in  ( select logi_id from tc_pose where pose_id ='"+loginUser.getPoseId()+"'  )");
//		车架号、司机手机号、组板号、交接单号、绑定日期
        DaoFactory.getsql(sql, "a.vin", DaoFactory.getParam(request, "vin"), 2);
        DaoFactory.getsql(sql, "e.order_no", DaoFactory.getParam(request, "order_no"), 2);
        DaoFactory.getsql(sql, "a.driver_phone", DaoFactory.getParam(request, "driver_phone"), 2);
        DaoFactory.getsql(sql, "bo.bo_no", DaoFactory.getParam(request, "bo_no"), 2);
        DaoFactory.getsql(sql, "tsw.bill_no", DaoFactory.getParam(request, "bill_no"), 2);
        DaoFactory.getsql(sql, "a.driver_bind_date", DaoFactory.getParam(request, "driver_bind_date_s"), 3);
        DaoFactory.getsql(sql, "a.driver_bind_date", DaoFactory.getParam(request, "driver_bind_date_e"), 4);
        sql.append("order by tsw.bill_no,e.order_no,a.report_date");
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
	}
    /**
     * 车厂查询在途
     * @param request
     * @param loginUser
     * @param pageSize
     * @param currPage
     * @return
     */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getCarFactoryOnTheWayListSGM(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage) {
		
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT tw.warehouse_id,count(distinct tw.warehouse_id) over () warehouse_count \n" );
		sb.append("  FROM tc_pose p, tm_pose_business_area tba, tm_warehouse tw\n" );
		sb.append(" WHERE p.pose_id = tba.pose_id\n" );
		sb.append("   AND tba.area_id = tw.area_id");
		sb.append(" and p.pose_id='"+loginUser.getPoseId()+"'");
        List<Map<String, Object>> pageQuery = dao.pageQuery(sb.toString(), null, getFunName());
//        BigDecimal decimal = BigDecimal.
        Long warehouse_count =  Long.valueOf(pageQuery.get(0).get("WAREHOUSE_COUNT").toString()) ;
		
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("select VMT.SERIES_NAME,\n" );
		sql.append("       VMT.PACKAGE_NAME,\n" );
		sql.append("       a.vin,\n" );
		sql.append("       bo.DLV_FY_DATE,\n" );
		sql.append("       vmt.MODEL_NAME,\n" );
		sql.append("       a.report_address,\n" );
		sql.append("       a.report_date,\n" );
		sql.append("       a.driver_bind_date,\n" );
		sql.append("       DECODE(a.driver_phone,NULL,bo.driver_tel,a.driver_phone) driver_phone,\n" );
		sql.append("       tsw.address_info ADDRESS,\n" );
		sql.append("       e.order_no,\n" );
		sql.append("        f_get_tccode_desc(a.status)STATUS_NAME,\n" );
		sql.append("       tsw.bill_id,tsw.bill_no,\n" );
		sql.append("       bo.bo_no,\n" );
//		sql.append("       F.ADDRESS,\n" );
		sql.append("       a.dtl_id,\n" );
		sql.append("       a.status,\n" );
		sql.append("       vmt.COLOR_NAME,\n" );
		sql.append("       vmt.MATERIAL_NAME,\n" );
		sql.append("       nvl(d.dealer_shortname,tm.WAREHOUSE_NAME) dealer_name\n" );
		sql.append("  from tt_sales_way_bill_dtl a,\n" );
		sql.append("       TT_SALES_WAYBILL      tsw,\n" );
		sql.append("       tt_sales_bo_detail    e,\n" );
		sql.append("       tt_sales_board  bo,\n" );
//		sql.append("       TM_VS_ADDRESS         f,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT VMT,\n" );
		sql.append("       tm_dealer  d,\n" );
		sql.append("       tm_warehouse  tm\n" );
		sql.append("\n" );
		sql.append(" where a.order_detail_id = e.or_de_id\n" );
		sql.append("   and e.bo_id=bo.bo_id\n" );
		sql.append("   and a.mat_id = e.mat_id\n" );
		sql.append("   and e.bill_id=tsw.bill_id\n" );
		sql.append("   and a.bill_id = tsw.bill_id\n" );
		sql.append("   and e.mat_id = vmt.MATERIAL_ID\n" );
//		sql.append("   and tsw.address_id = f.id\n" );
		sql.append("   and tsw.or_dealer_id=d.dealer_id(+)");
		sql.append("   and tsw.or_dealer_id=tm.warehouse_id(+)");
        if(warehouse_count<=1){//仓库的情况
        	sql.append(" and tsw.or_dealer_id='"+pageQuery.get(0).get("WAREHOUSE_ID")+"'");
        }
//		车架号、司机手机号、组板号、交接单号、绑定日期
        DaoFactory.getsql(sql, "a.vin", DaoFactory.getParam(request, "vin"), 2);
        DaoFactory.getsql(sql, "a.driver_phone", DaoFactory.getParam(request, "driver_phone"), 2);
        DaoFactory.getsql(sql, "bo.bo_no", DaoFactory.getParam(request, "bo_no"), 2);
        DaoFactory.getsql(sql, "e.order_no", DaoFactory.getParam(request, "order_no"), 2);
        DaoFactory.getsql(sql, "a.status", DaoFactory.getParam(request, "status"), 1);
        DaoFactory.getsql(sql, "tsw.logi_id", DaoFactory.getParam(request, "logi_id"), 1);
        DaoFactory.getsql(sql, "tsw.bill_no", DaoFactory.getParam(request, "bill_no"), 2);
        DaoFactory.getsql(sql, "d.dealer_name", DaoFactory.getParam(request, "dealerName"), 6);
        DaoFactory.getsql(sql, "a.driver_bind_date", DaoFactory.getParam(request, "driver_bind_date_s"), 3);
        DaoFactory.getsql(sql, "a.driver_bind_date", DaoFactory.getParam(request, "driver_bind_date_e"), 4);
        DaoFactory.getsql(sql, "bo.DLV_FY_DATE", DaoFactory.getParam(request, "dlv_fy_date_s"), 3);//最晚发运日期
        DaoFactory.getsql(sql, "bo.DLV_FY_DATE", DaoFactory.getParam(request, "dlv_fy_date_e"), 4);
        sql.append("order by tsw.bill_no,e.order_no,a.report_date");
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
	}
   /**
    * 经销商查询在途
    * @param request
    * @param loginUser
    * @param pageSize
    * @param currPage
    * @return
    */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getCarFactoryOnTheWayListDealer(RequestWrapper request,
			AclUserBean loginUser, Integer pageSize, Integer currPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("select VMT.SERIES_NAME,\n" );
		sql.append("       VMT.PACKAGE_NAME,\n" );
		sql.append("       a.vin,\n" );
		sql.append("       vmt.MODEL_NAME,\n" );
		sql.append("       a.report_address,\n" );
		sql.append("       a.report_date,\n" );
		sql.append("       a.driver_bind_date,\n" );
		sql.append("       DECODE(a.driver_phone,NULL,bo.driver_tel,a.driver_phone) driver_phone,\n" );
		sql.append("       tsw.address_info address,\n" );
		sql.append("       e.order_no,\n" );
		sql.append("        f_get_tccode_desc(a.status) STATUS_NAME,\n" );
		sql.append("       tsw.bill_id,tsw.bill_no,\n" );
		sql.append("       bo.bo_no,\n" );
//		sql.append("       F.ADDRESS,\n" );
		sql.append("       a.dtl_id,\n" );
		sql.append("       a.status,\n" );
		sql.append("       vmt.COLOR_NAME,\n" );
		sql.append("       vmt.MATERIAL_NAME,\n" );
		sql.append("       d.dealer_name\n" );
		sql.append("  from tt_sales_way_bill_dtl a,\n" );
		sql.append("       TT_SALES_WAYBILL      tsw,\n" );
		sql.append("       tt_sales_bo_detail    e,\n" );
		sql.append("       tt_sales_board  bo,\n" );
//		sql.append("       TM_VS_ADDRESS         f,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT VMT,\n" );
		sql.append("       tm_dealer  d\n" );
//		sql.append("       tm_warehouse  tm\n" );
		sql.append("\n" );
		sql.append(" where a.order_detail_id = e.or_de_id\n" );
		sql.append("   and e.bo_id=bo.bo_id\n" );
		sql.append("   and a.bill_id = tsw.bill_id\n" );
		sql.append("   and a.mat_id = e.mat_id\n" );
		sql.append("   and e.mat_id = vmt.MATERIAL_ID\n" );
//		sql.append("   and tsw.address_id = f.id\n" );
		sql.append("   and tsw.or_dealer_id=d.dealer_id(+)");
		sql.append("  and tsw.or_dealer_id='"+loginUser.getDealerId()+"'");
//		sql.append(" and exists ( select logi_id from tc_pose where pose_id ='"+loginUser.getPoseId()+"'  )");
//		车架号、司机手机号、组板号、交接单号、绑定日期
        DaoFactory.getsql(sql, "a.vin", DaoFactory.getParam(request, "vin"), 2);
        DaoFactory.getsql(sql, "e.order_no", DaoFactory.getParam(request, "order_no"), 2);
        DaoFactory.getsql(sql, "a.driver_phone", DaoFactory.getParam(request, "driver_phone"), 2);
        DaoFactory.getsql(sql, "bo.bo_no", DaoFactory.getParam(request, "bo_no"), 2);
        DaoFactory.getsql(sql, "tsw.bill_no", DaoFactory.getParam(request, "bill_no"), 2);
        DaoFactory.getsql(sql, "a.driver_bind_date", DaoFactory.getParam(request, "driver_bind_date_s"), 3);
        DaoFactory.getsql(sql, "a.driver_bind_date", DaoFactory.getParam(request, "driver_bind_date_e"), 4);
        sql.append("order by tsw.bill_no,e.order_no,a.report_date");
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
	}
    /**
     * 查询车辆绑定解绑日志
     * @param request
     * @param loginUser
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getbindCarlog(RequestWrapper request, AclUserBean loginUser) {
		StringBuffer sql= new StringBuffer();
		sql.append("select t.*,\n" );
		sql.append("         f_get_tcuser_name(t.business_by) business_name,\n" );
		sql.append("         f_get_tccode_desc(t.before_status) before_status_name,\n" );
		sql.append("         f_get_tccode_desc(t.after_status) after_status_name\n" );
		sql.append("    from TT_SALES_WAY_address_log t\n" );
		sql.append("   where 1 = 1");

         DaoFactory.getsql(sql, "t.dtl_id", DaoFactory.getParam(request, "dtl_id"), 1);
		return dao.pageQuery(sql.toString(), null, getFunName());
	}

}
