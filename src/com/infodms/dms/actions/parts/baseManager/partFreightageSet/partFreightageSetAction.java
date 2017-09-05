package com.infodms.dms.actions.parts.baseManager.partFreightageSet;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partFreightageSet.partFreightageSetDao;
import com.infodms.dms.dao.parts.baseManager.partPlannerWarehouseManager.partPlannerWarehouseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartFreightageDefinePO;
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
 * @Title: 处理配件运费加价设置业务
 * @Description:CHANADMS
 * @Copyright: Copyright (c) 2010
 * @Date: 2013-7-6
 * @remark
 */
public class partFreightageSetAction {
    private static final partFreightageSetDao dao = partFreightageSetDao.getInstance();
    private static final String PART_FRT_SET_MAIN = "/jsp/parts/baseManager/partFreightageSet/partFreightageMain.jsp";//配件运费加价设置首页
    private static final String PART_FRT_SET_MOD = "/jsp/parts/baseManager/partFreightageSet/partFreightageMod.jsp";//配件运费加价设置页面
    private static final String PART_FRT_SET_ADD = "/jsp/parts/baseManager/partFreightageSet/partFreightageAdd.jsp";//配件运费加价设置ADD页面
    public Logger logger = Logger.getLogger(partFreightageSetAction.class);

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

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 跳转至配件运费加价设置页面
     */
    public void partFreightageSetInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            List<Map<String, Object>> vendList = dao.getVenderList();
            act.setOutData("vendList", vendList);
            act.setForword(PART_FRT_SET_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件运费加价设置初始化");
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
    public void partFreightageSetSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 配件件号
            String dealerType = CommonUtils.checkNull(request.getParamValue("dealerType")); // 配件编码
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));// 是否有效

            StringBuffer sql = new StringBuffer();


            if (null != orderType && !"".equals(orderType)) {
                sql.append(" AND FD.ORDER_TYPE = '" + orderType + "' ");
            }

            if (null != dealerType && !"".equals(dealerType)) {
                sql.append(" AND FD.DEALER_TYPE = '" + dealerType + "' ");
            }

            if (null != state && !"".equals(state)) {
                sql.append(" AND FD.STATE = '" + state + "' ");
            }
            sql.append(" AND  FD.STATUS = 1");

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = dao.queryPartFreightageSet(sql.toString(), Constant.PAGE_SIZE, curPage);

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
     * @Title : 失效配件运费加价设置
     */
    public void celPartFreightageSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String defineId = CommonUtils.checkNull(request.getParamValue("disabeParms"));// 关系ID
            Long userId = logonUser.getUserId();// 操作用户ID

            //TtPartFreightageDefinePO
            TtPartFreightageDefinePO selPO = new TtPartFreightageDefinePO();
            TtPartFreightageDefinePO updatePO = new TtPartFreightageDefinePO();

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
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "失效配件运费加价设置失败 ");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 有效配件运费加价设置
     */
    public void enablePartFreightageSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String defineId = CommonUtils.checkNull(request.getParamValue("enableParms"));// 关系ID
            Long userId = logonUser.getUserId();// 操作用户ID
            partPlannerWarehouseDao dao =  partPlannerWarehouseDao.getInstance();

            //TtPartFreightageDefinePO
            TtPartFreightageDefinePO selPO = new TtPartFreightageDefinePO();
            TtPartFreightageDefinePO updatePO = new TtPartFreightageDefinePO();

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
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件运费加价设置失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-6
     * @Title : 更新配件运费加价设置
     */
    public void updatePartFreightageSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            Long userId = logonUser.getUserId();// 操作用户ID
            Date date = new Date();
            String defineId = CommonUtils.checkNull(request.getParamValue("defineId"));// 序列ID
//			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));// 订单类型
//			String dealerType = CommonUtils.checkNull(request.getParamValue("dealerType")); // 订货单位类型
            String freeTimes = CommonUtils.checkNull(request.getParamValue("freeTimes")); // 免运费次数
            String freeOption = CommonUtils.checkNull(request.getParamValue("freeOption")); // 免运费设置
            String freeCondition = CommonUtils.checkNull(request.getParamValue("freeCondition")).trim().replace(",", ""); // 免运费条件
            String markupRatio = CommonUtils.checkNull(request.getParamValue("markupRatio")); // 加收比例
            String minPrice = CommonUtils.checkNull(request.getParamValue("minPrice")); // 加收比例
