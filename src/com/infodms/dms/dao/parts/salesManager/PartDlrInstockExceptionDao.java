package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartDlrInstockExceptionDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartDlrInstockExceptionDao.class);

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    private static final PartDlrInstockExceptionDao dao = new PartDlrInstockExceptionDao();

    private PartDlrInstockExceptionDao() {
    }

    public static final PartDlrInstockExceptionDao getInstance() {
        return dao;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询
     */
    public PageResult<Map<String, Object>> queryExceptionOrder(RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
        String inCode = CommonUtils.checkNull(request.getParamValue("inCode"));
        String state = CommonUtils.checkNull(request.getParamValue("state"));
        String startDate = CommonUtils.checkNull(request.getParamValue("EstartDate"));//提交时间 开始
        String endDate = CommonUtils.checkNull(request.getParamValue("EendDate"));//提交时间 结束

        String orgId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            orgId = beanList.get(0).getOrgId() + "";
        }
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT A.IN_ID,\n");
        sql.append("       TD.DEALER_CODE,\n");
        sql.append("       TD.DEALER_NAME,\n");
        sql.append("       OT.TRPLAN_CODE,\n");
        sql.append("       SUM(EXCEPTION_NUM) AS EXCEPTION_NUM,\n");
        sql.append("       A.STATE,\n");
        sql.append("       A.CREATE_DATE,\n");
        sql.append("       (SELECT NAME FROM TC_USER B WHERE B.USER_ID = A.CREATE_BY) AS CREATE_BY_NAME\n");
        sql.append("  FROM TT_PART_INSTOCK_EXCEPTION_LOG A,\n");
        sql.append("       TT_PART_DLR_INSTOCK_MAIN      IM,\n");
        sql.append("       VW_PART_ORDER_TRANS           OT,\n");
        sql.append("       TM_DEALER                     TD\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND A.IN_ID = IM.IN_ID\n");
        sql.append("   AND IM.IN_ID = OT.IN_ID\n");
        sql.append("   AND IM.DEALER_ID = TD.DEALER_ID\n");
        if (!"".equals(startDate) && null != startDate) {
            sql.append("       AND A.CREATE_DATE >= TO_DATE('" + startDate + " 00:00:00', 'YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!"".equals(endDate) && null != endDate) {
            sql.append("   AND A.CREATE_DATE <=\n");
            sql.append("       TO_DATE('" + endDate + " 23:59:59', 'YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!"".equals(state) && null != state) {
            sql.append("   AND A.STATE =" + state + "");
        }
        sql.append(" GROUP BY OT.TRPLAN_CODE,\n");
        sql.append("          A.IN_ID,\n");
        sql.append("          A.STATE,\n");
        sql.append("          A.CREATE_DATE,\n");
        sql.append("          A.CREATE_BY,\n");
        sql.append("          TD.DEALER_CODE,\n");
        sql.append("          TD.DEALER_NAME\n");
        sql.append(" ORDER BY A.CREATE_DATE DESC");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }


    public List<Map<String, Object>> getDetailList(String inId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.*,b.PART_OLDCODE,b.PART_CNAME  from tt_part_INSTOCK_EXCEPTION_LOG a");
        sql.append(" join tt_part_dlr_instock_dtl b on a.in_id=b.in_id and a.part_id=b.part_id ");
        sql.append(" where 1=1 ");
        sql.append(" and a.in_id='").append(inId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

}
