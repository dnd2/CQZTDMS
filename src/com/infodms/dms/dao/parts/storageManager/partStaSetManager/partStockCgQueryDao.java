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
 * @ClassName : partStockCgQueryDao
 */
public class partStockCgQueryDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partStockCgQueryDao.class);
    private static final partStockCgQueryDao dao = new partStockCgQueryDao();

    private partStockCgQueryDao() {
    }

    public static final partStockCgQueryDao getInstance() {
        return dao;
    }

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
     * @throws : LastDate    : 2013-4-22
     * @Title : 库存状态变更信息查询
     */
    public PageResult<Map<String, Object>> queryPartStockStuatus(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TM.*, U.NAME ");
        sql.append( "FROM TT_PART_CHG_STATE_MAIN TM, TC_USER U ");
        sql.append(" WHERE TM.CREATE_BY = U.USER_ID ");
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
     * @throws : LastDate    : 2013-4-15
     * @Title : 返回库存状态变更信息List
     */
    public List<Map<String, Object>> queryAllPartStockStatus(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.*, U.NAME "
                        + "FROM TT_PART_CHG_STATE_MAIN TM, TC_USER U ");
        sql.append(" WHERE TM.CREATE_BY = U.USER_ID");
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
     * @throws : LastDate    : 2013-7-1
     * @Title : 汇总查询
     */
    public PageResult<Map<String, Object>> staSetCntQuery(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TD.*, TL.LOC_NAME, (NVL(TD.RETURN_QTY,'0') - NVL(TD.COLSE_QTY, '0')) AS UNCLS_QTY, TM.WH_CNAME, U.NAME, PB.NORMAL_QTY "
                        + " FROM TT_PART_CHG_STATE_DTL TD, TT_PART_CHG_STATE_MAIN TM, TC_USER U, TT_PART_BOOK PB, TT_PART_LOACTION_DEFINE TL  ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.CHANGE_ID = TM.CHANGE_ID ");
        sql.append(" AND TM.CREATE_BY = U.USER_ID(+) ");
        sql.append(" AND TM.WH_ID = PB.WH_ID ");
        sql.append(" AND TD.PART_ID = PB.PART_ID ");
        sql.append(" AND TD.LOC_ID = PB.LOC_ID ");
        sql.append(" AND TD.LOC_ID = TL.LOC_ID ");
        sql.append(sbString);
        sql.append(" ORDER BY TD.CREATE_DATE DESC, TD.PART_OLDCODE, TD.PART_CNAME, TD.PART_CODE ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-1
     * @Title : 汇总查询List
     */
    public List<Map<String, Object>> staSetCntQuyList(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TD.*, (NVL(TD.RETURN_QTY,'0') - NVL(TD.COLSE_QTY, '0')) AS UNCLS_QTY, TM.WH_CNAME, "
                        + " TO_CHAR(TD.CREATE_DATE,'yyyy-MM-dd') AS CREATE_DATE_FM, TO_CHAR(TD.UPDATE_DATE,'yyyy-MM-dd') AS UPDATE_DATE_FM, U.NAME, PB.NORMAL_QTY "
                        + " FROM TT_PART_CHG_STATE_DTL TD, TT_PART_CHG_STATE_MAIN TM, TC_USER U, TT_PART_BOOK PB ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.CHANGE_ID = TM.CHANGE_ID ");
        sql.append(" AND TM.CREATE_BY = U.USER_ID(+) ");
        sql.append(" AND TM.WH_ID = PB.WH_ID ");
        sql.append(" AND TD.PART_ID = PB.PART_ID ");
        sql.append(sbString);
        sql.append(" ORDER BY TD.CREATE_DATE DESC, TD.PART_OLDCODE, TD.PART_CNAME, TD.PART_CODE ");
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
     * @Title : 库存状态变更详细信息查询
     */
    public PageResult<Map<String, Object>> queryPartStockDeatil(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TD.DTL_ID, TD.PART_CODE, TD.PART_OLDCODE,TD.PART_CNAME, TD.STOCK_QTY, TD.CHANGE_REASON, TD.CHANGE_TYPE, TD.RETURN_QTY, TD.REMARK "
                        + " FROM TT_PART_CHG_STATE_DTL TD ");
        sql.append(" WHERE 1 = 1 ");
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
     * @throws : LastDate    : 2013-4-23
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
    public List checkOldCode(String oldCode) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT TD.PART_ID,TD.PART_CODE FROM TT_PART_DEFINE TD " +
                " WHERE  TD.PART_OLDCODE = '" + oldCode + "' ";
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
    public List getPartStockInfos(String oldCode, String parentOrgId) {
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
    public List getPartStockInfos(String oldCode, String parentOrgId, String whId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM VW_PART_STOCK TD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_OLDCODE = '" + oldCode + "' AND TD.ORG_ID = '" + parentOrgId + "' ");
        sql.append(" AND TD.WH_ID = '" + whId + "' ");
        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }
}
