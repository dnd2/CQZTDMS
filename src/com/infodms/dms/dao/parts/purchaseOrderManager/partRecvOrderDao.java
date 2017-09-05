package com.infodms.dms.dao.parts.purchaseOrderManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtIfStandardPO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : huchao
 *         CreateDate     : 2013-7-31
 * @ClassName : partRecvOrderDao
 */
public class partRecvOrderDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partRecvOrderDao.class);
    private static final partRecvOrderDao dao = new partRecvOrderDao();

    private partRecvOrderDao() {
    }

    public static final partRecvOrderDao getInstance() {
        return dao;
    }

    private static final Integer bzType = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_06;
    private static final int enableValue = Constant.STATUS_ENABLE;//有效

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-31
     * @Title : 配件计划领件单信息查询
     */
    public PageResult<Map<String, Object>> queryPartRecvOrder(String sbString, String sbStrN, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT A.ORDER_ID, A.ORDER_CODE, A.BUYER, A.CREATE_DATE, A.PRINT_DATE, A.WH_NAME, '领件计划' AS PLAN_TYPE, " +
                " A.SUM_QTY, TO_CHAR(A.AMOUNT,'FM999,999,999.00') AMOUNT, A.STATE, A.REMARK1, A.PUR_ORDER_CODE " +
                " FROM TT_PART_PO_MAIN A ");
        sql.append(" WHERE A.PLAN_TYPE = '" + Constant.PART_PURCHASE_PLAN_TYPE_03 + "'");

        if (null != sbStrN && !"".equals(sbStrN)) {
            sql.append(" AND A.ORDER_ID IN ");
            sql.append("(SELECT OD.ORDER_ID FROM TT_PART_PO_DTL OD WHERE 1 = 1 ");
            sql.append(sbStrN);
            sql.append(" GROUP BY OD.ORDER_ID) ");
        }

        sql.append(sbString);
        sql.append(" ORDER BY A.CREATE_DATE DESC ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-31
     * @Title : 返回仓库信息List
     */
    public List<Map<String, Object>> getWareHouses(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.WH_ID, TM.WH_NAME AS WH_CNAME "
                        + " FROM TT_PART_WAREHOUSE_DEFINE TM "
                        + " WHERE 1 = 1  ");
        sql.append(" AND TM.STATE = '" + enableValue + "' ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.WH_ID ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-31
     * @Title : 获取供应商List
     */
    public List<Map<String, Object>> getVenderList(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT VD.* "
                        + " FROM TT_PART_VENDER_DEFINE VD "
                        + " WHERE 1 = 1  ");
        sql.append(" AND VD.STATE = '" + enableValue + "' ");
        sql.append(sbString);
        sql.append(" ORDER BY VD.VENDER_NAME ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param dealerName
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-27
     * @Title : 返回配件计划领件单信息List
     */
    public List<Map<String, Object>> queryPartRecvOrderList(String sbString, String sbStrN) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT A.ORDER_ID, A.ORDER_CODE, A.BUYER, A.BUYER_ID, TO_CHAR(A.CREATE_DATE,'yyyy-MM-dd') AS CREATE_DATE, A.WH_ID, A.WH_NAME, '领件计划' AS PLAN_TYPE, A.PUR_ORDER_CODE, " +
                " A.SUM_QTY, TO_CHAR(A.AMOUNT,'FM999,999,999.00') AMOUNT, A.STATE, A.REMARK, A.PUR_ORDER_CODE " +
                " FROM TT_PART_PO_MAIN A ");
        sql.append(" WHERE A.PLAN_TYPE = '" + Constant.PART_PURCHASE_PLAN_TYPE_03 + "'");
        if (null != sbStrN && !"".equals(sbStrN)) {
            sql.append(" AND A.ORDER_ID IN ");
            sql.append("(SELECT OD.ORDER_ID FROM TT_PART_PO_DTL OD WHERE 1 = 1 ");
            sql.append(sbStrN);
            sql.append(" GROUP BY OD.ORDER_ID) ");
        }
        sql.append(sbString);
        sql.append(" ORDER BY A.CREATE_DATE DESC");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * 配件计划领件单详细信息List
     *
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-8-1
     * @Title :
     */
    public PageResult<Map<String, Object>> queryPartRecvOrderDtl(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT A.ORDER_ID, A.ORDER_CODE, OD.POLINE_ID, OD.PART_ID, OD.PART_CODE, OD.PART_OLDCODE, TO_CHAR(OD.FORECAST_DATE,'yyyy-MM-dd') AS  FORECAST_DATE, OD.VENDER_ID, " +
                " OD.PART_CNAME, OD.UNIT, OD.VENDER_NAME, OD.PLAN_QTY,  OD.BUY_PRICE, OD.BUY_QTY, TO_CHAR(OD.BUY_AMOUNT,'FM999,999,990.00') AS BUY_AMOUNT, (OD.BUY_QTY - NVL(OD.CHECK_QTY, '0')) AS GE_QTY, OD.CHECK_QTY, A.STATE, " +
                " (SELECT RL.MAKER_ID FROM TT_PART_VENDER_MAKER_RELATION RL WHERE RL.PART_ID = OD.PART_ID AND RL.VENDER_ID = OD.VENDER_ID AND RL.DF_VENDER = '" + Constant.PART_BASE_FLAG_YES + "' AND RL.DF_MAKER = '" + Constant.PART_BASE_FLAG_YES + "' ) AS MAKER_ID, " +
                " (SELECT MD.MAKER_NAME FROM TT_PART_VENDER_MAKER_RELATION RL, TT_PART_MAKER_DEFINE MD WHERE RL.PART_ID = OD.PART_ID AND RL.VENDER_ID = OD.VENDER_ID AND RL.DF_VENDER = '" + Constant.PART_BASE_FLAG_YES + "' AND RL.DF_MAKER = '" + Constant.PART_BASE_FLAG_YES + "' AND RL.MAKER_ID = MD.MAKER_ID ) AS MAKER_NAME" +
                " FROM TT_PART_PO_MAIN A, TT_PART_PO_DTL OD ");
        sql.append(" WHERE A.PLAN_TYPE = '" + Constant.PART_PURCHASE_PLAN_TYPE_03 + "'");
        sql.append(" AND A.ORDER_ID = OD.ORDER_ID ");
        sql.append(sbString);
        sql.append(" ORDER BY A.CREATE_DATE DESC, OD.PART_OLDCODE ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-2
     * @Title : 配件计划领件单详细信息List
     */
    public List<Map<String, Object>> queryPartRecvOrderDtlList(String sbString) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT A.ORDER_ID, A.ORDER_CODE, A.PUR_ORDER_CODE, OD.POLINE_ID, OD.PART_ID, OD.PART_CODE, OD.PART_OLDCODE, OD.VENDER_ID, TO_CHAR(OD.FORECAST_DATE,'yyyy-MM-dd') AS  FORECAST_DATE, OD.REMARK, " +
                " NVL((SELECT VM.NORMAL_QTY FROM VW_PART_STOCK VM WHERE A.WH_ID = VM.WH_ID AND OD.PART_ID = VM.PART_ID), '0') AS NORMAL_QTY, SD.WHMAN, SD.ROOM," +
                " OD.PART_CNAME, OD.UNIT, OD.VENDER_NAME, OD.PLAN_QTY, OD.BUY_PRICE, OD.BUY_QTY, TO_CHAR(OD.BUY_AMOUNT,'FM999,999,990.00') AS BUY_AMOUNT, (OD.BUY_QTY - NVL(OD.CHECK_QTY, '0')) AS GE_QTY, OD.CHECK_QTY, A.STATE " +
                " FROM TT_PART_PO_MAIN A, TT_PART_PO_DTL OD, TT_PART_SPPLAN_DEFINE SD ");
        sql.append(" WHERE A.PLAN_TYPE = '" + Constant.PART_PURCHASE_PLAN_TYPE_03 + "'");
        sql.append(" AND A.ORDER_ID = OD.ORDER_ID ");
        sql.append(" AND OD.PART_ID = SD.PART_ID ");
        sql.append(sbString);
        sql.append(" ORDER BY SD.WHMAN, SD.ROOM, OD.PART_OLDCODE ");

        list = pageQuery(sql.toString(), null, getFunName());

        return list;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title : 返回仓库配件库存信息
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
        sql.append(" NVL(SD.PLAN_QTY, '0') AS PLAN_QTY, ");
        sql.append(" SD.ROOM, ");
        sql.append(" SD.WHMAN, ");
        sql.append(" U.NAME ");
        sql.append(" FROM TT_PART_BUY_PRICE BP, TT_PART_SALES_PRICE SP, ");
        sql.append(" TT_PART_DEFINE P, ");
        sql.append(" TT_PART_PLAN_DEFINE PP, ");
        sql.append(" TT_PART_WAREHOUSE_DEFINE W, ");
        sql.append(" TT_PART_SPPLAN_DEFINE SD, ");
        sql.append(" TC_USER U ");
        sql.append(" WHERE PP.WH_ID = W.WH_ID ");
        sql.append(" AND PP.PART_ID = P.PART_ID ");
        sql.append(" AND PP.PLAN_TYPE = 1 ");//月计划类型
        sql.append(" AND P.IS_RECEIVE = '" + Constant.IF_TYPE_YES + "' ");//是领件件
        sql.append(" AND W.WH_NAME LIKE '%景德镇%' ");//景德镇仓库
        sql.append(" AND P.STATE = '" + Constant.STATUS_ENABLE + "' ");//有效
        sql.append(" AND SD.STATE = '" + Constant.STATUS_ENABLE + "' ");//有效
        sql.append(" AND P.PART_ID = SD.PART_ID ");
        sql.append(" AND SD.PART_ID = BP.PART_ID ");
        sql.append(" AND SD.PART_ID = SP.PART_ID ");
        sql.append(" AND SD.PLANNER_ID = U.USER_ID(+) ");
        sql.append(sbString);
        sql.append(" ORDER BY SD.WHMAN ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param dealeridBill
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-8-1
     * @Title : 服务商开票信息
     */
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
     * @param : @param oemCompanyId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-23
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

    /**
     * @param : @param oldCode
     * @param : @return
     * @return :
     * LastDate    : 2013-4-15
     * @Title : 验证配件编码是否合法
     * @Description:
     */
    public List<Map<String, Object>> checkOldCode(String oldCode) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT TD.PART_ID,TD.PART_CODE FROM TT_PART_DEFINE TD " +
                " WHERE  UPPER(TD.PART_OLDCODE) = '" + oldCode + "' ";
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }

    /**
     * @param : @param oldCode
     * @param : @param parentOrgId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-25
     * @Title : 返回配件库存状态信息
     */
    public List<Map<String, Object>> getPartStockInfos(String oldCode, String parentOrgId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM VW_PART_STOCK TD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_OLDCODE = '" + oldCode + "' AND TD.ORG_ID = '" + parentOrgId + "' ");
        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-10-29
     * @Title : 获取计划领件维护保管员信息
     */
    public List<Map<String, Object>> getWhManInfos(String sqlStr) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT SD.WHMAN, COUNT(SD.WHMAN) FROM TT_PART_SPPLAN_DEFINE SD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);
        sql.append(" GROUP BY SD.WHMAN ");

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-10-29
     * @Title : 获取计划领件维护信息
     */
    public List<Map<String, Object>> getPartSpplanInfos(String sqlStr) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT SD.* FROM TT_PART_SPPLAN_DEFINE SD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param oldCode
     * @param : @param parentOrgId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-25
     * @Title : 返回精确的配件库存状态信息
     */
    public List<Map<String, Object>> getPartStockInfos(String oldCode, String parentOrgId, String whId, String venderId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TD.*, SP.SALE_PRICE3 AS PLAN_PRICE FROM VW_PART_STOCK TD, TT_PART_SALES_PRICE SP, ");
        sql.append(" TT_PART_BUY_PRICE BP,  ");
        sql.append(" TT_PART_DEFINE P, ");
        sql.append(" TT_PART_SPPLAN_DEFINE SD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND UPPER(TD.PART_OLDCODE) = '" + oldCode + "' AND TD.ORG_ID = '" + parentOrgId + "' ");
        sql.append(" AND TD.WH_ID = '" + whId + "' ");
        sql.append(" AND TD.PART_ID = SP.PART_ID ");
        sql.append(" AND TD.PART_ID = BP.PART_ID ");
        sql.append(" AND BP.VENDER_ID = '" + venderId + "' ");
        sql.append(" AND P.PART_ID = SD.PART_ID ");
        sql.append(" AND P.IS_RECEIVE = '" + Constant.IF_TYPE_YES + "' ");//是领件件
        sql.append(" AND P.PART_ID = TD.PART_ID ");
        sql.append(" AND P.STATE = '" + Constant.STATUS_ENABLE + "' ");//有效

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param oldCode
     * @param : @param parentOrgId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-2
     * @Title : 返回当前已出库、可用库存的数量
     */
    public List getLatestQtys(String sbString) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TD.DTL_ID, TD.RETAL_ID, "
                + " VM.NORMAL_QTY AS STOCK_QTY, VM.IS_LOCKED,"
                + " TD.QTY, TD.OUT_QTY,"
                + " TD.VER"
                + " FROM TT_PART_RETAIL_DTL TD,"
                + " TT_PART_RETAIL_MAIN TM,"
                + " VW_PART_STOCK VM"
                + " WHERE TD.RETAL_ID = TM.RETAIL_ID"
                + " AND TM.SORG_ID = VM.ORG_ID"
                + " AND TD.PART_ID = VM.PART_ID"
                + " AND TM.WH_ID = VM.WH_ID ");
        sql.append(sbString);
        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param oldCode
     * @param : @param parentOrgId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-8
     * @Title : 返回仓库、货位信息
     */
    public List<Map<String, Object>> getWareLocaInfos(String sbString, String orgId, String partId) {

        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");

        if (Constant.OEM_ACTIVITIES.equals(orgId)) {
            sql.append("SELECT WD.WH_ID,"
                    + " WD.WH_CODE,"
                    + " WD.WH_NAME,"
                    + " LD.LOC_ID,"
                    + " LD.LOC_CODE,"
                    + " LD.LOC_NAME"
                    + " FROM TT_PART_WAREHOUSE_DEFINE WD, TT_PART_LOACTION_DEFINE LD"
                    + " WHERE WD.WH_ID = LD.WH_ID"
                    + " AND WD.STATE = '" + enableValue + "' "
                    + " AND WD.ORG_ID = LD.ORG_ID ");
            sql.append(sbString);
        } else {
            TmDealerPO tmDealerPO = new TmDealerPO();
            tmDealerPO.setDealerId(new Long(orgId));
            TmDealerPO dealerPO = (TmDealerPO) this.select(tmDealerPO).get(0);

            if (dealerPO.getPdealerType() == Constant.PART_SALE_PRICE_DEALER_TYPE_01) {
                sql.append("SELECT WD.WH_ID,"
                        + " WD.WH_CODE,"
                        + " WD.WH_NAME,"
                        + " LD.LOC_ID,"
                        + " LD.LOC_CODE,"
                        + " LD.LOC_NAME"
                        + " FROM TT_PART_WAREHOUSE_DEFINE WD, TT_PART_LOACTION_DEFINE LD"
                        + " WHERE WD.WH_ID = LD.WH_ID"
                        + " AND WD.STATE = '" + enableValue + "' "
                        + " AND WD.ORG_ID = LD.ORG_ID ");
                sql.append(sbString);
            } else {

                sql.append("SELECT WD.WH_ID,");
                sql.append("      WD.WH_CODE,");
                sql.append("      WD.WH_NAME,");
                sql.append("      LD.LOC_ID,");
                sql.append("      LD.LOC_CODE,");
                sql.append("      LD.LOC_NAME ");
                sql.append(" FROM TT_PART_WAREHOUSE_DEFINE WD, TT_PART_LOACTION_DEFINE LD\n");
                sql.append(" WHERE wd.state= " + Constant.STATUS_ENABLE);
                sql.append(" AND wd.status=1 ");
                sql.append(" AND wd.org_id=" + orgId);
                sql.append(" AND ld.wh_id=99999 ");
                sql.append(" AND ld.part_id= " + partId);

            }
        }

        list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-12-3
     * @Title : 获取采购订单信息
     */
    public List<Map<String, Object>> queryPoMainList(String sbString) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT A.* FROM TT_PART_PO_MAIN A ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
}
