package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.po.TtPartInstockExceptionLogPO;
import com.infodms.dms.po.TtPartLoactionDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
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

public class PartDlrInstockDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartDlrInstockDao.class);

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    private static final PartDlrInstockDao dao = new PartDlrInstockDao();

    private PartDlrInstockDao() {
    }

    public static final PartDlrInstockDao getInstance() {
        return dao;
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
    public PageResult<Map<String, Object>> queryInstockOrder(RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String transId = CommonUtils.checkNull(request.getParamValue("transId"));//发运单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//订货单位
        String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//采购单号
        String sellerName = CommonUtils.checkNull(request.getParamValue("sellerName"));//销售单位
        String TstartDate = CommonUtils.checkNull(request.getParamValue("TstartDate"));//发运日期(start)
        String TendDate = CommonUtils.checkNull(request.getParamValue("TendDate"));//发运日期(end)
        String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));//订单类型
        String state = CommonUtils.checkNull(request.getParamValue("state"));//状态
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }

//        sql.append("WITH TRANS_ORDER AS\n");
//        sql.append(" (SELECT PT.TRANS_ID, ZA_CONCAT(DISTINCT VD.ORDER_CODE) TR_ORDER_CODE\n");
//        sql.append("    FROM TT_PART_TRANS         PT,\n");
//        sql.append("         TT_PART_OUTSTOCK_MAIN OM,\n");
//        sql.append("         TT_PART_TRANS_PLAN    PTP,\n");
//        sql.append("         VW_PART_TRANS_DTL     VD\n");
//        sql.append("   WHERE PT.OUT_ID = OM.OUT_ID\n");
//        sql.append("     AND OM.TRPLAN_ID = PTP.TRPLAN_ID\n");
//        sql.append("     AND PTP.TRPLAN_ID = VD.TRPLAN_ID\n");
//        sql.append("   GROUP BY PT.TRANS_ID)\n");
        sql.append("\n");
        sql.append("SELECT A.*,\n");
        //20170807 add start
        sql.append("       (select LOGI_FULL_NAME from TT_SALES_LOGI tv where tv.STATUS=10011001 and tv.LOGI_CODE=a.transport_org) TRANSPORT_ORG_CN,");
        //20170807 add end
        sql.append("       CASE\n");
        sql.append("         WHEN A.ORDER_TYPE = 92151004 OR A.ORDER_TYPE = 92151007 THEN\n");
        sql.append("          A.TRANS_CODE\n");
        sql.append("         ELSE\n");
        sql.append("          (SELECT P.TRPLAN_CODE\n");
        sql.append("             FROM TT_PART_TRANS_PLAN P\n");
        sql.append("            WHERE P.OUT_ID = A.OUT_ID)\n");
        sql.append("       END TRPLAN_CODE,\n");
        sql.append("       (SELECT NAME FROM TC_USER B WHERE A.CREATE_BY = B.USER_ID) AS CREATE_BY_NAME,\n");
        sql.append("       (SELECT ORDER_AMOUNT\n");
        sql.append("          FROM TT_PART_DLR_ORDER_MAIN C\n");
        sql.append("         WHERE A.ORDER_ID = C.ORDER_ID) AS ORDER_AMOUNT,\n");
        sql.append("       TO_CHAR(A.AMOUNT, 'FM999,999,999,999,990.00') AS CONVERSEAMOUNT,\n");
        sql.append("       (SELECT SUM(LG.EXCEPTION_NUM) NUM\n");
        sql.append("          FROM TT_PART_INSTOCK_EXCEPTION_LOG LG,\n");
        sql.append("               TT_PART_DEFINE                D,\n");
        sql.append("               TT_PART_DLR_INSTOCK_MAIN      IM,\n");
        sql.append("               TT_PART_TRANS                 PT,\n");
        sql.append("               TT_PART_OUTSTOCK_MAIN         OM,\n");
        sql.append("               TT_PART_OUTSTOCK_DTL          OD\n");
        sql.append("         WHERE IM.IN_ID = LG.IN_ID\n");
        sql.append("           AND LG.PART_ID = D.PART_ID\n");
        sql.append("           AND IM.TRANS_ID = PT.TRANS_ID\n");
        sql.append("           AND OM.OUT_ID = PT.OUT_ID\n");
        sql.append("           AND OM.OUT_ID = OD.OUT_ID\n");
        sql.append("           AND LG.PART_ID = OD.PART_ID\n");
        sql.append("           AND IM.TRANS_ID = A.TRANS_ID) AS FLAG\n");
