package com.infodms.dms.actions.sales.planmanage.RequirementForecast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanParaBean;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.planmanage.OemRequireForecastReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtVsMonthlyForecastDetailPO;
import com.infodms.dms.po.TtVsMonthlyForecastPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

public class RequireForecastManage {

	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String requireForecastReportInitUrl = "/jsp/sales/planmanage/requireforecast/requireforecastreport.jsp";
	private final String requireForecastReportOutDateUrl = "/jsp/sales/planmanage/requireforecast/requireforecastreportoutdate.jsp";
	private final String requireForecastReportOpeUrl = "/jsp/sales/planmanage/requireforecast/requireforecastreportope.jsp";
	private final String requireForecastReportComUrl = "/jsp/sales/planmanage/requireforecast/requireforecastreportcomplete.jsp";
	private final String unReportRequireForecastReportComUrl = "/jsp/sales/planmanage/requireforecast/requireforecastunreportdealers.jsp";
	private final String unReportRequireForecastReportComOrgUrl = "/jsp/sales/planmanage/requireforecast/requireforecastunreportOrgdealers.jsp";
	private final String dealerDetailUrl = "/jsp/sales/planmanage/requireforecast/requireforecastdealersdetail.jsp";
	private final String areaDetailUrl = "/jsp/sales/planmanage/requireforecast/requireforecastareasdetail.jsp";
	private final String lessReportRequireForecastReportComUrl = "/jsp/sales/planmanage/requireforecast/requireforecastlessmonthplandealers.jsp";
	private final String requireForecastHistoryInitUrl = "/jsp/sales/planmanage/requireforecast/requireforecasthistoryshow.jsp";
	private OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();

