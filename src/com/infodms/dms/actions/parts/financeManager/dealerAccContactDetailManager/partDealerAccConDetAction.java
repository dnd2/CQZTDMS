package com.infodms.dms.actions.parts.financeManager.dealerAccContactDetailManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.financeManager.dealerAccContactDetailManager.partDealerAccConDetDao;
import com.infodms.dms.dao.parts.financeManager.dealerAccQueryManager.partDealerAccQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理服务商资金往来明细查询
 * @Description:CHDMS
 * @Date: 2013-4-17
 * @remark
 */
public class partDealerAccConDetAction implements PTConstants {
    public Logger logger = Logger.getLogger(partDealerAccConDetAction.class);
    private static final partDealerAccConDetDao dao = partDealerAccConDetDao.getInstance();
    public static final String DEALER_ACC_CON_DETAIL = "/jsp/parts/financeManager/dealerAccContactDetailManager/partDealerAccContactDetail.jsp";//资金导入记录查询页面

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-17
     * @Title : 跳转至服务商账户往来明细页面
     */
    public void dalerAccConDetInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));//父机构（销售单位）ID
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));//服务商ID
            String accountId = CommonUtils.checkNull(request.getParamValue("ACCOUNT_ID"));//服务商ID
            String accountKind = CommonUtils.checkNull(request.getParamValue("accountKind"));//服务商ID
            String sbStr = "";
            if (null != dealerId && !"".equals(dealerId)) {
                sbStr += " AND TD.DEALER_ID = '" + dealerId + "' ";
            }

            String dealerName = partDealerAccQueryDao.getInstance().getDealerName(sbStr);
            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("dealerName", dealerName);
            act.setOutData("dealerId", dealerId);
            act.setOutData("ACCOUNT_ID", accountId);
            act.setOutData("accountKind", accountKind);
            act.setForword(DEALER_ACC_CON_DETAIL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "账户往来明细查询初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-17
     * @Title : 按条件查询账户往来信息
     */
    public void partDealerAccConDetSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 服务商ID
            String remark = CommonUtils.checkNull(request.getParamValue("remark")); // 备注(财务凭证)
            String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate"));// 开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate"));// 结束时间
//            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String invoiceNO = CommonUtils.checkNull(request.getParamValue("invoiceNO"));// 发票号
            String accountKind = CommonUtils.checkNull(request.getParamValue("accountKind"));// 资金类型
//            String accountId = CommonUtils.checkNull(request.getParamValue("ACCOUNT_ID"));// 资金类型

            String sqlStr = "";
            if (null != dealerId && !"".equals(dealerId)) {
                sqlStr += " AND PA.DEALER_ID = '" + dealerId + "' ";
            }
            if (null != remark && !"".equals(remark)) {
                sqlStr += " AND PA.REMARK LIKE '%" + remark + "%'";
            }
            if (null != startDate && !"".equals(startDate)) {
                sqlStr += " AND TO_CHAR(PA.CREATE_DATE,'yyyy-MM-dd') >= '" + startDate + "' ";
            }
            if (null != endDate && !"".equals(endDate)) {
                sqlStr += " AND TO_CHAR(PA.CREATE_DATE,'yyyy-MM-dd') <= '" + endDate + "' ";
            }
            if (null != accountKind && !accountKind.equals("")) {
                sqlStr += " AND PA.FIN_TYPE  = '" + accountKind + "' ";
            }
            if (null != invoiceNO && !"".equals(invoiceNO)) {
                sqlStr += " AND PA.INVOICE_NO LIKE '%" + invoiceNO + "%' ";
            }
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryDealerAccConDet(12, curPage, sqlStr);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询资金导入记录信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

}
