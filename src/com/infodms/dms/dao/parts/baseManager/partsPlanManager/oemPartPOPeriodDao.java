package com.infodms.dms.dao.parts.baseManager.partsPlanManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Yu
 * Date: 13-7-7
 * Time: 下午2:31
 * To change this template use File | Settings | File Templates.
 */
public class oemPartPOPeriodDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partPlanSortDao.class);
    private static final oemPartPOPeriodDao dao = new oemPartPOPeriodDao();

    private oemPartPOPeriodDao() {
    }

    public static final oemPartPOPeriodDao getInstance() {
        return dao;
    }

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public List<Map<String, Object>> queryoemPartPOPeriod() {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT PS.*  FROM TT_PART_PERIOD_DEFINE PS ");
        sql.append("WHERE 1=1 and PERIOD_TYPE = 95601002");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public List<Map<String, Object>> queryoemRepartPOPeriod() {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT PS.*  FROM TT_PART_PERIOD_DEFINE PS ");
        sql.append("WHERE 1=1 and PERIOD_TYPE = 95601001");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
}

