package com.infodms.dms.dao.parts.baseManager.partsPlanManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : luole
 *         CreateDate     : 2013-4-7
 * @ClassName : PartPlanSettingDao
 * @Description : 计划参数维护DAO
 */
public class PartPlanSettingDao extends BaseDao<PO> {
    public static final Logger logger = Logger.getLogger(PartPlanSettingDao.class);
    private static final PartPlanSettingDao dao = new PartPlanSettingDao();

    private PartPlanSettingDao() {
    }

    public static final PartPlanSettingDao getInstance() {
        return dao;
    }

    /**
     * @param : @param consql    :条件
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-7
     * @Title : planPageQuery
     * @Description: 计划参数维护分页查询
     */
    public PageResult<Map<String, Object>> planPageQuery(String consql, String command, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT PP.PLAN_ID,\n");
        sql.append("       P.PART_CODE,\n");
        sql.append("       P.PART_OLDCODE,\n");
        sql.append("       P.PART_CNAME,\n");
        sql.append("       P.PART_ID,\n");
        sql.append("       PP.SAFETY_STOCK,\n");
        sql.append("       TO_CHAR((NVL(PP.SAFETY_STOCK, '0') * SP.SALE_PRICE3),\n");
        sql.append("               'FM999,999,999,990.00') AS SF_AMOUNT,\n");
        sql.append("       TO_CHAR(SP.SALE_PRICE3, 'FM999,999,999,990.00') AS SALE_PRICE3,\n");
        sql.append("       PP.MAX_STOCK,\n");
        sql.append("       PP.PLAN_CYCLE,\n");
        sql.append("       PP.ARRIVE_CYCLE,\n");
        sql.append("       (SELECT D.SAFTY_CYCLE\n");
        sql.append("          FROM TT_PART_PALNSORT_DEFINE D\n");
        sql.append("         WHERE D.STATE = 10011001\n");
        sql.append("           AND D.STATUS = 1\n");
        sql.append("           AND D.SORT_ID = PP.PLAN_RATIO) SAFETY_CYCLE,\n");
        sql.append("       (SELECT D.SORT_TYPE\n");
        sql.append("          FROM TT_PART_PALNSORT_DEFINE D\n");
        sql.append("         WHERE D.STATE = 10011001\n");
        sql.append("           AND D.STATUS = 1\n");
        sql.append("           AND D.SORT_ID = PP.PLAN_RATIO) PLAN_RATIO_TYPE,\n");
        sql.append("\t\t\t\t(SELECT D.safty_rate\n");
        sql.append("          FROM TT_PART_PALNSORT_DEFINE D\n");
        sql.append("         WHERE D.STATE = 10011001\n");
        sql.append("           AND D.STATUS = 1\n");
        sql.append("           AND D.SORT_ID = PP.PLAN_RATIO)\t safty_rate,\n");
        sql.append("       PP.PLAN_RATIO,\n");
        sql.append("       PP.BO_QTY,\n");
        sql.append("       PP.LOC_BO_QTY,\n");
        sql.append("       PP.ORDER_QTY,\n");
        sql.append("       PP.PLAN_QTY,\n");
        sql.append("       PP.AVG_QTY,\n");
        sql.append("       PP.YEAR_QTY,\n");
        sql.append("       PP.HFYEAR_QTY,\n");
        sql.append("       PP.QUARTER_QTY,\n");
        sql.append("       NVL(VW.NORMAL_QTY, 0) ITEM_QTY,\n");
        sql.append("       W.WH_NAME,\n");
        sql.append("       W.WH_ID,\n");
        sql.append("       PP.STATE\n");
        sql.append("  FROM TT_PART_DEFINE           P,\n");
        sql.append("       TT_PART_PLAN_DEFINE      PP,\n");
        sql.append("       TT_PART_WAREHOUSE_DEFINE W,\n");
        sql.append("       VW_PART_STOCK            VW,\n");
        sql.append("       TT_PART_SALES_PRICE      SP\n");
        sql.append(" WHERE PP.WH_ID = W.WH_ID\n");
        sql.append("   AND PP.PART_ID = P.PART_ID\n");
        sql.append("   AND PP.PART_ID = SP.PART_ID(+)\n");
        sql.append("   AND PP.WH_ID = VW.WH_ID(+)\n");
        sql.append("   AND PP.PART_ID = VW.PART_ID(+)\n");
        sql.append("   AND PP.STATE = 10011001\n");

        sql.append(" and pp.plan_type =" + command + " ");
        sql.append(consql);
        sql.append(" ORDER BY p.part_oldcode");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    /**
     * @param : @param consql
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-7
     * @Title : partPlanDownload
     * @Description: 下载查询
     */
    public List<Map<String, Object>> partPlanDownload(String consql, String command) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT PP.PLAN_ID,\n");
        sql.append("       P.PART_CODE,\n");
        sql.append("       P.PART_OLDCODE,\n");
        sql.append("       P.PART_CNAME,\n");
        sql.append("       P.PART_ID,\n");
        sql.append("       PP.SAFETY_STOCK,\n");
        sql.append("       TO_CHAR((NVL(PP.SAFETY_STOCK, '0') * SP.SALE_PRICE3),\n");
        sql.append("               'FM999,999,999,990.00') AS SF_AMOUNT,\n");
        sql.append("       TO_CHAR(SP.SALE_PRICE3, 'FM999,999,999,990.00') AS SALE_PRICE3,\n");
        sql.append("       PP.MAX_STOCK,\n");
        sql.append("       PP.PLAN_CYCLE,\n");
        sql.append("       PP.ARRIVE_CYCLE,\n");
        sql.append("       (SELECT D.SAFTY_CYCLE\n");
        sql.append("          FROM TT_PART_PALNSORT_DEFINE D\n");
        sql.append("         WHERE D.STATE = 10011001\n");
        sql.append("           AND D.STATUS = 1\n");
        sql.append("           AND D.SORT_ID = PP.PLAN_RATIO) SAFETY_CYCLE,\n");
        sql.append("       (SELECT D.SORT_TYPE\n");
        sql.append("          FROM TT_PART_PALNSORT_DEFINE D\n");
        sql.append("         WHERE D.STATE = 10011001\n");
        sql.append("           AND D.STATUS = 1\n");
        sql.append("           AND D.SORT_ID = PP.PLAN_RATIO) PLAN_RATIO_TYPE,\n");
        sql.append("\t\t\t\t(SELECT D.safty_rate\n");
        sql.append("          FROM TT_PART_PALNSORT_DEFINE D\n");
        sql.append("         WHERE D.STATE = 10011001\n");
        sql.append("           AND D.STATUS = 1\n");
        sql.append("           AND D.SORT_ID = PP.PLAN_RATIO)\t safty_rate,\n");
        sql.append("       PP.PLAN_RATIO,\n");
        sql.append("       PP.BO_QTY,\n");
        sql.append("       PP.LOC_BO_QTY,\n");
        sql.append("       PP.ORDER_QTY,\n");
        sql.append("       PP.PLAN_QTY,\n");
        sql.append("       PP.AVG_QTY,\n");
        sql.append("       PP.YEAR_QTY,\n");
        sql.append("       PP.HFYEAR_QTY,\n");
        sql.append("       PP.QUARTER_QTY,\n");
        sql.append("       NVL(VW.NORMAL_QTY, 0) ITEM_QTY,\n");
        sql.append("       W.WH_NAME,\n");
        sql.append("       W.WH_ID,\n");
        sql.append("       PP.STATE\n");
        sql.append("  FROM TT_PART_DEFINE           P,\n");
        sql.append("       TT_PART_PLAN_DEFINE      PP,\n");
        sql.append("       TT_PART_WAREHOUSE_DEFINE W,\n");
        sql.append("       VW_PART_STOCK            VW,\n");
        sql.append("       TT_PART_SALES_PRICE      SP\n");
        sql.append(" WHERE PP.WH_ID = W.WH_ID\n");
        sql.append("   AND PP.PART_ID = P.PART_ID\n");
        sql.append("   AND PP.PART_ID = SP.PART_ID(+)\n");
        sql.append("   AND PP.WH_ID = VW.WH_ID(+)\n");
        sql.append("   AND PP.PART_ID = VW.PART_ID(+)\n");
        sql.append("   AND PP.STATE = 10011001\n");

        sql.append(" and pp.plan_type =" + command + " ");
        sql.append(consql);
        sql.append(" ORDER BY p.part_oldcode");

        return pageQuery(sql.toString(), null, getFunName());
    }

