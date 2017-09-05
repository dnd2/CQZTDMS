package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PartOderGoodsDetailDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartOderGoodsDetailDao.class);
    private static final PartOderGoodsDetailDao dao = new PartOderGoodsDetailDao();

    private PartOderGoodsDetailDao() {

    }

    public static final PartOderGoodsDetailDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> query(RequestWrapper request, int curPage, int pageSize, String flag) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String DEALER_CODE = CommonUtils.checkNull(request.getParamValue("dealerCode")).toUpperCase();//服务站号
            String DEALER_NAME = CommonUtils.checkNull(request.getParamValue("dealerName"));//服务站名称
            String ORDER_CODE = CommonUtils.checkNull(request.getParamValue("orderCode")).toUpperCase();//订单号
            String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("partOldCode")).toUpperCase();//配件编码
            String PART_CNAME = CommonUtils.checkNull(request.getParamValue("partCnaem"));//配件名称
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));//出库日期从
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));//出库日期到
            String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); //大区

            StringBuffer sql = new StringBuffer("");
            //明细查询
            if ("1".equals(flag)) {
                sql.append("SELECT * FROM VW_PART_TRANS_DTL OM WHERE 1 = 1");
            } else {
                //汇总查询
                sql.append("SELECT SUM(om.outstock_qty) outQty,SUM(om.sale_amount) outAmount\n");
                sql.append("  FROM VW_PART_TRANS_DTL OM\n");
                sql.append(" WHERE 1 = 1");
            }
            sql.append("   AND OM.SELLER_ID = 2010010100070674");
            if (!"".equals(SCREATE_DATE)) {
                sql.append(" AND TRUNC(OM.SUBMIT_DATE) >= TO_DATE('").append(SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')\n");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append(" AND TRUNC(OM.SUBMIT_DATE) <= TO_DATE('").append(ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')\n");
            }
            if (!"".equals(DEALER_CODE)) {
                if (DEALER_CODE != null) {
                    if (!(DEALER_CODE.split(",").length <= 0)) {
                        sql.append(" AND OM.DEALER_CODE IN(");
                        String[] paramStrArr = DEALER_CODE.split(",");
                        for (int i = 0; i < paramStrArr.length; i++) {
                            String tempCode = paramStrArr[i];
                            if (i == paramStrArr.length - 1) {
                                sql.append("'" + tempCode + "')\n");
                            } else {
                                sql.append("'" + tempCode + "', ");
                            }
                        }
                    }
                }
            }
            if (!"".equals(DEALER_NAME)) {
                sql.append(" AND OM.DEALER_NAME LIKE '%").append(DEALER_NAME).append("%'\n");
            }
            if (!"".equals(ORDER_CODE)) {
                sql.append(" and OM.ORDER_CODE like '%").append(ORDER_CODE).append("%'\n");
            }
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" and OM.PART_OLDCODE like '%").append(PART_OLDCODE).append("%'\n");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" and OM.PART_CNAME like '%").append(PART_CNAME).append("%'\n");
            }
            //增加大区限制
            if (!"".equals(orgCode) && null != orgCode) {
                sql.append("AND EXISTS (SELECT 1\n");
                sql.append("         FROM VW_ORG_DEALER_SERVICE S\n");
                sql.append("        WHERE S.DEALER_ID = OM.DEALER_ID\n");
                sql.append("          AND S.Org_Code IN (" + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(orgCode) + "))\n");
            }

            sql.append("  ORDER BY OM.SUBMIT_DATE\n");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }
}