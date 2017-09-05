package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.po.TtPartOutstockMainPO;
import com.infodms.dms.po.TtPartPkgBoxDtlPO;
import com.infodms.dms.po.TtPartRecordPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartOutstockDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PartOutstockDao.class);
    private static final PartOutstockDao dao = new PartOutstockDao();

    private PartOutstockDao() {
    }

    public static final PartOutstockDao getInstance() {
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

    public PageResult<Map<String, Object>> queryPkgOrder(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String orgId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            if ((beanList.get(0).getOrgId() + "").equals(Constant.OEM_ACTIVITIES)) {
                orgId = Constant.OEM_ACTIVITIES;
            } else {
                orgId = loginUser.getDealerId();
            }
        }
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));//单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//单号
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//单号
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));//单号
        String startDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//开始
        String endDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//结束
        String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE"));//单号
        String trplanCode = CommonUtils.checkNull(request.getParamValue("trplanCode"));//单号

        String pkgState = CommonUtils.checkNull(request.getParamValue("pkgState"));//是否出库状态  //add zhumingwei 2013-09-17

        //modify by yuan 20130920 start
       /* sql.append("SELECT SM.PICK_ORDER_ID,\n");
        sql.append("       SM.DEALER_CODE,\n");
        sql.append("       SM.DEALER_ID,\n");
        sql.append("       SM.DEALER_NAME,\n");
        sql.append("       SM.WH_ID,\n");
        sql.append("       SM.TRANS_TYPE,\n");
        sql.append("       SM.PICK_ORDER_CREATE_DATE,\n");
        sql.append("       SM.PICK_ORDER_CREATE_BY,\n");
        sql.append("       SM.STATE,\n");
        sql.append("       (SELECT W.WH_NAME\n");
        sql.append("          FROM TT_PART_WAREHOUSE_DEFINE W\n");
        sql.append("         WHERE W.WH_ID = SM.WH_ID) WH_NAME,\n");
        sql.append("       (SELECT FIX_NAME\n");
        sql.append("          FROM TT_PART_FIXCODE_DEFINE D\n");
        sql.append("         WHERE D.FIX_VALUE = SM.TRANS_TYPE\n");
        sql.append("           AND D.FIX_GOUPTYPE = 92251004) TRANS_TYPE_NAME,\n");
        sql.append("       (SELECT U.NAME\n");
        sql.append("          FROM TC_USER U\n");
        sql.append("         WHERE U.USER_ID = SM.PICK_ORDER_CREATE_BY) CREATEBYNAME,\n");
        sql.append("       NVL(SUM(AMOUNT), '0') TOTALMONEY,\n");
        //add by yaun 20131024
        sql.append("za_concat(SM.REMARK2) REMARK2\n");
        //end
        sql.append("  FROM TT_PART_SO_MAIN SM\n");
        sql.append(" WHERE (SM.PICK_ORDER_ID IS NOT NULL OR SM.PICK_ORDER_ID <> '')\n");
        //end
        sql.append(" and seller_id='").append(CommonUtils.checkNull(dealerId)).append("'");
        if (!"".equals(pickOrderId)) {
            sql.append(" and PICK_ORDER_ID like '%").append(pickOrderId).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and dealer_name like  '%").append(dealerName).append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and upper(dealer_code) like  upper('%").append(dealerCode).append("%')");
        }
        if (!"".equals(transType)) {
            sql.append(" and trans_Type='").append(transType).append("'");
        }
        if (!"".equals(whId)) {
            sql.append(" and wh_id='").append(whId).append("'");
        }

        //add zhumingwei 2013-09-17
        if (pkgState.equals(Constant.IF_TYPE_YES.toString())) {
            sql.append(" and state in (").append(Constant.CAR_FACTORY_TRANS_STATE_01 + "," + Constant.CAR_FACTORY_TRANS_STATE_02 + "," + Constant.CAR_FACTORY_PKG_STATE_03).append(")");
        } else if (pkgState.equals(Constant.IF_TYPE_NO.toString())) {
            sql.append(" and state in (").append(Constant.CAR_FACTORY_PKG_STATE_02).append(")");
        } else {
            sql.append(" and state in (").append(Constant.CAR_FACTORY_TRANS_STATE_01 + "," + Constant.CAR_FACTORY_TRANS_STATE_02 + "," + Constant.CAR_FACTORY_PKG_STATE_03 + "," + Constant.CAR_FACTORY_PKG_STATE_02).append(")");
        }
        //add zhumingwei 2013-09-17

        if (!"".equals(startDate) && !"".equals(endDate)) {
            sql.append(" and PICK_ORDER_CREATE_DATE>= to_date('").append(startDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and PICK_ORDER_CREATE_DATE<= to_date('").append(endDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        //add yuan 2014-03-23 过滤有未出库箱子的拣货单
        sql.append("AND EXISTS (SELECT 1\n");
        sql.append("        FROM TT_PART_PKG_BOX_DTL BD\n");
        sql.append("       WHERE BD.PICK_ORDER_ID = SM.PICK_ORDER_ID\n");
        sql.append("         AND BD.STATUS = 1)");
        //modify by yuan 20130913 start
        sql.append("GROUP BY SM.PICK_ORDER_ID,\n");
        sql.append("         SM.DEALER_ID,\n");
        sql.append("         SM.DEALER_NAME,\n");
        sql.append("         SM.DEALER_CODE,\n");
        sql.append("         SM.WH_ID,\n");
        sql.append("         SM.TRANS_TYPE,\n");
        sql.append("         SM.PICK_ORDER_CREATE_DATE,\n");
        sql.append("         SM.PICK_ORDER_CREATE_BY,\n");
        sql.append("         SM.STATE\n");
        //end
        sql.append(" order by PICK_ORDER_CREATE_DATE desc");*/

        sql.append("SELECT TP.TRPLAN_CODE,\n");
        sql.append("       TP.PICK_ORDER_ID,\n");
        sql.append("       TP.DEALER_CODE,\n");
        sql.append("       TP.DEALER_NAME,\n");
