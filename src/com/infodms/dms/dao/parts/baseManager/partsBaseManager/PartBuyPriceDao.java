package com.infodms.dms.dao.parts.baseManager.partsBaseManager;

import com.infodms.dms.actions.parts.baseManager.PartBaseQuery;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.parts.baseManager.PartBaseQueryDao;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartVenderDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author : chenjunjiang CreateDate : 2013-4-5
 * @ClassName : PartBuyPriceDao
 * @Description : 配件采购价格dao
 */
public class PartBuyPriceDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PartBuyPriceDao.class);
    private static final PartBuyPriceDao dao = new PartBuyPriceDao();


    private PartBuyPriceDao() {

    }

    public static final PartBuyPriceDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> queryPartInfoList(
            TtPartDefinePO bean, Integer curPage, Integer pageSize)
            throws Exception {

        PageResult<Map<String, Object>> ps;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "SELECT T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,T.REMARK, " +
                            "nvl((SELECT BP.BUY_PRICE FROM TT_PART_BUY_PRICE BP WHERE BP.PART_ID = T.PART_ID AND BP.IS_DEFAULT = '" + Constant.IF_TYPE_YES + "'),0) AS DEF_BUY_PRICE " +
                            "FROM TT_PART_DEFINE T WHERE T.STATE=")
                    .append(Constant.STATUS_ENABLE);
            if (!"".equals(bean.getPartOldcode())) {
                sql.append(" AND UPPER(T.PART_OLDCODE) LIKE '%")
                        .append(bean.getPartOldcode().toUpperCase()).append("%'");
            }
            if (!"".equals(bean.getPartCname())) {
                sql.append(" AND UPPER(T.PART_CNAME) LIKE '%")
                        .append(bean.getPartCname().toUpperCase()).append("%'");
            }
            if (null != bean.getPartIsChanghe()) {
                sql.append(" AND t.PART_IS_CHANGHE =").append(bean.getPartIsChanghe());
            }
            sql.append(" ORDER BY T.PART_OLDCODE, T.PART_CNAME ");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }

        return ps;
    }

    /**
     * @param dealerid
     * @param vin
     * @param claim_no
     * @param curPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageResult<Map<String, Object>> queryPartFirstInfoList(String dealerid, String claim_no, String vin, Integer curPage, Integer pageSize)
            throws Exception {

        PageResult<Map<String, Object>> ps;
        StringBuffer sb = new StringBuffer();

        try {
            sb.append("SELECT t.VIN ,t.claim_no from TT_AS_WR_APPLICATION t where nvl(t.dealer_id,t.second_dealer_id)=" + dealerid);
            if (!"".equals(claim_no)) {
                sb.append(" and t.claim_no like '%" + claim_no + "%'");
            }
            if (!"".equals(vin)) {
                sb.append(" and t.vin like '%" + vin + "%'");
            }
            ps = pageQuery(sb.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }

        return ps;
    }

    public PageResult<Map<String, Object>> queryPartVenderList(
            Map<String, String> paramMap, Integer curPage, Integer pageSize)
            throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(
                    "SELECT T.VENDER_ID,T.VENDER_CODE,T.VENDER_NAME FROM TT_PART_VENDER_DEFINE T WHERE T.STATE=")
                    .append(Constant.STATUS_ENABLE)
                    .append((" AND T.STATUS=1\n"));
            if (!"".equals(paramMap.get("venderCode"))) {
                sql.append("AND T.VENDER_CODE LIKE '%")
                        .append(paramMap.get("venderCode")).append("%'\n");
            }
            if (!"".equals(paramMap.get("venderName"))) {
                sql.append("AND T.VENDER_NAME LIKE '%")
                        .append(paramMap.get("venderName")).append("%'\n");
            }
            if (!"".equals(paramMap.get("notExistsPartId")) && Pattern.matches("^-?\\d+$", paramMap.get("notExistsPartId"))){
                sql.append("AND NOT EXISTS (SELECT 1 FROM TT_PART_BUY_PRICE BP WHERE BP.VENDER_ID = T.VENDER_ID AND BP.PART_ID = '"+paramMap.get("notExistsPartId")+"')");
            }
            sql.append("ORDER BY T.VENDER_CODE ");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    /**
     * <p>Description: 查询配件采购价列表</p>
     * @param partCode
     * @param partOldCode
     * @param partName
     * @param venderCode
     * @param venderName
     * @param buyerName
     * @param state
     * @param isGuard
     * @param base
     * @param curPage
     * @param pageSize
     * @param poseBusType
     * @return
     * @throws Exception
     */
    public PageResult<Map<String, Object>> queryBuyPriceList(String partCode,
                                                             String partOldCode, String partName, String venderCode, String venderName,
                                                             String buyerName, int state, int isGuard, int base, Integer curPage,
                                                             Integer pageSize, int poseBusType) throws Exception {

        PageResult<Map<String, Object>> ps;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "SELECT T2.PART_ID,\n" +
                            "       T2.PART_CODE,\n" +
                            "       T2.PART_OLDCODE,\n" +
                            "       T2.PART_CNAME,\n" +
                            "       T3.VENDER_ID,\n" +
                            "       T3.VENDER_CODE,\n" +
                            "       T3.VENDER_NAME,\n" +
                            "       T1.PRICE_ID,\n" +
                            "       TO_CHAR(NVL(SP.SALE_PRICE3,'0.00'),'FM999,999,990.00') AS PLAN_PRICE,\n" +
                            "       TO_CHAR(T1.BUY_PRICE,'FM999,999,990.00') AS BUY_PRICE,\n" +
                            "       T1.IS_GUARD,\n" +
                            "       T1.IS_DEFAULT,\n" +
                            "       T1.MIN_PACKAGE,\n" +
                            "       T5.NAME BUYER_NAME,\n" +
                            "       T1.CREATE_DATE,\n" +
                            "       T1.UPDATE_DATE,\n" +
                            "       T2.PART_IS_CHANGHE,\n" + // 
                            "       T2.PRODUCE_FAC,\n" + // 
                            "       T1.STATE\n" +
                            "  FROM TT_PART_BUY_PRICE        T1,\n" +
                            "       TT_PART_DEFINE T2,\n" +
                            "       TT_PART_VENDER_DEFINE T3,\n" +
                            "       TT_PART_SALES_PRICE SP,\n" +
                            "       TC_USER T5\n" +
                            " WHERE T1.PART_ID=T2.PART_ID \n" +
                            "   AND T1.PART_ID=SP.PART_ID(+)\n" +
                            "   AND T1.VENDER_ID=T3.VENDER_ID(+)\n" +
                            "   AND T2.BUYER_ID=T5.USER_ID(+)");

            // 艾春 10.08 添加职位控制
            if (poseBusType == Constant.POSE_BUS_TYPE_WRD) {
                sql.append(" and T2.part_is_changhe =" + Constant.PART_IS_CHANGHE_02 + "\n");
            }
            if (!"".equals(partCode)) {
                sql.append(" and upper(t2.part_code) like '%").append(partCode)
                        .append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" and upper(t2.part_oldcode) like '%").append(partOldCode)
                        .append("%'");
            }
            if (!"".equals(partName)) {
                sql.append(" and t2.part_cname like '%").append(partName)
                        .append("%'");
            }
            if (!"".equals(venderCode)) {
                sql.append(" and T3.VENDER_CODE like '%").append(venderCode)
                        .append("%'");
            }
            if (!"".equals(venderName)) {
                sql.append(" and t3.vender_name like '%").append(venderName)
                        .append("%'");
            }
            if (!"".equals(buyerName)) {
                sql.append(" and t5.name like '%").append(buyerName)
                        .append("%'");
            }
            if (state != 0) {
                sql.append(" and t1.state=").append(state);
            }
            if (isGuard != 0) {
                sql.append(" and t1.is_guard=").append(isGuard);
            }
            // 结算基地 艾春 9.11 添加
