package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.PartPickOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartPkgBoxDtlPO;
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

public class PartCarConfirm extends BaseImport {

    public Logger logger = Logger.getLogger(PartCarConfirm.class);
    private static final String PART_CAR_CONFIRM_URL = "/jsp/parts/salesManager/carFactorySalesManager/partCarConfirm.jsp";

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list,
                                  String tableName) throws Exception {

        String name = tableName + ".xls";
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
                    /* ws.addCell(new Label(i, z, str[i])); */// modify by yuan
                    if (CheckUtil.checkFormatNumber1(str[i] == null ? ""
                            : str[i])) {
                        ws.addCell(new jxl.write.Number(i, z, Double
                                .parseDouble(str[i])));
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


    public void init() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        RequestWrapper request = act.getRequest();
        try {
            String orgId = "";
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(orgId);
            request.setAttribute("wareHouseList", wareHouseList);
            act.setForword(PART_CAR_CONFIRM_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件随车确认失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }


    public void queryPartConfirm() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartPickOrderDao dao = PartPickOrderDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryPartConfirm(request, curPage, Constant.PAGE_SIZE_MIDDLE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }


    public void confirmCar() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPickOrderDao dao = PartPickOrderDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String assNo = CommonUtils.checkNull(request.getParamValue("assNo"));
            //更新随车箱子的状态为随车确认
            TtPartPkgBoxDtlPO boxDtlPO1 = new TtPartPkgBoxDtlPO();
            TtPartPkgBoxDtlPO boxDtlPO2 = new TtPartPkgBoxDtlPO();
            boxDtlPO1.setPickOrderId(pickOrderId);
            boxDtlPO1.setAssNo(assNo);
            boxDtlPO2.setState(Constant.CAR_FACTORY_PKG_STATE_04);
            boxDtlPO2.setConfirmDate(new Date());
            boxDtlPO2.setConfirmBy(loginUser.getUserId());
            //更新销售单的状态
        /*	TtPartSoMainPO po1 = new TtPartSoMainPO();
        	TtPartSoMainPO po2 = new TtPartSoMainPO();
        	po1.setPickOrderId(pickOrderId);
        	po2.setState(Constant.CAR_FACTORY_PKG_STATE_04);*/

            dao.update(boxDtlPO1, boxDtlPO2);
            //dao.update(po1, po2);

            act.setOutData("success", "确认成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件随车确认失败!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }


    public void exportPartCarExcel() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        PartPickOrderDao dao = PartPickOrderDao.getInstance();
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[8];
            head[0] = "销售单号";
            head[1] = "订货单位";
            head[2] = "拣货单号";
            head[3] = "订单类型";
            head[4] = "出库仓库";
            head[5] = "随车箱号";
            head[6] = "未随车箱号";
            head[7] = "整车发运计划单号";
            List<Map<String, Object>> list = dao.queryPartCar(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[8];
                        detail[0] = CommonUtils.checkNull(map
                                .get("SO_CODE"));
                        detail[1] = CommonUtils
                                .checkNull(map.get("DEALER_NAME"));
                        detail[2] = CommonUtils.checkNull(map.get("PICK_ORDER_ID"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("ORDER_TYPE"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("WH_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("PKG_NO1"));
                        detail[6] = CommonUtils.checkNull(map.get("PKG_NO2"));
                        detail[7] = CommonUtils.checkNull(map.get("ASS_NO"));

                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1, "配件随车确认 ");
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
            act.setForword(PART_CAR_CONFIRM_URL);
        }

    }
}
