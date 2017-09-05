package com.infodms.dms.actions.parts.baseManager.partPlannerWarehouseManager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partPlannerWarehouseManager.partPlannerWarehouseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartPlanerWhRelationPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理计划员仓库维护业务
 * @Description:CHANADMS
 * @Copyright: Copyright (c) 2010
 * @Date: 2013-4-10
 * @remark
 */
public class partPlannerWarehouseAction implements PTConstants {
    public Logger logger = Logger.getLogger(partPlannerWarehouseAction.class);

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 跳转计划员仓库维护页面
     */
    public void partPlannerWarehouseInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            act.setForword(PART_PLANNER_WAREHOUSE_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "计划员仓库维护初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 按条件查询计划员与仓库信息
     */
    public void partPlannerWarehouseSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String plannerName = CommonUtils.checkNull(request
                    .getParamValue("plannerName")); // 计划员
            String WHName = CommonUtils.checkNull(request
                    .getParamValue("WHName")); // 仓库名称
            String state = CommonUtils.checkNull(request
                    .getParamValue("STATE"));// 是否有效
            partPlannerWarehouseDao dao = partPlannerWarehouseDao.getInstance();
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartPlannerWarehouse(
                    plannerName, WHName, state, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询计划员与仓库信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 失效计划员与仓库关系
     */
    public void celPartPlannerWarehouse() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String relationId = CommonUtils.checkNull(request.getParamValue("disabeParms"));//关系ID
            Long userId = logonUser.getUserId();//操作用户ID
            partPlannerWarehouseDao dao = partPlannerWarehouseDao.getInstance();

            // TtPartPlanerWhRelationPO
            TtPartPlanerWhRelationPO selPO = new TtPartPlanerWhRelationPO();
            TtPartPlanerWhRelationPO updatePO = new TtPartPlanerWhRelationPO();

            selPO.setRelationId(Long.parseLong(relationId));

            updatePO.setUpdateBy(userId);
            updatePO.setUpdateDate(new Date());
            updatePO.setDisableBy(userId);
            updatePO.setDisableDate(new Date());
            updatePO.setState(Constant.STATUS_DISABLE);

            dao.update(selPO, updatePO);

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "失效选中的设计");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 有效计划员与仓库关系
     */
    public void enablePartPlannerWarehouse() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String relationId = CommonUtils.checkNull(request.getParamValue("enableParms"));//关系ID
            Long userId = logonUser.getUserId();//操作用户ID
            partPlannerWarehouseDao dao = partPlannerWarehouseDao.getInstance();

            // TtPartPlanerWhRelationPO
            TtPartPlanerWhRelationPO selPO = new TtPartPlanerWhRelationPO();
            TtPartPlanerWhRelationPO updatePO = new TtPartPlanerWhRelationPO();

            selPO.setRelationId(Long.parseLong(relationId));

            updatePO.setUpdateBy(userId);
            updatePO.setUpdateDate(new Date());
            updatePO.setState(Constant.STATUS_ENABLE);

            dao.update(selPO, updatePO);

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "有效选中的设计");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 跳转至计划员与仓库修改页面
     */
    public void partPlannerWarehouseFormodInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        try {
            String plannerID = CommonUtils.checkNull(request.getParamValue("plannerID"));//计划员ID
            String plannerName = CommonUtils.checkNull(request.getParamValue("plannerName"));//计划员姓名

            act.setOutData("plannerID", plannerID);
            act.setOutData("plannerName", plannerName);
            act.setForword(PART_PLANNER_WAREHOUSE_MOD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "计划员与仓库修改初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 跳转至计划员与仓库新增页面
     */
    public void partPlannerWarehouseAddInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        try {
            act.setForword(PART_PLANNER_WAREHOUSE_ADD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "计划员与仓库新增初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 返回计划员信息
     */
    public void partPlannerSelect() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer sb = new StringBuffer();
        try {
            RequestWrapper request = act.getRequest();
            act.getResponse().setContentType("application/json");
            if ("1".equals(request.getParamValue("query"))) { // 开始查询
                String plannerName = request.getParamValue("plannerName");// 用户名
                String userPost = "计划员";//人员类型：计划员

                if (Utility.testString(plannerName)) {
                    sb.append(" and U.NAME LIKE '%" + plannerName + "%' \n");
                }
                sb.append(" AND PF.FIX_NAME ='" + userPost + "' \n");
                Integer curPage = request.getParamValue("curPage") != null ? Integer
                        .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
                partPlannerWarehouseDao dao = partPlannerWarehouseDao.getInstance();
                PageResult<Map<String, Object>> ps = dao.getAllPlanner(Constant.PAGE_SIZE, curPage, sb.toString());
                act.setOutData("ps", ps);
            } else {
                act.setForword(PLANNER_INFO);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "返回计划员信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 返回选中计划员的所有仓库信息
     */
    public void partSinglePlannerWarehouseSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String plannerID = CommonUtils.checkNull(request
                    .getParamValue("plannerID")); // 计划员
            String whName = CommonUtils.checkNull(request
                    .getParamValue("WHName")); // 仓库名称
            String state = CommonUtils.checkNull(request
                    .getParamValue("STATE"));// 是否有效

            partPlannerWarehouseDao dao = partPlannerWarehouseDao.getInstance();
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartSinglePlannerWarehouse(
                    plannerID, whName, state, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询计划员与仓库信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 仓库信息弹出框 for Modify
     */
    public void queryWarehouseInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String plannerID = CommonUtils.checkNull(request
                    .getParamValue("plannerID"));
            act.setOutData("plannerID", plannerID);
            act.setForword(WAREHOUSE_INFO);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "仓库信息初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 仓库信息弹出框 for Add
     */
    public void queryWarehousefoAddInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(WAREHOUSE_Add_INFO);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "仓库信息初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 返回所有仓库信息
     */
    public void queryWarehouseDialog() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;
            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();
            }
            String whName = CommonUtils.checkNull(request.getParamValue("WH_NAME")); //仓库名称
            partPlannerWarehouseDao dao = partPlannerWarehouseDao.getInstance();
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

            StringBuffer sbStr = new StringBuffer();
            if (null != whName && !"".equals(whName)) {
                sbStr.append(" AND W.WH_NAME like '%" + whName + "%'\n");
            }
            sbStr.append(" AND W.ORG_ID = '" + parentOrgId + "'");

            PageResult<Map<String, Object>> ps = dao.getAllWarehouse(sbStr.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询计划员与仓库信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 新增计划员与仓库关系
     */
    public void insertPartPlannerWarehouse() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String errorExist = "";
        try {

            String plannerID = CommonUtils.checkNull(request.getParamValue("plannerID"));//计划员ID
            String whIds = CommonUtils.checkNull(request.getParamValue("whIds"));
            String remark = request.getParamValue("remark");

            Long userId = logonUser.getUserId();//用户ID

            partPlannerWarehouseDao dao = partPlannerWarehouseDao.getInstance();
            // TtPartPlanerWhRelationPO
            TtPartPlanerWhRelationPO insertPO = new TtPartPlanerWhRelationPO();

            String whIdsArr[] = whIds.split(",");
            for (int i = 0; i < whIdsArr.length; i++) {
                List<TtPartWarehouseDefinePO> existlist = dao.getExistPO(plannerID, whIdsArr[i]);
                if (existlist != null && existlist.size() > 0) {
                    TtPartWarehouseDefinePO tpwPO = (TtPartWarehouseDefinePO) existlist.get(0);
                    errorExist += tpwPO.getWhName() + " ";
                }

            }
            if ("".equals(errorExist)) {
                for (int i = 0; i < whIdsArr.length; i++) {
                    insertPO.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
                    insertPO.setPlanerId(Long.parseLong(plannerID));
                    insertPO.setWhId(Long.parseLong(whIdsArr[i]));
                    insertPO.setCreateBy(userId);
                    insertPO.setCreateDate(new Date());
                    insertPO.setRemark(remark);

                    dao.insert(insertPO);
                }
            }


            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("errorExist", errorExist);// 计划员与仓库关系记录存在
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "新增计划员与仓库关系");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

}
