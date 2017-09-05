package com.infodms.dms.dao.parts.financeManager.partInvoiceManager;

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
 *         CreateDate     : 2013-5-3
 * @ClassName : partInvoiceDao
 */
public class partInvoiceDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partInvoiceDao.class);

    private static final partInvoiceDao dao = new partInvoiceDao();

    private partInvoiceDao() {
    }

    public static final partInvoiceDao getInstance() {
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
     * @throws : LastDate    : 2013-5-6
     * @Title : 财务金税发票信息查询
     */
    public PageResult<Map<String, Object>> queryPartInvoices(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT OM.OUT_ID, OM.OUT_CODE, OM.SO_ID, OM.SO_CODE, OM.ORDER_TYPE, OM.DEALER_ID, OM.DEALER_NAME, SM.FCAUDIT_DATE, UU.NAME AS F_NAME, "
                        + " OM.IS_INV, OM.BILL_BY, OM.BILL_DATE, OM.BILL_UPDATE_BY, OM.BILL_UPDATE_DATE, OM.BILL_NO, "
                        + " NVL((SELECT D.IS_NBDW FROM TM_DEALER D WHERE D.DEALER_ID = OM.DEALER_ID),'0') AS IS_NBDW, "
                        + " OM.INV_WAY, OM.SELLER_ID, OM.SELLER_NAME, TO_CHAR(OM.AMOUNT,'fm999,999,999,990.00') AS AMOUNT, OM.CREATE_DATE, OM.IS_EXPORT, OM.EXPORT_DATE, U.NAME, WH.WH_NAME, BD.INV_TYPE "
                        + " FROM TT_PART_OUTSTOCK_MAIN OM, TC_USER U, TC_USER UU, TT_PART_WAREHOUSE_DEFINE WH, TT_PART_SO_MAIN SM,  TT_PART_BILL_DEFINE BD ");
        sql.append(" WHERE 1 = 1 AND OM.STATUS = 1  ");
        
        sql.append(" AND OM.SO_ID = SM.SO_ID");
        // 凡是经销商提交过退货申请的都不能够开票
        sql.append(" AND NOT EXISTS ( ");
        sql.append("         SELECT 1 ");
        sql.append("         FROM TT_PART_DLR_RETURN_MAIN RM ");
        sql.append("         WHERE OM.SO_ID = RM.SO_ID ");
        sql.append("         AND RM.STATE > '"+Constant.PART_DLR_RETURN_STATUS_01+"' ");
        sql.append("     ) ");
        sql.append(" AND OM.SO_ID = SM.SO_ID");
        sql.append(" AND SM.FCAUDIT_BY = UU.USER_ID(+)");
        sql.append(" AND OM.EXPORT_BY = U.USER_ID(+)");
        sql.append(" AND OM.WH_ID = WH.WH_ID(+)");
        sql.append(" AND OM.DEALER_ID = BD.DEALER_ID(+) ");
        sql.append(sbString);
        sql.append(" ORDER BY OM.DEALER_NAME  ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> queryInvCountInfos(RequestWrapper request, int pageSize, int curPage) {
        String outCode = CommonUtils.checkNull(request
                .getParamValue("outCode")); // 出库单号
        String dealerName = CommonUtils.checkNull(request
                .getParamValue("resultCode")); // 订货单位
        String orderType = CommonUtils.checkNull(request
                .getParamValue("orderType")); // 订单类型
        String invoOutState = CommonUtils.checkNull(request
                .getParamValue("invoOutState")); // 金税导出状态
        String whId = CommonUtils.checkNull(request
                .getParamValue("whId")); // 出库仓库ID
        String checkSDate = CommonUtils.checkNull(request
                .getParamValue("checkSDate")); // 出库开始时间
        String checkEDate = CommonUtils.checkNull(request
                .getParamValue("checkEDate")); // 出库截止时间
        String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID

        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT MAST.ORDER_COUNT, MAST.DEALER_ID, MAST.AMOUNT, LINE.LINE_COUNT,  OM.DEALER_NAME");
        sql.append(" FROM (SELECT M.DEALER_ID, TO_CHAR(SUM(M.AMOUNT),'fm999,999,999,990.00') AS AMOUNT, COUNT(1) AS ORDER_COUNT");
        sql.append(" FROM TT_PART_OUTSTOCK_MAIN M ");
        sql.append(" WHERE M.STATUS = 1 ");
        sql.append(" AND M.SELLER_ID = '" + parentOrgId + "' ");
        if (null != dealerName && !"".equals(dealerName)) {
            sql.append(" AND M.DEALER_NAME LIKE '%" + dealerName + "%' ");
        }
        if (null != checkSDate && !"".equals(checkSDate)) {
            sql.append(" AND TO_CHAR(M.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
        }
        if (null != checkEDate && !"".equals(checkEDate)) {
            sql.append(" AND TO_CHAR(M.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
        }
        if (null != invoOutState && !"".equals(invoOutState)) {
            sql.append(" AND M.IS_EXPORT = '" + invoOutState + "' ");
        }
        if (null != orderType && !"".equals(orderType)) {
            sql.append(" AND M.ORDER_TYPE = '" + orderType + "' ");
        }
        if (null != whId && !"".equals(whId)) {
            sql.append(" AND M.WH_ID = '" + whId + "' ");
        }
        if (null != outCode && !"".equals(outCode)) {
            sql.append(" AND M.OUT_CODE LIKE '%" + outCode + "%' ");
        }
        sql.append(" GROUP BY M.DEALER_ID) MAST, ");
        sql.append(" (SELECT M.DEALER_ID,COUNT(DTL.OUTLINE_ID) AS LINE_COUNT ");
        sql.append(" FROM TT_PART_OUTSTOCK_MAIN M, TT_PART_OUTSTOCK_DTL DTL ");
        sql.append(" WHERE M.OUT_ID = DTL.OUT_ID ");
        sql.append(" AND M.STATUS = 1 ");
        sql.append(" AND DTL.STATUS = 1 ");
        sql.append(" AND M.SELLER_ID = '" + parentOrgId + "'");
        if (null != dealerName && !"".equals(dealerName)) {
            sql.append(" AND M.DEALER_NAME LIKE '%" + dealerName + "%' ");
        }
        if (null != checkSDate && !"".equals(checkSDate)) {
            sql.append(" AND TO_CHAR(M.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
        }
        if (null != checkEDate && !"".equals(checkEDate)) {
            sql.append(" AND TO_CHAR(M.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
        }
        if (null != invoOutState && !"".equals(invoOutState)) {
            sql.append(" AND M.IS_EXPORT = '" + invoOutState + "' ");
        }
        if (null != orderType && !"".equals(orderType)) {
            sql.append(" AND M.ORDER_TYPE = '" + orderType + "' ");
        }
        if (null != whId && !"".equals(whId)) {
            sql.append(" AND M.WH_ID = '" + whId + "' ");
        }
        if (null != outCode && !"".equals(outCode)) {
            sql.append(" AND M.OUT_CODE LIKE '%" + outCode + "%' ");
        }
        sql.append(" GROUP BY M.DEALER_ID) LINE , TM_DEALER OM ");
        sql.append(" WHERE MAST.DEALER_ID = LINE.DEALER_ID");
        sql.append(" AND MAST.DEALER_ID = OM.DEALER_ID");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回仓库信息List
     */
    public List<Map<String, Object>> getWareHouses(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.WH_ID, TM.WH_NAME AS WH_CNAME "
                        + " FROM TT_PART_WAREHOUSE_DEFINE TM "
                        + " WHERE 1 = 1  ");

        sql.append(sbString);
        sql.append(" ORDER BY TM.WH_ID ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param dealerName
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回库存财务金税发票信息List
     */
    public List<Map<String, Object>> queryPartInvoicesList(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT OM.OUT_ID, OM.OUT_CODE, OM.SO_ID, OM.SO_CODE, OM.ORDER_TYPE, OM.DEALER_ID, OM.DEALER_NAME, SM.FCAUDIT_DATE, UU.NAME AS F_NAME, "
                        + " OM.IS_INV, OM.BILL_BY, OM.BILL_DATE, OM.BILL_UPDATE_BY, OM.BILL_UPDATE_DATE, OM.BILL_NO, "
                        + " OM.INV_WAY, OM.SELLER_ID, OM.SELLER_NAME, TO_CHAR(OM.AMOUNT,'fm999,999,999,990.00') AS AMOUNT, OM.CREATE_DATE, OM.IS_EXPORT, OM.EXPORT_DATE, U.NAME, WH.WH_NAME, BD.INV_TYPE "
                        + " FROM TT_PART_OUTSTOCK_MAIN OM, TC_USER U, TC_USER UU, TT_PART_WAREHOUSE_DEFINE WH, TT_PART_SO_MAIN SM,  TT_PART_BILL_DEFINE BD ");
        sql.append(" WHERE 1 = 1 AND OM.STATUS = 1  ");
        sql.append(" AND OM.SO_ID = SM.SO_ID");
        sql.append(" AND SM.FCAUDIT_BY = UU.USER_ID(+)");
        sql.append(" AND OM.EXPORT_BY = U.USER_ID(+)");
        sql.append(" AND OM.WH_ID = WH.WH_ID(+)");
        sql.append(" AND OM.DEALER_ID = BD.DEALER_ID(+) ");
        sql.append(sbString);
        sql.append(" ORDER BY OM.DEALER_NAME  ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-17
     * @Title : 返回订货单位ID List
     */
    public List<Map<String, Object>> queryInvoDealerList(String sbString) {
        StringBuffer sql = new StringBuffer("");
        //modify by yuan 20130921 start
        sql.append("SELECT OM.DEALER_ID\n");
        sql.append("  FROM TT_PART_OUTSTOCK_MAIN OM, TT_PART_BILL_DEFINE BD\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append(" AND OM.STATUS = 1\n");
        sql.append(" AND OM.DEALER_ID = BD.DEALER_ID(+) ");
        //end
        sql.append(sbString);
        sql.append(" GROUP BY OM.DEALER_ID ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-17
     * @Title : 金税文本Title信息List
     */
    public List<Map<String, Object>> queryInvoTxtTitleList(String sbString) {
        StringBuffer sql = new StringBuffer("");
        //modify by yuan 20130921 start
        sql.append("SELECT OM.OUT_ID,\n");
        sql.append("       OM.OUT_CODE,\n");
        sql.append("       OM.SO_ID,\n");
        sql.append("       OM.SO_CODE,\n");
        sql.append("       OM.DEALER_ID,\n");
        sql.append("       OM.DEALER_NAME,\n");
        sql.append("       OM.SELLER_ID,\n");
        sql.append("       OM.SELLER_NAME,\n");
        sql.append("       TO_CHAR(NVL(OM.FREIGHT, '0'), 'FM9999990.00') AS OM_FREIGHT,\n");
        sql.append("       TO_CHAR(OM.SALE_DATE, 'yyMMdd') AS SALE_DATE,\n");
        sql.append("       /* OMM.ORDER_ID,\n");
        sql.append("       OMM.ORDER_CODE,*/\n");
        sql.append("       BD.BILL_ID,\n");
        sql.append("       TO_CHAR(NVL(OM.FREIGHT, '0'), 'FM9999990.00') AS FREIGHT,\n");
        sql.append("       BD.TAX_NAME,\n");
        sql.append("       BD.TAX_NO,\n");
        sql.append("       BD.ADDR,\n");
        sql.append("       BD.TEL,\n");
        sql.append("       BD.BANK,\n");
        sql.append("       BD.ACCOUNT,\n");
        sql.append("       BD.CREATE_BY\n");
        sql.append("  FROM TT_PART_OUTSTOCK_MAIN OM, TT_PART_BILL_DEFINE BD\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND OM.STATUS = 1\n");
        sql.append("   AND OM.DEALER_ID = BD.DEALER_ID\n");
        //end
        sql.append(sbString);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-17
     * @Title : 金税文本Detail信息List
     */
    public List<Map<String, Object>> queryInvoTxtDtlList(String sbString) {
        StringBuffer sql = new StringBuffer("");
        //modify by yuan 20130921 start
        sql.append("SELECT '昌河' || NVL(TC.CODE_DESC, '国产件') || '-' || OD.PART_CNAME || '~~' ||\n");
        sql.append("       OD.UNIT || '~~' || OD.PART_CODE || '~~' || OD.OUTSTOCK_QTY || '~~' ||\n");
        sql.append("       TO_CHAR(OD.SALE_AMOUNT, 'fm999999999990.00') ||\n");
        sql.append("       '~~0.17~~税目~~0~~~~~~~~~~1' AS INVO_TXT_DTL\n");
        sql.append("  FROM TT_PART_OUTSTOCK_MAIN  OM,\n");
        sql.append("       TT_PART_OUTSTOCK_DTL   OD,\n");
        sql.append("       TT_PART_BILL_DEFINE    BD,\n");
        sql.append("       TT_PART_DEFINE         TD,\n");
        sql.append("       TC_CODE                TC\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND OM.STATUS = 1\n");
        sql.append("   AND OM.OUT_ID = OD.OUT_ID\n");
        sql.append("   AND OM.DEALER_ID = BD.DEALER_ID\n");
        sql.append("   AND OD.PART_ID = TD.PART_ID\n");
        sql.append("   AND OD.OUTSTOCK_QTY > 0\n");
        sql.append("   AND OD.SALE_AMOUNT > 0\n");
        sql.append("   AND TD.PART_TYPE = TC.CODE_ID(+)\n");
        //end
        sql.append(sbString);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param oemCompanyId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-23
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
     * @param : @param delName
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title : 返回服务商信息
     */
    public List<Map<String, Object>> getDealerName(String dlrName) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TD.DEALER_ID, TD.DEALER_CODE, TD.DEALER_NAME "
                        + " FROM TM_DEALER TD "
                        + " WHERE 1 = 1  ");

        sql.append("  AND TD.DEALER_CODE = '" + dlrName + "' ");
        sql.append("  AND TD.DEALER_TYPE = '" + Constant.MSG_TYPE_2 + "' ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) //list size 应该为 1（测试数据有问题）
        {
            return list;
        } else {
            sql = new StringBuffer("");
            sql
                    .append("SELECT TM.COMPANY_ID AS DEALER_ID, TM.COMPANY_CODE AS DEALER_CODE, TM.COMPANY_NAME AS DEALER_NAME "
                            + " FROM TM_COMPANY TM "
                            + " WHERE 1 = 1  ");

            sql.append("  AND TM.COMPANY_NAME = '" + dlrName + "' ");

            list = pageQuery(sql.toString(), null, getFunName());
        }

        return list;
    }

    /**
     * @param : @param delearId
     *          * @param      : @param orgId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-20
     * @Title : 获取服务商账户ID信息
     */
    public String getdelearAccountId(String delearId, String orgId) {
        String accountId = "";
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT AD.ACCOUNT_ID "
                        + " FROM TT_PART_ACCOUNT_DEFINE AD "
                        + " WHERE 1 = 1  ");

        sql.append("  AND AD.CHILDORG_ID = '" + delearId + "' ");
        sql.append("  AND AD.PARENTORG_ID = '" + orgId + "' ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            accountId = list.get(0).get("ACCOUNT_ID").toString();
        }

        return accountId;
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title : 获取服务商资金导入方式
     */
    public String getAccountImportType() {
        String impType = "0";
        List<Map<String, Object>> list = null;
        String sql = "SELECT TB.PARA_VALUE FROM TM_BUSINESS_PARA TB WHERE TB.PARA_ID = '" + Constant.FIXCODE_IMPORT_TYPE + "'";
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        if (list.size() > 0)
            impType = list.get(0).get("PARA_VALUE").toString();
        return impType;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title : 验证 是否已导入发票信息
     */
    public List<Map<String, Object>> CheckBillInfo(String sqlStr) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT BD.* "
                        + " FROM TT_PART_OUTSTOCK_MAIN BD "
                        + " WHERE 1 = 1  ");
        sql.append(sqlStr);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());

        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-18
     * @Title : 获取出库单信息
     */
    public List<Map<String, Object>> getOutStockInfo(String sqlStr) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT BD.* "
                        + " FROM TT_PART_OUTSTOCK_MAIN BD "
                        + " WHERE 1 = 1  ");
        sql.append(sqlStr);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());

        return list;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-17
     * @Title : 获取出库单信息
     */
    public List<Map<String, Object>> queryOutStockInfos(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT OM.*, TO_CHAR(OM.AMOUNT,'FM999,999,990.00') AS INV_AMOUNT "
                        + " FROM TT_PART_OUTSTOCK_MAIN OM"
                        + " WHERE 1 = 1 ");
        sql.append(sbString);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public List<Map<String, Object>> queryOutStockInfosNew(String sbString) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT OM_GP.DEALER_ID, OM_GP.INV_AMOUNT ");
        sql.append(" FROM (SELECT OM.DEALER_ID, TO_CHAR(SUM(NVL(OM.AMOUNT,'0')),'FM999,999,990.00') AS INV_AMOUNT ");
        sql.append(" FROM TT_PART_OUTSTOCK_MAIN OM ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);
        sql.append(" GROUP BY OM.DEALER_ID ) OM_GP, TM_DEALER TD ");
        sql.append(" WHERE OM_GP.DEALER_ID = TD.DEALER_ID ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-11-18
     * @Title : 获取出库单明细
     */
    public List<Map<String, Object>> queryDtlList(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TC.CODE_DESC, TD.PART_TYPE,  ");
        sql.append(" SUM(OD.SALE_AMOUNT) AS SALE_AMOUNT ");
        sql.append("  FROM TT_PART_OUTSTOCK_MAIN  OM, ");
        sql.append("       TT_PART_OUTSTOCK_DTL   OD, ");
        sql.append("       TT_PART_BILL_DEFINE    BD, ");
        sql.append("       TT_PART_DEFINE         TD, ");
        sql.append("       TC_CODE                TC ");
        sql.append(" WHERE 1 = 1 ");
        sql.append("   AND OM.STATUS = 1 ");
        sql.append("   AND OM.OUT_ID = OD.OUT_ID ");
        sql.append("   AND OM.DEALER_ID = BD.DEALER_ID ");
        sql.append("   AND OD.PART_ID = TD.PART_ID ");
        sql.append("   AND OD.OUTSTOCK_QTY > 0 ");
        sql.append("   AND OD.SALE_AMOUNT > 0 ");
        sql.append("   AND TD.PART_TYPE = TC.CODE_ID(+) ");
        sql.append(sbString);
        sql.append(" GROUP BY TC.CODE_DESC, TD.PART_TYPE ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
