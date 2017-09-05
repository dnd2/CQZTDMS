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
public class PartOderGxQueryDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartOderGxQueryDao.class);
    private static final PartOderGxQueryDao dao = new PartOderGxQueryDao();

    private PartOderGxQueryDao() {

    }

    public static final PartOderGxQueryDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> query(RequestWrapper request, int curPage, int pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {

            String DEALER_CODE = CommonUtils.checkNull(request.getParamValue("dealerCode")).toUpperCase();//服务站号
            String DEALER_NAME = CommonUtils.checkNull(request.getParamValue("dealerName"));//服务站名称
            String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("partOldCode")).toUpperCase();//配件编码
            String PART_CNAME = CommonUtils.checkNull(request.getParamValue("partCnaem"));//配件名称
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));//发运日期段起始日期
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));//发运日期段结束日期


            StringBuffer sql = new StringBuffer("");

            sql.append("select \n");
            sql.append("	Aa.Dealer_Id, \n");
            sql.append("	Aa.Dealer_Code, \n");
            sql.append("	Aa.Dealer_Name, \n");
            sql.append("	aa.part_id, \n");
            sql.append("	aa.part_oldcode, \n");
            sql.append("	aa.part_cname, \n");
            sql.append("	sum(aa.buy_qty) buy_qty, \n");
            sql.append("	sum(aa.trans_QTy) trans_QTy, \n");
            sql.append("	sum(aa.report_qty) report_qty \n");
            sql.append("from (select A.Dealer_Id, \n");
            sql.append("		A.Dealer_Code, \n");
            sql.append("		A.Dealer_Name, \n");
            sql.append("		b.part_id, \n");
            sql.append("		b.part_oldcode, \n");
            sql.append("		b.part_cname, \n");
            sql.append(" 		sum(b.buy_qty) buy_qty, \n");
            sql.append(" 		0 trans_QTy, \n");
            sql.append(" 		0 report_qty \n");
            sql.append(" 	  from tt_part_dlr_order_main a, \n");
            sql.append("		tt_part_dlr_order_dtl b \n");
            sql.append("	  where 1 = 1 \n");
            sql.append("		and a.order_id = b.order_id \n");
            sql.append("		and A.order_type = 92151007 \n");
            sql.append(" 		and a.state in (92161002, 92161003) \n");


            if (!"".equals(SCREATE_DATE)) {
                sql.append(" and A.SUBMIT_DATE>= to_date('").append(SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append(" and A.SUBMIT_DATE<= to_date('").append(ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
            }

            if (!"".equals(DEALER_CODE)) {
                if (DEALER_CODE != null) {
                    if (!(DEALER_CODE.split(",").length <= 0)) {
                        sql.append(" and a.dealer_code in(");
                        String[] paramStrArr = DEALER_CODE.split(",");
                        for (int i = 0; i < paramStrArr.length; i++) {
                            String tempCode = paramStrArr[i];
                            if (i == paramStrArr.length - 1) {
                                sql.append("'" + tempCode + "')");
                            } else {
                                sql.append("'" + tempCode + "', ");
                            }
                        }
                    }
                }


            }
            if (!"".equals(DEALER_NAME)) {
                sql.append(" and a.dealer_name like '%").append(DEALER_NAME).append("%'");
            }
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" and b.part_oldcode like '%").append(PART_OLDCODE).append("%'");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" and b.part_cname like '%").append(PART_CNAME).append("%'");
            }


            sql.append("	  group by A.Dealer_Id, \n");
            sql.append("		A.Dealer_Code, \n");
            sql.append(" 		A.Dealer_Name, \n");
            sql.append(" 		b.part_id, \n");
            sql.append(" 		b.part_oldcode, \n");
            sql.append(" 		b.part_cname \n");
            sql.append(" 	  union \n");
            sql.append("	  select A.Dealer_Id, \n");
            sql.append("		A.Dealer_Code, \n");
            sql.append("		A.Dealer_Name, \n");
            sql.append("		b.part_id, \n");
            sql.append("		b.part_oldcode, \n");
            sql.append("		b.part_cname, \n");
            sql.append("		0 buy_qty, \n");
            sql.append("		sum(decode(a.order_id, null, 0, b.report_qty)) trans_QTy, \n");
            sql.append("		sum(decode(a.order_id, null, b.report_qty, 0)) report_qty \n");
            sql.append("	  from tt_part_dplan_main a, \n");
            sql.append("		tt_part_dplan_dtl b \n");
            sql.append("	  where a.plan_id = b.plan_id \n");

            if (!"".equals(SCREATE_DATE)) {
                sql.append(" and a.create_date>= to_date('").append(SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append(" and a.create_date<= to_date('").append(ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
            }

            if (!"".equals(DEALER_CODE)) {
                if (DEALER_CODE != null) {
                    if (!(DEALER_CODE.split(",").length <= 0)) {
                        sql.append(" and a.dealer_code in(");
                        String[] paramStrArr = DEALER_CODE.split(",");
                        for (int i = 0; i < paramStrArr.length; i++) {
                            String tempCode = paramStrArr[i];
                            if (i == paramStrArr.length - 1) {
                                sql.append("'" + tempCode + "')");
                            } else {
                                sql.append("'" + tempCode + "', ");
                            }
                        }
                    }
                }
            }
            if (!"".equals(DEALER_NAME)) {
                sql.append(" and a.dealer_name like '%").append(DEALER_NAME).append("%'");
            }
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" and b.part_oldcode like '%").append(PART_OLDCODE).append("%'");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" and b.part_cname like '%").append(PART_CNAME).append("%'");
            }


            sql.append("	  group by A.Dealer_Id, \n");
            sql.append("		A.Dealer_Code, \n");
            sql.append("		A.Dealer_Name, \n");
            sql.append("		b.part_id, \n");
            sql.append("		b.part_oldcode, \n");
            sql.append("		b.part_cname) aa \n");
            sql.append(" group by Aa.Dealer_Id, \n");
            sql.append(" 	Aa.Dealer_Code, \n");
            sql.append(" 	Aa.Dealer_Name, \n");
            sql.append(" 	aa.part_id, \n");
            sql.append(" 	aa.part_oldcode, \n");
            sql.append(" 	aa.part_cname  \n");


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
            String DEALER_CODE = CommonUtils.checkNull(request.getParamValue("dealerCode")).toUpperCase();//服务站号
            String DEALER_NAME = CommonUtils.checkNull(request.getParamValue("dealerName"));//服务站名称
            String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("partOldCode")).toUpperCase();//配件编码
            String PART_CNAME = CommonUtils.checkNull(request.getParamValue("partCnaem"));//配件名称
            String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));//发运日期段起始日期
            String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));//发运日期段结束日期


            StringBuffer sql = new StringBuffer("");

            sql.append("select \n");
            sql.append("	Aa.Dealer_Id, \n");
            sql.append("	Aa.Dealer_Code, \n");
            sql.append("	Aa.Dealer_Name, \n");
            sql.append("	aa.part_id, \n");
            sql.append("	aa.part_oldcode, \n");
            sql.append("	aa.part_cname, \n");
            sql.append("	sum(aa.buy_qty) buy_qty, \n");
            sql.append("	sum(aa.trans_QTy) trans_QTy, \n");
            sql.append("	sum(aa.report_qty) report_qty \n");
            sql.append("from (select A.Dealer_Id, \n");
            sql.append("		A.Dealer_Code, \n");
            sql.append("		A.Dealer_Name, \n");
            sql.append("		b.part_id, \n");
            sql.append("		b.part_oldcode, \n");
            sql.append("		b.part_cname, \n");
            sql.append(" 		sum(b.buy_qty) buy_qty, \n");
            sql.append(" 		0 trans_QTy, \n");
            sql.append(" 		0 report_qty \n");
            sql.append(" 	  from tt_part_dlr_order_main a, \n");
            sql.append("		tt_part_dlr_order_dtl b \n");
            sql.append("	  where 1 = 1 \n");
            sql.append("		and a.order_id = b.order_id \n");
            sql.append("		and A.order_type = 92151007 \n");
            sql.append(" 		and a.state in (92161002, 92161003) \n");


            if (!"".equals(SCREATE_DATE)) {
                sql.append(" and A.SUBMIT_DATE>= to_date('").append(SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            }
            if (!"".equals(ECREATE_DATE)) {
                sql.append(" and A.SUBMIT_DATE<= to_date('").append(ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
            }

            if (!"".equals(DEALER_CODE)) {
                if (DEALER_CODE != null) {
                    if (!(DEALER_CODE.split(",").length <= 0)) {
                        sql.append(" and a.dealer_code in(");
                        String[] paramStrArr = DEALER_CODE.split(",");
                        for (int i = 0; i < paramStrArr.length; i++) {
                            String tempCode = paramStrArr[i];
                            if (i == paramStrArr.length - 1) {
                                sql.append("'" + tempCode + "')");
                            } else {
                                sql.append("'" + tempCode + "', ");
                            }
                        }
                    }
                }


            }
            if (!"".equals(DEALER_NAME)) {
                sql.append(" and a.dealer_name like '%").append(DEALER_NAME).append("%'");
            }
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" and b.part_oldcode like '%").append(PART_OLDCODE).append("%'");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" and b.part_cname like '%").append(PART_CNAME).append("%'");
            }


            sql.append("	  group by A.Dealer_Id, \n");
            sql.append("		A.Dealer_Code, \n");
            sql.append(" 		A.Dealer_Name, \n");
            sql.append(" 		b.part_id, \n");
            sql.append(" 		b.part_oldcode, \n");
            sql.append(" 		b.part_cname \n");
            sql.append(" 	  union \n");
            sql.append("	  select A.Dealer_Id, \n");
            sql.append("		A.Dealer_Code, \n");
            sql.append("		A.Dealer_Name, \n");
            sql.append("		b.part_id, \n");
            sql.append("		b.part_oldcode, \n");
            sql.append("		b.part_cname, \n");
            sql.append("		0 buy_qty, \n");
            sql.append("		sum(decode(a.order_id, null, 0, b.report_qty)) trans_QTy, \n");
            sql.append("		sum(decode(a.order_id, null, b.report_qty, 0)) report_qty \n");
            sql.append("	  from tt_part_dplan_main a, \n");
            sql.append("		tt_part_dplan_dtl b \n");
            sql.append("	  where a.plan_id = b.plan_id \n");


