package com.infodms.dms.dao.parts.storageManager.partReturnManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class ProcurementReturnPrintDao extends BaseDao<PO> {

    public static Logger logger = Logger.getLogger(ProcurementReturnPrintDao.class);

    private static final ProcurementReturnPrintDao dao = new ProcurementReturnPrintDao();

    private ProcurementReturnPrintDao() {
    }

    public static final ProcurementReturnPrintDao getInstance() {
        return dao;
    }

    protected PO wrapperPO(ResultSet rs, int idx) {

        return null;
    }


    //打印查询
    public List<Map<String, Object>> ProcurementReturnPrintQuery(RequestWrapper request, String returnId) {

        String value = CommonUtils.checkNull(request.getParamValue("value"));

        String type = CommonUtils.checkNull(request.getParamValue("type"));

        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT T4.Return_Id, \n");
        sql.append("       T4.RETURN_CODE, \n");
        sql.append("       TO_CHAR(T4.RETURN_DATE,'yyyy-mm-dd hh24:mi:ss') RETURN_DATE,	 \n");
        sql.append("       T4.ORG_NAME, \n");
        sql.append("       T4.REMARK, \n");
        sql.append("       T1.PART_OLDCODE, \n");
        sql.append("       T1.PART_CNAME, \n");
        sql.append("       T3.WH_NAME, \n");
        sql.append("       T1.UNIT, \n");
        sql.append("       T1.RETURN_QTY, \n");
        sql.append("       (select distinct d.vender_code \n");
        sql.append("          from Tt_Part_Oem_Return_Dtl d \n");
        sql.append("         where d.return_id = t4.return_id) vender_code, \n");
        sql.append("       (select distinct d.vender_name \n");
        sql.append("          from Tt_Part_Oem_Return_Dtl d \n");
        sql.append("         where d.return_id = t4.return_id) vender_name \n");
        sql.append("  FROM TT_PART_OEM_RETURN_DTL    T1, \n");
        sql.append("       TT_PART_OEM_RETURN_MAIN  T4, \n");
        sql.append("       TC_USER                  T2, \n");
        sql.append("       TT_PART_WAREHOUSE_DEFINE T3, \n");
        sql.append("       TT_PART_SALES_PRICE      SP \n");
        sql.append(" WHERE T1.RETURN_ID = T4.RETURN_ID \n");
        sql.append("   AND t1.PART_ID = SP.PART_ID \n");
        sql.append("   AND T1.CREATE_BY = T2.USER_ID \n");
        sql.append("   AND T1.STOCK_OUT = T3.WH_ID(+) \n");
//		sql.append("   AND T4.RETURN_TYPE = '"+Constant.PART_RETURN_TYPE_02+"' \n");
        sql.append("   AND T4.RETURN_ID ='" + value + "' \n");

        logger.info(sql.toString());
        return dao.pageQuery(sql.toString(), null, getFunName());
    }

    //合计
    public List<Map<String, Object>> returnPrintSum(RequestWrapper request) {

        String value = CommonUtils.checkNull(request.getParamValue("value"));

        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT SUM(T1.RETURN_QTY) SUM_RETURN_QTY \n");
        sql.append("from TT_PART_RETURN_RECORD T1, TT_PART_OEM_RETURN_MAIN T4 \n");
        sql.append("WHERE T1.RETURN_ID = T4.RETURN_ID \n");
        sql.append("AND T1.RETURN_ID ='" + value + "' \n");

        return dao.pageQuery(sql.toString(), null, getFunName());
    }
}
