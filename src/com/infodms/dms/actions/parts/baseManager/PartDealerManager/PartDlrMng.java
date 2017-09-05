package com.infodms.dms.actions.parts.baseManager.PartDealerManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.parts.baseManager.partServicerManager.PartDlrDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartFixcodeDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtPartDealerCartypePO;
import com.infodms.dms.po.TtPartFixcodeDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Cell;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @author hezk
 * @version 1.0
 * @Title: CHANADMS
 * @Description:
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2013-4-23
 * @mail hezk@yonyou.com
 * @remark
 */
public class PartDlrMng extends BaseImport {
    PartDlrDao dao = PartDlrDao.getInstance();
    public Logger logger = Logger.getLogger(PartDlrMng.class);

    private final String queryServiceInitUrl = "/jsp/parts/servicerInforManager/PartDlrQuery.jsp";
    private final String queryServicerInitDetailUrl = "/jsp/parts/servicerInforManager/PartDlrQueryDtl.jsp";
    private final String addAddressInit = "/jsp/parts/servicerInforManager/PartDlrAddrMng.jsp";
    private final String modifyAddressUrl = "/jsp/parts/servicerInforManager/PartDlrAddrMod.jsp";
    private final String uploadErrUrl = "/jsp/parts/servicerInforManager/partUploadInputError.jsp";


