package com.infodms.dms.dao.parts.financeManager.dealerAccImpRecordManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-4-16
 * @ClassName : partDealerAccImpRecDao
 */
public class partDealerAccImpRecDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partDealerAccImpRecDao.class);
    private static final partDealerAccImpRecDao dao = new partDealerAccImpRecDao();

    private partDealerAccImpRecDao() {
    }

    public static final partDealerAccImpRecDao getInstance() {
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
    public PageResult<Map<String, Object>> queryDealerAccImpRec(int pageSize, int curPage, String sqlStr) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT PA.HISTRORY_ID, PA.ACCOUNT_KIND, PA.CHILDORG_CODE, PA.CHILDORG_NAME, TO_CHAR(PA.AMOUNT,'999,999,990.99') AS AMOUNT, PA.IMPORT_TYPE, PA.CREATE_DATE, PA.REMARK, U.NAME " +
                " FROM TC_USER U, TT_PART_ACCOUNT_IMPORT_HISTORY PA " +
                " WHERE PA.CREATE_BY = U.USER_ID(+) \n");
//        sql.append("WHERE 1=1 ");
        sql.append(sqlStr);
        sql.append(" ORDER BY PA.CREATE_DATE DESC");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> queryDealerAccImp(int pageSize, int curPage, String sqlStr) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT PA.HISTRORY_ID,\n");
        sql.append("       PA.ACCOUNT_KIND,\n");
        sql.append("       PA.CHILDORG_CODE,\n");
        sql.append("       PA.CHILDORG_NAME,\n");
        sql.append("       TO_CHAR(PA.AMOUNT, '999,999,990.99') AS AMOUNT,\n");
        sql.append("       PA.IMPORT_TYPE,\n");
        sql.append("       PA.CREATE_DATE,\n");
        sql.append("       PA.PZ_NO,\n");
        sql.append("       PA.REMARK,\n");
        sql.append("       U.NAME,\n");
        sql.append("       FB.BANK_NAME,\n");
        sql.append("       DECODE(PA.STATUS,\n");
        sql.append("              0,\n");
        sql.append("              '未提交',\n");
        sql.append("              1,\n");
        sql.append("              '未处理',\n");
        sql.append("              2,\n");
        sql.append("              '已通过',\n");
        sql.append("              3,\n");
        sql.append("              '已驳回') STATUS2,\n");
        sql.append("       PA.STATUS,\n");
        sql.append("       PA.ACCOUNT_PURPOSE,\n");
        sql.append("       TO_CHAR(DK_DATE, 'yyyy-mm-dd') DK_DATE\n");
        sql.append("  FROM TC_USER U, TT_PART_ACCOUNT_IMPORT_HISTORY PA,TT_SALES_FIN_BANK fb\n");
        sql.append(" WHERE PA.CREATE_BY = U.USER_ID(+)");
        sql.append("   AND PA.BANK_ID = FB.BANK_ID(+)");
        sql.append(sqlStr);
        sql.append(" ORDER BY PA.CREATE_DATE DESC");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

}
