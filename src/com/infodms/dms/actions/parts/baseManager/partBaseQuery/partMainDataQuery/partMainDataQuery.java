package com.infodms.dms.actions.parts.baseManager.partBaseQuery.partMainDataQuery;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.component.dict.CodeDict;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.PartBaseQueryDao;
import com.infodms.dms.dao.parts.baseManager.partSalePriceQuery.PartSalePriceDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartFixcodeDAO;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : Administrator
 *         CreateDate     : 2013-7-22
 * @ClassName : partMainDataQuery
 */
public class partMainDataQuery extends BaseImport implements PTConstants {
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);
    PartBaseQueryDao dao = PartBaseQueryDao.getInstance();

    private static final String MAIN_DATA_URL = "/jsp/parts/baseManager/partBaseQuery/PartMainDataQuery/PartMainDataQuery.jsp"; //配件主信息查询
    private static final String DETAIL_DATA_URL = "/jsp/parts/baseManager/PartBaseView.jsp"; //配件主信息详情查看
    private static final String DETAIL_DATA_PIC_URL = "/jsp/parts/baseManager/PartPicView.jsp"; //配件图片查看

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-1
     * @Title : 配件主信息查询初始化
     */
    public void partBaseQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            // 获取价格的变量
            List<Map<String, Object>> list = PartSalePriceDao.getInstance().queryPartPriceSettingList();
            Map<String, String> map = new HashMap<String, String>();
            map.put("prciceName1", "价格1");
            map.put("prciceName2", "价格2");
            map.put("prciceName3", "价格3");
            map.put("prciceName4", "价格4");
            map.put("prciceName5", "价格5");
            map.put("prciceName6", "价格6");
            map.put("prciceName7", "价格7");
            map.put("prciceName8", "价格8");
            map.put("prciceName9", "价格9");
            map.put("prciceName10", "价格10");
            map.put("prciceName11", "价格11");
            map.put("prciceName12", "价格12");
            map.put("prciceName13", "价格13");
            map.put("prciceName14", "价格14");
            map.put("prciceName15", "价格15");

            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String tempValue = list.get(i).get("TYPE_DESC").toString();
                    map.put("prciceName" + (i + 1), tempValue);
                }
            }

            
            PurchasePlanSettingDao dao2 = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> planerList = dao2.getUserPoseLise(1, null);
            act.setOutData("map", map);
            act.setForword(MAIN_DATA_URL);
            act.setOutData("sortList", dao.getSortList());
            act.setOutData("planerList", planerList);
            // 添加 东安数据过滤
            act.setOutData("poseBusType", logonUser.getPoseBusType());
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件主信息维护初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-2
     * @Title : 配件主信息维护信息查询
     */
    public void queryPartBaseInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPartBase(request, curPage, Constant.PAGE_SIZE, logonUser.getPoseBusType());
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件主信息维护");
            logger.error(logonUser, e1);
            act.setException(e1);

        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-2
     * @Title : 配件主信息维护详细信息初始化页面
     */

    public void queryPartBaseDetail() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper req = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        logonUser.getDealerId();
        String forword = "";
        try {
            String partId = CommonUtils.checkNull(req.getParamValue("partId"));//所选查看信息
            String flag = CommonUtils.checkNull(req.getParamValue("flag"));
            String curPage = CommonUtils.checkNull(req.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            
            Map<String, Object> ps = null;
            ps = dao.queryPartDetail(partId);
            Map<String, Object> carInfoMap = new HashMap<String, Object>(); // 车辆信息map
            // 适用品牌基础数据
            String brandName = null;
            List<Map<String, Object>> branList = PartFixcodeDAO.getInstance().getPartFixcodeListByTypeId(Constant.FIXCODE_TYPE_09);
            carInfoMap.put("branList", branList);
            
            // 适用车型基础数据
            List<Map<String, Object>> carTypeList = PartFixcodeDAO.getInstance().getPartFixcodeListByTypeId(Constant.FIXCODE_TYPE_10, brandName);
            carInfoMap.put("carTypeList", carTypeList);
//            carInfoMap.put("carTypeChkeds", dao.getPartCarTypes(partId)); 
            // 选中的车型
            Object modelId = ps.get("MODEL_ID");
            if(modelId != null && !"".equals(modelId)){
                carInfoMap.put("carTypeChkeds", modelId.toString().split(","));
            }
            
            // 适用车系基础数据
            List<Map<String, Object>> seriesList = PartFixcodeDAO.getInstance().getPartFixcodeListByTypeId(Constant.FIXCODE_TYPE_11);
            carInfoMap.put("carSeriesList", seriesList);
            // 选中的车系
            Object seriesId = ps.get("SERIES_ID");
            if(seriesId != null && !"".equals(seriesId)){
                carInfoMap.put("carSeriesChkeds", seriesId.toString().split(","));
            }
            
            act.setOutData("carInfo", carInfoMap);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            Map<String, String> map = new HashMap();
            ps.put("DEFEND_DATE", now);
            act.setOutData("ps", ps);
            act.setOutData("curPage", curPage);
            act.setForword(DETAIL_DATA_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件主信息维护详细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartBaseQueryUrl);
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :下载
     */
    public void exportPartBaseExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        OutputStream os = null;
        try {
            
            
            Map<String,List<TcCodePO>> dictMap = CodeDict.dictMap;
            
            ResponseWrapper response = act.getResponse();
            List<List<Object>> expExlList = new LinkedList<List<Object>>();
            //标题
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
            listHead.add("配件编码");
            listHead.add("配件名称");
            listHead.add("配件种类");
            listHead.add("单位");
            listHead.add("适用车型");
            listHead.add("是否协议包装");
            listHead.add("是否批次包装");
            listHead.add("是否停用");
            listHead.add("停用日期");
            listHead.add("最小销售数量");
            listHead.add("最大销售数量");
            listHead.add("最小采购数量");
            listHead.add("是否可替代");
            listHead.add("是否有效");
            // 价格变量-获取前4个
//            List<Map<String, Object>> priceList = PartSalePriceDao.getInstance().queryPartPriceSettingList();
//            if (null != priceList && priceList.size() > 0) {
//                for (int i = 0; i < 4; i++) {
//                    String tempValue = priceList.get(i).get("TYPE_DESC").toString();
//                    listHead.add(tempValue);
//                }
//            }
            listHead.add("备注");
            expExlList.add(listHead);
            
            // 查询导出数据
            List<Map<String, Object>> dataList = dao.queryPartBaseList(request, logonUser.getPoseBusType());
            for(Map<String, Object> data:dataList){
                List<Object> rowDataList = new ArrayList<Object>();
                rowDataList.add(CommonUtils.checkNull(data.get("PART_OLDCODE")));
                rowDataList.add(CommonUtils.checkNull(data.get("PART_CNAME")));
                rowDataList.add(CommonUtils.checkNull(data.get("PART_TYPE_DESC")));
                rowDataList.add(CommonUtils.checkNull(data.get("UNIT")));
                rowDataList.add(CommonUtils.checkNull(data.get("MODEL_NAME")));
                rowDataList.add(CommonUtils.checkNull(data.get("IS_PROTOCOL_PACK_DESC")));
                rowDataList.add(CommonUtils.checkNull(data.get("IS_MAG_BATCH_DESC")));
                rowDataList.add(CommonUtils.checkNull(data.get("IS_PART_DISABLE_DESC")));
                rowDataList.add(CommonUtils.checkNull(data.get("PART_DISABLE_DATE")));
                rowDataList.add(CommonUtils.checkNull(data.get("MIN_SALE")));
                rowDataList.add(CommonUtils.checkNull(data.get("MAX_SALE_VOLUME")));
                rowDataList.add(CommonUtils.checkNull(data.get("MIN_PURCHASE")));
                rowDataList.add(CommonUtils.checkNull(data.get("IS_REPLACED_DESC")));
                rowDataList.add(CommonUtils.checkNull(data.get("STATE_DESC")));
//                if (null != priceList && priceList.size() > 0) {
//                    for (int i = 0; i < 4; i++) {
//                        rowDataList.add(CommonUtils.checkNull(data.get("SALE_PRICE"+(i+1))));
//                    }
//                }
                rowDataList.add(CommonUtils.checkNull(data.get("REMARK")));
                expExlList.add(rowDataList);
            }
            
            // 导出的文件名
            String fileName = "配件数据.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(expExlList, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }finally {
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
                    logger.error(logonUser, e1);
                    act.setException(e1);
                }
            }
        }
    }

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "配件主数据信息.xls";
        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8"));
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

    public void ShowPartPic() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper req = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(req.getParamValue("partId"));
            List<Map<String, Object>> list = dao.getPartPicURLId(partId);
            if (list.size() > 0) {
                act.setOutData("picId", list.get(0).get("TPLJ"));
                act.setOutData("partName", list.get(0).get("PART_CNAME"));
                act.setOutData("partOldCode", list.get(0).get("PART_OLDCODE"));
            }

            act.setForword(DETAIL_DATA_PIC_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件主信息查看详细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

}

