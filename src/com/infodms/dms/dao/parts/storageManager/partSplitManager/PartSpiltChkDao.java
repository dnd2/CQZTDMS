package com.infodms.dms.dao.parts.storageManager.partSplitManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PartSpiltChkDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartSpiltChkDao.class);

    private static final PartSpiltChkDao dao = new PartSpiltChkDao();

    private PartSpiltChkDao() {

    }

    public static final PartSpiltChkDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> queryPartSpiltApplyList(
            String spcpdCode, String startDate, String endDate,
            AclUserBean logonUser, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T1.spcpd_id, T1.spcpd_code,T1.org_cname, T2.NAME CREATE_NAME,T1.CREATE_DATE,T1.wh_cname,T3.LOC_NAME,T1.part_code,T1.part_oldcode,T1.part_name,T1.spcpd_type,T1.qty,T1.normal_qty,T1.STATE FROM TT_PART_SPCP_MAIN T1,TC_USER T2,TT_PART_LOACTION_DEFINE T3");
            sql.append(" WHERE T1.CREATE_BY=T2.USER_ID AND T1.LOC_ID=T3.LOC_ID");
            sql.append(" AND T1.org_id=").append(logonUser.getOrgId());
            sql.append(" AND T1.STATE=").append(Constant.PART_SPCPD_STATUS_01);
            if (!"".equals(spcpdCode)) {
                sql.append(" AND T1.spcpd_code LIKE '%")
                        .append(spcpdCode).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
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

}