    /**
     * @param : @param consql
     * @param : @param command
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-29
     * @Title : 获取总安全库存金额
     */
    public List<Map<String, Object>> getTotalSfAmount(String consql, String command) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TO_CHAR(SUM(NVL(PP.SAFETY_STOCK,'0') * NVL(SP.SALE_PRICE3,'0')),'FM999,999,999,990.00') AS TOTAL_SF_AMOUT "
                + " FROM TT_PART_PLAN_DEFINE PP, TT_PART_DEFINE P, TT_PART_SALES_PRICE SP "
                + " WHERE PP.PART_ID = P.PART_ID "
                + " AND PP.PART_ID = SP.PART_ID(+) ");
        sql.append(" AND PP.PLAN_TYPE =" + command + " ");
        sql.append(consql);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());

        return list;
    }

    /**
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-7
     * @Title : getPartWareHouse
     * @Description: 前端配件仓库选择下拉框
     */
    public List<Map<String, Object>> getPartWareHouse(Long userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct(WH_NAME),WH_ID from tt_part_warehouse_define d where 1=1 AND d.state=10011001 AND d.status=1 AND exists");
        sql.append("(SELECT 1 FROM tt_part_planer_wh_relation r WHERE r.wh_id=d.wh_id AND r.planer_id=" + userId + ")");
        return pageQuery(sql.toString(), null, getFunName());
    }

    public List<Map<String, Object>> getSortList() throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("\n");
        sql.append("SELECT D.SORT_TYPE, D.SORT_ID\n");
        sql.append("  FROM TT_PART_PALNSORT_DEFINE D\n");
        sql.append(" WHERE D.STATE =").append(Constant.STATUS_ENABLE);
        sql.append("   AND D.STATUS = 1\n");
        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param : @param delCode
     * @param : @return
     * @return :
     * LastDate    : 2013-5-3
     * @Title : 验证配件编码是否存在 并返回配件ID、Name
     * @Description:
     */
    public List<Map<String, Object>> checkOldCode(String partOldCode) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT TD.* FROM TT_PART_DEFINE TD " +
                " WHERE UPPER(TD.PART_OLDCODE) = '" + partOldCode + "' ";
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-29
     * @Title : 验证配件在计划维护中是否存在
     */
    public List<Map<String, Object>> checkPartInPlan(String sqlStr) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT PP.PLAN_ID ");
        sql.append(" FROM TT_PART_PLAN_DEFINE PP, TT_PART_DEFINE P ");
        sql.append(" WHERE PP.PART_ID = P.PART_ID ");
        sql.append(sqlStr);

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public List<Map<String, Object>> checkSortType(String sqlStr) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT PD.SAFTY_RATE ");
        sql.append(" FROM TT_PART_PALNSORT_DEFINE PD ");
        sql.append(" WHERE PD.STATE = '" + Constant.STATUS_ENABLE + "' ");
        sql.append(sqlStr);

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
