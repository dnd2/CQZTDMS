package com.infodms.dms.dao.parts.baseManager.partExceptionQueryManger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrAuthmonitorpartPO;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.po.TtPartReplacedDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PageResult;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-4-10
 * @ClassName : PartExceptionQueryDao
 */
public class PartExceptionQueryDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartExceptionQueryDao.class);
    private static final PartExceptionQueryDao dao = new PartExceptionQueryDao();

    private PartExceptionQueryDao() {
    }

    public static final PartExceptionQueryDao getInsttance() {
        return dao;
    }

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
     * @Title : 计划分页查询
     */
    public PageResult<Map<String, Object>> queryPartException(String orderID, String partCode, String partName, String stateValue, int pageSize, int curPage) {
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
                        "       T.TYPE,\n" +
                        "       T.CREATE_DATE,\n" +
                        "       T.UPDATE_DATE,\n" +
                        "       T.REMARK,\n" +
                        "       u.NAME acnt,\n" +
                        "       T.STATE\n" +
                        "  FROM TT_PART_REPLACED_DEFINE T, TC_USER U\n" +
                        " WHERE T.UPDATE_BY = U.USER_ID(+)");
        if (null != orderID && !orderID.equals("")) {
            sql.append(" and UPPER(T.PART_CODE) LIKE '%" + orderID.toUpperCase() + "%' ");
        }

        if (null != partCode && !partCode.equals("")) {
            sql.append(" and UPPER(T.PART_OLDCODE) LIKE '%" + partCode.toUpperCase() + "%' ");
        }
        if (null != partName && !partName.equals("")) {
            sql.append(" and UPPER(T.PART_CNAME) LIKE '%" + partName.toUpperCase() + "%'");
        }
        if (null != stateValue && !stateValue.equals("")) {
            sql.append(" and T.STATE = '" + stateValue + "'");
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
     * @Title : 计划下载-查询返回结果
     */
    public List<Map<String, Object>> queryAllPartException(String orderID, String partCode, String partName, String stateValue) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT T.REPLACE_ID,T.PART_ID, T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,T.REPART_CODE,T.REPART_OLDCODE,T.REPART_CNAME,"
                        + "T.CREATE_DATE,T.UPDATE_DATE,T.STATE FROM TT_PART_REPLACED_DEFINE T ");
        sql.append("WHERE 1=1 ");
        if (null != orderID && !orderID.equals("")) {
            sql.append(" and UPPER(T.PART_CODE) LIKE '%" + orderID.toUpperCase() + "%' ");
        }

        if (null != orderID && !partCode.equals("")) {
            sql.append(" and UPPER(T.PART_OLDCODE) LIKE '%" + partCode.toUpperCase() + "%' ");
        }
        if (null != orderID && !partName.equals("")) {
            sql.append(" and UPPER(T.PART_CNAME) LIKE '%" + partName.toUpperCase() + "%'");
        }
        if (null != stateValue && !stateValue.equals("")) {
            sql.append(" and T.STATE = '" + stateValue + "'");
        }
        sql.append(" ORDER BY T.CREATE_DATE DESC ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * <p>
     * Description: 查询可替换的配件
     * </p>
     * @param pageSize 每页条数
     * @param curPage 页面
     * @param paramMap 查询参数
     * @return
     */
    public PageResult<Map<String, Object>> getPartOLCode(int pageSize, int curPage, Map<String, String> paramMap) {
        PageResult<Map<String, Object>> result = null;
        StringBuffer sql = new StringBuffer();

        String partOldId = paramMap.get("partOldId");
        // 如果配件id不为空的就查询该配件的所有替换件
        if(Utility.testString(partOldId)){
            sql.append(" WITH NOT_IN_PART_ID AS \n");
            sql.append(" (SELECT PART_ID \n");
            sql.append("    FROM TT_PART_REPLACED_DEFINE \n");
            sql.append("   START WITH REPART_ID = '"+partOldId+"' \n");
            sql.append(" CONNECT BY PRIOR PART_ID = REPART_ID \n");
            sql.append("   UNION ALL \n");
            sql.append("  SELECT REPART_ID \n");
            sql.append("    FROM TT_PART_REPLACED_DEFINE \n");
            sql.append("   START WITH PART_ID = '"+partOldId+"' \n");
            sql.append(" CONNECT BY PRIOR PART_ID = REPART_ID) \n");
        }
        
        sql.append("SELECT T.PART_ID, T.PART_CODE, T.PART_OLDCODE, T.PART_CNAME\n");
        sql.append("  FROM TT_PART_DEFINE T\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND T.STATE = '" + Constant.STATUS_ENABLE + "' \n");
        sql.append("   AND T.IS_REPLACED = '" + Constant.IF_TYPE_YES + "' \n");

        if (Utility.testString(paramMap.get("partOlcode"))) {
            sql.append(" and UPPER(T.PART_OLDCODE) like '%" + paramMap.get("partOlcode").toUpperCase() + "%' \n");
        }
        if (Utility.testString(paramMap.get("partCName"))) {
            sql.append(" and UPPER(T.PART_CNAME) like '%" + paramMap.get("partCName").toUpperCase() + "%' \n");
        }

        // 如果配件id不为空的就查询不包含该配件的所有替换件
        if(Utility.testString(partOldId)){
            sql.append("   AND T.PART_ID <> '"+partOldId+"' \n");
            sql.append("   AND NOT EXISTS (  \n");
            sql.append("       SELECT 1  \n");
            sql.append("         FROM NOT_IN_PART_ID TR  \n");
            sql.append("        WHERE TR.PART_ID = T.PART_ID) \n");
        }
        
        sql.append(" ORDER BY T.PART_OLDCODE, T.PART_CNAME, T.PART_CODE ");
        result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return result;
    }

    /**
     * @param : @param partId
     * @param : @param partCode
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title : 验证配件是否已存在替代记录
     */
    @SuppressWarnings("unchecked")
    public List<TtPartReplacedDefinePO> getExistPO(String partId, String repartId, String oldRePartId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM TT_PART_REPLACED_DEFINE T\n");
        sql.append(" WHERE 1= 1 \n");
        sql.append(" AND ( \n");
        sql.append("     (T.PART_ID = '"+partId+"' AND T.REPART_ID = '"+repartId+"')  \n");
        sql.append("     OR \n");
        sql.append("     (T.REPART_ID = '"+repartId+"' AND T.PART_ID = '"+partId+"') \n");
        sql.append(" ) \n");
        if(StringUtil.notNull(oldRePartId)){
            sql.append(" AND T.REPART_ID <> '"+oldRePartId+"' \n");
        }
        return dao.select(TtPartReplacedDefinePO.class, sql.toString(), null);
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-15
     * @Title : 获取配件List
     */
    public List<Map<String, Object>> queryReplacePart(String sqlStr) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT T.* FROM TT_PART_REPLACED_DEFINE T ");
        sql.append("WHERE 1=1 ");
        sql.append(sqlStr);
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