//        sql.append("       TP.TRANS_TYPE,\n");
//        sql.append("       TP.TRANSPORT_ORG,\n");
        sql.append("   	   (select TV_NAME from TT_TRANSPORT_VALUATION tv where tv.STATUS=10011001 and tv.tv_id=tp.trans_type) TRANS_TYPE_CN,\n");
        sql.append("       (select LOGI_FULL_NAME from TT_SALES_LOGI tv where tv.STATUS=10011001 and tv.LOGI_CODE=tp.transport_org) TRANSPORT_ORG_CN,\n");
        sql.append("       TP.TRANS_TYPE,\n");
        sql.append("       TP.TRANSPORT_ORG,\n");
        sql.append("       TP.TRPLAN_ID,\n");
        sql.append("       (SELECT ZA_CONCAT(DISTINCT BD.PICK_ORDER_ID)\n");
        sql.append("          FROM TT_PART_PKG_BOX_DTL BD\n");
        sql.append("         WHERE BD.TRPLAN_ID = TP.TRPLAN_ID) PICKORDERIDS,\n");
        sql.append("       (SELECT ZA_CONCAT(DISTINCT BD.PKG_NO)\n");
        sql.append("          FROM TT_PART_PKG_BOX_DTL BD\n");
        sql.append("         WHERE BD.TRPLAN_ID = TP.TRPLAN_ID) PKG_NO,\n");
        sql.append("       TP.CREATE_DATE,\n");
        sql.append("       (SELECT ZA_CONCAT(DISTINCT SM.ORDER_CODE)\n");
        sql.append("          FROM TT_PART_SO_MAIN SM\n");
        sql.append("         WHERE EXISTS (SELECT 1\n");
        sql.append("                  FROM TT_PART_TRANS_PLAN_DTL PD\n");
        sql.append("                 WHERE PD.TRPLAN_ID = TP.TRPLAN_ID\n");
        sql.append("                   AND PD.PICK_ORDER_ID = SM.PICK_ORDER_ID)) ORDER_CODE\n");
        sql.append("  FROM TT_PART_TRANS_PLAN TP");
        sql.append(" WHERE TP.SELLER_ID = " + orgId + "\n");
        sql.append("   AND TP.STATE = 10011001\n");
        sql.append("   AND TP.STATUS = 1\n");
        sql.append("   AND TP.OUT_ID = 0");

        if (!"".equals(pickOrderId)) {
            sql.append("AND EXISTS (SELECT 1\n");
            sql.append("         FROM TT_PART_TRANS_PLAN_DTL PD\n");
            sql.append("        WHERE PD.TRPLAN_ID = TP.TRPLAN_ID\n");
            sql.append("          AND PD.PICK_ORDER_ID LIKE '%" + pickOrderId + "%')");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and TP.dealer_name like  '%").append(dealerName).append("%'");
        }
        if (!"".equals(trplanCode)) {
            sql.append(" and TP.TRPLAN_CODE like  '%").append(trplanCode).append("%'");
        }
        sql.append("  ORDER BY TP.TRPLAN_CODE DESC");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * 获取仓位
     *
     * @param soId
     * @return
     */
    public List<Map<String, Object>> queryPartLocation(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select LOC_NAME,LOC_ID from TT_PART_LOACTION_DEFINE where STATE='");
        sql.append(Constant.STATUS_ENABLE).append("'");
        sql.append(" and PART_ID in (");
        sql.append(" select part_id from TT_PART_SO_DTL ");
        sql.append(" where SO_ID='").append(soId).append("'");
        sql.append(")");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * 获取仓位
     *
     * @param soId
     * @return
     */
    public List<Map<String, Object>> queryPartPkg(String soId) {
        StringBuffer sql = new StringBuffer();
        /*sql.append(" select LOC_NAME,LOC_ID from TT_PART_LOACTION_DEFINE where STATE='");
        sql.append(Constant.STATUS_ENABLE).append("'");
		sql.append(" and PART_ID in (");
		sql.append(" select part_id from TT_PART_SO_DTL ");
		sql.append(" where SO_ID='").append(soId).append("'");
		sql.append(")");*/
        sql.append("SELECT Distinct p.pkg_no AS LOC_NAME,p.pkg_no AS LOC_ID FROM tt_part_pkg_dtl p WHERE p.so_id=" + soId);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * 获取仓位
     *
     * @param soId
     * @return
     */
    public Map<String, Object> queryPartLocationByPartId(String partId, String whId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select LOC_NAME,LOC_ID from TT_PART_LOACTION_DEFINE where STATE='");
        sql.append(Constant.STATUS_ENABLE).append("'");
        sql.append(" and PART_ID ='");
        sql.append(partId);
        sql.append("'");
        sql.append(" and wh_Id='").append(whId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0) {
            return new HashMap();
        }
        return list.get(0);
    }

    /**
     * @param : @param soId
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询详细
     */
    public List<Map<String, Object>> queryPartInfo(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from Tt_Part_So_dtl ");
        sql.append(" where SO_ID='").append(soId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * @param : @param soId
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询装箱
     */
    public List<Map<String, Object>> queryPkg(String soId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select max(pkg_no) as pkg_no from TT_PART_PKG_DTL ");
        sql.append(" where 1=1 ");
        sql.append(" and SO_ID='").append(soId).append("'");
        sql.append(" and PART_ID='").append(partId).append("'");
        sql.append(" order by pkg_no desc ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * @param : @param slineId
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询详细
     */
    public List<Map<String, Object>> queryDetail(String slineId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_SO_DTL ");
        sql.append(" where 1=1 ");
        sql.append(" and SLINE_ID='").append(slineId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
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
    // OUT_ID,SO_CODE,OUT_CODE,DEALER_NAME,b.NAME as CREATE_BY_NAME,SALE_DATE,CREATE_DATE,SELLER_ID,ORDER_TYPE,AMOUNT,STATUS
    public PageResult<Map<String, Object>> queryOustockOrder(RequestWrapper request, String sellerId, int curPage, int pageSize) {
        String outCode = CommonUtils.checkNull(request.getParamValue("outCode"));
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
        String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));
        String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));
        String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
        String state = CommonUtils.checkNull(request.getParamValue("state"));
        String bSstartDate = CommonUtils.checkNull(request.getParamValue("bSstartDate"));
        String bSendDate = CommonUtils.checkNull(request.getParamValue("bSendDate"));
        String billNo = CommonUtils.checkNull(request.getParamValue("billNo"));
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
        String pSstartDate = CommonUtils.checkNull(request.getParamValue("pSstartDate"));
        String pSendDate = CommonUtils.checkNull(request.getParamValue("pSendDate"));
        StringBuffer sql = new StringBuffer();

        sql.append("WITH OUT_SO_INFO AS\n");
        sql.append(" (SELECT A.OUT_ID,\n");
        sql.append("         ZA_CONCAT(A.PICK_ORDER_ID) PICK_ORDER_ID,\n");
        sql.append("         ZA_CONCAT(A.ORDER_CODE) ORDER_CODE,\n");
        sql.append("         ZA_CONCAT(A.SO_CODE) SO_CODE,\n");
        sql.append("         MAX(A.PICK_ORDER_CREATE_DATE) PICK_ORDER_CREATE_DATE\n");
        sql.append("    FROM VW_PART_ORDER_ALL A\n");
        sql.append("   GROUP BY A.OUT_ID)\n");
        sql.append("SELECT A.*,\n");
        sql.append("       B.NAME                    AS CREATE_BY_NAME,\n");
        sql.append("       C.WH_NAME,\n");
        sql.append("       SM.SO_CODE                SOCODE,\n");
        sql.append("       SM.ORDER_CODE,\n");
        sql.append("       SM.PICK_ORDER_CREATE_DATE\n");
        sql.append("  FROM TT_PART_OUTSTOCK_MAIN A\n");
        sql.append("  LEFT JOIN TC_USER B\n");
        sql.append("    ON A.CREATE_BY = B.USER_ID\n");
        sql.append("  LEFT JOIN TT_PART_WAREHOUSE_DEFINE C\n");
        sql.append("    ON A.WH_ID = C.WH_ID\n");
        sql.append("  JOIN OUT_SO_INFO SM\n");
        sql.append("    ON A.OUT_ID = SM.OUT_ID\n");
        sql.append(" WHERE 1 = 1");

        if (!"".equals(sellerId)) {
            sql.append(" and A.seller_id = ").append(sellerId);
        }
        if (!"".equals(SstartDate)) {
            sql.append(" and A.CREATE_DATE>= to_date('").append(SstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(SendDate)) {
            sql.append(" and A.CREATE_DATE<= to_date('").append(SendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(bSstartDate)) {
            sql.append(" and A.Bill_DATE>= to_date('").append(bSstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(bSendDate)) {
            sql.append(" and A.Bill_DATE<= to_date('").append(bSendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(billNo)) {
            sql.append(" and A.bill_no  like '%").append(billNo).append("%'");
        }

        if (!"".equals(dealerName)) {
            sql.append(" and A.DEALER_NAME like '%").append(dealerName).append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and A.DEALER_Code like '%").append(dealerCode).append("%'");
        }
        if (!"".equals(whId)) {
            sql.append(" and A.WH_ID ='").append(whId).append("'");
        }
        if (!"".equals(orderType)) {
            sql.append(" and A.ORDER_TYPE ='").append(orderType).append("'");
        }
        if (state.equals(Constant.IF_TYPE_YES.toString())) {
            sql.append(" and A.bill_no  is not null");
        }
        if (state.equals(Constant.IF_TYPE_NO.toString())) {
            sql.append(" and A.bill_no  is  null");
        }
        if (!"".equals(outCode)) {
            sql.append(" and  a.out_code  like  '%").append(outCode).append("%'");
        }
        if (!"".equals(pickOrderId)) {
            sql.append(" and  sm.pick_order_id  like  '%").append(pickOrderId).append("%'");
        }
        if (!"".equals(soCode) && null != soCode) {
            sql.append(" and sm.so_code LIKE  '%").append(soCode).append("%'");
        }
        if (!"".equals(pSstartDate)) {
            sql.append(" and SM.PICK_ORDER_CREATE_DATE>= to_date('").append(pSstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(pSendDate)) {
            sql.append(" and SM.PICK_ORDER_CREATE_DATE<= to_date('").append(pSendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        sql.append(" order by a.create_date desc ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    private String getSoId(String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select SO_ID from TT_PART_SO_MAIN a");
        sql.append(" where order_id='").append(orderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return "";
        }
        return CommonUtils.checkNull(list.get(0));
    }

    /**
     * @param : @param outId
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    :
     *           2013-4-16
     * @Title :
     * @Description: 查询详细
     */
    public PageResult<Map<String, Object>> queryOutstockDetail(RequestWrapper request, String sellerId, int curPage, int pageSize) {
        String outCode = CommonUtils.checkNull(request.getParamValue("outCode"));
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
        String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));
        String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));
        String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
        String state = CommonUtils.checkNull(request.getParamValue("state"));
        String bSstartDate = CommonUtils.checkNull(request.getParamValue("bSstartDate"));
        String bSendDate = CommonUtils.checkNull(request.getParamValue("bSendDate"));
        String pSstartDate = CommonUtils.checkNull(request.getParamValue("pSstartDate"));
        String pSendDate = CommonUtils.checkNull(request.getParamValue("pSendDate"));
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));

        StringBuffer sql = new StringBuffer();

        sql.append("WITH OUT_SO_INFO AS\n");
        sql.append(" (SELECT A.OUT_ID,\n");
        sql.append("         ZA_CONCAT(A.PICK_ORDER_ID) PICK_ORDER_ID,\n");
        sql.append("         ZA_CONCAT(A.ORDER_CODE) ORDER_CODE,\n");
        sql.append("         ZA_CONCAT(A.SO_CODE) SO_CODE,\n");
        sql.append("         MAX(A.PICK_ORDER_CREATE_DATE) PICK_ORDER_CREATE_DATE\n");
        sql.append("    FROM VW_PART_ORDER_ALL A\n");
        sql.append("   GROUP BY A.OUT_ID)\n");
        sql.append("SELECT M.OUT_CODE,\n");
        sql.append("       M.DEALER_CODE,\n");
        sql.append("       m.DEALER_NAME,\n");
        sql.append("       D.PART_OLDCODE,\n");
        sql.append("       D.PART_CNAME,\n");
        sql.append("       D.PART_CODE,\n");
        sql.append("       D.UNIT,\n");
        sql.append("       D.SALE_PRICE,\n");
        sql.append("       --SD.SALES_QTY,\n");
        sql.append("       --SD.BUY_AMOUNT,\n");
        sql.append("       D.OUTSTOCK_QTY,\n");
        sql.append("       D.SALE_AMOUNT,\n");
        sql.append("       D.CREATE_DATE,\n");
        sql.append("       SP.SALE_PRICE3,\n");
        sql.append("       SP.SALE_PRICE3 * D.OUTSTOCK_QTY PLAN_AMOUNT,\n");
        sql.append("       (SELECT C.CODE_DESC\n");
        sql.append("          FROM TC_CODE C, TT_PART_DEFINE DE\n");
        sql.append("         WHERE C.CODE_ID = DE.PART_TYPE\n");
        sql.append("           AND DE.PART_ID = D.PART_ID) PART_TYPE,\n");
        sql.append("       M.BILL_NO\n");
        sql.append("  FROM TT_PART_OUTSTOCK_MAIN M,\n");
        sql.append("       TT_PART_OUTSTOCK_DTL  D,\n");
        sql.append("       OUT_SO_INFO           SM,\n");
        sql.append("       TT_PART_SALES_PRICE   SP\n");
        sql.append(" WHERE M.OUT_ID = D.OUT_ID\n");
        sql.append("   AND M.OUT_ID = SM.OUT_ID\n");
        sql.append("   AND D.PART_ID = SP.PART_ID");

        if (!"".equals(sellerId)) {
            sql.append(" and M.seller_id = ").append(sellerId);
        }
        if (!"".equals(SstartDate)) {
            sql.append(" and M.CREATE_DATE>= to_date('").append(SstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(SendDate)) {
            sql.append(" and M.CREATE_DATE<= to_date('").append(SendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(bSstartDate)) {
            sql.append(" and M.BILL_DATE>= to_date('").append(bSstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(bSendDate)) {
            sql.append(" and M.BILL_DATE<= to_date('").append(bSendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(soCode) && null != soCode) {
            sql.append(" and SM.SO_CODE like '%").append(soCode).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and M.DEALER_NAME like '%").append(dealerName).append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and M.DEALER_Code like '%").append(dealerCode).append("%'");
        }
        if (!"".equals(whId)) {
            sql.append(" and M.WH_ID ='").append(whId).append("'");
        }
        if (!"".equals(orderType)) {
            sql.append(" and M.ORDER_TYPE ='").append(orderType).append("'");
        }
        if (state.equals(Constant.IF_TYPE_YES.toString())) {
            sql.append(" and M.BILL_NO  is not null");
        } else if (state.equals(Constant.IF_TYPE_NO.toString())) {
            sql.append(" and M.BILL_NO  is  null");
        }
        if (!"".equals(outCode)) {
            sql.append(" and  M.out_code  like  '%").append(outCode).append("%'");
        }
        if (!"".equals(pickOrderId)) {
            sql.append(" and  SM.pick_order_id  like  '%").append(pickOrderId).append("%'");
        }
        if (!"".equals(pSstartDate)) {
            sql.append(" and SM.PICK_ORDER_CREATE_DATE>= to_date('").append(pSstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(pSendDate)) {
            sql.append(" and SM.PICK_ORDER_CREATE_DATE<= to_date('").append(pSendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        sql.append(" order by M.create_date desc ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param slineId
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询主表
     */
    public Map<String, Object> queryOutstockMain(String outId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.*,TO_CHAR(A.AMOUNT, 'FM999,999,999,990.00') AS F_AMOUNT, (select name from tc_user b where a.create_by=b.user_id) as CREATE_BY_NAME,(select nvl(t.wh_name,'') as wh_name from TT_PART_WAREHOUSE_DEFINE t where t.WH_ID=a.wh_id) as WH_NAME,");
        sql.append(" to_char(c.SALE_DATE,'yyyy-MM-dd') SALE_DATE1,");
        sql.append(" (select tv_name from TT_TRANSPORT_VALUATION tv where tv.tv_id= c.TRANS_TYPE ) TRANS_TYPE1,  ");
        sql.append(" c.TRANSPAY_TYPE TRANSPAY_TYPE1,  ");
        sql.append(" c.STATION STATION1 ");
        sql.append(" from TT_PART_OUTSTOCK_MAIN a");
        sql.append(" left join TT_PART_SO_MAIN c on c.so_id=a.so_id ");
        sql.append(" where 1=1 ");
        sql.append(" and OUT_ID=").append(outId);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    /**
     * @param : @param outId
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询详细
     */
    public List<Map<String, Object>> queryOutstockDetail(String outId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select A.*,");
        sql.append(" (select loc_name from tt_part_loaction_define td where td.loc_id=a.loc_id and td.state=STATE ) loc_name, ");
        sql.append(" B.BUY_QTY BUY_QTY1,B.SALES_QTY,TO_CHAR(B.BUY_PRICE, 'FM999,999,990.00') AS BUY_PRICE,TO_CHAR(B.BUY_AMOUNT,'FM999,999,990.00') AS BUY_AMOUNT, B.STOCK_QTY  ");
        sql.append(" from TT_PART_OUTSTOCK_DTL A"); 
        sql.append(" left join TT_PART_SO_DTL B ON A.SO_ID=B.SO_ID AND A.PART_ID=B.PART_ID");
        sql.append(" where 1=1 ");
        sql.append(" and OUT_ID= ").append(outId);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public Map<String, Object> getWarehouse(String whId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_warehouse_define where wh_id='").append(whId).append("'");
        sql.append(" and state='").append(Constant.STATUS_ENABLE).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    /**
     * 现场BO回滚预占资源
     *
     * @param partId
     * @param locId
     * @param whId
     * @param boQty
     */
    public void updateBook(String partId, String locId, String whId, String boQty) {
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE TT_PART_BOOK T ");
        sql.append("   SET T.NORMAL_QTY = T.NORMAL_QTY + ").append(boQty);
        sql.append(" WHERE PART_ID ='").append(partId).append("'");
        sql.append("  AND WH_ID ='").append(whId).append("'");
        sql.append("  AND LOC_ID ='").append(locId).append("'");
        sql.append("  AND STATUS = 1 ");
        this.update(sql.toString(), null);
        sql.setLength(0);
        sql.append(" UPDATE TT_PART_BOOK T ");
        sql.append("   SET T.BOOKED_QTY = T.BOOKED_QTY - ").append(boQty);
        sql.append(" WHERE PART_ID ='").append(partId).append("'");
        sql.append("  AND WH_ID ='").append(whId).append("'");
        sql.append("  AND LOC_ID ='").append(locId).append("'");
        sql.append("  AND STATUS = 1 ");
        this.update(sql.toString(), null);
    }

    //有PART_ID就是配件的  没有就是销售单的
    public String getPkgQty(String soId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select  nvl(max(to_number(pkg_no)),'0') as  pkgQty from tt_part_pkg_dtl where so_id ='").append(soId).append("'");
        if (partId != null) {
            sql.append(" and part_id='").append(partId).append("'");
        }
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0) || null == list.get(0).get("PKGQTY")) {
            return "0";
        }
        return list.get(0).get("PKGQTY").toString();
    }

    public String getPartWeight(String soId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select sum(WEIGHT) as WEIGHT from tt_part_pkg_dtl ");
        sql.append(" where 1=1 ");
        sql.append(" and so_id='").append(soId).append("'");
        if (partId != null) {
            sql.append(" and part_id='").append(partId).append("'");
        }
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0) || null == list.get(0).get("WEIGHT")) {
            return "0";
        }
        return list.get(0).get("WEIGHT").toString();
    }

    public Map<String, Object> getOutMain(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_outstock_main where so_id='").append(soId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public String getPkgGroup(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(pkg_no) as num from tt_part_pkg_dtl where so_id='").append(soId).append("'");
        sql.append(" group by pkg_no ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0) || null == list.get(0).get("NUM")) {
            return "0";
        }
        return list.get(0).get("NUM").toString();
    }

    public List<Map<String, Object>> getLock(String soId, String whId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from VW_PART_STOCK where part_id in (");
        sql.append(" select part_id from tt_part_so_dtl where so_id='").append(soId).append("'");
        sql.append(") and wh_id='").append(whId).append("' and is_locked='1'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }


    public Map<String, Object> queryAmount(RequestWrapper request, AclUserBean logonUser) throws Exception {

        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));//拣货单号
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//订货单位编码
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//订货单位
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));//出库仓库
        String startDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//合并日期开始
        String endDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//合并日期结束
        String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE"));//发运方式
        //String pkgState = CommonUtils.checkNull(request.getParamValue("pkgState"));//是否已出库

        try {
            StringBuffer sql = new StringBuffer("");


            sql.append("SELECT nvl(SO.DCK_AMOUNT,0) DCK_AMOUNT,nvl(om.YCK_AMOUNT,0) YCK_AMOUNT\n");
            sql.append("  FROM (SELECT SUM(M.AMOUNT) DCK_AMOUNT\n");
            sql.append("          FROM TT_PART_SO_MAIN M\n");
            sql.append("         WHERE M.STATE <> " + Constant.CAR_FACTORY_SALE_ORDER_STATE_03 + "\n");
            if (!"".equals(pickOrderId)) {
                sql.append(" AND M.PICK_ORDER_ID LIKE '%" + pickOrderId + "%'");
            }
            if (!"".equals(dealerCode)) {
                sql.append(" AND M.DEALER_CODE LIKE '%" + dealerCode + "%'");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND M.DEALER_NAME LIKE '%" + dealerName + "%'");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND TO_DATE(M.PICK_ORDER_CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(endDate)) {
                sql.append(" AND TO_DATE(M.PICK_ORDER_CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(transType)) {
                sql.append(" AND M.TRANS_TYPE=" + transType + "");
            }
            if (!"".equals(whId)) {
                sql.append(" AND M.WH_ID=" + whId + "");
            }
            if (logonUser.getDealerId() == null) {
                sql.append("  and m.seller_id=" + logonUser.getOrgId() + "");
            } else {
                sql.append("  and m.seller_id=" + logonUser.getDealerId() + "");
            }
            sql.append("  and M.STATE=" + Constant.CAR_FACTORY_PKG_STATE_02 + "");
            //if (pkgState.equals(Constant.IF_TYPE_YES.toString())) {
            //sql.append(" and M.STATE in (").append(Constant.CAR_FACTORY_TRANS_STATE_01 + "," + Constant.CAR_FACTORY_TRANS_STATE_02 + "," + Constant.CAR_FACTORY_PKG_STATE_03).append(")");
            //} else if (pkgState.equals(Constant.IF_TYPE_NO.toString())) {
            //sql.append(" and M.STATE in (").append(Constant.CAR_FACTORY_PKG_STATE_02).append(")");
            //} else {
            //sql.append(" and M.STATE in (").append(Constant.CAR_FACTORY_TRANS_STATE_01 + "," + Constant.CAR_FACTORY_TRANS_STATE_02 + "," + Constant.CAR_FACTORY_PKG_STATE_03 + "," + Constant.CAR_FACTORY_PKG_STATE_02).append(")");
            //}
            sql.append(" ) SO,\n");

            sql.append("       (SELECT SUM(M.AMOUNT) YCK_AMOUNT\n");
            sql.append("          FROM TT_PART_OUTSTOCK_MAIN M\n");
            sql.append("         WHERE 1=1\n");
            if (logonUser.getDealerId() == null) {
                sql.append("  and m.seller_id=" + logonUser.getOrgId() + "");
            } else {
                sql.append("  and m.seller_id=" + logonUser.getDealerId() + "");
            }
            if (!"".equals(dealerCode)) {
                sql.append(" AND M.DEALER_CODE LIKE '%" + dealerCode + "%'");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND M.DEALER_NAME LIKE '%" + dealerName + "%'");
            }
            if (!"".equals(pickOrderId)) {
                sql.append(" AND EXISTS (SELECT  1 FROM TT_PART_SO_MAIN sm WHERE sm.so_id=m.so_id AND  sM.PICK_ORDER_ID LIKE '%" + pickOrderId + "%')");
            }
            sql.append(" AND EXISTS (SELECT  1 FROM TT_PART_SO_MAIN sm WHERE sm.so_id=m.so_id AND NVL(SM.PICK_ORDER_CREATE_DATE,TO_DATE('" + startDate + "', 'yyyy-MM-dd')) >=TO_DATE('" + startDate + "', 'yyyy-MM-dd') AND NVL(SM.PICK_ORDER_CREATE_DATE,TO_DATE('" + endDate + "', 'yyyy-MM-dd')) <=TO_DATE('" + endDate + "', 'yyyy-MM-dd'))\n");
            if (!"".equals(transType)) {
                sql.append(" AND M.TRANS_TYPE=" + transType + "");
            }
            if (!"".equals(whId)) {
                sql.append(" AND M.WH_ID=" + whId + "");
            }
            //if (pkgState.equals(Constant.IF_TYPE_YES.toString())) {
            //sql.append(" and M.STATE in (").append(Constant.CAR_FACTORY_TRANS_STATE_01 + "," + Constant.CAR_FACTORY_TRANS_STATE_02 + "," + Constant.CAR_FACTORY_PKG_STATE_03).append(")");
            //}// else if (pkgState.equals(Constant.IF_TYPE_NO.toString())) {
            //sql.append(" and M.STATE in (").append(Constant.CAR_FACTORY_PKG_STATE_02).append(")");
            //} else {
            //sql.append(" and M.STATE in (").append(Constant.CAR_FACTORY_TRANS_STATE_01 + "," + Constant.CAR_FACTORY_TRANS_STATE_02 + "," + Constant.CAR_FACTORY_PKG_STATE_03 + "," + Constant.CAR_FACTORY_PKG_STATE_02).append(")");
            //}
            sql.append("  )OM\n");

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 查询销售单服务商订货价格
     *
     * @param pickOrderId 拣货单ID
     * @param partId      配件ID
     * @return
     * @throws Exception
     */
    public Map<String, Object> getBuyprice(String pickOrderId, Long partId) throws Exception {

        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT DISTINCT T2.PART_ID, T2.BUY_PRICE \n");
            sql.append("   FROM TT_PART_SO_MAIN T1, TT_PART_SO_DTL T2 \n");
            sql.append("  WHERE T1.SO_ID = T2.SO_ID \n");
            sql.append("    AND T1.PICK_ORDER_ID IN (").append(pickOrderId).append(")");
            sql.append("    AND T2.PART_ID = ").append(partId);

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * 查询账面库存
     *
     * @param whId   仓库ID
     * @param partId 配件ID
     * @param locId  货位ID
     * @return
     * @throws Exception
     */
    public Map<String, Object> getStockQty(String whId, Long partId, Long locId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT NVL(SUM(T.ITEM_QTY),0) STOCK_QTY,NVL(SUM(T.BOOKED_QTY),0) BOOKED_QTY,NVL(SUM(T.NORMAL_QTY),0) NORMAL_QTY  \n");
            sql.append("  FROM VW_PART_STOCK T \n");
            sql.append("  WHERE T.WH_ID = ").append(whId);
            sql.append("  AND T.PART_ID = ").append(partId);
            sql.append("  AND T.LOC_ID = ").append(locId);
            sql.append("  AND T.STATE = 10011001");
            sql.append("  AND T.STATUS = 1");

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * 更新装箱单状态为已出库
     *
     * @param pkgNos      箱号
     * @param pickOrderId 拣货单ID
     * @param outId       出库单ID
     * @throws Exception
     */
    public void updatePkgStatus(String pkgNos, String pickOrderId, Long outId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");

            String[] pkgNoArr = pkgNos.split(",");
            sql.append("UPDATE TT_PART_PKG_BOX_DTL T SET T.STATUS=0,T.OUT_ID=").append(outId);
            sql.append(" WHERE T.PICK_ORDER_ID='").append(pickOrderId).append("'");
            sql.append("    AND T.PKG_NO IN (");
            for (int i = 0; i < pkgNoArr.length; i++) {
                sql.append("'").append(pkgNoArr[i]).append("',");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")");

            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param trpland
     * @param outId
     * @throws Exception
     */
    public void updatePkgStatus(String trpland, Long outId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE TT_PART_PKG_BOX_DTL t SET t.status=0,t.out_id=" + outId + "\n");
            sql.append("WHERE t.trplan_id=" + trpland);
            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param trpland
     * @param outId
     * @throws Exception
     */
    public void updateTranPlanStatus(String trpland, Long outId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE TT_PART_TRANS_PLAN P\n");
            sql.append("   SET P.out_id = " + outId + "\n");
            sql.append(" WHERE P.TRPLAN_ID = " + trpland);
            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 查询现场BO明细
     *
     * @param pickOrderId 拣货单号
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getLocBoList(String pickOrderId) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT T.PART_ID,\n");
            sql.append("       T.PART_CODE,\n");
            sql.append("       T.PART_OLDCODE,\n");
            sql.append("       T.PART_CNAME,\n");
            sql.append("       T.UNIT,\n");
            sql.append("       T.LOC_ID,\n");
            sql.append("       T.BATCH_NO,\n");//20170829 add
            sql.append("       SUM(T.LOC_BO_QTY) LOC_BO_QTY,\n");
            sql.append("       SUM(T.SALES_QTY) SALES_QTY,\n");
            sql.append("       SUM(T.PKG_QTY) PKG_QTY,\n");
            sql.append("       SD.BUY_PRICE\n");
            sql.append("  FROM TT_PART_PKG_DTL T, TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM\n");
            sql.append(" WHERE T.PICK_ORDER_ID = SM.PICK_ORDER_ID\n");
            sql.append("   AND SD.SO_ID = SM.SO_ID\n");
            sql.append("   AND T.PART_ID = SD.PART_ID\n");
            sql.append("   AND T.PKG_NO IS NULL\n");
            sql.append("   AND T.STATUS = 1\n");
            sql.append("   AND T.PICK_ORDER_ID IN (").append(pickOrderId).append(")\n");
            sql.append(" GROUP BY T.PART_ID,\n");
            sql.append("          T.PART_CODE,\n");
            sql.append("          T.PART_OLDCODE,\n");
            sql.append("          T.PART_CNAME,\n");
            sql.append("          T.UNIT,\n");
            sql.append("          T.LOC_ID,\n");
            sql.append("          T.BATCH_NO,\n");//20170829 add
            sql.append("          SD.BUY_PRICE");

            list = this.pageQuery(sql.toString(), null, this.getFunName());
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 现场BO生成后更新状态为完成
     *
     * @param pickOrderId 拣货单号
     * @throws Exception
     */
    public void updatePkgDtlStatus(String pickOrderId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE TT_PART_PKG_DTL T SET T.STATUS=0 ");
            sql.append(" WHERE T.PICK_ORDER_ID='").append(pickOrderId).append("'");
            sql.append(" AND T.PKG_NO IS NULL");
            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 通过货位ID和配件ID查询现场BO单对应的销售ID和订单ID
     *
     * @param locId  货位ID
     * @param partId 配件ID
     * @return
     * @throws Exception
     */
    public Map<String, Object> getSoId(String locId, Long partId) throws Exception {

        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT SM.SO_ID, SM.ORDER_ID\n");
            sql.append("  FROM TT_PART_SO_MAIN SM\n");
            sql.append(" WHERE EXISTS (SELECT 1\n");
            sql.append("          FROM TT_PART_BOOK_DTL BD\n");
            sql.append("         WHERE BD.ORDER_ID = SM.SO_ID\n");
            sql.append("           AND BD.LOC_ID = " + locId + "\n");
            sql.append("           AND BD.PART_ID = " + partId + ")\n");
            sql.append("   AND SM.STATUS = 1");

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

    }

    public PageResult<Map<String, Object>> queryPartLocBo(
            RequestWrapper request, Integer curPage, Integer pageSizePartMini) throws Exception {

        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        L.LOC_NAME, \n");
            sql.append("        T.SALES_QTY, \n");
            sql.append("        T.PKG_QTY, \n");
            sql.append("        T.LOC_BO_QTY, \n");
            sql.append("        DECODE(T.STATUS, 1, '未处理', 0, '已处理') STATUS \n");
            sql.append("   FROM TT_PART_PKG_DTL T, TT_PART_LOACTION_DEFINE L \n");
            sql.append("  WHERE T.LOC_ID = L.LOC_ID \n");
            sql.append("    AND T.PICK_ORDER_ID in (").append(pickOrderId).append(")");
            sql.append(" AND T.LOC_BO_QTY IS NOT NULL \n");

            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCname)) {
                sql.append(" AND T.PART_CNAME LIKE '%").append(partCname).append("%'");
            }

            return pageQuery(sql.toString(), null, getFunName(), pageSizePartMini, curPage);

        } catch (Exception e) {
            throw e;
        }

    }

    public List<TtPartPkgBoxDtlPO> queryPkgNo(String pickOrderId, String printFlag, String state) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT *\n");
        sql.append("  FROM TT_PART_PKG_BOX_DTL T\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND T.PICK_ORDER_ID = '" + pickOrderId + "'\n");
        sql.append("   AND T.STATE =" + state + "\n");
        if ("0".equals(printFlag)) {
            sql.append("   AND T.PRINT_NUM = 0\n");
        } else {
            sql.append("   AND T.PRINT_NUM > 0\n");
        }
        sql.append("\t ORDER BY t.pkg_no ASC");

        List list = this.select(TtPartPkgBoxDtlPO.class, sql.toString(), null);
        return list;
    }

    /**
     * 查询现场BO明细
     *
     * @param pickOrderId 拣货单号
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getLocBoListWithLocId(String pickOrderId) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            StringBuffer sql = new StringBuffer();

            sql.append("SELECT T.* FROM TT_PART_PKG_DTL T WHERE T.PICK_ORDER_ID='").append(pickOrderId).append("'");
            sql.append(" AND T.PKG_NO IS NULL AND T.STATUS=1");

            list = this.pageQuery(sql.toString(), null, this.getFunName());
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPartLocBoById(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {

            String boId = CommonUtils.checkNull(request.getParamValue("boId"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        L.LOC_NAME, \n");
            sql.append("        T.BUY_QTY, \n");
            sql.append("        T.SALES_QTY, \n");
            sql.append("        T.BO_QTY \n");
            sql.append("   FROM TT_PART_BO_DTL T, TT_PART_LOACTION_DEFINE L \n");
            sql.append("  WHERE T.LOC_ID = L.LOC_ID \n");
            sql.append("   AND T.BO_ID = " + boId + "\n");

            if (!"".equals(partOldCode)) {
                sql.append(" AND upper(T.PART_OLDCODE) LIKE upper('%").append(partOldCode).append("%')");
            }
            if (!"".equals(partCname)) {
                sql.append(" AND T.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND upper(T.partCode) LIKE upper('%").append(partCname).append("%')");
            }

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public boolean isOutStock(String trplan_id) {
        TtPartOutstockMainPO mainPO = new TtPartOutstockMainPO();
        mainPO.setTrplanId(Long.valueOf(trplan_id));
        if (select(mainPO).size() > 0)
            return true;
        return false;
    }

    /**
     * 取消发运时解除箱子和发运单的关系
     *
     * @param transPlanId 发运计划ID
     */
    public void updatePkgBoxTransInfo(String transPlanId) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE TT_PART_PKG_BOX_DTL BD\n");
        sql.append("   SET BD.TRPLAN_ID = NULL\n");
        sql.append(" WHERE BD.TRPLAN_ID IS NOT NULL\n");
        sql.append("   AND EXISTS (SELECT 1\n");
        sql.append("          FROM TT_PART_TRANS_PLAN_DTL PD\n");
        sql.append("         WHERE PD.PICK_ORDER_ID = BD.PICK_ORDER_ID\n");
        sql.append("           AND PD.PKG_NO = BD.PKG_NO)\n");
        sql.append("   AND BD.TRPLAN_ID = " + transPlanId + "");
        this.update(sql.toString(), null);
    }
    
    
    /**
     * 获取批次号
     * @param outId
     * @param partId
     * @param locId
     * @param whId
     * @return
     */
//    public String getBatchNo(Long outId,Long partId,Long locId,Long whId){
////    	StringBuffer sql = new StringBuffer();
////        sql.append("SELECT PART_BATCH FROM TT_PART_RECORD");
////        sql.append(" WHERE ORG_ID=2010010100070674" );
////        sql.append(" AND STATE=1 ");
////        sql.append(" AND ORDER_ID = "+outId);
////        sql.append(" AND PART_ID="+partId);
////        sql.append(" AND LOC_ID="+locId);
////        sql.append(" AND WH_ID="+whId);
//        
//        TtPartRecordPO recordPo = new TtPartRecordPO();
//        recordPo.setOrderId( outId );
//        recordPo.setPartId( partId );
//        recordPo.setLocId( locId );
//        recordPo.setWhId( whId );
//        List<TtPartRecordPO> list= select(recordPo);
//        if (list !=null && list.size() > 0){
//        	TtPartRecordPO resultPo= (TtPartRecordPO)select(recordPo).get(0);
//            return resultPo.getBatchNo();
//        }
//        return "";
//    	
//    }
    
    
}

