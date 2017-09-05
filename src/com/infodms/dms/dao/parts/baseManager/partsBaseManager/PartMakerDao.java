package com.infodms.dms.dao.parts.baseManager.partsBaseManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.po.TtPartMakerDefinePO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : chenjunjiang CreateDate : 2013-4-2
 * @ClassName : PartVenderDao
 * @Description : 制造商dao
 */
public class PartMakerDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartMakerDao.class);

    private static final PartMakerDao dao = new PartMakerDao();

    private PartMakerDao() {

    }

    public static final PartMakerDao getInstance() {
        return dao;
    }

    @Override
    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
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
    public PageResult<Map<String, Object>> queryPartMakerList(TtPartMakerDefinePO bean, String venderCode, String venderName, int curPage, int pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.MAKER_ID,T.MAKER_CODE,T.MAKER_NAME,T1.VENDER_CODE,T1.VENDER_NAME,T.LINKMAN,T.TEL,T.CREATE_DATE,T.UPDATE_DATE,");
            sql.append("U.NAME ACNT,T.STATE FROM TT_PART_MAKER_DEFINE T,TT_PART_VENDER_DEFINE T1,TC_USER U WHERE t.vender_id=t1.vender_id(+) and T.UPDATE_BY = U.USER_ID(+) AND T.STATUS = 1");
            if (bean.getState() != 0) {
                sql.append(" AND T.STATE=").append(bean.getState()).append("\n");
            }
            if (!bean.getMakerCode().equals("")) {
                sql.append(" AND UPPER(T.MAKER_CODE) LIKE '%").append(bean.getMakerCode()).append("%'\n");
            }
            if (!bean.getMakerName().equals("")) {
                sql.append(" AND T.MAKER_NAME LIKE '%").append(bean.getMakerName()).append("%'\n");
            }
            if (!"".equals(venderCode)) {
                sql.append(" AND UPPER(T1.VENDER_CODE) LIKE '%").append(venderCode).append("%'\n");
            }
            if (!"".equals(venderName)) {
                sql.append(" AND T1.VENDER_NAME LIKE '%").append(venderName).append("%'\n");
            }
            sql.append(" ORDER BY T.MAKER_CODE ");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
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
     * @Description: 查询制造商详细信息
     */
    public Map<String, Object> getPartMakerDetail(String makerId) throws Exception {
        Map<String, Object> map;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT A.MAKER_ID,  A.MAKER_CODE,   A.MAKER_NAME,B.VENDER_ID,B.VENDER_CODE,B.VENDER_NAME,  A.LINKMAN,  A.TEL,A.FAX,A.ADDR,A.IS_ABROAD,A.MAKER_TYPE,A.STATE ");
            sql.append(" FROM TT_PART_MAKER_DEFINE A,TT_PART_VENDER_DEFINE B WHERE A.VENDER_ID=B.VENDER_ID(+) AND A.MAKER_ID = ").append(makerId);
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
     * @Description: 判断制造商编码是否存在
     */
    public boolean existMakerCode(String makerCode) throws Exception {

        try {
            TtPartMakerDefinePO po = new TtPartMakerDefinePO();
            po.setMakerCode(makerCode);
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
     * @Description: 判断制造商名称是否已经存在
     */
    public boolean existMakerName(String makerName) throws Exception {
        try {
            TtPartMakerDefinePO po = new TtPartMakerDefinePO();
            po.setMakerName(makerName);
            List list = select(po);
            if (list.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    public void validPartMaker(String makerId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("update TT_PART_MAKER_DEFINE t set t.STATE=").append(Constant.STATUS_ENABLE).append(" where t.MAKER_ID=").append(makerId);
            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }

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
     * @Description: 分页查询供应商和配件关系信息
     */
    public PageResult<Map<String, Object>> queryPartMakerRelation(Long maker_id, String part_oldCode, int curPage, int pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT R.RELAION_ID,D.PART_ID, D.PART_CODE, D.PART_OLDCODE, D.PART_CNAME, R.STATE\n");
            sql.append("  FROM TT_PART_MAKER_RELATION R, TT_PART_DEFINE D\n");
            sql.append(" WHERE R.PART_ID = D.PART_ID\n");
            sql.append("   AND R.MAKER_ID = " + maker_id);

            if (!"".equals(part_oldCode) && part_oldCode != null) {
                sql.append(" AND exists(SELECT 1 FROM tt_part_define pd WHERE pd.part_id=r.part_id AND  pd.part_oldcode like '%" + part_oldCode).append("%' AND pd.status=1) ");
            }
            logger.info("SQL===" + sql);
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }
}
