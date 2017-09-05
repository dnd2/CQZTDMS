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
import java.util.List;
import java.util.Map;

public class PartOrderDetailRepDao extends BaseDao {
    public Logger logger = Logger.getLogger(PartOrderDetailRepDao.class);
    private static final PartOrderDetailRepDao dao = new PartOrderDetailRepDao();

    private PartOrderDetailRepDao() {
    }

    public static final PartOrderDetailRepDao getInstance() {
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
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询
     */
    public PageResult<Map<String, Object>> queryRep(RequestWrapper request, int curPage, int pageSize, AclUserBean logonUser) {
        StringBuffer sql = new StringBuffer();
        String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
        String orgName = CommonUtils.checkNull(request.getParamValue("orgName"));
        String orderStartDate = CommonUtils.checkNull(request.getParamValue("orderStartDate"));
        String orderEndDate = CommonUtils.checkNull(request.getParamValue("orderEndDate"));
        String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldCode"));
        String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
        String partType = CommonUtils.checkNull(request.getParamValue("partType"));
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
        String soFlag = CommonUtils.checkNull(request.getParamValue("soFlag"));
        String province = CommonUtils.checkNull(request.getParamValue("province"));


        sql.append("SELECT *\n");
        sql.append("  FROM (SELECT PO.*,\n");
        sql.append("               SO.SO_CODE,\n");
        sql.append("               NVL(SO.SALES_QTY, 0) SALES_QTY, --已交付数量\n");
        sql.append("               CASE\n");
        sql.append("                 WHEN SO.SALES_QTY IS NULL THEN\n");
        sql.append("                  0\n");
        sql.append("                 WHEN SO.SALES_QTY IS NOT NULL THEN\n");
        sql.append("                  1\n");
        sql.append("               END XSXS, --销售总项数\n");
        sql.append("               NVL(BO.BO_QTY, 0) BO_QTY, --BO数量\n");
        sql.append("               CASE\n");
        sql.append("                 WHEN BO.BO_QTY IS NULL THEN\n");
        sql.append("                  0\n");
        sql.append("                 WHEN BO.BO_QTY IS NOT NULL THEN\n");
        sql.append("                  1\n");
        sql.append("               END BOXS, --BO总项数\n");
        sql.append("               NVL(BO.BO_AMOUNT, 0) BO_AMOUNT,\n");
        sql.append("               NVL(BO.TOSAL_QTY, 0) TOSAL_QTY --BO满足数量\n");
        sql.append("          FROM (SELECT (SELECT VD.VENDER_NAME\n");
        sql.append("                          FROM TT_PART_BUY_PRICE P, TT_PART_VENDER_DEFINE VD\n");
        sql.append("                         WHERE VD.VENDER_ID = P.VENDER_ID\n");
        sql.append("                           AND P.PART_ID = TPD.PART_ID\n");
        sql.append("                           AND P.IS_DEFAULT = 10041001\n");
        sql.append("                           AND P.STATUS = 1\n");
        sql.append("                           AND ROWNUM = 1) VENDER_NAME,\n");
        sql.append("                       (SELECT SUM(S.NORMAL_QTY)\n");
        sql.append("                          FROM VW_PART_STOCK S\n");
        sql.append("                         WHERE S.PART_ID = TPD.PART_ID\n");
        sql.append("                           AND S.ORG_ID = OM.SELLER_ID) STOCK,\n");
        sql.append("                       OM.ORDER_ID,\n");
        sql.append("                       OM.DEALER_CODE,\n");
        sql.append("                       OM.DEALER_NAME,\n");
        sql.append("                       TD.PROVINCE_ID,\n");
        sql.append("                       TR.REGION_NAME,\n");
        sql.append("                       OD.PART_ID,\n");
        sql.append("                       OD.BUY_PRICE,\n");
        sql.append("                       OM.SUBMIT_DATE,\n");
        sql.append("                       TPD.PART_TYPE,\n");
        sql.append("                       TPD.PART_OLDCODE,\n");
        sql.append("                       TPD.PART_CNAME,\n");
        sql.append("                       TPD.PART_CODE,\n");
        sql.append("                       TPD.UNIT,\n");
        sql.append("                       SUM(OD.BUY_QTY) BUY_QTY,\n");
        sql.append("                       SUM(OD.BUY_AMOUNT) BUY_AMOUNT\n");
        sql.append("                  FROM TT_PART_DLR_ORDER_DTL  OD,\n");
        sql.append("                       TT_PART_DLR_ORDER_MAIN OM,\n");
        sql.append("                       TM_DEALER              TD,\n");
        sql.append("                       TM_REGION              TR,\n");
        sql.append("                       TT_PART_DEFINE         TPD,\n");
        sql.append("                       TC_CODE                TC\n");
        sql.append("                 WHERE OD.ORDER_ID = OM.ORDER_ID\n");
        sql.append("                   AND OM.DEALER_ID = TD.DEALER_ID\n");
        sql.append("                   AND TD.PROVINCE_ID = TR.REGION_CODE\n");
        sql.append("                   AND OD.PART_ID = TPD.PART_ID(+)\n");
        sql.append("                   AND TPD.PART_TYPE = TC.CODE_ID(+)\n");
        sql.append("                   AND OM.SUBMIT_DATE is not null \n");
        sql.append("                   AND OM.STATE <> 92161004\n");
        sql.append("                   AND OM.SELLER_ID = 2010010100070674\n");
      /*  sql.append("                   AND TO_DATE(OM.CREATE_DATE) >=\n");
        sql.append("                       TO_DATE('2013-10-01', 'yyyy-MM-dd')\n");
        sql.append("                   AND TO_DATE(OM.CREATE_DATE) <=\n");
        sql.append("                       TO_DATE('2013-11-03', 'yyyy-MM-dd')\n");*/
        sql.append("                 GROUP BY OM.ORDER_ID,\n");
        sql.append("                          OM.DEALER_CODE,\n");
        sql.append("                          OM.DEALER_NAME,\n");
        sql.append("                          TD.PROVINCE_ID,\n");
        sql.append("                          TR.REGION_NAME,\n");
        sql.append("                          OD.PART_ID,\n");
        sql.append("                          OD.BUY_PRICE,\n");
        sql.append("                          OM.SUBMIT_DATE,\n");
        sql.append("                          TPD.PART_TYPE,\n");
        sql.append("                          TPD.PART_OLDCODE,\n");
        sql.append("                          TPD.PART_CNAME,\n");
        sql.append("                          TPD.PART_CODE,\n");
        sql.append("                          TPD.UNIT,\n");
        sql.append("                          TPD.PART_ID,\n");
        sql.append("                          OM.SELLER_ID) PO,\n");
        sql.append("               (SELECT SM.ORDER_ID,\n");
        sql.append("                       SM.SO_CODE,\n");
        sql.append("                       SD.PART_ID,\n");
        sql.append("                       SUM(SD.SALES_QTY) SALES_QTY\n");
        sql.append("                  FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM\n");
        sql.append("                 WHERE SD.SO_ID = SM.SO_ID\n");
        sql.append("                   AND SM.STATE <> 92401003\n");
        sql.append("                   AND SM.STATE <> 92401009\n");
        sql.append("                   AND SM.SELLER_ID = 2010010100070674\n");
        sql.append("                 GROUP BY SM.ORDER_ID, SM.SO_CODE, SD.PART_ID) SO,\n");
        sql.append("               (SELECT BM.ORDER_ID,\n");
        sql.append("                       BD.PART_ID,\n");
        sql.append("                       SUM(BD.BUY_QTY) BUY_QTY,\n");
        sql.append("                       SUM(BD.SALES_QTY) SALES_QTY,\n");
        sql.append("                       SUM(BD.BO_QTY) BO_QTY,\n");
        sql.append("                       SUM(BD.BO_QTY * BD.BUY_PRICE) BO_AMOUNT,\n");
        sql.append("                       SUM(BD.TOSAL_QTY) TOSAL_QTY\n");
        sql.append("                  FROM TT_PART_BO_DTL BD, TT_PART_BO_MAIN BM\n");
        sql.append("                 WHERE BD.BO_ID = BM.BO_ID\n");
        sql.append("                 GROUP BY BM.ORDER_ID, BD.PART_ID) BO\n");
        sql.append("         WHERE PO.ORDER_ID = SO.ORDER_ID(+)\n");
        sql.append("           AND PO.PART_ID = SO.PART_ID(+)\n");
        sql.append("           AND PO.ORDER_ID = BO.ORDER_ID(+)\n");
        sql.append("           AND PO.PART_ID = BO.PART_ID(+)) XX\n");

        sql.append(" WHERE 1 = 1\n");

        if (!orgCode.equals("")) {
            sql.append(" and  xx.dealer_code like '%").append(orgCode.toUpperCase()).append("%'\n");
        }
        if (!orgName.equals("")) {
            sql.append(" and  xx.dealer_name like '%").append(orgName).append("%'\n");
        }
        if (!orderStartDate.equals("")) {
            sql.append(" and xx.SUBMIT_DATE>= to_date('").append(orderStartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!orderEndDate.equals("")) {
            sql.append(" and xx.SUBMIT_DATE<= to_date('").append(orderEndDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!partOldCode.equals("")) {
            sql.append(" and  xx.part_oldcode like upper('%").append(partOldCode).append("%')\n");
        }
        if (!partCname.equals("")) {
            sql.append(" and  xx.part_cname like '%").append(partCname).append("%'\n");
        }
        if (!partCode.equals("")) {
            sql.append(" and  xx.part_code like upper('%").append(partCode).append("%')\n");
        }

        if (!soCode.equals("")) {
            sql.append(" and exists (select d.order_id from tt_part_so_main d where xx.order_id=d.order_id and d.so_code like '%").append(soCode).append("%')");
        }
        if (!soFlag.equals("")) {
            if (soFlag.equals("0")) {
                sql.append("  and  (xx.so_code is null or xx.so_code='')");
            } else {
                sql.append("  and  (xx.so_code is not null or xx.so_code<>'')");
            }
        }
        if (!partType.equals("")) {
            sql.append(" and xx.part_type =").append(partType);
        }
        if (!province.equals("")) {
            sql.append(" and  xx.region_name like '%").append(province).append("%'");
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询
     */
    public List<Map<String, Object>> queryExp(RequestWrapper request, AclUserBean logonUser) {
        StringBuffer sql = new StringBuffer();

        String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
        String orgName = CommonUtils.checkNull(request.getParamValue("orgName"));
        String orderStartDate = CommonUtils.checkNull(request.getParamValue("orderStartDate"));
        String orderEndDate = CommonUtils.checkNull(request.getParamValue("orderEndDate"));
        String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldCode"));
        String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
        String partType = CommonUtils.checkNull(request.getParamValue("partType"));
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
        String soFlag = CommonUtils.checkNull(request.getParamValue("soFlag"));
        String province = CommonUtils.checkNull(request.getParamValue("province"));

        sql.append("SELECT *\n");
        sql.append("  FROM (SELECT PO.*,\n");
        sql.append("               SO.SO_CODE,\n");
        sql.append("               NVL(SO.SALES_QTY, 0) SALES_QTY, --已交付数量\n");
        sql.append("               CASE\n");
        sql.append("                 WHEN SO.SALES_QTY IS NULL THEN\n");
        sql.append("                  0\n");
        sql.append("                 WHEN SO.SALES_QTY IS NOT NULL THEN\n");
        sql.append("                  1\n");
        sql.append("               END XSXS, --销售总项数\n");
        sql.append("               NVL(BO.BO_QTY, 0) BO_QTY, --BO数量\n");
        sql.append("               CASE\n");
        sql.append("                 WHEN BO.BO_QTY IS NULL THEN\n");
        sql.append("                  0\n");
        sql.append("                 WHEN BO.BO_QTY IS NOT NULL THEN\n");
        sql.append("                  1\n");
        sql.append("               END BOXS, --BO总项数\n");
        sql.append("               NVL(BO.BO_AMOUNT, 0) BO_AMOUNT,\n");
        sql.append("               NVL(BO.TOSAL_QTY, 0) TOSAL_QTY --BO满足数量\n");
        sql.append("          FROM (SELECT (SELECT VD.VENDER_NAME\n");
        sql.append("                          FROM TT_PART_BUY_PRICE P, TT_PART_VENDER_DEFINE VD\n");
        sql.append("                         WHERE VD.VENDER_ID = P.VENDER_ID\n");
        sql.append("                           AND P.PART_ID = TPD.PART_ID\n");
        sql.append("                           AND P.IS_DEFAULT = 10041001\n");
        sql.append("                           AND P.STATUS = 1\n");
        sql.append("                           AND ROWNUM = 1) VENDER_NAME,\n");
        sql.append("                       (SELECT SUM(S.NORMAL_QTY)\n");
        sql.append("                          FROM VW_PART_STOCK S\n");
        sql.append("                         WHERE S.PART_ID = TPD.PART_ID\n");
        sql.append("                           AND S.ORG_ID = OM.SELLER_ID) STOCK,\n");
        sql.append("                       OM.ORDER_ID,\n");
        sql.append("                       OM.DEALER_CODE,\n");
        sql.append("                       OM.DEALER_NAME,\n");
        sql.append("                       TD.PROVINCE_ID,\n");
        sql.append("                       TR.REGION_NAME,\n");
        sql.append("                       OD.PART_ID,\n");
        sql.append("                       OD.BUY_PRICE,\n");
        sql.append("                       OM.SUBMIT_DATE,\n");
        sql.append("                       TC.CODE_DESC PART_TYPE,\n");
        sql.append("                       TPD.PART_OLDCODE,\n");
        sql.append("                       TPD.PART_CNAME,\n");
        sql.append("                       TPD.PART_CODE,\n");
        sql.append("                       TPD.UNIT,\n");
        sql.append("                       SUM(OD.BUY_QTY) BUY_QTY,\n");
        sql.append("                       SUM(OD.BUY_AMOUNT) BUY_AMOUNT\n");
        sql.append("                  FROM TT_PART_DLR_ORDER_DTL  OD,\n");
        sql.append("                       TT_PART_DLR_ORDER_MAIN OM,\n");
        sql.append("                       TM_DEALER              TD,\n");
        sql.append("                       TM_REGION              TR,\n");
        sql.append("                       TT_PART_DEFINE         TPD,\n");
        sql.append("                       TC_CODE                TC\n");
        sql.append("                 WHERE OD.ORDER_ID = OM.ORDER_ID\n");
        sql.append("                   AND OM.DEALER_ID = TD.DEALER_ID\n");
        sql.append("                   AND TD.PROVINCE_ID = TR.REGION_CODE\n");
        sql.append("                   AND OD.PART_ID = TPD.PART_ID(+)\n");
        sql.append("                   AND TPD.PART_TYPE = TC.CODE_ID(+)\n");
        sql.append("                   AND OM.SUBMIT_DATE is not null \n");
        sql.append("                   AND OM.SELLER_ID = 2010010100070674\n");
      /*  sql.append("                   AND TO_DATE(OM.CREATE_DATE) >=\n");
        sql.append("                       TO_DATE('2013-10-01', 'yyyy-MM-dd')\n");
        sql.append("                   AND TO_DATE(OM.CREATE_DATE) <=\n");
        sql.append("                       TO_DATE('2013-11-03', 'yyyy-MM-dd')\n");*/
        sql.append("                 GROUP BY OM.ORDER_ID,\n");
        sql.append("                          OM.DEALER_CODE,\n");
        sql.append("                          OM.DEALER_NAME,\n");
        sql.append("                          TD.PROVINCE_ID,\n");
        sql.append("                          TR.REGION_NAME,\n");
        sql.append("                          OD.PART_ID,\n");
        sql.append("                          OD.BUY_PRICE,\n");
        sql.append("                          OM.SUBMIT_DATE,\n");
        sql.append("                          TC.CODE_DESC,\n");
        sql.append("                          TPD.PART_OLDCODE,\n");
        sql.append("                          TPD.PART_CNAME,\n");
        sql.append("                          TPD.PART_CODE,\n");
        sql.append("                          TPD.UNIT,\n");
        sql.append("                          TPD.PART_ID,\n");
        sql.append("                          OM.SELLER_ID) PO,\n");
        sql.append("               (SELECT SM.ORDER_ID,\n");
        sql.append("                       SM.SO_CODE,\n");
        sql.append("                       SD.PART_ID,\n");
        sql.append("                       SUM(SD.SALES_QTY) SALES_QTY\n");
        sql.append("                  FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM\n");
        sql.append("                 WHERE SD.SO_ID = SM.SO_ID\n");
        sql.append("                   AND SM.STATE <> 92401003\n");
        sql.append("                   AND SM.STATE <> 92401009\n");
        sql.append("                   AND SM.SELLER_ID = 2010010100070674\n");
        sql.append("                 GROUP BY SM.ORDER_ID, SM.SO_CODE, SD.PART_ID) SO,\n");
        sql.append("               (SELECT BM.ORDER_ID,\n");
        sql.append("                       BD.PART_ID,\n");
        sql.append("                       SUM(BD.BUY_QTY) BUY_QTY,\n");
        sql.append("                       SUM(BD.SALES_QTY) SALES_QTY,\n");
        sql.append("                       SUM(BD.BO_QTY) BO_QTY,\n");
        sql.append("                       SUM(BD.BO_QTY * BD.BUY_PRICE) BO_AMOUNT,\n");
        sql.append("                       SUM(BD.TOSAL_QTY) TOSAL_QTY\n");
        sql.append("                  FROM TT_PART_BO_DTL BD, TT_PART_BO_MAIN BM\n");
        sql.append("                 WHERE BD.BO_ID = BM.BO_ID\n");
        sql.append("                 GROUP BY BM.ORDER_ID, BD.PART_ID) BO\n");
        sql.append("         WHERE PO.ORDER_ID = SO.ORDER_ID(+)\n");
        sql.append("           AND PO.PART_ID = SO.PART_ID(+)\n");
        sql.append("           AND PO.ORDER_ID = BO.ORDER_ID(+)\n");
        sql.append("           AND PO.PART_ID = BO.PART_ID(+)) XX\n");

        sql.append(" WHERE 1 = 1\n");

        if (!orgCode.equals("")) {
            sql.append(" and  xx.dealer_code like '%").append(orgCode.toUpperCase()).append("%'\n");
        }
        if (!orgName.equals("")) {
            sql.append(" and  xx.dealer_name like '%").append(orgName).append("%'\n");
        }
        if (!orderStartDate.equals("")) {
            sql.append(" and xx.SUBMIT_DATE>= to_date('").append(orderStartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!orderEndDate.equals("")) {
            sql.append(" and xx.SUBMIT_DATE<= to_date('").append(orderEndDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!partOldCode.equals("")) {
            sql.append(" and  xx.part_oldcode like upper('%").append(partOldCode).append("%')\n");
        }
        if (!partCname.equals("")) {
            sql.append(" and  xx.part_cname like '%").append(partCname).append("%'\n");
        }
        if (!partCode.equals("")) {
            sql.append(" and  xx.part_code like upper('%").append(partCode).append("%')\n");
        }

        if (!soCode.equals("")) {
            sql.append(" and exists (select d.order_id from tt_part_so_main d where xx.order_id=d.order_id and d.so_code like '%").append(soCode).append("%')");
        }
        if (!soFlag.equals("")) {
            if (soFlag.equals("0")) {
                sql.append("  and  (xx.so_code is null or xx.so_code='')");
            } else {
                sql.append("  and  (xx.so_code is not null or xx.so_code<>'')");
            }
        }
        if (!partType.equals("")) {
            sql.append(" and xx.part_type =").append(partType);
        }
        if (!province.equals("")) {
            sql.append(" and  xx.region_name like '%").append(province).append("%'");
        }
        List<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, this.getFunName());
        return ps;
    }

    public PageResult<Map<String, Object>> queryPartOrderDtl4GyzxList(
            RequestWrapper request, Integer curPage, Integer pageSize,
            AclUserBean logonUser) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订货单号
            String soCode = CommonUtils.checkNull(request.getParamValue("SO_CODE"));//销售单号
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//服务商编码
            String childorgName = CommonUtils.checkNull(request.getParamValue("childorgName"));//服务商
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//订货日期
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//订货日期
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件类型
            String regionName = CommonUtils.checkNull(request.getParamValue("REGION_NAME"));//省份
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));//配件编码
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String venderName = CommonUtils.checkNull(request.getParamValue("venderName"));//供应中心
            String venderCode = CommonUtils.checkNull(request.getParamValue("venderCode"));//供应中心
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));//供应中心

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT * \n");
            sql.append("   FROM (SELECT PO.*, \n");
            sql.append("                SO.SO_CODE, \n");
            sql.append("                NVL(SO.SALES_QTY, 0) SALES_QTY, --已交付数量 \n");
            sql.append("                CASE \n");
            sql.append("                  WHEN SO.SALES_QTY IS NULL THEN \n");
            sql.append("                   0 \n");
            sql.append("                  WHEN SO.SALES_QTY IS NOT NULL THEN \n");
            sql.append("                   1 \n");
            sql.append("                END XSXS, --销售总项数 \n");
            sql.append("                NVL(BO.BO_QTY, 0) BO_QTY, --BO数量 \n");
            sql.append("                CASE \n");
            sql.append("                  WHEN BO.BO_QTY IS NULL THEN \n");
            sql.append("                   0 \n");
            sql.append("                  WHEN BO.BO_QTY IS NOT NULL THEN \n");
            sql.append("                   1 \n");
            sql.append("                END BOXS, --BO总项数 \n");
            sql.append("                NVL(BO.BO_AMOUNT, 0) BO_AMOUNT, \n");
            sql.append("                NVL(BO.TOSAL_QTY, 0) TOSAL_QTY --BO满足数量 \n");
            sql.append("           FROM (SELECT (SELECT SUM(S.NORMAL_QTY) \n");
            sql.append("                           FROM VW_PART_STOCK S \n");
            sql.append("                          WHERE S.PART_ID = TPD.PART_ID \n");
            sql.append("                            AND S.ORG_ID = OM.SELLER_ID) STOCK, \n");
            sql.append("                        OM.ORDER_ID, \n");
            sql.append("                        OM.ORDER_CODE, \n");
            sql.append("                        OM.DEALER_CODE, \n");
            sql.append("                        OM.DEALER_NAME, \n");
            sql.append("                        OM.SELLER_CODE, \n");
            sql.append("                        OM.SELLER_NAME, \n");
            sql.append("                        TD.PROVINCE_ID, \n");
            sql.append("                        TR.REGION_NAME, \n");
            sql.append("                        OD.PART_ID, \n");
            sql.append("                        OD.BUY_PRICE, \n");
            sql.append("                        OM.SUBMIT_DATE, \n");
            sql.append("                        TPD.PART_TYPE, \n");
            sql.append("                        TPD.PART_OLDCODE, \n");
            sql.append("                        TPD.PART_CNAME, \n");
            sql.append("                        TPD.PART_CODE, \n");
            sql.append("                        TPD.UNIT, \n");
            sql.append("                        SUM(OD.BUY_QTY) BUY_QTY, \n");
            sql.append("                        SUM(OD.BUY_AMOUNT) BUY_AMOUNT \n");
            sql.append("                   FROM TT_PART_DLR_ORDER_DTL  OD, \n");
            sql.append("                        TT_PART_DLR_ORDER_MAIN OM, \n");
            sql.append("                        TM_DEALER              TD, \n");
            sql.append("                        TM_REGION              TR, \n");
            sql.append("                        TT_PART_DEFINE         TPD, \n");
            sql.append("                        TC_CODE                TC \n");
            sql.append("                  WHERE OD.ORDER_ID = OM.ORDER_ID \n");
            sql.append("                    AND OM.DEALER_ID = TD.DEALER_ID \n");
            sql.append("                    AND TD.PROVINCE_ID = TR.REGION_CODE \n");
            sql.append("                    AND OD.PART_ID = TPD.PART_ID(+) \n");
            sql.append("                    AND TPD.PART_TYPE = TC.CODE_ID(+) \n");
            sql.append("                    AND OM.SUBMIT_DATE IS NOT NULL \n");
            if (!"".equals(venderId)) {
                sql.append(" AND OM.SELLER_ID=").append(venderId);
            } else {
                sql.append(" AND OM.SELLER_ID <>").append(logonUser.getOrgId());
            }
            sql.append("                  GROUP BY OM.ORDER_ID, \n");
            sql.append("                           OM.ORDER_CODE, \n");
            sql.append("                           OM.DEALER_CODE, \n");
            sql.append("                           OM.DEALER_NAME, \n");
            sql.append("                           TD.PROVINCE_ID, \n");
            sql.append("                           TR.REGION_NAME, \n");
            sql.append("                           OD.PART_ID, \n");
            sql.append("                           OD.BUY_PRICE, \n");
            sql.append("                           OM.SUBMIT_DATE, \n");
            sql.append("                           TPD.PART_TYPE, \n");
            sql.append("                           TPD.PART_OLDCODE, \n");
            sql.append("                           TPD.PART_CNAME, \n");
            sql.append("                           TPD.PART_CODE, \n");
            sql.append("                           TPD.UNIT, \n");
            sql.append("                           TPD.PART_ID, \n");
            sql.append("                           OM.SELLER_ID, \n");
            sql.append("                           OM.SELLER_CODE, \n");
            sql.append("                           OM.SELLER_NAME) PO, \n");
            sql.append("                (SELECT SM.ORDER_ID, \n");
            sql.append("                        SM.SO_CODE, \n");
            sql.append("                        SD.PART_ID, \n");
            sql.append("                        SUM(SD.SALES_QTY) SALES_QTY \n");
            sql.append("                   FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM \n");
            sql.append("                  WHERE SD.SO_ID = SM.SO_ID \n");
            sql.append("                    AND SM.STATE <> ").append(Constant.CAR_FACTORY_SALE_ORDER_STATE_03);
            sql.append("                    AND SM.STATE <> ").append(Constant.CAR_FACTORY_SALE_ORDER_STATE_09);
            if (!"".equals(venderId)) {
                sql.append(" AND SM.SELLER_ID=").append(venderId);
            } else {
                sql.append(" AND SM.SELLER_ID <>").append(logonUser.getOrgId());
            }
            sql.append("                  GROUP BY SM.ORDER_ID, SM.SO_CODE, SD.PART_ID) SO, \n");
            sql.append("                (SELECT BM.ORDER_ID, \n");
            sql.append("                        BD.PART_ID, \n");
            sql.append("                        SUM(BD.BUY_QTY) BUY_QTY, \n");
            sql.append("                        SUM(BD.SALES_QTY) SALES_QTY, \n");
            sql.append("                        SUM(BD.BO_QTY) BO_QTY, \n");
            sql.append("                        SUM(BD.BO_QTY * BD.BUY_PRICE) BO_AMOUNT, \n");
            sql.append("                        SUM(BD.TOSAL_QTY) TOSAL_QTY \n");
            sql.append("                   FROM TT_PART_BO_DTL BD, TT_PART_BO_MAIN BM \n");
            sql.append("                  WHERE BD.BO_ID = BM.BO_ID \n");
            sql.append("                  GROUP BY BM.ORDER_ID, BD.PART_ID) BO \n");
            sql.append("          WHERE PO.ORDER_ID = SO.ORDER_ID(+) \n");
            sql.append("            AND PO.PART_ID = SO.PART_ID(+) \n");
            sql.append("            AND PO.ORDER_ID = BO.ORDER_ID(+) \n");
            sql.append("            AND PO.PART_ID = BO.PART_ID(+)) XX \n");
            sql.append("  WHERE 1 = 1 \n");

            if (!"".equals(dealerCode)) {
                sql.append(" AND XX.DEALER_CODE LIKE '%").append(dealerCode).append("%'");
            }

            if (!"".equals(orderCode)) {
                sql.append(" AND XX.ORDER_CODE LIKE '%").append(orderCode).append("%'");
            }

            if (!"".equals(soCode)) {
                sql.append(" AND XX.SO_CODE LIKE '%").append(soCode).append("%'");
            }

            if (!"".equals(childorgName)) {
                sql.append(" AND XX.DEALER_NAME LIKE '%").append(childorgName).append("%'");
            }

            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(XX.SUBMIT_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(XX.SUBMIT_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(partType)) {
                sql.append(" AND XX.PART_TYPE = ").append(partType);
            }

            if (!"".equals(regionName)) {
                sql.append(" AND XX.REGION_NAME LIKE '%").append(regionName).append("%'");
            }

            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(XX.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(XX.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCname)) {
                sql.append(" AND XX.PART_CNAME LIKE '%").append(partCname).append("%'");
            }

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartOrderDtl4Gyzx(
            RequestWrapper request, AclUserBean logonUser) throws Exception {
        List<Map<String, Object>> list;
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订货单号
            String soCode = CommonUtils.checkNull(request.getParamValue("SO_CODE"));//销售单号
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//服务商编码
            String childorgName = CommonUtils.checkNull(request.getParamValue("childorgName"));//订货单位
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//订货日期
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//订货日期
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件类型
            String regionName = CommonUtils.checkNull(request.getParamValue("REGION_NAME"));//省份
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));//配件编码
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String venderName = CommonUtils.checkNull(request.getParamValue("venderName"));//供应中心
            String venderCode = CommonUtils.checkNull(request.getParamValue("venderCode"));//供应中心
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));//供应中心

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT * \n");
            sql.append("   FROM (SELECT PO.*, \n");
            sql.append("                SO.SO_CODE, \n");
            sql.append("                NVL(SO.SALES_QTY, 0) SALES_QTY, --已交付数量 \n");
            sql.append("                CASE \n");
            sql.append("                  WHEN SO.SALES_QTY IS NULL THEN \n");
            sql.append("                   0 \n");
            sql.append("                  WHEN SO.SALES_QTY IS NOT NULL THEN \n");
            sql.append("                   1 \n");
            sql.append("                END XSXS, --销售总项数 \n");
            sql.append("                NVL(BO.BO_QTY, 0) BO_QTY, --BO数量 \n");
            sql.append("                CASE \n");
            sql.append("                  WHEN BO.BO_QTY IS NULL THEN \n");
            sql.append("                   0 \n");
            sql.append("                  WHEN BO.BO_QTY IS NOT NULL THEN \n");
            sql.append("                   1 \n");
            sql.append("                END BOXS, --BO总项数 \n");
            sql.append("                NVL(BO.BO_AMOUNT, 0) BO_AMOUNT, \n");
            sql.append("                NVL(BO.TOSAL_QTY, 0) TOSAL_QTY --BO满足数量 \n");
            sql.append("           FROM (SELECT (SELECT SUM(S.NORMAL_QTY) \n");
            sql.append("                           FROM VW_PART_STOCK S \n");
            sql.append("                          WHERE S.PART_ID = TPD.PART_ID \n");
            sql.append("                            AND S.ORG_ID = OM.SELLER_ID) STOCK, \n");
            sql.append("                        OM.ORDER_ID, \n");
            sql.append("                        OM.ORDER_CODE, \n");
            sql.append("                        OM.DEALER_CODE, \n");
            sql.append("                        OM.DEALER_NAME, \n");
            sql.append("                        OM.SELLER_CODE, \n");
            sql.append("                        OM.SELLER_NAME, \n");
            sql.append("                        TD.PROVINCE_ID, \n");
            sql.append("                        TR.REGION_NAME, \n");
            sql.append("                        OD.PART_ID, \n");
            sql.append("                        OD.BUY_PRICE, \n");
            sql.append("                        OM.SUBMIT_DATE, \n");
            sql.append("                        TPD.PART_TYPE, \n");
            sql.append("                        TPD.PART_OLDCODE, \n");
            sql.append("                        TPD.PART_CNAME, \n");
            sql.append("                        TPD.PART_CODE, \n");
            sql.append("                        TPD.UNIT, \n");
            sql.append("                        SUM(OD.BUY_QTY) BUY_QTY, \n");
            sql.append("                        SUM(OD.BUY_AMOUNT) BUY_AMOUNT \n");
            sql.append("                   FROM TT_PART_DLR_ORDER_DTL  OD, \n");
            sql.append("                        TT_PART_DLR_ORDER_MAIN OM, \n");
            sql.append("                        TM_DEALER              TD, \n");
            sql.append("                        TM_REGION              TR, \n");
            sql.append("                        TT_PART_DEFINE         TPD, \n");
            sql.append("                        TC_CODE                TC \n");
            sql.append("                  WHERE OD.ORDER_ID = OM.ORDER_ID \n");
            sql.append("                    AND OM.DEALER_ID = TD.DEALER_ID \n");
            sql.append("                    AND TD.PROVINCE_ID = TR.REGION_CODE \n");
            sql.append("                    AND OD.PART_ID = TPD.PART_ID(+) \n");
            sql.append("                    AND TPD.PART_TYPE = TC.CODE_ID(+) \n");
            sql.append("                    AND OM.SUBMIT_DATE IS NOT NULL \n");
            if (!"".equals(venderId)) {
                sql.append(" AND OM.SELLER_ID=").append(venderId);
            } else {
                sql.append(" AND OM.SELLER_ID <>").append(logonUser.getOrgId());
            }
            sql.append("                  GROUP BY OM.ORDER_ID, \n");
            sql.append("                           OM.ORDER_CODE, \n");
            sql.append("                           OM.DEALER_CODE, \n");
            sql.append("                           OM.DEALER_NAME, \n");
            sql.append("                           TD.PROVINCE_ID, \n");
            sql.append("                           TR.REGION_NAME, \n");
            sql.append("                           OD.PART_ID, \n");
            sql.append("                           OD.BUY_PRICE, \n");
            sql.append("                           OM.SUBMIT_DATE, \n");
            sql.append("                           TPD.PART_TYPE, \n");
            sql.append("                           TPD.PART_OLDCODE, \n");
            sql.append("                           TPD.PART_CNAME, \n");
            sql.append("                           TPD.PART_CODE, \n");
            sql.append("                           TPD.UNIT, \n");
            sql.append("                           TPD.PART_ID, \n");
            sql.append("                           OM.SELLER_ID, \n");
            sql.append("                           OM.SELLER_CODE, \n");
            sql.append("                           OM.SELLER_NAME) PO, \n");
            sql.append("                (SELECT SM.ORDER_ID, \n");
            sql.append("                        SM.SO_CODE, \n");
            sql.append("                        SD.PART_ID, \n");
            sql.append("                        SUM(SD.SALES_QTY) SALES_QTY \n");
            sql.append("                   FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM \n");
            sql.append("                  WHERE SD.SO_ID = SM.SO_ID \n");
            sql.append("                    AND SM.STATE <> ").append(Constant.CAR_FACTORY_SALE_ORDER_STATE_03);
            sql.append("                    AND SM.STATE <> ").append(Constant.CAR_FACTORY_SALE_ORDER_STATE_09);
            if (!"".equals(venderId)) {
                sql.append(" AND SM.SELLER_ID=").append(venderId);
            } else {
                sql.append(" AND SM.SELLER_ID <>").append(logonUser.getOrgId());
            }
            sql.append("                  GROUP BY SM.ORDER_ID, SM.SO_CODE, SD.PART_ID) SO, \n");
            sql.append("                (SELECT BM.ORDER_ID, \n");
            sql.append("                        BD.PART_ID, \n");
            sql.append("                        SUM(BD.BUY_QTY) BUY_QTY, \n");
            sql.append("                        SUM(BD.SALES_QTY) SALES_QTY, \n");
            sql.append("                        SUM(BD.BO_QTY) BO_QTY, \n");
            sql.append("                        SUM(BD.BO_QTY * BD.BUY_PRICE) BO_AMOUNT, \n");
            sql.append("                        SUM(BD.TOSAL_QTY) TOSAL_QTY \n");
            sql.append("                   FROM TT_PART_BO_DTL BD, TT_PART_BO_MAIN BM \n");
            sql.append("                  WHERE BD.BO_ID = BM.BO_ID \n");
            sql.append("                  GROUP BY BM.ORDER_ID, BD.PART_ID) BO \n");
            sql.append("          WHERE PO.ORDER_ID = SO.ORDER_ID(+) \n");
            sql.append("            AND PO.PART_ID = SO.PART_ID(+) \n");
            sql.append("            AND PO.ORDER_ID = BO.ORDER_ID(+) \n");
            sql.append("            AND PO.PART_ID = BO.PART_ID(+)) XX \n");
            sql.append("  WHERE 1 = 1 \n");

            if (!"".equals(dealerCode)) {
                sql.append(" AND XX.DEALER_CODE LIKE '%").append(dealerCode).append("%'");
            }

            if (!"".equals(orderCode)) {
                sql.append(" AND XX.ORDER_CODE LIKE '%").append(orderCode).append("%'");
            }

            if (!"".equals(soCode)) {
                sql.append(" AND XX.SO_CODE LIKE '%").append(soCode).append("%'");
            }

            if (!"".equals(childorgName)) {
                sql.append(" AND XX.DEALER_NAME LIKE '%").append(childorgName).append("%'");
            }

            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(XX.SUBMIT_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(XX.SUBMIT_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(partType)) {
                sql.append(" AND XX.PART_TYPE = ").append(partType);
            }

            if (!"".equals(regionName)) {
                sql.append(" AND XX.REGION_NAME LIKE '%").append(regionName).append("%'");
            }

            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(XX.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(XX.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }

            if (!"".equals(partCname)) {
                sql.append(" AND XX.PART_CNAME LIKE '%").append(partCname).append("%'");
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }
}
