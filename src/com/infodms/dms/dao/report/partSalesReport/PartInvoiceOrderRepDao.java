package com.infodms.dms.dao.report.partSalesReport;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartInvoiceOrderRepDao extends BaseDao {
    public Logger logger = Logger.getLogger(PartInvoiceOrderRepDao.class);
    private static final PartInvoiceOrderRepDao dao = new PartInvoiceOrderRepDao();

    private PartInvoiceOrderRepDao() {
    }

    public static final PartInvoiceOrderRepDao getInstance() {
        return dao;
    }

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询
     */
    public PageResult<Map<String, Object>> queryRep(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
        String orgName = CommonUtils.checkNull(request.getParamValue("orgName"));
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
        String returnCode = CommonUtils.checkNull(request.getParamValue("returnCode"));
        String isExport = CommonUtils.checkNull(request.getParamValue("isExport"));
        String invoiceNo = CommonUtils.checkNull(request.getParamValue("invoiceNo"));
        String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));
        String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
        String modelName = CommonUtils.checkNull(request.getParamValue("modelName"));
        String partType = CommonUtils.checkNull(request.getParamValue("partType"));
        String InvDateB = CommonUtils.checkNull(request.getParamValue("beginTime"));
        String InvDateE = CommonUtils.checkNull(request.getParamValue("endTime"));


        sql.append("SELECT TPSM.DEALER_CODE,\n");
        sql.append("       TPSM.DEALER_NAME,\n");
        sql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TPD.IS_EXPORT) AS IS_EXPORT,\n");
        sql.append("       TPSM.SO_CODE,\n");
        sql.append("       TPDRM.RETURN_CODE,\n");
        sql.append("       TPSD.PART_OLDCODE,\n");
        sql.append("       TPSD.PART_CNAME,\n");
        sql.append("       TPSD.PART_CODE,\n");
        sql.append("       TPSM.INVOICE_NO,\n");
        sql.append("       TPOD.OUTSTOCK_QTY,\n");
        sql.append("       TPSP.SALE_PRICE3,\n");
        sql.append("       TRUNC(TPSP.SALE_PRICE3 * TPOD.OUTSTOCK_QTY, 2) AS AMOUNT,\n");
        sql.append("       TPD.MODEL_NAME,\n");
        sql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TPD.PART_TYPE) AS PART_TYPE,\n");
        sql.append("       TPOD.SALE_PRICE,\n");
        sql.append("       TPOD.SALE_AMOUNT\n");
        sql.append("  FROM TT_PART_SO_MAIN TPSM\n");
        sql.append("  JOIN TT_PART_SO_DTL TPSD\n");
        sql.append("    ON TPSD.SO_ID = TPSM.SO_ID\n");
        sql.append("  JOIN TT_PART_DEFINE TPD\n");
        sql.append("    ON TPD.PART_ID = TPSD.PART_ID\n");
        sql.append("  LEFT JOIN TT_PART_DLR_RETURN_MAIN TPDRM\n");
        sql.append("    ON TPDRM.SO_ID = TPSM.SO_ID\n");
        sql.append("  JOIN TT_PART_OUTSTOCK_MAIN TPOM\n");
        sql.append("    ON TPOM.SO_ID = TPSM.SO_ID\n");
        sql.append("  JOIN TT_PART_OUTSTOCK_DTL TPOD\n");
        sql.append("    ON TPOD.OUT_ID = TPOM.OUT_ID\n");
        sql.append("   AND TPOD.PART_ID = TPSD.PART_ID\n");
        sql.append("  JOIN TT_PART_SALES_PRICE TPSP\n");
        sql.append("    ON TPSP.PART_ID = TPSD.PART_ID\n");
        sql.append("   AND TPSP.STATE = '10011001'\n");
        sql.append(" WHERE (INVOICE_NO IS NOT NULL OR INVOICE_NO <> '')\n");
        sql.append("   AND TPSM.SELLER_ID = TPOM.SELLER_ID\n");
        sql.append("   AND TPSM.SELLER_ID = 2010010100070674\n");

        if (!"".equals(orgCode)) {
            sql.append(" and tpsm.dealer_code like '%").append(orgCode).append("%'");
        }
        if (!"".equals(orgName)) {
            sql.append(" and tpsm.dealer_name like '%").append(orgName).append("%'");
        }
        if (!"".equals(soCode)) {
            sql.append(" and tpsm.so_code like '%").append(soCode).append("%'");
        }
        if (!"".equals(returnCode)) {
            sql.append(" and tpdrm.return_code like '%").append(returnCode).append("%'");
        }
        if (!"".equals(isExport)) {
            sql.append(" and tpd.is_export='").append(isExport).append("'");
        }
        if (!"".equals(invoiceNo)) {
            sql.append(" and tpsm.invoice_no like '%").append(invoiceNo).append("%'");
        }
        if (!"".equals(partOldcode)) {
            sql.append(" and tpsd.part_oldcode like '%").append(partOldcode).append("%'");
        }
        if (!"".equals(partCname)) {
            sql.append(" and tpsd.part_cname like '%").append(partCname).append("%'");
        }
        if (!"".equals(partCode)) {
            sql.append(" and tpsd.part_code like '%").append(partCode).append("%'");
        }
        if (!"".equals(modelName)) {
            sql.append(" and tpd.model_name like '%").append(modelName).append("%'");
        }
        if (!"".equals(partType)) {
            sql.append(" and tpd.part_type='").append(partType).append("'");
        }
        if (!"".equals(InvDateB)) {
            sql.append("AND trunc(tpom.bill_date)>=to_date('" + InvDateB + "','yyyy-mm-dd')\n");
        }
        if (!"".equals(InvDateE)) {
            sql.append("AND trunc(tpom.bill_date)<=to_date('" + InvDateE + "','yyyy-mm-dd')\n");
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询
     */
    public Map<String, Object> queryRepSum(RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
        String orgName = CommonUtils.checkNull(request.getParamValue("orgName"));
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
        String returnCode = CommonUtils.checkNull(request.getParamValue("returnCode"));
        String isExport = CommonUtils.checkNull(request.getParamValue("isExport"));
        String invoiceNo = CommonUtils.checkNull(request.getParamValue("invoiceNo"));
        String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));
        String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
        String modelName = CommonUtils.checkNull(request.getParamValue("modelName"));
        String partType = CommonUtils.checkNull(request.getParamValue("partType"));
        String InvDateB = CommonUtils.checkNull(request.getParamValue("beginTime"));
        String InvDateE = CommonUtils.checkNull(request.getParamValue("endTime"));


        sql.append("SELECT SUM(B.OUTLINE_ID) OUTLINE_ID,\n");
        sql.append("       ROUND(SUM(B.OUTSTOCK_QTY) / 1.17, 2) OUTSTOCK_QTY,\n");
        sql.append("       ROUND(SUM(B.SALE_AMOUNT) / 1.17, 2) SALE_AMOUNT,\n");
        sql.append("       SUM(B.PLAN_AMOUNT) PLAN_AMOUNT,\n");
        sql.append("       ROUND(SUM(B.FREIGHT) / 1.17, 2) FREIGHT\n");
        sql.append("  FROM (SELECT 0 OUTLINE_ID,\n");
        sql.append("               0 OUTSTOCK_QTY,\n");
        sql.append("               0 SALE_AMOUNT,\n");
        sql.append("               0 PLAN_AMOUNT,\n");
        sql.append("               SUM(A.FREIGHT) FREIGHT\n");
        sql.append("          FROM (SELECT DISTINCT TPOM.*\n");
        sql.append("                  FROM TT_PART_SO_MAIN TPSM\n");
        sql.append("                  JOIN TT_PART_SO_DTL TPSD\n");
        sql.append("                    ON TPSD.SO_ID = TPSM.SO_ID\n");
        sql.append("                  JOIN TT_PART_DEFINE TPD\n");
        sql.append("                    ON TPD.PART_ID = TPSD.PART_ID\n");
        sql.append("                  LEFT JOIN TT_PART_DLR_RETURN_MAIN TPDRM\n");
        sql.append("                    ON TPDRM.SO_ID = TPSM.SO_ID\n");
        sql.append("                  JOIN TT_PART_OUTSTOCK_MAIN TPOM\n");
        sql.append("                    ON TPOM.SO_ID = TPSM.SO_ID\n");
        sql.append("                  JOIN TT_PART_OUTSTOCK_DTL TPOD\n");
        sql.append("                    ON TPOD.OUT_ID = TPOM.OUT_ID\n");
        sql.append("                   AND TPOD.PART_ID = TPSD.PART_ID\n");
        sql.append("                  JOIN TT_PART_SALES_PRICE TPSP\n");
        sql.append("                    ON TPSP.PART_ID = TPSD.PART_ID\n");
        sql.append("                   AND TPSP.STATE = '10011001'\n");
        sql.append("                 WHERE (INVOICE_NO IS NOT NULL OR INVOICE_NO <> '')\n");
        sql.append("                   AND TPSM.SELLER_ID = TPOM.SELLER_ID\n");
        sql.append("                   AND TPSM.SELLER_ID = 2010010100070674\n");
        if (!"".equals(orgCode)) {
            sql.append(" and tpsm.dealer_code like '%").append(orgCode).append("%'");
        }
        if (!"".equals(orgName)) {
            sql.append(" and tpsm.dealer_name like '%").append(orgName).append("%'");
        }
        if (!"".equals(soCode)) {
            sql.append(" and tpsm.so_code like '%").append(soCode).append("%'");
        }
        if (!"".equals(returnCode)) {
            sql.append(" and tpdrm.return_code like '%").append(returnCode).append("%'");
        }
        if (!"".equals(isExport)) {
            sql.append(" and tpd.is_export='").append(isExport).append("'");
        }
        if (!"".equals(invoiceNo)) {
            sql.append(" and tpsm.invoice_no like '%").append(invoiceNo).append("%'");
        }
        if (!"".equals(partOldcode)) {
            sql.append(" and tpsd.part_oldcode like '%").append(partOldcode).append("%'");
        }
        if (!"".equals(partCname)) {
            sql.append(" and tpsd.part_cname like '%").append(partCname).append("%'");
        }
        if (!"".equals(partCode)) {
            sql.append(" and tpsd.part_code like '%").append(partCode).append("%'");
        }
        if (!"".equals(modelName)) {
            sql.append(" and tpd.model_name like '%").append(modelName).append("%'");
        }
        if (!"".equals(partType)) {
            sql.append(" and tpd.part_type='").append(partType).append("'");
        }
        if (!"".equals(InvDateB)) {
            sql.append("AND trunc(tpom.bill_date)>=to_date('" + InvDateB + "','yyyy-mm-dd')\n");
        }
        if (!"".equals(InvDateE)) {
            sql.append("AND trunc(tpom.bill_date)<=to_date('" + InvDateE + "','yyyy-mm-dd')\n");
        }
        sql.append("\t\t\t\t\t\t\t\t\t ) A\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT COUNT(TPOD.OUTLINE_ID) OUTLINE_ID,\n");
        sql.append("               SUM(TPOD.OUTSTOCK_QTY) OUTSTOCK_QTY,\n");
        sql.append("               SUM(TPOD.SALE_AMOUNT) SALE_AMOUNT,\n");
        sql.append("               SUM(TPSP.SALE_PRICE3 * TPOD.OUTSTOCK_QTY) PLAN_AMOUNT,\n");
        sql.append("               0 FREIGHT\n");
        sql.append("          FROM TT_PART_SO_MAIN TPSM\n");
        sql.append("          JOIN TT_PART_SO_DTL TPSD\n");
        sql.append("            ON TPSD.SO_ID = TPSM.SO_ID\n");
        sql.append("          JOIN TT_PART_DEFINE TPD\n");
        sql.append("            ON TPD.PART_ID = TPSD.PART_ID\n");
        sql.append("          LEFT JOIN TT_PART_DLR_RETURN_MAIN TPDRM\n");
        sql.append("            ON TPDRM.SO_ID = TPSM.SO_ID\n");
        sql.append("          JOIN TT_PART_OUTSTOCK_MAIN TPOM\n");
        sql.append("            ON TPOM.SO_ID = TPSM.SO_ID\n");
        sql.append("          JOIN TT_PART_OUTSTOCK_DTL TPOD\n");
        sql.append("            ON TPOD.OUT_ID = TPOM.OUT_ID\n");
        sql.append("           AND TPOD.PART_ID = TPSD.PART_ID\n");
        sql.append("          JOIN TT_PART_SALES_PRICE TPSP\n");
        sql.append("            ON TPSP.PART_ID = TPSD.PART_ID\n");
        sql.append("           AND TPSP.STATE = '10011001'\n");
        sql.append("         WHERE (INVOICE_NO IS NOT NULL OR INVOICE_NO <> '')\n");
        sql.append("           AND TPSM.SELLER_ID = TPOM.SELLER_ID\n");
        sql.append("           AND TPSM.SELLER_ID = 2010010100070674\n");
        sql.append("\n");
        if (!"".equals(orgCode)) {
            sql.append(" and tpsm.dealer_code like '%").append(orgCode).append("%'");
        }
        if (!"".equals(orgName)) {
            sql.append(" and tpsm.dealer_name like '%").append(orgName).append("%'");
        }
        if (!"".equals(soCode)) {
            sql.append(" and tpsm.so_code like '%").append(soCode).append("%'");
        }
        if (!"".equals(returnCode)) {
            sql.append(" and tpdrm.return_code like '%").append(returnCode).append("%'");
        }
        if (!"".equals(isExport)) {
            sql.append(" and tpd.is_export='").append(isExport).append("'");
        }
        if (!"".equals(invoiceNo)) {
            sql.append(" and tpsm.invoice_no like '%").append(invoiceNo).append("%'");
        }
        if (!"".equals(partOldcode)) {
            sql.append(" and tpsd.part_oldcode like '%").append(partOldcode).append("%'");
        }
        if (!"".equals(partCname)) {
            sql.append(" and tpsd.part_cname like '%").append(partCname).append("%'");
        }
        if (!"".equals(partCode)) {
            sql.append(" and tpsd.part_code like '%").append(partCode).append("%'");
        }
        if (!"".equals(modelName)) {
            sql.append(" and tpd.model_name like '%").append(modelName).append("%'");
        }
        if (!"".equals(partType)) {
            sql.append(" and tpd.part_type='").append(partType).append("'");
        }
        if (!"".equals(InvDateB)) {
            sql.append("AND trunc(tpom.bill_date)>=to_date('" + InvDateB + "','yyyy-mm-dd')\n");
        }
        if (!"".equals(InvDateE)) {
            sql.append("AND trunc(tpom.bill_date)<=to_date('" + InvDateE + "','yyyy-mm-dd')\n");
        }
        sql.append("\t\t\t\t\t ) B\n");

        return pageQueryMap(sql.toString(), null, getFunName());
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询
     */
    public List<Map<String, Object>> queryExp(RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
        String orgName = CommonUtils.checkNull(request.getParamValue("orgName"));
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
        String returnCode = CommonUtils.checkNull(request.getParamValue("returnCode"));
        String isExport = CommonUtils.checkNull(request.getParamValue("isExport"));
        String invoiceNo = CommonUtils.checkNull(request.getParamValue("invoiceNo"));
        String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));
        String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
        String modelName = CommonUtils.checkNull(request.getParamValue("modelName"));
        String partType = CommonUtils.checkNull(request.getParamValue("partType"));
        String InvDateB = CommonUtils.checkNull(request.getParamValue("beginTime"));
        String InvDateE = CommonUtils.checkNull(request.getParamValue("endTime"));


        sql.append("SELECT TPSM.DEALER_CODE,\n");
        sql.append("       TPSM.DEALER_NAME,\n");
        sql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TPD.IS_EXPORT) AS IS_EXPORT,\n");
        sql.append("       TPSM.SO_CODE,\n");
        sql.append("       TPDRM.RETURN_CODE,\n");
        sql.append("       TPSD.PART_OLDCODE,\n");
        sql.append("       TPSD.PART_CNAME,\n");
        sql.append("       TPSD.PART_CODE,\n");
        sql.append("       TPSM.INVOICE_NO,\n");
        sql.append("       TPOD.OUTSTOCK_QTY,\n");
        sql.append("       TPSP.SALE_PRICE3,\n");
        sql.append("       TRUNC(TPSP.SALE_PRICE3 * TPOD.OUTSTOCK_QTY, 2) AS AMOUNT,\n");
        sql.append("       TPD.MODEL_NAME,\n");
        sql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TPD.PART_TYPE) AS PART_TYPE,\n");
        sql.append("       TPOD.SALE_PRICE,\n");
        sql.append("       TPOD.SALE_AMOUNT\n");
        sql.append("  FROM TT_PART_SO_MAIN TPSM\n");
        sql.append("  JOIN TT_PART_SO_DTL TPSD\n");
        sql.append("    ON TPSD.SO_ID = TPSM.SO_ID\n");
        sql.append("  JOIN TT_PART_DEFINE TPD\n");
        sql.append("    ON TPD.PART_ID = TPSD.PART_ID\n");
        sql.append("  LEFT JOIN TT_PART_DLR_RETURN_MAIN TPDRM\n");
        sql.append("    ON TPDRM.SO_ID = TPSM.SO_ID\n");
        sql.append("  JOIN TT_PART_OUTSTOCK_MAIN TPOM\n");
        sql.append("    ON TPOM.SO_ID = TPSM.SO_ID\n");
        sql.append("  JOIN TT_PART_OUTSTOCK_DTL TPOD\n");
        sql.append("    ON TPOD.OUT_ID = TPOM.OUT_ID\n");
        sql.append("   AND TPOD.PART_ID = TPSD.PART_ID\n");
        sql.append("  JOIN TT_PART_SALES_PRICE TPSP\n");
        sql.append("    ON TPSP.PART_ID = TPSD.PART_ID\n");
        sql.append("   AND TPSP.STATE = '10011001'\n");
        sql.append(" WHERE (INVOICE_NO IS NOT NULL OR INVOICE_NO <> '')\n");
        sql.append("   AND TPSM.SELLER_ID = TPOM.SELLER_ID\n");
        sql.append("   AND TPSM.SELLER_ID = 2010010100070674\n");

        if (!"".equals(orgCode)) {
            sql.append(" and tpsm.dealer_code like '%").append(orgCode).append("%'");
        }
        if (!"".equals(orgName)) {
            sql.append(" and tpsm.dealer_name like '%").append(orgName).append("%'");
        }
        if (!"".equals(soCode)) {
            sql.append(" and tpsm.so_code like '%").append(soCode).append("%'");
        }
        if (!"".equals(returnCode)) {
            sql.append(" and tpdrm.return_code like '%").append(returnCode).append("%'");
        }
        if (!"".equals(isExport)) {
            sql.append(" and tpd.is_export='").append(isExport).append("'");
        }
        if (!"".equals(invoiceNo)) {
            sql.append(" and tpsm.invoice_no like '%").append(invoiceNo).append("%'");
        }
        if (!"".equals(partOldcode)) {
            sql.append(" and tpsd.part_oldcode like '%").append(partOldcode).append("%'");
        }
        if (!"".equals(partCname)) {
            sql.append(" and tpsd.part_cname like '%").append(partCname).append("%'");
        }
        if (!"".equals(partCode)) {
            sql.append(" and tpsd.part_code like '%").append(partCode).append("%'");
        }
        if (!"".equals(modelName)) {
            sql.append(" and tpd.model_name like '%").append(modelName).append("%'");
        }
        if (!"".equals(partType)) {
            sql.append(" and tpd.part_type='").append(partType).append("'");
        }
        if (!"".equals(InvDateB)) {
            sql.append("AND trunc(tpom.bill_date)>=to_date('" + InvDateB + "','yyyy-mm-dd')\n");
        }
        if (!"".equals(InvDateE)) {
            sql.append("AND trunc(tpom.bill_date)<=to_date('" + InvDateE + "','yyyy-mm-dd')\n");
        }
        List<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, this.getFunName());
        return ps;
    }
}
