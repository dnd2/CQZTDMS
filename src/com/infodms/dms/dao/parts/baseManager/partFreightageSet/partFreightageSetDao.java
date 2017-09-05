package com.infodms.dms.dao.parts.baseManager.partFreightageSet;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.po.TtPartFreightageDefinePO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-7-7
 * @ClassName : partFreightageSetDao
 */
public class partFreightageSetDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partFreightageSetDao.class);
    private static final partFreightageSetDao dao = new partFreightageSetDao();

    private partFreightageSetDao() {
    }

    public static final partFreightageSetDao getInstance() {
        return dao;
    }

    private static final int FIX_VALUE_01 = 92101001; //供应中心
    private static final int FIX_VALUE_02 = 92101011; //服务商(不分等级)

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
    public PageResult<Map<String, Object>> queryPartFreightageSet(String sqlStr, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT FD.DEF_ID, FD.ORDER_TYPE, FD.DEALER_TYPE, PF.FIX_NAME, FD.FREE_TIMES, FD.STATE, FD.FRG_OPTION, ");
        sql.append(" TO_CHAR(FD.FREE_CONDITION,'fm999,999,999,990.00') AS FREE_CONDITION, TO_CHAR(FD.MARKUP_RATIO,'fm990.00') AS  MARKUP_RATIO,FD.min_pirce");
        sql.append(" FROM TT_PART_FREIGHTAGE_DEFINE FD, TT_PART_FIXCODE_DEFINE PF ");
        sql.append(" WHERE 1 = 1  AND PF.FIX_GOUPTYPE = " + Constant.FIXCODE_TYPE_05);
        sql.append(" AND FD.DEALER_TYPE = PF.FIX_VALUE(+) ");
        sql.append(sqlStr);
        //sql.append(" ORDER BY  FD.ORDER_TYPE, FD.DEALER_TYPE ");
        sql.append(" order by fd.def_id");
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
    public List<Map<String, Object>> queryPartFreightageSetList(String sqlStr) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT FD.DEF_ID, FD.ORDER_TYPE, C1.CODE_DESC AS OT_NAME, FD.DEALER_TYPE, PF.FIX_NAME, FD.FREE_TIMES, FD.STATE, FD.FRG_OPTION, ");
        sql.append(" TO_CHAR(FD.FREE_CONDITION,'fm999,999,990.00') AS FREE_CONDITION, TO_CHAR(FD.MARKUP_RATIO,'fm990.00') AS MARKUP_RATIO ");
        sql.append(" FROM TT_PART_FREIGHTAGE_DEFINE FD, TC_CODE C1, TT_PART_FIXCODE_DEFINE PF ");
        sql.append(" WHERE 1 = 1  AND PF.FIX_GOUPTYPE = " + Constant.FIXCODE_TYPE_05);
        sql.append(" AND FD.DEALER_TYPE = PF.FIX_VALUE(+) ");
        sql.append(" AND FD.ORDER_TYPE = C1.CODE_ID ");
        sql.append(sqlStr);
        sql.append(" ORDER BY FD.ORDER_TYPE, FD.DEALER_TYPE ");

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param partId
     * @param : @param partCode
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title : 验证配件运费加价设置记录是否存在
     */
    @SuppressWarnings("unchecked")
    public List getExistPO(String orderType, String dealerType) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT FD.* FROM TT_PART_FREIGHTAGE_DEFINE FD ");
        sql.append(" WHERE 1= 1 ");
        sql.append(" AND FD.ORDER_TYPE = '" + orderType + "' ");
        sql.append(" AND FD.DEALER_TYPE = '" + dealerType + "' ");
        return dao.select(TtPartFreightageDefinePO.class, sql.toString(), null);
    }


    /**
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-7-8
     * @Title : 获取指定服务商
     */
    public List<Map<String, Object>> getVenderList() throws Exception {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT PF.FIX_NAME, PF.FIX_VALUE");
        sql.append("  FROM TT_PART_FIXCODE_DEFINE PF");
        sql.append(" WHERE 1 = 1  AND PF.FIX_GOUPTYPE = " + Constant.FIXCODE_TYPE_05);
        sql.append(" AND PF.FIX_VALUE IN ('" + FIX_VALUE_01 + "','" + FIX_VALUE_02 + "') ");
        sql.append(" ORDER BY PF.FIX_VALUE ");

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
