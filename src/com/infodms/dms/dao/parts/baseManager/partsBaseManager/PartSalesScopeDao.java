package com.infodms.dms.dao.parts.baseManager.partsBaseManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class PartSalesScopeDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PartSalesScopeDao.class);
    private static final PartSalesScopeDao dao = new PartSalesScopeDao();

    private PartSalesScopeDao() {

    }

    public static final PartSalesScopeDao getInstance() {
        return dao;
    }

    private static final int user_type_03 = 3;//配件销售员 fixcode
    private static final int user_type_05 = 5;//整车销售员 fixcode
    private static final int user_type_06 = 6;//配件索赔员 fixcode

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> queryPartSales(String acnt,
                                                          String name, String companyId, String user_status, String userType, Integer curPage,
                                                          Integer pageSize) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();

            sql.append("SELECT U.USER_ID,\n");
            sql.append("       U.ACNT,\n");
            sql.append("       U.NAME,\n");
            sql.append("       U.USER_STATUS,\n");
            sql.append("       PU.USER_TYPE,\n");
            sql.append("       (SELECT za_concat(REGION_NAME)\n");
            sql.append("          FROM (SELECT DISTINCT SD.USER_ID, TR.REGION_NAME\n");
            sql.append("                  FROM TM_DEALER                 TD,\n");
            sql.append("                       TM_REGION                 TR,\n");
            sql.append("                       TT_PART_SALESSCOPE_DEFINE SD\n");
            sql.append("                 WHERE TD.DEALER_ID = SD.DEALER_ID\n");
            sql.append("                   AND TD.PROVINCE_ID = TR.REGION_CODE) \n");
            sql.append("         WHERE U.USER_ID = USER_ID) lo\n");
            sql.append("  FROM TC_USER U,\n");
            sql.append("       (SELECT PU.*\n");
            sql.append("          FROM TT_PART_USERPOSE_DEFINE PU\n");
            sql.append("         WHERE PU.STATE = 10011001\n");
            sql.append("           AND PU.STATUS = 1\n");
            sql.append(" AND ( pu.user_type='" + user_type_03 + "' OR pu.user_type='" + user_type_05 + "' OR pu.user_type='" + user_type_06 + "' ) ) PU \n");
            sql.append(" WHERE COMPANY_ID = ").append(companyId).append("\n");
            sql.append(" AND U.USER_ID = PU.USER_ID ");
            if (Utility.testString(acnt)) {
                sql.append("AND U.ACNT LIKE '%").append(acnt).append("%'\n");
            }
            if (Utility.testString(name)) {
                sql.append("AND U.NAME LIKE '%").append(name).append("%'\n");
            }
            if (Utility.testString(user_status)) {
                sql.append("AND U.USER_STATUS=").append(user_status).append("\n");
            }
            if (Utility.testString(userType)) {
                sql.append("AND pu.user_type=").append(userType).append("\n");
            }
            sql.append(" ORDER BY PU.USER_TYPE, U.ACNT ");
            PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
            return ps;
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<String, Object> getUserMap(String userId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT USER_ID, ACNT, NAME\n");
            sql.append("FROM TC_USER\n");
            sql.append("WHERE USER_ID = ").append(userId).append("\n");
            Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
            return map;
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryScopeByUserList(String userId, String userType, String regionCode,
                                                                Integer curPage, Integer pageSize) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT C.REGION_NAME,A.PROVINCE_ID,A.DEALER_ID,A.DEALER_NAME, B.DEFINE_ID, B.USER_TYPE\n");
            sql.append("FROM TM_DEALER A,TT_PART_SALESSCOPE_DEFINE B,TM_REGION C \n");
            sql.append("WHERE A.DEALER_ID = B.DEALER_ID and A.PROVINCE_ID=C.REGION_CODE AND B.STATUS=1 and C.STATUS=" + Constant.STATUS_ENABLE + " \n");
            sql.append("AND B.USER_ID = ").append(userId).append("\n");
            sql.append("AND B.USER_TYPE = '").append(userType).append("'\n");
            if (!regionCode.equals("")) {
                sql.append(" and A.PROVINCE_ID='" + regionCode + "'  \n");
            }
            PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
            return ps;
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryScopeByUserList1(JSONObject paraObject, Integer curPage, Integer pageSize) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();

            sql.append("SELECT DISTINCT C.REGION_NAME, B.PROVINCE_ID, B.USER_TYPE\n");
            sql.append("  FROM TT_PART_USERPROVINCE_DEFINE B, TM_REGION C\n");
            sql.append(" WHERE B.PROVINCE_ID = C.REGION_CODE\n");
            sql.append("   AND B.STATUS = 1\n");
            sql.append("   AND C.STATUS = 10011001");
            sql.append("   AND B.USER_ID = ").append(paraObject.get("userId")).append("\n");
            sql.append("   AND B.USER_TYPE = '").append(paraObject.get("userType")).append("'\n");
            PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
            return ps;
        } catch (Exception e) {
            throw e;
        }
    }


    public Map checkDealerId(String userId, String dealerId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT A.DEALER_SHORTNAME\n");
            sql.append("FROM TM_DEALER A,TT_PART_SALESSCOPE_DEFINE B\n");
            sql.append("WHERE A.DEALER_ID = B.DEALER_ID AND B.USER_ID = ").append(userId).append("\n").append("AND B.DEALER_ID=").append(dealerId);
            sql.append(" AND B.STATUS=1\n");
            Map map = pageQueryMap(sql.toString(), null, getFunName());

            return map;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> getDealerId(String codes) throws Exception {
        try {
            String[] ids = null;
            StringBuffer sql = new StringBuffer();
            sql.append("select DEALER_ID from TM_DEALER where DEALER_TYPE=" + Constant.DEALER_TYPE_DWR + " AND dealer_level=" + Constant.DEALER_LEVEL_01 + " and STATUS=" + Constant.STATUS_ENABLE + " and PROVINCE_ID in(" + codes + ") ");
            List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> getSaleScope(String regionCode, String userId, String userType) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("select A.DEALER_ID,A.DEALER_NAME \n");
            sql.append("from TM_DEALER A,TT_PART_SALESSCOPE_DEFINE B \n");
            sql.append("where A.DEALER_ID = B.DEALER_ID and A.STATUS=" + Constant.STATUS_ENABLE + " and B.STATUS=1 and B.USER_ID = " + Long.parseLong(userId) + " and B.USER_TYPE =" + Long.parseLong(userType) + " and A.PROVINCE_ID=" + regionCode + " \n");
            List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> checkRegion(String userId, String codes) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();

            sql.append("SELECT DISTINCT C.REGION_NAME, B.PROVINCE_ID, B.USER_TYPE\n");
            sql.append("  FROM TT_PART_USERPROVINCE_DEFINE B, TM_REGION C\n");
            sql.append(" WHERE B.PROVINCE_ID = C.REGION_CODE");
            sql.append(" AND B.STATUS=1 and C.STATUS=" + Constant.STATUS_ENABLE + " \n");
            sql.append(" AND B.USER_ID = ").append(userId).append(" and B.PROVINCE_ID in(" + codes + ") ").append(" \n");
            List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
            return list;
        } catch (Exception e) {
            throw e;
        }
    }


}
