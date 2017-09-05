package com.infodms.dms.actions.report.partReport.partStockReport;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderInDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class PartCheckReport implements PTConstants{

	public Logger logger = Logger.getLogger(PurOrderInReport.class);
	private PurchaseOrderInDao dao = PurchaseOrderInDao.getInstance();
	

	/**
	 * 
	 * @Title      : 
	 * @Description: 库存盘点统计初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-23
	 */
	public void partCheckReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setOutData("old",CommonUtils.getMonthFirstDay());
            act.setOutData("now",CommonUtils.getDate());
			act.setForword(PART_CHECKREPORT_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存盘点");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 库存盘点统计初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-23
	 */
	public void partCheckGyzxReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			int flag = 0;//默认是车厂,0表示车厂,1表示供应中心
			if(logonUser.getDealerId()!=null){
				flag = 1;
				TmDealerPO po = new TmDealerPO();
				po.setDealerId(Long.parseLong(logonUser.getDealerId()));
				po = (TmDealerPO) dao.select(po).get(0);
				act.setOutData("venderId", po.getDealerId());
				act.setOutData("venderCode", po.getDealerCode());
				act.setOutData("venderName", po.getDealerName());
			}
			act.setOutData("flag", flag);
			act.setOutData("old",CommonUtils.getMonthFirstDay());
            act.setOutData("now",CommonUtils.getDate());
			act.setForword(PART_CHECKREPORTGYZX_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存盘点");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void queryPartCheck(){
		ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartCheckList(request,
                    curPage,logonUser,0, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库存盘点");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
	}
	
	
	public void queryPartCheckGyzx(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					PageResult<Map<String, Object>> ps = dao.queryPartCheckList(request,
							curPage,logonUser,1, Constant.PAGE_SIZE);
					//分页方法 end
					act.setOutData("ps", ps);
		} catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库存盘点");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	

	/**
	 * 
	 * @Title      : 
	 * @Description: 导出 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-23
	 */
	public void expPartCheckExcel(){
		 ActionContext act = ActionContext.getContext();
	        AclUserBean logonUser = (AclUserBean) act.getSession().get(
	                Constant.LOGON_USER);
	        RequestWrapper request = act.getRequest();
            String expFlag = CommonUtils.checkNull(request.getParamValue("expFlag"));
	        try {
	            String[] head;
	            if("0".equals(expFlag)){
	            	head = new String[8];
		            head[0] = "配件编码";
		            head[1] = "配件名称";
		            head[2] = "配件件号";
		            head[3] = "单位";
		            head[4] = "盘点数量";
		            head[5] = "盘点结果";
		            head[6] = "盘点日期";
	            }else{
	            	head = new String[10];
		            head[0] = "供应中心编码";
		            head[1] = "供应中心名称";
		            head[2] = "配件编码";
		            head[3] = "配件名称";
		            head[4] = "配件件号";
		            head[5] = "单位";
		            head[6] = "盘点数量";
		            head[7] = "盘点结果";
		            head[8] = "盘点日期";
	            }
	            List<Map<String, Object>> list = dao.queryPartCheck(request,logonUser,Integer.parseInt(expFlag));
	            List list1 = new ArrayList();
	            if (list != null && list.size() != 0) {
	                for (int i = 0; i < list.size(); i++) {
	                    Map map = (Map) list.get(i);
	                    if (map != null && map.size() != 0) {
	                        String[] detail = new String[head.length];
	                        if("0".equals(expFlag)){
	                        	detail[0] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
		                        detail[1] = CommonUtils.checkNull(map.get("PART_CNAME"));
		                        detail[2] = CommonUtils.checkNull(map
		                                .get("PART_CODE"));
		                        detail[3] = CommonUtils
		                                .checkNull(map.get("UNIT"));
		                        detail[4] = CommonUtils.checkNull(map
		                                .get("DIFF_QTY"));
		                        detail[5] = CommonUtils.checkNull(map
		                                .get("CHECK_RESULT"));
		                        detail[6] = CommonUtils.checkNull(map.get("CREATE_DATE"));
	                        }else{
	                        	detail[0] = CommonUtils.checkNull(map.get("CHGORG_CODE"));
	                        	detail[1] = CommonUtils.checkNull(map.get("CHGORG_CNAME"));
	                        	detail[2] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
		                        detail[3] = CommonUtils.checkNull(map.get("PART_CNAME"));
		                        detail[4] = CommonUtils.checkNull(map
		                                .get("PART_CODE"));
		                        detail[5] = CommonUtils
		                                .checkNull(map.get("UNIT"));
		                        detail[6] = CommonUtils.checkNull(map
		                                .get("DIFF_QTY"));
		                        detail[7] = CommonUtils.checkNull(map
		                                .get("CHECK_RESULT"));
		                        detail[8] = CommonUtils.checkNull(map.get("CREATE_DATE"));
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
	            if("0".equals(expFlag)){
	            	act.setForword(PART_CHECKREPORT_QUERY_URL);
	            }else{
	            	int flag = 0;//默认是车厂,0表示车厂,1表示供应中心
	    			if(logonUser.getDealerId()!=null){
	    				flag = 1;
	    				TmDealerPO po = new TmDealerPO();
	    				po.setDealerId(Long.parseLong(logonUser.getDealerId()));
	    				po = (TmDealerPO) dao.select(po).get(0);
	    				act.setOutData("venderId", po.getDealerId());
	    				act.setOutData("venderCode", po.getDealerCode());
	    				act.setOutData("venderName", po.getDealerName());
	    			}
	    			act.setOutData("old",CommonUtils.getMonthFirstDay());
	                act.setOutData("now",CommonUtils.getDate());
	    			act.setOutData("flag", flag);
	            	act.setForword(PART_CHECKREPORTGYZX_QUERY_URL);
	            }
	        }
	}
	
	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "库存盘点.xls";
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
                    if(CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])){
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                    }else{
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
