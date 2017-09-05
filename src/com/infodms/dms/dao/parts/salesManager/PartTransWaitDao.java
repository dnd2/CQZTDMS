package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartTransWaitDao extends BaseDao<PO> {
    public static Logger logger = Logger.getLogger(PartTransWaitDao.class);
    private static final PartTransWaitDao dao = new PartTransWaitDao();

    private PartTransWaitDao() {
    }

    public static final PartTransWaitDao getInstance() {
        return dao;
    }

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2014-3-31
     * @Title :
     * @Description: 配件待发运查询
     */

    public PageResult<Map<String, Object>> queryDatas(RequestWrapper request, int curPage, int pageSize) {
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE")); //采购订单号
        String soCode = CommonUtils.checkNull(request.getParamValue("SO_CODE"));//销售订单号
        String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//服务商代码
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//服务商名称
        String TstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//开始
        String TendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//结束
        String isSC = CommonUtils.checkNull(request.getParamValue("isSC"));//是否随车
        String transType = CommonUtils.checkNull(request.getParamValue("transType"));//发运方式
        String transportOrg = CommonUtils.checkNull(request.getParamValue("transportOrg"));//承运物流
        String fstartDate = CommonUtils.checkNull(request.getParamValue("fstartDate"));//发运开始
        String fsendDate = CommonUtils.checkNull(request.getParamValue("fsendDate"));//发运结束
        String isFY = CommonUtils.checkNull(request.getParamValue("isFY"));//是否已发运
        String pkgno = CommonUtils.checkNull(request.getParamValue("PKGNO"));//是否已发运
        String trplanCode = CommonUtils.checkNull(request.getParamValue("TRPLAN_CODE"));//配件发运单号
        String billNo = CommonUtils.checkNull(request.getParamValue("BILL_NO"));//整车发运单号
        int yes = Constant.IF_TYPE_YES;
        int no = Constant.IF_TYPE_NO;

        StringBuffer sql = new StringBuffer();

        sql.append("WITH SALES_TRANS AS\n");
        sql.append(" (SELECT F3.ASS_NO, TD.COMPANY_ID, ZA_CONCAT(DISTINCT TSW.BILL_NO) BILL_NO\n");
        sql.append("    FROM TT_SALES_ALLOCA_DE  F1,\n");
        sql.append("         TT_SALES_ASS_DETAIL F2,\n");
        sql.append("         TT_SALES_ASSIGN     F3,\n");
        sql.append("         TT_SALES_WAYBILL    TSW,\n");
        sql.append("         TM_DEALER           TD\n");
        sql.append("   WHERE F1.BILL_ID = TSW.BILL_ID\n");
        sql.append("     AND F1.ASS_DETAIL_ID = F2.ASS_DETAIL_ID\n");
        sql.append("     AND F2.ASS_ID = F3.ASS_ID\n");
        sql.append("     AND F2.STATUS = 10011001\n");
        sql.append("     AND F3.STATUS = 10011001\n");
        sql.append("     AND F3.DEALER_ID = TD.DEALER_ID\n");
        sql.append("     AND EXISTS\n");
        sql.append("   (SELECT 1 FROM TT_PART_PKG_BOX_DTL BD WHERE BD.VIN = F1.BILL_ID)\n");
        sql.append("   GROUP BY F3.ASS_NO, TD.COMPANY_ID)\n");
        sql.append("SELECT ZA_CONCAT(DISTINCT BSR.BO_CODE) BO_CODE,\n");
        sql.append("       ZA_CONCAT(DISTINCT nvl(SM.ORDER_CODE,BSR.order_code)) ORDER_CODE,\n");
        sql.append("       ZA_CONCAT(DISTINCT SM.SO_CODE) SO_CODE,\n");
        sql.append("       SM.DEALER_CODE,\n");
        sql.append("       SM.DEALER_NAME,\n");
        sql.append("       SM.PICK_ORDER_ID,\n");
        sql.append("       BD.CREATE_DATE,\n");
        sql.append("       BD.PKG_NO,\n");
        sql.append("       BD.LENGTH,\n");
        sql.append("       BD.WIDTH,\n");
        sql.append("       BD.HEIGHT,\n");
        sql.append("       BD.WEIGHT,\n");
        sql.append("       BD.VOLUME,\n");
        sql.append("       BD.EQ_WEIGHT,\n");
        sql.append("       BD.CH_WEIGHT,\n");
        sql.append("       ((SELECT ST.BILL_NO\n");
        sql.append("           FROM SALES_TRANS ST, TM_DEALER TD\n");
        sql.append("          WHERE TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("            AND TD.COMPANY_ID = ST.COMPANY_ID\n");
        sql.append("            AND ST.ASS_NO = BD.ASS_NO)) ASS_NO,\n");
        sql.append("       DECODE(((SELECT ST.BILL_NO\n");
        sql.append("                  FROM SALES_TRANS ST, TM_DEALER TD\n");
        sql.append("                 WHERE TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                   AND TD.COMPANY_ID = ST.COMPANY_ID\n");
        sql.append("                   AND ST.ASS_NO = BD.ASS_NO)),\n");
        sql.append("              NULL,\n");
        sql.append("              10041002,\n");
        sql.append("              10041001) ISVIN,\n");
        sql.append("       (CASE\n");
        sql.append("         WHEN P.CREATE_DATE IS NULL AND (SYSDATE - BD.CREATE_DATE) > 3 THEN\n");
        sql.append("          '10041001'\n");
        sql.append("         ELSE\n");
        sql.append("          '10041002'\n");
        sql.append("       END) ISOVER,\n");
        sql.append("       P.TRPLAN_CODE,\n");
//        sql.append("       P.TRANS_TYPE,\n");
//        sql.append("       P.TRANSPORT_ORG,\n");
        sql.append("       (select tv_name from TT_TRANSPORT_VALUATION tv where tv.tv_Id=p.TRANS_TYPE  and STATUS=10011001) TRANS_TYPE,\n");//20170817 update
        sql.append("       (select logi_name from TT_SALES_LOGI sl where sl.logi_code=p.TRANSPORT_ORG and STATUS=10011001)   TRANSPORT_ORG,\n");//20170817 update

        sql.append("       P.CREATE_DATE TRANS_DATE,\n");
        sql.append("       BD.CONFIRM_DATE\n");
        sql.append("  FROM TT_PART_PKG_BOX_DTL         BD,\n");
        sql.append("       TT_PART_SO_MAIN             SM,\n");
        sql.append("       TT_PART_TRANS_PLAN          P,\n");
        sql.append("       VW_PART_BO_ORDERSO_RELATION BSR\n");
        sql.append(" WHERE SM.PICK_ORDER_ID = BD.PICK_ORDER_ID\n");
        sql.append("   AND SM.SO_ID = BSR.SO_ID(+)\n");
        sql.append("   AND BD.TRPLAN_ID = P.TRPLAN_ID(+)\n");

        if (!"".equals(trplanCode) && null != trplanCode) {
            sql.append("  AND P.TRPLAN_CODE LIKE '%" + trplanCode + "%'\n");
        }
        if (!"".equals(billNo) && null != billNo) {
            sql.append("  AND ST.BILL_NO LIKE '%" + billNo + "%'\n");
        }

        if (orderCode != "") {
            sql.append("   AND SM.ORDER_CODE LIKE '%" + orderCode + "%'\n");
        }
        if (soCode != "") {
            sql.append("   AND SM.SO_CODE LIKE '%" + soCode + "%'\n");
        }
        if (dealerCode != "") {
            sql.append("   AND SM.DEALER_CODE LIKE '%" + dealerCode + "%'\n");
        }
        if (dealerName != "") {
            sql.append("   AND SM.DEALER_NAME LIKE '%" + dealerName + "%'\n");
        }
        if ((TstartDate != null && !"".equals(TstartDate)) && (TendDate != null && !"".equals(TendDate))) {
            sql.append("   AND SM.CREATE_DATE BETWEEN TO_DATE('" + TstartDate + "','YYYY-MM-DD') AND TO_DATE('" + TendDate + "','YYYY-MM-DD')\n");
        } else if ((TstartDate == null || "".equals(TstartDate)) && (TendDate != null && !"".equals(TendDate))) {
            sql.append("   AND TRUNC(SM.CREATE_DATE) < TO_DATE('" + TendDate + "','YYYY-MM-DD')\n");
        } else if ((TstartDate != null && !"".equals(TstartDate)) && (TendDate == null || "".equals(TendDate))) {
            sql.append("   AND TRUNC(SM.CREATE_DATE) > TO_DATE('" + TstartDate + "','YYYY-MM-DD') \n");
        }

        if (isSC != null && !"".equals(isSC)) {
            if (isSC.equals(Constant.IF_TYPE_YES + "")) {
                sql.append("AND BD.ASS_NO IS NOT NULL\n");
            } else {
                sql.append("AND BD.ASS_NO IS  NULL\n");
            }
        }
        if (!"".equals(transType) && null != transType) {
            sql.append("   AND P.TRANS_TYPE LIKE '%" + transType + "%'\n");
        }
        if (!"".equals(transportOrg) && null != transportOrg) {
            sql.append("   AND P.TRANSPORT_ORG LIKE '%" + transportOrg + "%'\n");
        }
        if (!"".equals(fstartDate) && fstartDate != null) {
            sql.append("   AND TRUNC(P.CREATE_DATE) >= TO_DATE('" + fstartDate + "','YYYY-MM-DD')\n");
        }
        if (!"".equals(fsendDate) && fsendDate != null) {
            sql.append("   AND TRUNC(P.CREATE_DATE) <= TO_DATE('" + fsendDate + "','YYYY-MM-DD')\n");
        }
        if (!"".equals(isFY) && null != isFY) {
            if (isFY.equals(Constant.IF_TYPE_NO + "")) {
                sql.append("AND BD.TRPLAN_ID IS NULL\n");
            } else {
                sql.append("AND BD.TRPLAN_ID IS NOT　NULL\n");
            }
        }
        if (!"".equals(pkgno) && null != pkgno) {
            sql.append("   AND BD.PKG_NO LIKE '%" + pkgno + "%'\n");
        }

        sql.append("GROUP BY P.TRPLAN_CODE,\n");
        sql.append("         SM.DEALER_CODE,\n");
        sql.append("         SM.DEALER_NAME,\n");
        sql.append("         SM.PICK_ORDER_ID,\n");
        sql.append("         BD.CREATE_DATE,\n");
        sql.append("         BD.PKG_NO,\n");
        sql.append("         BD.LENGTH,\n");
        sql.append("         BD.WIDTH,\n");
        sql.append("         BD.HEIGHT,\n");
        sql.append("         BD.WEIGHT,\n");
        sql.append("         BD.VOLUME,\n");
        sql.append("         BD.EQ_WEIGHT,\n");
        sql.append("         BD.CH_WEIGHT,\n");
        sql.append("         P.TRANS_TYPE,\n");
        sql.append("         P.TRANSPORT_ORG,\n");
        sql.append("         P.CREATE_DATE,\n");
        sql.append("         P.DEALER_ID,\n");
        sql.append("         BD.ASS_NO,\n");
        sql.append("         BD.CONFIRM_DATE\n");
        sql.append("ORDER BY P.CREATE_DATE DESC\n");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> getDatas(RequestWrapper request) {
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE")); //采购订单号
        String soCode = CommonUtils.checkNull(request.getParamValue("SO_CODE"));//销售订单号
        String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//服务商代码
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//服务商名称
        String TstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//开始
        String TendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//结束
        String isSC = CommonUtils.checkNull(request.getParamValue("isSC"));//是否随车
        int yes = Constant.IF_TYPE_YES;
        int no = Constant.IF_TYPE_NO;

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ZA_CONCAT(DISTINCT SM.ORDER_CODE) ORDER_CODE,\n");
        sql.append("       ZA_CONCAT(DISTINCT SM.SO_CODE) SO_CODE,\n");
        sql.append("       SM.DEALER_CODE,\n");
        sql.append("       SM.DEALER_NAME,\n");
        sql.append("       SM.PICK_ORDER_ID,\n");
        sql.append("       BD.CREATE_DATE,\n");
        sql.append("       BD.PKG_NO,\n");
        sql.append("       BD.LENGTH,\n");
        sql.append("       BD.WIDTH,\n");
        sql.append("       BD.HEIGHT,\n");
        sql.append("       BD.WEIGHT,\n");
        sql.append("       BD.VOLUME,\n");
        sql.append("       BD.EQ_WEIGHT,\n");
        sql.append("       BD.CH_WEIGHT,\n");
        sql.append("       BD.ASS_NO,\n");
        sql.append("       DECODE(BD.ASS_NO, NULL, 10041002, 10041001) ISVIN,\n");
        sql.append("       (CASE\n");
        sql.append("         WHEN (SYSDATE - BD.CREATE_DATE) > 3 THEN\n");
        sql.append("          '10041001'\n");
        sql.append("         ELSE\n");
        sql.append("          '10041002'\n");
        sql.append("       END) ISOVER\n");
        sql.append("  FROM TT_PART_PKG_BOX_DTL BD, TT_PART_SO_MAIN SM\n");
        sql.append(" WHERE SM.PICK_ORDER_ID = BD.PICK_ORDER_ID\n");
        sql.append("   AND BD.TRPLAN_ID IS NULL\n");

        if (orderCode != "") {
            sql.append("   AND SM.ORDER_CODE LIKE '%" + orderCode + "%'\n");
        }
        if (soCode != "") {
            sql.append("   AND SM.SO_CODE LIKE '%" + soCode + "%'\n");
        }
        if (dealerCode != "") {
            sql.append("   AND SM.DEALER_CODE LIKE '%" + dealerCode + "%'\n");
        }
        if (dealerName != "") {
            sql.append("   AND SM.DEALER_NAME LIKE '%" + dealerName + "%'\n");
        }
        if ((TstartDate != null && !"".equals(TstartDate)) && (TendDate != null && !"".equals(TendDate))) {
            sql.append("   AND SM.CREATE_DATE BETWEEN TO_DATE('" + TstartDate + "','YYYY-MM-DD') AND TO_DATE('" + TendDate + "','YYYY-MM-DD')\n");
        } else if ((TstartDate == null || "".equals(TstartDate)) && (TendDate != null && !"".equals(TendDate))) {
            sql.append("   AND SM.CREATE_DATE < TO_DATE('" + TendDate + "','YYYY-MM-DD')\n");
        } else if ((TstartDate != null && !"".equals(TstartDate)) && (TendDate == null || "".equals(TendDate))) {
            sql.append("   AND SM.CREATE_DATE > TO_DATE('" + TstartDate + "','YYYY-MM-DD') \n");
        }

        if (isSC != null && !"".equals(isSC)) {
            if (isSC.equals(Constant.IF_TYPE_YES + "")) {
                sql.append("AND BD.ASS_NO IS NOT NULL");
            } else {
                sql.append("AND BD.ASS_NO IS  NULL");
            }
        }

        sql.append(" GROUP BY SM.DEALER_CODE,\n");
        sql.append("         SM.DEALER_NAME,\n");
        sql.append("         SM.PICK_ORDER_ID,\n");
        sql.append("         BD.CREATE_DATE,\n");
        sql.append("         BD.PKG_NO,\n");
        sql.append("         BD.LENGTH,\n");
        sql.append("         BD.WIDTH,\n");
        sql.append("         BD.HEIGHT,\n");
        sql.append("         BD.WEIGHT,\n");
        sql.append("         BD.VOLUME,\n");
        sql.append("         BD.EQ_WEIGHT,\n");
        sql.append("         BD.CH_WEIGHT,\n");
        sql.append("         BD.ASS_NO");

        List<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, this.getFunName());
        return ps;
    }

}
