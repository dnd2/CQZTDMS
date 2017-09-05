/**   
* @Title: ClaimLaborMain.java 
* @Package com.infodms.dms.actions.claim.basicData 
* @Description: TODO(索赔工时维护Action) 
* @author Administrator   
* @date 2010-6-1 上午11:05:38 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.basicData;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.ClaimCommonAction;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.basicData.ClaimLaborDao;
import com.infodms.dms.dao.claim.basicData.PartsAssemblyDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrAdditionalitemPO;
import com.infodms.dms.po.TtAsWrPartsAssemblyPO;
import com.infodms.dms.po.TtAsWrTrobleMapPO;
import com.infodms.dms.po.TtAsWrWrlabinfoPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimLaborMain 
 * @Description: TODO(索赔工时维护Action) 
 * @author Administrator 
 * @date 2013-04-12 上午11:05:38 
 *  
 */
public class PartsAssembly {
	private Logger logger = Logger.getLogger(PartsAssembly.class);
	private final PartsAssemblyDao dao = PartsAssemblyDao.getInstance();
	private final String VIEW_URL = "/jsp/claim/basicData/partsAccemblyIndex.jsp";//主页面（查询）
	private final String MODIFY_URL = "/jsp/claim/basicData/partsAccemblyModify.jsp";//修改页面（查询）
	private final String ADD_URL = "/jsp/claim/basicData/partsAccemblyAdd.jsp";//新增页面（查询）
	private final ClaimCommonAction claimCommon = ClaimCommonAction.getInstance();
	/**
	 * 
	* @Title: claimLaborInit 
	* @Description: TODO(总成维护跳转界面) 
	* @param    
	* @return void  
	* @throws
	 */
	public void PartsAssemblyMain(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			act.setForword(VIEW_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**********
	 * add user:xiongchuan
	 * add date:2013-04-12
	 * remark： 总成维护查询功能
	 */
	public void PartsAssemblyView(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String parts_accembly_code= request.getParamValue("parts_accembly_code");
			String parts_accembly_name= request.getParamValue("parts_accembly_name");
			String status= request.getParamValue("TYPE_CODE");
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<TtAsWrPartsAssemblyPO>  ps = dao.getPartsAssemblyView(parts_accembly_code, parts_accembly_name, status, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"总成维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**********
	 * add user:xiongchuan
	 * add date:2013-04-12
	 * remark： 总成维护删除功能
	 */
	public void delPartsAssembly(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String recesel [] = request.getParamValues("recesel");
			TtAsWrPartsAssemblyPO po = new TtAsWrPartsAssemblyPO();
			TtAsWrPartsAssemblyPO po1 = new TtAsWrPartsAssemblyPO();
			for(String id:recesel){
				po.setPartsAssemblyId(Long.valueOf(id));
				po1.setStatus(Constant.STATUS_DISABLE);
			 dao.update(po,po1);
			}
			
			act.setOutData("falg", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"总成维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**********
	 * add user:xiongchuan
	 * add date:2013-04-12
	 * remark： 总成维护修改跳转功能
	 */
	public void addPartsAssembly(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			act.setForword(ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"总成修改报错");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**********
	 * add user:xiongchuan
	 * add date:2013-04-12
	 * remark： 总成维护修改跳转功能
	 */
	public void modifyPartsAssembly(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String recesel = request.getParamValue("ID");
			TtAsWrPartsAssemblyPO po = new TtAsWrPartsAssemblyPO();
			po.setPartsAssemblyId(Long.valueOf(recesel));
			List<TtAsWrPartsAssemblyPO> lit =   dao.select(po);
			
			act.setOutData("lit", lit.get(0));
			act.setForword(MODIFY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"总成修改报错");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	
	
	/**********
	 * add user:xiongchuan
	 * add date:2013-04-13
	 * remark： 总成维护修改保存功能
	 */
	public void savePartsAssembly(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String id = request.getParamValue("ID");
			String partsAssemblyCode = request.getParamValue("parts_accembly_code");
			String partsAssemblyName = request.getParamValue("parts_accembly_name");
			String TYPE_CODE = request.getParamValue("TYPE_CODE");
			TtAsWrPartsAssemblyPO po = new TtAsWrPartsAssemblyPO();
			TtAsWrPartsAssemblyPO po1 = new TtAsWrPartsAssemblyPO();
			po.setPartsAssemblyId(Long.valueOf(id));
			po1.setPartsAssemblyCode(partsAssemblyCode);
			po1.setPartsAssemblyName(partsAssemblyName);
			po1.setStatus(Integer.valueOf(TYPE_CODE));
			dao.update(po, po1);
			act.setOutData("flag", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"总成修改报错");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	
	
	
	
	/**********
	 * add user:xiongchuan
	 * add date:2013-04-13
	 * remark： 总成维护修改保存功能
	 */
	public void addSavePartsAssembly(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Long id = Utility.getLong(SequenceManager.getSequence(""));
			String partsAssemblyCode = request.getParamValue("parts_accembly_code");
			String partsAssemblyName = request.getParamValue("parts_accembly_name");
			String TYPE_CODE = request.getParamValue("TYPE_CODE");
			TtAsWrPartsAssemblyPO po1 = new TtAsWrPartsAssemblyPO();
			po1.setPartsAssemblyId(Long.valueOf(id));
			po1.setPartsAssemblyCode(partsAssemblyCode);
			po1.setPartsAssemblyName(partsAssemblyName);
			po1.setStatus(Integer.valueOf(TYPE_CODE));
			po1.setCreateDate( new Date());
			po1.setCreateBy(logonUser.getUserId());
			po1.setIsDe(0);//默认未下发
			dao.insert(po1);
			act.setOutData("flag", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"总成修改报错");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
}
