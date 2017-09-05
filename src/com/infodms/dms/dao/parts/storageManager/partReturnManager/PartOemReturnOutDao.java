package com.infodms.dms.dao.parts.storageManager.partReturnManager;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class PartOemReturnOutDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PartOemReturnOutDao.class);

    private static final PartOemReturnOutDao dao = new PartOemReturnOutDao();

    private PartOemReturnOutDao() {

    }

    public static final PartOemReturnOutDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public PageResult<Map<String, Object>> queryPartOemReturnList(
            String returnCode, String startDate, String endDate,
            AclUserBean logonUser, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");


            sql.append("SELECT T1.RETURN_ID,\n");
            sql.append("       T1.RETURN_CODE,\n");
            sql.append("       T1.ORG_NAME,\n");
            sql.append("       T1.ORG_NAME    CREATE_ORG,\n");
            sql.append("       T2.NAME        CREATE_NAME,\n");
            sql.append("       T1.CREATE_DATE,\n");
            sql.append("       T1.REMARK,\n");
            sql.append("       T1.APPLY_DATE,\n");
            sql.append("       T1.VERIFY_DATE，\n");
            sql.append("       DECODE(T1.RETURN_TO, 1, '退回供应商', '退回北汽银翔') RETURN_TO\n");
            sql.append("  FROM TT_PART_OEM_RETURN_MAIN T1, TC_USER T2\n");
            sql.append(" WHERE T1.CREATE_BY = T2.USER_ID\n");

            sql.append(" and T1.ORG_ID=").append(logonUser.getOrgId());
            sql.append(" and T1.state=").append(Constant.PART_OEM_RETURN_STATUS_03);
            if (!"".equals(returnCode)) {
                sql.append(" AND T1.RETURN_CODE LIKE '%")
                        .append(returnCode).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append(" AND T1.STATUS=1");
            sql.append(" ORDER BY T1.CREATE_DATE ASC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getPartOemReturnMainInfo(String returnId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T1.RETURN_ID, T1.RETURN_CODE,T1.ORG_NAME ,T1.REMARK, T1.CHECK_CODE IN_CODE FROM TT_PART_OEM_RETURN_MAIN T1 WHERE T1.RETURN_ID=");
            sql.append(CommonUtils.parseLong(returnId));
            return pageQueryMap(sql.toString(), null, getFunName());

        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public PageResult<Map<String, Object>> queryPartOemReturnChkList(
            String returnId, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T1.DTL_ID, \n");
            sql.append("        T1.IN_ID, \n");
            sql.append("        T1.IN_CODE, \n");
            sql.append("        T1.PART_CODE, \n");
            sql.append("        T1.PART_OLDCODE, \n");
            sql.append("        T1.PART_CNAME, \n");
            sql.append("        T1.VENDER_NAME, \n");
            sql.append("        T2.WH_ID, \n");
            sql.append("        T2.WH_NAME, \n");
            sql.append("        T2.LOC_ID, \n");
            sql.append("        T2.LOC_CODE, \n");
            sql.append("        T2.BATCH_NO, \n");
            sql.append("        T1.IN_QTY, \n");
            sql.append("        VS.NORMAL_QTY, \n");
            sql.append("        T1.APPLY_QTY, \n");
            sql.append("        T1.CHECK_QTY, \n");
            sql.append("        DECODE(T2.STATE,92321001,'否','是') IS_BALANCES, \n");
            sql.append("        T1.REMARK \n");
            sql.append("   FROM TT_PART_OEM_RETURN_DTL T1, TT_PART_PO_IN T2, VW_PART_STOCK VS \n");
            sql.append("  WHERE T1.IN_ID=T2.IN_ID \n");
            sql.append("    AND T2.PART_ID=VS.PART_ID \n");
            sql.append("    AND T2.WH_ID=VS.WH_ID \n");
            sql.append("    AND T2.LOC_ID=VS.LOC_ID \n");
            sql.append("    AND T2.BATCH_NO=VS.BATCH_NO \n");
            sql.append("    AND T2.ORG_ID=VS.ORG_ID \n");
            sql.append("    AND T1.STATUS = 1 \n");
            sql.append("    AND T1.RETURN_ID = ").append(returnId);

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;

    }

    @SuppressWarnings("unchecked")
    public PageResult<Map<String, Object>> queryPartOemReturnChkList1(
            String returnId, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT T1.DTL_ID,\n");
            sql.append("       T1.PART_CODE,\n");
            sql.append("       T1.PART_OLDCODE,\n");
            sql.append("       T1.PART_CNAME,\n");
            sql.append("       VS.WH_ID,\n");
            sql.append("       VS.WH_NAME,\n");
            sql.append("       VS.NORMAL_QTY,\n");
            sql.append("       T1.APPLY_QTY,\n");
            sql.append("       T1.CHECK_QTY,\n");
            sql.append("       T1.REMARK,\n");
            sql.append("       T1.LOC_ID,\n");
            sql.append("       VS.LOC_CODE\n");
            sql.append("  FROM TT_PART_OEM_RETURN_DTL T1, VW_PART_STOCK VS\n");
            sql.append(" WHERE T1.STOCK_OUT = VS.WH_ID\n");
            sql.append("   AND T1.PART_ID = VS.PART_ID\n");
            sql.append("   AND T1.LOC_ID = VS.LOC_ID\n");
            sql.append("   AND T1.RETURN_ID = ").append(returnId);
            sql.append("   AND T1.STATUS = 1");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    @SuppressWarnings("unchecked")
    public boolean isAllOut(String returnId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T1.DTL_ID FROM TT_PART_OEM_RETURN_DTL T1 WHERE T1.RETURN_ID=").append(returnId);
            sql.append(" AND T1.STATUS=1");
            Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
            if (map != null) {
                return false;
            }
        } catch (Exception e) {
            throw e;
        }
        return true;
    }
}
