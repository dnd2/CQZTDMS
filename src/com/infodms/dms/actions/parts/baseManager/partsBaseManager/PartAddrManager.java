package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartAddrDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartAddrDefinePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Workbook;
import jxl.write.Label;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartAddrManager implements PTConstants {

    public Logger logger = Logger.getLogger(PartAddrManager.class);
    private PartAddrDao dao = PartAddrDao.getInstance();

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "经销商发运接收地址信息.xls";
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
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 查询初始化, 转到查询页面
     */
    public void partAddrQueryInit() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            act.setForword(PART_ADDR_QUERY_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商发运接收地址");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-10
     * @Title :
     * @Description: 查询
     */
    public void queryPartAddrInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {

            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartAddrList(request, curPage, Constant.PAGE_SIZE);
            // 分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商发运接收地址");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 新增初始化, 跳转到新增页面
     */
    public void addPartAddrInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            act.setForword(PART_ADDR_ADD_URL);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.ADD_FAILURE_CODE, "经销商发运接收地址添加");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * <p>Description: 验证经销商发运是否有默认地址</p>
     */
    public void validPartDefaultAddr(){
        ActionContext act = ActionContext.getContext();
        try {
            RequestWrapper req = act.getRequest();
            // 服务站ID
            String dealerId = CommonUtils.checkNull(req.getParamValue("dealerId")); // 经销商ID
            String actionType = CommonUtils.checkNull(req.getParamValue("actionType")); // 地址ID
            String notExistsAddrId = CommonUtils.checkNull(req.getParamValue("notExistsAddrId")); // 地址ID
            Map<String, String> paramMap = new HashMap<String, String>(); // 
            paramMap.put("dealerId", dealerId); // 经销商ID
            // 查询默认地址列表
            List<Map<String, Object>> addrList = dao.getDealerPartAddrList(paramMap);
            paramMap.put("isDefaultAddr", Constant.IF_TYPE_YES.toString()); // 默认地址标志-是
            paramMap.put("notExistsAddrId", notExistsAddrId); // 地址ID
            List<Map<String, Object>> defaultAddrList = dao.getDealerPartAddrList(paramMap);
            int returnCode = 0;
            if(addrList.size() >=3){
                // 已存在的地址数量大于等于3个时
                returnCode = 1;
            }else if(defaultAddrList.size() > 0){
                // 默认地址数量大于0时
                returnCode = 2;
                if(StringUtils.isEmpty(notExistsAddrId) && "eidt".equals(actionType)){
                    returnCode = 3;
                }
            }
            act.setOutData("returnCode", returnCode);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.ADD_FAILURE_CODE, "经销商发运接收地址");
            act.setException(e1);
            act.setOutData("returnCode", -1);
            act.setOutData("error", "验证失败,请联系管理员!");
        }
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 添加信息
     */
    public void addPartAddrInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String str_dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));// 获取采购单位id
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));// 获取采购单位编码
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));// 获取采购单位名称
            String addr = CommonUtils.checkNull(request.getParamValue("ADDR"));// 获取地址
            String linkman = CommonUtils.checkNull(request.getParamValue("LINKMAN"));// 获取接收人
            String tel = CommonUtils.checkNull(request.getParamValue("TEL"));// 获取接收人电话
            String postcode = CommonUtils.checkNull(request.getParamValue("POST_CODE"));// 获取邮编
            String station = CommonUtils.checkNull(request.getParamValue("STATION"));// 获取到站名称
            String fax = CommonUtils.checkNull(request.getParamValue("FAX"));// 获取传真
            String remark = CommonUtils.checkNull(request.getParamValue("REMARK"));// 获取备注
            //add by yuan 20130727
            String linkman2 = CommonUtils.checkNull(request.getParamValue("LINKMAN2"));// 获取联系人
            String tel2 = CommonUtils.checkNull(request.getParamValue("TEL2"));// 获取联系人电话
            String provinceId = CommonUtils.checkNull(request.getParamValue("PROVINCE_ID"));//省份ID
            String cityId = CommonUtils.checkNull(request.getParamValue("CITY_ID"));// 城市ID
            String counties = CommonUtils.checkNull(request.getParamValue("COUNTIES"));//区县ID
            String isDefaultAddr = CommonUtils.checkNull(request.getParamValue("isDefaultAddr"));//是否默认地址
            String existsDefaultAddr = CommonUtils.checkNull(request.getParamValue("existsDefaultAddr"));//是否已存在默认地址

            TtPartAddrDefinePO ttPartAddrDefinePO = new TtPartAddrDefinePO();
            ttPartAddrDefinePO.setAddrId(CommonUtils.parseLong(SequenceManager.getSequence("")));
            ttPartAddrDefinePO.setDealerId(CommonUtils.parseLong(str_dealerId));
            ttPartAddrDefinePO.setDealerCode(dealerCode);
            ttPartAddrDefinePO.setDealerName(dealerName);
            ttPartAddrDefinePO.setAddr(addr);
            ttPartAddrDefinePO.setLinkman(linkman);
            ttPartAddrDefinePO.setTel(tel);
            ttPartAddrDefinePO.setPostCode(postcode);
            ttPartAddrDefinePO.setStation(station);
            ttPartAddrDefinePO.setFax(fax);
            ttPartAddrDefinePO.setRemark(remark);
            ttPartAddrDefinePO.setCreateDate(new Date());
            ttPartAddrDefinePO.setCreateBy(logonUser.getUserId());
            ttPartAddrDefinePO.setAddressType(20491001);
            ttPartAddrDefinePO.setIsDefaultAddr(isDefaultAddr);
            //add by yuan 20130727
            ttPartAddrDefinePO.setLinkman2(linkman2);
            ttPartAddrDefinePO.setTel2(tel2);
            if (!"".equals(provinceId) && null != provinceId) {
                ttPartAddrDefinePO.setProvinceId(Long.valueOf(provinceId));
            }
            if (!"".equals(cityId) && null != cityId) {
                ttPartAddrDefinePO.setCityId(Long.valueOf(cityId));
            }
            if (!"".equals(counties) && null != counties) {
                ttPartAddrDefinePO.setCounties(Long.valueOf(counties));
            }
            // 当前新增地址为默认地址，因此将该经销商下的其他默认地址修改为否
            if(Constant.IF_TYPE_YES.toString().equals(isDefaultAddr) 
                    && Integer.parseInt(existsDefaultAddr) == Constant.IF_TYPE_YES){
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("user", logonUser.getUserId().toString());
                paramMap.put("dealerId", str_dealerId);
                dao.updatePartDefaultAddr(paramMap);
            }
            dao.insert(ttPartAddrDefinePO);
            act.setOutData("success", "添加成功!");

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.ADD_FAILURE_CODE, "经销商发运接收地址");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("error", "添加失败,请联系管理员!");
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 修改初始化, 转到修改页面
     */
    public void queryPartAddrDetail() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String addrId = CommonUtils.checkNull(request.getParamValue("addrId")); //经销商发运接收地址Id
            Map<String, Object> addrInfo = dao.getPartAddrDetail(addrId);
            act.setOutData("addrInfo", addrInfo);
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("curPage", curPage);
            act.setForword(PART_ADDR_INFO_MOD);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商发运接收地址");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 修改
     */
    public void updatePartAddrInfo() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            String addrId = request.getParamValue("ADDR_ID");

            String str_dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));// 获取采购单位id
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));// 获取采购单位编码
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));// 获取采购单位名称
            String addr = CommonUtils.checkNull(request.getParamValue("ADDR"));// 获取地址
            String linkman = CommonUtils.checkNull(request.getParamValue("LINKMAN"));// 获取接收人
            String tel = CommonUtils.checkNull(request.getParamValue("TEL"));// 获取接收人电话
            String postcode = CommonUtils.checkNull(request.getParamValue("POST_CODE"));// 获取邮编
            String station = CommonUtils.checkNull(request.getParamValue("STATION"));// 获取到站名称
            String fax = CommonUtils.checkNull(request.getParamValue("FAX"));// 获取传真
            String remark = CommonUtils.checkNull(request.getParamValue("REMARK"));// 获取备注
            //add by yuan 20130727
            String linkman2 = CommonUtils.checkNull(request.getParamValue("LINKMAN2"));// 获取联系人
            String tel2 = CommonUtils.checkNull(request.getParamValue("TEL2"));// 获取联系人电话
            String provinceId = CommonUtils.checkNull(request.getParamValue("PROVINCE_ID"));//省份ID
            String cityId = CommonUtils.checkNull(request.getParamValue("CITY_ID"));// 城市ID
            String counties = CommonUtils.checkNull(request.getParamValue("COUNTIES"));//区县ID
            String isDefaultAddr = CommonUtils.checkNull(request.getParamValue("isDefaultAddr"));//是否默认地址
            String existsDefaultAddr = CommonUtils.checkNull(request.getParamValue("existsDefaultAddr"));//是否已存在默认地址

            TtPartAddrDefinePO spo = new TtPartAddrDefinePO();//源po
            spo.setAddrId((CommonUtils.parseLong(addrId)));

            TtPartAddrDefinePO ttPartAddrDefinePO = new TtPartAddrDefinePO();//更新po
            ttPartAddrDefinePO.setDealerId(CommonUtils.parseLong(str_dealerId));
            ttPartAddrDefinePO.setDealerCode(dealerCode);
            if (dealerName.indexOf(dealerCode) > -1) {
                ttPartAddrDefinePO.setDealerName(dealerName.substring(0, dealerName.indexOf("(")));
            } else {
                ttPartAddrDefinePO.setDealerName(dealerName);
            }
            ttPartAddrDefinePO.setAddr(addr);
            ttPartAddrDefinePO.setLinkman(linkman);
            ttPartAddrDefinePO.setTel(tel);
            ttPartAddrDefinePO.setPostCode(postcode);
            ttPartAddrDefinePO.setStation(station);
            ttPartAddrDefinePO.setFax(fax);
            ttPartAddrDefinePO.setRemark(remark);
            ttPartAddrDefinePO.setUpdateDate(new Date());
            ttPartAddrDefinePO.setUpdateBy(logonUser.getUserId());
            ttPartAddrDefinePO.setIsDefaultAddr(isDefaultAddr);
            //add by yuan 20130727
            ttPartAddrDefinePO.setLinkman2(linkman2);
            ttPartAddrDefinePO.setTel2(tel2);
            if (!"".equals(provinceId) && null != provinceId) {
                ttPartAddrDefinePO.setProvinceId(Long.valueOf(provinceId));
            }
            if (!"".equals(cityId) && null != cityId) {
                ttPartAddrDefinePO.setCityId(Long.valueOf(cityId));
            }
            if (!"".equals(counties) && null != counties) {
                ttPartAddrDefinePO.setCounties(Long.valueOf(counties));
            }
            // 当前修改地址为默认地址，因此将该经销商下的其他默认地址修改为否
            if(Constant.IF_TYPE_YES.toString().equals(isDefaultAddr)
                    && Integer.parseInt(existsDefaultAddr) == Constant.IF_TYPE_YES){
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("user", logonUser.getUserId().toString());
                paramMap.put("dealerId", str_dealerId);
                dao.updatePartDefaultAddr(paramMap);
            }
            dao.update(spo, ttPartAddrDefinePO);

            act.setOutData("success", "修改成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "经销商发运接收地址");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 下载经销商发运接收地址
     */
    public void exportPartAddrExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[15];
            head[0] = "省份";
            head[1] = "城市";
            head[2] = "区县";
            head[3] = "经销商编码";
            head[4] = "经销商名称";
            head[5] = "地址";
            head[6] = "接收人";
            head[7] = "接收人电话";
            head[8] = "邮编";
            head[9] = "到站名称";
            head[10] = "传真";
            head[11] = "是否有效";
            head[12] = "是否默认地址";
            List<Map<String, Object>> list = dao.queryPartAddrList(request, 1, Constant.PAGE_SIZE_MAX).getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(map.get("PROVINCE_ID"));
                        detail[1] = CommonUtils.checkNull(map.get("CITY_ID"));
                        detail[2] = CommonUtils.checkNull(map.get("COUNTIES"));
                        detail[3] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("ADDR"));
                        detail[6] = CommonUtils.checkNull(map.get("LINKMAN"));
                        detail[7] = CommonUtils.checkNull(map.get("TEL"));
                        detail[8] = CommonUtils.checkNull(map.get("POST_CODE"));
                        detail[9] = CommonUtils.checkNull(map.get("STATION"));
                        detail[10] = CommonUtils.checkNull(map.get("FAX"));
                        if (CommonUtils.checkNull(map.get("STATE")).equals(Constant.STATUS_ENABLE + "")) {
                            detail[11] = "有效";
                        } else {
                            detail[11] = "无效";
                        }
                        if (CommonUtils.checkNull(map.get("IS_DEFAULT_ADDR")).equals(Constant.IF_TYPE_YES + "")) {
                            detail[12] = "是";
                        } else {
                            detail[12] = "否";
                        }
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
            act.setForword(PART_ADDR_QUERY_URL);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 失效
     */
    public void celPartAddr() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        try {
            String addrId = CommonUtils.checkNull(request.getParamValue("addrId")); //经销商发运接收地址Id
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            TtPartAddrDefinePO spo = new TtPartAddrDefinePO();//源po
            spo.setAddrId(((Long.parseLong(addrId))));
            TtPartAddrDefinePO po = new TtPartAddrDefinePO();//更新po
            po.setState(Constant.STATUS_DISABLE);
            po.setDisableBy(logonUser.getUserId());
            po.setDisableDate(new Date());

            dao.update(spo, po);
            act.setOutData("success", "失效成功!");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "经销商发运接收地址");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 有效
     */
    public void selPartAddr() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        try {
            String addrId = CommonUtils.checkNull(request.getParamValue("addrId")); //经销商发运接收地址Id
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            TtPartAddrDefinePO spo = new TtPartAddrDefinePO();//源po
            spo.setAddrId(((Long.parseLong(addrId))));
            TtPartAddrDefinePO po = new TtPartAddrDefinePO();//更新po
            po.setState(Constant.STATUS_ENABLE);

            dao.update(spo, po);
            act.setOutData("curPage", curPage);
            act.setOutData("success", "设置有效成功!");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "经销商发运接收地址");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}
