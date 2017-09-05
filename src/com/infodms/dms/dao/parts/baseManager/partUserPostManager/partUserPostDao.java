package com.infodms.dms.dao.parts.baseManager.partUserPostManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.po.TtPartUserOrdertypeDefinePO;
import com.infoservice.po3.bean.PageResult;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-4-10
 * @ClassName : partUserPostDao
 */
public class partUserPostDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partUserPostDao.class);
    private static final partUserPostDao dao = new partUserPostDao();

    private partUserPostDao() {
    }

    public static final partUserPostDao getInstance() {
        return dao;
    }

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param orderID
     * @param : @param partCode
     * @param : @param partName
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 条件查询人员类型信息
     */
    public PageResult<Map<String, Object>> queryPartUserPost(String userName, String fixValue,
            String state, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append(
                "SELECT PU.DEFINE_ID, PU.USER_ID, PU.USER_TYPE, U.NAME, PF.FIX_GROUPNAME, PF.FIX_NAME, PF.FIX_VALUE, PU.STATE, PU.IS_LEADER,PU.IS_DIRECT,PU.IS_CHKZY "
                        + "FROM TC_USER U, TT_PART_USERPOSE_DEFINE PU, TT_PART_FIXCODE_DEFINE PF "
                        + "WHERE PU.USER_ID = U.USER_ID(+) AND  PF.FIX_VALUE = PU.USER_TYPE \n" + "AND U.USER_STATUS ='"
                        + Constant.STATUS_ENABLE + "' ");
        if (null != userName && !userName.equals("")) {
            sql.append(" AND U.NAME  like '%" + userName + "%' ");
        }

        if (null != fixValue && !fixValue.equals("")) {
            sql.append(" AND PF.FIX_VALUE =  '" + fixValue + "' ");
        }
        if (null != state && !state.equals("")) {
            sql.append(" AND PU.STATE = " + Integer.parseInt(state) + "");
        }
        sql.append(" AND PF.FIX_GOUPTYPE = " + Constant.FIXCODE_TYPE_01);
        sql.append(" ORDER BY pf.fix_name");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }
    
    /**
     * <p>Description: 根据人员类型和用户ID查询人员信息</p>
     * @param userId 用户ID
     * @param fixValue 人员类型
     * @return
     */
    public Map<String, Object> queryPartUserInfo(String userId, String userType){
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT PU.DEFINE_ID, \n");
        sql.append("        PU.USER_ID, \n");
        sql.append("        PU.USER_TYPE, \n");
        sql.append("        U.NAME, \n");
        sql.append("        PF.FIX_GROUPNAME, \n");
        sql.append("        PF.FIX_NAME, \n");
        sql.append("        PF.FIX_VALUE, \n");
        sql.append("        PU.STATE, \n");
        sql.append("        PU.IS_LEADER, \n");
        sql.append("        PU.IS_DIRECT, \n");
        sql.append("        PU.IS_CHKZY \n");
        sql.append("   FROM TC_USER U, TT_PART_USERPOSE_DEFINE PU, TT_PART_FIXCODE_DEFINE PF \n");
        sql.append("  WHERE PU.USER_ID = U.USER_ID(+) \n");
        sql.append("    AND PF.FIX_VALUE = PU.USER_TYPE \n");
        sql.append("    AND U.USER_ID = "+userId+" \n");
        sql.append("    AND PU.USER_TYPE = "+userType+" \n");
        sql.append("    AND PF.FIX_GOUPTYPE = " + Constant.FIXCODE_TYPE_01);
        Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
        return map;
    }
    

    /**
     * @param : @param orderID
     * @param : @param partCode
     * @param : @param partName
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-8
     * @Title : 条件查询订单类型信息
     */
    public PageResult<Map<String, Object>> queryPartUserOrder(String userId, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT U.NAME,PUOD.USER_ID,PUOD.ORDER_TYPE " +
                "FROM TC_USER U, TT_PART_USER_ORDERTYPE_DEFINE PUOD \n" +
                " WHERE PUOD.USER_ID = U.USER_ID(+)\n" +
                "AND U.USER_STATUS ='" + Constant.STATUS_ENABLE + "' ");
        sql.append(" AND PUOD.USER_ID = " + userId);
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-11
     * @Title : 获取人员类型
     */
    public List<Map<String, Object>> getPostList() throws Exception {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT PF.FIX_NAME, PF.FIX_VALUE\n");
        sql.append("  FROM TT_PART_FIXCODE_DEFINE PF\n");
        sql.append(" WHERE 1 = 1  AND PF.FIX_GOUPTYPE = " + Constant.FIXCODE_TYPE_01);

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @param whereSql
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 返回所有User信息
     */
    public PageResult<Map<String, Object>> getAllUsers(String name, String oemId, int pageSize, int curPage) {
        PageResult<Map<String, Object>> result = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT U.*\n");
        sql.append("  FROM TC_USER U\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append(" AND U.USER_STATUS = '" + Constant.STATUS_ENABLE + "' ");
        sql.append(" AND U.USER_TYPE = '" + Constant.SYS_USER_SGM + "' ");

        if (null != name && !"".equals(name)) {
            sql.append(" AND U.NAME like '%" + name + "%'\n");
        }

        if (null != oemId && !"".equals(oemId)) {
            sql.append(" AND U.COMPANY_ID = '" + oemId + "'\n");
        }

        sql.append(" ORDER BY U.ACNT, U.NAME ");
        result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return result;
    }

    /**
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @param whereSql
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 返回所有订单类型信息
     */
    public PageResult<Map<String, Object>> getAllUsersOrder(String orderType, int pageSize, int curPage) {
        PageResult<Map<String, Object>> result = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT C.*\n");
        sql.append("  FROM TC_CODE C\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append(" AND C.STATUS = '" + Constant.STATUS_ENABLE + "' ");
        sql.append(" AND C.TYPE = '" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE + "' ");

        if (null != orderType && !"".equals(orderType)) {
            sql.append(" AND C.CODE_DESC like '%" + orderType + "%'\n");
        }
//        
//        if(null!= oemId && !"".equals(oemId))
//        {
//        	sql.append(" AND U.COMPANY_ID = '" + oemId + "'\n");
//        }

//        sql.append(" ORDER BY C.ACNT, C.NAME ");
        result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return result;
    }

    /**
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @param whereSql
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 返回所有用户信息
     */
    public PageResult<Map<String, Object>> getAllPlanner(int pageSize, int curPage, String sbString) {
        PageResult<Map<String, Object>> result = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT U.USER_ID, U.NAME\n");
        sql.append("  FROM TC_USER U\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append(" AND U.USER_STATUS = '" + Constant.STATUS_ENABLE + "' ");
        sql.append(sbString);

        result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return result;
    }

    /**
     * @param : @param partId
     * @param : @param partCode
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title : 验证计划员与仓库关系记录是否存在
     */
    @SuppressWarnings("unchecked")
    public List getExistPO(String fixValue, String userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT U.NAME FROM TC_USER U, TT_PART_USERPOSE_DEFINE PU\n");
        sql.append(" WHERE 1= 1 AND PU.USER_ID = U.USER_ID(+)\n");
        sql.append(" AND PU.USER_TYPE = '" + Integer.parseInt(fixValue) + "'\n");
        sql.append(" AND PU.USER_ID = '" + Long.parseLong(userId) + "'");
        return dao.select(TcUserPO.class, sql.toString(), null);
    }

    /**
     * @param : @param partId
     * @param : @param partCode
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title : 验证计划员与订单类型记录是否存在
     */
    @SuppressWarnings("unchecked")
    public List getExistPO1(String userId, String codeId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ORDER_TYPE from tt_part_user_orderType_define \n");
        sql.append(" WHERE 1= 1 \n");
        sql.append(" AND ORDER_TYPE = '" + Integer.parseInt(codeId) + "'\n");
        sql.append(" AND USER_ID = '" + Long.parseLong(userId) + "'");
        return dao.select(TtPartUserOrdertypeDefinePO.class, sql.toString(), null);
    }

}
