package com.infodms.dms.dao.parts.baseManager.partsBaseManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author : luole
 *         CreateDate     : 2013-4-2
 * @ClassName : PartWareHouseDao
 * @Description : 配件仓库维护DAO
 */
public class PartMoveSeatDao extends BaseDao<PO> {
    public static final Logger logger = Logger.getLogger(PartMoveSeatDao.class);
    private static final PartMoveSeatDao dao = new PartMoveSeatDao();

    private PartMoveSeatDao() {
    }

    public static final PartMoveSeatDao getInstance() {
        return dao;
    }

    public PageResult<Map<String, Object>> getQueryData(Map<String, Object> map, int curPage, int pageSize) throws Exception {

        String loc_code = (String) map.get("LOC_CODE");
        String part_code = (String) map.get("PART_OLDCODE");
        String part_name = (String) map.get("PART_NAME");
        String TstartDate = (String) map.get("TstartDate");
        String TendDate = (String) map.get("TendDate");
        String orgId = map.get("ORG_ID").toString();
        String whId = map.get("whId").toString();
        String partId = map.get("partId") != null ? map.get("partId").toString() : null;

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM VW_PART_STOCK TD WHERE  ORG_ID=" + orgId + " AND TD.STATUS = 1 AND TD.STATE = " + Constant.STATUS_ENABLE);

        if (loc_code != null && !"".equals(loc_code)) {
            sql.append("   AND TD.LOC_CODE  like '%" + loc_code + "%'\n");
        }
        if (part_code != null && !"".equals(part_code)) {
            sql.append("   AND TD.PART_OLDCODE  like '%" + part_code + "%'\n");
        }
        if (part_name != null && !"".equals(part_name)) {
            sql.append("   AND TD.PART_CNAME LIKE '%" + part_name + "%'\n");
        }
        if ((TstartDate != null && !"".equals(TstartDate)) && (TendDate != null && !"".equals(TendDate))) {
            sql.append("   AND TD.STORAGE_DATE BETWEEN TO_DATE('" + TstartDate + "','YYYY-MM-DD') AND TO_DATE('" + TendDate + "','YYYY-MM-DD')");
        } else if ((TstartDate == null || "".equals(TstartDate)) && (TendDate != null && !"".equals(TendDate))) {
            sql.append("   AND TD.STORAGE_DATE < TO_DATE('" + TendDate + "','YYYY-MM-DD')");
        } else if ((TstartDate != null && !"".equals(TstartDate)) && (TendDate == null || "".equals(TendDate))) {
            sql.append("   AND TD.STORAGE_DATE > TO_DATE('" + TstartDate + "','YYYY-MM-DD') ");
        }
        if (whId != null && !"".equals(whId)) {
            sql.append("   AND TD.WH_ID = '" + whId + "'\n");
        }
        if (partId != null && !"".equals(partId)) {
            sql.append("   AND TD.PART_ID = '" + partId + "'\n");
        }
        sql.append("  ORDER BY TD.PART_OLDCODE ASC");
        List<Object> params = new LinkedList<Object>();
        PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * 查询移位历史
     *
     * @param map
     * @param curPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageResult<Map<String, Object>> getQueryRecord(Map<String, Object> map, int curPage, int pageSize) throws Exception {

        String loc_code = (String) map.get("LOC_CODE");
        String part_code = (String) map.get("PART_OLDCODE");
        String part_name = (String) map.get("PART_NAME");
        String TstartDate = (String) map.get("TstartDate");
        String TendDate = (String) map.get("TendDate");
        String orgId = map.get("ORG_ID").toString();
        String whId = map.get("whId").toString();
        String configId = map.get("CONFIG_ID").toString();

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT TD.*, WD.WH_NAME, WD2.WH_NAME WH_NAME2\n");
        sql.append("  FROM TT_PART_STOCK_CHG_RECORD TD,\n");
        sql.append("       TT_PART_WAREHOUSE_DEFINE WD,\n");
        sql.append("       TT_PART_WAREHOUSE_DEFINE WD2\n");
        sql.append(" WHERE TD.WH_ID = WD.WH_ID\n");
        sql.append("   AND TD.TOWH_ID = WD2.WH_ID\n");
        sql.append("   AND TD.ORG_ID = 2010010100070674\n");
        sql.append("   AND TD.STATUS = 1");

        if (loc_code != null && !"".equals(loc_code)) {
            sql.append("   AND TD.LOC_CODE  like '%" + loc_code + "%'\n");
        }
        if (part_code != null && !"".equals(part_code)) {
            sql.append("   AND TD.PART_OLDCODE  like '%" + part_code + "%'\n");
        }
        if (part_name != null && !"".equals(part_name)) {
            sql.append("   AND TD.PART_CNAME LIKE '%" + part_name + "%'\n");
        }
        if ((TstartDate != null && !"".equals(TstartDate)) && (TendDate != null && !"".equals(TendDate))) {
            sql.append("   AND TD.CREATE_DATE BETWEEN TO_DATE('" + TstartDate + "','YYYY-MM-DD') AND TO_DATE('" + TendDate + "','YYYY-MM-DD')");
        } else if ((TstartDate == null || "".equals(TstartDate)) && (TendDate != null && !"".equals(TendDate))) {
            sql.append("   AND TD.CREATE_DATE < TO_DATE('" + TendDate + "','YYYY-MM-DD')");
        } else if ((TstartDate != null && !"".equals(TstartDate)) && (TendDate == null || "".equals(TendDate))) {
            sql.append("   AND TD.CREATE_DATE > TO_DATE('" + TstartDate + "','YYYY-MM-DD') ");
        }
        if (whId != null && !"".equals(whId)) {
            sql.append("   AND TD.WH_ID = '" + whId + "'\n");
        }
        if (configId != null && !"".equals(configId)) {
            sql.append("  AND td.config_id =" + configId);
        }
        sql.append("  ORDER BY TD.CREATE_DATE DESC");
        List<Object> params = new LinkedList<Object>();
        PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> getVm_part_stock(String locId, String partId, String orgId) throws Exception {

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM VW_PART_STOCK TD WHERE ORG_ID=" + orgId + " AND TD.LOC_ID = " + locId + " AND TD.PART_ID = " + partId);

        List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName());
        return ps;
    }

    /**
     * @param : @param conSql  条件 SQl
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-3
     * @Title : 配件分布查询
     */


    public PageResult<Map<String, Object>> selPartPageQuery(String conSql, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("select  * from tt_part_define tp where 1=1 ");
        sql.append(conSql);
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    public PageResult<Map<String, Object>> selWhPageQuery(String conSql, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("select  * from tt_part_warehouse_define tpwd where 1=1 ");
        sql.append(conSql);
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    /**
     * @param : @param conSql
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-3
     * @Title : 货位分布查询
     */
    public PageResult<Map<String, Object>> locPageQuery(String conSql, long curUserId, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append(

                "select l.loc_id,\n" +
                        "       d.part_code,\n" +
                        "       d.part_oldcode,\n" +
                        "       d.part_cname,\n" +
                        "       D.MIN_PKG,\n" +
                        "       D.OEM_min_pkg,\n" +
                        "       D.PKG_SIZE,\n" +
                        "       l.loc_code,\n" +
                        "       l.loc_name,\n" +
                        "       l.sub_loc,\n" +
                        "       w.wh_name,\n" +
                        "       U.NAME AS WH_MAN,\n" +
                        "       l.create_date,\n" +
                        "       l.update_date,\n" +
                        "       l.state\n" +
                        "  from tt_part_loaction_define  l,\n" +
                        "       tt_part_define           d,\n" +
                        "       tt_part_warehouse_define w,\n" +
                        "       tc_user u\n" +
                        " where l.part_id = d.part_id\n" +
                        "   and l.wh_id = w.wh_id\n" +
                        "   and l.WHMAN_ID = U.USER_ID(+)\n" +
                        "   AND l.org_id = w.org_id" +
                        "   and l.org_id =" + curUserId
        );

        sql.append(conSql);
        sql.append(" ORDER BY D.PART_OLDCODE, D.PART_CODE, D.PART_CNAME ");
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    /**
     * @param : @param conSql
     * @param : @param orgId
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-27
     * @Title : 货位变更查询
     */
    public PageResult<Map<String, Object>> locHistoryQuery(String conSql, long orgId, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append(
                " SELECT L.LOC_ID, " +
                        " L.HS_ID, " +
                        " D.PART_CODE, " +
                        " D.PART_OLDCODE, " +
                        " D.PART_CNAME, " +
                        " L.LOC_CODE, " +
                        " L.OLD_LOC_CODE, " +
                        " L.LOC_NAME, " +
                        " L.OLD_LOC_NAME, " +
                        " L.SUB_LOC, " +
                        " L.OLD_SUB_LOC, " +
                        " W.WH_NAME, " +
                        " U1.NAME AS WH_MAN, " +
                        " U2.NAME AS OLD_WH_MAN, " +
                        " L.CREATE_DATE " +
                        " FROM TT_PART_LOACTION_HISTORY L, " +
                        " TT_PART_DEFINE D, " +
                        " TT_PART_WAREHOUSE_DEFINE W, " +
                        " TC_USER U1, " +
                        " TC_USER U2 " +
                        " WHERE L.PART_ID = D.PART_ID " +
                        " AND L.WH_ID = W.WH_ID " +
                        " AND L.WHMAN_ID = U1.USER_ID(+) " +
                        " AND L.OLD_WHMAN_ID = U2.USER_ID(+) " +
                        " AND L.ORG_ID = W.ORG_ID " +
                        " AND L.ORG_ID = " + orgId
        );

        sql.append(conSql);
        sql.append(" ORDER BY L.CREATE_DATE DESC ");
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    public List<Map<String, Object>> locInfoList(String sqlStr) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();
        sql.append(

                "select l.loc_id,\n" +
                        "       d.part_code,\n" +
                        "       d.part_oldcode,\n" +
                        "       d.part_cname,\n" +
                        "       D.MIN_PKG,\n" +
                        "       D.OEM_min_pkg,\n" +
                        "       D.PKG_SIZE,\n" +
                        "       l.loc_code,\n" +
                        "       L.PART_ID,\n" +
                        "       l.loc_name,\n" +
                        "       l.sub_loc,\n" +
                        "       l.wh_id,\n" +
                        "       l.org_id,\n" +
                        "       w.wh_name,\n" +
                        "       U.NAME AS WH_MAN,\n" +
                        "       l.create_date,\n" +
                        "       TO_CHAR(l.update_date,'yyyy-MM-dd hh24:mm:ss') AS update_date,\n" +
                        "       l.state\n" +
                        "  from tt_part_loaction_define  l,\n" +
                        "       tt_part_define           d,\n" +
                        "       tt_part_warehouse_define w,\n" +
                        "       tc_user u\n" +
                        " where l.part_id = d.part_id\n" +
                        "   and l.WHMAN_ID = U.USER_ID(+)\n" +
                        "   and l.wh_id = w.wh_id\n" +
                        "   AND l.org_id = w.org_id "
        );

        sql.append(sqlStr);
        sql.append(" ORDER BY D.PART_OLDCODE, D.PART_CODE, D.PART_CNAME ");
        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-29
     * @Title : 货位变更List
     */
    public List<Map<String, Object>> locHistoryList(String sqlStr) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();
        sql.append(
                " SELECT L.LOC_ID, " +
                        " L.HS_ID, " +
                        " D.PART_CODE, " +
                        " D.PART_OLDCODE, " +
                        " D.PART_CNAME, " +
                        " L.LOC_CODE, " +
                        " L.OLD_LOC_CODE, " +
                        " L.LOC_NAME, " +
                        " L.OLD_LOC_NAME, " +
                        " L.SUB_LOC, " +
                        " L.OLD_SUB_LOC, " +
                        " W.WH_NAME, " +
                        " U1.NAME AS WH_MAN, " +
                        " U2.NAME AS OLD_WH_MAN, " +
                        " TO_CHAR(L.CREATE_DATE,'yyyy-MM-dd hh24:mm:ss') AS  CREATE_DATE " +
                        " FROM TT_PART_LOACTION_HISTORY L, " +
                        " TT_PART_DEFINE D, " +
                        " TT_PART_WAREHOUSE_DEFINE W, " +
                        " TC_USER U1, " +
                        " TC_USER U2 " +
                        " WHERE L.PART_ID = D.PART_ID " +
                        " AND L.WH_ID = W.WH_ID " +
                        " AND L.WHMAN_ID = U1.USER_ID(+) " +
                        " AND L.OLD_WHMAN_ID = U2.USER_ID(+) " +
                        " AND L.ORG_ID = W.ORG_ID "
        );

        sql.append(sqlStr);
        sql.append(" ORDER BY L.CREATE_DATE DESC ");

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param whId    : 仓库ID
     * @param : @param partId  : 配件 ID
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-3
     * @Title : 根据ID查询其他信息
     */
    public Map<String, Object> selInfo(long locId) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append(
                "select w.wh_name, w.wh_code, p.part_code, p.part_oldcode, p.part_cname\n" +
                        "  from tt_part_warehouse_define w,\n" +
                        "       tt_part_loaction_define  l,\n" +
                        "       tt_part_define           p\n" +
                        " where l.wh_id = w.wh_id\n" +
                        "   and l.part_id = p.part_id\n" +
                        "   and l.loc_id = " + locId
        );
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        for (Map<String, Object> temp : list) {
            map.put("WH_NAME", temp.get("WH_NAME"));
            map.put("WH_CODE", temp.get("WH_CODE"));
            map.put("PART_CODE", temp.get("PART_CODE"));
            map.put("PART_OLDCODE", temp.get("PART_OLDCODE"));
            map.put("PART_CNAME", temp.get("PART_CNAME"));
        }
        return map;
    }

    public List<Map<String, Object>> getPartWareHouse(long orgId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select WH_NAME,WH_ID from tt_part_warehouse_define d where 1=1");
        if (orgId != 0l) {
            sql.append(" and d.state=" + Constant.STATUS_ENABLE).append("AND d.status=1 AND d.org_id =" + orgId);
            sql.append(" ORDER BY D.WH_NAME ");
        }
        return pageQuery(sql.toString(), null, getFunName());
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param delCode
     * @param : @return
     * @return :
     * LastDate    : 2013-5-3
     * @Title : 验证服配件编码是否存在 并返回配件ID、Name
     * @Description:
     */
    public List<Map<String, Object>> checkOldCode(String oldCode) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT TD.PART_ID,TD.PART_CODE, TD.PART_OLDCODE, TD.PART_CNAME, TD.MIN_PKG FROM TT_PART_DEFINE TD " +
                " WHERE UPPER(TD.PART_OLDCODE) = '" + oldCode + "' ";
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 验证货位是否存在
     */
    public List<Map<String, Object>> checkLocCode(String sqlStr) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT LD.* FROM TT_PART_LOACTION_DEFINE LD " +
                " WHERE 1 = 1 ";
        sql += sqlStr;
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }

    /**
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title : 获取指定的人员信息
     */
    public List<Map<String, Object>> getUsers(String userPost, String userAct) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT U.USER_ID, U.NAME " +
                "FROM TC_USER U, TT_PART_USERPOSE_DEFINE PU, TT_PART_FIXCODE_DEFINE PF " +
                "WHERE PU.USER_ID = U.USER_ID(+) AND  PF.FIX_VALUE = PU.USER_TYPE  " +
                "AND U.USER_STATUS ='" + Constant.STATUS_ENABLE + "' " +
                "AND PU.STATE = '" + Constant.STATUS_ENABLE + "' AND PF.FIX_NAME ='" + userPost + "' ");

        if (!"".equals(userAct)) {
            sql.append(" AND U.ACNT = '" + userAct + "' ");
        }

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-24
     * @Title : 返回库管员list
     */
    public PageResult<Map<String, Object>> getManagerList(int pageSize, int curPage, String sbString) {
        PageResult<Map<String, Object>> result = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT U.USER_ID, U.NAME " +
                "FROM TC_USER U, TT_PART_USERPOSE_DEFINE PU, TT_PART_FIXCODE_DEFINE PF " +
                "WHERE PU.USER_ID = U.USER_ID(+) AND  PF.FIX_VALUE = PU.USER_TYPE  " +
                "AND U.USER_STATUS ='" + Constant.STATUS_ENABLE + "' " +
                "AND PU.STATE = '" + Constant.STATUS_ENABLE + "' ");
        sql.append(sbString);

        result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return result;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-24
     * @Title : 获取库管员信息
     */
    public List<Map<String, Object>> getUsers(String sqlStr) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT L.WHMAN_ID AS USER_ID, U.NAME FROM TC_USER U, TT_PART_LOACTION_DEFINE L ");
        sql.append(" WHERE L.WHMAN_ID = U.USER_ID(+) ");
        sql.append(sqlStr);

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    //zhumingwei 2013-09-11 添加库官员下拉列表 begin
    public List queryWhmanInfo() throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT A.USER_ID WHMAN_ID, U.NAME WHMAN_NAME \n");
            sql.append("   FROM TT_PART_USERPOSE_DEFINE A, TC_USER U \n");
            sql.append("  WHERE A.USER_ID = U.USER_ID \n");
            sql.append("    AND A.USER_TYPE = 4 ");

            return pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param partId
     * @param whId
     * @param locId
     * @return
     */
    public List<Map<String, Object>> getItem_qty(String partId, String whId, String locId) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT *\n");
        sql.append("  FROM VW_PART_STOCK V\n");
        sql.append(" WHERE V.PART_ID =" + partId + "\n");
        sql.append("   AND V.WH_ID =" + whId + "\n");
        sql.append("   AND V.LOC_ID =" + locId + "\n");
        sql.append("   AND V.STATE = 10011001\n");
        sql.append("   AND V.STATUS = 1");
        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param request
     * @param config_id
     * @return
     */
    public List<Map<String, Object>> getPartMoveSeatPrint(RequestWrapper request, int config_id) {

        String wh_id = CommonUtils.checkNull(request.getParamValue("value"));
        String rec_id = CommonUtils.checkNull(request.getParamValue("recId"));

        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT TD.*, WD.WH_NAME, WD2.WH_NAME WH_NAME2, O.ORG_NAME \n");
        sql.append(" FROM TT_PART_STOCK_CHG_RECORD TD, \n");
        sql.append(" 	TT_PART_WAREHOUSE_DEFINE WD, \n");
        sql.append(" 	TT_PART_WAREHOUSE_DEFINE WD2, \n");
        sql.append(" 	TM_ORG O \n");
        sql.append(" WHERE TD.WH_ID = WD.WH_ID \n");
        sql.append(" 	AND TD.TOWH_ID = WD2.WH_ID \n");
        sql.append(" 	AND TD.ORG_ID = O.ORG_ID \n");
        sql.append(" 	AND TD.ORG_ID = 2010010100070674 \n");
        sql.append(" 	AND TD.STATUS = 1 \n");
        sql.append(" 	AND TD.WH_ID = '" + wh_id + "' \n");
        sql.append(" 	AND TD.CONFIG_ID = " + config_id + " \n");
        sql.append(" 	AND TD.REC_ID = '" + rec_id + "' \n");

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public List<Map<String, Object>> getPartMoveSeatPrintSum(RequestWrapper request, int config_id) {

        String wh_id = CommonUtils.checkNull(request.getParamValue("value"));
        String rec_id = CommonUtils.checkNull(request.getParamValue("recId"));

        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT SUM(T.TOPART_NUM) TOPART_NUM,SUM(T.PART_NUM) PART_NUM  FROM ( \n");
        sql.append(" SELECT TD.TOPART_NUM, TD.PART_NUM \n");
        sql.append(" FROM TT_PART_STOCK_CHG_RECORD TD, \n");
        sql.append(" 	TT_PART_WAREHOUSE_DEFINE WD, \n");
        sql.append(" 	TT_PART_WAREHOUSE_DEFINE WD2, \n");
        sql.append(" 	TM_ORG O \n");
        sql.append(" WHERE TD.WH_ID = WD.WH_ID \n");
        sql.append(" 	AND TD.TOWH_ID = WD2.WH_ID \n");
        sql.append(" 	AND TD.ORG_ID = O.ORG_ID \n");
        sql.append(" 	AND TD.ORG_ID = 2010010100070674 \n");
        sql.append(" 	AND TD.STATUS = 1 \n");
        sql.append(" 	AND TD.WH_ID = '" + wh_id + "' \n");
        sql.append(" 	AND TD.CONFIG_ID = " + config_id + " \n");
        sql.append(" 	AND TD.REC_ID = '" + rec_id + "' \n");
        sql.append(") T \n");

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }
}
