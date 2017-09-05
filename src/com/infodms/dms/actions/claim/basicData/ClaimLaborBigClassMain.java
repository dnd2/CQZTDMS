/**   
* @Title: ClaimLaborBigClassMain.java 
* @Package com.infodms.dms.actions.claim.basicData 
* @Description: TODO(索赔工时大类维护Action) 
* @author wangjinbao   
* @date 2010-7-14 下午01:59:51 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.basicData;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ConditionBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.basicData.ClaimLaborBigClassDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrWrlabinfoPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimLaborBigClassMain 
 * @Description: TODO(索赔工时大类维护Action) 
 * @author wangjinbao 
 * @date 2010-7-14 下午01:59:51 
 *  
 */
public class ClaimLaborBigClassMain {
	private Logger logger = Logger.getLogger(ClaimLaborBigClassMain.class);
	private final ClaimLaborBigClassDao dao = ClaimLaborBigClassDao.getInstance();
	private final String LABOR_BIG_CLASS_URL = "/jsp/claim/basicData/claimLaborBigClassIndex.jsp";//主页面（查询）
	private final String LABOR_BIG_CLASS_ADD_URL = "/jsp/claim/basicData/claimLaborBigClassAdd.jsp";//新增页面
	private final String LABOR_BIG_CLASS_SELECT_URL = "/jsp/claim/basicData/claimLaborBigClassQuery.jsp";//选择父类页面
	private final String LABOR_BIG_CLASS_UPDATE_URL = "/jsp/claim/basicData/claimLaborBigClassModify.jsp";//修改页面
	/**
	 * 
	* @Title: claimLaborBigClassInit 
	* @Description: TODO(索赔工时大类维护的初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborBigClassInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(LABOR_BIG_CLASS_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时大类维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimLaborBigClassQuery 
	* @Description: TODO(索赔工时大类维护查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborBigClassQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				//代码列别
				String code = request.getParamValue("CODE");//代码
				String codeName = request.getParamValue("CODE_NAME");//名称
				//拼sql的查询条件				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				ConditionBean bean = new ConditionBean();
				bean.setConOne(code);
				bean.setConTwo(codeName);
				
				PageResult<Map<String, Object>> ps = dao.laborBigClassQuery(Constant.PAGE_SIZE, curPage,bean,oemCompanyId);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时大类维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: claimLaborBigClassAddInit 
	* @Description: TODO(索赔工时大类维护新增初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborBigClassAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(LABOR_BIG_CLASS_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时大类维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: laborPaterClassQueryInit 
	* @Description: TODO(索赔工时大类维护大类选择) 
	* @param    
	* @return void  
	* @throws
	 */
	public void laborPaterClassQueryInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(LABOR_BIG_CLASS_SELECT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时大类维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: laborPaterClassQuery 
	* @Description: TODO(索赔工时大类选择父类页面) 
	* @param    
	* @return void  
	* @throws
	 */
	public void laborPaterClassQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				//代码列别
				String code = request.getParamValue("CODE");//代码
				String codeName = request.getParamValue("CODE_NAME");//名称
				//拼sql的查询条件				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				ConditionBean bean = new ConditionBean();
				bean.setConOne(code);
				bean.setConTwo(codeName);
				
				PageResult<Map<String, Object>> ps = dao.laborBigClassQuery(Constant.PAGE_SIZE, curPage,bean,oemCompanyId);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时大类维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: claimLaborBigClassAdd 
	* @Description: TODO(索赔工时大类维护新增) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimLaborBigClassAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		try {
			RequestWrapper request = act.getRequest();
			String errorMsg = null;
			String laborCode = request.getParamValue("CODE");  //索赔工时大类代码
			String laborName = request.getParamValue("CN_DES");  //索赔工时大类名称
//			String wrgroupId = request.getParamValue("WRGROUP_ID");  //索赔车型组ID
//			String paterClassCode = request.getParamValue("P_LABOUR_CODE");//父类代码
			String paterId = request.getParamValue("P_ID");//父类ID
			String remark = request.getParamValue("REMARK");//备注

			
			
			//判断大类是否存在：
			List list = dao.getPaterClassByCode(laborCode);
			if(list != null && list.size() == 0){
				//待增加的po
				TtAsWrWrlabinfoPO addpo = new TtAsWrWrlabinfoPO();
				addpo.setCreateBy(logonUser.getUserId());
				addpo.setCreateDate(new Date());				
				addpo.setCnDes(laborName);
				addpo.setLabourCode(laborCode);
				addpo.setTreeCode(Constant.CLAIM_LABHOUR_TREE_CODE_02);
				addpo.setLabourHour(Float.parseFloat("0"));
				addpo.setLabourQuotiety(Float.parseFloat("0"));
				addpo.setPaterId(Utility.getLong(paterId));//上级大类
				addpo.setRemark(remark);//备注
				addpo.setId(Long.parseLong(SequenceManager.getSequence("")));
				addpo.setOemCompanyId(oemCompanyId);
				dao.insert(addpo);
			}else{
				errorMsg = laborCode;//工时代码已经存在
			}
			act.setOutData("error", errorMsg);
			act.setOutData("success", "true");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时大类维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimLaborBigClassUpdateInit 
	* @Description: TODO(索赔工时大类维护修改初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborBigClassUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id=request.getParamValue("ID");
			HashMap hm = null;
			hm = dao.getLaborBigClassById(new Long(id));
			
			act.setOutData("SELMAP", hm);//对应的索赔工时大类
			act.setForword(LABOR_BIG_CLASS_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时大类维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimLaborBigClassUpdate 
	* @Description: TODO(索赔工时大类维护修改) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimLaborBigClassUpdate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrWrlabinfoPO selpo = null;
		TtAsWrWrlabinfoPO updatepo = null;
		try {
			RequestWrapper request = act.getRequest();
			String id=request.getParamValue("ID");//待修改的主键ID
			String laborCode = request.getParamValue("CODE");  //索赔工时大类代码
			String laborName = request.getParamValue("CN_DES");  //索赔工时大类名称
			String paterId = request.getParamValue("P_ID");//父类ID
			String remark = request.getParamValue("REMARK");//备注
			//待修改的PO
			selpo = new TtAsWrWrlabinfoPO();
			selpo.setId(Utility.getLong(id));
			selpo.setLabourCode(laborCode);
			//修改po
			updatepo = new TtAsWrWrlabinfoPO();
			updatepo.setCnDes(laborName);
			updatepo.setRemark(remark);
			updatepo.setPaterId(Utility.getLong(paterId));
			updatepo.setUpdateBy(logonUser.getUserId());
			updatepo.setUpdateDate(new Date());
			
			dao.update(selpo, updatepo);
	
			act.setOutData("success", "true");

		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"索赔工时大类维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimLaborBigClassDel 
	* @Description: TODO(索赔工时大类删除) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimLaborBigClassDel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrWrlabinfoPO selpo = null;
		TtAsWrWrlabinfoPO updatepo = null;
		try {
			RequestWrapper request = act.getRequest();
			String id=request.getParamValue("ID");//待修改的主键ID
			//待修改的PO
			selpo = new TtAsWrWrlabinfoPO();
			selpo.setId(Utility.getLong(id));

			//修改po
			updatepo = new TtAsWrWrlabinfoPO();
			updatepo.setIsDel(Constant.IS_DEL);//删除标志=1
			updatepo.setUpdateBy(logonUser.getUserId());
			updatepo.setUpdateDate(new Date());
			
			dao.update(selpo, updatepo);
	
			act.setOutData("success", "true");

		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"索赔工时大类维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}	
	
	

}
