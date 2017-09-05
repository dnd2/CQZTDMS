package com.infodms.dms.dao.parts.baseManager.partPlannerQueryManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.component.dict.CodeDict;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-4-10
 * @ClassName : partPlannerQueryDao
 */
public class partPlannerQueryDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partPlannerQueryDao.class);
    private static final partPlannerQueryDao dao = new partPlannerQueryDao();

    private partPlannerQueryDao() {
    }

    public static final partPlannerQueryDao getInstance() {
        return dao;
    }

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }
    
    private String loadDecodeSql2(List<TcCodePO> list, String code) {
        String decodeSql = "decode(" + code + "";
        for (TcCodePO tc : list) {
            decodeSql += ",'" + tc.getCodeId() + "','" + tc.getCodeDesc() + "'";
        }
        if (decodeSql == "docode(" + code + "") {
            return "'' " + code;
        }
        decodeSql += ",'')";
        return decodeSql;
    }
    
    /**
     * <p>Description: 查询配件采购属性信息列表sql</p>
     * @param paramMap
     * @return
     */
    private String queryPartPlannerSqlStr(Map<String, String> paramMap){
        
        
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT ROWNUM, \n");
        // decode 选项值
        List<TcCodePO> ownedBaseList = CodeDict.dictMap.get(Constant.PURCHASE_WAY.toString()); // 采购方式
        String ownedBaseDecode = this.loadDecodeSql2(ownedBaseList, "T1.PRODUCE_FAC"); // 所属基地
        if(!CommonUtils.isEmpty(paramMap.get("decodeSql"))){
            List<TcCodePO> flagList = CodeDict.dictMap.get(Constant.PART_BASE_FLAG.toString());  //配件中所有是否
            List<TcCodePO> partTypeList = CodeDict.dictMap.get(Constant.PART_PRODUCE_STATE.toString()); //配件主数据维护-配件种类-9263
            List<TcCodePO> spList = CodeDict.dictMap.get(Constant.PURCHASE_TYPE.toString()); // 上级采购单位
            String isPlanDecode = this.loadDecodeSql2(flagList, "T1.IS_PLAN"); // 是否计划
            String partTypeDecode = this.loadDecodeSql2(partTypeList, "T1.PART_TYPE"); // 配件种类
            String spDecode = this.loadDecodeSql2(spList, "T1.SUPERIOR_PURCHASING"); // 上级采购单位
            String ssDecode = this.loadDecodeSql2(partTypeList, "T1.PRODUCE_STATE"); // 配件种类
            sql.append("       "+ssDecode+" PRODUCE_STATE_DESC, \n");
            sql.append("       "+isPlanDecode+" IS_PLAN_DESC, \n");
            sql.append("       "+partTypeDecode+" PART_TYPE_DESC, \n");
            sql.append("       "+ownedBaseDecode+" OWNED_BASE_DESC, \n");
            sql.append("       "+spDecode+" SP_DESC, \n");
            sql.append("        TU1.NAME PLANER_NAME, \n");
            sql.append("        TU2.NAME BUYER_NAME, \n");
        }
        sql.append("        T1.PART_ID, \n");
        sql.append("        T1.PART_CODE, \n");
        sql.append("        T1.PART_OLDCODE, \n");
        sql.append("        T1.PART_CNAME, \n");
        sql.append("        T2.PRICE_ID,\n");
        sql.append("        T2.VENDER_ID,\n");
        sql.append("        T2.VENDER_NAME, \n");
        sql.append("        T1.PLANER_ID, \n");
        sql.append("        T1.BUYER_ID, \n");
        sql.append("        T1.IS_PLAN, \n");
        sql.append("        T1.PART_TYPE, \n");
        sql.append("        T1.PART_MATERIAL, \n");
        sql.append("        T1.OWNED_BASE, \n");
        sql.append("        T1.PRODUCE_FAC, \n");
        sql.append("        "+ownedBaseDecode+" PRODUCE_FAC_NAME, \n");
        sql.append("        T1.SUPERIOR_PURCHASING SP, \n");
        sql.append("        T1.PRODUCE_STATE, \n");
        sql.append("        T1.BUY_MIN_PKG, \n");
        sql.append("        T1.MIN_PURCHASE, \n");
        sql.append("        T1.DELIVERY_CYCLE, \n");
        sql.append("        T1.DELIVER_PERIOD, \n");
        sql.append("        T1.PLAN_CYCLE, \n");
        sql.append("        T1.NOTICE_NUM, \n");
        sql.append("        T1.WH_ID, \n");
        sql.append("        T3.WH_CODE, \n");
        sql.append("        T3.WH_NAME \n");
        sql.append("   FROM TT_PART_DEFINE T1, TT_PART_WAREHOUSE_DEFINE T3, \n");
        sql.append("        (SELECT T1.PART_ID, T1.PRICE_ID, T2.VENDER_ID, T2.VENDER_NAME \n");
        sql.append("           FROM TT_PART_BUY_PRICE T1, TT_PART_VENDER_DEFINE T2 \n");
        sql.append("          WHERE T1.VENDER_ID = T2.VENDER_ID) T2 \n");
        if(!CommonUtils.isEmpty(paramMap.get("decodeSql"))){
            sql.append("        ,TC_USER TU1, TC_USER TU2 \n");
        }
        sql.append("  WHERE T1.PART_ID = T2.PART_ID(+) \n");
        sql.append("    AND T1.WH_ID = T3.WH_ID(+) \n");
        if(!CommonUtils.isEmpty(paramMap.get("decodeSql"))){
            sql.append("     AND T1.PLANER_ID = TU1.USER_ID(+) \n");
            sql.append("     AND T1.BUYER_ID = TU2.USER_ID(+) \n");
        }
        if (!CommonUtils.isEmpty(paramMap.get("partCode"))) {
            sql.append(" and UPPER(T1.PART_CODE) LIKE '%" + paramMap.get("partCode").toUpperCase() + "%' ");
        }

        if (!CommonUtils.isEmpty(paramMap.get("partOldcode"))) {
            sql.append(" and UPPER(T1.PART_OLDCODE) LIKE '%" + paramMap.get("partOldcode").toUpperCase() + "%' ");
        }
        if (!CommonUtils.isEmpty(paramMap.get("partCname"))) {
            sql.append(" and UPPER(T1.PART_CNAME) LIKE '%" + paramMap.get("partCname").toUpperCase() + "%'");
        }

        if (!CommonUtils.isEmpty(paramMap.get("planerId"))) {
            sql.append(" AND T1.PLANER_ID = " + paramMap.get("planerId"));
        }

        if (!CommonUtils.isEmpty(paramMap.get("isPlan"))) {
            sql.append(" and T1.IS_PLAN = " + paramMap.get("isPlan"));
        }

        if (!CommonUtils.isEmpty(paramMap.get("whId"))) {
            sql.append(" and T1.WH_ID = " + paramMap.get("whId"));
        }

        if (!CommonUtils.isEmpty(paramMap.get("buyerId"))) {
            sql.append(" and T1.BUYER_ID = " + paramMap.get("buyerId"));
        }

        //mod by zhumingwei 2013-09-16
        if (!CommonUtils.isEmpty(paramMap.get("produceState"))) {
            sql.append(" and T1.PRODUCE_STATE = " + paramMap.get("produceState"));
        }
        //mod by zhumingwei 2013-09-16

        if (!CommonUtils.isEmpty(paramMap.get("ownedBase"))) {
            sql.append(" and T1.OWNED_BASE = " + paramMap.get("ownedBase"));
        }
        
        if (!CommonUtils.isEmpty(paramMap.get("produceFac"))) {
            sql.append(" and T1.PRODUCE_FAC = " + paramMap.get("produceFac"));
        }

        if (!CommonUtils.isEmpty(paramMap.get("venderId"))) {
            sql.append(" and T2.VENDER_ID = " + paramMap.get("venderId"));
        }

        sql.append(" and T1.status=1");
        sql.append(" ORDER BY T1.PART_OLDCODE, T1.PART_CNAME, T1.PART_CODE ");
        return sql.toString();
    }

    /**
     * @param : @param partCode
     * @param : @param partOldcode
     * @param : @param partCname
     * @param : @param name
     * @param : @param state
     * @param : @param isDirect
     * @param : @param isLack
     * @param : @param isPlan
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-10
     * @Title : 配件采购属性查询
     */
    @SuppressWarnings("unchecked")
    public PageResult<Map<String, Object>> queryPartPlanner(Map<String, String> paramMap, int pageSize, int curPage) {
//        sql.append("SELECT T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,PP.NAME AS P_NAME,PP.PLANER_ID AS PLANER_ID, BB.NAME AS B_NAME,BB.BUYER_ID AS BUYER_ID,T.IS_PLAN, T.IS_RECEIVE, "
//                        + "T.IS_DIRECT,T.IS_LACK,T.OEM_PLAN,T.STATE,T.IS_SPECIAL FROM TT_PART_DEFINE T, (SELECT P.NAME, TP.PLANER_ID, TP.PART_ID FROM TT_PART_DEFINE TP, TC_USER P WHERE TP.PLANER_ID = P.USER_ID) PP,"
//                        + "(SELECT B.NAME, TB.BUYER_ID, TB.PART_ID FROM TT_PART_DEFINE TB, TC_USER B WHERE TB.BUYER_ID = B.USER_ID) BB, TC_USER TC");
//        sql.append(" WHERE 1=1 AND T.PART_ID = PP.PART_ID(+) AND T.PART_ID = BB.PART_ID(+) AND T.PLANER_ID = TC.USER_ID(+) ");

        PageResult<Map<String, Object>> ps = pageQuery(this.queryPartPlannerSqlStr(paramMap), null,getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param partCode
     * @param : @param partOldcode
     * @param : @param partCname
     * @param : @param name
     * @param : @param state
     * @param : @param isDirect
     * @param : @param isLack
     * @param : @param isPlan
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 配件采购属性List
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> partPurchaseList(Map<String, String> paramMap) {
//        sql.append("SELECT T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,PP.NAME AS P_NAME,PP.PLANER_ID AS PLANER_ID, BB.NAME AS B_NAME,BB.BUYER_ID AS BUYER_ID,T.IS_PLAN, T.IS_RECEIVE, "
//                        + "T.IS_DIRECT,T.IS_LACK,T.OEM_PLAN,T.STATE FROM TT_PART_DEFINE T, (SELECT P.NAME, TP.PLANER_ID, TP.PART_ID FROM TT_PART_DEFINE TP, TC_USER P WHERE TP.PLANER_ID = P.USER_ID) PP,"
//                        + "(SELECT B.NAME, TB.BUYER_ID, TB.PART_ID FROM TT_PART_DEFINE TB, TC_USER B WHERE TB.BUYER_ID = B.USER_ID) BB, TC_USER TC");
//        sql.append(" WHERE 1=1 AND T.PART_ID = PP.PART_ID(+) AND T.PART_ID = BB.PART_ID(+) AND T.PLANER_ID = TC.USER_ID(+) ");
        List<Map<String, Object>> list = dao.pageQuery(this.queryPartPlannerSqlStr(paramMap), null, getFunName());
        return list;
    }

    /**
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-7
     * @Title : 获取所有用户信息（未启用）
     */
    public List<Map<String, Object>> getTcUser() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT U.USER_ID, U.NAME\n");
        sql.append("  FROM TC_USER U\n");

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title : 获取制定的人员信息
     */
    public List<Map<String, Object>> getUsers(String userPost, String userAct) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT U.USER_ID, U.NAME " +
                "FROM TC_USER U, TT_PART_USERPOSE_DEFINE PU, TT_PART_FIXCODE_DEFINE PF " +
                "WHERE PU.USER_ID = U.USER_ID(+) AND  PF.FIX_VALUE = PU.USER_TYPE  " +
                "AND U.USER_STATUS ='" + Constant.STATUS_ENABLE + "' " +
                "AND PU.STATE = '" + Constant.STATUS_ENABLE + "' AND PF.FIX_NAME ='" + userPost + "' ");

        if (!"".equals(userAct)) {
            sql.append(" AND U.ACNT = '" + userAct + "' ");
        }

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param venderId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-18
     * @Title : 条件查询制造商
     */
    public List<Map<String, Object>> getMakers(String venderId, String partId) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT MD.MAKER_ID, MD.MAKER_CODE, MD.MAKER_NAME ");
        sql.append(" FROM TT_PART_MAKER_DEFINE MD, TT_PART_MAKER_RELATION MR ");
        sql.append(" WHERE MD.STATE = '" + Constant.STATUS_ENABLE + "' ");
        sql.append(" AND MD.VENDER_ID = '" + venderId + "'");
        sql.append(" AND MR.PART_ID = '" + partId + "'");
        sql.append(" AND MR.MAKER_ID = MD.MAKER_ID ");

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }


    /**
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-8
     * @Title : 获取配件供应商信息
     */
    public PageResult<Map<String, Object>> queryVender(String partId,
                                                       int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TP.PRICE_ID,TP.IS_DEFAULT, TP.MIN_PACKAGE, V.VENDER_ID, V.VENDER_CODE, V.VENDER_NAME, M.MAKER_NAME, M.MAKER_CODE, M.MAKER_ID ");
        sql.append(" FROM TT_PART_DEFINE T, TT_PART_BUY_PRICE TP, TT_PART_VENDER_DEFINE V,  ");
        sql.append(" (SELECT MD.*, MR.PART_ID FROM TT_PART_MAKER_DEFINE MD, TT_PART_MAKER_RELATION MR ");
        sql.append(" WHERE MR.MAKER_ID = MD.MAKER_ID  AND MR.IS_DEFAULT = '" + Constant.IF_TYPE_YES + "' AND MR.PART_ID = '" + partId + "') M ");
        sql.append(" WHERE 1=1 AND TP.VENDER_ID = V.VENDER_ID AND T.PART_ID = TP.PART_ID ");
        sql.append(" AND V.VENDER_ID = M.VENDER_ID(+) ");
        sql.append(" AND T.PART_ID = '" + partId + "'");
        sql.append(" ORDER BY V.VENDER_CODE ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-7
     * @Title : 获取配件默认用户信息（未启用）
     */
    public List<Map<String, Object>> getPartDefaultVender(String partId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT P.IS_DEFAULT, P.MIN_PACKAGE, V.VENDER_ID AS EXIST_VENDER_ID");
        sql.append("  FROM TT_PART_BUY_PRICE P, TT_PART_VENDER_DEFINE V\n");
        sql.append(" WHERE 1=1 AND P.VENDER_ID = V.VENDER_ID AND P.PART_ID = " + partId);

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param delCode
     * @param : @return
     * @return :
     * LastDate    : 2013-5-3
     * @Title : 验证配件编码是否存在 并返回配件ID、Name
     * @Description:
     */
    public List<Map<String, Object>> checkOldCode(String oldCode) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT TD.PART_ID,TD.PART_CODE, TD.PART_OLDCODE, TD.PART_CNAME FROM TT_PART_DEFINE TD " +
                " WHERE UPPER(TD.PART_OLDCODE) = '" + oldCode + "' ";
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 验证用户账号
     */
    public List<Map<String, Object>> checkUserAccount(String sqlStr) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT U.* FROM TC_USER U " +
                " WHERE 1 = 1 ";
        sql += sqlStr;
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-27
     * @Title : 验证供应商
     */
    public List<Map<String, Object>> checkVenderAccount(String sqlStr) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT V.* FROM TT_PART_VENDER_DEFINE V " +
                " WHERE 1 = 1 AND V.STATE = '" + Constant.STATUS_ENABLE + "' ";
        sql += sqlStr;
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-27
     * @Title : 验证制造商
     */
    public List<Map<String, Object>> checkMakerAccount(String sqlStr) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT M.* FROM TT_PART_MAKER_DEFINE M " +
                " WHERE 1 = 1 AND M.STATE = '" + Constant.STATUS_ENABLE + "' ";
        sql += sqlStr;
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-27
     * @Title : 验证配件和供应商关系合法性
     */
    public List<Map<String, Object>> checkPartVender(String sqlStr) {
        List<Map<String, Object>> list = null;

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT V.* FROM TT_PART_DEFINE T, TT_PART_BUY_PRICE TP, TT_PART_VENDER_DEFINE V");
        sql.append(" WHERE 1 = 1");
        sql.append(" AND T.PART_ID = TP.PART_ID ");
        sql.append(" AND TP.VENDER_ID = V.VENDER_ID ");
        sql.append(" AND V.STATE = '" + Constant.STATUS_ENABLE + "' ");
        sql.append(sqlStr);

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param partOldCode
     * @param : @param venderCode
     * @param : @param makerCode
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-27
     * @Title : 验证配件、供应商和制造商共同关系合法性
     */
    public List<Map<String, Object>> checkPartVenderMaker(String partOldCode, String venderCode, String makerCode) {
        List<Map<String, Object>> list = null;

        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT TP.PRICE_ID,TP.IS_DEFAULT, TP.MIN_PACKAGE, V.VENDER_ID, V.VENDER_CODE, V.VENDER_NAME, M.MAKER_NAME, M.MAKER_CODE, M.MAKER_ID ");
        sql.append(" FROM TT_PART_DEFINE T, TT_PART_BUY_PRICE TP, TT_PART_VENDER_DEFINE V,  ");
        sql.append(" (SELECT MD.*, MR.PART_ID FROM TT_PART_MAKER_DEFINE MD, TT_PART_MAKER_RELATION MR, TT_PART_DEFINE PD ");
        sql.append(" WHERE MR.MAKER_ID = MD.MAKER_ID  AND MD.MAKER_CODE = '" + makerCode + "' AND MD.STATE = '" + Constant.STATUS_ENABLE + "' AND MR.PART_ID = PD.PART_ID AND PD.PART_OLDCODE = '" + partOldCode + "') M ");
        sql.append(" WHERE 1=1 AND TP.VENDER_ID = V.VENDER_ID AND T.PART_ID = TP.PART_ID ");
        sql.append(" AND V.VENDER_ID = M.VENDER_ID ");
        sql.append(" AND V.VENDER_CODE = '" + venderCode + "' ");
        sql.append(" AND T.PART_OLDCODE = '" + partOldCode + "'");

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * <p>Description: 获取仓库</p>
     * @param logonUser
     * @return
     */
    public List<Map<String, Object>> getWarnHouseList(AclUserBean logonUser) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT WH_ID, WH_CODE, WH_NAME \n");
        sql.append("  FROM TT_PART_WAREHOUSE_DEFINE T \n");
        sql.append(" WHERE T.ORG_ID = "+logonUser.getOrgId()+" \n");
        sql.append("  \n");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
}
