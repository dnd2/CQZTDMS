package com.infodms.dms.actions.parts.baseManager.partsPlanManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partsPlanManager.partPlanSortDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartPalnsortDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理设计变更维护业务
 * @Description:CHANADMS
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2013-4-2
 * @remark
 */
public class partPlanSortAction {
    public Logger logger = Logger.getLogger(partPlanSortAction.class);
    private static final partPlanSortDao dao = partPlanSortDao.getInstance();

    //计划分类设置 页面
    private static final String PART_PLAN_SORT = "/jsp/parts/baseManager/partsPlanManager/partPlanSort/partPlanSortPg.jsp";

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-5
     * @Title : 跳转至计划分类设置页面
     */
    public void partPlanSortInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<Map<String, Object>> pslist = dao.queryPartPlanSort();

            act.setOutData("pslist", pslist);
            act.setForword(PART_PLAN_SORT);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "计划分类设置维护初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-5
     * @Title : 保存计划分类设置信息
     */
    public void savePlanSortInfos() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            String sortIds = req.getParamValue("ids");
            String srotIdArr[] = null;
            if (null != sortIds && !"".equals(sortIds)) {
                srotIdArr = sortIds.trim().split(",");
            }

            TtPartPalnsortDefinePO srcPo = null;
            TtPartPalnsortDefinePO updatePo = null;

            if (null != srotIdArr) {
                for (int i = 0; i < srotIdArr.length; i++) {
                    srcPo = new TtPartPalnsortDefinePO();
                    updatePo = new TtPartPalnsortDefinePO();

                    String sortId = srotIdArr[i];
                    String sortType = CommonUtils.checkNull(req.getParamValue("SORT_TYPE" + sortId));//计划类型
                    String saftyRate = CommonUtils.checkNull(req.getParamValue("SAFTY_RATE" + sortId)); //安全系数
                    String saftyCycle = CommonUtils.checkNull(req.getParamValue("SAFTY_CYCLE" + sortId)); //安全周期

                    srcPo.setSortType(sortType);
                    updatePo.setSaftyRate(Float.valueOf(saftyRate));
                    updatePo.setSaftyCycle(Integer.valueOf(saftyCycle));
                    updatePo.setUpdateBy(logonUser.getUserId());
                    updatePo.setUpdateDate(new Date());

                    dao.update(srcPo, updatePo);
                }
            }
            act.setOutData("success", "true");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存计划分类设置信息失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("errorExist", "保存失败!");
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-5
     * @Title : 修改计划分类设置信息
     */
    public void updatePlanSortInfos() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            Long userId = logonUser.getUserId(); //修改人ID
            Date date = new Date();
            String sortId = req.getParamValue("sortId");
            String opType = req.getParamValue("opType");


            TtPartPalnsortDefinePO selPo = new TtPartPalnsortDefinePO();
            TtPartPalnsortDefinePO updPo = new TtPartPalnsortDefinePO();

            selPo.setSortId(Long.parseLong(sortId));

            updPo.setUpdateBy(userId);
            updPo.setUpdateDate(date);
            if ("disable".equals(opType)) {
                updPo.setState(Constant.STATUS_DISABLE);
            } else {
                updPo.setState(Constant.STATUS_ENABLE);
            }

            dao.update(selPo, updPo);
            act.setOutData("success", "true");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "修改计划分类设置信息失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("errorExist", "保存失败!");
        }
    }

}
