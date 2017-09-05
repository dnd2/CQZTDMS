package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.salesManager.PartPkgDao;
import com.infodms.dms.dao.parts.salesManager.PartPkgRePrintDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtPartAddrDefinePO;
import com.infodms.dms.po.TtPartDlrPkgnoPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import org.apache.log4j.Logger;

import java.util.*;

public class PartPkgRePrint {
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);
    public String PART_PKG_PRINT_URL = "/jsp/parts/salesManager/carFactorySalesManager/partPkgRePrint.jsp";
    public String PRINT_URL = "/jsp/parts/salesManager/carFactorySalesManager/PartPrint.jsp";
    public String DLR_PKGNO_URL = "/jsp/parts/salesManager/carFactorySalesManager/partDlrPkgQuery.jsp";
    public String PART_CONFIG_URL = "/jsp/parts/salesManager/carFactorySalesManager/partPkgRePrintConfig.jsp";
    public void init() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(PART_PKG_PRINT_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "补打初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void dlrPKginit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
            act.setOutData("dealerId", dealerId);
            act.setForword(DLR_PKGNO_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询服务商有效箱号错误!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }
    
	public void partConfig() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPkgRePrintDao dao = PartPkgRePrintDao.getInstance();
        try {
            List<Map<String, Object>> list = dao.getPaerRep(request);
            act.setOutData("list", list.get(0));
            act.setForword(PART_CONFIG_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询服务商有效箱号错误!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void query() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartPkgRePrintDao dao = PartPkgRePrintDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryRePrint(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "补打查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void dlrPkgNoquery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartPkgRePrintDao dao = PartPkgRePrintDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryDlrPkgNo(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void existCheck() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartPkgRePrintDao dao = PartPkgRePrintDao.getInstance();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            list = dao.getPaerRep(request);
            if (list.size() == 0) {
                act.setOutData("error", "你输入的装箱单号不存在");
            } else {
                act.setOutData("list", list);
            }

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "打印失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void printQxt() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPkgRePrintDao daop = PartPkgRePrintDao.getInstance();
        try {
            int start = Integer.valueOf(CommonUtils.checkNull(request.getParamValue("start")));
            int end = Integer.valueOf(CommonUtils.checkNull(request.getParamValue("end")));
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));//订货单位ID

            String orgId = "";
            if (null == loginUser.getDealerId()) {
                orgId = loginUser.getOrgId() + "";
            } else {
                orgId = loginUser.getDealerId();
            }

            //获取系统年、月
            Calendar cal = Calendar.getInstance();
            String month = cal.get(Calendar.MONTH) + 1 + "";
            String mothFm = "";
            if (month.length() > 1) {
                mothFm = cal.get(Calendar.MONTH) + 1 + "";
            } else {
                mothFm = "0" + (cal.get(Calendar.MONTH) + 1);
            }
            String year = cal.get(Calendar.YEAR) + "";
            year = year.substring(year.length() - 2, year.length());

            int printNum = end - start;//打印张数

            //发运标签 箱号规则 YY+MM+5位数字 如：140300001
            PartPkgRePrintDao pkgPrtDao = PartPkgRePrintDao.getInstance();

            List<Map<String, Object>> listPrtRcd = pkgPrtDao.getPrintNum(year, month, orgId);

            int rcdNum = 0;
            if (null != listPrtRcd && listPrtRcd.size() > 0) {
                rcdNum = Integer.parseInt(listPrtRcd.get(0).get("RCD_COUNT").toString());
            }

            List<Map<String, Object>> pkgList = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<String, Object>();
            List<TtPartDlrPkgnoPO> list = new ArrayList<TtPartDlrPkgnoPO>();
            for (int i = 1; i <= printNum; i++) {
                String pkgNo = "";
                int pkgNoNum = rcdNum + i;
                TtPartDlrPkgnoPO pkgnoPO = new TtPartDlrPkgnoPO();
                if (pkgNoNum > 999) {
                    pkgNo = year + mothFm + pkgNoNum;
                } else if (pkgNoNum > 99) {
                    pkgNo = year + mothFm + "0" + pkgNoNum;
                } else if (pkgNoNum > 9) {
                    pkgNo = year + mothFm + "00" + pkgNoNum;
                } else {
                    pkgNo = year + mothFm + "000" + pkgNoNum;
                }
                pkgnoPO.setPkgnoId(Long.valueOf(OrderCodeManager.getSequence("")));
                pkgnoPO.setPkgNo(pkgNo);
                pkgnoPO.setDealerId(Long.valueOf(dealerId));
                pkgnoPO.setCreateBy(loginUser.getUserId());
                pkgnoPO.setOrgId(Long.valueOf(orgId));

                map.put("pkgNo", pkgNo);
                map.put("flag", true);
                pkgList.add(map);
                list.add(pkgnoPO);
                map = new HashMap<String, Object>();
            }

            List<Map<String, Object>> listprp = daop.getPaerRep(request);
            act.setOutData("listprp", listprp.get(0));

            act.setOutData("list", pkgList);
            pkgPrtDao.updateOutmarkRecord(year, month, orgId, printNum + "");//更新打印箱号
            pkgPrtDao.insert(list);
            act.setForword(PRINT_URL);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱单数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }
}
