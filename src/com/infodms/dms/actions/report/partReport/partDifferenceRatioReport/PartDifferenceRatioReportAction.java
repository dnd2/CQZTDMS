package com.infodms.dms.actions.report.partReport.partDifferenceRatioReport;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import jxl.Workbook;
import jxl.write.Label;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.partAbnormityManager.PartAbnormityDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartFixcodeDefinePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class PartDifferenceRatioReportAction implements PTConstants{

	public Logger logger = Logger.getLogger(PartDifferenceRatioReportAction.class);
	private static final String INIT_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/abnormityInStock.jsp";
	
	//查询初始化
	public void init(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Calendar c=Calendar.getInstance();
			c.set(Calendar.DATE, 1);//把日期设置为当月第一天
			c.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
			act.setOutData("start",c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-" + "1");
			act.setOutData("end", c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-" + c.get(Calendar.DATE));
			act.setForword(INIT_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"差异率查询错误,请联系管理员!");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	//查询
	public void query(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			PartAbnormityDao dao = PartAbnormityDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = null;
			ps = dao.query(request, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"差异率查询错误,请联系管理员!");
			logger.error(loginUser,e1);
			act.setException(e1);
			
		}
		
	}
	//导出查询
	public void expExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartAbnormityDao dao = PartAbnormityDao.getInstance();
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[13];
            head[0] = "序号";
            head[1] = "类型";
            head[2] = "发出数";
            head[3] = "差异数";
            head[4] = "差异率";
            
            PageResult<Map<String, Object>> ps = dao.query(request, 1, Constant.PAGE_SIZE_MAX);
            List<Map<String, Object>> list = ps.getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[13];
                        detail[0] = CommonUtils.checkNull((i+1));
                        detail[1] = CommonUtils.checkNull(map.get("PN"));
                        detail[2] = CommonUtils.checkNull(map.get("TRCNT"));
                        detail[3] = CommonUtils.checkNull(map.get("ECNT"));
                        detail[4] = CommonUtils.checkNull(map.get("WLRATE"));
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
	//导出表格
	 public Object exportEx(ResponseWrapper response,
	            RequestWrapper request, String[] head, List<String[]> list, int flag)
	       throws Exception {

			String name = "差异率查询.xls";
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
