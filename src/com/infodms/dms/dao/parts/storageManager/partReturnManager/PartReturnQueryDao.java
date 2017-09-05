package com.infodms.dms.dao.parts.storageManager.partReturnManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.po.TtPartReturnRecordPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartReturnQueryDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartReturnQueryDao.class);
    private static final PartReturnQueryDao dao = new PartReturnQueryDao();

    private PartReturnQueryDao() {

    }

    public static final PartReturnQueryDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> queryPartDlrReturnList(
            TtPartReturnRecordPO po, String sellerId, String dealerName, String startDate, String endDate, String state,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        ActionContext act = ActionContext.getContext();
//		RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("WITH PART_DLR_RETURN AS\n");
            sql.append(" (SELECT R.RETURN_ID, R.PART_ID, SUM(R.RETURN_QTY) RETURN_QTY\n");
            sql.append("    FROM TT_PART_RETURN_RECORD R\n");
            sql.append("   GROUP BY R.RETURN_ID, R.PART_ID)\n");
            sql.append("SELECT A.*,\n");
            sql.append("       B.WH_NAME STOCK_IN_NAME,\n");
            sql.append("       CASE\n");
            sql.append("         WHEN A.CHECK_QTY = A.RETURN_QTY THEN\n");
            sql.append("          92361006\n");
            sql.append("         ELSE\n");
            sql.append("          A.STATE\n");
            sql.append("       END STATE2,\n");
            sql.append("       CASE\n");
            sql.append("         WHEN A.STATE > 92361004 THEN\n");
            sql.append("          A.CHECK_QTY\n");
            sql.append("         ELSE\n");
            sql.append("          0\n");
            sql.append("       END BACK_QTY\n");
            sql.append("  FROM (SELECT T4.RETURN_CODE,\n");
            sql.append("               T4.DEALER_CODE,\n");
            sql.append("               T4.DEALER_NAME,\n");
            sql.append("               T5.PART_CODE,\n");
            sql.append("               T5.PART_OLDCODE,\n");
            sql.append("               T5.PART_CNAME,\n");
            sql.append("               T5.UNIT,\n");
            sql.append("               CASE\n");
            sql.append("                 WHEN T4.SO_CODE IS NULL THEN\n");
            sql.append("                  '无单退货'\n");
            sql.append("                 WHEN T4.SO_CODE IS NOT NULL THEN\n");
            sql.append("                  '有单退货'\n");
            sql.append("               END RETURN_TYPE,\n");
            sql.append("               (SELECT C.CODE_DESC\n");
            sql.append("                  FROM TT_PART_DEFINE D, TC_CODE C\n");
            sql.append("                 WHERE D.PART_TYPE = C.CODE_ID\n");
            sql.append("                   AND T5.PART_ID = D.PART_ID) PART_TYPE,\n");
            sql.append("\n");
            sql.append("               (SELECT RETURN_QTY\n");
            sql.append("                  FROM PART_DLR_RETURN DR\n");
            sql.append("                 WHERE DR.PART_ID = T5.PART_ID\n");
            sql.append("                   AND DR.RETURN_ID = T5.RETURN_ID) RETURN_QTY,\n");
            sql.append("               T5.BUY_PRICE,\n");
            sql.append("               T5.BUY_AMOUNT,\n");
            sql.append("               CASE\n");
            sql.append("                 WHEN T4.SELLER_ID = 2010010100070674 THEN\n");
            sql.append("                  SP.SALE_PRICE3\n");
            sql.append("                 WHEN T4.SELLER_ID != 2010010100070674 THEN\n");
            sql.append("                  0\n");
            sql.append("               END SALE_PRICE3,\n");
            sql.append("               CASE\n");
            sql.append("                 WHEN T4.SELLER_ID = 2010010100070674 THEN\n");
            sql.append("                  TO_CHAR(SP.SALE_PRICE3 * T5.RETURN_QTY, 'fm999,999,999.00')\n");
            sql.append("                 WHEN T4.SELLER_ID != 2010010100070674 THEN\n");
            sql.append("                  '0'\n");
            sql.append("               END RETURN_AMOUNT,\n");
            sql.append("               T2.NAME,\n");
            sql.append("               T3.WH_NAME STOCK_OUT_NAME,\n");
            sql.append("               T4.STOCK_IN,\n");
            sql.append("               T4.STATE,\n");
            sql.append("               T4.RETURN_DATE CREATE_DATE,\n");
            sql.append("               T5.APPLY_QTY,\n");
            sql.append("               T5.CHECK_QTY\n");
            sql.append("          FROM TT_PART_DLR_RETURN_MAIN  T4,\n");
            sql.append("               TT_PART_DLR_RETURN_DTL   T5,\n");
            sql.append("               TC_USER                  T2,\n");
            sql.append("               TT_PART_WAREHOUSE_DEFINE T3,\n");
            sql.append("               TT_PART_SALES_PRICE      SP\n");
            sql.append("         WHERE T4.RETURN_ID = T5.RETURN_ID\n");
            sql.append("           AND T5.PART_ID = SP.PART_ID\n");
            sql.append("           AND T4.CREATE_BY = T2.USER_ID\n");
            sql.append("           AND T4.STOCK_OUT = T3.WH_ID\n");
            sql.append("           AND T4.STATUS = 1");
            if (logonUser.getDealerId() == null) {
                sql.append("  and T4.SELLER_ID=").append(sellerId).append("\n");
                sql.append("  AND T4.state IN (92361002,92361004,92361005,92361006)");
            } else
                sql.append("  and T4.DEALER_ID=").append(sellerId).append("\n");

            if (!"".equals(po.getReturnCode())) {
                sql.append(" AND T4.RETURN_CODE LIKE '%")
                        .append(po.getReturnCode()).append("%'\n");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND T4.DEALER_NAME LIKE '%").append(dealerName).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T4.IN_Date)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')").append("\n");
                ;
            }
            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T4.IN_Date)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')").append("\n");
                ;
            }
            if (!"".equals(po.getPartCode())) {
                sql.append(" AND T5.PART_CODE LIKE '%").append(po.getPartCode()).append("%'\n");
            }
            if (!"".equals(po.getPartOldcode())) {
                sql.append(" AND T5.PART_OLDCODE LIKE '%").append(po.getPartOldcode()).append("%'\n");
            }
            if (!"".equals(po.getPartCname())) {
                sql.append(" AND T5.PART_CNAME LIKE '%").append(po.getPartCname()).append("%'\n");
            }
            if (!"".equals(state) && null != state) {
                if (state.equals(Constant.PART_DLR_RETURN_STATUS_05 + "")) {
                    sql.append(" AND T4.STATE=" + state).append("\n");
                    sql.append(" AND T5.CHECK_QTY != NVL(T5.RETURN_QTY, 0)\n");

                } else if (state.equals(Constant.PART_DLR_RETURN_STATUS_06 + "")) {
                    sql.append(" AND T5.CHECK_QTY = T5.RETURN_QTY\n");
                } else {
                    sql.append(" AND T4.STATE=" + state).append("\n");
                }

            }

            sql.append(" AND T4.STATE<>92361007\n");
            sql.append(" ) A,TT_PART_WAREHOUSE_DEFINE B WHERE A.STOCK_IN=B.WH_ID(+)\n");
            sql.append(" ORDER BY A.dealer_code,a.part_oldcode\n");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;

    }

    public PageResult<Map<String, Object>> queryPartReturnList(
            TtPartReturnRecordPO po, String startDate, String endDate,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("  SELECT T1.Return_Id,\n");
            sql.append("         T1.RETURN_CODE,\n");
            sql.append("         T4.ORG_NAME DEALER_NAME,\n");
            sql.append("         T4.RETURN_TYPE,\n");
            sql.append("         T1.PART_CODE,\n");
            sql.append("         T1.PART_OLDCODE,\n");
            sql.append("         T1.PART_CNAME,\n");
            sql.append("         T1.UNIT,\n");
            sql.append("         (SELECT C.CODE_DESC\n");
            sql.append("            FROM TT_PART_DEFINE D, TC_CODE C\n");
//            sql.append("           WHERE D.PART_TYPE = C.CODE_ID\n");
            sql.append("           WHERE D.PRODUCE_STATE = C.CODE_ID\n");//20170821 update
            sql.append("             AND T1.PART_ID = D.PART_ID) PART_TYPE,\n");
            sql.append("         T1.RETURN_QTY,\n");
            sql.append("         SP.SALE_PRICE3,\n");
            sql.append("         SP.SALE_PRICE3 * T1.RETURN_QTY RETURN_AMOUNT,\n");
            sql.append("         T2.NAME,\n");
            sql.append("         T3.WH_NAME STOCK_OUT_NAME,\n");
            sql.append("         T1.STOCK_IN,\n");
            sql.append("         T5.WH_NAME STOCK_IN_NAME,\n");
            sql.append("         T4.STATE,\n");
            sql.append("         T1.CREATE_DATE\n");
            sql.append("    FROM TT_PART_RETURN_RECORD    T1,\n");
            sql.append("         TC_USER                  T2,\n");
            sql.append("         TT_PART_WAREHOUSE_DEFINE T3,\n");
            sql.append("         TT_PART_OEM_RETURN_MAIN  T4,\n");
            sql.append("         TT_PART_WAREHOUSE_DEFINE T5,\n");
            sql.append("         TT_PART_SALES_PRICE      SP\n");
            sql.append("   WHERE T1.RETURN_ID = T4.RETURN_ID\n");
            sql.append("     AND t1.PART_ID = SP.PART_ID\n");
            sql.append("     AND T1.CREATE_BY = T2.USER_ID\n");
            sql.append("     AND T1.STOCK_OUT = T3.WH_ID(+)\n");
            sql.append("     AND T1.STOCK_IN = T5.WH_ID(+)\n");
            ;
            sql.append(" AND T1.RETURN_TYPE=").append(Constant.PART_RETURN_TYPE_02);
            if (logonUser.getOrgId() != null) {
                sql.append(" AND T4.ORG_ID=").append(logonUser.getOrgId());
            }
            if (!"".equals(po.getReturnCode())) {
                sql.append(" AND T1.RETURN_CODE LIKE '%")
                        .append(po.getReturnCode()).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(po.getPartCode())) {
                sql.append(" AND T1.PART_CODE LIKE '%").append(po.getPartCode()).append("%'\n");
            }
            if (!"".equals(po.getPartOldcode())) {
                sql.append(" AND T1.PART_OLDCODE LIKE '%").append(po.getPartOldcode()).append("%'\n");
            }
            if (!"".equals(po.getPartCname())) {
                sql.append(" AND T1.PART_CNAME LIKE '%").append(po.getPartCname()).append("%'\n");
            }
            sql.append(" AND T1.STATUS=1");
            sql.append(" ORDER BY T1.CREATE_DATE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;

    }

    public List<Map<String, Object>> queryPartReturn(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnCode = CommonUtils.checkNull(request.getParamValue("RETURN_CODE"));//退货单号
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//退货开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//退货结束时间
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//件号
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String state = CommonUtils.checkNull(request.getParamValue("state"));//状态

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT A.*, B.WH_NAME STOCK_IN_NAME\n");
            sql.append("  FROM (SELECT T1.RETURN_CODE,\n");
            sql.append("               T4.ORG_NAME     DEALER_NAME,\n");
            sql.append("               T1.RETURN_TYPE,\n");
            sql.append("               T1.PART_CODE,\n");
            sql.append("               T1.PART_OLDCODE,\n");
            sql.append("               T1.PART_CNAME,\n");
            sql.append("               T1.RETURN_QTY,\n");
            sql.append("               T1.UNIT,\n");
            sql.append("(SELECT C.CODE_DESC\n");
            sql.append("               FROM TT_PART_DEFINE D, TC_CODE C\n");
            sql.append("              WHERE D.PART_TYPE = C.CODE_ID\n");
            sql.append("                AND T1.PART_ID = D.PART_ID) PART_TYPE,\n");
            sql.append("\t\t\t\t\t\t\t sp.sale_price3,\n");
            sql.append("\t\t\t\t\t\t\t sp.sale_price3*t1.return_qty return_amount,\n");
            sql.append("               T2.NAME,\n");
            sql.append("               T3.WH_NAME      STOCK_OUT_NAME,\n");
            sql.append("               T1.STOCK_IN,\n");
            sql.append("               T1.CREATE_DATE,\n");
            sql.append("               T4.STATE\n");
            sql.append("          FROM TT_PART_RETURN_RECORD    T1,\n");
            sql.append("               TT_PART_OEM_RETURN_MAIN  T4,\n");
            sql.append("               TC_USER                  T2,\n");
            sql.append("               TT_PART_WAREHOUSE_DEFINE T3,\n");
            sql.append("\t\t\t\t\t\t\t tt_part_sales_price      sp\n");
            sql.append("         WHERE T1.RETURN_ID = T4.RETURN_ID\n");
            sql.append("\t\t\t\t AND sp.part_id=t1.part_id\n");
            sql.append("           AND T1.CREATE_BY = T2.USER_ID\n");
            sql.append("           AND T1.STOCK_OUT = T3.WH_ID(+)\n");

            sql.append(" AND T1.RETURN_TYPE=").append(Constant.PART_RETURN_TYPE_02);
            if (logonUser.getOrgId() != null) {
                sql.append(" AND T4.ORG_ID=").append(logonUser.getOrgId());
            }
            if (!"".equals(returnCode)) {
                sql.append(" AND T1.RETURN_CODE LIKE '%")
                        .append(returnCode).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND T1.PART_CODE LIKE '%").append(partCode).append("%'\n");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND T1.PART_OLDCODE LIKE '%").append(partOldCode).append("%'\n");
            }
            if (!"".equals(partCname)) {
                sql.append(" AND T1.PART_CNAME LIKE '%").append(partCname).append("%'\n");
            }
            if (!"".equals(state) && null != state) {
                sql.append(" AND T4.STATE=" + state);
            }
            sql.append(" AND T1.STATUS=1");
            sql.append(" ORDER BY T1.CREATE_DATE ASC) A,TT_PART_WAREHOUSE_DEFINE B WHERE A.STOCK_IN=B.WH_ID(+)");
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<Map<String, Object>> queryPartDlrReturn(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnCode = CommonUtils.checkNull(request.getParamValue("RETURN_CODE"));//退货单号
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//退货单位
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//退货开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//退货结束时间
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//件号
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称

            StringBuffer sql = new StringBuffer("");
            String dealerId = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";

            }

            sql.append("SELECT A.*, B.WH_NAME STOCK_IN_NAME\n");
            sql.append("  FROM (SELECT T4.RETURN_CODE,\n");
            sql.append("               T4.DEALER_NAME,\n");
            sql.append("               T5.PART_CODE,\n");
            sql.append("               T5.PART_OLDCODE,\n");
            sql.append("               T5.PART_CNAME,\n");
            sql.append("               T5.UNIT,\n");
            sql.append("               CASE\n");
            sql.append("                 WHEN T4.SO_CODE IS NULL THEN\n");
            sql.append("                  '无单退货'\n");
            sql.append("                 WHEN T4.SO_CODE IS NOT NULL THEN\n");
            sql.append("                  '有单退货'\n");
            sql.append("               END RETURN_TYPE,\n");
            sql.append("               (SELECT C.CODE_DESC\n");
            sql.append("                  FROM TT_PART_DEFINE D, TC_CODE C\n");
            sql.append("                 WHERE D.PART_TYPE = C.CODE_ID\n");
            sql.append("                   AND T5.PART_ID = D.PART_ID) PART_TYPE,\n");
            sql.append("               T5.RETURN_QTY,\n");
            sql.append("               T5.BUY_PRICE,\n");
            sql.append("               T5.BUY_AMOUNT,\n");
            sql.append("               CASE\n");
            sql.append("                 WHEN T4.SELLER_ID = 2010010100070674 THEN\n");
            sql.append("                  SP.SALE_PRICE3\n");
            sql.append("                 WHEN T4.SELLER_ID != 2010010100070674 THEN\n");
            sql.append("                  0\n");
            sql.append("               END SALE_PRICE3,\n");
            sql.append("               CASE\n");
            sql.append("                 WHEN T4.SELLER_ID = 2010010100070674 THEN\n");
            sql.append("                  TO_CHAR(SP.SALE_PRICE3 * T5.RETURN_QTY, 'fm999,999,999.00')\n");
            sql.append("                 WHEN T4.SELLER_ID != 2010010100070674 THEN\n");
            sql.append("                  '0'\n");
            sql.append("               END RETURN_AMOUNT,\n");
            sql.append("               T2.NAME,\n");
            sql.append("               T3.WH_NAME STOCK_OUT_NAME,\n");
            sql.append("               T4.STOCK_IN,\n");
            sql.append("               T4.Return_Date CREATE_DATE\n");
            sql.append("          FROM TT_PART_DLR_RETURN_MAIN  T4,\n");
            sql.append("               TT_PART_DLR_RETURN_DTL   T5,\n");
            sql.append("               TC_USER                  T2,\n");
            sql.append("               TT_PART_WAREHOUSE_DEFINE T3,\n");
            sql.append("               TT_PART_SALES_PRICE      SP\n");
            sql.append("         WHERE T4.RETURN_ID = T5.RETURN_ID\n");
            sql.append("           AND T5.PART_ID = SP.PART_ID\n");
            sql.append("           AND T4.CREATE_BY = T2.USER_ID\n");
            sql.append("           AND T4.STOCK_OUT = T3.WH_ID\n");
            sql.append("           AND T4.STATUS = 1\n");
            sql.append("  and T4.SELLER_ID=").append(dealerId);

            if (!"".equals(dealerName)) {
                sql.append(" AND T4.DEALER_NAME LIKE '%")
                        .append(dealerName).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T4.CREATE_DATE)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T4.CREATE_DATE)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(returnCode)) {
                sql.append(" AND T4.RETURN_CODE LIKE '%")
                        .append(returnCode).append("%'\n");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND T4.DEALER_NAME LIKE '%")
                        .append(dealerName).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T4.CREATE_DATE)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T4.CREATE_DATE)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND T5.PART_CODE LIKE '%").append(partCode).append("%'\n");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND T5.PART_OLDCODE LIKE '%").append(partOldCode).append("%'\n");
            }
            if (!"".equals(partCname)) {
                sql.append(" AND T5.PART_CNAME LIKE '%").append(partCname).append("%'\n");
            }
            sql.append(" AND T4.STATE <> 92361007");
            sql.append(" ORDER BY T4.CREATE_DATE ASC) A,TT_PART_WAREHOUSE_DEFINE B WHERE A.STOCK_IN=B.WH_ID(+)");
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public Map getPartOemReturnMainInfo(String returnId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T1.RETURN_ID, \n");
            sql.append("        T1.RETURN_CODE, \n");
            sql.append("        T1.ORG_NAME, \n");
            sql.append("        TRUNC(T1.CREATE_DATE) CREATE_DATE, \n");
            sql.append(" NVL(T1.CHECK_CODE, '无单退货') AS CHECK_CODE, ");
            sql.append("        DECODE(T1.RETURN_TYPE, \n");
            sql.append(Constant.PART_OEM_RETURN_TYPE_01);
            sql.append("               ,'有单退货', \n");
            sql.append(Constant.PART_OEM_RETURN_TYPE_02);
            sql.append("               ,'无单退货') RETURN_TYPE, \n");
            sql.append("        T1.REMARK, \n");
            sql.append("        T1.REMARK1, \n");
            sql.append("        T2.NAME \n");
            sql.append("   FROM TT_PART_OEM_RETURN_MAIN T1, TC_USER T2 \n");
            sql.append("  WHERE T1.CREATE_BY = T2.USER_ID \n");
            sql.append("    AND T1.RETURN_ID = ").append(returnId);

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }


    public List<Map<String, Object>> queryPartOemReturn(String returnId) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.UNIT, \n");
            sql.append("        T.VENDER_NAME, \n");
            sql.append("        T.IN_QTY, \n");
            sql.append("        T.ITEM_QTY, \n");
            sql.append("        T.NORMAL_QTY, \n");
            sql.append("        T.APPLY_QTY, \n");
            sql.append("        T.CHECK_QTY, \n");
            sql.append("        T.BUY_PRICE, \n");
            sql.append("        T.BUY_AMOUNT, \n");
            sql.append("        T.RETURN_QTY, \n");
            sql.append("        T1.WH_NAME, \n");
            sql.append("        T2.LOC_NAME, \n");
            sql.append("        T.REMARK \n");
            sql.append("   FROM TT_PART_OEM_RETURN_DTL T,TT_PART_WAREHOUSE_DEFINE T1,TT_PART_LOACTION_DEFINE T2 \n");
            sql.append("    WHERE T.STOCK_OUT=T1.WH_ID AND T.PART_ID=T2.PART_ID AND T.STOCK_OUT=T2.WH_ID\n");
            sql.append(" AND T.RETURN_ID=").append(returnId);

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

}
