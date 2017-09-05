package com.infodms.dms.dao.parts.baseManager.partBarCodePrt;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-6-16
 * @ClassName : partBarCodePrtDao
 */
public class partBarCodePrtDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partBarCodePrtDao.class);
    private static final partBarCodePrtDao dao = new partBarCodePrtDao();

    private partBarCodePrtDao() {
    }

    public static final partBarCodePrtDao getInstance() {
        return dao;
    }

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @param whereSql
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 返回配件查询结果
     * @Title : 获取配件编码等信息
     */
    public PageResult<Map<String, Object>> getPartOLCode(int pageSize, int curPage, String strSql) {
        PageResult<Map<String, Object>> result = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT T.PART_ID, T.PART_CODE, T.PART_OLDCODE, T.PART_CNAME,T.OEM_MIN_PKG,T.MIN_PKG,b.NORMAL_QTY,b.loc_code\n");
        sql.append("  FROM TT_PART_DEFINE T,vw_part_stock b\n");
        sql.append(" WHERE 1 = 1 and b.PART_ID(+)=t.part_id\n");
        sql.append(" AND T.STATE = '" + Constant.STATUS_ENABLE + "' \n");
        sql.append(strSql);
        result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return result;
    }

    /**
     * @param : @param strSql
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-6-16
     * @Title :
     * @Description: 分页查询供应商信息
     */
    public PageResult<Map<String, Object>> queryPartVender(
            String strSql, int curPage, int pageSize) {
        PageResult<Map<String, Object>> ps;
        StringBuffer sql = new StringBuffer("");


        sql.append("SELECT DISTINCT  MD.MAKER_ID   VENDER_ID,\n");
        sql.append("       MD.MAKER_CODE VENDER_CODE,\n");
        sql.append("       MD.MAKER_NAME VENDER_NAME\n");
        sql.append("  FROM TT_PART_MAKER_RELATION MR, TT_PART_DEFINE D, TT_PART_MAKER_DEFINE MD\n");
        sql.append(" WHERE MR.PART_ID = D.PART_ID\n");
        sql.append("   AND MR.MAKER_ID = MD.MAKER_ID\n");
        sql.append(" AND md.STATUS = 1 AND MD.STATE = '" + Constant.STATUS_ENABLE + "' ");
        sql.append(strSql);
        sql.append("ORDER BY MD.MAKER_CODE DESC");

        ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

}
