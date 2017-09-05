package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.po.TtPartBoMainPO;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartBoDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartBoDao.class);
    private static final PartBoDao dao = new PartBoDao();

    private PartBoDao() {

    }

    public static final PartBoDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> queryPartBoList(TtPartBoMainPO po, String partOldCode,
                                                           String startDate, String endDate, String dealerName, String sellerName,
                                                           String orderType, String dealerCode, String salerId, String startDate1, String endDate1, AclUserBean logonUser, String boType,
                                                           Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T1.BO_ID, \n");
            sql.append("        T1.BO_CODE, \n");
            sql.append("        T1.ORDER_ID, \n");
            sql.append("        T2.ORDER_CODE, \n");
            sql.append("        T2.DEALER_CODE, \n");
            sql.append("        T2.DEALER_NAME, \n");
            sql.append("        T2.SELLER_NAME, \n");
            sql.append("        T2.ORDER_TYPE, \n");
            sql.append("        T1.CREATE_DATE, \n");
            sql.append("        (SELECT SUM(T3.BUY_QTY) \n");
            sql.append("           FROM TT_PART_BO_DTL T3 \n");
            sql.append("          WHERE T3.BO_ID = T1.BO_ID) BUY_QTY, \n");
            sql.append("        TO_CHAR(T2.ORDER_AMOUNT, 'FM999,999,990.00') ORDER_AMOUNT, \n");
            sql.append("        (SELECT SUM(T4.BO_QTY) \n");
            sql.append("           FROM TT_PART_BO_DTL T4 \n");
            sql.append("          WHERE T4.BO_ID = T1.BO_ID) BO_QTY, \n");
            sql.append("        (SELECT TO_CHAR(SUM(T4.BO_QTY * T4.BUY_PRICE), 'FM999,999,990.00') \n");
            sql.append("           FROM TT_PART_BO_DTL T4 \n");
            sql.append("          WHERE T4.BO_ID = T1.BO_ID) BO_AMOUNT, \n");
            sql.append("        (SELECT SUM(T4.SALES_QTY) \n");
            sql.append("           FROM TT_PART_BO_DTL T4 \n");
            sql.append("          WHERE T4.BO_ID = T1.BO_ID) SALES_QTY, \n");
            sql.append("        (SELECT SUM(T4.TOSAL_QTY) \n");
            sql.append("           FROM TT_PART_BO_DTL T4 \n");
            sql.append("          WHERE T4.BO_ID = T1.BO_ID) TOSAL_QTY, \n");
            sql.append("        (SELECT SUM(T4.CLOSE_QTY) \n");
            sql.append("           FROM TT_PART_BO_DTL T4 \n");
            sql.append("          WHERE T4.BO_ID = T1.BO_ID) CLOSE_QTY, \n");
            sql.append("        (SELECT SUM(T4.BO_ODDQTY) \n");
            sql.append("           FROM TT_PART_BO_DTL T4 \n");
            sql.append("          WHERE T4.BO_ID = T1.BO_ID) BO_ODDQTY, \n");
            sql.append("        0 BO_COUNT, \n");
            sql.append("        T1.REMARK, \n");
            sql.append("        T1.STATE \n");
            sql.append("   FROM TT_PART_DLR_ORDER_MAIN T2,TT_PART_BO_MAIN T1 \n");
            sql.append("  WHERE T1.ORDER_ID = T2.ORDER_ID \n");
            //sql.append("    AND T1.BO_TYPE = 1 \n");
            sql.append(" AND (T2.SELLER_ID =");
            if (logonUser.getDealerId() == null) {
                PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
                List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
                if (null != beanList || beanList.size() >= 0) {
                    sql.append(beanList.get(0).getOrgId() + "");
                    sql.append(")");
                }
            } else {
                sql.append(logonUser.getDealerId());
                sql.append("  OR T2.DEALER_ID=");
                sql.append(logonUser.getDealerId());
                sql.append(")");
            }

            if (po.getOrderCode() != null && !"".equals(po.getOrderCode().trim())) {
                sql.append(" AND T2.ORDER_CODE LIKE '%")
                        .append(po.getOrderCode().trim()).append("%'\n");
            }
            if (!"".equals(partOldCode.trim())) {
                sql.append("    AND EXISTS (SELECT 1\n" +
                        "          FROM TT_PART_BO_DTL M\n" +
                        "         WHERE T1.BO_ID=M.BO_ID\n" +
                        "           AND UPPER(M.PART_OLDCODE) LIKE '%")
                        .append(partOldCode.trim().toUpperCase()).append("%')\n");
            }
            if (po.getBoCode() != null && !"".equals(po.getBoCode().trim())) {
                sql.append(" AND T1.BO_CODE LIKE '%")
                        .append(po.getBoCode().trim()).append("%'\n");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND T2.DEALER_NAME LIKE '%")
                        .append(dealerName.trim()).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(startDate1)) {
                sql.append(" AND TO_DATE(T2.CREATE_DATE)>=").append("TO_DATE('").append(startDate1).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate1)) {
                sql.append(" AND TO_DATE(T2.CREATE_DATE)<=").append("TO_DATE('").append(endDate1).append("','yyyy-MM-dd')");
            }
            if (!"".equals(sellerName)) {
                sql.append(" AND T2.SELLER_NAME LIKE '%")
                        .append(sellerName.trim()).append("%'\n");
            }
            if (po.getState() != null && po.getState() != 1) {
                sql.append(" AND T1.STATE=").append(po.getState());
            }
            if (!"".equals(orderType)) {
                sql.append(" AND T2.ORDER_TYPE=")
                        .append(orderType);
            }
            if (!"".equals(dealerCode)) {
                sql.append(" AND T2.DEALER_CODE LIKE '%")
                        .append(dealerCode.trim()).append("%'\n");
            }
            sql.append("    AND T1.STATUS = 1 \n");
            sql.append("    AND T2.STATUS = 1 \n");
            if (logonUser.getDealerId() == null) {
                /*if("".equals(salerId)){
                     sql.append("  AND EXISTS (SELECT 1\n" +
	  		                "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
	  		                "         WHERE T2.DEALER_ID = B.DEALER_ID\n" +
	  		                "           AND B.USER_ID ="+logonUser.getUserId() +")");
	          	}else{
	          		sql.append("  AND EXISTS (SELECT 1\n" +
	  		                "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
	  		                "         WHERE T2.DEALER_ID = B.DEALER_ID\n" +
	  		                "           AND B.USER_ID ="+salerId+")");
	          	}*/
                if (!"".equals(salerId)) {
                    sql.append("  AND EXISTS (SELECT 1\n" +
                            "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
                            "         WHERE T2.DEALER_ID = B.DEALER_ID\n" +
                            "           AND B.USER_ID =" + salerId + ")");
                }
            }

            if (!"".equals(boType) && boType != null) {
                sql.append(" AND  T1.BO_TYPE =").append(boType);

            }
            sql.append("    AND EXISTS(SELECT 1 FROM  TT_PART_SALES_RELATION R WHERE (MONTHS_BETWEEN(TRUNC(SYSDATE, 'mm'), \n");
            sql.append("                        TO_DATE(TO_CHAR(T1.CREATE_DATE, 'yyyy-mm'), 'yyyy-mm')) <= \n");
            sql.append("        R.BO_DAYS)) ");

            //增加大区人员限制 1128
            if (logonUser.getCompanyId().toString().equals(Constant.OEM_ACTIVITIES) && !logonUser.getOrgId().toString().equals(Constant.OEM_ACTIVITIES)
                    && logonUser.getParentOrgId().toString().equals(Constant.OEM_ACTIVITIES)) {
                sql.append(CommonUtils.getOrgDealerLimitSqlByPose("T2", logonUser));
            }
            sql.append("  ORDER BY  T1.CREATE_DATE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartBo(RequestWrapper request, AclUserBean logonUser) throws Exception {
        List<Map<String, Object>> list;
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String boCode = CommonUtils.checkNull(request.getParamValue("BO_CODE"));//BO单号
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
            String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
            String startDate1 = CommonUtils.checkNull(request.getParamValue("startDate1"));//制单开始时间
            String endDate1 = CommonUtils.checkNull(request.getParamValue("endDate1"));//制单结束时间
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//BO单状态
            String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//订单单位编码
            String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));//销售人员

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT T1.BO_ID,\n");
            sql.append("       T1.BO_CODE,\n");
            sql.append("       T1.ORDER_ID,\n");
            sql.append("       T2.ORDER_CODE,\n");
            sql.append("       T2.DEALER_CODE,\n");
            sql.append("       T2.DEALER_NAME,\n");
            sql.append("       T2.SELLER_NAME,\n");
            sql.append("       (SELECT C.CODE_DESC FROM TC_CODE C WHERE C.CODE_ID = T2.ORDER_TYPE) ORDER_TYPE,\n");
            sql.append("       T1.CREATE_DATE,\n");
            sql.append("       (SELECT SUM(T3.BUY_QTY)\n");
            sql.append("          FROM TT_PART_DLR_ORDER_DTL T3\n");
            sql.append("         WHERE T3.ORDER_ID = T2.ORDER_ID) BUY_QTY,\n");
            sql.append("       (SELECT SUM(T4.BO_QTY)\n");
            sql.append("          FROM TT_PART_BO_DTL T4\n");
            sql.append("         WHERE T4.BO_ID = T1.BO_ID) BO_QTY,\n");
            sql.append("       (SELECT SUM(T4.SALES_QTY)\n");
            sql.append("          FROM TT_PART_BO_DTL T4\n");
            sql.append("         WHERE T4.BO_ID = T1.BO_ID) SALES_QTY,\n");
            sql.append("       (SELECT SUM(T4.TOSAL_QTY)\n");
            sql.append("          FROM TT_PART_BO_DTL T4\n");
            sql.append("         WHERE T4.BO_ID = T1.BO_ID) TOSAL_QTY,\n");
            sql.append("       (SELECT SUM(T4.CLOSE_QTY)\n");
            sql.append("          FROM TT_PART_BO_DTL T4\n");
            sql.append("         WHERE T4.BO_ID = T1.BO_ID) CLOSE_QTY,\n");
            sql.append("       (SELECT SUM(T4.BO_ODDQTY)\n");
            sql.append("          FROM TT_PART_BO_DTL T4\n");
            sql.append("         WHERE T4.BO_ID = T1.BO_ID) BO_ODDQTY,\n");
            sql.append("       (SELECT C.CODE_DESC FROM TC_CODE C WHERE C.CODE_ID = T1.STATE) STATE\n");
            sql.append("  FROM TT_PART_BO_MAIN T1, TT_PART_DLR_ORDER_MAIN T2\n");
            sql.append(" WHERE T1.ORDER_ID = T2.ORDER_ID\n");
            //sql.append("   AND T1.BO_TYPE = 1\n");
            sql.append(" AND (T2.SELLER_ID =");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
                sql.append(")");
            } else {
                sql.append(logonUser.getDealerId());
                sql.append("  OR T2.DEALER_ID=");
                sql.append(logonUser.getDealerId());
                sql.append(")");
            }

            if (orderCode != null && !"".equals(orderCode.trim())) {
                sql.append(" AND T2.ORDER_CODE LIKE '%")
                        .append(orderCode.trim()).append("%'\n");
            }
            if (!"".equals(partOldCode.trim())) {
                sql.append("    AND EXISTS (SELECT 1\n" +
                        "          FROM TT_PART_BO_DTL M\n" +
                        "         WHERE T1.BO_ID=M.BO_ID\n" +
                        "           AND UPPER(M.PART_OLDCODE) LIKE '%")
                        .append(partOldCode.trim().toUpperCase()).append("%')\n");
            }
            if (boCode != null && !"".equals(boCode.trim())) {
                sql.append(" AND T1.BO_CODE LIKE '%")
                        .append(boCode.trim()).append("%'\n");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND T2.DEALER_NAME LIKE '%")
                        .append(dealerName.trim()).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(startDate1)) {
                sql.append(" AND TO_DATE(T2.CREATE_DATE)>=").append("TO_DATE('").append(startDate1).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate1)) {
                sql.append(" AND TO_DATE(T2.CREATE_DATE)<=").append("TO_DATE('").append(endDate1).append("','yyyy-MM-dd')");
            }
            if (!"".equals(sellerName)) {
                sql.append(" AND T2.SELLER_NAME LIKE '%")
                        .append(sellerName.trim()).append("%'\n");
            }
            if (state != null && !"".equals(state)) {
                sql.append(" AND T1.STATE=").append(state);
            }
            if (!"".equals(orderType)) {
                sql.append(" AND T2.ORDER_TYPE=")
                        .append(orderType);
            }
            if (!"".equals(dealerCode)) {
                sql.append(" AND T2.DEALER_CODE LIKE '%")
                        .append(dealerCode.trim()).append("%'\n");
            }
            sql.append("    AND T1.STATUS = 1 \n");
            sql.append("    AND T2.STATUS = 1 \n");
            if (logonUser.getDealerId() == null) {
                if (!"".equals(salerId)) {
                    sql.append("  AND EXISTS (SELECT 1\n" +
                            "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
                            "         WHERE T2.DEALER_ID = B.DEALER_ID\n" +
                            "           AND B.USER_ID =" + salerId + ")");
                }
            }

            sql.append("    AND EXISTS(SELECT 1 FROM  TT_PART_SALES_RELATION R WHERE (MONTHS_BETWEEN(TRUNC(SYSDATE, 'mm'), \n");
            sql.append("                        TO_DATE(TO_CHAR(T1.CREATE_DATE, 'yyyy-mm'), 'yyyy-mm')) <= \n");
            sql.append("        R.BO_DAYS)) ");

            sql.append("  ORDER BY T1.CREATE_DATE DESC");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;
    }

    public Map getPartBoMainInfo(String boId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T1.BO_ID,T1.BO_CODE,T1.ORDER_CODE,T3.NAME,to_char(T1.CREATE_DATE,'yyyy-MM-dd') CREATE_DATE,T2.SELLER_NAME,T2.DEALER_NAME FROM TT_PART_BO_MAIN T1,TT_PART_DLR_ORDER_MAIN T2,TC_USER T3");
            sql.append(" WHERE T1.ORDER_ID=T2.ORDER_ID AND T1.CREATE_BY=T3.USER_ID AND T1.BO_ID=");
            sql.append(CommonUtils.parseLong(boId));
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

    }

    public PageResult<Map<String, Object>> queryPartBoDetailList(String boId, String orderId,
                                                                 String partOldCode, String partCname, String partCode, String flag, Integer curPage, Integer pageSize, String orgId, String viewFlag) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT T.BOLINE_ID,\n");
            sql.append("       T.BO_ID,\n");
            sql.append("       T.PART_CODE,\n");
            sql.append("       T.PART_OLDCODE,\n");
            sql.append("       T.PART_CNAME,\n");
            sql.append("       T.UNIT,\n");
            sql.append("       TO_CHAR(T.BUY_PRICE, 'fm999,999,990.00') BUY_PRICE,\n");
            sql.append("       T.BUY_QTY,\n");
            sql.append("       T.SALES_QTY,\n");
            sql.append("       T.BO_QTY,\n");
            sql.append("       T.TOSAL_QTY,\n");
            sql.append("       T.CLOSE_QTY,\n");
            sql.append("       T.BO_ODDQTY,\n");
            sql.append("       NVL((SELECT SUM(V.NORMAL_QTY)\n");
            sql.append("             FROM VW_PART_STOCK V\n");
            sql.append("            WHERE V.ORG_ID = T2.SELLER_ID\n");
            sql.append("              AND V.WH_ID = T2.WH_ID\n");
            sql.append("              AND V.PART_ID = T.PART_ID\n");
            sql.append("              AND V.STATE = 10011001\n");
            sql.append("\t\t\t\t\t\t\tAND v.ORG_ID =" + orgId + "\n");
            sql.append("              AND V.STATUS = 1),\n");
            sql.append("           0) NORMAL_QTY,\n");
            sql.append("       DECODE(T.STATUS, 1, '未处理完成', 0, '已处理完成') STATUS,\n");
            sql.append("       T2.CREATE_DATE,\n");
            sql.append("       T.REMARK\n");
            sql.append("  FROM TT_PART_BO_DTL T, TT_PART_BO_MAIN T1, TT_PART_DLR_ORDER_MAIN T2\n");
            sql.append(" WHERE T.BO_ID = T1.BO_ID\n");
            sql.append("   AND T1.ORDER_ID = T2.ORDER_ID");
            sql.append("   AND T.BO_ID = " + boId + "\n");
//            sql.append("   AND T.STATUS=1");

            if (!"".equals(partOldCode)) {
                sql.append(" AND upper(T.PART_OLDCODE) LIKE upper('%").append(partOldCode).append("%')");
            }
            if (!"".equals(partCname)) {
                sql.append(" AND T.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND upper(T.partCode) LIKE upper('%").append(partCname).append("%')");
            }
            if ("1".equals(flag)) {
                sql.append(" AND T.STATUS=1");
            }
            if (!"".equals(viewFlag) && null != viewFlag) {
                if ("0".equals(viewFlag)) {
                    sql.append(" AND T.STATUS=1");
                }
            }

            sql.append("GROUP BY T.BOLINE_ID,\n");
            sql.append("         T.BO_ID,\n");
            sql.append("         T.PART_CODE,\n");
            sql.append("         T.PART_OLDCODE,\n");
            sql.append("         T.PART_CNAME,\n");
            sql.append("         T.UNIT,\n");
            sql.append("         T.BUY_PRICE,\n");
            sql.append("         T.BUY_QTY,\n");
            sql.append("         T.SALES_QTY,\n");
            sql.append("         T.BO_QTY,\n");
            sql.append("         T.TOSAL_QTY,\n");
            sql.append("         T.CLOSE_QTY,\n");
            sql.append("         T.BO_ODDQTY,\n");
            sql.append("         T.STATUS,\n");
            sql.append("         T2.CREATE_DATE,\n");
            sql.append("         T2.SELLER_ID,\n");
            sql.append("         T2.WH_ID,\n");
            sql.append("         T.PART_ID,\n");
            sql.append("         T.REMARK\n");
            sql.append("ORDER BY NORMAL_QTY DESC, T.BOLINE_ID\n");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;

    }

    public void updateBoDtlStatus(Long boId) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE TT_PART_BO_DTL A SET A.CLOSE_QTY=A.BO_ODDQTY,A.BO_ODDQTY=0,A.STATUS=0,A.DISABLE_DATE=SYSDATE,A.DISABLE_BY=").append(logonUser.getUserId());
            sql.append(" WHERE A.BO_ID=").append(boId);
            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isCloseAll(String boId) throws Exception {
        boolean flag = false;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT COUNT(1) MYCOUNT FROM  TT_PART_BO_DTL T WHERE T.BO_ID=").append(boId);
            sql.append(" AND T.STATUS=1");
            Map map = pageQueryMap(sql.toString(), null, getFunName());
            int count = ((BigDecimal) map.get("MYCOUNT")).intValue();
            if (count == 0) {
                flag = true;
            }
        } catch (Exception e) {
            throw e;
        }
        return flag;
    }

    public PageResult<Map<String, Object>> queryPartBoList4Handle(
            TtPartBoMainPO po, String partOldCode, String startDate, String endDate,
            String dealerName, String sellerName, String orderType, String dealerCode, String IF_TYPE,
            String salerId, AclUserBean logonUser, Integer curPage, Integer pageSize, String dealerId) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T1.BO_ID, \n");
            sql.append("        T1.BO_CODE, \n");
            sql.append("        T1.ORDER_ID, \n");
            sql.append("        T2.ORDER_CODE, \n");
            sql.append("        T2.DEALER_ID, \n");
            sql.append("        T2.DEALER_CODE, \n");
            sql.append("        T2.DEALER_NAME, \n");
            sql.append("        T2.RCV_ORGID, \n");
            sql.append("        T2.SELLER_NAME, \n");
            sql.append("        T2.ORDER_TYPE, \n");
            sql.append(IF_TYPE).append(" IF_TYPE,\n");
            sql.append("        T1.CREATE_DATE, \n");
            sql.append("        (SELECT SUM(T4.BO_QTY) \n");
            sql.append("           FROM TT_PART_BO_DTL T4 \n");
            sql.append("          WHERE T4.BO_ID = T1.BO_ID and t4.status=1\n");
            sql.append("            ) BO_QTY, \n");
            sql.append("        (SELECT TO_CHAR(SUM(T4.BO_QTY * NVL(T4.BUY_PRICE,");
            sql.append(" PKG_PART.F_GETPRICE(T2.DEALER_ID,T4.PART_ID)");
            sql.append(")), 'FM999,999,990.00') \n");
            sql.append("           FROM TT_PART_BO_DTL T4 \n");
            sql.append("          WHERE T4.BO_ID = T1.BO_ID and t4.status=1\n");
            sql.append("            ) BO_AMOUNT, \n");
            sql.append("        (SELECT SUM(T4.TOSAL_QTY) \n");
            sql.append("           FROM TT_PART_BO_DTL T4 \n");
            sql.append("          WHERE T4.BO_ID = T1.BO_ID and t4.status=1\n");
            sql.append("            ) TOSAL_QTY, \n");
            sql.append("        (SELECT SUM(T4.CLOSE_QTY) \n");
            sql.append("           FROM TT_PART_BO_DTL T4 \n");
            sql.append("          WHERE T4.BO_ID = T1.BO_ID and t4.status=1\n");
            sql.append("            ) CLOSE_QTY, \n");
            sql.append("        (SELECT SUM(T4.BO_ODDQTY) \n");
            sql.append("           FROM TT_PART_BO_DTL T4 \n");
            sql.append("          WHERE T4.BO_ID = T1.BO_ID and t4.status=1\n");
            sql.append("            ) BO_ODDQTY, \n");
          /*  sql.append("        (SELECT NVL((SELECT SUM(V.NORMAL_QTY) \n");
            sql.append("                      FROM VW_PART_STOCK V \n");
            sql.append("                     WHERE V.ORG_ID = T2.SELLER_ID \n");
            sql.append("                       AND V.WH_ID = T2.WH_ID \n");
            sql.append("                       AND EXISTS (SELECT 1 \n");
            sql.append("                              FROM TT_PART_BO_DTL B \n");
            sql.append("                             WHERE V.PART_ID = B.PART_ID \n");
            sql.append("                               AND B.BO_ID = T1.BO_ID \n");
            sql.append("                               AND B.STATUS = 1)), \n");
            sql.append("                    0) \n");
            sql.append("           FROM DUAL) NORMAL_QTY, \n");*/
            sql.append("        T2.WH_ID, \n");
            sql.append("        T5.WH_NAME, \n");
            sql.append("        T1.REMARK, \n");
            sql.append("        T1.STATE \n");
            sql.append("   FROM TT_PART_BO_MAIN          T1, \n");
            sql.append("        TT_PART_WAREHOUSE_DEFINE T5, \n");
            sql.append("        TT_PART_SALES_RELATION   T6, \n");
            sql.append("        TT_PART_DLR_ORDER_MAIN   T2 \n");
            sql.append("  WHERE T1.ORDER_ID = T2.ORDER_ID \n");
            sql.append("    AND T2.WH_ID = T5.WH_ID(+) \n");
            sql.append("    AND T2.SELLER_ID = T6.PARENTORG_ID \n");
            sql.append("    AND T2.DEALER_ID = T6.CHILDORG_ID \n");
            sql.append("    AND T2.SELLER_ID =");

            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }

            if (po.getOrderCode() != null && !"".equals(po.getOrderCode().trim())) {
                sql.append(" AND T2.ORDER_CODE LIKE '%")
                        .append(po.getOrderCode().trim()).append("%'\n");
            }
            if (!"".equals(partOldCode.trim())) {
                sql.append("    AND EXISTS (SELECT 1\n" +
                        "          FROM TT_PART_BO_DTL M\n" +
                        "         WHERE T1.BO_ID=M.BO_ID\n" +
                        "         AND M.BO_ODDQTY>0\n" +
                        "           AND UPPER(M.PART_OLDCODE) LIKE '%")
                        .append(partOldCode.trim().toUpperCase()).append("%')\n");
            }
            if (po.getBoCode() != null && !"".equals(po.getBoCode().trim())) {
                sql.append(" AND T1.BO_CODE LIKE '%")
                        .append(po.getBoCode().trim()).append("%'\n");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND T2.DEALER_NAME LIKE '%")
                        .append(dealerName.trim()).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(sellerName)) {
                sql.append(" AND T2.SELLER_NAME LIKE '%").append(sellerName.trim()).append("%'\n");
            }
            if (po.getState() != null && po.getState() != 1) {
                sql.append(" AND T1.STATE=").append(po.getState());
            } else {
                sql.append(" AND T1.STATE<>").append(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);
            }
            if (!"".equals(orderType)) {
                sql.append(" AND T2.ORDER_TYPE=").append(orderType);
            }
            if (!"".equals(dealerCode)) {
                sql.append(" AND T2.DEALER_CODE='").append(dealerCode.trim()).append("'\n");
            }
            sql.append("    AND T1.STATUS = 1 \n");
            sql.append("    AND T2.STATUS = 1 \n");
            sql.append("    AND (MONTHS_BETWEEN(TRUNC(SYSDATE, 'mm'), \n");
            sql.append("                        TO_DATE(TO_CHAR(T1.CREATE_DATE, 'yyyy-mm'), 'yyyy-mm')) <= \n");
            sql.append("        T6.BO_DAYS) \n");
            if (logonUser.getDealerId() == null) {
                if ("".equals(salerId)) {
                   /*   sql.append("  AND EXISTS (SELECT 1\n" +
                               "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
 	  		                "         WHERE T2.DEALER_ID = B.DEALER_ID\n" +
 	  		                "           AND B.USER_ID ="+logonUser.getUserId() +")");*/
                } else {
                    sql.append("  AND EXISTS (SELECT 1\n" +
                            "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
                            "         WHERE T2.DEALER_ID = B.DEALER_ID\n" +
                            "           AND B.USER_ID =" + salerId + ")");
                }
            }

            if (IF_TYPE.equals(Constant.IF_TYPE_YES.toString())) {//如果有库存
                /* sql.append("    AND (SELECT NVL((SELECT SUM(V.NORMAL_QTY) \n");
                 sql.append("                      FROM VW_PART_STOCK V \n");
                 sql.append("                     WHERE V.ORG_ID = T2.SELLER_ID \n");
                 sql.append("                       AND V.WH_ID = T2.WH_ID \n");
                 sql.append("                       AND EXISTS (SELECT 1 \n");
                 sql.append("                              FROM TT_PART_BO_DTL B \n");
                 sql.append("                             WHERE V.PART_ID = B.PART_ID \n");
                 sql.append("                               AND B.BO_ID = T1.BO_ID \n");
                 sql.append("                               AND B.STATUS = 1)), \n");
                 sql.append("                    0) \n");
                 sql.append("           FROM DUAL) > 0 \n");*/

                sql.append("AND EXISTS (SELECT 1\n");
                sql.append("        FROM VW_PART_STOCK S\n");
                sql.append("       WHERE S.ORG_ID = ").append(dealerId);
                sql.append("         AND EXISTS (SELECT 1\n");
                sql.append("                FROM TT_PART_BO_DTL T4\n");
                sql.append("               WHERE T4.BO_ID = T1.BO_ID\n");
                sql.append("                 AND T4.STATUS = 1\n");
                sql.append("                 AND T4.BO_ODDQTY > 0\n");
                sql.append("                 AND T4.PART_ID = S.PART_ID)\n");
                //add by yaun 20131011 start
                if (!"".equals(partOldCode.trim())) {
                    sql.append("AND UPPER(S.PART_OLDCODE) LIKE '%").append(partOldCode.trim().toUpperCase()).append("%'\n");
                }
                //end
                if (dealerId.equals(Constant.OEM_ACTIVITIES + "")) {
                  sql.append("         AND S.WH_TYPE=15021001\n");//有效可售库房  //20170824 add
//                sql.append("         AND S.WH_TYPE=1\n");//有效可售库房 //20170824 update 屏蔽
                }
                sql.append("         AND S.NORMAL_QTY > 0)\n");

            } else {
                sql.append("AND (EXISTS (SELECT 1 FROM TT_PART_BO_DTL T4\n");
                sql.append("                 WHERE T4.BO_ID = T1.BO_ID\n");
                sql.append("                   AND EXISTS (SELECT 1 FROM VW_PART_STOCK S\n");
                sql.append("                         WHERE S.ORG_ID = 2010010100070674\n");
                sql.append("                           AND T4.PART_ID = S.PART_ID\n");
                //add by yaun 20131011 start
                if (!"".equals(partOldCode.trim())) {
                    sql.append("  AND UPPER(S.PART_OLDCODE) LIKE '%").append(partOldCode.trim().toUpperCase()).append("%'\n");
                }
                sql.append("                           AND S.WH_TYPE = 15021001 HAVING\n");//20170824 add
//              sql.append("                         HAVING\n");//20170821 update 
//                sql.append("                           AND S.WH_TYPE = 1 HAVING\n");//20170821 update 
                
                sql.append("                         SUM（S.NORMAL_QTY） = 0))\n");
                // add by yuan 20140108
                sql.append("\t OR NOT EXISTS\n");
                sql.append("     (SELECT 1 FROM VW_PART_STOCK S\n");
                sql.append("       WHERE S.ORG_ID =").append(dealerId);
                sql.append("         AND EXISTS (SELECT 1 FROM TT_PART_BO_DTL T4\n");
                sql.append("               WHERE T4.BO_ID = T1.BO_ID AND T4.PART_ID = S.PART_ID)\n");
                if (!"".equals(partOldCode.trim())) {
                    sql.append(" AND UPPER(S.PART_OLDCODE) LIKE '%").append(partOldCode.trim().toUpperCase()).append("%'\n");
                }
                //sql.append("         AND S.WH_TYPE IS NULL");//本意是过滤虚拟库房，当前暂时取消过滤140612
                sql.append(")");
                sql.append("\tOR EXISTS\n");
                sql.append("(SELECT 1 FROM TT_PART_BO_DTL BD\n");
                sql.append("  WHERE BD.BO_ID = T1.BO_ID\n");
                sql.append("    AND NOT EXISTS (SELECT 1 FROM VW_PART_STOCK S\n");
                sql.append("          WHERE S.PART_ID = BD.PART_ID  AND S.ORG_ID = " + dealerId + ")))");
                //end
            }
            sql.append("  ORDER BY T1.CREATE_DATE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    /**
     * @param PARTOLD_NAME
     * @param PART_CODE
     * @param PARTOLD_CODE
     * @param desc
     * @param startDate
     * @param endDate
     * @param startDate1
     * @param endDate1
     * @param logonUser
     * @param fliter
     * @param curPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageResult<Map<String, Object>> queryAllPartBoList(
            String PARTOLD_NAME, String PART_CODE, String PARTOLD_CODE, String desc, String startDate, String endDate, String startDate1, String endDate1, AclUserBean logonUser, String fliter,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps = null;
        try {
            String nowOrgId = logonUser.getDealerId() == null ? logonUser.getOrgId().toString() : logonUser.getDealerId();
            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT PD.PART_OLDCODE, PD.PART_CNAME, PD.PART_CODE, BOS.*, BOC.BO_CNT\n");
            sql.append("  FROM (SELECT T3.PART_ID,\n");
            sql.append("               SUM(T3.BO_QTY) BO_QTY,\n");
            sql.append("               NVL((SELECT SUM(VS.NORMAL_QTY)\n");
            sql.append("                     FROM TT_PART_BOOK VS\n");
            sql.append("                    WHERE T3.PART_ID = VS.PART_ID\n");
            sql.append("                      AND EXISTS\n");
            sql.append("                    (SELECT 1\n");
            sql.append("                             FROM TT_PART_WAREHOUSE_DEFINE WD\n");
            sql.append("                            WHERE WD.WH_ID = VS.WH_ID\n");
            if (nowOrgId.equals(Constant.OEM_ACTIVITIES)) {
                sql.append("                              AND WD.WH_TYPE = 1\n");
            }
            sql.append("                              AND WD.ORG_ID = " + nowOrgId + ")),\n");
            sql.append("                   0) NORMAL_QTY,\n");
            sql.append("               SUM(T3.BO_ODDQTY) BO_ODDQTY\n");
            sql.append("          FROM TT_PART_BO_MAIN        T1,\n");
            sql.append("               TT_PART_DLR_ORDER_MAIN T2,\n");
            sql.append("               TT_PART_BO_DTL         T3,\n");
            sql.append("               TT_PART_SALES_RELATION T4\n");
            sql.append("         WHERE T1.ORDER_ID = T2.ORDER_ID\n");
            sql.append("           AND T2.SELLER_ID = T4.PARENTORG_ID\n");
            sql.append("           AND T2.DEALER_ID = T4.CHILDORG_ID\n");
            sql.append("           AND T1.BO_ID = T3.BO_ID\n");
            sql.append("           AND (MONTHS_BETWEEN(TRUNC(SYSDATE, 'mm'),\n");
            sql.append("                               TO_DATE(TO_CHAR(T1.CREATE_DATE, 'yyyy-mm'),\n");
            sql.append("                                       'yyyy-mm')) <= T4.BO_DAYS)\n");
            sql.append("  AND (T2.SELLER_ID=");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId()).append(")");
            } else {
                sql.append(logonUser.getDealerId());
                sql.append("  OR T2.DEALER_ID=");
                sql.append(logonUser.getDealerId()).append(")");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND T1.CREATE_DATE>=").append("TO_DATE('").append(startDate).append("00:00:00','yyyy-MM-dd  hh24:mi:ss')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND T1.CREATE_DATE<=").append("TO_DATE('").append(endDate).append("23:59:59','yyyy-MM-dd  hh24:mi:ss')");
            }
            if (!"".equals(startDate1)) {
                sql.append(" AND T2.CREATE_DATE>=").append("TO_DATE('").append(startDate1).append("00:00:00','yyyy-MM-dd  hh24:mi:ss')");
            }

            if (!"".equals(endDate1)) {
                sql.append(" AND T2.CREATE_DATE<=").append("TO_DATE('").append(endDate1).append("23:59:59','yyyy-MM-dd  hh24:mi:ss')");
            }
            sql.append("           AND T1.STATUS = 1\n");
            sql.append("           AND T2.STATUS = 1\n");
            sql.append("         GROUP BY T3.PART_ID, T2.SELLER_ID) BOS,\n");
            sql.append(" (SELECT T3.PART_ID, COUNT(T3.BO_QTY) BO_CNT\n");
            sql.append("                  FROM TT_PART_BO_MAIN        T1,\n");
            sql.append("                       TT_PART_DLR_ORDER_MAIN T2,\n");
            sql.append("                       TT_PART_BO_DTL         T3,\n");
            sql.append("                       TT_PART_SALES_RELATION T4\n");
            sql.append("                 WHERE T1.ORDER_ID = T2.ORDER_ID\n");
            sql.append("\t\t\t\t\t\t\t\t    AND T2.DEALER_ID = T4.CHILDORG_ID\n");
            sql.append("                   AND T2.SELLER_ID = T4.PARENTORG_ID\n");
            sql.append("                   AND T1.BO_ID = T3.BO_ID\n");
            sql.append("                   AND (MONTHS_BETWEEN(TRUNC(SYSDATE, 'mm'),\n");
            sql.append("                                       TO_DATE(TO_CHAR(T1.CREATE_DATE,\n");
            sql.append("                                                       'yyyy-mm'),\n");
            sql.append("                                               'yyyy-mm')) <= T4.BO_DAYS)\n");
            sql.append("  AND (T2.SELLER_ID=");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId()).append(")");
            } else {
                sql.append(logonUser.getDealerId());
                sql.append("  OR T2.DEALER_ID=");
                sql.append(logonUser.getDealerId()).append(")");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND T1.CREATE_DATE>=").append("TO_DATE('").append(startDate).append("00:00:00','yyyy-MM-dd  hh24:mi:ss')");
            }
            if (!"".equals(endDate)) {
                sql.append(" AND T1.CREATE_DATE<=").append("TO_DATE('").append(endDate).append("23:59:59','yyyy-MM-dd  hh24:mi:ss')");
            }
            sql.append("           AND T1.STATUS = 1\n");
            sql.append("           AND T2.STATUS = 1\n");
            sql.append("         GROUP BY T3.PART_ID) BOC,\n");
            sql.append("       TT_PART_DEFINE PD\n");
            sql.append(" WHERE BOS.PART_ID = BOC.PART_ID\n");
            sql.append("   AND BOS.PART_ID = PD.PART_ID\n");
            if (!"".equals(PARTOLD_NAME)) {
                sql.append(" AND pd.PART_CNAME like '%" + PARTOLD_NAME + "%'\n");
            }
            if (!"".equals(PART_CODE)) {
                sql.append(" AND pd.PART_CODE like '%" + PART_CODE + "%'\n");
            }
            if (!"".equals(PARTOLD_CODE)) {
                sql.append(" AND pd.PART_OLDCODE like '%" + PARTOLD_CODE + "%'\n");
            }
            if (!"".equals(fliter) && null != fliter) {
                if ("1".equals(fliter)) {
                    sql.append(" AND BOS.normal_QTY<BOS.BO_QTY\n");
                    sql.append(" AND BOS.BO_ODDQTY>0\n");
                }
            }
            if ("1".equals(desc)) {
                sql.append("order by BOS.BO_QTY desc\n");
            }
            if ("2".equals(desc)) {
                sql.append("order by BOC.BO_CNT desc\n");
            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartBoAll(RequestWrapper request, AclUserBean logonUser) throws Exception {
        List<Map<String, Object>> list;
        try {
            String PARTOLD_NAME = CommonUtils.checkNull(request.getParamValue("PARTOLD_NAME"));
            String PART_CODE = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String PARTOLD_CODE = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));
            String desc = CommonUtils.checkNull(request.getParamValue("desc"));
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
            String startDate1 = CommonUtils.checkNull(request.getParamValue("startDate1"));
            String endDate1 = CommonUtils.checkNull(request.getParamValue("endDate1"));
            String fliter = CommonUtils.checkNull(request.getParamValue("fliter"));//过滤条件
            String nowOrgId = logonUser.getDealerId() == null ? logonUser.getOrgId().toString() : logonUser.getDealerId();

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT PD.PART_OLDCODE, PD.PART_CNAME, PD.PART_CODE, BOS.*, BOC.BO_CNT\n");
            sql.append("  FROM (SELECT T3.PART_ID,\n");
            sql.append("               SUM(T3.BO_QTY) BO_QTY,\n");
            sql.append("               NVL((SELECT SUM(VS.NORMAL_QTY)\n");
            sql.append("                     FROM VW_PART_STOCK VS\n");
            sql.append("                    WHERE T3.PART_ID = VS.PART_ID\n");
            sql.append("                      AND VS.ORG_ID = " + nowOrgId + "),\n");
            sql.append("                   0) NORMAL_QTY,\n");
            sql.append("               SUM(T3.BO_ODDQTY) BO_ODDQTY\n");
            sql.append("          FROM TT_PART_BO_MAIN        T1,\n");
            sql.append("               TT_PART_DLR_ORDER_MAIN T2,\n");
            sql.append("               TT_PART_BO_DTL         T3,\n");
            sql.append("               TT_PART_SALES_RELATION T4\n");
            sql.append("         WHERE T1.ORDER_ID = T2.ORDER_ID\n");
            sql.append("           AND T2.SELLER_ID = T4.PARENTORG_ID\n");
            sql.append("           AND T2.DEALER_ID = T4.CHILDORG_ID\n");
            sql.append("           AND T1.BO_ID = T3.BO_ID\n");

            sql.append("           AND (MONTHS_BETWEEN(TRUNC(SYSDATE, 'mm'),\n");
            sql.append("                               TO_DATE(TO_CHAR(T1.CREATE_DATE, 'yyyy-mm'),\n");
            sql.append("                                       'yyyy-mm')) <= T4.BO_DAYS)\n");
            sql.append("  AND (T2.SELLER_ID=");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId()).append(")");
            } else {
                sql.append(logonUser.getDealerId());
                sql.append("  OR T2.DEALER_ID=");
                sql.append(logonUser.getDealerId()).append(")");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(startDate1)) {
                sql.append(" AND TO_DATE(T2.CREATE_DATE)>=").append("TO_DATE('").append(startDate1).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate1)) {
                sql.append(" AND TO_DATE(T2.CREATE_DATE)<=").append("TO_DATE('").append(endDate1).append("','yyyy-MM-dd')");
            }
            sql.append("           AND T1.STATUS = 1\n");
            sql.append("           AND T2.STATUS = 1\n");
            sql.append("         GROUP BY T3.PART_ID, T2.SELLER_ID) BOS,\n");
            sql.append("\n");
            sql.append("       (SELECT T3.PART_ID, COUNT(T3.BO_QTY) BO_CNT\n");
            sql.append("          FROM TT_PART_BO_MAIN        T1,\n");
            sql.append("               TT_PART_DLR_ORDER_MAIN T2,\n");
            sql.append("               TT_PART_BO_DTL         T3,\n");
            sql.append("               TT_PART_SALES_RELATION T4\n");
            sql.append("         WHERE T1.ORDER_ID = T2.ORDER_ID\n");
            sql.append("           AND T2.SELLER_ID = T4.PARENTORG_ID\n");
            sql.append("           AND T2.DEALER_ID = T4.CHILDORG_ID\n");
            sql.append("           AND T1.BO_ID = T3.BO_ID\n");
            sql.append("           AND (MONTHS_BETWEEN(TRUNC(SYSDATE, 'mm'),\n");
            sql.append("                               TO_DATE(TO_CHAR(T1.CREATE_DATE, 'yyyy-mm'),\n");
            sql.append("                                       'yyyy-mm')) <= T4.BO_DAYS)\n");
            sql.append("  AND (T2.SELLER_ID=");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId()).append(")");
            } else {
                sql.append(logonUser.getDealerId());
                sql.append("  OR T2.DEALER_ID=");
                sql.append(logonUser.getDealerId()).append(")");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append("           AND T1.STATUS = 1\n");
            sql.append("           AND T2.STATUS = 1\n");
            sql.append("         GROUP BY T3.PART_ID) BOC,\n");
            sql.append("       TT_PART_DEFINE PD\n");
            sql.append(" WHERE BOS.PART_ID = BOC.PART_ID\n");
            sql.append("   AND BOS.PART_ID = PD.PART_ID\n");

            if (!"".equals(PARTOLD_NAME)) {
                sql.append(" AND pd.PART_CNAME like '%" + PARTOLD_NAME + "%'");
            }
            if (!"".equals(PART_CODE)) {
                sql.append(" AND pd.PART_CODE like '%" + PART_CODE + "%'");
            }
            if (!"".equals(PARTOLD_CODE)) {
                sql.append(" AND pd.PART_OLDCODE like '%" + PARTOLD_CODE + "%'");
            }
            if (!"".equals(fliter) && null != fliter) {
                if ("1".equals(fliter)) {
                    sql.append(" AND BOS.normal_QTY<BOS.BO_QTY\n");
                    sql.append(" AND BOS.BO_ODDQTY>0");
                }
            }

            if ("1".equals(desc)) {
                sql.append("order by BOS.BO_QTY desc");
            }
            if ("2".equals(desc)) {
                sql.append("order by BOC.BO_CNT desc");
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public PageResult<Map<String, Object>> queryPartBoDetail4SalList(
            String dealerId, String whId, String[] boIds, AclUserBean logonUser, Integer curPage,
            Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("  SELECT A.PART_ID,A.PART_CODE,A.PART_OLDCODE,A.PART_CNAME,SUM(A.BUY_QTY) BUY_QTY,SUM(A.SALES_QTY) SALES_QTY,SUM(A.BO_QTY) BO_QTY,SUM(A.BO_ODDQTY) BO_ODDQTY,NVL(A.NORMAL_QTY,0) NORMAL_QTY");
            sql.append(" FROM (SELECT T3.PART_ID, T3.PART_CODE,T3.PART_OLDCODE,T3.PART_CNAME,T3.BUY_QTY,T3.SALES_QTY,T3.BO_QTY,NVL(T3.BO_ODDQTY,T3.BO_QTY) BO_ODDQTY,");
            sql.append("   (SELECT V.NORMAL_QTY FROM VW_PART_STOCK V WHERE V.ORG_ID=T2.SELLER_ID AND V.WH_ID=T2.WH_ID AND V.PART_ID=T3.PART_ID) NORMAL_QTY");
            sql.append(" FROM TT_PART_BO_MAIN T1,TT_PART_DLR_ORDER_MAIN T2,TT_PART_BO_DTL T3 WHERE T1.ORDER_ID=T2.ORDER_ID AND T1.BO_ID=T3.BO_ID  AND T3.STATUS=1");
            sql.append(" AND T2.WH_ID=").append(whId);
            sql.append(" AND T2.DEALER_ID=").append(dealerId);
            sql.append(" AND T2.SELLER_ID=");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            //sql.append("AND T1.BO_TYPE=1");
            sql.append(" AND T1.BO_ID IN(");
            for (int i = 0; i < boIds.length; i++) {
                sql.append(boIds[i]).append(",");
            }
            sql.deleteCharAt(sql.length() - 1);

            sql.append(") ) A GROUP BY A.PART_ID, A.PART_CODE,A.PART_OLDCODE,A.PART_CNAME,A.NORMAL_QTY HAVING A.NORMAL_QTY>0");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryBoDtlDetailAll(String boIds, String rDealerId, AclUserBean logonUser, String whId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT A.PART_ID,\n");
            sql.append("       A.PART_CODE,\n");
            sql.append("       A.PART_OLDCODE,\n");
            sql.append("       A.PART_CNAME,\n");
            sql.append("       A.BUY_PRICE,\n");
            sql.append("       A.UNIT,\n");
            sql.append("       A.ALLOT_RATIO,\n");
            sql.append("       A.ALLOT_NUM,\n");
            sql.append("       A.BO_TOSALES,\n");
            sql.append("       SUM(A.BUY_QTY) BUY_QTY,\n");
            sql.append("       SUM(A.SALES_QTY) SALES_QTY,\n");
            sql.append("       SUM(A.BO_QTY) BO_QTY,\n");
            sql.append("       SUM(A.BO_ODDQTY) BO_ODDQTY,\n");
            sql.append("       NVL(A.NORMAL_QTY, 0) NORMAL_QTY,\n");
            sql.append("       ZA_CONCAT(A.REMARK) REMARK\n");
            sql.append("  FROM (SELECT T3.UNIT,\n");
            sql.append("               T5.PART_ID,\n");
            sql.append("               T5.PART_CODE,\n");
            sql.append("               T5.PART_OLDCODE,\n");
            sql.append("               T5.PART_CNAME,\n");
            sql.append("               TO_CHAR(PKG_PART.F_GETPRICE(T2.DEALER_ID, T3.PART_ID),\n");
            sql.append("                       'FM999,999,990.00') BUY_PRICE,\n");
            sql.append("               T3.BUY_QTY,\n");
            sql.append("               T3.SALES_QTY,\n");
            sql.append("               T3.BO_QTY,\n");
            sql.append("               NVL(T3.BO_ODDQTY, T3.BO_QTY) BO_ODDQTY,\n");
            sql.append("               (SELECT SUM(V.NORMAL_QTY)\n");
            sql.append("                  FROM VW_PART_STOCK V\n");
            sql.append("                 WHERE V.ORG_ID = T2.SELLER_ID\n");
            sql.append("                   AND V.WH_ID = T2.WH_ID\n");
            sql.append("                   AND V.PART_ID = T3.PART_ID\n");
            sql.append("                   AND V.STATE = 10011001\n");
            sql.append("                   AND V.STATUS = 1) NORMAL_QTY,\n");
            sql.append("               NVL(T4.ALLOT_RATIO, 0) ALLOT_RATIO,\n");
            sql.append("               NVL(T4.ALLOT_NUM, 0) ALLOT_NUM,\n");
            sql.append("               NVL(T4.BO_TOSALES, 0) BO_TOSALES,\n");
            sql.append("               T3.REMARK\n");
            sql.append("          FROM TT_PART_BO_MAIN              T1,\n");
            sql.append("               TT_PART_DLR_ORDER_MAIN       T2,\n");
            sql.append("               TT_PART_BO_DTL               T3,\n");
            sql.append("               TT_PART_RESOURCEALLOT_DEFINE T4,\n");
            sql.append("               TT_PART_DEFINE               T5\n");
            sql.append("         WHERE T1.ORDER_ID = T2.ORDER_ID\n");
            sql.append("           AND T1.BO_ID = T3.BO_ID\n");
            sql.append("           AND T3.PART_ID = T4.PART_ID(+)\n");
            sql.append("           AND T3.PART_ID = T5.PART_ID\n");
            sql.append("           AND T3.STATUS = 1\n");
            sql.append("           AND T2.WH_ID = " + whId + "\n");
            sql.append("           AND T2.DEALER_ID = " + rDealerId + "\n");
            sql.append("           AND T2.SELLER_ID = \n");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            sql.append("           AND T1.BO_ID IN (" + boIds + ")) A\n");
            sql.append(" GROUP BY A.PART_ID,\n");
            sql.append("          A.PART_CODE,\n");
            sql.append("          A.PART_OLDCODE,\n");
            sql.append("          A.PART_CNAME,\n");
            sql.append("          A.BUY_PRICE,\n");
            sql.append("          A.UNIT,\n");
            sql.append("          A.ALLOT_RATIO,\n");
            sql.append("          A.ALLOT_NUM,\n");
            sql.append("          A.BO_TOSALES,\n");
            sql.append("          A.NORMAL_QTY\n");
            sql.append("HAVING A.NORMAL_QTY > 0");
            return this.pageQuery(sql.toString(), null, this.getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> queryBoDtlDetail(String boId, String rDealerId, AclUserBean logonUser, String whId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();

            sql.append(" SELECT A.* \n");
            sql.append("   FROM (SELECT T3.UNIT, \n");
            sql.append("                T3.PART_ID, \n");
            sql.append("                T3.PART_CODE, \n");
            sql.append("                T3.PART_OLDCODE, \n");
            sql.append("                T3.PART_CNAME, \n");
            sql.append("                TO_CHAR(T3.BUY_PRICE, 'FM999,999,990.00') BUY_PRICE,\n");//取订单上单价
//            sql.append("                TO_CHAR(PKG_PART.F_GETPRICE(T2.DEALER_ID, T3.PART_ID), \n");
//            sql.append("                        'FM999,999,990.00') BUY_PRICE, \n");//取最新价格
            sql.append("                T3.BUY_QTY, \n");
            sql.append("                T3.SALES_QTY, \n");
            sql.append("                T3.BO_QTY, \n");
            sql.append("                NVL(T3.BO_ODDQTY, T3.BO_QTY) BO_ODDQTY, \n");
            sql.append("                (SELECT sum(V.NORMAL_QTY) \n");
            sql.append("                   FROM VW_PART_STOCK V \n");
            sql.append("                  WHERE V.ORG_ID = T2.SELLER_ID \n");
            sql.append("                    AND V.WH_ID = T2.WH_ID \n");
            sql.append("                    AND V.PART_ID = T3.PART_ID  AND V.STATE=10011001 AND V.STATUS=1) NORMAL_QTY, \n");
            sql.append("                NVL(T4.ALLOT_RATIO,0) ALLOT_RATIO, \n");
            sql.append("                NVL(T4.ALLOT_NUM,0) ALLOT_NUM, \n");
            sql.append("                NVL(T4.BO_TOSALES,0) BO_TOSALES, \n");
            sql.append("                T3.REMARK \n");
            sql.append("           FROM TT_PART_BO_MAIN        T1, \n");
            sql.append("                TT_PART_DLR_ORDER_MAIN T2, \n");
            sql.append("                TT_PART_BO_DTL         T3, \n");
            sql.append("                TT_PART_RESOURCEALLOT_DEFINE t4 \n");
            sql.append("          WHERE T1.ORDER_ID = T2.ORDER_ID \n");
            sql.append("            AND T1.BO_ID = T3.BO_ID \n");
            sql.append("            AND T3.PART_ID=T4.PART_ID(+) \n");
            sql.append("            AND T3.STATUS = 1 \n");
            sql.append("            AND T2.WH_ID = " + whId + " \n");
            sql.append("            AND T2.DEALER_ID = " + rDealerId + " \n");
            sql.append("            AND T2.SELLER_ID = ");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            sql.append("\n");
            //sql.append("            AND T1.BO_TYPE = 1 \n");
            sql.append("            AND T1.BO_ID = " + boId + ") A \n");
            sql.append("  WHERE A.NORMAL_QTY > 0 \n");

            return this.pageQuery(sql.toString(), null, this.getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<String, Object> queryNameByBoId(String boId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT T2.DEALER_NAME,T2.SELLER_NAME FROM TT_PART_BO_MAIN T1,TT_PART_DLR_ORDER_MAIN T2 WHERE T1.ORDER_ID=T2.ORDER_ID");
            sql.append(" AND T1.BO_ID=").append(boId);
            return this.pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<String, Object> queryPartStockInfo(String whId, String partId,
                                                  String orgId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,T.UNIT,T.IS_DIRECT,T.IS_PLAN,T.IS_LACK,T.IS_REPLACED,SUM(V.NORMAL_QTY) NORMAL_QTY,T.IS_GIFT,");
            sql.append("NVL(S.ALLOT_RATIO, 0) ALLOT_RATIO,NVL(S.ALLOT_NUM, 0) ALLOT_NUM");
            sql.append(" FROM TT_PART_DEFINE T,VW_PART_STOCK V,TT_PART_RESOURCEALLOT_DEFINE S WHERE T.PART_ID=V.PART_ID AND  V.PART_ID = S.PART_ID(+) AND V.ORG_ID=").append(orgId);
            sql.append(" AND V.WH_ID=").append(whId);
            sql.append("  AND V.PART_ID=").append(partId);
            sql.append(" GROUP BY T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,T.UNIT,T.IS_DIRECT,T.IS_PLAN,T.IS_LACK,T.IS_REPLACED,T.IS_GIFT,S.ALLOT_RATIO,S.ALLOT_NUM");
            return this.pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<String, Object> queryDtlById(String boId, String partId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT T.BOLINE_ID,T.PART_CNAME,T.BO_QTY,T.BO_ODDQTY,T.TOSAL_QTY FROM TT_PART_BO_DTL T WHERE T.BO_ID=");
            sql.append(boId);
            sql.append(" AND T.STATUS=1  AND T.PART_ID=").append(partId);
            return this.pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public List<DynaBean> queryOrderCode(Long partId, String boIds) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT T3.ORDER_CODE \n");
            sql.append("   FROM TT_PART_BO_MAIN T1, TT_PART_BO_DTL T2, TT_PART_DLR_ORDER_MAIN T3 \n");
            sql.append("  WHERE T1.BO_ID = T2.BO_ID \n");
            sql.append("    AND T1.ORDER_ID = T3.ORDER_ID \n");
            sql.append("    AND T1.BO_ID IN (").append(boIds);
            sql.append(") \n");
            sql.append("    AND T2.PART_ID = ").append(partId).append("\n");
            return pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<String, Object> queryMainInfo(RequestWrapper request, AclUserBean logonUser) throws Exception {
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String boCode = CommonUtils.checkNull(request.getParamValue("BO_CODE"));//BO单号
        String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//订单单位编码
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
        String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
        String state = CommonUtils.checkNull(request.getParamValue("STATE"));//BO单状态
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//开始时间
        String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//结束时间
        String startDate1 = CommonUtils.checkNull(request.getParamValue("startDate1"));//开始时间
        String endDate1 = CommonUtils.checkNull(request.getParamValue("endDate1"));//结束时间
        String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));//配件编码
        String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));//销售人员
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT COUNT(1) ALLQTY,\n");
            sql.append("       nvl(SUM(T3.BO_QTY),0) BO_QTY,\n");
            sql.append("       nvl(sum(t3.bo_oddqty),0) BO_NUM,");
            sql.append("       TO_CHAR(nvl(SUM(T3.BO_ODDQTY * T3.BUY_PRICE),0), 'FM999,999,990.00') AMOUNT\n");
            sql.append("  FROM TT_PART_BO_MAIN        T1,\n");
            sql.append("       TT_PART_DLR_ORDER_MAIN T2,\n");
            sql.append("       TT_PART_BO_DTL         T3,\n");
            sql.append("       TT_PART_SALES_RELATION T4\n");
            sql.append(" WHERE T1.ORDER_ID = T2.ORDER_ID\n");
            sql.append("   AND T2.SELLER_ID = T4.PARENTORG_ID\n");
            sql.append("   AND T2.DEALER_ID = T4.CHILDORG_ID\n");
            sql.append("   AND T1.BO_ID = T3.BO_ID\n");
            sql.append("   AND (MONTHS_BETWEEN(TRUNC(SYSDATE, 'mm'),\n");
            sql.append("                       TO_DATE(TO_CHAR(T1.CREATE_DATE, 'yyyy-mm'), 'yyyy-mm')) <=\n");
            sql.append("       T4.BO_DAYS)\n");

            if (logonUser.getDealerId() == null) {
//                sql.append("  AND T2.SELLER_ID=");
//                sql.append(logonUser.getOrgId()).append(")");
            } else {
//                sql.append(logonUser.getDealerId());
                sql.append("  AND T2.DEALER_ID=");
                sql.append(logonUser.getDealerId()).append("");
            }

            if (!"".equals(orderCode)) {
                sql.append(" AND T1.ORDER_CODE LIKE '%")
                        .append(orderCode.trim()).append("%'\n");
            }
            if (!"".equals(boCode)) {
                sql.append(" AND T1.BO_CODE LIKE '%")
                        .append(boCode.trim()).append("%'\n");
            }
            if (!"".equals(dealerCode)) {
                sql.append(" AND T2.DEALER_CODE LIKE '%")
                        .append(dealerCode.trim()).append("%'\n");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND T2.DEALER_NAME LIKE '%")
                        .append(dealerName.trim()).append("%'\n");
            }
            if (!"".equals(sellerName)) {
                sql.append(" AND T2.SELLER_NAME LIKE '%")
                        .append(sellerName.trim()).append("%'\n");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND T3.PART_OLDCODE LIKE '%")
                        .append(partOldCode.trim()).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(startDate1)) {
                sql.append(" AND TO_DATE(T2.CREATE_DATE)>=").append("TO_DATE('").append(startDate1).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate1)) {
                sql.append(" AND TO_DATE(T2.CREATE_DATE)<=").append("TO_DATE('").append(endDate1).append("','yyyy-MM-dd')");
            }
            if (!"".equals(state)) {
                sql.append(" AND T1.STATE=")
                        .append(state);
            }
            if (!"".equals(orderType)) {
                sql.append(" AND T2.ORDER_TYPE=")
                        .append(orderType);
            }
            sql.append(" AND T1.STATUS=1 AND T2.STATUS=1");

            if (logonUser.getDealerId() == null) {
                if (!"".equals(salerId)) {
                    sql.append("  AND EXISTS (SELECT 1\n" +
                            "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
                            "         WHERE T2.DEALER_ID = B.DEALER_ID\n" +
                            "           AND B.USER_ID =" + salerId + ")");
                }
            }

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPartBoDtlList(
            RequestWrapper request, AclUserBean logonUser, Integer curPage,
            Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;

        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String boCode = CommonUtils.checkNull(request.getParamValue("BO_CODE"));//BO单号
        String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//订单单位编码
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
        String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
        String state = CommonUtils.checkNull(request.getParamValue("STATE"));//BO单状态
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//开始时间
        String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//结束时间
        String startDate1 = CommonUtils.checkNull(request.getParamValue("startDate1"));//开始时间
        String endDate1 = CommonUtils.checkNull(request.getParamValue("endDate1"));//结束时间
        String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));//配件编码
        String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));//销售人员
        String nowOrgId = logonUser.getDealerId() == null ? logonUser.getOrgId().toString() : logonUser.getDealerId();

        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T2.DEALER_NAME,\n");
            sql.append("       T2.DEALER_CODE,\n");
            sql.append("       T3.PART_CODE,\n");
            sql.append("       T3.PART_OLDCODE,\n");
            sql.append("       T3.PART_CNAME,\n");
            sql.append("       T3.BUY_QTY,\n");
            sql.append("       T3.SALES_QTY,\n");
            sql.append("       T3.BO_QTY,\n");
            sql.append("       T3.TOSAL_QTY,\n");
            sql.append("       T3.CLOSE_QTY,\n");
            sql.append("       T3.BO_ODDQTY,\n");
            sql.append("       TO_CHAR((T3.BO_ODDQTY * T3.BUY_PRICE), 'FM999,999,990.00') AMOUNT,\n");
            sql.append("(SELECT SUM(VW.NORMAL_QTY)\n");
            sql.append("        FROM TT_PART_BOOK VW\n");
            sql.append("       WHERE VW.PART_ID = T3.PART_ID\n");
            sql.append("         AND VW.ORG_ID = " + nowOrgId + ") NORMAL_QTY,\n");
            sql.append("    NVL((SELECT SUM(VW.ON_QTY)\n");
            sql.append("        FROM TT_PART_ONWAY VW\n");
            sql.append("       WHERE VW.PART_ID = T3.PART_ID\n");
            sql.append("         /*AND VW.ORG_ID = " + nowOrgId + "*/),0) ZT_QTY,\n");
            sql.append("       T3.PART_ID,\n");
            sql.append("       T2.WH_ID\n");
            sql.append("  FROM TT_PART_BO_MAIN        T1,\n");
            sql.append("       TT_PART_DLR_ORDER_MAIN T2,\n");
            sql.append("       TT_PART_BO_DTL         T3,\n");
            sql.append("       TT_PART_SALES_RELATION T4\n");
           /* sql.append("       VW_PART_STOCK          VW\n");*/
            sql.append(" WHERE T1.ORDER_ID = T2.ORDER_ID\n");
            sql.append("   AND T2.SELLER_ID = T4.PARENTORG_ID\n");
            sql.append("   AND T2.DEALER_ID = T4.CHILDORG_ID\n");
            sql.append("   AND T1.BO_ID = T3.BO_ID\n");
         /*   sql.append("   AND VW.PART_ID = T3.PART_ID\n");
            sql.append("   AND VW.ORG_ID = T2.SELLER_ID\n");*/
            sql.append("   AND (MONTHS_BETWEEN(TRUNC(SYSDATE, 'mm'),\n");
            sql.append("                       TO_DATE(TO_CHAR(T1.CREATE_DATE, 'yyyy-mm'), 'yyyy-mm')) <=\n");
            sql.append("       T4.BO_DAYS)\n");
            sql.append("  AND (T2.SELLER_ID=");

            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId()).append(")");
            } else {
                sql.append(logonUser.getDealerId());
                sql.append("  OR T2.DEALER_ID=");
                sql.append(logonUser.getDealerId()).append(")");
            }

            if (!"".equals(orderCode)) {
                sql.append(" AND T1.ORDER_CODE LIKE '%")
                        .append(orderCode.trim()).append("%'\n");
            }
            if (!"".equals(boCode)) {
                sql.append(" AND T1.BO_CODE LIKE '%")
                        .append(boCode.trim()).append("%'\n");
            }
            if (!"".equals(dealerCode)) {
                sql.append(" AND T2.DEALER_CODE LIKE '%")
                        .append(dealerCode.trim()).append("%'\n");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND T2.DEALER_NAME LIKE '%")
                        .append(dealerName.trim()).append("%'\n");
            }
            if (!"".equals(sellerName)) {
                sql.append(" AND T2.SELLER_NAME LIKE '%")
                        .append(sellerName.trim()).append("%'\n");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T3.PART_OLDCODE) LIKE '%")
                        .append(partOldCode.trim().toUpperCase()).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(startDate1)) {
                sql.append(" AND TO_DATE(T2.CREATE_DATE)>=").append("TO_DATE('").append(startDate1).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate1)) {
                sql.append(" AND TO_DATE(T2.CREATE_DATE)<=").append("TO_DATE('").append(endDate1).append("','yyyy-MM-dd')");
            }
            if (!"".equals(state)) {
                sql.append(" AND T1.STATE=")
                        .append(state);
            }
            if (!"".equals(orderType)) {
                sql.append(" AND T2.ORDER_TYPE=")
                        .append(orderType);
            }
            sql.append(" AND T1.STATUS=1 AND T2.STATUS=1");

            if (logonUser.getDealerId() == null) {
                if (!"".equals(salerId)) {
                    sql.append("  AND EXISTS (SELECT 1\n" +
                            "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
                            "         WHERE T2.DEALER_ID = B.DEALER_ID\n" +
                            "           AND B.USER_ID =" + salerId + ")");
                }
            }
//            sql.append(" ORDER BY T3.PART_OLDCODE, T3.CREATE_DATE");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }


    public PageResult<Map<String, Object>> queryPartBoRiskList(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));
            String soCOde = CommonUtils.checkNull(request.getParamValue("SO_CODE"));
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String region = CommonUtils.checkNull(request.getParamValue("REGION"));
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String rSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
            String sortType = CommonUtils.checkNull(request.getParamValue("sortType"));//排序方式
            String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//配件是否有效

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT BS.*,\n");
            sql.append("       NVL(BOS.BUY_QTY, 0) BUY_QTY,\n");
            sql.append("       NVL(BOS.BUY_AMOUNT, 0) BUY_AMOUNT,\n");
            sql.append("       NVL(BOS.SALES_QTY, 0) SALES_QTY,\n");
            sql.append("       NVL(BOS.BO_QTY, 0) BO_QTY,\n");
            sql.append("       NVL(BOS.BO_AMOUNT, 0) BO_AMOUNT,\n");
            sql.append("       NVL(BOS.TOSAL_QTY, 0) TOSAL_QTY,\n");
            sql.append("       NVL(BOS.BOXS, 0) BOXS,\n");
            sql.append("       NVL(BOS.XSXS, 0) XSXS,\n");
            sql.append("       TO_CHAR(NVL(BOS.SALES_QTY, 0) / NVL(BOS.BUY_QTY, 1) * 100, '999.99') || '%' XLMZLV\n");
            sql.append("   FROM (SELECT TPD.PART_ID, \n");
            sql.append("                (SELECT NVL(V.NORMAL_QTY, 0) \n");
            sql.append("                   FROM VW_PART_STOCK V \n");
            sql.append("                  WHERE V.PART_ID = TPD.PART_ID \n");
            sql.append("                    AND V.WH_ID = ").append(whId).append(") NORMAL_QTY, \n");
            sql.append("                (SELECT VD.VENDER_NAME \n");
            sql.append("                   FROM TT_PART_BUY_PRICE BP, TT_PART_VENDER_DEFINE VD \n");
            sql.append("                  WHERE BP.VENDER_ID = VD.VENDER_ID \n");
            sql.append("                    AND BP.IS_DEFAULT = ").append(Constant.IF_TYPE_YES);
            sql.append("                    AND BP.PART_ID = TPD.PART_ID \n");
            sql.append("                    ) VENDER_NAME, --默认供应商 \n");
            sql.append("                TPD.PART_TYPE, --配件类型 \n");
            sql.append("                TPD.PART_OLDCODE, --配件编码 \n");
            sql.append("                TPD.PART_CNAME, --配件名称 \n");
            sql.append("                TPD.PART_CODE, --配件件号 \n");
            sql.append("                TPD.STATE, --是否有效 \n");
            sql.append("                (SELECT PD.AVG_QTY \n");
            sql.append("                   FROM TT_PART_PLAN_DEFINE PD \n");
            sql.append("                  WHERE PD.PART_ID = TPD.PART_ID \n");
            sql.append("                    AND PD.PLAN_TYPE = 1 AND ROWNUM = 1) * 30 AVG_QTY, --平均月销量 \n");
            sql.append("                (SELECT PD.SAFETY_STOCK \n");
            sql.append("                   FROM TT_PART_PLAN_DEFINE PD \n");
            sql.append("                  WHERE PD.PART_ID = TPD.PART_ID \n");
            sql.append("                    AND PD.PLAN_TYPE = 1 AND ROWNUM = 1) * 1 SAFETY_STOCK, --安全库存 \n");
            sql.append("                NVL(ZT.ZT_QTY, 0) ZT_QTY, --在途数量 \n");
            sql.append("                NVL(DRK.SPAREIN_QTY, 0) SPAREIN_QTY, --待入库数量, \n");
            sql.append("                TU.NAME --计划员 \n");
            sql.append("           FROM TT_PART_DEFINE TPD, \n");
            sql.append("                (SELECT D.PART_ID, M.WH_ID, \n");
            sql.append("CASE\n");
            sql.append("                       WHEN SUM(D.SPARE_QTY) > 0 THEN\n");
            sql.append("                        SUM(D.SPARE_QTY)\n");
            sql.append("                       WHEN SUM(D.SPARE_QTY) < 0 THEN\n");
            sql.append("                        0\n");
            sql.append("                     END ZT_QTY\n");
            sql.append("                   FROM TT_PART_PO_MAIN M, TT_PART_PO_DTL D --有效订单未分配数量 \n");
            sql.append("                  WHERE D.ORDER_ID = M.ORDER_ID \n");
            sql.append("                    AND M.STATE = ").append(Constant.PURCHASE_ORDER_STATE_01);
            sql.append("                    AND D.SPARE_QTY > 0\n");
            sql.append("                    AND (TRUNC(SYSDATE) - TRUNC(D.CREATE_DATE)) <= \n");
            sql.append("                        (SELECT D.DAYS \n");
            sql.append("                           FROM TT_PART_PERIOD_DEFINE D \n");
            sql.append("                          WHERE D.STATE = ").append(Constant.STATUS_ENABLE);
            sql.append("                            AND D.STATUS = 1) \n");
            sql.append("                  GROUP BY D.PART_ID, M.WH_ID) ZT, \n");
            sql.append("                (SELECT PO.PART_ID, \n");
            sql.append("                        PO.WH_ID, \n");
            sql.append("                        SUM(PO.GENERATE_QTY - PO.IN_QTY) SPAREIN_QTY \n");
            sql.append("                   FROM TT_PART_OEM_PO PO --验收中未入库数量 \n");
            sql.append("                  WHERE PO.STATE <> ").append(Constant.PART_PURCHASE_ORDERCHK_STATUS_02).append("--非关闭 \n");
            sql.append("                    AND PO.STATUS = 1 HAVING \n");
            sql.append("                  SUM(PO.GENERATE_QTY - PO.IN_QTY) > 0 \n");
            sql.append("                  GROUP BY PO.PART_ID, PO.WH_ID) DRK, \n");
            sql.append("                TC_USER TU \n");
            sql.append("          WHERE TPD.PART_ID = ZT.PART_ID(+) \n");
            sql.append("            AND TPD.PART_ID = DRK.PART_ID(+) \n");
            sql.append("            AND TPD.PLANER_ID = TU.USER_ID(+) \n");
            if (!"".equals(planerId) && null != planerId) {
                sql.append("AND TU.USER_ID =").append(planerId);
            }
            if (!"".equals(venderId)) {
                sql.append(" AND (SELECT BP.VENDER_ID \n");
                sql.append("                   FROM TT_PART_BUY_PRICE BP \n");
                sql.append("                  WHERE BP.PART_ID = TPD.PART_ID \n");
                sql.append("                    AND BP.IS_DEFAULT = ").append(Constant.IF_TYPE_YES);
                sql.append("                    AND BP.STATE = ").append(Constant.STATUS_ENABLE).append(")=").append(venderId);
            }
            if ("1".equals(rSelect)) {
                sql.append("         AND (SELECT NVL(V.NORMAL_QTY, 0) \n");
                sql.append("            FROM VW_PART_STOCK V \n");
                sql.append("           WHERE V.PART_ID = TPD.PART_ID \n");
                sql.append("             AND V.WH_ID = ").append(whId).append(")< \n");
                sql.append("         ((SELECT PD.AVG_QTY \n");
                sql.append("             FROM TT_PART_PLAN_DEFINE PD \n");
                sql.append("            WHERE PD.PART_ID = TPD.PART_ID \n");
                sql.append("              AND PD.PLAN_TYPE = 1 AND ROWNUM = 1) * 30) \n");
            }
            if ("2".equals(rSelect)) {
                sql.append("  AND (SELECT NVL(V.NORMAL_QTY, 0) \n");
                sql.append("            FROM VW_PART_STOCK V \n");
                sql.append("           WHERE V.PART_ID = TPD.PART_ID \n");
                sql.append("             AND V.WH_ID = ").append(whId).append(")< \n");
                sql.append("         ((SELECT PD.SAFETY_STOCK \n");
                sql.append("                   FROM TT_PART_PLAN_DEFINE PD \n");
                sql.append("                  WHERE PD.PART_ID = TPD.PART_ID \n");
                sql.append("                    AND PD.PLAN_TYPE = 1 AND ROWNUM = 1) * 1) \n");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(TPD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(TPD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partType)) {
                sql.append(" AND TPD.PART_TYPE=").append(partType);
            }
            if (!"".equals(state)) {
                sql.append(" AND TPD.STATE=").append(state);
            }
            sql.append("         ) BS, \n");
            sql.append("(SELECT PO.PART_ID,\n");
            sql.append("        SUM(PO.BUY_AMOUNT) BUY_AMOUNT, --订货金额\n");
            sql.append("        SUM(PO.BUY_QTY) BUY_QTY, --订货数量\n");
            sql.append("        SUM(NVL(SO.SALES_QTY, 0)) SALES_QTY, --已交付数量\n");
            sql.append("        SUM((CASE\n");
            sql.append("              WHEN SO.SALES_QTY IS NULL THEN\n");
            sql.append("               0\n");
            sql.append("              WHEN SO.SALES_QTY IS NOT NULL THEN\n");
            sql.append("               1\n");
            sql.append("            END)) XSXS, --销售总项数\n");
            sql.append("        SUM(NVL(BO.BO_QTY, 0)) BO_QTY, --BO数量\n");
            sql.append("        SUM((CASE\n");
            sql.append("              WHEN BO.BO_QTY IS NULL THEN\n");
            sql.append("               0\n");
            sql.append("              WHEN BO.BO_QTY IS NOT NULL THEN\n");
            sql.append("               1\n");
            sql.append("            END)) BOXS, --BO总项数\n");
            sql.append("        SUM(NVL(BO.BO_AMOUNT, 0)) BO_AMOUNT,\n");
            sql.append("        SUM(NVL(BO.TOSAL_QTY, 0)) TOSAL_QTY --BO满足数量\n");
            sql.append("   FROM (SELECT OM.ORDER_ID,\n");
            sql.append("                OD.PART_ID,\n");
            sql.append("                OD.BUY_PRICE,\n");
            sql.append("                SUM(OD.BUY_AMOUNT) BUY_AMOUNT,\n");
            sql.append("                SUM(OD.BUY_QTY) BUY_QTY\n");
            sql.append("           FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DLR_ORDER_MAIN OM\n");
            sql.append("          WHERE OD.ORDER_ID = OM.ORDER_ID\n");
            sql.append("            AND OM.SELLER_ID = ");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            sql.append("AND EXISTS\n");
            sql.append("                (SELECT 1\n");
            sql.append("                         FROM TT_PART_SO_MAIN M\n");
            sql.append("                        WHERE M.ORDER_ID = OM.ORDER_ID\n");
            if (!"".equals(soCOde)) {
                sql.append("                          AND M.SO_CODE LIKE  '%").append(soCOde).append("%' \n");
            }
            if (!"".equals(orderCode)) {
                sql.append("                          AND M.ORDER_CODE LIKE  '%").append(orderCode).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
            }
            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
            }
            sql.append(")\n");
            sql.append(" GROUP BY OM.ORDER_ID, OD.PART_ID, OD.BUY_PRICE\n");
            sql.append("UNION ALL\n");
            sql.append("SELECT OM.ORDER_ID,\n");
            sql.append("       OD.PART_ID,\n");
            sql.append("       OD.BUY_PRICE,\n");
            sql.append("       SUM(OD.BUY_AMOUNT) BUY_AMOUNT,\n");
            sql.append("       SUM(OD.BUY_QTY) BUY_QTY\n");
            sql.append("  FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DLR_ORDER_MAIN OM\n");
            sql.append(" WHERE OD.ORDER_ID = OM.ORDER_ID\n");
            sql.append("   AND OM.SELLER_ID = 2010010100070674\n");
            sql.append("   AND EXISTS\n");
            sql.append(" (SELECT 1\n");
            sql.append("          FROM TT_PART_BO_MAIN BM\n");
            sql.append("         WHERE BM.ORDER_ID = OM.ORDER_ID\n");
            sql.append("           AND BM.SO_ID IS NULL\n");
            if (!"".equals(orderCode)) {
                sql.append("                          AND BM.ORDER_CODE LIKE  '%").append(orderCode).append("%' \n");
            }
            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
            }
            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
            }
            sql.append(")\n");
            sql.append("          GROUP BY OM.ORDER_ID, OD.PART_ID, OD.BUY_PRICE) PO,\n");
            sql.append("        (SELECT SM.ORDER_ID, SD.PART_ID, SUM(SD.SALES_QTY) SALES_QTY\n");
            sql.append("           FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM\n");
            sql.append("          WHERE SD.SO_ID = SM.SO_ID\n");
            sql.append("           AND SM.STATUS = 1\n");
            sql.append("           AND SD.STATUS = 1\n");
            sql.append("           AND SM.ORDER_ID IS NOT NULL\n");
            sql.append("           AND SM.SELLER_ID = ");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            sql.append(" AND EXISTS (SELECT 1 \n");
            sql.append("                   FROM TT_PART_SO_MAIN BM \n");
            sql.append("                  WHERE BM.SO_ID = SM.SO_ID \n");
            if (!"".equals(soCOde)) {
                sql.append("                    AND BM.SO_CODE LIKE '%").append(soCOde).append("%' \n");
            }
            if (!"".equals(orderCode)) {
                sql.append("         AND BM.ORDER_CODE LIKE '%").append(orderCode).append("%' \n");
            }
            if (!"".equals(dealerName)) {
                sql.append("         AND BM.DEALER_NAME LIKE '%").append(dealerName).append("%' \n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
            }
            sql.append(")\n");
            sql.append("          GROUP BY SM.ORDER_ID, SD.PART_ID) SO,\n");
            sql.append("        (SELECT BM.ORDER_ID,\n");
            sql.append("                BD.PART_ID,\n");
            sql.append("                SUM(BD.BUY_QTY) BUY_QTY,\n");
            sql.append("                SUM(BD.SALES_QTY) SALES_QTY,\n");
            sql.append("                SUM(BD.BO_QTY) BO_QTY,\n");
            sql.append("                SUM(BD.BO_QTY * BD.BUY_PRICE) BO_AMOUNT,\n");
            sql.append("                SUM(BD.TOSAL_QTY) TOSAL_QTY\n");
            sql.append("           FROM TT_PART_BO_DTL BD, TT_PART_BO_MAIN BM\n");
            sql.append("          WHERE BD.BO_ID = BM.BO_ID\n");
            sql.append("            AND BM.BO_TYPE=1\n");
            if (!"".equals(soCOde)) {
                sql.append("     AND EXISTS (SELECT 1 \n");
                sql.append("                   FROM TT_PART_SO_MAIN sm \n");
                sql.append("                  WHERE BM.SO_ID = SM.SO_ID \n");
                sql.append("                    AND sm.SO_CODE LIKE '%").append(soCOde).append("%') \n");
            }
            if (!"".equals(orderCode)) {
                sql.append("         AND bm.ORDER_CODE LIKE '%").append(orderCode).append("%' \n");
            }
            sql.append("          GROUP BY BM.ORDER_ID, BD.PART_ID) BO\n");
            sql.append("  WHERE PO.ORDER_ID = SO.ORDER_ID(+)\n");
            sql.append("    AND PO.PART_ID = SO.PART_ID(+)\n");
            sql.append("    AND PO.ORDER_ID = BO.ORDER_ID(+)\n");
            sql.append("    AND PO.PART_ID = BO.PART_ID(+)\n");
            sql.append("    HAVING  SUM(NVL(BO.BO_AMOUNT, 0))>0\n");
            sql.append("  GROUP BY PO.PART_ID) BOS\n");
            if ("1".equals(rSelect) || "2".equals(rSelect)) {
                sql.append("  WHERE BS.PART_ID = BOS.PART_ID(+) \n");
            } else {
                sql.append("  WHERE BS.PART_ID = BOS.PART_ID \n");
            }
            if (!"".equals(sortType)) {
                if ("1".equals(sortType)) {
                    sql.append(" ORDER BY BS.PART_ID,BOXS DESC");
                } else if ("2".equals(sortType)) {
                    sql.append(" ORDER BY BS.PART_ID,BO_QTY DESC ");
                } else {
                    sql.append(" ORDER BY BOXS DESC,BO_QTY DESC ");
                }
            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartBoRisk(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));
            String soCOde = CommonUtils.checkNull(request.getParamValue("SO_CODE"));
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String region = CommonUtils.checkNull(request.getParamValue("REGION"));
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String rSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
            String sortType = CommonUtils.checkNull(request.getParamValue("sortType"));//排序方式
            String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//配件是否有效

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT BS.*,\n");
            sql.append("       NVL(BOS.BUY_QTY, 0) BUY_QTY,\n");
            sql.append("       NVL(BOS.BUY_AMOUNT, 0) BUY_AMOUNT,\n");
            sql.append("       NVL(BOS.SALES_QTY, 0) SALES_QTY,\n");
            sql.append("       NVL(BOS.BO_QTY, 0) BO_QTY,\n");
            sql.append("       NVL(BOS.BO_AMOUNT, 0) BO_AMOUNT,\n");
            sql.append("       NVL(BOS.TOSAL_QTY, 0) TOSAL_QTY,\n");
            sql.append("       NVL(BOS.BOXS, 0) BOXS,\n");
            sql.append("       NVL(BOS.XSXS, 0) XSXS,\n");
            sql.append("       TO_CHAR(NVL(BOS.SALES_QTY, 0) / NVL(BOS.BUY_QTY, 1) * 100, '999.99') || '%' XLMZLV\n");
            sql.append("   FROM (SELECT TPD.PART_ID, \n");
            sql.append("                (SELECT NVL(V.NORMAL_QTY, 0) \n");
            sql.append("                   FROM VW_PART_STOCK V \n");
            sql.append("                  WHERE V.PART_ID = TPD.PART_ID \n");
            sql.append("                    AND V.WH_ID = ").append(whId).append(") NORMAL_QTY, \n");
            sql.append("                (SELECT VD.VENDER_NAME \n");
            sql.append("                   FROM TT_PART_BUY_PRICE BP, TT_PART_VENDER_DEFINE VD \n");
            sql.append("                  WHERE BP.VENDER_ID = VD.VENDER_ID \n");
            sql.append("                    AND BP.IS_DEFAULT = ").append(Constant.IF_TYPE_YES);
            sql.append("                    AND BP.PART_ID = TPD.PART_ID \n");
            sql.append("                    AND BP.STATE = ").append(Constant.STATUS_ENABLE).append(") VENDER_NAME, --默认供应商 \n");
            //sql.append("                TPD.PART_TYPE, --配件类型 \n");
            sql.append("(SELECT c.code_desc FROM tc_code  c WHERE c.code_id=TPD.PART_TYPE ) PART_TYPE,  --配件类型\n");
            sql.append("                TPD.PART_OLDCODE, --配件编码 \n");
            sql.append("                TPD.PART_CNAME, --配件名称 \n");
            sql.append("                TPD.PART_CODE, --配件件号 \n");
            sql.append("                TPD.STATE, \n");
            sql.append("                (SELECT PD.AVG_QTY \n");
            sql.append("                   FROM TT_PART_PLAN_DEFINE PD \n");
            sql.append("                  WHERE PD.PART_ID = TPD.PART_ID \n");
            sql.append("                    AND PD.PLAN_TYPE = 1 AND ROWNUM = 1) * 30 AVG_QTY, --平均月销量 \n");
            sql.append("                (SELECT PD.SAFETY_STOCK \n");
            sql.append("                   FROM TT_PART_PLAN_DEFINE PD \n");
            sql.append("                  WHERE PD.PART_ID = TPD.PART_ID \n");
            sql.append("                    AND PD.PLAN_TYPE = 1 AND ROWNUM = 1) * 1 SAFETY_STOCK, --安全库存 \n");
            sql.append("                NVL(ZT.ZT_QTY, 0) ZT_QTY, --在途数量 \n");
            sql.append("                NVL(DRK.SPAREIN_QTY, 0) SPAREIN_QTY, --待入库数量, \n");
            sql.append("                TU.NAME --计划员 \n");
            sql.append("           FROM TT_PART_DEFINE TPD, \n");
            sql.append("                (SELECT D.PART_ID, M.WH_ID, \n");
            sql.append("CASE\n");
            sql.append("                       WHEN SUM(D.SPARE_QTY) > 0 THEN\n");
            sql.append("                        SUM(D.SPARE_QTY)\n");
            sql.append("                       WHEN SUM(D.SPARE_QTY) < 0 THEN\n");
            sql.append("                        0\n");
            sql.append("                     END ZT_QTY\n");
            sql.append("                   FROM TT_PART_PO_MAIN M, TT_PART_PO_DTL D --有效订单未分配数量 \n");
            sql.append("                  WHERE D.ORDER_ID = M.ORDER_ID \n");
            sql.append("                    AND M.STATE = ").append(Constant.PURCHASE_ORDER_STATE_01);
            sql.append("                    AND D.SPARE_QTY > 0\n");
            sql.append("                    AND (TRUNC(SYSDATE) - TRUNC(D.CREATE_DATE)) <= \n");
            sql.append("                        (SELECT D.DAYS \n");
            sql.append("                           FROM TT_PART_PERIOD_DEFINE D \n");
            sql.append("                          WHERE D.STATE = ").append(Constant.STATUS_ENABLE);
            sql.append("                            AND D.STATUS = 1) \n");
            sql.append("                  GROUP BY D.PART_ID, M.WH_ID) ZT, \n");
            sql.append("                (SELECT PO.PART_ID, \n");
            sql.append("                        PO.WH_ID, \n");
            sql.append("                        SUM(PO.GENERATE_QTY - PO.IN_QTY) SPAREIN_QTY \n");
            sql.append("                   FROM TT_PART_OEM_PO PO --验收中未入库数量 \n");
            sql.append("                  WHERE PO.STATE <> ").append(Constant.PART_PURCHASE_ORDERCHK_STATUS_02).append("--非关闭 \n");
            sql.append("                    AND PO.STATUS = 1 HAVING \n");
            sql.append("                  SUM(PO.GENERATE_QTY - PO.IN_QTY) > 0 \n");
            sql.append("                  GROUP BY PO.PART_ID, PO.WH_ID) DRK, \n");
            sql.append("                TC_USER TU \n");
            sql.append("          WHERE TPD.PART_ID = ZT.PART_ID(+) \n");
            sql.append("            AND TPD.PART_ID = DRK.PART_ID(+) \n");
            sql.append("            AND TPD.PLANER_ID = TU.USER_ID(+) \n");
            if (!"".equals(planerId) && null != planerId) {
                sql.append("AND TU.USER_ID =").append(planerId);
            }
            if (!"".equals(venderId)) {
                sql.append(" AND (SELECT BP.VENDER_ID \n");
                sql.append("                   FROM TT_PART_BUY_PRICE BP \n");
                sql.append("                  WHERE BP.PART_ID = TPD.PART_ID \n");
                sql.append("                    AND BP.IS_DEFAULT = ").append(Constant.IF_TYPE_YES);
                sql.append("                    AND BP.STATE = ").append(Constant.STATUS_ENABLE).append(")=").append(venderId);
            }
            if ("1".equals(rSelect)) {
                sql.append("         AND (SELECT NVL(V.NORMAL_QTY, 0) \n");
                sql.append("            FROM VW_PART_STOCK V \n");
                sql.append("           WHERE V.PART_ID = TPD.PART_ID \n");
                sql.append("             AND V.WH_ID = ").append(whId).append(")< \n");
                sql.append("         ((SELECT PD.AVG_QTY \n");
                sql.append("             FROM TT_PART_PLAN_DEFINE PD \n");
                sql.append("            WHERE PD.PART_ID = TPD.PART_ID \n");
                sql.append("              AND PD.PLAN_TYPE = 1 AND ROWNUM = 1) * 30) \n");
            }
            if ("2".equals(rSelect)) {
                sql.append("  AND (SELECT NVL(V.NORMAL_QTY, 0) \n");
                sql.append("            FROM VW_PART_STOCK V \n");
                sql.append("           WHERE V.PART_ID = TPD.PART_ID \n");
                sql.append("             AND V.WH_ID = ").append(whId).append(")< \n");
                sql.append("         ((SELECT PD.SAFETY_STOCK \n");
                sql.append("                   FROM TT_PART_PLAN_DEFINE PD \n");
                sql.append("                  WHERE PD.PART_ID = TPD.PART_ID \n");
                sql.append("                    AND PD.PLAN_TYPE = 1 AND ROWNUM = 1) * 1) \n");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(TPD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(TPD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partType)) {
                sql.append(" AND TPD.PART_TYPE=").append(partType);
            }
            if (!"".equals(state)) {
                sql.append(" AND TPD.STATE=").append(state);
            }
            sql.append("         ) BS, \n");
            sql.append("(SELECT PO.PART_ID,\n");
            sql.append("        SUM(PO.BUY_AMOUNT) BUY_AMOUNT, --订货金额\n");
            sql.append("        SUM(PO.BUY_QTY) BUY_QTY, --订货数量\n");
            sql.append("        SUM(NVL(SO.SALES_QTY, 0)) SALES_QTY, --已交付数量\n");
            sql.append("        SUM((CASE\n");
            sql.append("              WHEN SO.SALES_QTY IS NULL THEN\n");
            sql.append("               0\n");
            sql.append("              WHEN SO.SALES_QTY IS NOT NULL THEN\n");
            sql.append("               1\n");
            sql.append("            END)) XSXS, --销售总项数\n");
            sql.append("        SUM(NVL(BO.BO_QTY, 0)) BO_QTY, --BO数量\n");
            sql.append("        SUM((CASE\n");
            sql.append("              WHEN BO.BO_QTY IS NULL THEN\n");
            sql.append("               0\n");
            sql.append("              WHEN BO.BO_QTY IS NOT NULL THEN\n");
            sql.append("               1\n");
            sql.append("            END)) BOXS, --BO总项数\n");
            sql.append("        SUM(NVL(BO.BO_AMOUNT, 0)) BO_AMOUNT,\n");
            sql.append("        SUM(NVL(BO.TOSAL_QTY, 0)) TOSAL_QTY --BO满足数量\n");
            sql.append("   FROM (SELECT OM.ORDER_ID,\n");
            sql.append("                OD.PART_ID,\n");
            sql.append("                OD.BUY_PRICE,\n");
            sql.append("                SUM(OD.BUY_AMOUNT) BUY_AMOUNT,\n");
            sql.append("                SUM(OD.BUY_QTY) BUY_QTY\n");
            sql.append("           FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DLR_ORDER_MAIN OM\n");
            sql.append("          WHERE OD.ORDER_ID = OM.ORDER_ID\n");
            sql.append("            AND OM.SELLER_ID = ");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            sql.append("AND EXISTS\n");
            sql.append("                (SELECT 1\n");
            sql.append("                         FROM TT_PART_SO_MAIN M\n");
            sql.append("                        WHERE M.ORDER_ID = OM.ORDER_ID\n");
            if (!"".equals(soCOde)) {
                sql.append("                          AND M.SO_CODE LIKE  '%").append(soCOde).append("%' \n");
            }
            if (!"".equals(orderCode)) {
                sql.append("                          AND M.ORDER_CODE LIKE  '%").append(orderCode).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
            }
            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
            }
            sql.append(")\n");
            sql.append(" GROUP BY OM.ORDER_ID, OD.PART_ID, OD.BUY_PRICE\n");
            sql.append("UNION ALL\n");
            sql.append("SELECT OM.ORDER_ID,\n");
            sql.append("       OD.PART_ID,\n");
            sql.append("       OD.BUY_PRICE,\n");
            sql.append("       SUM(OD.BUY_AMOUNT) BUY_AMOUNT,\n");
            sql.append("       SUM(OD.BUY_QTY) BUY_QTY\n");
            sql.append("  FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DLR_ORDER_MAIN OM\n");
            sql.append(" WHERE OD.ORDER_ID = OM.ORDER_ID\n");
            sql.append("   AND OM.SELLER_ID = 2010010100070674\n");
            sql.append("   AND EXISTS\n");
            sql.append(" (SELECT 1\n");
            sql.append("          FROM TT_PART_BO_MAIN BM\n");
            sql.append("         WHERE BM.ORDER_ID = OM.ORDER_ID\n");
            sql.append("           AND BM.SO_ID IS NULL\n");
            if (!"".equals(orderCode)) {
                sql.append("                          AND BM.ORDER_CODE LIKE  '%").append(orderCode).append("%' \n");
            }
            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
            }
            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
            }
            sql.append(")\n");
            sql.append("          GROUP BY OM.ORDER_ID, OD.PART_ID, OD.BUY_PRICE) PO,\n");
            sql.append("        (SELECT SM.ORDER_ID, SD.PART_ID, SUM(SD.SALES_QTY) SALES_QTY\n");
            sql.append("           FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM\n");
            sql.append("          WHERE SD.SO_ID = SM.SO_ID\n");
            sql.append("            AND SM.SELLER_ID = ");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            sql.append(" AND EXISTS (SELECT 1 \n");
            sql.append("                   FROM TT_PART_SO_MAIN BM \n");
            sql.append("                  WHERE BM.SO_ID = SM.SO_ID \n");
            if (!"".equals(soCOde)) {
                sql.append("                    AND BM.SO_CODE LIKE '%").append(soCOde).append("%' \n");
            }
            if (!"".equals(orderCode)) {
                sql.append("         AND BM.ORDER_CODE LIKE '%").append(orderCode).append("%' \n");
            }
            if (!"".equals(dealerName)) {
                sql.append("         AND BM.DEALER_NAME LIKE '%").append(dealerName).append("%' \n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
            }
            sql.append(")\n");
            sql.append("          GROUP BY SM.ORDER_ID, SD.PART_ID) SO,\n");
            sql.append("        (SELECT BM.ORDER_ID,\n");
            sql.append("                BD.PART_ID,\n");
            sql.append("                SUM(BD.BUY_QTY) BUY_QTY,\n");
            sql.append("                SUM(BD.SALES_QTY) SALES_QTY,\n");
            sql.append("                SUM(BD.BO_QTY) BO_QTY,\n");
            sql.append("                SUM(BD.BO_QTY * BD.BUY_PRICE) BO_AMOUNT,\n");
            sql.append("                SUM(BD.TOSAL_QTY) TOSAL_QTY\n");
            sql.append("           FROM TT_PART_BO_DTL BD, TT_PART_BO_MAIN BM\n");
            sql.append("          WHERE BD.BO_ID = BM.BO_ID\n");
            sql.append("            AND BM.BO_TYPE=1\n");
            if (!"".equals(soCOde)) {
                sql.append("     AND EXISTS (SELECT 1 \n");
                sql.append("                   FROM TT_PART_SO_MAIN sm \n");
                sql.append("                  WHERE BM.SO_ID = SM.SO_ID \n");
                sql.append("                    AND sm.SO_CODE LIKE '%").append(soCOde).append("%') \n");
            }
            if (!"".equals(orderCode)) {
                sql.append("         AND bm.ORDER_CODE LIKE '%").append(orderCode).append("%' \n");
            }
            sql.append("          GROUP BY BM.ORDER_ID, BD.PART_ID) BO\n");
            sql.append("  WHERE PO.ORDER_ID = SO.ORDER_ID(+)\n");
            sql.append("    AND PO.PART_ID = SO.PART_ID(+)\n");
            sql.append("    AND PO.ORDER_ID = BO.ORDER_ID(+)\n");
            sql.append("    AND PO.PART_ID = BO.PART_ID(+)\n");
            sql.append("    HAVING  SUM(NVL(BO.BO_AMOUNT, 0))>0\n");
            sql.append("  GROUP BY PO.PART_ID) BOS\n");
            if ("1".equals(rSelect) || "2".equals(rSelect)) {
                sql.append("  WHERE BS.PART_ID = BOS.PART_ID(+) \n");
            } else {
                sql.append("  WHERE BS.PART_ID = BOS.PART_ID \n");
            }
            if (!"".equals(sortType)) {
                if ("1".equals(sortType)) {
                    sql.append(" ORDER BY BOXS DESC");
                } else if ("2".equals(sortType)) {
                    sql.append(" ORDER BY BO_QTY DESC ");
                } else {
                    sql.append(" ORDER BY BOXS DESC,BO_QTY DESC ");
                }
            }
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;
    }

    public PageResult<Map<String, Object>> queryPartBoCycleList(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String whName = CommonUtils.checkNull(request.getParamValue("WH_NAME"));
            String planer = CommonUtils.checkNull(request.getParamValue("PLANER_NAME"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT BB.PART_TYPE, --配件类型 \n");
            sql.append("        BB.VENDER_NAME, --供应商名称 \n");
            sql.append("        BB.WH_NAME, --库房 \n");
            sql.append("        BB.PART_OLDCODE, --配件编码 \n");
            sql.append("        BB.PART_CNAME, --配件名称 \n");
            sql.append("        BB.PART_CODE, --配件件号 \n");
            sql.append("        BB.UNIT, --单位 \n");
            sql.append("        BB.BOCNT, --BO项数 \n");
            sql.append("        MODQ.MTH_ODDQTY, --近一个月未满足BO数量 \n");
            sql.append("        WODQ.WEK_ODDQTY, --近一周未满足数量 \n");
            sql.append("        WODQ.WEK_BOCNT, --近一周BO项数 \n");
            sql.append("        BB.OR_QTY, --在途数量 \n");
            sql.append("        PO.ORDER_CODE, --采购订单编号 \n");
            sql.append("        PO.CREATE_DATE, --订单编制时间 \n");
            sql.append("        BB.NAME, --计划员 \n");
            sql.append("        '' BO_NOTE --BO说明 \n");
            sql.append("   FROM (SELECT DISTINCT PD.PART_TYPE, \n");
            sql.append("                         (SELECT V.VENDER_NAME \n");
            sql.append("                            FROM TT_PART_BUY_PRICE BP, TT_PART_VENDER_DEFINE V \n");
            sql.append("                           WHERE BP.VENDER_ID = V.VENDER_ID \n");
            sql.append("                             AND BP.PART_ID = BD.PART_ID \n");
            sql.append("                             AND BP.IS_DEFAULT = 10041001 \n");
            sql.append("                             AND ROWNUM = 1) VENDER_NAME, \n");
            sql.append("                         WD.WH_NAME, \n");
            sql.append("                         BD.PART_ID, \n");
            sql.append("                         BD.PART_OLDCODE, \n");
            sql.append("                         BD.PART_CNAME, \n");
            sql.append("                         BD.PART_CODE, \n");
            sql.append("                         BD.UNIT, \n");
            sql.append("                         TU.NAME, \n");
            sql.append("                         NVL((SELECT OPR.ONROAD_QTY \n");
            sql.append("                               FROM VW_PART_OEM_PO_ONROAD OPR \n");
            sql.append("                              WHERE OPR.PART_ID = BD.PART_ID \n");
            sql.append("                                AND OPR.WH_ID = OM.WH_ID), \n");
            sql.append("                             0) OR_QTY, \n");
            sql.append("                         COUNT(BD.BOLINE_ID) BOCNT \n");
            sql.append("           FROM TT_PART_BO_DTL           BD, \n");
            sql.append("                TT_PART_BO_MAIN          BM, \n");
            sql.append("                TT_PART_DLR_ORDER_MAIN   OM, \n");
            sql.append("                TT_PART_DEFINE           PD, \n");
            sql.append("                TT_PART_WAREHOUSE_DEFINE WD, \n");
            sql.append("                TC_USER                  TU \n");
            sql.append("          WHERE BD.BO_ID = BM.BO_ID \n");
            sql.append("            AND BM.ORDER_ID = OM.ORDER_ID \n");
            sql.append("            AND PD.PART_ID = BD.PART_ID \n");
            sql.append("            AND OM.WH_ID = WD.WH_ID \n");
            sql.append("            AND PD.PLANER_ID = TU.USER_ID(+) \n");
            sql.append("            AND OM.SELLER_ID = ").append(Constant.OEM_ACTIVITIES).append(" --所有参数放下面 \n");
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(BD.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(BD.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append("          GROUP BY PART_TYPE, \n");
            sql.append("                   WD.WH_NAME, \n");
            sql.append("                   BD.PART_ID, \n");
            sql.append("                   BD.PART_OLDCODE, \n");
            sql.append("                   BD.PART_CNAME, \n");
            sql.append("                   BD.PART_CODE, \n");
            sql.append("                   BD.UNIT, \n");
            sql.append("                   TU.NAME, \n");
            sql.append("                   OM.WH_ID \n");
            sql.append("          ORDER BY BOCNT DESC) BB, \n");
            sql.append("        (SELECT BD.PART_ID, SUM(BD.BO_ODDQTY) MTH_ODDQTY \n");
            sql.append("           FROM TT_PART_BO_DTL         BD, \n");
            sql.append("                TT_PART_BO_MAIN        BM, \n");
            sql.append("                TT_PART_DLR_ORDER_MAIN OM \n");
            sql.append("          WHERE BD.BO_ID = BM.BO_ID \n");
            sql.append("            AND BM.ORDER_ID = OM.ORDER_ID \n");
            sql.append("            AND OM.SELLER_ID = ").append(Constant.OEM_ACTIVITIES);
            sql.append("            AND TRUNC(BD.CREATE_DATE) BETWEEN TRUNC(SYSDATE - 30) AND \n");
            sql.append("                TRUNC(SYSDATE) \n");
            sql.append("          GROUP BY BD.PART_ID) MODQ, \n");

            sql.append("        (SELECT BD.PART_ID, \n");
            sql.append("                SUM(BD.BO_ODDQTY) WEK_ODDQTY, \n");
            sql.append("                COUNT(BD.BOLINE_ID) WEK_BOCNT \n");
            sql.append("           FROM TT_PART_BO_DTL         BD, \n");
            sql.append("                TT_PART_BO_MAIN        BM, \n");
            sql.append("                TT_PART_DLR_ORDER_MAIN OM \n");
            sql.append("          WHERE BD.BO_ID = BM.BO_ID \n");
            sql.append("            AND BM.ORDER_ID = OM.ORDER_ID \n");
            sql.append("            AND OM.SELLER_ID = ").append(Constant.OEM_ACTIVITIES);
            sql.append("            AND TRUNC(BD.CREATE_DATE) BETWEEN TRUNC(SYSDATE - 7) AND \n");
            sql.append("                TRUNC(SYSDATE) \n");
            sql.append("          GROUP BY BD.PART_ID) WODQ, \n");
            sql.append("        (SELECT za_concat(OD.ORDER_CODE) ORDER_CODE, \n");
            sql.append("                za_concat(to_char(OD.CREATE_DATE, 'yyyy-mm-dd')) CREATE_DATE, \n");
            sql.append("                OD.PART_ID \n");
            sql.append("           FROM VW_PART_OEM_PO_ONROAD_DTL OD \n");
            sql.append("          GROUP BY OD.PART_ID) PO \n");
            sql.append("  WHERE BB.PART_ID = MODQ.PART_ID(+) \n");
            sql.append("    AND MODQ.PART_ID = WODQ.PART_ID(+) \n");
            sql.append("    AND BB.PART_ID = PO.PART_ID(+) \n");

            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(BB.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partType)) {
                sql.append(" AND BB.PART_TYPE=").append(partType);
            }
            if (!"".equals(whName)) {
                sql.append(" AND BB.WH_NAME='").append(whName).append("'");
            }
            if (!"".equals(planer)) {
                sql.append(" AND BB.NAME='").append(planer).append("'");
            }
            sql.append("ORDER BY BB.BOCNT DESC\n");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartBoCycle(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {

            String whName = CommonUtils.checkNull(request.getParamValue("WH_NAME"));
            String planer = CommonUtils.checkNull(request.getParamValue("PLANER_NAME"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT BB.PART_TYPE, --配件类型 \n");
            sql.append("        BB.VENDER_NAME, --供应商名称 \n");
            sql.append("        BB.WH_NAME, --库房 \n");
            sql.append("        BB.PART_OLDCODE, --配件编码 \n");
            sql.append("        BB.PART_CNAME, --配件名称 \n");
            sql.append("        BB.PART_CODE, --配件件号 \n");
            sql.append("        BB.UNIT, --单位 \n");
            sql.append("        BB.BOCNT, --BO项数 \n");
            sql.append("        MODQ.MTH_ODDQTY, --近一个月未满足BO数量 \n");
            sql.append("        WODQ.WEK_ODDQTY, --近一周未满足数量 \n");
            sql.append("        WODQ.WEK_BOCNT, --近一周BO项数 \n");
            sql.append("        BB.OR_QTY, --在途数量 \n");
            sql.append("        PO.ORDER_CODE, --采购订单编号 \n");
            sql.append("        PO.CREATE_DATE, --订单编制时间 \n");
            sql.append("        BB.NAME, --计划员 \n");
            sql.append("        '' BO_NOTE --BO说明 \n");
            sql.append("   FROM (SELECT DISTINCT PD.PART_TYPE, \n");
            sql.append("                         (SELECT V.VENDER_NAME \n");
            sql.append("                            FROM TT_PART_BUY_PRICE BP, TT_PART_VENDER_DEFINE V \n");
            sql.append("                           WHERE BP.VENDER_ID = V.VENDER_ID \n");
            sql.append("                             AND BP.PART_ID = BD.PART_ID \n");
            sql.append("                             AND BP.IS_DEFAULT = 10041001 \n");
            sql.append("                             AND ROWNUM = 1) VENDER_NAME, \n");
            sql.append("                         WD.WH_NAME, \n");
            sql.append("                         BD.PART_ID, \n");
            sql.append("                         BD.PART_OLDCODE, \n");
            sql.append("                         BD.PART_CNAME, \n");
            sql.append("                         BD.PART_CODE, \n");
            sql.append("                         BD.UNIT, \n");
            sql.append("                         TU.NAME, \n");
            sql.append("                         NVL((SELECT OPR.ONROAD_QTY \n");
            sql.append("                               FROM VW_PART_OEM_PO_ONROAD OPR \n");
            sql.append("                              WHERE OPR.PART_ID = BD.PART_ID \n");
            sql.append("                                AND OPR.WH_ID = OM.WH_ID), \n");
            sql.append("                             0) OR_QTY, \n");
            sql.append("                         COUNT(BD.BOLINE_ID) BOCNT \n");
            sql.append("           FROM TT_PART_BO_DTL           BD, \n");
            sql.append("                TT_PART_BO_MAIN          BM, \n");
            sql.append("                TT_PART_DLR_ORDER_MAIN   OM, \n");
            sql.append("                TT_PART_DEFINE           PD, \n");
            sql.append("                TT_PART_WAREHOUSE_DEFINE WD, \n");
            sql.append("                TC_USER                  TU \n");
            sql.append("          WHERE BD.BO_ID = BM.BO_ID \n");
            sql.append("            AND BM.ORDER_ID = OM.ORDER_ID \n");
            sql.append("            AND PD.PART_ID = BD.PART_ID \n");
            sql.append("            AND OM.WH_ID = WD.WH_ID \n");
            sql.append("            AND PD.PLANER_ID = TU.USER_ID(+) \n");
            sql.append("            AND OM.SELLER_ID = ").append(Constant.OEM_ACTIVITIES).append(" --所有参数放下面 \n");
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(BD.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(BD.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append("          GROUP BY PART_TYPE, \n");
            sql.append("                   WD.WH_NAME, \n");
            sql.append("                   BD.PART_ID, \n");
            sql.append("                   BD.PART_OLDCODE, \n");
            sql.append("                   BD.PART_CNAME, \n");
            sql.append("                   BD.PART_CODE, \n");
            sql.append("                   BD.UNIT, \n");
            sql.append("                   TU.NAME, \n");
            sql.append("                   OM.WH_ID \n");
            sql.append("          ORDER BY BOCNT DESC) BB, \n");
            sql.append("        (SELECT BD.PART_ID, SUM(BD.BO_ODDQTY) MTH_ODDQTY \n");
            sql.append("           FROM TT_PART_BO_DTL         BD, \n");
            sql.append("                TT_PART_BO_MAIN        BM, \n");
            sql.append("                TT_PART_DLR_ORDER_MAIN OM \n");
            sql.append("          WHERE BD.BO_ID = BM.BO_ID \n");
            sql.append("            AND BM.ORDER_ID = OM.ORDER_ID \n");
            sql.append("            AND OM.SELLER_ID = ").append(Constant.OEM_ACTIVITIES);
            sql.append("            AND TRUNC(BD.CREATE_DATE) BETWEEN TRUNC(SYSDATE - 30) AND \n");
            sql.append("                TRUNC(SYSDATE) \n");
            sql.append("          GROUP BY BD.PART_ID) MODQ, \n");

            sql.append("        (SELECT BD.PART_ID, \n");
            sql.append("                SUM(BD.BO_ODDQTY) WEK_ODDQTY, \n");
            sql.append("                COUNT(BD.BOLINE_ID) WEK_BOCNT \n");
            sql.append("           FROM TT_PART_BO_DTL         BD, \n");
            sql.append("                TT_PART_BO_MAIN        BM, \n");
            sql.append("                TT_PART_DLR_ORDER_MAIN OM \n");
            sql.append("          WHERE BD.BO_ID = BM.BO_ID \n");
            sql.append("            AND BM.ORDER_ID = OM.ORDER_ID \n");
            sql.append("            AND OM.SELLER_ID = ").append(Constant.OEM_ACTIVITIES);
            sql.append("            AND TRUNC(BD.CREATE_DATE) BETWEEN TRUNC(SYSDATE - 7) AND \n");
            sql.append("                TRUNC(SYSDATE) \n");
            sql.append("          GROUP BY BD.PART_ID) WODQ, \n");
            sql.append("        (SELECT za_concat(OD.ORDER_CODE) ORDER_CODE, \n");
            sql.append("                za_concat(to_char(OD.CREATE_DATE, 'yyyy-mm-dd')) CREATE_DATE, \n");
            sql.append("                OD.PART_ID \n");
            sql.append("           FROM VW_PART_OEM_PO_ONROAD_DTL OD \n");
            sql.append("          GROUP BY OD.PART_ID) PO \n");
            sql.append("  WHERE BB.PART_ID = MODQ.PART_ID(+) \n");
            sql.append("    AND MODQ.PART_ID = WODQ.PART_ID(+) \n");
            sql.append("    AND BB.PART_ID = PO.PART_ID(+) \n");

            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(BB.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partType)) {
                sql.append(" AND BB.PART_TYPE=").append(partType);
            }
            if (!"".equals(whName)) {
                sql.append(" AND BB.WH_NAME='").append(whName).append("'");
            }
            if (!"".equals(planer)) {
                sql.append(" AND BB.NAME='").append(planer).append("'");
            }
            sql.append("ORDER BY BB.BOCNT DESC\n");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public PageResult<Map<String, Object>> queryBoDtlList(
            RequestWrapper request, AclUserBean logonUser, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;

        String partId = CommonUtils.checkNull(request.getParamValue("partId"));//配件id
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String boCode = CommonUtils.checkNull(request.getParamValue("BO_CODE"));//BO单号
        String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//订单单位编码
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
        String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
        String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//开始时间
        String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//结束时间

        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT T2.DEALER_NAME,\n");
            sql.append("       T1.BO_CODE,\n");
            sql.append("       T1.ORDER_CODE,\n");
            sql.append("       T2.SELLER_NAME,\n");
            sql.append("       T3.BUY_QTY,\n");
            sql.append("       T3.PART_OLDCODE,\n");
            sql.append("       T3.SALES_QTY,\n");
            sql.append("       T3.BO_QTY,\n");
            sql.append("       T3.TOSAL_QTY,\n");
            sql.append("       T3.CLOSE_QTY,\n");
            sql.append("       T3.BO_ODDQTY,\n");
            sql.append("       T3.REMARK,\n");
            sql.append("       T3.CREATE_DATE,\n");
            sql.append("       TO_CHAR((T3.BO_QTY * T3.BUY_PRICE), 'FM999,999,990.00') AMOUNT\n");

           /* sql.append("       nvl((SELECT SUM(VW.NORMAL_QTY)\n");
            sql.append("          FROM VW_PART_STOCK VW\n");
            sql.append("         WHERE VW.PART_ID = T3.PART_ID\n");
            sql.append("           AND VW.ORG_ID = T2.SELLER_ID),0) NORMAL_QTY\n");*/

            sql.append("  FROM TT_PART_BO_MAIN        T1,\n");
            sql.append("       TT_PART_DLR_ORDER_MAIN T2,\n");
            sql.append("       TT_PART_SALES_RELATION   R, \n");
            sql.append("       TT_PART_BO_DTL         T3\n");
            sql.append(" WHERE T1.ORDER_ID = T2.ORDER_ID\n");
            sql.append("   AND T2.SELLER_ID = R.PARENTORG_ID \n");
            sql.append("   AND T2.DEALER_ID = R.CHILDORG_ID \n");
            sql.append("   AND T1.BO_ID = T3.BO_ID\n");
            sql.append("   AND T2.SELLER_ID=");

            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }

            if (!"".equals(orderCode)) {
                sql.append(" AND T1.ORDER_CODE LIKE '%")
                        .append(orderCode.trim()).append("%'\n");
            }
            if (!"".equals(boCode)) {
                sql.append(" AND T1.BO_CODE LIKE '%")
                        .append(boCode.trim()).append("%'\n");
            }
            if (!"".equals(dealerCode)) {
                sql.append(" AND T2.DEALER_CODE LIKE '%")
                        .append(dealerCode.trim()).append("%'\n");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND T2.DEALER_NAME LIKE '%")
                        .append(dealerName.trim()).append("%'\n");
            }
            if (!"".equals(sellerName)) {
                sql.append(" AND T2.SELLER_NAME LIKE '%")
                        .append(sellerName.trim()).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            //deta
          /*  sql.append("                    AND (MONTHS_BETWEEN(TRUNC(SYSDATE, 'mm'), \n");
            sql.append("                                        TO_DATE(TO_CHAR(T1.CREATE_DATE, \n");
            sql.append("                                                        'yyyy-mm'), \n");
            sql.append("                                                'yyyy-mm')) <= \n");
            sql.append("                        (R.BO_DAYS + 1)) --BO有效期,下个月起效 \n");*/
            sql.append("                    AND T1.BO_TYPE = 1 \n");
           /* sql.append("                    AND T1.STATE <> ").append(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);*/
            sql.append("                    AND T1.STATUS = 1 \n");
            sql.append("                    AND T2.STATUS = 1 \n");
            /*sql.append("                    AND T3.STATUS = 1 \n");*/ //不需要过滤

            sql.append(" AND T3.PART_ID=").append(partId);
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> queryPartSalesList(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String year = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//年
            String month = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//月
            String season = CommonUtils.checkNull(request.getParamValue("MYSEASON"));//季度

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT SXE.PART_TYPE, \n");
            sql.append("        SXE.SAL_AMOUNT, \n");
            sql.append("        SXE.SALES_QTY, \n");
            sql.append("        SXE.SAL_CB, \n");
            sql.append("        SXE.ML, \n");
            sql.append("        SXE.MLL, \n");
            sql.append("        NVL(BO.BOZXS, 0) BOZXS, --BO总项数 \n");
            sql.append("        XS.SALZXS， --销售总项数 \n");
            sql.append("        TO_CHAR(NVL(BO.BOZXS, 0) / XS.SALZXS * 100, '99.99') || '%' BO_RATE, --BO率 \n");
            sql.append("        NVL(CG.BUY_AMOUNT,'0') AS BUY_AMOUNT, --采购金额不含税 \n");
            sql.append("        NVL(RK.IN_AMOUNT, 0) IN_AMOUNT, ----入库金额(计划价) \n");
            sql.append("        NVL(RK.IN_QTY, 0) BUY_QTY, --入库数量 \n");
            sql.append("        CK.OUT_AMOUNT, \n");
            sql.append("        CK.OUTSTOCK_QTY, \n");
            sql.append("        ZK.STOCK_AMOUNT --在库金额(计划价) \n");
            sql.append("   FROM (SELECT TPD.PART_TYPE, \n");
            sql.append("                NVL(ROUND(SUM(SD.SALE_AMOUNT) / (1 + ").append(Constant.PART_TAX_RATE);
            sql.append("), 2), 0) SAL_AMOUNT, --销售金额(不含税) \n");
            sql.append("                NVL(SUM(SD.OUTSTOCK_QTY), 0) SALES_QTY, --销售数量 \n");
            sql.append("                ROUND(SUM(SD.OUTSTOCK_QTY * \n");
            sql.append("                          ((SELECT TP.BUY_PRICE \n");
            sql.append("                              FROM TT_PART_BUY_PRICE TP \n");
            sql.append("                             WHERE TP.PART_ID = SD.PART_ID \n");
            sql.append("                               AND TP.IS_DEFAULT = 10041001 \n");
            sql.append("                               AND ROWNUM = 1))) / (1 + ").append(Constant.PART_TAX_RATE);
            sql.append("                      ),2) SAL_CB, --销售成本(不含税)， \n");
            sql.append("                NVL(ROUND(SUM(SD.SALE_AMOUNT) / (1 + ").append(Constant.PART_TAX_RATE);
            sql.append("), 2), 0) - \n");
            sql.append("                ROUND(SUM(SD.OUTSTOCK_QTY * \n");
            sql.append("                          ((SELECT TP.BUY_PRICE \n");
            sql.append("                              FROM TT_PART_BUY_PRICE TP \n");
            sql.append("                             WHERE TP.PART_ID = SD.PART_ID \n");
            sql.append("                               AND TP.IS_DEFAULT = 10041001 \n");
            sql.append("                               AND ROWNUM = 1))) / (1 + ").append(Constant.PART_TAX_RATE);
            sql.append("                      ),2) ML, --毛利 \n");
            sql.append("                TO_CHAR((NVL(ROUND(SUM(SD.SALE_AMOUNT) / (1 + ").append(Constant.PART_TAX_RATE);
            sql.append("), 2), 0) - \n");
            sql.append("                        ROUND(SUM(SD.OUTSTOCK_QTY * \n");
            sql.append("                                   ((SELECT TP.BUY_PRICE \n");
            sql.append("                                       FROM TT_PART_BUY_PRICE TP \n");
            sql.append("                                      WHERE TP.PART_ID = SD.PART_ID \n");
            sql.append("                                        AND TP.IS_DEFAULT = 10041001 \n");
            sql.append("                                        AND ROWNUM = 1))) / (1 +  ").append(Constant.PART_TAX_RATE);
            sql.append("                               ),2)) / \n");
            sql.append("                        NVL(ROUND(SUM(SD.SALE_AMOUNT) / (1 +  ").append(Constant.PART_TAX_RATE);
            sql.append("), 2), 1) * 100, \n");
            sql.append("                        '99.99') || '%' MLL --毛利率 \n");
            sql.append("           FROM TT_PART_OUTSTOCK_MAIN SM, \n");
            sql.append("                TT_PART_OUTSTOCK_DTL  SD, \n");
            sql.append("                TT_PART_DEFINE        TPD \n");
            sql.append("          WHERE SM.OUT_ID = SD.OUT_ID \n");
            sql.append("            AND SD.PART_ID = TPD.PART_ID \n");
            sql.append("            AND SM.STATE <> ").append(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);
            sql.append("            AND SM.SELLER_ID = ");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(SM.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("            AND SM.STATUS = 1 \n");
            sql.append("            AND SD.STATUS = 1 \n");
            sql.append("          GROUP BY TPD.PART_TYPE) SXE, \n");
            sql.append("        (SELECT D.PART_TYPE, NVL(COUNT(1), 0) BOZXS --BO总项数  \n");
            sql.append("           FROM TT_PART_BO_MAIN        T1, \n");
            sql.append("                TT_PART_BO_DTL         T2, \n");
            sql.append("                TT_PART_DEFINE         D, \n");
            sql.append("                TT_PART_DLR_ORDER_MAIN T4 \n");
           /* sql.append("                TT_PART_SALES_RELATION R \n");*/
            sql.append("          WHERE T1.BO_ID = T2.BO_ID \n");
            sql.append("            AND T2.PART_ID = D.PART_ID \n");
            sql.append("            AND T1.ORDER_ID = T4.ORDER_ID \n");
            sql.append("            AND T1.bo_type = 1 \n");
          /*  sql.append("            AND T4.SELLER_ID = R.PARENTORG_ID \n");
            sql.append("            AND T4.DEALER_ID = R.CHILDORG_ID \n");*/
            sql.append("            AND T4.SELLER_ID = ");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(T1.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(T1.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(T1.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
           /* sql.append("            AND (MONTHS_BETWEEN(TRUNC(SYSDATE, 'mm'), \n");
            sql.append("                                TO_DATE(TO_CHAR(T1.CREATE_DATE, 'yyyy-mm'), \n");
            sql.append("                                        'yyyy-mm')) <= (R.BO_DAYS + 1)) \n");*/
            /*sql.append("            AND T1.STATE <> ").append(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);*/
           /* sql.append("            AND T2.STATUS = 1 \n");
            sql.append("            AND T1.STATUS = 1 \n");
            sql.append("            AND T4.STATUS = 1 \n");*/
           /* sql.append("            AND T2.BO_ODDQTY > 0 \n");*/
            sql.append("          GROUP BY D.PART_TYPE) BO, \n");
          /*  sql.append("        (SELECT TPD.PART_TYPE, COUNT(1) SALZXS --销售总项数  \n");
            sql.append("           FROM TT_PART_SO_MAIN SM, TT_PART_SO_DTL SD, TT_PART_DEFINE TPD \n");
            sql.append("          WHERE SM.SO_ID = SD.SO_ID \n");
            sql.append("            AND SD.PART_ID = TPD.PART_ID \n");
            sql.append("AND SM.ORDER_ID IS NOT NULL\n");
            sql.append("            AND SM.STATE <> ").append(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);
            sql.append("            AND SM.SELLER_ID =");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(SM.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("            AND SM.STATUS = 1 \n");
            sql.append("            AND SD.STATUS = 1 \n");
            sql.append("          GROUP BY PART_TYPE) XS, \n");*/

            sql.append("(SELECT A.PART_TYPE, SUM(SALZXS) SALZXS\n");
            sql.append("          FROM (SELECT TPD.PART_TYPE, COUNT(1) SALZXS --销售总项数\n");
            sql.append("                  FROM TT_PART_DLR_ORDER_DTL  SD,\n");
            sql.append("                       TT_PART_DLR_ORDER_MAIN SM,\n");
            sql.append("                       TT_PART_DEFINE         TPD\n");
            sql.append("                 WHERE SD.ORDER_ID = SM.ORDER_ID\n");
            sql.append("                   AND SD.PART_ID = TPD.PART_ID\n");
            sql.append("                   AND SM.SELLER_ID = 2010010100070674\n");
            sql.append("                   AND EXISTS\n");
            sql.append("                 (SELECT 1\n");
            sql.append("                          FROM TT_PART_SO_MAIN BM\n");
            sql.append("                         WHERE BM.ORDER_ID = SM.ORDER_ID\n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(BM.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(BM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(BM.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("                               )\n");
            sql.append("                 GROUP BY PART_TYPE -- 销售单对应订单\n");
            sql.append("                UNION ALL\n");
            sql.append("                SELECT TPD.PART_TYPE, COUNT(1) SALZXS --销售总项数\n");
            sql.append("                  FROM TT_PART_DLR_ORDER_DTL  SD,\n");
            sql.append("                       TT_PART_DLR_ORDER_MAIN SM,\n");
            sql.append("                       TT_PART_DEFINE         TPD\n");
            sql.append("                 WHERE SD.ORDER_ID = SM.ORDER_ID\n");
            sql.append("                   AND SD.PART_ID = TPD.PART_ID\n");
            sql.append("                   AND SM.SELLER_ID = 2010010100070674\n");
            sql.append("                   AND EXISTS\n");
            sql.append("                 (SELECT 1\n");
            sql.append("                          FROM TT_PART_BO_MAIN BM\n");
            sql.append("                         WHERE BM.ORDER_ID = SM.ORDER_ID\n");
            sql.append("                           AND BM.SO_ID IS NULL\n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(BM.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(BM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(BM.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("                               )\n");
            sql.append("                 GROUP BY PART_TYPE --完全BO单对应订单\n");
            sql.append("                ) A\n");
            sql.append("         GROUP BY A.PART_TYPE) XS,\n");

            sql.append("        (SELECT TPD.PART_TYPE, \n");
            sql.append("                ROUND(SUM(NVL(B.IN_QTY * \n");
            sql.append("                        (SELECT P.BUY_PRICE \n");
            sql.append("                           FROM TT_PART_BUY_PRICE P \n");
            sql.append("                          WHERE P.PART_ID = B.PART_ID \n");
            sql.append("                            AND P.VENDER_ID = B.VENDER_ID), \n");
            sql.append("                        0)) / (1 + 0.17),2) BUY_AMOUNT --采购金额(不含税)  \n");
            sql.append("           FROM TT_PART_PO_IN B, TT_PART_DEFINE TPD \n");
            sql.append("          WHERE B.PART_ID = TPD.PART_ID \n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(B.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(B.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(B.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("          GROUP BY TPD.PART_TYPE) CG, \n");
            sql.append("        (SELECT TPD.PART_TYPE, \n");
            sql.append("                ROUND(NVL(SUM(P.IN_QTY * (SELECT PR.SALE_PRICE3 \n");
            sql.append("                                         FROM TT_PART_SALES_PRICE PR \n");
            sql.append("                                        WHERE PR.PART_ID = P.PART_ID \n");
            sql.append("                                          AND PR.STATE = 10011001 \n");
            sql.append("                                          AND PR.STATUS = 1)), \n");
            sql.append("                    0),2) IN_AMOUNT, --入库金额(计划价) \n");
            sql.append("                NVL(SUM(P.IN_QTY), 0) IN_QTY --入库数量 \n");
            sql.append("           FROM TT_PART_PO_IN P, TT_PART_DEFINE TPD \n");
            sql.append("          WHERE P.PART_ID = TPD.PART_ID \n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(P.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(P.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(P.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("          GROUP BY TPD.PART_TYPE) RK, \n");
            sql.append("        (SELECT D.PART_TYPE, \n");
            sql.append("                SUM(DT.OUTSTOCK_QTY) OUTSTOCK_QTY, --出库数量 \n");
            sql.append("                SUM(DT.OUTSTOCK_QTY * \n");
            sql.append("                    (SELECT PR.SALE_PRICE3 \n");
            sql.append("                       FROM TT_PART_SALES_PRICE PR \n");
            sql.append("                      WHERE PR.PART_ID = DT.PART_ID \n");
            sql.append("                        AND PR.STATE = 10011001 \n");
            sql.append("                        AND PR.STATUS = 1)) OUT_AMOUNT --出库金额(计划价)  \n");
            sql.append("           FROM TT_PART_OUTSTOCK_DTL  DT, \n");
            sql.append("                TT_PART_OUTSTOCK_MAIN OM, \n");
            sql.append("                TT_PART_DEFINE        D \n");
            sql.append("          WHERE DT.OUT_ID = OM.OUT_ID \n");
            sql.append("            AND DT.PART_ID = D.PART_ID \n");
            sql.append("            AND OM.SELLER_ID = ");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(DT.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(DT.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(DT.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("          GROUP BY D.PART_TYPE) CK, \n");
            sql.append("        (SELECT D.PART_TYPE, \n");
            sql.append("                NVL(SUM(V.ITEM_QTY * (SELECT PR.SALE_PRICE3 \n");
            sql.append("                                        FROM TT_PART_SALES_PRICE PR \n");
            sql.append("                                       WHERE PR.PART_ID = V.PART_ID \n");
            sql.append("                                         AND PR.STATE = 10011001 \n");
            sql.append("                                         AND PR.STATUS = 1)), \n");
            sql.append("                    0) STOCK_AMOUNT --在库金额(计划价)  \n");
            sql.append("           FROM VW_PART_STOCK V, TT_PART_DEFINE D \n");
            sql.append("          WHERE V.PART_ID = D.PART_ID \n");
            sql.append("            AND V.ORG_ID = ");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            sql.append("          GROUP BY PART_TYPE) ZK \n");
            sql.append("  WHERE SXE.PART_TYPE = BO.PART_TYPE(+) \n");
            sql.append("    AND SXE.PART_TYPE = XS.PART_TYPE(+) \n");
            sql.append("    AND SXE.PART_TYPE = CG.PART_TYPE(+) \n");
            sql.append("    AND SXE.PART_TYPE = RK.PART_TYPE(+) \n");
            sql.append("    AND SXE.PART_TYPE = CK.PART_TYPE(+) \n");
            sql.append("    AND SXE.PART_TYPE = ZK.PART_TYPE(+) \n");

            sql.append("UNION ALL\n");
            sql.append("SELECT 92021004,\n");
            sql.append("       ROUND(nvl(SUM(SM.FREIGHT),0) / 1.17, 2),\n");
            sql.append("       0,\n");
            sql.append("       0,\n");
            sql.append("       0,\n");
            sql.append("       '0',\n");
            sql.append("       0,\n");
            sql.append("       0,\n");
            sql.append("       '0',\n");
            sql.append("       0,\n");
            sql.append("       0,\n");
            sql.append("       0,\n");
            sql.append("       0,\n");
            sql.append("       0,\n");
            sql.append("       0\n");
            sql.append("  FROM TT_PART_OUTSTOCK_MAIN SM\n");
            sql.append(" WHERE SM.SELLER_ID = 2010010100070674\n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(SM.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    //zhumingwei add 2013-11-27 销售退货报表(本部)
    public PageResult<Map<String, Object>> queryPartSalesReturnList(RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        ActionContext act = ActionContext.getContext();
        //AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnCode = CommonUtils.checkNull(request.getParamValue("returnCode"));//
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//
            String balBeginTime = CommonUtils.checkNull(request.getParamValue("balBeginTime"));//
            String balEndTime = CommonUtils.checkNull(request.getParamValue("balEndTime"));//
            String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//
            String PART_CNAME = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//
            String PART_CODE = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT M.RETURN_CODE,--退货单号\n");
            sql.append("       M.DEALER_CODE,--服务商代码\n");
            sql.append("       M.DEALER_NAME,--服务商\n");
            sql.append("       M.Seller_Code,\n");
            sql.append("       m.seller_name,\n");
            sql.append("       D.PART_OLDCODE,--配件编码\n");
            sql.append("       D.PART_CNAME,--配件名称\n");
            sql.append("       D.PART_CODE,--配件件号\n");
            sql.append("       D.UNIT,--单位\n");
            sql.append("       TC.CODE_DESC PART_TYPE,--配件类型\n");
            sql.append("       D.RETURN_QTY,--退货数量\n");
            sql.append("       D.BUY_PRICE,--单价\n");
            sql.append("       D.BUY_AMOUNT,--退货金额\n");
            sql.append("       M.REMARK,--退货原因\n");
            sql.append("       M.CREATE_DATE--退货日期\n");
            sql.append("  FROM TT_PART_DLR_RETURN_DTL  D,\n");
            sql.append("       TT_PART_DLR_RETURN_MAIN M,\n");
            sql.append("       TT_PART_DEFINE          TPD,\n");
            sql.append("       TC_CODE                 TC\n");
            sql.append(" WHERE D.RETURN_ID = M.RETURN_ID\n");
            sql.append("   AND D.PART_ID = TPD.PART_ID\n");
            sql.append("   AND TPD.PART_TYPE = TC.CODE_ID\n");
            sql.append("   AND M.SELLER_ID = 2010010100070674\n");
            sql.append("   AND M.STATE = 92361006\n");

            if (!"".equals(returnCode)) {
                sql.append(" AND M.RETURN_CODE like '%" + returnCode + "%'");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND M.DEALER_NAME like '%" + dealerName + "%'");
            }
            if (!"".equals(balBeginTime)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)>=").append("TO_DATE('").append(balBeginTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balEndTime)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)<=").append("TO_DATE('").append(balEndTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" AND D.PART_OLDCODE like '%" + PART_OLDCODE + "%'");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" AND D.PART_CNAME like '%" + PART_CNAME + "%'");
            }
            if (!"".equals(PART_CODE)) {
                sql.append(" AND D.PART_CODE like '%" + PART_CODE + "%'");
            }

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartSales(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String year = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//年
            String month = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//月
            String season = CommonUtils.checkNull(request.getParamValue("MYSEASON"));//季度

            StringBuffer sql = new StringBuffer("");

            sql.append("    SELECT    DECODE(SXE.PART_TYPE, \n");
            sql.append(Constant.PART_BASE_PART_TYPES_SELF_MADE).append(",\n");
            sql.append("               '自制件', \n");
            sql.append(Constant.PART_BASE_PART_TYPES_PURCHASE).append(",\n");
            sql.append("               '国产件', \n");
            sql.append(Constant.PART_BASE_PART_TYPES_ENTRANCE).append(",\n");
            sql.append("               '进口件') PART_TYPE, \n");
            sql.append("        nvl(SXE.SAL_AMOUNT,0) SAL_AMOUNT, \n");
            sql.append("        nvl(SXE.SALES_QTY,0) SALES_QTY, \n");
            sql.append("        nvl(SXE.SAL_CB,0) SAL_CB, \n");
            sql.append("        nvl(SXE.ML,0) ML, \n");
            sql.append("        nvl(SXE.MLL,0) MLL, \n");
            sql.append("        NVL(BO.BOZXS, 0) BOZXS, --BO总项数 \n");
            sql.append("        nvl(XS.SALZXS，0) SALZXS, --销售总项数 \n");
            sql.append("        TO_CHAR(NVL(BO.BOZXS, 0) / XS.SALZXS * 100, '99.99') || '%' BO_RATE, --BO率 \n");
            sql.append("        nvl(CG.BUY_AMOUNT,0) BUY_AMOUNT, --采购金额不含税 \n");
            sql.append("        NVL(RK.IN_AMOUNT, 0) IN_AMOUNT, ----入库金额(计划价) \n");
            sql.append("        NVL(RK.IN_QTY, 0) BUY_QTY, --入库数量 \n");
            sql.append("        nvl(CK.OUT_AMOUNT,0) OUT_AMOUNT, \n");
            sql.append("        nvl(CK.OUTSTOCK_QTY,0) OUTSTOCK_QTY, \n");
            sql.append("        nvl(ZK.STOCK_AMOUNT,0) STOCK_AMOUNT --在库金额(计划价) \n");
            sql.append("   FROM (SELECT TPD.PART_TYPE, \n");
            sql.append("                NVL(ROUND(SUM(SD.SALE_AMOUNT) / (1 + ").append(Constant.PART_TAX_RATE);
            sql.append("), 2), 0) SAL_AMOUNT, --销售金额(不含税) \n");
            sql.append("                NVL(SUM(SD.OUTSTOCK_QTY), 0) SALES_QTY, --销售数量 \n");
            sql.append("                ROUND(SUM(SD.OUTSTOCK_QTY * \n");
            sql.append("                          ((SELECT TP.BUY_PRICE \n");
            sql.append("                              FROM TT_PART_BUY_PRICE TP \n");
            sql.append("                             WHERE TP.PART_ID = SD.PART_ID \n");
            sql.append("                               AND TP.IS_DEFAULT = 10041001 \n");
            sql.append("                               AND ROWNUM = 1))) / (1 + ").append(Constant.PART_TAX_RATE);
            sql.append("                      ),2) SAL_CB, --销售成本(不含税)， \n");
            sql.append("                NVL(ROUND(SUM(SD.SALE_AMOUNT) / (1 + ").append(Constant.PART_TAX_RATE);
            sql.append("), 2), 0) - \n");
            sql.append("                ROUND(SUM(SD.OUTSTOCK_QTY * \n");
            sql.append("                          ((SELECT TP.BUY_PRICE \n");
            sql.append("                              FROM TT_PART_BUY_PRICE TP \n");
            sql.append("                             WHERE TP.PART_ID = SD.PART_ID \n");
            sql.append("                               AND TP.IS_DEFAULT = 10041001 \n");
            sql.append("                               AND ROWNUM = 1))) / (1 + ").append(Constant.PART_TAX_RATE);
            sql.append("                      ),2) ML, --毛利 \n");
            sql.append("                TO_CHAR((NVL(ROUND(SUM(SD.SALE_AMOUNT) / (1 + ").append(Constant.PART_TAX_RATE);
            sql.append("), 2), 0) - \n");
            sql.append("                        ROUND(SUM(SD.OUTSTOCK_QTY * \n");
            sql.append("                                   ((SELECT TP.BUY_PRICE \n");
            sql.append("                                       FROM TT_PART_BUY_PRICE TP \n");
            sql.append("                                      WHERE TP.PART_ID = SD.PART_ID \n");
            sql.append("                                        AND TP.IS_DEFAULT = 10041001 \n");
            sql.append("                                        AND ROWNUM = 1))) / (1 +  ").append(Constant.PART_TAX_RATE);
            sql.append("                               ),2)) / \n");
            sql.append("                        NVL(ROUND(SUM(SD.SALE_AMOUNT) / (1 +  ").append(Constant.PART_TAX_RATE);
            sql.append("), 2), 1) * 100, \n");
            sql.append("                        '99.99') || '%' MLL --毛利率 \n");
            sql.append("           FROM TT_PART_OUTSTOCK_MAIN SM, \n");
            sql.append("                TT_PART_OUTSTOCK_DTL  SD, \n");
            sql.append("                TT_PART_DEFINE        TPD \n");
            sql.append("          WHERE SM.OUT_ID = SD.OUT_ID \n");
            sql.append("            AND SD.PART_ID = TPD.PART_ID \n");
            sql.append("            AND SM.STATE <> ").append(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);
            sql.append("            AND SM.SELLER_ID = ");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(SM.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("            AND SM.STATUS = 1 \n");
            sql.append("            AND SD.STATUS = 1 \n");
            sql.append("          GROUP BY TPD.PART_TYPE) SXE, \n");

            sql.append("(SELECT D.PART_TYPE, NVL(COUNT(1), 0) BOZXS --BO总项数\n");
            sql.append("         FROM TT_PART_BO_MAIN        T1,\n");
            sql.append("              TT_PART_BO_DTL         T2,\n");
            sql.append("              TT_PART_DEFINE         D,\n");
            sql.append("              TT_PART_DLR_ORDER_MAIN T4\n");
            sql.append("        WHERE T1.BO_ID = T2.BO_ID\n");
            sql.append("          AND T2.PART_ID = D.PART_ID\n");
            sql.append("          AND T1.ORDER_ID = T4.ORDER_ID\n");
            sql.append("          AND T1.BO_TYPE = 1\n");
            sql.append("          AND T4.SELLER_ID = 2010010100070674\n");

            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(T1.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(T1.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(T1.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
               /* sql.append("            AND (MONTHS_BETWEEN(TRUNC(SYSDATE, 'mm'), \n");
                sql.append("                                TO_DATE(TO_CHAR(T1.CREATE_DATE, 'yyyy-mm'), \n");
	            sql.append("                                        'yyyy-mm')) <= (R.BO_DAYS + 1)) \n");
	            sql.append("            AND T1.STATE <> ").append(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);*/
         /*   sql.append("            AND T2.STATUS = 1 \n");
            sql.append("            AND T1.STATUS = 1 \n");
            sql.append("            AND T4.STATUS = 1 \n");*/
                /*sql.append("            AND T2.BO_ODDQTY > 0 \n");*/
            sql.append("          GROUP BY D.PART_TYPE) BO, \n");
           /* sql.append("        (SELECT TPD.PART_TYPE, COUNT(1) SALZXS --销售总项数  \n");
            sql.append("           FROM TT_PART_SO_MAIN SM, TT_PART_SO_DTL SD, TT_PART_DEFINE TPD \n");
            sql.append("          WHERE SM.SO_ID = SD.SO_ID \n");
            sql.append("            AND SD.PART_ID = TPD.PART_ID \n");
            sql.append("            AND SM.STATE <> ").append(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);
            sql.append("            AND SM.SELLER_ID =");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(SM.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("            AND SM.STATUS = 1 \n");
            sql.append("            AND SD.STATUS = 1 \n");
            sql.append("          GROUP BY PART_TYPE) XS, \n");*/
            sql.append("(SELECT A.PART_TYPE, SUM(SALZXS) SALZXS\n");
            sql.append("          FROM (SELECT TPD.PART_TYPE, COUNT(1) SALZXS --销售总项数\n");
            sql.append("                  FROM TT_PART_DLR_ORDER_DTL  SD,\n");
            sql.append("                       TT_PART_DLR_ORDER_MAIN SM,\n");
            sql.append("                       TT_PART_DEFINE         TPD\n");
            sql.append("                 WHERE SD.ORDER_ID = SM.ORDER_ID\n");
            sql.append("                   AND SD.PART_ID = TPD.PART_ID\n");
            sql.append("                   AND SM.SELLER_ID = 2010010100070674\n");
            sql.append("                   AND EXISTS\n");
            sql.append("                 (SELECT 1\n");
            sql.append("                          FROM TT_PART_SO_MAIN BM\n");
            sql.append("                         WHERE BM.ORDER_ID = SM.ORDER_ID\n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(BM.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(BM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(BM.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("                               )\n");
            sql.append("                 GROUP BY PART_TYPE -- 销售单对应订单\n");
            sql.append("                UNION ALL\n");
            sql.append("                SELECT TPD.PART_TYPE, COUNT(1) SALZXS --销售总项数\n");
            sql.append("                  FROM TT_PART_DLR_ORDER_DTL  SD,\n");
            sql.append("                       TT_PART_DLR_ORDER_MAIN SM,\n");
            sql.append("                       TT_PART_DEFINE         TPD\n");
            sql.append("                 WHERE SD.ORDER_ID = SM.ORDER_ID\n");
            sql.append("                   AND SD.PART_ID = TPD.PART_ID\n");
            sql.append("                   AND SM.SELLER_ID = 2010010100070674\n");
            sql.append("                   AND EXISTS\n");
            sql.append("                 (SELECT 1\n");
            sql.append("                          FROM TT_PART_BO_MAIN BM\n");
            sql.append("                         WHERE BM.ORDER_ID = SM.ORDER_ID\n");
            sql.append("                           AND BM.SO_ID IS NULL\n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(BM.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(BM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(BM.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("                               )\n");
            sql.append("                 GROUP BY PART_TYPE --完全BO单对应订单\n");
            sql.append("                ) A\n");
            sql.append("         GROUP BY A.PART_TYPE) XS,\n");
            sql.append("        (SELECT TPD.PART_TYPE, \n");
            sql.append("                ROUND(SUM(NVL(B.IN_QTY * \n");
            sql.append("                        (SELECT P.BUY_PRICE \n");
            sql.append("                           FROM TT_PART_BUY_PRICE P \n");
            sql.append("                          WHERE P.PART_ID = B.PART_ID \n");
            sql.append("                            AND P.VENDER_ID = B.VENDER_ID), \n");
            sql.append("                        0)),2) / (1 + 0.17) BUY_AMOUNT --采购金额(不含税)  \n");
            sql.append("           FROM TT_PART_PO_IN B, TT_PART_DEFINE TPD \n");
            sql.append("          WHERE B.PART_ID = TPD.PART_ID \n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(B.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(B.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(B.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("          GROUP BY TPD.PART_TYPE) CG, \n");
            sql.append("        (SELECT TPD.PART_TYPE, \n");
            sql.append("                ROUND(NVL(SUM(P.IN_AMOUNT * (SELECT PR.SALE_PRICE3 \n");
            sql.append("                                         FROM TT_PART_SALES_PRICE PR \n");
            sql.append("                                        WHERE PR.PART_ID = P.PART_ID \n");
            sql.append("                                          AND PR.STATE = 10011001 \n");
            sql.append("                                          AND PR.STATUS = 1)), \n");
            sql.append("                    0),2) IN_AMOUNT, --入库金额(计划价) \n");
            sql.append("                NVL(SUM(P.IN_QTY), 0) IN_QTY --入库数量 \n");
            sql.append("           FROM TT_PART_PO_IN P, TT_PART_DEFINE TPD \n");
            sql.append("          WHERE P.PART_ID = TPD.PART_ID \n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(P.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(P.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(P.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("          GROUP BY TPD.PART_TYPE) RK, \n");
            sql.append("        (SELECT D.PART_TYPE, \n");
            sql.append("                SUM(DT.OUTSTOCK_QTY) OUTSTOCK_QTY, --出库数量 \n");
            sql.append("                SUM(DT.OUTSTOCK_QTY * \n");
            sql.append("                    (SELECT PR.SALE_PRICE3 \n");
            sql.append("                       FROM TT_PART_SALES_PRICE PR \n");
            sql.append("                      WHERE PR.PART_ID = DT.PART_ID \n");
            sql.append("                        AND PR.STATE = 10011001 \n");
            sql.append("                        AND PR.STATUS = 1)) OUT_AMOUNT --出库金额(计划价)  \n");
            sql.append("           FROM TT_PART_OUTSTOCK_DTL  DT, \n");
            sql.append("                TT_PART_OUTSTOCK_MAIN OM, \n");
            sql.append("                TT_PART_DEFINE        D \n");
            sql.append("          WHERE DT.OUT_ID = OM.OUT_ID \n");
            sql.append("            AND DT.PART_ID = D.PART_ID \n");
            sql.append("            AND OM.SELLER_ID = ");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(DT.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(DT.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(DT.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("          GROUP BY D.PART_TYPE) CK, \n");
            sql.append("        (SELECT D.PART_TYPE, \n");
            sql.append("                NVL(SUM(V.ITEM_QTY * (SELECT PR.SALE_PRICE3 \n");
            sql.append("                                        FROM TT_PART_SALES_PRICE PR \n");
            sql.append("                                       WHERE PR.PART_ID = V.PART_ID \n");
            sql.append("                                         AND PR.STATE = 10011001 \n");
            sql.append("                                         AND PR.STATUS = 1)), \n");
            sql.append("                    0) STOCK_AMOUNT --在库金额(计划价)  \n");
            sql.append("           FROM VW_PART_STOCK V, TT_PART_DEFINE D \n");
            sql.append("          WHERE V.PART_ID = D.PART_ID \n");
            sql.append("            AND V.ORG_ID = ");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            sql.append("          GROUP BY PART_TYPE) ZK \n");
            sql.append("  WHERE SXE.PART_TYPE = BO.PART_TYPE(+) \n");
            sql.append("    AND SXE.PART_TYPE = XS.PART_TYPE(+) \n");
            sql.append("    AND SXE.PART_TYPE = CG.PART_TYPE(+) \n");
            sql.append("    AND SXE.PART_TYPE = RK.PART_TYPE(+) \n");
            sql.append("    AND SXE.PART_TYPE = CK.PART_TYPE(+) \n");
            sql.append("    AND SXE.PART_TYPE = ZK.PART_TYPE(+) \n");

            sql.append("UNION ALL\n");
            sql.append("SELECT '运费',\n");
            sql.append("       ROUND(SUM(SM.FREIGHT) / 1.17, 2),\n");
            sql.append("       0,\n");
            sql.append("       0,\n");
            sql.append("       0,\n");
            sql.append("       '0',\n");
            sql.append("       0,\n");
            sql.append("       0,\n");
            sql.append("       '0',\n");
            sql.append("       0,\n");
            sql.append("       0,\n");
            sql.append("       0,\n");
            sql.append("       0,\n");
            sql.append("       0,\n");
            sql.append("       0\n");
            sql.append("  FROM TT_PART_OUTSTOCK_MAIN SM\n");
            sql.append(" WHERE SM.SELLER_ID = 2010010100070674\n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(SM.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    //zhumingwei add 2013-11-27 销售退货报表(本部)
    public List<Map<String, Object>> queryPartSalesReturn(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        ActionContext act = ActionContext.getContext();
        //AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnCode = CommonUtils.checkNull(request.getParamValue("returnCode"));//
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//
            String balBeginTime = CommonUtils.checkNull(request.getParamValue("balBeginTime"));//
            String balEndTime = CommonUtils.checkNull(request.getParamValue("balEndTime"));//
            String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//
            String PART_CNAME = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//
            String PART_CODE = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT M.RETURN_CODE,--退货单号\n");
            sql.append("       M.DEALER_CODE,--服务商代码\n");
            sql.append("       M.DEALER_NAME,--服务商\n");
            sql.append("       M.Seller_Code,\n");
            sql.append("       m.seller_name,\n");
            sql.append("       D.PART_OLDCODE,--配件编码\n");
            sql.append("       D.PART_CNAME,--配件名称\n");
            sql.append("       D.PART_CODE,--配件件号\n");
            sql.append("       D.UNIT,--单位\n");
            sql.append("       TC.CODE_DESC PART_TYPE,--配件类型\n");
            sql.append("       D.RETURN_QTY,--退货数量\n");
            sql.append("       D.BUY_PRICE,--单价\n");
            sql.append("       D.BUY_AMOUNT,--退货金额\n");
            sql.append("       M.REMARK,--退货原因\n");
            sql.append("       M.CREATE_DATE--退货日期\n");
            sql.append("  FROM TT_PART_DLR_RETURN_DTL  D,\n");
            sql.append("       TT_PART_DLR_RETURN_MAIN M,\n");
            sql.append("       TT_PART_DEFINE          TPD,\n");
            sql.append("       TC_CODE                 TC\n");
            sql.append(" WHERE D.RETURN_ID = M.RETURN_ID\n");
            sql.append("   AND D.PART_ID = TPD.PART_ID\n");
            sql.append("   AND TPD.PART_TYPE = TC.CODE_ID\n");
            sql.append("   AND M.SELLER_ID = 2010010100070674\n");
            sql.append("   AND M.STATE = 92361006\n");

            if (!"".equals(returnCode)) {
                sql.append(" AND M.RETURN_CODE like '%" + returnCode + "%'");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND M.DEALER_NAME like '%" + dealerName + "%'");
            }
            if (!"".equals(balBeginTime)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)>=").append("TO_DATE('").append(balBeginTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balEndTime)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)<=").append("TO_DATE('").append(balEndTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" AND D.PART_OLDCODE like '%" + PART_OLDCODE + "%'");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" AND D.PART_CNAME like '%" + PART_CNAME + "%'");
            }
            if (!"".equals(PART_CODE)) {
                sql.append(" AND D.PART_CODE like '%" + PART_CODE + "%'");
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public Map<String, Object> queryRepSum(RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        Map<String, Object> ps;
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));
        String soCOde = CommonUtils.checkNull(request.getParamValue("SO_CODE"));
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
        String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
        String region = CommonUtils.checkNull(request.getParamValue("REGION"));
        String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
        String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));
        String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
        String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
        String rSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));
        String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
        String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
        String sortType = CommonUtils.checkNull(request.getParamValue("sortType"));//排序方式
        String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员
        String state = CommonUtils.checkNull(request.getParamValue("STATE"));//配件是否有效

        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT NVL(SUM(BOS.BOXS), 0) BOZXS,\n");
        sql.append("       NVL(SUM(BOS.XSXS), 0) SALZXS,\n");
        sql.append("       TO_CHAR(SUM(NVL(BOS.BOXS, 0)) /\n");
        sql.append("               (DECODE(NVL(SUM(BOS.XSXS), 0), 0, 1, NVL(SUM(BOS.XSXS), 0))) * 100,\n");
        sql.append("               '999.99') || '%' BOLV\n");
        sql.append("   FROM (SELECT TPD.PART_ID, \n");
        sql.append("                (SELECT NVL(V.NORMAL_QTY, 0) \n");
        sql.append("                   FROM VW_PART_STOCK V \n");
        sql.append("                  WHERE V.PART_ID = TPD.PART_ID \n");
        sql.append("                    AND V.WH_ID = ").append(whId).append(") NORMAL_QTY, \n");
        sql.append("                (SELECT VD.VENDER_NAME \n");
        sql.append("                   FROM TT_PART_BUY_PRICE BP, TT_PART_VENDER_DEFINE VD \n");
        sql.append("                  WHERE BP.VENDER_ID = VD.VENDER_ID \n");
        sql.append("                    AND BP.IS_DEFAULT = ").append(Constant.IF_TYPE_YES);
        sql.append("                    AND BP.PART_ID = TPD.PART_ID \n");
        sql.append("                    AND BP.STATE = ").append(Constant.STATUS_ENABLE).append(") VENDER_NAME, --默认供应商 \n");
        sql.append("                TPD.PART_TYPE, --配件类型 \n");
        sql.append("                TPD.PART_OLDCODE, --配件编码 \n");
        sql.append("                TPD.PART_CNAME, --配件名称 \n");
        sql.append("                TPD.PART_CODE, --配件件号 \n");
        sql.append("                TPD.STATE, --是否有效 \n");
        sql.append("                (SELECT PD.AVG_QTY \n");
        sql.append("                   FROM TT_PART_PLAN_DEFINE PD \n");
        sql.append("                  WHERE PD.PART_ID = TPD.PART_ID \n");
        sql.append("                    AND PD.PLAN_TYPE = 1 AND ROWNUM = 1) * 30 AVG_QTY, --平均月销量 \n");
        sql.append("                (SELECT PD.SAFETY_STOCK \n");
        sql.append("                   FROM TT_PART_PLAN_DEFINE PD \n");
        sql.append("                  WHERE PD.PART_ID = TPD.PART_ID \n");
        sql.append("                    AND PD.PLAN_TYPE = 1 AND ROWNUM = 1) * 1 SAFETY_STOCK, --安全库存 \n");
        sql.append("                NVL(ZT.ZT_QTY, 0) ZT_QTY, --在途数量 \n");
        sql.append("                NVL(DRK.SPAREIN_QTY, 0) SPAREIN_QTY, --待入库数量, \n");
        sql.append("                TU.NAME --计划员 \n");
        sql.append("           FROM TT_PART_DEFINE TPD, \n");
        sql.append("                (SELECT D.PART_ID, M.WH_ID, \n");
        sql.append("CASE\n");
        sql.append("                       WHEN SUM(D.SPARE_QTY) > 0 THEN\n");
        sql.append("                        SUM(D.SPARE_QTY)\n");
        sql.append("                       WHEN SUM(D.SPARE_QTY) < 0 THEN\n");
        sql.append("                        0\n");
        sql.append("                     END ZT_QTY\n");
        sql.append("                   FROM TT_PART_PO_MAIN M, TT_PART_PO_DTL D --有效订单未分配数量 \n");
        sql.append("                  WHERE D.ORDER_ID = M.ORDER_ID \n");
        sql.append("                    AND M.STATE = ").append(Constant.PURCHASE_ORDER_STATE_01);
        sql.append("                    AND (TRUNC(SYSDATE) - TRUNC(D.CREATE_DATE)) <= \n");
        sql.append("                        (SELECT D.DAYS \n");
        sql.append("                           FROM TT_PART_PERIOD_DEFINE D \n");
        sql.append("                          WHERE D.STATE = ").append(Constant.STATUS_ENABLE);
        sql.append("                            AND D.STATUS = 1) \n");
        sql.append("                  GROUP BY D.PART_ID, M.WH_ID) ZT, \n");
        sql.append("                (SELECT PO.PART_ID, \n");
        sql.append("                        PO.WH_ID, \n");
        sql.append("                        SUM(PO.GENERATE_QTY - PO.IN_QTY) SPAREIN_QTY \n");
        sql.append("                   FROM TT_PART_OEM_PO PO --验收中未入库数量 \n");
        sql.append("                  WHERE PO.STATE <> ").append(Constant.PART_PURCHASE_ORDERCHK_STATUS_02).append("--非关闭 \n");
        sql.append("                    AND PO.STATUS = 1 HAVING \n");
        sql.append("                  SUM(PO.GENERATE_QTY - PO.IN_QTY) > 0 \n");
        sql.append("                  GROUP BY PO.PART_ID, PO.WH_ID) DRK, \n");
        sql.append("                TC_USER TU \n");
        sql.append("          WHERE TPD.PART_ID = ZT.PART_ID(+) \n");
        sql.append("            AND TPD.PART_ID = DRK.PART_ID(+) \n");
        sql.append("            AND TPD.PLANER_ID = TU.USER_ID(+) \n");
        if (!"".equals(planerId) && null != planerId) {
            sql.append("AND TU.USER_ID =").append(planerId);
        }
        if (!"".equals(venderId)) {
            sql.append(" AND (SELECT BP.VENDER_ID \n");
            sql.append("                   FROM TT_PART_BUY_PRICE BP \n");
            sql.append("                  WHERE BP.PART_ID = TPD.PART_ID \n");
            sql.append("                    AND BP.IS_DEFAULT = ").append(Constant.IF_TYPE_YES);
            sql.append("                    AND BP.STATE = ").append(Constant.STATUS_ENABLE).append(")=").append(venderId);
        }
/*        if ("1".equals(rSelect)) {
            sql.append("         AND (SELECT NVL(V.NORMAL_QTY, 0) \n");
            sql.append("            FROM VW_PART_STOCK V \n");
            sql.append("           WHERE V.PART_ID = TPD.PART_ID \n");
            sql.append("             AND V.WH_ID = ").append(whId).append(")< \n");
            sql.append("         ((SELECT PD.AVG_QTY \n");
            sql.append("             FROM TT_PART_PLAN_DEFINE PD \n");
            sql.append("            WHERE PD.PART_ID = TPD.PART_ID \n");
            sql.append("              AND PD.PLAN_TYPE = 1 AND ROWNUM = 1) * 30) \n");
        }
        if ("2".equals(rSelect)) {
            sql.append("  AND (SELECT NVL(V.NORMAL_QTY, 0) \n");
            sql.append("            FROM VW_PART_STOCK V \n");
            sql.append("           WHERE V.PART_ID = TPD.PART_ID \n");
            sql.append("             AND V.WH_ID = ").append(whId).append(")< \n");
            sql.append("         ((SELECT PD.SAFETY_STOCK \n");
            sql.append("                   FROM TT_PART_PLAN_DEFINE PD \n");
            sql.append("                  WHERE PD.PART_ID = TPD.PART_ID \n");
            sql.append("                    AND PD.PLAN_TYPE = 1 AND ROWNUM = 1) * 30) \n");
        }*/
        if (!"".equals(partOldCode)) {
            sql.append(" AND UPPER(TPD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
        }
        if (!"".equals(partCode)) {
            sql.append(" AND UPPER(TPD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
        }
        if (!"".equals(partType)) {
            sql.append(" AND TPD.PART_TYPE=").append(partType);
        }
        if (!"".equals(state)) {
            sql.append(" AND TPD.STATE=").append(state);
        }
        sql.append("         ) BS, \n");
        sql.append("(SELECT PO.PART_ID,\n");
        sql.append("        SUM(PO.BUY_AMOUNT) BUY_AMOUNT, --订货金额\n");
        sql.append("        SUM(PO.BUY_QTY) BUY_QTY, --订货数量\n");
        sql.append("        SUM(NVL(SO.SALES_QTY, 0)) SALES_QTY, --已交付数量\n");
        sql.append("        SUM((CASE\n");
        sql.append("              WHEN SO.SALES_QTY IS NULL THEN\n");
        sql.append("               0\n");
        sql.append("              WHEN SO.SALES_QTY IS NOT NULL THEN\n");
        sql.append("               1\n");
        sql.append("            END)) XSXS, --销售总项数\n");
        sql.append("        SUM(NVL(BO.BO_QTY, 0)) BO_QTY, --BO数量\n");
        sql.append("        SUM((CASE\n");
        sql.append("              WHEN BO.BO_QTY IS NULL THEN\n");
        sql.append("               0\n");
        sql.append("              WHEN BO.BO_QTY IS NOT NULL THEN\n");
        sql.append("               1\n");
        sql.append("            END)) BOXS, --BO总项数\n");
        sql.append("        SUM(NVL(BO.BO_AMOUNT, 0)) BO_AMOUNT,\n");
        sql.append("        SUM(NVL(BO.TOSAL_QTY, 0)) TOSAL_QTY --BO满足数量\n");
        sql.append("   FROM (SELECT OM.ORDER_ID,\n");
        sql.append("                OD.PART_ID,\n");
        sql.append("                OD.BUY_PRICE,\n");
        sql.append("                SUM(OD.BUY_AMOUNT) BUY_AMOUNT,\n");
        sql.append("                SUM(OD.BUY_QTY) BUY_QTY\n");
        sql.append("           FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DLR_ORDER_MAIN OM\n");
        sql.append("          WHERE OD.ORDER_ID = OM.ORDER_ID\n");
        sql.append("            AND OM.SELLER_ID = ");
        if (logonUser.getDealerId() == null) {
            sql.append(logonUser.getOrgId());
        } else {
            sql.append(logonUser.getDealerId());
        }
        sql.append("AND EXISTS\n");
        sql.append("                (SELECT 1\n");
        sql.append("                         FROM TT_PART_SO_MAIN M\n");
        sql.append("                        WHERE M.ORDER_ID = OM.ORDER_ID\n");
        if (!"".equals(soCOde)) {
            sql.append("                          AND M.SO_CODE LIKE  '%").append(soCOde).append("%' \n");
        }
        if (!"".equals(orderCode)) {
            sql.append("                          AND M.ORDER_CODE LIKE  '%").append(orderCode).append("%'\n");
        }
        if (!"".equals(startDate)) {
            sql.append(" AND TO_DATE(M.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
        }
        if (!"".equals(endDate)) {
            sql.append(" AND TO_DATE(M.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
        }
        sql.append(")\n");
        sql.append(" GROUP BY OM.ORDER_ID, OD.PART_ID, OD.BUY_PRICE\n");
        sql.append("UNION ALL\n");
        sql.append("SELECT OM.ORDER_ID,\n");
        sql.append("       OD.PART_ID,\n");
        sql.append("       OD.BUY_PRICE,\n");
        sql.append("       SUM(OD.BUY_AMOUNT) BUY_AMOUNT,\n");
        sql.append("       SUM(OD.BUY_QTY) BUY_QTY\n");
        sql.append("  FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DLR_ORDER_MAIN OM\n");
        sql.append(" WHERE OD.ORDER_ID = OM.ORDER_ID\n");
        sql.append("   AND OM.SELLER_ID = 2010010100070674\n");
        sql.append("   AND EXISTS\n");
        sql.append(" (SELECT 1\n");
        sql.append("          FROM TT_PART_BO_MAIN BM\n");
        sql.append("         WHERE BM.ORDER_ID = OM.ORDER_ID\n");
        sql.append("           AND BM.SO_ID IS NULL\n");
        if (!"".equals(orderCode)) {
            sql.append("                          AND BM.ORDER_CODE LIKE  '%").append(orderCode).append("%' \n");
        }
        if (!"".equals(endDate)) {
            sql.append(" AND TO_DATE(BM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
        }
        if (!"".equals(endDate)) {
            sql.append(" AND TO_DATE(BM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
        }
        sql.append(")\n");
        sql.append("          GROUP BY OM.ORDER_ID, OD.PART_ID, OD.BUY_PRICE \n");

        sql.append("UNION ALL\n");
        sql.append("              SELECT SM.ORDER_ID,\n");
        sql.append("                     D.PART_ID,\n");
        sql.append("                     D.BUY_PRICE,\n");
        sql.append("                     SUM(D.BUY_AMOUNT) BUY_AMOUNT,\n");
        sql.append("                     SUM(D.BUY_QTY) BUY_QTY\n");
        sql.append("                FROM TT_PART_SO_DTL D, TT_PART_SO_MAIN SM\n");
        sql.append("               WHERE D.SO_ID = SM.SO_ID\n");
        sql.append("                 AND SM.ORDER_ID IS NOT NULL\n");
        sql.append("                 AND NOT EXISTS (SELECT 1\n");
        sql.append("                        FROM TT_PART_DLR_ORDER_DTL OD\n");
        sql.append("                       WHERE OD.PART_ID = D.PART_ID\n");
        sql.append("                         AND D.ORDER_ID = OD.ORDER_ID)\n");
        sql.append("            AND SM.SELLER_ID = ");
        if (logonUser.getDealerId() == null) {
            sql.append(logonUser.getOrgId());
        } else {
            sql.append(logonUser.getDealerId());
        }
        sql.append("AND EXISTS\n");
        sql.append("                (SELECT 1\n");
        sql.append("                         FROM TT_PART_SO_MAIN M\n");
        sql.append("                        WHERE M.ORDER_ID =  SM.ORDER_ID\n");
        if (!"".equals(soCOde)) {
            sql.append("                          AND M.SO_CODE LIKE  '%").append(soCOde).append("%' \n");
        }
        if (!"".equals(orderCode)) {
            sql.append("                          AND M.ORDER_CODE LIKE  '%").append(orderCode).append("%'\n");
        }
        if (!"".equals(startDate)) {
            sql.append(" AND TO_DATE(M.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
        }
        if (!"".equals(endDate)) {
            sql.append(" AND TO_DATE(M.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
        }
        sql.append(")\n");
        sql.append(" GROUP BY SM.ORDER_ID, D.PART_ID, D.BUY_PRICE\n");
        sql.append("          ) PO,\n");


        sql.append("(SELECT SM.ORDER_ID, SD.PART_ID, SUM(SD.BUY_QTY) SALES_QTY\n");
        sql.append("                 FROM TT_PART_DLR_ORDER_DTL SD, TT_PART_DLR_ORDER_MAIN SM\n");
        sql.append("                WHERE SD.ORDER_ID = SM.ORDER_ID\n");
        sql.append("                  AND SM.SELLER_ID = 2010010100070674\n");
        sql.append("                  AND EXISTS\n");
        sql.append("                (SELECT 1\n");
        sql.append("                         FROM TT_PART_SO_MAIN BM\n");
        sql.append("                        WHERE BM.ORDER_ID = SM.ORDER_ID\n");
        if (!"".equals(soCOde)) {
            sql.append("                    AND BM.SO_CODE LIKE '%").append(soCOde).append("%' \n");
        }
        if (!"".equals(orderCode)) {
            sql.append("         AND BM.ORDER_CODE LIKE '%").append(orderCode).append("%' \n");
        }
        if (!"".equals(dealerName)) {
            sql.append("         AND BM.DEALER_NAME LIKE '%").append(dealerName).append("%' \n");
        }
        if (!"".equals(startDate)) {
            sql.append(" AND TO_DATE(BM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
        }

        if (!"".equals(endDate)) {
            sql.append(" AND TO_DATE(BM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
        }
        sql.append("\t\t\t\t\t\t\t\t\t)\n");
        sql.append("                GROUP BY SM.ORDER_ID, SD.PART_ID -- 销售单对应订单\n");
        sql.append("               UNION ALL\n");
        sql.append("               SELECT SM.ORDER_ID, SD.PART_ID, SUM(SD.BUY_QTY) SALES_QTY\n");
        sql.append("                 FROM TT_PART_DLR_ORDER_DTL SD, TT_PART_DLR_ORDER_MAIN SM\n");
        sql.append("                WHERE SD.ORDER_ID = SM.ORDER_ID\n");
        sql.append("                  AND SM.SELLER_ID = 2010010100070674\n");
        sql.append("                  AND EXISTS\n");
        sql.append("                (SELECT 1\n");
        sql.append("                         FROM TT_PART_BO_MAIN BM\n");
        sql.append("                        WHERE BM.ORDER_ID = SM.ORDER_ID\n");
        sql.append("                          AND BM.SO_ID IS NULL\n");
        if (!"".equals(soCOde)) {
            sql.append("                    AND BM.SO_CODE LIKE '%").append(soCOde).append("%' \n");
        }
        if (!"".equals(orderCode)) {
            sql.append("         AND BM.ORDER_CODE LIKE '%").append(orderCode).append("%' \n");
        }
        if (!"".equals(dealerName)) {
            sql.append("         AND BM.DEALER_NAME LIKE '%").append(dealerName).append("%' \n");
        }
        if (!"".equals(startDate)) {
            sql.append(" AND TO_DATE(BM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
        }

        if (!"".equals(endDate)) {
            sql.append(" AND TO_DATE(BM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
        }
        sql.append("\t\t\t\t\t\t\t\t\t)\n");
        sql.append("                GROUP BY SM.ORDER_ID, SD.PART_ID --完全BO单对应订单\n");
        sql.append("               ) SO,\n");

      /*  sql.append("        (SELECT SM.ORDER_ID, SD.PART_ID, SUM(SD.SALES_QTY) SALES_QTY\n");
        sql.append("           FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM\n");
        sql.append("          WHERE SD.SO_ID = SM.SO_ID\n");
        sql.append("            AND SM.SELLER_ID = ");
        if (logonUser.getDealerId() == null) {
            sql.append(logonUser.getOrgId());
        } else {
            sql.append(logonUser.getDealerId());
        }
        sql.append(" AND EXISTS (SELECT 1 \n");
        sql.append("                   FROM TT_PART_SO_MAIN BM \n");
        sql.append("                  WHERE BM.SO_ID = SM.SO_ID \n");
        if (!"".equals(soCOde)) {
            sql.append("                    AND BM.SO_CODE LIKE '%").append(soCOde).append("%' \n");
        }
        if (!"".equals(orderCode)) {
            sql.append("         AND BM.ORDER_CODE LIKE '%").append(orderCode).append("%' \n");
        }
        if (!"".equals(dealerName)) {
            sql.append("         AND BM.DEALER_NAME LIKE '%").append(dealerName).append("%' \n");
        }
        if (!"".equals(startDate)) {
            sql.append(" AND TO_DATE(BM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')\n");
        }

        if (!"".equals(endDate)) {
            sql.append(" AND TO_DATE(BM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')\n");
        }
        sql.append(")\n");
        sql.append("          GROUP BY SM.ORDER_ID, SD.PART_ID) SO,\n");*/
        sql.append("        (SELECT BM.ORDER_ID,\n");
        sql.append("                BD.PART_ID,\n");
        sql.append("                SUM(BD.BUY_QTY) BUY_QTY,\n");
        sql.append("                SUM(BD.SALES_QTY) SALES_QTY,\n");
        sql.append("                SUM(BD.BO_QTY) BO_QTY,\n");
        sql.append("                SUM(BD.BO_QTY * BD.BUY_PRICE) BO_AMOUNT,\n");
        sql.append("                SUM(BD.TOSAL_QTY) TOSAL_QTY\n");
        sql.append("           FROM TT_PART_BO_DTL BD, TT_PART_BO_MAIN BM\n");
        sql.append("          WHERE BD.BO_ID = BM.BO_ID\n");
        sql.append("            AND BM.BO_TYPE=1\n");
        if (!"".equals(soCOde)) {
            sql.append("     AND EXISTS (SELECT 1 \n");
            sql.append("                   FROM TT_PART_SO_MAIN sm \n");
            sql.append("                  WHERE BM.SO_ID = SM.SO_ID \n");
            sql.append("                    AND sm.SO_CODE LIKE '%").append(soCOde).append("%') \n");
        }
        if (!"".equals(orderCode)) {
            sql.append("         AND bm.ORDER_CODE LIKE '%").append(orderCode).append("%' \n");
        }
        sql.append("          GROUP BY BM.ORDER_ID, BD.PART_ID) BO\n");
        sql.append("  WHERE PO.ORDER_ID = SO.ORDER_ID(+)\n");
        sql.append("    AND PO.PART_ID = SO.PART_ID(+)\n");
        sql.append("    AND PO.ORDER_ID = BO.ORDER_ID(+)\n");
        sql.append("    AND PO.PART_ID = BO.PART_ID(+)\n");
        //sql.append("    HAVING  SUM(NVL(BO.BO_AMOUNT, 0))>0\n");
        sql.append("  GROUP BY PO.PART_ID) BOS\n");
        if ("1".equals(rSelect) || "2".equals(rSelect)) {
            sql.append("  WHERE BS.PART_ID = BOS.PART_ID(+) \n");
        } else {
            sql.append("  WHERE BS.PART_ID = BOS.PART_ID \n");
        }
        if (!"".equals(sortType)) {
            if ("1".equals(sortType)) {
                sql.append(" ORDER BY BOXS DESC");
            } else if ("2".equals(sortType)) {
                sql.append(" ORDER BY BO_QTY DESC ");
            } else {
                sql.append(" ORDER BY BOXS DESC,BO_QTY DESC ");
            }
        }
        return pageQueryMap(sql.toString(), null, getFunName());
    }

    public PageResult<Map<String, Object>> queryPartSalesDtlList(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订货单号
            String soCode = CommonUtils.checkNull(request.getParamValue("SO_CODE"));//销售单号
            String childorgName = CommonUtils.checkNull(request.getParamValue("childorgName"));//订货单位
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//订货日期
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//订货日期
            String startSaleDate = CommonUtils.checkNull(request.getParamValue("startSaleDate"));//销售日期
            String endSaleDate = CommonUtils.checkNull(request.getParamValue("endSaleDate"));//销售日期
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件类型
            String regionName = CommonUtils.checkNull(request.getParamValue("REGION_NAME"));//省份
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));//配件编码
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT SM.ORDER_CODE, --订单编码 \n");
//                sql.append("        DOD.CREATE_DATE ORDER_DATE, --订货日期 \n");
            sql.append("        null ORDER_DATE, --订货日期 \n");
            sql.append("        SM.SO_CODE, --销售单号 \n");
            sql.append("        OD.CREATE_DATE  OUT_DATE, --出库日期 \n");
            sql.append("        TR.REGION_NAME, --省份 \n");
            sql.append("        /* SM.DEALER_ID,*/ \n");
            sql.append("        SM.DEALER_CODE, --订货单位编码 \n");
            sql.append("        SM.DEALER_NAME, --订货单位 \n");
            sql.append("        SM.SELLER_CODE, --销售单位编码 \n");
            sql.append("        SM.SELLER_NAME, --销售单位 \n");
            sql.append("        /*OD.PART_ID,*/ \n");
            sql.append("        OD.PART_OLDCODE, --部品编码 \n");
            sql.append("        OD.PART_CNAME, --部品名称 \n");
            sql.append("        OD.PART_CODE, --部品件号 \n");
            sql.append("        TC.CODE_ID    PART_TYPE, --部品类型 \n");
            sql.append("        OD.UNIT, --单位 \n");
            sql.append("        SD.BUY_QTY, --订货数量 \n");
            sql.append("        OD.OUTSTOCK_QTY, --销售数量 \n");
            sql.append("        TO_CHAR(OD.SALE_PRICE, 'FM999,999,990.00') SALE_PRICE, --销售单价 \n");
            sql.append("        TO_CHAR(OD.SALE_AMOUNT, 'FM999,999,990.00') SALE_AMOUNT --销售金额 \n");
//                sql.append("   FROM TT_PART_DLR_ORDER_DTL DOD, \n");
            sql.append("  FROM  TT_PART_SO_DTL        SD, \n");
            sql.append("        TT_PART_OUTSTOCK_DTL  OD, \n");
            sql.append("        TT_PART_SO_MAIN       SM, \n");
            sql.append("        TM_REGION             TR, \n");
            sql.append("        TM_DEALER             TD, \n");
            sql.append("        TT_PART_DEFINE        TPD, \n");
            sql.append("        TC_CODE               TC \n");
            sql.append("  WHERE SD.SO_ID = SM.SO_ID \n");
//                sql.append("    AND SD.ORDER_ID = DOD.ORDER_ID \n");
//                sql.append("    AND SD.PART_ID = DOD.PART_ID \n");
            sql.append("    AND SD.SO_ID = OD.SO_ID \n");
            sql.append("    AND TPD.PART_ID = SD.PART_ID \n");
            sql.append("    AND SD.PART_ID = OD.PART_ID \n");
            sql.append("    AND TPD.PART_TYPE = TC.CODE_ID \n");
            sql.append("    AND SM.DEALER_ID = TD.DEALER_ID \n");
            sql.append("    AND TD.PROVINCE_ID = TR.REGION_CODE \n");
            sql.append("    AND SM.SELLER_ID = ");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }

            if (!"".equals(orderCode)) {
                sql.append(" AND SM.ORDER_CODE LIKE '%").append(orderCode).append("%'");
            }

            if (!"".equals(soCode)) {
                sql.append(" AND SM.SO_CODE LIKE '%").append(soCode).append("%'");
            }

            if (!"".equals(childorgName)) {
                sql.append(" AND SM.DEALER_NAME LIKE '%").append(childorgName).append("%'");
            }

            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(DOD.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(DOD.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(startSaleDate)) {
                sql.append(" AND TO_DATE(OD.CREATE_DATE)>=").append("TO_DATE('").append(startSaleDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endSaleDate)) {
                sql.append(" AND TO_DATE(OD.CREATE_DATE)<=").append("TO_DATE('").append(endSaleDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(partType)) {
                sql.append(" AND TC.CODE_ID = ").append(partType);
            }

            if (!"".equals(regionName)) {
                sql.append(" AND TR.REGION_NAME LIKE '%").append(regionName).append("%'");
            }

            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(OD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(OD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCname)) {
                sql.append(" AND OD.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartSalesDtl(RequestWrapper request,
                                                       AclUserBean logonUser) throws Exception {
        List<Map<String, Object>> list;
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订货单号
            String soCode = CommonUtils.checkNull(request.getParamValue("SO_CODE"));//销售单号
            String childorgName = CommonUtils.checkNull(request.getParamValue("childorgName"));//订货单位
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//订货日期
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//订货日期
            String startSaleDate = CommonUtils.checkNull(request.getParamValue("startSaleDate"));//销售日期
            String endSaleDate = CommonUtils.checkNull(request.getParamValue("endSaleDate"));//销售日期
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件类型
            String regionName = CommonUtils.checkNull(request.getParamValue("REGION_NAME"));//省份
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));//配件编码
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT SM.ORDER_CODE, --订单编码 \n");
//                sql.append("        DOD.CREATE_DATE ORDER_DATE, --订货日期 \n");
            sql.append("        null ORDER_DATE, --订货日期 \n");
            sql.append("        SM.SO_CODE, --销售单号 \n");
            sql.append("        OD.CREATE_DATE  OUT_DATE, --出库日期 \n");
            sql.append("        TR.REGION_NAME, --省份 \n");
            sql.append("        /* SM.DEALER_ID,*/ \n");
            sql.append("        SM.DEALER_CODE, --订货单位编码 \n");
            sql.append("        SM.DEALER_NAME, --订货单位 \n");
            sql.append("        SM.SELLER_CODE, --销售单位编码 \n");
            sql.append("        SM.SELLER_NAME, --销售单位 \n");
            sql.append("        /*OD.PART_ID,*/ \n");
            sql.append("        OD.PART_OLDCODE, --部品编码 \n");
            sql.append("        OD.PART_CNAME, --部品名称 \n");
            sql.append("        OD.PART_CODE, --部品件号 \n");
            sql.append("        DECODE(TC.CODE_ID, \n");
            sql.append(Constant.PART_BASE_PART_TYPES_SELF_MADE).append(",\n");
            sql.append("               '自制件', \n");
            sql.append(Constant.PART_BASE_PART_TYPES_PURCHASE).append(",\n");
            sql.append("               '国产件', \n");
            sql.append(Constant.PART_BASE_PART_TYPES_ENTRANCE).append(",\n");
            sql.append("               '进口件') PART_TYPE, --部品类型 \n");
            sql.append("        OD.UNIT, --单位 \n");
            sql.append("        SD.BUY_QTY, --订货数量 \n");
            sql.append("        OD.OUTSTOCK_QTY, --销售数量 \n");
            sql.append("        TO_CHAR(OD.SALE_PRICE, 'FM999,999,990.00') SALE_PRICE, --销售单价 \n");
            sql.append("        TO_CHAR(OD.SALE_AMOUNT, 'FM999,999,990.00') SALE_AMOUNT --销售金额 \n");
//                sql.append("   FROM TT_PART_DLR_ORDER_DTL DOD, \n");
            sql.append("     FROM   TT_PART_SO_DTL        SD, \n");
            sql.append("        TT_PART_OUTSTOCK_DTL  OD, \n");
            sql.append("        TT_PART_SO_MAIN       SM, \n");
            sql.append("        TM_REGION             TR, \n");
            sql.append("        TM_DEALER             TD, \n");
            sql.append("        TT_PART_DEFINE        TPD, \n");
            sql.append("        TC_CODE               TC \n");
            sql.append("  WHERE SD.SO_ID = SM.SO_ID \n");
//                sql.append("    AND SD.ORDER_ID = DOD.ORDER_ID \n");
//                sql.append("    AND SD.PART_ID = DOD.PART_ID \n");
            sql.append("    AND SD.SO_ID = OD.SO_ID \n");
            sql.append("    AND TPD.PART_ID = SD.PART_ID \n");
            sql.append("    AND SD.PART_ID = OD.PART_ID \n");
            sql.append("    AND TPD.PART_TYPE = TC.CODE_ID \n");
            sql.append("    AND SM.DEALER_ID = TD.DEALER_ID \n");
            sql.append("    AND TD.PROVINCE_ID = TR.REGION_CODE \n");
            sql.append("    AND SM.SELLER_ID = ");
            if (logonUser.getDealerId() == null) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }

            if (!"".equals(orderCode)) {
                sql.append(" AND SM.ORDER_CODE LIKE '%").append(orderCode).append("%'");
            }

            if (!"".equals(soCode)) {
                sql.append(" AND SM.SO_CODE LIKE '%").append(soCode).append("%'");
            }

            if (!"".equals(childorgName)) {
                sql.append(" AND SM.DEALER_NAME LIKE '%").append(childorgName).append("%'");
            }

            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(DOD.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(DOD.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(startSaleDate)) {
                sql.append(" AND TO_DATE(OD.CREATE_DATE)>=").append("TO_DATE('").append(startSaleDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endSaleDate)) {
                sql.append(" AND TO_DATE(OD.CREATE_DATE)<=").append("TO_DATE('").append(endSaleDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(partType)) {
                sql.append(" AND TC.CODE_ID = ").append(partType);
            }

            if (!"".equals(regionName)) {
                sql.append(" AND TR.REGION_NAME LIKE '%").append(regionName).append("%'");
            }

            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(OD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(OD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCname)) {
                sql.append(" AND OD.PART_CNAME LIKE '%").append(partCname).append("%'");
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public PageResult<Map<String, Object>> queryChildOrgList(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String childOrgName = CommonUtils.checkNull(request.getParamValue("childOrgName"));//名称
            String childOrgCode = CommonUtils.checkNull(request.getParamValue("childOrgCode"));//编码

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT T.CHILDORG_ID, T.CHILDORG_CODE, T.CHILDORG_NAME \n");
            sql.append("   FROM VW_PART_SALES_RELATION T, TM_DEALER D \n");
            sql.append("  WHERE T.CHILDORG_ID = D.DEALER_ID \n");
            sql.append("    AND T.PARENTORG_ID = ").append(logonUser.getOrgId());
            sql.append("    AND D.PDEALER_TYPE = ").append(Constant.PART_SALE_PRICE_DEALER_TYPE_01);

            if (!"".equals(childOrgName)) {
                sql.append(" AND T.CHILDORG_NAME LIKE '%").append(childOrgName).append("%'");
            }

            if (!"".equals(childOrgCode)) {
                sql.append(" AND T.CHILDORG_CODE LIKE '%").append(childOrgCode).append("%'");
            }

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> queryChildOrgByGyzxList(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String childOrgName = CommonUtils.checkNull(request.getParamValue("childOrgName"));//名称
            String childOrgCode = CommonUtils.checkNull(request.getParamValue("childOrgCode"));//编码
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));//供应中心

            StringBuffer sql = new StringBuffer("");

            if (!"".equals(venderId)) {
                sql.append(" SELECT T.CHILDORG_ID, T.CHILDORG_CODE, T.CHILDORG_NAME \n");
                sql.append("   FROM VW_PART_SALES_RELATION T\n");
                sql.append("  WHERE T.PARENTORG_ID=").append(venderId);
            } else {
                sql.append(" SELECT T.CHILDORG_ID, T.CHILDORG_CODE, T.CHILDORG_NAME \n");
                sql.append("   FROM VW_PART_SALES_RELATION T, TM_DEALER D \n");
                sql.append("  WHERE T.PARENTORG_ID = D.DEALER_ID \n");
                sql.append("    AND D.PDEALER_TYPE = ").append(Constant.PART_SALE_PRICE_DEALER_TYPE_01);
            }

            if (!"".equals(childOrgName)) {
                sql.append(" AND T.CHILDORG_NAME LIKE '%").append(childOrgName).append("%'");
            }

            if (!"".equals(childOrgCode)) {
                sql.append(" AND T.CHILDORG_CODE LIKE '%").append(childOrgCode).append("%'");
            }

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> queryPartSalesDtl4GyzxList(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订货单号
            String soCode = CommonUtils.checkNull(request.getParamValue("SO_CODE"));//销售单号
            String childorgName = CommonUtils.checkNull(request.getParamValue("childorgName"));//订货单位
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//订货日期
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//订货日期
            String startSaleDate = CommonUtils.checkNull(request.getParamValue("startSaleDate"));//销售日期
            String endSaleDate = CommonUtils.checkNull(request.getParamValue("endSaleDate"));//销售日期
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件类型
            String regionName = CommonUtils.checkNull(request.getParamValue("REGION_NAME"));//省份
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));//配件编码
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String venderName = CommonUtils.checkNull(request.getParamValue("venderName"));//供应中心
            String venderCode = CommonUtils.checkNull(request.getParamValue("venderCode"));//供应中心
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));//供应中心

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT SM.ORDER_CODE, --订单编码 \n");
            sql.append("        DOD.CREATE_DATE ORDER_DATE, --订货日期 \n");
            sql.append("        SM.SO_CODE, --销售单号 \n");
            sql.append("        OD.CREATE_DATE  OUT_DATE, --出库日期 \n");
            sql.append("        TR.REGION_NAME, --省份 \n");
            sql.append("        /* SM.DEALER_ID,*/ \n");
            sql.append("        SM.DEALER_CODE, --订货单位编码 \n");
            sql.append("        SM.DEALER_NAME, --订货单位 \n");
            if (!"".equals(venderId)) {
                sql.append("'" + venderCode + "' SELLER_CODE, --供应中心编码\n");
                sql.append("'" + venderName + "' SELLER_NAME, --供应中心\n");
            } else {
                sql.append("        SM.SELLER_CODE, --供应中心编码\n");
                sql.append("        SM.SELLER_NAME, --供应中心 \n");
            }
            sql.append("        /*OD.PART_ID,*/ \n");
            sql.append("        OD.PART_OLDCODE, --配件编码 \n");
            sql.append("        OD.PART_CNAME, --配件名称 \n");
            sql.append("        OD.PART_CODE, --配件件号 \n");
            sql.append("        TC.CODE_ID    PART_TYPE, --配件类型 \n");
            sql.append("        OD.UNIT, --单位 \n");
            sql.append("        SD.BUY_QTY, --订货数量 \n");
            sql.append("        OD.OUTSTOCK_QTY, --销售数量 \n");
            sql.append("        TO_CHAR(OD.SALE_PRICE, 'FM999,999,990.00') SALE_PRICE, --销售单价 \n");
            sql.append("        TO_CHAR(OD.SALE_AMOUNT, 'FM999,999,990.00') SALE_AMOUNT --销售金额 \n");
            sql.append("   FROM TT_PART_DLR_ORDER_DTL DOD, \n");
            sql.append("        TT_PART_SO_DTL        SD, \n");
            sql.append("        TT_PART_OUTSTOCK_DTL  OD, \n");
            sql.append("        TT_PART_SO_MAIN       SM, \n");
            sql.append("        TM_REGION             TR, \n");
            sql.append("        TM_DEALER             TD, \n");
            sql.append("        TT_PART_DEFINE        TPD, \n");
            sql.append("        TC_CODE               TC \n");
            sql.append("  WHERE SD.SO_ID = SM.SO_ID \n");
            sql.append("    AND SD.ORDER_ID = DOD.ORDER_ID \n");
            sql.append("    AND SD.PART_ID = DOD.PART_ID \n");
            sql.append("    AND SD.SO_ID = OD.SO_ID \n");
            sql.append("    AND TPD.PART_ID = SD.PART_ID \n");
            sql.append("    AND SD.PART_ID = OD.PART_ID \n");
            sql.append("    AND TPD.PART_TYPE = TC.CODE_ID \n");
            sql.append("    AND SM.DEALER_ID = TD.DEALER_ID \n");
            sql.append("    AND TD.PROVINCE_ID = TR.REGION_CODE \n");

            if (!"".equals(venderId)) {
                sql.append(" AND SM.SELLER_ID=").append(venderId);
            } else {
                sql.append(" AND SM.SELLER_ID <>").append(logonUser.getOrgId());
            }

            if (!"".equals(orderCode)) {
                sql.append(" AND SM.ORDER_CODE LIKE '%").append(orderCode).append("%'");
            }

            if (!"".equals(soCode)) {
                sql.append(" AND SM.SO_CODE LIKE '%").append(soCode).append("%'");
            }

            if (!"".equals(childorgName)) {
                sql.append(" AND SM.DEALER_NAME LIKE '%").append(childorgName).append("%'");
            }

            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(DOD.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(DOD.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(startSaleDate)) {
                sql.append(" AND TO_DATE(OD.CREATE_DATE)>=").append("TO_DATE('").append(startSaleDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endSaleDate)) {
                sql.append(" AND TO_DATE(OD.CREATE_DATE)<=").append("TO_DATE('").append(endSaleDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(partType)) {
                sql.append(" AND TC.CODE_ID = ").append(partType);
            }

            if (!"".equals(regionName)) {
                sql.append(" AND TR.REGION_NAME LIKE '%").append(regionName).append("%'");
            }

            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(OD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(OD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCname)) {
                sql.append(" AND OD.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartSalesDtl4Gyzx(
            RequestWrapper request, AclUserBean logonUser) throws Exception {
        List<Map<String, Object>> list;
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订货单号
            String soCode = CommonUtils.checkNull(request.getParamValue("SO_CODE"));//销售单号
            String childorgName = CommonUtils.checkNull(request.getParamValue("childorgName"));//订货单位
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//订货日期
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//订货日期
            String startSaleDate = CommonUtils.checkNull(request.getParamValue("startSaleDate"));//销售日期
            String endSaleDate = CommonUtils.checkNull(request.getParamValue("endSaleDate"));//销售日期
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件类型
            String regionName = CommonUtils.checkNull(request.getParamValue("REGION_NAME"));//省份
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));//配件编码
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String venderName = CommonUtils.checkNull(request.getParamValue("venderName"));//供应中心
            String venderCode = CommonUtils.checkNull(request.getParamValue("venderCode"));//供应中心
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));//供应中心

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT SM.ORDER_CODE, --订单编码 \n");
            sql.append("        DOD.CREATE_DATE ORDER_DATE, --订货日期 \n");
            sql.append("        SM.SO_CODE, --销售单号 \n");
            sql.append("        OD.CREATE_DATE  OUT_DATE, --出库日期 \n");
            sql.append("        TR.REGION_NAME, --省份 \n");
            sql.append("        /* SM.DEALER_ID,*/ \n");
            sql.append("        SM.DEALER_CODE, --订货单位编码 \n");
            sql.append("        SM.DEALER_NAME, --订货单位 \n");
            if (!"".equals(venderId)) {
                sql.append("'" + venderCode + "' SELLER_CODE, --供应中心编码\n");
                sql.append("'" + venderName + "' SELLER_NAME, --供应中心\n");
            } else {
                sql.append("        SM.SELLER_CODE, --供应中心编码\n");
                sql.append("        SM.SELLER_NAME, --供应中心 \n");
            }
            sql.append("        /*OD.PART_ID,*/ \n");
            sql.append("        OD.PART_OLDCODE, --配件编码 \n");
            sql.append("        OD.PART_CNAME, --配件名称 \n");
            sql.append("        OD.PART_CODE, --配件件号 \n");
            sql.append("        DECODE(TC.CODE_ID, \n");
            sql.append(Constant.PART_BASE_PART_TYPES_SELF_MADE).append(",\n");
            sql.append("               '自制件', \n");
            sql.append(Constant.PART_BASE_PART_TYPES_PURCHASE).append(",\n");
            sql.append("               '国产件', \n");
            sql.append(Constant.PART_BASE_PART_TYPES_ENTRANCE).append(",\n");
            sql.append("               '进口件') PART_TYPE, --配件类型 \n");
            sql.append("        OD.UNIT, --单位 \n");
            sql.append("        SD.BUY_QTY, --订货数量 \n");
            sql.append("        OD.OUTSTOCK_QTY, --销售数量 \n");
            sql.append("        TO_CHAR(OD.SALE_PRICE, 'FM999,999,990.00') SALE_PRICE, --销售单价 \n");
            sql.append("        TO_CHAR(OD.SALE_AMOUNT, 'FM999,999,990.00') SALE_AMOUNT --销售金额 \n");
            sql.append("   FROM TT_PART_DLR_ORDER_DTL DOD, \n");
            sql.append("        TT_PART_SO_DTL        SD, \n");
            sql.append("        TT_PART_OUTSTOCK_DTL  OD, \n");
            sql.append("        TT_PART_SO_MAIN       SM, \n");
            sql.append("        TM_REGION             TR, \n");
            sql.append("        TM_DEALER             TD, \n");
            sql.append("        TT_PART_DEFINE        TPD, \n");
            sql.append("        TC_CODE               TC \n");
            sql.append("  WHERE SD.SO_ID = SM.SO_ID \n");
            sql.append("    AND SD.ORDER_ID = DOD.ORDER_ID \n");
            sql.append("    AND SD.PART_ID = DOD.PART_ID \n");
            sql.append("    AND SD.SO_ID = OD.SO_ID \n");
            sql.append("    AND TPD.PART_ID = SD.PART_ID \n");
            sql.append("    AND SD.PART_ID = OD.PART_ID \n");
            sql.append("    AND TPD.PART_TYPE = TC.CODE_ID \n");
            sql.append("    AND SM.DEALER_ID = TD.DEALER_ID \n");
            sql.append("    AND TD.PROVINCE_ID = TR.REGION_CODE \n");

            if (!"".equals(venderId)) {
                sql.append(" AND SM.SELLER_ID=").append(venderId);
            } else {
                sql.append(" AND SM.SELLER_ID <>").append(logonUser.getOrgId());
            }

            if (!"".equals(orderCode)) {
                sql.append(" AND SM.ORDER_CODE LIKE '%").append(orderCode).append("%'");
            }

            if (!"".equals(soCode)) {
                sql.append(" AND SM.SO_CODE LIKE '%").append(soCode).append("%'");
            }

            if (!"".equals(childorgName)) {
                sql.append(" AND SM.DEALER_NAME LIKE '%").append(childorgName).append("%'");
            }

            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(DOD.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(DOD.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(startSaleDate)) {
                sql.append(" AND TO_DATE(OD.CREATE_DATE)>=").append("TO_DATE('").append(startSaleDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endSaleDate)) {
                sql.append(" AND TO_DATE(OD.CREATE_DATE)<=").append("TO_DATE('").append(endSaleDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(partType)) {
                sql.append(" AND TC.CODE_ID = ").append(partType);
            }

            if (!"".equals(regionName)) {
                sql.append(" AND TR.REGION_NAME LIKE '%").append(regionName).append("%'");
            }

            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(OD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(OD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCname)) {
                sql.append(" AND OD.PART_CNAME LIKE '%").append(partCname).append("%'");
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public PageResult<Map<String, Object>> queryPartSalesGyzxList(
            RequestWrapper request, AclUserBean logonUser, Integer curPage,
            Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String year = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//年
            String month = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//月
            String season = CommonUtils.checkNull(request.getParamValue("MYSEASON"));//季度
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//供应中心
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//供应中心

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT TD.DEALER_CODE,\n");
            sql.append("       TD.DEALER_NAME,\n");
            sql.append("       nvl(SXE.BUY_AMOUNT,0) BUY_AMOUNT, --采购金额无税\n");
            sql.append("       nvl(SXE.BUY_QTY,0) BUY_QTY, --采购数量\n");
            sql.append("       NVL(BO.BOZXS, 0) BOZXS, --BO总项数\n");
            sql.append("       nvl(XS.SALZXS,0) SALZXS,--销售总项数\n");
            sql.append("       TO_CHAR(NVL(BO.BOZXS, 0) / XS.SALZXS * 100, '99.99') || '%' BO_RATE, --BO率\n");
            sql.append("       nvl(CK.SALES_AMOUNT,0) SALES_AMOUNT, --销售金额无税\n");
            sql.append("       nvl(CK.SALES_QTY,0) SALES_QTY, --销售数量\n");
            sql.append("       nvl(ZK.STOCK_AMOUNT,0) STOCK_AMOUNT --在库金额(服务商价)\n");
            sql.append("   FROM (SELECT SM.DEALER_ID, \n");
            sql.append("                NVL(ROUND(SUM(SD.SALE_AMOUNT) / (1 + 0.17), 2), 0) BUY_AMOUNT, --采购金额(不含税) \n");
            sql.append("                NVL(SUM(SD.OUTSTOCK_QTY), 0) BUY_QTY --采购数量 \n");
            sql.append("           FROM TT_PART_OUTSTOCK_MAIN SM, \n");
            sql.append("                TT_PART_OUTSTOCK_DTL SD, \n");
            sql.append("                TT_PART_DEFINE TPD \n");
            sql.append("          WHERE SM.OUT_ID = SD.OUT_ID \n");
            sql.append("            AND SD.PART_ID = TPD.PART_ID \n");
            sql.append("            AND SM.STATE <> ").append(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);
            sql.append("            AND SM.SELLER_ID = ").append(Constant.OEM_ACTIVITIES);
            sql.append("            AND SM.DEALER_CODE LIKE 'G%' \n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(SM.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("            AND SM.STATUS = 1 \n");
            sql.append("            AND SD.STATUS = 1 \n");
            sql.append("          GROUP BY SM.DEALER_ID) SXE, \n");
            sql.append("        (SELECT NVL(COUNT(1), 0) BOZXS, --BO总项数  \n");
            sql.append("                T4.SELLER_ID DEALER_ID \n");
            sql.append("           FROM TT_PART_BO_MAIN T1, \n");
            sql.append("                TT_PART_BO_DTL T2, \n");
            sql.append("                TT_PART_DEFINE D, \n");
            sql.append("                TT_PART_DLR_ORDER_MAIN T4 \n");
            sql.append("          WHERE T1.BO_ID = T2.BO_ID \n");
            sql.append("            AND T2.PART_ID = D.PART_ID \n");
            sql.append("            AND T1.ORDER_ID = T4.ORDER_ID \n");
            sql.append("            AND T1.BO_TYPE = 1 \n");
            sql.append("            AND T4.SELLER_ID != ").append(Constant.OEM_ACTIVITIES);
            sql.append("          \n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(T1.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(T1.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(T1.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("          GROUP BY T4.SELLER_ID) BO, \n");
            sql.append("        (SELECT COUNT(1) SALZXS, --销售总项数  \n");
            sql.append("                SM.SELLER_ID DEALER_ID \n");
            sql.append("           FROM TT_PART_SO_MAIN SM, TT_PART_SO_DTL SD, TT_PART_DEFINE TPD \n");
            sql.append("          WHERE SM.SO_ID = SD.SO_ID \n");
            sql.append("            AND SD.PART_ID = TPD.PART_ID \n");
            sql.append("            AND SM.ORDER_ID IS NOT NULL \n");
            sql.append("            AND SM.STATE <> ").append(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);
            sql.append("            AND SM.SELLER_ID != ").append(Constant.OEM_ACTIVITIES);
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(SM.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("            AND SM.STATUS = 1 \n");
            sql.append("            AND SD.STATUS = 1 \n");
            sql.append("          GROUP BY SM.SELLER_ID) XS, \n");
            sql.append("        (SELECT SUM(DT.OUTSTOCK_QTY) SALES_QTY, --销售数量 \n");
            sql.append("                NVL(ROUND(SUM(DT.sale_amount) / (1 + 0.17), 2), 0) SALES_AMOUNT, --销售金额 (无税) \n");
            sql.append("                OM.SELLER_ID DEALER_ID \n");
            sql.append("           FROM TT_PART_OUTSTOCK_DTL DT, \n");
            sql.append("                TT_PART_OUTSTOCK_MAIN OM, \n");
            sql.append("                TT_PART_DEFINE D \n");
            sql.append("          WHERE DT.OUT_ID = OM.OUT_ID \n");
            sql.append("            AND DT.PART_ID = D.PART_ID \n");
            sql.append("            AND OM.SELLER_ID != ").append(Constant.OEM_ACTIVITIES);
            sql.append("            AND OM.SELLER_CODE LIKE 'G%' \n");
            sql.append("          \n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(DT.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(DT.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(DT.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("          GROUP BY OM.SELLER_ID) CK, \n");
            sql.append("        (SELECT round(NVL(SUM(V.ITEM_QTY * (SELECT PR.SALE_PRICE1 \n");
            sql.append("                                        FROM TT_PART_SALES_PRICE PR \n");
            sql.append("                                       WHERE PR.PART_ID = V.PART_ID \n");
            sql.append("                                         AND PR.STATE = 10011001 \n");
            sql.append("                                         AND PR.STATUS = 1)), \n");
            sql.append("                    0)/1.17,2) STOCK_AMOUNT, --在库金额(服务站采购价) \n");
            sql.append("                V.ORG_ID DEALER_ID \n");
            sql.append("           FROM VW_PART_STOCK V, TT_PART_DEFINE D \n");
            sql.append("          WHERE V.PART_ID = D.PART_ID \n");
            sql.append("            AND V.ORG_ID != ").append(Constant.OEM_ACTIVITIES);
            sql.append("          GROUP BY V.ORG_ID) ZK, \n");
            sql.append("        TM_DEALER TD \n");
            sql.append("  WHERE TD.DEALER_CODE LIKE 'G%' \n");
            if (!"".equals(dealerCode)) {
                sql.append("  AND TD.DEALER_CODE LIKE '%").append(dealerCode).append("%'");
            }
            if (!"".equals(dealerName)) {
                sql.append("  AND TD.DEALER_NAME LIKE '%").append(dealerName).append("%'");
            }
            sql.append("AND TD.DEALER_ID = SXE.DEALER_ID(+)\n");
            sql.append("AND TD.DEALER_ID = BO.DEALER_ID(+)\n");
            sql.append("AND TD.DEALER_ID = XS.DEALER_ID(+)\n");
            sql.append("AND TD.DEALER_ID = CK.DEALER_ID(+)\n");
            sql.append("AND TD.DEALER_ID = ZK.DEALER_ID(+)\n");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartSalesGyzx(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String year = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//年
            String month = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//月
            String season = CommonUtils.checkNull(request.getParamValue("MYSEASON"));//季度
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//供应中心
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//供应中心

            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT TD.DEALER_CODE,\n");
            sql.append("       TD.DEALER_NAME,\n");
            sql.append("       nvl(SXE.BUY_AMOUNT,0) BUY_AMOUNT, --采购金额无税\n");
            sql.append("       nvl(SXE.BUY_QTY,0) BUY_QTY, --采购数量\n");
            sql.append("       NVL(BO.BOZXS, 0) BOZXS, --BO总项数\n");
            sql.append("       nvl(XS.SALZXS,0) SALZXS,--销售总项数\n");
            sql.append("       TO_CHAR(NVL(BO.BOZXS, 0) / XS.SALZXS * 100, '99.99') || '%' BO_RATE, --BO率\n");
            sql.append("       nvl(CK.SALES_AMOUNT,0) SALES_AMOUNT, --销售金额无税\n");
            sql.append("       nvl(CK.SALES_QTY,0) SALES_QTY, --销售数量\n");
            sql.append("       nvl(ZK.STOCK_AMOUNT,0) STOCK_AMOUNT --在库金额(服务商价)\n");
            sql.append("   FROM (SELECT SM.DEALER_ID, \n");
            sql.append("                NVL(ROUND(SUM(SD.SALE_AMOUNT) / (1 + 0.17), 2), 0) BUY_AMOUNT, --采购金额(不含税) \n");
            sql.append("                NVL(SUM(SD.OUTSTOCK_QTY), 0) BUY_QTY --采购数量 \n");
            sql.append("           FROM TT_PART_OUTSTOCK_MAIN SM, \n");
            sql.append("                TT_PART_OUTSTOCK_DTL SD, \n");
            sql.append("                TT_PART_DEFINE TPD \n");
            sql.append("          WHERE SM.OUT_ID = SD.OUT_ID \n");
            sql.append("            AND SD.PART_ID = TPD.PART_ID \n");
            sql.append("            AND SM.STATE <> ").append(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);
            sql.append("            AND SM.SELLER_ID = ").append(Constant.OEM_ACTIVITIES);
            sql.append("            AND SM.DEALER_CODE LIKE 'G%' \n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(SM.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("            AND SM.STATUS = 1 \n");
            sql.append("            AND SD.STATUS = 1 \n");
            sql.append("          GROUP BY SM.DEALER_ID) SXE, \n");
            sql.append("        (SELECT NVL(COUNT(1), 0) BOZXS, --BO总项数  \n");
            sql.append("                T4.SELLER_ID DEALER_ID \n");
            sql.append("           FROM TT_PART_BO_MAIN T1, \n");
            sql.append("                TT_PART_BO_DTL T2, \n");
            sql.append("                TT_PART_DEFINE D, \n");
            sql.append("                TT_PART_DLR_ORDER_MAIN T4 \n");
            sql.append("          WHERE T1.BO_ID = T2.BO_ID \n");
            sql.append("            AND T2.PART_ID = D.PART_ID \n");
            sql.append("            AND T1.ORDER_ID = T4.ORDER_ID \n");
            sql.append("            AND T1.BO_TYPE = 1 \n");
            sql.append("            AND T4.SELLER_ID != ").append(Constant.OEM_ACTIVITIES);
            sql.append("          \n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(T1.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(T1.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(T1.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("          GROUP BY T4.SELLER_ID) BO, \n");
            sql.append("        (SELECT COUNT(1) SALZXS, --销售总项数  \n");
            sql.append("                SM.SELLER_ID DEALER_ID \n");
            sql.append("           FROM TT_PART_SO_MAIN SM, TT_PART_SO_DTL SD, TT_PART_DEFINE TPD \n");
            sql.append("          WHERE SM.SO_ID = SD.SO_ID \n");
            sql.append("            AND SD.PART_ID = TPD.PART_ID \n");
            sql.append("            AND SM.ORDER_ID IS NOT NULL \n");
            sql.append("            AND SM.STATE <> ").append(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);
            sql.append("            AND SM.SELLER_ID != ").append(Constant.OEM_ACTIVITIES);
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(SM.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(SM.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("            AND SM.STATUS = 1 \n");
            sql.append("            AND SD.STATUS = 1 \n");
            sql.append("          GROUP BY SM.SELLER_ID) XS, \n");
            sql.append("        (SELECT SUM(DT.OUTSTOCK_QTY) SALES_QTY, --销售数量 \n");
            sql.append("                NVL(ROUND(SUM(DT.sale_amount) / (1 + 0.17), 2), 0) SALES_AMOUNT, --销售金额 (无税) \n");
            sql.append("                OM.SELLER_ID DEALER_ID \n");
            sql.append("           FROM TT_PART_OUTSTOCK_DTL DT, \n");
            sql.append("                TT_PART_OUTSTOCK_MAIN OM, \n");
            sql.append("                TT_PART_DEFINE D \n");
            sql.append("          WHERE DT.OUT_ID = OM.OUT_ID \n");
            sql.append("            AND DT.PART_ID = D.PART_ID \n");
            sql.append("            AND OM.SELLER_ID != ").append(Constant.OEM_ACTIVITIES);
            sql.append("            AND OM.SELLER_CODE LIKE 'G%' \n");
            sql.append("          \n");
            if (!"".equals(year)) {
                if (!"".equals(month)) {
                    sql.append(" AND TO_CHAR(DT.CREATE_DATE,'yyyy-mm')='" + year + "-" + month + "'");
                } else if (!"".equals(season)) {
                    sql.append("  AND TO_CHAR(DT.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                } else {
                    sql.append("  AND TO_CHAR(DT.CREATE_DATE,'yyyy')='" + year + "'");
                }
            }
            sql.append("          GROUP BY OM.SELLER_ID) CK, \n");
            sql.append("        (SELECT round(NVL(SUM(V.ITEM_QTY * (SELECT PR.SALE_PRICE1 \n");
            sql.append("                                        FROM TT_PART_SALES_PRICE PR \n");
            sql.append("                                       WHERE PR.PART_ID = V.PART_ID \n");
            sql.append("                                         AND PR.STATE = 10011001 \n");
            sql.append("                                         AND PR.STATUS = 1)), \n");
            sql.append("                    0)/1.17,2) STOCK_AMOUNT, --在库金额(服务站采购价) \n");
            sql.append("                V.ORG_ID DEALER_ID \n");
            sql.append("           FROM VW_PART_STOCK V, TT_PART_DEFINE D \n");
            sql.append("          WHERE V.PART_ID = D.PART_ID \n");
            sql.append("            AND V.ORG_ID != ").append(Constant.OEM_ACTIVITIES);
            sql.append("          GROUP BY V.ORG_ID) ZK, \n");
            sql.append("        TM_DEALER TD \n");
            sql.append("  WHERE TD.DEALER_CODE LIKE 'G%' \n");
            if (!"".equals(dealerCode)) {
                sql.append("  AND TD.DEALER_CODE LIKE '%").append(dealerCode).append("%'");
            }
            if (!"".equals(dealerName)) {
                sql.append("  AND TD.DEALER_NAME LIKE '%").append(dealerName).append("%'");
            }
            sql.append("AND TD.DEALER_ID = SXE.DEALER_ID(+)\n");
            sql.append("AND TD.DEALER_ID = BO.DEALER_ID(+)\n");
            sql.append("AND TD.DEALER_ID = XS.DEALER_ID(+)\n");
            sql.append("AND TD.DEALER_ID = CK.DEALER_ID(+)\n");
            sql.append("AND TD.DEALER_ID = ZK.DEALER_ID(+)\n");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public Map<String, Object> queryBoRateRepGSum(RequestWrapper request, String dealerId, long whId) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        Map<String, Object> map;
        try {
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//BO时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//BO时间
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT NVL(SUM(BOS.BOXS), 0) BOXS, --BO项数 \n");
            sql.append("        NVL(SUM(BOS.XSXS), 0) XSXS, --BO金额 \n");
            sql.append("        TO_CHAR(SUM(NVL(BOS.BOXS, 0)) / \n");
            sql.append("                (DECODE(NVL(SUM(BOS.XSXS), 0), 0, 1, NVL(SUM(BOS.XSXS), 0))) * 100, --BO率 \n");
            sql.append("                '999.99') || '%' BOLV \n");
            sql.append("   FROM (SELECT TPD.PART_ID, \n");
            sql.append("                (SELECT NVL(V.NORMAL_QTY, 0) \n");
            sql.append("                   FROM VW_PART_STOCK V \n");
            sql.append("                  WHERE V.PART_ID = TPD.PART_ID \n");
            sql.append("                    AND V.WH_ID = ").append(whId).append(") NORMAL_QTY, \n");
            sql.append("                TPD.PART_TYPE, --配件类型  \n");
            sql.append("                TPD.PART_OLDCODE, --配件编码  \n");
            sql.append("                TPD.PART_CNAME, --配件名称  \n");
            sql.append("                TPD.PART_CODE --配件件号  \n");
            sql.append("           FROM TT_PART_DEFINE TPD WHERE 1=1\n");

            if (!"".equals(partOldCode)) {
                sql.append("         AND UPPER(TPD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCname)) {
                sql.append("         AND TPD.PART_CNAME LIKE '%").append(partCname).append("%'");
            }

            if (!"".equals(partCode)) {
                sql.append("         AND UPPER(TPD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }

            sql.append("         ) BS, \n");
            sql.append("        (SELECT PO.PART_ID, \n");
            sql.append("                PO.SELLER_CODE, \n");
            sql.append("                PO.SELLER_NAME, \n");
            sql.append("                SUM(PO.BUY_AMOUNT) BUY_AMOUNT, --订货金额 \n");
            sql.append("                SUM(PO.BUY_QTY) BUY_QTY, --订货数量 \n");
            sql.append("                SUM(NVL(SO.SALES_QTY, 0)) SALES_QTY, --已交付数量 \n");
            sql.append("                SUM((CASE \n");
            sql.append("                      WHEN SO.SALES_QTY IS NULL THEN \n");
            sql.append("                       0 \n");
            sql.append("                      WHEN SO.SALES_QTY IS NOT NULL THEN \n");
            sql.append("                       1 \n");
            sql.append("                    END)) XSXS, --销售总项数 \n");
            sql.append("                SUM(NVL(BO.BO_QTY, 0)) BO_QTY, --BO数量 \n");
            sql.append("                SUM((CASE \n");
            sql.append("                      WHEN BO.BO_QTY IS NULL THEN \n");
            sql.append("                       0 \n");
            sql.append("                      WHEN BO.BO_QTY IS NOT NULL THEN \n");
            sql.append("                       1 \n");
            sql.append("                    END)) BOXS, --BO总项数 \n");
            sql.append("                SUM(NVL(BO.BO_AMOUNT, 0)) BO_AMOUNT, \n");
            sql.append("                SUM(NVL(BO.TOSAL_QTY, 0)) TOSAL_QTY --BO满足数量 \n");
            sql.append("           FROM (SELECT OM.ORDER_ID, \n");
            sql.append("                        OM.SELLER_CODE, \n");
            sql.append("                        OM.SELLER_NAME, \n");
            sql.append("                        OD.PART_ID, \n");
            sql.append("                        OD.BUY_PRICE, \n");
            sql.append("                        SUM(OD.BUY_AMOUNT) BUY_AMOUNT, \n");
            sql.append("                        SUM(OD.BUY_QTY) BUY_QTY \n");
            sql.append("                   FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DLR_ORDER_MAIN OM \n");
            sql.append("                  WHERE OD.ORDER_ID = OM.ORDER_ID \n");
            sql.append("                    AND OM.SELLER_ID != ").append(Constant.OEM_ACTIVITIES).append(" --不能去 \n");
            sql.append("                    AND OM.SELLER_ID = ").append(dealerId).append(" --供应中心ID \n");
            sql.append("                    AND EXISTS \n");
            sql.append("                  (SELECT 1 \n");
            sql.append("                           FROM TT_PART_SO_MAIN M \n");
            sql.append("                          WHERE M.ORDER_ID = OM.ORDER_ID \n");
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append(")");
            sql.append("                  GROUP BY OM.ORDER_ID, \n");
            sql.append("                           OM.SELLER_CODE, \n");
            sql.append("                           OM.SELLER_NAME, \n");
            sql.append("                           OD.PART_ID, \n");
            sql.append("                           OD.BUY_PRICE \n");
            sql.append("                 UNION ALL \n");
            sql.append("                 SELECT OM.ORDER_ID, \n");
            sql.append("                        OM.SELLER_CODE, \n");
            sql.append("                        OM.SELLER_NAME, \n");
            sql.append("                        OD.PART_ID, \n");
            sql.append("                        OD.BUY_PRICE, \n");
            sql.append("                        SUM(OD.BUY_AMOUNT) BUY_AMOUNT, \n");
            sql.append("                        SUM(OD.BUY_QTY) BUY_QTY \n");
            sql.append("                   FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DLR_ORDER_MAIN OM \n");
            sql.append("                  WHERE OD.ORDER_ID = OM.ORDER_ID \n");
            sql.append("                    AND OM.SELLER_ID != ").append(Constant.OEM_ACTIVITIES).append(" --不能去 \n");
            sql.append("                    AND OM.SELLER_ID = ").append(dealerId).append(" --供应中心ID \n");
            sql.append("                    AND EXISTS \n");
            sql.append("                  (SELECT 1 \n");
            sql.append("                           FROM TT_PART_BO_MAIN BM \n");
            sql.append("                          WHERE BM.ORDER_ID = OM.ORDER_ID \n");
            sql.append("                            AND BM.SO_ID IS NULL \n");
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append(")");
            sql.append("                  GROUP BY OM.ORDER_ID, \n");
            sql.append("                           OM.SELLER_CODE, \n");
            sql.append("                           OM.SELLER_NAME, \n");
            sql.append("                           OD.PART_ID, \n");
            sql.append("                           OD.BUY_PRICE) PO, \n");
            sql.append("                (SELECT SM.ORDER_ID, SD.PART_ID, SUM(SD.SALES_QTY) SALES_QTY \n");
            sql.append("                   FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM \n");
            sql.append("                  WHERE SD.SO_ID = SM.SO_ID \n");
            sql.append("                    AND SM.STATUS = 1 \n");
            sql.append("                    AND SD.STATUS = 1 \n");
            sql.append("                    AND SM.ORDER_ID IS NOT NULL \n");
            sql.append("                    AND SM.SELLER_ID != ").append(Constant.OEM_ACTIVITIES).append(" --不能去 \n");
            sql.append("                    AND SM.SELLER_ID = ").append(dealerId).append(" --供应中心ID \n");
            sql.append("                    AND EXISTS \n");
            sql.append("                  (SELECT 1 \n");
            sql.append("                           FROM TT_PART_SO_MAIN BM \n");
            sql.append("                          WHERE BM.SO_ID = SM.SO_ID \n");
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append(")");
            sql.append("                  GROUP BY SM.ORDER_ID, SD.PART_ID) SO, \n");
            sql.append("                (SELECT BM.ORDER_ID, \n");
            sql.append("                        BD.PART_ID, \n");
            sql.append("                        SUM(BD.BUY_QTY) BUY_QTY, \n");
            sql.append("                        SUM(BD.SALES_QTY) SALES_QTY, \n");
            sql.append("                        SUM(BD.BO_QTY) BO_QTY, \n");
            sql.append("                        SUM(BD.BO_QTY * BD.BUY_PRICE) BO_AMOUNT, \n");
            sql.append("                        SUM(BD.TOSAL_QTY) TOSAL_QTY \n");
            sql.append("                   FROM TT_PART_BO_DTL BD, TT_PART_BO_MAIN BM \n");
            sql.append("                  WHERE BD.BO_ID = BM.BO_ID \n");
            sql.append("                    AND BM.BO_TYPE = 1 \n");
            sql.append("                  GROUP BY BM.ORDER_ID, BD.PART_ID) BO \n");
            sql.append("          WHERE PO.ORDER_ID = SO.ORDER_ID(+) \n");
            sql.append("            AND PO.PART_ID = SO.PART_ID(+) \n");
            sql.append("            AND PO.ORDER_ID = BO.ORDER_ID(+) \n");
            sql.append("            AND PO.PART_ID = BO.PART_ID(+)  \n");
            /*sql.append("                                 HAVING          SUM(NVL(BO.BO_AMOUNT, 0)) > 0 \n");*/
            sql.append("          GROUP BY PO.PART_ID, PO.SELLER_CODE, PO.SELLER_NAME) BOS \n");
            sql.append("  WHERE BS.PART_ID = BOS.PART_ID \n");

            map = pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return map;
    }

    public PageResult<Map<String, Object>> queryPartBoRateGList(
            RequestWrapper request, String dealerId, long whId,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//BO时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//BO时间
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT BOS.SELLER_CODE, --供应中心编码 \n");
            sql.append("        BOS.SELLER_NAME, --供应中心 \n");
            sql.append("        BS.PART_OLDCODE, --配件编码 \n");
            sql.append("        BS.PART_CNAME, --配件名称 \n");
            sql.append("        BS.PART_CODE, --配件件号 \n");
            sql.append("        DECODE(BS.PART_TYPE, \n");
            sql.append(Constant.PART_BASE_PART_TYPES_SELF_MADE).append(",\n");
            sql.append("               '自制件', \n");
            sql.append(Constant.PART_BASE_PART_TYPES_PURCHASE).append(",\n");
            sql.append("               '国产件', \n");
            sql.append(Constant.PART_BASE_PART_TYPES_ENTRANCE).append(",\n");
            sql.append("               '进口件') PART_TYPE, --配件类型 \n");
            sql.append("        BS.NORMAL_QTY, --当前可以库存 \n");
            sql.append("        NVL(BOS.BUY_QTY, 0) BUY_QTY, --订货数量 \n");
            sql.append("        NVL(BOS.BUY_AMOUNT, 0) BUY_AMOUNT, --订货金额 \n");
            sql.append("        NVL(BOS.SALES_QTY, 0) SALES_QTY, --已交货数量 \n");
            sql.append("        NVL(BOS.BO_QTY, 0) BO_QTY, --BO数量 \n");
            sql.append("        NVL(BOS.BO_AMOUNT, 0) BO_AMOUNT, --BO金额 \n");
            sql.append("        NVL(BOS.TOSAL_QTY, 0) TOSAL_QTY, --满足数量 \n");
            sql.append("        NVL(BOS.BOXS, 0) BOXS, --BO项数 \n");
            sql.append("        NVL(BOS.XSXS, 0) XSXS --BO金额 \n");
            sql.append("   FROM (SELECT TPD.PART_ID, \n");
            sql.append("                (SELECT NVL(V.NORMAL_QTY, 0) \n");
            sql.append("                   FROM VW_PART_STOCK V \n");
            sql.append("                  WHERE V.PART_ID = TPD.PART_ID \n");
            sql.append("                    AND V.WH_ID = ").append(whId).append(") NORMAL_QTY, \n");
            sql.append("                TPD.PART_TYPE, --配件类型  \n");
            sql.append("                TPD.PART_OLDCODE, --配件编码  \n");
            sql.append("                TPD.PART_CNAME, --配件名称  \n");
            sql.append("                TPD.PART_CODE --配件件号  \n");
            sql.append("           FROM TT_PART_DEFINE TPD WHERE 1=1\n");
            if (!"".equals(partOldCode)) {
                sql.append("         AND UPPER(TPD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCname)) {
                sql.append("         AND TPD.PART_CNAME LIKE '%").append(partCname).append("%'");
            }

            if (!"".equals(partCode)) {
                sql.append("         AND UPPER(TPD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            sql.append("         ) BS, \n");
            sql.append("        (SELECT PO.PART_ID, \n");
            sql.append("                PO.SELLER_CODE, \n");
            sql.append("                PO.SELLER_NAME, \n");
            sql.append("                SUM(PO.BUY_AMOUNT) BUY_AMOUNT, --订货金额 \n");
            sql.append("                SUM(PO.BUY_QTY) BUY_QTY, --订货数量 \n");
            sql.append("                SUM(NVL(SO.SALES_QTY, 0)) SALES_QTY, --已交付数量 \n");
            sql.append("                SUM((CASE \n");
            sql.append("                      WHEN SO.SALES_QTY IS NULL THEN \n");
            sql.append("                       0 \n");
            sql.append("                      WHEN SO.SALES_QTY IS NOT NULL THEN \n");
            sql.append("                       1 \n");
            sql.append("                    END)) XSXS, --销售总项数 \n");
            sql.append("                SUM(NVL(BO.BO_QTY, 0)) BO_QTY, --BO数量 \n");
            sql.append("                SUM((CASE \n");
            sql.append("                      WHEN BO.BO_QTY IS NULL THEN \n");
            sql.append("                       0 \n");
            sql.append("                      WHEN BO.BO_QTY IS NOT NULL THEN \n");
            sql.append("                       1 \n");
            sql.append("                    END)) BOXS, --BO总项数 \n");
            sql.append("                SUM(NVL(BO.BO_AMOUNT, 0)) BO_AMOUNT, \n");
            sql.append("                SUM(NVL(BO.TOSAL_QTY, 0)) TOSAL_QTY --BO满足数量 \n");
            sql.append("           FROM (SELECT OM.ORDER_ID, \n");
            sql.append("                        OM.SELLER_CODE, \n");
            sql.append("                        OM.SELLER_NAME, \n");
            sql.append("                        OD.PART_ID, \n");
            sql.append("                        OD.BUY_PRICE, \n");
            sql.append("                        SUM(OD.BUY_AMOUNT) BUY_AMOUNT, \n");
            sql.append("                        SUM(OD.BUY_QTY) BUY_QTY \n");
            sql.append("                   FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DLR_ORDER_MAIN OM \n");
            sql.append("                  WHERE OD.ORDER_ID = OM.ORDER_ID \n");
            sql.append("                    AND OM.SELLER_ID != ").append(Constant.OEM_ACTIVITIES).append(" --不能去 \n");
            sql.append("                    AND OM.SELLER_ID = ").append(dealerId).append(" --供应中心ID \n");
            sql.append("                    AND EXISTS \n");
            sql.append("                  (SELECT 1 \n");
            sql.append("                           FROM TT_PART_SO_MAIN M \n");
            sql.append("                          WHERE M.ORDER_ID = OM.ORDER_ID \n");
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append(")");
            sql.append("                  GROUP BY OM.ORDER_ID, \n");
            sql.append("                           OM.SELLER_CODE, \n");
            sql.append("                           OM.SELLER_NAME, \n");
            sql.append("                           OD.PART_ID, \n");
            sql.append("                           OD.BUY_PRICE \n");
            sql.append("                 UNION ALL \n");
            sql.append("                 SELECT OM.ORDER_ID, \n");
            sql.append("                        OM.SELLER_CODE, \n");
            sql.append("                        OM.SELLER_NAME, \n");
            sql.append("                        OD.PART_ID, \n");
            sql.append("                        OD.BUY_PRICE, \n");
            sql.append("                        SUM(OD.BUY_AMOUNT) BUY_AMOUNT, \n");
            sql.append("                        SUM(OD.BUY_QTY) BUY_QTY \n");
            sql.append("                   FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DLR_ORDER_MAIN OM \n");
            sql.append("                  WHERE OD.ORDER_ID = OM.ORDER_ID \n");
            sql.append("                    AND OM.SELLER_ID != ").append(Constant.OEM_ACTIVITIES).append(" --不能去 \n");
            sql.append("                    AND OM.SELLER_ID = ").append(dealerId).append(" --供应中心ID \n");
            sql.append("                    AND EXISTS \n");
            sql.append("                  (SELECT 1 \n");
            sql.append("                           FROM TT_PART_BO_MAIN BM \n");
            sql.append("                          WHERE BM.ORDER_ID = OM.ORDER_ID \n");
            sql.append("                            AND BM.SO_ID IS NULL \n");
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append(")");
            sql.append("                  GROUP BY OM.ORDER_ID, \n");
            sql.append("                           OM.SELLER_CODE, \n");
            sql.append("                           OM.SELLER_NAME, \n");
            sql.append("                           OD.PART_ID, \n");
            sql.append("                           OD.BUY_PRICE) PO, \n");
            sql.append("                (SELECT SM.ORDER_ID, SD.PART_ID, SUM(SD.SALES_QTY) SALES_QTY \n");
            sql.append("                   FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM \n");
            sql.append("                  WHERE SD.SO_ID = SM.SO_ID \n");
            sql.append("                    AND SM.STATUS = 1 \n");
            sql.append("                    AND SD.STATUS = 1 \n");
            sql.append("                    AND SM.ORDER_ID IS NOT NULL \n");
            sql.append("                    AND SM.SELLER_ID != ").append(Constant.OEM_ACTIVITIES).append(" --不能去 \n");
            sql.append("                    AND SM.SELLER_ID = ").append(dealerId).append(" --供应中心ID \n");
            sql.append("                    AND EXISTS \n");
            sql.append("                  (SELECT 1 \n");
            sql.append("                           FROM TT_PART_SO_MAIN BM \n");
            sql.append("                          WHERE BM.SO_ID = SM.SO_ID \n");
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append(")");
            sql.append("                  GROUP BY SM.ORDER_ID, SD.PART_ID) SO, \n");
            sql.append("                (SELECT BM.ORDER_ID, \n");
            sql.append("                        BD.PART_ID, \n");
            sql.append("                        SUM(BD.BUY_QTY) BUY_QTY, \n");
            sql.append("                        SUM(BD.SALES_QTY) SALES_QTY, \n");
            sql.append("                        SUM(BD.BO_QTY) BO_QTY, \n");
            sql.append("                        SUM(BD.BO_QTY * BD.BUY_PRICE) BO_AMOUNT, \n");
            sql.append("                        SUM(BD.TOSAL_QTY) TOSAL_QTY \n");
            sql.append("                   FROM TT_PART_BO_DTL BD, TT_PART_BO_MAIN BM \n");
            sql.append("                  WHERE BD.BO_ID = BM.BO_ID \n");
            sql.append("                    AND BM.BO_TYPE = 1 \n");
            sql.append("                  GROUP BY BM.ORDER_ID, BD.PART_ID) BO \n");
            sql.append("          WHERE PO.ORDER_ID = SO.ORDER_ID(+) \n");
            sql.append("            AND PO.PART_ID = SO.PART_ID(+) \n");
            sql.append("            AND PO.ORDER_ID = BO.ORDER_ID(+) \n");
            sql.append("            AND PO.PART_ID = BO.PART_ID(+) HAVING \n");
            sql.append("          SUM(NVL(BO.BO_AMOUNT, 0)) > 0 \n");
            sql.append("          GROUP BY PO.PART_ID, PO.SELLER_CODE, PO.SELLER_NAME) BOS \n");
            sql.append("  WHERE BS.PART_ID = BOS.PART_ID \n");
            sql.append("  ORDER BY BOXS DESC \n");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartRateGyzx(RequestWrapper request,
                                                       String dealerId, long whId) throws Exception {
        List<Map<String, Object>> list;
        try {
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//BO时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//BO时间
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT BOS.SELLER_CODE, --供应中心编码 \n");
            sql.append("        BOS.SELLER_NAME, --供应中心 \n");
            sql.append("        BS.PART_OLDCODE, --配件编码 \n");
            sql.append("        BS.PART_CNAME, --配件名称 \n");
            sql.append("        BS.PART_CODE, --配件件号 \n");
            sql.append("        DECODE(BS.PART_TYPE, \n");
            sql.append(Constant.PART_BASE_PART_TYPES_SELF_MADE).append(",\n");
            sql.append("               '自制件', \n");
            sql.append(Constant.PART_BASE_PART_TYPES_PURCHASE).append(",\n");
            sql.append("               '国产件', \n");
            sql.append(Constant.PART_BASE_PART_TYPES_ENTRANCE).append(",\n");
            sql.append("               '进口件') PART_TYPE, --配件类型 \n");
            sql.append("        BS.NORMAL_QTY, --当前可以库存 \n");
            sql.append("        NVL(BOS.BUY_QTY, 0) BUY_QTY, --订货数量 \n");
            sql.append("        NVL(BOS.BUY_AMOUNT, 0) BUY_AMOUNT, --订货金额 \n");
            sql.append("        NVL(BOS.SALES_QTY, 0) SALES_QTY, --已交货数量 \n");
            sql.append("        NVL(BOS.BO_QTY, 0) BO_QTY, --BO数量 \n");
            sql.append("        NVL(BOS.BO_AMOUNT, 0) BO_AMOUNT, --BO金额 \n");
            sql.append("        NVL(BOS.TOSAL_QTY, 0) TOSAL_QTY, --满足数量 \n");
            sql.append("        NVL(BOS.BOXS, 0) BOXS, --BO项数 \n");
            sql.append("        NVL(BOS.XSXS, 0) XSXS --BO金额 \n");
            sql.append("   FROM (SELECT TPD.PART_ID, \n");
            sql.append("                (SELECT NVL(V.NORMAL_QTY, 0) \n");
            sql.append("                   FROM VW_PART_STOCK V \n");
            sql.append("                  WHERE V.PART_ID = TPD.PART_ID \n");
            sql.append("                    AND V.WH_ID = ").append(whId).append(") NORMAL_QTY, \n");
            sql.append("                TPD.PART_TYPE, --配件类型  \n");
            sql.append("                TPD.PART_OLDCODE, --配件编码  \n");
            sql.append("                TPD.PART_CNAME, --配件名称  \n");
            sql.append("                TPD.PART_CODE --配件件号  \n");
            sql.append("           FROM TT_PART_DEFINE TPD WHERE 1=1\n");
            if (!"".equals(partOldCode)) {
                sql.append("         AND UPPER(TPD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCname)) {
                sql.append("         AND TPD.PART_CNAME LIKE '%").append(partCname).append("%'");
            }

            if (!"".equals(partCode)) {
                sql.append("         AND UPPER(TPD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            sql.append("         ) BS, \n");
            sql.append("        (SELECT PO.PART_ID, \n");
            sql.append("                PO.SELLER_CODE, \n");
            sql.append("                PO.SELLER_NAME, \n");
            sql.append("                SUM(PO.BUY_AMOUNT) BUY_AMOUNT, --订货金额 \n");
            sql.append("                SUM(PO.BUY_QTY) BUY_QTY, --订货数量 \n");
            sql.append("                SUM(NVL(SO.SALES_QTY, 0)) SALES_QTY, --已交付数量 \n");
            sql.append("                SUM((CASE \n");
            sql.append("                      WHEN SO.SALES_QTY IS NULL THEN \n");
            sql.append("                       0 \n");
            sql.append("                      WHEN SO.SALES_QTY IS NOT NULL THEN \n");
            sql.append("                       1 \n");
            sql.append("                    END)) XSXS, --销售总项数 \n");
            sql.append("                SUM(NVL(BO.BO_QTY, 0)) BO_QTY, --BO数量 \n");
            sql.append("                SUM((CASE \n");
            sql.append("                      WHEN BO.BO_QTY IS NULL THEN \n");
            sql.append("                       0 \n");
            sql.append("                      WHEN BO.BO_QTY IS NOT NULL THEN \n");
            sql.append("                       1 \n");
            sql.append("                    END)) BOXS, --BO总项数 \n");
            sql.append("                SUM(NVL(BO.BO_AMOUNT, 0)) BO_AMOUNT, \n");
            sql.append("                SUM(NVL(BO.TOSAL_QTY, 0)) TOSAL_QTY --BO满足数量 \n");
            sql.append("           FROM (SELECT OM.ORDER_ID, \n");
            sql.append("                        OM.SELLER_CODE, \n");
            sql.append("                        OM.SELLER_NAME, \n");
            sql.append("                        OD.PART_ID, \n");
            sql.append("                        OD.BUY_PRICE, \n");
            sql.append("                        SUM(OD.BUY_AMOUNT) BUY_AMOUNT, \n");
            sql.append("                        SUM(OD.BUY_QTY) BUY_QTY \n");
            sql.append("                   FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DLR_ORDER_MAIN OM \n");
            sql.append("                  WHERE OD.ORDER_ID = OM.ORDER_ID \n");
            sql.append("                    AND OM.SELLER_ID != ").append(Constant.OEM_ACTIVITIES).append(" --不能去 \n");
            sql.append("                    AND OM.SELLER_ID = ").append(dealerId).append(" --供应中心ID \n");
            sql.append("                    AND EXISTS \n");
            sql.append("                  (SELECT 1 \n");
            sql.append("                           FROM TT_PART_SO_MAIN M \n");
            sql.append("                          WHERE M.ORDER_ID = OM.ORDER_ID \n");
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append(")");
            sql.append("                  GROUP BY OM.ORDER_ID, \n");
            sql.append("                           OM.SELLER_CODE, \n");
            sql.append("                           OM.SELLER_NAME, \n");
            sql.append("                           OD.PART_ID, \n");
            sql.append("                           OD.BUY_PRICE \n");
            sql.append("                 UNION ALL \n");
            sql.append("                 SELECT OM.ORDER_ID, \n");
            sql.append("                        OM.SELLER_CODE, \n");
            sql.append("                        OM.SELLER_NAME, \n");
            sql.append("                        OD.PART_ID, \n");
            sql.append("                        OD.BUY_PRICE, \n");
            sql.append("                        SUM(OD.BUY_AMOUNT) BUY_AMOUNT, \n");
            sql.append("                        SUM(OD.BUY_QTY) BUY_QTY \n");
            sql.append("                   FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DLR_ORDER_MAIN OM \n");
            sql.append("                  WHERE OD.ORDER_ID = OM.ORDER_ID \n");
            sql.append("                    AND OM.SELLER_ID != ").append(Constant.OEM_ACTIVITIES).append(" --不能去 \n");
            sql.append("                    AND OM.SELLER_ID = ").append(dealerId).append(" --供应中心ID \n");
            sql.append("                    AND EXISTS \n");
            sql.append("                  (SELECT 1 \n");
            sql.append("                           FROM TT_PART_BO_MAIN BM \n");
            sql.append("                          WHERE BM.ORDER_ID = OM.ORDER_ID \n");
            sql.append("                            AND BM.SO_ID IS NULL \n");
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append(")");
            sql.append("                  GROUP BY OM.ORDER_ID, \n");
            sql.append("                           OM.SELLER_CODE, \n");
            sql.append("                           OM.SELLER_NAME, \n");
            sql.append("                           OD.PART_ID, \n");
            sql.append("                           OD.BUY_PRICE) PO, \n");
            sql.append("                (SELECT SM.ORDER_ID, SD.PART_ID, SUM(SD.SALES_QTY) SALES_QTY \n");
            sql.append("                   FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM \n");
            sql.append("                  WHERE SD.SO_ID = SM.SO_ID \n");
            sql.append("                    AND SM.STATUS = 1 \n");
            sql.append("                    AND SD.STATUS = 1 \n");
            sql.append("                    AND SM.ORDER_ID IS NOT NULL \n");
            sql.append("                    AND SM.SELLER_ID != ").append(Constant.OEM_ACTIVITIES).append(" --不能去 \n");
            sql.append("                    AND SM.SELLER_ID = ").append(dealerId).append(" --供应中心ID \n");
            sql.append("                    AND EXISTS \n");
            sql.append("                  (SELECT 1 \n");
            sql.append("                           FROM TT_PART_SO_MAIN BM \n");
            sql.append("                          WHERE BM.SO_ID = SM.SO_ID \n");
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(BM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append(")");
            sql.append("                  GROUP BY SM.ORDER_ID, SD.PART_ID) SO, \n");
            sql.append("                (SELECT BM.ORDER_ID, \n");
            sql.append("                        BD.PART_ID, \n");
            sql.append("                        SUM(BD.BUY_QTY) BUY_QTY, \n");
            sql.append("                        SUM(BD.SALES_QTY) SALES_QTY, \n");
            sql.append("                        SUM(BD.BO_QTY) BO_QTY, \n");
            sql.append("                        SUM(BD.BO_QTY * BD.BUY_PRICE) BO_AMOUNT, \n");
            sql.append("                        SUM(BD.TOSAL_QTY) TOSAL_QTY \n");
            sql.append("                   FROM TT_PART_BO_DTL BD, TT_PART_BO_MAIN BM \n");
            sql.append("                  WHERE BD.BO_ID = BM.BO_ID \n");
            sql.append("                    AND BM.BO_TYPE = 1 \n");
            sql.append("                  GROUP BY BM.ORDER_ID, BD.PART_ID) BO \n");
            sql.append("          WHERE PO.ORDER_ID = SO.ORDER_ID(+) \n");
            sql.append("            AND PO.PART_ID = SO.PART_ID(+) \n");
            sql.append("            AND PO.ORDER_ID = BO.ORDER_ID(+) \n");
            sql.append("            AND PO.PART_ID = BO.PART_ID(+) HAVING \n");
            sql.append("          SUM(NVL(BO.BO_AMOUNT, 0)) > 0 \n");
            sql.append("          GROUP BY PO.PART_ID, PO.SELLER_CODE, PO.SELLER_NAME) BOS \n");
            sql.append("  WHERE BS.PART_ID = BOS.PART_ID \n");
            sql.append("  ORDER BY BOXS DESC \n");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public PageResult<Map<String, Object>> queryPartSalesDirectList(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//销售时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//销售时间
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
            String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));

            StringBuffer sql = new StringBuffer("");

            if ("1".equals(radioSelect)) {//按单位汇总
                sql.append(" SELECT TD.DEALER_CODE, --服务商代码 \n");
                sql.append("        TD.DEALER_NAME, --服务商名称 \n");
                sql.append("        NVL(ZF.XS, 0) ZFXS, --直发箱数 \n");
                sql.append("        NVL(ZF.OUTSTOCK_QTY, 0) ZFSL, --直发数量 \n");
                sql.append("        ROUND(NVL(ZF.SALE_AMOUNT, 0) / 1.17, 2) ZFJE, --直发金额 \n");
                sql.append("        NVL(FZF.XS, 0) FZFXS, --非箱数 \n");
                sql.append("        NVL(FZF.OUTSTOCK_QTY, 0) FZFSL, --非直发数量 \n");
                sql.append("        ROUND(NVL(FZF.SALE_AMOUNT, 0) / 1.17, 2) FZFJE, --非直发金额 \n");
                sql.append("        NVL(ZF.XS, 0) + NVL(FZF.XS, 0) XS, --总箱数 \n");
                sql.append("        NVL(ZF.OUTSTOCK_QTY, 0) + NVL(FZF.OUTSTOCK_QTY, 0) SL, --总数量 \n");
                sql.append("        ROUND((NVL(ZF.SALE_AMOUNT, 0) + NVL(FZF.SALE_AMOUNT, 0)) / 1.17, 2) JE, --总金额 \n");
                sql.append("        NVL(WRK.XS, 0) WRKXS, --未入库箱数 \n");
                sql.append("        NVL(WRK.OUTSTOCK_QTY, 0) WRKSL, --未入库数量 \n");
                sql.append("        ROUND(NVL(WRK.SALE_AMOUNT, 0) / 1.17, 2) WRKJE --未入库金额 \n");
                sql.append("   FROM (SELECT OM.DEALER_ID, \n");
                sql.append("                /* OD.PART_OLDCODE,*/ \n");
                sql.append("                ROUND(SUM(OD.OUTSTOCK_QTY / \n");
                sql.append("                          (SELECT STO.MIN_PKG \n");
                sql.append("                             FROM TT_PART_STO_DEFINE STO \n");
                sql.append("                            WHERE STO.PART_ID = OD.PART_ID))) XS, \n");
                sql.append("                SUM(OD.OUTSTOCK_QTY) OUTSTOCK_QTY, --销售数量 \n");
                sql.append("                SUM(OD.SALE_AMOUNT) SALE_AMOUNT --销售金额 \n");
                sql.append("           FROM TT_PART_OUTSTOCK_DTL OD, TT_PART_OUTSTOCK_MAIN OM \n");
                sql.append("          WHERE OM.OUT_ID = OD.OUT_ID \n");
                sql.append("            AND OM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("            AND OM.ORDER_TYPE = 92151004 \n");
                sql.append("            AND OD.PART_OLDCODE LIKE 'Y0%' --不能去 \n");

                if (!"".equals(startDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
                }
                sql.append("          GROUP BY OM.DEALER_ID /*, OD.PART_OLDCODE*/ \n");
                sql.append("         ) ZF, \n");
                sql.append("        (SELECT OM.DEALER_ID, \n");
                sql.append("                /* OD.PART_OLDCODE,*/ \n");
                sql.append("                ROUND(SUM(OD.OUTSTOCK_QTY / \n");
                sql.append("                          (SELECT STO.MIN_PKG \n");
                sql.append("                             FROM TT_PART_STO_DEFINE STO \n");
                sql.append("                            WHERE STO.PART_ID = OD.PART_ID))) XS, \n");
                sql.append("                SUM(OD.OUTSTOCK_QTY) OUTSTOCK_QTY, --销售数量 \n");
                sql.append("                SUM(OD.SALE_AMOUNT) SALE_AMOUNT --销售金额 \n");
                sql.append("           FROM TT_PART_OUTSTOCK_DTL OD, TT_PART_OUTSTOCK_MAIN OM \n");
                sql.append("          WHERE OM.OUT_ID = OD.OUT_ID \n");
                sql.append("            AND OM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("            AND OM.ORDER_TYPE != 92151004 \n");
                sql.append("            AND OD.PART_OLDCODE LIKE 'Y0%' --不能去 \n");

                if (!"".equals(startDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
                }
                sql.append("          GROUP BY OM.DEALER_ID /*, OD.PART_OLDCODE*/ \n");
                sql.append("         ) FZF, \n");
                sql.append("         \n");
                sql.append("        (SELECT SM.DEALER_ID, \n");
                sql.append("                /*SD.PART_OLDCODE,*/ \n");
                sql.append("                SUM(SD.SALES_QTY / \n");
                sql.append("                    (SELECT STO.MIN_PKG \n");
                sql.append("                       FROM TT_PART_STO_DEFINE STO \n");
                sql.append("                      WHERE STO.PART_ID = SD.PART_ID)) XS, \n");
                sql.append("                SUM(SD.SALES_QTY) OUTSTOCK_QTY, --销售数量 \n");
                sql.append("                SUM(SD.BUY_AMOUNT) SALE_AMOUNT --销售金额 \n");
                sql.append("           FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM \n");
                sql.append("          WHERE SD.SO_ID = SM.SO_ID \n");
                sql.append("            AND SM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("            AND SM.ORDER_TYPE = 92151004 \n");
                sql.append("            AND SM.STATE <> 92401003 \n");
                if (!"".equals(partOldCode)) {
                    sql.append("         AND UPPER(SD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                sql.append("            AND NOT EXISTS (SELECT 1 \n");
                sql.append("                   FROM TT_PART_OUTSTOCK_MAIN OM \n");
                sql.append("                  WHERE OM.SO_ID = SM.SO_ID) \n");
                sql.append("          GROUP BY SM.DEALER_ID /*, SD.PART_OLDCODE*/ \n");
                sql.append("         ) WRK, \n");
                sql.append("        TM_DEALER TD \n");
                sql.append("  WHERE TD.DEALER_ID = ZF.DEALER_ID(+) \n");
                sql.append("    AND TD.DEALER_ID = FZF.DEALER_ID(+) \n");
                sql.append("    AND TD.DEALER_ID = WRK.DEALER_ID(+) \n");
                sql.append("    AND TD.DEALER_TYPE = 10771002 \n");
                sql.append("    AND TD.DEALER_LEVEL = 10851001 \n");
                sql.append("    AND NVL(ZF.XS, 0) + NVL(FZF.XS, 0) + NVL(WRK.XS, 0) <> 0 \n");
                if (!"".equals(dealerName)) {
                    sql.append("         AND TD.DEALER_NAME LIKE '%").append(dealerName).append("%'");
                }
            } else if ("2".equals(radioSelect)) {//按品种汇总
                sql.append(" SELECT TD.PART_OLDCODE, --配件编码 \n");
                sql.append("        TD.PART_CNAME, --配件名称 \n");
                sql.append("        TD.PART_CODE, --配件件号 \n");
                sql.append("        --ROUND(SP.SALE_PRICE1 / 1.17, 4) SALE_PRICE1, --当前无税单价 \n");
                sql.append("        --NVL(ZF.XS, 0) ZFXS, --直发箱数 \n");
                sql.append("        NVL(ZF.OUTSTOCK_QTY, 0) ZFSL, --直发数量 \n");
                sql.append("        ROUND(NVL(ZF.SALE_AMOUNT, 0) / 1.17, 2) ZFJE, --直发金额 \n");
                sql.append("        --NVL(FZF.XS, 0) FZFXS, --正常箱数 \n");
                sql.append("        NVL(FZF.OUTSTOCK_QTY, 0) FZFSL, --正常数量 \n");
                sql.append("        ROUND(NVL(FZF.SALE_AMOUNT, 0) / 1.17, 2) FZFJE, --正常金额 \n");
                sql.append("        --NVL(ZF.XS, 0) + NVL(FZF.XS, 0) XS, --总箱数 \n");
                sql.append("        NVL(ZF.OUTSTOCK_QTY, 0) + NVL(FZF.OUTSTOCK_QTY, 0) SL, --总数量 \n");
                sql.append("        ROUND((NVL(ZF.SALE_AMOUNT, 0) + NVL(FZF.SALE_AMOUNT, 0)) / 1.17, 2) JE, --总金额 \n");
                sql.append("        --NVL(WRK.XS, 0) WRKXS, --未入库箱数 \n");
                sql.append("        NVL(WRK.OUTSTOCK_QTY, 0) WRKSL, --未入库数量 \n");
                sql.append("        ROUND(NVL(WRK.SALE_AMOUNT, 0) / 1.17, 2) WRKJE --未入库金额 \n");
                sql.append("   FROM (SELECT /*OM.DEALER_ID,*/ \n");
                sql.append("          OD.PART_ID, \n");
                sql.append("          OD.SALE_PRICE, --当前服务商采购价 \n");
                sql.append("          /* ROUND(SUM(OD.OUTSTOCK_QTY / \n");
                sql.append("          (SELECT STO.MIN_PKG \n");
                sql.append("             FROM TT_PART_STO_DEFINE STO \n");
                sql.append("            WHERE STO.PART_ID = OD.PART_ID))) XS,*/ \n");
                sql.append("          SUM(OD.OUTSTOCK_QTY) OUTSTOCK_QTY, --销售数量 \n");
                sql.append("          SUM(OD.SALE_AMOUNT) SALE_AMOUNT --销售金额 \n");
                sql.append("           FROM TT_PART_OUTSTOCK_DTL OD, TT_PART_OUTSTOCK_MAIN OM \n");
                sql.append("          WHERE OM.OUT_ID = OD.OUT_ID \n");
                sql.append("            AND OM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("            AND OM.ORDER_TYPE = 92151004 \n");
                sql.append("            AND OD.PART_OLDCODE LIKE 'Y0%' --不能去 \n");

                if (!"".equals(startDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(dealerName)) {
                    sql.append("         AND OM.DEALER_NAME LIKE '%").append(dealerName).append("%'");
                }
                sql.append("          GROUP BY /*OM.DEALER_ID, */ OD.PART_ID, OD.SALE_PRICE) ZF, \n");
                sql.append("        (SELECT /*OM.DEALER_ID,*/ \n");
                sql.append("          OD.PART_ID, \n");
                sql.append("          OD.SALE_PRICE, \n");
                sql.append("          /*               SUM(OD.OUTSTOCK_QTY / \n");
                sql.append("          (SELECT STO.MIN_PKG \n");
                sql.append("             FROM TT_PART_STO_DEFINE STO \n");
                sql.append("            WHERE STO.PART_ID = OD.PART_ID)) XS,*/ \n");
                sql.append("          SUM(OD.OUTSTOCK_QTY) OUTSTOCK_QTY, --销售数量 \n");
                sql.append("          SUM(OD.SALE_AMOUNT) SALE_AMOUNT --销售金额 \n");
                sql.append("           FROM TT_PART_OUTSTOCK_DTL OD, TT_PART_OUTSTOCK_MAIN OM \n");
                sql.append("          WHERE OM.OUT_ID = OD.OUT_ID \n");
                sql.append("            AND OM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("            AND OM.ORDER_TYPE != 92151004 \n");
                sql.append("            AND OD.PART_OLDCODE LIKE 'Y0%' --不能去 \n");

                if (!"".equals(startDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(dealerName)) {
                    sql.append("         AND OM.DEALER_NAME LIKE '%").append(dealerName).append("%'");
                }
                sql.append("          GROUP BY /*OM.DEALER_ID ,*/ OD.PART_ID, OD.SALE_PRICE) FZF, \n");
                sql.append("        (SELECT /*SM.DEALER_ID,*/ \n");
                sql.append("          SD.PART_ID, \n");
                sql.append("          SD.BUY_PRICE SALE_PRICE, \n");
                sql.append("          /* ROUND(SUM(SD.SALES_QTY / \n");
                sql.append("          (SELECT STO.MIN_PKG \n");
                sql.append("             FROM TT_PART_STO_DEFINE STO \n");
                sql.append("            WHERE STO.PART_ID = SD.PART_ID))) XS,*/ \n");
                sql.append("          SUM(SD.SALES_QTY) OUTSTOCK_QTY, --销售数量 \n");
                sql.append("          SUM(SD.BUY_AMOUNT) SALE_AMOUNT --销售金额 \n");
                sql.append("           FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM \n");
                sql.append("          WHERE SD.SO_ID = SM.SO_ID \n");
                sql.append("            AND SM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("            AND SM.ORDER_TYPE = 92151004 \n");
                sql.append("            AND SM.STATE <> 92401003 \n");

                if (!"".equals(dealerName)) {
                    sql.append("         AND SM.DEALER_NAME LIKE '%").append(dealerName).append("%'");
                }
                sql.append("            AND NOT EXISTS (SELECT 1 \n");
                sql.append("                   FROM TT_PART_OUTSTOCK_MAIN OM \n");
                sql.append("                  WHERE OM.SO_ID = SM.SO_ID) \n");
                sql.append("          GROUP BY /*SM.DEALER_ID , */ SD.PART_ID, SD.BUY_PRICE) WRK, \n");
                sql.append("        TT_PART_DEFINE TD, \n");
                sql.append("        TT_PART_SALES_PRICE SP \n");
                sql.append("  WHERE TD.PART_ID = SP.PART_ID \n");
                sql.append("    AND TD.PART_ID = ZF.PART_ID(+) \n");
                sql.append("    AND TD.PART_ID = FZF.PART_ID(+) \n");
                sql.append("    AND TD.PART_ID = WRK.PART_ID(+) \n");
                sql.append("    AND TD.PART_OLDCODE LIKE 'Y0%' \n");
                if (!"".equals(partOldCode)) {
                    sql.append("         AND UPPER(TD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                sql.append(" 	 ORDER BY td.part_oldcode  \n");
                sql.append(" /*AND NVL(ZF.XS, 0) + NVL(FZF.XS, 0) + NVL(WRK.XS, 0) <> 0*/ \n");
            } else if ("3".equals(radioSelect)) {//已销售明细
                sql.append(" SELECT TD.DEALER_ID, \n");
                sql.append("        TD.DEALER_CODE, --服务商编码 \n");
                sql.append("        TD.DEALER_NAME, --服务商 \n");
                sql.append("        TP.PART_OLDCODE, --配件编码 \n");
                sql.append("        TP.PART_CNAME, --配件名称 \n");
                sql.append("        TP.PART_CODE, --配件件号 \n");
                sql.append("        OM.CREATE_DATE, --销售日期 \n");
                sql.append("        '直发销售' AS STYPE, --销售类型 \n");
                sql.append("        /* ROUND(SUM(OD.OUTSTOCK_QTY / \n");
                sql.append("        (SELECT STO.MIN_PKG \n");
                sql.append("           FROM TT_PART_STO_DEFINE STO \n");
                sql.append("          WHERE STO.PART_ID = OD.PART_ID))) XS,*/ \n");
                sql.append("        OD.SALE_PRICE, -- 含税单价 \n");
                sql.append("        OD.OUTSTOCK_QTY OUTSTOCK_QTY, --销售数量 \n");
                sql.append("        OD.SALE_AMOUNT  SALE_AMOUNT --销售金额(含税） \n");
                sql.append("   FROM TT_PART_OUTSTOCK_DTL  OD, \n");
                sql.append("        TT_PART_OUTSTOCK_MAIN OM, \n");
                sql.append("        TT_PART_DEFINE        TP, \n");
                sql.append("        TM_DEALER             TD \n");
                sql.append("  WHERE OM.OUT_ID = OD.OUT_ID \n");
                sql.append("    AND OD.PART_ID = TP.PART_ID \n");
                sql.append("    AND OM.DEALER_ID = TD.DEALER_ID \n");
                sql.append("    AND OM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("    AND OM.ORDER_TYPE = 92151004 \n");
                sql.append("    AND OD.PART_OLDCODE LIKE 'Y0%' --不能去 \n");
                if (!"".equals(startDate)) {
                    sql.append(" AND TO_DATE(OM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endDate)) {
                    sql.append(" AND TO_DATE(OM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(dealerName)) {
                    sql.append("         AND TD.DEALER_NAME LIKE '%").append(dealerName).append("%'");
                }
                if (!"".equals(partOldCode)) {
                    sql.append("         AND UPPER(OD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                sql.append(" UNION ALL \n");
                sql.append(" SELECT TD.DEALER_ID, \n");
                sql.append("        TD.DEALER_CODE, \n");
                sql.append("        TD.DEALER_NAME, \n");
                sql.append("        TP.PART_OLDCODE, \n");
                sql.append("        TP.PART_CNAME, \n");
                sql.append("        TP.PART_CODE, \n");
                sql.append("        OM.CREATE_DATE, \n");
                sql.append("        '正常销售' AS STYPE, \n");
                sql.append("        OD.SALE_PRICE, \n");
                sql.append("        /* SUM(OD.OUTSTOCK_QTY / \n");
                sql.append("        (SELECT STO.MIN_PKG \n");
                sql.append("           FROM TT_PART_STO_DEFINE STO \n");
                sql.append("          WHERE STO.PART_ID = OD.PART_ID)) XS,*/ \n");
                sql.append("        OD.OUTSTOCK_QTY OUTSTOCK_QTY, --销售数量 \n");
                sql.append("        OD.SALE_AMOUNT  SALE_AMOUNT --销售金额 \n");
                sql.append("   FROM TT_PART_OUTSTOCK_DTL  OD, \n");
                sql.append("        TT_PART_OUTSTOCK_MAIN OM, \n");
                sql.append("        TT_PART_DEFINE        TP, \n");
                sql.append("        TM_DEALER             TD \n");
                sql.append("  WHERE OM.OUT_ID = OD.OUT_ID \n");
                sql.append("    AND OD.PART_ID = TP.PART_ID \n");
                sql.append("    AND OM.DEALER_ID = TD.DEALER_ID \n");
                sql.append("    AND OM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("    AND OM.ORDER_TYPE != 92151004 \n");
                sql.append("    AND OD.PART_OLDCODE LIKE 'Y0%' --不能去 \n");
                if (!"".equals(startDate)) {
                    sql.append(" AND TO_DATE(OM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endDate)) {
                    sql.append(" AND TO_DATE(OM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(dealerName)) {
                    sql.append("         AND TD.DEALER_NAME LIKE '%").append(dealerName).append("%'");
                }
                if (!"".equals(partOldCode)) {
                    sql.append("         AND UPPER(OD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
            } else {//服务商未点入库明细
                sql.append(" SELECT SM.DEALER_ID, \n");
                sql.append("        TD.DEALER_CODE, --服务商代码 \n");
                sql.append("        TD.DEALER_NAME, --服务商 \n");
                sql.append("        TP.PART_OLDCODE, --配件代码 \n");
                sql.append("        TP.PART_CNAME, --配件名称 \n");
                sql.append("        TP.PART_CODE, --配件件号 \n");
                sql.append("        SM.CREATE_DATE, --审核通过日期 \n");
                sql.append("        /*  ROUND(SUM(SD.SALES_QTY / \n");
                sql.append("        (SELECT STO.MIN_PKG \n");
                sql.append("           FROM TT_PART_STO_DEFINE STO \n");
                sql.append("          WHERE STO.PART_ID = SD.PART_ID))) XS,*/ \n");
                sql.append("        SD.SALES_QTY  OUTSTOCK_QTY, --销售数量 \n");
                sql.append("        SD.BUY_AMOUNT SALE_AMOUNT --销售金额 \n");
                sql.append("   FROM TT_PART_SO_DTL  SD, \n");
                sql.append("        TT_PART_SO_MAIN SM, \n");
                sql.append("        TT_PART_DEFINE  TP, \n");
                sql.append("        TM_DEALER       TD \n");
                sql.append("  WHERE SD.SO_ID = SM.SO_ID \n");
                sql.append("    AND SD.PART_ID = TP.PART_ID \n");
                sql.append("    AND SM.DEALER_ID = TD.DEALER_ID \n");
                sql.append("    AND SM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("    AND SM.ORDER_TYPE = 92151004 \n");
                sql.append("    AND SM.STATE <> 92401003 \n");
                if (!"".equals(startDate)) {
                    sql.append(" AND TO_DATE(SM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endDate)) {
                    sql.append(" AND TO_DATE(SM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(dealerName)) {
                    sql.append("         AND TD.DEALER_NAME LIKE '%").append(dealerName).append("%'");
                }
                if (!"".equals(partOldCode)) {
                    sql.append("         AND UPPER(SD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                sql.append("    AND NOT EXISTS \n");
                sql.append("  (SELECT 1 FROM TT_PART_OUTSTOCK_MAIN OM WHERE OM.SO_ID = SM.SO_ID) \n");
            }

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartSalesDirect(
            RequestWrapper request, AclUserBean logonUser) throws Exception {
        List<Map<String, Object>> list;
        try {
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//销售时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//销售时间
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
            String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));

            StringBuffer sql = new StringBuffer("");

            if ("1".equals(radioSelect)) {//按单位汇总
                sql.append(" SELECT TD.DEALER_CODE, --服务商代码 \n");
                sql.append("        TD.DEALER_NAME, --服务商名称 \n");
                sql.append("        NVL(ZF.XS, 0) ZFXS, --直发箱数 \n");
                sql.append("        NVL(ZF.OUTSTOCK_QTY, 0) ZFSL, --直发数量 \n");
                sql.append("        ROUND(NVL(ZF.SALE_AMOUNT, 0) / 1.17, 2) ZFJE, --直发金额 \n");
                sql.append("        NVL(FZF.XS, 0) FZFXS, --非箱数 \n");
                sql.append("        NVL(FZF.OUTSTOCK_QTY, 0) FZFSL, --非直发数量 \n");
                sql.append("        ROUND(NVL(FZF.SALE_AMOUNT, 0) / 1.17, 2) FZFJE, --非直发金额 \n");
                sql.append("        NVL(ZF.XS, 0) + NVL(FZF.XS, 0) XS, --总箱数 \n");
                sql.append("        NVL(ZF.OUTSTOCK_QTY, 0) + NVL(FZF.OUTSTOCK_QTY, 0) SL, --总数量 \n");
                sql.append("        ROUND((NVL(ZF.SALE_AMOUNT, 0) + NVL(FZF.SALE_AMOUNT, 0)) / 1.17, 2) JE, --总金额 \n");
                sql.append("        NVL(WRK.XS, 0) WRKXS, --未入库箱数 \n");
                sql.append("        NVL(WRK.OUTSTOCK_QTY, 0) WRKSL, --未入库数量 \n");
                sql.append("        ROUND(NVL(WRK.SALE_AMOUNT, 0) / 1.17, 2) WRKJE --未入库金额 \n");
                sql.append("   FROM (SELECT OM.DEALER_ID, \n");
                sql.append("                /* OD.PART_OLDCODE,*/ \n");
                sql.append("                ROUND(SUM(OD.OUTSTOCK_QTY / \n");
                sql.append("                          (SELECT STO.MIN_PKG \n");
                sql.append("                             FROM TT_PART_STO_DEFINE STO \n");
                sql.append("                            WHERE STO.PART_ID = OD.PART_ID))) XS, \n");
                sql.append("                SUM(OD.OUTSTOCK_QTY) OUTSTOCK_QTY, --销售数量 \n");
                sql.append("                SUM(OD.SALE_AMOUNT) SALE_AMOUNT --销售金额 \n");
                sql.append("           FROM TT_PART_OUTSTOCK_DTL OD, TT_PART_OUTSTOCK_MAIN OM \n");
                sql.append("          WHERE OM.OUT_ID = OD.OUT_ID \n");
                sql.append("            AND OM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("            AND OM.ORDER_TYPE = 92151004 \n");
                sql.append("            AND OD.PART_OLDCODE LIKE 'Y0%' --不能去 \n");

                if (!"".equals(startDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
                }
                sql.append("          GROUP BY OM.DEALER_ID /*, OD.PART_OLDCODE*/ \n");
                sql.append("         ) ZF, \n");
                sql.append("        (SELECT OM.DEALER_ID, \n");
                sql.append("                /* OD.PART_OLDCODE,*/ \n");
                sql.append("                ROUND(SUM(OD.OUTSTOCK_QTY / \n");
                sql.append("                          (SELECT STO.MIN_PKG \n");
                sql.append("                             FROM TT_PART_STO_DEFINE STO \n");
                sql.append("                            WHERE STO.PART_ID = OD.PART_ID))) XS, \n");
                sql.append("                SUM(OD.OUTSTOCK_QTY) OUTSTOCK_QTY, --销售数量 \n");
                sql.append("                SUM(OD.SALE_AMOUNT) SALE_AMOUNT --销售金额 \n");
                sql.append("           FROM TT_PART_OUTSTOCK_DTL OD, TT_PART_OUTSTOCK_MAIN OM \n");
                sql.append("          WHERE OM.OUT_ID = OD.OUT_ID \n");
                sql.append("            AND OM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("            AND OM.ORDER_TYPE != 92151004 \n");
                sql.append("            AND OD.PART_OLDCODE LIKE 'Y0%' --不能去 \n");

                if (!"".equals(startDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
                }
                sql.append("          GROUP BY OM.DEALER_ID /*, OD.PART_OLDCODE*/ \n");
                sql.append("         ) FZF, \n");
                sql.append("         \n");
                sql.append("        (SELECT SM.DEALER_ID, \n");
                sql.append("                /*SD.PART_OLDCODE,*/ \n");
                sql.append("                SUM(SD.SALES_QTY / \n");
                sql.append("                    (SELECT STO.MIN_PKG \n");
                sql.append("                       FROM TT_PART_STO_DEFINE STO \n");
                sql.append("                      WHERE STO.PART_ID = SD.PART_ID)) XS, \n");
                sql.append("                SUM(SD.SALES_QTY) OUTSTOCK_QTY, --销售数量 \n");
                sql.append("                SUM(SD.BUY_AMOUNT) SALE_AMOUNT --销售金额 \n");
                sql.append("           FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM \n");
                sql.append("          WHERE SD.SO_ID = SM.SO_ID \n");
                sql.append("            AND SM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("            AND SM.ORDER_TYPE = 92151004 \n");
                sql.append("            AND SM.STATE <> 92401003 \n");
                if (!"".equals(partOldCode)) {
                    sql.append("         AND UPPER(SD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                sql.append("            AND NOT EXISTS (SELECT 1 \n");
                sql.append("                   FROM TT_PART_OUTSTOCK_MAIN OM \n");
                sql.append("                  WHERE OM.SO_ID = SM.SO_ID) \n");
                sql.append("          GROUP BY SM.DEALER_ID /*, SD.PART_OLDCODE*/ \n");
                sql.append("         ) WRK, \n");
                sql.append("        TM_DEALER TD \n");
                sql.append("  WHERE TD.DEALER_ID = ZF.DEALER_ID(+) \n");
                sql.append("    AND TD.DEALER_ID = FZF.DEALER_ID(+) \n");
                sql.append("    AND TD.DEALER_ID = WRK.DEALER_ID(+) \n");
                sql.append("    AND TD.DEALER_TYPE = 10771002 \n");
                sql.append("    AND TD.DEALER_LEVEL = 10851001 \n");
                sql.append("    AND NVL(ZF.XS, 0) + NVL(FZF.XS, 0) + NVL(WRK.XS, 0) <> 0 \n");
                if (!"".equals(dealerName)) {
                    sql.append("         AND TD.DEALER_NAME LIKE '%").append(dealerName).append("%'");
                }
            } else if ("2".equals(radioSelect)) {//按品种汇总
                sql.append(" SELECT TD.PART_OLDCODE, --配件编码 \n");
                sql.append("        TD.PART_CNAME, --配件名称 \n");
                sql.append("        TD.PART_CODE, --配件件号 \n");
                sql.append("        --ROUND(SP.SALE_PRICE1 / 1.17, 4) SALE_PRICE1, --当前无税单价 \n");
                sql.append("        --NVL(ZF.XS, 0) ZFXS, --直发箱数 \n");
                sql.append("        NVL(ZF.OUTSTOCK_QTY, 0) ZFSL, --直发数量 \n");
                sql.append("        ROUND(NVL(ZF.SALE_AMOUNT, 0) / 1.17, 2) ZFJE, --直发金额 \n");
                sql.append("        --NVL(FZF.XS, 0) FZFXS, --正常箱数 \n");
                sql.append("        NVL(FZF.OUTSTOCK_QTY, 0) FZFSL, --正常数量 \n");
                sql.append("        ROUND(NVL(FZF.SALE_AMOUNT, 0) / 1.17, 2) FZFJE, --正常金额 \n");
                sql.append("        --NVL(ZF.XS, 0) + NVL(FZF.XS, 0) XS, --总箱数 \n");
                sql.append("        NVL(ZF.OUTSTOCK_QTY, 0) + NVL(FZF.OUTSTOCK_QTY, 0) SL, --总数量 \n");
                sql.append("        ROUND((NVL(ZF.SALE_AMOUNT, 0) + NVL(FZF.SALE_AMOUNT, 0)) / 1.17, 2) JE, --总金额 \n");
                sql.append("        --NVL(WRK.XS, 0) WRKXS, --未入库箱数 \n");
                sql.append("        NVL(WRK.OUTSTOCK_QTY, 0) WRKSL, --未入库数量 \n");
                sql.append("        ROUND(NVL(WRK.SALE_AMOUNT, 0) / 1.17, 2) WRKJE --未入库金额 \n");
                sql.append("   FROM (SELECT /*OM.DEALER_ID,*/ \n");
                sql.append("          OD.PART_ID, \n");
                sql.append("          OD.SALE_PRICE, --当前服务商采购价 \n");
                sql.append("          /* ROUND(SUM(OD.OUTSTOCK_QTY / \n");
                sql.append("          (SELECT STO.MIN_PKG \n");
                sql.append("             FROM TT_PART_STO_DEFINE STO \n");
                sql.append("            WHERE STO.PART_ID = OD.PART_ID))) XS,*/ \n");
                sql.append("          SUM(OD.OUTSTOCK_QTY) OUTSTOCK_QTY, --销售数量 \n");
                sql.append("          SUM(OD.SALE_AMOUNT) SALE_AMOUNT --销售金额 \n");
                sql.append("           FROM TT_PART_OUTSTOCK_DTL OD, TT_PART_OUTSTOCK_MAIN OM \n");
                sql.append("          WHERE OM.OUT_ID = OD.OUT_ID \n");
                sql.append("            AND OM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("            AND OM.ORDER_TYPE = 92151004 \n");
                sql.append("            AND OD.PART_OLDCODE LIKE 'Y0%' --不能去 \n");

                if (!"".equals(startDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(dealerName)) {
                    sql.append("         AND OM.DEALER_NAME LIKE '%").append(dealerName).append("%'");
                }
                sql.append("          GROUP BY /*OM.DEALER_ID, */ OD.PART_ID, OD.SALE_PRICE) ZF, \n");
                sql.append("        (SELECT /*OM.DEALER_ID,*/ \n");
                sql.append("          OD.PART_ID, \n");
                sql.append("          OD.SALE_PRICE, \n");
                sql.append("          /*               SUM(OD.OUTSTOCK_QTY / \n");
                sql.append("          (SELECT STO.MIN_PKG \n");
                sql.append("             FROM TT_PART_STO_DEFINE STO \n");
                sql.append("            WHERE STO.PART_ID = OD.PART_ID)) XS,*/ \n");
                sql.append("          SUM(OD.OUTSTOCK_QTY) OUTSTOCK_QTY, --销售数量 \n");
                sql.append("          SUM(OD.SALE_AMOUNT) SALE_AMOUNT --销售金额 \n");
                sql.append("           FROM TT_PART_OUTSTOCK_DTL OD, TT_PART_OUTSTOCK_MAIN OM \n");
                sql.append("          WHERE OM.OUT_ID = OD.OUT_ID \n");
                sql.append("            AND OM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("            AND OM.ORDER_TYPE != 92151004 \n");
                sql.append("            AND OD.PART_OLDCODE LIKE 'Y0%' --不能去 \n");

                if (!"".equals(startDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endDate)) {
                    sql.append(" AND TO_DATE(OD.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(dealerName)) {
                    sql.append("         AND OM.DEALER_NAME LIKE '%").append(dealerName).append("%'");
                }
                sql.append("          GROUP BY /*OM.DEALER_ID ,*/ OD.PART_ID, OD.SALE_PRICE) FZF, \n");
                sql.append("        (SELECT /*SM.DEALER_ID,*/ \n");
                sql.append("          SD.PART_ID, \n");
                sql.append("          SD.BUY_PRICE SALE_PRICE, \n");
                sql.append("          /* ROUND(SUM(SD.SALES_QTY / \n");
                sql.append("          (SELECT STO.MIN_PKG \n");
                sql.append("             FROM TT_PART_STO_DEFINE STO \n");
                sql.append("            WHERE STO.PART_ID = SD.PART_ID))) XS,*/ \n");
                sql.append("          SUM(SD.SALES_QTY) OUTSTOCK_QTY, --销售数量 \n");
                sql.append("          SUM(SD.BUY_AMOUNT) SALE_AMOUNT --销售金额 \n");
                sql.append("           FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM \n");
                sql.append("          WHERE SD.SO_ID = SM.SO_ID \n");
                sql.append("            AND SM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("            AND SM.ORDER_TYPE = 92151004 \n");
                sql.append("            AND SM.STATE <> 92401003 \n");

                if (!"".equals(dealerName)) {
                    sql.append("         AND SM.DEALER_NAME LIKE '%").append(dealerName).append("%'");
                }
                sql.append("            AND NOT EXISTS (SELECT 1 \n");
                sql.append("                   FROM TT_PART_OUTSTOCK_MAIN OM \n");
                sql.append("                  WHERE OM.SO_ID = SM.SO_ID) \n");
                sql.append("          GROUP BY /*SM.DEALER_ID , */ SD.PART_ID, SD.BUY_PRICE) WRK, \n");
                sql.append("        TT_PART_DEFINE TD, \n");
                sql.append("        TT_PART_SALES_PRICE SP \n");
                sql.append("  WHERE TD.PART_ID = SP.PART_ID \n");
                sql.append("    AND TD.PART_ID = ZF.PART_ID(+) \n");
                sql.append("    AND TD.PART_ID = FZF.PART_ID(+) \n");
                sql.append("    AND TD.PART_ID = WRK.PART_ID(+) \n");
                sql.append("    AND TD.PART_OLDCODE LIKE 'Y0%' \n");
                if (!"".equals(partOldCode)) {
                    sql.append("         AND UPPER(TD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                sql.append(" 	 ORDER BY td.part_oldcode  \n");
                sql.append(" /*AND NVL(ZF.XS, 0) + NVL(FZF.XS, 0) + NVL(WRK.XS, 0) <> 0*/ \n");
            } else if ("3".equals(radioSelect)) {//已销售明细
                sql.append(" SELECT TD.DEALER_ID, \n");
                sql.append("        TD.DEALER_CODE, --服务商编码 \n");
                sql.append("        TD.DEALER_NAME, --服务商 \n");
                sql.append("        TP.PART_OLDCODE, --配件编码 \n");
                sql.append("        TP.PART_CNAME, --配件名称 \n");
                sql.append("        TP.PART_CODE, --配件件号 \n");
                sql.append("        OM.CREATE_DATE, --销售日期 \n");
                sql.append("        '直发销售' AS STYPE, --销售类型 \n");
                sql.append("        /* ROUND(SUM(OD.OUTSTOCK_QTY / \n");
                sql.append("        (SELECT STO.MIN_PKG \n");
                sql.append("           FROM TT_PART_STO_DEFINE STO \n");
                sql.append("          WHERE STO.PART_ID = OD.PART_ID))) XS,*/ \n");
                sql.append("        OD.SALE_PRICE, -- 含税单价 \n");
                sql.append("        OD.OUTSTOCK_QTY OUTSTOCK_QTY, --销售数量 \n");
                sql.append("        OD.SALE_AMOUNT  SALE_AMOUNT --销售金额(含税） \n");
                sql.append("   FROM TT_PART_OUTSTOCK_DTL  OD, \n");
                sql.append("        TT_PART_OUTSTOCK_MAIN OM, \n");
                sql.append("        TT_PART_DEFINE        TP, \n");
                sql.append("        TM_DEALER             TD \n");
                sql.append("  WHERE OM.OUT_ID = OD.OUT_ID \n");
                sql.append("    AND OD.PART_ID = TP.PART_ID \n");
                sql.append("    AND OM.DEALER_ID = TD.DEALER_ID \n");
                sql.append("    AND OM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("    AND OM.ORDER_TYPE = 92151004 \n");
                sql.append("    AND OD.PART_OLDCODE LIKE 'Y0%' --不能去 \n");
                if (!"".equals(startDate)) {
                    sql.append(" AND TO_DATE(OM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endDate)) {
                    sql.append(" AND TO_DATE(OM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(dealerName)) {
                    sql.append("         AND TD.DEALER_NAME LIKE '%").append(dealerName).append("%'");
                }
                if (!"".equals(partOldCode)) {
                    sql.append("         AND UPPER(OD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                sql.append(" UNION ALL \n");
                sql.append(" SELECT TD.DEALER_ID, \n");
                sql.append("        TD.DEALER_CODE, \n");
                sql.append("        TD.DEALER_NAME, \n");
                sql.append("        TP.PART_OLDCODE, \n");
                sql.append("        TP.PART_CNAME, \n");
                sql.append("        TP.PART_CODE, \n");
                sql.append("        OM.CREATE_DATE, \n");
                sql.append("        '正常销售' AS STYPE, \n");
                sql.append("        OD.SALE_PRICE, \n");
                sql.append("        /* SUM(OD.OUTSTOCK_QTY / \n");
                sql.append("        (SELECT STO.MIN_PKG \n");
                sql.append("           FROM TT_PART_STO_DEFINE STO \n");
                sql.append("          WHERE STO.PART_ID = OD.PART_ID)) XS,*/ \n");
                sql.append("        OD.OUTSTOCK_QTY OUTSTOCK_QTY, --销售数量 \n");
                sql.append("        OD.SALE_AMOUNT  SALE_AMOUNT --销售金额 \n");
                sql.append("   FROM TT_PART_OUTSTOCK_DTL  OD, \n");
                sql.append("        TT_PART_OUTSTOCK_MAIN OM, \n");
                sql.append("        TT_PART_DEFINE        TP, \n");
                sql.append("        TM_DEALER             TD \n");
                sql.append("  WHERE OM.OUT_ID = OD.OUT_ID \n");
                sql.append("    AND OD.PART_ID = TP.PART_ID \n");
                sql.append("    AND OM.DEALER_ID = TD.DEALER_ID \n");
                sql.append("    AND OM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("    AND OM.ORDER_TYPE != 92151004 \n");
                sql.append("    AND OD.PART_OLDCODE LIKE 'Y0%' --不能去 \n");
                if (!"".equals(startDate)) {
                    sql.append(" AND TO_DATE(OM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endDate)) {
                    sql.append(" AND TO_DATE(OM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(dealerName)) {
                    sql.append("         AND TD.DEALER_NAME LIKE '%").append(dealerName).append("%'");
                }
                if (!"".equals(partOldCode)) {
                    sql.append("         AND UPPER(OD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
            } else {//服务商未点入库明细
                sql.append(" SELECT SM.DEALER_ID, \n");
                sql.append("        TD.DEALER_CODE, --服务商代码 \n");
                sql.append("        TD.DEALER_NAME, --服务商 \n");
                sql.append("        TP.PART_OLDCODE, --配件代码 \n");
                sql.append("        TP.PART_CNAME, --配件名称 \n");
                sql.append("        TP.PART_CODE, --配件件号 \n");
                sql.append("        SM.CREATE_DATE, --审核通过日期 \n");
                sql.append("        /*  ROUND(SUM(SD.SALES_QTY / \n");
                sql.append("        (SELECT STO.MIN_PKG \n");
                sql.append("           FROM TT_PART_STO_DEFINE STO \n");
                sql.append("          WHERE STO.PART_ID = SD.PART_ID))) XS,*/ \n");
                sql.append("        SD.SALES_QTY  OUTSTOCK_QTY, --销售数量 \n");
                sql.append("        SD.BUY_AMOUNT SALE_AMOUNT --销售金额 \n");
                sql.append("   FROM TT_PART_SO_DTL  SD, \n");
                sql.append("        TT_PART_SO_MAIN SM, \n");
                sql.append("        TT_PART_DEFINE  TP, \n");
                sql.append("        TM_DEALER       TD \n");
                sql.append("  WHERE SD.SO_ID = SM.SO_ID \n");
                sql.append("    AND SD.PART_ID = TP.PART_ID \n");
                sql.append("    AND SM.DEALER_ID = TD.DEALER_ID \n");
                sql.append("    AND SM.SELLER_ID = " + logonUser.getOrgId() + "\n");
                sql.append("    AND SM.ORDER_TYPE = 92151004 \n");
                sql.append("    AND SM.STATE <> 92401003 \n");
                if (!"".equals(startDate)) {
                    sql.append(" AND TO_DATE(SM.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(endDate)) {
                    sql.append(" AND TO_DATE(SM.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(dealerName)) {
                    sql.append("         AND TD.DEALER_NAME LIKE '%").append(dealerName).append("%'");
                }
                if (!"".equals(partOldCode)) {
                    sql.append("         AND UPPER(SD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
                }
                sql.append("    AND NOT EXISTS \n");
                sql.append("  (SELECT 1 FROM TT_PART_OUTSTOCK_MAIN OM WHERE OM.SO_ID = SM.SO_ID) \n");
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public PageResult<Map<String, Object>> queryPartInfoList(String flag,
                                                             TtPartDefinePO bean, Integer curPage, int pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuilder sql = new StringBuilder("");
            if ("0".equals(flag)) {
                sql.append(
                        "SELECT T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,T.REMARK " +
                                "FROM TT_PART_DEFINE T WHERE T.OF_FLAG="
                ).append(Constant.IF_TYPE_YES);
            } else {
                sql.append(
                        "SELECT T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,T.REMARK " +
                                "FROM TT_PART_DEFINE T WHERE 1=1"
                );
            }

            if (!"".equals(bean.getPartOldcode())) {
                sql.append(" AND UPPER(T.PART_OLDCODE) LIKE '%")
                        .append(bean.getPartOldcode().toUpperCase()).append("%'");
            }
            if (!"".equals(bean.getPartCname())) {
                sql.append(" AND UPPER(T.PART_CNAME) LIKE '%")
                        .append(bean.getPartCname().toUpperCase()).append("%'");
            }
            if (null != bean.getPartCode()) {
                sql.append(" AND UPPER(T.PART_CODE) LIKE '%")
                        .append(bean.getPartCode().toUpperCase()).append("%'");
            }
            sql.append(" ORDER BY T.PART_OLDCODE, T.PART_CNAME ");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }

        return ps;
    }

    public PageResult<Map<String, Object>> queryPartOffInfoList(
            RequestWrapper request, Integer curPage, Integer pageSizeMax) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String partOldCodeStr = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String year = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//年
            String month = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//月
            String month1 = CommonUtils.checkNull(request.getParamValue("MYMONTH1"));//月
            String season = CommonUtils.checkNull(request.getParamValue("MYSEASON"));//季度
            String dealerIdStr = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));//服务商
            String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));

            StringBuilder sql = new StringBuilder("");

            if ("4".equals(radioSelect)) {
                if (!"".equals(partOldCodeStr)) {
                    String[] partOldCodes = partOldCodeStr.split(",");

                    sql.append(" SELECT OM.DEALER_ID, \n");
                    sql.append("        OM.DEALER_CODE, \n");
                    sql.append("        OM.DEALER_NAME, \n");
                    sql.append("        ---------------------------拼接SQL开始---------------------------------------- \n");

                    for (int i = 0; i < partOldCodes.length; i++) {
                        sql.append("        NVL(SUM(DECODE(OD.PART_OLDCODE,'").append(partOldCodes[i])
                                .append("',OD.OUTSTOCK_QTY)), 0) ").append(" AS XXX" + i + ",");
                    }
                    sql.append("        ---------------------------拼接SQL开始---------------------------------------- \n");
                    sql.append("        1 AS STATE \n");
                    sql.append("   FROM TT_PART_OUTSTOCK_DTL OD, TT_PART_OUTSTOCK_MAIN OM \n");
                    sql.append("  WHERE OD.OUT_ID = OM.OUT_ID \n");
                    if (!"".equals(year)) {
                        if (!"".equals(month) && !"".equals(month1)) {
                            sql.append("    AND TO_CHAR(OM.CREATE_DATE,'yyyy-MM') >= '").append(year).append("-").append(month).append("'\n");
                            sql.append("    AND TO_CHAR(OM.CREATE_DATE,'yyyy-MM') <= '").append(year).append("-").append(month1).append("'\n");
                        } else if (!"".equals(season)) {
                            sql.append("  AND TO_CHAR(OM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                        } else {
                            sql.append("  AND TO_CHAR(T1.CREATE_DATE,'yyyy')='" + year + "'");
                        }
                    }
                    sql.append("    AND OM.DEALER_CODE LIKE 'S%' \n");

                    sql.append("    AND OD.PART_OLDCODE IN ('");
                    for (int i = 0; i < partOldCodes.length; i++) {
                        sql.append(partOldCodes[i]).append("','");
                    }
                    sql.deleteCharAt(sql.length() - 2);
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(")");

                    if (!"".equals(dealerIdStr)) {
                        String[] dealerIds = dealerIdStr.split(",");
                        sql.append(" AND OM.DEALER_ID IN( '");
                        for (int i = 0; i < dealerIds.length; i++) {
                            sql.append(dealerIds[i]).append("','");
                        }
                        sql.deleteCharAt(sql.length() - 2);
                        sql.deleteCharAt(sql.length() - 1);
                        sql.append(")");
                    }

                    sql.append("  GROUP BY OM.DEALER_ID, OM.DEALER_CODE, OM.DEALER_NAME \n");
                }

            } else {
                sql.append(" SELECT ");
                if (!"".equals(year)) {
                    if (!"".equals(month) && !"".equals(month1)) {
                        sql.append(" '" + year + "-" + month + "至" + year + "-" + month1 + "'");
                    } else if (!"".equals(season)) {
                        if ("1".equals(season)) {
                            sql.append(" '" + year + "-01至" + year + "-03'");
                        } else if ("2".equals(season)) {
                            sql.append(" '" + year + "-04至" + year + "-06'");
                        } else if ("3".equals(season)) {
                            sql.append(" '" + year + "-07至" + year + "-09'");
                        } else {
                            sql.append(" '" + year + "-10至" + year + "-11'");
                        }
                    } else {
                        sql.append(" '" + year + "-01至" + year + "-12'");
                    }
                    sql.append(" MONTH_NO, \n");
                }

                if ("1".equals(radioSelect)) {

                    sql.append("        DS.ROOT_ORG_NAME, \n");
                    sql.append("        DS.REGION_NAME, \n");
                    sql.append("        TD.DEALER_CODE, \n");
                    sql.append("        TD.DEALER_NAME, \n");
                    String[] partOldCodes = contactPartOldCode(partOldCodeStr, sql);

                    /* sql.append("(DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
                     sql.append("        1,\n");
                     sql.append("        SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
                     sql.append("        0)) OF_QTY, --流失量\n");*/
                    /* sql.append("SUM(T.BAL_QTY) SBAL_QTY, --结算量\n");
                     sql.append("CASE\n");
                     sql.append("  WHEN SUM(T.BAL_QTY) != 0 AND\n");
                     sql.append("       (DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
                     sql.append("               1,\n");
                     sql.append("               SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
                     sql.append("               0)) > 0 THEN\n");
                     sql.append("   TO_CHAR((DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
                     sql.append("                   1,\n");
                     sql.append("                   SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
                     sql.append("                   0)) / SUM(T.BAL_QTY) * 100,\n");
                     sql.append("           '999.99') || '%'\n");
                     sql.append("  ELSE\n");
                     sql.append("   '-'\n");
                     sql.append("END OF_RATIO --流失率\n");*/
                    sql.append(" 1 as state");
                    sql.append("   FROM TT_PART_RP_OUTFLOW    T, \n");
                    sql.append("        TM_DEALER             TD, \n");
                    sql.append("        TT_PART_DEFINE        TPD, \n");
                    sql.append("        VW_ORG_DEALER_SERVICE DS \n");
                    sql.append("  WHERE T.DEALER_ID = TD.DEALER_ID \n");
                    sql.append("    AND T.PART_ID = TPD.PART_ID \n");
                    sql.append("    AND T.DEALER_ID = DS.DEALER_ID \n");
                    sql.append("    AND TD.DEALER_CODE NOT LIKE 'G%' --不能去 \n");
                    sql.append("    AND TD.IS_NBDW = 0 --不能去 \n");
                    monthNoBj(year, month, month1, season, sql);
                    sql.append("       --AND DS.ROOT_ORG_NAME = '川渝藏大区' \n");

                    sql.append("    AND TPD.PART_OLDCODE IN ('");
                    for (int i = 0; i < partOldCodes.length; i++) {
                        sql.append(partOldCodes[i]).append("','");
                    }
                    sql.deleteCharAt(sql.length() - 2);
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(")");

                    if (!"".equals(dealerIdStr)) {
                        String[] dealerIds = dealerIdStr.split(",");
                        sql.append(" AND DS.DEALER_ID IN( '");
                        for (int i = 0; i < dealerIds.length; i++) {
                            sql.append(dealerIds[i]).append("','");
                        }
                        sql.deleteCharAt(sql.length() - 2);
                        sql.deleteCharAt(sql.length() - 1);
                        sql.append(")");
                    }
                    sql.append("  GROUP BY DS.ROOT_ORG_NAME, DS.REGION_NAME, TD.DEALER_CODE, TD.DEALER_NAME \n");
                    sql.append("  ORDER BY DS.ROOT_ORG_NAME, DS.REGION_NAME \n");

                } else if ("2".equals(radioSelect)) {
                    sql.append("        DECODE(GROUPING(DS.ROOT_ORG_NAME), 1, '合计', DS.ROOT_ORG_NAME) ROOT_ORG_NAME, --大区 \n");
                    sql.append("        DECODE(GROUPING(DS.REGION_NAME), 1, '', DS.REGION_NAME) DREGION_NAME, --省份 \n");
                    String[] partOldCodes = contactPartOldCode(partOldCodeStr, sql);

                    /* sql.append("(DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
                     sql.append("        1,\n");
                     sql.append("        SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
                     sql.append("        0)) OF_QTY, --流失量\n");*/
                    /* sql.append("SUM(T.BAL_QTY) SBAL_QTY, --结算量\n");
                     sql.append("CASE\n");
                     sql.append("  WHEN SUM(T.BAL_QTY) != 0 AND\n");
                     sql.append("       (DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
                     sql.append("               1,\n");
                     sql.append("               SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
                     sql.append("               0)) > 0 THEN\n");
                     sql.append("   TO_CHAR((DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
                     sql.append("                   1,\n");
                     sql.append("                   SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
                     sql.append("                   0)) / SUM(T.BAL_QTY) * 100,\n");
                     sql.append("           '999.99') || '%'\n");
                     sql.append("  ELSE\n");
                     sql.append("   '-'\n");
                     sql.append("END OF_RATIO --流失率\n");*/
                    sql.append(" 1 as state");
                    sql.append("   FROM TT_PART_RP_OUTFLOW    T, \n");
                    sql.append("        TM_DEALER             TD, \n");
                    sql.append("        TT_PART_DEFINE        TPD, \n");
                    sql.append("        VW_ORG_DEALER_SERVICE DS \n");
                    sql.append("  WHERE T.DEALER_ID = TD.DEALER_ID \n");
                    sql.append("    AND T.PART_ID = TPD.PART_ID \n");
                    sql.append("    AND T.DEALER_ID = DS.DEALER_ID \n");
                    sql.append("    AND TD.DEALER_CODE NOT LIKE 'G%' --不能去 \n");
                    sql.append("    AND TD.IS_NBDW = 0 --不能去 \n");
                    monthNoBj(year, month, month1, season, sql);
                    sql.append("       --AND DS.ROOT_ORG_NAME = '川渝藏大区' \n");
                    sql.append("    AND TPD.PART_OLDCODE IN ('");
                    for (int i = 0; i < partOldCodes.length; i++) {
                        sql.append(partOldCodes[i]).append("','");
                    }
                    sql.deleteCharAt(sql.length() - 2);
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(")");

                    //sql.append("  GROUP BY ROLLUP(DS.ROOT_ORG_NAME, DS.REGION_NAME) \n");
                    sql.append("  GROUP BY DS.ROOT_ORG_NAME, DS.REGION_NAME \n");
                    sql.append("  ORDER BY DS.ROOT_ORG_NAME, DS.REGION_NAME NULLS LAST \n");

                } else {
                    sql.append("        TD.DEALER_CODE, --服务商代码 \n");
                    sql.append("        TD.DEALER_NAME, --服务商名称 \n");
                    sql.append("        TPD.PART_OLDCODE, --配件编码 \n");
                    sql.append("        TPD.PART_CNAME, --配件名称 \n");
                    sql.append("        TPD.PART_CODE, --配件件号 \n");
                    sql.append("        SUM(T.BEG_QTY) BEG_QTY, --期初库存 \n");
                    sql.append("        SUM(T.IN_QTY) IN_QTY, --购进 \n");
                    sql.append("        SUM(T.BAL_QTY) BAL_QTY, --结算 \n");

                    sql.append("(DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
                    sql.append("        1,\n");
                    sql.append("        SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
                    sql.append("        0)) OF_QTY, --流失量\n");
                    sql.append("SUM(T.BAL_QTY) SBAL_QTY, --结算量\n");
                    sql.append("CASE\n");
                    sql.append("  WHEN SUM(T.BAL_QTY) != 0 AND\n");
                    sql.append("       (DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
                    sql.append("               1,\n");
                    sql.append("               SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
                    sql.append("               0)) > 0 THEN\n");
                    sql.append("   TO_CHAR((DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
                    sql.append("                   1,\n");
                    sql.append("                   SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
                    sql.append("                   0)) / SUM(T.BAL_QTY) * 100,\n");
                    sql.append("           '999.99') || '%'\n");
                    sql.append("  ELSE\n");
                    sql.append("   '-'\n");
                    sql.append("END OF_RATIO --流失率\n");
                    sql.append("   FROM TT_PART_RP_OUTFLOW    T, \n");
                    sql.append("        TM_DEALER             TD, \n");
                    sql.append("        TT_PART_DEFINE        TPD, \n");
                    sql.append("        VW_ORG_DEALER_SERVICE DS \n");
                    sql.append("  WHERE T.DEALER_ID = TD.DEALER_ID \n");
                    sql.append("    AND T.PART_ID = TPD.PART_ID \n");
                    sql.append("    AND T.DEALER_ID = DS.DEALER_ID \n");
                    sql.append("    AND TD.DEALER_CODE NOT LIKE 'G%' --不能去 \n");
                    sql.append("    AND TD.IS_NBDW = 0 --不能去 \n");
                    monthNoBj(year, month, month1, season, sql);
                    sql.append("       --AND DS.ROOT_ORG_NAME = '川渝藏大区' \n");
                    sql.append("    AND TPD.PART_OLDCODE IN ('");

                    String[] partOldCodes = partOldCodeStr.split(",");
                    for (int i = 0; i < partOldCodes.length; i++) {
                        sql.append(partOldCodes[i]).append("','");
                    }
                    sql.deleteCharAt(sql.length() - 2);
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(")");

                    if (!"".equals(dealerIdStr)) {
                        String[] dealerIds = dealerIdStr.split(",");
                        sql.append(" AND DS.DEALER_ID IN( '");
                        for (int i = 0; i < dealerIds.length; i++) {
                            sql.append(dealerIds[i]).append("','");
                        }
                        sql.deleteCharAt(sql.length() - 2);
                        sql.deleteCharAt(sql.length() - 1);
                        sql.append(")");
                    }

                    sql.append("  GROUP BY TD.DEALER_CODE, \n");
                    sql.append("           TD.DEALER_NAME, \n");
                    sql.append("           TPD.PART_OLDCODE, \n");
                    sql.append("           TPD.PART_CNAME, \n");
                    sql.append("           TPD.PART_CODE \n");
                    sql.append("  ORDER BY TD.DEALER_NAME \n");

                }
            }

            ps = pageQuery(sql.toString(), null, getFunName(), pageSizeMax,
                    curPage);
        } catch (Exception e) {
            throw e;
        }

        return ps;
    }

    private void monthNoBj(String year, String month, String month1,
                           String season, StringBuilder sql) {
        if (!"".equals(year)) {
            if (!"".equals(month) && !"".equals(month1)) {
                sql.append("    AND T.MONTH_NO >= '").append(year).append("-").append(month).append("'\n");
                sql.append("    AND T.MONTH_NO <= '").append(year).append("-").append(month1).append("'\n");
            } else if (!"".equals(season)) {
                if ("1".equals(season)) {
                    sql.append("    AND T.MONTH_NO >= '").append(year).append("-01").append("'\n");
                    sql.append("    AND T.MONTH_NO <= '").append(year).append("-03").append("'\n");
                } else if ("2".equals(season)) {
                    sql.append("    AND T.MONTH_NO >= '").append(year).append("-04").append("'\n");
                    sql.append("    AND T.MONTH_NO <= '").append(year).append("-06").append("'\n");
                } else if ("3".equals(season)) {
                    sql.append("    AND T.MONTH_NO >= '").append(year).append("-07").append("'\n");
                    sql.append("    AND T.MONTH_NO <= '").append(year).append("-09").append("'\n");
                } else {
                    sql.append("    AND T.MONTH_NO >= '").append(year).append("-10").append("'\n");
                    sql.append("    AND T.MONTH_NO <= '").append(year).append("-12").append("'\n");
                }
            } else {
                sql.append("    AND T.MONTH_NO >= '").append(year).append("-01").append("'\n");
                sql.append("    AND T.MONTH_NO <= '").append(year).append("-12").append("'\n");
            }
        }
    }

    private String[] contactPartOldCode(String partOldCodeStr, StringBuilder sql) {
        sql.append("        ------------------------拼接要查询配件开始------------------------------------------ \n");
        String[] partOldCodes = partOldCodeStr.split(",");
        for (int i = 0; i < partOldCodes.length; i++) {
            String partOldCode = partOldCodes[i];

            sql.append("CASE\n");
            sql.append("  WHEN SUM((NVL(DECODE(TPD.PART_OLDCODE, '" + partOldCode + "', T.BAL_QTY), 0))) <> 0 AND\n");
            sql.append("       DECODE(SIGN(SUM((NVL(DECODE(TPD.PART_OLDCODE,\n");
            sql.append("                                   '" + partOldCode + "',\n");
            sql.append("                                   T.BAL_QTY),\n");
            sql.append("                            0))) - SUM(NVL(DECODE(TPD.PART_OLDCODE,\n");
            sql.append("                                                  '" + partOldCode + "',\n");
            sql.append("                                                  T.IN_QTY),\n");
            sql.append("                                           0)) -\n");
            sql.append("                   SUM(NVL(DECODE(TPD.PART_OLDCODE,\n");
            sql.append("                                  '" + partOldCode + "',\n");
            sql.append("                                  DECODE(T.MONTH_NO,\n");
            sql.append("                                         '2013-08',\n");
            sql.append("                                         T.BEG_QTY)),\n");
            sql.append("                           0))),\n");
            sql.append("              1,\n");
            sql.append("              SUM((NVL(DECODE(TPD.PART_OLDCODE, '" + partOldCode + "', T.BAL_QTY),\n");
            sql.append("                       0))) -\n");
            sql.append("              SUM(NVL(DECODE(TPD.PART_OLDCODE, '" + partOldCode + "', T.IN_QTY),\n");
            sql.append("                      0)) -\n");
            sql.append("              SUM(NVL(DECODE(TPD.PART_OLDCODE,\n");
            sql.append("                             '" + partOldCode + "',\n");
            sql.append("                             DECODE(T.MONTH_NO, '2013-08', T.BEG_QTY)),\n");
            sql.append("                      0)),\n");
            sql.append("              0) <> 0 THEN\n");
            sql.append("   TO_CHAR((DECODE(SIGN(SUM((NVL(DECODE(TPD.PART_OLDCODE,\n");
            sql.append("                                              '" + partOldCode + "',\n");
            sql.append("                                              T.BAL_QTY),\n");
            sql.append("                                       0))) -\n");
            sql.append("                              SUM(NVL(DECODE(TPD.PART_OLDCODE,\n");
            sql.append("                                             '" + partOldCode + "',\n");
            sql.append("                                             T.IN_QTY),\n");
            sql.append("                                      0)) - SUM(NVL(DECODE(TPD.PART_OLDCODE,\n");
            sql.append("                                                           '" + partOldCode + "',\n");
            sql.append("                                                           DECODE(T.MONTH_NO,\n");
            sql.append("                                                                  '2013-08',\n");
            sql.append("                                                                  T.BEG_QTY)),\n");
            sql.append("                                                    0))),\n");
            sql.append("                         1,\n");
            sql.append("                         SUM((NVL(DECODE(TPD.PART_OLDCODE,\n");
            sql.append("                                         '" + partOldCode + "',\n");
            sql.append("                                         T.BAL_QTY),\n");
            sql.append("                                  0))) - SUM(NVL(DECODE(TPD.PART_OLDCODE,\n");
            sql.append("                                                        '" + partOldCode + "',\n");
            sql.append("                                                        T.IN_QTY),\n");
            sql.append("                                                 0)) -\n");
            sql.append("                         SUM(NVL(DECODE(TPD.PART_OLDCODE,\n");
            sql.append("                                        '" + partOldCode + "',\n");
            sql.append("                                        DECODE(T.MONTH_NO,\n");
            sql.append("                                               '2013-08',\n");
            sql.append("                                               T.BEG_QTY)),\n");
            sql.append("                                 0)),\n");
            sql.append("                         0)) / SUM((NVL(DECODE(TPD.PART_OLDCODE,\n");
            sql.append("                                               '" + partOldCode + "',\n");
            sql.append("                                               T.BAL_QTY),\n");
            sql.append("                                        0)))* 100,\n");
            sql.append("           '999.99') || '%'\n");
            sql.append("  ELSE\n");
            sql.append("   '-'\n");
            sql.append("END as XXX" + i + ",");
        }
        sql.append("        -------------------------拼接要查询的配件结束-------------------------------------- \n");
        return partOldCodes;
    }

    public List<Map<String, Object>> queryPartOffs(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String partOldCodeStr = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String year = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//年
            String month = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//月
            String month1 = CommonUtils.checkNull(request.getParamValue("MYMONTH1"));//月
            String season = CommonUtils.checkNull(request.getParamValue("MYSEASON"));//季度
            String dealerIdStr = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));//服务商
            String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));

            StringBuilder sql = new StringBuilder("");

            if ("4".equals(radioSelect)) {
                if (!"".equals(partOldCodeStr)) {
                    String[] partOldCodes = partOldCodeStr.split(",");

                    sql.append(" SELECT OM.DEALER_ID, \n");
                    sql.append("        OM.DEALER_CODE, \n");
                    sql.append("        OM.DEALER_NAME, \n");
                    sql.append("        ---------------------------拼接SQL开始---------------------------------------- \n");

                    for (int i = 0; i < partOldCodes.length; i++) {
                        sql.append("        NVL(SUM(DECODE(OD.PART_OLDCODE,'").append(partOldCodes[i])
                                .append("',OD.OUTSTOCK_QTY)), 0) ").append(" AS XXX" + i + ",");
                    }
                    sql.append("        ---------------------------拼接SQL开始---------------------------------------- \n");
                    sql.append("        1 AS STATE \n");
                    sql.append("   FROM TT_PART_OUTSTOCK_DTL OD, TT_PART_OUTSTOCK_MAIN OM \n");
                    sql.append("  WHERE OD.OUT_ID = OM.OUT_ID \n");
                    if (!"".equals(year)) {
                        if (!"".equals(month) && !"".equals(month1)) {
                            sql.append("    AND TO_CHAR(OM.CREATE_DATE,'yyyy-MM') >= '").append(year).append("-").append(month).append("'\n");
                            sql.append("    AND TO_CHAR(OM.CREATE_DATE,'yyyy-MM') <= '").append(year).append("-").append(month1).append("'\n");
                        } else if (!"".equals(season)) {
                            sql.append("  AND TO_CHAR(OM.CREATE_DATE,'yyyy-q')='" + year + "-" + season + "'");
                        } else {
                            sql.append("  AND TO_CHAR(T1.CREATE_DATE,'yyyy')='" + year + "'");
                        }
                    }
                    sql.append("    AND OM.DEALER_CODE LIKE 'S%' \n");

                    sql.append("    AND OD.PART_OLDCODE IN ('");
                    for (int i = 0; i < partOldCodes.length; i++) {
                        sql.append(partOldCodes[i]).append("','");
                    }
                    sql.deleteCharAt(sql.length() - 2);
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(")");

                    if (!"".equals(dealerIdStr)) {
                        String[] dealerIds = dealerIdStr.split(",");
                        sql.append(" AND OM.DEALER_ID IN( '");
                        for (int i = 0; i < dealerIds.length; i++) {
                            sql.append(dealerIds[i]).append("','");
                        }
                        sql.deleteCharAt(sql.length() - 2);
                        sql.deleteCharAt(sql.length() - 1);
                        sql.append(")");
                    }

                    sql.append("  GROUP BY OM.DEALER_ID, OM.DEALER_CODE, OM.DEALER_NAME \n");
                }

            } else {
                sql.append(" SELECT ");
                if (!"".equals(year)) {
                    if (!"".equals(month) && !"".equals(month1)) {
                        sql.append(" '" + year + "-" + month + "至" + year + "-" + month1 + "'");
                    } else if (!"".equals(season)) {
                        if ("1".equals(season)) {
                            sql.append(" '" + year + "-01至" + year + "-03'");
                        } else if ("2".equals(season)) {
                            sql.append(" '" + year + "-04至" + year + "-06'");
                        } else if ("3".equals(season)) {
                            sql.append(" '" + year + "-07至" + year + "-09'");
                        } else {
                            sql.append(" '" + year + "-10至" + year + "-11'");
                        }
                    } else {
                        sql.append(" '" + year + "-01至" + year + "-12'");
                    }
                    sql.append(" MONTH_NO, \n");
                }

                if ("1".equals(radioSelect)) {

                    sql.append("        DS.ROOT_ORG_NAME, \n");
                    sql.append("        DS.REGION_NAME, \n");
                    sql.append("        TD.DEALER_CODE, \n");
                    sql.append("        TD.DEALER_NAME, \n");
                    String[] partOldCodes = contactPartOldCode(partOldCodeStr, sql);

	               /* sql.append("(DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
                    sql.append("              1,\n");
	                sql.append("              SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
	                sql.append("              0)) OF_QTY, --流失量\n");
	                sql.append("      SUM(T.BAL_QTY) SBAL_QTY, --结算量\n");
	                sql.append("      CASE\n");
	                sql.append("        WHEN SUM(T.BAL_QTY) != 0 AND\n");
	                sql.append("             (DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
	                sql.append("                     1,\n");
	                sql.append("                     SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
	                sql.append("                     0)) > 0 THEN\n");
	                sql.append("         TO_CHAR((DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
	                sql.append("                         1,\n");
	                sql.append("                         SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
	                sql.append("                         0)) / SUM(T.BAL_QTY) * 100,\n");
	                sql.append("                 '999.99') || '%'\n");
	                sql.append("        ELSE\n");
	                sql.append("         '-'\n");
	                sql.append("      END OF_RATIO --流失率\n");*/
                    sql.append(" 1 as state");
                    sql.append("   FROM TT_PART_RP_OUTFLOW    T, \n");
                    sql.append("        TM_DEALER             TD, \n");
                    sql.append("        TT_PART_DEFINE        TPD, \n");
                    sql.append("        VW_ORG_DEALER_SERVICE DS \n");
                    sql.append("  WHERE T.DEALER_ID = TD.DEALER_ID \n");
                    sql.append("    AND T.PART_ID = TPD.PART_ID \n");
                    sql.append("    AND T.DEALER_ID = DS.DEALER_ID \n");
                    sql.append("    AND TD.DEALER_CODE NOT LIKE 'G%' --不能去 \n");
                    sql.append("    AND TD.IS_NBDW = 0 --不能去 \n");
                    monthNoBj(year, month, month1, season, sql);
                    sql.append("       --AND DS.ROOT_ORG_NAME = '川渝藏大区' \n");

                    sql.append("    AND TPD.PART_OLDCODE IN ('");
                    for (int i = 0; i < partOldCodes.length; i++) {
                        sql.append(partOldCodes[i]).append("','");
                    }
                    sql.deleteCharAt(sql.length() - 2);
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(")");

                    if (!"".equals(dealerIdStr)) {
                        String[] dealerIds = dealerIdStr.split(",");
                        sql.append(" AND DS.DEALER_ID IN( '");
                        for (int i = 0; i < dealerIds.length; i++) {
                            sql.append(dealerIds[i]).append("','");
                        }
                        sql.deleteCharAt(sql.length() - 2);
                        sql.deleteCharAt(sql.length() - 1);
                        sql.append(")");
                    }
                    sql.append("  GROUP BY DS.ROOT_ORG_NAME, DS.REGION_NAME, TD.DEALER_CODE, TD.DEALER_NAME \n");
                    sql.append("  ORDER BY DS.ROOT_ORG_NAME, DS.REGION_NAME \n");

                } else if ("2".equals(radioSelect)) {
                    sql.append("        DECODE(GROUPING(DS.ROOT_ORG_NAME), 1, '合计', DS.ROOT_ORG_NAME) ROOT_ORG_NAME, --大区 \n");
                    sql.append("        DECODE(GROUPING(DS.REGION_NAME), 1, '', DS.REGION_NAME) DREGION_NAME, --省份 \n");
                    String[] partOldCodes = contactPartOldCode(partOldCodeStr, sql);
	
	              /*  sql.append("(DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
	                sql.append("              1,\n");
	                sql.append("              SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
	                sql.append("              0)) OF_QTY, --流失量\n");
	                sql.append("      SUM(T.BAL_QTY) SBAL_QTY, --结算量\n");
	                sql.append("      CASE\n");
	                sql.append("        WHEN SUM(T.BAL_QTY) != 0 AND\n");
	                sql.append("             (DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
	                sql.append("                     1,\n");
	                sql.append("                     SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
	                sql.append("                     0)) > 0 THEN\n");
	                sql.append("         TO_CHAR((DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
	                sql.append("                         1,\n");
	                sql.append("                         SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
	                sql.append("                         0)) / SUM(T.BAL_QTY) * 100,\n");
	                sql.append("                 '999.99') || '%'\n");
	                sql.append("        ELSE\n");
	                sql.append("         '-'\n");
	                sql.append("      END OF_RATIO --流失率\n");*/
                    sql.append(" 1 as state");
                    sql.append("   FROM TT_PART_RP_OUTFLOW    T, \n");
                    sql.append("        TM_DEALER             TD, \n");
                    sql.append("        TT_PART_DEFINE        TPD, \n");
                    sql.append("        VW_ORG_DEALER_SERVICE DS \n");
                    sql.append("  WHERE T.DEALER_ID = TD.DEALER_ID \n");
                    sql.append("    AND T.PART_ID = TPD.PART_ID \n");
                    sql.append("    AND T.DEALER_ID = DS.DEALER_ID \n");
                    sql.append("    AND TD.DEALER_CODE NOT LIKE 'G%' --不能去 \n");
                    sql.append("    AND TD.IS_NBDW = 0 --不能去 \n");
                    monthNoBj(year, month, month1, season, sql);
                    sql.append("       --AND DS.ROOT_ORG_NAME = '川渝藏大区' \n");
                    sql.append("    AND TPD.PART_OLDCODE IN ('");
                    for (int i = 0; i < partOldCodes.length; i++) {
                        sql.append(partOldCodes[i]).append("','");
                    }
                    sql.deleteCharAt(sql.length() - 2);
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(")");

                    //sql.append("  GROUP BY ROLLUP(DS.ROOT_ORG_NAME, DS.REGION_NAME) \n");
                    sql.append("  GROUP BY DS.ROOT_ORG_NAME, DS.REGION_NAME \n");
                    sql.append("  ORDER BY DS.ROOT_ORG_NAME, DS.REGION_NAME NULLS LAST \n");

                } else {
                    sql.append("        TD.DEALER_CODE, --服务商代码 \n");
                    sql.append("        TD.DEALER_NAME, --服务商名称 \n");
                    sql.append("        TPD.PART_OLDCODE, --配件编码 \n");
                    sql.append("        TPD.PART_CNAME, --配件名称 \n");
                    sql.append("        TPD.PART_CODE, --配件件号 \n");
                    sql.append("        SUM(T.BEG_QTY) BEG_QTY, --期初库存 \n");
                    sql.append("        SUM(T.IN_QTY) IN_QTY, --购进 \n");
                    sql.append("        SUM(T.BAL_QTY) BAL_QTY, --结算 \n");

                    sql.append("(DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
                    sql.append("              1,\n");
                    sql.append("              SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
                    sql.append("              0)) OF_QTY, --流失量\n");
                    sql.append("      SUM(T.BAL_QTY) SBAL_QTY, --结算量\n");
                    sql.append("      CASE\n");
                    sql.append("        WHEN SUM(T.BAL_QTY) != 0 AND\n");
                    sql.append("             (DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
                    sql.append("                     1,\n");
                    sql.append("                     SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
                    sql.append("                     0)) > 0 THEN\n");
                    sql.append("         TO_CHAR((DECODE(SIGN(SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY)),\n");
                    sql.append("                         1,\n");
                    sql.append("                         SUM(T.BAL_QTY - T.IN_QTY - T.BEG_QTY),\n");
                    sql.append("                         0)) / SUM(T.BAL_QTY) * 100,\n");
                    sql.append("                 '999.99') || '%'\n");
                    sql.append("        ELSE\n");
                    sql.append("         '-'\n");
                    sql.append("      END OF_RATIO --流失率\n");
                    sql.append("   FROM TT_PART_RP_OUTFLOW    T, \n");
                    sql.append("        TM_DEALER             TD, \n");
                    sql.append("        TT_PART_DEFINE        TPD, \n");
                    sql.append("        VW_ORG_DEALER_SERVICE DS \n");
                    sql.append("  WHERE T.DEALER_ID = TD.DEALER_ID \n");
                    sql.append("    AND T.PART_ID = TPD.PART_ID \n");
                    sql.append("    AND T.DEALER_ID = DS.DEALER_ID \n");
                    sql.append("    AND TD.DEALER_CODE NOT LIKE 'G%' --不能去 \n");
                    sql.append("    AND TD.IS_NBDW = 0 --不能去 \n");
                    monthNoBj(year, month, month1, season, sql);
                    sql.append("       --AND DS.ROOT_ORG_NAME = '川渝藏大区' \n");
                    sql.append("    AND TPD.PART_OLDCODE IN ('");

                    String[] partOldCodes = partOldCodeStr.split(",");
                    for (int i = 0; i < partOldCodes.length; i++) {
                        sql.append(partOldCodes[i]).append("','");
                    }
                    sql.deleteCharAt(sql.length() - 2);
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(")");

                    if (!"".equals(dealerIdStr)) {
                        String[] dealerIds = dealerIdStr.split(",");
                        sql.append(" AND DS.DEALER_ID IN( '");
                        for (int i = 0; i < dealerIds.length; i++) {
                            sql.append(dealerIds[i]).append("','");
                        }
                        sql.deleteCharAt(sql.length() - 2);
                        sql.deleteCharAt(sql.length() - 1);
                        sql.append(")");
                    }

                    sql.append("  GROUP BY TD.DEALER_CODE, \n");
                    sql.append("           TD.DEALER_NAME, \n");
                    sql.append("           TPD.PART_OLDCODE, \n");
                    sql.append("           TPD.PART_CNAME, \n");
                    sql.append("           TPD.PART_CODE \n");
                    sql.append("  ORDER BY TD.DEALER_NAME \n");

                }
            }
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<Map<String, Object>> queryStockIn(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PARTOLDCODE"));//配件编码
            String myyear = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//年份
            String mymonth = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//月份
            StringBuffer sql = new StringBuffer();
            Long tdyear = Long.parseLong(myyear.substring(2, 4));
            Long bfyear = tdyear - 1;
            //月份为空执行年查询
            if ("".equals(mymonth)) {
                sql.append("SELECT nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'01',ms.buy_count))),null) YI1,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'02',ms.buy_count))),null) YI2,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'03',ms.buy_count))),null) YI3,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'04',ms.buy_count))),null) YI4,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'05',ms.buy_count))),null) YI5,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'06',ms.buy_count))),null) YI6,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'07',ms.buy_count))),null) YI7,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'08',ms.buy_count))),null) YI8,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'09',ms.buy_count))),null) YI9,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'10',ms.buy_count))),null) YI10,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'11',ms.buy_count))),null) YI11,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'12',ms.buy_count))),null) YI12,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'01',ms.buy_count))),null) YI13,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'02',ms.buy_count))),null) YI14,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'03',ms.buy_count))),null) YI15,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'04',ms.buy_count))),null) YI16,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'05',ms.buy_count))),null) YI17,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'06',ms.buy_count))),null) YI18,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'07',ms.buy_count))),null) YI19,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'08',ms.buy_count))),null) YI20,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'09',ms.buy_count))),null) YI21,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'10',ms.buy_count))),null) YI22,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'11',ms.buy_count))),null) YI23,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'12',ms.buy_count))),null) YI24\n");
                sql.append("  FROM TT_PART_PER_MONTH_SALE MS, TT_PART_DEFINE TPD\n");
                sql.append(" WHERE MS.PART_ID = TPD.PART_ID\n");
                if (!"".equals(partOldcode) && partOldcode != null) {
                    sql.append("AND tpd.part_oldcode LIKE UPPER('" + partOldcode + "')\n");
                }
                if (!"".equals(myyear) && myyear != null) {
                    sql.append("   AND SUBSTR(MONTH_NO, 1, 2) in (" + tdyear + "," + bfyear + ")");
                }
                sql.append("   AND MONTH_NO != '9999'\n");
            } else {
                sql.append("SELECT NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '01', D.IN_QTY)), null) ID1,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '02', D.IN_QTY)), null) ID2,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '03', D.IN_QTY)), null) ID3,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '04', D.IN_QTY)), null) ID4,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '05', D.IN_QTY)), null) ID5,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '06', D.IN_QTY)), null) ID6,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '07', D.IN_QTY)), null) ID7,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '08', D.IN_QTY)), null) ID8,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '09', D.IN_QTY)), null) ID9,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '10', D.IN_QTY)), null) ID10,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '11', D.IN_QTY)), null) ID11,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '12', D.IN_QTY)), null) ID12,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '13', D.IN_QTY)), null) ID13,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '14', D.IN_QTY)), null) ID14,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '15', D.IN_QTY)), null) ID15,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '16', D.IN_QTY)), null) ID16,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '17', D.IN_QTY)), null) ID17,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '18', D.IN_QTY)), null) ID18,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '19', D.IN_QTY)), null) ID19,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '20', D.IN_QTY)), null) ID20,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '21', D.IN_QTY)), null) ID21,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '22', D.IN_QTY)), null) ID22,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '23', D.IN_QTY)), null) ID23,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '24', D.IN_QTY)), null) ID24,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '25', D.IN_QTY)), null) ID25,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '26', D.IN_QTY)), null) ID26,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '27', D.IN_QTY)), null) ID27,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '28', D.IN_QTY)), null) ID28,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '29', D.IN_QTY)), null) ID29,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '30', D.IN_QTY)), null) ID30,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '31', D.IN_QTY)), null) ID31\n");
                sql.append("  FROM TT_PART_RP_INVREPORT D, TT_PART_DEFINE TPD\n");
                sql.append(" WHERE D.PART_ID = TPD.PART_ID\n");
                if (!"".equals(partOldcode) && partOldcode != null) {
                    sql.append("AND tpd.part_oldcode LIKE UPPER('" + partOldcode + "')\n");
                }
                sql.append("   AND TO_CHAR(D.CREATE_DATE, 'yyyy-mm') = '" + myyear + "-" + mymonth + "'\n");
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<Map<String, Object>> queryStockOut(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PARTOLDCODE"));//配件编码
            String myyear = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//年份
            String myseason = CommonUtils.checkNull(request.getParamValue("MYSEASON"));//季度
            String mymonth = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//月份
            StringBuffer sql = new StringBuffer();
            Long tdyear = Long.parseLong(myyear.substring(2, 4));
            Long bfyear = tdyear - 1;
            //月份为空执行月查询
            if ("".equals(mymonth)) {
                sql.append("SELECT nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'01',ms.sale_count))),null) YS1,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'02',ms.sale_count))),null) YS2,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'03',ms.sale_count))),null) YS3,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'04',ms.sale_count))),null) YS4,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'05',ms.sale_count))),null) YS5,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'06',ms.sale_count))),null) YS6,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'07',ms.sale_count))),null) YS7,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'08',ms.sale_count))),null) YS8,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'09',ms.sale_count))),null) YS9,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'10',ms.sale_count))),null) YS10,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'11',ms.sale_count))),null) YS11,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'12',ms.sale_count))),null) YS12,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'01',ms.sale_count))),null) YS13,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'02',ms.sale_count))),null) YS14,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'03',ms.sale_count))),null) YS15,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'04',ms.sale_count))),null) YS16,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'05',ms.sale_count))),null) YS17,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'06',ms.sale_count))),null) YS18,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'07',ms.sale_count))),null) YS19,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'08',ms.sale_count))),null) YS20,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'09',ms.sale_count))),null) YS21,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'10',ms.sale_count))),null) YS22,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'11',ms.sale_count))),null) YS23,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'12',ms.sale_count))),null) YS24\n");
                sql.append("  FROM TT_PART_PER_MONTH_SALE MS, TT_PART_DEFINE TPD\n");
                sql.append(" WHERE MS.PART_ID = TPD.PART_ID\n");
                if (!"".equals(partOldcode) && partOldcode != null) {
                    sql.append("AND tpd.part_oldcode LIKE UPPER('" + partOldcode + "')\n");
                }
                if (!"".equals(myyear) && myyear != null) {
                    sql.append("   AND SUBSTR(MONTH_NO, 1, 2) in (" + tdyear + "," + bfyear + ")");
                }
                sql.append("   AND MONTH_NO != '9999'\n");
            } else {
                sql.append("SELECT NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '01', D.OUT_QTY)), null) OD1,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '02', D.OUT_QTY)), null) OD2,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '03', D.OUT_QTY)), null) OD3,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '04', D.OUT_QTY)), null) OD4,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '05', D.OUT_QTY)), null) OD5,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '06', D.OUT_QTY)), null) OD6,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '07', D.OUT_QTY)), null) OD7,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '08', D.OUT_QTY)), null) OD8,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '09', D.OUT_QTY)), null) OD9,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '10', D.OUT_QTY)), null) OD10,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '11', D.OUT_QTY)), null) OD11,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '12', D.OUT_QTY)), null) OD12,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '13', D.OUT_QTY)), null) OD13,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '14', D.OUT_QTY)), null) OD14,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '15', D.OUT_QTY)), null) OD15,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '16', D.OUT_QTY)), null) OD16,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '17', D.OUT_QTY)), null) OD17,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '18', D.OUT_QTY)), null) OD18,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '19', D.OUT_QTY)), null) OD19,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '20', D.OUT_QTY)), null) OD20,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '21', D.OUT_QTY)), null) OD21,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '22', D.OUT_QTY)), null) OD22,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '23', D.OUT_QTY)), null) OD23,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '24', D.OUT_QTY)), null) OD24,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '25', D.OUT_QTY)), null) OD25,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '26', D.OUT_QTY)), null) OD26,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '27', D.OUT_QTY)), null) OD27,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '28', D.OUT_QTY)), null) OD28,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '29', D.OUT_QTY)), null) OD29,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '30', D.OUT_QTY)), null) OD30,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '31', D.OUT_QTY)), null) OD31\n");
                sql.append("\n");
                sql.append("  FROM TT_PART_RP_INVREPORT D, TT_PART_DEFINE TPD\n");
                sql.append(" WHERE D.PART_ID = TPD.PART_ID\n");
                if (!"".equals(partOldcode) && partOldcode != null) {
                    sql.append("AND tpd.part_oldcode LIKE UPPER('" + partOldcode + "')\n");
                }
                sql.append("   AND TO_CHAR(D.CREATE_DATE, 'yyyy-mm') = '" + myyear + "-" + mymonth + "'\n");

            }
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<Map<String, Object>> queryStock(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PARTOLDCODE"));//配件编码
            String myyear = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//年份
            String mymonth = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//月份
            StringBuffer sql = new StringBuffer();
            Long tdyear = Long.parseLong(myyear.substring(2, 4));
            Long bfyear = tdyear - 1;
            //月份为空执行月查询
            if ("".equals(mymonth)) {
                sql.append("SELECT nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'01',ms.ITEM_COUNT))),null) YT1,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'02',ms.ITEM_COUNT))),null) YT2,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'03',ms.ITEM_COUNT))),null) YT3,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'04',ms.ITEM_COUNT))),null) YT4,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'05',ms.ITEM_COUNT))),null) YT5,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'06',ms.ITEM_COUNT))),null) YT6,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'07',ms.ITEM_COUNT))),null) YT7,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'08',ms.ITEM_COUNT))),null) YT8,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'09',ms.ITEM_COUNT))),null) YT9,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'10',ms.ITEM_COUNT))),null) YT10,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'11',ms.ITEM_COUNT))),null) YT11,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2),(" + bfyear + "),DECODE(SUBSTR(MONTH_NO, 3, 4),'12',ms.ITEM_COUNT))),null) YT12,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'01',ms.ITEM_COUNT))),null) YT13,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'02',ms.ITEM_COUNT))),null) YT14,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'03',ms.ITEM_COUNT))),null) YT15,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'04',ms.ITEM_COUNT))),null) YT16,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'05',ms.ITEM_COUNT))),null) YT17,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'06',ms.ITEM_COUNT))),null) YT18,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'07',ms.ITEM_COUNT))),null) YT19,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'08',ms.ITEM_COUNT))),null) YT20,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'09',ms.ITEM_COUNT))),null) YT21,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'10',ms.ITEM_COUNT))),null) YT22,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'11',ms.ITEM_COUNT))),null) YT23,\n");
                sql.append("     nvl(sum(DECODE(SUBSTR(MONTH_NO, 1, 2)," + tdyear + ",DECODE(SUBSTR(MONTH_NO, 3, 4),'12',ms.ITEM_COUNT))),null) YT24\n");
                sql.append("  FROM TT_PART_PER_MONTH_SALE MS, TT_PART_DEFINE TPD\n");
                sql.append(" WHERE MS.PART_ID = TPD.PART_ID\n");
                if (!"".equals(partOldcode) && partOldcode != null) {
                    sql.append("AND tpd.part_oldcode LIKE UPPER('" + partOldcode + "')\n");
                }
                //sql.append("   AND MS.WH_ID = 2013061319370891\n");
                if (!"".equals(myyear) && myyear != null) {
                    sql.append("   AND SUBSTR(MONTH_NO, 1, 2) in (" + tdyear + "," + bfyear + ")");
                }
                sql.append("   AND MONTH_NO != '9999'\n");
            } else {
                sql.append("SELECT NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '01', D.ITEM_QTY)), null) SD1,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '02', D.ITEM_QTY)), null) SD2,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '03', D.ITEM_QTY)), null) SD3,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '04', D.ITEM_QTY)), null) SD4,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '05', D.ITEM_QTY)), null) SD5,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '06', D.ITEM_QTY)), null) SD6,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '07', D.ITEM_QTY)), null) SD7,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '08', D.ITEM_QTY)), null) SD8,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '09', D.ITEM_QTY)), null) SD9,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '10', D.ITEM_QTY)), null) SD10,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '11', D.ITEM_QTY)), null) SD11,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '12', D.ITEM_QTY)), null) SD12,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '13', D.ITEM_QTY)), null) SD13,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '14', D.ITEM_QTY)), null) SD14,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '15', D.ITEM_QTY)), null) SD15,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '16', D.ITEM_QTY)), null) SD16,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '17', D.ITEM_QTY)), null) SD17,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '18', D.ITEM_QTY)), null) SD18,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '19', D.ITEM_QTY)), null) SD19,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '20', D.ITEM_QTY)), null) SD20,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '21', D.ITEM_QTY)), null) SD21,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '22', D.ITEM_QTY)), null) SD22,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '23', D.ITEM_QTY)), null) SD23,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '24', D.ITEM_QTY)), null) SD24,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '25', D.ITEM_QTY)), null) SD25,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '26', D.ITEM_QTY)), null) SD26,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '27', D.ITEM_QTY)), null) SD27,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '28', D.ITEM_QTY)), null) SD28,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '29', D.ITEM_QTY)), null) SD29,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '30', D.ITEM_QTY)), null) SD30,\n");
                sql.append("       NVL(SUM(DECODE(TO_CHAR(D.CREATE_DATE, 'dd'), '31', D.ITEM_QTY)), null) SD31\n");
                sql.append("  FROM TT_PART_RP_INVREPORT D, TT_PART_DEFINE TPD\n");
                sql.append(" WHERE D.PART_ID = TPD.PART_ID\n");
                if (!"".equals(partOldcode) && partOldcode != null) {
                    sql.append("AND tpd.part_oldcode LIKE UPPER('" + partOldcode + "')\n");
                }
                sql.append("   AND TO_CHAR(D.CREATE_DATE, 'yyyy-mm') = '" + myyear + "-" + mymonth + "'\n");

            }
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<Map<String, Object>> queryDate(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String myyear = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//年份
            String mymonth = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//月份
            StringBuffer sql = new StringBuffer();
            Long tdyear = Long.parseLong(myyear.substring(2, 4));
            Long bfyear = tdyear - 1;
            //月份为空查询每月
            if ("".equals(mymonth)) {
                sql.append("SELECT " + bfyear + " || '年1月' M1,\n");
                sql.append("       " + bfyear + " || '年2月' M2,\n");
                sql.append("       " + bfyear + " || '年3月' M3,\n");
                sql.append("       " + bfyear + " || '年4月' M4,\n");
                sql.append("       " + bfyear + " || '年5月' M5,\n");
                sql.append("       " + bfyear + " || '年6月' M6,\n");
                sql.append("       " + bfyear + " || '年7月' M7,\n");
                sql.append("       " + bfyear + " || '年8月' M8,\n");
                sql.append("       " + bfyear + " || '年9月' M9,\n");
                sql.append("       " + bfyear + " || '年10月' M10,\n");
                sql.append("       " + bfyear + " || '年11月' M11,\n");
                sql.append("       " + bfyear + " || '年12月' M12,\n");
                sql.append("       " + tdyear + " || '年1月' M13,\n");
                sql.append("       " + tdyear + " || '年2月' M14,\n");
                sql.append("       " + tdyear + " || '年3月' M15,\n");
                sql.append("       " + tdyear + " || '年4月' M16,\n");
                sql.append("       " + tdyear + " || '年5月' M17,\n");
                sql.append("       " + tdyear + " || '年6月' M18,\n");
                sql.append("       " + tdyear + " || '年7月' M19,\n");
                sql.append("       " + tdyear + " || '年8月' M20,\n");
                sql.append("       " + tdyear + " || '年9月' M21,\n");
                sql.append("       " + tdyear + " || '年10月' M22,\n");
                sql.append("       " + tdyear + " || '年11月' M23,\n");
                sql.append("       " + tdyear + " || '年12月' M24\n");
                sql.append("  FROM DUAL\n");

            } else {
                sql.append("SELECT 1  D1,\n");
                sql.append("       2  D2,\n");
                sql.append("       3  D3,\n");
                sql.append("       4  D4,\n");
                sql.append("       5  D5,\n");
                sql.append("       6  D6,\n");
                sql.append("       7  D7,\n");
                sql.append("       8  D8,\n");
                sql.append("       9  D9,\n");
                sql.append("       10 D10,\n");
                sql.append("       11 D11,\n");
                sql.append("       12 D12,\n");
                sql.append("       13 D13,\n");
                sql.append("       14 D14,\n");
                sql.append("       15 D15,\n");
                sql.append("       16 D16,\n");
                sql.append("       17 D17,\n");
                sql.append("       18 D18,\n");
                sql.append("       19 D19,\n");
                sql.append("       20 D20,\n");
                sql.append("       21 D21,\n");
                sql.append("       22 D22,\n");
                sql.append("       23 D23,\n");
                sql.append("       24 D24,\n");
                sql.append("       25 D25,\n");
                sql.append("       26 D26,\n");
                sql.append("       27 D27,\n");
                sql.append("       28 D28,\n");
                sql.append("       29 D29,\n");
                sql.append("       30 D30,\n");
                sql.append("       31 D31\n");
                sql.append("  FROM DUAL\n");
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<Map<String, Object>> queryExport(RequestWrapper request) {
        // TODO Auto-generated method stub
        return null;
    }
}