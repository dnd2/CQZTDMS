/**   
* @Title: ClaimBasicParamsMain.java 
* @Package com.infodms.dms.actions.claim.basicData 
* @Description: 索赔基本参数设定Action 
* @author wangjinbao   
* @date 2010-5-26 上午11:42:34 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.basicData;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.basicData.ClaimBasicParamsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmDownParameterPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimBasicParamsMain 
 * @Description: 索赔基本参数设定Action
 * @author wangjinbao 
 * @date 2010-5-26 上午11:42:34 
 *  
 */
public class ClaimBasicParamsMain extends BaseAction{
	private Logger logger = Logger.getLogger(ClaimBasicParamsMain.class);
	private final ClaimBasicParamsDao dao = ClaimBasicParamsDao.getInstance();
	private final String CLAIM_BASIC_PARMS_URL = "/jsp/claim/basicData/paraindex.jsp";//主页面（查询）
	private final String CLAIM_BASIC_PARMS_ADD_URL = "/jsp/claim/basicData/parainAdd.jsp";//增加页面
	private final String CLAIM_BASIC_PARMS_MODIFY_URL = "/jsp/claim/basicData/paraModify.jsp";//修改页面
	/**
	 * 
	* @Title: claimBasicParmsInit 
	* @Description: 索赔基本参数设定初始化
	* @param         
	* @return void  返回类型 
	* @throws
	 */
	public void claimBasicParmsInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			List claimBasicType = dao.getClaimBasicParams(Constant.CLAIM_BASIC_PARAMETER);//索赔基本参数
			request.setAttribute("CLAIMBASIC", claimBasicType);//查询动态列的集合
			
