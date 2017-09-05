package com.infodms.dms.actions.sales.ordermanage.orderreport;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.sales.ordermanage.orderreport.DealerPriceQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class DealerPriceQuery {
	public Logger logger = Logger.getLogger(DealerPriceQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final DealerPriceQueryDAO dao = DealerPriceQueryDAO.getInstance();
	
	private final String dealerPriceQueryInit_URL = "/jsp/sales/ordermanage/orderreport/dealerPriceQueryInit.jsp";
	
	public void dealerPriceQueryInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(dealerPriceQueryInit_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商价格查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void dealerPriceQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String dealerId = "";
			if (!"".equals(areaId)) {
				dealerId =  areaId.split("\\|")[1];
			}else{
				Long companyId  = logonUser.getCompanyId();
				Long poseId = logonUser.getPoseId() ;
				
				DealerRelation dr = new DealerRelation() ;
				
				dealerId = dr.getDealerIdByPose(companyId, poseId) ;
			}
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.getDealerPriceList(dealerId, modelCode, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商价格查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void priceDownload() {
		AclUserBean logonUser = null ;
		OutputStream os = null;
		
		try {
			ResponseWrapper response = act.getResponse() ;
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
			
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String dealerId = null ;
			
			if (!"".equals(areaId)) {
				dealerId =  areaId.split("\\|")[1];
			}else{
				Long companyId  = logonUser.getCompanyId();
				Long poseId = logonUser.getPoseId() ;
				
				DealerRelation dr = new DealerRelation() ;
				
				dealerId = dr.getDealerIdByPose(companyId, poseId) ;
			}
			
			// 导出的文件名
			String fileName = "经销商价格列表.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();
			
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("车型代码");
			listTemp.add("车型名称");
			listTemp.add("配置代码");
			listTemp.add("配置名称");
			listTemp.add("价格");
			listTemp.add("生成日期");
			list.add(listTemp);
			
			PageResult<Map<String, Object>> ps = dao.getDealerPriceList(dealerId, modelCode, 60000, 1);
			
			List<Map<String, Object>> results = ps.getRecords() ;
			
			if(!CommonUtils.isNullList(results)) {
				int len = results.size() ;
				
				for(int i=0; i<len; i++) {
					Map<String, Object> record = results.get(i);
					listTemp = new LinkedList<Object>();
					listTemp.add(CommonUtils.checkNull(record.get("MODEL_CODE")));
					listTemp.add(CommonUtils.checkNull(record.get("MODEL_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
					listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("SALES_PRICE")));
					listTemp.add(CommonUtils.checkNull(record.get("CREATE_DATE")));
					list.add(listTemp);
				}
			}
			
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商价格列表下载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
