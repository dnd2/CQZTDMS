package com.infodms.dms.dao.report.partSalesReport;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartMiscSaleRepDao extends BaseDao {
    public Logger logger = Logger.getLogger(PartMiscSaleRepDao.class);
    private static final PartMiscSaleRepDao dao = new PartMiscSaleRepDao();

    private PartMiscSaleRepDao() {
    }

    public static final PartMiscSaleRepDao getInstance() {
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
    public PageResult<Map<String, Object>> queryRep(RequestWrapper request, int curPage, int pageSize, AclUserBean loginUser) {
        StringBuffer sql = new StringBuffer();
        String oemFlag = CommonUtils.checkNull(request.getParamValue("oemFlag"));
        String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
        String org = CommonUtils.checkNull(request.getParamValue("org"));
        String start = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
        String end = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
        String partCName = CommonUtils.checkNull(request.getParamValue("partCName"));
        String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldCode"));
        sql.append(" SELECT RM.RETAIL_CODE,                                                       ");
        sql.append("        TD.DEALER_CODE,                                                       ");
        sql.append("        TD.DEALER_NAME,                                                       ");
        sql.append(" (select code_desc from tc_code where code_id=RM.CHG_TYPE) IN_TYPE ,");
        sql.append("        RM.LINKMAN,                                                           ");
        sql.append("        RM.PURPOSE,                                                           ");
        sql.append("        RM.REMARK,                                                            ");
        sql.append("        TPD.PART_OLDCODE,                                                     ");
        sql.append("        TPD.PART_CNAME,                                                       ");
        sql.append("        TPD.PART_CODE,                                                        ");
        sql.append("        TC.CODE_DESC PART_TYPE,                                               ");
        sql.append("        TPD.UNIT,                                                             ");
        sql.append("        RD.OUT_QTY,                                                           ");
        sql.append("        RD.SALE_PRICE,                                                        ");
        sql.append("        RD.SALE_AMOUNT,                                                       ");
        sql.append("        RM.CREATE_DATE                                                        ");
        sql.append("   FROM TT_PART_RETAIL_DTL  RD,                                               ");
        sql.append("        TT_PART_RETAIL_MAIN RM,                                               ");
        sql.append("        TM_DEALER           TD,                                               ");
        sql.append("        TT_PART_DEFINE      TPD,                                              ");
        sql.append("        TC_CODE             TC                                                ");
        sql.append("  WHERE RD.RETAL_ID = RM.RETAIL_ID                                            ");
        sql.append("    AND RM.SORG_ID = TD.DEALER_ID                                             ");
        sql.append("    AND RD.PART_ID = TPD.PART_ID                                              ");
        sql.append("    AND TPD.PART_TYPE = TC.CODE_ID(+)                                            ");
        sql.append("    AND RM.CHG_TYPE = '").append(Constant.PART_SALE_STOCK_REMOVAL_TYPE_01).append("'");
        sql.append("    AND RM.STATE = '").append(Constant.PART_RESALE_RECEIVE_ORDER_TYPE_03).append("'");
        sql.append("    AND RM.STATUS = 1                                                         ");
        sql.append("    AND RD.STATUS = 1                                                         ");

        if (!"".equals(orgCode)) {
            sql.append(" and UPPER(TD.DEALER_CODE) like '%").append(orgCode.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(org)) {
            sql.append(" and TD.DEALER_NAME like '%").append(org.trim()).append("%'");
        }
        if (!"".equals(start)) {
            sql.append(" and RD.CREATE_DATE >= to_date('").append(start).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(end)) {
            sql.append(" and RD.CREATE_DATE <= to_date('").append(end).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(partCode)) {
            sql.append(" and UPPER(TPD.part_Code) like '%").append(partCode.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(partCName)) {
            sql.append(" and TPD.part_CName like '%").append(partCName.trim()).append("%'");
        }
        if (!"".equals(partOldCode)) {
            sql.append(" and UPPER(TPD.PART_OLDCODE) like '%").append(partOldCode.trim().toUpperCase()).append("%'");
        }

        if (oemFlag.equals(Constant.IF_TYPE_YES + "")) {
            sql.append("AND RM.SORG_ID in (select childorg_id from tt_part_sales_relation where parentorg_id in('0','").append(Constant.OEM_ACTIVITIES).append("'))");
        } else {
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            String dealerId = "";
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            sql.append("AND RM.SORG_ID ='").append(dealerId).append("'");
        }
        //add 大区服务经理区域限制 01-01-15
        if (this.getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
            sql.append(CommonUtils.getOrgDealerLimitSqlByPose("TD", loginUser));
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
    public List<Map<String, Object>> export(RequestWrapper request, AclUserBean loginUser) {
        StringBuffer sql = new StringBuffer();
        String oemFlag = CommonUtils.checkNull(request.getParamValue("oemFlag"));
        String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
        String org = CommonUtils.checkNull(request.getParamValue("org"));
        String start = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
        String end = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
        String partCName = CommonUtils.checkNull(request.getParamValue("partCName"));
        String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldCode"));
        sql.append(" SELECT RM.RETAIL_CODE,                                                       ");
        sql.append("        TD.DEALER_CODE,                                                       ");
        sql.append("        TD.DEALER_NAME,                                                       ");
        sql.append(" (select code_desc from tc_code where code_id=RM.CHG_TYPE) IN_TYPE ,");
        sql.append("        RM.LINKMAN,                                                           ");
        sql.append("        RM.PURPOSE,                                                           ");
        sql.append("        RM.REMARK,                                                            ");
        sql.append("        TPD.PART_OLDCODE,                                                     ");
        sql.append("        TPD.PART_CNAME,                                                       ");
        sql.append("        TPD.PART_CODE,                                                        ");
        sql.append("        TC.CODE_DESC PART_TYPE,                                               ");
        sql.append("        TPD.UNIT,                                                             ");
        sql.append("        RD.OUT_QTY,                                                           ");
        sql.append("        RD.SALE_PRICE,                                                        ");
        sql.append("        RD.SALE_AMOUNT,                                                       ");
        sql.append("        RM.CREATE_DATE                                                        ");
        sql.append("   FROM TT_PART_RETAIL_DTL  RD,                                               ");
        sql.append("        TT_PART_RETAIL_MAIN RM,                                               ");
        sql.append("        TM_DEALER           TD,                                               ");
        sql.append("        TT_PART_DEFINE      TPD,                                              ");
        sql.append("        TC_CODE             TC                                                ");
        sql.append("  WHERE RD.RETAL_ID = RM.RETAIL_ID                                            ");
        sql.append("    AND RM.SORG_ID = TD.DEALER_ID                                             ");
        sql.append("    AND RD.PART_ID = TPD.PART_ID                                              ");
        sql.append("    AND TPD.PART_TYPE = TC.CODE_ID(+)                                            ");
        sql.append("    AND RM.CHG_TYPE = '").append(Constant.PART_SALE_STOCK_REMOVAL_TYPE_01).append("'");
        sql.append("    AND RM.STATE = '").append(Constant.PART_RESALE_RECEIVE_ORDER_TYPE_03).append("'");

        sql.append("    AND RM.STATUS = 1                                                         ");
        sql.append("    AND RD.STATUS = 1                                                         ");

        if (!"".equals(orgCode)) {
            sql.append(" and UPPER(TD.DEALER_CODE) like '%").append(orgCode.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(org)) {
            sql.append(" and TD.DEALER_NAME like '%").append(org.trim()).append("%'");
        }
        if (!"".equals(start)) {
            sql.append(" and RD.CREATE_DATE >= to_date('").append(start).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(end)) {
            sql.append(" and RD.CREATE_DATE <= to_date('").append(end).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(partCode)) {
            sql.append(" and UPPER(TPD.part_Code) like '%").append(partCode.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(partCName)) {
            sql.append(" and TPD.part_CName like '%").append(partCName.trim()).append("%'");
        }
        if (!"".equals(partOldCode)) {
            sql.append(" and UPPER(TPD.PART_OLDCODE) like '%").append(partOldCode.trim().toUpperCase()).append("%'");
        }

        if (oemFlag.equals(Constant.IF_TYPE_YES + "")) {
            sql.append("AND RM.SORG_ID in (select childorg_id from tt_part_sales_relation where parentorg_id in('0','").append(Constant.OEM_ACTIVITIES).append("'))");
        } else {
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            String dealerId = "";
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            sql.append("AND RM.SORG_ID ='").append(dealerId).append("'");
        }

        //add 大区服务经理区域限制 01-01-15
        if (getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
            sql.append(CommonUtils.getOrgDealerLimitSqlByPose("TD", loginUser));
        }
        List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
        return ps;
    }

}
