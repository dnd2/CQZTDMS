package com.infodms.dms.actions.parts.baseManager.partBaseQuery.partSaleManager;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partBaseQuery.partSaleManager.financePartSalePriceDao;
import com.infodms.dms.exception.BizException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-4-18
 * @ClassName : PartSalePrice
 * @Description : 配件销售价格查询
 */
public class PartSalePriceActon extends BaseImport {
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);
    private static final financePartSalePriceDao dao = financePartSalePriceDao.getInstance();
    private static final String PART_SALE_PRICE_QUERY_URL_OEM = "/jsp/parts/baseManager/partBaseQuery/partSalePriceManager/partSalePriceOEM.jsp";//OEM销售价格查询
    private static final String PART_SALE_PRICE_QUERY_URL_DLR = "/jsp/parts/baseManager/partBaseQuery/partSalePriceManager/partSalePriceDLR.jsp";//DLR销售价格查询
    private static final String PART_SALE_PRICE_QUERY_URL_CUSTOMER_OEM = "/jsp/parts/baseManager/partBaseQuery/partSalePriceManager/partSalePriceCustomerOEM.jsp";

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 配件销售价格查询初始化
     */
    public void partSalePriceInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<Map<String, Object>> list = dao.queryPartPriceSettingList();
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

            act.setOutData("map", map);
            act.setOutData("poseBusType", logonUser.getPoseBusType());
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                act.setForword(PART_SALE_PRICE_QUERY_URL_OEM);
            } else {
                act.setForword(PART_SALE_PRICE_QUERY_URL_DLR);
            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 配件销售价格查询初始化（售后使用）
     */
    public void parttoCustomerInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<Map<String, Object>> list = dao.queryPartPriceSettingList();
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

            act.setOutData("map", map);
            act.setOutData("poseBusType", logonUser.getPoseBusType());
            act.setForword(PART_SALE_PRICE_QUERY_URL_CUSTOMER_OEM);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 查询配件销售价格 OEM & DLR
     */
    public void queryPartSalePrice() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPartSalePrice(request, curPage, Constant.PAGE_SIZE, logonUser);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 查询配件销售价格(售后使用) OEM & DLR
     */
    public void queryPartSaleCustomerPrice() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPartSalePriceCustomer(request, curPage, Constant.PAGE_SIZE_MIDDLE, logonUser);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : 导出配件销售价格信息 OEM
     */
    public void exportPartPriceExcelOEM() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            List<Map<String, Object>> lists = dao.queryPartPriceSettingList();

            String[] head = new String[21];
            head[0] = "配件编码";
            head[1] = "配件名称";
            head[2] = "件号";
            List<Map<String, Object>> priceList = dao.queryPartPriceSettingList();
            if (null != priceList && priceList.size() > 0) {
                // 只需要前3种价格:实际代理价、实际零售价、实际零售价
                for (int i = 0; i < 3; i++) {
                    String tempValue = priceList.get(i).get("TYPE_DESC").toString();
                    head[i+3] = tempValue + "(含税)";
                }
                // 实际调拨价
                head[6] = priceList.get(5).get("TYPE_DESC").toString() + "(含税)";
            }
//            head[3] = "服务站价格(元)";
//            head[4] = "零售价(元)";
            /*head[5]="计划价(元)";
			head[6]="团购价(元)";
			head[7]="价格5(元)";
			head[8]="价格6(元)";
			head[9]="价格7(元)";
			head[10]="价格8(元)";
			head[11]="价格9(元)";
			head[12]="价格10(元)";
			head[13]="价格11(元)";
			head[14]="价格12(元)";
			head[15]="价格13(元)";
			head[16]="领用价";
			head[17]="价格15(元)";*/
			/*head[18]="结算基地";*/
			
        	/*if(null != lists && lists.size() > 0)
        	{
        		for(int i = 0; i < lists.size(); i ++)
        		{
        			String tempValue = lists.get(i).get("TYPE_DESC").toString();
        			head[i+3] = tempValue+"(元)";
        		}
        	}*/


            List<Map<String, Object>> list = dao.queryPartSalePriceForExport(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[21];

                        detail[0] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[1] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_CODE"));
                        if (null != priceList && priceList.size() > 0) {
                         // 只需要前3种价格:实际代理价、实际零售价、实际零售价
                            for (int n = 0; n < 3; n++) {
                                detail[n+3] = CommonUtils.checkNull(map.get("SALE_PRICE"+(n+1))); 
                            }
                            // 实际调拨价
                            detail[6] = CommonUtils.checkNull(map.get("SALE_PRICE6")); 
                        }
                        
//                        detail[3] = CommonUtils.checkNull(map.get("SALE_PRICE1"));
//                        detail[4] = CommonUtils.checkNull(map.get("SALE_PRICE2"));
						/*detail[5] = CommonUtils.checkNull(map.get("SALE_PRICE3"));
						detail[6] = CommonUtils.checkNull(map.get("SALE_PRICE4"));        
						detail[7] = CommonUtils.checkNull(map.get("SALE_PRICE5"));      
						detail[8] = CommonUtils.checkNull(map.get("SALE_PRICE6"));      
						detail[9] = CommonUtils.checkNull(map.get("SALE_PRICE7"));     
						detail[10] = CommonUtils.checkNull(map.get("SALE_PRICE8"));     
						detail[11] = CommonUtils.checkNull(map.get("SALE_PRICE9"));     
						detail[12] = CommonUtils.checkNull(map.get("SALE_PRICE10"));    
						detail[13] = CommonUtils.checkNull(map.get("SALE_PRICE11"));    
						detail[14] = CommonUtils.checkNull(map.get("SALE_PRICE12"));    
						detail[15] = CommonUtils.checkNull(map.get("SALE_PRICE13"));    
						detail[16] = CommonUtils.checkNull(map.get("SALE_PRICE14"));    
						detail[17] = CommonUtils.checkNull(map.get("SALE_PRICE15"));*/
						/*detail[18] = CommonUtils.checkNull(map.get("PART_IS_CHANGHE"));*/

                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
            }

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : 导出配件销售价格信息 DLR
     */
    public void exportPartPriceExcelDLR() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
//            List<Map<String, Object>> lists = dao.queryPartPriceSettingList();

            String[] head = new String[8];
            head[0] = "件号";
            head[1] = "配件编码";
            head[2] = "配件名称";
            head[3] = "适用车型";

            List<Map<String, Object>> priceList = dao.queryPartPriceSettingList();
            if (null != priceList && priceList.size() > 0) {
                // 只需要前7种价格
                for (int i = 0; i < 3; i++) {
                    String tempValue = priceList.get(i).get("TYPE_DESC").toString();
                    head[i+4] = tempValue + "(含税)";
                }
                // 实际调拨价
                head[7] = priceList.get(5).get("TYPE_DESC").toString() + "(含税)";
            }
            
            
//            head[3] = "调拨价(含税)";
//            head[4] = "领用价(含税)";
//            head[5] = "零售价(含税)";
//            head[6] = "结算基地";

//            if (null != lists && lists.size() > 0) {
//                head[3] = lists.get(0).get("TYPE_DESC").toString() + "(含税)";
//                head[4] = lists.get(13).get("TYPE_DESC").toString() + "(含税)";
//                head[5] = lists.get(1).get("TYPE_DESC").toString() + "(含税)";
//            }

            List<Map<String, Object>> list = dao.queryPartSalePriceForExport(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[8];

                        detail[0] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[1] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[3] = CommonUtils.checkNull(map.get("MODEL_NAME"));
                        if (null != priceList && priceList.size() > 0) {
                            // 只需要前7种价格
                            for (int n = 0; n < 3; n++) {
                                detail[n+4] = CommonUtils.checkNull(map.get("SALE_PRICE"+(n+1))); 
                            }
                            // 实际调拨价
                            detail[7] = CommonUtils.checkNull(map.get("SALE_PRICE6")); 
                        }
                        
//                        detail[3] = CommonUtils.checkNull(map.get("PART_PRICE"));
//                        detail[4] = CommonUtils.checkNull(map.get("SALE_PRICE14"));
//                        detail[5] = CommonUtils.checkNull(map.get("SALE_PRICE2"));
//                        detail[6] = CommonUtils.checkNull(map.get("PART_IS_CHANGHE"));

                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
            }

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
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
     * @throws : LastDate    : 2013-4-8
     * @Title :
     * @Description: 导出数据方法
     */
    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "配件销售价格列表.xls";
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

}
