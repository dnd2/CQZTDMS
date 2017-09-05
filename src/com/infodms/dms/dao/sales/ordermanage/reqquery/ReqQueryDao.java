package com.infodms.dms.dao.sales.ordermanage.reqquery;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ReqQueryDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(ReqQueryDao.class);
	private static final ReqQueryDao dao = new ReqQueryDao();
	public static final ReqQueryDao getInstance() {
		return dao;
	}
	private ReqQueryDao() {}
	
	/** 发运申请头表查询
	 * @param map
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public static PageResult<Map<String, Object>> reqQueryHead(Map<String, String> map, int pageSize, int curPage) {
		String areaId = map.get("areaId") ;
		String reqNo = map.get("reqNo") ;
		String orderNo = map.get("orderNo") ;
		String reqStatus = map.get("reqStatus") ;
		String orderType = map.get("orderType") ;
		String reqExeStatus = map.get("reqExeStatus") ;
		String oDlr = map.get("oDlr") ;
		String bDlr = map.get("bDlr") ;
		String rDlr = map.get("rDlr") ;
		String startTime = map.get("startTime") ;
		String endTime = map.get("endTime") ;
		String beginDate = map.get("beginDate") ;
		String overDate = map.get("overDate") ;
		String orgId = map.get("orgId") ;
		String isFleet = map.get("isFleet") ;
		
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("with delivery AS\n");
		sql.append(" (select tvd.req_id,\n");  
		sql.append("         tvd.delivery_id,\n");  
		sql.append("         tvd.billing_date,\n");
		sql.append("         sum(nvl(tvdd.match_amount, 0)) total_match, --发运数量\n");  
		sql.append("         sum(nvl(tvdd.inspection_amount, 0)) total_inspection --验收数量\n");  
		sql.append("    from tt_vs_dlvry tvd, tt_vs_dlvry_dtl tvdd\n");  
		sql.append("   where tvd.delivery_id = tvdd.delivery_id\n");  
		sql.append("     and tvd.delivery_status in\n");  
		sql.append("         (?, ?, ?, ?, ?)\n");  
		params.add(Constant.DELIVERY_STATUS_04) ;
		params.add(Constant.DELIVERY_STATUS_05) ;
		params.add(Constant.DELIVERY_STATUS_10) ;
		params.add(Constant.DELIVERY_STATUS_11) ;
		params.add(Constant.DELIVERY_STATUS_12) ;
		
		sql.append("   group by tvd.req_id, tvd.delivery_id, tvd.billing_date),\n");  
		sql.append("req as\n");  
		sql.append(" (select tvdr.req_id,\n");  
		sql.append("         tvo.area_id,\n");  
		sql.append("         tvo.order_no,\n");  
		sql.append("         tvo.order_type,\n");  
		sql.append("         tvdr.dlvry_req_no,\n");  
		sql.append("         tvdr.delivery_type,\n");  
		sql.append("         tvdr.req_status,\n");  
		sql.append("         tvdr.req_exec_status,\n");
		sql.append("         tvdr.is_fleet,\n");
		sql.append("         bdlr.dealer_id bdid,\n");  
		sql.append("         rdlr.dealer_id rdid,\n");  
		sql.append("         decode(tvdr.call_leavel, ?, odlr2.dealer_id, odlr1.dealer_id) odid,\n");  
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("         bdlr.dealer_shortname bdname,\n");  
		sql.append("         bdlr.dealer_code bdcode,\n");  
		sql.append("         rdlr.dealer_shortname rdname,\n");  
		sql.append("         rdlr.dealer_code rdcode,\n");  
		sql.append("         decode(tvdr.call_leavel,\n");  
		sql.append("                ?,\n");  
		sql.append("                odlr2.dealer_shortname,\n");  
		sql.append("                odlr1.dealer_shortname) odname,\n");  
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("         decode(tvdr.call_leavel,\n");  
		sql.append("                ?,\n");  
		sql.append("                odlr2.dealer_code,\n");  
		sql.append("                odlr1.dealer_code) odcode,\n");  
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("         tvdr.req_date,\n");
		sql.append("         sum(nvl(tvdrd.delivery_amount, 0)) total_delivery, --开票数量\n"); 
		sql.append("         sum(nvl(tvdrd.req_amount, 0)) total_req, --申请数量\n");  
		sql.append("         sum(nvl(tvdrd.reserve_amount, 0)) total_reserve --保留数量\n");  
		sql.append("    from tt_vs_dlvry_req     tvdr,\n");  
		sql.append("         tt_vs_dlvry_req_dtl tvdrd,\n");  
		sql.append("         tt_vs_order         tvo,\n"); 
		sql.append("         tm_dealer           bdlr,\n");  
		sql.append("         tm_dealer           rdlr,\n");  
		sql.append("         tm_dealer           odlr1,\n");  
		sql.append("         tm_dealer           odlr2\n");  
		sql.append("   where tvdr.req_id = tvdrd.req_id\n");  
		sql.append("     and tvdr.order_id = tvo.order_id\n");  
		sql.append("     and tvo.billing_org_id = bdlr.dealer_id\n");  
		sql.append("     and tvdr.receiver = rdlr.dealer_id(+)\n");  
		sql.append("     and tvo.order_org_id = odlr1.dealer_id\n");  
		sql.append("     and tvdr.order_dealer_id = odlr2.dealer_id(+)\n");  
		sql.append("   group by tvdr.req_id,\n");  
		sql.append("            tvo.area_id,\n");  
		sql.append("            tvdr.dlvry_req_no,\n");  
		sql.append("            tvdr.req_status,\n");  
		sql.append("            tvdr.req_exec_status,\n"); 
		sql.append("            tvdr.is_fleet,\n"); 
		sql.append("            tvdr.delivery_type,\n");  
		sql.append("            bdlr.dealer_id,\n");  
		sql.append("            rdlr.dealer_id,\n");  
		sql.append("            odlr2.dealer_id,\n");  
		sql.append("            odlr1.dealer_id,\n");  
		sql.append("            bdlr.dealer_shortname,\n");  
		sql.append("            bdlr.dealer_code,\n");  
		sql.append("            rdlr.dealer_shortname,\n");  
		sql.append("            rdlr.dealer_code,\n");  
		sql.append("            tvdr.call_leavel,\n");  
		sql.append("            odlr2.dealer_shortname,\n");  
		sql.append("            odlr2.dealer_code,\n");  
		sql.append("            odlr1.dealer_shortname,\n");  
		sql.append("            odlr1.dealer_code,\n");  
		sql.append("            tvo.order_no,\n");  
		sql.append("            tvo.order_type,\n");  
		sql.append("            tvdr.req_date)\n");  
		sql.append("select req.req_id,\n");  
		sql.append("       req.order_no,\n");  
		sql.append("       req.order_type,\n");  
		sql.append("       req.dlvry_req_no,\n");  
		sql.append("       req.req_status,\n");  
		sql.append("       req.delivery_type,\n");  
		sql.append("       req.bdid,\n");  
		sql.append("       req.bdname || '-' || req.bdcode binfo,\n");  
		sql.append("       req.odid,\n");  
		sql.append("       req.odname || '-' || req.odcode oinfo,\n");  
		sql.append("       req.rdid,\n");  
		sql.append("       req.rdname || '-' || req.rdcode rinfo,\n");  
		sql.append("       to_char(req.req_date, 'yyyy-mm-dd') req_date,\n");  
		sql.append("       decode(req.req_exec_status, ?, 0, 1) kinfo, --0，尚未开票；1，已开票\n");  
		params.add(Constant.REQ_EXEC_STATUS_01) ;
		
		sql.append("       delivery.delivery_id,\n");  
		sql.append("       req.total_req,\n");  
		sql.append("       req.total_reserve,\n");  
		sql.append("       nvl(req.total_delivery, 0) total_delivery,\n");  
		sql.append("       nvl(delivery.total_match, 0) total_match,\n");  
		sql.append("       nvl(delivery.total_inspection, 0) total_inspection\n");  
		sql.append("  from req, delivery\n");  
		sql.append(" where req.req_id = delivery.req_id(+)\n");
		
		if(!CommonUtils.isNullString(isFleet)) {
			if(Constant.IF_TYPE_YES.toString().equals(isFleet))
				sql.append("  and req.is_fleet = ").append(1).append("\n");
			else if(Constant.IF_TYPE_NO.toString().equals(isFleet))
				sql.append("  and req.is_fleet = ").append(0).append("\n");
		}
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("  and req.area_Id in (").append(areaId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(reqNo)) {
			sql.append("  and req.dlvry_req_no like '%").append(reqNo).append("%'\n");
		}
		
		if(!CommonUtils.isNullString(orderNo)) {
			sql.append("  and req.order_no like '%").append(orderNo).append("%'\n");
		}
		
		if(!CommonUtils.isNullString(orderType)) {
			sql.append("  and req.order_type = ").append(orderType).append("\n");
		}
		
		if(!CommonUtils.isNullString(reqStatus)) {
			sql.append("  and req.req_status = ").append(reqStatus).append("\n");
		}
		
		if(!CommonUtils.isNullString(reqExeStatus)) {
			sql.append("  and req.req_exec_status = ").append(reqExeStatus).append("\n");
		}
		
		if(!CommonUtils.isNullString(oDlr)) {
			oDlr = "'" + oDlr.replaceAll(",", "','") + "'";
			sql.append("  and req.odcode in (").append(oDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(bDlr)) {
			bDlr = "'" + bDlr.replaceAll(",", "','") + "'";
			sql.append("  and req.bdcode in (").append(bDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(rDlr)) {
			rDlr = "'" + rDlr.replaceAll(",", "','") + "'";
			sql.append("  and req.rdcode in (").append(rDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(startTime)) {
			sql.append("  and trunc(req.req_date) >= to_date('").append(startTime).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(endTime)) {
			sql.append("  and trunc(req.req_date) <= to_date('").append(endTime).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(beginDate)) {
			sql.append("  and trunc(delivery.billing_date) >= to_date('").append(beginDate).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(overDate)) {
			sql.append("  and trunc(delivery.billing_date) <= to_date('").append(overDate).append("','yyyy-mm-dd')\n");
		}
		
		sql.append(limitOrg("req", "bdid", orgId)) ;
		
		sql.append(" order by req.req_date desc\n");

		PageResult<Map<String, Object>> reqResult = dao.pageQuery(sql.toString(), params, dao.getFunName(), pageSize, curPage) ;
		
		return reqResult ;
	}
	
	public static PageResult<Map<String, Object>> reqQueryTotal(Map<String, String> map, int pageSize, int curPage) {
		String areaId = map.get("areaId") ;
		String reqNo = map.get("reqNo") ;
		String orderNo = map.get("orderNo") ;
		String reqStatus = map.get("reqStatus") ;
		String orderType = map.get("orderType") ;
		String reqExeStatus = map.get("reqExeStatus") ;
		String oDlr = map.get("oDlr") ;
		String bDlr = map.get("bDlr") ;
		String rDlr = map.get("rDlr") ;
		String startTime = map.get("startTime") ;
		String endTime = map.get("endTime") ;
		String beginDate = map.get("beginDate") ;
		String overDate = map.get("overDate") ;
		String materialCode = map.get("materialCode") ;
		String groupCode = map.get("groupCode") ;
		String orgId = map.get("orgId") ;
		String isFleet = map.get("isFleet") ;
		
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("with delivery AS\n");
		sql.append(" (select tvd.req_id,\n");  
		sql.append("         tvd.delivery_id,\n");  
		sql.append("         tvd.billing_date,\n");
		sql.append("         tvm.material_code,\n");  
		sql.append("         tvm.material_name,\n");  
		sql.append("         tvm.material_id,\n");  
		sql.append("         sum(nvl(tvdd.match_amount, 0)) total_match, --发运数量\n");  
		sql.append("         sum(nvl(tvdd.inspection_amount, 0)) total_inspection --验收数量\n");  
		sql.append("    from tt_vs_dlvry tvd, tt_vs_dlvry_dtl tvdd, tm_vhcl_material tvm\n");  
		sql.append("   where tvd.delivery_id = tvdd.delivery_id\n");  
		sql.append("     and tvdd.material_id = tvm.material_id\n");  
		sql.append("     and tvd.delivery_status in\n");  
		sql.append("         (?, ?, ?, ?, ?)\n");  
		params.add(Constant.DELIVERY_STATUS_04) ;
		params.add(Constant.DELIVERY_STATUS_05) ;
		params.add(Constant.DELIVERY_STATUS_10) ;
		params.add(Constant.DELIVERY_STATUS_11) ;
		params.add(Constant.DELIVERY_STATUS_12) ;
		
		sql.append("   group by tvd.req_id,\n");  
		sql.append("            tvd.delivery_id,\n"); 
		sql.append("            tvd.billing_date,\n"); 
		sql.append("            tvm.material_id,\n");  
		sql.append("            tvm.material_code,\n");  
		sql.append("            tvm.material_name),\n");  
		sql.append("req as\n");  
		sql.append(" (select tvdr.req_id,\n");  
		sql.append("         tvo.area_id,\n");  
		sql.append("         tvo.order_no,\n");  
		sql.append("         tvo.order_type,\n");  
		sql.append("         tvdr.dlvry_req_no,\n");  
		sql.append("         tvdr.req_exec_status,\n");  
		sql.append("         tvdr.req_status,\n");  
		sql.append("         tvdr.is_fleet,\n"); 
		sql.append("         bdlr.dealer_id bdid,\n");  
		sql.append("         bdlr.dealer_code bdcode,\n");  
		sql.append("         rdlr.dealer_code rdcode,\n");  
		sql.append("         decode(tvdr.call_leavel,\n");  
		sql.append("                ?,\n");  
		sql.append("                odlr2.dealer_code,\n");  
		sql.append("                odlr1.dealer_code) odcode,\n");
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("         tvdr.req_date,\n");  
		sql.append("         tvm.material_code,\n");  
		sql.append("         tvm.material_name,\n"); 
		sql.append("         tvm.material_id,\n"); 
		sql.append("         nvl(tvdrd.delivery_amount, 0) total_delivery, --开票数量\n");
		sql.append("         nvl(tvdrd.req_amount, 0) total_req, --申请数量\n");  
		sql.append("         nvl(tvdrd.reserve_amount, 0) total_reserve --保留数量\n");  
		sql.append("    from tt_vs_dlvry_req     tvdr,\n");  
		sql.append("         tt_vs_dlvry_req_dtl tvdrd,\n");  
		sql.append("         tt_vs_order         tvo,\n"); 
		sql.append("         tm_vhcl_material    tvm,\n");  
		sql.append("         tm_dealer           bdlr,\n");  
		sql.append("         tm_dealer           rdlr,\n");  
		sql.append("         tm_dealer           odlr1,\n");  
		sql.append("         tm_dealer           odlr2\n");  
		sql.append("   where tvdr.req_id = tvdrd.req_id\n");  
		sql.append("     and tvdrd.material_id = tvm.material_id\n");  
		sql.append("     and tvdr.order_id = tvo.order_id\n"); 
		sql.append("     and tvo.billing_org_id = bdlr.dealer_id\n");  
		sql.append("     and tvdr.receiver = rdlr.dealer_id\n");  
		sql.append("     and tvo.order_org_id = odlr1.dealer_id\n");  
		sql.append("     and tvdr.order_dealer_id = odlr2.dealer_id(+)\n"); 
		sql.append("     and tvdr.req_status <> ?)\n"); 
		params.add(Constant.ORDER_REQ_STATUS_07) ;
		
		sql.append("select req.material_code,\n");  
		sql.append("       req.material_name,\n");   
		sql.append("       sum(req.total_req) total_req,\n");  
		sql.append("       sum(req.total_reserve) total_reserve,\n");  
		sql.append("       sum(req.total_delivery) total_delivery,\n");  
		sql.append("       sum(nvl(delivery.total_match, 0)) total_match,\n");  
		sql.append("       sum(nvl(delivery.total_inspection, 0)) total_inspection\n");  
		sql.append("  from req, delivery\n");  
		sql.append(" where req.req_id = delivery.req_id(+)\n");  
		sql.append(" and req.material_id = delivery.material_id(+)\n");  
		
		if(!CommonUtils.isNullString(isFleet)) {
			if(Constant.IF_TYPE_YES.toString().equals(isFleet))
				sql.append("  and req.is_fleet = ").append(1).append("\n");
			else if(Constant.IF_TYPE_NO.toString().equals(isFleet))
				sql.append("  and req.is_fleet = ").append(0).append("\n");
		}
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("  and req.area_Id in (").append(areaId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(reqNo)) {
			sql.append("  and req.dlvry_req_no like '%").append(reqNo).append("%'\n");
		}
		
		if(!CommonUtils.isNullString(orderNo)) {
			sql.append("  and req.order_no like '%").append(orderNo).append("%'\n");
		}
		
		if(!CommonUtils.isNullString(orderType)) {
			sql.append("  and req.order_type = ").append(orderType).append("\n");
		}
		
		if(!CommonUtils.isNullString(reqStatus)) {
			sql.append("  and req.req_status = ").append(reqStatus).append("\n");
		}
		
		if(!CommonUtils.isNullString(reqExeStatus)) {
			sql.append("  and req.req_exec_status = ").append(reqExeStatus).append("\n");
		}
		
		if(!CommonUtils.isNullString(oDlr)) {
			oDlr = "'" + oDlr.replaceAll(",", "','") + "'";
			sql.append("  and req.odcode in (").append(oDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(bDlr)) {
			bDlr = "'" + bDlr.replaceAll(",", "','") + "'";
			sql.append("  and req.bdcode in (").append(bDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(rDlr)) {
			rDlr = "'" + rDlr.replaceAll(",", "','") + "'";
			sql.append("  and req.rdcode in (").append(rDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(startTime)) {
			sql.append("  and trunc(req.req_date) >= to_date('").append(startTime).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(endTime)) {
			sql.append("  and trunc(req.req_date) <= to_date('").append(endTime).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(beginDate)) {
			sql.append("  and trunc(delivery.billing_date) >= to_date('").append(beginDate).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(overDate)) {
			sql.append("  and trunc(delivery.billing_date) <= to_date('").append(overDate).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(materialCode)) {
			materialCode = "'" + materialCode.replaceAll(",", "','") + "'";
			sql.append("  and req.material_code in (").append(materialCode).append(")\n");
		}
		
		if(!CommonUtils.isNullString(groupCode)) {
			groupCode = "'" + groupCode.replaceAll(",", "','") + "'";
			sql.append("and exists\n");
			sql.append("(select 1\n");  
			sql.append("  from vw_material_group vmg, tm_vhcl_material_group_r tvmgr\n");  
			sql.append(" where tvmgr.group_id = vmg.PACKAGE_ID\n");  
			sql.append("   and tvmgr.material_id = req.material_id\n");  
			sql.append("   and (vmg.SERIES_CODE in (").append(groupCode).append(") or vmg.MODEL_CODE in (").append(groupCode).append(") or\n");  
			sql.append("       vmg.PACKAGE_CODE in (").append(groupCode).append(") or  vmg.BRAND_CODE in (").append(groupCode).append(")))\n");

		}
		
		sql.append(limitOrg("req", "bdid", orgId)) ;
		
		sql.append(" group by req.material_code, req.material_name\n");

		PageResult<Map<String, Object>> totalResult = dao.pageQuery(sql.toString(), params, dao.getFunName(), pageSize, curPage) ;
		
		return totalResult ;
	}
	
	public static PageResult<Map<String, Object>> reqQueryRes(Map<String, String> map, int pageSize, int curPage) {
		String areaId = map.get("areaId") ;
		String reqNo = map.get("reqNo") ;
		String orderNo = map.get("orderNo") ;
		String reqStatus = map.get("reqStatus") ;
		String orderType = map.get("orderType") ;
		String reqExeStatus = map.get("reqExeStatus") ;
		String oDlr = map.get("oDlr") ;
		String bDlr = map.get("bDlr") ;
		String rDlr = map.get("rDlr") ;
		String startTime = map.get("startTime") ;
		String endTime = map.get("endTime") ;
		String beginDate = map.get("beginDate") ;
		String overDate = map.get("overDate") ;
		String materialCode = map.get("materialCode") ;
		String groupCode = map.get("groupCode") ;
		String orgId = map.get("orgId") ;
		String isFleet = map.get("isFleet") ;
		
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvdr.req_id,\n");
		sql.append("       tvo.order_no,\n");  
		sql.append("       tvo.order_type,\n");  
		sql.append("       tvdr.dlvry_req_no,\n");  
		sql.append("       tvdr.req_status,\n");  
		sql.append("       to_char(tvdr.req_date, 'yyyy-mm-dd') req_date,\n");  
		sql.append("       tvdr.req_total_amount,\n");  
		sql.append("       tvmg.group_code,\n");  
		sql.append("       tvm.material_code,\n");  
		sql.append("       tvorr.batch_no,\n");
		sql.append("       nvl(tvdrd.req_amount, 0) req_amount,\n");  
		sql.append("       nvl(tvorr.amount, 0) reserve_amount,\n");
		sql.append("       bdlr.dealer_shortname || '-' || bdlr.dealer_code binfo,\n");  
		sql.append("       rdlr.dealer_shortname || '-' || rdlr.dealer_code rinfo,\n"); 
		sql.append("       decode(tvdr.call_leavel,\n");  
		sql.append("              ?,\n");  
		sql.append("              odlr2.dealer_shortname || '-' || odlr2.dealer_code,\n");  
		sql.append("              odlr1.dealer_shortname || '-' || odlr1.dealer_code) oinfo\n");  
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("  from tt_vs_dlvry_req          tvdr,\n");  
		sql.append("       tt_vs_dlvry_req_dtl      tvdrd,\n"); 
		sql.append("       tt_vs_order_resource_reserve tvorr,\n");
		sql.append("       tt_vs_order              tvo,\n");  
		sql.append("       tm_vhcl_material         tvm,\n");  
		sql.append("       tm_vhcl_material_group_r tvmgr,\n");  
		sql.append("       tm_vhcl_material_group   tvmg,\n");  
		sql.append("       tm_dealer                bdlr,\n");  
		sql.append("       tm_dealer                rdlr,\n");  
		sql.append("       tm_dealer                odlr1,\n");  
		sql.append("       tm_dealer                odlr2\n");  
		sql.append(" where tvdr.req_id = tvdrd.req_id\n");  
		sql.append("   and tvdrd.detail_id = tvorr.req_detail_id(+)\n");
		sql.append("   and tvdr.order_id = tvo.order_id\n");  
		sql.append("   and tvdrd.material_id = tvm.material_id\n");  
		sql.append("   and tvm.material_id = tvmgr.material_id\n");  
		sql.append("   and tvmgr.group_id = tvmg.group_id\n");  
		sql.append("   and tvo.billing_org_id = bdlr.dealer_id\n");  
		sql.append("   and tvdr.receiver = rdlr.dealer_id(+)\n");  
		sql.append("   and tvo.order_org_id = odlr1.dealer_id\n");  
		sql.append("   and tvdr.order_dealer_id = odlr2.dealer_id(+)\n");

		if(!CommonUtils.isNullString(isFleet)) {
			if(Constant.IF_TYPE_YES.toString().equals(isFleet))
				sql.append("  and tvdr.is_fleet = ").append(1).append("\n");
			else if(Constant.IF_TYPE_NO.toString().equals(isFleet))
				sql.append("  and tvdr.is_fleet = ").append(0).append("\n");
		}
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("  and tvo.area_Id in (").append(areaId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(reqNo)) {
			sql.append("  and tvdr.dlvry_req_no like '%").append(reqNo).append("%'\n");
		}
		
		if(!CommonUtils.isNullString(orderNo)) {
			sql.append("  and tvo.order_no like '%").append(orderNo).append("%'\n");
		}
		
		if(!CommonUtils.isNullString(orderType)) {
			sql.append("  and tvo.order_type = ").append(orderType).append("\n");
		}
		
		if(!CommonUtils.isNullString(reqStatus)) {
			sql.append("  and tvdr.req_status = ").append(reqStatus).append("\n");
		}
		
		if(!CommonUtils.isNullString(reqExeStatus)) {
			sql.append("  and tvdr.req_exec_status = ").append(reqExeStatus).append("\n");
		}
		
		if(!CommonUtils.isNullString(oDlr)) {
			oDlr = "'" + oDlr.replaceAll(",", "','") + "'";
			sql.append("  and ((odlr1.dealer_code in (").append(oDlr).append(") and tvdr.call_leavel is null) or (odlr2.dealer_code in (").append(oDlr).append(")))\n");
		}
		
		if(!CommonUtils.isNullString(bDlr)) {
			bDlr = "'" + bDlr.replaceAll(",", "','") + "'";
			sql.append("  and bdlr.dealer_code in (").append(bDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(rDlr)) {
			rDlr = "'" + rDlr.replaceAll(",", "','") + "'";
			sql.append("  and rdlr.dealer_code (").append(rDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(startTime)) {
			sql.append("  and trunc(tvdr.req_date) >= to_date('").append(startTime).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(endTime)) {
			sql.append("  and trunc(tvdr.req_date) <= to_date('").append(endTime).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(beginDate) || !CommonUtils.isNullString(overDate)) {
			sql.append("and exists(\n");
			sql.append("select 1\n");  
			sql.append("  from tt_vs_dlvry tvd\n");  
			sql.append(" where tvd.req_id = tvdr.req_id\n");  
			
			if(!CommonUtils.isNullString(beginDate)) {
				sql.append("   and trunc(tvd.billing_date) >= to_date('").append(beginDate).append("', 'yyyy-mm-dd')\n"); 
			}
			
			if(!CommonUtils.isNullString(overDate)) {
				sql.append("   and trunc(tvd.billing_date) <= to_date('").append(overDate).append("', 'yyyy-mm-dd')\n");  
			}
			
			sql.append("   )\n");
		}
		
		if(!CommonUtils.isNullString(materialCode)) {
			materialCode = "'" + materialCode.replaceAll(",", "','") + "'";
			sql.append("  and tvm.material_code in (").append(materialCode).append(")\n");
		}
		
		if(!CommonUtils.isNullString(groupCode)) {
			groupCode = "'" + groupCode.replaceAll(",", "','") + "'";
			sql.append("and exists\n");
			sql.append("(select 1\n");  
			sql.append("  from vw_material_group vmg, tm_vhcl_material_group_r tvmgr\n");  
			sql.append(" where tvmgr.group_id = vmg.PACKAGE_ID\n");  
			sql.append("   and tvmgr.material_id = tvm.material_id\n");  
			sql.append("   and (vmg.SERIES_CODE in (").append(groupCode).append(") or vmg.MODEL_CODE in (").append(groupCode).append(") or\n");  
			sql.append("       vmg.PACKAGE_CODE in (").append(groupCode).append(") or  vmg.BRAND_CODE in (").append(groupCode).append(")))\n");

		}
		
		sql.append(limitOrg("bdlr", "dealer_id", orgId)) ;
		
		sql.append(" order by tvdr.req_date desc\n");
		
		PageResult<Map<String, Object>> resResult = dao.pageQuery(sql.toString(), params, dao.getFunName(), pageSize, curPage) ;
		
		return resResult ;
	}
	
	/******************************************************          Download            ********************************************************/
	public static List<Map<String, Object>> reqDownloadHead(Map<String, String> map) {
		String areaId = map.get("areaId") ;
		String reqNo = map.get("reqNo") ;
		String orderNo = map.get("orderNo") ;
		String reqStatus = map.get("reqStatus") ;
		String orderType = map.get("orderType") ;
		String reqExeStatus = map.get("reqExeStatus") ;
		String oDlr = map.get("oDlr") ;
		String bDlr = map.get("bDlr") ;
		String rDlr = map.get("rDlr") ;
		String startTime = map.get("startTime") ;
		String endTime = map.get("endTime") ;
		String beginDate = map.get("beginDate") ;
		String overDate = map.get("overDate") ;
		String orgId = map.get("orgId") ;
		String isFleet = map.get("isFleet") ;
		
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("with delivery AS\n");
		sql.append(" (select tvd.req_id,\n");  
		sql.append("         tvd.delivery_id,\n"); 
		sql.append("         tvd.billing_date,\n");
		sql.append("         sum(nvl(tvdd.match_amount, 0)) total_match, --发运数量\n");  
		sql.append("         sum(nvl(tvdd.inspection_amount, 0)) total_inspection --验收数量\n");  
		sql.append("    from tt_vs_dlvry tvd, tt_vs_dlvry_dtl tvdd\n");  
		sql.append("   where tvd.delivery_id = tvdd.delivery_id\n");  
		sql.append("     and tvd.delivery_status in\n");  
		sql.append("         (?, ?, ?, ?, ?)\n");  
		params.add(Constant.DELIVERY_STATUS_04) ;
		params.add(Constant.DELIVERY_STATUS_05) ;
		params.add(Constant.DELIVERY_STATUS_10) ;
		params.add(Constant.DELIVERY_STATUS_11) ;
		params.add(Constant.DELIVERY_STATUS_12) ;
		
		sql.append("   group by tvd.req_id, tvd.delivery_id, tvd.billing_date),\n");  
		sql.append("req as\n");  
		sql.append(" (select tvdr.req_id,\n");  
		sql.append("         tvo.area_id,\n");  
		sql.append("         tvo.order_no,\n");  
		sql.append("         tvo.order_type,\n");  
		sql.append("         tcc1.code_desc order_type_desc,\n");
		sql.append("         tvdr.dlvry_req_no,\n");  
		sql.append("         tvdr.delivery_type,\n"); 
		sql.append("         tvdr.is_fleet,\n"); 
		sql.append("         tcc2.code_desc req_status_desc,\n");
		sql.append("         tvdr.req_status,\n"); 
		sql.append("         tcc3.code_desc delivery_type_desc,\n");
		sql.append("         tvdr.req_exec_status,\n");
		sql.append("         bdlr.dealer_id bdid,\n");  
		sql.append("         rdlr.dealer_id rdid,\n");  
		sql.append("         decode(tvdr.call_leavel, ?, odlr2.dealer_id, odlr1.dealer_id) odid,\n");  
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("         bdlr.dealer_shortname bdname,\n");  
		sql.append("         bdlr.dealer_code bdcode,\n");  
		sql.append("         rdlr.dealer_shortname rdname,\n");  
		sql.append("         rdlr.dealer_code rdcode,\n");  
		sql.append("         decode(tvdr.call_leavel,\n");  
		sql.append("                ?,\n");  
		sql.append("                odlr2.dealer_shortname,\n");  
		sql.append("                odlr1.dealer_shortname) odname,\n");  
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("         decode(tvdr.call_leavel,\n");  
		sql.append("                ?,\n");  
		sql.append("                odlr2.dealer_code,\n");  
		sql.append("                odlr1.dealer_code) odcode,\n");  
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("         tvdr.req_date,\n");
		sql.append("         sum(nvl(tvdrd.delivery_amount, 0)) total_delivery, --开票数量\n"); 
		sql.append("         sum(nvl(tvdrd.req_amount, 0)) total_req, --申请数量\n");  
		sql.append("         sum(nvl(tvdrd.reserve_amount, 0)) total_reserve --保留数量\n");  
		sql.append("    from tt_vs_dlvry_req     tvdr,\n");  
		sql.append("         tt_vs_dlvry_req_dtl tvdrd,\n");  
		sql.append("         tt_vs_order         tvo,\n"); 
		sql.append("         tc_code             tcc1,\n");  
		sql.append("         tc_code             tcc2,\n");  
		sql.append("         tc_code             tcc3,\n"); 
		sql.append("         tm_dealer           bdlr,\n");  
		sql.append("         tm_dealer           rdlr,\n");  
		sql.append("         tm_dealer           odlr1,\n");  
		sql.append("         tm_dealer           odlr2\n");  
		sql.append("   where tvdr.req_id = tvdrd.req_id\n");  
		sql.append("     and tvdr.order_id = tvo.order_id\n");  
		sql.append("     and tvo.billing_org_id = bdlr.dealer_id\n");  
		sql.append("     and tvo.order_type = tcc1.code_id\n");  
		sql.append("     and tvdr.req_status = tcc2.code_id\n");  
		sql.append("     and tvdr.delivery_type = tcc3.code_id(+)\n");  
		sql.append("     and tvdr.receiver = rdlr.dealer_id(+)\n");  
		sql.append("     and tvo.order_org_id = odlr1.dealer_id\n");  
		sql.append("     and tvdr.order_dealer_id = odlr2.dealer_id(+)\n");  
		sql.append("   group by tvdr.req_id,\n");  
		sql.append("            tvo.area_id,\n");  
		sql.append("            tcc1.code_desc,\n");
		sql.append("            tcc2.code_desc,\n");
		sql.append("            tcc3.code_desc,\n");
		sql.append("            tvdr.dlvry_req_no,\n");  
		sql.append("            tvdr.req_status,\n");  
		sql.append("            tvdr.is_fleet,\n"); 
		sql.append("            tvdr.req_exec_status,\n"); 
		sql.append("            tvdr.delivery_type,\n");  
		sql.append("            bdlr.dealer_id,\n");  
		sql.append("            rdlr.dealer_id,\n");  
		sql.append("            odlr2.dealer_id,\n");  
		sql.append("            odlr1.dealer_id,\n");  
		sql.append("            bdlr.dealer_shortname,\n");  
		sql.append("            bdlr.dealer_code,\n");  
		sql.append("            rdlr.dealer_shortname,\n");  
		sql.append("            rdlr.dealer_code,\n");  
		sql.append("            tvdr.call_leavel,\n");  
		sql.append("            odlr2.dealer_shortname,\n");  
		sql.append("            odlr2.dealer_code,\n");  
		sql.append("            odlr1.dealer_shortname,\n");  
		sql.append("            odlr1.dealer_code,\n");  
		sql.append("            tvo.order_no,\n");  
		sql.append("            tvo.order_type,\n");  
		sql.append("            tvdr.req_date)\n");  
		sql.append("select req.req_id,\n");  
		sql.append("       req.order_no,\n");  
		sql.append("       req.order_type_desc,\n");  //--
		sql.append("       req.dlvry_req_no,\n");  
		sql.append("       req.req_status_desc,\n");  //--
		sql.append("       req.delivery_type_desc,\n");  //--
		sql.append("       req.bdid,\n");  
		sql.append("       req.bdname || '-' || req.bdcode binfo,\n");  
		sql.append("       req.odid,\n");  
		sql.append("       req.odname || '-' || req.odcode oinfo,\n");  
		sql.append("       req.rdid,\n");  
		sql.append("       req.rdname || '-' || req.rdcode rinfo,\n");  
		sql.append("       to_char(req.req_date, 'yyyy-mm-dd') req_date,\n");  
		sql.append("       decode(req.req_exec_status, ?, '未开票', '已开票') kinfo, --0，未开票；1，已开票\n");  
		params.add(Constant.REQ_EXEC_STATUS_01) ;
		
		sql.append("       delivery.delivery_id,\n");  
		sql.append("       req.total_req,\n");  
		sql.append("       req.total_reserve,\n");  
		sql.append("       nvl(req.total_delivery, 0) total_delivery,\n");  
		sql.append("       nvl(delivery.total_match, 0) total_match,\n");  
		sql.append("       nvl(delivery.total_inspection, 0) total_inspection\n");  
		sql.append("  from req, delivery\n");  
		sql.append(" where req.req_id = delivery.req_id(+)\n");
		
		if(!CommonUtils.isNullString(isFleet)) {
			if(Constant.IF_TYPE_YES.toString().equals(isFleet))
				sql.append("  and req.is_fleet = ").append(1).append("\n");
			else if(Constant.IF_TYPE_NO.toString().equals(isFleet))
				sql.append("  and req.is_fleet = ").append(0).append("\n");
		}
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("  and req.area_Id in (").append(areaId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(reqNo)) {
			sql.append("  and req.dlvry_req_no like '%").append(reqNo).append("%'\n");
		}
		
		if(!CommonUtils.isNullString(orderNo)) {
			sql.append("  and req.order_no like '%").append(orderNo).append("%'\n");
		}
		
		if(!CommonUtils.isNullString(orderType)) {
			sql.append("  and req.order_type = ").append(orderType).append("\n");
		}
		
		if(!CommonUtils.isNullString(reqStatus)) {
			sql.append("  and req.req_status = ").append(reqStatus).append("\n");
		}
		
		if(!CommonUtils.isNullString(reqExeStatus)) {
			sql.append("  and req.req_exec_status = ").append(reqExeStatus).append("\n");
		}
		
		if(!CommonUtils.isNullString(oDlr)) {
			oDlr = "'" + oDlr.replaceAll(",", "','") + "'";
			sql.append("  and req.odcode in (").append(oDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(bDlr)) {
			bDlr = "'" + bDlr.replaceAll(",", "','") + "'";
			sql.append("  and req.bdcode in (").append(bDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(rDlr)) {
			rDlr = "'" + rDlr.replaceAll(",", "','") + "'";
			sql.append("  and req.rdcode in (").append(rDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(startTime)) {
			sql.append("  and trunc(req.req_date) >= to_date('").append(startTime).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(endTime)) {
			sql.append("  and trunc(req.req_date) <= to_date('").append(endTime).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(beginDate)) {
			sql.append("  and trunc(delivery.billing_date) >= to_date('").append(beginDate).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(overDate)) {
			sql.append("  and trunc(delivery.billing_date) <= to_date('").append(overDate).append("','yyyy-mm-dd')\n");
		}
		
		sql.append(limitOrg("req", "bdid", orgId)) ;
		
		sql.append(" order by req.req_date desc\n");

		List<Map<String, Object>> reqList = dao.pageQuery(sql.toString(), params, dao.getFunName()) ;
		
		return reqList ;
	}
	
	public static List<Map<String, Object>> reqDownloadTotal(Map<String, String> map) {
		String areaId = map.get("areaId") ;
		String reqNo = map.get("reqNo") ;
		String orderNo = map.get("orderNo") ;
		String reqStatus = map.get("reqStatus") ;
		String orderType = map.get("orderType") ;
		String reqExeStatus = map.get("reqExeStatus") ;
		String oDlr = map.get("oDlr") ;
		String bDlr = map.get("bDlr") ;
		String rDlr = map.get("rDlr") ;
		String startTime = map.get("startTime") ;
		String endTime = map.get("endTime") ;
		String beginDate = map.get("beginDate") ;
		String overDate = map.get("overDate") ;
		String materialCode = map.get("materialCode") ;
		String groupCode = map.get("groupCode") ;
		String orgId = map.get("orgId") ;
		String isFleet = map.get("isFleet") ;
		
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("with delivery AS\n");
		sql.append(" (select tvd.req_id,\n");  
		sql.append("         tvd.delivery_id,\n"); 
		sql.append("         tvd.billing_date,\n");
		sql.append("         tvm.material_code,\n");  
		sql.append("         tvm.material_name,\n");  
		sql.append("         tvm.material_id,\n");  
		sql.append("         sum(nvl(tvdd.match_amount, 0)) total_match, --发运数量\n");  
		sql.append("         sum(nvl(tvdd.inspection_amount, 0)) total_inspection --验收数量\n");  
		sql.append("    from tt_vs_dlvry tvd, tt_vs_dlvry_dtl tvdd, tm_vhcl_material tvm\n");  
		sql.append("   where tvd.delivery_id = tvdd.delivery_id\n");  
		sql.append("     and tvdd.material_id = tvm.material_id\n");  
		sql.append("     and tvd.delivery_status in\n");  
		sql.append("         (?, ?, ?, ?, ?)\n");  
		params.add(Constant.DELIVERY_STATUS_04) ;
		params.add(Constant.DELIVERY_STATUS_05) ;
		params.add(Constant.DELIVERY_STATUS_10) ;
		params.add(Constant.DELIVERY_STATUS_11) ;
		params.add(Constant.DELIVERY_STATUS_12) ;
		
		sql.append("   group by tvd.req_id,\n");  
		sql.append("            tvd.delivery_id,\n"); 
		sql.append("            tvd.billing_date,\n"); 
		sql.append("            tvm.material_id,\n");  
		sql.append("            tvm.material_code,\n");  
		sql.append("            tvm.material_name),\n");  
		sql.append("req as\n");  
		sql.append(" (select tvdr.req_id,\n");  
		sql.append("         tvo.area_id,\n");  
		sql.append("         tvo.order_no,\n");  
		sql.append("         tvo.order_type,\n");  
		sql.append("         tvdr.dlvry_req_no,\n");  
		sql.append("         tvdr.req_exec_status,\n");  
		sql.append("         tvdr.req_status,\n");  
		sql.append("         tvdr.is_fleet,\n"); 
		sql.append("         bdlr.dealer_id bdid,\n");  
		sql.append("         bdlr.dealer_code bdcode,\n");  
		sql.append("         rdlr.dealer_code rdcode,\n");  
		sql.append("         decode(tvdr.call_leavel,\n");  
		sql.append("                ?,\n");  
		sql.append("                odlr2.dealer_code,\n");  
		sql.append("                odlr1.dealer_code) odcode,\n");
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("         tvdr.req_date,\n");  
		sql.append("         tvm.material_code,\n");  
		sql.append("         tvm.material_name,\n"); 
		sql.append("         tvm.material_id,\n"); 
		sql.append("         nvl(tvdrd.delivery_amount, 0) total_delivery, --开票数量\n"); 
		sql.append("         nvl(tvdrd.req_amount, 0) total_req, --申请数量\n");  
		sql.append("         nvl(tvdrd.reserve_amount, 0) total_reserve --保留数量\n");  
		sql.append("    from tt_vs_dlvry_req     tvdr,\n");  
		sql.append("         tt_vs_dlvry_req_dtl tvdrd,\n");  
		sql.append("         tt_vs_order         tvo,\n");  
		sql.append("         tm_vhcl_material    tvm,\n");  
		sql.append("         tm_dealer           bdlr,\n");  
		sql.append("         tm_dealer           rdlr,\n");  
		sql.append("         tm_dealer           odlr1,\n");  
		sql.append("         tm_dealer           odlr2\n");  
		sql.append("   where tvdr.req_id = tvdrd.req_id\n");  
		sql.append("     and tvdrd.material_id = tvm.material_id\n");  
		sql.append("     and tvdr.order_id = tvo.order_id\n"); 
		sql.append("     and tvo.billing_org_id = bdlr.dealer_id\n");  
		sql.append("     and tvdr.receiver = rdlr.dealer_id\n");  
		sql.append("     and tvo.order_org_id = odlr1.dealer_id\n");  
		sql.append("     and tvdr.order_dealer_id = odlr2.dealer_id(+)\n"); 
		sql.append("     and tvdr.req_status <> ?)\n"); 
		params.add(Constant.ORDER_REQ_STATUS_07) ;
		
		sql.append("select req.material_code,\n");  
		sql.append("       req.material_name,\n");   
		sql.append("       sum(req.total_req) total_req,\n");  
		sql.append("       sum(req.total_reserve) total_reserve,\n");  
		sql.append("       sum(req.total_delivery) total_delivery,\n");  
		sql.append("       sum(nvl(delivery.total_match, 0)) total_match,\n");  
		sql.append("       sum(nvl(delivery.total_inspection, 0)) total_inspection\n");  
		sql.append("  from req, delivery\n");  
		sql.append(" where req.req_id = delivery.req_id(+)\n");  
		sql.append(" and req.material_id = delivery.material_id(+)\n");  
		
		if(!CommonUtils.isNullString(isFleet)) {
			if(Constant.IF_TYPE_YES.toString().equals(isFleet))
				sql.append("  and req.is_fleet = ").append(1).append("\n");
			else if(Constant.IF_TYPE_NO.toString().equals(isFleet))
				sql.append("  and req.is_fleet = ").append(0).append("\n");
		}
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("  and req.area_Id in (").append(areaId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(reqNo)) {
			sql.append("  and req.dlvry_req_no like '%").append(reqNo).append("%'\n");
		}
		
		if(!CommonUtils.isNullString(orderNo)) {
			sql.append("  and req.order_no like '%").append(orderNo).append("%'\n");
		}
		
		if(!CommonUtils.isNullString(orderType)) {
			sql.append("  and req.order_type = ").append(orderType).append("\n");
		}
		
		if(!CommonUtils.isNullString(reqStatus)) {
			sql.append("  and req.req_status = ").append(reqStatus).append("\n");
		}
		
		if(!CommonUtils.isNullString(reqExeStatus)) {
			sql.append("  and req.req_exec_status = ").append(reqExeStatus).append("\n");
		}
		
		if(!CommonUtils.isNullString(oDlr)) {
			oDlr = "'" + oDlr.replaceAll(",", "','") + "'";
			sql.append("  and req.odcode in (").append(oDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(bDlr)) {
			bDlr = "'" + bDlr.replaceAll(",", "','") + "'";
			sql.append("  and req.bdcode in (").append(bDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(rDlr)) {
			rDlr = "'" + rDlr.replaceAll(",", "','") + "'";
			sql.append("  and req.rdcode in (").append(rDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(startTime)) {
			sql.append("  and trunc(req.req_date) >= to_date('").append(startTime).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(endTime)) {
			sql.append("  and trunc(req.req_date) <= to_date('").append(endTime).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(beginDate)) {
			sql.append("  and trunc(delivery.billing_date) >= to_date('").append(beginDate).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(overDate)) {
			sql.append("  and trunc(delivery.billing_date) <= to_date('").append(overDate).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(materialCode)) {
			materialCode = "'" + materialCode.replaceAll(",", "','") + "'";
			sql.append("  and req.material_code in (").append(materialCode).append(")\n");
		}
		
		if(!CommonUtils.isNullString(groupCode)) {
			groupCode = "'" + groupCode.replaceAll(",", "','") + "'";
			sql.append("and exists\n");
			sql.append("(select 1\n");  
			sql.append("  from vw_material_group vmg, tm_vhcl_material_group_r tvmgr\n");  
			sql.append(" where tvmgr.group_id = vmg.PACKAGE_ID\n");  
			sql.append("   and tvmgr.material_id = req.material_id\n");  
			sql.append("   and (vmg.SERIES_CODE in (").append(groupCode).append(") or vmg.MODEL_CODE in (").append(groupCode).append(") or\n");  
			sql.append("       vmg.PACKAGE_CODE in (").append(groupCode).append(") or  vmg.BRAND_CODE in (").append(groupCode).append(")))\n");

		}
		
		sql.append(limitOrg("req", "bdid", orgId)) ;
		
		sql.append(" group by req.material_code, req.material_name\n");

		List<Map<String, Object>> totalList = dao.pageQuery(sql.toString(), params, dao.getFunName()) ;
		
		return totalList ;
	}
	
	public static List<Map<String, Object>> reqDownloadRes(Map<String, String> map) {
		String areaId = map.get("areaId") ;
		String reqNo = map.get("reqNo") ;
		String orderNo = map.get("orderNo") ;
		String reqStatus = map.get("reqStatus") ;
		String orderType = map.get("orderType") ;
		String reqExeStatus = map.get("reqExeStatus") ;
		String oDlr = map.get("oDlr") ;
		String bDlr = map.get("bDlr") ;
		String rDlr = map.get("rDlr") ;
		String startTime = map.get("startTime") ;
		String endTime = map.get("endTime") ;
		String beginDate = map.get("beginDate") ;
		String overDate = map.get("overDate") ;
		String materialCode = map.get("materialCode") ;
		String groupCode = map.get("groupCode") ;
		String orgId = map.get("orgId") ;
		String isFleet = map.get("isFleet") ;
		
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvdr.req_id,\n");
		sql.append("       tvo.order_no,\n");  
		sql.append("       tcc1.code_desc order_type_desc,\n");  
		sql.append("       tvdr.dlvry_req_no,\n");  
		sql.append("       tcc2.code_desc req_status_desc,\n");  
		sql.append("       to_char(tvdr.req_date, 'yyyy-mm-dd') req_date,\n");  
		sql.append("       tvdr.req_total_amount,\n");  
		sql.append("       tvmg.group_code,\n");  
		sql.append("       tvm.material_code,\n");  
		sql.append("       nvl(tvdrd.req_amount, 0) req_amount,\n");  
		sql.append("       nvl(tvdrd.reserve_amount, 0) reserve_amount,\n");  
		sql.append("       bdlr.dealer_shortname || '-' || bdlr.dealer_code binfo,\n");  
		sql.append("       rdlr.dealer_shortname || '-' || rdlr.dealer_code rinfo,\n"); 
		sql.append("       decode(tvdr.call_leavel,\n");  
		sql.append("              ?,\n");  
		sql.append("              odlr2.dealer_shortname || '-' || odlr2.dealer_code,\n");  
		sql.append("              odlr1.dealer_shortname || '-' || odlr1.dealer_code) oinfo\n");  
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("  from tt_vs_dlvry_req          tvdr,\n");  
		sql.append("       tt_vs_dlvry_req_dtl      tvdrd,\n");  
		sql.append("       tt_vs_order              tvo,\n");  
		sql.append("       tc_code                  tcc1,\n");  
		sql.append("       tc_code                  tcc2,\n");  
		sql.append("       tm_vhcl_material         tvm,\n");  
		sql.append("       tm_vhcl_material_group_r tvmgr,\n");  
		sql.append("       tm_vhcl_material_group   tvmg,\n");  
		sql.append("       tm_dealer                bdlr,\n");  
		sql.append("       tm_dealer                rdlr,\n");  
		sql.append("       tm_dealer                odlr1,\n");  
		sql.append("       tm_dealer                odlr2\n");  
		sql.append(" where tvdr.req_id = tvdrd.req_id\n");  
		sql.append("   and tvdr.order_id = tvo.order_id\n");
		sql.append("   and tvo.order_type = tcc1.code_id\n");  
		sql.append("   and tvdr.req_status = tcc2.code_id\n");  
		sql.append("   and tvdrd.material_id = tvm.material_id\n");  
		sql.append("   and tvm.material_id = tvmgr.material_id\n");  
		sql.append("   and tvmgr.group_id = tvmg.group_id\n");  
		sql.append("   and tvo.billing_org_id = bdlr.dealer_id\n");  
		sql.append("   and tvdr.receiver = rdlr.dealer_id(+)\n");  
		sql.append("   and tvo.order_org_id = odlr1.dealer_id\n");  
		sql.append("   and tvdr.order_dealer_id = odlr2.dealer_id(+)\n");

		if(!CommonUtils.isNullString(isFleet)) {
			if(Constant.IF_TYPE_YES.toString().equals(isFleet))
				sql.append("  and tvdr.is_fleet = ").append(1).append("\n");
			else if(Constant.IF_TYPE_NO.toString().equals(isFleet))
				sql.append("  and tvdr.is_fleet = ").append(0).append("\n");
		}
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("  and tvo.area_Id in (").append(areaId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(reqNo)) {
			sql.append("  and tvdr.dlvry_req_no like '%").append(reqNo).append("%'\n");
		}
		
		if(!CommonUtils.isNullString(orderNo)) {
			sql.append("  and tvo.order_no like '%").append(orderNo).append("%'\n");
		}
		
		if(!CommonUtils.isNullString(orderType)) {
			sql.append("  and tvo.order_type = ").append(orderType).append("\n");
		}
		
		if(!CommonUtils.isNullString(reqStatus)) {
			sql.append("  and tvdr.req_status = ").append(reqStatus).append("\n");
		}
		
		if(!CommonUtils.isNullString(reqExeStatus)) {
			sql.append("  and tvdr.req_exec_status = ").append(reqExeStatus).append("\n");
		}
		
		if(!CommonUtils.isNullString(oDlr)) {
			oDlr = "'" + oDlr.replaceAll(",", "','") + "'";
			sql.append("  and ((odlr1.dealer_code in (").append(oDlr).append(") and tvdr.call_leavel is null) or (odlr2.dealer_code in (").append(oDlr).append(")))\n");
		}
		
		if(!CommonUtils.isNullString(bDlr)) {
			bDlr = "'" + bDlr.replaceAll(",", "','") + "'";
			sql.append("  and bdlr.dealer_code in (").append(bDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(rDlr)) {
			rDlr = "'" + rDlr.replaceAll(",", "','") + "'";
			sql.append("  and rdlr.dealer_code (").append(rDlr).append(")\n");
		}
		
		if(!CommonUtils.isNullString(startTime)) {
			sql.append("  and trunc(tvdr.req_date) >= to_date('").append(startTime).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(endTime)) {
			sql.append("  and trunc(tvdr.req_date) <= to_date('").append(endTime).append("','yyyy-mm-dd')\n");
		}
		
		if(!CommonUtils.isNullString(beginDate) || !CommonUtils.isNullString(overDate)) {
			sql.append("and exists(\n");
			sql.append("select 1\n");  
			sql.append("  from tt_vs_dlvry tvd\n");  
			sql.append(" where tvd.req_id = tvdr.req_id\n");  
			
			if(!CommonUtils.isNullString(beginDate)) {
				sql.append("   and trunc(tvd.billing_date) >= to_date('").append(beginDate).append("', 'yyyy-mm-dd')\n"); 
			}
			
			if(!CommonUtils.isNullString(overDate)) {
				sql.append("   and trunc(tvd.billing_date) <= to_date('").append(overDate).append("', 'yyyy-mm-dd')\n");  
			}
			
			sql.append("   )\n");
		}
		
		if(!CommonUtils.isNullString(materialCode)) {
			materialCode = "'" + materialCode.replaceAll(",", "','") + "'";
			sql.append("  and tvm.material_code in (").append(materialCode).append(")\n");
		}
		
		if(!CommonUtils.isNullString(groupCode)) {
			groupCode = "'" + groupCode.replaceAll(",", "','") + "'";
			sql.append("and exists\n");
			sql.append("(select 1\n");  
			sql.append("  from vw_material_group vmg, tm_vhcl_material_group_r tvmgr\n");  
			sql.append(" where tvmgr.group_id = vmg.PACKAGE_ID\n");  
			sql.append("   and tvmgr.material_id = tvm.material_id\n");  
			sql.append("   and (vmg.SERIES_CODE in (").append(groupCode).append(") or vmg.MODEL_CODE in (").append(groupCode).append(") or\n");  
			sql.append("       vmg.PACKAGE_CODE in (").append(groupCode).append(") or  vmg.BRAND_CODE in (").append(groupCode).append(")))\n");

		}
		
		sql.append(limitOrg("bdlr", "dealer_id", orgId)) ;
		
		sql.append(" order by tvdr.req_date desc\n");
		
		List<Map<String, Object>> resList = dao.pageQuery(sql.toString(), params, dao.getFunName()) ;
		
		return resList ;
	}
	
	/******************************************************          DownLoad            ********************************************************/
	
	public static List<Map<String, Object>> getVhcl(String dlvryId) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvd.delivery_id,\n");
		sql.append("       tmv.vin,\n");  
		sql.append("       tvm.material_name,\n");  
		sql.append("       tvm.material_code,\n");  
		sql.append("       tmv.batch_no,\n");  
		sql.append("       decode(tvdm.if_inspection, 1, '√', 0, '×') if_inspection\n");  
		sql.append("  from tt_vs_dlvry      tvd,\n");  
		sql.append("       tt_vs_dlvry_erp  tvde,\n");  
		sql.append("       tt_vs_dlvry_mch  tvdm,\n");  
		sql.append("       tm_vehicle       tmv,\n");  
		sql.append("       tm_vhcl_material tvm\n");  
		sql.append(" where tvd.delivery_id = tvde.delivery_id\n");  
		sql.append("   and tvde.sendcar_header_id = tvdm.erp_sendcar_id(+)\n");  
		sql.append("   and tvdm.vehicle_id = tmv.vehicle_id\n");  
		sql.append("   and tmv.material_id = tvm.material_id(+)\n");  
		sql.append("   and tvd.delivery_id = ?\n");
		params.add(dlvryId) ;
		
		List<Map<String, Object>> vhclList = dao.pageQuery(sql.toString(), params, dao.getFunName()) ;
		
		return vhclList ;
	}
	
	public static String limitOrg(String table, String colName, String orgId) {
		StringBuffer sqlStr = new StringBuffer("") ;
		
		if(!CommonUtils.isNullString(orgId)) {
			sqlStr.append("and exists\n");
			sqlStr.append("(select 1 from vw_org_dealer vod where vod.dealer_id = ").append(table).append(".").append(colName).append(" and vod.root_org_id = ").append(orgId).append(")\n");
		}
		
		return sqlStr.toString() ;
	}
	
	public static String getDlr(String dealerIds, String DlrLeavel) {
		StringBuffer newDlrIds = new StringBuffer("") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tmd.dealer_code from tm_dealer tmd where tmd.status = ").append(Constant.STATUS_ENABLE).append(" and tmd.dealer_id in (").append(dealerIds).append(") and tmd.dealer_level = ").append(DlrLeavel).append("\n");

		List<Map<String, Object>> dlrList = dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		
		if(!CommonUtils.isNullList(dlrList)) {
			int len = dlrList.size() ;
			
			for(int i=0; i<len; i++) {
				if(newDlrIds.length() <= 0) {
					newDlrIds.append(dlrList.get(i).get("DEALER_CODE").toString()) ;
				} else {
					newDlrIds.append(",").append(dlrList.get(i).get("DEALER_CODE").toString()) ;
				}
			}
		}
		
		return newDlrIds.toString() ;
	}
	
	public static List<Map<String, Object>> getDlrArea(AclUserBean logon, String level) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tba.area_id, tba.area_code, tba.area_name, tpba.pose_id\n");
		sql.append("  from tm_business_area tba, tm_pose_business_area tpba\n");  
		sql.append(" where tba.area_id = tpba.area_id\n");  
		sql.append("   and tba.status = ?\n");  
		params.add(Constant.STATUS_ENABLE) ;
		
		sql.append("   and tpba.pose_id = ?\n");  
		params.add(logon.getPoseId()) ;
		
		if(!CommonUtils.isNullString(level) && !"-1".equals(level)) {
			sql.append("   and exists (select 1\n");  
			sql.append("          from tm_dealer_business_area tdba, tm_dealer tmd\n");  
			sql.append("         where tmd.dealer_id = tdba.dealer_id\n");  
			sql.append("           and tdba.area_id = tba.area_id\n");  
			sql.append("           and tmd.dealer_level = ?\n");
			params.add(level) ;
			
			sql.append("           and tmd.dealer_id in (").append(logon.getDealerId()).append("))\n");
		}

		List<Map<String, Object>> areaList = dao.pageQuery(sql.toString(), params, dao.getFunName()) ;
		
		return areaList ;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
