package com.infodms.dms.dao.parts.baseManager.partsBaseManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartSplitDefinePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartSplitDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PartSplitDao.class);

    private static final PartSplitDao dao = new PartSplitDao();

    private PartSplitDao() {

    }

    public static final PartSplitDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> queryPartSplitList(
            TtPartSplitDefinePO bean, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.SPLIT_ID,T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,T.SUBPART_ID,T.SUBPART_CODE,T.SUBPART_OLDCODE,T.SUBPART_CNAME,T.SPLIT_NUM,T.COST_RATE,T.REMARK,T.CREATE_DATE,T.UPDATE_DATE,T.STATE FROM TT_PART_SPLIT_DEFINE T WHERE T.STATUS=1\n");
            if (!"".equals(bean.getPartOldcode())) {
                sql.append("AND T.PART_OLDCODE LIKE '%").append(bean.getPartOldcode()).append("%'\n");
            }
            if (!"".equals(bean.getSubpartOldcode())) {
                sql.append("AND T.SUBPART_OLDCODE LIKE '%").append(bean.getSubpartOldcode()).append("%'\n");
            }
            if (bean.getState() != null) {
                sql.append("AND T.STATE=").append(bean.getState()).append("\n");
            }
            sql.append("ORDER BY T.CREATE_DATE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public Map<String, Object> checkPartId(String strPartId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "select t.PART_ID from TT_PART_SPLIT_DEFINE t where t.PART_ID=")
                    .append(strPartId).append(" and t.STATUS=1");
            Map<String, Object> map = pageQueryMap(sql.toString(), null,
                    getFunName());
            return map;
        } catch (Exception e) {
            throw e;
        }
    }

    public List getPartSplitDetail(String partId) throws Exception {
        List list;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT A.PART_CODE,  A.PART_OLDCODE,   A.PART_CNAME,  A.SUBPART_CODE, A.SUBPART_OLDCODE,A.SUBPART_CNAME,A.SPLIT_NUM,A.COST_RATE,A.REMARK ");
            sql.append(" FROM TT_PART_SPLIT_DEFINE A WHERE A.PART_ID = ")
                    .append(partId);
            list = pageQuery(sql.toString(), null, getFunName());

        } catch (Exception e) {
            throw e;
        }
        return list;
    }

}
