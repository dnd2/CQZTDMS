package com.infodms.dms.actions.report.partReport.partSalesReport;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.salesManager.PartBoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class PartOffReport implements PTConstants{

	public Logger logger = Logger.getLogger(PartOffReport.class);
	private PartBoDao dao = PartBoDao.getInstance();
	

	/**
	 * 
	 * @Title :
	 * @Description: 查询初始化
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-16
	 */
	public void partOffReportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(PART_OFF_REPORT_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件流失率分析报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-5
     * @Title :
     * @Description: 分页查询配件信息
     */
    public void queryPartInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldCode"));// 配件代码
            String partcode = CommonUtils.checkNull(request.getParamValue("partCode"));// 配件件号
            String flag = CommonUtils.checkNull(request.getParamValue("FLAG"));// flag
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));// 配件名称

            TtPartDefinePO bean = new TtPartDefinePO();
            bean.setPartOldcode(partOldcode);
            bean.setPartCname(partCname);
            bean.setPartCode(partcode);
            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartInfoList(flag,bean, curPage, 1500);
            // 分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 
     * @Title      : 
     * @Description: 配件流失率查询 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013年11月27日
     */
    public void queryPartOffInfo(){
    	ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
        	String partOldCodeStr = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
        	String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));
            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOffInfoList(request, curPage, Constant.PAGE_SIZE_PART_MINI);
            // 分页方法 end
            /*List<Map<String, Object>> list = ps.getRecords();
            if("1".equals(radioSelect)||"2".equals(radioSelect)){
            	long allOfQty = 0;
    			long allSbalQty = 0;
    			String allOfRate = "";
    			if (list != null && list.size() != 0) {
    				for (int i = 0; i < list.size(); i++) {
    					Map map = (Map) list.get(i);
    					if (map != null && map.size() != 0) {

    						Long ofQty = ((BigDecimal)map.get("OF_QTY")).longValue();
    						Long sbalQty = ((BigDecimal)map.get("SBAL_QTY")).longValue();

    						allOfQty = allOfQty+ofQty;
    						allSbalQty = allSbalQty+sbalQty;
    					}
    				}

    				DecimalFormat df=new DecimalFormat("0.00");

    				if(allSbalQty!=0&&allOfQty>0){
    					BigDecimal b1 = new BigDecimal(Double.toString(allOfQty));
    					BigDecimal b2 = new BigDecimal(Double.toString(allSbalQty));
    					double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
    					allOfRate = subZeroAndDot(df.format(s*100))+"%";
    				}else{
    					allOfRate = "-";
    				}
    				act.setOutData("allOfQty", allOfQty);
    				act.setOutData("allSbalQty", allSbalQty);
    				act.setOutData("allOfRate", allOfRate);
                }
            }else if("2".equals(radioSelect)){
            	int length = partOldCodeStr.split(",").length;
            	long allOfQty = 0;
    			long allSbalQty = 0;
    			String allOfRate = "";
    			
            	for(int i=0;i<length;i++){
            		long allPartQty = 0;
            		
            	}
            }else if("3".equals(radioSelect)){
            	long allInQty = 0;
            	long allOfQty = 0;
    			long allSbalQty = 0;
    			String allOfRate = "";
    			if (list != null && list.size() != 0) {
    				for (int i = 0; i < list.size(); i++) {
    					Map map = (Map) list.get(i);
    					if (map != null && map.size() != 0) {
    						
    						Long inQty = ((BigDecimal)map.get("IN_QTY")).longValue();
    						Long ofQty = ((BigDecimal)map.get("OF_QTY")).longValue();
    						Long sbalQty = ((BigDecimal)map.get("BAL_QTY")).longValue();
    						
    						allInQty = allInQty+inQty;
    						allOfQty = allOfQty+ofQty;
    						allSbalQty = allSbalQty+sbalQty;
    					}
    				}
    				
    				DecimalFormat df=new DecimalFormat("0.00");
    				
    				if(allSbalQty!=0&&allOfQty>0){
    					BigDecimal b1 = new BigDecimal(Double.toString(allOfQty));
    					BigDecimal b2 = new BigDecimal(Double.toString(allSbalQty));
    					double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
    					allOfRate = subZeroAndDot(df.format(s*100))+"%";
    				}else{
    					allOfRate = "-";
    				}
    				act.setOutData("allInQty", allInQty);
    				act.setOutData("allOfQty", allOfQty);
    				act.setOutData("allSbalQty", allSbalQty);
    				act.setOutData("allOfRate", allOfRate);
               }
            }*/
            act.setOutData("ps", ps);
            act.setOutData("partOldCodeStr", partOldCodeStr);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件流失率分析报表");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
        
    public static String subZeroAndDot(String s){  
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }
    
    /**
     * 
     * @Title      : 
     * @Description: 导出 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013年11月29日
     */
    public void expPartOffExcel(){
    	ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String partOldCodeStr = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
        	String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));
			
        	String[] partOldCodes = partOldCodeStr.split(",");
        	
			String[] head = null;
			
			long allInQty = 0;
        	long allOfQty = 0;
			long allSbalQty = 0;
			String allOfRate = "";
			
			List<Map<String, Object>> list = dao.queryPartOffs(request);
			List list1 = new ArrayList();
			
			if("1".equals(radioSelect)){
				head = new String[8+partOldCodes.length];
				head[0] = "月份";
				head[1] = "大区";
				head[2] = "省份";
				head[3] = "服务商编码";
				head[4] = "服务商名称";
				for(int i=0;i<partOldCodes.length;i++){
					head[i+5] = partOldCodes[i];
				}
				/*head[5+partOldCodes.length] = "流失量";
				head[6+partOldCodes.length] = "结算量";
				head[7+partOldCodes.length] = "流失率";*/
				
				if (list != null && list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						Map map = (Map) list.get(i);
						if (map != null && map.size() != 0) {
							String[] detail = new String[head.length];
							detail[0] = CommonUtils.checkNull(map.get("MONTH_NO"));
							detail[1] = CommonUtils.checkNull(map.get("ROOT_ORG_NAME"));
							detail[2] = CommonUtils.checkNull(map.get("REGION_NAME"));
							detail[3] = CommonUtils.checkNull(map.get("DEALER_CODE"));
							detail[4] = CommonUtils.checkNull(map.get("DEALER_NAME"));
							for(int j=0;j<partOldCodes.length;j++){
								detail[j+5] = CommonUtils.checkNull(map.get("XXX"+j));
							}
							/*detail[5+partOldCodes.length] = CommonUtils.checkNull(map.get("OF_QTY"));
							detail[6+partOldCodes.length] = CommonUtils.checkNull(map.get("SBAL_QTY"));
							detail[7+partOldCodes.length] = CommonUtils.checkNull(map.get("OF_RATIO"));
							
    						Long ofQty = ((BigDecimal)map.get("OF_QTY")).longValue();
    						Long sbalQty = ((BigDecimal)map.get("SBAL_QTY")).longValue();
							
    						allOfQty = allOfQty+ofQty;
    						allSbalQty = allSbalQty+sbalQty;*/
							
							list1.add(detail);
						}
					}
					
                    DecimalFormat df=new DecimalFormat("0.00");
    				
    				if(allSbalQty!=0&&allOfQty>0){
    					BigDecimal b1 = new BigDecimal(Double.toString(allOfQty));
    					BigDecimal b2 = new BigDecimal(Double.toString(allSbalQty));
    					double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
    					allOfRate = subZeroAndDot(df.format(s*100))+"%";
    				}else{
    					allOfRate = "-";
    				}
					
					/*String[] detail1 = new String[head.length];
					detail1[head.length-4]="合计：";
					detail1[head.length-3]=allOfQty+"";
					detail1[head.length-2]=allSbalQty+"";
					detail1[head.length-1]=allOfRate;
					
					list1.add(detail1);*/
					this.exportEx(ActionContext.getContext().getResponse(),
							request, head, list1, "配件流失率分析报表(按服务商).xls");
				} else {
					BizException e1 = new BizException(act,
							ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
					throw e1;
				}
			}else if("2".equals(radioSelect)){
				head = new String[6+partOldCodes.length];
				head[0] = "月份";
				head[1] = "大区";
				head[2] = "省份";
				for(int i=0;i<partOldCodes.length;i++){
					head[i+3] = partOldCodes[i];
				}
			/*	head[3+partOldCodes.length] = "流失量";
				head[4+partOldCodes.length] = "结算量";
				head[5+partOldCodes.length] = "流失率";*/
				
				if (list != null && list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						Map map = (Map) list.get(i);
						if (map != null && map.size() != 0) {
							String[] detail = new String[head.length];
							detail[0] = CommonUtils.checkNull(map.get("MONTH_NO"));
							detail[1] = CommonUtils.checkNull(map.get("ROOT_ORG_NAME"));
							detail[2] = CommonUtils.checkNull(map.get("DREGION_NAME"));
							for(int j=0;j<partOldCodes.length;j++){
								detail[j+3] = CommonUtils.checkNull(map.get("XXX"+j));
							}
							/*detail[3+partOldCodes.length] = CommonUtils.checkNull(map.get("OF_QTY"));
							detail[4+partOldCodes.length] = CommonUtils.checkNull(map.get("SBAL_QTY"));
							detail[5+partOldCodes.length] = CommonUtils.checkNull(map.get("OF_RATIO"));*/
							
							list1.add(detail);
						}
					}
					
					this.exportEx(ActionContext.getContext().getResponse(),
							request, head, list1, "配件流失率分析报表(按大区).xls");
				} else {
					BizException e1 = new BizException(act,
							ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
					throw e1;
				}
			}else if("3".equals(radioSelect)){
				head = new String[11];
				head[0] = "月份";
				head[1] = "服务商编码";
				head[2] = "服务商名称";
				head[3] = "配件编码";
				head[4] = "配件名称";
				head[5] = "配件件号";
				head[6] = "期初量";
				head[7] = "购进量";
				head[8] = "结算量";
				head[9] = "流失量";
				head[10] = "流失率";
				
				if (list != null && list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						Map map = (Map) list.get(i);
						if (map != null && map.size() != 0) {
							String[] detail = new String[head.length];
							detail[0] = CommonUtils.checkNull(map.get("MONTH_NO"));
							detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
							detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
							detail[3] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
							detail[4] = CommonUtils.checkNull(map.get("PART_CNAME"));
							detail[5] = CommonUtils.checkNull(map.get("PART_CODE"));
							detail[6] = CommonUtils.checkNull(map.get("BEG_QTY"));
							detail[7] = CommonUtils.checkNull(map.get("IN_QTY"));
							detail[8] = CommonUtils.checkNull(map.get("BAL_QTY"));
							detail[9] = CommonUtils.checkNull(map.get("OF_QTY"));
							detail[10] = CommonUtils.checkNull(map.get("OF_RATIO"));
							
							
							Long inQty = ((BigDecimal)map.get("IN_QTY")).longValue();
    						Long ofQty = ((BigDecimal)map.get("OF_QTY")).longValue();
    						Long sbalQty = ((BigDecimal)map.get("BAL_QTY")).longValue();
    						
    						allInQty = allInQty+inQty;
    						allOfQty = allOfQty+ofQty;
    						allSbalQty = allSbalQty+sbalQty;
							
							list1.add(detail);
						}
					}
					
                    DecimalFormat df=new DecimalFormat("0.00");
    				
    				if(allSbalQty!=0&&allOfQty>0){
    					BigDecimal b1 = new BigDecimal(Double.toString(allOfQty));
    					BigDecimal b2 = new BigDecimal(Double.toString(allSbalQty));
    					double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
    					allOfRate = subZeroAndDot(df.format(s*100))+"%";
    				}else{
    					allOfRate = "-";
    				}
					
					/*String[] detail1 = new String[head.length];
					detail1[head.length-5]="合计：";
					detail1[head.length-4]=allInQty+"";
					detail1[head.length-3]=allSbalQty+"";
					detail1[head.length-2]=allOfQty+"";
					detail1[head.length-1]=allOfRate;
					
					list1.add(detail1);*/
					this.exportEx(ActionContext.getContext().getResponse(),
							request, head, list1, "配件流失率分析报表(按明细).xls");
				} else {
					BizException e1 = new BizException(act,
							ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
					throw e1;
				}
			}else{
				head = new String[8+partOldCodes.length];
				head[0] = "服务商编码";
				head[1] = "服务商名称";
				for(int i=0;i<partOldCodes.length;i++){
					head[i+2] = partOldCodes[i];
				}
				/*head[5+partOldCodes.length] = "流失量";
				head[6+partOldCodes.length] = "结算量";
				head[7+partOldCodes.length] = "流失率";*/
				
				if (list != null && list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						Map map = (Map) list.get(i);
						if (map != null && map.size() != 0) {
							String[] detail = new String[head.length];
							detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
							detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
							for(int j=0;j<partOldCodes.length;j++){
								detail[j+2] = CommonUtils.checkNull(map.get("XXX"+j));
							}
							/*detail[5+partOldCodes.length] = CommonUtils.checkNull(map.get("OF_QTY"));
							detail[6+partOldCodes.length] = CommonUtils.checkNull(map.get("SBAL_QTY"));
							detail[7+partOldCodes.length] = CommonUtils.checkNull(map.get("OF_RATIO"));
							
    						Long ofQty = ((BigDecimal)map.get("OF_QTY")).longValue();
    						Long sbalQty = ((BigDecimal)map.get("SBAL_QTY")).longValue();
							
    						allOfQty = allOfQty+ofQty;
    						allSbalQty = allSbalQty+sbalQty;*/
							
							list1.add(detail);
						}
					}
					
                    /*DecimalFormat df=new DecimalFormat("0.00");
    				
    				if(allSbalQty!=0&&allOfQty>0){
    					BigDecimal b1 = new BigDecimal(Double.toString(allOfQty));
    					BigDecimal b2 = new BigDecimal(Double.toString(allSbalQty));
    					double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
    					allOfRate = subZeroAndDot(df.format(s*100))+"%";
    				}else{
    					allOfRate = "-";
    				}
					
					String[] detail1 = new String[head.length];
					detail1[head.length-4]="合计：";
					detail1[head.length-3]=allOfQty+"";
					detail1[head.length-2]=allSbalQty+"";
					detail1[head.length-1]=allOfRate;
					
					list1.add(detail1);*/
					this.exportEx(ActionContext.getContext().getResponse(),
							request, head, list1, "配件流失率分析报表(终端购进).xls");
				} else {
					BizException e1 = new BizException(act,
							ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
					throw e1;
				}
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
			act.setForword(PART_OFF_REPORT_QUERY_URL);
		}
    }
    
    public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list, String title)
			throws Exception {

		String name = title;
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
