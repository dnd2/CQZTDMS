package com.infodms.dms.dao.parts.partAbnormityManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import common.Logger;

import java.sql.ResultSet;
import java.util.Map;

public class PartAbnormityDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PartAbnormityDao.class);

    private static final PartAbnormityDao dao = new PartAbnormityDao();

    private PartAbnormityDao() {
    }

    public static final PartAbnormityDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    public PageResult<Map<String, Object>> query(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        try {
            String startTime = CommonUtils.checkNull(request.getParamValue("startTime"));
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));

            StringBuffer sql = new StringBuffer();

            sql.append("WITH TRANS AS\n");
            sql.append(" (SELECT OM.TRPLAN_ID, SUM(OD.OUTSTOCK_QTY) TRCNT\n");
            sql.append("    FROM TT_PART_OUTSTOCK_DTL OD, TT_PART_OUTSTOCK_MAIN OM\n");
            sql.append("   WHERE 1 = 1\n");
            sql.append("     AND OD.OUT_ID = OM.OUT_ID\n");
            if (!"".equals(startTime) && null != startTime) {
                sql.append("     AND TRUNC(OM.CREATE_DATE) >= TO_DATE('" + startTime + "', 'yyyy-MM-dd') --发运日期\n");
            }
            if (!"".equals(endTime) && null != endTime) {
                sql.append("     AND TRUNC(OM.CREATE_DATE) <= TO_DATE('" + endTime + "', 'yyyy-MM-dd') --发运日期\n");
            }
            sql.append("   GROUP BY OM.TRPLAN_ID),\n");
            sql.append("INSTOCK_EXCEPTION_LOG AS\n");
            sql.append(" (SELECT OM.TRPLAN_ID, SUM(LG.EXCEPTION_NUM) ECNT\n");
            sql.append("    FROM TT_PART_INSTOCK_EXCEPTION_LOG LG,\n");
            sql.append("         TT_PART_DLR_INSTOCK_MAIN      M,\n");
            sql.append("         TT_PART_TRANS                 PT,\n");
            sql.append("         TT_PART_OUTSTOCK_MAIN         OM\n");
            sql.append("   WHERE LG.IN_ID = M.IN_ID\n");
            sql.append("     AND M.TRANS_ID = PT.TRANS_ID\n");
            sql.append("     AND PT.OUT_ID = OM.OUT_ID\n");
            sql.append("   GROUP BY OM.TRPLAN_ID)\n");
            sql.append("SELECT '发出数量' PN, --类型\n");
            sql.append("       NVL(SUM(TR.TRCNT), 0) TRCNT, --发出数\n");
            sql.append("       NVL(SUM(EL.ECNT), 0) ECNT, --差异数\n");
            sql.append("       TO_CHAR(NVL(SUM(EL.ECNT), 0) / NVL(SUM(TR.TRCNT), 1) * 100,\n");
            sql.append("               '999.9999') || '%' WLRATE --差异率\n");
            sql.append("  FROM TRANS TR, INSTOCK_EXCEPTION_LOG EL\n");
            sql.append(" WHERE TR.TRPLAN_ID = EL.TRPLAN_ID(+)\n");
            sql.append("UNION ALL\n");
            sql.append("SELECT '订单数量' PN, --类型\n");
            sql.append("       COUNT(TR.TRPLAN_ID) TRCNT, --发出数\n");
            sql.append("       COUNT(EL.TRPLAN_ID) ECNT, --差异数\n");
            sql.append("       TO_CHAR(COUNT(EL.TRPLAN_ID) /\n");
            sql.append("               DECODE(COUNT(TR.TRPLAN_ID), 0, 1, COUNT(TR.TRPLAN_ID)) * 100,\n");
            sql.append("               '999.9999') || '%' WLRATE --差异率\n");
            sql.append("  FROM TRANS TR, INSTOCK_EXCEPTION_LOG EL\n");
            sql.append(" WHERE TR.TRPLAN_ID = EL.TRPLAN_ID(+)");

            PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
            return ps;

        } catch (Exception e) {
            throw e;
        }
    }
}
