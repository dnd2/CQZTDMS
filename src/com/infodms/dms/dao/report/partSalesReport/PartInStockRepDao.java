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

public class PartInStockRepDao extends BaseDao {
    public Logger logger = Logger.getLogger(PartCheckAnalysisRepDao.class);
    private static final PartInStockRepDao dao = new PartInStockRepDao();

    private PartInStockRepDao() {
    }

    public static final PartInStockRepDao getInstance() {
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
        sql.append("SELECT *    ");

        sql.append("  FROM VW_PART_DLR_INSTOCK_DTL OT where 1=1");

        if (!"".equals(orgCode)) {
            sql.append(" and UPPER(DEALER_CODE) like '%").append(orgCode.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(org)) {
            sql.append(" and DEALER_NAME like '%").append(org.trim()).append("%'");
        }
        if (!"".equals(start)) {
            sql.append(" AND TO_CHAR(CREATE_DATE,'yyyy-MM-dd') >= '" + start + "' ");
        }
        if (!"".equals(end)) {
            sql.append(" AND TO_CHAR(CREATE_DATE,'yyyy-MM-dd') <= '" + end + "' ");
        }
        if (!"".equals(partCode)) {
            sql.append(" and UPPER(part_Code) like '%").append(partCode.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(partCName)) {
            sql.append(" and part_CName like '%").append(partCName.trim()).append("%'");
        }
        if (!"".equals(partOldCode)) {
            sql.append(" and UPPER(PART_OLDCODE) like '%").append(partOldCode.trim().toUpperCase()).append("%'");
        }

        if (oemFlag.equals(Constant.IF_TYPE_YES + "")) {
            sql.append("AND dealer_ID in (select childorg_id from tt_part_sales_relation where parentorg_id in('0','").append(Constant.OEM_ACTIVITIES).append("'))");
        } else {
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            String dealerId = "";
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            sql.append("AND dealer_ID ='").append(dealerId).append("'");
        }

        //add 大区服务经理区域限制 01-01-15
        if (this.getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
            sql.append(CommonUtils.getOrgDealerLimitSqlByPose("OT", loginUser));
        }
        sql.append("ORDER BY CREATE_DATE DESC");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @param loginUser
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-11-26
     * @Title : 供应中心入库查询
     */
    public PageResult<Map<String, Object>> queryGRep(RequestWrapper request, int curPage, int pageSize, AclUserBean loginUser) {
        StringBuffer sql = new StringBuffer();
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
        String start = CommonUtils.checkNull(request.getParamValue("checkSDate"));
        String end = CommonUtils.checkNull(request.getParamValue("checkEDate"));
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
        String partCName = CommonUtils.checkNull(request.getParamValue("partCname"));
        String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldcode"));
        sql.append("SELECT *    ");

        sql.append("  FROM VW_PART_DLR_INSTOCK_DTL where 1=1");

        if (!"".equals(dealerCode)) {
            sql.append(" and UPPER(DEALER_CODE) like '%").append(dealerCode.trim().toUpperCase()).append("%'");
            sql.append(" AND DEALER_CODE LIKE 'G%' ");
        } else {
            sql.append(" AND DEALER_CODE LIKE 'G%' ");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and DEALER_NAME like '%").append(dealerName.trim()).append("%'");
        }
        if (!"".equals(start)) {
            sql.append(" AND TO_CHAR(CREATE_DATE,'yyyy-MM-dd') >= '" + start + "' ");
        }
        if (!"".equals(end)) {
            sql.append(" AND TO_CHAR(CREATE_DATE,'yyyy-MM-dd') <= '" + end + "' ");
        }
        if (!"".equals(partCode)) {
            sql.append(" and UPPER(part_Code) like '%").append(partCode.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(partCName)) {
            sql.append(" and part_CName like '%").append(partCName.trim()).append("%'");
        }
        if (!"".equals(partOldCode)) {
            sql.append(" and UPPER(PART_OLDCODE) like '%").append(partOldCode.trim().toUpperCase()).append("%'");
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
        sql.append("SELECT *    ");

        sql.append("  FROM VW_PART_DLR_INSTOCK_DTL where 1=1");

        if (!"".equals(orgCode)) {
            sql.append(" and UPPER(DEALER_CODE) like '%").append(orgCode.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(org)) {
            sql.append(" and DEALER_NAME like '%").append(org.trim()).append("%'");
        }
        if (!"".equals(start)) {
            sql.append(" and CREATE_DATE >= to_date('").append(start).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(end)) {
            sql.append(" and CREATE_DATE <= to_date('").append(end).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(partCode)) {
            sql.append(" and UPPER(part_Code) like '%").append(partCode.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(partCName)) {
            sql.append(" and part_CName like '%").append(partCName.trim()).append("%'");
        }
        if (!"".equals(partOldCode)) {
            sql.append(" and UPPER(PART_OLDCODE) like '%").append(partOldCode.trim().toUpperCase()).append("%'");
        }

        if (oemFlag.equals(Constant.IF_TYPE_YES + "")) {
            sql.append("AND dealer_ID in (select childorg_id from tt_part_sales_relation where parentorg_id in('0','").append(Constant.OEM_ACTIVITIES).append("'))");
        } else {
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            String dealerId = "";
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            sql.append("AND dealer_ID ='").append(dealerId).append("'");
        }
        List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
        return ps;
    }

}
