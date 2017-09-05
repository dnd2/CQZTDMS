package com.infodms.dms.dao.parts.financeManager.dealerAccQueryManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : huchao
 *         CreateDate     : 2013-4-10
 * @ClassName : partPlannerWarehouseDao
 */
public class partDealerAccQueryDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partDealerAccQueryDao.class);
    private static final partDealerAccQueryDao dao = new partDealerAccQueryDao();

    private partDealerAccQueryDao() {
    }

    public static final partDealerAccQueryDao getInstance() {
        return dao;
    }

    private static final String USER_ROLE_PDR = "2"; //供应中心

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param orderID
     * @param : @param partCode
     * @param : @param partName
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title : 条件查询服务商账户信息
     */
    public PageResult<Map<String, Object>> queryPartDealerAccount(RequestWrapper request, String parentOrgId, AclUserBean logonUser, int pageSize, int curPage) {
        String accountKind = CommonUtils.checkNull(request.getParamValue("accountKind")); // 资金类型
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 服务商编码
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 服务商名称
        String userRole = CommonUtils.checkNull(request.getParamValue("userRole")); // 服务商角色
        String accountPurpose = CommonUtils.checkNull(request.getParamValue("accountPurpose")); // 服务商角色

        StringBuffer sql = new StringBuffer("");
        //统一修改为从vw_part_dlr_account取值
        sql.append("SELECT VWW1.* FROM (SELECT VW.ACCOUNT_ID,\n");
        sql.append("       VW.ACCOUNT_KIND,\n");
        sql.append("       VW.ACCOUNT_PURPOSE,\n");
        sql.append("       VW.CHILDORG_ID,\n");
        sql.append("       VW.DEALER_CODE,\n");
        sql.append("       VW.DEALER_NAME,\n");
        sql.append("       VW.PARENTORG_ID,\n");
        sql.append("       VW.PARENTORG_CODE,\n");
        sql.append("       VW.PARENTORG_NAME,\n");
        sql.append("       TO_CHAR(VW.ACCOUNT_SUM, 'fm999,999,999,990.00') AS ACCOUNT_SUM,\n");
        sql.append("       TO_CHAR(VW.ACCOUNT_DJ, 'fm999,999,999,990.00') AS PREEMPTIONVALUE,\n");
        sql.append("       TO_CHAR(VW.ACCOUNT_KY, 'fm999,999,999,990.00') USEABLEACCOUNT,\n");
        sql.append("       TO_CHAR(VW.CASH_KY, 'fm999,999,999,990.00') CASH_KY,\n");
        sql.append("       TO_CHAR(VW.CREDIT_LIMIT, 'fm999,999,999,990.00') CREDIT_LIMIT,\n");
        sql.append("       TO_CHAR(VW.CREDIT_LIMIT, 'fm999999999990.00') CREDIT_LIMIT_UFM,\n");
        sql.append("       TO_CHAR(VW.ACCount_INVO, 'fm999,999,999,990.00') ACCOUNT_INVO\n");
        sql.append("  FROM VW_PART_DLR_ACCOUNT VW\n");
        sql.append("  Where 1= 1\n");

        if (!"".equals(dealerCode) && null != dealerCode) {
            sql.append(" AND UPPER(VW.DEALER_CODE)  in  (" + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(dealerCode) + ")\n");
        }
       /* if (null != dealerName && !dealerName.equals("")) {
            sql.append(" AND VW.DEALER_NAME LIKE '%" + dealerName.trim() + "%' ");
        }*/
        if (null != parentOrgId && !parentOrgId.equals("")) {//modify by yuan 20130527
            sql.append(" AND ( VW.PARENTORG_ID  = " + parentOrgId).append(" OR  VW.CHILDORG_ID = " + parentOrgId + ")");
        }

        if (null != accountKind && !accountKind.equals("")) {
            sql.append(" AND VW.ACCOUNT_KIND  = '" + accountKind + "' ");
        }
        if (null != accountPurpose && !accountPurpose.equals("")) {
            sql.append(" AND VW.ACCOUNT_PURPOSE  = '" + accountPurpose + "' ");
        }
        //add 大区服务经理区域限制 04-04-14
        if (this.getPoseRoleId(logonUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
            sql.append(CommonUtils.getOrgDealerLimitSqlByPose("vw", logonUser));
        }
        sql.append(" ORDER BY VW.DEALER_CODE) VWW1 ");

        if (USER_ROLE_PDR.equals(userRole)) {
            sql.append(" UNION ");
            sql.append("SELECT VWW2.* FROM (SELECT VW.ACCOUNT_ID,\n");
            sql.append("       VW.ACCOUNT_KIND,\n");
            sql.append("       VW.CHILDORG_ID,\n");
            sql.append("       VW.DEALER_CODE,\n");
            sql.append("       VW.DEALER_NAME,\n");
            sql.append("       VW.PARENTORG_ID,\n");
            sql.append("       VW.PARENTORG_CODE,\n");
            sql.append("       VW.PARENTORG_NAME,\n");
            sql.append("       TO_CHAR(VW.ACCOUNT_SUM, 'fm999,999,999,990.00') AS ACCOUNT_SUM,\n");
            sql.append("       TO_CHAR(VW.ACCOUNT_DJ, 'fm999,999,999,990.00') AS PREEMPTIONVALUE,\n");
            sql.append("       TO_CHAR(VW.ACCOUNT_KY, 'fm999,999,999,990.00') USEABLEACCOUNT,\n");
            sql.append("       TO_CHAR(VW.CASH_KY, 'fm999,999,999,990.00') CASH_KY,\n");
            sql.append("       TO_CHAR(VW.CREDIT_LIMIT, 'fm999,999,999,990.00') CREDIT_LIMIT,\n");
            sql.append("       TO_CHAR(VW.ACCount_INVO, 'fm999,999,999,990.00') ACCOUNT_INVO\n");
            sql.append("  FROM VW_PART_DLR_ACCOUNT VW, VW_PART_DLR_ACCOUNT VW1\n");
            sql.append("  Where 1= 1\n");
            sql.append(" AND VW.CHILDORG_ID = VW1.CHILDORG_ID ");
            sql.append(" AND VW1.PARENTORG_ID = '" + parentOrgId + "' ");

            if (!"".equals(dealerCode) && null != dealerCode) {
                sql.append(" AND UPPER(VW.DEALER_CODE)  in  (" + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(dealerCode) + ")\n");
            }
           /* if (null != dealerName && !dealerName.equals("")) {
                sql.append(" AND VW.DEALER_NAME LIKE '%" + dealerName.trim() + "%' ");
            }*/
            if (null != parentOrgId && !parentOrgId.equals("")) {
                sql.append(" AND VW.PARENTORG_ID  = '" + Constant.OEM_ACTIVITIES + "' ");
            }
            //add 大区服务经理区域限制 04-04-14
            if (this.getPoseRoleId(logonUser.getPoseId().toString()).equals("4000005644")) {
                sql.append(CommonUtils.getOrgDealerLimitSqlByPose("vw", logonUser));
            }
            if (null != accountKind && !accountKind.equals("")) {
                sql.append(" AND VW.ACCOUNT_KIND  = '" + accountKind + "' ");
            }

            sql.append(" ORDER BY VW.DEALER_CODE) VWW2");
        }

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param dealerName
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title : 返回所有服务商账户信息
     */
    public List<Map<String, Object>> queryAllPartDealerAccount(RequestWrapper request, String parentOrgId) {
        String accountKind = CommonUtils.checkNull(request.getParamValue("accountKind")); // 资金类型
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 服务商编码
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 服务商名称
        String userRole = CommonUtils.checkNull(request.getParamValue("userRole")); // 服务商角色
        String accountPurpose = CommonUtils.checkNull(request.getParamValue("accountPurpose")); // 服务商角色

        StringBuffer sql = new StringBuffer("");
        //统一修改为从vw_part_dlr_account取值
        sql.append("SELECT VWW1.* FROM (SELECT VW.ACCOUNT_ID,\n");
        sql.append("       VW.ACCOUNT_KIND,\n");
        sql.append("       VW.ACCOUNT_PURPOSE,\n");
        sql.append("       VW.CHILDORG_ID,\n");
        sql.append("       VW.DEALER_CODE,\n");
        sql.append("       VW.DEALER_NAME,\n");
        sql.append("       VW.PARENTORG_ID,\n");
        sql.append("       VW.PARENTORG_CODE,\n");
        sql.append("       VW.PARENTORG_NAME,\n");
        sql.append("       TO_CHAR(VW.ACCOUNT_SUM, 'fm999,999,999,990.00') AS ACCOUNT_SUM,\n");
        sql.append("       TO_CHAR(VW.ACCOUNT_DJ, 'fm999,999,999,990.00') AS PREEMPTIONVALUE,\n");
        sql.append("       TO_CHAR(VW.ACCOUNT_KY, 'fm999,999,999,990.00') USEABLEACCOUNT,\n");
        sql.append("       TO_CHAR(VW.CASH_KY, 'fm999,999,999,990.00') CASH_KY,\n");
        sql.append("       TO_CHAR(VW.CREDIT_LIMIT, 'fm999,999,999,990.00') CREDIT_LIMIT,\n");
        sql.append("       TO_CHAR(VW.ACCount_INVO, 'fm999,999,999,990.00') ACCount_INVO\n");
        sql.append("  FROM VW_PART_DLR_ACCOUNT VW\n");
        sql.append("  Where 1= 1\n");

        if (!"".equals(dealerCode) && null != dealerCode) {
            sql.append(" AND UPPER(VW.DEALER_CODE)  in  (" + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(dealerCode) + ")\n");
        }
       /* if (null != dealerName && !dealerName.equals("")) {
            sql.append(" AND VW.DEALER_NAME LIKE '%" + dealerName.trim() + "%' ");
        }*/
        if (null != parentOrgId && !parentOrgId.equals("")) {//modify by yuan 20130527
            sql.append(" AND ( VW.PARENTORG_ID  = " + parentOrgId).append(" OR  VW.CHILDORG_ID = " + parentOrgId + ")");
        }

        if (null != accountKind && !accountKind.equals("")) {
            sql.append(" AND VW.ACCOUNT_KIND  = '" + accountKind + "' ");
        }
        if (null != accountPurpose && !accountPurpose.equals("")) {
            sql.append(" AND VW.ACCOUNT_PURPOSE  = '" + accountPurpose + "' ");
        }
        sql.append(" ORDER BY VW.DEALER_CODE) VWW1 ");

        if (USER_ROLE_PDR.equals(userRole)) {
            sql.append(" UNION ");
            sql.append("SELECT VWW2.* FROM (SELECT VW.ACCOUNT_ID,\n");
            sql.append("       VW.ACCOUNT_KIND,\n");
            sql.append("       VW.CHILDORG_ID,\n");
            sql.append("       VW.DEALER_CODE,\n");
            sql.append("       VW.DEALER_NAME,\n");
            sql.append("       VW.PARENTORG_ID,\n");
            sql.append("       VW.PARENTORG_CODE,\n");
            sql.append("       VW.PARENTORG_NAME,\n");
            sql.append("       TO_CHAR(VW.ACCOUNT_SUM, 'fm999,999,999,990.00') AS ACCOUNT_SUM,\n");
            sql.append("       TO_CHAR(VW.ACCOUNT_DJ, 'fm999,999,999,990.00') AS PREEMPTIONVALUE,\n");
            sql.append("       TO_CHAR(VW.ACCOUNT_KY, 'fm999,999,999,990.00') USEABLEACCOUNT,\n");
            sql.append("       TO_CHAR(VW.CASH_KY, 'fm999,999,999,990.00') CASH_KY,\n");
            sql.append("       TO_CHAR(VW.CREDIT_LIMIT, 'fm999,999,999,990.00') CREDIT_LIMIT,\n");
            sql.append("       TO_CHAR(VW.ACCount_INVO, 'fm999,999,999,990.00') ACCount_INVO\n");
            sql.append("  FROM VW_PART_DLR_ACCOUNT VW, VW_PART_DLR_ACCOUNT VW1\n");
            sql.append("  Where 1= 1\n");
            sql.append(" AND VW.CHILDORG_ID = VW1.CHILDORG_ID ");
            sql.append(" AND VW1.PARENTORG_ID = '" + parentOrgId + "' ");

            if (!"".equals(dealerCode) && null != dealerCode) {
                sql.append(" AND UPPER(VW.DEALER_CODE)  in  (" + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(dealerCode) + ")\n");
            }
           /* if (null != dealerName && !dealerName.equals("")) {
                sql.append(" AND VW.DEALER_NAME LIKE '%" + dealerName.trim() + "%' ");
            }*/
            if (null != parentOrgId && !parentOrgId.equals("")) {
                sql.append(" AND VW.PARENTORG_ID  = '" + Constant.OEM_ACTIVITIES + "' ");
            }

            if (null != accountKind && !accountKind.equals("")) {
                sql.append(" AND VW.ACCOUNT_KIND  = '" + accountKind + "' ");
            }

            sql.append(" ORDER BY VW.DEALER_CODE) VWW2");
        }

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @param whereSql
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title : 返回服务商信息
     */
    public PageResult<Map<String, Object>> getDealers(int pageSize, int curPage, String sbString) {
        PageResult<Map<String, Object>> result = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT TD.DEALER_ID, TD.DEALER_CODE, TD.DEALER_NAME " +
                "FROM TM_DEALER TD, TT_PART_ACCOUNT_DEFINE PA " +
                "WHERE PA.CHILDORG_ID = TD.DEALER_ID(+)  ");
        sql.append(sbString);

        result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return result;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title : 获取服务商名称
     */
    public String getDealerName(String sbString) {
        String dealerName = "";
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT TD.DEALER_NAME " +
                " FROM TM_DEALER TD " +
                " WHERE 1 = 1");
        sql.append(sbString);

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            dealerName = list.get(0).get("DEALER_NAME").toString();
        }
        return dealerName;
    }

    /**
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title : 条件查询资金占用明细
     */
    public PageResult<Map<String, Object>> queryPartDealerPreemptionDetail(int pageSize, int curPage, String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT AC.RECORD_ID, AC.ACCOUNT_ID, AC.SOURCE_ID, AC.SOURCE_CODE, AC.ORDER_ID, AC.ORDER_CODE, TO_CHAR(AC.AMOUNT,'fm999,999,999,990.00') AS AMOUNT, TO_CHAR(AC.INVO_AMOUNT,'fm999,999,999,990.00') AS INVO_AMOUNT, AC.CHANGE_TYPE, TD.DEALER_NAME, U.NAME, AC.CREATE_DATE, AC.INVOICE_NO "
                        + " FROM TM_DEALER TD, TT_PART_ACCOUNT_RECORD AC, TT_PART_ACCOUNT_DEFINE PA, TC_USER U "
                        + " WHERE AC.DEALER_ID = TD.DEALER_ID AND PA.ACCOUNT_ID = AC.ACCOUNT_ID AND AC.CREATE_BY = U.USER_ID ");

        sql.append(sbString);
        //add  by yaun 20130921 start
        sql.append(" AND ac.state=").append(Constant.STATUS_ENABLE);
        sql.append(" AND ac.status=1");
        //end
        sql.append(" And ( AC.AMOUNT <> 0 OR AC.INVO_AMOUNT <> 0 ) ");
        sql.append(" ORDER BY AC.CREATE_DATE DESC");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title : 条件查询资金占用明细
     */
    public PageResult<Map<String, Object>> queryPartDealerPreemptionDetail2(RequestWrapper request, int pageSize, int curPage) {
        String dealerId = request.getParamValue("dealerId");// 服务商ID
        String parentOrgIdHidden = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
        String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate"));// 开始时间
        String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate"));// 结束时间
//				String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));// 业务单号(销售单、出库单)
        String invNo = CommonUtils.checkNull(request.getParamValue("invNo"));// 发票号
        String accountId = CommonUtils.checkNull(request.getParamValue("ACCOUNT_ID"));//

        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM VW_PART_BILL T WHERE 1 = 1\n");
        if (Utility.testString(dealerId)) {
            sql.append(" AND T.DEALER_ID = '" + dealerId.trim() + "'  \n");
        }
        if (null != invNo && !"".equals(invNo)) {
            sql.append(" AND UPPER(T.INVOICE_NO) LIKE '%" + invNo.trim().toUpperCase() + "%'\n ");
        }
        if (null != startDate && !"".equals(startDate)) {
            sql.append(" AND TO_CHAR(T.CREATE_DATE,'yyyy-MM-dd') >= '" + startDate + "'\n ");
        }
        if (null != endDate && !"".equals(endDate)) {
            sql.append(" AND TO_CHAR(T.CREATE_DATE,'yyyy-MM-dd') <= '" + endDate + "'\n ");
        }
                /*if (null != parentOrgIdHidden && !"".equals(parentOrgIdHidden)) {
                    sb.append(" AND  T.PARENTORG_ID = '" + parentOrgIdHidden + "' ");
                }*/
        if (null != accountId && !"".equals(accountId)) {
            sql.append(" AND  T.ACCOUNT_ID = '" + accountId + "' \n");
        }
        sql.append("ORDER BY T.CREATE_DATE DESC\n");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title : 返回查询资金占用明细List
     */
    public List<Map<String, Object>> queryAllPartDealerPreemptionDetail(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT AC.SOURCE_ID, AC.ORDER_ID, TO_CHAR(AC.AMOUNT,'999,999,990.99') AS AMOUNT,AC.CHANGE_TYPE,TO_CHAR(AC.INVO_AMOUNT,'999,999,990.99') AS INVO_AMOUNT, TO_CHAR((AC.AMOUNT - AC.INVO_AMOUNT),'999,999,990.99') AS CURR_AMOUNT, TD.DEALER_NAME, U.NAME, AC.CREATE_DATE "
                        + " FROM TM_DEALER TD, TT_PART_ACCOUNT_RECORD AC, TT_PART_ACCOUNT_DEFINE PA, TC_USER U "
                        + " WHERE AC.DEALER_ID = TD.DEALER_ID AND PA.ACCOUNT_ID = AC.ACCOUNT_ID AND AC.CREATE_BY = U.USER_ID ");

        sql.append(sbString);
        //add  by yaun 20130921 start
        sql.append(" AND ac.state=").append(Constant.STATUS_ENABLE);
        sql.append(" AND ac.status=1");
        //end
        sql.append(" ORDER BY AC.ACCOUNT_ID, AC.SOURCE_ID, AC.CREATE_DATE DESC");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * : zhengZhiQiang 2014-03-07
     *
     * @param : @param poseId
     * @param : @param rebId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2014-03-11
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

    /**
     * 查询可用余额明细
     *
     * @param pageSize
     * @param curPage
     * @param sbString
     * @return
     */
    public PageResult<Map<String, Object>> queryPartDealerKYDetail(RequestWrapper request, int pageSize, int curPage) {
        String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate"));// 开始时间
        String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate"));// 结束时间
        String accountId = CommonUtils.checkNull(request.getParamValue("ACCOUNT_ID"));// 账号ID
        String remark = CommonUtils.checkNull(request.getParamValue("remark"));// 订单号
        StringBuffer sql = new StringBuffer("");


        sql.append("SELECT *\n");
        sql.append("  FROM VW_PART_DLR_ACCOUNT_HIS H\n");
        sql.append(" WHERE 1 = 1\n");
        if (!"".equals(startDate) && null != startDate) {
            sql.append("   AND TRUNC(H.CREATE_DATE) >= TO_DATE('" + startDate + "', 'yyyy-mm-dd')\n");
        }
        if (!"".equals(endDate) && null != endDate) {
            sql.append("   AND TRUNC(H.CREATE_DATE) <= TO_DATE('" + endDate + "', 'yyyy-mm-dd')\n");
        }
        sql.append("   AND H.ACCOUNT_ID = " + accountId + "");

        if (!"".equals(remark) && null != remark) {
            sql.append(" AND ABSTRACT like '%" + remark + "%'");
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }
}
