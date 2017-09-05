/**   
* @Title: DownloadcodeMain.java 
* @Package com.infodms.dms.actions.claim.basicData 
* @Description: TODO(下发代码维护) 
* @author wangjinbao   
* @date 2010-5-28 下午04:05:09 
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
import com.infodms.dms.dao.claim.basicData.DownloadCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessChngCodePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: DownloadcodeMain 
 * @Description: TODO(下发代码维护) 
 * @author wangjinbao 
 * @date 2010-5-28 下午04:05:09 
 *  
 */
public class DownloadcodeMain {
	private Logger logger = Logger.getLogger(DownloadcodeMain.class);
	private final DownloadCodeDao dao = DownloadCodeDao.getInstance();
	private final String DOWN_LOAD_CODE_URL = "/jsp/claim/basicData/downloadCodeIndex.jsp";//主页面（查询）
	private final String DOWN_LOAD_CODE_ADD_URL = "/jsp/claim/basicData/downloadCodeAdd.jsp";//新增页面
	private final String DOWN_LOAD_CODE_UPDATE_URL = "/jsp/claim/basicData/downloadCodeUpdate.jsp";//修改页面
	/**
	 * 
	* @Title: downloadcodeInit 
	* @Description: 下发代码维护初始化
	* @param         
	* @return void  返回类型 
	* @throws
	 */
	public void downloadcodeInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(DOWN_LOAD_CODE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"下发代码维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: downloadcodeQuery 
	* @Description: 下发代码维护查询
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void downloadcodeQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);    
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				//代码列别
				String typeCode = request.getParamValue("TYPE_CODE");
				String code = request.getParamValue("CODE");//代码
				String codeName = request.getParamValue("CODE_NAME");//描述
				//拼sql的查询条件
				if (Utility.testString(typeCode)) {
					sb.append(" and tbcc.type_code = ? ");
					params.add(typeCode);
				}
				if (Utility.testString(code)) {
					sb.append(" and upper(tbcc.code) like ? ");
					params.add("%"+code.toUpperCase()+"%");
				}
				if (Utility.testString(codeName)) {
					sb.append(" and tbcc.code_name like ? ");
					params.add("%"+codeName+"%");
				}				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.downloadCodeQuery(Constant.PAGE_SIZE, curPage,companyId ,sb.toString(),params);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"下发代码维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: downloadcodeAddInit 
	* @Description: 下发代码维护新增初始化
	* @param         
	* @return void  返回类型 
	* @throws
	 */
	public void downloadcodeAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(DOWN_LOAD_CODE_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"下发代码维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**
	 * 下发代码维护增加
	* @Title: downloadCodeAdd 
	* @Description: 下发代码维护增加
	* @param          无 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void downloadCodeAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId=GetOemcompanyId.getOemCompanyId(logonUser); 
		try {
			RequestWrapper request = act.getRequest();
			
			String typeCode = request.getParamValue("TYPE_CODE");//类别
			String code = request.getParamValue("CODE");//代码
			String codeName = request.getParamValue("CODE_NAME");//描述
			
			List list = isExist(code,typeCode,oemCompanyId);
			if(list != null && list.size() > 0){//存在
				act.setOutData("returnValue", code);
			}else{
				TmBusinessChngCodePO tbccpo = new TmBusinessChngCodePO();
				tbccpo.setBusinessCodeId(Long.parseLong(SequenceManager.getSequence("")));
				tbccpo.setTypeCode(typeCode);
				tbccpo.setCode(code);
				tbccpo.setCodeName(codeName);
				tbccpo.setIsSend(Constant.DOWNLOAD_CODE_STATUS_01);//待下发
				tbccpo.setCreateBy(logonUser.getUserId());
				tbccpo.setCreateDate(new Date());
				tbccpo.setOemCompanyId(oemCompanyId);
				dao.insertDownloadCode(tbccpo);
			}
			act.setOutData("success", "true");
		}  catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"下发代码维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 
	* @Title: downloadcodeUpdateInit 
	* @Description: 下发代码维护修改初始化
	* @param         
	* @return void  返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void downloadcodeUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TmBusinessChngCodePO tbccpo = null;
		List list = null;
		try {
			RequestWrapper request = act.getRequest();
			String businessCodeId = request.getParamValue("BUSINESS_CODE_ID");//业务基础代码ID
			tbccpo = new TmBusinessChngCodePO();
			tbccpo.setBusinessCodeId(new Long(businessCodeId));
			list = dao.select(tbccpo);
			TmBusinessChngCodePO result = new TmBusinessChngCodePO();
			if(list != null && list.size() > 0){
				result = (TmBusinessChngCodePO)list.get(0);
			}
			request.setAttribute("businesscode", result);
			act.setForword(DOWN_LOAD_CODE_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"下发代码维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: downloadcodeUpdate 
	* @Description: TODO(下发代码维护修改) 
	* @param    
	* @return void  
	* @throws
	 */
	public void downloadcodeUpdate() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TmBusinessChngCodePO tbccpo = null;
		TmBusinessChngCodePO updatetbccpo = null;
		try {
			RequestWrapper request = act.getRequest();
			String businessCodeId = request.getParamValue("BUSINESS_CODE_ID"); //业务基础代码ID
			String codeName = request.getParamValue("CODE_NAME"); // 代码名称(描述)
			
			tbccpo = new TmBusinessChngCodePO();
			tbccpo.setBusinessCodeId(new Long(businessCodeId));
			
			updatetbccpo = new TmBusinessChngCodePO();
			updatetbccpo.setCodeName(codeName);
			updatetbccpo.setIsSend(Constant.DOWNLOAD_CODE_STATUS_01);//待下发
			updatetbccpo.setUpdateBy(logonUser.getUserId());
			updatetbccpo.setUpdateDate(new Date());
			
			
			dao.updateDownloadCode(tbccpo,updatetbccpo);
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"下发代码维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: downloadcodeDel 
	* @Description: TODO(下发代码维护删除) 
	* @param    
	* @return void  
	* @throws
	 */
	public void downloadcodeDel() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TmBusinessChngCodePO tbccpo = null;
		TmBusinessChngCodePO updatetbccpo = null;
		try {
			RequestWrapper request = act.getRequest();
			String businessCodeId = Utility.getString(request.getParamValue("BUSINESS_CODE_ID")); //业务基础代码ID
			String codeName = Utility.getString(request.getParamValue("CODE_NAME")); // 代码名称(描述)
			
			tbccpo = new TmBusinessChngCodePO();
			tbccpo.setBusinessCodeId(new Long(businessCodeId));
			
			updatetbccpo = new TmBusinessChngCodePO();
			updatetbccpo.setCodeName(codeName);
			updatetbccpo.setIsSend(Constant.DOWNLOAD_CODE_STATUS_01);//待下发
			updatetbccpo.setIsDel(Constant.IS_DEL);
			updatetbccpo.setUpdateBy(logonUser.getUserId());
			updatetbccpo.setUpdateDate(new Date());
			dao.updateDownloadCode(tbccpo,updatetbccpo);
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"下发代码维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: sendAll 
	* @Description: TODO(下发代码维护下发) 
	* @param    
	* @return void  
	* @throws
	 */
	public void sendAll(){
		//检索数据所有状态是“待下发的”，循环给所有经销商
	}
	@SuppressWarnings("unchecked")
	private List isExist(String code,String typeCode, long oemCompanyId){
		List list = null;
		TmBusinessChngCodePO po = new TmBusinessChngCodePO();
		po.setCode(code);
		po.setTypeCode(typeCode);
		po.setOemCompanyId(oemCompanyId);
		list = dao.select(po);
		return list;
		
	}

}
