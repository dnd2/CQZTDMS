package com.infodms.dms.dao.parts.storageManager.partStoInveManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : huchao
 *         CreateDate     : 2013-5-3
 * @ClassName : entityInvImpDao
 */
public class entityInvImpDao extends BaseDao {
    public static Logger logger = Logger.getLogger(entityInvImpDao.class);
    private static final entityInvImpDao dao = new entityInvImpDao();

    private entityInvImpDao() {
    }

    public static final entityInvImpDao getInstance() {
        return dao;
    }

    private static final int orderState1 = Constant.PART_INVE_ORDER_STATE_01;//已保存
    private static final int orderState4 = Constant.PART_INVE_ORDER_STATE_04;//已驳回
    private static final int state2 = Constant.PART_STOCK_INVE_STATE_02; //盘点中
    private static final int inveType1 = Constant.PART_STOCK_INVE_TYPE_01;//全部
    private static final int inveType2 = Constant.PART_STOCK_INVE_TYPE_02;//部分
    private static final int inveStatusAble = Constant.PART_STATUS_EN_ABLE; //盘点单可用
    private static final int inveStatusUnAble = Constant.PART_STATUS_UN_ABLE; //盘点单不可用
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
     * @throws : LastDate    : 2013-5-6
     * @Title : 配件库存盘点结果信息查询
     */
    public PageResult<Map<String, Object>> queryPartStockInve(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.RESULT_ID, TM.RESULT_CODE, TM.CHANGE_ID, TM.CHANGE_CODE, TM.CHGORG_CNAME, TM.WH_CNAME, U.NAME, TM.CREATE_DATE, TM.STATE, TM.CHECK_TYPE "
                        + "FROM TT_PART_CHECK_RESULT_MAIN TM, TC_USER U ");
        sql.append(" WHERE TM.CREATE_BY = U.USER_ID(+)  ");
        sql.append(" AND TM.STATE IN ('" + orderState1 + "','" + orderState4 + "') ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.CHANGE_CODE DESC, TM.RESULT_CODE DESC ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-6
     * @Title : 返回盘点中的单信息List
     */
    public List<Map<String, Object>> getInventories(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT * "
                        + " FROM TT_PART_CHECK_MAIN TM "
                        + " WHERE 1 = 1  AND TM.STATE = '" + state2 + "' ");
        sql.append(" AND TM.STATUS = '" + inveStatusAble + "' ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.CHANGE_CODE DESC ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
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
        sql.append(" AND TM.STATE = '" + enableValue + "' ");
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
     * @Title : 返回配件库存盘点结果信息List（带限制条件）
     */
    public List<Map<String, Object>> queryAllPartStockInve(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.RESULT_ID, TM.RESULT_CODE, TM.CHANGE_ID, TM.CHANGE_CODE, TM.CHGORG_CNAME, TM.WH_ID, TM.WH_CNAME, U.NAME, TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') AS CREATE_DATE, TM.CHECK_TYPE, TM.STATE "
                        + "FROM TT_PART_CHECK_RESULT_MAIN TM, TC_USER U ");
        sql.append(" WHERE TM.CREATE_BY = U.USER_ID(+) ");
        sql.append(" AND TM.STATE IN ('" + orderState1 + "','" + orderState4 + "') ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.CHANGE_CODE DESC, TM.RESULT_CODE DESC ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param dealerName
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回配件库存盘点结果信息List（无限制条件）
     */
    public List<Map<String, Object>> queryChkResList(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.RESULT_ID, TM.RESULT_CODE, TM.CHANGE_ID, TM.CHANGE_CODE, TM.CHGORG_CNAME, TM.WH_ID, TM.WH_CNAME, U.NAME, TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') AS CREATE_DATE, TM.CHECK_TYPE, TM.STATE "
                        + "FROM TT_PART_CHECK_RESULT_MAIN TM, TC_USER U ");
        sql.append(" WHERE TM.CREATE_BY = U.USER_ID(+) ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.CHANGE_CODE DESC, TM.RESULT_CODE DESC ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 配件库存盘点结果详细信息List
     */
    public List<Map<String, Object>> queryPartStockDeatil(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.RESULT_CODE,TM.CHANGE_CODE,TM.WH_CNAME, TD.DTL_ID, TD.PART_ID, TD.PART_CODE, TD.PART_OLDCODE,TD.PART_CNAME, TD.UNIT, TD.REMARK, TD.CHECK_QTY, TD.DIFF_QTY, TD.CHECK_RESULT, "
                        + " VM.ITEM_QTY, VM.NORMAL_QTY, VM.BOOKED_QTY, VM.ZCFC_QTY, VM.FC_QTY "
                        + " FROM TT_PART_CHECK_RESULT_DTL TD, TT_PART_CHECK_RESULT_MAIN TM, VW_PART_STOCK VM ");
        sql.append(" WHERE 1 = 1 AND TM.RESULT_ID = TD.RESULT_ID ");
        sql.append(" AND TM.CHGORG_ID = VM.ORG_ID ");
        sql.append(" AND TM.WH_ID = VM.WH_ID ");
        sql.append(" AND TD.PART_ID = VM.PART_ID ");
        sql.append(sbString);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 配件库存盘点结果详细信息查询
     */
    public PageResult<Map<String, Object>> queryPartStockDeatil(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TM.RESULT_CODE,TM.CHANGE_CODE,TM.WH_CNAME, TD.DTL_ID, TD.PART_ID, TD.PART_CODE, TD.PART_OLDCODE,"
                + "TD.PART_CNAME, TD.UNIT, TD.VENDER_ID, TD.BATCH_CODE, TD.LOC_ID, TD.LOC_CODE, TD.LOC_NAME, TD.REMARK, TD.CHECK_QTY, TD.DIFF_QTY, TD.CHECK_RESULT, "
                + " VM.ITEM_QTY, VM.NORMAL_QTY, VM.BOOKED_QTY, VM.ZCFC_QTY, VM.FC_QTY "
                + " FROM TT_PART_CHECK_RESULT_DTL TD, TT_PART_CHECK_RESULT_MAIN TM, VW_PART_STOCK VM ");
        sql.append(" WHERE 1 = 1 AND TM.RESULT_ID = TD.RESULT_ID ");
        sql.append(" AND TM.CHGORG_ID = VM.ORG_ID ");
        sql.append(" AND TM.WH_ID = VM.WH_ID ");
        sql.append(" AND TD.PART_ID = VM.PART_ID ");
        sql.append(" AND TD.LOC_ID = VM.LOC_ID ");
        sql.append(sbString);

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
     * @throws : LastDate    : 2013-5-
     * @Title : 返回仓库配件库存信息
     */
    public PageResult<Map<String, Object>> showPartStockBase(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM VW_PART_STOCK TD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-4
     * @Title : 返回仓库配件库存信息LIST
     */
    public List<Map<String, Object>> showPartStockBase(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM VW_PART_STOCK TD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);

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

    /**
     * @param : @param delCode
     * @param : @return
     * @return :
     * LastDate    : 2013-5-3
     * @Title : 验证服配件编码是否存在 并返回配件ID、Name
     * @Description:
     */
    public List<Map<String, Object>> checkOldCode(String oldCode) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT TD.PART_ID,TD.PART_CODE FROM TT_PART_DEFINE TD " +
                " WHERE TD.PART_OLDCODE = '" + oldCode + "' ";
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }

    /**
     * @param : @param oldCode
     * @param : @param inveType
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-6
     * @Title : 检验导入配件是否在盘点范围内
     */
    public List<Map<String, Object>> checkPartWhInInve(String oldCode, int inveType, String inveId, String locCode) {
        List<Map<String, Object>> list = null;
        String sql = null;
        if (inveType1 == inveType) {
            sql = "SELECT TM.CHANGE_ID FROM TT_PART_CHECK_MAIN TM,  VW_PART_STOCK VM " +
                    " WHERE 1 = 1 AND TM.CHGORG_ID = VM.ORG_ID AND TM.WH_ID = VM.WH_ID " +
                    " AND VM.PART_OLDCODE = '" + oldCode + "' " +
                    " AND TM.CHANGE_ID = '" + inveId + "' " +
                    " AND VM.LOC_CODE = '" + locCode + "'";
        } else if (inveType2 == inveType) {
            sql = "SELECT TM.CHANGE_ID FROM TT_PART_CHECK_MAIN TM, TT_PART_CHECK_DTL TD " +
                    " WHERE 1 = 1 AND TM.CHANGE_ID = TD.CHECK_ID " +
                    " AND TD.PART_OLDCODE = '" + oldCode + "' " +
                    " AND TM.CHANGE_ID = '" + inveId + "' "+
                    " AND TD.LOC_CODE = '" + locCode + "'";
        }
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
     * @param : @param oldCode
     * @param : @param parentOrgId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-25
     * @Title : 返回精确的配件库存状态信息
     */
    public List<Map<String, Object>> getPartStockInfos(String oldCode, String parentOrgId, String whId, String locCode) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM VW_PART_STOCK TD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_OLDCODE = '" + oldCode + "' AND TD.ORG_ID = '" + parentOrgId + "' ");
        sql.append(" AND TD.WH_ID = '" + whId + "' ");
        sql.append(" AND TD.LOC_CODE = '" + locCode + "' ");
        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-6
     * @Title : 返回盘点单ID
     */
    public List<Map<String, Object>> getInveId(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT * "
                        + " FROM TT_PART_CHECK_RESULT_MAIN TM "
                        + " WHERE 1 = 1 ");
        sql.append(sbString);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
