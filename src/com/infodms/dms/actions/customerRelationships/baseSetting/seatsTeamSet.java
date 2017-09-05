package com.infodms.dms.actions.customerRelationships.baseSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.tempuri.UltraCRMWebservice;
import org.tempuri.UltraCRMWebserviceSoap;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.SeatsTeamSetDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmSeatsTeamPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : seatsTeamSet 
 * @Description   : 坐席组设置
 * @author        : wangming
 * CreateDate     : 2013-4-2
 */
public class seatsTeamSet {
	private static Logger logger = Logger.getLogger(seatsTeamSet.class);
	// 坐席组初始化页面
	private final String seatsTeamSetUrl = "/jsp/customerRelationships/baseSetting/seatsTeamSet.jsp";
	//坐席组新增页面
	private final String seatsTeamSetAddOrUpdateUrl = "/jsp/customerRelationships/baseSetting/seatsTeamSetAddOrUpdate.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * 坐席组初始化
	 */
	public void seatsTeamSetInit(){		
		try{
			act.setForword(seatsTeamSetUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"坐席组初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void querySeatsTeamSet(){
		act.getResponse().setContentType("application/json");
		try{
			
			String seatsName = CommonUtils.checkNull(request.getParamValue("seatsName"));  				
			
			SeatsTeamSetDao dao = SeatsTeamSetDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> seatsTeamSetData = dao.querySeatsTeamSet(seatsName,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", seatsTeamSetData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"坐席组查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 坐席组增加或修改
	 * @Description: 坐席组增加或修改 
	 * LastDate    : 2013-4-1
	 */
	public void addOrUpdateSeatsTeamSet(){
		ActionContext act = ActionContext.getContext();
		
		String id = request.getParamValue("id");
		if(id!=null&&!"".equals(id)){
			SeatsTeamSetDao dao = SeatsTeamSetDao.getInstance();
			TtCrmSeatsTeamPO ttCrmSeatsTeamPO = new TtCrmSeatsTeamPO();
			ttCrmSeatsTeamPO.setStId(Long.valueOf(id));
			TtCrmSeatsTeamPO ttCrmSeatsTeamPO2 = dao.queryTtCrmSeatsTeamPO(ttCrmSeatsTeamPO);
			act.setOutData("ttCrmSeatsTeamPO", ttCrmSeatsTeamPO2);
		}
		
		act.setForword(seatsTeamSetAddOrUpdateUrl);
	}
	/**
	 * 
	 * @Title      : 新增修改坐席组提交
	 * @Description: TODO 新增修改坐席组提交
	 * LastDate    : 2013-4-1
	 */
	public void addOrUpdateSeatsTeamSetSubmit(){
		
		String seatsTeamName = CommonUtils.checkNull(request.getParamValue("seatsTeamName"));  				//座席组名
		String seatsTeamCode = CommonUtils.checkNull(request.getParamValue("seatsTeamCode"));  				//座席编号
		String remark = CommonUtils.checkNull(request.getParamValue("remark"));  		        		//备注
		
		try{
			SeatsTeamSetDao dao = SeatsTeamSetDao.getInstance();
			
			TtCrmSeatsTeamPO ttCrmSeatsTeamPO = new TtCrmSeatsTeamPO();
			TtCrmSeatsTeamPO ttCrmSeatsTeamPO2 = new TtCrmSeatsTeamPO();
			
			String id = request.getParamValue("id");
			if(id!=null&&!"".equals(id)){
				ttCrmSeatsTeamPO2.setStId(Long.valueOf(id));
				ttCrmSeatsTeamPO.setUpdateBy(logonUser.getUserId());
				ttCrmSeatsTeamPO.setUpdateDate(new Date());
			}else{		
				ttCrmSeatsTeamPO.setStId(new Long(SequenceManager.getSequence("")));
				ttCrmSeatsTeamPO.setCreateBy(logonUser.getUserId());
				ttCrmSeatsTeamPO.setCreateDate(new Date());
				ttCrmSeatsTeamPO.setStCode(seatsTeamCode);
			}
			ttCrmSeatsTeamPO.setStName(seatsTeamName);
			ttCrmSeatsTeamPO.setStMemo(remark);
			/*UltraCRMWebservice ultraCRMWebservice = new UltraCRMWebservice();
			UltraCRMWebserviceSoap soap = ultraCRMWebservice.getUltraCRMWebserviceSoap();
			*/
			if(id!=null&&!"".equals(id)){
				
				//	soap.editACDGroupInfo( seatsTeamCode+"|"+seatsTeamName+"|"+"1"+"|"+"1",seatsTeamCode+"|"+seatsTeamName+"|"+"1"+"1" , 1);
					dao.update(ttCrmSeatsTeamPO2, ttCrmSeatsTeamPO);
			}else{
				TtCrmSeatsTeamPO ttCrmSeatsTeamPO3 = new TtCrmSeatsTeamPO();
				ttCrmSeatsTeamPO3.setStCode(seatsTeamCode);
				
				if(dao.select(ttCrmSeatsTeamPO3).size()  > 0 )
				{
					act.setOutData("bug", "false");
				}else
				{
					//soap.editACDGroupInfo(seatsTeamCode+"|"+seatsTeamName+"|"+"1"+"|"+"1","1|1|1|1" , 0);
					dao.insert(ttCrmSeatsTeamPO);
				}
				
			}
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"坐席组新增修改");
			logger.error(logger,e1);
			act.setException(e1);
		}

	}
	
	public void delSeatsTeamSetSubmit(){
		try{
			String ids = request.getParamValue("ids");
			ids = ids.replaceAll(",", "','");
			SeatsTeamSetDao dao = SeatsTeamSetDao.getInstance();
			String dateStr = DateTimeUtil.parseDateToDate(new Date());
			StringBuffer sql = new StringBuffer();
			sql.append(" update tt_crm_seats_team t \n");
			sql.append(" set t.delete_by = '"+logonUser.getUserId()+"',t.delete_date=to_date('"+dateStr+"','yyyy-MM-dd'),t.status = "+Constant.STATUS_DISABLE+" \n");
			sql.append(" where t.st_id in('"+ids+"')");
			dao.update(sql.toString(), null);
			
			sql = new StringBuffer();
			sql.append(" select t.ST_CODE,t.ST_NAME from tt_crm_seats_team t  where t.ST_ID in ('"+ids+"')");
			List<TtCrmSeatsTeamPO> list= dao.select(TtCrmSeatsTeamPO.class,sql.toString(),null);
			
			/*UltraCRMWebservice ultraCRMWebservice = new UltraCRMWebservice();
			UltraCRMWebserviceSoap soap = ultraCRMWebservice.getUltraCRMWebserviceSoap();*/
			for(TtCrmSeatsTeamPO crmSeatsTeamPO : list)
			{
				//soap.editACDGroupInfo(crmSeatsTeamPO.getStCode()+"|"+crmSeatsTeamPO.getStName()+"|"+"1"+"|"+"1","1|1|1|1" , 2);
			}
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"坐席组");
			logger.error(logger,e1);
			act.setException(e1);
		}	
	}

}