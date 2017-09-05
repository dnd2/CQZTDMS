package com.infodms.dms.dao.parts.baseManager.partBaseQuery.partSaleManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-4-18
 * @ClassName : financePartSalePriceDao
 * @Description : 配件销售价格查询 DAO
 */
public class financePartSalePriceDao extends BaseDao<PO> {
    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    private static final financePartSalePriceDao dao = new financePartSalePriceDao();

    private financePartSalePriceDao() {
    }

    public static final financePartSalePriceDao getInstance() {
        return dao;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-18
     * @Title :
     * @Description: 查询配件销售信息 OEM&DLR
     */
    public PageResult<Map<String, Object>> queryPartSalePrice(
            RequestWrapper request, int curPage, int pageSize, AclUserBean logonUser) {
        String partCode = CommonUtils.checkNull(request
                .getParamValue("PART_CODE"));// 件号
        String partOldcode = CommonUtils.checkNull(request
                .getParamValue("PART_OLDCODE"));// 配件编码
        String partCname = CommonUtils.checkNull(request
                .getParamValue("PART_CNAME"));// 配件名称
        String orgType = CommonUtils.checkNull(request
                .getParamValue("orgType"));// 用户类型
        String str_isChange = CommonUtils.checkNull(request.getParamValue("PART_IS_CHANGHE"));// 结算基地
        int isChanghe = 0;

        if (!"".equals(str_isChange)) {
            isChanghe = CommonUtils.parseInteger(str_isChange);
        }

        StringBuffer sql = new StringBuffer();

        if ("OEM".equals(orgType)) {
            sql.append(" select ");
            sql.append(" a.PRICE_ID ,");// 主键标识
            sql.append(" b.PART_CODE ,"); // 件号
            sql.append(" b.PART_OLDCODE ,");// 配件编码
            sql.append(" b.PART_CNAME ,");// 配件名称
            sql.append(" b.PART_IS_CHANGHE ,");//结算基地
            sql.append(" TO_CHAR(a.SALE_PRICE1,'fm999,999,999,990.00') AS  SALE_PRICE1,");// 调拨价
            sql.append(" TO_CHAR(a.SALE_PRICE2,'fm999,999,999,990.00') AS  SALE_PRICE2,");// 建议零售价
            sql.append(" TO_CHAR(a.SALE_PRICE3,'fm999,999,999,990.00') AS  SALE_PRICE3,");// 计划价
            sql.append(" TO_CHAR(a.SALE_PRICE4,'fm999,999,999,990.00') AS  SALE_PRICE4,");// 团购价
            sql.append(" TO_CHAR(a.SALE_PRICE5,'fm999,999,999,990.00') AS  SALE_PRICE5,");// 价格5
            sql.append(" TO_CHAR(a.SALE_PRICE6,'fm999,999,999,990.00') AS  SALE_PRICE6,");// 价格6
            sql.append(" TO_CHAR(a.SALE_PRICE7,'fm999,999,999,990.00') AS  SALE_PRICE7,");// 价格7
            sql.append(" TO_CHAR(a.SALE_PRICE8,'fm999,999,999,990.00') AS  SALE_PRICE8,");// 价格8
            sql.append(" TO_CHAR(a.SALE_PRICE9,'fm999,999,999,990.00') AS  SALE_PRICE9,");// 价格9
            sql.append(" TO_CHAR(a.SALE_PRICE10,'fm999,999,999,990.00') AS  SALE_PRICE10,");// 价格10
            sql.append(" TO_CHAR(a.SALE_PRICE11,'fm999,999,999,990.00') AS  SALE_PRICE11,");// 价格11
            sql.append(" TO_CHAR(a.SALE_PRICE12,'fm999,999,999,990.00') AS  SALE_PRICE12,");// 价格12
            sql.append(" TO_CHAR(a.SALE_PRICE13,'fm999,999,999,990.00') AS  SALE_PRICE13,");// 价格13
            sql.append(" TO_CHAR(a.SALE_PRICE14,'fm999,999,999,990.00') AS  SALE_PRICE14,");// 价格14
            sql.append(" TO_CHAR(a.SALE_PRICE15,'fm999,999,999,990.00') AS  SALE_PRICE15,");// 价格15
            sql.append("  A.UPDATE_DATE," + "      U.NAME"
                    + " FROM TT_PART_SALES_PRICE A, TC_USER U, TT_PART_DEFINE B"
                    + "  WHERE A.PART_ID = B.PART_ID "
                    + "  AND A.UPDATE_BY = U.USER_ID(+) AND A.STATUS = 1 ");

            if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WRD) {
                sql.append(" and B.part_is_changhe =" + Constant.PART_IS_CHANGHE_02 + "\n");
            }
            if (!"".equals(partCode) && partCode != null)
                sql.append(" AND UPPER(B.PART_CODE) LIKE '%").append(partCode.trim().toUpperCase())
                        .append("%'");
            if (!"".equals(partOldcode) && partOldcode != null)
                sql.append(" AND UPPER(B.PART_OLDCODE) LIKE '%").append(partOldcode.trim().toUpperCase())
                        .append("%'");
            if (!"".equals(partCname) && partCname != null)
                sql.append(" AND B.PART_CNAME LIKE '%").append(partCname)
                        .append("%'");

            if (isChanghe != 0) {
                sql.append(" and B.PART_IS_CHANGHE=").append(isChanghe);
            }
            sql.append(" ORDER BY B.PART_OLDCODE");
        } else if ("DLR".equals(orgType)) {
            sql.append("SELECT TO_CHAR(NVL(PKG_PART.F_GETPRICE(2014041994277963, D.PART_ID), '0'),\n");
            sql.append("               'fm999,999,999,990.00') AS PART_PRICE,\n");
            sql.append("       TO_CHAR(P.SALE_PRICE1,'fm999,999,999,990.00') AS  SALE_PRICE1,");// 价格1
            sql.append("       TO_CHAR(P.SALE_PRICE2,'fm999,999,999,990.00') AS  SALE_PRICE2,");// 价格2
            sql.append("       TO_CHAR(P.SALE_PRICE3,'fm999,999,999,990.00') AS  SALE_PRICE3,");// 价格3
            sql.append("       TO_CHAR(P.SALE_PRICE4,'fm999,999,999,990.00') AS  SALE_PRICE4,");// 价格4
            sql.append("       TO_CHAR(P.SALE_PRICE5,'fm999,999,999,990.00') AS  SALE_PRICE5,");// 价格5
            sql.append("       TO_CHAR(P.SALE_PRICE6,'fm999,999,999,990.00') AS  SALE_PRICE6,");// 价格6
            sql.append("       TO_CHAR(P.SALE_PRICE7,'fm999,999,999,990.00') AS  SALE_PRICE7,");// 价格7
            sql.append("       TO_CHAR(P.SALE_PRICE8,'fm999,999,999,990.00') AS  SALE_PRICE8,");// 价格8
            sql.append("       TO_CHAR(P.SALE_PRICE9,'fm999,999,999,990.00') AS  SALE_PRICE9,");// 价格9
            sql.append("       D.PART_IS_CHANGHE,\n");
            sql.append("       D.PART_ID,\n");
            sql.append("       D.PART_CODE,\n");
            sql.append("       D.PART_OLDCODE,\n");
            sql.append("       D.PART_CNAME,\n");
            sql.append("       D.MODEL_NAME\n");
            sql.append("  FROM TT_PART_DEFINE D, TT_PART_SALES_PRICE P\n");
            sql.append(" WHERE 1 = 1\n");
            sql.append("   AND D.PART_ID = P.PART_ID\n");
            sql.append("   AND P.STATUS = 1");
            //sql.append(" AND P.UPDATE_BY = U.USER_ID(+) ");

            if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WRD) {
                sql.append(" and D.part_is_changhe =" + Constant.PART_IS_CHANGHE_02 + "\n");
            }

            if (!"".equals(partCode) && partCode != null)
                sql.append(" AND UPPER(D.PART_CODE) LIKE '%").append(partCode.trim().toUpperCase())
                        .append("%'");
            if (!"".equals(partOldcode) && partOldcode != null)
                sql.append(" AND UPPER(D.PART_OLDCODE) LIKE '%").append(partOldcode.trim().toUpperCase())
                        .append("%'");
            if (!"".equals(partCname) && partCname != null)
                sql.append(" AND D.PART_CNAME LIKE '%").append(partCname)
                        .append("%'");

            if (isChanghe != 0) {
                sql.append(" and D.PART_IS_CHANGHE=").append(isChanghe);
            }
            sql.append(CommonUtils.bindPartSQL(logonUser.getDealerId(), "D"));
            sql.append(" ORDER BY D.PART_OLDCODE");
        }

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-18
     * @Title :
     * @Description: 返回配件销售价格List
     */
    public List<Map<String, Object>> queryPartSalePriceForExport(
            RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String partCode = CommonUtils.checkNull(request
                .getParamValue("PART_CODE"));// 件号
        String partOldcode = CommonUtils.checkNull(request
                .getParamValue("PART_OLDCODE"));// 配件编码
        String partCname = CommonUtils.checkNull(request
                .getParamValue("PART_CNAME"));// 配件名称
        String orgType = CommonUtils.checkNull(request
                .getParamValue("orgType"));// 用户类型
        String str_isChange = CommonUtils.checkNull(request.getParamValue("PART_IS_CHANGHE"));// 结算基地
        int isChanghe = 0;

        if (!"".equals(str_isChange)) {
            isChanghe = CommonUtils.parseInteger(str_isChange);
        }

        StringBuffer sql = new StringBuffer();

        if ("OEM".equals(orgType)) {
            sql.append(" select ");
            sql.append(" a.PRICE_ID ,");// 主键标识
            sql.append(" b.PART_CODE ,"); // 件号
            sql.append(" b.PART_OLDCODE ,");// 配件编码
            sql.append(" b.PART_CNAME ,");// 配件名称
            sql.append(" decode(B.PART_IS_CHANGHE, " + Constant.PART_IS_CHANGHE_01 + ",'昌河'," + Constant.PART_IS_CHANGHE_02 + ",'东安') PART_IS_CHANGHE, ");
            sql.append(" TO_CHAR(a.SALE_PRICE1,'fm999,999,999,990.00') AS  SALE_PRICE1,");// 调拨价
            sql.append(" TO_CHAR(a.SALE_PRICE2,'fm999,999,999,990.00') AS  SALE_PRICE2,");// 建议零售价
            sql.append(" TO_CHAR(a.SALE_PRICE3,'fm999,999,999,990.00') AS  SALE_PRICE3,");// 计划价
            sql.append(" TO_CHAR(a.SALE_PRICE4,'fm999,999,999,990.00') AS  SALE_PRICE4,");// 团购价
            sql.append(" TO_CHAR(a.SALE_PRICE5,'fm999,999,999,990.00') AS  SALE_PRICE5,");// 价格5
            sql.append(" TO_CHAR(a.SALE_PRICE6,'fm999,999,999,990.00') AS  SALE_PRICE6,");// 价格6
            sql.append(" TO_CHAR(a.SALE_PRICE7,'fm999,999,999,990.00') AS  SALE_PRICE7,");// 价格7
            sql.append(" TO_CHAR(a.SALE_PRICE8,'fm999,999,999,990.00') AS  SALE_PRICE8,");// 价格8
            sql.append(" TO_CHAR(a.SALE_PRICE9,'fm999,999,999,990.00') AS  SALE_PRICE9,");// 价格9
            sql.append(" TO_CHAR(a.SALE_PRICE10,'fm999,999,999,990.00') AS  SALE_PRICE10,");// 价格10
            sql.append(" TO_CHAR(a.SALE_PRICE11,'fm999,999,999,990.00') AS  SALE_PRICE11,");// 价格11
            sql.append(" TO_CHAR(a.SALE_PRICE12,'fm999,999,999,990.00') AS  SALE_PRICE12,");// 价格12
            sql.append(" TO_CHAR(a.SALE_PRICE13,'fm999,999,999,990.00') AS  SALE_PRICE13,");// 价格13
            sql.append(" TO_CHAR(a.SALE_PRICE14,'fm999,999,999,990.00') AS  SALE_PRICE14,");// 价格14
            sql.append(" TO_CHAR(a.SALE_PRICE15,'fm999,999,999,990.00') AS  SALE_PRICE15,");// 价格15
            sql.append(" TO_CHAR(A.UPDATE_DATE,'yyyy-MM-dd hh24:mm:ss') AS UPDATE_DATE," + " U.NAME "
                    + " FROM TT_PART_SALES_PRICE A, TC_USER U, TT_PART_DEFINE B"
                    + "  WHERE A.PART_ID = B.PART_ID "
                    + "  AND A.UPDATE_BY = U.USER_ID(+) AND A.STATUS = 1 ");

            if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WRD) {
                sql.append(" and B.part_is_changhe =" + Constant.PART_IS_CHANGHE_02 + "\n");
            }

            if (!"".equals(partCode) && partCode != null)
                sql.append(" AND UPPER(B.PART_CODE) LIKE '%").append(partCode.trim().toUpperCase())
                        .append("%'");
            if (!"".equals(partOldcode) && partOldcode != null)
                sql.append(" AND UPPER(B.PART_OLDCODE) LIKE '%").append(partOldcode.trim().toUpperCase())
                        .append("%'");
            if (!"".equals(partCname) && partCname != null)
                sql.append(" AND B.PART_CNAME LIKE '%").append(partCname)
                        .append("%'");

            if (isChanghe != 0) {
                sql.append(" and B.PART_IS_CHANGHE=").append(isChanghe);
            }
            sql.append(" ORDER BY B.PART_OLDCODE ");
        } else if ("DLR".equals(orgType)) {
            sql.append("SELECT TO_CHAR(NVL(PKG_PART.F_GETPRICE(2014041994277963, D.PART_ID), '0'),\n");
            sql.append("               'fm999,999,999,990.00') AS PART_PRICE,\n");
            sql.append("       TO_CHAR(P.SALE_PRICE1,'fm999,999,999,990.00') AS  SALE_PRICE1,");// 价格1
            sql.append("       TO_CHAR(P.SALE_PRICE2,'fm999,999,999,990.00') AS  SALE_PRICE2,");// 价格2
            sql.append("       TO_CHAR(P.SALE_PRICE3,'fm999,999,999,990.00') AS  SALE_PRICE3,");// 价格3
            sql.append("       TO_CHAR(P.SALE_PRICE4,'fm999,999,999,990.00') AS  SALE_PRICE4,");// 价格4
            sql.append("       TO_CHAR(P.SALE_PRICE5,'fm999,999,999,990.00') AS  SALE_PRICE5,");// 价格5
            sql.append("       TO_CHAR(P.SALE_PRICE6,'fm999,999,999,990.00') AS  SALE_PRICE6,");// 价格6
            sql.append("       TO_CHAR(P.SALE_PRICE7,'fm999,999,999,990.00') AS  SALE_PRICE7,");// 价格7
            sql.append("       TO_CHAR(P.SALE_PRICE8,'fm999,999,999,990.00') AS  SALE_PRICE8,");// 价格8
            sql.append("       TO_CHAR(P.SALE_PRICE9,'fm999,999,999,990.00') AS  SALE_PRICE9,");// 价格9
            sql.append("       D.PART_IS_CHANGHE,\n");
            sql.append("       D.PART_ID,\n");
            sql.append("       D.PART_CODE,\n");
            sql.append("       D.PART_OLDCODE,\n");
            sql.append("       D.PART_CNAME,\n");
            sql.append("       D.MODEL_NAME\n");
            sql.append("  FROM TT_PART_DEFINE D, TT_PART_SALES_PRICE P\n");
            sql.append(" WHERE 1 = 1\n");
            sql.append("   AND D.PART_ID = P.PART_ID\n");
            sql.append("   AND P.STATUS = 1");
            //sql.append(" AND P.UPDATE_BY = U.USER_ID(+) ");

            if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WRD) {
                sql.append(" and D.part_is_changhe =" + Constant.PART_IS_CHANGHE_02 + "\n");
            }

