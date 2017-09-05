package com.infodms.dms.actions.report.partReport.partStockReport;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.purchaseOrderManager.PartChargeWeightDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartFixcodeDefinePO;
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

public class PartChargeWeightReport extends BaseImport {

    public Logger logger = Logger.getLogger(PartChargeWeightReport.class);
    private final static String PART_CHARGE_WEIGHT_DETAIL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/partChargeWeightDetail.jsp";
    public ActionContext act = ActionContext.getContext();
    public PartChargeWeightDao dao = PartChargeWeightDao.getInstance();

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-9-10
     * @author 
     * @Description: 配件计费重量明细表初始化
     */
    public void init() {

        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<TtPartFixcodeDefinePO> listf = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_06);// 发运类型
            List<TtPartFixcodeDefinePO> listc = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_08);// 承运物流
            act.setOutData("listf", listf);
            act.setOutData("listc", listc);
            act.setOutData("old",CommonUtils.getBefore(new Date()));
            act.setOutData("now",CommonUtils.getDate());
            act.setForword(PART_CHARGE_WEIGHT_DETAIL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件计费重量明细查询初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-9-10
     * @Title :
     * @Description: 配件计费重量明细表查询
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
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件计费重量明细查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_CHARGE_WEIGHT_DETAIL);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-9-10
     * @Title :
     * @Description: 配件计费重量明细表导出
     */
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
        try {
            List<List<Object>> list = new LinkedList<List<Object>>();

            List<Object> listHead = new LinkedList<Object>();
           
            listHead.add("序号");
            listHead.add("省份");
            listHead.add("城市");
            listHead.add("经销商编号");
            listHead.add("经销商名称");
            listHead.add("发运单号");
            listHead.add("承运商");
            listHead.add("运输方式");
            listHead.add("体积");
            listHead.add("包装尺寸[长(cm)*宽(cm)*高(cm)]");
            listHead.add("箱号");
            listHead.add("实际重量(kg)");
            listHead.add("折合重量(kg)");
            listHead.add("计费重量(kg)");
            listHead.add("备注");
            list.add(listHead);
            
            
            PageResult<Map<String, Object>> ps = dao.queryDatas(request, 1, 999999);
            if (ps != null && ps.getRecords().size() > 0 ) {
                for (int i = 0; i < ps.getRecords().size(); i++){
                    Map<String, Object> map =  ps.getRecords().get(i);
                        List<Object> listRowData = new LinkedList<Object>();
                        //序号留白
                        listRowData.add(i+1);
                        listRowData.add(CommonUtils.checkNull(map.get("PROVINCE")));
                        listRowData.add(CommonUtils.checkNull(map.get("CITY")));
                        listRowData.add(CommonUtils.checkNull(map.get("DEALER_CODE")));
                        listRowData.add(CommonUtils.checkNull(map.get("DEALER_NAME")));
                        listRowData.add(CommonUtils.checkNull(map.get("TRPLAN_CODE")));
                        listRowData.add(CommonUtils.checkNull(map.get("TRANSPORT_ORG")));
                        listRowData.add(CommonUtils.checkNull(map.get("TRANS_TYPE")));
                        listRowData.add(CommonUtils.checkNull(map.get("PKG_NO")));
                        String length = CommonUtils.checkNull(map.get("LENGTH"));
                        String width = CommonUtils.checkNull(map.get("WIDTH"));
                        String height = CommonUtils.checkNull(map.get("HEIGHT"));
                        String pkgSize = length + " * " + width + " * " + height;
                        listRowData.add(pkgSize);
                        listRowData.add(CommonUtils.checkNull(map.get("VOLUME")));
                        listRowData.add(CommonUtils.checkNull(map.get("WEIGHT")));
                        listRowData.add(CommonUtils.checkNull(map.get("EQ_WEIGHT")));
                        listRowData.add(CommonUtils.checkNull(map.get("CH_WEIGHT")));
                        listRowData.add(CommonUtils.checkNull(map.get("REMARK")));
                        list.add(listRowData);
            }
            }
            String fileName = "配件计费重量明细.xls";
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            ResponseWrapper response = act.getResponse();
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();

            CsvWriterUtil.createXlsFile(list, os);

            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "导出配件计费重量明细 EXECEL数据错误");
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
