/**   
* @Title: ClaimDowLoadMain.java 
* @Package com.infodms.dms.actions.claim.basicData 
* @Description: TODO(下发索赔工时ACTION) 
* @author wangjinbao   
* @date 2010-7-9 上午10:24:22 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.basicData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.basicData.ClaimDowLoadDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrWrlabinfoPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.dms.chana.actions.OSC11;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

/** 
 * @ClassName: ClaimDowLoadMain 
 * @Description: TODO(下发索赔工时ACTION) 
 * @author wangjinbao 
 * @date 2010-7-9 上午10:24:22 
 *  
 */
public class ClaimDowLoadMain {
	private Logger logger = Logger.getLogger(ClaimDowLoadMain.class);
	private final ClaimDowLoadDao dao = ClaimDowLoadDao.getInstance();
	private final String DOWN_LOAD_CODE_URL = "/jsp/claim/basicData/claimDownloadIndex.jsp";//主页面（查询）
	private final String DOWN_LOAD_CODE_DETAIL_URL = "/jsp/claim/basicData/claimDownloadDetail.jsp";//工时明细页面
	private final String DOWN_LOAD_CODE_ADDDETAIL_URL = "/jsp/claim/basicData/addDownloadDetail.jsp";//附加工时明细页面
	
	/**
	 * 
	* @Title: claimDownloadcodeInit 
	* @Description: TODO(下发索赔工时初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimDownloadcodeInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			//modify at 2010-07-19 start
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
			//modify end
			List list = dao.getClaimModel(companyId,Constant.WR_MODEL_GROUP_TYPE_01);
			act.setOutData("ADDLIST", list);//车型组
			act.setForword(DOWN_LOAD_CODE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"下发索赔工时");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimDowLoadSend 
	* @Description: TODO(下发方法) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimDowLoadSend(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//modify at 2010-07-19 start
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
			//modify end
			String[] wrgroupids = request.getParamValues("wrgroupIds");
			String dealerCodes = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			List<String> dcl = assembleDealerCode(dealerCodes);
			//经销商参数：
//			if(Utility.testString(dealercodes)){
//				String[] dealerArray = dealercodes.split(",");
//				for(int i=0;i<dealerArray.length;i++){
//					String dealercode = dealerArray[i];
//					TmDealerPO dealerpo = dao.getCodeToPO(dealercode);
//				}
//				
//			}else{
//				List dealerList = dao.getDealerList(companyId);//获取所有的经销商列表
//				
//			}
			if (wrgroupids.length > 1) {
				StringBuilder str = new StringBuilder();
				for(int i=1;i<wrgroupids.length;i++){
					TtAsWrWrlabinfoPO selpo = new TtAsWrWrlabinfoPO();
					TtAsWrWrlabinfoPO uppo = new TtAsWrWrlabinfoPO();
					selpo.setWrgroupId(new Long(wrgroupids[i]));
					selpo.setOemCompanyId(companyId);
					
					uppo.setIsSend(Constant.DOWNLOAD_CODE_STATUS_03);//已下发
					uppo.setUpdateBy(logonUser.getUserId());
					uppo.setUpdateDate(new Date());
					str.append(wrgroupids[i]).append(",");
					dao.update(selpo, uppo);
				}
				//车型组id列表 1000,2000,3000
				str.deleteCharAt(str.length() - 1);
				//调用下发接口 Start
				OSC11 os = new OSC11();
				os.execute(str.toString(), companyId, dcl);
				//调用下发接口 End
			}
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"下发索赔工时");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	private List<String> assembleDealerCode(String dealerCodes) {
		String[] dcs = dealerCodes.split(",");
		List<String> dcl = new ArrayList<String>();
		for (String dealerCode : dcs) {
			dcl.add(dealerCode);
		}
		return dcl;
	}
	
	/**
	 * 
	* @Title: claimLaborDetail 
	* @Description: TODO(车型组对应的索赔工时明细) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborDetail() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
			String id=request.getParamValue("WRGROUP_ID");//索赔车型组ID
			List addlist =  dao.getClaimLabourByWrgroupId(new Long(id),companyId);
			
			act.setOutData("ADDLIST", addlist);//索赔车型组对应的索赔工时
			act.setForword(DOWN_LOAD_CODE_DETAIL_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"下发索赔工时");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	/**
	 * 
	* @Title: addLaborDetail 
	* @Description: TODO(索赔工时对应的附加工时明细) 
	* @param    
	* @return void  
	* @throws
	 */
	public void addLaborDetail() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id=request.getParamValue("WRGROUP_ID");//索赔车型组ID
			List addlist =  dao.getAddLabourById(new Long(id));
			act.setOutData("ADDLIST", addlist);//索赔工时对应的附加工时
			act.setForword(DOWN_LOAD_CODE_ADDDETAIL_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"下发索赔工时");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}	

}
