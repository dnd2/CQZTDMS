package com.infodms.dms.dao.parts.storageManager.stockNumManager;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.inst2xsd.SalamiSliceStrategy;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.component.dict.CodeDict;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.parts.storageManager.partStoInveManager.entityInvImpDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * <p>
 * ClassName: StockAbjustmentDao
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author: MEpaper
 * </p>
 * <p>
 * Date: 2017年7月24日
 * </p>
 */
@SuppressWarnings("unchecked")
public class StockAbjustmentDao extends BaseDao {

    public static Logger logger = Logger.getLogger(entityInvImpDao.class);
    private static final StockAbjustmentDao dao = new StockAbjustmentDao();

    private StockAbjustmentDao() {
    }

    public static final StockAbjustmentDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    private String loadDecodeSql(List<TcCodePO> list, String code) {
        String decodeSql = "decode(" + code + "";
        for (TcCodePO tc : list) {
            decodeSql += ",'" + tc.getCodeId() + "','" + tc.getCodeDesc() + "'";
        }
        if (decodeSql == "docode(" + code + "") {
            return "'' " + code;
        }
        decodeSql += ",'')";
        return decodeSql;
    }

    /**
     * <p>
     * Description: 获取查询参数sql
     * </p>
     * 
     * @param paramMap
     * @return
     */
    private String getAbjustmentMainSql(Map<String, String> paramMap) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT T1.ABJUSTMENT_ID, \n");
        sql.append("        T1.ABJUSTMENT_CODE, \n");
        sql.append("        T1.ABJUSTMENT_TYPE, \n");
        sql.append("        T1.WH_ID, \n");
        sql.append("        T1.WH_CNAME, \n");
        sql.append("        T1.APPLY_STATE, \n");
        sql.append("        T1.CHECK_STATE, \n");
        sql.append("        T1.STATE, \n");
        sql.append("        T1.STATUS, \n");
        sql.append("        T1.REMARK, \n");
        sql.append("        T1.APPLY_BY, \n");
        sql.append("        T1.APPLY_DATE, \n");
        sql.append("        T1.CHECK_REMARK, \n");
        if ("1".equals(paramMap.get("decodeSql"))) {
            List<TcCodePO> typeList = CodeDict.dictMap.get(Constant.PART_ABJUSTMENT_TYPE.toString());
            String typeDecode = this.loadDecodeSql(typeList, "T1.ABJUSTMENT_TYPE");
            List<TcCodePO> applyList = CodeDict.dictMap.get(Constant.PART_ABJUSTMENT_APPLY.toString());
            String applyDecode = this.loadDecodeSql(applyList, "T1.APPLY_STATE");
            List<TcCodePO> checkList = CodeDict.dictMap.get(Constant.PART_ABJUSTMENT_CHECK.toString());
            String checkDecode = this.loadDecodeSql(checkList, "T1.CHECK_STATE");
            List<TcCodePO> stateList = CodeDict.dictMap.get(Constant.PART_ABJUSTMENT_STATE.toString());
            String stateDecode = this.loadDecodeSql(stateList, "T1.STATE");
            List<TcCodePO> statusList = CodeDict.dictMap.get(Constant.STATUS.toString());
            String statusDecode = this.loadDecodeSql(statusList, "T1.STATUS");
            sql.append("       " + typeDecode + " ABJUSTMENT_TYPE_DESC,\n");
            sql.append("       " + applyDecode + " APPLY_STATE_DESC,\n");
            sql.append("       " + checkDecode + " CHECK_STATE_DESC,\n");
            sql.append("       " + stateDecode + " STATE_DESC,\n");
            sql.append("       " + statusDecode + " STATUS_DESC,\n");
            sql.append("        T2.NAME APPLY_NAME, \n");
            sql.append("        T3.NAME CHECK_NAME, \n");
            sql.append("        T1.CREATE_DATE \n");
            sql.append("   FROM TT_PART_STOCK_ABJUSTMENT_MAIN T1, TC_USER T2, TC_USER T3 \n");
            sql.append("  WHERE T1.APPLY_BY = T2.USER_ID(+) \n");
            sql.append("    AND T1.CHECK_BY = T3.USER_ID(+) \n");
        } else {
            sql.append("        T1.CREATE_DATE \n");
            sql.append("   FROM TT_PART_STOCK_ABJUSTMENT_MAIN T1 \n");
            sql.append("  WHERE 1 =1 \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("status"))) {
            sql.append("    AND T1.STATUS = '" + paramMap.get("status") + "' \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("abjustmentId"))) {
            sql.append("    AND T1.ABJUSTMENT_ID = '" + paramMap.get("abjustmentId") + "' \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("createSDate"))) {
            sql.append("    AND T1.CREATE_DATE >= TO_DATE('" + paramMap.get("createSDate") + "', 'YYYY-MM-DD') \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("createEDate"))) {
            sql.append("    AND T1.CREATE_DATE <= TO_DATE('" + paramMap.get("createEDate")
                    + "', 'YYYY-MM-DD')+1-1/23/59/59 \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("applyName"))) {
            sql.append("    AND T2.NAME like '%" + paramMap.get("applyName") + "%'\n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("applySDate"))) {
            sql.append("    AND T1.APPLY_DATE >= TO_DATE('" + paramMap.get("applySDate") + "', 'YYYY-MM-DD') \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("applyEDate"))) {
            sql.append("    AND T1.APPLY_DATE <= TO_DATE('" + paramMap.get("applyEDate")
                    + "', 'YYYY-MM-DD')+1-1/23/59/59 \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("whId"))) {
            sql.append("    AND T1.WH_ID = '" + paramMap.get("whId") + "' \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("abjustmentType"))) {
            sql.append("    AND T1.ABJUSTMENT_TYPE = '" + paramMap.get("abjustmentType") + "' \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("state"))) {
            sql.append("    AND T1.STATE = '" + paramMap.get("state") + "' \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("abjustmentCode"))) {
            sql.append("    AND T1.ABJUSTMENT_CODE = '" + paramMap.get("abjustmentCode") + "' \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("applyStatus"))) {
            sql.append("    AND T1.APPLY_STATE = '" + paramMap.get("applyStatus") + "' \n");
        }
        sql.append("  ORDER BY T1.CREATE_DATE DESC \n");
        return sql.toString();
    }

    /**
     * <p>
     * Description: 分页查询配件库存调整信息
     * </p>
     * 
     * @param paramMap 查询参数
     * @param pageSize 每页条数
     * @param curPage 页码
     * @return
     */
    public PageResult<Map<String, Object>> queryStockAbjustmentPageList(Map<String, String> paramMap, Integer pageSize,
            Integer curPage) {
        PageResult<Map<String, Object>> ps = pageQuery(getAbjustmentMainSql(paramMap), null, getFunName(), pageSize,
                curPage);
        return ps;
    }

    /**
     * <p>
     * Description: 查询配件库存调整信息
     * </p>
     * 
     * @param paramMap 查询参数
     * @return
     */
    public List<Map<String, Object>> getAbjustmentMainList(Map<String, String> paramMap) {
        paramMap.put("decodeSql", "1");
        List<Map<String, Object>> list = pageQuery(getAbjustmentMainSql(paramMap), null, getFunName());
        return list;
    }

    /**
     * <p>
     * Description: 获取配件库存调整明细列表
     * </p>
     * 
     * @param paramMap 查询参数
     * @return
     */
    public List<Map<String, Object>> getAbjustmentDetailList(Map<String, String> paramMap) {
        StringBuffer sql = new StringBuffer();
        List<TcCodePO> dealList = CodeDict.dictMap.get(Constant.PART_ABJUSTMENT_CHECK_DEAL_STATUS.toString());
        String dealDecode = this.loadDecodeSql(dealList, "T1.DEAL_STATUS");
        List<TcCodePO> checkList = CodeDict.dictMap.get(Constant.PART_ABJUSTMENT_CHECK.toString());
        String checkDecode = this.loadDecodeSql(checkList, "T1.CHECK_STATUS");
        sql.append(" SELECT T1.DLT_ID, \n");
        sql.append("        T1.ABJUSTMENT_ID, \n");
        sql.append("        T1.STOCK_ID, \n");
        sql.append("        T1.BOOK_ID, \n");
        sql.append("        T3.IS_LOCKED, \n");
        sql.append("        T1.PART_ID, \n");
        sql.append("        T2.PART_OLDCODE, \n");
        sql.append("        T1.PART_CODE, \n");
        sql.append("        T1.PART_CNAME, \n");
        sql.append("        T2.UNIT, \n");
        sql.append("        T1.LOC_ID, \n");
        sql.append("        T1.LOC_CODE, \n");
        sql.append("        T1.LOC_NAME, \n");
        sql.append("        T1.ABJUSTMENT_NUM, \n");
        sql.append("        T4.NORMAL_QTY, \n");
        sql.append("        \n");
        sql.append("        (SELECT SUM(S1.ITEM_QTY)\n");
        sql.append("           FROM TT_PART_ITEM_STOCK S1\n");
        sql.append("          WHERE S1.STATE IN (1, 2, 4)\n");
        sql.append("            AND S1.PART_ID = T3.PART_ID\n");
        sql.append("            AND S1.WH_ID = T3.WH_ID\n");
        sql.append("            AND S1.ORG_ID = T3.ORG_ID\n");
        sql.append("            AND S1.LOC_ID = T3.LOC_ID\n");
        sql.append("            AND S1.STATUS = 1) ITEM_QTY,\n");
        sql.append("        \n");
        sql.append("        T1.BATCH_CODE, \n");
        sql.append("        T1.DEAL_STATUS, \n");
        sql.append("       " + dealDecode + " DEAL_STATUS_DESC,\n");
        sql.append("        T1.CHECK_STATUS, \n");
        sql.append("       " + checkDecode + " CHECK_STATUS_DESC,\n");
        sql.append("        T1.REMARK \n");
        sql.append("   FROM TT_PART_STOCK_ABJUSTMENT_DTL T1, \n");
        sql.append("        TT_PART_DEFINE T2, \n");
        sql.append("        TT_PART_ITEM_STOCK T3,  \n");    
        sql.append("        TT_PART_BOOK T4  \n");    
        sql.append("  WHERE T1.PART_ID = T2.PART_ID \n");
        sql.append("    AND T1.STOCK_ID = T3.STOCK_ID \n");
        sql.append("    AND T1.BOOK_ID = T4.BOOK_ID \n");
        sql.append("    AND T1.ABJUSTMENT_ID = '" + paramMap.get("abjustmentId") + "' \n");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }





    
    /**
     * <p>
     * Description: 返回主机厂名称
     * </p>
     * 
     * @param oemCompanyId
     * @return
     */
    public String getMainCompanyName(String oemCompanyId) {
        String companyName = "";
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TM.COMPANY_NAME FROM TM_COMPANY TM  WHERE 1 = 1 ");
        sql.append("  AND TM.COMPANY_ID = '" + oemCompanyId + "' ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            companyName = list.get(0).get("COMPANY_NAME").toString();
        }
        return companyName;
    }

    /**
     * <p>
     * Description: 返回服务商名称
     * </p>
     * 
     * @param oemCompanyId
     * @return
     */
    public String getDealerName(String oemCompanyId) {
        String companyName = "";
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TD.DEALER_NAME  FROM TM_DEALER TD  WHERE 1 = 1  ");
        sql.append("  AND TD.DEALER_ID = '" + oemCompanyId + "' ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            companyName = list.get(0).get("DEALER_NAME").toString();
        }
        return companyName;
    }

    /**
     * <p>
     * Description: 返回仓库信息List
     * </p>
     * 
     * @param sbString
     * @return
     */
    public List<Map<String, Object>> getWareHouses(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TM.WH_ID, TM.WH_NAME AS WH_CNAME FROM TT_PART_WAREHOUSE_DEFINE TM WHERE 1 = 1  ");
        sql.append(" AND TM.STATE = '" + Constant.STATUS_ENABLE + "' ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.WH_ID ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * <p>
     * Description: 查询有效的配件库存
     * </p>
     * 
     * @param paramMap 查询参数
     * @param pageSize 每页数量
     * @param curPage 页码
     * @return
     */
    public PageResult<Map<String, Object>> getValidPartsStock(Map<String, String> paramMap, Integer pageSize,
            Integer curPage) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * \n");
        sql.append("   FROM VW_PART_STOCK T \n");
        sql.append("  WHERE 1 = 1 \n");
        if (!CommonUtils.isEmpty(paramMap.get("partCode"))) {
            sql.append("    AND T.PART_CODE = '" + paramMap.get("partCode") + "' \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("partOldcode"))) {
            sql.append("    AND T.PART_OLDCODE = '" + paramMap.get("partOldcode") + "' \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("partCname"))) {
            sql.append("    AND T.PART_CNAME like '%" + paramMap.get("partCname") + "%' \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("whId"))) {
            sql.append("    AND T.WH_ID = '" + paramMap.get("whId") + "' \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("itemQtyRangeStart"))) {
            sql.append("    AND T.ITEM_QTY >= '" + paramMap.get("itemQtyRangeStart") + "' \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("itemQtyRangeEnd"))) {
            sql.append("    AND T.ITEM_QTY <= '" + paramMap.get("itemQtyRangeEnd") + "' \n");
        }
        sql.append("  ORDER BY T.PART_OLDCODE \n");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * <p>
     * Description: 根据条件查询配件可用库存信息
     * </p>
     * 
     * @param paramMap 查询参数
     * @return
     */
    public List<Map<String, Object>> getPartStockList(Map<String, String> paramMap) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T1.BOOK_ID, T1.NORMAL_QTY, T1.BOOKED_QTY\n");
        sql.append("  FROM TT_PART_BOOK T1\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND T1.PART_ID = '" + paramMap.get("partId") + "'\n");
        sql.append("   AND T1.LOC_ID = '" + paramMap.get("locId") + "'\n");
        sql.append("   AND T1.BATCH_NO = '" + paramMap.get("batchNo") + "'\n");
        sql.append("   AND T1.WH_ID = '" + paramMap.get("whId") + "'\n");
        sql.append("   AND T1.ORG_ID = '" + paramMap.get("orgId") + "'\n");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
    
    /**
     * <p>
     * Description: 根据调整id获取调整配件的可用库存信息
     * </p>
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> getAbjPartsStockList(Map<String, String> paramMap){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T.BOOK_ID,\n");
        sql.append("       T2.PART_ID,\n");
        sql.append("       T2.PART_OLDCODE,\n");
        sql.append("       T.NORMAL_QTY,\n");
        sql.append("       T.BOOKED_QTY,\n");
        sql.append("       T2.ABJUSTMENT_NUM\n");
        sql.append("  FROM TT_PART_BOOK T, TT_PART_STOCK_ABJUSTMENT_DTL T2\n");
        sql.append(" WHERE T.BOOK_ID = T2.BOOK_ID\n");
        sql.append("   AND T2.ABJUSTMENT_ID = '"+paramMap.get("abjustmentId")+"'\n");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * <p>
     * Description: 获取调整的库存id
     * </p>
     * 
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> getItemStockList(Map<String, String> paramMap) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT T2.STOCK_ID, T1.DLT_ID \n");
        sql.append("   FROM TT_PART_STOCK_ABJUSTMENT_DTL T1, TT_PART_ITEM_STOCK T2 \n");
        sql.append("  WHERE T1.STOCK_ID = T2.STOCK_ID \n");
        sql.append("    AND T1.ABJUSTMENT_ID = '" + paramMap.get("abjustmentId") + "' \n");
        sql.append("    AND T2.IS_LOCKED = '" + paramMap.get("isLocked") + "' \n");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * <p>
     * Description: 获取库存调整明细数量
     * </p>
     * 
     * @param paramMap 查询参数
     * @return
     */
    public Map<String, Object> getAbjustmentDtlCount(Map<String, String> paramMap) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT COUNT(1) COUNT \n");
        sql.append("   FROM TT_PART_STOCK_ABJUSTMENT_DTL T \n");
        sql.append("  WHERE T.ABJUSTMENT_ID = '" + paramMap.get("abjustmentId") + "' \n");
        sql.append("    AND CHECK_STATUS = '" + paramMap.get("checkStatus") + "' \n");
        Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
        return map;
    }

    /**
     * <p>
     * Description: 更新库存数量
     * </p>
     * 
     * @param paramMap 查询参数
     */
    public int updateItemQty(Map<String, String> paramMap) {
        StringBuffer sql = new StringBuffer();
        sql.append("  \n");
        sql.append(" UPDATE TT_PART_ITEM_STOCK T1 \n");
        sql.append(" SET T1.ITEM_QTY = \n");
        sql.append("     (SELECT T1.ITEM_QTY " + paramMap.get("operator") + " T2.ABJUSTMENT_NUM \n");
        sql.append("        FROM TT_PART_STOCK_ABJUSTMENT_DTL T2 \n");
        sql.append("       WHERE T1.STOCK_ID = T2.STOCK_ID \n");
        sql.append("         AND T2.ABJUSTMENT_ID = '" + paramMap.get("abjustmentId") + "'), \n");
        sql.append("     T1.IS_LOCKED   = '" + paramMap.get("isLocked") + "', \n");
        sql.append("     T1.UPDATE_BY   = '" + paramMap.get("userId") + "', \n");
        sql.append("     T1.UPDATE_DATE = SYSDATE \n");
        sql.append("   WHERE T1.STOCK_ID IN (SELECT T3.STOCK_ID \n");
        sql.append("            FROM TT_PART_STOCK_ABJUSTMENT_DTL T3 \n");
        sql.append("           WHERE T1.STOCK_ID = T3.STOCK_ID \n");
        sql.append("             AND T3.ABJUSTMENT_ID = '" + paramMap.get("abjustmentId") + "' \n");
        if (!CommonUtils.isEmpty(paramMap.get("dtlId"))) {
            sql.append("             AND T3.DLT_ID = '" + paramMap.get("dtlId") + "' \n");
        }
        sql.append("            ) \n");
        return dao.update(sql.toString(), null);
    }
    
    public int updateNormalQty(Map<String, String> paramMap) {
        StringBuffer sql = new StringBuffer();
        sql.append("  \n");
        sql.append(" UPDATE TT_PART_BOOK T1 \n");
        sql.append(" SET T1.NORMAL_QTY = \n");
        sql.append("     (SELECT T1.NORMAL_QTY " + paramMap.get("operator") + " T2.ABJUSTMENT_NUM \n");
        sql.append("        FROM TT_PART_STOCK_ABJUSTMENT_DTL T2 \n");
        sql.append("       WHERE T1.BOOK_ID = T2.BOOK_ID \n");
        sql.append("         AND T2.ABJUSTMENT_ID = '" + paramMap.get("abjustmentId") + "'), \n");
        sql.append("     T1.UPDATE_BY   = '" + paramMap.get("userId") + "', \n");
        sql.append("     T1.UPDATE_DATE = SYSDATE \n");
        sql.append("   WHERE T1.BOOK_ID IN (SELECT T3.BOOK_ID \n");
        sql.append("            FROM TT_PART_STOCK_ABJUSTMENT_DTL T3 \n");
        sql.append("           WHERE T1.BOOK_ID = T3.BOOK_ID \n");
        sql.append("             AND T3.ABJUSTMENT_ID = '" + paramMap.get("abjustmentId") + "' \n");
        if (!CommonUtils.isEmpty(paramMap.get("dtlId"))) {
            sql.append("             AND T3.DLT_ID = '" + paramMap.get("dtlId") + "' \n");
        }
        sql.append("            ) \n");
        return dao.update(sql.toString(), null);
    }
}
