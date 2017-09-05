/**   
* @Title: ClaimPartWatchMain.java 
* @Package com.infodms.dms.actions.claim.authorization 
* @Description: TODO(监控配件维护ACTION) 
* @author Administrator   
* @date 2010-6-10 上午11:13:51 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.authorization;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jcifs.util.transport.Request;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.ClaimCommonAction;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.authorization.ClaimPartWatchDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrAuthmonitorpartPO;
import com.infodms.dms.po.TtAsWrAuthmonitortypePO;
import com.infodms.dms.po.TtAsWrWoorLevelPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimPartWatchMain 
 * @Description: TODO(监控配件维护action) 
 * @author Administrator 
 * @date 2010-6-10 上午11:13:51 
 *  
 */
public class ClaimPartWatchMain {
	private Logger logger = Logger.getLogger(ClaimLaborWatchMain.class);
	private final ClaimPartWatchDao dao = ClaimPartWatchDao.getInstance();
	private final String CLAIM_PART_WATCH_URL = "/jsp/claim/authorization/claimPartWatchIndex.jsp";//主页面（查询）
	private final String CLAIM_ORDER_WATCH_URL = "/jsp/claim/authorization/workOrderWatchIndex.jsp";//工单授权主页面（查询）
	private final String CLAIM_PART_WATCH_BIG_URL = "/jsp/claim/authorization/claimPartWatchBigIndex.jsp";//监控配件大类主页面（查询）Iverson add with 2010-11-09
	private final String CLAIM_PART_WATCH_ADD_URL = "/jsp/claim/authorization/claimPartWatchAdd.jsp";//新增页面
	private final String CLAIM_PART_WATCH_BIG_ADD_URL = "/jsp/claim/authorization/claimPartWatchBigAdd.jsp";//新增页面 Iverson add with 2010-11-09
	private final String CLAIM_PART_MODEL_QUERY_URL ="/jsp/claim/authorization/claimPartModelQuery.jsp";//工时选择页面
	private final String CLAIM_PART_BIG_MODEL_QUERY_URL ="/jsp/claim/authorization/claimPartBigModelQuery.jsp";//工时选择页面 Iverson add with 2010-11-09
	private final String CLAIM_PART_WATCH_UPDATE_URL = "/jsp/claim/authorization/claimPartWatchModify.jsp";//修改页面
	private final String WORK_ORDER_CAN_UPDATE_URL = "/jsp/claim/authorization/workOrderWatchModify.jsp";//修改页面
	private final String CLAIM_PART_BIG_WATCH_UPDATE_URL = "/jsp/claim/authorization/claimPartBigWatchModify.jsp";//修改配件大类页面
	
