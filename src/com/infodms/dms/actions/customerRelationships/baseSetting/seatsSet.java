package com.infodms.dms.actions.customerRelationships.baseSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.tempuri.UltraCRMWebservice;
import org.tempuri.UltraCRMWebserviceSoap;

import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.SeatsSetDao;
import com.infodms.dms.dao.customerRelationships.SeatsTeamSetDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmSeatsPO;
import com.infodms.dms.po.TtCrmSeatsTeamPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : seatsSet 
 * @Description   : 坐席设置
 * @author        : wangming
 * CreateDate     : 2013-4-3
 */
public class seatsSet {
	private static Logger logger = Logger.getLogger(seatsSet.class);
	// 坐席组初始化页面
	private final String seatsSetUrl = "/jsp/customerRelationships/baseSetting/seatsSet.jsp";
	//坐席组新增页面
	private final String seatsSetUpdateUrl = "/jsp/customerRelationships/baseSetting/seatsSetUpdate.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * 坐席组初始化
	 */
	public void seatsSetInit(){		
		try{
			// 艾春9.21 修改
			SeatsSetDao dao = SeatsSetDao.getInstance();
			List<Map<String,Object>> stTeams = dao.queryTtCrmSeatsTeamAll();
			act.setOutData("stTeams", stTeams);
			
			act.setForword(seatsSetUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"坐席组初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void querySeatsSet(){
		act.getResponse().setContentType("application/json");
		try{
			
			String name = CommonUtils.checkNull(request.getParamValue("name"));  				
			String account = CommonUtils.checkNull(request.getParamValue("account"));  				
			String isSeats = CommonUtils.checkNull(request.getParamValue("isSeats"));  				
			String level = CommonUtils.checkNull(request.getParamValue("level"));  				
			String isAdmin = CommonUtils.checkNull(request.getParamValue("isAdmin"));  				
			
			// 艾春9.21添加
			String stId = CommonUtils.checkNull(request.getParamValue("stId")); 
			
			SeatsSetDao dao = SeatsSetDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> seatsSetData = dao.querySeatsSet(stId, name,account,isSeats,level,isAdmin,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", seatsSetData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"坐席组查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 坐席修改
	 * @Description: 坐席修改 
	 * LastDate    : 2013-4-3
	 */
	public void updateSeatsSet(){
		ActionContext act = ActionContext.getContext();
		CommonUtilActions commonUtilActions = new CommonUtilActions();
		try {
			act.setOutData("isSeatsList",commonUtilActions.getTcCode(Constant.IF_TYPE));
			act.setOutData("seatsExtList",commonUtilActions.getTcCode(Constant.SEAT_EXT.toString()));
			act.setOutData("seatLevelList",commonUtilActions.getTcCode(Constant.SEATS_LEVEL.toString()));
			act.setOutData("isAdminList",commonUtilActions.getTcCode(Constant.se_is_manamger.toString()));
			String id = request.getParamValue("id");
			if(id!=null&&!"".equals(id)){
				SeatsSetDao dao = SeatsSetDao.getInstance();
				TtCrmSeatsPO ttCrmSeatsPO = dao.queryTtCrmSeatsTeamPOByUserId(Long.valueOf(id));
				if(ttCrmSeatsPO.getSeIsSeats()==null) ttCrmSeatsPO.setSeIsSeats(Constant.IF_TYPE_YES);
				if(ttCrmSeatsPO.getSeIsManamger()==null) ttCrmSeatsPO.setSeIsManamger(Constant.se_is_manamger_2);
				SeatsTeamSetDao stDao =  SeatsTeamSetDao.getInstance();
				List<TtCrmSeatsTeamPO> list = stDao.querySeatsTeamSet();
				//坐席组查询
				
				act.setOutData("ttCrmSeatsPO", ttCrmSeatsPO);
				act.setOutData("seatTeamList", list);
			}
			
			act.setForword(seatsSetUpdateUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,"",ErrorCodeConstant.UPDATE_FAILURE_CODE,"坐席修改");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 新增修改坐席提交
	 * @Description: TODO 新增修改坐席提交
	 * LastDate    : 2013-4-3
	 */
	public void updateSeatsSetSubmit(){
		
		long seUserId = Long.parseLong(CommonUtils.checkNull(request.getParamValue("seUserId")));  				//用户ID
		String seName = CommonUtils.checkNull(request.getParamValue("seName"));  		        		//用户名
		String seAccount = CommonUtils.checkNull(request.getParamValue("seAccount"));  				//帐号
		String isSeats = CommonUtils.checkNull(request.getParamValue("isSeats"));  		        		//是否坐席
		String isSeatsH = CommonUtils.checkNull(request.getParamValue("isSeatsH"));  		 
		String seSeatsNo = CommonUtils.checkNull(request.getParamValue("seSeatsNo"));  				//座席号
		String seExt = CommonUtils.checkNull(request.getParamValue("seExt"));  		        		//分机号
		String seIp = CommonUtils.checkNull(request.getParamValue("seIp"));  				//IP地址
		String seatTeam = CommonUtils.checkNull(request.getParamValue("seatTeam"));  		        		//座席组
		String level = CommonUtils.checkNull(request.getParamValue("level"));  		        		//坐席级别
		String isAdmin = CommonUtils.checkNull(request.getParamValue("isAdmin"));           //是否管理员
		
		try{
			/*UltraCRMWebservice ultraCRMWebservice = new UltraCRMWebservice();
			UltraCRMWebserviceSoap soap = ultraCRMWebservice.getUltraCRMWebserviceSoap();*/
			SeatsSetDao dao = SeatsSetDao.getInstance();
			List<TtCrmSeatsPO> list = dao.queryTtCrmSeatsPOByUserId(seUserId);
			// 艾春 9.18 添加坐席状态判断
			int seStatus = 0;
			TtCrmSeatsPO ttCrmSeatsPO1 = new TtCrmSeatsPO();
			TtCrmSeatsPO ttCrmSeatsPO2 = new TtCrmSeatsPO();
			if(list!=null&&list.size()>0){
				ttCrmSeatsPO1.setSeId(list.get(0).getSeId());
				ttCrmSeatsPO2.setSeId(ttCrmSeatsPO1.getSeId());
				ttCrmSeatsPO2.setUpdateBy(logonUser.getUserId());
				ttCrmSeatsPO2.setUpdateDate(new Date());
				seStatus = list.get(0).getSeStatus();
			}else{
				ttCrmSeatsPO2.setSeId(new Long(SequenceManager.getSequence("")));
				ttCrmSeatsPO2.setCreateBy(logonUser.getUserId());
				ttCrmSeatsPO2.setCreateDate(new Date());
				ttCrmSeatsPO2.setSeUserId(seUserId);
			}
			if(!"".equals(seName)) ttCrmSeatsPO2.setSeName(seName);
			if(!"".equals(seAccount)) ttCrmSeatsPO2.setSeAccount(seAccount);
			if("".equals(isSeatsH)){
				ttCrmSeatsPO2.setSeIsSeats(Integer.parseInt(isSeats));
			}else{
				ttCrmSeatsPO2.setSeIsSeats(Integer.parseInt(isSeatsH));
			}
			if(!"".equals(seSeatsNo)) ttCrmSeatsPO2.setSeSeatsNo(seSeatsNo);
			if(!"".equals(seExt)) ttCrmSeatsPO2.setSeExt(Long.parseLong(seExt));
			if(!"".equals(seIp)) ttCrmSeatsPO2.setSeIp(seIp);
			if(!"".equals(seatTeam)) {
				ttCrmSeatsPO2.setStId(Long.parseLong(seatTeam));
			}else{
				ttCrmSeatsPO2.setStId(null);
			}
			
			if(!"".equals(level)){
				ttCrmSeatsPO2.setSeLevel(Integer.parseInt(level));
			}else {
				ttCrmSeatsPO2.setSeLevel(null);
			}
			if(!"".equals(isAdmin)) ttCrmSeatsPO2.setSeIsManamger(Integer.parseInt(isAdmin));

			if(list!=null&&list.size()>0){
				//处理设为空时 所以用纯SQL
				StringBuffer sql = new StringBuffer();
				sql.append(" update Tt_Crm_Seats set ");
				sql.append(" SE_IS_SEATS = "+ttCrmSeatsPO2.getSeIsSeats()+"," );
				sql.append(" Se_Seats_No = '"+ttCrmSeatsPO2.getSeSeatsNo()+"'," );
				sql.append(" Se_Ext =  "+ttCrmSeatsPO2.getSeExt()+"," );
				//sql.append(" Se_Ip = "+ttCrmSeatsPO2.getSeIp()+"," );
				sql.append(" Se_Ip = '"+ttCrmSeatsPO2.getSeIp()+"'," );
				sql.append(" SE_LEVEL = "+ttCrmSeatsPO2.getSeLevel()+"," );
				sql.append(" ST_ID = "+ttCrmSeatsPO2.getStId()+"," );
				sql.append(" SE_IS_MANAMGER = "+ttCrmSeatsPO2.getSeIsManamger()+"," );
				sql.append(" se_status = "+Constant.STATUS_ENABLE );
				sql.append(" where SE_ID = "+ttCrmSeatsPO2.getSeId() );
				
				if(seStatus==Constant.STATUS_ENABLE){
					//soap.editAgentInfo(seAccount+"|"+seName+"|"+"1234"+"|"+seName+"|"+seExt , seAccount+"|1|1|1|1", 1);
					TtCrmSeatsTeamPO teamPO = new TtCrmSeatsTeamPO();
					// 2013.9.6艾春添加 坐席组为空判断
					if(!"".equals(seatTeam)) {
						teamPO.setStId(Long.parseLong(seatTeam));
						List<TtCrmSeatsTeamPO> Teamlist= dao.select(teamPO);
						if(Teamlist.size() > 0) {
							//soap.magACDGPMember(seAccount+"|"+seName+"|"+Teamlist.get(0).getStCode()+"|"+Teamlist.get(0).getStName()+"|"+"0",seAccount+"|1|1|1|1" , 1);
						}else {
							//soap.magACDGPMember(seAccount+"|"+seName+"|"+""+"|"+""+"|"+"0",seAccount+"|1|1|1|1" , 1);
						}
					}else {
						//soap.magACDGPMember(seAccount+"|"+seName+"|"+"2"+"|"+"呼出坐席组"+"|"+"0",seAccount+"|1|1|1|1" , 1);
					}
				}else{
					//soap.editAgentInfo(seAccount+"|"+seName+"|"+"1234"+"|"+seName+"|"+seExt , seAccount+"|1|1|1|1", 0);
					TtCrmSeatsTeamPO teamPO = new TtCrmSeatsTeamPO();
					// 2013.9.6艾春添加 坐席组为空判断
					if(!"".equals(seatTeam)) {
						teamPO.setStId(Long.parseLong(seatTeam));
						List<TtCrmSeatsTeamPO> Teamlist= dao.select(teamPO);
						if(Teamlist.size() > 0) {
							//soap.magACDGPMember(seAccount+"|"+seName+"|"+Teamlist.get(0).getStCode()+"|"+Teamlist.get(0).getStName()+"|"+"0",seAccount+"|1|1|1|1" , 0);
						}else {
							//soap.magACDGPMember(seAccount+"|"+seName+"|"+""+"|"+""+"|"+"0",seAccount+"|1|1|1|1" , 0);
						}
					}else {
						//soap.magACDGPMember(seAccount+"|"+seName+"|"+"2"+"|"+"呼出坐席组"+"|"+"0",seAccount+"|1|1|1|1" , 0);
					}
				}
				dao.update(sql.toString(), null);
			}else{
				//soap.editAgentInfo(seAccount+"|"+seName+"|"+"1234"+"|"+seName+"|"+seExt , seAccount+"|1|1|1|1", 0);
				TtCrmSeatsTeamPO teamPO = new TtCrmSeatsTeamPO();
				// 2013.9.6艾春添加 坐席组为空判断
				if(!"".equals(seatTeam)) {
					teamPO.setStId(Long.parseLong(seatTeam));
					List<TtCrmSeatsTeamPO> Teamlist= dao.select(teamPO);
					if(Teamlist.size() > 0) {
						//soap.magACDGPMember(seAccount+"|"+seName+"|"+Teamlist.get(0).getStCode()+"|"+Teamlist.get(0).getStName()+"|"+"0",seAccount+"|1|1|1|1" , 0);
					}else {
						//soap.magACDGPMember(seAccount+"|"+seName+"|"+""+"|"+""+"|"+"0",seAccount+"|1|1|1|1" , 0);
					}
				}else {
					//soap.magACDGPMember(seAccount+"|"+seName+"|"+"2"+"|"+"呼出坐席组"+"|"+"0",seAccount+"|1|1|1|1" , 0);
				}
				dao.insert(ttCrmSeatsPO2);
			}
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"坐席修改");
			logger.error(logger,e1);
			act.setException(e1);
		}

	}
	//注销坐席
	public void destroySeatsSet(){
		long seid = Long.parseLong(CommonUtils.checkNull(request.getParamValue("seid"))); 
		SeatsSetDao dao = SeatsSetDao.getInstance();
		TtCrmSeatsPO ttCrmSeatsPO1 = new TtCrmSeatsPO();
		TtCrmSeatsPO ttCrmSeatsPO2 = new TtCrmSeatsPO();
		ttCrmSeatsPO1.setSeId(seid);
		
		ttCrmSeatsPO2.setSeStatus(Constant.STATUS_DISABLE);
		dao.update(ttCrmSeatsPO1,ttCrmSeatsPO2);
		
		List<TtCrmSeatsPO> list= dao.select(ttCrmSeatsPO1); 
		
		UltraCRMWebservice ultraCRMWebservice = new UltraCRMWebservice();
		UltraCRMWebserviceSoap soap = ultraCRMWebservice.getUltraCRMWebserviceSoap();
		
		soap.editAgentInfo(list.get(0).getSeSeatsNo()+"|"+"1"+"|"+"1234"+"|"+"1"+"|"+"1" , list.get(0).getSeSeatsNo()+"|1|1|1|1", 2);
		soap.magACDGPMember(list.get(0).getSeSeatsNo()+"|"+"1"+"|"+""+"|"+""+"|"+"0",list.get(0).getSeSeatsNo()+"|1|1|1|1" , 2);
		
		act.setOutData("success", "true");
	}
	
	public boolean isAdmin(long userId){
		SeatsSetDao dao = SeatsSetDao.getInstance();
		List<TtCrmSeatsPO> TtCrmSeatsPOList = dao.queryTtCrmSeatsPOByUserId(userId);
		TtCrmSeatsPO ttCrmSeatsPO = null;
		if(TtCrmSeatsPOList!=null && TtCrmSeatsPOList.size()>0)  ttCrmSeatsPO = TtCrmSeatsPOList.get(0);
		if(ttCrmSeatsPO != null && ttCrmSeatsPO.getSeIsManamger().toString().equals(Constant.se_is_manamger_1.toString())) return true;
		else return false;
	}

}