//			String state = CommonUtils.checkNull(request.getParamValue("STATE")); // 状态

            //TtPartFreightageDefinePO
            TtPartFreightageDefinePO selPO = new TtPartFreightageDefinePO();
            TtPartFreightageDefinePO updatePO = new TtPartFreightageDefinePO();

            selPO.setDefId(Long.parseLong(defineId));

            updatePO.setFreeTimes(Integer.parseInt(freeTimes));
            updatePO.setFrgOption(Integer.parseInt(freeOption));
            updatePO.setFreeCondition(Double.parseDouble(freeCondition));
            updatePO.setMarkupRatio(Float.parseFloat(markupRatio));
            if (minPrice != null && minPrice != "" && !"".equals(minPrice)) {
                updatePO.setMinPirce(Double.valueOf(minPrice));
            }
            /*if(null != state && !"".equals(state))
			{
				updatePO.setState(Integer.parseInt(state));
			}
			else
			{
				updatePO.setState(Constant.STATUS_ENABLE);
			}*/
            updatePO.setUpdateBy(userId);
            updatePO.setUpdateDate(date);

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
     * @throws : LastDate : 2013-7-6
     * @Title : 跳转至配件运费加价设置修改页面
     */
    public void partFreightageSetFormodInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        try {
            String defineId = CommonUtils.checkNull(request.getParamValue("defineId"));// 序列ID
            String orderType = "";
            String otName = "";
            String dealerType = "";
            String dtName = "";
            String freeTimes = "";
            String freeCondition = "";
            String markupRatio = "";
            String state = "";

            String sqlStr = " AND FD.DEF_ID = '" + defineId + "'";
            List<Map<String, Object>> ftList = dao.queryPartFreightageSetList(sqlStr);
            if (null != ftList && ftList.size() == 1) {
                orderType = ftList.get(0).get("ORDER_TYPE").toString();
                otName = ftList.get(0).get("OT_NAME").toString();
                dealerType = ftList.get(0).get("DEALER_TYPE").toString();
                dtName = ftList.get(0).get("DT_NAME").toString();
                freeTimes = ftList.get(0).get("FREE_TIMES").toString();
                freeCondition = ftList.get(0).get("FREE_CONDITION").toString();
                markupRatio = ftList.get(0).get("MARKUP_RATIO").toString();
                state = ftList.get(0).get("STATE").toString();
            }

            act.setOutData("defineId", defineId);
            act.setOutData("orderType", orderType);
            act.setOutData("otName", otName);
            act.setOutData("dealerType", dealerType);
            act.setOutData("dtName", dtName);
            act.setOutData("freeTimes", freeTimes);
            act.setOutData("freeCondition", freeCondition);
            act.setOutData("markupRatio", markupRatio);
            act.setOutData("state", state);
            act.setForword(PART_FRT_SET_MOD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件运费加价设置修改初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 跳转至配件运费加价设置新增页面
     */
    public void partFreightageSetAddInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            List<Map<String, Object>> vendList = dao.getVenderList();
            act.setOutData("vendList", vendList);
            act.setForword(PART_FRT_SET_ADD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件运费加价设置新增初始化");
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
    public void insertPartFreightageSet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String errorExist = "";
        try {
            String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));// 订单类型
            String dealerType = CommonUtils.checkNull(request.getParamValue("dealerType")); // 订货单位类型
            String freeTimes = CommonUtils.checkNull(request.getParamValue("freeTimes")); // 免运费次数
            String freeCondition = CommonUtils.checkNull(request.getParamValue("freeCondition")).trim().replace(",", ""); // 免运费条件
            String markupRatio = CommonUtils.checkNull(request.getParamValue("markupRatio")); // 加收比例
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); // 状态

            Long userId = logonUser.getUserId();// 用户ID
            Date date = new Date();

            // TtPartFreightageDefinePO
            TtPartFreightageDefinePO insertPO = null;

            List existlist = dao.getExistPO(orderType, dealerType);
            if (existlist != null && existlist.size() > 0) {
                errorExist += "运费关系已存在,不能重复新增!";
            }
            if ("".equals(errorExist)) {

                insertPO = new TtPartFreightageDefinePO();

                insertPO.setDefId(Long.parseLong(SequenceManager.getSequence("")));
                insertPO.setOrderType(Integer.parseInt(orderType));
                insertPO.setDealerType(Integer.parseInt(dealerType));
                insertPO.setFreeTimes(Integer.parseInt(freeTimes));
                insertPO.setFreeCondition(Double.parseDouble(freeCondition));
                insertPO.setMarkupRatio(Float.parseFloat(markupRatio));
                insertPO.setCreateBy(userId);
                if (null != state && !"".equals(state)) {
                    insertPO.setState(Integer.parseInt(state));
                } else {
                    insertPO.setState(Constant.STATUS_ENABLE);
                }

                dao.insert(insertPO);
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
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "新增配件直发条件");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void exportPartFreightageExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 配件件号
            String dealerType = CommonUtils.checkNull(request.getParamValue("dealerType")); // 配件编码
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));// 是否有效

            StringBuffer sql = new StringBuffer();


            if (null != orderType && !"".equals(orderType)) {
                sql.append(" AND FD.ORDER_TYPE = '" + orderType + "' ");
            }

            if (null != dealerType && !"".equals(dealerType)) {
                sql.append(" AND FD.DEALER_TYPE = '" + dealerType + "' ");
            }

            if (null != state && !"".equals(state)) {
                sql.append(" AND FD.STATE = '" + state + "' ");
            }

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "订单类型";
            head[2] = "订货单位类型";
            head[3] = "免运费次数";
            head[4] = "免运费设置";
            head[5] = "免运费条件(含税)";
            head[6] = "加收比例(按销售额)";
            head[7] = "是否有效";

            List<Map<String, Object>> list = dao.queryPartFreightageSetList(sql.toString());
            int stateEnable = Constant.STATUS_ENABLE;
            int option1 = Constant.PART_FREIGHTAGE_OPTION_01;
            int option2 = Constant.PART_FREIGHTAGE_OPTION_02;
            int option3 = Constant.PART_FREIGHTAGE_OPTION_03;

            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("OT_NAME"));
                        detail[2] = CommonUtils.checkNull(map.get("FIX_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("FREE_TIMES"));

                        int optionInt = Integer.parseInt(CommonUtils.checkNull(map.get("FRG_OPTION")));

                        if (option1 == optionInt) {
                            detail[4] = "大于";
                        } else if (option2 == optionInt) {
                            detail[4] = "小于";
                        } else {
                            detail[4] = "等于";
                        }

                        detail[5] = CommonUtils.checkNull(map.get("FREE_CONDITION")).replace(",", "");
                        detail[6] = CommonUtils.checkNull(map.get("MARKUP_RATIO"));

                        int stateInt = Integer.parseInt(CommonUtils.checkNull(map.get("STATE")));

                        if (stateEnable == stateInt) {
                            detail[7] = "有效";
                        } else {
                            detail[7] = "无效";
                        }

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件运费加价设置信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件运费加价设置");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

}