	//轿车三包信息变更新增配件监控
	private final String CLAIM_PART_WATCH_ADD_JC_URL = "/jsp/vehicleInfoManage/check/claimPartWatchAddJC.jsp";//新增页面
	private final ClaimCommonAction claimCommon = ClaimCommonAction.getInstance();
	private final String CLAIM_PART_MODEL_QUERY_JC_URL ="/jsp/vehicleInfoManage/check/claimPartModelQueryJC.jsp";//工时选择页面
	/**
	 * 
	* @Title: claimPartWatchInit 
	* @Description: TODO(监控配件维护初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimPartWatchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
		act.setOutData("LEVELLIST", list);
		act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("1",Constant.WR_MODEL_GROUP_TYPE_02,oemCompanyId));//配件车型组
		
		try {
			act.setForword(CLAIM_PART_WATCH_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}

	/**Iverson add with 2010-11-09
	 * @Title: claimPartWatchBigInit 
	 * @Description: TODO(监控配件大类维护初始化) 
	 * @param    
	 * @return void  
	 * @throws
	 */
	public void claimPartWatchBigInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
		act.setOutData("LEVELLIST", list);
		act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("1",Constant.WR_MODEL_GROUP_TYPE_02,oemCompanyId));//配件车型组
		
		try {
			act.setForword(CLAIM_PART_WATCH_BIG_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件大类维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: claimPartWatchQuery 
	* @Description: TODO(监控配件维护查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimPartWatchQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				
				// 从session中取得公司ID,add by zouchao 2010-07-19 begin
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
				// 2010-07-19 end 
				//配件代码
				String partCode = request.getParamValue("PART_CODE");//配件代码
				//配件名称
				String partName = request.getParamValue("PART_NAME");//配件名称
				//车型组id
				String wrgroupId = request.getParamValue("WRGROUP_ID");//索赔车型组ID
				//拼sql的查询条件
				if (Utility.testString(partCode)) {
					sb.append(" and tawap.part_code like ? ");
					params.add("%"+partCode+"%");
				}
				if (Utility.testString(partName)) {
					sb.append(" and tawap.part_name like ? ");
					params.add("%"+partName+"%");
				}
				if (Utility.testString(wrgroupId)) {
					sb.append(" and tawap.model_group = ? ");
					params.add(wrgroupId);
				}				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.claimPartWatchQuery(Constant.PAGE_SIZE, curPage,sb.toString(),params,oemCompanyId);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**Iverson add with 2010-11-09
	 * @Title: claimPartWatchBigQuery 
	 * @Description: TODO(监控配件维护大类查询) 
	 * @param    
	 * @return void  
	 * @throws
	 */
	public void claimPartWatchBigQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				// 从session中取得公司ID
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
				// 2010-07-19 end 
				//配件代码
				String partTypeCode = request.getParamValue("PARTTYPE_CODE");//配件大类代码
				//配件名称
				String partTypeName = request.getParamValue("PARTTYPE_NAME");//配件大类名称
				//拼sql的查询条件
				if (Utility.testString(partTypeCode)) {
					sb.append(" and tawat.PART_BIGTYPE_CODE like ? ");
					params.add("%"+partTypeCode+"%");
				}
				if (Utility.testString(partTypeName)) {
					sb.append(" and tawat.PART_BIGTYPE_NAME like ? ");
					params.add("%"+partTypeName+"%");
				}		
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.claimPartWatchBigQuery(Constant.PAGE_SIZE, curPage,sb.toString(),params,oemCompanyId);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件大类维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: claimPartWatchAddInit 
	* @Description: TODO(监控配件维护新增初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimPartWatchAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
			act.setOutData("LEVELLIST", list);
			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("0",Constant.WR_MODEL_GROUP_TYPE_02,oemCompanyId));
			act.setForword(CLAIM_PART_WATCH_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**Iverson add with 2010-11-09
	 * @Title: claimPartWatchBigAddInit
	 * @Description: TODO(监控配件大类维护新增初始化) 
	 * @param 
	 * @return void  
	 * @throws
	 */
	public void claimPartWatchBigAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
			act.setOutData("LEVELLIST", list);
			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("0",Constant.WR_MODEL_GROUP_TYPE_02,oemCompanyId));
			act.setForword(CLAIM_PART_WATCH_BIG_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件大类维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * Iverson add with 2010-11-09
	 * @Title: claimPartSelect 
	 * @Description: TODO(配件查询) 
	 * @param    
	 * @return void  
	 * @throws
	 */
	public void claimPartSelect(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("query"))){ //开始查询
				String partCode = request.getParamValue("partcode");//配件代码
				String partName = request.getParamValue("partname");//配件名称			
				if(Utility.testString(partCode)){
					sb.append(" and tppb.part_code like '%"+partCode+"%' \n");
				}
				if(Utility.testString(partName)){
					sb.append(" and tppb.part_name like '%"+partName+"%' \n");
				}
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.getPartById1(Constant.PAGE_SIZE,curPage,oemCompanyId,sb.toString());
				act.setOutData("ps", ps);
			}else{
				act.setForword(CLAIM_PART_MODEL_QUERY_URL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	
	/** Iverson add with 2010-11-09
	 * @Title: claimPartBigSelect 
	 * @Description: TODO(配件大类查询) 
	 * @param
	 * @return void  
	 * @throws
	 */
	public void claimPartBigSelect(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("query"))){ //开始查询
				String partTypeCode = request.getParamValue("parttypecode");//配件代码
				String partTypeName = Utility.getString(request.getParamValue("parttypename"));//配件名称			
				if(Utility.testString(partTypeCode)){
					sb.append(" and tppt.PARTTYPE_CODE like ? \n");
					params.add("%"+partTypeCode+"%");
				}
				if(Utility.testString(partTypeName)){
					sb.append(" and tppt.PARTTYPE_NAME like ? \n");
					params.add("%"+partTypeName+"%");
				}
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.getPartBigById(Constant.PAGE_SIZE,curPage, oemCompanyId,sb.toString(),params);
				act.setOutData("ps", ps);
			}else{
				act.setForword(CLAIM_PART_BIG_MODEL_QUERY_URL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件大类维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	
	/**
	 * 
	* @Title: claimPartWatchAdd 
	* @Description: TODO(监控配件维护新增) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimPartWatchAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrAuthmonitorpartPO addpo = null;
//		String errorCode = null;
		String errorExist = null;
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());//授权级别列表
			String[] level = new String[list.size()];
			for(int i=0; i<list.size(); i++){
				HashMap levlmap = (HashMap)list.get(i);
				level[i] = CommonUtils.checkNull(request.getParamValue(levlmap.get("APPROVAL_LEVEL_CODE").toString()));  //获得索赔基本参数对应的value
			}
			
			String wrgroupId = request.getParamValue("WRGROUP_ID");//配件车型组id
			String partCode = request.getParamValue("PART_CODE");//配件代码
			String partName = request.getParamValue("PART_NAME");//配件名称
//			长安项目监控配件和车型不需要关联：
//			List sellist = dao.getRelatingForPart(wrgroupId, partCode);
			//判断配件是否和车型组关联
//			if(sellist != null && sellist.size() > 0){
				//判断该配件是否在监控配件表中维护过
				List existlist = dao.getExistPO(wrgroupId, partCode);
				if(existlist != null && existlist.size() > 0){
					errorExist = partCode;
				}else{
					addpo = new TtAsWrAuthmonitorpartPO();
					//构造授权级别，多个用“,”分隔
					String levelStr = "";
					for(int i=0;i<level.length - 1;i++){
						if(Utility.testString(level[i])){
							levelStr += level[i];
						}
						if(Utility.testString(level[i+1])){
							levelStr += ",";
						}
					}
					if(Utility.testString(level[level.length - 1])){
						levelStr += level[level.length - 1];
					}
					//预授权监控配件表添加一条记录
					addpo.setApprovalLevel(levelStr);//授权级别
					addpo.setCreateBy(logonUser.getUserId());
					addpo.setCreateDate(new Date());
					addpo.setId(Long.parseLong(SequenceManager.getSequence("")));
					addpo.setPartName(partName);//配件名称
					addpo.setPartCode(partCode);//配件代码
					addpo.setModelGroup(Utility.getLong(wrgroupId));
					addpo.setOemCompanyId(oemCompanyId);
					dao.insert(addpo);
				}
//			}else{
//				errorCode = partCode;
//			}
//			act.setOutData("errorCode", errorCode);//配件不在选择的车型组中
			act.setOutData("errorExist", errorExist);//配件数据库已经存在
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**
	 * Iverson add with 2010-11-09
	 * @Title: claimPartBigWatchAdd 
	 * @Description: TODO(监控配件大类维护新增) 
	 * @param    
	 * @return void  
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimPartBigWatchAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrAuthmonitortypePO addpo = null;
		String errorExist = null;
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());//授权级别列表
			String[] level = new String[list.size()];
			for(int i=0; i<list.size(); i++){
				HashMap levlmap = (HashMap)list.get(i);
				level[i] = CommonUtils.checkNull(request.getParamValue(levlmap.get("APPROVAL_LEVEL_CODE").toString()));  //获得索赔基本参数对应的value
			}
			String partTypeId = request.getParamValue("PARTTYPE_ID");//配件大类名称
			String partTypeCode = request.getParamValue("PARTTYPE_CODE");//配件大类代码
			String partTypeName = request.getParamValue("PARTTYPE_NAME");//配件大类名称
			//判断该配件是否在监控配件表中维护过
			List existlist = dao.getExistBigPO(partTypeCode);
			if(existlist != null && existlist.size() > 0){
					errorExist = partTypeCode;
				}else{
					addpo = new TtAsWrAuthmonitortypePO();
					//构造授权级别，多个用“,”分隔
					String levelStr = "";
					for(int i=0;i<level.length - 1;i++){
						if(Utility.testString(level[i])){
							levelStr += level[i];
						}
						if(Utility.testString(level[i+1])){
							levelStr += ",";
						}
					}
					if(Utility.testString(level[level.length - 1])){
						levelStr += level[level.length - 1];
					}
					//预授权监控配件表添加一条记录
					addpo.setApprovalLevel(levelStr);//授权级别
					addpo.setCreateBy(logonUser.getUserId());
					addpo.setCreateDate(new Date());
					addpo.setId(Long.parseLong(SequenceManager.getSequence("")));
					addpo.setPartBigtypeId(Long.parseLong(partTypeId));//配件大类ID
					addpo.setPartBigtypeName(partTypeName);//配件大类名称
					addpo.setPartBigtypeCode(partTypeCode);//配件大类代码
					addpo.setOemCompanyId(oemCompanyId);
					dao.insert(addpo);
				}
			act.setOutData("errorExist", errorExist);//配件数据库已经存在
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	
	/**
	 * 
	* @Title: claimPartWatchUpdateInit 
	* @Description: TODO(监控配件维护修改初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimPartWatchUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());//授权级别列表
			String id=request.getParamValue("ID");
			HashMap hm = null;
			hm = dao.claimPartWatchQueryById(id,oemCompanyId);
			act.setOutData("LEVELLIST", list);//授权级别列表
			act.setOutData("SELMAP", hm);//对应的监控工时
			act.setForword(CLAIM_PART_WATCH_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**
	 * Iverson add with 2010-11-09
	 * @Title: claimPartBigWatchUpdateInit 
	 * @Description: TODO(监控配件大类维护修改初始化) 
	 * @param    
	 * @return void  
	 * @throws
	 */
	public void claimPartBigWatchUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());//授权级别列表
			String id=request.getParamValue("ID");
			HashMap hm = null;
			hm = dao.claimPartBigWatchQueryById(id,oemCompanyId);
			act.setOutData("LEVELLIST", list);//授权级别列表
			act.setOutData("SELMAP", hm);//对应的监控工时
			act.setForword(CLAIM_PART_BIG_WATCH_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件大类维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**
	 * 
	* @Title: claimPartWatchUpdate 
	* @Description: TODO(监控配件维护修改) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimPartWatchUpdate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrAuthmonitorpartPO selpo = null;
		TtAsWrAuthmonitorpartPO updatepo = null;
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());//授权级别列表
			String[] level = new String[list.size()];
			for(int i=0; i<list.size(); i++){
				HashMap levlmap = (HashMap)list.get(i);
				level[i] = CommonUtils.checkNull(request.getParamValue(levlmap.get("APPROVAL_LEVEL_CODE").toString()));  //获得索赔基本参数对应的value
			}
			String id = request.getParamValue("ID");//监控工时记录ID
			selpo = new TtAsWrAuthmonitorpartPO();
			selpo.setId(Utility.getLong(id));
			
			updatepo = new TtAsWrAuthmonitorpartPO();
			//构造授权级别，多个用“,”分隔
			String levelStr = "";
			for(int i=0;i<level.length - 1;i++){
				if(Utility.testString(level[i])){
					levelStr += level[i];
				}
				if(Utility.testString(level[i+1])){
					levelStr += ",";
				}
			}
			if(Utility.testString(level[level.length - 1])){
				levelStr += level[level.length - 1];
			}
			//预授权监控配件表修改po  
			updatepo.setApprovalLevel(levelStr);//授权级别
			updatepo.setUpdateBy(logonUser.getUserId());
			updatepo.setUpdateDate(new Date());
			dao.update(selpo, updatepo);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**
	 * Iverson add with 2010-11-09
	 * @Title: claimPartBigWatchUpdate 
	 * @Description: TODO(监控配件大类维护修改) 
	 * @param    
	 * @return void  
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimPartBigWatchUpdate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrAuthmonitortypePO selpo = null;
		TtAsWrAuthmonitortypePO updatepo = null;
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());//授权级别列表
			String[] level = new String[list.size()];
			for(int i=0; i<list.size(); i++){
				HashMap levlmap = (HashMap)list.get(i);
				level[i] = CommonUtils.checkNull(request.getParamValue(levlmap.get("APPROVAL_LEVEL_CODE").toString()));  //获得索赔基本参数对应的value
			}
			String id = request.getParamValue("ID");//监控工时记录ID
			selpo = new TtAsWrAuthmonitortypePO();
			selpo.setId(Utility.getLong(id));
			updatepo = new TtAsWrAuthmonitortypePO();
			//构造授权级别，多个用“,”分隔
			String levelStr = "";
			for(int i=0;i<level.length - 1;i++){
				if(Utility.testString(level[i])){
					levelStr += level[i];
				}
				if(Utility.testString(level[i+1])){
					levelStr += ",";
				}
			}
			if(Utility.testString(level[level.length - 1])){
				levelStr += level[level.length - 1];
			}
			//预授权监控配件表修改po  
			updatepo.setApprovalLevel(levelStr);//授权级别
			updatepo.setUpdateBy(logonUser.getUserId());
			updatepo.setUpdateDate(new Date());
			dao.update(selpo, updatepo);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: claimPartWatchDel 
	* @Description: TODO(监控配件维护逻辑删除) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimPartWatchDel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrAuthmonitorpartPO selpo = null;
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");//监控配件记录ID
			selpo = new TtAsWrAuthmonitorpartPO();
			selpo.setId(Utility.getLong(id));
			dao.delete(selpo);
			
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	
	/**
	 * Iverson add with 2010-11-09
	 * @Title: claimPartBigWatchDel 
	 * @Description: TODO(监控配件大类维护逻辑删除) 
	 * @param    
	 * @return void  
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimPartBigWatchDel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrAuthmonitortypePO selpo = null;
		TtAsWrAuthmonitortypePO updatepo = null;
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");//监控配件大类记录ID
			selpo = new TtAsWrAuthmonitortypePO();
			selpo.setId(Utility.getLong(id));
			updatepo = new TtAsWrAuthmonitortypePO();
			//预授权监控配件大类表修改po
			updatepo.setIsDel(Constant.IS_DEL);//逻辑删除表识：1
			updatepo.setUpdateBy(logonUser.getUserId());
			updatepo.setUpdateDate(new Date());
			dao.update(selpo, updatepo);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * add by kevinyin 20110616
	* @Title: claimPartWatchAddInit 
	* @Description: TODO(三包信息变更（监控配件维护新增初始化）轿车) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimPartWatchAddInitVehRuleChgJC(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
			act.setOutData("changeId", changeId);
			act.setOutData("partCode", partCode);
			act.setOutData("vin", vin);
			act.setOutData("LEVELLIST", list);
			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("0",Constant.WR_MODEL_GROUP_TYPE_02,oemCompanyId));
			act.setForword(CLAIM_PART_WATCH_ADD_JC_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**
	 * Iverson add with 2010-11-09
	 * @Title: claimPartSelect 
	 * @Description: TODO(配件查询) 
	 * @param    
	 * @return void  
	 * @throws
	 */
	public void claimPartSelectVehChangeForJC(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("query"))){ //开始查询
				String partCode = request.getParamValue("partcode");//配件代码
				String partName = request.getParamValue("partname");//配件名称			
				if(Utility.testString(partCode)){
					sb.append(" and tppb.part_code like '%"+partCode+"%' \n");
				}
				if(Utility.testString(partName)){
					sb.append(" and tppb.part_name like '%"+partName+"%' \n");
				}
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.getPartById1(Constant.PAGE_SIZE,curPage,oemCompanyId,sb.toString());
				act.setOutData("ps", ps);
			}else{
				act.setForword(CLAIM_PART_MODEL_QUERY_JC_URL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	/**
	 * 工单预授权设置
	 */
	
	public void autoOrderInit() {
			ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getOrderLevelList();
			act.setOutData("LEVELLIST", list);
			
			try {
				act.setForword(CLAIM_ORDER_WATCH_URL);
			} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
				logger.error(logonUser,e1);
				act.setException(e1);
			}		
			
		}
	/**
	 * 显示工单授权信息
	 */
	public void workOrderWatchQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			 //开始查询
				
				//维修类型编码
				String num = request.getParamValue("NUM");//维修类型编码
				//维修类型
				String codeDesc = request.getParamValue("CODE_DESC");//维修类型
				//拼sql的查询条件
				if (Utility.testString(num)) {
					sb.append(" and tawl.num like ? ");
					params.add("%"+num+"%");
				}
				if (Utility.testString(codeDesc)) {
					sb.append(" and tawl.code_desc like ? ");
					params.add("%"+codeDesc+"%");
				}
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.workOrderCanQuery(Constant.PAGE_SIZE, curPage,sb.toString(),params);
				act.setOutData("ps", ps);
			
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"工单预授权设置");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: workOrderUpdateInit 
	* @Description: TODO(工单预授权修改初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void workOrderUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getOrderLevelList();//授权级别列表
			String num=request.getParamValue("NUM");
			HashMap hm = null;
			hm = dao.workOrderCanByNum(num);
			act.setOutData("LEVELLIST", list);//授权级别列表
			act.setOutData("SELMAP", hm);//对应的监控工时
			act.setForword(WORK_ORDER_CAN_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"工单预授权维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**
	 * 
	* @Title: claimPartWatchUpdate 
	* @Description: TODO(工单授权维护修改) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void workOrderWatchUpdate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrWoorLevelPO selpo = null;
		TtAsWrWoorLevelPO updatepo = null;
		try {
			RequestWrapper request = act.getRequest();
			List list = dao.getOrderLevelList();//授权级别列表
			String[] level = new String[list.size()];
			for(int i=0; i<list.size(); i++){
				HashMap levlmap = (HashMap)list.get(i);
				level[i] = CommonUtils.checkNull(request.getParamValue(levlmap.get("APPROVAL_LEVEL_CODE").toString()));  //获得索赔基本参数对应的value
			}
			String num= request.getParamValue("NUM");//监控工时记录ID
			selpo = new TtAsWrWoorLevelPO();
			selpo.setNum(Utility.getInt(num));
			
			updatepo = new TtAsWrWoorLevelPO();
			//构造授权级别，多个用“,”分隔
			String levelStr = "";
			for(int i=0;i<level.length - 1;i++){
				if(Utility.testString(level[i])){
					levelStr += level[i];
				}
				if(Utility.testString(level[i+1])){
					levelStr += ",";
				}
			}
			if(Utility.testString(level[level.length - 1])){
				levelStr += level[level.length - 1];
			}
			//预授权监控配件表修改po  
			updatepo.setWoorLevel(levelStr);//授权级别
			updatepo.setUpdateDate(new Date());
			dao.update(selpo, updatepo);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"工单授权维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
}
