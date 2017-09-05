package com.infodms.dms.dao.parts.baseManager.partBaseQuery.partFCStockQuery;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : huchao
 *         CreateDate     : 2013-5-3
 * @ClassName : partFCStockDao
 */
public class partFCStockDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partFCStockDao.class);
    private static final partFCStockDao dao = new partFCStockDao();

    private partFCStockDao() {
    }

    public static final partFCStockDao getInstance() {
        return dao;
    }

    private static final int type1 = Constant.PART_STOCK_INVE_TYPE_01;
    private static final int type2 = Constant.PART_STOCK_INVE_TYPE_02;
    private static final int enableValue = Constant.STATUS_ENABLE;//有效


    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
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
        sql.append(" ORDER BY TM.WH_ID");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回仓库配件库存信息
     */
    public PageResult<Map<String, Object>> showPartStockBase(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        //获取经销商名称
        /*SELECT TD.*, nvl(dl.dealer_name,(select cp.company_name from tm_company cp where td.ORG_ID = cp.company_id)) AS dealer_name FROM VW_PART_STOCK TD, tm_dealer dl
        WHERE 1 = 1
		AND TD.ORG_ID = DL.DEALER_ID(+)*/

        sql.append(" SELECT TD.*, DL.DEALER_NAME, DL.DEALER_CODE, ");
        sql.append(" CASE ");
        sql.append(" WHEN TD.ORG_ID = '" + Constant.OEM_ACTIVITIES + "' THEN ");
        sql.append(" (SELECT TO_CHAR(S.SALE_PRICE3, 'FM999,999,990.00') ");
        sql.append(" FROM TT_PART_SALES_PRICE S ");
        sql.append(" WHERE S.PART_ID = TD.PART_ID) ");
        sql.append(" WHEN TD.ORG_ID != '" + Constant.OEM_ACTIVITIES + "' THEN ");
        sql.append(" TO_CHAR(PKG_PART.F_GETPRICE(DL.DEALER_ID, TD.PART_ID), 'FM999,999,990.00') ");
        sql.append(" END AS PRICE, ");
        sql.append(" (NVL((SELECT SUM(SD.RETURN_QTY - SD.COLSE_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SM.STATE = '1' ");
        sql.append(" AND SD.STATUS = '1' ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') )PTFC, ");
        sql.append(" (NVL((SELECT SUM(SD.RETURN_QTY - SD.COLSE_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SM.STATE = '1' ");
        sql.append(" AND SD.STATUS = '1' ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_04 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') )LHCW, ");
        sql.append(" (NVL((SELECT SUM(SD.RETURN_QTY - SD.COLSE_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SM.STATE = '1' ");
        sql.append(" AND SD.STATUS = '1' ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_05 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') )ZLWT, ");
        sql.append(" (NVL((SELECT SUM(SD.RETURN_QTY - SD.COLSE_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SM.STATE = '1' ");
        sql.append(" AND SD.STATUS = '1' ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_06 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') )JTCL, ");
        sql.append(" (NVL((SELECT SUM(SD.RETURN_QTY - SD.COLSE_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SM.STATE = '1' ");
        sql.append(" AND SD.STATUS = '1' ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_07 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') ");
        sql.append(" + ");
        sql.append(" NVL((SELECT SUM(NVL(BD.LOC_BO_ODDQTY, BD.BO_QTY)) ");
        sql.append(" FROM TT_PART_BO_MAIN BM, TT_PART_BO_DTL BD, TT_PART_SO_MAIN SM ");
        sql.append(" WHERE BM.BO_ID = BD.BO_ID ");
        sql.append(" AND BM.BO_TYPE = 2 ");
        sql.append(" AND BM.LOC_STATE IN ('" + Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_01 + "','" + Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_02 + "') ");
        sql.append(" AND BM.SO_ID = SM.SO_ID ");
        sql.append(" AND SM.SELLER_ID = TD.ORG_ID ");
        sql.append(" AND BD.PART_ID = TD.PART_ID), '0') ");
        sql.append(" ) XCBO ");
        sql.append(" FROM VW_PART_STOCK TD, TM_DEALER DL ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.ORG_ID = DL.DEALER_ID(+) ");
        sql.append(sbString);
        sql.append(" AND TD.FC_QTY > 0 ");
        // sql.append(" ORDER BY DL.DEALER_CODE, TD.WH_NAME, TD.PART_OLDCODE, TD.PART_CNAME, TD.PART_CODE ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-29
     * @Title : 返回盘点封存详情
     */
    public PageResult<Map<String, Object>> showPartPDStockDT(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT R.* ");
        sql.append(" FROM TT_PART_RECORD R ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);
        sql.append(" ORDER BY R.CREATE_DATE DESC ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> showPartZYStockDT(RequestWrapper request, String OrgId, int pageSize, int curPage) {

        String partId = CommonUtils.checkNull(request.getParamValue("partId")); // 配件ID
        String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
        String zyCode = CommonUtils.checkNull(request.getParamValue("zyCode")); // 占用单号
        String zyType = CommonUtils.checkNull(request.getParamValue("zyType")); //占用类型
        String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 封存开始时间
        String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 封存截止时间

        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT ZY.* FROM ( ");

        //销售单占用明细
        sql.append(" SELECT SM.SO_CODE AS BILL_CODE, SM.CREATE_DATE, SDD.PART_CODE, SDD.PART_OLDCODE, SDD.PART_CNAME, SDD.SALES_QTY AS PRV_NUM, LD.LOC_NAME, SDD.PRV_TYPE ");
        sql.append(" FROM TT_PART_SO_MAIN SM, ");
        sql.append(" (SELECT SD.SLINE_ID, SD.SO_ID, SD.PART_ID, ");
        sql.append(" SD.PART_CODE,SD.PART_OLDCODE, SD.PART_CNAME, SD.SALES_QTY, '销售业务' AS PRV_TYPE");
        sql.append(" FROM TT_PART_SO_DTL SD WHERE SD.PART_ID = '" + partId + "') SDD, ");
        sql.append(" TT_PART_LOACTION_DEFINE LD ");
        sql.append(" WHERE SM.SELLER_ID = '" + OrgId + "' ");
        sql.append(" AND SM.SO_ID = SDD.SO_ID ");
        sql.append(" AND SM.WH_ID = '" + whId + "' ");
        sql.append(" AND SM.WH_ID = LD.WH_ID ");
        sql.append(" AND SDD.PART_ID = LD.PART_ID ");
        sql.append(" AND SM.STATE IN ('" + Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "','" + Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "'");
        sql.append(",'" + Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "','" + Constant.CAR_FACTORY_PKG_STATE_01 + "'");
        sql.append(",'" + Constant.CAR_FACTORY_PKG_STATE_02 + "') ");

        sql.append(" UNION ");

        //零售领用占用明细 已OK
        sql.append(" SELECT RM.RETAIL_CODE AS BILL_CODE, RM.CREATE_DATE, RDD.PART_CODE, RDD.PART_OLDCODE, RDD.PART_CNAME, RDD.R_ACOUNT AS PRV_NUM, LD.LOC_NAME, RDD.PRV_TYPE ");
        sql.append(" FROM TT_PART_RETAIL_MAIN RM, ");
        sql.append(" (SELECT RD.DTL_ID, RD.RETAL_ID, RD.PART_ID,RD.PART_CODE, RD.PART_OLDCODE,RD.PART_CNAME, (RD.QTY - NVL(RD.OUT_QTY,'0')) AS R_ACOUNT, '零售/领用' AS PRV_TYPE ");
        sql.append(" FROM TT_PART_RETAIL_DTL RD WHERE RD.PART_ID = '" + partId + "') RDD, ");
        sql.append(" TT_PART_LOACTION_DEFINE LD ");
        sql.append(" WHERE RM.STATE = '" + Constant.PART_RESALE_RECEIVE_ORDER_TYPE_02 + "' ");
        sql.append(" AND RM.SORG_ID = '" + OrgId + "' ");
        sql.append(" AND RM.RETAIL_ID = RDD.RETAL_ID ");
        sql.append(" AND RM.WH_ID = LD.WH_ID ");
        sql.append(" AND RM.WH_ID = '" + whId + "' ");
        sql.append(" AND RDD.PART_ID = LD.PART_ID ");

        sql.append(" UNION ");

        //销售退货提交占用明细
        sql.append(" SELECT RM.RETURN_CODE AS BILL_CODE, ");
        sql.append(" RM.CREATE_DATE, ");
        sql.append(" RDD.PART_CODE, ");
        sql.append(" RDD.PART_OLDCODE, ");
        sql.append(" RDD.PART_CNAME, ");
        sql.append(" RDD.PRV_NUM, ");
        sql.append(" LD.LOC_NAME, ");
        sql.append(" RDD.PRV_TYPE ");
        sql.append(" FROM TT_PART_DLR_RETURN_MAIN RM, ");
        sql.append(" (SELECT RD.RETURN_ID, RD.DTL_ID, RD.PART_ID, RD.PART_CODE, RD.PART_OLDCODE, ");
        sql.append(" RD.PART_CNAME, RD.APPLY_QTY AS PRV_NUM, '销售退货' AS PRV_TYPE ");
        sql.append(" FROM TT_PART_DLR_RETURN_DTL RD, TT_PART_DLR_RETURN_MAIN RMM ");
        sql.append(" WHERE RD.PART_ID = '" + partId + "' ");
        sql.append(" AND RD.RETURN_ID = RMM.RETURN_ID ");
        sql.append(" AND RMM.STATE = '" + Constant.PART_DLR_RETURN_STATUS_02 + "' ) RDD, ");
        sql.append(" TT_PART_LOACTION_DEFINE LD ");
        sql.append(" WHERE RM.STATE = '" + Constant.PART_DLR_RETURN_STATUS_02 + "' ");
        sql.append(" AND RM.DEALER_ID = '" + OrgId + "' ");
        sql.append(" AND RM.STOCK_OUT = '" + whId + "' ");
        sql.append(" AND RM.RETURN_ID = RDD.RETURN_ID ");
        sql.append(" AND RM.STOCK_OUT = LD.WH_ID ");
        sql.append(" AND RDD.PART_ID = LD.PART_ID ");

        sql.append(" UNION ");

        //销售退货出库中占用明细
        sql.append(" SELECT RM.RETURN_CODE AS BILL_CODE, ");
        sql.append(" RM.CREATE_DATE, ");
        sql.append(" RDD.PART_CODE, ");
        sql.append(" RDD.PART_OLDCODE, ");
        sql.append(" RDD.PART_CNAME, ");
        sql.append(" RDD.PRV_NUM, ");
        sql.append(" LD.LOC_NAME, ");
        sql.append(" RDD.PRV_TYPE ");
        sql.append(" FROM TT_PART_DLR_RETURN_MAIN RM, ");
        sql.append(" (SELECT RD.RETURN_ID, RD.DTL_ID, RD.PART_ID, RD.PART_CODE, RD.PART_OLDCODE, ");
        sql.append(" RD.PART_CNAME, (RD.CHECK_QTY - NVL(RD.RETURN_QTY,'0')) AS PRV_NUM, '销售退货' AS PRV_TYPE ");
        sql.append(" FROM TT_PART_DLR_RETURN_DTL RD, TT_PART_DLR_RETURN_MAIN RMM ");
        sql.append(" WHERE RD.PART_ID = '" + partId + "' ");
        sql.append(" AND RD.RETURN_ID = RMM.RETURN_ID ");
        sql.append(" AND RMM.STATE = '" + Constant.PART_DLR_RETURN_STATUS_05 + "' ) RDD, ");
        sql.append(" TT_PART_LOACTION_DEFINE LD ");
        sql.append(" WHERE RM.STATE = '" + Constant.PART_DLR_RETURN_STATUS_05 + "' ");
        sql.append(" AND RM.DEALER_ID = '" + OrgId + "' ");
        sql.append(" AND RM.STOCK_OUT = '" + whId + "' ");
        sql.append(" AND RM.RETURN_ID = RDD.RETURN_ID ");
        sql.append(" AND RM.STOCK_OUT = LD.WH_ID ");
        sql.append(" AND RDD.PART_ID = LD.PART_ID ");

        sql.append(" UNION ");

        //采购退货提交占用明细
        sql.append(" SELECT RM.RETURN_CODE AS BILL_CODE, ");
        sql.append(" RM.CREATE_DATE, ");
        sql.append(" RDD.PART_CODE, ");
        sql.append(" RDD.PART_OLDCODE, ");
        sql.append(" RDD.PART_CNAME, ");
        sql.append(" RDD.PRV_NUM, ");
        sql.append(" LD.LOC_NAME, ");
        sql.append(" RDD.PRV_TYPE ");
        sql.append(" FROM TT_PART_OEM_RETURN_MAIN RM, ");
        sql.append(" (SELECT RD.RETURN_ID, RD.DTL_ID, RD.PART_ID, RD.PART_CODE, RD.PART_OLDCODE, RD.STOCK_OUT, ");
        sql.append(" RD.PART_CNAME, RD.APPLY_QTY AS PRV_NUM, '采购退货' AS PRV_TYPE ");
        sql.append(" FROM TT_PART_OEM_RETURN_DTL RD, TT_PART_OEM_RETURN_MAIN RMM ");
        sql.append(" WHERE RD.PART_ID = '" + partId + "' ");
        sql.append(" AND RD.RETURN_ID = RMM.RETURN_ID ");
        sql.append(" AND RMM.STATE = '" + Constant.PART_OEM_RETURN_STATUS_01 + "' ) RDD, ");
        sql.append(" TT_PART_LOACTION_DEFINE LD ");
        sql.append(" WHERE RM.STATE = '" + Constant.PART_OEM_RETURN_STATUS_01 + "' ");
        sql.append(" AND RM.ORG_ID = '" + OrgId + "' ");
        sql.append(" AND RM.RETURN_ID = RDD.RETURN_ID ");
        sql.append(" AND RDD.STOCK_OUT = '" + whId + "' ");
        sql.append(" AND RDD.STOCK_OUT = LD.WH_ID ");
        sql.append(" AND RDD.PART_ID = LD.PART_ID ");

        sql.append(" UNION ");

        //采购退货出库中占用明细
        sql.append(" SELECT RM.RETURN_CODE AS BILL_CODE, ");
        sql.append(" RM.CREATE_DATE, ");
        sql.append(" RDD.PART_CODE, ");
        sql.append(" RDD.PART_OLDCODE, ");
        sql.append(" RDD.PART_CNAME, ");
        sql.append(" RDD.PRV_NUM, ");
        sql.append(" LD.LOC_NAME, ");
        sql.append(" RDD.PRV_TYPE ");
        sql.append(" FROM TT_PART_OEM_RETURN_MAIN RM, ");
        sql.append(" (SELECT RD.RETURN_ID, RD.DTL_ID, RD.PART_ID, RD.PART_CODE, RD.PART_OLDCODE, RD.STOCK_OUT, ");
        sql.append(" RD.PART_CNAME, (RD.CHECK_QTY - NVL(RD.RETURN_QTY,'0')) AS PRV_NUM, '采购退货' AS PRV_TYPE ");
        sql.append(" FROM TT_PART_OEM_RETURN_DTL RD, TT_PART_OEM_RETURN_MAIN RMM ");
        sql.append(" WHERE RD.PART_ID = '" + partId + "' ");
        sql.append(" AND RD.RETURN_ID = RMM.RETURN_ID ");
        sql.append(" AND RMM.STATE = '" + Constant.PART_OEM_RETURN_STATUS_03 + "' ) RDD, ");
        sql.append(" TT_PART_LOACTION_DEFINE LD ");
        sql.append(" WHERE RM.STATE = '" + Constant.PART_OEM_RETURN_STATUS_03 + "' ");
        sql.append(" AND RM.ORG_ID = '" + OrgId + "' ");
        sql.append(" AND RM.RETURN_ID = RDD.RETURN_ID ");
        sql.append(" AND RDD.STOCK_OUT = '" + whId + "' ");
        sql.append(" AND RDD.STOCK_OUT = LD.WH_ID ");
        sql.append(" AND RDD.PART_ID = LD.PART_ID ");

        sql.append(" UNION ");

        //配件拆件占用明细
        sql.append(" SELECT RM.SPCPD_CODE AS BILL_CODE, ");
        sql.append(" RM.CREATE_DATE, ");
        sql.append(" RM.PART_CODE, ");
        sql.append(" RM.PART_OLDCODE, ");
        sql.append(" RM.PART_NAME AS PART_CNAME, ");
        sql.append(" RM.QTY AS PRV_NUM, ");
        sql.append(" LD.LOC_NAME, ");
        sql.append(" '拆/合件' AS PRV_TYPE ");
        sql.append(" FROM TT_PART_SPCP_MAIN RM, ");
        sql.append(" TT_PART_LOACTION_DEFINE LD ");
        sql.append(" WHERE RM.SPCPD_TYPE = '" + Constant.PART_SPCPD_TYPE_01 + "' ");
        sql.append(" AND RM.LOC_ID = LD.LOC_ID ");
        sql.append(" AND RM.PART_ID = '" + partId + "' ");
        sql.append(" AND RM.WH_ID = '" + whId + "'  ");
//		sql.append(" AND RM.ORG_ID = '"+ OrgId +"' ");
        sql.append(" AND RM.STATE IN ('" + Constant.PART_SPCPD_STATUS_01 + "','" + Constant.PART_SPCPD_STATUS_03 + "') ");

        sql.append(" UNION ");

        //配件合件占用明细
        sql.append(" SELECT RM.SPCPD_CODE AS BILL_CODE, ");
        sql.append(" RD.CREATE_DATE, ");
        sql.append(" RD.PART_CODE, ");
        sql.append(" RD.PART_OLDCODE, ");
        sql.append(" RD.PART_CNAME, ");
        sql.append(" RD.QTY AS PRV_NUM, ");
        sql.append(" LD.LOC_NAME, ");
        sql.append(" RM.PRV_TYPE ");
        sql.append(" FROM TT_PART_SPLIT_COMPOUND_DTL RD, ");
        sql.append(" (SELECT SPCPD_ID, SPCPD_CODE, '拆/合件' AS PRV_TYPE ");
        sql.append(" FROM TT_PART_SPCP_MAIN ");
        sql.append(" WHERE SPCPD_TYPE = '" + Constant.PART_SPCPD_TYPE_02 + "' ");
        sql.append(" AND STATE IN ('" + Constant.PART_SPCPD_STATUS_01 + "','" + Constant.PART_SPCPD_STATUS_03 + "') ) RM, ");
        sql.append(" TT_PART_LOACTION_DEFINE LD ");
        sql.append(" WHERE RD.SPCPD_ID = RM.SPCPD_ID ");
        sql.append(" AND LD.WH_ID = '" + whId + "' ");
        sql.append(" AND RD.LOC_ID = LD.LOC_ID ");
        sql.append(" AND RD.PART_ID = LD.PART_ID ");
        sql.append(" AND RD.PART_ID = '" + partId + "' ");

        sql.append(" ) ZY ");
        sql.append(" WHERE 1 = 1 ");

        if (null != zyCode && !"".equals(zyCode)) {
            sql.append(" AND UPPER(ZY.BILL_CODE) LIKE '%" + zyCode.toUpperCase() + "%' ");
        }
        if (null != checkSDate && !"".equals(checkSDate)) {
            sql.append(" AND TO_CHAR(ZY.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
        }
        if (null != checkEDate && !"".equals(checkEDate)) {
            sql.append(" AND TO_CHAR(ZY.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
        }
        if (null != zyType && !"".equals(zyType)) {
            sql.append(" AND ZY.PRV_TYPE LIKE '%" + zyType + "%' ");
        }

        sql.append(" ORDER BY ZY.CREATE_DATE DESC ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-11
     * @Title : 返回配件入库详情
     */
    public PageResult<Map<String, Object>> showPartInStockDT(String sbString, String checkCode, String inCode, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        /*sql.append(" SELECT R.*, PI.IN_CODE, PI.CHECK_CODE, PI.VENDER_NAME, ");
    	sql.append(" TO_CHAR(PI.BUY_PRICE,'FM999,999,990.00') AS BUY_PRICE, TO_CHAR(PI.IN_AMOUNT,'FM999,999,990.00') AS IN_AMOUNT ");
    	sql.append(" FROM TT_PART_RECORD R, TT_PART_PO_IN PI ");
    	sql.append(" WHERE 1 = 1 ");
    	sql.append(" AND R.ORDER_ID = PI.IN_ID ");
    	sql.append(sbString);*/

        sql.append(" SELECT INN.* FROM ( ");
    	
    	/*总部入库单*/
        sql.append(" SELECT R.RECORD_ID, ");
        sql.append(" R.PART_ID, ");
        sql.append(" R.PART_CODE, ");
        sql.append(" R.PART_OLDCODE, ");
        sql.append(" R.PART_NAME, ");
        sql.append(" R.PART_NUM, ");
        sql.append(" R.CREATE_DATE, ");
        sql.append(" R.LOC_NAME, ");
        sql.append(" PI.IN_CODE, ");
        sql.append(" PI.CHECK_CODE, ");
        sql.append(" PI.VENDER_NAME, ");
        sql.append(" TO_CHAR(PI.BUY_PRICE, 'FM999,999,990.00') AS BUY_PRICE, ");
        sql.append(" TO_CHAR(PI.IN_AMOUNT, 'FM999,999,990.00') AS IN_AMOUNT, ");
        sql.append(" '采购入库' AS OUT_TYPE ");
        sql.append(" FROM TT_PART_RECORD R, TT_PART_PO_IN PI ");
        sql.append(" WHERE R.ORDER_ID = PI.IN_ID ");
        sql.append(sbString);

        if (null != checkCode && !"".equals(checkCode)) {
            sql.append(" AND UPPER(PI.CHECK_CODE) LIKE '%" + checkCode.toUpperCase() + "%' ");
        }
        if (null != inCode && !"".equals(inCode)) {
            sql.append(" AND UPPER(PI.IN_CODE) LIKE '%" + inCode.toUpperCase() + "%' ");
        }

        sql.append(" UNION  ");
    	
    	/*销售退货入库*/
        sql.append(" SELECT R.RECORD_ID, ");
        sql.append(" R.PART_ID, ");
        sql.append(" R.PART_CODE, ");
        sql.append(" R.PART_OLDCODE, ");
        sql.append(" R.PART_NAME, ");
        sql.append(" R.PART_NUM, ");
        sql.append(" R.CREATE_DATE, ");
        sql.append(" R.LOC_NAME, ");
        sql.append(" RM.RETURN_CODE AS IN_CODE, ");
        sql.append(" RM.RETURN_CODE AS CHECK_CODE, ");
        sql.append(" RM.SELLER_NAME AS VENDER_NAME, ");
        sql.append(" TO_CHAR(NVL(RD.BUY_PRICE,'0'), 'FM999,999,990.00') AS BUY_PRICE, ");
        sql.append(" TO_CHAR((R.PART_NUM * NVL(RD.BUY_PRICE,'0')), 'FM999,999,990.00') AS IN_AMOUNT, ");
        sql.append(" '销售退货' AS OUT_TYPE ");
        sql.append(" FROM TT_PART_RECORD R, TT_PART_DLR_RETURN_MAIN RM, tt_part_dlr_return_dtl RD ");
        sql.append(" WHERE R.ORDER_ID = RM.RETURN_ID ");
        sql.append(" AND RM.RETURN_ID = RD.RETURN_ID ");
        sql.append(" AND R.PART_ID = RD.PART_ID ");

        sql.append(sbString);

        if (null != checkCode && !"".equals(checkCode)) {
            sql.append(" AND UPPER(RM.RETURN_CODE) LIKE '%" + checkCode.toUpperCase() + "%' ");
        }
        if (null != inCode && !"".equals(inCode)) {
            sql.append(" AND UPPER(RM.RETURN_CODE) LIKE '%" + inCode.toUpperCase() + "%' ");
        }

        sql.append(" UNION ");
    	
    	/*配件拆合件*/
        sql.append(" SELECT R.RECORD_ID, ");
        sql.append(" R.PART_ID, ");
        sql.append(" R.PART_CODE, ");
        sql.append(" R.PART_OLDCODE, ");
        sql.append(" R.PART_NAME, ");
        sql.append(" R.PART_NUM, ");
        sql.append(" R.CREATE_DATE, ");
        sql.append(" R.LOC_NAME, ");
        sql.append(" RM.SPCPD_CODE AS IN_CODE, ");
        sql.append(" RM.SPCPD_CODE AS CHECK_CODE, ");
        sql.append(" RD.VENDER_NAME, ");
        sql.append(" TO_CHAR(NVL(RD.PART_COST,'0'), 'FM999,999,990.00') AS BUY_PRICE, ");
        sql.append(" TO_CHAR((R.PART_NUM * NVL(RD.PART_COST,'0')), 'FM999,999,990.00') AS IN_AMOUNT, ");
        sql.append(" '拆/合件' AS OUT_TYPE ");
        sql.append(" FROM TT_PART_RECORD R, TT_PART_SPCP_MAIN RM, tt_part_split_compound_dtl RD ");
        sql.append(" WHERE R.ORDER_ID = RM.SPCPD_ID ");
        sql.append(" AND RM.SPCPD_ID = RD.SPCPD_ID ");
        sql.append(" AND R.PART_ID = RD.PART_ID ");
        sql.append(" AND RM.SPCPD_TYPE = '92481001' ");/*拆件*/

        sql.append(sbString);

        if (null != checkCode && !"".equals(checkCode)) {
            sql.append(" AND UPPER(RM.SPCPD_CODE) LIKE '%" + checkCode.toUpperCase() + "%' ");
        }
        if (null != inCode && !"".equals(inCode)) {
            sql.append(" AND UPPER(RM.SPCPD_CODE) LIKE '%" + inCode.toUpperCase() + "%' ");
        }

        sql.append(" UNION ");

        sql.append(" SELECT R.RECORD_ID, ");
        sql.append(" R.PART_ID, ");
        sql.append(" R.PART_CODE, ");
        sql.append(" R.PART_OLDCODE, ");
        sql.append(" R.PART_NAME, ");
        sql.append(" R.PART_NUM, ");
        sql.append(" R.CREATE_DATE, ");
        sql.append(" R.LOC_NAME, ");
        sql.append(" RM.SPCPD_CODE AS IN_CODE, ");
        sql.append(" RM.SPCPD_CODE AS CHECK_CODE, ");
        sql.append(" RM.ORG_CNAME AS VENDER_NAME, ");
        sql.append(" TO_CHAR(nvl(RM.PART_COST,'0'), 'FM999,999,990.00') AS SALE_PRICE, ");
        sql.append(" TO_CHAR((R.PART_NUM * nvl(RM.PART_COST,'0')), 'FM999,999,990.00') AS SALE_AMOUNT, ");
        sql.append(" '拆/合件' AS OUT_TYPE ");
        sql.append(" FROM TT_PART_RECORD R, TT_PART_SPCP_MAIN RM ");
        sql.append(" WHERE R.ORDER_ID = RM.SPCPD_ID ");
        sql.append(" AND R.PART_ID = RM.PART_ID ");
        sql.append(" AND RM.SPCPD_TYPE = '92481002' ");/*合件*/

        sql.append(sbString);

        if (null != checkCode && !"".equals(checkCode)) {
            sql.append(" AND UPPER(RM.SPCPD_CODE) LIKE '%" + checkCode.toUpperCase() + "%' ");
        }
        if (null != inCode && !"".equals(inCode)) {
            sql.append(" AND UPPER(RM.SPCPD_CODE) LIKE '%" + inCode.toUpperCase() + "%' ");
        }

        sql.append(" UNION ");
    	
    	/*服务商采购入库*/
        sql.append(" SELECT R.RECORD_ID, ");
        sql.append(" R.PART_ID, ");
        sql.append(" R.PART_CODE, ");
        sql.append(" R.PART_OLDCODE, ");
        sql.append(" R.PART_NAME, ");
        sql.append(" R.PART_NUM, ");
        sql.append(" R.CREATE_DATE, ");
        sql.append(" R.LOC_NAME, ");
        sql.append(" RM.IN_CODE, ");
        sql.append(" RM.TRANS_CODE AS CHECK_CODE, ");
        sql.append(" RM.SELLER_NAME AS VENDER_NAME, ");
        sql.append(" TO_CHAR(NVL(SD.BUY_PRICE,'0'), 'FM999,999,990.00') AS BUY_PRICE, ");
        sql.append(" TO_CHAR((R.PART_NUM * NVL(SD.BUY_PRICE,'0')), 'FM999,999,990.00') AS IN_AMOUNT, ");
        sql.append(" '采购入库' AS OUT_TYPE ");
        sql.append(" FROM TT_PART_RECORD R, TT_PART_DLR_INSTOCK_MAIN RM, TT_PART_SO_MAIN SM, TT_PART_SO_DTL SD ");
        sql.append(" WHERE R.ORDER_ID = RM.IN_ID ");
        sql.append(" AND RM.SO_ID = SM.SO_ID ");
        sql.append(" AND SM.SO_ID = SD.SO_ID ");
        sql.append(" AND R.PART_ID = SD.PART_ID  ");

        sql.append(sbString);

        if (null != checkCode && !"".equals(checkCode)) {
            sql.append(" AND UPPER(RM.IN_CODE) LIKE '%" + checkCode.toUpperCase() + "%' ");
        }
        if (null != inCode && !"".equals(inCode)) {
            sql.append(" AND UPPER(RM.TRANS_CODE) LIKE '%" + inCode.toUpperCase() + "%' ");
        }

        sql.append(" UNION ");
    	
    	/*杂项入库*/
        sql.append(" SELECT R.RECORD_ID, ");
        sql.append(" R.PART_ID, ");
        sql.append(" R.PART_CODE, ");
        sql.append(" R.PART_OLDCODE, ");
        sql.append(" R.PART_NAME, ");
        sql.append(" R.PART_NUM, ");
        sql.append(" R.CREATE_DATE, ");
        sql.append(" R.LOC_NAME, ");
        sql.append(" RM.ORDER_CODE AS IN_CODE, ");
        sql.append(" RM.ORDER_CODE AS CHECK_CODE, ");
        sql.append(" RM.SELLER_NAME AS VENDER_NAME, ");
        sql.append(" TO_CHAR(NVL(RD.BUY_PRICE,'0'), 'FM999,999,990.00') AS BUY_PRICE, ");
        sql.append(" TO_CHAR((R.PART_NUM * NVL(RD.BUY_PRICE,'0')), 'FM999,999,990.00') AS IN_AMOUNT, ");
        sql.append(" '杂项入库' AS OUT_TYPE ");
        sql.append(" FROM TT_PART_RECORD R, tt_part_dlr_order_main RM,  tt_part_dlr_order_dtl RD ");
        sql.append(" WHERE R.ORDER_ID = RM.ORDER_ID ");
        sql.append(" AND RM.ORDER_ID = RD.ORDER_ID ");
        sql.append(" AND R.PART_ID = RD.PART_ID ");

        sql.append(sbString);

        if (null != checkCode && !"".equals(checkCode)) {
            sql.append(" AND UPPER(RM.ORDER_CODE) LIKE '%" + checkCode.toUpperCase() + "%' ");
        }
        if (null != inCode && !"".equals(inCode)) {
            sql.append(" AND UPPER(RM.ORDER_CODE) LIKE '%" + inCode.toUpperCase() + "%' ");
        }

        sql.append(" UNION ");
    	
    	/*盘盈入库*/
        sql.append(" SELECT R.RECORD_ID, ");
        sql.append(" R.PART_ID, ");
        sql.append(" R.PART_CODE, ");
        sql.append(" R.PART_OLDCODE, ");
        sql.append(" R.PART_NAME, ");
        sql.append(" R.PART_NUM, ");
        sql.append(" R.CREATE_DATE, ");
        sql.append(" R.LOC_NAME, ");
        sql.append(" RM.CHANGE_CODE AS IN_CODE, ");
        sql.append(" RM.CHANGE_CODE AS CHECK_CODE, ");
        sql.append(" RM.CHGORG_CNAME AS VENDER_NAME, ");
        sql.append(" TO_CHAR('0', 'FM999,999,990.00') AS BUY_PRICE, ");
        sql.append(" TO_CHAR('0','FM999,999,990.00') AS IN_AMOUNT, ");
        sql.append(" '盘盈入库' AS OUT_TYPE ");
        sql.append(" FROM TT_PART_RECORD R, ");
        sql.append(" TT_PART_CHG_STATE_MAIN RM, ");
        sql.append(" TT_PART_CHG_STATE_DTL RD ");
        sql.append(" WHERE R.ORDER_ID = RM.CHANGE_ID ");
        sql.append(" AND RM.CHG_TYPE = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01 + "' ");
        sql.append(" AND RM.CHANGE_ID = RD.CHANGE_ID ");
        sql.append(" AND R.PART_ID = RD.PART_ID ");
        sql.append(" AND RD.CHANGE_REASON IN ('" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02 + "', '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03 + "') ");

        sql.append(sbString);

        if (null != checkCode && !"".equals(checkCode)) {
            sql.append(" AND UPPER(RM.ORDER_CODE) LIKE '%" + checkCode.toUpperCase() + "%' ");
        }
        if (null != inCode && !"".equals(inCode)) {
            sql.append(" AND UPPER(RM.ORDER_CODE) LIKE '%" + inCode.toUpperCase() + "%' ");
        }

        sql.append(" ) INN  ");
        sql.append(" ORDER BY INN.CREATE_DATE DESC ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-12
     * @Title : 返回配件出库详情
     */
    public PageResult<Map<String, Object>> showPartOutStockDT(String sbString, String soCode, String outCode, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
    	/*sql.append(" SELECT R.*, PO.OUT_CODE, PO.SO_CODE, PO.DEALER_NAME, ");
    	sql.append(" TO_CHAR(OD.SALE_PRICE, 'FM999,999,990.00') AS SALE_PRICE, ");
    	sql.append(" TO_CHAR(OD.SALE_AMOUNT, 'FM999,999,990.00') AS SALE_AMOUNT ");
    	sql.append(" FROM TT_PART_RECORD R, TT_PART_OUTSTOCK_MAIN PO, TT_PART_OUTSTOCK_DTL OD ");
    	sql.append(" WHERE 1 = 1 ");
    	sql.append(" AND R.ORDER_ID = PO.OUT_ID ");
    	sql.append(" AND PO.OUT_ID = OD.OUT_ID ");
    	sql.append(" AND R.PART_ID = OD.PART_ID ");
    	sql.append(sbString);
    	sql.append(" ORDER BY R.CREATE_DATE DESC ");*/

        sql.append(" SELECT OT.* FROM ( ");
    	
    	/*销售出库明细*/
        sql.append(" SELECT R.RECORD_ID, ");
        sql.append(" R.PART_ID, ");
        sql.append(" R.PART_CODE, ");
        sql.append(" R.PART_OLDCODE, ");
        sql.append(" R.PART_NAME, ");
        sql.append(" R.PART_NUM, ");
        sql.append(" R.CREATE_DATE, ");
        sql.append(" R.LOC_NAME, ");
        sql.append(" PO.OUT_CODE, ");
        sql.append(" PO.SO_CODE, ");
        sql.append(" PO.DEALER_NAME, ");
        sql.append(" TO_CHAR(OD.SALE_PRICE, 'FM999,999,990.00') AS SALE_PRICE, ");
        sql.append(" TO_CHAR(OD.SALE_AMOUNT, 'FM999,999,990.00') AS SALE_AMOUNT, ");
        sql.append(" '销售业务' AS OUT_TYPE ");
        sql.append(" FROM TT_PART_RECORD R, TT_PART_OUTSTOCK_MAIN PO, TT_PART_OUTSTOCK_DTL OD ");
        sql.append(" WHERE  R.ORDER_ID = PO.OUT_ID ");
        sql.append(" AND PO.OUT_ID = OD.OUT_ID ");
        sql.append(" AND R.PART_ID = OD.PART_ID ");
        sql.append(sbString);

        if (null != soCode && !"".equals(soCode)) {
            sql.append(" AND UPPER(PO.SO_CODE) LIKE '%" + soCode.toUpperCase() + "%' ");
        }
        if (null != outCode && !"".equals(outCode)) {
            sql.append(" AND UPPER(PO.OUT_CODE) LIKE '%" + outCode.toUpperCase() + "%' ");
        }

        sql.append(" UNION ");
    	
    	/*零售领用出库明细*/
        sql.append(" SELECT R.RECORD_ID, ");
        sql.append(" R.PART_ID, ");
        sql.append(" R.PART_CODE, ");
        sql.append(" R.PART_OLDCODE, ");
        sql.append(" R.PART_NAME, ");
        sql.append(" R.PART_NUM, ");
        sql.append(" R.CREATE_DATE, ");
        sql.append(" R.LOC_NAME, ");
        sql.append(" RM.RETAIL_CODE AS OUT_CODE, ");
        sql.append(" RM.RETAIL_CODE AS SO_CODE, ");
        sql.append(" RM.SORG_CNAME AS DEALER_NAME, ");
        sql.append(" TO_CHAR(RD.SALE_PRICE, 'FM999,999,990.00') AS SALE_PRICE, ");
        sql.append(" TO_CHAR((R.PART_NUM * RD.SALE_PRICE), 'FM999,999,990.00') AS SALE_AMOUNT, ");
        sql.append(" '零售/领用' AS OUT_TYPE ");
        sql.append(" FROM TT_PART_RECORD R, TT_PART_RETAIL_MAIN RM, TT_PART_RETAIL_DTL RD ");
        sql.append(" WHERE R.ORDER_ID = RM.RETAIL_ID ");
        sql.append(" AND RM.RETAIL_ID = RD.RETAL_ID ");
        sql.append(" AND R.PART_ID = RD.PART_ID ");
        sql.append(sbString);

        if (null != soCode && !"".equals(soCode)) {
            sql.append(" AND UPPER(RM.RETAIL_CODE) LIKE '%" + soCode.toUpperCase() + "%' ");
        }
        if (null != outCode && !"".equals(outCode)) {
            sql.append(" AND UPPER(RM.RETAIL_CODE) LIKE '%" + outCode.toUpperCase() + "%' ");
        }

        sql.append(" UNION ");
    	
    	/*销售退货出库明细*/
        sql.append(" SELECT R.RECORD_ID, ");
        sql.append(" R.PART_ID, ");
        sql.append(" R.PART_CODE, ");
        sql.append(" R.PART_OLDCODE, ");
        sql.append(" R.PART_NAME, ");
        sql.append(" R.PART_NUM, ");
        sql.append(" R.CREATE_DATE, ");
        sql.append(" R.LOC_NAME, ");
        sql.append(" RM.RETURN_CODE AS OUT_CODE, ");
        sql.append(" RM.RETURN_CODE AS SO_CODE, ");
        sql.append(" RM.DEALER_NAME, ");
        sql.append(" TO_CHAR(NVL(RD.BUY_PRICE,'0'), 'FM999,999,990.00') AS SALE_PRICE, ");
        sql.append(" TO_CHAR((R.PART_NUM * NVL(RD.BUY_PRICE,'0')), 'FM999,999,990.00') AS SALE_AMOUNT, ");
        sql.append(" '销售退货' AS OUT_TYPE ");
        sql.append(" FROM TT_PART_RECORD R, TT_PART_DLR_RETURN_MAIN RM, TT_PART_DLR_RETURN_DTL RD ");
        sql.append(" WHERE R.ORDER_ID = RM.RETURN_ID ");
        sql.append(" AND RM.RETURN_ID = RD.RETURN_ID ");
        sql.append(" AND R.PART_ID = RD.PART_ID ");
        sql.append(sbString);

        if (null != soCode && !"".equals(soCode)) {
            sql.append(" AND UPPER(RM.RETURN_CODE) LIKE '%" + soCode.toUpperCase() + "%' ");
        }
        if (null != outCode && !"".equals(outCode)) {
            sql.append(" AND UPPER(RM.RETURN_CODE) LIKE '%" + outCode.toUpperCase() + "%' ");
        }

        sql.append(" UNION ");
    	
    	/*配件拆合件出库明细*/
        sql.append(" SELECT R.RECORD_ID, ");
        sql.append(" R.PART_ID, ");
        sql.append(" R.PART_CODE, ");
        sql.append(" R.PART_OLDCODE, ");
        sql.append(" R.PART_NAME, ");
        sql.append(" R.PART_NUM, ");
        sql.append(" R.CREATE_DATE, ");
        sql.append(" R.LOC_NAME, ");
        sql.append(" RM.SPCPD_CODE AS OUT_CODE, ");
        sql.append(" RM.SPCPD_CODE AS SO_CODE, ");
        sql.append(" RD.VENDER_NAME AS DEALER_NAME, ");
        sql.append(" TO_CHAR(NVL(RD.PART_COST,'0'), 'FM999,999,990.00') AS SALE_PRICE, ");
        sql.append(" TO_CHAR((R.PART_NUM * NVL(RD.PART_COST,'0')), 'FM999,999,990.00') AS SALE_AMOUNT, ");
        sql.append(" '拆/合件' AS OUT_TYPE ");
        sql.append(" FROM TT_PART_RECORD R, TT_PART_SPCP_MAIN RM, TT_PART_SPLIT_COMPOUND_DTL RD ");
        sql.append(" WHERE R.ORDER_ID = RM.SPCPD_ID ");
        sql.append(" AND RM.SPCPD_ID = RD.SPCPD_ID ");
        sql.append(" AND R.PART_ID = RD.PART_ID ");
        sql.append(" AND RM.SPCPD_TYPE = '92481002' ");/*合件*/
        sql.append(sbString);

        if (null != soCode && !"".equals(soCode)) {
            sql.append(" AND UPPER(RM.SPCPD_CODE) LIKE '%" + soCode.toUpperCase() + "%' ");
        }
        if (null != outCode && !"".equals(outCode)) {
            sql.append(" AND UPPER(RM.SPCPD_CODE) LIKE '%" + outCode.toUpperCase() + "%' ");
        }

        sql.append(" UNION ");

        sql.append(" SELECT R.RECORD_ID, ");
        sql.append(" R.PART_ID, ");
        sql.append(" R.PART_CODE, ");
        sql.append(" R.PART_OLDCODE, ");
        sql.append(" R.PART_NAME, ");
        sql.append(" R.PART_NUM, ");
        sql.append(" R.CREATE_DATE, ");
        sql.append(" R.LOC_NAME, ");
        sql.append(" RM.SPCPD_CODE AS OUT_CODE, ");
        sql.append(" RM.SPCPD_CODE AS SO_CODE, ");
        sql.append(" RM.ORG_CNAME AS DEALER_NAME, ");
        sql.append(" TO_CHAR(nvl(RM.PART_COST,'0'), 'FM999,999,990.00') AS SALE_PRICE, ");
        sql.append(" TO_CHAR((R.PART_NUM * nvl(RM.PART_COST,'0')), 'FM999,999,990.00') AS SALE_AMOUNT, ");
        sql.append(" '拆/合件' AS OUT_TYPE ");
        sql.append(" FROM TT_PART_RECORD R, TT_PART_SPCP_MAIN RM ");
        sql.append(" WHERE R.ORDER_ID = RM.SPCPD_ID ");
        sql.append(" AND R.PART_ID = RM.PART_ID ");
        sql.append(" AND RM.SPCPD_TYPE = '92481001' ");/*拆件*/
        sql.append(sbString);

        if (null != soCode && !"".equals(soCode)) {
            sql.append(" AND UPPER(RM.SPCPD_CODE) LIKE '%" + soCode.toUpperCase() + "%' ");
        }
        if (null != outCode && !"".equals(outCode)) {
            sql.append(" AND UPPER(RM.SPCPD_CODE) LIKE '%" + outCode.toUpperCase() + "%' ");
        }

        sql.append(" UNION ");
    	
    	/*采购退货出库明细*/
        sql.append(" SELECT R.RECORD_ID, ");
        sql.append(" R.PART_ID, ");
        sql.append(" R.PART_CODE, ");
        sql.append(" R.PART_OLDCODE, ");
        sql.append(" R.PART_NAME, ");
        sql.append(" R.PART_NUM, ");
        sql.append(" R.CREATE_DATE, ");
        sql.append(" R.LOC_NAME, ");
        sql.append(" RM.RETURN_CODE AS OUT_CODE, ");
        sql.append(" RM.RETURN_CODE AS SO_CODE, ");
        sql.append(" RM.ORG_NAME AS DEALER_NAME, ");
        sql.append(" TO_CHAR(RD.BUY_PRICE, 'FM999,999,990.00') AS SALE_PRICE, ");
        sql.append(" TO_CHAR((R.PART_NUM * RD.BUY_PRICE), 'FM999,999,990.00') AS SALE_AMOUNT, ");
        sql.append(" '采购退货' AS OUT_TYPE ");
        sql.append(" FROM TT_PART_RECORD R, TT_PART_OEM_RETURN_MAIN RM, TT_PART_OEM_RETURN_DTL RD ");
        sql.append(" WHERE R.ORDER_ID = RM.RETURN_ID ");
        sql.append(" AND RM.RETURN_ID = RD.RETURN_ID ");
        sql.append(" AND R.PART_ID = RD.PART_ID ");
        sql.append(sbString);

        if (null != soCode && !"".equals(soCode)) {
            sql.append(" AND UPPER(RM.RETURN_CODE) LIKE '%" + soCode.toUpperCase() + "%' ");
        }
        if (null != outCode && !"".equals(outCode)) {
            sql.append(" AND UPPER(RM.RETURN_CODE) LIKE '%" + outCode.toUpperCase() + "%' ");
        }

        sql.append(" UNION ");
    	
    	/*盘亏出库*/
        sql.append(" SELECT R.RECORD_ID, ");
        sql.append(" R.PART_ID, ");
        sql.append(" R.PART_CODE, ");
        sql.append(" R.PART_OLDCODE, ");
        sql.append(" R.PART_NAME, ");
        sql.append(" R.PART_NUM, ");
        sql.append(" R.CREATE_DATE, ");
        sql.append(" R.LOC_NAME, ");
        sql.append(" RM.CHANGE_CODE AS IN_CODE, ");
        sql.append(" RM.CHANGE_CODE AS CHECK_CODE, ");
        sql.append(" RM.CHGORG_CNAME AS VENDER_NAME, ");
        sql.append(" TO_CHAR('0', 'FM999,999,990.00') AS BUY_PRICE, ");
        sql.append(" TO_CHAR('0','FM999,999,990.00') AS IN_AMOUNT, ");
        sql.append(" '盘亏出库' AS OUT_TYPE ");
        sql.append(" FROM TT_PART_RECORD R, ");
        sql.append(" TT_PART_CHG_STATE_MAIN RM, ");
        sql.append(" TT_PART_CHG_STATE_DTL RD ");
        sql.append(" WHERE R.ORDER_ID = RM.CHANGE_ID ");
        sql.append(" AND RM.CHG_TYPE = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01 + "' ");
        sql.append(" AND RM.CHANGE_ID = RD.CHANGE_ID ");
        sql.append(" AND R.PART_ID = RD.PART_ID ");
        sql.append(" AND RD.CHANGE_REASON IN ('" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02 + "', '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03 + "') ");

        sql.append(sbString);

        if (null != soCode && !"".equals(soCode)) {
            sql.append(" AND UPPER(RM.RETURN_CODE) LIKE '%" + soCode.toUpperCase() + "%' ");
        }
        if (null != outCode && !"".equals(outCode)) {
            sql.append(" AND UPPER(RM.RETURN_CODE) LIKE '%" + outCode.toUpperCase() + "%' ");
        }

        sql.append(" ) OT ");
        sql.append(" ORDER BY OT.CREATE_DATE DESC ");


        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-4
     * @Title : 返回仓库配件库存信息LIST
     */
    public List<Map<String, Object>> showPartStockBase(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT TD.*, DL.DEALER_NAME, DL.DEALER_CODE, ");
        sql.append(" CASE ");
        sql.append(" WHEN TD.ORG_ID = '" + Constant.OEM_ACTIVITIES + "' THEN ");
        sql.append(" (SELECT TO_CHAR(S.SALE_PRICE3, 'FM999,999,990.00') ");
        sql.append(" FROM TT_PART_SALES_PRICE S ");
        sql.append(" WHERE S.PART_ID = TD.PART_ID) ");
        sql.append(" WHEN TD.ORG_ID != '" + Constant.OEM_ACTIVITIES + "' THEN ");
        sql.append(" TO_CHAR(PKG_PART.F_GETPRICE(DL.DEALER_ID, TD.PART_ID), 'FM999,999,990.00') ");
        sql.append(" END AS PRICE, ");
        sql.append(" (NVL((SELECT SUM(SD.RETURN_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') ");
        sql.append(" - ");
        sql.append(" NVL((SELECT SUM(SD.RETURN_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_02 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') )PTFC, ");
        sql.append(" (NVL((SELECT SUM(SD.RETURN_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_04 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') ");
        sql.append(" - ");
        sql.append(" NVL((SELECT SUM(SD.RETURN_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_04 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_02 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') )LHCW, ");
        sql.append(" (NVL((SELECT SUM(SD.RETURN_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_05 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') ");
        sql.append(" - ");
        sql.append(" NVL((SELECT SUM(SD.RETURN_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_05 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_02 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') )ZLWT, ");
        sql.append(" (NVL((SELECT SUM(SD.RETURN_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_06 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') ");
        sql.append(" - ");
        sql.append(" NVL((SELECT SUM(SD.RETURN_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_06 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_02 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') )JTCL, ");
        sql.append(" (NVL((SELECT SUM(SD.RETURN_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_07 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') ");
        sql.append(" - ");
        sql.append(" NVL((SELECT SUM(SD.RETURN_QTY) FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD ");
        sql.append(" WHERE SM.CHGORG_ID = TD.ORG_ID ");
        sql.append(" AND SD.CHANGE_ID = SM.CHANGE_ID ");
        sql.append(" AND SD.CHANGE_REASON = '" + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_07 + "' ");
        sql.append(" AND SD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_02 + "' ");
        sql.append(" AND SD.PART_ID = TD.PART_ID),'0') ");
        sql.append(" + ");
        sql.append(" NVL((SELECT SUM(BD.BO_QTY) ");
        sql.append(" FROM TT_PART_BO_MAIN BM, TT_PART_BO_DTL BD, TT_PART_SO_MAIN SM ");
        sql.append(" WHERE BM.BO_ID = BD.BO_ID ");
        sql.append(" AND BM.BO_TYPE = 2 ");
        sql.append(" AND BM.SO_ID = SM.SO_ID ");
        sql.append(" AND SM.SELLER_ID = TD.ORG_ID ");
        sql.append(" AND BD.PART_ID = TD.PART_ID), '0') ");
        sql.append(" ) XCBO ");
        sql.append(" FROM VW_PART_STOCK TD, TM_DEALER DL ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.ORG_ID = DL.DEALER_ID(+) ");
        sql.append(sbString);
        sql.append(" AND TD.FC_QTY > 0 ");
        sql.append(" ORDER BY DL.DEALER_CODE, TD.WH_NAME, TD.PART_OLDCODE, TD.PART_CNAME, TD.PART_CODE ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param orgId
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-17
     * @Title : 配件占用资金汇总查询
     */
    public List<Map<String, Object>> showPartGroupAmount(String orgId, String groupType) {
        StringBuffer sql = new StringBuffer("");

        if (Constant.OEM_ACTIVITIES.equals(orgId)) {
            sql.append(" SELECT substr(TD.PART_OLDCODE,1,1) AS GROUP_TYPE, ");
            sql.append(" TO_CHAR(NVL(SUM(TD.ITEM_QTY * SP.SALE_PRICE3),'0'),'FM999,999,999,990.00') AS GROUP_AMOUNT, ");
            sql.append(" NVL(SUM(TD.ITEM_QTY),'0') AS GROUP_COUNT ");
            sql.append(" FROM VW_PART_STOCK TD, TT_PART_SALES_PRICE SP ");
            sql.append(" WHERE TD.PART_ID = SP.PART_ID ");
            sql.append(" AND TD.ORG_ID = '" + orgId + "'");
            if (null != groupType && !"".equals(groupType)) {
                sql.append(" AND UPPER(SUBSTR(TD.PART_OLDCODE,1,1)) = '" + groupType.trim().toUpperCase() + "'");
            }
            sql.append(" GROUP BY substr(TD.PART_OLDCODE,1,1) ");
            sql.append(" ORDER BY substr(TD.PART_OLDCODE,1,1) ");
        } else {
            sql.append(" SELECT substr(TD.PART_OLDCODE,1,1) AS GROUP_TYPE, ");
            sql.append(" TO_CHAR(NVL(SUM(TD.ITEM_QTY * (PKG_PART.F_GETPRICE('" + orgId + "', D.PART_ID))),'0'),'FM999,999,999,990.00') AS GROUP_AMOUNT, ");
            sql.append(" NVL(SUM(TD.ITEM_QTY),'0') AS GROUP_COUNT ");
            sql.append(" FROM VW_PART_STOCK TD, TT_PART_DEFINE D ");
            sql.append(" WHERE TD.PART_ID = D.PART_ID ");
            sql.append(" AND TD.ORG_ID = '" + orgId + "'");
            if (null != groupType && !"".equals(groupType)) {
                sql.append(" AND UPPER(SUBSTR(TD.PART_OLDCODE,1,1)) = '" + groupType.trim().toUpperCase() + "'");
            }
            sql.append(" GROUP BY substr(TD.PART_OLDCODE,1,1) ");
            sql.append(" ORDER BY substr(TD.PART_OLDCODE,1,1) ");
        }

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
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
     * @param : @param delCode
     * @param : @return
     * @return :
     * LastDate    : 2013-5-3
     * @Title : 验证服务商编码是否存在 并返回服务商ID、Name
     * @Description:
     */
    public List checkOldCode(String oldCode) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT TD.PART_ID,TD.PART_CODE FROM TT_PART_DEFINE TD " +
                " WHERE TD.PART_OLDCODE = '" + oldCode + "' ";
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }

    /**
     * @param : @param oldCode
     * @param : @param parentOrgId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-25
     * @Title : 返回配件库存状态信息
     */
    public List getPartStockInfos(String oldCode, String parentOrgId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM VW_PART_STOCK TD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_OLDCODE = '" + oldCode + "' AND TD.ORG_ID = '" + parentOrgId + "' ");
        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param oldCode
     * @param : @param parentOrgId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-25
     * @Title : 返回精确的配件库存状态信息
     */
    public List getPartStockInfos(String oldCode, String parentOrgId, String whId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM VW_PART_STOCK TD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_OLDCODE = '" + oldCode + "' AND TD.ORG_ID = '" + parentOrgId + "' ");
        sql.append(" AND TD.WH_ID = '" + whId + "' ");
        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-17
     * @Title : 主机厂库存占用总资金
     */
    public List<Map<String, Object>> getOEMPartsAmount() {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT TO_CHAR(SUM(TD.ITEM_QTY * SP.SALE_PRICE3),'FM999,999,999,990.00') AS PARTS_AMOUNT, NVL(SUM(TD.ITEM_QTY),'0') AS ITEM_QTY ");
        sql.append(" FROM VW_PART_STOCK TD, TT_PART_SALES_PRICE SP ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_ID = SP.PART_ID ");
        sql.append(" AND TD.ORG_ID = '" + Constant.OEM_ACTIVITIES + "'");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param delId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-17
     * @Title : 服务商库存占用总资金
     */
    public List<Map<String, Object>> getDLRPartsAmount(String dealerId) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT TO_CHAR(NVL(SUM(TD.ITEM_QTY * (PKG_PART.F_GETPRICE(" + dealerId + ", D.PART_ID))),'0'),'FM999,999,999,990.00') AS PARTS_AMOUNT, NVL(SUM(TD.ITEM_QTY),'0') AS ITEM_QTY ");
        sql.append(" FROM VW_PART_STOCK TD, TT_PART_DEFINE D ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_ID = D.PART_ID ");
        sql.append(" AND TD.ORG_ID = '" + dealerId + "'");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
}
