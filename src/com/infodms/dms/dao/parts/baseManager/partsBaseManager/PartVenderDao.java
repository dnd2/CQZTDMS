package com.infodms.dms.dao.parts.baseManager.partsBaseManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.po.TtPartVenderDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : chenjunjiang CreateDate : 2013-4-2
 * @ClassName : PartVenderDao
 * @Description : 供应商dao
 */
public class PartVenderDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartVenderDao.class);

    private static final PartVenderDao dao = new PartVenderDao();

    private PartVenderDao() {

    }

    public static final PartVenderDao getInstance() {
        return dao;
    }

    @Override
    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param : @param bean
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-2
     * @Title :
     * @Description: 分页查询供应商信息
     */
    public PageResult<Map<String, Object>> queryPartVenderList(
            TtPartVenderDefinePO bean, int curPage, int pageSize)
            throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(

                    "SELECT T.VENDER_ID,\n" + "       T.VENDER_CODE,\n"
                            + "       T.VENDER_NAME,\n" + "       T.LINKMAN,\n"
                            + "       T.TEL,\n" + "       T.CREATE_DATE,\n"
                            + "       T.UPDATE_DATE,\n" + "       U.NAME ACNT,\n"
                            + "       T.STATE,\n" + "      T.INV_TYPE\n"
                            + "  FROM TT_PART_VENDER_DEFINE T, TC_USER U\n"
                            + " WHERE T.UPDATE_BY = U.USER_ID(+)\n"
                            + "   AND T.STATUS = 1");
            if (bean.getState() != 0) {
                sql.append(" AND T.STATE=").append(bean.getState())
                        .append("\n");
            }
            if (bean.getInvType() != 0) {
                sql.append(" AND T.INV_TYPE=").append(bean.getInvType())
                        .append("\n");
            }
            if (!bean.getVenderCode().equals("")) {
                sql.append("AND UPPER(T.VENDER_CODE) LIKE '%")
                        .append(bean.getVenderCode()).append("%'\n");
            }
            if (!bean.getVenderName().equals("")) {
                sql.append("AND T.VENDER_NAME LIKE '%")
                        .append(bean.getVenderName()).append("%'\n");
            }
            sql.append("ORDER BY T.VENDER_CODE");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    /**
     * @param : @param venderId
     * @param : @return
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-2
     * @Title :
     * @Description: 查询供应商详细信息
     */
    public Map<String, Object> getPartVenderDetail(String venderId)
            throws Exception {
        Map<String, Object> map;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT A.VENDER_ID,  A.VENDER_CODE,   A.VENDER_NAME,  A.LINKMAN,  A.TEL,A.FAX,A.ADDR,A.IS_ABROAD,A.VENDER_TYPE,A.STATE,A.INV_TYPE ");
            sql.append(" FROM TT_PART_VENDER_DEFINE A WHERE A.VENDER_ID = ")
                    .append(venderId);
            map = pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return map;
    }

    /**
     * @param : @param venderCode
     * @param : @return
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-3
     * @Title :
     * @Description: 判断供应商编码是否存在
     */
    public boolean existVenderCode(String venderCode) throws Exception {

        try {
            TtPartVenderDefinePO po = new TtPartVenderDefinePO();
            po.setVenderCode(venderCode);
            List list = select(po);
            if (list.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * @param : @param venderName
     * @param : @return
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-3
     * @Title :
     * @Description: TODO
     */
    public boolean existVenderName(String venderName) throws Exception {
        try {
            TtPartVenderDefinePO po = new TtPartVenderDefinePO();
            po.setVenderName(venderName);
            List list = select(po);
            if (list.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    public void validPartVender(String venderId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("update TT_PART_VENDER_DEFINE t set t.STATE=")
                    .append(Constant.STATUS_ENABLE)
                    .append(" where t.VENDER_ID=").append(venderId);
            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    public Map getPartVenderByCode(String venderCode) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.VENDER_ID,T.VENDER_CODE,T.VENDER_NAME FROM TT_PART_VENDER_DEFINE T WHERE T.VENDER_CODE='")
                    .append(venderCode)
                    .append("'");
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPartMakerListByVId(
            String venderId, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            //modify by yuan 20130731
            sql.append("SELECT Distinct T.MAKER_ID, T.MAKER_CODE, T.MAKER_NAME, T.LINKMAN, T.TEL, T.STATE\n");
            sql.append("  FROM TT_PART_MAKER_DEFINE T,tt_part_vender_maker_relation r\n");
            sql.append(" WHERE t.maker_id=r.maker_id\n");

            sql.append(" AND R.VENDER_ID=").append(venderId);
            sql.append(" AND T.STATUS = 1");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    /**
     * <p>Description: 获取配件供应商供货比例sql字符串</p>
     * @param paramMap
     * @return
     */
    private String getPartInfoSql(Map<String, String> paramMap) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T.PART_ID, \n");
        sql.append("       T.PART_CODE, \n");
        sql.append("       T.PART_OLDCODE, \n");
        sql.append("       T.PART_CNAME, \n");
        sql.append("       T.PRODUCE_STATE, \n");
        sql.append("       T.MODEL_NAME, \n");
        sql.append("       T.IS_PART_DISABLE, \n");
        sql.append("       T.STATE, \n");
        sql.append("       T2.COEFF_NUM \n");
        sql.append("  FROM TT_PART_DEFINE T \n");
        sql.append(" INNER JOIN ( \n");
        sql.append("    SELECT T1.PART_ID, \n");
        sql.append("           TO_CHAR(LISTAGG(T2.COEFF_NUM * 100, ':') WITHIN \n");
        sql.append("                   GROUP(ORDER BY T2.VENDER_ID)) COEFF_NUM \n");
        sql.append("      FROM TT_PART_DEFINE T1, TT_PART_BUY_PRICE T2 \n");
        sql.append("     WHERE T1.PART_ID = T2.PART_ID \n");
        sql.append("     GROUP BY T1.PART_ID \n");
        sql.append(" ) T2 ON T2.PART_ID = T.PART_ID \n");
        
        sql.append(" WHERE 1=1 \n");
        if(!CommonUtils.isEmpty(paramMap.get("PART_ID"))){
            sql.append("   AND T.PART_ID = '"+paramMap.get("PART_ID").trim()+"' \n");
        }
        if(!CommonUtils.isEmpty(paramMap.get("PART_CODE"))){
            sql.append("   AND T.PART_CODE = '"+paramMap.get("PART_CODE").trim()+"' \n");
        }
        if(!CommonUtils.isEmpty(paramMap.get("PART_OLDCODE"))){
            sql.append("   AND T.PART_OLDCODE = '"+paramMap.get("PART_OLDCODE").trim()+"' \n");
        }
        if(!CommonUtils.isEmpty(paramMap.get("PART_CNAME"))){
            sql.append("   AND T.PART_CNAME LIKE '%"+paramMap.get("PART_CNAME").trim()+"%' \n");
        }
        if(!CommonUtils.isEmpty(paramMap.get("STATE"))){
            sql.append("   AND T.STATE = "+paramMap.get("STATE").trim()+" \n");
        }
        sql.append(" ORDER BY T.CREATE_DATE DESC \n");
        return sql.toString();
    }
    
    /**
     * <p>Description: 供应商配件比例列表</p>
     * @param paramMap
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> qeryPartInfoPageList(Map<String, String> paramMap, Integer curPage,
            Integer pageSize) {
        PageResult<Map<String, Object>> ps = pageQuery(getPartInfoSql(paramMap), null, getFunName(), pageSize, curPage);
        return ps;
    }
    
    /**
     * <p>Description: 获取配件信息列表</p>
     * @param paramMap 查询参数
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPartInfoList(Map<String, String> paramMap){
        List<Map<String, Object>> list = pageQuery(getPartInfoSql(paramMap), null, getFunName());
        return list;
    }

    /**
     * <p>Description: 获取配件供应商比例列表</p>
     * @param paramMap 查询参数
     * @return
     */
    public List<Map<String, Object>> getPartVenderRelationList(Map<String, String> paramMap) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T1.PRICE_ID, T2.VENDER_CODE, T2.VENDER_NAME, T1.COEFF_NUM * 100 COEFF_NUM\n");
        sql.append("  FROM TT_PART_BUY_PRICE T1, TT_PART_VENDER_DEFINE T2 \n");
        sql.append(" WHERE T1.VENDER_ID = T2.VENDER_ID\n");
        sql.append("   AND T1.PART_ID = "+paramMap.get("PART_ID")+" \n");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
}