//            if (isChanghe != 0) {
//                sql.append(" and t2.PART_IS_CHANGHE=").append(isChanghe);
//            }
            // 所属基地 张磊 17.7.11 添加
            if (base != 0) {
                sql.append(" and t2.OWNED_BASE = ").append(base);
            }

            sql.append(" ORDER BY T2.PART_OLDCODE ");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }

        return ps;
    }

    public void selPartBuyPrice(String buyPriceId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("update TT_PART_BUY_PRICE t set t.STATE=")
                    .append(Constant.STATUS_ENABLE)
                    .append(" where t.PRICE_ID=").append(buyPriceId);
            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * <p>Description: 组装decode函数的sql</p>
     * @param list
     * @param code
     * @return
     */
    private String loadDecodeSql(List<Map<String, Object>> list, String code) {
        String decodeSql = "decode(" + code + "";
        for (Map<String, Object> map : list) {
            decodeSql += ",'" + map.get("CODE_ID").toString() + "','" +
                    map.get("CODE_DESC").toString() + "'";
        }
        if (decodeSql == "docode(" + code + "") {
            return "'' " + code;
        }
        decodeSql += ",'')";
        return decodeSql;
    }

    /**
     * 查询满足条件的采购价格信息,主要用于数据下载
     *
     * @param : @param request
     * @param : @return
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-7
     * @Title :
     * @Description: TODO
     */
    public List<Map<String, Object>> queryPartBuyPrice(RequestWrapper request)
            throws Exception {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        List<Map<String, Object>> list;
        try {

            String partCode = CommonUtils.checkNull(request
                    .getParamValue("PART_CODE"));// 件号
            partCode = partCode.toUpperCase();
            String partOldCode = CommonUtils.checkNull(request
                    .getParamValue("PART_OLDCODE"));// 配件编码
            partOldCode = partOldCode.toUpperCase();
            String partName = CommonUtils.checkNull(request
                    .getParamValue("PART_CNAME"));// 配件名称
            String venderName = CommonUtils.checkNull(request
                    .getParamValue("VENDER_NAME"));// 供应商名称
            String venderCode = CommonUtils.checkNull(request
                    .getParamValue("VENDER_CODE"));// 供应商编码
            venderCode = venderCode.toUpperCase();
            String buyerName = CommonUtils.checkNull(request
                    .getParamValue("BUYER_NAME"));// 采购员
            String str_state = CommonUtils.checkNull(request
                    .getParamValue("STATE"));// 是否有效
            String str_isGuard = CommonUtils.checkNull(request
                    .getParamValue("IS_GUARD"));// 是否暂估
            String str_isChange = CommonUtils.checkNull(request.getParamValue("PART_IS_CHANGHE"));// 结算基地
            String str_ownedBAse = CommonUtils.checkNull(request.getParamValue("OWNED_BASE"));// 所属基地
            
            // 所属基地tc_code
            PartBaseQueryDao partBaseQueryDao = PartBaseQueryDao.getInstance();
            List<Map<String, Object>> partProcurementSiteList = partBaseQueryDao.getTcCode(Constant.PURCHASE_WAY); //采购方式(配件主数据维护-所属基地)-9281
            String ownedBaseDecode = this.loadDecodeSql(partProcurementSiteList, "t1.OWNED_BASE"); // 所属基地

            int state = 0;
            int isGuard = 0;
            int isChanghe = 0;
            int ownedBase = 0;

            if (!"".equals(str_state)) {
                state = CommonUtils.parseInteger(str_state);
            }
            if (!"".equals(str_isGuard)) {
                isGuard = CommonUtils.parseInteger(str_isGuard);
            }
            if (!"".equals(str_isChange)) {
                isChanghe = CommonUtils.parseInteger(str_isChange);
            }
            if (!"".equals(str_ownedBAse)) {
                ownedBase = CommonUtils.parseInteger(str_ownedBAse);
            }
            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "select t1.part_code,t1.part_oldcode,t1.part_cname,t2.vender_code,t2.vender_name,TO_CHAR(NVL(SP.SALE_PRICE3,'0.00'),'FM999,999,990.00') AS PLAN_PRICE,TO_CHAR(t3.buy_price,'FM999,999,990.00') AS buy_price,t3.MIN_PACKAGE, decode(t3.is_guard,")
                    .append(Constant.IS_GUARD_YES).append(",'是',")
                    .append(Constant.IS_GUARD_NO).append(",'否') is_guard, ")
                    .append(" decode(t3.IS_DEFAULT, " + Constant.PART_BASE_FLAG_YES + ",'是'," + Constant.PART_BASE_FLAG_NO + ",'否') IS_DEFAULT, ")
                    .append(" decode(T1.PART_IS_CHANGHE, " + Constant.PART_IS_CHANGHE_01 + ",'昌河'," + Constant.PART_IS_CHANGHE_02 + ",'东安') PART_IS_CHANGHE, ")
                    .append(" t4.name BUYER_NAME, decode(t3.STATE, " + Constant.STATUS_ENABLE + ",'有效'," + Constant.STATUS_DISABLE + ",'无效') STATE, ")
                    .append(ownedBaseDecode + " OWNED_BASE");
            
            
            sql.append("  from tt_part_define t1, tt_part_vender_define t2, tt_part_buy_price t3, tc_user t4, TT_PART_SALES_PRICE SP ");
            sql.append(" where t1.part_id = t3.part_id and t2.vender_id = t3.vender_id and t1.buyer_id = t4.user_id(+) and t3.STATUS=1");
            sql.append(" AND T3.PART_ID=SP.PART_ID(+) ");

            if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WRD) {
                sql.append(" and T1.part_is_changhe =" + Constant.PART_IS_CHANGHE_02 + "\n");
            }

            if (!"".equals(partCode)) {
                sql.append(" and UPPER(t1.part_code) like '%").append(partCode.trim().toUpperCase())
                        .append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" and UPPER(t1.part_oldcode) like '%").append(partOldCode.trim().toUpperCase())
                        .append("%'");
            }
            if (!"".equals(partName)) {
                sql.append(" and UPPER(t1.part_cname) like '%").append(partName.trim().toUpperCase())
                        .append("%'");
            }
            if (!"".equals(venderName)) {
                sql.append(" and t2.vender_name like '%").append(venderName)
                        .append("%'");
            }
            if (!"".equals(venderCode)) {
                sql.append(" and T2.VENDER_CODE LIKE '%").append(venderCode)
                        .append("%'");
            }
            if (!"".equals(buyerName)) {
                sql.append(" and t4.name like '%").append(buyerName)
                        .append("%'");
            }
            if (state != 0) {
                sql.append(" and t3.state=").append(state);
            }
            if (isGuard != 0) {
                sql.append(" and t3.is_guard=").append(isGuard);
            }

            if (isChanghe != 0) {
                sql.append(" and t1.PART_IS_CHANGHE=").append(isChanghe);
            }
            if (ownedBase != 0) {
                sql.append(" and t1.OWNED_BASE =").append(ownedBase);
            }

            sql.append(" ORDER BY T1.PART_OLDCODE ");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;

    }

    /**
     * @param : @param subCell
     * @param : @return
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-8
     * @Title :
     * @Description: 判断当前导入的配件编码在配件主数据表中是否已经存在, 存在就允许保存, 否则提示错误信息
     */
    public Map<String, Object> validatePartOldcode(String subCell)
            throws Exception {
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "select PART_ID from TT_PART_DEFINE t where t.PART_OLDCODE=trim('")
                    .append(subCell).append("')");
            Map<String, Object> map = pageQueryMap(sql.toString(), null,
                    getFunName());

            return map;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param : @param subCell
     * @param : @return
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-8
     * @Title :
     * @Description: 判断当前导入的供应商名称在供应商表中是否已经存在, 存在就允许保存, 否则提示错误信息
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> validatePartVenderName(String subCell)
            throws Exception {
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "select VENDER_ID from TT_PART_VENDER_DEFINE t where VENDER_NAME='")
                    .append(subCell).append("'");
            Map<String, Object> map = pageQueryMap(sql.toString(), null,
                    getFunName());

            return map;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param : @param subCell
     * @param : @return
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-8
     * @Title :
     * @Description: 判断当前导入的制造商名称在供应商表中是否已经存在, 存在就允许保存, 否则提示错误信息
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> validatePartMakerName(String subCell)
            throws Exception {
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "select MAKER_ID from TT_PART_MAKER_DEFINE t where MAKER_NAME='")
                    .append(subCell).append("'");
            Map<String, Object> map = pageQueryMap(sql.toString(), null,
                    getFunName());

            return map;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param : @param strPartId 配件id
     * @param : @param strVenderId 供应商id
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-8
     * @Title :
     * @Description: 检测配件id和供应商id是否同时重复
     */
    public Map<String, Object> checkPartIdAndVenderId(long partId, long venderId)
            throws Exception {
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "select t.PRICE_ID,t.PART_ID,t.VENDER_ID, nvl(t.PLAN_PRICE,'0') AS PLAN_PRICE, t.STATE, t.IS_GUARD from TT_PART_BUY_PRICE t where t.PART_ID=")
                    .append(partId).append(" and t.VENDER_ID=")
                    .append(venderId);
            Map<String, Object> map = pageQueryMap(sql.toString(), null,
                    getFunName());
            return map;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param : @param strPartId 配件id
     * @param : @param strVenderId 制造商id
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-8
     * @Title :
     * @Description: 检测配件id和制造商id是否同时重复
     */
    public Map<String, Object> checkPartIdAndMakerId(long partId, long mkerId)
            throws Exception {
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "SELECT T.RELAION_ID,T.PART_ID,T.MAKER_ID FROM TT_PART_MAKER_RELATION T WHERE T.PART_ID=")
                    .append(partId).append(" and T.MAKER_ID=")
                    .append(mkerId);
            Map<String, Object> map = pageQueryMap(sql.toString(), null,
                    getFunName());
            return map;
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateBuyPrice(Long partId, Long venderId, Double buyPrice, int isDefalut, AclUserBean logonUser) throws Exception {
        try {
            String sql = "update TT_PART_BUY_PRICE t set t.BUY_PRICE=?,t.UPDATE_DATE=?, t.IS_DEFAULT=?, t.UPDATE_BY=? where t.PART_ID=? and t.VENDER_ID=?";
            List params = new ArrayList();
            params.add(buyPrice);
            params.add(new Date());
            params.add(isDefalut);
            params.add(logonUser.getUserId());
            params.add(partId);
            params.add(venderId);
            update(sql, params);
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryBuyPriceHisList(
            String partCode, String partOldCode, String partName, String venderCode,
            String venderName, String buyerName, int state, int isGuard,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(
                    "SELECT T1.PART_ID,\n" +
                            "       T1.PART_CODE,\n" +
                            "       T1.PART_OLDCODE,\n" +
                            "       T1.PART_CNAME,\n" +
                            "       T2.VENDER_ID,\n" +
                            "       T2.VENDER_CODE,\n" +
                            "       T2.VENDER_NAME,\n" +
                            "       T3.PRICE_ID,\n" +
                            "       T3.PLAN_PRICE,\n" +
                            "       TO_CHAR(T3.BUY_PRICE,'FM999,999,990.00')AS BUY_PRICE,\n" +
                            "       TO_CHAR(NVL(T3.OLD_BUY_PRICE,T3.BUY_PRICE),'FM999,999,990.00') AS OLD_BUY_PRICE,\n" +
                            "       T3.IS_GUARD,\n" +
                            "       T4.NAME ACNT,\n" +
                            "       T3.CREATE_DATE,\n" +
                            "       T3.STATE\n" +
                            "  FROM TT_PART_DEFINE        T1,\n" +
                            "       TT_PART_VENDER_DEFINE T2,\n" +
                            "       TT_PART_BUY_PRICE_HISTORY  T3,\n" +
                            "       TC_USER               T4\n" +
                            " WHERE T1.PART_ID = T3.PART_ID\n" +
                            "   AND T2.VENDER_ID = T3.VENDER_ID\n" +
                            "   AND T3.CREATE_BY = T4.USER_ID(+)\n" +
                            "   AND T3.STATUS = 1");

            if (!"".equals(partCode)) {
                sql.append(" and t1.part_code like '%").append(partCode)
                        .append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" and t1.part_oldcode like '%").append(partOldCode)
                        .append("%'");
            }
            if (!"".equals(partName)) {
                sql.append(" and t1.part_cname like '%").append(partName)
                        .append("%'");
            }
            if (!"".equals(venderCode)) {
                sql.append(" and T2.VENDER_CODE like '%").append(venderCode)
                        .append("%'");
            }
            if (!"".equals(venderName)) {
                sql.append(" and t2.vender_name like '%").append(venderName)
                        .append("%'");
            }
            if (!"".equals(buyerName)) {
                sql.append(" and t4.name like '%").append(buyerName)
                        .append("%'");
            }
            if (state != 0) {
                sql.append(" and t3.state=").append(state);
            }
            if (isGuard != 0) {
                sql.append(" and t3.is_guard=").append(isGuard);
            }

            sql.append(" ORDER BY T3.CREATE_DATE DESC");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }

        return ps;
    }

    public PageResult<Map<String, Object>> queryPartMakerList(String partCode,
                                                              String partOldCode, String partName, String isChange,
                                                              String buyerName, int state, int isGuard, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(" SELECT MR.*, "
                    + " MD.MAKER_CODE, MD.MAKER_NAME, "
                    + " PD.PART_CODE, PD.PART_OLDCODE, PD.PART_CNAME "
                    + " FROM TT_PART_MAKER_RELATION MR, TT_PART_MAKER_DEFINE MD, "
                    + " TT_PART_DEFINE PD "
                    + " WHERE MR.PART_ID = PD.PART_ID "
                    + " AND MR.MAKER_ID = MD.MAKER_ID ");

            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(PD.PART_CODE) LIKE '%").append(partCode.toUpperCase())
                        .append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(PD.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase())
                        .append("%'");
            }
            if (!"".equals(partName)) {
                sql.append(" AND UPPER(PD.PART_CNAME) LIKE '%").append(partName.toUpperCase())
                        .append("%'");
            }
            if (!"".equals(isChange)) {
                sql.append(" AND PD.PART_IS_CHANGHE = '").append(isChange)
                        .append("'");
            }

			/*if (!"".equals(venderCode)) {
				sql.append(" AND UPPER(VD.VENDER_CODE) LIKE '%").append(venderCode.toUpperCase())
				.append("%'");
			}
			if (!"".equals(venderName)) {
				sql.append(" AND UPPER(VD.VENDER_NAME) LIKE '%").append(venderName.toUpperCase())
						.append("%'");
			}*/

            sql.append(" ORDER BY PD.PART_OLDCODE ");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }

        return ps;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-8-5
     * @Title : 获取采购信息
     */
    public List<Map<String, Object>> getPrtPrcList(String sqlStr) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT BP.*, PD.PART_CODE, PD.PART_OLDCODE, PD.PART_CNAME, VD.VENDER_CODE, VD.VENDER_NAME ");
        sql.append(" FROM TT_PART_BUY_PRICE BP, TT_PART_DEFINE PD, TT_PART_VENDER_DEFINE VD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND BP.PART_ID = PD.PART_ID ");
        sql.append(" AND BP.VENDER_ID = VD.VENDER_ID ");
        sql.append(sqlStr);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-10-28
     * @Title : 获取配件信息
     */
    public List<Map<String, Object>> getPartInfo(String sqlStr) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT PD.* ");
        sql.append(" FROM TT_PART_DEFINE PD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-8-5
     * @Title : 配件供应商制造商关系查询
     */
    public PageResult<Map<String, Object>> relationSearch(String sqlStr, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuilder sql = new StringBuilder("");

            sql.append("SELECT RL.*, MD.MAKER_CODE, MD.MAKER_NAME ");
            sql.append(" FROM TT_PART_MAKER_RELATION RL, TT_PART_MAKER_DEFINE MD ");
            sql.append(" WHERE 1 = 1 ");
            sql.append(" AND RL.MAKER_ID = MD.MAKER_ID ");
            sql.append(sqlStr);

            sql.append(" ORDER BY MD.MAKER_CODE ");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }

        return ps;
    }

    /**
     * @param : @param sqlStr
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-8-5
     * @Title : 获取制造商信息
     */
    public PageResult<Map<String, Object>> getMakers(String sqlStr, int pageSize, int curPage) {
        PageResult<Map<String, Object>> result = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT MD.* ");
        sql.append(" FROM TT_PART_MAKER_DEFINE MD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND MD.STATE = '" + Constant.STATUS_ENABLE + "' ");
        sql.append(sqlStr);
        sql.append(" ORDER BY MD.MAKER_CODE ");
        result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return result;
    }

    /**
     * @param : @param venderId
     * @param : @param makerId
     * @param : @param partId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-8-5
     * @Title : 验证配件供应商制造商关系是否已存在
     */
    public List<Map<String, Object>> relationCheck(String venderId, String makerId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT RL.*, MD.MAKER_CODE, MD.MAKER_NAME FROM TT_PART_VENDER_MAKER_RELATION RL, TT_PART_MAKER_DEFINE MD ");
        sql.append(" WHERE 1= 1 ");
        sql.append(" AND RL.MAKER_ID = MD.MAKER_ID ");
        sql.append(" AND RL.VENDER_ID = '" + venderId + "' ");
        sql.append(" AND RL.MAKER_ID = '" + makerId + "' ");
        sql.append(" AND RL.PART_ID = '" + partId + "' ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param makerId
     * @param : @param partId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-8-5
     * @Title : 配件制造商关系检查
     */
    public List<Map<String, Object>> partMakerCheck(String makerId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT RL.*, MD.MAKER_CODE, MD.MAKER_NAME FROM TT_PART_MAKER_RELATION RL, TT_PART_MAKER_DEFINE MD ");
        sql.append(" WHERE 1= 1 ");
        sql.append(" AND RL.MAKER_ID = '" + makerId + "' ");
        sql.append(" AND RL.PART_ID = '" + partId + "' ");
        sql.append(" AND RL.MAKER_ID = MD.MAKER_ID ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-10-29
     * @Title : 获取配件采购信息
     */
    public List<Map<String, Object>> getPartBuyPriceList(String sqlStr) {
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT BP.*, VD.VENDER_CODE, VD.VENDER_NAME FROM TT_PART_BUY_PRICE BP, TT_PART_VENDER_DEFINE VD ");
        sql.append(" WHERE 1= 1 ");
        sql.append(" AND BP.VENDER_ID = VD.VENDER_ID ");
        sql.append(sqlStr);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
    
    public PageResult<Map<String, Object>> queryPartInfoList1(
            TtPartDefinePO bean, String whid, Integer curPage, Integer pageSize)
            throws Exception {

        PageResult<Map<String, Object>> ps;
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append("SELECT *  FROM ( SELECT PD.PART_ID,\n");
            sql.append("           PD.PART_OLDCODE,\n");
            sql.append("           PD.PART_CODE,\n");
            sql.append("           PD.PART_CNAME,\n");
            sql.append("           PD.Remark,\n");
            sql.append("           NVL(SUM(SC.ITEM_QTY + SC.BUY_QTY), 0) QCQTY, --期初及购入数量\n");
            sql.append("           TRUNC(NVL(SUM(SC.COST_PRICE), 0),6) YCCB, --月初成本单价\n");
            sql.append("           TRUNC(NVL(SUM(SC.COST_SUM + SC.BUY_SUM + SC.DEC_SUM),0),6) QCAMOUNT --期初及购入成本\n");
            sql.append("      FROM TT_PART_DEFINE PD, TT_PART_STOCK_COST SC\n");
            sql.append("     WHERE PD.PART_ID = SC.PART_ID\n");

//            if (!"".equals(whid)&&null!=whid) {
//                sql.append("    AND SC.WH_ID = " + whid + "\n");
//            }
            if (!"".equals(bean.getPartOldcode())&&bean.getPartOldcode()!=null) {
                sql.append(" AND UPPER(PD.PART_OLDCODE) LIKE '%").append(bean.getPartOldcode().toUpperCase()).append("%'");
            }
            if (!"".equals(bean.getPartCname())&&bean.getPartCname()!=null) {
                sql.append(" AND UPPER(PD.PART_CNAME) LIKE '%").append(bean.getPartCname().toUpperCase()).append("%'");
            }
            sql.append("    GROUP BY PD.PART_ID, PD.PART_OLDCODE, PD.PART_CODE, PD.PART_CNAME,PD.Remark) T ORDER BY T.PART_OLDCODE, T.PART_CNAME");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }

        return ps;
    }

}
