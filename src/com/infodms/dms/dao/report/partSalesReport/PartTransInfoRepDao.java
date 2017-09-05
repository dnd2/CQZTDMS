package com.infodms.dms.dao.report.partSalesReport;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartTransInfoRepDao extends BaseDao {
    public Logger logger = Logger.getLogger(PartTransInfoRepDao.class);
    private static final PartTransInfoRepDao dao = new PartTransInfoRepDao();

    private PartTransInfoRepDao() {
    }

    public static final PartTransInfoRepDao getInstance() {
        return dao;
    }

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询
     */
    public PageResult<Map<String, Object>> queryRep(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
        String orgName = CommonUtils.checkNull(request.getParamValue("orgName"));
        String transCode = CommonUtils.checkNull(request.getParamValue("TransCode"));
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));
        String transType = CommonUtils.checkNull(request.getParamValue("transType"));
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
        String transorg = CommonUtils.checkNull(request.getParamValue("transorg"));
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));
        String start = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
        String end = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
        String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));

        if ("1".equals(radioSelect)) {
            sql.append("SELECT *\n");
            sql.append("  FROM VW_PART_TRANS_FOLLOW TF\n");
            sql.append(" WHERE 1 = 1\n");
            if (!"".equals(orgCode)) {
                sql.append("   AND TF.DEALER_CODE LIKE '%" + orgCode + "%'\n");
            }
            if (!"".equals(orgName)) {
                sql.append("   AND TF.DEALER_NAME LIKE '%" + orgName + "%'\n");
            }
            if (!"".equals(start)) {
                sql.append("   AND TRUNC(TF.CREATE_DATE) >= TO_DATE('" + start + "', 'yyyy-mm-dd')\n");
            }
            if (!"".equals(end)) {
                sql.append("   AND TRUNC(TF.CREATE_DATE) <= TO_DATE('" + end + "', 'yyyy-mm-dd')\n");
            }
            if (!"".equals(transType)) {
                sql.append("   AND TF.TRANS_TYPE = '" + transType + "'\n");
            }
            if (!"".equals(transorg)) {
                sql.append("   AND TF.TRANSPORT_ORG = '" + transorg + "'\n");
            }
            if (!"".equals(orderType)) {
                sql.append("   AND TF.ORDER_TYPE = '" + orderType + "'\n");
            }
            if (!"".equals(transCode)) {
                sql.append("   AND TF.TRPLAN_CODE like '%" + transCode + "%'");
            }

            sql.append(" ORDER BY TF.CREATE_DATE DESC");
        } else {
            sql.append("SELECT *\n");
            sql.append("  FROM VW_PART_TRANS_FOLLOW_DTL TF\n");
            sql.append(" WHERE 1 = 1\n");
            if (!"".equals(orgCode)) {
                sql.append("   AND TF.DEALER_CODE LIKE '%" + orgCode + "%'\n");
            }
            if (!"".equals(orgName)) {
                sql.append("   AND TF.DEALER_NAME LIKE '%" + orgName + "%'\n");
            }
            if (!"".equals(start)) {
                sql.append("   AND TRUNC(TF.CREATE_DATE) >= TO_DATE('" + start + "', 'yyyy-mm-dd')\n");
            }
            if (!"".equals(end)) {
                sql.append("   AND TRUNC(TF.CREATE_DATE) <= TO_DATE('" + end + "', 'yyyy-mm-dd')\n");
            }
            if (!"".equals(transType)) {
                sql.append("   AND TF.TRANS_TYPE = '" + transType + "'\n");
            }
            if (!"".equals(transorg)) {
                sql.append("   AND TF.TRANSPORT_ORG = '" + transorg + "'\n");
            }
            if (!"".equals(orderType)) {
                sql.append("   AND TF.ORDER_TYPE = '" + orderType + "'\n");
            }
            if (!"".equals(transCode)) {
                sql.append("   AND TF.TRPLAN_CODE like  '%" + transCode + "%'");
            }

        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询
     */
    public List<Map<String, Object>> queryExp(RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
        String orgName = CommonUtils.checkNull(request.getParamValue("orgName"));
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
        String outCode = CommonUtils.checkNull(request.getParamValue("outCode"));
        String transType = CommonUtils.checkNull(request.getParamValue("transType"));
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
        String logisticsNo = CommonUtils.checkNull(request.getParamValue("logisticsNo"));
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));

        sql.append(" select ");
        sql.append(" tpom.create_date as out_date, ");
        sql.append(" tpdom.submit_date, ");
        sql.append(" tpsm.fcaudit_date, ");
        sql.append(" tpwd.wh_code, ");
        sql.append(" tpwd.wh_name, ");
        sql.append(" tpsm.dealer_code, ");
        sql.append(" tpsm.dealer_name, ");
        sql.append(" tpom.amount, ");
        sql.append(" tpsm.so_code, ");
        sql.append(" tpom.out_code, ");
        sql.append(" tu.name as pkgPrintBy, ");
        sql.append(" (select sum(outstock_qty) from tt_part_outstock_dtl where out_id=tpom.out_id) as partQty, ");
        sql.append(" tppom.weight, ");
        sql.append(" tpsm.trans_type, ");
        sql.append(" (select fix_name from tt_part_fixcode_define where fix_gouptype='").append(Constant.FIXCODE_TYPE_04).append("' and fix_value=tpsm.trans_type)  as trans_type_name, ");
        sql.append(" tpsm.pick_order_id, ");
        sql.append(" tppom.logistics_no, ");
        sql.append(" tu.name as pkgBy ,");
        sql.append("tpom.remark ");
        sql.append(" from tt_part_outstock_main tpom ");
        sql.append(" join tt_part_so_main tpsm on tpom.so_id=tpsm.so_id ");
        sql.append(" left join tt_part_dlr_order_main tpdom on tpdom.order_id=tpsm.order_id ");
        sql.append(" left join tt_part_warehouse_define tpwd on tpwd.wh_id=tpsm.wh_id ");
        sql.append(" left join tc_user tu on tu.user_id=tpsm.PKG_BY ");
        sql.append(" left join tt_part_pick_order_main tppom on tppom.pick_order_id=tpsm.pick_order_id ");
        sql.append(" where 1=1 ");
        if (!"".equals(orgCode)) {
            sql.append(" and tpom.dealer_code like '%").append(orgCode).append("%'");
        }
        if (!"".equals(orgName)) {
            sql.append(" and tpom.dealer_name like '%").append(orgName).append("%'");
        }
        if (!"".equals(soCode)) {
            sql.append(" and tpom.so_code like '%").append(soCode).append("%'");
        }
        if (!"".equals(outCode)) {
            sql.append(" and tpom.out_code like '%").append(outCode).append("%'");
        }
        if (!"".equals(transType)) {
            sql.append(" and tpom.trans_type ='").append(transType).append("'");
        }
        if (!"".equals(pickOrderId)) {
            sql.append(" and tpsm.pick_Order_Id like '%").append(pickOrderId).append("%'");
        }
        if (!"".equals(logisticsNo)) {
            sql.append(" and tpsm.pick_order_id in (select pick_order_id from tt_part_pick_order_main where logisticsNo like '%").append(logisticsNo).append("%' )");
        }
        if (!"".equals(whId)) {
            sql.append(" and tpom.wh_id ='").append(whId).append("'");
        }
        List<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, this.getFunName());
        return ps;
    }

    /**
     * 查询物流信息
     *
     * @param request
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> queryogisticsrep(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String trplanCode = CommonUtils.checkNull(request.getParamValue("trplanCode"));
        if (!"".equals(trplanCode) && null != trplanCode) {
            sql.append("SELECT * FROM TT_PART_LOGISTICS_INFO LI WHERE LI.LOGISTICS_NO LIKE '%" + trplanCode + "%'\n");
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }
}
