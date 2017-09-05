package com.infodms.dms.dao.parts.financeManager.dealerAccImpManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class partDealerAccImpDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partDealerAccImpDao.class);
    private static final partDealerAccImpDao dao = new partDealerAccImpDao();

    private partDealerAccImpDao() {
    }

    public static final partDealerAccImpDao getInstance() {
        return dao;
    }

    /**
     * @param : @param delCode
     * @param : @return
     * @return :
     * LastDate    : 2013-4-15
     * @Title : 验证服务商编码是否存在 并返回服务商ID、Name
     * @Description:
     */
    public List checkDealer(String sqlStr) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT * FROM TM_DEALER TD WHERE 1 = 1 and TD.dealer_type=10771002 ";
        sql += sqlStr;
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title : 获取服务商资金导入方式
     */
    public String getAccountImportType() {
        String impType = "0";
        List<Map<String, Object>> list = null;
        String sql = "SELECT TB.PARA_VALUE FROM TM_BUSINESS_PARA TB WHERE TB.PARA_ID = '" + Constant.FIXCODE_IMPORT_TYPE + "'";
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        if (list.size() > 0)
            impType = list.get(0).get("PARA_VALUE").toString();
        return impType;
    }

    /**
     * @param : @param dealerId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title : 判断是否为新增账户
     */
    public String checkDealerAccWhetherExisted(String sqlStr) {
        String res = "";
        List<Map<String, Object>> list = null;
        String sql = "SELECT TA.ACCOUNT_ID FROM TT_PART_ACCOUNT_DEFINE TA WHERE 1 = 1 ";
        sql += sqlStr;
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        if (list.size() > 0)
            res = list.get(0).get("ACCOUNT_ID").toString();
        return res;
    }

    /**
     * @param : @param dealerId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title : 获取服务商原有的金额
     */
    public String getOriginalAmount(String sqlStr) {
        String oldAmount = "0";
        List<Map<String, Object>> list = null;
        String sql = "SELECT TA.ACCOUNT_SUM FROM TT_PART_ACCOUNT_DEFINE TA WHERE 1 = 1 ";
        sql += sqlStr;
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        if (list.size() == 1)
            oldAmount = list.get(0).get("ACCOUNT_SUM").toString();
        return oldAmount;
    }

    /**
     * : zhumingwei 2013-09-17
     *
     * @param : @param poseId
     * @param : @param rebId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-9-17
     * @FUNCTION : 获取经销商(经销商售后)
     */
    public List<Map<String, Object>> getDealerList(Long companyId, String sellerId) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append("  SELECT   A.DEALER_CODE,\n");
        sbSql.append("           A.DEALER_ID,\n");
        sbSql.append("           A.DEALER_TYPE,\n");
        sbSql.append("           A.DEALER_NAME,\n");
        sbSql.append("           A.DEALER_SHORTNAME\n");
        sbSql.append("    FROM   TM_DEALER a\n");
        sbSql.append("   WHERE       A.STATUS = " + Constant.STATUS_ENABLE + "\n");
        sbSql.append("           AND A.OEM_COMPANY_ID = " + companyId + "\n");
        sbSql.append("           AND DEALER_TYPE=" + Constant.DEALER_TYPE_DWR + "\n");
        sbSql.append("           AND DEALER_LEVEL=" + Constant.DEALER_LEVEL_01 + "\n");
        sbSql.append("  AND ( A.DEALER_CLASS <>" + Constant.DEALER_CLASS_TYPE_13 + " OR A.DEALER_CLASS IS NULL)\n");
        sbSql.append("  AND a.dealer_id not in (2013082011016251,2013082011016135,2013082011016458,2013082011015184)");
        //sbSql.append("AND EXISTS (SELECT 1 FROM tt_part_sales_relation r WHERE r.childorg_id=a.dealer_id AND r.parentorg_id="+sellerId+")\n");

        return pageQuery(sbSql.toString(), null, dao.getFunName());

    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }
}
