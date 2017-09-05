package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PartBuySaleStoreDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartBuySaleStoreDao.class);
    private static final PartBuySaleStoreDao dao = new PartBuySaleStoreDao();

    private PartBuySaleStoreDao() {

    }

    public static final PartBuySaleStoreDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> query(RequestWrapper request, int curPage, int pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String WH_ID = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String startDate = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String endDate = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")).toUpperCase();
            String selType = CommonUtils.checkNull(request.getParamValue("selType")).toUpperCase();
            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT B1.*,abs(b1.OUT_QTY) OUT_QTY2\n");
            sql.append("  FROM VW_PART_STOCK_HIS B1\n");
            sql.append(" WHERE 1 = 1");
            if (!"".equals(selType) && null != selType) {
                sql.append("    AND B1.IN_TYPE LIKE '%" + selType + "%'");
            }
            if (!"".equals(startDate)) {
                sql.append(" and B1.CREATE_DATE>= to_date('").append(startDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            }
            if (!"".equals(endDate)) {
                sql.append(" and B1.CREATE_DATE<= to_date('").append(endDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
            }
            if (!"".equals(WH_ID)) {
                sql.append(" and B1.wh_id = " + WH_ID + "");
            }
            if (!"".equals(partOldcode)) {
                sql.append(" and B1.part_oldcode like '%").append(partOldcode).append("%'\n");
            }
            sql.append(" order by B1.CREATE_DATE desc");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryExport(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String WH_ID = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String startDate = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String endDate = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")).toUpperCase();
            StringBuffer sql = new StringBuffer("");

            sql.append("select *\n");
            sql.append("  from (SELECT TPOD.OUT_CODE AS CODE,\n");
            sql.append("TPOD.OUT_TYPE     AS ORDER_TYPE,\n");
            sql.append("             TPOD.CREATE_DATE,\n");
            sql.append("             TPOD.DEALER_NAME,\n");
            sql.append("             TPOD.NAME,\n");
            sql.append("             TPOD.PART_OLDCODE,\n");
            sql.append("             (-TPOD.PART_NUM) PART_NUM,\n");
            sql.append("             TPOD.LOC_NAME,\n");
            sql.append("             TPOD.SALE_PRICE3,\n");
            sql.append("             TPOD.STOCK_QTY,\n");
            sql.append("             TPOD.stock_amount\n");
            sql.append("          FROM vw_part_outstock_dtl TPOD where 1=1\n");
            if (!"".equals(startDate)) {
                sql.append(" and TPOD.CREATE_DATE>= to_date('").append(startDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            }
            if (!"".equals(endDate)) {
                sql.append(" and TPOD.CREATE_DATE<= to_date('").append(endDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
            }
            if (!"".equals(WH_ID)) {
                sql.append(" and TPOD.wh_id = " + WH_ID + "");
            }
            if (!"".equals(partOldcode)) {
                sql.append(" and tpod.part_oldcode like '%").append(partOldcode).append("%'");
            }
            sql.append("        UNION ALL\n");
            sql.append("        SELECT TPPI.IN_CODE AS CODE,\n");
            sql.append("    TPPI.IN_TYPE      AS ORDER_TYPE,\n");
            sql.append("             TPPI.CREATE_DATE,\n");
            sql.append("             TPPI.DEALER_NAME,\n");
            sql.append("             TPPI.NAME,\n");
            sql.append("             TPPI.PART_OLDCODE,\n");
            sql.append("             TPPI.PART_NUM,\n");
            sql.append("             TPPI.LOC_NAME,\n");
            sql.append("             TPPI.SALE_PRICE3,\n");
            sql.append("             TPPI.ITEM_QTY,\n");
            sql.append("             TPPI.stock_amount\n");
            sql.append("          FROM vw_part_instock_dtl TPPI\n");
            sql.append("\n");
            sql.append("         WHERE 1 = 1\n");
            if (!"".equals(startDate)) {
                sql.append(" and TPPI.CREATE_DATE>= to_date('").append(startDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            }
            if (!"".equals(endDate)) {
                sql.append(" and TPPI.CREATE_DATE<= to_date('").append(endDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
            }
            if (!"".equals(WH_ID)) {
                sql.append(" and TPPI.wh_id = " + WH_ID + "");
            }
            if (!"".equals(partOldcode)) {
                sql.append(" and tppi.part_oldcode like '%").append(partOldcode).append("%'");
            }
            sql.append("        )\n");
            sql.append(" order by CREATE_DATE asc\n");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

}