package com.infodms.dms.dao.parts.storageManager.partStockMvManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.schema.SchemaStringEnumEntryImpl;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : huchao CreateDate : 2013-4-22
 * @ClassName : partResRecDao
 */
public class partDlrStockMvDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partDlrStockMvDao.class);
    private static final partDlrStockMvDao dao = new partDlrStockMvDao();

    private partDlrStockMvDao() {
    }

    public static final partDlrStockMvDao getInstance() {
        return dao;
    }

    private static final Integer bzType = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_06;
    private static final int enableValue = Constant.STATUS_ENABLE;//有效

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * 移库申请主界面查询
     *
     * @param request
     * @param pageSize
     * @param curPage
     * @return
     */
    public PageResult<Map<String, Object>> queryPartMvOrders(RequestWrapper request, int pageSize, int curPage) {
        String changeCode = CommonUtils.checkNull(request.getParamValue("changeCode")); // 制单单号
        String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 开始时间
        String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 截止时间
        String whId = CommonUtils.checkNull(request.getParamValue("fromWhId")); // 移出库ID
        String toWhId = CommonUtils.checkNull(request.getParamValue("toWhId")); // 移入库ID
        String orderState = CommonUtils.checkNull(request.getParamValue("orderState")); // 制单状态
        String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));// 当前机构ID
        String fromOrgId = CommonUtils.checkNull(request.getParamValue("fromOrgId"));// 调拨机构
        String chgId = CommonUtils.checkNull(request.getParamValue("CHG_ID")) == ""
                ? CommonUtils.checkNull(request.getParamValue("chgId"))
                : CommonUtils.checkNull(request.getParamValue("CHG_ID"));// 变更单ID

        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT SM.CHG_ID,\n");
        sql.append("       SM.CHG_CODE,\n");
        sql.append("       SM.ORG_ID,\n");
        sql.append("       SM.ORG_CODE,\n");
        sql.append("       SM.ORG_NAME,\n");
        sql.append("       SM.REMARK,\n");
        sql.append("       SM.CREATE_DATE,\n");
        sql.append("       SM.STATE,\n");
        sql.append("       TU.NAME,\n");
        sql.append("       SM.WH_ID,\n");
        sql.append("       W1.WH_NAME,\n");
        sql.append("       SM.TOWH_ID,\n");
        sql.append("       SM.FLAG,\n");
        sql.append("       W2.WH_NAME TOWH_NAME,\n");
        sql.append("       SM.TOORG_ID,\n");
        sql.append("       SM.TOORG_CODE,\n");
        sql.append("       SM.TOORG_NAME,\n");
//        sql.append("       TD.DEALER_ID FROMDEALER_ID,\n");
//        sql.append("       TD.DEALER_CODE FROMDEALER_CODE,\n");
//        sql.append("       TD.DEALER_NAME FROMDEALER_NAME,\n");
        sql.append("       SM.CHECK_REMARK\n");
        sql.append("  FROM TT_PART_STOCK_CHG_MIAN   SM,\n");
        sql.append("       TC_USER                  TU,\n");
        sql.append("       TT_PART_WAREHOUSE_DEFINE W1,\n");
        sql.append("       TT_PART_WAREHOUSE_DEFINE W2\n");
//        sql.append("       TM_DEALER TD\n");
        sql.append(" WHERE SM.WH_ID = W1.WH_ID\n");
        sql.append("   AND SM.TOWH_ID = W2.WH_ID\n");
