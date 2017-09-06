package com.infodms.dms.dao.parts.baseManager.partInnerOrgManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-4-10
 * @ClassName : partInnerOrgDao
 */
public class partInnerOrgDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partInnerOrgDao.class);
    private static final partInnerOrgDao dao = new partInnerOrgDao();

    private partInnerOrgDao() {
    }

    public static final partInnerOrgDao getInstance() {
        return dao;
    }

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param sqlStr
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-6
     * @Title : 条件查询配件直发设置信息
     */
    public PageResult<Map<String, Object>> queryPartInnerOrg(String sqlStr, String orgId, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        if (Constant.OEM_ACTIVITIES.equals(orgId)) {
            sql.append("SELECT IO.*, U.NAME, ORG.ORG_DESC AS ORG_NAME FROM TT_PART_INNER_ORG IO, TC_USER U, TM_ORG ORG ");
            sql.append(" WHERE 1 = 1 ");
            sql.append(" AND IO.UPDATE_BY = U.USER_ID(+) ");
            sql.append(" AND IO.PRT_ORG_ID = ORG.ORG_ID(+) ");
        } else {
            sql.append("SELECT IO.*, U.NAME, TD.DEALER_NAME AS ORG_NAME FROM TT_PART_INNER_ORG IO, TC_USER U, TM_DEALER TD ");
            sql.append(" WHERE 1 = 1 ");
            sql.append(" AND IO.UPDATE_BY = U.USER_ID(+) ");
            sql.append(" AND IO.PRT_ORG_ID = TD.DEALER_ID(+) ");
        }
        sql.append(sqlStr);
        sql.append(" ORDER BY IO.IN_ORG_CODE DESC ,IO.IN_ORG_NAME DESC,IO.CREATE_DATE DESC  ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-6
     * @Title : 配件直发设置信息 List
     */
    public List<Map<String, Object>> queryPartInnerOrgList(String sqlStr, String orgId) {
        StringBuffer sql = new StringBuffer("");

        if (Constant.OEM_ACTIVITIES.equals(orgId)) {
            sql.append("SELECT IO.*, U.NAME, ORG.ORG_DESC AS ORG_NAME FROM TT_PART_INNER_ORG IO, TC_USER U, TM_ORG ORG ");
            sql.append(" WHERE 1 = 1 ");
            sql.append(" AND IO.UPDATE_BY = U.USER_ID(+) ");
            sql.append(" AND IO.PRT_ORG_ID = ORG.ORG_ID(+) ");
        } else {
            sql.append("SELECT IO.*, U.NAME, TD.DEALER_NAME AS ORG_NAME FROM TT_PART_INNER_ORG IO, TC_USER U, TM_DEALER TD ");
            sql.append(" WHERE 1 = 1 ");
            sql.append(" AND IO.UPDATE_BY = U.USER_ID(+) ");
            sql.append(" AND IO.PRT_ORG_ID = TD.DEALER_ID(+) ");
        }
        sql.append(sqlStr);
        sql.append(" ORDER BY IO.IN_ORG_CODE, IO.IN_ORG_NAME ");

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
