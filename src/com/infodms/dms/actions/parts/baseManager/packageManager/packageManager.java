package com.infodms.dms.actions.parts.baseManager.packageManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.packageManager.packageDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartPackagePO;
import com.infodms.dms.po.TtPartPackageRecordPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.TC_CodeAddUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Workbook;
import jxl.write.Label;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class packageManager {
    private final String packageMainInit = "/jsp/parts/baseManager/partsBaseManager/packageManager/packageMain.jsp";
    private final String packageAddInit = "/jsp/parts/baseManager/partsBaseManager/packageManager/packageAdd.jsp";
    private final String packageEditInit = "/jsp/parts/baseManager/partsBaseManager/packageManager/packageEdit.jsp";
    public Logger logger = Logger.getLogger(packageManager.class);
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    packageDAO dao = packageDAO.getInstance();
    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);


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
     * @throws :
     * @FUNCTION : 包装材料主页面初始化
     * @author : andyzhou
     * @LastDate : 2013-10-20
     */
    public void packageMainInit() {
        try {
            String selectBox = TC_CodeAddUtil.genSelBoxExp(Constant.PACK_TYPE, "TYPE");
            act.setOutData("selectBox", selectBox);
            act.setForword(packageMainInit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 包装材料主页面实际查询
     * @author : andyzhou
     * @LastDate : 2013-10-20
     */
    public void getMainList() {
        RequestWrapper request = act.getRequest();
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            //String json =URLDecoder.decode(request.getParamValue("json"),"UTF-8");
          /*  String json = new String(request.getParamValue("json").getBytes("ISO8859-1"), "UTF-8");
            JSONObject paraObject = JSONObject.fromObject(json);
            paraObject.put("TYPE", request.getParamValue("TYPE"));
            logger.info("---paraObject" + paraObject);*/
            PageResult<Map<String, Object>> ps = dao.getMainList(request, Constant.PAGE_SIZE, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "包装材料主页面错误!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 包装材料新增初始化
     * @author : andyzhou
     * @LastDate : 2013-10-20
     */
    public void packageAddInit() {
        try {
            String selectBox = TC_CodeAddUtil.genSelBoxExp(Constant.PACK_TYPE, "PACK_TYPE");
            act.setOutData("selectBox", selectBox);
            act.setForword(packageAddInit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 包装材料修改初始化
     * @author : andyzhou
     * @LastDate : 2013-10-20
     */
    public void packageEditInit() {
        try {
            String packageId = request.getParamValue("packageId");
            Map<String, Object> dataMap = dao.getViewData(packageId);
            String selectBox = TC_CodeAddUtil.genSelBoxExp(Constant.PACK_TYPE, "PACK_TYPE");
            act.setOutData("selectBox", selectBox);
            act.setOutData("dataMap", dataMap);
            act.setForword(packageEditInit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 包装材料保存
     * @author : andyzhou
     * @LastDate : 2013-7-1
     */
    public void save() {
        try {
            String pack_spec = CommonUtils.checkNull(request.getParamValue("PACK_SPEC"));
            String pack_type = CommonUtils.checkNull(request.getParamValue("PACK_TYPE"));
            String pack_name = CommonUtils.checkNull(request.getParamValue("PACK_NAME"));
            String pack_uom = CommonUtils.checkNull(request.getParamValue("PACK_UOM"));
            String pack_qty = CommonUtils.checkNull(request.getParamValue("PACK_QTY"));
            String id = CommonUtils.checkNull(request.getParamValue("PACK_ID"));
            TtPartPackagePO po = new TtPartPackagePO();
            TtPartPackagePO oldpo = new TtPartPackagePO();
            if (!id.equals("")) {
                po.setPackId(Long.parseLong(id));
                po.setUpdateBy(logonUser.getUserId());
                po.setUpdateDate(new Date());
                oldpo.setPackId(Long.parseLong(id));
            } else {
                po.setPackId(Long.parseLong(SequenceManager.getSequence("")));
                po.setCreateBy(logonUser.getUserId());
                po.setCreateDate(new Date());
            }
            po.setPackSpec(pack_spec);
            po.setPackType(Long.valueOf(pack_type));
            po.setPackName(pack_name);
            po.setPackUom(pack_uom);
            po.setPackQty(Double.valueOf(pack_qty));
            if (!id.equals("")) {
                dao.update(oldpo, po);
            } else {
                dao.insert(po);
            }
            act.setOutData("success", "保存成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "包装材料保存出错,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 包装材料删除
     * @author : andyzhou
     * @LastDate : 2013-10-21
     */
    public void packageDel() {
        try {
            String packageId = request.getParamValue("PACK_ID");
            logger.info("----------packageId=" + packageId);
            TtPartPackagePO po = new TtPartPackagePO();
            TtPartPackagePO oldpo = new TtPartPackagePO();
            po.setPackId(Long.parseLong(packageId));
            oldpo.setPackId(Long.parseLong(packageId));
            po.setStatus(Constant.STATUS_DISABLE);
            po.setUpdateBy(logonUser.getUserId());
            po.setUpdateDate(new Date());
            dao.update(oldpo, po);
            act.setOutData("success", "删除成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.DELETE_FAILURE_CODE, "包装材料删除出错,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @FUNCTION : 包装材料出入库
     * @author : andyzhou
     * @LastDate : 2013-10-21
     */
    public void change() {
        try {
            String json = new String(request.getParamValue("json").getBytes("ISO8859-1"), "UTF-8");
            JSONObject dataObject = JSONObject.fromObject(json);
            logger.info("---------dataObject=" + dataObject);
            Long packageId = dataObject.getLong("PACK_ID");
            Long flag = dataObject.getLong("FLAG");
            Double qty = dataObject.getDouble("QTY");
            String remark = dataObject.getString("remark").trim();
            TtPartPackageRecordPO po = new TtPartPackageRecordPO();
            po.setPackId(packageId);
            po.setFlag(flag);
            po.setQty(qty);
            po.setRemark(remark);
            po.setCreateBy(logonUser.getUserId());
            po.setCreateDate(new Date());
            dao.insert(po);

            Map<String, Object> dataMap = dao.getViewData(dataObject.getString("PACK_ID"));
            Double packQty = Double.parseDouble(dataMap.get("PACK_QTY").toString());
            if (flag == 1) {
                dao.update("update TT_PART_PACKAGE set PACK_QTY=PACK_QTY+" + qty + " where PACK_ID=" + packageId, null);
            } else {
                if (packQty < qty) {
                    BizException e1 = new BizException(act, ErrorCodeConstant.UPDATE_FAILURE_CODE, "库存不足");
                    act.setException(e1);
                } else {
                    dao.update("update TT_PART_PACKAGE set PACK_QTY=PACK_QTY-" + qty + " where PACK_ID=" + packageId, null);
                }
            }
            act.setOutData("success", "保存成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "包装材料出入库出错,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 导出配件包装材料Excel
     */
    public void expPartPkgProExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {

            String[] head = new String[11];
            head[0] = "序号";
            head[1] = "规格";
            head[2] = "包装类别";
            head[3] = "名称";
            head[4] = "单位";
            head[5] = "当前库存";
            head[6] = "计划数量";

            List<Map<String, Object>> list = dao.partPkgExpList();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[11];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[3] = CommonUtils.checkNull(map
                                .get("PACK_SPEC"));
                        detail[1] = CommonUtils.checkNull(map
                                .get("PACK_TYPE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("PACK_NAME"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("PACK_UOM"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("PACK_QTY"));
                        detail[6] = CommonUtils.checkNull(map
                                .get("PLAN_QTY"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件包装材料采购计划";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件包装材料采购计划");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 导出配件包装材料Excel
     */
    public void expPartPkgToExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {

            String[] head = new String[11];
            head[0] = "序号";
            head[1] = "规格";
            head[2] = "包装类别";
            head[3] = "名称";
            head[4] = "单位";
            head[5] = "库存";

            List<Map<String, Object>> list = dao.getExportList(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[11];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[3] = CommonUtils.checkNull(map
                                .get("PACK_SPEC"));
                        detail[1] = CommonUtils.checkNull(map
                                .get("PACK_TYPE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("PACK_NAME"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("PACK_UOM"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("PACK_QTY"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件包装材料库存";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件包装材料采购计划");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}