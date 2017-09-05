package com.infodms.dms.dao.parts.baseManager.partBaseQuery.partPurPriceManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : huchao
 *         LastDate: 2013-4-18
 * @ClassName : partPurPriceDao
 * @Description : 配件采购价格DAO
 */
public class partPurPriceDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partPurPriceDao.class);
    private static final partPurPriceDao dao = new partPurPriceDao();

    private partPurPriceDao() {

    }

    public static final partPurPriceDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param partCode
     * @param : @param partOldCode
     * @param : @param partName
     * @param : @param venderName
     * @param : @param buyerName
     * @param : @param state
     * @param : @param isGuard
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : 分页查询配件采购价格
     */
    public PageResult<Map<String, Object>> queryBuyPriceList(String partCode,
                                                             String partOldCode, String partName, String venderName,
                                                             String buyerName, int state, int isGuard, int isChanghe, Integer curPage,
                                                             Integer pageSize, int poseBusType) throws Exception {

        PageResult<Map<String, Object>> ps;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "SELECT T1.PART_ID,\n" +
                            "       T1.PART_CODE,\n" +
                            "       T1.PART_OLDCODE,\n" +
                            "       T1.PART_CNAME,\n" +
                            "       T2.VENDER_ID,\n" +
                            "       T2.VENDER_CODE,\n" +
                            "       T2.VENDER_NAME,\n" +
                            "       T3.PRICE_ID,\n" +
                            "       TO_CHAR(T3.BUY_PRICE,'fm999,999,999,990.00') AS BUY_PRICE,\n" +
                            "       T3.IS_GUARD,\n" +
                            "       T4.NAME BUYER_NAME,\n" +
                            "       T3.CREATE_DATE,\n" +
                            "       T3.UPDATE_DATE,\n" +
                            "       T1.PART_IS_CHANGHE,\n" +
                            "       T5.NAME ACNT,\n" +
                            "       T3.STATE\n" +
                            "  FROM TT_PART_DEFINE        T1,\n" +
                            "       TT_PART_VENDER_DEFINE T2,\n" +
                            "       TT_PART_BUY_PRICE     T3,\n" +
                            "       TC_USER               T4,\n" +
                            "       TC_USER               T5\n" +
                            " WHERE T1.PART_ID = T3.PART_ID\n" +
                            "   AND T2.VENDER_ID = T3.VENDER_ID\n" +
                            "   AND T1.BUYER_ID = T4.USER_ID(+)\n" +
                            "   AND T3.UPDATE_BY = T5.USER_ID(+)\n" +
                            "   AND T3.STATUS = 1");

            if (poseBusType == Constant.POSE_BUS_TYPE_WRD) {
                sql.append(" and T1.part_is_changhe =" + Constant.PART_IS_CHANGHE_02 + "\n");
            }

            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T1.PART_CODE) LIKE '%").append(partCode.trim().toUpperCase())
                        .append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T1.PART_OLDCODE) LIKE '%").append(partOldCode.trim().toUpperCase())
                        .append("%'");
            }
            if (!"".equals(partName)) {
                sql.append(" AND UPPER(T1.PART_CNAME) LIKE '%").append(partName.trim().toUpperCase())
                        .append("%'");
            }
            if (!"".equals(venderName)) {
                sql.append(" AND UPPER(T2.VENDER_NAME) LIKE '%").append(venderName.trim().toUpperCase())
                        .append("%'");
            }
            if (!"".equals(buyerName)) {
                sql.append(" AND UPPER(T4.NAME) LIKE '%").append(buyerName.trim().toUpperCase())
                        .append("%'");
            }
            if (state != 0) {
                sql.append(" AND T3.STATE = ").append(state);
            }
            if (isGuard != 0) {
                sql.append(" AND T3.IS_GUARD = ").append(isGuard);
            }

            if (isChanghe != 0) {
                sql.append(" and t1.PART_IS_CHANGHE=").append(isChanghe);
            }

            sql.append(" ORDER BY T1.PART_OLDCODE, T1.PART_CNAME, T1.PART_CODE ");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }

        return ps;
    }

    /**
     * @param : @param request
     * @param : @return
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-7
     * @Title :查询满足条件的采购价格信息,用于数据下载
     */
    public List<Map<String, Object>> queryPartBuyPrice(RequestWrapper request)
            throws Exception {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        List<Map<String, Object>> list;
        try {

            String partCode = CommonUtils.checkNull(request
                    .getParamValue("PART_CODE"));// 件号
            String partOldCode = CommonUtils.checkNull(request
                    .getParamValue("PART_OLDCODE"));// 配件编码
            String partName = CommonUtils.checkNull(request
                    .getParamValue("PART_CNAME"));// 配件名称
            String venderName = CommonUtils.checkNull(request
                    .getParamValue("VENDER_NAME"));// 供应商名称
            String buyerName = CommonUtils.checkNull(request
                    .getParamValue("BUYER_NAME"));// 采购员
            String str_state = CommonUtils.checkNull(request
                    .getParamValue("STATE"));// 是否有效
            String str_isGuard = CommonUtils.checkNull(request
                    .getParamValue("IS_GUARD"));// 是否暂估
            String str_isChange = CommonUtils.checkNull(request.getParamValue("PART_IS_CHANGHE"));// 结算基地

            int state = 0;
            int isGuard = 0;
            int isChanghe = 0;

            if (!"".equals(str_state)) {
                state = CommonUtils.parseInteger(str_state);
            }
            if (!"".equals(str_isGuard)) {
                isGuard = CommonUtils.parseInteger(str_isGuard);
            }
            if (!"".equals(str_isChange)) {
                isChanghe = CommonUtils.parseInteger(str_isChange);
            }

            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "select t1.part_code,t1.part_oldcode,t1.part_cname,t2.vender_code,t2.vender_name,TO_CHAR(t3.buy_price,'fm999,999,999,990.00') AS buy_price,decode(t3.is_guard,")
                    .append(Constant.IS_GUARD_YES).append(",'是',")
                    .append(Constant.IS_GUARD_NO).append(",'否')")
                    .append(" is_guard,t4.name BUYER_NAME, T3.CREATE_DATE,\n" +
                            "       T3.UPDATE_DATE,\n" +
                            "       T5.NAME ACNT,\n")
                    .append(" decode(T1.PART_IS_CHANGHE, " + Constant.PART_IS_CHANGHE_01 + ",'昌河'," + Constant.PART_IS_CHANGHE_02 + ",'东安') PART_IS_CHANGHE, ");
            sql.append("decode(T3.STATE, " + Constant.STATUS_ENABLE + ",'有效'," + Constant.STATUS_DISABLE + ",'无效') STATE ");
            sql.append(" FROM TT_PART_DEFINE        T1,\n" +
                    "       TT_PART_VENDER_DEFINE T2,\n" +
                    "       TT_PART_BUY_PRICE     T3,\n" +
                    "       TC_USER               T4,\n" +
                    "       TC_USER               T5\n" +
                    " WHERE T1.PART_ID = T3.PART_ID\n" +
                    "   AND T2.VENDER_ID = T3.VENDER_ID\n" +
                    "   AND T1.BUYER_ID = T4.USER_ID(+)\n" +
                    "   AND T3.UPDATE_BY = T5.USER_ID(+)\n" +
                    "   AND T3.STATUS = 1");

            if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WRD) {
                sql.append(" and T1.part_is_changhe =" + Constant.PART_IS_CHANGHE_02 + "\n");
            }

            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T1.PART_CODE) LIKE '%").append(partCode.trim().toUpperCase())
                        .append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T1.PART_OLDCODE) LIKE '%").append(partOldCode.trim().toUpperCase())
                        .append("%'");
            }
            if (!"".equals(partName)) {
                sql.append(" AND UPPER(T1.PART_CNAME) LIKE '%").append(partName.trim().toUpperCase())
                        .append("%'");
            }
            if (!"".equals(venderName)) {
                sql.append(" AND UPPER(T2.VENDER_NAME) LIKE '%").append(venderName.trim().toUpperCase())
                        .append("%'");
            }
            if (!"".equals(buyerName)) {
                sql.append(" AND UPPER(T4.NAME) LIKE '%").append(buyerName.trim().toUpperCase())
                        .append("%'");
            }
            if (state != 0) {
                sql.append(" AND T3.STATE = ").append(state);
            }
            if (isGuard != 0) {
                sql.append(" AND T3.IS_GUARD = ").append(isGuard);
            }

            if (isChanghe != 0) {
                sql.append(" and t1.PART_IS_CHANGHE=").append(isChanghe);
            }

            sql.append(" ORDER BY T1.PART_OLDCODE, T1.PART_CNAME, T1.PART_CODE ");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;

    }
}
