package com.infodms.dms.actions.parts.baseManager.partsPlanManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partsPlanManager.avgSaleParmsSetDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartPlanweightDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理平均销量权重维护业务
 * @Description:CHANADMS
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2013-4-2
 * @remark
 */
public class avgSaleParmsSetAction {
    public Logger logger = Logger.getLogger(avgSaleParmsSetAction.class);
    private static final avgSaleParmsSetDao dao = avgSaleParmsSetDao.getInstance();

    //计划分类设置 页面
    private static final String PART_PLAN_SORT = "/jsp/parts/baseManager/partsPlanManager/avgSaleParmsSet/avgSaleParmsSetPg.jsp";

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-5
     * @Title : 跳转至平均销量权重维护页面
     */
    public void avgSaleParmsSetInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<Map<String, Object>> list = dao.queryAvgSaleParms();
            String deftId = "";
            String threeWeight = "";
            String sixWeight = "";
            String twelveWeight = "";
            if (null != list && list.size() == 1) {
                deftId = list.get(0).get("DEFT_ID").toString();
                threeWeight = list.get(0).get("THREE_WEIGHT").toString();
                sixWeight = list.get(0).get("SIX_WEIGHT").toString();
                twelveWeight = list.get(0).get("TWELVE_WEIGHT").toString();
            }
            act.setOutData("deftId", deftId);
            act.setOutData("threeWeight", threeWeight);
            act.setOutData("sixWeight", sixWeight);
            act.setOutData("twelveWeight", twelveWeight);

            act.setForword(PART_PLAN_SORT);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "平均销量权重维护维护初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-5
     * @Title : 保存平均销量权重设置
     */
    public void saveAvgSaleParmsSet() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            Long userId = logonUser.getUserId(); //修改人ID
            Date date = new Date();
            String deftId = CommonUtils.checkNull(req.getParamValue("deftId")); //序列ID
            String threeWeight = CommonUtils.checkNull(req.getParamValue("threeWeight")); //3个月比重
            String sixWeight = CommonUtils.checkNull(req.getParamValue("sixWeight")); //6个月比重
            String twelveWeight = CommonUtils.checkNull(req.getParamValue("twelveWeight")); //12个月比重


            List<Map<String, Object>> list = dao.queryAvgSaleParms();

            if (null != list && list.size() == 1) {
                //更新
                TtPartPlanweightDefinePO selPo = new TtPartPlanweightDefinePO();
                TtPartPlanweightDefinePO updPo = new TtPartPlanweightDefinePO();

                selPo.setDeftId(Long.parseLong(deftId));

                updPo.setThreeWeight(Double.parseDouble(threeWeight));
                updPo.setSixWeight(Double.parseDouble(sixWeight));
                updPo.setTwelveWeight(Double.parseDouble(twelveWeight));
                updPo.setUpdateBy(userId);
                updPo.setUpdateDate(date);

                dao.update(selPo, updPo);
            } else {
                //插入
                TtPartPlanweightDefinePO insPo = new TtPartPlanweightDefinePO();

                insPo.setDeftId(Long.parseLong(SequenceManager.getSequence("")));
                insPo.setThreeWeight(Double.parseDouble(threeWeight));
                insPo.setSixWeight(Double.parseDouble(sixWeight));
                insPo.setTwelveWeight(Double.parseDouble(twelveWeight));
                insPo.setCreateBy(userId);
                insPo.setCreateDate(date);
                insPo.setUpdateBy(userId);
                insPo.setUpdateDate(date);

                dao.insert(insPo);
            }
            act.setOutData("success", "true");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存平均销量权重设置信息失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("errorExist", "保存失败!");
        }
    }
}
