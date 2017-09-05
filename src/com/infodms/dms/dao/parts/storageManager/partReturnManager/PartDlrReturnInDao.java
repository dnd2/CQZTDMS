package com.infodms.dms.dao.parts.storageManager.partReturnManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartDlrReturnInDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PartDlrReturnInDao.class);

    private static final PartDlrReturnInDao dao = new PartDlrReturnInDao();

    private PartDlrReturnInDao() {

    }

    public static final PartDlrReturnInDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> queryPartDlrReturnApplyList(String returnCode, String dealerName,
            String startDate, String endDate, boolean flag, AclUserBean logonUser, String dealerCode, Integer curPage,
            Integer pageSize, String state) throws Exception {

        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(
                    "SELECT T1.RETURN_ID, T1.RETURN_CODE,T1.DEALER_CODE,T1.DEALER_NAME,T1.CREATE_ORGNAME CREATE_DEALER, T2.NAME CREATE_NAME,T1.CREATE_DATE,T1.REMARK,T1.APPLY_DATE,T1.Wl,T1.WlNO,T1.STATE,to_char(T1.Wl_DATE,'yyyy-mm-dd') Wl_DATE FROM TT_PART_DLR_RETURN_MAIN T1,TC_USER T2");
            sql.append(" WHERE T1.CREATE_BY=T2.USER_ID");
            sql.append(" and T1.SELLER_ID=");
            if (flag) {
                sql.append(logonUser.getOrgId());
            } else {
                sql.append(logonUser.getDealerId());
            }
            //			sql.append(" and T1.state=").append(Constant.PART_DLR_RETURN_STATUS_05);
            if (!"".equals(returnCode)) {
                sql.append(" AND T1.RETURN_CODE LIKE '%").append(returnCode).append("%'\n");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND T1.DEALER_NAME LIKE '%").append(dealerName).append("%'\n");
            }
            if (!"".equals(dealerCode)) {
                sql.append(" AND T1.DEALER_CODE LIKE '%").append(dealerCode).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)>=").append("to_date('").append(startDate)
                        .append("','yyyy-MM-dd')");
            }
            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)<=").append("to_date('").append(endDate)
                        .append("','yyyy-MM-dd')");
            }
            if (!"".equals(state) && null != state) {
                sql.append(" and T1.STATE=").append(state);
            }
            sql.append(" AND T1.STATUS=1");
            sql.append(" ORDER BY T1.Wl_DATE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;

    }

    public Map getPartDlrReturnMainInfo(String returnId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(
                    "SELECT T1.RETURN_ID, T1.RETURN_CODE,T1.CREATE_ORGNAME ORG_NAME,T1.DEALER_NAME,T1.SELLER_NAME,T1.SO_CODE,T1.REMARK FROM TT_PART_DLR_RETURN_MAIN T1 WHERE T1.RETURN_ID=");
            sql.append(CommonUtils.parseLong(returnId));
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

    }

    public PageResult<Map<String, Object>> queryPartDlrReturnInfoList(String returnId, String soCode, Integer curPage,
            Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT T.DTL_ID,\n");
            sql.append("       T.PART_ID,\n");
            sql.append("       T.PART_CODE,\n");
            sql.append("       T.PART_OLDCODE,\n");
            sql.append("       T.PART_CNAME,\n");
            sql.append("       NVL(T.SALES_QTY, 0) BUY_QTY,\n");
            sql.append("       T.APPLY_QTY,\n");
            sql.append("       T.CHECK_QTY,\n");
            sql.append("       T.OUT_QTY,\n");
            sql.append("       NVL(T.IN_QTY, 0) IN_QTY,\n");
            sql.append("       T.CHECK_QTY - NVL(T.IN_QTY, 0) MAX_QTY,\n");
            sql.append("       T.REMARK\n");
            sql.append("  FROM TT_PART_DLR_RETURN_DTL T\n");
            sql.append(" WHERE T.STATUS = 1\n");
            sql.append("   AND T.RETURN_ID = " + returnId + "");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List getPartWareHouseList(AclUserBean logonUser, String whType) throws Exception {
        try {
            TtPartWarehouseDefinePO po = new TtPartWarehouseDefinePO();
            po.setState(Constant.STATUS_ENABLE);
            if (!"".equals(whType) && null != whType) {
                po.setWhType(Integer.valueOf(whType));
            }
            po.setStatus(1);
            if (logonUser.getDealerId() != null) {
                po.setOrgId(CommonUtils.parseLong(logonUser.getDealerId()));
            } else {
                po.setOrgId(logonUser.getOrgId());
            }
            List list = select(po);
            return list;
        } catch (Exception e) {
            throw e;
        }

    }

    public boolean isAllIn(String returnId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T1.DTL_ID FROM TT_PART_DLR_RETURN_DTL T1 WHERE T1.RETURN_ID=").append(returnId);
            sql.append(" AND T1.STATUS=1");
            Map map = pageQueryMap(sql.toString(), null, getFunName());
            if (map != null) {
                return false;
            }
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    public void updateAccount(Long sellerId, Long dealerId, Double buyAmount) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("UPDATE TT_PART_ACCOUNT_DEFINE T ");
            sql.append(" SET T.ACCOUNT_SUM=T.ACCOUNT_SUM+").append(buyAmount);
            sql.append(" WHERE T.PARENTORG_ID=").append(sellerId).append(" AND T.CHILDORG_ID=").append(dealerId);
            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * <p>
     * Description: 根据退货明细id查询退货配件信息
     * </p>
     * @param dtlId 退货明细id
     * @return
     */
    public List<Map<String, Object>> getReturnPartDtl(String dtlId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT \n");
        sql.append("        RD.DTL_ID, \n");
        sql.append("        RD.RETURN_ID, \n");
        sql.append("        RD.PART_ID, \n");
        sql.append("        RD.PART_CODE, \n");
        sql.append("        RD.PART_OLDCODE, \n");
        sql.append("        RD.PART_CNAME, \n");
        sql.append("        RD.BUY_PRICE, \n");
        sql.append("        RD.UNIT, \n");
        sql.append("        RD.STATUS, \n");
        sql.append("        RD.REMARK, \n");
        sql.append("        TIS.BATCH_CODE BATCH_NO, \n");
        sql.append("        OD.LOC_ID, \n");
        sql.append("        TIS.VENDER_ID \n");
        sql.append("   FROM TT_PART_DLR_RETURN_MAIN M \n");
        sql.append("  INNER JOIN TT_PART_DLR_RETURN_DTL RD \n");
        sql.append("     ON M.RETURN_ID = RD.RETURN_ID \n");
        sql.append("  INNER JOIN TT_PART_DLR_INSTOCK_MAIN IM \n");
        sql.append("     ON IM.IN_ID = M.IN_ID \n");
        sql.append("  INNER JOIN TT_PART_TRANS S \n");
        sql.append("     ON S.TRANS_ID = IM.TRANS_ID \n");
        sql.append("  INNER JOIN TT_PART_OUTSTOCK_MAIN OM \n");
        sql.append("     ON OM.OUT_ID = S.OUT_ID \n");
        sql.append("  INNER JOIN TT_PART_OUTSTOCK_DTL OD \n");
        sql.append("     ON OD.OUT_ID = OM.OUT_ID \n");
        sql.append("    AND RD.PART_ID = OD.PART_ID \n");
        sql.append("  INNER JOIN TT_PART_ITEM_STOCK TIS \n");
        sql.append("     ON TIS.PART_ID = OD.PART_ID \n");
        sql.append("    AND TIS.ORG_ID = OM.SELLER_ID \n");
        sql.append("    AND TIS.WH_ID = OM.WH_ID \n");
        sql.append("    AND TIS.LOC_ID = OD.LOC_ID \n");
        sql.append("  WHERE RD.DTL_ID = '"+dtlId+"' \n");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
