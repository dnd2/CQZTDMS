package com.infodms.dms.actions.sales.planmanage.ProductPlan;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.planmanage.ProductPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class ProductPlanSearch {
	
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String productPlanConfrimSearchInit = "/jsp/sales/planmanage/productplan/productplansearchinit.jsp";

	/*
	 * 初始化页面，查询确认汇总查询
	 */
	public void productPlanSearchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		try {
		    List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
		    Map<String, Object> map=areaBusList.get(0);
		    //String areaId=map.get("AREA_ID").toString();
		    String curyear=PlanUtil.getCurrentYear();
		    String nextyear=(new Integer(curyear)+1)+"";
		    Integer maxver=dao.selectMaxPlanVer();
		    act.setOutData("curyear", curyear);
		    act.setOutData("nextyear", nextyear);
		    act.setOutData("areaBusList", areaBusList);
		    act.setOutData("maxver", maxver);
			act.setForword(productPlanConfrimSearchInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 查询最大的版本号
	 */
	public void selectMaxPlanVer(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();
			String year=request.getParamValue("year");
			String month=request.getParamValue("month");
			String areaId=request.getParamValue("areaId");
			Integer maxVer=dao.selectMaxPlanVer(year,month,areaId,logonUser.getCompanyId().toString());
			act.setOutData("maxVer", maxVer);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 月生产计划查询
	 */
	public void productPlanSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();
			String year=request.getParamValue("year");
			String month=request.getParamValue("month");
			String areaId=request.getParamValue("buss_area");
			String ver=request.getParamValue("plan_ver");
			String groupCode=request.getParamValue("groupCode")==null?"":request.getParamValue("groupCode");
			//查询年月的周次
			TmDateSetPO conPo=new TmDateSetPO();
			conPo.setSetYear(year);
			conPo.setSetMonth(month);
			conPo.setCompanyId(logonUser.getCompanyId());
			List<Map<String, Object>> weekList=PlanUtil.selectDateSetWeekList(conPo);
			
			Map<String, Object> conMap=new HashMap<String, Object>();
			conMap.put("year", year);
			conMap.put("month", month);
			conMap.put("areaId", areaId);
			conMap.put("ver", ver);
			conMap.put("groupCode",groupCode);
			conMap.put("companyId",logonUser.getCompanyId().toString());
			
			PageResult<Map<String, Object>> ps=null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")): 1;
			
			ps=dao.productPlanSearch(conMap,weekList, Constant.PAGE_SIZE, curPage);
			
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 生产计划表头查询
	 */
	public void productPlanThWeekSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request=act.getRequest();
			String year=request.getParamValue("year");
			String month=request.getParamValue("month");
			TmDateSetPO conPo=new TmDateSetPO();
			conPo.setSetYear(year);
			conPo.setSetMonth(month);
			conPo.setCompanyId(logonUser.getCompanyId());
			List<Map<String, Object>> weekList=PlanUtil.selectDateSetWeekList(conPo);
			
			act.setOutData("list", weekList);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void productPlanSearchExport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		OutputStream os = null;
		try {
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			String year = CommonUtils.checkNull(request.getParamValue("year"));
			String month = CommonUtils.checkNull(request.getParamValue("month"));
			String areaId = CommonUtils.checkNull(request.getParamValue("buss_area"));
			String ver = CommonUtils.checkNull(request.getParamValue("plan_ver"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));

			Map<String, Object> conMap=new HashMap<String, Object>();
			conMap.put("year", year);
			conMap.put("month", month);
			conMap.put("areaId", areaId);
			conMap.put("ver", ver);
			conMap.put("groupCode",groupCode);
			conMap.put("companyId",logonUser.getCompanyId().toString());
			
			TmDateSetPO conPo = new TmDateSetPO();
			conPo.setSetYear(year);
			conPo.setSetMonth(month);
			conPo.setCompanyId(logonUser.getCompanyId());
			List<Map<String, Object>> weekList=PlanUtil.selectDateSetWeekList(conPo);

			// 导出的文件名
			String fileName = "生产计划.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("配置代码");
			listTemp.add("配置名称");
			for (int i = 0; i < weekList.size(); i++) {
				Map<String, Object> map = weekList.get(i);
				listTemp.add(map.get("WEEK").toString() + "周");
			}
			listTemp.add("合计");
			list.add(listTemp);

			List<Map<String, Object>> results = dao.productPlanDownLoadSearch(conMap, weekList);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
				for (int j = 0; j < weekList.size(); j++) {
					listTemp.add(CommonUtils.checkNull(record.get("W" + j)));
				}
				listTemp.add(CommonUtils.checkNull(record.get("AMOUNT")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "最大配额总量查询");
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
}
