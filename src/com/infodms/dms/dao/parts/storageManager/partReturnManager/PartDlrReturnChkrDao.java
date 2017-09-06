package com.infodms.dms.dao.parts.storageManager.partReturnManager;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class PartDlrReturnChkrDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PartDlrReturnChkrDao.class);

    private static final PartDlrReturnChkrDao dao = new PartDlrReturnChkrDao();

    private PartDlrReturnChkrDao() {

    }

    public static final PartDlrReturnChkrDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * <p>
     * Description: 更新经销商入库明细记录的退货数量
     * </p>
     * 
     * @param paramMap 参数
     */
    @SuppressWarnings("unchecked")
    public int updateInStrockReturnQty(Map<String, String> paramMap) {
        String returnId = paramMap.get("returnId");  // 退货单id
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE TT_PART_DLR_INSTOCK_DTL T\n");
        sql.append("   SET RETURN_QTY  = NVL(NVL((SELECT SUM(T2.APPLY_QTY)\n");
        sql.append("                               FROM TT_PART_DLR_RETURN_DTL  T2,\n");
        sql.append("                                    TT_PART_DLR_RETURN_MAIN T3\n");
        sql.append("                              WHERE T2.RETURN_ID = T3.RETURN_ID\n");
        sql.append("                                AND T3.IN_ID = T.IN_ID\n");
        sql.append("                                AND T.PART_ID = T2.PART_ID\n");
        sql.append("                                AND T3.STATE = '"+Constant.PART_DLR_RETURN_STATUS_02+"'\n");
        sql.append("                              GROUP BY T2.PART_ID, T3.IN_ID),\n");
        sql.append("                             0) + NVL((SELECT SUM(T2.CHECK_THREE_QTY)\n");
        sql.append("                                        FROM TT_PART_DLR_RETURN_DTL  T2,\n");
        sql.append("                                             TT_PART_DLR_RETURN_MAIN T3\n");
        sql.append("                                       WHERE T2.RETURN_ID = T3.RETURN_ID\n");
        sql.append("                                         AND T3.IN_ID = T.IN_ID\n");
        sql.append("                                         AND T.PART_ID = T2.PART_ID\n");
        sql.append("                                         AND T3.VERIFY_STATUS = '"+Constant.PART_ABJUSTMENT_CHECK_DEAL_STATUS_01+"'\n");
        sql.append("                                       GROUP BY T2.PART_ID, T3.IN_ID),\n");
        sql.append("                                      0),\n");
        sql.append("                         0),\n");
        sql.append("       UPDATE_DATE = SYSDATE\n");
        sql.append(" WHERE EXISTS (SELECT 1\n");
        sql.append("          FROM TT_PART_DLR_RETURN_DTL T2, TT_PART_DLR_RETURN_MAIN T3\n");
        sql.append("         WHERE T2.RETURN_ID = T3.RETURN_ID\n");
        sql.append("           AND T3.IN_ID = T.IN_ID\n");
        sql.append("           AND T2.PART_ID = T.PART_ID\n");
        sql.append("           AND T2.RETURN_ID = '" + returnId + "')\n");
        
        
        return dao.update(sql.toString(), null);
    }

    /**
     * <p>
     * Description: 获取查询配件销售退货sql字符串
     * </p>
     * 
     * @param paramMap
     * @param logonUser
     * @return
     */
    private String getQueryPartDlrReturnSql(Map<String, String> paramMap, AclUserBean logonUser) {
        String chkLevel = paramMap.get("chkLevel");
        int verifyLevel = 0;
        if ("1".equals(chkLevel)) {
            verifyLevel = Constant.PART_RETURN_CHK_LEVEL_01;
        } else if ("2".equals(chkLevel)) {
            verifyLevel = Constant.PART_RETURN_CHK_LEVEL_02;
        } else if ("3".equals(chkLevel)) {
            verifyLevel = Constant.PART_RETURN_CHK_LEVEL_03;
        }
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT T1.RETURN_ID, \n");
        sql.append("        T1.RETURN_CODE, \n");
        sql.append("        T1.DEALER_NAME, \n");
        sql.append("        T1.CREATE_ORGNAME CREATE_DEALER, \n");
        sql.append("        TU1.NAME          CREATE_NAME, \n");
        sql.append("        T1.CREATE_DATE, \n");
        sql.append("        T1.REMARK, \n");
        sql.append("        T1.REMARK1, \n");
        sql.append("        T1.APPLY_DATE, \n");

        // 一级审核通过或二级审核通过
        if ("2".equals(chkLevel) || "3".equals(chkLevel) || "all".equals(chkLevel)) {
            sql.append("        TU2.NAME          VL_ONE_BY_NAME, \n");
            sql.append("        T1.VL_ONE_STATUS, \n");
            sql.append("        T1.VL_ONE_DATE, \n");
            sql.append("        T1.VL_ONE_REMARK, \n");
        }
        // 一级审核通过时
        if ("3".equals(chkLevel) || "all".equals(chkLevel)) {
            sql.append("        TU3.NAME          VL_TWO_BY_NAME, \n");
            sql.append("        T1.VL_TWO_STATUS, \n");
            sql.append("        T1.VL_TWO_DATE, \n");
            sql.append("        T1.VL_TWO_REMARK, \n");
        }
        // 所有审核
        if ("all".equals(chkLevel)) {
            sql.append("        TU4.NAME          VERIFY_BY_NAME, \n");
            sql.append("        T1.VERIFY_STATUS, \n");
            sql.append("        T1.VERIFY_DATE, \n");
        }

        sql.append("        T1.STATE \n");
        sql.append("   FROM TT_PART_DLR_RETURN_MAIN T1 \n");
        sql.append("  INNER JOIN TC_USER TU1 \n");
        sql.append("     ON T1.CREATE_BY = TU1.USER_ID \n");

        if ("2".equals(chkLevel) || "3".equals(chkLevel) || "all".equals(chkLevel)) {
            sql.append("   LEFT JOIN TC_USER TU2 \n");
            sql.append("     ON TU2.USER_ID = T1.VL_ONE_BY \n");
        }
        if ("3".equals(chkLevel) || "all".equals(chkLevel)) {
            sql.append("   LEFT JOIN TC_USER TU3 \n");
            sql.append("     ON TU3.USER_ID = T1.VL_TWO_BY \n");
        }
        if ("all".equals(chkLevel)) {
            sql.append("   LEFT JOIN TC_USER TU4 \n");
            sql.append("     ON TU4.USER_ID = T1.VERIFY_BY \n");
        }
        // --------------查询条件
        sql.append(" WHERE 1 =1 \n");
        if (!CommonUtils.isEmpty(paramMap.get("returnId"))) {
            sql.append("   AND T1.RETURN_ID = " + paramMap.get("returnId") + " \n");
        }
        if (verifyLevel != 0) {
            sql.append("   AND T1.VERIFY_LEVEL = " + verifyLevel + " \n");
        }
        //        sql.append("   AND T1.SELLER_ID=");
        //        if (logonUser.getDealerId() == null) {
        //            sql.append(logonUser.getOrgId());
        //总部增加区域限制
        sql.append(" AND EXISTS (SELECT 1\n");
        sql.append("         FROM TT_PART_SALESSCOPE_DEFINE B\n");
        sql.append("        WHERE T1.DEALER_ID = B.DEALER_ID\n");
        if (!CommonUtils.isEmpty(paramMap.get("salerId"))) {
            sql.append("     AND B.USER_ID = " + paramMap.get("salerId") + "");
        }
        sql.append(" )");
        //        } else {
        //            sql.append(logonUser.getDealerId());
        //        }
        //        sql.append(" and T1.STATE = ").append(Constant.PART_DLR_RETURN_STATUS_02);
        if (!CommonUtils.isEmpty(paramMap.get("state"))) {
            sql.append(" AND T1.STATE = '" + paramMap.get("state") + "' \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("returnCode"))) {
            sql.append(" AND T1.RETURN_CODE LIKE '%").append(paramMap.get("returnCode")).append("%'\n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("dealerName"))) {
            sql.append(" AND T1.DEALER_NAME LIKE '%").append(paramMap.get("dealerName")).append("%'\n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("startDate"))) {
            sql.append(" AND to_date(T1.CREATE_DATE) >= to_date('" + paramMap.get("startDate") + "','yyyy-MM-dd')");
        }
        if (!CommonUtils.isEmpty(paramMap.get("endDate"))) {
            sql.append(" AND to_date(T1.CREATE_DATE) <= to_date('" + paramMap.get("endDate") + "','yyyy-MM-dd')");
        }
        sql.append(" AND T1.STATUS=1");
        sql.append(" ORDER BY T1.APPLY_DATE ASC");
        return sql.toString();
    }

    /**
     * <p>
     * Description: 分页查询配件销售退货审核列表
     * </p>
     * 
     * @param paramMap
     * @param logonUser
     * @param curPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public PageResult<Map<String, Object>> queryPartDlrReturnApplyList(Map<String, String> paramMap,
            AclUserBean logonUser, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            ps = pageQuery(this.getQueryPartDlrReturnSql(paramMap, logonUser), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    /**
     * <p>
     * Description: 查询
     * </p>
     * 
     * @param paramMap
     * @param logonUser
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPartDlrReturnMainInfo(Map<String, String> paramMap, AclUserBean logonUser)
            throws Exception {
        try {
            //            StringBuffer sql = new StringBuffer("");
            //            sql.append(
            //                    "SELECT T1.RETURN_ID,T1.RETURN_CODE,T1.CREATE_ORGNAME ORG_NAME,T1.DEALER_NAME,T1.SELLER_NAME,T1.SO_CODE,T1.REMARK");
            //            sql.append(" FROM TT_PART_DLR_RETURN_MAIN T1 WHERE T1.RETURN_ID=").append(returnId);
            return pageQuery(this.getQueryPartDlrReturnSql(paramMap, logonUser), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public PageResult<Map<String, Object>> queryPartDlrReturnApplyDetailList(String returnId, String soCode,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.DTL_ID,\n");
            sql.append("       T.PART_ID,\n");
            sql.append("       T.PART_CODE,\n");
            sql.append("       T.PART_OLDCODE,\n");
            sql.append("       T.PART_CNAME,\n");
            sql.append("       NVL(T.SALES_QTY, 0) BUY_QTY,\n");
            sql.append("       T.APPLY_QTY,\n");
            sql.append("       T.CHECK_QTY,\n");
            sql.append("       T.CHECK_TWO_QTY,\n");
            sql.append("       T.CHECK_THREE_QTY,\n");
            sql.append("       T.OUT_QTY,\n");
            sql.append("       T.REMARK\n");
            sql.append("  FROM TT_PART_DLR_RETURN_DTL T\n");
            sql.append(" WHERE T.RETURN_ID = '" + returnId + "'\n");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

}
