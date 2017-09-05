package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partPlannerQueryManager.partPlannerQueryDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartHolidayDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartHolidayPo;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;

/**
 * <p>ClassName: PartHolidayManager</p>
 * <p>Description: 配件节假日维护</p>
 * <p>Author: MEpaper</p>
 * <p>Date: 2017年7月13日</p>
 */
public class PartHolidayManager extends BaseImport implements PTConstants {

    public Logger logger = Logger.getLogger(PartAddrManager.class);
    
    private PartHolidayDao dao = PartHolidayDao.getInstance();
    // 配件节假日维护
    String PART_HOLIDAY_QUERY_URL = "/jsp/parts/baseManager/partsBaseManager/partHoliday/partHolidayQuery.jsp"; // 节假日查询页面

    /**
     * <p>Description: 配件节假日初始化</p>
     */
    public void partHolidayQueryInit(){
        ActionContext act = ActionContext.getContext();
        try {
            act.setForword(PART_HOLIDAY_QUERY_URL);
        } catch (Exception e) {
            //异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件节假日维护初始化");
            act.setException(e1);
            act.setForword(PART_HOLIDAY_QUERY_URL);
        }
        
        
    }
    
    /**
     * <p>Description: 配件节假日列表查询</p>
     */
    public void partHolidayQuery(){
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryPartHolidayList(request, logonUser, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            //异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件节假日维护初始化");
            act.setException(e1);
            act.setForword(PART_HOLIDAY_QUERY_URL);
        }
    }
    