			act.setForword(CLAIM_BASIC_PARMS_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔基本参数设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimBasicParmsQuery 
	* @Description: 索赔基本参数设定查询
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void claimBasicParmsQuery() {
		PageResult<Map<String, Object>> ps = dao.claimBasicParamsQuery(request,loginUser,Constant.PAGE_SIZE, getCurrPage());
		act.setOutData("ps", ps);
	}
	
	/**
	 * 
	* @Title: claimBasicParmsAddInit 
	* @Description: 索赔基本参数设定增加初始化
	* @param         
	* @return void  返回类型 
	* @throws
	 */
	public void claimBasicParmsAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			List claimBasicType = dao.getClaimBasicParams(Constant.CLAIM_BASIC_PARAMETER);//索赔基本参数
			request.setAttribute("CLAIMBASIC", claimBasicType);//查询动态列的集合
			
			act.setForword(CLAIM_BASIC_PARMS_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔基本参数设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 索赔基本参数设定增加
	* @Title: claimBasicParmsAdd 
	* @Description: 索赔基本参数设定增加（原需求）
	* @param          无 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimBasicParmsAddOLD(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId=GetOemcompanyId.getOemCompanyId(logonUser);  
		try {
			RequestWrapper request = act.getRequest();
			List claimBasicList = dao.getClaimBasicParams(Constant.CLAIM_BASIC_PARAMETER);//索赔基本参数
			String[] codenames = new String[claimBasicList.size()];
			String[] codevalues = new String[claimBasicList.size()];
			String[] codedescs = new String[claimBasicList.size()];
			for(int i=0; i<claimBasicList.size(); i++){
				HashMap tccode = (HashMap)claimBasicList.get(i);
				codenames[i] = tccode.get("CODE_ID").toString();                          //获得索赔基本参数对应的id
				codedescs[i] = tccode.get("CODE_DESC").toString();                        //获得索赔基本参数对应的desc
				codevalues[i] = request.getParamValue(tccode.get("CODE_ID").toString());  //获得索赔基本参数对应的value
			}
			//经销商code集合
			String dealerCodes = request.getParamValue("dealerName");//待查询的经销商code，以,隔开
			if (Utility.testString(dealerCodes)) {
				String[] dealerIdArr = dealerCodes.split(",");
				TmDownParameterPO tmdp = null;
				TmDownParameterPO tmadd = null;
				List list = null;
				List re = new ArrayList();
				for(int i=0;i<dealerIdArr.length;i++){
					String dealercode = dealerIdArr[i];
					TmDealerPO dealerpo = dao.getCodeToPO(dealercode);
					tmdp = new TmDownParameterPO();
					tmdp.setDealerId(dealerpo.getDealerId());
					list = dao.select(tmdp);
					if(list != null && list.size() > 0){
						re.add(dealercode + "[" +dealerpo.getDealerShortname()+"]");
					}else if(list.size() == 0){
						for(int j=0;j<codenames.length;j++){
							tmadd = new TmDownParameterPO();
							tmadd.setDownParaId(Long.parseLong(SequenceManager.getSequence("")));
							tmadd.setCreateBy(logonUser.getUserId());
							tmadd.setCreateDate(new Date());
							tmadd.setDealerId(dealerpo.getDealerId());
							tmadd.setParameterCode(codenames[j]);
							tmadd.setParameterValue(codevalues[j]);
							tmadd.setParameterDesc(codedescs[j]);
							tmadd.setOemCompanyId(oemCompanyId);
							dao.insert(tmadd);
						}
					}else{
					}
					
				}
				act.setOutData("returnValue", re);
				act.setOutData("success", "true");
			}else{
				throw new Exception("经销商代码为空！");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"索赔基本参数设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimBasicParmsAdd 
	* @Description: TODO(索赔基本参数设定新增)（新需求） 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimBasicParmsAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId=GetOemcompanyId.getOemCompanyId(logonUser);
		try {
			RequestWrapper request = act.getRequest();
			List claimBasicList = dao.getClaimBasicParams(Constant.CLAIM_BASIC_PARAMETER);//索赔基本参数
			String[] codenames = new String[claimBasicList.size()];
			String[] codevalues = new String[claimBasicList.size()];
			String[] codedescs = new String[claimBasicList.size()];
			for(int i=0; i<claimBasicList.size(); i++){
				HashMap tccode = (HashMap)claimBasicList.get(i);
				codenames[i] = tccode.get("CODE_ID").toString();                          //获得索赔基本参数对应的id
				codedescs[i] = tccode.get("CODE_DESC").toString();                        //获得索赔基本参数对应的desc
				codevalues[i] = request.getParamValue(tccode.get("CODE_ID").toString());  //获得索赔基本参数对应的value
			}
			//经销商code集合
			String dealerCodes = request.getParamValue("dealerName");//待查询的经销商code，以,隔开
			if (Utility.testString(dealerCodes)) {
				String[] dealerIdArr = dealerCodes.split(",");
				for(int i=0;i<dealerIdArr.length;i++){
					String dealercode = dealerIdArr[i];
					TmDealerPO dealerpo = dao.getCodeToPO(dealercode);
					
					TmDownParameterPO seltmdp = null;
					TmDownParameterPO retmdp = null;
					TmDownParameterPO tmdp = null;
					TmDownParameterPO tmaupdate = null;
					for(int j=0; j<codenames.length;j++){
						seltmdp = new TmDownParameterPO();
						tmdp = new TmDownParameterPO();
						tmaupdate = new TmDownParameterPO();
						//根据经销商id和基本参数的code查询
						seltmdp.setDealerId(dealerpo.getDealerId());
						seltmdp.setParameterCode(codenames[j]);
						
						retmdp = dao.getObjFromList(seltmdp);
						if(retmdp != null){//存在修改
							tmdp.setDownParaId(retmdp.getDownParaId());//确定索赔参数表的id
								
							tmaupdate.setParameterValue(codevalues[j]);
							tmaupdate.setUpdateBy(logonUser.getUserId());
							tmaupdate.setUpdateDate(new Date());
							tmaupdate.setOemCompanyId(oemCompanyId);
							dao.updateBasicParams(tmdp, tmaupdate);
						}else{//不存在新增
							tmaupdate.setDownParaId(Long.parseLong(SequenceManager.getSequence("")));
							tmaupdate.setCreateBy(logonUser.getUserId());
							tmaupdate.setCreateDate(new Date());
							tmaupdate.setDealerId(dealerpo.getDealerId());
							tmaupdate.setParameterCode(codenames[j]);
							tmaupdate.setParameterValue(codevalues[j]);
							tmaupdate.setParameterDesc(codedescs[j]);
							tmaupdate.setOemCompanyId(oemCompanyId);
							dao.insert(tmaupdate);
						}
					}					
				}
				act.setOutData("success", "true");
			}else{
				throw new Exception("经销商代码为空！");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"索赔基本参数设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimBasicParmsUpdateInit 
	* @Description: TODO(索赔基本参数修改前的初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimBasicParmsUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			List<Object> params = new LinkedList<Object>();
			StringBuffer sb = new StringBuffer();
			HashMap hm = null;
			List claimBasicType = dao.getClaimBasicParams(Constant.CLAIM_BASIC_PARAMETER);//索赔基本参数
			String dealercode = request.getParamValue("DEALER_CODE");//经销商编码参数
			TmDealerPO dealerpo = dao.getCodeToPO(dealercode);
			if (Utility.testString(dealercode)) {
				sb.append(" and td.dealer_code = ? ");//拼sql的查询条件：经销商代码
				params.add(dealercode);
			}
			PageResult<Map<String, Object>> rs = dao.claimBasicParamsQuery(Constant.PAGE_SIZE, 1,sb.toString(),params,Constant.CLAIM_BASIC_PARAMETER);
			List ls = rs.getRecords();
			if (ls!=null){
				if (ls.size()>0) {
					 hm = (HashMap)ls.get(0);
				}
			}
			request.setAttribute("DOWNPARAMETER", hm);//经销商PO
			request.setAttribute("DEALERPO", dealerpo);//经销商PO
			request.setAttribute("CLAIMBASIC", claimBasicType);//查询动态列的集合
			
			act.setForword(CLAIM_BASIC_PARMS_MODIFY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"索赔基本参数设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimBasicParmsUpdate 
	* @Description: TODO(索赔基本参数的修改) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimBasicParmsUpdate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			List claimBasicList = dao.getClaimBasicParams(Constant.CLAIM_BASIC_PARAMETER);//索赔基本参数
			String[] codenames = new String[claimBasicList.size()];
			String[] codevalues = new String[claimBasicList.size()];
			String[] codedescs = new String[claimBasicList.size()];
			for(int i=0; i<claimBasicList.size(); i++){
				HashMap tccode = (HashMap)claimBasicList.get(i);
				codenames[i] = tccode.get("CODE_ID").toString();                          //获得索赔基本参数对应的id
				codedescs[i] = tccode.get("CODE_DESC").toString();                        //获得索赔基本参数对应的desc
				codevalues[i] = request.getParamValue(tccode.get("CODE_ID").toString());//获得索赔基本参数对应的value
			}
			//经销商code
			String dealerCode = request.getParamValue("delearCode");//待查询的经销商code
			TmDealerPO dealerpo = dao.getCodeToPO(dealerCode);
			if (Utility.testString(dealerCode)) {
				TmDownParameterPO seltmdp = null;
				TmDownParameterPO retmdp = null;
				TmDownParameterPO tmdp = null;
				TmDownParameterPO tmaupdate = null;
				for(int i=0; i<codenames.length;i++){
					seltmdp = new TmDownParameterPO();
					tmdp = new TmDownParameterPO();
					tmaupdate = new TmDownParameterPO();
					//根据经销商id和基本参数的code查询
					seltmdp.setDealerId(dealerpo.getDealerId());
					seltmdp.setParameterCode(codenames[i]);
					
					retmdp = dao.getObjFromList(seltmdp);
					if(retmdp != null){//存在修改
						tmdp.setDownParaId(retmdp.getDownParaId());//确定索赔参数表的id
							
						tmaupdate.setParameterValue(codevalues[i]);
						tmaupdate.setUpdateBy(logonUser.getUserId());
						tmaupdate.setUpdateDate(new Date());
							
						dao.updateBasicParams(tmdp, tmaupdate);
					}else{//不存在新增
						tmaupdate.setDownParaId(Long.parseLong(SequenceManager.getSequence("")));
						tmaupdate.setCreateBy(logonUser.getUserId());
						tmaupdate.setCreateDate(new Date());
						tmaupdate.setDealerId(dealerpo.getDealerId());
						tmaupdate.setParameterCode(codenames[i]);
						tmaupdate.setParameterValue(codevalues[i]);
						tmaupdate.setParameterDesc(codedescs[i]);
						dao.insert(tmaupdate);
					}
				}
			}else{
				throw new Exception("经销商代码为空！");
			}
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"索赔基本参数设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}	
	/**
	 * 
	* @Title: claimBasicParmsUpdate 
	* @Description: TODO(索赔基本参数的修改) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimBasicParmsUpdate2(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			//经销商code
			String dealerCode = request.getParamValue("codes");//待查询的经销商code
			String value = request.getParamValue("value");//获取配件加价率
			String value2 = request.getParamValue("values");//获取配件加价率
			System.out.println(dealerCode+"--"+value+"======="+value2);
			String[] codes =dealerCode.split(",");
			for(int i=0;i<codes.length;i++){
			if(Utility.testString(codes[i])){
				//得到经销商ID
				TmDealerPO d = new TmDealerPO();
				d.setDealerCode(codes[i]);
				d = (TmDealerPO) dao.select(d).get(0);
				TmDownParameterPO p1 = new TmDownParameterPO();
				p1.setDealerId(d.getDealerId());
				p1.setParameterCode(Constant.CLAIM_BASIC_PARAMETER_09.toString());
				List<TmDownParameterPO> list = dao.select(p1);
				if(list ==null ||list.size()<1){//不存在,新增
					TmDownParameterPO	tmaupdate = new TmDownParameterPO();
					tmaupdate.setDownParaId(Long.parseLong(SequenceManager.getSequence("")));
					tmaupdate.setCreateBy(logonUser.getUserId());
					tmaupdate.setCreateDate(new Date());
					tmaupdate.setDealerId(d.getDealerId());
					tmaupdate.setParameterCode(Constant.CLAIM_BASIC_PARAMETER_09.toString());
					tmaupdate.setParameterValue(value.trim());
					tmaupdate.setParameterDesc("昌河配件加价率(%)");
					dao.insert(tmaupdate);
				}else{//存在就更新
					TmDownParameterPO p2 = new TmDownParameterPO();
					TmDownParameterPO p3 = new TmDownParameterPO();
					p2.setDealerId(d.getDealerId());
					p2.setParameterCode(Constant.CLAIM_BASIC_PARAMETER_09.toString());
					p3.setParameterValue(value.trim());
					dao.update(p2, p3);
				}
				//检查东安加价率
				TmDownParameterPO pp = new TmDownParameterPO();
				pp.setDealerId(d.getDealerId());
				pp.setParameterCode(Constant.CLAIM_BASIC_PARAMETER_08.toString());
				List<TmDownParameterPO> list2 = dao.select(pp);
				if(list2 ==null ||list2.size()<1){//不存在,新增
					TmDownParameterPO	tmaupdate = new TmDownParameterPO();
					tmaupdate.setDownParaId(Long.parseLong(SequenceManager.getSequence("")));
					tmaupdate.setCreateBy(logonUser.getUserId());
					tmaupdate.setCreateDate(new Date());
					tmaupdate.setDealerId(d.getDealerId());
					tmaupdate.setParameterCode(Constant.CLAIM_BASIC_PARAMETER_08.toString());
					tmaupdate.setParameterValue(value2.trim());
					tmaupdate.setParameterDesc("东安配件加价率(%)");
					dao.insert(tmaupdate);
				}else{//存在就更新
					TmDownParameterPO p2 = new TmDownParameterPO();
					TmDownParameterPO p3 = new TmDownParameterPO();
					p2.setDealerId(d.getDealerId());
					p2.setParameterCode(Constant.CLAIM_BASIC_PARAMETER_08.toString());
					p3.setParameterValue(value2.trim());
					dao.update(p2, p3);
				}
			}
			}
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"索赔基本参数设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}	
}
