package com.infodms.dms.dao.sales.ordermanage.orderdetail;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class OrderOptionDao extends BaseDao{
	public static Logger logger = Logger.getLogger(OrderOptionDao.class);
	private static final OrderOptionDao dao = new OrderOptionDao();

	public static final OrderOptionDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public TmDateSetPO getTmDateSetPO(Date date, Long companyId) {
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
		String dayStr = formate.format(date);
		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = new TmDateSetPO();
		dateSet.setSetDate(dayStr);
		dateSet.setCompanyId(companyId);
		List<PO> list = select(dateSet);
		return list.size() != 0 ? (TmDateSetPO) list.get(0) : null;
	}
	public List<Map<String, Object>> getGeneralDeliveryDateList_New(Long companyId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = getTmDateSetPO(new Date(), companyId);
		if (dateSet != null) {
			/*
			 * TmBusinessParaPO para1 = getTmBusinessParaPO(
			 * Constant.GENEREAL_ORDER_BEFORE_WEEK_PARA, new Long(
			 * companyId));// 常规订单提报提前周度参数 TmBusinessParaPO para2 =
			 * getTmBusinessParaPO( Constant.GENEREAL_ORDER_WEEK_PARA, new
			 * Long(companyId));
			 */// 常规订单提报周度参数
			String currentYear = dateSet.getSetYear(); // 获取系统日历表中对应年份
			String currentWeek = dateSet.getSetWeek(); // 获取系统日历表中对应月份

			String date_start = null;
			String date_end = null;

			Map<String, String> map = new HashMap<String, String>();
			map.put("year", currentYear);
			map.put("week", currentWeek);

			OrderReportDao ord = new OrderReportDao();
			Map<String, Object> dateMap = ord.getDayByWeek(map);

			date_start = dateMap.get("MINDATE").toString().substring(0, 4) + "/" + dateMap.get("MINDATE").toString().substring(4, 6) + "/" + dateMap.get("MINDATE").toString().substring(6, 8);
			date_end = dateMap.get("MAXDATE").toString().substring(0, 4) + "/" + dateMap.get("MAXDATE").toString().substring(4, 6) + "/" + dateMap.get("MAXDATE").toString().substring(6, 8);

			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put("code", currentYear + "-" + currentWeek);
			newMap.put("name", currentYear + "年" + currentWeek + "周");
			newMap.put("date_start", date_start);
			newMap.put("date_end", date_end);
			list.add(newMap);
		}
		return list;
	}
	/**
	 * 获取锁定资源的明细信息
	 * @return
	 */
	public static List <Map<String,Object>> getLockResourceDetail(String material_id){
		List<Map<String,Object>> list=null;
//		StringBuilder sql= new StringBuilder();
//		sql.append("SELECT TVO.ORDER_ORG_ID,\n" );
//		sql.append("       TVDR.DLVRY_REQ_NO,\n" );
//		sql.append("       TD.DEALER_CODE,\n" );
//		sql.append("       TD.DEALER_SHORTNAME,\n" );
//		sql.append("       TVM.MATERIAL_CODE,\n" );
//		sql.append("       TVM.MATERIAL_NAME,\n" );
//		sql.append("       NVL(TVORR.AMOUNT,0)-NVL(TVORR.DELIVERY_AMOUNT,0) AMOUNT\n" );
//		sql.append("  FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR,\n" );
//		sql.append("       TT_VS_DLVRY_REQ_DTL          TVDRD,\n" );
//		sql.append("       TT_VS_DLVRY_REQ              TVDR,\n" );
//		sql.append("       TT_VS_ORDER                  TVO,\n" );
//		sql.append("       TM_VHCL_MATERIAL             TVM,\n" );
//		sql.append("       TM_DEALER                    TD\n" );
//		sql.append(" WHERE TVORR.MATERIAL_ID(+) = TVM.MATERIAL_ID\n" );
//		sql.append("   AND TVORR.RESERVE_STATUS = 11581001\n" );
//		sql.append("   AND TVORR.REQ_DETAIL_ID = TVDRD.DETAIL_ID\n" );
//		sql.append("   AND TVDRD.REQ_ID = TVDR.REQ_ID\n" );
//		sql.append("   AND TVO.ORDER_ID(+) = TVDR.ORDER_ID\n" );
//		sql.append("   AND TD.DEALER_ID(+) = TVO.ORDER_ORG_ID\n" );
//		sql.append("   AND TVM.MATERIAL_ID = "+material_id+"\n" );
//		sql.append("   AND NVL(TVORR.AMOUNT,0)-NVL(TVORR.DELIVERY_AMOUNT,0)! = 0 \n" );
//		sql.append(" GROUP BY TVO.ORDER_ORG_ID,\n" );
//		sql.append("          TVDR.DLVRY_REQ_NO,\n" );
//		sql.append("          TD.DEALER_CODE,\n" );
//		sql.append("          TD.DEALER_SHORTNAME,\n" );
//		sql.append("          TVORR.AMOUNT,\n" );
//		sql.append("          TVM.MATERIAL_CODE,\n" );
//		sql.append("       TVM.MATERIAL_NAME\n" );
		//sql.append(" ORDER BY TVO.ORDER_ORG_ID DESC");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		TmDateSetPO tds=new TmDateSetPO();
		tds.setSetDate(sdf.format(new Date()));
		tds=(TmDateSetPO) dao.select(tds).get(0);
		StringBuilder sql= new StringBuilder();
		sql.append("select t.dlvry_req_no,t.dealer_code,t.dealer_shortname,t.material_code,t.material_name,t.types ,t.amount from (");
		sql.append("      SELECT TVO.ORDER_ORG_ID,\n" );
		sql.append("       TVDR.DLVRY_REQ_NO,\n" );
		sql.append("       TD.DEALER_CODE,\n" );
		sql.append("       TD.DEALER_SHORTNAME,\n" );
		sql.append("       TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("        '发运申请' types,");
		sql.append("       NVL(TVORR.AMOUNT,0)-NVL(TVORR.DELIVERY_AMOUNT,0) AMOUNT\n" );
		sql.append("  FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR,\n" );
		sql.append("       TT_VS_DLVRY_REQ_DTL          TVDRD,\n" );
		sql.append("       TT_VS_DLVRY_REQ              TVDR,\n" );
		sql.append("       TT_VS_ORDER                  TVO,\n" );
		sql.append("       TM_VHCL_MATERIAL             TVM,\n" );
		sql.append("       TM_DEALER                    TD\n" );
		sql.append(" WHERE TVORR.MATERIAL_ID(+) = TVM.MATERIAL_ID\n" );
		sql.append("   AND TVORR.RESERVE_STATUS = 11581001\n" );
		sql.append("   AND TVORR.REQ_DETAIL_ID = TVDRD.DETAIL_ID\n" );
		sql.append("   AND TVDRD.REQ_ID = TVDR.REQ_ID\n" );
		sql.append("   AND TVO.ORDER_ID(+) = TVDR.ORDER_ID\n" );
		sql.append("   AND TD.DEALER_ID(+) = TVO.ORDER_ORG_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID ="+material_id+" \n" );
		sql.append("   AND NVL(TVORR.AMOUNT,0)-NVL(TVORR.DELIVERY_AMOUNT,0)! = 0\n" );
		sql.append(" union\n" );
		sql.append("SELECT TVO.ORDER_ORG_ID,\n" );
		sql.append("       TVo.order_no,\n" );
		sql.append("       TD.DEALER_CODE,\n" );
		sql.append("       TD.DEALER_SHORTNAME,\n" );
		sql.append("       TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       '常规订单',\n" );
		sql.append("       NVL(TVOd.check_amount,0)-NVL(TVOd.CALL_AMOUNT,0) AMOUNT\n" );
		sql.append("  FROM\n" );
		sql.append("       TT_VS_order_DeTaiL          TVoD,\n" );
		sql.append("       TT_VS_ORDER                  TVO,\n" );
		sql.append("       TM_VHCL_MATERIAL             TVM,\n" );
		sql.append("       TM_DEALER                    TD\n" );
		sql.append(" WHERE TVOd.MATERIAL_ID(+) = TVM.MATERIAL_ID\n" );
		sql.append("   AND TD.DEALER_ID(+) = TVO.ORDER_ORG_ID\n" );
		sql.append("   and tvo.order_id=tvod.order_id\n" );
		sql.append("and tvo.order_year="+tds.getSetYear()+"\n" );
		sql.append("   and tvo.order_month="+tds.getSetMonth()+"\n" );
		sql.append("   and tvo.order_week="+tds.getSetWeek()+"");
		sql.append( "  and tvo.order_type="+Constant.ORDER_TYPE_01);
		sql.append("   AND TVM.MATERIAL_ID = "+material_id+"\n" );
		sql.append("   AND (Tvod.is_lock IS NULL OR TVOd.IS_LOCK=10041001)\n" );
		sql.append("   AND NVL(Tvod.check_amount,0)-NVL(TVOd.CALL_AMOUNT,0)! = 0");
		sql.append(")  t order by  t.types");


		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	/**
	 * Function : 常规订单发运申请查询
	 * 
	 * @param :
	 *            订单年周
	 * @param :
	 *            订单号码
	 * @param :
	 *            车系
	 * @return : 满足条件的常规订单信息
	 * @throws :
	 *             Exception LastUpdate : 2010-05-24
	 */
	public PageResult<Map<String, Object>> getDeliveryDetailQuery(Map<String,String> map,int pageSize,int curPage) throws Exception {
		String orderYear=map.get("orderYear");
		String orderWeek=map.get("orderWeek");
		String orderNo=map.get("orderNo");
		String orderId=map.get("orderId");
		String beginTime=map.get("startDate");
		String endTime=map.get("endDate");
		String dealerCodes=map.get("dealerCodes");
		String materialCode=map.get("materialCode");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT distinct TVM.MATERIAL_ID, ---物料ID\n" );
		sql.append("       td.dealer_id,--经销商\n" );
		sql.append("       td.dealer_code,--经销商代码\n" );
		sql.append("       td.dealer_shortname,--经销商名称\n" );
		sql.append("       TSO.ORDER_ID, ---订单ID\n" );
		sql.append("       TSO.AREA_ID, ---业务范围ID\n" );
		sql.append("       TSOD.DETAIL_ID, ---订单明细ID\n" );
		sql.append("       TSOD.IS_LOCK, ---订单明细ID\n" );
		sql.append("       TSO.ORDER_NO, ---订单号码\n" );
		sql.append("       VMG.SERIES_NAME,---车系\n" );
		sql.append("       TVM.MATERIAL_CODE, ---物料编码\n" );
		sql.append("       TVM.MATERIAL_NAME, ---物料名称\n" );
		sql.append("       TVM.COLOR_NAME, ---颜色\n" );
		//sql.append("        F_GET_MATPRICE(td.dealer_code,TVMG.GROUP_CODE)SINGLE_PRICE, ---物料单价\n" );
		//sql.append("        F_GET_PRICELISTID(td.dealer_code,TVMG.GROUP_CODE) PRICTLIST_ID, ---物料单价id\n" );
		//sql.append(" --     1000 SINGLE_PRICE, ---物料单价\n" );
		//sql.append("  --    10195 PRICTLIST_ID, ---物料单价id\n" );
		sql.append("       TSOD.ORDER_AMOUNT, ---订单数量\n" );
		sql.append("   	    tvw.AVA_STOCK ,\n" );
		sql.append("   	    nvl(tvw.UNDO_ORDER_AMOUNT,0) UNDO_ORDER_AMOUNT,\n" );
		sql.append("       NVL(TSOD.CHECK_AMOUNT, 0) CHECK_AMOUNT, ---已审核数量\n" );
		sql.append("       NVL(TSOD.CALL_AMOUNT, 0) CALL_AMOUNT ,---已申请数量\n" );
		sql.append("TO_NUMBER(NVL(TSOD.CHECK_AMOUNT, 0))-TO_NUMBER(NVL(TSOD.CALL_AMOUNT, 0)) APPLY_AMOUNT ,---默认申请数量\n");
		//sql.append("(TO_NUMBER(NVL(TSOD.CHECK_AMOUNT, 0))-TO_NUMBER(NVL(TSOD.CALL_AMOUNT, 0)))*TO_NUMBER(F_GET_MATPRICE(td.dealer_code,TVMG.GROUP_CODE)) TOTAL_PRICE,\n");
		sql.append("        TSOD.DISCOUNT_RATE,\n" );
		sql.append("       TSOD.DISCOUNT_S_PRICE,\n" );
		sql.append("       TSOD.DISCOUNT_PRICE\n" );
		sql.append("  FROM TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		sql.append("       VW_MATERIAL_GROUP        VMG,\n" );
		sql.append("       TM_AREA_GROUP            TAG,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );
		sql.append("        TT_VS_ORDER              TSO,\n" );
		sql.append("  VW_VS_resource_ENTITY_week tvw,");
		sql.append("       TT_VS_ORDER_DETAIL       TSOD,\n" );
		sql.append("       tm_dealer td\n" );
		sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n" );
		sql.append("   AND TSOD.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append("   AND TVMGR.GROUP_ID = VMG.PACKAGE_ID\n" );
		sql.append("   AND tvw.material_id(+) = tvm.material_id\n" );
		sql.append("   AND (VMG.SERIES_ID = TAG.MATERIAL_GROUP_ID OR\n" );
		sql.append("       VMG.MODEL_ID = TAG.MATERIAL_GROUP_ID OR\n" );
		sql.append("       VMG.PACKAGE_ID = TAG.MATERIAL_GROUP_ID)\n" );
		sql.append("   AND TAG.AREA_ID = TSO.AREA_ID\n" );
		sql.append("AND TVMG.GROUP_ID=TVMGR.GROUP_ID\n" );
		sql.append("  AND TVM.MATERIAL_ID=TVMGR.MATERIAL_ID\n" );
		sql.append("  and td.dealer_id=tso.order_org_id\n" );
		sql.append("   AND TSO.ORDER_TYPE = 10201001\n" );
		sql.append("   AND TSO.ORDER_STATUS = 10211005\n" );
		sql.append("  AND TSOD.CHECK_AMOUNT > 0\n" );
		sql.append("  AND NVL(TSOD.CHECK_AMOUNT, 0) <> NVL(TSOD.CALL_AMOUNT, 0)");
		
		if (!"".equals(orderYear) && orderYear != null) {
			sql.append(" AND TSO.ORDER_YEAR = '" + orderYear + "'\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			sql.append(" AND TSO.ORDER_WEEK = '" + orderWeek + "'\n");
		}
		if (!CommonUtils.isNullString(orderNo)) {
			sql.append(" AND TSO.ORDER_NO = '" + orderNo + "'\n");
		}
		if (!CommonUtils.isNullString(materialCode)) {
			sql.append(" AND TVM.MATERIAL_CODE like '%" + materialCode + "%'\n");
		}
		if (!CommonUtils.isNullString(orderId)) {
			sql.append(" AND TSO.ORDER_ID = '" + orderId + "'\n");
		}
		if (null != beginTime && !"".equals(beginTime)) {
			sql.append("   AND TSO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append("   AND TSO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (!"".equals(dealerCodes) && dealerCodes != null) {
			sql.append(" AND Tso.ORDER_ORG_ID  in(select td.dealer_id from tm_dealer td where td.dealer_code in("+CommonUtils.getSplitStringForIn(dealerCodes)+")) \n");
		}
		sql.append("    ORDER BY ORDER_ID\n");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * Function : 常规订单发运申请查询
	 * 
	 * @param :
	 *            订单年周
	 * @param :
	 *            订单号码
	 * @param :
	 *            车系
	 * @return : 满足条件的常规订单信息
	 * @throws :
	 *             Exception LastUpdate : 2010-05-24
	 */
	public List<Map<String, Object>> getOrderDetailList(Map<String,String> map) throws Exception {
		String orderYear=map.get("orderYear");
		String orderWeek=map.get("orderWeek");
		String orderNo=map.get("orderNo");
		String orderId=map.get("orderId");
		String beginTime=map.get("startDate");
		String endTime=map.get("endDate");
		String dealerCodes=map.get("dealerCodes");
		String materialCode=map.get("materialCode");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT distinct TVM.MATERIAL_ID, ---物料ID\n" );
		sql.append("       td.dealer_id,--经销商\n" );
		sql.append("       td.dealer_code,--经销商代码\n" );
		sql.append("       td.dealer_shortname,--经销商名称\n" );
		sql.append("       TSO.ORDER_ID, ---订单ID\n" );
		sql.append("       TSO.AREA_ID, ---业务范围ID\n" );
		sql.append("       TSOD.DETAIL_ID, ---订单明细ID\n" );
		sql.append("       TSOD.IS_LOCK, ---订单明细ID\n" );
		sql.append("       TSO.ORDER_NO, ---订单号码\n" );
		sql.append("       VMG.SERIES_NAME,---车系\n" );
		sql.append("       TVM.MATERIAL_CODE, ---物料编码\n" );
		sql.append("       TVM.MATERIAL_NAME, ---物料名称\n" );
		sql.append("       TVM.COLOR_NAME, ---颜色\n" );
		//sql.append("        F_GET_MATPRICE(td.dealer_code,TVMG.GROUP_CODE)SINGLE_PRICE, ---物料单价\n" );
		//sql.append("        F_GET_PRICELISTID(td.dealer_code,TVMG.GROUP_CODE) PRICTLIST_ID, ---物料单价id\n" );
		//sql.append(" --     1000 SINGLE_PRICE, ---物料单价\n" );
		//sql.append("  --    10195 PRICTLIST_ID, ---物料单价id\n" );
		sql.append("       TSOD.ORDER_AMOUNT, ---订单数量\n" );
		sql.append("   	    tvw.AVA_STOCK ,\n" );
		sql.append("   	    nvl(tvw.UNDO_ORDER_AMOUNT,0) UNDO_ORDER_AMOUNT,\n" );
		sql.append("       NVL(TSOD.CHECK_AMOUNT, 0) CHECK_AMOUNT, ---已审核数量\n" );
		sql.append("       NVL(TSOD.CALL_AMOUNT, 0) CALL_AMOUNT ,---已申请数量\n" );
		sql.append("       NVL(TSOD.RESPOND_AMOUNT, 0) RESPOND_AMOUNT ,---免责数量\n" );
		
		sql.append("TO_NUMBER(NVL(TSOD.CHECK_AMOUNT, 0))-TO_NUMBER(NVL(TSOD.CALL_AMOUNT, 0)) APPLY_AMOUNT ,---默认申请数量\n");
		//sql.append("(TO_NUMBER(NVL(TSOD.CHECK_AMOUNT, 0))-TO_NUMBER(NVL(TSOD.CALL_AMOUNT, 0)))*TO_NUMBER(F_GET_MATPRICE(td.dealer_code,TVMG.GROUP_CODE)) TOTAL_PRICE,\n");
		sql.append("        TSOD.DISCOUNT_RATE,\n" );
		sql.append("       TSOD.DISCOUNT_S_PRICE,\n" );
		sql.append("       TSOD.DISCOUNT_PRICE\n" );
		sql.append("  FROM TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		sql.append("       VW_MATERIAL_GROUP        VMG,\n" );
		sql.append("       TM_AREA_GROUP            TAG,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );
		sql.append("        TT_VS_ORDER              TSO,\n" );
		sql.append("  VW_VS_resource_ENTITY_week tvw,");
		sql.append("       TT_VS_ORDER_DETAIL       TSOD,\n" );
		sql.append("       tm_dealer td\n" );
		sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n" );
		sql.append("   AND TSOD.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append("   AND TVMGR.GROUP_ID = VMG.PACKAGE_ID\n" );
		sql.append("   AND tvw.material_id(+) = tvm.material_id\n" );
		sql.append("   AND (VMG.SERIES_ID = TAG.MATERIAL_GROUP_ID OR\n" );
		sql.append("       VMG.MODEL_ID = TAG.MATERIAL_GROUP_ID OR\n" );
		sql.append("       VMG.PACKAGE_ID = TAG.MATERIAL_GROUP_ID)\n" );
		sql.append("   AND TAG.AREA_ID = TSO.AREA_ID\n" );
		sql.append("AND TVMG.GROUP_ID=TVMGR.GROUP_ID\n" );
		sql.append("  AND TVM.MATERIAL_ID=TVMGR.MATERIAL_ID\n" );
		sql.append("  and td.dealer_id=tso.order_org_id\n" );
		sql.append("   AND TSO.ORDER_TYPE = 10201001\n" );
		sql.append("   AND TSO.ORDER_STATUS = 10211005\n" );
		sql.append("  AND TSOD.CHECK_AMOUNT > 0\n" );
		sql.append("  AND NVL(TSOD.CHECK_AMOUNT, 0) <> NVL(TSOD.CALL_AMOUNT, 0)");
		
		if (!"".equals(orderYear) && orderYear != null) {
			sql.append(" AND TSO.ORDER_YEAR = '" + orderYear + "'\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			sql.append(" AND TSO.ORDER_WEEK = '" + orderWeek + "'\n");
		}
		if (!CommonUtils.isNullString(orderNo)) {
			sql.append(" AND TSO.ORDER_NO = '" + orderNo + "'\n");
		}
		if (!CommonUtils.isNullString(materialCode)) {
			sql.append(" AND TVM.MATERIAL_CODE like '%" + materialCode + "%'\n");
		}
		if (!CommonUtils.isNullString(orderId)) {
			sql.append(" AND TSO.ORDER_ID = '" + orderId + "'\n");
		}
		if (null != beginTime && !"".equals(beginTime)) {
			sql.append("   AND TSO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append("   AND TSO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (!"".equals(dealerCodes) && dealerCodes != null) {
			sql.append(" AND Tso.ORDER_ORG_ID  in(select td.dealer_id from tm_dealer td where td.dealer_code in("+CommonUtils.getSplitStringForIn(dealerCodes)+")) \n");
		}
		sql.append("    ORDER BY ORDER_ID\n");
		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName());
		return ps;
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
		sql.append("       NVL(TVOD.CHECK_AMOUNT,0) CHECK_AMOUNT,\n" );
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
	public PageResult<Map<String, Object>> getOrderAllQuery(Map<String, String> map,int pageSize,int curPage) {
		String orderNo=map.get("orderNo");
		String dealerCodes=map.get("dealerCodes");
		String orderYear=map.get("orderYear");
		String orderWeek=map.get("orderWeek");
		String dutyType=map.get("dutyType");
		String orgId=map.get("orgId");
		String dealerId=map.get("dealerId");
		String isRespond=map.get("isRespond");
		String year=map.get("year");
		String week=map.get("week");
		String endYear=map.get("endYear");
		String endWeek=map.get("endWeek");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT distinct td.dealer_id,\n" );
		sql.append("                td.dealer_code,\n" );
		sql.append("                td.dealer_shortname,\n" );
		sql.append("                TSO.ORDER_ID, ---订单ID\n" );
		sql.append("                TSO.ORDER_YEAR, ---订单ID\n" );
		sql.append("                TSO.ORDER_WEEK, ---订单ID\n" );
		sql.append("                TSO.AREA_ID, ---业务范围ID\n" );
		sql.append("                TSO.ORDER_NO ,---订单号码\n" );
		sql.append("                "+dutyType+" duty_Type ,---dutyType\n" );
		sql.append("                 TSO.IS_RESPOND,");
		sql.append("                 TSO.RESPOND_STATUS");
		sql.append("  FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD, tm_dealer td\n" );
		sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n" );
		sql.append("   and td.dealer_id = tso.order_org_id\n" );
		sql.append("   AND TSO.ORDER_TYPE = 10201001\n" );
		sql.append("   AND TSO.ORDER_STATUS = 10211005\n" );
		sql.append("   AND TSO.COMPANY_ID = 2010010100070674\n" );
		sql.append("   AND TSO.AREA_ID = 2012112619161228\n" );
		sql.append("   AND TSOD.CHECK_AMOUNT > 0\n" );
		sql.append("   AND NVL(TSOD.CHECK_AMOUNT, 0) <> NVL(TSOD.CALL_AMOUNT, 0)");
		if (!"".equals(orderYear) && orderYear != null) {
			//sql.append(" AND TSO.ORDER_YEAR = '" + orderYear + "'\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			//sql.append(" AND TSO.ORDER_WEEK = '" + orderWeek + "'\n");
		}
		if (!"".equals(year) && year != null) {
			sql.append(" AND TSO.ORDER_YEAR >= '" + year + "'\n");
		}
		if (!"".equals(week) && week != null) {
			sql.append(" AND TSO.ORDER_WEEK >= '" + week + "'\n");
		}
		if (!"".equals(endYear) && endYear != null) {
			sql.append(" AND TSO.ORDER_YEAR <= '" + endYear + "'\n");
		}
		if (!"".equals(endWeek) && endWeek != null) {
			sql.append(" AND TSO.ORDER_WEEK <= '" + endWeek + "'\n");
		}
		// 大区机构人员变动查询
		if ("10431003".equals(dutyType)) {
			sql.append("AND td.DEALER_ID IN");
			sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sql.append("WHERE VW.ROOT_ORG_ID="+orgId+")");
			// 小区机构人员变动查询
		} else if ("10431004".equals(dutyType)) {
			sql.append("AND td.DEALER_ID IN");
			sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sql.append("WHERE VW.PQ_ORG_ID="+orgId+")");
		}
		if(dealerId!=null&&!"".equals(dealerId)){
			sql.append("AND td.DEALER_ID IN("+dealerId+")");
		}
		if (!CommonUtils.isNullString(orderNo)) {
			sql.append(" AND TSO.ORDER_NO = '" + orderNo + "'\n");
		}
		if (!CommonUtils.isNullString(isRespond)) {
			if("10041001".equals(isRespond)){
				sql.append(" AND TSO.respond_status =90011003 \n");
			}else{
				sql.append(" and  ( TSO.respond_status is null or TSO.respond_status!=90011003 ) \n");
			}
		}
		//sql.append("AND (TSO.RESPOND_STATUS IS NULL OR  TSO.RESPOND_STATUS="+Constant.RESPOND_STATUS_TYPE_04+")");
		if (!"".equals(dealerCodes) && dealerCodes != null) {
			sql.append(" AND Tso.ORDER_ORG_ID  in(select td.dealer_id from tm_dealer td where td.dealer_code in("+CommonUtils.getSplitStringForIn(dealerCodes)+")) \n");
		}
		PageResult<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);
		return list;
	}
	public PageResult<Map<String, Object>> getOrderFisrtQuery(Map<String, String> map,int pageSize,int curPage) {
		String orderNo=map.get("orderNo");
		String dealerCodes=map.get("dealerCodes");
		String orderYear=map.get("orderYear");
		String orderWeek=map.get("orderWeek");
		String dutyType=map.get("dutyType");
		String orgId=map.get("orgId");
		String dealerId=map.get("dealerId");
		String isRespond=map.get("isRespond");
		String year=map.get("year");
		String week=map.get("week");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT distinct td.dealer_id,\n" );
		sql.append("                td.dealer_code,\n" );
		sql.append("                td.dealer_shortname,\n" );
		sql.append("                TSO.ORDER_ID, ---订单ID\n" );
		sql.append("                TSO.ORDER_YEAR, ---订单ID\n" );
		sql.append("                TSO.ORDER_WEEK, ---订单ID\n" );
		sql.append("                TSO.AREA_ID, ---业务范围ID\n" );
		sql.append("                TSO.ORDER_NO ,---订单号码\n" );
		sql.append("                "+dutyType+" duty_Type ,---dutyType\n" );
		sql.append("                 TSO.IS_RESPOND");
		sql.append("  FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD, tm_dealer td\n" );
		sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n" );
		sql.append("   and td.dealer_id = tso.order_org_id\n" );
		sql.append("   AND TSO.ORDER_TYPE = 10201001\n" );
		sql.append("   AND TSO.ORDER_STATUS = 10211005\n" );
		sql.append("   AND TSO.COMPANY_ID = 2010010100070674\n" );
		sql.append("   AND TSO.AREA_ID = 2012112619161228\n" );
		sql.append("   AND TSO.respond_status = "+Constant.RESPOND_STATUS_TYPE_01+"\n" );
		sql.append("   AND TSOD.CHECK_AMOUNT > 0\n" );
		sql.append("   AND NVL(TSOD.CHECK_AMOUNT, 0) <> NVL(TSOD.CALL_AMOUNT, 0)");
		if (!"".equals(orderYear) && orderYear != null) {
			//sql.append(" AND TSO.ORDER_YEAR = '" + orderYear + "'\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			//sql.append(" AND TSO.ORDER_WEEK = '" + orderWeek + "'\n");
		}
		if (!"".equals(year) && year != null) {
			sql.append(" AND TSO.ORDER_YEAR = '" + year + "'\n");
		}
		if (!"".equals(week) && week != null) {
			sql.append(" AND TSO.ORDER_WEEK = '" + week + "'\n");
		}
		// 大区机构人员变动查询
		if ("10431003".equals(dutyType)) {
			sql.append("AND td.DEALER_ID IN");
			sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sql.append("WHERE VW.ROOT_ORG_ID="+orgId+")");
			// 小区机构人员变动查询
		} else if ("10431004".equals(dutyType)) {
			sql.append("AND td.DEALER_ID IN");
			sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sql.append("WHERE VW.PQ_ORG_ID="+orgId+")");
		}
		if(dealerId!=null&&!"".equals(dealerId)){
			sql.append("AND td.DEALER_ID IN("+dealerId+")");
		}
		if (!CommonUtils.isNullString(orderNo)) {
			sql.append(" AND TSO.ORDER_NO = '" + orderNo + "'\n");
		}
		if (!CommonUtils.isNullString(isRespond)) {
			if("10041001".equals(isRespond)){
				sql.append(" AND TSOD.respond_AMOUNT <>0 \n");
			}
			
		}
		if (!"".equals(dealerCodes) && dealerCodes != null) {
			sql.append(" AND Tso.ORDER_ORG_ID  in(select td.dealer_id from tm_dealer td where td.dealer_code in("+CommonUtils.getSplitStringForIn(dealerCodes)+")) \n");
		}
		PageResult<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);
		return list;
	}
	public PageResult<Map<String, Object>> getOrderEndQuery(Map<String, String> map,int pageSize,int curPage) {
		String orderNo=map.get("orderNo");
		String dealerCodes=map.get("dealerCodes");
		String orderYear=map.get("orderYear");
		String orderWeek=map.get("orderWeek");
		String dutyType=map.get("dutyType");
		String orgId=map.get("orgId");
		String dealerId=map.get("dealerId");
		String isRespond=map.get("isRespond");
		String year=map.get("year");
		String week=map.get("week");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT distinct td.dealer_id,\n" );
		sql.append("                td.dealer_code,\n" );
		sql.append("                td.dealer_shortname,\n" );
		sql.append("                TSO.ORDER_ID, ---订单ID\n" );
		sql.append("                TSO.ORDER_YEAR, ---订单ID\n" );
		sql.append("                TSO.ORDER_WEEK, ---订单ID\n" );
		sql.append("                TSO.AREA_ID, ---业务范围ID\n" );
		sql.append("                TSO.ORDER_NO ,---订单号码\n" );
		sql.append("                "+dutyType+" duty_Type ,---dutyType\n" );
		sql.append("                 TSO.IS_RESPOND");
		sql.append("  FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD, tm_dealer td\n" );
		sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n" );
		sql.append("   and td.dealer_id = tso.order_org_id\n" );
		sql.append("   AND TSO.ORDER_TYPE = 10201001\n" );
		sql.append("   AND TSO.ORDER_STATUS = 10211005\n" );
		sql.append("   AND TSO.COMPANY_ID = 2010010100070674\n" );
		sql.append("   AND TSO.AREA_ID = 2012112619161228\n" );
		sql.append("   AND TSOD.CHECK_AMOUNT > 0\n" );
		sql.append("   AND TSO.respond_status = "+Constant.RESPOND_STATUS_TYPE_02+"\n" );
		sql.append("   AND NVL(TSOD.CHECK_AMOUNT, 0) <> NVL(TSOD.CALL_AMOUNT, 0)");
		if (!"".equals(orderYear) && orderYear != null) {
			//sql.append(" AND TSO.ORDER_YEAR = '" + orderYear + "'\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			//sql.append(" AND TSO.ORDER_WEEK = '" + orderWeek + "'\n");
		}
		if (!"".equals(year) && year != null) {
			sql.append(" AND TSO.ORDER_YEAR = '" + year + "'\n");
		}
		if (!"".equals(week) && week != null) {
			sql.append(" AND TSO.ORDER_WEEK = '" + week + "'\n");
		}
		// 大区机构人员变动查询
		if ("10431003".equals(dutyType)) {
			sql.append("AND td.DEALER_ID IN");
			sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sql.append("WHERE VW.ROOT_ORG_ID="+orgId+")");
			// 小区机构人员变动查询
		} else if ("10431004".equals(dutyType)) {
			sql.append("AND td.DEALER_ID IN");
			sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sql.append("WHERE VW.PQ_ORG_ID="+orgId+")");
		}
		if(dealerId!=null&&!"".equals(dealerId)){
			sql.append("AND td.DEALER_ID IN("+dealerId+")");
		}
		if (!CommonUtils.isNullString(orderNo)) {
			sql.append(" AND TSO.ORDER_NO = '" + orderNo + "'\n");
		}
		if (!CommonUtils.isNullString(isRespond)) {
			if("10041001".equals(isRespond)){
				sql.append(" AND TSO.respond_status =90011003 \n");
			}else{
				sql.append(" and  ( TSO.respond_status is null or TSO.respond_status!=90011003 )\n");
			}
		}
		if (!"".equals(dealerCodes) && dealerCodes != null) {
			sql.append(" AND Tso.ORDER_ORG_ID  in(select td.dealer_id from tm_dealer td where td.dealer_code in("+CommonUtils.getSplitStringForIn(dealerCodes)+")) \n");
		}
		PageResult<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);
		return list;
	}


	public List<Map<String, Object>> getOrderDetailSelect(
			Map<String, String> map) {
		String orderId=map.get("orderId");
		String year=map.get("year");
		String week=map.get("week");
		String endYear=map.get("endYear");
		String endWeek=map.get("endWeek");
		String orderNo=map.get("orderNo");
		String dealerCodes=map.get("dealerCodes");
		String isRespond=map.get("isRespond");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT distinct TVM.MATERIAL_ID, ---物料ID\n" );
		sql.append("       td.dealer_id,--经销商\n" );
		sql.append("       td.dealer_code,--经销商代码\n" );
		sql.append("       td.dealer_shortname,--经销商名称\n" );
		sql.append("       TSO.ORDER_ID, ---订单ID\n" );
		sql.append("       TSO.AREA_ID, ---业务范围ID\n" );
		sql.append("       TSOD.DETAIL_ID, ---订单明细ID\n" );
		sql.append("       TSOD.IS_LOCK, ---订单明细ID\n" );
		sql.append("       TSO.ORDER_NO, ---订单号码\n" );
		sql.append("       VMG.SERIES_NAME,---车系\n" );
		sql.append("       TVM.MATERIAL_CODE, ---物料编码\n" );
		sql.append("       TVM.MATERIAL_NAME, ---物料名称\n" );
		sql.append("       TVM.COLOR_NAME, ---颜色\n" );
	//	sql.append("        F_GET_MATPRICE(td.dealer_code,TVMG.GROUP_CODE)SINGLE_PRICE, ---物料单价\n" );
	//	sql.append("        F_GET_PRICELISTID(td.dealer_code,TVMG.GROUP_CODE) PRICTLIST_ID, ---物料单价id\n" );
		//sql.append(" --     1000 SINGLE_PRICE, ---物料单价\n" );
		//sql.append("  --    10195 PRICTLIST_ID, ---物料单价id\n" );
		sql.append("       TSOD.ORDER_AMOUNT, ---订单数量\n" );
		sql.append("   	    tvw.AVA_STOCK ,\n" );
		sql.append("   	    nvl(tvw.UNDO_ORDER_AMOUNT,0) UNDO_ORDER_AMOUNT,\n" );
		sql.append("       NVL(TSOD.CHECK_AMOUNT, 0) CHECK_AMOUNT, ---已审核数量\n" );
		sql.append("       NVL(TSOD.CALL_AMOUNT, 0) CALL_AMOUNT ,---已申请数量\n" );
		sql.append("TO_NUMBER(NVL(TSOD.RESPOND_AMOUNT, 0)) RES_AMOUNT ,---默认申请数量\n");
		sql.append("TO_NUMBER(NVL(TSOD.CHECK_AMOUNT, 0))-TO_NUMBER(NVL(TSOD.CALL_AMOUNT, 0)) APPLY_AMOUNT ---默认申请数量\n");
		//sql.append("(TO_NUMBER(NVL(TSOD.CHECK_AMOUNT, 0))-TO_NUMBER(NVL(TSOD.CALL_AMOUNT, 0)))*TO_NUMBER(F_GET_MATPRICE(td.dealer_code,TVMG.GROUP_CODE)) TOTAL_PRICE,\n");
	//	sql.append("        TSOD.DISCOUNT_RATE,\n" );
	//	sql.append("       TSOD.DISCOUNT_S_PRICE,\n" );
	//	sql.append("       TSOD.DISCOUNT_PRICE\n" );
		sql.append("  FROM TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		sql.append("       VW_MATERIAL_GROUP        VMG,\n" );
		sql.append("       TM_AREA_GROUP            TAG,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );
		sql.append("        TT_VS_ORDER              TSO,\n" );
		sql.append("  VW_VS_resource_ENTITY_week tvw,");
		sql.append("       TT_VS_ORDER_DETAIL       TSOD,\n" );
		sql.append("       tm_dealer td\n" );
		sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n" );
		sql.append("   AND TSOD.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append("   AND TVMGR.GROUP_ID = VMG.PACKAGE_ID\n" );
		sql.append("   AND tvw.material_id(+) = tvm.material_id\n" );
		sql.append("   AND (VMG.SERIES_ID = TAG.MATERIAL_GROUP_ID OR\n" );
		sql.append("       VMG.MODEL_ID = TAG.MATERIAL_GROUP_ID OR\n" );
		sql.append("       VMG.PACKAGE_ID = TAG.MATERIAL_GROUP_ID)\n" );
		sql.append("   AND TAG.AREA_ID = TSO.AREA_ID\n" );
		sql.append("AND TVMG.GROUP_ID=TVMGR.GROUP_ID\n" );
		sql.append("  AND TVM.MATERIAL_ID=TVMGR.MATERIAL_ID\n" );
		sql.append("  and td.dealer_id=tso.order_org_id\n" );
		sql.append("   AND TSO.ORDER_TYPE = 10201001\n" );
		sql.append("   AND TSO.ORDER_STATUS = 10211005\n" );
		sql.append("  AND TSOD.CHECK_AMOUNT > 0\n" );
		sql.append("  AND NVL(TSOD.CHECK_AMOUNT, 0) <> NVL(TSOD.CALL_AMOUNT, 0)");
		
		if (!CommonUtils.isNullString(orderId)) {
			sql.append(" AND TSO.ORDER_ID = '" + orderId + "'\n");
		}
		if (!CommonUtils.isNullString(week)) {
			sql.append(" and tso.order_week>="+week+"\n");
		}
		if (!CommonUtils.isNullString(week)) {
			sql.append(" and tso.order_year>="+year+"\n");
		}
		if (!CommonUtils.isNullString(endWeek)) {
			sql.append(" and tso.order_week<="+endWeek+"\n");
		}
		if (!CommonUtils.isNullString(endYear)) {
			sql.append(" and tso.order_year<="+endYear+"\n");
		}
		if (!CommonUtils.isNullString(orderNo)) {
			sql.append(" AND TSO.ORDER_NO = '" + orderNo + "'\n");
		}
		if (!CommonUtils.isNullString(isRespond)) {
			if("10041001".equals(isRespond)){
				sql.append(" AND TSO.respond_status =90011003 \n");
			}else{
				sql.append(" and  ( TSO.respond_status is null or TSO.respond_status!=90011003 ) \n");
			}
		}
		if (!"".equals(dealerCodes) && dealerCodes != null) {
			sql.append(" AND Tso.ORDER_ORG_ID  in(select td.dealer_id from tm_dealer td where td.dealer_code in("+CommonUtils.getSplitStringForIn(dealerCodes)+")) \n");
		}
		sql.append("    ORDER BY ORDER_ID\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}

}