    /**
     * <p>Description: 有效/失效节假日</p>
     */
    @SuppressWarnings("unchecked")
    public void disOrEnHoilday(){
        ActionContext act = ActionContext.getContext();
        RequestWrapper req = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String holidayId = CommonUtils.checkNull(req.getParamValue("holidayId"));
            String status = CommonUtils.checkNull(req.getParamValue("actionStatus"));
            String curPage = CommonUtils.checkNull(req.getParamValue("curPage"));
            
            TtPartHolidayPo selPo = new TtPartHolidayPo();
            selPo.setHolidayId(Long.parseLong(holidayId));
            
            TtPartHolidayPo updatePo = new TtPartHolidayPo();
            updatePo.setStatus(Integer.valueOf(status));
            updatePo.setUpdateBy(logonUser.getUserId());
            updatePo.setUpdateDate(new Date());
            dao.update(selPo, updatePo);
            if(updatePo.getStatus().equals(Constant.STATUS_DISABLE)){
                act.setOutData("success", "失效成功!");
            }else{
                act.setOutData("success", "设置成功!");
            }
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            //异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件节假日维护初始化");
            act.setException(e1);
            act.setForword(PART_HOLIDAY_QUERY_URL);
        }
    }
    
    /**
     * <p>Description: 导出节假日到Excel</p>
     */
    public void expHolidayExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        ResponseWrapper response = act.getResponse();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
        try {

            List<Object> headList = new ArrayList<Object>();
            headList.add("节假日日期");
            headList.add("状态");
            List<Map<String, Object>> list = dao.getPartHolidayList(request, logonUser);
            List<List<Object>> expList = new ArrayList<List<Object>>();
            expList.add(headList);
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    if (map != null && map.size() != 0) {
                        List<Object> bodyList = new ArrayList<Object>();
                        bodyList.add(CommonUtils.checkNull(map.get("HOLIDAY_DATE")));

                        int statusValue = 0;
                        String statusStr = CommonUtils.checkNull(map.get("STATUS"));
                        if(StringUtils.isNotEmpty(statusStr)){
                            statusValue = Integer.parseInt(statusStr); 
                        }
                        if (Constant.STATUS_ENABLE.intValue() == statusValue) {
                            statusStr = "有效";
                        } else {
                            statusStr = "无效";
                        }
                        bodyList.add(statusStr);
                        expList.add(bodyList);
                    }
                }
            }
            String fileName = "配件节假日.xls";
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(expList, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件节假日Excel");
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
     * <p>Description: 导出节假日Excel模板</p>
     */
    public void exportExcelTemplate(){
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        ResponseWrapper response = act.getResponse();
        OutputStream os = null;
        try {
            List<List<Object>> list = new LinkedList<List<Object>>();
            List<Object> listHead = new LinkedList<Object>();
            //导出模板第一列
            listHead.add("节假日日期");
            listHead.add("状态（1：有效，0：无效）");
            list.add(listHead);
            // 导出的文件名
            String fileName = "配件节假日模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
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
     * <p>Description: 导入节假日Excel</p>
     */
    public void impHolidayExcel(){
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer errorInfo = new StringBuffer("");
        RequestWrapper request = act.getRequest();
        try {
            long maxSize = 1024 * 1024 * 5;
            int errNum = 0;
            errNum = insertIntoTmp(request, "uploadExcelFile", 2, 0, maxSize);
            String err = "";
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
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
                throw e1;
            } else {
                List<Map> list = getMapList();
                List<Map<String, String>> voList = new ArrayList<Map<String, String>>();
                for(int i = 0; i < list.size(); i++){
                    Map map = list.get(i);
                    if(map == null){
                        map = new HashMap<String, Cell[]>();
                    }
                    Set<String> keys = map.keySet();
                    Iterator it = keys.iterator();
                    String key = "";
                    while (it.hasNext()) {
                        key = (String) it.next();
                        Cell[] cells = (Cell[]) map.get(key);
                        
                        // 验证节假日日期
                        String holidayDateStr = cells[0].getContents().trim();
                        if(CommonUtils.isEmpty(holidayDateStr)){
                            errorInfo.append("第" + (Integer.parseInt(key) - 1) + "行节假日日期错误！");
                            break;
                        }

                        // 验证日期格式
                        Date holidayDate = null;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        if(cells[0].getType() == CellType.DATE){
                            DateCell dc = (DateCell) cells[0];
                            holidayDate = dc.getDate();
                            holidayDateStr = sdf.format(holidayDate);
                        }else{
                            errorInfo.append("第" + (Integer.parseInt(key) - 1) + "行节假日日期格式错误！");
                            break;
                        }
                        // 验证节假日是否已存在
                        Map<String, String> paramMaps = new HashMap<String, String>();
                        paramMaps.put("holidayDate", holidayDateStr);
                        if(dao.getPartHolidayCount(paramMaps) > 0){
                            errorInfo.append("第" + (Integer.parseInt(key) - 1) + "行节假日日期已存在！");
                            break;
                        }
                        
                        
                        // 验证状态值
                        String statusStr = cells[1].getContents().trim();
                        if(CommonUtils.isEmpty(statusStr)){
                            errorInfo.append("第" + (Integer.parseInt(key) - 1) + "行状态值错误！");
                            break;
                        }
                        Integer status = 0;
                        if(Integer.parseInt(statusStr) == 1){
                            status = Constant.STATUS_ENABLE;
                        }else if(Integer.parseInt(statusStr) == 0){
                            status = Constant.STATUS_DISABLE;
                        }else{
                            errorInfo.append("第" + (Integer.parseInt(key) - 1) + "行状态值错误！");
                            break;
                        }
                        
                        // 插入节假日记录
                        TtPartHolidayPo po = new TtPartHolidayPo();
                        po.setHolidayId(Long.parseLong(SequenceManager.getSequence("")));
                        po.setHolidayDate(holidayDate);
                        po.setStatus(status);
                        po.setCreateDate(new Date());
                        dao.insert(po);
                    }
                }
                
                act.setOutData("flag", "导入成功");
                act.setOutData("impExcelErrorInfo", errorInfo);
                act.setForword(PART_HOLIDAY_QUERY_URL);
            }

        } catch (Exception e) {
            BizException e1 = null;
            e.printStackTrace();
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("flag", "导入失败");
            act.setForword(PART_HOLIDAY_QUERY_URL);
        }

    }

    
}
