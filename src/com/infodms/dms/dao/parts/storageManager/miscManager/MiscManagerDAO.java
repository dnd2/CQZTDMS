package com.infodms.dms.dao.parts.storageManager.miscManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MiscManagerDAO extends BaseDao<PO> {
    public static Logger logger = Logger.getLogger(MiscManagerDAO.class);
    private static final MiscManagerDAO dao = new MiscManagerDAO();

    private MiscManagerDAO() {
    }

    public static final MiscManagerDAO getInstance() {
        return dao;
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 杂项入库单查询实际执行
     * @author : andyzhou
     * @LastDate : 2013-7-1
     */
    public PageResult<Map<String, Object>> getMainList(RequestWrapper request, String orgId, int b_type, int pageSize, int curPage) {
        List<Object> params = new LinkedList<Object>();
        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode")); // 入库单号
        String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 开始时间
        String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 截止时间
        StringBuffer sql = new StringBuffer("");
        //modify by yuan 20130809
        //sql.append("select t1.MISC_ORDER_ID,t1.MISC_ORDER_CODE,t2.ORG_ID,t2.ORG_CODE,t2.ORG_NAME,t3.WH_ID,t3.WH_CODE,t3.WH_NAME,t1.REMARK,to_char(t1.CREATE_DATE,'yyyy-mm-dd') as CREATE_DATE,t4.NAME   \n");
        //sql.append("from TT_PARTS_MISC_MAIN t1,TM_ORG t2,TT_PART_WAREHOUSE_DEFINE t3,TC_USER t4 where t1.ORG_ID=t2.ORG_ID and t1.WH_ID=t3.WH_ID and t1.CREATE_BY=t4.USER_ID  \n");

        sql.append("SELECT T1.MISC_ORDER_ID,\n");
        sql.append("       T1.MISC_ORDER_CODE,\n");
        sql.append("       T2.DEALER_ID ORG_ID,\n");
        sql.append("       T2.DEALER_CODE ORG_CODE,\n");
        sql.append("       T2.DEALER_NAME ORG_NAME,\n");
        sql.append("	   T1.B_TYPE, \n");
        sql.append("       T3.WH_ID,\n");
        sql.append("       T3.WH_CODE,\n");
        sql.append("       T3.WH_NAME,\n");
        sql.append("       T1.REMARK,\n");
        sql.append("       TO_CHAR(T1.CREATE_DATE, 'yyyy-mm-dd') AS CREATE_DATE,\n");
        sql.append("       T4.NAME\n");
        sql.append("  FROM TT_PARTS_MISC_MAIN       T1,\n");
        sql.append("       TM_DEALER                T2,\n");
        sql.append("       TT_PART_WAREHOUSE_DEFINE T3,\n");
        sql.append("       TC_USER                  T4\n");
        sql.append(" WHERE T1.ORG_ID = T2.DEALER_ID(+)\n");
        sql.append("   AND T1.WH_ID = T3.WH_ID\n");
        sql.append("   AND T1.B_TYPE = " + b_type + "\n");
        sql.append("   AND T1.CREATE_BY = T4.USER_ID\n");


        if (null != orderCode && !orderCode.equals("")) {
            sql.append("and UPPER(t1.MISC_ORDER_CODE) like'%" + orderCode.trim().toUpperCase() + "%'  \n");
        }
        if (null != checkSDate && !"".equals(checkSDate)) {
            sql.append(" AND TO_CHAR(t1.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
        }
        if (null != checkEDate && !"".equals(checkEDate)) {
            sql.append(" AND TO_CHAR(T1.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
        }
        sql.append(" and  t1.org_id = '" + orgId + "' ");
        sql.append(" order by t1.CREATE_DATE desc  \n");
        logger.info(sql.toString());
        return dao.pageQuery(sql.toString(), params, getFunName() + System.currentTimeMillis(), pageSize, curPage);
    }


    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 获取配件列表
     * @author : andyzhou
     * @LastDate : 2013-7-1
     */
    public PageResult<Map<String, Object>> showPartBase(RequestWrapper request, int pageSize, int curPage) {
        String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));// 制单单位ID
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));// 仓库ID
        String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));// 件号
        String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));// 配件编码
        String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));// 配件名称

        StringBuffer sql = new StringBuffer();
        sql.append("select PART_ID,PART_CODE,PART_OLDCODE,PART_CNAME,nvl(UNIT,'件') UNIT,BUY_MIN_PKG as MIN_PACKAGE  \n");
        sql.append("from TT_PART_DEFINE  \n");
        sql.append("where 1=1   \n");
        if (null != partCode && !partCode.equals("")) {
            sql.append("and UPPER(PART_CODE) like'%" + partCode.trim().toUpperCase() + "%'  \n");
        }
        if (null != partOldcode && !partOldcode.equals("")) {
            sql.append("and UPPER(PART_OLDCODE) like'%" + partOldcode.trim().toUpperCase() + "%'  \n");
        }
        if (null != partCname && !partCname.equals("")) {
            sql.append("and PART_CNAME like'%" + partCname.trim() + "%'  \n");
        }
        sql.append("order by PART_OLDCODE  \n");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION :杂项入库单主表信息
     * @author : andyzhou
     * @LastDate : 2013-7-1
     */
    public Map<String, Object> getMainData(String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T1.MISC_ORDER_CODE,\n");
        sql.append("       T1.ORG_ID,\n");
        sql.append("       T2.WH_ID,\n");
        sql.append("       T2.WH_CODE,\n");
        sql.append("       T2.WH_NAME,\n");
        sql.append("       T1.REMARK,\n");
        sql.append("       TO_CHAR(T1.CREATE_DATE, 'yyyy-mm-dd') AS CREATE_DATE,\n");
        sql.append("       T3.NAME,\n");
        sql.append("       DECODE(T1.B_TYPE, 1, '杂项入库', '杂项出库') B_TYPE,\n");
        sql.append("       TC.CODE_DESC EX_TYPE\n");
        sql.append("  FROM TT_PARTS_MISC_MAIN T1, TT_PART_WAREHOUSE_DEFINE T2, TC_USER T3, TC_CODE TC\n");
        sql.append(" WHERE MISC_ORDER_ID = '"+orderId+"'\n");
        sql.append("   AND T1.WH_ID = T2.WH_ID\n");
        sql.append("   AND T1.EX_TYPE = TC.CODE_ID\n");
        sql.append("   AND T1.CREATE_BY = T3.USER_ID\n");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }


    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION :杂项入库单配件明细
     * @author : andyzhou
     * @LastDate : 2013-7-1
     */
    public PageResult<Map<String, Object>> getDetailList(String orderId, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T1.MISC_DETAIL_ID,\n");
        sql.append("       T1.PART_ID,\n");
        sql.append("       T2.PART_CODE,\n");
        sql.append("       T2.PART_OLDCODE,\n");
        sql.append("       T2.PART_CNAME,\n");
        sql.append("       T1.LOC_CODE,\n");
        sql.append("       T1.UNIT,\n");
        sql.append("       T1.MIN_PACKAGE,\n");
        sql.append("       T1.BATCH_NO,\n");
        sql.append("       IN_QTY\n");
        sql.append("  FROM TT_PARTS_MISC_DETAIL T1, TT_PART_DEFINE T2\n");
        sql.append(" WHERE MISC_ORDER_ID = '"+orderId+"'\n");
        sql.append("   AND T1.PART_ID = T2.PART_ID\n");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }


    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-8-22
     * @Title : 配件验证
     */
    public List<Map<String, Object>> checkPartState(String sqlStr) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT VM.* "
                + " FROM VW_PART_STOCK VM "
                + " WHERE 1 = 1 ");
        sql.append(sqlStr);

        list = pageQuery(sql.toString(), null, getFunName());

        return list;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-8-22
     * @Title : 获取仓库信息
     */
    public List<Map<String, Object>> getWareHouses(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.WH_ID, TM.WH_NAME, TM.WH_CODE "
                        + " FROM TT_PART_WAREHOUSE_DEFINE TM "
                        + " WHERE 1 = 1  ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.WH_ID ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param delCode
     * @param : @return
     * @return :
     * LastDate    : 2013-4-15
     * @Title : 验证服务商编码是否存在 并返回服务商ID、Name
     * @Description:
     */
    public List checkOldCode(String oldCode) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT TD.* FROM TT_PART_DEFINE TD " +
                " WHERE  TD.PART_OLDCODE = '" + oldCode + "' ";
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }

    /**
     * @param : @param oemCompanyId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title : 返回主机厂名称
     */
    public String getMainCompanyName(String oemCompanyId) {
        String companyName = "";
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.COMPANY_NAME "
                        + " FROM TM_COMPANY TM "
                        + " WHERE 1 = 1  ");

        sql.append("  AND TM.COMPANY_ID = '" + oemCompanyId + "' ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            companyName = list.get(0).get("COMPANY_NAME").toString();
        }

        return companyName;
    }

    /**
     * @param : @param oemCompanyId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title : 返回服务商名称
     */
    public String getDealerName(String oemCompanyId) {
        String companyName = "";
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TD.DEALER_NAME "
                        + " FROM TM_DEALER TD "
                        + " WHERE 1 = 1  ");

        sql.append("  AND TD.DEALER_ID = '" + oemCompanyId + "' ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            companyName = list.get(0).get("DEALER_NAME").toString();
        }

        return companyName;
    }

    public List<Map<String, Object>> getPartInfo() throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T1.PART_ID, \n");
            sql.append("        T1.PART_CODE, \n");
            sql.append("        T1.PART_OLDCODE, \n");
            sql.append("        T1.PART_CNAME, \n");
            sql.append("        T1.UNIT, \n");
            sql.append("        T2.QTY INQTY \n");
            sql.append("   FROM TT_PART_DEFINE T1, TMP_PART_UPLOAD T2 \n");
            sql.append("  WHERE T1.PART_OLDCODE = T2.PART_OLDCODE \n");

            list = this.pageQuery(sql.toString(), null, this.getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    /**
     * 查询杂项明细
     *
     * @param request
     * @param pageSize
     * @param curPage
     * @return
     */
    public PageResult<Map<String, Object>> getMiscDtl(RequestWrapper request, String orgId, String b_type, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode")); // 入库单号
        String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 开始时间
        String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 截止时间

        sql.append("SELECT T3.MISC_ORDER_CODE,\n");
        sql.append("       DECODE(T3.B_TYPE, 1, '杂入', '杂出') B_TYPE,\n");
        sql.append("       T2.PART_ID,\n");
        sql.append("       T2.PART_CODE,\n");
        sql.append("       T2.PART_OLDCODE,\n");
        sql.append("       T2.PART_CNAME,\n");
        sql.append("       T2.UNIT,\n");
        sql.append("       T2.MIN_PACK1,\n");
        sql.append("       T1.IN_QTY,\n");
        //sql.append("       T1.PRICE,\n");
        //sql.append("       ROUND(T1.IN_QTY * T1.PRICE, 2) AMOUNT,\n");
        sql.append("       TU.NAME,\n");
        sql.append("       T3.CREATE_DATE,\n");
        sql.append("       T3.REMARK\n");
        sql.append("  FROM TT_PARTS_MISC_DETAIL T1,\n");
        sql.append("       TT_PART_DEFINE       T2,\n");
        sql.append("       TT_PARTS_MISC_MAIN   T3,\n");
        sql.append("       TC_USER              TU\n");
        sql.append(" WHERE T1.MISC_ORDER_ID = T3.MISC_ORDER_ID\n");
        sql.append("   AND T3.CREATE_BY = TU.USER_ID\n");
        sql.append("   AND T1.PART_ID = T2.PART_ID\n");
        sql.append("   AND T3.B_TYPE = " + b_type + "\n");

        if (null != orderCode && !orderCode.equals("")) {
            sql.append("and UPPER(t3.MISC_ORDER_CODE) like'%" + orderCode.trim().toUpperCase() + "%'  \n");
        }
        if (null != checkSDate && !"".equals(checkSDate)) {
            sql.append(" AND TO_CHAR(t3.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
        }
        if (null != checkEDate && !"".equals(checkEDate)) {
            sql.append(" AND TO_CHAR(T3.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
        }
        sql.append(" and  t3.org_id = '" + orgId + "' ");
        sql.append(" order by t3.CREATE_DATE desc  \n");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }
}
