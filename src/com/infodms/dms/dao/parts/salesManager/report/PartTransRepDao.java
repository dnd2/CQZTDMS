package com.infodms.dms.dao.parts.salesManager.report;

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

public class PartTransRepDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartTransRepDao.class);
    private static final PartTransRepDao dao = new PartTransRepDao();

    private PartTransRepDao() {
    }

    public static final PartTransRepDao getInstance() {
        return dao;
    }

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
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
    public PageResult<Map<String, Object>> queryRep(RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        StringBuffer sql = new StringBuffer();
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));
        String transStartDate = CommonUtils.checkNull(request.getParamValue("transStartDate"));
        String transEndDate = CommonUtils.checkNull(request.getParamValue("transEndDate"));
        String subStartDate = CommonUtils.checkNull(request.getParamValue("subStartDate"));
        String subEndDate = CommonUtils.checkNull(request.getParamValue("subEndDate"));
        String finStartDate = CommonUtils.checkNull(request.getParamValue("finStartDate"));
        String finEndDate = CommonUtils.checkNull(request.getParamValue("finEndDate"));
        String dealerId = "";

        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";

        }
        sql.append(" select a.*,b.submit_date,c.FCAUDIT_DATE,d.wh_code,d.wh_name,c.pick_order_id, e.tv_name fix_name from tt_part_trans a");
        sql.append(" left join tt_part_dlr_order_main b on a.order_id = b.order_id ");
        sql.append(" left join tt_part_so_main c on a.so_id=c.so_id	");
        sql.append(" left join tt_part_warehouse_define d on d.wh_id=c.wh_id ");
//        sql.append(" left join tt_part_fixcode_define e on e.fix_gouptype='").append(Constant.FIXCODE_TYPE_04).append("' and fix_value=c.trans_type ");
        sql.append(" left join tt_transport_valuation e  on e.tv_id = a.trans_type ");
        sql.append(" where 1=1 ");

        if (!"".equals(subStartDate)) {
            sql.append(" and b.SUBMIT_DATE>= to_date('").append(subStartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(subEndDate)) {
            sql.append(" and b.SUBMIT_DATE<= to_date('").append(subEndDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(finStartDate)) {
            sql.append(" and c.FCAUDIT_DATE>= to_date('").append(finStartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(finEndDate)) {
            sql.append(" and c.FCAUDIT_DATE<= to_date('").append(finEndDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(transStartDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(transStartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(transEndDate)) {
            sql.append(" and a.CREATE_DATE<= to_date('").append(transEndDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(whId)) {
            sql.append(" and d.wh_id='").append(whId).append("'");
        }

        sql.append(" order by  a.CREATE_DATE desc ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }
}
