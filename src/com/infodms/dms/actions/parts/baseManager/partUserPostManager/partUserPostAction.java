package com.infodms.dms.actions.parts.baseManager.partUserPostManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partPlannerWarehouseManager.partPlannerWarehouseDao;
import com.infodms.dms.dao.parts.baseManager.partUserPostManager.partUserPostDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TtPartUserOrdertypeDefinePO;
import com.infodms.dms.po.TtPartUserposeDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理配件人员设置业务
 * @Description:CHANADMS
 * @Copyright: Copyright (c) 2010
 * @Date: 2013-4-10
 * @remark
 */
public class partUserPostAction implements PTConstants {
    public Logger logger = Logger.getLogger(partUserPostAction.class);
    private static final partUserPostDao dao = partUserPostDao.getInstance();

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 跳转至配件人员类型设置页面
     */
    public void partUserPostInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            List<Map<String, Object>> postList = dao.getPostList();
            act.setOutData("postList", postList);
            act.setForword(PART_USER_POST_PAGE);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件人员类型设置初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 按条件查询人员类型信息
     */
    public void partUserPostSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String userName = CommonUtils.checkNull(request
                    .getParamValue("userName")); // 人员名称
            // 人员类型 USER_TYPE 对应 字段 FIX_VALUE
            String fixValue = CommonUtils.checkNull(request
                    .getParamValue("fixValue")); // 人员类型
            String state = CommonUtils
                    .checkNull(request.getParamValue("STATE"));// 是否有效
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartUserPost(
                    userName, fixValue, state, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询人员类型信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 按条件查询订单类型信息
     */
    public void partUserOrderSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String userId = CommonUtils.checkNull(request.getParamValue("userId")); // 人员名称
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartUserOrder(userId, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询人员类型信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 失效配件人员类型关系
     */
    public void celPartUserPost() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String defineId = CommonUtils.checkNull(request
                    .getParamValue("disabeParms"));// 关系ID
            Long userId = logonUser.getUserId();// 操作用户ID

            // TtPartUserposeDefinePO
            TtPartUserposeDefinePO selPO = new TtPartUserposeDefinePO();
            TtPartUserposeDefinePO updatePO = new TtPartUserposeDefinePO();

            selPO.setDefineId(Long.parseLong(defineId));

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
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "失效选中的人员类型关系");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 有效配件人员类型关系
     */
    public void enablePartUserPost() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String defineId = CommonUtils.checkNull(request
                    .getParamValue("enableParms"));// 关系ID
            Long userId = logonUser.getUserId();// 操作用户ID
            partPlannerWarehouseDao dao = partPlannerWarehouseDao.getInstance();

            // TtPartUserposeDefinePO
            TtPartUserposeDefinePO selPO = new TtPartUserposeDefinePO();
            TtPartUserposeDefinePO updatePO = new TtPartUserposeDefinePO();

            selPO.setDefineId(Long.parseLong(defineId));

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
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "有效配件人员类型关系");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-29
     * @Title : 更新配件人员类型关系
     */
    public void updatePartUserPost() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String defineId = CommonUtils.checkNull(request
                    .getParamValue("updateParms"));// 关系ID
            String isLeader = CommonUtils.checkNull(request
                    .getParamValue("isLeader"));// 是否主管
            String isDirect = CommonUtils.checkNull(request
                    .getParamValue("isDirect"));// 是否直发计划人员
            String isChkZy = CommonUtils.checkNull(request
                    .getParamValue("isChkZy"));// 是否审核专员
            Long userId = logonUser.getUserId();// 操作用户ID
            partPlannerWarehouseDao dao = partPlannerWarehouseDao.getInstance();

            // TtPartUserposeDefinePO
            TtPartUserposeDefinePO selPO = new TtPartUserposeDefinePO();
            TtPartUserposeDefinePO updatePO = new TtPartUserposeDefinePO();

            selPO.setDefineId(Long.parseLong(defineId));

