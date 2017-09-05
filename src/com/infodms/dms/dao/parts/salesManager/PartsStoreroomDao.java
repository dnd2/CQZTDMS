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

public class PartsStoreroomDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PartsStoreroomDao.class);
    private static final PartsStoreroomDao dao = new PartsStoreroomDao();

    private PartsStoreroomDao() {

    }

    public static final PartsStoreroomDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }


    public PageResult<Map<String, Object>> query(RequestWrapper request, int curPage, int pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            //String a = CommonUtils.checkNull(request.getParamValue(""));
            //String b = CommonUtils.checkNull(request.getParamValue(""));
            String StartDate = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String EndDate = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
            String Oldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")).toUpperCase();

            StringBuffer sql = new StringBuffer("");

            sql.append("select a.part_id,\n");
            sql.append("a.PART_OLDCODE,\n");
            sql.append("a.PART_NAME,\n");
            sql.append("a.code,\n");
            sql.append("a.to_type,\n");
            sql.append("a.NAME,\n");
            sql.append("a.CREATE_DATE,\n");
            sql.append("a.LOC_NAME,\n");
            sql.append("sum(a.ru_PART_NUM) ru_PART_NUM,\n");
            sql.append("sum(a.chu_PART_NUM) chu_PART_NUM,\n");
            sql.append("sum(a.ru_PART_NUM) - sum(a.chu_PART_NUM) shengyu_num\n");//--库存数量
            //--库存金额
            sql.append("from (SELECT r.PART_ID,\n");
            sql.append("R.PART_OLDCODE,\n");
            sql.append("r.PART_NAME,\n");
            sql.append("r.IN_CODE code,\n");
            sql.append("r.IN_TYPE to_type,\n");
            sql.append("r.NAME,\n");
            sql.append("r.CREATE_DATE,\n");
            sql.append("r.LOC_NAME,\n");
            sql.append("nvl(r.PART_NUM, 0) ru_PART_NUM,\n");
            sql.append("0 chu_PART_NUM\n");
            sql.append("FROM VW_PART_INSTOCK_DTL R\n");

            if (!"".equals(StartDate)) {
                sql.append("WHERE TO_CHAR(r.CREATE_DATE, 'yyyy-MM-dd') >= " + StartDate + "");
            }
            if (!"".equals(EndDate)) {
                sql.append(" AND TO_CHAR(r.CREATE_DATE, 'yyyy-MM-dd') <= " + EndDate + "");
            }
            if (!"".equals(Oldcode)) {
                sql.append(" AND R.PART_OLDCODE like '%").append(Oldcode).append("%'");
            }
            sql.append(" UNION\n");

            sql.append("SELECT rr.PART_ID,\n");
            sql.append("Rr.PART_OLDCODE,\n");
            sql.append("rr.PART_NAME,\n");
            sql.append("rr.Out_Code code,\n");
            sql.append("rr.OUT_TYPE to_type,\n");
            sql.append("rr.NAME,\n");
            sql.append("rr.CREATE_DATE,\n");
            sql.append("rr.LOC_NAME,\n");
            sql.append("0 ru_PART_NUM,\n");
            sql.append("nvl(rr.PART_NUM, 0) chu_PART_NUM\n");
            sql.append("FROM VW_PART_OUTSTOCK_DTL RR\n");
            if (!"".equals(StartDate)) {
                sql.append("WHERE TO_CHAR(rr.CREATE_DATE, 'yyyy-MM-dd') >= '" + StartDate + "'");
            }
            if (!"".equals(EndDate)) {
                sql.append(" AND TO_CHAR(rr.CREATE_DATE, 'yyyy-MM-dd') <= '" + EndDate + "'");
            }
            if (!"".equals(Oldcode)) {
                sql.append(" AND Rr.PART_OLDCODE like '%").append(Oldcode).append("%'");
            }
            sql.append(") A\n");

            sql.append("group BY a.part_id,\n");
            sql.append("a.PART_OLDCODE,\n");
            sql.append("a.PART_NAME,\n");
            sql.append("a.code,\n");
            sql.append("a.to_type,\n");
            sql.append("a.NAME,\n");
            sql.append("a.CREATE_DATE,\n");
            sql.append("a.LOC_NAME\n");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryExport(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String StartDate = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String EndDate = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
            String Oldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")).toUpperCase();

            StringBuffer sql = new StringBuffer("");

            sql.append("select a.part_id,\n");
            sql.append("a.PART_OLDCODE,\n");
            sql.append("a.PART_NAME,\n");
            sql.append("a.code,\n");
            sql.append("a.to_type,\n");
            sql.append("a.NAME,\n");
            sql.append("a.CREATE_DATE,\n");
            sql.append("a.LOC_NAME,\n");
            sql.append("sum(a.ru_PART_NUM) ru_PART_NUM,\n");
            sql.append("sum(a.chu_PART_NUM) chu_PART_NUM,\n");
            sql.append("sum(a.ru_PART_NUM) - sum(a.chu_PART_NUM) shengyu_num\n");//--库存数量
            //--库存金额
            sql.append("from (SELECT r.PART_ID,\n");
            sql.append("R.PART_OLDCODE,\n");
            sql.append("r.PART_NAME,\n");
            sql.append("r.IN_CODE code,\n");
            sql.append("r.IN_TYPE to_type,\n");
            sql.append("r.NAME,\n");
            sql.append("r.CREATE_DATE,\n");
            sql.append("r.LOC_NAME,\n");
            sql.append("nvl(r.PART_NUM, 0) ru_PART_NUM,\n");
            sql.append("0 chu_PART_NUM\n");
            sql.append("FROM VW_PART_INSTOCK_DTL R\n");
            if (!"".equals(StartDate)) {
                sql.append("WHERE TO_CHAR(r.CREATE_DATE, 'yyyy-MM-dd') >= '" + StartDate + "'");
            }
            if (!"".equals(EndDate)) {
                sql.append(" AND TO_CHAR(r.CREATE_DATE, 'yyyy-MM-dd') <= '" + EndDate + "'");
            }
            if (!"".equals(Oldcode)) {
                sql.append(" AND R.PART_OLDCODE like '%").append(Oldcode).append("%'");
            }
            sql.append(" UNION\n");

            sql.append("SELECT rr.PART_ID,\n");
            sql.append("Rr.PART_OLDCODE,\n");
            sql.append("rr.PART_NAME,\n");
            sql.append("rr.Out_Code code,\n");
            sql.append("rr.OUT_TYPE to_type,\n");
            sql.append("rr.NAME,\n");
            sql.append("rr.CREATE_DATE,\n");
            sql.append("rr.LOC_NAME,\n");
            sql.append("0 ru_PART_NUM,\n");
            sql.append("nvl(rr.PART_NUM, 0) chu_PART_NUM\n");
            sql.append("FROM VW_PART_OUTSTOCK_DTL RR\n");
            if (!"".equals(StartDate)) {
                sql.append("WHERE TO_CHAR(rr.CREATE_DATE, 'yyyy-MM-dd') >= " + StartDate + "");
            }
            if (!"".equals(EndDate)) {
                sql.append(" AND TO_CHAR(rr.CREATE_DATE, 'yyyy-MM-dd') <= " + EndDate + "");
            }
            if (!"".equals(Oldcode)) {
                sql.append(" AND Rr.PART_OLDCODE like '%").append(Oldcode).append("%'");
            }
            sql.append(") A\n");

            sql.append("group BY a.part_id,\n");
            sql.append("a.PART_OLDCODE,\n");
            sql.append("a.PART_NAME,\n");
            sql.append("a.code,\n");
            sql.append("a.to_type,\n");
            sql.append("a.NAME,\n");
            sql.append("a.CREATE_DATE,\n");
            sql.append("a.LOC_NAME\n");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }


}
