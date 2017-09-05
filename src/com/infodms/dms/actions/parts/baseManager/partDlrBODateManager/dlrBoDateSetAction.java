package com.infodms.dms.actions.parts.baseManager.partDlrBODateManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partDlrBODateManager.dlrBoDateSetDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartSalesRelationPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
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
 * @Title: 处理服务商BO有效期设置业务
 * @Date: 2013-7-2
 * @remark
 */
public class dlrBoDateSetAction {
    public Logger logger = Logger.getLogger(dlrBoDateSetAction.class);
    private static final dlrBoDateSetDao dao = dlrBoDateSetDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();

    //BO有效期设置页面
    private static final String PART_DLR_BO_DATE_SET = "/jsp/parts/baseManager/partDlrBODateManager/partDlrBODateSet.jsp";

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-2
     * @Title : BO有效期设置页面
     */
    public void dlrBoDateSetInit() {
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
            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);

            act.setForword(PART_DLR_BO_DATE_SET);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "BO有效期设置页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-2
     * @Title : 配件采购关系查询
     */
    public void dlrBoDaysSetSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String cldName = CommonUtils.checkNull(request.getParamValue("cldName")); // 子机构名称
            String cldCode = CommonUtils.checkNull(request.getParamValue("cldCode")); // 子机构编码
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); // 状态
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID

            StringBuffer sqlStr = new StringBuffer();
            if (null != cldName && !"".equals(cldName)) {
                sqlStr.append(" AND SA.CHILDORG_NAME  LIKE '%" + cldName + "%' ");
            }

            if (null != cldCode && !"".equals(cldCode)) {
                sqlStr.append(" AND SA.CHILDORG_CODE  LIKE '%" + cldCode + "%' ");
            }

            if (null != state && !"".equals(state)) {
                sqlStr.append(" AND SA.STATE  = '" + state + "' ");
            }

            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sqlStr.append(" AND SA.PARENTORG_ID = '" + parentOrgId + "'");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = dao.querySaleRelation(sqlStr.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件采购关系查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-2
     * @Title : 更新BO有效期
     */
    public void updateBoDays() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String rlId = CommonUtils.checkNull(request.getParamValue("rlId"));//采购关系ID
            String boDays = CommonUtils.checkNull(request.getParamValue("boDays"));//BO有效期
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            TtPartSalesRelationPO selPO = new TtPartSalesRelationPO();
            TtPartSalesRelationPO updatePO = new TtPartSalesRelationPO();

            selPO.setRelationId(Long.parseLong(rlId));

            updatePO.setBoDays(Long.parseLong(boDays));
            updatePO.setUpdateBy(logonUser.getUserId());
            updatePO.setUpdateDate(new Date());

            dao.update(selPO, updatePO);

            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "更新BO有效期");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-15
     * @Title :导出配件采购关系信息
     */
    public void expDlrBODaysExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String cldName = CommonUtils.checkNull(request.getParamValue("cldName")); // 子机构名称
            String cldCode = CommonUtils.checkNull(request.getParamValue("cldCode")); // 子机构编码
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); // 状态
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID

            StringBuffer sqlStr = new StringBuffer();
            if (null != cldName && !"".equals(cldName)) {
                sqlStr.append(" AND SA.CHILDORG_NAME  LIKE '%" + cldName + "%' ");
            }

            if (null != cldCode && !"".equals(cldCode)) {
                sqlStr.append(" AND SA.CHILDORG_CODE  LIKE '%" + cldCode + "%' ");
            }

            if (null != state && !"".equals(state)) {
                sqlStr.append(" AND SA.STATE  = '" + state + "' ");
            }

            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sqlStr.append(" AND SA.PARENTORG_ID = '" + parentOrgId + "'");
            }

            String[] head = new String[10];
            head[0] = "序号";
            head[1] = "子机构单位编码";
            head[2] = "子机构单位名称";
            head[3] = "是否有效";
            head[4] = "BO有效期(天)";
            List<Map<String, Object>> list = dao.querySaleRelationList(sqlStr.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[10];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("CHILDORG_CODE"));
                        detail[2] = CommonUtils
                                .checkNull(map.get("CHILDORG_NAME"));
                        int stateInt = Integer.parseInt(CommonUtils
                                .checkNull(map.get("STATE")));

                        if (Constant.STATUS_ENABLE == stateInt) {
                            detail[3] = "有效";
                        } else {
                            detail[3] = "无效";
                        }
                        detail[4] = CommonUtils.checkNull(map
                                .get("BO_DAYS"));
                        list1.add(detail);
                    }
                }
            }

            String fileName = "BO有效期设置信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出服务商账户信息");
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
     * @throws : LastDate    : 2013-4-15
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
