package com.infodms.dms.dao.parts.baseManager.partsPlanManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-7-5
 * @ClassName : partPlanSortDao
 */
public class partPlanSortDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partPlanSortDao.class);
    private static final partPlanSortDao dao = new partPlanSortDao();

    private partPlanSortDao() {
    }

    public static final partPlanSortDao getInstance() {
        return dao;
    }

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public List<Map<String, Object>> queryPartPlanSort() {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT PS.*  FROM TT_PART_PALNSORT_DEFINE PS ");
        sql.append("WHERE 1=1 ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
