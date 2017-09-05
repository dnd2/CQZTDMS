package com.infodms.dms.actions.dealerInfo.dealerInfoChange;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtVehicleChangeBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.application.BalanceQueryDAO;
import com.infodms.dms.dao.claim.application.DealerBalanceDao;
import com.infodms.dms.dao.claim.oldPart.ClaimBackListDao;
import com.infodms.dms.dao.feedbackMng.StandardVipApplyManagerDao;
import com.infodms.dms.dao.vehicleInfoManage.VehicleInfoManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TtAsChangedatePO;
import com.infodms.dms.po.TtAsDealerTypePO;
import com.infodms.dms.po.TtAsWrBalanceExtPO;
import com.infodms.dms.po.TtAsWrGamePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import com.lowagie.tools.concat_pdf;

/**
 * Iverson add by 2011-01-07
 * @ClassName: changeOldDate 
 * @Description: TODO(无费用(旧件)变更申请) 
 *
 */
public class changeOldDate implements Constant {
	public Logger logger = Logger.getLogger(changeOldDate.class);
	private VehicleInfoManageDao dao = VehicleInfoManageDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request = act.getRequest();
	
	private final String INIT_URL = "/jsp/dealerInfo/dealerInfoChange/changeOldReturn.jsp";
	private final String CHANGE_URL = "/jsp/dealerInfo/dealerInfoChange/changeDatePage.jsp";
	private final String CHANGE_INFO = "/jsp/dealerInfo/dealerInfoChange/changeInfo.jsp";
	
