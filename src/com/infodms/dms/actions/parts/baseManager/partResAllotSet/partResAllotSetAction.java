package com.infodms.dms.actions.parts.baseManager.partResAllotSet;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partPlannerWarehouseManager.partPlannerWarehouseDao;
import com.infodms.dms.dao.parts.baseManager.partResAllotSet.partResAllotSetDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartResourceallotDefinePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理配件资源分配设置业务
 * @Description:CHANADMS
 * @Copyright: Copyright (c) 2010
 * @Date: 2013-7-6
 * @remark
 */
public class partResAllotSetAction {
    public Logger logger = Logger.getLogger(partResAllotSetAction.class);
    private static final partResAllotSetDao dao = partResAllotSetDao.getInstance();

    private static final String PART_STO_SET_MAIN = "/jsp/parts/baseManager/partResAllotSet/partResAllotSetMain.jsp";//配件资源分配设置首页

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 跳转至配件资源分配设置页面
     */
    public void partResAllotSetInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            act.setForword(PART_STO_SET_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件资源分配设置初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 按条件查询配件直发条件信息
     */
    public void partResAllotSetSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode")); // 配件件号
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("partName")); // 配件名称
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));// 是否有效

            StringBuffer sql = new StringBuffer();

            if (null != partCode && !"".equals(partCode)) {
                sql.append(" AND UPPER(PD.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
            }

            if (null != partOldcode && !"".equals(partOldcode)) {
                sql.append(" AND UPPER(PD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
            }
            if (null != partName && !"".equals(partName)) {
                sql.append(" AND PD.PART_NAME LIKE '%" + partName + "%' ");
            }

            if (null != state && !"".equals(state)) {
                sql.append(" AND RD.STATE = '" + state + "' ");
            }
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = dao.queryPartResAllotSet(sql.toString(), Constant.PAGE_SIZE, curPage);

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
     * @throws : LastDate : 2013-7-8
     * @Title : 失效配件资源分配设置
     */
    public void celPartResAllotSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String defineId = CommonUtils.checkNull(request.getParamValue("disabeParms"));// 关系ID
            Long userId = logonUser.getUserId();// 操作用户ID

            //TtPartResourceallotDefinePO
            TtPartResourceallotDefinePO selPO = new TtPartResourceallotDefinePO();
            TtPartResourceallotDefinePO updatePO = new TtPartResourceallotDefinePO();

            selPO.setDefId(Long.parseLong(defineId));

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
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "失效配件资源分配设置失败 ");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 有效配件资源分配设置
     */
    public void enablePartResAllotSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String defineId = CommonUtils.checkNull(request.getParamValue("enableParms"));// 关系ID
            Long userId = logonUser.getUserId();// 操作用户ID
            partPlannerWarehouseDao dao = partPlannerWarehouseDao.getInstance();

            //TtPartResourceallotDefinePO
            TtPartResourceallotDefinePO selPO = new TtPartResourceallotDefinePO();
            TtPartResourceallotDefinePO updatePO = new TtPartResourceallotDefinePO();

            selPO.setDefId(Long.parseLong(defineId));

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
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "有效配件资源分配设置失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-6
     * @Title : 更新配件资源分配设置
     */
    public void updatePartResAllotSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            Long userId = logonUser.getUserId();// 操作用户ID
            Date date = new Date();
            String defId = request.getParamValue("defId"); // 序列ID
            String partId = CommonUtils.checkNull(request.getParamValue("partId")); // 配件ID
            String allotRatio = request.getParamValue("allotRatio"); // 分配比例
            String allotNum = request.getParamValue("allotNum"); // 分配数量
            String boToSales = CommonUtils.checkNull(request.getParamValue("boToSales")); // 是否允许BO转销售

            if (null != defId && !"".equals(defId) && !"null".equals(defId)) {
                //TtPartResourceallotDefinePO
                TtPartResourceallotDefinePO selPO = new TtPartResourceallotDefinePO();
                TtPartResourceallotDefinePO updatePO = new TtPartResourceallotDefinePO();


                selPO.setDefId(Long.parseLong(defId));

                updatePO.setAllotRatio(Float.parseFloat(allotRatio));
                updatePO.setAllotNum(Integer.parseInt(allotNum));
                updatePO.setBoTosales(Integer.parseInt(boToSales));
                updatePO.setUpdateBy(userId);
                updatePO.setUpdateDate(date);

                dao.update(selPO, updatePO);
            } else {
                String sqlStr = " AND PD.PART_ID = '" + partId + "' ";
                List<Map<String, Object>> partList = dao.getPartList(sqlStr);
                String partCode = partList.get(0).get("PART_CODE").toString();
                String partOldcode = partList.get(0).get("PART_OLDCODE").toString();
                String partName = partList.get(0).get("PART_CNAME").toString();

                TtPartResourceallotDefinePO insPo = new TtPartResourceallotDefinePO();

                insPo.setDefId(Long.parseLong(SequenceManager.getSequence("")));
                insPo.setPartId(Long.parseLong(partId));
                insPo.setPartCode(partCode);
                insPo.setPartOldcode(partOldcode);
                insPo.setPartName(partName);
                //add by yuan 20130707
                if (allotRatio != null && allotRatio != "" && allotRatio != "null" && allotRatio != "undefined") {
                    insPo.setAllotRatio(Float.parseFloat(allotRatio));
                }
                if (allotNum != null && allotNum != "" && allotNum != "null" && allotNum != "undefined") {
                    insPo.setAllotNum(Integer.parseInt(allotNum));
                }
                insPo.setBoTosales(Integer.parseInt(boToSales));
                insPo.setState(Constant.STATUS_ENABLE);
                insPo.setCreateBy(userId);
                insPo.setCreateDate(date);

                dao.insert(insPo);
            }

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "更新配件分配设置");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-6
     * @Title : 配件资源分配设置 导出
     */
    public void exportPartResAllotExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode")); // 配件件号
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("partName")); // 配件名称
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));// 是否有效

            StringBuffer sql = new StringBuffer();

            if (null != partCode && !"".equals(partCode)) {
                sql.append(" AND UPPER(PD.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
            }

            if (null != partOldcode && !"".equals(partOldcode)) {
                sql.append(" AND UPPER(PD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
            }
            if (null != partName && !"".equals(partName)) {
                sql.append(" AND PD.PART_NAME LIKE '%" + partName + "%' ");
            }

            if (null != state && !"".equals(state)) {
                sql.append(" AND RD.STATE = '" + state + "' ");
            }

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "配件编码";
            head[2] = "配件名称";
            head[3] = "件号";
            head[4] = "分配比例";
            head[5] = "分配数量";
            head[6] = "是否允许BO转销售";
            head[7] = "是否有效";

            List<Map<String, Object>> list = dao.queryPartResAllotSetList(sql.toString());

            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[3] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("ALLOT_RATIO"));
                        detail[5] = CommonUtils.checkNull(map.get("ALLOT_NUM"));
                        int boTosales = Constant.IF_TYPE_NO;
                        if (null != map.get("BO_TOSALES") && !"".equals(map.get("BO_TOSALES"))) {
                            boTosales = Integer.parseInt(map.get("BO_TOSALES").toString());
                        }
                        int typeYes = Constant.IF_TYPE_YES;
                        if (typeYes == boTosales) {
                            detail[6] = "是";
                        } else {
                            detail[6] = "否";
                        }

                        int stateInt = Constant.STATUS_DISABLE;
                        if (null != map.get("STATE") && !"".equals(map.get("STATE"))) {
                            stateInt = Integer.parseInt(map.get("STATE").toString());
                        }
                        int stateEnable = Constant.STATUS_ENABLE;
                        if (stateEnable == stateInt) {
                            detail[7] = "有效";
                        } else {
                            detail[7] = "无效";
                        }

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件资源分配设置信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件资源分配设置");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param : @param response
     * @param : @param request
     * @param : @param head
     * @param : @param list
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 文件导出为xls文件
     */
    public static Object exportEx(String fileName, ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = fileName + ".xls";
        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename="
                    + URLEncoder.encode(name, "utf-8"));
            out = response.getOutputStream();
            wwb = Workbook.createWorkbook(out);
            jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

            if (head != null && head.length > 0) {
                for (int i = 0; i < head.length; i++) {
                    ws.addCell(new Label(i, 0, head[i]));
                }
            }
            int pageSize = list.size() / 30000;
            for (int z = 1; z < list.size() + 1; z++) {
                String[] str = list.get(z - 1);
                for (int i = 0; i < str.length; i++) {
                        /*ws.addCell(new Label(i, z, str[i]));*/ //modify by yuan
                    if (CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])) {
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                    } else {
                        ws.addCell(new Label(i, z, str[i]));
                    }
                }
            }
            wwb.write();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (null != wwb) {
                wwb.close();
            }
            if (null != out) {
                out.close();
            }
        }
        return null;
    }

}