            if ("yes".equals(isLeader)) {
                updatePO.setIsLeader(Constant.PART_BASE_FLAG_YES);
            } else {
                updatePO.setIsLeader(Constant.PART_BASE_FLAG_NO);
            }
            if ("yes".equals(isDirect)) {
                updatePO.setIsDirect(Constant.PART_BASE_FLAG_YES);
            } else {
                updatePO.setIsDirect(Constant.PART_BASE_FLAG_NO);
            }
            if ("yes".equals(isChkZy)) {
                updatePO.setIsChkzy(Constant.PART_BASE_FLAG_YES);
            } else {
                updatePO.setIsChkzy(Constant.PART_BASE_FLAG_NO);
            }
            updatePO.setUpdateBy(userId);
            updatePO.setUpdateDate(new Date());

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
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "更新配件人员类型关系");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-29
     * @Title : 删除配件人员订单类型
     */
    public void deleteOrderType() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));// 订单类型
            String userId = CommonUtils.checkNull(request.getParamValue("userId"));// 订单类型
            partPlannerWarehouseDao dao = partPlannerWarehouseDao.getInstance();

            // TtPartUserOrdertypeDefinePO
            TtPartUserOrdertypeDefinePO po = new TtPartUserOrdertypeDefinePO();
            po.setOrderType(Integer.valueOf(orderType));
            po.setUserId(Long.valueOf(userId));
            dao.delete(po);

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "更新配件人员类型关系");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 跳转至配件人员类型修改页面
     */
    public void partUserPostFormodInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        try {
            String uesrType = CommonUtils.checkNull(request.getParamValue("uesrType"));// 人员类型
            String fixName = CommonUtils.checkNull(request.getParamValue("fixName"));// 类型名称

            act.setOutData("fixName", fixName);
            act.setOutData("fixValue", uesrType);
            act.setForword(PART_USER_POST_MOD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件人员类型修改初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 跳转至配件订单类型修改页面
     */
    public void partUserOrderFormodInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        try {
            String userId = CommonUtils.checkNull(request.getParamValue("userId"));// 用户ID
            String userType = CommonUtils.checkNull(request.getParamValue("userType"));// 人员类型
            Map<String, Object> map = dao.queryPartUserInfo(userId, userType);
            act.setOutData("userId", userId);
            act.setOutData("map", map);
            act.setForword(PART_USER_ORDER_MOD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件人员类型修改初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 跳转至配件人员类型新增页面
     */
    public void partUserPostAddInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            List<Map<String, Object>> postList = dao.getPostList();
            act.setOutData("postList", postList);
            act.setForword(PART_USER_POST_ADD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件人员类型新增初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 人员信息弹出框 for Modify
     */
    public void queryUsersInit() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String fixValue = CommonUtils.checkNull(request
                    .getParamValue("fixValue"));
            act.setOutData("fixValue", fixValue);
            act.setForword(MOD_USE_INFO);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "仓库信息初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 订单类型弹出框 for Modify
     */
    public void queryUsersOrderInit() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String userId = request.getParamValue("userId");
            act.setOutData("userId", userId);
            act.setForword(MOD_USE_ORDER_INFO);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "仓库信息初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 人员信息弹出框 for Add
     */
    public void queryUsersForAddInit() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String fixValue = CommonUtils.checkNull(request.getParamValue("fixValue"));
            act.setOutData("fixValue", fixValue);
            act.setForword(ADD_USE_INFO);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "仓库信息初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 返回人员信息
     */
    public void queryUsersDialog() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String name = CommonUtils.checkNull(request.getParamValue("NAME")); // 用户名称
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            String oemId = Constant.OEM_ACTIVITIES;//主机厂ID
            PageResult<Map<String, Object>> ps = dao.getAllUsers(name, oemId,
                    Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询人员信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title : 返回订单类型
     */
    public void queryUsersOrderDialog() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String userId = CommonUtils.checkNull(request.getParamValue("userId")); // 用户名称
            String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 用户名称
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.getAllUsersOrder(orderType, Constant.PAGE_SIZE, curPage);
            act.setOutData("userId", userId);
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询人员信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 新增人员类型关系
     */
    public void insertPartUserPost() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String errorExist = "";
        try {

            String fixValue = CommonUtils.checkNull(request
                    .getParamValue("fixValue"));// 人员类型
            String users = CommonUtils
                    .checkNull(request.getParamValue("users")); //新增User

            String leaderIds = request.getParamValue("leaderIds");
            String directIds = request.getParamValue("directIds");
            String chkZyIds = request.getParamValue("chkZyIds");

            Long userId = logonUser.getUserId();// 用户ID

            // TtPartUserposeDefinePO
            TtPartUserposeDefinePO insertPO = new TtPartUserposeDefinePO();

            String leaderIdsArr[] = null;
            String directIdsArr[] = null;
            String chkZyIdsArr[] = null;

            if (null != leaderIds && !"".equals(leaderIds)) {
                leaderIdsArr = leaderIds.split(",");
            }
            if (null != directIds && !"".equals(directIds)) {
                directIdsArr = directIds.split(",");
            }
            if (null != chkZyIds && !"".equals(chkZyIds)) {
                chkZyIdsArr = chkZyIds.split(",");
            }

            String usersArr[] = users.split(",");
            for (int i = 0; i < usersArr.length; i++) {
                List existlist = dao.getExistPO(fixValue, usersArr[i]
                        .toString());
                if (existlist != null && existlist.size() > 0) {
                    TcUserPO userPO = (TcUserPO) existlist.get(0);
                    errorExist += userPO.getName() + " ";
                }

            }
            if ("".equals(errorExist)) {
                for (int i = 0; i < usersArr.length; i++) {
                    insertPO.setDefineId(Long.parseLong(SequenceManager
                            .getSequence("")));
                    insertPO.setUserType(Long.parseLong(fixValue));
                    insertPO.setUserId(Long.parseLong(usersArr[i]));

                    if (null != leaderIds && !"".equals(leaderIds) && leaderIdsArr[i].equals(usersArr[i])) {
                        insertPO.setIsLeader(Constant.PART_BASE_FLAG_YES);
                    } else {
                        insertPO.setIsLeader(Constant.PART_BASE_FLAG_NO);
                    }
                    if (null != directIds && !"".equals(directIds) && directIdsArr[i].equals(usersArr[i])) {

                        insertPO.setIsDirect(Constant.PART_BASE_FLAG_YES);

                    }
                    if (null != chkZyIds && !"".equals(chkZyIds) && chkZyIdsArr[i].equals(usersArr[i])) {

                        insertPO.setIsChkzy(Constant.PART_BASE_FLAG_YES);

                    }
                    insertPO.setCreateBy(userId);
                    insertPO.setCreateDate(new Date());

                    dao.insert(insertPO);
                }
            }

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("errorExist", errorExist);// 关系记录存在
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "新增计划员与仓库关系");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2014-5-12
     * @Title : 新增订单类型
     */
    public void insertPartUserOrder() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String errorExist = "";
        try {

            String userId = CommonUtils.checkNull(request.getParamValue("userId"));// 人员ID
            String users = CommonUtils.checkNull(request.getParamValue("users")); //新增订单类型
            TcUserPO tcUserPO = new TcUserPO();
            tcUserPO.setUserId(Long.valueOf(userId));
            List<TcUserPO> tcUserList = dao.select(tcUserPO);
            // TtPartUserposeDefinePO
            TtPartUserOrdertypeDefinePO insertPO = new TtPartUserOrdertypeDefinePO();

            String usersArr[] = users.split(",");
            for (int i = 0; i < usersArr.length; i++) {
                List existlist = dao.getExistPO1(userId, usersArr[i]
                        .toString());
                if (existlist != null && existlist.size() > 0) {
                    TtPartUserOrdertypeDefinePO userPO = (TtPartUserOrdertypeDefinePO) existlist.get(0);
                    TcCodePO t = new TcCodePO();
                    t.setCodeId(userPO.getOrderType().toString());
                    List<TcCodePO> tcCodeList = dao.select(t);
                    errorExist += tcCodeList.get(0).getCodeDesc() + " ";
                }

            }
            if ("".equals(errorExist)) {
                for (int i = 0; i < usersArr.length; i++) {
                    insertPO.setDefId(Long.parseLong(SequenceManager.getSequence("")));
                    insertPO.setUserId(Long.parseLong(userId));
                    insertPO.setOrderType(Integer.parseInt(usersArr[i]));

                    insertPO.setCreateBy(logonUser.getUserId());
                    insertPO.setCreateDate(new Date());
                    insertPO.setVer(1);
//					insertPO.setStatus();
                    insertPO.setState(Constant.STATUS_ENABLE);
                    insertPO.setUserType(tcUserList.get(0).getUserType());

                    dao.insert(insertPO);
                }
            }

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("errorExist", errorExist);// 关系记录存在
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "新增订单类型");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

}
