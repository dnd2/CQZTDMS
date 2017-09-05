package com.infodms.dms.dao.parts.financeManager.dealerAccContactDetailManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-4-16
 * @ClassName : partDealerAccConDetDao
 */
public class partDealerAccConDetDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partDealerAccConDetDao.class);
    private static final partDealerAccConDetDao dao = new partDealerAccConDetDao();

    private partDealerAccConDetDao() {
    }

    public static final partDealerAccConDetDao getInstance() {
        return dao;
    }

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title : 条件查询资金导入记录信息
     */
    public PageResult<Map<String, Object>> queryDealerAccConDet(int pageSize, int curPage, String sqlStr) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT PA.DETAIL_ID,\n");
        sql.append("       TD.DEALER_CODE CHILDORG_CODE,\n");
        sql.append("       TD.DEALER_NAME CHILDORG_NAME,\n");
        sql.append("       DECODE(PA.IS_TYPE,\n");
        sql.append("              1,\n");
        sql.append("              '打款',\n");
        sql.append("              2,\n");
        sql.append("              '异动',\n");
        sql.append("              3,\n");
        sql.append("              '开票',\n");
        sql.append("              4,\n");
        sql.append("              '开票折让',\n");
        sql.append("              5,\n");
        sql.append("              '索赔转配件款') FIN_TYPE,\n");
        sql.append("       PA.AMOUNT,\n");
        sql.append("       PA.REMARK,\n");
        sql.append("       PA.CREATE_DATE\n");
        sql.append("  FROM TT_PART_FUNDS_ROSE_DETAIL PA, TM_DEALER TD\n");
        sql.append(" WHERE PA.DEALER_ID = TD.DEALER_ID\n");
        sql.append(sqlStr + "\n");
        sql.append(" ORDER BY PA.CREATE_DATE DESC\n");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

}
