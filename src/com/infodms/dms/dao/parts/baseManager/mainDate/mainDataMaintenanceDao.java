package com.infodms.dms.dao.parts.baseManager.mainDate;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class mainDataMaintenanceDao extends BaseDao<PO> {
    public static Logger logger = Logger.getLogger(OrderDeliveryDao.class);
    private static final mainDataMaintenanceDao dao = new mainDataMaintenanceDao();
    private mainDataMaintenanceDao(){}

    public static final mainDataMaintenanceDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> getmainDataMaintenanceList(RequestWrapper request, int curPage, int pageSize) throws Exception {
        String GIFT_TYPE_SEARCH = CommonUtils.checkNull(request.getParamValue("GIFT_TYPE_SEARCH"));// FOR SEARCH
        String giftWay = CommonUtils.checkNull(request.getParamValue("giftWay"));
        String isOemStart = CommonUtils.checkNull(request.getParamValue("isOemStart"));
        String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 制单开始时间
        String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 制单截止时间

        List<Object> params = new LinkedList<Object>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T.GIFT_TYPE, T.GIFT_WAY, T.START_DATE, T.END_DATE, T.IS_OEM_START ");
        sql.append(" FROM TT_PART_GIFT_DEFINE T WHERE 1 = 1");

        if (!("".equals(GIFT_TYPE_SEARCH) || GIFT_TYPE_SEARCH == null)) {
            sql.append("AND UPPER(T.GIFT_TYPE) LIKE '%" + GIFT_TYPE_SEARCH.toUpperCase() + "%'");
        }
        if (!("".equals(giftWay) || giftWay == null)) {
            sql.append("AND T.GIFT_WAY = '" + giftWay + "'");
        }
        if (!("".equals(isOemStart) || isOemStart == null)) {
            sql.append("AND T.IS_OEM_START = '" + isOemStart + "'");
        }
        if (null != checkSDate && !"".equals(checkSDate)) {
            sql.append(" AND TO_CHAR(T.START_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
        }
        if (null != checkEDate && !"".equals(checkEDate)) {
            sql.append(" AND TO_CHAR(T.END_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
        }
        sql.append(" GROUP BY (T.GIFT_TYPE, T.GIFT_WAY, T.START_DATE, T.END_DATE, T.IS_OEM_START) ");
        sql.append(" ORDER BY T.START_DATE DESC ");
        PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
        return rs;

    }

    public PageResult<Map<String, Object>> getmainDataMaintenanceList(String GIFT_TYPE_SEARCH, String GIFT_TYPE, String PART_OLDCODE, String PART_NAME, String giftWay, int curPage, int pageSize) throws Exception {
        List<Object> params = new LinkedList<Object>();
        StringBuffer sql = new StringBuffer();
        sql
                .append("SELECT T.DEF_ID, T.GIFT_TYPE, TO_CHAR(T.CONDITION,'FM999,999,990.00') AS CONDITION, ");
        sql
                .append(" T.PART_ID, T.PART_OLDCODE, T.PART_NAME, T.PART_CODE, T.GIFT_QTY, T.COND_NUM, T.STATE ");
        sql.append(" from tt_part_gift_define t where 1 = 1");

        if (!("".equals(GIFT_TYPE_SEARCH) || GIFT_TYPE_SEARCH == null)) {
            sql.append("and UPPER(t.GIFT_TYPE) like '%"
                    + GIFT_TYPE_SEARCH.toUpperCase() + "%'");
        }
        if (!("".equals(GIFT_TYPE) || GIFT_TYPE == null)) {
            sql.append("and UPPER(t.GIFT_TYPE) = '" + GIFT_TYPE.toUpperCase()
                    + "'");
        }
        if (!("".equals(PART_OLDCODE) || PART_OLDCODE == null)) {
            sql.append("and UPPER(t.PART_CODE) like '%"
                    + PART_OLDCODE.toUpperCase() + "%'");
        }
        if (!("".equals(PART_NAME) || PART_NAME == null)) {
            sql.append("and  UPPER(t.PART_NAME) like '%"
                    + PART_NAME.toUpperCase() + "%'");
        }
        if (null != giftWay && !"".equals(giftWay)) {
            if ("ZDWay".equalsIgnoreCase(giftWay)) {
                sql.append("and  t.GIFT_WAY = '" + Constant.PART_GIFT_WAY_01 + "' ");
            } else {
                sql.append("and  t.GIFT_WAY = '" + Constant.PART_GIFT_WAY_02 + "' ");
            }

        }
        sql.append(" ORDER BY T.GIFT_TYPE, T.CREATE_DATE DESC ");
        PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(),
                params, getFunName(), pageSize, curPage);
        return rs;

    }

    /**
     * @param DEF_ID
     * @return 加载修改数据
     * @throws Exception
     */
    public Map<String, Object> UpdatemainDataMaintenanceList(String sqlStr) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("select T.*, TO_CHAR(T.CONDITION,'FM999,999,990.00') AS CONDITION_FM, TO_CHAR(T.START_DATE, 'yyyy-MM-dd') AS START_DATE_FM, " +
                " TO_CHAR(T.END_DATE, 'yyyy-MM-dd') AS END_DATE_FM from tt_part_gift_define t where 1=1 ");
        sql.append(sqlStr);
        return dao.pageQueryMap(sql.toString(), null, getFunName());

    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-6
     * @Title : 配件List
     */
    public List<Map<String, Object>> getPartList(String sqlStr) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT PD.* ");
        sql.append("  FROM TT_PART_DEFINE PD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param giftType
     * @param : @param partId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-12
     * @Title : 验证关系是否已存在
     */
    public List<Map<String, Object>> getExistPO(String giftType, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT GD.* FROM TT_PART_GIFT_DEFINE GD ");
        sql.append(" WHERE 1= 1 ");
        sql.append(" AND GD.GIFT_TYPE = '" + giftType + "' ");
        sql.append(" AND GD.PART_ID = '" + partId + "' ");

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param giftType
     * @param : @param partId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-11-12
     * @Title : 获取最新的赠品信息
     */
    public List<Map<String, Object>> getLatestDef(String sqlStr) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT TT.*, TO_CHAR(TT.END_DATE, 'yyyy-MM-dd') AS END_DATE_FM, TO_CHAR(TT.START_DATE, 'yyyy-MM-dd') AS START_DATE_FM  " +
                "FROM (SELECT T.*, ROW_NUMBER() OVER (ORDER BY END_DATE DESC) AS RNUM FROM TT_PART_GIFT_DEFINE T WHERE 1 = 1 " +
                sqlStr + ") TT WHERE RNUM = 1 ");

        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }
}
