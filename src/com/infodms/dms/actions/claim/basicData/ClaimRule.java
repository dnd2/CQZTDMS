/**********************************************************************
* <pre>
* FILE : ClaimRule.java
* CLASS : ClaimRule
*
* AUTHOR : PGM
*
* FUNCTION :三包规则维护.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-07-14| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ClaimRule.java,v 1.5 2011/10/18 02:31:38 xiongc Exp $
 */
package com.infodms.dms.actions.claim.basicData;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.serviceActivity.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.basicData.ClaimRuleDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrBackupListPO;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.po.TtAsWrRuleListTmpPO;
import com.infodms.dms.po.TtAsWrRulePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import flex.messaging.io.ArrayList;

/**
 * Function       :  三包规则维护
 * @author        :  PGM
 * CreateDate     :  2010-07-14
 * @version       :  0.1
 */
public class ClaimRule extends BaseImport{
	private Logger logger = Logger.getLogger(ClaimRule.class);
	private ClaimRuleDao dao = ClaimRuleDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ClaimRuleInitUrl = "/jsp/claim/basicData/claimRulePreIndex.jsp";//查询页面
	private final String ClaimRuleAddInitUrl= "/jsp/claim/basicData/claimRuleAdd.jsp";//新增页面
	private final String ClaimRuleupdateInitUrl= "/jsp/claim/basicData/claimRuleModfiy.jsp";//修改页面
	private final String ClaimRuleupdateUrl= "/jsp/claim/basicData/claimRuleDetailModfiy.jsp";//修改明细页面
	private final String ClaimRuleImportUrl= "/jsp/claim/basicData/claimRuleImport.jsp";//导入配件页面
	private final String ClaimRuleImportFailureUrl= "/jsp/claim/basicData/claimRuleImportFailure.jsp";//导入配件失败页面
	private final String BackUpClaimInitUrl = "/jsp/claim/basicData/BackUpClaimIndex.jsp";//查询页面
	
	
	
