package com.infodms.dms.actions.report.partReport.partStockReport;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.purchaseOrderManager.InventoryOperationDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartTransPlanPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InventoryOperationReport extends BaseImport {

    public Logger logger = Logger.getLogger(InventoryOperationReport.class);
    private final static String PART_FREIGHT_SUMMARY = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/inventoryOperation.jsp";

    public ActionContext act = ActionContext.getContext();
    public InventoryOperationDao dao = InventoryOperationDao.getInstance();

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-9-10
     * @author
     * @Description: 库存运行情况初始化
     */
    public void init() {

        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
        	List list = dao.getPartWareHouseList(loginUser);// 获取配件库房信息
            act.setOutData("old",CommonUtils.getMonthFirstDay());
            act.setOutData("now",CommonUtils.getDate());
            act.setOutData("wareHouses", list);
            act.setForword(PART_FREIGHT_SUMMARY);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "库存运行情况查询初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-9-10
     * @Title :
     * @Description: 库存运行情况查询
     */
    public void queryData() {
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryDatas(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "库存运行情况查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_FREIGHT_SUMMARY);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-9-10
     * @Title :
     * @Description: 库存运行情况导出
     */
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
        try {
            List<List<Object>> list = new LinkedList<List<Object>>();

            List<Object> listHead = new LinkedList<Object>();


            //listHead.add("序号");
            
            
           
           
            listHead.add("期初库存品种数");
            listHead.add("发运品种数");
            listHead.add("退货品种数");
            listHead.add("期末库存品种数");

            listHead.add("期初库存数量");
            listHead.add("发运数量");
            listHead.add("退货数量");
            listHead.add("期末数量");
            
            listHead.add("期初库存金额");
            listHead.add("发出金额");
            listHead.add("退货金额");
            listHead.add("期末金额");
            list.add(listHead);


            PageResult<Map<String, Object>> ps = dao.queryDatas(request, 1, 999999);
            if (ps != null && ps.getRecords().size() > 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map<String, Object> map = ps.getRecords().get(i);
                    List<Object> listRowData = new LinkedList<Object>();
                    //listRowData.add(i + 1);
                    
                    listRowData.add(CommonUtils.checkNull(map.get("QC_PARTTYPES")));
                    listRowData.add(CommonUtils.checkNull(map.get("FY_PARTTYPES")));
                    listRowData.add(CommonUtils.checkNull(map.get("TH_PARTTYPES")));
                    listRowData.add(CommonUtils.checkNull(map.get("QM_PARTTYPES")));
                    
                    listRowData.add(CommonUtils.checkNull(map.get("QC_PART_QTYS")));
                    listRowData.add(CommonUtils.checkNull(map.get("FY_PART_QTYS")));
                    listRowData.add(CommonUtils.checkNull(map.get("TH_PART_QTYS")));
                    listRowData.add(CommonUtils.checkNull(map.get("QM_PART_QTYS")));
                    
                    listRowData.add(CommonUtils.checkNull(map.get("QC_PART_AMOUNTS")));
                    listRowData.add(CommonUtils.checkNull(map.get("FY_PART_AMOUNTS")));
                    listRowData.add(CommonUtils.checkNull(map.get("TH_PART_AMOUNTS")));
                    listRowData.add(CommonUtils.checkNull(map.get("QM_PART_AMOUNTS")));
                    list.add(listRowData);


                }
            }
            String fileName = "库存运行情况.xls";
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            ResponseWrapper response = act.getResponse();
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();

            CsvWriterUtil.createXlsFile(list, os);

            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "导出库存运行情况 EXECEL数据错误");
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

}
