package com.infodms.dms.dao.parts.baseManager.partsBaseManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.*;

/**
 * @author : luole
 *         CreateDate     : 2013-4-2
 * @ClassName : PartWareHouseDao
 * @Description : 配件仓库维护DAO
 */
public class PartSalesRelationDao extends BaseDao<PO> {
    public static final Logger logger = Logger.getLogger(PartSalesRelationDao.class);
    private static final PartSalesRelationDao dao = new PartSalesRelationDao();

    private PartSalesRelationDao() {
    }

    public static final PartSalesRelationDao getInstance() {
        return dao;
    }

    /**
     * @param : @param conSql  条件 SQl
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-3
     * @Title :
     * @Description: TODO
     */
    public PageResult<Map<String, Object>> selPartPageQuery(String conSql, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from vw_PART_SALES_RELATION p  where 1=1 ");
        sql.append(conSql);
        /*sql.append(" ORDER BY p.parentorg_code DESC ");*/
        sql.append(" order by create_date desc ");
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }
    /**
     * @Title      :取得上级ID
     * @Description: TODO
     * @param      : @param relId
     * @param      : @return
     * @return     :
     * @throws     : luole
     * LastDate    : 2013-4-9
     *//*
	public String getRelFather(Long relId){
		StringBuffer sql = new StringBuffer();
		sql.append("select PARENTORG_ID from TT_PART_SALES_RELATION t where  t.CHILDORG_ID  = "+relId);
		List<Map<String,Object>> list =pageQuery(sql.toString(), null, getFunName());
		return (list.get(0).get("PARENTORG_ID")).toString();
	}
	*/

    /**
     * @param : @param relId
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-9
     * @Title : 查看下级
     * @Description: TODO
     */
    public List<Map<String, Object>> getPartSalesSun(String relId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from TT_PART_SALES_RELATION t where  t.PARENTORG_ID  = " + relId);
        return pageQuery(sql.toString(), null, getFunName());
    }

    /**
     * @param : @param fatherId
     * @param : @param dealerId
     * @param : @return  存在返回true  不存在返回false
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-9
     * @Title : 验证当前是否存在
     * @Description: TODO
     */
    public boolean checkRelation(String fatherId, String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from TT_PART_SALES_RELATION t where  t.parentorg_id  = " + fatherId);
        sql.append("  and t.childorg_id =" + dealerId);
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() == 0)
            return false;
        return true;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Map<String, Object>> queryPartSRelation(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {

            String fName = request.getParamValue("FATHER_NAME"); // 上级名称
            String sName = request.getParamValue("SUN_NAME"); // 下级名称
            String state = request.getParamValue("STATE"); // 状态
            StringBuffer sql = new StringBuffer();
            sql.append("select p.PARENTORG_CODE,p.PARENTORG_NAME,p.CHILDORG_CODE,p.CHILDORG_NAME,decode(p.STATE,")
                    .append(Constant.STATUS_ENABLE).append(",'有效',")
                    .append(Constant.STATUS_DISABLE).append(",'无效') STATE");
            sql.append(" from TT_PART_SALES_RELATION p  where 1=1 ");
            if (!CommonUtils.isNullString(fName)) {
                sql.append(" and  p. PARENTORG_NAME like '%" + fName + "%' ");
            }
            if (!CommonUtils.isNullString(sName)) {
                sql.append(" and  p.CHILDORG_NAME  like '%" + sName + "%' ");
            }
            if (!CommonUtils.isNullString(state)) {
                sql.append("  and p.STATE  =  " + state);
            }

            sql.append(" ORDER BY p.parentorg_code DESC ");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;
    }

    public Map<String, Object> validateChildOrgcode(String subCell) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "select t.DEALER_ID,t.DEALER_CODE,t.DEALER_NAME from TM_DEALER t where t.DEALER_CODE='")
                    .append(subCell).append("'");
            Map<String, Object> map = pageQueryMap(sql.toString(), null,
                    getFunName());

            return map;
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<String, Object> validateParentOrgCode(String subCell) throws Exception {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            StringBuilder sql = new StringBuilder("");
            StringBuilder sql1 = new StringBuilder("");
            sql.append(
                    "select t.DEALER_ID COMPANY_ID,t.DEALER_CODE COMPANY_CODE,t.DEALER_NAME COMPANY_NAME from TM_DEALER t where t.DEALER_CODE='")
                    .append(subCell).append("'");
            sql1.append(
                    "select t.company_id,t.company_name,t.company_code from TM_COMPANY t where t.company_code='")
                    .append(subCell).append("'");
            sql1.append(" and t.company_type=").append(Constant.COMPANY_TYPE_SGM);

            map = pageQueryMap(sql.toString(), null,
                    getFunName());
            if (map == null) {
                map = pageQueryMap(sql1.toString(), null,
                        getFunName());
            }

            return map;
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isExist(String childorgCode, String parentorgCode) throws Exception {
        boolean flag = true;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "SELECT * FROM TT_PART_SALES_RELATION T WHERE T.CHILDORG_CODE='")
                    .append(childorgCode).append("'");
            sql.append(" AND T.PARENTORG_CODE='").append(parentorgCode).append("'");
            Map<String, Object> map = pageQueryMap(sql.toString(), null,
                    getFunName());

            if (map == null) {
                flag = false;
            }
        } catch (Exception e) {
            throw e;
        }
        return flag;
    }

    public Map<String, Object> isMeanwhile(String childorgCode,
                                           String parentorgCode) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "SELECT * FROM TT_PART_SALES_RELATION T WHERE T.CHILDORG_CODE='")
                    .append(parentorgCode).append("'");
            sql.append(" AND T.PARENTORG_CODE='").append(childorgCode).append("'");
            Map<String, Object> map = pageQueryMap(sql.toString(), null,
                    getFunName());
            return map;
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateChildOrg(String pId, int flag) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            StringBuilder sql = new StringBuilder("");
            List<Object> params = new ArrayList<Object>();
            if (flag == 0) {
                sql.append("UPDATE TT_PART_SALES_RELATION T SET T.STATE=?,T.DISABLE_DATE=?,T.DISABLE_BY=? WHERE T.PARENTORG_ID=?");
                params.add(Constant.STATUS_DISABLE);
                params.add(new Date());
                params.add(logonUser.getUserId());
            } else {
                sql.append("UPDATE TT_PART_SALES_RELATION T SET T.STATE=? WHERE T.PARENTORG_ID=?");
                params.add(Constant.STATUS_ENABLE);
            }
            params.add(pId);
            update(sql.toString(), params);
        } catch (Exception e) {
            throw e;
        }
    }


    public void deleteChildOrg(String pId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("");
            List<Object> params = new ArrayList<Object>();
            sql.append("DELETE FROM TT_PART_SALES_RELATION T WHERE T.PARENTORG_ID=?");
            params.add(pId);
            update(sql.toString(), params);
        } catch (Exception e) {
            throw e;
        }
    }
}
