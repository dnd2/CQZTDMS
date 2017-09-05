package com.infodms.dms.dao.parts.storageManager.partReturnManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

public class PartOemReturnChkDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PartOemReturnChkDao.class);

    private static final PartOemReturnChkDao dao = new PartOemReturnChkDao();

    private PartOemReturnChkDao() {

    }

    public static final PartOemReturnChkDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public PageResult<Map<String, Object>> queryPartOemReturnApplyList(
            String returnCode, String startDate, String endDate,
            AclUserBean logonUser, Integer curPage, Integer pageSize) throws Exception {

        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T1.RETURN_ID,\n");
            sql.append("       T1.RETURN_CODE,\n");
            sql.append("       T1.ORG_NAME,\n");
            sql.append("       T2.NAME CREATE_NAME,\n");
            sql.append("       T1.CREATE_DATE,\n");
            sql.append("       T1.REMARK,\n");
            sql.append("       T1.APPLY_DATE,\n");
            sql.append("       DECODE(T1.RETURN_TO, 1, '退回供应商', '退回北汽银翔') RETURN_TO\n");
            sql.append("  FROM TT_PART_OEM_RETURN_MAIN T1, TC_USER T2\n");
            sql.append(" WHERE T1.CREATE_BY = T2.USER_ID");
            sql.append(" and T1.CREATE_ORG=").append(logonUser.getOrgId());
            sql.append(" and T1.state=").append(Constant.PART_OEM_RETURN_STATUS_01);

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
            sql.append("SELECT T1.RETURN_ID, T1.RETURN_CODE,T1.ORG_NAME,T1.REMARK, T1.CHECK_CODE IN_CODE FROM TT_PART_OEM_RETURN_MAIN T1 WHERE T1.RETURN_ID=");
            sql.append(CommonUtils.parseLong(returnId));
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

    }

    @SuppressWarnings("unchecked")
    public PageResult<Map<String, Object>> queryPartOemReturnApplyDetailList(
            String returnId, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT T.DTL_ID,\n");
            sql.append("       T.IN_ID,\n");
            sql.append("       T.PART_ID,\n");
            sql.append("       T.PART_CODE,\n");
            sql.append("       T.PART_OLDCODE,\n");
            sql.append("       T.PART_CNAME,\n");
            sql.append("       T.VENDER_NAME,\n");
            sql.append("       T.IN_QTY,\n");
            sql.append("       T.ITEM_QTY,\n");
            sql.append("       VW.NORMAL_QTY,\n");
            sql.append("       VW.LOC_CODE,\n");
            sql.append("       T.APPLY_QTY\n");
            sql.append("  FROM TT_PART_OEM_RETURN_DTL T, TT_PART_OEM_RETURN_MAIN TM, VW_PART_STOCK VW\n");
            sql.append(" WHERE T.STOCK_IN = VW.WH_ID\n");
            sql.append("   AND T.PART_ID = VW.PART_ID\n");
            sql.append("   AND T.LOC_ID = VW.LOC_ID\n");
            sql.append("   AND T.BATCH_NO = VW.BATCH_NO\n");
            sql.append("   AND TM.ORG_ID = VW.ORG_ID\n");
            sql.append("   AND T.RETURN_ID = TM.RETURN_ID\n");
            sql.append("   AND T.RETURN_ID=").append(CommonUtils.parseLong(returnId));
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;

    }

}
