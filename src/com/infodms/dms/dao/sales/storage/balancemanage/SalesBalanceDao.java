package com.infodms.dms.dao.sales.storage.balancemanage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TcPosePO;
import com.infodms.yxdms.dao.IBaseDao;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class SalesBalanceDao  extends  IBaseDao  {

	private static final SalesBalanceDao dao = new SalesBalanceDao();
	public static final SalesBalanceDao getInstance(){
		if (dao == null) {
			return new SalesBalanceDao();
		}
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 挂帐单管理查询
	 * @param request
	 * @param loginUser
	 * @param pageSize
	 * @param currPage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getTtSalesBalance(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage) {
		Long poseId = loginUser.getPoseId();
		TcPosePO  posePO = new TcPosePO();
		posePO.setPoseId(poseId);
		List<TcPosePO> list = dao.select(posePO);
		Long logiId = list.get(0).getLogiId();
		StringBuffer sql= new StringBuffer();
		sql.append("select b.*,\n" );
		sql.append("       f_get_tccode_desc(b.status) STATUS_NAME,\n" );
		sql.append("       wb.address_info,\n" );
		sql.append("       wb.bill_id,\n" );
		sql.append("       wb.bill_no,\n" );
		sql.append("       wb.bill_amount,\n" );
		sql.append("       lg.logi_name,\n" );
		sql.append("       lg.logi_code,\n" );
		sql.append("       tt.balance_address,wb.apply_id\n" );
		sql.append("  from tt_sales_balance b,\n" );
		sql.append("       tt_sales_waybill wb,\n" );
		sql.append("       tt_sales_logi lg,\n" );
		sql.append("       (select pp.region_code,\n" );
		sql.append("             --  pp.region_name,\n" );
		sql.append("            --   pa.region_name,\n" );
		sql.append("            --   re.region_name,\n" );
		sql.append("               re.region_name || pa.region_name || pp.region_name balance_address\n" );
		sql.append("          from tm_region re, tm_region pa, tm_region pp\n" );
		sql.append("         where re.region_id = pa.parent_id\n" );
		sql.append("           and pa.region_id = pp.parent_id) tt\n" );
		sql.append(" where b.bal_id = wb.bal_id\n" );
		sql.append("   and b.logi_id = lg.logi_id\n" );
		sql.append("   and wb.dlv_bal_county_id = tt.region_code");
		if(0!=logiId &&null!=logiId){
			DaoFactory.getsql(sql, "lg.logi_id", String.valueOf(logiId), 1);
		}else {
			DaoFactory.getsql(sql, "lg.logi_id", DaoFactory.getParam(request, "logi_id"), 1);
		}
		DaoFactory.getsql(sql, "b.bal_no", DaoFactory.getParam(request, "bal_no"), 2);
		DaoFactory.getsql(sql, "wb.bill_no", DaoFactory.getParam(request, "bill_no"), 2);
		DaoFactory.getsql(sql, "b.status", DaoFactory.getParam(request, "status"), 1);
		sql.append(" order by b.BAL_DATE desc");
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getTtSalesBalanceQuery(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage) {
		Long poseId = loginUser.getPoseId();
		TcPosePO  posePO = new TcPosePO();
		posePO.setPoseId(poseId);
		List<TcPosePO> list = dao.select(posePO);
		Long logiId = list.get(0).getLogiId();
		String poseBusType=list.get(0).getPoseBusType().toString();
		StringBuffer sql= new StringBuffer();
		sql.append("select b.*,\n" );
		sql.append("       f_get_tccode_desc(b.status) STATUS_NAME,\n" );
		sql.append("       wb.address_info,\n" );
		sql.append("       wb.bill_id,\n" );
		sql.append("       wb.bill_no,\n" );
		sql.append("       wb.bill_amount,\n" );
		sql.append("       lg.logi_name,\n" );
		sql.append("       lg.logi_code,\n" );
		sql.append("       tt.balance_address\n" );
		sql.append("  from tt_sales_balance b,\n" );
		sql.append("       tt_sales_waybill wb,\n" );
		sql.append("       tt_sales_logi lg,\n" );
		sql.append("       (select pp.region_code,\n" );
		sql.append("             --  pp.region_name,\n" );
		sql.append("            --   pa.region_name,\n" );
		sql.append("            --   re.region_name,\n" );
		sql.append("               re.region_name || pa.region_name || pp.region_name balance_address\n" );
		sql.append("          from tm_region re, tm_region pa, tm_region pp\n" );
		sql.append("         where re.region_id = pa.parent_id\n" );
		sql.append("           and pa.region_id = pp.parent_id) tt\n" );
		sql.append(" where b.bal_id = wb.bal_id\n" );
		sql.append("   and b.logi_id = lg.logi_id\n" );
		sql.append("   and wb.dlv_bal_county_id = tt.region_code");
		sql.append("   and b.status>"+Constant.BAL_ORDER_STATUS_00+"\n");//已挂账以后的数据
		if(0!=logiId &&null!=logiId&&poseBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//物流商
			DaoFactory.getsql(sql, "lg.logi_id", String.valueOf(logiId), 1);
		}else {
			DaoFactory.getsql(sql, "lg.logi_id", DaoFactory.getParam(request, "logi_id"), 1);
		}
		DaoFactory.getsql(sql, "b.bal_no", DaoFactory.getParam(request, "bal_no"), 2);
		DaoFactory.getsql(sql, "wb.bill_no", DaoFactory.getParam(request, "bill_no"), 2);
		DaoFactory.getsql(sql, "b.status", DaoFactory.getParam(request, "status"), 1);
		sql.append(" order by b.BAL_DATE desc");
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
	}
	/**
	 * 导出查询结果
	 * @param request
	 * @param loginUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTtSalesBalanceExport(RequestWrapper request, AclUserBean loginUser) {
		Long poseId = loginUser.getPoseId();
		TcPosePO  posePO = new TcPosePO();
		posePO.setPoseId(poseId);
		List<TcPosePO> list = dao.select(posePO);
		Long logiId = list.get(0).getLogiId();
		String poseBusType=list.get(0).getPoseBusType().toString();
		StringBuffer sql= new StringBuffer();
		sql.append("select b.*,\n" );
		sql.append("       f_get_tccode_desc(b.status) STATUS_NAME,\n" );
		sql.append("       wb.address_info,\n" );
		sql.append("       wb.bill_id,\n" );
		sql.append("       wb.bill_no,\n" );
		sql.append("       wb.bill_amount,\n" );
		sql.append("       lg.logi_name,\n" );
		sql.append("       lg.logi_code,\n" );
		sql.append("       tt.balance_address\n" );
		sql.append("  from tt_sales_balance b,\n" );
		sql.append("       tt_sales_waybill wb,\n" );
		sql.append("       tt_sales_logi lg,\n" );
		sql.append("       (select pp.region_code,\n" );
		sql.append("             --  pp.region_name,\n" );
		sql.append("            --   pa.region_name,\n" );
		sql.append("            --   re.region_name,\n" );
		sql.append("               re.region_name || pa.region_name || pp.region_name balance_address\n" );
		sql.append("          from tm_region re, tm_region pa, tm_region pp\n" );
		sql.append("         where re.region_id = pa.parent_id\n" );
		sql.append("           and pa.region_id = pp.parent_id) tt\n" );
		sql.append(" where b.bal_id = wb.bal_id\n" );
		sql.append("   and b.logi_id = lg.logi_id\n" );
		sql.append("   and wb.dlv_bal_county_id = tt.region_code");
		sql.append("   and b.status>"+Constant.BAL_ORDER_STATUS_00+"\n");//已挂账以后的数据
		if(0!=logiId &&null!=logiId&&poseBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//物流商
			DaoFactory.getsql(sql, "lg.logi_id", String.valueOf(logiId), 1);
		}else {
			DaoFactory.getsql(sql, "lg.logi_id", DaoFactory.getParam(request, "logi_id"), 1);
		}
		DaoFactory.getsql(sql, "b.bal_no", DaoFactory.getParam(request, "bal_no"), 2);
		DaoFactory.getsql(sql, "wb.bill_no", DaoFactory.getParam(request, "bill_no"), 2);
		DaoFactory.getsql(sql, "b.status", DaoFactory.getParam(request, "status"), 1);
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 明细导出
	 * @param request
	 * @param loginUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBalanceExportDtl(RequestWrapper request) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSBA.BAL_ID,\n" );
		sql.append("       TSBA.BAL_NO,--对账单号\n" );
		sql.append("       TSL.LOGI_NAME,--承运商\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSWB.DLV_IS_SD) DLV_IS_SD,--是否散单\n" );
		sql.append("       TSW.BILL_NO,--交接单号\n" );
		sql.append("       TO_CHAR(TSW.LAST_CAR_DATE, 'yyyy-mm-dd') LAST_CAR_DATE,--最后交车日期\n" );
		sql.append("       FY.WAREHOUSE_NAME,--发运仓库\n" );
		sql.append("       DECODE(TD.DEALER_SHORTNAME,\n" );
		sql.append("              NULL,\n" );
		sql.append("              SH.WAREHOUSE_NAME,\n" );
		sql.append("              TD.DEALER_SHORTNAME) DEALER_NAME,--经销商或收货仓库\n" );
		sql.append("       TSW.ADDRESS_INFO,--订单收货地\n" );
		sql.append("       BAL.BAL_ADDR,--发运结算地\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSWB.DLV_IS_ZZ) DLV_IS_ZZ,TSWB.DLV_IS_ZZ IS_ZZ,--是否中转\n" );
		sql.append("       ZZ.ZZ_ADDR,--中转地址\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSW.DLV_SHIP_TYPE) DLV_SHIP_TYPE,--发运方式\n" );
		sql.append("       VMGM.SERIES_NAME,--车系\n" );
		sql.append("       VMGM.MODEL_NAME,--车型\n" );
		sql.append("       VMGM.PACKAGE_NAME,--配置\n" );
		sql.append("       VMGM.COLOR_NAME,--颜色\n" );
		sql.append("       TSWB.VIN,--车架号\n" );
		sql.append("       TSWB.MILEAGE,TSWB.MILEAGE_ZZ,--运送里程\n" );
		sql.append("       TSWB.PRICE,TSWB.PRICE_ZZ,--单价\n" );
		sql.append("       TSWB.ONE_BILL_AMOUNT--挂账运费\n" );
		sql.append("  FROM TT_SALES_BALANCE TSBA,\n" );
		sql.append("       TT_SALES_WAYBILL TSW,\n" );
		sql.append("       TT_SALES_WAY_BILL_DTL TSWB,\n" );
		sql.append("       TT_SALES_LOGI TSL,\n" );
		sql.append("       TM_WAREHOUSE FY,\n" );
		sql.append("       TM_WAREHOUSE SH,\n" );
		sql.append("       TM_DEALER TD,\n" );
		sql.append("       (SELECT TR1.REGION_CODE PROV_CODE,\n" );
		sql.append("               TR2.REGION_CODE CITY_CODE,\n" );
		sql.append("               TR3.REGION_CODE COUNTY_CODE,\n" );
		sql.append("               TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME BAL_ADDR\n" );
		sql.append("          FROM TM_REGION TR1, TM_REGION TR2, TM_REGION TR3\n" );
		sql.append("         WHERE TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("           AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("           AND TR1.REGION_TYPE = 10541002\n" );
		sql.append("           AND TR2.REGION_TYPE = 10541003\n" );
		sql.append("           AND TR3.REGION_TYPE = 10541004) BAL,\n" );
		sql.append("       (SELECT TR1.REGION_CODE PROV_CODE,\n" );
		sql.append("               TR2.REGION_CODE CITY_CODE,\n" );
		sql.append("               TR3.REGION_CODE COUNTY_CODE,\n" );
		sql.append("               TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME ZZ_ADDR\n" );
		sql.append("          FROM TM_REGION TR1, TM_REGION TR2, TM_REGION TR3\n" );
		sql.append("         WHERE TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("           AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("           AND TR1.REGION_TYPE = 10541002\n" );
		sql.append("           AND TR2.REGION_TYPE = 10541003\n" );
		sql.append("           AND TR3.REGION_TYPE = 10541004) ZZ,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT VMGM\n" );
		sql.append(" WHERE TSBA.BAL_ID = TSW.BAL_ID\n" );
		sql.append("   AND TSW.BILL_ID = TSWB.BILL_ID\n" );
		sql.append("   AND TSBA.LOGI_ID = TSL.LOGI_ID\n" );
		sql.append("   AND TSWB.DLV_WH_ID = FY.WAREHOUSE_ID\n" );
		sql.append("   AND TSW.OR_DEALER_ID = TD.DEALER_ID(+)\n" );
		sql.append("   AND TSW.OR_DEALER_ID = SH.WAREHOUSE_ID(+)\n" );
		sql.append("   AND TSW.DLV_BAL_PROV_ID = BAL.PROV_CODE\n" );
		sql.append("   AND TSW.DLV_BAL_CITY_ID = BAL.CITY_CODE\n" );
		sql.append("   AND TSW.DLV_BAL_COUNTY_ID = BAL.COUNTY_CODE\n" );
		sql.append("   AND TSWB.DLV_ZZ_PROV_ID = ZZ.PROV_CODE(+)\n" );
		sql.append("   AND TSWB.DLV_ZZ_CITY_ID = ZZ.CITY_CODE(+)\n" );
		sql.append("   AND TSWB.DLV_ZZ_COUNTY_ID = ZZ.COUNTY_CODE(+)\n" );
		sql.append("   AND VMGM.MATERIAL_ID = TSWB.MAT_ID\n" );
		DaoFactory.getsql(sql, "TSBA.BAL_ID", DaoFactory.getParam(request, "balId"), 1);

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	public List<Map<String, Object>> getBalanceDtlFare(RequestWrapper request) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSW.BILL_ID,\n" );
		sql.append("       TSW.BILL_NO,\n" );
		sql.append("       TO_CHAR(TSW.LAST_CAR_DATE, 'yyyy-mm-dd') LAST_CAR_DATE,\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSW.DLV_SHIP_TYPE) DLV_SHIP_TYPE,\n" );
		sql.append("       TSW.BILL_AMOUNT,\n" );
		sql.append("       DECODE(TD.DEALER_SHORTNAME,\n" );
		sql.append("              NULL,\n" );
		sql.append("              SH.WAREHOUSE_NAME,\n" );
		sql.append("              TD.DEALER_SHORTNAME) DEALER_NAME, --经销商或收货仓库\n" );
		sql.append("       TSW.ADDRESS_INFO, --订单收货地\n" );
		sql.append("       BAL.BAL_ADDR --发运结算地\n" );
		sql.append("  FROM TT_SALES_WAYBILL TSW,\n" );
		sql.append("       TM_WAREHOUSE SH,\n" );
		sql.append("       TM_DEALER TD,\n" );
		sql.append("       (SELECT TR1.REGION_CODE PROV_CODE,\n" );
		sql.append("               TR2.REGION_CODE CITY_CODE,\n" );
		sql.append("               TR3.REGION_CODE COUNTY_CODE,\n" );
		sql.append("               TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME BAL_ADDR\n" );
		sql.append("          FROM TM_REGION TR1, TM_REGION TR2, TM_REGION TR3\n" );
		sql.append("         WHERE TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("           AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("           AND TR1.REGION_TYPE = 10541002\n" );
		sql.append("           AND TR2.REGION_TYPE = 10541003\n" );
		sql.append("           AND TR3.REGION_TYPE = 10541004) BAL\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TSW.OR_DEALER_ID = TD.DEALER_ID(+)\n" );
		sql.append("   AND TSW.OR_DEALER_ID = SH.WAREHOUSE_ID(+)\n" );
		sql.append("   AND TSW.DLV_BAL_PROV_ID = BAL.PROV_CODE\n" );
		sql.append("   AND TSW.DLV_BAL_CITY_ID = BAL.CITY_CODE\n" );
		sql.append("   AND TSW.DLV_BAL_COUNTY_ID = BAL.COUNTY_CODE");

		DaoFactory.getsql(sql, "TSW.BAL_ID", DaoFactory.getParam(request, "balId"), 1);

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	public Map<String, Object> getBalanceInfo(RequestWrapper request) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSB.BAL_ID,\n" );
		sql.append("       TSB.BAL_NO,\n" );
		sql.append("       TSL.LOGI_NAME,\n" );
		sql.append("       TSB.BAL_MONTH,\n" );
		sql.append("       TSB.BAL_COUNT,\n" );
		sql.append("       TSB.BAL_AMOUNT\n" );
		sql.append("  FROM TT_SALES_BALANCE TSB, TT_SALES_LOGI TSL\n" );
		sql.append(" WHERE TSB.LOGI_ID = TSL.LOGI_ID\n");

		DaoFactory.getsql(sql, "TSB.BAL_ID", DaoFactory.getParam(request, "balId"), 1);

		return dao.pageQueryMap(sql.toString(), null, getFunName());
	}
	/**
	 * 修改交接单明细数据
	 * @param billId
	 * @param loginUser 
	 */
	@SuppressWarnings("unchecked")
	public void updateTtSalesWayBillDtlbyBillId(Long billId, AclUserBean loginUser) {
		StringBuffer sql= new StringBuffer();
		sql.append("update tt_sales_way_bill_dtl  dt set dt.price=null,dt.price_factor=null,dt.mileage=null,dt.one_bill_amount=null,dt.update_date=sysdate,dt.update_by='"+loginUser.getUserId()+"' where dt.bill_id='"+billId+"'");
        dao.update(sql.toString(), null);
	}
	/**
	 * 修改交接单
	 * @param billId
	 * @param loginUser 
	 */
	@SuppressWarnings("unchecked")
	public void updaTtSalesWaybillByBillId(Long billId, AclUserBean loginUser) {
		StringBuffer sql= new StringBuffer();
		sql.append("update  tt_sales_waybill  tt set tt.bal_id=null,tt.bill_amount=null,tt.update_date=sysdate,tt.update_by='"+loginUser.getUserId()+"'  where tt.bill_id='"+billId+"'");
        dao.update(sql.toString(), null);
	}

}
