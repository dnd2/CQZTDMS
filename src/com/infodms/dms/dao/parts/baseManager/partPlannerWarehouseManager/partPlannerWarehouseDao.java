package com.infodms.dms.dao.parts.baseManager.partPlannerWarehouseManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-4-10
 * @ClassName : partPlannerWarehouseDao
 */
public class partPlannerWarehouseDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partPlannerWarehouseDao.class);
    private static final partPlannerWarehouseDao dao = new partPlannerWarehouseDao();

    private partPlannerWarehouseDao() {
    }

    public static final partPlannerWarehouseDao getInstance() {
        return dao;
    }

    private static final int enableValue = Constant.STATUS_ENABLE;//有效

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param orderID
     * @param : @param partCode
     * @param : @param partName
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-8
     * @Title : 条件查询计划员与仓库信息
     */
    public PageResult<Map<String, Object>> queryPartPlannerWarehouse(String plannerName,
                                                                     String WHName, String state, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT PW.RELATION_ID, PW.PLANER_ID, U.NAME, PW.WH_ID, W.WH_NAME, PW.STATE " +
                "FROM TC_USER U, TT_PART_WAREHOUSE_DEFINE W, TT_PART_PLANER_WH_RELATION PW " +
                "WHERE PW.PLANER_ID = U.USER_ID(+) AND PW.WH_ID = W.WH_ID(+)");
        sql.append(" AND W.STATE = '" + enableValue + "' ");
        sql.append(" AND U.USER_STATUS = '" + enableValue + "' ");
        if (null != plannerName && !plannerName.equals("")) {
            sql.append(" AND U.NAME  like '%" + plannerName + "%' ");
        }

        if (null != WHName && !WHName.equals("")) {
            sql.append(" AND W.WH_NAME like  '%" + WHName + "%' ");
        }
        if (null != state && !state.equals("")) {
            sql.append(" AND PW.STATE = " + Integer.parseInt(state) + "");
        }
        sql.append(" ORDER BY pw.planer_id,pw.wh_id");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param orderID
     * @param : @param partCode
     * @param : @param partName
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-8
     * @Title : 条件查询计划员与仓库信息
     */
    public PageResult<Map<String, Object>> queryPartSinglePlannerWarehouse(String plannerID, String whName, String state, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT PW.RELATION_ID, PW.PLANER_ID, PW.WH_ID, W.WH_NAME, PW.STATE " +
                "FROM  TT_PART_WAREHOUSE_DEFINE W, TT_PART_PLANER_WH_RELATION PW " +
                "WHERE PW.WH_ID = W.WH_ID(+)");
        sql.append(" AND W.STATE = '" + enableValue + "' ");
//        sql.append("WHERE 1=1 ");
        if (null != plannerID && !plannerID.equals("")) {
            sql.append(" AND PW.PLANER_ID = " + Long.parseLong(plannerID));
        }

        if (null != whName && !whName.equals("")) {
            sql.append(" AND W.WH_NAME LIKE '%" + whName + "%' ");
        }

        if (null != state && !state.equals("")) {
            sql.append(" AND PW.STATE = " + Long.parseLong(state));
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }


    /**
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @param whereSql
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 返回所有仓库信息
     */
    public PageResult<Map<String, Object>> getAllWarehouse(String sbStr, int pageSize, int curPage) {
        PageResult<Map<String, Object>> result = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT W.WH_ID, W.WH_NAME\n");
        sql.append("  FROM TT_PART_WAREHOUSE_DEFINE W\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append(" AND W.STATE = '" + enableValue + "' ");
        sql.append(sbStr);

        result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return result;
    }

    /**
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @param whereSql
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 返回所有计划员信息
     */
    public PageResult<Map<String, Object>> getAllPlanner(int pageSize, int curPage, String sbString) {
        PageResult<Map<String, Object>> result = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT U.USER_ID, U.NAME " +
                "FROM TC_USER U, TT_PART_USERPOSE_DEFINE PU, TT_PART_FIXCODE_DEFINE PF " +
                "WHERE PU.USER_ID = U.USER_ID(+) AND  PF.FIX_VALUE = PU.USER_TYPE  " +
                "AND PU.STATE = '" + enableValue + "' ");
        sql.append(sbString);

        result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return result;
    }

    /**
     * @param : @param partId
     * @param : @param partCode
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title : 验证计划员与仓库关系记录是否存在
     */
    @SuppressWarnings("unchecked")
    public List<TtPartWarehouseDefinePO> getExistPO(String plannerID, String whId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT W.WH_NAME FROM TT_PART_PLANER_WH_RELATION T, TT_PART_WAREHOUSE_DEFINE W\n");
        sql.append(" WHERE 1= 1 AND T.WH_ID = W.WH_ID(+)\n");
        sql.append(" AND T.PLANER_ID = '" + Long.parseLong(plannerID) + "'\n");
        sql.append(" AND T.WH_ID = '" + Long.parseLong(whId) + "'");
        return dao.select(TtPartWarehouseDefinePO.class, sql.toString(), null);
    }


}