            if (!"".equals(partCode) && partCode != null)
                sql.append(" AND UPPER(D.PART_CODE) LIKE '%").append(partCode.trim().toUpperCase())
                        .append("%'");
            if (!"".equals(partOldcode) && partOldcode != null)
                sql.append(" AND UPPER(D.PART_OLDCODE) LIKE '%").append(partOldcode.trim().toUpperCase())
                        .append("%'");
            if (!"".equals(partCname) && partCname != null)
                sql.append(" AND D.PART_CNAME LIKE '%").append(partCname)
                        .append("%'");

            if (isChanghe != 0) {
                sql.append(" and D.PART_IS_CHANGHE=").append(isChanghe);
            }
            sql.append(CommonUtils.bindPartSQL(logonUser.getDealerId(), "D"));
            sql.append(" ORDER BY D.PART_OLDCODE ");
        }
        List<Map<String, Object>> list = pageQuery(sql.toString(), null,
                getFunName());
        return list;
    }
    


    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-18
     * @Title :
     * @Description: 查询配件销售信息（售后使用） OEM&DLR
     */
    public PageResult<Map<String, Object>> queryPartSalePriceCustomer(
            RequestWrapper request, int curPage, int pageSize, AclUserBean logonUser) {
        String partCode = CommonUtils.checkNull(request
                .getParamValue("PART_CODE"));// 件号
        String partOldcode = CommonUtils.checkNull(request
                .getParamValue("PART_OLDCODE"));// 配件编码
        String partCname = CommonUtils.checkNull(request
                .getParamValue("PART_CNAME"));// 配件名称
        String orgType = CommonUtils.checkNull(request
                .getParamValue("orgType"));// 用户类型
        String str_isChange = CommonUtils.checkNull(request.getParamValue("PART_IS_CHANGHE"));// 结算基地
        int isChanghe = 0;

        if (!"".equals(str_isChange)) {
            isChanghe = CommonUtils.parseInteger(str_isChange);
        }

        StringBuffer sql = new StringBuffer();

        if ("OEM".equals(orgType)) {
            sql.append(" select ");
            sql.append(" a.PRICE_ID ,");// 主键标识
            sql.append(" b.PART_CODE ,"); // 件号
            sql.append(" b.PART_OLDCODE ,");// 配件编码
            sql.append(" b.PART_CNAME ,");// 配件名称
            sql.append(" b.PART_IS_CHANGHE ,");//结算基地
            sql.append(" TO_CHAR(a.SALE_PRICE1,'fm999,999,999,990.00') AS  SALE_PRICE1,");// 价格1
            sql.append(" TO_CHAR(a.SALE_PRICE2,'fm999,999,999,990.00') AS  SALE_PRICE2,");// 价格2
            sql.append(" TO_CHAR(a.SALE_PRICE3,'fm999,999,999,990.00') AS  SALE_PRICE3,");// 价格3
            sql.append(" TO_CHAR(a.SALE_PRICE4,'fm999,999,999,990.00') AS  SALE_PRICE4,");// 价格4
            sql.append(" TO_CHAR(a.SALE_PRICE5,'fm999,999,999,990.00') AS  SALE_PRICE5,");// 价格5
            sql.append(" TO_CHAR(a.SALE_PRICE6,'fm999,999,999,990.00') AS  SALE_PRICE6,");// 价格6
            sql.append(" TO_CHAR(a.SALE_PRICE7,'fm999,999,999,990.00') AS  SALE_PRICE7,");// 价格7
            sql.append(" TO_CHAR(a.SALE_PRICE8,'fm999,999,999,990.00') AS  SALE_PRICE8,");// 价格8
            sql.append(" TO_CHAR(a.SALE_PRICE9,'fm999,999,999,990.00') AS  SALE_PRICE9,");// 价格9
            sql.append(" TO_CHAR(a.SALE_PRICE10,'fm999,999,999,990.00') AS  SALE_PRICE10,");// 价格10
            sql.append(" TO_CHAR(a.SALE_PRICE11,'fm999,999,999,990.00') AS  SALE_PRICE11,");// 价格11
            sql.append(" TO_CHAR(a.SALE_PRICE12,'fm999,999,999,990.00') AS  SALE_PRICE12,");// 价格12
            sql.append(" TO_CHAR(a.SALE_PRICE13,'fm999,999,999,990.00') AS  SALE_PRICE13,");// 价格13
            sql.append(" TO_CHAR(a.SALE_PRICE14,'fm999,999,999,990.00') AS  SALE_PRICE14,");// 价格14
            sql.append(" TO_CHAR(a.SALE_PRICE15,'fm999,999,999,990.00') AS  SALE_PRICE15,");// 价格15
            sql.append("  A.UPDATE_DATE," + "      U.NAME"
                    + " FROM TT_PART_SALES_PRICE A, TC_USER U, TT_PART_DEFINE B"
                    + "  WHERE A.PART_ID = B.PART_ID "
                    + "  AND A.UPDATE_BY = U.USER_ID(+) AND A.STATUS = 1 ");

            if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WRD) {
                sql.append(" and B.part_is_changhe =" + Constant.PART_IS_CHANGHE_02 + "\n");
            }
            if (!"".equals(partCode) && partCode != null)
                sql.append(" AND UPPER(B.PART_CODE) LIKE '%").append(partCode.trim().toUpperCase())
                        .append("%'");
            if (!"".equals(partOldcode) && partOldcode != null)
                sql.append(" AND UPPER(B.PART_OLDCODE) LIKE '%").append(partOldcode.trim().toUpperCase())
                        .append("%'");
            if (!"".equals(partCname) && partCname != null)
                sql.append(" AND B.PART_CNAME LIKE '%").append(partCname)
                        .append("%'");

            if (isChanghe != 0) {
                sql.append(" and B.PART_IS_CHANGHE=").append(isChanghe);
            }
        } else if ("DLR".equals(orgType)) {
            sql.append("SELECT TO_CHAR(NVL(PKG_PART.F_GETPRICE(2014041994277963, D.PART_ID), '0'),\n");
            sql.append("               'fm999,999,999,990.00') AS PART_PRICE,\n");
            sql.append("       TO_CHAR(P.SALE_PRICE1,'fm999,999,999,990.00') AS  SALE_PRICE1,");// 价格1
            sql.append("       TO_CHAR(P.SALE_PRICE2,'fm999,999,999,990.00') AS  SALE_PRICE2,");// 价格2
            sql.append("       TO_CHAR(P.SALE_PRICE3,'fm999,999,999,990.00') AS  SALE_PRICE3,");// 价格3
            sql.append("       TO_CHAR(P.SALE_PRICE4,'fm999,999,999,990.00') AS  SALE_PRICE4,");// 价格4
            sql.append("       TO_CHAR(P.SALE_PRICE5,'fm999,999,999,990.00') AS  SALE_PRICE5,");// 价格5
            sql.append("       TO_CHAR(P.SALE_PRICE6,'fm999,999,999,990.00') AS  SALE_PRICE6,");// 价格6
            sql.append("       TO_CHAR(P.SALE_PRICE7,'fm999,999,999,990.00') AS  SALE_PRICE7,");// 价格7
            sql.append("       TO_CHAR(P.SALE_PRICE8,'fm999,999,999,990.00') AS  SALE_PRICE8,");// 价格8
            sql.append("       TO_CHAR(P.SALE_PRICE9,'fm999,999,999,990.00') AS  SALE_PRICE9,");// 价格9
            sql.append("       D.PART_IS_CHANGHE,\n");
            sql.append("       D.PART_ID,\n");
            sql.append("       D.PART_CODE,\n");
            sql.append("       D.PART_OLDCODE,\n");
            sql.append("       D.PART_CNAME,\n");
            sql.append("       D.MODEL_NAME\n");
            sql.append("  FROM TT_PART_DEFINE D, TT_PART_SALES_PRICE P\n");
            sql.append(" WHERE 1 = 1\n");
            sql.append("   AND D.PART_ID = P.PART_ID\n");
            sql.append("   AND P.STATUS = 1");

            if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WRD) {
                sql.append(" and D.part_is_changhe =" + Constant.PART_IS_CHANGHE_02 + "\n");
            }

            if (!"".equals(partCode) && partCode != null)
                sql.append(" AND UPPER(D.PART_CODE) LIKE '%").append(partCode.trim().toUpperCase())
                        .append("%'");
            if (!"".equals(partOldcode) && partOldcode != null)
                sql.append(" AND UPPER(D.PART_OLDCODE) LIKE '%").append(partOldcode.trim().toUpperCase())
                        .append("%'");
            if (!"".equals(partCname) && partCname != null)
                sql.append(" AND D.PART_CNAME LIKE '%").append(partCname)
                        .append("%'");

            if (isChanghe != 0) {
                sql.append(" and D.PART_IS_CHANGHE=").append(isChanghe);
            }
            sql.append(CommonUtils.bindPartSQL(logonUser.getDealerId(), "D"));
            sql.append(" ORDER BY D.PART_OLDCODE ");
        }
        //sql.append(" ORDER BY D.PART_OLDCODE");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param code
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title :
     * @Description: 查询配件PARTID
     */
    public String getPartId(String code) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select b.part_id from TT_PART_SALES_PRICE a");
        sql.append(" join TT_PART_DEFINE b on a.PART_ID=b.PART_ID ");
        sql.append(" where 1=1 ");
        sql.append(" and b.PART_CODE='").append(code).append("'");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null,
                getFunName());
        if (list == null || list.size() <= 0) {
            return "";
        }
        return ((Map) list.get(0)).get("PART_ID").toString();
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-15
     * @Title : 查询设置列表List
     */
    public List<Map<String, Object>> queryPartPriceSettingList() {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_PRICE_SETTING where 1=1  ORDER BY type_id ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null,
                getFunName());
        return list;
    }

}
