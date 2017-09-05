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
 * @ClassName : avgSaleParmsSetDao
 */
public class avgSaleParmsSetDao extends BaseDao {
    public static Logger logger = Logger.getLogger(avgSaleParmsSetDao.class);
    private static final avgSaleParmsSetDao dao = new avgSaleParmsSetDao();

    private avgSaleParmsSetDao() {
    }

    public static final avgSaleParmsSetDao getInstance() {
        return dao;
    }

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-5
     * @Title : 获取平均销量权重信息
     */
    public List<Map<String, Object>> queryAvgSaleParms() {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT PW.* FROM TT_PART_PLANWEIGHT_DEFINE PW ");
        sql.append("WHERE 1=1 ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
