package com.infodms.dms.dao.parts.baseManager.partsBaseManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;
import flex.messaging.io.ArrayList;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : luole CreateDate : 2013-4-2
 * @ClassName : PartWareHouseDao
 * @Description : 配件仓库维护DAO
 */
public class PartWareHouseDao extends BaseDao<PO> {
    public static final Logger logger = Logger.getLogger(PartWareHouseDao.class);
    private static final PartWareHouseDao dao = new PartWareHouseDao();

    private PartWareHouseDao() {
    }

    public static final PartWareHouseDao getInstance() {
        return dao;
    }

    /**
     * @param : @param orgCode 所属机构代码
     * @param : @return 所属机构ID
     * @return :
     * @throws : luole LastDate : 2013-4-2
     * @Title : 根据所属机构代码获得ID
     * @Description: TODO
     */
    public Long getOrgIdByOrgCode(String orgCode) {
        String sql = "select t.dealer_id from tm_dealer t where t.dealer_code='" + orgCode + "'";
        List<Map<String, Object>> list = pageQuery(sql, null, getFunName());

        Long i = (Long) (list.get(0).get("DEALER_ID"));
        System.out.println(i);
        return i;
    }

    /**
     * @param : @param conSql sql条件
     * @param : @param curPage 当前页
     * @param : @param pageSize 一页数量
     * @param : @return
     * @return :
     * @throws : luole LastDate : 2013-4-2
     * @Title :
     * @Description: TODO
     */
    public PageResult<Map<String, Object>> getPartPageQuery(String conSql, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("select  * from tt_part_warehouse_define tpwd where 1=1 ");
        sql.append(conSql);
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param : @param logonUser
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-5-4
     * @Title : 通用车厂和经销商ID取值方法
     * @Description: TODO
     */
    public List<OrgBean> getOrgInfo(AclUserBean logonUser) {
        StringBuilder sql = new StringBuilder();
        if (logonUser.getDealerId() == null) {
            sql.append("SELECT P.COMPANY_ID COMPANY_ID,\n");
            sql.append("       P.ORG_CODE   COMPANY_CODE,\n");
            sql.append("       P.ORG_NAME   COMPANY_NAME\n");
            sql.append("  FROM TM_ORG P\n");
            sql.append(" WHERE p.Org_Id = " + logonUser.getOrgId());
            sql.append(" AND p.STATUS = " + Constant.STATUS_ENABLE);
            ArrayList list = new ArrayList();
            return factory.select(sql.toString(), list, new DAOCallback<OrgBean>() {
                public OrgBean wrapper(ResultSet rs, int paramInt) {
                    OrgBean bean = new OrgBean();
                    try {
                        bean.setOrgId(rs.getLong("company_id"));
                        bean.setOrgName(rs.getString("company_name"));
                        bean.setOrgCode(rs.getString("company_code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return bean;
                }
            });
        } else {
            sql.append("SELECT DEALER_ID,DEALER_NAME ,DEALER_CODE\n");
            sql.append("        FROM TM_DEALER D\n");
            sql.append("       WHERE D.DEALER_ID = " + Long.parseLong(logonUser.getDealerId()));
            sql.append("         AND D.STATUS =  " + Constant.STATUS_ENABLE);
            ArrayList list = new ArrayList();
            return factory.select(sql.toString(), list, new DAOCallback<OrgBean>() {
                public OrgBean wrapper(ResultSet rs, int idx) {
                    OrgBean bean = new OrgBean();
                    try {
                        bean.setOrgId(rs.getLong("DEALER_ID"));
                        bean.setOrgName(rs.getString("DEALER_NAME"));
                        bean.setOrgCode(rs.getString("DEALER_CODE"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return bean;
                }
            });
        }
    }
}
