package com.infodms.dms.actions.parts.storageManager.partLocation;

import com.infodms.dms.actions.parts.storageManager.partShelfMgr.PartShelfMgr;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.storageManager.partLocationMgr.PartLocationMgrDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtLoaction_historyPO;
import com.infodms.dms.po.TtPartLocationPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class PartLocation {
    public Logger logger = Logger.getLogger(PartShelfMgr.class);
    private final PartLocationMgrDao plmd = PartLocationMgrDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();

    private final String PART_LOCATION_MANAGE = "/jsp/parts/storageManager/partLocationMgr/partLocationMgr.jsp";
    private final String PART_LOCATION_ADD = "/jsp/parts/storageManager/partLocationMgr/partLocationAdd.jsp";

    public void init() {
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orgId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList && beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(orgId);
            request.setAttribute("wareHouseList", wareHouseList);
            act.setForword(PART_LOCATION_MANAGE);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "货位维护--初始化页面失败");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void query() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String searchType = request.getParamValue("searchType"); // 查询类型
            String loc_code = request.getParamValue("LOC_CODE");
            String loc_name = request.getParamValue("LOC_NAME");
            String whId = request.getParamValue("WH_ID");
            String state = request.getParamValue("STATE");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("LOC_CODE", loc_code);
            map.put("LOC_NAME", loc_name);
            map.put("STATE", state);
            map.put("WH_ID", whId);

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            PageResult<Map<String, Object>> ps = null;
            if ("normal".equals(searchType)) {
                ps = plmd.queryData(map, curPage, Constant.PAGE_SIZE);
            } else {
                ps = plmd.queryUpdateLog(map, curPage, Constant.PAGE_SIZE);
            }
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "货位维护--数据信息查询失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void partLocationAdd() {
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orgId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList && beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(orgId);
            request.setAttribute("wareHouseList", wareHouseList);
            act.setForword(PART_LOCATION_ADD);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "货位维护--新增初始化页面失败");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void partLocationAddSave() {
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String success = "";
        String error = "";
        try {
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String locCode = CommonUtils.checkNull(request.getParamValue("LOC_CODE"));
            //判断新增的货位编码是否存在
            if (!plmd.isRepeatLOC_CODE(locCode, whId)) {
                TtPartLocationPO tplp = new TtPartLocationPO();
                String locId = SequenceManager.getSequence(null);
                tplp.setLocId(Long.parseLong(locId));
                tplp.setWhId(Long.valueOf(whId));
                tplp.setLocCode(locCode);
                tplp.setLocName(locCode);
                tplp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
                tplp.setCreateBy(loginUser.getUserId());
                tplp.setStatus(Long.parseLong("1"));
                tplp.setState(Constant.STATUS_ENABLE);
                plmd.insert(tplp);
                success = "新增货位编码成功";
            } else {
                error = "新增货位编码存在";
            }
            act.setOutData("error", error);
            act.setOutData("success", success);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "货位维护--新增保存失败");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @Title : 货位 -> 导入文件
     * @author : wucl
     * LastDate    : 2014-3-10
     */
    public void locImpUpload() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        String whId = request.getParamValue("WH_ID_IMP"); // 仓库ID
        FileObject uploadFile = request.getParamObject("uploadFile");//获取导入文件
        if (uploadFile == null) {//文件为空报空指针异常

            return;
        }
        String fileName = uploadFile.getFileName();//获取文件名
        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());//截取文件名
        ByteArrayInputStream inputStream = new ByteArrayInputStream(uploadFile.getContent());//获取文件数据

        Workbook wb = null;
        List<PO> addListtplp = new ArrayList<PO>();
        try {
            wb = Workbook.getWorkbook(inputStream);
            inputStream.reset();
            Sheet[] sheets = wb.getSheets();
            Sheet sheet = sheets[0];
            int rowNum = sheet.getRows();
            for (int j = 1; j < rowNum; j++) {
                Cell[] cells = sheet.getRow(j);
                String code = cells[0].getContents().trim();
                //String codeName = cells[1].getContents().trim();
                if (!plmd.isRepeatLOC_CODE(code, whId)) {
                    TtPartLocationPO tplp = new TtPartLocationPO();
                    String locId = SequenceManager.getSequence(null);
                    tplp.setLocId(Long.parseLong(locId));

//					String[] strs = code.split("-");//导入货位编码自动关联系统货位编码
//					if(strs.length == 4){
//						String positionId = plmd.getPositionId(strs[0], strs[1], strs[2], strs[3]);
//						tplp.setPositionId(positionId!=null?Long.parseLong(positionId):null);
//					}
                    tplp.setWhId(Long.valueOf(whId));
                    tplp.setLocCode(code);
                    tplp.setLocName(code);
                    tplp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
                    tplp.setCreateBy(logonUser.getUserId());
                    tplp.setStatus(Long.parseLong("1"));
                    tplp.setState(Constant.STATUS_ENABLE);
                    addListtplp.add(tplp);
                }
            }
            plmd.insert(addListtplp);
            init();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "货位 -> 批量导入货位数据失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @Title : 货位导入批量更新
     * @author : wucl
     * LastDate    : 2014-3-10
     */
    public void impUpdateUpload() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        String whId = request.getParamValue("WH_ID_UPD"); // 仓库ID
        FileObject uploadFile = request.getParamObject("uploadFileUpdate");//获取导入文件
        if (uploadFile == null) {//文件为空报空指针异常
            return;
        }
        String fileName = uploadFile.getFileName();//获取文件名
        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());//截取文件名
        ByteArrayInputStream inputStream = new ByteArrayInputStream(uploadFile.getContent());//获取文件数据

        Workbook wb = null;
        try {
            wb = Workbook.getWorkbook(inputStream);
            inputStream.reset();
            Sheet[] sheets = wb.getSheets();
            Sheet sheet = sheets[0];
            int rowNum = sheet.getRows();
            for (int j = 1; j < rowNum; j++) {
                Cell[] cells = sheet.getRow(j);
                String oldcode = cells[0].getContents().trim();
                String newcode = cells[1].getContents().trim();
                if (newcode != oldcode && !plmd.isRepeatLOC_CODE(newcode, whId)) {
                    //1.更新货位信息
                    TtPartLocationPO t1 = new TtPartLocationPO();
                    t1.setLocCode(oldcode);
                    t1.setWhId(Long.valueOf(whId));
                    List<PO> list = plmd.select(t1);
                    boolean flag = plmd.updateLocCode(oldcode, newcode, whId);
                    //2.插入更新历史
                    if (flag) {
                        if (list != null && list.size() > 0 && list.get(0) != null) {
                            t1 = (TtPartLocationPO) list.get(0);
                            TtLoaction_historyPO lhp = new TtLoaction_historyPO();
                            String hsId = SequenceManager.getSequence(null);
                            lhp.setHsId(Long.parseLong(hsId));
                            lhp.setLocId(t1.getLocId());
                            lhp.setOldLocCode(t1.getLocCode());
                            lhp.setOldLocName(t1.getLocName());
                            lhp.setLocCode(newcode);
                            lhp.setLocName(t1.getLocName());
                            lhp.setOrgId(logonUser.getOrgId());
                            lhp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
                            lhp.setCreateBy(logonUser.getUserId());
                            lhp.setStatus(Constant.STATUS_ENABLE);
                            plmd.insert(lhp);
                        }
                    }
                }
            }
            init();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "货位 -> 批量更新货位数据失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @author wucl
     * @Title : 货位批量导入模板
     * LastDate    : 2014-3-7
     */
    public void exportExcelTemplate() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();
            List<List<Object>> list = new LinkedList<List<Object>>();
            List<Object> listHead = new LinkedList<Object>();
            listHead.add("货位编码 ");
            //listHead.add("货位名称 ");
            list.add(listHead);
            String fileName = "货位批量导入模板.xls";
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "导出货位批量导入 EXECEL模板错误");
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
     * @author wucl
     * @Title : 批量更新模板
     * LastDate    : 2014-3-7
     */
    public void exportUpdateTemplate() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
        try {
            String whId = request.getParamValue("WH_ID"); // 仓库ID
            List<Map<String, Object>> partLocation = plmd.queryImportLocation(whId);
            if (partLocation == null || partLocation.size() <= 0) {
                act.setOutData("returnValue", 3);
                return;
            }
            ResponseWrapper response = act.getResponse();
            List<List<Object>> list = new LinkedList<List<Object>>();
            List<Object> listHead = new LinkedList<Object>();
            listHead.add("旧货位编码");
            listHead.add("新货位编码");
            list.add(listHead);
            for (Map<String, Object> map : partLocation) {
                String loc_code = map.get("LOC_CODE").toString();
                List<Object> row = new LinkedList<Object>();
                row.add(loc_code);
                row.add(loc_code);
                list.add(row);
            }
            String fileName = "货位批量更新模板.xls";
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "导出批量更新 EXECEL模板错误");
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

    public void modifyLocCode() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String old_loc_code = request.getParamValue("OLD_LOC_CODE").trim();
        String new_loc_code = request.getParamValue("NEW_LOC_CODE").trim();
        String whId = request.getParamValue("WH_ID").trim();
        try {
            TtPartLocationPO tp1 = new TtPartLocationPO();
            TtPartLocationPO tp2 = new TtPartLocationPO();
            tp1.setLocCode(old_loc_code);
            tp2.setLocCode(new_loc_code);

            List<PO> list = plmd.select(tp2);
            if (list != null && list.size() > 0 && list.get(0) != null) {
                act.setOutData("returnValue", 2);
                act.setOutData("OLD_LOC_CODE", old_loc_code);//该货位编码存在
            } else {
                plmd.updateLocCode(old_loc_code, new_loc_code, whId);
                act.setOutData("returnValue", 1);
            }
        } catch (Exception e) {
            act.setOutData("returnValue", 2);
            act.setOutData("OLD_LOC_CODE", old_loc_code);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "货位维护--修改货位编码异常");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

}
