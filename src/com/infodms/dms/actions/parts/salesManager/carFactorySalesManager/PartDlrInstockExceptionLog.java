package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.salesManager.PartDlrInstockExceptionDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartInstockExceptionLogPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.POContext;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>ClassName: PartDlrInstockExceptionLog</p>
 * <p>Description: 配件入库异常</p>
 * <p>Author: MEpaper</p>
 * <p>Date: 2017年8月23日</p>
 */
@SuppressWarnings("unchecked")
public class PartDlrInstockExceptionLog extends BaseImport implements PTConstants {
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);

    private static final String mainUrl = "/jsp/parts/salesManager/carFactorySalesManager/partInstockExceptionMain.jsp";
    private static final String detailUrl = "/jsp/parts/salesManager/carFactorySalesManager/partInstockExceptionDetail.jsp";


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 初始化
     */
    public void partDlrInstockExceptionLogInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setForword(mainUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "入库初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * <p>
     * Description: 入库异常长袖
     * </p>
     */
    public void partDlrInstockExceptionLogQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrInstockExceptionDao dao = PartDlrInstockExceptionDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryExceptionOrder(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "异常数据查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * <p>
     * Description: 入库明细
     * </p>
     */
    public void detail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrInstockExceptionDao dao = PartDlrInstockExceptionDao.getInstance();
            String inId = request.getParamValue("inId");
            String state = request.getParamValue("state");
            dao.getDetailList(inId);

            List<Map<String, Object>> list = dao.getDetailList(inId);
            act.setOutData("state", state);
            act.setOutData("list", list);
            act.setForword(detailUrl);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "异常数据查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * <p>
     * Description: 回复
     * </p>
     */
    public void reply() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrInstockExceptionDao dao = PartDlrInstockExceptionDao.getInstance();
        try {
            String[] id = request.getParamValues("ck");

            for (int i = 0; i < id.length; i++) {
                String remark = CommonUtils.checkNull(request.getParamValue("reply_" + id[i]));
                TtPartInstockExceptionLogPO newPo = new TtPartInstockExceptionLogPO();
                newPo.setExceptionId(Long.valueOf(id[i]));
                newPo.setOemRemark(remark);
                newPo.setState(Constant.INSTOCK_EXCEPTION_REPLY_02);
                newPo.setReplyBy(loginUser.getUserId());
                newPo.setReplyDate(new Date());
                TtPartInstockExceptionLogPO oldPo = new TtPartInstockExceptionLogPO();
                oldPo.setExceptionId(Long.valueOf(id[i]));
                dao.update(oldPo, newPo);
            }
            POContext.endTxn(true);
            act.setOutData("success", "回复成功!");
        } catch (Exception e) {
            POContext.endTxn(false);
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "回复失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * <p>
     * Description: 关闭入库异常
     * </p>
     */
    public void closeException() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrInstockExceptionDao dao = PartDlrInstockExceptionDao.getInstance();
        try {
            String inId = CommonUtils.checkNull(request.getParamValue("inId"));
            TtPartInstockExceptionLogPO newPo = new TtPartInstockExceptionLogPO();
            newPo.setInId(Long.valueOf(inId));
            newPo.setState(Constant.INSTOCK_EXCEPTION_REPLY_03);
            newPo.setCloseBy(loginUser.getUserId());
            newPo.setCloseDate(new Date());
            TtPartInstockExceptionLogPO oldPo = new TtPartInstockExceptionLogPO();
            oldPo.setInId(Long.valueOf(inId));
            dao.update(oldPo, newPo);
            POContext.endTxn(true);
            act.setOutData("success", "关闭成功!");
        } catch (Exception e) {
            POContext.endTxn(false);
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "关闭失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }
}
