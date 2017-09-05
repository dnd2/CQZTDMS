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
 * @ClassName : invReqCheckDao
 */
public class invReqCheckDao extends BaseDao {
    public static Logger logger = Logger.getLogger(invReqCheckDao.class);
    private static final invReqCheckDao dao = new invReqCheckDao();

    private invReqCheckDao() {
    }

    public static final invReqCheckDao getInstance() {
        return dao;
    }

    private static final int orderState2 = Constant.PART_INVE_ORDER_STATE_02;//审核中
    private static final int state2 = Constant.PART_STOCK_INVE_STATE_02; //盘点中
    private static final int inveType1 = Constant.PART_STOCK_INVE_TYPE_01;//全部
    private static final int inveType2 = Constant.PART_STOCK_INVE_TYPE_02;//部分
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
                .append("SELECT TM.RESULT_ID,TM.RESULT_CODE, TM.CHANGE_ID, TM.CHANGE_CODE, TM.CHGORG_CNAME, TM.WH_CNAME, LU.NAME AS IMP_NAME,CU.NAME AS COMM_NAME, TM.CREATE_DATE, TM.COMMIT_DATE, TM.STATE, TM.CHECK_TYPE "
                        + "FROM TT_PART_CHECK_RESULT_MAIN TM, TC_USER LU, TC_USER CU");
        sql.append(" WHERE TM.CREATE_BY = LU.USER_ID(+)  ");
        sql.append(" AND TM.COMMIT_BY = CU.USER_ID(+)  ");
        sql.append(" AND TM.STATE IN ('" + orderState2 + "') ");
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
        sql.append(" AND TM.STATUS = '1' ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.CHANGE_CODE ");
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
     * @Title : 返回配件库存盘点结果信息List
     */
    public List<Map<String, Object>> queryAllPartStockInve(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.RESULT_ID,TM.RESULT_CODE, TM.CHANGE_ID, TM.CHANGE_CODE, TM.CHGORG_CNAME, TM.WH_ID, TM.WH_CNAME, LU.NAME AS IMP_NAME, CU.NAME AS COMM_NAME, TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') AS CREATE_DATE,TO_CHAR(TM.COMMIT_DATE,'yyyy-MM-dd') AS COMMIT_DATE, TM.CHECK_TYPE, TM.STATE "
                        + "FROM TT_PART_CHECK_RESULT_MAIN TM, TC_USER LU, TC_USER CU ");
        sql.append(" WHERE TM.CREATE_BY = LU.USER_ID(+) ");
        sql.append(" AND TM.COMMIT_BY = CU.USER_ID(+)  ");
        sql.append(" AND TM.STATE IN ('" + orderState2 + "') ");
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
                .append("SELECT TM.RESULT_CODE, TM.CHANGE_CODE,TM.WH_CNAME, TD.DTL_ID, TD.PART_ID, TD.PART_CODE, TD.PART_OLDCODE,TD.PART_CNAME, TD.UNIT, TD.REMARK, TD.CHECK_QTY, TD.DIFF_QTY, TD.CHECK_RESULT, "
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
        sql.append("SELECT TM.RESULT_CODE, TM.CHANGE_CODE,TM.WH_CNAME, TD.DTL_ID, TD.PART_ID, TD.PART_CODE, TD.PART_OLDCODE,"
                        + "TD.PART_CNAME, TD.UNIT, TD.VENDER_ID, TD.BATCH_CODE, TD.REMARK, TD.CHECK_QTY, TD.DIFF_QTY, TD.CHECK_RESULT, "
                        + "TD.LOC_ID, TD.LOC_CODE, TD.LOC_NAME,  VM.ITEM_QTY, VM.NORMAL_QTY, VM.BOOKED_QTY, VM.ZCFC_QTY, VM.FC_QTY "
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

}
