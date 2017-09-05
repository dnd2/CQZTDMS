package com.infodms.dms.dao.parts.baseManager.activityPartSet;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.po.TtPartSpecialDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-7-13
 * @ClassName : activityPartSetDao
 */
public class activityPartSetDao extends BaseDao {
    public static Logger logger = Logger.getLogger(activityPartSetDao.class);
    private static final activityPartSetDao dao = new activityPartSetDao();

    private activityPartSetDao() {
    }

    public static final activityPartSetDao getInstance() {
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
    public PageResult<Map<String, Object>> queryActivityPartSet(String sqlStr, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT DISTINCT SD.START_DATE,SD.end_DATE,sd.DESCRIBE_ID,SD.DESCRIBE,SD.ACTIVITY_CODE,SD.ACTIVITY_TYPE,SD.PART_TYPE,SD.BAND_ACTICODE,"
                + "decode(SD.state,'10011001','有效','10011002','已关闭')as state FROM TT_PART_SPECIAL_DEFINE SD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);
        sql.append(" ORDER BY SD.DESCRIBE_ID DESC\n");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> queryActivityPartSetClose(RequestWrapper request, int pageSize, int curPage) {
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 活动描述
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 配件编码
        String descId = CommonUtils.checkNull(request.getParamValue("descId")); // 配件名称
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT DISTINCT DR.REL_ID,\n");
        sql.append("                SD.DESCRIBE_ID,\n");
        sql.append("                SD.DESCRIBE,\n");
        sql.append("                TD.DEALER_CODE,\n");
        sql.append("                TD.DEALER_NAME,\n");
        sql.append("                DR.STATE,\n");
        sql.append("                DR.STUTUS\n");
        sql.append("  FROM TT_PART_SPECIAL_DLR_REL DR, TM_DEALER TD, TT_PART_SPECIAL_DEFINE SD\n");
        sql.append(" WHERE DR.DEALER_ID = TD.DEALER_ID\n");
        sql.append("   AND DR.DESCRIBE_ID = SD.DESCRIBE_ID\n");
        sql.append("   AND DR.DESCRIBE_ID = " + descId + "\n");
        if (!"".equals(dealerCode) && null != dealerCode) {
            sql.append("   AND TD.DEALER_CODE LIKE UPPER('%" + dealerCode + "%')\n");
        }
        if (!"".equals(dealerName) && null != dealerName) {
            sql.append("   AND TD.DEALER_NAME LIKE '%" + dealerName + "%'\n");
        }

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> queryActivityPartSetDtl(String sqlStr, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT SD.*, TO_CHAR(SD.START_DATE,'yyyy-MM-dd') AS F_START_DATE, TO_CHAR(SD.END_DATE,'yyyy-MM-dd') AS F_END_DATE FROM TT_PART_SPECIAL_DEFINE SD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);
        sql.append(" ORDER BY SD.DESCRIBE_ID, SD.PART_OLDCODE, SD.PART_NAME, SD.PART_CODE ");
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
    public List<Map<String, Object>> queryActivityPartSetList(String sqlStr) {
        StringBuffer sql = new StringBuffer("");
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        String act_Code = CommonUtils.checkNull(request.getParamValue("act_Code")); // 活动编码
        String state = CommonUtils.checkNull(request.getParamValue("state")); // 活动类型
        String actPartType = CommonUtils.checkNull(request.getParamValue("actPartType")); // 活动配件类型
        sql.append("SELECT SD.DESCRIBE, SD.ACTIVITY_CODE,SD.PART_TYPE,"
                + "(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=SD.ACTIVITY_TYPE) AS ACTIVITY_TYPE,"
                + "(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=SD.PART_TYPE) AS PART_TYPE_DESC,"
                + "SD.PART_OLDCODE,SD.PART_NAME,SD.PART_CODE,SD.SPEC_QTY,"
                + "(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=SD.ISNEED_FLAG) AS ISNEED_FLAG,"
                + "TO_CHAR(SD.START_DATE,'yyyy-MM-dd') AS F_START_DATE, TO_CHAR(SD.END_DATE,'yyyy-MM-dd') AS F_END_DATE  FROM TT_PART_SPECIAL_DEFINE SD ");
        sql.append(" WHERE 1 = 1 ");
        if (null != act_Code && !"".equals(act_Code)) {
            sql.append(" AND UPPER(SD.ACTIVITY_CODE) LIKE '%" + act_Code.toUpperCase() + "%' ");
        }
        if (null != state && !"".equals(state)) {
            sql.append(" and SD.ACTIVITY_TYPE='").append(state).append("'");
        }
        if (null != actPartType && !"".equals(actPartType)) {
            sql.append(" and SD.PART_TYPE='").append(actPartType).append("'");
        }
        sql.append(sqlStr);

        sql.append(" ORDER BY SD.DESCRIBE_ID, SD.PART_OLDCODE, SD.PART_NAME, SD.PART_CODE ");

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
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-13
     * @Title : 获取最大描述ID
     */
    public List<Map<String, Object>> getMaxDesId() {
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT NVL(MAX(SD.DESCRIBE_ID),'1') AS MAX_DESID ");
        sql.append(" FROM TT_PART_SPECIAL_DEFINE SD ");
        sql.append(" WHERE 1 = 1 ");

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-13
     * @Title : 获取描述Id
     */
    public List<Map<String, Object>> getDesId(String sqlStr) {
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT SD.DESCRIBE_ID, SD.DESCRIBE ");
        sql.append(" FROM TT_PART_SPECIAL_DEFINE SD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);
        sql.append(" GROUP BY (SD.DESCRIBE_ID, SD.DESCRIBE) ");

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param partId
     * @param : @param partCode
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title : 验证关系记录是否存在
     */
    @SuppressWarnings("unchecked")
    public List getExistPO(String describe, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT SD.* FROM TT_PART_SPECIAL_DEFINE SD ");
        sql.append(" WHERE 1= 1 ");
        sql.append(" AND SD.DESCRIBE = '" + describe + "' ");
        sql.append(" AND SD.PART_ID = '" + partId + "' ");
        return dao.select(TtPartSpecialDefinePO.class, sql.toString(), null);
    }


    /**
     * 批量插入关系
     *
     * @param descId
     */
    public void insertRel(String descId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT INTO TT_PART_SPECIAL_DLR_REL\n");
        sql.append("  (REL_ID, DESCRIBE_ID, DEALER_ID)\n");
        sql.append("  SELECT F_GETID, '" + descId + "', TD.DEALER_ID\n");
        sql.append("    FROM TM_DEALER TD\n");
        sql.append("   WHERE TD.DEALER_TYPE = 10771002\n");
        sql.append("     AND TD.DEALER_LEVEL = 10851001\n");
        sql.append("     AND NOT EXISTS (SELECT 1\n");
        sql.append("            FROM TT_PART_SPECIAL_DLR_REL D\n");
        sql.append("           WHERE D.DESCRIBE_ID = '" + descId + "'\n");
        sql.append("             AND D.DEALER_ID = TD.DEALER_ID)\n");
        dao.insert(sql.toString());

    }

    public List<Map<String, Object>> getSpeRel(String describeId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TD.DEALER_ID, TD.DEALER_CODE, TD.DEALER_NAME\n");
        sql.append("  FROM TT_PART_SPECIAL_DLR_REL DR, TM_DEALER TD\n");
        sql.append(" WHERE DR.DEALER_ID = TD.DEALER_ID\n");
        sql.append("   AND DR.DESCRIBE_ID =" + describeId + "\n");
        sql.append(" ORDER BY TD.DEALER_CODE\n");
        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }
}
