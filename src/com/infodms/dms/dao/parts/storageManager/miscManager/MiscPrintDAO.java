package com.infodms.dms.dao.parts.storageManager.miscManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class MiscPrintDAO extends BaseDao<PO> {
    public static Logger logger = Logger.getLogger(MiscPrintDAO.class);
    private static final MiscPrintDAO dao = new MiscPrintDAO();

    private MiscPrintDAO() {
    }

    public static final MiscPrintDAO getInstance() {
        return dao;
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 杂项出、入库单打印头部信息查询
     * @author :
     * @LastDate : 2013-7-1
     */
    public List<Map<String, Object>> getMainPrintHead(RequestWrapper request, String orgId) {

        String orderId = CommonUtils.checkNull(request.getParamValue("value"));//单据ID
        int b_type = Integer.parseInt(CommonUtils.checkNull(request.getParamValue("type")));//单据类型
        StringBuffer sql = new StringBuffer("");


        sql.append(" WITH ORG_infO AS \n");
        sql.append(" (SELECT M.MISC_ORDER_ID, O.ORG_ID, O.ORG_NAME \n");
        sql.append(" FROM TM_ORG O, TT_PARTS_MISC_MAIN M \n");
        sql.append(" WHERE O.ORG_ID = M.ORG_ID \n");
        sql.append(" UNION \n");
        sql.append(" SELECT M.MISC_ORDER_ID, TD.DEALER_ID ORG_ID, TD.DEALER_NAME ORG_NAME \n");
        sql.append(" FROM TM_DEALER TD, TT_PARTS_MISC_MAIN M \n");
        sql.append(" WHERE TD.DEALER_ID = M.ORG_ID) \n");

        sql.append("SELECT T1.MISC_ORDER_ID, \n");//出库单ID
        sql.append("	T1.MISC_ORDER_CODE, \n");//出库单号
        sql.append("	decode(T1.B_TYPE,1,'杂项入库','杂项出库') B_TYPE, \n");//货位/库位
        sql.append("	ORG.ORG_ID, \n");//出库单位ID
        sql.append("	ORG.ORG_NAME, \n");//出库单位
        sql.append("	T3.WH_NAME, \n");//出库仓库
        sql.append("	substr(T1.REMARK,1,100)  REMARK, \n");//备注
        sql.append("	TO_CHAR(T1.CREATE_DATE, 'yyyy-mm-dd') AS CREATE_DATE, \n");//出库日期
        sql.append("	T5.DEPARTMENT_NAME  \n");//领料单位
        sql.append("FROM TT_PARTS_MISC_MAIN       T1, \n");
        sql.append("	TT_PART_WAREHOUSE_DEFINE T3, \n");
        sql.append("	TC_USER                  T4, \n");
        sql.append("	tt_vs_department         T5, \n");
        sql.append("	ORG_INFO                 ORG \n");
        sql.append("WHERE T1.WH_ID = T3.WH_ID \n");
        sql.append("	AND T1.MISC_ORDER_ID = ORG.MISC_ORDER_ID \n");
        sql.append("	AND T1.B_TYPE = " + b_type + " \n");
        sql.append("	AND T1.CREATE_BY = T4.USER_ID \n");
        sql.append("	and T1.Department_Code = T5.Department_Code(+) \n");
        sql.append("	AND T1.MISC_ORDER_ID='" + orderId + "' \n");
        sql.append("order by T1.CREATE_DATE desc \n");

        logger.info(sql.toString());
        return dao.pageQuery(sql.toString(), null, getFunName());
    }


    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 杂项出、入库单打印列表信息查询
     * @author :
     * @LastDate : 2013-7-1
     */
    public List<Map<String, Object>> getMainPrintList(RequestWrapper request, String orgId) {
        String orderId = CommonUtils.checkNull(request.getParamValue("value"));//单据ID

        StringBuffer sql = new StringBuffer("");


        sql.append("SELECT T1.MISC_DETAIL_ID,\n");
        sql.append("       T1.LOC_NAME,\n");
        sql.append("       T2.PART_ID,\n");
        sql.append("       T2.PART_CODE,\n");
        sql.append("       T2.PART_OLDCODE,\n");
        sql.append("       T2.PART_CNAME,\n");
        sql.append("       T2.UNIT,\n");
        sql.append("       T2.MIN_PACK1,\n");
        sql.append("       IN_QTY,\n");
        sql.append("       LD.LOC_CODE\n");
        sql.append("  FROM TT_PARTS_MISC_DETAIL    T1,\n");
        sql.append("       TT_PART_DEFINE          T2,\n");
        sql.append("       TT_PART_LOACTION_DEFINE LD\n");
        sql.append(" WHERE T1.LOC_ID = LD.LOC_ID\n");
        sql.append("   AND T1.PART_ID = T2.PART_ID");
        sql.append("   AND MISC_ORDER_ID = " + orderId + " \n");

        logger.info(sql.toString());
        return dao.pageQuery(sql.toString(), null, getFunName());
    }


    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 杂项出、入库单打印合计信息查询
     * @author :
     * @LastDate : 2013-7-1
     */
    public List<Map<String, Object>> getMainPrintSum(RequestWrapper request, String orgId) {

        String orderId = CommonUtils.checkNull(request.getParamValue("value"));//单据ID
        StringBuffer sql = new StringBuffer("");


        sql.append("SELECT SUM(A.IN_QTY) SUM_IN_QTY \n");
        sql.append("FROM ( \n");
        sql.append("select t1.MISC_DETAIL_ID, \n");//出库单号
        sql.append("t1.LOC_NAME, \n");//货位/库位
        sql.append("t2.PART_ID, \n");//配件id
        sql.append("t2.PART_CODE, \n");//配件件号
        sql.append("t2.PART_OLDCODE, \n");//配件编码
        sql.append("t2.PART_CNAME, \n");//配件名称
        sql.append("t2.UNIT, \n");//单位
        sql.append("t2.MIN_PACK1, \n");//最小包装量（供应中心）
        sql.append("IN_QTY \n");//数量
        sql.append("from TT_PARTS_MISC_DETAIL t1, TT_PART_DEFINE t2 \n");
        sql.append("where MISC_ORDER_ID = " + orderId + " \n");
        sql.append("and t1.PART_ID = t2.PART_ID \n");
        sql.append(") A \n");


        logger.info(sql.toString());
        return dao.pageQuery(sql.toString(), null, getFunName());
    }


    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }


}
