package com.infodms.dms.dao.parts.baseManager.packageManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class packageDAO extends BaseDao {
    public static Logger logger = Logger.getLogger(packageDAO.class);
    private static final packageDAO dao = new packageDAO();

    private packageDAO() {
    }

    public static final packageDAO getInstance() {
        return dao;
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 包装材料查询实际执行
     * @author : andyzhou
     * @LastDate : 2013-10-20
     */
    public PageResult<Map<String, Object>> getMainList(RequestWrapper req, int pageSize, int curPage) {
        List<Object> params = new LinkedList<Object>();
        StringBuffer sql = new StringBuffer("");
        String spec = CommonUtils.checkNull(req.getParamValue("SPEC"));
        String type = CommonUtils.checkNull(req.getParamValue("TYPE"));
        String name = CommonUtils.checkNull(req.getParamValue("NAME"));

        sql.append("select PACK_ID,PACK_SPEC,PACK_TYPE,PACK_NAME,PACK_UOM,PACK_QTY    \n");
        sql.append("from TT_PART_PACKAGE where STATUS=" + Constant.STATUS_ENABLE + "   \n");
        if (!spec.equals("")) {
            sql.append("and PACK_SPEC like'%" + spec + "%'  \n");
        }
        if (!type.equals("0")) {
            sql.append("and PACK_TYPE=" + type + "  \n");
        }
        if (!name.equals("")) {
            sql.append("and PACK_NAME like'%" + name + "%'  \n");
        }
        sql.append("order by PACK_NAME \n");
        logger.info(sql.toString());
        return dao.pageQuery(sql.toString(), params, getFunName() + System.currentTimeMillis(), pageSize, curPage);
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 包装材料明细
     * @author : andyzhou
     * @LastDate : 2013-10-20
     */
    public Map<String, Object> getViewData(String packageId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select PACK_ID,PACK_SPEC,PACK_TYPE,PACK_NAME,PACK_UOM,PACK_QTY \n");
        sql.append("from TT_PART_PACKAGE  \n");
        sql.append("where PACK_ID=" + packageId + "  \n");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }


    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
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
     * @Title : 配件包装储运维护List
     */
    public List<Map<String, Object>> partPkgExpList() {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT P.PACK_SPEC,\n");
        sql.append("       (SELECT tc.code_desc FROM tc_code tc WHERE tc.code_id=p.pack_type) pack_type,\n");
        sql.append("       P.PACK_NAME,\n");
        sql.append("       P.PACK_UOM,\n");
        sql.append("\t\t\t p.pack_qty,\n");
        sql.append("       ROUND(PM.Y_AVG * 30 - P.PACK_QTY, 2) PLAN_QTY\n");
        sql.append("  FROM (SELECT R.PACK_ID, SUM(R.QTY) / 360 Y_AVG\n");
        sql.append("          FROM TT_PART_PACKAGE_RECORD R\n");
        sql.append("         WHERE R.FLAG = 2\n");
        sql.append("           AND R.STATUS = 10011001\n");
        sql.append("           AND R.CREATE_DATE >= ADD_MONTHS(SYSDATE, -12)\n");
        sql.append("         GROUP BY R.PACK_ID) PM,\n");
        sql.append("       TT_PART_PACKAGE P\n");
        sql.append(" WHERE P.PACK_ID = PM.PACK_ID(+)\n");
        sql.append("   AND ROUND(PM.Y_AVG * 30 - P.PACK_QTY, 2) > 0\n");
        List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 包装材料查询实际执行
     * @author : andyzhou
     * @LastDate : 2013-10-20
     */
    public List<Map<String, Object>> getExportList(RequestWrapper req) {
        List<Object> params = new LinkedList<Object>();
        StringBuffer sql = new StringBuffer("");
        String spec = CommonUtils.checkNull(req.getParamValue("SPEC"));
        String type = CommonUtils.checkNull(req.getParamValue("TYPE"));
        String name = CommonUtils.checkNull(req.getParamValue("NAME"));

        sql.append("select PACK_ID,PACK_SPEC,PACK_TYPE,PACK_NAME,PACK_UOM,PACK_QTY    \n");
        sql.append("from TT_PART_PACKAGE where STATUS=" + Constant.STATUS_ENABLE + "   \n");
        if (!spec.equals("")) {
            sql.append("and PACK_SPEC like'%" + spec + "%'  \n");
        }
        if (!type.equals("0")) {
            sql.append("and PACK_TYPE=" + type + "  \n");
        }
        if (!name.equals("")) {
            sql.append("and PACK_NAME like'%" + name + "%'  \n");
        }
        sql.append("order by PACK_NAME \n");
        logger.info(sql.toString());
        return dao.pageQuery(sql.toString(), params, getFunName() + System.currentTimeMillis());
    }

}