//        sql.append("   AND TD.DEALER_ID = W2.ORG_ID\n");
        sql.append("   AND SM.CREATE_BY = TU.USER_ID(+)\n");

        if(!"".equals(chgId) && null != chgId) {
            sql.append("   AND SM.CHG_ID =" + chgId + "");
        }else if (!"".equals(orderState) && null != orderState) {
            // 根据移库状态 获取添加条件，移库状态是已提交或已审核时，查询移库经销商的记录
            sql.append("   AND SM.STATE=" + orderState + "\n");
            long orderStateL = Long.parseLong(orderState);
            if(orderStateL == Constant.PART_MV_STATUS_02 || orderStateL == Constant.PART_MV_STATUS_03){
                sql.append("   AND SM.ORG_ID = '" + fromOrgId + "'\n");
            }else{
//                sql.append("   AND W2.ORG_ID = '" + orgId + "'\n");
                sql.append("   AND SM.TOORG_ID = '" + orgId + "'\n");
            }
        }else {
            sql.append("   AND SM.ORG_ID = '" + orgId + "'\n");
        }
        if (!"".equals(changeCode) && null != changeCode) {
            sql.append("   AND SM.CHG_CODE LIKE '%" + changeCode.toUpperCase() + "%'\n");
        }
        if (!"".equals(checkSDate) && null != checkSDate) {
            sql.append("   AND TO_CHAR(SM.CREATE_DATE) >= '" + checkSDate + "' ");
        }
        if (!"".equals(checkEDate) && null != checkEDate) {
            sql.append("   AND TO_CHAR(SM.CREATE_DATE) <= '" + checkEDate + "' ");
        }
        if (!"".equals(whId) && null != whId) {
            sql.append("   AND SM.WH_ID =" + whId + "\n");
        }
        if (!"".equals(toWhId) && null != toWhId) {
            sql.append("   AND SM.TOWH_ID=" + toWhId + "\n");
        }

        sql.append(" ORDER BY SM.CREATE_DATE DESC");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-22
     * @Title : 返回仓库信息List
     */
    public List<Map<String, Object>> getWareHouses(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TM.WH_ID, TM.WH_NAME AS WH_CNAME, TM.ORG_ID, TM.ORG_NAME "
                + " FROM TT_PART_WAREHOUSE_DEFINE TM " + " WHERE 1 = 1  ");
        sql.append(" AND TM.STATE = '" + enableValue + "' ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.WH_ID ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public List<Map<String, Object>> queryAllPartSaleOrders(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql.append(
                "SELECT TM.RETAIL_ID, TM.RETAIL_CODE, TM.SORG_CNAME, TM.CHG_TYPE, TM.WH_ID, TM.WH_CNAME, U.NAME, TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') AS CREATE_DATE, TM.STATE, "
                        + " TO_CHAR(TDD.AMOUNTS,'fm999,999,999,990.00') AS AMOUNTS, TM.LINKMAN, TM.TEL, TM.PURPOSE, TM.REMARK, TDD.OUT_QTYS "
                        + " FROM TT_PART_RETAIL_MAIN TM, TC_USER U, (SELECT TD.RETAL_ID, NVL(SUM(TD.SALE_AMOUNT),'0.00') AS AMOUNTS, SUM(NVL(TD.OUT_QTY, 0)) AS OUT_QTYS FROM TT_PART_RETAIL_DTL TD GROUP BY TD.RETAL_ID ) TDD ");
        sql.append(" WHERE TM.CREATE_BY = U.USER_ID ");
        sql.append(" AND TM.RETAIL_ID = TDD.RETAL_ID(+)");
        sql.append(sbString);
        sql.append(" ORDER BY TM.CREATE_DATE DESC");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public PageResult<Map<String, Object>> queryMvDeatil(RequestWrapper request, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        String chgId = CommonUtils.checkNull(request.getParamValue("CHG_ID")) == null
                ? CommonUtils.checkNull(request.getParamValue("CHG_ID"))
                : CommonUtils.checkNull(request.getParamValue("CHG_ID")); // 订单ID
        String optionType = CommonUtils.checkNull(request.getParamValue("optionType"));// 操作类型

        sql.append("SELECT SD.PART_OLDCODE,\n");
        sql.append("       SD.PART_CNAME,\n");
        sql.append("       SD.PART_CODE,\n");
        sql.append("       SD.UNIT,\n");
        sql.append("       SD.APPLY_QTY,\n");
        sql.append("       SD.CHECK_QTY,\n");
        sql.append("       SD.OUT_QTY,\n");
        sql.append("       SD.IN_QTY,\n");
        sql.append("       SD.REMARK,\n");
        sql.append("       SD.LOC_ID,\n");
        sql.append("       VS.LOC_CODE,\n");
        sql.append("       VS.NORMAL_QTY,\n");
        sql.append("       VS.ITEM_QTY,\n");
        sql.append("       VS.BOOKED_QTY,\n");
        sql.append("       VS.PART_ID, VS.PART_ID || '_' || VS.LOC_ID PART_IDS,\n");
        sql.append("       SD.OUT_QTY - SD.IN_QTY REM_QTY,\n");
        sql.append("       DECODE(SM.STATE,\n");
        sql.append("              92951001,\n");
        sql.append("              NVL(SD.APPLY_QTY, '0'),\n");
        sql.append("              NVL(SD.CHECK_QTY, '0')) PRINT_QTY\n");
        sql.append("  FROM TT_PART_STOCK_CHG_MIAN  SM,\n");
        sql.append("       TT_PART_STOCK_CHG_DTL   SD,\n");
        //        sql.append("       TT_PART_LOACTION_DEFINE LD,\n");
        sql.append("       VW_PART_STOCK           VS\n");
        sql.append(" WHERE SM.CHG_ID = SD.CHG_ID\n");
        //        sql.append("   AND SD.LOC_ID = LD.LOC_ID\n");
        sql.append("   AND SM.WH_ID = VS.WH_ID\n");
        sql.append("   AND SD.LOC_ID = VS.LOC_ID\n");
        sql.append("   AND SD.PART_ID = VS.PART_ID\n");

        sql.append("   AND SM.CHG_ID =" + chgId + "\n");
        if ("inStock".equals(optionType) && null != optionType) {
            sql.append("   AND SD.OUT_QTY > SD.IN_QTY\n");
        }
        sql.append("   ORDER BY SD.PART_OLDCODE\n");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * 
     * @param request
     * @param pageSize
     * @param curPage
     * @return
     */
    public PageResult<Map<String, Object>> queryMvDeatil2(RequestWrapper request, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        String chgId = CommonUtils.checkNull(request.getParamValue("CHG_ID")) == null
                ? CommonUtils.checkNull(request.getParamValue("CHG_ID"))
                : CommonUtils.checkNull(request.getParamValue("CHG_ID")); // 订单ID
        String optionType = CommonUtils.checkNull(request.getParamValue("optionType"));// 操作类型

        sql.append("SELECT SD.PART_OLDCODE,\n");
        sql.append("       SD.PART_CNAME,\n");
        sql.append("       SD.PART_CODE,\n");
        sql.append("       SD.UNIT,\n");
        sql.append("       SD.APPLY_QTY,\n");
        sql.append("       SD.CHECK_QTY,\n");
        sql.append("       SD.OUT_QTY,\n");
        sql.append("       SD.IN_QTY,\n");
        sql.append("       Sm.REMARK,\n");
        sql.append("       SD.LOC_ID,\n");
        sql.append("       --VS.LOC_CODE,\n");
        sql.append("       --VS.NORMAL_QTY,\n");
        sql.append("       --VS.ITEM_QTY,\n");
        sql.append("       --VS.BOOKED_QTY,\n");
        sql.append("       --VS.PART_ID,\n");
        sql.append("      -- VS.PART_ID || ',' || VS.LOC_ID PART_IDS,\n");
        sql.append("       SD.OUT_QTY - SD.IN_QTY REM_QTY,\n");
        sql.append("       DECODE(SM.STATE,\n");
        sql.append("              92951001,\n");
        sql.append("              NVL(SD.APPLY_QTY, '0'),\n");
        sql.append("              NVL(SD.CHECK_QTY, '0')) PRINT_QTY\n");
        sql.append("  FROM TT_PART_STOCK_CHG_MIAN SM, TT_PART_STOCK_CHG_DTL SD /*,\n");
        sql.append("       VW_PART_STOCK          VS*/\n");
        sql.append(" WHERE SM.CHG_ID = SD.CHG_ID\n");
        sql.append("      /* AND SM.WH_ID = VS.WH_ID(+)\n");
        sql.append("      AND SD.LOC_ID = VS.LOC_ID\n");
        sql.append("      AND SD.PART_ID = VS.PART_ID*/\n");
        sql.append("   AND SM.CHG_ID = " + chgId + "\n");
        if ("inStock".equals(optionType) && null != optionType) {
            sql.append("   AND SD.OUT_QTY > SD.IN_QTY\n");
        }
        sql.append("   ORDER BY SD.PART_OLDCODE\n");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * <p>
     * Description: 查询调拨单下的配件列表
     * </p>
     * 
     * @param request
     * @return
     */
    private String getMvPartsListSql(RequestWrapper request) {
        String chgId = CommonUtils.checkNull(request.getParamValue("CHG_ID")); // 订单ID
        String optionType = CommonUtils.checkNull(request.getParamValue("optionType"));// 操作类型
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT CONCAT(CONCAT(T2.PART_ID, ','), T2.LOC_ID) PART_LOC_ID, \n");
        sql.append("        T2.PART_ID, \n");
        sql.append("        T2.PART_OLDCODE, \n");
        sql.append("        T2.PART_CNAME, \n");
        sql.append("        T2.PART_CODE, \n");
        sql.append("        T2.UNIT, \n");
        sql.append("        T2.APPLY_QTY, \n");
        sql.append("        T2.CHECK_QTY, \n");
        sql.append("        T2.OUT_QTY, \n");
        sql.append("        T2.IN_QTY, \n");
        sql.append("        T2.OUT_QTY - T2.IN_QTY REM_QTY, \n");
        sql.append("        T2.REMARK, \n");
        sql.append("        T3.LOC_ID TOLOC_ID, \n");
        sql.append("        T3.LOC_CODE TOLOC_CODE, \n");
        sql.append("        T3.LOC_NAME TOLOC_NAME, \n");
        sql.append("        T4.LOC_ID, \n");
        sql.append("        T4.LOC_CODE, \n");
        sql.append("        T4.LOC_NAME, \n");
        sql.append("        DECODE(T1.STATE, \n");
        sql.append("               92951001, \n");
        sql.append("               NVL(T2.APPLY_QTY, '0'), \n");
        sql.append("               NVL(T2.CHECK_QTY, '0')) PRINT_QTY\n");
        sql.append("   FROM TT_PART_STOCK_CHG_MIAN T1 \n");
        sql.append("  INNER JOIN TT_PART_STOCK_CHG_DTL T2 \n");
        sql.append("     ON T1.CHG_ID = T2.CHG_ID \n");
        sql.append("   LEFT JOIN TT_PART_LOACTION_DEFINE T3 \n");
        //        sql.append("     ON T1.TOWH_ID = T3.WH_ID \n");
        //        sql.append("    AND T2.PART_ID = T3.PART_ID \n");
        sql.append("     ON T2.TOLOC_ID = T3.LOC_ID \n");
        //        sql.append("    AND T3.STATE = '"+Constant.STATUS_ENABLE+"' \n");
        //        sql.append("    AND T3.STATUS = '"+Constant.IF_TYPE_YES+"' \n");
        sql.append("  INNER JOIN TT_PART_LOACTION_DEFINE T4 \n");
        sql.append("     ON T2.LOC_ID = T4.LOC_ID \n");
        sql.append("  WHERE T1.CHG_ID = '" + chgId + "' \n");
        if ("inStock".equals(optionType) && null != optionType) {
            sql.append("   AND T2.OUT_QTY > T2.IN_QTY\n");
        }
        sql.append("   ORDER BY T2.PART_OLDCODE\n");
        return sql.toString();
    }

    /**
     * <p>
     * Description: 获取入库时的配件列表
     * </p>
     * 
     * @param request
     * @return
     */
    public List<Map<String, Object>> queryInStockDetial(RequestWrapper request) {
        List<Map<String, Object>> list = pageQuery(getMvPartsListSql(request), null, getFunName());
        return list;
    }

    /**
     * <p>
     * Description: 分页查询调拨单下的配件列表
     * </p>
     * 
     * @param request
     * @param pageSize 每页数量
     * @param curPage 页码
     * @return
     */
    public PageResult<Map<String, Object>> queryMvPartsPageList(RequestWrapper request, int pageSize, int curPage) {
        PageResult<Map<String, Object>> ps = pageQuery(getMvPartsListSql(request), null, getFunName(), pageSize,
                curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-23
     * @Title : 返回仓库配件库存信息
     */
    public PageResult<Map<String, Object>> showPartStockBase(RequestWrapper request, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
//        String parentOrgId = CommonUtils.checkNull(request.getParamValue("orgId"));// 当前机构ID
        String fromWhId = CommonUtils.checkNull(request.getParamValue("fromWhId"));//移出仓库ID
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));// 件号
        String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));// 配件编码
        String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));// 配件名称
