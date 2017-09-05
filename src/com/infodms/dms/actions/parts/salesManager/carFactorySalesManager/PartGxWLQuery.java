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

public class PartGxWLQuery implements PTConstants{

	public Logger logger = Logger.getLogger(PartGxTransTypeAdjust.class);
	
	private static final String INIT_URL = "/jsp/parts/salesManager/carFactorySalesManager/PartGxWL.jsp";
		
	//查询初始化
	public void init(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<TtPartFixcodeDefinePO> listc = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_08);// 承运物流
			act.setOutData("listc", listc);
			act.setForword(INIT_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"广宣品物流信息查询错误,请联系管理员!");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	//查询页面
	public void query(){
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
			ps = dao.queryGXWL(request, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(INIT_URL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"广宣品物流信息查询错误,请联系管理员!");
			logger.error(loginUser,e1);
			act.setException(e1);
			
		}
	}
	//导出
	public void expExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[14];
            head[0] = "序号";
            head[1] = "服务站代码";
            head[2] = "服务站名称";
            head[3] = "发运计划单号";
            head[4] = "订单号";
//            head[5] = "装箱号";
            head[5] = "随车物流名称";
            head[6] = "随车发运单号";
            head[7] = "随车发运日期";
            head[8] = "物流名称";
            head[9] = "物流单号";
            head[10] = "物流发运时间";
            head[11] = "是否已收货";
            
            PageResult<Map<String, Object>> ps = dao.queryGXWL(request, 1, Constant.PAGE_SIZE_MAX);
            List<Map<String, Object>> list = ps.getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[14];
                        detail[0] = CommonUtils.checkNull((i+1));
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("PLAN_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("CODER_CODE"));
//                        detail[5] = CommonUtils.checkNull(map.get("PKG_NO"));
                        detail[5] = CommonUtils.checkNull(map.get("LOGI_NAME"));
                        detail[6] = CommonUtils.checkNull(map.get("ASS_NO"));
                        detail[7] = CommonUtils.checkNull(map.get("ASS_DATE"));
                        detail[8] = CommonUtils.checkNull(map.get("TRANS_ORG"));
                        detail[9] = CommonUtils.checkNull(map.get("WL_NO"));
                        detail[10] = CommonUtils.checkNull(map.get("WL_DATE"));
                        detail[11] = CommonUtils.getCodeDesc(CommonUtils.checkNull(map.get("STATE")));
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
            act.setForword(INIT_URL);
        }
    }
	

	//物流信息表导出
	 public Object exportEx(ResponseWrapper response,
            RequestWrapper request, String[] head, List<String[]> list, int flag)
       throws Exception {

		String name = "广宣品物流信息查询.xls";
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
