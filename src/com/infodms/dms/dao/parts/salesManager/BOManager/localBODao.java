package com.infodms.dms.dao.parts.salesManager.BOManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
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
 *         CreateDate     : 2013-5-10
 * @ClassName : localBODao
 */
public class localBODao extends BaseDao {
    public static Logger logger = Logger.getLogger(localBODao.class);
    private static final localBODao dao = new localBODao();

    private localBODao() {
    }

    public static final localBODao getInstance() {
        return dao;
    }

    private static final int BOType = 2;//现场BO值
    private static final int enableValue = Constant.STATUS_ENABLE;//有效

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
     * @Title : 配件现场BO信息查询
     */
    public PageResult<Map<String, Object>> queryPartLocBOInfos(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT Distinct BD.BOLINE_ID,\n");
        sql.append("       BD.PART_ID,\n");
        sql.append("       BD.PART_CODE,\n");
        sql.append("       BD.PART_OLDCODE,\n");
        sql.append("       BD.PART_CNAME,\n");
        sql.append("       BD.UNIT,\n");
        sql.append("       U.NAME AS U_NAME,\n");
        sql.append("       BD.BO_QTY,\n");
        sql.append("       NVL(BD.LOC_BO_ODDQTY, BO_QTY) AS BO_ODDQTY,\n");
        sql.append("       BD.LOC_CLOSE_QTY AS CLOSE_QTY,\n");
        sql.append("       BD.REMARK,\n");
        sql.append("       BD.LOC_STATUS AS STATUS,\n");
        sql.append("       BD.UPDATE_DATE,\n");
        sql.append("       BM.BO_CODE,\n");
        sql.append("       BM.BO_ID,\n");
        sql.append("       BM.CREATE_DATE AS BM_DATE,\n");
        sql.append("       BM.LOC_STATE AS STATE,\n");
//        sql.append("       SM.SO_ID,\n");
        sql.append("       SM.SO_CODE,\n");
        sql.append("       SM.PICK_ORDER_ID,\n");
        sql.append("       SM.DEALER_NAME,\n");
        sql.append("       WD.WH_NAME,\n");
        sql.append("		BD.BATCH_NO,\n");//20170830 add
        sql.append("		l.loc_id,\n");
        sql.append("		l.loc_code\n");
        sql.append("  FROM TT_PART_BO_DTL           BD,\n");
        sql.append("       TT_PART_BO_MAIN          BM,\n");
        sql.append("       TT_PART_SO_MAIN          SM,\n");
        sql.append("       TT_PART_WAREHOUSE_DEFINE WD,\n");
        sql.append("       TC_USER                  U,\n");
        sql.append("       TT_PART_LOACTION_DEFINE  L\n");
        sql.append(" WHERE BD.BO_ID = BM.BO_ID\n");
        sql.append("   AND BM.BO_TYPE = '2'\n");
        sql.append("   AND BM.PICK_ORDER_ID = SM.PICK_ORDER_ID\n");
        sql.append("   AND SM.WH_ID = WD.WH_ID\n");
        sql.append("   AND BD.LOC_ID = L.LOC_ID\n");
        sql.append("   AND BM.CREATE_BY = U.USER_ID(+)");

        sql.append(sbString);

        sql.append(" ORDER BY BM.CREATE_DATE desc, BM.BO_CODE, WD.WH_NAME, BD.PART_OLDCODE ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param dealerName
     * @param : @param partOldCode
     * @param : @param boStartDate
     * @param : @param boEndDate
     * @param : @param orgId
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-28
     * @Title : 汇总查询
     */
    public PageResult<Map<String, Object>> queryLocBOCntInfos(String dealerName, String partOldCode, String boStartDate, String boEndDate, String orgId, String optionType, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT OMM.DEALER_ID, BMDO.DEALER_NAME, BMDO.SELLER_NAME, BMDO.BO_TOTAL_QTY "
                + " FROM (SELECT DISTINCT DEALER_ID FROM TT_PART_DLR_ORDER_MAIN  WHERE 1 =1  ");
        if (null != dealerName && !"".equals(dealerName)) {
            sql.append(" AND DEALER_NAME LIKE '%" + dealerName + "%' ");
        }
        if ("view".equalsIgnoreCase(optionType)) {
            sql.append(" AND (SELLER_ID = '" + orgId + "' OR DEALER_ID = '" + orgId + "')) OMM, ");
        } else {
            sql.append(" AND SELLER_ID = '" + orgId + "') OMM, ");
        }

        sql.append(" (SELECT OM.DEALER_ID, OM.DEALER_NAME, OM.SELLER_NAME, SUM(BMD.BO_TOTAL_QTY) AS BO_TOTAL_QTY ");
        sql.append(" FROM(SELECT BM.ORDER_ID, BDD.* FROM (SELECT BD.BO_ID, SUM(BD.BO_QTY) AS BO_TOTAL_QTY FROM TT_PART_BO_DTL BD");
        if (null != partOldCode && !"".equals(partOldCode)) {
            sql.append(" , TT_PART_DEFINE TD WHERE TD.PART_ID = BD.PART_ID AND TD.PART_OLDCODE LIKE '%" + partOldCode + "%' ");
        }
        sql.append(" GROUP BY BD.BO_ID) BDD, (SELECT * FROM TT_PART_BO_MAIN WHERE BO_TYPE = '" + BOType + "' ");
        if (null != boStartDate && !"".equals(boStartDate)) {
            sql.append(" AND TO_CHAR(CREATE_DATE,'yyyy-MM-dd') >= '" + boStartDate + "' ");
        }
        if (null != boEndDate && !"".equals(boEndDate)) {
            sql.append(" AND TO_CHAR(CREATE_DATE,'yyyy-MM-dd') <= '" + boEndDate + "' ");
        }
        sql.append(" ) BM WHERE BM.BO_ID = BDD.BO_ID ");
        sql.append(" ) BMD, TT_PART_DLR_ORDER_MAIN OM WHERE BMD.ORDER_ID = OM.ORDER_ID  GROUP BY (OM.DEALER_ID, OM.DEALER_NAME, OM.SELLER_NAME)) BMDO");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND OMM.DEALER_ID = BMDO.DEALER_ID ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }


    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回仓库信息List
     */
    public List<Map<String, Object>> getWareHouses(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.WH_ID, TM.WH_NAME AS WH_CNAME "
                        + " FROM TT_PART_WAREHOUSE_DEFINE TM "
                        + " WHERE 1 = 1  ");
        sql.append(" AND TM.STATE = '" + enableValue + "' ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.WH_ID ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param dealerName
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回配件现场BO信息List
     */
    public List<Map<String, Object>> queryPartLocBOInfosList(String sbString) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT BD.BOLINE_ID, BD.PART_ID, BD.PART_CODE, BD.PART_OLDCODE, BD.PART_CNAME, BD.UNIT, U.NAME AS U_NAME, ");
        sql.append(" BD.BO_QTY, NVL(BD.LOC_BO_ODDQTY, BO_QTY) AS BO_ODDQTY, BD.LOC_CLOSE_QTY AS CLOSE_QTY, BD.REMARK, BD.LOC_STATUS AS STATUS, TO_CHAR(BD.UPDATE_DATE,'yyyy-MM-dd hh24:mm:ss') AS UPDATE_DATE, ");
        sql.append(" BM.BO_CODE, BM.BO_ID, TO_CHAR(BM.CREATE_DATE,'yyyy-MM-dd hh24:mm:ss') AS BM_DATE, BM.LOC_STATE AS STATE, SM.SO_ID, SM.SO_CODE, SM.DEALER_NAME, WD.WH_NAME ");
        sql.append(" FROM TT_PART_BO_DTL BD, TT_PART_BO_MAIN BM, TT_PART_SO_MAIN SM, TT_PART_WAREHOUSE_DEFINE WD, TC_USER U ");
        sql.append(" WHERE BD.BO_ID = BM.BO_ID ");
        sql.append(" AND BM.BO_TYPE = '" + BOType + "' ");
        sql.append(" AND BM.SO_ID = SM.SO_ID ");
        sql.append(" AND SM.WH_ID = WD.WH_ID ");
        sql.append(" AND BD.UPDATE_BY = U.USER_ID(+) ");

        sql.append(sbString);

        sql.append(" ORDER BY BM.CREATE_DATE, BM.BO_CODE, WD.WH_NAME, BD.PART_OLDCODE ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param dealerName
     * @param : @param partOldCode
     * @param : @param boStartDate
     * @param : @param boEndDate
     * @param : @param orgId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-28
     * @Title : 汇总查询List
     */
    public List<Map<String, Object>> queryLocBOCntInfos(String dealerName, String partOldCode, String boStartDate, String boEndDate, String orgId, String optionType) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT OMM.DEALER_ID, BMDO.DEALER_NAME, BMDO.SELLER_NAME, BMDO.BO_TOTAL_QTY "
                + " FROM (SELECT DISTINCT DEALER_ID FROM TT_PART_DLR_ORDER_MAIN  WHERE 1 =1  ");
        if (null != dealerName && !"".equals(dealerName)) {
            sql.append(" AND DEALER_NAME LIKE '%" + dealerName + "%' ");
        }
        if ("view".equalsIgnoreCase(optionType)) {
            sql.append(" AND (SELLER_ID = '" + orgId + "' OR DEALER_ID = '" + orgId + "')) OMM, ");
        } else {
            sql.append(" AND SELLER_ID = '" + orgId + "') OMM, ");
        }

        sql.append(" (SELECT OM.DEALER_ID, OM.DEALER_NAME, OM.SELLER_NAME, SUM(BMD.BO_TOTAL_QTY) AS BO_TOTAL_QTY ");
        sql.append(" FROM(SELECT BM.ORDER_ID, BDD.* FROM (SELECT BD.BO_ID, SUM(BD.BO_QTY) AS BO_TOTAL_QTY FROM TT_PART_BO_DTL BD");
        if (null != partOldCode && !"".equals(partOldCode)) {
            sql.append(" , TT_PART_DEFINE TD WHERE TD.PART_ID = BD.PART_ID AND TD.PART_OLDCODE LIKE '%" + partOldCode + "%' ");
        }
        sql.append(" GROUP BY BD.BO_ID) BDD, (SELECT * FROM TT_PART_BO_MAIN WHERE BO_TYPE = '" + BOType + "' ");
        if (null != boStartDate && !"".equals(boStartDate)) {
            sql.append(" AND TO_CHAR(CREATE_DATE,'yyyy-MM-dd') >= '" + boStartDate + "' ");
        }
        if (null != boEndDate && !"".equals(boEndDate)) {
            sql.append(" AND TO_CHAR(CREATE_DATE,'yyyy-MM-dd') <= '" + boEndDate + "' ");
        }
        sql.append(" ) BM WHERE BM.BO_ID = BDD.BO_ID ");
        sql.append(" ) BMD, TT_PART_DLR_ORDER_MAIN OM WHERE BMD.ORDER_ID = OM.ORDER_ID  GROUP BY (OM.DEALER_ID, OM.DEALER_NAME, OM.SELLER_NAME)) BMDO");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND OMM.DEALER_ID = BMDO.DEALER_ID ");

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
     * @Title : 配件现场BO详细信息查询
     */
    public PageResult<Map<String, Object>> queryPartBODeatil(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT BD.BOLINE_ID, BD.BO_ID, BD.PART_ID, BD.PART_CODE, BD.PART_OLDCODE, BD.PART_CNAME, BD.UNIT, "
                        + " BD.BUY_PRICE, BD.BUY_QTY, TO_CHAR((BD.BUY_QTY * BD.BUY_PRICE),'999,999,990.99') AS BUY_SUM, "
                        + " BD.SALES_QTY, TO_CHAR((BD.SALES_QTY * BD.BUY_PRICE),'999,999,990.99') AS SALES_SUM, "
                        + " BD.BO_QTY, TO_CHAR((BD.BO_QTY * BD.BUY_PRICE),'999,999,990.99') AS BO_SUM, BD.REMARK, BD.LOC_STATUS AS STATUS "
                        + " FROM TT_PART_BO_DTL BD ");
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
     * @Title : 返回现场BO明细LIST
     */
    public List<Map<String, Object>> getPartBODtlList(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT BD.*, NVL(BD.LOC_BO_ODDQTY, BD.BO_QTY) AS LOC_BO_ODDQTY_FM FROM TT_PART_BO_DTL BD ");
        sql.append(" WHERE 1 = 1 AND BD.LOC_STATUS = '1' ");
        sql.append(sbString);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-9-27
     * @Title : 获取仓库，货位信息
     */
    public List<Map<String, Object>> getWHLocInfos(String locId) {
        StringBuffer sql = new StringBuffer("");

/*		sql.append("SELECT WD.WH_ID, ");
        sql.append(" WD.WH_CODE, ");
		sql.append(" WD.WH_NAME, ");
		sql.append(" LD.LOC_ID, ");
		sql.append(" LD.LOC_CODE, ");
		sql.append(" LD.LOC_NAME ");
		sql.append(" FROM TT_PART_SO_MAIN SM, TT_PART_BO_MAIN BM,  ");
		sql.append(" TT_PART_WAREHOUSE_DEFINE WD, TT_PART_LOACTION_DEFINE LD ");
		sql.append(" WHERE BM.SO_ID = SM.SO_ID ");
		sql.append(" AND SM.WH_ID = WD.WH_ID ");
		sql.append(" AND WD.WH_ID = LD.WH_ID ");
		sql.append(sbString);*/

        sql.append("SELECT LD.LOC_ID, LD.LOC_CODE, WD.WH_ID, WD.WH_NAME, WD.WH_CODE\n");
        sql.append("  FROM TT_PART_LOACTION_DEFINE LD, TT_PART_WAREHOUSE_DEFINE WD\n");
        sql.append(" WHERE LD.WH_ID = WD.WH_ID\n");
        sql.append("   AND LD.LOC_ID = " + locId);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * 查询未审核的现场BO
     *
     * @param request
     * @param pageSize
     * @param curPage
     * @return
     */
    public PageResult<Map<String, Object>> queryPartLocBOInfos(RequestWrapper request, int pageSize, int curPage) {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
        String orgId = "";
        if (logonUser.getDealerId() == null) {
            orgId = logonUser.getCompanyId() + "";
        } else {
            orgId = logonUser.getDealerId();
        }
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT *\n");
        sql.append("  FROM VW_PART_BO_XC X\n");
        sql.append(" WHERE 1 = 1\n");
        if (!"".equals(soCode) && null != soCode) {
            sql.append("   AND X.SO_CODE LIKE '%" + soCode.toUpperCase() + "%'\n");
        }
        if (!"".equals(orderCode) && null != orderCode) {
            sql.append("   AND X.ORDER_CODE LIKE '%" + orderCode.toUpperCase() + "%'\n");
        }
        if (!"".equals(dealerName) && null != dealerName) {
            sql.append("   AND X.DEALER_NAME LIKE '%" + dealerName + "%'");
        }
        if (!"".equals(pickOrderId) && null != pickOrderId) {
            sql.append("   AND X.PICK_ORDER_ID LIKE '%" + pickOrderId + "%'");
        }
        sql.append(" AND SELLER_ID=" + orgId + "\n");
        sql.append(" ORDER BY x.PKG_OVER_DATE DESC");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> queryPartLocBODtlQuery(RequestWrapper request, int pageSize, int curPage) {
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT TPD.PART_OLDCODE,\n");
        sql.append("       TPD.PART_CNAME,\n");
        sql.append("       PD.SALES_QTY,\n");
        sql.append("       PD.PKG_QTY,\n");
        sql.append("       PD.LOC_BO_QTY,\n");
        sql.append("       PD.BATCH_NO,\n");//20170830 add
        sql.append("       L.LOC_NAME\n");
        sql.append("  FROM TT_PART_PKG_DTL PD, TT_PART_DEFINE TPD, TT_PART_LOACTION_DEFINE L\n");
        sql.append(" WHERE PD.PART_ID = TPD.PART_ID\n");
        sql.append("   AND PD.LOC_ID = L.LOC_ID\n");
        sql.append("   AND PD.PKG_NO IS NULL");
        if (!"".equals(pickOrderId) && null != pickOrderId) {
            sql.append("   AND PD.PICK_ORDER_ID = '" + pickOrderId + "'");
        }

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * 更新现场BO状态为已确认
     *
     * @param pickOrderId 拣货单ID
     */
    public void updateBoDtl(String pickOrderId, AclUserBean logonUser) {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE TT_PART_PKG_DTL PD\n");
        sql.append("   SET PD.IS_CHECK   = 10041001,\n");
        sql.append("       PD.CHECK_BY   = '" + logonUser.getUserId() + "',\n");
        sql.append("       PD.CHECK_DATE = SYSDATE\n");
        sql.append(" WHERE PD.PICK_ORDER_ID = '" + pickOrderId + "'\n");
        sql.append("   AND PD.IS_CHECK = 10041002\n");
        sql.append("   AND PD.PKG_NO IS NULL");

        dao.update(sql.toString(), null);
    }
}