//        sql.append("        NVL(TR.TR_ORDER_CODE, A.ORDER_CODE) TR_ORDER_CODE\n");
//        sql.append("  FROM TT_PART_TRANS A, TRANS_ORDER TR\n");
        sql.append("  FROM TT_PART_TRANS A\n");
        sql.append(" WHERE 1 = 1\n");
//        sql.append("   AND A.TRANS_ID = TR.TRANS_ID(+)\n");
        if (!"".equals(transId)) {
            sql.append(" AND (A.TRANS_CODE LIKE '%" + transId + "%' OR EXISTS\n");
            sql.append("     (SELECT 1\n");
            sql.append("        FROM TT_PART_TRANS_PLAN P\n");
            sql.append("       WHERE P.OUT_ID = A.OUT_ID\n");
            sql.append("         AND P.TRPLAN_CODE LIKE '%" + transId + "%'))\n");
        }

        if (!"".equals(dealerName)) {
            sql.append(" and A.DEALER_NAME like '%").append(dealerName).append("%'");
        }

        if (!"".equals(orderId)) {
            sql.append(" and ( TR.TR_ORDER_CODE like '%").append(orderId).append("%'");
            sql.append(" OR  A.ORDER_CODE like '%").append(orderId).append("%')");
        }

        if (!"".equals(sellerName)) {
            sql.append(" and A.SELLER_NAME like '%").append(sellerName).append("%'");
        }

        if (!"".equals(TstartDate)) {
            sql.append(" and A.CREATE_DATE>=to_date('").append(TstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(TendDate)) {
            sql.append(" and A.CREATE_DATE<=to_date('").append(TendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }

        if (!"".equals(orderType)) {
            sql.append(" and A.ORDER_TYPE = '").append(orderType).append("'");
        }

        if (!"".equals(state)) {
            sql.append(" and A.STATE = '").append(state).append("'");
        }
        sql.append(" and A.CONSIGNEES_ID='").append(dealerId).append("'");
        sql.append(" order by a.create_date desc");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public Map<String, Object> getTransMain(String transId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.*, ");
        sql.append(" (select wh_name from TT_PART_WAREHOUSE_DEFINE b where a.wh_id=b.wh_id) as WH_NAME ");
        sql.append(" from TT_PART_TRANS a ");
        sql.append(" where 1=1 ");
        sql.append(" and TRANS_ID='").append(transId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return new HashMap();
        }
        return list.get(0);
    }

    public Map<String, Object> getTransMainNew(String transId, String flag) {
        StringBuffer sql = new StringBuffer();

        sql.append("WITH PART_TRANS_PLAN AS\n");
        sql.append(" (SELECT OM.OUT_ID, TP.TRPLAN_CODE\n");
        sql.append("    FROM TT_PART_OUTSTOCK_MAIN OM, TT_PART_TRANS_PLAN TP\n");
        sql.append("   WHERE OM.TRPLAN_ID = TP.TRPLAN_ID)\n");
        sql.append("SELECT A.*,\n");
        sql.append("       (SELECT WH_NAME\n");
        sql.append("          FROM TT_PART_WAREHOUSE_DEFINE B\n");
        sql.append("         WHERE A.WH_ID = B.WH_ID) AS WH_NAME,\n");
        sql.append("       TP.TRPLAN_CODE\n");
        sql.append("  FROM TT_PART_TRANS A, PART_TRANS_PLAN TP\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND A.OUT_ID = TP.OUT_ID(+)\n");
        if ("0".equals(flag)) {
            sql.append("   AND A.OUT_ID = TP.OUT_ID(+)\n");
            sql.append("   AND A.TRANS_ID = '" + transId + "'\n");
        } else {
            sql.append("   AND A.OUT_ID = TP.OUT_ID\n");
            sql.append("   AND TP.TRPLAN_CODE = '" + transId + "'\n");
        }
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return new HashMap();
        }
        return list.get(0);
    }

    public List<Map<String, Object>> getTransDetail(String transId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.*,");
        sql.append("    NVL((SELECT SUM(S.ITEM_QTY)\n");
        sql.append("         FROM TT_PART_ITEM_STOCK S\n");
        sql.append("         WHERE S.PART_ID = A.PART_ID\n");
        sql.append("         AND S.ORG_ID = PT.DEALER_ID\n");
        sql.append("         AND S.STATE IN (1, 2)\n");
        sql.append("         AND S.STATUS = 1),\n");
        sql.append("    0) AS NORMAL_QTY_NOW\n");
        sql.append(" FROM TT_PART_TRANS_DTL a, TT_PART_TRANS PT ");
        sql.append(" where 1=1 ");
        sql.append(" and A.TRANS_ID=").append(transId);
        sql.append(" AND A.TRANS_ID = PT.TRANS_ID");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return new ArrayList<Map<String, Object>>();
        }
        return list;
    }
    
    public List<Map<String, Object>> getTransDetail2(String transId, String[] partIds) {
        StringBuffer sql = new StringBuffer();
        sql.append("WITH SUM_PART_TRANS AS\n");
        sql.append(" (SELECT TD.TRANS_ID,\n");
        sql.append("         TD.PART_ID,\n");
        sql.append("         TD.PART_CNAME,\n");
        sql.append("         TD.PART_OLDCODE,\n");
        sql.append("         TD.PART_CODE,\n");
        sql.append("         TD.MIN_PACKAGE,\n");
        sql.append("         TD.UNIT,\n");
        sql.append("         SUM(TD.BUY_QTY) BUY_QTY,\n");
        sql.append("         SUM(TD.TRANS_QTY) TRANS_QTY\n");
        sql.append("    FROM TT_PART_TRANS_DTL TD\n");
        sql.append("   WHERE 1 = 1\n");
        sql.append("     AND TD.TRANS_ID = '"+transId+"'\n");
        sql.append("   GROUP BY TD.TRANS_ID,\n");
        sql.append("            TD.PART_ID,\n");
        sql.append("            TD.PART_CNAME,\n");
        sql.append("            TD.PART_OLDCODE,\n");
        sql.append("            TD.PART_CODE,\n");
        sql.append("            TD.MIN_PACKAGE,\n");
        sql.append("            TD.UNIT)\n");
        sql.append("\n");
        sql.append("SELECT TD.TRANS_ID,\n");
        sql.append("       TD.PART_ID,\n");
        sql.append("       TD.PART_CNAME,\n");
        sql.append("       TD.PART_OLDCODE,\n");
        sql.append("       TD.PART_CODE,\n");
        sql.append("       TD.MIN_PACKAGE,\n");
        sql.append("       TD.UNIT,\n");
        sql.append("       TD.BUY_QTY,\n");
        sql.append("       TD.TRANS_QTY,\n");
        sql.append("       NVL((SELECT SUM(S.ITEM_QTY)\n");
        sql.append("             FROM TT_PART_ITEM_STOCK S\n");
        sql.append("            WHERE S.PART_ID = TD.PART_ID\n");
        sql.append("              AND S.ORG_ID = TM.DEALER_ID\n");
        sql.append("              AND S.STATE IN (1, 2)\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) NORMAL_QTY_NOW\n");
        sql.append("  FROM SUM_PART_TRANS TD, TT_PART_TRANS TM\n");
        sql.append(" WHERE TD.TRANS_ID = TM.TRANS_ID\n");
        sql.append("   AND TM.TRANS_ID = '"+transId+"'\n");
        if(null != partIds && partIds.length > 0){
            sql.append("   AND TD.PART_ID IN ");
            sql.append("('"+partIds[0]+"'");
            for(int i = 1; i < partIds.length; i++){
                sql.append(",'" + partIds[i]+"'");
            }
            sql.append(")\n");
        }
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return new ArrayList<Map<String, Object>>();
        }
        return list;
    }

    public Map<String, Object> getLoc(String partId, String whId, String orgId) {
//        Long locId = OrderCodeManager.getPartLocId(orgId, whId, partId); //不再需要生成
        StringBuffer sql = new StringBuffer();
//        sql.append("  select * from TT_PART_LOACTION_DEFINE where loc_id ='").append(locId).append("'");
        sql.append("SELECT *\n");
        sql.append("  FROM TT_PART_LOACTION_DEFINE\n");
        sql.append(" WHERE PART_ID = '"+partId+"'\n");
        sql.append("   AND WH_ID = '"+whId+"'\n");
        sql.append("   AND ORG_ID = '"+orgId+"'\n");
        sql.append("   AND STATE = '10011001'\n");
        sql.append("   AND STATUS = '10041001'\n");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return new HashMap();
        }
        return list.get(0);
    }

    public List<Map<String, Object>> getWareHouse(String orgId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT Distinct wh_id,wh_name from TT_PART_WAREHOUSE_DEFINE ");
        sql.append(" where org_id='").append(orgId).append("'");
        sql.append(" AND state ='").append(Constant.STATUS_ENABLE).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    /**
     * @param orgId
     * @param whId
     * @param partId
     * @return
     */
    public String getStockQty(String orgId, String whId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append("   select nvl(sum(ITEM_QTY), 0) as qty ");
        sql.append("  from vw_part_stock t ");
        sql.append(" where t.WH_ID='").append(whId).append("'");
        sql.append(" and t.PART_ID='").append(partId).append("'");
        sql.append(" and org_id='").append(orgId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return "0";
        }
        return CommonUtils.checkNull(list.get(0).get("QTY"));
    }


    /**
     * @param transId
     * @return
     */
    public List<Map<String, Object>> getInstockMain(String transId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_dlr_instock_main where TRANS_ID='").append(transId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }


    /**
     * 赔偿清单主表
     *
     * @param transId
     * @return
     */
    public List<Map<String, Object>> getPayApplyMain(String transId) {
        StringBuffer sql = new StringBuffer();

//        sql.append("WITH TRANS_ORDER AS\n");
//        sql.append(" (SELECT PT.TRANS_ID, ZA_CONCAT(DISTINCT VD.ORDER_CODE) TR_ORDER_CODE\n");
//        sql.append("    FROM TT_PART_TRANS         PT,\n");
//        sql.append("         TT_PART_OUTSTOCK_MAIN OM,\n");
//        sql.append("         TT_PART_TRANS_PLAN    PTP,\n");
//        sql.append("         VW_PART_TRANS_DTL     VD\n");
//        sql.append("   WHERE PT.OUT_ID = OM.OUT_ID\n");
//        sql.append("     AND OM.TRPLAN_ID = PTP.TRPLAN_ID\n");
//        sql.append("     AND PTP.TRPLAN_ID = VD.TRPLAN_ID\n");
//        sql.append("   GROUP BY PT.TRANS_ID)\n");
//        sql.append("SELECT NVL(OM.TR_ORDER_CODE, TS.ORDER_CODE) ORDER_CODE,\n");
//        sql.append("       CASE\n");
//        sql.append("         WHEN TS.ORDER_TYPE = 92151004 OR TS.ORDER_TYPE = 92151007 THEN\n");
//        sql.append("          TS.TRANS_CODE\n");
//        sql.append("         ELSE\n");
//        sql.append("          (SELECT P.TRPLAN_CODE\n");
//        sql.append("             FROM TT_PART_TRANS_PLAN P\n");
//        sql.append("            WHERE P.OUT_ID = TS.OUT_ID)\n");
//        sql.append("       END TRANS_CODE,\n");
//        sql.append("       IM.CREATE_DATE,\n");
//        sql.append("       TS.DEALER_CODE,\n");
//        sql.append("       TS.DEALER_NAME,\n");
//        sql.append("       IM.IN_CODE\n");
//        sql.append("  FROM TT_PART_DLR_INSTOCK_MAIN IM, TT_PART_TRANS TS, TRANS_ORDER OM\n");
//        sql.append(" WHERE IM.TRANS_ID = TS.TRANS_ID\n");
//        sql.append("   AND TS.TRANS_ID = OM.TRANS_ID\n");
//        sql.append("   AND TS.TRANS_ID = '" + transId + "'\n");

        sql.append("SELECT TS.ORDER_CODE,\n");
        sql.append("       CASE\n");
        sql.append("         WHEN TS.ORDER_TYPE = 92151004\n");
        sql.append("              OR TS.ORDER_TYPE = 92151007 THEN\n");
        sql.append("          TS.TRANS_CODE\n");
        sql.append("         ELSE\n");
        sql.append("          (SELECT P.TRPLAN_CODE\n");
        sql.append("             FROM TT_PART_TRANS_PLAN P\n");
        sql.append("            WHERE P.OUT_ID = TS.OUT_ID)\n");
        sql.append("       END TRANS_CODE,\n");
        sql.append("       IM.CREATE_DATE,\n");
        sql.append("       TS.DEALER_CODE,\n");
        sql.append("       TS.DEALER_NAME,\n");
        sql.append("       IM.IN_CODE\n");
        sql.append("  FROM TT_PART_DLR_INSTOCK_MAIN IM, TT_PART_TRANS TS\n");
        sql.append(" WHERE IM.TRANS_ID = TS.TRANS_ID\n");
        sql.append("   AND TS.TRANS_ID = '" + transId + "'\n");

        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    /**
     * 赔偿清单列表
     *
     * @param id
     * @return
     */
    public List<Map<String, Object>> getPayDetail(String id) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT D.PART_OLDCODE,\n");
        sql.append("       D.PART_CNAME,\n");
        sql.append("       OD.OUTSTOCK_QTY,\n");
        sql.append("       OD.SALE_PRICE,\n");
        sql.append("       LG.EXCEPTION_NUM,\n");
        sql.append("       LG.EXCEPTION_NUM * OD.SALE_PRICE SUM_PRICE\n");
        sql.append("  FROM TT_PART_INSTOCK_EXCEPTION_LOG LG,\n");
        sql.append("       TT_PART_DEFINE                D,\n");
        sql.append("       TT_PART_DLR_INSTOCK_MAIN      IM,\n");
        sql.append("       TT_PART_TRANS                 PT,\n");
        sql.append("       TT_PART_OUTSTOCK_MAIN         OM,\n");
        sql.append("       TT_PART_OUTSTOCK_DTL          OD\n");
        sql.append(" WHERE IM.IN_ID = LG.IN_ID\n");
        sql.append("   AND LG.PART_ID = D.PART_ID\n");
        sql.append("   AND IM.TRANS_ID = PT.TRANS_ID\n");
        sql.append("   AND OM.OUT_ID = PT.OUT_ID\n");
        sql.append("   AND OM.OUT_ID = OD.OUT_ID\n");
        sql.append("   AND LG.PART_ID = OD.PART_ID\n");
        sql.append("   AND Im.TRANS_ID ='").append(id).append("'");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * @param inIds  入库单ID
     * @param partId 配件ID
     * @return
     */
    public Long getInQty(String inIds, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(sum(in_qty),'0') as qty from tt_part_dlr_instock_dtl where in_id in (").append(inIds).append(")");
        sql.append(" and part_id='").append(partId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null || list.get(0).get("QTY") == null) {
            return 0L;
        }
        return Long.valueOf(CommonUtils.checkNull(list.get(0).get("QTY")));
    }

    /**
     * @param id 入库明细ID
     * @return
     */
    public List<Map<String, Object>> getInDetail(String id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_dlr_instock_dtl where in_id='").append(id).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * @param transId 发运单ID
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> exportPurOrder(String transId) throws Exception {
        StringBuffer sql = new StringBuffer("");
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String dealerId = "";
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        /*sql.append(" select a.*,(select nvl(sum(ITEM_QTY),0) from vw_part_stock t where t.WH_ID=(select WH_ID from Tt_Part_Trans b where b.TRANS_ID=a.TRANS_ID) and t.PART_ID=a.PART_ID");
        sql.append(" and t.ORG_ID='").append(dealerId).append("'");
		sql.append(" ) as NORMAL_QTY_NOW from TT_PART_TRANS_DTL a ");
		sql.append(" left join TT_PART_ITEM_STOCK b on a.part_id=b.part_id and b.state='").append(Constant.STATUS_ENABLE).append("'");
		sql.append(" where 1=1 ");
		sql.append(" and TRANS_ID='").append(transId).append("'");
*/
        sql.append("WITH OUT_PRICE AS\n");
        sql.append(" (SELECT DISTINCT M.OUT_ID, OD.PART_ID, OD.SALE_PRICE\n");
        sql.append("    FROM TT_PART_OUTSTOCK_DTL OD, TT_PART_OUTSTOCK_MAIN M\n");
        sql.append("   WHERE OD.OUT_ID = M.OUT_ID),\n");
        sql.append("SUM_PART_TRANS AS\n");
        sql.append(" (SELECT TD.TRANS_ID,\n");
        sql.append("         TD.PART_ID,\n");
        sql.append("         TD.PART_CNAME,\n");
        sql.append("         TD.PART_OLDCODE,\n");
        sql.append("         TD.PART_CODE,\n");
        sql.append("         TD.MIN_PACKAGE,\n");
        sql.append("         TD.UNIT,\n");
        sql.append("         SUM(TD.BUY_QTY) BUY_QTY,\n");
        sql.append("         SUM(TD.TRANS_QTY) TRANS_QTY\n");
        sql.append("    FROM TT_PART_TRANS_DTL TD\n");
        sql.append("   WHERE 1 = 1\n");
        sql.append("     AND TD.TRANS_ID = '" + transId + "'\n");
        sql.append("   GROUP BY TD.TRANS_ID,\n");
        sql.append("            TD.PART_ID,\n");
        sql.append("            TD.PART_CNAME,\n");
        sql.append("            TD.PART_OLDCODE,\n");
        sql.append("            TD.PART_CODE,\n");
        sql.append("            TD.MIN_PACKAGE,\n");
        sql.append("            TD.UNIT)\n");
        sql.append("SELECT A.*,\n");
        sql.append("       NVL((SELECT OP.SALE_PRICE\n");
        sql.append("             FROM OUT_PRICE OP\n");
        sql.append("            WHERE OP.OUT_ID = M.OUT_ID\n");
        sql.append("              AND OP.PART_ID = A.PART_ID),\n");
        sql.append("           SP.SALE_PRICE1) SALE_PRICE1,\n");
        sql.append("       (A.TRANS_QTY * NVL((SELECT OP.SALE_PRICE\n");
        sql.append("                            FROM OUT_PRICE OP\n");
        sql.append("                           WHERE OP.OUT_ID = M.OUT_ID\n");
        sql.append("                             AND OP.PART_ID = A.PART_ID),\n");
        sql.append("                          SP.SALE_PRICE1)) BUY_AMOUNT\n");
        sql.append("  FROM SUM_PART_TRANS A\n");
        sql.append("  JOIN TT_PART_TRANS M\n");
        sql.append("    ON A.TRANS_ID = M.TRANS_ID\n");
        sql.append("  JOIN TT_PART_SALES_PRICE SP\n");
        sql.append("    ON A.PART_ID = SP.PART_ID\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND M.TRANS_ID = '" + transId + "'\n");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());

        return list;
    }

    public List<Map<String, Object>> exportDetail(RequestWrapper request) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String transId = CommonUtils.checkNull(request.getParamValue("transId"));//发运单号
        String TstartDate = CommonUtils.checkNull(request.getParamValue("TstartDate"));//发运日期(start)
        String TendDate = CommonUtils.checkNull(request.getParamValue("TendDate"));//发运日期(end)
        String state = CommonUtils.checkNull(request.getParamValue("state"));//状态

        //判断是否为车厂
        String dealerId = loginUser.getDealerId();
        sql.append("WITH SUM_PART_TRANS AS\n");
        sql.append(" (SELECT M.TRANS_CODE,\n");
        sql.append("         M.ORDER_CODE,\n");
        sql.append("         M.SO_CODE,\n");
        sql.append("         TC.CODE_DESC ORDER_TYPE,\n");
        sql.append("         TD.TRANS_ID,\n");
        sql.append("         TD.PART_ID,\n");
        sql.append("         TD.PART_CNAME,\n");
        sql.append("         TD.PART_OLDCODE,\n");
        sql.append("         TD.PART_CODE,\n");
        sql.append("         TD.MIN_PACKAGE,\n");
        sql.append("         TD.UNIT,\n");
        sql.append("         SUM(TD.BUY_QTY) BUY_QTY,\n");
        sql.append("         SUM(TD.TRANS_QTY) TRANS_QTY,\n");
        sql.append("         TO_CHAR(M.CREATE_DATE, 'YYYY/MM/dd HH24:mi:ss') CREATE_DATE \n");
        sql.append("    FROM TT_PART_TRANS_DTL TD, TT_PART_TRANS M, TC_CODE TC\n");
        sql.append("   WHERE TD.TRANS_ID = M.TRANS_ID\n");
        sql.append("     AND TC.CODE_ID = M.ORDER_TYPE\n");
        if (!"".equals(dealerId)) {
            sql.append(" and m.dealer_id=" + dealerId + "");
        }
        if (!"".equals(transId)) {
            sql.append(" and m.trans_code like '%").append(transId).append("%'");
        }
        if (!"".equals(TstartDate)) {
            sql.append(" and m.create_date>=to_date('").append(TstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(TendDate)) {
            sql.append(" and m.create_date<=to_date('").append(TendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(state)) {
            sql.append(" and m.state= '").append(state).append("'");
        }
        sql.append("   GROUP BY TD.TRANS_ID,\n");
        sql.append("            TD.PART_ID,\n");
        sql.append("            TD.PART_CNAME,\n");
        sql.append("            TD.PART_OLDCODE,\n");
        sql.append("            TD.PART_CODE,\n");
        sql.append("            TD.MIN_PACKAGE,\n");
        sql.append("            TD.UNIT,\n");
        sql.append("            TC.CODE_DESC,\n");
        sql.append("            M.TRANS_CODE,\n");
        sql.append("            M.ORDER_CODE,\n");
        sql.append("            M.SO_CODE,\n");
        sql.append("            M.CREATE_DATE)\n");
        sql.append("SELECT D.*, SP.SALE_PRICE1, SP.SALE_PRICE1 * D.TRANS_QTY SALE_AMOUNT\n");
        sql.append("  FROM TT_PART_SALES_PRICE SP, SUM_PART_TRANS D\n");
        sql.append(" WHERE SP.PART_ID = D.PART_ID\n");
        sql.append(" ORDER BY D.CREATE_DATE DESC\n");
        
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * 检查是否需要弹出打印页面
     *
     * @return
     */
    public boolean checkShowExpPrint(String inId) {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();


        TtPartInstockExceptionLogPO exceptionLogPO = new TtPartInstockExceptionLogPO();
        exceptionLogPO.setInId(Long.valueOf(inId));

        if (select(exceptionLogPO).size() > 0) {
            return true;
        }
        return false;

    }

    /**
     * 产生货位
     *
     * @param transId
     * @param whId
     * @return
     */
    public List<Map<String, Object>> getPartLocId(String transId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT PKG_PART.F_GETPARTLOCID(PT.DEALER_ID,\n");
        sql.append("                         (SELECT D.WH_ID\n");
        sql.append("                            FROM TT_PART_WAREHOUSE_DEFINE D\n");
        sql.append("                           WHERE D.ORG_ID = PT.DEALER_ID\n");
        sql.append("                             AND ROWNUM = 1),\n");
        sql.append("                         A.PART_ID) LOCID\n");
        sql.append("  FROM TT_PART_TRANS_DTL A, TT_PART_TRANS PT\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND A.TRANS_ID = PT.TRANS_ID\n");
        sql.append("   AND PT.TRANS_ID = '" + transId + "'\n");

        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

}
