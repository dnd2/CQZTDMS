package com.infodms.dms.dao.report.partSalesReport;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

public class PartSaleReturnGReportDao extends BaseDao {
    public Logger logger = Logger.getLogger(PartSaleReturnGReportDao.class);
    private static final PartSaleReturnGReportDao dao = new PartSaleReturnGReportDao();

    private PartSaleReturnGReportDao() {
    }

    public static final PartSaleReturnGReportDao getInstance() {
        return dao;
    }

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @param loginUser
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-11-26
     * @Title : 供应中心销售退货查询
     */
    public PageResult<Map<String, Object>> queryGRep(RequestWrapper request, int curPage, int pageSize, AclUserBean loginUser) {
        StringBuffer sql = new StringBuffer();
        String dlrNCode = CommonUtils.checkNull(request.getParamValue("dlrNCode"));//服务商代码
        String dlrNName = CommonUtils.checkNull(request.getParamValue("dlrNName"));//服务商名称
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//供应中心代码
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//供应中心名称
        String start = CommonUtils.checkNull(request.getParamValue("checkSDate"));
        String end = CommonUtils.checkNull(request.getParamValue("checkEDate"));
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
        String partCName = CommonUtils.checkNull(request.getParamValue("partCname"));
        String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldcode"));
        String returnCode = CommonUtils.checkNull(request.getParamValue("returnCode"));//退货单号

        sql.append(" SELECT M.RETURN_CODE, ");
        sql.append(" M.DEALER_CODE, ");
        sql.append(" M.DEALER_NAME, ");
        sql.append(" M.SELLER_CODE, ");
        sql.append(" M.SELLER_NAME, ");
        sql.append(" D.PART_OLDCODE, ");
        sql.append(" D.PART_CNAME, ");
        sql.append(" D.PART_CODE, ");
        sql.append(" D.UNIT, ");
        sql.append(" TC.CODE_DESC PART_TYPE, ");
        sql.append(" D.RETURN_QTY, ");
        sql.append(" D.BUY_PRICE, ");
        sql.append(" D.BUY_AMOUNT, ");
        sql.append(" M.REMARK, ");
        sql.append(" M.CREATE_DATE ");
        sql.append(" FROM TT_PART_DLR_RETURN_DTL D, ");
        sql.append(" TT_PART_DLR_RETURN_MAIN M, ");
        sql.append(" TT_PART_DEFINE TPD, ");
        sql.append(" TC_CODE TC ");
        sql.append(" WHERE D.RETURN_ID = M.RETURN_ID ");
        sql.append(" AND D.PART_ID = TPD.PART_ID ");
        sql.append(" AND TPD.PART_TYPE = TC.CODE_ID ");
        sql.append(" AND M.SELLER_ID != '" + Constant.OEM_ACTIVITIES + "' ");
        sql.append(" AND M.STATE = '" + Constant.PART_DLR_RETURN_STATUS_06 + "' ");
        sql.append(" AND M.SELLER_CODE LIKE 'G%' ");


        if (!"".equals(dlrNCode)) {
            sql.append(" and UPPER(M.DEALER_CODE) LIKE '%").append(dlrNCode.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(dlrNName)) {
            sql.append(" AND M.DEALER_NAME LIKE '%").append(dlrNName.trim()).append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and UPPER(M.SELLER_CODE) LIKE '%").append(dealerCode.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" AND M.SELLER_NAME LIKE '%").append(dealerName.trim()).append("%'");
        }
        if (!"".equals(start)) {
            sql.append(" AND TO_CHAR(M.CREATE_DATE,'yyyy-MM-dd') >= '" + start + "' ");
        }
        if (!"".equals(end)) {
            sql.append(" AND TO_CHAR(M.CREATE_DATE,'yyyy-MM-dd') <= '" + end + "' ");
        }
        if (!"".equals(partCode)) {
            sql.append(" AND UPPER(D.PART_CODE) LIKE '%").append(partCode.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(partCName)) {
            sql.append(" AND D.PART_CNAME LIKE '%").append(partCName.trim()).append("%'");
        }
        if (!"".equals(partOldCode)) {
            sql.append(" AND UPPER(D.PART_OLDCODE) LIKE '%").append(partOldCode.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(returnCode)) {
            sql.append(" AND UPPER(M.RETURN_CODE) LIKE '%").append(returnCode.trim().toUpperCase()).append("%'");
        }

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

}
