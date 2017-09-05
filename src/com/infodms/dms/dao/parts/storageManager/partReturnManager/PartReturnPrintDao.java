package com.infodms.dms.dao.parts.storageManager.partReturnManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartReturnPrintDao extends BaseDao<PO> {

    public static Logger logger = Logger.getLogger(PartReturnPrintDao.class);

    private static final PartReturnPrintDao dao = new PartReturnPrintDao();

    private PartReturnPrintDao() {
    }

    public static final PartReturnPrintDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 退货单打印头部信息查询
     * @author :
     * @LastDate : 2013-7-1
     */
    public List<Map<String, Object>> PrintHead(RequestWrapper request, String returnId) {

        String value = CommonUtils.checkNull(request.getParamValue("value"));//单据ID
        //int state = Integer.parseInt(CommonUtils.checkNull(request.getParamValue("state")));
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT T1.RETURN_CODE,\n"); //--退货单号
        sql.append("T1.DEALER_NAME,\n"); //--退货单位
        sql.append("to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') DEALER_DATE,\n"); //--退货日期
        sql.append("T1.REMARK \n");//--退货原因
        sql.append("FROM TT_PART_DLR_RETURN_MAIN T1, TT_PART_WAREHOUSE_DEFINE T2, TC_USER T3 \n");
        sql.append("WHERE T1.STOCK_OUT = T2.WH_ID \n");
        sql.append("AND T1.CREATE_BY = T3.USER_ID \n");
        //sql.append("AND T1.RETURN_ID = 2014071896285135 \n");
        sql.append("AND T1.RETURN_ID ='" + value + "' \n");

        logger.info(sql.toString());
        return dao.pageQuery(sql.toString(), null, getFunName());
    }


    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 退货单打印列表信息查询
     * @author :
     * @LastDate : 2013-7-1
     */
    public List<Map<String, Object>> returnPrintList(RequestWrapper request, String returnId) {
        String value = CommonUtils.checkNull(request.getParamValue("value"));//单据ID

        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT T.PART_OLDCODE,\n"); //--配件编码
        sql.append("T.PART_CNAME,\n"); //--配件名称
        sql.append("T.UNIT,\n"); //-- 单位
        sql.append("T.IN_QTY,\n"); //--已退货数量
        sql.append("ld.loc_code,\n"); //--货位
        sql.append("wd.wh_name \n");
        sql.append("FROM TT_PART_DLR_RETURN_DTL   T,\n");
        sql.append("TT_PART_DLR_RETURN_MAIN  T1,\n");
        sql.append("TT_PART_LOACTION_DEFINE  LD,\n");
        sql.append("TT_PART_WAREHOUSE_DEFINE WD \n");
        sql.append("WHERE T.RETURN_ID = T1.RETURN_ID \n");
        sql.append("AND T.INLOC_ID = LD.LOC_ID(+) \n");
        sql.append("and t1.stock_in = wd.wh_id(+) \n");
        //sql.append("AND T.RETURN_ID = 2014071896285135 \n");
        sql.append("AND T.RETURN_ID ='" + value + "' \n");


        logger.info(sql.toString());
        return dao.pageQuery(sql.toString(), null, getFunName());
    }


    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 退货单打印合计信息查询
     * @author :
     * @LastDate : 2013-7-1
     */
    public List<Map<String, Object>> returnPrintSum(RequestWrapper request, String returnId) throws Exception {

        String value = CommonUtils.checkNull(request.getParamValue("value"));//单据ID
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT SUM(T.IN_QTY) SUM_IN_QTY\n");
            sql.append("  FROM TT_PART_DLR_RETURN_DTL T, TT_PART_DLR_RETURN_MAIN T1\n");
            sql.append(" WHERE T.RETURN_ID = T1.RETURN_ID\n");
            sql.append("   AND T.RETURN_ID = '" + value + "'");
            return dao.pageQuery(sql.toString(), null, getFunName());

        } catch (Exception e) {

            throw e;
        }
    }
}
