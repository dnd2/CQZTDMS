package com.infodms.dms.dao.parts.salesManager.partOrderQueManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.po.TtPartUserposeDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : huchao
 *         CreateDate     : 2013-5-14
 * @ClassName : orderQueDao
 */
public class orderQueDao extends BaseDao {
    public static Logger logger = Logger.getLogger(orderQueDao.class);
    private static final orderQueDao dao = new orderQueDao();

    private orderQueDao() {
    }

    public static final orderQueDao getInstance() {
        return dao;
    }

    private static final int optType = Constant.PART_OPERATION_TYPE_01;//订单

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-14
     * @Title : 配件订单MAIN信息查询
     */
    public PageResult<Map<String, Object>> queryPartOrderMainInfos(RequestWrapper request, AclUserBean logonUser, int curPage, int pageSize) {
        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode")); // 订单单号
        String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 订单类型
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName2")); // 订货单位
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 订货单位编码-弹出框
        String isSaler = CommonUtils.checkNull(request.getParamValue("isSaler")); // 是否销售单位
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode")); //流水号
        String orderBy = CommonUtils.checkNull(request.getParamValue("order_By")); //排序方式
        String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); //大区
        String dealerCode2 = CommonUtils.checkNull(request.getParamValue("dealerCode2")); // 订货单位编码
        String cmmSDate = CommonUtils.checkNull(request.getParamValue("cmmSDate")); // 提交开始时间
        String cmmEDate = CommonUtils.checkNull(request.getParamValue("cmmEDate")); // 提交截止时间
        String transSDate = CommonUtils.checkNull(request.getParamValue("transSDate")); // 发运开始时间
        String transEDate = CommonUtils.checkNull(request.getParamValue("transEDate")); // 发运截止时间
        String transCode = CommonUtils.checkNull(request.getParamValue("transCode")); // 发运单号
        String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldCode")); // 配件编码
        String partName = CommonUtils.checkNull(request.getParamValue("partName")); // 配件名称
        String soSate = CommonUtils.checkNull(request.getParamValue("soSate")); // 销售单状态

        String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
        //String userRole = CommonUtils.checkNull(request.getParamValue("userRole")); // 用户角色

        StringBuffer sbString = new StringBuffer();
        if (null != orderCode && !"".equals(orderCode)) {
            sbString.append(" AND UPPER(OFW.ORDER_CODE) like '%" + orderCode.trim().toUpperCase() + "%'\n");
        }
        if (null != orderType && !"".equals(orderType)) {
            sbString.append(" AND OFW.ORDER_TYPE = '" + orderType + "'\n");
        }
        if (null != dealerName && !"".equals(dealerName)) {
            sbString.append(" AND OFW.DEALER_NAME LIKE '%" + dealerName.trim() + "%'\n");
        }
        if (!"".equals(dealerCode) && null != dealerCode) {
            sbString.append(" AND OFW.DEALER_CODE in (" + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(dealerCode) + ")\n");
        }
        if (!"".equals(dealerCode2) && null != dealerCode2) {
            sbString.append(" AND OFW.DEALER_CODE like  '%" + dealerCode2.trim().toUpperCase() + "%'\n");
        }
        if (null != soCode && !"".equals(soCode)) {
            sbString.append(" AND UPPER(OFW.SO_CODE) like '%" + soCode.trim().toUpperCase() + "%'\n");
        }
        if (!"".equals(soSate) && null != soSate) {
            sbString.append(" AND UPPER(OFW.SO_STATE) like '%" + soSate.trim() + "%'\n");
        }

