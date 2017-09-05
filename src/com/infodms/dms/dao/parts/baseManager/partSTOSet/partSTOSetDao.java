package com.infodms.dms.dao.parts.baseManager.partSTOSet;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.po.TtPartStoDefinePO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-4-10
 * @ClassName : partSTOSetDao
 */
public class partSTOSetDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partSTOSetDao.class);
    private static final partSTOSetDao dao = new partSTOSetDao();

    private partSTOSetDao() {
    }

    public static final partSTOSetDao getInstance() {
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
    public PageResult<Map<String, Object>> queryPartSTOSet(String sqlStr, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT SD.* FROM TT_PART_STO_DEFINE SD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);
        sql.append(" ORDER BY SD.VENDER_NAME, SD.BRAND, SD.PART_OLDCODE, SD.PART_NAME, SD.PART_CODE ");
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
    public List<Map<String, Object>> queryPartSTOSetList(String sqlStr) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT SD.* FROM TT_PART_STO_DEFINE SD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);

        sql.append(" ORDER BY SD.VENDER_NAME, SD.BRAND, SD.PART_OLDCODE, SD.PART_NAME, SD.PART_CODE ");

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }


    /**
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @param whereSql
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 返回所有配件信息
     */
    public PageResult<Map<String, Object>> getParts(String partolcode, String partcname, int pageSize, int curPage) {
        PageResult<Map<String, Object>> result = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT PD.* ");
        sql.append(" FROM TT_PART_DEFINE PD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND PD.STATE = '" + Constant.STATUS_ENABLE + "' ");

        if (null != partolcode && !"".equals(partolcode)) {
            sql.append(" AND UPPER(PD.PART_OLDCODE) LIKE '%" + partolcode.toUpperCase() + "%' ");
        }

        if (null != partcname && !"".equals(partcname)) {
            sql.append(" AND PD.PART_CNAME LIKE '%" + partcname + "%' ");
        }

        sql.append(" ORDER BY PD.PART_OLDCODE ");
        result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return result;
    }

    /**
     * @param : @param sqlStr
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-8
     * @Title : 获取供货商信息
     */
    public PageResult<Map<String, Object>> getVenders(String sqlStr, int pageSize, int curPage) {
        PageResult<Map<String, Object>> result = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT MD.* ");
        sql.append(" FROM TT_PART_MAKER_DEFINE MD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND MD.STATE = '" + Constant.STATUS_ENABLE + "' ");
        sql.append(sqlStr);

        sql.append(" ORDER BY MD.MAKER_CODE, MD.MAKER_NAME ");
        result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return result;
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

    /**
     * @param : @param partId
     * @param : @param partCode
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title : 验证计划员与仓库关系记录是否存在
     */
    @SuppressWarnings("unchecked")
    public List getExistPO(String venderId, String brand, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT SD.* FROM TT_PART_STO_DEFINE SD ");
        sql.append(" WHERE 1= 1 ");
        sql.append(" AND SD.VENDER_ID = '" + venderId + "' ");
        sql.append(" AND SD.BRAND = '" + brand + "' ");
        sql.append(" AND SD.PART_ID = '" + partId + "' ");
        return dao.select(TtPartStoDefinePO.class, sql.toString(), null);
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
     * @Title : 计划分页查询
     */
    public PageResult<Map<String, Object>> queryPartException(String partCode, String partName, String partId, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT T.REPLACE_ID,\n" +
                        "       T.PART_ID,\n" +
                        "       T.PART_CODE,\n" +
                        "       T.PART_OLDCODE,\n" +
                        "       T.PART_CNAME,\n" +
                        "       T.REPART_ID,\n" +
                        "       T.REPART_CODE,\n" +
                        "       T.REPART_OLDCODE,\n" +
                        "       T.REPART_CNAME,\n" +
                        "       T.CREATE_DATE,\n" +
                        "       T.UPDATE_DATE,\n" +
                        "       T.REMARK,\n" +
                        "       u.NAME acnt,\n" +
                        "       T.STATE\n" +
                        "  FROM TT_PART_REPLACED_DEFINE T, TC_USER U\n" +
                        "  WHERE 1=1 AND T.UPDATE_BY = U.USER_ID(+)\n" +
                        "  AND STATE = 10011001\n");

        if (null != partCode && !partCode.equals("")) {
            sql.append(" and UPPER(T.PART_OLDCODE) LIKE '%" + partCode.toUpperCase() + "%' ");
        }

        if (null != partId && !partId.equals("")) {
            sql.append(" and partId= )").append(partId);
        }
        if (null != partName && !partName.equals("")) {
            sql.append(" and UPPER(T.PART_CNAME) LIKE '%" + partName.toUpperCase() + "%'");
        }
        sql.append(" ORDER BY T.CREATE_DATE DESC ");
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
     * @Title : 计划分页查询
     */
    public List<Map<String, Object>> getPartException(String partId) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT T.REPLACE_ID,\n" +
                        "       T.PART_ID,\n" +
                        "       T.PART_CODE,\n" +
                        "       T.PART_OLDCODE,\n" +
                        "       T.PART_CNAME,\n" +
                        "       T.REPART_ID,\n" +
                        "       T.REPART_CODE,\n" +
                        "       T.REPART_OLDCODE,\n" +
                        "       T.REPART_CNAME,\n" +
                        "       T.CREATE_DATE,\n" +
                        "       T.UPDATE_DATE,\n" +
                        "       T.REMARK,\n" +
                        "       u.NAME acnt,\n" +
                        "       T.STATE\n" +
                        "  FROM TT_PART_REPLACED_DEFINE T, TC_USER U\n" +
                        " WHERE T.UPDATE_BY = U.USER_ID(+)");

        if (null != partId && !partId.equals("")) {
            sql.append(" and PART_ID= ").append(partId);
        }

        sql.append(" ORDER BY T.CREATE_DATE DESC ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
