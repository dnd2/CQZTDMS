/**
 * @Title: UrgentOrderResQuery.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-27
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sales.ordermanage.orderreport;

import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.ordermanage.resourceQuery.ResourceQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class UrgentOrderResQuery {
	private Logger logger = Logger.getLogger(UrgentOrderResQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final OrderReportDao dao = OrderReportDao.getInstance();

	private final String URGENT_ORDER_RES_QUERY_URL = "/jsp/sales/ordermanage/orderreport/urgentOrderResQuery.jsp";// 补充订单资源查询

	public void urgentOrderResQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerBusiness(poseId.toString());
			act.setOutData("areaList", areaList);
			act.setForword(URGENT_ORDER_RES_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单资源查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void urgentOrderResQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);

			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));	//物料组
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));			//业务范围
			String resStatus = CommonUtils.checkNull(request.getParamValue("resStatus"));	//资源状态
			if("".equals(areaId)) {
				Long poseId = logonUser.getPoseId();
				List<Map<String, Object>> areaList = MaterialGroupManagerDao
						.getDealerBusiness(poseId.toString());
				for (int i=0; i< areaList.size();i++) {
					areaId += areaList.get(i).get("AREA_ID").toString()+",";
				}
				if (!"".equals(areaId)) {
					areaId = areaId.substring(0, areaId.length()-1);
				}
			}

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = null;
			
			String reqURL = act.getRequest().getContextPath(); // YH 2011.2.22
			String CVS_SERVICE = "/CVS-SERVICE";
			String CVS_SALES = "/CVS-SALES";
			if(CVS_SERVICE.equals(reqURL.toUpperCase())||CVS_SALES.equals(reqURL.toUpperCase())){
			  ps = ResourceQueryDAO.getDealerResourceQueryList2(resStatus, areaId , groupCode , oemCompanyId, Constant.PAGE_SIZE, curPage);	
			}else {
			  ps = ResourceQueryDAO.getDealerResourceQueryList(resStatus, areaId , groupCode , oemCompanyId, Constant.PAGE_SIZE, curPage);	
	
			}
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单资源查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 补充订单资源下载
	 *  add by yx 20110228
	 */
	public void urgentOrderResDownLoad(){
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		ResponseWrapper response = act.getResponse();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//		act.getSession().get(Constant.LOGON_USER);

		// 导出的文件名
		String fileName = "补充订单资源下载.csv";
		// 导出的文字编码
		OutputStream os = null;
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));	//物料组
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));			//业务范围
			String resStatus = CommonUtils.checkNull(request.getParamValue("resStatus"));	//资源状态
			if("".equals(areaId)) {
				Long poseId = logonUser.getPoseId();
				List<Map<String, Object>> areaList = MaterialGroupManagerDao
						.getDealerBusiness(poseId.toString());
				for (int i=0; i< areaList.size();i++) {
					areaId += areaList.get(i).get("AREA_ID").toString()+",";
				}
				if (!"".equals(areaId)) {
					areaId = areaId.substring(0, areaId.length()-1);
				}
			}

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
					
					
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("物料编号");
			listTemp.add("车型名称");
			listTemp.add("颜色");
			listTemp.add("资源情况");
			list.add(listTemp);
			PageResult<Map<String, Object>> ps = null;
			
			String reqURL = act.getRequest().getContextPath(); // YH 2011.2.22
			String CVS_SERVICE = "/CVS-SERVICE";
			String CVS_SALES = "/CVS-SALES";
			if(CVS_SERVICE.equals(reqURL.toUpperCase())||CVS_SALES.equals(reqURL.toUpperCase())){
			  ps = ResourceQueryDAO.getDealerResourceQueryList2(resStatus, areaId , groupCode , oemCompanyId, 100000, curPage);	
			}else {
			  ps = ResourceQueryDAO.getDealerResourceQueryList(resStatus, areaId , groupCode , oemCompanyId, 100000, curPage);	
	
			}
			List<Map<String, Object>> results = ps.getRecords();
			if(results!=null && !results.equals("")){
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("COLOR_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("RAMOUNT")));
				list.add(listTemp);
			}
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);		
			os.flush();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"补充订单资源下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