	public void requireForecastReportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {

			PlanParaBean bean = PlanUtil.selectForecastParas();
			int curDay = PlanUtil.getCurrentDay();
            Long orgId=logonUser.getOrgId();
			// 如果是区域用户登陆并且已经超过规定预测日期，不能进行预测
			if (logonUser.getDutyType().equals(Constant.DUTY_TYPE_LARGEREGION.toString())) {
				if (curDay > bean.getSbuForecastEndDay() || curDay < bean.getSbuForecastStartDay()) {
					act.setForword(requireForecastReportOutDateUrl);
				} else {
					List<Map<String, Object>> areaBusList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
					List<Map<String, Object>> mapList = getForecastDate(bean);
					act.setOutData("mapList", mapList);
					act.setOutData("areaBusList", areaBusList);
					act.setForword(requireForecastReportInitUrl);
                    act.setOutData("orgId",orgId);
				}
			} else {
				List<Map<String, Object>> areaBusList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
				List<Map<String, Object>> mapList = getForecastDate(bean);
				act.setOutData("mapList", mapList);
				act.setOutData("areaBusList", areaBusList);
				act.setForword(requireForecastReportInitUrl);
                act.setOutData("orgId",orgId);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 取得查询日期，展示月数，提前预测月数，0表示从当前月开始预测，每次预测月数（开始预测月+每次预测月数）
	 */
	private List<Map<String, Object>> getForecastDate(PlanParaBean bean) {
		List<Map<String, Object>> list = new ArrayList();
		Map<String, Object> map = new HashMap<String, Object>();
		int his = bean.getShowHistoryMonthAmt();// 展示月数，
		int start = -his + bean.getForecastPreMonth();
		int end = bean.getForecastPreMonth() + bean.getForecastMonthAmt();// 每次预测月数（开始预测月+每次预测月数）
		for (int i = start; i < end; i++) {
			list.add(PlanUtil.getMapDate(i));
		}
		return list;
	}

	/*
	 * 取得查询日期，展示月数，提前预测月数，0表示从当前月开始预测，每次预测月数（开始预测月+每次预测月数）
	 */
	private List<Map<String, Object>> getForecastDateAmt(PlanParaBean bean) {
		List<Map<String, Object>> list = new ArrayList();
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = bean.getForecastPreMonth(); i < bean.getForecastMonthAmt() + bean.getForecastPreMonth(); i++) {
			list.add(PlanUtil.getMapDate(i));
		}
		return list;
	}

	public void requireForecastReportModelSearch() { // YH 2011.6.25
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			// 如果是部门，查询用ORG_ID是PARENT_ORG_ID，这里判断是不是大区，即经销商的上级
			String duty = logonUser.getDutyType();
			String orgLevel = "";
			String orgId = logonUser.getOrgId().toString();
			String porgId = logonUser.getParentOrgId();
			if (null == duty || "".equals(duty)) {
				duty = "0";
			}
			if (new Integer(duty).intValue() == Constant.DUTY_TYPE_LARGEREGION.intValue()) {
				orgLevel = "sub";
			}
			if (new Integer(duty).intValue() == Constant.DUTY_TYPE_DEPT.intValue()) {
				orgLevel = "dept";
			}

			//String areaId = request.getParamValue("areaId");
			//areaId = areaId.substring(0, areaId.length() - 1);

			String orgCodes = CommonUtils.checkNull(request.getParamValue("orgCode"));
			String orgIds = "";
			if (null != orgCodes && !"".equals(orgCodes)) {
				orgIds = getOrgIds(orgCodes);
				orgIds = CommonUtils.strToSql(orgIds, ",");
			}

			PlanParaBean bean = PlanUtil.selectForecastParas();
			if (null == bean) {
				throw new BizException(act, ErrorCodeConstant.INVALIDATION_DATA, "系统参数错误");
			}

			List<Map<String, Object>> mapList = getForecastDate(bean);

			// 目前单月预测，校验如果存在已经上报数据，则不返回数据
			List<Map<String, Object>> cList = getForecastDateAmt(bean);
			Map<String, Object> map = cList.get(0);
			String year = (String) map.get("YEAR");
			String month = (String) map.get("MONTH");
			TtVsMonthlyForecastPO conPo = new TtVsMonthlyForecastPO();
			//conPo.setAreaId(new Long(areaId));
			conPo.setOrgId(new Long(orgId));
			conPo.setForecastYear(new Integer(year));
			conPo.setForecastMonth(new Integer(month));
			conPo.setOrgType(Constant.ORG_TYPE_OEM);
			conPo.setCompanyId(logonUser.getCompanyId());
			conPo.setStatus(Constant.FORECAST_STATUS_CONFIRM);
			conPo.setForecastType(Constant.FORECAST_STATUS_FORMAL);//zxf
			// 校验用LIST，SIZE>0则不能操作,如果预测第一个月已确认，那么不能操作
			List<PO> checkList = dao.select(conPo);
			List<Map<String, Object>> infoList = null;
			List<Map<String, Object>> infoList_f = new ArrayList();
			boolean isSubmit = false;//zxf 是否已经提交
			if(checkList.size() > 0){
				for(int i=0;i<checkList.size();i++){
					conPo = (TtVsMonthlyForecastPO)checkList.get(i);
					if (null != conPo.getSubStatus() && 20381001 == conPo.getSubStatus()&&conPo.getForecastType()==60591001){
						isSubmit = true;
					}
				}
			}
			if (isSubmit) {				
				Map sub_map = new HashMap();
				sub_map.put("GROUP_ID", "");
				sub_map.put("SERIES_NAME", "已上报,等待审核");
				sub_map.put("D0", "");
				sub_map.put("S0", "");
				sub_map.put("D1", "");
				sub_map.put("S1", "");
				sub_map.put("D2", "");
				sub_map.put("S2", "");
				sub_map.put("MODEL_CODE", "");
				sub_map.put("SERIES_CODE", "");
				sub_map.put("MODEL_NAME", "");
				infoList_f.add(sub_map);				
			} else {
				conPo.setStatus(Constant.FORECAST_STATUS_UNCONFIRM);
				List<PO> checkList2 = dao.select(conPo);
				if (checkList2.size() > 0) {
					conPo = (TtVsMonthlyForecastPO) checkList2.get(0);
				}

				if (null != orgCodes && !"".equals(orgCodes)) {
					//orgId = orgIds;//注释原因是，驳回大区选择框不能作用查询本界面的查询条件使用。
					
				} else {
					orgId = CommonUtils.strToSql(orgId, ",");
				}
				infoList = dao.selectOemRequireForecastModelInfo(porgId, orgId, mapList, orgLevel, logonUser.getCompanyId().toString());
				int subtotal_D0 = 0; // 按车系小计
				int subtotal_S0 = 0;
				int subtotal_D1 = 0;
				int subtotal_S1 = 0;
				//int subtotal_D2 = 0;  //zxf
				//int subtotal_S2 = 0;

				int total_D0 = 0; // 按车系合计
				int total_S0 = 0;
				int total_D1 = 0;
				int total_S1 = 0;
				//int total_D2 = 0;  //zxf
				//int total_S2 = 0;

				for (int i = 0; i < infoList.size(); i++) { // 计算小计合计 YH
															// 2011.6.20
					Map map1 = (Map) infoList.get(i);
					Map map_f = new HashMap();
					map_f.put("GROUP_ID", map1.get("GROUP_ID"));
					map_f.put("SERIES_NAME", map1.get("SERIES_NAME"));
					map_f.put("D0", map1.get("D0"));
					map_f.put("S0", map1.get("S0"));
					map_f.put("D1", map1.get("D1"));
					map_f.put("S1", map1.get("S1"));
					map_f.put("D2", map1.get("D2"));
					map_f.put("S2", map1.get("S2"));
					map_f.put("MODEL_CODE", map1.get("MODEL_CODE"));
					map_f.put("SERIES_CODE", map1.get("SERIES_CODE"));
					map_f.put("MODEL_NAME", map1.get("MODEL_NAME"));

					subtotal_D0 = subtotal_D0 + Integer.parseInt(map1.get("D0").toString());
					subtotal_S0 = subtotal_S0 + Integer.parseInt(map1.get("S0").toString());
					subtotal_D1 = subtotal_D1 + Integer.parseInt(map1.get("D1").toString());
					subtotal_S1 = subtotal_S1 + Integer.parseInt(map1.get("S1").toString());
					//subtotal_D2 = subtotal_D2 + Integer.parseInt(map1.get("D2").toString());//zxf
					//subtotal_S2 = subtotal_S2 + Integer.parseInt(map1.get("S2").toString());

					total_D0 = total_D0 + Integer.parseInt(map1.get("D0").toString());
					total_S0 = total_S0 + Integer.parseInt(map1.get("S0").toString());
					total_D1 = total_D1 + Integer.parseInt(map1.get("D1").toString());
					total_S1 = total_S1 + Integer.parseInt(map1.get("S1").toString());
					//total_D2 = total_D2 + Integer.parseInt(map1.get("D2").toString()); //zxf
					//total_S2 = total_S2 + Integer.parseInt(map1.get("S2").toString());

					infoList_f.add(map_f);

					if (i + 1 < infoList.size()) {
						Map map2 = infoList.get(i + 1);
						String SERIES_CODE_1 = map1.get("SERIES_CODE").toString();
						String SERIES_CODE_2 = map2.get("SERIES_CODE").toString();
						if (!SERIES_CODE_1.equals(SERIES_CODE_2)) {
							Map map_sub = new HashMap();
							map_sub.put("GROUP_ID", "");
							map_sub.put("SERIES_NAME", "----小计----");
							map_sub.put("D0", subtotal_D0);
							map_sub.put("S0", subtotal_S0);
							map_sub.put("D1", subtotal_D1);
							map_sub.put("S1", subtotal_S1);
							//map_sub.put("D2", subtotal_D2); //zxf
							//map_sub.put("S2", subtotal_S2);
							map_sub.put("MODEL_CODE", "");
							map_sub.put("SERIES_CODE", "");
							map_sub.put("MODEL_NAME", "");

							infoList_f.add(map_sub);

							subtotal_D0 = 0; // 按车系小计
							subtotal_S0 = 0;
							subtotal_D1 = 0;
							subtotal_S1 = 0;
							//subtotal_D2 = 0; //zxf
							//subtotal_S2 = 0;
						}
					} else {
						Map map_sub = new HashMap();
						map_sub.put("GROUP_ID", "");
						map_sub.put("SERIES_NAME", "----小计----");
						map_sub.put("D0", subtotal_D0);
						map_sub.put("S0", subtotal_S0);
						map_sub.put("D1", subtotal_D1);
						map_sub.put("S1", subtotal_S1);
						//map_sub.put("D2", subtotal_D2); //zxf
						//map_sub.put("S2", subtotal_S2);
						map_sub.put("MODEL_CODE", "");
						map_sub.put("SERIES_CODE", "");
						map_sub.put("MODEL_NAME", "");

						infoList_f.add(map_sub);

						Map map_tota = new HashMap();
						map_tota.put("GROUP_ID", "");
						map_tota.put("SERIES_NAME", "----合计----");
						map_tota.put("D0", total_D0);
						map_tota.put("S0", total_S0);
						map_tota.put("D1", total_D1);
						map_tota.put("S1", total_S1);
						//map_tota.put("D2", total_D2); //zxf
						//map_tota.put("S2", total_S2);
						map_tota.put("MODEL_CODE", "");
						map_tota.put("SERIES_CODE", "");
						map_tota.put("MODEL_NAME", "");

						infoList_f.add(map_tota);
					}
				}
				if (null != conPo.getSubStatus() && 20381004 == conPo.getSubStatus()) {
					Map sub_map = new HashMap();
					sub_map.put("GROUP_ID", "");
					sub_map.put("SERIES_NAME", "总部已驳回，需重新上报");
					sub_map.put("D0", "");
					sub_map.put("S0", "");
					sub_map.put("D1", "");
					sub_map.put("S1", "");
					sub_map.put("D2", "");
					sub_map.put("S2", "");
					sub_map.put("MODEL_CODE", "");
					sub_map.put("SERIES_CODE", "");
					sub_map.put("MODEL_NAME", "");

					infoList_f.add(sub_map);
				}
			}
			//act.setOutData("areaId", areaId);
			act.setOutData("mapList", mapList);
			act.setOutData("infoList", infoList_f);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 预测查询 需要查询参数，确定预测时间浮动
	 */
	public void requireForecastSearch() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		try {

			// 如果是部门，查询用ORG_ID是PARENT_ORG_ID，这里判断是不是大区，即经销商的上级
			String duty = logonUser.getDutyType();
			String orgLevel = "";
			String orgId = logonUser.getOrgId().toString();
			String porgId = logonUser.getParentOrgId();
			if (null == duty || "".equals(duty)) {
				duty = "0";
			}
			if (new Integer(duty).intValue() == Constant.DUTY_TYPE_LARGEREGION.intValue()) {
				orgLevel = "sub";
			}
			if (new Integer(duty).intValue() == Constant.DUTY_TYPE_DEPT.intValue()) {
				orgLevel = "dept";
			}

			RequestWrapper request = act.getRequest();
			//String areaId = request.getParamValue("area");
			String modelId = request.getParamValue("modelId");

			PlanParaBean bean = PlanUtil.selectForecastParas();
			List<Map<String, Object>> dateList = getForecastDateAmt(bean);

			TmVhclMaterialGroupPO gconPo = new TmVhclMaterialGroupPO();
			gconPo.setGroupId(new Long(modelId));
			List<PO> glist = dao.select(gconPo);
			if (null == glist || glist.size() == 0) {
				throw new Exception("车型不存在");
			}
			TmVhclMaterialGroupPO gpo = (TmVhclMaterialGroupPO) glist.get(0);
			String modelName = gpo.getGroupName();
			List<Map<String, Object>> ps = null;
			// 这里要做判断，总部和区域要分开SQL查询
			ps = dao.selectOemForecastOpeList(porgId, modelId, orgId,  dateList, orgLevel, logonUser.getCompanyId().toString());
			act.setOutData("ps", ps);
			act.setOutData("dateList", dateList); // 预测月份
			act.setOutData("modelName", modelName);// 预测车型
			//act.setOutData("areaId", areaId);
			act.setForword(requireForecastReportOpeUrl);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 保存需求预测
	 */
	public void requireForecastSave() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		try {
			RequestWrapper request = act.getRequest();
			//String areaId = request.getParamValue("areaId");
			String orgId = logonUser.getOrgId().toString();

			Enumeration<String> params = request.getParamNames();
			boolean bl = true;// 以防万一，没啥用，如果第一月已经确认，那不可操作，这是用来确定
								// 是否是第一个月，如果是，查询未确认状态的，否则查所有清掉
			while (params.hasMoreElements()) {
				String elem = (String) params.nextElement();
				if (elem.indexOf("ipt") != -1) {
					String amt = request.getParamValue(elem);
					if (null == amt) {
						amt = "0";
					}
					elem = elem.substring(3, elem.length());
					String[] gym = elem.split("_");
					String groupId = gym[0];
					String year = gym[1];
					String month = gym[2];

					List<Map<String, Object>> clrList_ = dao.selectClrOemForecast(year, month, orgId, "", logonUser.getCompanyId().toString(), bl);
					List<Map<String, Object>> clrList = dao.selectClrOemForecast(year, month, orgId, groupId, logonUser.getCompanyId().toString(), bl);
					if (null != clrList_ && clrList_.size() > 0) {
						Long forecastId_ = new Long(clrList_.get(0).get("FORECAST_ID").toString());
						if (null != clrList && clrList.size() > 0) {
							Long detailId_ = new Long(clrList.get(0).get("DETAIL_ID").toString());
							TtVsMonthlyForecastDetailPO detailPo_ = new TtVsMonthlyForecastDetailPO();
							detailPo_.setDetailId(detailId_);
							TtVsMonthlyForecastDetailPO detailPo = new TtVsMonthlyForecastDetailPO();
							detailPo.setDetailId(detailId_);
							detailPo.setForecastAmount(new Integer(amt));

							dao.update(detailPo_, detailPo);
						} else {
							TtVsMonthlyForecastDetailPO detailPo = new TtVsMonthlyForecastDetailPO();
							detailPo.setDetailId(new Long(SequenceManager.getSequence("")));
							detailPo.setForecastId(forecastId_);
							detailPo.setGroupId(new Long(groupId));
							detailPo.setForecastAmount(new Integer(amt));
							detailPo.setCreateBy(logonUser.getUserId());
							detailPo.setCreateDate(new Date());

							dao.insert(detailPo);
						}
					} else {
						// 保存数据
						TtVsMonthlyForecastPO mainPo = new TtVsMonthlyForecastPO();
						Long mseq = new Long(SequenceManager.getSequence(""));
						mainPo.setForecastId(mseq);
						mainPo.setCompanyId(logonUser.getCompanyId());
						mainPo.setOrgId(new Long(orgId));
						//mainPo.setAreaId(new Long(areaId));
						mainPo.setForecastYear(new Integer(year));
						mainPo.setForecastMonth(new Integer(month));
						mainPo.setOrgType(Constant.ORG_TYPE_OEM);
						mainPo.setStatus(Constant.FORECAST_STATUS_UNCONFIRM);
						mainPo.setCreateBy(logonUser.getUserId());
						mainPo.setCreateDate(new Date());
						Calendar cal = Calendar.getInstance();  //zxf PlanUtil.getRadomDate
				        cal.add(cal.MONTH, 1);
				        String m = (cal.get(Calendar.MONTH)+1)+"";
				        if(m.equals(month)){//等于下一个月
				        	mainPo.setForecastType(60591001);
				        }
				        else{
				        	mainPo.setForecastType(60591002);
				        }
						dao.insert(mainPo);

						TtVsMonthlyForecastDetailPO detailPo = new TtVsMonthlyForecastDetailPO();
						detailPo.setDetailId(new Long(SequenceManager.getSequence("")));
						detailPo.setForecastId(mseq);
						detailPo.setGroupId(new Long(groupId));
						detailPo.setForecastAmount(new Integer(amt));
						detailPo.setCreateBy(logonUser.getUserId());
						detailPo.setCreateDate(new Date());

						dao.insert(detailPo);
					}

					/*
					 * //清空 当前登陆经销商年、月、业务范围内、未提报车辆配置的需求预测, List<Map<String,
					 * Object>> clrList=dao.selectClrOemForecast(year, month,
					 * areaId, orgId,
					 * groupId,logonUser.getCompanyId().toString(), bl);
					 * bl=false; if(null!=clrList&&clrList.size()>0){ for(int
					 * i=0;i<clrList.size();i++){ Map<String, Object>
					 * map=clrList.get(i); Long forecastId=new
					 * Long(map.get("FORECAST_ID").toString());
					 * TtVsMonthlyForecastDetailPO clrDPo=new
					 * TtVsMonthlyForecastDetailPO();
					 * clrDPo.setForecastId(forecastId); TtVsMonthlyForecastPO
					 * clrPo=new TtVsMonthlyForecastPO();
					 * clrPo.setForecastId(forecastId); dao.delete(clrDPo);
					 * dao.delete(clrPo); } }
					 * 
					 * TtVsMonthlyForecastPO mainPo=new TtVsMonthlyForecastPO();
					 * mseq=new Long(SequenceManager.getSequence(""));
					 * mainPo.setForecastId(mseq);
					 * mainPo.setCompanyId(logonUser.getCompanyId());
					 * mainPo.setOrgId(new Long(orgId)); mainPo.setAreaId(new
					 * Long(areaId)); mainPo.setForecastYear(new Integer(year));
					 * mainPo.setForecastMonth(new Integer(month));
					 * mainPo.setOrgType(Constant.ORG_TYPE_OEM);
					 * mainPo.setStatus(Constant.FORECAST_STATUS_UNCONFIRM);
					 * mainPo.setCreateBy(logonUser.getUserId());
					 * mainPo.setCreateDate(new Date());
					 * 
					 * dao.insert(mainPo);
					 * 
					 * TtVsMonthlyForecastDetailPO detailPo=new
					 * TtVsMonthlyForecastDetailPO(); detailPo.setDetailId(new
					 * Long(SequenceManager.getSequence("")));
					 * detailPo.setForecastId(mseq); detailPo.setGroupId(new
					 * Long(groupId)); detailPo.setForecastAmount(new
					 * Integer(amt));
					 * detailPo.setCreateBy(logonUser.getUserId());
					 * detailPo.setCreateDate(new Date());
					 * 
					 * dao.insert(detailPo);
					 */
				} else {
					continue;
				}

			}
			//act.setOutData("areaId", areaId);
			act.setForword("/sales/planmanage/RequirementForecast/RequireForecastManage/requireForecastReportInit.do");

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 区域上报，总部调整完成 YH 2011.6.27
	 */

	public void requireForecastReport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		try {
			PlanParaBean bean = PlanUtil.selectForecastParas();
			List<Map<String, Object>> mapList = getForecastDate(bean);
			RequestWrapper request = act.getRequest();
			//String areaId = request.getParamValue("area");
			String orgId = logonUser.getOrgId().toString();

			for (int i = 0; i < mapList.size(); i++) {
				Map<String, Object> map = mapList.get(i);
				String year = (String) map.get("YEAR");
				String month = (String) map.get("MONTH");

				TtVsMonthlyForecastPO conPo = new TtVsMonthlyForecastPO();
				//conPo.setAreaId(new Long(areaId));
				conPo.setOrgId(new Long(orgId));
				conPo.setForecastYear(new Integer(year));
				conPo.setForecastMonth(new Integer(month));
				conPo.setStatus(Constant.FORECAST_STATUS_UNCONFIRM);
				conPo.setOrgType(Constant.ORG_TYPE_OEM);

				TtVsMonthlyForecastPO valPo = new TtVsMonthlyForecastPO();
				valPo.setStatus(Constant.FORECAST_STATUS_CONFIRM);
				valPo.setUpdateBy(logonUser.getUserId());
				valPo.setUpdateDate(new Date());
				valPo.setSubStatus(Constant.FORECAST_SUBSTATUS_1); // YH
																	// 2011.6.27
				dao.update(conPo, valPo);
			}
			act.setForword(requireForecastReportComUrl);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 转发未提报组织名单URL
	 */
	public void unreportForwardAction() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		try {
			RequestWrapper request = act.getRequest();
			String areaId = request.getParamValue("area");
			String org = request.getParamValue("org");
			act.setOutData("area", areaId);
			if("1".equals(org)){ //大区，sa车厂
				act.setForword(unReportRequireForecastReportComOrgUrl);
			}
			else{
				act.setForword(unReportRequireForecastReportComUrl);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 大区经销商明细
	 */
	public void dealerDetail() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		try {
			RequestWrapper request = act.getRequest();
			String areaId = request.getParamValue("area");
			act.setOutData("area", areaId);
			act.setForword(dealerDetailUrl);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 车厂大区明细
	 */
	public void areaDetail() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		try {
			RequestWrapper request = act.getRequest();
			String areaId = request.getParamValue("area");
			act.setOutData("area", areaId);
			act.setForword(areaDetailUrl);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/*
	 * 未提报组织名单查询
	 */
	public void selectUnreportForecastDealers() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		try {
			PlanParaBean bean = PlanUtil.selectForecastParas();
			List<Map<String, Object>> mapList = getForecastDateAmt(bean);
			RequestWrapper request = act.getRequest();
			//String areaId = request.getParamValue("area");
			//String org = request.getParamValue("org");
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String duty = logonUser.getDutyType();
			PageResult<Map<String, Object>> ps = null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

			if (new Integer(duty).intValue() == Constant.DUTY_TYPE_LARGEREGION.intValue()) 
			{				
				ps = dao.selectSubRequireForecastUnreportedDealers(orgId, "", logonUser.getCompanyId().toString(), mapList, curPage, 1000);
			} 
			else {
				ps = dao.selectDeptRequireForecastUnreportedOrg(orgId, logonUser.getCompanyId().toString(), mapList, curPage, 1000);				
			    //ps = dao.selectDeptRequireForecastUnreportedDealers(orgId, "", mapList, curPage, 1000);												
			}
			act.setOutData("ps", ps);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 大区查询经销商明细
	 */
	public void selectForecastDealers() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		try {
			PlanParaBean bean = PlanUtil.selectForecastParas();
			List<Map<String, Object>> mapList = getForecastDateAmt(bean);
			RequestWrapper request = act.getRequest();
			//String areaId = request.getParamValue("area");
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String duty = logonUser.getDutyType();
			String dealer_code = request.getParamValue("dealer_code");
			String dealer_name = request.getParamValue("dealer_name");

			PageResult<Map<String, Object>> ps = null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

			if (new Integer(duty).intValue() == Constant.DUTY_TYPE_LARGEREGION.intValue()) {
				ps = dao.selectSubRequireForecastDealersDetail(dealer_code, dealer_name, orgId, "", logonUser.getCompanyId().toString(), mapList, curPage, 1000);
			}
			act.setOutData("ps", ps);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/*
	 * 车厂查询经大区明细
	 */
	public void selectForecastAreas() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		try {
			PlanParaBean bean = PlanUtil.selectForecastParas();
			List<Map<String, Object>> mapList = getForecastDateAmt(bean);
			RequestWrapper request = act.getRequest();
			String areaId = request.getParamValue("area");
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String duty = logonUser.getDutyType();
			String area_code = request.getParamValue("area_code");
			String area_name = request.getParamValue("area_name");

			PageResult<Map<String, Object>> ps = null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

			if (new Integer(duty).intValue() == 10431001) {
				ps = dao.selectSubRequireForecastAreasDetail(area_code, area_name, orgId, areaId, logonUser.getCompanyId().toString(), mapList, curPage, 1000);
			}
			act.setOutData("ps", ps);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/** 经销商车型类别 */
	public static final String DEALER_GROUP_LEVEL = "2";

	/**
	 * 大区查询经销商明细时经销商车辆列的初始化 HXY 2011.12.22
	 */
	public void findDealerVehicleType() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		try {
			RequestWrapper request = act.getRequest();
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			List<Map<String, Object>> list = dao.findAreaVehicleTypeByGroupLevel(DEALER_GROUP_LEVEL);
			act.setOutData("groupList", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 *车厂大区明细时经销商车辆列的初始化 HXY 2011.12.22
	 */
	public void findAreaVehicleType() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		try {
			RequestWrapper request = act.getRequest();
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			List<Map<String, Object>> list = dao.findDealerVehicleTypeByGroupLevel(DEALER_GROUP_LEVEL);
			act.setOutData("groupList", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 跳转至提报预测数量小于月度任务数量的经销商（车系）
	 */
	public void lessReportForwardAction() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		try {
			RequestWrapper request = act.getRequest();
			String areaId = request.getParamValue("area");
			act.setOutData("area", areaId);
			act.setForword(lessReportRequireForecastReportComUrl);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 查询预测量小于目标量的组织名单
	 */
	public void selectLessForecastDealers() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		try {
			PlanParaBean bean = PlanUtil.selectForecastParas();
			List<Map<String, Object>> mapList = getForecastDateAmt(bean);
			RequestWrapper request = act.getRequest();
			String areaId = request.getParamValue("area");
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String duty = logonUser.getDutyType();

			PageResult<Map<String, Object>> ps = null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

			if (new Integer(duty).intValue() == Constant.DUTY_TYPE_LARGEREGION.intValue()) {
				ps = dao.selectSubForecastLessThanMonthPlanDealers(orgId, areaId, mapList, curPage, 1000);
			} else {
				ps = dao.selectDeptForecastLessThanMonthPlanDealers(parentOrgId, areaId, mapList, curPage, 1000);
			}
			act.setOutData("ps", ps);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 未提报组织预测下载
	 */
	public void oemRequireForecastUnreportExport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			PlanParaBean bean = PlanUtil.selectForecastParas();
			List<Map<String, Object>> mapList = getForecastDateAmt(bean);
			String areaId = request.getParamValue("area");
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String duty = logonUser.getDutyType();
			// 导出的文件名
			String fileName = "需求预测.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			List<List<Object>> list = null;
			if (new Integer(duty).intValue() == Constant.DUTY_TYPE_LARGEREGION.intValue()) {
				list = getSubUnReortDealerList(orgId, areaId, logonUser.getCompanyId().toString(), mapList);
			} else {
				//list = getDeptUnReortDealerList(orgId, areaId, mapList);  
				list = getDeptUnReortOrgList(orgId,logonUser.getCompanyId().toString(), mapList);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "最大配额总量查询");
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

	/*
	 * 区域未提报组织下载
	 */
	private List<List<Object>> getSubUnReortDealerList(String orgId, String areaId, String companyId, List<Map<String, Object>> mapList) {
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();

		List<Object> listTemp = new LinkedList<Object>();
		listTemp.add("大区名称");
		listTemp.add("经销商代码");
		listTemp.add("经销商名称");
		listTemp.add("预测月份");
		listTemp.add("联系人");
		listTemp.add("联系方式");
		list.add(listTemp);

		List<Map<String, Object>> results = dao.downLoadSubRequireForecastUnreportedDealers(orgId, areaId, companyId, mapList);

		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
			listTemp.add(CommonUtils.checkNull(record.get("FORECAST_MONTH")));
			listTemp.add(CommonUtils.checkNull(record.get("LINK_MAN")) + "");
			listTemp.add(CommonUtils.checkNull(record.get("PHONE")));

			list.add(listTemp);
		}
		return list;
	}

	/*
	 * 总部查未提报组织下载
	 */
	private List<List<Object>> getDeptUnReortDealerList(String parentOrgId, String areaId, List<Map<String, Object>> mapList) {
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();

		List<Object> listTemp = new LinkedList<Object>();
		listTemp.add("大区名称");
		listTemp.add("经销商代码");
		listTemp.add("经销商名称");
		listTemp.add("预测月份");
		listTemp.add("联系人");
		listTemp.add("联系方式");
		list.add(listTemp);

		List<Map<String, Object>> results = dao.downLoadDeptRequireForecastUnreportedDealers(parentOrgId, areaId, mapList);

		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
			listTemp.add(CommonUtils.checkNull(record.get("FORECAST_MONTH")));
			listTemp.add(CommonUtils.checkNull(record.get("LINK_MAN")) + "");
			listTemp.add(CommonUtils.checkNull(record.get("PHONE")));
			list.add(listTemp);
		}
		return list;
	}
	
	/*
	 * 总部查未提报组织下载
	 */
	private List<List<Object>> getDeptUnReortOrgList(String parentOrgId, String compandId, List<Map<String, Object>> mapList) {
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();
		
		List<Map<String, Object>> dealer_list = dao.findAreaVehicleTypeByGroupLevel(DEALER_GROUP_LEVEL);
		
		List<Object> listTemp = new LinkedList<Object>();
		listTemp.add("大区名称");
		listTemp.add("大域代码");
		listTemp.add("预测月份");
		for(int i=0;i<dealer_list.size();i++){
			String GROUP_CODE = dealer_list.get(i).get("GROUP_NAME").toString();
			listTemp.add(GROUP_CODE);
		}
		list.add(listTemp);

		List<Map<String, Object>> results = dao.selectDeptRequireForecastUnreportedOrgData(parentOrgId, compandId, mapList);

		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("ORG_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("FORECAST_MONTH")));
			for(int j=0;j<dealer_list.size();j++){
				listTemp.add("0");
			}
			list.add(listTemp);
		}
		return list;
	}
	
	/*
	 * 预测数量小月度目标预测下载
	 */
	public void oemRequireForecastLessExport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			PlanParaBean bean = PlanUtil.selectForecastParas();
			List<Map<String, Object>> mapList = getForecastDateAmt(bean);
			String areaId = request.getParamValue("area");
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String duty = logonUser.getDutyType();
			// 导出的文件名
			String fileName = "需求预测.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			List<List<Object>> list = null;
			if (new Integer(duty).intValue() == Constant.DUTY_TYPE_LARGEREGION.intValue()) {
				list = getSubLessReortDealerList(orgId, areaId, mapList);
			} else {
				list = getDeptLessReortDealerList(parentOrgId, areaId, mapList);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "最大配额总量查询");
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

	/*
	 * 区域预测数量小月度目标
	 */
	private List<List<Object>> getSubLessReortDealerList(String orgId, String areaId, List<Map<String, Object>> mapList) {
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();

		List<Object> listTemp = new LinkedList<Object>();
		listTemp.add("经销商代码");
		listTemp.add("经销商名称");
		listTemp.add("预测月份");
		listTemp.add("联系人");
		listTemp.add("联系方式");
		listTemp.add("预测数量");
		listTemp.add("目标数量");
		list.add(listTemp);

		List<Map<String, Object>> results = dao.downLoadSubForecastLessThanMonthPlanDealers(orgId, areaId, mapList);

		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
			listTemp.add(CommonUtils.checkNull(record.get("FORECAST_MONTH")));
			listTemp.add(CommonUtils.checkNull(record.get("LINK_MAN")) + "");
			listTemp.add(CommonUtils.checkNull(record.get("PHONE")));
			listTemp.add(CommonUtils.checkNull(record.get("AMOUNT")) + "");
			listTemp.add(CommonUtils.checkNull(record.get("SALE_AMOUNT")));
			list.add(listTemp);
		}
		return list;
	}

	/*
	 * 总部查预测数量小月度目标下载
	 */
	private List<List<Object>> getDeptLessReortDealerList(String parentOrgId, String areaId, List<Map<String, Object>> mapList) {
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();

		List<Object> listTemp = new LinkedList<Object>();
		listTemp.add("经销商代码");
		listTemp.add("经销商名称");
		listTemp.add("预测月份");
		listTemp.add("联系人");
		listTemp.add("联系方式");
		listTemp.add("预测数量");
		listTemp.add("目标数量");
		list.add(listTemp);

		List<Map<String, Object>> results = dao.downLoadDeptForecastLessThanMonthPlanDealers(parentOrgId, areaId, mapList);

		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
			listTemp.add(CommonUtils.checkNull(record.get("FORECAST_MONTH")));
			listTemp.add(CommonUtils.checkNull(record.get("LINK_MAN")) + "");
			listTemp.add(CommonUtils.checkNull(record.get("PHONE")));
			listTemp.add(CommonUtils.checkNull(record.get("AMOUNT")) + "");
			listTemp.add(CommonUtils.checkNull(record.get("SALE_AMOUNT")));
			list.add(listTemp);
		}
		return list;
	}

	/*
	 * 打开参考信息查看页
	 */
	public void requireForecastHistoryInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String groupId = request.getParamValue("groupId") == null ? "" : request.getParamValue("groupId");
			act.setOutData("groupId", groupId);
			act.setForword(requireForecastHistoryInitUrl);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 参考信息查询
	 */
	public void forecastReportThreeMonthHistory() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao = OemRequireForecastReportDao.getInstance();
		try {
			RequestWrapper request = act.getRequest();
			String groupId = request.getParamValue("groupId") == null ? "" : request.getParamValue("groupId");

			List<Map<String, Object>> list = new ArrayList();
			for (int i = -3; i < 0; i++) {
				list.add(PlanUtil.getMapDate(1));
			}

			PageResult<Map<String, Object>> ps = null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

			ps = dao.selectHistoryMonth(logonUser.getDutyType(), logonUser.getOrgId().toString(), groupId, list, Constant.PAGE_SIZE, curPage);

			act.setOutData("ps", ps);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void OemRebut() { // YH 2011.6.25
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			PlanParaBean bean = PlanUtil.selectForecastParas();
			if (null == bean) {
				throw new BizException(act, ErrorCodeConstant.INVALIDATION_DATA, "系统参数错误");
			}

			List<Map<String, Object>> mapList = getForecastDate(bean);

			// 目前单月预测，校验如果存在已经上报数据，则不返回数据
			List<Map<String, Object>> cList = getForecastDateAmt(bean);
//			Map<String, Object> map = cList.get(0);//zxf
//			String year = (String) map.get("YEAR");
//			String month = (String) map.get("MONTH");

			String orgCodes = CommonUtils.checkNull(request.getParamValue("orgCode"));
			//String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));//zxf
			String orgIds = "";
			if (null != orgCodes && !"".equals(orgCodes)) {
				orgIds = getOrgIds(orgCodes);
				orgIds = CommonUtils.strToSql(orgIds, ",");
			}
			int flag = dao.OemRebutOrg(orgIds, logonUser.getCompanyId().toString(),cList);
			act.setOutData("flag", flag);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	// 根据大区code 得到大区的id
	private String getOrgIds(String orgCodes) {// YH 2011.6.25
		String[] str_orgcodes = orgCodes.split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str_orgcodes.length; i++) {
			TmOrgPO org = new TmOrgPO();
			org.setOrgCode(str_orgcodes[i]);
			org = (TmOrgPO) dao.select(org).get(0);
			sb.append(org.getOrgId().toString() + ",");
		}
		String str_f = sb.toString();
		return str_f.substring(0, str_f.length() - 1);
	}

	// 根据经销商code 得到经销商的id
	private String getDealerIds(String dealerCodes) {// YH 2011.6.25
		String[] str_dealercodes = dealerCodes.split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str_dealercodes.length; i++) {
			TmDealerPO dealer = new TmDealerPO();
			dealer.setDealerCode(str_dealercodes[i]);
			dealer = (TmDealerPO) dao.select(dealer).get(0);
			sb.append(dealer.getDealerId().toString() + ",");
		}
		String str_f = sb.toString();
		return str_f.substring(0, str_f.length() - 1);
	}

	public void OrgRebut() {// YH 2011.6.25
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			PlanParaBean bean = PlanUtil.selectForecastParas();
			if (null == bean) {
				throw new BizException(act, ErrorCodeConstant.INVALIDATION_DATA, "系统参数错误");
			}

			List<Map<String, Object>> mapList = getForecastDate(bean);

			// 目前单月预测，校验如果存在已经上报数据，则不返回数据
			List<Map<String, Object>> cList = getForecastDateAmt(bean);
//			Map<String, Object> map = cList.get(0); //zxf
//			String year = (String) map.get("YEAR");
//			String month = (String) map.get("MONTH");

			String dealerCodes = CommonUtils.checkNull(request.getParamValue("dealerCode"));
		//	String areaId = CommonUtils.checkNull(request.getParamValue("areaId")); //zxf
			String dealerIds = "";
			if (null != dealerCodes && !"".equals(dealerCodes)) {
				dealerIds = getDealerIds(dealerCodes);
				dealerIds = CommonUtils.strToSql(dealerIds, ",");
			}
			int flag = dao.OrgRebutDealer(dealerIds, logonUser.getCompanyId().toString(),cList);
			act.setOutData("flag", flag);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 判断用户是否已经调整了所有的数据
	 */
	public void checkDataIsModify(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			//获取出入进来的数据
			String[] groupCodes= request.getParamValues("dataList");
			String[] underNumber= request.getParamValues("dataList1");
			Enumeration<String> name = request.getParamNames();
			//获取组织id
			String orgId=logonUser.getOrgId().toString();
			//获取当前的年和月
			Calendar c=Calendar.getInstance();
			
			PlanParaBean bean = PlanUtil.selectForecastParas();
			if (null == bean) {
				throw new BizException(act, ErrorCodeConstant.INVALIDATION_DATA, "系统参数错误");
			}

			List<Map<String, Object>> mapList = getForecastDate(bean);

			// 目前单月预测，校验如果存在已经上报数据，则不返回数据
			List<Map<String, Object>> cList = getForecastDateAmt(bean);
			Map<String, Object> map = cList.get(0);
			String year = (String) map.get("YEAR");
			String month = (String) map.get("MONTH");
			
			List<Map<String, Object>> returnList=dao.checkDataUnAdjust(Integer.parseInt(year), Integer.parseInt(month), orgId, groupCodes,underNumber);
			act.setOutData("data", returnList);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
}