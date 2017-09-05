package com.infodms.dms.actions.parts.baseManager.partInnerOrgManager;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.util.CheckUtil;
import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partInnerOrgManager.partInnerOrgDao;
import com.infodms.dms.dao.parts.baseManager.partPlannerWarehouseManager.partPlannerWarehouseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartInnerOrgPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 配件内部单位设置
 * @Date: 2013-9-22
 * @remark
 */
public class partInnerOrgAction {
    public Logger logger = Logger.getLogger(partInnerOrgAction.class);
    private static final partInnerOrgDao dao = partInnerOrgDao.getInstance();

    private static final String PART_IN_ORG_MAIN = "/jsp/parts/baseManager/partInnerOrgManager/partInnerOrgMain.jsp";//配件内部单位设置首页
    private static final String PART_IN_ORG_MOD = "/jsp/parts/baseManager/partInnerOrgManager/partInnerOrgMod.jsp";//配件内部单位设置页面
    private static final String PART_IN_ORG_ADD = "/jsp/parts/baseManager/partInnerOrgManager/partInnerOrgAdd.jsp";//配件内部单位设置ADD页面

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 跳转至配件内部单位设置页面
     */
    public void partInnerOrgInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            act.setForword(PART_IN_ORG_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件内部单位设置初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 按条件查询配件内部单位信息
     */
    public void partInnerOrgSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); // 单位编码
            String orgName = CommonUtils.checkNull(request.getParamValue("orgName")); // 单位名称
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));// 是否有效

            String parentOrgId = "";//父机构ID
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
            } else {
                parentOrgId = logonUser.getDealerId();
            }

            StringBuffer sql = new StringBuffer();

            if (null != orgCode && !"".equals(orgCode)) {
                sql.append(" AND UPPER(IO.IN_ORG_CODE) LIKE '%" + orgCode.trim().toUpperCase() + "%' ");
            }

            if (null != orgName && !"".equals(orgName)) {
                sql.append(" AND IO.IN_ORG_NAME LIKE '%" + orgName.trim() + "%' ");
            }

            if (null != state && !"".equals(state)) {
                sql.append(" AND IO.STATE = '" + state + "' ");
            }

            sql.append(" AND IO.PRT_ORG_ID = '" + parentOrgId + "' ");

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = dao.queryPartInnerOrg(sql.toString(), parentOrgId, Constant.PAGE_SIZE, curPage);

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
     * @Title : 失效配件内部单位设置
     */
    public void celPartInnerOrg() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String defineId = CommonUtils.checkNull(request.getParamValue("disabeParms"));// 关系ID
            Long userId = logonUser.getUserId();// 操作用户ID

            //TtPartInnerOrgPO
            TtPartInnerOrgPO selPO = new TtPartInnerOrgPO();
            TtPartInnerOrgPO updatePO = new TtPartInnerOrgPO();

            selPO.setInOrgId(Long.parseLong(defineId));

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
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "失效配件内部单位设置失败 ");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 有效配件内部单位设置
     */
    public void enablePartInnerOrg() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String defineId = CommonUtils.checkNull(request.getParamValue("enableParms"));// 关系ID
            Long userId = logonUser.getUserId();// 操作用户ID
            partPlannerWarehouseDao dao = partPlannerWarehouseDao.getInstance();

            //TtPartInnerOrgPO
            TtPartInnerOrgPO selPO = new TtPartInnerOrgPO();
            TtPartInnerOrgPO updatePO = new TtPartInnerOrgPO();

            selPO.setInOrgId(Long.parseLong(defineId));

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
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "有效配件内部单位设置失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-6
     * @Title : 更新配件内部单位设置
     */
    public void updatePartInnerOrg() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            Long userId = logonUser.getUserId();// 操作用户ID
            Date date = new Date();
            String inOrgId = CommonUtils.checkNull(request.getParamValue("inOrgId"));// 内部单位ID
            String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));// 单位编码
            String orgName = CommonUtils.checkNull(request.getParamValue("orgName")); // 单位名称
            String state = request.getParamValue("STATE"); // 是否有效
            String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan")); // 联系人
            String address = CommonUtils.checkNull(request.getParamValue("address")); // 地址
            String linkPhone = CommonUtils.checkNull(request.getParamValue("linkPhone")); // 联系电话

            //TtPartInnerOrgPO
            TtPartInnerOrgPO selPO = null;
            TtPartInnerOrgPO updatePO = null;

            selPO = new TtPartInnerOrgPO();
            updatePO = new TtPartInnerOrgPO();

            selPO.setInOrgId(Long.parseLong(inOrgId));

            updatePO.setInOrgCode(orgCode.trim().toUpperCase());
            updatePO.setInOrgName(orgName.trim());
            updatePO.setLinkMan(linkMan);
            updatePO.setLinkPhone(linkPhone);
            updatePO.setAddress(address);
            if (null != state && !"".equals(state)) {
                updatePO.setState(Integer.parseInt(state));
            }

            updatePO.setUpdateBy(userId);
            updatePO.setUpdateDate(date);

            dao.update(selPO, updatePO);

            Thread.sleep(200);


            act.setOutData("success", "true");

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "更新配件内部单位设置");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-6
     * @Title : 跳转至配件内部单位设置修改页面
     */
    public void partInnerOrgFormodInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        try {
            String defineIdMod = CommonUtils.checkNull(request.getParamValue("defineIdMod"));// 内部单位id
            String parentOrgId = "";//父机构ID
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
            } else {
                parentOrgId = logonUser.getDealerId();
            }

            String sqlStr = " AND IO.IN_ORG_ID = '" + defineIdMod + "' ";

            List<Map<String, Object>> list = dao.queryPartInnerOrgList(sqlStr, parentOrgId);

            Map<String, Object> map = null;

            if (null != list && list.size() > 0) {
                map = list.get(0);
            }

            act.setOutData("map", map);

            act.setForword(PART_IN_ORG_MOD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件内部单位设置修改初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title : 跳转至配件内部单位设置新增页面
     */
    public void partInnerOrgAddInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            act.setForword(PART_IN_ORG_ADD);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件内部单位设置新增初始化");
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
    public void insertPartInnerOrg() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String errorExist = "";
        try {
            String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));// 单位编码
            String orgName = CommonUtils.checkNull(request.getParamValue("orgName")); // 单位名称
            String state = request.getParamValue("STATE"); // 是否有效
            String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan")); // 联系人
            String address = CommonUtils.checkNull(request.getParamValue("address")); // 地址
            String linkPhone = CommonUtils.checkNull(request.getParamValue("linkPhone")); // 联系电话

            Long userId = logonUser.getUserId();// 用户ID
            Date date = new Date();

            String parentOrgId = "";//父机构ID
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
            } else {
                parentOrgId = logonUser.getDealerId();
            }


            StringBuffer sbStr = new StringBuffer();
            sbStr.append(" AND UPPER(IO.IN_ORG_CODE) = '" + orgCode.trim().toUpperCase() + "' ");
            sbStr.append(" AND IO.PRT_ORG_ID = '" + parentOrgId + "' ");

            List<Map<String, Object>> list = dao.queryPartInnerOrgList(sbStr.toString(), parentOrgId);

            if (null != list && list.size() > 0) {
                errorExist = orgCode.trim();
                act.setOutData("errorExist", errorExist);// 关系记录存在
            } else {
                // TtPartInnerOrgPO
                TtPartInnerOrgPO insertPO = new TtPartInnerOrgPO();

                insertPO.setInOrgId(Long.parseLong(SequenceManager.getSequence("")));
                insertPO.setInOrgCode(orgCode.trim().toUpperCase());
                insertPO.setInOrgName(orgName.trim());
                insertPO.setAddress(address);
                insertPO.setLinkMan(linkMan);
                insertPO.setLinkPhone(linkPhone);
                insertPO.setPrtOrgId(Long.parseLong(parentOrgId));
                insertPO.setCreateBy(userId);
                insertPO.setCreateDate(date);
                insertPO.setUpdateBy(userId);
                insertPO.setUpdateDate(date);
                insertPO.setState(Constant.STATUS_ENABLE);
                insertPO.setStatus(1);

                dao.insert(insertPO);

                act.setOutData("success", "true");
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "新增配件内部单位");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-22
     * @Title : 导出内部单位维护excel
     */
    public void expPartInnerOrgExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); // 单位编码
            String orgName = CommonUtils.checkNull(request.getParamValue("orgName")); // 单位名称
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));// 是否有效

            String parentOrgId = "";//父机构ID
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
            } else {
                parentOrgId = logonUser.getDealerId();
            }

            StringBuffer sql = new StringBuffer();

            if (null != orgCode && !"".equals(orgCode)) {
                sql.append(" AND UPPER(IO.IN_ORG_CODE) LIKE '%" + orgCode.trim().toUpperCase() + "%' ");
            }

            if (null != orgName && !"".equals(orgName)) {
                sql.append(" AND IO.IN_ORG_NAME LIKE '%" + orgName.trim() + "%' ");
            }

            if (null != state && !"".equals(state)) {
                sql.append(" AND IO.STATE = '" + state + "' ");
            }

            sql.append(" AND IO.PRT_ORG_ID = '" + parentOrgId + "' ");

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "单位编码";
            head[2] = "单位名称";
            head[3] = "地址";
            head[4] = "联系人";
            head[5] = "联系电话";
            head[6] = "修改人";
            head[7] = "修改时间";
            head[8] = "是否有效";

            List<Map<String, Object>> list = dao.queryPartInnerOrgList(sql.toString(), parentOrgId);

            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("IN_ORG_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("IN_ORG_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("ADDRESS"));
                        detail[4] = CommonUtils.checkNull(map.get("LINK_MAN"));
                        detail[5] = CommonUtils.checkNull(map.get("LINK_PHONE"));
                        detail[6] = CommonUtils.checkNull(map.get("NAME"));
                        detail[7] = CommonUtils.checkNull(map.get("UPDATE_DATE"));

                        int stateInt = Integer.parseInt(CommonUtils.checkNull(map.get("STATE")));
                        int stateEnable = Constant.STATUS_ENABLE;
                        if (stateEnable == stateInt) {
                            detail[8] = "有效";
                        } else {
                            detail[8] = "无效";
                        }

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件内部单位维护信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件内部单位设置失败!");
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
