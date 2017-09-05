package com.infodms.dms.dao.parts.purchaseOrderManager;


import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

public class LogTimelyArrivalRateDao extends BaseDao {

    public static Logger logger = Logger.getLogger(LogTimelyArrivalRateDao.class);

    private static final LogTimelyArrivalRateDao dao = new LogTimelyArrivalRateDao();

    private LogTimelyArrivalRateDao() {

    }

    public static final LogTimelyArrivalRateDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> queryLogTimelyArrivalRateList(RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));
            String query = CommonUtils.checkNull(request.getParamValue("Query"));
            StringBuffer sql = new StringBuffer("");
            if ("1".equals(query)) {
                sql.append("SELECT NVL(RT.TRANSPORT_ORG || '--' || RT.TRANS_TYPE, '合计') TRANSPORT_ORG,\n");
                sql.append("       COUNT(RT.TRPLAN_ID) TRCNT,\n");
                sql.append("       SUM(RT.WL) WLCNT,\n");
                sql.append("       TO_CHAR(SUM(RT.WL) / COUNT(RT.TRPLAN_ID) * 100, '999.99') || '%' WLRATE\n");
                sql.append("  FROM VW_PART_RP_TRANS RT\n");
                sql.append(" WHERE 1 = 1");
                if ("1".equals(flag)) {
                    sql.append("   AND RT.TRANSPORT_ORG <> '补单'\n");
                    sql.append("   AND RT.TRANSPORT_ORG <> '自提'");
                }
                if (!"".equals(SCREATE_DATE)) {
                    sql.append("	AND TRUNC(RT.CREATE_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd') \n");
                }
                if (!"".equals(ECREATE_DATE)) {
                    sql.append("	AND TRUNC(RT.CREATE_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd') \n");
                }
                sql.append(" GROUP BY ROLLUP(RT.TRANSPORT_ORG || '--' || RT.TRANS_TYPE)\n");
                sql.append(" ORDER BY RT.TRANSPORT_ORG || '--' || RT.TRANS_TYPE");
            } else {
                sql.append("SELECT RT.*\n");
                sql.append("  FROM VW_PART_RP_TRANS RT\n");
                sql.append(" WHERE 1 = 1\n");
                if ("1".equals(flag)) {
                    sql.append("   AND RT.TRANSPORT_ORG <> '补单'\n");
                    sql.append("   AND RT.TRANSPORT_ORG <> '自提'");
                }
                if (!"".equals(SCREATE_DATE)) {
                    sql.append("	AND TRUNC(RT.CREATE_DATE) >= TO_DATE('" + SCREATE_DATE + "', 'yyyy-mm-dd') \n");
                }
                if (!"".equals(ECREATE_DATE)) {
                    sql.append("	AND TRUNC(RT.CREATE_DATE) <= TO_DATE('" + ECREATE_DATE + "', 'yyyy-mm-dd') \n");
                }
            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }


}
