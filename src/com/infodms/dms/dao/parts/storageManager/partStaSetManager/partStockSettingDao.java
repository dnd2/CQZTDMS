package com.infodms.dms.dao.parts.storageManager.partStaSetManager;

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
 *         CreateDate     : 2013-4-22
 * @ClassName : partStockSettingDao
 */
public class partStockSettingDao extends BaseDao {
    private static final Integer bzType1 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01;
    private static final Integer bzType2 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02;
    private static final Integer bzType3 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03;
    private static final int enableValue = Constant.STATUS_ENABLE;//有效
    public static Logger logger = Logger.getLogger(partStockSettingDao.class);
    private static final partStockSettingDao dao = new partStockSettingDao();

    private partStockSettingDao() {
    }

    public static final partStockSettingDao getInstance() {
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
     * @throws : LastDate    : 2013-4-22
     * @Title : 配件库存状态变更信息查询
     */
    public PageResult<Map<String, Object>> queryPartStockStuatus(String sbString, String sqlStr, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.*, U.NAME "
                        + "FROM TT_PART_CHG_STATE_MAIN TM, TC_USER U, ");
        sql.append(" (SELECT TD.CHANGE_ID FROM TT_PART_CHG_STATE_DTL TD WHERE 1 = 1 ");
        sql.append(sqlStr);
        sql.append(" GROUP BY TD.CHANGE_ID) TDD ");
        sql.append(" WHERE TM.CREATE_BY = U.USER_ID  AND TM.CHG_TYPE = '" + bzType1 + "' ");
        sql.append(" AND TM.CHANGE_ID = TDD.CHANGE_ID ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.CREATE_DATE DESC");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-22
     * @Title : 返回仓库信息List
     */
    public List<Map<String, Object>> getWareHouses(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.WH_ID, TM.WH_CODE, TM.WH_NAME AS WH_CNAME "
                        + " FROM TT_PART_WAREHOUSE_DEFINE TM "
                        + " WHERE 1 = 1  ");
        sql.append(" AND TM.STATE = '" + enableValue + "' ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.WH_ID ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
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
    public List<Map<String, Object>> getWareLocaInfos(String locId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT LD.LOC_ID, LD.LOC_CODE, WD.WH_ID, WD.WH_NAME, WD.WH_CODE\n");
        sql.append("  FROM TT_PART_LOACTION_DEFINE LD, TT_PART_WAREHOUSE_DEFINE WD\n");
        sql.append(" WHERE LD.WH_ID = WD.WH_ID\n");
        sql.append("   AND LD.LOC_ID = " + locId);
        list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param dealerName
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title : 返回配件库存状态变更信息List
     */
    public List<Map<String, Object>> queryAllPartStockStatus(String sbString, String sqlStr) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TM.*, U.NAME "
                        + "FROM TT_PART_CHG_STATE_MAIN TM, TC_USER U, ");
        sql.append(" (SELECT TD.CHANGE_ID FROM TT_PART_CHG_STATE_DTL TD WHERE 1 = 1 ");
        sql.append(sqlStr);
        sql.append(" GROUP BY TD.CHANGE_ID) TDD ");
        sql.append(" WHERE TM.CREATE_BY = U.USER_ID AND TM.CHG_TYPE = '" + bzType1 + "' ");
        sql.append(" AND TM.CHANGE_ID = TDD.CHANGE_ID ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.CREATE_DATE DESC");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-22
     * @Title : 配件库存状态变更详细信息查询
     */
    public PageResult<Map<String, Object>> queryPartStockDeatil(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT TD.*,\n");
        sql.append("       LD.LOC_CODE,\n");
        sql.append("       (NVL(TD.RETURN_QTY, '0') - NVL(TD.COLSE_QTY, '0')) AS UNCLS_QTY\n");
        sql.append("  FROM TT_PART_CHG_STATE_DTL TD, TT_PART_LOACTION_DEFINE LD\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND TD.LOC_ID = LD.LOC_ID");

        sql.append(sbString);

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-9-25
     * @Title : 配件库存状态变更详细信息List
     */
    public List<Map<String, Object>> queryPartStockDeatil(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TD.*, (NVL(TD.RETURN_QTY,'0') - NVL(TD.COLSE_QTY, '0')) AS UNCLS_QTY "
                        + " FROM TT_PART_CHG_STATE_DTL TD ");
        sql.append(" WHERE 1 = 1 ");
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
     * @throws : LastDate    : 2013-4-23
     * @Title : 返回仓库配件库存信息
     */
    public PageResult<Map<String, Object>> showPartStockBase(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM VW_PART_STOCK TD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);
//		sql.append(" ORDER BY TD.ZCFC_QTY DESC, TD.PART_OLDCODE ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
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
     * @param : @param delCode
     * @param : @return
     * @return :
     * LastDate    : 2013-4-15
     * @Title : 验证服务商编码是否存在 并返回服务商ID、Name
     * @Description:
     */
    public List<Map<String, Object>> checkOldCode(String oldCode) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT TD.PART_ID,TD.PART_CODE FROM TT_PART_DEFINE TD " +
                " WHERE TD.PART_OLDCODE = '" + oldCode + "' ";
        list = pageQuery(sql, null, getFunName());
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
        list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param oldCode
     * @param parentOrgId
     * @param whId
     * @param locId
     * @return
     */
    public List<Map<String, Object>> getPartStockInfos(String oldCode, String parentOrgId, String whId, String locCode, String batchNo) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM VW_PART_STOCK TD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_OLDCODE = '" + oldCode + "' AND TD.ORG_ID = '" + parentOrgId + "' ");
        sql.append(" AND TD.WH_ID = '" + whId + "' ");
        sql.append(" AND TD.LOC_CODE = '" + locCode + "' ");
        sql.append(" AND TD.BATCH_NO = '" + batchNo + "' ");
        list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public List<Map<String, Object>> getPartStockInfos2(String oldCode, String parentOrgId, String whId, String locId, String batchNo) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM VW_PART_STOCK TD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_OLDCODE = '" + oldCode + "' AND TD.ORG_ID = '" + parentOrgId + "' ");
        sql.append(" AND TD.WH_ID = '" + whId + "' " + " AND TD.LOC_ID = " + locId);
        sql.append(" AND TD.BATCH_NO = '" + batchNo + "' ");
        list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
