/**   
* @Title: PreclaimPreQuery.java 
* @Package com.infodms.dms.actions.claim.preAuthorization 
* @Description: TODO(索赔预申请状态查询Action) 
* @author wangjinbao   
* @date 2010-6-25 下午07:31:57 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.preAuthorization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ConditionBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.preAuthorization.PreclaimPreQueryDealerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: PreclaimPreQuery 
 * @Description: TODO(索赔预申请状态查询Action(经销商端)) 
 * @author wangjinbao 
 * @date 2010-6-25 下午07:31:57 
 *  
 */
public class PreclaimPreQueryDealer {
	private Logger logger = Logger.getLogger(PreclaimPreQueryDealer.class);
	private final PreclaimPreQueryDealerDao dao = PreclaimPreQueryDealerDao.getInstance();
	private final String PRE_CLAIM_DETAIL_URL = "/jsp/claim/preAuthorization/preclaimDetailDealer.jsp";//明细页面
	private final String PRE_CLAIM_QUERY_URL ="/jsp/claim/preAuthorization/preclaimQueryDealer.jsp";//查询页面（主页面）

	/**
	 * 
	* @Title: preclaimPreQueryInit 
	* @Description: TODO(索赔预申请状态查询初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void preclaimPreQueryInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(PRE_CLAIM_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预申请状态查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: preclaimPreQuery 
	* @Description: TODO(索赔预申请状态查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void preclaimPreQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				String dealerCode = logonUser.getDealerId();//当前用户对应的经销商id
				String orderId = request.getParamValue("ORDER_ID");//工单号
				String roNo = request.getParamValue("RO_NO");//预授权单号
				String vin = request.getParamValue("VIN");
				String itemcode = request.getParamValue("ITEMCODE");//项目代码
				String preAuthItem = request.getParamValue("PRE_AUTH_ITEM");//项目类型
				String preclaimAudit = request.getParamValue("PRECLAIM_AUDIT");//提报状态
				String beginTime = request.getParamValue("beginTime");//申请日期起
				String endTime = request.getParamValue("endTime");//申请日期止
				ConditionBean bean = new ConditionBean();//查询条件bean
				bean.setConOne(dealerCode);
				bean.setConTwo(orderId);
				bean.setConThree(vin);
				bean.setConFour(itemcode);
				bean.setConFive(preAuthItem);
				bean.setConSix(preclaimAudit);
				bean.setConSeven(beginTime);
				bean.setConEight(endTime);
				bean.setConNine(roNo);
				//modify at 2010-07-19 start
				Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
				String dealerId = logonUser.getDealerId();  //经销商ID
				//modify end
				PageResult<Map<String, Object>> ps = dao.preclaimPreQuery(companyId,dealerId,Constant.PAGE_SIZE, curPage,bean);
				act.setOutData("ps", ps);
			}
		} 
		catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} 
		catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预申请状态查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: preclaimDetail 
	* @Description: TODO(索赔预申请状态查询明细) 
	* @param    
	* @return void  
	* @throws
	 */
	public void preclaimDetail() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");//主键id，子表的ID
			//根据主键查找索赔预申请明细
			//modify at 2010-07-19 start
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
			String dealerId = logonUser.getDealerId();  //经销商ID
			//modify end			
			HashMap hashmap = dao.getPreclaimById(companyId,dealerId,id);
			//取附件信息：
			List<FsFileuploadPO> attachLs = dao.queryAttById(hashmap.get("FID").toString());// 取得附件
			act.setOutData("attachLs", attachLs);//附件列表			
			act.setOutData("FOREAPPROVAL_HASHMAP", hashmap);//索赔预申请详细信息
			act.setForword(PRE_CLAIM_DETAIL_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预申请状态查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}

}
