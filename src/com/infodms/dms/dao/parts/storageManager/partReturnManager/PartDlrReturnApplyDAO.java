package com.infodms.dms.dao.parts.storageManager.partReturnManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartDlrReturnApplyDAO extends BaseDao {
    public static Logger logger = Logger.getLogger(PartDlrReturnApplyDAO.class);
    private static final PartDlrReturnApplyDAO dao = new PartDlrReturnApplyDAO();

    private PartDlrReturnApplyDAO() {
    }

    public static final PartDlrReturnApplyDAO getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param : @param dealerId
     * @param : @return   存在返回true  否则返回false
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-15
     * @Title : 检查当前经销商ID在才采购关系中是否存在
     * @Description: TODO
     */
    public boolean checkSalesIsNull(String dealerId) {
        String sql = "select * from tt_part_sales_relation ps where ps.parentorg_id = " + dealerId + " and ps.state = " + Constant.STATUS_ENABLE;
        List<Map<String, Object>> list = pageQuery(sql, null, getFunName());
        if (list != null && list.size() > 0 && null != list.get(0))
            return true;
        return false;
    }

    /**
     * @param : @param consql
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-15
     * @Title : 经销商有下级采购关系
     * @Description: TODO
     */
    public PageResult<Map<String, Object>> selReturnPageQuery(String consql, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from tt_part_sales_relation ps where ps.state = " + Constant.STATUS_ENABLE + " \n");
        sql.append(consql);
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);

    }

    /**
     * @param : @param consql
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-15
     * @Title : 车厂端选择采购关系下级
     * @Description: TODO
     */
    public PageResult<Map<String, Object>> selMainReturnPageQuery(String consql, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from tt_part_sales_relation ps where ps.state = " + Constant.STATUS_ENABLE + " \n");
        sql.append("and  ps.PARENTORG_ID = " + Constant.OEM_ACTIVITIES + " \n");
        sql.append(consql);
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);

    }

    /**
     * @param : @param consql
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-15
     * @Title : 查询配件      不强制检查销售单
     * @Description: TODO
     */
    public PageResult<Map<String, Object>> selPartPageQuery(String consql, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from tt_part_define p where p.state = " + Constant.STATUS_ENABLE + " \n");
        sql.append(consql);
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);

    }

    /**
     * @param returnCode
     * @param dealerName
     * @param startDate   验收日期起
     * @param endDate     验收日期止
     * @param flag
     * @param logonUser
     * @param state
     * @param isWo
     * @param curPage
     * @param pageSize
     * @param startDate_t 提交日期起
     * @param endDate_t   提交日期止
     * @param startDate_s 审核日期起
     * @param endDate_s   审核日期止
     * @param startDate_h 回运日期起
     * @param endDate_h   回运日期止
     * @return
     * @throws Exception
     */
    public PageResult<Map<String, Object>> queryPartDlrReturnList(Map<String, String> paramMap, AclUserBean logonUser, Integer curPage, Integer pageSize
//            String returnCode, String dealerName, String startDate, String endDate, int flag, AclUserBean logonUser, String state,
//            String isWo, Integer curPage, Integer pageSize, String startDate_t, String endDate_t, String startDate_s, String endDate_s
//            , String startDate_h, String endDate_h, String createDate_c, String endDate_c
            ) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            if ("0".equals(paramMap.get("flag"))) {//如果是车厂,就需要把销售单位是车厂的申请都查询出来
                sql.append("SELECT T1.RETURN_ID,\n");
                sql.append("       T1.RETURN_CODE,\n");
                sql.append("       T1.DEALER_CODE,T1.DEALER_NAME,\n");
                sql.append("       T2.NAME CREATE_NAME,\n");
                sql.append("       T1.CREATE_DATE,\n");
                sql.append("       T1.REMARK,\n");
                sql.append("       T1.APPLY_DATE,\n");
                sql.append("       T1.STATE,\n");
                sql.append("       T1.CREATE_ORG,\n");
                sql.append("       T1.CREATE_ORGNAME CREATE_DEALER,\n");
                sql.append("       T1.IS_WO,\n");
                sql.append("       NVL((SELECT COUNT(RD.DTL_ID)\n");
                sql.append("             FROM TT_PART_DLR_RETURN_DTL RD\n");
                sql.append("            WHERE RD.OUT_QTY > 0\n");
                sql.append("              AND RD.RETURN_ID = T1.RETURN_ID),\n");
                sql.append("           0) AS OUT_QTYS,\n");
                sql.append("       T1.VERIFY_DATE,\n");
                sql.append("       T3.NAME T3NAME,\n");
                sql.append("       T1.WL_DATE,\n");
                sql.append("       T4.NAME T4NAME,\n");
                sql.append("       T1.IN_DATE,\n");
                sql.append("       T5.NAME T5NAME,\n");
                sql.append("       T6.NAME T6NAME\n");
                sql.append("  FROM TT_PART_DLR_RETURN_MAIN T1,\n");
                sql.append("       TC_USER                 T2,\n");
                sql.append("       TC_USER                 T3,\n");
                sql.append("       TC_USER                 T4,\n");
                sql.append("       TC_USER                 T5,\n");
                sql.append("       TC_USER                 T6\n");
                sql.append(" WHERE T1.CREATE_BY = T2.USER_ID\n");
                sql.append("   AND T1.VERIFY_BY = T3.USER_ID(+)\n");
                sql.append("   AND T1.WL_BY = T4.USER_ID(+)\n");
                sql.append("   AND T1.IN_BY = T5.USER_ID(+)\n");
                sql.append("   AND T1.APPLY_BY = T6.USER_ID(+)\n");
                sql.append("   AND T1.SELLER_ID =").append(logonUser.getOrgId() + "\n");
                sql.append(" AND T1.state IN (92361002,92361004,92361005,92361006)\n");
                //提交日期
                if (!CommonUtils.isEmpty(paramMap.get("startDate_t"))) {
                    sql.append(" AND to_date(T1.APPLY_DATE)>=").append("to_date('").append(paramMap.get("startDate_t")).append("','yyyy-MM-dd')\n");
                }

                if (!CommonUtils.isEmpty(paramMap.get("endDate_t"))) {
                    sql.append(" AND to_date(T1.APPLY_DATE)<=").append("to_date('").append(paramMap.get("endDate_t")).append("','yyyy-MM-dd')\n");
                }

                //审核日期
                if (!CommonUtils.isEmpty(paramMap.get("startDate_s"))) {
                    sql.append(" AND to_date(T1.VERIFY_DATE)>=").append("to_date('").append(paramMap.get("startDate_s")).append("','yyyy-MM-dd')\n");
                }

                if (!CommonUtils.isEmpty(paramMap.get("endDate_s"))) {
                    sql.append(" AND to_date(T1.VERIFY_DATE)<=").append("to_date('").append(paramMap.get("endDate_s")).append("','yyyy-MM-dd')\n");
                }

                //回运日期
                if (!CommonUtils.isEmpty(paramMap.get("startDate_h"))) {
                    sql.append(" AND to_date(T1.WL_DATE)>=").append("to_date('").append(paramMap.get("startDate_h")).append("','yyyy-MM-dd')\n");
                }

                if (!CommonUtils.isEmpty(paramMap.get("endDate_h"))) {
                    sql.append(" AND to_date(T1.WL_DATE)<=").append("to_date('").append(paramMap.get("endDate_h")).append("','yyyy-MM-dd')\n");
                }
                //验收日期
                if (!CommonUtils.isEmpty(paramMap.get("startDate"))) {
                    sql.append(" AND to_date(T1.In_date)>=").append("to_date('").append(paramMap.get("startDate")).append("','yyyy-MM-dd')\n");
                }

                if (!CommonUtils.isEmpty(paramMap.get("endDate"))) {
                    sql.append(" AND to_date(T1.In_date)<=").append("to_date('").append(paramMap.get("endDate")).append("','yyyy-MM-dd')\n");
                }

                //新增日期
                if (!CommonUtils.isEmpty(paramMap.get("createDate_c"))) {
                    sql.append(" AND to_date(T1.Create_date)>=").append("to_date('").append(paramMap.get("createDate_c")).append("','yyyy-MM-dd')\n");
                }

                if (!CommonUtils.isEmpty(paramMap.get("endDate_c"))) {
                    sql.append(" AND to_date(T1.Create_date)<=").append("to_date('").append(paramMap.get("endDate_c")).append("','yyyy-MM-dd')\n");
                }
            } else if ("1".equals(paramMap.get("flag"))) {//如果是供应中心,就需要把退货单位或销售单位是它的都查询出来
                sql.append("SELECT T1.RETURN_ID,T1.RETURN_CODE,T1.DEALER_NAME,T2.NAME CREATE_NAME,T1.CREATE_DATE,T1.REMARK,T1.APPLY_DATE,T1.STATE,T1.CREATE_ORG,T1.CREATE_ORGNAME CREATE_DEALER,T1.IS_WO,");
                sql.append(" NVL((SELECT COUNT(RD.DTL_ID) FROM TT_PART_DLR_RETURN_DTL RD WHERE RD.OUT_QTY > 0 AND RD.RETURN_ID = T1.RETURN_ID ),0) AS OUT_QTYS ");
                sql.append(" FROM TT_PART_DLR_RETURN_MAIN T1,TC_USER T2 WHERE T1.CREATE_BY=T2.USER_ID AND (T1.DEALER_ID=").append(logonUser.getDealerId());
                sql.append(" OR T1.SELLER_ID=").append(logonUser.getDealerId()).append(")");
               /* if (!"".equals(startDate)) {
                    sql.append(" AND to_date(T1.Create_Date)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
                }

                if (!"".equals(endDate)) {
                    sql.append(" AND to_date(T1.Create_Date)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
                }*/
            } else {//如果是一般服务商,就需要把退货单位是它自己的都查询出来

                sql.append("SELECT T1.RETURN_ID,\n");
                sql.append("       T1.RETURN_CODE,\n");
                sql.append("       T1.DEALER_NAME,\n");
                sql.append("       T2.NAME CREATE_NAME,\n");
                sql.append("       T1.CREATE_DATE,\n");
                sql.append("       T1.REMARK,\n");
                sql.append("       T1.APPLY_DATE,\n");
                sql.append("       T1.STATE,\n");
                sql.append("       T1.CREATE_ORG,\n");
                sql.append("       T1.CREATE_ORGNAME CREATE_DEALER,\n");
                sql.append("       T1.IS_WO,\n");
                sql.append("       NVL((SELECT COUNT(RD.DTL_ID)\n");
                sql.append("             FROM TT_PART_DLR_RETURN_DTL RD\n");
                sql.append("            WHERE RD.OUT_QTY > 0\n");
                sql.append("              AND RD.RETURN_ID = T1.RETURN_ID),\n");
                sql.append("           0) AS OUT_QTYS,\n");
                sql.append("       T1.VERIFY_DATE,\n");
                sql.append("       T3.NAME T3NAME,\n");
                sql.append("       T1.WL_DATE,\n");
                sql.append("       T4.NAME T4NAME,\n");
                sql.append("       T1.IN_DATE,\n");
                sql.append("       T5.NAME T5NAME,\n");
                sql.append("       T6.NAME T6NAME\n");
                sql.append("  FROM TT_PART_DLR_RETURN_MAIN T1,\n");
                sql.append("       TC_USER                 T2,\n");
                sql.append("       TC_USER                 T3,\n");
                sql.append("       TC_USER                 T4,\n");
                sql.append("       TC_USER                 T5,\n");
                sql.append("       TC_USER                 T6\n");
                sql.append(" WHERE T1.CREATE_BY = T2.USER_ID\n");
                sql.append("   AND T1.VERIFY_BY = T3.USER_ID(+)\n");
                sql.append("   AND T1.WL_BY = T4.USER_ID(+)\n");
                sql.append("   AND T1.IN_BY = T5.USER_ID(+)\n");
                sql.append("   AND T1.APPLY_BY = T6.USER_ID(+)");
                sql.append("   AND T1.DEALER_ID=").append(logonUser.getDealerId());
                //提交日期
                if (!CommonUtils.isEmpty(paramMap.get("startDate_t"))) {
                    sql.append(" AND to_date(T1.APPLY_DATE)>=").append("to_date('").append(paramMap.get("startDate_t")).append("','yyyy-MM-dd')\n");
                }

                if (!CommonUtils.isEmpty(paramMap.get("endDate_t"))) {
                    sql.append(" AND to_date(T1.APPLY_DATE)<=").append("to_date('").append(paramMap.get("endDate_t")).append("','yyyy-MM-dd')\n");
                }

                //审核日期
                if (!CommonUtils.isEmpty(paramMap.get("endDate_t"))) {
                    sql.append(" AND to_date(T1.VERIFY_DATE)>=").append("to_date('").append(paramMap.get("startDate_s")).append("','yyyy-MM-dd')\n");
                }

                if (!CommonUtils.isEmpty(paramMap.get("endDate_t"))) {
                    sql.append(" AND to_date(T1.VERIFY_DATE)<=").append("to_date('").append(paramMap.get("endDate_s")).append("','yyyy-MM-dd')\n");
                }

                //回运日期
                if (!CommonUtils.isEmpty(paramMap.get("startDate_h"))) {
                    sql.append(" AND to_date(T1.WL_DATE)>=").append("to_date('").append(paramMap.get("endDate_t")).append("','yyyy-MM-dd')\n");
                }

                if (!CommonUtils.isEmpty(paramMap.get("endDate_h"))) {
                    sql.append(" AND to_date(T1.WL_DATE)<=").append("to_date('").append(paramMap.get("endDate_h")).append("','yyyy-MM-dd')\n");
                }
                //验收日期
                if (!CommonUtils.isEmpty(paramMap.get("startDate"))) {
                    sql.append(" AND to_date(T1.In_date)>=").append("to_date('").append(paramMap.get("startDate")).append("','yyyy-MM-dd')\n");
                }

                if (!CommonUtils.isEmpty(paramMap.get("endDate"))) {
                    sql.append(" AND to_date(T1.In_date)<=").append("to_date('").append(paramMap.get("endDate")).append("','yyyy-MM-dd')\n");
                }

                //新增日期
                if (!CommonUtils.isEmpty(paramMap.get("createDate_c"))) {
                    sql.append(" AND to_date(T1.Create_date)>=").append("to_date('").append(paramMap.get("createDate_c")).append("','yyyy-MM-dd')\n");
                }

                if (!CommonUtils.isEmpty(paramMap.get("endDate_c"))) {
                    sql.append(" AND to_date(T1.Create_date)<=").append("to_date('").append(paramMap.get("endDate_c")).append("','yyyy-MM-dd')\n");
                }
            }
            if (!CommonUtils.isEmpty(paramMap.get("returnCode"))) {
                sql.append(" AND T1.RETURN_CODE LIKE '%")
                        .append(paramMap.get("returnCode")).append("%'\n");
            }
            if (!CommonUtils.isEmpty(paramMap.get("dealerName"))) {
                sql.append(" AND T1.DEALER_NAME LIKE '%")
                        .append(paramMap.get("dealerName")).append("%'\n");
            }

            if (!CommonUtils.isEmpty(paramMap.get("state"))) {
                sql.append(" AND T1.STATE=" + paramMap.get("state"));
            }
            if (!CommonUtils.isEmpty(paramMap.get("isWo"))) {
                sql.append(" AND T1.is_wo=" + paramMap.get("isWo"));
            }
            sql.append(" ORDER BY NVL(T1.APPLY_DATE,T1.CREATE_DATE) DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> queryPartDlrReturnDetailList(
            String returnId, String soCode, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("WITH SJ_RETURN AS\n");
            sql.append(" (SELECT R.RETURN_ID, R.PART_ID, SUM(R.RETURN_QTY) RETURN_QTY\n");
            sql.append("    FROM TT_PART_RETURN_RECORD R\n");
            sql.append("   GROUP BY R.RETURN_ID, R.PART_ID)\n");
            sql.append("SELECT T.DTL_ID,\n");
            sql.append("       T.PART_ID,\n");
            sql.append("       T.PART_CODE,\n");
            sql.append("       T.PART_OLDCODE,\n");
            sql.append("       T.UNIT,\n");
            sql.append("       T.BUY_PRICE,\n");
            sql.append("       T.BUY_AMOUNT,\n");
            sql.append("       T.PART_CNAME,\n");
            sql.append("       NVL(T.SALES_QTY, 0) BUY_QTY,\n");
            sql.append("       T.APPLY_QTY,\n");
            sql.append("       T.CHECK_QTY,\n");
            sql.append("       (SELECT SR.RETURN_QTY\n");
            sql.append("          FROM SJ_RETURN SR\n");
            sql.append("         WHERE SR.RETURN_ID = T.RETURN_ID\n");
            sql.append("           AND SR.PART_ID = T.PART_ID) RETURN_QTY,\n");
            sql.append("       (SELECT SR.RETURN_QTY\n");
            sql.append("          FROM SJ_RETURN SR\n");
            sql.append("         WHERE SR.RETURN_ID = T.RETURN_ID\n");
            sql.append("           AND SR.PART_ID = T.PART_ID) * T.BUY_PRICE RETURN_AMOUNT,\n");
            sql.append("       T.OUT_QTY,\n");
            sql.append("       (SELECT M.LOC_NAME\n");
            sql.append("          FROM TT_PART_LOACTION_DEFINE M\n");
            sql.append("         WHERE M.LOC_ID = T.INLOC_ID) LOC_NAME,\n");
            sql.append("       T.REMARK\n");
            sql.append("  FROM TT_PART_DLR_RETURN_DTL T, TT_PART_DLR_RETURN_MAIN T1\n");
            sql.append(" WHERE T.RETURN_ID = T1.RETURN_ID\n");
            sql.append("   AND T.RETURN_ID = " + returnId + "");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    /**
     * <p>Description: 获取经销商退货申请配件明细</p>
     * @param returnId
     * @param soCode
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> queryPartDlrReturnDetailList1(
            String returnId, String soCode) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");
//            sql.append("SELECT T.DTL_ID, T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.UNIT,T.BUY_PRICE,T.PART_CNAME,NVL(T.SALES_QTY,0) BUY_QTY, T.STOCK_QTY, T.APPLY_QTY,T.CHECK_QTY,T.OUT_QTY,T.REMARK ");
//            sql.append("  FROM TT_PART_DLR_RETURN_DTL T \n");
//            sql.append(" WHERE T.RETURN_ID= '"+returnId+"' \n");
            sql.append("SELECT T.DTL_ID,\n");
            sql.append("       T.PART_ID,\n");
            sql.append("       T.PART_CODE,\n");
            sql.append("       T.PART_OLDCODE,\n");
            sql.append("       T.UNIT,\n");
            sql.append("       T.BUY_PRICE,\n");
            sql.append("       T.PART_CNAME,\n");
            sql.append("       NVL(T.SALES_QTY, 0) BUY_QTY,\n");
            sql.append("       T.STOCK_QTY,\n");
            sql.append("       T3.NORMAL_QTY,\n");
            sql.append("       ITD.RETURN_QTY IN_RETURN_QTY,\n");
            sql.append("       T.APPLY_QTY,\n");
            sql.append("       T.CHECK_QTY,\n");
            sql.append("       T.OUT_QTY,\n");
            sql.append("       T.REMARK\n");
            sql.append("  FROM TT_PART_DLR_RETURN_DTL T\n");
            sql.append(" INNER JOIN TT_PART_DLR_RETURN_MAIN T2\n");
            sql.append("    ON T2.RETURN_ID = T.RETURN_ID\n");
            sql.append(" INNER JOIN TT_PART_DLR_INSTOCK_DTL ITD\n");
            sql.append("    ON ITD.PART_ID = T.PART_ID\n");
            sql.append("   AND ITD.IN_ID = T2.SO_ID\n");
            sql.append(" INNER JOIN TT_PART_BOOK T3\n");
            sql.append("    ON T3.PART_ID = T.PART_ID\n");
            sql.append("   AND T3.WH_ID = T2.STOCK_OUT\n");
            sql.append("   AND T3.ORG_ID = T2.DEALER_ID\n");
            sql.append(" WHERE T.RETURN_ID = '"+returnId+"'\n");
            
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    /**
     * @param po
     * @param childorgId 121xx11
     * @param soPara
     * @param soCode
     * @param whId
     * @param beginTime
     * @param endTime
     * @param curPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageResult<Map<String, Object>> queryPartInfoList(TtPartDefinePO po, String childorgId,
                                                             String soPara, String soCode, String whId, String beginTime, String endTime, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            if (!"".equals(soCode) || !"".equals(beginTime) || !"".equals(endTime)) {//如果有销售单号或者销售日期
                /*sql.append(" SELECT T.PART_ID, \n");
                sql.append("        T.PART_CODE, \n");
				sql.append("        T.PART_OLDCODE, \n");
				sql.append("        T.PART_CNAME, \n");
				sql.append("        T.UNIT, \n");
				sql.append("        T.SALES_QTY BUY_QTY, \n");
				sql.append("        T.BUY_PRICE, \n");
				sql.append("        nvl((SELECT V.NORMAL_QTY \n");
				sql.append("              FROM VW_PART_STOCK V \n");
				sql.append("             WHERE V.PART_ID = T.PART_ID \n");
				sql.append("               AND V.WH_ID = A.WH_ID), \n");
				sql.append("            0) NORMAL_QTY \n");
				sql.append("   FROM TT_PART_DLR_INSTOCK_MAIN A, TT_PART_SO_DTL T \n");
				sql.append("  WHERE T.SO_ID = A.SO_ID \n");*/

                sql.append("SELECT T.PART_ID,\n");
                sql.append("       T.PART_CODE,\n");
                sql.append("       T.PART_OLDCODE,\n");
                sql.append("       T.PART_CNAME,\n");
                sql.append("       T.UNIT,\n");
                sql.append("       T.IN_QTY BUY_QTY,\n");
                sql.append("       T.RETURN_QTY,\n");
                sql.append("       (SELECT OD.SALE_PRICE\n");
                sql.append("          FROM TT_PART_OUTSTOCK_DTL OD\n");
                sql.append("         WHERE OD.OUT_ID = OM.OUT_ID\n");
                sql.append("           AND OD.PART_ID = T.PART_ID AND ROWNUM=1) BUY_PRICE,\n");
                sql.append("       NVL((SELECT SUM(V.NORMAL_QTY)\n");
                sql.append("             FROM VW_PART_STOCK V\n");
                sql.append("            WHERE V.PART_ID = T.PART_ID\n");
                sql.append("              AND V.WH_ID = IM.WH_ID),\n");
                sql.append("           0) NORMAL_QTY\n");
                sql.append("  FROM TT_PART_DLR_INSTOCK_DTL  T,\n");
                sql.append("       TT_PART_DLR_INSTOCK_MAIN IM,\n");
                sql.append("       TT_PART_TRANS            S,\n");
                sql.append("       TT_PART_OUTSTOCK_MAIN    OM,\n");
                sql.append("       TT_PART_TRANS_PLAN       TP\n");
                sql.append(" WHERE T.IN_ID = IM.IN_ID\n");
                sql.append("   AND IM.TRANS_ID = S.TRANS_ID\n");
                sql.append("   AND S.OUT_ID = OM.OUT_ID\n");
                sql.append("   AND OM.TRPLAN_ID = TP.TRPLAN_ID");
                sql.append("   AND IM.DEALER_ID = ").append(childorgId);

//                if (!"".equals(soCode)) {
//                    sql.append("  AND TP.TRPLAN_CODE='").append(soCode).append("'");
//                }
                if (!"".equals(soCode)) {
                    sql.append("  AND IM.IN_CODE ='").append(soCode).append("'");
                }

                if (!"".equals(whId)) {
                    sql.append(" AND IM.WH_ID=").append(whId);
                }

                if (!"".equals(beginTime)) {
                    sql.append(" AND to_date(IM.CREATE_DATE)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')");
                }

                if (!"".equals(endTime)) {
                    sql.append(" AND to_date(IM.CREATE_DATE)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')");
                }
            } else {
                if (!"".equals(whId)) {
                    sql.append("SELECT T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,NVL(T.UNIT,'件') UNIT,0 BUY_QTY,");
                    sql.append(" PKG_PART.F_GETPRICE(").append(childorgId);
                    sql.append(",t.PART_ID) BUY_PRICE,nvl(T.NORMAL_QTY,0) NORMAL_QTY FROM VW_PART_STOCK T");
                    sql.append(" WHERE T.WH_ID=").append(whId);
                } else {
                    sql.append("SELECT T.PART_ID, T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,T.UNIT,0 BUY_QTY,");
                    sql.append(" PKG_PART.F_GETPRICE(").append(childorgId);
                    sql.append(",T.PART_ID) BUY_PRICE FROM TT_PART_DEFINE T WHERE T.STATE=")
                            .append(Constant.STATUS_ENABLE);
                    sql.append(" AND T.STATUS=1");
                }
            }

            if (po.getPartCode() != null && !"".equals(po.getPartCode())) {
                sql.append(" AND t.part_code LIKE '%")
                        .append(po.getPartCode()).append("%'\n");
            }
            if ((po.getPartOldcode() != null) && !"".equals(po.getPartOldcode())) {
                sql.append(" AND t.part_oldcode LIKE '%")
                        .append(po.getPartOldcode()).append("%'\n");
            }
            if ((po.getPartCname() != null) && !"".equals(po.getPartCname())) {
                sql.append(" AND t.part_cname LIKE '%")
                        .append(po.getPartCname()).append("%'\n");
            }
            sql.append(" ORDER BY T.PART_OLDCODE ASC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public Map<String, Object> getSoInfoByCode(String soCode) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            /*sql.append("SELECT T.SO_ID,T.ORDER_TYPE,T.SALE_DATE FROM TT_PART_OUTSTOCK_MAIN T WHERE T.SO_CODE=")
            .append("'").append(soCode).append("'");
*/
            sql.append("SELECT P.TRPLAN_ID SO_ID, NULL ORDER_TYPE, P.CREATE_DATE SALE_DATE\n");
            sql.append("  FROM TT_PART_TRANS_PLAN P\n");
            sql.append(" WHERE P.TRPLAN_CODE = '" + soCode + "'");

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * <p>Description: 获取经销商申请配件退货主要信息</p>
     * @param returnId 退货id
     * @return
     * @throws Exception
     */
    public Map<String, Object> getPartDlrReturnMainInfo(String returnId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T1.RETURN_ID, \n");
            sql.append("        T1.RETURN_CODE, \n");
            sql.append("        T1.CREATE_ORGNAME ORG_NAME, \n");
            sql.append("        T1.DEALER_ID, \n");
            sql.append("        T1.DEALER_CODE, \n");
            sql.append("        T1.DEALER_NAME, \n");
            sql.append("        T1.SELLER_ID, \n");
            sql.append("        T1.SELLER_NAME, \n");
            sql.append("        T1.SO_CODE, \n");
            sql.append("        T1.IN_CODE, \n");
            sql.append("        T1.STOCK_OUT, \n");
            sql.append("        T2.WH_NAME, \n");
            sql.append("        T3.NAME, \n");
            sql.append("        T1.REMARK, \n");
            sql.append("        T1.REMARK1 \n");
            sql.append("   FROM TT_PART_DLR_RETURN_MAIN T1,TT_PART_WAREHOUSE_DEFINE T2,TC_USER T3 \n");
            sql.append("  WHERE T1.STOCK_OUT=T2.WH_ID AND T1.CREATE_BY=T3.USER_ID AND T1.RETURN_ID = ").append(returnId);
            return pageQueryMap(sql.toString(), null, getFunName());

        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> getReturnApplyInfo(RequestWrapper request, int flag, AclUserBean logonUser) throws Exception {
        List<Map<String, Object>> list;
        try {
            String returnCode = CommonUtils.checkNull(request.getParamValue("RETURN_CODE"));//退货单号
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//退货单位
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间

            StringBuffer sql = new StringBuffer("");
            if (flag == 0) {//如果是车厂,就需要把销售单位是车厂的申请都查询出来
                sql.append("SELECT T1.RETURN_ID,T1.RETURN_CODE,T1.DEALER_NAME,T2.NAME CREATE_NAME,T1.CREATE_DATE,T1.REMARK,T1.APPLY_DATE,T1.STATE,T1.CREATE_ORG,T1.CREATE_ORGNAME CREATE_DEALER, TC.CODE_DESC");
                sql.append(" FROM TT_PART_DLR_RETURN_MAIN T1,TC_USER T2, TC_CODE TC WHERE T1.CREATE_BY=T2.USER_ID AND T1.SELLER_ID=");
                sql.append(logonUser.getOrgId());
                sql.append(" AND T1.STATE = TC.CODE_ID(+) ");
            } else if (flag == 1) {//如果是供应中心,就需要把退货单位或销售单位是它的都查询出来
                sql.append("SELECT T1.RETURN_ID,T1.RETURN_CODE,T1.DEALER_NAME,T2.NAME CREATE_NAME,T1.CREATE_DATE,T1.REMARK,T1.APPLY_DATE,T1.STATE,T1.CREATE_ORG,T1.CREATE_ORGNAME CREATE_DEALER, TC.CODE_DESC");
                sql.append(" FROM TT_PART_DLR_RETURN_MAIN T1,TC_USER T2, TC_CODE TC WHERE T1.CREATE_BY=T2.USER_ID AND (T1.DEALER_ID=").append(logonUser.getDealerId());
                sql.append(" OR T1.SELLER_ID=").append(logonUser.getDealerId()).append(")");
                sql.append(" AND T1.STATE = TC.CODE_ID(+) ");
            } else {//如果是一般服务商,就需要把退货单位是它自己的都查询出来
                sql.append("SELECT T1.RETURN_ID,T1.RETURN_CODE,T1.DEALER_NAME,T2.NAME CREATE_NAME,T1.CREATE_DATE,T1.REMARK,T1.APPLY_DATE,T1.STATE,T1.CREATE_ORG,T1.CREATE_ORGNAME CREATE_DEALER, TC.CODE_DESC");
                sql.append(" FROM TT_PART_DLR_RETURN_MAIN T1,TC_USER T2, TC_CODE TC WHERE T1.CREATE_BY=T2.USER_ID AND T1.DEALER_ID=");
                sql.append(logonUser.getDealerId());
                sql.append(" AND T1.STATE = TC.CODE_ID(+) ");
            }
            if (!"".equals(returnCode)) {
                sql.append(" AND T1.RETURN_CODE LIKE '%")
                        .append(returnCode).append("%'\n");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND T1.DEALER_NAME LIKE '%")
                        .append(dealerName).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append(" ORDER BY NVL(T1.APPLY_DATE,T1.CREATE_DATE) DESC");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;
    }

    public List<Map<String, Object>> getSoDtlInfoByPartId(String soCode, String[] partIds) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("select T2.PART_ID,T2.SLINE_ID,T2.STOCK_QTY,T2.LINE_NO,T2.OUTSTOCK_QTY,T2.SALE_PRICE from TT_PART_OUTSTOCK_MAIN T1,TT_PART_OUTSTOCK_DTL T2 WHERE T1.OUT_ID=T2.OUT_ID AND T1.IN_CODE='");
            sql.append(soCode).append("'");
            sql.append(" AND　T2.PART_ID　IN(");
            for (int i = 0; i < partIds.length; i++) {
                sql.append(partIds[i]).append(",");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")");
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<Map<String, Object>> queryChildOrg(String dealerId) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("select t.CHILDORG_ID,t.CHILDORG_CODE,t.CHILDORG_NAME from TT_PART_SALES_RELATION t where t.parentorg_id=");
            sql.append(dealerId).append(" or t.childorg_id=").append(dealerId);
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<Map<String, Object>> queryParentOrg(String dealerId) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.PARENTORG_ID,T.PARENTORG_CODE,T.PARENTORG_NAME FROM TT_PART_SALES_RELATION T WHERE T.CHILDORG_ID=")
                    .append(dealerId);
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<TtPartWarehouseDefinePO> getPartWareHouseList(String str_id) throws Exception {
        try {
            TtPartWarehouseDefinePO po = new TtPartWarehouseDefinePO();
            po.setState(Constant.STATUS_ENABLE);
            po.setStatus(1);
            po.setOrgId(CommonUtils.parseLong(str_id));
            List<TtPartWarehouseDefinePO> list = select(po);
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<String, Object> queryNormalQty(Long whId, Long partId, Long dealerId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.NORMAL_QTY,T.PART_OLDCODE FROM VW_PART_STOCK T WHERE T.PART_ID=").append(partId);
            sql.append(" AND T.ORG_ID=").append(dealerId);
            sql.append(" AND T.WH_ID=").append(whId);
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param orgId
     * @param flag
     * @param childOrgCode
     * @param childOrgName
     * @param curPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageResult<Map<String, Object>> queryChildOrgList(String orgId,
                                                             int flag, String childOrgCode, String childOrgName, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            if (flag == 0) {//如果是车厂
                sql.append("SELECT T.CHILDORG_ID,T.CHILDORG_CODE,T.CHILDORG_NAME,T1.WH_ID,T1.WH_NAME FROM VW_PART_SALES_RELATION T,TT_PART_WAREHOUSE_DEFINE T1");
                sql.append("  WHERE T.CHILDORG_ID=T1.ORG_ID(+) AND T.PARENTORG_ID=")
                        .append(orgId);
            } else {
                sql.append("SELECT T.CHILDORG_ID,T.CHILDORG_CODE,T.CHILDORG_NAME,T1.WH_ID,T1.WH_NAME FROM VW_PART_SALES_RELATION T,TT_PART_WAREHOUSE_DEFINE T1  WHERE T.CHILDORG_ID=T1.ORG_ID(+) AND (T.CHILDORG_ID=")
                        .append(orgId);
                sql.append(" or t.PARENTORG_ID=").append(orgId).append(")");
            }
            if (!childOrgCode.equals("")) {
                sql.append(" and UPPER(t.CHILDORG_CODE) like '%").append(childOrgCode.trim().toUpperCase()).append("%'");
            }
            if (!childOrgName.equals("")) {
                sql.append(" and t.CHILDORG_NAME like '%").append(childOrgName.trim()).append("%'");
            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    /**
     * @param childorgId
     * @param saleOrgId
     * @param soCode
     * @param partOldCode
     * @param beginTime
     * @param endTime
     * @param curPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageResult<Map<String, Object>> querySoCodeList(String childorgId, String saleOrgId,
                                                           String soCode, String partOldCode, String beginTime, String endTime, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
//			sql.append("SELECT DISTINCT T.DEALER_NAME,T.SELLER_NAME,T.SO_CODE FROM TT_PART_DLR_INSTOCK_MAIN T,");
//			sql.append("TT_PART_DLR_INSTOCK_DTL T1,TT_PART_SO_MAIN T2 WHERE T.IN_ID=T1.IN_ID AND T.SO_ID=T2.SO_ID");

            sql.append("SELECT DISTINCT T.DEALER_NAME, T.SELLER_NAME, TP.TRPLAN_CODE SO_CODE, T.IN_ID, T.IN_CODE \n");
            sql.append("  FROM TT_PART_DLR_INSTOCK_MAIN T,\n");
            sql.append("       TT_PART_DLR_INSTOCK_DTL  T1,\n");
            sql.append("       TT_PART_TRANS            T2,\n");
            sql.append("       TT_PART_OUTSTOCK_MAIN    OM,\n");
            sql.append("       TT_PART_TRANS_PLAN       TP\n");
            sql.append(" WHERE T.IN_ID = T1.IN_ID\n");
            sql.append("   AND T.TRANS_ID = T2.TRANS_ID\n");
            sql.append("   AND T2.OUT_ID = OM.OUT_ID\n");
            sql.append("   AND OM.TRPLAN_ID = TP.TRPLAN_ID");

            sql.append(" AND T.DEALER_ID=").append(childorgId + "\n");
            sql.append(" AND T.SELLER_ID=").append(saleOrgId + "\n");
            
//            if (!soCode.equals("")) {
//                sql.append(" and T.SO_CODE like '%").append(soCode).append("%'\n");
//            }
            // 将销售单号改为入库单号
            if (!soCode.equals("")) {
                sql.append(" and T.IN_CODE like '%").append(soCode).append("%'\n");
            }
            if (!partOldCode.equals("")) {
                sql.append(" and T1.PART_OLDCODE like '%").append(partOldCode).append("%'\n");
            }
            if (!"".equals(beginTime)) {
                sql.append(" AND to_date(T.CREATE_DATE)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')\n");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND to_date(T.CREATE_DATE)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')\n");
            }
//            if (!"".equals(beginTime)) {
//                sql.append(" AND to_date(T2.CREATE_DATE)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')\n");
//            }
//            
//            if (!"".equals(endTime)) {
//                sql.append(" AND to_date(T2.CREATE_DATE)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')\n");
//            }
//            sql.append("  AND exists(SELECT 1 FROM TT_PART_OUTSTOCK_MAIN ot WHERE ot.so_code=t.so_code)\n");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }
    
    /**
     * <p>Description: 获取经销商入库信息</p>
     * @param childorgId 销售单位id
     * @param saleOrgId 经销商id
     * @param inCode 入库单号
     * @return
     */
    public List<Map<String, Object>> queryInCodeList(String childorgId, String saleOrgId, String inCode){
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT T.DEALER_NAME, \n");
        sql.append("        T.SELLER_NAME, \n");
        sql.append("        TP.TRPLAN_ID SO_ID, \n");
        sql.append("        TP.TRPLAN_CODE SO_CODE, \n");
        sql.append("        TP.CREATE_DATE SO_DATE, \n");
        sql.append("        T.IN_ID, \n");
        sql.append("        T.IN_CODE, \n");
        sql.append("        T.CREATE_DATE  IN_DATE \n");
        sql.append("   FROM TT_PART_DLR_INSTOCK_MAIN T,\n");
//        sql.append("        TT_PART_DLR_INSTOCK_DTL  T1,\n");
        sql.append("        TT_PART_TRANS            T2,\n");
        sql.append("        TT_PART_OUTSTOCK_MAIN    OM,\n");
        sql.append("        TT_PART_TRANS_PLAN       TP\n");
//        sql.append("  WHERE T.IN_ID = T1.IN_ID\n");
        sql.append("  WHERE T.TRANS_ID = T2.TRANS_ID\n");
        sql.append("    AND T2.OUT_ID = OM.OUT_ID\n");
        sql.append("    AND OM.TRPLAN_ID = TP.TRPLAN_ID\n");
        sql.append("    AND T.DEALER_ID='" + childorgId + "' \n");
        sql.append("    AND T.SELLER_ID='" + saleOrgId + "'\n");
        sql.append("    AND T.IN_CODE='" + inCode + "'\n");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
    
    /**
     * <p>Description: 经销商入库明细</p>
     * @param childorgId
     * @param saleOrgId
     * @param inCode
     * @return
     */
    public List<Map<String, Object>> queryInStockDtlList(String childorgId, String saleOrgId, String inCode, String partIds[]){
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT T.DEALER_NAME, \n");
        sql.append("        T.SELLER_NAME, \n");
        sql.append("        TP.TRPLAN_ID SO_ID, \n");
        sql.append("        TP.TRPLAN_CODE SO_CODE, \n");
        sql.append("        TP.CREATE_DATE SO_DATE, \n");
        sql.append("        T.IN_ID, \n");
        sql.append("        T.IN_CODE, \n");
        sql.append("        T.CREATE_DATE  IN_DATE \n");
        sql.append("   FROM TT_PART_DLR_INSTOCK_MAIN T,\n");
        sql.append("        TT_PART_DLR_INSTOCK_DTL  T1,\n");
        sql.append("        TT_PART_TRANS            T2,\n");
        sql.append("        TT_PART_OUTSTOCK_MAIN    OM,\n");
        sql.append("        TT_PART_TRANS_PLAN       TP\n");
        sql.append("  WHERE T.IN_ID = T1.IN_ID\n");
        sql.append("    AND T.TRANS_ID = T2.TRANS_ID\n");
        sql.append("    AND T2.OUT_ID = OM.OUT_ID\n");
        sql.append("    AND OM.TRPLAN_ID = TP.TRPLAN_ID\n");
        sql.append("    AND T.DEALER_ID='" + childorgId + "' \n");
        sql.append("    AND T.SELLER_ID='" + saleOrgId + "'\n");
        sql.append("    AND T.IN_CODE='" + inCode + "'\n");
        sql.append("    AND T1.PART_ID　IN(");
        for (int i = 0; i < partIds.length; i++) {
            sql.append(partIds[i] +",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") \n");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * 退货明细查询
     *
     * @param returnId
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getPartDlrReturnDtl(String returnId) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T.DTL_ID, \n");
            sql.append("        T.PART_ID, \n");
            sql.append("        T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.UNIT, \n");
            sql.append("        T.BUY_PRICE, \n");
            sql.append("        T.BUY_AMOUNT, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        NVL(T.SALES_QTY, 0) BUY_QTY, \n");
            sql.append("        T.APPLY_QTY, \n");
            sql.append("        T.CHECK_QTY, \n");
            sql.append("        T.RETURN_QTY, \n");
            sql.append("        T.OUT_QTY, \n");
            sql.append("        (SELECT M.LOC_NAME \n");
            sql.append("           FROM TT_PART_LOACTION_DEFINE M \n");
            sql.append("          WHERE M.WH_ID = T1.STOCK_OUT \n");
            sql.append("            AND M.PART_ID = T.PART_ID \n");
            sql.append("            AND M.ORG_ID = T1.DEALER_ID AND　ROWNUM=1) LOC_NAME, \n");
            sql.append("        T.REMARK \n");
            sql.append("   FROM TT_PART_DLR_RETURN_DTL T, TT_PART_DLR_RETURN_MAIN T1 \n");
            sql.append("  WHERE T.RETURN_ID = T1.RETURN_ID \n");
            sql.append("    AND T.RETURN_ID = ").append(CommonUtils.parseLong(returnId));
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-12-20
     * @Title : 验证销售申请是否存在出库（入库）情况
     */
    public List<Map<String, Object>> orderStateCheck(String sqlStr) {
        List<Map<String, Object>> list;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT COUNT(RD.DTL_ID) AS OUT_QTYS FROM TT_PART_DLR_RETURN_DTL RD WHERE RD.OUT_QTY > 0 ");
        sql.append(sqlStr);
        list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-12-20
     * @Title : 获取库存信息
     */
    public List<Map<String, Object>> getStock(String sqlStr) {
        List<Map<String, Object>> list;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT VW.* FROM VW_PART_STOCK VW  WHERE 1 = 1 ");
        sql.append(sqlStr);
        list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param returnCode
     * @param dealerName
     * @param startDate
     * @param endDate
     * @param flag
     * @param logonUser
     * @param curPage
     * @param pageSize
     * @return
     * @throws Exception
     */

    public PageResult<Map<String, Object>> queryPartDlrReturnBackList(
            String returnCode, String dealerName, String startDate, String endDate, int flag, AclUserBean logonUser,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            if (flag == 0) {//如果是车厂,就需要把销售单位是车厂的申请都查询出来
                sql.append("SELECT T1.RETURN_ID,T1.RETURN_CODE,T1.DEALER_NAME,T2.NAME CREATE_NAME,T1.CREATE_DATE,T1.REMARK,T1.APPLY_DATE,T1.STATE,T1.CREATE_ORG,T1.CREATE_ORGNAME CREATE_DEALER,");
                sql.append(" NVL((SELECT COUNT(RD.DTL_ID) FROM TT_PART_DLR_RETURN_DTL RD WHERE RD.OUT_QTY > 0 AND RD.RETURN_ID = T1.RETURN_ID ),0) AS OUT_QTYS ");
                sql.append(" FROM TT_PART_DLR_RETURN_MAIN T1,TC_USER T2 WHERE T1.CREATE_BY=T2.USER_ID AND T1.SELLER_ID=");
                sql.append(logonUser.getOrgId());
            } else if (flag == 1) {//如果是供应中心,就需要把退货单位或销售单位是它的都查询出来
                sql.append("SELECT T1.RETURN_ID,T1.RETURN_CODE,T1.DEALER_NAME,T2.NAME CREATE_NAME,T1.CREATE_DATE,T1.REMARK,T1.APPLY_DATE,T1.STATE,T1.CREATE_ORG,T1.CREATE_ORGNAME CREATE_DEALER,");
                sql.append(" NVL((SELECT COUNT(RD.DTL_ID) FROM TT_PART_DLR_RETURN_DTL RD WHERE RD.OUT_QTY > 0 AND RD.RETURN_ID = T1.RETURN_ID ),0) AS OUT_QTYS ");
                sql.append(" FROM TT_PART_DLR_RETURN_MAIN T1,TC_USER T2 WHERE T1.CREATE_BY=T2.USER_ID AND (T1.DEALER_ID=").append(logonUser.getDealerId());
                sql.append(" OR T1.SELLER_ID=").append(logonUser.getDealerId()).append(")");
            } else {//如果是一般服务商,就需要把退货单位是它自己的都查询出来
                sql.append("SELECT T1.RETURN_ID,T1.RETURN_CODE,T1.DEALER_NAME,T2.NAME CREATE_NAME,T1.CREATE_DATE,T1.REMARK,T1.APPLY_DATE,T1.STATE,T1.CREATE_ORG,T1.CREATE_ORGNAME CREATE_DEALER,");
                sql.append(" NVL((SELECT COUNT(RD.DTL_ID) FROM TT_PART_DLR_RETURN_DTL RD WHERE RD.OUT_QTY > 0 AND RD.RETURN_ID = T1.RETURN_ID ),0) AS OUT_QTYS ");
                sql.append(" FROM TT_PART_DLR_RETURN_MAIN T1,TC_USER T2 WHERE T1.CREATE_BY=T2.USER_ID AND T1.DEALER_ID=");
                sql.append(logonUser.getDealerId());
            }
            if (!"".equals(returnCode)) {
                sql.append(" AND T1.RETURN_CODE LIKE '%")
                        .append(returnCode).append("%'\n");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND T1.DEALER_NAME LIKE '%")
                        .append(dealerName).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append(" AND T1.STATE =").append(Constant.PART_DLR_RETURN_STATUS_04);
            sql.append(" ORDER BY NVL(T1.APPLY_DATE,T1.CREATE_DATE) DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> PartDlrReturnInStockQuery(
            String returnCode, String dealerName, String startDate, String endDate, int flag, AclUserBean logonUser, String partOldcode,
            String partCname, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT T4.RETURN_CODE,\n");
            sql.append("       T4.DEALER_CODE,\n");
            sql.append("       T4.DEALER_NAME,\n");
            sql.append("       RR.PART_OLDCODE,\n");
            sql.append("       RR.PART_CNAME,\n");
            sql.append("       RR.UNIT,\n");
            sql.append("       RR.RETURN_QTY,\n");
            sql.append("       RR.CREATE_DATE,\n");
            sql.append("       TU.NAME\n");
            sql.append("  FROM TT_PART_DLR_RETURN_MAIN T4,\n");
            sql.append("       TT_PART_DLR_RETURN_DTL  T5,\n");
            sql.append("       TT_PART_RETURN_RECORD   RR,\n");
            sql.append("       TC_USER                 TU\n");
            sql.append(" WHERE T4.RETURN_ID = T5.RETURN_ID\n");
            sql.append("   AND T4.RETURN_ID = RR.RETURN_ID\n");
            sql.append("   AND T5.PART_ID = RR.PART_ID\n");
            sql.append("   AND RR.CREATE_BY = TU.USER_ID\n");
            if (flag == 0) {
                sql.append("   AND T4.SELLER_ID =" + logonUser.getOrgId() + "\n");
            } else if (flag == 1) {
                sql.append("   AND T4.SELLER_ID =" + logonUser.getDealerId() + "\n");
            } else {
                sql.append("   AND T4.DEALER_ID =" + logonUser.getDealerId() + "\n");
            }
            sql.append("   AND RR.RETURN_QTY > 0\n");
            if (!"".equals(startDate) && null != startDate) {
                sql.append("   AND TRUNC(RR.CREATE_DATE) >= TO_DATE('" + startDate + "', 'yyyy-mm-dd')\n");
            }
            if (!"".equals(endDate) && null != endDate) {
                sql.append("   AND TRUNC(RR.CREATE_DATE) <= TO_DATE('" + endDate + "', 'yyyy-mm-dd')");
            }
            if (!"".equals(partOldcode) && null != partOldcode) {
                sql.append("  AND rr.part_oldcode LIKE '%" + partOldcode.toUpperCase() + "%'\n");
            }
            if (!"".equals(partCname) && null != partCname) {
                sql.append("  AND rr.part_cname LIKE '%" + partCname + "%'");
            }
            if (!"".equals(returnCode) && null != returnCode) {
                sql.append("  AND rr.return_code LIKE '%" + returnCode.toUpperCase() + "%'\n");
            }
            if (!"".equals(dealerName) && null != dealerName) {
                sql.append("  AND t4.dealer_name LIKE '%" + dealerName + "%'\n");
            }
            sql.append(" ORDER BY RR.CREATE_DATE DESC");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }
    
    /**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 描述:银翔原型报错修复
     * 新增方法
     * Date:2017-06-29
     * java：PartDlrReturnApplyQuery.java
     */
//    public PageResult<Map<String, Object>> queryPartDlrReturnList(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16) throws Exception {
//    	return null ;
//    }
}