	/**
	 * 
	 * @Title: vehicleInfoChangeApplyInit 
	 * @Description: TODO(初始页面) 
	 */
	public void oldReturnDateChangeInit() {
		try {
			act.setForword(INIT_URL);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"旧件开始时间变更初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询页(分页)
	 */
	public void queryOldChangeInfo() {
		try {
			String changeTime  = request.getParamValue("changeTime");
			String changeTime1  = request.getParamValue("changeTime1");
			String yieldly  = request.getParamValue("yieldly");
			String change_date  = request.getParamValue("change_date");
			String change_review_date  = request.getParamValue("change_review_date");
			String type  = request.getParamValue("type");
			Integer curPage = request.getParamValue("curPage") != null ?
					Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.queryOldChangeInfo(logonUser.getDealerId(),changeTime,changeTime1,yieldly,change_date,change_review_date,type,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增初始页
	 */
	public void changeDateInit() {
		try {
			String type = request.getParamValue("type");
			act.setOutData("type",type);
			
			/*************Iverson add By 2010-12-02 显示12月份的结算日，必须选择结算日才能结算在基础表(tc_code)里面维护***********/
			TcCodePO po = new TcCodePO();
			po.setCodeId(Constant.DAY_12);
			ClaimBackListDao claimBackdao=ClaimBackListDao.getInstance();
			TcCodePO poValue = (TcCodePO)claimBackdao.select(po).get(0);
			String day = poValue.getCodeDesc();
			act.setOutData("day", Long.parseLong(day));
			
			TcCodePO po1 = new TcCodePO();
			po1.setCodeId(Constant.DAY_12_31);
			TcCodePO poValue1 = (TcCodePO)claimBackdao.select(po1).get(0);
			String day1 = poValue1.getCodeDesc();
			act.setOutData("day1", Long.parseLong(day1));
			/*************Iverson add By 2010-12-02 显示12月份的结算日，必须选择结算日才能结算在基础表(tc_code)里面维护***********/
			
			act.setForword(CHANGE_URL);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void showDate(){
		String yieldly = request.getParamValue("yieldly");
		String type = request.getParamValue("type");
		ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
		TtAsDealerTypePO po = new TtAsDealerTypePO();
		po.setDealerId(Long.parseLong(logonUser.getDealerId()));
		po.setYieldly(yieldly);
		TtAsDealerTypePO poValue = (TtAsDealerTypePO)claimBackDao.select(po).get(0);
		
		//Date balanceDate = poValue.getBalanceDate();//取出时间
		//Calendar calendar = Calendar.getInstance();//公用类，加年月日的
		//calendar.setTime(balanceDate);
		//calendar.add(Calendar.DAY_OF_MONTH, 1);//当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
		//balanceDate = calendar.getTime();//得到加一天后的值
		
		if(Integer.parseInt(type)==Constant.CHANGE_TYPE_OLD){
			Date oldDate = poValue.getOldDate();
			Date oldReviewDate = poValue.getOldReviewDate();
			act.setOutData("oldDate", oldDate);
			act.setOutData("oldReviewDate", oldReviewDate);
			
			Calendar calendar = Calendar.getInstance();//公用类，加年月日的
			calendar.setTime(oldDate);
			calendar.add(Calendar.DAY_OF_MONTH, 1);//当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
			Date oldDate_1 = calendar.getTime();//得到加一天后的值
			act.setOutData("oldDate_1", oldDate_1);
		}else{
			Date balanceDate = poValue.getBalanceDate();
			Date balanceReviewDate = poValue.getBalanceReviewDate();
			act.setOutData("balanceDate", balanceDate);
			act.setOutData("balanceReviewDate", balanceReviewDate);
			
			Calendar calendar = Calendar.getInstance();//公用类，加年月日的
			calendar.setTime(balanceDate);
			calendar.add(Calendar.DAY_OF_MONTH, 1);//当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
			Date balanceDate_1 = calendar.getTime();//得到加一天后的值
			act.setOutData("balanceDate_1", balanceDate_1);
		}
	}
	/**
	 * 变更提交
	 * @Title: doOemSave
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void updateOldReturn() {
		try {
			String dealerId = logonUser.getDealerId();
			String yieldly = request.getParamValue("yieldly");
			String oldDate = request.getParamValue("oldDate");
			String oldReview = request.getParamValue("oldReview");
			String oldDate_1 = request.getParamValue("oldDate_1");
			String old_Date = request.getParamValue("old_Date");
			
			String balanceDate = request.getParamValue("balanceDate");
			String balanceReview = request.getParamValue("balanceReview");
			String balanceDate_1 = request.getParamValue("balanceDate_1");
			String balance_Date = request.getParamValue("balance_Date");
			
			String count = request.getParamValue("count");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			// 判断当前变更日期内，是否有索赔费用（包括索赔单总费用+特殊费用）
			Map<String,Object> map = new HashMap<String,Object>() ;
			if(count.equals("2")){
				map = dao.checkFee(balanceDate_1,balance_Date,logonUser.getDealerId(),yieldly);
			}else if(count.equals("1")){
				//zhumingwei 2011-03-11  此方法用于区分轿车和微车
			    TcCodePO codePo= new TcCodePO();
			    codePo.setType(Constant.chana+"");
			    TcCodePO poValue = (TcCodePO)dao.select(codePo).get(0);
				String codeId = poValue.getCodeId();
				//zhumingwei 2011-03-11 此方法用于区分轿车和微车 end
				if("80081001".equals(codeId)){
					map = dao.checkOld(logonUser.getDealerId(),yieldly,oldDate_1, old_Date) ;
					if(Double.parseDouble(map.get("MONEY").toString())>0){
						act.setOutData("ok","error");
					}
				}else{
					map = dao.checkOld2(logonUser.getDealerId(),yieldly,oldDate_1, old_Date) ;
					if(Double.parseDouble(map.get("MONEY").toString())>0){
						act.setOutData("ok","errorOld");
					}
				}
			}
			//if(Double.parseDouble(map.get("MONEY").toString())>0){
				//act.setOutData("ok","error");
			//}else{
			if(Double.parseDouble(map.get("MONEY").toString())<=0){
				TtAsChangedatePO poDate = new TtAsChangedatePO();
				poDate.setId(Long.parseLong(SequenceManager.getSequence("")));
				poDate.setDealerId(Long.parseLong(dealerId));
				poDate.setYieldly(yieldly);
				if(count.equals("1")){
					poDate.setHostDate(sdf.parse(oldDate));
					poDate.setHostReview(sdf.parse(oldReview));
					poDate.setChangeDate(sdf.parse(oldDate_1));
					poDate.setChangeReviewDate(sdf.parse(old_Date));
					poDate.setStatus(Constant.CHANGE_TYPE_OLD);
				}else if(count.equals("2")){
					poDate.setHostDate(sdf.parse(balanceDate));
					poDate.setHostReview(sdf.parse(balanceReview));
					poDate.setChangeDate(sdf.parse(balanceDate_1));
					poDate.setChangeReviewDate(sdf.parse(balance_Date));
					poDate.setStatus(Constant.CHANGE_TYPE_BALABCE);
				}
				poDate.setChangeTime(new Date());
				poDate.setChangeBy(logonUser.getUserId());
				poDate.setCreateBy(logonUser.getUserId());
				poDate.setCreateDate(new Date());
				poDate.setUpdateDate(new Date());
				poDate.setUpdateBy(logonUser.getUserId());
				dao.insert(poDate);
				
				TtAsDealerTypePO po = new TtAsDealerTypePO();
				po.setDealerId(Long.parseLong(dealerId));
				po.setYieldly(yieldly);
				TtAsDealerTypePO poValue = new TtAsDealerTypePO();
				if(count.equals("1")){
					poValue.setOldDate(sdf.parse(old_Date));
					poValue.setOldReviewDate(sdf.parse(old_Date));
				}else if(count.equals("2")){
					poValue.setBalanceDate(sdf.parse(balance_Date));
					//同时更新结算审核时间
					poValue.setBalanceReviewDate(sdf.parse(balance_Date));//2011-05-25  add
				}
				poValue.setUpdateBy(sdf.format(new Date()));
				poValue.setUpdateBy(logonUser.getUserId().toString());
				dao.update(po, poValue);
				act.setOutData("ok", "ok");
			}
			act.setOutData("type", count);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//判断时间
	public void isCheck(){
		ClaimBackListDao dao=ClaimBackListDao.getInstance();
		String count = request.getParamValue("count");
		String oldDate_1 = request.getParamValue("oldDate_1");
		String old_Date = request.getParamValue("old_Date");
		//String balance_Date = request.getParamValue("balance_Date");
		
		String oldDate = request.getParamValue("oldDate");
		String oldReview = request.getParamValue("oldReview");
		
		if(count.equals("1")){//是旧件
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date time = new Date();
			try {
				Date a = sdf.parse(sdf.format(time));
				Date b = sdf.parse(old_Date);
				Calendar cal1 = Calendar.getInstance(); 
				Calendar cal2 = Calendar.getInstance(); 
				cal1.setTime(a); 
				cal2.setTime(b);
				if(cal1.before(cal2)){
					act.setOutData("ok", "ok");
				}
				TmBusinessParaPO poCon = new TmBusinessParaPO();
				poCon.setTypeCode(Constant.TYPE_CODE);
				TmBusinessParaPO poConValue = (TmBusinessParaPO)dao.select(poCon).get(0);
				String timeCon=poConValue.getParaValue();
				timeCon=timeCon.substring(11, timeCon.length());
				Date timeCon1=sdf.parse(timeCon);
				Calendar calendar = Calendar.getInstance();//公用类，加年月日的
				calendar.setTime(timeCon1);
				calendar.add(Calendar.DAY_OF_MONTH, 1);//当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
				timeCon1 = calendar.getTime();//得到加一天后的值
				Date startDate1=sdf.parse(oldDate_1);
				if(startDate1.before(timeCon1)){
					act.setOutData("flag", "false");
				}
				
				if(!oldDate.equals(oldReview)){
					act.setOutData("rr", "rr");
				}
				
				String nowTime1 = sdf.format(time);
				String yearTime1= nowTime1.substring(0,4);//year
				nowTime1= nowTime1.substring(5,7);
				String nowTime2 = old_Date.substring(5,7);
				String yearTime2 = old_Date.substring(0,4);
				act.setOutData("nowTime2", nowTime2);
				act.setOutData("nowTime1", nowTime1);
				act.setOutData("yearTime1", yearTime1);
				act.setOutData("yearTime2", yearTime2);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void getFalg(){
		String balanceDate_1 = request.getParamValue("balanceDate_1");
		String balance_Date = request.getParamValue("balance_Date");
		String count = request.getParamValue("count");
		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		try{
			TmBusinessParaPO poCon = new TmBusinessParaPO();
			poCon.setTypeCode(Constant.TYPE_CODE);
			TmBusinessParaPO poConValue = (TmBusinessParaPO)balanceDao.select(poCon).get(0);
			String timeCon=poConValue.getParaValue();
			timeCon=timeCon.substring(11, timeCon.length());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date timeCon2=sdf.parse(timeCon);
			Calendar calendar = Calendar.getInstance();//公用类，加年月日的
			calendar.setTime(timeCon2);
			calendar.add(Calendar.DAY_OF_MONTH, 1);//当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
			timeCon2 = calendar.getTime();//得到加一天后的值
			Date endBalanceDate1=sdf.parse(balanceDate_1);
			if(endBalanceDate1.before(timeCon2)){
				act.setOutData("flag", "false");
			}
			//判断选择时间不能在当前时间之后
			Date time = new Date();
			String time1 = sdf.format(time);
			Date time2 = sdf.parse(time1);
			if(sdf.parse(balance_Date).after(time2)){
				act.setOutData("error", "error");
			}
			act.setOutData("count", count);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//明细
	public void changeInfo(){
		String id = request.getParamValue("id");
		Map<String,Object> map = dao.queryChangeDateInfo(id);
		act.setOutData("poValue", map);
		act.setOutData("status", Constant.CHANGE_TYPE_OLD) ;
		act.setForword(CHANGE_INFO);
	}
}
