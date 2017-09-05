package com.infodms.dms.dao.parts.storageManager.partSplitManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PartSpiltApplyDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PartSpiltApplyDao.class);

    private static final PartSpiltApplyDao dao = new PartSpiltApplyDao();

    private PartSpiltApplyDao() {

    }

    public static final PartSpiltApplyDao getInstance() {
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
            sql.append("SELECT T1.spcpd_id, T1.spcpd_code,T1.org_cname, T2.NAME CREATE_NAME,T1.CREATE_DATE,T1.wh_cname,T3.loc_name,T1.part_code,T1.part_oldcode,T1.part_name,T1.spcpd_type,T1.qty,T1.normal_qty,T1.STATE FROM TT_PART_SPCP_MAIN T1,TC_USER T2,TT_PART_LOACTION_DEFINE T3");
            sql.append(" WHERE T1.CREATE_BY=T2.USER_ID AND T1.LOC_ID=T3.LOC_ID");
            sql.append(" AND T1.org_id=").append(logonUser.getOrgId());
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

    public List getPartWareHouseList(AclUserBean logonUser) throws Exception {
        try {
            TtPartWarehouseDefinePO po = new TtPartWarehouseDefinePO();
            po.setState(Constant.STATUS_ENABLE);
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

    public PageResult<Map<String, Object>> queryPartByWhIdList(String whId, String spcpdType,
                                                               AclUserBean logonUser, String partOldCode, String partCname, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            //如果是拆件
            if (CommonUtils.parseInteger(spcpdType).equals(Constant.PART_SPCPD_TYPE_01)) {
                sql.append(" SELECT T.PART_ID, \n");
                sql.append("        T.WH_ID, \n");
                sql.append("        T.ORG_ID, \n");
                sql.append("        T.PART_CODE, \n");
                sql.append("        T.PART_OLDCODE, \n");
                sql.append("        T.PART_CNAME, \n");
                sql.append("        T.UNIT, \n");
                sql.append("        T.NORMAL_QTY, \n");
                sql.append("        T1.LOC_ID, \n");
                sql.append("        T1.LOC_CODE, \n");
                sql.append("        T1.LOC_NAME, \n");
                sql.append("        T2.WH_NAME \n");
                sql.append("   FROM VW_PART_STOCK            T, \n");
                sql.append("        TT_PART_LOACTION_DEFINE  T1, \n");
                sql.append("        TT_PART_WAREHOUSE_DEFINE T2 \n");
                sql.append("  WHERE T.LOC_ID = T1.LOC_ID \n");
                sql.append("    AND T1.WH_ID = T2.WH_ID \n");
                sql.append("    AND T.ORG_ID = ").append(logonUser.getOrgId());
                sql.append("    AND T.WH_ID = ").append(whId);

            }
            //如果是合件
            if (CommonUtils.parseInteger(spcpdType).equals(Constant.PART_SPCPD_TYPE_02)) {
                sql.append("select T.*,nvl(t3.NORMAL_QTY,0) NORMAL_QTY from (select t1.part_id,t1.wh_id,t1.loc_id,t1.loc_code,t1.loc_name,t1.org_id,t2.part_code,t2.part_oldcode,t2.part_cname,nvl(t2.unit,'件') unit");
                sql.append(" from tt_part_loaction_define t1,tt_part_define t2 where t1.part_id=t2.part_id and t1.org_id=").append(logonUser.getOrgId());
                sql.append(" and t1.wh_id=").append(whId);
                sql.append(") T,vw_part_stock t3 where t.part_id=t3.PART_ID(+) and t.org_id=t3.org_id(+) and t.wh_id=t3.wh_id(+)");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND T.PART_OLDCODE LIKE '%")
                        .append(partOldCode).append("%'\n");
            }
            if (!"".equals(partCname)) {
                sql.append(" AND T.PART_CNAME LIKE '%")
                        .append(partCname).append("%'\n");
            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> querySubpartList(String partId, String whId,
                                                            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("select t4.SUBPART_ID,t4.SUBPART_CODE,t4.SUBPART_OLDCODE,t4.SUBPART_CNAME,nvl(t5.unit,'件') unit,T4.SPLIT_NUM,t4.COST_RATE,t4.QTY,t4.PART_ID,t4.LOC_ID,t4.LOC_CODE,t4.LOC_NAME,NVL(T3.NORMAL_QTY, 0) NORMAL_QTY,t4.wh_id");
            sql.append("    from (select T1.SUBPART_ID,T1.SUBPART_CODE,T1.SUBPART_OLDCODE,T1.SUBPART_CNAME,T1.SPLIT_NUM,T1.COST_RATE,(T1.SPLIT_NUM * 0) QTY,T2.PART_ID,T2.LOC_ID,T2.LOC_CODE,T2.LOC_NAME,t2.wh_id");
            sql.append("  FROM TT_PART_SPLIT_DEFINE T1 JOIN TT_PART_LOACTION_DEFINE T2 ON T1.SUBPART_ID = T2.PART_ID WHERE T1.PART_ID =");
            if ("".equals(partId)) {
                sql.append(0);
            } else {
                sql.append(partId);
            }
            sql.append(" and t2.wh_id=").append(whId);
            sql.append(" AND T1.STATE =").append(Constant.STATUS_ENABLE);
            sql.append(") t4,VW_PART_STOCK T3,tt_part_define t5");
            sql.append(" where T4.SUBPART_ID = T3.PART_ID(+) and t3.WH_ID(+)=").append(whId);
            sql.append("and t4.SUBPART_ID=t5.part_id");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public Map getPartSpcpMainInfo(String spcpdId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.SPCPD_ID,T.SPCPD_CODE,T.ORG_CNAME,U.NAME,T.WH_CNAME,T.PART_OLDCODE,T.PART_CODE,T.PART_NAME,T.SPCPD_TYPE,T.QTY,T.NORMAL_QTY,T.UNIT,T3.LOC_NAME,T.REMARK,T.REMARK1");
            sql.append(" FROM TT_PART_SPCP_MAIN T,TC_USER U,TT_PART_LOACTION_DEFINE T3 WHERE T.CREATE_BY=U.USER_ID AND T.LOC_ID=T3.LOC_ID AND T.SPCPD_ID=");
            sql.append(CommonUtils.parseLong(spcpdId));
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> querySubpartBySpcpdIdList(
            String spcpdId, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T1.PART_CODE,T1.PART_OLDCODE,T1.PART_CNAME,T1.UNIT,T1.SPLIT_QTY,T1.SPLIT_RATE,T2.LOC_NAME,T1.QTY,T1.NORMAL_QTY,T1.REMARK");
            sql.append(" FROM TT_PART_SPLIT_COMPOUND_DTL T1,TT_PART_LOACTION_DEFINE T2 WHERE T1.LOC_ID=T2.LOC_ID AND T1.SPCPD_ID=")
                    .append(spcpdId);
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public Long queryNormalQty(String partId, String whId, Long orgId) throws Exception {
        Long normalQty = 0l;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT V.NORMAL_QTY FROM VW_PART_STOCK V WHERE V.WH_ID=").append(whId)
                    .append(" AND V.PART_ID=").append(partId)
                    .append(" AND V.ORG_ID=")
                    .append(orgId);
            Map map = pageQueryMap(sql.toString(), null, getFunName());
            if (map != null) {
                normalQty = ((BigDecimal) map.get("NORMAL_QTY")).longValue();
            }
        } catch (Exception e) {
            throw e;
        }
        return normalQty;
    }

}
