package com.infodms.dms.dao.parts.purchaseOrderManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
import com.infodms.dms.po.TtPartPoChkPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseOrderInDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PurchaseOrderInDao.class);

    private static final PurchaseOrderInDao dao = new PurchaseOrderInDao();

    private PurchaseOrderInDao() {

    }

    public static  final PurchaseOrderInDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public List getPartWareHouseList(AclUserBean logonUser) throws Exception {
        try {
            TtPartWarehouseDefinePO po = new TtPartWarehouseDefinePO();
            po.setState(Constant.STATUS_ENABLE);
            po.setStatus(1);
            if (logonUser.getDealerId() != null) {
                po.setOrgId(CommonUtils.parseLong(logonUser.getDealerId()));
            } else {
                po.setOrgId(logonUser.getOrgId());
            }

            List list = select(po);
            return list;
        } catch (Exception e) {
            throw e;
        }
    }


    public PageResult<Map<String, Object>> queryPartOrderInList(
            TtPartPoChkPO po, String beginTime, String endTime,
            String checkBeginTime, String checkEndTime, String checkName, String chkCode, String planerId, String whmanId,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT A.PO_ID, \n");
            sql.append("        A.CHECK_ID, \n");
            sql.append("        A.ORDER_CODE, \n");
            sql.append("        A.CHECK_CODE, \n");
            sql.append("        A.CREATE_DATE, \n");
            sql.append("        A.PART_TYPE, \n");
            sql.append("        A.PART_ID, \n");
            sql.append("        A.PART_OLDCODE, \n");
            sql.append("        A.PART_CNAME, \n");
            sql.append("        A.PART_CODE, \n");
            sql.append("        PD.PART_ENAME,\n");
            sql.append("        A.BUY_QTY, \n");
            sql.append("        A.CHECK_QTY, \n");
            sql.append("        A.VENDER_ID, \n");
            sql.append("        A.VENDER_NAME, \n");
            sql.append("        A.MAKER_ID, \n");
            sql.append("        D.MAKER_NAME, \n");
            sql.append("        A.Wh_Id, \n");
            sql.append("        A.WH_NAME, \n");
            sql.append("        A.IN_QTY, \n");
            sql.append("        (SELECT V.NORMAL_QTY \n");
            sql.append("           FROM VW_PART_STOCK V \n");
            sql.append("          WHERE A.PART_ID = V.PART_ID \n");
            sql.append("            AND A.WH_ID = V.WH_ID) NORMAL_QTY, \n");
            //sql.append("        B.NAME CHECK_NAME, \n");
            sql.append("        (SELECT B.NAME FROM TC_USER B WHERE B.USER_ID = A.CHECK_BY) CHECK_NAME,\n");
            sql.append("        C.BUYER, \n");
            sql.append("        A.CHECK_DATE, \n");
            sql.append("        (A.CHECK_QTY - A.IN_QTY) RELIN_QTY, \n");
            sql.append("        A.REMARK, \n");
            sql.append("        A.STATE, \n");
            sql.append("        PD.PKG_SIZE, \n");

            sql.append("CASE\n");
            sql.append("       WHEN 1 = 1 THEN\n");
            sql.append("        (SELECT 1\n");
            sql.append("           FROM VW_PART_STOCK V, TT_PART_PLAN_DEFINE PD\n");
            sql.append("          WHERE V.PART_ID = PD.PART_ID\n");
            sql.append("            AND V.WH_ID = PD.WH_ID\n");
            sql.append("            AND A.PART_ID = V.PART_ID\n");
            sql.append("            AND A.WH_ID = V.WH_ID\n");
            sql.append("            AND PD.PLAN_TYPE = 1\n");
            sql.append("            AND V.NORMAL_QTY < (PD.SAFETY_STOCK + PD.AVG_QTY * 30 / 2))\n");
            sql.append("     END AS FLAG,\n");

            sql.append("        SUBSTR(WD.WH_CODE, 2, 2) || TO_CHAR(SYSDATE, 'yymm') ||to_char(SUBSTR(VD.VENDER_CODE, 2, 5),'fm00000') || TO_CHAR(PD.BARCODE_ID, 'fm00000')||PB.BATCH_NO barCode \n");
            sql.append("   FROM TT_PART_PO_CHK       A, \n");
            sql.append("        TT_PART_DEFINE       PD,\n");
            // sql.append("        TC_USER              B, \n");
            sql.append("        TT_PART_OEM_PO       C, \n");
            sql.append("        TT_PART_MAKER_DEFINE D, \n");
            sql.append("        tt_part_warehouse_define WD, \n");
            sql.append("        tt_part_vender_define VD, \n");
            sql.append("        VW_PART_OEM_PO_BATCH  PB \n");
            //sql.append("  WHERE A.CHECK_BY = B.USER_ID \n");
            sql.append("    WHERE A.PO_ID = C.PO_ID \n");
            sql.append("    AND A.WH_ID = WD.WH_ID \n");
            sql.append("    AND A.vender_id = VD.vender_id \n");
            sql.append("    AND A.PART_ID = PD.PART_ID\n");
            sql.append("    AND pb.PART_ID = PD.PART_ID\n");
            sql.append("    AND A.MAKER_ID = D.MAKER_ID(+) \n");
            sql.append("    AND A.STATUS = 1 \n");

            if (!"".equals(po.getOrderCode())) {
                sql.append(" AND upper(A.ORDER_CODE) LIKE upper('%")
                        .append(po.getOrderCode()).append("%')\n");
            }

            if (!"".equals(chkCode)) {
                sql.append(" AND upper(A.CHECK_CODE) LIKE upper('%")
                        .append(chkCode).append("%')\n");
            }
            if (!"".equals(checkName)) {
                sql.append(" AND B.NAME LIKE '%")
                        .append(checkName).append("%'\n");
            }
            if (!"".equals(checkName)) {
                sql.append(" AND B.NAME LIKE '%")
                        .append(checkName).append("%'\n");
            }
            if (!"".equals(planerId)) {
                sql.append(" AND C.PLANER_ID=").append(new Long(planerId));
            }
            if (!"".equals(whmanId) && null != whmanId) {
                sql.append(" AND C.WHMAN_ID=").append(new Long(whmanId));
            }
            if (!"".equals(beginTime)) {
                sql.append(" AND to_date(A.CREATE_DATE)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND to_date(A.CREATE_DATE)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(checkBeginTime)) {
                sql.append(" AND to_date(A.CHECK_DATE)>=").append("to_date('").append(checkBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(checkEndTime)) {
                sql.append(" AND to_date(A.CHECK_DATE)<=").append("to_date('").append(checkEndTime).append("','yyyy-MM-dd')");
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
                sql.append(" AND upper(A.PART_OLDCODE) LIKE upper('%")
                        .append(po.getPartOldcode()).append("%')\n");
            }
            if (!po.getPartCname().equals("")) {
                sql.append(" AND A.PART_CNAME LIKE '%")
                        .append(po.getPartCname()).append("%'\n");
            }
            if (!po.getPartCode().equals("")) {
                sql.append(" AND upper(A.PART_CODE) LIKE upper('%")
                        .append(po.getPartCode()).append("%')\n");
            }
            if (po.getState() != null) {
                sql.append(" AND A.STATE=")
                        .append(po.getState());
            } else {
                sql.append(" AND A.STATE=")
                        .append(Constant.PART_PURCHASE_ORDERCIN_STATUS_01);
            }

            sql.append("  ORDER BY A.CREATE_DATE DESC, A.PART_OLDCODE");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public Map<String, Object> queryPartAndLocationInfo(Long partId, Long orgId, Long whId) throws Exception {

        try {
            StringBuilder sql = new StringBuilder("SELECT A.IS_DIRECT, A.BUYER_ID, B.LOC_ID, B.LOC_CODE,B.LOC_NAME FROM TT_PART_DEFINE A, TT_PART_LOACTION_DEFINE B");
            sql.append(" WHERE A.PART_ID(+) = B.PART_ID");
            sql.append(" AND B.ORG_ID =").append(orgId);
            sql.append("  AND B.WH_ID =").append(whId);
            sql.append(" AND B.PART_ID=").append(partId);
            sql.append(" AND B.STATE=").append(Constant.STATUS_ENABLE);
            sql.append(" AND B.STATUS=1");
            logger.info("--------sql=" + sql);
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public Map getVerByCheckId(Long checkId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.VER,T.CHECK_ID FROM TT_PART_PO_CHK T WHERE T.CHECK_ID=").append(checkId);
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public void update(TtPartPoChkPO po) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE TT_PART_PO_CHK T SET T.IN_QTY=?,T.SPARE_QTY=?,T.REMARK=?,T.STATE=? WHERE T.CHECK_ID=?");
            List params = new ArrayList();
            params.add(po.getInQty());
            params.add(po.getSpareQty());
            if (po.getRemark() != null) {
                params.add(po.getRemark());
            } else {
                params.add("");
            }
            params.add(po.getState());
            params.add(po.getCheckId());
            update(sql.toString(), params);
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateVer(TtPartPoChkPO po) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE TT_PART_PO_CHK T SET T.VER=? WHERE T.CHECK_ID=?");
            List params = new ArrayList();
            params.add(po.getVer());
            params.add(po.getCheckId());
            update(sql.toString(), params);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isLocked(Long partId, String strWhId, String locId, Long dealerId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT V.IS_LOCKED  FROM VW_PART_STOCK V WHERE V.ORG_ID =");
            sql.append(dealerId);
            sql.append(" AND V.PART_ID = ").append(partId);
            sql.append(" AND V.WH_ID = ").append(strWhId);
            if(!CommonUtils.isEmpty(locId)){
                sql.append(" AND V.LOC_ID = ").append(locId);
            }
            map = pageQueryMap(sql.toString(), null, getFunName());
            if (map != null && ((BigDecimal) map.get("IS_LOCKED")).intValue() == 1) {
                return true;
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }

    public List<Map<String, Object>> queryPurchaseOrderIn(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {

            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//采购订单号
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//验收单号
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//制单开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//制单结束时间
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房id
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件种类
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String checkBeginTime = CommonUtils.checkNull(request.getParamValue("checkBeginTime"));//验货开始时间
            String checkEndTime = CommonUtils.checkNull(request.getParamValue("checkEndTime"));//验货结束时间
            String checkName = CommonUtils.checkNull(request.getParamValue("CHECK_NAME"));//验货人员
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//件号
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//状态

            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT  A.PO_ID,A.CHECK_ID,A.ORDER_CODE,A.CHECK_CODE,A.CREATE_DATE,DECODE(A.PART_TYPE,")
                    .append(Constant.PART_BASE_PART_TYPES_SELF_MADE).append(",'自制件',")
                    .append(Constant.PART_BASE_PART_TYPES_PURCHASE).append(",'国产件',")
                    .append(Constant.PART_BASE_PART_TYPES_ENTRANCE).append(",'进口件')")
                    .append(" PART_TYPE,");
            sql.append("A.PART_OLDCODE,A.PART_CNAME,");
            sql.append("A.PART_CODE,A.BUY_QTY,A.CHECK_QTY, A.VENDER_ID,A.VENDER_NAME,A.MAKER_ID,D.MAKER_NAME,A.Wh_Id,A.WH_NAME,A.IN_QTY,B.NAME CHECK_NAME,C.BUYER,A.CHECK_DATE,(A.CHECK_QTY-A.IN_QTY) RELIN_QTY,A.REMARK,SP.SALE_PRICE3,(SP.SALE_PRICE3 * A.IN_QTY) IN_AMOUNT,DECODE(A.STATE,")
                    .append(Constant.PART_PURCHASE_ORDERCIN_STATUS_01).append(",'待入库',")
                    .append(Constant.PART_PURCHASE_ORDERCIN_STATUS_02).append(",'入库完成')")
                    .append(" STATE");
            sql.append(" FROM TT_PART_PO_CHK A,TC_USER B,TT_PART_OEM_PO C,TT_PART_MAKER_DEFINE D,TT_PART_SALES_PRICE  SP  WHERE A.CHECK_BY=B.USER_ID AND A.PO_ID=C.PO_ID AND A.MAKER_ID=D.MAKER_ID(+)");
            sql.append(" AND A.STATUS=1 AND A.PART_ID = SP.PART_ID");

            if (!"".equals(orderCode)) {
                sql.append(" AND A.ORDER_CODE LIKE '%")
                        .append(orderCode).append("%'\n");
            }

            if (!"".equals(chkCode)) {
                sql.append(" AND A.CHECK_CODE LIKE '%")
                        .append(chkCode).append("%'\n");
            }

            if (!"".equals(checkName)) {
                sql.append(" AND B.NAME LIKE '%")
                        .append(checkName).append("%'\n");
            }

            if (!"".equals(beginTime)) {
                sql.append(" AND to_date(A.CREATE_DATE)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND to_date(A.CREATE_DATE)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(checkBeginTime)) {
                sql.append(" AND to_date(A.CHECK_DATE)>=").append("to_date('").append(checkBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(checkEndTime)) {
                sql.append(" AND to_date(A.CHECK_DATE)<=").append("to_date('").append(checkEndTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(whId) && !"0".equals(whId)) {
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
            if (!"".equals(state) && !"0".equals(state)) {
                sql.append(" AND A.STATE=")
                        .append(state);
            }

            sql.append(" ORDER BY A.CREATE_DATE ASC");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;
    }

    public Map<String, Object> getInfoByChkCodeAndWhIdAndPartId(
            String checkCode, Long whId, Long partId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.IN_ID,T.IN_CODE,T.IN_QTY,T.BUY_PRICE,T.IN_AMOUNT,T.BUY_PRICE_NOTAX,T.IN_AMOUNT_NOTAX FROM TT_PART_PO_IN T WHERE T.CHECK_CODE=");
            sql.append("'" + checkCode + "'");
            sql.append(" AND T.WH_ID= ").append(whId);
            sql.append(" AND T.PART_ID=").append(partId);
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateInDateAndInby(String[] checkCodes) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE TT_PART_OEM_PO T SET T.IN_DATE=SYSDATE,T.IN_BY=").append(logonUser.getUserId());
            sql.append(" WHERE T.CHK_CODE in(");
            for (int i = 0; i < checkCodes.length; i++) {
                sql.append("'" + checkCodes[i]).append("',");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")");
            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateRemark1(String[] chkCodes, String remark1, String partId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE TT_PART_OEM_PO T SET T.REMARK1='").append(remark1).append("'");
            sql.append(" WHERE T.CHK_CODE in(");
            for (int i = 0; i < chkCodes.length; i++) {
                sql.append("'" + chkCodes[i]).append("',");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")");
            if (!"".equals(partId)) {
                sql.append(" and t.part_id = ").append(partId);
            }
            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<String, Object> getNormalQty(String partId, String whId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT V.NORMAL_QTY \n");
            sql.append("   FROM VW_PART_STOCK V \n");
            sql.append("  WHERE V.PART_ID = ").append(partId);
            sql.append("    AND V.WH_ID = ").append(whId);

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    //zhumingwei 2013-09-13 配件条码打印(为了取得拼串出来的代码)
    public List<Map<String, Object>> getDefinePrint(String partId, String venderId, String WH_ID) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT  pd.part_oldcode||'    '||to_char(SYSDATE,'yymmdd') part_oldcode,\n" +
                "       nvl(pd.part_cname,'') part_cname,\n" +
                "       nvl(PD.PART_ENAME,'') PART_ENAME,\n" +
                "       pd.part_code,\n" +
                "       SUBSTR(WD.WH_CODE, 2, 2) || TO_CHAR(SYSDATE, 'yymm') ||\n" +
                "       to_char(SUBSTR(VD.VENDER_CODE, 2, 5), 'fm00000') ||\n" +
                "       TO_CHAR(PD.BARCODE_ID, 'fm00000') || PB.BATCH_NO barCode\n" +
                "  FROM TT_PART_DEFINE           PD,\n" +
                "       tt_part_warehouse_define WD,\n" +
                "       tt_part_vender_define    VD,\n" +
                "       VW_PART_OEM_PO_BATCH     PB\n" +
                " WHERE WD.WH_ID = '" + WH_ID + "'\n" +
                "   AND VD.vender_id = '" + venderId + "'\n" +
                "   and pd.part_id = '" + partId + "'\n" +
                "   AND pb.PART_ID = PD.PART_ID");

        List<Map<String, Object>> wareHoustList = this.pageQuery(sql.toString(), null, this.getFunName());
        return wareHoustList;
    }

    //zhumingwei 2013-11-19 条码打印新改（填写配件编码回车后自动带出相关信息）
    public List<Map<String, Object>> getDefine(String PART_OLDCODE, String WH_ID) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT T.PART_ID, T.PART_CODE, T.PART_OLDCODE, T.PART_CNAME,T.OEM_MIN_PKG,T.MIN_PKG,b.NORMAL_QTY,b.loc_code\n");
        sql.append("  FROM TT_PART_DEFINE T,vw_part_stock b\n");
        sql.append(" WHERE 1 = 1 and b.PART_ID(+)=t.part_id\n");
        sql.append(" AND T.STATE = '" + Constant.STATUS_ENABLE + "' and b.WH_ID =" + WH_ID + " and T.PART_OLDCODE ='" + PART_OLDCODE + "'\n");

        List<Map<String, Object>> wareHoustList = this.pageQuery(sql.toString(), null, this.getFunName());
        return wareHoustList;
    }

    public PageResult<Map<String, Object>> queryPurOrderInList(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String inCode = CommonUtils.checkNull(request.getParamValue("IN_CODE"));
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));
            String returnCode = CommonUtils.checkNull(request.getParamValue("RETURN_CODE"));
            String modelName = CommonUtils.checkNull(request.getParamValue("MODEL_NAME"));
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T1.IN_CODE, \n");
            sql.append("        T1.CHECK_CODE, \n");
            sql.append("        T3.RETURN_CODE, \n");
            sql.append("        T1.VENDER_CODE, \n");
            sql.append("        T1.VENDER_NAME, \n");
            sql.append("        T1.PART_OLDCODE, \n");
            sql.append("        T1.PART_CNAME, \n");
            sql.append("        T1.IN_QTY, \n");
            sql.append("        TO_CHAR(T1.BUY_PRICE_NOTAX,'FM999,999,999,999,990.00') BUY_PRICE_NOTAX, \n");
            sql.append("        TO_CHAR(T1.IN_AMOUNT_NOTAX,'FM999,999,999,999,990.00') IN_AMOUNT_NOTAX, \n");
            sql.append("        T4.MODEL_NAME, \n");
            sql.append("        T1.PART_TYPE, \n");
            sql.append("        T1.REMARK \n");
            sql.append("   FROM TT_PART_PO_IN           T1, \n");
            sql.append("        TT_PART_OEM_RETURN_DTL  T2, \n");
            sql.append("        TT_PART_OEM_RETURN_MAIN T3, \n");
            sql.append("        TT_PART_DEFINE          T4 \n");
            sql.append("  WHERE T1.IN_ID = T2.IN_ID(+) \n");
            sql.append("    AND T2.RETURN_ID = T3.RETURN_ID(+) \n");
            sql.append("    AND T1.PART_ID = T4.PART_ID \n");

            if (!"".equals(inCode)) {
                sql.append(" AND T1.IN_CODE LIKE '%").append(inCode).append("%'");
            }
            if (!"".equals(chkCode)) {
                sql.append(" AND T1.CHECK_CODE LIKE '%").append(chkCode).append("%'");
            }
            if (!"".equals(returnCode)) {
                sql.append(" AND T3.RETURN_CODE LIKE '%").append(returnCode).append("%'");
            }
            if (!"".equals(modelName)) {
                sql.append(" AND T4.MODEL_NAME LIKE '%").append(modelName).append("%'");
            }
            if (!"".equals(partCname)) {
                sql.append(" AND T1.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T1.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T1.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partType)) {
                sql.append(" AND T1.PART_TYPE=").append(partType);
            }
            if (!"".equals(venderId)) {
                sql.append(" AND T1.VENDER_ID=").append(venderId);
            }

            sql.append(" ORDER BY T1.IN_CODE");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    //zhumingwei 2013-11-15 包装材料出入库明细报表
    public PageResult<Map<String, Object>> queryPckMaterialsDetailList(RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String Specification = CommonUtils.checkNull(request.getParamValue("Specification"));
            String c_name = CommonUtils.checkNull(request.getParamValue("c_name"));
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT PP.PACK_SPEC,\n");
            sql.append("       PP.PACK_NAME,\n");
            sql.append("       DECODE(PR.FLAG, 1, '入库', '出库') FLAG,\n");
            sql.append("       PR.QTY,\n");
            sql.append("       PR.Remark,\n");
            sql.append("       TU.NAME,\n");
            sql.append("       PR.CREATE_DATE\n");
            sql.append("  FROM TT_PART_PACKAGE PP, TT_PART_PACKAGE_RECORD PR, TC_USER TU\n");
            sql.append(" WHERE PP.PACK_ID = PR.PACK_ID\n");
            sql.append("   AND PR.CREATE_BY = TU.USER_ID\n");
            sql.append("   AND PP.STATUS = 10011001\n");
            sql.append("   AND PP.STATUS = 10011001\n");

            if (!"".equals(Specification)) {
                sql.append(" AND Pp.PACK_SPEC LIKE '%").append(Specification).append("%'");
            }
            if (!"".equals(c_name)) {
                sql.append(" AND Pp.PACK_NAME LIKE '%").append(c_name).append("%'");
            }
            if (!"".equals(SCREATE_DATE)) {
                sql.append(" and pr.CREATE_DATE>= to_date('").append(SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append(" and pr.CREATE_DATE<= to_date('").append(ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
            }

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    //zhumingwei 2013-11-15 库存分析报表(本部)
    public PageResult<Map<String, Object>> queryStockAnalysisList(RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String part_oldcode = CommonUtils.checkNull(request.getParamValue("part_oldcode"));
            String part_name = CommonUtils.checkNull(request.getParamValue("part_name"));
            String part_code = CommonUtils.checkNull(request.getParamValue("part_code"));
            String storehouse = CommonUtils.checkNull(request.getParamValue("storehouse"));
            String models = CommonUtils.checkNull(request.getParamValue("models"));
            String part_type = CommonUtils.checkNull(request.getParamValue("part_type"));
            String fDay = CommonUtils.checkNull(request.getParamValue("fDay"));
            String tDay = CommonUtils.checkNull(request.getParamValue("tDay"));

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT *\n");
            sql.append("  FROM VW_PART_INVENTORY_AGING T\n");
            sql.append("\tWHERE 1=1");

            if (!"".equals(part_oldcode)) {
                sql.append(" AND T.PART_OLDCODE LIKE '%").append(part_oldcode).append("%'");
            }
            if (!"".equals(part_name)) {
                sql.append(" AND T.PART_CNAME LIKE '%").append(part_name).append("%'");
            }
            if (!"".equals(part_code)) {
                sql.append(" AND T.PART_CODE LIKE '%").append(part_code).append("%'");
            }
            if (!"".equals(fDay) && null != fDay) {
                sql.append(" AND T.AGING >= " + fDay + "");
            }
            if (!"".equals(tDay) && null != tDay) {
                sql.append(" AND T.AGING <= " + tDay + "");
            }
            if (!"".equals(models)) {
                sql.append(" AND T.MODEL_NAME like '%" + models + "%'");
            }
            if (!"".equals(part_type)) {
                sql.append(" AND T.PART_TYPE = " + part_type + "");
            }

            sql.append("ORDER BY T.AGING DESC");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    //zhumingwei 2014-08-20   BO订单发出数
    public PageResult<Map<String, Object>> queryboOrderIssueList(RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));

            StringBuffer sql = new StringBuffer("");

            sql.append("WITH BO_DTL AS\n");
            sql.append(" (SELECT BD.BO_ID,\n");
            sql.append("         SUM(BD.BO_QTY) BO_QTY,\n");
            sql.append("         SUM(BD.BO_QTY) - SUM(BD.BO_ODDQTY) CL_QTY\n");
            sql.append("    FROM TT_PART_BO_DTL BD\n");
            sql.append("   GROUP BY BD.BO_ID)\n");
            sql.append("SELECT NVL((SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = OM.ORDER_TYPE),\n");
            sql.append("           '合计') ORDER_TYPE, --订单类型\n");
            sql.append("       --DECODE(BM.BO_TYPE, 1, '一般BO', '现场BO') BO_TYPE, --BO类型\n");
            sql.append("       COUNT(BM.BO_ID) BOCNT, --BO总数\n");
            sql.append("       SUM(DECODE(BM.STATE, 92141003, 1, 0)) BOHDCNT, --处理数量\n");
            sql.append("       TO_CHAR(SUM(DECODE(BM.STATE, 92141003, 1, 0)) / COUNT(BM.BO_ID) * 100,\n");
            sql.append("               '999.99') || '%' BORATE, --百分比\n");
            sql.append("       SUM(BD.BO_QTY) BO_QTY,\n");
            sql.append("       SUM(BD.CL_QTY) CL_QTY,\n");
            sql.append("       TO_CHAR(SUM(BD.CL_QTY) / SUM(BD.BO_QTY) * 100, '999.99') || '%' BOQTYRATE\n");
            sql.append("  FROM TT_PART_BO_MAIN           BM,\n");
            sql.append("       VW_PART_BO_ORDER_RELATION BR,\n");
            sql.append("       TT_PART_DLR_ORDER_MAIN    OM,\n");
            sql.append("       BO_DTL                    BD\n");
            sql.append(" WHERE BM.BO_ID = BR.BO_ID\n");
            sql.append("   AND BM.BO_ID = BD.BO_ID\n");
            sql.append("   AND BR.ORDER_ID = OM.ORDER_ID\n");
            if (!"".equals(SCREATE_DATE) && null != SCREATE_DATE) {
                sql.append("   AND TRUNC(BM.CREATE_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-MM-dd') --BO生成开始日期\n");
            }
            if (!"".equals(ECREATE_DATE) && null != ECREATE_DATE) {
                sql.append("\t AND TRUNC(BM.CREATE_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-MM-dd') --BO生成结束日期\n");
            }
            sql.append("\t AND OM.ORDER_TYPE IN (92151001, 92151002)\n");
            sql.append(" GROUP BY ROLLUP(OM.ORDER_TYPE)");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> queryOrderIssuedTimelyList(RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
            String flag = CommonUtils.checkNull(request.getParamValue("Query"));
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));
            String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));

            StringBuffer sql = new StringBuffer("");
            if ("1".equals(flag)) {
                sql.append("SELECT (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = tt.ORDER_TYPE) ORDER_TYPE, --订单类型\n");
                sql.append("       COUNT(tt.SO_ID) SOCNT, --总数量\n");
                sql.append("       --STANDARD_DATE,--标准出发时间\n");
                sql.append("       --REAL_DATE,--实际出发时间\n");
                sql.append("       SUM(DECODE(SIGN(STANDARD_DATE - REAL_DATE), 1, 1, 0)) BETIMESCNT, --及时发出数量\n");
                sql.append("       TO_CHAR(SUM(DECODE(SIGN(STANDARD_DATE - REAL_DATE), 1, 1, 0)) /\n");
                sql.append("               COUNT(tt.SO_ID) * 100,\n");
                sql.append("               '999.99') || '%' SMRA --及时率\n");
                sql.append("  FROM vw_part_trans_timely tt\n");
                sql.append("\tWHERE 1=1\n");
                if (!"".equals(SCREATE_DATE)) {
                    sql.append("\t AND TRUNC(tt.SO_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd') --订单审核通过开始日期\n");
                }
                if (!"".equals(ECREATE_DATE)) {
                    sql.append("   AND TRUNC(tt.SO_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd') --订单审核通过结束日期\n");
                }
                sql.append("GROUP BY tt.ORDER_TYPE");
            } else {
                sql.append("SELECT (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TT.ORDER_TYPE) ORDER_TYPE, --订单类型\n");
                sql.append("       TD.DEALER_CODE,\n");
                sql.append("       TD.DEALER_NAME,\n");
                sql.append("       TT.ORDER_CODE,\n");
                sql.append("       TT.SO_CODE,\n");
                sql.append("       TT.SO_DATE,\n");
                sql.append("       TT.TRNAS_DATE,\n");
                sql.append("       DECODE(SIGN((TT.STANDARD_DATE - TT.REAL_DATE)),\n");
                sql.append("              1,\n");
                sql.append("              '及时',\n");
                sql.append("              -1,\n");
                sql.append("              '不及时',\n");
                sql.append("              '') TIMELY_DESC\n");
                sql.append("  FROM VW_PART_TRANS_TIMELY TT, TM_DEALER TD\n");
                sql.append(" WHERE 1 = 1\n");
                sql.append("   AND TT.DEALER_ID = TD.DEALER_ID");
                if (!"".equals(SCREATE_DATE)) {
                    sql.append("\t AND TRUNC(tt.SO_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd') --订单审核通过开始日期\n");
                }
                if (!"".equals(ECREATE_DATE)) {
                    sql.append("   AND TRUNC(tt.SO_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd') --订单审核通过结束日期\n");
                }
                if (!"".equals(orderCode) && null != orderCode) {
                    sql.append("   AND  tt.ORDER_CODE like '%" + orderCode.toUpperCase() + "%'");
                }
                if (!"".equals(soCode) && null != soCode) {
                    sql.append("   AND  tt.SO_CODE like '%" + soCode.toUpperCase() + "%'");
                }
                if (!"".equals(dealerName) && null != dealerName) {
                    sql.append("   AND  TD.DEALER_NAME like '%" + dealerName.toUpperCase() + "%'");
                }

                sql.append(" ORDER BY TT.SO_DATE");

            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> queryDealerStorageTimelyList(RequestWrapper request, Integer curPage, Integer pageSize, AclUserBean logonUser) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));

            String dealer_code = CommonUtils.checkNull(request.getParamValue("dealer_code"));
            String dealer_name = CommonUtils.checkNull(request.getParamValue("dealer_name"));
            String flag = CommonUtils.checkNull(request.getParamValue("Query"));

            StringBuffer sql = new StringBuffer("");
            if ("1".equals(flag)) {
                sql.append("SELECT TS.DEALER_CODE,--服务商编码\n");
                sql.append("       TS.DEALER_NAME,--服务商名称\n");
                sql.append("       COUNT(TS.TRPLAN_ID) TRCNT,--发运数量\n");
                sql.append("       SUM(TS.DLR) DLRCNT,--及时入库数量\n");
                sql.append("       TO_CHAR(SUM(TS.DLR) / COUNT(TS.TRPLAN_ID) * 100, '999.99') || '%' WLRATE --及时率\n");
                sql.append("       FROM VW_PART_RP_TRANS TS\n");
                sql.append("       WHERE 1 = 1\n");

                if (!"".equals(SCREATE_DATE)) {
                    sql.append("              AND TRUNC(TS.CREATE_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd') --发运日期\n");
                }
                if (!"".equals(ECREATE_DATE)) {
                    sql.append("              AND TRUNC(TS.CREATE_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd') --发运日期\n");
                }
                if (!"".equals(dealer_code)) {
                    sql.append("              AND TS.DEALER_CODE LIKE '%" + dealer_code + "%'\n");
                }
                if (!"".equals(dealer_name)) {
                    sql.append("              AND TS.DEALER_NAME LIKE '%" + dealer_name + "%'\n");
                }
                //add 大区服务经理区域限制 15-01-22
                if (this.getPoseRoleId(logonUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
                    sql.append(CommonUtils.getOrgDealerLimitSqlByPose("TS", logonUser));
                }
                sql.append("              GROUP BY TS.DEALER_CODE, TS.DEALER_NAME\n");
                sql.append("              ORDER BY TS.DEALER_CODE\n");
            } else {
                sql.append("SELECT TS.*\n");
                sql.append("  FROM VW_PART_RP_TRANS TS\n");
                sql.append(" WHERE 1 = 1\n");
                if (!"".equals(SCREATE_DATE)) {
                    sql.append("              AND TRUNC(TS.CREATE_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd') --发运日期\n");
                }
                if (!"".equals(ECREATE_DATE)) {
                    sql.append("              AND TRUNC(TS.CREATE_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd') --发运日期\n");
                }
                if (!"".equals(dealer_code)) {
                    sql.append("              AND TS.DEALER_CODE LIKE '%" + dealer_code + "%'\n");
                }
                if (!"".equals(dealer_name)) {
                    sql.append("              AND TS.DEALER_NAME LIKE '%" + dealer_name + "%'\n");
                }

                //add 大区服务经理区域限制 15-01-22
                if (this.getPoseRoleId(logonUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
                    sql.append(CommonUtils.getOrgDealerLimitSqlByPose("TS", logonUser));
                }

                sql.append(" ORDER BY TS.DEALER_CODE");

            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> queryOrderMeetList(RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
            String flag = CommonUtils.checkNull(request.getParamValue("Query"));//查询标志
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));//订单号
            String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));//流水号
            String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldCode"));//流水号

            StringBuffer sql = new StringBuffer("");
            if ("1".equals(flag)) {
                sql.append("WITH ORDER_INFO AS\n");
                sql.append(" (SELECT OM.ORDER_TYPE,\n");
                sql.append("         COUNT(OM.PART_ID) PARTCNT,\n");
                sql.append("         SUM(OM.BUY_QTY) QTYSUM,\n");
//                sql.append("         SUM(OM.OUTSTOCK_QTY) FY_QTYSUM,\n");
                sql.append("         decode(SIGN(SUM(OM.OUTSTOCK_QTY)-SUM(OM.BUY_QTY)),1,SUM(OM.BUY_QTY),SUM(OM.OUTSTOCK_QTY)) FY_QTYSUM,\n");
                sql.append("         SUM(DECODE(SIGN(NVL(OM.OUTSTOCK_QTY, 0)), 1, 1, 0)) FY_PARTCNT\n");
                sql.append("    FROM VW_PART_DLR_ORDER_TRANS OM\n");
                sql.append("   WHERE OM.SELLER_ID = 2010010100070674\n");
                if (!"".equals(SCREATE_DATE) && null != SCREATE_DATE) {
                    sql.append("     AND TRUNC(OM.SUBMIT_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd') --订单提交时间\n");
                }
                if (!"".equals(ECREATE_DATE) && null != ECREATE_DATE) {
                    sql.append("     AND TRUNC(OM.SUBMIT_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd') --订单提交时间\n");
                }
                sql.append("  --AND OM.ORDER_ID = 2014103104889621\n");
                sql.append("   GROUP BY OM.ORDER_TYPE) ， --订货品种和明细\n");
                sql.append("OUTSTOCK_ORDER_INFO_FIRST AS\n");
                sql.append(" (SELECT SM.ORDER_TYPE, COUNT(SD.PART_ID) PARTCNT\n");
                sql.append("    FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM\n");
                sql.append("   WHERE SD.SO_ID = SM.SO_ID\n");
                sql.append("     AND EXISTS\n");
                sql.append("   (SELECT 1\n");
                sql.append("            FROM VW_PART_DLR_ORDER_TRANS OM\n");
                sql.append("           WHERE OM.ORDER_ID = SM.ORDER_ID\n");
                if (!"".equals(SCREATE_DATE) && null != SCREATE_DATE) {
                    sql.append("             AND TRUNC(OM.SUBMIT_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd') --订单提交时间\n");
                }
                if (!"".equals(ECREATE_DATE) && null != ECREATE_DATE) {
                    sql.append("             AND TRUNC(OM.SUBMIT_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd') --订单提交时间\n");
                }
                sql.append("          )\n");
                sql.append("   GROUP BY SM.ORDER_TYPE)\n");
                sql.append("SELECT NVL((SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = OI.ORDER_TYPE),\n");
                sql.append("           '合计') ORDER_TYPE, --订单类型\n");
                sql.append("       SUM(OI.PARTCNT) DH_PARTCNT, --订货品种\n");
                sql.append("       SUM(OI.FY_PARTCNT) FY_PARTCNT, --满足品种\n");
                sql.append("       TO_CHAR(SUM(OOIF.PARTCNT) / SUM(OI.PARTCNT) * 100, '999.99') || '%' PARTRATE, --品种满足率\n");
                sql.append("       SUM(OI.QTYSUM) DH_QTYSUM, --订货数量\n");
                sql.append("       SUM(OI.FY_QTYSUM) FY_QTYSUM, --满足数量\n");
                sql.append("       TO_CHAR(SUM(OI.FY_QTYSUM) / SUM(OI.QTYSUM) * 100, '999.99') || '%' QTYRATE --数量满足率\n");
                sql.append("  FROM ORDER_INFO OI, OUTSTOCK_ORDER_INFO_FIRST OOIF\n");
                sql.append(" WHERE OI.ORDER_TYPE = OOIF.ORDER_TYPE(+)\n");
//                sql.append("   AND OI.ORDER_TYPE IN (92151001, 92151002)\n");
                sql.append(" GROUP BY ROLLUP(OI.ORDER_TYPE)");

            } else {
                sql.append("SELECT TC.CODE_DESC ORDER_TYPE1, --订单类型\n");
                sql.append("       OM.*\n");
                sql.append("  FROM VW_PART_DLR_ORDER_TRANS OM, TC_CODE TC\n");
                sql.append(" WHERE 1 = 1\n");
                sql.append("   AND OM.ORDER_TYPE = TC.CODE_ID\n");
//                sql.append("   AND OM.ORDER_TYPE IN (92151001, 92151002)\n");
                sql.append("   AND OM.SELLER_ID = 2010010100070674\n");
                sql.append("   AND OM.BUY_QTY <> NVL(OM.OUTSTOCK_QTY, 0)\n");
                if (!"".equals(SCREATE_DATE) && null != SCREATE_DATE) {
                    sql.append("   AND TRUNC(OM.SUBMIT_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd') --订单提交时间\n");
                }
                if (!"".equals(ECREATE_DATE) && null != ECREATE_DATE) {
                    sql.append("   AND TRUNC(OM.SUBMIT_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd') --订单提交时间\n");
                }
                if (!"".equals(orderCode) && null != orderCode) {
                    sql.append(" AND OM.ORDER_CODE like '%" + orderCode + "%'\n");
                }
                if (!"".equals(soCode) && null != soCode) {
                    sql.append(" AND OM.SO_CODE like '%" + soCode + "%'\n");
                }
                if (!"".equals(partOldCode) && null != partOldCode) {
                    sql.append(" AND OM.PART_OLDCODE like '%" + partOldCode.toUpperCase() + "%'\n");
                }

            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> queryDealerOrderSituationList(RequestWrapper request, Integer curPage, Integer pageSize, String flag) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
            String dealer_code = CommonUtils.checkNull(request.getParamValue("dealer_code"));
            String dealer_name = CommonUtils.checkNull(request.getParamValue("dealer_name"));

            StringBuffer sql = new StringBuffer("");

            sql.append("WITH PART_OUTSTOCK AS\n");
            sql.append(" (SELECT OM.DEALER_ID,\n");
            sql.append("         COUNT(OD.OUTLINE_ID) PARTCNT，SUM(OD.OUTSTOCK_QTY) PARTQTY,\n");
            sql.append("         SUM(DECODE(OM.ORDER_TYPE, 92151001, OD.SALE_AMOUNT, 0)) jj_AMOUNT, --常规订单出库金额\n");
            sql.append("         SUM(DECODE(OM.ORDER_TYPE, 92151002, OD.SALE_AMOUNT, 0)) CG_AMOUNT, --紧急订单出库金额\n");
            sql.append("         SUM(DECODE(OM.ORDER_TYPE, 92151003, OD.SALE_AMOUNT, 0)) TS_AMOUNT, --特殊订单出库金额\n");
            sql.append("         SUM(DECODE(OM.ORDER_TYPE, 92151010, OD.SALE_AMOUNT, 0)) QH_AMOUNT, --切换订单出库金额\n");
            sql.append("         SUM(DECODE(OM.ORDER_TYPE, 92151010, 0, OD.SALE_AMOUNT)) AMOUNT --不含切换订单出库金额\n");
//            sql.append("         SUM(OD.SALE_AMOUNT) AMOUNT\n");
            sql.append("    FROM TT_PART_OUTSTOCK_DTL OD, TT_PART_OUTSTOCK_MAIN OM\n");
            sql.append("   WHERE OD.OUT_ID = OM.OUT_ID\n");
            sql.append("     AND OM.ORDER_TYPE IN (92151001, 92151002, 92151003, 92151010) --紧急+常规+特殊+切换\n");
            sql.append("     AND OM.SELLER_ID = 2010010100070674\n");
            if (!"".equals(SCREATE_DATE) && null != SCREATE_DATE) {
                sql.append("     AND TRUNC(OM.CREATE_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd')\n");
            }
            if (!"".equals(ECREATE_DATE) && null != ECREATE_DATE) {
                sql.append("     AND TRUNC(OM.CREATE_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd')\n");
            }
            sql.append("   GROUP BY OM.DEALER_ID), --统计出库信息\n");
            sql.append("PART_ORDER_JC AS\n");
            sql.append(" (SELECT A.DEALER_ID，SUM(ORDER_AMOUNT) ORDER_AMOUNT,\n");
            sql.append("         SUM(JJ_AMOUNT) JJ_AMOUNT,\n");
            sql.append("         SUM(CG_AMOUNT) CG_AMOUNT,\n");
            sql.append("         SUM(TS_AMOUNT) TS_AMOUNT,\n");
            sql.append("         SUM(QH_AMOUNT) QH_AMOUNT\n");
            sql.append("    FROM (SELECT OM.DEALER_ID,\n");
            sql.append("                 SUM(OM.ORDER_AMOUNT) ORDER_AMOUNT, --订单总金额\n");
            sql.append("                 SUM(DECODE(OM.ORDER_TYPE, 92151001, OM.ORDER_AMOUNT，0)) JJ_AMOUNT， --紧急订单金额\n");
            sql.append("                 SUM(DECODE(OM.ORDER_TYPE, 92151002, OM.ORDER_AMOUNT，0)) CG_AMOUNT, --常规订单金额\n");
            sql.append("                 SUM(DECODE(OM.ORDER_TYPE, 92151003, OM.ORDER_AMOUNT，0)) TS_AMOUNT, --特殊订单金额\n");
            sql.append("                 SUM(DECODE(OM.ORDER_TYPE, 92151010, OM.ORDER_AMOUNT，0)) QH_AMOUNT --切换订单金额\n");
            sql.append("            FROM VW_PART_DLR_ORDER_TRANS OM\n");
            sql.append("           WHERE OM.ORDER_TYPE IN (92151001, 92151002, 92151003, 92151010)\n");
//            sql.append("             AND OM.STATE IN (92161002, 92161003, 92161013, 92161014)\n");
            sql.append("             AND OM.SELLER_ID = 2010010100070674\n");
            if (!"".equals(SCREATE_DATE) && null != SCREATE_DATE) {
                sql.append("             AND TRUNC(OM.SUBMIT_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd') --订单提报日期\n");
            }
            if (!"".equals(ECREATE_DATE) && null != ECREATE_DATE) {
                sql.append("             AND TRUNC(OM.SUBMIT_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd') --订单提报日期\n");
            }
            sql.append("           GROUP BY OM.DEALER_ID) A\n");
            sql.append("   GROUP BY A.DEALER_ID), --统计有效订单信息\n");
            sql.append("PART_BO AS\n");
            sql.append(" (SELECT OM.DEALER_ID, SUM(BD.BO_ODDQTY * NVL(BD.BUY_PRICE, 0)) BO_AMOUNT\n");
            sql.append("    FROM TT_PART_BO_DTL            BD,\n");
            sql.append("         TT_PART_BO_MAIN           BM,\n");
            sql.append("         TT_PART_DLR_ORDER_MAIN    OM,\n");
            sql.append("         VW_PART_BO_ORDER_RELATION BR\n");
            sql.append("   WHERE BD.BO_ID = BM.BO_ID\n");
            sql.append("     AND BM.ORDER_ID = OM.ORDER_ID\n");
            sql.append("     AND BD.BO_ID = BR.BO_ID\n");
            sql.append("     AND BR.ORDER_ID = OM.ORDER_ID\n");
            sql.append("     AND OM.SELLER_ID = 2010010100070674\n");
            if (!"".equals(SCREATE_DATE) && null != SCREATE_DATE) {
                sql.append("     AND TRUNC(BM.CREATE_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd')\n");
            }
            if (!"".equals(ECREATE_DATE) && null != ECREATE_DATE) {
                sql.append("     AND TRUNC(BM.CREATE_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd')\n");
            }
            sql.append("   GROUP BY OM.DEALER_ID), --统计有效BO金额\n");
            sql.append("DLR_RETURN AS\n");
            sql.append(" (SELECT RM.DEALER_ID, SUM(RR.RETURN_QTY * RD.BUY_PRICE) RETURN_AMOUNT\n");
            sql.append("    FROM TT_PART_DLR_RETURN_DTL  RD,\n");
            sql.append("         TT_PART_DLR_RETURN_MAIN RM,\n");
            sql.append("         TT_PART_RETURN_RECORD   RR\n");
            sql.append("   WHERE RD.RETURN_ID = RM.RETURN_ID\n");
            sql.append("     AND RM.RETURN_ID = RR.RETURN_ID\n");
            sql.append("     AND RD.PART_ID = RR.PART_ID\n");
            sql.append("     AND RM.SELLER_ID = 2010010100070674\n");
            if (!"".equals(SCREATE_DATE) && null != SCREATE_DATE) {
                sql.append("     AND TRUNC(RR.CREATE_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd')\n");
            }
            if (!"".equals(ECREATE_DATE) && null != ECREATE_DATE) {
                sql.append("     AND TRUNC(RR.CREATE_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd')\n");
            }
            sql.append("   GROUP BY RM.DEALER_ID) --统计有效退货\n");
            if ("1".equals(flag)) {
                sql.append("SELECT TM.DEALER_CODE, --服务商编码\n");
                sql.append("        TM.DEALER_NAME, --服务商名称\n");
                sql.append("        NVL(OT.PARTCNT, 0) PARTCNT, --出库品种\n");
                sql.append("        NVL(OT.PARTQTY, 0) PARTQTY, --出库数量\n");
                sql.append("        NVL(OD.ORDER_AMOUNT, 0) ORDER_AMOUNT, --订货金额\n");
                sql.append("        NVL(BO.BO_AMOUNT, 0) BO_AMOUNT, --缺件金额\n");
                sql.append("        NVL(RE.RETURN_AMOUNT, 0) RETURN_AMOUNT, --退货金额\n");
                sql.append("        NVL(OT.AMOUNT, 0) AMOUNT, --实际销售金额\n");
                sql.append("        NVL(ot.QH_AMOUNT, 0） QH_AMOUNT, --切换件金额\n");
                sql.append("        NVL(ot.TS_AMOUNT, 0) TS_AMOUNT --特殊定件金额\n");
            } else {
                sql.append("SELECT  \n");
                sql.append("        SUM(NVL(OT.PARTCNT, 0)) PARTCNT, --出库品种\n");
                sql.append("        SUM(NVL(OT.PARTQTY, 0)) PARTQTY, --出库数量\n");
                sql.append("        SUM(NVL(OD.ORDER_AMOUNT, 0)) ORDER_AMOUNT, --订货金额\n");
                sql.append("        SUM(NVL(BO.BO_AMOUNT, 0)) BO_AMOUNT, --缺件金额\n");
                sql.append("        SUM(NVL(RE.RETURN_AMOUNT, 0)) RETURN_AMOUNT, --退货金额\n");
                sql.append("        SUM(NVL(OT.AMOUNT, 0)) AMOUNT, --实际销售金额\n");
                sql.append("        SUM(NVL(ot.QH_AMOUNT, 0)) QH_AMOUNT, --切换件金额\n");
                sql.append("        SUM(NVL(ot.TS_AMOUNT, 0)) TS_AMOUNT --特殊定件金额\n");
            }
            sql.append("  FROM TM_DEALER     TM,\n");
            sql.append("        PART_OUTSTOCK OT,\n");
            sql.append("        PART_ORDER_JC OD,\n");
            sql.append("        PART_BO       BO,\n");
            sql.append("        DLR_RETURN    RE\n");
            sql.append(" WHERE TM.DEALER_TYPE = 10771002\n");
            sql.append("   AND TM.DEALER_LEVEL = 10851001\n");
            sql.append("   AND TM.DEALER_ID = OT.DEALER_ID(+)\n");
            sql.append("   AND TM.DEALER_ID = OD.DEALER_ID(+)\n");
            sql.append("   AND TM.DEALER_ID = BO.DEALER_ID(+)\n");
            sql.append("   AND TM.DEALER_ID = RE.DEALER_ID(+)\n");
            if (!"".equals(dealer_code)) {
                sql.append("   AND TM.DEALER_CODE LIKE upper('%" + dealer_code + "%')\n");
            }
            if (!"".equals(dealer_name)) {
                sql.append("   AND TM.DEALER_NAME LIKE '%" + dealer_name + "%'\n");
            }
            sql.append(" ORDER BY TM.DEALER_CODE");


            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }


    //zhumingwei 2014-08-21   订单满足率(导出)
    public List<Map<String, Object>> queryOrderMeet(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));

            StringBuffer sql = new StringBuffer("");

            sql.append("WITH ORDER_INFO AS\n");
            sql.append(" (SELECT OM.ORDER_TYPE, COUNT(OD.LINE_ID) PARTCNT, SUM(OD.BUY_QTY) QTYSUM\n");
            sql.append("    FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DLR_ORDER_MAIN OM\n");
            sql.append("   WHERE OD.ORDER_ID = OM.ORDER_ID\n");
            sql.append("     AND EXISTS\n");
            sql.append("   (SELECT 1\n");
            sql.append("            FROM TT_PART_SO_MAIN SM\n");
            sql.append("           WHERE SM.ORDER_ID = OM.ORDER_ID\n");
            sql.append("             AND SM.SELLER_ID = 2010010100070674\n");
            if (!"".equals(SCREATE_DATE)) {
                sql.append("             AND TRUNC(SM.CREATE_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd')--销售单创建时间\n");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append("             AND TRUNC(SM.CREATE_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd'))\n");
            }

            sql.append("   GROUP BY OM.ORDER_TYPE),\n");
            sql.append("OUTSTOCK_ORDER_INFO AS\n");
            sql.append(" (SELECT OM.ORDER_TYPE,\n");
            sql.append("         COUNT(OD.OUTLINE_ID) PARTCNT,\n");
            sql.append("         SUM(OD.OUTSTOCK_QTY) QTYSUM\n");
            sql.append("    FROM TT_PART_OUTSTOCK_DTL OD, TT_PART_OUTSTOCK_MAIN OM\n");
            sql.append("   WHERE OD.OUT_ID = OM.OUT_ID\n");
            sql.append("     AND EXISTS\n");
            sql.append("   (SELECT 1\n");
            sql.append("            FROM TT_PART_SO_MAIN SM\n");
            sql.append("           WHERE SM.PICK_ORDER_ID = OM.PICK_ORDER_ID\n");
            sql.append("             AND SM.SELLER_ID = 2010010100070674\n");
            sql.append("             AND SM.ORDER_ID IS NOT NULL --剔除BO产生的销售单\n");
            if (!"".equals(SCREATE_DATE)) {
                sql.append("             AND TRUNC(SM.CREATE_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd')--销售单创建时间\n");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append("             AND TRUNC(SM.CREATE_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd'))\n");
            }
            sql.append("   GROUP BY OM.ORDER_TYPE),\n");
            sql.append("BO_INFO AS\n");
            sql.append(" (SELECT A.ORDER_TYPE,\n");
            sql.append("         SUM(A.BOQTYCNT) BOQTYCNT,\n");
            sql.append("         SUM(BOQTYSUM) BOQTYSUM,\n");
            sql.append("         SUM(TOSAL_QTY) TOSAL_QTY,\n");
            sql.append("         SUM(CLOSE_QTY) CLOSE_QTY,\n");
            sql.append("         SUM(BO_ODDQTY) BO_ODDQTY\n");
            sql.append("    FROM (SELECT OM.ORDER_TYPE,\n");
            sql.append("                 COUNT(BD.BOLINE_ID) BOQTYCNT,\n");
            sql.append("                 SUM(BD.BO_QTY) BOQTYSUM,\n");
            sql.append("                 SUM(BD.TOSAL_QTY) TOSAL_QTY,\n");
            sql.append("                 SUM(BD.CLOSE_QTY) CLOSE_QTY,\n");
            sql.append("                 SUM(BD.BO_ODDQTY) BO_ODDQTY\n");
            sql.append("            FROM TT_PART_BO_DTL         BD,\n");
            sql.append("                 TT_PART_BO_MAIN        BM,\n");
            sql.append("                 TT_PART_DLR_ORDER_MAIN OM\n");
            sql.append("           WHERE BD.BO_ID = BM.BO_ID\n");
            sql.append("             AND BM.ORDER_ID = OM.ORDER_ID\n");
            sql.append("             AND OM.ORDER_TYPE IN (92151001, 92151002)\n");
            sql.append("             AND EXISTS\n");
            sql.append("           (SELECT 1\n");
            sql.append("                    FROM TT_PART_SO_MAIN SM\n");
            sql.append("                   WHERE SM.SO_ID = BM.SO_ID\n");
            sql.append("                     AND SM.SELLER_ID = 2010010100070674\n");
            if (!"".equals(SCREATE_DATE)) {
                sql.append("             AND TRUNC(SM.CREATE_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd')--销售单创建时间\n");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append("             AND TRUNC(SM.CREATE_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd'))\n");
            }
            sql.append("           GROUP BY OM.ORDER_TYPE\n");
            sql.append("          UNION ALL\n");
            sql.append("          SELECT OM.ORDER_TYPE,\n");
            sql.append("                 COUNT(BD.BOLINE_ID) BOQTYCNT,\n");
            sql.append("                 SUM(BD.BO_QTY) BOQTYSUM,\n");
            sql.append("                 SUM(BD.TOSAL_QTY) TOSAL_QTY,\n");
            sql.append("                 SUM(BD.CLOSE_QTY) CLOSE_QTY,\n");
            sql.append("                 SUM(BD.BO_ODDQTY) BO_ODDQTY\n");
            sql.append("            FROM TT_PART_BO_DTL         BD,\n");
            sql.append("                 TT_PART_BO_MAIN        BM,\n");
            sql.append("                 TT_PART_DLR_ORDER_MAIN OM\n");
            sql.append("           WHERE BD.BO_ID = BM.BO_ID\n");
            sql.append("             AND BM.ORDER_ID = OM.ORDER_ID\n");
            sql.append("             AND OM.ORDER_TYPE IN (92151001, 92151002)\n");
            sql.append("             AND EXISTS\n");
            sql.append("           (SELECT 1\n");
            sql.append("                    FROM TT_PART_SO_MAIN SM\n");
            sql.append("                   WHERE SM.PICK_ORDER_ID = BM.PICK_ORDER_ID\n");
            sql.append("                     AND SM.SELLER_ID = 2010010100070674\n");
            sql.append("                     AND SM.ORDER_ID IS NOT NULL --剔除BO产生的销售单\n");
            if (!"".equals(SCREATE_DATE)) {
                sql.append("             AND TRUNC(SM.CREATE_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd')--销售单创建时间\n");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append("             AND TRUNC(SM.CREATE_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd'))\n");
            }
            sql.append("           GROUP BY OM.ORDER_TYPE) A\n");
            sql.append("   GROUP BY A.ORDER_TYPE)\n");
            sql.append("SELECT (select code_desc from TC_CODE WHERE code_id=OI.ORDER_TYPE) ORDER_TYPE, --订单类型\n");
            sql.append("       OI.PARTCNT dh_PARTCNT, --订货品种\n");
            sql.append("       OOI.PARTCNT fy_PARTCNT, --发运品种\n");
            sql.append("       TO_CHAR(OOI.PARTCNT / OI.PARTCNT * 100, '999.99') || '%' PARTRATE, --品种满足率\n");
            sql.append("       OI.QTYSUM dh_QTYSUM, --订货数量\n");
            sql.append("       OOI.QTYSUM fy_QTYSUM, --发运数量\n");
            sql.append("       TO_CHAR(OOI.QTYSUM / OI.QTYSUM * 100, '999.99') || '%' QTYRATE, --数量满足率\n");
            sql.append("       OI.QTYSUM - OOI.QTYSUM JSBOQTY, --BO数量(计算数量，页面不展示)\n");
            sql.append("       BI.BOQTYSUM, --BO数量\n");
            sql.append("       BI.TOSAL_QTY + BI.CLOSE_QTY CLBOQTY, --BO处理数量\n");
            sql.append("       BI.BO_ODDQTY, --剩余未处理BO数量\n");
            sql.append("       TO_CHAR((BI.TOSAL_QTY + BI.CLOSE_QTY) / BI.BOQTYSUM * 100, '999.99') || '%' BOQTYRATE --BO数量满足率\n");
            sql.append("  FROM ORDER_INFO OI, OUTSTOCK_ORDER_INFO OOI，BO_INFO BI\n");
            sql.append(" WHERE OI.ORDER_TYPE = OOI.ORDER_TYPE(+)\n");
            sql.append("   AND OI.ORDER_TYPE = BI.ORDER_TYPE(+)\n");
            sql.append("   AND OI.ORDER_TYPE IN (92151001, 92151002)");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    //zhumingwei 2014-08-20   BO订单发出数(导出)
    public List<Map<String, Object>> queryBoOrderIssue(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT (select code_desc from tc_code where code_id = OM.ORDER_TYPE) ORDER_TYPE, --订单类型\n");
            sql.append("       DECODE(BM.BO_TYPE, 1, '一般BO', '现场BO') BO_TYPE, --BO类型\n");
            sql.append("       COUNT(BM.BO_ID) BOCNT, --BO总数\n");
            sql.append("       SUM(DECODE(BM.STATE, 92141003, 1, 0)) BOHDCNT, --处理数量\n");
            sql.append("       TO_CHAR(SUM(DECODE(BM.STATE, 92141003, 1, 0)) / COUNT(BM.BO_ID) * 100,\n");
            sql.append("       '999.99') || '%' BORATE --百分比\n");
            sql.append("       FROM TT_PART_BO_MAIN BM, TT_PART_DLR_ORDER_MAIN OM\n");
            sql.append("       WHERE BM.ORDER_ID = OM.ORDER_ID\n");
            if (!"".equals(SCREATE_DATE)) {
                sql.append(" AND TRUNC(BM.CREATE_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-MM-dd') --BO生成开始日期");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append(" AND TRUNC(BM.CREATE_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-MM-dd') --BO生成结束日期");
            }
            sql.append("              AND OM.ORDER_TYPE IN (92151001, 92151002)\n");
            sql.append("              GROUP BY OM.ORDER_TYPE, BM.BO_TYPE\n");
            sql.append("              order by OM.ORDER_TYPE\n");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }


    //zhumingwei 2014-08-21   服务商入库及时率(导出)
    public List<Map<String, Object>> queryDealerStorageTimely(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));

            String dealer_code = CommonUtils.checkNull(request.getParamValue("dealer_code"));
            String dealer_name = CommonUtils.checkNull(request.getParamValue("dealer_name"));

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT TS.DEALER_CODE,--服务商编码\n");
            sql.append("       TS.DEALER_NAME,--服务商名称\n");
            sql.append("       COUNT(TS.TRPLAN_ID) TRCNT,--发运数量\n");
            sql.append("       SUM(TS.DLR) DLRCNT,--及时入库数量\n");
            sql.append("       TO_CHAR(SUM(TS.DLR) / COUNT(TS.TRPLAN_ID) * 100, '999.99') || '%' WLRATE --及时率\n");
            sql.append("       FROM VW_PART_RP_TRANS TS\n");
            sql.append("       WHERE 1 = 1\n");

            if (!"".equals(SCREATE_DATE)) {
                sql.append("              AND TRUNC(TS.CREATE_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd') --发运日期\n");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append("              AND TRUNC(TS.CREATE_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd') --发运日期\n");
            }

            sql.append("              AND TS.DEALER_CODE LIKE '%" + dealer_code + "%'\n");
            sql.append("              AND TS.DEALER_NAME LIKE '%" + dealer_name + "%'\n");
            sql.append("              GROUP BY TS.DEALER_CODE, TS.DEALER_NAME\n");
            sql.append("              ORDER BY TS.DEALER_CODE\n");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    //zhumingwei 2013-11-15 包装材料出入库汇总报表
    public PageResult<Map<String, Object>> queryPckMaterialsList(RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String Specification = CommonUtils.checkNull(request.getParamValue("Specification"));
            String c_name = CommonUtils.checkNull(request.getParamValue("c_name"));
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT P.PACK_ID,\n");
            sql.append("           P.PACK_SPEC,--规格\n");
            sql.append("           (SELECT TC.CODE_DESC\n");
            sql.append("              FROM TC_CODE TC\n");
            sql.append("             WHERE TC.CODE_ID = P.PACK_TYPE) PACK_TYPE,--包装类别\n");
            sql.append("           P.PACK_NAME,--名称\n");
            sql.append("           P.PACK_UOM,--单位\n");
            sql.append("           NVL(SUM(DECODE(R.FLAG, 1, R.QTY)), 0) IN_QTY,--入库数量\n");
            sql.append("           NVL(SUM(DECODE(R.FLAG, 2, R.QTY)), 0) OUT_QTY,--出库数量\n");
            sql.append("           P.PACK_QTY--当前可用\n");
            sql.append("      FROM TT_PART_PACKAGE_RECORD R, TT_PART_PACKAGE P\n");
            sql.append("     WHERE R.PACK_ID = P.PACK_ID\n");

            if (!"".equals(Specification)) {
                sql.append(" AND P.PACK_SPEC LIKE '%").append(Specification).append("%'");
            }
            if (!"".equals(c_name)) {
                sql.append(" AND P.PACK_NAME LIKE '%").append(c_name).append("%'");
            }
            if (!"".equals(SCREATE_DATE)) {
                sql.append(" and p.CREATE_DATE>= to_date('").append(SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append(" and p.CREATE_DATE<= to_date('").append(ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
            }

            sql.append("     GROUP BY P.PACK_ID,\n");
            sql.append("              P.PACK_SPEC,\n");
            sql.append("              P.PACK_TYPE,\n");
            sql.append("              P.PACK_NAME,\n");
            sql.append("              P.PACK_UOM,\n");
            sql.append("              P.PACK_QTY\n");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    //zhumingwei 2013-11-15 库存分析报表(本部)(导出)
    public List<Map<String, Object>> queryStockAnalysis(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String part_oldcode = CommonUtils.checkNull(request.getParamValue("part_oldcode"));
            String part_name = CommonUtils.checkNull(request.getParamValue("part_name"));
            String part_code = CommonUtils.checkNull(request.getParamValue("part_code"));
            String storehouse = CommonUtils.checkNull(request.getParamValue("storehouse"));
            String models = CommonUtils.checkNull(request.getParamValue("models"));
            String part_type = CommonUtils.checkNull(request.getParamValue("part_type"));

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT TPD.PART_OLDCODE,\n");
            sql.append("       TPD.PART_CNAME,\n");
            sql.append("       TPD.PART_CODE,\n");
            sql.append("       TPD.UNIT,\n");
            sql.append("       SP.SALE_PRICE3,\n");
            sql.append("       VS.ITEM_QTY,\n");
            sql.append("       (SP.SALE_PRICE3 * VS.ITEM_QTY) STOCK_AMOUNT,\n");
            sql.append("\n");
            sql.append("       DECODE(T.INVA,\n");
            sql.append("              1,\n");
            sql.append("              '3个月内',\n");
            sql.append("              2,\n");
            sql.append("              '3-6个月内',\n");
            sql.append("              3,\n");
            sql.append("              '6-12个月内',\n");
            sql.append("              4,\n");
            sql.append("              '1-2年内',\n");
            sql.append("              '2年以上') AGING,\n");
            sql.append("       TPD.SERIES_NAME,\n");
            sql.append("       TPD.MODEL_NAME,\n");
            sql.append("       (SELECT C.CODE_DESC FROM TC_CODE C WHERE C.CODE_ID = TPD.PART_TYPE) PART_TYPE\n");
            sql.append("  FROM VW_PART_INVENTORY_AGING T,\n");
            sql.append("       TT_PART_DEFINE          TPD,\n");
            sql.append("       VW_PART_STOCK           VS,\n");
            sql.append("       TT_PART_SALES_PRICE     SP\n");
            sql.append(" WHERE TPD.PART_ID = T.PART_ID(+)\n");
            sql.append("   AND TPD.PART_ID = VS.PART_ID(+)\n");
            sql.append("   AND TPD.PART_ID = SP.PART_ID(+)\n");
            sql.append("   AND VS.ORG_ID = 2010010100070674\n");

            if (!"".equals(part_oldcode)) {
                sql.append(" AND TPD.PART_OLDCODE LIKE '%").append(part_oldcode).append("%'");
            }
            if (!"".equals(part_name)) {
                sql.append(" AND TPD.PART_CNAME LIKE '%").append(part_name).append("%'");
            }
            if (!"".equals(part_code)) {
                sql.append(" AND TPD.PART_CODE LIKE '%").append(part_code).append("%'");
            }
            if (!"".equals(storehouse)) {
                sql.append(" AND T.INVA = " + storehouse + "");
            }
            if (!"".equals(models)) {
                sql.append(" AND TPD.MODEL_NAME like '%" + models + "%'");
            }
            if (!"".equals(part_type)) {
                sql.append(" AND TPD.PART_TYPE = " + part_type + "");
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    //zhumingwei 2013-11-15 包装材料出入库明细报表(导出)
    public List<Map<String, Object>> queryPckMaterialsDetail(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String Specification = CommonUtils.checkNull(request.getParamValue("Specification"));
            String c_name = CommonUtils.checkNull(request.getParamValue("c_name"));
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT PP.PACK_SPEC,\n");
            sql.append("       PP.PACK_NAME,\n");
            sql.append("       DECODE(PR.FLAG, 1, '入库', '出库') FLAG,\n");
            sql.append("       PR.QTY,\n");
            sql.append("       TU.NAME,\n");
            sql.append("       PR.CREATE_DATE\n");
            sql.append("  FROM TT_PART_PACKAGE PP, TT_PART_PACKAGE_RECORD PR, TC_USER TU\n");
            sql.append(" WHERE PP.PACK_ID = PR.PACK_ID\n");
            sql.append("   AND PR.CREATE_BY = TU.USER_ID\n");
            sql.append("   AND PP.STATUS = 10011001\n");
            sql.append("   AND PP.STATUS = 10011001\n");

            if (!"".equals(Specification)) {
                sql.append(" AND Pp.PACK_SPEC LIKE '%").append(Specification).append("%'");
            }
            if (!"".equals(c_name)) {
                sql.append(" AND Pp.PACK_NAME LIKE '%").append(c_name).append("%'");
            }
            if (!"".equals(SCREATE_DATE)) {
                sql.append(" and pr.CREATE_DATE>= to_date('").append(SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append(" and pr.CREATE_DATE<= to_date('").append(ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    //zhumingwei 2013-11-15 包装材料出入库汇总报表(导出)
    public List<Map<String, Object>> queryPckMaterials(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String Specification = CommonUtils.checkNull(request.getParamValue("Specification"));
            String c_name = CommonUtils.checkNull(request.getParamValue("c_name"));
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT P.PACK_ID,\n");
            sql.append("           P.PACK_SPEC,--规格\n");
            sql.append("           (SELECT TC.CODE_DESC\n");
            sql.append("              FROM TC_CODE TC\n");
            sql.append("             WHERE TC.CODE_ID = P.PACK_TYPE) PACK_TYPE,--包装类别\n");
            sql.append("           P.PACK_NAME,--名称\n");
            sql.append("           P.PACK_UOM,--单位\n");
            sql.append("           NVL(SUM(DECODE(R.FLAG, 1, R.QTY)), 0) IN_QTY,--入库数量\n");
            sql.append("           NVL(SUM(DECODE(R.FLAG, 2, R.QTY)), 0) OUT_QTY,--出库数量\n");
            sql.append("           P.PACK_QTY--当前可用\n");
            sql.append("      FROM TT_PART_PACKAGE_RECORD R, TT_PART_PACKAGE P\n");
            sql.append("     WHERE R.PACK_ID = P.PACK_ID\n");

            if (!"".equals(Specification)) {
                sql.append(" AND P.PACK_SPEC LIKE '%").append(Specification).append("%'");
            }
            if (!"".equals(c_name)) {
                sql.append(" AND P.PACK_NAME LIKE '%").append(c_name).append("%'");
            }
            if (!"".equals(SCREATE_DATE)) {
                sql.append(" and p.CREATE_DATE>= to_date('").append(SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append(" and p.CREATE_DATE<= to_date('").append(ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
            }

            sql.append("     GROUP BY P.PACK_ID,\n");
            sql.append("              P.PACK_SPEC,\n");
            sql.append("              P.PACK_TYPE,\n");
            sql.append("              P.PACK_NAME,\n");
            sql.append("              P.PACK_UOM,\n");
            sql.append("              P.PACK_QTY\n");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<Map<String, Object>> queryPurOrderIn(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String inCode = CommonUtils.checkNull(request.getParamValue("IN_CODE"));
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));
            String returnCode = CommonUtils.checkNull(request.getParamValue("RETURN_CODE"));
            String modelName = CommonUtils.checkNull(request.getParamValue("MODEL_NAME"));
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T1.IN_CODE, \n");
            sql.append("        T1.CHECK_CODE, \n");
            sql.append("        T3.RETURN_CODE, \n");
            sql.append("        T1.VENDER_CODE, \n");
            sql.append("        T1.VENDER_NAME, \n");
            sql.append("        T1.PART_OLDCODE, \n");
            sql.append("        T1.PART_CNAME, \n");
            sql.append("        T1.IN_QTY, \n");
            sql.append("        TO_CHAR(T1.BUY_PRICE_NOTAX,'FM999,999,999,999,990.00') BUY_PRICE_NOTAX, \n");
            sql.append("        TO_CHAR(T1.IN_AMOUNT_NOTAX,'FM999,999,999,999,990.00') IN_AMOUNT_NOTAX, \n");
            sql.append("        T4.MODEL_NAME, \n");
            sql.append("        DECODE(T1.PART_TYPE,").append(Constant.PART_BASE_PART_TYPES_SELF_MADE)
                    .append(",'自制件',").append(Constant.PART_BASE_PART_TYPES_PURCHASE).append(",'国产件',")
                    .append(Constant.PART_BASE_PART_TYPES_ENTRANCE).append(",'进口件'")
                    .append(") PART_TYPE,\n");
            sql.append("        T1.REMARK \n");
            sql.append("   FROM TT_PART_PO_IN           T1, \n");
            sql.append("        TT_PART_OEM_RETURN_DTL  T2, \n");
            sql.append("        TT_PART_OEM_RETURN_MAIN T3, \n");
            sql.append("        TT_PART_DEFINE          T4 \n");
            sql.append("  WHERE T1.IN_ID = T2.IN_ID(+) \n");
            sql.append("    AND T2.RETURN_ID = T3.RETURN_ID(+) \n");
            sql.append("    AND T1.PART_ID = T4.PART_ID \n");

            if (!"".equals(inCode)) {
                sql.append(" AND T1.IN_CODE LIKE '%").append(inCode).append("%'");
            }
            if (!"".equals(chkCode)) {
                sql.append(" AND T1.CHECK_CODE LIKE '%").append(chkCode).append("%'");
            }
            if (!"".equals(returnCode)) {
                sql.append(" AND T3.RETURN_CODE LIKE '%").append(returnCode).append("%'");
            }
            if (!"".equals(modelName)) {
                sql.append(" AND T4.MODEL_NAME LIKE '%").append(modelName).append("%'");
            }
            if (!"".equals(partCname)) {
                sql.append(" AND T1.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T1.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T1.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partType)) {
                sql.append(" AND T1.PART_TYPE=").append(partType);
            }
            if (!"".equals(venderId)) {
                sql.append(" AND T1.VENDER_ID=").append(venderId);
            }

            sql.append(" ORDER BY T1.IN_CODE");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public PageResult<Map<String, Object>> queryPartsInvoicingDataList(RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String startDate = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String endDate = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
            String partoldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT TPD.PART_OLDCODE,\n");
            sql.append("       TPD.PART_CNAME,\n");
            sql.append("       TPD.PART_CODE,\n");
            sql.append("       TPD.PART_ID,\n");
            sql.append("       NVL(QC.QC_QTY, 0) QC_QTY,\n");
            sql.append("       NVL(RK.IN_QTY, 0) IN_QTY,\n");
            sql.append("       NVL(CK.OUT_QTY, 0) OUT_QTY,\n");
            sql.append("       NVL(QC.QC_QTY, 0) + NVL(RK.IN_QTY, 0) - NVL(CK.OUT_QTY, 0) JS_QTY,\n");
            sql.append("       VS.ITEM_QTY,\n");
            sql.append("       (NVL(QC.QC_QTY, 0) + NVL(RK.IN_QTY, 0) - NVL(CK.OUT_QTY, 0)) -\n");
            sql.append("       VS.ITEM_QTY CY\n");
            sql.append("  FROM (SELECT S.PART_ID, S.WH_ID, S.ITEM_QTY QC_QTY\n");
            sql.append("          FROM TT_PART_RP_INVREPORT S\n");
            sql.append("         WHERE 1 = 1\n");
            sql.append("           AND S.CREATE_DATE = TO_DATE('" + startDate + "', 'yyyy-mm-dd')-1 --起始日期-1天\n");
            sql.append("           AND S.WH_ID = " + whId + ") QC,\n");
            sql.append("       (SELECT PID.PART_ID, PID.WH_ID, SUM(PID.PART_NUM) IN_QTY\n");
            sql.append("          FROM VW_PART_INSTOCK_DTL PID\n");
            sql.append("         WHERE 1 = 1\n");
            sql.append("           AND PID.WH_ID = " + whId + "\n");
            sql.append("           AND TRUNC(PID.CREATE_DATE) >=\n");
            sql.append("               TO_DATE('" + startDate + "', 'yyyy-mm-dd') --起始日期\n");
            sql.append("           AND TRUNC(PID.CREATE_DATE) <= TO_DATE('" + endDate + "', 'yyyy-mm-dd') --截至日期\n");
            sql.append("         GROUP BY PID.PART_ID, PID.WH_ID) RK,\n");
            sql.append("       (SELECT D.PART_ID, D.WH_ID, SUM(D.PART_NUM) OUT_QTY\n");
            sql.append("          FROM VW_PART_OUTSTOCK_DTL D\n");
            sql.append("         WHERE 1 = 1\n");
            sql.append("           AND D.WH_ID = " + whId + "\n");
            sql.append("           AND TRUNC(D.CREATE_DATE) >=\n");
            sql.append("               TO_DATE('" + startDate + "', 'yyyy-mm-dd') --起始日期\n");
            sql.append("           AND TRUNC(D.CREATE_DATE) <= TO_DATE('" + endDate + "', 'yyyy-mm-dd') --截至日期\n");
            sql.append("         GROUP BY D.PART_ID, D.WH_ID) CK,\n");
            sql.append("       TT_PART_DEFINE TPD,\n");
            sql.append("       (SELECT S.WH_ID,\n");
            sql.append("               S.PART_ID,\n");
            sql.append("               SUM(ITEM_QTY) ITEM_QTY　FROM　 VW_PART_STOCK S WHERE S.WH_ID = " + whId + " GROUP BY S.PART_ID,\n");
            sql.append("               S.WH_ID) VS --期末日期\n");
            sql.append(" WHERE TPD.PART_ID = QC.PART_ID(+)\n");
            sql.append("   AND TPD.PART_ID = RK.PART_ID(+)\n");
            sql.append("   AND TPD.PART_ID = CK.PART_ID(+)\n");
            sql.append("   AND TPD.PART_ID = VS.PART_ID\n");

            if (!"".equals(partoldCode) && null != partoldCode) {
                sql.append(" AND tpd.part_oldcode like upper('%" + partoldCode + "%')\n");
            }

          /*  if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(pid.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
            }
			if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(pid.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
            }*/
            sql.append(" ORDER BY TPD.PART_OLDCODE\n");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }


    public PageResult<Map<String, Object>> queryPartsDInDataList(RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String PART_CNAME = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String PART_CODE = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT PO.CHK_CODE,\n");
            sql.append("       PO.PART_OLDCODE,\n");
            sql.append("       PO.PART_CNAME,\n");
            sql.append("       PO.PART_CODE,\n");
            sql.append("       PO.SPAREIN_QTY,\n");
            sql.append("       PO.PLANER,\n");
            sql.append("       TU.NAME,\n");
            sql.append("       PO.VENDER_NAME\n");
            sql.append("  FROM TT_PART_OEM_PO PO, TC_USER TU\n");
            sql.append(" WHERE PO.WHMAN_ID = TU.USER_ID\n");
            sql.append("   AND PO.STATE = 92271001 --待确认\n");
            sql.append("   AND EXISTS (SELECT 1\n");
            sql.append("          FROM TT_PART_PO_MAIN M\n");
            sql.append("         WHERE M.ORDER_ID = PO.ORDER_ID\n");
            sql.append("           AND M.PLAN_TYPE != 92111111) --add by yuan 20131022 领用不算待入库\n");
            sql.append("   AND PO.STATUS = 1\n");
            sql.append("   AND PO.SPAREIN_QTY > 0\n");

            if (!"".equals(PART_OLDCODE)) {
                sql.append(" AND upper(PO.PART_OLDCODE) like upper('%" + PART_OLDCODE + "%')");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" AND PO.PART_CNAME like '%" + PART_CNAME + "%'");
            }
            if (!"".equals(PART_CODE)) {
                sql.append(" AND upper(PO.PART_CODE) like upper('%" + PART_CODE + "%')");
            }
            /*if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(pid.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
            }
			if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(pid.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
            }*/

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartsDInData(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String PART_CNAME = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String PART_CODE = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT PO.CHK_CODE,\n");
            sql.append("       PO.PART_OLDCODE,\n");
            sql.append("       PO.PART_CNAME,\n");
            sql.append("       PO.PART_CODE,\n");
            sql.append("       PO.SPAREIN_QTY,\n");
            sql.append("       PO.PLANER,\n");
            sql.append("       TU.NAME,\n");
            sql.append("       PO.VENDER_NAME\n");
            sql.append("  FROM TT_PART_OEM_PO PO, TC_USER TU\n");
            sql.append(" WHERE PO.WHMAN_ID = TU.USER_ID\n");
            sql.append("   AND PO.STATE = 92271001 --待确认\n");
            sql.append("   AND EXISTS (SELECT 1\n");
            sql.append("          FROM TT_PART_PO_MAIN M\n");
            sql.append("         WHERE M.ORDER_ID = PO.ORDER_ID\n");
            sql.append("           AND M.PLAN_TYPE != 92111111) --add by yuan 20131022 领用不算待入库\n");
            sql.append("   AND PO.STATUS = 1\n");
            sql.append("   AND PO.SPAREIN_QTY > 0\n");

            if (!"".equals(PART_OLDCODE)) {
                sql.append(" AND PO.PART_OLDCODE like '%" + PART_OLDCODE + "%'");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" AND PO.PART_CNAME like '%" + PART_CNAME + "%'");
            }
            if (!"".equals(PART_CODE)) {
                sql.append(" AND PO.PART_CODE like '%" + PART_CODE + "%'");
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    //zhumingwei add 2013-10-28 有效占用
    public PageResult<Map<String, Object>> queryPartsYxzyDataList(RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String PART_CNAME = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String PART_CODE = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT SM.SO_CODE,\n");
            sql.append("       SM.DEALER_CODE,\n");
            sql.append("       SM.DEALER_NAME,\n");
            sql.append("       SD.PART_ID,\n");
            sql.append("       SD.PART_OLDCODE,\n");
            sql.append("       SD.PART_CNAME,\n");
            sql.append("       SD.PART_CODE,\n");
            sql.append("       SD.SALES_QTY    QTY,\n");
            sql.append("       SD.CREATE_DATE,\n");
            sql.append("       '销售占用'              BTYPE\n");
            sql.append("  FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM\n");
            sql.append(" WHERE SD.SO_ID = SM.SO_ID\n");
            sql.append("   AND SM.SELLER_ID = " + Constant.OEM_ACTIVITIES + "\n");
            sql.append("   AND SM.STATE IN (92371001, 92371002, 92371005, 92451002)\n");
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" AND upper(SD.PART_OLDCODE) like upper('%" + PART_OLDCODE + "%')");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" AND SD.PART_CNAME like '%" + PART_CNAME + "%'");
            }
            if (!"".equals(PART_CODE)) {
                sql.append(" AND upper(SD.PART_CODE) like upper('%" + PART_CODE + "%')");
            }
            sql.append("UNION ALL\n");
            sql.append("SELECT RM.RETURN_CODE,\n");
            sql.append("       RM.ORG_CODE,\n");
            sql.append("       RM.ORG_NAME,\n");
            sql.append("       RD.PART_ID,\n");
            sql.append("       RD.PART_OLDCODE,\n");
            sql.append("       RD.PART_CNAME,\n");
            sql.append("       RD.PART_CODE,\n");
            sql.append("       RD.RETURN_QTY   QTY,\n");
            sql.append("       RD.CREATE_DATE,\n");
            sql.append("       '采购退货占用'              BTYPE\n");
            sql.append("  FROM TT_PART_OEM_RETURN_DTL RD, TT_PART_OEM_RETURN_MAIN RM\n");
            sql.append(" WHERE RD.RETURN_ID = RM.RETURN_ID\n");
            sql.append("   AND RM.ORG_ID = 2010010100070674\n");
            sql.append("   AND RM.STATE IN (92441001, 92441003)\n");
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" AND RD.PART_OLDCODE like '%" + PART_OLDCODE + "%'");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" AND RD.PART_CNAME like '%" + PART_CNAME + "%'");
            }
            if (!"".equals(PART_CODE)) {
                sql.append(" AND RD.PART_CODE like '%" + PART_CODE + "%'");
            }
            sql.append("UNION ALL\n");
            sql.append("\n");
            sql.append("SELECT DM.RETURN_CODE,\n");
            sql.append("       DM.DEALER_CODE,\n");
            sql.append("       DM.DEALER_NAME,\n");
            sql.append("       DD.PART_ID,\n");
            sql.append("       DD.PART_OLDCODE,\n");
            sql.append("       DD.PART_CNAME,\n");
            sql.append("       DD.PART_CODE,\n");
            sql.append("       DD.RETURN_QTY,\n");
            sql.append("       DD.CREATE_DATE,\n");
            sql.append("       '销售退货占用' BTYPE\n");
            sql.append("  FROM TT_PART_DLR_RETURN_DTL DD, TT_PART_DLR_RETURN_MAIN DM\n");
            sql.append(" WHERE DD.RETURN_ID = DM.RETURN_ID\n");
            sql.append("   AND DM.DEALER_ID = 2010010100070674\n");
            sql.append("   AND DM.STATE IN (92361002, 92361004)\n");
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" AND DD.PART_OLDCODE like '%" + PART_OLDCODE + "%'");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" AND DD.PART_CNAME like '%" + PART_CNAME + "%'");
            }
            if (!"".equals(PART_CODE)) {
                sql.append(" AND DD.PART_CODE like '%" + PART_CODE + "%'");
            }

			/*if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(pid.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
            }
			if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(pid.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
            }*/

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartsYxzyData(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String PART_CNAME = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String PART_CODE = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT SM.SO_CODE,\n");
            sql.append("       SM.DEALER_CODE,\n");
            sql.append("       SM.DEALER_NAME,\n");
            sql.append("       SD.PART_ID,\n");
            sql.append("       SD.PART_OLDCODE,\n");
            sql.append("       SD.PART_CNAME,\n");
            sql.append("       SD.PART_CODE,\n");
            sql.append("       SD.SALES_QTY    QTY,\n");
            sql.append("       SD.CREATE_DATE,\n");
            sql.append("       '销售占用'              BTYPE\n");
            sql.append("  FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM\n");
            sql.append(" WHERE SD.SO_ID = SM.SO_ID\n");
            sql.append("   AND SM.SELLER_ID = " + Constant.OEM_ACTIVITIES + "\n");
            sql.append("   AND SM.STATE IN (92371001, 92371002, 92371005, 92451002)\n");
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" AND SD.PART_OLDCODE like '%" + PART_OLDCODE + "%'");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" AND SD.PART_CNAME like '%" + PART_CNAME + "%'");
            }
            if (!"".equals(PART_CODE)) {
                sql.append(" AND SD.PART_CODE like '%" + PART_CODE + "%'");
            }
            sql.append("UNION ALL\n");
            sql.append("SELECT RM.RETURN_CODE,\n");
            sql.append("       RM.ORG_CODE,\n");
            sql.append("       RM.ORG_NAME,\n");
            sql.append("       RD.PART_ID,\n");
            sql.append("       RD.PART_OLDCODE,\n");
            sql.append("       RD.PART_CNAME,\n");
            sql.append("       RD.PART_CODE,\n");
            sql.append("       RD.RETURN_QTY   QTY,\n");
            sql.append("       RD.CREATE_DATE,\n");
            sql.append("       '采购退货占用'              BTYPE\n");
            sql.append("  FROM TT_PART_OEM_RETURN_DTL RD, TT_PART_OEM_RETURN_MAIN RM\n");
            sql.append(" WHERE RD.RETURN_ID = RM.RETURN_ID\n");
            sql.append("   AND RM.ORG_ID = 2010010100070674\n");
            sql.append("   AND RM.STATE IN (92441001, 92441003)\n");
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" AND RD.PART_OLDCODE like '%" + PART_OLDCODE + "%'");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" AND RD.PART_CNAME like '%" + PART_CNAME + "%'");
            }
            if (!"".equals(PART_CODE)) {
                sql.append(" AND RD.PART_CODE like '%" + PART_CODE + "%'");
            }
            sql.append("UNION ALL\n");
            sql.append("\n");
            sql.append("SELECT DM.RETURN_CODE,\n");
            sql.append("       DM.DEALER_CODE,\n");
            sql.append("       DM.DEALER_NAME,\n");
            sql.append("       DD.PART_ID,\n");
            sql.append("       DD.PART_OLDCODE,\n");
            sql.append("       DD.PART_CNAME,\n");
            sql.append("       DD.PART_CODE,\n");
            sql.append("       DD.RETURN_QTY,\n");
            sql.append("       DD.CREATE_DATE,\n");
            sql.append("       '销售退货占用' BTYPE\n");
            sql.append("  FROM TT_PART_DLR_RETURN_DTL DD, TT_PART_DLR_RETURN_MAIN DM\n");
            sql.append(" WHERE DD.RETURN_ID = DM.RETURN_ID\n");
            sql.append("   AND DM.DEALER_ID = 2010010100070674\n");
            sql.append("   AND DM.STATE IN (92361002, 92361004)\n");
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" AND DD.PART_OLDCODE like '%" + PART_OLDCODE + "%'");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" AND DD.PART_CNAME like '%" + PART_CNAME + "%'");
            }
            if (!"".equals(PART_CODE)) {
                sql.append(" AND DD.PART_CODE like '%" + PART_CODE + "%'");
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public PageResult<Map<String, Object>> queryPartCheckList(
            RequestWrapper request, Integer curPage, AclUserBean logonUser, int flag, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String balBeginTime = CommonUtils.checkNull(request.getParamValue("balBeginTime"));//盘点日期
            String balEndTime = CommonUtils.checkNull(request.getParamValue("balEndTime"));//盘点日期
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));
            String venderCode = CommonUtils.checkNull(request.getParamValue("venderCode"));
            String venderName = CommonUtils.checkNull(request.getParamValue("venderName"));

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT RM.CHGORG_CODE, \n");
            sql.append("        RM.CHGORG_CNAME, \n");
            sql.append("        RD.PART_OLDCODE, \n");
            sql.append("        RD.PART_CNAME, \n");
            sql.append("        RD.PART_CODE, \n");
            sql.append("        RD.UNIT, \n");
            sql.append("        RD.DIFF_QTY, \n");
            sql.append("        (SELECT C.CODE_DESC FROM TC_CODE C WHERE C.CODE_ID = RD.CHECK_RESULT) CHECK_RESULT, \n");
            sql.append("        RD.CREATE_DATE \n");
            sql.append("   FROM TT_PART_CHECK_RESULT_DTL RD, TT_PART_CHECK_RESULT_MAIN RM \n");
            sql.append("  WHERE RD.RESULT_ID = RM.RESULT_ID \n");
            sql.append("    AND RM.STATE = ").append(Constant.PART_INVE_ORDER_STATE_06);
            if (flag == 0) {
                sql.append("    AND RM.CHGORG_ID = ").append(logonUser.getOrgId());
            } else {
                if (!"".equals(venderId)) {
                    sql.append("    AND RM.CHGORG_ID = ").append(venderId);
                } else {
                    sql.append("    AND RM.CHGORG_ID <> ").append(logonUser.getOrgId());
                    if (!"".equals(venderCode)) {
                        sql.append(" AND RM.CHGORG_CODE LIKE '%").append(venderCode).append("%'");
                    }
                    if (!"".equals(venderName)) {
                        sql.append(" AND RM.CHGORG_CNAME LIKE '%").append(venderName).append("%'");
                    }
                }
            }
            sql.append("    AND RD.IS_OVER = 1 \n");

            if (!"".equals(partCname)) {
                sql.append(" AND RD.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(RD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(RD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(balBeginTime)) {
                sql.append(" AND to_date(RD.CREATE_DATE)>=").append("to_date('").append(balBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(balEndTime)) {
                sql.append(" AND to_date(RD.CREATE_DATE)<=").append("to_date('").append(balEndTime).append("','yyyy-MM-dd')");
            }

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartCheck(RequestWrapper request,
                                                    AclUserBean logonUser, int flag) throws Exception {
        List<Map<String, Object>> list;
        try {
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String balBeginTime = CommonUtils.checkNull(request.getParamValue("balBeginTime"));//盘点日期
            String balEndTime = CommonUtils.checkNull(request.getParamValue("balEndTime"));//盘点日期
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));
            String venderCode = CommonUtils.checkNull(request.getParamValue("venderCode"));
            String venderName = CommonUtils.checkNull(request.getParamValue("venderName"));

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT RM.CHGORG_CODE, \n");
            sql.append("        RM.CHGORG_CNAME, \n");
            sql.append("        RD.PART_OLDCODE, \n");
            sql.append("        RD.PART_CNAME, \n");
            sql.append("        RD.PART_CODE, \n");
            sql.append("        RD.UNIT, \n");
            sql.append("        RD.DIFF_QTY, \n");
            sql.append("        (SELECT C.CODE_DESC FROM TC_CODE C WHERE C.CODE_ID = RD.CHECK_RESULT) CHECK_RESULT, \n");
            sql.append("        RD.CREATE_DATE \n");
            sql.append("   FROM TT_PART_CHECK_RESULT_DTL RD, TT_PART_CHECK_RESULT_MAIN RM \n");
            sql.append("  WHERE RD.RESULT_ID = RM.RESULT_ID \n");
            sql.append("    AND RM.STATE = ").append(Constant.PART_INVE_ORDER_STATE_06);
            if (flag == 0) {
                sql.append("    AND RM.CHGORG_ID = ").append(logonUser.getOrgId());
            } else {
                if (!"".equals(venderId)) {
                    sql.append("    AND RM.CHGORG_ID = ").append(venderId);
                } else {
                    sql.append("    AND RM.CHGORG_ID <> ").append(logonUser.getOrgId());
                    if (!"".equals(venderCode)) {
                        sql.append(" AND RM.CHGORG_CODE LIKE '%").append(venderCode).append("%'");
                    }
                    if (!"".equals(venderName)) {
                        sql.append(" AND RM.CHGORG_CNAME LIKE '%").append(venderName).append("%'");
                    }
                }
            }
            sql.append("    AND RD.IS_OVER = 1 \n");

            if (!"".equals(partCname)) {
                sql.append(" AND RD.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(RD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(RD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(balBeginTime)) {
                sql.append(" AND to_date(RD.CREATE_DATE)>=").append("to_date('").append(balBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(balEndTime)) {
                sql.append(" AND to_date(RD.CREATE_DATE)<=").append("to_date('").append(balEndTime).append("','yyyy-MM-dd')");
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    /**
     * 服务站订货明细
     *
     * @param request
     * @param curPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageResult<Map<String, Object>> queryDlrOrderDtlList(RequestWrapper request, Integer curPage, Integer pageSize, AclUserBean logonUser) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String DEALER_CODE = CommonUtils.checkNull(request.getParamValue("dealerCode")).toUpperCase();//服务站号
            String DEALER_NAME = CommonUtils.checkNull(request.getParamValue("dealerName"));//服务站名称
            String ORDER_CODE = CommonUtils.checkNull(request.getParamValue("orderCode")).toUpperCase();//订单号
            String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("partOldCode")).toUpperCase();//配件编码
            String PART_CNAME = CommonUtils.checkNull(request.getParamValue("partCnaem"));//配件名称
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));//订货日期从
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));//订货日期到
            String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); //大区

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT * FROM VW_PART_DLR_ORDER_TRANS OM WHERE 1 = 1");
            sql.append("   AND OM.SELLER_ID = 2010010100070674");
            if (!"".equals(SCREATE_DATE)) {
                sql.append(" AND TRUNC(OM.SUBMIT_DATE) >= TO_DATE('").append(SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')\n");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append(" AND TRUNC(OM.SUBMIT_DATE) <= TO_DATE('").append(ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')\n");
            }
            if (!"".equals(DEALER_CODE)) {
                if (DEALER_CODE != null) {
                    if (!(DEALER_CODE.split(",").length <= 0)) {
                        sql.append(" AND OM.DEALER_CODE IN(");
                        String[] paramStrArr = DEALER_CODE.split(",");
                        for (int i = 0; i < paramStrArr.length; i++) {
                            String tempCode = paramStrArr[i];
                            if (i == paramStrArr.length - 1) {
                                sql.append("'" + tempCode + "')\n");
                            } else {
                                sql.append("'" + tempCode + "', ");
                            }
                        }
                    }
                }
            }
            if (!"".equals(DEALER_NAME)) {
                sql.append(" AND OM.DEALER_NAME LIKE '%").append(DEALER_NAME).append("%'\n");
            }
            if (!"".equals(ORDER_CODE)) {
                sql.append(" and OM.ORDER_CODE like '%").append(ORDER_CODE).append("%'\n");
            }
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" and OM.PART_OLDCODE like '%").append(PART_OLDCODE).append("%'\n");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" and OM.PART_CNAME like '%").append(PART_CNAME).append("%'\n");
            }
            //add 大区服务经理区域限制 01-01-15
            if (this.getPoseRoleId(logonUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
                sql.append(CommonUtils.getOrgDealerLimitSqlByPose("OM", logonUser));
            }
            //增加大区限制
            if (!"".equals(orgCode) && null != orgCode) {
                sql.append("AND EXISTS (SELECT 1\n");
                sql.append("         FROM VW_ORG_DEALER_SERVICE S\n");
                sql.append("        WHERE S.DEALER_ID = OM.DEALER_ID\n");
                sql.append("          AND S.Org_Code IN (" + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(orgCode) + "))\n");
            }

//            sql.append("  ORDER BY OM.SUBMIT_DATE\n");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    /**
     * 服务站订货明细
     *
     * @param request
     * @param curPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageResult<Map<String, Object>> queryDlrOrderSumList(RequestWrapper request, Integer curPage, Integer pageSize, AclUserBean logonUser) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String DEALER_CODE = CommonUtils.checkNull(request.getParamValue("dealerCode")).toUpperCase();//服务站号
            String DEALER_NAME = CommonUtils.checkNull(request.getParamValue("dealerName"));//服务站名称
            String ORDER_CODE = CommonUtils.checkNull(request.getParamValue("orderCode")).toUpperCase();//订单号
            String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("partOldCode")).toUpperCase();//配件编码
            String PART_CNAME = CommonUtils.checkNull(request.getParamValue("partCnaem"));//配件名称
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));//订货日期从
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));//订货日期到
            String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); //大区

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT SUM(OM.BUY_QTY) BUY_QTY,\n");
            sql.append("       SUM(OM.ORDER_AMOUNT) ORDER_AMOUNT,\n");
            sql.append("       SUM(OM.OUTSTOCK_QTY) OUTSTOCK_QTY,\n");
            sql.append("       SUM(OM.SALES_AMOUNT) SALES_AMOUNT\n");
            sql.append("  FROM VW_PART_DLR_ORDER_TRANS OM\n");
            sql.append(" WHERE 1 = 1");
            sql.append("   AND OM.SELLER_ID = 2010010100070674");
            if (!"".equals(SCREATE_DATE)) {
                sql.append(" AND TRUNC(OM.SUBMIT_DATE) >= TO_DATE('").append(SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')\n");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append(" AND TRUNC(OM.SUBMIT_DATE) <= TO_DATE('").append(ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')\n");
            }
            if (!"".equals(DEALER_CODE)) {
                if (DEALER_CODE != null) {
                    if (!(DEALER_CODE.split(",").length <= 0)) {
                        sql.append(" AND OM.DEALER_CODE IN(");
                        String[] paramStrArr = DEALER_CODE.split(",");
                        for (int i = 0; i < paramStrArr.length; i++) {
                            String tempCode = paramStrArr[i];
                            if (i == paramStrArr.length - 1) {
                                sql.append("'" + tempCode + "')\n");
                            } else {
                                sql.append("'" + tempCode + "', ");
                            }
                        }
                    }
                }
            }
            if (!"".equals(DEALER_NAME)) {
                sql.append(" AND OM.DEALER_NAME LIKE '%").append(DEALER_NAME).append("%'\n");
            }
            if (!"".equals(ORDER_CODE)) {
                sql.append(" and OM.ORDER_CODE like '%").append(ORDER_CODE).append("%'\n");
            }
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" and OM.PART_OLDCODE like '%").append(PART_OLDCODE).append("%'\n");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" and OM.PART_CNAME like '%").append(PART_CNAME).append("%'\n");
            }

            //add 大区服务经理区域限制 05-01-22
            if (this.getPoseRoleId(logonUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
                sql.append(CommonUtils.getOrgDealerLimitSqlByPose("OM", logonUser));
            }

            //增加大区限制
            if (!"".equals(orgCode) && null != orgCode) {
                sql.append("AND EXISTS (SELECT 1\n");
                sql.append("         FROM VW_ORG_DEALER_SERVICE S\n");
                sql.append("        WHERE S.DEALER_ID = OM.DEALER_ID\n");
                sql.append("          AND S.Org_Code IN (" + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(orgCode) + "))\n");
            }

//            sql.append("  ORDER BY OM.SUBMIT_DATE\n");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

}