//        String toWhId = CommonUtils.checkNull(request.getParamValue("toWhId"));// 移入仓库ID

        sql.append(" SELECT TD.*, TO_CHAR(NVL(P.SALE_PRICE1,'0'),'fm999,999,999,990.00') AS  REC_PRICE, ");
        sql.append(" TO_CHAR(NVL(P.SALE_PRICE2,'0'),'fm999,999,999,990.00') AS  RES_PRICE ");
        sql.append(" FROM VW_PART_STOCK TD, TT_PART_SALES_PRICE P ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_ID = P.PART_ID ");
//        if (null != parentOrgId && !"".equals(parentOrgId)) {
//            sql.append(" AND TD.ORG_ID = '" + parentOrgId + "' ");
//        }
        if (null != fromWhId && !"".equals(fromWhId)) {
            sql.append(" AND TD.WH_ID = '" + fromWhId + "' ");
        }
        if (null != partCode && !"".equals(partCode)) {
            sql.append(" AND upper(TD.PART_CODE) LIKE upper('%" + partCode + "%') ");
        }
        if (null != partOldcode && !"".equals(partOldcode)) {
            sql.append(" AND upper(TD.PART_OLDCODE) LIKE upper('%" + partOldcode + "%') ");
        }
        if (null != partCname && !"".equals(partCname)) {
            sql.append(" AND TD.PART_CNAME LIKE '%" + partCname + "%' ");
        }