//            if(!"".equals(SCREATE_DATE)){
//    			sql.append(" and A.SUBMIT_DATE>= to_date('").append(SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
//    		}
//            if(!"".equals(ECREATE_DATE)){
//    			sql.append(" and A.SUBMIT_DATE<= to_date('").append(ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
//    		}
            if (!"".equals(DEALER_CODE)) {
                if (DEALER_CODE != null) {
                    if (!(DEALER_CODE.split(",").length <= 0)) {
                        sql.append(" and a.dealer_code in(");
                        String[] paramStrArr = DEALER_CODE.split(",");
                        for (int i = 0; i < paramStrArr.length; i++) {
                            String tempCode = paramStrArr[i];
                            if (i == paramStrArr.length - 1) {
                                sql.append("'" + tempCode + "')");
                            } else {
                                sql.append("'" + tempCode + "', ");
                            }
                        }
                    }
                }


            }
            if (!"".equals(DEALER_NAME)) {
                sql.append(" and a.dealer_name like '%").append(DEALER_NAME).append("%'");
            }
            if (!"".equals(PART_OLDCODE)) {
                sql.append(" and b.part_oldcode like '%").append(PART_OLDCODE).append("%'");
            }
            if (!"".equals(PART_CNAME)) {
                sql.append(" and b.part_cname like '%").append(PART_CNAME).append("%'");
            }


            sql.append("	  group by A.Dealer_Id, \n");
            sql.append("		A.Dealer_Code, \n");
            sql.append("		A.Dealer_Name, \n");
            sql.append("		b.part_id, \n");
            sql.append("		b.part_oldcode, \n");
            sql.append("		b.part_cname) aa \n");
            sql.append(" group by Aa.Dealer_Id, \n");
            sql.append(" 	Aa.Dealer_Code, \n");
            sql.append(" 	Aa.Dealer_Name, \n");
            sql.append(" 	aa.part_id, \n");
            sql.append(" 	aa.part_oldcode, \n");
            sql.append(" 	aa.part_cname  \n");


            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

}