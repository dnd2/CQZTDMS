package com.infodms.dms.dao.parts.baseManager.partResAllotSet;

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
 * @ClassName : partResAllotSetDao
 */
public class partResAllotSetDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partResAllotSetDao.class);
    private static final partResAllotSetDao dao = new partResAllotSetDao();

    private partResAllotSetDao() {

    }

    public static final partResAllotSetDao getInstance() {
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
    public PageResult<Map<String, Object>> queryPartResAllotSet(String sqlStr, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT PD.PART_ID, PD.PART_CODE, PD.PART_OLDCODE, PD.PART_CNAME, RD.DEF_ID, TO_CHAR(RD.ALLOT_RATIO,'fm90.00') AS ALLOT_RATIO, RD.ALLOT_NUM, RD.BO_TOSALES, RD.STATE  ");
        sql.append(" FROM TT_PART_DEFINE PD, TT_PART_RESOURCEALLOT_DEFINE RD ");
        sql.append(" WHERE PD.IS_LACK = '" + Constant.IF_TYPE_YES + "'");
        sql.append(" AND PD.PART_ID = RD.PART_ID(+)");
        sql.append(sqlStr);
        sql.append(" ORDER BY PD.PART_OLDCODE, PD.PART_CNAME, PD.PART_CODE ");
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
    public List<Map<String, Object>> queryPartResAllotSetList(String sqlStr) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT PD.PART_ID, PD.PART_CODE, PD.PART_OLDCODE, PD.PART_CNAME, RD.DEF_ID, TO_CHAR(RD.ALLOT_RATIO,'fm90.00') AS ALLOT_RATIO, RD.ALLOT_NUM, RD.BO_TOSALES, RD.STATE ");
        sql.append(" FROM TT_PART_DEFINE PD, TT_PART_RESOURCEALLOT_DEFINE RD ");
        sql.append(" WHERE PD.IS_LACK = '" + Constant.IF_TYPE_YES + "'");
        sql.append(" AND PD.PART_ID = RD.PART_ID(+)");
        sql.append(sqlStr);
        sql.append(" ORDER BY PD.PART_OLDCODE, PD.PART_CNAME, PD.PART_CODE ");

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }


    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-6
     * @Title : 配件List
     */
    public List<Map<String, Object>> getPartList(String sqlStr) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT PD.* ");
        sql.append("  FROM TT_PART_DEFINE PD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