//        if (null != toWhId && !"".equals(toWhId)) {
//            sql.append("AND TD.WH_ID = '"+toWhId+"'\n");
//        }

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param oemCompanyId
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-23
     * @Title : 返回主机厂名称
     */
    public String getMainCompanyName(String oemCompanyId) {
        String companyName = "";
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TM.COMPANY_NAME " + " FROM TM_COMPANY TM " + " WHERE 1 = 1  ");

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
     * @throws : LastDate : 2013-4-23
     * @Title : 返回服务商名称
     */
    public String getDealerName(String oemCompanyId) {
        String companyName = "";
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TD.DEALER_NAME " + " FROM TM_DEALER TD " + " WHERE 1 = 1  ");

        sql.append("  AND TD.DEALER_ID = '" + oemCompanyId + "' ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            companyName = list.get(0).get("DEALER_NAME").toString();
        }

        return companyName;
    }

    /**
     * 库存查询
     *
     * @param partId 配件ID
     * @param orgId 配件机构ID
     * @param whId 配件仓库ID
     * @param locId 配件货位ID
     * @return
     */
    public List getPartStockInfos(String partId, String orgId, String whId, String locId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append(
                "SELECT TD.*, SP.SALE_PRICE2 AS RES_PRICE, SP.SALE_PRICE1 AS REC_PRICE FROM VW_PART_STOCK TD, TT_PART_SALES_PRICE SP ");
        sql.append(" WHERE 1 = 1 ");
        if (!"".equals(partId) && null != partId) {
            sql.append(" AND TD.PART_ID = '" + partId + "'");
        }
        if (!"".equals(whId) && null != whId) {
            sql.append(" AND TD.WH_ID = '" + whId + "' ");
        }
        if (!"".equals(orgId) && null != orgId) {
            sql.append(" AND TD.ORG_ID = '" + orgId + "' ");
        }
        if (!"".equals(locId) && null != locId) {
            sql.append(" AND TD.LOC_ID = '" + locId + "' ");
        }
        sql.append(" AND TD.PART_ID = SP.PART_ID ");
        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    //状态更新
    public void updateMvState(String chgId) {
        StringBuffer sql = new StringBuffer("");

        sql.append("\n");
        sql.append("UPDATE TT_PART_STOCK_CHG_MIAN CM\n");
        sql.append("   SET CM.STATE = 92951005\n");
        sql.append(" WHERE EXISTS\n");
        sql.append(" (SELECT 1\n");
        sql.append("          FROM TT_PART_STOCK_CHG_DTL CD\n");
        sql.append("         WHERE CD.CHG_ID = CM.CHG_ID HAVING SUM(CD.OUT_QTY) = SUM(CD.IN_QTY))\n");
        if (!"".equals(chgId) && null != chgId) {
            sql.append("   AND CM.CHG_ID = " + chgId + "\n");
        } else {
            sql.append("   AND CM.CHG_ID = 0\n");
        }

        dao.update(sql.toString(), null);
    }

    /**
     * @param dbFlag 切换件更新后状态 0 未入库 、1已入库、2已出库、3资源锁定
     * @param dbId 切换件调拨单ID
     * @param dbState 切换件标标识
     * @param partCode 切换件编码
     * @param preDbFlag 切换件调拨前状态 0 未入库 、1已入库、2已出库、3资源锁定
     * @param Qty 切换件调拨数量
     * @param srcDbId 切换件调拨单原始ID
     */

    public void updateASdMvState(String dbFlag, String dbId, String dbState, String partCode, String preDbFlag,
            String Qty, String srcDbId) {
        StringBuffer sql = new StringBuffer("");

        sql.append("UPDATE TT_AS_WR_OLD_RETURNED_DETAIL DTL\n");
        sql.append("   SET DTL.KCDB_FLAG = " + dbFlag + ", DTL.KCDB_ID = " + dbId + "\n");
        sql.append(" WHERE EXISTS\n");
        sql.append(" (SELECT 1\n");
        sql.append("          FROM (SELECT *\n");
        sql.append("                  FROM (SELECT RD.ID, RD.PART_CODE, RD.IN_DATE\n");
        sql.append("                          FROM TT_AS_WR_OLD_RETURNED_DETAIL RD\n");
        sql.append("                         WHERE EXISTS (SELECT 1\n");
        sql.append("                                  FROM TT_PART_DEFINE D\n");
        sql.append("                                 WHERE D.PART_OLDCODE = RD.PART_CODE)\n");
        sql.append("                           AND RD.IS_MAIN_CODE = 94001001\n");
        //        sql.append("                           AND RD.IS_OUT = 0\n");
        sql.append("                           AND RD.SIGN_AMOUNT > 0\n");
        sql.append("                           AND RD.QHJ_FLAG = " + dbState + "\n");
        sql.append("                           AND RD.PART_CODE = '" + partCode + "'\n");
        sql.append("                           AND RD.KCDB_FLAG = " + preDbFlag + "\n");
        sql.append("                         ORDER BY RD.IN_DATE)\n");
        sql.append("                 WHERE ROWNUM <= " + Qty + ") X1\n");
        sql.append("         WHERE X1.ID = DTL.ID)\n");
        if (!"".equals(srcDbId) && null != srcDbId) {
            sql.append("   AND DTL.KCDB_ID ='" + srcDbId + "'\n");
        }

        dao.update(sql.toString(), null);
    }

    /**
     * <p>
     * Description: 获取有库房的经销商信息
     * </p>
     * 
     * @param paramMap 查询参数
     * @param pageSize 每页条数
     * @param curPage 页码
     * @return
     */
    public PageResult<Map<String, Object>> queryWhDealer(Map<String, String> paramMap, Integer pageSize,
            Integer curPage) {
        StringBuffer sql = new StringBuffer();
        sql.append(
                " SELECT T1.WH_ID, T1.WH_CODE, T1.WH_NAME, T1.ORG_ID, T2.DEALER_CODE ORG_CODE,T2.DEALER_NAME ORG_NAME\n");
        sql.append("   FROM TT_PART_WAREHOUSE_DEFINE T1 \n");
        sql.append("  INNER JOIN TM_DEALER T2 \n");
        sql.append("     ON T2.DEALER_ID = T1.ORG_ID \n");
        sql.append("    AND T1.WH_TYPE = 15021003 \n");
        sql.append("  WHERE T1.WH_TYPE = '" + Constant.PARTS_WAREHOUSE_TYPE_JXS + "'  \n");
        if (!CommonUtils.isEmpty(paramMap.get("ORG_CODE"))) {
            sql.append("    AND T1.ORG_CODE LIKE '%" + paramMap.get("ORG_CODE") + "%' \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("ORG_NAME"))) {
            sql.append("    AND T1.ORG_CODE LIKE '%" + paramMap.get("ORG_NAME") + "%' \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("NOT_EXISTS_ORG"))) {
            sql.append("    AND T1.ORG_ID <> '" + paramMap.get("NOT_EXISTS_ORG") + "' \n");
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }
    
    /**
     * <p>
     * Description: 获取调拨申请单下的配件
     * </p>
     * @param chgId
     * @return
     */
    public List<Map<String, Object>> getApplyChgPartList(String chgId){
        StringBuffer sql = new StringBuffer();
        sql.append("  \n");
        sql.append(" SELECT T1.ORG_ID, \n");
        sql.append("        T1.ORG_NAME, \n");
        sql.append("        T2.PART_ID, \n");
        sql.append("        T2.PART_OLDCODE, \n");
        sql.append("        T2.PART_CNAME, \n");
        sql.append("        T2.APPLY_QTY, \n");
        sql.append("        T3.NORMAL_QTY \n");
        sql.append("   FROM TT_PART_STOCK_CHG_MIAN T1 \n");
        sql.append("  INNER JOIN TT_PART_STOCK_CHG_DTL T2 \n");
        sql.append("     ON T1.CHG_ID = T2.CHG_ID \n");
        sql.append("  INNER JOIN TT_PART_BOOK T3 \n");
        sql.append("     ON T3.WH_ID = T1.WH_ID \n");
        sql.append("    AND T3.ORG_ID = T1.ORG_ID \n");
        sql.append("    AND T3.LOC_ID = T2.LOC_ID \n");
        sql.append("    AND T3.PART_ID = T2.PART_ID \n");
        sql.append("  WHERE T1.CHG_ID = '"+chgId+"' \n");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
    
}

