package com.infodms.dms.dao.report.partSalesReport;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : huchao
 *         CreateDate     : 2013-11-13
 * @ClassName : mothOutStockAmtDlrDao
 */
public class mothOutStockAmtDlrDao extends BaseDao {
    public static Logger logger = Logger.getLogger(mothOutStockAmtDlrDao.class);
    private static final mothOutStockAmtDlrDao dao = new mothOutStockAmtDlrDao();

    private mothOutStockAmtDlrDao() {

    }

    public static final mothOutStockAmtDlrDao getInstance() {
        return dao;
    }

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 配件库存盘点信息查询
     */
    public PageResult<Map<String, Object>> queryMothOutStockAmtDlr(RequestWrapper request, int pageSize, int curPage, AclUserBean logonUser) {

        String partCode = CommonUtils.checkNull(request.getParamValue("partCode")); // 配件件号
        String partCname = CommonUtils.checkNull(request.getParamValue("partCname")); // 配件名称
        String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 服务商编码
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 服务商名称
        String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 开始时间
        String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 结束时间
        String dealerId1 = CommonUtils.checkNull(request.getParamValue("dealerId")); // 所选服务商ID

        //当前登录单位ID,主机厂为空，服务商为当前用户的dealerId
        String orgId = logonUser.getDealerId();
        //前台没有勾选服务商且后台当前用户的所属服务商不为空取当前用户所属服务商
        if ("".equals(dealerId1) && orgId != null) {
            dealerId1 = orgId;
        }

        StringBuffer sql = new StringBuffer("");

        sql.append("WITH RP_QC AS\n");
        sql.append(" (SELECT QI.DEALER_ID, QI.PART_ID, IN_QTY - NVL(OUT_QTY, 0) QC_QTY\n");
        sql.append("    FROM (SELECT DD.PART_ID, SUM(DD.IN_QTY) IN_QTY, DD.DEALER_ID\n");
        sql.append("            FROM VW_PART_DLR_INSTOCK_DTL DD\n");
        sql.append("           WHERE 1 = 1\n");
        sql.append("             AND DD.CREATE_DATE <= TO_DATE('" + checkSDate + "', 'yyyy-mm-dd') - 1 --起始日期前一天为期初日期\n");
        sql.append("           GROUP BY DD.PART_ID, DD.DEALER_ID) QI,\n");
        sql.append("         (SELECT DD.PART_ID, SUM(DD.OUT_QTY) OUT_QTY, DD.DEALER_ID\n");
        sql.append("            FROM VW_PART_DLR_OUTSTOCK_DTL DD\n");
        sql.append("           WHERE 1 = 1\n");
        sql.append("             AND DD.CREATE_DATE <= TO_DATE('" + checkSDate + "', 'yyyy-mm-dd') - 1 --起始日期前一天为期初日期\n");
        sql.append("           GROUP BY DD.PART_ID, DD.DEALER_ID) QC\n");
        sql.append("   WHERE QI.PART_ID = QC.PART_ID(+)\n");
        sql.append("     AND QI.DEALER_ID = QC.DEALER_ID(+)),\n");
        sql.append("RP_XS AS\n");
        sql.append(" (SELECT DD.PART_ID,\n");
        sql.append("         SUM(DD.OUT_QTY) OUT_QTY,\n");
        sql.append("         SUM(DD.SALE_AMOUNT) OUT_AMOUNT,\n");
        sql.append("         DD.DEALER_ID\n");
        sql.append("    FROM VW_PART_DLR_OUTSTOCK_DTL DD\n");
        sql.append("   WHERE 1 = 1\n");
        sql.append("     AND TO_CHAR(DD.CREATE_DATE, 'yyyy-MM-dd') >= '" + checkSDate + "'\n");
        sql.append("     AND TO_CHAR(DD.CREATE_DATE, 'yyyy-MM-dd') <= '" + checkEDate + "'\n");
        sql.append("   GROUP BY DD.DEALER_ID, DD.PART_ID),\n");
        sql.append("RP_GJ AS\n");
        sql.append(" (SELECT DD.PART_ID,\n");
        sql.append("         SUM(DD.IN_QTY) IN_QTY,\n");
        sql.append("         SUM(NVL(DD.IN_AMOUNT, 0)) IN_AMOUNT,\n");
        sql.append("         DD.DEALER_ID\n");
        sql.append("    FROM VW_PART_DLR_INSTOCK_DTL DD\n");
        sql.append("   WHERE 1 = 1\n");
        sql.append("     AND TO_CHAR(DD.CREATE_DATE, 'yyyy-MM-dd') >= '" + checkSDate + "'\n");
        sql.append("     AND TO_CHAR(DD.CREATE_DATE, 'yyyy-MM-dd') <= '" + checkEDate + "'\n");
        sql.append("   GROUP BY DD.DEALER_ID, DD.PART_ID)\n");
        sql.append("SELECT TD.DEALER_CODE,\n");
        sql.append("       TD.DEALER_NAME,\n");
        sql.append("       TPD.PART_OLDCODE,\n");
        sql.append("       TPD.PART_CNAME,\n");
        sql.append("       TPD.UNIT,\n");
        sql.append("       NVL(QC.QC_QTY, 0) QC_QTY,\n");
        sql.append("       NVL(GJ.IN_QTY, 0) IN_QTY,\n");
        sql.append("       NVL(GJ.IN_AMOUNT, 0) IN_AMOUNT,\n");
        sql.append("       NVL(XS.OUT_QTY, 0) OUT_QTY,\n");
        sql.append("       NVL(XS.OUT_AMOUNT, 0) OUT_AMOUNT,\n");
        sql.append("       NVL(QC.QC_QTY, 0) + NVL(GJ.IN_QTY, 0) - NVL(XS.OUT_QTY, 0) QM_QTY,\n");
        sql.append("       (NVL(QC.QC_QTY, 0) + NVL(GJ.IN_QTY, 0) - NVL(XS.OUT_QTY, 0)) *\n");
        sql.append("       SP.SALE_PRICE1 QM_AMOUNT\n");
        sql.append("  FROM RP_QC               QC,\n");
        sql.append("       RP_GJ               GJ,\n");
        sql.append("       RP_XS               XS,\n");
        sql.append("       TM_DEALER           TD,\n");
        sql.append("       TT_PART_DEFINE      TPD,\n");
        sql.append("       TT_PART_SALES_PRICE SP\n");
        sql.append(" WHERE QC.DEALER_ID = TD.DEALER_ID\n");
        sql.append("   AND QC.PART_ID = TPD.PART_ID\n");
        sql.append("   AND QC.PART_ID = SP.PART_ID\n");
        sql.append("   AND QC.DEALER_ID = GJ.DEALER_ID(+)\n");
        sql.append("   AND QC.PART_ID = GJ.PART_ID(+)\n");
        sql.append("   AND QC.DEALER_ID = XS.DEALER_ID(+)\n");
        sql.append("   AND QC.PART_ID = XS.PART_ID(+)\n");

        if (!"".equals(partOldcode) && null != partOldcode) {
            sql.append("  AND UPPER(TPD.PART_OLDCODE) LIKE '%" + partOldcode + "%'\n");
        }
        if (!"".equals(partCode) && null != partCode) {
            sql.append("  AND UPPER(TPD.PART_CODE) LIKE '%" + partCode + "%'\n");
        }
        if (!"".equals(partCname) && null != partCname) {
            sql.append("  AND TPD.PART_CNAME LIKE '%" + partCname + "%'\n");
        }
        if (!"".equals(dealerId1) && null != dealerId1) {
            sql.append("  AND TD.DEALER_ID = " + dealerId1 + "\n");
        }
        if (!"".equals(dealerCode) && null != dealerCode) {
            sql.append("  AND TD.DEALER_CODE LIKE '%" + dealerCode + "%'\n");
        }
        if (!"".equals(dealerName) && null != dealerName) {
            sql.append("  AND TD.DEALER_NAME LIKE '%" + dealerName + "%'\n");
        }

        //add 大区服务经理区域限制 15-01-22
        if (this.getPoseRoleId(logonUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
            sql.append(CommonUtils.getOrgDealerLimitSqlByPose("TD", logonUser));
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param dealerName
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回配件库存盘点信息List
     */
    public List<Map<String, Object>> queryMothOutStockAmtDlr(String sbString, String dealerId) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT A.DEALER_ID,\n");
        sql.append("       A.DEALER_CODE,\n");
        sql.append("       A.DEALER_NAME,\n");
        sql.append("       A.PART_OLDCODE,\n");
        sql.append("       A.PART_CNAME,\n");
        sql.append("       A.PART_CODE,\n");
        sql.append("       A.UNIT,\n");
        sql.append("       NULL QC_STOCK,\n");
        sql.append("       SUM(A.OUT_QTY) OUT_QTY,\n");
        sql.append("       SUM(A.SALE_AMOUNT) SALE_AMOUNT,\n");
        sql.append("       SUM(A.IN_QTY) IN_QTY,\n");
        sql.append("       SUM(A.IN_AMOUNT) IN_AMOUNT,\n");
        sql.append("       VW.ITEM_QTY ITEM_QTY\n");
        sql.append("  FROM (SELECT DD.DEALER_CODE,\n");
        sql.append("               DD.DEALER_NAME,\n");
        sql.append("               DD.PART_ID,\n");
        sql.append("               DD.PART_OLDCODE,\n");
        sql.append("               DD.PART_CNAME,\n");
        sql.append("               DD.PART_CODE,\n");
        sql.append("               DD.OUT_QTY      OUT_QTY,\n");
        sql.append("               DD.SALE_AMOUNT  SALE_AMOUNT,\n");
        sql.append("               0               IN_QTY,\n");
        sql.append("               0               IN_AMOUNT,\n");
        sql.append("               DD.DEALER_ID,\n");
        sql.append("               DD.UNIT,\n");
        sql.append("               DD.WH_ID\n");
        sql.append("          FROM VW_PART_DLR_OUTSTOCK_DTL DD\n");
        sql.append("         WHERE 1 = 1\n");
        sql.append(sbString + "\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT DD.DEALER_CODE,\n");
        sql.append("               DD.DEALER_NAME,\n");
        sql.append("               DD.PART_ID,\n");
        sql.append("               DD.PART_OLDCODE,\n");
        sql.append("               DD.PART_CNAME,\n");
        sql.append("               DD.PART_CODE,\n");
        sql.append("               0 OUT_QTY,\n");
        sql.append("               0 SALE_AMOUNT,\n");
        sql.append("               DD.IN_QTY IN_QTY,\n");
        sql.append("               NVL(DD.IN_AMOUNT, 0) IN_AMOUNT,\n");
        sql.append("               DD.DEALER_ID,\n");
        sql.append("               DD.UNIT,\n");
        sql.append("               DD.WH_ID\n");
        sql.append("          FROM VW_PART_DLR_INSTOCK_DTL DD\n");
        sql.append("         WHERE 1 = 1\n");
        sql.append(sbString + "\n");
        sql.append("           ) A,\n");
        sql.append("       VW_PART_STOCK VW\n");
        sql.append(" WHERE A.DEALER_ID = VW.ORG_ID\n");
        sql.append("   AND A.PART_ID = VW.PART_ID\n");
        sql.append("   AND A.WH_ID = VW.WH_ID\n");
        if (!"".equals(dealerId) && null != dealerId) {
            sql.append("   AND VW.ORG_ID = " + dealerId + "\n");
        }
        sql.append(" GROUP BY A.DEALER_ID,\n");
        sql.append("          A.DEALER_CODE,\n");
        sql.append("          A.DEALER_NAME,\n");
        sql.append("          A.PART_OLDCODE,\n");
        sql.append("          A.PART_CNAME,\n");
        sql.append("          A.PART_CODE,\n");
        sql.append("          A.UNIT,\n");
        sql.append("          VW.ITEM_QTY");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param oemCompanyId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回主机厂名称
     */
    public String getMainCompanyName(String oemCompanyId) {
        String companyName = "";
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.COMPANY_NAME "
                        + " FROM TM_COMPANY TM "
                        + " WHERE 1 = 1  ");

        sql.append("  AND TM.COMPANY_ID = '" + oemCompanyId + "' ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            companyName = list.get(0).get("COMPANY_NAME").toString();
        }

        return companyName;
    }

    /**
     * @param : @param oemCompanyId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回服务商名称
     */
    public String getDealerName(String oemCompanyId) {
        String companyName = "";
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TD.DEALER_NAME "
                        + " FROM TM_DEALER TD "
                        + " WHERE 1 = 1  ");

        sql.append("  AND TD.DEALER_ID = '" + oemCompanyId + "' ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            companyName = list.get(0).get("DEALER_NAME").toString();
        }

        return companyName;
    }

    public PageResult<Map<String, Object>> queryMothOutStockAmtGyzxDlr(
            RequestWrapper request, AclUserBean logonUser, Integer pageSize, Integer curPage) {

        String partCode = CommonUtils.checkNull(request.getParamValue("partCode")); // 配件件号
        String partCname = CommonUtils.checkNull(request.getParamValue("partCname")); // 配件名称
        String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 供应中心编码
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 供应中心名称
        String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 出库开始时间
        String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 出库结束时间

        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT A.DEALER_ID, \n");
        sql.append("        A.DEALER_CODE, \n");
        sql.append("        A.DEALER_NAME, \n");
        sql.append("        A.PART_OLDCODE, \n");
        sql.append("        A.PART_CNAME, \n");
        sql.append("        A.PART_CODE, \n");
        sql.append("        A.UNIT, \n");
        sql.append("        NULL QC_STOCK, \n");
        sql.append("        SUM(A.OUT_QTY) OUT_QTY, \n");
        sql.append("        SUM(A.SALE_AMOUNT) SALE_AMOUNT, \n");
        sql.append("        SUM(A.IN_QTY) IN_QTY, \n");
        sql.append("        SUM(A.IN_AMOUNT) IN_AMOUNT, \n");
        sql.append("        VW.ITEM_QTY ITEM_QTY \n");
        sql.append("   FROM (SELECT DOD.DEALER_CODE, \n");
        sql.append("                DOD.DEALER_NAME, \n");
        sql.append("                DOD.PART_OLDCODE, \n");
        sql.append("                DOD.PART_CNAME, \n");
        sql.append("                DOD.PART_CODE, \n");
        sql.append("                DOD.UNIT, \n");
        sql.append("                DOD.OUT_QTY      OUT_QTY, \n");
        sql.append("                DOD.SALE_AMOUNT  SALE_AMOUNT, \n");
        sql.append("                0                IN_QTY, \n");
        sql.append("                0                IN_AMOUNT, \n");
        sql.append("                DOD.DEALER_ID \n");
        sql.append("           FROM VW_PART_DLR_OUTSTOCK_DTL DOD \n");
        sql.append("          WHERE 1 = 1 \n");

        if (logonUser.getDealerId() == null) {
            sql.append("            AND DOD.DEALER_CODE LIKE 'G%' \n");
        }

        if (null != partCode && !"".equals(partCode)) {
            sql.append(" AND UPPER(DOD.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
        }
        if (null != partCname && !"".equals(partCname)) {
            sql.append(" AND DOD.PART_CNAME LIKE '%" + partCname + "%' ");
        }
        if (null != partOldcode && !"".equals(partOldcode)) {
            sql.append(" AND UPPER(DOD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
        }
        if (null != dealerCode && !"".equals(dealerCode)) {
            sql.append(" AND UPPER(DOD.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%' ");
        }
        if (null != dealerName && !"".equals(dealerName)) {
            sql.append(" AND DOD.DEALER_NAME LIKE '%" + dealerName.trim() + "%' ");
        }
        if (null != checkSDate && !"".equals(checkSDate)) {
            sql.append(" AND TO_CHAR(DOD.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
        }
        if (null != checkEDate && !"".equals(checkEDate)) {
            sql.append(" AND TO_CHAR(DOD.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
        }

        sql.append("         UNION ALL \n");
        sql.append("         SELECT DID.DEALER_CODE, \n");
        sql.append("                DID.DEALER_NAME, \n");
        sql.append("                DID.PART_OLDCODE, \n");
        sql.append("                DID.PART_CNAME, \n");
        sql.append("                DID.PART_CODE, \n");
        sql.append("                DID.UNIT, \n");
        sql.append("                0 OUT_QTY, \n");
        sql.append("                0 SALE_AMOUNT, \n");
        sql.append("                DID.IN_QTY IN_QTY, \n");
        sql.append("                NVL(DID.IN_AMOUNT, 0) IN_AMOUNT, \n");
        sql.append("                DID.DEALER_ID \n");
        sql.append("           FROM VW_PART_DLR_INSTOCK_DTL DID \n");
        sql.append("          WHERE 1 = 1 \n");

        if (logonUser.getDealerId() == null) {
            sql.append("            AND DID.DEALER_CODE LIKE 'G%' \n");
        }

        if (null != partCode && !"".equals(partCode)) {
            sql.append(" AND UPPER(DID.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
        }
        if (null != partCname && !"".equals(partCname)) {
            sql.append(" AND DID.PART_CNAME LIKE '%" + partCname + "%' ");
        }
        if (null != partOldcode && !"".equals(partOldcode)) {
            sql.append(" AND UPPER(DID.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
        }
        if (null != dealerCode && !"".equals(dealerCode)) {
            sql.append(" AND UPPER(DID.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%' ");
        }
        if (null != dealerName && !"".equals(dealerName)) {
            sql.append(" AND DID.DEALER_NAME LIKE '%" + dealerName.trim() + "%' ");
        }
        if (null != checkSDate && !"".equals(checkSDate)) {
            sql.append(" AND TO_CHAR(DID.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
        }
        if (null != checkEDate && !"".equals(checkEDate)) {
            sql.append(" AND TO_CHAR(DID.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
        }

        sql.append("         ) A, \n");
        sql.append("         \n");
        sql.append("        VW_PART_STOCK VW \n");
        sql.append("  WHERE A.DEALER_ID = VW.ORG_ID \n");
        sql.append("    AND A.PART_OLDCODE = VW.PART_OLDCODE \n");
        sql.append("  GROUP BY A.DEALER_ID, \n");
        sql.append("           A.DEALER_CODE, \n");
        sql.append("           A.DEALER_NAME, \n");
        sql.append("           A.PART_OLDCODE, \n");
        sql.append("           A.PART_CNAME, \n");
        sql.append("           A.PART_CODE, \n");
        sql.append("           A.UNIT, \n");
        sql.append("           VW.ITEM_QTY \n");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;

    }

    public List<Map<String, Object>> queryMothOutStockAmtGyzxDlr(
            RequestWrapper request, AclUserBean logonUser) {

        String partCode = CommonUtils.checkNull(request.getParamValue("partCode")); // 配件件号
        String partCname = CommonUtils.checkNull(request.getParamValue("partCname")); // 配件名称
        String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 供应中心编码
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 供应中心名称
        String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 出库开始时间
        String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 出库结束时间

        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT A.DEALER_ID, \n");
        sql.append("        A.DEALER_CODE, \n");
        sql.append("        A.DEALER_NAME, \n");
        sql.append("        A.PART_OLDCODE, \n");
        sql.append("        A.PART_CNAME, \n");
        sql.append("        A.PART_CODE, \n");
        sql.append("        A.UNIT, \n");
        sql.append("        NULL QC_STOCK, \n");
        sql.append("        SUM(A.OUT_QTY) OUT_QTY, \n");
        sql.append("        SUM(A.SALE_AMOUNT) SALE_AMOUNT, \n");
        sql.append("        SUM(A.IN_QTY) IN_QTY, \n");
        sql.append("        SUM(A.IN_AMOUNT) IN_AMOUNT, \n");
        sql.append("        VW.ITEM_QTY ITEM_QTY \n");
        sql.append("   FROM (SELECT DOD.DEALER_CODE, \n");
        sql.append("                DOD.DEALER_NAME, \n");
        sql.append("                DOD.PART_OLDCODE, \n");
        sql.append("                DOD.PART_CNAME, \n");
        sql.append("                DOD.PART_CODE, \n");
        sql.append("                DOD.UNIT, \n");
        sql.append("                DOD.OUT_QTY      OUT_QTY, \n");
        sql.append("                DOD.SALE_AMOUNT  SALE_AMOUNT, \n");
        sql.append("                0                IN_QTY, \n");
        sql.append("                0                IN_AMOUNT, \n");
        sql.append("                DOD.DEALER_ID \n");
        sql.append("           FROM VW_PART_DLR_OUTSTOCK_DTL DOD \n");
        sql.append("          WHERE 1 = 1 \n");

        if (logonUser.getDealerId() == null) {
            sql.append("            AND DOD.DEALER_CODE LIKE 'G%' \n");
        }

        if (null != partCode && !"".equals(partCode)) {
            sql.append(" AND UPPER(DOD.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
        }
        if (null != partCname && !"".equals(partCname)) {
            sql.append(" AND DOD.PART_CNAME LIKE '%" + partCname + "%' ");
        }
        if (null != partOldcode && !"".equals(partOldcode)) {
            sql.append(" AND UPPER(DOD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
        }
        if (null != dealerCode && !"".equals(dealerCode)) {
            sql.append(" AND UPPER(DOD.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%' ");
        }
        if (null != dealerName && !"".equals(dealerName)) {
            sql.append(" AND DOD.DEALER_NAME LIKE '%" + dealerName.trim() + "%' ");
        }
        if (null != checkSDate && !"".equals(checkSDate)) {
            sql.append(" AND TO_CHAR(DOD.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
        }
        if (null != checkEDate && !"".equals(checkEDate)) {
            sql.append(" AND TO_CHAR(DOD.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
        }

        sql.append("         UNION ALL \n");
        sql.append("         SELECT DID.DEALER_CODE, \n");
        sql.append("                DID.DEALER_NAME, \n");
        sql.append("                DID.PART_OLDCODE, \n");
        sql.append("                DID.PART_CNAME, \n");
        sql.append("                DID.PART_CODE, \n");
        sql.append("                DID.UNIT, \n");
        sql.append("                0 OUT_QTY, \n");
        sql.append("                0 SALE_AMOUNT, \n");
        sql.append("                DID.IN_QTY IN_QTY, \n");
        sql.append("                NVL(DID.IN_AMOUNT, 0) IN_AMOUNT, \n");
        sql.append("                DID.DEALER_ID \n");
        sql.append("           FROM VW_PART_DLR_INSTOCK_DTL DID \n");
        sql.append("          WHERE 1 = 1 \n");

        if (logonUser.getDealerId() == null) {
            sql.append("            AND DID.DEALER_CODE LIKE 'G%' \n");
        }

        if (null != partCode && !"".equals(partCode)) {
            sql.append(" AND UPPER(DID.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
        }
        if (null != partCname && !"".equals(partCname)) {
            sql.append(" AND DID.PART_CNAME LIKE '%" + partCname + "%' ");
        }
        if (null != partOldcode && !"".equals(partOldcode)) {
            sql.append(" AND UPPER(DID.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
        }
        if (null != dealerCode && !"".equals(dealerCode)) {
            sql.append(" AND UPPER(DID.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%' ");
        }
        if (null != dealerName && !"".equals(dealerName)) {
            sql.append(" AND DID.DEALER_NAME LIKE '%" + dealerName.trim() + "%' ");
        }
        if (null != checkSDate && !"".equals(checkSDate)) {
            sql.append(" AND TO_CHAR(DID.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
        }
        if (null != checkEDate && !"".equals(checkEDate)) {
            sql.append(" AND TO_CHAR(DID.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
        }

        sql.append("         ) A, \n");
        sql.append("         \n");
        sql.append("        VW_PART_STOCK VW \n");
        sql.append("  WHERE A.DEALER_ID = VW.ORG_ID \n");
        sql.append("    AND A.PART_OLDCODE = VW.PART_OLDCODE \n");
        sql.append("  GROUP BY A.DEALER_ID, \n");
        sql.append("           A.DEALER_CODE, \n");
        sql.append("           A.DEALER_NAME, \n");
        sql.append("           A.PART_OLDCODE, \n");
        sql.append("           A.PART_CNAME, \n");
        sql.append("           A.PART_CODE, \n");
        sql.append("           A.UNIT, \n");
        sql.append("           VW.ITEM_QTY \n");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
