package com.infodms.dms.dao.parts.baseManager.partPkgStkManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-7-26
 * @ClassName : partPkgStkDao
 */
public class partPkgStkDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partPkgStkDao.class);
    private static final partPkgStkDao dao = new partPkgStkDao();

    private partPkgStkDao() {

    }

    public static final partPkgStkDao getInstance() {
        return dao;
    }

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param partCode
     * @param : @param partOldcode
     * @param : @param partCname
     * @param : @param name
     * @param : @param state
     * @param : @param isDirect
     * @param : @param isLack
     * @param : @param isPlan
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-26
     * @Title : 配件包装储运维护查询
     */
    public PageResult<Map<String, Object>> queryPkgStk(String partCode,
                                                       String partOldcode, String partCname, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME, T.OEM_MIN_PKG, T.MIN_PKG, T.PKG_SIZE FROM TT_PART_DEFINE T ");
        sql.append(" WHERE 1 = 1 ");

        if (null != partCode && !partCode.equals("")) {
            sql.append(" and UPPER(T.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
        }

        if (null != partOldcode && !partOldcode.equals("")) {
            sql.append(" and UPPER(T.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
        }
        if (null != partCname && !partCname.equals("")) {
            sql.append(" and UPPER(T.PART_CNAME) LIKE '%" + partCname.toUpperCase() + "%'");
        }

        sql.append(" ORDER BY T.PART_OLDCODE, T.PART_CNAME, T.PART_CODE ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param partCode
     * @param : @param partOldcode
     * @param : @param partCname
     * @param : @param name
     * @param : @param state
     * @param : @param isDirect
     * @param : @param isLack
     * @param : @param isPlan
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 配件包装储运维护List
     */
    public List<Map<String, Object>> partPkgStkList(String partCode, String partOldcode, String partCname) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME, T.OEM_MIN_PKG, T.MIN_PKG, T.PKG_SIZE FROM TT_PART_DEFINE T ");
        sql.append(" WHERE 1 = 1 ");

        if (null != partCode && !partCode.equals("")) {
            sql.append(" and UPPER(T.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
        }

        if (null != partOldcode && !partOldcode.equals("")) {
            sql.append(" and UPPER(T.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
        }
        if (null != partCname && !partCname.equals("")) {
            sql.append(" and UPPER(T.PART_CNAME) LIKE '%" + partCname.toUpperCase() + "%'");
        }

        sql.append(" ORDER BY T.PART_OLDCODE, T.PART_CNAME, T.PART_CODE ");

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }


    /**
     * @param : @param delCode
     * @param : @return
     * @return :
     * LastDate    : 2013-5-3
     * @Title : 验证配件编码是否存在 并返回配件ID、Name
     * @Description:
     */
    public List<Map<String, Object>> checkOldCode(String oldCode) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT TD.PART_ID,TD.PART_CODE, TD.PART_OLDCODE, TD.PART_CNAME FROM TT_PART_DEFINE TD " +
                " WHERE UPPER(TD.PART_OLDCODE) = '" + oldCode + "' ";
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }
}
