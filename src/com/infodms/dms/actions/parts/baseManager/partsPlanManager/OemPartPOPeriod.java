package com.infodms.dms.actions.parts.baseManager.partsPlanManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partsPlanManager.oemPartPOPeriodDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Yu
 * Date: 13-7-7
 * Time: 下午2:26
 * To change this template use File | Settings | File Templates.
 */
public class OemPartPOPeriod {
    private static oemPartPOPeriodDao dao = oemPartPOPeriodDao.getInstance();
    //计划分类设置 页面
    private static final String PART_PLAN_SORT = "/jsp/parts/baseManager/partsPlanManager/oempoperiod/oempoperiod.jsp";
    public Logger logger = Logger.getLogger(partPlanSortAction.class);

    /*
    *       初始化
    * 、
     * */
    public void oemPartPOPeriodInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            List<Map<String, Object>> pslist = dao.queryoemPartPOPeriod();

            act.setOutData("DEF_ID", pslist.get(0).get("DEF_ID"));
            act.setOutData("DAYS", pslist.get(0).get("DAYS"));
            act.setOutData("TYPE", pslist.get(0).get("PERIOD_TYPE"));
            act.setForword(PART_PLAN_SORT);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "计划分类设置维护初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /*
     *       初始化
     * 、
      * */
    public void oemRepartPOPeriodInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            List<Map<String, Object>> pslist = dao.queryoemRepartPOPeriod();

            act.setOutData("TYPE", pslist.get(0).get("PERIOD_TYPE"));
            act.setOutData("DEF_ID", pslist.get(0).get("DEF_ID"));
            act.setOutData("DAYS", pslist.get(0).get("DAYS"));
            act.setForword(PART_PLAN_SORT);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "计划分类设置维护初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void saveoemPartPOPeriod() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        try {
            String defId = req.getParamValue("deftId");
            String days = req.getParamValue("days");
            ArrayList al = new ArrayList();
            al.add(0, days);
            al.add(1, defId);
            String sql = "UPDATE TT_PART_PERIOD_DEFINE d SET d.days= ? WHERE d.def_id=?";
            dao.update(sql, al);
            act.setOutData("success", "true");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "修改订单有效期失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("errorExist", "修改失败!");
        }
    }
}
