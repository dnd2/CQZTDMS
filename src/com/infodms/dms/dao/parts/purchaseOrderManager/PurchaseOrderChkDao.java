package com.infodms.dms.dao.parts.purchaseOrderManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartOemPoPO;
import com.infodms.dms.po.TtPartPoMainPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PurchaseOrderChkDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PurchaseOrderChkDao.class);
    private static final PurchaseOrderChkDao dao = new PurchaseOrderChkDao();

    private PurchaseOrderChkDao() {

    }

    public static final PurchaseOrderChkDao getInstance() {
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

    public PageResult<Map<String, Object>> queryPartOrderChkList(
            TtPartOemPoPO po, String originType, String chkId, String state, String isPrint, String beginTime, String endTime, Integer curPage, Integer pageSize) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT A.PO_ID, \n");
            sql.append("        A.ORDER_CODE, \n");
            sql.append("        A.CHK_CODE, \n");
            sql.append("        A.ORIGIN_TYPE, \n");
            sql.append("        A.CREATE_DATE, \n");
            sql.append("        A.BUYER, \n");
            sql.append("        A.PLINE_NO, \n");
            sql.append("        A.PART_TYPE, \n");
            sql.append("        A.PART_OLDCODE, \n");
            sql.append("        A.PART_CNAME, \n");
//            sql.append("        B.MAKER_ID, \n");
//            sql.append("        B.MAKER_NAME, \n");
            sql.append("        A.PART_CODE, \n");
            sql.append("        A.BUY_QTY, \n");
            sql.append("        A.CHECK_QTY, \n");
            sql.append("        A.SPAREIN_QTY, \n");
            sql.append("        A.IN_QTY, \n");
            sql.append("        A.GENERATE_QTY, \n");
            sql.append("        A.VENDER_ID, \n");
            sql.append("        A.VENDER_NAME, \n");
            sql.append("        A.Wh_Id, \n");
            sql.append("        A.WH_NAME, \n");
            sql.append("        A.FORECAST_DATE, \n");
            sql.append("        A.PRINT_DATE2, \n");
            sql.append("        TRUNC(SYSDATE) CHECK_DATE, \n");
            sql.append("        A.SPARE_QTY, \n");
            sql.append("        A.REMARK, \n");
            sql.append("        A.REMARK1, \n");
            sql.append("        A.STATE \n");
            sql.append("   FROM TT_PART_OEM_PO A \n");//, TT_PART_MAKER_DEFINE B \n");
            sql.append("  WHERE 1=1 \n");//A.MAKER_ID = B.MAKER_ID(+) \n");
//            sql.append("    AND B.STATE = 10011001 \n");
            sql.append("    AND A.STATUS = 1 \n");
            if (!"".equals(isPrint)) {
                if (isPrint.equals(Constant.IF_TYPE_YES.toString())) {
                    sql.append("    AND EXISTS (SELECT 1 FROM TT_PART_OEM_PO P \n");
                    sql.append("          WHERE P.PO_ID = A.PO_ID \n");
                    sql.append("            AND (P.STATE = 92271003 OR P.STATE = 92271002) \n");
                    sql.append("            AND P.IN_QTY > 0 AND P.PRINT_DATE2 IS NOT NULL) \n");
                } else {
                    sql.append("    AND EXISTS (SELECT 1 FROM TT_PART_OEM_PO P \n");
                    sql.append("          WHERE P.PO_ID = A.PO_ID \n");
                    sql.append("            AND (P.STATE = 92271003 OR P.STATE = 92271002) \n");
                    sql.append("            AND P.IN_QTY > 0 AND P.PRINT_DATE2 IS NULL) \n");
                }
            }

//            if (!"".equals(chkId)) {//暂时屏蔽测试hyy
//                sql.append("    AND A.CHK_ID = ").append(chkId);
//            }

           
            if (!"".equals(originType)) {
                sql.append(" AND A.ORIGIN_TYPE =" + originType);
            }
            if (!"".equals(po.getOrderCode())) {
                sql.append(" AND A.ORDER_CODE LIKE '%")
                        .append(po.getOrderCode()).append("%'\n");
            }
            if (!"".equals(po.getChkCode()) && po.getChkCode() != null) {
                sql.append(" AND A.CHK_CODE LIKE '%")
                        .append(po.getChkCode()).append("%'\n");
            }
            if (po.getBuyer() != null && !"".equals(po.getBuyer())) {
                sql.append(" AND A.BUYER LIKE '%")
                        .append(po.getBuyer()).append("%'\n");
            }

            if (!"".equals(beginTime)) {
                sql.append(" AND TO_DATE(A.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND TO_DATE(A.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
            }

            if (po.getWhId() != null && po.getWhId() != 0) {
                sql.append(" AND A.Wh_Id=").append(po.getWhId());
            }

            if (po.getPartType() != null && po.getPartType() != 0) {
                sql.append(" AND A.PART_TYPE=").append(po.getPartType());
            }

            if (po.getVenderId() != null && po.getVenderId() != 0) {
                sql.append(" AND A.VENDER_ID=").append(po.getVenderId());
            }

            if (!po.getPartOldcode().equals("")) {
                sql.append(" AND UPPER(A.PART_OLDCODE) LIKE '%")
                        .append(po.getPartOldcode().toUpperCase()).append("%'\n");
            }
            if (!po.getPartCname().equals("")) {
                sql.append(" AND A.PART_CNAME LIKE '%")
                        .append(po.getPartCname()).append("%'\n");
            }
            if (!po.getPartCode().equals("")) {
                sql.append(" AND UPPER(A.PART_CODE) LIKE '%")
                        .append(po.getPartCode().toUpperCase()).append("%'\n");
            }
            if (!"".equals(state)) {
                if ("10041001".equals(state)) { //完全入库
                    sql.append(" and (a.generate_qty =a.in_qty AND a.sparein_qty=0 or a.state=" + Constant.PART_PURCHASE_ORDERCHK_STATUS_03 + ")");
                } else if ("10041002".equals(state)) {//未入库
                    sql.append("  and a.IN_QTY = 0");
                } else { //部分入库
                    sql.append("  and a.generate_qty!=a.in_qty AND a.in_qty >0 and a.state!=" + Constant.PART_PURCHASE_ORDERCHK_STATUS_03);
                }
            }
            sql.append(" ORDER BY  A.PART_OLDCODE ASC ");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public Map getVerByPoId(Long poId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.VER,T.PO_ID FROM TT_PART_OEM_PO T WHERE T.PO_ID=").append(poId);
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public void update(TtPartOemPoPO po) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE TT_PART_OEM_PO T SET T.CHECK_QTY=?,T.SPARE_QTY=?,T.SPAREIN_QTY=?,T.REMARK=?,T.STATE=?,T.MAKER_ID=? WHERE T.PO_ID=?");
            List params = new ArrayList();
            params.add(po.getCheckQty());
            params.add(po.getSpareQty());
            params.add(po.getSpareinQty());
            if (po.getRemark() != null) {
                params.add(po.getRemark());
            } else {
                params.add("");
            }
            params.add(po.getState());
            params.add(po.getMakerId());
            params.add(po.getPoId());
            update(sql.toString(), params);
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateVer(TtPartOemPoPO po) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE TT_PART_OEM_PO T SET T.VER=? WHERE T.PO_ID=?");
            List params = new ArrayList();
            params.add(po.getVer());
            params.add(po.getPoId());
            update(sql.toString(), params);
        } catch (Exception e) {
            throw e;
        }
    }

    public List queryMakerInfo(Long partId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T1.MAKER_ID, T1.MAKER_NAME \n");
            sql.append("   FROM TT_PART_MAKER_RELATION T, TT_PART_MAKER_DEFINE T1 \n");
            sql.append("  WHERE T.MAKER_ID = T1.MAKER_ID \n");
            sql.append("    AND T.PART_ID = ").append(partId);
            return pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public List queryMakerInfo2(Long partId, Long venderId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T1.MAKER_ID, T1.MAKER_NAME \n");
            sql.append("   FROM tt_part_vender_maker_relation T, TT_PART_MAKER_DEFINE T1 \n");
            sql.append("  WHERE T.MAKER_ID = T1.MAKER_ID \n");
            sql.append("    AND t.vender_id= ").append(venderId);
            sql.append("    AND T.PART_ID = ").append(partId);
            return pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> queryPurchaseOrderChk(
            RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {

            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//采购订单号
            String checkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//验收单号
            String buyer = CommonUtils.checkNull(request.getParamValue("BUYER"));//采购员
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//结束时间
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房id
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件种类
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//件号
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//状态
            String chkId = CommonUtils.checkNull(request.getParamValue("chkId"));//状态

            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT  A.PO_ID,A.CHK_CODE,A.ORDER_CODE,A.CREATE_DATE,A.BUYER,A.PLINE_NO,DECODE(A.PART_TYPE,")
                    .append(Constant.PART_BASE_PART_TYPES_SELF_MADE).append(",'自制件',")
                    .append(Constant.PART_BASE_PART_TYPES_PURCHASE).append(",'国产件',")
                    .append(Constant.PART_BASE_PART_TYPES_ENTRANCE).append(",'进口件')")
                    .append(" PART_TYPE,");
            sql.append("A.PART_OLDCODE,A.PART_CNAME,B.MAKER_ID,B.MAKER_NAME,");
            sql.append("A.PART_CODE,A.BUY_QTY,A.CHECK_QTY,A.IN_QTY,A.VENDER_ID,A.VENDER_NAME,A.Wh_Id,A.WH_NAME,A.FORECAST_DATE,TRUNC(SYSDATE) CHECK_DATE,(A.Buy_Qty-A.CHECK_QTY) REL_QTY,A.REMARK,DECODE(A.STATE,")
                    .append(Constant.PART_PURCHASE_ORDERCHK_STATUS_01).append(",'待验收',")
                    .append(Constant.PART_PURCHASE_ORDERCHK_STATUS_02).append(",'已关闭',")
                    .append(Constant.PART_PURCHASE_ORDERCHK_STATUS_03).append(",'验收完成')")
                    .append(" STATE");
            sql.append(" FROM TT_PART_OEM_PO A,TT_PART_MAKER_DEFINE B WHERE A.MAKER_ID=B.MAKER_ID(+) ");
            sql.append(" AND B.STATE(+)=").append(Constant.STATUS_ENABLE)
                    .append(" AND A.STATUS=1 AND B.STATUS(+)=1");
            if (!"".equals(chkId)) {
                sql.append(" AND A.chk_id =" + chkId);
            }
            if (!"".equals(orderCode)) {
                sql.append(" AND A.ORDER_CODE LIKE '%")
                        .append(orderCode).append("%'\n");
            }
            if (!"".equals(checkCode)) {
                sql.append(" AND A.CHK_CODE LIKE '%")
                        .append(checkCode).append("%'\n");
            }
            if (!"".equals(buyer)) {
                sql.append(" AND A.BUYER LIKE '%")
                        .append(buyer).append("%'\n");
            }

            if (!"".equals(beginTime)) {
                sql.append(" AND to_date(A.CREATE_DATE)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND to_date(A.CREATE_DATE)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(whId)) {
                sql.append(" AND A.Wh_Id=").append(whId);
            }

            if (!"".equals(partType) && !"0".equals(partType)) {
                sql.append(" AND A.PART_TYPE=").append(partType);
            }

            if (!"".equals(venderId) && !"0".equals(venderId)) {
                sql.append(" AND A.VENDER_ID=").append(venderId);
            }

            if (!partOldCode.equals("")) {
                sql.append(" AND A.PART_OLDCODE LIKE '%")
                        .append(partOldCode).append("%'\n");
            }
            if (!partName.equals("")) {
                sql.append(" AND A.PART_CNAME LIKE '%")
                        .append(partName).append("%'\n");
            }
            if (!partCode.equals("")) {
                sql.append(" AND A.PART_CODE LIKE '%")
                        .append(partCode).append("%'\n");
            }
            if (!state.equals("")) {
                sql.append(" AND A.STATE=")
                        .append(state);
            }

            sql.append(" ORDER BY A.STATE ASC");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;
    }

    public PageResult<Map<String, Object>> queryPurchaseOrderList(
            TtPartPoMainPO po, String beginTime, String endTime,
            Integer curPage, Integer pageSize, String planer_name,
            String partOldCode, String partCname, String partCode) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
           /* sql.append("SELECT A.ORDER_ID,A.ORDER_CODE,A.BUYER,A.CREATE_DATE,A.WH_NAME,A.PLAN_TYPE,TO_CHAR(A.SUM_QTY,'fm999,999,999') SUM_QTY,");
            sql.append("to_char(A.AMOUNT,'fm999,999,999,999,990.00') AMOUNT,A.STATE,A.REMARK1,TO_CHAR(A.CLOSE_DATE,'yyyy-MM-dd') CLOSE_DATE FROM TT_PART_PO_MAIN A");*/

            sql.append("SELECT A.ORDER_ID,\n");
            sql.append("       A.ORDER_CODE,\n");
            sql.append("       A.BUYER,\n");
            sql.append("       A.CREATE_DATE,\n");
            sql.append("       A.WH_NAME,\n");
            sql.append("       A.PLAN_TYPE,\n");
            sql.append("       TO_CHAR(A.SUM_QTY, 'fm999,999,999') SUM_QTY,\n");
            sql.append("       TO_CHAR(A.AMOUNT, 'fm999,999,999,999,990.00') AMOUNT,\n");
            sql.append("       A.STATE,\n");
            sql.append("       A.REMARK,\n");
            sql.append("       A.REMARK1,\n");
            sql.append("       A.IS_URGENT_IN,\n");
            sql.append("       TO_CHAR(A.CLOSE_DATE, 'yyyy-MM-dd') CLOSE_DATE,\n");
            sql.append("       (SELECT DISTINCT V.VENDER_NAME\n");
            sql.append("          FROM TT_PART_PO_DTL D, TT_PART_VENDER_DEFINE V\n");
            sql.append("         WHERE D.VENDER_ID = V.VENDER_ID\n");
            sql.append("           AND D.STATUS = 1\n");
            sql.append("           AND V.STATUS = 1\n");
            sql.append("           AND D.ORDER_ID = A.ORDER_ID) VENDER_NAME\n");
            sql.append("  FROM TT_PART_PO_MAIN A, TT_PART_PO_DTL B\n");

            sql.append(" WHERE A.ORDER_ID = B.ORDER_ID AND A.PLAN_TYPE =").append(Constant.PART_PURCHASE_PLAN_TYPE_04);

            if (!"".equals(po.getOrderCode())) {
                sql.append(" AND A.ORDER_CODE LIKE '%")
                        .append(po.getOrderCode()).append("%'\n");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND upper(B.PART_OLDCODE) LIKE upper('%")
                        .append(partOldCode).append("%')\n");
            }
            if (!"".equals(partCname)) {
                sql.append(" AND B.PART_CNAME LIKE '%")
                        .append(partCname).append("%'\n");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND B.PART_CODE LIKE '%")
                        .append(partCode).append("%'\n");
            }
            if (po.getBuyer() != null && !"".equals(po.getBuyer())) {
                sql.append(" AND A.BUYER LIKE '%")
                        .append(po.getBuyer()).append("%'\n");
            }

            if (!"".equals(beginTime)) {
                sql.append(" AND to_date(A.CREATE_DATE)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND to_date(A.CREATE_DATE)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')");
            }

//            if (po.getWhId() != null && po.getWhId() != 0) {
//                sql.append(" AND A.Wh_Id=").append(po.getWhId());
//            }

//            if (po.getPlanType() != null && po.getPlanType() != 0) {
//                sql.append(" AND A.PLAN_TYPE=").append(po.getPlanType());
//            }
            if (po.getState() != null && po.getState() != 0) {
                if (po.getState().equals(Constant.PURCHASE_ORDER_STATE_04)) {//Constant.PURCHASE_ORDER_STATE_04 采购验收指令- 已超期
                    sql.append("AND ((TRUNC(SYSDATE) - TRUNC(A.CREATE_DATE)) >\n");
                    sql.append("      (SELECT D.DAYS\n");
                    sql.append("          FROM TT_PART_PERIOD_DEFINE D\n");
                    sql.append("         WHERE D.STATE = 10011001\n");
                    sql.append("           AND D.STATUS = 1) OR A.STATE = " + Constant.PURCHASE_ORDER_STATE_04 + ")\n");
                } else {
                    sql.append(" AND A.state =").append(po.getState());
                }
            }

            sql.append(" AND B.SPARE_QTY > 0\n");

            sql.append(" GROUP BY A.ORDER_ID, \n");
            sql.append("           A.ORDER_CODE, \n");
            sql.append("           A.BUYER, \n");
            sql.append("           A.CREATE_DATE, \n");
            sql.append("           A.WH_NAME, \n");
            sql.append("           A.PLAN_TYPE, \n");
            sql.append("           A.SUM_QTY, \n");
            sql.append("           A.AMOUNT, \n");
            sql.append("           A.STATE, \n");
            sql.append("           A.REMARK, \n");
            sql.append("           A.REMARK1, \n");
            sql.append("           A.IS_URGENT_IN, \n");
            sql.append("           A.CLOSE_DATE \n");
            sql.append(" ORDER BY A.CREATE_DATE DESC, VENDER_NAME");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryOrderInfo(String chkId) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");
            //modify by yuan 20130708
            sql.append("SELECT T.PO_ID,\n");
            sql.append("       T.ORDER_CODE,\n");
            sql.append("       T.PLAN_CODE,\n");
            sql.append("       T.CHK_CODE,\n");
            sql.append("       T.WH_NAME,\n");
            sql.append("       T.VENDER_NAME,\n");
            sql.append("       0.17 ORATE,\n");
            sql.append("       (SELECT u.name FROM tc_user u WHERE u.user_id=t.create_by) NAME,\n");
//            sql.append("       M.MAKER_NAME,\n");
            sql.append("       TO_CHAR(T.CREATE_DATE, 'yyyy/MM/dd HH:mm:ss') CREATE_DATE,\n");
            sql.append("       TO_CHAR(SYSDATE, 'yyyy/MM/dd HH:mm:ss') CURDATE,\n");
            sql.append("       T.PART_CODE,\n");
            sql.append("       T.PART_OLDCODE,\n");
            sql.append("       T.PART_CNAME,\n"); 
            sql.append("DECODE(T.IN_QTY,0,t.generate_qty,t.in_qty) GENERATE_QTY,\n");
            sql.append("       T.UNIT,\n");
            sql.append("       (SELECT TO_CHAR(P.SALE_PRICE3, 'FM999,999,999,999,990.00')\n");
            sql.append("          FROM TT_PART_SALES_PRICE P\n");
            sql.append("         WHERE T.PART_ID = P.PART_ID) PLAN_PRICE,\n"); 
            sql.append("nvl((SELECT DECODE(T.IN_QTY,0,t.generate_qty,t.in_qty) * P.SALE_PRICE3\n");
            sql.append("          FROM TT_PART_SALES_PRICE P\n");
            sql.append("         WHERE T.PART_ID = P.PART_ID),0) AMOUNT,\n"); 
            sql.append("(SELECT TO_CHAR(DECODE(T.IN_QTY,0,t.generate_qty,t.in_qty) * P.SALE_PRICE3, 'FM999,999,999,999,990.00')\n");
            sql.append("          FROM TT_PART_SALES_PRICE P\n");
            sql.append("         WHERE T.PART_ID = P.PART_ID) FAMOUNT,\n");
            sql.append("       (SELECT L.LOC_NAME\n");
            sql.append("          FROM TT_PART_LOACTION_DEFINE L\n");
            sql.append("         WHERE T.WH_ID = L.WH_ID\n");
            sql.append("           AND T.PART_ID = L.PART_ID) LOC_NAME\n");

//            sql.append(" FROM TT_PART_OEM_PO T,TT_PART_MAKER_DEFINE M WHERE  T.MAKER_ID=M.MAKER_ID(+) AND T.CHK_ID='")
            sql.append(" FROM TT_PART_OEM_PO T WHERE 1=1 ");
//            sql.append("AND T.CHK_ID='").append(chkId).append("'"); //暂时屏蔽测试hyy
            
            sql.append(" ORDER BY  T.PART_OLDCODE ");
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    /**
     * @param chkId
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> queryOrderListInfo(String chkIds) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");


            sql.append("SELECT P.PART_OLDCODE,\n");
            sql.append("       P.PART_CNAME,\n");
            sql.append("       P.PART_CODE,\n");
            sql.append("       P.UNIT,\n");
            sql.append("       (SELECT D.LOC_CODE\n");
            sql.append("          FROM TT_PART_LOACTION_DEFINE D\n");
            sql.append("         WHERE   D.PART_ID = P.PART_ID\n");
            sql.append("           AND D.WH_ID = P.WH_ID\n");
            sql.append("           AND ROWNUM = 1) LOC_CODE,\n");
            sql.append("       P.GENERATE_QTY,\n");
            sql.append("       NULL AS YSSL,\n");
            sql.append("       P.VENDER_NAME,\n");
            sql.append("       (SELECT M.MAKER_NAME\n");
            sql.append("          FROM TT_PART_MAKER_DEFINE M\n");
            sql.append("         WHERE M.MAKER_ID = P.MAKER_ID) MAKER_NAME,\n");
            sql.append("       P.CHK_CODE,\n");
            sql.append("       (SELECT U.NAME FROM TC_USER U WHERE U.USER_ID = P.CREATE_BY) PLANER,\n");
            sql.append("       (SELECT U.NAME FROM TC_USER U WHERE U.USER_ID = P.WHMAN_ID) WHMAN,\n");
            sql.append("       NULL AS NOTE\n");
            sql.append("  FROM TT_PART_OEM_PO P\n");
            sql.append(" WHERE 1 = 1\n");
            sql.append("  AND p.chk_id in (").append(chkIds).append(")");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public Map<String, Object> getOrderMain(String orderId) throws Exception {
        Map<String, Object> map;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT A.ORDER_ID,A.ORDER_CODE,A.BUYER_ID,A.BUYER,TO_CHAR(A.CREATE_DATE,'yyyy-MM-dd') CREATE_DATE,A.WH_ID,A.WH_NAME,A.PLAN_TYPE,A.SUM_QTY,to_char(A.AMOUNT,'fm999,999,999,999,990.00') AMOUNT,A.STATE, A.REMARK");
            sql.append(" FROM TT_PART_PO_MAIN A WHERE A.ORDER_ID = ")
                    .append(orderId);
            map = pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return map;
    }

    public PageResult<Map<String, Object>> queryPurchaseOrderDetailList(
            String orderId, String partOldCode,
            String partCname, String partCode, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT ROWNUM R_NUM,T.POLINE_ID, \n");
            sql.append("        M.ORDER_ID, \n");
            sql.append("        M.PUR_ORDER_CODE, \n");
            sql.append("        T.PART_ID, \n");
            sql.append("        T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.PART_TYPE, \n");
            sql.append("        T.UNIT, \n");
            sql.append("        T.PLAN_QTY, \n");
            sql.append("        T.BUY_QTY, \n");
            sql.append("        (T.BUY_QTY - T.CHECK_QTY) GE_QTY, \n");
            sql.append("        T.CHECK_QTY, \n");
            sql.append("        T.BUY_PRICE, \n");
//            sql.append("(SELECT s.sale_price3 FROM tt_part_sales_price s WHERE s.part_id=t.part_id AND s.state=10011001 AND s.status=1) BUY_PRICE,\n");
//            sql.append("(SELECT round(s.sale_price3*BUY_QTY,2) FROM tt_part_sales_price s WHERE s.part_id=t.part_id AND s.state=10011001 AND s.status=1) BUY_AMOUNT,\n");
            sql.append("        T.BUY_AMOUNT, \n");
            sql.append("        T.VENDER_ID, \n");
            sql.append("        T.VENDER_NAME, \n");
            sql.append("        (SELECT T1.MAKER_ID \n");
            sql.append("           FROM TT_PART_MAKER_DEFINE T1, tt_part_maker_relation T2 \n");
            sql.append("          WHERE T1.MAKER_ID = T2.MAKER_ID \n");
            sql.append("            AND T2.PART_ID = T.PART_ID \n");
            sql.append("            AND T2.Is_Default = ").append(Constant.IF_TYPE_YES);
            sql.append("            AND ROWNUM = 1) MAKER_ID, \n");
            sql.append("        (SELECT T1.MAKER_NAME \n");
            sql.append("           FROM TT_PART_MAKER_DEFINE T1, tt_part_maker_relation T2 \n");
            sql.append("          WHERE T1.MAKER_ID = T2.MAKER_ID \n");
            sql.append("            AND T2.PART_ID = T.PART_ID \n");
            sql.append("            AND T2.Is_Default = ").append(Constant.IF_TYPE_YES);
            sql.append("            AND ROWNUM = 1) MAKER_NAME, \n");
            sql.append("        (SELECT L.WHMAN_ID \n");
            sql.append("           FROM TT_PART_LOACTION_DEFINE L \n");
            sql.append("          WHERE L.PART_ID = T.PART_ID \n");
            sql.append("            AND L.WH_ID = M.WH_ID) WHMAN_ID, \n");
            sql.append("        (SELECT U.NAME \n");
            sql.append("           FROM TT_PART_LOACTION_DEFINE L, TC_USER U \n");
            sql.append("          WHERE L.WHMAN_ID = U.USER_ID \n");
            sql.append("            AND L.PART_ID = T.PART_ID \n");
            sql.append("            AND L.WH_ID = M.WH_ID) WHMAN_NAME, \n");
            sql.append("          T.REMARK\n");
            sql.append("   FROM TT_PART_PO_DTL T, TT_PART_PO_MAIN M \n");
            sql.append("  where T.ORDER_ID = M.ORDER_ID \n");
            sql.append(" AND T.IS_PRODUCT_RECV = 1 ");//未生成领用订单
            sql.append("    AND T.ORDER_ID = ").append(orderId);
            if (!partOldCode.equals("")) {
                sql.append(" AND upper(T.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!partCname.equals("")) {
                sql.append(" AND T.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            if (!partCode.equals("")) {
                sql.append(" AND upper(T.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            sql.append("  AND t.buy_qty>t.check_qty ");
            if (!"".equals(partCode)) {
                sql.append("  ORDER BY T.PART_CODE ASC ");
            }
            if (!"".equals(partOldCode)) {
                sql.append("  ORDER BY T.PART_OLDCODE ASC ");
            }
            if ("".equals(partCode) && "".equals(partOldCode)) {
                sql.append("  ORDER BY T.PART_OLDCODE ASC ");
            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public Map<String, Object> getChkInfoByPartIdAndChkCode(Long partId,
                                                            String checkCode) throws Exception {
        Map<String, Object> map;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.CHECK_ID,T.CHECK_QTY,T.SPARE_QTY FROM TT_PART_PO_CHK T WHERE T.CHECK_CODE=");
            sql.append("'" + checkCode + "'");
            sql.append(" AND T.PART_ID=").append(partId);
            map = pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return map;
    }

    public List<Map<String, Object>> queryPurOrderDetailList(String orderId) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("SELECT T.POLINE_ID, T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,T.PART_TYPE,T.UNIT,T.PLAN_QTY,T.BUY_QTY,(T.BUY_QTY-T.CHECK_QTY) GE_QTY,");
            sql.append("T.CHECK_QTY,T.BUY_PRICE,T.BUY_AMOUNT,T.VENDER_ID,T.VENDER_NAME,TO_CHAR(T.FORECAST_DATE,'yyyy-MM-dd') FORECAST_DATE,T.REMARK");
            //屏蔽这段是制造商信息（目前还没制造商）
//            sql.append(" (SELECT T1.MAKER_ID FROM TT_PART_MAKER_DEFINE T1,TT_PART_MAKER_RELATION T2 WHERE T1.MAKER_ID=T2.MAKER_ID");
//            sql.append(" AND T1.VENDER_ID=T.VENDER_ID AND T2.PART_ID=T.PART_ID AND T2.IS_DEFAULT=").append(Constant.IF_TYPE_YES);
//            sql.append(" AND ROWNUM=1)MAKER_ID, (SELECT T1.MAKER_NAME FROM TT_PART_MAKER_DEFINE T1,TT_PART_MAKER_RELATION T2 WHERE T1.MAKER_ID=T2.MAKER_ID");
//            sql.append(" AND T1.VENDER_ID=T.VENDER_ID AND T2.PART_ID=T.PART_ID AND T2.IS_DEFAULT=").append(Constant.IF_TYPE_YES);
//            sql.append(" AND ROWNUM=1) MAKER_NAME FROM TT_PART_PO_DTL T");
            sql.append(" FROM TT_PART_PO_DTL T");
            sql.append(" where  T.ORDER_ID=").append(orderId);
            sql.append(" AND T.IS_PRODUCT_RECV = 1 ");//未生成领用订单，默认值1
           /* sql.append("  AND t.buy_qty<>t.check_qty");*/
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public PageResult<Map<String, Object>> queryPartOrderChkAllList(
            String checkCode, String venderId, String beginTime,
            String endTime, String pBeginTime, String pEndTime, String planerId, String whmanId,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            //sql.append("SELECT  T.CHK_ID,T.CHK_CODE,T.VENDER_ID,T.VENDER_NAME,TRUNC(T.CREATE_DATE) CREATE_DATE,SUM(T.GENERATE_QTY) GENERATE_QTY,T.PRINT_DATE, t.print_times  FROM TT_PART_OEM_PO T where 1=1 ");
            //add by yuan 20130819
            sql.append("SELECT T.CHK_ID,\n");
            sql.append("       T.CHK_CODE,\n");
            sql.append("       T.ORDER_CODE,\n");
            sql.append("       T.VENDER_ID,\n");
            sql.append("       T.VENDER_NAME,\n");
            sql.append("       M.MAKER_ID,\n");
            sql.append("       M.MAKER_NAME,\n");
            sql.append("       TRUNC(T.CREATE_DATE) CREATE_DATE,\n");
            sql.append("       SUM(T.GENERATE_QTY) GENERATE_QTY,\n");
            sql.append("       T.PRINT_DATE,\n");
            sql.append("       T.PRINT_TIMES,\n");
            sql.append("       T.PLANER,\n");
            sql.append("       (SELECT U.NAME FROM TC_USER U WHERE U.USER_ID = T.WHMAN_ID) WHMAN\n");
            sql.append("  FROM TT_PART_OEM_PO T,TT_PART_MAKER_DEFINE M\n");
            sql.append(" WHERE T.MAKER_ID=M.MAKER_ID\n");

            if (!"".equals(checkCode)) {
                sql.append(" AND T.CHK_CODE like upper('%").append(checkCode).append("%')");
            }
            if (!"".equals(planerId)) {
                sql.append(" AND T.PLANER_ID=").append(planerId);
            }
            if (!"".equals(whmanId) && null != whmanId) {
                sql.append(" AND T.WHMAN_ID=").append(whmanId);
            }
            if (!"".equals(venderId)) {
                sql.append(" AND T.VENDER_ID=").append(venderId);
            }
            if (!"".equals(beginTime)) {
                sql.append(" AND to_date(T.CREATE_DATE)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND to_date(T.CREATE_DATE)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(pBeginTime)) {
                sql.append(" AND to_date(T.PRINT_DATE)>=").append("to_date('").append(pBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(pEndTime)) {
                sql.append(" AND to_date(T.PRINT_DATE)<=").append("to_date('").append(pEndTime).append("','yyyy-MM-dd')");
            }
            sql.append("  AND T.Origin_Type !=").append(Constant.ORDER_ORIGIN_TYPE_03);

            sql.append(" GROUP BY T.CHK_ID,\n");
            sql.append("         T.CHK_CODE,\n");
            sql.append("         T.VENDER_ID,\n");
            sql.append("         T.VENDER_NAME,\n");
            sql.append("         M.MAKER_ID,\n");
            sql.append("         M.MAKER_NAME,\n");
            sql.append("         T.ORDER_CODE,\n");
            sql.append("         TRUNC(T.CREATE_DATE),\n");
            sql.append("         T.PRINT_DATE,\n");
            sql.append("         T.PRINT_TIMES,\n");
            sql.append("         T.PLANER,\n");
            sql.append("         T.WHMAN_ID\n");
            sql.append(" ORDER BY T.CHK_CODE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> queryPartOrderChkInstockList(RequestWrapper request,
                                                                        Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String checkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//验收单号
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//验收开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//验收结束时间
            String pBeginTime = CommonUtils.checkNull(request.getParamValue("pBeginTime"));//打印开始时间
            String pEndTime = CommonUtils.checkNull(request.getParamValue("pEndTime"));//打印结束时间
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//状态
            String state1 = CommonUtils.checkNull(request.getParamValue("STATE1"));//状态
            String originType = CommonUtils.checkNull(request.getParamValue("ORDER_ORIGIN_TYPE"));//来源
            String planId = CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员
            String partOldCode1 = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE1"));//配件编码
            String partCode1 = CommonUtils.checkNull(request.getParamValue("PART_CODE1"));//配件件号
            String partName1 = CommonUtils.checkNull(request.getParamValue("PART_NAME1"));//配件件号


            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT T.CHK_ID,\n");
            sql.append("       T.CHK_CODE,\n");
            sql.append("       T.ORDER_CODE,\n");
            sql.append("       T.REMARK2,\n");
            sql.append("       T.ORIGIN_TYPE,\n");
            sql.append("       T.VENDER_ID,\n");

            sql.append("(SELECT D.VENDER_NAME\n");
            sql.append("        FROM TT_PART_VENDER_DEFINE D\n");
            sql.append("       WHERE D.VENDER_ID = T.VENDER_ID) VENDER_NAME,\n");

            sql.append("       TRUNC(T.CREATE_DATE) CREATE_DATE,\n");
            sql.append("       SUM(T.IN_QTY) IN_QTY,\n");
            sql.append("       SUM(T.GENERATE_QTY) GENERATE_QTY,\n");
            sql.append("       max(T.PRINT_DATE2) PRINT_DATE,\n");
            sql.append("       max(T.PRINT_TIMES2) PRINT_TIMES,\n");
            sql.append("       T.IN_DATE,\n");
            sql.append("       U.NAME IN_NAME,\n");
            sql.append("       U1.NAME PLANER,\n");
            sql.append("       U2.NAME WHMAN_NAME,\n");
          /*  sql.append("       (CASE\n");
            sql.append("         WHEN SUM(T.IN_QTY) = SUM(T.GENERATE_QTY) THEN\n");
            sql.append("          10041001\n");
            sql.append("         WHEN SUM(T.IN_QTY) <> SUM(T.GENERATE_QTY) THEN\n");
            sql.append("          10041002\n");
            sql.append("       END) AS STATE\n");*/
            sql.append("DECODE(NVL((SELECT COUNT(1)\n");
            sql.append("                  FROM TT_PART_OEM_PO T1\n");
            sql.append("                 WHERE T1.STATE = 92271001\n");
            sql.append("                   AND T1.CHK_ID = T.CHK_ID\n");
            sql.append("                 GROUP BY T1.CHK_ID),\n");
            sql.append("                0),\n");
            sql.append("            0,\n");
            sql.append("            10041001,\n");
            sql.append("            10041002) STATE,\n");
            //sql.append("            DECODE(SUM(T.SPAREIN_QTY),0,10041001,10041002) STATE1\n");

            sql.append("CASE\n");
            sql.append("        WHEN SUM(T.In_Qty) = SUM(T.GENERATE_QTY) THEN\n");
            sql.append("         '完全入库'\n");
            sql.append("         WHEN SUM(T.IN_QTY) <> SUM(T.GENERATE_QTY) AND SUM(T.IN_QTY)>0 THEN\n");
            sql.append("         '部分入库'\n");
            sql.append("        WHEN SUM(T.In_Qty) = 0 THEN\n");
            sql.append("         '未入库'\n");
            sql.append("      END STATE1\n");

            sql.append("  FROM TT_PART_OEM_PO T,TC_USER U,TC_USER U1,TC_USER U2\n");
            sql.append(" WHERE T.IN_BY=U.USER_ID(+) AND T.PLANER_ID=U1.USER_ID(+) AND T.WHMAN_ID=U2.USER_ID(+)");
            //add by yuan 20130807
            //sql.append(" AND T.STATE <> ").append(Constant.PART_PURCHASE_ORDERCHK_STATUS_02);


            if (!"".equals(checkCode)) {
                sql.append(" AND T.CHK_CODE like '%").append(checkCode).append("%'");
            }
            if (!"".equals(partOldCode1)) {
                sql.append(" AND UPPER(T.PART_OLDCODE) like '%").append(partOldCode1.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode1)) {
                sql.append(" AND UPPER(T.PART_CODE) like '%").append(partCode1.toUpperCase()).append("%'");
            }
            if (!"".equals(partName1)) {
                sql.append(" AND UPPER(T.PART_CNAME) like '%").append(partName1.toUpperCase()).append("%'");
            }
//            if (!"".equals(planId)) {//屏蔽测试hyy 
//                sql.append(" AND ( T.PLANER_ID=").append(planId);
//                sql.append(" OR T.CREATE_BY = ").append(planId);
//                sql.append(" OR T.ORIGIN_TYPE = DECODE(NVL((SELECT PU.IS_DIRECT\n");
//                sql.append(" FROM TT_PART_USERPOSE_DEFINE PU\n");
//                sql.append(" WHERE PU.USER_ID = ").append(planId);
//                sql.append(" AND PU.USER_TYPE = 1),\n");
//                sql.append(" 10041002),\n");
//                sql.append(" 10041001,\n");
//                sql.append(" 92751003,\n");
//                sql.append(" 1))\n");
//            }
            if (!"".equals(originType)) {
                sql.append(" AND T.ORIGIN_TYPE = ").append(originType);
            }
            if (!"".equals(venderId)) {
                sql.append(" AND T.VENDER_ID=").append(venderId);
            }
            if (!"".equals(beginTime)) {
                sql.append(" AND trunc(T.CREATE_DATE)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND trunc(T.CREATE_DATE)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(pBeginTime)) {
                sql.append(" AND trunc(T.PRINT_DATE2)>=").append("to_date('").append(pBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(pEndTime)) {
                sql.append(" AND trunc(T.PRINT_DATE2)<=").append("to_date('").append(pEndTime).append("','yyyy-MM-dd')");
            }
//            if (!"".equals(state)) {//屏蔽测试hyy 
//
//                sql.append(" AND DECODE(NVL((SELECT COUNT(1)");
//                sql.append("                   FROM TT_PART_OEM_PO T1");
//                sql.append("                  WHERE T1.STATE = 92271001");
//                sql.append("                    AND T1.CHK_ID = T.CHK_ID");
//                sql.append("                  GROUP BY T1.CHK_ID),");
//                sql.append("                 0),");
//                sql.append("             0,");
//                sql.append("             10041001,");
//                sql.append("             10041002) =").append(state);
//
//            }
            if (!"".equals(state1)) {
                if ("10041001".equals(state1)) { //完全入库
                    sql.append(" HAVING SUM(t.generate_qty)=SUM(t.in_qty) AND SUM(t.sparein_qty)=0");
                } else if ("10041002".equals(state1)) {//未入库
                    sql.append("  HAVING SUM(T.IN_QTY) = 0");
                } else { //部分入库
                    sql.append("  HAVING SUM(t.generate_qty)!=SUM(t.in_qty) AND SUM(t.in_qty)>0");
                }
            }
            sql.append("  GROUP BY  T.CHK_ID,T.CHK_CODE,T.ORDER_CODE,T.IN_DATE,U.NAME,U1.NAME,U2.NAME,T.ORIGIN_TYPE,T.VENDER_ID,TRUNC(T.CREATE_DATE),T.REMARK2");
            sql.append(" ORDER BY T.CHK_CODE ASC, TRUNC(T.CREATE_DATE)");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public Map<String, Object> getChkOrderMain(String chkCode) throws Exception {
        Map<String, Object> map;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.CHECK_CODE,T.VENDER_ID,T.VENDER_NAME,TRUNC(T.CHECK_DATE) CHECK_DATE,SUM(T.CHECK_QTY) CHECK_QTY,TRUNC(T.PRINT_DATE) PRINT_DATE FROM TT_PART_PO_CHK T");
            sql.append("  GROUP BY T.CHECK_CODE,T.VENDER_ID,T.VENDER_NAME,CHECK_DATE,T.PRINT_DATE HAVING T.CHECK_CODE='")
                    .append(chkCode + "'");
            map = pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return map;
    }

    public List<Map<String, Object>> queryChkOrderDetailList(String chkCode) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT A.PART_CODE,A.PART_OLDCODE,A.PART_CNAME,A.UNIT,A.VENDER_NAME,A.CHECK_QTY FROM TT_PART_PO_CHK A WHERE A.CHECK_CODE='")
                    .append(chkCode + "'");
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public PageResult<Map<String, Object>> queryPurOrderDtlList(
            String orderCode, String partOldCode, String partCname, String partCode, String beginTime, String endTime, String state, Integer curPage,
            Integer pageSize) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT T1.ORDER_ID,\n");
            sql.append("       T1.ORDER_CODE,\n");
            sql.append("       T2.PART_CODE,\n");
            sql.append("       T2.PART_OLDCODE,\n");
            sql.append("       T2.PART_CNAME,\n");
            sql.append("       T2.UNIT,\n");
            sql.append("       T2.VENDER_NAME,\n");
            sql.append("       T2.PLAN_QTY,\n");
            sql.append("       T2.BUY_QTY,\n");
            sql.append("       T2.CHECK_QTY,\n");
            sql.append("       T2.SPARE_QTY,\n");
            sql.append("       T1.BUYER,\n");

            sql.append("CASE\n");
            sql.append("      WHEN T1.STATE <> 92691003 AND\n");
            sql.append("           ((TRUNC(SYSDATE) - TRUNC(T1.CREATE_DATE)) >\n");
            sql.append("           (SELECT D.DAYS\n");
            sql.append("               FROM TT_PART_PERIOD_DEFINE D\n");
            sql.append("              WHERE D.STATE = 10011001\n");
            sql.append("                AND D.STATUS = 1)) AND T2.SPARE_QTY > 0 THEN --非已关闭且超过订单有效期且剩余数量大于0 状态已关闭\n");
            sql.append("       92691004\n");
            sql.append("      WHEN T1.STATE <> 92691003 AND T2.SPARE_QTY <= 0 THEN --非已关闭且剩余数量为大于小于0 状态已完成\n");
            sql.append("       92691002\n");
            sql.append("      WHEN T1.STATE <> 92691003 AND T2.SPARE_QTY > 0 THEN --非已关闭且剩余数量大于0 状态为未完成\n");
            sql.append("       92691001\n");
            sql.append("      ELSE\n");
            sql.append("       T1.STATE\n");
            sql.append("    END STATE\n");

            sql.append(" FROM TT_PART_PO_MAIN T1,TT_PART_PO_DTL T2 WHERE T1.ORDER_ID=T2.ORDER_ID AND T1.PLAN_TYPE<>").append(Constant.PART_PURCHASE_PLAN_TYPE_03);
            /*sql.append(" AND T1.CREATE_BY=").append(logonUser.getUserId());*/
            sql.append(" AND T1.ORDER_CODE LIKE '%").append(orderCode).append("%'");
            sql.append(" AND T2.PART_OLDCODE LIKE '%").append(partOldCode).append("%'");
            sql.append(" AND T2.PART_CNAME LIKE '%").append(partCname).append("%'");
            sql.append(" AND T2.PART_CODE LIKE '%").append(partCode).append("%'");
            if (!"".equals(beginTime)) {
                sql.append(" AND trunc(t1.create_date)>=to_date('").append(beginTime).append("','yyyy-mm-dd')");
            }
            if (!"".equals(endTime)) {
                sql.append(" AND trunc(t1.create_date)<=to_date('").append(endTime).append("','yyyy-mm-dd')");
            }
            if (!"".equals(state)) {
                if (state.equals(Constant.PURCHASE_ORDER_STATE_04 + "")) {

                    sql.append(" AND T1.STATE <> 92691003 AND\n");
                    sql.append("              ((TRUNC(SYSDATE) - TRUNC(T1.CREATE_DATE)) >\n");
                    sql.append("              (SELECT D.DAYS\n");
                    sql.append("                 FROM TT_PART_PERIOD_DEFINE D\n");
                    sql.append("                WHERE D.STATE = 10011001\n");
                    sql.append("                  AND D.STATUS = 1)) AND T2.SPARE_QTY > 0\n");
                } else if (state.equals(Constant.PURCHASE_ORDER_STATE_01 + "")) {
                    sql.append(" AND T1.STATE <> 92691003 AND T2.SPARE_QTY > 0\n");
                } else if (state.equals(Constant.PURCHASE_ORDER_STATE_02 + "")) {
                    sql.append(" And T1.STATE <> 92691003 AND T2.SPARE_QTY <= 0\n");
                } else {
                    sql.append(" AND t1.state =").append(state);
                }
            }
            sql.append(" ORDER BY T2.PART_OLDCODE ASC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public Map<String, Object> getBillInfo(String dealeridBill) throws Exception {
        Map<String, Object> map;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T.DEALER_NAME, T.ADDR, T.TEL, T.BANK, T.TAX_NO, T.ACCOUNT \n");
            sql.append("   FROM TT_PART_BILL_DEFINE T \n");
            sql.append("  WHERE T.DEALER_ID = ").append(dealeridBill);
            map = pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return map;
    }

    public Map<String, Object> getVenderInfo(Long venderId) throws Exception {
        Map<String, Object> map;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T.VENDER_NAME, T.LINKMAN, T.TEL, T.FAX \n");
            sql.append("   FROM TT_PART_VENDER_DEFINE T \n");
            sql.append("  WHERE T.VENDER_ID = ").append(venderId);
            map = pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return map;
    }

    public boolean isAllChk(String orderId) throws Exception {
        List<Map<String, Object>> list;
        try {

            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT A.PART_ID \n");
            sql.append("   FROM TT_PART_PO_DTL A \n");
            sql.append("  WHERE A.ORDER_ID = ").append(orderId).append("\n");
            sql.append("    AND A.SPARE_QTY > 0 \n");

            list = pageQuery(sql.toString(), null, getFunName());
            if (list.size() > 0) {
                return false;
            }
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    public List queryWhmanInfo() throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT A.USER_ID WHMAN_ID, U.NAME WHMAN_NAME \n");
            sql.append("   FROM TT_PART_USERPOSE_DEFINE A, TC_USER U \n");
            sql.append("  WHERE A.USER_ID = U.USER_ID \n");
            sql.append("    AND A.USER_TYPE = 4 ");

            return pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPurchaseOrderDetailList1(
            String orderId, String partOldCode, String partCname,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T.POLINE_ID, \n");
            sql.append("        T.PART_ID, \n");
            sql.append("        T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.PART_TYPE, \n");
            sql.append("        T.UNIT, \n");
            sql.append("        T.PLAN_QTY, \n");
            sql.append("        T.BUY_QTY, \n");
            sql.append("        (T.BUY_QTY - T.CHECK_QTY) GE_QTY, \n");
            sql.append("        T.CHECK_QTY, \n");
            sql.append("        T.BUY_PRICE, \n");
            sql.append("        T.BUY_AMOUNT, \n");
            sql.append("        T.VENDER_ID, \n");
            sql.append("        T.VENDER_NAME, \n");
            sql.append("        (SELECT T1.MAKER_ID \n");
            sql.append("           FROM TT_PART_MAKER_DEFINE T1, TT_PART_MAKER_RELATION T2 \n");
            sql.append("          WHERE T1.MAKER_ID = T2.MAKER_ID \n");
            sql.append("            AND T2.PART_ID = T.PART_ID \n");
            sql.append("            AND T2.IS_DEFAULT = ").append(Constant.IF_TYPE_YES);
            sql.append("            AND ROWNUM = 1) MAKER_ID, \n");
            sql.append("        (SELECT T1.MAKER_NAME \n");
            sql.append("           FROM TT_PART_MAKER_DEFINE T1, TT_PART_MAKER_RELATION T2 \n");
            sql.append("          WHERE T1.MAKER_ID = T2.MAKER_ID \n");
            sql.append("            AND T2.PART_ID = T.PART_ID \n");
            sql.append("            AND T2.IS_DEFAULT = ").append(Constant.IF_TYPE_YES);
            sql.append("            AND ROWNUM = 1) MAKER_NAME, \n");
            sql.append("        (SELECT L.WHMAN_ID \n");
            sql.append("           FROM TT_PART_LOACTION_DEFINE L \n");
            sql.append("          WHERE L.PART_ID = T.PART_ID \n");
            sql.append("            AND L.WH_ID = M.WH_ID) WHMAN_ID, \n");
            sql.append("        (SELECT U.NAME \n");
            sql.append("           FROM TT_PART_LOACTION_DEFINE L, TC_USER U \n");
            sql.append("          WHERE L.WHMAN_ID = U.USER_ID \n");
            sql.append("            AND L.PART_ID = T.PART_ID \n");
            sql.append("            AND L.WH_ID = M.WH_ID) WHMAN_NAME, \n");
            sql.append("         T.REMARK\n");
            sql.append("   FROM TT_PART_PO_DTL T, TT_PART_PO_MAIN M \n");
            sql.append("  where T.ORDER_ID = M.ORDER_ID \n");
            sql.append("    AND T.ORDER_ID = ").append(orderId);
            if (!partOldCode.equals("")) {
                sql.append(" AND T.PART_OLDCODE LIKE '%").append(partOldCode).append("%'");
            }
            if (!partCname.equals("")) {
                sql.append(" AND T.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            sql.append(" ORDER BY T.PART_OLDCODE");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> queryPurOrderDtlList(
            String chkId, String partOldCode, String partCname, Integer curPage,
            Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT P.PART_OLDCODE, \n");
            sql.append("        P.PART_CNAME, \n");
            sql.append("        P.PART_CODE, \n");
            sql.append("        P.UNIT, \n");
            sql.append("        (SELECT D.LOC_CODE \n");
            sql.append("           FROM TT_PART_LOACTION_DEFINE D \n");
            sql.append("          WHERE D.PART_ID = P.PART_ID \n");
            sql.append("            AND D.WH_ID = P.WH_ID \n");
            sql.append("            AND ROWNUM = 1) LOC_CODE, \n");
            sql.append("        P.GENERATE_QTY, \n");
            sql.append("        P.CHECK_QTY, \n");
            sql.append("        P.SPARE_QTY, \n");
            sql.append("        P.VENDER_NAME, \n");
            sql.append("        (SELECT M.MAKER_NAME \n");
            sql.append("           FROM TT_PART_MAKER_DEFINE M \n");
            sql.append("          WHERE M.MAKER_ID = P.MAKER_ID) MAKER_NAME, \n");
            sql.append("        P.CHK_CODE, \n");
            sql.append("        (SELECT U.NAME FROM TC_USER U WHERE U.USER_ID = P.CREATE_BY) PLANER, \n");
            sql.append("        (SELECT U.NAME FROM TC_USER U WHERE U.USER_ID = P.WHMAN_ID) WHMAN, \n");
            sql.append("        NULL AS NOTE \n");
            sql.append("   FROM TT_PART_OEM_PO P \n");
            sql.append("  WHERE 1 = 1 \n");
            sql.append("    AND p.chk_id = ").append(chkId);

            if (!partOldCode.equals("")) {
                sql.append(" AND P.PART_OLDCODE LIKE '%").append(partOldCode).append("%'");
            }
            if (!partCname.equals("")) {
                sql.append(" AND P.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> getPartInfo(RequestWrapper request, String orderId) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT ROWNUM R_NUM,T.POLINE_ID, \n");
            sql.append("        T.PART_ID, \n");
            sql.append("        T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.PART_TYPE, \n");
            sql.append("        T.UNIT, \n");
            sql.append("        T.PLAN_QTY, \n");
            sql.append("        T.BUY_QTY, \n");
            sql.append("        (T.BUY_QTY - T.CHECK_QTY) GE_QTY, \n");
            sql.append("        T.CHECK_QTY, \n");
            sql.append("        T.BUY_PRICE, \n");
            sql.append("        T.BUY_AMOUNT, \n");
            sql.append("        T.VENDER_ID, \n");
            sql.append("        T.VENDER_NAME, \n");
            sql.append("        Q.QTY, \n");
            sql.append("        Q.REMARK, \n");
            sql.append("        (SELECT T1.MAKER_ID \n");
            sql.append("           FROM TT_PART_MAKER_DEFINE T1, tt_part_maker_relation T2 \n");
            sql.append("          WHERE T1.MAKER_ID = T2.MAKER_ID \n");
            sql.append("            AND T2.PART_ID = T.PART_ID \n");
            sql.append("            AND T2.Is_Default = ").append(Constant.IF_TYPE_YES);
            sql.append("            AND ROWNUM = 1) MAKER_ID, \n");
            sql.append("        (SELECT T1.MAKER_NAME \n");
            sql.append("           FROM TT_PART_MAKER_DEFINE T1, tt_part_maker_relation T2 \n");
            sql.append("          WHERE T1.MAKER_ID = T2.MAKER_ID \n");
            sql.append("            AND T2.PART_ID = T.PART_ID \n");
            sql.append("            AND T2.Is_Default = ").append(Constant.IF_TYPE_YES);
            sql.append("            AND ROWNUM = 1) MAKER_NAME \n");
                /*sql.append("        (SELECT L.WHMAN_ID \n");
                sql.append("           FROM TT_PART_LOACTION_DEFINE L \n");
	            sql.append("          WHERE L.PART_ID = T.PART_ID \n");
	            sql.append("            AND L.WH_ID = M.WH_ID) WHMAN_ID, \n");
	            sql.append("        (SELECT U.NAME \n");
	            sql.append("           FROM TT_PART_LOACTION_DEFINE L, TC_USER U \n");
	            sql.append("          WHERE L.WHMAN_ID = U.USER_ID \n");
	            sql.append("            AND L.PART_ID = T.PART_ID \n");
	            sql.append("            AND L.WH_ID = M.WH_ID) WHMAN_NAME \n");*/
            sql.append("   FROM TT_PART_PO_DTL T, TT_PART_PO_MAIN M,TMP_PART_UPLOAD Q \n");
            sql.append("  where T.ORDER_ID = M.ORDER_ID AND T.PART_OLDCODE=Q.PART_OLDCODE \n");
            sql.append("    AND T.ORDER_ID = ").append(orderId);

            sql.append("  AND t.buy_qty>t.check_qty");
            sql.append("  ORDER BY T.PART_OLDCODE ASC");
            list = this.pageQuery(sql.toString(), null, this.getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<Map<String, Object>> getPartInfoNot(RequestWrapper request,
                                                    String orderId) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT *\n");
            sql.append("  FROM (SELECT T1.PART_OLDCODE, T1.QTY, '主数据不存在' REMARK\n");
            sql.append("          FROM TMP_PART_UPLOAD T1\n");
            sql.append("         WHERE NOT EXISTS (SELECT 1\n");
            sql.append("                  FROM TT_PART_PO_DTL T\n");
            sql.append("                 WHERE T1.PART_OLDCODE = T.PART_OLDCODE\n");
            sql.append("                   AND T.ORDER_ID = " + orderId + ")\n");
            sql.append("        UNION ALL\n");
            sql.append("        SELECT T.PART_OLDCODE, Q.QTY, '已生成验收指令' REMARK\n");
            sql.append("          FROM TT_PART_PO_DTL T, TT_PART_PO_MAIN M, TMP_PART_UPLOAD Q\n");
            sql.append("         WHERE T.ORDER_ID = M.ORDER_ID\n");
            sql.append("           AND T.PART_OLDCODE = Q.PART_OLDCODE\n");
            sql.append("           AND T.ORDER_ID = " + orderId + "\n");
            sql.append("           AND T.BUY_QTY <= T.CHECK_QTY) A\n");
            sql.append(" ORDER BY A.PART_OLDCODE\n");


            list = this.pageQuery(sql.toString(), null, this.getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<Map<String, Object>> getPartInfo1(RequestWrapper request,
                                                  String orderId) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT ROWNUM R_NUM,T.POLINE_ID, \n");
            sql.append("        T.PART_ID, \n");
            sql.append("        T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.PART_TYPE, \n");
            sql.append("        T.UNIT, \n");
            sql.append("        T.PLAN_QTY, \n");
            sql.append("        T.BUY_QTY, \n");
            sql.append("        (T.BUY_QTY - T.CHECK_QTY) GE_QTY, \n");
            sql.append("        T.CHECK_QTY, \n");
            sql.append("        T.BUY_PRICE, \n");
            sql.append("        T.BUY_AMOUNT, \n");
            sql.append("        T.VENDER_ID, \n");
            sql.append("        T.VENDER_NAME, \n");
            sql.append("        Q.QTY, \n");
            sql.append("        Q.REMARK, \n");
            sql.append("        T.VENDER_ID MAKER_ID, \n");
            sql.append("        T.VENDER_NAME MAKER_NAME \n");
                /*sql.append("        (SELECT L.WHMAN_ID \n");
	            sql.append("           FROM TT_PART_LOACTION_DEFINE L \n");
	            sql.append("          WHERE L.PART_ID = T.PART_ID \n");
	            sql.append("            AND L.WH_ID = M.WH_ID) WHMAN_ID, \n");
	            sql.append("        (SELECT U.NAME \n");
	            sql.append("           FROM TT_PART_LOACTION_DEFINE L, TC_USER U \n");
	            sql.append("          WHERE L.WHMAN_ID = U.USER_ID \n");
	            sql.append("            AND L.PART_ID = T.PART_ID \n");
	            sql.append("            AND L.WH_ID = M.WH_ID) WHMAN_NAME \n");*/
            sql.append("   FROM TT_PART_PO_DTL T, TT_PART_PO_MAIN M,TMP_PART_UPLOAD Q \n");
            sql.append("  where T.ORDER_ID = M.ORDER_ID AND T.PART_OLDCODE=Q.PART_OLDCODE \n");
            sql.append("    AND T.ORDER_ID = ").append(orderId);

            sql.append("  AND t.buy_qty>t.check_qty");
            sql.append("  ORDER BY T.PART_OLDCODE ASC");
            list = this.pageQuery(sql.toString(), null, this.getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<Map<String, Object>> queryOrderDtlInfo(String[] poIds) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.PO_ID,\n");
            sql.append("       T.ORDER_CODE,\n");
            sql.append("       T.PLAN_CODE,\n");
            sql.append("       T.CHK_CODE,\n");
            sql.append("       T.WH_NAME,\n");
            sql.append("       T.VENDER_NAME,\n");
            sql.append("       0.17 ORATE,\n");
            sql.append("       (SELECT U.NAME FROM TC_USER U WHERE U.USER_ID=T.CREATE_BY) NAME,\n");
            sql.append("       M.MAKER_NAME,\n");
            sql.append("       TO_CHAR(T.CREATE_DATE, 'yyyy/MM/dd HH:mm:ss') CREATE_DATE,\n");
            sql.append("       T.PART_CODE,\n");
            sql.append("       T.PART_OLDCODE,\n");
            sql.append("       T.PART_CNAME,\n");
            sql.append("       T.IN_QTY GENERATE_QTY,\n");
            sql.append("       T.UNIT,\n");
            sql.append("       (SELECT TO_CHAR(P.SALE_PRICE3, 'FM999,999,999,999,990.00')\n");
            sql.append("          FROM TT_PART_SALES_PRICE P\n");
            sql.append("         WHERE T.PART_ID = P.PART_ID) PLAN_PRICE,\n");
            sql.append("       (SELECT T.IN_QTY * P.SALE_PRICE3\n");
            sql.append("          FROM TT_PART_SALES_PRICE P\n");
            sql.append("         WHERE T.PART_ID = P.PART_ID) AMOUNT,\n");
            sql.append("       (SELECT TO_CHAR(T.IN_QTY * P.SALE_PRICE3, 'FM999,999,999,999,990.00')\n");
            sql.append("          FROM TT_PART_SALES_PRICE P\n");
            sql.append("         WHERE T.PART_ID = P.PART_ID) FAMOUNT,\n");
            sql.append("       (SELECT L.LOC_NAME\n");
            sql.append("          FROM TT_PART_LOACTION_DEFINE L\n");
            sql.append("         WHERE T.WH_ID = L.WH_ID\n");
            sql.append("           AND T.PART_ID = L.PART_ID) LOC_NAME\n");

            sql.append(" FROM TT_PART_OEM_PO T,TT_PART_MAKER_DEFINE M WHERE\n");
            sql.append(" T.MAKER_ID=M.MAKER_ID(+)\n");
            sql.append(" AND T.IN_QTY>0 AND T.PO_ID IN(");
            for (int i = 0; i < poIds.length; i++) {
                sql.append(poIds[i]).append(",");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(") ORDER BY  T.PART_OLDCODE ");
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public PageResult<Map<String, Object>> queryPurOrderDtl4Mod(String orderId,
                                                                String partOldCode, String partCname, String PART_CODE, Integer curPage,
                                                                Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T.POLINE_ID, \n");
            sql.append("        M.ORDER_ID, \n");
            sql.append("        T.PART_ID, \n");
            sql.append("        T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.PART_TYPE, \n");
            sql.append("        T.UNIT, \n");
            sql.append("        T.PLAN_QTY, \n");
            sql.append("        T.BUY_QTY, \n");
            sql.append("        (T.BUY_QTY - T.CHECK_QTY) GE_QTY, \n");
            sql.append("        T.CHECK_QTY, \n");
            sql.append("        T.BUY_PRICE, \n");
            sql.append("        T.BUY_AMOUNT, \n");
            sql.append("        T.VENDER_ID, \n");
            sql.append("        T.VENDER_NAME, \n");
            sql.append("        (SELECT T1.MAKER_ID \n");
            sql.append("           FROM TT_PART_MAKER_DEFINE T1, TT_PART_MAKER_RELATION T2 \n");
            sql.append("          WHERE T1.MAKER_ID = T2.MAKER_ID \n");
            sql.append("            AND T2.PART_ID = T.PART_ID \n");
            sql.append("            AND T2.IS_DEFAULT = ").append(Constant.IF_TYPE_YES);
            sql.append("            AND ROWNUM = 1) MAKER_ID, \n");
            sql.append("        (SELECT T1.MAKER_NAME \n");
            sql.append("           FROM TT_PART_MAKER_DEFINE T1, TT_PART_MAKER_RELATION T2 \n");
            sql.append("          WHERE T1.MAKER_ID = T2.MAKER_ID \n");
            sql.append("            AND T2.PART_ID = T.PART_ID \n");
            sql.append("            AND T2.IS_DEFAULT = ").append(Constant.IF_TYPE_YES);
            sql.append("            AND ROWNUM = 1) MAKER_NAME, \n");
            sql.append("        (SELECT L.WHMAN_ID \n");
            sql.append("           FROM TT_PART_LOACTION_DEFINE L \n");
            sql.append("          WHERE L.PART_ID = T.PART_ID \n");
            sql.append("            AND L.WH_ID = M.WH_ID) WHMAN_ID, \n");
            sql.append("        (SELECT U.NAME \n");
            sql.append("           FROM TT_PART_LOACTION_DEFINE L, TC_USER U \n");
            sql.append("          WHERE L.WHMAN_ID = U.USER_ID \n");
            sql.append("            AND L.PART_ID = T.PART_ID \n");
            sql.append("            AND L.WH_ID = M.WH_ID) WHMAN_NAME \n");
            sql.append("   FROM TT_PART_PO_DTL T, TT_PART_PO_MAIN M \n");
            sql.append("  where T.ORDER_ID = M.ORDER_ID \n");
            sql.append("    AND T.CHECK_QTY = 0");
            sql.append("    AND T.ORDER_ID = ").append(orderId);
            if (!partOldCode.equals("")) {
                sql.append(" AND T.PART_OLDCODE LIKE '%").append(partOldCode).append("%'");
            }
            if (!partCname.equals("")) {
                sql.append(" AND T.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            if (!PART_CODE.equals("")) {
                sql.append(" AND T.PART_CODE LIKE '%").append(PART_CODE).append("%'");
            }
            sql.append(" ORDER BY T.PART_OLDCODE");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public boolean isAllDel(String orderId) throws Exception {
        List<Map<String, Object>> list;
        try {

            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT A.PART_ID \n");
            sql.append("   FROM TT_PART_PO_DTL A \n");
            sql.append("  WHERE A.ORDER_ID = ").append(orderId).append("\n");

            list = pageQuery(sql.toString(), null, getFunName());
            if (list.size() > 0) {
                return false;
            }
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    public boolean isAllDelCon(String orderId) throws Exception {
        List<Map<String, Object>> list;
        try {

            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT A.PART_ID \n");
            sql.append("   FROM TT_PART_PO_DTL A \n");
            sql.append("  WHERE A.ORDER_ID = ").append(orderId).append("\n");
            sql.append("    AND A.CHECK_QTY = 0");
            list = pageQuery(sql.toString(), null, getFunName());
            if (list.size() > 0) {
                return false;
            }
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    public PageResult<Map<String, Object>> queryPartOrderChkList1(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        String checkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//验收单号
        String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
        String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//验收开始时间
        String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//验收结束时间
        String pBeginTime = CommonUtils.checkNull(request.getParamValue("pBeginTime"));//打印开始时间
        String pEndTime = CommonUtils.checkNull(request.getParamValue("pEndTime"));//打印结束时间
        String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员
        String whmanId = CommonUtils.checkNull(request.getParamValue("WHMAN_ID"));//保管员
        String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
        String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
        String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
        //zhumingwei 2013-09-11 添加是否打印查询条件 begin
        String isPrint = CommonUtils.checkNull(request.getParamValue("is_print"));//是否打印
        //zhumingwei 2013-09-11 添加是否打印查询条件 end
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.CHK_ID,\n");
            sql.append("       T.PO_ID,\n");
            sql.append("       T.CHK_CODE,\n");
            sql.append("       T.ORDER_CODE,\n");
            sql.append("       T.PART_ID,\n");
            sql.append("       T.PART_OLDCODE,\n");
            sql.append("       T.PART_CNAME,\n");
            sql.append("       T.PART_CODE,\n");
            sql.append("       T.VENDER_ID,\n");
            sql.append("       T.VENDER_NAME,\n");
            sql.append("       M.MAKER_ID,\n");
            sql.append("       M.MAKER_NAME,\n");
            sql.append("       T.CREATE_DATE,\n");
            sql.append("       T.GENERATE_QTY,\n");
            sql.append("       T.PRINT_DATE,\n");
            sql.append("       T.PRINT_TIMES,\n");
            sql.append("       T.PLANER,\n");
            sql.append("       (SELECT U.NAME FROM TC_USER U WHERE U.USER_ID = T.WHMAN_ID) WHMAN\n");
            sql.append("  FROM TT_PART_OEM_PO T,TT_PART_MAKER_DEFINE M\n");
            sql.append(" WHERE T.MAKER_ID=M.MAKER_ID\n");

            if (!"".equals(checkCode)) {
                sql.append(" AND T.CHK_CODE like upper('%").append(checkCode).append("%')");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T.PART_OLDCODE) like '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCname)) {
                sql.append(" AND T.PART_CNAME like '%").append(partCname).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T.PART_CODE) like '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(planerId)) {
                sql.append(" AND T.PLANER_ID=").append(planerId);
            }
            if (!"".equals(whmanId) && null != whmanId) {
                sql.append(" AND T.WHMAN_ID=").append(whmanId);
            }
            if (!"".equals(venderId)) {
                sql.append(" AND T.VENDER_ID=").append(venderId);
            }
            if (!"".equals(beginTime)) {
                sql.append(" AND to_date(T.CREATE_DATE)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND to_date(T.CREATE_DATE)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(pBeginTime)) {
                sql.append(" AND to_date(T.PRINT_DATE)>=").append("to_date('").append(pBeginTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(pEndTime)) {
                sql.append(" AND to_date(T.PRINT_DATE)<=").append("to_date('").append(pEndTime).append("','yyyy-MM-dd')");
            }
            //zhumingwei 2013-09-11 添加是否打印查询条件 begin
            if (!"".equals(isPrint) && "0".equals(isPrint)) {
                sql.append(" AND T.PRINT_TIMES = 0");
            }
            if (!"".equals(isPrint) && "1".equals(isPrint)) {
                sql.append(" AND T.PRINT_TIMES > 0");
            }
            //过滤已入库验收单     yuan
            sql.append(" AND NOT EXISTS (SELECT 1\n");
            sql.append("         FROM TT_PART_PO_IN I\n");
            sql.append("        WHERE I.PART_ID = T.PART_ID\n");
            sql.append("          AND I.CHECK_ID = T.CHK_ID)\n");

            //zhumingwei 2013-09-11 添加是否打印查询条件 end
            sql.append("  AND T.ORIGIN_TYPE !=").append(Constant.ORDER_ORIGIN_TYPE_03);
            sql.append("  AND T.STATE !=").append(Constant.PART_PURCHASE_ORDERCHK_STATUS_03);
            sql.append(" ORDER BY T.CHK_CODE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;

    }

    public List<Map<String, Object>> queryOrderListInfo1(String[] poIds) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT P.PART_OLDCODE,\n");
            sql.append("       P.PART_CNAME,\n");
            sql.append("       P.PART_CODE,\n");
            sql.append("       P.UNIT,\n");
            sql.append("       (SELECT D.LOC_CODE\n");
            sql.append("          FROM TT_PART_LOACTION_DEFINE D\n");
            sql.append("         WHERE   D.PART_ID = P.PART_ID\n");
            sql.append("           AND D.WH_ID = P.WH_ID\n");
            sql.append("           AND ROWNUM = 1) LOC_CODE,\n");
            sql.append("       P.GENERATE_QTY,\n");
            sql.append("       NULL AS YSSL,\n");
            sql.append("       P.VENDER_NAME,\n");
            sql.append("       (SELECT M.MAKER_NAME\n");
            sql.append("          FROM TT_PART_MAKER_DEFINE M\n");
            sql.append("         WHERE M.MAKER_ID = P.MAKER_ID) MAKER_NAME,\n");
            sql.append("       P.CHK_CODE,\n");
            sql.append("       (SELECT U.NAME FROM TC_USER U WHERE U.USER_ID = P.CREATE_BY) PLANER,\n");
            sql.append("       (SELECT U.NAME FROM TC_USER U WHERE U.USER_ID = P.WHMAN_ID) WHMAN,\n");
            sql.append("       NULL AS NOTE\n");
            sql.append("  FROM TT_PART_OEM_PO P\n");
            sql.append(" WHERE 1 = 1\n");
            sql.append("  AND P.PO_ID IN(");
            for (int i = 0; i < poIds.length; i++) {
                sql.append(poIds[i]).append(",");
            }
            sql.deleteCharAt(sql.length() - 1).append(")");
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public PageResult<Map<String, Object>> queryPurOrderDtlList(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
            String makerId = CommonUtils.checkNull(request.getParamValue("MAKER_ID"));
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String partTYpe = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_NAME"));

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT T1.CHK_CODE, \n");
            sql.append("        T1.ORDER_CODE, \n");
            sql.append("        T1.VENDER_NAME, \n");
            sql.append("        T4.MAKER_NAME, \n");
            sql.append("        T1.WH_NAME, \n");
            sql.append("        DECODE(T3.PLAN_TYPE,").append(Constant.PART_PURCHASE_PLAN_TYPE_02).append(", '紧急订单', '采购订单') PLAN_TYPE, \n");
            sql.append("        T1.PART_TYPE, \n");
            sql.append("        T1.PART_OLDCODE, \n");
            sql.append("        T1.PART_CNAME, \n");
            sql.append("        T1.PART_CODE, \n");
            sql.append("        T1.UNIT, \n");
            sql.append("        T1.ORIGIN_TYPE, \n");
            sql.append("        TO_CHAR(T1.BUY_PRICE,'FM999,999,999,999,990.00') BUY_PRICE, \n");
            sql.append("        TO_CHAR(T1.BUY_AMOUNT,'FM999,999,999,999,990.00') BUY_AMOUNT, \n");
            sql.append("        T2.LOC_CODE, \n");
            sql.append("        T1.CHECK_QTY, \n");
            sql.append("        T1.IN_QTY, \n");
            sql.append("        T2.IN_DATE, \n");
            sql.append("        T1.CREATE_DATE, \n");
            sql.append("        T5.NAME, \n");
            sql.append("        (SELECT U.NAME FROM TC_USER U WHERE U.USER_ID=T1.CREATE_BY) PLAN_NAME, \n");
            sql.append("        NVL(V.NORMAL_QTY, 0) NORMAL_QTY, \n");
            sql.append("        DECODE(NVL(T2.IN_DATE, ''), '',").append(Constant.IF_TYPE_NO).append(",").append(Constant.IF_TYPE_YES).append(") IS_INCODE, \n");
            sql.append("        DECODE(T1.SPAREIN_QTY, 0, ").append(Constant.IF_TYPE_YES).append(",").append(Constant.IF_TYPE_NO).append(") IS_ALLIN, \n");
            sql.append("        DECODE(NVL(T1.PRINT_DATE, ''), '',").append(Constant.IF_TYPE_NO).append(",").append(Constant.IF_TYPE_YES).append(") IS_PRINT \n");
            sql.append("   FROM TT_PART_OEM_PO       T1, \n");
            sql.append("        TT_PART_PO_IN        T2, \n");
            sql.append("        TT_PART_PLAN_MAIN    T3, \n");
            sql.append("        TT_PART_MAKER_DEFINE T4, \n");
            sql.append("        TC_USER              T5, \n");
            sql.append("        VW_PART_STOCK        V \n");
            sql.append("  WHERE T1.PO_ID = T2.PO_ID(+) \n");
            sql.append("    AND T1.WH_ID = T2.WH_ID(+) \n");
            sql.append("    AND T1.PLAN_ID = T3.PLAN_ID(+) \n");
            sql.append("    AND T1.MAKER_ID = T4.MAKER_ID \n");
            sql.append("    AND T1.WHMAN_ID = T5.USER_ID(+) \n");
            sql.append("    AND T1.PART_ID = V.PART_ID(+) \n");
            sql.append("    AND T1.WH_ID = V.WH_ID(+) \n");

            if (!"".equals(planerId)) {
                sql.append(" AND T1.CREATE_BY = ").append(planerId);
            }

            if (!"".equals(chkCode)) {
                sql.append(" AND T1.CHK_CODE LIKE '%").append(chkCode).append("%'");
            }
            if (!"".equals(orderCode)) {
                sql.append(" AND T1.ORDER_CODE LIKE '%").append(orderCode).append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T1.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T1.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partTYpe)) {
                sql.append(" AND T1.PART_TYPE=").append(partTYpe);
            }
            if (!"".equals(venderId)) {
                sql.append(" AND T1.VENDER_ID=").append(venderId);
            }
            if (!"".equals(makerId)) {
                sql.append(" AND T1.MAKER_ID=").append(makerId);
            }
            if (!"".equals(beginTime)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(endTime)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(whId)) {
                sql.append(" AND T1.WH_ID=").append(whId);
            }
            sql.append(" ORDER BY T1.CHK_CODE");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public Map<String, Object> queryMainInfo(RequestWrapper request) throws Exception {
        try {
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
            String makerId = CommonUtils.checkNull(request.getParamValue("MAKER_ID"));
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String partTYpe = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_NAME"));

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT COUNT(1) CHK_COUNT, \n");
            sql.append("        NVL(SUM(T1.CHECK_QTY),0) CHK_NUM, \n");
            sql.append("        TO_CHAR(NVL(SUM(T1.BUY_PRICE * T1.CHECK_QTY),0), 'FM999,999,999,999,990.00') CHK_AMOUNT \n");
            sql.append("   FROM TT_PART_OEM_PO T1  WHERE 1=1\n");


            if (!"".equals(planerId)) {
                sql.append(" AND T1.CREATE_BY = ").append(planerId);
            }

            if (!"".equals(chkCode)) {
                sql.append(" AND T1.CHK_CODE LIKE '%").append(chkCode).append("%'");
            }
            if (!"".equals(orderCode)) {
                sql.append(" AND T1.ORDER_CODE LIKE '%").append(orderCode).append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T1.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T1.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partTYpe)) {
                sql.append(" AND T1.PART_TYPE=").append(partTYpe);
            }
            if (!"".equals(venderId)) {
                sql.append(" AND T1.VENDER_ID=").append(venderId);
            }
            if (!"".equals(makerId)) {
                sql.append(" AND T1.MAKER_ID=").append(makerId);
            }
            if (!"".equals(beginTime)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(endTime)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(whId)) {
                sql.append(" AND T1.WH_ID=").append(whId);
            }

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> queryChkOrderDtl(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
            String makerId = CommonUtils.checkNull(request.getParamValue("MAKER_ID"));
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String partTYpe = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_NAME"));

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT T1.CHK_CODE, \n");
            sql.append("        T1.ORDER_CODE, \n");
            sql.append("        T1.VENDER_NAME, \n");
            sql.append("        T4.MAKER_NAME, \n");
            sql.append("        T1.WH_NAME, \n");
            sql.append("        DECODE(T3.PLAN_TYPE,").append(Constant.PART_PURCHASE_PLAN_TYPE_02).append(", '紧急订单', '采购订单') PLAN_TYPE, \n");
            sql.append("        DECODE(T1.PART_TYPE, \n")
                    .append(Constant.PART_BASE_PART_TYPES_SELF_MADE).append(",'自制件',")
                    .append(Constant.PART_BASE_PART_TYPES_PURCHASE).append(",'国产件',")
                    .append(Constant.PART_BASE_PART_TYPES_ENTRANCE).append(",'进口件')")
                    .append(" PART_TYPE,");
            sql.append("        DECODE(T1.ORIGIN_TYPE, \n")
                    .append(Constant.ORDER_ORIGIN_TYPE_01).append(",'计划',")
                    .append(Constant.ORDER_ORIGIN_TYPE_02).append(",'领用',")
                    .append(Constant.ORDER_ORIGIN_TYPE_03).append(",'直发')")
                    .append(" ORIGIN_TYPE,");
            sql.append("        T1.PART_OLDCODE, \n");
            sql.append("        T1.PART_CNAME, \n");
            sql.append("        T1.PART_CODE, \n");
            sql.append("        T1.UNIT, \n");
            sql.append("        TO_CHAR(T1.BUY_PRICE,'FM999,999,999,999,990.00') BUY_PRICE, \n");
            sql.append("        TO_CHAR(T1.BUY_AMOUNT,'FM999,999,999,999,990.00') BUY_AMOUNT, \n");
            sql.append("        T2.LOC_CODE, \n");
            sql.append("        T1.CHECK_QTY, \n");
            sql.append("        T1.IN_QTY, \n");
            sql.append("        T2.IN_DATE, \n");
            sql.append("        T1.CREATE_DATE, \n");
            sql.append("        T5.NAME, \n");
            sql.append("        (SELECT U.NAME FROM TC_USER U WHERE U.USER_ID=T1.CREATE_BY) PLAN_NAME, \n");
            sql.append("        NVL(V.NORMAL_QTY, 0) NORMAL_QTY, \n");
            sql.append("        DECODE(NVL(T2.IN_DATE, ''), '',").append(Constant.IF_TYPE_NO).append(",").append(Constant.IF_TYPE_YES).append(") IS_INCODE, \n");
            sql.append("        DECODE(T1.SPAREIN_QTY, 0, ").append(Constant.IF_TYPE_YES).append(",").append(Constant.IF_TYPE_NO).append(") IS_ALLIN, \n");
            sql.append("        DECODE(NVL(T1.PRINT_DATE, ''), '',").append(Constant.IF_TYPE_NO).append(",").append(Constant.IF_TYPE_YES).append(") IS_PRINT \n");
            sql.append("   FROM TT_PART_OEM_PO       T1, \n");
            sql.append("        TT_PART_PO_IN        T2, \n");
            sql.append("        TT_PART_PLAN_MAIN    T3, \n");
            sql.append("        TT_PART_MAKER_DEFINE T4, \n");
            sql.append("        TC_USER              T5, \n");
            sql.append("        VW_PART_STOCK        V \n");
            sql.append("  WHERE T1.PO_ID = T2.PO_ID(+) \n");
            sql.append("    AND T1.WH_ID = T2.WH_ID(+) \n");
            sql.append("    AND T1.PLAN_ID = T3.PLAN_ID(+) \n");
            sql.append("    AND T1.MAKER_ID = T4.MAKER_ID \n");
            sql.append("    AND T1.WHMAN_ID = T5.USER_ID(+) \n");
            sql.append("    AND T1.PART_ID = V.PART_ID(+) \n");
            sql.append("    AND T1.WH_ID = V.WH_ID(+) \n");

            if (!"".equals(planerId)) {
                sql.append(" AND T1.CREATE_BY = ").append(planerId);
            }

            if (!"".equals(chkCode)) {
                sql.append(" AND T1.CHK_CODE LIKE '%").append(chkCode).append("%'");
            }
            if (!"".equals(orderCode)) {
                sql.append(" AND T1.ORDER_CODE LIKE '%").append(orderCode).append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T1.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T1.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partTYpe)) {
                sql.append(" AND T1.PART_TYPE=").append(partTYpe);
            }
            if (!"".equals(venderId)) {
                sql.append(" AND T1.VENDER_ID=").append(venderId);
            }
            if (!"".equals(makerId)) {
                sql.append(" AND T1.MAKER_ID=").append(makerId);
            }
            if (!"".equals(beginTime)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(endTime)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(whId)) {
                sql.append(" AND T1.WH_ID=").append(whId);
            }
            sql.append(" ORDER BY T1.CHK_CODE");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    //导出订单入库明细 add zhumingwei 2013-09-22
    public List<Map<String, Object>> exportPurOrder(String orderId) throws Exception {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT T.POLINE_ID, \n");
        sql.append("        T.PART_ID, \n");
        sql.append("        T.PART_CODE, \n");
        sql.append("        T.PART_OLDCODE, \n");
        sql.append("        T.PART_CNAME, \n");
        sql.append("        T.PART_TYPE, \n");
        sql.append("        T.UNIT, \n");
        sql.append("        T.PLAN_QTY, \n");
        sql.append("        T.BUY_QTY, \n");
        sql.append("        (T.BUY_QTY - T.CHECK_QTY) GE_QTY, \n");
        sql.append("        T.CHECK_QTY, \n");
        sql.append("        T.BUY_PRICE, \n");
        sql.append("        T.BUY_AMOUNT, \n");
        sql.append("        T.VENDER_ID, \n");
        sql.append("        T.VENDER_NAME, \n");
        sql.append("        (SELECT T1.MAKER_ID \n");
        sql.append("           FROM TT_PART_MAKER_DEFINE T1, TT_PART_MAKER_RELATION T2 \n");
        sql.append("          WHERE T1.MAKER_ID = T2.MAKER_ID \n");
        sql.append("            AND T2.PART_ID = T.PART_ID \n");
        sql.append("            AND T2.IS_DEFAULT = ").append(Constant.IF_TYPE_YES);
        sql.append("            AND ROWNUM = 1) MAKER_ID, \n");
        sql.append("        (SELECT T1.MAKER_NAME \n");
        sql.append("           FROM TT_PART_MAKER_DEFINE T1, TT_PART_MAKER_RELATION T2 \n");
        sql.append("          WHERE T1.MAKER_ID = T2.MAKER_ID \n");
        sql.append("            AND T2.PART_ID = T.PART_ID \n");
        sql.append("            AND T2.IS_DEFAULT = ").append(Constant.IF_TYPE_YES);
        sql.append("            AND ROWNUM = 1) MAKER_NAME, \n");
        sql.append("        (SELECT L.WHMAN_ID \n");
        sql.append("           FROM TT_PART_LOACTION_DEFINE L \n");
        sql.append("          WHERE L.PART_ID = T.PART_ID \n");
        sql.append("            AND L.WH_ID = M.WH_ID) WHMAN_ID, \n");
        sql.append("        (SELECT U.NAME \n");
        sql.append("           FROM TT_PART_LOACTION_DEFINE L, TC_USER U \n");
        sql.append("          WHERE L.WHMAN_ID = U.USER_ID \n");
        sql.append("            AND L.PART_ID = T.PART_ID \n");
        sql.append("            AND L.WH_ID = M.WH_ID) WHMAN_NAME \n");
        sql.append("   FROM TT_PART_PO_DTL T, TT_PART_PO_MAIN M \n");
        sql.append("  where T.ORDER_ID = M.ORDER_ID \n");
        sql.append("    AND T.ORDER_ID = ").append(orderId);
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());

        return list;
    }

    public List<Map<String, Object>> getCheckkerList() throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT U.USER_ID, U.NAME \n");
            sql.append("   FROM TT_PART_OEM_PO T, TC_USER U \n");
            sql.append("  WHERE T.CREATE_BY = U.USER_ID \n");
            sql.append("  GROUP BY U.USER_ID, U.NAME \n");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @param whId
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-10-30
     * @Title : 获取采购订单中的领用配件
     */
    public PageResult<Map<String, Object>> showPartStockBase(String sbString, String whId, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT P.PART_ID,");
        sql.append(" P.PART_CODE, ");
        sql.append(" P.PART_OLDCODE, ");
        sql.append(" P.PART_CNAME, ");
        sql.append(" P.UNIT, ");
        sql.append(" NVL((SELECT TD.NORMAL_QTY FROM VW_PART_STOCK TD WHERE SD.PART_ID = TD.PART_ID AND TD.WH_ID = '" + whId + "'), '0') NORMAL_QTY, ");
        sql.append(" TO_CHAR(BP.BUY_PRICE,'fm999,999,999,990.00') AS  BUY_PRICE, ");
        sql.append(" TO_CHAR(SP.SALE_PRICE3,'fm999,999,999,990.00') AS  PLAN_PRICE, ");
        sql.append(" NVL(PP.SAFETY_STOCK,'0') SAFETY_STOCK, ");
        sql.append(" NVL(PP.ORDER_QTY,'0') ORDER_QTY, ");
        sql.append(" NVL(PD.BUY_QTY, '0') AS PLAN_QTY, ");
        sql.append(" SD.ROOM, ");
        sql.append(" SD.WHMAN, ");
        sql.append(" U.NAME ");
        sql.append(" FROM TT_PART_BUY_PRICE BP, TT_PART_SALES_PRICE SP, ");
        sql.append(" TT_PART_DEFINE P, ");
        sql.append(" TT_PART_PLAN_DEFINE PP, ");
        sql.append(" TT_PART_WAREHOUSE_DEFINE W, ");
        sql.append(" TT_PART_SPPLAN_DEFINE SD, ");
        sql.append(" TT_PART_PO_DTL PD, ");
        sql.append(" TC_USER U ");
        sql.append(" WHERE PP.WH_ID = W.WH_ID ");
        sql.append(" AND PP.PART_ID = P.PART_ID ");
        sql.append(" AND PP.PLAN_TYPE = 1 ");//月计划类型
        sql.append(" AND P.IS_RECEIVE = '" + Constant.IF_TYPE_YES + "' ");//是领用件
        sql.append(" AND W.WH_NAME LIKE '%景德镇%' ");//景德镇仓库
        sql.append(" AND P.STATE = '" + Constant.STATUS_ENABLE + "' ");//有效
        sql.append(" AND SD.STATE = '" + Constant.STATUS_ENABLE + "' ");//有效
        sql.append(" AND P.PART_ID = SD.PART_ID ");
        sql.append(" AND SD.PART_ID = BP.PART_ID ");
        sql.append(" AND SD.PART_ID = SP.PART_ID ");
        sql.append(" AND SD.PART_ID = PD.PART_ID ");
        sql.append(" AND SD.PLANNER_ID = U.USER_ID(+) ");
        sql.append(" AND PD.CHECK_QTY <= 0 ");//未生成验收指令的
        sql.append(sbString);
        sql.append(" ORDER BY SD.WHMAN ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-10-30
     * @Title : 验证采购订单是否已生成验收指令
     */
    public List<Map<String, Object>> checkOrderDtlList(String sqlStr) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT PD.* ");
            sql.append("   FROM TT_PART_PO_DTL PD ");
            sql.append("  WHERE 1 = 1 ");
            sql.append(sqlStr);

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-10-30
     * @Title : 获取新增领用订单明细
     */
    public List<Map<String, Object>> getPartSpplanInfos(String sqlStr) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT SD.*, PD.POLINE_ID, PD.BUY_QTY FROM TT_PART_SPPLAN_DEFINE SD, TT_PART_PO_DTL PD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND PD.PART_ID = SD.PART_ID ");
        sql.append(sqlStr);

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-10-30
     * @Title : 统计采购订单数量
     */
    public List<Map<String, Object>> getCountPoMain(String sqlStr) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT COUNT(PM.ORDER_CODE) AS ORDER_COUNT FROM TT_PART_PO_MAIN PM ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public PageResult<Map<String, Object>> queryPurOrderDtlDeliveryList(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));
            String planCode = CommonUtils.checkNull(request.getParamValue("PLAN_CODE"));
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String partTYpe = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));

            StringBuffer sql = new StringBuffer("");

            if ("1".equals(radioSelect)) {
                sql.append(" SELECT PL.PLAN_CODE, --计划单号 \n");
                sql.append("        PL.CREATE_DATE, --制单日期 \n");
                sql.append("        PL.FC_DATE, --预计到货日期 \n");
                sql.append("        PL.PL_QTY, --计划总数量 \n");
                sql.append("        PL.PL_AMT, --计划总金额 \n");
                sql.append("        PL.PLAN_TYPE, --计划类型 \n");
                sql.append("        PO.ORDER_CODE, --采购订单号 \n");
                sql.append("        PO.POCNT, --订货品种数 \n");
                sql.append("        PO.BUY_QTY, --订货总数量 \n");
                sql.append("        PO.BUY_AMT, --订单金额 \n");
                sql.append("        PO.VENDER_NAME, --供货商名称 \n");
                sql.append("        PO.STATE, --订单状态 \n");
                sql.append("        PI.INCNT, --已交货品种数 \n");
                sql.append("        PI.IN_QTY, --已交货数 \n");
                sql.append("        PI.IN_AMT, --已交货金额 \n");
                sql.append("        CASE \n");
                sql.append("          WHEN NVL(PI.INCNT, 0) > 0 THEN \n");
                sql.append("           TO_CHAR(PI.INCNT / PO.POCNT * 100, '999.99') || '%' \n");
                sql.append("          ELSE \n");
                sql.append("           '-' \n");
                sql.append("        END CNTRT, --品种满足率 \n");
                sql.append("        CASE \n");
                sql.append("          WHEN NVL(PI.IN_QTY, 0) > 0 THEN \n");
                sql.append("           TO_CHAR(PI.IN_QTY / PO.BUY_QTY * 100, '999.99') || '%' \n");
                sql.append("          ELSE \n");
                sql.append("           '-' \n");
                sql.append("        END QTYRT, --数量满足率 \n");
                sql.append("        CASE \n");
                sql.append("          WHEN PI.IN_QTY >= PO.BUY_QTY THEN \n");
                sql.append("           PI.IN_DATE \n");
                sql.append("          ELSE \n");
                sql.append("           NULL \n");
                sql.append("        END CMPDATE --完成时间 \n");
                sql.append("   FROM (SELECT M.PLAN_ID, \n");
                sql.append("                M.PLAN_CODE, \n");
                sql.append("                TRUNC(M.CREATE_DATE) CREATE_DATE, \n");
                sql.append("                TRUNC(M.CREATE_DATE + 30) FC_DATE, \n");
                sql.append("                COUNT(D.PART_ID) PLCNT, \n");
                sql.append("                SUM(NVL(DECODE(D.CHECK_NUM, 0, D.PLAN_QTY, D.CHECK_NUM), \n");
                sql.append("                        D.PLAN_QTY)) PL_QTY, \n");
                sql.append("                SUM(NVL(D.CHECK_AMOUNT, D.PLAN_AMOUNT)) PL_AMT, \n");
                sql.append("                TC.CODE_DESC PLAN_TYPE \n");
                sql.append("           FROM TT_PART_PLAN_DETAIL D, TT_PART_PLAN_MAIN M, TC_CODE TC \n");
                sql.append("          WHERE D.PLAN_ID = M.PLAN_ID \n");
                sql.append("            AND M.PLAN_TYPE = TC.CODE_ID \n");
                sql.append("            AND M.STATE <> ").append(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_07);

                if (!"".equals(planCode)) {
                    sql.append(" AND M.PLAN_CODE LIKE '%").append(planCode).append("%'");
                }
                if (!"".equals(partOldCode)) {
                    sql.append(" AND UPPER(D.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                if (!"".equals(partCode)) {
                    sql.append(" AND UPPER(D.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
                }
                /*if(!"".equals(partTYpe)){
                	sql.append(" AND D.PART_TYPE=").append(partTYpe);
                }*/

                sql.append("          GROUP BY M.PLAN_ID, M.PLAN_CODE, M.CREATE_DATE, TC.CODE_DESC) PL, \n");
                sql.append("         \n");
                sql.append("        (SELECT PM.PLAN_ID, \n");
                sql.append("                PM.ORDER_ID, \n");
                sql.append("                PM.ORDER_CODE, \n");
                sql.append("                TC.CODE_DESC STATE, \n");
                sql.append("                PD.VENDER_NAME, \n");
                sql.append("                COUNT(PD.PART_ID) POCNT, \n");
                sql.append("                SUM(PD.BUY_QTY) BUY_QTY, \n");
                sql.append("                SUM(PD.BUY_AMOUNT) BUY_AMT \n");
                sql.append("           FROM TT_PART_PO_DTL PD, TT_PART_PO_MAIN PM, TC_CODE TC \n");
                sql.append("          WHERE PD.ORDER_ID = PM.ORDER_ID \n");
                sql.append("            AND PM.STATE = TC.CODE_ID \n");

                if (!"".equals(partOldCode)) {
                    sql.append(" AND UPPER(PD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                if (!"".equals(partCode)) {
                    sql.append(" AND UPPER(PD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
                }
                /*if(!"".equals(partTYpe)){
                	sql.append(" AND PD.PART_TYPE=").append(partTYpe);
                }*/

                sql.append("          GROUP BY PM.PLAN_ID, \n");
                sql.append("                   TC.CODE_DESC, \n");
                sql.append("                   PD.VENDER_NAME, \n");
                sql.append("                   PM.ORDER_ID, \n");
                sql.append("                   PM.ORDER_CODE) PO, \n");
                sql.append("         \n");
                sql.append("        (SELECT OP.PLAN_ID, \n");
                sql.append("                PI.PLAN_CODE, \n");
                sql.append("                OP.ORDER_ID, \n");
                sql.append("                OP.ORDER_CODE, \n");
                sql.append("                COUNT(DISTINCT PI.PART_ID) INCNT, \n");
                sql.append("                SUM(PI.IN_QTY) IN_QTY, \n");
                sql.append("                SUM(PI.IN_AMOUNT) IN_AMT, \n");
                sql.append("                MAX(PI.CHECK_DATE) IN_DATE \n");
                sql.append("           FROM TT_PART_OEM_PO OP, TT_PART_PO_IN PI \n");
                sql.append("          WHERE OP.PO_ID = PI.PO_ID(+) \n");

                if (!"".equals(planCode)) {
                    sql.append(" AND PI.PLAN_CODE LIKE '%").append(planCode).append("%'");
                }
                if (!"".equals(partOldCode)) {
                    sql.append(" AND UPPER(OP.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                if (!"".equals(partCode)) {
                    sql.append(" AND UPPER(OP.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
                }
                /*if(!"".equals(partTYpe)){
                	sql.append(" AND OP.PART_TYPE=").append(partTYpe);
                }*/

                sql.append("          GROUP BY OP.PLAN_ID, PI.PLAN_CODE, OP.ORDER_ID, OP.ORDER_CODE) PI \n");
                sql.append("  WHERE PL.PLAN_ID = PO.PLAN_ID(+) \n");
                sql.append("    AND PO.ORDER_ID = PI.ORDER_ID(+) \n");


                if (!"".equals(orderCode)) {
                    sql.append(" AND PO.ORDER_CODE LIKE '%").append(orderCode).append("%'");
                }

                if (!"".equals(venderName)) {
                    sql.append(" AND PO.VENDER_NAME = '").append(venderName).append("'");
                }

                if (!"".equals(beginTime)) {
                    sql.append(" AND TO_DATE(PL.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endTime)) {
                    sql.append(" AND TO_DATE(PL.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
                }

            }

            if ("2".equals(radioSelect)) {
                sql.append(" SELECT PL.PLAN_CODE, --计划单号 \n");
                sql.append("        PL.CREATE_DATE, --制单日期 \n");
                sql.append("        PL.FC_DATE, --预计到货日期 \n");
                sql.append("        PL.PLAN_TYPE, --计划类型 \n");
                sql.append("        PO.ORDER_CODE, --采购订单好 \n");
                sql.append("        PL.PART_OLDCODE, --配件编码 \n");
                sql.append("        PL.PART_CNAME, --配件名称 \n");
                sql.append("        PL.PART_CODE, --配件件号 \n");
                sql.append("        PL.UNIT, --单位 \n");
                sql.append("        PL.CHECK_NUM, --计划数量 \n");
                sql.append("        PL.CHECK_AMOUNT, --计划金额 \n");
                sql.append("        PO.BUY_QTY, --订单数量 \n");
                sql.append("        PO.BUY_AMOUNT, --订单金额 \n");
                sql.append("        PO.CHECK_QTY, --已转验收单数量 \n");
                sql.append("        PO.SPARE_QTY, --剩余订单数量 \n");
                sql.append("        PO.CREATE_DATE O_CREATE_DATE, --订单创建日期 \n");
                sql.append("        PI.IN_QTY, --入库数量 \n");
                sql.append("        PI.IN_AMOUNT, --入库金额 \n");
                sql.append("        PI.CREATE_DATE IN_DATE \n");
                sql.append("   FROM (SELECT M.PLAN_ID, \n");
                sql.append("                M.PLAN_CODE, \n");
                sql.append("                TRUNC(M.CREATE_DATE) CREATE_DATE, \n");
                sql.append("                TRUNC(M.CREATE_DATE + 30) FC_DATE, \n");
                sql.append("                D.PART_ID, \n");
                sql.append("                D.PART_OLDCODE, \n");
                sql.append("                D.PART_CNAME, \n");
                sql.append("                D.PART_CODE, \n");
                sql.append("                D.UNIT, \n");
                sql.append("                D.CHECK_NUM, \n");
                sql.append("                D.CHECK_AMOUNT, \n");
                sql.append("                TC.CODE_DESC PLAN_TYPE \n");
                sql.append("           FROM TT_PART_PLAN_DETAIL D, TT_PART_PLAN_MAIN M, TC_CODE TC \n");
                sql.append("          WHERE D.PLAN_ID = M.PLAN_ID \n");
                sql.append("            AND M.PLAN_TYPE = TC.CODE_ID \n");
                if (!"".equals(partTYpe)) {
                    sql.append(" AND D.PART_TYPE=").append(partTYpe);
                }
                sql.append("         ) PL, \n");
                sql.append("        (SELECT PM.PLAN_ID, \n");
                sql.append("                PM.ORDER_ID, \n");
                sql.append("                PM.ORDER_CODE, \n");
                sql.append("                PM.STATE, \n");
                sql.append("                PD.VENDER_ID, \n");
                sql.append("                PD.VENDER_NAME, \n");
                sql.append("                PD.PART_ID, \n");
                sql.append("                PD.BUY_QTY, \n");
                sql.append("                PD.BUY_AMOUNT, \n");
                sql.append("                PD.CHECK_QTY, \n");
                sql.append("                PD.SPARE_QTY, \n");
                sql.append("                PD.CREATE_DATE \n");
                sql.append("           FROM TT_PART_PO_DTL PD, TT_PART_PO_MAIN PM \n");
                sql.append("          WHERE PD.ORDER_ID = PM.ORDER_ID \n");
                sql.append("            AND PM.STATE <> " + Constant.PURCHASE_ORDER_STATE_03 + "\n");
                if (!"".equals(partTYpe)) {
                    sql.append(" AND PD.PART_TYPE=").append(partTYpe);
                }

                if (!"".equals(venderName)) {
                    sql.append(" AND PD.VENDER_NAME = '").append(venderName).append("'");
                }
                sql.append("         ) PO, \n");
                sql.append("        (SELECT OP.PLAN_ID, \n");
                sql.append("                PI.PLAN_CODE, \n");
                sql.append("                OP.ORDER_ID, \n");
                sql.append("                PI.ORDER_CODE, \n");
                sql.append("                PI.PART_ID, \n");
                sql.append("                PI.PART_OLDCODE, \n");
                sql.append("                PI.IN_QTY, \n");
                sql.append("                PI.IN_AMOUNT, \n");
                sql.append("                PI.CREATE_DATE \n");
                sql.append("           FROM TT_PART_OEM_PO OP, TT_PART_PO_IN PI \n");
                sql.append("          WHERE OP.PO_ID = PI.PO_ID(+) \n");
                if (!"".equals(partTYpe)) {
                    sql.append(" AND OP.PART_TYPE=").append(partTYpe);
                }
                sql.append("         ) PI \n");
                sql.append("  WHERE PL.PLAN_ID = PO.PLAN_ID(+) \n");
                sql.append("    AND PL.PART_ID = PO.PART_ID(+) \n");
                sql.append("    AND PO.ORDER_ID = PI.ORDER_ID(+) \n");
                sql.append("    AND PO.PART_ID = PI.PART_ID(+) \n");
                sql.append("    AND PO.SPARE_QTY > 0 --未满足品种数量 \n");

                if (!"".equals(planCode)) {
                    sql.append(" AND PL.PLAN_CODE LIKE '%").append(planCode).append("%'");
                }

                if (!"".equals(orderCode)) {
                    sql.append(" AND PO.ORDER_CODE LIKE '%").append(orderCode).append("%'");
                }

                if (!"".equals(partOldCode)) {
                    sql.append(" AND UPPER(PL.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                if (!"".equals(partCode)) {
                    sql.append(" AND UPPER(PL.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
                }

                if (!"".equals(beginTime)) {
                    sql.append(" AND TO_DATE(PO.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endTime)) {
                    sql.append(" AND TO_DATE(PO.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
                }
            }

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPurOrderDtlDelivery(
            RequestWrapper request) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));
            String planCode = CommonUtils.checkNull(request.getParamValue("PLAN_CODE"));
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String partTYpe = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));

            StringBuffer sql = new StringBuffer("");

            if ("1".equals(radioSelect)) {
                sql.append(" SELECT PL.PLAN_CODE, --计划单号 \n");
                sql.append("        PL.CREATE_DATE, --制单日期 \n");
                sql.append("        PL.FC_DATE, --预计到货日期 \n");
                sql.append("        PL.PL_QTY, --计划总数量 \n");
                sql.append("        PL.PL_AMT, --计划总金额 \n");
                sql.append("        PL.PLAN_TYPE, --计划类型 \n");
                sql.append("        PO.ORDER_CODE, --采购订单号 \n");
                sql.append("        PO.POCNT, --订货品种数 \n");
                sql.append("        PO.BUY_QTY, --订货总数量 \n");
                sql.append("        PO.BUY_AMT, --订单金额 \n");
                sql.append("        PO.VENDER_NAME, --供货商名称 \n");
                sql.append("        PO.STATE, --订单状态 \n");
                sql.append("        PI.INCNT, --已交货品种数 \n");
                sql.append("        PI.IN_QTY, --已交货数 \n");
                sql.append("        PI.IN_AMT, --已交货金额 \n");
                sql.append("        CASE \n");
                sql.append("          WHEN NVL(PI.INCNT, 0) > 0 THEN \n");
                sql.append("           TO_CHAR(PI.INCNT / PO.POCNT * 100, '999.99') || '%' \n");
                sql.append("          ELSE \n");
                sql.append("           '-' \n");
                sql.append("        END CNTRT, --品种满足率 \n");
                sql.append("        CASE \n");
                sql.append("          WHEN NVL(PI.IN_QTY, 0) > 0 THEN \n");
                sql.append("           TO_CHAR(PI.IN_QTY / PO.BUY_QTY * 100, '999.99') || '%' \n");
                sql.append("          ELSE \n");
                sql.append("           '-' \n");
                sql.append("        END QTYRT, --数量满足率 \n");
                sql.append("        CASE \n");
                sql.append("          WHEN PI.IN_QTY >= PO.BUY_QTY THEN \n");
                sql.append("           PI.IN_DATE \n");
                sql.append("          ELSE \n");
                sql.append("           NULL \n");
                sql.append("        END CMPDATE --完成时间 \n");
                sql.append("   FROM (SELECT M.PLAN_ID, \n");
                sql.append("                M.PLAN_CODE, \n");
                sql.append("                TRUNC(M.CREATE_DATE) CREATE_DATE, \n");
                sql.append("                TRUNC(M.CREATE_DATE + 30) FC_DATE, \n");
                sql.append("                COUNT(D.PART_ID) PLCNT, \n");
                sql.append("                SUM(NVL(DECODE(D.CHECK_NUM, 0, D.PLAN_QTY, D.CHECK_NUM), \n");
                sql.append("                        D.PLAN_QTY)) PL_QTY, \n");
                sql.append("                SUM(NVL(D.CHECK_AMOUNT, D.PLAN_AMOUNT)) PL_AMT, \n");
                sql.append("                TC.CODE_DESC PLAN_TYPE \n");
                sql.append("           FROM TT_PART_PLAN_DETAIL D, TT_PART_PLAN_MAIN M, TC_CODE TC \n");
                sql.append("          WHERE D.PLAN_ID = M.PLAN_ID \n");
                sql.append("            AND M.PLAN_TYPE = TC.CODE_ID \n");
                sql.append("            AND M.STATE <> ").append(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_07);

                if (!"".equals(planCode)) {
                    sql.append(" AND M.PLAN_CODE LIKE '%").append(planCode).append("%'");
                }
                if (!"".equals(partOldCode)) {
                    sql.append(" AND UPPER(D.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                if (!"".equals(partCode)) {
                    sql.append(" AND UPPER(D.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
                }
	                /*if(!"".equals(partTYpe)){
	                	sql.append(" AND D.PART_TYPE=").append(partTYpe);
	                }*/

                sql.append("          GROUP BY M.PLAN_ID, M.PLAN_CODE, M.CREATE_DATE, TC.CODE_DESC) PL, \n");
                sql.append("         \n");
                sql.append("        (SELECT PM.PLAN_ID, \n");
                sql.append("                PM.ORDER_ID, \n");
                sql.append("                PM.ORDER_CODE, \n");
                sql.append("                TC.CODE_DESC STATE, \n");
                sql.append("                PD.VENDER_NAME, \n");
                sql.append("                COUNT(PD.PART_ID) POCNT, \n");
                sql.append("                SUM(PD.BUY_QTY) BUY_QTY, \n");
                sql.append("                SUM(PD.BUY_AMOUNT) BUY_AMT \n");
                sql.append("           FROM TT_PART_PO_DTL PD, TT_PART_PO_MAIN PM, TC_CODE TC \n");
                sql.append("          WHERE PD.ORDER_ID = PM.ORDER_ID \n");
                sql.append("            AND PM.STATE = TC.CODE_ID \n");

                if (!"".equals(partOldCode)) {
                    sql.append(" AND UPPER(PD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                if (!"".equals(partCode)) {
                    sql.append(" AND UPPER(PD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
                }
	                /*if(!"".equals(partTYpe)){
	                	sql.append(" AND PD.PART_TYPE=").append(partTYpe);
	                }*/

                sql.append("          GROUP BY PM.PLAN_ID, \n");
                sql.append("                   TC.CODE_DESC, \n");
                sql.append("                   PD.VENDER_NAME, \n");
                sql.append("                   PM.ORDER_ID, \n");
                sql.append("                   PM.ORDER_CODE) PO, \n");
                sql.append("         \n");
                sql.append("        (SELECT OP.PLAN_ID, \n");
                sql.append("                PI.PLAN_CODE, \n");
                sql.append("                OP.ORDER_ID, \n");
                sql.append("                OP.ORDER_CODE, \n");
                sql.append("                COUNT(DISTINCT PI.PART_ID) INCNT, \n");
                sql.append("                SUM(PI.IN_QTY) IN_QTY, \n");
                sql.append("                SUM(PI.IN_AMOUNT) IN_AMT, \n");
                sql.append("                MAX(PI.CHECK_DATE) IN_DATE \n");
                sql.append("           FROM TT_PART_OEM_PO OP, TT_PART_PO_IN PI \n");
                sql.append("          WHERE OP.PO_ID = PI.PO_ID(+) \n");

                if (!"".equals(planCode)) {
                    sql.append(" AND PI.PLAN_CODE LIKE '%").append(planCode).append("%'");
                }
                if (!"".equals(partOldCode)) {
                    sql.append(" AND UPPER(OP.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                if (!"".equals(partCode)) {
                    sql.append(" AND UPPER(OP.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
                }
	                /*if(!"".equals(partTYpe)){
	                	sql.append(" AND OP.PART_TYPE=").append(partTYpe);
	                }*/

                sql.append("          GROUP BY OP.PLAN_ID, PI.PLAN_CODE, OP.ORDER_ID, OP.ORDER_CODE) PI \n");
                sql.append("  WHERE PL.PLAN_ID = PO.PLAN_ID(+) \n");
                sql.append("    AND PO.ORDER_ID = PI.ORDER_ID(+) \n");


                if (!"".equals(orderCode)) {
                    sql.append(" AND PO.ORDER_CODE LIKE '%").append(orderCode).append("%'");
                }

                if (!"".equals(venderName)) {
                    sql.append(" AND PO.VENDER_NAME = '").append(venderName).append("'");
                }

                if (!"".equals(beginTime)) {
                    sql.append(" AND TO_DATE(PL.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endTime)) {
                    sql.append(" AND TO_DATE(PL.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
                }

            }

            if ("2".equals(radioSelect)) {
                sql.append(" SELECT PL.PLAN_CODE, --计划单号 \n");
                sql.append("        PL.CREATE_DATE, --制单日期 \n");
                sql.append("        PL.FC_DATE, --预计到货日期 \n");
                sql.append("        PL.PLAN_TYPE, --计划类型 \n");
                sql.append("        PO.ORDER_CODE, --采购订单好 \n");
                sql.append("        PL.PART_OLDCODE, --配件编码 \n");
                sql.append("        PL.PART_CNAME, --配件名称 \n");
                sql.append("        PL.PART_CODE, --配件件号 \n");
                sql.append("        PL.UNIT, --单位 \n");
                sql.append("        PL.CHECK_NUM, --计划数量 \n");
                sql.append("        PL.CHECK_AMOUNT, --计划金额 \n");
                sql.append("        PO.BUY_QTY, --订单数量 \n");
                sql.append("        PO.BUY_AMOUNT, --订单金额 \n");
                sql.append("        PO.CHECK_QTY, --已转验收单数量 \n");
                sql.append("        PO.SPARE_QTY, --剩余订单数量 \n");
                sql.append("        PO.CREATE_DATE O_CREATE_DATE, --订单创建日期 \n");
                sql.append("        PI.IN_QTY, --入库数量 \n");
                sql.append("        PI.IN_AMOUNT, --入库金额 \n");
                sql.append("        PI.CREATE_DATE IN_DATE \n");
                sql.append("   FROM (SELECT M.PLAN_ID, \n");
                sql.append("                M.PLAN_CODE, \n");
                sql.append("                TRUNC(M.CREATE_DATE) CREATE_DATE, \n");
                sql.append("                TRUNC(M.CREATE_DATE + 30) FC_DATE, \n");
                sql.append("                D.PART_ID, \n");
                sql.append("                D.PART_OLDCODE, \n");
                sql.append("                D.PART_CNAME, \n");
                sql.append("                D.PART_CODE, \n");
                sql.append("                D.UNIT, \n");
                sql.append("                D.CHECK_NUM, \n");
                sql.append("                D.CHECK_AMOUNT, \n");
                sql.append("                TC.CODE_DESC PLAN_TYPE \n");
                sql.append("           FROM TT_PART_PLAN_DETAIL D, TT_PART_PLAN_MAIN M, TC_CODE TC \n");
                sql.append("          WHERE D.PLAN_ID = M.PLAN_ID \n");
                sql.append("            AND M.PLAN_TYPE = TC.CODE_ID \n");
                if (!"".equals(partTYpe)) {
                    sql.append(" AND D.PART_TYPE=").append(partTYpe);
                }
                sql.append("         ) PL, \n");
                sql.append("        (SELECT PM.PLAN_ID, \n");
                sql.append("                PM.ORDER_ID, \n");
                sql.append("                PM.ORDER_CODE, \n");
                sql.append("                PM.STATE, \n");
                sql.append("                PD.VENDER_ID, \n");
                sql.append("                PD.VENDER_NAME, \n");
                sql.append("                PD.PART_ID, \n");
                sql.append("                PD.BUY_QTY, \n");
                sql.append("                PD.BUY_AMOUNT, \n");
                sql.append("                PD.CHECK_QTY, \n");
                sql.append("                PD.SPARE_QTY, \n");
                sql.append("                PD.CREATE_DATE \n");
                sql.append("           FROM TT_PART_PO_DTL PD, TT_PART_PO_MAIN PM \n");
                sql.append("          WHERE PD.ORDER_ID = PM.ORDER_ID \n");
                if (!"".equals(partTYpe)) {
                    sql.append(" AND PD.PART_TYPE=").append(partTYpe);
                }

                if (!"".equals(venderName)) {
                    sql.append(" AND PD.VENDER_NAME = '").append(venderName).append("'");
                }
                sql.append("         ) PO, \n");
                sql.append("        (SELECT OP.PLAN_ID, \n");
                sql.append("                PI.PLAN_CODE, \n");
                sql.append("                OP.ORDER_ID, \n");
                sql.append("                PI.ORDER_CODE, \n");
                sql.append("                PI.PART_ID, \n");
                sql.append("                PI.PART_OLDCODE, \n");
                sql.append("                PI.IN_QTY, \n");
                sql.append("                PI.IN_AMOUNT, \n");
                sql.append("                PI.CREATE_DATE \n");
                sql.append("           FROM TT_PART_OEM_PO OP, TT_PART_PO_IN PI \n");
                sql.append("          WHERE OP.PO_ID = PI.PO_ID(+) \n");
                if (!"".equals(partTYpe)) {
                    sql.append(" AND OP.PART_TYPE=").append(partTYpe);
                }
                sql.append("         ) PI \n");
                sql.append("  WHERE PL.PLAN_ID = PO.PLAN_ID(+) \n");
                sql.append("    AND PL.PART_ID = PO.PART_ID(+) \n");
                sql.append("    AND PO.ORDER_ID = PI.ORDER_ID(+) \n");
                sql.append("    AND PO.PART_ID = PI.PART_ID(+) \n");
                sql.append("    AND PO.SPARE_QTY > 0 --未满足品种数量 \n");

                if (!"".equals(planCode)) {
                    sql.append(" AND PL.PLAN_CODE LIKE '%").append(planCode).append("%'");
                }

                if (!"".equals(orderCode)) {
                    sql.append(" AND PO.ORDER_CODE LIKE '%").append(orderCode).append("%'");
                }

                if (!"".equals(partOldCode)) {
                    sql.append(" AND UPPER(PL.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                if (!"".equals(partCode)) {
                    sql.append(" AND UPPER(PL.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
                }

                if (!"".equals(beginTime)) {
                    sql.append(" AND TO_DATE(PO.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endTime)) {
                    sql.append(" AND TO_DATE(PO.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
                }
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }
}
