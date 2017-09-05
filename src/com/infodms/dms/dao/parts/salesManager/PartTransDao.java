
package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
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

public class PartTransDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartTransDao.class);
    private static final PartTransDao dao = new PartTransDao();

    private PartTransDao() {
    }

    public static final PartTransDao getInstance() {
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
    public PageResult<Map<String, Object>> queryOutstockOrder(RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
        String startDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));
        String endDate = CommonUtils.checkNull(request.getParamValue("SendDate"));
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));
        String transCode = CommonUtils.checkNull(request.getParamValue("TransCode"));
        String isChkIn = CommonUtils.checkNull(request.getParamValue("isChkIn"));
        String transFlag = CommonUtils.checkNull(request.getParamValue("TransFlag"));//发运单打印标识
        String partTrans = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE"));//运单类型

        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT OM.PICK_ORDER_ID,\n");
        sql.append("       S.TRANS_ID,");
//        sql.append("       S.TRANSPORT_ORG TRANS_ORG,\n");//20170818 update start
//        sql.append("       S.TRANS_TYPE ORDER_TRANS_TYPE,\n");
        sql.append("       (select logi_name from TT_SALES_LOGI sl where sl.logi_code=s.TRANSPORT_ORG and STATUS=10011001) TRANS_ORG,");
        sql.append("       (select tv_name from TT_TRANSPORT_VALUATION tv where tv.tv_Id=s.TRANS_TYPE  and STATUS=10011001) ORDER_TRANS_TYPE,");//20170818 update end
        sql.append("       OM.DEALER_CODE,\n");
        sql.append("       OM.DEALER_NAME,\n");
        sql.append("       S.CREATE_DATE TRANS_DATE,\n");
        sql.append("       S.TRANS_CODE LOGISTICS_NO,\n");
        sql.append("       SUM(OD.OUTSTOCK_QTY) OUTSTOCK_QTY,\n");
        sql.append("       OM.AMOUNT SALE_AMOUNT,\n");
        sql.append("       T1.NAME CREATE_BY_NAME,\n");
        sql.append("       PK.PKG_NUM,\n");
        sql.append("       S.CREATE_DATE,\n");
        sql.append("       PK.WEIGHT,\n");
        sql.append("       /* A.PKG_BY,\n");
        sql.append("       A.CHECK_PICK_BY,*/\n");
        sql.append("       W.WH_NAME,\n");
        sql.append("       S.IS_CHECK,\n");
        sql.append("       NVL(S.PRINT_NUM, 0) PRINT_NUM\n");
        sql.append("  FROM TT_PART_OUTSTOCK_MAIN OM,\n");
        sql.append("       TT_PART_OUTSTOCK_DTL OD,\n");
        sql.append("       TT_PART_TRANS S,\n");
        sql.append("       TC_USER T1,\n");
        sql.append("       TT_PART_WAREHOUSE_DEFINE W,\n");
        sql.append("       (SELECT BD.OUT_ID, COUNT(*) PKG_NUM, SUM(BD.CH_WEIGHT) WEIGHT\n");
        sql.append("          FROM TT_PART_PKG_BOX_DTL BD\n");
        sql.append("         WHERE BD.OUT_ID IS NOT NULL\n");
        sql.append("         GROUP BY BD.OUT_ID) PK\n");
        sql.append(" WHERE OM.OUT_ID = OD.OUT_ID\n");
        sql.append("   AND OM.OUT_ID = S.OUT_ID\n");
        sql.append("   AND OM.OUT_ID = PK.OUT_ID\n");
        sql.append("   AND OM.CREATE_BY = T1.USER_ID\n");
        sql.append("   AND OM.WH_ID = W.WH_ID\n");

        if (!"".equals(pickOrderId)) {
            sql.append(" AND UPPER(OM.PICK_ORDER_ID) LIKE '%").append(pickOrderId.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" AND OM.DEALER_NAME LIKE '%").append(dealerName.trim()).append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" AND UPPER(OM.DEALER_CODE) LIKE '%").append(dealerCode.trim().toUpperCase()).append("%'");
        }
        if (!"".equals(startDate)) {
            sql.append(" AND OM.CREATE_DATE>=to_date('").append(startDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(endDate)) {
            sql.append(" AND OM.CREATE_DATE<=to_date('").append(endDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(transCode)) {
            sql.append(" AND upper(S.TRANS_CODE) like upper('%").append(transCode).append("%')");
        }
        if (!"".equals(whId)) {
            sql.append(" AND OM.WH_ID = '").append(whId).append("'");
        }
        if (!"".equals(isChkIn)) {
            sql.append(" AND S.is_Check = '").append(isChkIn).append("'");
        }
        if (!"".equals(partTrans)) {
            sql.append(" AND s.TRANS_TYPE = '").append(partTrans).append("'");
        }
        
       /* if(transFlag.equals(Constant.PART_BASE_FLAG_YES+"")){
            sql.append(" and  SM.TRANS_PRINT_NUM>0 ");
        }else if(transFlag.equals(Constant.PART_BASE_FLAG_NO+"")){
            sql.append(" and  SM.TRANS_PRINT_NUM=0 ");
        }
        */
        sql.append(" AND OM.SELLER_ID = '").append(dealerId).append("'");
//		sql.append(" AND A.STATE IN ('").append(Constant.CAR_FACTORY_TRANS_STATE_01).append("')");
        sql.append("GROUP BY OM.PICK_ORDER_ID,\n");
        sql.append("         S.TRANSPORT_ORG,\n");
        sql.append("         S.TRANS_TYPE,\n");
        sql.append("         OM.DEALER_CODE,\n");
        sql.append("         OM.DEALER_NAME,\n");
        sql.append("         S.CREATE_DATE,\n");
        sql.append("         S.TRANS_CODE,\n");
        sql.append("         OM.AMOUNT,\n");
        sql.append("         T1.NAME,\n");
        sql.append("         PK.PKG_NUM,\n");
        sql.append("         S.CREATE_DATE,\n");
        sql.append("         PK.WEIGHT,\n");
        sql.append("         W.WH_NAME,\n");
        sql.append("         S.IS_CHECK,\n");
        sql.append("         S.TRANS_ID,");
        sql.append("         S.PRINT_NUM\n");
        sql.append("ORDER BY S.CREATE_DATE DESC\n");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-9-9
     * @Title : 明细查询
     */
    public PageResult<Map<String, Object>> queryOrderDtl(RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId2"));
        String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));
        String partName = CommonUtils.checkNull(request.getParamValue("partName"));
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
        String whId = CommonUtils.checkNull(request.getParamValue("whId2"));
//		String partTrans = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE"));
        String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate2"));
        String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate2"));
        String isForce = CommonUtils.checkNull(request.getParamValue("isForce"));//是否强制出库

        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT GP.PICK_ORDER_ID,\n");
        sql.append("       MAX(GP.CREATE_DATE) AS CREATE_DATE,\n");
        sql.append("       GP.PART_OLDCODE,\n");
        sql.append("       GP.PART_CODE,\n");
        sql.append("       GP.PART_CNAME,\n");
        sql.append("       SUM(GP.OUTSTOCK_QTY) AS OUTSTOCK_QTY,\n");
        sql.append("       GP.LOC_CODE,\n");
        sql.append("       TO_CHAR(MAX(GP.SALE_PRICE), 'FM999,999,990.00') AS SALE_PRICE,\n");
        sql.append("       TO_CHAR(SUM(GP.SALE_AMOUNT), 'FM999,999,990.00') AS SALE_AMOUNT,\n");
        sql.append("       GP.WH_NAME,\n");
        sql.append("       GP.FIX_NAME,\n");
        sql.append("       GP.PKG_BY,\n");
        sql.append("       GP.CHECK_PICK_BY\n");
        sql.append("  FROM (SELECT SM.PICK_ORDER_ID,\n");
        sql.append("               SM.PICK_ORDER_CREATE_DATE CREATE_DATE,\n");
        sql.append("               SD.PART_OLDCODE,\n");
        sql.append("               SD.PART_CODE,\n");
        sql.append("               SD.PART_CNAME,\n");
        sql.append("               NVL(OD.OUTSTOCK_QTY, '0') AS OUTSTOCK_QTY,\n");
        sql.append("               LD.LOC_CODE,\n");
        sql.append("               NVL(OD.SALE_PRICE, '0') AS SALE_PRICE,\n");
        sql.append("               NVL(OD.SALE_AMOUNT, '0') AS SALE_AMOUNT,\n");
        sql.append("               WD.WH_NAME,\n");
        sql.append("               FD.FIX_NAME,\n");
        sql.append("               OM.PKG_BY,\n");
        sql.append("               OM.CHECK_PICK_BY\n");
        sql.append("          FROM TT_PART_SO_MAIN          SM,\n");
        sql.append("               TT_PART_SO_DTL           SD,\n");
        sql.append("               TT_PART_OUTSTOCK_DTL     OD,\n");
        sql.append("               TT_PART_WAREHOUSE_DEFINE WD,\n");
        sql.append("               TT_PART_LOACTION_DEFINE  LD,\n");
        sql.append("               TT_PART_FIXCODE_DEFINE   FD,\n");
        sql.append("               TT_PART_PICK_ORDER_MAIN  OM\n");
        sql.append("         WHERE SM.SO_ID = SD.SO_ID\n");
        sql.append("           AND SM.PICK_ORDER_ID = OM.PICK_ORDER_ID\n");
        sql.append("           AND FD.FIX_VALUE = SM.TRANS_TYPE\n");
        sql.append("           AND SD.PART_ID = OD.PART_ID(+)\n");
        sql.append("           AND SD.SO_ID = OD.SO_ID(+)\n");
        sql.append("           AND SM.WH_ID = WD.WH_ID\n");
        sql.append("           AND SM.WH_ID = LD.WH_ID\n");
        sql.append("           AND SD.PART_ID = LD.PART_ID\n");
        sql.append("\t\t\t\t\t AND sm.seller_id=2010010100070674\n");
        sql.append("           AND FD.FIX_GOUPTYPE = '92251004'\n");
        sql.append("AND SM.SELLER_ID = ").append(dealerId);
        if (!"".equals(pickOrderId)) {
            sql.append(" AND UPPER(SM.PICK_ORDER_ID) LIKE '%").append(pickOrderId.trim().toUpperCase()).append("%' ");
        }
        if (!"".equals(partOldcode)) {
            sql.append(" AND UPPER(SD.PART_OLDCODE) LIKE '%").append(partOldcode.trim().toUpperCase()).append("%' ");
        }
        if (!"".equals(partName)) {
            sql.append(" AND SD.PART_CNAME LIKE '%").append(partName.trim()).append("%' ");
        }
        if (!"".equals(partCode)) {
            sql.append(" AND UPPER(SD.PART_CODE) LIKE '%").append(partCode.trim().toUpperCase()).append("%' ");
        }

        if (!"".equals(whId)) {
            sql.append(" AND SM.WH_ID = '").append(whId).append("' ");
        }
       /* if(!"".equals(partTrans)){
            sql.append(" AND SM.trans_type=").append(partTrans);
        }*/
        if (!"".equals(SstartDate)) {
            sql.append(" AND OM.CREATE_DATE>=to_date('").append(SstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(SendDate)) {
            sql.append(" AND OM.CREATE_DATE<=to_date('").append(SendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(isForce)) {
            if ("1".equals(isForce)) {
                sql.append("AND SM.STATE = 92401009\n");
            }

        }
        sql.append(" ) GP ");


        sql.append("GROUP BY GP.PICK_ORDER_ID,\n");
        sql.append("         GP.PART_OLDCODE,\n");
        sql.append("         GP.PART_CODE,\n");
        sql.append("         GP.PART_CNAME,\n");
        sql.append("         GP.LOC_CODE,\n");
        sql.append("         GP.WH_NAME,\n");
        sql.append("         GP.FIX_NAME,\n");
        sql.append("         GP.PKG_BY,\n");
        sql.append("         GP.CHECK_PICK_BY\n");
        sql.append(" ORDER BY MAX(GP.CREATE_DATE) DESC, GP.PICK_ORDER_ID DESC, GP.LOC_CODE ASC ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-9-9
     * @Title : 发运明细List
     */
    public List<Map<String, Object>> orderDtlList(RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId2"));
        String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));
        String partName = CommonUtils.checkNull(request.getParamValue("partName"));
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
        String whId = CommonUtils.checkNull(request.getParamValue("whId2"));
        String partTrans = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE"));
        String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate2"));
        String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate2"));

        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT GP.PICK_ORDER_ID,\n");
        sql.append("       MAX(GP.CREATE_DATE) AS CREATE_DATE,\n");
        sql.append("       GP.PART_OLDCODE,\n");
        sql.append("       GP.PART_CODE,\n");
        sql.append("       GP.PART_CNAME,\n");
        sql.append("       SUM(GP.OUTSTOCK_QTY) AS OUTSTOCK_QTY,\n");
        sql.append("       GP.LOC_CODE,\n");
        sql.append("       TO_CHAR(MAX(GP.SALE_PRICE), 'FM999,999,990.00') AS SALE_PRICE,\n");
        sql.append("       TO_CHAR(SUM(GP.SALE_AMOUNT), 'FM999,999,990.00') AS SALE_AMOUNT,\n");
        sql.append("       GP.WH_NAME,\n");
        sql.append("       GP.FIX_NAME,\n");
        sql.append("       GP.PKG_BY,\n");
        sql.append("       GP.CHECK_PICK_BY\n");
        sql.append("  FROM (SELECT SM.PICK_ORDER_ID,\n");
        sql.append("               SM.PICK_ORDER_CREATE_DATE CREATE_DATE,\n");
        sql.append("               SD.PART_OLDCODE,\n");
        sql.append("               SD.PART_CODE,\n");
        sql.append("               SD.PART_CNAME,\n");
        sql.append("               NVL(OD.OUTSTOCK_QTY, '0') AS OUTSTOCK_QTY,\n");
        sql.append("               LD.LOC_CODE,\n");
        sql.append("               NVL(OD.SALE_PRICE, '0') AS SALE_PRICE,\n");
        sql.append("               NVL(OD.SALE_AMOUNT, '0') AS SALE_AMOUNT,\n");
        sql.append("               WD.WH_NAME,\n");
        sql.append("               FD.FIX_NAME,\n");
        sql.append("               OM.PKG_BY,\n");
        sql.append("               OM.CHECK_PICK_BY\n");
        sql.append("          FROM TT_PART_SO_MAIN          SM,\n");
        sql.append("               TT_PART_SO_DTL           SD,\n");
        sql.append("               TT_PART_OUTSTOCK_DTL     OD,\n");
        sql.append("               TT_PART_WAREHOUSE_DEFINE WD,\n");
        sql.append("               TT_PART_LOACTION_DEFINE  LD,\n");
        sql.append("               TT_PART_FIXCODE_DEFINE   FD,\n");
        sql.append("               TT_PART_PICK_ORDER_MAIN  OM\n");
        sql.append("         WHERE SM.SO_ID = SD.SO_ID\n");
        sql.append("           AND SM.PICK_ORDER_ID = OM.PICK_ORDER_ID\n");
        sql.append("           AND FD.FIX_VALUE = SM.TRANS_TYPE\n");
        sql.append("           AND SD.PART_ID = OD.PART_ID(+)\n");
        sql.append("           AND SD.SO_ID = OD.SO_ID(+)\n");
        sql.append("           AND SM.WH_ID = WD.WH_ID\n");
        sql.append("           AND SM.WH_ID = LD.WH_ID\n");
        sql.append("           AND SD.PART_ID = LD.PART_ID\n");
        sql.append("\t\t\t\t\t AND sm.seller_id=2010010100070674\n");
        sql.append("           AND FD.FIX_GOUPTYPE = '92251004'\n");
        sql.append("AND SM.SELLER_ID = ").append(dealerId);
        if (!"".equals(pickOrderId)) {
            sql.append(" AND UPPER(SM.PICK_ORDER_ID) LIKE '%").append(pickOrderId.trim().toUpperCase()).append("%' ");
        }
        if (!"".equals(partOldcode)) {
            sql.append(" AND UPPER(SD.PART_OLDCODE) LIKE '%").append(partOldcode.trim().toUpperCase()).append("%' ");
        }
        if (!"".equals(partName)) {
            sql.append(" AND SD.PART_CNAME LIKE '%").append(partName.trim()).append("%' ");
        }
        if (!"".equals(partCode)) {
            sql.append(" AND UPPER(SD.PART_CODE) LIKE '%").append(partCode.trim().toUpperCase()).append("%' ");
        }

        if (!"".equals(whId)) {
            sql.append(" AND SM.WH_ID = '").append(whId).append("' ");
        }
        if (!"".equals(partTrans)) {
            sql.append(" AND SM.trans_type=").append(partTrans);
        }

        if (!CommonUtils.isNullString(SstartDate)) {
            sql.append("   AND OM.CREATE_DATE >= TO_DATE('" + SstartDate + " 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        }
        if (!CommonUtils.isNullString(SendDate)) {
            sql.append("   AND OM.CREATE_DATE <= TO_DATE('" + SendDate + " 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        }
        sql.append(" ) GP ");


        sql.append("GROUP BY GP.PICK_ORDER_ID,\n");
        sql.append("         GP.PART_OLDCODE,\n");
        sql.append("         GP.PART_CODE,\n");
        sql.append("         GP.PART_CNAME,\n");
        sql.append("         GP.LOC_CODE,\n");
        sql.append("         GP.WH_NAME,\n");
        sql.append("         GP.FIX_NAME,\n");
        sql.append("         GP.PKG_BY,\n");
        sql.append("         GP.CHECK_PICK_BY\n");
        sql.append(" ORDER BY MAX(GP.CREATE_DATE) DESC, GP.PICK_ORDER_ID DESC, GP.LOC_CODE ASC ");

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
     * @Description: 查询主表信息
     */
    public Map<String, Object> getOutStockMain(String outId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.* ");
        sql.append(" ,(select NAME from TC_USER b where a.CREATE_BY=b.USER_ID) as CREATE_BY_NAME ");
        sql.append(" ,(select nvl(t.wh_name,'') as wh_name from TT_PART_WAREHOUSE_DEFINE t where t.WH_ID=a.wh_id) as WH_NAME ");
        sql.append(" from tt_part_outstock_main a ");
        sql.append(" where 1=1 ");
        sql.append(" and out_id='").append(outId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return new HashMap();
        }
        return list.get(0);
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询详细
     */
    public List<Map<String, Object>> getOutStockDetail(String outId) {
        ActionContext act = ActionContext.getContext();
        Map<String, Object> map = (Map<String, Object>) act.getOutData("mainMap");
        StringBuffer sql = new StringBuffer();
        /*sql.append(" select a.*,b.LOC_NAME,c.PKG_TYPE,c.PKG_QTY from tt_part_outstock_dtl a ");
        sql.append(" left join TT_PART_LOACTION_DEFINE b on a.part_id=b.part_id and b.STATE='").append(Constant.STATUS_ENABLE).append("' and b.WH_ID='");
		sql.append(CommonUtils.checkNull(map.get("WH_ID"))).append("'");
		sql.append(" left join TT_PART_PKG_DTL c on c.so_id=a.so_id and a.part_id=c.part_id ");
		sql.append(" where 1=1 ");
		sql.append(" and out_id ='").append(outId).append("'");*/
        sql.append(" select a.*,b.LOC_NAME,(select sum(ITEM_QTY) from vw_part_stock t where t.WH_ID=(select WH_ID from tt_part_outstock_main b where b.OUT_ID=a.OUT_ID) and t.PART_ID=a.PART_ID) as NORMAL_QTY_NOW from tt_part_outstock_dtl a ");
        sql.append(" left join TT_PART_LOACTION_DEFINE b on a.part_id=b.part_id and b.STATE='").append(Constant.STATUS_ENABLE).append("' and b.WH_ID='");
        sql.append(CommonUtils.checkNull(map.get("WH_ID"))).append("'");
        sql.append(" where 1=1 ");
        sql.append(" and out_id ='").append(outId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 获取订单信息
     */
    public Map<String, Object> getOrderMainInfo(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.*,(select NAME from TC_USER t where a.CREATE_BY=t.USER_ID) as CREATE_BY_NAME from  tt_part_dlr_order_main a ");
        sql.append(" where 1=1 ");
        sql.append(" and a.ORDER_ID in ( ");
        sql.append(" select order_id from tt_part_so_main b where b.so_id='").append(soId).append("'");
        sql.append(")");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return new HashMap();
        }
        return list.get(0);
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 获取订单信息
     */
    public Map<String, Object> getOrderMainInfo2(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select za_concat(b.order_id) order_id,za_concat(b.order_code) order_code from tt_part_so_main b where b.pick_order_id='").append(pickOrderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return new HashMap();
        }
        return list.get(0);
    }

    /**
     * @param : @param req
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 获取订单详细信息
     */
    public List<Map<String, Object>> getDetail(String soId, String outId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_SO_DTL ");
        sql.append(" where 1=1 ");
        sql.append(" and SO_ID='").append(soId).append("'");
        sql.append(" and part_id in ( ");
        sql.append(" select part_id from tt_part_outstock_dtl where out_id='").append(outId).append("')");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
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
    public Map<String, Object> queryOutstockDetail(String outlineId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_OUTSTOCK_DTL ");
        sql.append(" where 1=1 ");
        sql.append(" and OUTLINE_ID='").append(outlineId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return new HashMap();
        }
        return list.get(0);
    }

    public List<Map<String, Object>> getSoMain(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_so_main where pick_order_id='").append(pickOrderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0) {
            return new ArrayList<Map<String, Object>>();
        }
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public Map<String, Object> getOutQtyAndOutAmount(String soIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" nvl(sum(SALE_AMOUNT),'0') as SALE_AMOUNT,nvl(sum(OUTSTOCK_QTY),'0') as OUTSTOCK_QTY ");
        sql.append(" from tt_part_outstock_dtl ");
        sql.append(" where 1=1 ");
        sql.append(" and so_id in (").append(soIds).append(")");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public String getWhName(String whId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(t.wh_name,'') as wh_name from TT_PART_WAREHOUSE_DEFINE t where t.WH_ID='");
        sql.append(whId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null) {
            return "";
        }
        return CommonUtils.checkNull(list.get(0).get("WH_NAME"));
    }

    public String getUserName(String id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tc_user where user_id='").append(id).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null) {
            return "";
        }
        return CommonUtils.checkNull(list.get(0).get("NAME"));
    }

    public Map<String, Object> getOutMain(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select T.*, TO_CHAR(T.CREATE_DATE,'yyyy-MM-dd hh24:mm:ss') AS CREATE_DATE_FM from tt_part_outstock_main T ");
        sql.append(" where so_id='").append(soId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public Map<String, Object> getTransMain(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_TRANS ");
        sql.append(" where so_id='").append(soId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public PageResult<Map<String, Object>> queryMessages(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT * FROM TT_PART_FIXCODE_DEFINE T WHERE 1 = 1 ");
        sql.append(" AND T.FIX_GOUPTYPE = 92251007 AND T.STATE = 10011001 ORDER BY T.FIX_GOUPTYPE, T.SORT_NO ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> getWareHouse(String orgId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT Distinct d.wh_id,d.wh_name\n");
        sql.append("  FROM TT_PART_WAREHOUSE_DEFINE D\n");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND D.ORG_ID = '" + orgId + "' ");

        List<Map<String, Object>> wareHoustList = this.pageQuery(sql.toString(), null, this.getFunName());
        return wareHoustList;
    }
    
    
    /**
	 * 查询需要补录的发运信息
	 * @param request
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getTransInfoMakeUp(String dealerId,RequestWrapper request, Integer curPage, Integer pageSize) throws Exception{
		String transCode = request.getParamValue("TRANS_CODE");
		String orderCode = request.getParamValue("ORDER_CODE");
		String state = request.getParamValue("STATE");
		String sStartDate = request.getParamValue("EstartDate");
		String eEndDdate = request.getParamValue("EendDate");

		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sbSql.append("SELECT * FROM TT_PART_TRANS TPS\n");
		sbSql.append("WHERE TPS.ORDER_TYPE<>?\n");
		params.add(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04);
		sbSql.append("  AND TPS.SELLER_ID = ?\n");
		params.add(dealerId);
		sbSql.append(" AND to_char(TPS.CREATE_DATE,'YYYY-MM-DD')>='"+sStartDate+"'");
		sbSql.append(" AND to_char(TPS.CREATE_DATE,'YYYY-MM-DD')<='"+eEndDdate+"'");
		
		if(StringUtil.notNull(transCode)){
			sbSql.append("  AND TPS.TRANS_CODE LIKE ?\n");
			params.add("%"+transCode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(orderCode)){
			sbSql.append("  AND TPS.ORDER_CODE LIKE ?\n");
			params.add("%"+orderCode.toUpperCase()+"%");
		}
		if("0".equals(state)){
			//sbSql.append("  AND TPS.UPDATE_DATE IS NULL ");//请选择
		}else if("1".equals(state)){
			sbSql.append("  AND TPS.UPDATE_DATE IS NULL \n");//未补录
		}else if("2".equals(state)){
			sbSql.append("  AND TPS.UPDATE_DATE IS NOT NULL \n");//已补录
		}
		sbSql.append(" ORDER BY CREATE_DATE DESC\n");
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
        return ps;
	}
    
	/**
	 * 根据条件查询补录信息写入excel
	 * @param request
	 * @param dealerId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTransByDealerId(RequestWrapper request, String dealerId)  throws Exception{
		String transCode = request.getParamValue("TRANS_CODE");
		String orderCode = request.getParamValue("ORDER_CODE");
		String state = request.getParamValue("STATE");
		String sStartDate = request.getParamValue("EstartDate");
		String eEndDdate = request.getParamValue("EendDate");
		
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sbSql.append("SELECT TPS.trans_code, \n");
		sbSql.append(" (select tv_name from tt_transport_valuation tv where tv.tv_id=tps.trans_type ) Trans_Type,\n");
		sbSql.append(" (select logi_name from TT_SALES_LOGI ts where ts.logi_code=tps.transport_org ) transport_org,\n");
		sbSql.append(" TPS.Wuliu_Code,\n");
		sbSql.append(" TPS.Ac_Amount,\n");
		sbSql.append(" TPS.Remark2 \n");
		sbSql.append(" FROM TT_PART_TRANS TPS\n");
		sbSql.append("	WHERE TPS.ORDER_TYPE<>?\n");
		params.add(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04);
		sbSql.append("  AND TPS.SELLER_ID = ?\n");
		params.add(dealerId);
		sbSql.append(" AND to_char(TPS.CREATE_DATE,'YYYY-MM-DD')>='"+sStartDate+"'");
		sbSql.append(" AND to_char(TPS.CREATE_DATE,'YYYY-MM-DD')<='"+eEndDdate+"'");
		
		if(StringUtil.notNull(transCode)){
			sbSql.append("  AND TPS.TRANS_CODE LIKE ?\n");
			params.add("%"+transCode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(orderCode)){
			sbSql.append("  AND TPS.ORDER_CODE LIKE ?\n");
			params.add("%"+orderCode.toUpperCase()+"%");
		}
		if("0".equals(state)){
			//sbSql.append("  AND TPS.UPDATE_DATE IS NULL ");//请选择
		}else if("1".equals(state)){
			sbSql.append("  AND TPS.UPDATE_DATE IS NULL \n");//未补录
		}else if("2".equals(state)){
			sbSql.append("  AND TPS.UPDATE_DATE IS NOT NULL \n");//已补录
		}
		
		sbSql.append(" ORDER BY CREATE_DATE DESC\n");
		
		List<Map<String, Object>> ps = pageQuery(sbSql.toString(), params, getFunName());
		return ps;
	}
	/**
	 * 根据transCode获取发运信息
	 * @param subCell
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> validateTransInfoByTransCode(String subCell)  throws Exception{
		List<Object> params = new ArrayList<Object>();
		String sql = "SELECT TPS.TRANS_ID,TPS.OUT_ID FROM TT_PART_TRANS TPS WHERE TPS.TRANS_CODE=?";
		params.add(subCell);
		Map<String, Object> map = pageQueryMap(sql, params, getFunName());
		return map;
	}
	/**
	 * 验证发运方式是否存在
	 * @param subCell
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> validateTransType(String subCell) throws Exception{
		List<Object> params = new ArrayList<Object>();
//		String sql = "SELECT TPFD.FIX_NAME FROM TT_PART_FIXCODE_DEFINE TPFD WHERE TPFD.FIX_GOUPTYPE = ? AND TPFD.FIX_NAME = ?";
//		params.add(Constant.FIXCODE_TYPE_04);
//		params.add(subCell);
		String sql = "select tv_id,tv_name from TT_TRANSPORT_VALUATION where tv_name='"+subCell+"' and status=10011001";
		Map<String, Object> map = pageQueryMap(sql, params, getFunName());
		return map;
	}
	
	/**
	 * 验证承运物流
	 * @param subCell
	 * @param loginUser
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> validateWuliu(String subCell, AclUserBean loginUser) throws Exception{
//		List<OrgBean> beanList =PartWareHouseDao.getInstance().getOrgInfo(loginUser);
//		String orgId = beanList.get(0).getOrgId().toString();  //组织id
//		List<Object> params = new ArrayList<Object>();
//		StringBuilder sql = new StringBuilder();
//		sql.append("SELECT L.LOGI_ID ID, L.LOGI_NAME NAME \n");
//		sql.append("  FROM TT_PART_LOGISTICS L\n");
//		sql.append(" WHERE L.STATUS = 10011001\n");
//		sql.append("   AND L.ORG_ID =? \n");
//		params.add(orgId);
//		sql.append("  AND L.LOGI_NAME = ?\n");
//		params.add(subCell);
		
		List<OrgBean> beanList =PartWareHouseDao.getInstance().getOrgInfo(loginUser);
		String orgId = beanList.get(0).getOrgId().toString();  //组织id
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT L.LOGI_ID,L.LOGI_CODE ID, L.LOGI_NAME NAME \n");
		sql.append("  FROM TT_SALES_LOGI L\n");
		sql.append(" WHERE L.STATUS = 10011001\n");
		sql.append("  AND L.LOGI_NAME ='"+subCell+"'");
		
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), params, getFunName());
		return map;
		
	}
    
    
    
}