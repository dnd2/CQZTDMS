package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PartGxWLDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartGxWLDao.class);
    private static final PartGxWLDao dao = new PartGxWLDao();

    private PartGxWLDao() {

    }

    public static final PartGxWLDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> query(RequestWrapper request, int curPage, int pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {

//        	String DEALER_CODE = CommonUtils.checkNull(request.getParamValue("dealerCode")).toUpperCase();//服务站号
//        	String DEALER_NAME = CommonUtils.checkNull(request.getParamValue("dealerName"));//服务站名称
//        	String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("partOldCode")).toUpperCase();//配件编码
//        	String PART_CNAME = CommonUtils.checkNull(request.getParamValue("partCnaem"));//配件名称 
//        	String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));//发运日期段起始日期
//        	String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));//发运日期段结束日期        	
//        	


            StringBuffer sql = new StringBuffer("");

            sql.append("select distinct dm.dealer_code,\n");
            sql.append("dm.dealer_name,\n");
            sql.append("dm.plan_code,\n");
            sql.append("dm.order_code,\n");
            sql.append("dt.pkg_no,\n");
            sql.append("lg.logi_name,\n");
            sql.append("dt.ass_no,\n");
            sql.append("w.ass_date,\n");
            sql.append("(select fd.fix_name \n");
            sql.append("from tt_part_fixcode_define fd \n");
            sql.append("where fd.fix_gouptype = '92251008' \n");
            sql.append("and fd.fix_value = dt.trans_org) AS trans_org,\n");
            sql.append("dt.wl_no,\n");
            sql.append("dt.wl_date \n");
            sql.append("from tt_sales_assign     w,\n");
            sql.append("tt_sales_ass_detail d,\n");
            sql.append("tt_sales_alloca_de  de,\n");
            sql.append("tt_sales_waybill    wb,\n");
            sql.append("tt_sales_logi       lg,\n");
            sql.append("tt_part_dplan_dtl   dt,\n");
            sql.append("tt_part_dplan_main  dm \n");
            sql.append("where w.ass_id = d.ass_id \n");
            sql.append("and d.ass_detail_id = de.ass_detail_id \n");
            sql.append("and de.bill_id = wb.bill_id \n");
            sql.append("and wb.logi_id = lg.logi_id \n");
            sql.append("and dt.plan_id = dm.plan_id \n");
            sql.append("and dt.ass_no = w.ass_no(+)\n");


            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryExport(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list = null;
        try {
//        	String DEALER_CODE = CommonUtils.checkNull(request.getParamValue("dealerCode")).toUpperCase();//服务站号
//        	String DEALER_NAME = CommonUtils.checkNull(request.getParamValue("dealerName"));//服务站名称
//        	String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("partOldCode")).toUpperCase();//配件编码
//        	String PART_CNAME = CommonUtils.checkNull(request.getParamValue("partCnaem"));//配件名称 
//        	String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));//发运日期段起始日期
//        	String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));//发运日期段结束日期        	


            StringBuffer sql = new StringBuffer("");

            sql.append("select distinct dm.dealer_code,\n");
            sql.append("dm.dealer_name,\n");
            sql.append("dm.plan_code,\n");
            sql.append("dm.order_code,\n");
            sql.append("dt.pkg_no,\n");
            sql.append("lg.logi_name,\n");
            sql.append("dt.ass_no,\n");
            sql.append("w.ass_date,\n");
            sql.append("(select fd.fix_name \n");
            sql.append("from tt_part_fixcode_define fd \n");
            sql.append("where fd.fix_gouptype = '92251008' \n");
            sql.append("and fd.fix_value = dt.trans_org) AS trans_org,\n");
            sql.append("dt.wl_no,\n");
            sql.append("dt.wl_date \n");
            sql.append("from tt_sales_assign     w,\n");
            sql.append("tt_sales_ass_detail d,\n");
            sql.append("tt_sales_alloca_de  de,\n");
            sql.append("tt_sales_waybill    wb,\n");
            sql.append("tt_sales_logi       lg,\n");
            sql.append("tt_part_dplan_dtl   dt,\n");
            sql.append("tt_part_dplan_main  dm \n");
            sql.append("where w.ass_id = d.ass_id \n");
            sql.append("and d.ass_detail_id = de.ass_detail_id \n");
            sql.append("and de.bill_id = wb.bill_id \n");
            sql.append("and wb.logi_id = lg.logi_id \n");
            sql.append("and dt.plan_id = dm.plan_id \n");
            sql.append("and dt.ass_no = w.ass_no(+)\n");


            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

}