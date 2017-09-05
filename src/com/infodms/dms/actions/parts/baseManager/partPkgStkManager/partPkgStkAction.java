package com.infodms.dms.actions.parts.baseManager.partPkgStkManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.infodms.dms.util.CheckUtil;
import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partPkgStkManager.partPkgStkDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartBuyPricePO;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartLoactionDefinePO;
import com.infodms.dms.po.TtPartMakerRelationPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author huchao
 * @version 1.0
 * @Title: 处理配件包装储运维护业务
 * @Description:CHANADMS
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2013-7-26
 * @remark
 */
public class partPkgStkAction extends BaseImport {
    public Logger logger = Logger.getLogger(partPkgStkAction.class);
    private static final partPkgStkDao dao = partPkgStkDao.getInstance();

    private static final String PKG_STK_URL = "/jsp/parts/baseManager/partPkgStkManager/partPkgStkMain.jsp";//配件包装储运维护页面
    private static final String INPUT_ERROR_URL = "/jsp/parts/baseManager/partPkgStkManager/inputError.jsp";//数据导入出错页面

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-26
     * @Title : 访问配件包装储运维护页面
     */
    public void partPkgStkInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PKG_STK_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件包装储运维护初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :    json
     * @throws : LastDate    : 2013-7-26
     * @Title : 配件包装储运维护-查询
     */
    public void partPkgStkSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //件号
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPkgStk(partCode,
                    partOldcode, partCname, Constant.PAGE_SIZE, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件包装储运维护初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-26
     * @Title : 保存配件包装储运
     */
    public void savePkgStk() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId")); //配件Id
            String pkgSizeText = CommonUtils.checkNull(request.getParamValue("pkgSizeText"));//包装尺寸
            String oemMinPkgText = CommonUtils.checkNull(request.getParamValue("oemMinPkgText"));//最小包装量
            String minPkgText = CommonUtils.checkNull(request.getParamValue("minPkgText")); //整包发运量

            Long userId = logonUser.getUserId();
            TtPartDefinePO selPo = new TtPartDefinePO();
            TtPartDefinePO updatePo = new TtPartDefinePO();

            selPo.setPartId(Long.parseLong(partId));

            updatePo.setPkgSize(pkgSizeText);
            updatePo.setOemMinPkg(Long.parseLong(oemMinPkgText));
            updatePo.setMinPkg(Long.parseLong(minPkgText));

            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            dao.update(selPo, updatePo);

            TtPartLoactionDefinePO selLPo = new TtPartLoactionDefinePO();
            TtPartLoactionDefinePO updLPo = new TtPartLoactionDefinePO();

            selLPo.setPartId(Long.parseLong(partId));

            updLPo.setMinPkg(Long.parseLong(minPkgText));

            dao.update(selLPo, updLPo);

            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "保存配件包装储运");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param : @param relList
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 批量更新配件包装储运
     */
    public void savePkgStk(List<Map<String, String>> relList) {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            if (null != relList && relList.size() > 0) {
                TtPartDefinePO selPo = null;
                TtPartDefinePO updatePo = null;
                TtPartLoactionDefinePO selLPo = null;
                TtPartLoactionDefinePO updLPo = null;
                int listSize = relList.size();
                for (int i = 0; i < listSize; i++) {
                    String partId = relList.get(i).get("partId"); //配件Id
                    String pkgSize = relList.get(i).get("pkgSize");//计划员ID
                    String oemMinPkg = relList.get(i).get("oemMinPkg");//最小包装量
                    String minPkg = relList.get(i).get("minPkg");//整包发运量

                    selPo = new TtPartDefinePO();
                    updatePo = new TtPartDefinePO();

                    selPo.setPartId(Long.parseLong(partId));

                    updatePo.setPkgSize(pkgSize);
                    updatePo.setOemMinPkg(Long.parseLong(oemMinPkg));
                    updatePo.setMinPkg(Long.parseLong(minPkg));

                    dao.update(selPo, updatePo);

                    selLPo = new TtPartLoactionDefinePO();
                    updLPo = new TtPartLoactionDefinePO();

                    selLPo.setPartId(Long.parseLong(partId));

                    updLPo.setMinPkg(Long.parseLong(minPkg));

                    dao.update(selLPo, updLPo);
                }
            }

            partPkgStkInit();

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "批量更新配件包装储运失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 配件包装储运维护-> 导入文件
     */
    public void purProUpload() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
            } else {
                parentOrgId = logonUser.getDealerId();
            }
            List<Map<String, String>> errorInfo = null;
            String err = "";

            errorInfo = new ArrayList<Map<String, String>>();
            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmp(request, "uploadFile", 4, 3, maxSize);

            if (errNum != 0) {
                switch (errNum) {
                    case 1:
                        err += "文件列数过多!";
                        break;
                    case 2:
                        err += "空行不能大于三行!";
                        break;
                    case 3:
                        err += "文件不能为空!";
                        break;
                    case 4:
                        err += "文件不能为空!";
                        break;
                    case 5:
                        err += "文件不能大于!";
                        break;
                    default:
                        break;
                }
            }

