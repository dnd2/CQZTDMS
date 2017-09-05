package com.infodms.dms.dao.parts.baseManager.partDlrBODateManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : huchao
 *         CreateDate     : 2013-7-2
 * @ClassName : dlrBoDateSetDao
 */
public class dlrBoDateSetDao extends BaseDao {
    public static Logger logger = Logger.getLogger(dlrBoDateSetDao.class);
    private static final dlrBoDateSetDao dao = new dlrBoDateSetDao();

    private dlrBoDateSetDao() {
    }

    public static final dlrBoDateSetDao getInstance() {
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
     * @throws : LastDate    : 2013-7-2
     * @Title : 配件采购关系属性查询
     */
    public PageResult<Map<String, Object>> querySaleRelation(String sqlStr, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT SA.* ");
        sql.append(" FROM TT_PART_SALES_RELATION SA ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);
        sql.append(" ORDER BY SA.CREATE_DATE ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-2
     * @Title : 配件采购关系属性查询List
     */
    public List<Map<String, Object>> querySaleRelationList(String sqlStr) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT SA.* ");
        sql.append(" FROM TT_PART_SALES_RELATION SA ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);
        sql.append(" ORDER BY SA.CREATE_DATE ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
}
