package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartSalesRelationDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtPartSalesRelationPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author : luole CreateDate : 2013-4-9
 * @ClassName : PartSalesRelation
 * @Description : 配件采购关系
 */
public class PartSalesRelation extends PartBaseImport {
    private final String PART_SALES_RELATION_URL = "/jsp/parts/baseManager/partsBaseManager/partSalesRelation.jsp";   // 配件采购关系首页
    private final String PART_SALES_SEL_URL = "/jsp/parts/baseManager/partsBaseManager/partSalesRelationSel.jsp"; // 配件采购关系查看
    private final String PART_SALES_SUN_URL = "/jsp/parts/baseManager/partsBaseManager/partSalesRelationSun.jsp"; // 配件采购关系维护

    PartSalesRelationDao dao = PartSalesRelationDao.getInstance();
    private Logger logger = Logger.getLogger(PartSalesRelation.class);
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "配件采购关系.xls";
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
                     if(CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])){
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                    }else{
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
     * @throws : luole LastDate : 2013-4-9
     * @Title : partSalesRelationInit
     * @Description: 配件采购关系维护-初始化
     */
    public void partSalesRelationInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_SALES_RELATION_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件采购关系初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-9
     * @Title : 添加
     * @Description: TODO
     */
    public void saveSalerRelation() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String fatherId = CommonUtils.checkNull(request.getParamValue("FATHER_ID"));
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//下级单位编码
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//下级单位名称
            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_IDS"));//下级单位id
            String fatherCode = "";
            String fatherName = "";
            TmDealerPO tmDealerPO = new TmDealerPO();
            tmDealerPO.setDealerId(CommonUtils.parseLong(fatherId));
            List list2 = dao.select(tmDealerPO);
            if (list2.size() > 0) {
                tmDealerPO = (TmDealerPO) list2.get(0);
                fatherCode = tmDealerPO.getDealerCode();
                fatherName = tmDealerPO.getDealerName();
            } else {
                TmCompanyPO tmOrgPO = new TmCompanyPO();
                tmOrgPO.setCompanyId(CommonUtils.parseLong(fatherId));
                List list1 = dao.select(tmOrgPO);
                if (list1.size() > 0) {
                    tmOrgPO = (TmCompanyPO) list1.get(0);
                    fatherCode = tmOrgPO.getCompanyCode();
                    fatherName = tmOrgPO.getCompanyName();
                }
            }

            String[] ids = dealerId.split(",");
            String[] codes = dealerCode.split(",");
            String[] names = dealerName.split(",");
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    if (dao.checkRelation(fatherId, ids[i])) {
                        act.setOutData("error", "【" + fatherName + "】与【" + names[i] + "】的采购关系已经存在,请重新选择!");
                        return;
                    } else {
                        //在新增之前还要判断一个机构(A)是否既是另一个机构(B)的上级,又是另一个机构(B)的下级
                        Map<String, Object> map = dao.isMeanwhile(codes[i], fatherCode);
                        if (map != null) {//如果同时存在上下级关系
                            act.setOutData("error", "【" + fatherName + "】不能既是【" + names[i] + "】的上级又是其下级,请重新选择!");
                            return;
                        } else {
                            TtPartSalesRelationPO po = new TtPartSalesRelationPO();
                            po.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
                            po.setParentorgId(Long.parseLong(fatherId));
                            po.setParentorgCode(fatherCode);
                            po.setParentorgName(fatherName);
                            po.setChildorgId(Long.parseLong(ids[i]));

                            TmDealerPO tpo = new TmDealerPO();
                            tpo.setDealerId(po.getChildorgId());
                            List<PO> list = dao.select(tpo);
                            tpo = (TmDealerPO) list.get(0);
                            String dName = tpo.getDealerName();

                            po.setChildorgName(dName);
                            po.setChildorgCode(codes[i]);
                            po.setCreateBy(logonUser.getUserId());
                            po.setCreateDate(new Date());
                            dao.insert(po);
                        }

                    }
                }

            }
            act.setOutData("success", "添加成功!");
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "配件采购关系");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-10
     * @Title : 配件采购关系维护-查看下级分布查询
     * @Description: TODO
     */
    public void salerChildQuery() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String fatherId = request.getParamValue("FATHER_ID"); // 件号
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
            
            
            StringBuffer sql = new StringBuffer();
            if (!CommonUtils.isNullString(fatherId)) {
                sql.append(" and  p. PARENTORG_ID = " + fatherId);
            }
            if (!CommonUtils.isNullString(dealerCode)) {
                sql.append(" and  p.CHILDORG_CODE = '" + dealerCode +"'");
            }
            if (!CommonUtils.isNullString(dealerName)) {
                sql.append(" and  p.CHILDORG_NAME LIKE '%"+dealerName+"%' ");
            }
            System.out.println("配件采购关系维护-查看下级分布查询 sql:"+sql);
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.selPartPageQuery(sql.toString(), curPage, Constant.PAGE_SIZE); // 在查看下级的时候每页显示50条
            // 或更多
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件货位维护新增页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-9
     * @Title : 查看下级
     * @Description: TODO
     */
    public void selRel() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            String relId = CommonUtils.checkNull(request.getParamValue("Id")); // ID
            TtPartSalesRelationPO po = new TtPartSalesRelationPO();
            po.setRelationId(Long.parseLong(relId));
            List<PO> templist = dao.select(po);
            if (templist != null)
                po = (TtPartSalesRelationPO) templist.get(0);
            act.setOutData("fatherId", po.getChildorgId());
            act.setOutData("fatherCode", po.getChildorgCode());
            act.setOutData("fatherName", po.getChildorgName());
            act.setOutData("curPage", curPage);
            act.setForword(PART_SALES_SEL_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件采购关系初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-9
     * @Title : 维护下级初始化
     * @Description: TODO
     */
    public void selSun() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String relId = CommonUtils.checkNull(request.getParamValue("Id")); // ID
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));
            TtPartSalesRelationPO po = new TtPartSalesRelationPO();
            po.setRelationId(Long.parseLong(relId));
            List<PO> templist = dao.select(po);
            if (templist != null)
                po = (TtPartSalesRelationPO) templist.get(0);
            act.setOutData("fatherId", po.getChildorgId());
            act.setOutData("fatherCode", po.getChildorgCode());
            act.setOutData("fatherName", po.getChildorgName());
            act.setForword(PART_SALES_SUN_URL);
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("curPage", curPage);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件采购关系初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-9
     * @Title : partQuery
     * @Description:  配件采购关系维护-查询
     */
    public void partQuery() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.getResponse().setContentType("application/json");
            String fName = request.getParamValue("FATHER_NAME"); // 上级名称
            String sName = request.getParamValue("SUN_NAME"); // 下级名称
            String state = request.getParamValue("STATE"); // 状态
            StringBuffer sql = new StringBuffer();
            if (!CommonUtils.isNullString(fName)) {
                sql.append(" and  p. PARENTORG_NAME like '%" + fName + "%' ");
            }
            if (!CommonUtils.isNullString(sName)) {
                sql.append(" and  p.CHILDORG_NAME  like '%" + sName + "%' ");
            }
            if (!CommonUtils.isNullString(state)) {
                sql.append("  and p.STATE  =  " + state);
            }
            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = dao.selPartPageQuery(sql.toString(), curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件采购关系");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-9
     * @Title : 失效
     * @Description: TODO
     */
    public void partNotState() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String relId = CommonUtils.checkNull(request.getParamValue("Id")); //
            String pId = CommonUtils.checkNull(request.getParamValue("pId")); //上级单位id
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));
            TtPartSalesRelationPO po = new TtPartSalesRelationPO();
            po.setRelationId(Long.parseLong(relId));
            TtPartSalesRelationPO po1 = new TtPartSalesRelationPO();
            po1.setState(Constant.STATUS_DISABLE);
            po1.setDisableBy(logonUser.getUserId());
            po1.setDisableDate(new Date());
            dao.update(po, po1);
            //还需要把该单位的所有下级单位都失效
            dao.updateChildOrg(pId, 0);
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "失效成功");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件仓库维护查询页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-9
     * @Title : 有效
     * @Description: TODO
     */
    public void partEnableState() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String relId = CommonUtils.checkNull(request.getParamValue("Id")); //
            String pId = CommonUtils.checkNull(request.getParamValue("pId")); //上级单位id
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));
            if ("".equals(curPage)) {
                curPage = "1";
            }
            TtPartSalesRelationPO po = new TtPartSalesRelationPO();
            po.setRelationId(Long.parseLong(relId));
            TtPartSalesRelationPO po1 = new TtPartSalesRelationPO();
            po1.setState(Constant.STATUS_ENABLE);
            dao.update(po, po1);
            //需要把该单位的所有下级单位设置为有效
            dao.updateChildOrg(pId, 1);
            act.setOutData("success", "设置有效成功");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购关系设置有效失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }


    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-24
     * @Title :
     * @Description: 删除
     */
    public void deleteRelation() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String relId = CommonUtils.checkNull(request.getParamValue("Id")); //
            String pId = CommonUtils.checkNull(request.getParamValue("pId")); //上级单位id
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));
            if ("".equals(curPage)) {
                curPage = "1";
            }
            TtPartSalesRelationPO po = new TtPartSalesRelationPO();
            po.setRelationId(Long.parseLong(relId));
            dao.delete(po);
            //需要把该单位的所有下级单位删除
            dao.deleteChildOrg(pId);
            act.setOutData("success", "删除成功");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "删除失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-24
     * @Title :
     * @Description: 导出
     */
    public void exportPartSRelationExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[6];
            head[0] = "父机构单位代码";
            head[1] = "父机构单位名称";
            head[2] = "子机构单位代码";
            head[3] = "子机构单位名称";
            head[4] = "是否有效";
            List<Map<String, Object>> list = dao.queryPartSRelation(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[6];
                        detail[0] = CommonUtils.checkNull(map.get("PARENTORG_CODE"));
                        detail[1] = CommonUtils.checkNull(map
                                .get("PARENTORG_NAME"));
                        detail[2] = CommonUtils
                                .checkNull(map.get("CHILDORG_CODE"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("CHILDORG_NAME"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("STATE"));
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1);
            } else {
                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
                        "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_SALES_RELATION_URL);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-24
     * @Title :
     * @Description: 下载采购关系模板
     */
    public void exportPartRelationTemp() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();

            // 用于下载传参的集合
            List<List<Object>> list = new LinkedList<List<Object>>();

            // 标题
            List<Object> listHead = new LinkedList<Object>();// 导出模板第一行
            listHead.add("下级单位编码");
            listHead.add("下级单位名称");
            listHead.add("上级单位编码");
            listHead.add("上级单位名称");

            list.add(listHead);
            // 导出的文件名
            String fileName = "配件采购关系模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename="
                    + fileName);

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
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
     * @Title :
     * @Description: 导入采购关系
     */
    public void uploadPartRelExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer errorInfo = new StringBuffer("");
        RequestWrapper request = act.getRequest();
        try {
            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmp(request, "uploadFile", 4, 3, maxSize);

            String err = "";

            if (errNum != 0) {
                switch (errNum) {
                    case 1:
                        err += "文件列数过多,请修改后再上传!";
                        break;
                    case 2:
                        err += "空行不能大于三行,请修改后再上传!";
                        break;
                    case 3:
                        err += "文件内容不能为空,请修改后再上传!";
                        break;
                    case 4:
                        err += "文件类型错误,请重新上传!";
                        break;
                    case 5:
                        err += "文件不能大于" + maxSize + ",请修改后再上传";
                        break;
                    default:
                        break;
                }
            }

            if (!"".equals(err)) {
                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, err);
                throw e1;
            } else {
                List<Map> list = getMapList();
                List voList = new ArrayList();
                loadVoList(voList, list, errorInfo);
                if (errorInfo.length() > 0) {

                    BizException e1 = new BizException(act,
                            ErrorCodeConstant.SPECIAL_MEG, errorInfo);
                    throw e1;
                }
                for (int i = 0; i < voList.size(); i++) {
                    TtPartSalesRelationPO salesRelationPO = (TtPartSalesRelationPO) voList
                            .get(i);

                    //判断采购关系是否已经存在
                    boolean flag = dao.isExist(
                            salesRelationPO.getChildorgCode(),
                            salesRelationPO.getParentorgCode());
                    if (flag) {// 如果已经存在
                        errorInfo.append("第" + (i + 2) + "行的下级编码与上级编码对应的采购关系已经存在,请修改后再上传!<br>");
                    } else {// 否则新增
                        //在新增之前还要判断一个机构(A)是否既是另一个机构(B)的上级,又是另一个机构(B)的下级

                        Map<String, Object> map = dao.isMeanwhile(salesRelationPO.getChildorgCode(), salesRelationPO.getParentorgCode());
                        if (map != null) {//如果同时存在上下级关系
                            errorInfo.append("第" + (i + 2) + "行的【" + salesRelationPO.getChildorgCode() + "】不能既是【" + salesRelationPO.getParentorgCode() + "】的上级又是其下级,请修改后再上传!<br>");
                        }
                    }

                    if (errorInfo.length() > 0) {
                        BizException e1 = new BizException(act,
                                ErrorCodeConstant.SPECIAL_MEG, errorInfo);
                        throw e1;
                    }

                    salesRelationPO.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
                    salesRelationPO.setCreateDate(new Date());
                    salesRelationPO.setCreateBy(logonUser.getUserId());
                    dao.insert(salesRelationPO);
                }
                act.setForword(PART_SALES_RELATION_URL);
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
                        "文件读取错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_SALES_RELATION_URL);
        }
    }

    /**
     * @param : @param voList
     * @param : @param list
     * @param : @param errorInfo
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-7
     * @Title :
     * @Description: 循环获取cell
     */
    private void loadVoList(List voList, List<Map> list, StringBuffer errorInfo)
            throws Exception {
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
                parseCells(voList, key, cells, errorInfo);
                if (errorInfo.length() > 0) {
                    break;
                }
            }
        }

    }

    /**
     * 装载VO
     *
     * @param : @param list
     * @param : @param rowNum
     * @param : @param cells
     * @param : @param errorInfo
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-7
     * @Title :
     * @Description: 
     */
    private void parseCells(List list, String rowNum, Cell[] cells,
                            StringBuffer errorInfo) throws Exception {

        TtPartSalesRelationPO salesRelationPO = new TtPartSalesRelationPO();

        if ("" == subCell(cells[0].getContents().trim())) {
            errorInfo.append("第" + rowNum + "行的下级单位编码不能为空,请修改后再上传!");
            return;
        }
        if ("" == subCell(cells[2].getContents().trim())) {
            errorInfo.append("第" + rowNum + "行的上级单位编码不能为空,请修改后再上传!");
            return;
        }

        //如果下级单位编码与上级单位编码相同就提示错误
        if (subCell(cells[0].getContents().trim()).equals(subCell(cells[2].getContents().trim()))) {
            errorInfo.append("第" + rowNum + "行的下级单位编码与上级编码不能相同,请修改后再上传!");
            return;
        }
        Map<String, Object> map = null;
        map = dao.validateChildOrgcode(subCell(cells[0].getContents().trim()));
        if (map == null) {
            errorInfo.append("第" + rowNum + "行的下级单位编码不存在,请修改后再上传!");
            return;
        }

        Long dealerId = ((BigDecimal) map.get("DEALER_ID")).longValue();
        salesRelationPO.setChildorgId(dealerId);
        salesRelationPO.setChildorgCode((String) map.get("DEALER_CODE"));
        salesRelationPO.setChildorgName((String) map.get("DEALER_NAME"));

        map = dao.validateParentOrgCode(subCell(cells[2].getContents().trim()));
        if (map == null) {
            errorInfo.append("第" + rowNum + "行的上级单位编码不存在,请修改后再上传!");
            return;
        }

        salesRelationPO.setParentorgId(((BigDecimal) map.get("COMPANY_ID")).longValue());
        salesRelationPO.setParentorgCode((String) map.get("COMPANY_CODE"));
        salesRelationPO.setParentorgName((String) map.get("COMPANY_NAME"));

        list.add(salesRelationPO);
    }

    /**
     * 截取字符串
     *
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-7
     * @Title :
     * @Description: TODO
     */
    private String subCell(String orgAmt) {
        String newAmt = "";
        if (null == orgAmt || "".equals(orgAmt)) {
            return newAmt;
        }
        if (orgAmt.length() > 30) {
            newAmt = orgAmt.substring(0, 30);
        } else {
            newAmt = orgAmt;
        }
        return newAmt;
    }
}