        //查询权限控制
        if (null == isSaler || "".equals(isSaler) || "NULL".equalsIgnoreCase(isSaler)) {
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND (OFW.DEALER_ID = '" + parentOrgId + "' OR  OFW.SELLER_ID = '" + parentOrgId + "')\n");
            }
        } else if ((Constant.IF_TYPE_YES + "").equals(isSaler)) {
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND OFW.SELLER_ID = '" + parentOrgId + "'\n");
            }
        } else {
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND OFW.DEALER_ID = '" + parentOrgId + "' ");
            }
        }

        //增加大区人员限制 1128
        if (dao.getPoseRoleId(logonUser.getPoseId().toString()).equals("4000005644")) {
            sbString.append(CommonUtils.getOrgDealerLimitSqlByPose("OFW", logonUser));
        }
        //增加大区限制
        if (!"".equals(orgCode) && null != orgCode) {
            sbString.append("AND EXISTS (SELECT 1\n");
            sbString.append("         FROM VW_ORG_DEALER_SERVICE S\n");
            sbString.append("        WHERE S.DEALER_ID = OFW.DEALER_ID\n");
            sbString.append("          AND S.Org_Code IN (" + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(orgCode) + "))\n");
        }

        //订单提交日期
        if (null != cmmSDate && !"".equals(cmmSDate)) {
            sbString.append(" AND TO_CHAR(OFW.SUBMIT_DATE,'yyyy-MM-dd') >= '" + cmmSDate + "'\n");
        }
        if (null != cmmEDate && !"".equals(cmmEDate)) {
            sbString.append(" AND TO_CHAR(OFW.SUBMIT_DATE,'yyyy-MM-dd') <= '" + cmmEDate + "'\n");
        }

        //发运日期和发运单号查询
        if ((!"".equals(transSDate) && null != transSDate) || (!"".equals(transEDate) && null != transEDate) ||
                null != transCode && !"".equals(transCode)) {
            sbString.append("  AND EXISTS\n");
            sbString.append("(SELECT 1\n");
            sbString.append("         FROM VW_PART_TRANS_DTL TD\n");
            sbString.append("        WHERE TD.ORDER_ID = OFW.ORDER_ID\n");
            sbString.append("          AND TD.PICK_ORDER_ID = OFW.PICK_ORDER_ID");//xx
            if (!"".equals(transSDate) && null != transSDate) {
                sbString.append("          AND trunc(TD.SUBMIT_DATE) >= TO_DATE('" + transSDate + "', 'yyyy-mm-dd')\n");
            }
            if (!"".equals(transEDate) && null != transEDate) {
                sbString.append("          AND trunc(TD.SUBMIT_DATE) <= TO_DATE('" + transEDate + "', 'yyyy-mm-dd')\n");
            }
            if (null != transCode && !"".equals(transCode)) {
                sbString.append("          AND TD.TRPLAN_CODE LIKE '%" + transCode + "%'");
            }
            sbString.append("\t\t\t\t)");
        }

        //配件编码和名称查询
        if ((!"".equals(partOldCode) && null != partOldCode) || (!"".equals(partName) && null != partName)) {
            sbString.append("AND EXISTS (SELECT 1\n");
            sbString.append("       FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DEFINE PD\n");
            sbString.append("      WHERE OD.PART_ID = PD.PART_ID\n");
            sbString.append("        AND OD.ORDER_ID = OFW.ORDER_ID\n");
            if (!"".equals(partName) && null != partName) {
                sbString.append("        AND PD.PART_CNAME LIKE '%" + partName + "%'\n");
            }
            if (!"".equals(partOldCode) && null != partOldCode) {
                sbString.append("        AND PD.PART_OLDCODE LIKE '%" + partOldCode + "%'\n");
            }
            sbString.append("\t\t\t\t)");
        }

        //配件销售员订单类型限制
        String userType = null;
        TtPartUserposeDefinePO userposeDefinePO = new TtPartUserposeDefinePO();
        userposeDefinePO.setUserId(logonUser.getUserId());
        if (dao.select(userposeDefinePO).size() > 0) {
            userposeDefinePO = (TtPartUserposeDefinePO) dao.select(userposeDefinePO).get(0);
            userType = userposeDefinePO.getUserType() + "";
        }
        if (!"".equals(userType) && null != userType) {
            if ("3".equals(userType)) {
                sbString.append(" AND EXISTS (SELECT 1\n");
                sbString.append("       FROM TT_PART_USER_ORDERTYPE_DEFINE D\n");
                sbString.append("      WHERE D.ORDER_TYPE = OFW.ORDER_TYPE\n");
                sbString.append("        AND D.USER_ID = " + logonUser.getUserId() + "\n");
                sbString.append("        AND D.STATE = 10011001\n");
                sbString.append("        AND D.STATUS = 1)\n");
            }
        }


        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT OFW.* ");
        sql.append(" FROM VW_PART_ORDER_FOLLOW_U2 OFW ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);
        if ("".equals(orderBy) || null == orderBy) {
            sql.append(" ORDER BY OFW.SUBMIT_DATE  DESC");
        } else {
            if ("1".equals(orderBy)) {
                sql.append(" ORDER BY OFW.ORDER_CODE  ");
            } else if ("2".equals(orderBy)) {
                sql.append(" ORDER BY OFW.SUBMIT_DATE ");
            } else if ("3".equals(orderBy)) {
                sql.append(" ORDER BY OFW.DEALER_CODE ");
            } else if ("4".equals(orderBy)) {
                sql.append(" ORDER BY OFW.CODE_DESC ");
            } else if ("5".equals(orderBy)) {
                sql.append(" ORDER BY OFW.CREATE_DATE ");
            }


        }

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param request
     * @return
     * @Title 配件订单总数、金额、发运数量、发运金额
     */
    public Map<String, Object> querySumPartDlrOrder(RequestWrapper request, AclUserBean logonUser) {

        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode")); // 订单单号
        String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 订单类型
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName2")); // 订货单位
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 订货单位编码-弹出框
        String isSaler = CommonUtils.checkNull(request.getParamValue("isSaler")); // 是否销售单位
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode")); //流水号
        String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); //大区
        String dealerCode2 = CommonUtils.checkNull(request.getParamValue("dealerCode2")); // 订货单位编码
        String cmmSDate = CommonUtils.checkNull(request.getParamValue("cmmSDate")); // 提交开始时间
        String cmmEDate = CommonUtils.checkNull(request.getParamValue("cmmEDate")); // 提交截止时间
        String transSDate = CommonUtils.checkNull(request.getParamValue("transSDate")); // 发运开始时间
        String transEDate = CommonUtils.checkNull(request.getParamValue("transEDate")); // 发运截止时间
        String transCode = CommonUtils.checkNull(request.getParamValue("transCode")); // 发运单号
        String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldCode")); // 配件编码
        String partName = CommonUtils.checkNull(request.getParamValue("partName")); // 配件名称
        String soSate = CommonUtils.checkNull(request.getParamValue("soSate")); // 销售单状态

        String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID

        StringBuffer sbString = new StringBuffer();

        //汇总SQL判断条件
        //1 订单提交日期和发运日期都为空
        String sumFlag = "0";
        //2 订单提交日期和发运日期都不为空(查询在提交日期内的订单的在发运时间内的发运情况)
        if (((!"".equals(cmmSDate) && null != cmmSDate) || (!"".equals(cmmEDate) && null != cmmEDate))
                && ((!"".equals(transSDate) && null != transSDate) || (!"".equals(transEDate) && null != transEDate))
                ) {
            sumFlag = "1";
        }
        //3 订单提交日期不为空、发运日期为空(查询提交日期内的订单的发运情况)
        if (((!"".equals(cmmSDate) && null != cmmSDate) || (!"".equals(cmmEDate) && null != cmmEDate))
                && (("".equals(transSDate) || null == transSDate) && ("".equals(transEDate) || null == transEDate))
                ) {
            sumFlag = "2";
            if (!"".equals(transCode) && null != transCode) {
                sumFlag = "5";
            }
        }
        //4 订单提交日期为空、发运日期不为空(查询发运时间内的发运情况)
        if ((("".equals(cmmSDate) || null == cmmSDate) || ("".equals(cmmEDate) || null == cmmEDate))
                && ((!"".equals(transSDate) && null != transSDate) || (!"".equals(transEDate) && null != transEDate))
                ) {
            sumFlag = "3";
        }

        //配件销售员订单类型限制
        String userType = null;
        TtPartUserposeDefinePO userposeDefinePO = new TtPartUserposeDefinePO();
        userposeDefinePO.setUserId(logonUser.getUserId());
        if (dao.select(userposeDefinePO).size() > 0) {
            userposeDefinePO = (TtPartUserposeDefinePO) dao.select(userposeDefinePO).get(0);
            userType = userposeDefinePO.getUserType() + "";
        }

        StringBuffer sql = new StringBuffer("");

        //订单查出库单

        //拼装SQL开始
        if (null != orderCode && !"".equals(orderCode)) {
            sbString.append(" AND UPPER(OFW.ORDER_CODE) like '%" + orderCode.trim().toUpperCase() + "%'\n");
        }
        if (null != orderType && !"".equals(orderType)) {
            sbString.append(" AND OFW.ORDER_TYPE = '" + orderType + "'\n");
        }
        if (null != dealerName && !"".equals(dealerName)) {
            sbString.append(" AND OFW.DEALER_NAME LIKE '%" + dealerName.trim() + "%'\n");
        }
        if (!"".equals(dealerCode) && null != dealerCode) {
            sbString.append(" AND OFW.DEALER_CODE in (" + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(dealerCode) + ")\n");
        }
        if (!"".equals(dealerCode2) && null != dealerCode2) {
            sbString.append(" AND OFW.DEALER_CODE like  '%" + dealerCode2.trim().toUpperCase() + "%'\n");
        }
        if (null != soCode && !"".equals(soCode)) {
            sbString.append(" AND UPPER(OFW.SO_CODE) like '%" + soCode.trim().toUpperCase() + "%'\n");
        }

        //增加大区人员限制 1128
        if (dao.getPoseRoleId(logonUser.getPoseId().toString()).equals("4000005644")) {
            sbString.append(CommonUtils.getOrgDealerLimitSqlByPose("OFW", logonUser));
        }
        //增加大区限制
        if (!"".equals(orgCode) && null != orgCode) {
            sbString.append("AND EXISTS (SELECT 1\n");
            sbString.append("         FROM VW_ORG_DEALER_SERVICE S\n");
            sbString.append("        WHERE S.DEALER_ID = OFW.DEALER_ID\n");
            sbString.append("          AND S.Org_Code IN (" + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(orgCode) + "))\n");
        }

        //查询权限控制
        if (null == isSaler || "".equals(isSaler) || "NULL".equalsIgnoreCase(isSaler)) {
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND (OFW.DEALER_ID = '" + parentOrgId + "' OR  OFW.SELLER_ID = '" + parentOrgId + "')\n");
            }
        } else if ((Constant.IF_TYPE_YES + "").equals(isSaler)) {
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND OFW.SELLER_ID = '" + parentOrgId + "'\n");
            }
        } else {
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND OFW.DEALER_ID = '" + parentOrgId + "' ");
            }
        }
        //订单提交日期
        if (null != cmmSDate && !"".equals(cmmSDate)) {
            sbString.append(" AND TO_CHAR(OFW.SUBMIT_DATE,'yyyy-MM-dd') >= '" + cmmSDate + "'\n");
        }
        if (null != cmmEDate && !"".equals(cmmEDate)) {
            sbString.append(" AND TO_CHAR(OFW.SUBMIT_DATE,'yyyy-MM-dd') <= '" + cmmEDate + "'\n");
        }
        //配件编码和名称查询
        if ((!"".equals(partOldCode) && null != partOldCode) || (!"".equals(partName) && null != partName)) {
            sbString.append("AND EXISTS (SELECT 1\n");
            sbString.append("       FROM TT_PART_DLR_ORDER_DTL OD, TT_PART_DEFINE PD\n");
            sbString.append("      WHERE OD.PART_ID = PD.PART_ID\n");
            sbString.append("        AND OD.ORDER_ID = OFW.ORDER_ID\n");
            if (!"".equals(partName) && null != partName) {
                sbString.append("        AND PD.PART_CNAME LIKE '%" + partName + "%'\n");
            }
            if (!"".equals(partOldCode) && null != partOldCode) {
                sbString.append("        AND PD.PART_OLDCODE LIKE '%" + partOldCode + "%'\n");
            }
            sbString.append("\t\t\t\t)");
        }

        if (!"".equals(userType) && null != userType) {
                /*if ("3".equals(userType)) {
                    sbString.append(" AND EXISTS (SELECT 1\n");
                    sbString.append("       FROM TT_PART_USER_ORDERTYPE_DEFINE D\n");
                    sbString.append("      WHERE D.ORDER_TYPE = OFW.ORDER_TYPE\n");
                    sbString.append("        AND D.USER_ID = " + logonUser.getUserId() + "\n");
                    sbString.append("        AND D.STATE = 10011001\n");
                    sbString.append("        AND D.STATUS = 1)\n");
                }*/
        }
        if (!"".equals(soSate) && null != soSate) {
            sbString.append("AND EXISTS (SELECT 1\n");
            sbString.append("           FROM VW_PART_ORDER_FOLLOW_U2 U2\n");
            sbString.append("          WHERE U2.ORDER_ID = OFW.ORDER_ID\n");
            sbString.append("            AND U2.SO_STATE LIKE '%" + soSate + "%')");
        }
        //拼SQL结束
        if ("1".equals(sumFlag) || "2".equals(sumFlag)) {
            sql.append("WITH PART_ORDER_OUT AS\n");
            sql.append(" (SELECT COUNT(DISTINCT TD.TRPLAN_ID) OUTCNT,\n");
            sql.append("         SUM(TD.OUTSTOCK_QTY) OUTQTY,\n");
            sql.append("         SUM(TD.SALE_AMOUNT) OUTAMOUNT\n");
            sql.append("    FROM VW_PART_TRANS_DTL TD\n");
            sql.append("   WHERE 1 = 1\n");
            if (!"".equals(transSDate) && null != transSDate) {
                sql.append("          AND trunc(TD.SUBMIT_DATE) >= TO_DATE('" + transSDate + "', 'yyyy-mm-dd')\n");
            }
            if (!"".equals(transEDate) && null != transEDate) {
                sql.append("          AND trunc(TD.SUBMIT_DATE) <= TO_DATE('" + transEDate + "', 'yyyy-mm-dd')\n");
            }
            if (null != transCode && !"".equals(transCode)) {
                sql.append("          AND TD.TRPLAN_CODE LIKE '%" + transCode + "%'\n");
            }
            sql.append("     AND EXISTS\n");
            sql.append("   (SELECT 1\n");
            sql.append("            FROM VW_PART_DLR_ORDER_TRANS OFW\n");
            sql.append("           WHERE TD.ORDER_ID = OFW.ORDER_ID\n");
            sql.append(sbString.toString());
            sql.append("                     )),\n");
            sql.append("PART_ORDER AS\n");
            sql.append(" (SELECT COUNT(DISTINCT OFW.ORDER_CODE) ORDERCNT,\n");
            sql.append("         SUM(OFW.BUY_QTY) ORDERQTY,\n");
            sql.append("         SUM(OFW.ORDER_AMOUNT) ORDERAMOUNT,\n");
            sql.append("         SUM(OFW.OUTSTOCK_QTY) OUTQTY,\n");
            sql.append("         SUM(OFW.SALES_AMOUNT) OUTAMOUNT\n");
            sql.append("    FROM VW_PART_DLR_ORDER_TRANS OFW\n");
            sql.append("   WHERE 1 = 1\n");
            //发运日期和发运单号查询
            if ((!"".equals(transSDate) && null != transSDate) || (!"".equals(transEDate) && null != transEDate) ||
                    null != transCode && !"".equals(transCode)) {
                sbString.append("  AND EXISTS\n");
                sbString.append("(SELECT 1\n");
                sbString.append("         FROM VW_PART_TRANS_DTL TD\n");
                sbString.append("        WHERE TD.ORDER_ID = OFW.ORDER_ID\n");
                if (!"".equals(transSDate) && null != transSDate) {
                    sbString.append("          AND trunc(TD.SUBMIT_DATE) >= TO_DATE('" + transSDate + "', 'yyyy-mm-dd')\n");
                }
                if (!"".equals(transEDate) && null != transEDate) {
                    sbString.append("          AND trunc(TD.SUBMIT_DATE) <= TO_DATE('" + transEDate + "', 'yyyy-mm-dd')\n");
                }
                if (null != transCode && !"".equals(transCode)) {
                    sbString.append("          AND TD.TRPLAN_CODE LIKE '%" + transCode + "%'");
                }
                sbString.append("\t\t\t\t)");
            }
            sql.append(sbString.toString());
            sql.append("              )\n");
            sql.append("SELECT SUM(PO.ORDERCNT) ORDERCNT,\n");
            sql.append("       SUM(PO.ORDERQTY) ORDERQTY,\n");
            sql.append("       SUM(PO.ORDERAMOUNT) ORDERAMOUNT,\n");
            sql.append("       SUM(PO.OUTQTY) OUTQTY,\n");
            sql.append("       SUM(PO.OUTAMOUNT) OUTAMOUNT,\n");
            sql.append("       SUM(OT.OUTCNT) OUTCNT\n");
            sql.append("  FROM PART_ORDER PO, PART_ORDER_OUT OT");
        }
        //发运单查询订单

        if ("3".equals(sumFlag) || "0".equals(sumFlag) || "5".equals(sumFlag)) {
            //发运日期和发运单号查询
            if (!"".equals(transSDate) && null != transSDate) {
                sbString.append("          AND trunc(OFW.SUBMIT_DATE) >= TO_DATE('" + transSDate + "', 'yyyy-mm-dd')\n");
            }
            if (!"".equals(transEDate) && null != transEDate) {
                sbString.append("          AND trunc(OFW.SUBMIT_DATE) <= TO_DATE('" + transEDate + "', 'yyyy-mm-dd')\n");
            }
            if (null != transCode && !"".equals(transCode)) {
                sbString.append("          AND OFW.TRPLAN_CODE LIKE '%" + transCode + "%'");
            }
            sql.append("WITH PART_ORDER_OUT AS\n");
            sql.append(" (SELECT COUNT(DISTINCT OFW.TRPLAN_ID) OUTCNT,\n");
            sql.append("         SUM(OFW.OUTSTOCK_QTY) OUTQTY,\n");
            sql.append("         SUM(OFW.SALE_AMOUNT) OUTAMOUNT\n");
            sql.append("    FROM VW_PART_TRANS_DTL OFW\n");
            sql.append("   WHERE 1 = 1\n");
            sql.append(sbString.toString());
            sql.append("             ),\n");
            sql.append("PART_ORDER AS\n");
            sql.append(" (SELECT COUNT(DISTINCT TD.ORDER_CODE) ORDERCNT,\n");
            sql.append("         SUM(TD.BUY_QTY) ORDERQTY,\n");
            sql.append("         SUM(TD.ORDER_AMOUNT) ORDERAMOUNT,\n");
            sql.append("         SUM(TD.OUTSTOCK_QTY) OUTQTY,\n");
            sql.append("         SUM(TD.SALES_AMOUNT) OUTAMOUNT\n");
            sql.append("    FROM VW_PART_DLR_ORDER_TRANS TD\n");
            sql.append("   WHERE 1 = 1\n");
            sql.append("     AND EXISTS\n");
            sql.append("   (SELECT 1\n");
            sql.append("            FROM VW_PART_TRANS_DTL OFW\n");
            sql.append("           WHERE TD.ORDER_ID = OFW.ORDER_ID\n");
//            sql.append("             AND TD.PART_ID = OFW.PART_ID\n");
            sql.append(sbString.toString());
            sql.append("                 ))\n");
            sql.append("SELECT SUM(PO.ORDERCNT) ORDERCNT,\n");
            sql.append("       SUM(PO.ORDERQTY) ORDERQTY,\n");
            sql.append("       SUM(PO.ORDERAMOUNT) ORDERAMOUNT,\n");
            sql.append("       SUM(ot.OUTQTY) OUTQTY,\n");
            sql.append("       SUM(ot.OUTAMOUNT) OUTAMOUNT,\n");
            sql.append("       SUM(OT.OUTCNT) OUTCNT\n");
            sql.append("  FROM PART_ORDER PO, PART_ORDER_OUT OT");
        }

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());

        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

    /**
     * @param request
     * @return
     * @Title 配件发运数量
     */
    public Map<String, Object> querySumPartNum(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT COUNT(1) PKG_NUM ");
        sql.append(" FROM VW_PART_ORDER_FOLLOW OFW ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);
        sql.append(" and TRANS_DATE is not null");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());

        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-14
     * @Title : 配件订单Detail信息查询
     */
    public PageResult<Map<String, Object>> queryPartOrderDtlInfos(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

//		sql.append(sbString);
//		sql.append(" ORDER BY OM.ORDER_CODE DESC ");


        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param dealerName
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-15
     * @Title : 返回配件订单Main信息List
     */
    public List<Map<String, Object>> queryPartOrderMainInfosList(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT OFW.* ");
        sql.append(" FROM VW_PART_ORDER_FOLLOW OFW ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);
        sql.append(" ORDER BY OFW.OD_SUBMIT_DATE DESC ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-15
     * @Title : 获取订单单据操作信息List
     */
    public List<Map<String, Object>> getOrderOperationHistory(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT OH.OPT_ID, OH.BUSSINESS_ID, OH.OPT_BY, OH.OPT_NAME, OH.WHAT, OH.STATUS,OH.OPT_TYPE, "
                        + " OH.REMARK, OH.ORDER_ID, TO_CHAR(OH.OPT_DATE,'yyyy-MM-dd hh24:mi:ss') AS  OPT_DATE"
                        + " FROM TT_PART_OPERATION_HISTORY OH "
                        + " WHERE 1 = 1  ");
        sql.append(" AND OH.OPT_TYPE = '" + optType + "' ");
        sql.append(sbString);
        sql.append(" ORDER BY OH.OPT_DATE ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param oemCompanyId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回主机厂名称
     */
    public String getMainCompanyName(String oemCompanyId) {
        String companyName = "";
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.COMPANY_NAME "
                        + " FROM TM_COMPANY TM "
                        + " WHERE 1 = 1  ");

        sql.append("  AND TM.COMPANY_ID = '" + oemCompanyId + "' ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            companyName = list.get(0).get("COMPANY_NAME").toString();
        }

        return companyName;
    }

    /**
     * @param : @param oemCompanyId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回服务商名称
     */
    public String getDealerName(String oemCompanyId) {
        String companyName = "";
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TD.DEALER_NAME "
                        + " FROM TM_DEALER TD "
                        + " WHERE 1 = 1  ");

        sql.append("  AND TD.DEALER_ID = '" + oemCompanyId + "' ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            companyName = list.get(0).get("DEALER_NAME").toString();
        }

        return companyName;
    }

    public String getPoseRoleId(String poseId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM tr_role_pose p WHERE p.pose_id=" + poseId + "");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null || list.get(0).get("ROLE_ID") == null) {
            return "0";
        }
        return CommonUtils.checkNull(list.get(0).get("ROLE_ID"));
    }
}