            if (!"".equals(err)) {
                act.setOutData("error", err);
                act.setForword(INPUT_ERROR_URL);
            } else {
                List<Map> list = getMapList();
                List<Map<String, String>> voList = new ArrayList<Map<String, String>>();
                loadVoList(voList, list, errorInfo);
                if (errorInfo.size() > 0) {
                    act.setOutData("errorInfo", errorInfo);
                    act.setForword(INPUT_ERROR_URL);
                } else {
                    //保存
                    savePkgStk(voList);
                }

            }
        } catch (Exception e) {// 异常方法
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @param voList
     * @param : @param list
     * @param : @param errorInfo
     * @return :
     * LastDate    : 2013-4-12
     * @Title : 读取CELL
     */
    private void loadVoList(List<Map<String, String>> voList, List<Map> list, List<Map<String, String>> errorInfo) {
        if (null == list) {
            list = new ArrayList();
        }
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            if (null == map) {
                map = new HashMap<String, Cell[]>();
            }
            Set<String> keys = map.keySet();
            Iterator it = keys.iterator();
            String key = "";
            while (it.hasNext()) {
                key = (String) it.next();
                Cell[] cells = (Cell[]) map.get(key);
                String partIdTmp = "";
                Map<String, String> tempmap = new HashMap<String, String>();
                if ("".equals(cells[0].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "配件编码");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    List<Map<String, Object>> partCheck = dao.checkOldCode(cells[0].getContents().trim().toUpperCase());
                    if (null != partCheck && partCheck.size() == 1) {
                        partIdTmp = partCheck.get(0).get("PART_ID").toString();
                        tempmap.put("partId", partCheck.get(0).get("PART_ID").toString());
                        tempmap.put("partoldCode", partCheck.get(0).get("PART_OLDCODE").toString());
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "配件编码【" + cells[0].getContents().trim() + "】");
                        errormap.put("3", "不存在!");
                        errorInfo.add(errormap);
                    }
                }

                if (cells.length < 2 || CommonUtils.isEmpty(cells[1].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "包装尺寸");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    String PkStr = cells[1].getContents().trim();
                    tempmap.put("pkgSize", PkStr);
                }

                if (cells.length < 3 || CommonUtils.isEmpty(cells[2].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "最小包装量");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    String minPkStr = cells[2].getContents().trim();
                    String regex = "(^[1-9]+(\\d)*$)";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(minPkStr);

                    if (matcher.find()) {
                        tempmap.put("oemMinPkg", minPkStr);
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "最小包装量");
                        errormap.put("3", "不合法!");
                        errorInfo.add(errormap);
                    }
                }

                if (cells.length < 4 || CommonUtils.isEmpty(cells[3].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "整包发运量");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    String minPkStr = cells[3].getContents().trim();
                    String regex = "(^[1-9]+(\\d)*$)";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(minPkStr);

                    if (matcher.find()) {
                        tempmap.put("minPkg", minPkStr);
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "整包发运量");
                        errormap.put("3", "不合法!");
                        errorInfo.add(errormap);
                    }
                }

                voList.add(tempmap);
            }
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-6
     * @Title : 导出配件采购属批量修改模板
     */
    public void exportExcelTemplate() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();


            List<List<Object>> list = new LinkedList<List<Object>>();

            //标题
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
            listHead.add("配件编码");
            listHead.add("包装尺寸");
            listHead.add("最小包装量 ");
            listHead.add("整包发运量 ");
            list.add(listHead);
            // 导出的文件名
            String fileName = "配件包装储运批量更新模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "导出配件包装储运批量修改模板错误");
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-24
     * @Title : 导出配件采购属批量修改模板 (含配件编码)
     */
    public void expPartPurProTempExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = "";//件号
            String partOldcode = "";//配件编码
            String partCname = "";//配件名称


            String[] head = new String[11];
            head[0] = "配件编码";
            head[1] = "包装尺寸";
            head[2] = "最小包装量";
            head[3] = "整包发运量";

            List<Map<String, Object>> list = dao.partPkgStkList(partCode, partOldcode, partCname);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[11];
                        detail[0] = CommonUtils.checkNull(map
                                .get("PART_OLDCODE"));
                        detail[1] = "";
                        detail[2] = "";
                        detail[3] = "";
                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件采购属批量更新模板";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件包装储运Excel");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 导出配件包装储运Excel
     */
    public void expPartPurProExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //件号
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称


            String[] head = new String[11];
            head[0] = "序号";
            head[1] = "配件编码";
            head[2] = "配件名称";
            head[3] = "件号";
            head[4] = "包装尺寸";
            head[5] = "最小包装量";
            head[6] = "整包发运量";

            List<Map<String, Object>> list = dao.partPkgStkList(partCode, partOldcode, partCname);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[11];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[3] = CommonUtils.checkNull(map
                                .get("PART_CODE"));
                        detail[1] = CommonUtils.checkNull(map
                                .get("PART_OLDCODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("PART_CNAME"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("PKG_SIZE"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("OEM_MIN_PKG"));
                        detail[6] = CommonUtils.checkNull(map
                                .get("MIN_PKG"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件包装储运维护信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件包装储运Excel");
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
