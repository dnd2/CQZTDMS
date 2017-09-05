package com.infodms.dms.dao.parts.salesManager.partInStockQueManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : huchao
 *         CreateDate     : 2013-5-14
 * @ClassName : inStockQueDao
 */
public class inStockQueDao extends BaseDao {
    public static Logger logger = Logger.getLogger(inStockQueDao.class);
    private static final inStockQueDao dao = new inStockQueDao();

    private inStockQueDao() {
    }

    public static final inStockQueDao getInstance() {
        return dao;
    }

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-6
     * @Title : 配件入库信息查询
     */
    public PageResult<Map<String, Object>> queryInStockInfos(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        //modify by yuan 20130920 start
        sql.append("SELECT IM.IN_ID,\n");
        sql.append("       IM.IN_CODE,\n");
        sql.append("       IM.ORDER_CODE,\n");
        sql.append("       IM.DEALER_NAME,\n");
        sql.append("       IM.SELLER_NAME,\n");
        sql.append("       IM.SALE_DATE,\n");
        sql.append("       IM.ORDER_TYPE,\n");
        sql.append("       IM.REMARK2,\n");
        sql.append("       (SELECT U.NAME FROM TC_USER U WHERE IM.CREATE_BY = U.USER_ID) NAME\n");
        sql.append("  FROM TT_PART_DLR_INSTOCK_MAIN IM\n");
        sql.append(" WHERE 1 = 1\n");
        //end
        sql.append(sbString);
        sql.append(" ORDER BY IM.IN_CODE DESC ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param dealerName
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回配件入库信息List
     */
    public List<Map<String, Object>> queryInStockInfosList(String sbString) {
        StringBuffer sql = new StringBuffer("");
        //modify by yuan 20130920 start
        sql.append("SELECT IM.IN_ID,\n");
        sql.append("       IM.IN_CODE,\n");
        sql.append("       IM.ORDER_CODE,\n");
        sql.append("       IM.DEALER_NAME,\n");
        sql.append("       IM.SELLER_NAME,\n");
        sql.append("       TO_CHAR(IM.SALE_DATE, 'yyyy-MM-dd') AS SALE_DATE,\n");
        sql.append("       IM.WH_NAME,\n");
        sql.append("       TO_CHAR(IM.ARRIVAL_DATE, 'yyyy-MM-dd') AS ARRIVAL_DATE,\n");
        sql.append("       IM.TRANS_CODE,\n");
        sql.append("       IM.ORDER_TYPE,\n");
        sql.append("       IM.REMARK2,\n");
        sql.append("       (SELECT U.NAME FROM  TC_USER U WHERE IM.CREATE_BY = U.USER_ID) NAME\n");
        sql.append("  FROM TT_PART_DLR_INSTOCK_MAIN IM\n");
        sql.append(" WHERE 1 = 1\n");
        //end
        sql.append(sbString);
        sql.append(" ORDER BY IM.IN_CODE DESC ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-13
     * @Title : 配件入库详细信息查询
     */
    public PageResult<Map<String, Object>> queryInStockDeatil(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT ID.INLINE_ID, ID.PART_CODE, ID.PART_OLDCODE, ID.PART_CNAME, ID.UNIT, ID.MIN_PACKAGE, "
                        + " ID.BUY_QTY, ID.TRANS_QTY, ID.IN_QTY, ID.IN_TYPE, ID.REMARK "
                        + " FROM TT_PART_DLR_INSTOCK_DTL ID ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param oemCompanyId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回主机厂名称
     */
    public String getMainCompanyName(String oemCompanyId) {
        String companyName = "";
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.COMPANY_NAME "
                        + " FROM TM_COMPANY TM "
                        + " WHERE 1 = 1  ");

        sql.append("  AND TM.COMPANY_ID = '" + oemCompanyId + "' ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            companyName = list.get(0).get("COMPANY_NAME").toString();
        }

        return companyName;
    }

    /**
     * @param : @param oemCompanyId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回服务商名称
     */
    public String getDealerName(String oemCompanyId) {
        String companyName = "";
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TD.DEALER_NAME "
                        + " FROM TM_DEALER TD "
                        + " WHERE 1 = 1  ");

        sql.append("  AND TD.DEALER_ID = '" + oemCompanyId + "' ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            companyName = list.get(0).get("DEALER_NAME").toString();
        }

        return companyName;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-8
     * @Title : 返回入库明细LIST
     */
    public List<Map<String, Object>> getPartBODtlList(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM TT_PART_BO_DTL BD ");
        sql.append(" WHERE 1 = 1 AND BD.STATUS = '1' ");
        sql.append(sbString);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public PageResult<Map<String, Object>> queryDetail(RequestWrapper request, int pageSize, int curPage) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String dealerId = "";
        StringBuffer sql = new StringBuffer();
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }

        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));
        String transCode = CommonUtils.checkNull(request.getParamValue("transCode"));
        String inCode = CommonUtils.checkNull(request.getParamValue("inCode"));
        String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
        String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));
        String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
        String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));

        sql.append("SELECT TPDIM.ORDER_CODE,\n");
        sql.append("       TPDIM.ORDER_TYPE,\n");
