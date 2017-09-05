/**   
* @Title: QualityMain.java 
* @Package com.infodms.dms.actions.claim.basicData 
* @Description: TODO(索赔配件质保期维护Action) 
* @author wangjinbao   
* @date 2010-6-5 上午11:32:15 
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
import com.infodms.dms.dao.claim.basicData.QualityDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrModelPartGuaranteesPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: QualityMain 
 * @Description: TODO(索赔配件质保期维护) 
 * @author wangjinbao 
 * @date 2010-6-5 上午11:32:15 
 *  
 */
public class QualityMain {
	private Logger logger = Logger.getLogger(QualityMain.class);
	private final QualityDao dao = QualityDao.getInstance();
	private final String QUALITY_URL = "/jsp/claim/basicData/qualityIndex.jsp";//主页面（查询）
	private final String QUALITY_MODIFY_URL = "/jsp/claim/basicData/qualityModify.jsp";//修改页面
	private final String QUALITY_DETAIL_URL = "/jsp/claim/basicData/qualityDetail.jsp";//明细页面
	
	/**
	 * 
	* @Title: qualityInit 
	* @Description: TODO(索赔配件质保期维护查询初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void qualityInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(QUALITY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔配件质保期维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: qualityQuery 
	* @Description: TODO(索赔配件质保期维护查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void qualityQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				//代码列别
				String groupName = request.getParamValue("GROUP_NAME");//车型名称
				String groupCode = request.getParamValue("GROUP_CODE");//车型代码
				String flag = request.getParamValue("FLAG");//是否质保标识
				String flagStr = "";
				//拼sql的查询条件
				if (Utility.testString(groupName)) {
					sb.append(" and tvmg.group_name like ? ");
					params.add("%"+groupName+"%");
				}
				if (Utility.testString(groupCode)) {
					sb.append(" and tvmg.group_code like ? ");
					params.add("%"+groupCode+"%");
				}
				if(flag != null && "true".equals(flag)){
					flagStr = " in ";
				}else{
					flagStr = " not in ";
				}
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.qualityQuery(Constant.PAGE_SIZE, curPage,sb.toString(),params,flagStr);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔配件质保期维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: qualityUpdateInit 
	* @Description: TODO(索赔配件质保期维护修改初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void qualityUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String groupId=request.getParamValue("GROUP_ID");//车型ID
			List modelType = dao.getModelByIdType(new Long(groupId), Constant.PART_TYPE);//
			act.setOutData("MODELTYPE", modelType);//配件类型集合
			act.setOutData("GROUP_ID", groupId);//车型ID
			act.setForword(QUALITY_MODIFY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"索赔配件质保期维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: qualityUpdate 
	* @Description: TODO(索赔配件质保期维护修改) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void qualityUpdate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String modelId=request.getParamValue("MODEL_ID");//车型ID
			String[] gurnIds = request.getParamValues("GURN_ID");//质保ID数组
			String[] gurnMonths = request.getParamValues("GURN_MONTH");//质保时间(月)数组
			String[] gurnMiles = request.getParamValues("GURN_MILE");//质保里程(公里)数组
			String[] codeIds = request.getParamValues("CODE_ID");//配件类型数组
			//判断是否存在质保
			for(int i = 0 ;i < gurnIds.length; i++){
				if(Utility.testString(gurnIds[i])){
					TtAsWrModelPartGuaranteesPO selpo = new TtAsWrModelPartGuaranteesPO();
					TtAsWrModelPartGuaranteesPO updatepo = new TtAsWrModelPartGuaranteesPO();
					selpo.setGurnId(new Long(gurnIds[i]));
					updatepo.setGurnMile(Utility.getBigDecimal(gurnMiles[i]));
					updatepo.setGurnMonth(Utility.getBigDecimal(gurnMonths[i]));
					updatepo.setUpdateBy(logonUser.getUserId());
					updatepo.setUpdateDate(new Date());
					dao.updateModelPartGuarantees(selpo, updatepo);//修改
				}else{
					TtAsWrModelPartGuaranteesPO addpo = new TtAsWrModelPartGuaranteesPO();
					addpo.setCreateBy(logonUser.getUserId());
					addpo.setCreateDate(new Date());
					addpo.setGurnId(Long.parseLong(SequenceManager.getSequence("")));
					addpo.setGurnMile(Utility.getBigDecimal(gurnMiles[i]));
					addpo.setGurnMonth(Utility.getBigDecimal(gurnMonths[i]));
					addpo.setModelId(new Long(modelId));
					addpo.setPartType(new Integer(codeIds[i]));
					dao.insert(addpo);
				}
				
			}
			act.setOutData("success", "true");

		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"索赔配件质保期维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: qualityDetail 
	* @Description: TODO(索赔配件质保期维护详细) 
	* @param    
	* @return void  
	* @throws
	 */
	public void qualityDetail(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String groupId=request.getParamValue("GROUP_ID");//车型ID
			List modelType = dao.getModelByIdTypeDetail(new Long(groupId), Constant.PART_TYPE);//索赔配件质保期维护明细所需要的信息list
			act.setOutData("MODELTYPE", modelType);//配件类型集合
			act.setOutData("GROUP_ID", groupId);//车型ID
			act.setForword(QUALITY_DETAIL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"索赔配件质保期维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}

}
