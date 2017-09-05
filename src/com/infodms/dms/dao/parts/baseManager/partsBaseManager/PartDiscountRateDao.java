package com.infodms.dms.dao.parts.baseManager.partsBaseManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartDiscountDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartDiscountRateDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartDiscountRateDao.class);

    private static final PartDiscountRateDao dao = new PartDiscountRateDao();

    private PartDiscountRateDao() {

    }

    public static final PartDiscountRateDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> queryPartDiscountList(
            TtPartDiscountDefinePO po, String startDate, String endDate,
            AclUserBean logonUser,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.DISCOUNT_ID, T.DISCOUNT_TYPE,T.DISCOUNT_RATE,T.VALID_FROM,T.VALID_TO,T.CREATE_DATE,U.NAME,T.STATE FROM TT_PART_DISCOUNT_DEFINE T,TC_USER U WHERE T.CREATE_BY=U.USER_ID");
            if (po.getDiscountType() != null) {
                sql.append(" AND T.DISCOUNT_TYPE=")
                        .append(po.getDiscountType());
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T.VALID_FROM)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T.VALID_TO)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (po.getState() != null) {
                sql.append(" AND T.STATE=").append(po.getState());
            }
            sql.append(" AND T.STATUS=1");
            sql.append(" ORDER BY T.CREATE_DATE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> queryPartDiscountDtlList(
            TtPartDiscountDefinePO po, String startDate, String endDate,
            String dealerName, String partOldCode, AclUserBean logonUser,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T2.DP_CODE,T2.DP_NAME,T1.DISCOUNT_TYPE,T1.DISCOUNT_RATE,T1.VALID_FROM,T1.VALID_TO,T2.CREATE_DATE,U.NAME,T2.STATE");
            sql.append(" FROM TT_PART_DISCOUNT_DEFINE T1,TT_PART_DISCOUNT_DEFINE_DTL T2,TC_USER U WHERE T1.DISCOUNT_ID=T2.DISCOUNT_ID");
            sql.append("  AND T2.CREATE_BY=U.USER_ID");
            if (po.getDiscountType() != null) {
                sql.append(" AND T1.DISCOUNT_TYPE=")
                        .append(po.getDiscountType());
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T1.VALID_FROM)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T1.VALID_TO)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(dealerName)) {

                sql.append(" AND T2.DP_NAME like '%").append(dealerName).append("%'\n");
            }
            if (!"".equals(partOldCode)) {

                sql.append(" AND T2.DP_CODE like '%").append(partOldCode).append("%'\n");
            }
            if (po.getState() != null) {
                sql.append(" AND T2.STATE=").append(po.getState());
            }
            sql.append(" AND T2.STATUS=1");
            sql.append(" ORDER BY T2.CREATE_DATE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List queryDearlerType() throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.FIX_ID,T.FIX_GOUPTYPE,T.FIX_NAME,T.FIX_VALUE FROM TT_PART_FIXCODE_DEFINE T WHERE T.FIX_GOUPTYPE=");
            sql.append(Constant.FIXCODE_TYPE_05);
            List list = pageQuery(sql.toString(), null, getFunName());
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPartDiscountDtlTempList(
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.DTL_ID,T.DP_ID,T.DP_CODE,T.DP_NAME,T.STATE FROM TT_PART_DISCOUNT_DTL_TEMP T");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public Map getPartDiscountInfo(String discountId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.DISCOUNT_ID,T.DISCOUNT_TYPE,T.DISCOUNT_RATE,TRUNC(T.VALID_FROM) VALID_FROM,TRUNC(T.VALID_TO) VALID_TO,T.DEALER_TYPE,T.ORDER_AMOUNT,T.STATE");
            sql.append(" FROM TT_PART_DISCOUNT_DEFINE T WHERE T.DISCOUNT_ID=");
            sql.append(CommonUtils.parseLong(discountId));
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPartDiscountDtlByIdList(
            String discountId, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.DTL_ID,T.DP_ID,T.DP_CODE,T.DP_NAME,T1.DISCOUNT_RATE,T.STATE ,t.rate,t.amount FROM TT_PART_DISCOUNT_DEFINE_DTL T,TT_PART_DISCOUNT_DEFINE T1");
            sql.append(" WHERE T.DISCOUNT_ID=T1.DISCOUNT_ID AND T.DISCOUNT_ID=").append(discountId);
            sql.append(" AND T.STATUS=1");
            sql.append(" ORDER BY T.CREATE_DATE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

}
