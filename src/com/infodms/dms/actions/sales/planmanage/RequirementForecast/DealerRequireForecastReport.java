package com.infodms.dms.actions.sales.planmanage.RequirementForecast;


import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
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
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.planmanage.DealerRequireForecastReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtVsMonthlyForecastDetailPO;
import com.infodms.dms.po.TtVsMonthlyForecastPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

public class DealerRequireForecastReport {

	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String dealerRequireForecastReportInitUrl = "/jsp/sales/planmanage/requireforecast/dealerrequireforecastreport.jsp";
	private final String dealerRequireForecastReportOutDateUrl = "/jsp/sales/planmanage/requireforecast/dealerrequireforecastreportoutdate.jsp";
	private final String dealerRequireForecastReportOpeUrl = "/jsp/sales/planmanage/requireforecast/dealerrequireforecastreportope.jsp";
	private final String dealerRequireForecastReportComUrl = "/jsp/sales/planmanage/requireforecast/dealerrequireforecastreportcomplete.jsp";
	private final String dealerRequireForecastHistoryInitUrl = "/jsp/sales/planmanage/requireforecast/dealerrequireforecasthistoryshow.jsp";
	
	public void dealerRequireForecastReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			 PlanParaBean bean=PlanUtil.selectForecastParas();
			 int curDay=PlanUtil.getCurrentDay();
			 if(curDay>bean.getDealerForecastEndDay()||curDay<bean.getDealerForecastStartDay()){
				 act.setForword(dealerRequireForecastReportOutDateUrl);
			 }else{
//				 List<Map<String, Object>> areaBusList = new ArrayList();
//				 
//				 String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
//				 if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
//					 areaBusList = MaterialGroupManagerDao.getDealerLevelBusiness(logonUser.getPoseId().toString());
//					} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
//						 areaBusList = MaterialGroupManagerDao.getDealerQJD(logonUser.getPoseId().toString());
//					} else {
//						throw new RuntimeException("判断当前系统的系统参数错误！") ;
//					}
				 List<Map<String, Object>> mapList=getForecastDate(bean);
				 act.setOutData("mapList", mapList);
//				 act.setOutData("areaBusList", areaBusList);
				 //Map<String,Object> m=CommonUtils.getIsConstracExpire(logonUser.getDealerCode());
				//int count= 	Integer.parseInt(m.get("COUNT").toString());
				//String contractDate=m.get("CONTRACT_DATE").toString();
				//act.setOutData("IsExpire", count);
				//act.setOutData("expireDate", contractDate);
				 act.setForword(dealerRequireForecastReportInitUrl);
			 }
			 
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void dealerRequireForecastReportModelSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		DealerRequireForecastReportDao dao=DealerRequireForecastReportDao.getInstance();
		try {
			 String dealerId=logonUser.getDealerId();
			// String areaId="";
			 RequestWrapper request=act.getRequest();
			// String adStr=request.getParamValue("areaId");
			// String[] str=adStr.split(",");
			// dealerId=str[1];
			// areaId=str[0];
			 PlanParaBean bean=PlanUtil.selectForecastParas();
			 if(null==bean){
				 throw new Exception("参数错误");
			 }
			 
			 List<Map<String, Object>> mapList=getForecastDate(bean);
			 //目前单月预测，校验如果存在已经上报数据，则不返回数据
			 List<Map<String, Object>> cList=getForecastDateAmt(bean);
			 Map<String, Object> map=cList.get(0);
			 String year=(String)map.get("YEAR");
			 String month=(String)map.get("MONTH");
			 
			 TtVsMonthlyForecastPO conPo=new TtVsMonthlyForecastPO();
			 //conPo.setAreaId(new Long(areaId));
			 conPo.setDealerId(Long.parseLong(dealerId)); //(DealerRequireForecastReportDao.getOrgId(dealerId))
			 conPo.setForecastYear(new Integer(year));
			 conPo.setForecastMonth(new Integer(month));
			 conPo.setCompanyId(new Long(logonUser.getOemCompanyId()));
			 conPo.setOrgType(Constant.ORG_TYPE_DEALER);
			 conPo.setStatus(Constant.FORECAST_STATUS_CONFIRM);
			 conPo.setForecastType(Constant.FORECAST_STATUS_FORMAL); 
			 List<PO> checkList = dao.select(conPo);
			 PageResult<Map<String, Object>> ps=null;
			 // 处理当前页
			 Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")): 1; 
			
			 if(null!=checkList&&checkList.size()>0){
				  conPo = (TtVsMonthlyForecastPO)checkList.get(0);
				  ps = dao.selectRubbish(curPage, new Integer(1000)); 				 	
				  if(null != conPo.getSubStatus() && 20381005 == conPo.getSubStatus()){	  
					  List<Map<String,Object>> infoList_f = new ArrayList();
					  Map sub_map = new HashMap();
					  sub_map.put("GROUP_ID", "");
					  sub_map.put("SERIES_NAME", "贵单位需求预测已提报，等待审核");
					  sub_map.put("S0", "");
					  sub_map.put("S1", "");
					  sub_map.put("S2", "");
					  sub_map.put("MODEL_CODE", "");
					  sub_map.put("SERIES_CODE", "");
					  sub_map.put("MODEL_NAME", "");
					  infoList_f.add(sub_map);
					  ps.setRecords(infoList_f);
				  }
  			 }else{
  				 conPo.setStatus(Constant.FORECAST_STATUS_UNCONFIRM);
  				 List<PO> checkList2 = dao.select(conPo);
  				 if(checkList2.size()>0){
  					 conPo = (TtVsMonthlyForecastPO)checkList2.get(0);
  				}
				 ps = dao.selectRequireForecastModelInfo(dealerId, mapList,logonUser.getOemCompanyId().toString(),curPage,new Integer(1000));
				 List<Map<String, Object>> infoList = null;
				 List<Map<String, Object>> infoList_f = new ArrayList(); 
				 infoList =  ps.getRecords(); //YH 2011.6.29
				   //按车系小计
				   int subtotal_S0 = 0;
				   int subtotal_S1 = 0;
				   int subtotal_S2 = 0;
				   
				   //按车系合计
				   int total_S0 = 0;
				   int total_S1 = 0;
				   int total_S2 = 0;
				   
				 if(infoList != null) {
					 for(int i=0; i< infoList.size();i++){ //计算小计合计 YH 2011.6.20
						 Map map1 = (Map)infoList.get(i);
						 Map map_f = new HashMap();
						 map_f.put("GROUP_ID", map1.get("GROUP_ID"));
						 map_f.put("SERIES_NAME", map1.get("SERIES_NAME"));
						 map_f.put("S0", map1.get("S0"));
						 map_f.put("S1", map1.get("S1"));
						 map_f.put("S2", map1.get("S2"));
						 map_f.put("MODEL_CODE", map1.get("MODEL_CODE"));
						 map_f.put("SERIES_CODE", map1.get("SERIES_CODE"));
						 map_f.put("MODEL_NAME", map1.get("MODEL_NAME"));
						 
						 
						 subtotal_S0 = subtotal_S0 + Integer.parseInt(map1.get("S0").toString());
						 subtotal_S1 = subtotal_S1 + Integer.parseInt(map1.get("S1").toString());
						// subtotal_S2 = subtotal_S2 + Integer.parseInt(map1.get("S2").toString());		//zxf  改为2个月，多余的数据不要				 					
						 
						 total_S0 = total_S0 + Integer.parseInt(map1.get("S0").toString());
						 total_S1 = total_S1 + Integer.parseInt(map1.get("S1").toString());
						// total_S2 = total_S2 + Integer.parseInt(map1.get("S2").toString()); //zxf  改为2个月，多余的数据不要	
						 
						 infoList_f.add(map_f);
						 
						 if(i+1 < infoList.size()){					  
							 Map map2 = infoList.get(i+1);
							 String SERIES_CODE_1 = map1.get("SERIES_CODE").toString();
							 String SERIES_CODE_2 = map2.get("SERIES_CODE").toString();
							 if(!SERIES_CODE_1.equals(SERIES_CODE_2)){
								 Map map_sub = new HashMap();
								 map_sub.put("GROUP_ID","");
								 map_sub.put("SERIES_NAME", "----小计----");
								 map_sub.put("S0", subtotal_S0);
								 map_sub.put("S1", subtotal_S1);
								 map_sub.put("S2", subtotal_S2);
								 map_sub.put("MODEL_CODE", "");
								 map_sub.put("SERIES_CODE", "");
								 map_sub.put("MODEL_NAME", "");
								 
								 infoList_f.add(map_sub);				   
								 //按车系小计
								 subtotal_S0 = 0;
								 subtotal_S1 = 0;
								 subtotal_S2 = 0;
							 }
						 }else {
							 Map map_sub = new HashMap();
							 map_sub.put("GROUP_ID", "");
							 map_sub.put("SERIES_NAME", "----小计----");
							 map_sub.put("S0", subtotal_S0);
							 map_sub.put("S1", subtotal_S1);
							 map_sub.put("S2", subtotal_S2);
							 map_sub.put("MODEL_CODE", "");
							 map_sub.put("SERIES_CODE", "");
							 map_sub.put("MODEL_NAME", "");
							 
							 infoList_f.add(map_sub);
							 
							 Map map_tota = new HashMap();
							 map_tota.put("GROUP_ID", "");
							 map_tota.put("SERIES_NAME", "----合计----");
							 map_tota.put("S0", total_S0);
							 map_tota.put("S1", total_S1);
							 map_tota.put("S2", total_S2);
							 map_tota.put("MODEL_CODE", "");
							 map_tota.put("SERIES_CODE", "");
							 map_tota.put("MODEL_NAME", "");
							 infoList_f.add(map_tota);				
						 }
					 }
				 }
				 if( null != conPo.getSubStatus() && 20381003 == conPo.getSubStatus()){
					    Map sub_map = new HashMap();
					    sub_map.put("GROUP_ID", "");
					    sub_map.put("SERIES_NAME", "大区已驳回，需重新上报");
					    sub_map.put("S0", "");
					    sub_map.put("S1", "");
					    sub_map.put("S2", "");
					    sub_map.put("MODEL_CODE", "");
					    sub_map.put("SERIES_CODE", "");
					    sub_map.put("MODEL_NAME", "");
					    infoList_f.add(sub_map);
				 }
				 ps.setRecords(infoList_f);
			     act.setOutData("areaId", "");
			     act.setOutData("mapList", mapList);
			 }			
			 act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 取得查询日期，展示月数，提前预测月数，0表示从当前月开始预测，每次预测月数（开始预测月+每次预测月数）
	 */
	private List<Map<String, Object>> getForecastDate(PlanParaBean bean){
		List<Map<String, Object>> list=new ArrayList();
		int his=bean.getShowHistoryMonthAmt();//展示月数，
		int start=-his+bean.getForecastPreMonth();
		int end=bean.getForecastPreMonth()+bean.getForecastMonthAmt();//每次预测月数（开始预测月+每次预测月数）
		for(int i=start;i<end;i++){
			list.add(PlanUtil.getMapDate(i));
		}
		return list;
	}
	/*
	 * 经销商预测查询
	 * 需要查询参数，确定预测时间浮动
	 */
	public void dealerForecastSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		DealerRequireForecastReportDao dao=DealerRequireForecastReportDao.getInstance();
		try {
			
			 String dealerId="";
			// String areaId="";
			 String modelId="";
			 RequestWrapper request=act.getRequest();
			 //参数车型ID
			 String parastr=request.getParamValue("parastr");
			 String[] str=parastr.split(",");
			 modelId=str[0];
			 dealerId=str[1];
			// areaId=str[2];
			 
			 PlanParaBean bean=PlanUtil.selectForecastParas();
			 List<Map<String, Object>> dateList = getForecastDateAmt(bean);
			
			 TmVhclMaterialGroupPO gconPo=new TmVhclMaterialGroupPO();
			 gconPo.setGroupId(new Long(modelId));
			 gconPo.setCompanyId(new Long(logonUser.getOemCompanyId()));
			 List<PO> glist=dao.select(gconPo);
			 if(null==glist||glist.size()==0){
				 throw new Exception("车型不存在");
			 }
			 TmVhclMaterialGroupPO gpo=(TmVhclMaterialGroupPO)glist.get(0);
			 String modelName=gpo.getGroupName();
			 List<Map<String, Object>> ps=null;
			 ps=dao.selectForecastOpeList(modelId,dealerId,  dateList,logonUser.getOemCompanyId());
			 act.setOutData("ps", ps);
			 act.setOutData("dateList", dateList); //预测月份
			 act.setOutData("len", dateList.size()-1);
			 act.setOutData("modelName", modelName);//预测车型
			 act.setOutData("parastr", parastr);
			 act.setForword(dealerRequireForecastReportOpeUrl);
			 
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 取得查询日期，展示月数，提前预测月数，0表示从当前月开始预测，每次预测月数（开始预测月+每次预测月数）
	 */
	private List<Map<String, Object>> getForecastDateAmt(PlanParaBean bean){
		List<Map<String, Object>> list=new ArrayList();
		Map<String, Object> map=new HashMap<String, Object>();
		for(int i=bean.getForecastPreMonth();i<bean.getForecastMonthAmt()+bean.getForecastPreMonth();i++){
			list.add(PlanUtil.getMapDate(i));
		}
		return list;
	}
	/*
	 * 保存需求预测
	 */
	public void dealerRequireForecastSave(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		DealerRequireForecastReportDao dao=DealerRequireForecastReportDao.getInstance();
		try {
			 RequestWrapper request=act.getRequest();
			 String parastr=request.getParamValue("parastr");
			 String[] arr=parastr.split(",");
			 String dealerId=arr[1];
			// String areaId=arr[2];
			 
			 Enumeration<String> params = request.getParamNames();
			 boolean bl=false;
			 while (params.hasMoreElements()) {
				String elem = (String) params.nextElement();
				if(elem.indexOf("ipt")!=-1){
					String amt = request.getParamValue(elem);
					if(null==amt){
						amt="0";
					}
					elem=elem.substring(3,elem.length());
					String[] gym=elem.split("_");
					String groupId=gym[0];
					String year=gym[1];
					String month=gym[2];
					List<Map<String, Object>> clrList_;
					List<Map<String, Object>> clrList;
					boolean formMonth = false;
					if(PlanUtil.getRadomDate(1,"").equals(month)){ //zxf 正式预测					
						formMonth = true;
					}
					clrList_ = dao.selectClrDealerForecast(year, month, dealerId, "",bl,formMonth);
					clrList = dao.selectClrDealerForecast(year, month, dealerId, groupId,bl,formMonth);

					if(null!=clrList_&&clrList_.size()>0) {
						Long forecastId_ = new Long(clrList_.get(0).get("FORECAST_ID").toString());
						if (null!=clrList && clrList.size()>0) {
							Long detailId_ = new Long(clrList.get(0).get("DETAIL_ID").toString()) ;
							TtVsMonthlyForecastDetailPO detailPo_=new TtVsMonthlyForecastDetailPO();
							detailPo_.setDetailId(detailId_);
							TtVsMonthlyForecastDetailPO detailPo=new TtVsMonthlyForecastDetailPO();
							detailPo.setDetailId(detailId_) ;
							detailPo.setForecastAmount(new Integer(amt));
							
							dao.update(detailPo_, detailPo);
						} else {
							TtVsMonthlyForecastDetailPO detailPo=new TtVsMonthlyForecastDetailPO();
							detailPo.setDetailId(new Long(SequenceManager.getSequence("")));
							detailPo.setForecastId(forecastId_);
							detailPo.setGroupId(new Long(groupId));
							detailPo.setForecastAmount(new Integer(amt));
							detailPo.setCreateBy(logonUser.getUserId());
							detailPo.setCreateDate(new Date());
							
							dao.insert(detailPo);
						}						
					} else {
						//保存数据
						TtVsMonthlyForecastPO mainPo=new TtVsMonthlyForecastPO();
						Long mseq=new Long(SequenceManager.getSequence(""));
						mainPo.setForecastId(mseq);
						mainPo.setCompanyId(new Long(logonUser.getOemCompanyId()));
						mainPo.setDealerId(new Long(dealerId));
						//mainPo.setAreaId(new Long(areaId));
						mainPo.setForecastYear(new Integer(year));
						mainPo.setForecastMonth(new Integer(month));
						mainPo.setOrgType(Constant.ORG_TYPE_DEALER);
						mainPo.setStatus(Constant.FORECAST_STATUS_UNCONFIRM);
						mainPo.setCreateBy(logonUser.getUserId());
						mainPo.setCreateDate(new Date());
						if(PlanUtil.getRadomDate(2,"").equals(month)){//如果是当前月的第二个月则为展望预计 //zxf
							mainPo.setForecastType(60591002);
						}
						else{
							mainPo.setForecastType(60591001);
						}
						dao.insert(mainPo);	
						
						TtVsMonthlyForecastDetailPO detailPo=new TtVsMonthlyForecastDetailPO();
						detailPo.setDetailId(new Long(SequenceManager.getSequence("")));
						detailPo.setForecastId(mseq);
						detailPo.setGroupId(new Long(groupId));
						detailPo.setForecastAmount(new Integer(amt));
						detailPo.setCreateBy(logonUser.getUserId());
						detailPo.setCreateDate(new Date());
						
						dao.insert(detailPo);
					}
					
					/*//清空 当前登陆经销商年、月、业务范围内、未提报车辆配置的需求预测,
					List<Map<String, Object>> clrList = dao.selectClrDealerForecast(year, month, areaId, dealerId, groupId,bl);
					bl=false;
					if(null!=clrList&&clrList.size()>0){
						for(int i=0;i<clrList.size();i++){
							Map<String, Object> map=clrList.get(i);
							Long forecastId = new Long(map.get("FORECAST_ID").toString());
							TtVsMonthlyForecastDetailPO clrDPo=new TtVsMonthlyForecastDetailPO();
							clrDPo.setForecastId(forecastId);
							TtVsMonthlyForecastPO clrPo=new TtVsMonthlyForecastPO();
							clrPo.setForecastId(forecastId);
							dao.delete(clrDPo);
							dao.delete(clrPo);
						}
					}
					//保存数据
					
					TtVsMonthlyForecastPO mainPo=new TtVsMonthlyForecastPO();
					Long mseq=new Long(SequenceManager.getSequence(""));
					mainPo.setForecastId(mseq);
					mainPo.setCompanyId(new Long(logonUser.getOemCompanyId()));
					mainPo.setDealerId(new Long(dealerId));
					mainPo.setAreaId(new Long(areaId));
					mainPo.setForecastYear(new Integer(year));
					mainPo.setForecastMonth(new Integer(month));
					mainPo.setOrgType(Constant.ORG_TYPE_DEALER);
					mainPo.setStatus(Constant.FORECAST_STATUS_UNCONFIRM);
					mainPo.setCreateBy(logonUser.getUserId());
					mainPo.setCreateDate(new Date());
					
					dao.insert(mainPo);
					
					TtVsMonthlyForecastDetailPO detailPo=new TtVsMonthlyForecastDetailPO();
					detailPo.setDetailId(new Long(SequenceManager.getSequence("")));
					detailPo.setForecastId(mseq);
					detailPo.setGroupId(new Long(groupId));
					detailPo.setForecastAmount(new Integer(amt));
					detailPo.setCreateBy(logonUser.getUserId());
					detailPo.setCreateDate(new Date());
					
					dao.insert(detailPo);*/
				}else{
					continue;
				}
				
			}
			act.setOutData("parastr", parastr);
			act.setForword("/sales/planmanage/RequirementForecast/DealerRequireForecastReport/dealerRequireForecastReportInit.do");
			 
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 经销商需求预测上报 YH 2011.6.26
	 */
	public void dealerForecastReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		DealerRequireForecastReportDao dao=DealerRequireForecastReportDao.getInstance();
		try {
			 PlanParaBean bean=PlanUtil.selectForecastParas();
			 List<Map<String, Object>> mapList=getForecastDate(bean);
			 RequestWrapper request=act.getRequest();
			
			 String dealerId=logonUser.getDealerId();
			// String areaId=str[0];
			 
			 for(int i=0;i<mapList.size();i++){
				 Map<String, Object> map=mapList.get(i);
				 String year=(String)map.get("YEAR");
				 String month=(String)map.get("MONTH");
				 
				 TtVsMonthlyForecastPO conPo = new TtVsMonthlyForecastPO();
				 //conPo.setAreaId(new Long(areaId));
				 conPo.setDealerId(new Long(dealerId));
				 conPo.setCompanyId(new Long(logonUser.getOemCompanyId()));
				 conPo.setForecastYear(new Integer(year));
				 conPo.setForecastMonth(new Integer(month));
				 conPo.setStatus(Constant.FORECAST_STATUS_UNCONFIRM);
				 conPo.setOrgType(Constant.ORG_TYPE_DEALER);
				 
				 TtVsMonthlyForecastPO valPo = new TtVsMonthlyForecastPO();
				 valPo.setStatus(Constant.FORECAST_STATUS_CONFIRM);
				 valPo.setUpdateBy(logonUser.getUserId());
				 valPo.setUpdateDate(new Date());
				 valPo.setSubStatus(Constant.FORECAST_SUBSTATUS_5);//YH 2011.6.26 等待大区审核

				 int s = dao.update(conPo,valPo);

			 }
			 act.setForword(dealerRequireForecastReportComUrl);
			 
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 打开参考信息查看页
	 */
	public void dealerRequireForecastHistoryInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request=act.getRequest();
			String groupId=request.getParamValue("groupId")==null?"":request.getParamValue("groupId");
			act.setOutData("groupId", groupId);
			act.setForword(dealerRequireForecastHistoryInitUrl);
			 
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/*
	 * 参考信息查询
	 */
	public void dealerForecastReportThreeMonthHistory(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		DealerRequireForecastReportDao dao=DealerRequireForecastReportDao.getInstance();
		try {
			 RequestWrapper request=act.getRequest();
			 String groupId=request.getParamValue("groupId")==null?"":request.getParamValue("groupId");
			 
			 List<Map<String, Object>> list = new ArrayList();
			 for(int i=-3;i<0;i++){
				 list.add(PlanUtil.getMapDate(1));
			 }
			 
			 PageResult<Map<String, Object>> ps=null;
			 // 处理当前页
			 Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")): 1; 
			 
			 ps=dao.selectHistoryMonth(logonUser.getDealerId().toString(), groupId, list,Constant.PAGE_SIZE, curPage);
			 
			 act.setOutData("ps",ps);
			 
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public static void main(String[] args){
	}
}