//        sql.append("       TP.TRPLAN_CODE TRANS_CODE,\n");
        sql.append("       TPT.TRANS_CODE,\n");
        sql.append("       TPDIM.IN_CODE,\n");
        sql.append("       TPDID.PART_OLDCODE,\n");
        sql.append("       TPDID.PART_CNAME,\n");
        sql.append("       TPDID.PART_CODE,\n");
        sql.append("       TPDID.UNIT,\n");
        sql.append("       TO_CHAR(OD.SALE_PRICE, 'FM999,999,999,999,990.00') AS BUY_PRICE,\n");
        sql.append("       TO_CHAR((OD.SALE_PRICE * TPDID.IN_QTY), 'FM999,999,999,999,990.00') AS AMOUNT,\n");
        sql.append("       IN_QTY,\n");
        sql.append("       TPDIM.CREATE_DATE,\n");
        sql.append("       TPDIM.REMARK2\n");
        sql.append("  FROM TT_PART_DLR_INSTOCK_DTL  TPDID,\n");
        sql.append("       TT_PART_DLR_INSTOCK_MAIN TPDIM,\n");
        sql.append("       TT_PART_TRANS            TPT,\n");
        sql.append("       TT_PART_OUTSTOCK_DTL     OD,\n");
        sql.append("       TT_PART_OUTSTOCK_MAIN    OM\n");
//        sql.append("       ,TT_PART_TRANS_PLAN       TP\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND TPDID.IN_ID = TPDIM.IN_ID\n");
        sql.append("   AND TPT.TRANS_ID = TPDIM.TRANS_ID\n");
        sql.append("   AND TPT.OUT_ID = OD.OUT_ID\n");
        sql.append("   AND TPDID.PART_ID = OD.PART_ID\n");
        sql.append("   AND OD.OUT_ID = OM.OUT_ID\n");
//        sql.append("   AND OM.TRPLAN_ID = TP.TRPLAN_ID\n");
        if (!"".equals(orderCode)) {
            sql.append(" and tpdim.order_code like '%").append(orderCode).append("%'");
        }
        if (!"".equals(transCode)) {
            sql.append(" and TP.TRPLAN_CODE like '%").append(transCode).append("%'");
        }
        if (!"".equals(inCode)) {
            sql.append(" and tpdim.in_code like '%").append(inCode).append("%'");
        }
        if (!"".equals(partOldcode)) {
            sql.append(" and  tpdid.part_oldcode like '%").append(partOldcode).append("%'");
        }
        if (!"".equals(partCode)) {
            sql.append(" and  tpdid.part_code like '%").append(partCode).append("%'");
        }
        if (!"".equals(partCname)) {
            sql.append(" and  tpdid.part_Cname like '%").append(partCname).append("%'");
        }
        if (!"".equals(startDate)) {
            sql.append(" and tpdim.CREATE_DATE>= to_date('").append(startDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(endDate)) {
            sql.append(" and tpdim.CREATE_DATE<= to_date('").append(endDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        sql.append(" and tpdim.dealer_id='").append(dealerId).append("'");
        sql.append(" order by tpdim.create_date desc, TPDID.INLINE_ID");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> queryDetailForExport(RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String dealerId = "";
        StringBuffer sql = new StringBuffer();
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }

        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));
        String transCode = CommonUtils.checkNull(request.getParamValue("transCode"));
        String inCode = CommonUtils.checkNull(request.getParamValue("inCode"));
        String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
        String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));
        String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
        String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));

        sql.append("SELECT TPDIM.ORDER_CODE,\n");
        sql.append("       TPDIM.ORDER_TYPE,\n");
        sql.append("       TPDIM.TRANS_CODE,\n");
        sql.append("       TPDIM.IN_CODE,\n");
        sql.append("       TPDID.PART_OLDCODE,\n");
        sql.append("       TPDID.PART_CNAME,\n");
        sql.append("       TPDID.PART_CODE,\n");
        sql.append("       TPDID.UNIT,\n");
        sql.append("       TO_CHAR(OD.SALE_PRICE, 'FM999,999,999,999,990.00') AS BUY_PRICE,\n");
        sql.append("       TO_CHAR((OD.SALE_PRICE * IN_QTY), 'FM999,999,999,999,990.00') AS AMOUNT,\n");
        sql.append("       IN_QTY,\n");
        sql.append("       TPDIM.CREATE_DATE,\n");
        sql.append("       TPDIM.REMARK2\n");
        sql.append("  FROM TT_PART_DLR_INSTOCK_DTL  TPDID,\n");
        sql.append("       TT_PART_DLR_INSTOCK_MAIN TPDIM,\n");
        sql.append("       TT_PART_TRANS TPT,\n");
        sql.append("       TT_PART_OUTSTOCK_DTL od\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND TPDID.IN_ID = TPDIM.IN_ID\n");
        sql.append("   AND TPT.TRANS_ID = TPDIM.TRANS_ID\n");
        sql.append("   AND TPT.OUT_ID = OD.OUT_ID\n");
        sql.append("   AND TPDID.PART_ID =OD.PART_ID");

        if (!"".equals(orderCode)) {
            sql.append(" and tpdim.order_code like '%").append(orderCode).append("%'");
        }
        if (!"".equals(transCode)) {
            sql.append(" and tpdim.trans_code like '%").append(transCode).append("%'");
        }
        if (!"".equals(inCode)) {
            sql.append(" and tpdim.in_code like '%").append(inCode).append("%'");
        }
        if (!"".equals(partOldcode)) {
            sql.append(" and  tpdid.part_oldcode like '%").append(partOldcode).append("%'");
        }
        if (!"".equals(partCode)) {
            sql.append(" and  tpdid.part_code like '%").append(partCode).append("%'");
        }
        if (!"".equals(partCname)) {
            sql.append(" and  tpdid.part_Cname like '%").append(partCname).append("%'");
        }
        if (!"".equals(startDate)) {
            sql.append(" and tpdim.CREATE_DATE>= to_date('").append(startDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(endDate)) {
            sql.append(" and tpdim.CREATE_DATE<= to_date('").append(endDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        sql.append(" and tpdim.dealer_id='").append(dealerId).append("'");
        sql.append(" order by tpdim.create_date ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
}