	/**
	 * Function       :  三包规则维护页面初始化
	 * @param         :  
	 * @return        :  claimRuleInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	public void claimRuleInit(){
		try {
			act.setForword(ClaimRuleInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  备件三包期维护页面初始化
	 * @param         :  
	 * @return        :  claimRuleInit
	 * @throws        :  Exception
	 * LastUpdate     :  2014-09-16
	 */
	public void backUpClaimInit(){
		try {
			act.setForword(BackUpClaimInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询三包规则维护中符合条件的信息，其中包括：
	 * @param         :  request-三包规则代码、三包规则名称、规则类型、状态
	 * @return        :  三包规则维护
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	public void claimRuleQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String ruleCode = request.getParamValue("ruleCode");        //三包规则代码
			String ruleName = request.getParamValue("ruleName");        //三包规则名称
			String ruleType = request.getParamValue("ruleType");        //规则类型
			String ruleStatus = request.getParamValue("ruleStatus");    //规则状态
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);  //公司ID
			TtAsWrRulePO rulePO =new TtAsWrRulePO();
			rulePO.setRuleCode(ruleCode);
			rulePO.setRuleName(ruleName);
			if(!"".equals(ruleType)&&null!=ruleType){
			rulePO.setRuleType(Integer.parseInt(ruleType));
			}
			if(!"".equals(ruleStatus)&&null!=ruleStatus){
			rulePO.setRuleStatus(Integer.parseInt(ruleStatus));
			}
			rulePO.setCompanyId(companyId);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getClaimRuleQuery(rulePO,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ClaimRuleInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包规则维");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  三包规则维护---新增页面初始化
	 * @param         :  
	 * @return        :  claimRuleAddInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	public void claimRuleAddInit(){
		try {
			act.setForword(ClaimRuleAddInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包规则维护新增页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  增加三包规则信息
	 * @param         :  request---三包规则代码、三包规则名称、三包规则类型、三包规则状态
	 * @return        :  三包规则维护
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	@SuppressWarnings("static-access")
	public void claimRuleAdd(){
		try {
			RequestWrapper request = act.getRequest();
			String id =SequenceManager.getSequence("");                 //ID
			String ruleCode = request.getParamValue("ruleCode");       //三包规则代码
			String ruleName = request.getParamValue("ruleName");       //三包规则名称
			String ruleType = request.getParamValue("ruleType");       //三包规则类型
			String ruleStatus = request.getParamValue("ruleStatus");   //三包规则状态
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser); //公司ID
			TtAsWrRulePO RulePO =new TtAsWrRulePO();
			RulePO.setId(Long.parseLong(id));
			RulePO.setRuleCode(ruleCode);
			RulePO.setRuleName(ruleName);
			RulePO.setRuleType(Integer.parseInt(ruleType));
			RulePO.setRuleStatus(Integer.parseInt(ruleStatus));
			RulePO.setCreateBy(logonUser.getUserId());
			RulePO.setCreateDate(new Date());
			RulePO.setUpdateBy(logonUser.getUserId());
			RulePO.setUpdateDate(new Date());
			RulePO.setCompanyId(companyId);//公司ID
			dao.claimRuleAdd(RulePO);
			this.claimRuleAddAfter(id); //跳转到修改页面，维护三包规则信息页面
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包规则信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  新增之后---查询三包规则信息
	 * @param         :  request-规则ID
	 * @return        :  三包规则信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	public void claimRuleAddAfter(String id){ 
		RequestWrapper request = act.getRequest();
		try {
			TtAsWrRulePO RulePO = new TtAsWrRulePO();
			RulePO=dao.claimRuleAddAfter(id);
			request.setAttribute("RulePO", RulePO);
			act.setForword(ClaimRuleupdateInitUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包规则信息修改查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  修改三包规则信息
	 * @param         :  request-规则ID
	 * @return        :  三包规则信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	@SuppressWarnings("static-access")
	public void claimRuleUpdate(){
		try {
			RequestWrapper request = act.getRequest();
			String	id = request.getParamValue("id");                      //ID
			String ruleName   = request.getParamValue("ruleName");         //三包规则名称
			String ruleStatus   = request.getParamValue("ruleStatus");     //三包规则状态
			TtAsWrRulePO RulePO = new TtAsWrRulePO();
			RulePO.setId(Long.parseLong(id));
			TtAsWrRulePO RulePOContent = new TtAsWrRulePO();
			RulePOContent.setRuleName(ruleName);
			RulePOContent.setRuleStatus(Integer.parseInt(ruleStatus));
			RulePOContent.setUpdateBy(logonUser.getUserId());
			RulePOContent.setUpdateDate(new Date());
			dao.claimRuleUpdate(RulePO,RulePOContent);
			act.setOutData("returnValue", 1);//returnValue 值：1，表示成功
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  维护明细
	 * @param         :  request-规则ID
	 * @return        :  三包规则信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	public void claimRuleUpdateQuery(){ 
		try {
			RequestWrapper request = act.getRequest();
			String ruleId=request.getParamValue("ruleId");//规则ID
			request.setAttribute("ruleId", ruleId);
			act.setForword(ClaimRuleupdateUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包规则信息修改明细页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询三包规则维护中符合条件的信息，其中包括：
	 * @param         :  request-配件代码、三包月份、三包里程
	 * @return        :  三包规则明细维护
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	public void claimRuleDetailQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String ruleId = request.getParamValue("ruleId");                //规则ID
			String partCode = request.getParamValue("partCode");            //配件代码
			String partName = request.getParamValue("partName");            //配件名称
			String claimMonth = request.getParamValue("claimMonth");        //三包月份
			String claimMelieage = request.getParamValue("claimMelieage");  //三包里程
			String partWarType = request.getParamValue("partWarType");
			TtAsWrRuleListPO RuleListPO =new  TtAsWrRuleListPO();
			if(!"".equals(ruleId)&&null!=ruleId){
				RuleListPO.setRuleId(Long.parseLong(ruleId));
			}
			if(!"".equals(partCode)&&null!=partCode){
				RuleListPO.setPartCode(partCode);
			}
			if(!"".equals(partName)&&null!=partName){
				RuleListPO.setPartName(partName);
			}
			if(!"".equals(claimMonth)&&null!=claimMonth){
				RuleListPO.setClaimMonth(Integer.parseInt(claimMonth));
			}
			if(!"".equals(claimMelieage)&&null!=claimMelieage){
				RuleListPO.setClaimMelieage(Double.parseDouble(claimMelieage));
			}
			if (Utility.testString(partWarType)) {
				RuleListPO.setPartWarType(partWarType);
			}
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.claimRuleDetailQuery(RuleListPO,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
		    request.setAttribute("ruleId", ruleId);
			act.setForword(ClaimRuleupdateUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包规则维");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void backUpClaimDetailQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String partCode = request.getParamValue("partCode");            //配件代码
			String partName = request.getParamValue("partName");            //配件名称
			String claimMonth = request.getParamValue("claimMonth");        //三包月份
			String claimMelieage = request.getParamValue("claimMelieage");  //三包里程
			String partWarType = request.getParamValue("partWarType");
			TtAsWrBackupListPO backUpListPO =new  TtAsWrBackupListPO();
			if(!"".equals(partCode)&&null!=partCode){
				backUpListPO.setPartCode(partCode);
			}
			if(!"".equals(partName)&&null!=partName){
				backUpListPO.setPartName(partName);
			}
			if(!"".equals(claimMonth)&&null!=claimMonth){
				backUpListPO.setClaimMonth(Integer.parseInt(claimMonth));
			}
			if(!"".equals(claimMelieage)&&null!=claimMelieage){
				backUpListPO.setClaimMelieage(Double.parseDouble(claimMelieage));
			}
			if (Utility.testString(partWarType)) {
				backUpListPO.setPartWarType(partWarType);
			}
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.backUpClaimDetailQuery(backUpListPO,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"备件三包维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  新增之后---查询三包规则信息
	 * @param         :  request-规则ID
	 * @return        :  三包规则信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	public void claimRuleUpdateInit(){ 
		RequestWrapper request = act.getRequest();
		String id=request.getParamValue("id");
		try {
			TtAsWrRulePO RulePO = new TtAsWrRulePO();
			RulePO=dao.claimRuleAddAfter(id);
			request.setAttribute("RulePO", RulePO);
			
			act.setForword(ClaimRuleupdateInitUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包规则信息修改查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 三包规则维护删除
	 * @author KFQ
	 * @serialData 2013-4-2 14:04
	 */
	@SuppressWarnings("unchecked")
	public void claimRuleDelete(){
		RequestWrapper request = act.getRequest();
		String id=request.getParamValue("did");
		System.out.println(id);
		try {
//		TtAsWrRuleListPO  lp = new TtAsWrRuleListPO();
//		lp.setRuleId(Long.valueOf(id));
//		dao.delete(lp);
		TtAsWrRulePO rp = new TtAsWrRulePO();
		rp.setId(Long.valueOf(id));
		TtAsWrRulePO rp2 = new TtAsWrRulePO();
		rp2.setRuleStatus(Constant.STATUS_DISABLE);
		//dao.delete(rp);
		dao.update(rp, rp2);
		act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"三包规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	/**
	 * Function       :  维护明细
	 * @param         :  request-规则ID
	 * @return        :  三包规则信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	public void claimRuleImport(){ 
		try {
			RequestWrapper request = act.getRequest();
	    	String ruleId=request.getParamValue("ruleId");//规则ID
	    	request.setAttribute("ruleId", ruleId);
			act.setForword(ClaimRuleImportUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件导入页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  服务活动管理---VIN导入清单
	 * @param         :  
	 * @return        :  serviceActivityVinImportOption
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public void claimRulePartsImportOption(){
		try {
			//获得本地文件
			RequestWrapper request = act.getRequest();
			String ruleId=request.getParamValue("ruleId");//规则ID
			TtAsWrRuleListPO RuleListPO = new TtAsWrRuleListPO();
			if (null!=ruleId&&!"".equals(ruleId)) {
				RuleListPO.setRuleId(Long.parseLong(ruleId));
			}
			TtAsWrRuleListTmpPO RuleListTmpPO = new TtAsWrRuleListTmpPO();
			dao.claimRulePartsImportDelete(RuleListTmpPO);
			//dao.claimRulePartsImportDelete(RuleListPO);//调用删除方法，在导入配件清单之前，先清空临时表

			long maxSize=1024*1024*5;
			int errNum=insertIntoTmp(request, "importFile",4,3,maxSize);
			if(errNum!=0){
				List<ExcelErrors> errorList=new ArrayList();
				ExcelErrors ees=new ExcelErrors();
				ees.setRowNum(0);
				switch (errNum) {
				case 1:
					ees.setErrorDesc("文件列数过多");
					break;
				case 2:
					ees.setErrorDesc("空行不能大于三行");
					break;
				case 3:
					ees.setErrorDesc("文件不能为空");
					break;
				case 4:
					ees.setErrorDesc("文件不能为空");
					break;
				case 5:
					ees.setErrorDesc("文件不能大于"+maxSize);
					break;
				default:
					break;
				}
				errorList.add(ees);
				act.setOutData("errorList", errorList);
			    request.setAttribute("ruleId", ruleId);
			    if(1==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ClaimRuleImportFailureUrl);
				}
			    else if(2==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ClaimRuleImportFailureUrl);
				}
			    else if(3==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ClaimRuleImportFailureUrl);
				}
			    else if(4==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ClaimRuleImportFailureUrl);
				}
			    else if(5==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ClaimRuleImportFailureUrl);
				}
				else{
					act.setForword(ClaimRuleupdateUrl);
				}
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTtAsWrRuleList(list,ruleId);
			}
		}catch(Exception e)
		{   
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"VIN清单导入");
			logger.error(logonUser,e1);
			act.setException(e1);
		} 	
	}
	/*
	 * 把所有导入记录插入TT_AS_WR_RULE_LIST临时表
	 */
	private void insertTtAsWrRuleList(List<Map> list,String ruleId) throws Exception{
		RequestWrapper request = act.getRequest();
		boolean fl=true;
		StringBuffer sb=new StringBuffer();
		if(null==list){
			list=new ArrayList();
		}
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while(it.hasNext()){
				key=(String)it.next();
				Cell[] cells=(Cell[])map.get(key);
				//parseCells(key, cells,ruleId);
				int partCode =cells[0].getContents().length();
				if(partCode >50){
					sb.append("配件代码超过20位！");
					fl=false;
					break;
				}
				int partName =cells[1].getContents().length();
				if(partName >200){
					sb.append("配件名称超过200位！");
					fl=false;
					break;
				}
				int claimMonth =cells[2].getContents().length();
				if(claimMonth >10){
					sb.append("三包月份超过10位！");
					fl=false;
					break;
				}
				int claimMelieage =cells[3].getContents().length();
				if(claimMelieage >12){
					sb.append("三包里程超过12位！");
					fl=false;
					break;
				}
			}
		}
		if(fl==true){
			for(int i=0;i<list.size();i++){
				Map map=list.get(i);
				if(null==map){
					map=new HashMap<String, Cell[]>();
				}
				Set<String> keys=map.keySet();
				Iterator it=keys.iterator();
				String key="";
				while(it.hasNext()){
					key=(String)it.next();
					Cell[] cells=(Cell[])map.get(key);
					parseCells(key, cells,ruleId);
				}
			}
			request.setAttribute("ruleId", ruleId);
			act.setForword(ClaimRuleupdateUrl);
		}else{
			ExcelErrors ees=new ExcelErrors();
			request.setAttribute("ees", ees);
			request.setAttribute("sb", sb.toString());
			act.setForword(ClaimRuleImportFailureUrl);
		}
	}
	/*
	 * 每一行插入TT_AS_WR_RULE_LIST临时表
	 * 数字只截取30位
	 */
	private void parseCells(String rowNum,Cell[] cells,String ruleId) throws Exception{
		   TtAsWrRuleListTmpPO RuleListPO=new TtAsWrRuleListTmpPO();
		    StringBuffer sb=new StringBuffer();
		    if(cells[0].getContents().equals("")||cells[0].getContents()==null){
				sb.append("配件代码为空！"+",");
			}else{
				RuleListPO.setPartCode(subCell(cells[0].getContents().trim()));//配件代码
			}
		    if(cells[1].getContents().equals("")||cells[1].getContents()==null){
				sb.append("配件名称为空！"+",");
			}else{
				RuleListPO.setPartName(subCell(cells[1].getContents().trim()));//配件名称
			}
			if(cells[2].getContents().equals("")||cells[2].getContents()==null){
				sb.append("三包月份为空！"+",");
			}else{
				RuleListPO.setClaimMonth(Long.parseLong(subCell(cells[2].getContents().trim())));//三包月份
			}
			if(cells[3].getContents().equals("")||cells[3].getContents()==null){
				sb.append("三包里程为空！"+",");
			}else{
				RuleListPO.setClaimMelieage(Double.parseDouble(subCell(cells[3].getContents().trim())));//三包里程
			}
			RuleListPO.setId(Long.parseLong(SequenceManager.getSequence("")));//ID
			RuleListPO.setRuleId(Long.parseLong(ruleId));                    //规则ID
			RuleListPO.setUpdateBy(logonUser.getUserId());
			RuleListPO.setUpdateDate(new Date());
			RuleListPO.setCreateBy(logonUser.getUserId());
			RuleListPO.setCreateDate(new Date());
			String ss=null;
			if(sb.toString().lastIndexOf(",")!=-1){
				if(sb.toString().lastIndexOf(",")==sb.toString().length()-1){
					ss=sb.toString().substring(0, sb.toString().lastIndexOf(","));
				}else{
					ss=sb.toString();
				}
			}
			//RuleListPO.setErrorRemark(ss);               //错误原因
			dao.claimRulePartsImportAdd(RuleListPO);
		    dao.claimRulePartsImportAddMerge(RuleListPO); //调用新增导入方法 
	}
	/*
	 * 将输入字符截取最多30位
	 */
    private String subCell(String orgAmt){
		String newAmt="";
		if(null==orgAmt||"".equals(orgAmt)){
			return newAmt;
		}
		if(orgAmt.length()>20){
			newAmt=orgAmt.substring(0,20);
		}else{
			newAmt=orgAmt;
		}
		return newAmt;
	}
	 /**
	 * Function       :  修改导入的配件信息中的三包月份、三包里程
	 * @param         :  request-规则ID
	 * @return        :  修改导入的配件信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	@SuppressWarnings("static-access")
	public void claimRuleUpdateImportParts(){
		try {
			RequestWrapper request = act.getRequest();
			String id =request.getParamValue("id");                        //ID
			String claimMonth =request.getParamValue("claimMonth");        //三包月份
			String claimMelieage =request.getParamValue("claimMelieage");  //三包里程
			if (id!=null&&!"".equals(id)) {
				String [] idArray = id.split(",");                         //取得所有ID放在数组中
				String [] claimMonthArray =  claimMonth.split(",");        //取得所有三包月份放在数组中
				String [] claimMelieageArray = claimMelieage.split(",");   //取得所有三包里程放在数组中
				TtAsWrRuleListPO  RuleListPOContent  =new TtAsWrRuleListPO();
				RuleListPOContent.setUpdateBy(logonUser.getUserId());
				RuleListPOContent.setUpdateDate(new Date());
				dao.claimRuleUpdateImportParts(idArray,claimMonthArray,claimMelieageArray,RuleListPOContent);
				act.setOutData("success", "true");
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"修改导入的配件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("static-access")
	public void backUpClaimUpdateImportParts(){
		try {
			RequestWrapper request = act.getRequest();
			String id =request.getParamValue("id");                        //ID
			String claimMonth =request.getParamValue("claimMonth");        //三包月份
			String claimMelieage =request.getParamValue("claimMelieage");  //三包里程
			if (id!=null&&!"".equals(id)) {
				String [] idArray = id.split(",");                         //取得所有ID放在数组中
				String [] claimMonthArray =  claimMonth.split(",");        //取得所有三包月份放在数组中
				String [] claimMelieageArray = claimMelieage.split(",");   //取得所有三包里程放在数组中
				TtAsWrBackupListPO  backUpListPOContent  =new TtAsWrBackupListPO();
				backUpListPOContent.setUpdateBy(logonUser.getUserId());
				backUpListPOContent.setUpdateDate(new Date());
				dao.backUpClaimUpdateImportParts(idArray,claimMonthArray,claimMelieageArray,backUpListPOContent);
				act.setOutData("success", "true");
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"修改导入的备件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  删除添加的配件
	 * @param         :  request-规则ID
	 * @return        :  修改导入的配件信息
	 * @throws        :  Exception
	 * LastUpdate     :  2011-01-11
	 */
	@SuppressWarnings("static-access")
	public void claimRuleDelImportParts(){
		try {
			RequestWrapper request = act.getRequest();
			String id =CommonUtils.checkNull(request.getParamValue("delIdValue"));
			if (!"".equals(id)) {
				String [] idArray = id.split(",");                         //取得所有ID放在数组中
				for(int i=0;i<idArray.length;i++){
					TtAsWrRuleListPO  RuleListPOContent  =new TtAsWrRuleListPO();
					RuleListPOContent.setId(Long.valueOf(idArray[i]));
					dao.delete(RuleListPOContent);
				}
				act.setOutData("success", "true");
			}else {
				act.setOutData("success", "false");
				act.setOutData("msg", "未选择数据！");
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"修改导入的配件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	 /**
	 * Function       :  根据配件代码、三包月份、三包里程查询出来的信息，进行下载
	 * @param         :  request-规则ID
	 * @return        :  下载配件信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-14
	 */
	/**
	 * @author KFQ
	 * 将以前的下载文件进行写死。形成模板文件供客户下载
	 * @serialData 2013-4-2 15:22
	 */
	@SuppressWarnings("unchecked")
	public void exportExcel(){
		try {
		RequestWrapper request = act.getRequest();
		String ruleId = request.getParamValue("ruleId");                     //规则ID
		String[] head=new String[4];
		head[0]="配件代码";
		head[1]="配件名称";
		head[2]="三包月份";
		head[3]="三包里程";
		List list1=new ArrayList();
				String[]detail=new String[4];
				detail[0]=("CV8057-0106");
				detail[1]=("组合仪表总成(C118/1.6L)");
				detail[2]=("36");
				detail[3]=("80000");
				list1.add(detail);
			com.infodms.dms.actions.claim.basicData.ToExcel.toExcel(ActionContext.getContext().getResponse(), request, head, list1);
	    request.setAttribute("ruleId", ruleId);
	    act.setForword(ClaimRuleupdateUrl);	
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包规则明细下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
}