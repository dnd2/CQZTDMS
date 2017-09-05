/**   
* @Title: ClaimOtherFeeMain.java 
* @Package com.infodms.dms.actions.claim.basicData 
* @Description: TODO(索赔其它费用维护) 
* @author wangjinbao   
* @date 2010-5-31 下午04:05:09 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.basicData;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.basicData.ClaimOtherFeeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtAsWrOtherfeePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimOtherFeeMain 
 * @Description: TODO(索赔其它费用维护) 
 * @author wangjinbao 
 * @date 2010-5-31 下午04:05:09 
 *  
 */
public class ClaimOtherFeeMain {
	private Logger logger = Logger.getLogger(ClaimOtherFeeMain.class);
	private final ClaimOtherFeeDao dao = ClaimOtherFeeDao.getInstance();
	private final String CLAIM_OTHER_FEE_URL = "/jsp/claim/basicData/claimOtherFeeIndex.jsp";//主页面（查询）
	private final String CLAIM_OTHER_FEE_ADD_URL = "/jsp/claim/basicData/claimOtherFeeAdd.jsp";//新增页面
	private final String CLAIM_OTHER_FEE_UPDATE_URL = "/jsp/claim/basicData/claimOtherFeeUpdate.jsp";//修改页面
	/**
	 * 
	* @Title: claimOtherFeeInit 
	* @Description: 索赔其它费用维护初始化
	* @param         
	* @return void  返回类型 
	* @throws
	 */
	public void claimOtherFeeInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(CLAIM_OTHER_FEE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔其它费用维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimOtherFeeQuery 
	* @Description: 索赔其它费用维护查询
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void claimOtherFeeQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				String code = request.getParamValue("CODE");//项目代码
				String codeName = request.getParamValue("CODE_NAME");//项目名称
				if (Utility.testString(code)) {
					sb.append(" and upper(tawo.fee_code) like ? ");
					params.add("%"+code.toUpperCase()+"%");
				}
				if (Utility.testString(codeName)) {
					sb.append(" and tawo.fee_name like ? ");
					params.add("%"+codeName+"%");
				}
				PageResult<Map<String, Object>> ps = dao.claimOtherFeeQuery(Constant.PAGE_SIZE, curPage,sb.toString(),params,oemCompanyId);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔其它费用维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: claimOtherFeeAddInit 
	* @Description: 索赔其它费用维护新增初始化
	* @param         
	* @return void  返回类型 
	* @throws
	 */
	public void claimOtherFeeAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(CLAIM_OTHER_FEE_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔其它费用维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**
	 * 
	* @Title: claimOtherFeeAdd 
	* @Description: 索赔其它费用维护增加
	* @param          无 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimOtherFeeAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		TtAsWrOtherfeePO tawo = null;
		TtAsWrOtherfeePO selpo = null;
		try {
			RequestWrapper request = act.getRequest();
			
			String feeCode = request.getParamValue("FEE_CODE");//费用代码
			String feeName = request.getParamValue("FEE_NAME");//费用名称
			selpo = new TtAsWrOtherfeePO();
			selpo.setFeeCode(feeCode);
			List list = dao.select(selpo);
			if(list != null && list.size() > 0){
				act.setOutData("feeCode", feeCode);//存在费用代码
				
			}else{
				tawo = new TtAsWrOtherfeePO();
				tawo.setFeeId(Long.parseLong(SequenceManager.getSequence("")));//id
				tawo.setFeeCode(feeCode);//费用代码
				tawo.setFeeName(feeName);//费用名称
				tawo.setCreateBy(logonUser.getUserId());
				tawo.setCreateDate(new Date());
				tawo.setOemCompanyId(oemCompanyId);
				dao.insertClaimOtherFee(tawo);
				act.setOutData("success", "true");
			}
		}  catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"索赔其它费用维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 
	* @Title: claimOtherFeeUpdateInit 
	* @Description: 索赔其它费用维护修改初始化
	* @param         
	* @return void  返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimOtherFeeUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrOtherfeePO tawo = null;
		List list = null;
		try {
			RequestWrapper request = act.getRequest();
			String feeId = request.getParamValue("FEE_ID");//费用ID
			tawo = new TtAsWrOtherfeePO();
			tawo.setFeeId(new Long(feeId));
			list = dao.select(tawo);
			TtAsWrOtherfeePO result = new TtAsWrOtherfeePO();
			if(list != null && list.size() > 0){
				result = (TtAsWrOtherfeePO)list.get(0);
			}
			request.setAttribute("otherfee", result);
			act.setForword(CLAIM_OTHER_FEE_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔其它费用维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimOtherFeeUpdate 
	* @Description: TODO(索赔其它费用维护修改) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimOtherFeeUpdate() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrOtherfeePO tawopo = null;
		TtAsWrOtherfeePO updatetawopo = null;
		try {
			RequestWrapper request = act.getRequest();
			String feeId = request.getParamValue("FEE_ID"); //费用ID
			String feeName = request.getParamValue("FEE_NAME"); // 费用名称
			
			tawopo = new TtAsWrOtherfeePO();
			tawopo.setFeeId(new Long(feeId));
			
			updatetawopo = new TtAsWrOtherfeePO();
			updatetawopo.setFeeName(feeName);
			updatetawopo.setUpdateBy(logonUser.getUserId());
			updatetawopo.setUpdateDate(new Date());
			
			dao.updateClaimOtherFee(tawopo,updatetawopo);
			act.setOutData("success", "true");
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"索赔其它费用维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * @author KFQ
	 * 删除车型组
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void claimModelDel(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		String id = request.getParamValue("did");
		try {
			TtAsWrModelGroupPO gp = new TtAsWrModelGroupPO();
			gp.setWrgroupId(Long.valueOf(id));
			dao.delete(gp);
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"车型组维护");
			act.setException(e1);
		}
		
		
	}
	/**
	 * 
	* @Title: claimOtherFeeDel 
	* @Description: TODO(索赔其它费用维护删除) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimOtherFeeDel() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrOtherfeePO tawopo = null;
		TtAsWrOtherfeePO updatetawopo = null;
		try {
			RequestWrapper request = act.getRequest();
			String feeId = Utility.getString(request.getParamValue("FEE_ID")); //费用ID
			
			tawopo = new TtAsWrOtherfeePO();
			tawopo.setFeeId(new Long(feeId));
			
			updatetawopo = new TtAsWrOtherfeePO();
			updatetawopo.setIsDel(Constant.IS_DEL);//删除标识：
			updatetawopo.setUpdateBy(logonUser.getUserId());
			updatetawopo.setUpdateDate(new Date());
			
			dao.updateClaimOtherFee(tawopo,updatetawopo);
			act.setOutData("success", "true");
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"索赔其它费用维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: sendAll 
	* @Description: TODO(下发方法) 
	* @param    
	* @return void  
	* @throws
	 */
	public void sendAll(){
		//检索数据所有状态是“待下发的”，循环给所有经销商
	}

}