    // 主页面设置服务商的类型
    public void resetServiceType() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String serviceType = CommonUtils.checkNull(request.getParamValue("serviceType"));// 服务类型
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));// 服务类型
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            String dealerIds[] = dealerId.split("@@");
            for (int i = 0; i < dealerIds.length; i++) {
                String dealerId2 = dealerIds[i];
                String sql = "update tm_dealer t set t.Update_Date=sysdate, t.pdealer_type = " + serviceType + " where t.dealer_id = '"
                        + dealerId2 + "'";
                dao.update(sql, null);
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("serviceType", serviceType);
            map.put("dealerId", dealerId);
            act.setOutData("returnValue", 1);
            act.setOutData("curPage", curPage);
            act.setForword(queryServiceInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "主页面设置服务商的类型");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 经销商维护查询页面初始化
     *
     * @return void
     * @throws Exception
     */
    public void queryServicerInfoInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_05);// 获取配件服务商类型
            act.setOutData("dlrType", list);
            act.setOutData("areaList", areaList);
            act.setForword(queryServiceInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 服务商查询结果页面
     *
     * @return void
     * @throws Exception
     */
    public void queryServicerInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryDealerInfo(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商查询结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void dealerInfoDownload() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            ResponseWrapper response = act.getResponse();
            // 导出的文件名
            String fileName = "服务商分网信息.xls";
            OutputStream os = null;
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            List<List<Object>> contentList = new LinkedList<List<Object>>();
            List<Object> rowList = new LinkedList<Object>();

            rowList.add("服务商代码");
            rowList.add("服务商");
            rowList.add("配件车型");

            contentList.add(rowList);
            PageResult<Map<String, Object>> dealerInfoList = dao.queryDealerInfo(request, 1, Constant.PAGE_SIZE_MAX);
            if (dealerInfoList != null) {
                int len = dealerInfoList.getRecords().size();
                for (int i = 0; i < len; i++) {
                    rowList = new LinkedList<Object>();
                    rowList.add(CommonUtils.checkNull(dealerInfoList.getRecords().get(i).get("DEALER_CODE")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.getRecords().get(i).get("DEALER_NAME")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.getRecords().get(i).get("CARTYPE")));// 新增经销商评级
                    contentList.add(rowList);
                }
            }

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(contentList, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商信息下载结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 车型选择界面
     */
    public void queryServicerInfoDetail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));// 经销商id
            String brandName = null;
            Map<String, Object> map1 = null;
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> branList = PartFixcodeDAO.getInstance().getPartFixcodeListByTypeId(Constant.FIXCODE_TYPE_09);

//            for (int i = 0; i < branList.size(); i++) {
//                brandName = branList.get(i).get("FIX_NAME") + "";
                brandName = "";
                List<Map<String, Object>> carTypeList = PartFixcodeDAO.getInstance().getPartFixcodeListByTypeId(Constant.FIXCODE_TYPE_10, brandName);
                map1 = new HashMap<String, Object>();
                map1.put("carTypeList", carTypeList);
                list.add(map1);
//            }

            act.setOutData("list", list);
            act.setOutData("dlrCarTypes", dao.getDlrCarTypes(dealerId));
            act.setOutData("dealerId", dealerId);
            act.setForword(queryServicerInitDetailUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商信息查看");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * 更新服务商和适提车型关系
     */
    public void updateDLRCarType() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
            String carTypeIds = CommonUtils.checkNull(request.getParamValue("carTypeIds"));
            Integer curPage = request.getParamValue("mcurPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            String[] carTypeIds2 = carTypeIds.trim().split(",");
            TtPartDealerCartypePO dealerCartypePO = null;
            if (carTypeIds.length() > 0 && dealerId != null) {
                //先删除
                dealerCartypePO = new TtPartDealerCartypePO();
                dealerCartypePO.setDealerId(Long.valueOf(dealerId));
                dao.delete(dealerCartypePO);
                for (String carType : carTypeIds2) {
                    dealerCartypePO = new TtPartDealerCartypePO();
                    dealerCartypePO.setDealerId(Long.valueOf(dealerId));
                    dealerCartypePO.setCarType(carType);
                    //再重新插入
                    if (dao.select(dealerCartypePO).size() == 0) {
                        dealerCartypePO.setDcId(Long.valueOf(SequenceManager.getSequence("")));
                        dealerCartypePO.setCreateDate(new Date());
                        dealerCartypePO.setCreateBy(logonUser.getUserId());

                        dao.insert(dealerCartypePO);
                    }
                }
            }
            act.setOutData("success", "设置成功!");
            act.setOutData("mcurPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商和车型关系设置失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * 导入模板下载
     */
    public void exportExcTemplate() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        ResponseWrapper response = act.getResponse();
        OutputStream os = null;
        try {
            List<List<Object>> list = new LinkedList<List<Object>>();
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列

            listHead.add("服务商代码");
            listHead.add("车型(多个车型用逗号分隔)");
            list.add(listHead);
            // 导出的文件名
            String fileName = "服务商分网维护模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "模板下载失败");
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
     * 导入上传
     */
    public void uploadExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer errorInfo = new StringBuffer("");
        RequestWrapper request = act.getRequest();
        try {
            long maxSize = 1024 * 1024 * 3;
            int errNum = 0;
            errNum = insertIntoTmp(request, "uploadFile", 3, 10, maxSize);
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
                List<String> vdIdList = new ArrayList();
                List voList = new ArrayList();
                List<HashMap<String, Object>> errList = new ArrayList<HashMap<String, Object>>();
                HashMap<String, Object> errMap = null;
                List<TtPartDealerCartypePO> insertList = new ArrayList<TtPartDealerCartypePO>();
                List<TtPartDealerCartypePO> deleteList = new ArrayList<TtPartDealerCartypePO>();
                List<Object> dealerIdList = new ArrayList<Object>();

                TtPartDealerCartypePO cartypePO = null;

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
                        if (errorInfo.length() > 0) {
                            break;
                        }
                        String dealerCode = cells[0].getContents().trim();
                        String carTypes = cells[2].getContents().trim();

                        //服务商代码校验
                        Long dealerId = null;
                        TmDealerPO dealerPO = new TmDealerPO();
                        dealerPO.setDealerCode(dealerCode);

                        if (!"".equals(dealerCode) && null != dealerCode) {
                            if (dao.select(dealerPO).size() != 0) {
                                dealerId = ((TmDealerPO) dao.select(dealerPO).get(0)).getDealerId();
                            }
                            if (dealerId == null) {
                                errMap = new HashMap<String, Object>();
                                errMap.put("err", "第" + key + "行服务商代码【" + dealerCode + "】不存在！");
                                errList.add(errMap);
                            } else {
                                dealerIdList.add(dealerId);
                            }
                        } else {
                            errMap = new HashMap<String, Object>();
                            errMap.put("err", "第" + key + "行服务商代码不能为空！");
                            errList.add(errMap);
                        }
                        //车型校验
                        if (!"".equals(carTypes) && null != carTypes) {
                            if (carTypes.split(",").length == 0) {
                                errMap = new HashMap<String, Object>();
                                errMap.put("err", "第" + key + "行多车型必须通过逗号分隔！");
                                errList.add(errMap);
                            } else {
                                TtPartFixcodeDefinePO fixcodeDefinePO = new TtPartFixcodeDefinePO();
                                fixcodeDefinePO.setFixGouptype(Constant.FIXCODE_TYPE_10);
                                for (String carType : carTypes.split(",")) {
                                    String carTypeValue = null;
                                    cartypePO = new TtPartDealerCartypePO();
                                    cartypePO.setDealerId(dealerId);
                                    fixcodeDefinePO.setFixName(carType);
                                    if (dao.select(fixcodeDefinePO).size() != 0) {
                                        carTypeValue = ((TtPartFixcodeDefinePO) dao.select(fixcodeDefinePO).get(0)).getFixValue();
                                    }
                                    if (carTypeValue == null) {
                                        errMap = new HashMap<String, Object>();
                                        errMap.put("err", "第" + key + "行车型【" + carType + "】不存在！");
                                        errList.add(errMap);
                                    } else {
                                        cartypePO.setCarType(carTypeValue);
                                        cartypePO.setCreateDate(new Date());
                                        cartypePO.setDcId(Long.valueOf(SequenceManager.getSequence("")));
                                        insertList.add(cartypePO);
                                    }
                                }
                            }
                        }
                    }
                }
                if (errList.size() > 0) {
                    act.setOutData("errList", errList);
                    act.setOutData("flag", 1);
                    act.setForword(uploadErrUrl);
                    return;
                } else {
                    dao.delete(dealerIdList);
                    dao.insert(insertList);
                }
                act.setOutData("flag", "导入成功");
                act.setForword(queryServiceInitUrl);
            }

        } catch (Exception e) {
            new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
            act.setOutData("flag", "导入失败");
            act.setForword(queryServiceInitUrl);
        }

    }
}