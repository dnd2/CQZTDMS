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
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class PartBoRateReport implements PTConstants{

	public Logger logger = Logger.getLogger(PartBoRateReport.class);
	private PartBoDao dao = PartBoDao.getInstance();
	

	/**
	 * 
	 * @Title :
	 * @Description: 查询初始化
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-9-16
	 */
	public void partBoRateReportGyzxInit() {
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
			act.setForword(PART_BO_RATEGYZX_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "BO率报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void queryPartBoRateGyzx(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			//获取当前供应中心的仓库
			TtPartWarehouseDefinePO po = new TtPartWarehouseDefinePO();
			po.setOrgId(CommonUtils.parseLong(dealerId));
			po = (TtPartWarehouseDefinePO) dao.select(po).get(0);
			long whId = po.getWhId();
			
            Map<String,Object> map = dao.queryBoRateRepGSum(request,dealerId,whId);
            act.setOutData("bozxs",map==null?0:((BigDecimal)map.get("BOXS")).longValue());
            act.setOutData("salzxs",map==null?0:((BigDecimal)map.get("XSXS")).longValue());
            act.setOutData("bolv",map==null?0:(String)map.get("BOLV"));
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartBoRateGList(
					request,dealerId,whId, curPage, Constant.PAGE_SIZE);
			// 分页方法 end

			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "BO率报表");
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
	 * LastDate    : 2013年11月18日
	 */
	public void expPartBoRateGExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			//获取当前供应中心的仓库
			TtPartWarehouseDefinePO po = new TtPartWarehouseDefinePO();
			po.setOrgId(CommonUtils.parseLong(dealerId));
			po = (TtPartWarehouseDefinePO) dao.select(po).get(0);
			long whId = po.getWhId();
			
			String[] head = new String[14];
			head[0] = "供应中心代码";
			head[1] = "供应中心";
			head[2] = "配件编码";
			head[3] = "配件名称";
			head[4] = "配件件号";
			head[5] = "配件类型";
			head[6] = "当前可用库存";
			head[7] = "订货数量";
			head[8] = "订货金额";
			head[9] = "已交货数量";
			head[10] = "BO数量";
			head[11] = "BO金额";
			head[12] = "已满足数量";
			List<Map<String, Object>> list = dao.queryPartRateGyzx(request,dealerId,whId);
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[14];
						detail[0] = CommonUtils.checkNull(map.get("SELLER_CODE"));
						detail[1] = CommonUtils.checkNull(map.get("SELLER_NAME"));
						detail[2] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
						detail[3] = CommonUtils.checkNull(map.get("PART_CNAME"));
						detail[4] = CommonUtils.checkNull(map.get("PART_CODE"));
						detail[5] = CommonUtils.checkNull(map.get("PART_TYPE"));
						detail[6] = CommonUtils.checkNull(map.get("NORMAL_QTY"));
						detail[7] = CommonUtils.checkNull(map.get("BUY_QTY"));
						detail[8] = CommonUtils.checkNull(map.get("BUY_AMOUNT"));
						detail[9] = CommonUtils.checkNull(map.get("SALES_QTY"));
						detail[10] = CommonUtils.checkNull(map.get("BO_QTY"));
						detail[11] = CommonUtils.checkNull(map.get("BO_AMOUNT"));
						detail[12] = CommonUtils.checkNull(map.get("TOSAL_QTY"));
						
						list1.add(detail);
					}
				}
				this.exportEx(ActionContext.getContext().getResponse(),
						request, head, list1, "配件BO率报表.xls");
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
			logger.error(logonUser, e1);
			act.setException(e1);
			act.setForword(PART_BO_RATEGYZX_QUERY_URL);
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
