package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartPkgRePrintDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartPkgRePrintDao.class);
    private static final PartPkgRePrintDao dao = new PartPkgRePrintDao();

    private PartPkgRePrintDao() {
    }

    public static final PartPkgRePrintDao getInstance() {
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
    public PageResult<Map<String, Object>> queryRePrint(RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String loginDealerId = "";
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);

        if (null != beanList || beanList.size() >= 0) {
            loginDealerId = beanList.get(0).getOrgId() + "";
        }
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
        String dealercode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
        String pkgNo = CommonUtils.checkNull(request.getParamValue("pkgNo"));
        StringBuffer sql = new StringBuffer();

        sql.append("select a.trans_code, --发运单号\n" );
        sql.append("a.trans_id,\n" );
        sql.append("c.Consignees_Id,\n" );
        sql.append("       (select dealer_code from tm_dealer where dealer_id = c.Consignees_Id) CONSIGNEES_code,\n" );
        sql.append("       c.CONSIGNEES,\n" );
        sql.append("       C.ADDR_ID,\n" );
        sql.append("       c.addr,\n" );
        sql.append("       c.out_code, --出库单号\n" );
        sql.append("       e.pkg_no,\n" );
        sql.append("       a.trans_type trans_type1,\n" );//这个字段没用
        sql.append("       (select tv.tv_name from tt_transport_valuation tv where a.trans_type=tv.tv_id )  trans_type,\n" );
        sql.append("       '', --配合日期\n" );
        sql.append("       (select wh_name from tt_part_warehouse_define where wh_id = c.wh_id) wh_name\n" );
        sql.append("  from tt_part_trans a, tt_part_outstock_main c, tt_part_pkg_box_dtl e\n" );
        sql.append(" where a.out_id = c.out_id\n" );
        sql.append("   and e.out_id = c.out_id\n" );
        sql.append("");

        if (!"".equals(dealerName)) {
            sql.append(" AND c.CONSIGNEES LIKE '%").append(dealerName).append("%'");
        }

        if (!"".equals(dealercode)) {
            sql.append(" AND (select dealer_code from tm_dealer where dealer_id = c.Consignees_Id) LIKE '%").append(dealercode).append("%'");
        }
        if (!"".equals(pkgNo)) {
            sql.append(" AND e.pkg_no LIKE '%").append(pkgNo).append("%'");
        }
        if (!loginDealerId.equals(Constant.OEM_ACTIVITIES + "")) {
            sql.append(" AND EXISTS (SELECT 1\n");
            sql.append("       FROM TT_PART_SALES_RELATION R\n");
            sql.append("      WHERE R.CHILDORG_ID = T1.DEALER_ID\n");
            sql.append("        AND R.PARENTORG_ID = '" + loginDealerId + "')\n");
        }
        sql.append(" order by a.create_date desc ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2014-3-4
     * @Title : 获取发运标签打印记录信息
     */
    public List<Map<String, Object>> getPrintNum(String year, String moth, String orgId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT RD.* FROM TT_PART_OUTMARK_RECORD RD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND RD.RCD_YEAR = '" + year + "' ");
        sql.append(" AND RD.RCD_MONTH = '" + moth + "' ");
        sql.append(" AND RD.ORG_ID = '" + orgId + "' ");

        list = this.pageQuery(sql.toString(), null, this.getFunName());

        if (null == list || list.size() < 1) {
            initOutmarkRecord(year, moth, orgId);
        }

        return list;
    }

    /**
     * @param string
     * @param :      @param sqlStr
     * @param :      @return
     * @return :
     * @throws : LastDate    : 2014-3-4
     * @Title : 获取发运标签打印记录信息
     */
    public List<Map<String, Object>> getDealer(String dealerCode) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT * FROM TM_DEALER d WHERE d.dealer_type=10771002 AND d.dealer_level=10851001 AND d.dealer_code='");
        sql.append(dealerCode).append("'");
        list = this.pageQuery(sql.toString(), null, this.getFunName());

        return list;
    }
    public List<Map<String, Object>> getPaerRep(RequestWrapper request) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();

        String addrId = CommonUtils.checkNull(request.getParamValue("addrId"));
        String pkgNo = CommonUtils.checkNull(request.getParamValue("pkgNo"));
        
        sql.append("select a.trans_code, --发运单号\n" );
        sql.append(" a.trans_id,\n" );
        sql.append(" c.Consignees_Id,\n" );
        sql.append(" (select dealer_code from tm_dealer where dealer_id = c.Consignees_Id) CONSIGNEES_code,\n" );
        sql.append(" c.CONSIGNEES,\n" );
        sql.append(" C.ADDR_ID,\n" );
        sql.append(" c.addr,\n" );
        sql.append(" c.out_code, --出库单号\n" );
        sql.append(" e.pkg_no,\n" );
//        sql.append("       a.trans_type,\n" );
        sql.append(" (select tv.tv_name from tt_transport_valuation tv where a.trans_type=tv.tv_id ) trans_type,\n" );
        sql.append(" '', --配合日期\n" );
        sql.append(" (select wh_name from tt_part_warehouse_define where wh_id = c.wh_id) wh_name\n" );
        sql.append(" from tt_part_trans a, tt_part_outstock_main c, tt_part_pkg_box_dtl e\n" );
        sql.append(" where a.out_id = c.out_id\n" );
        sql.append(" and e.out_id = c.out_id\n" );

        if (!"".equals(addrId)) {
            sql.append(" and e.pkg_no = '"+addrId+"'");
        }
        if (!"".equals(pkgNo)) {
            sql.append(" and e.pkg_no = '"+pkgNo+"'");
        }

        sql.append(" order by a.create_date desc " );
        list = this.pageQuery(sql.toString(), null, this.getFunName());

        return list;
    }
   
    /**
     * @param : @param year
     * @param : @param moth
     * @param : @param orgId
     * @return :
     * @throws : LastDate    : 2014-3-4
     * @Title : 初始化发运标签数据
     */
    private void initOutmarkRecord(String year, String moth, String orgId) {
        Long rcdId = Long.parseLong(SequenceManager.getSequence(""));
        int yearNum = Integer.parseInt(year);
        int mothNum = Integer.parseInt(moth);
        long orgIdNum = Long.parseLong(orgId);

        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT INTO TT_PART_OUTMARK_RECORD ");
        sql.append(" ( ");
        sql.append(" RCD_ID, ");
        sql.append(" RCD_YEAR, ");
        sql.append(" RCD_MONTH, ");
        sql.append(" RCD_COUNT, ");
        sql.append(" ORG_ID ");
        sql.append(" ) ");
        sql.append(" VALUES ");
        sql.append(" ( ");
        sql.append(rcdId + ", ");
        sql.append(yearNum + ", ");
        sql.append(mothNum + ", ");
        sql.append("0, ");
        sql.append(orgIdNum);
        sql.append(" ) ");

        this.update(sql.toString(), null);
    }

    /**
     * @param : @param year
     * @param : @param moth
     * @param : @param orgId
     * @param : @param printValue
     * @return :
     * @throws : LastDate    : 2014-3-4
     * @Title : 更新发运标签打印记录
     */
    public void updateOutmarkRecord(String year, String moth, String orgId, String printValue) {
        int yearNum = Integer.parseInt(year);
        int mothNum = Integer.parseInt(moth);
        long orgIdNum = Long.parseLong(orgId);
        int printNum = Integer.parseInt(printValue);

        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE TT_PART_OUTMARK_RECORD ");
        sql.append(" SET RCD_COUNT = ");
        sql.append("(RCD_COUNT + " + printNum + ") ");
        sql.append(" WHERE RCD_YEAR = " + yearNum + " ");
        sql.append(" AND RCD_MONTH = " + mothNum + " ");
        sql.append(" AND ORG_ID = " + orgIdNum + " ");

        this.update(sql.toString(), null);
    }

    /**
     * 查询服务商对应的有效装箱号
     *
     * @param request
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> queryDlrPkgNo(RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String orgId = "";
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);

        if (null != beanList || beanList.size() >= 0) {
            orgId = beanList.get(0).getOrgId() + "";
        }
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
        String dealercode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
        String pkgNo = CommonUtils.checkNull(request.getParamValue("pkgNo"));
        String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT TD.DEALER_CODE, TD.DEALER_NAME, PD.PKG_NO, PD.CREATE_DATE, TU.NAME\n");
        sql.append("  FROM TT_PART_DLR_PKGNO PD, TM_DEALER TD, TC_USER TU\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND PD.DEALER_ID = TD.DEALER_ID\n");
        sql.append("   AND PD.CREATE_BY = TU.USER_ID(+)\n");
        sql.append("   AND pd.org_id=" + orgId + "\n");
        sql.append("   AND NOT EXISTS (SELECT 1\n");
        sql.append("          FROM TT_PART_PKG_BOX_DTL BD\n");
        sql.append("         WHERE BD.PKG_NO = PD.PKG_NO\n");
        sql.append("           AND BD.OUT_ID IS NOT NULL)");

        if (!"".equals(dealerId) && null != dealerId) {
            sql.append(" AND TD.DEALER_ID  = '").append(dealerId).append("'");
        }
        if (!"".equals(dealerName) && null != dealerName) {
            sql.append(" AND TD.DEALER_NAME  LIKE '%").append(dealerName).append("%'");
        }

        if (!"".equals(dealercode) && null != dealercode) {
            sql.append(" AND TD.DEALER_CODE LIKE '%").append(dealercode.toUpperCase()).append("%'");
        }
        if (!"".equals(pkgNo) && null != pkgNo) {
            sql.append(" AND PD.PKG_NO LIKE '%").append(pkgNo.toUpperCase()).append("%'");
        }
        sql.append(" ORDER BY　PD.CREATE_DATE\n");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }
}
