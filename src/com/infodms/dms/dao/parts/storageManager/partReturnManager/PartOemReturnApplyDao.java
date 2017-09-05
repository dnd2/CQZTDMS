package com.infodms.dms.dao.parts.storageManager.partReturnManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartPoInPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TypeHost;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PartOemReturnApplyDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PartOemReturnApplyDao.class);

    private static final PartOemReturnApplyDao dao = new PartOemReturnApplyDao();

    private PartOemReturnApplyDao() {

    }

    public static final PartOemReturnApplyDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> queryPartOrderInList(
            TtPartPoInPO po, String beginTime, String endTime,
            String inBeginTime, String inEndTime, String inName, String buyer,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT A.IN_ID,\n");
            sql.append("       A.CHECK_CODE   ORDER_CODE,\n");
            sql.append("       A.IN_CODE,\n");
            sql.append("       A.PART_ID,\n");
            sql.append("       A.PART_CODE,\n");
            sql.append("       A.PART_OLDCODE,\n");
            sql.append("       A.PART_CNAME,\n");
            sql.append("       A.VENDER_ID,\n");
            sql.append("       A.VENDER_CODE,\n");
            sql.append("       A.VENDER_NAME,\n");
            sql.append("       A.WH_ID,\n");
            sql.append("       A.WH_NAME,\n");
            sql.append("       A.LOC_ID,\n");
            sql.append("       A.LOC_CODE,\n");
            sql.append("       B.LOC_NAME,\n");
            sql.append("       A.BATCH_NO,\n");
            sql.append("       A.PRODUCE_STATE,\n");
            sql.append("       A.BUY_QTY,\n");
            sql.append("       A.IN_QTY,\n");
            sql.append("       A.APPLY_QTY,\n");
            sql.append("       A.APPLY_QTY    AS APPLY_QTY2,\n");
            sql.append("       A.RETURN_QTY,\n");
            sql.append("       B.ITEM_QTY,\n");
            sql.append("       B.NORMAL_QTY,\n");
            sql.append("       A.IN_DATE,\n");
            sql.append("       A.CREATE_DATE,\n");
            sql.append("       A.IS_BALANCES\n");
            sql.append("  FROM TT_PART_PO_IN A, VW_PART_STOCK B\n");
            sql.append(" WHERE A.PART_ID = B.PART_ID\n");
            sql.append("   AND A.ORG_ID = B.ORG_ID\n");
            sql.append("   AND A.WH_ID = B.WH_ID\n");
            sql.append("   AND A.BATCH_NO = B.BATCH_NO\n");
            sql.append("   AND A.LOC_ID = B.LOC_ID\n");
            sql.append("   AND A.IN_QTY > A.RETURN_QTY\n");
            sql.append("   AND A.STATE = '"+Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01+"'\n"); // 待结算
            if (!"".equals(po.getOrderCode())) {
                sql.append(" AND A.Check_Code LIKE '%"+po.getOrderCode()+"%'\n");
            }

            if (!"".equals(po.getInCode())) {
                sql.append(" AND A.IN_CODE = '"+po.getInCode()+"'\n");
            }

            if (!"".equals(buyer)) {
                sql.append(" AND C.BUYER LIKE '%")
                        .append(buyer).append("%'\n");
            }

            if (!"".equals(inName)) {
                sql.append(" AND B.NAME LIKE '%")
                        .append(inName).append("%'\n");
            }

            if (!"".equals(beginTime)) {
                sql.append(" AND to_date(A.CREATE_DATE) >= to_date('"+beginTime+"','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND to_date(A.CREATE_DATE) <= to_date('"+endTime+"','yyyy-MM-dd')");
            }
            if (!"".equals(inBeginTime)) {
                sql.append(" AND to_date(A.IN_DATE)>= to_date('"+inBeginTime+"','yyyy-MM-dd')");
            }

            if (!"".equals(inEndTime)) {
                sql.append(" AND to_date(A.IN_DATE) <= to_date('"+inEndTime+"','yyyy-MM-dd')");
            }

            if (po.getWhId() != null && po.getWhId() != 0) {
                sql.append(" AND A.Wh_Id="+po.getWhId());
            }

            if (po.getProduceState() != null && po.getProduceState() != 0) {
                sql.append(" AND A.PRODUCE_STATE="+po.getProduceState());
            }

            if (po.getVenderId() != null && po.getVenderId() != 0) {
                sql.append(" AND A.VENDER_ID="+po.getVenderId());
            }

            if (!po.getPartOldcode().equals("")) {
                sql.append(" AND A.PART_OLDCODE LIKE '%"+po.getPartOldcode()+"%'\n");
            }
            if (!po.getPartCname().equals("")) {
                sql.append(" AND A.PART_CNAME LIKE '%"+po.getPartCname()+"%'\n");
            }

            sql.append(" ORDER BY A.CREATE_DATE ASC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }
    
    /**
     * <p>
     * Description: 根据退货id查询退货的配件
     * </p>
     * @param returnId 退货id
     * @return
     */
    public List<Map<String, Object>> queryApplyDetailListById(String returnId){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T.RETURN_ID,\n");
        sql.append("       T.IN_ID,\n");
        sql.append("       T1.CHECK_CODE,\n");
        sql.append("       T.PART_CODE,\n");
        sql.append("       T.PART_OLDCODE,\n");
        sql.append("       T.PART_CNAME,\n");
        sql.append("       T.VENDER_NAME,\n");
        sql.append("       T.IN_QTY,\n");
        sql.append("       V.ITEM_QTY,\n");
        sql.append("       V.NORMAL_QTY,\n");
        sql.append("       T.APPLY_QTY,\n");
        sql.append("       T3.APPLY_QTY APPLY_QTY2,\n");
        sql.append("       T.CHECK_QTY,\n");
        sql.append("       T.RETURN_QTY,\n");
        sql.append("       V.LOC_CODE,\n");
        sql.append("       V.BATCH_NO,\n");
        sql.append("       T3.RETURN_QTY A_RETURN_QTY,\n");
        sql.append("       T3.PART_ID || T3.LOC_ID || T3.LOC_CODE || V.LOC_NAME || T3.BATCH_NO LOC,\n");
        sql.append("       T.REMARK\n");
        sql.append("  FROM TT_PART_OEM_RETURN_MAIN T1, \n");
        sql.append("       TT_PART_OEM_RETURN_DTL  T, \n");
        sql.append("       TT_PART_PO_IN           T3, \n");
        sql.append("       VW_PART_STOCK           V \n");
        sql.append(" WHERE T1.RETURN_ID = T.RETURN_ID \n");
        sql.append("   AND T.IN_ID = T3.IN_ID \n");
        sql.append("   AND T3.PART_ID = V.PART_ID \n");
        sql.append("   AND T3.ORG_ID = V.ORG_ID\n");
        sql.append("   AND T3.WH_ID = V.WH_ID\n");
        sql.append("   AND T3.BATCH_NO = V.BATCH_NO\n");
        sql.append("   AND T3.LOC_ID = V.LOC_ID\n");
        sql.append("   AND T.RETURN_ID = ").append(returnId);
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
    
    /**
     * <p>
     * Description: 根据入库id更新采购退货申请数量
     * </p>
     * @param inId 入库id
     * @param logonUser 当前登录用户信息
     * @return
     */
    public int updateApplyQtyByInId(String inId, AclUserBean logonUser){
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE TT_PART_PO_IN T1 \n");
        sql.append("    SET APPLY_QTY = \n");
        sql.append("        (SELECT SUM(APPLY_QTY) \n");
        sql.append("           FROM TT_PART_OEM_RETURN_DTL T2 \n");
        sql.append("          WHERE T1.IN_ID = T2.IN_ID \n");
        sql.append("            AND T2.IN_ID = "+inId+"), \n");
        sql.append("          GROUP BY T2.IN_ID), \n");
        sql.append("    UPDATE_BY = "+logonUser.getUserId()+", \n");
        sql.append("    UPDATE_DATE = SYSDATE \n");
        sql.append("  WHERE T1.IN_ID = "+inId+" ");
        return update(sql.toString(), null);
    }
    
    public PageResult<Map<String, Object>> queryApplyDetailList(String[] ids, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("select t1.in_id,t1.order_code, t1.in_code,t1.part_id,t1.part_code,t1.part_oldcode,t1.part_cname,t1.vender_id,");
            sql.append("t1.vender_code,t1.vender_name,t1.buy_qty, t1.in_qty,t2.ITEM_QTY,");
            sql.append("t2.NORMAL_QTY,t1.apply_qty as apply_qty2 ,t1.return_qty,t1.is_balances");
            sql.append("  from tt_part_po_in t1 left join vw_part_stock t2 on t1.part_id=t2.PART_ID  AND t2.WH_ID=t1.wh_id where t1.in_id in(");
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    sql.append(ids[i]).append(",");
                }
                sql.deleteCharAt(sql.length() - 1);
            }
            sql.append(")");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }


    /**
     * <p>
     * Description: 查询
     * </p>
     * @param ids
     * @param returnId
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> queryInAndNormalQty(String[] ids, String returnId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            
            if(!CommonUtils.isEmpty(returnId)){
                sql.append("WITH SUM_QTY AS(\n");
                sql.append("SELECT PART_ID, STOCK_IN, LOC_ID, BATCH_NO, RETURN_ID, SUM(APPLY_QTY) SUM_QTY\n");
                sql.append("  FROM TT_PART_OEM_RETURN_DTL\n");
                sql.append(" WHERE RETURN_ID = '"+returnId+"'\n");
                sql.append(" GROUP BY PART_ID, STOCK_IN, LOC_ID, BATCH_NO, RETURN_ID)\n");
            }
            sql.append("SELECT T1.IN_ID,T1.IN_CODE,T1.ORDER_CODE, T1.IN_CODE,T1.UNIT,T1.BUY_PRICE,T1.WH_ID, T1.LOC_ID, T1.LOC_CODE, T1.PART_ID,T1.PART_CODE,T1.PART_OLDCODE,T1.PART_CNAME,");
            sql.append("T1.VENDER_ID,T1.VENDER_CODE,T1.VENDER_NAME,T1.BUY_QTY, T1.IN_QTY,NVL(T2.ITEM_QTY,T1.IN_QTY) ITEM_QTY, T1.BATCH_NO, T1.RETURN_QTY,");
            if(!CommonUtils.isEmpty(returnId)){
                sql.append("T3.APPLY_QTY THIS_APPLY_QTY, SQ.SUM_QTY,");
            }
            sql.append("NVL(T2.NORMAL_QTY,T1.IN_QTY) NORMAL_QTY,T1.APPLY_QTY,T1.RETURN_QTY,T1.IS_BALANCES");
            sql.append("  FROM TT_PART_PO_IN T1 INNER JOIN VW_PART_STOCK T2 ON T1.PART_ID=T2.PART_ID AND T1.WH_ID =T2.WH_ID AND T1.LOC_ID = T2.LOC_ID ");
            sql.append("  AND T1.BATCH_NO = T2.BATCH_NO AND T1.ORG_ID = T2.ORG_ID");
            if(CommonUtils.isEmpty(returnId)){
                sql.append(" WHERE 1 = 1 ");
                sql.append("AND T1.IN_ID IN( ");
                for (int i = 0; i < ids.length; i++) {
                    sql.append(ids[i]).append(",");
                }
                sql.deleteCharAt(sql.length() - 1);
                sql.append(")");
            }else {
                sql.append(" INNER JOIN TT_PART_OEM_RETURN_DTL T3 ON T3.PART_ID = T1.PART_ID AND T3.IN_ID = T1.IN_ID AND T3.LOC_ID = T1.LOC_ID");
                sql.append(" INNER JOIN SUM_QTY SQ ON SQ.RETURN_ID = T3.RETURN_ID AND SQ.PART_ID = T3.PART_ID AND SQ.LOC_ID = T3.LOC_ID AND SQ.BATCH_NO = T3.BATCH_NO AND SQ.STOCK_IN = T3.STOCK_IN");
                sql.append(" WHERE 1 = 1 ");
                sql.append("AND T3.RETURN_ID = " + returnId);
            }
            return pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    
    
    public PageResult<Map<String, Object>> queryPartOemReturnApplyList(
            Map<String, String> paramMap,AclUserBean logonUser, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T1.RETURN_ID,\n");
            sql.append("       T1.RETURN_CODE,\n");
            sql.append("       T1.ORG_NAME,\n");
            sql.append("       T1.RETURN_TYPE,\n");
            sql.append("       T2.NAME CREATE_NAME,\n");
            sql.append("       T1.CREATE_DATE,\n");
            sql.append("       T1.REMARK,\n");
            sql.append("       T1.APPLY_DATE,\n");
            sql.append("       T1.STATE,\n");
            sql.append("       NVL((SELECT COUNT(RD.DTL_ID)\n");
            sql.append("             FROM TT_PART_OEM_RETURN_DTL RD\n");
            sql.append("            WHERE RD.STATUS = 2\n");
            sql.append("              AND RD.RETURN_ID = T1.RETURN_ID),\n");
            sql.append("           0) AS OUT_COUNT,\n");
            sql.append("       DECODE(T1.RETURN_TO, 1, '退回供应商', '退回北汽银翔') RETURN_TO\n");
            sql.append("  FROM TT_PART_OEM_RETURN_MAIN T1, TC_USER T2\n");
            sql.append(" WHERE T1.CREATE_BY = T2.USER_ID");
            sql.append(" AND T1.CREATE_ORG=").append(logonUser.getOrgId());
            if (!"".equals(paramMap.get("returnCode"))) {
                sql.append(" AND T1.RETURN_CODE LIKE '%")
                        .append(paramMap.get("returnCode")).append("%'\n");
            }
            if (!"".equals(paramMap.get("returnType"))) {
                sql.append(" AND T1.RETURN_TYPE=")
                        .append(paramMap.get("returnType"));
            }
            if (!"".equals(paramMap.get("state"))) {
                sql.append(" AND T1.STATE= "+paramMap.get("state"));
            }
            if (!"".equals(paramMap.get("createName"))) {
                sql.append(" AND T2.NAME LIKE '%")
                        .append(paramMap.get("createName")).append("%'\n");
            }
            if (!"".equals(paramMap.get("startDate"))) {
                sql.append(" AND to_date(T1.CREATE_DATE)>=").append("to_date('").append(paramMap.get("startDate")).append("','yyyy-MM-dd')");
            }

            if (!"".equals(paramMap.get("endDate"))) {
                sql.append(" AND to_date(T1.CREATE_DATE)<=").append("to_date('").append(paramMap.get("endDate")).append("','yyyy-MM-dd')");
            }
            sql.append(" AND T1.STATUS=1");
            sql.append(" ORDER BY T1.CREATE_DATE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public Map<String, Object> getPartOemReturnMainInfo(String returnId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T1.RETURN_ID, \n");
            sql.append("        T1.RETURN_CODE, \n");
            sql.append("        T1.ORG_NAME, \n");
            sql.append("        TRUNC(T1.CREATE_DATE) CREATE_DATE, \n");
            sql.append("        T1.CHECK_CODE, ");
            sql.append("        DECODE(T1.RETURN_TYPE, \n");
            sql.append(         Constant.PART_OEM_RETURN_TYPE_01);
            sql.append("               ,'有单退货', \n");
            sql.append(         Constant.PART_OEM_RETURN_TYPE_02);
            sql.append("               ,'无单退货') RETURN_TYPE, \n");
            sql.append("        T1.REMARK, \n");
            sql.append("        T1.REMARK1, \n");
            sql.append("        T2.NAME \n");
            sql.append("   FROM TT_PART_OEM_RETURN_MAIN T1, TC_USER T2 \n");
            sql.append("  WHERE T1.CREATE_BY = T2.USER_ID \n");
            sql.append("    AND T1.RETURN_ID = ").append(returnId);

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

    }

    public PageResult<Map<String, Object>> queryPartOemReturnDetailList(
            String returnId, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT T.PART_CODE,\n");
            sql.append("       T.PART_OLDCODE,\n");
            sql.append("       T.PART_CNAME,\n");
            sql.append("       T.VENDER_NAME,\n");
            sql.append("       T.IN_CODE,\n");
            sql.append("       T.IN_QTY,\n");
            sql.append("       V.ITEM_QTY,\n");
            sql.append("       V.NORMAL_QTY,\n");
            sql.append("       T.APPLY_QTY,\n");
            sql.append("       T.CHECK_QTY,\n");
            sql.append("       T.RETURN_QTY,\n");
            sql.append("       V.LOC_CODE,\n");
            sql.append("       T.REMARK\n");
            sql.append("  FROM TT_PART_OEM_RETURN_DTL T, TT_PART_OEM_RETURN_MAIN TM, VW_PART_STOCK V\n");
            sql.append(" WHERE T.STOCK_IN = V.WH_ID\n");
            sql.append("   AND T.PART_ID = V.PART_ID\n");
            sql.append("   AND T.LOC_ID = V.LOC_ID\n");
            sql.append("   AND T.BATCH_NO = V.BATCH_NO\n");
            sql.append("   AND T.STOCK_IN = V.WH_ID\n");
            sql.append("   AND TM.ORG_ID = V.ORG_ID\n");
            sql.append("   AND T.RETURN_ID = TM.RETURN_ID\n");
            sql.append("   AND T.RETURN_ID = ").append(returnId);
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<TtPartWarehouseDefinePO> getPartWareHouseList(AclUserBean logonUser) throws Exception {
        try {
            TtPartWarehouseDefinePO po = new TtPartWarehouseDefinePO();
            po.setState(Constant.STATUS_ENABLE);
            po.setStatus(1);
            if (logonUser.getDealerId() != null) {
                po.setOrgId(CommonUtils.parseLong(logonUser.getDealerId()));
            } else {
                po.setOrgId(logonUser.getOrgId());
            }
            List<TtPartWarehouseDefinePO> list = select(po);
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPartInfoList(TtPartDefinePO po,
                                                             String whId, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T.PART_ID, \n");
            sql.append("        T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.NORMAL_QTY, \n");
            sql.append("        T.UNIT, \n");
            sql.append("        T.LOC_CODE LOC_NAME, \n");
            sql.append("        T.LOC_ID, \n");
            sql.append("        ROUND(T1.SALE_PRICE3, 2) SALE_PRICE3 \n");
            sql.append("   FROM VW_PART_STOCK T, TT_PART_SALES_PRICE T1 \n");
            sql.append("  WHERE T.PART_ID = T1.PART_ID \n");
//			sql.append("    AND T1.STATE = ").append(Constant.STATUS_ENABLE);
//			sql.append("    AND T1.STATUS = 1 \n");
            sql.append("    AND T.WH_ID = ").append(whId);

            if ("2014092298805416".equals(whId)) {
                sql.append(" AND T.LOC_CODE!='QHJ'");
            }
            if (po.getPartCode() != null && !"".equals(po.getPartCode())) {
                sql.append(" AND T.PART_CODE LIKE '%")
                        .append(po.getPartCode()).append("%'\n");
            }
            if ((po.getPartOldcode() != null) && !"".equals(po.getPartOldcode())) {
                sql.append(" AND T.PART_OLDCODE LIKE '%")
                        .append(po.getPartOldcode()).append("%'\n");
            }
            if ((po.getPartCname() != null) && !"".equals(po.getPartCname())) {
                sql.append(" AND T.PART_CNAME LIKE '%")
                        .append(po.getPartCname()).append("%'\n");
            }
            sql.append(" ORDER BY T.PART_OLDCODE ASC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryNormalQty(String partId,
                                                    String whId, String locId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T.PART_ID, \n");
            sql.append("        T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.NORMAL_QTY, \n");
            sql.append("        T.ITEM_QTY, \n");
            sql.append("        T.WH_ID, \n");
            sql.append("        T.UNIT, \n");
            sql.append("        T.LOC_NAME, \n");
            sql.append("        ROUND(T1.SALE_PRICE3, 2) BUY_PRICE \n");
            sql.append("   FROM VW_PART_STOCK T, TT_PART_SALES_PRICE T1 \n");
            sql.append("  WHERE T.PART_ID = T1.PART_ID \n");
//			sql.append("    AND T1.STATE = ").append(Constant.STATUS_ENABLE);
//			sql.append("    AND T1.STATUS = 1 \n");
            sql.append("    AND T.WH_ID = ").append(whId);
            sql.append("    AND T.PART_ID =" + partId);
            sql.append("    AND T.LOC_ID =" + locId);
            return pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> queryPartOemReturn(String returnId) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.UNIT, \n");
            sql.append("        T.VENDER_NAME, \n");
            sql.append("        T.IN_QTY, \n");
            sql.append("        T.ITEM_QTY, \n");
            sql.append("        T.NORMAL_QTY, \n");
            sql.append("        T.APPLY_QTY, \n");
            sql.append("        T.CHECK_QTY, \n");
            sql.append("        T.BUY_PRICE, \n");
            sql.append("        T.BUY_AMOUNT, \n");
            sql.append("        T.RETURN_QTY, \n");
            sql.append("        T1.WH_NAME, \n");
            sql.append("        T2.LOC_NAME, \n");
            sql.append("        T.REMARK \n");
            sql.append("   FROM TT_PART_OEM_RETURN_DTL T,TT_PART_WAREHOUSE_DEFINE T1,TT_PART_LOACTION_DEFINE T2 \n");
            sql.append("    WHERE T.STOCK_OUT=T1.WH_ID AND T.PART_ID=T2.PART_ID AND T.STOCK_OUT=T2.WH_ID\n");
            sql.append(" AND T.RETURN_ID=").append(returnId);

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public void updateApplyQty(Long inId, Long applyQty) throws Exception {
        try {
            StringBuffer sql2 = new StringBuffer("");
            sql2.append("UPDATE TT_PART_PO_IN P SET P.APPLY_QTY=P.APPLY_QTY-").append(applyQty);
            sql2.append(" WHERE P.IN_ID=").append(inId);
            update(sql2.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-12-20
     * @Title : 验证采购退货申请是否存在出库（入库）情况
     */
    public List<Map<String, Object>> orderStateCheck(String sqlStr) {
        List<Map<String, Object>> list;
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT COUNT(RD.DTL_ID) AS OUT_COUNT FROM TT_PART_OEM_RETURN_DTL RD WHERE RD.STATUS = 2 ");
        sql.append(sqlStr);
        list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * <p>
     * Description: 获取入库单信息
     * </p>
     * @param paramMap 查询参数
     * @param curPage 页码
     * @param pageSize 每页条数
     * @return
     */
    public PageResult<Map<String, Object>> queryPartOrderInList(Map<String, String> paramMap, Integer curPage, Integer pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT IN_CODE, VENDER_ID, VENDER_CODE, VENDER_NAME \n");
        sql.append("   FROM TT_PART_PO_IN \n");
        sql.append("  WHERE ORG_ID = '"+paramMap.get("orgId")+"' \n");
        // 入库单号
        if(StringUtil.notNull(paramMap.get("inCode"))){
            sql.append("    AND IN_CODE LIKE '%"+paramMap.get("inCode")+"%' \n");
        }
        // 入库开始时间
        if(StringUtil.notNull(paramMap.get("beginTime"))){
            sql.append("    AND IN_DATE >= TO_DATE('"+paramMap.get("beginTime")+"', 'YYYY-MM-DD') \n");
        }
        // 入库结束时间
        if(StringUtil.notNull(paramMap.get("endTime"))){
            sql.append("    AND IN_DATE <= TO_DATE('"+paramMap.get("endTime")+"', 'YYYY-MM-DD')+1-1/23/59/59 \n");
        }
        sql.append("  GROUP BY IN_CODE, VENDER_ID, VENDER_CODE, VENDER_NAME \n");
        sql.append("  ORDER BY IN_CODE DESC\n");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize,  curPage);
        return ps;
    }

}
