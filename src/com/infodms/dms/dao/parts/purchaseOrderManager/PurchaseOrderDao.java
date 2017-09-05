package com.infodms.dms.dao.parts.purchaseOrderManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartOemPoPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PurchaseOrderDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PurchaseOrderDao.class);

    private static final PurchaseOrderDao dao = new PurchaseOrderDao();

    private PurchaseOrderDao() {

    }

    public static final PurchaseOrderDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public List getPartWareHouseList(AclUserBean logonUser) throws Exception {
        try {
            TtPartWarehouseDefinePO po = new TtPartWarehouseDefinePO();
            po.setOrgId(logonUser.getOrgId());
            List list = select(po);
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPurchaseOrderDetailList(
            TtPartOemPoPO po, String beginTime, String endTime,
            String checkBeginTime, String checkEndTime, String inBeginTime,
            String inEndTime, String balanceBeginTime, String balanceEndTime,
            String chkCode, String inCode, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" select t1.order_code, \n");
            sql.append("        t1.chk_code, \n");
            sql.append("        t3.in_code, \n");
            sql.append("        t1.buyer, \n");
            sql.append("        t1.part_type, \n");
            sql.append("        t1.part_oldcode, \n");
            sql.append("        t1.part_cname, \n");
            sql.append("        t1.PART_CODE, \n");
            sql.append("        t1.buy_qty, \n");
            sql.append("        t1.check_qty, \n");
            sql.append("        NVL(T3.IN_QTY,0) IN_QTY,\n");
            sql.append("        t1.buy_price plan_price,\n");
            sql.append("        TO_CHAR(NVL(t3.in_amount,0), 'FM999,999,999,999,990.00') IN_AMOUNT,\n");
            sql.append("        NVL(NVL(t4.BAL_QTY,t4.in_qty),0) balance_qty, \n");
            sql.append("        TO_CHAR(NVL(NVL(t4.buy_price1,t4.buy_price),0), 'fm999,999,990.00') buy_price, \n");
            sql.append("        TO_CHAR(NVL(NVL(T4.BAL_AMOUNT,t4.in_amount),0), 'FM999,999,999,999,990.00') BAL_AMOUNT, \n");
            sql.append("        nvl(t4.vender_name, t1.vender_name) vender_name, \n");
            sql.append("        t5.maker_name, \n");
            sql.append("        nvl(t3.wh_name, t1.wh_name) wh_name, \n");
            sql.append("        t1.create_date, \n");
            sql.append("        t3.in_date, \n");
            sql.append("        t4.balance_date,t1.unit \n");
            sql.append("   from tt_part_oem_po       t1, \n");
            sql.append("        tt_part_po_in        t3, \n");
            sql.append("        tt_part_po_balance   t4, \n");
            sql.append("        tt_part_maker_define t5 \n");
            sql.append("  where t1.po_id = t3.po_id(+) \n");
            sql.append("    and t3.in_id = t4.in_id(+) \n");
            sql.append("    and t1.maker_id = t5.maker_id \n");

            if (!"".equals(po.getOrderCode())) {
                sql.append(" AND t1.order_code LIKE '%")
                        .append(po.getOrderCode()).append("%'\n");
            }

            if (!"".equals(chkCode)) {
                sql.append(" AND t1.chk_code LIKE '%")
                        .append(chkCode).append("%'\n");
            }
            if (!"".equals(inCode)) {
                sql.append(" AND t3.in_code LIKE '%")
                        .append(inCode).append("%'\n");
            }

			/*if(!"".equals(beginTime)){
                sql.append(" AND to_date(t1.create_date)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')");
			}
			
			if(!"".equals(endTime)){
				sql.append(" AND to_date(t1.create_date)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')");
			}*/

            if (!"".equals(checkBeginTime)) {
                sql.append(" AND to_date(t1.create_date)>=").append("to_date('").append(checkBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(checkEndTime)) {
                sql.append(" AND to_date(t1.create_date)<=").append("to_date('").append(checkEndTime).append("','yyyy-MM-dd')");
            }

            if (po.getWhId() != null && po.getWhId() != 0) {
                sql.append(" AND t1.wh_id=").append(po.getWhId());
            }

            if (po.getPartType() != null && po.getPartType() != 0) {
                sql.append(" AND t1.part_type=").append(po.getPartType());
            }

            if (po.getVenderId() != null && po.getVenderId() != 0) {
                sql.append(" AND (t4.vender_id=").append(po.getVenderId()).append(" or t1.vender_id=").append(po.getVenderId()).append(")");
            }

            if (po.getPartOldcode() != null && !"".equals(po.getPartOldcode())) {
                sql.append(" AND upper(t1.part_oldcode) LIKE '%")
                        .append(po.getPartOldcode().toUpperCase()).append("%'\n");
            }
            if (po.getPartCode() != null && !"".equals(po.getPartCode())) {
                sql.append(" AND upper(t1.PART_CODE) LIKE '%")
                        .append(po.getPartCode().toUpperCase()).append("%'\n");
            }
            if (po.getPartCname() != null && !"".equals(po.getPartCname())) {
                sql.append(" AND t1.part_cname LIKE '%")
                        .append(po.getPartCname()).append("%'\n");
            }
			
			/*if(!"".equals(checkBeginTime)){
				sql.append(" AND to_date(t2.check_date)>=").append("to_date('").append(checkBeginTime).append("','yyyy-MM-dd')");
			}
			
			if(!"".equals(checkEndTime)){
				sql.append(" AND to_date(t2.check_date)<=").append("to_date('").append(checkEndTime).append("','yyyy-MM-dd')");
			}*/

            if (!"".equals(inBeginTime)) {
                sql.append(" AND to_date(t3.in_date)>=").append("to_date('").append(inBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(inEndTime)) {
                sql.append(" AND to_date(t3.in_date)<=").append("to_date('").append(inEndTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balanceBeginTime)) {
                sql.append(" AND to_date(t4.balance_date)>=").append("to_date('").append(balanceBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(balanceEndTime)) {
                sql.append(" AND to_date(t4.balance_date)<=").append("to_date('").append(balanceEndTime).append("','yyyy-MM-dd')");
            }

            //sql.append(" AND t1.STATE != ").append(Constant.PART_PURCHASE_ORDERCHK_STATUS_02);
            sql.append(" order by t1.order_code");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPurchaseOrder(RequestWrapper request) throws Exception {


        List<Map<String, Object>> list;
        try {

            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//采购订单号
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//验收单号
            String inCode = CommonUtils.checkNull(request.getParamValue("IN_CODE"));//入库单号
			/*String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//制单开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//制单结束时间
*/
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房id
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件种类
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String checkBeginTime = CommonUtils.checkNull(request.getParamValue("checkBeginTime"));//验货开始时间
            String checkEndTime = CommonUtils.checkNull(request.getParamValue("checkEndTime"));//验货结束时间
            String inBeginTime = CommonUtils.checkNull(request.getParamValue("inBeginTime"));//入库开始时间
            String inEndTime = CommonUtils.checkNull(request.getParamValue("inEndTime"));//入库结束时间
            String balanceBeginTime = CommonUtils.checkNull(request.getParamValue("balanceBeginTime"));//结算开始时间
            String balanceEndTime = CommonUtils.checkNull(request.getParamValue("balanceEndTime"));//结算结束时间

            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号    //add zhumingwei 2013-09-17

            StringBuffer sql = new StringBuffer("");
            sql.append(" select t1.order_code, \n");
            sql.append("        t1.chk_code, \n");
            sql.append("        t3.in_code, \n");
            sql.append("        t1.buyer, \n");
            sql.append("        decode(T1.PART_TYPE, \n")
                    .append(Constant.PART_BASE_PART_TYPES_SELF_MADE).append(",'自制件',")
                    .append(Constant.PART_BASE_PART_TYPES_PURCHASE).append(",'国产件',")
                    .append(Constant.PART_BASE_PART_TYPES_ENTRANCE).append(",'进口件')")
                    .append(" PART_TYPE,");
            sql.append("        t1.part_oldcode, \n");
            sql.append("        t1.part_cname, \n");
            sql.append("        t1.buy_qty, \n");
            sql.append("        t1.check_qty, \n");
            sql.append("        NVL(T3.IN_QTY,0) IN_QTY,\n");
            sql.append("        t1.buy_price plan_price,\n");
            sql.append("        TO_CHAR(NVL(t3.in_amount,0), 'FM999,999,999,999,990.00') IN_AMOUNT,\n");
            sql.append("        NVL(NVL(t4.BAL_QTY,t4.in_qty),0) balance_qty, \n");
            sql.append("        TO_CHAR(NVL(NVL(t4.buy_price1,t4.buy_price),0), 'fm999,999,990.00') buy_price, \n");
            sql.append("        TO_CHAR(NVL(NVL(T4.BAL_AMOUNT,t4.in_amount),0), 'FM999,999,999,999,990.00') BAL_AMOUNT, \n");
            sql.append("        nvl(t4.vender_name, t1.vender_name) vender_name, \n");
            sql.append("        t5.maker_name, \n");
            sql.append("        nvl(t3.wh_name, t1.wh_name) wh_name, \n");
            sql.append("        t1.create_date, \n");
            sql.append("        t3.in_date, \n");
            sql.append("        t4.balance_date,t1.part_code ,t1.unit\n");
            sql.append("   from tt_part_oem_po       t1, \n");
            sql.append("        tt_part_po_in        t3, \n");
            sql.append("        tt_part_po_balance   t4, \n");
            sql.append("        tt_part_maker_define t5 \n");
            sql.append("  where t1.po_id = t3.po_id(+) \n");
            sql.append("    and t3.in_id = t4.in_id(+) \n");
            sql.append("    and t1.maker_id = t5.maker_id \n");

            if (!"".equals(orderCode)) {
                sql.append(" AND T1.ORDER_CODE LIKE '%")
                        .append(orderCode).append("%'\n");
            }

            if (!"".equals(chkCode)) {
                sql.append(" AND t1.chk_code LIKE '%")
                        .append(chkCode).append("%'\n");
            }
            if (!"".equals(inCode)) {
                sql.append(" AND t3.in_code LIKE '%")
                        .append(inCode).append("%'\n");
            }
			
			/*if(!"".equals(beginTime)){
				sql.append(" AND to_date(t1.create_date)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')");
			}
			
			if(!"".equals(endTime)){
				sql.append(" AND to_date(t1.create_date)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')");
			}*/

            if (!"".equals(checkBeginTime)) {
                sql.append(" AND to_date(t1.create_date)>=").append("to_date('").append(checkBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(checkEndTime)) {
                sql.append(" AND to_date(t1.create_date)<=").append("to_date('").append(checkEndTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(whId)) {
                sql.append(" AND t1.wh_id=").append(CommonUtils.parseLong(whId));
            }

            if (!"".equals(partType)) {
                sql.append(" AND t1.part_type=").append(CommonUtils.parseInteger(partType));
            }

            if (!"".equals(venderId)) {
                sql.append(" AND (t4.vender_id=").append(CommonUtils.parseLong(venderId)).append(" or t1.vender_id=").append(CommonUtils.parseLong(venderId)).append(")");
            }

            if (partOldCode != null && !"".equals(partOldCode)) {
                sql.append(" AND upper(t1.part_oldcode) LIKE '%")
                        .append(partOldCode.toUpperCase()).append("%'\n");
            }
            if (partName != null && !"".equals(partName)) {
                sql.append(" AND t1.part_cname LIKE '%")
                        .append(partName).append("%'\n");
            }
			
			/*if(!"".equals(checkBeginTime)){
				sql.append(" AND to_date(t2.check_date)>=").append("to_date('").append(checkBeginTime).append("','yyyy-MM-dd')");
			}
			
			if(!"".equals(checkEndTime)){
				sql.append(" AND to_date(t2.check_date)<=").append("to_date('").append(checkEndTime).append("','yyyy-MM-dd')");
			}*/

            if (!"".equals(inBeginTime)) {
                sql.append(" AND to_date(t3.in_date)>=").append("to_date('").append(inBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(inEndTime)) {
                sql.append(" AND to_date(t3.in_date)<=").append("to_date('").append(inEndTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balanceBeginTime)) {
                sql.append(" AND to_date(t4.balance_date)>=").append("to_date('").append(balanceBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(balanceEndTime)) {
                sql.append(" AND to_date(t4.balance_date)<=").append("to_date('").append(balanceEndTime).append("','yyyy-MM-dd')");
            }

            //add zhumingwei 2013-09-17
            if (!"".equals(partCode)) {
                sql.append(" AND upper(t1.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'\n");
            }
            //add zhumingwei 2013-09-17

            //sql.append(" AND t1.STATE != ").append(Constant.PART_PURCHASE_ORDERCHK_STATUS_02);
            sql.append(" order by t1.order_code");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;


    }

    public PageResult<Map<String, Object>> queryPurchaseOrderInDiffList(
            TtPartOemPoPO po, String beginTime, String endTime,
            String checkBeginTime, String checkEndTime, String inBeginTime,
            String inEndTime, String balanceBeginTime, String balanceEndTime,
            String chkCode, String inCode, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {

            StringBuffer sql = new StringBuffer("");
            sql.append(" select t1.order_code, \n");
            sql.append("        t1.chk_code, \n");
            sql.append("        t3.in_code, \n");
            sql.append("        t1.buyer, \n");
            sql.append("        t1.part_type, \n");
            sql.append("        t1.part_oldcode, \n");
            sql.append("        t1.part_cname, \n");
            sql.append("        t1.buy_qty, \n");
            sql.append("        t1.check_qty, \n");
            sql.append("        t1.in_qty, \n");
            sql.append("        (t1.check_qty-t1.in_qty) INDIFF_QTY, \n");
            sql.append("        decode(nvl(t4.buy_price,0),0,0,t1.in_qty) balance_qty, \n");
            sql.append("        TO_CHAR(t4.buy_price, 'fm999,999,990.00') buy_price, \n");
            sql.append("        TO_CHAR(t4.in_amount, 'FM999,999,999,999,990.00') in_amount, \n");
            sql.append("        nvl(t4.vender_name, t1.vender_name) vender_name, \n");
            sql.append("        t5.maker_name, \n");
            sql.append("        nvl(t3.wh_name, t1.wh_name) wh_name, \n");
            sql.append("        t1.create_date, \n");
            sql.append("        t3.in_date, \n");
            sql.append("        t4.balance_date \n");
            sql.append("   from tt_part_oem_po       t1, \n");
            sql.append("        tt_part_po_in        t3, \n");
            sql.append("        tt_part_po_balance   t4, \n");
            sql.append("        tt_part_maker_define t5 \n");
            sql.append("  where t1.po_id = t3.po_id(+) \n");
            sql.append("    and t3.in_id = t4.in_id(+) \n");
            sql.append("    and t1.maker_id = t5.maker_id \n");

            if (!"".equals(po.getOrderCode())) {
                sql.append(" AND t1.order_code LIKE '%")
                        .append(po.getOrderCode()).append("%'\n");
            }

            if (!"".equals(chkCode)) {
                sql.append(" AND t1.chk_code LIKE '%")
                        .append(chkCode).append("%'\n");
            }
            if (!"".equals(inCode)) {
                sql.append(" AND t3.in_code LIKE '%")
                        .append(inCode).append("%'\n");
            }
			
			/*if(!"".equals(beginTime)){
				sql.append(" AND to_date(t1.create_date)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')");
			}
			
			if(!"".equals(endTime)){
				sql.append(" AND to_date(t1.create_date)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')");
			}*/

            if (!"".equals(checkBeginTime)) {
                sql.append(" AND to_date(t1.create_date)>=").append("to_date('").append(checkBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(checkEndTime)) {
                sql.append(" AND to_date(t1.create_date)<=").append("to_date('").append(checkEndTime).append("','yyyy-MM-dd')");
            }

            if (po.getWhId() != null && po.getWhId() != 0) {
                sql.append(" AND t1.wh_id=").append(po.getWhId());
            }

            if (po.getPartType() != null && po.getPartType() != 0) {
                sql.append(" AND t1.part_type=").append(po.getPartType());
            }

            if (po.getVenderId() != null && po.getVenderId() != 0) {
                sql.append(" AND (t4.vender_id=").append(po.getVenderId()).append(" or t1.vender_id=").append(po.getVenderId()).append(")");
            }

            if (po.getPartOldcode() != null && !"".equals(po.getPartOldcode())) {
                sql.append(" AND t1.part_oldcode LIKE '%")
                        .append(po.getPartOldcode()).append("%'\n");
            }
            if (po.getPartCname() != null && !"".equals(po.getPartCname())) {
                sql.append(" AND t1.part_cname LIKE '%")
                        .append(po.getPartCname()).append("%'\n");
            }
			
			/*if(!"".equals(checkBeginTime)){
				sql.append(" AND to_date(t2.check_date)>=").append("to_date('").append(checkBeginTime).append("','yyyy-MM-dd')");
			}
			
			if(!"".equals(checkEndTime)){
				sql.append(" AND to_date(t2.check_date)<=").append("to_date('").append(checkEndTime).append("','yyyy-MM-dd')");
			}*/

            if (!"".equals(inBeginTime)) {
                sql.append(" AND to_date(t3.in_date)>=").append("to_date('").append(inBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(inEndTime)) {
                sql.append(" AND to_date(t3.in_date)<=").append("to_date('").append(inEndTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balanceBeginTime)) {
                sql.append(" AND to_date(t4.balance_date)>=").append("to_date('").append(balanceBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(balanceEndTime)) {
                sql.append(" AND to_date(t4.balance_date)<=").append("to_date('").append(balanceEndTime).append("','yyyy-MM-dd')");
            }

            sql.append(" order by t1.order_code");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPurchaseOrderInDiff(
            RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {

            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//采购订单号
            //String buyer = CommonUtils.checkNull(request.getParamValue("BUYER"));//采购员
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//验收单号
            String inCode = CommonUtils.checkNull(request.getParamValue("IN_CODE"));//入库单号
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//制单开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//制单结束时间
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房id
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件种类
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String checkBeginTime = CommonUtils.checkNull(request.getParamValue("checkBeginTime"));//验货开始时间
            String checkEndTime = CommonUtils.checkNull(request.getParamValue("checkEndTime"));//验货结束时间
            String inBeginTime = CommonUtils.checkNull(request.getParamValue("inBeginTime"));//入库开始时间
            String inEndTime = CommonUtils.checkNull(request.getParamValue("inEndTime"));//入库结束时间
            String balanceBeginTime = CommonUtils.checkNull(request.getParamValue("balanceBeginTime"));//结算开始时间
            String balanceEndTime = CommonUtils.checkNull(request.getParamValue("balanceEndTime"));//结算结束时间

            StringBuffer sql = new StringBuffer("");
            sql.append(" select t1.order_code, \n");
            sql.append("        t1.chk_code, \n");
            sql.append("        t3.in_code, \n");
            sql.append("        t1.buyer, \n");
            sql.append("        decode(T1.PART_TYPE, \n")
                    .append(Constant.PART_BASE_PART_TYPES_SELF_MADE).append(",'自制件',")
                    .append(Constant.PART_BASE_PART_TYPES_PURCHASE).append(",'国产件',")
                    .append(Constant.PART_BASE_PART_TYPES_ENTRANCE).append(",'进口件')")
                    .append(" PART_TYPE,");
            sql.append("        t1.part_oldcode, \n");
            sql.append("        t1.part_cname, \n");
            sql.append("        t1.buy_qty, \n");
            sql.append("        t1.check_qty, \n");
            sql.append("        t1.in_qty, \n");
            sql.append("        (t1.check_qty-t1.in_qty) INDIFF_QTY, \n");
            sql.append("        decode(nvl(t4.buy_price,0),0,0,t1.in_qty) balance_qty, \n");
            sql.append("        TO_CHAR(t4.buy_price, 'fm999,999,990.00') buy_price, \n");
            sql.append("        TO_CHAR(t4.in_amount, 'FM999,999,999,999,990.00') in_amount, \n");
            sql.append("        nvl(t4.vender_name, t1.vender_name) vender_name, \n");
            sql.append("        t5.maker_name, \n");
            sql.append("        nvl(t3.wh_name, t1.wh_name) wh_name, \n");
            sql.append("        t1.create_date, \n");
            sql.append("        t3.in_date, \n");
            sql.append("        t4.balance_date \n");
            sql.append("   from tt_part_oem_po       t1, \n");
            sql.append("        tt_part_po_in        t3, \n");
            sql.append("        tt_part_po_balance   t4, \n");
            sql.append("        tt_part_maker_define t5 \n");
            sql.append("  where t1.po_id = t3.po_id(+) \n");
            sql.append("    and t3.in_id = t4.in_id(+) \n");
            sql.append("    and t1.maker_id = t5.maker_id \n");

            if (!"".equals(orderCode)) {
                sql.append(" AND T1.ORDER_CODE LIKE '%")
                        .append(orderCode).append("%'\n");
            }

            if (!"".equals(chkCode)) {
                sql.append(" AND t1.chk_code LIKE '%")
                        .append(chkCode).append("%'\n");
            }
            if (!"".equals(inCode)) {
                sql.append(" AND t3.in_code LIKE '%")
                        .append(inCode).append("%'\n");
            }
			
			/*if(!"".equals(beginTime)){
				sql.append(" AND to_date(t1.create_date)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')");
			}
			
			if(!"".equals(endTime)){
				sql.append(" AND to_date(t1.create_date)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')");
			}*/

            if (!"".equals(checkBeginTime)) {
                sql.append(" AND to_date(t1.create_date)>=").append("to_date('").append(checkBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(checkEndTime)) {
                sql.append(" AND to_date(t1.create_date)<=").append("to_date('").append(checkEndTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(whId)) {
                sql.append(" AND t1.wh_id=").append(CommonUtils.parseLong(whId));
            }

            if (!"".equals(partType)) {
                sql.append(" AND t1.part_type=").append(CommonUtils.parseInteger(partType));
            }

            if (!"".equals(venderId)) {
                sql.append(" AND (t4.vender_id=").append(CommonUtils.parseLong(venderId)).append(" or t1.vender_id=").append(CommonUtils.parseLong(venderId)).append(")");
            }

            if (partOldCode != null && !"".equals(partOldCode)) {
                sql.append(" AND t1.part_oldcode LIKE '%")
                        .append(partOldCode).append("%'\n");
            }
            if (partName != null && !"".equals(partName)) {
                sql.append(" AND t1.part_cname LIKE '%")
                        .append(partName).append("%'\n");
            }
			
			/*if(!"".equals(checkBeginTime)){
				sql.append(" AND to_date(t2.check_date)>=").append("to_date('").append(checkBeginTime).append("','yyyy-MM-dd')");
			}
			
			if(!"".equals(checkEndTime)){
				sql.append(" AND to_date(t2.check_date)<=").append("to_date('").append(checkEndTime).append("','yyyy-MM-dd')");
			}*/

            if (!"".equals(inBeginTime)) {
                sql.append(" AND to_date(t3.in_date)>=").append("to_date('").append(inBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(inEndTime)) {
                sql.append(" AND to_date(t3.in_date)<=").append("to_date('").append(inEndTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balanceBeginTime)) {
                sql.append(" AND to_date(t4.balance_date)>=").append("to_date('").append(balanceBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(balanceEndTime)) {
                sql.append(" AND to_date(t4.balance_date)<=").append("to_date('").append(balanceEndTime).append("','yyyy-MM-dd')");
            }

            sql.append(" order by t1.order_code");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;
    }

}
