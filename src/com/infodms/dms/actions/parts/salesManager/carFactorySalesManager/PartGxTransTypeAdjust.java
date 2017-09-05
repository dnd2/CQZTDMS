package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartDlrOrderMainPO;
import com.infodms.dms.po.TtPartDplanDtlPO;
import com.infodms.dms.po.TtPartDplanMainPO;
import com.infodms.dms.po.TtPartFixcodeDefinePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class PartGxTransTypeAdjust implements PTConstants{

	public Logger logger = Logger.getLogger(PartGxTransTypeAdjust.class);
	
	private static final String PART_GX_TRANSTYPE_ADJUST_QUERY = "/jsp/parts/salesManager/carFactorySalesManager/partGXTransTypeAdjustQuery.jsp";
	private static final String PART_GX_DETAIL_URL = "/jsp/parts/salesManager/carFactorySalesManager/partGXDtlQuery.jsp";
	private static final String PART_GX_MOD_URL = "/jsp/parts/salesManager/carFactorySalesManager/partGXMod.jsp";
	
	 public static Object exportEx(ResponseWrapper response,
             RequestWrapper request, String[] head, List<String[]> list, int flag)
        throws Exception {

		String name = "广宣品发运方式信息.xls";
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
	
	public void partGxTransTypeAdjustInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<TtPartFixcodeDefinePO> listc = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_08);// 承运物流
			act.setOutData("listc", listc);
			act.setForword(PART_GX_TRANSTYPE_ADJUST_QUERY);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"广宣品发运计划查询错误,请联系管理员!");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void partGxTransTypeAdjustQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = null;
			ps = dao.queryPartGxTransTypeAdjust(request, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"广宣品发运计划方式调整查询错误,请联系管理员!");
			logger.error(loginUser,e1);
			act.setException(e1);
			act.setForword(PART_GX_TRANSTYPE_ADJUST_QUERY);
		}
	}
	
	public void expExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[9];
            head[0] = "发运计划单号";
            head[1] = "整车发运单号";
            head[2] = "服务商代码";
            head[3] = "服务商名称";
            head[4] = "包装号";
            head[5] = "发运方式";
            head[6] = "承运物流";
            head[7] = "计划生成日期";
            head[8] = "要求最终发运日期";
            
            PageResult<Map<String, Object>> ps = dao.queryPartGxTransTypeAdjust(request, 1, Constant.PAGE_SIZE_MAX);
            List<Map<String, Object>> list = ps.getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[9];
                        detail[0] = CommonUtils.checkNull(map.get("PLAN_CODE"));
                        detail[1] = CommonUtils.checkNull(map.get("ASS_NO"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[4] = CommonUtils.checkNull(map.get("PKG_NO"));
                        if(map.get("TRANS_TYPE")!=null){
                        	int transType = ((BigDecimal)map.get("TRANS_TYPE")).intValue();
                        	if(Constant.PART_GX_ORDER_OUT_TYPE_01.intValue()==transType){
                        		detail[5] = "随车出库";
                        	}else{
                        		detail[5] = "直发出库";
                        	}
                        }else{
                        	detail[5] = "";
                        }
                        detail[6] = CommonUtils.checkNull(map.get("TRANS_ORG"));
                        detail[7] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[8] = CommonUtils.checkNull(map.get("FINAL_DATE"));
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, 1);
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
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_GX_TRANSTYPE_ADJUST_QUERY);
        }
    }
	
	public void detailPlanOrderInit(){
		
		ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
        	List<TtPartFixcodeDefinePO> listc = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_08);// 承运物流
            String planCode = CommonUtils.checkNull(request.getParamValue("planCode"));
            String pkgNo = CommonUtils.checkNull(request.getParamValue("pkgNo"));
            Map<String,Object> map = dao.getGxPlanOrderInfo(planCode,pkgNo);
            request.setAttribute("po", map);
            act.setOutData("listc", listc);
            act.setForword(PART_GX_DETAIL_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "广宣品发运计划单");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
		
	}
	
	public void modifyPlanOrderInit(){
		
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
		try {
			List<TtPartFixcodeDefinePO> listc = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_08);// 承运物流
			String planCode = CommonUtils.checkNull(request.getParamValue("planCode"));
			String pkgNo = CommonUtils.checkNull(request.getParamValue("pkgNo"));
			Map<String,Object> map = dao.getGxPlanOrderInfo(planCode,pkgNo);
			request.setAttribute("po", map);
			act.setOutData("listc", listc);
			act.setForword(PART_GX_MOD_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "广宣品发运计划单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	
	
	public void queryPartGxDetail(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = null;
			ps = dao.queryPartGxDtl(request, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"广宣品发运计划单查询错误,请联系管理员!");
			logger.error(loginUser,e1);
			act.setException(e1);
			act.setForword(PART_GX_TRANSTYPE_ADJUST_QUERY);
		}
	}
	
	
	public void updatePartGxPlan(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
			
			String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE"));
			String transOrg = CommonUtils.checkNull(request.getParamValue("transportOrg"));
			String wl_NO = CommonUtils.checkNull(request.getParamValue("wl_NO"));
			
			String[] params = request.getParamValues("ck");
			for(int i=0,len=params.length;i<len;i++){
				String planId = params[i].split(",")[0];
				String pkgNo = params[i].split(",")[1];
				dao.updatePartGxTransType(planId,pkgNo,transType,transOrg,wl_NO);
			}
			act.setOutData("success", "广宣品发运方式调整成功!");
		}catch(Exception e) {//异常方法
			if(e instanceof BizException){
				BizException e1 = (BizException)e;
				logger.error(loginUser,e1);
				act.setException(e1);
				return ;
			}
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"广宣品发运方式调整失败,请联系管理员!");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
}
